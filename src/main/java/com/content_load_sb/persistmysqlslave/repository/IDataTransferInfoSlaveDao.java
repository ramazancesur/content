package com.content_load_sb.persistmysqlslave.repository;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.persistmysqlslave.entity.DataTransferInfoSlave;
import org.springframework.stereotype.Repository;

@Repository("dataTransferInfoSlaveDao")
public interface IDataTransferInfoSlaveDao extends IGenericDao<DataTransferInfoSlave,Long> {

}
