package com.content_load_sb.service.interfaces;

import com.content_load_sb.dbops.interfaces.IGenericService;
import com.content_load_sb.persistmysqlslave.entity.TargetDirectorySlave;
import jcifs.smb.SmbFile;

import java.nio.file.Path;

/**
 * Created by ramazancesur on 12/21/17.
 */
public interface ITargetDirectorySlaveService extends IGenericService<TargetDirectorySlave,Long>{
    public boolean createDirectoryData(Path directoryPath, Path sourceDir);

    public boolean createDataPath(SmbFile file, Path filePath);

    public boolean romoveDirectoryFolder(String filePath);
}
