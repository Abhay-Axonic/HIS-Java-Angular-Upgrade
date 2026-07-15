package com.param.global.billing.dao;

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

import com.param.global.billing.dto.EmployeeCategoryMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.EmployeeCategoryMaster;
import com.param.global.unit.dto.UserRoleMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmployeeCategoryMasterDaoImpl extends GenericDao<EmployeeCategoryMaster, Integer>
		implements IEmployeeCategoryMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(EmployeeCategoryMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	public EmployeeCategoryMasterDaoImpl() {
		super(EmployeeCategoryMaster.class);
	}

	@Override
	public Response saveEmployeeCategoryMaster(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try {
			EmployeeCategoryMaster employeeCategoryMaster = new EmployeeCategoryMaster();
			employeeCategoryMaster.setCreatedBy(employeeCategoryMasterDto.getCreatedBy());
			employeeCategoryMaster.setCreatedDate(GlobalCommonDateUtils.getDate(employeeCategoryMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			employeeCategoryMaster.setEmployeeCategoryCode(employeeCategoryMasterDto.getEmployeeCategoryCode());
			employeeCategoryMaster.setEmployeeCategoryDescription(employeeCategoryMasterDto.getEmployeeCategoryDescription());
			employeeCategoryMaster.setOrganizationId(employeeCategoryMasterDto.getOrganizationId());
			employeeCategoryMaster.setStatus(employeeCategoryMasterDto.getStatus());
			employeeCategoryMaster.setUpdatedBy(employeeCategoryMasterDto.getUpdatedBy());
			employeeCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(employeeCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(employeeCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			logger.error("saveEmployeeCategoryMaster failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response getEmployeeCategoryMasterById(int employeeId, int orgId) throws ApplicationException {
		try {
			List<EmployeeCategoryMasterDto> employeeCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMPLOYEE_CATEGORY_MASTER_BY_ID").setParameter("employeeId", employeeId)
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(EmployeeCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, employeeCategoryMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getEmployeeCategoryMasterById failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response getEmployeeCategoryMasterList(EmployeeCategoryMasterDto employeeCategoryMasterDto) throws ApplicationException {
		try {
			List<EmployeeCategoryMasterDto> employeeCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMPLOYEE_CATEGORY_MASTER_LIST")
					.setParameter("orgId", employeeCategoryMasterDto.getOrganizationId())
					//.setFirstResult(employeeCategoryMasterDto.getOffset() != null ? employeeCategoryMasterDto.getOffset() : 0) //uncomment by minesh
					//.setMaxResults(employeeCategoryMasterDto.getNoOfRecordsPerPage() != null ? employeeCategoryMasterDto.getNoOfRecordsPerPage() : 10) //uncomment by minesh
					.setResultTransformer(Transformers.aliasToBean(EmployeeCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, employeeCategoryMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getEmployeeCategoryMasterList failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response getActiveEmployeeCategoryMasterList(EmployeeCategoryMasterDto employeeCategoryMasterDto) throws ApplicationException {
		try {
			List<EmployeeCategoryMasterDto> employeeCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE EMPLOYEE_CATEGORY_MASTER_LIST")
					.setParameter("orgId", employeeCategoryMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(EmployeeCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, employeeCategoryMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveEmployeeCategoryMasterList failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response updateEmployeeCategoryMaster(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try {
			EmployeeCategoryMaster employeeCategoryMaster = findById(employeeCategoryMasterDto.getEmployeeCategoryId());
			employeeCategoryMaster.setEmployeeCategoryCode(employeeCategoryMasterDto.getEmployeeCategoryCode());
			employeeCategoryMaster
					.setEmployeeCategoryDescription(employeeCategoryMasterDto.getEmployeeCategoryDescription());
			employeeCategoryMaster.setUpdatedBy(employeeCategoryMasterDto.getUpdatedBy());
			employeeCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(employeeCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(employeeCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateEmployeeCategoryMaster failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response updateEmployeeCategoryMasterStatus(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try {
			EmployeeCategoryMaster employeeCategoryMaster = findById(employeeCategoryMasterDto.getEmployeeCategoryId());
			employeeCategoryMaster.setStatus(employeeCategoryMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			employeeCategoryMaster.setUpdatedBy(employeeCategoryMasterDto.getUpdatedBy());
			employeeCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(employeeCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(employeeCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateEmployeeCategoryMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response getEmployeeCategoryMasterByNameNotId(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try {

			List<EmployeeCategoryMasterDto> employeeCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMPLOYEE_CATEGORY_MASTER_BY_NAME_NOT_ID")
					.setParameter("employeeId", employeeCategoryMasterDto.getEmployeeCategoryId())
					.setParameter("orgId", employeeCategoryMasterDto.getOrganizationId())
					.setParameter("empName", employeeCategoryMasterDto.getEmployeeCategoryDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(EmployeeCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, employeeCategoryMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getEmployeeCategoryMasterByNameNotId failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response getEmployeeCategoryMasterByName(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try {
			List<EmployeeCategoryMasterDto> employeeCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMPLOYEE_CATEGORY_MASTER_BY_NAME")
					.setParameter("orgId", employeeCategoryMasterDto.getOrganizationId())
					.setParameter("empName", employeeCategoryMasterDto.getEmployeeCategoryDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(EmployeeCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, employeeCategoryMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getEmployeeCategoryMasterByName failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response getEmployeeCategoryCount(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(EmployeeCategoryMaster.class); 
			c.add(Restrictions.eq("organizationId", employeeCategoryMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(HibernateException he){
			logger.error("getEmployeeCategoryCount failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("getEmployeeCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllEmployeeCategoryMasterList(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try {
			List<EmployeeCategoryMasterDto> employeeCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_EMPLOYEE_CATEGORY_MASTER_LIST")
					.setParameter("orgId", employeeCategoryMasterDto.getOrganizationId())
					.setFirstResult(employeeCategoryMasterDto.getOffset() != null ? employeeCategoryMasterDto.getOffset() : 0)
					.setMaxResults(employeeCategoryMasterDto.getNoOfRecordsPerPage() != null ? employeeCategoryMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(EmployeeCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, employeeCategoryMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getAllEmployeeCategoryMasterList failed", he);
			throw new ServiceException(he.getMessage());
		}
	}



}
