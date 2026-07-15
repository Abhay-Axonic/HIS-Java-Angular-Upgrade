package com.param.adt.discharge.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.sync.PatientDocumentSyncDto;
import com.param.global.service.IPatientRegistrationService;
import com.param.workbench.service.IRISWorkbenchService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontFactoryImp;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.dto.DischargeSummaryReportDto;
import com.param.adt.discharge.dto.DischargeSummaryTabularTemplateDataDto;
import com.param.adt.discharge.dto.InvestigationOrderDto;
import com.param.adt.discharge.model.DischargeSummary;
import com.param.adt.discharge.model.DischargeSummaryMapper;
import com.param.billing.global.smartcard.dto.ItemDetailsResponseDto;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.LabReportReqDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.unit.dao.ILabConfigurationDao;
import com.param.global.unit.dto.LabConfigurationDto;

import in.param.common.dao.GenericDao;
import org.springframework.web.client.RestTemplate;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeSummaryDaoImpl extends GenericDao<DischargeSummary, Integer> implements IDischargeSummaryDao,ICommonConstants{
	
	public DischargeSummaryDaoImpl() {
		super(DischargeSummary.class);
	}
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
    @Autowired
    DocUploadUtility docUploadUtility;
	
    @Autowired
	private ServletContext servletContext;
    @Autowired
   	private ILabConfigurationDao iLabConfigurationDao;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IRISWorkbenchService iRISWorkbenchService;

	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;

	@Autowired
	private Gson gson;
    
	@Override
	public Response savePatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto) {
		try {
			DischargeSummary dischargeSummary=new DischargeSummary();
			dischargeSummary.setPatientId(dischargeSummaryDto.getPatientId());
			dischargeSummary.setAdmissionId(dischargeSummaryDto.getAdmissionId());
			dischargeSummary.setDischargeTemplateId(dischargeSummaryDto.getDischargeTemplateId());
			dischargeSummary.setDepartmentId(dischargeSummaryDto.getDepartmentId());
			dischargeSummary.setDoctorId(dischargeSummaryDto.getDoctorId());
			dischargeSummary.setOrganizationId(dischargeSummaryDto.getOrganizationId());
			dischargeSummary.setUnitId(dischargeSummaryDto.getUnitId());
			dischargeSummary.setStatus(dischargeSummaryDto.getStatus());
			dischargeSummary.setCreatedBy(dischargeSummaryDto.getCreatedBy());
			dischargeSummary.setCreatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummary.setUpdatedBy(dischargeSummaryDto.getUpdatedBy());
			dischargeSummary.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummary=save(dischargeSummary);
		
		return new Response(SUCCESS, SUCCESS_CODE, null, null, dischargeSummary);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeSummaryTemplateByPatientId(Integer admissionId, Integer organizationId, Integer unitId) {
		try {
			 DischargeSummaryDto dischargeSummaryDto=new DischargeSummaryDto();
			 DischargeSummaryDto dischargeSummaryDto1=(DischargeSummaryDto) sessionFactory.getCurrentSession()
	    				.createSQLQuery(
	    						"select patient.speciality_id as \"departmentId\",patient.admission_id as \"admissionId\" " 
	    						+ "FROM adt.t_admission patient "  
	    						+ "where patient.admission_id=:admissionId AND patient.organization_id=:organizationId "
	    						+ " AND patient.unit_id=:unitId "// And patient.status='A'
	    						).
					 setParameter("admissionId", admissionId)
					 .setParameter("organizationId", organizationId)
					 .setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).uniqueResult();
			 
			 DischargeSummaryDto dto2 = (DischargeSummaryDto) sessionFactory.getCurrentSession()
	    				.createSQLQuery(
	    						"select app.er_speciality_id as \"departmentId\" from t_unit_application_billing_configuration app where app.organization_id=:organizationId "
	    					  + " and app.unit_id=:unitId and app.status='A'"
	    						) .setParameter("organizationId", organizationId)
					 .setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).uniqueResult();
	    		if(dischargeSummaryDto1!=null) {
	    			if(dischargeSummaryDto1.getDepartmentId()!=null)
	    			dischargeSummaryDto.setDepartmentId(dischargeSummaryDto1.getDepartmentId()); 
	    			else
	    				dischargeSummaryDto.setDepartmentId(dto2.getDepartmentId()); 
	    			dischargeSummaryDto.setAdmissionId(dischargeSummaryDto1.getAdmissionId());
	    		}
	    		
	    		List<DischargeSummaryDto> templateList =  sessionFactory.getCurrentSession()
	    				.createSQLQuery(
	    						/*"select temp.discharge_id as \"dischargeTemplateId\" ,temp.discharge_desc as \"dischargeTemplateName\",temp.department_id as \"departmentId\" " 
	    						+ "FROM adt.m_discharge_summary_template_master temp " 
	    						+ "where temp.department_id="+ dischargeSummaryDto.getDepartmentId()+" AND temp.organization_id="+organizationId+" "
	    						+ " AND temp.unit_id="+unitId +" AND temp.status='A' "*/
								"SELECT " +
										"   temp.discharge_id AS \"dischargeTemplateId\", " +
										"   temp.discharge_desc AS \"dischargeTemplateName\", " +
										"   mppr.department_id AS \"departmentId\", " +
										"   temp.is_html AS \"isHtml\" " +
										"FROM " +
										"   adt.m_discharge_summary_template_master temp " +
										"INNER JOIN adt.t_discharge_template_department_mapper mppr " +
										"   ON mppr.discharge_summary_temp_id = temp.discharge_id " +
										"WHERE " +
										"   mppr.department_id = :departmentId " +
										"   AND temp.organization_id = :organizationId " +
										"   AND temp.unit_id = :unitId " +
										"   AND temp.status = 'A'")
						.setParameter("departmentId", dischargeSummaryDto.getDepartmentId())
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();

	    		List<DischargeSummaryDto> list=new ArrayList<>();
	    		for(DischargeSummaryDto l:templateList)
	    		{
	    			DischargeSummaryDto d2=new DischargeSummaryDto();
	    			d2.setAdmissionId(dischargeSummaryDto.getAdmissionId());
	    			d2.setDischargeTemplateId(l.getDischargeTemplateId());
	    			d2.setDischargeTemplateName(l.getDischargeTemplateName());
	    			d2.setDepartmentId(l.getDepartmentId());
	    			d2.setIsHtml(l.getIsHtml());
	    			list.add(d2);
	    		}

		  return new Response(SUCCESS, SUCCESS_CODE,null, list, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientDischargeSummaryList(DischargeSummaryDto dischargeSummaryDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getFieldTemplateSummaryByTemplateId(Integer templateId, Integer organizationId, Integer unitId) {
		try {
		Character status='A';
    		List<DischargeSummaryDto> templateFieldList =  sessionFactory.getCurrentSession()
    				.createSQLQuery(
    						"select fieldmaster.discharge_field_id as \"dischargeFieldId\" ,field1.discharge_changed_name as \"dischargeFieldName\","
    						+ " fieldMaster.discharge_field_template_obj as \"dischargeFieldDescription\", " 
    						+ "temp.discharge_id as \"dischargeTemplateId\" " 
    						+ "FROM adt.m_discharge_summary_field_master fieldmaster "
    						+ "inner join adt.t_discharge_summary_template_mapper tempmapper "
    						+ "on fieldmaster.discharge_field_id=tempmapper.discharge_field_id "
							+ " inner join public.m_discharge_field_unit_wise field1 on "
							+ " tempmapper.discharge_field_id = field1.discharge_field_id "
    						+ "inner join adt.m_discharge_summary_template_master temp "
    						+ "on temp.discharge_id=tempmapper.discharge_summary_temp_id "
    						+ "where tempmapper.discharge_summary_temp_id=:templateId "
							+" AND tempmapper.organization_id=:organizationId "
    						+ " AND tempmapper.unit_id=:unitId "
    						+ " AND tempmapper.status=:status "
							+ " and field1.organization_id =:organizationId "
							+ " and field1.unit_id =:unitId "
							+ " and field1.status = 'A'"
    						+ " order by tempmapper.discharge_summary_temp_mppr_id asc"
    						)
					.setParameter("templateId", templateId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("status", status)
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
	  return new Response(SUCCESS, SUCCESS_CODE,null, templateFieldList, null);
	} catch (HibernateException he) {
			he.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDischargeSummaryDetails(DischargeSummaryDto dto) {
		try {
			
			DischargeSummaryMapper dischargeSummaryMapper = (DischargeSummaryMapper)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_DETAIL_BY_DISCHARGE_SUMMARY_ID_AND_FIELD_ID")
					.setParameter("dischargeSummaryId", dto.getDischargeSummaryId())
					.setParameter("dischargeFieldId",dto.getDischargeFieldId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryMapper.class)).uniqueResult();
			if(dischargeSummaryMapper!=null) {
				dischargeSummaryMapper.setDischargeFieldId(dto.getDischargeFieldId());
				dischargeSummaryMapper.setDischargeFieldDescription(dto.getDischargeFieldDescription());
				dischargeSummaryMapper.setDischargeSummaryId(dto.getDischargeSummaryId());
				dischargeSummaryMapper.setOrganizationId(dto.getOrganizationId());
				dischargeSummaryMapper.setUnitId(dto.getUnitId());
				dischargeSummaryMapper.setStatus(dto.getStatus());
				dischargeSummaryMapper.setUpdatedBy(dto.getUpdatedBy());
				dischargeSummaryMapper.setCreatedBy(dischargeSummaryMapper.getCreatedBy());
				dischargeSummaryMapper.setCreatedDate(dischargeSummaryMapper.getCreatedDate());
				dischargeSummaryMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				dischargeSummaryMapper.setDischargeSummaryDataViewOptionId(dto.getDischargeSummaryDataViewOptionId());
				sessionFactory.getCurrentSession().update(dischargeSummaryMapper);
				dto.setDischargeSummaryDetailId(dischargeSummaryMapper.getDischargeSummaryDetailId());
			}
			else {
			DischargeSummaryMapper dischargeSummaryMapper1=new DischargeSummaryMapper();
			dischargeSummaryMapper1.setDischargeFieldId(dto.getDischargeFieldId());
			dischargeSummaryMapper1.setDischargeFieldDescription(dto.getDischargeFieldDescription());
			dischargeSummaryMapper1.setDischargeSummaryId(dto.getDischargeSummaryId());
			dischargeSummaryMapper1.setOrganizationId(dto.getOrganizationId());
			dischargeSummaryMapper1.setUnitId(dto.getUnitId());
			dischargeSummaryMapper1.setStatus(dto.getStatus());
			dischargeSummaryMapper1.setCreatedBy(dto.getCreatedBy());
			dischargeSummaryMapper1.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryMapper1.setUpdatedBy(dto.getUpdatedBy());
			dischargeSummaryMapper1.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryMapper1.setDischargeSummaryDataViewOptionId(dto.getDischargeSummaryDataViewOptionId());
			sessionFactory.getCurrentSession().saveOrUpdate(dischargeSummaryMapper1);
			dto.setDischargeSummaryDetailId(dischargeSummaryMapper1.getDischargeSummaryDetailId());
			}
			  return new Response(SUCCESS, SUCCESS_CODE,null, null, dto);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientDischargeSummaryFieldList(DischargeSummaryDto dischargeSummaryDto) {
		try {
			DischargeSummaryDto dischargeSummaryFieldDto=new DischargeSummaryDto();
			
			 DischargeSummaryDto dischargeSummaryDto1 = (DischargeSummaryDto) sessionFactory.getCurrentSession()
					 .createSQLQuery(
							 "SELECT dis.discharge_summary_id AS \"dischargeSummaryId\" " +
									 "FROM adt.m_discharge_summary dis " +
									 "WHERE dis.admission_id = :admissionId " +
									 "AND dis.organization_id = :organizationId " +
									 "AND dis.unit_id = :unitId " +
									 "AND dis.discharge_template_id = :dischargeTemplateId " +
									 "AND dis.status = 'A'")
					 .setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
					 .setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
					 .setParameter("unitId", dischargeSummaryDto.getUnitId())
					 .setParameter("dischargeTemplateId", dischargeSummaryDto.getDischargeTemplateId())
					 .setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class))
					 .uniqueResult();
	    		if(dischargeSummaryDto1!=null) {
	    			dischargeSummaryFieldDto.setDischargeSummaryId(dischargeSummaryDto1.getDischargeSummaryId());
	    		}
			List<DischargeSummaryDto> dischargeSummaryFieldList = new ArrayList<>();
			if(dischargeSummaryFieldDto.getDischargeSummaryId() != null) {
				 dischargeSummaryFieldList = sessionFactory.getCurrentSession()
							.createSQLQuery(
									"select disSumm.discharge_summary_id as \"dischargeSummaryId\",fieldMaster.discharge_field_id as \"dischargeFieldId\" ,field1.discharge_changed_name as \"dischargeFieldName\","
											+ "(select td.discharge_id from adt.t_discharge td where td.admission_id=disSumm.admission_id limit 1) as \"dischargeId\", "
											+ " disDetail.discharge_field_description as \"dischargeFieldDescription\", "
											+ " disDetail.discharge_summary_data_view_option_id as \"dischargeSummaryDataViewOptionId\","
											+ " disSumm.discharge_template_id as \"dischargeTemplateId\",disSummTemp.discharge_desc as \"dischargeTemplateName\",disSumm.is_report_release as \"isDischargeSummaryRelease\"  "
											+ " FROM adt.m_discharge_summary_field_master fieldMaster "
											+ " inner join adt.m_discharge_summary_detail disDetail "
											+ " on disDetail.discharge_field_id=fieldMaster.discharge_field_id "
											+ " inner join adt.m_discharge_summary disSumm "
											+ " on disDetail.discharge_summary_id=disSumm.discharge_summary_id "
											+ " inner join public.m_discharge_field_unit_wise field1 on "
											+ " fieldMaster.discharge_field_id  = field1.discharge_field_id "
											+ " left join adt.m_discharge_summary_template_master disSummTemp "
											+ " on disSummTemp.discharge_id=disSumm.discharge_template_id "
											+ " where disDetail.discharge_summary_id=:dischargeSummaryId "
											+ " AND disDetail.organization_id=:organizationId "
											+ " AND disDetail.unit_id=:unitId "
											+ " AND field1.organization_id =:organizationId "
											+ " AND field1.unit_id =:unitId "
											+ " AND field1.status = 'A' "
							)
							.setParameter("dischargeSummaryId", dischargeSummaryFieldDto.getDischargeSummaryId())
							.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
							.setParameter("unitId", dischargeSummaryDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
				}
	    		
	    		if(dischargeSummaryFieldList.size()>0) {
				for(DischargeSummaryDto dto : dischargeSummaryFieldList){
				List<DischargeSummaryTabularTemplateDataDto> dischargeSummaryTemplateListdata = sessionFactory.getCurrentSession().createSQLQuery(
						"  select template_transaction.discharge_summary_tabular_template_id as  \"dischargeSummaryTabularTemplateId\",       " + 
						"  template_transaction.updated_by as \"updatedBy\" ,       " + 
						"  template_transaction.created_by as \"createdBy\" ,       " + 
						"  template_transaction.organization_id as \"organizationId\" ,       " + 
						"  template_transaction.unit_id as \"unitId\" ,       " + 
						"  template_transaction.table_type_id  as \"tableTypeId\",      " + 
						"  template_transaction.discharge_template_id as \"dischargeTemplateId\",      " + 
						"  template_transaction.patient_id as \"patientId\",      " + 
						"  template_transaction.doctor_id as \"doctorId\",      " + 
						"  template_transaction.admission_id as \"admissionId\",      " + 
						"  template_transaction.column_name as \"columnName\",      " + 
						"  template_transaction.coulmn_id as \"coulmnId\",      " + 
						"  template_transaction.row_id as \"rowId\",      " + 
						"  template_transaction.row_name as \"rowName\",      " + 
						"  template_transaction.input_value as \"inputValue\",      " + 
						"  template_transaction.number_of_coulmn as \"numberOfCoulmn\",      " + 
						"  template_transaction.number_of_row as \"numberOfRow\",      "+
						"  template_transaction.table_name as \"tableName\", " + 
						"  template_transaction.department_id as \"departmentId\"    " + 
						"  from adt.m_discharge_summary_tabular_template_data template_transaction       " + 
						"   where       " + 
						"  template_transaction.organization_id =:organizationId " +
						"  and template_transaction.unit_id =:unitId " +
						"  and template_transaction.discharge_template_id =:dischargeTemplateId " +
						"  and template_transaction.admission_id =:admissionId "+
						"  and template_transaction.discharge_field_id =:dischargeFieldId " +
						"  and template_transaction.status = 'A'")
						.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryDto.getUnitId())
						.setParameter("dischargeTemplateId", dischargeSummaryDto.getDischargeTemplateId())
						.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
						.setParameter("dischargeFieldId", dto.getDischargeFieldId())
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTabularTemplateDataDto.class)).list();
				
				dto.setDischargeSummaryTemplateListdata(dischargeSummaryTemplateListdata);
				}
	    		}
	    		
	    		if(dischargeSummaryFieldList.size()>0) {
		         return new Response(SUCCESS, SUCCESS_CODE,null, dischargeSummaryFieldList, null);
	    		}
	    		else
	    		{
	    			 return new Response(ERROR, SUCCESS_CODE,null, dischargeSummaryFieldList, null);
	    		}
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response findByPatientIdTemplateId(DischargeSummaryDto dischargeSummaryDto) {
		try{
			DischargeSummary dischargeSummary=(DischargeSummary)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_BY_PATIENTID_AND_TEMPLATEID_ID")
					.setParameter("dischargeTemplateId", dischargeSummaryDto.getDischargeTemplateId())
					.setParameter("admissionId",dischargeSummaryDto.getAdmissionId())
					.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummary.class)).uniqueResult();
         if(dischargeSummary!=null) {
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dischargeSummary);
         }
         else
         {
        	 return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null); 
         }
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmissionReason(DischargeSummaryDto dischargeSummaryDto) {
		try{
		List<DischargeSummaryDto> admissionReasonList =  sessionFactory.getCurrentSession()
				.createSQLQuery(
						"select reas.reason_desc as \"admissionReason\",reas.reason_master_id as \"reasonId\" from adt.t_admission addm" 
					    + " inner join adt.m_reason_master reas on reas.reason_master_id=addm.reason_id "  
						+ "where addm.admission_id=:admissionId "
						+"AND addm.organization_id=L:organizationId "
						+ " AND addm.unit_id=:unitId "
						)
				.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
				.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
				.setParameter("unitId", dischargeSummaryDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE,null, admissionReasonList, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeSummaryReportStatus(DischargeSummaryDto dischargeSummaryDto) {
		try{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			byteStream = this.createDischargePDF(dischargeSummaryDto);
			Response unitRes = iGlobalOrganisationMasterDao
					.getOrganiazationFolderPath(dischargeSummaryDto.getOrganizationId());
			OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();
			String path = docUploadUtility.uploadDischargeSummaryReport(byteStream, organizationMasterDto,
					dischargeSummaryDto.getUnitId(), dischargeSummaryDto.getDischargeSummaryId());
			Integer count = sessionFactory.getCurrentSession()
					.createQuery("update DischargeSummary set isDischargeSummaryRelease='Y'," 
			                + "dischargePdfPath=:path "
							+ " where organizationId=:orgId and unitId=:unitId and dischargeSummaryId=:id ")
					.setParameter("id", dischargeSummaryDto.getDischargeSummaryId())
					.setParameter("orgId", dischargeSummaryDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryDto.getUnitId())
					.setParameter("path", path).executeUpdate();
			if (count > 0)
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			else
				return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}catch(HibernateException he){
			}
			catch (Exception e) {
				
			}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSavedDischargeSummaryByAdmissionId(DischargeSummaryDto dischargeSummaryDto) {

		try {
			DischargeSummaryDto dischargeSummaryFieldDto = new DischargeSummaryDto();

			DischargeSummaryDto dischargeSummaryDto1 = (DischargeSummaryDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select dis.discharge_summary_id as \"dischargeSummaryId\", "
							+ " (case when ((select is_from_discharge_summary from opd.m_appointment_master"
							+ " where against_admission_id=dis.admission_id order by appointment_id desc limit 1 ) is not null) "
							+ "  then 'Y' else 'N' end) as \"isFollowUpAppointment\" "
							+ "FROM adt.m_discharge_summary dis " + "where dis.admission_id=:admissionId "
							+"AND dis.organization_id=:orgId "
							+ " AND dis.unit_id=:unitId "
							+"AND dis.status='A' ")
					.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
					.setParameter("orgId", dischargeSummaryDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).uniqueResult();
			if (dischargeSummaryDto1 != null) {
				dischargeSummaryFieldDto.setDischargeSummaryId(dischargeSummaryDto1.getDischargeSummaryId());
			}
			List<DischargeSummaryDto> dischargeSummaryFieldList =new ArrayList<>();
			if(dischargeSummaryFieldDto.getDischargeSummaryId() != null) {
				dischargeSummaryFieldList = sessionFactory.getCurrentSession().createSQLQuery(
								"select disSumm.discharge_summary_id as \"dischargeSummaryId\",fieldMaster.discharge_field_id as \"dischargeFieldId\" ,fieldMaster.discharge_field_desc as \"dischargeFieldName\","
										+ " disDetail.discharge_field_description as \"dischargeFieldDescription\", "
										+ " disSumm.discharge_template_id as \"dischargeTemplateId\",disSummTemp.discharge_desc as \"dischargeTemplateName\",disSumm.is_report_release as \"isDischargeSummaryRelease\"  "
										+ "FROM adt.m_discharge_summary_field_master fieldMaster "
										+ "inner join adt.m_discharge_summary_detail disDetail "
										+ "on disDetail.discharge_field_id=fieldMaster.discharge_field_id "
										+ "inner join adt.m_discharge_summary disSumm "
										+ "on disDetail.discharge_summary_id=disSumm.discharge_summary_id "
										+ "left join adt.m_discharge_summary_template_master disSummTemp "
										+ "on disSummTemp.discharge_id=disSumm.discharge_template_id "
										+ "where disDetail.discharge_summary_id=:dischargeSummaryId "
										+ " AND disDetail.organization_id=:organizationId "
										+ " AND disDetail.unit_id=:unitId ")
						.setParameter("dischargeSummaryId", dischargeSummaryFieldDto.getDischargeSummaryId())
						.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
			}

			if (dischargeSummaryFieldList.size() > 0) {
				return new Response(SUCCESS, SUCCESS_CODE, null, dischargeSummaryFieldList, dischargeSummaryDto1);
			} else {
				return new Response(ERROR, SUCCESS_CODE, null, dischargeSummaryFieldList, dischargeSummaryDto1);
			}
		} catch (HibernateException he) {
			he.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public ByteArrayOutputStream createDischargePDF(DischargeSummaryDto dischargeSummaryDto) {
		try {
			ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
			List<DischargeSummaryDto> dischargeSummaryFieldList =  getDischargeDetails(dischargeSummaryDto);
			DischargeSummaryReportDto patientData= getPatientsDischargeDetails(dischargeSummaryDto);
			
			if(patientData != null && dischargeSummaryFieldList != null) {
				PdfWriter pdfWriter = null;
				
				String disSummHead=null;
				String patientHeader="";
		        String htmlContent1="";
		        
		      //check whether discharge summary header required for unit
		        Character showDischargeSummHead=(patientData.getIsDischargeSummHeadReq()==null)?'N':
		        	patientData.getIsDischargeSummHeadReq().equals('N')?'N':'Y';
		        if(patientData.getDischargeSummaryHeader()!=null) {
		        	disSummHead=patientData.getDischargeSummaryHeader();
		        }
		   
		        /*   if(showDischargeSummHead.equals('Y')) {
					String imgSrc = patientData.getUnitLogo() != null && patientData.getUnitLogo() != "" 
							? "<img src='"+patientData.getUnitLogo()+"' alt='Unit Logo' height='100%' width='100%'>" : "" ;
				    System.err.println("logo "+ patientData.getUnitLogo());
					disSummHead= "<table border='0' width='500' >"  
								+ "<tr><td id='image' height='70'  width='100'>"+imgSrc+"</td>"
								+ "<td width='350' colspan='2' >"
								+ " <table border='0' >"
								+ " <tr><td height='20' width='400' ></td></tr>"
								+ " <tr><td><font size='+1'><b>"+patientData.getUnitName()+" </b></font></td></tr>"
								+ " <tr><td><font size='-1'>"+patientData.getUnitAddress()+"</font></td></tr>"
								+ "	</table>"
								+ "</td>"
								+ "</tr>"  
								+ "<tr><td colspan='3'></td></tr>"
								+ "</table>"  ;
				}*/
				
				 /*patientHeader= "<table border='1' >"  
						+ "<tr><th width='500' height='20' style='text-align: center;'><font size='+1'><b>DISCHARGE SUMMARY</b></font></th></tr>"  
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
					
						+ "<tr>"  
						+ "<th width='100' ><font size='-1'><b>Date of Discharge</b></font></th>"  
						+ "<th width='170' ><font size='-1'>: "+patientData.getDischargeDate()+"</font></th>"    
						+ "<th width='100' ><font size='-1'><b>Time of Discharge</b></font></th>"  
						+ "<th width='180' ><font size='-1'>: "+patientData.getDischargeTime()+"</font></th>"  
						+ "</tr>"  
						 
//					+ "<tr>"  
//						+ "<th width='100' ></th>"  
//						+ "<th width='150' ></th>"  
//						+ "</tr>" 	+ "<tr colspan='4' height='1' width='500' border='1' style='border-bottom: solid;' ></tr>"  
						+ "</table>";*/
		        
		        //space added for letter head
		        String heading=patientData.getDischargeSummaryHeaderLogo()+""
		         		+ "<font size='5'><h1 align='center' style='line-height: 10pt;margin-top: 10pt;'>Discharge Summary</h1></font>";
		         //without space
		        //String heading="<H3 align='center'><b>Discharge Summary</b></H3>";
					 //"<table>"  
//						+ "<tr><th width='500' height='20' style='text-align: center;'><font size='+1'><b>Discharge Summary</b></font></th></tr>"  
//						+ "</table>"  
		     
		        
		        //new patient header for UCC as per Nived requirement
		        patientHeader="<br><p style='line-height: 12pt;margin-top: 12pt;'><table border='0' style='margin-left: 5%; margin-right: 5%;'>"  
						+ "<tr>"  
						+ "<th width='14%' ><font size='3'><b>UHID No</b></font></th><th width='1%'>:</th>"  
						+ "<th width='31%' ><font size='3'>"+patientData.getUhidNumber()+"</font></th>"
								+ "<th width='16%' ><font size='3'><b>IP No</b></font></th><th width='1%'>:</th>"  
								+ "<th width='30%' ><font size='3'>"+patientData.getAdmissionId()+"</font></th>"  
						+ "</tr>"  
						
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Patient Name</b></font></th><th width='1%'>:</th>"  
                        + "<th  width='31%' ><font size='3'>"+patientData.getPatientName()+"</font></th>"  
								+ "<th  width='16%' ><font size='3'><b>Bed No</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getBedNumber()+"</font></th>"  
						+ "</tr>"  
					
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Age/Gender</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='31%' ><font size='3'>"+patientData.getAgeWithGender()+"</font></th>"  
								+ "<th  width='16%' ><font size='3'><b>Ward</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getWardName()+"</font></th>" 
						+ "</tr>"

						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Address No</b></font></th><th width='1%'>:</th>" 
						+ "<th  width='31%' ><font size='3'>"+(patientData.getPatientAddress()!=null?patientData.getPatientAddress():"")+"</font></th>" 
								+ "<th  width='16%' ><font size='3'><b>Admission Date</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getAdmissionDate()+" "+patientData.getAdmissionTime()+"</font></th>"   
						+ "</tr>"  
	
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Contact No</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='31%' ><font size='3'>"+patientData.getPatientContactNo()+"</font></th>"  
								+ "<th  width='16%' ><font size='3'><b>Discharge Date</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getDischargeDate()+" "+patientData.getDischargeTime()+"</font></th>"  
						+ "</tr>"  
					
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Doctor</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='31%' ><font size='3'>Dr. "+patientData.getDoctorName()+"</font></th>"    
						+ "<th  width='16%' ><font size='3'><b>Department</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='30%' ><font size='3'>"+patientData.getDepartmentName()+"</font></th>"  
					+ "</tr>"  
						 
					/*+ "<tr>"  
						+ "<th width='200' ></th>"  
						+ "<th width='250' ></th>"  
						+ "</tr>" 	+ "<tr colspan='4' height='2' width='500' border='2' style='border-bottom: solid;' ></tr>"  */
						+ "</table></p>";
				 
				 if(disSummHead==null) {
					  htmlContent1 =patientHeader;
				 }else {
					  htmlContent1=disSummHead.concat(patientHeader);
				 }
				
						
		        String htmlContent="<br>";
					for(DischargeSummaryDto i :dischargeSummaryFieldList){
						//htmlContent =htmlContent.concat("<table border='1'>");
						htmlContent = htmlContent.concat("<b>"+i.getDischargeFieldName()+" : </b><br>");
						htmlContent = htmlContent.concat(i.getDischargeFieldDescription().replaceAll("<table", "<br><table border='0.01' ")
								.replaceAll("</B>", "</B><br>").replaceAll("<tr", "<tr size='2'").replaceAll("<th", "<th bgcolor='#FFFFFF' color='#080808' ")+"");//bgcolor='#2e6ea2' color='#FFFFFF'
						//htmlContent =htmlContent.concat("</table>");

						
				//NIVED requirement start		
//						htmlContent =htmlContent.concat("<table border='0.2' cellpadding='4'><tr valign='top'><td>");
//						htmlContent = htmlContent.concat("<b><u>"+i.getDischargeFieldName()+" :</u></b>");
//						//.replaceAll("</B>", "</B><br>").replaceAll("<tr", "<tr size='2'")
//						htmlContent = htmlContent.concat(i.getDischargeFieldDescription().replaceAll("<table", "<br><br><table cellpadding='4' border='0.01' ")
//								.replaceAll("<B>Personal History</B>", "<br><B>Personal History</B>").replaceAll("<th", "<th bgcolor='#FFFFFF' color='#080808' ")
//								.replaceAll("<B>Vitals</B>", "<br><B>Vitals</B>")
//								.replaceAll("<td", "<td valign='top' "));//bgcolor='#2e6ea2' color='#FFFFFF'
//						htmlContent =htmlContent.concat("</td></tr></table><br>");
						//NIVED requirement end	
						
						
					}
					String footerText="<br><br><br><table border='0'><tr><td colspan='3'></td><td><p align='center'><font size='-1'><b>Consultant Signature</b></font></p></td></tr></table>";
									//+ "<tr><td colspan='4'></td><td><p align='center'><font size='-1'><b>( Register / CMO )</b></font></p></td></tr></table>";
							//+ "<tr><td colspan='4'></td><td><p align='center'><font size='-1'><b>( Consultant )</b></font></p></td></tr></table>";
					
					htmlContent = htmlContent.concat(footerText);
					try {
						final int ALIGN_TOP = 4;
						//document top header
						//StringBuffer sb = new StringBuffer(heading);
						//document top header
						StringBuffer sb1 = new StringBuffer(htmlContent1);
						//main body
						StringBuffer sb2 = new StringBuffer(htmlContent);
						
				        Document documentPDF =new Document();
//				        fileName = "D:/dischargeSummary"+dischargeSummaryDto.getDischargeSummaryId()+".pdf";
//				        FileOutputStream fos =new FileOutputStream(fileName);
//				        pdfWriter = PdfWriter.getInstance(documentPDF,fos);
						pdfWriter = PdfWriter.getInstance(documentPDF,byteStream);
				        pdfWriter.setLinearPageMode();
				        pdfWriter.setFullCompression();
                        documentPDF.setMargins(60, 20, 120, 30);
                       // documentPDF.addTitle("Discharge Summary");
				        documentPDF.open();
				        
				        String realPath = servletContext.getRealPath("/")+"resources"+File.separator+"assets"+File.separator +"fonts"+File.separator+"Verdana10.ttf";
				        
				        FontFactoryImp facImpl=new FontFactoryImp();
				        Font font = facImpl.getFont(realPath, 12, Font.BOLD, BaseColor.BLACK);
						Paragraph docHeading = new Paragraph();
						docHeading.setFont(font);
						docHeading.setAlignment(Element.ALIGN_CENTER);
						//docHeading.add("DISCHARGE SUMMARY");
						docHeading.add(dischargeSummaryFieldList.get(0).getDischargeTemplateName());
						docHeading.setSpacingAfter(1);
						documentPDF.add(docHeading);
						
				        //PdfWriter.
				       // documentPDF.add((Element) FontFactory.getFont(FontFactory.TIMES_ROMAN,18, Font.ITALIC, BaseColor.BLACK));
				        HTMLWorker htmlWorker = new HTMLWorker(documentPDF);
         		        Map<String,Object> providers = new HashMap<String, Object>();

				        defaultFontProvider dfp = new defaultFontProvider(realPath);
				        providers.put(HTMLWorker.FONT_PROVIDER, dfp);
				        //providers.put(HTMLWorker., );
				        htmlWorker.setProviders(providers);
                       // dfp.register(realPath, "Verdana");
				        
				        //htmlWorker.setStyleSheet(GenerateStyleSheet());
				        
				       // StringReader sr = new StringReader(sb.toString());
				        StringReader sr1 = new StringReader(sb1.toString());
				        StringReader sr2 = new StringReader(sb2.toString());
				      
				        //heading
				        //htmlWorker.parse(sr);
				        
				        LineSeparator lsp= new LineSeparator();
				        lsp.setOffset(-12);
//				        Chunk linebreak = new Chunk(lsp);
//				        linebreak.setCharacterSpacing(-30);
				        documentPDF.add(lsp);
				     
				        //patient header
				        htmlWorker.parse(sr1);
				        
				        
				        LineSeparator lsp1= new LineSeparator();
				        lsp1.setOffset(-10);
//				        Chunk linebreak = new Chunk(lsp1);
//				        linebreak.setCharacterSpacing(+5);
				        documentPDF.add(lsp1);
				        
				        //body
				        htmlWorker.parse(sr2);
				        htmlWorker.close();
				        //sr.close();
				        documentPDF.close();
//				        fos.close();
			            pdfWriter.close();
			           // Response unitRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(dischargeSummaryDto.getOrganizationId());
			            //OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();
			            //fileName =  docUploadUtility.uploadDischargeSummaryReport(byteStream, organizationMasterDto,dischargeSummaryDto.getUnitId(),dischargeSummaryDto.getDischargeSummaryId());
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
	
	public  DischargeSummaryReportDto getPatientsDischargeDetails(DischargeSummaryDto dischargeSummaryDto){
		DischargeSummaryReportDto dto= null;  
		try {
				String query =	"SELECT pim.uhid_number as \"uhidNumber\",ad.admission_id as \"admissionId\",ad.admission_number as \"admissionNo\", " + 
					"    concat(COALESCE(pm.prefix_code,''),' ',COALESCE(pim.first_name,''), ' ', COALESCE(pim.middle_name, ''), ' '," + 
					"     COALESCE(pim.last_name, '')) as \"patientName\", " + 
					//"    concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pim.dob)),'M ',EXTRACT(days from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'D ') as \"age\"," + 
					"   gm.gender_name as \"gender\"," + 
					"    um.unit_name as \"unitName\","+
				    " um.unit_logo as \"unitLogo\"," + 
					"    um.unit_address as \"unitAddress\",dm.doctor_id as \"doctorId\"," + 
					"    CONCAT(dm.first_name,' ',dm.last_name) AS \"doctorName\"," + //' ',dm.middle_name,
					"      (case when ad.speciality_id is null then (select  spm.speciality_name from t_unit_application_billing_configuration app " +
				    "           inner join doctor.m_speciality_master spm on spm.speciality_id=app.er_speciality_id"+
				    "         where app.organization_id=ad.organization_id and app.unit_id=ad.unit_id and app.status='A') else (select  spm.speciality_name from doctor.m_speciality_master spm "+
				    "         where spm.speciality_id=ad.speciality_id) end) as \"departmentName\"," + 
					"    to_char(adet.doa,'dd/MM/yyyy') as \"admissionDate\"," + 
					"    to_char(adet.doa,'HH:MI AM') as \"admissionTime\"," + 
					"    wm.ward_name as \"wardName\"," + 
					"    bm.bed_number as \"bedNumber\"," + 
					"    to_char(dc.discharge_date,'dd/MM/yyyy') as \"dischargeDate\"," + 
					"    to_char(dc.discharge_time,'HH:MI AM') as \"dischargeTime\","
					+ "  um.is_discharge_summ_head_req as \"isDischargeSummHeadReq\","
					+ "  um.discharge_summary_header_logo as \"dischargeSummaryHeaderLogo\","
					+ "  um.discharge_summary_header as \"dischargeSummaryHeader\",um.discharge_summary_footer as \"dischargeSummaryFooter\","+
				    "  concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pim.dob)), ' Years ','/ ',gm.gender_code) as \"ageWithGender\", pim.mobile_number  as \"patientContactNo\",patDet.address as \"patientAddress\",um.discharge_summary_page_margin as \"dischargeSummaryPageMargin\", " +
					"  pim.patient_id as \"patientId\" " +
					"  from  adt.t_admission ad  " +
					"     LEFT join patient.t_patient_registration pim on pim.patient_id = ad.patient_id"+
				    "     LEFT join adt.m_discharge_summary dis on dis.admission_id=ad.admission_id "+
				    "     LEFT join m_unit_master um ON ad.unit_id = um.unit_id" + 
					"     LEFT JOIN m_prefix_master pm ON pim.prefix_id = pm.prefix_id" + 
					"     LEFT JOIN m_gender_master gm ON pim.gender_id = gm.gender_id" + 
					"     LEFT JOIN patient.t_patient_details patDet ON pim.patient_id = patDet.patient_id " + 
					"     LEFT JOIN m_organization_master om ON ad.organization_id = om.organization_id " + 
					"     LEFT JOIN doctor.m_doctor_master dm ON dm.doctor_id =(case when (ad.doctor_id is null or ad.doctor_id=0) then dis.doctor_id else ad.doctor_id end) " + 
					"     LEFT JOIN m_user_master udm ON ad.created_by = udm.user_id " + 
				//	"     LEFT JOIN doctor.m_speciality_master spm ON spm.speciality_id=dis.speciality_id " + 
					"     LEFT JOIN adt.t_admission_details adet ON ad.admission_id = adet.admission_id " + 
					"     LEFT JOIN adt.m_ward_master wm ON adet.ward_id = wm.ward_id " + 
					"     LEFT JOIN adt.t_discharge dc ON ad.admission_id = dc.admission_id and dc.status ='A' " + 
					"     LEFT JOIN adt.m_bed_master bm ON adet.bed_id = bm.bed_id " + 
					"  WHERE dis.status='A' AND adet.admission_details_id = (( SELECT t_admission_details.admission_details_id " + //dc.status = 'A' AND 
					"           FROM adt.t_admission_details " + 
					"          WHERE t_admission_details.admission_id = ad.admission_id " + 
					"          ORDER BY t_admission_details.admission_details_id DESC " + 
					"         LIMIT 1)) and ad.admission_id=:admissionId";
				dto = (DischargeSummaryReportDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryReportDto.class)).uniqueResult();
			}catch (HibernateException he) {
			} catch (Exception e) {
				
			}
		return dto;
	}
	
	public  DischargeSummaryReportDto getPatientsDischargeDetailsForWithoutHeader(DischargeSummaryDto dischargeSummaryDto){
		DischargeSummaryReportDto dto= null;  
		try {
				String query =	"SELECT pim.uhid_number as \"uhidNumber\",ad.admission_id as \"admissionId\",ad.admission_number as \"admissionNo\", " + 
					"    concat(COALESCE(pm.prefix_code,''),' ',COALESCE(pim.first_name,''), ' ', COALESCE(pim.middle_name, ''), ' '," + 
					"     COALESCE(pim.last_name, '')) as \"patientName\", " + 
					//"    concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pim.dob)),'M ',EXTRACT(days from AGE(CURRENT_TIMESTAMP, pim.dob)) , 'D ') as \"age\"," + 
					"   gm.gender_name as \"gender\"," + 
//					"    um.unit_name as \"unitName\","+
//				    " um.unit_logo as \"unitLogo\"," + 
					"    um.unit_address as \"unitAddress\",dm.doctor_id as \"doctorId\"," + 
					"    CONCAT(dm.first_name,' ',dm.last_name) AS \"doctorName\"," + //' ',dm.middle_name,
					"      (case when ad.speciality_id is null then (select  spm.speciality_name from t_unit_application_billing_configuration app " +
				    "           inner join doctor.m_speciality_master spm on spm.speciality_id=app.er_speciality_id"+
				    "         where app.organization_id=ad.organization_id and app.unit_id=ad.unit_id and app.status='A') else (select  spm.speciality_name from doctor.m_speciality_master spm "+
				    "         where spm.speciality_id=ad.speciality_id) end) as \"departmentName\"," + 
					"    to_char(adet.doa,'dd/MM/yyyy') as \"admissionDate\"," + 
					"    to_char(adet.doa,'HH:MI AM') as \"admissionTime\"," + 
					"    wm.ward_name as \"wardName\"," + 
					"    bm.bed_number as \"bedNumber\"," + 
					"    to_char(dc.discharge_date,'dd/MM/yyyy') as \"dischargeDate\"," + 
					"    to_char(dc.discharge_time,'HH:MI AM') as \"dischargeTime\", "
					+ " um.is_discharge_summ_head_req as \"isDischargeSummHeadReq\","
					+ " um.discharge_summary_header as \"dischargeSummaryHeader\","
					+ " um.page_margine_for_without_letterhead as \"pageMargineForWithoutLetterhead\", um.discharge_summary_footer as \"dischargeSummaryFooter\","+
				    "  concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pim.dob)), ' Years ','/ ',gm.gender_code) as \"ageWithGender\", pim.mobile_number  as \"patientContactNo\",patDet.address as \"patientAddress\",um.discharge_summary_page_margin as \"dischargeSummaryPageMargin\", " +
					" pim.patient_id as \"patientId\" " +
					"  from  adt.t_admission ad  " + 
					"     LEFT join patient.t_patient_registration pim on pim.patient_id = ad.patient_id"+
				    "     LEFT join adt.m_discharge_summary dis on dis.admission_id=ad.admission_id "+
				    "     LEFT join m_unit_master um ON ad.unit_id = um.unit_id" + 
					"     LEFT JOIN m_prefix_master pm ON pim.prefix_id = pm.prefix_id" + 
					"     LEFT JOIN m_gender_master gm ON pim.gender_id = gm.gender_id" + 
					"     LEFT JOIN patient.t_patient_details patDet ON pim.patient_id = patDet.patient_id " + 
					"     LEFT JOIN m_organization_master om ON ad.organization_id = om.organization_id " + 
					"     LEFT JOIN doctor.m_doctor_master dm ON dm.doctor_id =(case when (ad.doctor_id is null or ad.doctor_id=0) then dis.doctor_id else ad.doctor_id end) " + 
					"     LEFT JOIN m_user_master udm ON ad.created_by = udm.user_id " + 
				//	"     LEFT JOIN doctor.m_speciality_master spm ON spm.speciality_id=dis.speciality_id " + 
					"     LEFT JOIN adt.t_admission_details adet ON ad.admission_id = adet.admission_id " + 
					"     LEFT JOIN adt.m_ward_master wm ON adet.ward_id = wm.ward_id " + 
					"     LEFT JOIN adt.t_discharge dc ON ad.admission_id = dc.admission_id and dc.status ='A' " + 
					"     LEFT JOIN adt.m_bed_master bm ON adet.bed_id = bm.bed_id " + 
					"  WHERE dis.status='A' AND adet.admission_details_id = (( SELECT t_admission_details.admission_details_id " + //dc.status = 'A' AND 
					"           FROM adt.t_admission_details " + 
					"          WHERE t_admission_details.admission_id = ad.admission_id " + 
					"          ORDER BY t_admission_details.admission_details_id DESC " + 
					"         LIMIT 1)) and ad.admission_id=:admissionId";
				dto = (DischargeSummaryReportDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryReportDto.class)).uniqueResult();
			}catch (HibernateException he) {
			} catch (Exception e) {
				
			}
		return dto;
	}
	
	
	
	public List<DischargeSummaryDto> getDischargeDetails(DischargeSummaryDto dischargeSummaryDto){
		List<DischargeSummaryDto> dischargeSummaryFieldList = null;
		try {
			String query = "select fieldMaster.discharge_field_desc as \"dischargeFieldName\","
					+ " disSummTemp.discharge_desc as \"dischargeTemplateName\"," 
				    + " disDetail.discharge_field_description as \"dischargeFieldDescription\","
				    + " fieldMaster.discharge_field_id as \"dischargeFieldId\" "
				    + "FROM adt.m_discharge_summary_field_master fieldMaster "  
					+ "inner join adt.m_discharge_summary_detail disDetail "
					+ "on disDetail.discharge_field_id=fieldMaster.discharge_field_id "
					+ "inner join adt.m_discharge_summary disSumm "
					+ "on disDetail.discharge_summary_id=disSumm.discharge_summary_id "
					+ " inner join adt.t_discharge_summary_template_mapper tempmapper on"
					+" fieldMaster.discharge_field_id = tempmapper.discharge_field_id "
					+ " and disSumm.discharge_template_id =tempmapper.discharge_summary_temp_id and tempmapper.status='A' "
					+ " inner join adt.m_discharge_summary_template_master disSummTemp "
					+ " on disSummTemp.discharge_id=disSumm.discharge_template_id "
					+ " where disDetail.discharge_summary_id=:dischargeSummaryId "
					+ " AND disDetail.status='A' and disSumm.status='A' order by tempmapper.discharge_summary_temp_mppr_id asc ";
					//+ " AND disDetail.organization_id="+dischargeSummaryDto.getOrganizationId()
					//+ " AND disDetail.unit_id="+dischargeSummaryDto.getUnitId();

			Query sql =  sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("dischargeSummaryId", dischargeSummaryDto.getDischargeSummaryId());
			dischargeSummaryFieldList =	sql.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
				return dischargeSummaryFieldList;
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response searchPatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto) {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_LIST_FOR_DISCHARGE_SUMMARY").getQueryString());
			List<AdmissionDto> admissionDtosList=new ArrayList<AdmissionDto>();
			 if(dischargeSummaryDto.getRmoMappedWardList()!=null) {
				 query.append(" and adl.ward_id in (:wardList) "
						 + " and (lower(pr.first_name) like :searchKey or lower(pr.last_name) like :searchKey or lower(pr.uhid_number) like :searchKey  or lower(admission.admission_number) like :searchKey ) ");
						 if(dischargeSummaryDto.getVisitTypeId()!=OP) {
					    	 query.append(" and admission.visit_type_id=:visitTypeId ");
					    }
				 query.append( " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");//\"dischargeId\" desc limit 1
			     Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", dischargeSummaryDto.getOrganizationId())
							.setParameter("unitId", dischargeSummaryDto.getUnitId())
							.setParameterList("wardList", dischargeSummaryDto.getRmoMappedWardList())
							.setParameter("doctorId", dischargeSummaryDto.getDoctorId())
							.setParameter("searchKey", "%"+dischargeSummaryDto.getSearchKeyword().toLowerCase()+"%");
				 if(dischargeSummaryDto.getVisitTypeId()!=OP) {
					 sql.setParameter("visitTypeId", dischargeSummaryDto.getVisitTypeId());
				 }
				 admissionDtosList=	sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			     
			 }else {
				query.append(" and (admission.doctor_id = admission.doctor_id OR (((case when (select specMa.speciality_id from doctor.t_doctor_speciality_mapper specMa where specMa.doctor_id= admission.doctor_id and specMa.status='A' and  specMa.speciality_id=("
				 		+" select app.er_speciality_id from t_unit_application_billing_configuration app where app.organization_id=:orgId and app.unit_id=:unitId ))>0 then true else false end) and admission.visit_type_id=4)))"
				 		+ " and ((lower(concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.middle_name, ''), ' ', coalesce(pr.last_name, ''),' ', coalesce(pr.uhid_number, ''))) like :searchKey) or lower(pr.uhid_number) like :searchKey or lower(admission.admission_number) like :searchKey )");
					 if(dischargeSummaryDto.getVisitTypeId()!=1) {
				    	 query.append(" and admission.visit_type_id=:getVisitTypeId ");
				    }
					 
				query.append( " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");//,\"dischargeId\" desc limit 1 
				 Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", dischargeSummaryDto.getOrganizationId())
							.setParameter("unitId", dischargeSummaryDto.getUnitId())
							.setParameter("doctorId", dischargeSummaryDto.getDoctorId())
							.setParameter("searchKey", "%"+dischargeSummaryDto.getSearchKeyword().toLowerCase()+"%");
				 if(dischargeSummaryDto.getVisitTypeId()!=1) {
					 sql.setParameter("getVisitTypeId", dischargeSummaryDto.getVisitTypeId());
				 }
				 admissionDtosList=	sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			 }
			 
				if(admissionDtosList.size()>0){
				   return new Response(SUCCESS, null, null, admissionDtosList, null);
				}else{
					 return new Response(ERROR, null, "NO patient marked for discharge", null, null);
				}
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Integer getDischargeSummaryIdByAdmissionId(Integer admissionId) {
		try {
			Integer dischargeSummaryId=(Integer)sessionFactory.getCurrentSession().createSQLQuery("select discharge_summary_id   "
					+ " from adt.m_discharge_summary " + 
					"	where admission_id="+admissionId).uniqueResult();
			 return dischargeSummaryId;
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response getFollowUpAppointment(DischargeSummaryDto dischargeSummaryDto) {
		try {
			List<SlotMasterDto> appList = sessionFactory.getCurrentSession()
					.createSQLQuery(" select to_char(appMas.appointment_date,'DD/MM/YYYY') as \"appointmentDate\","
							+ " to_char(slot.from_time,'HH:MI AM') as \"fromTimeString\",appMas.appointment_id as \"appointmentId\" "
							+ " from opd.m_appointment_master appMas " 
							+ " inner join opd.m_slot_master slot "
							+ "  on appMas.slot_id=slot.slot_id " 
							+ " where appMas.is_from_discharge_summary='Y' "
							+ " and appMas.against_admission_id=:admissionId")
					.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					
			 return new Response(SUCCESS, null, null, appList, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}
	
	
	 private static StyleSheet GenerateStyleSheet()
	    {
	        StyleSheet css = new StyleSheet();

	        css.loadStyle("body", "face", "sylfaen");
	        css.loadStyle("body", "encoding", "Identity-H");
	        css.loadStyle("body", "size", "9pt");
	        css.loadStyle("a", "style", "text-decoration:underline;");
	        return css;
	    }

	@Override
	public ByteArrayOutputStream createDischargePDF1(DischargeSummaryDto dischargeSummaryDto) {

		try {
			ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
			ByteArrayOutputStream labByteArrayOutputStream=new ByteArrayOutputStream();
			List<DischargeSummaryDto> dischargeSummaryFieldList =  getDischargeDetails(dischargeSummaryDto);
			DischargeSummaryReportDto patientData=null;
			if(dischargeSummaryDto.getHeaderType() != null && dischargeSummaryDto.getHeaderType() == 2){
				 patientData= getPatientsDischargeDetailsForWithoutHeader(dischargeSummaryDto);

			}else {
				 patientData= getPatientsDischargeDetails(dischargeSummaryDto);

			}
			Boolean isReportReleaseAvailable=false;
			String patientHeader="";
			String hospitalHeader="";
			
			//get application configuration data
			DischargeSummaryDto unitConfigRes = (DischargeSummaryDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select pdf_generation_url as \"crystalReportUrl\","
							+ "is_add_report_to_discharge_summary as \"isReportAddIntoDischargeSummary\" from public.t_unit_application_billing_configuration tuabc\r\n"
							+ "where tuabc.organization_id =:orgId and tuabc.unit_id=:unitId")
					.setParameter("orgId", dischargeSummaryDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).uniqueResult();
			if(unitConfigRes!=null) {
				dischargeSummaryDto.setCrystalReportUrl(unitConfigRes.getCrystalReportUrl());
				if(unitConfigRes.getIsReportAddIntoDischargeSummary()!=null)
				dischargeSummaryDto.setIsReportAddIntoDischargeSummary(unitConfigRes.getIsReportAddIntoDischargeSummary());
				 //labByteArrayOutputStream=getAllLabReports(dischargeSummaryDto);
			}
			if(patientData != null && dischargeSummaryFieldList != null) {
				PdfWriter pdfWriter = null;
				
				String disSummHead=null;
				String footerText="";
		        String htmlContent1="";

		        if(patientData.getDischargeSummaryHeader()==null) {
		        //new patient header for UCC as per Nived requirement
                patientHeader= "<br><p style='line-height: 12pt;margin-top: 12pt;'><table border='0' style='margin-left: 5%; margin-right: 5%;'>"  
						+ "<tr>"  
						+ "<th width='14%' ><font size='3'><b>UHID No</b></font></th><th width='1%'>:</th>"  
						+ "<th width='31%' ><font size='3'>"+patientData.getUhidNumber()+"</font></th>"
								+ "<th width='16%' ><font size='3'><b>IP No</b></font></th><th width='1%'>:</th>"  
								+ "<th width='30%' ><font size='3'>"+patientData.getAdmissionNo()+"</font></th>"  
						+ "</tr>"  
						
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Patient Name</b></font></th><th width='1%'>:</th>"  
                        + "<th  width='31%' ><font size='3'>"+patientData.getPatientName()+"</font></th>"  
								+ "<th  width='16%' ><font size='3'><b>Bed No</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getBedNumber()+"</font></th>"  
						+ "</tr>"  
					
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Age/Gender</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='31%' ><font size='3'>"+patientData.getAgeWithGender()+"</font></th>"  
								+ "<th  width='16%' ><font size='3'><b>Ward</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getWardName()+"</font></th>" 
						+ "</tr>"

						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Address No</b></font></th><th width='1%'>:</th>" 
						+ "<th  width='31%' ><font size='3'>"+(patientData.getPatientAddress()!=null?patientData.getPatientAddress():"")+"</font></th>" 
								+ "<th  width='16%' ><font size='3'><b>Admission Date</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getAdmissionDate()+" "+patientData.getAdmissionTime()+"</font></th>"   
						+ "</tr>"  
	
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Contact No</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='31%' ><font size='3'>"+patientData.getPatientContactNo()+"</font></th>"  
								+ "<th  width='16%' ><font size='3'><b>Discharge Date</b></font></th><th width='1%'>:</th>"  
								+ "<th  width='30%' ><font size='3'>"+patientData.getDischargeDate()+" "+patientData.getDischargeTime()+"</font></th>"  
						+ "</tr>"  
					
						+ "<tr>"  
						+ "<th  width='14%' ><font size='3'><b>Doctor</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='31%' ><font size='3'>Dr. "+patientData.getDoctorName()+"</font></th>"    
						+ "<th  width='16%' ><font size='3'><b>Department</b></font></th><th width='1%'>:</th>"  
						+ "<th  width='30%' ><font size='3'>"+patientData.getDepartmentName()+"</font></th>"  
					    + "</tr>"  
						+ "</table></p>";
		        }else {
		        	String logoWithHeader="";
		        	if(patientData.getDischargeSummaryHeaderLogo() != null) {
		        		logoWithHeader=patientData.getDischargeSummaryHeaderLogo();
		        	}
		        	String header="<br>"+ patientData.getDischargeSummaryHeader();
		        	header=header.replace("@dischargeTemplateName", dischargeSummaryFieldList.get(0).getDischargeTemplateName());

		        	header=header.replace("@uhidNo", patientData.getUhidNumber());
		        	//header=header.replace("@admissionNo", patientData.getAdmissionId().toString());
		        	header=header.replace("@admissionNo", patientData.getAdmissionNo());
		        	
		        	header=header.replace("@patientName", patientData.getPatientName());
		        	header=header.replace("@bedNo", patientData.getBedNumber());
		        	
		        	header=header.replace("@patAge", patientData.getAgeWithGender());
		        	header=header.replace("@patWard", patientData.getWardName());
		        	
		        	header=header.replace("@patAddress", (patientData.getPatientAddress()!=null?patientData.getPatientAddress():""));
		        	header=header.replace("@admDateAndTime", patientData.getAdmissionDate()+" "+patientData.getAdmissionTime());
		        	
		        	header=header.replace("@admissionDate", patientData.getAdmissionDate());
		        	header=header.replace("@dischargeDate", patientData.getDischargeDate()!=null?patientData.getDischargeDate():"");
		        	
		        	header=header.replace("@contactNo", patientData.getPatientContactNo());
		        	if(patientData.getDischargeDate()!=null) {
		        	header=header.replace("@disDateAndTime", patientData.getDischargeDate()+" "+patientData.getDischargeTime());
		        	}else {
		        		header=header.replace("@disDateAndTime", "");
		        	}
		        	header=header.replace("@doctorName", patientData.getDoctorName());
		        	header=header.replace("@departmentName", patientData.getDepartmentName());
		        	
		        	patientHeader=logoWithHeader+"<br>"+header;
		        }
				 
				 if(disSummHead==null) {
					  htmlContent1 =patientHeader;
				 }else {
					  htmlContent1=disSummHead.concat(patientHeader);
				 }
				 
				 String doctorDegree=(String)sessionFactory.getCurrentSession()
						 .createSQLQuery("select (select array_to_string(array(select dm.degree_desc from doctor.t_doctor_doc_mapper  tddm " 
						 		+"inner join public.m_degree_master dm on dm.degree_id =tddm.degree_id " 
						 		+"where tddm.doctor_id =doc.doctor_id and tddm.status='A'),',')) "  
						 		+"from doctor.m_doctor_master doc " 
						 		+"where doc.doctor_id=:docId")
						 .setParameter("docId", patientData.getDoctorId()).uniqueResult();
				 if(patientData.getDischargeSummaryFooter()!=null && !patientData.getDischargeSummaryFooter().isEmpty()) {
					 footerText= patientData.getDischargeSummaryFooter();
					 footerText=footerText.replace("@doctorName","Dr. "+patientData.getDoctorName());
					 if(doctorDegree!=null && !doctorDegree.isEmpty()) {   
						 footerText=footerText.replace("@doctorDegrees", doctorDegree);
					 }else {
						 footerText=footerText.replace("@doctorDegrees", "");
						 footerText=footerText.replace("(", "");
						 footerText=footerText.replace(")", "");
					 }
				 }else {
					 
					 footerText="<br><br><br><table border='0'><tr><td colspan='2'></td><td><p align='center'><font size='-1'><b>Consultant Signature</b></font></p></td></tr>"
							 + "<tr><td colspan='2'></td><td><p align='center'><font size='-1'><b>Dr. "+patientData.getDoctorName()+"</b></font></p></td></tr>";
					if(doctorDegree!=null && !doctorDegree.isEmpty()) {       
						footerText+="<tr><td colspan='2'></td><td><p align='center'><font size='-1'><b>("+doctorDegree+")</b></font></p></td></tr></table>";
					}else {
						footerText+="</table>";
					}
				 }
				 
					
					//htmlContent = htmlContent.concat(footerText);
					try {
						final int ALIGN_TOP = 4;
						StringBuffer sb1 = new StringBuffer(htmlContent1);
						
						//main body
						//StringBuffer sb2 = new StringBuffer(htmlContent);
						
				        Document documentPDF =new Document();
						pdfWriter = PdfWriter.getInstance(documentPDF,byteStream);
				        pdfWriter.setLinearPageMode();
				        pdfWriter.setFullCompression();
                       // documentPDF.setMargins(60, 20, 120, 30);
				        if(dischargeSummaryDto.getHeaderType() == 1 && patientData.getDischargeSummaryPageMargin()!=null && !patientData.getDischargeSummaryPageMargin().isEmpty()) {
				        	
				        		String margin[]=(patientData.getDischargeSummaryPageMargin()).split(",");
						           //left right top bottom
						        	if(margin.length==4)
						        	documentPDF.setMargins(Float.parseFloat(margin[0]), Float.parseFloat(margin[1]), Float.parseFloat(margin[2]),Float.parseFloat(margin[3]));
						        	else
						            documentPDF.setMargins(60, 20, 120, 30);
				        		
				        }
				        else  if(dischargeSummaryDto.getHeaderType() == 2 && patientData.getPageMargineForWithoutLetterhead()!=null && !patientData.getPageMargineForWithoutLetterhead().isEmpty()) {
				        		String margin[]=(patientData.getPageMargineForWithoutLetterhead()).split(",");
						           //left right top bottom
						        	if(margin.length==4)
						        	documentPDF.setMargins(Float.parseFloat(margin[0]), Float.parseFloat(margin[1]), Float.parseFloat(margin[2]),Float.parseFloat(margin[3]));
						        	else
						            documentPDF.setMargins(60, 20, 120, 30);
				        }else {
				        	documentPDF.setMargins(60, 20, 120, 30);
				        }
                        
				        //for adding hospital header
				        if(patientData.getIsDischargeSummHeadReq()!=null  && patientData.getIsDischargeSummHeadReq().equals('Y')) {
					        PdfHeader header=new PdfHeader(patientData);
	                        pdfWriter.setPageEvent(header);
				        }
				        PdfFooter pdfFooter=new PdfFooter();
				        pdfWriter.setPageEvent(pdfFooter);
                        // documentPDF.addTitle("Discharge Summary");
				        documentPDF.open();
				        
				        String realPath = servletContext.getRealPath("/")+"resources"+File.separator+"assets"+File.separator +"fonts"+File.separator+"Verdana10.ttf";
				        
				        //documentPDF.add((Element) FontFactory.getFont(FontFactory.TIMES_ROMAN,18, Font.ITALIC, BaseColor.BLACK));
				        HTMLWorker htmlWorker = new HTMLWorker(documentPDF);
         		        Map<String,Object> providers = new HashMap<String, Object>();
         		        Map<String,Object> providers2 = new HashMap<String, Object>();
				        defaultFontProvider dfp = new defaultFontProvider(realPath);
				        providers.put(HTMLWorker.FONT_PROVIDER, dfp);
				        //providers.put(HTMLWorker., );
				        htmlWorker.setProviders(providers);
                        
				        defaultFontProvider2 dfp2= new defaultFontProvider2(realPath);
				        providers2.put(HTMLWorker.FONT_PROVIDER, dfp2);
				        
				        
				        FontFactoryImp facImpl=new FontFactoryImp();
				        Font baseFont = FontFactory.getFont("Times-Roman", 12, Font.BOLD, BaseColor.BLACK);
						Paragraph docHeading = new Paragraph();
						docHeading.setSpacingAfter(1);
						documentPDF.add(docHeading);
						
				       // StringReader sr = new StringReader(sb.toString());
				        StringReader sr1 = new StringReader(sb1.toString());
				        //StringReader sr2 = new StringReader(sb2.toString());
				      
				        //heading
				        LineSeparator lsp= new LineSeparator();
				        if(dischargeSummaryDto.getHeaderType() != null && dischargeSummaryDto.getHeaderType() == 2){
				        	 lsp.setOffset(-80);

						}else {
							 lsp.setOffset(-170);

						}

				        documentPDF.add(lsp);
				     
				        //patient header
				        //htmlWorker.setProviders(providers2);
				        htmlWorker.parse(sr1);
				        
				        
				        LineSeparator lsp1= new LineSeparator();
				        lsp1.setOffset(-5);
//				        Chunk linebreak = new Chunk(lsp1);
//				        linebreak.setCharacterSpacing(+5);
				        documentPDF.add(lsp1);
				        
				        
				        String htmlContent="";
				        Integer count=0;
						for(DischargeSummaryDto i :dischargeSummaryFieldList){
							
							if(count==0) {
								htmlContent="<br>";
							}else {
								htmlContent="";
							}
							count++;
							if(i.getDischargeFieldId()==10 && unitConfigRes!=null && unitConfigRes.getIsReportAddIntoDischargeSummary()!=null &&
									unitConfigRes.getIsReportAddIntoDischargeSummary().equals('Y')) {
								String fieldDes=i.getDischargeFieldDescription();
								int isAttrExist=fieldDes.indexOf("ord-det-ids", 1);
						  if(isAttrExist!=-1) {
								int firstIndex=fieldDes.indexOf("'", fieldDes.indexOf("ord-det-ids", 1));
								int secondIndex=fieldDes.indexOf("'", firstIndex+1);
								String finalString=fieldDes.substring(firstIndex+1, secondIndex);
								List<Integer> orderIds=new ArrayList();
 								if(!finalString.isEmpty()) {
									String ordDet[]=finalString.split(",");
									for(Integer j=0;j<ordDet.length;j++) {
										Integer orderDetId=Integer.parseInt(ordDet[j]);
										orderIds.add(orderDetId);
									}
									List<com.param.adt.discharge.dto.InvestigationOrderDto> orderList=getInvestigationReport(dischargeSummaryDto.getAdmissionId(),orderIds);
									appendLabAndRadiologyReportToDischargeSummary(orderList,documentPDF,htmlWorker);
								}
							}else {
								if(i.getDischargeFieldDescription()!=null && !i.getDischargeFieldDescription().isEmpty()) {
									htmlContent = htmlContent.concat("<b>"+i.getDischargeFieldName()+" : </b>");
									StringBuffer fieldHeading = new StringBuffer(htmlContent);
									StringReader fieldHeadingRead = new StringReader(fieldHeading.toString());
									htmlWorker.setProviders(providers2);
									StyleSheet css = new StyleSheet();
									css.loadTagStyle("p", "style", "text-align:justify;text-justify:inter-word;line-height:200%;");
									htmlWorker.setStyleSheet(css);
									htmlWorker.parse(fieldHeadingRead);
									htmlContent="";
									htmlContent = htmlContent.concat(i.getDischargeFieldDescription().replaceAll("<table", "<br><table border='0.01' ")
											.replaceAll("<p", "<span><p").replaceAll("</p>", "</p></span>")
											.replaceAll("</B>", "</B><br>").replaceAll("<tr", "<tr size='2'").replaceAll("<th", "<th bgcolor='#FFFFFF' color='#080808' ")+"");//bgcolor='#2e6ea2' color='#FFFFFF'
									StringBuffer sb2 = new StringBuffer(htmlContent);
									StringReader sr2 = new StringReader(sb2.toString());
									htmlWorker.setProviders(providers);
									StyleSheet css1 = new StyleSheet();
									css1.loadTagStyle("span", "style", "line-height:110%;");
								    css1.loadTagStyle("p", "style", "text-align:justify;text-justify:inter-word;");
									htmlWorker.setStyleSheet(css1);
									htmlWorker.parse(sr2);
								  }
							}
								
							}else {
							
								
								if(i.getDischargeFieldDescription()!=null && !i.getDischargeFieldDescription().isEmpty()) {
								htmlContent = htmlContent.concat("<b>"+i.getDischargeFieldName()+" : </b>");
								StringBuffer fieldHeading = new StringBuffer(htmlContent);
								StringReader fieldHeadingRead = new StringReader(fieldHeading.toString());
								htmlWorker.setProviders(providers2);
								StyleSheet css = new StyleSheet();
								 css.loadTagStyle("p", "style", "text-align:justify;text-justify:inter-word;line-height:200%;");
								htmlWorker.setStyleSheet(css);
								htmlWorker.parse(fieldHeadingRead);
								htmlContent="";
								htmlContent = htmlContent.concat(i.getDischargeFieldDescription().replaceAll("<table", "<br><table border='0.01' ")
										.replaceAll("<p", "<span><p").replaceAll("</p>", "</p></span>")
										.replaceAll("</B>", "</B><br>").replaceAll("<tr", "<tr size='2'").replaceAll("<th", "<th bgcolor='#FFFFFF' color='#080808' ")+"");//bgcolor='#2e6ea2' color='#FFFFFF'
								StringBuffer sb2 = new StringBuffer(htmlContent);
								StringReader sr2 = new StringReader(sb2.toString());
								htmlWorker.setProviders(providers);
								StyleSheet css1 = new StyleSheet();
								css1.loadTagStyle("span", "style", "line-height:110%;");
								css1.loadTagStyle("p", "style", "text-align:justify;text-justify:inter-word;");//line-height:110%;
								htmlWorker.setStyleSheet(css1);
								htmlWorker.parse(sr2);
							  }
							}
						}
					
						StringBuffer sb3 = new StringBuffer(footerText);
						StringReader sr3 = new StringReader(sb3.toString());
						StyleSheet css1 = new StyleSheet();
						css1.loadTagStyle("p", "style", "line-height:110%;");//line-height:110%;
						htmlWorker.setStyleSheet(css1);
						htmlWorker.parse(sr3);
				        
				        
				        htmlWorker.close();
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

					dischargeSummaryDto.setDischargeSummaryPdfBytes(byteStream);
					dischargeSummaryDto.setPatientId(patientData.getPatientId());
					Response myLifeRes = sendDischargeSummaryReportToMyLife(dischargeSummaryDto);

					return byteStream;
				}
			 return byteStream;
			}catch (HibernateException he) {
			} catch (Exception e) {
				
			}
		return null;
	
	}

	private void appendLabAndRadiologyReportToDischargeSummary(List<InvestigationOrderDto> orderList,
			Document document, HTMLWorker htmlWorker) {
		// for lab report
		try {

			List<InvestigationOrderDto> labReports = orderList.stream().filter(dto -> dto.getTypeId() == 1
					&& dto.getSampleStatusId() != null
					&& (dto.getSampleStatusId() == 6 || dto.getSampleStatusId() == 8 || dto.getSampleStatusId() == 17
							|| dto.getSampleStatusId() == 9 || dto.getSampleStatusId() == 10))
					.collect(Collectors.toList());
			
		    Map<Integer,InvestigationOrderDto> map=new HashMap();
		    List<InvestigationOrderDto> testList =new ArrayList();
		    for(InvestigationOrderDto test:labReports) {
		    	if(!map.containsKey(test.getLabTestResId())) {
		    		map.put(test.getLabTestResId(), test);
		    		testList.add(test);
		    	}
		    }
		    
		    
			List<InvestigationOrderDto> radilogyReport = orderList.stream().filter(dto -> dto.getTypeId() == 2)
					.collect(Collectors.toList());
			List<InvestigationOrderDto> labMicrobilogyTemplateReport = orderList.stream().filter(
					dto -> dto.getTypeId() == 1 && dto.getSampleStatusId() != null && dto.getSampleStatusId() == 41)
					.collect(Collectors.toList());
			Font normalFont = FontFactory.getFont("Times-Roman", 12, Font.NORMAL, BaseColor.BLACK);
			Font boldFontUnderline = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
			//boldFontUnderline.setStyle(Font.UNDERLINE);;
			Font boldFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
			Font boldFont11 = new Font(FontFamily.TIMES_ROMAN, 11, Font.BOLD);
			Map<String, Object> providers = new HashMap<String, Object>();
			String realPath = servletContext.getRealPath("/") + "resources" + File.separator + "assets" + File.separator
					+ "fonts" + File.separator + "Verdana10.ttf";
			Map<String, Object> providers2 = new HashMap<String, Object>();
			defaultFontProvider dfp = new defaultFontProvider(realPath);
			providers.put(HTMLWorker.FONT_PROVIDER, dfp);
			// providers.put(HTMLWorker., );
			htmlWorker.setProviders(providers);
			
			defaultFontProvider2 dfp2 = new defaultFontProvider2(realPath);
			providers2.put(HTMLWorker.FONT_PROVIDER, dfp);
			// providers.put(HTMLWorker., );
			htmlWorker.setProviders(providers);
			if ((labReports != null && !labReports.isEmpty())
					|| (labMicrobilogyTemplateReport != null && !labMicrobilogyTemplateReport.isEmpty())) {
				StringReader sr2 = new StringReader("<b>Investigation Done :</b>");
				htmlWorker.setProviders(providers2);
				htmlWorker.parse(sr2);
			}
			if (labReports != null && !labReports.isEmpty()) {


				PdfPTable table = new PdfPTable(4);
				table.setWidthPercentage(100);
				table.setWidths(new float[] { 40, 18, 32, 10 });
				table.setSpacingBefore(10);
				table.getDefaultCell().setPadding(0);
				
			
				// 1-left 2-right 3-top 4-bottom
				PdfPCell cell1 = new PdfPCell();
				cell1.setPaddingTop(-4);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				Paragraph col1Head = new Paragraph();
				col1Head.add("Test"); // Observed Value Biological Reference Interval Unit");
				col1Head.setFont(boldFont11);
				col1Head.setSpacingBefore(0);
				cell1.addElement(col1Head);
				cell1.enableBorderSide(1);
				cell1.enableBorderSide(3);
				cell1.enableBorderSide(4);
				table.addCell(cell1);

				
				PdfPCell cell2 = new PdfPCell();
				cell2.setPaddingTop(-4);
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				Paragraph col2Head = new Paragraph();
				col2Head.add("Observed Value");
				col2Head.setFont(boldFont11);
				col2Head.setSpacingBefore(0);
				cell2.addElement(col2Head);
				cell2.enableBorderSide(3);
				cell2.enableBorderSide(4);
				table.addCell(cell2);

				PdfPCell cell3 = new PdfPCell();
				cell3.setPaddingTop(-4);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				Paragraph col3Head = new Paragraph();
				col3Head.add("Biological Reference Interval"); // Observed Value Biological Reference Interval Unit");
				col3Head.setFont(boldFont11);
				col3Head.setSpacingBefore(0);
				cell3.addElement(col3Head);
				cell3.enableBorderSide(3);
				cell3.enableBorderSide(4);
				table.addCell(cell3);

				PdfPCell cell4 = new PdfPCell();
				cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell4.setPaddingTop(-4);
				Paragraph col4Head = new Paragraph();
				col4Head.add("Unit"); // Observed Value Biological Reference Interval Unit");
				col4Head.setFont(boldFont11);
				col4Head.setSpacingBefore(0);
				cell4.addElement(col4Head);
				cell4.enableBorderSide(3);
				cell4.enableBorderSide(4);
				cell4.enableBorderSide(2);
				table.addCell(cell4);
			
			
				document.add(table);
				Integer labRecCounter = 0;
				for (InvestigationOrderDto dto : testList) {
				
					table = new PdfPTable(4);
					table.setWidthPercentage(100);
					if(labRecCounter>0) {
						table.setSpacingBefore(8);
					}
					labRecCounter++;
					
					//table.setWidths(new float[] { 30, 20, 30, 20 });
					table.setWidths(new float[] { 40, 18, 32, 10 });
				
                    List<InvestigationOrderDto> multiParamTestList=labReports.stream()
                    .filter(t->t.getLabTestResId().equals(dto.getLabTestResId())&& t.getTestType()!=null && t.getTestType()==1 ).collect(Collectors.toList());
                    Boolean isMultiParamTestReport=false;
                    if(multiParamTestList!=null && !multiParamTestList.isEmpty()) {
                    	isMultiParamTestReport=true;
                    }
                   
                    //false means single param test
                    if(isMultiParamTestReport==false) {
                    	PdfPCell cont = new PdfPCell();
    					cont.setBorder(Rectangle.NO_BORDER);
    					cont.setPaddingTop(-4);
    					Paragraph testName = new Paragraph();
    					testName.add(dto.getServiceStandardName());
    					testName.setFont(boldFontUnderline);
    					cont.setColspan(4);
    					cont.addElement(testName);
    					table.addCell(cont);
    					
						PdfPCell col1 = new PdfPCell();
						col1.setPaddingTop(-4);
						col1.setBorder(Rectangle.NO_BORDER);
						col1.addElement(new Paragraph(dto.getServiceStandardName(), normalFont));
						table.addCell(col1);
						
						
						Paragraph para2=new Paragraph();
 						para2.setFont(boldFont);
						String res="";
 						
 						if(dto.getRefrangeTypeId() == 1) {
 							
 							if(dto.getResultValue()!=null && !dto.getResultValue().equals("-")) {
     							Double value=Double.valueOf(dto.getResultValue());
     							if(Double.valueOf(dto.getResultValue())>dto.getParameterMaxValue()) {
     								if(value>1) 
     								res="     High";
     								if(value>9) 
     								res="    High";
     								if(value>99)
     								res="   High";
     								if(value>9999)
         							res="  High";
     							}
     							if(Double.valueOf(dto.getResultValue())<dto.getParameterMinValue()) {
     								   if(value>1) 
         								res="     Low";
         							   if(value>9) 
         								res="    Low";
         								if(value>99)
         								res="   Low";
         								if(value>9999)
             							res="  Low";
     							}
     						}
 							
 						}else {
 							
 							Character isAb = 'Y';
 							if(dto.getIsAbnormal().equals(isAb)) {
     							res="  High";
 							}
 						}
 						
						PdfPCell col2 = new PdfPCell();
						col2.setBorder(Rectangle.NO_BORDER);
						col2.setPaddingTop(-4);
						
						if(!res.isEmpty()) {
	 						  para2.add(dto.getResultValue()+res);
	 						  col2.addElement(para2);
	 						}
	 						else
	 						col2.addElement(new Paragraph(dto.getResultValue(), normalFont));
	 						table.addCell(col2);
						
						//col2.addElement(new Paragraph(dto.getResultValue(), normalFont));
						table.addCell(col2);

						PdfPCell col3 = new PdfPCell();
						col3.setBorder(Rectangle.NO_BORDER);
						col3.setPaddingTop(-4);
						col3.addElement(
								new Paragraph((dto.getParameterMinValue() != null ? dto.getParameterMinValue()  : " " )+ " - " + (dto.getParameterMaxValue()  != null ? dto.getParameterMaxValue()  : " "), normalFont));
						table.addCell(col3);

						PdfPCell col4 = new PdfPCell();
						col4.setBorder(Rectangle.NO_BORDER);
						col4.setPaddingTop(-4);
						col4.addElement(new Paragraph(dto.getParameterUnit(), normalFont));
						table.addCell(col4);

                    }else {
                    	PdfPCell cont = new PdfPCell();
    					cont.setBorder(Rectangle.NO_BORDER);
    					cont.setPaddingTop(-4);
    					Paragraph testName = new Paragraph();
    					testName.add(dto.getServiceStandardName());
    					testName.setFont(boldFontUnderline);
    					cont.setColspan(4);
    					cont.addElement(testName);
    					table.addCell(cont);
    					
                    	Map<Integer, List<InvestigationOrderDto>> headerParamListMap=multiParamTestList.stream().collect(Collectors.groupingBy(InvestigationOrderDto::getHeaderId));
                    	for(Entry<Integer, List<InvestigationOrderDto>> mapEntry:headerParamListMap.entrySet())
                    	{
                    		PdfPCell headerCell = new PdfPCell();
                    		headerCell.setPaddingTop(-4);
                    		headerCell.setBorder(Rectangle.NO_BORDER);
        					Paragraph headerPara = new Paragraph();
        					headerPara.add(mapEntry.getValue().get(0).getHeaderName());
        					headerPara.setFont(boldFont);
        					headerCell.setColspan(4);
        					headerCell.addElement(headerPara);
        					table.addCell(headerCell);
        					List<InvestigationOrderDto> paramList=mapEntry.getValue();
                    	 for(InvestigationOrderDto param:paramList) {
                    		PdfPCell col1 = new PdfPCell();
     						col1.setBorder(Rectangle.NO_BORDER);
     						col1.setPaddingTop(-4);
     						col1.addElement(new Paragraph(param.getParameterName(), normalFont));
     						table.addCell(col1);
     						
     						PdfPCell col2 = new PdfPCell();
     						col2.setBorder(Rectangle.NO_BORDER);
     						col2.setPaddingTop(-4);
     						Paragraph para2=new Paragraph();
     						para2.setFont(boldFont);
     						String res="";
     						
     						if(param.getRefrangeTypeId() == 1) {
     							
     							if(param.getResultValue()!=null && !param.getResultValue().equals("-")) {
         							Double value=Double.valueOf(param.getResultValue());
         							if(Double.valueOf(param.getResultValue())>param.getParameterMaxValue()) {
         								if(value>1) 
         								res="     High";
         								if(value>9) 
         								res="    High";
         								if(value>99)
         								res="   High";
         								if(value>9999)
             							res="  High";
         							}
         							if(Double.valueOf(param.getResultValue())<param.getParameterMinValue()) {
         								   if(value>1) 
             								res="     Low";
             							   if(value>9) 
             								res="    Low";
             								if(value>99)
             								res="   Low";
             								if(value>9999)
                 							res="  Low";
         							}
         						}
     							
     						}else {
     							
     							Character isAb = 'Y';
     							if(param.getIsAbnormal().equals(isAb)) {
         							res="  High";
     							}
     						}
     						
     						if(!res.isEmpty()) {
     						  para2.add(param.getResultValue()+res);
     						  col2.addElement(para2);
     						}
     						else
     						col2.addElement(new Paragraph(param.getResultValue(), normalFont));
     						table.addCell(col2);

     						PdfPCell col3 = new PdfPCell();
     						col3.setBorder(Rectangle.NO_BORDER);
     						col3.setPaddingTop(-4);
     						col3.addElement(
     								//new Paragraph(param.getParameterMinValue() + " - " + param.getParameterMaxValue(), normalFont));
     								new Paragraph((param.getParameterMinValue() != null ? param.getParameterMinValue()  : " " )+ " - " + (param.getParameterMaxValue()  != null ? param.getParameterMaxValue()  : " "), normalFont));

     						table.addCell(col3);

     						PdfPCell col4 = new PdfPCell();
     						col4.setBorder(Rectangle.NO_BORDER);
     						col4.setPaddingTop(-4);
     						col4.addElement(new Paragraph(param.getParameterUnit(), normalFont));
     						table.addCell(col4);
                    	 }
                    	 
                    	}
                   
                    }

					
					// Remarks start here
                    if(dto.getResultRemark()!=null && !dto.getResultRemark().isEmpty()) {
						PdfPCell remarkRow = new PdfPCell();
						remarkRow.setBorder(Rectangle.NO_BORDER);
						remarkRow.setPaddingTop(-4);
						Paragraph remarkPara = new Paragraph();
						remarkPara.add("Remarks :");
						remarkPara.setFont(boldFont);
						remarkRow.setColspan(4);
						remarkRow.addElement(remarkPara);
						table.addCell(remarkRow);
	
						PdfPCell remarkRowCon = new PdfPCell();
						remarkRowCon.setBorder(Rectangle.NO_BORDER);
						remarkRowCon.setPaddingTop(-4);
						remarkRowCon.setColspan(4);
						remarkRowCon.addElement(new Paragraph(dto.getResultRemark(), normalFont));
						table.addCell(remarkRowCon);
                    }
					// Intepretation
                    if(dto.getFootNote()!=null && !dto.getFootNote().isEmpty()) {
						PdfPCell interRow = new PdfPCell();
						interRow.setBorder(Rectangle.NO_BORDER);
						interRow.setPaddingTop(-4);
						Paragraph interPara = new Paragraph();
						interPara.add("Interpretation :");
						interPara.setFont(boldFont);
						interRow.setColspan(4);
						interRow.addElement(interPara);
						table.addCell(interRow);
	
						PdfPCell interRowCon = new PdfPCell();
						interRowCon.setBorder(Rectangle.NO_BORDER);
						interRowCon.setPaddingTop(-4);
						interRowCon.setColspan(4);
						interRowCon.addElement(new Paragraph(dto.getFootNote(), normalFont));
						table.addCell(interRowCon);
						document.add(table);
                    }
				}
			}

			// lab microbiologry template
			Integer labTemRecCounter=0;
			for (InvestigationOrderDto dto : labMicrobilogyTemplateReport) {
				if(labTemRecCounter>1) {
					document.add(new Paragraph(""));
				}
				labTemRecCounter++;
				Paragraph radiologyServName = new Paragraph();
				radiologyServName.add(dto.getServiceStandardName());
				radiologyServName.setFont(normalFont);
				document.add(radiologyServName);
				String updateTempObj= dto.getTemplateObject() != null ? dto.getTemplateObject().replaceAll("<p", "<span><p").replaceAll("</p>", "</p></span>"):"";
				StringReader sr2 = new StringReader(updateTempObj);
				htmlWorker.setProviders(providers);
				StyleSheet css = new StyleSheet();
				css.loadTagStyle("span", "style", "line-height:110%;");
			    css.loadTagStyle("p", "style", "text-align:justify;text-justify:inter-word;");
				htmlWorker.setStyleSheet(css);
				htmlWorker.parse(sr2);
			}

			// Radiology report
			Integer radiTemRecCounter=0;
			if (radilogyReport != null && !radilogyReport.isEmpty()) {
//			Paragraph radilogyHeading=new  Paragraph();
//			radilogyHeading.add("Radiology Report :"); 
//			radilogyHeading.setFont(baseFont);
//			document.add(radilogyHeading);
				if(radiTemRecCounter>1) {
					document.add(new Paragraph(""));
				}
				radiTemRecCounter++;
				StringReader radiologyMainHead = new StringReader("<b>Radiology Report :</b>");
				htmlWorker.parse(radiologyMainHead);
				for (InvestigationOrderDto dto : radilogyReport) {
					Paragraph radiologyServName = new Paragraph();
					radiologyServName.add(dto.getServiceStandardName());
					radiologyServName.setFont(normalFont);
					document.add(radiologyServName);
					String updateTempObj=dto.getTemplateObject() != null ?dto.getTemplateObject().replaceAll("<p", "<span><p").replaceAll("</p>", "</p></span>"):"";
					StringReader tempCont = new StringReader(updateTempObj);
					htmlWorker.setProviders(providers);
					StyleSheet css = new StyleSheet();
					css.loadTagStyle("span", "style", "line-height:110%;");
				    css.loadTagStyle("p", "style", "text-align:justify;text-justify:inter-word;");
					htmlWorker.setStyleSheet(css);
					htmlWorker.parse(tempCont);
				}
			}

		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			
		}
	}

	private ByteArrayOutputStream getAllLabReports(DischargeSummaryDto dischargeSummaryDto) throws IOException {

		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	       try {
		   String reportParam="";
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	      // String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(dischargeSummaryDto.getOrganizationId(),dischargeSummaryDto.getUnitId());
	       if(dischargeSummaryDto.getCrystalReportUrl()==null)
	    	   return null;
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'pathologytestListForDischargesummary.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'admission_id',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+dischargeSummaryDto.getAdmissionId()
	    		           +"}" 
	    		             
	    	            +"]"
	    	            +"}" ; 
	     
	      
	       HttpPost  post = new HttpPost(dischargeSummaryDto.getCrystalReportUrl());
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return  byteOutPutStream;
	       }catch(Exception e)
		   { 
			   
		   }
		   finally {
			   if(output!=null)
				   output.close();
		       if(httpClient!=null)
		    	   httpClient.close();
		       if(byteOutPutStream!=null)
		    	   byteOutPutStream.close();
		    }
		   return null;
	}

	private List<com.param.adt.discharge.dto.InvestigationOrderDto> getInvestigationReport(Integer admissionId,List<Integer> orderIds) {
		/*StringBuilder strBlr=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_CUEERENT_ORDERED_INVESTIGATION").getQueryString());
		strBlr.append(" where inv.\"admissionId\"="+admissionId);
		List<com.param.adt.discharge.dto.InvestigationOrderDto> investigationDTOsList=sessionFactory.getCurrentSession().createSQLQuery(strBlr.toString())
				.setResultTransformer(Transformers.aliasToBean(com.param.adt.discharge.dto.InvestigationOrderDto.class)).list();
		
		
		
		
		*/
		
				StringBuilder sb = new  StringBuilder();
		 			sb.append(" select "  + 
		 					" ord_mst.order_id as \"orderId\", " + 
		 					" service_mst.speciality_id as \"specialityId\"," + 
		 					" service_mst.sub_speciality_id as \"subDeptId\"," + 
		 					" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
		 					" ord_mst.admission_id as \"admissionId\", " + 
		 					" ord_mst.encounter_id as \"encounterId\", " + 
		 					" ord_mst.patient_id as \"patientId\", " + 
		 					" ord_dtls.order_details_id as \"orderDetailsId\", " + 
		 					" ord_dtls.service_id as \"serviceMasterId\", " + 
		 					" ord_dtls.order_set_id as \"orderSetId\", " + 
		 					" ord_dtls.priority_id as \"priorityId\", " + 
		 					" ord_dtls.ord_remarks as \"ordRemarks\", " + 
		 					" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
		 					" service_mst.service_standard_name as \"serviceStandardName\", " + 
		 					" service_mst.service_standard_code as \"serviceStandardCode\", " + 
		 					" speciality_mst.speciality_name as \"specialityName\", " + 
		 					" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
		 					" service_dtls.is_panel as \"isPanel\", " + 
		 					" lab_res.lab_test_res_id  as \"labTestResId\", " + 
		 					" cast( " + 
		 					" 	null as integer " + 
		 					" ) as \"panelId\", " + 
		 					" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
		 					" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
		 					" to_char( " + 
		 					" 	ord_dtls.created_date, " + 
		 					" 	'dd/MM/yyyy' " + 
		 					" ) as \"createdDate\", " + 
		 					" to_char( " + 
		 					" 	ord_dtls.created_date, " + 
		 					" 	'yyyy/mm/dd' " + 
		 					" ) as \"createdDateNew\", " + 
		 					" ord_dtls.service_rendered as \"serviceRendered\", " + 
		 					" ord_dtls.is_package_service as \"isPackageService\", " + 
		 					" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
		 					" (case when (sample_dtls.micro_template_status_id is not null and sample_dtls.micro_template_status_id=41)" + 
		 					" then microTemp.template_object else templatetr12_.template_object end) as \"templateObject\", " + 
		 					" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
		 					" ord_dtls.status as \"orderStatus\", " + 
		 					" am.appointment_id as \"appointmentId\", " + 
		 					" am.slot_id as \"slotId\", " +
		 					 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , " 
		 					+" (case " + 
		 					" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
		 					" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
		 					" else 'Admin' end) as \"userName\"," + 
		 					" ord_dtls.package_id as \"packageId\","+
		 					" lab_res_det.final_result as \"resultValue\"," + 
		 					" lab_res_det.parameter_min  as \"parameterMinValue\"," + 
		 					" lab_res_det.parameter_max  as \"parameterMaxValue\"," + 
		 					" lab_res_det.param_unit as  \"parameterUnit\"," + 
		 					" 1 as \"typeId\",lab_res_det.remarks as \"resultRemark\",mtm.test_type as \"testType\",lab_res_det.param_name as \"parameterName\","+
		 					"mhm.header_id as \"headerId\",mhm.header_desc as \"headerName\",mtm.foot_note as \"footNote\","
		 					+ "lab_res_det.is_abnormal  as \"isAbnormal\", " + 
		 					"lab_res_det.refrange_type_id as \"refrangeTypeId\" " + 
		 					" from 	public.t_order_master ord_mst " + 
		 					" 		inner join public.t_order_details ord_dtls on " + 
		 					" ord_mst.order_id = ord_dtls.order_id " + 
		 					" 		inner join service.t_unit_service_mapper service_mppr on " + 
		 					" service_mppr.service_id = ord_dtls.service_id " + 
		 					" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
		 					" 		inner join service.t_unit_service_master_details service_dtls on " + 
		 					" service_dtls.service_master_id = service_mppr.service_id " + 
		 					" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
		 					" 		inner join service.m_service_master service_mst on " + 
		 					" service_mst.service_master_id = ord_dtls.service_id " + 
		 					" 		left join doctor.m_speciality_master speciality_mst on " + 
		 					" speciality_mst.speciality_id = service_mst.speciality_id " + 
		 					" 		left join t_unit_speciality_mapper unitSplMpr on " + 
		 					" unitSplMpr.speciality_id = service_mst.speciality_id " + 
		 					" and unitSplMpr.unit_id = service_mppr.unit_id " + 
		 					" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
		 					" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
		 					" and sample_dtls.sample_status_id <> 4 " + 
		 					" 		left join lab.m_test_status_master test_status on " + 
		 					" test_status.test_status_id = sample_dtls.sample_status_id " + 
		 					"        	left join lab.m_test_status_master test_status2 on " + 
		 					" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
		 					" 	left join lab.t_lab_test_result lab_res on " + 
		 					" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
		 					" AND  lab_res.status = 'A' "+
		 					
		 					" left join lab.t_lab_res_dtls lab_res_det ON lab_res.lab_test_res_id = lab_res_det.lab_result_id"+
		 					" left join lab.m_test_master mtm on mtm.test_id =lab_res.test_id" + 
		 					"  left join lab.m_header_master mhm on mhm.header_id =lab_res_det.header_id "+
		 					" left outer join public.t_template_transaction templatetr12_ on " + 
		 					" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
		 					" and( " + 
		 					" 	templatetr12_.status = 'A' " + 
		 					" ) " + 
		 					" 		left join opd.m_appointment_master am on " + 
		 					" am.order_details_id = ord_dtls.order_details_id " + 
		 					" 		left join t_unit_application_billing_configuration uabc on " + 
		 					" uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A' " + 
		 					" left join public.m_user_master um on ord_mst.created_by = um.user_id " + 
		 					" left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
		 					" left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id " + 
		 					" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id "
		 					+ "  left join (select is_rendering_speciality " + 
		 					"from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) temp on temp.is_rendering_speciality = unitSplMpr.is_rendering_speciality " + 
		 					" 		where " + 
		 					" service_mst.status = 'A' " + 
		 					" and ord_dtls.old_ord_dtl_id is null " + 
		 					" and service_dtls.is_package = 'I' " + 
		 					" and ord_dtls.nursing_service_order_flag = 'N' " + 
		 					" and service_dtls.is_panel = 'N' " + 
			 				" and ord_dtls.is_procedure_service = 'N' and (lab_res.sample_status_id=6 "
		 					+ " or lab_res.sample_status_id=8 or lab_res.sample_status_id=17 or lab_res.sample_status_id=9 or lab_res.sample_status_id=10 "
		 					+ " or sample_dtls.micro_template_status_id=41 ) "+
		 					" and ord_dtls.order_details_id in(:orderIds) ");   
				       
				        	sb.append(" AND ord_mst.admission_id=:admissionId ");
				        
				        	sb.append(" union select "  + 
				 					" ord_mst.order_id as \"orderId\", " + 
				 					" service_mst.speciality_id as \"specialityId\"," + 
				 					" service_mst.sub_speciality_id as \"subDeptId\"," + 
				 					" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
				 					" ord_mst.admission_id as \"admissionId\", " + 
				 					" ord_mst.encounter_id as \"encounterId\", " + 
				 					" ord_mst.patient_id as \"patientId\", " + 
				 					" ord_dtls.order_details_id as \"orderDetailsId\", " + 
				 					" ord_dtls.service_id as \"serviceMasterId\", " + 
				 					" ord_dtls.order_set_id as \"orderSetId\", " + 
				 					" ord_dtls.priority_id as \"priorityId\", " + 
				 					" ord_dtls.ord_remarks as \"ordRemarks\", " + 
				 					" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
				 					" service_mst.service_standard_name as \"serviceStandardName\", " + 
				 					" service_mst.service_standard_code as \"serviceStandardCode\", " + 
				 					" speciality_mst.speciality_name as \"specialityName\", " + 
				 					" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
				 					" service_dtls.is_panel as \"isPanel\", " + 
				 					" lab_res.lab_test_res_id  as \"labTestResId\", " + 
				 					" cast( " + 
				 					" 	null as integer " + 
				 					" ) as \"panelId\", " + 
				 					" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
				 					" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
				 					" to_char( " + 
				 					" 	ord_dtls.created_date, " + 
				 					" 	'dd/MM/yyyy' " + 
				 					" ) as \"createdDate\", " + 
				 					" to_char( " + 
				 					" 	ord_dtls.created_date, " + 
				 					" 	'yyyy/mm/dd' " + 
				 					" ) as \"createdDateNew\", " + 
				 					" ord_dtls.service_rendered as \"serviceRendered\", " + 
				 					" ord_dtls.is_package_service as \"isPackageService\", " + 
				 					" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
				 					" (case when (sample_dtls.micro_template_status_id is not null and sample_dtls.micro_template_status_id=41)" + 
				 					" then microTemp.template_object else templatetr12_.template_object end) as \"templateObject\", " + 
				 					" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
				 					" ord_dtls.status as \"orderStatus\", " + 
				 					" am.appointment_id as \"appointmentId\", " + 
				 					" am.slot_id as \"slotId\", " +
				 					 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , " 
				 					+" (case " + 
				 					" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
				 					" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
				 					" else 'Admin' end) as \"userName\"," + 
				 					" ord_dtls.package_id as \"packageId\","+
				 					" '' as \"resultValue\"," + 
				 					" 0  as \"parameterMinValue\"," + 
				 					" 0  as \"parameterMaxValue\"," + 
				 					" '' as  \"parameterUnit\"," + 
				 					//" 2 as \"typeId\",'' as \"resultRemark\" " + 
				 					" 2 as \"typeId\",'' as \"resultRemark\",0 as \"testType\",'' as \"parameterName\","+
				 					"0 as \"headerId\",'' as \"headerName\",'' as \"footNote\", " +
				 					"'N'  as \"isAbnormal\", " + 
				 					"0 as \"refrangeTypeId\" " + 
				 					" from 	public.t_order_master ord_mst " + 
				 					" 		inner join public.t_order_details ord_dtls on " + 
				 					" ord_mst.order_id = ord_dtls.order_id " + 
				 					" 		inner join service.t_unit_service_mapper service_mppr on " + 
				 					" service_mppr.service_id = ord_dtls.service_id " + 
				 					" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
				 					" 		inner join service.t_unit_service_master_details service_dtls on " + 
				 					" service_dtls.service_master_id = service_mppr.service_id " + 
				 					" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
				 					" 		inner join service.m_service_master service_mst on " + 
				 					" service_mst.service_master_id = ord_dtls.service_id " + 
				 					" 		left join doctor.m_speciality_master speciality_mst on " + 
				 					" speciality_mst.speciality_id = service_mst.speciality_id " + 
				 					" 		left join t_unit_speciality_mapper unitSplMpr on " + 
				 					" unitSplMpr.speciality_id = service_mst.speciality_id " + 
				 					" and unitSplMpr.unit_id = service_mppr.unit_id " + 
				 					" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
				 					" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
				 					" and sample_dtls.sample_status_id <> 4 " + 
				 					" 		left join lab.m_test_status_master test_status on " + 
				 					" test_status.test_status_id = sample_dtls.sample_status_id " + 
				 					"        	left join lab.m_test_status_master test_status2 on " + 
				 					" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
				 					" 	left join lab.t_lab_test_result lab_res on " + 
				 					" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
				 					" AND  lab_res.status = 'A' "+
				 					" 		inner  join public.t_template_transaction templatetr12_ on " + 
				 					" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
				 					" and( " + 
				 					" 	templatetr12_.status = 'A' " + 
				 					" ) " + 
				 					" 		left join opd.m_appointment_master am on " + 
				 					" am.order_details_id = ord_dtls.order_details_id " + 
				 					" 		left join t_unit_application_billing_configuration uabc on " + 
				 					" uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A' " + 
				 					" left join public.m_user_master um on ord_mst.created_by = um.user_id " + 
				 					" left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
				 					" left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id " + 
				 					" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id "  
				 					+ "  left join (select is_rendering_speciality " + 
				 					"from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) temp on temp.is_rendering_speciality = unitSplMpr.is_rendering_speciality " +
				 					" 		where " + 
				 					" service_mst.status = 'A' " + 
				 					" and ord_dtls.old_ord_dtl_id is null " + 
				 					" and service_dtls.is_package = 'I' " + 
				 					" and ord_dtls.nursing_service_order_flag = 'N' " + 
				 					" and service_dtls.is_panel = 'N' " 
				 					//" and unitSplMpr.is_rendering_speciality = 'Y' " + 
				 					// " and unitSplMpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
				 					+" and ord_dtls.is_procedure_service = 'N'   "+//and templatetr12_.is_final_release is not null and templatetr12_.is_final_release = 'Y'
				 					" and ord_dtls.order_details_id in(:orderIds) ");   
						       
						        	sb.append(" AND ord_mst.admission_id=:admissionId ");
				          
	        
	        Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		  createdQuery.setParameterList("orderIds", orderIds);
		   createdQuery.setParameter("admissionId", admissionId);
		List<com.param.adt.discharge.dto.InvestigationOrderDto> investigationDTOsList = createdQuery.setResultTransformer(Transformers.aliasToBean(com.param.adt.discharge.dto.InvestigationOrderDto.class)).list();
		
	        return investigationDTOsList;

	}

	static void mergePdfFiles(List<InputStream> inputPdfList,
			PdfWriter pdfWriter,Document document) throws Exception{
 
        List<PdfReader> readers = 
        		new ArrayList<PdfReader>();
        int totalPages = 0;
 
        //Create pdf Iterator object using inputPdfList.
        Iterator<InputStream> pdfIterator = 
        		inputPdfList.iterator();
 
        // Create reader list for the input pdf files.
        while (pdfIterator.hasNext()) {
                InputStream pdf = pdfIterator.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages = totalPages + pdfReader.getNumberOfPages();
        }
 
        //Contain the pdf data.
        PdfContentByte pageContentByte = pdfWriter.getDirectContent();
 
        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();
 
        // Iterate and process the reader list.
        while (iteratorPDFReader.hasNext()) {
          PdfReader pdfReader = iteratorPDFReader.next();
          //Create page and add content.
          while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                      document.newPage();
                      pdfImportedPage = pdfWriter.getImportedPage(
                    		  pdfReader,currentPdfReaderPage);
                      pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                      currentPdfReaderPage++;
          }
          currentPdfReaderPage = 1;
        }
 
        System.out.println("Pdf files merged successfully.");
       }
	
	public ByteArrayOutputStream labReport(LabReportReqDto labReportReqDto) throws IOException {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	       try {
		   String reportParam="";
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(labReportReqDto.getOrganizationId(),labReportReqDto.getUnitId());
	       if(API_URL==null)
	    	   return null;
	       Response labRes=iLabConfigurationDao.getLabConfigurationByOrgUnit(labReportReqDto.getOrganizationId(),labReportReqDto.getUnitId());
	       LabConfigurationDto labConfigDto=(LabConfigurationDto) labRes.getObject();
	       
	       if(labConfigDto.getBiochemistryId().equals(labReportReqDto.getSubDeptId()) ||
	    		   labConfigDto.getClinicalPathologyId().equals(labReportReqDto.getSubDeptId()) || 
	    		   labConfigDto.getHematologyId().equals(labReportReqDto.getSubDeptId())) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'pathologyReport.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'labtesresid',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabTestResId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ; 
	       }else if(labConfigDto.getMicrobiologyId().equals(labReportReqDto.getSubDeptId())) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'pathologyReport_Sensitivity.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'lab_sample_dtls_id',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabSampleDtlsId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ;
	       }else if(labConfigDto.getHistopathologyId().equals(labReportReqDto.getSubDeptId()) ||
	    		   labConfigDto.getCystopathologyId().equals(labReportReqDto.getSubDeptId()) ) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'GetHistopathologyReport.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'labtesresid',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabSampleDtlsId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ;
	       }else if(labConfigDto.getSerologyId().equals(labReportReqDto.getSubDeptId())) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'SerologyReport.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'lab_sample_dtls_id',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabSampleDtlsId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ;
	       }
	     
	      
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return  byteOutPutStream;
	       }catch(Exception e)
		   { 
			   
		   }
		   finally {
			   if(output!=null)
				   output.close();
		       if(httpClient!=null)
		    	   httpClient.close();
		       if(byteOutPutStream!=null)
		    	   byteOutPutStream.close();
		    }
		   return null;
	}
	
	
	@Override
	public Response getPatientDischargeSummaryTemplateData(DischargeSummaryDto dischargeSummaryDto) {
		try {
	
				//tabular template code 
				List<DischargeSummaryDto> dischargeSummFinalFieldList=new ArrayList<DischargeSummaryDto>();
				Character status='A';
	    		Query sql =  sessionFactory.getCurrentSession()
	    				.createSQLQuery(
	    						"select fieldmaster.discharge_field_id as \"dischargeFieldId\" ,field1.discharge_changed_name as \"dischargeFieldName\","
	    						+ " fieldMaster.discharge_field_template_obj as \"dischargeFieldDescription\", " 
	    						+ "temp.discharge_id as \"dischargeTemplateId\" " 
	    						+ "FROM adt.m_discharge_summary_field_master fieldmaster "  
	    						+ "inner join adt.t_discharge_summary_template_mapper tempmapper "
	    						+ "on fieldmaster.discharge_field_id=tempmapper.discharge_field_id "
	    						+ "inner join adt.m_discharge_summary_template_master temp "
	    						+ "on temp.discharge_id=tempmapper.discharge_summary_temp_id "
								+ "inner join public.m_discharge_field_unit_wise field1 "
								+ "on fieldmaster.discharge_field_id = field1.discharge_field_id  "
	    						+ "where tempmapper.discharge_summary_temp_id=:dischargeTemplateId "
								+"AND tempmapper.organization_id=:organizationId "
	    						+ " AND tempmapper.unit_id=:unitId "
	    						+ " AND tempmapper.status=:status"
	    						+ " AND fieldmaster.status=:status "
	    						+ " AND field1.status=:status "
	    						+ " AND field1.organization_id=:organizationId "
	    						+ "  and field1.unit_id  =:unitId  "
	    						+ " order by tempmapper.discharge_summary_temp_mppr_id asc"
	    						)
			.setParameter("dischargeTemplateId", dischargeSummaryDto.getDischargeTemplateId())
			.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
			.setParameter("unitId", dischargeSummaryDto.getUnitId())
			.setParameter("status", status);
			List<DischargeSummaryDto> templateFieldList= sql.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
	    		
				DischargeSummaryDto dischargeSummaryFieldDto = new DischargeSummaryDto();
				DischargeSummaryDto dischargeSummaryDto1 = (DischargeSummaryDto) sessionFactory.getCurrentSession()
						.createSQLQuery("select dis.discharge_summary_id as \"dischargeSummaryId\", "
								+ " dis.discharge_template_id as \"dischargeTemplateId\","
								+ " dis.is_report_release as \"isDischargeSummaryRelease\"  "
								+ "FROM adt.m_discharge_summary dis " 
								+ " where dis.status='A' AND dis.admission_id=:admissionId "
								+ " AND dis.organization_id=:organizationId "
								+ " AND dis.unit_id=:unitId ")
						.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
						.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).uniqueResult();
				if (dischargeSummaryDto1 != null) {
					dischargeSummaryFieldDto.setDischargeSummaryId(dischargeSummaryDto1.getDischargeSummaryId());
				}
			List<DischargeSummaryDto> dischargeSummaryFieldList =new ArrayList<>();
			if(dischargeSummaryFieldDto.getDischargeSummaryId() != null) {
				Query sql1 = sessionFactory.getCurrentSession().createSQLQuery(
								"select disSumm.discharge_summary_id as \"dischargeSummaryId\",fieldMaster.discharge_field_id as \"dischargeFieldId\" ,"
										+ " field1.discharge_changed_name as \"dischargeFieldName\","
										+ " disDetail.discharge_summary_data_view_option_id as \"dischargeSummaryDataViewOptionId\","
										+ " ((case when disDetail.discharge_field_description != '' then disDetail.discharge_field_description " +
										"	else (" +
										"	case when fieldMaster.discharge_field_template_obj is not null then fieldMaster.discharge_field_template_obj " +
										"	else ('')" +
										"	end)" +
										"	end)) as \"dischargeFieldDescription\" "
										+ "FROM adt.m_discharge_summary_field_master fieldMaster "
										+ "inner join adt.m_discharge_summary_detail disDetail "
										+ "on disDetail.discharge_field_id=fieldMaster.discharge_field_id "
										+ "inner join public.m_discharge_field_unit_wise field1 "
										+ "on fieldMaster.discharge_field_id = field1.discharge_field_id  "
										+ "inner join adt.m_discharge_summary disSumm "
										+ "on disDetail.discharge_summary_id=disSumm.discharge_summary_id "
										+ "left join adt.m_discharge_summary_template_master disSummTemp "
										+ "on disSummTemp.discharge_id=disSumm.discharge_template_id "
										+ "where disDetail.status='A'"
										+ " AND disDetail.discharge_summary_id=:dischargeSummaryId "
										+ " AND disDetail.organization_id=:organizationId "
										+ " AND field1.organization_id=:organizationId "
										+ " AND disDetail.unit_id=:unitId ")
						.setParameter("dischargeSummaryId", dischargeSummaryFieldDto.getDischargeSummaryId())
						.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryDto.getUnitId());
				 dischargeSummaryFieldList = sql1.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
			}
		    		
					for(DischargeSummaryDto dto:templateFieldList) {
						DischargeSummaryDto dto1=new DischargeSummaryDto();
						dto1.setDischargeTemplateId(dto.getDischargeTemplateId());
						dto1.setDischargeFieldId(dto.getDischargeFieldId());
						dto1.setDischargeFieldName(dto.getDischargeFieldName());
						Optional<String> fieldDiscription= null;
						Optional<Integer> id = null;
						if(dischargeSummaryFieldDto.getDischargeSummaryId() != null) {
							fieldDiscription=dischargeSummaryFieldList.stream()
									.filter(f->f.getDischargeFieldId().equals(dto.getDischargeFieldId()))
							.map(m->m.getDischargeFieldDescription()).findAny();
						}else {
							fieldDiscription=templateFieldList.stream()
									.filter(f->f.getDischargeFieldId().equals(dto.getDischargeFieldId()))
							.map(m->m.getDischargeFieldDescription()!= null ? m.getDischargeFieldDescription() :"").findAny();
						}
						
						if(dischargeSummaryFieldList != null && dischargeSummaryFieldList.size() > 0) {
							
							if(dischargeSummaryFieldDto.getDischargeSummaryId() != null) {
								id=dischargeSummaryFieldList.stream()
										.filter(f->f.getDischargeFieldId().equals(dto.getDischargeFieldId()))
								.map(m->m.getDischargeSummaryDataViewOptionId() != null ? m.getDischargeSummaryDataViewOptionId() :1).findAny();
							}
						}
						
						
						if(fieldDiscription.isPresent()) {
							dto1.setDischargeFieldDescription(fieldDiscription.get());
						}else {
							dto1.setDischargeFieldDescription("");
						}
						
						if(id != null &&id.isPresent() ) {
							dto1.setDischargeSummaryDataViewOptionId(id.get());
						}else {
							dto1.setDischargeSummaryDataViewOptionId(1);
						}
						dischargeSummFinalFieldList.add(dto1);
					}
				
				if(dischargeSummaryDto1!=null && dischargeSummaryDto1.getDischargeTemplateId().equals(dischargeSummaryDto.getDischargeTemplateId())) {
					if(dischargeSummaryDto1.getIsDischargeSummaryRelease()!=null && dischargeSummaryDto1.getIsDischargeSummaryRelease().equals('Y')) {
						dischargeSummaryDto.setIsSaveDisable(true);
						dischargeSummaryDto.setDisableReleaseBtn(true);
						dischargeSummaryDto.setDisablePrintBtn(false);
						if(dischargeSummaryDto1.getIsFollowUpAppointment()!=null && dischargeSummaryDto1.getIsFollowUpAppointment().equals('Y')) {
						  dischargeSummaryDto.setDisableFollowupBtn(false);
						}else {
							dischargeSummaryDto.setDisableFollowupBtn(true);
						}
					}else {
						dischargeSummaryDto.setIsSaveDisable(false);
						dischargeSummaryDto.setDisableReleaseBtn(false);
						dischargeSummaryDto.setDisablePrintBtn(true);
						dischargeSummaryDto.setDisableFollowupBtn(false);
					}
					dischargeSummaryDto.setDischargeSummaryId(dischargeSummaryDto1.getDischargeSummaryId());
				}else {
					dischargeSummaryDto.setIsSaveDisable(false);
					dischargeSummaryDto.setDisablePrintBtn(true);
					dischargeSummaryDto.setDisableFollowupBtn(false);
					dischargeSummaryDto.setDisableReleaseBtn(false);
				}
				
				for(DischargeSummaryDto dto : dischargeSummFinalFieldList){
				List<DischargeSummaryTabularTemplateDataDto> dischargeSummaryTemplateListdata = sessionFactory.getCurrentSession().createSQLQuery(
						"  select template_transaction.discharge_summary_tabular_template_id as  \"dischargeSummaryTabularTemplateId\",       " + 
//						"  to_char(template_transaction.created_date, 'dd-M-yyyy  HH:mm:ss') as \"createdDate\" ,       " + 
//						"  to_char(template_transaction.updated_date, 'dd-M-yyyy  HH:mm:ss') as \"updatedDate\" ,       " + 
						"  template_transaction.updated_by as \"updatedBy\" ,       " + 
						"  template_transaction.created_by as \"createdBy\" ,       " + 
						"  template_transaction.organization_id as \"organizationId\" ,       " + 
						"  template_transaction.unit_id as \"unitId\" ,       " + 
						"  template_transaction.table_type_id  as \"tableTypeId\",      " + 
						"  template_transaction.discharge_template_id as \"dischargeTemplateId\",      " + 
						"  template_transaction.patient_id as \"patientId\",      " + 
						"  template_transaction.doctor_id as \"doctorId\",      " + 
						"  template_transaction.admission_id as \"admissionId\",      " + 
						"  template_transaction.column_name as \"columnName\",      " + 
						"  template_transaction.coulmn_id as \"coulmnId\",      " + 
						"  template_transaction.row_id as \"rowId\",      " + 
						"  template_transaction.row_name as \"rowName\",      " + 
						"  template_transaction.input_value as \"inputValue\",      " + 
						"  template_transaction.number_of_coulmn as \"numberOfCoulmn\",      " + 
						"  template_transaction.number_of_row as \"numberOfRow\",      "+
						"  template_transaction.table_name as \"tableName\", " + 
						"  template_transaction.department_id as \"departmentId\"    " + 
						"  from adt.m_discharge_summary_tabular_template_data template_transaction       " + 
						"   where       " + 
						"  template_transaction.organization_id =:organizationId " +
						"  and template_transaction.unit_id =:unitId " +
						"  and template_transaction.discharge_template_id =:dischargeTemplateId " +
						"  and template_transaction.admission_id =:admissionId "+
						"  and template_transaction.discharge_field_id =:dischargeFieldId " +
						"  and template_transaction.status = 'A'")
						.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryDto.getUnitId())
						.setParameter("dischargeTemplateId", dischargeSummaryDto.getDischargeTemplateId())
						.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
						.setParameter("dischargeFieldId", dto.getDischargeFieldId())
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTabularTemplateDataDto.class)).list();
				
				dto.setDischargeSummaryTemplateListdata(dischargeSummaryTemplateListdata);
				}
		    		if (dischargeSummFinalFieldList.size() > 0) {
						return new Response(SUCCESS, SUCCESS_CODE, null, dischargeSummFinalFieldList, dischargeSummaryDto);
					} else {
						return new Response(ERROR, SUCCESS_CODE, null, dischargeSummFinalFieldList, dischargeSummaryDto);
					}
//			}
			
				
		} catch (HibernateException he) {
			he.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	
	}

	@Override
	public Response inactiveDischargeSummaryTemplates(DischargeSummaryDto dischargeSummaryDto) {
		try {
			int count = sessionFactory.getCurrentSession()
					.createSQLQuery(" update adt.m_discharge_summary set status ='I' "
							+ " where admission_id =:admId and discharge_template_id !=:tempId")
					.setParameter("admId", dischargeSummaryDto.getAdmissionId())
					.setParameter("tempId", dischargeSummaryDto.getDischargeTemplateId()).executeUpdate();
			if (count > 0) {
				Integer disSummaryId = (Integer) sessionFactory.getCurrentSession()
						.createSQLQuery("select mds.discharge_summary_id from adt.m_discharge_summary mds "
								+ "where mds.admission_id =:admId and mds.discharge_template_id !=:tempId "
								+ " order by mds.discharge_summary_id desc limit 1")
						.setParameter("admId", dischargeSummaryDto.getAdmissionId())
						.setParameter("tempId", dischargeSummaryDto.getDischargeTemplateId()).uniqueResult();

				sessionFactory.getCurrentSession()
						.createSQLQuery(" update adt.m_discharge_summary_detail set status ='I' "
								+ "where discharge_summary_id=:disSummId ")
						.setParameter("disSummId", disSummaryId).executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	public Response getDischargeSummaryPrintType(DischargeSummaryDto dischargeSummaryDto) {
		try{
			List<DischargeSummaryDto> list =  sessionFactory.getCurrentSession()
					.createSQLQuery(
							"select " + 
							"	distinct disSumm.discharge_summary_id as \"dischargeSummaryId\", " + 
							"	disSumm.discharge_template_id as \"dischargeTemplateId\", " + 
							"	disTemp.is_html as \"isHtml\" " + 
							"from " + 
							"	adt.t_discharge discharge " + 
							"inner join adt.t_admission as admission on " + 
							"	discharge.admission_id = admission.admission_id " + 
							"left join adt.m_discharge_summary disSumm on " + 
							"	disSumm.admission_id = admission.admission_id " + 
							"	and disSumm.status = 'A' " + 
							"left join adt.m_discharge_summary_template_master disTemp on " + 
							"	disTemp.discharge_id = disSumm.discharge_template_id " + 
							"where " + 
							"	discharge.organization_id =:organizationId "+
							"	and discharge.unit_id =:unitId "+
							" and disSumm.discharge_summary_id =:dischargeSummaryId "
							)
			        .setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryDto.getUnitId())
					.setParameter("dischargeSummaryId", dischargeSummaryDto.getDischargeSummaryId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE,null, list, null);
			}
			catch(HibernateException he){
			}
			catch (Exception e) {
				
			}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateExtendPdd(AdmissionDto dto) {
		try {
		
			Query query = sessionFactory.getCurrentSession().createQuery(
					"update AdmissionDetails dis set dis.pdd=:pdd " + "where dis.admissionId=:admissionId");

			query.setParameter("admissionId", dto.getAdmissionId());
			query.setParameter("pdd", GlobalCommonDateUtils.getDate(dto.getPdd(), "dd-M-yyyy HH:mm:ss"));
			query.executeUpdate();
			
	
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAdmittedPatientList(AdmissionDto dto) {
		try {
			 if(dto.getMealPrefId()!= null) {
					Query query = sessionFactory.getCurrentSession().createQuery(
							" update AdmissionDetails dis set dis.mealPrefId=:mealPrefId"
							+ " where dis.admissionId=:admissionId and dis.status='A'");
				
					query.setParameter("mealPrefId", dto.getMealPrefId()!= null ? dto.getMealPrefId() : null);
				 query.setParameter("admissionId", dto.getAdmissionId());
					query.executeUpdate();

				}
			  if (dto.getDietTypeId() != null){

			Query query = sessionFactory.getCurrentSession().createQuery(
					" update AdmissionDetails dis set dis.dietType=:dietTypeId "
					+ " where dis.admissionId=:admissionId and dis.status='A'");
		
			query.setParameter("dietTypeId", dto.getDietTypeId()!= null ? dto.getDietTypeId() : null);
			query.setParameter("admissionId", dto.getAdmissionId());
			query.executeUpdate();
			}
			 
			  if(!dto.getDietRemarkDesc().isEmpty())
			 {
				 Query query = sessionFactory.getCurrentSession().createQuery(
							" update AdmissionDetails dis set dis.remarkForDiet=:dietRemarkDesc "
							+ " where dis.admissionId=:admissionId and dis.status='A'");
				
					query.setParameter("dietRemarkDesc", dto.getDietRemarkDesc() != null ? dto.getDietRemarkDesc() : null);
					query.setParameter("admissionId", dto.getAdmissionId());
					query.executeUpdate();
					}
		
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
		System.out.println("Hibernate Exception : " + he);
			
			} catch (Exception e) {
				System.out.println("Exception : " +e);
				
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response sendDischargeSummaryReportToMyLife(DischargeSummaryDto dto) {
		try {
			OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) iGlobalOrganisationMasterDao
					.getOrganiazationFolderPath(dto.getOrganizationId()).getObject();
//			OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();

			String uuid = iPatientRegistrationService.getUUIDByPatientId(dto.getPatientId());

			Response isSyncRes=iRISWorkbenchService.getMyLifeDetail(dto.getOrganizationId());
			OrganizationMasterDto dtoOrg = (OrganizationMasterDto) isSyncRes.getObject();

			DischargeSummaryDto globalOrgUnitDto = getGlobalOrgUnit(dto);

			if (isSyncRes.getObject() != null && dtoOrg.getMyLifeLink() != null && dtoOrg.getIsSyncScheduling() == true && uuid != null && !uuid.trim().isEmpty()) {
				int lowerLimit = 97;
				int upperLimit = 122;
				Random random = new Random();
				StringBuffer r = new StringBuffer(10);
				for (int i = 0; i < 10; i++) {
					int nextRandomChar = lowerLimit
							+ (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));
					r.append((char) nextRandomChar);
				}

				ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();

				if(dto.getDischargeSummaryPdfBytes() != null && dto.getDischargeSummaryPdfBytes().size()>0) {
					byteOutPutStream = dto.getDischargeSummaryPdfBytes();
				}
				else {
					String str = "";
					ByteArrayOutputStream byteOutPutStreamHtml = null;
					CloseableHttpClient httpClientHtml = null;
					InputStream htmlInputStream = null;
					byteOutPutStreamHtml = new ByteArrayOutputStream();
					httpClientHtml = HttpClientBuilder.create().build();
					String rptString = "{  " + " \"appName\": \"HIS\",  "
							+ " \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\",  "
							+ "	\"reportDesignFilePath\": \"Dischargesummary.rpt\",  "
							+ " \"reportFormat\": \"PDF\",  "
							+ "	\"exportFileName\": \"pathologyReport\",  "
							+ " \"reportParams\": [  "
							+ " {  "
							+ " \"paramName\": \"discharge_summary_id\",  "
							+ " \"paramType\": \"String\",  "
							+ " \"paramValue\": " + dto.getDischargeSummaryId()
							+ " },{  "
							+ " \"paramName\": \"selectedHeader\",  "
							+ " \"paramType\": \"Integer\",  "
							+ " \"paramValue\": 1  "
							+ " }   				        		]  "
							+ " }";
					String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(dto.getOrganizationId(),
							dto.getUnitId());
					HttpPost pst = new HttpPost(API_URL);
					pst.addHeader("Content-Type", "application/json");
					pst.setEntity(new StringEntity(rptString));
					CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					CloseableHttpResponse response = httpClient.execute(pst);
					HttpEntity resEntity = response.getEntity();
					resEntity.writeTo(byteOutPutStream);
				}

				String docPath = "";

				docPath = docUploadUtility.updateRISPdfFile(byteOutPutStream,
						organizationMasterDto.getOrganizationFolder(), r.toString(), dto.getOrganizationId(), dto.getUnitId());

				if(organizationMasterDto !=null && organizationMasterDto.getIsSyncScheduling() == true){

					dto.setDischargePdfPath(docPath);

					String MY_LIFE_URI = null;
					URI url = null;
					Response res1 = iRISWorkbenchService.getConnectionStringForMyLife();
					if (res1.getObject() != null) {
						MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res1.getObject();
						if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null
								&& connectionLinkDto.getMyLifeLink() != "")
							MY_LIFE_URI = connectionLinkDto.getMyLifeLink();

						System.out.println("In the APIS.....2222222");
						if (MY_LIFE_URI != null) {
							Response commonResponse = null;
							PatientDocumentSyncDto patientDocDto = new PatientDocumentSyncDto();
							patientDocDto.setGlobalOrgId(globalOrgUnitDto.getGlobalOrgId());
							patientDocDto.setGlobalUnitId(globalOrgUnitDto.getGlobalUnitId());
							patientDocDto.setDocumentPath(docPath);
							patientDocDto.setTitle(uuid+"_DischargeSummary.pdf");
							patientDocDto.setDocumentTypeName("Discharge Summary");
							patientDocDto.setUuid(uuid);
							patientDocDto.setDocumentTypeId(10); // for Discharge Summary
//							MY_LIFE_URI = "http://localhost:9091/MyLifeGlobal/api";
							url = URI.create(MY_LIFE_URI + "/uploadPatientDocumentFromHIS");
							String ss = new Gson().toJson(patientDocDto);
							System.out.println(ss);
							String stringResponse = restTemplate.postForObject(url, patientDocDto, String.class);
							commonResponse = gson.fromJson(stringResponse, Response.class);
							if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)){
								return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, commonResponse);
							}
							return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);

						}
					}
				}else {
					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,dto.getDischargeSummaryId());
				}
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto.getDischargeSummaryId());
			}
		} catch (HibernateException he) {
			System.out.println("Hibernate Exception : " + he);

		} catch (Exception e) {
			System.out.println("Exception : " +e);

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	public DischargeSummaryDto getGlobalOrgUnit (DischargeSummaryDto dto) {
		try{
			DischargeSummaryDto resDto = (DischargeSummaryDto) sessionFactory.getCurrentSession()
				.createSQLQuery("select mom.global_org_id as \"globalOrgId\", " +
						" mum.global_unit_id as \"globalUnitId\" " +
						" from public.m_unit_master mum " +
						" inner join m_organization_master mom on mom.organization_id = mum.organization_id " +
						" where mum.unit_id =:unitId " +
						" and mum.organization_id =:orgId " +
						" and mum.status ='A' " +
						" and mom.status ='A' ")
				.setParameter("unitId", dto.getUnitId())
				.setParameter("orgId", dto.getOrganizationId())

				.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).uniqueResult();
			return resDto;
		}
		catch (Exception e){

		}
		return null;
	}
}



class PdfHeader extends PdfPageEventHelper{
	private DischargeSummaryReportDto dto;
	
	PdfHeader(DischargeSummaryReportDto dto){
		this.dto= dto;
	}
	
	 public void onStartPage(PdfWriter writer, Document document) {
		    Image image=null;
		try {
			if (dto.getDischargeSummaryHeaderLogo() != null && !dto.getDischargeSummaryHeaderLogo().isEmpty()) {
//				image = Image.getInstance(dto.getDischargeSummaryHeaderLogo());
//				image.scalePercent(50f, 50f);
				PdfPTable table = new PdfPTable(2);
				table.setWidthPercentage(100);
				table.setWidths(new float[] { 30, 100 });
				table.setSpacingBefore(15);
				table.getDefaultCell().setBorderWidth(0f);

				PdfPCell cell = new PdfPCell();
				cell.addElement(image);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
			}
			else if(dto.getUnitLogo() != null && !dto.getUnitLogo().isEmpty()){
				
			}
		} catch (DocumentException e) {
				// TODO Auto-generated catch block
				
			}
	    }
}

class PdfFooter extends PdfPageEventHelper{
	public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("Page " + document.getPageNumber()), 560, 30, 0);
    }
}

class defaultFontProvider extends FontFactoryImp {

    private String _default;

    public defaultFontProvider(String def) {
        _default = def;
    }

    public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
        if (fontName == null || size == 0) {
            fontName = _default;
        }

        return super.getFont(BaseFont.TIMES_ROMAN, encoding, embedded, 12, style, color, cached);
    }
}


class defaultFontProvider2 extends FontFactoryImp {

    private String _default;

    public defaultFontProvider2(String def) {
        _default = def;
    }

    public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
        if (fontName == null || size == 0) {
            fontName = _default;
        }

        return super.getFont(BaseFont.TIMES_ROMAN, encoding, embedded, 13, style, color, cached);
    }


}
   


	
