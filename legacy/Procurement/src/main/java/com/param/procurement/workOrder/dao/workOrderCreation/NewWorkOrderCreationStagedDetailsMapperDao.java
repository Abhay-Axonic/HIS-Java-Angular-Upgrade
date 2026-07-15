package com.param.procurement.workOrder.dao.workOrderCreation;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.NewWorkOrderCreationStagedDetailsMapper;
import com.param.service.global.SessionService;

public class NewWorkOrderCreationStagedDetailsMapperDao extends AbstractDao<Integer, NewWorkOrderCreationStagedDetailsMapper> implements INewWorkOrderCreationStagedDetailsMapperDao {
	@Autowired
    private SessionService sessionService;
	
	@Autowired
	private SessionFactory sessionFactory;

}
