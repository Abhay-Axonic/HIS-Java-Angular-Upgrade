package com.param.global.common;

import static com.param.global.common.ICommonConstants.SELF;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.param.billing.global.CommonGSTCalculation;
import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.ITPackageConsumptionCapDetailsDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PackageConsumptionMasterResDto;
import com.param.global.dto.PackageConsumptionServiceDetailDto;
import com.param.global.dto.ServiceForPackageReqDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;
import com.param.global.dto.UpdatePayeeDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

@Configuration
public class BillingCommonMethods<T> {

    @Autowired
    IContractConsumptionDao iContractConsumptionDao;
    @Autowired
    ITPackageConsumptionCapDetailsDao iTPackageConsumptionCapDetailsDao;
    @Autowired
    private IOrderMasterDao iOrderMasterDao;

    public ContractConsumptionDto taxCalculation(Double rate, Double concession, double discountAmt,
                                                 Integer billingBedCategoryId, ContractConsumptionDto resDto, Double serviceReteAfterCalculaion) {

        ContractConsumptionDto dto = resDto;
        List<ContractConsumptionDto> gstDetailsList = iContractConsumptionDao.getgstDetails(billingBedCategoryId);
        for (ContractConsumptionDto gstList : gstDetailsList) {
            if (gstList.getIsGstApplicable() == true && serviceReteAfterCalculaion >= gstList.getGstAmountRange()) {
                dto.setTaxId(0);
                dto.setTaxPercentage(gstList.getGstPercentage().doubleValue());
                dto.setTaxAmt((rate - (concession + discountAmt)) * (gstList.getGstPercentage() * 0.01));
            } else {
                dto.setTaxId(0);
                dto.setTaxPercentage(0.00);
                dto.setTaxAmt(0.00);
            }
        }

        return dto;
    }


    public ContractConsumptionDto calculateSelfCreditAndFinalAmount(ContractConsumptionDto consumptionDto,
                                                                    ContractConsumptionDto dto, Double qty, Boolean gstApplicableForBillingBedCategory, Double concessionPer, Double serviceAmount1, Boolean checkConsultaionService) {

        ContractConsumptionDto resDto = dto;

        Double concession = 0.00;
        double discountAmt = 0.00;
        Double taxAmt = 0.0;
        Double contractRate = null;
        Double rate = null;

        concession = (concessionPer != null && concessionPer > 0.00) ? ((serviceAmount1 * qty) * (concessionPer * 0.01)) : 0.00;
        discountAmt = consumptionDto.getOrdDiscountPer() > 0 ? (((serviceAmount1 * qty) - concession) * (consumptionDto.getOrdDiscountPer() * 0.01)) : 0.00;

        contractRate = (resDto.getFinalPrice() != null ? resDto.getFinalPrice() : serviceAmount1);

        if (gstApplicableForBillingBedCategory == true && consumptionDto.getIsBedService().equals('Y')) {
        	
            Double serviceReteAfterCalculaion = CommonGSTCalculation.calculateServiceRateForGST(serviceAmount1,concessionPer,consumptionDto.getOrdDiscountPer());
            resDto = this.taxCalculation((serviceAmount1 * qty), concession, discountAmt, consumptionDto.getBillingBedCategoryId(), resDto,serviceReteAfterCalculaion);
            taxAmt = resDto.getTaxAmt();
        } else {
            taxAmt = resDto.getTaxPercentage() != null ? (((serviceAmount1 * qty) - Math.abs(concession + discountAmt)) * (resDto.getTaxPercentage() * 0.01)) : 0.00;
        }

        resDto.setTaxAmt(taxAmt);
        resDto.setFinalPrice(((serviceAmount1 * qty) - (Math.abs(concession + discountAmt))) + taxAmt);
        resDto.setFinalPrice(Double.valueOf(OrderMasterServiceImpl.df2.format(resDto.getFinalPrice())));
        resDto.setServiceRate(serviceAmount1);
        serviceAmount1 = (serviceAmount1 * qty) + taxAmt;
        if (checkConsultaionService) {

            if (resDto.getIsTariffRateApplicable() == 'Y') {
                resDto.setSelfPayable(resDto.getFinalPrice() * ((resDto.getCoShare() != null ? resDto.getCoShare() : 0.00) * 0.01));
                resDto.setSelfPayable(Double.valueOf(OrderMasterServiceImpl.df2.format(resDto.getSelfPayable())));
                resDto.setCreaditPayable(resDto.getFinalPrice() - resDto.getSelfPayable());

            } else {

                rate = resDto.getFinalPrice();
                resDto.setSelfPayable(rate >= contractRate ? (resDto.getSelfPayable() + (rate - contractRate)) : (resDto.getCreaditPayable() >= rate ? 0.00 : (contractRate - rate)));
                resDto.setSelfPayable(Double.valueOf(OrderMasterServiceImpl.df2.format(resDto.getSelfPayable())));
                resDto.setCreaditPayable(rate >= contractRate ? resDto.getCreaditPayable() : (resDto.getCreaditPayable() >= rate ? rate : resDto.getCreaditPayable()));
            }

        } else {

            resDto.setSelfPayable(resDto.getFinalPrice() * ((resDto.getCoShare() != null ? resDto.getCoShare() : 0.00) * 0.01));
            resDto.setSelfPayable(Double.valueOf(OrderMasterServiceImpl.df2.format(resDto.getSelfPayable())));
            resDto.setCreaditPayable(resDto.getFinalPrice() - resDto.getSelfPayable());
        }

        resDto.setOrdDiscount(discountAmt);
        resDto.setConcession(concession);
        
        return resDto;
    }

    public OrderDetailsPackageMapperDto createOrderDetailsPackageMapper(ContractConsumptionDto consumptionDto, Double serviceAmt,
                                                                        OrderDetailsMasterDto ordDtlsDto, OrderDetailsMaster detailsMaster) {

        OrderDetailsPackageMapperDto detailsPackageMapper = new OrderDetailsPackageMapperDto();
        detailsPackageMapper.setIsPackageService('N');
        detailsPackageMapper.setIsCapService('N');
        detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
        detailsPackageMapper.setStatus('A');
        if (consumptionDto != null && serviceAmt != null && ordDtlsDto == null) {
            detailsPackageMapper.setServiceId(consumptionDto.getServiceId());
            detailsPackageMapper.setServiceAmount(BigDecimal.valueOf(serviceAmt));
        } else if (ordDtlsDto != null && consumptionDto == null && serviceAmt == null) {
            detailsPackageMapper.setServiceAmount(ordDtlsDto.getSelfPayable());
        }else{
        	detailsPackageMapper.setOrderDetailsId(detailsMaster.getOrderDetailsId());
			detailsPackageMapper.setServiceId(detailsMaster.getServiceId());
			detailsPackageMapper.setServiceAmount(detailsMaster.getServiceAmt());
			detailsPackageMapper.setOrganizationId(detailsMaster.getOrgId());
			detailsPackageMapper.setUnitId(detailsMaster.getOrgUnitId());
        }

        return detailsPackageMapper;
    }

    public List<OrderDetailsPayeeMapperDto> createOrderDetailsPayeeMapperDtos(ContractConsumptionDto contractConsumptionDto, ContractConsumptionDto consumptionDto, BillingMasterDto billingMasterDto, OrderDetailsMasterDto ordDtlsDto) {
        List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
        if (consumptionDto != null && contractConsumptionDto != null) {
            OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
            detailsPayeeMapperDto.setPayeeId(contractConsumptionDto.getSelfPayeeId());
            detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
            detailsPayeeMapperDto.setPayeeDesc("Self");
            detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
            detailsPayeeMapperDto.setIsRateEditable('N');
            detailsPayeeMapperDto.setCoShare(0.00);
            list.add(detailsPayeeMapperDto);
        } else if (billingMasterDto != null && ordDtlsDto != null) {
            OrderDetailsPayeeMapperDto dto = new OrderDetailsPayeeMapperDto();
            dto.setOrderDetailsId(ordDtlsDto.getOrderDetailsId());
            dto.setPayeeAmount(ordDtlsDto.getSelfPayable());
            dto.setPayeeId(billingMasterDto.getSelfPayeeId());
            dto.setStatus('A');
            dto.setOrganizationId(billingMasterDto.getOrganisationId());
            dto.setUnitId(billingMasterDto.getUnitId());
            list.add(dto);
        }
        return list;
    }

    public OrderDetailsPackageMapperDto createOrderDetailsPackageMapperDtoAndUpdatePackageCap(PackageConsumptionMasterResDto resDto, TPackageConsumptionCapDetailsDto capDtls, ContractConsumptionDto consumptionDto, Double consumeAmt) {
        OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
        oDPmpprDto.setPackageId(resDto.getPackageId());
        oDPmpprDto.setIsPackageService('Y');
        oDPmpprDto.setIsCapService('Y');
        oDPmpprDto.setPackageName(resDto.getPackageName());
        oDPmpprDto.setPackageCode(resDto.getPackageCode());
        oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
        oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
        oDPmpprDto.setPackageAmount(BigDecimal.valueOf(consumeAmt));
        oDPmpprDto.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
        consumptionDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(consumptionDto.getServiceRate())));
        iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), consumeAmt * -1);
        return oDPmpprDto;
    }

    public OrderDetailsPackageMapperDto createOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(PackageConsumptionMasterResDto resDto, PackageConsumptionServiceDetailDto srv,
                                                                                                     ContractConsumptionDto consumptionDto, Double consumeAmt, Character isCapService, Boolean isUpdatePackageService) {
        OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
        oDPmpprDto.setPackageId(resDto.getPackageId());
        oDPmpprDto.setIsPackageService('Y');
        oDPmpprDto.setIsCapService(isCapService);
        oDPmpprDto.setPackageName(resDto.getPackageName());
        oDPmpprDto.setPackageCode(resDto.getPackageCode());

        oDPmpprDto.setPackageAmount(BigDecimal.valueOf(consumeAmt));
        oDPmpprDto.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
        consumptionDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()
                .add(BigDecimal.valueOf(consumeAmt)));
        if (isUpdatePackageService) {
            iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(),
                    consumeAmt * -1);
            oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
            oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
        }
        return oDPmpprDto;
    }


    public OrderDetailsPackageMapperDto createOrderDetailsPackageMapperForMultiEncounter(PackageConsumptionMasterResDto resDto, PackageConsumptionServiceDetailDto srv,
                                                                                         Integer availableQty, ContractConsumptionDto consumptionDto, Integer serviceQty, Boolean setPkgAmt) {
        OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
        oDPmpprDto.setPackageId(resDto.getPackageId());
        oDPmpprDto.setIsPackageService('Y');
        oDPmpprDto.setIsCapService('N');
        oDPmpprDto.setPackageName(resDto.getPackageName());
        oDPmpprDto.setPackageCode(resDto.getPackageCode());
        oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
        oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
        if (setPkgAmt)
            oDPmpprDto.setPackageAmount(BigDecimal.valueOf(consumptionDto.getSelfPayable()));
        if (consumptionDto.getConsumeService() == 0) {
            if (availableQty >= serviceQty) {
                oDPmpprDto.setPackageQty(serviceQty);
                availableQty = availableQty - serviceQty;
                consumptionDto.setServiceRate(srv.getAvailableCap());
                consumptionDto.setOrdTotalAmount(consumptionDto.getServiceRate()
                        * serviceQty.doubleValue());
                consumptionDto.setPatientClassPercentage(0.0);
                consumptionDto.setOrdDiscountPer(0.0);
                consumptionDto.setNetAmount(consumptionDto.getFinalPrice());
                consumptionDto.setTaxId(null);
                consumptionDto.setTaxAmt(0.0);
                consumptionDto.setSelfPayable(0.0);
                consumptionDto.setPackageConsumptionAmt(BigDecimal.valueOf(srv.getAvailableCap()));
                oDPmpprDto.setPackageQty(serviceQty);
                oDPmpprDto.setPackageAmount(new BigDecimal(consumptionDto.getOrdTotalAmount()));
                oDPmpprDto.setServiceAmount(new BigDecimal(consumptionDto.getServiceRate()));

                iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(),
                        (serviceQty) * -1);
                srv.setBalanceQuantity(availableQty);
            } else {
                Double ratePerQty = consumptionDto.getServiceRate().doubleValue();
                serviceQty = serviceQty - availableQty;

                consumptionDto.setPackageConsumptionAmt(new BigDecimal(availableQty * srv.getAvailableCap()));
                consumptionDto.setServiceRate(ratePerQty);
                consumptionDto.setOrdTotalAmount(consumptionDto.getServiceRate()
                        * serviceQty.doubleValue());
                consumptionDto.setPatientClassPercentage(0.0);
                consumptionDto.setOrdDiscountPer(0.0);
                consumptionDto.setNetAmount(
                        consumptionDto.getOrdTotalAmount() - consumptionDto
                                .getPackageConsumptionAmt().doubleValue());
                consumptionDto.setSelfPayable(consumptionDto.getNetAmount());

                oDPmpprDto.setPackageQty(1);
                iTPackageConsumptionCapDetailsDao.updatePackageQty(
                        srv.getPackageConsumptionServiceDetailId(), (1 * -1));
                srv.setBalanceQuantity(0);
            }
        }
        return oDPmpprDto;
    }

    public ContractConsumptionDto updatePreAuthAndServiceCapAmountForFirstPriorityContract(
            ContractConsumptionDto consumptionDto, ContractConsumptionDto resDto, Double creditPayable, Double selfPayable, Double preAuthAmount,
            Double qtyToBeUpdated, Boolean fromContact, Boolean checkFlagUpdateCapOrServiceCap) {

        ContractConsumptionDto dto = new ContractConsumptionDto();

        if (fromContact && resDto.getCoShare() != null && creditPayable > 0) {
            selfPayable = selfPayable + (creditPayable * resDto.getCoShare() * 0.01);
            creditPayable = creditPayable - (creditPayable * resDto.getCoShare() * 0.01);
        }

        if (preAuthAmount != null && consumptionDto.isApplyAuth()) {
            if (creditPayable > preAuthAmount) {
                selfPayable = selfPayable + creditPayable - preAuthAmount;
                creditPayable = preAuthAmount;
            }
            if (consumptionDto.getUpdateCap() == null && creditPayable > 0)
                iContractConsumptionDao.updatePreAuthAmount(resDto.getPreauthId(), creditPayable);
        }

        if (checkFlagUpdateCapOrServiceCap) {

            if (consumptionDto.getUpdateCap() == null && creditPayable > 0) {
                consumptionDto.setSpecialityId(resDto.getSpecialityId());
                consumptionDto.setContractId(resDto.getContractId());
                iContractConsumptionDao.updateCapAmount(consumptionDto, creditPayable);
            }
        } else if (consumptionDto.getUpdateCap() == null && creditPayable > 0) {
            if (qtyToBeUpdated == null) {
                iContractConsumptionDao.updateServiceCapAmount(resDto, creditPayable.doubleValue(), null);
            } else {
                iContractConsumptionDao.updateServiceCapAmount(resDto, null, (qtyToBeUpdated != null && qtyToBeUpdated > 1) ? qtyToBeUpdated : 1.00);
            }

        }
        dto.setCreaditPayable(creditPayable);
        dto.setSelfPayable(selfPayable);

        return dto;
    }


    public ContractConsumptionDto updatePreAuthAndServiceCapAmountForSecondContract(ContractConsumptionDto secondRound,
                                                                                    Double creditPayableSecondRound, Double totalSelfPayable, Double totalCreditPayable,
                                                                                    Double secondRounfCoShare, Double preAuthAmount, ContractConsumptionDto consumptionDto, Boolean checkFlagUpdateCapOrServiceCap, Double qtyToBeUpdated, Boolean fromContact) {

        if (fromContact && secondRound.getCoShare() != null && secondRound.getCoShare() > 0) {
            secondRounfCoShare = ((secondRound.getCoShare() * 0.01) * creditPayableSecondRound);
            creditPayableSecondRound = creditPayableSecondRound - secondRounfCoShare;
            totalSelfPayable = totalSelfPayable + secondRounfCoShare;
            totalCreditPayable = totalCreditPayable - secondRounfCoShare;
        }

        if (preAuthAmount != null && consumptionDto.isApplyAuth()) {
            if (creditPayableSecondRound > preAuthAmount) {
                double diff = creditPayableSecondRound - preAuthAmount;
                creditPayableSecondRound = preAuthAmount;

                totalSelfPayable = totalSelfPayable + diff;
                totalCreditPayable = totalCreditPayable - diff;
            }
            if (consumptionDto.getUpdateCap() == null && creditPayableSecondRound > 0)
                iContractConsumptionDao.updatePreAuthAmount(secondRound.getPreauthId(), creditPayableSecondRound);
        }

        if (checkFlagUpdateCapOrServiceCap) {

            consumptionDto.setContractId(secondRound.getContractId());
            if (consumptionDto.getUpdateCap() == null && secondRound.getBalanceCapAmount() != null && creditPayableSecondRound > 0)
                iContractConsumptionDao.updateCapAmount(consumptionDto, creditPayableSecondRound);
        } else {

            if (consumptionDto.getUpdateCap() == null && creditPayableSecondRound.doubleValue() > 0.00) {
                if (qtyToBeUpdated == null) {
                    iContractConsumptionDao.updateServiceCapAmount(secondRound, creditPayableSecondRound.doubleValue(), null);
                } else {
                    iContractConsumptionDao.updateServiceCapAmount(secondRound, null, qtyToBeUpdated);
                }


            }
        }

        consumptionDto.setSelfPayable(totalSelfPayable);
        consumptionDto.setCreaditPayable(totalCreditPayable);

        return consumptionDto;
    }

    public ContractConsumptionDto updatePackageServiceCap(PackageConsumptionMasterResDto resDto, PackageConsumptionServiceDetailDto srv, ContractConsumptionDto consumptionDto, Double consumeAmt, Character isCapService, Boolean isUpdatePackageService) {
        if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
            consumptionDto.getOrderDetailsPackageMapperDtosList().add(createOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto, srv, consumptionDto, consumeAmt, isCapService, isUpdatePackageService));
        } else {
            List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
            mppr.add(createOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto, srv, consumptionDto, consumeAmt, isCapService, isUpdatePackageService));
            consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
        }
        return consumptionDto;
    }

    public ContractConsumptionDto updatePackageCap(PackageConsumptionMasterResDto resDto, TPackageConsumptionCapDetailsDto capDtls, ContractConsumptionDto consumptionDto, double capAmount) {
        if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
            consumptionDto.getOrderDetailsPackageMapperDtosList().add(createOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto, capDtls, consumptionDto, consumptionDto.getSelfPayable()));
        } else {
            List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
            mppr.add(createOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto, capDtls, consumptionDto, capAmount));
            consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
        }
        return consumptionDto;
    }

    public ContractConsumptionDto updatePackageForMultiEncounter(PackageConsumptionMasterResDto resDto, PackageConsumptionServiceDetailDto srv, Integer availableQty, ContractConsumptionDto consumptionDto, Integer serviceQty) {
        if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
                && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
            consumptionDto.getOrderDetailsPackageMapperDtosList()
                    .add(createOrderDetailsPackageMapperForMultiEncounter(resDto, srv, availableQty, consumptionDto, serviceQty, false));
        } else {
            List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
            mppr.add(createOrderDetailsPackageMapperForMultiEncounter(resDto, srv, availableQty, consumptionDto, serviceQty, true));
            consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
            consumptionDto.setSelfPayable(0.00);
        }
        return consumptionDto;
    }

    public UpdatePayeeDto createUpdatePayeeDto(BillingMasterDto billingMasterDto) {
        UpdatePayeeDto updatePayeeDto = new UpdatePayeeDto();
        updatePayeeDto.setOrgId(billingMasterDto.getOrganisationId());
        updatePayeeDto.setUnitId(billingMasterDto.getUnitId());
        updatePayeeDto.setPaymentEntitlementId(billingMasterDto.getPaymentEntitlementId());
        updatePayeeDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
        updatePayeeDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
        updatePayeeDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
        updatePayeeDto.setAdmissionId(billingMasterDto.getAdmissionId());
        updatePayeeDto.setEncounterId(billingMasterDto.getEncounterId());
        updatePayeeDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
        updatePayeeDto.setPatientId(billingMasterDto.getPatientId());
        updatePayeeDto.setPharmacyServiceId(billingMasterDto.getPharmacyServiceId());
        updatePayeeDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
        updatePayeeDto.setIsPreBilling(billingMasterDto.getIsPreBilling());
        updatePayeeDto.setContractId(billingMasterDto.getContractId());
        return updatePayeeDto;
    }

    public ServiceForPackageReqDto createPackageRateFromServiceForPackageDto(UpdatePayeeDto updatePayeeDto, OrderDetailsByEncounterIdDto dto) {
        ServiceForPackageReqDto reqDto = new ServiceForPackageReqDto();
        reqDto.setPaymentEntitlementId(updatePayeeDto.getPaymentEntitlementId());
        reqDto.setVisitTypeId(updatePayeeDto.getVisitTypeId());
        reqDto.setPatientTypeId(updatePayeeDto.getPatientCategoryId());
        reqDto.setBillingBedCategoryId(updatePayeeDto.getBillingBedCategoryId());
        reqDto.setServiceId(dto.getServiceMasterId());
        reqDto.setUnitId(updatePayeeDto.getUnitId());
        reqDto.setOrgId(updatePayeeDto.getOrgId());
        return reqDto;
    }

    public BillingMasterDto createBillingMasterDto(UpdatePayeeDto updatePayeeDto) {
        BillingMasterDto billDto = new BillingMasterDto();
        billDto.setOrganisationId(updatePayeeDto.getOrgId());
        billDto.setUnitId(updatePayeeDto.getUnitId());
        billDto.setCreatedBy(updatePayeeDto.getCreatedBy());
        billDto.setUpdatedBy(updatePayeeDto.getCreatedBy());
        billDto.setPatientId(updatePayeeDto.getPatientId());
        billDto.setVisitTypeId(updatePayeeDto.getVisitTypeId());
        billDto.setEncounterId(updatePayeeDto.getEncounterId());
        billDto.setAdmissionId(updatePayeeDto.getAdmissionId());
        billDto.setPaymentEntitlementId(updatePayeeDto.getPaymentEntitlementId());
        billDto.setPatientTypeId(updatePayeeDto.getPatientCategoryId());
        billDto.setBillingBedCategoryId(updatePayeeDto.getBillingBedCategoryId());
        billDto.setContractId(updatePayeeDto.getContractId());
        billDto.setTariffId(updatePayeeDto.getDefaultSelfTariffId());
        return billDto;
    }

    public ContractConsumptionDto createContractConsumption(BillingMasterDto billingMasterDto, OrderDetailsMasterDto detailsMasterDto) {
        ContractConsumptionDto addCapDto = new ContractConsumptionDto();
        addCapDto.setAdmissionId(billingMasterDto.getAdmissionId());
        addCapDto.setEncounterId(billingMasterDto.getEncounterId());
        addCapDto.setOrderDetailsPayeeMapperDtosList(detailsMasterDto.getOrderDetailsPayeeMapperDtosList());
        addCapDto.setOrderDetailsPackageMapperDtosList(detailsMasterDto.getOrderDetailsPackageMapperDtosList());
        addCapDto.setSpecialityId(detailsMasterDto.getDepartmentId());
        addCapDto.setApplyAuth(billingMasterDto.isApplyAuth());
        addCapDto.setQuantity(detailsMasterDto.getOrderQty().doubleValue());
        if (detailsMasterDto.getProcedureOrderMstId() != null && detailsMasterDto.getProcedureOrderMstId() > 0 && detailsMasterDto.getIsProcedureService() == 'Y') {
            addCapDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
            for (OrderDetailsPayeeMapperDto odPayeeDto : detailsMasterDto.getOrderDetailsPayeeMapperDtosList())
                odPayeeDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
            for (OrderDetailsPackageMapperDto odPayeeDto : detailsMasterDto.getOrderDetailsPackageMapperDtosList())
                odPayeeDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
        } else {
            addCapDto.setServiceId(detailsMasterDto.getServiceId());
        }
        return addCapDto;
    }

    public ContractConsumptionDto createContractConsumptionForClone(BillingMasterDto billingMasterDto, List<OrderDetailsMasterDto> listToBeUpdated) {
        ContractConsumptionDto contractDto = new ContractConsumptionDto();
        contractDto.setPatientId(billingMasterDto.getPatientId());
        contractDto.setEncounterId(billingMasterDto.getEncounterId() != null ? billingMasterDto.getEncounterId() : billingMasterDto.getAdmissionId());
        contractDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
        contractDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
        contractDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
        contractDto.setOrganizationId(billingMasterDto.getOrganisationId());
        contractDto.setUnitId(billingMasterDto.getUnitId());
        contractDto.setTariffId(billingMasterDto.getTariffId());
        contractDto.setPaymentEntitlementId(billingMasterDto.getPaymentEntitlementId());
        contractDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
        contractDto.setListOrderDetailsMasterDto(listToBeUpdated);
        contractDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
        contractDto.setPatientClassId(billingMasterDto.getPatientClassId());
        contractDto.setApplyAuth(billingMasterDto.isApplyAuth());
        return contractDto;
    }

    public AdmissionListIpdChargesResDto createAdmissionListIpdChargesResDto(ContractConsumptionDto contractDtoClone, Integer patientId) {
        AdmissionListIpdChargesResDto ipdChargesResDto = new AdmissionListIpdChargesResDto();
        ipdChargesResDto.setAdmissionId(contractDtoClone.getAdmissionId());
        ipdChargesResDto.setPatientId(patientId);
        ipdChargesResDto.setPatientCategoryId(contractDtoClone.getPatientCategoryId());
        ipdChargesResDto.setBedBillingCategoryId(contractDtoClone.getBillingBedCategoryId());
        ipdChargesResDto.setContractId(contractDtoClone.getContractId());
        return ipdChargesResDto;
    }

    public ServiceSearchResDto createServiceSearchResDto(ContractConsumptionDto contractDtoClone, OrderDetailsMasterDto ordDtlsDto, Boolean applyAuth, Boolean isAddPreAuth) {
        ServiceSearchResDto searchResDto = new ServiceSearchResDto();
        searchResDto.setAdmissionId(contractDtoClone.getAdmissionId());
        searchResDto.setEncounterId(contractDtoClone.getEncounterId());
        searchResDto.setContractId(contractDtoClone.getContractId());
        searchResDto.setServiceMasterId(contractDtoClone.getServiceId());
        searchResDto.setOrderDate(contractDtoClone.getCurrentDate());
        searchResDto.setOrganizationId(contractDtoClone.getOrganizationId());
        searchResDto.setUnitId(contractDtoClone.getUnitId());
        searchResDto.setVisitTypeId(contractDtoClone.getVisitTypeId());
        searchResDto.setBillingBedCategoryId(contractDtoClone.getBillingBedCategoryId());
        searchResDto.setPatientCategoryId(contractDtoClone.getPatientCategoryId() != null ? contractDtoClone.getPatientCategoryId() : contractDtoClone.getPatientTypeId());
        searchResDto.setDefaultSelfTariffId(contractDtoClone.getDefaultSelfTariffId());
        searchResDto.setPackageMasterId(ordDtlsDto.getPackageId());
        searchResDto.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
        searchResDto.setBasePrice(ordDtlsDto.getServiceAmt());
        searchResDto.setPatientClassPer(ordDtlsDto.getPatientClassPercentage() != null ? ordDtlsDto.getPatientClassPercentage().doubleValue() : 0.00);
        searchResDto.setApplyAuth(applyAuth != null ? applyAuth : isAddPreAuth);
        return searchResDto;
    }

    public EncounterMasterDto createEncounterMasterDto(BillingMasterDto billingMasterDto, Map.Entry<Integer, Integer> entry) {
        EncounterMasterDto dto = new EncounterMasterDto();
        dto.setOrderDetailsIdEhc(entry.getValue());
        dto.setEhcPackageId(entry.getKey());
        dto.setOrganizationId(billingMasterDto.getOrganisationId());
        dto.setUnitId(billingMasterDto.getUnitId());
        dto.setCreatedBy(billingMasterDto.getCreatedBy());
        dto.setUpdatedBy(billingMasterDto.getCreatedBy());
        dto.setEncounterId(billingMasterDto.getEncounterId());
        dto.setCreatedDate(billingMasterDto.getCreatedDate());
        return dto;
    }


    public OrderMasterDto createOrderMasterDto(BillingMasterDto billingMasterDto, Long orderDate, List<OrderDetailsMasterDto> listToBeAdded,Boolean setSelfPayee) {
        OrderMasterDto orderMasterDto = new OrderMasterDto();
        orderMasterDto.setPatientId(billingMasterDto.getPatientId());
        orderMasterDto.setEncounterId(billingMasterDto.getEncounterId());
        orderMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
        orderMasterDto.setCreatedBy(billingMasterDto.getCreatedBy());
        orderMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
        orderMasterDto.setCreatedDate(orderDate);
        orderMasterDto.setUpdatedDate(orderDate);
        orderMasterDto.setOrderDate(orderDate);
        orderMasterDto.setOrderStatus('A');
        orderMasterDto.setOrgId(billingMasterDto.getOrganisationId());
        orderMasterDto.setOrgUnitId(billingMasterDto.getUnitId());
        orderMasterDto.setOrderStatusId(1);// order status==1
        orderMasterDto.setAdmissionId(billingMasterDto.getAdmissionId());
        orderMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
        orderMasterDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
        orderMasterDto.setPaymentEntitlementTypeId(billingMasterDto.getPaymentEntitlementId());
        orderMasterDto.setReferralId(billingMasterDto.getReferralId());
        if(billingMasterDto.getOrdDoctorId() != null){
        	 orderMasterDto.setDoctorId(billingMasterDto.getOrdDoctorId());
        }
        if(billingMasterDto.getOrdDocSplId() != null){
        	   orderMasterDto.setOrdDocSplId(billingMasterDto.getOrdDocSplId());
       }
     
        if (setSelfPayee)
            orderMasterDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
        else {
            orderMasterDto.setPriorityId(2);
            orderMasterDto.setListOrderDetailsMasterDto(listToBeAdded);
            orderMasterDto.setDoctorId(billingMasterDto.getOrdDoctorId());
            orderMasterDto.setNetAmount(BigDecimal.valueOf(billingMasterDto.getNetAmount()));
            orderMasterDto.setReqDisAmt(billingMasterDto.getReqDisAmt());
            orderMasterDto.setDiscType(billingMasterDto.getDiscType());
            orderMasterDto.setRequestSendTo(billingMasterDto.getRequestSentTo());
            orderMasterDto.setLineLevelDiscountOrderList(billingMasterDto.getLineLevelDiscountOrderList());
        }
        return orderMasterDto;
    }

    public ServiceSearchResDto createServiceSearchResDto(BillingMasterDto billingMasterDto, OrderDetailsMasterDto orderDtls, Boolean isChangedPrice) {
        ServiceSearchResDto searchResDto = new ServiceSearchResDto();
        searchResDto.setAdmissionId(billingMasterDto.getAdmissionId());
        searchResDto.setEncounterId(billingMasterDto.getEncounterId());
        searchResDto.setContractId(billingMasterDto.getContractId());
        searchResDto.setServiceMasterId(orderDtls.getServiceId());
        searchResDto.setOrderDate(orderDtls.getOrderDate() != null ? GlobalCommonDateUtils.getStringDate(orderDtls.getOrderDate(), "yyyy-MM-dd HH:mm:ss") : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(orderDtls.getOrgUnitId()), "yyyy-MM-dd HH:mm:ss"));
        searchResDto.setOrganizationId(orderDtls.getOrgId() != null ? orderDtls.getOrgId() : billingMasterDto.getOrgId());
        searchResDto.setUnitId(orderDtls.getOrgUnitId() != null ? orderDtls.getOrgUnitId() : billingMasterDto.getUnitId());
        searchResDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
        searchResDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
        searchResDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
        searchResDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
        searchResDto.setPackageMasterId(orderDtls.getPackageId());
        searchResDto.setBasePrice(orderDtls.getServiceAmt());
        searchResDto.setOrdDiscAmt(orderDtls.getOrdDiscount().doubleValue());
        searchResDto.setPatientClassPer(orderDtls.getPatientClassPercentage() != null ? orderDtls.getPatientClassPercentage().doubleValue() : 0.00);
        searchResDto.setApplyAuth(billingMasterDto.isAddPreAuth());
        if (!isChangedPrice)
            searchResDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
        else {
            searchResDto.setChangedPrice(orderDtls.getServiceAmt().doubleValue());
            searchResDto.setOrdDiscPer(orderDtls.getServiceAmt().doubleValue() > 0 ? orderDtls.getOrdDiscountPer().doubleValue() : 0.00);
        }
        return searchResDto;
    }

    public ContractConsumptionDto updatePreAuthForPharmacyContract(ContractConsumptionDto resDto, Double creditPayable,
                                                                   Double selfPayable, PharmacyBillCommonDto dto, Map<Integer, Double> preAuthDetails) {

        ContractConsumptionDto res = new ContractConsumptionDto();
        if (resDto.getCoShare() != null) {
            selfPayable = GlobalCommonDateUtils.getAmount(selfPayable + (creditPayable * (resDto.getCoShare() / 100)));
            creditPayable = GlobalCommonDateUtils.getAmount(creditPayable - (creditPayable * (resDto.getCoShare() / 100)));
        }

        if (resDto.getPreauthId() != null && preAuthDetails.get(resDto.getPreauthId()) >= 0) {
            if (creditPayable > preAuthDetails.get(resDto.getPreauthId())) {
                selfPayable = selfPayable + creditPayable - preAuthDetails.get(resDto.getPreauthId());
                creditPayable = preAuthDetails.get(resDto.getPreauthId());
            }
            if (dto.getConsume() && creditPayable > 0)
                iContractConsumptionDao.updatePreAuthAmount(resDto.getPreauthId(), creditPayable);
        }

        res.setCreaditPayable(creditPayable);
        res.setSelfPayable(selfPayable);
        return res;
    }


    public CommonBillingItemsDto createSaleDetailsPayeeObj(CommonBillingItemsDto item, ContractConsumptionDto resDto,
                                                           Double creditPayable, Boolean tariffRateFlag, PharmacyBillCommonDto dto, Double selfPayable) {

        SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
        mpprDto.setItemId(item.getItemId());
        mpprDto.setQuantity(1);

        if (creditPayable != null) {
            mpprDto.setContractId(resDto.getContractId());
            mpprDto.setPayeeDesc(resDto.getCompanyName());
            mpprDto.setPayeeId(resDto.getPayeeId());
            mpprDto.setAssociateCompanyId(resDto.getAssociateCompanyId());
            item.setPayeeId(resDto.getPayeeId());
        } else {
            mpprDto.setPayeeId(dto.getSelfPayeeId());
        }

        mpprDto.setPayeeAmount(creditPayable != null ? BigDecimal.valueOf(creditPayable) : BigDecimal.valueOf(selfPayable));

        if (tariffRateFlag) {
            mpprDto.setIsTariffRateApplicable(1);
        }

        if (item.getSaleDetailPayeeMapperDtoList() != null && item.getSaleDetailPayeeMapperDtoList().size() > 0) {
            item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
        } else {
            List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
            mpprList.add(mpprDto);
            item.setSaleDetailPayeeMapperDtoList(mpprList);
        }
        return item;

    }


    public OrderDetailsPayeeMapperDto createOrderPayeeMapperObj(SaleDetailPayeeMapperDto mppr,
                                                                BillingMasterDto billingMasterDto) {

        OrderDetailsPayeeMapperDto mpprDto = new OrderDetailsPayeeMapperDto();
        mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
        mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
        mpprDto.setOrganizationId(billingMasterDto.getOrganisationId());
        mpprDto.setUnitId(billingMasterDto.getUnitId());
        mpprDto.setStatus('A');
        mpprDto.setPayeeAmount(mppr.getPayeeAmount());
        mpprDto.setSpiltBillAmt(mppr.getPayeeAmount());
        mpprDto.setContractId(mppr.getContractId());
        mpprDto.setPayeeId(mppr.getPayeeId());
       
        return mpprDto;
    }


    public OrderDetailsMasterDto setDefaultOrderDetailsParameter(OrderDetailsMasterDto dtoToAdd, Date orderDate,
                                                                 Integer orgId, Integer unitId) {

        dtoToAdd.setOrderSchDate(orderDate.getTime());
        dtoToAdd.setOrderDate(orderDate.getTime());
        dtoToAdd.setOrdRenderDatetime(orderDate.getTime());
        dtoToAdd.setIsPackage(false);
        dtoToAdd.setIsCapService('N');
        dtoToAdd.setIsInvestigationOrder('N');
        dtoToAdd.setIsProcedureService('N');
        dtoToAdd.setIsProcedure('N');
        dtoToAdd.setOrderEmergencyFlag('N');
        dtoToAdd.setIsAppointableRadiology('N');
        dtoToAdd.setOrdCancelled('N');
        dtoToAdd.setStatus('A');
        dtoToAdd.setOrgId(orgId);
        dtoToAdd.setOrgUnitId(unitId);
        dtoToAdd.setServiceRendered(1);
        dtoToAdd.setIsOutsourced('N');
        dtoToAdd.setIsConsultationService('N');
        dtoToAdd.setDebitNoteService(false);
        dtoToAdd.setPriorityId(2);
        dtoToAdd.setIsPackageService('N');
        return dtoToAdd;
    }

    public OrderDetailsPackageMapperDto createOrderDetailsPackageMapper(PackageConsumptionMasterResDto resDto, PackageConsumptionServiceDetailDto srv,
                                                                        BigDecimal packageAmt, BigDecimal serviceAmt, Character isCapService, Boolean isCapServiceUpdate) {
        OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
        oDPmpprDto.setPackageId(resDto.getPackageId());
        oDPmpprDto.setIsPackageService('Y');
        oDPmpprDto.setIsCapService(isCapService);
        oDPmpprDto.setPackageName(resDto.getPackageName());
        oDPmpprDto.setPackageCode(resDto.getPackageCode());

        oDPmpprDto.setPackageAmount(packageAmt);
        oDPmpprDto.setServiceAmount(serviceAmt);
        if (isCapServiceUpdate) {
            oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
            oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
            iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), packageAmt.doubleValue() * -1);
        }
        return oDPmpprDto;
    }

    public OrderDetailsPackageMapperDto createOrderDetailsPackageMapper(PackageConsumptionMasterResDto resDto, TPackageConsumptionCapDetailsDto capDtls,
                                                                        BigDecimal packageAmt, BigDecimal serviceAmt) {
        OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
        oDPmpprDto.setPackageId(resDto.getPackageId());
        oDPmpprDto.setIsPackageService('Y');
        oDPmpprDto.setIsCapService('Y');
        oDPmpprDto.setPackageName(resDto.getPackageName());
        oDPmpprDto.setPackageCode(resDto.getPackageCode());
        oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
        oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
        oDPmpprDto.setPackageAmount(packageAmt);
        oDPmpprDto.setServiceAmount(serviceAmt);
        iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), packageAmt.doubleValue() * -1);

        return oDPmpprDto;
    }

    public ContractConsumptionDto consumeServiceFromPackageOrContract(BillingMasterDto billingMasterDto, OrderDetailsMasterDto detailsMasterDto) {

        ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
        consumptionDto.setOrganizationId(billingMasterDto.getOrganisationId());
        consumptionDto.setUnitId(billingMasterDto.getUnitId());
        consumptionDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
        consumptionDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
        consumptionDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
        consumptionDto.setPaymentEntitlementId(SELF);
        consumptionDto.setServiceId(detailsMasterDto.getServiceId());
        if (detailsMasterDto.getOrdRenderDatetime() != null) {
            consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrdRenderDatetime(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
        }
        consumptionDto.setIsConsultationService(detailsMasterDto.getIsConsultationService()!=  null ?detailsMasterDto.getIsConsultationService() : 'N' );
        consumptionDto.setContractId(billingMasterDto.getContractId());
        consumptionDto.setEncounterId(billingMasterDto.getEncounterId());
        consumptionDto.setAdmissionId(billingMasterDto.getAdmissionId());
        consumptionDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
        consumptionDto.setPatientId(billingMasterDto.getPatientId());
        consumptionDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
        consumptionDto.setPatientClassId(billingMasterDto.getPatientClassId());
        consumptionDto.setServiceRate(detailsMasterDto.getServiceAmt().doubleValue());
        consumptionDto.setChangedPrice(detailsMasterDto.getServiceAmt().doubleValue());
        consumptionDto.setSpecialityId(detailsMasterDto.getDepartmentId());
        consumptionDto.setSubSpecialityId(detailsMasterDto.getSubDepartmentId());
        consumptionDto.setOrdDiscountPer(detailsMasterDto.getServiceAmt().doubleValue() > 0 ? detailsMasterDto.getOrdDiscountPer().doubleValue() : 0.00);
        consumptionDto.setPatientClassPercentage(detailsMasterDto.getServiceAmt().doubleValue() > 0 ? detailsMasterDto.getPatientClassPercentage().doubleValue() : 0.00);
        consumptionDto.setQuantity(detailsMasterDto.getOrderQty().doubleValue());
        consumptionDto.setDocSplId(detailsMasterDto.getOrdDocSplId());
        return consumptionDto;
    }


	public OrderDetailsPackageMapperDto createOrderPackageMapperForPharmacyObj(SaleDetailPackageMapperDto mppr,
			BillingMasterDto billingMasterDto, Integer orgId, Integer unitId, OrderDetailsMasterDto detail) {
		
		OrderDetailsPackageMapperDto mpprDto = new OrderDetailsPackageMapperDto();
		mpprDto.setOrganizationId((billingMasterDto != null && billingMasterDto.getOrganisationId() != null )? billingMasterDto.getOrganisationId() :orgId );
		mpprDto.setUnitId((billingMasterDto != null && billingMasterDto.getUnitId() != null ) ? billingMasterDto.getUnitId() :unitId );
		mpprDto.setStatus('A');
		mpprDto.setIsPackageService(mppr.getIsPackageItem());
		mpprDto.setPackageId(mppr.getPackageId());
		mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
		mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
		mpprDto.setServiceAmount(billingMasterDto != null ? BigDecimal.valueOf(mppr.getRate()): BigDecimal.valueOf(mppr.getRate()*detail.getOrderQty()));
		mpprDto.setPackageConsumptionMasterId(mppr.getPackageConsumptionMasterId());
		mpprDto.setPacakgeConsumptionServiceDetailId(mppr.getPacakgeConsumptionServiceDetailId());
		mpprDto.setPackageConsumptionCapDetailsId(mppr.getPackageConsumptionCapDetailsId());
		mpprDto.setPackageConsumptionEODetailId(mppr.getPackageConsumptionEODetailId());
		mpprDto.setPackageAmount(BigDecimal.valueOf(mppr.getPackageAmount()));
		mpprDto.setIsCapService('N');
		return mpprDto;
	}


	public OrderDetailsPayeeMapperDto createOrderDetaildPayeeObjFinal(Integer selfPayeeId, Double totalSelfPayable,
			Integer serviceId) {
		OrderDetailsPayeeMapperDto 	detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
		detailsPayeeMapperDto.setPayeeId(selfPayeeId);
		detailsPayeeMapperDto.setPayeeDesc("Self");
		detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(totalSelfPayable));
		detailsPayeeMapperDto.setServiceId(serviceId);
		detailsPayeeMapperDto.setIsRateEditable('N');
		detailsPayeeMapperDto.setCoShare(0.00);
		return detailsPayeeMapperDto;
	}



    public SaleDetailPackageMapperDto createSaleDetailPackageMapperDto(PackageConsumptionMasterResDto resDto, T srvCapDto,
                                                                       Character isPackageItem, Double packageAmount, Double unitNetAmountDto, Boolean consume,
                                                                       Integer capOrQty, Integer pharRecal) {
        SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
        oDPmpprDto.setPackageId(resDto.getPackageId());
        oDPmpprDto.setIsPackageItem(isPackageItem);
        oDPmpprDto.setPackageName(resDto.getPackageName());
        oDPmpprDto.setPackageCode(resDto.getPackageCode());
        oDPmpprDto.setPackageAmount(packageAmount);
        oDPmpprDto.setPackageQty(1);
        oDPmpprDto.setCapOrQty(capOrQty);
        oDPmpprDto.setRate(unitNetAmountDto);
        oDPmpprDto.setPackageConsumptionMasterId(-1);

        if (srvCapDto instanceof PackageConsumptionServiceDetailDto srv) {
            oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
            oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
            if (consume) {
                if (capOrQty > 1)
                    iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1*-1), (packageAmount*-1),false);
                else
                    iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (packageAmount * -1), false);
            }
        } else if (srvCapDto instanceof TPackageConsumptionCapDetailsDto capDtls) {
            oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
            oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
            if(consume) {
            	boolean pharmacyReturnFlag = false;
            	if(pharRecal!= null && pharRecal== 1) {
            		pharmacyReturnFlag = true;
            	}
                if (capOrQty > 1) {
                	
                	
                	
                    iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (1 * -1), (packageAmount * -1), false,pharmacyReturnFlag);
                } else {
                	
                    iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (packageAmount*-1),false,pharmacyReturnFlag);
           
                }
            }
        }
        return oDPmpprDto;
    }

    public Integer saveOrderMaster(OrderMasterDto orderMasterDto,String orderNo) {
        Response orderMasterResponse = null;
        Integer orderId = 0;
        Integer labOrderId = 0;
        Integer orderGenerationFlag = 0;
        Integer labOrderFlag = 0;
        String currentDate = GlobalCommonDateUtils.getStringDate(orderMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss");
        for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto())
        {
            if((orderDetailsMasterDto.getIsLabService() != null && orderDetailsMasterDto.getIsLabService().equals('Y'))
                    && ((orderDetailsMasterDto.getIsFromServiceSerach()!= null && orderDetailsMasterDto.getIsFromServiceSerach() == 'Y')
                    || (orderDetailsMasterDto.getIsFromAddservice()!= null && orderDetailsMasterDto.getIsFromAddservice() == 'Y')))
            {
                labOrderFlag = 1;

            }
        }

        if((labOrderFlag==1) && (orderMasterDto.getIsFutureService() == null || !orderMasterDto.getIsFutureService())) {// as discussion with Vishal
            OrderDetailsMasterDto OrderObj = iOrderMasterDao.getOrderIdForLabService(orderMasterDto.getEncounterId(),orderMasterDto.getAdmissionId());

            if(OrderObj != null){

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = simpleDateFormat.format(TimeZoneComponent.getDateByzone(orderMasterDto.getOrgUnitId()));

                if(OrderObj.getOrderDateString() != null && dateStr.equals(OrderObj.getOrderDateString())){
                    orderId = OrderObj.getOrderId()!= null ? OrderObj.getOrderId() : null;
                }
            }
        }
        if(labOrderFlag == 0 || orderId == null || orderId == 0 )// as discussion with Vishal
        {
            orderMasterDto.setCurrentDate(currentDate);
           // packageOrders = new LinkedHashMap<>();
            orderMasterDto.setOrderNo(orderNo);
            orderMasterResponse = iOrderMasterDao.saveOrderMaster(orderMasterDto);
            orderId = (Integer) orderMasterResponse.getObject();
        }
        return orderId;
    }
}
