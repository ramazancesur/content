package com.content_load_sb.service;

import com.content_load_sb.dbops.GenericService;
import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.persistmysql.entity.DataTransferInfoMySql;
import com.content_load_sb.persistmysql.repositories.IDataTransferInfoMySqlDao;
import com.content_load_sb.service.interfaces.IDataTransferInfoMySqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DataTransferInfoMySqlService extends GenericService<DataTransferInfoMySql,Long> implements IDataTransferInfoMySqlService{
    private IDataTransferInfoMySqlDao mySqlDao;

    @Autowired
    public DataTransferInfoMySqlService(@Qualifier("dataTransferInfoMySqlDao") IGenericDao<DataTransferInfoMySql, Long> genericDao) {
        super(genericDao);
        this.mySqlDao= (IDataTransferInfoMySqlDao) genericDao;
    }
}
