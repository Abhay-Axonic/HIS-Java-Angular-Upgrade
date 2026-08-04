package com.param.billing.patientType.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.PatientTypeServiceWiseDetailsMaster;
import com.param.billing.patientType.dto.PatientTypeServiceWiseDetailsDto;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@SuppressWarnings({"rawtypes","unchecked","unused"})
@Repository
public class PatientTypeServiceWiseDetailsDaoImpl extends GenericDao<PatientTypeServiceWiseDetailsMaster, Integer>
		implements IPatientTypeServiceWiseDetailsDao,ICommonConstants{

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	public PatientTypeServiceWiseDetailsDaoImpl() {
		super(PatientTypeServiceWiseDetailsMaster.class);
	}

	@Autowired
	private SessionFactory SessionFactory;
	
	
	@Override
	public Response savePatientTypeServiceWiseDetails(List<PatientTypeServiceWiseDetailsDto> patientTypeServiceWiseDetailsDto) {
		try {

			for (PatientTypeServiceWiseDetailsDto patientServiceMasterList : patientTypeServiceWiseDetailsDto) {
				PatientTypeServiceWiseDetailsMaster patientServiceMaster = new PatientTypeServiceWiseDetailsMaster();

				patientServiceMaster.setSpecialityId(patientServiceMasterList.getSpecialityId());
				patientServiceMaster.setSubSpecialityId(patientServiceMasterList.getSubSpecialityId());
				patientServiceMaster.setServiceId(patientServiceMasterList.getServiceId());
				patientServiceMaster.setCreatedBy(patientServiceMasterList.getCreatedBy());
				patientServiceMaster.setMarkupStatus(patientServiceMasterList.getMarkupStatus());
				patientServiceMaster.setStatus("A");
				patientServiceMaster.setPatientTypeId(patientServiceMasterList.getPatientTypeId());
				patientServiceMaster.setOrganizationId(patientServiceMasterList.getOrganizationId());
				patientServiceMaster.setPercentage(patientServiceMasterList.getPercentage());
				patientServiceMaster.setUnitId(patientServiceMasterList.getUnitId());
				patientServiceMaster.setUpdatedBy(patientServiceMasterList.getUpdatedBy());
				patientServiceMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientServiceMasterList.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				patientServiceMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientServiceMasterList.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				save(patientServiceMaster);

				
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getPatientTypeServiceWiseDetails(int patientTypeId,int organizationId,int unitId)
	{
		try {
			List<PatientTypeServiceWiseDetailsDto> patientTypeServiceList =sessionFactory.getCurrentSession()
					.createSQLQuery("select " + 
							"	pts.patient_type_service_id as \"patientTypeServiceId\", " + 
							"	pts.speciality_id as \"specialityId\", " + 
							"	pts.service_id as \"serviceId\", " + 
							"	pts.sub_speciality_id as \"subSpecialityId\", " + 
							"	pts.percentage as \"percentage\", " + 
							"	pts.code as \"code\", " + 
							"	pts.description as \"description\", " + 
							"	pts.markup_status as \"markupStatus\", " + 
							"	pts.status as \"status\", " + 
							"	pts.unit_id as \"unitId\", " + 
							"	pts.organization_id  as \"organizationId\", " + 
							"	to_char(pts.created_date,'dd/mm/yyyy') as \"createdDate\", " + 
							"	pts.created_by as \"createdBy\", " + 
							"	to_char(pts.updated_date,'dd/mm/yyyy') as \"updatedDate\", " + 
							"	pts.updated_by as \"updatedBy\", " +
							"  sp.speciality_name as \"specialityName\", " + 
							"  ssm.sub_speciality_name as \"subSpecialityName\", " + 
							"  sm.service_standard_name as \"serviceName\" "+
							"from " + 
							"billing.m_patient_type_service_wise_details pts  " + 
							"inner join doctor.m_speciality_master sp on pts.speciality_id=sp.speciality_id " + 
							"inner join doctor.m_sub_speciality_master ssm on pts.sub_speciality_id=ssm.sub_speciality_id " + 
							"inner join service.m_service_master sm on pts.service_id=sm.service_master_id " + 
							"where pts.patient_type_id =:patientTypeId "
							+" AND pts.organization_id =:organizationId AND pts.unit_id =:unitId ")
					.setParameter("patientTypeId", patientTypeId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PatientTypeServiceWiseDetailsDto.class)).list();
			

				return new Response(SUCCESS, SUCCESS_CODE, "success", patientTypeServiceList, null);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updatePatientTypeServiceStatus(PatientTypeServiceWiseDetailsDto patientTypeServiceWiseDetailsDto) {
		try {
			// BATCH-4 org-trust fix (CRUD-sibling of the batch-2 patientType toggle guard): this
			// toggle was a bare write-by-ID (findById + update, zero org check) — same class as
			// the live-proven unitServiceMaster cross-tenant write. Reject rows not owned by the
			// verified tenant.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			PatientTypeServiceWiseDetailsMaster master = findById(patientTypeServiceWiseDetailsDto.getPatientTypeServiceId());
			if (master == null || master.getOrganizationId() == null
					|| !master.getOrganizationId().equals(verifiedOrgId)
					|| master.getUnitId() == null
					|| !master.getUnitId().equals(verifiedUnitId)) {
				return new Response<>(ERROR, ERROR_CODE, "Cross-tenant update rejected", null, null);
			}
			master.setUpdatedBy(patientTypeServiceWiseDetailsDto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(patientTypeServiceWiseDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			master.setStatus(patientTypeServiceWiseDetailsDto.getStatus().toString());
			update(master);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	
}


