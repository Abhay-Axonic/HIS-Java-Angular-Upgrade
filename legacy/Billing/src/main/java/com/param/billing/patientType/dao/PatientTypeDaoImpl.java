package com.param.billing.patientType.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.PatientTypeMaster;
import com.param.billing.patientType.dto.PatientTypeDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@SuppressWarnings({"rawtypes","unchecked","unused"})
@Repository
public class PatientTypeDaoImpl extends GenericDao<PatientTypeMaster,Integer> implements IPatientTypeDao,ICommonConstants{

	public PatientTypeDaoImpl() {
		super(PatientTypeMaster.class);
	}
	
	@Autowired
	private SessionFactory SessionFactory;

	@Override
	public Response savePatientType(PatientTypeDto patientTypeDto) 
	{
		try {
			PatientTypeMaster patientTypeMaster = new PatientTypeMaster();
			
			patientTypeMaster.setCode(patientTypeDto.getCode());
			patientTypeMaster.setDescription(patientTypeDto.getDescription());
			patientTypeMaster.setCreatedBy(patientTypeDto.getCreatedBy());
			patientTypeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientTypeDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			patientTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientTypeDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			patientTypeMaster.setUpdatedBy(patientTypeDto.getUpdatedBy());
			patientTypeMaster.setUnitId(patientTypeDto.getUnitId());
			patientTypeMaster.setOrganizationId(patientTypeDto.getOrganizationId());
			patientTypeMaster.setStatus("A");
			save(patientTypeMaster);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, patientTypeMaster);
		}
		catch(Exception e)
		{
			
		}
		return new Response<>(ERROR,ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
	}

	@Override
	public Response getPatientType(int organizationId,int unitId) {
		try {
			List<PatientTypeDto> patientTypeList =sessionFactory.getCurrentSession()
					.createSQLQuery("select " + 
							"	patient_type_id as \"patientTypeId\", " + 
							"	code as \"code\", " + 
							"	status as \"status\", " + 
							"	description as \"description\", " +
							"	organization_id as \"organizationId\", " +
							"	unit_id as \"unitId\" " +
							"from billing.m_patient_type "
							+" where organization_id =:organizationId AND unit_id =:unitId and status = 'A'"
							+" ORDER BY description ASC")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PatientTypeDto.class)).list();
			

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, patientTypeList, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePatientTypeStatus(PatientTypeDto patientTypeDto) throws Exception {
		try {
			PatientTypeMaster patientTypeMaster = findById(patientTypeDto.getPatientTypeId());
			patientTypeMaster.setUpdatedBy(patientTypeDto.getUpdatedBy());
			patientTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(patientTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			patientTypeMaster.setStatus(patientTypeDto.getStatus().toString());
			update(patientTypeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, patientTypeMaster.getUnitId());
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePatientType(PatientTypeDto patientTypeDto) throws Exception {
		try {
			PatientTypeMaster patientTypeMaster = findById(patientTypeDto.getPatientTypeId());
			patientTypeMaster.setCode(patientTypeDto.getCode());
			patientTypeMaster.setDescription(patientTypeDto.getDescription());
			patientTypeMaster.setCreatedBy(patientTypeDto.getCreatedBy());
			patientTypeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientTypeDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			patientTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(patientTypeDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			patientTypeMaster.setUpdatedBy(patientTypeDto.getUpdatedBy());
			patientTypeMaster.setUnitId(patientTypeDto.getUnitId());
			patientTypeMaster.setOrganizationId(patientTypeDto.getOrganizationId());
			patientTypeMaster.setStatus("A");
			update(patientTypeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkPatientType(PatientTypeDto patientTypeDto) throws Exception {
		try {
			List<PatientTypeDto> patientTypeList =sessionFactory.getCurrentSession()
					.createSQLQuery("select patient_type_id as \"patientTypeId\" from billing.m_patient_type "
							+ "where lower(description) =:description "
							+ " and organization_id =:organizationId AND unit_id =:unitId and status = 'A'")
					.setParameter("organizationId", patientTypeDto.getOrganizationId())
					.setParameter("unitId", patientTypeDto.getUnitId())
					.setParameter("description", patientTypeDto.getDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(PatientTypeDto.class)).list();
			

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, patientTypeList, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCheckPatientType(PatientTypeDto patientTypeDto) throws Exception {
		try {
			List<PatientTypeDto> patientTypeList =sessionFactory.getCurrentSession()
					.createSQLQuery("select patient_type_id as \"patientTypeId\" from billing.m_patient_type "
							+ "where lower(description) =:description "
							+ " and organization_id =:organizationId AND unit_id =:unitId and status = 'A'"
									+ " and patient_type_id <> :patientTypeId")
					.setParameter("organizationId", patientTypeDto.getOrganizationId())
					.setParameter("unitId", patientTypeDto.getUnitId())
					.setParameter("patientTypeId", patientTypeDto.getPatientTypeId())
					.setParameter("description", patientTypeDto.getDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(PatientTypeDto.class)).list();
			

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, patientTypeList, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	
}
