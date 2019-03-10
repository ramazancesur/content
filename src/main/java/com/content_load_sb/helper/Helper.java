package com.content_load_sb.helper;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dto.DiscInfo;
import com.content_load_sb.fileops.ChannelTools;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by asd on 13.11.2017.
 */
public class Helper {
    private static Helper instance;
    private Logger LOGGER = LoggerFactory.getLogger(Helper.class);

    private Helper() {
    }

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        Class[] classArray;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassPath classPath = ClassPath.from(classLoader);
        ImmutableList<ClassPath.ClassInfo> lstClassInfo = classPath.getTopLevelClasses(packageName).asList();
        classArray = new Class[lstClassInfo.size()];
        int counter = 0;
        for (ClassPath.ClassInfo classInfo : lstClassInfo) {
            Class c = Class.forName(classInfo.getName());
            classArray[counter] = c;
            counter++;
        }
        return classArray;
    }


    public Properties getRestrictProp(String propertiesFile, String restrict) {
        Resource resource = new ClassPathResource(propertiesFile);
        Properties properties = new Properties();
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();
                String value = props.getProperty(name);
                // now you have name and value
                if (name.startsWith(restrict)) {
                    // this is the app name. Write yor code here
                    properties.put(name, value);
                }
            }
            return properties;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }


    public List<DiscInfo> discDriveList(){
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();
        List<DiscInfo> lstDiscInfo= new LinkedList<>();
        for(File path:paths)
        {
            DiscInfo discInfo= new DiscInfo();
            discInfo.setDiscPath(path.getPath());
            discInfo.setDiscType(fsv.getSystemTypeDescription(path));
            lstDiscInfo.add(discInfo);
        }
        return lstDiscInfo;
    }

    public Map<String, Object> readProperties(String propertiesFile) {
        Resource resource = new ClassPathResource(propertiesFile);
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            return properties.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue())
                    );
        } catch (IOException e) {
            return new HashMap<String, Object>();
        }
    }
    public void createFileRemeout(SmbFile remoteFile, File localFile) {
        final RateLimiter rateLimiter = RateLimiter.create(Setting.getInstance().getFileUploadLimit());
        InputStream in = null;
        OutputStream out = null;
        try {
            remoteFile.connect(); //Try to connect

            in = new BufferedInputStream(new FileInputStream(localFile));
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));

            byte[] buffer = new byte[4096];
            int len = 0; //Read length
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                rateLimiter.tryAcquire(buffer.length);
                out.write(buffer, 0, len);
            }
            out.flush();//The refresh buffer output stream
        } catch (Exception e) {
            String msg = "The error occurred: " + e.getLocalizedMessage();
            System.out.println(msg);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public <T extends Object> String dataToJson(List<T> dataList) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(dataList, listType);
        return json;
    }


    public <T> List<T> jsonToData(Class<T> clazz, String strJson) {
        List<T> lst = new LinkedList<T>();
        try {
            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });

            Gson gson = builder.create();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(strJson);

            if (jsonElement instanceof JsonArray) {
                for (final JsonElement json : jsonElement.getAsJsonArray()) {
                    T entity = gson.fromJson(json, clazz);
                    lst.add(entity);
                }

            } else if (jsonElement instanceof JsonObject) {
                T entity = gson.fromJson(jsonElement, clazz);
                lst.add(entity);
            }
            return lst;

        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<T>();
        }

    }


    /*
        Ramazan CESUR 14 Kasım 2017
       *** Verilen 2 liste arasındaki farkı bulup bir tane listeye atmayı sağlar ***
     */
    public <E> List<E> diffirentTwoList(List<E> lstMainData, List<E> lstDifferancableData) {
        return lstMainData.parallelStream().filter(o1 -> lstDifferancableData.parallelStream().
                noneMatch(o2 -> o2.hashCode() == o1.hashCode()))
                .collect(Collectors.toList());
    }

    public EnumUtil.RunModeEnum callRunMode() {
        String destinationFilePath = Setting.getInstance().getTargetFileLocation();
        File fileCheck = new File(destinationFilePath);
        String[] fileCheckFiles = fileCheck.list();
        if (fileCheck.exists() == false || fileCheckFiles == null || fileCheckFiles.length == 0) {
            return EnumUtil.RunModeEnum.INITIAL;
        } else {
            final boolean[] flag = {false};
            List<File> lstSubFiles = Arrays.asList(fileCheck.listFiles());
            lstSubFiles.forEach(file -> {
                if (file.getPath().contains("localdata.kife")) {
                    flag[0] = true;
                }
            });

            if (flag[0] == false) {
                try {
                    FileUtils.deleteDirectory(fileCheck);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return EnumUtil.RunModeEnum.INITIAL;
            }
        }
        return EnumUtil.RunModeEnum.DELTA;
    }

    public boolean fileCopy(File sourceFile, File targerFile) {
        boolean flag = true;
        try {
            final InputStream input = new FileInputStream(sourceFile);
            final OutputStream output = new FileOutputStream(targerFile);
            final ReadableByteChannel inputChannel = Channels.newChannel(input);
            final WritableByteChannel outputChannel = Channels.newChannel(output);
            ChannelTools.fastChannelCopy(inputChannel, outputChannel);
            inputChannel.close();
            outputChannel.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public String getFileParentPathTail(String fileParentPathTail) {
        String fileLocation = "";
        if (OsDetector.isWindows()) {
            fileLocation = Setting.getInstance().getNetworkDisk() + Setting.getInstance().getSystemFileSeparator() + fileParentPathTail.replace("/", "\\");
        } else {
            fileLocation = Setting.getInstance().getNetworkDisk() + Setting.getInstance().getSystemFileSeparator() + fileParentPathTail.replace("/", "\\");
        }
        return fileLocation;
    }

    public boolean fileCopy(DataInputStream dataInputStream, OutputStream outputStream) {
        boolean flag = true;
        try {
            final ReadableByteChannel inputChannel = Channels.newChannel(dataInputStream);
            final WritableByteChannel outputChannel = Channels.newChannel(outputStream);
            ChannelTools.fastChannelCopy(inputChannel, outputChannel);
            inputChannel.close();
            outputChannel.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            flag = false;
        }
        return flag;
    }

    // Pararel stream yapmayacan
    public synchronized List<File> removeEmptyDirectoryByFile(String filePath) {
        List<File> lstFile = new LinkedList<>();
        Path path = Paths.get(filePath);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isDirectory() && attrs.size() == 0) {
                        lstFile.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> lstSortedData = reverseList(lstFile);
        return lstSortedData;
    }


    public byte[] readFileByte(String filePath) {
        Path path = Paths.get(filePath);
        try {
            byte[] data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T extends Serializable> LinkedList<T> reverseList(List<T> lstData) {
        LinkedList<T> reversedList = new LinkedList<>();
        lstData.stream()
                .collect(Collectors.toCollection(ArrayDeque::new)) // or LinkedList
                .descendingIterator()
                .forEachRemaining(x -> {
                    reversedList.add(x);
                });
        return reversedList;
    }

    public Long convertToByte(float value, EnumUtil.MaxUploadSize uploadSize) {
        Long datasize = 0l;
        if (uploadSize == EnumUtil.MaxUploadSize.GB) {
            datasize = (long) (value * 1024 * 1024 * 1034 / 8);
        } else if (uploadSize == EnumUtil.MaxUploadSize.MB) {
            datasize = (long) (value * 1024 * 1024 / 8);
        } else {
            datasize = (long) (value * 1024 / 8);
        }
        return datasize;
    }

    public String readFileJson(String filePath) {
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine = "";
            while ((sCurrentLine = br.readLine()) != null) {
                jsonData.append(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }


    public <T extends Object> boolean writeJson(List<T> lstHashPath, String targetLocation) {
        boolean flag = true;
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<T> data = lstHashPath;
            // Convert object to JSON string and save into a file directly
            mapper.writeValue(new File(targetLocation), lstHashPath);
            /*
            // Convert object to JSON string
            String jsonInString = mapper.writeValueAsString(staff);
            System.out.println(jsonInString);
            */

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    public String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMo = sizeKb * sizeKb;
        float sizeGo = sizeMo * sizeKb;
        float sizeTerra = sizeGo * sizeKb;


        if (size < sizeMo)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGo)
            return df.format(size / sizeMo) + " Mb";
        else if (size < sizeTerra)
            return df.format(size / sizeGo) + " Gb";

        return "";
    }

}
