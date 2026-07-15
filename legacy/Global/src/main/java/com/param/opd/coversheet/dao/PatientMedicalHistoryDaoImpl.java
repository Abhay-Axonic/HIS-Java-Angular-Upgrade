package com.param.opd.coversheet.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.opd.coversheet.dto.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
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
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.opd.coversheet.model.PatientMedicalHistory;
import com.param.opd.coversheet.service.IDiagnosisMapperService;
import com.param.opd.coversheet.service.IDiagnosisMasterService;
import com.param.opd.coversheet.service.IPatientMedicalHistoryService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.springframework.web.client.RestTemplate;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientMedicalHistoryDaoImpl extends GenericDao<PatientMedicalHistory, Integer> implements IPatientMedicalHistoryDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientMedicalHistoryDaoImpl.class);


	public PatientMedicalHistoryDaoImpl() {
		super(PatientMedicalHistory.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IPatientMedicalHistoryService iPatientMedicalHistoryService;
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
	public Response savePatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException {
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

			List<PatientMedicalHistoryDto> historyDtoList = new ArrayList<>();

			if(patientMedicalHistoryDto.getEncounterId() != null && patientMedicalHistoryDto.getEncounterId()>0) {
				isSyncScheduling = (Boolean) sessionFactory.getCurrentSession()
						.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
						.uniqueResult();

				channelId = (Integer) sessionFactory.getCurrentSession()
						.createSQLQuery("  select org.channel_id as \"channelId\" from public.m_organization_master org where  org.organization_id=:organizationId and org.status='A' ")
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
						.uniqueResult();
				uuid = (String) sessionFactory.getCurrentSession()
						.createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "
								+ "	where patReg.patient_id=:patientId and patReg.organization_id=:orgId ")
						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
						.setParameter("orgId", patientMedicalHistoryDto.getOrganizationId())
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

			for (int i = 0; i < patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().size(); i++) {
				PatientMedicalHistoryDto historyDto = new PatientMedicalHistoryDto();
				if (patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsDiagnosisName() == 'N') {
					if (patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
							.getPatientMedicalHistoryId() != null
							&& patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
									.getPatientMedicalHistoryId() > 0) {
						PatientMedicalHistory patientMedicalHistory = findById(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getPatientMedicalHistoryId());
						patientMedicalHistory.setIsReviewedFlag(YES);
						patientMedicalHistory.setIsNoSignificantStatus(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
						patientMedicalHistory.setRemark(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
						patientMedicalHistory.setDiagnosisId(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId());
						patientMedicalHistory.setSince(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
						patientMedicalHistory.setDuration(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());

						patientMedicalHistory.setRoleId(patientMedicalHistoryDto.getRoleId());
						patientMedicalHistory.setIsReviewedFlag(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO
										: patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
												.getIsReviewedFlag());

						patientMedicalHistory.setIsEnterInError(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
						patientMedicalHistory.setCreated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getCreated_date(), "dd-MM-yyyy"));

						if (patientMedicalHistoryDto.getEncounterId() != null) {
							patientMedicalHistory.setEncounterId(patientMedicalHistoryDto.getEncounterId());
						}
						if (patientMedicalHistoryDto.getAdmissionId() != null) {
							patientMedicalHistory.setAdmissionId(patientMedicalHistoryDto.getAdmissionId());
						}
						patientMedicalHistory.setCreated_by(patientMedicalHistoryDto.getCreated_by());
						patientMedicalHistory.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
						patientMedicalHistory.setUpdated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getUpdated_date(), "dd-MM-yyyy"));
						patientMedicalHistory.setStatus('I');
						patientMedicalHistory.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
						patientMedicalHistory.setUnitId(patientMedicalHistoryDto.getUnitId());
						patientMedicalHistory.setPatientId(patientMedicalHistoryDto.getPatientId());
						iPatientMedicalHistoryService.updateSaveNurseReviewedFlag(patientMedicalHistory);
					} else {
						PatientMedicalHistory patientMedicalHistory = new PatientMedicalHistory();
						patientMedicalHistory.setIsNoSignificantStatus(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
						patientMedicalHistory.setRemark(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
						patientMedicalHistory.setDiagnosisId(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId());
						patientMedicalHistory.setSince(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
						patientMedicalHistory.setDuration(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());

						patientMedicalHistory.setRoleId(patientMedicalHistoryDto.getRoleId());
						patientMedicalHistory.setIsReviewedFlag(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO
										: patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
												.getIsReviewedFlag());

						patientMedicalHistory.setIsEnterInError(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
						patientMedicalHistory.setCreated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getCreated_date(), "dd-MM-yyyy"));

						if (patientMedicalHistoryDto.getEncounterId() != null) {
							patientMedicalHistory.setEncounterId(patientMedicalHistoryDto.getEncounterId());
						}
						if (patientMedicalHistoryDto.getAdmissionId() != null) {
							patientMedicalHistory.setAdmissionId(patientMedicalHistoryDto.getAdmissionId());
						}
						patientMedicalHistory.setCreated_by(patientMedicalHistoryDto.getCreated_by());
						patientMedicalHistory.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
						patientMedicalHistory.setUpdated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getUpdated_date(), "dd-MM-yyyy"));
						patientMedicalHistory.setStatus(patientMedicalHistoryDto.getStatus());
						patientMedicalHistory.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
						patientMedicalHistory.setUnitId(patientMedicalHistoryDto.getUnitId());
						patientMedicalHistory.setPatientId(patientMedicalHistoryDto.getPatientId());

						PatientMedicalHistory historyId = save(patientMedicalHistory);

						if(historyId.getPatientMedicalHistoryId() != null && isSyncScheduling){

							PatientMedicalHistoryDto tempDto = new PatientMedicalHistoryDto();

							tempDto.setHospitalId(patientMedicalHistoryDto.getOrganizationId());
							tempDto.setUuid(uuid != null ? uuid : "0");
							tempDto.setIsNoSignificantStatus(patientMedicalHistoryDto
									.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
							tempDto.setRemark(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
							tempDto.setDiagnosisId(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId() != null ? patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId(): patientMedicalHistory.getDiagnosisId());
							tempDto.setSince(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
							tempDto.setDuration(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());
							tempDto.setIsReviewedFlag(patientMedicalHistoryDto
									.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO
									: patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
									.getIsReviewedFlag());

							tempDto.setIsEnterInError(patientMedicalHistoryDto
									.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
							tempDto.setCreated_by(patientMedicalHistoryDto.getCreated_by());
							tempDto.setCreated_date(patientMedicalHistoryDto.getCreated_date());
							tempDto.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
							tempDto.setUpdated_date(patientMedicalHistoryDto.getUpdated_date());
							tempDto.setStatus(patientMedicalHistoryDto.getStatus());
							tempDto.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
							tempDto.setUnitId(patientMedicalHistoryDto.getUnitId());
							tempDto.setPatientId(patientMedicalHistoryDto.getPatientId());
							tempDto.setEncounterId(patientMedicalHistoryDto.getEncounterId());
							tempDto.setDiagnosisDesc(patientMedicalHistoryDto.getDiagnosisDesc() != null ?
									patientMedicalHistoryDto.getDiagnosisDesc() : patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisNameText());
							tempDto.setApplicationId(2);

							historyDtoList.add(tempDto);
						}
					}
				} else {

					// Diagnosis Master save
					DiagnosisMasterDto diagnosisMasterDto = new DiagnosisMasterDto();
					diagnosisMasterDto.setDiagnosisDesc(
							patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setStatus('A');
					diagnosisMasterDto.setCode(
							patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisCode());
					diagnosisMasterDto.setDiagnosisName(
							patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setIsNotifiable('N');
					diagnosisMasterDto.setShortDesc(
							patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisNameText());
					diagnosisMasterDto.setMarathiDiagnosisDesc(null);
					diagnosisMasterDto.setCreated_by(patientMedicalHistoryDto.getCreated_by());
					diagnosisMasterDto.setCreated_date(patientMedicalHistoryDto.getCreated_date());
					diagnosisMasterDto.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
					diagnosisMasterDto.setUpdated_date(patientMedicalHistoryDto.getUpdated_date());
					diagnosisMasterDto.setUnitId(patientMedicalHistoryDto.getUnitId());
					diagnosisMasterDto.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
					diagnosisMasterDto.setIsComorbidity('N');
					Response res = iDiagnosisMasterService.saveDiagnosisMaster(diagnosisMasterDto);
					DiagnosisMasterDto dto = (DiagnosisMasterDto) res.getObject();
					patientMedicalHistoryDto.setDiagnosisId(dto.getDiagnosisId());
					patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).setDiagnosisId(dto.getDiagnosisId());
					// Diagnosis Gender mapper save
					DiagnosisMapperDto diagnosisMapperDto = new DiagnosisMapperDto();
					diagnosisMapperDto.setDiagnosisId(patientMedicalHistoryDto.getDiagnosisId());
					diagnosisMapperDto.setIsNotifiable('N');
					diagnosisMapperDto.setIsCommunicable('N');
					diagnosisMapperDto.setIsNonCommunicable('N');
					diagnosisMapperDto.setIsMedicoLegal('N');
					diagnosisMapperDto.setIsDeleted('N');
					diagnosisMapperDto.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
					diagnosisMapperDto.setUnitId(patientMedicalHistoryDto.getUnitId());
					diagnosisMapperDto.setCreatedBy(patientMedicalHistoryDto.getCreated_by());
					diagnosisMapperDto.setUpdatedBy(patientMedicalHistoryDto.getUpdated_by());
					diagnosisMapperDto.setCreatedDate(patientMedicalHistoryDto.getCreated_date());
					diagnosisMapperDto.setUpdatedDate(patientMedicalHistoryDto.getUpdated_date());
					diagnosisMapperDto.setStatus('A');

					List<DiagnosisMapperDto> listOfGender = new ArrayList<DiagnosisMapperDto>();
					for (int j = 1; j <= 3; j++) {
						diagnosisMapperDto.setId(j);
						listOfGender.add(diagnosisMapperDto);
					}

					diagnosisMapperDto.setListOfGendersDto(listOfGender);
					Response res2 = iDiagnosisMapperService.saveDiagnosisMapper(diagnosisMapperDto);

					// Patient Medication History save

					if (patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
							.getPatientMedicalHistoryId() != null
							&& patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
									.getPatientMedicalHistoryId() > 0) {
						PatientMedicalHistory patientMedicalHistory = findById(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getPatientMedicalHistoryId());
						patientMedicalHistory.setIsReviewedFlag(YES);
						patientMedicalHistory.setIsNoSignificantStatus(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
						patientMedicalHistory.setRemark(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
						patientMedicalHistory.setDiagnosisId(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId());
						patientMedicalHistory.setSince(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
						patientMedicalHistory.setDuration(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());

						patientMedicalHistory.setRoleId(patientMedicalHistoryDto.getRoleId());
						patientMedicalHistory.setIsReviewedFlag(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO
										: patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
												.getIsReviewedFlag());

						patientMedicalHistory.setIsEnterInError(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
						patientMedicalHistory.setCreated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getCreated_date(), "dd-MM-yyyy"));

						if (patientMedicalHistoryDto.getEncounterId() != null) {
							patientMedicalHistory.setEncounterId(patientMedicalHistoryDto.getEncounterId());
						}
						if (patientMedicalHistoryDto.getAdmissionId() != null) {
							patientMedicalHistory.setAdmissionId(patientMedicalHistoryDto.getAdmissionId());
						}
						patientMedicalHistory.setCreated_by(patientMedicalHistoryDto.getCreated_by());
						patientMedicalHistory.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
						patientMedicalHistory.setUpdated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getUpdated_date(), "dd-MM-yyyy"));
						patientMedicalHistory.setStatus('I');
						patientMedicalHistory.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
						patientMedicalHistory.setUnitId(patientMedicalHistoryDto.getUnitId());
						patientMedicalHistory.setPatientId(patientMedicalHistoryDto.getPatientId());
						iPatientMedicalHistoryService.updateSaveNurseReviewedFlag(patientMedicalHistory);
					} else {
						PatientMedicalHistory patientMedicalHistory = new PatientMedicalHistory();
						patientMedicalHistory.setIsNoSignificantStatus(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
						patientMedicalHistory.setRemark(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
						patientMedicalHistory.setDiagnosisId(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId());
						patientMedicalHistory.setSince(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
						patientMedicalHistory.setDuration(
								patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());

						patientMedicalHistory.setRoleId(patientMedicalHistoryDto.getRoleId());
						patientMedicalHistory.setIsReviewedFlag(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO
										: patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
												.getIsReviewedFlag());

						patientMedicalHistory.setIsEnterInError(patientMedicalHistoryDto
								.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
						patientMedicalHistory.setCreated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getCreated_date(), "dd-MM-yyyy"));

						if (patientMedicalHistoryDto.getEncounterId() != null) {
							patientMedicalHistory.setEncounterId(patientMedicalHistoryDto.getEncounterId());
						}
						if (patientMedicalHistoryDto.getAdmissionId() != null) {
							patientMedicalHistory.setAdmissionId(patientMedicalHistoryDto.getAdmissionId());
						}
						patientMedicalHistory.setCreated_by(patientMedicalHistoryDto.getCreated_by());
						patientMedicalHistory.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
						patientMedicalHistory.setUpdated_date(
								CommonDateUtils.getDate(patientMedicalHistoryDto.getUpdated_date(), "dd-MM-yyyy"));
						patientMedicalHistory.setStatus(patientMedicalHistoryDto.getStatus());
						patientMedicalHistory.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
						patientMedicalHistory.setUnitId(patientMedicalHistoryDto.getUnitId());
						patientMedicalHistory.setPatientId(patientMedicalHistoryDto.getPatientId());
						PatientMedicalHistory historyId = save(patientMedicalHistory);


						if(historyId.getPatientMedicalHistoryId() != null && isSyncScheduling){//------------------------------------------------
							PatientMedicalHistoryDto tempDto = new PatientMedicalHistoryDto();

							tempDto.setHospitalId(patientMedicalHistoryDto.getOrganizationId());
							tempDto.setUuid(uuid != null ? uuid : "0");
							tempDto.setIsNoSignificantStatus(patientMedicalHistoryDto
									.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
							tempDto.setRemark(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
							tempDto.setDiagnosisId(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId() != null ? patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId(): patientMedicalHistory.getDiagnosisId());
							tempDto.setSince(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
							tempDto.setDuration(
									patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());
							tempDto.setIsReviewedFlag(patientMedicalHistoryDto
									.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO
									: patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i)
									.getIsReviewedFlag());

							tempDto.setIsEnterInError(patientMedicalHistoryDto
									.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
							tempDto.setCreated_by(patientMedicalHistoryDto.getCreated_by());
							tempDto.setCreated_date(patientMedicalHistoryDto.getCreated_date());
							tempDto.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
							tempDto.setUpdated_date(patientMedicalHistoryDto.getUpdated_date());
							tempDto.setStatus(patientMedicalHistoryDto.getStatus());
							tempDto.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
							tempDto.setUnitId(patientMedicalHistoryDto.getUnitId());
							tempDto.setPatientId(patientMedicalHistoryDto.getPatientId());
							tempDto.setEncounterId(patientMedicalHistoryDto.getEncounterId());
							tempDto.setDiagnosisDesc(patientMedicalHistoryDto.getDiagnosisDesc() != null ?
									patientMedicalHistoryDto.getDiagnosisDesc() : patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisNameText());
							tempDto.setApplicationId(2);
							historyDtoList.add(tempDto);
						}
					}
				}

			}

			if(!historyDtoList.isEmpty() && isSyncScheduling) {
				List<PatientMedicalHistoryDto> diagnosisIdAndNameList = new ArrayList<>();
				List<Integer> diagnosisIds = historyDtoList.stream().map(PatientMedicalHistoryDto::getDiagnosisId).filter(Objects::nonNull).collect(Collectors.toList());

				if (diagnosisIds != null && !diagnosisIds.isEmpty()) {
					String sqlQuery = "  select mst.diagnosis_id as \"diagnosisId\", mst.diagnosis_name as \"diagnosisDesc\" from  emr.m_diagnosis_master mst "
							+ "	where mst.status='A' and mst.diagnosis_id in (:diagnosisIds)";

					StringBuffer sb = new StringBuffer(sqlQuery);
					diagnosisIdAndNameList = getSession().createSQLQuery(sb.toString())
							.setParameterList("diagnosisIds", diagnosisIds)
							.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
				}

				if(diagnosisIdAndNameList != null && !diagnosisIdAndNameList.isEmpty() && diagnosisIdAndNameList.size() == diagnosisIds.size()){
					for (PatientMedicalHistoryDto dto : historyDtoList) {
						PatientMedicalHistoryDto objectDto = diagnosisIdAndNameList.stream()
								.filter(order -> dto.getDiagnosisId().equals(order.getDiagnosisId()))
								.findAny()
								.orElse(null);
						if(objectDto!=null){
							dto.setDiagnosisDesc(objectDto.getDiagnosisDesc());
						}
					}
				}

				PatientMedicalHistoryDto reqHistoryDto = new PatientMedicalHistoryDto();
				reqHistoryDto.setPatientMedicalHistoryDtoList(historyDtoList);

				try {
					urlMyLife = URI.create(MY_LIFE_URI + "/savePatientMedicalHistoryFromHis");
					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + token);
					headers.set("userName", "ClinivantageAdminUser");
					headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
					HttpEntity<PatientMedicalHistoryDto> request = new HttpEntity<>(reqHistoryDto, headers);
						String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
					commonResponseMyLife = gson.fromJson(stringResponse, Response.class);

				}catch(Exception e){
					logger.error("savePatientMedicalHistory MyLife sync failed", e);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("savePatientMedicalHistory failed", he);
		} catch (Exception e) {
		logger.error("savePatientMedicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getOldPatientMedicalHistory(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientMedicalHistoryDto> listPatientFamilyHistoryDto ;
			/*if(patientMedicalHistoryDto.getRoleId() == 3)
			{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_PATIENT_MEDICAL_HISTORY_BY_ROLE_ID")
						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
						.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
						.setParameter("encounterId", patientMedicalHistoryDto.getEncounterId())
						.setParameter("roleId", patientMedicalHistoryDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
				statlessSession.close();
			}
			else
			{*/
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_PATIENT_MEDICAL_HISTORY")
//						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
//						.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
//						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
//						.setParameter("encounterId", patientMedicalHistoryDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
//				statlessSession.close();
			/*}
			*/
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_MEDICAL_HISTORY").getQueryString();
			StringBuffer subQuery=new StringBuffer();
//			if(patientMedicalHistoryDto.getEncounterId()!=null){
//				subQuery.append(" AND (patientMedicalHist.encounterId IS NULL OR patientMedicalHist.encounterId!="+patientMedicalHistoryDto.getEncounterId()+")");
//				subQuery.append("  ORDER BY 	patientMedicalHist.patientMedicalHistoryId asc");
//			}
//			if(patientMedicalHistoryDto.getAdmissionId()!=null){
//				subQuery.append(" AND (patientMedicalHist.admissionId IS NULL OR patientMedicalHist.admissionId!="+patientMedicalHistoryDto.getAdmissionId()+")");
//				subQuery.append("  ORDER BY 	patientMedicalHist.patientMedicalHistoryId asc");
//			}
			query=query.concat(subQuery.toString());
			listPatientFamilyHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
//					.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
					.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
					.setParameter("created_date", patientMedicalHistoryDto.getCreated_date())
					.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldPatientMedicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response getCurrentPatientMedicalHistory(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientMedicalHistoryDto> listPatientFamilyHistoryDto ;
			if(patientMedicalHistoryDto.getRoleId() == 3)
			{
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_CURRENT_PATIENT_MEDICAL_HISTORY_BY_ROLE_ID")
//						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
//						.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
//						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
//						.setParameter("encounterId", patientMedicalHistoryDto.getEncounterId())
//						.setParameter("roleId", patientMedicalHistoryDto.getRoleId())
//						.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
//				statlessSession.close();
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_MEDICAL_HISTORY_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientMedicalHistoryDto.getEncounterId()!=null){
					subQuery.append(" AND patientMedicalHist.encounterId = :encounterId");
					subQuery.append("  ORDER BY 	patientMedicalHist.patientMedicalHistoryId asc");
				}
				if(patientMedicalHistoryDto.getAdmissionId()!=null){
					subQuery.append(" AND patientMedicalHist.admissionId = :admissionId");
					subQuery.append("  ORDER BY patientMedicalHist.patientMedicalHistoryId asc");
				}
				query=query.concat(subQuery.toString());
			Query createQuery =sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
						.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
						.setParameter("roleId", patientMedicalHistoryDto.getRoleId());


				if (patientMedicalHistoryDto.getEncounterId() != null) {
					createQuery.setParameter("encounterId", patientMedicalHistoryDto.getEncounterId());
				}
				if (patientMedicalHistoryDto.getAdmissionId() != null) {
					createQuery.setParameter("admissionId", patientMedicalHistoryDto.getAdmissionId());
				}

				listPatientFamilyHistoryDto=createQuery.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
			}
			else
			{
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientFamilyHistoryDto = statlessSession.getNamedQuery("GET_CURRENT_PATIENT_MEDICAL_HISTORY")
//						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
//						.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
//						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
//						.setParameter("encounterId", patientMedicalHistoryDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
//				statlessSession.close();
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_MEDICAL_HISTORY").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientMedicalHistoryDto.getEncounterId()!=null){
					subQuery.append(" AND patientMedicalHist.encounterId = :encounterId");
					subQuery.append("  ORDER BY patientMedicalHist.patientMedicalHistoryId asc");
				}
				if(patientMedicalHistoryDto.getAdmissionId()!=null){
					subQuery.append(" AND patientMedicalHist.admissionId = :admissionId");
					subQuery.append("  ORDER BY 	patientMedicalHist.patientMedicalHistoryId asc");
				}
				query=query.concat(subQuery.toString());
			Query createQuery=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
						.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId());

				if (patientMedicalHistoryDto.getEncounterId() != null) {
					createQuery.setParameter("encounterId", patientMedicalHistoryDto.getEncounterId());
				}
				if (patientMedicalHistoryDto.getAdmissionId() != null) {
					createQuery.setParameter("admissionId", patientMedicalHistoryDto.getAdmissionId());
				}


				listPatientFamilyHistoryDto=createQuery.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientMedicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePatientMedicalHistory(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().size();i++)
			{
				
				PatientMedicalHistory patientMedicalHistory = findById(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getPatientMedicalHistoryId());
				
				/*if(patientMedicalHistoryDto.getRoleId() != null 
						&& patientMedicalHistoryDto.getRoleId() == 2 
						&& patientMedicalHistory.getRoleId() != 2 
						&& patientMedicalHistory.getIsReviewedFlag() == 'N'
						)
				{
//					iPatientMedicalHistoryService.updatePatientMedicalHistory(patientMedicalHistoryDto);
					iPatientMedicalHistoryService.savePatientMedicalHistory(patientMedicalHistoryDto);

				}*/
				
				patientMedicalHistory.setIsNoSignificantStatus(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
				patientMedicalHistory.setRemark(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
				patientMedicalHistory.setDiagnosisId(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId());
				patientMedicalHistory.setSince(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
				patientMedicalHistory.setDuration(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());
				
				patientMedicalHistory.setRoleId(patientMedicalHistoryDto.getRoleId());
				patientMedicalHistory.setIsReviewedFlag(patientMedicalHistoryDto.getRoleId() == 2 
						&& patientMedicalHistory.getRoleId() == 3
						? YES  :
					patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag()
						);
				
				patientMedicalHistory.setIsEnterInError(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
				patientMedicalHistory.setCreated_date(CommonDateUtils.getDate(patientMedicalHistoryDto.getCreated_date(), "dd-MM-yyyy"));
				if(patientMedicalHistoryDto.getEncounterId()!=null){
					patientMedicalHistory.setEncounterId(patientMedicalHistoryDto.getEncounterId());
				}
				if(patientMedicalHistoryDto.getAdmissionId()!=null){
					patientMedicalHistory.setAdmissionId(patientMedicalHistoryDto.getAdmissionId());
				}
				patientMedicalHistory.setCreated_by(patientMedicalHistoryDto.getCreated_by());
				patientMedicalHistory.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
				patientMedicalHistory.setUpdated_date(CommonDateUtils.getDate(patientMedicalHistoryDto.getUpdated_date(), "dd-MM-yyyy"));
				patientMedicalHistory.setStatus(patientMedicalHistoryDto.getStatus());
				patientMedicalHistory.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
				patientMedicalHistory.setUnitId(patientMedicalHistoryDto.getUnitId());
				patientMedicalHistory.setPatientId(patientMedicalHistoryDto.getPatientId());
				
				PatientMedicalHistory historyId=save(patientMedicalHistory);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updatePatientMedicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(
			PatientMedicalHistory patientMedicalHistory)
			throws ApplicationException {
		try
		{
			save(patientMedicalHistory);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusEnterInError(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().size();i++)
			{
				
				PatientMedicalHistory patientMedicalHistoryDetails = findById(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getPatientMedicalHistoryId());
				patientMedicalHistoryDetails.setIsEnterInError(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsEnterInError());
				patientMedicalHistoryDetails.setIsReviewedFlag(patientMedicalHistoryDetails.getIsReviewedFlag() == 'N' && 
						patientMedicalHistoryDetails.getRoleId() == 3 &&
						patientMedicalHistoryDto.getRoleId() == 2 
						? YES : patientMedicalHistoryDetails.getIsReviewedFlag());
				iPatientMedicalHistoryService.updateSaveNurseReviewedFlag(patientMedicalHistoryDetails);
				
				if(patientMedicalHistoryDto.getRoleId() != null && patientMedicalHistoryDto.getRoleId() == 2 && patientMedicalHistoryDetails.getRoleId() == 3)
				{
					PatientMedicalHistory patientMedicalHistory = new PatientMedicalHistory();
					patientMedicalHistory.setIsNoSignificantStatus(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsNoSignificantStatus());
					patientMedicalHistory.setRemark(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getRemark());
					patientMedicalHistory.setDiagnosisId(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDiagnosisId());
					patientMedicalHistory.setSince(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getSince());
					patientMedicalHistory.setDuration(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getDuration());
					
					patientMedicalHistory.setRoleId(patientMedicalHistoryDto.getRoleId());
					patientMedicalHistory.setIsReviewedFlag(patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag() == '\0'? NO :
						patientMedicalHistoryDto.getListOfPatientMedicalHistoryDto().get(i).getIsReviewedFlag()
							);
					
					patientMedicalHistory.setIsEnterInError(NO);
					patientMedicalHistory.setCreated_date(CommonDateUtils.getDate(patientMedicalHistoryDto.getCreated_date(), "dd-MM-yyyy"));
					if(patientMedicalHistoryDto.getEncounterId()!=null){
						patientMedicalHistory.setEncounterId(patientMedicalHistoryDto.getEncounterId());
					}
					if(patientMedicalHistoryDto.getAdmissionId()!=null){
						patientMedicalHistory.setAdmissionId(patientMedicalHistoryDto.getAdmissionId());
					}
					patientMedicalHistory.setCreated_by(patientMedicalHistoryDto.getCreated_by());
					patientMedicalHistory.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
					patientMedicalHistory.setUpdated_date(CommonDateUtils.getDate(patientMedicalHistoryDto.getUpdated_date(), "dd-MM-yyyy"));
					patientMedicalHistory.setStatus(patientMedicalHistoryDto.getStatus());
					patientMedicalHistory.setOrganizationId(patientMedicalHistoryDto.getOrganizationId());
					patientMedicalHistory.setUnitId(patientMedicalHistoryDto.getUnitId());
					patientMedicalHistory.setPatientId(patientMedicalHistoryDto.getPatientId());
					
					PatientMedicalHistory historyId=save(patientMedicalHistory);
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
	public Response getCurrentPatientMedicalHistoryDetailsForCoversheet(
			PatientMedicalHistoryDto patientMedicalHistoryDto) throws ApplicationException {
		try
		{
			List<PatientMedicalHistoryDto> listPatientFamilyHistoryDto ;
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_MEDICAL_HISTORY_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());
				listPatientFamilyHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
//						.setParameter("unitId", patientMedicalHistoryDto.getUnitId())
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
						.setParameter("created_date", patientMedicalHistoryDto.getCreated_date())
						.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientMedicalHistoryDetailsForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getPatientMedicalHistoryForBookView(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientMedicalHistoryDto> listPatientFamilyHistoryDto ;
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_MEDICAL_HISTORY_FOR_BOOK_VIEW").getQueryString();
				/*StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());*/
				listPatientFamilyHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
						.setParameter("fromDate", patientMedicalHistoryDto.getFromDate())
						.setParameter("toDate", patientMedicalHistoryDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientFamilyHistoryDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientMedicalHistoryForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updateStatusRemoveMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try {
			PatientMedicalHistory patientMedicalHistory = findById(patientMedicalHistoryDto.getPatientMedicalHistoryId());
			patientMedicalHistory.setStatus('I');
			patientMedicalHistory.setUpdated_by(patientMedicalHistoryDto.getUpdated_by());
			patientMedicalHistory.setUpdated_date(CommonDateUtils.getDate(patientMedicalHistoryDto.getUpdated_date(), "dd-MM-yyyy HH:mm:ss"));
			 
			update(patientMedicalHistory);
	
	return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
	
	} catch (HibernateException he) {
	logger.error("updateStatusRemoveMedicalHistory failed", he);
	} catch (Exception e) {
	logger.error("updateStatusRemoveMedicalHistory failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getPatientDataForBookView(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try
		{
				
			String query ="select  "
					 + " 	tem.encounter_id as \"visitId\",  "
					 + " 	to_char(tem.created_date, 'YYYY-MM-DD HH:24MI') as \"date\",  "
					 + " 	1 as \"typeId\",   "
					 + " 	tem.unit_id as \"unitId\",  "
					 + " 	tem.organization_id as \"organizationId\",  "
					 + " 	0 as \"dischargeSummeryId\",  "
					 + " 	null as \"documentPath\"  "
					 + " from  "
					 + " 	opd.t_encounter_master tem  "
					 + " where  "
					 + " 	tem.patient_id = "+patientMedicalHistoryDto.getPatientId()+" "
					 + " 	union  "
					 + " 	select  "
					 + " 	ta.admission_id as \"visitId\",  "
					 + " 	to_char(ta.created_date, 'YYYY-MM-DD HH:24MI') as \"date\",  "
					 + " 	2 as \"typeId\",   "
					 + " 	ta.unit_id as \"unitId\",  "
					 + " 	ta.organization_id as \"organizationId\",  "
					 + " 	0 as \"dischargeSummeryId\",  "
					 + " 	null as \"documentPath\"  "
					 + " 	from  "
					 + " 	adt.t_admission ta "
					 + " where  "
					 + " 	ta.patient_id = "+patientMedicalHistoryDto.getPatientId()+" "
					 + " 	union  "
					 + " 	select  "
					 + " 	0 as \"visitId\",  "
					 + " 	to_char(dd.created_date, 'YYYY-MM-DD HH:24MI') as \"date\",  "
					 + " 	4 as \"typeId\",   "
					 + " 	dd.unit_id as \"unitId\",  "
					 + " 	dd.organisation_id as \"organizationId\",  "
					 + " 	0 as \"dischargeSummeryId\",  "
					 + " 	dd.document_path as \"documentPath\"  "
					 + " 	from  "
					 + " 	opd.t_encounter_doc_details dd "
					 + " where  "
					 + " 	dd.patient_id = "+patientMedicalHistoryDto.getPatientId()+" "
					 + " 	union  "
					 + " 	select  "
					 + " 	mds.admission_id as \"visitId\",  "
					 + " 	to_char(mds.created_date, 'YYYY-MM-DD HH:24MI') as \"date\",  "
					 + " 	3 as \"typeId\",  "
					 + " 	mds.unit_id as \"unitId\",  "
					 + " 	mds.organization_id as \"organizationId\",  "
					 + " 	mds.discharge_summary_id as \"dischargeSummeryId\",  "
					 + " 	null as \"documentPath\"  "
					 + " 	from  "
					 + " 	adt.m_discharge_summary mds "
					 + " where  "
					 + " 	mds.status='A' and mds.patient_id =  "+patientMedicalHistoryDto.getPatientId()+" order by 2 desc ";

					
					List<PatientMedicalHistoryDto> listPatientFamilyHistoryDto = sessionFactory.getCurrentSession().createSQLQuery(query)
							.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
					
					return new Response(SUCCESS, null, null, listPatientFamilyHistoryDto, null);
					
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientDataForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getPatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {

		try
		{
			String dDate= GlobalCommonDateUtils.getAvalDate(TimeZoneComponent.getDateByzone(patientMedicalHistoryDto.getUnitId()), "yyyy-MM-dd");
			String query = "select " + 
					"	dm.patient_id as \"patientId\"," + 
					"	dm.diagnosis_id as \"diagnosisId\"," + 
					"	dm.type as \"type\", " + 
					"	to_char(dm.created_date,'DD/MM/YYYY') as \"created_date\", " + 
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
					"	inner join emr.m_diagnosis_master diagnosisMaster on " + 
					"	dm.diagnosis_id = diagnosisMaster.diagnosis_id " + 
					"	where " + 
					"	dm.patient_id ="+patientMedicalHistoryDto.getPatientId()+" " +
					"	and dm.organization_id ="+patientMedicalHistoryDto.getOrganizationId()+" " +
					"	and dm.is_resolved = 'Y' " + 
					"	and dm.is_deleted = 'N' " + 
					"	and dm.is_diagnosis_cancel = 'N'";
			 
			   List<DiagnosisPatientAppointmentMapperDto> resolveDiagnosisList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(DiagnosisPatientAppointmentMapperDto.class)).list();
			
				String query1=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_MEDICAL_HISTORY_FOR_DOCTOR_APP").getQueryString();
			
				List<PatientMedicalHistoryDto> medicalHistoryList=sessionFactory.getCurrentSession().createQuery(query1)
						.setParameter("patientId", patientMedicalHistoryDto.getPatientId())
						.setParameter("organizationId", patientMedicalHistoryDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientMedicalHistoryDto.class)).list();
				MedicalHistoryDto dto=new MedicalHistoryDto();
				if(medicalHistoryList!=null && !medicalHistoryList.isEmpty())
					dto.setMedicalHistoryList(medicalHistoryList);
				if(resolveDiagnosisList!=null && !resolveDiagnosisList.isEmpty())
					dto.setResolveDiagnosisList(resolveDiagnosisList);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientMedicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}


	@Override
	public Response saveMededicalHistoryCurrent(List<PatientMedicalHistoryDto> list, CoversheetTemplateMasterDto resDto)
			throws ApplicationException {
		try{
			
			for (PatientMedicalHistoryDto dto : list) {
				if(dto.getCovTemplateId() == null){ 
					PatientMedicalHistory patientMedicalHistory = new PatientMedicalHistory();
					patientMedicalHistory.setIsNoSignificantStatus(dto.getIsNoSignificantStatus());
					patientMedicalHistory.setRemark(dto.getRemark());
					patientMedicalHistory.setDiagnosisId(dto.getDiagnosisId());
					patientMedicalHistory.setSince(dto.getSince());
					patientMedicalHistory.setDuration(dto.getDuration());

//					patientMedicalHistory.setRoleId(dto.getRoleId());
					patientMedicalHistory.setIsReviewedFlag(dto.getIsReviewedFlag());

					patientMedicalHistory.setIsEnterInError(dto.getIsEnterInError());
					patientMedicalHistory.setCreated_date(TimeZoneComponent.getDateByzone(dto.getUnitId()));

//					if (resDto.getEncounterId() != null) {
//						patientMedicalHistory.setEncounterId(resDto.getEncounterId());
//					}
//					if (resDto.getAdmissionId() != null) {
//						patientMedicalHistory.setAdmissionId(resDto.getAdmissionId());
//					}
					patientMedicalHistory.setCreated_by(resDto.getCreatedBy());
					patientMedicalHistory.setUpdated_by(resDto.getUpdatedBy());
					patientMedicalHistory.setUpdated_date(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientMedicalHistory.setStatus(resDto.getStatus());
					patientMedicalHistory.setOrganizationId(resDto.getOrganizationId());
					patientMedicalHistory.setUnitId(resDto.getUnitId());
//					patientMedicalHistory.setPatientId(resDto.getPatientId());
					patientMedicalHistory.setCovTemplateId(resDto.getCovTemplateId());
					save(patientMedicalHistory);
				}/*else {
					 
					PatientMedicalHistory patientMedicalHistory = findById(dto.getCovTemplateId());
					patientMedicalHistory.setIsNoSignificantStatus(dto.getIsNoSignificantStatus());
					patientMedicalHistory.setRemark(dto.getRemark());
					patientMedicalHistory.setDiagnosisId(dto.getDiagnosisId());
					patientMedicalHistory.setSince(dto.getSince());
					patientMedicalHistory.setDuration(dto.getDuration());

					patientMedicalHistory.setRoleId(dto.getRoleId());
					patientMedicalHistory.setIsReviewedFlag(dto.getIsReviewedFlag());

					patientMedicalHistory.setIsEnterInError(dto.getIsEnterInError());
					patientMedicalHistory.setCreated_date(TimeZoneComponent.getDateByzone(dto.getUnitId()));

//					if (resDto.getEncounterId() != null) {
//						patientMedicalHistory.setEncounterId(resDto.getEncounterId());
//					}
//					if (resDto.getAdmissionId() != null) {
//						patientMedicalHistory.setAdmissionId(resDto.getAdmissionId());
//					}
					patientMedicalHistory.setCreated_by(resDto.getCreatedBy());
					patientMedicalHistory.setUpdated_by(resDto.getUpdatedBy());
					patientMedicalHistory.setUpdated_date(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientMedicalHistory.setStatus(resDto.getStatus());
					patientMedicalHistory.setOrganizationId(resDto.getOrganizationId());
					patientMedicalHistory.setUnitId(resDto.getUnitId());
//					patientMedicalHistory.setPatientId(resDto.getPatientId());
					PatientMedicalHistory historyId = update(patientMedicalHistory);
				
				}*/
						

			}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response updateCovTempMededicalHistoryCurrent(List<PatientMedicalHistoryDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		// TODO Auto-generated method stub
		return null;
	}



	
}
