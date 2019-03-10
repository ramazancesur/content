package com.content_load_sb.dbops;

import com.content_load_sb.dbops.interfaces.IGenericDao;
import com.content_load_sb.dbops.interfaces.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asd on 8.11.2017.
 */


@Service
public abstract class GenericService<E extends BaseEntity, K extends Serializable> implements IGenericService<E, K> {
    @Autowired
    IGenericDao<E, K> genericDao;

    // Test edecem
    public GenericService(IGenericDao<E, K> genericDao) {
        this.genericDao = genericDao;
    }

    @Override
    public void saveOrUpdate(E entity) {
        genericDao.save(entity);
    }

    @Override
    public List<E> getAll() {
        return genericDao.findAll();
    }

    @Override
    public E get(K id) {
        return genericDao.findOne(id);
    }

    @Override
    public boolean add(E entity) {
        try {
            genericDao.save(entity);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(E entity) {
        try {
            genericDao.saveAndFlush(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(E entity) {
        try {
            genericDao.delete(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(K id) {
        try {
            genericDao.delete(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

}
