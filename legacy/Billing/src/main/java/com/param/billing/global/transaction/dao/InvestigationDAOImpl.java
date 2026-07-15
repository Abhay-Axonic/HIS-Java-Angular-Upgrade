package com.param.billing.global.transaction.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.dto.InvestigationDTO;
import com.param.billing.dto.InvestigationOrderDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ServiceMasterDto;
import com.param.opd.model.InvestigationModel;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

/*
 * 
 * Created by TUSHAR
 */
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class InvestigationDAOImpl extends GenericDao<InvestigationModel, Integer>  implements IinvestigationDAO,ICommonConstants{
	
	public InvestigationDAOImpl() {
		super(InvestigationModel.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveInvestigation(List<InvestigationDTO> investigationDTO)
			throws ApplicationException {
		try{
			for(int i=0;i<investigationDTO.size();i++){
				InvestigationModel investigationModel = new InvestigationModel();
				investigationModel.setService_type_id(investigationDTO.get(i).getService_type_id());
				investigationModel.setService_id(investigationDTO.get(i).getService_id());
				investigationModel.setCreated_by(investigationDTO.get(i).getCreated_by());
				investigationModel.setPatient_id(investigationDTO.get(i).getPatient_id());
				investigationModel.setEncounter_id(investigationDTO.get(i).getEncounter_id());
				investigationModel.setUnit_id(investigationDTO.get(i).getUnit_id());
				investigationModel.setOrganisation_id(investigationDTO.get(i).getOrganisation_id());
				investigationModel.setInvestigation_status(true);
				investigationModel.setCreated_date(TimeZoneComponent.getDateByzone(investigationDTO.get(i).getUnit_id()));
				investigationModel.setUpdated_by(investigationDTO.get(i).getUpdated_by());
				investigationModel.setUpdated_date(TimeZoneComponent.getDateByzone(investigationDTO.get(i).getUnit_id()));
				InvestigationModel inestigationId=save(investigationModel);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentInvestigations(Integer patient_id,boolean flag)
			throws ApplicationException {
//		StatelessSession statlessSession = sessionFactory.openStatelessSession();
//		List<InvestigationOrderDto> investigationDTOsList = statlessSession.getNamedQuery("GET_CUEERENT_ORDERED_INVESTIGATION")
//				.setInteger("encounterId", encounter_id)
//				.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		/*StatelessSession statlessSession = sessionFactory.openStatelessSession();
		StringBuilder strBlr=new StringBuilder(statlessSession.getNamedQuery("GET_CUEERENT_ORDERED_INVESTIGATION").getQueryString());
		if(flag){
			
			
			strBlr.append(" where inv.\"encounterId\"="+patient_id);
		}
		else{
			strBlr.append(" where inv.\"admissionId\"="+patient_id);
		}
		List<InvestigationOrderDto> investigationDTOsList=statlessSession.createSQLQuery(strBlr.toString()).setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		statlessSession.close();*/
		
		
		StringBuilder sb = new  StringBuilder();
			sb.append("( select "  + 
					" ord_mst.order_id as \"orderId\", " + 
					" service_mst.speciality_id as \"specialityId\"," + 
					" service_mst.sub_speciality_id as \"subDeptId\"," + 
					" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
					" ord_mst.admission_id as \"admissionId\", " + 
					" ord_mst.encounter_id as \"encounterId\", " + 
					" ord_mst.patient_id as \"patientId\", " + 
					 "  ord_mst.order_no as \"orderNo\","+
					" ord_dtls.order_details_id as \"orderDetailsId\", " + 
					" ord_dtls.service_id as \"serviceMasterId\", " + 
					" ord_dtls.order_set_id as \"orderSetId\", " + 
					" ord_dtls.priority_id as \"priorityId\", " + 
					" ord_dtls.ord_remarks as \"ordRemarks\", " + 
					" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
					"	ord_dtls.service_amt as \"serviceAmt\"," + 
					 "	templatemstr.template_type_id as \"templateTypeId\", "+
					"	templatemstr.template_format_id as \"templateFormatId\"," +
					" concat( to_char( extract( year from age( current_timestamp, patientreg.dob )), '999' ), ' Y' ) as \"ageString\", "+
					 "	patientreg.mobile_number as \"mobileNumber\", " +
					 "	prefixmaster.prefix_code as \"prefixCode\", " +
					" to_char(ord_mst.order_date, 'dd/MM/yyyy HH12:MI AM') AS  \"orderDateTime\", " + 
					"	to_char(ord_mst.order_date,'dd/MM/yyyy') as \"orderDate\","+
					 " detailsusm.is_procedure as \"isProcedure\"," +
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
					" ord_dtls.created_date  as \"createdDateNew\", " + 
					" ord_dtls.service_rendered as \"serviceRendered\", " + 
					" ord_dtls.is_package_service as \"isPackageService\", " + 
					" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
					" (case when (sample_dtls.micro_template_status_id is not null and sample_dtls.micro_template_status_id=41)" + 
					" then microTemp.template_object else templatetr12_.template_object end) as \"templateObject\", "
					+ " (case when ttm.is_html_print is null  then  templatemstr.is_html_print  else  ttm.is_html_print end) as \"isHtmlPrint\", " + 
					"	service_mst.service_master_id as \"serviceId\"," + 
					" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
					" ord_dtls.status as \"orderStatus\", " + 
					" am.appointment_id as \"appointmentId\", " + 
					" am.slot_id as \"slotId\", " + 
					 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , " 
					+" (case " + 
					" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name)  " + 
					" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
					" else 'Admin' end) as \"userName\"," + 
					" ord_dtls.package_id as \"packageId\" " + 
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
					" 		left join lab.t_lab_test_result lab_res on " + 
					" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
					" and lab_res.sample_status_id in( 8, 9) AND  lab_res.status = 'A' " + 
					" 		left outer join public.t_template_transaction templatetr12_ on " + 
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
					" left join lab.t_microbiology_template microTemp on "
					+ " microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id "
					 + "inner join patient.t_patient_registration patientreg on "
					 + " ord_mst.patient_id = patientreg.patient_id "
					 + "inner join public.m_prefix_master prefixmaster on "
					 + " patientreg.prefix_id = prefixmaster.prefix_id "
					+ " left join public.t_template_master ttm on " + 
					"	ttm.template_id = microTemp.template_id " + 
					 "  left join service.t_unit_service_master_details detailsusm on  "  
					 + "  detailsusm.service_master_id = service_mst.service_master_id "+
					"	left outer join public.t_template_master templatemstr on " + 
					"	templatetr12_.template_id = templatemstr.template_id " + 
					" 		where " + 
					" service_mst.status = 'A' " + 
					" and ord_dtls.old_ord_dtl_id is null " + 
					" and service_dtls.is_package = 'I' " + 
					" and ord_dtls.nursing_service_order_flag = 'N' " + 
					" and service_dtls.is_panel = 'N' " + 
					//" and unitSplMpr.is_rendering_speciality = 'Y' " + 
					" and unitSplMpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
	 				+" and ord_dtls.is_procedure_service = 'N' ");   
	       
			if(flag){
				
				sb.append(" and  ord_mst.encounter_id  =:patient_id )");
			}
			else{
				sb.append(" and ord_mst.admission_id  =:patient_id )");
			}
	        
	     sb.append(" union "+
	    		 " (select " + 
	    		 "ord_mst.order_id as \"orderId\", " + 
	    		 "service_mst.speciality_id as \"specialityId\"," + 
	    		 "service_mst.sub_speciality_id as \"subDeptId\"," + 
	    		 "service_mst.sub_speciality_id as \"subSpecialityId\"," + 
	    		 "ord_mst.admission_id as \"admissionId\", " + 
	    		 "ord_mst.encounter_id as \"encounterId\", " + 
	    		 "ord_mst.patient_id as \"patientId\", " + 
	    		 "    ord_mst.order_no as \"orderNo\","+
	    		 "ord_dtls.order_details_id as \"orderDetailsId\", " + 
	    		 "ord_dtls.service_id as \"serviceMasterId\", " + 
	    		 "	ord_dtls.order_set_id as \"orderSetId\", " + 
	    		 "	ord_dtls.priority_id as \"priorityId\", " + 
	    		 "	ord_dtls.ord_remarks as \"ordRemarks\", " + 
	    		 "  to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", "+
	    		 "	ord_dtls.service_amt as \"serviceAmt\"," + 
	    		 "	templatemstr.template_type_id as \"templateTypeId\", "+
	    		 "	templatemstr.template_format_id as \"templateFormatId\"," +
				 " concat( to_char( extract( year from age( current_timestamp, patientreg.dob )), '999' ), ' Y' ) as \"ageString\", "+
				 "	patientreg.mobile_number as \"mobileNumber\", " +
				 "	prefixmaster.prefix_code as \"prefixCode\", " +
	    		 " to_char(ord_mst.order_date, 'dd/MM/yyyy HH12:MI AM') AS  \"orderDateTime\", " + 
	    		 "	to_char(ord_mst.order_date,'dd/MM/yyyy') as \"orderDate\","+
	    		 " detailsusm.is_procedure as \"isProcedure\"," +
	    		 "service_mst.service_standard_name as \"serviceStandardName\", " + 
	    		 "service_mst.service_standard_code as \"serviceStandardCode\", " + 
	    		 "speciality_mst.speciality_name as \"specialityName\", " + 
	    		 "cast( " + 
	    		 "	null as integer " + 
	    		 ") as \"labSampleDtlsId\", " + 
	    		 "service_dtls.is_panel as \"isPanel\", " + 
	    		 "cast( " + 
	    		 "	null as integer " + 
	    		 ") as \"labTestResId\", " + 
	    		 "panel.panel_id as \"panelId\", " + 
	    		 "null as \"statusName\", " + 
	    		 "0 as \"sampleStatusId\", " + 
	    		 "to_char( " + 
	    		 "	ord_dtls.created_date, " + 
	    		 "	'dd/MM/yyyy' " + 
	    		 ") as \"createdDate\", " + 
	    		 "	ord_dtls.created_date as \"createdDateNew\", " + 
	    		 "ord_dtls.service_rendered as \"serviceRendered\", " + 
	    		 "ord_dtls.is_package_service as \"isPackageService\", " + 
	    		 "templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
	    		 " templatetr12_.template_object as \"templateObject\", "
	    		 + " templatemstr.is_html_print  as \"isHtmlPrint\", "+
	    		 "	service_mst.service_master_id as \"serviceId\"," + 
	    		 "ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
	    		 "ord_dtls.status as \"orderStatus\", " + 
	    		 "am.appointment_id as \"appointmentId\", " + 
	    		 "am.slot_id as \"slotId\", " + 
	    		 " 0 as \"microTemplateStatusId\" , "
	    		 +"(case " + 
	    		 "when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
	    		 "when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
	    		 "else 'Admin' end) as \"userName\"," + 
	    		 "ord_dtls.package_id as \"packageId\" " + 
	    		 "	from " + 
	    		 "public.t_order_master ord_mst " + 
	    		 "	inner join public.t_order_details ord_dtls on " + 
	    		 "ord_mst.order_id = ord_dtls.order_id " + 
	    		 "	inner join service.t_unit_service_mapper service_mppr on " + 
	    		 "service_mppr.service_id = ord_dtls.service_id " + 
	    		 "and service_mppr.unit_id = ord_dtls.org_unit_id " + 
	    		 "	inner join service.t_unit_service_master_details service_dtls on " + 
	    		 "service_dtls.service_master_id = service_mppr.service_id " + 
	    		 "and service_dtls.unit_id = ord_dtls.org_unit_id " + 
	    		 "	inner join service.m_service_master service_mst on " + 
	    		 "service_mst.service_master_id = ord_dtls.service_id " + 
	    		 "	left join doctor.m_speciality_master speciality_mst on " + 
	    		 "speciality_mst.speciality_id = service_mst.speciality_id " + 
	    		 "	left join t_unit_speciality_mapper unitSplMpr on " + 
	    		 "unitSplMpr.speciality_id = service_mst.speciality_id " + 
	    		 "and unitSplMpr.unit_id = service_mppr.unit_id " + 
	    		 "	inner join lab.t_panel_master panel on " + 
	    		 "panel.service_id = service_mst.service_master_id " + 
	    		 "and panel.org_unit_id = service_mppr.unit_id " + 
	    		 "	left outer join public.t_template_transaction templatetr12_ on " + 
	    		 "ord_dtls.order_details_id = templatetr12_.order_details_id " + 
	    		 "and( " + 
	    		 "	templatetr12_.status = 'A' " + 
	    		 ") " + 
	    		 "	left join opd.m_appointment_master am on " + 
	    		 "am.order_details_id = ord_dtls.order_details_id " + 
	    		 "	left join t_unit_application_billing_configuration uabc on " + 
	    		 "	uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A'	 " + 
	    		 "left join public.m_user_master um on ord_mst.created_by = um.user_id " + 
	    		 "left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
	    		 "left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
				 + "inner join patient.t_patient_registration patientreg on "
				 + " ord_mst.patient_id = patientreg.patient_id "
				 + "inner join public.m_prefix_master prefixmaster on "
				 + " patientreg.prefix_id = prefixmaster.prefix_id "
				 + " left join service.t_unit_service_master_details detailsusm on  "  
				 + "  detailsusm.service_master_id = service_mst.service_master_id "
	    		 + " left outer join public.t_template_master templatemstr on  " + 
	    		 "	templatetr12_.template_id = templatemstr.template_id" + 
	    		 " where " + 
	    		 "service_mst.status = 'A' " + 
	    		 "and ord_dtls.old_ord_dtl_id is null " + 
	    		 "and service_dtls.is_package = 'I' " + 
	    		 "and service_dtls.is_panel = 'Y' " + 
	    		// "and unitSplMpr.is_rendering_speciality = 'Y' " + 
	    		 " and unitSplMpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
	    		 + "	and ord_dtls.nursing_service_order_flag = 'N' " + 
	    		 " and ord_dtls.is_procedure_service = 'N' " );
	       
	     		if(flag){
				
	     			sb.append(" and  ord_mst.encounter_id  =:patient_id)");
	     		}
	     		else{
	     			
	     			sb.append(" and ord_mst.admission_id  =:patient_id)");
	     		}

	        	Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					 .setParameter("patient_id", patient_id);

	        	List<InvestigationOrderDto> investigationDTOsList = createdQuery
	        	.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
	}
	
	@Override
	public Response getInvestigations(Date start_date,Date end_date,Integer service_id)
			throws ApplicationException {
		StatelessSession statlessSession = sessionFactory.openStatelessSession();
		List<InvestigationDTO> investigationDTOsList = statlessSession.getNamedQuery("GET_INVESTIGATIONS")
				.setParameter("START_DATE", start_date)
				.setParameter("END_DATE", end_date)
				.setParameter("SERVICE_ID", service_id)
				.setResultTransformer(Transformers.aliasToBean(InvestigationDTO.class)).list();
		statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
	}

	@Override
	public Response updateInvestigation(InvestigationDTO investigationDTO)
			throws ApplicationException {
		try{
			
			InvestigationModel investigationModel = findById(investigationDTO.getInvestigation_id());
			if(investigationDTO.getService_type_id() > 0){
				investigationModel.setService_type_id(investigationDTO.getService_type_id());				
			}
			if(investigationDTO.getService_id() > 0){
				investigationModel.setService_id(investigationDTO.getService_id());
			}
			if(investigationDTO.getPatient_id() > 0){
				investigationModel.setPatient_id(investigationDTO.getPatient_id());
			}
			if(investigationDTO.getEncounter_id() > 0){
				investigationModel.setEncounter_id(investigationDTO.getEncounter_id());
			}
			
			if(investigationDTO.getUnit_id() > 0){
				investigationModel.setUnit_id(investigationDTO.getUnit_id());
			}
			if(investigationDTO.getOrganisation_id() > 0){
				investigationModel.setOrganisation_id(investigationDTO.getOrganisation_id());				
			}
			investigationModel.setUpdated_by(investigationDTO.getUpdated_by());
			investigationModel.setUpdated_date(TimeZoneComponent.getDateByzone(investigationDTO.getUnit_id()));
			update(investigationModel);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response deleteInvestigation(Integer investigation_id)
			throws ApplicationException {
		try{
			InvestigationModel investigationModel = findById(investigation_id);
			if(investigationModel != null){
				investigationModel.setInvestigation_status(FALSE);
				update(investigationModel);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);		
			}else {
				return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);		
			}
			
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getServices(String serviceCode, String serviceName, Integer orgId) throws ApplicationException {
		try{
			if(null != serviceCode){
				if(serviceCode.length() >= 3){
					StatelessSession statlessSession = sessionFactory.openStatelessSession();
					List<ServiceMasterDto> serviceDTOsList = statlessSession.getNamedQuery("GET_SERVICES_FOR_INVESTIGATION_BY_CODE")
							.setParameter("SERVICE_CODE", "%" + serviceCode.toLowerCase() + "%")
							.setParameter("ORGID", orgId)
							.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
					statlessSession.close();
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, serviceDTOsList, null);	
					
				}else{
					return new Response(ERROR, ERROR_CODE, COMMON_ERROR, null, null);
				}	
			} else {
				if(serviceName.length() >= 3){
					StatelessSession statlessSession = sessionFactory.openStatelessSession();
					List<ServiceMasterDto> serviceDTOsList = statlessSession.getNamedQuery("GET_SERVICES_FOR_INVESTIGATION_BY_NAME")
							.setParameter("SERVICE_NAME", "%" + serviceName.toLowerCase() + "%")
							.setParameter("ORGID", orgId)
							.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
					statlessSession.close();
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, serviceDTOsList, null);	
					
				}else{
					return new Response(ERROR, ERROR_CODE, COMMON_ERROR, null, null);
				}
			}
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getServiceTypes() throws ApplicationException {
		try{
			/*StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<GroupMasterDTO> serviceDTOsList = statlessSession.getNamedQuery("GET_SERVICE_TYPES_FOR_INVESTIGATION")
					.setResultTransformer(Transformers.aliasToBean(GroupMasterDTO.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, serviceDTOsList, null);	*/
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	public Response getHistoryServices(Integer orgId) throws ApplicationException {
		try{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				List<ServiceMasterDto> serviceDTOsList = statlessSession.getNamedQuery("GET_SERVICES_FOR_HISTORY")
						.setParameter("ORGID", orgId)
						.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
				statlessSession.close();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, serviceDTOsList, null);	
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response getServicesByTest(Integer serviceId) throws ApplicationException {
		try{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				List<ServiceMasterDto> serviceDTOsList = statlessSession.getNamedQuery("GET_SERVICES_FROM_TEST")
						.setParameter("SERVICEID", serviceId)
						.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
				statlessSession.close();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, serviceDTOsList, null);	
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentInvestigationDetails(Integer panelId, Integer orderDetailsId)
			throws ApplicationException {
		StatelessSession statlessSession = sessionFactory.openStatelessSession();

		List<InvestigationOrderDto> investigationDTOsList = statlessSession
				.getNamedQuery("GET_CUEERENT_ORDERED_INVESTIGATION_DETAILS")
				.setParameter("panelId", panelId)
				.setParameter("orderDetailsId", orderDetailsId)
				.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);
	}

	@Override
	public Response getCurrentInvestigationsByPatientId(Integer patient_id)
			throws ApplicationException {
		StatelessSession statlessSession = sessionFactory.openStatelessSession();
		StringBuilder strBlr=new StringBuilder(statlessSession.getNamedQuery("GET_CURRENT_INVESTIGATIONS_BY_PATIENT_ID").getQueryString());
			strBlr.append(" where inv.\"patientId\"=:patientId ");
		List<InvestigationOrderDto> investigationDTOsList=statlessSession.createSQLQuery(strBlr.toString())
				.setParameter("patientId", patient_id)
				.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);
	}

	@Override
	public Response getInvestigationsListForBookView(Integer patientId, Integer organizationId,String fromDate, String toDate) {
		StatelessSession statlessSession = sessionFactory.openStatelessSession();
		StringBuilder strBlr=new StringBuilder(statlessSession.getNamedQuery("GET_INVESTIGATIONS_LIST_FOR_BOOK_VIEW").getQueryString());
		
			
			/*strBlr.append(" where inv.\"patientId\"="+patientId);
		
			strBlr.append(" and inv.\"organizationId\"="+organizationId);
			strBlr.append(" and inv.\"createdDate\">="+fromDate);
			strBlr.append(" and inv.\"createdDate\"<="+toDate);*/
		
		List<InvestigationOrderDto> investigationDTOsList=statlessSession.createSQLQuery(strBlr.toString())
				.setParameter("patientId", patientId)
				.setParameter("organizationId", organizationId)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
	}

	@Override
	public Response getInvestigationsListForCoversheet(int patient_id, boolean flag,Integer unitId)
			throws ApplicationException {
		
		StringBuilder sb = new  StringBuilder();
		sb.append("select "  + 
				" ord_mst.order_id as \"orderId\", " + 
				" service_mst.speciality_id as \"specialityId\"," + 
				" service_mst.medall_service_id as \"medallServiceId\"," + 
				" service_mst.sub_speciality_id as \"subDeptId\"," + 
				" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
				"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
				" ord_mst.admission_id as \"admissionId\", " + 
				" ord_mst.encounter_id as \"encounterId\", " + 
				" ord_mst.patient_id as \"patientId\", " + 
				" ord_dtls.order_details_id as \"orderDetailsId\", " + 
				" ord_dtls.service_id as \"serviceMasterId\", " + 
				" ord_dtls.order_set_id as \"orderSetId\", " + 
				" ord_dtls.priority_id as \"priorityId\", " + 
				" ord_dtls.ord_remarks as \"ordRemarks\", " + 
				" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
				 " to_char(ord_dtls.order_date,'YYYY-MM-DD') as \"orderDateString\", "+
				 " to_char(ord_dtls.order_date, 'HH24:MI') AS  \"orderDateTime\", "+
				" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy HH24:MI') as \"orderSchDateString\", " + 
				" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
				" service_mst.service_standard_name as \"serviceStandardName\", " + 
				" service_mst.service_standard_code as \"serviceStandardCode\", " + 
				" speciality_mst.speciality_name as \"specialityName\", " + 
				" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\","
				+ " sample_dtls.provisional as \"provisional\",  " + 
				" service_dtls.is_panel as \"isPanel\", " + 
				" workbench.result_id as \"resultId\", " +
				" workbench.workbench_added_date as \"workbenchAddedDate\", " +
				" workbench.patient_workbench_id as \"patientWorkbenchId\", " +
				" workbench.remark as \"workbenchRemark\", " +
				" service_dtls.is_poc as \"isPoc\", " +
				" lab_res.lab_test_res_id  as \"labTestResId\", " +
				" cast( " + 
				" 	null as integer " + 
				" ) as \"panelId\", " + 
				" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
				" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
				" to_char( " + 
				" 	ord_dtls.created_date, " + 
				" 	'dd/MM/yyyy HH24:MI' " + 
				" ) as \"createdDate\", " +
				" to_char( " +
				" 	ord_dtls.created_date, " +
				" 	'DD/MM/YYYY' " +
				" ) as \"createdDateForFilter\", " +
				" to_char( " +
				" 	sample_dtls.sample_gen_datetime, " + 
				" 	'dd/MM/yyyy HH24:MI' " + 
				" ) as \"sampleGenDatetime\", " + 
				" 	ord_dtls.created_date as \"createdDateNew\", " + 
				" ord_dtls.service_rendered as \"serviceRendered\", " + 
				" ord_dtls.is_package_service as \"isPackageService\", " + 
				" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
				" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
				" ord_dtls.status as \"orderStatus\", " + 
				 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
				 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
				 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
				 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
				 + " else 'Super Admin' end) as \"collectedUserName\","
				+" (case " + 
				" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
				" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
				" else 'Admin' end) as \"userName\"," + 
				" ord_dtls.package_id as \"packageId\" , "
				+ " mtm.test_type as \"testType\", "
				+  " ord_dtls.ord_render_datetime as \"ordRenderDatetime\" " +
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
				" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
				" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
				" and sample_dtls.sample_status_id <> 4 " + 
				" 		left join lab.m_test_status_master test_status on " + 
				" test_status.test_status_id = sample_dtls.sample_status_id " + 
				"        	left join lab.m_test_status_master test_status2 on " + 
				" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
				" 		left join lab.t_lab_test_result lab_res on " + 
				" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
				" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
				" 		left outer join public.t_template_transaction templatetr12_ on " + 
				" ord_dtls.order_details_id = templatetr12_.order_details_id  "
				+ " left join lab.m_test_master mtm  on " + 
				"	mtm.service_id = ord_dtls.service_id " + 
				" and( " + 
				" 	templatetr12_.status = 'A' " + 
				" ) " + 
				" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
				" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
				" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " +
				 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " +
	    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " +
	    		 " left join adt.t_admitted_patient_orders_workbench workbench on ord_dtls.order_details_id  = workbench.record_id " +
				" 		where " +
				" service_mst.status = 'A' " + 
				" and ord_dtls.old_ord_dtl_id is null " + 
				" and service_dtls.is_package = 'I' " + 
				" and ord_dtls.nursing_service_order_flag = 'N' " + 
				" and service_dtls.is_panel = 'N' " + 
				 " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id =:unitId and is_rendering_speciality = 'Y' ) "
				);   
       
			
			sb.append(" and  ord_mst.patient_id =:patientId ");
        
/*     sb.append(" union all "+
    		 " select " + 
    		 "ord_mst.order_id as \"orderId\", " + 
    		 "service_mst.speciality_id as \"specialityId\"," + 
    		 " service_mst.medall_service_id as \"medallServiceId\"," + 
    		 "service_mst.sub_speciality_id as \"subDeptId\"," + 
    		 "service_mst.sub_speciality_id as \"subSpecialityId\"," + 
    		 "ord_dtls.is_procedure_service as \"isProcedureService\", " + 
    		 "ord_mst.admission_id as \"admissionId\", " + 
    		 "ord_mst.encounter_id as \"encounterId\", " + 
    		 "ord_mst.patient_id as \"patientId\", " + 
    		 "ord_dtls.order_details_id as \"orderDetailsId\", " + 
    		 "ord_dtls.service_id as \"serviceMasterId\", " + 
    		 "	ord_dtls.order_set_id as \"orderSetId\", " + 
    		 "	ord_dtls.priority_id as \"priorityId\", " + 
    		 "	ord_dtls.ord_remarks as \"ordRemarks\", " + 
    		 "  to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
    		 " to_char(ord_dtls.order_date,'YYYY-MM-DD') as \"orderDateString\", "+
			 " to_char(ord_dtls.order_date, 'HH24:MI') AS  \"orderDateTime\", "+
    		 " to_char(ord_dtls.order_sch_date,'dd/MM/yyyy HH24:MI') as \"orderSchDateString\", " + 
    		 "  ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
    		 "service_mst.service_standard_name as \"serviceStandardName\", " + 
    		 "service_mst.service_standard_code as \"serviceStandardCode\", " + 
    		 "speciality_mst.speciality_name as \"specialityName\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labSampleDtlsId\", " + 
    		 "cast( " + 
    		 "	null as boolean  " + 
    		 ")  as \"provisional\",  " + 
    		 "service_dtls.is_panel as \"isPanel\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labTestResId\", " + 
    		 "panel.panel_id as \"panelId\", " + 
    		 "null as \"statusName\", " + 
    		 "0 as \"sampleStatusId\", " + 
    		 "to_char( " + 
    		 "	ord_dtls.created_date, " + 
    		 "	'dd/MM/yyyy HH24:MI' " + 
    		 ") as \"createdDate\", " +
			 " to_char( " +
			 " 	ord_dtls.created_date, " +
			 " 	'DD/MM/YYYY' " +
			 " ) as \"createdDateForFilter\", " +
    		 " '' as \"sampleGenDatetime\", " + 
    		 "		ord_dtls.created_date as \"createdDateNew\", " + 
    		 "ord_dtls.service_rendered as \"serviceRendered\", " + 
    		 "ord_dtls.is_package_service as \"isPackageService\", " + 
    		 "templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
    		 "ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
    		 "ord_dtls.status as \"orderStatus\", " + 
    		 " 0 as \"microTemplateStatusId\" , "
    		 +"'' as \"collectedUserName\"," 
    		+ "(case " + 
    		 "when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
    		 "when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
    		 "else 'Admin' end) as \"userName\"," + 
    		 "ord_dtls.package_id as \"packageId\" ,"
    			+ " mtm.test_type as \"testType\" " +
    		 "	from " + 
    		 "public.t_order_master ord_mst " + 
    		 "	inner join public.t_order_details ord_dtls on " + 
    		 "ord_mst.order_id = ord_dtls.order_id " + 
    		 "	inner join service.t_unit_service_mapper service_mppr on " + 
    		 "service_mppr.service_id = ord_dtls.service_id " + 
    		 "and service_mppr.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.t_unit_service_master_details service_dtls on " + 
    		 "service_dtls.service_master_id = service_mppr.service_id " + 
    		 "and service_dtls.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.m_service_master service_mst on " + 
    		 "service_mst.service_master_id = ord_dtls.service_id " + 
    		 "	left join doctor.m_speciality_master speciality_mst on " + 
    		 "speciality_mst.speciality_id = service_mst.speciality_id " + 
    		 "	inner join lab.t_panel_master panel on " + 
    		 "panel.service_id = service_mst.service_master_id " + 
    		 "and panel.org_unit_id = service_mppr.unit_id " + 
    		 "	left outer join public.t_template_transaction templatetr12_ on " + 
    		 "ord_dtls.order_details_id = templatetr12_.order_details_id " + 
    		 "and( " + 
    		 "	templatetr12_.status = 'A' " + 
    		 ") " + 
    		 "left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
    		 "left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " 
    		 + " left join lab.m_test_master mtm  on " + 
				"	mtm.service_id = ord_dtls.service_id " + 
    		 " where " + 
    		 "service_mst.status = 'A' " + 
    		 "and ord_dtls.old_ord_dtl_id is null " + 
    		 "and service_dtls.is_package = 'I' " + 
    		 "and service_dtls.is_panel = 'Y' " + 
    		 " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id ="+unitId+" and is_rendering_speciality = 'Y' ) " + 
    		 "	and ord_dtls.nursing_service_order_flag = 'N' " 
    		 );*/
       
     		sb.append(" order by \"createdDateNew\" desc ");
     		
        	Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("unitId", unitId)
					.setParameter("patientId", patient_id);

        	List<InvestigationOrderDto> investigationDTOsList = createdQuery
        	.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
	}

	@Override
	public Response getInvestigationsListForAnesthesia(int patient_id, boolean flag, Integer unitId) throws ApplicationException {

		try{
			StringBuilder sb = new  StringBuilder();
			sb.append("select "  +
					" service_dtls.is_procedure as \"isProcedure\", " +
					" service_dtls.procedure_type_id as \"procedureTypeId\", " +
					" service_mst.service_standard_name as \"serviceStandardName\", " +
					" service_mst.service_standard_code as \"serviceStandardCode\", " +
					" service_mst.speciality_id as \"specialityId\"," +
					" speciality_mst.speciality_name as \"specialityName\", " +
					" ord_mst.order_id as \"orderId\", " +
					"		concat('Dr. ',coalesce(docmst.first_name ,''),' ',coalesce(docmst.middle_name,''),' ',coalesce(docmst.last_name,'')) as \"doctorName\", "+
					" ord_dtls.created_date as \"createdDateNew\" " +
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
					" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " +
					" 		where " +
					" service_mst.status = 'A' " +
					" and ord_dtls.old_ord_dtl_id is null " +
					" and service_dtls.is_package = 'I' " +
					" and ord_dtls.nursing_service_order_flag = 'N' " +
					" and service_dtls.is_panel = 'N' " +
					" and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id =:unitId and is_rendering_speciality = 'Y' ) "
			);


			sb.append("and  ord_mst.patient_id  =:patientId");

			sb.append(" union all"+
					" select " +
					" service_dtls.is_procedure as \"isProcedure\", " +
					" service_dtls.procedure_type_id as \"procedureTypeId\", " +
					" service_mst.service_standard_name as \"serviceStandardName\", " +
					" service_mst.service_standard_code as \"serviceStandardCode\", " +
					" service_mst.speciality_id as \"specialityId\"," +
					" speciality_mst.speciality_name as \"specialityName\", " +
					" ord_mst.order_id as \"orderId\", " +
					"		concat('Dr. ',coalesce(docmst.first_name ,''),' ',coalesce(docmst.middle_name,''),' ',coalesce(docmst.last_name,'')) as \"doctorName\", "+
					" ord_dtls.created_date as \"createdDateNew\" " +
					"	from " +
					"public.t_order_master ord_mst " +
					"	inner join public.t_order_details ord_dtls on " +
					"ord_mst.order_id = ord_dtls.order_id " +
					"	inner join service.t_unit_service_mapper service_mppr on " +
					"service_mppr.service_id = ord_dtls.service_id " +
					"and service_mppr.unit_id = ord_dtls.org_unit_id " +
					"	inner join service.t_unit_service_master_details service_dtls on " +
					"service_dtls.service_master_id = service_mppr.service_id " +
					"and service_dtls.unit_id = ord_dtls.org_unit_id " +
					"	inner join service.m_service_master service_mst on " +
					"service_mst.service_master_id = ord_dtls.service_id " +
					"	left join doctor.m_speciality_master speciality_mst on " +
					"speciality_mst.speciality_id = service_mst.speciality_id " +
					"	inner join lab.t_panel_master panel on " +
					"panel.service_id = service_mst.service_master_id " +
					"and panel.org_unit_id = service_mppr.unit_id " +
					"left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id "
					+ " left join lab.m_test_master mtm  on " +
					"	mtm.service_id = ord_dtls.service_id " +
					" where " +
					"service_mst.status = 'A' " +
					"and ord_dtls.old_ord_dtl_id is null " +
					"and service_dtls.is_package = 'I' " +
					"and service_dtls.is_panel = 'Y' " +
					" and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id =:unitId and is_rendering_speciality = 'Y' ) " +
					"	and ord_dtls.nursing_service_order_flag = 'N' "
			);

			sb.append("and  ord_mst.patient_id  =:patientId order by \"createdDateNew\" desc ");

			Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("unitId", unitId)
					.setParameter("patientId", patient_id);

			List<InvestigationOrderDto> investigationDTOsList = createdQuery
					.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);
		}catch (Exception e){
			throw e;
		}
	}

	@Override
	public Response getCurrentInvestigationsByProcedureIdAndVisitId(Integer visitId, Integer procedureId, boolean flag)
			throws ApplicationException {
		try {
		StatelessSession statlessSession = sessionFactory.openStatelessSession();
		StringBuilder strBlr=new StringBuilder(statlessSession.getNamedQuery("GET_CURRENT_INVESTIGATION_BY_PRO_REQ_ID").getQueryString());
		if(flag){
			
			strBlr.append(" where inv.\"encounterId\"=:visitId ");
			strBlr.append(" and inv.\"procedureReqId\"=:procedureId ");
			
		}
		else{
			strBlr.append(" where inv.\"admissionId\"=:visitId ");
			strBlr.append(" and inv.\"proReqId\"=:procedureId ");
		}
		List<InvestigationOrderDto> investigationDTOsList=statlessSession.createSQLQuery(strBlr.toString())
				.setParameter("visitId", visitId)
				.setParameter("procedureId", procedureId)
				.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getInvestigationsByPatientDetails(InvestigationOrderDto investigationOrderDto)
			throws ApplicationException {
		/*StatelessSession statlessSession = sessionFactory.openStatelessSession();
		StringBuilder strBlr=new StringBuilder(statlessSession.getNamedQuery("GET_CUEERENT_ORDERED_INVESTIGATION").getQueryString());
		if(investigationOrderDto.getPatientId() != null) {
			strBlr.append(" where inv.\"patientId\"="+investigationOrderDto.getPatientId());
		}
		if(investigationOrderDto.getEncounterId() != null) {
			strBlr.append(" AND inv.\"encounterId\"="+investigationOrderDto.getEncounterId());
		}
		if(investigationOrderDto.getAdmissionId() != null) {
			strBlr.append(" AND inv.\"admissionId\"="+investigationOrderDto.getAdmissionId());
		}
		List<InvestigationOrderDto> investigationDTOsList=statlessSession.createSQLQuery(strBlr.toString()).setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		statlessSession.close();*/
		
		
		
		
		StringBuilder sb = new  StringBuilder();
		sb.append("select "  + 
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
				" ord_dtls.created_date as \"createdDateNew\", " + 
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
				" ord_dtls.package_id as \"packageId\" " + 
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
				" 		left join lab.t_lab_test_result lab_res on " + 
				" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
				" and lab_res.sample_status_id in( 8, 9) AND  lab_res.status = 'A' " + 
				" 		left outer join public.t_template_transaction templatetr12_ on " + 
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
				" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
				" 		where " + 
				" service_mst.status = 'A' " + 
				" and ord_dtls.old_ord_dtl_id is null " + 
				" and service_dtls.is_package = 'I' " + 
				" and ord_dtls.nursing_service_order_flag = 'N' " + 
				" and service_dtls.is_panel = 'N' " + 
				//" and unitSplMpr.is_rendering_speciality = 'Y' " + 
				" and unitSplMpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
 				+" and ord_dtls.is_procedure_service = 'N' ");   
       
		
		if(investigationOrderDto.getPatientId() != null) {
			sb.append(" and  ord_mst.patient_id  =:patientId ");
		}
		if(investigationOrderDto.getEncounterId() != null) {
			sb.append(" and  ord_mst.encounter_id =:encounterId ");
		}
		if(investigationOrderDto.getAdmissionId() != null) {
			sb.append(" and ord_mst.admission_id  =:admissionId ");
		}
        
     sb.append(" union "+
    		 " select " + 
    		 "ord_mst.order_id as \"orderId\", " + 
    		 "service_mst.speciality_id as \"specialityId\"," + 
    		 "service_mst.sub_speciality_id as \"subDeptId\"," + 
    		 "service_mst.sub_speciality_id as \"subSpecialityId\"," + 
    		 "ord_mst.admission_id as \"admissionId\", " + 
    		 "ord_mst.encounter_id as \"encounterId\", " + 
    		 "ord_mst.patient_id as \"patientId\", " + 
    		 "ord_dtls.order_details_id as \"orderDetailsId\", " + 
    		 "ord_dtls.service_id as \"serviceMasterId\", " + 
    		 "	ord_dtls.order_set_id as \"orderSetId\", " + 
    		 "	ord_dtls.priority_id as \"priorityId\", " + 
    		 "	ord_dtls.ord_remarks as \"ordRemarks\", " + 
    		 "           to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
    		 "service_mst.service_standard_name as \"serviceStandardName\", " + 
    		 "service_mst.service_standard_code as \"serviceStandardCode\", " + 
    		 "speciality_mst.speciality_name as \"specialityName\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labSampleDtlsId\", " + 
    		 "service_dtls.is_panel as \"isPanel\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labTestResId\", " + 
    		 "panel.panel_id as \"panelId\", " + 
    		 "null as \"statusName\", " + 
    		 "0 as \"sampleStatusId\", " + 
    		 "to_char( " + 
    		 "	ord_dtls.created_date, " + 
    		 "	'dd/MM/yyyy' " + 
    		 ") as \"createdDate\", " + 
    		 "	ord_dtls.created_date as \"createdDateNew\", " + 
    		 "ord_dtls.service_rendered as \"serviceRendered\", " + 
    		 "ord_dtls.is_package_service as \"isPackageService\", " + 
    		 "templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
    		 " templatetr12_.template_object as \"templateObject\", " + 
    		 "ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
    		 "ord_dtls.status as \"orderStatus\", " + 
    		 "am.appointment_id as \"appointmentId\", " + 
    		 "am.slot_id as \"slotId\", " + 
    		 " 0 as \"microTemplateStatusId\" , "
    		+ "(case " + 
    		 "when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
    		 "when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
    		 "else 'Admin' end) as \"userName\"," + 
    		 "ord_dtls.package_id as \"packageId\" " + 
    		 "	from " + 
    		 "public.t_order_master ord_mst " + 
    		 "	inner join public.t_order_details ord_dtls on " + 
    		 "ord_mst.order_id = ord_dtls.order_id " + 
    		 "	inner join service.t_unit_service_mapper service_mppr on " + 
    		 "service_mppr.service_id = ord_dtls.service_id " + 
    		 "and service_mppr.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.t_unit_service_master_details service_dtls on " + 
    		 "service_dtls.service_master_id = service_mppr.service_id " + 
    		 "and service_dtls.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.m_service_master service_mst on " + 
    		 "service_mst.service_master_id = ord_dtls.service_id " + 
    		 "	left join doctor.m_speciality_master speciality_mst on " + 
    		 "speciality_mst.speciality_id = service_mst.speciality_id " + 
    		 "	left join t_unit_speciality_mapper unitSplMpr on " + 
    		 "unitSplMpr.speciality_id = service_mst.speciality_id " + 
    		 "and unitSplMpr.unit_id = service_mppr.unit_id " + 
    		 "	inner join lab.t_panel_master panel on " + 
    		 "panel.service_id = service_mst.service_master_id " + 
    		 "and panel.org_unit_id = service_mppr.unit_id " + 
    		 "	left outer join public.t_template_transaction templatetr12_ on " + 
    		 "ord_dtls.order_details_id = templatetr12_.order_details_id " + 
    		 "and( " + 
    		 "	templatetr12_.status = 'A' " + 
    		 ") " + 
    		 "	left join opd.m_appointment_master am on " + 
    		 "am.order_details_id = ord_dtls.order_details_id " + 
    		 "	left join t_unit_application_billing_configuration uabc on " + 
    		 "	uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A'	 " + 
    		 "left join public.m_user_master um on ord_mst.created_by = um.user_id " + 
    		 "left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
    		 "left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id " + 
    		 " where " + 
    		 "service_mst.status = 'A' " + 
    		 "and ord_dtls.old_ord_dtl_id is null " + 
    		 "and service_dtls.is_package = 'I' " + 
    		 "and service_dtls.is_panel = 'Y' " + 
    		 //"and unitSplMpr.is_rendering_speciality = 'Y' " +
    		 " and unitSplMpr.is_rendering_speciality in (select is_rendering_speciality from t_unit_speciality_mapper where is_rendering_speciality = 'Y' ) "
			+"	and ord_dtls.nursing_service_order_flag = 'N' " + 
    		 " and ord_dtls.is_procedure_service = 'N' " );
       
     	if(investigationOrderDto.getPatientId() != null) {
			sb.append(" and  ord_mst.patient_id  =:patientId ");
		}
		if(investigationOrderDto.getEncounterId() != null) {
			sb.append(" and  ord_mst.encounter_id =:encounterId ");
		}
		if(investigationOrderDto.getAdmissionId() != null) {
			sb.append("  and ord_mst.admission_id  =:admissionId ");
		}

		Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

		if(investigationOrderDto.getPatientId() != null)
			createdQuery.setParameter("patientId", investigationOrderDto.getPatientId());

		if(investigationOrderDto.getEncounterId() != null)
			createdQuery.setParameter("encounterId", investigationOrderDto.getEncounterId());

		if(investigationOrderDto.getAdmissionId() != null)
			createdQuery.setParameter("admissionId", investigationOrderDto.getAdmissionId());

		List<InvestigationOrderDto> investigationDTOsList = createdQuery
		.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);
	}

	@Override
	public Response updateDiagnosticsRemarks(InvestigationOrderDto investigationOrderDto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update public.t_order_details set ord_remarks =:ordRemarks "
			+ " where order_details_id =:orderDetailsId ")
			.setParameter("orderDetailsId", investigationOrderDto.getOrderDetailsId())
			.setParameter("ordRemarks", investigationOrderDto.getOrdRemarks())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelLabOrderForNewEMR(InvestigationOrderDto investigationOrderDto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update public.t_order_master set order_status= 'I'"
					+ " where order_id =:orderId "
					+ " and org_unit_id =:unitId "
					+ " and org_id =:organizationId ")
					.setParameter("orderId", investigationOrderDto.getOrderId())
					.setParameter("unitId", investigationOrderDto.getUnitId())
					.setParameter("organizationId", investigationOrderDto.getOrganizationId())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
