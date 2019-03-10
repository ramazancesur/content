package com.content_load_sb.service;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.helper.HashMapReader;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persistmysql.entity.TargetFile;
import com.content_load_sb.persistmysql.repositories.ITargetFileDao;
import com.content_load_sb.persistmysqlslave.entity.TargetFileSlave;
import com.content_load_sb.persistmysqlslave.repository.ITargetFileSlaveDao;
import com.content_load_sb.service.interfaces.ITargetFileSlaveService;
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

/**
 * Created by ramazancesur on 12/21/17.
 */
@Service
public class TargetFileSlaveService extends GenericService<TargetFileSlave,Long> implements ITargetFileSlaveService{
    private ITargetFileSlaveDao targetFileDao;

    public TargetFileSlaveService(@Qualifier("targetFileSlaveDao") IGenericDao<TargetFileSlave, Long> genericDao) {
        super(genericDao);
        targetFileDao= (ITargetFileSlaveDao) genericDao;
    }


    @Override
    public TargetFileSlave createFileData(SmbFile file) {
        try {
            TargetFileSlave targetFile = new TargetFileSlave();
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
    public TargetFileSlave getTargetFilebyFilePathName(String filePath) {
        return targetFileDao.findByPathTAIL(filePath);
    }

    @Override
    public TargetFileSlave getTargetFileByLasted() {
        return targetFileDao.findFirstByOrderByOidDesc();
    }

    @Override
    public List<HashPath> getAllServerDataList() {
        return  targetFileDao.getAllServerDataList();
    }

    @Override
    public List<TargetFileSlave> removeByPathTAIL(String pathTAIL) {
        return targetFileDao.removeByPathTAIL(pathTAIL);
    }
}
