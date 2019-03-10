package com.content_load_sb.service;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persist.entity.Directory;
import com.content_load_sb.persist.repositories.IDirectoryDao;
import com.content_load_sb.service.interfaces.IDirectoryService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class DirectoryService extends GenericService<Directory, Long> implements IDirectoryService {

    private IDirectoryDao directoryRepository;

    @Autowired
    public DirectoryService(@Qualifier("directoryDao") IGenericDao<Directory, Long> genericDao) {
        super(genericDao);
        this.directoryRepository = (IDirectoryDao) genericDao;
    }


    @Override
    public Directory findByPath(String path) {
        return directoryRepository.findByPath(path).get(0);
    }


    public List<Directory> findAllDirectories() {
        List<Directory> dirList = new ArrayList<>();
        directoryRepository.findAll().forEach(dirList::add);
        return dirList;
    }

    public boolean createDirectoryData(Path directoryPath, Path sourceDir) {
        try {
            File file = new File(directoryPath.toUri());
            File sourceFile = new File(sourceDir.toUri());

            Directory directory = new Directory();
            directory.setInsertDateToKIFE(LocalDateTime.now());
            directory.setUpdateDateAtKIFE(directory.getInsertDateToKIFE());
            long directorySize = FileUtils.sizeOfDirectory(sourceFile);
            directory.setSize(Helper.getInstance().getStringSizeLengthFile(directorySize));

            directory.setName(file.getName());
            directory.setParentPathTAIL(file.getParentFile().toPath().toString().substring(Setting.getInstance().getTargetDiskRootPath().length()) ) ;
            directory.setPath(file.getPath());
            directory.setPathHEAD(Setting.getInstance().getTargetDiskRootPath());
            directory.setPathTAIL(file.getPath().substring(Setting.getInstance().getTargetDiskRootPath().length()) );
            directoryRepository.save(directory);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

}