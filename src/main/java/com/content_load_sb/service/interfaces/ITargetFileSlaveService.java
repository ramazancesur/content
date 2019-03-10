package com.content_load_sb.service.interfaces;

import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericService;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.persistmysql.entity.TargetFile;
import com.content_load_sb.persistmysqlslave.entity.TargetFileSlave;
import jcifs.smb.SmbFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by ramazancesur on 12/21/17.
 */
public interface ITargetFileSlaveService extends IGenericService<TargetFileSlave, Long> {
    TargetFileSlave createFileData(SmbFile file);

    TargetFileSlave getTargetFilebyFilePathName(String filePath);

    TargetFileSlave getTargetFileByLasted();

    List<HashPath> getAllServerDataList();

    List<TargetFileSlave> removeByPathTAIL(@Param("pathTail") String pathTAIL);
}
