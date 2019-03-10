package com.content_load_sb.service;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persistmysql.entity.TargetDirectory;
import com.content_load_sb.persistmysqlslave.entity.TargetDirectorySlave;
import com.content_load_sb.persistmysqlslave.repository.ITargetDirectorySlaveDao;
import com.content_load_sb.service.interfaces.ITargetDirectorySlaveService;
import jcifs.smb.SmbFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Created by ramazancesur on 12/21/17.
 */
@Service
public class TargetDirectorySlaveService extends GenericService<TargetDirectorySlave,Long>
                                                                implements ITargetDirectorySlaveService{

    private ITargetDirectorySlaveDao targetDirectorySlaveDao;

    @Autowired
    public TargetDirectorySlaveService(@Qualifier("targetDirectorySlaveDao") IGenericDao<TargetDirectorySlave, Long> genericDao) {
        super(genericDao);
        targetDirectorySlaveDao= (ITargetDirectorySlaveDao) genericDao;
    }



    @Override
    public boolean createDirectoryData(Path directoryPath, Path sourceDir) {
        try {
            File file = new File(directoryPath.toUri());
            File sourceFile = new File(sourceDir.toUri());

            TargetDirectorySlave directory = new TargetDirectorySlave();
            directory.setInsertDateToKIFE(LocalDateTime.now());
            directory.setUpdateDateAtKIFE(directory.getInsertDateToKIFE());
            long directorySize = FileUtils.sizeOfDirectory(sourceFile);
            directory.setSize(Helper.getInstance().getStringSizeLengthFile(directorySize));

            directory.setName(file.getName());
            directory.setParentPathTAIL(file.getParent());
            directory.setPath(file.getPath());
            targetDirectorySlaveDao.save(directory);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean createDataPath(SmbFile file,Path filePath) {
        try {
            TargetDirectorySlave targetDirectory = new TargetDirectorySlave();
            targetDirectory.setInsertDateToKIFE(LocalDateTime.now());
            targetDirectory.setUpdateDateAtKIFE(targetDirectory.getInsertDateToKIFE());
            targetDirectory.setName(file.getName());
            targetDirectory.setPath(file.getPath());
            targetDirectory.setPathHEAD(Setting.getInstance().getTargetDiskRootPath());
            targetDirectory.setPathTAIL(file.getPath().substring(Setting.getInstance().getTargetDiskRootPath().length()));
            // Dosya boyutu byte şeklinde hesaplandı
            long size = Files.size(filePath);
            targetDirectory.setSize(Helper.getInstance().getStringSizeLengthFile(size));
            targetDirectorySlaveDao.save(targetDirectory);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean romoveDirectoryFolder(String filePath) {
        return targetDirectorySlaveDao.romoveDirectoryFolder(filePath);
    }


}

