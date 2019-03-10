package com.content_load_sb.fileops;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.helper.EnumUtil;
import com.content_load_sb.helper.HashMapReader;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persist.entity.DataFile;
import com.content_load_sb.service.interfaces.IDirectoryService;
import com.content_load_sb.service.interfaces.IFileService;
import com.content_load_sb.service.interfaces.ITargetFileService;
import com.content_load_sb.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by asd on 13.11.2017.
 */
@Component
public class TransferMain {
    Helper helper = Helper.getInstance();
    Utils utils = new Utils();
    Setting setting = Setting.getInstance();
    @Autowired
    private IFileService fileService;
    @Autowired
    private IDirectoryService directoryService;

    @Autowired
    private ITargetFileService targetFileService;

    private void copyDirectory() {
        File sourcefile = new File(setting.getSourceDiskLocation());
        File targetFile = new File(setting.getTargetFileLocation());
        try {
            if (!targetFile.exists()) {
                Files.createDirectory(targetFile.toPath());
                directoryService.createDirectoryData(targetFile.toPath(), sourcefile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Database işlemlerini CopyFileVisitor sınıfında yapıyoruz
            Files.walkFileTree(sourcefile.toPath(), new CopyFileVisitor(sourcefile.toPath(), targetFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void transferStart() throws IOException {
        long start = System.nanoTime();
        EnumUtil.RunModeEnum runMode = helper.callRunMode();
        if (runMode == EnumUtil.RunModeEnum.INITIAL) {
            System.out.println("Initial Loading starting...");
        } else if (runMode == EnumUtil.RunModeEnum.DELTA) {
            System.out.println("Delta Loading starting...");
            List<HashPath> lstHashDlMachine = helper.jsonToData(HashPath.class, helper.readFileJson(setting.getDlHashFileLocation()));

            List<HashPath> lstHashSourceDisk = HashMapReader.getInstance().getLstHashPath();
            List<HashPath> lstDeletingData = helper.diffirentTwoList(lstHashDlMachine, lstHashSourceDisk);

            lstDeletingData.parallelStream()
                    .forEach(hashPath -> {
                        File file = new File(setting.getTargetDiskRootPath() + hashPath.getPathTail());
                        try {
                            Files.deleteIfExists(file.toPath());
                            lstHashDlMachine.remove(hashPath);
                            List<DataFile> lstDataFile = fileService.findByPath(file.getPath().substring(Setting.getInstance().getTargetDiskRootPath().length()));
                            lstDataFile.parallelStream()
                                    .filter(x -> x.getOid() != null || x.getOid() != 0)
                                    .forEach(dataFile -> {
                                        try {
                                            fileService.remove(dataFile);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
   /*
          YAVAŞ OLDUĞU İÇİN BU YÖNTEMDEN VAZGEÇİLMİŞTİR
          List<HashPath>lstAddingData =   Helper.getInstance().diffirentTwoList(lstHashSourceDisk, lstHashDlMachine);
            int nThreads = Runtime.getRuntime().availableProcessors();
            System.out.println(nThreads);

            ForkJoinPool fjPool = new ForkJoinPool(nThreads);
            PararelWrite pararelWrite= new PararelWrite(lstAddingData, lstHashDlMachine);
            fjPool.invoke(pararelWrite);
*/
        }
        copyDirectory();
        List<HashPath> lstHashPath = fileService.getDLMachineFileList();
        // JSon Dosyası Oluşturup kife fileye yazdık

        // Yazıyoruz ve datayı atıyoruz
        helper.writeJson(lstHashPath, setting.getDlHashFileLocation());

        String hmsFinal = utils.stopwatch(start, "MAIN");
        System.out.println(hmsFinal);
    }
}