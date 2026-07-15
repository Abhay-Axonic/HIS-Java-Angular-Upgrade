package com.param.global.org.common.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.EmployeeDesignationMaster;
import com.param.global.org.common.dto.DesignationMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })

public class DesignationMasterDaoImpl extends GenericDao<EmployeeDesignationMaster, Integer>
		implements IDesignationMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(DesignationMasterDaoImpl.class);


	public DesignationMasterDaoImpl() {
		super(EmployeeDesignationMaster.class);
	}

	@Override
	public Response saveDesignationMaster(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			EmployeeDesignationMaster employeeDesignationMaster=new EmployeeDesignationMaster();
			employeeDesignationMaster.setCreatedBy(designationMasterDto.getCreatedBy());
			employeeDesignationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(designationMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss" ));
			employeeDesignationMaster.setEmployeeDesignationCode(designationMasterDto.getEmployeeDesignationCode());
			employeeDesignationMaster.setEmployeeDesignationDescription(designationMasterDto.getEmployeeDesignationDescription());
			employeeDesignationMaster.setOrganizationId(designationMasterDto.getOrganizationId());
			employeeDesignationMaster.setStatus(designationMasterDto.getStatus());
			employeeDesignationMaster.setUpdatedBy(designationMasterDto.getUpdatedBy());
			employeeDesignationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(designationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(employeeDesignationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveDesignationMaster failed", he);
		} catch (Exception e) {
		logger.error("saveDesignationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getDesignationMasterById(int designationId, int orgId) throws ApplicationException {
		try {
			List<DesignationMasterDto> designationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DESIGNATION_MASTER_BY_ID").setInteger("designationId", designationId)
					.setInteger("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(DesignationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, designationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getDesignationMasterById failed", he);
		} catch (Exception e) {
		logger.error("getDesignationMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDesignationMasterList(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			List<DesignationMasterDto> designationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DESIGNATION_MASTER_LIST")
					.setInteger("orgId", designationMasterDto.getOrganizationId())
					.setFirstResult(designationMasterDto.getOffset() != null ? designationMasterDto.getOffset() : 0)
					.setMaxResults(designationMasterDto.getNoOfRecordsPerPage() != null ? designationMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DesignationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, designationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getDesignationMasterList failed", he);
		} catch (Exception e) {
		logger.error("getDesignationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override//minesh
	public Response getActiveDesignationMasterList(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			List<DesignationMasterDto> designationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DESIGNATION_MASTER_LIST")
					.setInteger("orgId", designationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DesignationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, designationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getActiveDesignationMasterList failed", he);
		} catch (Exception e) {
		logger.error("getActiveDesignationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}//minesh

	@Override
	public Response updateDesignationMaster(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			EmployeeDesignationMaster employeeDesignationMaster=findById(designationMasterDto.getEmployeeDesignationId());
			employeeDesignationMaster.setEmployeeDesignationCode(designationMasterDto.getEmployeeDesignationCode());
			employeeDesignationMaster.setEmployeeDesignationDescription(designationMasterDto.getEmployeeDesignationDescription());
			employeeDesignationMaster.setUpdatedBy(designationMasterDto.getUpdatedBy());
			employeeDesignationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(designationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(employeeDesignationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateDesignationMaster failed", he);
		} catch (Exception e) {
		logger.error("updateDesignationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDesignationMasterStatus(DesignationMasterDto designationMasterDto)
			throws ApplicationException {
		try {
			EmployeeDesignationMaster employeeDesignationMaster=findById(designationMasterDto.getEmployeeDesignationId());
			employeeDesignationMaster.setStatus(designationMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			employeeDesignationMaster.setUpdatedBy(designationMasterDto.getUpdatedBy());
			employeeDesignationMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(designationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(employeeDesignationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateDesignationMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateDesignationMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDesignationMasterCount(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(EmployeeDesignationMaster.class); 
			c.add(Restrictions.eq("organizationId", designationMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
		} catch (HibernateException he) {
		logger.error("getDesignationMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getDesignationMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDesignationMasterByName(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			List<DesignationMasterDto> designationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DESIGNATION_MASTER_BY_NAME")
					.setInteger("orgId", designationMasterDto.getOrganizationId())
					.setString("designationDesc", designationMasterDto.getEmployeeDesignationDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DesignationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, designationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getDesignationMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getDesignationMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDesignationMasterByNameNotById(DesignationMasterDto designationMasterDto)
			throws ApplicationException {
		try {
			List<DesignationMasterDto> designationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DESIGNATION_MASTER_BY_NAME_NOT_BY_ID")
					.setInteger("designationId", designationMasterDto.getEmployeeDesignationId())
					.setInteger("orgId", designationMasterDto.getOrganizationId())
					.setString("designationDesc", designationMasterDto.getEmployeeDesignationDescription())
					.setResultTransformer(Transformers.aliasToBean(DesignationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, designationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getDesignationMasterByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getDesignationMasterByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
