package com.content_load_sb.persist.repositories;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.persist.entity.DataFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("fileDao")
public interface IFileDao extends IGenericDao<DataFile, Long> {
    public List<DataFile> findByPath(String path);

    public List<DataFile> findByDirectoryOid(Long directoryId);

    @Modifying
    @Transactional
    @Query("select DISTINCT new com.content_load_sb.dto.HashPath(df.pathTAIL, df.hash)  FROM DataFile as df")
    List<HashPath> getDLMachineFileList();

}