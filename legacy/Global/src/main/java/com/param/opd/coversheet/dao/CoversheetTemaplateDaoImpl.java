package com.param.opd.coversheet.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.InvestigationOrderMasterNewDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.model.CoversheetTemplateMaster;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CoversheetTemaplateDaoImpl extends GenericDao<CoversheetTemplateMaster, Integer> implements ICoversheetTemaplateDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(CoversheetTemaplateDaoImpl.class);



	public CoversheetTemaplateDaoImpl() {
		super(CoversheetTemplateMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveCoversheetAsTemaplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {/*
			CoversheetTemplateMaster mst=new CoversheetTemplateMaster();
			mst.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
			mst.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
			mst.setUnitId(coversheetTemplateMasterDto.getUnitId());
			mst.setStatus('A');
			mst.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
			mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			mst.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
			mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 
			sessionFactory.getCurrentSession().save(mst);
			coversheetTemplateMasterDto.setCovTemplateId(mst.getCovTemplateId());
		return new Response(SUCCESS, SUCCESS_CODE, null, null, coversheetTemplateMasterDto);
		
		*/
			if(coversheetTemplateMasterDto.getCovTemplateId() != null) {
			if(coversheetTemplateMasterDto.getTypeId() != 1) {
				CoversheetTemplateMaster mst=findById(coversheetTemplateMasterDto.getCovTemplateId());
				mst.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
				mst.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				mst.setUnitId(coversheetTemplateMasterDto.getUnitId());
				mst.setStatus('A');
				mst.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
//				mst.setParentTemplateId(coversheetTemplateMasterDto.getParentTemplateId());
				mst.setTypeId(coversheetTemplateMasterDto.getTypeId());
				mst.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
				mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				mst.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
				mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 
				sessionFactory.getCurrentSession().update(mst);
				coversheetTemplateMasterDto.setCovTemplateId(mst.getCovTemplateId());
			}else {
				CoversheetTemplateMaster mst1=findById(coversheetTemplateMasterDto.getCovTemplateId());
				mst1.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
				mst1.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				mst1.setUnitId(coversheetTemplateMasterDto.getUnitId());
				mst1.setStatus('A');
				mst1.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
//				mst.setParentTemplateId(coversheetTemplateMasterDto.getParentTemplateId());
				mst1.setTypeId(coversheetTemplateMasterDto.getTypeId());
				mst1.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
				mst1.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				mst1.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
				mst1.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 
				sessionFactory.getCurrentSession().save(mst1);
				coversheetTemplateMasterDto.setCovTemplateId(mst1.getCovTemplateId());
				Integer id=coversheetTemplateMasterDto.getCovTemplateId();
				if(coversheetTemplateMasterDto.getTemplateTypeList()!= null && coversheetTemplateMasterDto.getTemplateTypeList().size() > 0) {
					for (CoversheetTemplateMasterDto dto : coversheetTemplateMasterDto.getTemplateTypeList()) {
						CoversheetTemplateMaster mst=new CoversheetTemplateMaster();
						mst.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
						mst.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						mst.setUnitId(coversheetTemplateMasterDto.getUnitId());
						mst.setStatus('A');
						mst.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						mst.setParentTemplateId(id);
						mst.setTypeId(coversheetTemplateMasterDto.getTypeId());
						mst.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
						mst.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
						 
						sessionFactory.getCurrentSession().update(mst);
						coversheetTemplateMasterDto.setCovTemplateId(mst.getCovTemplateId());
					}
				}
			}
		}else {
			if(coversheetTemplateMasterDto.getTypeId() != 1) {
				CoversheetTemplateMaster mst=new CoversheetTemplateMaster();
				mst.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
				mst.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				mst.setUnitId(coversheetTemplateMasterDto.getUnitId());
				mst.setStatus('A');
				mst.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
//				mst.setParentTemplateId(coversheetTemplateMasterDto.getParentTemplateId());
				mst.setTypeId(coversheetTemplateMasterDto.getTypeId());
				mst.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
				mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				mst.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
				mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 
				sessionFactory.getCurrentSession().save(mst);
				coversheetTemplateMasterDto.setCovTemplateId(mst.getCovTemplateId());
			}else {
				CoversheetTemplateMaster mst1=new CoversheetTemplateMaster();
				mst1.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
				mst1.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				mst1.setUnitId(coversheetTemplateMasterDto.getUnitId());
				mst1.setStatus('A');
				mst1.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
//				mst.setParentTemplateId(coversheetTemplateMasterDto.getParentTemplateId());
				mst1.setTypeId(coversheetTemplateMasterDto.getTypeId());
				mst1.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
				mst1.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				mst1.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
				mst1.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 
				sessionFactory.getCurrentSession().save(mst1);
				coversheetTemplateMasterDto.setCovTemplateId(mst1.getCovTemplateId());
				Integer id=coversheetTemplateMasterDto.getCovTemplateId();
				if(coversheetTemplateMasterDto.getTemplateTypeList()!= null && coversheetTemplateMasterDto.getTemplateTypeList().size() > 0) {
					for (CoversheetTemplateMasterDto dto : coversheetTemplateMasterDto.getTemplateTypeList()) {
						CoversheetTemplateMaster mst=new CoversheetTemplateMaster();
						mst.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
						mst.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						mst.setUnitId(coversheetTemplateMasterDto.getUnitId());
						mst.setStatus('A');
						mst.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						mst.setParentTemplateId(id);
						mst.setTypeId(coversheetTemplateMasterDto.getTypeId());
						mst.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
						mst.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
						 
						sessionFactory.getCurrentSession().save(mst);
						coversheetTemplateMasterDto.setCovTemplateId(mst.getCovTemplateId());
					}
				}
			}
		}
		return new Response(SUCCESS, SUCCESS_CODE, null, null, coversheetTemplateMasterDto);
		
		
		} catch (HibernateException he) {
		logger.error("saveCoversheetAsTemaplate failed", he);
		} catch (Exception e) {
		logger.error("saveCoversheetAsTemaplate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCoversheetAsTemaplateList(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			String query = "select " + 
					"	cov.cov_template_id as \"covTemplateId\" ," + 
					"	cov.template_name as \"templateName\" " + 
					"from " + 
					"	opd.t_coversheet_template_master cov "
					+ " where cov.organization_id=:organizationId "
					+ " and cov.unit_id=:unitId";
			StringBuffer subQuery=new StringBuffer();
			if(coversheetTemplateMasterDto.getTypeId() != null && coversheetTemplateMasterDto.getTypeId() == 1) {
				subQuery.append(" AND cov.type_id = :typeId");
//				subQuery.append(" and cov.doctor_id ="+coversheetTemplateMasterDto.getDoctorId());
//				subQuery.append(" and cov.parent_template_id is not null");
			}
			if(coversheetTemplateMasterDto.getTypeId() != null && (coversheetTemplateMasterDto.getTypeId() == 2 || coversheetTemplateMasterDto.getTypeId() == 3 || coversheetTemplateMasterDto.getTypeId() == 4)) {
				subQuery.append(" AND cov.type_id = :typeId");
//				subQuery.append(" and cov.doctor_id ="+coversheetTemplateMasterDto.getDoctorId());
				subQuery.append(" and cov.parent_template_id is null");
			}
			if(coversheetTemplateMasterDto.getDoctorId() != null) {
				subQuery.append(" AND cov.doctor_id = :doctorId");
			}

			subQuery.append(" ORDER BY cov.template_name ASC");
			query = query.concat(subQuery.toString());	

					Query createQuery= sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
				.setParameter("unitId", coversheetTemplateMasterDto.getUnitId());

			if (coversheetTemplateMasterDto.getTypeId() != null) {
				createQuery.setParameter("typeId", coversheetTemplateMasterDto.getTypeId());
			}
			if (coversheetTemplateMasterDto.getDoctorId() != null) {
				createQuery.setParameter("doctorId", coversheetTemplateMasterDto.getDoctorId());
			}


			List<CoversheetTemplateMasterDto> list=	createQuery.setResultTransformer(Transformers.aliasToBean(CoversheetTemplateMasterDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			
	} catch (HibernateException he) {
	logger.error("getCoversheetAsTemaplateList failed", he);
	} catch (Exception e) {
	logger.error("getCoversheetAsTemaplateList failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response checkAvailableCoversheetAsTemaplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			String query = "select "
					+ " cov.cov_template_id as \"covTemplateId\" ," 
					+ " cov.template_name as \"templateName\" " 
					+ " from " 
					+ " opd.t_coversheet_template_master cov "
					+ " where cov.organization_id=:organizationId "
					+ " and cov.unit_id=:unitId "
					+ " and lower(cov.template_name) like '"+coversheetTemplateMasterDto.getTemplateName()+"%'";
			List<MedicationHistoryDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CoversheetTemplateMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
	} catch (HibernateException he) {
	logger.error("checkAvailableCoversheetAsTemaplate failed", he);
	} catch (Exception e) {
	logger.error("checkAvailableCoversheetAsTemaplate failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getAllCoversheetListsByTemplateId(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			
			List<ComplaintAppointmentDto> listComplaintAppointmentMapper = sessionFactory.getCurrentSession().getNamedQuery("GET_COMPLAINT_APPOINTMENT_MAPPER_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentDto.class)).list();
			coversheetTemplateMasterDto.setComplaintsPatientList(listComplaintAppointmentMapper);
			
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_PATIENT_SURGICAL_HISTORY_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();
			coversheetTemplateMasterDto.setSurgicalHistoryCurrentList(listPatientSurgicalHistoryDto);
			
			List<PatientMedicalHistoryDto> listPatientMedicalHistoryDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_PATIENT_MEDICAL_HISTORY_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
			coversheetTemplateMasterDto.setMedicalHistoryCurrentList(listPatientMedicalHistoryDto);
			
			
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_PATIENT_HABIT_DETAILS_LIST_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
			coversheetTemplateMasterDto.setPatientHabitList(listPatientHabitDetailsDto);
			
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			coversheetTemplateMasterDto.setAllergyCurrentList(listAllergyPatientMapperDto);
			
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_PATIENT_TREATMENT_HISTORY_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			coversheetTemplateMasterDto.setTreatmentHistoryCurrentList(listPatientTreatmentHistoryDetailsDto);
			
			List<MedicationHistoryDto> listMedicationHistoryDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEDICATION_HISTORY_LIST_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			coversheetTemplateMasterDto.setDetailedMedicationsHistory(listMedicationHistoryDto);
			
			List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_PATIENT_FAMILY_HISTORY_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
			coversheetTemplateMasterDto.setFamilyHistoryCurrentList(listPatientFamilyHistoryDto);
			
			List<PatientPersonalHistoryDetailsDto> listPatientPersonalHistoryDetailsDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class)).list();
			coversheetTemplateMasterDto.setPersonalHistoryCurrentList(listPatientPersonalHistoryDetailsDto);
			
			List<PatientTravelHistoryDetailsDto> listPatientTravelHistoryDetailsDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENT_PATIENT_TRAVEL_HISTORY_DEATILS_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();
			coversheetTemplateMasterDto.setTravelHistoryCurrentList(listPatientTravelHistoryDetailsDto);
			
			List<MedicationDTO> listMedication=new ArrayList<>();
			String query = 
					  " SELECT "
					+ " pres.id as \"prescriptionId\", "
					+ " pres.medication_type as \"medicationType\", "
					+ " pres.status_id as \"statusIdPres\", "
					+ " presDtl.is_medication_stop as \"isMedicationStop\", "
					+ " presDtl.priority_id as \"priorityId\", "
					+ " presDtl.duration as \"durationCount\", "
					+ " presDtl.duration_unit as \"durationUnit\", "
					+ " presDtl.justification_stop \"justificationStop\", "
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " presDtl.item_id as \"itemId\", "
//					+ " issue.status_id as \"statusIdIssue\","  
//					+ " itm.name as \"itemName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " case when (itm.name is null) then presDtl.item_name else itm.name end AS \"itemName\", "
					+ " case when (gen.name is null) then presDtl.generic_name else gen.name end AS \"genericName\", "
					+ " su.unit as \"strengthDesc\", " 
                    + " su.id as \"doseUnitId\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " presDtl.is_non_formulary as \"isNonFormulary\", "
					+ " presDtl.status_id as \"statusId\", "
					+ " presDtl.statflag as \"statflag\", "
					+ " roa.description as \"roaDesc\", "
					+ " presDtl.order_qty as \"orderQty\", "
					+ " itm.generic_id as \"genericId\", "
					+ " itm.formulation_type_id as \"formulationTypeId\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " to_char(presDtl.added_date,'dd/mm/yyyy') as \"addedDate\"," 
					+ " to_char(presDtl.added_date, 'HH24:MI') as \"addedTime\"," 
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\" , "
					+ " pres. is_otims as \"isOtims\","
					+ " (case " 
					+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "  
					+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
					+ " else 'Admin' end) as \"userName\","	
//					+ " gen.name as \"genericName\","
					+ " presDtl.is_discharge_medication as \"isDischargeMedication\",presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
					+ " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
					+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " left JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
//					+ "  LEFT JOIN pharmacy.t_issue_detail issue ON issue.prescription_detail_id = presDtl.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
					+ " left JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " left JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
					+ " left join public.m_user_master um on pres.added_by = um.user_id " 
					+ " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
					+ " left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
//					+ " WHERE pres.patient_id=:patientId "
//			        + " AND pres.doctor_id=:doctorId "
			        + " WHERE presDtl.is_medication_stop='N'  "
			        + " AND presDtl.status_id != 9 "
					;
			
			StringBuffer sb = new StringBuffer(query);
			
			if(coversheetTemplateMasterDto.getCovTemplateId()!=null){
				//sb.append(" AND pres.admission_id =" +coversheetTemplateMasterDto.getCovTemplateId());
				sb.append(" AND pres.admission_id = :covTemplateId");

			}
			sb.append("  order by pres.id asc" );

			listMedication= getSession().createSQLQuery(sb.toString()).setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
			.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			coversheetTemplateMasterDto.setDetailedMedications(listMedication);
			
			List<ObservationDto> listObservation=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OBSERVATION_LIST_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ObservationDto.class)).list();
			coversheetTemplateMasterDto.setObservationList(listObservation);
			
			List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COV_TEMPLATE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
			coversheetTemplateMasterDto.setPatientViatalList(listAppointmentVitalMapperDto);
			
			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_DIAGNOSIS_COV_TEMPALTE")
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			coversheetTemplateMasterDto.setPatientDiagnosisList(diagnosisPatientAppointmentMapperList);
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, coversheetTemplateMasterDto);
			
			
	} catch (HibernateException he) {
	logger.error("getAllCoversheetListsByTemplateId failed", he);
	} catch (Exception e) {
	logger.error("getAllCoversheetListsByTemplateId failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getTemplateDataByCovTempId(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			String query = "select " + 
					"	cov.cov_template_id as \"covTemplateId\" ," + 
					"	cov.type_id as \"typeId\", " + 
					"	cov.template_name as \"templateName\" " + 
					"from " + 
					"	opd.t_coversheet_template_master cov "
					+ " where "
					+ " cov.organization_id=:organizationId "
					+ " and cov.cov_template_id=:covTemplateId "
					+ " and cov.unit_id=:unitId";
			
			StringBuffer subQuery=new StringBuffer();
//			if(coversheetTemplateMasterDto.getTypeId() != null && coversheetTemplateMasterDto.getTypeId() == 1) {
//				subQuery.append(" and cov.type_id ="+coversheetTemplateMasterDto.getTypeId());
//				subQuery.append(" and cov.doctor_id ="+coversheetTemplateMasterDto.getDoctorId());
////				subQuery.append(" and cov.parent_template_id is not null");
//			}
//			if(coversheetTemplateMasterDto.getTypeId() != null && (coversheetTemplateMasterDto.getTypeId() == 2 || coversheetTemplateMasterDto.getTypeId() == 3 || coversheetTemplateMasterDto.getTypeId() == 4)) {
//				subQuery.append(" and cov.type_id ="+coversheetTemplateMasterDto.getTypeId()+"");
//				subQuery.append(" and cov.doctor_id ="+coversheetTemplateMasterDto.getDoctorId());
//				subQuery.append(" and cov.parent_template_id is null");
//			}
			query = query.concat(subQuery.toString());	
			List<CoversheetTemplateMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
				.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(CoversheetTemplateMasterDto.class)).list();
			
			for (CoversheetTemplateMasterDto mainDto : list) {
				
				/*List<ComplaintAppointmentMapperDto> listComplaints=sessionFactory.getCurrentSession()
						.createSQLQuery("select " + "	complaintAppointment.complaint_appo_id as \"complaintAppoId\", "
								+ "	icpc1_.icpc_desc as \"diagnosisName\", " + "	icpc1_.icpc_code as \"icpcCode\", "
								+ "	complaintAppointment.icpc_code_text as \"icpcCodeText\", "
								+ "	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\" " + "from "
								+ "	patient.t_complaint_appo_mapper complaintAppointment "
								+ "inner join emr.m_icpc icpc1_ on "
								+ "	complaintAppointment.diagnosis_id = icpc1_.icpc_id "
								+ "left join emr.m_icpc_system icpcsystem2_ on "
								+ "	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id " 
								+ " where "
								+ "	 complaintAppointment.status = 'A' "
								+ "	and complaintAppointment.is_resolved = 'N' "
								+ "	and complaintAppointment.organization_id =:organizationId "
								+ "	and complaintAppointment.unit_id =:unitId "
								+ "	and complaintAppointment.cov_template_id =:covTemplateId "
								+ "	and(complaintAppointment.is_complaint_stop = 'N')")
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("covTemplateId", mainDto.getCovTemplateId())
					.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentMapperDto.class)).list();
				    mainDto.setListComplaintData(listComplaints);*/
				    
				    if(mainDto.getTypeId() == 1) {
				    	List<DiagnosisPatientAppointmentMapperDto> listDiagnosis=sessionFactory.getCurrentSession()
								.createQuery("SELECT " + " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
										+ " diagMapper.admissionId AS admissionId, " + " diagMapper.isDeleted AS isDeleted, "
										+ " diagMapper.patientId AS patientId, "
										+ " diagMapper.diagnosisText AS diagnosisText, "
										+ " diagMapper.diagnosisId AS diagnosisId, " 
										+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
										+ " diagMapper.created_date as created_date_new, "
										+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
										+ " diagMaster.code AS code,"
										+ " diagMaster.diagnosisName AS diagnosisName "
										+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
										+ " left JOIN diagMapper.diagnosisMaster diagMaster "
										+ " WHERE " + "  diagMapper.status='A'"
										+ " AND diagMapper.covTemplateId=:covTemplateId"
//										+ " AND diagMapper.isDiagnosisCancel='N'" 
										+ " AND diagMapper.status='A'"
										+ " AND diagMapper.isDeleted='N'"
										+ " AND diagMapper.isResolved='N'" 
										+ " AND diagMapper.unitId=:unitId"
										+ " AND diagMapper.organizationId=:organizationId"
										+ " order by diagMapper.patientDiagnosisId desc")
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
				    	
				    	mainDto.setListDiagnosisData(listDiagnosis);
				    }
				
				
				
				    if(mainDto.getTypeId() == 1 || mainDto.getTypeId() == 2) {
				    	List<InvestigationOrderMasterNewDto> listlabOrder=sessionFactory.getCurrentSession()
								.createSQLQuery(/*"select " + 
										"	ord_mst.order_id as \"orderId\", "+
										"	service_mst.speciality_id as \"specialityId\", " + 
										"	service_mst.sub_speciality_id as \"subDeptId\", " + 
										"	service_mst.sub_speciality_id as \"subSpecialityId\", " + 
										"	ord_dtls.is_procedure_service as \"isProcedureService\", " + 
										"	ord_mst.admission_id as \"admissionId\", " + 
										"	ord_mst.encounter_id as \"encounterId\", " + 
										"	ord_mst.patient_id as \"patientId\", " + 
										"	ord_dtls.order_details_id as \"orderDetailsId\", " + 
										"	ord_dtls.service_id as \"serviceMasterId\", " + 
										"	ord_dtls.order_set_id as \"orderSetId\", " + 
										"	ord_dtls.priority_id as \"priorityId\", " + 
										"	ord_dtls.ord_remarks as \"ordRemarks\", " + 
										"	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\", " + 
										"	ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
										"	service_mst.service_standard_name as \"serviceStandardName\", " + 
										"	service_mst.service_standard_code as \"serviceStandardCode\", " + 
										"	speciality_mst.speciality_name as \"specialityName\", " + 
										"	sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
										"	service_dtls.is_panel as \"isPanel\", " + 
										"	lab_res.lab_test_res_id as \"labTestResId\", " + 
										"	cast( null as integer ) as \"panelId\", " + 
										"	(case " + 
										"		when sample_dtls.micro_template_status_id is not null then test_status2.status_name " + 
										"		else test_status.status_name " + 
										"	end) as \"statusName\", " + 
										"	(case " + 
										"		when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id " + 
										"		else test_status.test_status_id " + 
										"	end) as \"sampleStatusId\", " + 
										"	to_char(ord_dtls.created_date, 'dd/MM/yyyy HH24:MI' ) as \"createdDate\", " + 
										"	to_char( sample_dtls.sample_gen_datetime, 'dd/MM/yyyy HH24:MI' ) as \"sampleGenDatetime\", " + 
										"	ord_dtls.created_date as \"createdDateNew\", " + 
										"	ord_dtls.service_rendered as \"serviceRendered\", " + 
										"	ord_dtls.is_package_service as \"isPackageService\", " + 
										"	templatetr12_.template_transaction_id as \"templateTransactionId\", " + 
										"	ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
										"	ord_dtls.status as \"orderStatus\", " + 
										"	sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , " + 
										"	(case " + 
										"		when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
										"		when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) " + 
										"		when sample_dtls.lab_sample_dtls_id is null then concat(' ') " + 
										"		else 'Super Admin' " + 
										"	end) as \"collectedUserName\", " + 
										"	(case " + 
										"		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
										"		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
										"		else 'Admin' " + 
										"	end) as \"userName\", " + 
										"	ord_dtls.package_id as \"packageId\" " + 
										"from " + 
										"	public.t_order_master ord_mst " + 
										"inner join public.t_order_details ord_dtls on " + 
										"	ord_mst.order_id = ord_dtls.order_id " + 
										"inner join service.t_unit_service_mapper service_mppr on " + 
										"	service_mppr.service_id = ord_dtls.service_id " + 
										"	and service_mppr.unit_id = ord_dtls.org_unit_id " + 
										"inner join service.t_unit_service_master_details service_dtls on " + 
										"	service_dtls.service_master_id = service_mppr.service_id " + 
										"	and service_dtls.unit_id = ord_dtls.org_unit_id " + 
										"inner join service.m_service_master service_mst on " + 
										"	service_mst.service_master_id = ord_dtls.service_id " + 
										"left join doctor.m_speciality_master speciality_mst on " + 
										"	speciality_mst.speciality_id = service_mst.speciality_id " + 
										"left join lab.t_lab_sample_dtls sample_dtls on " + 
										"	sample_dtls.order_details_id = ord_dtls.order_details_id " + 
										"	and sample_dtls.sample_status_id <> 4 " + 
										"left join lab.m_test_status_master test_status on " + 
										"	test_status.test_status_id = sample_dtls.sample_status_id " + 
										"left join lab.m_test_status_master test_status2 on " + 
										"	test_status2.test_status_id = sample_dtls.micro_template_status_id " + 
										"left join lab.t_lab_test_result lab_res on " + 
										"	lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
										"	and lab_res.sample_status_id in( 8, 9, 6, 17) " + 
										"	and lab_res.status = 'A' " + 
										"left outer join public.t_template_transaction templatetr12_ on " + 
										"	ord_dtls.order_details_id = templatetr12_.order_details_id " + 
										"	and( templatetr12_.status = 'A' ) " + 
										"left join public.m_employee_master empmst on " + 
										"	ord_mst.created_by = empmst.user_id " + 
										"left join doctor.m_doctor_master docmst on " + 
										"	ord_mst.created_by = docmst.user_id " + 
										"left join lab.t_microbiology_template microTemp on " + 
										"	microTemp.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
										"left join public.m_employee_master empmst1 on " + 
										"	sample_dtls.sample_collection_by = empmst1.user_id " + 
										"left join doctor.m_doctor_master docmst1 on " + 
										"	sample_dtls.sample_collection_by = docmst1.user_id " + 
										"where " + 
										"	service_mst.status = 'A' " + 
										"	and ord_dtls.old_ord_dtl_id is null " + 
										"	and service_dtls.is_package = 'I' " + 
										"	and ord_dtls.nursing_service_order_flag = 'N' " + 
										"	and service_dtls.is_panel = 'N' " + 
										"	and speciality_mst.speciality_id in( " + 
										"	select " + 
										"		speciality_id " + 
										"	from " + 
										"		t_unit_speciality_mapper " + 
										"	where " + 
										"		ord_mst.org_id =:organizationId " + 
										"		and ord_dtls.cov_template_id =:covTemplateId " + 
										"		and ord_dtls.status ='A' " + 
										"		and ord_mst.org_unit_id =:unitId )"
										
										*/
										
										"select  " + 
										"	ord_mst.order_id as \"orderId\",  " + 
										"	service_mst.speciality_id as \"specialityId\",  " + 
										"	service_mst.sub_speciality_id as \"subDeptId\",  " + 
										"	service_mst.sub_speciality_id as \"subSpecialityId\",  " + 
										"	ord_dtls.order_details_id as \"orderDetailsId\",  " + 
										"	ord_dtls.service_id as \"serviceMasterId\",  " + 
										"	ord_dtls.order_set_id as \"orderSetId\",  " + 
										"	ord_dtls.priority_id as \"priorityId\",  " + 
										"	ord_dtls.ord_remarks as \"ordRemarks\",  " + 
										"	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\",  " + 
										"	ord_dtls.order_sch_date as \"orderSchDateNew\",  " + 
										"	service_mst.service_standard_name as \"serviceStandardName\",  " + 
										"	ord_dtls.created_date as \"createdDateNew\",  " + 
										"	ord_dtls.service_rendered as \"serviceRendered\",  " + 
										"	ord_dtls.is_package_service as \"isPackageService\",  " + 
										"	ord_dtls.service_is_billed as \"serviceIsBilled\",  " + 
										"	ord_dtls.status as \"orderStatus\",  " + 
										"	ord_dtls.package_id as \"packageId\"  " + 
										"from  " + 
										"	public.t_order_master ord_mst  " + 
										"inner join public.t_order_details ord_dtls on  " + 
										"	ord_mst.order_id = ord_dtls.order_id  " + 
										"inner join service.t_unit_service_mapper service_mppr on  " + 
										"	service_mppr.service_id = ord_dtls.service_id  " + 
										"	and service_mppr.unit_id = ord_dtls.org_unit_id  " + 
										"inner join service.t_unit_service_master_details service_dtls on  " + 
										"	service_dtls.service_master_id = service_mppr.service_id  " + 
										"	and service_dtls.unit_id = ord_dtls.org_unit_id  " + 
										"inner join service.m_service_master service_mst on  " + 
										"	service_mst.service_master_id = ord_dtls.service_id  " + 
										" left join doctor.m_speciality_master speciality_mst on  " + 
										"	speciality_mst.speciality_id = service_mst.speciality_id  " + 
										" " + 
										"where  " + 
										"speciality_mst.speciality_id in(  " + 
										"	select  " + 
										"		speciality_id  " + 
										"	from  " + 
										"		t_unit_speciality_mapper  " + 
										"	where  " + 
										"		ord_mst.org_id =:organizationId  " + 
										"		and ord_dtls.status = 'A'  " + 
										"		and ord_mst.org_unit_id =:unitId )  " + 
										"	and service_dtls.is_package = 'I'  " + 
										"	and ord_dtls.nursing_service_order_flag = 'N'  " + 
										"	and service_dtls.is_panel = 'N' "+
										"   and service_dtls.unit_id =:unitId" + 
										"	and service_mst.status = 'A'  " + 
										"	and ord_dtls.old_ord_dtl_id is null  " + 
										"	and ord_dtls.cov_template_id =:covTemplateId  " + 
										"	and ord_dtls.status = 'A' "
										
										)
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
				    	
				    	List<InvestigationOrderMasterNewDto> listlabOrderFreeText=sessionFactory.getCurrentSession()
								.createSQLQuery("select " + 
										"	tftos.patient_id as \"patientId\", " + 
										"	tftos.admission_id as \"admissionId\", " + 
					                    " tftos.free_text_ordered_services_id as \"freeTextOrderedServicesId\", " + 
										"	tftos.encounter_id as \"encounterId\", " + 
										"	tftos.order_details_id as \"orderDetailsId\", " + 
										"	CURRENT_TIMESTAMP as \"orderSchDateNew\", " + 
										"	tftos.service_text as \"serviceText\", " + 
										"	CURRENT_TIMESTAMP as \"createdDateNew\" " + 
										" from " + 
										"	public.t_free_text_ordered_services tftos " + 
										" left join public.t_order_details ord_dtls on " + 
										"	tftos.order_details_id = ord_dtls.order_details_id " + 
										"	where " + 
										"		tftos.organization_id =:organizationId " + 
										"		and tftos.cov_template_id =:covTemplateId " + 
										"		and tftos.status ='A' " + 
										"		and tftos.unit_id =:unitId "
										
										)
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
				    		listlabOrder.addAll(listlabOrderFreeText);
				    		mainDto.setListLabOrderData(listlabOrder);
				    }
				
				
				    if(mainDto.getTypeId() == 1 || mainDto.getTypeId() == 4) {
				    	List<CoversheetInstructionDto> listAdvice=sessionFactory.getCurrentSession()
								.createQuery("SELECT cover.instructionId as instructionId, "
										+ " cover.instructionDetailsId as instructionDetailsId, "
										+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
										+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction "
										+ " FROM CoversheetInstruction cover "
										+ " LEFT JOIN cover.instructionMaster instr "+
										" where " + 
										"	 cover.organizationId =:organizationId " +
										"	 and cover.unitId =:unitId " +
										"	 and cover.status ='A' " +
										"	 and cover.covTemplateId =:covTemplateId "
										+ " and (cover.instructionDetailsId is not null or cover.advicedInstruction is not null )" 

										)
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
				    	mainDto.setListAdviceData(listAdvice);
				    }
				
				
				if(mainDto.getTypeId() == 1 || mainDto.getTypeId() == 3) {
					String pattern = "yyyy-MM-dd";
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//				    String toDayDate = simpleDateFormat.format(new Date());
				    String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					List<MedicationDTO> listMedication=sessionFactory.getCurrentSession()
							.createSQLQuery( "SELECT "
									+ " pres.id as \"prescriptionId\", "+
									" pres.visit_type_id as \"visitTypeId\", "+
									"   coalesce((select CAST((b.rate) as decimal(10, 2))     " + 
									" from inventory.t_batch b    " + 
									" inner join inventory.t_store_stock S    " + 
									" on B.id = S.batch_id    " + 
									" where b.item_id = itm.id   " + 
									" and s.store_id in (   " + 
									"  select store_id    " + 
									" from m_store_item_mapper msim    " + 
									" where item_id = itm.id and unit_id = :unitId and store_id in    " + 
									" ( select ms.store_id from m_store ms   " + 
									" where ms.unit_id = :unitId and ms.is_pharmacy = true))    " + 
									" and b.unit_id = :unitId   " + 
									" and s.unit_id = :unitId   " + 
									" and to_char(b.expiry_date, 'yyyy-mm-dd') >='" + toDayDate+"'"+
									" and b.is_quarantine = false   " + 
									" order by b.id desc limit 1),0) as \"rate\","
									+ " presDtl.is_medication_stop as \"isMedicationStop\", "
				                    + " presDtl.justification_stop as \"justificationStop\", "
									+ " presDtl.priority_id as \"priorityId\", "
				                    + " presDtl.duration as \"duration\", "
				                    + " presDtl.duration as \"durationCount\", "
									+ " presDtl.duration_unit as \"durationUnit\", "+
				                    	" presDtl.statflag as \"isStat\",  "
				                    + " presDtl.instruction_desc \"instructionText\", "
				                    + "  presDtl.id as \"prescriptionDetailsid\","
									+ " presDtl.item_id as \"itemId\", "
									 + "itm.formulation_type_id as \"formulationTypeId\", "
									 + "itm.generic_id as \"genericId\", "
									+ " case when (itm.name is null) then presDtl.item_name else itm.name end AS \"itemName\", "
									+ " case when (gen.name is null) then presDtl.generic_name else gen.name end AS \"genericName\", "
									+ " preFreDtl.dose as \"doseDesc\", "
									+ "su.id as \"doseUnitId\", "
									+ " presDtl.strength as \"strength\","
									+ " su.unit as \"strengthDesc\", " 
									+ " preFreDtl.frequency_id as \"frequencyId\", "
									+ " presDtl.is_non_formulary as \"isNonFormulary\", "
									+ " freq.description as \"frequencyDesc\", "
									+ " freq.no_of_administration_time as \"noOfAdministrationTime\", "
									+ " preFreDtl.route_id as \"roaId\", "
									+ " roa.description as \"roaDesc\", "
									//+ " presDtl.duration as \"duration\"," 
									+ " presDtl.order_qty as \"quantity\", "
									+ " presDtl.order_qty as \"orderQty\", "
									+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\","
									+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"medicationStartDate\"," 
									+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\"," 
									+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"medicationEndDate\"," 
									+ " preFreDtl.start_date as \"startDateNew\"," 
									+ " preFreDtl.stop_date as \"endDateNew\", "
									+ " presDtl.instruction_id as \"instructionId\" ,"
									+" prmst.priority_code as \"priorityCode\", " 
									+ " instr.instruction_desc as \"instructionDesc\", "
//									+ " gen.name as \"genericName\", "
									+ "presDtl.is_discharge_medication as \"isDischargeMedication\","
									+ "presDtl.is_discharge_medication as \"isDischarge\","

									+ " pres.encounter_id as \"encounterId\" ,"
									+ " concat('Dr. ',coalesce(doctmst.first_name),' ',coalesce(doctmst.middle_name),' ',coalesce(doctmst.last_name)) as \"doctorName\","
									+ " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
									+ " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
									+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" "+
									" from " + 
									"	pharmacy.t_prescription pres " 
									+ " LEFT JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
									+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
									+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
									+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
									+ " LEFT JOIN public.m_item itm on itm.id=presDtl.item_id "
									+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
									+ " LEFT JOIN public.m_generic gen on itm.generic_id = gen.id "
									+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
									+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
									+ " LEFT JOIN doctor.m_doctor_master doctmst on doctmst.doctor_id=pres.doctor_id "
									+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
									+ " LEFT JOIN adt.m_priority_master prmst on prmst.priority_id=presDtl.priority_id "+
									" where "  
									+ "  pres.organization_id =:organizationId"
									+ "  and pres.unit_id =:unitId"
									+ " and presDtl.cov_template_id =:covTemplateId"
									+ " and presDtl.is_active = true"
									
									/* " select pres.id as \"prescriptionId\",    " + 
									 "	presDtl.is_medication_stop as \"isMedicationStop\",    " + 
									 "	presDtl.priority_id as \"priorityId\",    " + 
									 "	pres.added_by as \"addedBy\",    "
									 + " pres.visit_type_id as \"visitTypeId\",  " + 
									 "	presDtl.duration as \"durationCount\",    " + 
									 "	presDtl.duration_unit as \"\",    " + 
									 "	presDtl.justification_stop as \"justificationStop\",    " + 
									 "	presDtl.instruction_desc as \"instructionText\",    " + 
									 "	presDtl.item_id as \"itemId\",    " + 
									 "	preFreDtl.dose as \"doseDesc\",    " + 
									 "	preFreDtl.start_date as \"startDateNew\", " + 
										"	preFreDtl.stop_date as \"endDateNew\", " + 
									 "	case    " + 
									 "		when (itm.name is null) then presDtl.item_name    " + 
									 "		else itm.name    " + 
									 "	end as \"itemName\",    " + 
									 "	case    " + 
									 "		when (gen.name is null) then presDtl.generic_name    " + 
									 "		else gen.name    " + 
									 "	end as \"genericName\",    " + 
									 "	su.unit as \"strengthDesc\",    " + 
									 "	su.id as \"doseUnitId\",    " + 
									 "	preFreDtl.frequency_id as \"frequencyId\",    " + 
									 "	freq.description as \"frequencyDesc\",    " + 
									 "	preFreDtl.route_id as \"roaId\",    " + 
									 "	presDtl.is_non_formulary as \"isNonFormulary\",    " + 
									 "	presDtl.status_id as \"statusId\",    " + 
									 "	presDtl.statflag as \"statflag\",    " + 
									 "	roa.description as \"roaDesc\",    " + 
									 "	presDtl.order_qty as \"orderQty\",    " + 
									 "	itm.generic_id as \"genericId\",    " + 
									 "	itm.formulation_type_id as \"formulationTypeId\",    " + 
									 "	to_char(preFreDtl.start_date, 'dd/mm/yyyy') as \"startDate\",    " + 
									 "	to_char(preFreDtl.stop_date, 'dd/mm/yyyy') as \"endDate\",    " + 
									 "	to_char(presDtl.added_date, 'dd/mm/yyyy') as \"addedDate\",    " + 
									 "	to_char(presDtl.added_date, 'HH24:MI') as \"addedTime\",    " + 
									 "	presDtl.instruction_id as \"instructionId\" ,    " + 
									 "	instr.instruction_desc as \"instructionDesc\",    " + 
									 "	presDtl.id as \"prescriptionDetailsid\",    " + 
									 "	preFreDtl.id as \"prescriptionFrequencyId\" " + 
										" from   " + 
										"	pharmacy.t_prescription pres   " + 
										" left join pharmacy.t_prescription_detail presDtl on   " + 
										"	presDtl.prescription_id = pres.id   " + 
										"inner join pharmacy.t_prescription_frequency_detail preFreDtl on   " + 
										"	preFreDtl.prescription_detail_id = presDtl.id   " + 
										"left join public.m_strength_unit su on   " + 
										"	preFreDtl.dose_unit_id = su.id   " + 
										"left join public.m_strength_unit presDose on   " + 
										"	presDtl.prescribe_dose_unit_id = presDose.id   " + 
										"left join public.m_item itm on   " + 
										"	itm.id = presDtl.item_id   " + 
										"left join public.m_route_of_administration roa on   " + 
										"	roa.id = preFreDtl.route_id   " + 
										"left join public.m_generic gen on   " + 
										"	itm.generic_id = gen.id   " + 
										"inner join public.m_frequency freq on   " + 
										"	freq.id = preFreDtl.frequency_id   " + 
										"left join public.m_instruction instr on   " + 
										"	instr.instruction_id = presDtl.instruction_id "  + 
										" where "  
										+ "  pres.organization_id =:organizationId"
										+ "  and pres.unit_id =:unitId"
										+ " and presDtl.cov_template_id =:covTemplateId"
										+ " and presDtl.is_active = true"*/
										)
							.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
							.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
							.setParameter("covTemplateId", mainDto.getCovTemplateId())
						.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
					
					mainDto.setListPrescriptionData(listMedication);
				}
				coversheetTemplateMasterDto=mainDto;
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, coversheetTemplateMasterDto);
			
			
	} catch (HibernateException he) {
		throw he ;
	} catch (Exception e) {
		throw e ;
	}
//	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response saveCovTemplateDataForPatient(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			String query = "select " + 
					"	cov.cov_template_id as \"covTemplateId\" ," + 
					"	cov.type_id as \"typeId\", " + 
					"	cov.template_name as \"templateName\" " + 
					"from " + 
					"	opd.t_coversheet_template_master cov "
					+ " where "
					+ " cov.organization_id=:organizationId "
					+ " and cov.cov_template_id=:covTemplateId "
					+ " and cov.unit_id=:unitId";
			
			StringBuffer subQuery=new StringBuffer();
			query = query.concat(subQuery.toString());	
			List<CoversheetTemplateMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
				.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(CoversheetTemplateMasterDto.class)).list();
			
			for (CoversheetTemplateMasterDto mainDto : list) {
				
				List<ComplaintAppointmentMapperDto> listComplaints=sessionFactory.getCurrentSession()
						.createSQLQuery("select " + "	complaintAppointment.complaint_appo_id as \"complaintAppoId\", "
								+ "	icpc1_.icpc_desc as \"diagnosisName\", " + "	icpc1_.icpc_code as \"icpcCode\", "
								+ "	complaintAppointment.icpc_code_text as \"icpcCodeText\", "
								+ "	icpcsystem2_.icpc_system_desc as \"icpcSystemDesc\" " + "from "
								+ "	patient.t_complaint_appo_mapper complaintAppointment "
								+ "inner join emr.m_icpc icpc1_ on "
								+ "	complaintAppointment.diagnosis_id = icpc1_.icpc_id "
								+ "left join emr.m_icpc_system icpcsystem2_ on "
								+ "	complaintAppointment.icpc_system_id = icpcsystem2_.icpc_system_id " 
								+ " where "
								+ "	 complaintAppointment.status = 'A' "
								+ "	and complaintAppointment.is_resolved = 'N' "
								+ "	and complaintAppointment.organization_id =:organizationId "
								+ "	and complaintAppointment.unit_id =:unitId "
								+ "	and complaintAppointment.cov_template_id =:covTemplateId "
								+ "	and(complaintAppointment.is_complaint_stop = 'N')")
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setParameter("covTemplateId", mainDto.getCovTemplateId())
					.setResultTransformer(Transformers.aliasToBean(ComplaintAppointmentMapperDto.class)).list();
				    mainDto.setListComplaintData(listComplaints);
				    
				    if(mainDto.getTypeId() == 1) {
				    	List<DiagnosisPatientAppointmentMapperDto> listDiagnosis=sessionFactory.getCurrentSession()
								.createQuery("SELECT " + " diagMapper.patientDiagnosisId AS patientDiagnosisId, "
										+ " diagMapper.admissionId AS admissionId, " + " diagMapper.isDeleted AS isDeleted, "
										+ "(select max(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId "
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as templateTransactionId, "
										+ "(select count(tempObj.templateTransactionId) from TemplateTransaction tempObj where tempObj.diagnosisId=diagMapper.diagnosisId and tempObj.status = 'A' "
										+ " and diagMapper.patientId=tempObj.patientId group by tempObj.diagnosisId ) as diagnosisTempCount, "
										+ " diagMapper.patientId AS patientId, "
										+ " diagMapper.diagnosisText AS diagnosisText, "
										+ " diagMapper.diagnosisId AS diagnosisId, " + " diagMapper.type AS type, "
										+ " diagMapper.diagnosisType AS diagnosisType, " + " (case "
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
										+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
										+ " else 'Admin' end) as userName,"
										+ " to_char(diagMapper.created_date,'DD/MM/YYYY') as created_date, "
										+ " diagMapper.created_date as created_date_new, "
										+ " to_char(diagMapper.created_date,'HH24:MI') as createdTime, "
										+ " diagMapper.remark AS remark, " + " diagMaster.isNotifiable AS isNotifiable , "
										+ " diagMaster.isComorbidity AS isComorbidity," + " diagMaster.code AS code,"
										+ " diagMaster.diagnosisName AS diagnosisName "
										+ " FROM DiagnosisPatientAppointmentMapper diagMapper "
										+ " left JOIN diagMapper.diagnosisMaster diagMaster "
										+ " LEFT JOIN diagMapper.userMaster um " + " LEFT JOIN um.employeeMastersList empmst "
										+ " LEFT JOIN um.doctorMastersList  docmst " 
										+ " WHERE " + "  diagMapper.status='A'"
										+ " AND diagMapper.organizationId=:organizationId"
										+ " AND diagMapper.covTemplateId=:covTemplateId"
										+ " AND diagMapper.unitId=:unitId"
										+ " AND diagMapper.isDiagnosisCancel='N'" 
										+ " AND diagMapper.status='A'"
										+ " AND diagMapper.isDeleted='N'"
										+ " AND diagMapper.isResolved='N'" 
										+ " order by diagMapper.patientDiagnosisId desc")
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
				    	
				    	mainDto.setListDiagnosisData(listDiagnosis);
				    }
				
				    if(mainDto.getTypeId() == 1 || mainDto.getTypeId() == 2) {
				    	List<InvestigationOrderMasterNewDto> listlabOrder=sessionFactory.getCurrentSession()
								.createSQLQuery("select  " + 
										"	ord_mst.order_id as \"orderId\",  " + 
										"	service_mst.speciality_id as \"specialityId\",  " + 
										"	service_mst.sub_speciality_id as \"subDeptId\",  " + 
										"	service_mst.sub_speciality_id as \"subSpecialityId\",  " + 
										"	ord_dtls.order_details_id as \"orderDetailsId\",  " + 
										"	ord_dtls.service_id as \"serviceMasterId\",  " + 
										"	ord_dtls.order_set_id as \"orderSetId\",  " + 
										"	ord_dtls.priority_id as \"priorityId\",  " + 
										"	ord_dtls.ord_remarks as \"ordRemarks\",  " + 
										"	to_char(ord_dtls.order_sch_date, 'dd/MM/yyyy') as \"orderSchDate\",  " + 
										"	ord_dtls.order_sch_date as \"orderSchDateNew\",  " + 
										"	service_mst.service_standard_name as \"serviceStandardName\",  " + 
										"	ord_dtls.created_date as \"createdDateNew\",  " + 
										"	ord_dtls.service_rendered as \"serviceRendered\",  " + 
										"	ord_dtls.is_package_service as \"isPackageService\",  " + 
										"	ord_dtls.service_is_billed as \"serviceIsBilled\",  " + 
										"	ord_dtls.status as \"orderStatus\",  " + 
										"	ord_dtls.package_id as \"packageId\"  " + 
										"from  " + 
										"	public.t_order_master ord_mst  " + 
										"inner join public.t_order_details ord_dtls on  " + 
										"	ord_mst.order_id = ord_dtls.order_id  " + 
										"inner join service.t_unit_service_mapper service_mppr on  " + 
										"	service_mppr.service_id = ord_dtls.service_id  " + 
										"	and service_mppr.unit_id = ord_dtls.org_unit_id  " + 
										"inner join service.t_unit_service_master_details service_dtls on  " + 
										"	service_dtls.service_master_id = service_mppr.service_id  " + 
										"	and service_dtls.unit_id = ord_dtls.org_unit_id  " + 
										"inner join service.m_service_master service_mst on  " + 
										"	service_mst.service_master_id = ord_dtls.service_id  " + 
										" left join doctor.m_speciality_master speciality_mst on  " + 
										"	speciality_mst.speciality_id = service_mst.speciality_id  " + 
										" " + 
										" where  " + 
										"	service_mst.status = 'A'  " + 
										"	and ord_dtls.old_ord_dtl_id is null  " + 
										"	and service_dtls.is_package = 'I'  " + 
										"	and ord_dtls.nursing_service_order_flag = 'N'  " + 
										"	and service_dtls.is_panel = 'N'  " + 
										"	and speciality_mst.speciality_id in(  " + 
										"	select  " + 
										"		speciality_id  " + 
										"	from  " + 
										"		t_unit_speciality_mapper " + 
										"	where " + 
										"		ord_mst.org_id =:organizationId " + 
										"		and ord_dtls.cov_template_id =:covTemplateId " + 
										"		and ord_dtls.status ='A' " + 
										"		and ord_mst.org_unit_id =:unitId )"
										
										)
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
				    	
				    	List<InvestigationOrderMasterNewDto> listlabOrderFreeText=sessionFactory.getCurrentSession()
								.createSQLQuery("select " + 
										"	tftos.patient_id as \"patientId\", " + 
										"	tftos.admission_id as \"admissionId\", " + 
										"	tftos.encounter_id as \"encounterId\", " + 
										"	tftos.order_details_id as \"orderDetailsId\", " + 
										"	CURRENT_TIMESTAMP as \"orderSchDateNew\", " + 
										"	tftos.service_text as \"serviceStandardName\", " + 
										"	CURRENT_TIMESTAMP as \"createdDateNew\" " + 
										" from " + 
										"	public.t_free_text_ordered_services tftos " + 
										" left join public.t_order_details ord_dtls on " + 
										"	tftos.order_details_id = ord_dtls.order_details_id " + 
										"	where " + 
										"		tftos.organization_id =:organizationId " + 
										"		and tftos.cov_template_id =:covTemplateId " + 
										"		and tftos.status ='A' " + 
										"		and tftos.unit_id =:unitId "
										
										)
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
				    		listlabOrder.addAll(listlabOrderFreeText);
				    		mainDto.setListLabOrderData(listlabOrder);
				    }
				
				
				    if(mainDto.getTypeId() == 1 || mainDto.getTypeId() == 4) {
				    	List<CoversheetInstructionDto> listAdvice=sessionFactory.getCurrentSession()
								.createQuery("SELECT cover.instructionId as instructionId, "
										+ " cover.instructionDetailsId as instructionDetailsId, "
										+ " instr.instructionDesc as instructionDesc, cover.activityOrIntrcs as activityOrIntrcs, cover.isOtims as isOtims,"
										+ " cover.notes as notes, cover.advicedInstruction as advicedInstruction,"
										+ " (case "
										+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
										+ " when doc.doctorId is not null then concat('Dr. ', doc.firstName, ' ', doc.lastName) "
										+ " else 'Admin' end) as userName,"
										+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
										+ " FROM CoversheetInstruction cover "
										+ " LEFT JOIN cover.instructionMaster instr "
										+ " LEFT JOIN cover.userMaster userMaster "
										+ " LEFT JOIN userMaster.doctorMastersList doc "
										+ " LEFT JOIN userMaster.employeeMastersList empmst " + 
										" where " + 
										"	 cover.organizationId =:organizationId " +
										"	 and cover.unitId =:unitId " +
										"	 and cover.status ='A' " +
										"	 and cover.covTemplateId =:covTemplateId "
										+ " and (cover.instructionDetailsId is not null or cover.advicedInstruction is not null )" 

										)
								.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
								.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
								.setParameter("covTemplateId", mainDto.getCovTemplateId())
							.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
				    	mainDto.setListAdviceData(listAdvice);
				    }
				
				
				if(mainDto.getTypeId() == 1 || mainDto.getTypeId() == 3) {
					List<MedicationDTO> listMedication=sessionFactory.getCurrentSession()
							.createSQLQuery(
									 "select   " + 
										"	pres.id as \"prescriptionId\",   " + 
										"	pres.medication_type as \"medicationType\",   " + 
										"	pres.organization_id as \"organizationId\",   " + 
										"	pres.unit_id as \"unitId\",   " + 
										"	pres.status_id as \"statusIdPres\",   " + 
										"	pres.visit_type_id as \"visitTypeId\",   " + 
										"	presDtl.is_medication_stop as \"isMedicationStop\",   " + 
										"	presDtl.priority_id as \"priorityId\",   " + 
										"	pres.added_by as \"addedBy\",   " + 
										"	presDtl.duration as \"duration\",   " + 
										"	presDtl.duration as \"durationCount\",   " + 
										"	presDtl.duration_unit as \"durationUnit\",   " + 
										"	presDtl.justification_stop as \"justificationStop\",   " + 
										"	presDtl.instruction_desc as \"instructionText\",   " + 
										"	presDtl.item_id as \"itemId\",   " + 
										"	preFreDtl.dose as \"doseDesc\",   " + 
										"	case   " + 
										"		when (itm.name is null) then presDtl.item_name   " + 
										"		else itm.name   " + 
										"	end as \"itemName\",   " + 
										"	case   " + 
										"		when (gen.name is null) then presDtl.generic_name   " + 
										"		else gen.name   " + 
										"	end as \"genericName\",   " + 
										"	su.unit as \"strengthDesc\",   " + 
										"	su.id as \"doseUnitId\",   " + 
										"	preFreDtl.frequency_id as \"frequencyId\",   " + 
										"	freq.description as \"frequencyDesc\",   " + 
										"	preFreDtl.route_id as \"roaId\",   " + 
										"	presDtl.is_non_formulary as \"isNonFormulary\",   " + 
										"	presDtl.status_id as \"statusId\",   " + 
										"	presDtl.statflag as \"statflag\",   " + 
										"	roa.description as \"roaDesc\",   " + 
										"	presDtl.order_qty as \"orderQty\",   " + 
										"	itm.generic_id as \"genericId\",   " + 
										"	itm.formulation_type_id as \"formulationTypeId\",   " + 
										"	to_char(preFreDtl.start_date, 'dd/mm/yyyy') as \"startDate\",   " + 
										"	to_char(preFreDtl.stop_date, 'dd/mm/yyyy') as \"endDate\",   " + 
										"	preFreDtl.start_date as \"startDateNew\",   " + 
										"	preFreDtl.stop_date as \"endDateNew\",   " + 
										"	to_char(presDtl.added_date, 'dd/mm/yyyy') as \"addedDate\",   " + 
										"	presDtl.added_date as \"addedDateNew\",   " + 
										"	to_char(presDtl.added_date, 'HH24:MI') as \"addedTime\",   " + 
										"	presDtl.instruction_id as \"instructionId\" ,   " + 
										"	instr.instruction_desc as \"instructionDesc\",   " + 
										"	presDtl.id as \"prescriptionDetailsid\",   " + 
										"	preFreDtl.id as \"prescriptionFrequencyId\" ,   " + 
										"	pres. is_otims as \"isOtims\",   " + 
										"	presDtl.is_discharge_medication as \"isDischargeMedication\",   " + 
										"	presDose.id as \"prescribeDoseUnitId\",   " + 
										"	presDose.unit as \"prescribeDoseUnit\",   " + 
										"	presDtl.prescribe_dose as \"prescribeDose\",   " + 
										"	'' as \"dosageForm\",   " + 
										"	presDtl.factor_id as \"factorId\",   " + 
										"	presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\"   " + 
										" from   " + 
										"	pharmacy.t_prescription pres   " + 
										" left join pharmacy.t_prescription_detail presDtl on   " + 
										"	presDtl.prescription_id = pres.id   " + 
										"inner join pharmacy.t_prescription_frequency_detail preFreDtl on   " + 
										"	preFreDtl.prescription_detail_id = presDtl.id   " + 
										"left join public.m_strength_unit su on   " + 
										"	preFreDtl.dose_unit_id = su.id   " + 
										"left join public.m_strength_unit presDose on   " + 
										"	presDtl.prescribe_dose_unit_id = presDose.id   " + 
										"left join public.m_item itm on   " + 
										"	itm.id = presDtl.item_id   " + 
										"left join public.m_route_of_administration roa on   " + 
										"	roa.id = preFreDtl.route_id   " + 
										"left join public.m_generic gen on   " + 
										"	itm.generic_id = gen.id   " + 
										"inner join public.m_frequency freq on   " + 
										"	freq.id = preFreDtl.frequency_id   " + 
										"left join public.m_instruction instr on   " + 
										"	instr.instruction_id = presDtl.instruction_id "  + 
										" where "  
										+ "  pres.organization_id =:organizationId"
										+ "  and pres.unit_id =:unitId"
										+ " and presDtl.cov_template_id =:covTemplateId"
										+ " and presDtl.is_active = true"
										)
							.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
							.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
							.setParameter("covTemplateId", mainDto.getCovTemplateId())
						.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
					
					mainDto.setListPrescriptionData(listMedication);
				}
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			
	} catch (HibernateException he) {
	logger.error("saveCovTemplateDataForPatient failed", he);
	} catch (Exception e) {
	logger.error("saveCovTemplateDataForPatient failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getCoversheetListData(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			String query = "select  " + 
					"	pres.id as \"id\"  " + 
					"from  " + 
					"	pharmacy.t_prescription pres  " + 
					"left join pharmacy.t_prescription_detail presDtl on  " + 
					"	presDtl.prescription_id = pres.id  " + 
					"where  " + 
					"	pres.organization_id = :organizationId  " + 
					"	and pres.unit_id = :unitId  " + 
					"	and pres.patient_id = :patientId  " + 
					"	and pres.encounter_id = :encounterId  " + 
					"	and presDtl.is_active = true  " + 
					"union  " + 
					"select  " + 
					"	ord_mst.order_id as \"id\"  " + 
					"from  " + 
					"	public.t_order_master ord_mst  " + 
					"inner join public.t_order_details ord_dtls on  " + 
					"	ord_mst.order_id = ord_dtls.order_id  " + 
					"inner join service.t_unit_service_mapper service_mppr on  " + 
					"	service_mppr.service_id = ord_dtls.service_id  " + 
					"	and service_mppr.unit_id = ord_dtls.org_unit_id  " + 
					"inner join service.t_unit_service_master_details service_dtls on  " + 
					"	service_dtls.service_master_id = service_mppr.service_id  " + 
					"	and service_dtls.unit_id = ord_dtls.org_unit_id  " + 
					"inner join service.m_service_master service_mst on  " + 
					"	service_mst.service_master_id = ord_dtls.service_id  " + 
					"left join doctor.m_speciality_master speciality_mst on  " + 
					"	speciality_mst.speciality_id = service_mst.speciality_id  " + 
					"where  " + 
					" speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id =:unitId and is_rendering_speciality = 'Y')" + 
					"	and service_dtls.is_package = 'I'  " + 
					"	and ord_dtls.nursing_service_order_flag = 'N'  " + 
					"	and service_dtls.is_panel = 'N'  " + 
					"	and service_mst.status = 'A'  " + 
					"	and ord_dtls.old_ord_dtl_id is null  " + 
					"	and ord_dtls.status = 'A'  " + 
					"	and ord_mst.patient_id = :patientId  " + 
					"	and ord_mst.encounter_id = :encounterId  " + 
					"union  " + 
					"select  " + 
					"	coversheet.instruction_id as \"id\"  " + 
					"from  " + 
					"	opd.t_instruction coversheet " + 
					"left outer join public.m_instruction instructiontt on  " + 
					"	coversheet.instruction_details_id = instructiontt.instruction_id  " + 
					"where  " + 
					"	coversheet.organization_id = :organizationId  " + 
					"	and coversheet.unit_id = :unitId  " + 
					"	and coversheet.status = 'A'  " + 
					"	and coversheet.patient_id = :patientId  " + 
					"	and coversheet.encounter_id = :encounterId  " + 
					"	and (coversheet.instruction_details_id is not null  " + 
					"	or coversheet.adviced_instruction is not null)  " + 
					"union  " + 
					"select  " + 
					"	diagnosisp.patient_diagnosis_id as \"id\"  " + 
					"from  " + 
					"	patient.t_patient_diagnosis_appo_mapper diagnosisp  " + 
					" where  " + 
					"	diagnosisp.patient_id = :patientId  " + 
					"	and diagnosisp.encounter_id = :encounterId  " + 
					"	and diagnosisp.status = 'A'  " + 
					"	and diagnosisp.organization_id = :organizationId  " + 
					"	and diagnosisp.is_diagnosis_cancel = 'N'  " + 
					"	and diagnosisp.is_deleted = 'N'  " + 
					"	and diagnosisp.is_resolved = 'N'  " 
					;
			StringBuffer subQuery=new StringBuffer();
			query = query.concat(subQuery.toString());	
			List<CoversheetTemplateMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", coversheetTemplateMasterDto.getOrganizationId())
					.setParameter("encounterId", coversheetTemplateMasterDto.getEncounterId())
					.setParameter("patientId", coversheetTemplateMasterDto.getPatientId())
					.setParameter("unitId", coversheetTemplateMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CoversheetTemplateMasterDto.class)).list();
			
			PatientRegistrationDto dto=new PatientRegistrationDto();
			if(list != null && list.size() > 0){
				
				dto.setIsDataAvailable(true);
			}else {
				dto.setIsDataAvailable(false);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, dto);
			
			
	} catch (HibernateException he) {
	logger.error("getCoversheetListData failed", he);
	} catch (Exception e) {
	logger.error("getCoversheetListData failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
}
