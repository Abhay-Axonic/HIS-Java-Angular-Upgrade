package com.param.mrd.dao;

import com.param.adt.mrd.MrdFileAllocationStatus;
import com.param.global.model.MrdFileStatus;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IMrdFileStatusDaoImpl extends GenericDao<MrdFileAllocationStatus,Integer> implements IMrdFileStatusDao{
    @Autowired
    private SessionFactory sessionFactory;

    public IMrdFileStatusDaoImpl() {
        super(MrdFileAllocationStatus.class);
    }


    @Override
    public void saveMrdFileStatus(List<MrdFileAllocationStatus> list) {
        list.forEach(this::save);
    }
}
