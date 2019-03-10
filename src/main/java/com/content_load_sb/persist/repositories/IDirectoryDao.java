package com.content_load_sb.persist.repositories;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.persist.entity.Directory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("directoryDao")
public interface IDirectoryDao extends IGenericDao<Directory, Long> {
    List<Directory> findByPath(String path);
}