package com.param.global.service;

import in.param.exception.ApplicationException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.billing.dao.IAutoPostingConfigDao;
import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.billing.service.IContractConsumtionForCostEstimationService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.PatientSponsorForCostEstimationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientSponsorDetailsServiceImpl implements IPatientSponsorDetailsService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(PatientSponsorDetailsServiceImpl.class);

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao; 
	
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IAutoPostingConfigDao iAutoPostingConfigDao;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	
	@Autowired
	IContractConsumtionForCostEstimationService iContractConsumptionServiceCostEstimation;
	
	@Override
	@Transactional
	public Response savePatientSponsorDetailsPharamcy(List<PatientSponsorDetailsDto> patientSponsorDetailslist,
			Integer encounterId, Integer admissionId,Integer selfPayeeId) {
		try {
			Integer patientId = null;
			List<Integer> sponsorIds = new ArrayList<Integer>();
			ListIterator<PatientSponsorDetailsDto> iterator = patientSponsorDetailslist.listIterator();
            while(iterator.hasNext()){
				PatientSponsorDetailsDto patientSponsorDetailsDto = iterator.next();
					patientId = patientSponsorDetailsDto.getPatientId();
					if(patientSponsorDetailsDto.getPatientSponsorDetailsId()!=null && patientSponsorDetailsDto.getPatientSponsorDetailsId()>0) {
					sponsorIds.add(patientSponsorDetailsDto.getPatientSponsorDetailsId());}
			}
			if(patientId!=null &&patientId>0 && sponsorIds!=null && sponsorIds.size()>0)
			{
			   this.inactiveteSponsoredDetails(patientId,sponsorIds);
			}else if(patientId!=null &&patientId>0 && sponsorIds.size() == 0) {
				this.inactiveteSponsoredDetails(patientId,sponsorIds); 
			}
			
			return this.savePatientSponsorDetails(patientSponsorDetailslist, encounterId, admissionId,selfPayeeId);
		}  catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response savePatientSponsorDetails(List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList, Integer encounterId,Integer admissionId,Integer selfPayeeId) {
		try {
			List<Integer> contractIds = new LinkedList<>();
			Integer patientId = null;
			
		/**	ListIterator<PatientSponsorDetailsDto> iterator = patientSponsorDetailsDtosList.listIterator();
			while(iterator.hasNext()){
				
				PatientSponsorDetailsDto patientSponsorDetailsDto = iterator.next();
					patientSponsorDetailsDto.setEncounterId(encounterId);
					patientSponsorDetailsDto.setAdmissionId(admissionId);
					patientId = patientSponsorDetailsDto.getPatientId();
				/*if(admissionId!=null && admissionId>0 
						&& !selfPayeeId.equals(patientSponsorDetailsDto.getPayeeId())
							&& (patientSponsorDetailsDto.getApplicableDate()==null || patientSponsorDetailsDto.getApplicableDate().length()==0))	
				{
					patientSponsorDetailsDto.setApplicableDate(iPatientSponsorDetailsDao.getDOAFromAdmissionId(admissionId));
				}
				if(encounterId!=null && encounterId>0 
						&& !selfPayeeId.equals(patientSponsorDetailsDto.getPayeeId())
							&& (patientSponsorDetailsDto.getApplicableDate()==null || patientSponsorDetailsDto.getApplicableDate().length()==0))	
				{
					patientSponsorDetailsDto.setApplicableDate(iPatientSponsorDetailsDao.getVisitDateFromEncounterId(encounterId));
				}*/
			/**	Response res =  iPatientSponsorDetailsDao.savePatientSponsorDetails(patientSponsorDetailsDto);
				
				if(patientSponsorDetailsDto.getContractId()!=null && patientSponsorDetailsDto.getPatientSponsorDetailsId()==null)
					contractIds.add(patientSponsorDetailsDto.getContractId());
				
				if(res.getObject()!=null) {
					 Object patientSponsorDetailsId = res.getObject();
					patientSponsorDetailsDto.setPatientSponsorDetailsId((Integer)patientSponsorDetailsId);
				}
			}*/
				patientSponsorDetailsDtosList.forEach(patientSponsorDetailsDto -> {
					patientSponsorDetailsDto.setEncounterId(encounterId);
					patientSponsorDetailsDto.setAdmissionId(admissionId);
					patientSponsorDetailsDto.setPatientId(patientId);
//					patientId.set(patientSponsorDetailsDto.getPatientId());
					Response res = iPatientSponsorDetailsDao.savePatientSponsorDetails(patientSponsorDetailsDto);
					if (patientSponsorDetailsDto.getContractId() != null && patientSponsorDetailsDto.getPatientSponsorDetailsId() == null)
						contractIds.add(patientSponsorDetailsDto.getContractId());

					if (res.getObject() != null) {
						Object patientSponsorDetailsId = res.getObject();
						patientSponsorDetailsDto.setPatientSponsorDetailsId((Integer) patientSponsorDetailsId);
					}
				});

			if(!contractIds.isEmpty()){
				//Response res = iPatientSponsorDetailsDao.getCapDetailsByContractIds(contractIds);
				List<PatientSponsorDetailsDto> list = iPatientSponsorDetailsDao.getCapDetailsByContractIds(contractIds).getListObject();
				if(!list.isEmpty()) {
					//List<PatientSponsorDetailsDto> list =(List<PatientSponsorDetailsDto>) res.getListObject();
					/**for (PatientSponsorDetailsDto pPDto : list)
					{
						Optional<PatientSponsorDetailsDto> matchingObject = patientSponsorDetailsDtosList.stream().
							    filter(p -> p.getContractId()!=null && p.getContractId().equals(pPDto.getContractId())).
							    findFirst();

						PatientSponsorDetailsDto obj = matchingObject.get();
						pPDto.setPatientSponsorDetailsId(obj!=null?obj.getPatientSponsorDetailsId():null);
						pPDto.setPatientId(patientId);
					}*/

//					Integer finalPatientId = patientId.get();
					list.forEach(pPDto -> {
						Optional<PatientSponsorDetailsDto> matchingObject = patientSponsorDetailsDtosList.stream().
								filter(p -> p.getContractId()!=null && p.getContractId().equals(pPDto.getContractId())).
								findFirst();

						PatientSponsorDetailsDto obj = matchingObject.get();
						pPDto.setPatientSponsorDetailsId(obj.getPatientSponsorDetailsId());
						pPDto.setPatientId(patientId);
					});
					iPatientSponsorDetailsDao.saveCapDetailsByPatientAndContractId(list,encounterId,admissionId);
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getSponsorDetailsByEncounterId(int encounterId) {
		try {
			return iPatientSponsorDetailsDao.getSponsorDetailsByEncounterId(encounterId);
		} catch (Exception e) {
			logger.error("getSponsorDetailsByEncounterId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePatientSponsorDetailsAndUpdatePatientSponsorUpdate(
			List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList, int encounterId) {
		try {
			
			ListIterator<PatientSponsorDetailsDto> iterator = patientSponsorDetailsDtosList.listIterator();
			while(iterator.hasNext()){
				
				PatientSponsorDetailsDto patientSponsorDetailsDto = iterator.next();
				 if(patientSponsorDetailsDto.getPatientSponsorDetailsId()!=null) {
					 iPatientSponsorDetailsDao.updatePatientSponsorDetails(patientSponsorDetailsDto);
				 }else {
						patientSponsorDetailsDto.setEncounterId(encounterId);
						//check if paymentEntitlementId = 3
						// get cap amount details from contract and save to new table "patient_cantrract_cap_details"
					iPatientSponsorDetailsDao.savePatientSponsorDetails(patientSponsorDetailsDto);
				 }
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("savePatientSponsorDetailsAndUpdatePatientSponsorUpdate failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSponsoredDetails(Integer admissionId, Integer paymentEntitlementId) {
		try {
			return iPatientSponsorDetailsDao.getSponsoredDetails(admissionId,paymentEntitlementId);
		} catch (Exception e) {
			logger.error("getSponsoredDetails failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSponsoredDetailsForUpdatePayee(Integer admissionId,Integer encounterId) {
		try {
			return iPatientSponsorDetailsDao.getSponsoredDetailsForUpdatePayee(admissionId,encounterId);
		} catch (Exception e) {
			logger.error("getSponsoredDetailsForUpdatePayee failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveSponsorDetailsForUpdatePayee(Integer admissionId,Integer encounterId) {
		try {
			return iPatientSponsorDetailsDao.inactiveSponsorDetailsForUpdatePayee(admissionId,encounterId);
		} catch (Exception e) {
			logger.error("inactiveSponsorDetailsForUpdatePayee failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePatientSponsorDetailsForCostEstimation(PatientSponsorForCostEstimationDto dto) {
		try {
			
			iPatientSponsorDetailsDao.inactivePatientSponsors(dto.getPatientId());
			
			List<Integer> contractIds = new LinkedList<>();
			Integer patientId = null;
			Integer primaryContractId = null;
			Integer paymentEntitlementId = null;
			ListIterator<PatientSponsorDetailsDto> iterator =dto.getPatientSponsorDetailsDtosList().listIterator();
			while(iterator.hasNext()){
				
				PatientSponsorDetailsDto patientSponsorDetailsDto = iterator.next();
					if(patientSponsorDetailsDto.getPriorityId()==1)
					{	
						paymentEntitlementId = patientSponsorDetailsDto.getPaymentEntitlementId();
						primaryContractId = patientSponsorDetailsDto.getContractId();
					}
					patientId = patientSponsorDetailsDto.getPatientId();
				iPatientSponsorDetailsDao.savePatientSponsorDetailsForCostEstimation(patientSponsorDetailsDto);
				
				if(patientSponsorDetailsDto.getContractId()!=null && patientSponsorDetailsDto.getPatientSponsorDetailsId()==null)
					contractIds.add(patientSponsorDetailsDto.getContractId());
			}
			if(contractIds.size()>0){
				Response res = iPatientSponsorDetailsDao.getCapDetailsByContractIds(contractIds);
				if(res.getListObject().size()>0)
				{
					List<PatientSponsorDetailsDto> list =(List<PatientSponsorDetailsDto>) res.getListObject();
					for (PatientSponsorDetailsDto pPDto : list) {
						pPDto.setPatientId(patientId);
					}
					iPatientSponsorDetailsDao.saveCapDetailsByPatientAndContractIdForCostEstimation(list);
				}
				
			}
			dto.setContractId(primaryContractId);
			dto.setPaymentEntitlementId(paymentEntitlementId);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
		} catch (Exception e) {
			logger.error("savePatientSponsorDetailsForCostEstimation failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response calculateAutoRenderForCostEstimation(PatientSponsorForCostEstimationDto dto) {
		try {
			Set<Integer> serviceIds = new HashSet<>();
			List<ContractConsumptionDto> contractList = new LinkedList<>();
			List<ContractConsumptionDto> finalContractList = new LinkedList<>();
			Integer seftPayeeId =null;
			Integer defaultTariffId = null;
			Integer administativeCharges = null;
			List<OrderDetailsPayeeMapperDto> finalPayeeDto = new LinkedList<>();
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(dto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(dto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if(appConfigRes.getObject()!=null)
			{
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				
				if(unitApplicationConfigurationDtos.getSelfPayeeId()==null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
				else
					seftPayeeId=unitApplicationConfigurationDtos.getSelfPayeeId();
				
				if(unitApplicationConfigurationDtos.getDefaultSelfTariffId()==null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Default traiff is not defined !!", null, null);
				else
					defaultTariffId = unitApplicationConfigurationDtos.getDefaultSelfTariffId();
				
				administativeCharges = unitApplicationConfigurationDtos.getAdmissionChargesServiceId();
			}else{
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Application Configuration is yet to set !!", null, null);
			}
			
			if(administativeCharges !=null && administativeCharges>0)
			{
				ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
					consumptionDto.setPatientId(dto.getPatientId()); 
					consumptionDto.setPaymentEntitlementId(dto.getPaymentEntitlementId()); 
					consumptionDto.setContractId(dto.getContractId()); 
					consumptionDto.setServiceId(administativeCharges); 
					consumptionDto.setCurrentDate(dto.getDoa().substring(0,10)); 
					consumptionDto.setOrganizationId(dto.getOrganizationId());
					consumptionDto.setUnitId(dto.getUnitId());
					consumptionDto.setVisitTypeId(dto.getVisitTypeId());
					consumptionDto.setBillingBedCategoryId(dto.getBillingBedCategoryId());
					consumptionDto.setPatientCategoryId(dto.getPatietCategoryId());
					consumptionDto.setDefaultSelfTariffId(defaultTariffId);
					consumptionDto.setSelfPayeeId(seftPayeeId);
				Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
				
				finalContractList.add((ContractConsumptionDto)res.getObject());
			}
			
			Response serviceConfigRes = iAutoPostingConfigDao.getAutoPostingConfigList(dto.getOrganizationId(), dto.getUnitId());
			List<AutoPostingServicesConfigDto> autoPostingServicesConfigDtoList = serviceConfigRes.getListObject();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = formatter.parse(dto.getDoa());
			Date endDate = formatter.parse(dto.getPdd());
			
			Calendar start = Calendar.getInstance();
			start.setTime(startDate);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) 
			{

				for(AutoPostingServicesConfigDto configDto :autoPostingServicesConfigDtoList)
				{
					
					ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto.setPatientId(dto.getPatientId()); 
						consumptionDto.setPaymentEntitlementId(dto.getPaymentEntitlementId()); 
						consumptionDto.setContractId(dto.getContractId()); 
						consumptionDto.setServiceId(configDto.getServiceId()); 
						consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd")); 
						consumptionDto.setOrganizationId(dto.getOrganizationId());
						consumptionDto.setUnitId(dto.getUnitId());
						consumptionDto.setVisitTypeId(dto.getVisitTypeId());
						consumptionDto.setBillingBedCategoryId(dto.getBillingBedCategoryId());
						consumptionDto.setPatientCategoryId(dto.getPatietCategoryId());
						consumptionDto.setDefaultSelfTariffId(defaultTariffId);
						consumptionDto.setSelfPayeeId(seftPayeeId);
					Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
					consumptionDto = (ContractConsumptionDto)res.getObject();
					if(consumptionDto.getServiceName()!=null || consumptionDto.getFinalPrice()>0){
						serviceIds.add(configDto.getServiceId());
						contractList.add((ContractConsumptionDto)res.getObject());
					}
						
				}
			}
			
			for(Integer serviceId : serviceIds)
			{
				Double finalPrice = 0.00;
				Double quantity = 0.00;
				Double selfPayable = 0.00;
				Double creditPayable = 0.00;
				ContractConsumptionDto finalDto = new ContractConsumptionDto();
				ContractConsumptionDto finalDtoToBeAdded = new ContractConsumptionDto();
				finalPayeeDto = new LinkedList<>();
				Map<Integer, Double> payeeMap = new HashMap<>(); 
				Map<Integer, String> payeeDtls = new HashMap<>(); 
				for(ContractConsumptionDto finalConsumptionDto : contractList)
				{
					
					if(finalConsumptionDto.getServiceId().equals(serviceId))
					{
						finalPrice = finalPrice+finalConsumptionDto.getFinalPrice();
						quantity = quantity+1.00;
						selfPayable = selfPayable+finalConsumptionDto.getSelfPayable();
						creditPayable = creditPayable+finalConsumptionDto.getCreaditPayable();
						finalDto = finalConsumptionDto;
						for(OrderDetailsPayeeMapperDto payeeDto : finalConsumptionDto.getOrderDetailsPayeeMapperDtosList())
						{
							payeeDtls.put(payeeDto.getPayeeId(), payeeDto.getPayeeDesc());
							if(payeeMap.isEmpty())
							{
								payeeMap.put(payeeDto.getPayeeId(),payeeDto.getPayeeAmount().doubleValue());
							}else{
									if(payeeMap.containsKey(payeeDto.getPayeeId()))
									{
										Double amt = payeeMap.get(payeeDto.getPayeeId());
										amt = amt+payeeDto.getPayeeAmount().doubleValue();
										payeeMap.put(payeeDto.getPayeeId(),amt);
									}
									else{
										payeeMap.put(payeeDto.getPayeeId(),payeeDto.getPayeeAmount().doubleValue());
									}
							}
							
						}
					}
				}
				
				finalDtoToBeAdded.setSpecialityName(finalDto.getSpecialityName());
				finalDtoToBeAdded.setServiceName(finalDto.getServiceName());
				finalDtoToBeAdded.setMinRateEditable(finalDto.getMinRateEditable());
				finalDtoToBeAdded.setMaxRateEditable(finalDto.getMaxRateEditable());
				finalDtoToBeAdded.setServiceRate(finalDto.getServiceRate());
				finalDtoToBeAdded.setFinalPrice(finalPrice);
				finalDtoToBeAdded.setCreaditPayable(creditPayable);
				finalDtoToBeAdded.setSelfPayable(selfPayable);
				finalDtoToBeAdded.setServiceId(finalDto.getServiceId());
				finalDtoToBeAdded.setPayeeId(finalDto.getSelfPayeeId());
				finalDtoToBeAdded.setIsRateEditable('Y');
				finalDtoToBeAdded.setQuantity(quantity);
				finalDtoToBeAdded.setTariffId(finalDto.getDefaultSelfTariffId());
				for (Map.Entry<Integer, Double> entry : payeeMap.entrySet()) 
				{
					OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
						detailsPayeeMapperDto.setPayeeId(entry.getKey());
						detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(entry.getValue()));
						detailsPayeeMapperDto.setPayeeDesc(payeeDtls.get(entry.getKey()));
						detailsPayeeMapperDto.setServiceId(finalDto.getServiceId());
					finalPayeeDto.add(detailsPayeeMapperDto);
				}
				
				finalDtoToBeAdded.setOrderDetailsPayeeMapperDtosList(finalPayeeDto); 
				finalDtoToBeAdded.setOrderDetailsPackageMapperDtosList(finalDto.getOrderDetailsPackageMapperDtosList());
				
			finalContractList.add(finalDtoToBeAdded);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalContractList, null);
		} catch (Exception e) {
			logger.error("calculateAutoRenderForCostEstimation failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLastSponsorsByPatientId(Integer patientId,Integer visitTypeId, Integer unitId) {
		try {
			if(patientId!=null) 
			{
				Response res = iPatientSponsorDetailsDao.getLastPatientClass(patientId,visitTypeId,unitId);
				if(res != null && res.getObject() != null) 
				{
					PatientSponsorDetailsDto dto = (PatientSponsorDetailsDto)res.getObject();
					Response sponsorRes = iPatientSponsorDetailsDao.getLastSponsors(dto.getVisitId(),dto.getType());
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, sponsorRes.getListObject(), dto.getPatientClassId());
				}else {
					return new Response<>(SUCCESS, null, null, null, null);
				}
			}

		} catch (Exception e) {
			logger.error("getLastSponsorsByPatientId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response inactiveteSponsoredDetails(Integer patientId,List<Integer> sponsorIds) {
		try {
			 iPatientSponsorDetailsDao.inactiveteSponsoredDetails(patientId,sponsorIds);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updatePatientSponserDetailsClaimNumber(PatientSponsorDetailsDto patientSponsorDetailsDto) {
		try {
			Response sponsorRes = iPatientSponsorDetailsDao.updatePatientSponserDetailsClaimNumber(patientSponsorDetailsDto);

			 return new Response<>(SUCCESS, SUCCESS_CODE, "Updated Successfully", null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
}
