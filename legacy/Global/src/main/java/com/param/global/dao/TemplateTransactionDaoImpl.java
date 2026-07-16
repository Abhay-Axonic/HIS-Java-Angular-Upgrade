package com.param.global.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.log4j.Logger;

import com.param.dozer.DozerFactory;
import com.param.entity.lis.histo.TTemplateResult;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.global.TTemplateResultDto;
import com.param.global.model.TemplateTransaction;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.dto.UnitMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TemplateTransactionDaoImpl extends GenericDao<TemplateTransaction, Integer> implements ITemplateTransactionDao, ICommonConstants,IError{

	private static final Logger logger = Logger.getLogger(TemplateTransactionDaoImpl.class);

	public TemplateTransactionDaoImpl() {
		super(TemplateTransaction.class);
	}

	@Autowired
	private DozerFactory factory;

	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Override
	public Response saveTemplateTransaction(TemplateTransactionDto dto) throws ApplicationException {
		try{
			TemplateTransaction template = factory.map(dto, TemplateTransaction.class, null);
			template.setStatus(ACTIVE);
			template.setReportEntryBy(dto.getReportEntryBy());
			template.setIsQuestionary(dto.getIsQuestionary()!=null && dto.getIsQuestionary()=='Y' ? dto.getIsQuestionary() : 'N');
			template.setDiagnosisId(dto.getDiagnosisId()==0?null:dto.getDiagnosisId());
			template.setmIcpcId(dto.getmIcpcId()==0?null:dto.getmIcpcId());
			template.setmIcpcSystemId(dto.getmIcpcSystemId()==0?null:dto.getmIcpcSystemId());
			 if( dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()=='Y' ) {
				 template.setReportReleaseDatetime(TimeZoneComponent.getDateByzone(dto.getUnitId())); 
				 template.setReportValidateby(dto.getReportvalidateBy()!= null ?  dto.getReportvalidateBy() : dto.getReportEntryBy());
			 }
			template.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			template.setSignedDate(dto.getSignedDate() != null && !dto.getSignedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getSignedDate(),"dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			if(dto.getTemplateTransactionId()!=0){
				template.setTemplateTransactionId(dto.getTemplateTransactionId());
				update(template);
			}else{
				template.setReportEntryDatetime(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				template = save(template);
			}
			if(dto.getTemplateTransactionId()!=0)
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, template.getTemplateTransactionId());
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateTemplateTransaction(TemplateTransactionDto dto) throws ApplicationException {
		try{
			
			TemplateTransaction template =null;
			if(dto.getTemplateTransactionId()==0) 
			{
				template = factory.map(dto, TemplateTransaction.class, null);
				template.setTemplateDoctorId(dto.getTemplateDoctorId());
				template.setReportValidateby(dto.getReportvalidateBy()!= null ?  dto.getReportvalidateBy() :null);
				template.setReportEntryBy(dto.getReportEntryBy());
				template.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				template.setSignedDate(dto.getSignedDate() != null && !dto.getSignedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getSignedDate(),"dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				template.setDiagnosisId(dto.getDiagnosisId()==0?null:dto.getDiagnosisId());
				template.setmIcpcId(dto.getmIcpcId()==0?null:dto.getmIcpcId());
				template.setIsQuestionary(dto.getIsQuestionary()!=null && template.getIsQuestionary()=='Y' ? template.getIsQuestionary() : 'N');
				template.setmIcpcSystemId(dto.getmIcpcSystemId()==0?null:dto.getmIcpcSystemId());
				template.setIsProvisionalRelease(dto.getIsProvisionalRelease());
				template.setReportEntryDatetime(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				template = save(template);
			}
			else {
				template = findById(dto.getTemplateTransactionId());
				template.setTemplateDoctorId(dto.getTemplateDoctorId() != null ? dto.getTemplateDoctorId() :template.getTemplateDoctorId() );
				template.setReportEntryBy(dto.getReportEntryBy() != null ?  dto.getReportEntryBy() :template.getReportEntryBy());
				template.setTemplateObject(dto.getTemplateObject());
				template.setIsQuestionary(dto.getIsQuestionary()!=null && template.getIsQuestionary()=='Y' ? template.getIsQuestionary() : 'N');
				template.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				template.setSignedDate(dto.getSignedDate() != null && !dto.getSignedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getSignedDate(),"dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				template.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
//				template.setReportEntryDatetime(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				if(dto.getIsReportRelease()!=null){
					template.setIsReportRelease(dto.getIsReportRelease());
				}else{
					if(dto.getIsFinalRelease()!=null){
						if(dto.getIsFinalRelease()=='Y' || dto.getIsFinalRelease()=='N') {
							template.setIsFinalRelease(dto.getIsFinalRelease());
							template.setReportValidateby(dto.getReportvalidateBy()!= null ?  dto.getReportvalidateBy() :null);
							template.setReportReleaseDatetime(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));	
							template.setIsProvisionalRelease(dto.getIsProvisionalRelease()!=null ? dto.getIsProvisionalRelease() :'N');
						}
						if(dto.getSkipRISReportEntry()!=null && dto.getSkipRISReportEntry() == 'Y'){
							if((dto.getIsAmend()!= null && dto.getIsAmend() =='Y') && (dto.getIsProvisionalRelease() == 'Y' && (dto.getIsFinalRelease() != null || dto.getIsFinalRelease()== 'Y'))){
								template.setReportEntryDatetime(TimeZoneComponent.getDateByzone(dto.getUnitId()));
							}
						}
					}else{
						template.setIsProvisionalRelease(dto.getIsProvisionalRelease()!=null ? dto.getIsProvisionalRelease() :'N');
					}
				}
				
				
				template = update(template);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, template.getTemplateTransactionId());
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response getTemplateByPatient(TemplateReqDto dto) throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession().createSQLQuery("select to_char(template_transaction.created_date, 'DD/MM/YYYY HH24:MI') as \"createdDate\" , "
					+ "to_char(template_transaction.created_date, 'DD/MM/YYYY') as \"createdDate1\" , "
					+ "template_transaction.template_object as \"templateObject\","
					+ "template_transaction.template_id as \"templateId\", "
					+ "template_transaction.template_url as \"templateUrl\", "
					+ "template_transaction.template_html as \"templateHtml\", "
					+ "template_transaction.patient_id as \"patientId\", "
					+ "template_transaction.template_transaction_id as \"templateTransactionId\", "
					+ "template_transaction.admission_id as \"admissionId\","
					+ "template_transaction.title as \"title\","
					+ " tem_mst.template_format_id as \"templateFormatId\" ,"
					+ " tem_mst.is_html_print as \"isHtmlPrint\", "
					+ " (case " 
					+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
					+ " when docUserMst.doctor_id is not null then concat('Dr. ', docUserMst.first_name, ' ', docUserMst.last_name) " 
					+ " else 'Admin' end) as \"userName\", "
					+ "concat('Dr. ',coalesce(dctMst.first_name,''),' ',coalesce(dctMst.middle_name,''),' ',coalesce(dctMst.last_name,'')) as \"doctorName\" "
					+ "from public.t_template_transaction template_transaction "
					+ "inner join public.t_template_master tem_mst on template_transaction.template_id = tem_mst.template_id "
					+ "left join doctor.m_doctor_master dctMst on dctMst.doctor_id=template_transaction.doctor_id "
					+ " left join public.m_user_master um on template_transaction.created_by = um.user_id " 
					+ " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
					+ " left join doctor.m_doctor_master docUserMst on um.user_id = docUserMst.user_id "
					+ "where "
					+ "template_transaction.org_id = :orgId "
					+ "and template_transaction.unit_id = :unitId "
					+ "and tem_mst.template_type_id =:templateTypeId "
				//	+ "and template_transaction.patient_id = :patientId "
					+ "and template_transaction.status = 'A' "
					+ " and template_transaction.status = 'A'").getQueryString();
			
			
			StringBuilder q1 = new StringBuilder(query);
			if(dto.getAdmissionId() != 0){
				q1.append(dto.getVisitTypeId()!=0&&dto.getVisitTypeId()==2&&dto.getTemplateTypeId()!=10?" and template_transaction.admission_id =  "+dto.getAdmissionId():"");
			}
			if(dto.getEncounterId() != 0){
				q1.append(dto.getVisitTypeId()!=0&&dto.getVisitTypeId()==1&&dto.getTemplateTypeId()!=10?" and template_transaction.encounter_id =  "+dto.getEncounterId():"");
			}
			
			if(dto.getPatientId() != 0){
				q1.append(dto.getPatientId()!=0 ? " and template_transaction.patient_id =  "+dto.getPatientId():"");
			}
			q1.append(dto.getAppointmentId()>0&&dto.getTemplateTypeId()==10?" and template_transaction.appointment_id =  "+dto.getAppointmentId():"");
			q1.append(dto.getAdmNoteId()>0&&dto.getTemplateTypeId()==10?" and template_transaction.adm_note_id =  "+dto.getAdmNoteId():"");
			q1.append(dto.getmIcpcId()!=0 ?" and template_transaction.m_icpc_id =  "+dto.getmIcpcId():"");
			q1.append(dto.getDiagnosisId()!=0 ?" and template_transaction.diagnosis_id =  "+dto.getDiagnosisId():"");
			q1.append(dto.getConversationId()!=null ?" and template_transaction.conversation_id =  '"+dto.getConversationId()+"'":"");
				List<TemplateTransactionDto> listTemplateMasterDto = (List<TemplateTransactionDto>)sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
				//	.setParameter("patientId", dto.getPatientId())
					.setParameter("templateTypeId", dto.getTemplateTypeId())
					.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).list();
				
				
				
				String query1 = sessionFactory.getCurrentSession().createSQLQuery("select distinct(template_transaction.template_id) as  \"templateId\",   " + 
						" to_char(template_transaction.created_date, 'DD/MM/YYYY HH24:MI') as \"createdDate\" ,   " +
						" to_char(template_transaction.created_date, 'DD/MM/YYYY') as \"createdDate1\" ,   " +
						" template_transaction.patient_id as \"patientId\",   "+
						" template_transaction.tabular_template_id as \"tabularTemplateId\",  " + 
						" template_transaction.tabular_template_id as \"formTemplateId\",  " + 
						" template_transaction.admission_id as \"admissionId\",  " + 
						" template_transaction.title as \"title\",  " + 
						" tem_mst.template_format_id as \"templateFormatId\" , "+
						" tem_mst.template_type_id as \"templateTypeId\", " + 
						" tem_mst.is_html_print as \"isHtmlPrint\",   " + 
						" (case   " + 
						" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name)   " + 
						" when docUserMst.doctor_id is not null then concat('Dr. ', docUserMst.first_name, ' ', docUserMst.last_name)   " + 
						" else 'Admin' end) as \"userName\",   " + 
						" concat('Dr. ',coalesce(dctMst.first_name,''),' ',coalesce(dctMst.middle_name,''),' ',coalesce(dctMst.last_name,'')) as \"doctorName\"   " + 
						" from public.m_patient_tabular_template_data_mapper template_transaction   " + 
						" inner join public.t_template_master tem_mst on template_transaction.template_id = tem_mst.template_id   " + 
						" left join doctor.m_doctor_master dctMst on dctMst.doctor_id=template_transaction.doctor_id   " + 
						" left join public.m_user_master um on template_transaction.created_by = um.user_id   " + 
						" left join public.m_employee_master empmst on um.user_id = empmst.user_id   " + 
						" left join doctor.m_doctor_master docUserMst on um.user_id = docUserMst.user_id   " + 
						"where   " +
						"template_transaction.organization_id = :orgId "+
						"and template_transaction.unit_id = :unitId "+
						"and tem_mst.template_type_id =:templateTypeId "+
						"and template_transaction.patient_id = :patientId "+
						" and template_transaction.status = 'A'").getQueryString();
				
				
				StringBuilder q2 = new StringBuilder(query1);
					List<TemplateTransactionDto> listTabularTemplateMasterDto = (List<TemplateTransactionDto>)sessionFactory.getCurrentSession().createSQLQuery(q2.toString())
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("patientId", dto.getPatientId())
						.setParameter("templateTypeId", dto.getTemplateTypeId())
						.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).list();
					if(listTabularTemplateMasterDto != null && listTabularTemplateMasterDto.size() > 0) {
						listTemplateMasterDto.addAll(listTabularTemplateMasterDto);
					}
				
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getQuestionaaiireDetails(TemplateReqDto dto) throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession().createSQLQuery("select to_char(template_transaction.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" , "
					+ "template_transaction.template_object as \"templateObject\", "
					+ "template_transaction.template_transaction_id as \"templateTransactionId\", "
					+ "template_transaction.title as \"title\" "
					+ "from public.t_template_transaction template_transaction where "
					+ "template_transaction.org_id = :orgId "
					+ "and template_transaction.unit_id = :unitId "
					+ "and template_transaction.order_details_id = :orderDetailsId order by template_transaction.template_transaction_id desc ").getQueryString();
			
			
			
			
			List<TemplateTransactionDto> listTemplateMasterDto = (List<TemplateTransactionDto>)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("orderDetailsId", dto.getOrderDetailsId())
					.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplateQuestionaaire(TemplateReqDto dto) throws ApplicationException 
	{
		try{
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_QUESTINAAIRE_TEMPLATE").getQueryString();
			StringBuilder q1 = new StringBuilder(query);
			q1.append(dto.getTemplateTypeId()!=0?" and tmp.template_type_id = "+dto.getTemplateTypeId():"");
			q1.append(" order by tmp.template_id desc ");
			List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("doctorId", dto.getDoctorId())
					.setParameter("specialityId", dto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveAdmissionConcent(AdmissionDto dto) throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession().createSQLQuery("update adt.t_admission set concent = :concent where admission_id = :admissionId ").getQueryString();
			
			Integer count =  sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", dto.getAdmissionId())
					.setParameter("concent", dto.getConcent())
					.executeUpdate();
			if(count>0)
			{
				return new Response<>(SUCCESS, SUCCESS_CODE, "Concent Saved Successfully.", null, dto);
			}
			else {
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateTemplateAsInactive(TemplateTransactionDto dto) throws ApplicationException {
		try{
			TemplateTransaction template=null;
			template = findById(dto.getTemplateTransactionId());
			template.setStatus('I');
			template.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			template = update(template);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(HibernateException he){
			logger.error("updateTemplateAsInactive failed: " + he.getMessage(), he);
		}catch(Exception e){
			logger.error("updateTemplateAsInactive failed: " + e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getSavedTemplateCount(TemplateTransactionDto templateTransactionDto) throws ApplicationException {
		try{
			//count by patient Id
			List<TemplateReqDto> tempListCount=sessionFactory.getCurrentSession().createSQLQuery("	select count(template_transaction_id) as \"templateCount\","
					+ " tempmast.template_type_id as \"templateTypeId\" "
					+ " from t_template_transaction tempTran"  
					+"	inner join t_template_master tempMast " 
					+"	on tempMast.template_id=tempTran.template_id"
					+ " where tempTran.status = 'A'"  
					+"	group by tempMast.template_type_id,tempTran.patient_id having tempTran.patient_id=:patientId and tempMast.template_type_id!=4 ")
					.setParameter("patientId", templateTransactionDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(TemplateReqDto.class)).list();
			
			//count by admissionId or encounterId(for complaint)
			List<TemplateReqDto> complaintTempListCount = new ArrayList<>();
			if(templateTransactionDto.getAdmissionId()!=null) {
			    complaintTempListCount=sessionFactory.getCurrentSession().createSQLQuery("select count(template_transaction_id) as \"templateCount\",tempmast.template_type_id as \"templateTypeId\" "
			    	+ "from t_template_transaction tempTran"  
					+"	inner join t_template_master tempMast " 
					+"	on tempMast.template_id=tempTran.template_id "
					+ " where tempTran.status = 'A'" 
					+"	group by tempMast.template_type_id,"
					+ " tempTran.admission_id having tempTran.admission_id=:admissionId"
					+ " and tempMast.template_type_id=4")
					.setParameter("admissionId", templateTransactionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(TemplateReqDto.class)).list();
			}
			if(templateTransactionDto.getEncounterId()!=null){
				  complaintTempListCount=sessionFactory.getCurrentSession().createSQLQuery("select count(template_transaction_id) as \"templateCount\",tempmast.template_type_id as \"templateTypeId\" "
					    	+ "from t_template_transaction tempTran"  
							+"	inner join t_template_master tempMast " 
							+"	on tempMast.template_id=tempTran.template_id"
							+ " where tempTran.status = 'A'" 
							+"	group by tempMast.template_type_id,"
							+ " tempTran.encounter_id having tempTran.encounter_id=:encounterId"
							+ " and tempMast.template_type_id=4")
							.setParameter("encounterId", templateTransactionDto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(TemplateReqDto.class)).list();
			}
			
			
			//count by admissionId or encounterId(for complaint)
			List<TemplateReqDto> tabularTemplateListCount = new ArrayList<>();
			if(templateTransactionDto.getAdmissionId()!=null) {
				tabularTemplateListCount=sessionFactory.getCurrentSession().createSQLQuery("      " + 
			    		"	select count(distinct(tempTran.template_id)) as \"templateCount\" ,tempMast.template_type_id as \"templateTypeId\"  " +
			    		"    from public.m_patient_tabular_template_data_mapper tempTran      " + 
			    		"	inner join public.t_template_master tempMast       " + 
			    		"	on tempMast.template_id=tempTran.template_id      " + 
			    		"  where tempTran.status = 'A'      " + 
			    		"	group by tempTran.template_id,tempTran.admission_id ,tempMast.template_type_id      " + 
			    		"	having tempTran.admission_id=:admissionId       " + 
			    		"	and tempMast.template_type_id!=4 ")
					.setParameter("admissionId", templateTransactionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(TemplateReqDto.class)).list();
			}
			if(templateTransactionDto.getEncounterId()!=null){
				tabularTemplateListCount=sessionFactory.getCurrentSession().createSQLQuery("      " + 
			    		"	select count(distinct(tempTran.template_id)) as \"templateCount\" ,tempMast.template_type_id as \"templateTypeId\"  " +
			    		"    from public.m_patient_tabular_template_data_mapper tempTran      " + 
			    		"	inner join public.t_template_master tempMast       " + 
			    		"	on tempMast.template_id=tempTran.template_id      " + 
			    		"  where tempTran.status = 'A'      " + 
			    		"	group by tempTran.template_id,tempTran.encounter_id ,tempMast.template_type_id      " + 
			    		"	having tempTran.encounter_id=:encounterId " +
			    		"	and tempMast.template_type_id!=4 ")
							.setParameter("encounterId", templateTransactionDto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(TemplateReqDto.class)).list();
			}
			
			tempListCount.addAll(complaintTempListCount);
			
			if(tabularTemplateListCount != null) {
				tempListCount.addAll(tabularTemplateListCount);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, tempListCount, null);
		}catch(HibernateException he){
			logger.error("getSavedTemplateCount failed: " + he.getMessage(), he);
		}catch(Exception e){
			logger.error("getSavedTemplateCount failed: " + e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response saveTemplateResult(TTemplateResultDto tTemplateResultDto) throws ApplicationException {
		try
		{
				if(tTemplateResultDto.getTemplateResId()!=null&&tTemplateResultDto.getTemplateResId()!=0) {
					
					TTemplateResult tTemplateRes = factory.map(tTemplateResultDto, TTemplateResult.class,
							"TTemplateResultDtoToTTemplateResult");
					tTemplateRes.setUpdatedDate(TimeZoneComponent.getDateByzone(tTemplateResultDto.getOrgUnitId()).getTime());
					tTemplateRes.setReportReleaseBy((tTemplateResultDto.getReportReleaseBy()!=null && tTemplateResultDto.getReportReleaseBy()>0)?tTemplateResultDto.getReportReleaseBy():tTemplateResultDto.getCreatedBy());
					 sessionFactory.getCurrentSession().update(tTemplateRes);
					return new Response(SUCCESS, SUCCESS_200, "Template Result Update Successfully.", null, null);
				/*
				 * } else { return new Response(ERROR, ERR_500,
				 * "Failed to Update Template Result.", null, null); }
				 */
				}else {
					
					TTemplateResult tTemplateRes = factory.map(tTemplateResultDto, TTemplateResult.class,
							"TTemplateResultDtoToTTemplateResult");
					tTemplateRes.setCreatedDate(TimeZoneComponent.getDateByzone(tTemplateResultDto.getOrgUnitId()).getTime());
					tTemplateRes.setReportReleaseBy((tTemplateResultDto.getReportReleaseBy()!=null && tTemplateResultDto.getReportReleaseBy()>0)?tTemplateResultDto.getReportReleaseBy():tTemplateResultDto.getCreatedBy());
					Integer result = (Integer)sessionFactory.getCurrentSession().save(tTemplateRes);
					if (result != null)
					{
						return new Response(SUCCESS, SUCCESS_200, "Template Result Added Successfully.", null, null);
						
					} else
					{
						return new Response(ERROR, ERR_500, "Failed to Added Template Result.", null, null);
					}
				}

		}catch(HibernateException exception){
			logger.error("saveTemplateResult failed: " + exception.getMessage(), exception);
		} catch (Exception e)
		{
			logger.error("saveTemplateResult failed: " + e.getMessage(), e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@SuppressWarnings("unused")
	@Override
	public Response updateLabSampleDetailsMaster(LabSampleDetailsMaster master) {
		try {
			
			LabSampleDetailsMaster labSampleDetailsMaster = (LabSampleDetailsMaster) sessionFactory.getCurrentSession().get(LabSampleDetailsMaster.class,master.getLabSampleDtlsId());
				labSampleDetailsMaster.setUpdatedBy(master.getCreatedBy());
				labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(master.getOrgUnitId()).getTime());
				labSampleDetailsMaster.setSampleStatusId(master.getSampleStatusId());
			//	labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
			//	labSampleDetailsMaster.setAssignUnit(labSampleDetailsMasterDto.getOrgUnitId());
				sessionFactory.getCurrentSession().update(labSampleDetailsMaster);

			
			if (labSampleDetailsMaster!=null) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, labSampleDetailsMaster);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, Integer.valueOf(0));
			}
		} catch (HibernateException exception) {
		logger.error("updateLabSampleDetailsMaster failed", exception);
		} catch (Exception e) {
		logger.error("updateLabSampleDetailsMaster failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);

	}

	@Override
	public Response updatePdfByte(byte[]  byteOutPutStream, Integer templateTransactionId)
			throws ApplicationException {
		try{
				TemplateTransaction mstr = findById(templateTransactionId);
				mstr.setPdfByte(byteOutPutStream);
				update(mstr);
				return new Response<>(SUCCESS, SUCCESS_CODE, "Concent Saved Successfully.", null, null);
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getPasswordAndUserId(Integer unitId, Integer organizationId) {
		try{
			String query = sessionFactory.getCurrentSession().createSQLQuery(" select pdf_user_id as \"pdfUserId\" ,pdf_password as \"pdfPassword\" from  public.m_unit_master where organization_id = :orgId   and unit_id = :unitId").getQueryString();
			
			UnitMasterDto dto = (UnitMasterDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	public void updateLabSampleDetailsMailStatus(Integer labSampleDtlsId) {

		try {
			Session session = sessionFactory.getCurrentSession();
			String query = "UPDATE lab.t_lab_sample_dtls " +
					"SET is_mail_send = true " +
					"WHERE lab_sample_dtls_id = :labSampleDtlsId";
			int rowsUpdated = session.createSQLQuery(query)
					.setParameter("labSampleDtlsId", labSampleDtlsId)
					.executeUpdate();
		} catch (Exception e) {
		logger.error("updateLabSampleDetailsMailStatus failed", e);
		}
	}

	@Override
	public Response checkPateintConsent(String conversationId, String HtmlString) throws ApplicationException, Exception {
		try{
          String querys = sessionFactory.getCurrentSession().createSQLQuery(" select conversation_id as \"conversationId\" from t_patient_consent_mapper where conversation_id =:conversationId").getQueryString();
			
          PatientConsentMapperDto dto = (PatientConsentMapperDto)sessionFactory.getCurrentSession().createSQLQuery(querys)
					.setParameter("conversationId", conversationId.toString())
					.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).uniqueResult();
			
			if(dto != null && dto.getConversationId() != null) {
				String query = sessionFactory.getCurrentSession().createSQLQuery("update t_patient_consent_mapper set html_template_data ='"+HtmlString.replaceAll("'", "''")+"' where conversation_id = :conversationId ").getQueryString();
				
				Integer count =  sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("conversationId", conversationId.toString())
						.executeUpdate();
				if(count>0)
				{
					return new Response<>(SUCCESS, SUCCESS_CODE, "Concent Saved Successfully.", null, count);
				}
			}else {
				return new Response<>(SUCCESS, SUCCESS_CODE, "Concent Saved Successfully.", null, 0);
			}
			
		}catch(Exception he){
			
			throw he;
		}
		return null;

	}

	@Override
	public Response printSavedTemplate(TemplateTransactionDto dto) throws ApplicationException {
		try {
			String query =	"		select " + 
					"	ttr.template_id as \"templateId\" ," + 
					"	ttr.admission_id as \"admissionId\", " + 
					"	ttr.patient_id as  \"patientId\" , " + 
					"	ttr.encounter_id as \"encounterId\", " + 
					"	ttr.template_html as \"templateHtml\" " + 
					" from " + 
					"	public.t_template_transaction ttr " + 
					" where  " + 
					"	(ttr.template_transaction_id = :templateTransactionId or 0 = :templateTransactionId)" + 
					"	and (ttr.admission_id = :admissionId or 0 = :admissionId) " + 
					"	and (ttr.patient_id = :patientId or 0 = :patientId) " + 
					"	and (ttr.encounter_id = :encounterId or 0 = :encounterId) " + 
					"	and ttr.org_id = :orgId " + 
					"	and ttr.unit_id = :unitId " ;
	
			List<TemplateTransactionDto> dto1 =sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("encounterId", dto.getEncounterId() != null ? dto.getEncounterId() :0)
					.setParameter("admissionId", dto.getAdmissionId() != null ? dto.getAdmissionId() :0)
					.setParameter("patientId", dto.getPatientId() != null ? dto.getPatientId() :0)
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("templateTransactionId", dto.getTemplateTransactionId())
				.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dto1, null);
		}catch (HibernateException he) {
		logger.error("printSavedTemplate failed", he);
		} catch (Exception e) {
		logger.error("printSavedTemplate failed", e);
		}
		return null;
	}

	@Override
	public Response printHTMLTemplate(TemplateTransactionDto dto) throws ApplicationException {
		try {
			
			PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
			TemplateTransactionDto  header = (TemplateTransactionDto) sessionFactory.getCurrentSession()
					.createSQLQuery(
							"SELECT template_header AS \"headerPart\", " +
									"template_footer AS \"footerPart\", " +
									"signture_img AS \"signtureImg\", " +
									"latter_head_img AS \"latterHeadImg\" " +
									"FROM public.m_report_header " +
									"WHERE organization_id = :organizationId " +
									"AND unit_id = :unitId")
					.setParameter("organizationId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class))
					.uniqueResult();

			String query = sessionFactory.getCurrentSession().createSQLQuery("select  " +
					"	to_char(template_transaction.created_date, 'DD/MM/YYYY HH24:MI') as \"createdDate\" ,  " + 
					"	template_transaction.template_object as \"templateObject\",  " + 
					"	template_transaction.template_id as \"templateId\",  " + 
					"	template_transaction.template_html as \"templateHtml\",  " + 
					"	template_transaction.patient_id as \"patientId\",  " + 
					"	template_transaction.template_transaction_id as \"templateTransactionId\",  " + 
					"	template_transaction.admission_id as \"admissionId\",  " + 
					"	template_transaction.title as \"title\",  " + 
					"	tem_mst.template_format_id as \"templateFormatId\" ,  " + 
					"	concat(mpm.prefix_code ,' ',tpr.first_name, ' ', tpr.last_name) as \"patientName\",  " + 
					"	tpr.uhid_number as \"uhidNumber\", "
					+ " 	gen.gender_name as \"gender\", " 
					+ " 	tpr.gender_id as \"genderId\", " + 
					"	trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, tpr.dob)),'999')) as \"ageString\",  " + 
					"	(case  " + 
					"		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name)  " + 
					"		when docUserMst.doctor_id is not null then concat('Dr. ', docUserMst.first_name, ' ', docUserMst.last_name)  " + 
					"		else 'Admin'  " + 
					"	end) as \"userName\",  " + 
					"	concat('Dr. ', coalesce(dctMst.first_name, ''), ' ', coalesce(dctMst.middle_name, ''), ' ', coalesce(dctMst.last_name, '')) as \"doctorName\"  " + 
					"from  " + 
					"	public.t_template_transaction template_transaction  " + 
					"left join public.t_template_master tem_mst on  " + 
					"	template_transaction.template_id = tem_mst.template_id  " + 
					"left join patient.t_patient_registration tpr on  " + 
					"	tpr.patient_id = template_transaction.patient_id   " + 
					"left join public.m_prefix_master mpm on  " + 
					"	mpm.prefix_id = tpr.prefix_id 	  " + 
					"left join doctor.m_doctor_master dctMst on  " + 
					"	dctMst.doctor_id = template_transaction.doctor_id  " + 
					"left join public.m_user_master um on  " + 
					"	template_transaction.created_by = um.user_id  " + 
					"left join public.m_employee_master empmst on  " + 
					"	um.user_id = empmst.user_id  " + 
					"left join doctor.m_doctor_master docUserMst on  " + 
					"	um.user_id = docUserMst.user_id  "
					+ "  left join public.m_gender_master gen on \r\n" + 
					" 	gen.gender_id = tpr.gender_id " + 
					"where  " + 
					"	template_transaction.template_transaction_id =:templateTransactionId "
					+ " AND template_transaction.status ='A' ").getQueryString();
			
			
			StringBuilder q1 = new StringBuilder(query);
				TemplateTransactionDto templateTransactionDto = (TemplateTransactionDto) sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("templateTransactionId", dto.getTemplateTransactionId())
					.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).uniqueResult();
			   System.out.println(dto.getTemplateTransactionId());
			   String latter = "<br><br><br><br>";
			   String body = templateTransactionDto.getTemplateHtml()+"<br>";
			   
			   Map<String,String> qMapResultsHearder = new HashMap<String, String>();
			    String   patientHeader = header.getHeaderPart();
			    if(patientHeader == null) {
			    	patientHeader = "";
			    }else {
			    	System.out.println("In header template");	
			    	String patternNew = "yyyy-mm-dd HH:mm:ss";
			    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patternNew);
			    	String date = simpleDateFormat.format(new Date());
			    	
                    patientHeader = patientHeader.replace("@patient",templateTransactionDto.getPatientName());
                    	qMapResultsHearder.put("@patient",templateTransactionDto.getPatientName());
                    	
                    patientHeader = patientHeader.replace("@date",templateTransactionDto.getCreatedDate());
                    	qMapResultsHearder.put("@date", templateTransactionDto.getCreatedDate());
                    	if(templateTransactionDto.getUhidNumber() != null) {
                    		patientHeader = patientHeader.replace("@uhid",templateTransactionDto.getUhidNumber());
                        	qMapResultsHearder.put("@uhid", templateTransactionDto.getUhidNumber());
                    	}else {
                    		patientHeader = patientHeader.replace("@uhid","");
                        	qMapResultsHearder.put("@uhid","");
                    	}
                    	if(templateTransactionDto.getAgeString() != null) {
                    		patientHeader = patientHeader.replace("@age",templateTransactionDto.getAgeString());
                    		qMapResultsHearder.put("@age", templateTransactionDto.getAgeString());
                    	}else{
                    		patientHeader = patientHeader.replace("@age","");
                    		qMapResultsHearder.put("@age", "");
                    	}
                    	if(templateTransactionDto.getGenderId() != null) {
                              patientHeader = patientHeader.replace("@gender",templateTransactionDto.getGenderId()==1 ? "Male" : templateTransactionDto.getGenderId()==2 ? "Female" : "Other");
                              qMapResultsHearder.put("@gender", templateTransactionDto.getGenderId()==1 ? "Male" : templateTransactionDto.getGenderId()==2 ? "Female" : "Other");
                    	}else {
                    		patientHeader = patientHeader.replace("@gender","");
                            qMapResultsHearder.put("@gender", "");
                    	}
                    patientHeader = patientHeader.replace("@doctorName",templateTransactionDto.getDoctorName());
                    	qMapResultsHearder.put("@referral",templateTransactionDto.getDoctorName());
                    patientHeader = patientHeader.replace("@reportPrintTime",date);
				    	qMapResultsHearder.put("@reportPrintTime",date);
				    	templateTransactionDto.setHeaderPart(patientHeader);
				    	
				    Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(patientHeader);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						
					}
					
					System.out.println("last header template");	
			    }
	
			   
			    if(templateTransactionDto.getTemplateHtml()!=null)
			    {
			    	
			    	System.out.println("In  template html");	
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(templateTransactionDto.getTemplateHtml());
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{
							Integer count = 0;
							String tempContent = templateTransactionDto.getTemplateHtml();
							for(String q : paramNames)
							{
								if (qMapResultsHearder.containsKey(q))
								{
									tempContent = tempContent.replace(q, qMapResultsHearder.get(q));
								}else{
									count++;
								}
							}
							
							body = tempContent; 
						}
					}	
					
					System.out.println("In  template html");	
			    }
			   
				System.out.println("template created");	
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, templateTransactionDto);
				
			}catch (HibernateException he) {
			logger.error("printHTMLTemplate failed", he);
				String errorMsg = TemplateTransactionDaoImpl.class.getName()+" :"+he.getMessage();
				return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
			} catch(Exception e) {
			logger.error("printHTMLTemplate failed", e);
			}
		return null;
	}

	@Override
	public Response checkTemplateTransactionExits(TemplateTransactionDto dto) throws ApplicationException {

		try{
			String query = sessionFactory.getCurrentSession().createSQLQuery(" select  order_details_id as \"orderDetailsId\"  from  public.t_template_transaction where order_details_id  =:OrderDetailsId and status  = 'A' order by 1 desc ").getQueryString();
			
			TemplateTransactionDto dto1 = (TemplateTransactionDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("OrderDetailsId", dto.getOrderDetailsId())
					.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto1);
		}catch(Exception e){
			
			throw e;
		}
	
	}

	@Override
	public Response getTemplateByPatientInOtims(TemplateReqDto dto) throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession().createSQLQuery("select to_char(template_transaction.created_date, 'DD/MM/YYYY HH24:MI') as \"createdDate\" , "
					+ "to_char(template_transaction.created_date, 'DD/MM/YYYY') as \"createdDate1\" , "
					+ "template_transaction.template_object as \"templateObject\","
					+ "template_transaction.template_id as \"templateId\", "
					+ "template_transaction.template_url as \"templateUrl\", "
					+ "template_transaction.template_html as \"templateHtml\", "
					+ "template_transaction.patient_id as \"patientId\", "
					+ "template_transaction.template_transaction_id as \"templateTransactionId\", "
					+ "template_transaction.admission_id as \"admissionId\","
					+ "template_transaction.title as \"title\","
					+ " tem_mst.template_format_id as \"templateFormatId\" ,"
					+ " tem_mst.is_html_print as \"isHtmlPrint\", "
					+ " (case " 
					+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
					+ " when docUserMst.doctor_id is not null then concat('Dr. ', docUserMst.first_name, ' ', docUserMst.last_name) " 
					+ " else 'Admin' end) as \"userName\", "
					+ "concat('Dr. ',coalesce(dctMst.first_name,''),' ',coalesce(dctMst.middle_name,''),' ',coalesce(dctMst.last_name,'')) as \"doctorName\" "
					+ "from public.t_template_transaction template_transaction "
					+ "inner join public.t_template_master tem_mst on template_transaction.template_id = tem_mst.template_id "
					+ "left join doctor.m_doctor_master dctMst on dctMst.doctor_id=template_transaction.doctor_id "
					+ " left join public.m_user_master um on template_transaction.created_by = um.user_id " 
					+ " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
					+ " left join doctor.m_doctor_master docUserMst on um.user_id = docUserMst.user_id "
					+ "where "
					+ "template_transaction.org_id = :orgId "
					+ "and template_transaction.unit_id = :unitId "
					+ "and tem_mst.template_type_id =:templateTypeId "
					+ "and template_transaction.patient_id = :patientId "
					+ "and template_transaction.template_id = :templateId "
					+ "and template_transaction.status = 'A' "
					+ " and template_transaction.status = 'A'").getQueryString();
			
			
			StringBuilder q1 = new StringBuilder(query);
			q1.append(dto.getVisitTypeId()!=0&&dto.getVisitTypeId()==2&&dto.getTemplateTypeId()!=10?" and template_transaction.admission_id =  "+dto.getAdmissionId():"");
			q1.append(dto.getVisitTypeId()!=0&&dto.getVisitTypeId()==1&&dto.getTemplateTypeId()!=10?" and template_transaction.encounter_id =  "+dto.getEncounterId():"");
			q1.append(dto.getAppointmentId()>0&&dto.getTemplateTypeId()==10?" and template_transaction.appointment_id =  "+dto.getAppointmentId():"");
			q1.append(dto.getAdmNoteId()>0&&dto.getTemplateTypeId()==10?" and template_transaction.adm_note_id =  "+dto.getAdmNoteId():"");
			q1.append(dto.getmIcpcId()!=0 ?" and template_transaction.m_icpc_id =  "+dto.getmIcpcId():"");
			q1.append(dto.getDiagnosisId()!=0 ?" and template_transaction.diagnosis_id =  "+dto.getDiagnosisId():"");
				List<TemplateTransactionDto> listTemplateMasterDto = (List<TemplateTransactionDto>)sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("patientId", dto.getPatientId())
					.setParameter("templateTypeId", dto.getTemplateTypeId())
					.setParameter("templateId", dto.getTemplateId())
					.setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).list();
		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}
