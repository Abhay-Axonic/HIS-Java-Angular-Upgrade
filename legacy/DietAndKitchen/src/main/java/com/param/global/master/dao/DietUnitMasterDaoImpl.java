package com.param.global.master.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.UnitOfMeasurmentMasterDto;
import com.param.global.master.model.UnitOfMeasurmentMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DietUnitMasterDaoImpl extends GenericDao<UnitOfMeasurmentMaster, Integer> implements IDietUnitMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietUnitMasterDaoImpl.class);

	public DietUnitMasterDaoImpl() {
		super(UnitOfMeasurmentMaster.class);
	}

@Autowired
SessionFactory sessionFactory;

@Override
public Response saveDietUnitMaster(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
	try {
		UnitOfMeasurmentMaster unitOfMeasurmentMaster=new UnitOfMeasurmentMaster();
		unitOfMeasurmentMaster.setUomDescription(unitOfMeasurmentMasterDto.getUomDescription());
		unitOfMeasurmentMaster.setUomName(unitOfMeasurmentMasterDto.getUomName());
		unitOfMeasurmentMaster.setUnitId(unitOfMeasurmentMasterDto.getUnitId());
		unitOfMeasurmentMaster.setOrganizationId(unitOfMeasurmentMasterDto.getOrganizationId());
		unitOfMeasurmentMaster.setStatus(unitOfMeasurmentMasterDto.getStatus());
		unitOfMeasurmentMaster.setCreatedBy(unitOfMeasurmentMasterDto.getCreatedBy());
		unitOfMeasurmentMaster.setCreatedDate(GlobalCommonDateUtils.getDate(unitOfMeasurmentMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
		unitOfMeasurmentMaster.setUpdatedBy(unitOfMeasurmentMasterDto.getUpdatedBy());
		unitOfMeasurmentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(unitOfMeasurmentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

		save(unitOfMeasurmentMaster);

	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response updateDietUnitMaster(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
	try {
			UnitOfMeasurmentMaster unitOfMeasurmentMaster=findById(unitOfMeasurmentMasterDto.getUomId());
			unitOfMeasurmentMaster.setUomDescription(unitOfMeasurmentMasterDto.getUomDescription());
			unitOfMeasurmentMaster.setUomName(unitOfMeasurmentMasterDto.getUomName());
			unitOfMeasurmentMaster.setOrganizationId(unitOfMeasurmentMasterDto.getOrganizationId());
			unitOfMeasurmentMaster.setUpdatedBy(unitOfMeasurmentMasterDto.getUpdatedBy());
			unitOfMeasurmentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(unitOfMeasurmentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

		update(unitOfMeasurmentMaster);

	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response updateDietUnitMasterStatus(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
	try {
			UnitOfMeasurmentMaster unitOfMeasurmentMaster=findById(unitOfMeasurmentMasterDto.getUomId());

			unitOfMeasurmentMaster.setStatus(unitOfMeasurmentMasterDto.getStatus());
			unitOfMeasurmentMaster.setUpdatedBy(unitOfMeasurmentMasterDto.getUpdatedBy());
			unitOfMeasurmentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(unitOfMeasurmentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(unitOfMeasurmentMaster);

	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response getDietUnitMasterById(Integer uomId, Integer organizationId) throws ApplicationException{
	try {
		String query="select " +
				"	uom.uom_id as \"uomId\", " +
				"	uom.uom_name as \"uomName\", " +
				"	uom.uom_description as \"uomDescription\" " +
				"from " +
				"	kitchen.m_unit_of_measurment_master uom " +
				"where " +
				"	uom.organization_id =:organizationId " +
				"	and uom.uom_id =:uomId "
				;
		List<UnitOfMeasurmentMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId", organizationId)
				.setParameter("uomId", uomId)
				.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();


	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response getDietUnitMasterList(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
	try {
		String query="select " +
				"	uom.uom_id as \"uomId\", " +
				"	uom.uom_name as \"uomName\", " +
				"	uom.uom_description as \"uomDescription\", " +
				"	uom.status as \"status\" " +
				"from " +
				"	kitchen.m_unit_of_measurment_master uom " +
				"where " +
				"	uom.organization_id =:organizationId "
				;
		List<UnitOfMeasurmentMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId", unitOfMeasurmentMasterDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response getDietUnitMasterCount(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
	try {

		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(UnitOfMeasurmentMaster.class);
		crit.add(Restrictions.eq("organizationId", unitOfMeasurmentMasterDto.getOrganizationId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();

		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response getActiveDietUnitMaster(Integer organizationId) throws ApplicationException{
	try {
		String query="select " +
				"	uom.uom_id as \"uomId\", " +
				"	uom.uom_name as \"uomName\", " +
				"	uom.uom_description as \"uomDescription\", " +
				"	uom.uom_name as \"uomName\" " +
				"from " +
				"	kitchen.m_unit_of_measurment_master uom " +
				"where " +
				"	uom.organization_id =:organizationId " +
				"	and uom.status = 'A'";
		List<UnitOfMeasurmentMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId", organizationId)
				.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response checkAvailableUnitByName(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto)
		throws ApplicationException {
	try {
		String query="select " +
				"	uom.uom_id as \"uomId\", " +
				"	uom.uom_name as \"uomName\", " +
				"	uom.uom_description as \"uomDescription\", " +
				"	uom.status as \"status\" " +
				"from " +
				"	kitchen.m_unit_of_measurment_master uom " +
				"where " +
				"	uom.organization_id =:organizationId " +
				"	and lower(uom.uom_name) = '" + unitOfMeasurmentMasterDto.getUomName()+"'"+
				"	and uom.status = 'A'";
		List<UnitOfMeasurmentMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId", unitOfMeasurmentMasterDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}


@Override
public Response checkAvailableUnitById(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto)
		throws ApplicationException {
	try {
		String query="select " +
				"	uom.uom_id as \"uomId\", " +
				"	uom.uom_name as \"uomName\", " +
				"	uom.uom_description as \"uomDescription\" " +
				"from " +
				"	kitchen.m_unit_of_measurment_master uom " +
				"where " +
				"	uom.organization_id =:organizationId " +
				"	and lower(uom.uom_name) = '" + unitOfMeasurmentMasterDto.getUomName()+"'"+
				"	and uom.uom_id =:uomId " +
				"	and uom.status = 'A'";
		List<UnitOfMeasurmentMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId", unitOfMeasurmentMasterDto.getOrganizationId())
				.setParameter("uomId", unitOfMeasurmentMasterDto.getUomId())
				.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}



}
