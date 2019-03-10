package com.content_load_sb.service.interfaces;

import com.content_load_sb.dbops.interfaces.IGenericService;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.persistmysql.entity.TargetFile;
import jcifs.smb.SmbFile;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by asd on 20.11.2017.
 */
public interface ITargetFileService extends IGenericService<TargetFile, Long> {
    TargetFile createFileData(SmbFile file);

    TargetFile getTargetFilebyFilePathName(String filePath);

    TargetFile getTargetFileByLasted();

    List<TargetFile> removeByPathTail(String pathTail);

    List<HashPath> getAllServerDataList();
}
