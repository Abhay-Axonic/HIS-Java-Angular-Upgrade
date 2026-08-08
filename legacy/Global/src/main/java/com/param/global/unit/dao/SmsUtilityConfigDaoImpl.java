package com.param.global.unit.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import com.param.global.common.ISmsUtilityConstants;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.master.SmsUtilityConfiguration;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.SmsUtilityConfigurationDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SmsUtilityConfigDaoImpl extends GenericDao<SmsUtilityConfiguration, Integer>
		implements ISmsUtilityConfigDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(SmsUtilityConfigDaoImpl.class);


	public SmsUtilityConfigDaoImpl() {
		super(SmsUtilityConfiguration.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveSmsUtilityConfig(SmsUtilityConfigurationDto smsUtilityConfigurationDto)
			throws ApplicationException {
		try {
			// DQ-2026-08-08-003 guard: is_sms_send, is_whatsapp_send, critical_report,
			// user_defined_mail, whatsapp_template_master_id and type have NO backing column
			// (DQ-003 batch 1 removed the mappings; the columns exist in no schema).
			// Reject meaningful values loudly instead of silently dropping them. 'N'/null pass.
			if ((smsUtilityConfigurationDto.getIsSMSSend() != null && !"N".equals(smsUtilityConfigurationDto.getIsSMSSend()))
					|| (smsUtilityConfigurationDto.getIsWhatsappSend() != null && !"N".equals(smsUtilityConfigurationDto.getIsWhatsappSend()))
					|| (smsUtilityConfigurationDto.getCriticalReport() != null && !"N".equals(String.valueOf(smsUtilityConfigurationDto.getCriticalReport())))
					|| (smsUtilityConfigurationDto.getUserDefinedMail() != null && !smsUtilityConfigurationDto.getUserDefinedMail().trim().isEmpty())
					|| smsUtilityConfigurationDto.getWhatsAppTemplateMasterId() != null
					|| (smsUtilityConfigurationDto.getType() != null && !smsUtilityConfigurationDto.getType().trim().isEmpty())) {
				return new Response<>(ERROR, ERROR_CODE,
						"Cannot save: SMS/WhatsApp send flags, critical report, user-defined mail, WhatsApp template and type have no backing column. Deselect them to save the configuration.",
						null, null);
			}
			Integer id=null;
//			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_SMS_UTILITY_CONFIG_MASTER_LIST").getQueryString());
			if (smsUtilityConfigurationDto.getTriggerId() != null) {
				stringQuery.append(" AND suc.trigger_id=" + smsUtilityConfigurationDto.getTriggerId() + " ");
			}
			if (smsUtilityConfigurationDto.getCategoryId() != null) {
				stringQuery.append(" AND suc.category_id=" + smsUtilityConfigurationDto.getCategoryId() + " ");
			}
			if(smsUtilityConfigurationDto.getSmsUtilityId()!=null) {
				stringQuery.append(" AND suc.sms_utility_id <> " + smsUtilityConfigurationDto.getSmsUtilityId() + " ");
			}
			if(smsUtilityConfigurationDto.getIsEmail()!=null) {
				stringQuery.append(" AND suc.is_email = " + smsUtilityConfigurationDto.getIsEmail() + " ");
			}
			Query actualQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			SmsUtilityConfigurationDto smsUtilityConfigDto = (SmsUtilityConfigurationDto) actualQuery
					.setParameter("organizationId", smsUtilityConfigurationDto.getOrgId())
					.setParameter("unitId", smsUtilityConfigurationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigurationDto.class)).uniqueResult();
			if (smsUtilityConfigDto != null) {
				return new Response(ERROR, SUCCESS_CODE, "Already Exist", null, null);
			}

			SmsUtilityConfiguration smsUtilityConfiguration = new SmsUtilityConfiguration();

			if (smsUtilityConfigurationDto.getSmsUtilityId() != null) {
				smsUtilityConfiguration.setSmsUtilityId(smsUtilityConfigurationDto.getSmsUtilityId());
			}
			smsUtilityConfiguration.setTriggerId(smsUtilityConfigurationDto.getTriggerId());
			smsUtilityConfiguration.setTemplateDescription(smsUtilityConfigurationDto.getTemplateDescription());
			smsUtilityConfiguration.setCategoryId(smsUtilityConfigurationDto.getCategoryId());
			smsUtilityConfiguration.setOrgId(smsUtilityConfigurationDto.getOrgId());
			smsUtilityConfiguration.setUnitId(smsUtilityConfigurationDto.getUnitId());
			smsUtilityConfiguration.setStatus(smsUtilityConfigurationDto.getStatus());
			smsUtilityConfiguration.setUpdatedBy(smsUtilityConfigurationDto.getUpdatedBy());
			smsUtilityConfiguration.setUpdatedDate(TimeZoneComponent.getDateByzone(smsUtilityConfigurationDto.getUnitId()));
			smsUtilityConfiguration.setCreatedBy(smsUtilityConfigurationDto.getCreatedBy());
			smsUtilityConfiguration.setCreatedDate(TimeZoneComponent.getDateByzone(smsUtilityConfigurationDto.getUnitId()));
			smsUtilityConfiguration.setScheduleTypeId(smsUtilityConfigurationDto.getScheduleTypeId());
			smsUtilityConfiguration.setIsEmail(smsUtilityConfigurationDto.getIsEmail()!= null ? smsUtilityConfigurationDto.getIsEmail() : false);
			smsUtilityConfiguration.setMailSubject(smsUtilityConfigurationDto.getMailSubject()!=null ? smsUtilityConfigurationDto.getMailSubject() : null);
			if(smsUtilityConfiguration.getSmsUtilityId()!=null && smsUtilityConfiguration.getSmsUtilityId()>0)
				sessionFactory.getCurrentSession().update(smsUtilityConfiguration);
			else
				 id=(Integer)sessionFactory.getCurrentSession().save(smsUtilityConfiguration);
			// for inserting mobile no for schedule (collection) SMS
			     //checking the smsutilityId i.e. PK if we save then we get id but in update case we not get id so , use ternary optr to get value from smsUtilityConfiguration .
				id= id!=null ? id : smsUtilityConfiguration.getSmsUtilityId();
			if(smsUtilityConfigurationDto.getScheduleTypeId()==2 && smsUtilityConfigurationDto.getCategoryId()==9){
				if(smsUtilityConfiguration.getSmsUtilityId()!=null){
					sessionFactory.getCurrentSession().createSQLQuery("UPDATE public.t_sms_schedule_numbers "
							+ "set status='I' WHERE sms_utility_id="+smsUtilityConfiguration.getSmsUtilityId()).executeUpdate();
				}
				if(smsUtilityConfigurationDto.getMobileNoList()!=null && smsUtilityConfigurationDto.getMobileNoList().size()>0){
					
					ListIterator<String> list=smsUtilityConfigurationDto.getMobileNoList().listIterator();
					while(list.hasNext()){
								String mobileNO=list.next();					
							sessionFactory.getCurrentSession().createSQLQuery(""
							+ "INSERT INTO  public.t_sms_schedule_numbers (mobile_no, org_id, unit_id, status,sms_utility_id) "
							+" VALUES ('"+mobileNO+"',"+smsUtilityConfigurationDto.getOrgId()+","+smsUtilityConfigurationDto.getUnitId()+",'A',"+id+")").executeUpdate();
					}
				}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
		logger.error("saveSmsUtilityConfig failed", he);
		} catch (Exception e) {
		logger.error("saveSmsUtilityConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTriggerMaster() throws ApplicationException {

		try {

			List<SmsUtilityConfigurationDto> triggerlist = sessionFactory.getCurrentSession().createSQLQuery(
					"select trigger_id as\"triggerId\", trigger_name as\"triggerName\",trigger_type as \"triggerType\","
					+ " is_sms as \"isSms\" ," +  
					"	is_email as \"isEmail\" "
					+ " from public.m_sms_trigger_master order by trigger_name ASC ")
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigurationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, triggerlist, null);

		} catch (HibernateException he) {
		logger.error("getTriggerMaster failed", he);
		} catch (Exception e) {
		logger.error("getTriggerMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCategoryMaster() throws ApplicationException {
		try {

			List<SmsUtilityConfigurationDto> categorylist = sessionFactory.getCurrentSession().createSQLQuery(
					"select category_id as \"categoryId\",category_name as \"categoryName\",category_type as \"categoryType\"  from public.m_sms_category_master order by category_name ASC ")
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigurationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, categorylist, null);

		} catch (HibernateException he) {
		logger.error("getCategoryMaster failed", he);
		} catch (Exception e) {
		logger.error("getCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSmsUtilityConfigDetails(Integer organizationId, Integer unitId) {
		try {
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SMS_UTILITY_CONFIG_MASTER_LIST").getQueryString());
			
			query.append(" ORDER BY suc.category_id ");
			
			List<SmsUtilityConfigurationDto> smsutilityDetailslist = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigurationDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, smsutilityDetailslist, null);

		} catch (HibernateException he) {
		logger.error("getSmsUtilityConfigDetails failed", he);
		} catch (Exception e) {
		logger.error("getSmsUtilityConfigDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getSmsUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo) {
		try {

			List<SmsUtilityConfigInfoDto> smsUtilityConfigurationDto = null;
			if(triggerId!=null && triggerId== ISmsUtilityConstants.SUBSCRIPTION_REMINDER){

				String query ="SELECT suc.sms_utility_id as \"smsUtilityId\", "
						+ " 	suc.trigger_id as\"triggerId\", "
						+ "  	suc.template_description as \"templateDescription\", "
						+ " 	suc.category_id as \"categoryId\", "
						+ " 	cm.category_name as \"categoryName\", "
						+ "  	tm.trigger_name as \"triggerName\", "
						+ "    'abc' as \"senderId\" "
						+ "  	FROM public.m_sms_utility_cofiguration suc "
						+ "  	join public.m_sms_trigger_master tm on suc.trigger_id =tm.trigger_id "
						+ "  	join public.m_sms_category_master cm on suc.category_id=cm.category_id "
						+ "  	where suc.trigger_id = :triggerId "
						+ "	AND suc.status='A' "
						+ "	AND suc.schedule_type_id=2 "
						+ "    AND ( suc.is_email is null or suc.is_email=false)";

				smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("triggerId", triggerId)
						.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
			}
			else{
				String query = "SELECT suc.sms_utility_id as \"smsUtilityId\", "
						+" 	suc.trigger_id as\"triggerId\", "
						+"  	suc.template_description as \"templateDescription\", "
						+" 	suc.category_id as \"categoryId\", "
						+" 	cm.category_name as \"categoryName\","
						+"  	tm.trigger_name as \"triggerName\","
						+ " 	tm.rpt_report as \"rptReport\", "
						+ " ("
						+ "  select "
						+ "  concat(coalesce(whatsapp_calling_code,'91'),whatsapp_no ) "
						+ "  from "
						+ "  patient.t_patient_registration "
						+ "  where "
						+ "  patient_id =:patientId "
						+ "  limit 1) as \"patientWhatsAppNo\","
						+"  	(select sender_id from t_unit_application_billing_configuration where unit_id=:unitId) as \"senderId\","

						+"  	(select concat('Dr. ', coalesce(first_name, ''), ' ', coalesce(last_name, '')) from doctor.m_doctor_master where doctor_id=:treatingDocId limit 1) as \"treatingDoctorName\","
						+"   	(select mobile_number from doctor.m_doctor_master where doctor_id=:treatingDocId limit 1) as \"treatingDoctorMobileNo\","

						+"  	(select concat('Dr. ', coalesce(first_name, ''), ' ', coalesce(last_name, '')) from doctor.m_doctor_master where doctor_id=:consultingDocId limit 1) as \"consultingDoctorName\","
						+"   	(select mobile_number from doctor.m_doctor_master where doctor_id=:consultingDocId limit 1) as \"consultingDoctorMobileNo\","

						+"  	(select concat('Dr. ', coalesce(first_name, ''), ' ', coalesce(last_name, '')) from doctor.m_doctor_master where doctor_id=:admittingDocId limit 1) as \"admitingDoctorName\","
						+"   	(select mobile_number from doctor.m_doctor_master where doctor_id=:admittingDocId limit 1) as \"admitingDoctorMobileNo\","

						+"  	(select referral_name from adt.m_referral_master where referral_id =:referringDoctorId limit 1) as \"referringDoctorName\","
						+"   	(select referral_contact from adt.m_referral_master where referral_id=:referringDoctorId limit 1) as \"referringDoctorMobileNo\","


						+"		(select unit_name from m_unit_master where unit_id=:unitId)as \"hospitalName\",	"

						+"  	(select mobile_number from patient.t_patient_registration where patient_id=:patientId limit 1) as \"patientMobileNo\","
						+"  	(select uhid_number from patient.t_patient_registration where patient_id=:patientId limit 1) as \"patientUhidNo\","
						+"   	(select concat(coalesce(pre.prefix_code),' ',coalesce(first_name),' ',coalesce(last_name)) "
						+"   		from patient.t_patient_registration pat "
						+"   		inner join m_prefix_master pre on pre.prefix_id= pat.prefix_id "
						+"   		where patient_id=:patientId limit 1) as \"patientName\", "
						+ "			abc.pdf_generation_url as \"pdfGenerationUrl\" "
						+"  	FROM public.m_sms_utility_cofiguration suc "
						+"  	join public.m_sms_trigger_master tm on suc.trigger_id =tm.trigger_id "
						+"  	join public.m_sms_category_master cm on suc.category_id=cm.category_id "
						+ "  	join t_unit_application_billing_configuration abc on abc.unit_id=suc.unit_id "
						+ "  	where  suc.unit_id = :unitId "
						+"  	AND suc.trigger_id = :triggerId "
						+ "	AND suc.status='A' "
						+ "	AND suc.schedule_type_id=1 "
						+ "   AND ( suc.is_email is null or suc.is_email=false) ";

				 smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						//.setParameter("organizationId", smsInfo.getOrganizationId())
						.setParameter("unitId", smsInfo.getUnitId())
						.setParameter("triggerId", triggerId)
						.setParameter("treatingDocId", smsInfo.getTreatingDoctorId()!=null ? smsInfo.getTreatingDoctorId() : 0)
						.setParameter("consultingDocId", smsInfo.getConsultingDoctorId()!=null ? smsInfo.getConsultingDoctorId() : 0)
						.setParameter("admittingDocId", smsInfo.getAdmitingDoctorId()!=null ? smsInfo.getAdmitingDoctorId() : 0)
						.setParameter("patientId", smsInfo.getPatientId()!=null ? smsInfo.getPatientId() : 0)
						.setParameter("referringDoctorId", smsInfo.getReferringDoctorId()!=null ? smsInfo.getReferringDoctorId() : 0)
						.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
			}

		return new Response(SUCCESS, SUCCESS_CODE, null, smsUtilityConfigurationDto, null);
		} catch (HibernateException he) {
		logger.error("getSmsUtilityByTriggerId failed", he);
		} catch (Exception e) {
		logger.error("getSmsUtilityByTriggerId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRequiredDataList(Integer triggerId) {
		try{
			
			String query = "select "
					+ " 	reqData.data as \"data\", "
					+ " 	reqData.data_desc as \"dataDesc\" "
					+ " from "
					+ " 	t_trigger_data_mapper mapper "
					+ " inner join m_sms_required_data reqData on "
					+ " 	mapper.data_id = reqData.id "
					+ " where "
					+ " 	mapper.trigger_id =:triggerId "
					+ " ORDER BY reqData.data_desc ASC ";
			
			List<SmsUtilityConfigurationDto> smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("triggerId", triggerId)
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigurationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, smsUtilityConfigurationDto, null);
		} catch (HibernateException he) {
		logger.error("getRequiredDataList failed", he);
		} catch (Exception e) {
		logger.error("getRequiredDataList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCollectionSMSDetails(Integer organizationId, Integer unitId) throws ApplicationException {
		try{			
			LocalDate date = LocalDate.now().minusDays(1);
			Date todayDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			List<SmsUtilityConfigInfoDto> smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT cast(collection_sms.self_adm_count as text) as\"selfAdmissionPatientCount\",cast(collection_sms.credit_adm_count as text) as \"companyAdmissionPatientCount\",cast(collection_sms.self_dis_count as text) as \"selfDischargePatientCount\", cast(collection_sms.credit_dis_count as text) as \"companyDischargePatientCount\","
						+ "cast(collection_sms.new_visit_count as text) as \"opdNewCount\", cast(collection_sms.follow_up_visit_count as text) as \"opdFollowUpCount\", cast(collection_sms.deth_count as text) as \"deathCount\",cast(collection_sms.refund_amt as text) as \"refundAmount\",cast(collection_sms.deposit_amt as text) as \"depositeAmount\", "
						+ "cast(collection_sms.total_amt as text) as \"collectionAmount\" "
						+ "FROM collection_sms(:currentDate,:unitId)")
				//.setParameter("organizationId", smsInfo.getOrganizationId())
				.setString("currentDate", GlobalCommonDateUtils.getStringDate(todayDate, "yyyy-MM-dd"))
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, smsUtilityConfigurationDto, smsUtilityConfigurationDto);
		}catch(HibernateException he){
		logger.error("getCollectionSMSDetails failed", he);
		}
		catch(Exception e){
		logger.error("getCollectionSMSDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public List<SmsUtilityConfigInfoDto> getListOfMobileNoForScheduleSmsUtility(Integer orgId,Integer unitId,Integer triggerId) throws ApplicationException {
		try{			
			String query = "select sch_nos.mobile_no as \"mobileNo\" "
					+ " FROM public.t_sms_schedule_numbers sch_nos "
					+"	inner join public.m_sms_utility_cofiguration config on config.sms_utility_id=sch_nos.sms_utility_id "
					+" where config.trigger_id=:triggerId "
					+" and config.unit_id=:unitId "
					+" and config.org_id=:orgId "
					+" and sch_nos.status='A'";			
			List<SmsUtilityConfigInfoDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("triggerId", triggerId)
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
			return list;
		} catch (HibernateException he) {
		logger.error("getListOfMobileNoForScheduleSmsUtility failed", he);
		} catch (Exception e) {
		logger.error("getListOfMobileNoForScheduleSmsUtility failed", e);
		}
		return null;

	}

	@Override
	public Response getSmsTemplateDetails(List<Integer> unitIds) throws ApplicationException {
		try{
			String query = "SELECT suc.sms_utility_id as \"smsUtilityId\", "
			 +" 	suc.trigger_id as\"triggerId\", "
			 +"  	suc.template_description as \"templateDescription\", "
			 +" 	suc.category_id as \"categoryId\", "
			 +" 	cm.category_name as \"categoryName\","
			 +"  	tm.trigger_name as \"triggerName\","
			 + "	suc.org_id as \"organizationId\" ,"
			 + "	suc.unit_id as \"unitId\", "
			 +"		(select unit_name from m_unit_master where unit_id=suc.unit_id)as \"hospitalName\", "
			 +"  	(select sender_id from t_unit_application_billing_configuration where unit_id=suc.unit_id) as \"senderId\" "
			 +"  	FROM public.m_sms_utility_cofiguration suc "
			 +"  	join public.m_sms_trigger_master tm on suc.trigger_id =tm.trigger_id "
			 +"  	join public.m_sms_category_master cm on suc.category_id=cm.category_id "
			 + "	AND suc.status='A' AND suc.schedule_type_id=2"
			 + "    WHERE suc.unit_id in (:unitIds) ";			
			List<SmsUtilityConfigInfoDto> smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.setParameterList("unitIds", unitIds)
				.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, smsUtilityConfigurationDto, null);
		} catch (HibernateException he) {
		logger.error("getSmsTemplateDetails failed", he);
		} catch (Exception e) {
		logger.error("getSmsTemplateDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateStatus(SmsUtilityConfigurationDto smsUtilityConfigurationDto) throws ApplicationException {
		try{
			String query = "update SmsUtilityConfiguration set "
					+ "status=:status, "
					+ "updatedBy=:updatedBy, "
					+ "updatedDate=:updatedDate "
					+ "where smsUtilityId=:smsUtilityId";			
			sessionFactory.getCurrentSession()
				.createQuery(query)
				.setCharacter("status", smsUtilityConfigurationDto.getStatus())
				.setParameter("updatedBy", smsUtilityConfigurationDto.getUpdatedBy())
				.setParameter("smsUtilityId", smsUtilityConfigurationDto.getSmsUtilityId())
				.setDate("updatedDate", TimeZoneComponent.getDateByzone(smsUtilityConfigurationDto.getUnitId()))
				.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class))
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		logger.error("updateStatus failed", he);
		} catch (Exception e) {
		logger.error("updateStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmailUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo)
			throws ApplicationException {
		try {

			List<SmsUtilityConfigInfoDto> smsUtilityConfigurationDto = null;
			if(triggerId!=null && triggerId== ISmsUtilityConstants.SUBSCRIPTION_REMINDER){

				String query ="SELECT suc.sms_utility_id as \"smsUtilityId\", "
						+ " 	suc.trigger_id as \"triggerId\", "
						+ "  	suc.template_description as \"templateDescription\", "
						+ " 	suc.category_id as \"categoryId\", "
						+ " 	cm.category_name as \"categoryName\", "
						+ "    suc.mail_subject  as \"mailSubject\", "
						+ "  	tm.trigger_name as \"triggerName\", "
						+ "    'abc' as \"senderId\" "
						+ "  	FROM public.m_sms_utility_cofiguration suc "
						+ "  	join public.m_sms_trigger_master tm on suc.trigger_id =tm.trigger_id "
						+ "  	join public.m_sms_category_master cm on suc.category_id=cm.category_id "
						+ "  	AND suc.trigger_id = :triggerId "
						+ "	AND suc.status='A' "
						+ "	AND suc.schedule_type_id=2 "
						+ "    AND suc.is_email=true ";

				smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("triggerId", triggerId)
						.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
			}
			else{
			String query = " select "
					 + " 	suc.sms_utility_id as \"smsUtilityId\", "
					 + " 	suc.trigger_id as \"triggerId\", "
					 + " 	suc.template_description as \"templateDescription\", "
					 + " 	suc.category_id as \"categoryId\", "
					 + " 	suc.mail_subject as \"mailSubject\", "
					 + " 	cm.category_name as \"categoryName\", "
					 + " 	tm.trigger_name as \"triggerName\", "
					 + " 	tm.rpt_report as \"rptReport\", "
					 + " 	tuabc.sender_id as \"senderId\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat('Dr. ', coalesce(first_name)) "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:treatingDocId "
					 + " 	limit 1) as \"treatingDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:treatingDocId "
					 + " 	limit 1) as \"treatingEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat('Dr. ', coalesce(first_name)) "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:consultingDocId "
					 + " 	limit 1) as \"consultingDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:consultingDocId "
					 + " 	limit 1) as \"consultingEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat('Dr. ', coalesce(first_name)) "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:admittingDocId "
					 + " 	limit 1) as \"admitingDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:admittingDocId "
					 + " 	limit 1) as \"admitingEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		referral_name "
					 + " 	from "
					 + " 		adt.m_referral_master "
					 + " 	where "
					 + " 		referral_id =:referringDoctorId "
					 + " 	limit 1) as \"referringDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		referral_email "
					 + " 	from "
					 + " 		adt.m_referral_master "
					 + " 	where "
					 + " 		referral_id =:referringDoctorId "
					 + " 	limit 1) as \"referringEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		coalesce(first_name) "
					 + " 	from "
					 + " 		public.m_employee_master "
					 + " 	where "
					 + " 		employee_id =:employeeId "
					 + " 	limit 1) as \"employeeName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		public.m_employee_master "
					 + " 	where "
					 + " 		employee_id =:employeeId "
					 + " 	limit 1) as \"employeeEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		unit_name "
					 + " 	from "
					 + " 		m_unit_master "
					 + " 	where "
					 + " 		unit_id =:unitId)as \"hospitalName\", "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.email_id "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 2 "
					 + " 		and tcm.company_id = :insuranceId) as \"insuranceEmail\" , "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.company_desc "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 2 "
					 + " 		and tcm.company_id = :insuranceId) as \"insuranceName\" , "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.email_id "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 3 "
					 + " 		and tcm.company_id = :companyId) as \"companyEmail\" , "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.company_desc "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 3 "
					 + " 		and tcm.company_id = :companyId) as \"companyName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		patient.t_patient_registration "
					 + " 	where "
					 + " 		patient_id =:patientId "
					 + " 	limit 1) as \"patientEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		uhid_number "
					 + " 	from "
					 + " 		patient.t_patient_registration "
					 + " 	where "
					 + " 		patient_id =:patientId "
					 + " 	limit 1) as \"patientUhidNo\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat(coalesce(pre.prefix_code), ' ', coalesce(first_name), ' ', coalesce(last_name)) "
					 + " 	from "
					 + " 		patient.t_patient_registration pat "
					 + " 	inner join m_prefix_master pre on "
					 + " 		pre.prefix_id = pat.prefix_id "
					 + " 	where "
					 + " 		patient_id =:patientId "
					 + " 	limit 1) as \"patientName\", "
					 + " 	tuabc.smtp_host as \"smtpHost\", "
					 + " 	tuabc.smtp_port as \"smtpPort\", "
					 + " 	tuabc.smtp_user_id as \"mailId\", "
					 + " 	tuabc.smtp_user_pwd as \"mailPassword\", "
					 + " ("
					 + "  select "
					 + "  concat(coalesce(whatsapp_calling_code,'+91'),whatsapp_no ) "
					 + "  from "
					 + "  patient.t_patient_registration "
					 + "  where "
					 + "  patient_id =:patientId "
					 + "  limit 1) as \"patientWhatsAppNo\","
					 + " ( "
					 + " select "
					 + " tcm.mobile_no "
					 + " from "
					 + " public.t_company_master tcm "
					 + " where "
					 + " tcm.company_type_id = 3 "
					 + " and tcm.company_id = :companyId) as \"companyMobileNo\","
					 + " ( "
					 + " select "
					 + " tcm.mobile_no "
					 + " from "
					 + " public.t_company_master tcm "
					 + " where "
					 + " tcm.company_type_id = 2 "
					 + " and tcm.company_id = :insuranceId) as \"insuranceMobileNo\","
					 + " ( "
					 + " select "
					 + " mobile_number "
					 + " from "
					 + " public.m_employee_master "
					 + " where "
					 + " employee_id =:employeeId "
					 + " limit 1) as \"employeeMobileNo\","
					 + "( "
					 + " select "
					 + " mobile_number "
					 + " from "
					 + " doctor.m_doctor_master "
					 + " where "
					 + " doctor_id =:admittingDocId "
					 + " limit 1) as \"admitingDrMobileNo\"," 
					 + "( "
					 + " select "
					 + " mobile_number "
					 + " from "
					 + " doctor.m_doctor_master "
					 + " where "
					 + " doctor_id =:consultingDocId "
					 + " limit 1) as \"consultingDrMobileNo\","
					 + "( "
					 + " select "
					 + " mobile_number "
					 + " from "
					 + " doctor.m_doctor_master "
					 + " where "
					 + " doctor_id =:treatingDocId "
					 + " limit 1) as \"treatingDrMobileNo\","
					 + "	tuabc.pdf_generation_url as \"pdfGenerationUrl\" "
					 + " from "
					 + " 	public.m_sms_utility_cofiguration suc "
					 + " join public.m_sms_trigger_master tm on "
					 + " 	suc.trigger_id = tm.trigger_id "
					 + " join t_unit_application_billing_configuration tuabc on "
					 + " 	tuabc.unit_id = suc.unit_id "
					 + " join public.m_sms_category_master cm on "
					 + " 	suc.category_id = cm.category_id "
					 + " 	where suc.unit_id = :unitId "
					 + " 	and suc.trigger_id = :triggerId "
					 + " 	and suc.status = 'A' "
					 + " 	and suc.schedule_type_id = 1 "
					 + " 	and suc.is_email = true ";
			
			 smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				//.setParameter("organizationId", smsInfo.getOrganizationId())
				.setParameter("unitId", smsInfo.getUnitId())
				.setParameter("triggerId", triggerId)
				.setParameter("treatingDocId", smsInfo.getTreatingDoctorId()!=null ? smsInfo.getTreatingDoctorId() : 0)
				.setParameter("consultingDocId", smsInfo.getConsultingDoctorId()!=null ? smsInfo.getConsultingDoctorId() : 0)
				.setParameter("admittingDocId", smsInfo.getAdmitingDoctorId()!=null ? smsInfo.getAdmitingDoctorId() : 0)
				.setParameter("patientId", smsInfo.getPatientId()!=null ? smsInfo.getPatientId() : 0)
				.setParameter("referringDoctorId", smsInfo.getReferringDoctorId()!=null ? smsInfo.getReferringDoctorId() : 0)
				.setParameter("employeeId", smsInfo.getEmployeeId()!=null ? smsInfo.getEmployeeId() : 0)
				.setParameter("insuranceId", smsInfo.getInsuranceId()!=null ? smsInfo.getInsuranceId() : 0)
				.setParameter("companyId", smsInfo.getCompanyId()!=null ? smsInfo.getCompanyId() : 0)
				.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
			}
		return new Response(SUCCESS, SUCCESS_CODE, null, smsUtilityConfigurationDto, null);
	} catch (HibernateException he) {
			throw he;
	} catch (Exception e) {
		throw e;
	}
//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmailBulkUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo,
			Integer categoryId) throws ApplicationException {
try {
			
			String query = " select "
					 + " 	suc.sms_utility_id as \"smsUtilityId\", "
					 + " 	suc.trigger_id as \"triggerId\", "
					 + " 	suc.template_description as \"templateDescription\", "
					 + " 	suc.category_id as \"categoryId\", "
					 + " 	suc.mail_subject as \"mailSubject\", "
					 + " 	cm.category_name as \"categoryName\", "
					 + " 	tm.trigger_name as \"triggerName\", "
					 + " 	tm.rpt_report as \"rptReport\", "
					 + " 	tuabc.sender_id as \"senderId\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat('Dr. ', coalesce(first_name)) "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:treatingDocId "
					 + " 	limit 1) as \"treatingDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:treatingDocId "
					 + " 	limit 1) as \"treatingEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat('Dr. ', coalesce(first_name)) "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:consultingDocId "
					 + " 	limit 1) as \"consultingDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:consultingDocId "
					 + " 	limit 1) as \"consultingEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat('Dr. ', coalesce(first_name)) "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:admittingDocId "
					 + " 	limit 1) as \"admitingDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		doctor.m_doctor_master "
					 + " 	where "
					 + " 		doctor_id =:admittingDocId "
					 + " 	limit 1) as \"admitingEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		referral_name "
					 + " 	from "
					 + " 		adt.m_referral_master "
					 + " 	where "
					 + " 		referral_id =:referringDoctorId "
					 + " 	limit 1) as \"referringDoctorName\", "
					 + " 	( "
					 + " 	select "
					 + " 		referral_email "
					 + " 	from "
					 + " 		adt.m_referral_master "
					 + " 	where "
					 + " 		referral_id =:referringDoctorId "
					 + " 	limit 1) as \"referringEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		coalesce(first_name) "
					 + " 	from "
					 + " 		public.m_employee_master "
					 + " 	where "
					 + " 		user_id =:employeeId "
					 + " 	limit 1) as \"employeeName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		public.m_employee_master "
					 + " 	where "
					 + " 		user_id =:employeeId "
					 + " 	limit 1) as \"employeeEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		unit_name "
					 + " 	from "
					 + " 		m_unit_master "
					 + " 	where "
					 + " 		unit_id =:unitId)as \"hospitalName\", "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.email_id "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 2 "
					 + " 		and tcm.company_id = :insuranceId) as \"insuranceEmail\" , "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.company_desc "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 2 "
					 + " 		and tcm.company_id = :insuranceId) as \"insuranceName\" , "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.email_id "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 3 "
					 + " 		and tcm.company_id = :companyId) as \"companyEmail\" , "
					 + " 	( "
					 + " 	select "
					 + " 		tcm.company_desc "
					 + " 	from "
					 + " 		public.t_company_master tcm "
					 + " 	where "
					 + " 		tcm.company_type_id = 3 "
					 + " 		and tcm.company_id = :companyId) as \"companyName\", "
					 + " 	( "
					 + " 	select "
					 + " 		email "
					 + " 	from "
					 + " 		patient.t_patient_registration "
					 + " 	where "
					 + " 		patient_id =:patientId "
					 + " 	limit 1) as \"patientEmail\", "
					 + " 	( "
					 + " 	select "
					 + " 		uhid_number "
					 + " 	from "
					 + " 		patient.t_patient_registration "
					 + " 	where "
					 + " 		patient_id =:patientId "
					 + " 	limit 1) as \"patientUhidNo\", "
					 + " 	( "
					 + " 	select "
					 + " 		concat(coalesce(pre.prefix_code), ' ', coalesce(first_name), ' ', coalesce(last_name)) "
					 + " 	from "
					 + " 		patient.t_patient_registration pat "
					 + " 	inner join m_prefix_master pre on "
					 + " 		pre.prefix_id = pat.prefix_id "
					 + " 	where "
					 + " 		patient_id =:patientId "
					 + " 	limit 1) as \"patientName\", "
					 + " 	tuabc.smtp_host as \"smtpHost\", "
					 + " 	tuabc.smtp_port as \"smtpPort\", "
					 + " 	tuabc.smtp_user_id as \"mailId\", "
					 + " 	tuabc.smtp_user_pwd as \"mailPassword\", "
					 + "	tuabc.pdf_generation_url as \"pdfGenerationUrl\" "
					 + " from "
					 + " 	public.m_sms_utility_cofiguration suc "
					 + " join public.m_sms_trigger_master tm on "
					 + " 	suc.trigger_id = tm.trigger_id "
					 + " join t_unit_application_billing_configuration tuabc on "
					 + " 	tuabc.unit_id = suc.unit_id "
					 + " join public.m_sms_category_master cm on "
					 + " 	suc.category_id = cm.category_id "
					 + " 	where  suc.unit_id = :unitId "
					 + " 	and suc.trigger_id = :triggerId "
					 + " 	and suc.status = 'A' "
					 + " 	and suc.schedule_type_id = 1 "
					 + " 	and suc.is_email = true "
					 + "    and suc.category_id  = :categoryId ";
			
			List<SmsUtilityConfigInfoDto> smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				//.setParameter("organizationId", smsInfo.getOrganizationId())
				.setParameter("unitId", smsInfo.getUnitId())
				.setParameter("triggerId", triggerId)
				.setParameter("treatingDocId", smsInfo.getTreatingDoctorId()!=null ? smsInfo.getTreatingDoctorId() : 0)
				.setParameter("consultingDocId", smsInfo.getConsultingDoctorId()!=null ? smsInfo.getConsultingDoctorId() : 0)
				.setParameter("admittingDocId", smsInfo.getAdmitingDoctorId()!=null ? smsInfo.getAdmitingDoctorId() : 0)
				.setParameter("patientId", smsInfo.getPatientId()!=null ? smsInfo.getPatientId() : 0)
				.setParameter("referringDoctorId", smsInfo.getReferringDoctorId()!=null ? smsInfo.getReferringDoctorId() : 0)
				.setParameter("employeeId", smsInfo.getEmployeeId()!=null ? smsInfo.getEmployeeId() : 0)
				.setParameter("insuranceId", smsInfo.getInsuranceId()!=null ? smsInfo.getInsuranceId() : 0)
				.setParameter("companyId", smsInfo.getCompanyId()!=null ? smsInfo.getCompanyId() : 0)
				.setParameter("categoryId",categoryId)
				.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, smsUtilityConfigurationDto, null);
	} catch (HibernateException he) {
	logger.error("getEmailBulkUtilityByTriggerId failed", he);
	} catch (Exception e) {
	logger.error("getEmailBulkUtilityByTriggerId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMailUtilityByUnitId(String execution,Integer unitId) {
		try {
			
			String query = "select msuc.type as\"triggerType\" , "
					 + " 	msuc.sms_utility_id as \"smsUtilityId\", "
					 + " 	msuc.template_description as \"templateDescription\", "
					 + " 	msuc.category_id as \"categoryId\", "
					 + " 	msuc.mail_subject as \"mailSubject\", "
					 + " 	msuc.trigger_id as \"triggerId\", "
					 + " 	mstm.trigger_name as \"triggerName\", "
					 + " 	mstm.rpt_report as \"rptReport\", "
					 + " 	mscm.category_name as \"categoryName\", "
					 + " 	msuc.org_id as \"organizationId\" "
					 + " From public.m_sms_utility_cofiguration msuc  "
					 + " INNER JOIN public.m_sms_trigger_master mstm on mstm.trigger_id=msuc.trigger_id "
					 + " INNER JOIN public.m_sms_category_master mscm on mscm.category_id=msuc.category_id "
					 + " where  msuc.unit_id =:unitId " ;
			
					 List<SmsUtilityConfigInfoDto> smsUtilityConfigurationDto=sessionFactory.getCurrentSession()
					 .createSQLQuery(query)
					 .setParameter("unitId", unitId)
					 .setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, smsUtilityConfigurationDto, null);
		} catch (HibernateException he) {
		logger.error("getMailUtilityByUnitId failed", he);
		} catch (Exception e) {
		logger.error("getMailUtilityByUnitId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWhatsAppTemplateMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<SmsUtilityConfigurationDto> whatsAppTemplateList = sessionFactory.getCurrentSession().createSQLQuery(
							"select whatsapp_template_master_id as \"whatsAppTemplateMasterId\", template_desc as \"whatsAppTemplateDesc\" from public.m_whatsapp_template_master"
									+  " where organization_id = "+ organizationId
									+  "  and unit_id = "+ unitId +" and status = 'A' order by template_desc ASC ")
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigurationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, whatsAppTemplateList, null);
		} catch (HibernateException he) {
		logger.error("getWhatsAppTemplateMaster failed", he);
		} catch (Exception e) {
		logger.error("getWhatsAppTemplateMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
