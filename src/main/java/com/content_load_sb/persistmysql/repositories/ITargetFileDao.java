package com.content_load_sb.persistmysql.repositories;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.persistmysql.entity.TargetFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("targetFileDao")
@Transactional
public interface ITargetFileDao extends IGenericDao<TargetFile, Long> {
    public TargetFile findByPathTAIL(String filePath);

    public TargetFile findFirstByOrderByOidDesc();


    public List<TargetFile> removeByPathTAIL(String pathTAIL);

    @Modifying
    @Query("SELECT distinct new com.content_load_sb.dto.HashPath(df.pathTAIL, df.hash) FROM TargetFile as df")
    public List<HashPath> getAllServerDataList();
}