package com.param.global.billing.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.billing.dao.IContractConsumptionCostEstimationDao;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ContractConsumptionForCostEstimationServiceImpl implements IContractConsumtionForCostEstimationService,ICommonConstants{

	final static Logger logger = Logger.getLogger(ContractConsumptionForCostEstimationServiceImpl.class);

	@Autowired
	IContractConsumptionCostEstimationDao iContractConsumptionDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	SessionService sessionService;
	
	@Override
	@Transactional
	public Response consumeServiceFromContract(ContractConsumptionDto consumptionDto) throws ApplicationException {
		try{
			
			if(consumptionDto.getVisitTypeId()==OP)
				consumptionDto.setAdmissionId(null);
			if(consumptionDto.getVisitTypeId()==IP || consumptionDto.getVisitTypeId()==DC || consumptionDto.getVisitTypeId()==ER)
				consumptionDto.setEncounterId(null);
			Double concession = 0.00;
			Double concessionPer = 0.00;
			Integer priorityId = 0;
			Character isRateEditable = 'N';
			Character isRateEditableService = 'N';
			Character isQuantityEditable='N';
			Integer count = 0;
			Double rate = null;
			Double rateMinusConcession = null;
			double discountAmt = 0.00;
			Double rateDiff = null;
			Double contractRate = null;
			Double selfPayable = 0.00;
			Double creaditPayable = 0.00;
			Double preAuthAmount = 0.00;
			List<ContractConsumptionDto> serviceDetailslist = null;
			OrderDetailsPayeeMapperDto detailsPayeeMapperDto = null;
			List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = new LinkedList<>();
			Double taxAmt = 0.0;
			Double netAmt = 0.0;
			Double totalCreditPayable = 0.00;
			Double totalSelfPayable = 0.00;
			Integer specialityId = null;
			Integer subSpecialityId = null;
			Integer tariffId = null;
			ContractConsumptionDto finalDto = new ContractConsumptionDto();
			Integer serviceId = consumptionDto.getServiceId();
			if(consumptionDto.getProcedureId()!=null && consumptionDto.getProcedureId()>0 && consumptionDto.getIsBOM()!=null && consumptionDto.getIsBOM()=='Y')
			{
				consumptionDto.setServiceId(consumptionDto.getProcedureId());
			}
			ContractConsumptionDto patientClassDto = null;
			
			//<-----1. Check if an encounter or admission has any contract or not.
			if(consumptionDto.getContractId()!=null && consumptionDto.getContractId().intValue()>0)
			{
				//<-----1.1 Get the service rates from all the sponsors (list is fetched according to the highest priority)
				serviceDetailslist =  iContractConsumptionDao.getServiceDetailsByContractId(consumptionDto);
				for(ContractConsumptionDto resDto : serviceDetailslist)
				{	
					tariffId = resDto.getTariffId();
					resDto.setEncounterId(consumptionDto.getEncounterId());
					resDto.setAdmissionId(consumptionDto.getAdmissionId());
					specialityId = resDto.getSpecialityId();
					subSpecialityId = resDto.getSubSpecialityId();
					consumptionDto.setPaymentEntitlementId(resDto.getPaymentEntitlementId());
					/*if(consumptionDto.getPatientClassPercentage()!=null && consumptionDto.getPatientClassPercentage()>0.00)
						concessionPer = consumptionDto.getPatientClassPercentage();
					else if((resDto.getIsDiscount()=='Y' && resDto.getIsSchemeDiscount()=='Y')||(resDto.getIsSchemeDiscount()=='N')){
						patientClassDto = iContractConsumptionDao.getPatientClassAndPercentageByVisit(consumptionDto);
						
						if(patientClassDto !=null && patientClassDto.getMarkupStatus()!=null && patientClassDto.getMarkupStatus()==false && patientClassDto.getPatientClassPercentage()!=null && patientClassDto.getPatientClassPercentage()>0)
							concessionPer = patientClassDto.getPatientClassPercentage();
					}*/
					concessionPer = 0.00;
					/*if(resDto.getIsTariffRateApplicable()=='Y')
					{*/
					if(resDto.getType()==1)	
					{
						if(consumptionDto.getChangedPrice()!=null && consumptionDto.getChangedPrice()>=0) 
						{
						  /*if(consumptionDto.getOrdDiscount()==0) 
						  {
								concession = consumptionDto.getChangedPrice()*(patientClassDto.getPatientClassPercentage()*0.01);
								resDto.setServiceRate(consumptionDto.getChangedPrice());
								resDto.setFinalPrice((consumptionDto.getChangedPrice()-(Math.abs(concession-consumptionDto.getOrdDiscount())) + (consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount()))*(resDto.getTaxPercentage()*0.01)));
								resDto.setSelfPayable(resDto.getFinalPrice()*((resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00 )*0.01));
								resDto.setCreaditPayable(resDto.getFinalPrice()-resDto.getSelfPayable());
							}
							else 
							{
								concession = 0.00;
								resDto.setServiceRate(consumptionDto.getChangedPrice());
								resDto.setFinalPrice((consumptionDto.getChangedPrice()-(Math.abs(concession-consumptionDto.getOrdDiscount()))) + (consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount()))*(resDto.getTaxPercentage()*0.01));
								resDto.setSelfPayable(resDto.getFinalPrice()*((resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00 )*0.01));
								resDto.setCreaditPayable(resDto.getFinalPrice()-resDto.getSelfPayable());
							}*/
							if(concessionPer>0.00)
								concession = consumptionDto.getChangedPrice()*(concessionPer*0.01);
							else
								concession = 0.00;
							
							if(consumptionDto.getOrdDiscountPer()>0)
							{
								rateMinusConcession = consumptionDto.getChangedPrice() - concession;
								discountAmt = rateMinusConcession*(consumptionDto.getOrdDiscountPer()*0.01);
							}
							
							resDto.setFinalPrice((consumptionDto.getChangedPrice()-(Math.abs(concession+discountAmt))) + (consumptionDto.getChangedPrice()-Math.abs(concession+discountAmt))*(resDto.getTaxPercentage()*0.01));
							resDto.setServiceRate(consumptionDto.getChangedPrice());
							resDto.setSelfPayable(resDto.getFinalPrice()*((resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00 )*0.01));
							resDto.setCreaditPayable(resDto.getFinalPrice()-resDto.getSelfPayable());
						} else if(consumptionDto.getConsultationAmount()!=null && consumptionDto.getConsultationAmount()>0)
						{
							rate = consumptionDto.getConsultationAmount();
							resDto.setServiceRate(rate);
							contractRate = (resDto.getFinalPrice()!=null ? resDto.getFinalPrice():resDto.getServiceRate());
							if(resDto.getTaxPercentage()!=null)
							{
								taxAmt = rate * (resDto.getTaxPercentage()*0.01);
								rate = rate+taxAmt;
							}
							resDto.setFinalPrice(rate);
							if(resDto.getIsTariffRateApplicable()=='Y')
							{
								resDto.setSelfPayable(resDto.getFinalPrice()*((resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00 )*0.01));
								resDto.setCreaditPayable(resDto.getFinalPrice()-resDto.getSelfPayable());
							}else {
								if(rate>=contractRate)
								{
									//contract amount will remain same the difference between service rate and contractRate
									rateDiff = rate-contractRate;
									selfPayable = resDto.getSelfPayable()+rateDiff;
								    creaditPayable = resDto.getCreaditPayable();
								}
								else
								{
									rateDiff = contractRate-rate;
									if(resDto.getCreaditPayable()>=rate)
									{
										selfPayable = 0.00;
									    creaditPayable = rate;
									}else{
										selfPayable = rateDiff;
									    creaditPayable = resDto.getCreaditPayable();
									}
								}
								resDto.setCreaditPayable(creaditPayable);
								resDto.setSelfPayable(selfPayable);
							}
							
						}else if(consumptionDto.getProcedureId()!=null && consumptionDto.getProcedureId()>0)
						{
							rate = consumptionDto.getServiceRate();
							if(resDto.getIsTariffRateApplicable()=='Y')
							{
								resDto.setServiceRate(rate);
								if(resDto.getTaxPercentage()!=null)
								{
									taxAmt = rate * (resDto.getTaxPercentage()*0.01);
									rate = rate+taxAmt;
								}
								resDto.setFinalPrice(rate);
								resDto.setTaxAmt(taxAmt);
								resDto.setSelfPayable(resDto.getFinalPrice()*((resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00 )*0.01));
								resDto.setCreaditPayable(resDto.getFinalPrice()-resDto.getSelfPayable());
							}
							resDto.setBillingBedExist(1);
						}else if(consumptionDto.getIsPeriodicityService()!=null && consumptionDto.getIsPeriodicityService()=='Y')
						{
							rate = consumptionDto.getServiceRate();
							if(resDto.getIsTariffRateApplicable()=='Y')
							{
								resDto.setServiceRate(rate);
								if(resDto.getTaxPercentage()!=null)
								{
									taxAmt = rate * (resDto.getTaxPercentage()*0.01);
									rate = rate+taxAmt;
								}
								resDto.setFinalPrice(rate);
								resDto.setTaxAmt(taxAmt);
								resDto.setSelfPayable(resDto.getFinalPrice()*((resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00 )*0.01));
								resDto.setCreaditPayable(resDto.getFinalPrice()-resDto.getSelfPayable());
							}
						}else{
							if(resDto.getType()==1)
							{
								if(concessionPer>0.00)
									concession = resDto.getServiceRate()*(concessionPer*0.01);
								
								if(resDto.getTaxPercentage()!=null)
									resDto.setFinalPrice(resDto.getServiceRate()-Math.abs(concession) + (resDto.getServiceRate()-Math.abs(concession))*(resDto.getTaxPercentage()*0.01));
								else
									resDto.setFinalPrice(resDto.getServiceRate()-Math.abs(concession));
								
								resDto.setSelfPayable(resDto.getFinalPrice()*(resDto.getCoShare()*0.01));
								resDto.setCreaditPayable(resDto.getFinalPrice()-resDto.getSelfPayable());
							}
							
						}
					}
					//}
					
					if(consumptionDto.getServiceChargeStatus()!=null && consumptionDto.getServiceChargeStatus()==0)
					{
						resDto.setServiceRate(resDto.getServiceRate()/2);
						resDto.setFinalPrice(resDto.getFinalPrice()/2);
						resDto.setCreaditPayable(resDto.getCreaditPayable()/2);
						resDto.setSelfPayable(resDto.getSelfPayable()/2);
					}
					
					//<-----1.2 First check will be from the highest priority's sponsors/payer 
					if(totalCreditPayable==0)
					{
						priorityId = resDto.getPriorityId();
						isRateEditable = resDto.getIsRateEditable();
						isRateEditableService = resDto.getIsRateEditable();
						isQuantityEditable = resDto.getIsQuantityEditable();
						preAuthAmount = resDto.getPreAuthBalAmt();
						if(resDto!=null)
						{
							//Type decides whether data has came from service contract or service cap
							//<-----1.2.1 if data has came from service contract
							if(resDto.getType()==1)
							{
								rate = (resDto.getFinalPrice()!=null ? resDto.getFinalPrice():resDto.getServiceRate());
								//<-----1.2.2 If cap amount is un-defined, then the service amount included in contract will be credit payable.
								if(resDto.getBalanceCapAmount()==null)
								{
								    selfPayable = resDto.getSelfPayable();
								    creaditPayable = resDto.getCreaditPayable();
								    
									if(preAuthAmount!=null)
									{
										if(creaditPayable>preAuthAmount)
										{
											selfPayable = selfPayable + creaditPayable - preAuthAmount;
											creaditPayable = preAuthAmount;
										}
										/*if(consumptionDto.getUpdateCap()==null && creaditPayable>0)
											iContractConsumptionDao.updatePreAuthAmount(resDto.getPreauthId(),creaditPayable);*/
									}
									
									
									totalCreditPayable = totalCreditPayable+creaditPayable;
									totalSelfPayable = totalSelfPayable+selfPayable;
								}
								//1.2.2 ----->
								
								//<-----1.2.3 If cap amount is defined
								if(resDto.getBalanceCapAmount()!=null)
								{
									//<---1.2.3.1 If cap amount is greater than credit payable amount then assign the self and credit values as it is
									if(resDto.getCreaditPayable() <= resDto.getBalanceCapAmount())
									{
										selfPayable = resDto.getSelfPayable();
										creaditPayable =  resDto.getCreaditPayable();
									}
									//1.2.3.1--->
									
									//<---1.2.3.2 Else whatever amount remaining in the cap will be credit payable and rest of the amount will be self payable
									else
									{
										selfPayable = resDto.getSelfPayable()+(resDto.getCreaditPayable()-resDto.getBalanceCapAmount());
										creaditPayable = resDto.getBalanceCapAmount();
									}
									//1.2.3.2 --->

									if(preAuthAmount!=null)
									{
										if(creaditPayable>preAuthAmount)
										{
											selfPayable = selfPayable + creaditPayable - preAuthAmount;
											creaditPayable = preAuthAmount;
										}
										/*if(consumptionDto.getUpdateCap()==null && creaditPayable>0)
											iContractConsumptionDao.updatePreAuthAmount(resDto.getPreauthId(),creaditPayable);*/
									}
									
									//<---1.2.3.3 update cap amount
									if(consumptionDto.getUpdateCap()==null && creaditPayable>0)
									{
											
											consumptionDto.setSpecialityId(resDto.getSpecialityId());
											consumptionDto.setContractId(resDto.getContractId());
										iContractConsumptionDao.updateCapAmount(consumptionDto,creaditPayable); 
									}
									//1.2.3.3--->
									
									totalCreditPayable = totalCreditPayable+creaditPayable;
									totalSelfPayable = totalSelfPayable+selfPayable;
								}
								//1.2.3----->
								
							}
							//1.2.1----->
							
							//<-----1.2.2 if data has came from service cap
							if(resDto.getType()==2)
							{
								//<-----1.2.2.1 check the applicable days lies between the date of admission and current date's difference
								if(consumptionDto.getAdmissionId()!=null)
									resDto.setDayDiff(GlobalCommonDateUtils.getDateDifference(GlobalCommonDateUtils.getDate(consumptionDto.getCurrentDate().substring(0,10),"yyyy-MM-dd"), GlobalCommonDateUtils.getDate(resDto.getDoa().substring(0,10),"yyyy-MM-dd"))+1);
								
								if(resDto.getTillDischarge()==true||resDto.getApplicableDays()>=resDto.getDayDiff())
								{
									resDto.setType(3);
									if(resDto.getApplicableDate()!=null && !(resDto.getApplicableDate().equals(consumptionDto.getCurrentDate().substring(0,10))))
									{
											resDto.setType(4);
											resDto.setBalanceAmount(resDto.getAmount());
											resDto.setBalanceQuantity(resDto.getQuantity());
											resDto.setApplicableDate(consumptionDto.getCurrentDate().substring(0,10));
									}
									
									//<-----1.2.2.1.1 if yes then get the rates from the fall back tariff.
									if(consumptionDto.getChangedPrice()!=null && consumptionDto.getChangedPrice()>=0)
									{
										rate = consumptionDto.getChangedPrice();
										concession = rate*(concessionPer*0.01);	
										if(consumptionDto.getOrdDiscountPer()>0)
										{
											rateMinusConcession = consumptionDto.getChangedPrice() - concession;
											discountAmt = rateMinusConcession*(consumptionDto.getOrdDiscountPer()*0.01);
										}
									}else if(consumptionDto.getConsultationAmount()!=null && consumptionDto.getConsultationAmount()>0)
									{
										rate = consumptionDto.getConsultationAmount();
										concession = rate*(concessionPer*0.01);	
									}else if(consumptionDto.getIsPeriodicityService()!=null && consumptionDto.getIsPeriodicityService()=='Y')
									{
										rate = consumptionDto.getServiceRate();
										concession = rate*(concessionPer*0.01);	
									}else if(consumptionDto.getProcedureId()!=null && consumptionDto.getProcedureId()>0)
									{
										rate = consumptionDto.getServiceRate();
										concession = rate*(concessionPer*0.01);	
									}else{
										UnitServiceTariffMasterDto unitServiceTariffMasterDto =new UnitServiceTariffMasterDto();
											unitServiceTariffMasterDto.setOrganizationId(consumptionDto.getOrganizationId());
											unitServiceTariffMasterDto.setUnitId(consumptionDto.getUnitId());
											unitServiceTariffMasterDto.setVisitTypeId(consumptionDto.getVisitTypeId());
											unitServiceTariffMasterDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
											unitServiceTariffMasterDto.setPatientTypeId(consumptionDto.getPatientCategoryId()!=null ? consumptionDto.getPatientCategoryId() : consumptionDto.getPatientTypeId()); 
											unitServiceTariffMasterDto.setPaymentEntitlementId(SELF);//Self
											unitServiceTariffMasterDto.setServiceId(consumptionDto.getServiceId());
											unitServiceTariffMasterDto.setOrderDate(consumptionDto.getCurrentDate());
											unitServiceTariffMasterDto.setTariffId(resDto.getTariffId());
											unitServiceTariffMasterDto.setPatientClassId(consumptionDto.getPatientClassId());
											unitServiceTariffMasterDto = iEncounterMasterService.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
										rate = (unitServiceTariffMasterDto.getRate() != null ? unitServiceTariffMasterDto.getRate() : 0.0);
										concession = rate*(concessionPer*0.01);	
									}
									
									if(resDto.getTaxPercentage()!=null && resDto.getTaxPercentage()>0)
									{
										taxAmt = (rate-(concession+discountAmt)) * (resDto.getTaxPercentage()*0.01);
										netAmt = (rate-(concession+discountAmt))+taxAmt;
									}
									else
										netAmt = (rate-(concession+discountAmt));
									
									if(consumptionDto.getServiceChargeStatus()!=null && consumptionDto.getServiceChargeStatus()==0)
									{
										rate = rate/2;
									}
									//1.2.2.1.1----->
									
									//<-----1.2.2.1.2 If service capping is based on amount
									if(resDto.getBalanceAmount()!=null && resDto.getBalanceAmount()>0)
									{
										//<---1.2.2.1.2.1 If cap amount is greater than service rates then assign the rates to credit payable
										if(netAmt < resDto.getBalanceAmount())
										{
											creaditPayable = netAmt;
											selfPayable = 0.0;
										}
										//1.2.2.1.2.1--->
										
										//<---1.2.2.1.2.2 Else whatever amount remaining in the cap will be credit payable rest o the amount will be self payable
										else
										{
											selfPayable=netAmt-resDto.getBalanceAmount();
											creaditPayable=resDto.getBalanceAmount();
										}
										//1.2.2.1.2.2--->
										
										if(resDto.getCoShare()!=null && creaditPayable > 0)
										{
											selfPayable = selfPayable + (creaditPayable*resDto.getCoShare()*0.01);
											creaditPayable = creaditPayable - (creaditPayable*resDto.getCoShare()*0.01);
										}
										
										if(preAuthAmount!=null)
										{
											if(creaditPayable>preAuthAmount)
											{
												selfPayable = selfPayable + creaditPayable - preAuthAmount;
												creaditPayable = preAuthAmount;
											}
											/*if(consumptionDto.getUpdateCap()==null && creaditPayable>0)
												iContractConsumptionDao.updatePreAuthAmount(resDto.getPreauthId(),creaditPayable);*/
										}
										
										
										//<---1.2.2.1.2.3 update cap amount
										if(consumptionDto.getUpdateCap()==null && creaditPayable>0)
										{
											iContractConsumptionDao.updateServiceCapAmount(resDto,creaditPayable.doubleValue(),null); 
										}
											
										//1.2.2.1.2.3--->
										totalCreditPayable = totalCreditPayable+creaditPayable;
										totalSelfPayable = totalSelfPayable+selfPayable;
									}
									//1.2.2.1.2----->
									
									//<-----1.2.2.1.3 If service capping is based on quantity
									if(resDto.getBalanceQuantity()!=null)
									{
										//<---1.2.2.1.3.1 If quantity exists then service rates will be assigned to credit payable
										if(resDto.getBalanceQuantity()>0)
										{
											creaditPayable = netAmt;
											selfPayable = 0.0;
											
											if(resDto.getCoShare()!=null && creaditPayable > 0)
											{
												selfPayable = selfPayable + (creaditPayable*resDto.getCoShare()*0.01);
												creaditPayable = creaditPayable - (creaditPayable*resDto.getCoShare()*0.01);
											}
											
											if(preAuthAmount!=null)
											{
												if(creaditPayable>preAuthAmount)
												{
													selfPayable = selfPayable + creaditPayable - preAuthAmount;
													creaditPayable = preAuthAmount;
												}
												/*if(consumptionDto.getUpdateCap()==null && creaditPayable>0)
													iContractConsumptionDao.updatePreAuthAmount(resDto.getPreauthId(),creaditPayable);*/
											}
											
											//<---1.2.2.1.3.1.1 update quantity
											if(consumptionDto.getUpdateCap()==null && creaditPayable > 0)
											{
												iContractConsumptionDao.updateServiceCapAmount(resDto,null,1.00); 
											}
											//1.2.2.1.3.1.1--->
											
											totalCreditPayable = totalCreditPayable+creaditPayable;
											totalSelfPayable = totalSelfPayable+selfPayable;
										}
										//1.2.2.1.3.1--->
										
										//<---1.2.2.1.3.2 If quantity exceeds, then service rates will be assigned to self payable
										else
										{
											creaditPayable = 0.0;
											selfPayable =netAmt;
											
											totalCreditPayable = totalCreditPayable+creaditPayable;
											totalSelfPayable = totalSelfPayable+selfPayable;
										}
										//1.2.2.1.3.2--->
									}
									//1.2.2.1.3----->
								}
								//1.2.2.1----->
							}
							//1.2.2----->
						}
						if(creaditPayable>0)
						{
							detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
								detailsPayeeMapperDto.setPayeeId(resDto.getPayeeId());
								detailsPayeeMapperDto.setAssociateCompanyId(resDto.getAssociateCompanyId());
								detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(creaditPayable));
								detailsPayeeMapperDto.setContractId(resDto.getContractId());
								detailsPayeeMapperDto.setServiceId(serviceId);
								detailsPayeeMapperDto.setPayeeDesc(resDto.getCompanyName());
								detailsPayeeMapperDto.setIsRateEditable(isRateEditable);
								detailsPayeeMapperDto.setCoShare(resDto.getCoShare());
								detailsPayeeMapperDto.setSpiltBillAmt(new BigDecimal(creaditPayable));
							orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
						}
					}
					//1.2----->
					
					//<-----1.3 Second round, after consuming the service from the highest priority's payer, if there is a still self payable component exist then check with the next sponsor/payer
					if(totalSelfPayable>0)
					{
						List<ContractConsumptionDto> serviceDetailslistsecondRoundList= serviceDetailslist;
						for(ContractConsumptionDto secondRound : serviceDetailslistsecondRoundList)
						{
							Double creaditPayableSecondRound = 0.00;
							Double secondRounfCoShare = 0.00;
							if(secondRound.getPriorityId() > priorityId)
							{
								preAuthAmount = secondRound.getPreAuthBalAmt();
								secondRound.setEncounterId(consumptionDto.getEncounterId());
								secondRound.setAdmissionId(consumptionDto.getAdmissionId());
								
								if(secondRound.getType()==1)	
								{	
									consumptionDto.setSpecialityId(secondRound.getSpecialityId());
									if(secondRound.getBalanceCapAmount()==null)
									{
										if(secondRound.getCreaditPayable() > totalSelfPayable)
											creaditPayableSecondRound = totalSelfPayable;
										else
											creaditPayableSecondRound = secondRound.getCreaditPayable();
										
									}
									if(secondRound.getBalanceCapAmount()!=null)
									{
										if(secondRound.getCreaditPayable() >= totalSelfPayable)
										{
											if(totalSelfPayable < secondRound.getBalanceCapAmount())
												creaditPayableSecondRound=totalSelfPayable;
											else
												creaditPayableSecondRound=secondRound.getBalanceCapAmount();
										}
										else if(secondRound.getCreaditPayable() < totalSelfPayable)
										{
											if(secondRound.getCreaditPayable() < secondRound.getBalanceCapAmount())
												creaditPayableSecondRound=secondRound.getCreaditPayable();
											else
												creaditPayableSecondRound=secondRound.getBalanceCapAmount();
										}
									}
									
									totalCreditPayable = totalCreditPayable+creaditPayableSecondRound;
									totalSelfPayable = totalSelfPayable-creaditPayableSecondRound;
									
									if(creaditPayableSecondRound>0)
									{
										if(secondRound.getCoShare()!=null && secondRound.getCoShare()>0)
										{
											secondRounfCoShare  = ((secondRound.getCoShare()*0.01)*creaditPayableSecondRound);
											creaditPayableSecondRound = creaditPayableSecondRound - secondRounfCoShare ;
											totalSelfPayable = totalSelfPayable+secondRounfCoShare;
											totalCreditPayable =  totalCreditPayable-secondRounfCoShare;
										}
										
										if(preAuthAmount!=null)
										{
											if(creaditPayableSecondRound>preAuthAmount)
											{
												double diff = creaditPayableSecondRound - preAuthAmount;
												creaditPayableSecondRound = preAuthAmount;
												
												totalSelfPayable = totalSelfPayable+diff;
												totalCreditPayable =  totalCreditPayable-diff;
											}
											/*if(consumptionDto.getUpdateCap()==null && creaditPayableSecondRound>0)
												iContractConsumptionDao.updatePreAuthAmount(secondRound.getPreauthId(),creaditPayableSecondRound);*/	
										}
										
										consumptionDto.setContractId(secondRound.getContractId());
										if(consumptionDto.getUpdateCap()==null && secondRound.getBalanceCapAmount()!=null && creaditPayableSecondRound>0)
											iContractConsumptionDao.updateCapAmount(consumptionDto,creaditPayableSecondRound);
									}
									
								}
								
								if(secondRound.getType()==2)
								{
									
									if(consumptionDto.getAdmissionId()!=null)
										resDto.setDayDiff(GlobalCommonDateUtils.getDateDifference(GlobalCommonDateUtils.getDate(consumptionDto.getCurrentDate().substring(0,10),"yyyy-MM-dd"), GlobalCommonDateUtils.getDate(secondRound.getDoa().substring(0,10),"yyyy-MM-dd"))+1);
									
									if(secondRound.getTillDischarge()==true||secondRound.getApplicableDays()>=secondRound.getDayDiff())
									{
										secondRound.setType(3);
										if(secondRound.getApplicableDate()!=null && !(secondRound.getApplicableDate().equals(consumptionDto.getCurrentDate().substring(0,10))))
										{
											secondRound.setType(4);
											secondRound.setBalanceAmount(secondRound.getAmount());
											secondRound.setBalanceQuantity(secondRound.getQuantity());
											secondRound.setApplicableDate(consumptionDto.getCurrentDate().substring(0,10));
										}
										if((secondRound.getBalanceAmount()!=null && secondRound.getBalanceAmount()>0)||secondRound.getBalanceQuantity()!=null)
										{
										
											Double balCap = secondRound.getBalanceAmount();
											if(balCap!=null && balCap>0.00)
											{
												if(totalSelfPayable <= balCap)
												{
													creaditPayableSecondRound = totalSelfPayable;
												}else{
													creaditPayableSecondRound=balCap;
												}
												
												totalCreditPayable = totalCreditPayable+creaditPayableSecondRound;
												totalSelfPayable = totalSelfPayable-creaditPayableSecondRound;
												
												
												if(secondRound.getCoShare()!=null && secondRound.getCoShare()>0)
												{
													secondRounfCoShare  = ((secondRound.getCoShare()*0.01)*creaditPayableSecondRound);
													creaditPayableSecondRound = creaditPayableSecondRound - secondRounfCoShare ;
													totalSelfPayable = totalSelfPayable+secondRounfCoShare;
													totalCreditPayable =  totalCreditPayable-secondRounfCoShare;
												}
												
												if(preAuthAmount!=null)
												{
													if(creaditPayableSecondRound>preAuthAmount)
													{
														double diff = creaditPayableSecondRound - preAuthAmount;
														creaditPayableSecondRound = preAuthAmount;
														
														totalSelfPayable = totalSelfPayable+diff;
														totalCreditPayable =  totalCreditPayable-diff;
													}
													
													/*if(consumptionDto.getUpdateCap()==null && creaditPayableSecondRound>0)
														iContractConsumptionDao.updatePreAuthAmount(secondRound.getPreauthId(),creaditPayableSecondRound);*/
												}
												
												if(consumptionDto.getUpdateCap()==null && creaditPayableSecondRound.doubleValue()>0.00)
												{
													iContractConsumptionDao.updateServiceCapAmount(secondRound,creaditPayableSecondRound.doubleValue(),null); 
												}
												
											}
											if(secondRound.getBalanceQuantity()!=null)
											{
													
												UnitServiceTariffMasterDto unitServiceTariffMasterDto =new UnitServiceTariffMasterDto();
													unitServiceTariffMasterDto.setOrganizationId(secondRound.getOrganizationId());
													unitServiceTariffMasterDto.setUnitId(secondRound.getUnitId());
													unitServiceTariffMasterDto.setVisitTypeId(secondRound.getVisitTypeId());
													unitServiceTariffMasterDto.setBillingBedCategoryId(secondRound.getBillingBedCategoryId());
													unitServiceTariffMasterDto.setPatientTypeId(secondRound.getPatientCategoryId()!=null ? secondRound.getPatientCategoryId() : secondRound.getPatientTypeId()); 
													unitServiceTariffMasterDto.setPaymentEntitlementId(SELF);//Self
													unitServiceTariffMasterDto.setServiceId(secondRound.getServiceId());
													unitServiceTariffMasterDto.setOrderDate(secondRound.getCurrentDate());
													unitServiceTariffMasterDto.setTariffId(secondRound.getTariffId());
													unitServiceTariffMasterDto.setPatientClassId(secondRound.getPatientClassId());
													unitServiceTariffMasterDto = iEncounterMasterService.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
												rate = (unitServiceTariffMasterDto.getRate() != null ? unitServiceTariffMasterDto.getRate() : 0.0);
												
												if(secondRound.getTaxPercentage()!=null)
												{
													taxAmt = rate * (resDto.getTaxPercentage()*0.01);
													netAmt = rate+taxAmt;
												}
												else
													netAmt = rate;
												
												if(consumptionDto.getServiceChargeStatus()!=null && consumptionDto.getServiceChargeStatus()==0)
												{
													netAmt = netAmt/2;
												}
												
												if(secondRound.getBalanceQuantity()!=null && secondRound.getBalanceQuantity()>0)
												{
													orderDetailsPayeeMapperDtosList=new LinkedList<>();
													
													creaditPayableSecondRound = netAmt;
													
													totalCreditPayable = netAmt;
													totalSelfPayable = 0.0;
													
													if(secondRound.getCoShare()!=null && secondRound.getCoShare()>0)
													{
														secondRounfCoShare  = ((secondRound.getCoShare()*0.01)*creaditPayableSecondRound);
														creaditPayableSecondRound = creaditPayableSecondRound - secondRounfCoShare ;
														totalSelfPayable = totalSelfPayable+secondRounfCoShare;
														totalCreditPayable =  totalCreditPayable-secondRounfCoShare;
													}
													
													if(preAuthAmount!=null)
													{
														if(creaditPayableSecondRound>preAuthAmount)
														{
															double diff = creaditPayableSecondRound - preAuthAmount;
															creaditPayableSecondRound = preAuthAmount;
															
															totalSelfPayable = totalSelfPayable+diff;
															totalCreditPayable =  totalCreditPayable-diff;
														}
														/*if(consumptionDto.getUpdateCap()==null && creaditPayableSecondRound>0)
															iContractConsumptionDao.updatePreAuthAmount(secondRound.getPreauthId(),creaditPayableSecondRound);*/
													}
													
													if(consumptionDto.getUpdateCap()==null && creaditPayableSecondRound > 0)
													{
														iContractConsumptionDao.updateServiceCapAmount(secondRound,null,1.00); 
													}
														
												}
											}
										}
									}
								}
							
								if(creaditPayableSecondRound>0)
								{
									detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
										detailsPayeeMapperDto.setPayeeId(secondRound.getPayeeId());
										detailsPayeeMapperDto.setAssociateCompanyId(secondRound.getAssociateCompanyId());
										detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(creaditPayableSecondRound));
										detailsPayeeMapperDto.setContractId(secondRound.getContractId());
										detailsPayeeMapperDto.setPayeeDesc(secondRound.getCompanyName());
										detailsPayeeMapperDto.setServiceId(serviceId);
										detailsPayeeMapperDto.setIsRateEditable('N');
										detailsPayeeMapperDto.setCoShare(secondRound.getCoShare());
										detailsPayeeMapperDto.setSpiltBillAmt(new BigDecimal(creaditPayableSecondRound));
									orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
								}
							
							}	
						count++;
						}
							
					}
					//1.3----->
					
					if((totalSelfPayable==0 && totalCreditPayable>0) || serviceDetailslist.size()==count)
					{	
						if(resDto.getBillingBedExist()!=null)
						{
							finalDto.setTaxAmt(taxAmt);
							finalDto.setCoShare(resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00);
							finalDto.setMinRateEditable(resDto.getMinRateEditable());
							finalDto.setMaxRateEditable(resDto.getMaxRateEditable());
							finalDto.setServiceRate(resDto.getServiceRate()!=null && resDto.getServiceRate()>0 ? resDto.getServiceRate() : rate);
							finalDto.setFinalPrice(resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt);
							finalDto.setNetAmount(resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt);
							finalDto.setOrdTotalAmount(resDto.getServiceRate()!=null && resDto.getServiceRate()>0 ? resDto.getServiceRate() : rate);
							finalDto.setCreaditPayable(totalCreditPayable);
							finalDto.setSelfPayable(totalSelfPayable);
							finalDto.setConcession(concession);
							finalDto.setPayeeId(resDto.getPayeeId());
							finalDto.setSelfPayeeId(resDto.getSelfPayeeId());
							finalDto.setServiceId(serviceId);
								detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
									detailsPayeeMapperDto.setPayeeId(resDto.getSelfPayeeId());
									detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(totalSelfPayable));
									detailsPayeeMapperDto.setServiceId(resDto.getServiceId());
									detailsPayeeMapperDto.setIsRateEditable('N');
									detailsPayeeMapperDto.setCoShare(0.00);
								orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
							finalDto.setTaxId(resDto.getTaxId());
							finalDto.setTaxPercentage(resDto.getTaxPercentage());
							finalDto.setContractId(resDto.getContractId());
							finalDto.setPatientClassPercentage(concessionPer);
							finalDto.setMarkupStatus(resDto.getMarkupStatus());
							finalDto.setSpecialityId(specialityId);
							finalDto.setSubSpecialityId(subSpecialityId);
							finalDto.setIsRateEditable(isRateEditable!=null ? isRateEditable : 'N');
							//finalDto.setIsRateEditableService(isRateEditableService!=null ? isRateEditableService : 'N');
							finalDto.setIsQuantityEditable(isQuantityEditable!=null ? isQuantityEditable : 'N');
							finalDto.setTariffId(tariffId);
							finalDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
							finalDto.setIsDocReq(resDto.getIsDocReq());
							finalDto.setIsRefReq(resDto.getIsRefReq());
							finalDto.setOrdDiscount(discountAmt);
							finalDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
							finalDto.setIsAutoRender(resDto.getIsAutoRender()!=null ? resDto.getIsAutoRender() : 'N');
							return new Response(SUCCESS,SUCCESS_CODE,null,serviceDetailslist,finalDto);
						}else
						{
							UnitServiceTariffMasterDto unitServiceTariffMasterDto =new UnitServiceTariffMasterDto();
								unitServiceTariffMasterDto.setOrganizationId(consumptionDto.getOrganizationId());
								unitServiceTariffMasterDto.setUnitId(consumptionDto.getUnitId());
								unitServiceTariffMasterDto.setVisitTypeId(consumptionDto.getVisitTypeId());
								unitServiceTariffMasterDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
								unitServiceTariffMasterDto.setPatientTypeId(consumptionDto.getPatientCategoryId()!=null ? consumptionDto.getPatientCategoryId() : consumptionDto.getPatientTypeId()); 
								unitServiceTariffMasterDto.setPaymentEntitlementId(SELF);//Self
								unitServiceTariffMasterDto.setServiceId(serviceId);
								unitServiceTariffMasterDto.setOrderDate(consumptionDto.getCurrentDate());
								unitServiceTariffMasterDto.setTariffId(resDto.getTariffId());
								unitServiceTariffMasterDto.setPatientClassId(consumptionDto.getPatientClassId());
							unitServiceTariffMasterDto = iEncounterMasterService.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
							
							if(consumptionDto.getPatientClassPercentage()!=null && consumptionDto.getPatientClassPercentage()>0.00)
								concessionPer = consumptionDto.getPatientClassPercentage();
							else {
								/*patientClassDto = iContractConsumptionDao.getPatientClassAndPercentageByVisit(consumptionDto);	
								if(patientClassDto != null && patientClassDto.getPatientClassPercentage()!=null 
										 && patientClassDto.getMarkupStatus()==false 
										 && patientClassDto.getPatientClassPercentage()>0
										 && consumptionDto.getOrdDiscount()==0)
									concessionPer = patientClassDto.getPatientClassPercentage();*/
								concessionPer =0.00;
							}
							
							
							if(consumptionDto.getChangedPrice()!=null && consumptionDto.getChangedPrice()>=0)
							{
								/*if(consumptionDto.getOrdDiscount()==0) 
								{
									rate = consumptionDto.getChangedPrice();
									concession = rate*(patientClassDto.getPatientClassPercentage()*0.01);
									consumptionDto.setConcession(concession);
									rateWithTax = (consumptionDto.getChangedPrice()-concession) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-concession));
								}else {
									concession = 0.00;
									rate = consumptionDto.getChangedPrice();
									consumptionDto.setConcession(concession);
									rateWithTax = (consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount())) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount())));
								}*/
								rate = consumptionDto.getChangedPrice();
								concession = (rate*concessionPer)*0.01;
								consumptionDto.setConcession(concession);
								if(consumptionDto.getOrdDiscountPer()>0)
								{
									rateMinusConcession = rate - concession;
									discountAmt = rateMinusConcession*(consumptionDto.getOrdDiscountPer()*0.01);
								}
									
								netAmt = (consumptionDto.getChangedPrice()-Math.abs(concession+discountAmt)) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-Math.abs(concession+discountAmt)));
								
							}else {
								
								rate = (unitServiceTariffMasterDto.getRate() != null ? unitServiceTariffMasterDto.getRate() : 0.0);
								
								if(consumptionDto.getConsultationAmount()!=null)
									rate = consumptionDto.getConsultationAmount();
								else if(consumptionDto.getProcedureId()!=null && consumptionDto.getProcedureId()>0)
									rate = consumptionDto.getServiceRate();
								else if(consumptionDto.getIsPeriodicityService()!=null && consumptionDto.getIsPeriodicityService()=='Y')
									rate = consumptionDto.getServiceRate();
								else
									rate = (unitServiceTariffMasterDto.getRate() != null ? unitServiceTariffMasterDto.getRate() : 0.0);
								
								if(consumptionDto.getServiceChargeStatus()!=null && consumptionDto.getServiceChargeStatus()==0)
								{
									rate = rate/2;
									unitServiceTariffMasterDto.setFinalRate(unitServiceTariffMasterDto.getFinalRate()!=null && unitServiceTariffMasterDto.getFinalRate().intValue()>0 ?  unitServiceTariffMasterDto.getFinalRate()/2 : 0);
								}
							
								if(concessionPer>0)
								{
									concession = rate*(concessionPer*0.01);
								}
								
								netAmt = (rate-(concession+discountAmt)) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(rate-Math.abs(concession+discountAmt)));
							}
							
							if(netAmt > (resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt))
							{
								Double difference = netAmt - (resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt);
								
									finalDto.setTaxAmt((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(rate-(concession+discountAmt)));
									finalDto.setTaxId(resDto.getTaxId());
									finalDto.setTaxPercentage(resDto.getTaxPercentage());
									finalDto.setMinRateEditable(resDto.getMinRateEditable());
									finalDto.setMaxRateEditable(resDto.getMaxRateEditable());
									finalDto.setServiceRate(resDto.getServiceRate()!=null && resDto.getServiceRate()>0 ? resDto.getServiceRate() : rate);
									finalDto.setFinalPrice(resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt);
									finalDto.setNetAmount(resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt);
									finalDto.setOrdTotalAmount(resDto.getServiceRate()!=null && resDto.getServiceRate()>0 ? resDto.getServiceRate() : rate);
									finalDto.setCreaditPayable(totalCreditPayable);
									finalDto.setSelfPayable(totalSelfPayable+difference);
									finalDto.setConcession(concession);
									finalDto.setPayeeId(resDto.getPayeeId());
									finalDto.setSelfPayeeId(resDto.getSelfPayeeId());
									finalDto.setServiceId(serviceId);
										detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
											detailsPayeeMapperDto.setPayeeId(resDto.getSelfPayeeId());
											detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(totalSelfPayable+difference));
											detailsPayeeMapperDto.setServiceId(serviceId);
											detailsPayeeMapperDto.setIsRateEditable('N');
											detailsPayeeMapperDto.setCoShare(0.00);
										orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
									finalDto.setCoShare(resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00);
									finalDto.setTaxPercentage(resDto.getTaxPercentage());
									finalDto.setContractId(resDto.getContractId());
									finalDto.setPatientClassPercentage(concessionPer);
									finalDto.setMarkupStatus(resDto.getMarkupStatus());
									finalDto.setSpecialityId(specialityId);
									finalDto.setSubSpecialityId(subSpecialityId);
									finalDto.setIsRateEditable(isRateEditable!=null ? isRateEditable : 'N');
									//finalDto.setIsRateEditableService(isRateEditableService!=null ? isRateEditableService : 'N');
									finalDto.setIsQuantityEditable(isQuantityEditable!=null ? isQuantityEditable : 'N');
									finalDto.setTariffId(tariffId);
									finalDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
									finalDto.setIsDocReq(resDto.getIsDocReq());
									finalDto.setIsRefReq(resDto.getIsRefReq());
									finalDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
									finalDto.setIsAutoRender(resDto.getIsAutoRender()!=null ? resDto.getIsAutoRender() : 'N');
								return new Response(SUCCESS,SUCCESS_CODE,null,null,finalDto);
							}
							else
							{
									finalDto.setTaxAmt(taxAmt);
									finalDto.setTaxId(resDto.getTaxId());
									finalDto.setTaxPercentage(resDto.getTaxPercentage());
									finalDto.setMinRateEditable(resDto.getMinRateEditable());
									finalDto.setMaxRateEditable(resDto.getMaxRateEditable());
									finalDto.setServiceRate(resDto.getServiceRate()!=null && resDto.getServiceRate()>0 ? resDto.getServiceRate() : rate);
									finalDto.setFinalPrice(resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt);
									finalDto.setNetAmount(resDto.getFinalPrice()!=null && resDto.getFinalPrice()>0 ? resDto.getFinalPrice() : netAmt);
									finalDto.setOrdTotalAmount(resDto.getServiceRate()!=null && resDto.getServiceRate()>0 ? resDto.getServiceRate() : rate);
									finalDto.setCreaditPayable(totalCreditPayable);
									finalDto.setSelfPayable(totalSelfPayable);
									finalDto.setConcession(concession);
									finalDto.setPayeeId(resDto.getPayeeId());
									finalDto.setSelfPayeeId(resDto.getSelfPayeeId());
									finalDto.setServiceId(serviceId);
										detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
											detailsPayeeMapperDto.setPayeeId(resDto.getSelfPayeeId());
											detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(totalSelfPayable));
											detailsPayeeMapperDto.setServiceId(serviceId);
											detailsPayeeMapperDto.setIsRateEditable('N');
											detailsPayeeMapperDto.setCoShare(0.00);
										orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
									finalDto.setCoShare(resDto.getCoShare()!=null ? resDto.getCoShare() : 0.00);
									finalDto.setTaxPercentage(resDto.getTaxPercentage());
									finalDto.setContractId(resDto.getContractId());
									finalDto.setPatientClassPercentage(resDto.getPatientClassPercentage());
									finalDto.setMarkupStatus(resDto.getMarkupStatus());
									finalDto.setSpecialityId(specialityId);
									finalDto.setSubSpecialityId(subSpecialityId);
									finalDto.setIsRateEditable(isRateEditable!=null ? isRateEditable : 'N');
									//finalDto.setIsRateEditableService(isRateEditableService!=null ? isRateEditableService : 'N');
									finalDto.setIsQuantityEditable(isQuantityEditable!=null ? isQuantityEditable : 'N');
									finalDto.setTariffId(tariffId);
									finalDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
									finalDto.setIsDocReq(resDto.getIsDocReq());
									finalDto.setIsRefReq(resDto.getIsRefReq());
									finalDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
									finalDto.setIsAutoRender(resDto.getIsAutoRender()!=null ? resDto.getIsAutoRender() : 'N');
								return new Response(SUCCESS,SUCCESS_CODE,null,null,finalDto);
							}
							
						}
					
					}
					
				}
				//1.1 ----->
				
				if(consumptionDto.getContractId()!=null && totalCreditPayable==0.00)
				{
					Double rateWithTax = 0.00;
					Integer fallBackTariffId =  iContractConsumptionDao.getFallBacktariff(consumptionDto.getEncounterId(),consumptionDto.getAdmissionId());
					
					consumptionDto.setTariffId(fallBackTariffId);
					
					if(consumptionDto.getPatientClassPercentage()!=null && consumptionDto.getPatientClassPercentage()>0.00)
						concessionPer = consumptionDto.getPatientClassPercentage();
					else {
						/*patientClassDto = iContractConsumptionDao.getPatientClassAndPercentageByVisit(consumptionDto);	
						if(patientClassDto != null && patientClassDto.getPatientClassPercentage()!=null 
								 && patientClassDto.getMarkupStatus()==false 
								 && patientClassDto.getPatientClassPercentage()>0
								 && consumptionDto.getOrdDiscount()==0)
							concessionPer = patientClassDto.getPatientClassPercentage();*/
						concessionPer=0.00;
					}
					
					
					
					if(fallBackTariffId!=null && fallBackTariffId>0)
					{
						UnitServiceTariffMasterDto unitServiceTariffMasterDto =new UnitServiceTariffMasterDto();
							unitServiceTariffMasterDto.setOrganizationId(consumptionDto.getOrganizationId());
							unitServiceTariffMasterDto.setUnitId(consumptionDto.getUnitId());
							unitServiceTariffMasterDto.setVisitTypeId(consumptionDto.getVisitTypeId());
							unitServiceTariffMasterDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
							unitServiceTariffMasterDto.setPatientTypeId(consumptionDto.getPatientCategoryId()!=null ? consumptionDto.getPatientCategoryId() : consumptionDto.getPatientTypeId()); 
							unitServiceTariffMasterDto.setPaymentEntitlementId(SELF);//Self
							unitServiceTariffMasterDto.setServiceId(serviceId);
							unitServiceTariffMasterDto.setOrderDate(consumptionDto.getCurrentDate());
							unitServiceTariffMasterDto.setTariffId(fallBackTariffId);
							unitServiceTariffMasterDto.setPatientClassId(consumptionDto.getPatientClassId());
							unitServiceTariffMasterDto = iEncounterMasterService.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
						
						if(unitServiceTariffMasterDto !=null)
						{
							if(consumptionDto.getChangedPrice()!=null && consumptionDto.getChangedPrice()>=0)
							{
								/*if(consumptionDto.getOrdDiscount()==0) 
								{
									rate = consumptionDto.getChangedPrice();
									concession = rate*(patientClassDto.getPatientClassPercentage()*0.01);
									consumptionDto.setConcession(concession);
									rateWithTax = (consumptionDto.getChangedPrice()-concession) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-concession));
								}else {
									concession = 0.00;
									rate = consumptionDto.getChangedPrice();
									consumptionDto.setConcession(concession);
									rateWithTax = (consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount())) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount())));
								}*/
								rate = consumptionDto.getChangedPrice();
								concession = (rate*concessionPer)*0.01;
								consumptionDto.setConcession(concession);
								if(consumptionDto.getOrdDiscountPer()>0)
								{
									rateMinusConcession = rate - concession;
									discountAmt = rateMinusConcession*(consumptionDto.getOrdDiscountPer()*0.01);
								}
									
								rateWithTax = (consumptionDto.getChangedPrice()-Math.abs(concession+discountAmt)) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-Math.abs(concession+discountAmt)));
								
							}else {
								
								
								
								if(consumptionDto.getConsultationAmount()!=null)
									rate = consumptionDto.getConsultationAmount();
								else if(consumptionDto.getProcedureId()!=null && consumptionDto.getProcedureId()>0)
									rate = consumptionDto.getServiceRate();
								else if(consumptionDto.getIsPeriodicityService()!=null && consumptionDto.getIsPeriodicityService()=='Y')
									rate = consumptionDto.getServiceRate();
								else
									rate = (unitServiceTariffMasterDto.getRate() != null ? unitServiceTariffMasterDto.getRate() : 0.0);
								
								if(consumptionDto.getServiceChargeStatus()!=null && consumptionDto.getServiceChargeStatus()==0)
								{
									rate = rate/2;
									unitServiceTariffMasterDto.setFinalRate(unitServiceTariffMasterDto.getFinalRate()!=null && unitServiceTariffMasterDto.getFinalRate().intValue()>0 ?  unitServiceTariffMasterDto.getFinalRate()/2 : 0);
								}
							
								if(concessionPer>0)
								{
									concession = rate*(concessionPer*0.01);
								}
								rateWithTax = (rate-(concession+discountAmt)) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(rate-Math.abs(concession+discountAmt)));
							}
							
							if(rate>0)
							{
									finalDto.setTaxId(unitServiceTariffMasterDto.getTaxId());
									finalDto.setTaxAmt((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(rate-Math.abs(concession+discountAmt)));
									finalDto.setTaxPercentage(unitServiceTariffMasterDto.getTaxPercentage());
									finalDto.setMinRateEditable(unitServiceTariffMasterDto.getMinRateEditable());
									finalDto.setMaxRateEditable(unitServiceTariffMasterDto.getMaxRateEditable());
									finalDto.setServiceRate(rate!=null ? rate : 0.00);
									finalDto.setFinalPrice(rateWithTax!=null ? rateWithTax : 0.00);
									finalDto.setNetAmount(rateWithTax);
									finalDto.setOrdTotalAmount(rate !=null ? rate : 0.00);
									finalDto.setConcession(concession);
									finalDto.setCreaditPayable(0.00);
									finalDto.setSelfPayable(rateWithTax!=null ? rateWithTax : 0.00);
									finalDto.setServiceId(serviceId);
									finalDto.setCoShare(100.00);
										detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
											detailsPayeeMapperDto.setPayeeId(unitServiceTariffMasterDto.getSelfPayeeId());
											detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(rateWithTax!=null ? rateWithTax : 0.00));
											detailsPayeeMapperDto.setPayeeDesc("Self");
											detailsPayeeMapperDto.setServiceId(serviceId);
											detailsPayeeMapperDto.setIsRateEditable('N');
											detailsPayeeMapperDto.setCoShare(0.00);
										orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
									finalDto.setPayeeId(consumptionDto.getSelfPayeeId());
									finalDto.setSelfPayeeId(unitServiceTariffMasterDto.getSelfPayeeId());
									finalDto.setPatientClassPercentage(concessionPer);
									finalDto.setMarkupStatus(unitServiceTariffMasterDto.getMarkupStatus());
									finalDto.setIsRateEditable(unitServiceTariffMasterDto.getIsRateEditable());
									finalDto.setSpecialityId(unitServiceTariffMasterDto.getSpecialityId());
									finalDto.setSubSpecialityId(unitServiceTariffMasterDto.getSubSpecialityId());
									//finalDto.setIsRateEditableService(unitServiceTariffMasterDto.getIsRateEditableService()!=null ? unitServiceTariffMasterDto.getIsRateEditableService() : 'N');
									finalDto.setIsQuantityEditable(unitServiceTariffMasterDto.getIsQuantityEditable()!=null ? unitServiceTariffMasterDto.getIsQuantityEditable() : 'N');
									finalDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
									finalDto.setIsDocReq(unitServiceTariffMasterDto.getIsDocReq());
									finalDto.setIsRefReq(unitServiceTariffMasterDto.getIsRefReq());
									finalDto.setOrdDiscount(discountAmt);
									finalDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
									finalDto.setIsAutoRender(unitServiceTariffMasterDto.getIsAutoRender()!=null ? unitServiceTariffMasterDto.getIsAutoRender() : 'N');
								return new Response(SUCCESS,SUCCESS_CODE,null,serviceDetailslist,finalDto);
							}
						}else{
							
							finalDto.setTaxAmt(0.00);
							finalDto.setOrdDiscount(0.00);
							finalDto.setServiceRate(0.00);
							finalDto.setFinalPrice(0.00);
							finalDto.setNetAmount(0.00);
							finalDto.setOrdTotalAmount(0.00);
							finalDto.setConcession(0.00);
							finalDto.setCreaditPayable(0.00);
							finalDto.setSelfPayable(0.00);
							finalDto.setOrdDiscount(0.00);
							finalDto.setServiceId(serviceId);
								detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
									detailsPayeeMapperDto.setPayeeId(consumptionDto.getSelfPayeeId());
									detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
									detailsPayeeMapperDto.setPayeeDesc("Self");
									detailsPayeeMapperDto.setServiceId(serviceId);
									detailsPayeeMapperDto.setIsRateEditable('N');
									detailsPayeeMapperDto.setCoShare(0.00);
								orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
							finalDto.setPayeeId(consumptionDto.getSelfPayeeId());
							finalDto.setPatientClassPercentage(0.00);
							finalDto.setIsRateEditable('Y');
							finalDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
							finalDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
							finalDto.setIsAutoRender('N');
							finalDto.setDoesNotExist('N');
							return new Response(SUCCESS,SUCCESS_CODE,"Service is not mapped with the tariff..!!",null,finalDto);
						}
					}
				}
			}
			//1----->
			if(consumptionDto.getContractId()==null || totalCreditPayable==0.00 )
			{
				Double rateWithTax = 0.00;
				
				UnitServiceTariffMasterDto unitServiceTariffMasterDto =new UnitServiceTariffMasterDto();
					unitServiceTariffMasterDto.setOrganizationId(consumptionDto.getOrganizationId());
					unitServiceTariffMasterDto.setUnitId(consumptionDto.getUnitId());
					unitServiceTariffMasterDto.setVisitTypeId(consumptionDto.getVisitTypeId());
					unitServiceTariffMasterDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
					unitServiceTariffMasterDto.setPatientTypeId(consumptionDto.getPatientCategoryId()!=null ? consumptionDto.getPatientCategoryId() : consumptionDto.getPatientTypeId()); 
					unitServiceTariffMasterDto.setPaymentEntitlementId(SELF);//Self
					unitServiceTariffMasterDto.setServiceId(serviceId);
					unitServiceTariffMasterDto.setOrderDate(consumptionDto.getCurrentDate());
					unitServiceTariffMasterDto.setTariffId((consumptionDto.getTariffId()!=null && consumptionDto.getTariffId()>0)?consumptionDto.getTariffId():consumptionDto.getDefaultSelfTariffId());
					unitServiceTariffMasterDto.setPatientClassId(consumptionDto.getPatientClassId());
					unitServiceTariffMasterDto = iEncounterMasterService.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
				
				if(unitServiceTariffMasterDto !=null && unitServiceTariffMasterDto.getUnitServiceTriffId()!=null)
				{
					if(consumptionDto.getPatientClassPercentage()!=null && consumptionDto.getPatientClassPercentage()>0.00)
						concessionPer = consumptionDto.getPatientClassPercentage();
					else if((unitServiceTariffMasterDto.getIsDiscount()!=null && unitServiceTariffMasterDto.getIsSchemeDiscount()!=null && (unitServiceTariffMasterDto.getIsDiscount()=='Y' && unitServiceTariffMasterDto.getIsSchemeDiscount()=='Y')||(unitServiceTariffMasterDto.getIsSchemeDiscount()=='N'))) 
					{
						/*if(consumptionDto.getIsFromEncounter()!=null) 
						{
							patientClassDto = iContractConsumptionDao.getPatientClassAndPercentageByVisitWithountEncounter(consumptionDto);
						}else if((consumptionDto.getEncounterId()==null || consumptionDto.getEncounterId()==0) && consumptionDto.getAdmissionId()==null)	
						{
							patientClassDto = iContractConsumptionDao.getPatientClassAndPercentageByVisitWithountEncounter(consumptionDto);
						}else {
							patientClassDto = iContractConsumptionDao.getPatientClassAndPercentageByVisit(consumptionDto);	
						}*/
						
						/*if(patientClassDto != null && patientClassDto.getPatientClassPercentage()!=null 
									 && patientClassDto.getMarkupStatus()==false 
									 && patientClassDto.getPatientClassPercentage()>0)
						{
							concessionPer = patientClassDto.getPatientClassPercentage();
						}*/
						concessionPer =0.00;
					}
					
					if(consumptionDto.getChangedPrice()!=null && consumptionDto.getChangedPrice()>=0)
					{
						/*if(consumptionDto.getOrdDiscount()==0) 
						{
							rate = consumptionDto.getChangedPrice();
							concession = rate*((patientClassDto!=null && patientClassDto.getMarkupStatus()==false && patientClassDto.getPatientClassPercentage()>0 ?patientClassDto.getPatientClassPercentage():0)*0.01);
							consumptionDto.setConcession(concession);
							rateWithTax = (consumptionDto.getChangedPrice()-concession) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-concession));
						}else {
							concession = 0.00;
							rate = consumptionDto.getChangedPrice();
							consumptionDto.setConcession(concession);
							rateWithTax = (consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount())) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-Math.abs(concession-consumptionDto.getOrdDiscount())));
						}*/
						rate = consumptionDto.getChangedPrice();
						concession = rate*(concessionPer>0 ? concessionPer:0)*0.01;
						consumptionDto.setConcession(concession);
						if(consumptionDto.getOrdDiscountPer()>0)
						{
							rateMinusConcession = rate - concession;
							discountAmt = rateMinusConcession*(consumptionDto.getOrdDiscountPer()*0.01);
						}
							
						rateWithTax = (consumptionDto.getChangedPrice()-Math.abs(concession+discountAmt)) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(consumptionDto.getChangedPrice()-Math.abs(concession+discountAmt)));
						
					}else {
						
						if(consumptionDto.getConsultationAmount()!=null)
							rate = consumptionDto.getConsultationAmount();
						else if(consumptionDto.getProcedureId()!=null && consumptionDto.getProcedureId()>0)
							rate = consumptionDto.getServiceRate();
						else if(consumptionDto.getIsPeriodicityService()!=null && consumptionDto.getIsPeriodicityService()=='Y')
							rate = consumptionDto.getServiceRate();
						else
							rate = (unitServiceTariffMasterDto.getRate() != null ? unitServiceTariffMasterDto.getRate() : 0.0);
						
						if(consumptionDto.getServiceChargeStatus()!=null && consumptionDto.getServiceChargeStatus()==0)
						{
							rate = rate/2;
							unitServiceTariffMasterDto.setFinalRate(unitServiceTariffMasterDto.getFinalRate()!=null && unitServiceTariffMasterDto.getFinalRate().intValue()>0 ?  unitServiceTariffMasterDto.getFinalRate()/2 : 0);
						}
					
						if(concessionPer > 0.00)
						{
							concession = rate*(concessionPer*0.01);
						}
						
						rateWithTax = (rate-Math.abs(concession+discountAmt)) + ((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(rate-Math.abs(concession+discountAmt)));
					}
						
					finalDto.setTaxId(unitServiceTariffMasterDto.getTaxId());
					finalDto.setOrdDiscount(discountAmt);
					finalDto.setTaxAmt((unitServiceTariffMasterDto.getTaxPercentage()*0.01)*(rate-Math.abs(concession+consumptionDto.getOrdDiscount())));
					finalDto.setTaxPercentage(unitServiceTariffMasterDto.getTaxPercentage()!=null?unitServiceTariffMasterDto.getTaxPercentage():00);
					finalDto.setSpecialityName(unitServiceTariffMasterDto.getSpecialityDesc());
					finalDto.setServiceName(unitServiceTariffMasterDto.getServiceDesc());
					finalDto.setMinRateEditable(unitServiceTariffMasterDto.getMinRateEditable());
					finalDto.setMaxRateEditable(unitServiceTariffMasterDto.getMaxRateEditable());
					finalDto.setServiceRate(rate !=null ? rate : 0.00);
					finalDto.setFinalPrice(rateWithTax!=null ? rateWithTax : 0.00);
					finalDto.setNetAmount(rateWithTax);
					finalDto.setOrdTotalAmount(rate !=null ? rate : 0.00);
					finalDto.setConcession(concession);
					finalDto.setCreaditPayable(0.00);
					finalDto.setCoShare(100.00);
					finalDto.setSelfPayable(rateWithTax!=null ? rateWithTax : 0.00);
					finalDto.setSpecialityId(unitServiceTariffMasterDto.getSpecialityId());
					finalDto.setSubSpecialityId(unitServiceTariffMasterDto.getSubSpecialityId());
					finalDto.setServiceId(serviceId);
						detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
							detailsPayeeMapperDto.setPayeeId(unitServiceTariffMasterDto.getSelfPayeeId());
							detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(rateWithTax!=null ? rateWithTax : 0.00));
							detailsPayeeMapperDto.setPayeeDesc("Self");
							detailsPayeeMapperDto.setServiceId(serviceId);
							detailsPayeeMapperDto.setIsRateEditable('N');
							detailsPayeeMapperDto.setCoShare(0.00);
						orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
					finalDto.setPayeeId(unitServiceTariffMasterDto.getSelfPayeeId());
					finalDto.setSelfPayeeId(unitServiceTariffMasterDto.getSelfPayeeId());
					finalDto.setPatientClassPercentage(concessionPer);
					finalDto.setMarkupStatus(unitServiceTariffMasterDto.getMarkupStatus());
					finalDto.setIsRateEditable(unitServiceTariffMasterDto.getIsRateEditable());
					finalDto.setSpecialityId(unitServiceTariffMasterDto.getSpecialityId());
					finalDto.setSubSpecialityId(unitServiceTariffMasterDto.getSubSpecialityId());
					//finalDto.setIsRateEditable(unitServiceTariffMasterDto.getIsRateEditableService()!=null ? unitServiceTariffMasterDto.getIsRateEditableService() : 'N');
					finalDto.setIsQuantityEditable(unitServiceTariffMasterDto.getIsQuantityEditable()!=null ? unitServiceTariffMasterDto.getIsQuantityEditable() : 'N');
					finalDto.setIsDocReq(unitServiceTariffMasterDto.getIsDocReq());
					finalDto.setIsRefReq(unitServiceTariffMasterDto.getIsRefReq());
					finalDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
					finalDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
					finalDto.setIsAutoRender(unitServiceTariffMasterDto.getIsAutoRender()!=null ? unitServiceTariffMasterDto.getIsAutoRender() : 'N');
					return new Response(SUCCESS,SUCCESS_CODE,null,serviceDetailslist,finalDto);
				}else{
						finalDto.setTaxAmt(0.00);
						finalDto.setServiceRate(0.00);
						finalDto.setFinalPrice(0.00);
						finalDto.setNetAmount(0.00);
						finalDto.setOrdTotalAmount(0.00);
						finalDto.setConcession(0.00);
						finalDto.setCreaditPayable(0.00);
						finalDto.setSelfPayable(0.00);
						finalDto.setOrdDiscount(0.00);
						finalDto.setCoShare(100.00);
						finalDto.setTaxPercentage(0.00);
						finalDto.setServiceId(serviceId);
							detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
								detailsPayeeMapperDto.setPayeeId(consumptionDto.getSelfPayeeId());
								detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
								detailsPayeeMapperDto.setPayeeDesc("Self");
								detailsPayeeMapperDto.setServiceId(serviceId);
								detailsPayeeMapperDto.setIsRateEditable('N');
								detailsPayeeMapperDto.setCoShare(0.00);
							orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
						finalDto.setPayeeId(consumptionDto.getSelfPayeeId());
						finalDto.setPatientClassPercentage(0.00);
						finalDto.setIsRateEditable('Y');
						finalDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
						finalDto.setBillingBedCategoryId(consumptionDto.getBillingBedCategoryId());
						finalDto.setIsAutoRender('N');
						finalDto.setDoesNotExist('N');
					return new Response(SUCCESS,SUCCESS_CODE,"Service is not mapped with the tariff..!!",null,finalDto);
				}
				
			}	
			
		}catch(Exception e){
			logger.error("consumeServiceFromContract failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response consumeConsultationServiceFromContract(ContractConsumptionDto consumptionDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response addCapAmount(ContractConsumptionDto consumptionDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getVertualServiceDetailsByContractId(ContractConsumptionDto consumptionDto,
			EncounterMasterDto encounterMasterDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getActivePaymentEntitlement(ContractConsumptionDto consumptionDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response consumeServiceFromContractForPeriodicityBilling(ContractConsumptionDto consumptionDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response consumePackageFromContract(ServiceSearchResDto searchResDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response consumeProcedureFromContract(ContractConsumptionDto consumptionDto, boolean isProcedure)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
