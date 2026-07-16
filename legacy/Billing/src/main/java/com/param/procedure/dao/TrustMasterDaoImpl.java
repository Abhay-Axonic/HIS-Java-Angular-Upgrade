package com.param.procedure.dao;

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

import com.param.billing.global.model.TrustMaster;
import com.param.billing.global.model.TrustVisitTypeMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dto.global.VisitType;
import com.param.global.model.CompanyMaster;
import com.param.service.dto.TrustMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TrustMasterDaoImpl extends GenericDao<TrustMaster, Integer>
		implements ITrustMasterDao,ICommonConstants, IError  {

	public TrustMasterDaoImpl() {
		super(TrustMaster.class);
	}

	@Autowired
	private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(TrustMaster.class);

	@Override
	public Response saveTrust(TrustMasterDto trustMasterDto) throws ApplicationException {
    
		try{
			        CompanyMaster companyMaster = new CompanyMaster();
					companyMaster.setCompanyDesc(trustMasterDto.getTrustName());
					companyMaster.setAddress(trustMasterDto.getTrustAddress());
					companyMaster.setCountryId(trustMasterDto.getCountryId());
					companyMaster.setStateId(trustMasterDto.getStateId());
					companyMaster.setDistrictId(trustMasterDto.getDistrictId());
					companyMaster.setCityId(trustMasterDto.getCityId());
					companyMaster.setAreaId(trustMasterDto.getAreaId());
					companyMaster.setZipcode((trustMasterDto.getZipCode())); 
					companyMaster.setContactPersonName(trustMasterDto.getContactPersonName());
					companyMaster.setMobileNo(trustMasterDto.getPhoneNo());
					companyMaster.setResidencePhoneNo(trustMasterDto.getPhoneNo());
					companyMaster.setCreatedBy(trustMasterDto.getCreatedBy());
					companyMaster.setCreatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
					companyMaster.setUpdatedBy(trustMasterDto.getUpdatedBy());
					companyMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
					companyMaster.setOrganizationId(trustMasterDto.getOrgId());
					companyMaster.setUnitId(trustMasterDto.getUnitId());
					companyMaster.setStatus(trustMasterDto.getTrustStatus());
					companyMaster.setEmailId(trustMasterDto.getContactPersonEmail());
					companyMaster.setCompanyTypeId(3);
					companyMaster.setIsTrust("A");
					companyMaster.setStartDate(GlobalCommonDateUtils.getDate(trustMasterDto.getValidFrom(), "dd-M-yyyy HH:mm:ss"));
					
					sessionFactory.getCurrentSession().save(companyMaster);
					
//		     TrustMaster trustMaster= new TrustMaster();
//		     trustMaster.setTrustCompanyId(companyMaster.getCompanyId());
//		     trustMaster.setTrustName(trustMasterDto.getTrustName());
//		     trustMaster.setTrustAddress(trustMasterDto.getTrustAddress());
//		     trustMaster.setTrustStatus(trustMasterDto.getTrustStatus());
//		     trustMaster.setCountryId(trustMasterDto.getCountryId());
//		     trustMaster.setStateId(trustMasterDto.getStateId());
//		     trustMaster.setDistrictId(trustMasterDto.getDistrictId());
//		     trustMaster.setCityId(trustMasterDto.getCityId());
//		     trustMaster.setAreaId(trustMasterDto.getAreaId());
//		     trustMaster.setZipCode(trustMasterDto.getZipCode());
//		     trustMaster.setAreaId(trustMasterDto.getAreaId());
//		     trustMaster.setContactPersonName(trustMasterDto.getContactPersonName());
//		     trustMaster.setContactPersonEmail(trustMasterDto.getContactPersonEmail());
//		     trustMaster.setPhoneNo(trustMasterDto.getPhoneNo());
//		     trustMaster.setCellNo(trustMasterDto.getCellNo());
//		     trustMaster.setValidFrom(trustMasterDto.getValidFrom());
//		     trustMaster.setPatientCategoryId(trustMasterDto.getPatientCategoryId());
//		     trustMaster.setPatientTypeId(trustMasterDto.getPatientTypeId());
//		     //trustMaster.setVisitTypeId(trustMasterDto.getVisitTypeId());
//		     trustMaster.setDisease(trustMasterDto.getDisease());
//		     trustMaster.setMaxLimitPerPerson(trustMasterDto.getMaxLimitPerPerson());
//		     trustMaster.setUnitId(trustMasterDto.getUnitId());
//		     trustMaster.setOrgId(trustMasterDto.getOrgId());
//		     trustMaster.setCreatedBy(trustMasterDto.getCreatedBy());
//		     trustMaster.setCreatedDate(TimeZoneComponent.getDateByzone(unitId));
//		     trustMaster.setUpdatedBy(trustMasterDto.getUpdatedBy());
//		     trustMaster.setIsCompanyContract(trustMasterDto.getIsCompanyContract());
//		     trustMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//		     sessionFactory.getCurrentSession().save(trustMaster);
	     
	     
	   for(VisitType visitTypeObj:trustMasterDto.getVisitTypeId()){
			TrustVisitTypeMaster trustVisitTypeMaster= new TrustVisitTypeMaster();
			trustVisitTypeMaster.setVisitTypeId(visitTypeObj.getId());
			trustVisitTypeMaster.setTrustId(companyMaster.getCompanyId());
			trustVisitTypeMaster.setUnitId(trustMasterDto.getUnitId());
			trustVisitTypeMaster.setOrgId(trustMasterDto.getOrgId());
			trustVisitTypeMaster.setCreatedBy(trustMasterDto.getCreatedBy());
			trustVisitTypeMaster.setStatus('A');
			trustVisitTypeMaster.setCreatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
			trustVisitTypeMaster.setUpdatedBy(trustMasterDto.getCreatedBy());
			trustVisitTypeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
			sessionFactory.getCurrentSession().save(trustVisitTypeMaster);

	   }
			return new Response<>(SUCCESS, null, null, null, null);
}   catch(Exception e){
	throw e;
}
	}

	@Override
	public Response getTrustMasterById(Integer orgId, Integer unitId, Integer trustId) throws ApplicationException {
		try {
			TrustMasterDto trustMasterDto = (TrustMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_TRUST_MASTER_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("trustId", trustId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TrustMasterDto.class)).uniqueResult();

			List<VisitType> visitTypeList = 	sessionFactory.getCurrentSession()
				     .createSQLQuery(" select tvt.trust_id as \"trustId\",tvt.visit_type_id as \"id\" ,vtm.visit_type_code as \"visitTypeCode\""
								+ " FROM public.m_trust_visit_type tvt "
								+ " join public.m_visit_type_master vtm  on  tvt.visit_type_id = vtm.visit_type_id "
								+ " WHERE tvt.trust_id =:trustId "
								+ " AND tvt.org_id=:orgId "
								+ " AND tvt.unit_id=:unitId "
				                + " AND tvt.status='A'") 
				        .setParameter("trustId", trustId)
						.setParameter("orgId", orgId)
					    .setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(VisitType.class)).list();
			return new Response(SUCCESS, null, null, visitTypeList, trustMasterDto);

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, null, null, null);
		}
	}

	@Override
	public Response updateTrustMaster(TrustMasterDto trustMasterDto) throws ApplicationException {
		try{
			 CompanyMaster companyMaster = new CompanyMaster();
				    companyMaster.setCompanyId(trustMasterDto.getTrustId());
					companyMaster.setCompanyDesc(trustMasterDto.getTrustName());
					companyMaster.setAddress(trustMasterDto.getTrustAddress());
					companyMaster.setCountryId(trustMasterDto.getCountryId());
					companyMaster.setStateId(trustMasterDto.getStateId());
					companyMaster.setDistrictId(trustMasterDto.getDistrictId());
					companyMaster.setCityId(trustMasterDto.getCityId());
					companyMaster.setAreaId(trustMasterDto.getAreaId());
					companyMaster.setZipcode((trustMasterDto.getZipCode())); 
					companyMaster.setContactPersonName(trustMasterDto.getContactPersonName());
					companyMaster.setMobileNo(trustMasterDto.getPhoneNo());
					companyMaster.setResidencePhoneNo(trustMasterDto.getCellNo());
					companyMaster.setCreatedBy(trustMasterDto.getCreatedBy());
					companyMaster.setCreatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
					companyMaster.setUpdatedBy(trustMasterDto.getUpdatedBy());
					companyMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
					companyMaster.setOrganizationId(trustMasterDto.getOrgId());
					companyMaster.setUnitId(trustMasterDto.getUnitId());
					companyMaster.setStatus(trustMasterDto.getTrustStatus());
					companyMaster.setEmailId(trustMasterDto.getContactPersonEmail());
					companyMaster.setCompanyTypeId(3);
					companyMaster.setIsTrust("A");
					companyMaster.setStatus('A');
					companyMaster.setStartDate(GlobalCommonDateUtils.getDate(trustMasterDto.getValidFrom(), "dd-M-yyyy HH:mm:ss"));
					
					sessionFactory.getCurrentSession().update(companyMaster);
					
//		     TrustMaster trustMaster= new TrustMaster();
//		     trustMaster.setTrustCompanyId(trustMasterDto.getTrustCompanyId());
//		     trustMaster.setTrustId(trustMasterDto.getTrustId());
//		     trustMaster.setTrustName(trustMasterDto.getTrustName());
//		     trustMaster.setTrustAddress(trustMasterDto.getTrustAddress());
//		     trustMaster.setTrustStatus(trustMasterDto.getTrustStatus());
//		     trustMaster.setCountryId(trustMasterDto.getCountryId());
//		     trustMaster.setStateId(trustMasterDto.getStateId());
//		     trustMaster.setDistrictId(trustMasterDto.getDistrictId());
//		     trustMaster.setCityId(trustMasterDto.getCityId());
//		     trustMaster.setAreaId(trustMasterDto.getAreaId());
//		     trustMaster.setContactPersonName(trustMasterDto.getContactPersonName());
//		     trustMaster.setContactPersonEmail(trustMasterDto.getContactPersonEmail());
//		     trustMaster.setPhoneNo(trustMasterDto.getPhoneNo());
//		     trustMaster.setCellNo(trustMasterDto.getCellNo());
//		     trustMaster.setValidFrom(GlobalCommonDateUtils.getDate(trustMasterDto.getValidFrom(), "dd-M-yyyy HH:mm:ss"));
//		     trustMaster.setPatientCategoryId(trustMasterDto.getPatientCategoryId());
//		     trustMaster.setPatientTypeId(trustMasterDto.getPatientTypeId());
//		    // trustMaster.setVisitTypeId(trustMasterDto.getVisitTypeId());
//		     trustMaster.setDisease(trustMasterDto.getDisease());
//		     trustMaster.setMaxLimitPerPerson(trustMasterDto.getMaxLimitPerPerson());
//		     trustMaster.setUnitId(trustMasterDto.getUnitId());
//		     trustMaster.setOrgId(trustMasterDto.getOrgId());
//		     trustMaster.setCreatedBy(trustMasterDto.getCreatedBy());
//		     trustMaster.setCreatedDate(TimeZoneComponent.getDateByzone(unitId));
//		     trustMaster.setUpdatedBy(trustMasterDto.getUpdatedBy());
//		     trustMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
//		     trustMaster.setIsCompanyContract(trustMasterDto.getIsCompanyContract());
//		     sessionFactory.getCurrentSession().update(trustMaster);
		     
		     	Integer count=sessionFactory.getCurrentSession().createQuery(" update TrustVisitTypeMaster tvm set status='I' where tvm.trustId=:trustId "	)
		     			.setParameter("trustId", companyMaster.getCompanyId()).executeUpdate();
		     	
		     for(VisitType visitTypeObj:trustMasterDto.getVisitTypeId()){
			     TrustVisitTypeMaster trustVisitTypeMaster= new TrustVisitTypeMaster();
			    
				     trustVisitTypeMaster.setVisitTypeId(visitTypeObj.getId());
				     trustVisitTypeMaster.setTrustId(companyMaster.getCompanyId());
				     trustVisitTypeMaster.setUnitId(trustMasterDto.getUnitId());
				     trustVisitTypeMaster.setOrgId(trustMasterDto.getOrgId());
				     trustVisitTypeMaster.setCreatedBy(trustMasterDto.getCreatedBy());
				     trustVisitTypeMaster.setStatus('A');
				     trustVisitTypeMaster.setCreatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
				     trustVisitTypeMaster.setUpdatedBy(trustMasterDto.getCreatedBy());
				     trustVisitTypeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(trustMasterDto.getUnitId()));
				     sessionFactory.getCurrentSession().save(trustVisitTypeMaster);
			     }
			     
				return new Response<>(SUCCESS, null, null, null, null);
	}   catch(Exception e){
		throw e;
	}
		}

	@Override
	public Response activeInactiveTrustMaster(Integer orgId, Integer trustId, Character trustStatus)
			throws ApplicationException {
		try {
			if (trustId != 0) {
				sessionFactory.getCurrentSession().createQuery(" UPDATE CompanyMaster SET status =:trustStatus "
		        		+ " WHERE companyId =:trustId ")
						.setParameter("trustId", trustId)
						.setParameter("trustStatus", trustStatus)
						.executeUpdate();
						return new Response(SUCCESS, null, null, null, null);
					}
				return new Response(SUCCESS, null, null, null, null);

		} catch (Exception e) 	{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, null, null, null);
		}
	}

	@Override
	public Response listTrustMaster(TrustMasterDto trustMasterDto)
			throws ApplicationException {
		try {
			List<TrustMasterDto> listTrustMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_TRUST_MASTER_LIST")
					.setParameter("orgId", trustMasterDto.getOrgId())
					.setParameter("unitId", trustMasterDto.getUnitId())
					.setFirstResult(trustMasterDto.getOffset() != null ? trustMasterDto.getOffset()  : 0)
					.setMaxResults(trustMasterDto.getNoOfRecordsPerPage() != null ? trustMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(TrustMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, listTrustMasterDto, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTotalTrustMasterRecord(TrustMasterDto trustMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(TrustMaster.class); 
			c.add(Restrictions.eq("orgId", trustMasterDto.getOrgId()));
			c.add(Restrictions.eq("unitId", trustMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveTrustList(TrustMasterDto trustMasterDto) throws ApplicationException {
		try {
			List<TrustMasterDto> machineMtrDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_TRUST_ACTIVE_MASTER_LIST")
					.setParameter("orgId", trustMasterDto.getOrgId())
					.setParameter("unitId", trustMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TrustMasterDto.class)).list();
			return new Response(SUCCESS, null, null, machineMtrDto, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

   @Override
	public Response getVisitType(Integer trustId,Integer orgId, Integer unitId ) throws ApplicationException {
		try {
			List<TrustMasterDto> trustMtrDto = 	sessionFactory.getCurrentSession()
					     .createSQLQuery(" select tvt.trust_id as \"trustId\",tvt.visit_type_id as \"id\" ,vtm.visit_type_code as \"visitTypeCode\""
									+ " FROM public.m_trust_visit_type tvt "
									+ " join public.m_visit_type_master vtm  on  tvt.visit_type_id = vtm.visit_type_id "
									+ " WHERE tvt.trust_id =:trustId "
									+ " AND tvt.org_id=:orgId "
									+ " AND tvt.unit_id=:unitId "
									+ " AND tvt.status='A'") 
					        .setParameter("trustId", trustId)
							.setParameter("orgId", orgId)
						    .setParameter("unitId", unitId)
						    .setResultTransformer(Transformers.aliasToBean(TrustMasterDto.class)).list();
			return new Response(SUCCESS, null, null, trustMtrDto, null);

		} catch (HibernateException he) {

		} catch (Exception e) { 
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}
}
