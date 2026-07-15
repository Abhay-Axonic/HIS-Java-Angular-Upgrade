package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CustomEMRConfigurationDetailsDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.CustomEMRConfigurationDetails;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class CustomEmrConfigDetailsDaoImpl extends GenericDao<CustomEMRConfigurationDetails, Integer> implements ICustomEmrConfigDetailsDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(CustomEmrConfigDetailsDaoImpl.class);


	public CustomEmrConfigDetailsDaoImpl() {
		super(CustomEMRConfigurationDetails.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getCustomEMRCategorySubcategoryList(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto) {
		try {
			String query="select  " + 
					"	tt.custom_emr_config_id as \"customEmrConfigId\",  " + 
					"	cus.cus_emr_category_id as \"cusEmrCategoryId\",   " + 
					"	subCat.cus_emr_sub_category_desc as \"cusEmrSubCategoryDesc\",   " + 
					"	cus.cus_emr_category_desc as \"cusEmrCategoryDesc\"  , " + 
					"	subCat.cus_emr_sub_category_id as \"cusEmrSubCategoryId\"   " + 

					" from  " + 
					"	opd.t_custom_emr_configuration tt  " + 
					" inner join opd.m_custom_emr_category_master cus on   " + 
					"	cus .cus_emr_category_id = tt.cus_emr_category_id   " + 
					"	 inner join opd.m_custom_emr_sub_category_master subCat on   " + 
					"	cus .cus_emr_category_id = subCat.cus_emr_category_id" + 
					" where tt.organization_id =:organizationId " +
					" and tt.unit_id =:unitId " +
					" and tt.doctor_id =:doctorId ";
			List<CustomEMRConfigurationDetailsDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId",customEMRConfigurationDetailsDto.getOrganizationId())
					.setParameter("unitId",customEMRConfigurationDetailsDto.getUnitId())
					.setParameter("doctorId",customEMRConfigurationDetailsDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(CustomEMRConfigurationDetailsDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getCustomEMRCategorySubcategoryList failed", he);
		} catch (Exception e) {
		logger.error("getCustomEMRCategorySubcategoryList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCustomEMRCategorySubcategory(
			CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto) {
		 try {
				if(customEMRConfigurationDetailsDto.getCustomEmrConfigDetailsId() == null) {
					CustomEMRConfigurationDetails customEMRConfigurationDetails=new CustomEMRConfigurationDetails();
					customEMRConfigurationDetails.setPatientId(customEMRConfigurationDetailsDto.getPatientId());
					customEMRConfigurationDetails.setAdmissionId(customEMRConfigurationDetailsDto.getAdmissionId()!= null ? customEMRConfigurationDetailsDto.getAdmissionId() :null);
					customEMRConfigurationDetails.setEncounterId(customEMRConfigurationDetailsDto.getEncounterId()!= null ? customEMRConfigurationDetailsDto.getEncounterId() :null);
					customEMRConfigurationDetails.setCusEmrCategoryId(customEMRConfigurationDetailsDto.getCusEmrCategoryId());
					customEMRConfigurationDetails.setCusEmrSubCategoryId(customEMRConfigurationDetailsDto.getCusEmrSubCategoryId());
					customEMRConfigurationDetails.setDoctorId(customEMRConfigurationDetailsDto.getDoctorId());
					customEMRConfigurationDetails.setOrganizationId(customEMRConfigurationDetailsDto.getOrganizationId());
					customEMRConfigurationDetails.setUnitId(customEMRConfigurationDetailsDto.getUnitId());
					customEMRConfigurationDetails.setStatus(customEMRConfigurationDetailsDto.getStatus());
					customEMRConfigurationDetails.setRemark(customEMRConfigurationDetailsDto.getRemark());
					customEMRConfigurationDetails.setCreatedBy(customEMRConfigurationDetailsDto.getCreatedBy());
					customEMRConfigurationDetails.setCreatedDate(GlobalCommonDateUtils.getDate(customEMRConfigurationDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					customEMRConfigurationDetails.setUpdatedBy(customEMRConfigurationDetailsDto.getUpdatedBy());
					customEMRConfigurationDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(customEMRConfigurationDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					 
					save(customEMRConfigurationDetails);
				}else {
					Session session=sessionFactory.getCurrentSession();
					String query = "UPDATE opd.t_custom_emr_configuration_details "
							+ "SET remark = :remark "
							+ "WHERE custom_emr_config_details_id = :customEmrConfigDetailsId "
							+ "AND organization_id = :organizationId "
							+ "AND unit_id = :unitId "
							+ "AND status = 'A'";

					Integer result = session.createSQLQuery(query)
							.setParameter("remark", customEMRConfigurationDetailsDto.getRemark())
							.setParameter("customEmrConfigDetailsId", customEMRConfigurationDetailsDto.getCustomEmrConfigDetailsId())
							.setParameter("organizationId", customEMRConfigurationDetailsDto.getOrganizationId())
							.setParameter("unitId", customEMRConfigurationDetailsDto.getUnitId())
							.executeUpdate();


				}
				
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
				} catch (HibernateException he) {
				logger.error("updateCustomEMRCategorySubcategory failed", he);
				} catch (Exception e) {
				logger.error("updateCustomEMRCategorySubcategory failed", e);
				}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getAllListoCustomEMRByPatientId(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto) {
		try {
			String query = "select" + " distinct (dtl.custom_emr_config_details_id) as \"customEmrConfigDetailsId\","
					+ " dtl.cus_emr_category_id as \"cusEmrCategoryId\"," 
					+ " dtl.encounter_id as \"encounterId\","
					+ " dtl.patient_id as \"patientId\"," 
					+ " dtl.admission_id as \"admissionId\","
					+ " dtl.doctor_id as \"doctorId\"," 
					+ " dtl.remark as \"remark\","
					+ " mcescm.cus_emr_sub_category_desc as \"cusEmrSubCategoryDesc\","
					+ " cus.cus_emr_category_desc as \"cusEmrCategoryDesc\" " 
					+ " from"
					+ " opd.t_custom_emr_configuration_details dtl"
					+ " inner join opd.m_custom_emr_category_master cus on"
					+ " cus.cus_emr_category_id = dtl.cus_emr_category_id"
					+ " inner join opd.m_custom_emr_sub_category_master mcescm on"
					+ " dtl.cus_emr_sub_category_id = mcescm.cus_emr_sub_category_id" 
					+ " where dtl.organization_id=:organizationId "
					+ " and dtl.unit_id=:unitId "
					+ " and dtl.patient_id =:patientId ";
			List<CustomEMRConfigurationDetailsDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId",customEMRConfigurationDetailsDto.getOrganizationId())
					.setParameter("unitId",customEMRConfigurationDetailsDto.getUnitId())
					.setParameter("patientId",customEMRConfigurationDetailsDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(CustomEMRConfigurationDetailsDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getAllListoCustomEMRByPatientId failed", he);
		} catch (Exception e) {
		logger.error("getAllListoCustomEMRByPatientId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkAvailableCustomData(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto) {
		try {
			String query = "select" + " distinct (dtl.custom_emr_config_details_id) as \"customEmrConfigDetailsId\","
					+ " dtl.cus_emr_category_id as \"cusEmrCategoryId\"," 
					+ " dtl.encounter_id as \"encounterId\","
					+ " dtl.patient_id as \"patientId\"," 
					+ " dtl.admission_id as \"admissionId\","
					+ " dtl.doctor_id as \"doctorId\"," 
					+ " dtl.remark as \"remark\","
					+ " dtl.cus_emr_sub_category_id as \"cusEmrSubCategoryId\","
					+ " mcescm.cus_emr_sub_category_desc as \"cusEmrSubCategoryDesc\","
					+ " cus.cus_emr_category_desc as \"cusEmrCategoryDesc\" " 
					+ " from"
					+ " opd.t_custom_emr_configuration_details dtl"
					+ " inner join opd.m_custom_emr_category_master cus on"
					+ " cus.cus_emr_category_id = dtl.cus_emr_category_id"
					+ " inner join opd.m_custom_emr_sub_category_master mcescm on"
					+ " dtl.cus_emr_sub_category_id = mcescm.cus_emr_sub_category_id" 
					+ " where dtl.organization_id=:organizationId "
					+ " and dtl.unit_id=:unitId "
					+ " and dtl.patient_id =:patientId "
					+" and dtl.doctor_id =:doctorId "
					+" and dtl.cus_emr_category_id =:cusEmrCategoryId "
					+" and dtl.cus_emr_sub_category_id =:cusEmrSubCategoryId "
					;
			List<CustomEMRConfigurationDetailsDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId",customEMRConfigurationDetailsDto.getOrganizationId())
					.setParameter("unitId",customEMRConfigurationDetailsDto.getUnitId())
					.setParameter("doctorId",customEMRConfigurationDetailsDto.getDoctorId())
					.setParameter("patientId",customEMRConfigurationDetailsDto.getPatientId())
					.setParameter("cusEmrCategoryId",customEMRConfigurationDetailsDto.getCusEmrCategoryId())
					.setParameter("cusEmrSubCategoryId",customEMRConfigurationDetailsDto.getCusEmrSubCategoryId())
					.setResultTransformer(Transformers.aliasToBean(CustomEMRConfigurationDetailsDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("checkAvailableCustomData failed", he);
		} catch (Exception e) {
		logger.error("checkAvailableCustomData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

}
