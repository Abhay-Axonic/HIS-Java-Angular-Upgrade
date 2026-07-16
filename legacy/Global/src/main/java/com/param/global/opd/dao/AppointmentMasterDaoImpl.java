package com.param.global.opd.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.sync.AdmissionAndDischargeSyncDto;
import com.param.global.dto.sync.AppointmentDetailsRealTimeDto;
import com.param.global.dto.sync.AppointmentDoctorCoversheetMapperDto;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.dto.sync.AppointmentVitalMapperSyncDto;
import com.param.global.dto.sync.DiagnosisAppoMapperSyncDto;
import com.param.global.dto.sync.MedicationAppointmentMapperSyncDto;
import com.param.global.dto.sync.RadiologySyncDto;
import com.param.global.dto.sync.ServiceappoServiceMpprSyncDto;
import com.param.opd.appointment.model.AppointmentMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentMasterDaoImpl extends GenericDao<AppointmentMaster, Integer> implements IAppointmentMasterDao, ICommonConstants{

	public AppointmentMasterDaoImpl() {
		super(AppointmentMaster.class);
	}

	@Override
	@Transactional
	public Response getDoctorAppointmentToSync(int appointmentId) throws ApplicationException {
		try{
			//System.out.println("Appointment Id to sync:"+appointmentId);
			AppointmentSyncSchedulingDto dto = (AppointmentSyncSchedulingDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_APPOINTMENT_TO_SYNC_BY_ID")
					.setParameter("id", appointmentId).setResultTransformer(Transformers.aliasToBean(AppointmentSyncSchedulingDto.class))
					.uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBasicAppointmentDetaistoSync(int appointmentId) throws ApplicationException {
		try{
			AppointmentDetailsRealTimeDto dto = (AppointmentDetailsRealTimeDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_APPOINTMENT_BASIC_DETAILS_TO_SYNC")
					.setParameter("id", appointmentId)
					.setResultTransformer(Transformers.aliasToBean(AppointmentDetailsRealTimeDto.class))
					.uniqueResult();
			
			if(dto!=null)
			{
				AppointmentDetailsRealTimeDto dto1 = (AppointmentDetailsRealTimeDto)sessionFactory.getCurrentSession()
						.getNamedQuery("GET_URL_FOR_PDF_GENERATION")
						.setParameter("orgId", dto.getHospitalId())
						.setParameter("unitId", dto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentDetailsRealTimeDto.class))
						.uniqueResult();
				dto.setPdfGenerationUrl(dto1.getPdfGenerationUrl());
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
			}
			return new Response<>(ERROR, ERROR_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAppointmentServiceDtlsToSync(int appointmentId) throws ApplicationException {
		try{
			List<ServiceappoServiceMpprSyncDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENT_SERVICE_DETAILS_TO_SYNC").setParameter("id", appointmentId).setResultTransformer(Transformers.aliasToBean(ServiceappoServiceMpprSyncDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAppointmentVitalsToSync(int appointmentId) throws ApplicationException {
		try{
			List<AppointmentVitalMapperSyncDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENT_VITAL_DETAILS_TO_SYNC").setParameter("id", appointmentId).setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperSyncDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getMedicationDetailsToSync(int appointmentId) throws ApplicationException {
		try{
			List<MedicationAppointmentMapperSyncDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_MEDICATIONS_TO_SYNC").setParameter("id", appointmentId).setResultTransformer(Transformers.aliasToBean(MedicationAppointmentMapperSyncDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDiagnosisDtlsToSync(int appointmentId) throws ApplicationException {
		try{
			List<DiagnosisAppoMapperSyncDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_DIAGNOSIS_DETAILS_TO_SYNC").setParameter("id", appointmentId).setResultTransformer(Transformers.aliasToBean(DiagnosisAppoMapperSyncDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response geAppointmentStatus(int appointmentId) {
		try{
			//appointment_status_id in(2,6)
			AppointmentMaster dto = (AppointmentMaster)sessionFactory.getCurrentSession()
					.createSQLQuery("select appm.appointment_status_id as \"appointmentStatusId\",appm.appointment_id as \"appointmentId\" from opd.m_appointment_master appm where appointment_status_id=6 and appointment_id=:appointmentId  ")
					.setParameter("appointmentId",appointmentId)
					.setResultTransformer(Transformers.aliasToBean(AppointmentMaster.class)).uniqueResult();
			if(dto!=null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
			}
			else {
				return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getIPPatientDetailsToSync(Integer admissionId) throws ApplicationException {
		try{
			AdmissionAndDischargeSyncDto admissionAndDischargeSyncDto=(AdmissionAndDischargeSyncDto)sessionFactory.getCurrentSession()
					.createSQLQuery("select admission_id as \"admissionId\" "
							+ " from adt.m_discharge_Summary where admission_id=:admissionId and status = 'A' ")
					.setParameter("admissionId",admissionId)
					.setResultTransformer(Transformers.aliasToBean(AdmissionAndDischargeSyncDto.class))
					.uniqueResult();
			if(admissionAndDischargeSyncDto!=null) 
			{
				AdmissionAndDischargeSyncDto dto = (AdmissionAndDischargeSyncDto)sessionFactory.getCurrentSession()
						.getNamedQuery("GET_IP_PATIENT_APPOINTMENT_TO_SYNC_BY_ID")
						.setParameter("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(AdmissionAndDischargeSyncDto.class))
						.uniqueResult();
			
				if(dto!=null)
				{
					AdmissionAndDischargeSyncDto dto1 = (AdmissionAndDischargeSyncDto)sessionFactory.getCurrentSession()
							.getNamedQuery("GET_URL_FOR_PDF_GENERATION")
							.setParameter("orgId", dto.getHospitalId())
							.setParameter("unitId", dto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(AdmissionAndDischargeSyncDto.class))
							.uniqueResult();
					dto.setPdfGenerationUrl(dto1.getPdfGenerationUrl());
					
					//get medication details by admission id
					List<MedicationAppointmentMapperSyncDto> medicationList = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_MEDICATION_MAPPER_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE")
							.setParameter("admissionId", admissionId)
							.setResultTransformer(Transformers.aliasToBean(MedicationAppointmentMapperSyncDto.class)).list();
					dto.setMedicationList(medicationList);
					
					//get Service details by admission id
					List<ServiceappoServiceMpprSyncDto> serviceList = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_SERVICE_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE")
							.setParameter("admissionId", admissionId)
							.setResultTransformer(Transformers.aliasToBean(ServiceappoServiceMpprSyncDto.class)).list();
					
					if(serviceList!=null && serviceList.size()>0) 
					{
						
						Query query1 = sessionFactory.getCurrentSession()
						            .createSQLQuery("select lab_speciality_id from public.t_lab_configuration " + 
						    		" where unit_id = "+dto.getUnitId()+" and organisation_id =:hospitalId ");
						query1.setParameter("hospitalId",dto.getHospitalId());
						Integer  pathalogyId =  (Integer) query1.uniqueResult();
						
						
						Query query = sessionFactory.getCurrentSession()
						            .createSQLQuery("select radiology_speciality_id from public.t_unit_application_billing_configuration "
									+ "where unit_id =:unitId and organization_id =:organizationId ");
						query.setParameter("unitId",dto.getUnitId());
						query.setParameter("organizationId",dto.getHospitalId());
						Integer  rediologyId =  (Integer) query.uniqueResult();
						   
						for (ServiceappoServiceMpprSyncDto serviceappoServiceMpprSyncDto : serviceList) 
						{
							
							if(pathalogyId != null && serviceappoServiceMpprSyncDto.getGroupType().equals(pathalogyId.toString())) {
								
								serviceappoServiceMpprSyncDto.setGroupTypeId(1); // 1 for pathalogyId speciality Id
								
							}else if(rediologyId != null &&  serviceappoServiceMpprSyncDto.getGroupType().equals(rediologyId.toString())) {
								
								serviceappoServiceMpprSyncDto.setGroupTypeId(2); // 2 for rediologyId speciality Id
								
							}else {
								
								serviceappoServiceMpprSyncDto.setGroupTypeId(3); // 3 for other speciality Id
							}
						}
						
						dto.setServiceAppoMapperList(serviceList);
					}
			
				
					//get Vital Details by admission id
					List<AppointmentVitalMapperSyncDto> vitalList = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_VITAL_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE")
							.setParameter("admissionId", admissionId)
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperSyncDto.class)).list();
					dto.setVitalMapperList(vitalList);
					
					//get Diagnosis details by admission id
					List<DiagnosisAppoMapperSyncDto> diagnosisList = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_DIAGNOSIS_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE")
							.setParameter("admissionId", admissionId)
							.setResultTransformer(Transformers.aliasToBean(DiagnosisAppoMapperSyncDto.class)).list();
					dto.setDiagnosisAppoMapperList(diagnosisList);
					
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
				}else {
					return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
				}
				
			}
			else {
				return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAppointmentDetails(int appointmentId) {
		try{
			AppointmentDoctorCoversheetMapperDto dto = (AppointmentDoctorCoversheetMapperDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_APPOINTMENT_DETAILS_TO_SYNC_BY_ID")
					.setParameter("id", appointmentId).setResultTransformer(Transformers.aliasToBean(AppointmentDoctorCoversheetMapperDto.class))
					.uniqueResult();
			AppointmentDetailsRealTimeDto dto1=new AppointmentDetailsRealTimeDto();
			dto1.setCoversheetMapper(dto);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto1);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response syncPatientPathalogyReport() throws ApplicationException {
		try{
		
			List<ServiceappoServiceMpprSyncDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATHALOGY_RELEASED_REPORT")
					.setResultTransformer(Transformers.aliasToBean(ServiceappoServiceMpprSyncDto.class)).list();
			String pdfUrl=null;
			if(list.size()>0) {
			ServiceappoServiceMpprSyncDto dto1 = (ServiceappoServiceMpprSyncDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_URL_FOR_PDF_GENERATION")
					.setParameter("orgId", list.getFirst().getHospitalId())
					.setParameter("unitId", list.getFirst().getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ServiceappoServiceMpprSyncDto.class))
					.uniqueResult();
			pdfUrl=dto1.getPdfGenerationUrl();
			}
			if(list.size()>0) {
			for(ServiceappoServiceMpprSyncDto list1:list) {
				list1.setPdfGenerationUrl(pdfUrl);
			}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response syncPatientRadiologyReport(Integer id) throws ApplicationException {
		try{
		String query="	select  " + 
				"	ttt.radiology_report_link as \"documentPath\",  " + 
				"	tpr.uuid as \"uuid\",  " + 
				"	mom.global_org_id as \"hospitalId\",  " + 
				"	mum.global_unit_id as \"unitId\",  " + 
				"	msm.service_standard_name as \"serviceName\"  " + 
				"from  " + 
				"	public.t_template_transaction ttt  " + 
				"inner join public.t_order_details tod on  " + 
				"	tod.order_details_id = ttt.order_details_id  " + 
				"inner join service.m_service_master msm on  " + 
				"	tod.service_id = msm.service_master_id  " + 
				"inner join patient.t_patient_registration tpr on  " + 
				"	tpr.patient_id = ttt.patient_id  " + 
				"inner join public.m_organization_master mom on  " + 
				"	mom.organization_id = ttt.org_id  " + 
				"inner join public.m_unit_master mum on  " + 
				"	mum.unit_id = ttt.unit_id  " + 
				"where  " + 
				"	ttt.template_transaction_id =:id "+
				"	and tpr.uuid is not null  " ;
			RadiologySyncDto obj = (RadiologySyncDto) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("id",id)
					.setResultTransformer(Transformers.aliasToBean(RadiologySyncDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		}catch(Exception e){
			
			throw e;
		}
	}

}