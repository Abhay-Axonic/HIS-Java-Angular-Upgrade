package com.param.lis.microbiology.transaction.dao;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.micro.SensitivityTestResultDetailsMaster;
import com.param.entity.lis.micro.SensitivityTestResultSubDetailsMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SensitivityTestResultDetailsMasterDaoImpl  extends GenericDao<SensitivityTestResultDetailsMaster, Integer>
implements ISensitivityTestResultDetailsMasterDao, ICommonConstants, ITransactionConstants, IError{
	
	
	final static Logger logger = Logger.getLogger(SensitivityTestResultDetailsMasterDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	


	public SensitivityTestResultDetailsMasterDaoImpl() {
		super(SensitivityTestResultDetailsMaster.class);
	}

	@Override
	public Response saveSensitivityTestResultDetailsMaster(
			SensitivityTestResultDetailsMaster sensitivityTestResultDetailsMaster) throws ApplicationException {
		
		try
		{
			if (sensitivityTestResultDetailsMaster != null)
			{
				
				SensitivityTestResultDetailsMaster SensitivityTestResultDetailsMst = save(sensitivityTestResultDetailsMaster);
				if(SensitivityTestResultDetailsMst.getListSensitivityTestResultSubDetailsMaster()!=null)
				{
					for (Iterator iterator = SensitivityTestResultDetailsMst.getListSensitivityTestResultSubDetailsMaster().iterator(); iterator.hasNext();)
					{
						SensitivityTestResultSubDetailsMaster sensitivityTestResultSubDetailsMaster = (SensitivityTestResultSubDetailsMaster) iterator.next();
						sensitivityTestResultSubDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(sensitivityTestResultSubDetailsMaster.getOrgUnitId()).getTime());
						sensitivityTestResultSubDetailsMaster.setOrgId(SensitivityTestResultDetailsMst.getOrgId());
						sensitivityTestResultSubDetailsMaster.setSesitivityResultDetailsId(SensitivityTestResultDetailsMst.getSesitivityResultDetailsId());
						if (sensitivityTestResultSubDetailsMaster.getSesitivityResultSubDetailsId() == 0) 
						{
						sessionFactory.getCurrentSession().save(sensitivityTestResultSubDetailsMaster);
						}
						else 
						{ 
						  sensitivityTestResultSubDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(sensitivityTestResultSubDetailsMaster.getOrgUnitId()).getTime());
						  sessionFactory.getCurrentSession().update(sensitivityTestResultSubDetailsMaster);
                        }
					}
						return new Response(SUCCESS, SUCCESS_200, "Data Save Successfully", null, null);
				}
				else {
					return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
				}
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
				
			}
		}catch(HibernateException er){
			logger.error("Exection", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}	
		
	}

	@Override
	public Response updateSensitivityTestResultDetailsMaster(
			SensitivityTestResultDetailsMaster sensitivityTestResultDetailsMaster)
			throws ApplicationException {
		try {

			if (sensitivityTestResultDetailsMaster != null) {

					if (sensitivityTestResultDetailsMaster.getListSensitivityTestResultSubDetailsMaster() != null) 
					{
						for (Iterator iterator = sensitivityTestResultDetailsMaster
								.getListSensitivityTestResultSubDetailsMaster().iterator(); iterator.hasNext();) 
						{
							SensitivityTestResultSubDetailsMaster sensitivityTestResultSubDetailsMaster = (SensitivityTestResultSubDetailsMaster) iterator
									.next();
							
							if (sensitivityTestResultSubDetailsMaster.getSesitivityResultSubDetailsId() == 0) 
							{
								sensitivityTestResultSubDetailsMaster.setSesitivityResultDetailsId(sensitivityTestResultDetailsMaster.getSesitivityResultDetailsId());
								sensitivityTestResultSubDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(sensitivityTestResultSubDetailsMaster.getOrgUnitId()).getTime());
							    sessionFactory.getCurrentSession().save(sensitivityTestResultSubDetailsMaster);								
							} else 
							{
							  sensitivityTestResultSubDetailsMaster.setSesitivityResultDetailsId(sensitivityTestResultDetailsMaster.getSesitivityResultDetailsId());
	                          sensitivityTestResultSubDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(sensitivityTestResultDetailsMaster.getOrgUnitId()).getTime());
							  sessionFactory.getCurrentSession().update(sensitivityTestResultSubDetailsMaster);
							 
							}

						}
							return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);

					} else {
						return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
					}

			} else {
				return new Response(ERROR, ERR_500, "Sensitivity Examination Details Result list is empty", null, null);

			}
		} catch (HibernateException er) {
			logger.error("Exection", er);throw er;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

}
