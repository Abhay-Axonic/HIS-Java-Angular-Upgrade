package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DependentDetailsDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.model.DependentDetails;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DependentDetailsDaoImpl extends GenericDao<DependentDetails, Integer>
		implements IDependentDetailsDao, ICommonConstants {

	static final Logger logger = Logger.getLogger(DependentDetailsDaoImpl.class);

	public DependentDetailsDaoImpl() {
		super(DependentDetails.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response saveDependentDetails(DependentDetailsDto detailsDto) throws ApplicationException {
		try {
			DependentDetails dependentDetails = mapper.map(detailsDto, DependentDetails.class,
					"DependentDetailsDto_to_DependentDetails");
			dependentDetails.setStatus('A');
			dependentDetails.setIsNok(dependentDetails.getIsNok()=='\u0000'?'N':dependentDetails.getIsNok());
			sessionFactory.getCurrentSession().save(dependentDetails);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
			logger.error("saveDependentDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDependentDetails(DependentDetailsDto dependentDetailsDto) throws ApplicationException {
		try {
			List<DependentDetailsDto> dependentDetailsDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPENDENT_DETAILS")
					.setParameter("empDocId", dependentDetailsDto.getEmpDocId())
					.setParameter("typeId",dependentDetailsDto.getTypeId())
					.setResultTransformer(Transformers.aliasToBean(DependentDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, dependentDetailsDtosList, null);

		} catch (HibernateException he) {
			logger.error("getDependentDetails failed", he);
		} catch (Exception e) {
			logger.error("getDependentDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDependentDetails(DependentDetailsDto dependentDetailsDtoNew) throws ApplicationException {
		try {
			DependentDetails dependentDetails = mapper.map(dependentDetailsDtoNew, DependentDetails.class,
					"DependentDetailsDto_to_DependentDetails");
			dependentDetails.setStatus('A');
			dependentDetails.setIsNok(dependentDetails.getIsNok()=='\u0000'?'N':dependentDetails.getIsNok());
			sessionFactory.getCurrentSession().update(dependentDetails);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
			logger.error("updateDependentDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactivePreviousDependents(DependentDetailsDto dependentDetailsDto) throws ApplicationException {
		try {
			
			sessionFactory.getCurrentSession().createQuery("UPDATE DependentDetails depDet set depDet.status='I', "
					+ "depDet.updatedBy=:updatedBy, "
					+ "depDet.updatedDate=:updatedDate "
					+ "WHERE depDet.typeId=:typeId "
					+ "AND depDet.empDocId=:empDocId "
					+ "AND depDet.status='A' ")
			.setParameter("updatedBy", dependentDetailsDto.getUpdatedBy())
			.setTimestamp("updatedDate", GlobalCommonDateUtils.getDate(dependentDetailsDto.getUpdatedDate(), "yyyy-M-dd"))
			.setParameter("typeId", dependentDetailsDto.getTypeId())
			.setParameter("empDocId",dependentDetailsDto.getEmpDocId())
			.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("inactivePreviousDependents failed", he);
		} catch (Exception e) {
			logger.error("inactivePreviousDependents failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response searchDependentDetailsOfDoctor(DependentDetailsDto dependentDetailsDto)
			throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTORS_DEPENDENT_DETAILS").getQueryString());

			if (dependentDetailsDto.getEmployeeName() != null && !dependentDetailsDto.getEmployeeName().isEmpty()) {
				query.append(" AND ( (lower(coalesce(doctor.first_name, '')) like concat(lower(:employeeName), '%') ")
						.append("OR lower(coalesce(doctor.last_name, '')) like concat(lower(:employeeName), '%') ) ")
						.append("OR lower(concat(coalesce(doctor.first_name, ''), ' ', coalesce(doctor.last_name, ''))) ")
						.append("like concat('%', lower(:employeeName), '%') )");
			}

			if (dependentDetailsDto.getEmpCode() != null && !dependentDetailsDto.getEmpCode().isEmpty()) {
				query.append(" AND doctor.doc_code like concat('%', lower(:empCode), '%') ");
			}

			if (dependentDetailsDto.getIdentificationTypeId() > 0) {
				query.append(" AND depandents.identification_type_id = :identificationTypeId ");
			}

			if (dependentDetailsDto.getIdentificationNumber() != null && !dependentDetailsDto.getIdentificationNumber().isEmpty()) {
				query.append(" AND lower(depandents.identification_number) LIKE concat('%', lower(:identificationNumber), '%') ");
			}

			if (dependentDetailsDto.getDependentName() != null && !dependentDetailsDto.getDependentName().isEmpty()) {
				query.append(" AND ( (lower(coalesce(depandents.first_name, '')) like concat(lower(:dependentName), '%') ")
						.append("OR lower(coalesce(depandents.last_name, '')) like concat(lower(:dependentName), '%') ) ")
						.append("OR lower(concat(coalesce(depandents.first_name, ''), ' ', coalesce(depandents.last_name, ''))) ")
						.append("like concat('%', lower(:dependentName), '%') )");
			}

			if (dependentDetailsDto.getTypeId() != null) {
				query.append(" AND depandents.type_id = :typeId AND depandents.status = 'A' ");
			}

			SQLQuery actualQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());

			if (dependentDetailsDto.getOffset() != null && dependentDetailsDto.getNoOfRecordsPerPage() != null) {
				actualQuery.setFirstResult(dependentDetailsDto.getOffset());
				actualQuery.setMaxResults(dependentDetailsDto.getNoOfRecordsPerPage());
			}
			actualQuery.setParameter("organizationId", dependentDetailsDto.getOrganizationId());
			if (dependentDetailsDto.getEmployeeName() != null && !dependentDetailsDto.getEmployeeName().isEmpty()) {
				actualQuery.setParameter("employeeName", dependentDetailsDto.getEmployeeName()); // Set parameter for employeeName
			}
			if(dependentDetailsDto.getEmpCode()!=null && !dependentDetailsDto.getEmpCode().isEmpty()) {
				actualQuery.setParameter("empCode", dependentDetailsDto.getEmpCode()); // Set parameter for empCode
			}
			if (dependentDetailsDto.getIdentificationTypeId() > 0) {
				actualQuery.setParameter("identificationTypeId", dependentDetailsDto.getIdentificationTypeId()) ;// Set parameter for identificationTypeId
			}
			if(dependentDetailsDto.getIdentificationNumber()!=null && !dependentDetailsDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", dependentDetailsDto.getIdentificationNumber()); // Set parameter for identificationNumber
			}

		   if(dependentDetailsDto.getDependentName()!=null && !dependentDetailsDto.getDependentName().isEmpty()) {
		    	actualQuery.setParameter("dependentName", dependentDetailsDto.getDependentName()) ;// Set parameter for dependentName
		   }
			if(dependentDetailsDto.getTypeId()!=null) {
				actualQuery.setParameter("typeId", dependentDetailsDto.getTypeId()); // Set parameter for typeId
			}
			List<DependentDetailsDto> dependentDetailsDtosList = actualQuery.setResultTransformer(Transformers.aliasToBean(DependentDetailsDto.class))
					.list();
			return new Response(SUCCESS, null, null, dependentDetailsDtosList, null);

		} catch (HibernateException he) {
			logger.error("searchDependentDetailsOfDoctor failed", he);
		} catch (Exception e) {
			logger.error("searchDependentDetailsOfDoctor failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response searchDependentDetailsOfEmployee(DependentDetailsDto dependentDetailsDto)
			throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMPLOYEES_DEPENDENT_DETAILS").getQueryString());

			if (dependentDetailsDto.getEmployeeName() != null && !dependentDetailsDto.getEmployeeName().isEmpty()) {
				query.append(" AND ( (lower(coalesce(employee.first_name, '')) like concat(lower(:employeeName), '%') ")
						.append("OR lower(coalesce(employee.last_name, '')) like concat(lower(:employeeName), '%') ) ")
						.append("OR lower(concat(coalesce(employee.first_name, ''), ' ', coalesce(employee.last_name, ''))) ")
						.append("like concat('%', lower(:employeeName), '%') )");
			}

			if (dependentDetailsDto.getEmpCode() != null && !dependentDetailsDto.getEmpCode().isEmpty()) {
				query.append(" AND employee.emp_code = :empCode ");
			}

			if (dependentDetailsDto.getIdentificationTypeId() > 0) {
				query.append(" AND depandents.identification_type_id = :identificationTypeId ");
			}

			if (dependentDetailsDto.getIdentificationNumber() != null && !dependentDetailsDto.getIdentificationNumber().isEmpty()) {
				query.append(" AND lower(depandents.identification_number) LIKE concat('%', lower(:identificationNumber), '%') ");
			}

			if (dependentDetailsDto.getDependentName() != null && !dependentDetailsDto.getDependentName().isEmpty()) {
				query.append(" AND ( (lower(coalesce(depandents.first_name, '')) like concat(lower(:dependentName), '%') ")
						.append("OR lower(coalesce(depandents.last_name, '')) like concat(lower(:dependentName), '%') ) ")
						.append("OR lower(concat(coalesce(depandents.first_name, ''), ' ', coalesce(depandents.last_name, ''))) ")
						.append("like concat('%', lower(:dependentName), '%') )");
			}

			if (dependentDetailsDto.getTypeId() != null) {
				query.append(" AND depandents.type_id = :typeId ");
			}

			SQLQuery actualQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());

			if (dependentDetailsDto.getOffset() != null && dependentDetailsDto.getNoOfRecordsPerPage() != null) {
				actualQuery.setFirstResult(dependentDetailsDto.getOffset());
				actualQuery.setMaxResults(dependentDetailsDto.getNoOfRecordsPerPage());
			}

			actualQuery.setParameter("organizationId", dependentDetailsDto.getOrganizationId());

			if (dependentDetailsDto.getEmployeeName() != null && !dependentDetailsDto.getEmployeeName().isEmpty()) {
				actualQuery.setParameter("employeeName", dependentDetailsDto.getEmployeeName()); // Set parameter for employeeName
			}

			if (dependentDetailsDto.getEmpCode() != null && !dependentDetailsDto.getEmpCode().isEmpty()) {
				actualQuery.setParameter("empCode", dependentDetailsDto.getEmpCode()); // Set parameter for empCode
			}

			if (dependentDetailsDto.getIdentificationTypeId() > 0) {
				actualQuery.setParameter("identificationTypeId", dependentDetailsDto.getIdentificationTypeId()); // Set parameter for identificationTypeId
			}

			if (dependentDetailsDto.getIdentificationNumber() != null && !dependentDetailsDto.getIdentificationNumber().isEmpty()) {
				actualQuery.setParameter("identificationNumber", dependentDetailsDto.getIdentificationNumber()); // Set parameter for identificationNumber
			}

			if (dependentDetailsDto.getDependentName() != null && !dependentDetailsDto.getDependentName().isEmpty()) {
				actualQuery.setParameter("dependentName", dependentDetailsDto.getDependentName()); // Set parameter for dependentName
			}

			if (dependentDetailsDto.getTypeId() != null) {
				actualQuery.setParameter("typeId", dependentDetailsDto.getTypeId()); // Set parameter for typeId
			}

			List<DependentDetailsDto> dependentDetailsDtosList = actualQuery
					.setResultTransformer(Transformers.aliasToBean(DependentDetailsDto.class))
					.list();

			return new Response(SUCCESS, null, null, dependentDetailsDtosList, null);
		} catch (HibernateException he) {
			logger.error("searchDependentDetailsOfEmployee failed", he);
		} catch (Exception e) {
			logger.error("searchDependentDetailsOfEmployee failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
 