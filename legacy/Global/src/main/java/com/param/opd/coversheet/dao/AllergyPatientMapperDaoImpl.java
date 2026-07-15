package com.param.opd.coversheet.dao;

import com.google.gson.Gson;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.unit.dto.*;
import com.param.opd.coversheet.dto.*;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
import com.param.global.dao.IAllergyMasterDao;
import com.param.global.dto.AllergyMasterDto;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.model.AllergyMaster;
import com.param.global.service.IAllergyMasterService;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.model.AllergyPatientMapper;
import com.param.opd.coversheet.model.PatientMedicalHistory;
import com.param.opd.coversheet.model.PatientPersonalHistoryDetails;
import com.param.opd.coversheet.service.IAllergyPatientMapperService;
import com.param.utility.TimeZoneComponent;
import org.springframework.web.client.RestTemplate;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AllergyPatientMapperDaoImpl extends GenericDao<AllergyPatientMapper, Integer> implements IAllergyPatientMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AllergyPatientMapperDaoImpl.class);


	public AllergyPatientMapperDaoImpl() {
		super(AllergyPatientMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IAllergyPatientMapperService iAllergyPatientMapperService;
	
	@Autowired
	private IAllergyMasterService iAllergyMasterService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Autowired
	private Gson gson;
	
	@Override
	public Response saveAllergyPatientMapper(AllergyPatientMapperDto allergyPatientMapperDto)throws ApplicationException {
		try {
			String Global_Scheduling_URI = null;
			String MY_LIFE_URI = null;
			URI urlMyLife = null;
			Response responseMyLife = null;
			Response commonResponseMyLife = null;
			Boolean isOPDPatient = false;
			Boolean isSyncSchedulingData = false;
			Integer channelId = null;
			String uuid = null;
			List<AllergyPatientMapperDto> allergyIdAndNameList = new ArrayList<>();
			List<AllergyPatientMapperDto> allergyTypeIdAndNameList = new ArrayList<>();
			List<AllergyPatientMapperDto> severityTypeIdAndNameList = new ArrayList<>();
			List<AllergyPatientMapperDto> historyDtoList = new ArrayList<>();

			if (allergyPatientMapperDto.getEncounterId() != null && allergyPatientMapperDto.getEncounterId() > 0) {
				Boolean isSyncScheduling = (Boolean) sessionFactory.getCurrentSession()
						.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.uniqueResult();

				isSyncSchedulingData = isSyncScheduling;

				channelId = (Integer) sessionFactory.getCurrentSession()
						.createSQLQuery("  select org.channel_id as \"channelId\" from public.m_organization_master org where  org.organization_id=:organizationId and org.status='A' ")
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.uniqueResult();

				uuid = (String) sessionFactory.getCurrentSession()
						.createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "
								+ "	where patReg.patient_id=:patientId and patReg.organization_id=:orgId ")
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("orgId", allergyPatientMapperDto.getOrganizationId())
						.uniqueResult();

				if (isSyncScheduling) {
					Response res = iScheduleLogsDao.getConnectionStringForMyLife();
					MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
					if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null && connectionLinkDto.getMyLifeLink() != "" && connectionLinkDto.getGlobalSchedulingLink() != null && connectionLinkDto.getGlobalSchedulingLink() != "") {
						Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
						MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
					}

					List<Integer> allergyIds = allergyPatientMapperDto.getListOfAllergyPatientMapperDto().stream().map(ListOfAllergyPatientMapperDto::getAllergyId).filter(Objects::nonNull).collect(Collectors.toList());
					List<Integer> allergyTypeIds = allergyPatientMapperDto.getListOfAllergyPatientMapperDto().stream().map(ListOfAllergyPatientMapperDto::getAllergyTypeId).filter(Objects::nonNull).collect(Collectors.toList());
					List<Integer> severityIds = allergyPatientMapperDto.getListOfAllergyPatientMapperDto().stream().map(ListOfAllergyPatientMapperDto::getSeverityId).filter(Objects::nonNull).collect(Collectors.toList());

					if(allergyIds != null && allergyIds.size()>0) {
						String sqlQuery = "  select mst.allergy_desc as \"allergyDesc\", mst.allergy_id as \"allergyId\" from  emr.m_allergy_master mst "
								+ " where  mst.organization_id=:organizationId "
								+ "	and mst.status='A' and mst.allergy_id in (:allergyIds)";

						StringBuffer sb = new StringBuffer(sqlQuery);
						allergyIdAndNameList = getSession().createSQLQuery(sb.toString())
								.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
								.setParameterList("allergyIds", allergyIds)
								.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
					}

					if(allergyTypeIds != null && allergyTypeIds.size()>0) {
						String sqlTypeQuery = "  select mst.allergy_type_desc as \"allergyTypeDesc\", mst.allergy_type_id as \"allergyTypeId\" from  emr.m_allergy_type_master mst "
								+ " where  mst.organization_id=:organizationId "
								+ "	and mst.status='A' and mst.allergy_type_id in (:allergyTypeIds)";

						StringBuffer sbType = new StringBuffer(sqlTypeQuery);
						allergyTypeIdAndNameList = getSession().createSQLQuery(sbType.toString())
								.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
								.setParameterList("allergyTypeIds", allergyTypeIds)
								.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
					}

					if(severityIds != null && severityIds.size()>0) {
						String sqlQuery = "  select mst.severity_desc as \"severityDesc\", mst.severity_id as \"severityId\" from  emr.m_severity_master mst "
								+ " where  mst.status='A' and mst.severity_id in (:severityIds)";

						StringBuffer sb = new StringBuffer(sqlQuery);
						severityTypeIdAndNameList = getSession().createSQLQuery(sb.toString())
								.setParameterList("severityIds", severityIds)
								.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
					}
				}
			}

			for (int i = 0; i < allergyPatientMapperDto.getListOfAllergyPatientMapperDto().size(); i++) {
				if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText() == null || allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText() == "") {
					if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyPatientMapperId() != null &&
							allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyPatientMapperId() > 0
					) {
						AllergyPatientMapper allergyPatientMapper = findById(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyPatientMapperId());
						allergyPatientMapper.setIsReviewedFlag(YES);
						iAllergyPatientMapperService.updateSaveNurseReviewedFlag(allergyPatientMapper);
					}

					AllergyPatientMapper allergyPatientMapper = new AllergyPatientMapper();

					allergyPatientMapper.setMonthYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonthYear());

					if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() != null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() > 0) {
						allergyPatientMapper.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId());
					} else {
						allergyPatientMapper.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId());

					}
				/*if(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId() > 0)
				allergyPatientMapper.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId());*/
					if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() != null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() > 0)
						allergyPatientMapper.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());
					allergyPatientMapper.setPatientId(allergyPatientMapperDto.getPatientId());

					allergyPatientMapper.setRoleId(allergyPatientMapperDto.getRoleId());
					allergyPatientMapper.setIsReviewedFlag(
							allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag() == '\0' ? NO :
									allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag()
					);

					allergyPatientMapper.setIsEnterInErrorStatus(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsEnterInErrorStatus());

					if (allergyPatientMapperDto.getEncounterId() != null) {
						allergyPatientMapper.setEncounterId(allergyPatientMapperDto.getEncounterId());
						isOPDPatient = true;
					} else {
						allergyPatientMapper.setAdmissionId(allergyPatientMapperDto.getAdmissionId());
					}


					allergyPatientMapper.setComments(allergyPatientMapperDto.getComments());
					allergyPatientMapper.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
					allergyPatientMapper.setCreatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getCreatedDate(), "dd-M-yyyy"));
					allergyPatientMapper.setIsNoKnownAllergies(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsNoKnownAllergies());
					allergyPatientMapper.setMonth(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonth());
					allergyPatientMapper.setYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getYear());
					allergyPatientMapper.setNatureOfReaction(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getNatureOfReaction());
					allergyPatientMapper.setOrganizationId(allergyPatientMapperDto.getOrganizationId());
					allergyPatientMapper.setRemark(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getRemark());
					allergyPatientMapper.setSeverityId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getSeverityId());
					allergyPatientMapper.setStatus(allergyPatientMapperDto.getStatus());
					allergyPatientMapper.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
					allergyPatientMapper.setUnitId(allergyPatientMapperDto.getUnitId());
					allergyPatientMapper.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
					allergyPatientMapper.setUpdatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getUpdatedDate(), "dd-M-yyyy"));
					AllergyPatientMapper historyId = save(allergyPatientMapper);

					if (historyId.getAllergyPatientMapperId() != null && isSyncSchedulingData && isOPDPatient) {

						AllergyPatientMapperDto tempDto = new AllergyPatientMapperDto();

						tempDto.setMonthYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonthYear());
						if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() != null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() > 0) {
							tempDto.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId());
						} else {
							tempDto.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId());
						}
						if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() != null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() > 0)
							tempDto.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());

						tempDto.setIsReviewedFlag(
								allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag() == '\0' ? NO :
										allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag()
						);

						tempDto.setIsEnterInErrorStatus(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsEnterInErrorStatus());

						tempDto.setHospitalId(allergyPatientMapperDto.getOrganizationId());
						tempDto.setIsFromHisApiCall(TRUE);
						tempDto.setIsNoKnownAllergies(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsNoKnownAllergies());
						tempDto.setMonth(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonth());
						tempDto.setYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getYear());
						tempDto.setNatureOfReaction(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getNatureOfReaction());
						tempDto.setRemark(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getRemark());
						tempDto.setSeverityId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getSeverityId());
						tempDto.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
						tempDto.setUuid(uuid != null ? uuid : "0");
						tempDto.setComments(allergyPatientMapperDto.getComments());
						tempDto.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
						tempDto.setStatus(allergyPatientMapperDto.getStatus());
						tempDto.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
						tempDto.setUnitId(allergyPatientMapperDto.getUnitId());
						tempDto.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
						tempDto.setUpdatedDate(allergyPatientMapperDto.getUpdatedDate());
						tempDto.setCreatedDate(allergyPatientMapperDto.getCreatedDate());
						tempDto.setApplicationId(2);

						historyDtoList.add(tempDto);

					}

				} else {

					AllergyMasterDto allergyMasterDto = new AllergyMasterDto();
					allergyMasterDto.setAllergyDesc(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText());
					allergyMasterDto.setAllergyName(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText());
					allergyMasterDto.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());
					allergyMasterDto.setStatus(allergyPatientMapperDto.getStatus());
					allergyMasterDto.setOrganizationId(allergyPatientMapperDto.getOrganizationId());
					allergyMasterDto.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
					allergyMasterDto.setUnitId(allergyPatientMapperDto.getUnitId());
					allergyMasterDto.setCreatedDate(allergyPatientMapperDto.getCreatedDate());
					allergyMasterDto.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
					allergyMasterDto.setUpdatedDate(allergyPatientMapperDto.getUpdatedDate());
					Response res = iAllergyMasterService.saveAllergyMaster(allergyMasterDto);

					if (res.getObject() == null) {
						return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
					} else {
						AllergyMasterDto dto = (AllergyMasterDto) res.getObject();

						if (dto != null) {

							if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyPatientMapperId() != null &&
									allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyPatientMapperId() > 0
							) {
								AllergyPatientMapper allergyPatientMapper = findById(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyPatientMapperId());
								allergyPatientMapper.setIsReviewedFlag(YES);
								iAllergyPatientMapperService.updateSaveNurseReviewedFlag(allergyPatientMapper);
							}

							AllergyPatientMapper allergyPatientMapper = new AllergyPatientMapper();


							if (dto.getAllergyId() != null && dto.getAllergyId() > 0) {
								allergyPatientMapper.setAllergyId(dto.getAllergyId());
							}
						/*if(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId() > 0)
						allergyPatientMapper.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId());*/
							if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() != null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() > 0)
								allergyPatientMapper.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());
							allergyPatientMapper.setPatientId(allergyPatientMapperDto.getPatientId());

							allergyPatientMapper.setRoleId(allergyPatientMapperDto.getRoleId());
							allergyPatientMapper.setIsReviewedFlag(
									allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag() == '\0' ? NO :
											allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag()
							);

							allergyPatientMapper.setIsEnterInErrorStatus(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsEnterInErrorStatus());

							if (allergyPatientMapperDto.getEncounterId() != null) {
								allergyPatientMapper.setEncounterId(allergyPatientMapperDto.getEncounterId());
								isOPDPatient = true;
							} else {
								allergyPatientMapper.setAdmissionId(allergyPatientMapperDto.getAdmissionId());
							}


							allergyPatientMapper.setComments(allergyPatientMapperDto.getComments());
							allergyPatientMapper.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
							allergyPatientMapper.setCreatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getCreatedDate(), "dd-M-yyyy"));
							allergyPatientMapper.setIsNoKnownAllergies(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsNoKnownAllergies());
							allergyPatientMapper.setMonth(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonth());
							allergyPatientMapper.setYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getYear());
							allergyPatientMapper.setNatureOfReaction(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getNatureOfReaction());
							allergyPatientMapper.setOrganizationId(allergyPatientMapperDto.getOrganizationId());
							allergyPatientMapper.setRemark(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getRemark());
							allergyPatientMapper.setSeverityId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getSeverityId());
							allergyPatientMapper.setStatus(allergyPatientMapperDto.getStatus());
							allergyPatientMapper.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
							allergyPatientMapper.setUnitId(allergyPatientMapperDto.getUnitId());
							allergyPatientMapper.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
							allergyPatientMapper.setUpdatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getUpdatedDate(), "dd-M-yyyy"));
							AllergyPatientMapper historyId = save(allergyPatientMapper);

							if (historyId.getAllergyPatientMapperId() != null && isSyncSchedulingData && isOPDPatient) {

								AllergyPatientMapperDto tempDto = new AllergyPatientMapperDto();

								tempDto.setMonthYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonthYear());
								if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() != null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() > 0) {
									tempDto.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId());
								} else {
									tempDto.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId());

								}

								if (allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() != null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId() > 0)
									tempDto.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());

								tempDto.setIsReviewedFlag(
										allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag() == '\0' ? NO :
												allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag()
								);

								tempDto.setIsEnterInErrorStatus(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsEnterInErrorStatus());

								tempDto.setHospitalId(allergyPatientMapperDto.getOrganizationId());
								tempDto.setIsFromHisApiCall(TRUE);
								tempDto.setIsNoKnownAllergies(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsNoKnownAllergies());
								tempDto.setMonth(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonth());
								tempDto.setYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getYear());
								tempDto.setNatureOfReaction(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getNatureOfReaction());
								tempDto.setRemark(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getRemark());
								tempDto.setSeverityId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getSeverityId());
								tempDto.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
								tempDto.setUuid(uuid != null ? uuid : "0");
								tempDto.setApplicationId(2);
								tempDto.setComments(allergyPatientMapperDto.getComments());
								tempDto.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
								tempDto.setStatus(allergyPatientMapperDto.getStatus());
								tempDto.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
								tempDto.setUnitId(allergyPatientMapperDto.getUnitId());
								tempDto.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
								tempDto.setUpdatedDate(allergyPatientMapperDto.getUpdatedDate());
								tempDto.setCreatedDate(allergyPatientMapperDto.getCreatedDate());
								tempDto.setAllergyDesc(allergyPatientMapperDto.getAllergyDesc() != null ?
										allergyPatientMapperDto.getAllergyDesc() : allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText());

								historyDtoList.add(tempDto);
							}
						}
					}
				}
			}

			if ((historyDtoList != null && !historyDtoList.isEmpty())) {
				if (!allergyIdAndNameList.isEmpty()) {
					if (allergyIdAndNameList != null && !allergyIdAndNameList.isEmpty()) {
						for (AllergyPatientMapperDto dto : historyDtoList) {
							AllergyPatientMapperDto objectDto = allergyIdAndNameList.stream()
									.filter(order -> dto.getAllergyId().equals(order.getAllergyId()))
									.findAny()
									.orElse(null);
							if (objectDto != null) {
								dto.setAllergyDesc(objectDto.getAllergyDesc());
							}
						}
					}
				}

				if (allergyTypeIdAndNameList != null && !allergyTypeIdAndNameList.isEmpty()) {
					for (AllergyPatientMapperDto dto : historyDtoList) {
						AllergyPatientMapperDto objectDto = allergyTypeIdAndNameList.stream()
								.filter(order -> dto.getAllergyTypeId().equals(order.getAllergyTypeId()))
								.findAny()
								.orElse(null);
						if (objectDto != null) {
							dto.setAllergyTypeDesc(objectDto.getAllergyTypeDesc());
						}
					}
				}

				if (severityTypeIdAndNameList != null && !severityTypeIdAndNameList.isEmpty()) {
					for (AllergyPatientMapperDto dto : historyDtoList) {
						AllergyPatientMapperDto objectDto = severityTypeIdAndNameList.stream()
								.filter(order -> dto.getSeverityId().equals(order.getSeverityId()))
								.findAny()
								.orElse(null);
						if (objectDto != null) {
							dto.setSeverityDesc(objectDto.getSeverityDesc());
						}
					}
				}

				AllergyPatientMapperDto reqHistoryDto = new AllergyPatientMapperDto();
				reqHistoryDto.setAllergyPatientMapperDtoList(historyDtoList);

				try {
					urlMyLife = URI.create(MY_LIFE_URI + "/saveAllergyPatientMapperFromHis");
					HttpHeaders headers = new HttpHeaders();
					//headers.set("channelId", (patient.getChannelId() != null && patient.getChannelId() != 0) ? patient.getChannelId().toString():"1");
					headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
					HttpEntity<AllergyPatientMapperDto> request = new HttpEntity<>(reqHistoryDto, headers);
					String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
					commonResponseMyLife = gson.fromJson(stringResponse, Response.class);
				} catch (Exception e) {
					logger.error("saveAllergyPatientMapper MyLife sync failed", e);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);

			}catch(HibernateException he){
			logger.error("saveAllergyPatientMapper failed", he);
			} catch(Exception e){
			logger.error("saveAllergyPatientMapper failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getListOfAllergyPatientMapper(AllergyPatientMapperDto allergyPatientMapperDto)throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto = statlessSession.getNamedQuery("GET_LIST_ALLERGY_PATIENT_MAPPER")
					.setParameter("patientId", allergyPatientMapperDto.getPatientId())
//					.setParameter("unitId", allergyPatientMapperDto.getUnitId())
					.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
					.setParameter("created_date", allergyPatientMapperDto.getCreatedDate())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfAllergyPatientMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response cancelAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			int updateCount = statlessSession.getNamedQuery("CANCEL_ALLERGY_PATIENT_MAPPER")
					.setParameter("allergyId", allergyPatientMapperDto.getAllergyId())
					.setParameter("allergyTypeId", allergyPatientMapperDto.getAllergyTypeId())
					.setParameter("patientId", allergyPatientMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).executeUpdate();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("cancelAllergyPatientMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getOldAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto;
			/*if(allergyPatientMapperDto.getRoleId() == 3)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				listAllergyPatientMapperDto = statlessSession.getNamedQuery("GET_LIST_ALLERGY_PATIENT_MAPPER_BY_ROLE_ID")
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("unitId", allergyPatientMapperDto.getUnitId())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setParameter("encounterId", allergyPatientMapperDto.getEncounterId())
						.setParameter("roleId", allergyPatientMapperDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			}
			else
			{*/
//				Session statlessSession = sessionFactory.getCurrentSession();
//				listAllergyPatientMapperDto = statlessSession.getNamedQuery("GET_LIST_ALLERGY_PATIENT_MAPPER")
//						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
//						.setParameter("unitId", allergyPatientMapperDto.getUnitId())
//						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
//						.setParameter("encounterId", allergyPatientMapperDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			/*}
			*/
				
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_ALLERGY_PATIENT_MAPPER").getQueryString();
				StringBuffer subQuery=new StringBuffer();
						/*if(allergyPatientMapperDto.getEncounterId()!=null){
							subQuery.append(" AND (alrgyMap.encounterId IS NULL OR alrgyMap.encounterId !="+allergyPatientMapperDto.getEncounterId()+")");
							subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
						}
						if(allergyPatientMapperDto.getAdmissionId()!=null){
							subQuery.append(" AND (alrgyMap.admissionId IS NULL OR alrgyMap.admissionId !="+allergyPatientMapperDto.getAdmissionId()+")");
							subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
						}*/
						query = query.concat(subQuery.toString());	
						listAllergyPatientMapperDto=sessionFactory.getCurrentSession().createQuery(query)
								.setParameter("patientId", allergyPatientMapperDto.getPatientId())
//								.setParameter("unitId", allergyPatientMapperDto.getUnitId())
								.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
								.setParameter("created_date", allergyPatientMapperDto.getCreatedDate())
								.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldAllergyPatientMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto;
			if(allergyPatientMapperDto.getRoleId() == 3)
			{

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(allergyPatientMapperDto.getEncounterId()!=null){
//					subQuery.append(" AND alrgyMap.encounterId="+allergyPatientMapperDto.getEncounterId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}
				if(allergyPatientMapperDto.getAdmissionId()!=null){
//					subQuery.append(" AND alrgyMap.admissionId ="+allergyPatientMapperDto.getAdmissionId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}
				query = query.concat(subQuery.toString());	
				listAllergyPatientMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("unitId", allergyPatientMapperDto.getUnitId())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setParameter("roleId", allergyPatientMapperDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			}
			else
			{

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(allergyPatientMapperDto.getEncounterId()!=null){
//					subQuery.append(" AND alrgyMap.encounterId="+allergyPatientMapperDto.getEncounterId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}
				if(allergyPatientMapperDto.getAdmissionId()!=null){
//					subQuery.append(" AND alrgyMap.admissionId ="+allergyPatientMapperDto.getAdmissionId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}
				query = query.concat(subQuery.toString());	
				listAllergyPatientMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("unitId", allergyPatientMapperDto.getUnitId())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentAllergyPatientMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updateAllergyPatientMapper(
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			for(int i=0; i<allergyPatientMapperDto.getListOfAllergyPatientMapperDto().size();i++)
			{
				AllergyPatientMapper allergyPatientMapper = findById(allergyPatientMapperDto.getAllergyPatientMapperId());

				/*if(allergyPatientMapperDto.getRoleId() != null 
						&& allergyPatientMapperDto.getRoleId() == 2 
						&& allergyPatientMapper.getRoleId() != 2 
						&& allergyPatientMapper.getIsReviewedFlag() == 'N'
						)
				{
					iAllergyPatientMapperService.saveAllergyPatientMapper(allergyPatientMapperDto);
				}*/
				
//				allergyPatientMapper.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId());
//				if(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId()!=null && allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() > 0) {
//					allergyPatientMapper.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId());
//					}else {
//						allergyPatientMapper.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId());
//
//					}
						if(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText() == null) {
					     allergyPatientMapper.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId()!=null ? allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() : null);
						 allergyPatientMapper.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId()!=null ? allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId() : null);
		
						allergyPatientMapper.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());
						allergyPatientMapper.setPatientId(allergyPatientMapperDto.getPatientId());
						allergyPatientMapper.setMonthYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonthYear());
						allergyPatientMapper.setRoleId(allergyPatientMapperDto.getRoleId());
						allergyPatientMapper.setIsReviewedFlag(
									allergyPatientMapperDto.getRoleId() == 2 && allergyPatientMapper.getRoleId() == 3 ? YES :
										allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag()
								);
						
						allergyPatientMapper.setIsEnterInErrorStatus( allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsEnterInErrorStatus() );
						if(allergyPatientMapperDto.getEncounterId()!=null){
							allergyPatientMapper.setEncounterId(allergyPatientMapperDto.getEncounterId());
						}else{
							allergyPatientMapper.setAdmissionId(allergyPatientMapperDto.getAdmissionId());
						}
						
						allergyPatientMapper.setComments(allergyPatientMapperDto.getComments());
						allergyPatientMapper.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
						allergyPatientMapper.setCreatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getCreatedDate(), "dd-M-yyyy"));
						allergyPatientMapper.setIsNoKnownAllergies(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsNoKnownAllergies());
						allergyPatientMapper.setMonth(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonth());
						allergyPatientMapper.setYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getYear());
						allergyPatientMapper.setNatureOfReaction(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getNatureOfReaction());
						allergyPatientMapper.setOrganizationId(allergyPatientMapperDto.getOrganizationId());
						allergyPatientMapper.setRemark(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getRemark());
						allergyPatientMapper.setSeverityId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getSeverityId());
						allergyPatientMapper.setStatus(allergyPatientMapperDto.getStatus());
						allergyPatientMapper.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
						allergyPatientMapper.setUnitId(allergyPatientMapperDto.getUnitId());
						allergyPatientMapper.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
						allergyPatientMapper.setUpdatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getUpdatedDate(),"dd-M-yyyy"));
						AllergyPatientMapper historyId=save(allergyPatientMapper);
						}else {
							
							AllergyMasterDto allergyMasterDto = new AllergyMasterDto();
							allergyMasterDto.setAllergyDesc(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText());
							allergyMasterDto.setAllergyName(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyDescText());
							allergyMasterDto.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());
							allergyMasterDto.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId());
							allergyMasterDto.setOrganizationId(allergyPatientMapperDto.getOrganizationId());
							allergyMasterDto.setUnitId(allergyPatientMapperDto.getUnitId());
							allergyMasterDto.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
							allergyMasterDto.setUpdatedDate(allergyPatientMapperDto.getUpdatedDate());
							Response res=iAllergyMasterService.updateAllergyMaster(allergyMasterDto);
							
							AllergyMasterDto dto = (AllergyMasterDto) res.getObject();

						     allergyPatientMapper.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId()!=null ? allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId() : null);
							 allergyPatientMapper.setGenericId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId()!=null ? allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getGenericId() : null);
			
							allergyPatientMapper.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());
							allergyPatientMapper.setPatientId(allergyPatientMapperDto.getPatientId());
							
							allergyPatientMapper.setRoleId(allergyPatientMapperDto.getRoleId());
							allergyPatientMapper.setIsReviewedFlag(
										allergyPatientMapperDto.getRoleId() == 2 && allergyPatientMapper.getRoleId() == 3 ? YES :
											allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag()
									);
							
							allergyPatientMapper.setIsEnterInErrorStatus( allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsEnterInErrorStatus() );
							if(allergyPatientMapperDto.getEncounterId()!=null){
								allergyPatientMapper.setEncounterId(allergyPatientMapperDto.getEncounterId());
							}else{
								allergyPatientMapper.setAdmissionId(allergyPatientMapperDto.getAdmissionId());
							}
							
							allergyPatientMapper.setComments(allergyPatientMapperDto.getComments());
							allergyPatientMapper.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
							allergyPatientMapper.setCreatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getCreatedDate(), "dd-M-yyyy"));
							allergyPatientMapper.setIsNoKnownAllergies(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsNoKnownAllergies());
							allergyPatientMapper.setMonth(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonth());
							allergyPatientMapper.setYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getYear());
							allergyPatientMapper.setNatureOfReaction(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getNatureOfReaction());
							allergyPatientMapper.setOrganizationId(allergyPatientMapperDto.getOrganizationId());
							allergyPatientMapper.setRemark(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getRemark());
							allergyPatientMapper.setSeverityId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getSeverityId());
							allergyPatientMapper.setStatus(allergyPatientMapperDto.getStatus());
							allergyPatientMapper.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
							allergyPatientMapper.setUnitId(allergyPatientMapperDto.getUnitId());
							allergyPatientMapper.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
							allergyPatientMapper.setUpdatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getUpdatedDate(),"dd-M-yyyy"));
							AllergyPatientMapper historyId=save(allergyPatientMapper);
							
						}
			}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
		logger.error("updateAllergyPatientMapper failed", he);
		} catch (Exception e) {
		logger.error("updateAllergyPatientMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updateSaveNurseReviewedFlag(
			AllergyPatientMapper allergyPatientMapper)
			throws ApplicationException {
		try
		{
			save(allergyPatientMapper);
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
			AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			for(int i=0; i<allergyPatientMapperDto.getListOfAllergyPatientMapperDto().size();i++)
			{
				AllergyPatientMapper allergyPatient = findById(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyPatientMapperId());
				
				allergyPatient.setIsEnterInErrorStatus(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsEnterInErrorStatus());
				allergyPatient.setIsReviewedFlag(allergyPatient.getIsReviewedFlag() == 'N' && 
						allergyPatient.getRoleId() == 3 && allergyPatientMapperDto.getRoleId() == 2 ? YES : allergyPatient.getIsReviewedFlag());
				iAllergyPatientMapperService.updateSaveNurseReviewedFlag(allergyPatient);
				
				if(allergyPatientMapperDto.getRoleId() != null && allergyPatientMapperDto.getRoleId() == 2 && allergyPatient.getRoleId() == 3)
				{
					AllergyPatientMapper allergyPatientMapper = new AllergyPatientMapper();
					
					allergyPatientMapper.setAllergyId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyId());
					allergyPatientMapper.setAllergyTypeId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getAllergyTypeId());
					allergyPatientMapper.setPatientId(allergyPatientMapperDto.getPatientId());
					
					allergyPatientMapper.setRoleId(allergyPatientMapperDto.getRoleId());
					allergyPatientMapper.setIsReviewedFlag(
							allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag() == '\0' ? NO :
								allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsReviewedFlag()
							);
					
					allergyPatientMapper.setIsEnterInErrorStatus(NO);
					
					if(allergyPatientMapperDto.getEncounterId()!=null){
						allergyPatientMapper.setEncounterId(allergyPatientMapperDto.getEncounterId());
					}else{
						allergyPatientMapper.setAdmissionId(allergyPatientMapperDto.getAdmissionId());
					}
					
					allergyPatientMapper.setComments(allergyPatientMapperDto.getComments());
					allergyPatientMapper.setCreatedBy(allergyPatientMapperDto.getCreatedBy());
					allergyPatientMapper.setCreatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getCreatedDate(), "dd-M-yyyy"));
					allergyPatientMapper.setIsNoKnownAllergies(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getIsNoKnownAllergies());
					allergyPatientMapper.setMonth(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getMonth());
					allergyPatientMapper.setYear(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getYear());
					allergyPatientMapper.setNatureOfReaction(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getNatureOfReaction());
					allergyPatientMapper.setOrganizationId(allergyPatientMapperDto.getOrganizationId());
					allergyPatientMapper.setRemark(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getRemark());
					allergyPatientMapper.setSeverityId(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getSeverityId());
					allergyPatientMapper.setStatus(allergyPatientMapperDto.getStatus());
					allergyPatientMapper.setType(allergyPatientMapperDto.getListOfAllergyPatientMapperDto().get(i).getType());
					allergyPatientMapper.setUnitId(allergyPatientMapperDto.getUnitId());
					allergyPatientMapper.setUpdatedBy(allergyPatientMapperDto.getUpdatedBy());
					allergyPatientMapper.setUpdatedDate(CommonDateUtils.getDate(allergyPatientMapperDto.getUpdatedDate(),"dd-M-yyyy"));
					AllergyPatientMapper historyId=save(allergyPatientMapper);
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
	public Response getGenericAutoFillSearch(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try{
				List<AllergyPatientMapperDto> allergyPatientMapperDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GENERIC_AUTO_FILL_SEARCH")
						.setParameter("genericName", "%" + allergyPatientMapperDto.getGenericSearchName().toLowerCase() + "%")
						//.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						//.setParameter("unitId", allergyPatientMapperDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
				return new Response(SUCCESS, null, null, allergyPatientMapperDtoList, null);
			} catch (HibernateException he) {
			logger.error("getGenericAutoFillSearch failed", he);
			} catch (Exception e) {
			logger.error("getGenericAutoFillSearch failed", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCurrentAllergyPatientMapperForCoversheet(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto;
			/*if(allergyPatientMapperDto.getRoleId() == 3)
			{

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(allergyPatientMapperDto.getEncounterId()!=null){
//					subQuery.append(" AND alrgyMap.encounterId="+allergyPatientMapperDto.getEncounterId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}
				if(allergyPatientMapperDto.getAdmissionId()!=null){
//					subQuery.append(" AND alrgyMap.admissionId ="+allergyPatientMapperDto.getAdmissionId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}
				query = query.concat(subQuery.toString());	
				listAllergyPatientMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("unitId", allergyPatientMapperDto.getUnitId())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setParameter("roleId", allergyPatientMapperDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			}
			else
			{*/

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery=new StringBuffer();
			/*	if(allergyPatientMapperDto.getEncounterId()!=null){
//					subQuery.append(" AND alrgyMap.encounterId="+allergyPatientMapperDto.getEncounterId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}
				if(allergyPatientMapperDto.getAdmissionId()!=null){
//					subQuery.append(" AND alrgyMap.admissionId ="+allergyPatientMapperDto.getAdmissionId());
					subQuery.append("  ORDER BY 	alrgyMap.allergyPatientMapperId desc");
				}*/
				query = query.concat(subQuery.toString());	
				listAllergyPatientMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
//						.setParameter("unitId", allergyPatientMapperDto.getUnitId())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setParameter("created_date", allergyPatientMapperDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
//			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentAllergyPatientMapperForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientAllergyListForIcon(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try{
			List<AllergyPatientMapperDto> allergyPatientMapperDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_ALLERGY_LIST_FOR_ICON")
					.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
					.setParameter("unitId", allergyPatientMapperDto.getUnitId())
					.setParameter("patientId", allergyPatientMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			return new Response(SUCCESS, null, null, allergyPatientMapperDtoList, null);
		} catch (HibernateException he) {
		logger.error("getPatientAllergyListForIcon failed", he);
		} catch (Exception e) {
		logger.error("getPatientAllergyListForIcon failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getAllergyPatientMapperForBookView(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto;

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_ALLERGY_PATIENT_MAPPER_FOR_BOOK_VIEW").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query = query.concat(subQuery.toString());	
				listAllergyPatientMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("fromDate", allergyPatientMapperDto.getFromDate())
						.setParameter("toDate", allergyPatientMapperDto.getToDate())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getAllergyPatientMapperForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveResolvedAllergyDate(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try {
			
			AllergyPatientMapper allergyPatientMapper  = findById(allergyPatientMapperDto.getAllergyPatientMapperId());
			allergyPatientMapper.setResolvedDate(GlobalCommonDateUtils.getDate(allergyPatientMapperDto.getResolvedDate(), "dd-M-yyyy HH:mm:ss"));
			allergyPatientMapper.setResolvedRemark(allergyPatientMapperDto.getResolvedRemark());
			update(allergyPatientMapper);
			return new Response(SUCCESS, SUCCESS_CODE, "Allergy marked as Resolved.", null, null);
		} catch (Exception e) {
		logger.error("saveResolvedAllergyDate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getResolvedAllergyPatientMapperForCoversheet(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try
		{
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto;
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_RESOLVED_LIST_ALLERGY_PATIENT_MAPPER_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query = query.concat(subQuery.toString());	
				listAllergyPatientMapperDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getResolvedAllergyPatientMapperForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientAllergies(AllergyPatientMapperDto allergyPatientMapperDto) throws ApplicationException {
		try
		{
			List<AllergyPatientMapperDto> listAllergyPatientMapperDto=sessionFactory.getCurrentSession().getNamedQuery("GET_ALLERGY_PATIENT_MAPPER_FOR_DOCTOR_APP")
						.setParameter("patientId", allergyPatientMapperDto.getPatientId())
						.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientAllergies failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getActiveClinicalHeaderTabList(ClinicalHeaderTabDto clinicalHeaderTabDto)
			throws ApplicationException {
		try
		{
			
			List<ClinicalHeaderTabDto> list= new ArrayList<>();
			List<ClinicalHeaderTabDto> listClinicalHeaderTabList=sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_CLINICAL_HEADER_TAB")
						.setResultTransformer(Transformers.aliasToBean(ClinicalHeaderTabDto.class)).list();
			
			for(ClinicalHeaderTabDto dto : listClinicalHeaderTabList) 
			{
				
				List<ClinicalSubtabMapperDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LIST_CLINICAL_SUB_TAB_MAPPER")
						.setParameter("headerId",dto.getHeaderId())
						.setResultTransformer(Transformers.aliasToBean(ClinicalSubtabMapperDto.class)).list();
				
					dto.setListSubTabDetails(list1);
					list.add(dto);
		}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getActiveClinicalHeaderTabList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getMappedCoversheetDataForUser(ClinicalHeaderTabDto clinicalHeaderTabDto)
			throws ApplicationException {
		try {
	
			String query ="SELECT distinct(um.user_id) as \"userId\", "
					+ " um.tab_access_ids as \"tabAccessIds\", " 
					+ " um.mis_report_ids as \"misReportIds\", " 
					+ " um.tab_access_ids_IPD as \"tabAccessIdsIPD\" " 
					+ " FROM public.t_user_unit_mapper um "
					+ " WHERE um.unit_id=:unitId  "
					+ " AND um.user_id=:userId "
					+ " and status ='A'";
			List<ClinicalHeaderTabDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", clinicalHeaderTabDto.getUnitId())
					.setParameter("userId", clinicalHeaderTabDto.getUserId())
					.setResultTransformer(Transformers.aliasToBean(ClinicalHeaderTabDto.class)).list();
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getMappedCoversheetDataForUser failed", he);
		} catch (Exception e) {
		logger.error("getMappedCoversheetDataForUser failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getFrequentlyUsedAllergies(AllergyPatientMapperDto allergyPatientMapperDto)
			throws ApplicationException {
		try {
			
			String query = "select " 
					+ "	mppr.allergy_id as \"allergyId\", "
					+ "	mst.allergy_desc as \"allergyDesc\", "
					+ "	matm.allergy_type_desc as \"allergyTypeDesc\", "
					+ "	mppr.allergy_type_id as \"allergyTypeId\", " 
					+ "	mppr.status as \"status\", "
					+ "	mppr.organization_id as \"organizationId\", " 
					+ "	mppr.unit_id as \"unitId\", "
					+ "	count(*)	as \"allergyCount\" " 
					+ " from "
					+ "	patient.t_allergy_patient_mapper mppr "
					+ " inner join emr.m_allergy_type_master matm on matm.allergy_type_id = mppr.allergy_type_id  "
					+ " inner join emr.m_allergy_master mst on mst.allergy_id = mppr.allergy_id  "
					+ " where "
					+ "	mppr.unit_id = :unitId" 
					+ "	and mppr.organization_id = :organizationId" 
					+ "	and mppr.status = 'A' "
					+ " group by " 
					+ "	mppr.allergy_id , " 
					+ "	mppr.allergy_type_id,  "
					+ "	mst.allergy_desc, " 
					+ "	matm.allergy_type_desc, " 
					+ "	mppr.status, "
					+ "	mppr.unit_id, " 
					+ "	mppr.organization_id " 
					+ " order by " 
					+ "	count(*) desc "
					+ " limit 10;";
			List<ClinicalHeaderTabDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", allergyPatientMapperDto.getUnitId())
					.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getFrequentlyUsedAllergies failed", he);
		} catch (Exception e) {
		logger.error("getFrequentlyUsedAllergies failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchAllergenList(AllergyPatientMapperDto allergyPatientMapperDto) throws ApplicationException {
try {
			
			String query = "select " + 
					"	allergymas.allergy_id as \"allergyId\", " + 
					"	allergymas.allergy_name as \"allergyName\", " + 
					"	concat(allergymas.allergy_desc , ' | ', allergytype.allergy_type_desc) as \"allergen\", " + 
					"	allergymas.allergy_type_id as \"allergyTypeId\", " + 
					"	allergytype.allergy_type_desc as \"allergyTypeDesc\", " + 
					"	allergymas.status as \"status\" " + 
					" from " + 
					"	emr.m_allergy_master allergymas " + 
					" inner join emr.m_allergy_type_master allergytype on " + 
					"	allergymas.allergy_type_id = allergytype.allergy_type_id " + 
					" where " + 
					"	allergymas.organization_id = :organizationId"
					+ " and allergymas.status = 'A'" + 
					"	and lower(allergymas.allergy_name) like '%"+allergyPatientMapperDto.getSearchAllergen()+"%'";
			List<ClinicalHeaderTabDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", allergyPatientMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("searchAllergenList failed", he);
		} catch (Exception e) {
		logger.error("searchAllergenList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveCurrentAllergyPatientMapper(List<AllergyPatientMapperDto> list, CoversheetTemplateMasterDto resDto)
			throws ApplicationException {
		try{
			
			for (AllergyPatientMapperDto dto : list) {
				if(dto.getCovTemplateId() == null){
					AllergyPatientMapper allergyPatientMapper = new AllergyPatientMapper();
					allergyPatientMapper.setMonthYear(dto.getMonthYear());
					if(dto.getAllergyId()!=null && dto.getAllergyId() > 0) {
					allergyPatientMapper.setAllergyId(dto.getAllergyId());
					}else {
						allergyPatientMapper.setGenericId(dto.getGenericId());
					}
					if(dto.getAllergyTypeId()!=null && dto.getAllergyTypeId() > 0)
					allergyPatientMapper.setAllergyTypeId(dto.getAllergyTypeId());
//					allergyPatientMapper.setPatientId(resDto.getPatientId());
					
					allergyPatientMapper.setRoleId(dto.getRoleId());
					allergyPatientMapper.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO :dto.getIsReviewedFlag());
					allergyPatientMapper.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					
//					if(resDto.getEncounterId()!=null){
//						allergyPatientMapper.setEncounterId(resDto.getEncounterId());
//					}else{
//						allergyPatientMapper.setAdmissionId(resDto.getAdmissionId());
//					}
					allergyPatientMapper.setComments(dto.getComments());
					allergyPatientMapper.setCreatedBy(resDto.getCreatedBy());
					allergyPatientMapper.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					allergyPatientMapper.setIsNoKnownAllergies(dto.getIsNoKnownAllergies());
					allergyPatientMapper.setMonth(dto.getMonth());
					allergyPatientMapper.setYear(dto.getYear());
					allergyPatientMapper.setNatureOfReaction(dto.getNatureOfReaction());
					allergyPatientMapper.setOrganizationId(resDto.getOrganizationId());
					allergyPatientMapper.setRemark(dto.getRemark());
					allergyPatientMapper.setSeverityId(dto.getSeverityId());
					allergyPatientMapper.setStatus(resDto.getStatus());
					allergyPatientMapper.setType(dto.getType());
					allergyPatientMapper.setCovTemplateId(resDto.getCovTemplateId());
					allergyPatientMapper.setUnitId(resDto.getUnitId());
					allergyPatientMapper.setUpdatedBy(resDto.getUpdatedBy());
					allergyPatientMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					save(allergyPatientMapper);
					
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
					
				}/*else {
					AllergyPatientMapper allergyPatientMapper = findById(resDto.getCovTemplateId());
					allergyPatientMapper.setMonthYear(dto.getMonthYear());
					if(dto.getAllergyId()!=null && dto.getAllergyId() > 0) {
					allergyPatientMapper.setAllergyId(dto.getAllergyId());
					}else {
						allergyPatientMapper.setGenericId(dto.getGenericId());
					}
					if(dto.getAllergyTypeId()!=null && dto.getAllergyTypeId() > 0)
					allergyPatientMapper.setAllergyTypeId(dto.getAllergyTypeId());
//					allergyPatientMapper.setPatientId(resDto.getPatientId());
					
					allergyPatientMapper.setRoleId(dto.getRoleId());
					allergyPatientMapper.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO :dto.getIsReviewedFlag());
					allergyPatientMapper.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					
//					if(resDto.getEncounterId()!=null){
//						allergyPatientMapper.setEncounterId(resDto.getEncounterId());
//					}else{
//						allergyPatientMapper.setAdmissionId(resDto.getAdmissionId());
//					}
					allergyPatientMapper.setComments(dto.getComments());
					allergyPatientMapper.setCreatedBy(resDto.getCreatedBy());
					allergyPatientMapper.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					allergyPatientMapper.setIsNoKnownAllergies(dto.getIsNoKnownAllergies());
					allergyPatientMapper.setMonth(dto.getMonth());
					allergyPatientMapper.setYear(dto.getYear());
					allergyPatientMapper.setNatureOfReaction(dto.getNatureOfReaction());
					allergyPatientMapper.setOrganizationId(resDto.getOrganizationId());
					allergyPatientMapper.setRemark(dto.getRemark());
					allergyPatientMapper.setSeverityId(dto.getSeverityId());
					allergyPatientMapper.setStatus(resDto.getStatus());
					allergyPatientMapper.setType(dto.getType());
					allergyPatientMapper.setUnitId(resDto.getUnitId());
					allergyPatientMapper.setUpdatedBy(resDto.getUpdatedBy());
					allergyPatientMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));

					AllergyPatientMapper historyId=update(allergyPatientMapper);
					
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
					
				}*/
						

			}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempCurrentAllergyPatientMapper(List<AllergyPatientMapperDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
try{
			
			for (AllergyPatientMapperDto dto : list) {
				if(dto.getCovTemplateId() != null){
					
					AllergyPatientMapper allergyPatientMapper = findById(coversheetTemplateMasterDto.getCovTemplateId());
					allergyPatientMapper.setMonthYear(dto.getMonthYear());
					if(dto.getAllergyId()!=null && dto.getAllergyId() > 0) {
					allergyPatientMapper.setAllergyId(dto.getAllergyId());
					}else {
						allergyPatientMapper.setGenericId(dto.getGenericId());
					}
					if(dto.getAllergyTypeId()!=null && dto.getAllergyTypeId() > 0)
					allergyPatientMapper.setAllergyTypeId(dto.getAllergyTypeId());
//					allergyPatientMapper.setPatientId(resDto.getPatientId());
					
					allergyPatientMapper.setRoleId(dto.getRoleId());
					allergyPatientMapper.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO :dto.getIsReviewedFlag());
					allergyPatientMapper.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					
//					if(resDto.getEncounterId()!=null){
//						allergyPatientMapper.setEncounterId(resDto.getEncounterId());
//					}else{
//						allergyPatientMapper.setAdmissionId(resDto.getAdmissionId());
//					}
					allergyPatientMapper.setComments(dto.getComments());
					allergyPatientMapper.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					allergyPatientMapper.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					allergyPatientMapper.setIsNoKnownAllergies(dto.getIsNoKnownAllergies());
					allergyPatientMapper.setMonth(dto.getMonth());
					allergyPatientMapper.setYear(dto.getYear());
					allergyPatientMapper.setNatureOfReaction(dto.getNatureOfReaction());
					allergyPatientMapper.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					allergyPatientMapper.setRemark(dto.getRemark());
					allergyPatientMapper.setSeverityId(dto.getSeverityId());
					allergyPatientMapper.setStatus(coversheetTemplateMasterDto.getStatus());
					allergyPatientMapper.setType(dto.getType());
					allergyPatientMapper.setUnitId(coversheetTemplateMasterDto.getUnitId());
					allergyPatientMapper.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					allergyPatientMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));

					AllergyPatientMapper historyId=update(allergyPatientMapper);
					
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
					
				}
						

			}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}
}
