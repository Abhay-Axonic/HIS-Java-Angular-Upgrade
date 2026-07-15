package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CorporateEmpDependentDetailsDto;
import com.param.global.dto.CorporateEmployeeMasterDto;
import com.param.global.model.CorporateEmpDependentDetails;
import com.param.global.model.CorporateEmployeeMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CorporateEmployeeMasterDaoImpl extends GenericDao<CorporateEmployeeMaster, Integer>
		implements ICorporateEmployeeMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(CorporateEmployeeMasterDaoImpl.class);

	public CorporateEmployeeMasterDaoImpl() {
		super(CorporateEmployeeMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveCorporateEmployeeMaster(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			CorporateEmployeeMaster master = new CorporateEmployeeMaster();
			master.setEmployeeNumber(dto.getEmployeeNumber().trim());
			master.setAddress(dto.getAddress());
			master.setFirstName(dto.getFirstName());
			master.setMiddleName(dto.getMiddleName());
			master.setLastName(dto.getLastName());
			master.setEmailId(dto.getEmailId());
			master.setMobileNo(dto.getMobileNo());
			master.setCountryId(dto.getCountryId());
			master.setStateId(dto.getStateId());
			master.setDistrictId(dto.getDistrictId());
			master.setCityId(dto.getCityId());
			master.setAreaId(dto.getAreaId());
			master.setZipCode(dto.getZipCode());
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUnitId(dto.getUnitId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			master.setStatus('A');
			master.setCorporateId(dto.getCorporateId());
			Integer id = (Integer) sessionFactory.getCurrentSession().save(master);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);
		} catch (HibernateException he) {
			logger.error("saveCorporateEmployeeMaster failed", he);
		} catch (Exception e) {
			logger.error("saveCorporateEmployeeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveCorporateEmpDependentDetails(CorporateEmpDependentDetailsDto dto) throws ApplicationException {
		try {
			CorporateEmpDependentDetails master = new CorporateEmpDependentDetails();
			master.setDobKin(GlobalCommonDateUtils.getDate(dto.getDobKin(), "dd-MM-yyyy HH:mm:ss"));
			master.setFirstName(dto.getFirstName());
			master.setMiddleName(dto.getMiddleName());
			master.setLastName(dto.getLastName());
			master.setPrefixId(dto.getPrefixId());
			master.setOccupationId(dto.getOccupationId());
			master.setGenderId(dto.getGenderId());
			master.setIdentificationNumber(dto.getIdentificationNumber());
			master.setIdentificationTypeId(dto.getIdentificationTypeId());
			master.setCorporateEmployeeId(dto.getCorporateEmployeeId());
			master.setIsNok(dto.getIsNok());
			master.setRelationId(dto.getRelationId());
			master.setMobileNo(dto.getMobileNo());
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUnitId(dto.getUnitId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			master.setStatus('A');
			sessionFactory.getCurrentSession().save(master);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveCorporateEmpDependentDetails failed", he);
		} catch (Exception e) {
			logger.error("saveCorporateEmpDependentDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCorporateEmployeeMasterDetails(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {

		} catch (HibernateException he) {
			logger.error("getCorporateEmployeeMasterDetails failed", he);
		} catch (Exception e) {
			logger.error("getCorporateEmployeeMasterDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCorporateEmployeeDependantsDetails(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {	
			List<CorporateEmpDependentDetailsDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_CORPORATE_EMPLOYEE_DEPENDANTS_DETAILS")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("corporateEmployeeId", dto.getCorporateEmployeeId())
					.setResultTransformer(Transformers.aliasToBean(CorporateEmpDependentDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error("getCorporateEmployeeDependantsDetails failed", he);
		} catch (Exception e) {
			logger.error("getCorporateEmployeeDependantsDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCorporateEmployeeMaster(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			CorporateEmployeeMaster master=findById(dto.getCorporateEmployeeId());
			master.setEmployeeNumber(dto.getEmployeeNumber());
			master.setAddress(dto.getAddress());
			master.setFirstName(dto.getFirstName());
			master.setMiddleName(dto.getMiddleName());
			master.setLastName(dto.getLastName());
			master.setEmailId(dto.getEmailId());
			master.setMobileNo(dto.getMobileNo());
			master.setCountryId(dto.getCountryId());
			master.setStateId(dto.getStateId());
			master.setDistrictId(dto.getDistrictId());
			master.setCityId(dto.getCityId());
			master.setAreaId(dto.getAreaId());
			master.setZipCode(dto.getZipCode());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			master.setCorporateId(dto.getCorporateId());
			sessionFactory.getCurrentSession().update(master);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateCorporateEmployeeMaster failed", he);
		} catch (Exception e) {
			logger.error("updateCorporateEmployeeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCorporateEmployeeMasterDetailsById(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			 List<CorporateEmployeeMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_CORPORATE_EMPLOYEE_MASTER_DETAILS_BY_ID")
					 .setParameter("orgId", dto.getOrganizationId())
					 .setParameter("unitId",dto.getUnitId())
					 .setParameter("corporateEmployeeId", dto.getCorporateEmployeeId())
					 .setResultTransformer(Transformers.aliasToBean(CorporateEmployeeMasterDto.class)).list();
			 return new Response(SUCCESS, null, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error("getCorporateEmployeeMasterDetailsById failed", he);
		} catch (Exception e) {
			logger.error("getCorporateEmployeeMasterDetailsById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response searchCorporateEmployee(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			
			StringBuilder strBuilder =new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_CORPORATE_EMPLOYEE_DETAILS").getQueryString());
			
			strBuilder.append(dto.getCorporateId()!=null ? " AND mstr.corporateId="+dto.getCorporateId() :" ");
			strBuilder.append(dto.getEmployeeNumber()!=null ? " AND mstr.employeeNumber like '%"+dto.getEmployeeNumber()+"%'" :" ");
			strBuilder.append(dto.getFirstName()!=null ? " AND (lower(mstr.firstName) like '%"+dto.getFirstName().toLowerCase()+"%' OR lower(mstr.middleName) like '%"+dto.getFirstName().toLowerCase()+"%' OR lower(mstr.lastName) like '%"+dto.getFirstName().toLowerCase()+"%')" :" ");
			strBuilder.append(" order by mstr.corporateEmployeeId desc ");
			List<CorporateEmployeeMasterDto> dtoList=sessionFactory.getCurrentSession().createQuery(strBuilder.toString())
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CorporateEmployeeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error("searchCorporateEmployee failed", he);
		} catch (Exception e) {
			logger.error("searchCorporateEmployee failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCorporateEmployeeMasterStatus(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			CorporateEmployeeMaster master=findById(dto.getCorporateEmployeeId());
			master.setStatus(dto.getStatus()!=null ? dto.getStatus() : 'I');	
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(master);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateCorporateEmployeeMasterStatus failed", he);
		} catch (Exception e) {
			logger.error("updateCorporateEmployeeMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCorporateEmpDependents(CorporateEmployeeMasterDto dto) throws ApplicationException {
		try {
			
			Integer cnt=sessionFactory.getCurrentSession().createQuery("UPDATE CorporateEmpDependentDetails "
					+ "SET status='I' "
					+ "WHERE corporateEmployeeId=:corEmpId")
					.setParameter("corEmpId", dto.getCorporateEmployeeId()).executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateCorporateEmpDependents failed", he);
		} catch (Exception e) {
			logger.error("updateCorporateEmpDependents failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response verifyCorporateEmployee(CorporateEmployeeMasterDto dto) throws ApplicationException {
	try {
		      //gender and dob 
		/*
		  	1 - staff or employee 
			2- dependants 
			3- mannual
		 */
		if(dto.getTypeOfEmp()==1){
			List<CorporateEmployeeMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("VERIFY_CORPORATE_EMPLOYEE_DETAILS")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("empNo", dto.getEmployeeNumber().trim())
					.setParameter("companyId", dto.getCorporateId())
					.setResultTransformer(Transformers.aliasToBean(CorporateEmployeeMasterDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
					
		}else if(dto.getTypeOfEmp()==2){
			List<CorporateEmpDependentDetailsDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("VERIFY_CORPORATE_EMPLOYEE_DEPENDANTE_DETAILS")
					 .setParameter("orgId", dto.getOrganizationId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("empNo", dto.getEmployeeNumber().trim())
						.setParameter("companyId", dto.getCorporateId())
						.setResultTransformer(Transformers.aliasToBean(CorporateEmpDependentDetailsDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}else if(dto.getTypeOfEmp()==3){
//			update note z
		}
		
		
		
		} catch (HibernateException he) {
			logger.error("verifyCorporateEmployee failed", he);
		} catch (Exception e) {
			logger.error("verifyCorporateEmployee failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
