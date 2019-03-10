package com.content_load_sb.service;


import com.content_load_sb.config.Setting;
import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.helper.HashMapReader;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persistmysql.entity.TargetFile;
import com.content_load_sb.persistmysql.repositories.ITargetFileDao;
import com.content_load_sb.service.interfaces.ITargetFileService;
import jcifs.smb.SmbFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TargetFileService extends GenericService<TargetFile, Long> implements ITargetFileService {
    private ITargetFileDao targetFileDao;


    @Autowired
    public TargetFileService(@Qualifier("targetFileDao") IGenericDao<TargetFile, Long> genericDao) {
        super(genericDao);
        this.targetFileDao = (ITargetFileDao) genericDao;
    }

    @Override
    public TargetFile createFileData(SmbFile file) {
        try {
            TargetFile targetFile = new TargetFile();
            targetFile.setFileType(FilenameUtils.getExtension(file.getPath()));
            targetFile.setInsertDateToKIFE(LocalDateTime.now());
            targetFile.setUpdateDateAtKIFE(targetFile.getInsertDateToKIFE());
            targetFile.setName(file.getName());
            targetFile.setPath(file.getPath());
            targetFile.setPathHEAD(Setting.getInstance().getTargetDiskRootPath());
            targetFile.setPathTAIL(file.getPath().substring(Setting.getInstance().getTargetDiskRootPath().length()));
            // Dosya boyutu byte şeklinde hesaplandı
            long size = file.length();
            targetFile.setSize(Helper.getInstance().getStringSizeLengthFile(size));
            targetFile.setParentPath(file.getParent());
            String hashRead = HashMapReader.getInstance().getCalculatedHashByPathTail(targetFile.getPathTAIL());
            targetFile.setHash(hashRead);
            this.add(targetFile);
            return targetFile;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public TargetFile getTargetFilebyFilePathName(String filePath) {
        return targetFileDao.findByPathTAIL(filePath);
    }

    @Override
    public TargetFile getTargetFileByLasted() {
        return targetFileDao.findFirstByOrderByOidDesc();
    }

    @Override
    public List<TargetFile> removeByPathTail(String pathTail) {
        return  targetFileDao.removeByPathTAIL(pathTail);
    }

    @Override
    public List<HashPath> getAllServerDataList() {
        return targetFileDao.getAllServerDataList();
    }

}
