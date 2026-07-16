package com.param.global.billing.service;


import com.param.billing.global.model.FranchiseDepositMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IFrenchiseDepositDao;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dao.IPaymentFrDepositDetailsDao;
import com.param.global.billing.dto.*;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.org.common.dao.IPatientLoyaltyCardMapperDao;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.utility.TimeZoneComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FrenchiseDepositServiceImpl implements  IFrenchiseDepositService, ICommonConstants, IBillingCommonConstants {

    @Autowired
    private IFrenchiseDepositDao iFrenchiseDepositDao;

    @Autowired
    private IMpesaMasterDao iMpesaMasterDao;

    @Autowired
    private IPaymentFrDepositDetailsDao iPaymentFrDepositDetailsDao;

    @Autowired
    private IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;

    @Autowired
    private IPatientLoyaltyCardMapperDao iPatientLoyaltyCardMapperDao;

    @Override
    @Transactional
    public Response addFrenchiseDeposit(FranchiseDepositReqDto reqDto) throws BillingException {
        try{

            FranchiseDepositMaster frDepositMaster =null;
            Integer orgId = reqDto.getFranchiseDepositMasterDto().getOrgId();
            Integer unitId = reqDto.getFranchiseDepositMasterDto().getUnitId();
            Integer createdBy = reqDto.getFranchiseDepositMasterDto().getCreatedBy();
            Date date = TimeZoneComponent.getDateByzone(unitId);
            String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
            List<PatientLoyaltyCardMapperDto> mailList =  new ArrayList<PatientLoyaltyCardMapperDto>();
            ListIterator<FranchiseDepositDetailsDto> paymentMPesaItr = reqDto.getFranchiseDepositDetailsDtoList().listIterator();
            FranchiseDepositDetailsDto detailsMPesaDto = null;
            //get current deposit id
            //save deposit master
            FranchiseDepositMasterDto frDeposit = reqDto.getFranchiseDepositMasterDto();
            frDeposit.setAvailableDeposit(frDeposit.getAmount());
            frDeposit.setConsumedDeposit(0.0);
            frDeposit.setCreatedDate(dateTime);


            frDeposit.setFrDepositNo(String.valueOf(createdBy+unitId+System.currentTimeMillis()));
            frDeposit.setUpdatedBy(frDeposit.getCreatedBy());
            frDeposit.setUpdatedDate(dateTime);
            if(frDeposit.getSignetureImage()!=null && !frDeposit.getSignetureImage().isEmpty())
            {

                //BASE64Decoder decoder = new BASE64Decoder();
                //byte[] signature = decoder.decodeBuffer(deposit.getSignetureImage().split(",")[1]);
                byte[]  signature = Base64.getDecoder().decode(new String(frDeposit.getSignetureImage().split(",")[1]).getBytes("UTF-8"));
                //byte[]  signature = Base64.getDecoder().decode(new String(deposit.getSignetureImage()).getBytes("UTF-8"));

                frDeposit.setSignature(signature);
            }
            Response depositRes = iFrenchiseDepositDao.addFrenchiseDeposit(frDeposit);
            if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null){
                frDepositMaster = (FranchiseDepositMaster) depositRes.getObject();
                frDeposit.setFrDepositId(frDepositMaster.getFrDepositId());
                //save payment details
                ListIterator<FranchiseDepositDetailsDto> paymentItr = reqDto.getFranchiseDepositDetailsDtoList().listIterator();
                FranchiseDepositDetailsDto detailsDto = null;
                while(paymentItr.hasNext())
                {
                    detailsDto = paymentItr.next();
                    detailsDto.setCreatedDate(dateTime);
                    detailsDto.setFrDepositId(frDepositMaster.getFrDepositId());
                    detailsDto.setUpdatedBy(frDepositMaster.getCreatedBy());
                    detailsDto.setUpdatedDate(dateTime);
                    detailsDto.setBalanceAmount(detailsDto.getAmount());
                    detailsDto.setRefundAmount(0.00);
                    detailsDto.setUnitId(unitId);
                    iPaymentFrDepositDetailsDao.savePaymentFrDepositDetails(detailsDto);
                }
            }

            MpesaMasterDto masterDto = new MpesaMasterDto();
            while(paymentMPesaItr.hasNext()) {
                detailsMPesaDto = paymentMPesaItr.next();
                if(detailsMPesaDto.getPaymentModeId() == 5 && (detailsMPesaDto.getSkipMpesaValidation()!=null && detailsMPesaDto.getSkipMpesaValidation()))
                {
                    masterDto.setMpesaTxcode(detailsMPesaDto.getCardNo());
                    masterDto.setUnitId(unitId);
                    masterDto.setOrganizationId(orgId);
                    masterDto.setUpdatedBy(detailsMPesaDto.getCreatedBy());
                    masterDto.setMpesaAmount(detailsMPesaDto.getAmount());
                    iMpesaMasterDao.validateMpesaTrancastion(masterDto);
                }


                if(detailsMPesaDto.getPaymentModeId() == 12)
                {
                    if(detailsMPesaDto.getPaymentGatewayProviderId() == 1) {

                        iMpesaMasterDao.updateRazorPay(detailsMPesaDto.getTransactionNo());

                    }else if(detailsMPesaDto.getPaymentGatewayProviderId() == 2){

                        iMpesaMasterDao.updateCCAvenue(detailsMPesaDto.getTransactionNo(),frDepositMaster.getFrDepositId());
                    }

                }
                if(detailsMPesaDto.getPaymentModeId() == 3)
                {
                    POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
                    pOSMachineResponseMapperDto.setCreatedDate(dateTime);
                    pOSMachineResponseMapperDto.setUpdatedDate(dateTime);
                    pOSMachineResponseMapperDto.setUnitId(unitId);
                    pOSMachineResponseMapperDto.setOrganizationId(orgId);
                    pOSMachineResponseMapperDto.setUpdatedBy(detailsMPesaDto.getCreatedBy());
                    pOSMachineResponseMapperDto.setIsConsumed(true);
                    pOSMachineResponseMapperDto.setRequestedUniqueId(detailsMPesaDto.getRequestedUniqueId());
                    iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
                }

                if(detailsMPesaDto.getPaymentModeId()!=null && detailsMPesaDto.getPaymentModeId().intValue()==6)
                {
                    PatientLoyaltyCardMapperDto dto =new PatientLoyaltyCardMapperDto();
                    dto.setCardAmount(detailsMPesaDto.getAmount());
                    dto.setCardNo(detailsMPesaDto.getCardNo());
                    dto.setCreatedBy(detailsMPesaDto.getCreatedBy());
                    dto.setCreatedDate(dateTime);
                    dto.setOrganisationId(orgId);
                    dto.setUnitId(unitId);
                    Response loyalityCardResponse=iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(dto);
                    if(loyalityCardResponse.getListObject().size() >0 && loyalityCardResponse.getListObject()!=null){
                        PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)loyalityCardResponse.getListObject().getFirst();
                        //checking that front end amount should be less than or equal to card amount
                        if(patientLoyaltyCardMapperDto.getCardAmount()>=detailsMPesaDto.getAmount())
                        {
                            patientLoyaltyCardMapperDto.setConsumeAmount(detailsMPesaDto.getAmount());
                            iPatientLoyaltyCardMapperDao.updateLoyaltyCardAmountByCard(dto);
                            mailList.add(patientLoyaltyCardMapperDto);
                        }else{
                            return new Response(ERROR, ERROR_CODE, "Amount should be less than or equals to card amount", null, 0.0);
                        }
                    }

                }
            }
            return new Response<>(SUCCESS, SUCCESS_CODE, DEPOSIT_ADDED_MSG + frDepositMaster.getFrDepositNo(), null, frDepositMaster.getFrDepositId(),frDepositMaster);
        }catch(BillingException be){
            throw be;
        }catch(Exception e){
            
            throw new BillingException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Response getFrenchiseDeposit(FrDepositSearchReqDto serchDto) throws BillingException {
        try{
            return iFrenchiseDepositDao.getFrenchiseDeposit(serchDto);
        }catch(BillingException be){
            throw be;
        }catch(Exception e){
            
            throw new BillingException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Response getFrenchiseDepositDetails(Integer frDepositId) throws BillingException {
        try{
            return iFrenchiseDepositDao.getFrenchiseDepositDetails(frDepositId);
        }catch(BillingException be){
            throw be;
        }catch(Exception e){
            
            throw new BillingException(e.getMessage());
        }
    }
}
