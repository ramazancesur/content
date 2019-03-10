package com.content_load_sb.service.interfaces;

import com.content_load_sb.dbops.interfaces.IGenericService;
import com.content_load_sb.persist.entity.Directory;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by asd on 8.11.2017.
 */
public interface IDirectoryService extends IGenericService<Directory, Long> {
    Directory findByPath(String path);

    List<Directory> findAllDirectories();

    boolean createDirectoryData(Path directoryPath, Path sourceDir);
}
