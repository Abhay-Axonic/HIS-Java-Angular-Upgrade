package com.param.pharmacy.qms.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dto.QMSCounterMasterDto;
import com.param.qms.model.QMSCounter;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes" })
public class QMSCounterDaoImpl extends GenericDao<QMSCounter, Integer>implements IQMSCounterDao, ICommonConstants {

private static final Logger logger = Logger.getLogger(QMSCounterDaoImpl.class);

@Autowired
SessionFactory sessionFactory;

	public QMSCounterDaoImpl() {
	super(QMSCounter.class);
	}

	@Override
	public Response saveQMSCounter(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			QMSCounter master =new QMSCounter();
			master.setQmsCounterCode(dto.getQmsCounterCode());
			master.setQmsCounterDesc(dto.getQmsCounterDesc());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setOrganizationId(dto.getOrganisationId());
			master.setUnitId(dto.getUnitId());
			master.setStatus(ACTIVE);
			if(dto.getQmsCounterId()!= null ) {
				master.setQmsCounterId(dto.getQmsCounterId());
				master = update(master);
			}else {
				master = save(master);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSCounterStatus(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			
			QMSCounter master = findById(dto.getQmsCounterId());
			
				master.setStatus(dto.getStatus()=='A' ? dto.getStatus() : 'I');
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(master.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				sessionFactory.getCurrentSession().update(master);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response inactiveMaster(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			
			
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" UPDATE pharmacy.m_qms_counter_master " + 
					"SET  status='I' " + 
					"WHERE qms_counter_master_id in(select qms_counter_master_id from pharmacy.m_qms_counter_master mqcm where qms_counter_id =:qmsCounterId "+") ")
					.setParameter("qmsCounterId", dto.getQmsCounterId())
					.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, count);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
