package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.PatientTabularTemplateDataMapper;
import com.param.global.model.TabularTemplateDataMapper;
import com.param.global.unit.dto.PatientTabularTemplateDataMapperDto;
import com.param.global.unit.dto.TabularTemplateDataMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class PatientTabularTemplateDataMapperDaoImpl extends GenericDao<PatientTabularTemplateDataMapper, Integer> implements IPatientTabularTemplateDataMapperdDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(PatientTabularTemplateDataMapperDaoImpl.class);


	public PatientTabularTemplateDataMapperDaoImpl() {
		super(PatientTabularTemplateDataMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response savePatientTabularTemplateDataMapper(
			PatientTabularTemplateDataMapperDto patientTabularTemplateDataMapperDto) throws ApplicationException {
		try {
			 sessionFactory.getCurrentSession().createSQLQuery(
						"update public.m_patient_tabular_template_data_mapper set status = 'I' where tabular_template_id = :tabularTemplateId  and patient_id =:patientId and organization_id =:organizationId and unit_id =:unitId and template_type_id =:templateTypeId ")
						.setParameter("tabularTemplateId", patientTabularTemplateDataMapperDto.getTabularTemplateId())
						.setParameter("patientId", patientTabularTemplateDataMapperDto.getPatientId())
						.setParameter("organizationId", patientTabularTemplateDataMapperDto.getOrganizationId())
						.setParameter("unitId", patientTabularTemplateDataMapperDto.getUnitId())
						.setParameter("templateTypeId", patientTabularTemplateDataMapperDto.getTemplateTypeId())
						.executeUpdate();	
			 
			for(PatientTabularTemplateDataMapperDto dto : patientTabularTemplateDataMapperDto.getToSaveListPatient())
			{
				PatientTabularTemplateDataMapper patientTabularTemplateDataMapper=new PatientTabularTemplateDataMapper();
				patientTabularTemplateDataMapper.setTemplateId(patientTabularTemplateDataMapperDto.getTemplateId());
				patientTabularTemplateDataMapper.setPatientId(patientTabularTemplateDataMapperDto.getPatientId());
				patientTabularTemplateDataMapper.setDoctorId(patientTabularTemplateDataMapperDto.getDoctorId());
				patientTabularTemplateDataMapper.setEncounterId(patientTabularTemplateDataMapperDto.getEncounterId());
				patientTabularTemplateDataMapper.setAdmissionId(patientTabularTemplateDataMapperDto.getAdmissionId());
				patientTabularTemplateDataMapper.setTabularTemplateId(patientTabularTemplateDataMapperDto.getTabularTemplateId());
				patientTabularTemplateDataMapper.setTableName(dto.getTableName());
				patientTabularTemplateDataMapper.setTemplateTypeId(patientTabularTemplateDataMapperDto.getTemplateTypeId());
				patientTabularTemplateDataMapper.setTitle(patientTabularTemplateDataMapperDto.getTitle());
				patientTabularTemplateDataMapper.setColumnName(dto.getColumnName());
				patientTabularTemplateDataMapper.setCoulmnId(dto.getCoulmnId());
				patientTabularTemplateDataMapper.setRowName(dto.getRowName());
				patientTabularTemplateDataMapper.setRowId(dto.getRowId());
				patientTabularTemplateDataMapper.setInputValue(dto.getInputValue());
				patientTabularTemplateDataMapper.setNumberOfCoulmn(dto.getNumberOfCoulmn());
				patientTabularTemplateDataMapper.setNumberOfRow(dto.getNumberOfRow());
				patientTabularTemplateDataMapper.setOrganizationId(patientTabularTemplateDataMapperDto.getOrganizationId());
				patientTabularTemplateDataMapper.setUnitId(patientTabularTemplateDataMapperDto.getUnitId());
				patientTabularTemplateDataMapper.setStatus('A');
				patientTabularTemplateDataMapper.setCreatedBy(patientTabularTemplateDataMapperDto.getCreatedBy());
				patientTabularTemplateDataMapper.setCreatedDate(GlobalCommonDateUtils.getDate(patientTabularTemplateDataMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				patientTabularTemplateDataMapper.setUpdatedBy(patientTabularTemplateDataMapperDto.getUpdatedBy());
				patientTabularTemplateDataMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(patientTabularTemplateDataMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(patientTabularTemplateDataMapper);
			}
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("savePatientTabularTemplateDataMapper failed", he);
		} catch (Exception e) {
		logger.error("savePatientTabularTemplateDataMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId,
			Integer unitId , Integer patientId ,Integer templateTypeId) throws ApplicationException {
		try {
			List<PatientTabularTemplateDataMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TABULAR_TEMPLATE_DATA_PATIENT_BY_ID")
					.setParameter("tabularTemplateId", tabularTemplateId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("patientId", patientId)
					.setParameter("templateTypeId", templateTypeId)
					.setResultTransformer(Transformers.aliasToBean(PatientTabularTemplateDataMapperDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getPatientTabularTemplateDataMapperById failed", he);
		} catch (Exception e) {
		logger.error("getPatientTabularTemplateDataMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



}
