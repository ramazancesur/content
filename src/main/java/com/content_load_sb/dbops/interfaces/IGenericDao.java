package com.content_load_sb.dbops.interfaces;

import com.content_load_sb.dbops.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Created by asd on 8.11.2017.
 */
@NoRepositoryBean
@Transactional
public interface IGenericDao<E extends BaseEntity, K extends Serializable> extends JpaRepository<E, K> {

}