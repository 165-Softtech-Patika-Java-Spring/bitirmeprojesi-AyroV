package com.softtech.webapp.general.service;

import com.softtech.webapp.general.entity.BaseEntity;
import com.softtech.webapp.general.enums.ErrorMessage;
import com.softtech.webapp.general.enums.IErrorMessage;
import com.softtech.webapp.general.exceptions.ItemNotFoundException;
import com.softtech.webapp.general.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class BaseEntityService<E extends BaseEntity, D extends JpaRepository<E, Long>> {
    private final D dao;
    private final Class<E> type;

    public List<E> findAll(){
        return dao.findAll();
    }

    public Optional<E> findById(Long id){
        return dao.findById(id);
    }

    public E save(E entity, boolean isUpdate){

        setAdditionalFields(entity, isUpdate);
        entity = dao.save(entity);

        return entity;
    }

    public void delete(E entity){
        dao.delete(entity);
    }

    public boolean existsById(Long id){
        return dao.existsById(id);
    }

    public D getDao() {
        return dao;
    }

    private void setAdditionalFields(E entity, boolean isUpdate) {
        Date date = DateUtil.getCurrentDate();

        if(isUpdate) {
            entity.setUpdateDate(date);
            entity.setUpdatedBy(1L);
        }
        else {
            entity.setCreateDate(date);
            entity.setCreatedBy(1L);
        }
    }

    public E getByIdWithControl(Long id) {
        Optional<E> entityOptional = findById(id);

        E entity;

        if (entityOptional.isPresent())
            entity = entityOptional.get();
        else {
            throw new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND, type.getSimpleName());
        }

        return entity;
    }
}
