package com.param.opd.coversheet.dao;

import com.google.gson.Gson;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.dto.*;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.model.PatientFamilyHistory;
import com.param.opd.coversheet.model.PatientPersonalHistoryDetails;
import com.param.opd.coversheet.model.PatientTravelHistoryDetails;
import com.param.opd.coversheet.model.PatientTreatmentHistoryDetails;
import com.param.opd.coversheet.service.IDiagnosisMapperService;
import com.param.opd.coversheet.service.IDiagnosisMasterService;
import com.param.opd.coversheet.service.IpatientFamilyHistoryService;
import com.param.utility.TimeZoneComponent;
import org.springframework.web.client.RestTemplate;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientFamilyHistoryDaoImpl extends GenericDao<PatientFamilyHistory, Integer> implements IPatientFamilyHistoryDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientFamilyHistoryDaoImpl.class);


	
	public PatientFamilyHistoryDaoImpl() {
		super(PatientFamilyHistory.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IpatientFamilyHistoryService ipatientFamilyHistoryService;
	
	@Autowired
	private IDiagnosisMasterService iDiagnosisMasterService;

	@Autowired
	private IDiagnosisMapperService iDiagnosisMapperService;

	@Autowired
	private Gson gson;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@Override
	public Response savePatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException {
		try
		{
			String Global_Scheduling_URI = null;
			String MY_LIFE_URI = null;
			String uuid = null;
			URI urlMyLife = null;
			Response responseMyLife = null;
			Response commonResponseMyLife = null;
			Boolean isOPDPatient = false;
			Boolean isSyncScheduling = false;
			Integer channelId = null;
			String token = null;

			List<PatientFamilyHistoryDto> historyDtoList = new ArrayList<>();

			if(patientFamilyHistoryDto.getEncounterId() != null && patientFamilyHistoryDto.getEncounterId()>0) {

				isSyncScheduling = (Boolean) sessionFactory.getCurrentSession()
						.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
						.uniqueResult();

				channelId = (Integer) sessionFactory.getCurrentSession()
						.createSQLQuery("  select org.channel_id as \"channelId\" from public.m_organization_master org where  org.organization_id=:organizationId and org.status='A' ")
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
						.uniqueResult();
				uuid = (String) sessionFactory.getCurrentSession()
						.createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "
								+ "	where patReg.patient_id=:patientId and patReg.organization_id=:orgId ")
						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
						.setParameter("orgId", patientFamilyHistoryDto.getOrganizationId())
						.uniqueResult();

				if (isSyncScheduling) {
					Response res = iScheduleLogsDao.getConnectionStringForMyLife();
					MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
					if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null && connectionLinkDto.getMyLifeLink() != "" && connectionLinkDto.getGlobalSchedulingLink() != null && connectionLinkDto.getGlobalSchedulingLink() != "") {
						Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
						MY_LIFE_URI = connectionLinkDto.getMyLifeLink();

						RestTemplate restTemplate = new RestTemplate();
						String urlWithParams = MY_LIFE_URI + "/generateToken" + "?userName=" + "ClinivantageAdminUser";

						Map<String, String> mapResponse = restTemplate.getForObject(urlWithParams, Map.class);

						if (mapResponse != null) {
							token = mapResponse.get("token");
						}
					}
				}

			}

			for(int i=0;i<patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().size();i++)
			{
				PatientFamilyHistoryDto historyDto = new PatientFamilyHistoryDto();
				if (patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsDiagnosisName() == 'N') {
				
				if(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId() != null &&
						patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId()>0)
				{
					PatientFamilyHistory patientFamilyHistory = findById(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId());
					patientFamilyHistory.setIsReviewedFlag(YES);
					patientFamilyHistory.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
					patientFamilyHistory.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
					patientFamilyHistory.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
					patientFamilyHistory.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
					
					patientFamilyHistory.setRoleId(patientFamilyHistoryDto.getRoleId());
					patientFamilyHistory.setIsReviewedFlag(
								patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
								patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag()
							);
					
					patientFamilyHistory.setIsEnterInErrorStatus( patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus() );
					patientFamilyHistory.setCreatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
					if(patientFamilyHistoryDto.getEncounterId()!=null){
						patientFamilyHistory.setEncounterId(patientFamilyHistoryDto.getEncounterId());
						isOPDPatient = true;
					}
					if(patientFamilyHistoryDto.getAdmissionId()!=null){
						patientFamilyHistory.setAdmissionId(patientFamilyHistoryDto.getAdmissionId());
					}
					
					patientFamilyHistory.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
					patientFamilyHistory.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
					patientFamilyHistory.setUpdatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientFamilyHistory.setStatus('I');
					patientFamilyHistory.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
					patientFamilyHistory.setUnitId(patientFamilyHistoryDto.getUnitId());
					patientFamilyHistory.setPatientId(patientFamilyHistoryDto.getPatientId());
					
					PatientFamilyHistory historyId=save(patientFamilyHistory);
					ipatientFamilyHistoryService.updateSaveNurseReviewedFlag(patientFamilyHistory);
				}else {
				
				PatientFamilyHistory patientFamilyHistory = new PatientFamilyHistory();
				patientFamilyHistory.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
				patientFamilyHistory.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
				patientFamilyHistory.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
				patientFamilyHistory.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
				
				patientFamilyHistory.setRoleId(patientFamilyHistoryDto.getRoleId());
				patientFamilyHistory.setIsReviewedFlag(
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag()
						);
				
				patientFamilyHistory.setIsEnterInErrorStatus( patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus() );
				patientFamilyHistory.setCreatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
				if(patientFamilyHistoryDto.getEncounterId()!=null){
					patientFamilyHistory.setEncounterId(patientFamilyHistoryDto.getEncounterId());
					isOPDPatient = true;
				}
				if(patientFamilyHistoryDto.getAdmissionId()!=null){
					patientFamilyHistory.setAdmissionId(patientFamilyHistoryDto.getAdmissionId());
				}
				
				patientFamilyHistory.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
				patientFamilyHistory.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
				patientFamilyHistory.setUpdatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientFamilyHistory.setStatus(patientFamilyHistoryDto.getStatus());
				patientFamilyHistory.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
				patientFamilyHistory.setUnitId(patientFamilyHistoryDto.getUnitId());
				patientFamilyHistory.setPatientId(patientFamilyHistoryDto.getPatientId());
				
				PatientFamilyHistory historyId=save(patientFamilyHistory);

				if(historyId.getFamilyHistoryId() != null && isSyncScheduling && isOPDPatient){

					PatientFamilyHistoryDto tempDto = new PatientFamilyHistoryDto();

					tempDto.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
					tempDto.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
					tempDto.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
					tempDto.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
					tempDto.setHospitalId(patientFamilyHistoryDto.getOrganizationId());
					tempDto.setUuid(uuid != null ? uuid:"0");

					tempDto.setIsReviewedFlag(
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
									patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag()
					);
					tempDto.setIsEnterInErrorStatus( patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus() );
//					tempDto.setDiagnosisDesc(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisNameText() != null ?
//							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisNameText() : patientFamilyHistoryDto.getDiagnosisDesc());
					tempDto.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
					tempDto.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
					tempDto.setUpdatedDate(patientFamilyHistoryDto.getUpdatedDate());
					tempDto.setStatus(patientFamilyHistoryDto.getStatus());
					tempDto.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
					tempDto.setUnitId(patientFamilyHistoryDto.getUnitId());
					tempDto.setPatientId(patientFamilyHistoryDto.getPatientId());
					tempDto.setCreatedDate(patientFamilyHistoryDto.getCreatedDate());
					tempDto.setEncounterId(patientFamilyHistoryDto.getEncounterId());
					tempDto.setApplicationId(2);

					historyDtoList.add(tempDto);
				}
				
				}
				}else {
					
					
					// Diagnosis Master save
					DiagnosisMasterDto diagnosisMasterDto = new DiagnosisMasterDto();
					diagnosisMasterDto.setDiagnosisDesc(
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setStatus('A');
					diagnosisMasterDto.setCode(
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisCode());
					diagnosisMasterDto.setDiagnosisName(
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setIsNotifiable('N');
					diagnosisMasterDto.setShortDesc(
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setMarathiDiagnosisDesc(null);
					diagnosisMasterDto.setCreated_by(patientFamilyHistoryDto.getCreatedBy());
					diagnosisMasterDto.setCreated_date(patientFamilyHistoryDto.getCreatedDate());
					diagnosisMasterDto.setUpdated_by(patientFamilyHistoryDto.getUpdatedBy());
					diagnosisMasterDto.setUpdated_date(patientFamilyHistoryDto.getUpdatedDate());
					diagnosisMasterDto.setUnitId(patientFamilyHistoryDto.getUnitId());
					diagnosisMasterDto.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
					diagnosisMasterDto.setIsComorbidity('N');
					Response res = iDiagnosisMasterService.saveDiagnosisMaster(diagnosisMasterDto);
					DiagnosisMasterDto dto = (DiagnosisMasterDto) res.getObject();
					patientFamilyHistoryDto.setDiagnosisId(dto.getDiagnosisId());
					patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).setDiagnosisId(dto.getDiagnosisId());
					// Diagnosis Gender mapper save
					DiagnosisMapperDto diagnosisMapperDto = new DiagnosisMapperDto();
					diagnosisMapperDto.setDiagnosisId(patientFamilyHistoryDto.getDiagnosisId());
					diagnosisMapperDto.setIsNotifiable('N');
					diagnosisMapperDto.setIsCommunicable('N');
					diagnosisMapperDto.setIsNonCommunicable('N');
					diagnosisMapperDto.setIsMedicoLegal('N');
					diagnosisMapperDto.setIsDeleted('N');
					diagnosisMapperDto.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
					diagnosisMapperDto.setUnitId(patientFamilyHistoryDto.getUnitId());
					diagnosisMapperDto.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
					diagnosisMapperDto.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
					diagnosisMapperDto.setCreatedDate(patientFamilyHistoryDto.getCreatedDate());
					diagnosisMapperDto.setUpdatedDate(patientFamilyHistoryDto.getUpdatedDate());
					diagnosisMapperDto.setStatus('A');

					List<DiagnosisMapperDto> listOfGender = new ArrayList<DiagnosisMapperDto>();
					for (int j = 1; j <= 3; j++) {
						diagnosisMapperDto.setId(j);
						listOfGender.add(diagnosisMapperDto);
					}

					diagnosisMapperDto.setListOfGendersDto(listOfGender);
					Response res2 = iDiagnosisMapperService.saveDiagnosisMapper(diagnosisMapperDto);
					
					
					//save family code 
					if(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId() != null &&
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId()>0)
					{
						PatientFamilyHistory patientFamilyHistory = findById(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId());
						patientFamilyHistory.setIsReviewedFlag(YES);
						patientFamilyHistory.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
						patientFamilyHistory.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
						patientFamilyHistory.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
						patientFamilyHistory.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
						
						patientFamilyHistory.setRoleId(patientFamilyHistoryDto.getRoleId());
						patientFamilyHistory.setIsReviewedFlag(
									patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
									patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag()
								);
						
						patientFamilyHistory.setIsEnterInErrorStatus( patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus() );
						patientFamilyHistory.setCreatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
						if(patientFamilyHistoryDto.getEncounterId()!=null){
							patientFamilyHistory.setEncounterId(patientFamilyHistoryDto.getEncounterId());
						}
						if(patientFamilyHistoryDto.getAdmissionId()!=null){
							patientFamilyHistory.setAdmissionId(patientFamilyHistoryDto.getAdmissionId());
						}
						
						patientFamilyHistory.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
						patientFamilyHistory.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
						patientFamilyHistory.setUpdatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
						patientFamilyHistory.setStatus('I');
						patientFamilyHistory.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
						patientFamilyHistory.setUnitId(patientFamilyHistoryDto.getUnitId());
						patientFamilyHistory.setPatientId(patientFamilyHistoryDto.getPatientId());
						
						PatientFamilyHistory historyId=save(patientFamilyHistory);
						ipatientFamilyHistoryService.updateSaveNurseReviewedFlag(patientFamilyHistory);
					}else {
					
					PatientFamilyHistory patientFamilyHistory = new PatientFamilyHistory();
					patientFamilyHistory.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
					patientFamilyHistory.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
					patientFamilyHistory.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
					patientFamilyHistory.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
					
					patientFamilyHistory.setRoleId(patientFamilyHistoryDto.getRoleId());
					patientFamilyHistory.setIsReviewedFlag(
								patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
								patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag()
							);
					
					patientFamilyHistory.setIsEnterInErrorStatus( patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus() );
					patientFamilyHistory.setCreatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
					if(patientFamilyHistoryDto.getEncounterId()!=null){
						patientFamilyHistory.setEncounterId(patientFamilyHistoryDto.getEncounterId());
						isOPDPatient = true;
					}
					if(patientFamilyHistoryDto.getAdmissionId()!=null){
						patientFamilyHistory.setAdmissionId(patientFamilyHistoryDto.getAdmissionId());
					}
					
					patientFamilyHistory.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
					patientFamilyHistory.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
					patientFamilyHistory.setUpdatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientFamilyHistory.setStatus(patientFamilyHistoryDto.getStatus());
					patientFamilyHistory.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
					patientFamilyHistory.setUnitId(patientFamilyHistoryDto.getUnitId());
					patientFamilyHistory.setPatientId(patientFamilyHistoryDto.getPatientId());

					PatientFamilyHistory historyId=save(patientFamilyHistory);

						if(historyId.getFamilyHistoryId() != null && isSyncScheduling && isOPDPatient){

							PatientFamilyHistoryDto tempDto = new PatientFamilyHistoryDto();

							tempDto.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
							tempDto.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
							tempDto.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
							tempDto.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
							tempDto.setHospitalId(patientFamilyHistoryDto.getOrganizationId());
							tempDto.setUuid(uuid != null ? uuid:"0");

							tempDto.setIsReviewedFlag(
									patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
											patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag()
							);
							tempDto.setIsEnterInErrorStatus( patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus() );
							tempDto.setDiagnosisDesc(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisNameText() != null ?
									patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisNameText() : patientFamilyHistoryDto.getDiagnosisDesc());
							tempDto.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
							tempDto.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
							tempDto.setUpdatedDate(patientFamilyHistoryDto.getUpdatedDate());
							tempDto.setStatus(patientFamilyHistoryDto.getStatus());
							tempDto.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
							tempDto.setUnitId(patientFamilyHistoryDto.getUnitId());
							tempDto.setPatientId(patientFamilyHistoryDto.getPatientId());
							tempDto.setCreatedDate(patientFamilyHistoryDto.getCreatedDate());
							tempDto.setEncounterId(patientFamilyHistoryDto.getEncounterId());
							tempDto.setApplicationId(2);

							historyDtoList.add(tempDto);
						}
					}
				}
			}

			if(!historyDtoList.isEmpty() && isSyncScheduling){
				List<PatientFamilyHistoryDto> relationIdAndNameList = new ArrayList<>();
				List<PatientFamilyHistoryDto> diagnosisIdAndNameList = new ArrayList<>();
				List<Integer> diagnosisIds = historyDtoList.stream().map(PatientFamilyHistoryDto::getDiagnosisId).filter(Objects::nonNull).collect(Collectors.toList());
				List<Integer> relationIds = historyDtoList.stream().map(PatientFamilyHistoryDto::getRelationId).filter(Objects::nonNull).collect(Collectors.toList());

				if( diagnosisIds != null && !diagnosisIds.isEmpty()) {
					String sqlQuery = "  select mst.diagnosis_id as \"diagnosisId\", mst.diagnosis_name as \"diagnosisDesc\" from  emr.m_diagnosis_master mst "
							+ "	where mst.status='A' and mst.diagnosis_id in (:diagnosisIds)";

					StringBuffer sb = new StringBuffer(sqlQuery);
					diagnosisIdAndNameList = getSession().createSQLQuery(sb.toString())
							.setParameterList("diagnosisIds", diagnosisIds)
							.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
				}
				if(relationIds!= null && !relationIds.isEmpty()) {
					String sqlQueryRelation = "  select mst.relation_id as \"relationId\", mst.relation_name as \"relationDesc\" from  public.m_relation_master mst "
							+ " where  mst.organization_id=:organizationId "
							+ "	and mst.status='A' and mst.relation_id in (:relationIds)";

					StringBuffer sbRelation = new StringBuffer(sqlQueryRelation);
					relationIdAndNameList = getSession().createSQLQuery(sbRelation.toString())
							.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
							.setParameterList("relationIds", relationIds)
							.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
				}

				if(diagnosisIdAndNameList != null && !diagnosisIdAndNameList.isEmpty() && diagnosisIdAndNameList.size() == diagnosisIds.size()){
					for (PatientFamilyHistoryDto dto : historyDtoList) {
						PatientFamilyHistoryDto objectDto = diagnosisIdAndNameList.stream()
								.filter(order -> dto.getDiagnosisId().equals(order.getDiagnosisId()))
								.findAny()
								.orElse(null);
						if(objectDto!=null){
							dto.setDiagnosisDesc(objectDto.getDiagnosisDesc());
						}
					}
				}

				if((relationIdAndNameList != null && !relationIdAndNameList.isEmpty()) && (relationIdAndNameList.size() == relationIds.size())){
					for (PatientFamilyHistoryDto dto : historyDtoList) {
						PatientFamilyHistoryDto objectDto = relationIdAndNameList.stream()
								.filter(order -> dto.getRelationId().equals(order.getRelationId()))
								.findAny()
								.orElse(null);
						if(objectDto!=null){
							dto.setRelationDesc(objectDto.getRelationDesc());
						}
					}
				}

				PatientFamilyHistoryDto reqHistoryDto = new PatientFamilyHistoryDto();
				reqHistoryDto.setPatientFamilyHistoryDtoList(historyDtoList);

				try {
					urlMyLife = URI.create(MY_LIFE_URI + "/saveFamilyHistoryFromHis");
					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + token);
					headers.set("userName", "ClinivantageAdminUser");
					headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
					HttpEntity<PatientFamilyHistoryDto> request = new HttpEntity<>(reqHistoryDto, headers);
					String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
					commonResponseMyLife = gson.fromJson(stringResponse, Response.class);

				}catch(Exception e){
					logger.error("savePatientFamilyHistory MyLife sync failed", e);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("savePatientFamilyHistory failed", he);
		} catch (Exception e) {
		logger.error("savePatientFamilyHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	

	@Override
	public Response getOldPatientFamilyHistory(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto;
			/*if(patientFamilyHistoryDto.getRoleId() == 3)
			{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_PATIENT_FAMILY_HISTORY_BY_ROLE_ID")
						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
						.setParameter("encounterId", patientFamilyHistoryDto.getEncounterId())
						.setParameter("roleId", patientFamilyHistoryDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
				statlessSession.close();
			}
			else
			{*/
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_PATIENT_FAMILY_HISTORY")
//						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
//						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
//						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
//						.setParameter("encounterId", patientFamilyHistoryDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
//				statlessSession.close();
			/*}
			*/
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_FAMILY_HISTORY").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				/*if(patientFamilyHistoryDto.getEncounterId()!=null){
					subQuery.append(" AND (patientFamilyHist.encounterId IS NULL OR patientFamilyHist.encounterId!="+patientFamilyHistoryDto.getEncounterId()+")");
					subQuery.append("  ORDER BY 	patientFamilyHist.familyHistoryId asc");
				}
				if(patientFamilyHistoryDto.getAdmissionId()!=null){
					subQuery.append(" AND (patientFamilyHist.admissionId IS NULL OR patientFamilyHist.admissionId!="+patientFamilyHistoryDto.getAdmissionId()+")");
					subQuery.append("  ORDER BY 	patientFamilyHist.familyHistoryId asc");
				}*/
				query=query.concat(subQuery.toString());
				listPatientFamilyHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
//						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
						.setParameter("created_date", patientFamilyHistoryDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldPatientFamilyHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getCurrentPatientFamilyHistory(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto;
			if(patientFamilyHistoryDto.getRoleId() == 3)
			{
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_CURRENT_PATIENT_FAMILY_HISTORY_BY_ROLE_ID")
//						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
//						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
//						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
//						.setParameter("encounterId", patientFamilyHistoryDto.getEncounterId())
//						.setParameter("roleId", patientFamilyHistoryDto.getRoleId())
//						.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
//				statlessSession.close();
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_FAMILY_HISTORY_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientFamilyHistoryDto.getEncounterId()!=null){
					subQuery.append(" AND patientFamilyHist.encounterId = :encounterId");
					subQuery.append("  ORDER BY 	patientFamilyHist.familyHistoryId asc");
				}
				if(patientFamilyHistoryDto.getAdmissionId()!=null){
					subQuery.append(" AND patientFamilyHist.admissionId = :admissionId");
					subQuery.append("  ORDER BY 	patientFamilyHist.familyHistoryId asc");
				}
				query=query.concat(subQuery.toString());
				Query createQuery =sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
						.setParameter("roleId", patientFamilyHistoryDto.getRoleId());

				if (patientFamilyHistoryDto.getEncounterId() != null) {
					createQuery.setParameter("encounterId", patientFamilyHistoryDto.getEncounterId());
				}
				if (patientFamilyHistoryDto.getAdmissionId() != null) {
					createQuery.setParameter("admissionId", patientFamilyHistoryDto.getAdmissionId());
				}


				listPatientFamilyHistoryDto=createQuery.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
						
				
			}
			else
			{
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_CURRENT_PATIENT_FAMILY_HISTORY")
//						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
//						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
//						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
//						.setParameter("encounterId", patientFamilyHistoryDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
//				statlessSession.close();
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_FAMILY_HISTORY").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientFamilyHistoryDto.getEncounterId()!=null){
					subQuery.append(" AND patientFamilyHist.encounterId = :encounterId");
					subQuery.append("  ORDER BY 	patientFamilyHist.familyHistoryId asc");
				}
				if(patientFamilyHistoryDto.getAdmissionId()!=null){
					subQuery.append(" AND patientFamilyHist.admissionId = :admissionId");
					subQuery.append("  ORDER BY 	patientFamilyHist.familyHistoryId asc");
				}
				query=query.concat(subQuery.toString());
				Query createQuery=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId());

				if (patientFamilyHistoryDto.getEncounterId() != null) {
					createQuery.setParameter("encounterId", patientFamilyHistoryDto.getEncounterId());
				}
				if (patientFamilyHistoryDto.getAdmissionId() != null) {
					createQuery.setParameter("admissionId", patientFamilyHistoryDto.getAdmissionId());
				}

				listPatientFamilyHistoryDto=createQuery.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientFamilyHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updatePatientFamilyHistory(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		try
		{
			for(int i=0;i<patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().size();i++)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				PatientFamilyHistory patientFamilyHistory = findById(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId());
				

				if(patientFamilyHistoryDto.getRoleId() != null 
						&& patientFamilyHistoryDto.getRoleId() == 2 
						&& patientFamilyHistory.getRoleId() != 2 
						&& patientFamilyHistory.getIsReviewedFlag() == 'N'
						)
				{
					ipatientFamilyHistoryService.savePatientFamilyHistory(patientFamilyHistoryDto);
				}
				
				patientFamilyHistory.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
				patientFamilyHistory.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
				patientFamilyHistory.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
				patientFamilyHistory.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
				
				patientFamilyHistory.setRoleId(patientFamilyHistory.getRoleId());
				patientFamilyHistory.setIsReviewedFlag(patientFamilyHistoryDto.getRoleId() == 2 
														&& patientFamilyHistory.getRoleId() == 3
														? YES : patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag());
				patientFamilyHistory.setIsEnterInErrorStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus());
				patientFamilyHistory.setCreatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
				if(patientFamilyHistoryDto.getEncounterId()!=null){
					patientFamilyHistory.setEncounterId(patientFamilyHistoryDto.getEncounterId());
				}
				if(patientFamilyHistoryDto.getAdmissionId()!=null){
					patientFamilyHistory.setAdmissionId(patientFamilyHistoryDto.getAdmissionId());
				}
				patientFamilyHistory.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
				patientFamilyHistory.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
				patientFamilyHistory.setUpdatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientFamilyHistory.setStatus(patientFamilyHistoryDto.getStatus());
				patientFamilyHistory.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
				patientFamilyHistory.setUnitId(patientFamilyHistoryDto.getUnitId());
				patientFamilyHistory.setPatientId(patientFamilyHistoryDto.getPatientId());
				
				PatientFamilyHistory historyId=save(patientFamilyHistory);
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updatePatientFamilyHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response updateSaveNurseReviewedFlag(
			PatientFamilyHistory patientFamilyHistory)
			throws ApplicationException {
		try
		{
			save(patientFamilyHistory);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateSaveNurseReviewedFlag failed", he);
		} catch (Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response updateStatusEnterInError(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().size();i++)
			{
				PatientFamilyHistory patientFamilyHistoryDetails = findById(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getFamilyHistoryId());
				patientFamilyHistoryDetails.setIsEnterInErrorStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsEnterInErrorStatus());
				patientFamilyHistoryDetails.setIsReviewedFlag(patientFamilyHistoryDetails.getIsReviewedFlag() == 'N' && 
						patientFamilyHistoryDetails.getRoleId() == 3 && patientFamilyHistoryDto.getRoleId() == 2 ? YES : patientFamilyHistoryDetails.getIsReviewedFlag());
				ipatientFamilyHistoryService.updateSaveNurseReviewedFlag(patientFamilyHistoryDetails);
				
				if(patientFamilyHistoryDto.getRoleId() != null && patientFamilyHistoryDto.getRoleId() == 2 && patientFamilyHistoryDetails.getRoleId() == 3)
				{

					PatientFamilyHistory patientFamilyHistory = new PatientFamilyHistory();
					patientFamilyHistory.setIsNoFamilyHistoryStatus(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsNoFamilyHistoryStatus());
					patientFamilyHistory.setRemark(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRemark());
					patientFamilyHistory.setDiagnosisId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getDiagnosisId());
					patientFamilyHistory.setRelationId(patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getRelationId());
					
					patientFamilyHistory.setRoleId(patientFamilyHistoryDto.getRoleId());
					
					patientFamilyHistory.setIsReviewedFlag(
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
							patientFamilyHistoryDto.getListOfPatientFamilyHistoryDtos().get(i).getIsReviewedFlag()
						);
					
					patientFamilyHistory.setIsEnterInErrorStatus( NO );
					patientFamilyHistory.setCreatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
					if(patientFamilyHistoryDto.getEncounterId()!=null){
						patientFamilyHistory.setEncounterId(patientFamilyHistoryDto.getEncounterId());
					}
					if(patientFamilyHistoryDto.getAdmissionId()!=null){
						patientFamilyHistory.setAdmissionId(patientFamilyHistoryDto.getAdmissionId());
					}
					patientFamilyHistory.setCreatedBy(patientFamilyHistoryDto.getCreatedBy());
					patientFamilyHistory.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
					patientFamilyHistory.setUpdatedDate(CommonDateUtils.getDate(patientFamilyHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientFamilyHistory.setStatus(patientFamilyHistoryDto.getStatus());
					patientFamilyHistory.setOrganizationId(patientFamilyHistoryDto.getOrganizationId());
					patientFamilyHistory.setUnitId(patientFamilyHistoryDto.getUnitId());
					patientFamilyHistory.setPatientId(patientFamilyHistoryDto.getPatientId());
					
					PatientFamilyHistory historyId=save(patientFamilyHistory);
				}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateStatusEnterInError failed", he);
		} catch (Exception e) {
		logger.error("updateStatusEnterInError failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getCurrentPatientFamilyHistoryForCoversheet(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto;
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_FAMILY_HISTORY_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());
				listPatientFamilyHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
//						.setParameter("unitId", patientFamilyHistoryDto.getUnitId())
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
						.setParameter("created_date", patientFamilyHistoryDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientFamilyHistoryForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getPatientFamilyHistoryForBookView(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto;
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_FAMILY_HISTORY_FOR_BOOK_VIEW").getQueryString();
				/*StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());*/
				listPatientFamilyHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
						.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
						.setParameter("fromDate", patientFamilyHistoryDto.getFromDate())
						.setParameter("toDate", patientFamilyHistoryDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientFamilyHistoryForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response updateStatusRemoveFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try {
			PatientFamilyHistory patientFamilyHistory = findById(patientFamilyHistoryDto.getFamilyHistoryId());
			patientFamilyHistory.setStatus('I');
			patientFamilyHistory.setUpdatedBy(patientFamilyHistoryDto.getUpdatedBy());
			patientFamilyHistory.setUpdatedDate(GlobalCommonDateUtils.getDate(patientFamilyHistoryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(patientFamilyHistory);
	
	return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
	
	} catch (HibernateException he) {
	logger.error("updateStatusRemoveFamilyHistory failed", he);
	} catch (Exception e) {
	logger.error("updateStatusRemoveFamilyHistory failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response getPatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try {
			List<PatientFamilyHistoryDto> listPatientFamilyHistoryDto;

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_FAMILY_HISTORY_FOR_DOCTOR_APP").getQueryString();
			StringBuffer subQuery = new StringBuffer();
			query = query.concat(subQuery.toString());
			listPatientFamilyHistoryDto = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("patientId", patientFamilyHistoryDto.getPatientId())
					.setParameter("organizationId", patientFamilyHistoryDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientFamilyHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getPatientFamilyHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response saveCurrentPatientFamilyHistory(List<PatientFamilyHistoryDto> list, CoversheetTemplateMasterDto resDto) throws ApplicationException {
		try{
			
			for (PatientFamilyHistoryDto dto : list){
				if(dto.getCovTemplateId() == null){
					
					PatientFamilyHistory patientFamilyHistory = new PatientFamilyHistory();
					patientFamilyHistory.setIsNoFamilyHistoryStatus(dto.getIsNoFamilyHistoryStatus());
					patientFamilyHistory.setRemark(dto.getRemark());
					patientFamilyHistory.setDiagnosisId(dto.getDiagnosisId());
					patientFamilyHistory.setRelationId(dto.getRelationId());
					
					patientFamilyHistory.setRoleId(dto.getRoleId());
					patientFamilyHistory.setIsReviewedFlag(
								dto.getIsReviewedFlag() == '\0' ? NO :
								dto.getIsReviewedFlag()
							);
					
					patientFamilyHistory.setIsEnterInErrorStatus( dto.getIsEnterInErrorStatus() );
					patientFamilyHistory.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
//					if(resDto.getEncounterId()!=null){
//						patientFamilyHistory.setEncounterId(resDto.getEncounterId());
//					}
//					if(resDto.getAdmissionId()!=null){
//						patientFamilyHistory.setAdmissionId(resDto.getAdmissionId());
//					}
					
					patientFamilyHistory.setCreatedBy(resDto.getCreatedBy());
					patientFamilyHistory.setUpdatedBy(resDto.getUpdatedBy());
					patientFamilyHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientFamilyHistory.setStatus(resDto.getStatus());
					patientFamilyHistory.setOrganizationId(resDto.getOrganizationId());
					patientFamilyHistory.setUnitId(resDto.getUnitId());
//					patientFamilyHistory.setPatientId(resDto.getPatientId());
					patientFamilyHistory.setCovTemplateId(resDto.getCovTemplateId());

					save(patientFamilyHistory);
					
					}/*else {
						
						PatientFamilyHistory patientFamilyHistory = findById(resDto.getCovTemplateId());
						patientFamilyHistory.setIsNoFamilyHistoryStatus(dto.getIsNoFamilyHistoryStatus());
						patientFamilyHistory.setRemark(dto.getRemark());
						patientFamilyHistory.setDiagnosisId(dto.getDiagnosisId());
						patientFamilyHistory.setRelationId(dto.getRelationId());
						
						patientFamilyHistory.setRoleId(dto.getRoleId());
						patientFamilyHistory.setIsReviewedFlag(
									dto.getIsReviewedFlag() == '\0' ? NO :
									dto.getIsReviewedFlag()
								);
						
						patientFamilyHistory.setIsEnterInErrorStatus( dto.getIsEnterInErrorStatus() );
						patientFamilyHistory.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
//						if(dto.getEncounterId()!=null){
//							patientFamilyHistory.setEncounterId(resDto.getEncounterId());
//						}
//						if(dto.getAdmissionId()!=null){
//							patientFamilyHistory.setAdmissionId(resDto.getAdmissionId());
//						}
						
						patientFamilyHistory.setCreatedBy(resDto.getCreatedBy());
						patientFamilyHistory.setUpdatedBy(resDto.getUpdatedBy());
						patientFamilyHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
						patientFamilyHistory.setStatus(resDto.getStatus());
						patientFamilyHistory.setOrganizationId(resDto.getOrganizationId());
						patientFamilyHistory.setUnitId(resDto.getUnitId());
//						patientFamilyHistory.setPatientId(resDto.getPatientId());
						
						PatientFamilyHistory historyId=update(patientFamilyHistory);
						
						}*/
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}



	@Override
	public Response updateCovTempCurrentPatientFamilyHistory(List<PatientFamilyHistoryDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
	try{
			
			for (PatientFamilyHistoryDto dto : list){
				if(dto.getCovTemplateId() != null){
					
						
						PatientFamilyHistory patientFamilyHistory = findById(coversheetTemplateMasterDto.getCovTemplateId());
						patientFamilyHistory.setIsNoFamilyHistoryStatus(dto.getIsNoFamilyHistoryStatus());
						patientFamilyHistory.setRemark(dto.getRemark());
						patientFamilyHistory.setDiagnosisId(dto.getDiagnosisId());
						patientFamilyHistory.setRelationId(dto.getRelationId());
						
						patientFamilyHistory.setRoleId(dto.getRoleId());
						patientFamilyHistory.setIsReviewedFlag(
									dto.getIsReviewedFlag() == '\0' ? NO :
									dto.getIsReviewedFlag()
								);
						
						patientFamilyHistory.setIsEnterInErrorStatus( dto.getIsEnterInErrorStatus() );
						patientFamilyHistory.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
//						if(dto.getEncounterId()!=null){
//							patientFamilyHistory.setEncounterId(resDto.getEncounterId());
//						}
//						if(dto.getAdmissionId()!=null){
//							patientFamilyHistory.setAdmissionId(resDto.getAdmissionId());
//						}
						
						patientFamilyHistory.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						patientFamilyHistory.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						patientFamilyHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
						patientFamilyHistory.setStatus(coversheetTemplateMasterDto.getStatus());
						patientFamilyHistory.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						patientFamilyHistory.setUnitId(coversheetTemplateMasterDto.getUnitId());
//						patientFamilyHistory.setPatientId(resDto.getPatientId());
						
						PatientFamilyHistory historyId=update(patientFamilyHistory);
						
						}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}




}
