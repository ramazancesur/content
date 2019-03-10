package com.content_load_sb.service.interfaces;

import com.content_load_sb.dbops.interfaces.IGenericService;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.persist.entity.DataFile;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by asd on 9.11.2017.
 */
public interface IFileService extends IGenericService<DataFile, Long> {
    List<DataFile> findFileeListByDirectoryId(Long dirId);

    List<DataFile> findByPath(String path);

    List<HashPath> getDLMachineFileList();

    boolean createFileData(Path path);
}
