package com.content_load_sb.service.interfaces;

import com.content_load_sb.dbops.interfaces.IGenericService;
import com.content_load_sb.persistmysql.entity.TargetDirectory;
import jcifs.smb.SmbFile;

import java.nio.file.Path;

/**
 * Created by asd on 20.11.2017.
 */
public interface ITargetDirectoryService extends IGenericService<TargetDirectory, Long> {
    public boolean createDirectoryData(Path directoryPath, Path sourceDir);

    public boolean createDataPath(SmbFile file,Path directoryFilePath);

    public boolean romoveDirectoryFolder(String filePath);
}