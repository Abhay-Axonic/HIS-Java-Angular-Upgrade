package com.param.pharmacy.qms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dto.QMSDeptMasterDto;
import com.param.qms.model.QMSDeptMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class QMSDeptMasterDaoImpl extends GenericDao<QMSDeptMaster, Integer> implements IQMSDeptMasterDao,ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(QMSDeptMasterDaoImpl.class);

	public QMSDeptMasterDaoImpl() {
		super(QMSDeptMaster.class);
	}

	@Override
	public Response saveQMSDeptMaster(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			QMSDeptMaster master=new QMSDeptMaster();
			master.setQmsDeptName(dto.getQmsDeptName());
			master.setStartLetter(dto.getStartLetter());
			master.setStartNumber(dto.getStartNumber());
			master.setIsVoice(dto.getIsVoice()!=null ? dto.getIsVoice() : INACTIVE );
			master.setIsReception(dto.getIsReception()!=null ? dto.getIsReception() : INACTIVE);
			master.setQmsKioskMasterId(dto.getQmsKioskMasterId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setOrganisationId(dto.getOrganisationId());
			master.setUnitId(dto.getUnitId());
			master.setStatus(ACTIVE);
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSDeptMaster(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			QMSDeptMaster master=findById(dto.getQmsDeptMasterId());
			master.setQmsDeptName(dto.getQmsDeptName());
			master.setStartLetter(dto.getStartLetter());
			master.setStartNumber(dto.getStartNumber());
			master.setIsVoice(dto.getIsVoice()!=null ? dto.getIsVoice() : INACTIVE );
			master.setIsReception(dto.getIsReception()!=null ? dto.getIsReception() : INACTIVE);
			master.setQmsKioskMasterId(dto.getQmsKioskMasterId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSDeptMasterStatus(QMSDeptMasterDto dto) throws ApplicationException {

		try{
			QMSDeptMaster master=findById(dto.getQmsDeptMasterId());
			master.setStatus(dto.getStatus()==ACTIVE ? ACTIVE : INACTIVE);
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getQMSDeptMasterList(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			List<QMSDeptMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_DEPT_MASTER_LIST")
				.setParameter("orgId", dto.getOrganisationId())
				.setParameter("unitId", dto.getUnitId())
				.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
				.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(QMSDeptMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSDeptMasterCount(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			Criteria  c=sessionFactory.getCurrentSession().createCriteria(QMSDeptMaster.class);
			c.add(Restrictions.eq("organisationId", dto.getOrganisationId()));
			c.add(Restrictions.eq("unitId",dto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, count);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSDeptMasterById(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			List<QMSDeptMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_DEPT_MASTER_BY_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("id", dto.getQmsDeptMasterId())
					.setResultTransformer(Transformers.aliasToBean(QMSDeptMasterDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSDeptMasterByName(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			List<QMSDeptMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_DEPT_MASTER_BY_NAME")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("qmsKioskId", dto.getQmsKioskMasterId())
					.setParameter("deptName", dto.getQmsDeptName().trim().toLowerCase())
//					.setParameter("starNumber", dto.getStartNumber())
//					.setParameter("startLetter", dto.getStartLetter())
//					.setParameter("isVoice", dto.getIsVoice()!=null ? dto.getIsVoice() : INACTIVE)
//					.setParameter("isReception", dto.getIsReception()!=null ? dto.getIsReception() : INACTIVE)
					.setResultTransformer(Transformers.aliasToBean(QMSDeptMasterDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSDeptMasterByNameNotById(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			List<QMSDeptMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_DEPT_MASTER_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("qmsKioskId", dto.getQmsKioskMasterId())
					.setParameter("deptName", dto.getQmsDeptName().trim().toLowerCase())
//					.setParameter("starNumber", dto.getStartNumber().toLowerCase())
//					.setParameter("startLetter", dto.getStartLetter().toLowerCase())
//					.setParameter("isVoice", dto.getIsVoice()!=null ? dto.getIsVoice() : INACTIVE)
//					.setParameter("isReception", dto.getIsReception()!=null ? dto.getIsReception() : INACTIVE)
					.setParameter("id", dto.getQmsDeptMasterId())
					.setResultTransformer(Transformers.aliasToBean(QMSDeptMasterDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveQMSDeptMasterList(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			List<QMSDeptMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_QMS_DEPT_MASTER_LIST")
				.setParameter("orgId", dto.getOrganisationId())
				.setParameter("unitId", dto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(QMSDeptMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getReceptionListByKiosk(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			List<QMSDeptMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_RECEPTION_LIST_BY_KIOSK")
				.setParameter("orgId", dto.getOrganisationId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("kioskMasterId", dto.getQmsKioskMasterId())
				.setResultTransformer(Transformers.aliasToBean(QMSDeptMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDeptListByKioskId(QMSDeptMasterDto dto) throws ApplicationException {
		try{
			List<QMSDeptMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_DEPT_LIST_BY_KIOSK_ID")
				.setParameter("orgId", dto.getOrganisationId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("kioskId", dto.getQmsKioskMasterId())
				.setResultTransformer(Transformers.aliasToBean(QMSDeptMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
