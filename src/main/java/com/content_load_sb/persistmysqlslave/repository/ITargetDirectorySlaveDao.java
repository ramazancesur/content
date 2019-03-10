package com.content_load_sb.persistmysqlslave.repository;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.persistmysql.entity.TargetDirectory;
import com.content_load_sb.persistmysqlslave.entity.TargetDirectorySlave;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("targetDirectorySlaveDao")
public interface ITargetDirectorySlaveDao extends IGenericDao<TargetDirectorySlave, Long> {
    @Modifying
    @Transactional
    @Query("delete from TargetDirectorySlave as td where  td.pathTAIL= :filePath")
    boolean romoveDirectoryFolder(@Param("filePath") String filePath);
}
