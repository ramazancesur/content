package com.content_load_sb.persistmysql.repositories;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.persistmysql.entity.TargetDirectory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("targetDirectoryDao")
public interface ITargetDirectoryDao extends IGenericDao<TargetDirectory, Long> {
    @Modifying
    @Transactional
    @Query("delete from TargetDirectory as td where  td.pathTAIL= :filePath")
    boolean romoveDirectoryFolder(@Param("filePath") String filePath);
}
