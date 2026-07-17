package com.param.adt.admission.service;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.adt.master.global.dto.BedStatusMasterDto;
import com.param.global.dto.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.param.adt.admission.dao.IBedAllocationDao;
import com.param.adt.admission.dto.BedAllocationDto;
import com.param.adt.admission.dto.NursingPatientAcceptanceDto;
import com.param.adt.admission.model.Admission;
import com.param.adt.common.IADTConstants;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedCategoryMasterDto;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.unit.dao.IWardMasterDao;
import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.adt.transfer.dao.IBedToBedTransferDao;
import com.param.adt.transfer.dto.TransferDto;
import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.global.VisitorDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IPatientSponsorDetailsService;
import com.param.global.service.IReferalDetailsService;
import com.param.global.service.IReferralMasterService;
import com.param.global.service.IUnknownPatientRegistrationService;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BedAllocationServiceImpl implements IBedAllocationService, ICommonConstants,IADTConstants{

	private static final Logger logger = Logger.getLogger(BedAllocationServiceImpl.class);

	@Autowired
	IBedAllocationDao iBedAllocationDao;
	@Autowired
	IBedToBedTransferDao iBedToBedTransferDao;
	@Autowired
	IWardMasterDao iWardMasterDao;
	@Autowired
	IDepositMasterService iDepositMasterService;
	@Autowired
	IKinDetailsDao iKinDetailsDao;
	@Autowired
	IPatientSponsorDetailsService iPatientSponsorDetailsService;
	@Autowired
	IReferalDetailsService iReferalDetailsService;
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	@Autowired
	IUnknownPatientRegistrationService iUnknownPatientRegistrationService;
	@Autowired
	IReferralMasterService iReferralMasterService;
	@Autowired 
	IContractConsumptionService iContractConsumptionService;
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao; 
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	@Autowired
	IOrderMasterService iOrderMasterService; 
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	IBillingService globalBillingService;
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	IPassIssuedService iPassIssuedService;

	private Cache<String, List<BedStatusMasterDto>> bedStatusMasterCache;

	@PostConstruct
	public void initCache() {
		bedStatusMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}


	@Override
	public Response getActiveBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			if(bedMasterDto.getTransferTypeId()==null)
			{
				Response res = iBedAllocationDao.getActiveBedList(bedMasterDto);
				List<BedMasterDto> dtosList = res.getListObject();
				for(BedMasterDto dto : dtosList)
				{
					com.param.global.common.Response res2 = iWardMasterDao.getGenderListByWardId(dto.getWardId());
					dto.setGenderIdList(res2.getListObject());
				}
				return res;
			}
			else if(bedMasterDto.getTransferTypeId()==1)
			{
				Response res = iBedAllocationDao.getActiveBedList(bedMasterDto);
				List<BedMasterDto> dtosList = res.getListObject();
				for(BedMasterDto dto : dtosList)
				{
					com.param.global.common.Response res2 = iWardMasterDao.getGenderListByWardId(dto.getWardId());
					dto.setGenderIdList(res2.getListObject());
				}
				return res;
			}else
			{
				Response res =  iBedToBedTransferDao.getBedListByWardId(bedMasterDto);
				List<BedMasterDto> dtosList = res.getListObject();
				for(BedMasterDto dto : dtosList)
				{
					com.param.global.common.Response res2 = iWardMasterDao.getGenderListByWardId(dto.getWardId());
					dto.setGenderIdList(res2.getListObject());
				}
				return res;
			}
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveFloorMasterList(BedAllocationDto bedAllocationDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getActiveFloorMasterList(bedAllocationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardListByFloorId(BedAllocationDto bedAllocationDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getWardListByFloorId(bedAllocationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationListByWard(BedAllocationDto bedAllocationDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getNursingStationListByWard(bedAllocationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedCategoryList(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getBedCategoryList(bedCategoryMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedStatusList() throws ApplicationException {
		try {
			List<BedStatusMasterDto> responseCache = bedStatusMasterCache.getIfPresent("bedStatusList");
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iBedAllocationDao.getBedStatusList();
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				bedStatusMasterCache.put("bedStatusList", res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMealPreference(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getMealPreference(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Transactional
	@Override
	public Response savePatientAdmission(AdmissionDto admissionDto) throws ApplicationException {
		try {
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(admissionDto.getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(admissionDto.getUnitId());
				com.param.global.common.Response appConfigRes= iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				if(appConfigRes.getObject()!=null)
				{
					unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
					
					if(unitApplicationConfigurationDto.getDefaultSelfTariffId()==null)
						return new Response<>(ERROR, null, "Default tariff is not defined !!", null, null);
					else
						admissionDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
					
					if(unitApplicationConfigurationDto.getAdmissionChargesServiceId()!=null)
						admissionDto.setServiceId(unitApplicationConfigurationDto.getAdmissionChargesServiceId());
						
					if(unitApplicationConfigurationDto.getSelfPayeeId()==null)
						return new Response<>(ERROR, null, "Self Payee is not defined !!", null, null);
					else
						admissionDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
					
					if(unitApplicationConfigurationDto.getSkipAdmissionAcceptance()!=null)
						admissionDto.setSkipAdmissionAcceptance(unitApplicationConfigurationDto.getSkipAdmissionAcceptance());
					else
						admissionDto.setSkipAdmissionAcceptance('N');
					
					if(unitApplicationConfigurationDto.getBiomedicalsChargesServiceId()!=null)
						admissionDto.setBiomedicalsChargesServiceId(unitApplicationConfigurationDto.getBiomedicalsChargesServiceId());
					if(unitApplicationConfigurationDto.getMrdChargesServiceId()!=null)
						admissionDto.setMrdChargesServiceId(unitApplicationConfigurationDto.getMrdChargesServiceId());
					
					if(unitApplicationConfigurationDto.getHospitalChargesServiceId()!=null)
						admissionDto.setHospitalChargesServiceId(unitApplicationConfigurationDto.getHospitalChargesServiceId());	
					
					
					
				}else{
					return new Response<>(ERROR, null, "Application Configuration is yet to set !!", null, null);
				}
				
			
			if(iBedAllocationDao.isPatientAlreadyAdmitted(admissionDto.getPatientId(),admissionDto.getOrganizationId(),admissionDto.getUnitId())==TRUE)
			{
				return new Response(ERROR, null, "Patient is already admitted !!", null, null);
			}
			else
			{
				Iterator<KinDetailsDto> itrKinDetailsDto = admissionDto.getKinArray().iterator();
				int index = 0;
				while (itrKinDetailsDto.hasNext()) {
					KinDetailsDto kinDetailsDto = itrKinDetailsDto.next();
					if (kinDetailsDto.getKinDetailsId() == 0) {
						if (kinDetailsDto.getKinName().equals(""))
							continue;
						kinDetailsDto.setPatientId(admissionDto.getPatientId());
						com.param.global.common.Response res2 = iKinDetailsDao.saveKinDetails(kinDetailsDto);
						if (index == 0) {
							if (admissionDto.getKinDetailsId() == null) {
								Integer kinDetailsId = (Integer) res2.getObject();
								admissionDto.setKinDetailsId(kinDetailsId);
							}
						}
					} else {
						iKinDetailsDao.updateKinDetails(kinDetailsDto);
						admissionDto.setKinDetailsId(kinDetailsDto.getKinDetailsId());
					}
					index++;
				}
			/*	if (admissionDto.getKinDetailsId() != null && admissionDto.getKinDetailsId() > 0) {*/
	
					String admisisonNumber = iGlobalUnitMasterDao.getGeneratedNo(0, admissionDto.getUnitId(), IBillingCommonConstants.ADMISSION);
						admissionDto.setAdmissionNumber(admisisonNumber!=null ? admisisonNumber : iUnknownPatientRegistrationService.getAdmissionNumber());
						if(unitApplicationConfigurationDto.getIsBackDatedAdmission()!= null && unitApplicationConfigurationDto.getIsBackDatedAdmission()== 'N'){
							Date date = TimeZoneComponent.getDateByzone(admissionDto.getUnitId());
							String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
							admissionDto.setDoa(dateTime);
							admissionDto.setCreatedDate(dateTime);
							admissionDto.setUpdatedDate(dateTime);
						}
					Response res = iBedAllocationDao.savePatientAdmission(admissionDto);
						admissionDto.setAdmissionId((Integer) res.getObject());
						admissionDto.setActiveStatus('B'); // B----> Booked
						admissionDto.setBedStatusId(4); // 5----> Booked
					if (res.getStatus().equals(SUCCESS)) {
						Response res2 = iBedAllocationDao.savePatientDetails(admissionDto);
						
						
						 if(admissionDto.getReferralMasterDto()!=null)
							{
								com.param.global.common.Response refRess = iReferralMasterService.saveReferralMaster(admissionDto.getReferralMasterDto());
								if(refRess.getStatus().equals(SUCCESS) && refRess.getCode() !=null && refRess.getCode().equals("1")) {
									ReferralMasterDto dto = (ReferralMasterDto)refRess.getObject();
									
									admissionDto.setReferralMasterDto(dto);
									admissionDto.setReferralId(dto.getReferralId());
									admissionDto.setReferralTypeId(dto.getReferralTypeId());
								}else {
									ReferralMasterDto dto = (ReferralMasterDto)refRess.getObject();
									
									admissionDto.setReferralMasterDto(dto);
									admissionDto.setReferralId(dto.getReferralId());
									admissionDto.setReferralTypeId(dto.getReferralTypeId());
								}
							
							}
						 
						 
						if (admissionDto.getReferralTypeId() != null && admissionDto.getReferralId() != null) {
							ReferalDetailsDto referalDetailsDto = new ReferalDetailsDto();
								referalDetailsDto.setOrganizationId(admissionDto.getOrganizationId());
								referalDetailsDto.setUnitId(admissionDto.getUnitId());
								referalDetailsDto.setVisitTypeId(admissionDto.getVisitTypeId());
								referalDetailsDto.setAdmissionId(admissionDto.getAdmissionId());
								referalDetailsDto.setReferralId(admissionDto.getReferralId());
								referalDetailsDto.setReferralTypeId(admissionDto.getReferralTypeId());
								referalDetailsDto.setCreatedBy(admissionDto.getCreatedBy());
								referalDetailsDto.setReferralRemark(admissionDto.getReferralRemark());
								referalDetailsDto.setCreatedDate(admissionDto.getCreatedDate());
								referalDetailsDto.setUpdatedBy(admissionDto.getUpdatedBy());
								referalDetailsDto.setUpdatedDate(admissionDto.getUpdatedDate());
								referalDetailsDto.setStatus('A');
							iReferalDetailsService.saveReferalDetails(referalDetailsDto);
						}
						
						if (res2.getStatus().equals(SUCCESS)) 
						{
							TransferDto transferDto = new TransferDto();
								transferDto.setOrganizationId(admissionDto.getOrganizationId());
								transferDto.setUnitId(admissionDto.getUnitId());
								transferDto.setAdmissionId(admissionDto.getAdmissionId());
								transferDto.setFromBedCategoryId(admissionDto.getBedCategoryId());
								transferDto.setFromBillingBedCategoryId(admissionDto.getBillingBedCategoryId());
								transferDto.setFromWardId(admissionDto.getWardId());
								transferDto.setFromRoomId(admissionDto.getRoomId());
								transferDto.setFromBedId(admissionDto.getBedId());
								transferDto.setFromDate(admissionDto.getCreatedDate());
								transferDto.setFromTime(admissionDto.getCreatedDate());
								transferDto.setToBillingBedCategoryId(admissionDto.getBillingBedCategoryId());
								transferDto.setToWardId(admissionDto.getWardId());
								transferDto.setToRoomId(admissionDto.getRoomId());
								transferDto.setToBedId(admissionDto.getBedId());
								transferDto.setToDate(admissionDto.getCreatedDate());
								transferDto.setToTime(admissionDto.getCreatedDate());
								transferDto.setCreatedBy(admissionDto.getCreatedBy());
								transferDto.setCreatedDate(admissionDto.getCreatedDate());
								transferDto.setUpdatedBy(admissionDto.getUpdatedBy());
								transferDto.setUpdatedDate(admissionDto.getUpdatedDate());
								transferDto.setStatus('A');
								transferDto.setTransferStatusId(1);
								transferDto.setTransferTypeId(1);
								transferDto.setTreatingDoctorId(admissionDto.getDoctorId());
								transferDto.setTreatingDoctorSpecialityId(admissionDto.getSpecialityId());
							Response res3 = iBedAllocationDao.saveTransfer(transferDto);
	
							if (res3.getStatus().equals(SUCCESS)) 
							{
								admissionDto.getPatientSponsorDetailsDtosList().forEach(psd -> psd.setApplicableDate(admissionDto.getDoa().substring(0,10).concat(" 00:00:00")));
								
								iPatientSponsorDetailsService.savePatientSponsorDetails(admissionDto.getPatientSponsorDetailsDtosList(), null,admissionDto.getAdmissionId(),unitApplicationConfigurationDto.getSelfPayeeId());
							 	
							 	if (admissionDto.getBedCategoryWaitingListId() != null) 
							 	{
									updateWatingListNumber(admissionDto);
								}
							 	if(admissionDto.getReqDto()!=null && admissionDto.getReqDto().getDepositMasterDto()!=null)
							 	{
							 		admissionDto.getReqDto().getDepositMasterDto().setVisitTypeId(admissionDto.getVisitTypeId());
							 		admissionDto.getReqDto().getDepositMasterDto().setEncounterId(admissionDto.getAdmissionId());
							 		admissionDto.getReqDto().getDepositMasterDto().setAdmissionId(admissionDto.getAdmissionId());
							 		admissionDto.getReqDto().getDepositMasterDto().setDepositTypeId(2);//encounter
							 		com.param.global.common.Response depositRes = iDepositMasterService.addDepositAgainstAdmission(admissionDto.getReqDto());
							 		Integer depositId = (Integer)depositRes.getObject();
							 		admissionDto.getReqDto().getDepositMasterDto().setDepositMasterId(depositId);
							 	}
								if(admissionDto.getIsMedicoLegal()=='Y') 
								{
									if(admissionDto.getMlcRemark() != null){
										admissionDto.setRemark(admissionDto.getMlcRemark());
									}
									if(admissionDto.getmodeOfArrival() != null){
										admissionDto.setmodeOfArrival(admissionDto.getmodeOfArrival());
									}
									 iUnknownPatientRegistrationService.saveMedicoLegalDetails(admissionDto);
								}
								
								if(admissionDto.getListVisiterPassMaster()!=null)
								{
									VisitorDto visitorDto = new VisitorDto();
										visitorDto.setAdmissionId(admissionDto.getAdmissionId());
										visitorDto.setOrganizationId(admissionDto.getOrganizationId());
										visitorDto.setUnitId(admissionDto.getUnitId());
										visitorDto.setExpiryDate(admissionDto.getPdd());
										visitorDto.setCreatedBy(admissionDto.getCreatedBy());
										visitorDto.setUpdatedBy(admissionDto.getUpdatedBy());
										visitorDto.setStatus('A');
										visitorDto.setVisitorPassStatus('I');
										visitorDto.setVisitTypeId(admissionDto.getVisitTypeId());
										visitorDto.setBedCategoryId(admissionDto.getBedCategoryId());
										visitorDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
										visitorDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
										visitorDto.setListVisiterPassMaster(admissionDto.getListVisiterPassMaster());
										visitorDto.setPatientId(admissionDto.getPatientId());
									iPassIssuedService.saveVisitorAgainsPatient(visitorDto);
								}
							}
						}
						
					} else {
						return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
					}
	
				/*} else {
					return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
				}*/
					
				return new Response(SUCCESS, null, COMMON_SUCCESS, null,admissionDto);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}


	

	@Override
	public Response getBedListByMultipleCriteria(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getBedListByMultipleCriteria(bedMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response updateWatingListNumber(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Response res = iBedAllocationDao.getWatingListNumbers(admissionDto);
			if(res.getStatus().equals(SUCCESS) && !res.getListObject().isEmpty())
			{
				Iterator<AdmissionDto> itr = res.getListObject().iterator();
			 
				while (itr.hasNext()) 
				{
					
					AdmissionDto bedCategoryWaitingList =new AdmissionDto();
			    
					AdmissionDto obj = itr.next();
						bedCategoryWaitingList.setOrganizationId(obj.getOrganizationId());
						bedCategoryWaitingList.setUnitId(obj.getUnitId());
						bedCategoryWaitingList.setAdmissionNoteId(obj.getAdmissionNoteId());
						bedCategoryWaitingList.setBedCategoryId(obj.getBedCategoryId());
						bedCategoryWaitingList.setDoa(admissionDto.getDoa());
						bedCategoryWaitingList.setWaitListNumber(obj.getWaitListNumber()-1);
						bedCategoryWaitingList.setStatus('A');
						bedCategoryWaitingList.setCreatedBy(admissionDto.getUpdatedBy());
						bedCategoryWaitingList.setUpdatedBy(admissionDto.getUpdatedBy());
						bedCategoryWaitingList.setCreatedDate(admissionDto.getUpdatedDate());
						bedCategoryWaitingList.setUpdatedDate(admissionDto.getUpdatedDate());
					iBedAllocationDao.updateWatingListNumber(bedCategoryWaitingList);
			  }
				
			}
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Boolean isPatientAlreadyAdmitted(int patientId, int orgId, int unitId) throws ApplicationException {
		try {
			return iBedAllocationDao.isPatientAlreadyAdmitted(patientId,orgId,unitId);
		} catch (Exception e) {
			
		}
		return false;
	}
	
	@Override
	@Transactional
	public Response postAutorenderServices(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return updateAutorenderServices(admissionDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	
	@Transactional
	@Override
	public Response postAdmissionServiceCharges(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return saveAdmissionCharges(admissionDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
@Override
public Response updateAutorenderServices(AdmissionDto admissionDto){
		
		try {
			//--------get Auto-Rendered Services---------//
			OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
			
			EncounterMasterDto dto = new EncounterMasterDto();
				dto.setOrganizationId(admissionDto.getOrganizationId());
				dto.setUnitId(admissionDto.getUnitId());
				dto.setPatientId(admissionDto.getPatientId());
				dto.setVisitTypeId(admissionDto.getVisitTypeId());
			List<ServiceMasterDto> listServiceMasterDto=iEncounterMasterDao.getAutoRenderedServicesByPatient(dto).getListObject();
			if(listServiceMasterDto.size()>0)
			{
				ListIterator<ServiceMasterDto> iterator = listServiceMasterDto.listIterator();
					while(iterator.hasNext())
					{
						ServiceMasterDto masterDto = iterator.next();
						ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
							consumptionDto.setOrganizationId(admissionDto.getOrganizationId());
							consumptionDto.setUnitId(admissionDto.getUnitId());
//							consumptionDto.setVisitTypeId(2);
							//21-01-2020
							consumptionDto.setVisitTypeId(admissionDto.getVisitTypeId());
							consumptionDto.setPatientTypeId(admissionDto.getPatientCategoryId());
							consumptionDto.setBillingBedCategoryId(admissionDto.getBillingBedCategoryId());
							consumptionDto.setPaymentEntitlementId(SELF);
							consumptionDto.setServiceId(masterDto.getServiceMasterId());
							consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "yyyy-MM-dd"));
							consumptionDto.setContractId(admissionDto.getContractId());
							consumptionDto.setAdmissionId(admissionDto.getAdmissionId());
							consumptionDto.setEncounterId(admissionDto.getAdmissionId());
							consumptionDto.setDefaultSelfTariffId(admissionDto.getDefaultSelfTariffId());
							consumptionDto.setSelfPayeeId(admissionDto.getPayeeId());
							consumptionDto.setPatientId(admissionDto.getPatientId());
							consumptionDto.setPatientClassId(admissionDto.getPatientClassId());
						com.param.global.common.Response res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto=(ContractConsumptionDto)res.getObject();
				
					if(res.getObject()!=null)
					{
							orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getSpecialityId()!=null ? masterDto.getSpecialityId() : masterDto.getServiceRenderingDeptId());
//							orderDetailsMasterDto.setVisitTypeId(2);
							//21-01-2020
							orderDetailsMasterDto.setVisitTypeId(admissionDto.getVisitTypeId());
							orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
							orderDetailsMasterDto.setAdmissionId(admissionDto.getAdmissionId());
							orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
							orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(admissionDto.getDefaultSelfTariffId());
							orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							orderDetailsMasterDto.setServiceRendered(1);
							orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()).getTime());
							orderDetailsMasterDto.setServiceChargeable(1);
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setPriorityId(2);
							orderDetailsMasterDto.setIsOutsourced('N');
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
						
						orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
						orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
						orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
						orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
						orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId()!=null ? consumptionDto.getPayeeId():admissionDto.getPayeeId());
						orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
						orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
						orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
					    orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));

						if(consumptionDto.getPayeeId()==admissionDto.getPayeeId() && consumptionDto.getContractId()!=null)
						{
							List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
							OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
								detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
								detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
								detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
							list.add(detailsPayeeMapperDto);	
							orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
						}else
							orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
						
						if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
							orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						
						iEncounterMasterDao.updateAutorenderServices(orderDetailsMasterDto);
						return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
						
					}
				}
			}
			
			
			return new Response<>(SUCCESS, null, null, listServiceMasterDto, null);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public com.param.global.common.Response test() throws ApplicationException 
{
	try {
		return  null;
	} catch (Exception e) {
		
	}
	return null;
}

@Override
public Response getPatientAcceptanceListForNursing(NursingPatientAcceptanceDto nursingPatientAcceptanceDto) throws ApplicationException {
	try {
		return  iBedAllocationDao.getPatientAcceptanceListForNursing(nursingPatientAcceptanceDto);
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response acceptNursingAcceptanceRequet(AdmissionDto admission) throws ApplicationException {
	try {
		Response res =   iBedAllocationDao.acceptNursingAcceptanceRequet(admission);
		if(admission.getOrderDetailsIds()!=null && admission.getOrderDetailsIds().size()>0)
		{
			ConvertServicesFromOptoIpDto costEstimationDto = new ConvertServicesFromOptoIpDto();
				costEstimationDto.setOrderDetailsIds(admission.getOrderDetailsIds());
				costEstimationDto.setEncounterId(admission.getEncounterId());
				costEstimationDto.setAdmissionId(admission.getAdmissionId());
				costEstimationDto.setUserId(admission.getUpdatedBy());
				costEstimationDto.setVisitTypeId(admission.getVisitTypeId());
				costEstimationDto.setPatientId(admission.getPatientId());
				costEstimationDto.setOrganizationId(admission.getOrganizationId());
				costEstimationDto.setUnitId(admission.getUnitId());
				
			globalBillingService.convertServicesFromOPToIP(costEstimationDto);
		}

		return res;

	} catch (Exception e) {
		logger.error("acceptNursingAcceptanceRequet failed", e);
	}
	return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response rejectNursingAcceptanceRequet(AdmissionDto admission) throws ApplicationException {
	try {
		return  iBedAllocationDao.rejectNursingAcceptanceRequet(admission);
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
}

private Response saveAdmissionCharges(AdmissionDto admissionDto)
{
	try{
		
		Long newDate = TimeZoneComponent.getDateByzone(admissionDto.getUnitId()).getTime();
		
		ContractConsumptionDto consumptionDto = null;
		List orderDetailsList = new LinkedList<>();
		OrderMasterDto orderMasterDto = new OrderMasterDto();
			orderMasterDto.setVisitTypeId(IP);// IP==1
			orderMasterDto.setAdmissionId(admissionDto.getAdmissionId());
			orderMasterDto.setPatientId(admissionDto.getPatientId());
			orderMasterDto.setCreatedBy(admissionDto.getCreatedBy());
			orderMasterDto.setUpdatedBy(admissionDto.getUpdatedBy());
			orderMasterDto.setCreatedDate(newDate);
			orderMasterDto.setUpdatedDate(newDate);
			orderMasterDto.setOrderDate(newDate);
			orderMasterDto.setOrderStatus('A');
			orderMasterDto.setOrgId(admissionDto.getOrganizationId());
			orderMasterDto.setOrgUnitId(admissionDto.getUnitId());
			orderMasterDto.setOrderStatusId(1);// order status==1
			orderMasterDto.setPatientTypeId(admissionDto.getPatientCategoryId());
			orderMasterDto.setBillingBedCategoryId(admissionDto.getBillingBedCategoryId());
			orderMasterDto.setPaymentEntitlementTypeId(admissionDto.getPaymentEntitlementId());
			orderMasterDto.setReferralId(admissionDto.getReferralId());
			orderMasterDto.setDefaultDiscountOwnership(admissionDto.getDefaultDiscountOwnership());
			if(admissionDto.getDiagnosisServiceList()!=null)
			{
				for(ServiceOrdDto dto : admissionDto.getDiagnosisServiceList())
				{
					consumptionDto = new ContractConsumptionDto();
					consumptionDto.setOrganizationId(admissionDto.getOrganizationId());
					consumptionDto.setUnitId(admissionDto.getUnitId());
					consumptionDto.setVisitTypeId(admissionDto.getVisitTypeId());
					consumptionDto.setPatientTypeId(admissionDto.getPatientCategoryId());
					consumptionDto.setBillingBedCategoryId(admissionDto.getBillingBedCategoryId());
					consumptionDto.setPaymentEntitlementId(SELF);
					consumptionDto.setServiceId(dto.getServiceId());
					consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId():0), "yyyy-MM-dd"));
					consumptionDto.setContractId(admissionDto.getContractId());
					consumptionDto.setAdmissionId(admissionDto.getAdmissionId());
					consumptionDto.setEncounterId(admissionDto.getAdmissionId());
					consumptionDto.setDefaultSelfTariffId(admissionDto.getDefaultSelfTariffId());
					consumptionDto.setSelfPayeeId(admissionDto.getPayeeId());
					consumptionDto.setPatientId(admissionDto.getPatientId());
					consumptionDto.setPatientClassId(admissionDto.getPatientClassId());
					
					com.param.global.common.Response  res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
					consumptionDto=(ContractConsumptionDto)res.getObject();
							
					if(res.getObject()!=null && consumptionDto.getFinalPrice()>0)
					{
						//-------order details-------//
						OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
							orderDetailsMasterDto.setServiceId(dto.getServiceId());
							
							if(dto.getOrderSetId()!=null)
								orderDetailsMasterDto.setOrderSetId(dto.getOrderSetId());
							if(dto.getIsInvestigationOrder()!=null)
								orderDetailsMasterDto.setIsInvestigationOrder('Y');
							
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setOrderQty(1);
							orderDetailsMasterDto.setCreatedBy(admissionDto.getCreatedBy());
							orderDetailsMasterDto.setUpdatedBy(admissionDto.getUpdatedBy());
							orderDetailsMasterDto.setOrderDate(newDate);
							orderDetailsMasterDto.setOrderSchDate(newDate);
							orderDetailsMasterDto.setOrdRenderDatetime(newDate);
							orderDetailsMasterDto.setCreatedDate(newDate);
							orderDetailsMasterDto.setUpdatedDate(newDate);
							orderDetailsMasterDto.setStatus('A');
							orderDetailsMasterDto.setOrgId(admissionDto.getOrganizationId());
							orderDetailsMasterDto.setOrgUnitId(admissionDto.getUnitId());
							orderDetailsMasterDto.setOrdCancelled('N');
							orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
							orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(admissionDto.getDefaultSelfTariffId());
							orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							orderDetailsMasterDto.setServiceRendered(admissionDto.getServiceId().equals(dto.getServiceId()) ? 1 : 0);
							orderDetailsMasterDto.setOrdRenderDatetime(newDate);
							orderDetailsMasterDto.setServiceChargeable(1);
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setPriorityId(2);
							orderDetailsMasterDto.setServiceRenderingDeptId(consumptionDto.getSubSpecialityId()!= null ? consumptionDto.getSubSpecialityId() : null);
							orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
							orderDetailsMasterDto.setIsOutsourced('N');
							orderDetailsMasterDto.setDepartmentId(consumptionDto.getSpecialityId());
							orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
							orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
							orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
							orderDetailsMasterDto.setDiscountOwnership(admissionDto.getDefaultDiscountOwnership());
							orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
							if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
								orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
							
							orderDetailsList.add(orderDetailsMasterDto);
					}
				}
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
			}
			
			if(orderMasterDto.getListOrderDetailsMasterDto()!=null && orderMasterDto.getListOrderDetailsMasterDto().size()>0)
				iOrderMasterService.saveOrderMaster(orderMasterDto);
				
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
		}
	return new Response<>(ERROR, null, null, null, null);
	}

@Override
public Response getActiveLaborBedList(BedMasterDto bedMasterDto) throws ApplicationException {
	try{
		return iBedAllocationDao.getActiveLaborBedList(bedMasterDto);
	} catch (Exception e) {
		
	}
  return new Response<>(ERROR, null, null, null, null);
}

@Override
public Response getActiveHemodialysisBedList(BedMasterDto bedMasterDto) throws ApplicationException {
	try{
		return iBedAllocationDao.getActiveHemodialysisBedList(bedMasterDto);
	} catch (Exception e) {
		
	}
  return new Response<>(ERROR, null, null, null, null);
}

@Override
public Response getPatientAcceptanceListForNursingCount(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)
		throws ApplicationException {
	try{
		return iBedAllocationDao.getPatientAcceptanceListForNursingCount(nursingPatientAcceptanceDto);
	} catch (Exception e) {
		
	}
  return new Response<>(ERROR, null, null, null, null);
}

	@Override
	public Response getcheckBedAllocation(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getcheckBedAllocation(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, null, null, null);

	}

	@Override
	public Response isBedBookedOrNot(AdmissionDto admission) throws ApplicationException {
		try {
			List<BedMasterDto> BedMasterDtosList =null;
			if(admission.getBedId()!=null){
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT bm.bedId as bedId FROM BedMaster bm "
					+ "WHERE bm.bedId=:bedId "
					+ "AND bm.bedStatusId=3 ");
			
			BedMasterDtosList = query
					.setParameter("bedId", admission.getBedId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			}
			return new Response(SUCCESS, null, null, BedMasterDtosList,null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response universalSearchForNursingAcceptance(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)
			throws ApplicationException {
		try {
			return  iBedAllocationDao.universalSearchForNursingAcceptance(nursingPatientAcceptanceDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmisssoinTAT(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iBedAllocationDao.getAdmisssoinTAT(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNotRequiredServices(AdmissionDto admission) throws ApplicationException {
		try {
			return iBedAllocationDao.updateNotRequiredServices(admission);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRemarkAdmissionDetailsByAdissionId(AdmissionDto admission) throws ApplicationException {
		try {
			return iBedAllocationDao.updateRemarkAdmissionDetailsByAdissionId(admission);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response editAdmissionDetailsByAdissionIdAdmiitedPatientList(AdmissionDto admissionDto)
			throws ApplicationException {
		try {
			
		Admission adm =	iBedAllocationDao.getAdmissionByAdissionId(admissionDto);
		adm.setReferenceNumber(admissionDto.getReferenceNumber());
		adm.setReasonId(admissionDto.getReasonId());
	
		if(admissionDto.getListVisiterPassMaster()!=null)
		{
			    VisitorDto visitorDto = new VisitorDto();
				visitorDto.setAdmissionId(admissionDto.getAdmissionId());
				visitorDto.setOrganizationId(admissionDto.getOrganizationId());
				visitorDto.setUnitId(admissionDto.getUnitId());
//				visitorDto.setExpiryDate(admissionDto.getPdd());
				visitorDto.setCreatedBy(admissionDto.getCreatedBy());
				visitorDto.setUpdatedBy(admissionDto.getUpdatedBy());
				visitorDto.setStatus('A');
				visitorDto.setVisitorPassStatus('I');
				visitorDto.setVisitTypeId(admissionDto.getVisitTypeId());
				visitorDto.setBedCategoryId(admissionDto.getBedCategoryId());
				visitorDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId():0), "dd-MM-yyyy HH:mm:ss"));
				visitorDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId() != null ? admissionDto.getUnitId():0), "dd-MM-yyyy HH:mm:ss"));
				visitorDto.setListVisiterPassMaster(admissionDto.getListVisiterPassMaster());
			 iPassIssuedService.saveOrupdateVisitorAgainsPatient(visitorDto);
		}
		
		if(admissionDto.getIsMedicoLegal()=='Y') 
		{
			if(admissionDto.getMedicoLegalId() == null) {
			 iUnknownPatientRegistrationService.saveMedicoLegalDetails(admissionDto);
			}
			if(admissionDto.getMedicoLegalId() != null) {
			 iUnknownPatientRegistrationService.updateMedicolegalDetails(admissionDto);
			}
		}
		//update Admission details
		Iterator<KinDetailsDto> itrKinDetailsDto = admissionDto.getKinArray().iterator();
		int index = 0;
		while (itrKinDetailsDto.hasNext()) {
			KinDetailsDto kinDetailsDto = itrKinDetailsDto.next();
			if (kinDetailsDto.getKinDetailsId() == 0) {
				if (kinDetailsDto.getKinName().equals(""))
					continue;
				kinDetailsDto.setPatientId(admissionDto.getPatientId());
				com.param.global.common.Response res2 = iKinDetailsDao.saveKinDetails(kinDetailsDto);
				if (index == 0) {
					if (admissionDto.getKinDetailsId() == null) {
						Integer kinDetailsId = (Integer) res2.getObject();
						adm.setKinDetailsId(kinDetailsId);
					}
				}
			} else {
				iKinDetailsDao.updateKinDetails(kinDetailsDto);
				adm.setKinDetailsId(kinDetailsDto.getKinDetailsId());
			}
			index++;
		}
		
		if(admissionDto.getDoctorId()!=null && admissionDto.getSpecialityId()!=null && admissionDto.getAdmittingDoctorId()!=null && admissionDto.getAdmittingSpecialityId()!=null) {
		Session sessionUpdate=sessionFactory.getCurrentSession();
		sessionUpdate.createSQLQuery("update adt.t_admission set "
				+ "doctor_id=" + admissionDto.getDoctorId() + ", " 
				+" speciality_id=" +admissionDto.getSpecialityId()+ ", "
						+ "admitting_doctor_id=" + admissionDto.getAdmittingDoctorId() + ", " 
						+" admitting_speciality_id=" +admissionDto.getAdmittingSpecialityId()+ " "
				+ "where admission_id="+admissionDto.getAdmissionId())
		.executeUpdate();
		}
		
		Session session=sessionFactory.getCurrentSession();
		session.createSQLQuery("update adt.t_admission_details set "
				+ "admission_type_id=" + admissionDto.getAdmissionTypeId() + ", " 
				+" meal_pref_id=" +admissionDto.getMealPrefId() + ", "
				+" pdd='"+admissionDto.getPdd() + "', "
				+" attendent_meal_pref_id=" + admissionDto.getAttendentMealPrefId() + " "
				+ "where admission_details_id="+admissionDto.getAdmissionDetailsId())
		.executeUpdate();
		
		 
			
			//update status 'I' FOR PRE REF DETAIL
			
		if (admissionDto.getReferalDetailsId() != null) {
				if(admissionDto.getReferalDetailsId()!=null) {
					Session session1=sessionFactory.getCurrentSession();
					session1.createSQLQuery(" update public.t_referal_details set" +
									" status ='I'"+
									" where referal_details_id="+admissionDto.getReferalDetailsId())
					.executeUpdate();
				}
		}
			if (admissionDto.getReferralTypeId() != null && admissionDto.getReferralId() != null) {
			ReferalDetailsDto referalDetailsDto = new ReferalDetailsDto();
				referalDetailsDto.setOrganizationId(admissionDto.getOrganizationId());
				referalDetailsDto.setUnitId(admissionDto.getUnitId());
				referalDetailsDto.setVisitTypeId(admissionDto.getVisitTypeId());
				referalDetailsDto.setAdmissionId(admissionDto.getAdmissionId());
				referalDetailsDto.setReferralId(admissionDto.getReferralId());
				referalDetailsDto.setReferralTypeId(admissionDto.getReferralTypeId());
				referalDetailsDto.setCreatedBy(admissionDto.getCreatedBy());
				referalDetailsDto.setReferralRemark(admissionDto.getReferralRemark());
				referalDetailsDto.setCreatedDate(admissionDto.getCreatedDate());
				referalDetailsDto.setUpdatedBy(admissionDto.getUpdatedBy());
				referalDetailsDto.setUpdatedDate(admissionDto.getUpdatedDate());
				referalDetailsDto.setStatus('A');
			iReferalDetailsService.saveReferalDetails(referalDetailsDto);
			}
		
		
	
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (Exception e) {
		
	}
     return new Response<>(ERROR, null, null, null, null);
	}
	
	@Override
	public Response updateOtStatusAgainstAdmission(AdmissionDto admissionDto) throws ApplicationException {
		try {
			
			return iBedAllocationDao.updateOtStatusAgainstAdmission(admissionDto);
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedCategoryListByVisitId(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		try {
			
			return iBedAllocationDao.getBedCategoryListByVisitId(bedCategoryMasterDto);
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
}
