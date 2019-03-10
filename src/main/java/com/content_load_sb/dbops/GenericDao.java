package com.content_load_sb.dbops;
/*

import com.content_load_sb.dbops.interfaces.IGenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;

@Repository
@Transactional
public class GenericDao<T extends BaseEntity, K extends Serializable>
                         extends SimpleJpaRepository<T,K> implements IGenericDao<T, K> {


    // Bunu direct jpql ile işlem yapmıyorsan kullanabilirsin onun yerine interfacede iken işlem yapılmasına karar verildi

    private JpaEntityInformation<T, Serializable> entityInformation;
    private final EntityManager entityManager;
    @Autowired
    public GenericDao(JpaEntityInformation<T, Serializable> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }
*/
/*

    @Override
    @SuppressWarnings("unchecked")
    public T findByUuid(String uuid) {

        Query query = entityManager.createQuery("from " + entityInformation.getEntityName() + " e where e.uuid = ?1")
                .setParameter(1, uuid);

        return (T) query.getSingleResult();
    }
*//*


    @Override
    public <S extends T> S save(S entity) {
        if((entity.getOid()==null || entity.getOid()==0) || entity.getCreatedDate()== null){
            entity.setCreatedDate(new Date());
        }
        entity.setUpdatedDate(new Date());
        return super.save(entity);
    }
}
*/
