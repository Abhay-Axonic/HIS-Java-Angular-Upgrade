package com.param.opd.coversheet.dao;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;
import com.param.opd.coversheet.dto.DiagnosisMasterDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentDetailsDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.model.DiagnosisPatientAppointmentMapper;
import com.param.opd.coversheet.service.IDiagnosisMapperService;
import com.param.opd.coversheet.service.IDiagnosisMasterService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PatientDiagnosisDaoImpl extends GenericDao<DiagnosisPatientAppointmentMapper, Integer> implements IPatientDiagnosisDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientDiagnosisDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private IDiagnosisMasterService iDiagnosisMasterService;

	@Autowired
	private IDiagnosisMapperService iDiagnosisMapperService;

	public PatientDiagnosisDaoImpl() {
		super(DiagnosisPatientAppointmentMapper.class);
	}

	@Override
	public Response savePatientDiagnosis(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto)
			throws ApplicationException {
		try {
			for (int i = 0; i < diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().size(); i++) 
			{
				if(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getIsDiagnosisName() == 'N') {
					DiagnosisPatientAppointmentMapper diagnosisPatientAppointmentMapper = new DiagnosisPatientAppointmentMapper();
					diagnosisPatientAppointmentMapper.setCreated_date(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setEncounterId(diagnosisPatientAppointmentMapperDto.getEncounterId());
					diagnosisPatientAppointmentMapper.setAdmissionId(diagnosisPatientAppointmentMapperDto.getAdmissionId());
					diagnosisPatientAppointmentMapper.setCreated_by(diagnosisPatientAppointmentMapperDto.getCreated_by());
					diagnosisPatientAppointmentMapper.setUpdated_by(diagnosisPatientAppointmentMapperDto.getUpdated_by());
					diagnosisPatientAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setPatientId(diagnosisPatientAppointmentMapperDto.getPatientId());
					diagnosisPatientAppointmentMapper.setStatus(diagnosisPatientAppointmentMapperDto.getStatus());
					diagnosisPatientAppointmentMapper.setIsDiagnosisCancel('N');
					diagnosisPatientAppointmentMapper.setIsDeleted('N');
					diagnosisPatientAppointmentMapper.setIsResolved('N');
					diagnosisPatientAppointmentMapper.setResolvedDate(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setIsWhileAdmission(diagnosisPatientAppointmentMapperDto.getIsWhileAdmission()!=null ? diagnosisPatientAppointmentMapperDto.getIsWhileAdmission() : 'N');
					diagnosisPatientAppointmentMapper.setOrganizationId(diagnosisPatientAppointmentMapperDto.getOrganizationId());
					diagnosisPatientAppointmentMapper.setUnitId(diagnosisPatientAppointmentMapperDto.getUnitId());
					diagnosisPatientAppointmentMapper.setDiagnosisId(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisId());
					diagnosisPatientAppointmentMapper.setRemark(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getRemark());
					diagnosisPatientAppointmentMapper.setType(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getType());
					diagnosisPatientAppointmentMapper.setDiagnosisType(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisType());
			    save(diagnosisPatientAppointmentMapper);
				}else {
					
					//save diagnosis master
					
					DiagnosisMasterDto diagnosisMasterDto = new DiagnosisMasterDto();
					diagnosisMasterDto.setDiagnosisDesc(
							diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setStatus('A');
					diagnosisMasterDto.setCode(
							diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisCode());
					diagnosisMasterDto.setDiagnosisName(
							diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setIsNotifiable('N');
					diagnosisMasterDto.setShortDesc(
							diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setMarathiDiagnosisDesc(null);
					diagnosisMasterDto.setCreated_by(diagnosisPatientAppointmentMapperDto.getCreated_by());
					diagnosisMasterDto.setCreated_date(diagnosisPatientAppointmentMapperDto.getCreated_date());
					diagnosisMasterDto.setUpdated_by(diagnosisPatientAppointmentMapperDto.getUpdated_by());
					diagnosisMasterDto.setUpdated_date(diagnosisPatientAppointmentMapperDto.getUpdated_date());
					diagnosisMasterDto.setUnitId(diagnosisPatientAppointmentMapperDto.getUnitId());
					diagnosisMasterDto.setOrganizationId(diagnosisPatientAppointmentMapperDto.getOrganizationId());
					diagnosisMasterDto.setIsComorbidity('N');
					Response res = iDiagnosisMasterService.saveDiagnosisMaster(diagnosisMasterDto);
					DiagnosisMasterDto dto = (DiagnosisMasterDto) res.getObject();
					diagnosisPatientAppointmentMapperDto.setDiagnosisId(dto.getDiagnosisId());
					diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).setDiagnosisId(dto.getDiagnosisId());
					// Diagnosis Gender mapper save
					DiagnosisMapperDto diagnosisMapperDto = new DiagnosisMapperDto();
					diagnosisMapperDto.setDiagnosisId(diagnosisPatientAppointmentMapperDto.getDiagnosisId());
					diagnosisMapperDto.setIsNotifiable('N');
					diagnosisMapperDto.setIsCommunicable('N');
					diagnosisMapperDto.setIsNonCommunicable('N');
					diagnosisMapperDto.setIsMedicoLegal('N');
					diagnosisMapperDto.setIsDeleted('N');
					diagnosisMapperDto.setOrganizationId(diagnosisPatientAppointmentMapperDto.getOrganizationId());
					diagnosisMapperDto.setUnitId(diagnosisPatientAppointmentMapperDto.getUnitId());
					diagnosisMapperDto.setCreatedBy(diagnosisPatientAppointmentMapperDto.getCreated_by());
					diagnosisMapperDto.setUpdatedBy(diagnosisPatientAppointmentMapperDto.getUpdated_by());
					diagnosisMapperDto.setCreatedDate(diagnosisPatientAppointmentMapperDto.getCreated_date());
					diagnosisMapperDto.setUpdatedDate(diagnosisPatientAppointmentMapperDto.getUpdated_date());
					diagnosisMapperDto.setStatus('A');

					List<DiagnosisMapperDto> listOfGender = new ArrayList<DiagnosisMapperDto>();
					for (int j = 1; j <= 3; j++) {
						diagnosisMapperDto.setId(j);
						listOfGender.add(diagnosisMapperDto);
					}

					diagnosisMapperDto.setListOfGendersDto(listOfGender);
					Response res2 = iDiagnosisMapperService.saveDiagnosisMapper(diagnosisMapperDto);

					
					//save Dignosis patient Appo Mapper
					
					DiagnosisPatientAppointmentMapper diagnosisPatientAppointmentMapper = new DiagnosisPatientAppointmentMapper();
					diagnosisPatientAppointmentMapper.setCreated_date(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setEncounterId(diagnosisPatientAppointmentMapperDto.getEncounterId());
					diagnosisPatientAppointmentMapper.setAdmissionId(diagnosisPatientAppointmentMapperDto.getAdmissionId());
					diagnosisPatientAppointmentMapper.setCreated_by(diagnosisPatientAppointmentMapperDto.getCreated_by());
					diagnosisPatientAppointmentMapper.setUpdated_by(diagnosisPatientAppointmentMapperDto.getUpdated_by());
					diagnosisPatientAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setPatientId(diagnosisPatientAppointmentMapperDto.getPatientId());
					diagnosisPatientAppointmentMapper.setStatus(diagnosisPatientAppointmentMapperDto.getStatus());
					diagnosisPatientAppointmentMapper.setIsDiagnosisCancel('N');
					diagnosisPatientAppointmentMapper.setIsDeleted('N');
					diagnosisPatientAppointmentMapper.setIsResolved('N');
					diagnosisPatientAppointmentMapper.setResolvedDate(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setIsWhileAdmission(diagnosisPatientAppointmentMapperDto.getIsWhileAdmission()!=null ? diagnosisPatientAppointmentMapperDto.getIsWhileAdmission() : 'N');
					diagnosisPatientAppointmentMapper.setOrganizationId(diagnosisPatientAppointmentMapperDto.getOrganizationId());
					diagnosisPatientAppointmentMapper.setUnitId(diagnosisPatientAppointmentMapperDto.getUnitId());
					diagnosisPatientAppointmentMapper.setDiagnosisId(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisId());
					diagnosisPatientAppointmentMapper.setRemark(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getRemark());
					diagnosisPatientAppointmentMapper.setType(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getType());
					diagnosisPatientAppointmentMapper.setDiagnosisType(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().get(i).getDiagnosisType());
			    save(diagnosisPatientAppointmentMapper);
				}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("savePatientDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDiagnosisByPatientId(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {

			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList = sessionFactory
					.getCurrentSession().getNamedQuery("GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID")
//					.setParameter("unitId", diagnosisPatientAppointmentMapperDto.getUnitId())
					.setParameter("orgId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
					.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, diagnosisPatientAppointmentMapperList, null);
		} catch (HibernateException he) {
		logger.error("getPatientDiagnosisByPatientId failed", he);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByPatientId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDiagnosisForCaseSheet(int patientId, int visitId, int visitTypeId)
			throws ApplicationException {
		try {
			
			String query = sessionFactory
					.getCurrentSession().getNamedQuery("GET_PATIENT_DIAGNOSIS_BY_PATIENT_VISIT").getQueryString();

			StringBuffer sb  = new StringBuffer();
			sb.append(query);
			if(visitTypeId==1)
			{
				sb.append(" AND diagMapper.encounterId = :visitId");
			}
			else if(visitTypeId==2){
				sb.append(" AND diagMapper.admissionId = :visitId");
			}
			
			Query createQuery = sessionFactory
					.getCurrentSession().createQuery(sb.toString())
					.setParameter("patientId",patientId );

			if(visitTypeId==1)
			{
				createQuery.setParameter("visitId", visitId);
			}
			else if(visitTypeId==2){
				createQuery.setParameter("visitId", visitId);
			}

			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList=createQuery.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, diagnosisPatientAppointmentMapperList, null);
		} catch (HibernateException he) {
		logger.error("getPatientDiagnosisForCaseSheet failed", he);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisForCaseSheet failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDiagnosisByPatientIdForMedicalHistory(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			String dDate= GlobalCommonDateUtils.getAvalDate(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()), "yyyy-MM-dd");
			String query = "select " + 
					"	dm.patient_id as \"patientId\"," + 
					"	dm.diagnosis_id as \"diagnosisId\"," + 
					"	dm.type as \"type\", " + 
					"	to_char(dm.created_date,'DD/MM/YYYY') as \"created_date\", " + 
					"	dm.created_date as \"created_date_new\", " + 
					"	(SELECT CAST(DATE_PART('day',cast('"+dDate+"' as timestamp)  - "+
					"  cast(to_char(dm.resolved_date, 'YYYY-MM-DD') as timestamp)) AS int4)) as \"resolvedDays\" ,  " + 
					"	dm.remark as \"remark\", " + 
					"	diagnosisMaster.is_notifiable as \"isNotifiable\", " + 
					"	diagnosisMaster.is_comorbidity as \"isComorbidity\", " + 
					"   to_char(dm.resolved_date, 'DD/MM/YYYY') as \"resolvedDate\", "	+
					"	diagnosisMaster.code as \"code\"," + 
					"	diagnosisMaster.diagnosis_name as \"diagnosisName\", " + 
					"	dm.resolved_remark as \"resolvedRemark\", " + 
					"	dm.is_resolved as \"isResolved\" " + 
					"	from " + 
					"	patient.t_patient_diagnosis_appo_mapper dm " + 
					"	left join emr.m_diagnosis_master diagnosisMaster on " + 
					"	dm.diagnosis_id = diagnosisMaster.diagnosis_id " + 
					"	where " + 
					"	dm.patient_id ="+diagnosisPatientAppointmentMapperDto.getPatientId()+" " +
					"	and dm.organization_id ="+diagnosisPatientAppointmentMapperDto.getOrganizationId()+" " +
					"	and dm.is_resolved = 'Y' " + 
					"	and dm.is_deleted = 'N' " + 
					"	and dm.is_diagnosis_cancel = 'N'";
			List<DiagnosisPatientAppointmentMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		
		} catch (HibernateException he) {
		logger.error("getPatientDiagnosisByPatientIdForMedicalHistory failed", he);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByPatientIdForMedicalHistory failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response savePatientCancelledDiagnosis(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException
	{
		DiagnosisPatientAppointmentMapper diagnosisPatientAppointmentMapper = findById(diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId());
		
				diagnosisPatientAppointmentMapper.setIsDiagnosisCancel(diagnosisPatientAppointmentMapperDto.getIsDiagnosisCancel());
				diagnosisPatientAppointmentMapper.setJustificationCancel(diagnosisPatientAppointmentMapperDto.getJustificationCancel());
				diagnosisPatientAppointmentMapper.setUpdated_by(diagnosisPatientAppointmentMapperDto.getUpdated_by());
				diagnosisPatientAppointmentMapper.setUpdated_date(CommonDateUtils.getDate(diagnosisPatientAppointmentMapperDto.getUpdated_date(), "dd-MM-yyyy HH:mm:ss"));
	      update(diagnosisPatientAppointmentMapper);
	      
	      return new Response(SUCCESS, SUCCESS_CODE, "Diagnosis marked as entered in error.", null, null);
	}

	@Override
	public Response getListPatientCancelledDiagnosis(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {

			List<DiagnosisPatientAppointmentMapperDto> list = sessionFactory
					.getCurrentSession().getNamedQuery("GET_LIST_PATIENT_CANCELLED_DIAGNOSIS")
					.setParameter("unitId", diagnosisPatientAppointmentMapperDto.getUnitId())
					.setParameter("orgId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
					.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
		logger.error("getListPatientCancelledDiagnosis failed", he);
		} catch (Exception e) {
		logger.error("getListPatientCancelledDiagnosis failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDiagnosisByPatientIdForIcon(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {

			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList = sessionFactory
					.getCurrentSession().getNamedQuery("GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID_FOR_ICON")
//					.setParameter("unitId", diagnosisPatientAppointmentMapperDto.getUnitId())
					.setParameter("orgId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
					.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, diagnosisPatientAppointmentMapperList, null);
		} catch (HibernateException he) {
		logger.error("getPatientDiagnosisByPatientIdForIcon failed", he);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByPatientIdForIcon failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDiagnosisByPatientIdForBookView(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {

			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList = sessionFactory
					.getCurrentSession().getNamedQuery("GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID_FOR_BOOK_VIEW")
					.setParameter("orgId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
					.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
					.setParameter("fromDate", diagnosisPatientAppointmentMapperDto.getFromDate())
					.setParameter("toDate", diagnosisPatientAppointmentMapperDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, diagnosisPatientAppointmentMapperList, null);
		} catch (HibernateException he) {
		logger.error("getPatientDiagnosisByPatientIdForBookView failed", he);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByPatientIdForBookView failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePatientDiagnosisType(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			if(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().size()>1) {
			for (DiagnosisPatientAppointmentDetailsDto dto : diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto()) {
					if(dto.getDiagnosisType()!=2 && dto.getIsDeleted()=='N') {
							session.createSQLQuery(" update patient.t_patient_diagnosis_appo_mapper  set diagnosis_type = 2 "
									+" , type="+dto.getType()
							+ " where patient_id="+diagnosisPatientAppointmentMapperDto.getPatientId()
							+ " and diagnosis_type= 1 "
		//					+ " and patient_diagnosis_id="+diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId()
							)
							.executeUpdate();
									
							if(diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId()!=null) {
							session.createSQLQuery(" update patient.t_patient_diagnosis_appo_mapper  set diagnosis_type = "+dto.getDiagnosisType()
							+" , type="+dto.getType()
							+ " where patient_diagnosis_id="+diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId())
							.executeUpdate();
							}
					}
				}
			}
			if(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().size()==1) {
				for (DiagnosisPatientAppointmentDetailsDto dto : diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto()) {
					if(dto.getDiagnosisType()!=2 && dto.getIsDeleted()=='N') {
							session.createSQLQuery(" update patient.t_patient_diagnosis_appo_mapper  set diagnosis_type ="+dto.getDiagnosisType()
									+" , type="+dto.getType()
							+ " where patient_id="+diagnosisPatientAppointmentMapperDto.getPatientId()
							+ " and diagnosis_type= 1 "
		//					+ " and patient_diagnosis_id="+diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId()
							)
							.executeUpdate();
									
							if(diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId()!=null) {
							session.createSQLQuery(" update patient.t_patient_diagnosis_appo_mapper  set diagnosis_type = "+dto.getDiagnosisType()
							+" , type="+dto.getType()
							+ " where patient_diagnosis_id="+diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId())
							.executeUpdate();
							}
					}
				}
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("updatePatientDiagnosisType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkPrimaryDaignosisAvailabel(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			for (DiagnosisPatientAppointmentDetailsDto dto : diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto()) {
				List<DiagnosisPatientAppointmentMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_PRIMARY_DAIGNOSIS_AVAILABEL")
						.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
						.setParameter("diagnosisType", dto.getDiagnosisType())
						.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
				 
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}
		
		} catch (HibernateException he) {
		logger.error("checkPrimaryDaignosisAvailabel failed", he);
		} catch (Exception e) {
		logger.error("checkPrimaryDaignosisAvailabel failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDiagnosisTypeAsSecondary(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
//			if(diagnosisPatientAppointmentMapperDto.getDiagnosisType()!=2) {
			session.createSQLQuery(" update patient.t_patient_diagnosis_appo_mapper  set diagnosis_type = 2 "
			+ " where patient_id="+diagnosisPatientAppointmentMapperDto.getPatientId()
			+ " and diagnosis_type= 1 "
			)
			.executeUpdate();
			
//			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("updateDiagnosisTypeAsSecondary failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveResolvedDiagnosisDate(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto)
			throws ApplicationException {
		try {
			
			DiagnosisPatientAppointmentMapper mppr=findById(diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId());
			mppr.setResolvedDate(GlobalCommonDateUtils.getDate(diagnosisPatientAppointmentMapperDto.getResolvedDate(), "dd-M-yyyy HH:mm:ss"));
			mppr.setResolvedRemark(diagnosisPatientAppointmentMapperDto.getResolvedRemark());
			mppr.setIsResolved('Y');
			update(mppr);
			return new Response(SUCCESS, SUCCESS_CODE, "Diagnosis marked as Resolved.", null, null);
		} catch (Exception e) {
		logger.error("saveResolvedDiagnosisDate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDiagnosisByEncounterId(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {

			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList = sessionFactory
					.getCurrentSession().getNamedQuery("GET_PATIENT_DIAGNOSIS_BY_ECNCOUNTER_ID")
					.setParameter("encounterId", diagnosisPatientAppointmentMapperDto.getEncounterId())
					.setParameter("orgId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
					.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, diagnosisPatientAppointmentMapperList, null);
		} catch (HibernateException he) {
		logger.error("getPatientDiagnosisByEncounterId failed", he);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByEncounterId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDiagnosisByAdmissionId(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {

			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList = sessionFactory
					.getCurrentSession().getNamedQuery("GET_PATIENT_DIAGNOSIS_BY_ADMISSION_ID")
					.setParameter("admissionId", diagnosisPatientAppointmentMapperDto.getAdmissionId())
					.setParameter("orgId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
					.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, diagnosisPatientAppointmentMapperList, null);
		} catch (HibernateException he) {
		logger.error("getPatientDiagnosisByAdmissionId failed", he);
		} catch (Exception e) {
		logger.error("getPatientDiagnosisByAdmissionId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFrequentlyUsedDiagnosisList(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {

			List<DiagnosisPatientAppointmentMapperDto> diagnosisPatientAppointmentMapperList = sessionFactory
					.getCurrentSession().getNamedQuery("GET_FREQUENTLY_USED_DIAGNOSIS_LIST")
					.setParameter("orgId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, diagnosisPatientAppointmentMapperList, null);
		} catch (HibernateException he) {
		logger.error("getFrequentlyUsedDiagnosisList failed", he);
		} catch (Exception e) {
		logger.error("getFrequentlyUsedDiagnosisList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePatientDiagnosisTypeOPD(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();

			if(diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto().size()==1) {
				for (DiagnosisPatientAppointmentDetailsDto dto : diagnosisPatientAppointmentMapperDto.getListDiagnosisPatientAppointmentDetailsDto()) {
								    session.createSQLQuery(" update patient.t_patient_diagnosis_appo_mapper set diagnosis_type ="+dto.getDiagnosisType()
								+" , type="+dto.getType()+",remark="+"'"+ dto.getRemark()+"'"+
								 " where patient_diagnosis_id="+diagnosisPatientAppointmentMapperDto.getPatientDiagnosisId()
							)
							.executeUpdate();
				}
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("updatePatientDiagnosisTypeOPD failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveDiagnosisPatientList(List<DiagnosisPatientAppointmentMapperDto> list,CoversheetTemplateMasterDto resDto) throws ApplicationException {
try{
			
			for (DiagnosisPatientAppointmentMapperDto dto : list){
//				if(resDto.getCovTemplateId() == null){
					DiagnosisPatientAppointmentMapper diagnosisPatientAppointmentMapper = new DiagnosisPatientAppointmentMapper();
					diagnosisPatientAppointmentMapper.setCreated_date(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
//					diagnosisPatientAppointmentMapper.setEncounterId(resDto.getEncounterId());
//					diagnosisPatientAppointmentMapper.setAdmissionId(resDto.getAdmissionId());
					diagnosisPatientAppointmentMapper.setCreated_by(resDto.getCreatedBy());
					diagnosisPatientAppointmentMapper.setUpdated_by(resDto.getUpdatedBy());
					diagnosisPatientAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
//					diagnosisPatientAppointmentMapper.setPatientId(resDto.getPatientId());
					diagnosisPatientAppointmentMapper.setStatus(resDto.getStatus());
					diagnosisPatientAppointmentMapper.setIsDiagnosisCancel('N');
					diagnosisPatientAppointmentMapper.setIsDeleted('N');
					diagnosisPatientAppointmentMapper.setIsResolved('N');
					diagnosisPatientAppointmentMapper.setResolvedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setIsWhileAdmission(dto.getIsWhileAdmission()!=null ? dto.getIsWhileAdmission() : 'N');
					diagnosisPatientAppointmentMapper.setOrganizationId(resDto.getOrganizationId());
					diagnosisPatientAppointmentMapper.setUnitId(resDto.getUnitId());
					diagnosisPatientAppointmentMapper.setDiagnosisId(dto.getDiagnosisId());
					diagnosisPatientAppointmentMapper.setDiagnosisText(dto.getDiagnosisText());
					diagnosisPatientAppointmentMapper.setRemark(dto.getRemark());
					diagnosisPatientAppointmentMapper.setCovTemplateId(resDto.getCovTemplateId());
					diagnosisPatientAppointmentMapper.setType(dto.getType());
					diagnosisPatientAppointmentMapper.setDiagnosisType(dto.getDiagnosisType());
			    save(diagnosisPatientAppointmentMapper);
				
//				}
			    /*else {
					DiagnosisPatientAppointmentMapper diagnosisPatientAppointmentMapper = findById(resDto.getCovTemplateId());
					diagnosisPatientAppointmentMapper.setCreated_date(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setCreated_by(resDto.getCreatedBy());
					diagnosisPatientAppointmentMapper.setUpdated_by(resDto.getUpdatedBy());
					diagnosisPatientAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setStatus(resDto.getStatus());
					diagnosisPatientAppointmentMapper.setIsDiagnosisCancel('N');
					diagnosisPatientAppointmentMapper.setIsDeleted('N');
					diagnosisPatientAppointmentMapper.setIsResolved('N');
					diagnosisPatientAppointmentMapper.setResolvedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setIsWhileAdmission(dto.getIsWhileAdmission()!=null ? dto.getIsWhileAdmission() : 'N');
					diagnosisPatientAppointmentMapper.setOrganizationId(resDto.getOrganizationId());
					diagnosisPatientAppointmentMapper.setUnitId(resDto.getUnitId());
					diagnosisPatientAppointmentMapper.setDiagnosisId(dto.getDiagnosisId());
					diagnosisPatientAppointmentMapper.setRemark(dto.getRemark());
					diagnosisPatientAppointmentMapper.setType(dto.getType());
					diagnosisPatientAppointmentMapper.setDiagnosisType(dto.getDiagnosisType());
			   update(diagnosisPatientAppointmentMapper);
				
				}*/
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempDiagnosisPatientList(List<DiagnosisPatientAppointmentMapperDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) throws ApplicationException {
try{
			
			for (DiagnosisPatientAppointmentMapperDto dto : list){
				if(dto.getCovTemplateId() != null){
				
					DiagnosisPatientAppointmentMapper diagnosisPatientAppointmentMapper = findById(dto.getCovTemplateId());
					diagnosisPatientAppointmentMapper.setCreated_date(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
//					diagnosisPatientAppointmentMapper.setEncounterId(resDto.getEncounterId());
//					diagnosisPatientAppointmentMapper.setAdmissionId(resDto.getAdmissionId());
					diagnosisPatientAppointmentMapper.setCreated_by(coversheetTemplateMasterDto.getCreatedBy());
					diagnosisPatientAppointmentMapper.setUpdated_by(coversheetTemplateMasterDto.getUpdatedBy());
					diagnosisPatientAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
//					diagnosisPatientAppointmentMapper.setPatientId(resDto.getPatientId());
					diagnosisPatientAppointmentMapper.setStatus(coversheetTemplateMasterDto.getStatus());
					diagnosisPatientAppointmentMapper.setIsDiagnosisCancel('N');
					diagnosisPatientAppointmentMapper.setIsDeleted('N');
					diagnosisPatientAppointmentMapper.setIsResolved('N');
					diagnosisPatientAppointmentMapper.setResolvedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					diagnosisPatientAppointmentMapper.setIsWhileAdmission(dto.getIsWhileAdmission()!=null ? dto.getIsWhileAdmission() : 'N');
					diagnosisPatientAppointmentMapper.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					diagnosisPatientAppointmentMapper.setUnitId(coversheetTemplateMasterDto.getUnitId());
					diagnosisPatientAppointmentMapper.setDiagnosisId(dto.getDiagnosisId());
					diagnosisPatientAppointmentMapper.setRemark(dto.getRemark());
					diagnosisPatientAppointmentMapper.setType(dto.getType());
					diagnosisPatientAppointmentMapper.setDiagnosisType(dto.getDiagnosisType());
			   update(diagnosisPatientAppointmentMapper);
				
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveFreeTexPatientDiagnosis(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try {
			DiagnosisPatientAppointmentMapper diagnosisPatientAppointmentMapper = new DiagnosisPatientAppointmentMapper();
			diagnosisPatientAppointmentMapper.setCreated_date(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
			diagnosisPatientAppointmentMapper.setEncounterId(diagnosisPatientAppointmentMapperDto.getEncounterId());
			diagnosisPatientAppointmentMapper.setAdmissionId(diagnosisPatientAppointmentMapperDto.getAdmissionId());
			diagnosisPatientAppointmentMapper.setCreated_by(diagnosisPatientAppointmentMapperDto.getCreated_by());
			diagnosisPatientAppointmentMapper.setUpdated_by(diagnosisPatientAppointmentMapperDto.getUpdated_by());
			diagnosisPatientAppointmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
			diagnosisPatientAppointmentMapper.setPatientId(diagnosisPatientAppointmentMapperDto.getPatientId());
			diagnosisPatientAppointmentMapper.setStatus(diagnosisPatientAppointmentMapperDto.getStatus());
			diagnosisPatientAppointmentMapper.setIsDiagnosisCancel('N');
			diagnosisPatientAppointmentMapper.setIsDeleted('N');
			diagnosisPatientAppointmentMapper.setIsResolved('N');
			diagnosisPatientAppointmentMapper.setResolvedDate(TimeZoneComponent.getDateByzone(diagnosisPatientAppointmentMapperDto.getUnitId()));
			diagnosisPatientAppointmentMapper.setIsWhileAdmission(diagnosisPatientAppointmentMapperDto.getIsWhileAdmission() != null? diagnosisPatientAppointmentMapperDto.getIsWhileAdmission(): 'N');
			diagnosisPatientAppointmentMapper.setOrganizationId(diagnosisPatientAppointmentMapperDto.getOrganizationId());
			diagnosisPatientAppointmentMapper.setUnitId(diagnosisPatientAppointmentMapperDto.getUnitId());
			diagnosisPatientAppointmentMapper.setDiagnosisText(diagnosisPatientAppointmentMapperDto.getDiagnosisText());
			diagnosisPatientAppointmentMapper.setRemark(diagnosisPatientAppointmentMapperDto.getRemark());
			diagnosisPatientAppointmentMapper.setType(diagnosisPatientAppointmentMapperDto.getType());
			diagnosisPatientAppointmentMapper.setDiagnosisType(diagnosisPatientAppointmentMapperDto.getDiagnosisType());
			save(diagnosisPatientAppointmentMapper);

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response serchFreeTexPatientDiagnosis(
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) throws ApplicationException {
		try
		{
			List<DiagnosisPatientAppointmentMapperDto> list;
			String query=" select " + 
					"	distinct(mppr.diagnosis_text) as \"diagnosisName\",  " + 
//					"	mppr.patient_diagnosis_id  as \"patientDiagnosisId\", " + 
					"	mppr.status as \"status\",  " + 
					//"	mppr.is_diagnosis_cancel as \"isDiagnosisCancel\", " + 
					
					"	mppr.diagnosis_id as \"diagnosisId\"  " + 
					" from  " + 
					"	patient.t_patient_diagnosis_appo_mapper mppr " + 
					" where  " + 
					"	 mppr.status = 'A' " + 
					//"	and mppr.is_diagnosis_cancel= 'N'  " + 
					"	and mppr.organization_id =:organizationId " + 
					"	and mppr.is_resolved = 'N'  " + 
					"	and (lower(mppr.diagnosis_text) like '%"+diagnosisPatientAppointmentMapperDto.getSearchKeywordText()+"%' or mppr.diagnosis_text like '%"+diagnosisPatientAppointmentMapperDto.getSearchKeywordText()+"%')";
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				 list = statlessSession.createSQLQuery(query)
						.setParameter("organizationId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
				statlessSession.close();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("serchFreeTexPatientDiagnosis failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getdiagnosisNameByName(DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto)
			throws ApplicationException {
		try
		{
			List<DiagnosisPatientAppointmentMapperDto> list;
			String query="select " + 
					"	mppr.status as \"status\",  " + 
					"	mppr.is_diagnosis_cancel as \"isDiagnosisCancel\", " + 
					"	mppr.diagnosis_text as \"diagnosisName\", " + 
					"	mppr.diagnosis_id as \"diagnosisId\"  " + 
					" from  " + 
					"	patient.t_patient_diagnosis_appo_mapper mppr " + 
					" where " + 
					"	 mppr.status = 'A'" + 
					"	 and mppr.is_diagnosis_cancel= 'N'" + 
					"	and mppr.organization_id =:organizationId " + 
					"	and mppr.is_resolved = 'N'  " + 
					"	and mppr.patient_id =:patientId  " + 
					"	and mppr.encounter_id =:encounterId  " +
					"	and ((lower(mppr.diagnosis_text) =:diagnosisName) or (mppr.diagnosis_text=:diagnosisName)) ";
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				 list = statlessSession.createSQLQuery(query)
							.setParameter("diagnosisName", diagnosisPatientAppointmentMapperDto.getDiagnosisText())
							.setParameter("organizationId", diagnosisPatientAppointmentMapperDto.getOrganizationId())
							.setParameter("patientId", diagnosisPatientAppointmentMapperDto.getPatientId())
						 .setParameter("encounterId", diagnosisPatientAppointmentMapperDto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
				statlessSession.close();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getdiagnosisNameByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response updateDiagnosisPatientList(List<DiagnosisPatientAppointmentMapperDto> listDiagnosisData,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try
		{
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update patient.t_patient_diagnosis_appo_mapper  set status ='I'"
			+ " where cov_template_id="+coversheetTemplateMasterDto.getCovTemplateId())
			.executeUpdate();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateDiagnosisPatientList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}




}
