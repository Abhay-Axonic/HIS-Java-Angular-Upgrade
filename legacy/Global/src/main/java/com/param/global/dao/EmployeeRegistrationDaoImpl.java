package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.EmployeeRegistrationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.model.EmployeeDetails;
import com.param.global.model.EmployeeMaster;
import com.param.global.model.OTIMSUserRoleMapper;
import com.param.global.model.StaffSpecialityMapper;
import com.param.global.model.StaffSubSplecialityMapper;
import com.param.global.unit.dto.OTIMSUserRoleMapperDto;
import com.param.global.utility.UserComponent;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes"})
public class EmployeeRegistrationDaoImpl extends GenericDao<EmployeeMaster, Integer>implements IEmployeeRegistrationDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(EmployeeRegistrationDaoImpl.class);


	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	private IUserMasterDao iUserMasterDao;
	
	public EmployeeRegistrationDaoImpl() {
		super(EmployeeMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveEmployeeRegistration(EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			EmployeeMaster employeeMaster = mapper.map(employeeRegistrationDto,EmployeeMaster.class,
					"EmployeeMasterDto_to_EmployeeMaster");
			employeeMaster.setStatus('A');
			employeeMaster=save(employeeMaster);
			Integer eId=employeeMaster.getEmployeeId();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, eId);
		}catch (Exception e) {
		logger.error("saveEmployeeRegistration failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkUniqueEmployee(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{
			List<EmployeeRegistrationDto> employeeRegistrationDtosList = null;
		if(employeeRegistrationDto.getEmployeeId()!=null)
			{
			StringBuilder finalQuery=new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_UNIQUE_EMPLOYEE").getQueryString());
			finalQuery.append(" AND et.employeeId!="+employeeRegistrationDto.getEmployeeId());	
				
			employeeRegistrationDtosList=sessionFactory.getCurrentSession().createQuery(finalQuery.toString())
					.setParameter("orgId", employeeRegistrationDto.getOrganizationId())
					//.setParameter("unitId", employeeRegistrationDto.getUnitId())
					.setParameter("empCode", employeeRegistrationDto.getEmpCode())
					.setParameter("mobileNumber", employeeRegistrationDto.getMobileNumber())
					.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
			}
		else{
			employeeRegistrationDtosList=sessionFactory.getCurrentSession().getNamedQuery("CHECK_UNIQUE_EMPLOYEE")
					.setParameter("orgId", employeeRegistrationDto.getOrganizationId())
					//.setParameter("unitId", employeeRegistrationDto.getUnitId())
					.setParameter("empCode", employeeRegistrationDto.getEmpCode())
					.setParameter("mobileNumber", employeeRegistrationDto.getMobileNumber())
					.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
		}
		return new Response(SUCCESS, SUCCESS_CODE, null, employeeRegistrationDtosList, null);
		}catch (Exception e) {
		logger.error("checkUniqueEmployee failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try {
			EmployeeDetails employeeDetails = mapper.map(employeeRegistrationDto,EmployeeDetails.class,
					"EmployeeMasterDto_to_EmployeeDetails");
			employeeDetails.setStatus('A');
			Integer employeeDetailsId=(Integer) sessionFactory.getCurrentSession().save(employeeDetails);
			employeeRegistrationDto.setEmployeeDetailsId(employeeDetailsId);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch (Exception e) {
		logger.error("saveEmployeeDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, employeeRegistrationDto);
	}

	@Override
	public Response getEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{
			
			List<EmployeeRegistrationDto> employeeRegistrationDtosList=sessionFactory.getCurrentSession().getNamedQuery("GET_EMPLOYEE_DETAILS")
					.setParameter("organizationId", employeeRegistrationDto.getOrganizationId())
					//.setParameter("unitId", employeeRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, employeeRegistrationDtosList, null);
			}catch (Exception e) {
			logger.error("getEmployeeDetails failed", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
				EmployeeMaster employeeMaster = mapper.map(employeeRegistrationDto,EmployeeMaster.class,
						"EmployeeMasterDto_to_EmployeeMaster");
			employeeMaster.setStatus('A');
				session.update(employeeMaster);
				
				EmployeeDetails employeeDetails = mapper.map(employeeRegistrationDto,EmployeeDetails.class,
						"EmployeeMasterDto_to_EmployeeDetails");
			employeeDetails.setStatus('A');
				session.update(employeeDetails);
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateEmployeeDetails failed", he);
			} catch (Exception e) {
			logger.error("updateEmployeeDetails failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateEmployeeStatus(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE EmployeeMaster emp set emp.status=:status, "
					+ "emp.updatedBy=:updatedBy, "
					+ "emp.updatedDate=:updatedDate "
					+ "WHERE emp.employeeId=:employeeId ")
			.setParameter("updatedBy", employeeRegistrationDto.getUpdatedBy())
			.setTimestamp("updatedDate", GlobalCommonDateUtils.getDate(employeeRegistrationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.setParameter("employeeId", employeeRegistrationDto.getEmployeeId())
			.setCharacter("status", employeeRegistrationDto.getStatus())
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createQuery("UPDATE EmployeeDetails emp set emp.status=:status, "
					+ "emp.updatedBy=:updatedBy, "
					+ "emp.updatedDate=:updatedDate "
					+ "WHERE emp.employeeId=:employeeId ")
			.setParameter("updatedBy", employeeRegistrationDto.getUpdatedBy())
			.setTimestamp("updatedDate", GlobalCommonDateUtils.getDate(employeeRegistrationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.setParameter("employeeId", employeeRegistrationDto.getEmployeeId())
			.setCharacter("status", employeeRegistrationDto.getStatus())
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createQuery("UPDATE DependentDetails depDet set depDet.status=:status, "
					+ "depDet.updatedBy=:updatedBy, "
					+ "depDet.updatedDate=:updatedDate "
					+ "WHERE depDet.empDocId=:employeeId "
					+ "AND depDet.typeId=:typeId ")
			.setParameter("updatedBy", employeeRegistrationDto.getUpdatedBy())
			.setTimestamp("updatedDate", GlobalCommonDateUtils.getDate(employeeRegistrationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.setParameter("employeeId", employeeRegistrationDto.getEmployeeId())
			.setParameter("typeId", employeeRegistrationDto.getTypeId())
			.setCharacter("status", employeeRegistrationDto.getStatus())
			.executeUpdate();
			
			UserMasterDto userMasterDtos = new UserMasterDto();
            userMasterDtos.setUserId(employeeRegistrationDto.getUserId());		
	    	userMasterDtos.setOrganizationId(employeeRegistrationDto.getOrganizationId());		
	    	userMasterDtos.setUnitId(employeeRegistrationDto.getUnitId());		
            		
			Response loginResponse = iUserMasterDao.getUserDataByIdNew(userMasterDtos);
			
			 if(employeeRegistrationDto.getStatus() == 'I') {
				if (loginResponse.getStatus().equals(SUCCESS)) {
					 UserMasterDto userMasterDto = new UserMasterDto();
					 userMasterDto = (UserMasterDto) loginResponse.getObject();
					 userMasterDto.setStatus("I");
					 UserComponent.updateUserDetailsForAuth(userMasterDto);
				}
			 }
	    
	    
			
			if(employeeRegistrationDto.getUserId() != null && employeeRegistrationDto.getUserId() > 0){
			sessionFactory.getCurrentSession().createQuery("UPDATE UserMaster userMst set userMst.status=:status "
					+ "WHERE userMst.userId=:userId ")
			.setParameter("userId", employeeRegistrationDto.getUserId())
			.setCharacter("status", employeeRegistrationDto.getStatus())
			.executeUpdate();
			}


			 if(employeeRegistrationDto.getStatus() == 'A') {
					if (loginResponse.getStatus().equals(SUCCESS)) {
						 UserMasterDto userMasterDto = new UserMasterDto();
						 userMasterDto = (UserMasterDto) loginResponse.getObject();
						 userMasterDto.setStatus("A");
						 UserComponent.updateUserDetailsForAuth(userMasterDto);
					}
			    }
					
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateEmployeeStatus failed", he);
			} catch (Exception e) {
				throw e;
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchEmployeeByCriteria(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {

			StringBuilder rawQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMPLOYEE_DETAILS").getQueryString());
			
			if(employeeRegistrationDto.getEmployeeName()!=null && !employeeRegistrationDto.getEmployeeName().isEmpty())
			{
				rawQuery.append(" AND ((lower(coalesce(employee.firstName, '')) like concat( lower(:employeeName ), '%')  OR lower(coalesce( employee.lastName, '' )) like concat( lower('"+employeeRegistrationDto.getEmployeeName()+"' ), '%') )"
						+ " OR lower(concat(coalesce(employee.firstName),' ',coalesce(employee.lastName,''))) LIKE concat('%' , lower(:employeeName),'%') )");
			
			}if(employeeRegistrationDto.getEmpCategoryId() != null && employeeRegistrationDto.getEmpCategoryId()>0)
			{
				rawQuery.append(" AND employee.empCategoryId=:empCategoryId ");
			}if(employeeRegistrationDto.getEmpDesignationId()!= null && employeeRegistrationDto.getEmpDesignationId()>0)
			{
				rawQuery.append(" AND employee.empDesignationId=:empDesignationId ");
			}if(employeeRegistrationDto.getSpecialityId() != null && employeeRegistrationDto.getSpecialityId()>0)
			{
				rawQuery.append(" AND employee.specialityId=:specialityId ");
			}if(employeeRegistrationDto.getEmpCode()!=null && !employeeRegistrationDto.getEmpCode().isEmpty())
			{
				rawQuery.append(" AND lower(employee.empCode) LIKE concat('%' , lower( :empCode),'%') ");
			}if(employeeRegistrationDto.getIdentificationTypeId()!=null && employeeRegistrationDto.getIdentificationTypeId() > 0)
			{
				rawQuery.append(" AND employee.identificationTypeId=:identificationTypeId");
			}if(employeeRegistrationDto.getIdentificationNumber()!=null && !employeeRegistrationDto.getIdentificationNumber().isEmpty())
			{
				rawQuery.append(" AND lower(employee.identificationNumber) LIKE concat('%',LOWER(:identificationNumber),'%')");
			}
			
			if(employeeRegistrationDto.getIsPatientSearchStaff() != null && employeeRegistrationDto.getIsPatientSearchStaff() == 'Y')
			rawQuery.append(" AND employee.status='A' ");
			
			List<EmployeeRegistrationDto> employeeRegistrationDtosList;
			Query createQuery;

			if(employeeRegistrationDto.getIsPatientSearch()!=null && employeeRegistrationDto.getIsPatientSearch()=='Y') {
				 createQuery = sessionFactory.getCurrentSession()
						.createQuery(rawQuery.toString())
						.setParameter("organizationId", employeeRegistrationDto.getOrganizationId());

			}else {
				 createQuery = sessionFactory.getCurrentSession()
						.createQuery(rawQuery.toString())
						.setParameter("organizationId", employeeRegistrationDto.getOrganizationId())
						.setFirstResult(employeeRegistrationDto.getOffset() != null ? employeeRegistrationDto.getOffset() : 0)
						.setMaxResults(employeeRegistrationDto.getNoOfRecordsPerPage() != null ? employeeRegistrationDto.getNoOfRecordsPerPage() : 10);
			}

			if(employeeRegistrationDto.getEmployeeName()!=null && !employeeRegistrationDto.getEmployeeName().isEmpty())
			{
				createQuery.setParameter("employeeName",employeeRegistrationDto.getEmployeeName());
				rawQuery.append(" AND ((lower(coalesce(employee.firstName, '')) like concat( lower('"+employeeRegistrationDto.getEmployeeName()+"'), '%')  OR lower(coalesce( employee.lastName, '' )) like concat( lower('"+employeeRegistrationDto.getEmployeeName()+"' ), '%') )"
						+ " OR lower(concat(coalesce(employee.firstName),' ',coalesce(employee.lastName,''))) LIKE concat('%' , lower('"+employeeRegistrationDto.getEmployeeName()+"'),'%') )");

			}if(employeeRegistrationDto.getEmpCategoryId() != null && employeeRegistrationDto.getEmpCategoryId()>0)
			{
				createQuery.setParameter("empCategoryId",employeeRegistrationDto.getEmpCategoryId());
			}if(employeeRegistrationDto.getEmpDesignationId()!= null && employeeRegistrationDto.getEmpDesignationId()>0)
			{
				createQuery.setParameter("empDesignationId",employeeRegistrationDto.getEmpDesignationId());
			}if(employeeRegistrationDto.getSpecialityId() != null && employeeRegistrationDto.getSpecialityId()>0)
			{
				createQuery.setParameter("specialityId",employeeRegistrationDto.getSpecialityId());
			}if(employeeRegistrationDto.getEmpCode()!=null && !employeeRegistrationDto.getEmpCode().isEmpty())
			{
				createQuery.setParameter("empCode",employeeRegistrationDto.getEmpCode());
			}if(employeeRegistrationDto.getIdentificationTypeId()!=null && employeeRegistrationDto.getIdentificationTypeId() > 0)
			{
				createQuery.setParameter("identificationTypeId",employeeRegistrationDto.getIdentificationTypeId());
			}if(employeeRegistrationDto.getIdentificationNumber()!=null && !employeeRegistrationDto.getIdentificationNumber().isEmpty())
			{
				createQuery.setParameter("identificationNumber",employeeRegistrationDto.getIdentificationNumber());
			}

			employeeRegistrationDtosList = createQuery.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, employeeRegistrationDtosList, null);
		} catch (Exception e) {
		logger.error("searchEmployeeByCriteria failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateEmployeeImage(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		 try{
		      Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_EMPLOYEE_IMAGE_PATH")
		          .setParameter("orgId", employeeRegistrationDto.getOrganizationId())
		          .setParameter("employeeId", employeeRegistrationDto.getEmployeeId())
		          .setParameter("employeeImage", employeeRegistrationDto.getEmployeeImage()).executeUpdate();
		      if(result != null){
		          return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		      }
		              
		  }catch(HibernateException he){
		  logger.error("updateEmployeeImage failed", he);
		  }catch(Exception e){
		  logger.error("updateEmployeeImage failed", e);
		  }
		  return new Response<>(ERROR, ERROR_CODE, null, null, null);
		  }

	@Override
	public Response searchEmployeeByName(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
try{
			
			List<EmployeeRegistrationDto> list=sessionFactory.getCurrentSession().getNamedQuery("SEARCH_EMPLOYEE_BY_NAME")
					.setParameter("name", "%"+employeeRegistrationDto.getFirstName().toLowerCase()+"%")
					.setParameter("orgId", employeeRegistrationDto.getOrganizationId())
			        .setParameter("unitId", employeeRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch (Exception e) {
			logger.error("searchEmployeeByName failed", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveOTIMSUserRoleMapper(OTIMSUserRoleMapperDto otimsUserRoleMapperDto) throws ApplicationException {
		try{
			OTIMSUserRoleMapper OTIMSUserRoleMapperObj=new OTIMSUserRoleMapper();
			OTIMSUserRoleMapperObj.setRoleId(otimsUserRoleMapperDto.getRoleId());
			OTIMSUserRoleMapperObj.setUserId(otimsUserRoleMapperDto.getUserId());
			OTIMSUserRoleMapperObj.setCreatedBy(otimsUserRoleMapperDto.getCreatedBy());
			OTIMSUserRoleMapperObj.setUpdatedBy(otimsUserRoleMapperDto.getUpdatedBy());
			OTIMSUserRoleMapperObj.setOrganisationId(otimsUserRoleMapperDto.getOrganisationId());
			OTIMSUserRoleMapperObj.setUnitId(otimsUserRoleMapperDto.getUnitId());
			OTIMSUserRoleMapperObj.setStatus(otimsUserRoleMapperDto.getStatus());
			OTIMSUserRoleMapperObj.setCreatedDate(GlobalCommonDateUtils.getDate(otimsUserRoleMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			OTIMSUserRoleMapperObj.setUpdatedDate(GlobalCommonDateUtils.getDate(otimsUserRoleMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().save(OTIMSUserRoleMapperObj);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, OTIMSUserRoleMapperObj);
		}catch (Exception e) {
		logger.error("saveOTIMSUserRoleMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveRoleStatusOfOtimsUser(OTIMSUserRoleMapperDto otimsUserRoleMapperDto) {
		try {
			String query = "UPDATE OTIMSUserRoleMapper " +
					"SET status = :status " +
					"WHERE userId = :userId " +
					"AND unitId = :unitId";

			Integer count = sessionFactory.getCurrentSession()
					.createQuery(query)
					.setParameter("status", "I")  // Parameter for the status value
					.setParameter("userId", otimsUserRoleMapperDto.getUserId())  // Parameter for userId
					.setParameter("unitId", otimsUserRoleMapperDto.getUnitId())  // Parameter for unitId
					.executeUpdate();

			if(count>0) 
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			else
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		} catch (HibernateException he) {
		logger.error("inactiveRoleStatusOfOtimsUser failed", he);
		} catch (Exception e) {
		logger.error("inactiveRoleStatusOfOtimsUser failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveUnitMapperByUserId(UserUnitMapperDto userUnitMapperDto) throws ApplicationException {
		try {
			Integer count=sessionFactory.getCurrentSession().createQuery("UPDATE UserUnitMapper "
					+ " SET status='I'"
					+ " WHERE userId=:id ").setParameter("id",userUnitMapperDto.getUserId()).executeUpdate();
			if(count>0) 
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			else
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		} catch (HibernateException he) {
		logger.error("inactiveUnitMapperByUserId failed", he);
		} catch (Exception e) {
		logger.error("inactiveUnitMapperByUserId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmployeeRegistrationCount(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(EmployeeMaster.class);
			crit.add(Restrictions.eq("organizationId", employeeRegistrationDto.getOrganizationId()));
			//crit.add(Restrictions.eq("unitId", employeeRegistrationDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
			logger.error("getEmployeeRegistrationCount failed", he);
			} catch (Exception e) {
			logger.error("getEmployeeRegistrationCount failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmployeeListBySpecialityAndSubSpeciality(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try{
			
			List<EmployeeRegistrationDto> list=sessionFactory.getCurrentSession().getNamedQuery("GET_EMPLOYEE_LIST_BY_SPECIALITY_AND_SUBSPECIALITY")
					.setParameter("specialityId", employeeRegistrationDto.getSpecialityId())
					.setParameter("organizationId", employeeRegistrationDto.getOrganizationId())
			        .setParameter("unitId", employeeRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch (Exception e) {
			logger.error("getEmployeeListBySpecialityAndSubSpeciality failed", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response validateEmpCode(String empCode,Integer orgId,Integer doctorId ,Integer employeeId) {
		try {
			Integer EMPID ;
			if(doctorId == 0 && employeeId == 0) {
				 EMPID = (Integer)sessionFactory.getCurrentSession()
							.createSQLQuery("select emp.employee_id as \"employeeId\"  "
										+ "	from m_employee_master emp "
										+ " where emp.emp_code like :empCode "
										+ " and emp.organization_id=:orgId  "
										+ " union "
										+ " select mdm.doctor_id as \"employeeId\" "
										+ " from doctor.m_doctor_master mdm "
										+ " where mdm.doc_code like :empCode "
										+ " and mdm.organization_id =:orgId "
										+ " limit 1")
						 .setParameter("empCode",empCode)
						 .setParameter("orgId",orgId)
							.uniqueResult();
			}else {
				 EMPID = (Integer)sessionFactory.getCurrentSession()
							.createSQLQuery("select emp.employee_id as \"employeeId\"  "
										+ "	from m_employee_master emp "
										+ " where emp.emp_code like '"+empCode+"' "
										+ " and emp.employee_id !="+employeeId
										+ " and emp.organization_id="+orgId+" "
										+ " union "
										+ " select mdm.doctor_id as \"employeeId\" "
										+ " from doctor.m_doctor_master mdm "
										+ " where mdm.doc_code like '"+empCode+"' "
										+ "  and mdm.doctor_id !="+ doctorId
										+ " and mdm.organization_id = "+orgId+" "
										+ " limit 1")
							.uniqueResult();
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, EMPID);
			
		} catch (HibernateException he) {
		logger.error("validateEmpCode failed", he);
		} catch (Exception e) {
		logger.error("validateEmpCode failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response universalSearchForEmployee(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {

			StringBuilder rawQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("UNIVERSAL_SEARCH_FOR_EMPLOYEE_DETAILS").getQueryString());
			
			if(employeeRegistrationDto.getEmployeeName()!=null && !employeeRegistrationDto.getEmployeeName().isEmpty())
			{
				rawQuery.append(" AND ((lower(coalesce(employee.firstName, '')) like concat( lower(:employeeName), '%')  OR lower(coalesce( employee.lastName, '' )) like concat( lower('"+employeeRegistrationDto.getEmployeeName()+"' ), '%') )"
						+ " OR lower(concat(coalesce(employee.firstName),' ',coalesce(employee.lastName,''))) LIKE concat('%' , lower(:employeeName),'%') )");
			
			}if(employeeRegistrationDto.getEmpCategoryId()>0)
			{
				rawQuery.append(" AND employee.empCategoryId=:empCategoryId");
			}if(employeeRegistrationDto.getEmpDesignationId()>0)
			{
				rawQuery.append(" AND employee.empDesignationId=:empDesignationId ");
			}if(employeeRegistrationDto.getSpecialityId()>0)
			{
				rawQuery.append(" AND employee.specialityId=:specialityId ");
			}if(employeeRegistrationDto.getEmpCode()!=null && !employeeRegistrationDto.getEmpCode().isEmpty())
			{
				rawQuery.append(" AND employee.empCode LIKE concat('%' , lower(:empCode),'%') ");
			}if(employeeRegistrationDto.getIdentificationTypeId()>0)
			{
				rawQuery.append(" AND employee.identificationTypeId=:identificationTypeId");
			}if(employeeRegistrationDto.getIdentificationNumber()!=null && !employeeRegistrationDto.getIdentificationNumber().isEmpty())
			{
				rawQuery.append(" AND lower(employee.identificationNumber) LIKE concat('%',LOWER(:identificationNumber),'%')");
			}
			
			if(employeeRegistrationDto.getIsPatientSearchStaff() != null && employeeRegistrationDto.getIsPatientSearchStaff() == 'Y')
			rawQuery.append(" AND employee.status='A' ");
			
			List<EmployeeRegistrationDto> employeeRegistrationDtosList;
			Query createQuery;
			if(employeeRegistrationDto.getIsPatientSearch()!=null && employeeRegistrationDto.getIsPatientSearch()=='Y') {
				 createQuery =  sessionFactory.getCurrentSession()
						.createQuery(rawQuery.toString())
						.setParameter("organizationId", employeeRegistrationDto.getOrganizationId())
                        .setParameter("mobileNumber", "%"+employeeRegistrationDto.getMobileNumber().trim().toLowerCase()+"%");
			}else {
				 createQuery = sessionFactory.getCurrentSession()
						.createQuery(rawQuery.toString())
						.setParameter("organizationId", employeeRegistrationDto.getOrganizationId())
						.setParameter("mobileNumber", "%" + employeeRegistrationDto.getMobileNumber().trim().toLowerCase() + "%")
						.setFirstResult(employeeRegistrationDto.getOffset() != null ? employeeRegistrationDto.getOffset() : 0)
						.setMaxResults(employeeRegistrationDto.getNoOfRecordsPerPage() != null ? employeeRegistrationDto.getNoOfRecordsPerPage() : 10);
			}

			if(employeeRegistrationDto.getEmployeeName()!=null && !employeeRegistrationDto.getEmployeeName().isEmpty())
			{
				createQuery.setParameter("employeeName",employeeRegistrationDto.getEmployeeName());
				rawQuery.append(" AND ((lower(coalesce(employee.firstName, '')) like concat( lower('"+employeeRegistrationDto.getEmployeeName()+"'), '%')  OR lower(coalesce( employee.lastName, '' )) like concat( lower('"+employeeRegistrationDto.getEmployeeName()+"' ), '%') )"
						+ " OR lower(concat(coalesce(employee.firstName),' ',coalesce(employee.lastName,''))) LIKE concat('%' , lower('"+employeeRegistrationDto.getEmployeeName()+"'),'%') )");

			}if(employeeRegistrationDto.getEmpCategoryId()>0)
			{
				createQuery.setParameter("empCategoryId",employeeRegistrationDto.getEmpCategoryId());
			}if(employeeRegistrationDto.getEmpDesignationId()>0)
			{
				createQuery.setParameter("empDesignationId",employeeRegistrationDto.getEmpDesignationId());
			}if(employeeRegistrationDto.getSpecialityId()>0)
			{
				createQuery.setParameter("specialityId",employeeRegistrationDto.getSpecialityId());
			}if(employeeRegistrationDto.getEmpCode()!=null && !employeeRegistrationDto.getEmpCode().isEmpty())
			{
				createQuery.setParameter("empCode",employeeRegistrationDto.getEmpCode());
			}if(employeeRegistrationDto.getIdentificationTypeId()>0)
			{
				createQuery.setParameter("identificationTypeId",employeeRegistrationDto.getIdentificationTypeId());
			}if(employeeRegistrationDto.getIdentificationNumber()!=null && !employeeRegistrationDto.getIdentificationNumber().isEmpty())
			{
				createQuery.setParameter("identificationNumber",employeeRegistrationDto.getIdentificationNumber());
			}

			employeeRegistrationDtosList = createQuery.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, employeeRegistrationDtosList, null);
		} catch (Exception e) {
		logger.error("universalSearchForEmployee failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkTabAccessAvail(UserUnitMapperDto userUnitMapperDto) throws ApplicationException {
		
	try{
		String query = "select uum.tab_access_ids as \"tabAccessIds\" " + 
				" from  public.t_user_unit_mapper uum " + 
				" where uum.user_id=:userId " + 
				" and uum.organisation_id =:organizationId " + 
				" and uum.unit_id=:unitId " + 
				" and uum.status='A'";
		
				UserUnitMapperDto list=(UserUnitMapperDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("userId", userUnitMapperDto.getUserId())
					.setParameter("organizationId", userUnitMapperDto.getOrganisationId())
			        .setParameter("unitId", userUnitMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserUnitMapperDto.class)).uniqueResult();
				
			return new Response(SUCCESS, SUCCESS_CODE, null, null, list);
			}catch (Exception e) {
			logger.error("checkTabAccessAvail failed", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public String getUserNamwByUserId(Integer userId) throws ApplicationException {
		try{
			String name = (String)sessionFactory.getCurrentSession()
					.createSQLQuery("select concat(coalesce(emp.first_name, '') , ' ', coalesce(emp.middle_name, '') , ' ', coalesce(emp.last_name, ''))  as \"employeeId\"  "
								+ "	from m_employee_master emp "
								+ " where emp.user_id=:userId "
								+ " union all"
								+ " select concat(coalesce(mdm.first_name, '') , ' ', coalesce(mdm.middle_name, '') , ' ', coalesce(mdm.last_name, ''))  as \"employeeId\" "
								+ " from doctor.m_doctor_master mdm "
								+ " where mdm.user_id=:userId ")
					.setParameter("userId",userId)
					.uniqueResult();
				return name;
				}catch (Exception e) {
				logger.error("getUserNamwByUserId failed", e);
				}
				return null;
	}
	
	
	
	
	@Override
	public Response saveStaffSpecialityMapper(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{
			for(DoctorSpecialityMapperDto dto : employeeRegistrationDto.getStaffSpecialityMapperDtosList())
			{
				StaffSpecialityMapper staffSpecialityMapper = new StaffSpecialityMapper();
				staffSpecialityMapper.setSpecialityId(dto.getSpecialityId());
				staffSpecialityMapper.setUserId(employeeRegistrationDto.getUserId());
				staffSpecialityMapper.setUnitId(employeeRegistrationDto.getUnitId());
				staffSpecialityMapper.setCreatedBy(dto.getCreatedBy());
				staffSpecialityMapper.setUpdatedBy(dto.getUpdatedBy());
				staffSpecialityMapper.setStatus('A');
				staffSpecialityMapper.setCreatedDate(TimeZoneComponent.getDateByzone(employeeRegistrationDto.getUnitId()));
				staffSpecialityMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(employeeRegistrationDto.getUnitId()));
				sessionFactory.getCurrentSession().save(staffSpecialityMapper);
				
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			
			} catch (HibernateException he) {
			logger.error("saveStaffSpecialityMapper failed", he);
			} catch (Exception e) {
			logger.error("saveStaffSpecialityMapper failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response saveStaffSubSpecialityMapper(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{
			for(DoctorSpecialityMapperDto dto : employeeRegistrationDto.getStaffSubSpecialityMapperDtosList())
			{
				StaffSubSplecialityMapper staffSubSplecialityMapper = new StaffSubSplecialityMapper();
				staffSubSplecialityMapper.setSubSpecialityId(dto.getSubSpecialityId());
				staffSubSplecialityMapper.setUserId(employeeRegistrationDto.getUserId());
				staffSubSplecialityMapper.setUnitId(employeeRegistrationDto.getUnitId());
				staffSubSplecialityMapper.setCreatedBy(dto.getCreatedBy());
				staffSubSplecialityMapper.setUpdatedBy(dto.getUpdatedBy());
				staffSubSplecialityMapper.setStatus('A');
				staffSubSplecialityMapper.setCreatedDate(TimeZoneComponent.getDateByzone(employeeRegistrationDto.getUnitId()));
				staffSubSplecialityMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(employeeRegistrationDto.getUnitId()));
				sessionFactory.getCurrentSession().save(staffSubSplecialityMapper);
				
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			
			} catch (HibernateException he) {
			logger.error("saveStaffSubSpecialityMapper failed", he);
			} catch (Exception e) {
			logger.error("saveStaffSubSpecialityMapper failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response inactiveSpecialityStatusOfUser(EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			Integer count=sessionFactory.getCurrentSession().createQuery("UPDATE StaffSpecialityMapper "
					+ " SET status='I'"
					+ " WHERE userId=:userId ")
					.setParameter("userId",employeeRegistrationDto.getUserId())
					.executeUpdate();
			if(count>0) 
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			else
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		} catch (HibernateException he) {
		logger.error("inactiveSpecialityStatusOfUser failed", he);
		} catch (Exception e) {
		logger.error("inactiveSpecialityStatusOfUser failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveSubSpecialityStatusOfUser(EmployeeRegistrationDto employeeRegistrationDto)
			throws ApplicationException {
		try {
			Integer count=sessionFactory.getCurrentSession().createQuery("UPDATE StaffSubSplecialityMapper "
					+ " SET status='I'"
					+ " WHERE userId=:userId ")
					.setParameter("userId",employeeRegistrationDto.getUserId())
					.executeUpdate();
			if(count>0) 
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			else
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		} catch (HibernateException he) {
		logger.error("inactiveSubSpecialityStatusOfUser failed", he);
		} catch (Exception e) {
		logger.error("inactiveSubSpecialityStatusOfUser failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveUnitMapperByUserIdAndUnits(List<Integer> unitIds, Integer userId)
			throws ApplicationException {

		sessionFactory.getCurrentSession().createQuery("UPDATE UserUnitMapper "
				+ " SET status='I'"
				+ " WHERE unitId not in (:unitIds)  and userId=:userId ")
				.setParameterList("unitIds", unitIds)
				.setParameter("userId", userId)
				.executeUpdate();
		
		List<UserUnitMapperDto> userUnitMapperDtoList = sessionFactory.getCurrentSession()
				.createSQLQuery("select "
						+ " mapp.unit_id as \"unitId\" "
						+ " from t_user_unit_mapper mapp "
						+ "   where  mapp.user_id=:userId and mapp.status='A' ")
				.setParameter("userId",userId)
				.setResultTransformer(Transformers.aliasToBean(UserUnitMapperDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, userUnitMapperDtoList, null);
		
	
	}

	@Override
	public Response searchEmployeeByNameAndRole(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException {
		try{

			List<EmployeeRegistrationDto> list=sessionFactory.getCurrentSession().getNamedQuery("SEARCH_EMPLOYEE_BY_NAME_AND_ROLE")
					.setParameter("name", "%"+employeeRegistrationDto.getFirstName().toLowerCase()+"%")
					.setParameter("orgId", employeeRegistrationDto.getOrganizationId())
					.setParameter("unitId", employeeRegistrationDto.getUnitId())
					.setParameter("roleId", employeeRegistrationDto.getRoleId())
					.setResultTransformer(Transformers.aliasToBean(EmployeeRegistrationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch (Exception e) {
		logger.error("searchEmployeeByNameAndRole failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Integer getUserCount(EmployeeRegistrationDto employeeRegistrationDto) {
		 String sqlQuery=" select cast(count(*) as int) from m_user_master where unit_id=:unitId and organization_id=:orgId and status='A' ";
	        Integer data = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
	                .setParameter("orgId", employeeRegistrationDto.getOrganizationId())
	                .setParameter("unitId", employeeRegistrationDto.getUnitId()).uniqueResult();
	        return data;
	}

	@Override
	public Integer getConfiguredUserLimit(EmployeeRegistrationDto employeeRegistrationDto) {
		String sqlQuery=" select allocated_users from t_unit_application_billing_configuration where unit_id=:unitId and organization_id=:orgId and status='A' ";
        Integer data = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
                .setParameter("orgId", employeeRegistrationDto.getOrganizationId())
                .setParameter("unitId", employeeRegistrationDto.getUnitId()).uniqueResult();
        return data;
	}

}
