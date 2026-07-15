package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.InsuranceMasterDto;
import com.param.billing.global.transaction.model.InsuranceMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class InsuranceMasterDaoImpl extends GenericDao<InsuranceMaster, Integer>implements IInsuranceMasterDao, ICommonConstants{

	public InsuranceMasterDaoImpl() {
		super(InsuranceMaster.class);
		
	}
	
	@Autowired
	SessionFactory sessionFactory;
	@Override
	public Response saveInsuranceMaster(InsuranceMasterDto insuranceMasterDto) throws ApplicationException {
		try {
			InsuranceMaster insuranceMaster=new InsuranceMaster();
			insuranceMaster.setInsuranceCode(insuranceMasterDto.getInsuranceCode());
			insuranceMaster.setInsuranceDesc(insuranceMasterDto.getInsuranceDesc());
			insuranceMaster.setOrgId(insuranceMasterDto.getOrgId());
			insuranceMaster.setUnitId(insuranceMasterDto.getUnitId());
			insuranceMaster.setStartDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setEndDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getEndDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setAddress(insuranceMasterDto.getAddress());
			insuranceMaster.setAreaId(insuranceMasterDto.getAreaId());
			insuranceMaster.setCityId(insuranceMasterDto.getCityId());
			insuranceMaster.setCountryId(insuranceMasterDto.getCountryId());
			insuranceMaster.setDistrictId(insuranceMasterDto.getDistrictId());
			insuranceMaster.setContactNo(insuranceMasterDto.getContactNo());
			insuranceMaster.setEmail(insuranceMasterDto.getEmail());
			insuranceMaster.setContactPerson(insuranceMasterDto.getContactPerson());
			insuranceMaster.setFaxNo(insuranceMasterDto.getFaxNo());
			insuranceMaster.setStateId(insuranceMasterDto.getStateId());
			insuranceMaster.setCreatedBy(insuranceMasterDto.getCreatedBy());
			insuranceMaster.setCreatedDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setUpdatedBy(insuranceMasterDto.getUpdatedBy());
			insuranceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setZipCode(insuranceMasterDto.getZipCode());
			insuranceMaster.setStatus(insuranceMasterDto.getStatus());
			insuranceMaster.setBlockReportPrint(insuranceMasterDto.getBlockReportPrint());
			insuranceMaster.setGeneralLedgerId(insuranceMasterDto.getGeneralLedgerId());
			insuranceMaster = save(insuranceMaster);
	
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, insuranceMaster);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		

	@Override
	public Response updateInsuranceMaster(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			InsuranceMaster insuranceMaster=findById(insuranceMasterDto.getInsuranceId());
			insuranceMaster.setInsuranceCode(insuranceMasterDto.getInsuranceCode());
			insuranceMaster.setInsuranceDesc(insuranceMasterDto.getInsuranceDesc());
			insuranceMaster.setOrgId(insuranceMasterDto.getOrgId());
			insuranceMaster.setUnitId(insuranceMasterDto.getUnitId());
			insuranceMaster.setStartDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setEndDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getEndDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setAddress(insuranceMasterDto.getAddress());
			insuranceMaster.setAreaId(insuranceMasterDto.getAreaId());
			insuranceMaster.setCityId(insuranceMasterDto.getCityId());
			insuranceMaster.setCountryId(insuranceMasterDto.getCountryId());
			insuranceMaster.setDistrictId(insuranceMasterDto.getDistrictId());
			insuranceMaster.setContactNo(insuranceMasterDto.getContactNo());
			insuranceMaster.setEmail(insuranceMasterDto.getEmail());
			insuranceMaster.setFaxNo(insuranceMasterDto.getFaxNo());
			insuranceMaster.setStateId(insuranceMasterDto.getStateId());
			insuranceMaster.setZipCode(insuranceMasterDto.getZipCode());
			insuranceMaster.setUpdatedBy(insuranceMasterDto.getUpdatedBy());
			insuranceMaster.setContactPerson(insuranceMasterDto.getContactPerson());
			insuranceMaster.setBlockReportPrint(insuranceMasterDto.getBlockReportPrint());
			insuranceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setGeneralLedgerId(insuranceMasterDto.getGeneralLedgerId());
			update(insuranceMaster);
	
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, insuranceMasterDto.getInsuranceId());
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		
	@Override
	public Response updateInsuranceMasterStatus(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			InsuranceMaster insuranceMaster=findById(insuranceMasterDto.getInsuranceId());
			
			insuranceMaster.setUpdatedBy(insuranceMasterDto.getUpdatedBy());
			insuranceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(insuranceMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			insuranceMaster.setStatus(insuranceMasterDto.getStatus());
			update(insuranceMaster);
	
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		
		

	@Override
	public Response getInsuranceMasterById(Integer insuranceId, Integer orgId, Integer unitId) throws ApplicationException{
		try {

			List<InsuranceMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSURANCE_MASTER_BY_ID")
					.setParameter("insuranceId", insuranceId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(InsuranceMasterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list,null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getInsuranceMasterList(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {

			List<InsuranceMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSURANCE_MASTER_LIST")
					.setParameter("orgId", insuranceMasterDto.getOrgId())
					.setParameter("unitId", insuranceMasterDto.getUnitId())
					.setFirstResult(insuranceMasterDto.getOffset() != null ? insuranceMasterDto.getOffset() : 0)
					.setMaxResults(insuranceMasterDto.getNoOfRecordsPerPage() != null ? insuranceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(InsuranceMasterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list,null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getInsuranceMasterCount(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(InsuranceMaster.class);
			crit.add(Restrictions.eq("orgId", insuranceMasterDto.getOrgId()));
			crit.add(Restrictions.eq("unitId", insuranceMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getInsuranceMasterByName(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {

			List<InsuranceMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSURANCE_MASTER_BY_NAME")
					.setParameter("insuranceDesc", insuranceMasterDto.getInsuranceDesc().toLowerCase())
					.setParameter("unitId", insuranceMasterDto.getUnitId())
					.setParameter("orgId", insuranceMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(InsuranceMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInsuranceMasterByNameNotId(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {

			List<InsuranceMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSURANCE_MASTER_BY_NAME_NOT_NAME")
					.setParameter("insuranceId", insuranceMasterDto.getInsuranceId())
					.setParameter("orgId", insuranceMasterDto.getOrgId())
					.setParameter("unitId", insuranceMasterDto.getUnitId())
					.setParameter("insuranceDesc", insuranceMasterDto.getInsuranceDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(InsuranceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveInsuranceMasterList(InsuranceMasterDto insuranceMasterDto) {
		try {

			List<InsuranceMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_INSURANCE_MASTER_LIST")
					.setParameter("unitId", insuranceMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(InsuranceMasterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getMappedGradeToInsuerance(InsuranceMasterDto insuranceMasterDto) throws ApplicationException {
		try {

			List<InsuranceMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MAPPED_GRADE_TO_INSUERANCE")
					.setParameter("insuranceId", insuranceMasterDto.getInsuranceId())
					.setResultTransformer(Transformers.aliasToBean(InsuranceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateInsuranceImage(InsuranceMasterDto dto) throws ApplicationException {
		try{
			Integer result=sessionFactory.getCurrentSession().createQuery("UPDATE InsuranceMaster "
					+ " SET reportPath=:reportPath"
					+ " WHERE insuranceId=:insuranceId "
					+ " AND orgId=:orgId"
					+ " AND unitId=:unitId")
					.setParameter("insuranceId", dto.getInsuranceId())
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("reportPath", dto.getReportPath()).executeUpdate();
			if(result>0){
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
