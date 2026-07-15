package com.param.global.service;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.*;
import com.param.global.org.common.dto.PriorityMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dao.IMedicolegalDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IUnknownPatientRegistrationDao;
import com.param.global.utility.ErrorCorrelationUtil;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnknownPatientRegistrationServiceImpl
		implements IUnknownPatientRegistrationService, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(UnknownPatientRegistrationServiceImpl.class);


	@Autowired
	IUnknownPatientRegistrationDao iUnknownPatientRegistrationDao;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	IPatientRegistrationService patientRegistrationService;
	
	@Autowired
	IPatientRegistrationDao patientRegistrationDao;
	
	@Autowired
	IKinDetailsDao iKinDetailsDao;
	
	@Autowired
	IPatientSponsorDetailsService iPatientSponsorDetailsService;
	
	@Autowired
	IMedicolegalDao iMedicolegalDao;

	private Cache<String, List<PriorityMasterDto>> priorityMasterCache;

	@PostConstruct
	public void initCache() {
		priorityMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	public void invalidatePriorityMasterCache() {
		priorityMasterCache.invalidate("PriorityMaster");
	}


	@Override
	public Response getPriorityMasterList() throws ApplicationException {
		try {
			List<PriorityMasterDto> responseCache = priorityMasterCache.getIfPresent("PriorityMaster");
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iUnknownPatientRegistrationDao.getPriorityMasterList();
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				priorityMasterCache.put("PriorityMaster", res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		logger.error("getPriorityMasterList failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveUnknownPatient(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {
			
          int count=0;
			
          	if(unAdmissionDto.getPatientRegistrationDto()!=null)
          	{
          		Response res = patientRegistrationService.savePatientRegistration(unAdmissionDto.getPatientRegistrationDto());
          		unAdmissionDto.setPatientId((Integer)res.getObject());
          		unAdmissionDto.setVisitTypeId(ER); // For ER patients
				unAdmissionDto.setAdmissionTypeId(4); // For ER patients
				count=1;
          	}
          
          if(unAdmissionDto.getPatientRegistrationDto()==null) {
			if(!unAdmissionDto.gettUhid().equals("")||!unAdmissionDto.gettUhid().isEmpty())
				count=1;
			else
				count=0;
          }
			
			if(count==0)
			{
				PatientRegistrationDto patientDto = new PatientRegistrationDto();
					patientDto.setUhidNumber(iGlobalUnitMasterDao.getGeneratedGlobalNo(0, unAdmissionDto.getOrganizationId(),unAdmissionDto.getUnitId(), EMERGENCY_PATIENT_UHID));
					patientDto.setFirstName(unAdmissionDto.getPatientName());
					patientDto.setMiddleName(" ");
					patientDto.setLastName(" ");
					patientDto.setGenderId(unAdmissionDto.getGenderId());
					patientDto.setDob(unAdmissionDto.getDob());
					patientDto.setPrefixId(unAdmissionDto.getPrefixId());
					patientDto.setPatientCategoryId(unAdmissionDto.getPatientCategoryId());
					patientDto.setMobileNumber(unAdmissionDto.getMobileNo());
					patientDto.setRegistrationTypeId(4);
					patientDto.setOrganizationId(unAdmissionDto.getOrganizationId());
					patientDto.setUnitId(unAdmissionDto.getUnitId());
					patientDto.setCreatedBy(unAdmissionDto.getCreatedBy());
					patientDto.setUpdatedBy(unAdmissionDto.getUpdatedBy());
					patientDto.setStatus('A');
					patientDto.setIsPrimary('Y');
					patientDto.setKinDetailsId(unAdmissionDto.getKinDetailsId());
					patientDto.setIsVip('N');
					patientDto.setIsAbsconded('N');
				Response res =	patientRegistrationDao.savePatientRegistration(patientDto);
					patientDto = (PatientRegistrationDto) res.getObject();
					patientRegistrationDao.savePatientDetails(patientDto);
				
				unAdmissionDto.setPatientId(patientDto.getPatientId());
				unAdmissionDto.setVisitTypeId(4); // For ER patients
				unAdmissionDto.setAdmissionTypeId(4); // For ER patients
				count=1;
			}
			if(count==1) 
			{
				String admissionNumber = iGlobalUnitMasterDao.getGeneratedNo(0, unAdmissionDto.getUnitId(), IBillingCommonConstants.ADMISSION);	
					unAdmissionDto.setAdmissionNumber(admissionNumber!=null ? admissionNumber : this.getAdmissionNumber());
				Response res2 = iUnknownPatientRegistrationDao.saveERPatientAdmission(unAdmissionDto);
				String id = res2.getMessage();
				unAdmissionDto.setAdmissionId(Integer.parseInt(id));

				if (res2.getStatus().equals(SUCCESS)) 
				{
					unAdmissionDto.setAdmissionTypeId(4);
					
					Response res3= iUnknownPatientRegistrationDao.saveERPatientDetails(unAdmissionDto);
					
					unAdmissionDto.getPatientSponsorDetailsDtosList().forEach(psd -> psd.setApplicableDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unAdmissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss").substring(0,10).concat(" 00:00:00")));
					
					 iPatientSponsorDetailsService.savePatientSponsorDetails(unAdmissionDto.getPatientSponsorDetailsDtosList(), null,unAdmissionDto.getAdmissionId(),unAdmissionDto.getPayeeId());

//					iPatientSponsorDetailsService.savePatientSponsorDetails(sponsorList, null,unAdmissionDto.getAdmissionId());
					List<KinDetailsDto> kindetailList=unAdmissionDto.getKinArray();
					if(kindetailList.size()>0) {
						if(unAdmissionDto.getKinDetailsId() != 0) {
							for(KinDetailsDto kinDetailDto:kindetailList) {
								if (!kinDetailDto.getKinName().equals("")) {
									kinDetailDto.setKinDetailsId(unAdmissionDto.getKinDetailsId());
									kinDetailDto.setPatientId(unAdmissionDto.getPatientId());
									iKinDetailsDao.updateKinDetails(kinDetailDto);
								}
							}
						}else {
					for(KinDetailsDto kinDetailDto:kindetailList) {
						if (kinDetailDto.getKinName().equals(""))
							continue;
						kinDetailDto.setPatientId(unAdmissionDto.getPatientId());
						 iKinDetailsDao.saveKinDetails(kinDetailDto);
					}
					}
					}
//					if(unAdmissionDto.getIsMedicoLegal()=='Y' && res3.getStatus().equals(SUCCESS))
					if(res3.getStatus().equals(SUCCESS))
					{
						String erencounter = iGlobalUnitMasterDao.getGeneratedNo(0, unAdmissionDto.getUnitId(), ERENCOUNTER);
						unAdmissionDto.setMlcNo(erencounter);
						iUnknownPatientRegistrationDao.saveMedicoLegalDetails(unAdmissionDto);
						return res3;
					}
					else
					{
						return res3;
					}
				} else {
					return new Response(ERROR, null, "Error while adding in admission Details", null, null);
				}
			} else {
				return new Response(ERROR, null, "ERROR while adding admission", null, null);
			}

				//unAdmissionDto.settUhid(iGlobalUnitMasterDao.getGeneratedNo(0, unAdmissionDto.getUnitId(), EMERGENCY_PATIENT_UHID));
			//Response res = iUnknownPatientRegistrationDao.saveUnknownPatient(unAdmissionDto);
			//return new Response(SUCCESS, null, COMMON_SUCCESS, null, res.getObject());

		} catch (Exception e) {
		logger.error("saveUnknownPatient failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	protected String getUHID() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		StringBuilder salt = new StringBuilder("TUHID00");
		Random rnd = new Random();
		while (salt.length() < 10) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	@Override
	public Response saveERPatientAdmisson(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {
			String admissionNumber = iGlobalUnitMasterDao.getGeneratedNo(0, unAdmissionDto.getUnitId(), IBillingCommonConstants.ADMISSION);	
				unAdmissionDto.setAdmissionNumber(admissionNumber!=null ? admissionNumber : this.getAdmissionNumber());
			Response res = iUnknownPatientRegistrationDao.saveERPatientAdmission(unAdmissionDto);
			return new Response(SUCCESS, null, res.getMessage(), null, null);
		} catch (Exception e) {
		logger.error("saveERPatientAdmisson failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveERsavePatientDetails(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.saveERPatientDetails(unAdmissionDto);
		} catch (Exception e) {
		logger.error("saveERsavePatientDetails failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response patientSearchByMultipleCriteria(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteria(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchByMultipleCriteria failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}
	
	
	@Override
	public Response patientSearchByMultipleCriteriaForWalkInSales(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaForWalkInSales(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchByMultipleCriteriaForWalkInSales failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response patientSearchByMultipleCriteriaForIPD(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaForIPD(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchByMultipleCriteriaForIPD failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response patientSearchByMultipleCriteriaForOPBilling(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaForOPBilling(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchByMultipleCriteriaForOPBilling failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response saveMedicoLegalDetails(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {
			unAdmissionDto.setStatus('A');
			String erencounter = iGlobalUnitMasterDao.getGeneratedNo(0, unAdmissionDto.getUnitId(), ADMISSION);
			unAdmissionDto.setMlcNo(erencounter);
			return iUnknownPatientRegistrationDao.saveMedicoLegalDetails(unAdmissionDto);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Transactional
	public String getAdmissionNumber() {
		try {
			Long incrementedId = iUnknownPatientRegistrationDao.getNextValAdmissionId();
			String encouterPrefix = "ADM-18-";

			StringBuilder encounter = new StringBuilder(encouterPrefix);

			int length = Long.valueOf(incrementedId).toString().length();
			encounter = length == 1 ? (encounter.append("000000").append(incrementedId))
					: length == 2 ? (encounter.append("00000").append(incrementedId))
							: length == 3 ? (encounter.append("0000").append(incrementedId))
									: length == 4 ? (encounter.append("000").append(incrementedId))
											: length == 5 ? (encounter.append("00").append(incrementedId))
													: length == 6 ? (encounter.append("0").append(incrementedId))
															: length == 7 ? (encounter.append(incrementedId))
																	: encounter.append("");
			return encounter.toString();
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
		}
	}

	/*@Override
	public Response getListOfEncounterIdByPatientId(Integer patientId, Integer organizationId, Integer unitId,  String fromDate, String toDate)
			throws ApplicationException {
		return iUnknownPatientRegistrationDao.getListOfEncounterIdByPatientId(patientId, organizationId,unitId, fromDate, toDate);
	}*/

	@Override
	public Response getListOfDocumentForBookView(PatientSearchDto patientSearchDto) throws ApplicationException {
		return iUnknownPatientRegistrationDao.getListOfDocumentForBookView(patientSearchDto);
		
	}

	@Override
	public Response searchListOfPatientsByWardId(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.searchListOfPatientsByWardId(patientSearchDto);
		} catch (Exception e) {
		logger.error("searchListOfPatientsByWardId failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response patientSearchByForTransferICU(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByForTransferICU(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchByForTransferICU failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getListOfEncounterIdByPatientId(PatientSearchDto patientSearchDto)throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.getListOfEncounterIdByPatientId(patientSearchDto);
		} catch (Exception e) {
		logger.error("getListOfEncounterIdByPatientId failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMedicolegalDetailsForIcon(AdmissionDto admissionDto) throws ApplicationException {
		try {		
			return iUnknownPatientRegistrationDao.getMedicolegalDetailsForIcon(admissionDto);
		
		} catch (Exception e) {
		logger.error("getMedicolegalDetailsForIcon failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateMedicolegalDetails(AdmissionDto admissionDto) throws ApplicationException {
		try {
			// Validate string fields
			if (!TimeZoneComponent.isValidText(admissionDto.getAccompaniedBy())
					|| !TimeZoneComponent.isValidText(admissionDto.getFirNo())
					|| !TimeZoneComponent.isValidText(admissionDto.getPoliceBuckleNo())
					|| !TimeZoneComponent.isValidText(admissionDto.getPoliceStationName())
					|| !TimeZoneComponent.isValidText(admissionDto.getRemark())) {

				return new Response(
						ERROR,
						ERROR_CODE,
						"Invalid input detected",
						null,
						null
				);
			}
			return iMedicolegalDao.updateMedicolegalDetails(admissionDto);
		
		} catch (Exception e) {
		logger.error("updateMedicolegalDetails failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientListMedicolegalDetailsMLCWorklist(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.getPatientListMedicolegalDetailsMLCWorklist(admissionDto);
		
		} catch (Exception e) {
		logger.error("getPatientListMedicolegalDetailsMLCWorklist failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMedicolegalDetailsMLCWorklistCount(AdmissionDto admissionDto) throws ApplicationException {
		try {
			
			return iUnknownPatientRegistrationDao.getMedicolegalDetailsMLCWorklistCount(admissionDto);
	
	} catch (Exception e) {
	logger.error("getMedicolegalDetailsMLCWorklistCount failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response searchListOfPatientsByDoctorIdForIOChart(PatientSearchDto patientSearchDto)
			throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.searchListOfPatientsByDoctorIdForIOChart(patientSearchDto);
		} catch (Exception e) {
		logger.error("searchListOfPatientsByDoctorIdForIOChart failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response patientSearchByMultipleCriteriaWithDeathPatient(PatientSearchDto patientSearchDto)
			throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaWithDeathPatient(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchByMultipleCriteriaWithDeathPatient failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	@Transactional
	public Response patientSearchByMultipleCriteriaGlobal(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaGlobal(patientSearchDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getIpRenderedDateChange(PatientSearchDto patientSearchDto) {
		return iUnknownPatientRegistrationDao.getIpRenderedDateChange(patientSearchDto);
	}

	@Override
	public Response patientSearchByMultipleCriteriaForICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaForICUAndER(patientSearchDto);
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response patientSearchByMultipleCriteriaNabh(PatientSearchDto patientSearchDto) {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaNabh(patientSearchDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	public Response patientSearchForIPDNutritionalAssessment(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchForIPDNutritionalAssessment(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchForIPDNutritionalAssessment failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}
	

	@Override
	public Response patientSearchByICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException {
		try {
			return iUnknownPatientRegistrationDao.patientSearchByICUAndER(patientSearchDto);
		} catch (Exception e) {
		logger.error("patientSearchByICUAndER failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response patientSearchByMultipleCriteriaGrowthChart(PatientSearchDto patientSearchDto) throws ApplicationException {
		{
			try {
				return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteriaGrowthChart(patientSearchDto);
			} catch (Exception e) {
			String correlationId = ErrorCorrelationUtil.newCorrelationId();
			logger.error(ErrorCorrelationUtil.tag(correlationId, "patientSearchByMultipleCriteriaGrowthChart failed"), e);
			return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
			}
		}
	}

    @Override
    public Response getAppointmentsByPatientId(PatientSearchDto patientSearchDto) throws ApplicationException {
        try {
            return iUnknownPatientRegistrationDao.getAppointmentsByPatientId(patientSearchDto);
        } catch (Exception e) {
        logger.error("getAppointmentsByPatientId failed", e);
        }
    return new Response(ERROR, null, null, null, null);
    }

	@Override
	public Response updateMLCFLag(AdmissionDto admissionDto) {
		try {
			Response res = iUnknownPatientRegistrationDao.updateMLCFLag(admissionDto);
			Integer count  = iUnknownPatientRegistrationDao.getMLCcountForAdmission(admissionDto);
			if(SUCCESS.equals(res.getStatus()) && admissionDto.getIsMedicoLegal()=='Y' && count==0){
				String erencounter = iGlobalUnitMasterDao.getGeneratedNo(0, admissionDto.getUnitId(), ERENCOUNTER);
				admissionDto.setMlcNo(erencounter);
				iUnknownPatientRegistrationDao.saveMedicoLegalDetails(admissionDto);
			}
		} catch (Exception e) {
		logger.error("updateMLCFLag failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
