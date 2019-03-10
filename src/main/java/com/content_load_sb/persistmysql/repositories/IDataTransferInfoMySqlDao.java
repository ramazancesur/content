package com.content_load_sb.persistmysql.repositories;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.persistmysql.entity.DataTransferInfoMySql;
import org.springframework.stereotype.Repository;

@Repository("dataTransferInfoMySqlDao")
public interface IDataTransferInfoMySqlDao extends IGenericDao<DataTransferInfoMySql, Long> {

}
