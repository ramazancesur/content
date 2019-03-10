package com.content_load_sb.service;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persistmysql.entity.TargetDirectory;
import com.content_load_sb.persistmysql.repositories.ITargetDirectoryDao;
import com.content_load_sb.service.interfaces.ITargetDirectoryService;
import jcifs.smb.SmbFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class TargetDirectoryService extends GenericService<TargetDirectory, Long> implements ITargetDirectoryService {
    Helper helper= Helper.getInstance();
    private ITargetDirectoryDao targetDirectoryDao;

    @Autowired
    public TargetDirectoryService(@Qualifier("targetDirectoryDao") IGenericDao<TargetDirectory, Long> genericDao) {
        super(genericDao);
        this.targetDirectoryDao = (ITargetDirectoryDao) genericDao;
    }

    @Override
    public boolean createDirectoryData(Path directoryPath, Path sourceDir) {
        try {
            File file = new File(directoryPath.toUri());
            File sourceFile = new File(sourceDir.toUri());

            TargetDirectory directory = new TargetDirectory();
            directory.setInsertDateToKIFE(LocalDateTime.now());
            directory.setUpdateDateAtKIFE(directory.getInsertDateToKIFE());
            long directorySize = FileUtils.sizeOfDirectory(sourceFile);
            directory.setSize(Helper.getInstance().getStringSizeLengthFile(directorySize));

            directory.setName(file.getName());
            directory.setParentPathTAIL(file.getParent());
            directory.setPath(file.getPath());
            targetDirectoryDao.save(directory);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean createDataPath(SmbFile file,Path directoryFilePath) {
        try {
            TargetDirectory targetDirectory = new TargetDirectory();
            targetDirectory.setInsertDateToKIFE(LocalDateTime.now());
            targetDirectory.setUpdateDateAtKIFE(targetDirectory.getInsertDateToKIFE());
            targetDirectory.setName(file.getName());
            targetDirectory.setPath(file.getPath());
            targetDirectory.setPathHEAD(Setting.getInstance().getTargetDiskRootPath());
            targetDirectory.setPathTAIL(file.getPath().substring(Setting.getInstance().getTargetDiskRootPath().length()));
            // Dosya boyutu byte şeklinde hesaplandı
            long size= helper.folderSize(directoryFilePath.toFile());
            targetDirectory.setSize(Helper.getInstance().getStringSizeLengthFile(size));
            targetDirectoryDao.save(targetDirectory);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean romoveDirectoryFolder(String filePath) {
        return targetDirectoryDao.romoveDirectoryFolder(filePath);
    }


}