package com.content_load_sb.websocket.utils;

import com.content_load_sb.helper.ApplicationContextHolder;
import com.content_load_sb.helper.EnumUtil;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.helper.OsDetector;
import com.content_load_sb.persistmysql.entity.DataTransferInfoMySql;
import com.content_load_sb.persistmysqlslave.entity.DataTransferInfoSlave;
import com.content_load_sb.service.*;
import jcifs.smb.SmbFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

@Scope("prototype")
public class FileVisitor extends SimpleFileVisitor<Path> {
    private static Logger log = LoggerFactory.getLogger(FileVisitor.class);
    private Path sourceDir;
    private SmbFile targetSmbFile;
    private String rootPath;

    private boolean isCreateMode= false;

    private EnumUtil.DataBaseType dataBaseType;
    private Long totalFileCount;
    private static Long remainCount;

    private DataTransferInfoMySqlService dataTransferInfoMySqlService;
    private DataTransferSlaveService dataTransferSlaveService;

    private TargetDirectoryService targetDirectoryService;
    private TargetDirectorySlaveService targetDirectorySlaveService;

    private TargetFileService targetFileService;
    private TargetFileSlaveService targetFileSlaveService;

    public FileVisitor(Path sourceDir, SmbFile targetSmbFile , EnumUtil.DataBaseType dataBaseType, Long totalFileCount){
        this.sourceDir = sourceDir;
        this.targetSmbFile = targetSmbFile;
        this.dataBaseType=dataBaseType;
        remainCount= totalFileCount;
        this.totalFileCount = totalFileCount;
        if(dataBaseType== EnumUtil.DataBaseType.MYSQL) {
            dataTransferInfoMySqlService = ApplicationContextHolder.getContext().getBean(DataTransferInfoMySqlService.class);
            targetDirectoryService= ApplicationContextHolder.getContext().getBean(TargetDirectoryService.class);
            targetFileService= ApplicationContextHolder.getContext().getBean(TargetFileService.class);
        } else if(dataBaseType== EnumUtil.DataBaseType.MYSQL_SLAVE){
            this.dataTransferSlaveService= ApplicationContextHolder.getContext().getBean(DataTransferSlaveService.class);
            this.targetDirectorySlaveService = ApplicationContextHolder.getContext().getBean(TargetDirectorySlaveService.class);
            this.targetFileSlaveService= ApplicationContextHolder.getContext().getBean(TargetFileSlaveService.class);
        }
        rootPath = targetSmbFile.getPath();


        Properties properties = Helper.getInstance().getRestrictProp("application.properties", "hibernate.hbm2ddl.auto");
        if (properties.getProperty("hibernate.hbm2ddl.auto").contains("create")) {
            isCreateMode = true;
        }
    }

    private void dataTransfer(EnumUtil.DataBaseType dataBaseType, SmbFile sammbaFile, EnumUtil.FileType fileType,Path filePath){
        // Uzak Database Sunucusuna Bağlanıyoruz
        if(fileType== EnumUtil.FileType.FILE){
            if(dataBaseType== EnumUtil.DataBaseType.MYSQL ){
                targetFileService.createFileData(sammbaFile);
            } else if (dataBaseType== EnumUtil.DataBaseType.MYSQL_SLAVE){
                targetFileSlaveService.createFileData(sammbaFile);
            }
        } else{
            if(dataBaseType== EnumUtil.DataBaseType.MYSQL ){
                targetDirectoryService.createDataPath(sammbaFile,filePath);
            } else if (dataBaseType== EnumUtil.DataBaseType.MYSQL_SLAVE){
                targetDirectorySlaveService.createDataPath(sammbaFile,filePath);
            }
        }

    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attributes) {
        try {
            String resolvePath = file.toString().replace(file.getRoot().toString(), "");
            if (OsDetector.isWindows()) {
                resolvePath = resolvePath.replace("\\", "/");
            }

            targetSmbFile = new SmbFile(rootPath + resolvePath);
            if(isCreateMode== true){
                dataTransfer(dataBaseType,targetSmbFile, EnumUtil.FileType.FILE,null);
            }

            if (!targetSmbFile.exists()) {
                Helper.getInstance().createFileRemeout(targetSmbFile, file.toFile());
            }

            if(dataBaseType== EnumUtil.DataBaseType.MYSQL){
                DataTransferInfoMySql transferInfo= new DataTransferInfoMySql();
                transferInfo.setTotalFileSize(totalFileCount);
                remainCount--;
                transferInfo.setRemainingFileSize(remainCount);
                float percent =100- (float)((float)remainCount/totalFileCount*100);
                transferInfo.setPercent(percent);
                dataTransferInfoMySqlService.add(transferInfo);

            } else{
                DataTransferInfoSlave transferInfo= new DataTransferInfoSlave();
                transferInfo.setTotalFileSize(totalFileCount);
                remainCount--;
                transferInfo.setRemainingFileSize(remainCount);
                float percent = (float)((float)remainCount/totalFileCount*100);
                transferInfo.setPercent(percent);

                dataTransferSlaveService.add(transferInfo);
            }
        } catch (Exception ex) {
            log.info(ex.getMessage());
        } finally {
            return FileVisitResult.CONTINUE;
        }
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir,
                                             BasicFileAttributes attributes) {
        try {
            String resolvePath = dir.toString().replace(dir.getRoot().toString(), "");
            if (OsDetector.isWindows()) {
                resolvePath = resolvePath.replace("\\", "/");
            }
            targetSmbFile = new SmbFile(rootPath + resolvePath);

            if (isCreateMode== true){
                dataTransfer(dataBaseType,targetSmbFile,EnumUtil.FileType.DIRECTORY,dir);
            }

            if (!targetSmbFile.exists()) {
                targetSmbFile.mkdirs();
            }

        } catch (Exception ex) {
            log.info(ex.getMessage());
        } finally {
            return FileVisitResult.CONTINUE;
        }
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e)
            throws IOException {
        System.err.printf("Visiting failed for %s\n", file);
        return FileVisitResult.SKIP_SUBTREE;
    }

}
