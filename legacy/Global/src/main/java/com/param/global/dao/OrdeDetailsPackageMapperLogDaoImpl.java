package com.param.global.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.service.global.model.OrdeDetailsPackageMapperLog;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrdeDetailsPackageMapperLogDaoImpl extends GenericDao<OrdeDetailsPackageMapperLog, Integer> implements IOrdeDetailsPackageMapperLogDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(OrdeDetailsPackageMapperLogDaoImpl.class);


	public OrdeDetailsPackageMapperLogDaoImpl() {
		super(OrdeDetailsPackageMapperLog.class);
	}

	
	@Override
	public Response savePackageConsumptionLog(OrdeDetailsPackageMapperLog master) throws ApplicationException {

		try {
					save(master);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
			logger.error("savePackageConsumptionLog failed", he);
			} catch (Exception e) {
			logger.error("savePackageConsumptionLog failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}


	@Override
	public Response updatePackageConsumptionLog(OrderDetailsMasterDto detailsMasterDto) throws ApplicationException {



		try {
			
				String qry1 = null;
				
				 qry1 = "  update public.t_order_details_package_mapper_log  " + 
				 		"set package_consumption_master_id = null,package_amount = 0,package_consumption_cap_details_id = null " + 
				 		"where order_details_id =:orderDetailsId ";
				 sessionFactory.getCurrentSession()
					.createSQLQuery(qry1)
					.setParameter("orderDetailsId",detailsMasterDto.getOrderDetailsId())
			        .executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			throw e;
		}






	}
}
