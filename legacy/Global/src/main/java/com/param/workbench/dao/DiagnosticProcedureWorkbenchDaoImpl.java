package com.param.workbench.dao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.pdf.PdfWriter;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.radiology.model.DiagnosticProcedureMapper;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DiagnosticProcedureWorkbenchDaoImpl implements IDiagnosticProcedureWorkbenchDao,ICommonConstants {
	final static Logger logger = Logger.getLogger(DiagnosticProcedureWorkbenchDaoImpl.class);


	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	SessionFactory sessionFactory;
	@Override
	public Response getProcedureworkbench(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DIAGNOSIS_PROCEDURE_WORKLIST").getQueryString());			
			
			query.append( " AND orderdetails.service_rendered = 0 ");
			// search filter start
			if (radiologyOrdersListDto.getFromDate() != null) {
				query.append(" AND to_char( ordermaster.order_date, 'yyyy-MM-dd HH24:MI:SS') BETWEEN :fromDate AND :toDate ");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId ");
			}

			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				query.append(" AND orderdetails.service_id = :serviceId ");
			}

			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0){
				query.append(" and ordermaster.visit_type_id IN :visitTypeList ");
			}
			if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0){
				query.append(" and modalitymstr.modality_id IN :modalityList ");
			}
			
			Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size()>0) {
				finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}
			if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size()>0) {
				finalQuery.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
			}
		 finalQuery.setParameter("unitId", radiologyOrdersListDto.getUnitId())
			.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());

			if (radiologyOrdersListDto.getFromDate() != null) {
				finalQuery.setParameter("fromDate", radiologyOrdersListDto.getFromDate().substring(0, 10) + " 00:00:00");
				finalQuery.setParameter("toDate", radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00");
			}

			// Set parameters for patient ID
			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());
			}

			// Set parameters for service ID
			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				finalQuery.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
			}

			// Set parameter list for visit types
			if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
				finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}

			// Set parameter list for modality types
			if (radiologyOrdersListDto.getModalityList() != null && !radiologyOrdersListDto.getModalityList().isEmpty()) {
				finalQuery.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
			}


			List<RadiologyOrdersListDto> dtoList =finalQuery.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he) {
		logger.error("getProcedureworkbench failed", he);
		}catch(Exception e) {
		logger.error("getProcedureworkbench failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response saveDiagnosticProcedureMapper(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			   DiagnosticProcedureMapper  mapper = new  DiagnosticProcedureMapper();
			    mapper.setEncounterId(radiologyOrdersListDto.getEncounterId());
			    mapper.setAdmissionId(radiologyOrdersListDto.getAdmissionId());
				mapper.setOrderDetailsId(radiologyOrdersListDto.getOrderDetailsId());
				mapper.setCreatedBy(radiologyOrdersListDto.getUserId());
				mapper.setUpdatedBy(radiologyOrdersListDto.getUserId());
				mapper.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				mapper.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				mapper.setReasonId(radiologyOrdersListDto.getReasonId());
				mapper.setStatus('A');
				mapper.setIsCancel(radiologyOrdersListDto.getIsCritical()); // is Critical flag used here as Iscancel
				mapper.setUnitId(radiologyOrdersListDto.getUnitId());
				mapper.setOrganizationId(radiologyOrdersListDto.getOrganizationId());
				sessionFactory.getCurrentSession().save(mapper);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he) {
		logger.error("saveDiagnosticProcedureMapper failed", he);
		}catch(Exception e) {
		logger.error("saveDiagnosticProcedureMapper failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getProcedureResultEntry(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DIAGNOSIS_PROCEDURE_WORKLIST").getQueryString());		
			
					query.append( " AND orderdetails.service_rendered = 1 ");
					query.append( " and ( templatetxn.is_provisional_release is null or templatetxn.is_provisional_release='N' ) ");
			// search filter start
			if (radiologyOrdersListDto.getFromDate() != null) {
				query.append(" AND to_char( ordermaster.order_date, 'yyyy-MM-dd HH24:MI:SS') BETWEEN :fromDate AND :toDate ");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId ");
			}

			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				query.append(" AND orderdetails.service_id = :serviceId ");
			}
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0){
				query.append(" and ordermaster.visit_type_id IN :visitTypeList ");
			}
			if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0){
				query.append(" and modalitymstr.modality_id IN :modalityList ");
			}
			
			Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size()>0) {
				finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}
			if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size()>0) {
				finalQuery.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
			}
			if (radiologyOrdersListDto.getFromDate() != null) {
				finalQuery.setParameter("fromDate", radiologyOrdersListDto.getFromDate().substring(0, 10) + " 00:00:00");
				finalQuery.setParameter("toDate", radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());
			}

			// Set parameters for service ID
			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				finalQuery.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
			}
		 finalQuery
			.setParameter("unitId", radiologyOrdersListDto.getUnitId())
			.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());



			List<RadiologyOrdersListDto> dtoList =finalQuery.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he) {
		logger.error("getProcedureResultEntry failed", he);
		}catch(Exception e) {
		logger.error("getProcedureResultEntry failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getTemplateByTemplateTypeId(TemplateReqDto templateReqDto) throws ApplicationException {
		try {
			String templateQuery = "select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\", "
					+ " tmp.template_format_id as \"templateFormatId\" "
					+ "from "
					+ "	public.t_template_master tmp "
					+ "where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.doctor_id is null "
					+ "	and tmp.speciality_id is null "
					+ "	and tmp.sub_speciality_id is null "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id IN :templateTypeId "
					+ "union "
					+ "select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\", "
					+ " tmp.template_format_id as \"templateFormatId\" "
					+ "from "
					+ "	public.t_template_master tmp "
					+ "inner join t_unit_sub_speciality_mapper subM on "
					+ "	tmp.sub_speciality_id = subM.sub_speciality_id "
					+ "	and subM.organization_id =:orgId "
					+ "	and subM.unit_id = :unitId "
					+ "where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id IN :templateTypeId "
					+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
					+ "	and tmp.doctor_id is null "
					+ "union "
					+ "select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\", "
					+ " tmp.template_format_id as \"templateFormatId\" "
					+ "from "
					+ "	public.t_template_master tmp "
					+ "where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id IN :templateTypeId "
					+ "	and tmp.doctor_id = "+templateReqDto.getDoctorId()
					+ "	and tmp.speciality_id is null "
					+ "	and tmp.sub_speciality_id is null "
					+ "union "
					+ "select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\", "
					+ " tmp.template_format_id as \"templateFormatId\" "
					+ "from "
					+ "	public.t_template_master tmp "
					+ "where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id IN :templateTypeId "
					+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
					+ "	and tmp.doctor_id is null "
					+ "union "
					+ "select "
					+ "	tmp.template_id as \"templateId\", "
					+ "	tmp.title as \"title\", "
					+ "	tmp.speciality_id as \"specialityId\", "
					+ "	tmp.sub_speciality_id as \"subSpecialityId\", "
					+ "	tmp.template_object as \"templateObject\", "
					+ "	tmp.status as \"status\", "
					+ "	tmp.doctor_id as \"doctorId\", "
					+ "	tmp.template_type_id as \"templateTypeId\", "
					+ " tmp.template_format_id as \"templateFormatId\" "
					+ "from "
					+ "	public.t_template_master tmp "
					+ "where "
					+ "	tmp.status = 'A' "
					+ "	and tmp.org_id = :orgId "
					+ "	and tmp.unit_id = :unitId "
					+ "	and tmp.template_type_id IN :templateTypeId "
					+ "	and tmp.speciality_id = "+templateReqDto.getSpecialityId()	
					+ "	and tmp.doctor_id is null "
					+ "	and tmp.sub_speciality_id is null";
					
					List<TemplateMasterDto> listTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(templateQuery)
							.setParameter("orgId", templateReqDto.getOrgId())
							.setParameter("unitId", templateReqDto.getUnitId())
//							.setParameter("templateTypeId", templateReqDto.getTemplateTypeId())
							.setParameterList("templateTypeId", templateReqDto.getTemplateTypeIds())
							.setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();
					return new Response<>(SUCCESS, SUCCESS_CODE, null, listTemplateMasterDto, null);
					
					
				}catch(Exception e){
					
					throw e;
				}
	}


	@Override
	public Response getProcedureResultValidation(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DIAGNOSIS_PROCEDURE_WORKLIST").getQueryString());		
			
					query.append( " AND orderdetails.service_rendered = 1 ");
					query.append( " and templatetxn.template_transaction_id is not null and templatetxn.is_provisional_release='Y' and ( templatetxn.is_final_release is null or templatetxn.is_final_release='N') ");
			
			// search filter start
			if (radiologyOrdersListDto.getFromDate() != null) {
				query.append(" AND to_char( ordermaster.order_date, 'yyyy-MM-dd HH24:MI:SS') BETWEEN :fromDate AND :toDate ");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId ");
			}

			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				query.append(" AND orderdetails.service_id = :serviceId ");
			}
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0){
				query.append(" and ordermaster.visit_type_id IN :visitTypeList ");
			}
			if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0){
				query.append(" and modalitymstr.modality_id IN :modalityList ");
			}
			
			Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size()>0) {
				finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}
			if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size()>0) {
				finalQuery.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
			}

			if (radiologyOrdersListDto.getFromDate() != null) {
				finalQuery.setParameter("fromDate", radiologyOrdersListDto.getFromDate().substring(0, 10) + " 00:00:00");
				finalQuery.setParameter("toDate", radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());
			}

			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				finalQuery.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
			}

		finalQuery
			.setParameter("unitId", radiologyOrdersListDto.getUnitId())
			.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
			List<RadiologyOrdersListDto> dtoList = finalQuery.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he) {
		logger.error("getProcedureResultValidation failed", he);
		}catch(Exception e) {
		logger.error("getProcedureResultValidation failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getProcedureWorkListForPrinting(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DIAGNOSIS_PROCEDURE_WORKLIST").getQueryString());		
			
					query.append( " AND orderdetails.service_rendered = 1 ");
					query.append( " and templatetxn.template_transaction_id is not null and templatetxn.is_final_release='Y' ");
			
			// search filter start
			if (radiologyOrdersListDto.getFromDate() != null) {
				query.append(" AND to_char( ordermaster.order_date, 'yyyy-MM-dd HH24:MI:SS') BETWEEN :fromDate AND :toDate ");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId ");
			}

			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0){
				query.append(" and ordermaster.visit_type_id IN :visitTypeList ");
			}
			if(radiologyOrdersListDto.getDeptList()!=null && radiologyOrdersListDto.getDeptList().size() > 0){
				query.append(" and orderdetails.service_rendering_deptid IN :deptList ");
			}
			if(radiologyOrdersListDto.getServiceList()!=null && radiologyOrdersListDto.getServiceList().size() > 0){
				query.append(" and orderdetails.service_id IN :serviceList ");
			}
			
			
			Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());

			if (radiologyOrdersListDto.getFromDate() != null) {
				finalQuery.setParameter("fromDate", radiologyOrdersListDto.getFromDate().substring(0, 10) + " 00:00:00");
				finalQuery.setParameter("toDate", radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());
			}
			if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size()>0) {
				finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}
			if(radiologyOrdersListDto.getDeptList()!=null && radiologyOrdersListDto.getDeptList().size()>0) {
				finalQuery.setParameterList("deptList", radiologyOrdersListDto.getDeptList());
			}
			if(radiologyOrdersListDto.getServiceList()!=null && radiologyOrdersListDto.getServiceList().size()>0) {
				finalQuery.setParameterList("serviceList", radiologyOrdersListDto.getServiceList());
			}

		 finalQuery
			.setParameter("unitId", radiologyOrdersListDto.getUnitId())
			.setParameter("orgId", radiologyOrdersListDto.getOrganizationId());
			List<RadiologyOrdersListDto> dtoList =finalQuery.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he) {
		logger.error("getProcedureWorkListForPrinting failed", he);
		}catch(Exception e) {
		logger.error("getProcedureWorkListForPrinting failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response printDiagnosisReport(RadiologyOrdersListDto dto) throws ApplicationException {
		try{
			
			PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
			
			RadiologyOrdersListDto header = (RadiologyOrdersListDto)sessionFactory.getCurrentSession().createSQLQuery(
				"select diagnosis_header as \"header\", footer as \"footer\" , signture_img as \"signtureImg\" , latter_head_img as \"latterHeadImg\","
			    + " footer_img as \"footerImg\", mail_footer as \"mailFooter\", diagnosis_header_without_img as \"headerWithoutImg\" "
				+ "from  public.m_report_header where organization_id = "+dto.getOrganizationId()
				+ " and unit_id = " + dto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).uniqueResult();
			
			   ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
			   PdfWriter pdfWriter = null;
			   String referedName = "     -";
			   if(dto.getReferralName()!= null) {
				   referedName = dto.getReferralName();
			   }
			   
			   String latter = "<br><br><br><br>";
			   String body = dto.getTemplateContent()+"<br>";
			   //body = body.replace("<li>","<li>&bull;   ");
			   
			   Map<String,String> qMapResultsHearder = new HashMap<String, String>();
			   Map<String,String> qMapResultsHearderWithoutImg = new HashMap<String, String>();
			   Map<String,String> qMapResultsFirst=null;
			   Map<String,String> qMapResultsFooter = null;
			   String reportStatus = "Initiate";
	           if(dto.getIsProvisionalRelease() == null && dto.getIsFinalRelease()== null){
	               reportStatus = "Initiate";
	           }else if((dto.getIsProvisionalRelease() == 'N' || dto.getIsProvisionalRelease() == null) && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N') ){
	               reportStatus = "Initiate";
	           }else if(dto.getIsProvisionalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N')){
	               reportStatus = "Provisional";
	           }else if(dto.getIsFinalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N') ){
	               reportStatus = "Final";
	           }else if(dto.getIsFinalRelease() == 'Y' && dto.getIsProvisionalRelease()== 'Y') {
	               reportStatus = "Final";
	           }	   	
			    String   patientHeader = header.getHeader();
			    if(patientHeader == null) {
			    	patientHeader = "";
			    }else {
				   
			    	
			    	patientHeader = patientHeader.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
	            	qMapResultsHearder.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
	            patientHeader = patientHeader.replace("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate() );
	            	qMapResultsHearder.put("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate());
	            patientHeader = patientHeader.replace("@uhid",dto.getUhidNumber());
	            	qMapResultsHearder.put("@uhid", dto.getUhidNumber());
	            patientHeader = patientHeader.replace("@releaseDate",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
	            	qMapResultsHearder.put("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
	            patientHeader = patientHeader.replace("@age",dto.getAgeString());
	            	qMapResultsHearder.put("@age", dto.getAgeString());
	            patientHeader = patientHeader.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
	            	qMapResultsHearder.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
	            patientHeader = patientHeader.replace("@referral",referedName);
	            	qMapResultsHearder.put("@referral",referedName);
	            patientHeader = patientHeader.replace("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo() : dto.getVisitNumber());
			    	qMapResultsHearder.put("@visitNo",dto.getVisitNo());
			    patientHeader = patientHeader.replace("@orderNo",dto.getOrderNo());
			    	qMapResultsHearder.put("@orderNo",dto.getOrderNo());
			    patientHeader = patientHeader.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
			        	qMapResultsHearder.put("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
		
			    patientHeader = patientHeader.replace("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
					   	qMapResultsHearder.put("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
					   	
			    patientHeader = patientHeader.replace("@reportStatus",reportStatus);
			        	qMapResultsHearder.put("@reportStatus",reportStatus);
			    patientHeader = patientHeader.replace("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
				        	qMapResultsHearder.put("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");	   	
				    	
				        	
				 if(patientHeader.contains("@templateDoctorName")) {
				patientHeader = patientHeader.replace("@templateDoctorName",dto.getTemplateDoctorName() != null ?dto.getTemplateDoctorName(): "-");
				    qMapResultsHearder.put("@templateDoctorName",dto.getTemplateDoctorName() != null ?dto.getTemplateDoctorName(): "-");	
				 } 
				 if(patientHeader.contains("@serviceAmt")) {
					 patientHeader = patientHeader.replace("@serviceAmt",dto.getServiceAmt() != null ?dto.getServiceAmt().toString(): "-");
					 qMapResultsHearder.put("@serviceAmt",dto.getServiceAmt() != null ?dto.getServiceAmt().toString(): "-");	
			 		} 
				 
				 if(patientHeader.contains("@orderDateTime")) {
					 patientHeader = patientHeader.replace("@orderDateTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");
					 qMapResultsHearder.put("@orderDateTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");	
			 		} 
				    Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(patientHeader);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{	
							    consentMapperDto.setPatientId(dto.getPatientId());
							    consentMapperDto.setEncounterId(dto.getEncounterId()!=null ? dto.getEncounterId() : null);
							    consentMapperDto.setAdmissionId(dto.getAdmissionId()!=null ? dto.getAdmissionId(): null);
							    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
							    consentMapperDto.setTemplateDesc(patientHeader);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFirst = (Map<String, String>) res.getResObject();
							for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
							{
								qMapResultsHearder.put(itr.getKey(), itr.getValue());
							}
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							patientHeader = consentMapperDto.getTemplateDesc();
						}
					}
			    }
			   
			    // patient header without letter img
			    
			    String   patientHeaderWithoutImg = header.getHeaderWithoutImg();
			    if(patientHeaderWithoutImg == null) {
			    	patientHeaderWithoutImg = "";
			    }else {
				   
			    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
			    		qMapResultsHearderWithoutImg.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
			    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate() );
	            		qMapResultsHearderWithoutImg.put("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate());
	            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@uhid",dto.getUhidNumber());
	            		qMapResultsHearderWithoutImg.put("@uhid", dto.getUhidNumber());
	            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
	            		qMapResultsHearderWithoutImg.put("@releaseDate",  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
	            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@age",dto.getAgeString());
	            		qMapResultsHearderWithoutImg.put("@age", dto.getAgeString());
	            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
	            		qMapResultsHearderWithoutImg.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
	            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@referral",referedName);
	            		qMapResultsHearderWithoutImg.put("@referral",referedName);
	            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo() : dto.getVisitNumber());
	            		qMapResultsHearderWithoutImg.put("@visitNo",dto.getVisitNo());
	            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@orderNo",dto.getOrderNo());
			    		qMapResultsHearderWithoutImg.put("@orderNo",dto.getOrderNo());
			    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
			    		qMapResultsHearderWithoutImg.put("@printTime", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
		
			    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
			    		qMapResultsHearderWithoutImg.put("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
	                	   	
			    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@reportStatus",reportStatus);
			    		qMapResultsHearderWithoutImg.put("@reportStatus",reportStatus);
			    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
			    		qMapResultsHearderWithoutImg.put("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");	   	
				    
			    	if(patientHeaderWithoutImg.contains("@templateDoctorName")) {
			    		patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@templateDoctorName",dto.getTemplateDoctorName() != null ?dto.getTemplateDoctorName(): "-");
			    		qMapResultsHearderWithoutImg.put("@templateDoctorName",dto.getTemplateDoctorName() != null ?dto.getTemplateDoctorName(): "-");	
			 		} 
			    	
			    	if(patientHeaderWithoutImg.contains("@serviceAmt")) {
			    		patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@serviceAmt",dto.getServiceAmt() != null ?dto.getServiceAmt().toString(): "-");
			    		qMapResultsHearderWithoutImg.put("@serviceAmt",dto.getServiceAmt() != null ?dto.getServiceAmt().toString(): "-");	
			 		} 
			    	
			    	if(patientHeaderWithoutImg.contains("@orderDateTime")) {
			    		patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@orderDateTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");
			    		qMapResultsHearderWithoutImg.put("@orderDateTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");	
			 		} 
			    	
			    	
			    	
				    Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(patientHeaderWithoutImg);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{	
							    consentMapperDto.setPatientId(dto.getPatientId());
							    consentMapperDto.setEncounterId(dto.getEncounterId()!=null ? dto.getEncounterId() : null);
							    consentMapperDto.setAdmissionId(dto.getAdmissionId()!=null ? dto.getAdmissionId(): null);
							    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
							    consentMapperDto.setTemplateDesc(patientHeaderWithoutImg);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFirst = (Map<String, String>) res.getResObject();
							for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
							{
								qMapResultsHearderWithoutImg.put(itr.getKey(), itr.getValue());
							}
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							patientHeaderWithoutImg = consentMapperDto.getTemplateDesc();
						}
					}
			    }
			    
			    String footer = header.getFooter();
			    if(footer == null) {
			    	footer = "";
			    }else {
			    	footer = footer.replace("@signatureImg",header.getSigntureImg());
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(footer);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{
							Integer count = 0;
							for(String q : paramNames)
							{
								if (qMapResultsHearder.containsKey(q))
								{
									footer = footer.replace(q, qMapResultsHearder.get(q));
								}else{
									count++;
								}
							}
							if(count > 0)
							{
								consentMapperDto.setPatientId(dto.getPatientId());
							    consentMapperDto.setEncounterId(dto.getEncounterId());
							    consentMapperDto.setAdmissionId(dto.getAdmissionId());
							    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
								consentMapperDto.setTemplateDesc(footer);
								Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
								qMapResultsFooter = (Map<String, String>) res.getResObject(); 
								consentMapperDto = (PatientConsentMapperDto) res.getObject();
								footer = consentMapperDto.getTemplateDesc();
								
								for(Map.Entry<String,String> itr : qMapResultsFooter.entrySet())
								{
									qMapResultsHearder.put(itr.getKey(), itr.getValue());
								}
							}
							
						}
					}	
			    }
			    
			    String mailFooter = header.getMailFooter();
			    if(mailFooter == null) {
			    	mailFooter = "";
			    }else {
			    	mailFooter = mailFooter.replace("@signatureImg",header.getSigntureImg());
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(mailFooter);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{
							Integer count = 0;
							for(String q : paramNames)
							{
								if (qMapResultsHearder.containsKey(q))
								{
									mailFooter = mailFooter.replace(q, qMapResultsHearder.get(q));
								}else{
									count++;
								}
							}
							if(count > 0)
							{
								consentMapperDto.setPatientId(dto.getPatientId());
							    consentMapperDto.setEncounterId(dto.getEncounterId());
							    consentMapperDto.setAdmissionId(dto.getAdmissionId());
							    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
								consentMapperDto.setTemplateDesc(mailFooter);
								Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
								qMapResultsFooter = (Map<String, String>) res.getResObject(); 
								consentMapperDto = (PatientConsentMapperDto) res.getObject();
								mailFooter = consentMapperDto.getTemplateDesc();
								
								for(Map.Entry<String,String> itr : qMapResultsFooter.entrySet())
								{
									qMapResultsHearder.put(itr.getKey(), itr.getValue());
								}
							}
							
						}
					}	
			    }
			    
			    
			    
			    if(dto.getTemplateContent()!=null)
			    {
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(dto.getTemplateContent());
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{
							Integer count = 0;
							String tempContent = dto.getTemplateContent();
							for(String q : paramNames)
							{
								if (qMapResultsHearder.containsKey(q))
								{
									tempContent = tempContent.replace(q, qMapResultsHearder.get(q));
								}else{
									count++;
								}
							}
							if(count > 0)
							{
									consentMapperDto.setTemplateDesc(tempContent);
								Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
								consentMapperDto = (PatientConsentMapperDto) res.getObject();
								tempContent = consentMapperDto.getTemplateDesc();
							}
							dto.setTemplateContent(tempContent);
							body = tempContent; 
						}
					}	
			    
		RadiologyOrdersListDto resDto = new RadiologyOrdersListDto();
		Integer PrintFlag = 0;
		if(dto.getPrintWithHeader().equals(PrintFlag)) {
			
			String latterHeadImg = header.getLatterHeadImg();
			if(latterHeadImg == null) {
				
				resDto.setLatterHeadImg(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="" ?header.getLatterHeadImg():latter);
				
		    }else {
		    	
		    	if(dto.getPaymentEntitlementId()!=null && dto.getPaymentEntitlementId() == SELF) {
		    		if(latterHeadImg.contains("@companyDesc")) {
		    			latterHeadImg = latterHeadImg.replace("@companyDesc","WALKIN CLIENT");
		    		}
		    		if(latterHeadImg.contains("@comAddress")) {
		    			latterHeadImg = latterHeadImg.replace("@comAddress",dto.getUnitName());
		    		}
		    	}else {
		    		if(latterHeadImg.contains("@companyDesc")) {
		    			latterHeadImg = latterHeadImg.replace("@companyDesc",dto.getCompanyDesc()!=null ? dto.getCompanyDesc():"");
		    		}
		    		if(latterHeadImg.contains("@comAddress")) {
		    			latterHeadImg = latterHeadImg.replace("@comAddress",dto.getComAddress()!=null ? dto.getComAddress() : "");
		    		}
		    	}
		    	
		    	resDto.setLatterHeadImg(latterHeadImg);
		    }
		
		
		}else {
			resDto.setLatterHeadImg(latter);
		}
		
		resDto.setHeader(patientHeader);
		resDto.setFooter(footer);
		resDto.setTemplateContent(body);
		resDto.setMailFooter(mailFooter);
		resDto.setFooterImg(header.getFooterImg()!=null ? header.getFooterImg(): null);
	    resDto.setHeaderWithoutImg(patientHeaderWithoutImg);
		     
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, resDto);
		}
		}catch (HibernateException he) {
		logger.error("printDiagnosisReport failed", he);
		} catch (Exception e) {
		logger.error("printDiagnosisReport failed", e);
		}
		return null;
		}

}
