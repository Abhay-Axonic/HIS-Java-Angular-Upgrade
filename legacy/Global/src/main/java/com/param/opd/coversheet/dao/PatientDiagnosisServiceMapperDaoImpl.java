package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientDiagnosisServiceMapperDto;
import com.param.opd.coversheet.model.PatientDiagnosisServiceMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PatientDiagnosisServiceMapperDaoImpl  extends GenericDao<PatientDiagnosisServiceMapper, Integer> implements IPatientDiagnosisServiceMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientDiagnosisServiceMapperDaoImpl.class);


	public PatientDiagnosisServiceMapperDaoImpl() {
		super(PatientDiagnosisServiceMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response savePatientDaignosisServiceMapper(List<PatientDiagnosisServiceMapperDto> list)
			throws ApplicationException {
		try {
			 for (PatientDiagnosisServiceMapperDto dto : list) {
				
				 PatientDiagnosisServiceMapper master = new PatientDiagnosisServiceMapper();
				 
				 master.setAdmissionId(dto.getAdmissionId() != null ?dto.getAdmissionId() : null );
				 master.setCreatedBy(dto.getCreatedBy());
				 master.setDiagnosisId(dto.getDiagnosisId());
				 master.setDiagnosisType(dto.getDiagnosisType());
				 master.setEncounterId(dto.getEncounterId() != null ?dto.getEncounterId() : null);
				 master.setOrganizationId(dto.getOrganizationId());
				 master.setPatientId(dto.getPatientId());
				 master.setRemark(dto.getRemark());
				 master.setStatus(dto.getStatus());
				 master.setType(dto.getType());
				 master.setUnitId(dto.getUnitId());
				 master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				 
				 if(dto.getPatientDiagnosisServiceMapperId() != null ) {
					 master.setUpdatedBy(dto.getUpdatedBy());
					 master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					 master.setStatus('I');
					 master.setPatientDiagnosisServiceMapperId(dto.getPatientDiagnosisServiceMapperId());
					 update(master);
					 return new Response(SUCCESS, SUCCESS_CODE, REMOVE, null, null);
				 }else {
					 master.setUpdatedBy(dto.getUpdatedBy());
					 master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					 master.setStatus('A');
					 save(master);
					 return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				 }
				 
				 
			}
			
			
		} catch (Exception e) {
		logger.error("savePatientDaignosisServiceMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDaignosisServiceMapper(PatientDiagnosisServiceMapperDto dto) throws ApplicationException {

		try {
			List<PatientDiagnosisServiceMapperDto> list =  sessionFactory.getCurrentSession()
					.createSQLQuery("select   to_char(tpdam.created_date, 'DD-MM-YYYY' ) as \"createdDate\", " + 
							" to_char(tpdam.created_date, 'HH24:MI:SS' ) as  \"time\" , " +
							" tpdam.type as \"type\" , " +
							" tpdam.remark as  \"remark\" , " +
							" tpdam.diagnosis_type as \"diagnosisType\", " +
							" mdm.code as \"code\", " +
							" mdm.diagnosis_desc as \"diagnosisDesc\" , " +
							" 0 as  \"fromData\", " +
							" null  as \"patientDiagnosisServiceMapperId\", " + 
						    "  tpdam.diagnosis_id as \"diagnosisId\" " + 
							" from   patient.t_patient_diagnosis_appo_mapper tpdam  " +
							" inner join  emr.m_diagnosis_master mdm on mdm.diagnosis_id  = tpdam.diagnosis_id  " + 
							" where  tpdam.type  = 1 and tpdam.status ='A' and  tpdam.patient_id  = "+dto.getPatientId()+" and tpdam.unit_id  =" + dto.getUnitId()+
							" union " +
							" select   to_char(tpdam.created_date, 'DD-MM-YYYY' ) as \"createdDate\" , " +
							" to_char(tpdam.created_date, 'HH24:MI:SS' ) as \"time\", " +
							" tpdam.type as \"type\" , " +
							" tpdam.remark as \"remark\" , " +
							" tpdam.diagnosis_type as \"diagnosisType\" , " +
							" mdm.code as \"code\" , " +
							" mdm.diagnosis_desc as \"diagnosisDesc\", " +
							" 1 as  \"fromData\" ,"
							+ " tpdam.patient_diagnosis_service_mapper_id as \"patientDiagnosisServiceMapperId\","
							+ "  tpdam.diagnosis_id as \"diagnosisId\" " + 
							" from t_patient_diagnosis_service_mapper tpdam  " +
							" inner join  emr.m_diagnosis_master mdm on mdm.diagnosis_id  = tpdam.diagnosis_id " + 
							" where tpdam.type  = 1 and tpdam.status ='A' and tpdam.patient_id  = "+dto.getPatientId()+" and tpdam.unit_id  =" + dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientDiagnosisServiceMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
		logger.error("getPatientDaignosisServiceMapper failed", e);
			return new Response(ERROR, null, null, null, null);
		}

	}

}
