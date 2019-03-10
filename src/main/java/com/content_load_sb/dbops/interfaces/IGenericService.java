package com.content_load_sb.dbops.interfaces;

import com.content_load_sb.dbops.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asd on 8.11.2017.
 */
public interface IGenericService<E extends BaseEntity, K extends Serializable> {
    public void saveOrUpdate(E entity);

    public List<E> getAll();

    public E get(K id);

    public boolean add(E entity);

    public boolean update(E entity);

    public boolean remove(E entity);

    public boolean remove(K id);
}