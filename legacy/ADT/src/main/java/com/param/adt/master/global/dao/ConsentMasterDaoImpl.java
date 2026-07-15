package com.param.adt.master.global.dao;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.dto.DischargeSummaryReportDto;
import com.param.adt.master.global.dto.ConsentMasterDto;
import com.param.adt.master.global.model.ConsentMaster;
import com.param.adt.master.global.model.PatientConsentMapper;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ConsentMasterDaoImpl extends GenericDao<ConsentMaster, Integer> implements IConsentMasterDao,ICommonConstants{

	
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	public ConsentMasterDaoImpl() {
		super(ConsentMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response addConsentMaster(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			ConsentMaster consentMaster = mapper.map(consentMasterDto, ConsentMaster.class, "ConsentMasterDto_to_ConsentMaster");
			consentMaster.setCreatedDate(TimeZoneComponent.getDateByzone(consentMasterDto.getUnitId() != null ? consentMasterDto.getUnitId() : 0));
			consentMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(consentMasterDto.getUnitId() != null ? consentMasterDto.getUnitId() : 0));
			consentMaster.setStatus('A');
			consentMaster = save(consentMaster);
			return new Response(SUCCESS, null, null, null, consentMaster);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateConsentMaster(ConsentMasterDto consentMasterDto) {
		try {

			ConsentMaster consentMaster = findById(consentMasterDto.getConsentMasterId());
			consentMaster.setConsentMasterCode(consentMasterDto.getConsentMasterCode());
			consentMaster.setConsentMasterName(consentMasterDto.getConsentMasterName());
			consentMaster.setTemplate(consentMasterDto.getTemplate());
			consentMaster.setTemplateType(consentMasterDto.getTemplateType());
			consentMaster.setConsentTypeMasterId(consentMasterDto.getConsentTypeMasterId());
			consentMaster.setUpdatedBy(consentMasterDto.getUpdatedBy());
			consentMaster.setFilePath(consentMasterDto.getFilePath()!=null ? consentMasterDto.getFilePath(): null);
			consentMaster.setDocumentName(consentMasterDto.getDocumentName()!=null ? consentMasterDto.getDocumentName():null);
			consentMaster.setUpdatedDate(ADTCommonDateUtils.getDate(consentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(consentMaster);
			return new Response<ConsentMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			return new Response<ConsentMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<ConsentMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getConsentByName(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			List<ConsentMasterDto> consentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_LIST_BY_NAME")
					.setParameter("consentName", consentMasterDto.getConsentMasterName())
					.setParameter("orgId", consentMasterDto.getOrganizationId())
					.setParameter("unitId", consentMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ConsentMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentByNameNotId(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			List<ConsentMasterDto> consentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_LIST_BY_NAME_NOT_ID")
					.setParameter("consentName", consentMasterDto.getConsentMasterName())
					.setParameter("consentId", consentMasterDto.getConsentMasterId())
					.setParameter("orgId", consentMasterDto.getOrganizationId())
					.setParameter("unitId", consentMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ConsentMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentById(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			List<ConsentMasterDto> consentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_LIST_BY_ID")
					.setParameter("consentId", consentMasterDto.getConsentMasterId())
					.setResultTransformer(Transformers.aliasToBean(ConsentMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateConsentStatus(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {

			ConsentMaster consentMaster = findById(consentMasterDto.getConsentMasterId());
			consentMaster.setStatus(consentMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			consentMaster.setUpdatedBy(consentMasterDto.getUpdatedBy());
			consentMaster.setUpdatedDate(ADTCommonDateUtils.getDate(consentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(consentMaster);
			return new Response<ConsentMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			return new Response<ConsentMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<ConsentMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getActiveConsentList(Integer orgId,Integer unitId) throws ApplicationException {
		try {
			List<ConsentMasterDto> consentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CONSENT_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ConsentMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentMasterList(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			List<ConsentMasterDto> consentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_LIST")
					.setParameter("orgId", consentMasterDto.getOrganizationId())
					.setParameter("unitId", consentMasterDto.getUnitId())
					.setFirstResult(consentMasterDto.getOffset() != null ? consentMasterDto.getOffset() : 0)
					.setMaxResults(consentMasterDto.getNoOfRecordsPerPage() != null ? consentMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ConsentMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getCount(ConsentMasterDto consentMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(ConsentMaster.class); 
				c.add(Restrictions.eq("organizationId", consentMasterDto.getOrganizationId()));
				c.add(Restrictions.eq("unitId", consentMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getConsentListByConsentTypeId(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			List<ConsentMasterDto> consentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_LIST_BY_CONSENT_TYPE_ID")
					.setParameter("consentTypeMasterId", consentMasterDto.getConsentTypeMasterId())
					.setParameter("unitId", consentMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ConsentMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateConsentMasterForFileUploading(ConsentMasterDto consentMasterDto) throws ApplicationException {
		try {
			
			ConsentMaster mst = findById(consentMasterDto.getConsentMasterId());
			mst.setFilePath(consentMasterDto.getFilePath());
			mst.setDocumentName(consentMasterDto.getDocumentName());
			update(mst);
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientConsent(PatientConsentMapperDto dto) throws ApplicationException {
		try{
			
			if(dto.getConsentTypeId() == 1 ) {
				String Query = "SELECT "
						+ "       cm.consent_type_id          AS \"consentTypeId\", "
						+ "       cm.consent_master_id        AS \"consentMasterId\", "
						+ "       cm.consent_master_name    AS \"consentMasterName\", "
						+ "       cm.template_type as \"templeteTypeId\" ,"
						+ "       cm.file_path as \"documentPath\" ,"
						+ "       cm.template as \"templateOriginal\", "
						+ "       cm.template AS \"templateDesc\" "
						+ " FROM   adt.m_consent_master cm "
						+ " inner join adt.m_consent_type_master mctm on mctm.consent_type_master_id = cm.consent_type_id and cm.status='A' ";
						
				    StringBuffer sb = new StringBuffer(Query);
				sb.append(" and cm.organization_id = :organizationId and mctm.status = 'A' ");
				List<PatientConsentMapperDto> list = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						//.setParameter("consentTypeId", dto.getConsentTypeId())
						.setParameter("organizationId", dto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
				
			}else {
				String Query = "SELECT mppr.consent_transaction_id AS \"consentTransactionId\", "
						+ "       cm.consent_type_id          AS \"consentTypeId\", "
						+ "       cm.consent_master_id        AS \"consentMasterId\", "
						+ "       cm.consent_master_name    AS \"consentMasterName\", "
						+ "       cm.template as \"templateOriginal\", "
						+ "       ( CASE "
						+ "           WHEN mppr.template_desc IS NULL THEN cm.template "
						+ "           ELSE mppr.template_desc "
						+ "         END )                     AS \"templateDesc\" "
						+ " FROM   adt.m_consent_master cm "
						+ "       LEFT JOIN PUBLIC.t_patient_consent_mapper mppr "
						+ "              ON cm.consent_master_id = mppr.consent_master_id "
						+ "                 AND mppr.status = 'A' ";

				    StringBuffer sb = new StringBuffer(Query);
				if (dto.getAdmissionId() != null && dto.getAdmissionId() > 0) {
					sb.append(" AND mppr.admission_id = :admissionId");
				}
				if (dto.getAppointmentId() != null && dto.getAppointmentId() > 0) {
					sb.append(" AND mppr.appointment_id = :appointmentId");
				}
				if (dto.getEncounterId() != null && dto.getEncounterId() > 0) {
					sb.append(" AND mppr.encounter_id = :encounterId");
				}

				sb.append(" WHERE  cm.consent_type_id = :consentTypeId AND cm.status = 'A'");

				org.hibernate.Query query = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString());

				if (dto.getAdmissionId() != null && dto.getAdmissionId() > 0)
					query.setParameter("admissionId", dto.getAdmissionId());

				if (dto.getAppointmentId() != null && dto.getAppointmentId() > 0)
					query.setParameter("appointmentId", dto.getAppointmentId());

				if (dto.getEncounterId() != null && dto.getEncounterId() > 0)
					query.setParameter("encounterId", dto.getEncounterId());

				List<PatientConsentMapperDto> list  = query.setParameter("consentTypeId", dto.getConsentTypeId())
						.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			}
			
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response savePatientConsent(PatientConsentMapperDto dto) throws ApplicationException {
		try{
			
			Integer consentTransactionId = 0 ;
			if(dto.getConsentTransactionId()!=null &&dto.getConsentTransactionId()>0)
			{
				PatientConsentMapper master = new PatientConsentMapper();
				master.setConsentTransactionId(dto.getConsentTransactionId());
				master.setConsentMasterId(dto.getConsentMasterId());
				master.setOrganizationId(dto.getOrganizationId());
				master.setUnitId(dto.getUnitId());
				master.setTemplateDesc(dto.getTemplateDesc());
				master.setStatus(ACTIVE);
				master.setUnitId(dto.getUnitId());
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
				master.setAdmissionId(dto.getAdmissionId()!=null ? dto.getAdmissionId() : null);
				master.setEncounterId(dto.getEncounterId()!=null ?  dto.getEncounterId() : null);
				master.setVideoPath(dto.getVideoPath() != null ? dto.getVideoPath() : null);
				master.setDocumentPath(dto.getDocumentPath()!= null ? dto.getDocumentPath()  : null);
				master.setDocumentName(dto.getDocumentName()!= null ? dto.getDocumentName() : null);
				master.setIsFromConsentPrint(dto.getIsFromConsentPrint()!= null ? dto.getIsFromConsentPrint()  : false);
				master.setAppointmentId(dto.getAppointmentId());
				master.setFormTemplateId(dto.getFormTemplateId());
				master.setTemplateFormUrl(dto.getTemplateFormUrl());
				master.setConversationId(dto.getConversationId());
				sessionFactory.getCurrentSession().update(master);
			}
			else {
				PatientConsentMapper master = new PatientConsentMapper();
				master.setConsentMasterId(dto.getConsentMasterId());
				master.setOrganizationId(dto.getOrganizationId());
				master.setUnitId(dto.getUnitId());
				master.setTemplateDesc(dto.getTemplateDesc());
				master.setStatus(ACTIVE);
				master.setUnitId(dto.getUnitId());
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
				master.setAdmissionId(dto.getAdmissionId()!=null ? dto.getAdmissionId() : null);
				master.setEncounterId(dto.getEncounterId()!=null ?  dto.getEncounterId() : null);
				master.setAppointmentId(dto.getAppointmentId());
				master.setDocumentName(dto.getDocumentName()!= null ? dto.getDocumentName() : null);
				master.setDocumentPath(dto.getDocumentPath()!= null ? dto.getDocumentPath()  : null);
				master.setIsFromConsentPrint(dto.getIsFromConsentPrint()!= null ? dto.getIsFromConsentPrint()  : false);
				master.setFormTemplateId(dto.getFormTemplateId());
				master.setTemplateFormUrl(dto.getTemplateFormUrl());
				master.setConversationId(dto.getConversationId());
				consentTransactionId = (Integer) sessionFactory.getCurrentSession().save(master);
				master.setConsentTransactionId(consentTransactionId);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, consentTransactionId);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getConsentListByAdmissionAndEncounterId(PatientConsentMapperDto dto) throws ApplicationException {
try{
			
			
			String Query = " select  tpcm.consent_transaction_id as \"consentTransactionId\", " + 
					"tpcm.consent_master_id as \"consentMasterId\", " + 
					"tpcm.admission_id as \"admissionId\"," + 
					"tpcm.encounter_id as \"encounterId\", " + 
					"tpcm.template_desc as \"templateDesc\", " + 
					"to_char(tpcm.created_date,'DD-MM-YYYY HH:mm:ss') as \"createdDate\", " + 
					"tpcm.status as \"status\"," + 
					"tpcm.document_name as \"documentName\"," + 
					"tpcm.document_path as \"documentPath\"," + 
					"tpcm.video_path as \"videoPath\"," + 
					"mcm.consent_master_name  as \"consentMasterName\"," + 
					"mcm.template_type as \"templateType\"," + 
					"mctm.consent_type_master_name as \"consentTypeMasterName\","
					+ " mctm.consent_type_master_id  as \"consentTypeId\"," + 
					"(case when ta.admission_number is not null then " + 
					"ta.admission_number " + 
					"else tem.encounter_number end) as \"visitNumber\" ,"
					+ "tpcm.form_template_id as \"formTemplateId\" " + 
					"from t_patient_consent_mapper tpcm " + 
					"inner join adt.m_consent_master mcm   on mcm.consent_master_id = tpcm.consent_master_id " + 
					"inner join adt.m_consent_type_master mctm on mctm.consent_type_master_id  = mcm.consent_type_id " + 
					"left join adt.t_admission ta  on ta.admission_id  = tpcm.admission_id " + 
					"left join opd.t_encounter_master tem on  tem.encounter_id = tpcm.encounter_id "
					+ " WHERE  tpcm.is_from_consent_print  = true ";
					
			    StringBuffer sb = new StringBuffer(Query);
				if (dto.getAdmissionId()!=null && dto.getAdmissionId()>0) {
					sb.append(" AND tpcm.admission_id = :admissionId");
			    }
				if (dto.getEncounterId()!=null && dto.getEncounterId()>0) {
					sb.append(" AND tpcm.encounter_id = :encounterId");
				}
					sb.append(" order by tpcm.consent_transaction_id desc ");
					
					
			List<PatientConsentMapperDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("admissionId", dto.getAdmissionId())
					.setParameter("encounterId", dto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response cancelConsentPrint(Integer consentTransactionId) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery(" update public.t_patient_consent_mapper set status='I' where consent_transaction_id =:consentTransactionId ")
					.setParameter("consentTransactionId", consentTransactionId)
					.executeUpdate();
			
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, "400", COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public ByteArrayOutputStream createConsentPrintPDF(PatientConsentMapperDto dto) throws ApplicationException {
		try {
			   ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
			PatientConsentMapperDto finalDto =  getPatientConsentDetails(dto);
			DischargeSummaryReportDto patientData = new DischargeSummaryReportDto();
			if(dto.getAdmissionId()!=0) {
				 patientData= getPatientDetails(dto);
			}else if(dto.getEncounterId()!=0) {
				 patientData= getPatientOpDetails(dto);
			}
			if(patientData != null && finalDto != null) {
				PdfWriter pdfWriter = null;
				
			//	String consentSummHead="Consent Print";
				String patientHeader="";
		      //  String htmlContent1="";
		        if(dto.getAdmissionId()!=0) {
				 patientHeader= "<table border='1' >"  
						+ "<tr><th width='500' height='20' style='text-align: center;'><font size='+1'><b>CONSENT PRINT</b></font></th></tr>"  
						+ "</table>"  
						+ "<table border='0' style='margin-left: 5%; margin-right: 5%;'>"  
						+ "<tr>"  
						+ "<th width='100' ><font size='-1'><b>UHID</b></font></th>"  
						+ "<th width='170' ><font size='-1'>: "+patientData.getUhidNumber()+" </font></th>"
								+ "<th width='100' ><font size='-1'><b>Patient Name</b></font></th>"  
								+ "<th width='180' ><font size='-1'>: <b>"+patientData.getPatientName()+"</b></font></th>"  
					
						+ "</tr>"  
						+ "<tr>"  
						+ "<th width='100' ><font size='-1'><b>Age</b></font></th>"  
						+ "<th width='170' ><font size='-1'>: "+patientData.getAge()+"</font></th>"  
								+ "<th width='100' ><font size='-1'><b>Gender</b></font></th>"  
								+ "<th width='180' ><font size='-1'>: "+patientData.getGender()+"</font></th>"  
					
						+ "</tr>"  
						+ "<tr>"  
						+ "<th width='100' ><font size='-1'><b>Date of Admission</b></font></th>"  
						+ "<th width='170' ><font size='-1'>: "+patientData.getAdmissionDate()+"</font></th>"  
								+ "<th width='100' ><font size='-1'><b>Time of Admission</b></font></th>"  
								+ "<th width='180' ><font size='-1'>: "+patientData.getAdmissionTime()+"</font></th>" 
					
						+ "</tr>"  
						+ "<tr>"  
						+ "<th width='100' ><font size='-1'><b>Ward Name</b></font></th>"  
						+ "<th width='170' ><font size='-1'>: "+patientData.getWardName()+"</font></th>" 
						+ "<th width='100' ><font size='-1'><b>Bed Name</b></font></th>"  
						+ "<th width='180' ><font size='-1'>: "+patientData.getBedNumber()+"</font></th>"  
						+ "</tr>"  
					
						+ "<tr>"  
						+ "<th width='100' ><font size='-1'><b>Consultant</b></font></th>"  
						+ "<th width='170' ><font size='-1'>: <b>"+patientData.getDoctorName()+"</b></font></th>"  
								+ "<th width='100' ><font size='-1'><b>Department</b></font></th>"  
								+ "<th width='180' ><font size='-1'>: "+patientData.getDepartmentName()+"</font></th>"  
						+ "</tr>"  
						+ "<tr>"  
					
					/*	+ "<tr>"  
						+ "<th width='100' ><font size='-1'><b>Date of Discharge</b></font></th>"  
						+ "<th width='170' ><font size='-1'>: "+patientData.getDischargeDate()+"</font></th>"    
						+ "<th width='100' ><font size='-1'><b>Time of Discharge</b></font></th>"  
						+ "<th width='180' ><font size='-1'>: "+patientData.getDischargeTime()+"</font></th>"  
						+ "</tr>" */ 
						+ "</table>";
		        }else if(dto.getEncounterId()!=0) {
		        	 patientHeader= "<table border='1' >"  
								+ "<tr><th width='500' height='20' style='text-align: center;'><font size='+1'><b>CONSENT PRINT</b></font></th></tr>"  
								+ "</table>"  
								+ "<table border='0' style='margin-left: 5%; margin-right: 5%;'>"  
								+ "<tr>"  
								+ "<th width='100' ><font size='-1'><b>UHID</b></font></th>"  
								+ "<th width='170' ><font size='-1'>: "+patientData.getUhidNumber()+" </font></th>"
										+ "<th width='100' ><font size='-1'><b>Patient Name</b></font></th>"  
										+ "<th width='180' ><font size='-1'>: <b>"+patientData.getPatientName()+"</b></font></th>"  
							
								+ "</tr>"  
								+ "<tr>"  
								+ "<th width='100' ><font size='-1'><b>Age</b></font></th>"  
								+ "<th width='170' ><font size='-1'>: "+patientData.getAge()+"</font></th>"  
										+ "<th width='100' ><font size='-1'><b>Gender</b></font></th>"  
										+ "<th width='180' ><font size='-1'>: "+patientData.getGender()+"</font></th>"  
							
								+ "</tr>"  
								+ "<tr>"  
								+ "<th width='100' ><font size='-1'><b>Consultant</b></font></th>"  
								+ "<th width='170' ><font size='-1'>: <b>"+patientData.getDoctorName()+"</b></font></th>"  
										+ "<th width='100' ><font size='-1'><b>Department</b></font></th>"  
										+ "<th width='180' ><font size='-1'>: "+patientData.getDepartmentName()+"</font></th>"  
								+ "</tr>"  
								+ "<tr>"  
								+ "</table>";
		        }
					 // htmlContent1=consentSummHead.concat(patientHeader);
					  String htmlContent="<br>";
						htmlContent = htmlContent.concat(finalDto.getTemplateDesc()+" <br>");
					String footerText="<br><br><br><table border='0'><tr><td colspan='4'></td><td><p align='center'><font size='-1'><b>Signature</b></font></p></td></tr>"
									+ "<tr><td colspan='4'></td><td><p align='center'><font size='-1'><b>( Register / CMO )</b></font></p></td></tr></table>";
					htmlContent = htmlContent.concat(footerText);
					try {
						StringBuffer sb = new StringBuffer(htmlContent);
						StringBuffer sb1 = new StringBuffer(patientHeader);
				        Document documentPDF =new Document();
						pdfWriter = PdfWriter.getInstance(documentPDF,byteStream);
				        pdfWriter.setLinearPageMode();
				        pdfWriter.setFullCompression();
				        
				        documentPDF.open();
				        HTMLWorker htmlWorker = new HTMLWorker(documentPDF);
				        StringReader sr = new StringReader(sb.toString());
				        StringReader sr1 = new StringReader(sb1.toString());
				        htmlWorker.parse(sr1);
				        LineSeparator lsp= new LineSeparator();
				        lsp.setOffset(-15);
				        Chunk linebreak = new Chunk(lsp);
				        documentPDF.add(linebreak);
				        htmlWorker.parse(sr);
				        htmlWorker.close();
				        sr.close();
				        documentPDF.close();
			            pdfWriter.close();
			            byteStream.close();
				    } catch (FileNotFoundException e) {
				        
				        return null;
				    } catch (IOException e) {
				        
				        return null;
				    } catch (DocumentException e) {
				        
				        return null;
				    }
					return byteStream;
				}
			 return byteStream;
			}catch (HibernateException he) {
			} catch (Exception e) {
				
			}
		return null;
	}

	private DischargeSummaryReportDto getPatientOpDetails(PatientConsentMapperDto dto) {
		DischargeSummaryReportDto newDto= null;
		try {
			String query =	" SELECT pim.uhid_number as \"uhidNumber\", " + 
					"    concat(COALESCE(pm.prefix_code,''),' ',COALESCE(pim.first_name,''), ' ', COALESCE(pim.middle_name, ''), ' ', " + 
					"     COALESCE(pim.last_name, '')) as \"patientName\", " + 
					"    concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pim.dob)),'M ',EXTRACT(days from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'D ') as \"age\", " + 
					"   gm.gender_name as \"gender\", " + 
					"    um.unit_name as \"unitName\", " + 
					" um.unit_logo as \"unitLogo\", " + 
					"    um.unit_address as \"unitAddress\", " + 
					"    CONCAT(dm.first_name,' ',dm.middle_name,' ',dm.last_name) AS \"doctorName\", " + 
					"      (case when enc.dept_id is not null then (select  spm.speciality_name from doctor.m_speciality_master spm  " + 
					"         where spm.speciality_id=enc.dept_id and spm.status='A') end) as \"departmentName\" " + 
					"  from  opd.t_encounter_master enc  " + 
					"     LEFT join patient.t_patient_registration pim on pim.patient_id = enc.patient_id " + 
					"     LEFT join m_unit_master um ON enc.unit_id = um.unit_id " + 
					"     LEFT JOIN m_prefix_master pm ON pim.prefix_id = pm.prefix_id " + 
					"     LEFT JOIN m_gender_master gm ON pim.gender_id = gm.gender_id " + 
					"     LEFT JOIN patient.t_patient_details patDet ON pim.patient_id = patDet.patient_id  " + 
					"     LEFT JOIN m_organization_master om ON enc.organization_id = om.organization_id " + 
					"     LEFT JOIN doctor.m_doctor_master dm ON dm.doctor_id = enc.doctor_id " + 
					"     LEFT JOIN m_user_master udm ON enc.created_by = udm.user_id " + 
					"     LEFT JOIN opd.t_encounter_details encDtls ON enc.encounter_id = encDtls.encounter_id " + 
					"  WHERE  enc.encounter_id=:encounterId ";
			newDto = (DischargeSummaryReportDto)sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("encounterId", dto.getEncounterId())
				.setResultTransformer(Transformers.aliasToBean(DischargeSummaryReportDto.class)).uniqueResult();
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
	return newDto;

	}

	private DischargeSummaryReportDto getPatientDetails(PatientConsentMapperDto dto) {
		DischargeSummaryReportDto newDto= null;  
		try {
				String query =	"SELECT pim.uhid_number as \"uhidNumber\"," + 
					"    concat(COALESCE(pm.prefix_code,''),' ',COALESCE(pim.first_name,''), ' ', COALESCE(pim.middle_name, ''), ' '," + 
					"     COALESCE(pim.last_name, '')) as \"patientName\", " + 
					"    concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pim.dob)),'M ',EXTRACT(days from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'D ') as \"age\"," + 
					"   gm.gender_name as \"gender\"," + 
					"    um.unit_name as \"unitName\","+
				    " um.unit_logo as \"unitLogo\"," + 
					"    um.unit_address as \"unitAddress\"," + 
					"    CONCAT(dm.first_name,' ',dm.middle_name,' ',dm.last_name) AS \"doctorName\"," + 
					"      (case when ad.speciality_id is null then (select  spm.speciality_name from t_unit_application_billing_configuration app " +
				    "           inner join doctor.m_speciality_master spm on spm.speciality_id=app.er_speciality_id"+
				    "         where app.organization_id=ad.organization_id and app.unit_id=ad.unit_id and app.status='A') else (select  spm.speciality_name from doctor.m_speciality_master spm "+
				    "         where spm.speciality_id=ad.speciality_id) end) as \"departmentName\"," + 
					"    to_char(adet.doa,'dd/MM/yyyy') as \"admissionDate\"," + 
					"    to_char(adet.doa,'HH:MI AM') as \"admissionTime\"," + 
					"    wm.ward_name as \"wardName\"," + 
					"    bm.bed_number as \"bedNumber\"" + 
					"  from  adt.t_admission ad  " + 
					"     LEFT join patient.t_patient_registration pim on pim.patient_id = ad.patient_id"+
				    "     LEFT join m_unit_master um ON ad.unit_id = um.unit_id" + 
					"     LEFT JOIN m_prefix_master pm ON pim.prefix_id = pm.prefix_id" + 
					"     LEFT JOIN m_gender_master gm ON pim.gender_id = gm.gender_id" + 
					"     LEFT JOIN patient.t_patient_details patDet ON pim.patient_id = patDet.patient_id " + 
					"     LEFT JOIN m_organization_master om ON ad.organization_id = om.organization_id " + 
					"     LEFT JOIN doctor.m_doctor_master dm ON dm.doctor_id = ad.doctor_id " + 
					"     LEFT JOIN m_user_master udm ON ad.created_by = udm.user_id " + 
					"     LEFT JOIN adt.t_admission_details adet ON ad.admission_id = adet.admission_id " + 
					"     LEFT JOIN adt.m_ward_master wm ON adet.ward_id = wm.ward_id " + 
					"     LEFT JOIN adt.m_bed_master bm ON adet.bed_id = bm.bed_id " + 
					"  WHERE ad.admission_id=:admissionId and adet.status='A' AND ad.status='A' ";
				newDto = (DischargeSummaryReportDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", dto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryReportDto.class)).uniqueResult();
			}catch (HibernateException he) {
			} catch (Exception e) {
				
			}
		return newDto;
	}

	private PatientConsentMapperDto getPatientConsentDetails(PatientConsentMapperDto dto) {
		PatientConsentMapperDto finalDto = null;
		try {
			String query = "select template_desc as \"templateDesc\" " 
				    + "FROM public.t_patient_consent_mapper "  
					+ "where consent_transaction_id =:consentTransactionId ";
					
			finalDto = (PatientConsentMapperDto) sessionFactory.getCurrentSession()
						.createSQLQuery(query)
					.setParameter("consentTransactionId", dto.getConsentTransactionId())
					.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).uniqueResult();
				return finalDto;
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response getConsentHtmlString(Integer consentTransactionId) throws ApplicationException {
		PatientConsentMapperDto finalDto = null;
		try {
			String query = ("select html_template_data as \"htmlTemplateData\","
					+ "template_form_url as \"templateFormUrl\" " 
				    + "FROM public.t_patient_consent_mapper "  
					+ "where consent_transaction_id =:consentTransactionId ");
					
						finalDto = (PatientConsentMapperDto) sessionFactory.getCurrentSession()
						.createSQLQuery(query)
								.setParameter("consentTransactionId", consentTransactionId)
								.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).uniqueResult();
					
					  return new Response(SUCCESS, SUCCESS_CODE, null, null, finalDto);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;	}

}
