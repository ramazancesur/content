package com.content_load_sb.service;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.helper.HashMapReader;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persist.entity.DataFile;
import com.content_load_sb.persist.entity.Directory;
import com.content_load_sb.persist.repositories.IDirectoryDao;
import com.content_load_sb.persist.repositories.IFileDao;
import com.content_load_sb.service.interfaces.IFileService;
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
public class FileService extends GenericService<DataFile, Long> implements IFileService {

    private IFileDao fileeRepository;

    @Autowired
    private IDirectoryDao directoryDao;

    @Autowired
    public FileService(@Qualifier("fileDao") IGenericDao<DataFile, Long> genericDao) {
        super(genericDao);
        this.fileeRepository = (IFileDao) genericDao;
    }

    public boolean createFileData(Path path) {
        try {
            File file = new File(path.toUri());
            DataFile dataFile = new DataFile();
            dataFile.setFileType(FilenameUtils.getExtension(file.getPath()));
            dataFile.setInsertDateToKIFE(LocalDateTime.now());
            dataFile.setUpdateDateAtKIFE(dataFile.getInsertDateToKIFE());
            dataFile.setName(file.getName());
            dataFile.setPath(file.getPath());
            dataFile.setPathHEAD(Setting.getInstance().getTargetDiskRootPath());
            dataFile.setPathTAIL(file.getPath().substring(Setting.getInstance().getTargetDiskRootPath().length()));

            dataFile.setParentPath(file.getParentFile().getPath().substring(Setting.getInstance().getTargetDiskRootPath().length()));
            dataFile.setParentPathHEAD(Setting.getInstance().getTargetDiskRootPath());
            // Dosya boyutu byte şeklinde hesaplandı
            long size = FileUtils.sizeOf(file);
            dataFile.setSize(Helper.getInstance().getStringSizeLengthFile(size));
            dataFile.setParentPath(file.getParent());
            String hashRead = HashMapReader.getInstance().getCalculatedHashByPathTail(dataFile.getPathTAIL());
            dataFile.setHash(hashRead);

            Directory directory = directoryDao.findByPath(dataFile.getParentPath()).get(0);
            dataFile.setDirectory(directory);

            fileeRepository.save(dataFile);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public List<DataFile> findFileeListByDirectoryId(Long dirId) {
        return fileeRepository.findByDirectoryOid(dirId);
    }


    public List<DataFile> findByPath(String path) {
        return fileeRepository.findByPath(path);
    }

    @Override
    public List<HashPath> getDLMachineFileList() {
        return fileeRepository.getDLMachineFileList();
    }

}
