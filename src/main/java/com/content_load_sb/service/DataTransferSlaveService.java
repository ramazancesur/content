package com.content_load_sb.service;

import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.persistmysqlslave.entity.DataTransferInfoSlave;
import com.content_load_sb.persistmysqlslave.repository.IDataTransferInfoSlaveDao;
import com.content_load_sb.service.interfaces.IDataTransferSlaveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DataTransferSlaveService extends GenericService<DataTransferInfoSlave, Long>
        implements IDataTransferSlaveInfoService {
    private IDataTransferInfoSlaveDao slaveDao;

    @Autowired
    public DataTransferSlaveService(@Qualifier("dataTransferInfoSlaveDao") IGenericDao<DataTransferInfoSlave, Long> genericDao) {
        super(genericDao);
        slaveDao = (IDataTransferInfoSlaveDao) genericDao;
    }
}
