package com.content_load_sb.persistmysqlslave.repository;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.persistmysql.entity.TargetFile;
import com.content_load_sb.persistmysqlslave.entity.TargetFileSlave;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("targetFileSlaveDao")
@Transactional
public interface ITargetFileSlaveDao extends IGenericDao<TargetFileSlave, Long> {
    public TargetFileSlave findByPathTAIL(String filePath);

    public TargetFileSlave findFirstByOrderByOidDesc();

    public List<TargetFileSlave> removeByPathTAIL( String pathTAIL);

    @Modifying
    @Query("SELECT new com.content_load_sb.dto.HashPath(df.pathTAIL, df.hash) FROM TargetFileSlave as df")
    public List<HashPath> getAllServerDataList();
}