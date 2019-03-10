package com.content_load_sb.config;

import com.content_load_sb.helper.OsDetector;
import com.content_load_sb.utils.ResourceBundleReader;

import java.net.URISyntaxException;

/**
 * Created by asd on 16.11.2017.
 */
public class Setting {
    private static Setting instance;
    private String targetFileLocation = "";
    private String dlHashFileLocation = "";
    private String sourceDiskLocation = "";
    private String targetDiskRootPath = "";
    private String ssdSourceFolderJson = "";
    private String systemFileSeparator = "";
    private String sourceDiskRootPath = "";
    private int fileUploadLimit = 100000000;
    private String networkDisk = "";
    private String resourceFolder = "";
    private int socketPort = 10048;

    private Setting() {
        systemFileSeparator = setSystemFileSeparator();
        fileUploadLimit = setFileUploadLimit();
        targetFileLocation = setTargetFileLocation();
        dlHashFileLocation = setDlHashFileLocation();
        sourceDiskLocation = setSsdLocation();
        targetDiskRootPath = setTargetDiskRootPath();
        ssdSourceFolderJson = setSsdSourceFolderJson();
        sourceDiskRootPath = setSourceDiskRootPath();
        networkDisk = setNetworkDisk();
        try {
            resourceFolder = setResourceFolder();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.socketPort = setSocketPort();
    }

    public static Setting getInstance() {
        if (instance == null) {
            instance = new Setting();
        }
        return instance;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public String getResourceFolder() {
        return resourceFolder;
    }

    public int getFileUploadLimit() {
        return fileUploadLimit;
    }

    public String getSourceDiskRootPath() {
        return sourceDiskRootPath;
    }

    public String getTargetFileLocation() {
        return targetFileLocation;
    }

    public String getSystemFileSeparator() {
        return systemFileSeparator;
    }

    public String getSsdSourceFolderJson() {
        return ssdSourceFolderJson;
    }

    public String getDlHashFileLocation() {
        return dlHashFileLocation;
    }

    public String getSourceDiskLocation() {
        return sourceDiskLocation;
    }

    public String getTargetDiskRootPath() {
        return targetDiskRootPath;
    }

    private String setSystemFileSeparator() {
        return System.getProperty("file.separator");
    }

    public String getNetworkDisk() {
        return networkDisk;
    }

    private int setFileUploadLimit() {
        int limit = Integer.parseInt(ResourceBundleReader
                .getExchangeProperties("server.upload"));
        return limit;
    }

    private int setSocketPort() {
        int port = Integer.parseInt(ResourceBundleReader.getExchangeProperties("socket.port").replace(" ", ""));
        return port;
    }

    private String setResourceFolder() throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResource("./").getPath();
    }

    private String setNetworkDisk() {
        return ResourceBundleReader.getExchangeProperties("networkDisk");
    }

    private String setTargetFileLocation() {
        if (OsDetector.isWindows()) {
            targetFileLocation = ResourceBundleReader.getExchangeProperties("destinationDisk")
                    + ResourceBundleReader.getExchangeProperties("ssdSourceFolderForTransfer").split(":")[1];
        } else {
            targetFileLocation = ResourceBundleReader.getExchangeProperties("destinationDisk") + "/"
                    + ResourceBundleReader.getExchangeProperties("ssdSourceFolderForTransfer").split("/")[3];
        }
        return targetFileLocation;
    }

    private String setDlHashFileLocation() {
        if (targetFileLocation.contains("/")) {
            dlHashFileLocation = targetFileLocation + "/localdata.kife";
        } else {
            dlHashFileLocation = targetFileLocation + "\\localdata.kife";
        }
        return dlHashFileLocation;
    }

    private String setSsdSourceFolderJson() {
        if (this.targetFileLocation.contains("/")) {
            ssdSourceFolderJson = ResourceBundleReader.getExchangeProperties("ssdSourceFolderForTransfer") + "/ssddatahash.kife";
        } else {
            ssdSourceFolderJson = ResourceBundleReader.getExchangeProperties("ssdSourceFolderForTransfer") + "\\ssddatahash.kife";
        }
        return ssdSourceFolderJson;
    }

    private String setSsdLocation() {
        sourceDiskLocation = ResourceBundleReader.getExchangeProperties("ssdSourceFolderForTransfer");
        return sourceDiskLocation;
    }

    private String setTargetDiskRootPath() {
        if (OsDetector.isWindows()) {
            targetDiskRootPath = targetFileLocation.substring(0, 2);
        } else {
            String[] directoryArray = targetFileLocation.split("/");
            targetDiskRootPath = systemFileSeparator + directoryArray[1] + systemFileSeparator + directoryArray[2];
        }
        return targetDiskRootPath;
    }

    public String setSourceDiskRootPath() {
        if (OsDetector.isWindows()) {
            sourceDiskRootPath = sourceDiskLocation.substring(0, 2);
        } else {
            String[] directoryArray = sourceDiskLocation.split("/");
            sourceDiskRootPath = systemFileSeparator + directoryArray[1] + systemFileSeparator + directoryArray[2];
        }
        return sourceDiskRootPath;
    }

}