package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureOrderStatusDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProcedureOrderStatusDaoImpl implements IProcedureOrderStatusDao,ICommonConstants {
	final static Logger logger = Logger.getLogger(ProcedureOrderStatusDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response getActiveProcedureOrderStatus() throws ApplicationException {
		try{
			List<ProcedureOrderStatusDto> procedureOrderStatusDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PROCEDURE_ORDER_STATUS")
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderStatusDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, procedureOrderStatusDtoList, null);
		}catch(HibernateException he){
		logger.error("getActiveProcedureOrderStatus failed", he);
		}catch(Exception e){
		logger.error("getActiveProcedureOrderStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
