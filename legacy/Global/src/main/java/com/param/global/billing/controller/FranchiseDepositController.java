package com.param.global.billing.controller;

import com.param.global.billing.dto.FrDepositSearchReqDto;
import com.param.global.billing.dto.FranchiseDepositDetailsDto;
import com.param.global.billing.dto.FranchiseDepositReqDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.billing.service.IFrenchiseDepositService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.utility.TimeZoneComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/billing/unit/franchise")
public class FranchiseDepositController implements ICommonConstants {

    @Autowired
    private IFrenchiseDepositService iFrenchiseDepositService;

    @Autowired
    private ISmsUtilityConfigService smsUtilityService;

    @Autowired
    private IDepositMasterService iDepositMasterService;

    @RequestMapping(value = "",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
    public synchronized Response addFrenchiseDeposit(@RequestBody FranchiseDepositReqDto reqDto){
        try{


            if(reqDto.getFranchiseDepositDetailsDtoList() != null) {
                for (FranchiseDepositDetailsDto dto : reqDto.getFranchiseDepositDetailsDtoList()) {

                    if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) {
                        Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
                        if(resCcavenue.getObject() == null) {
                            return new Response<>(ERROR, ERROR_CODE, dto.getTransactionNo()+" This already used in other trancation", null, null);
                        }
                    }
                }

            }
            Response res =   iFrenchiseDepositService.addFrenchiseDeposit(reqDto);
            Date currentDate = TimeZoneComponent.getDateByzone(reqDto.getFranchiseDepositMasterDto().getUnitId());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String strCurrentDate = formatter.format(currentDate);
            return res;
        }catch(Exception e){
            
            throw new BillingException(e.getMessage());
        }
    }

    @RequestMapping(value = "/all" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response getFrenchiseDeposit(@RequestBody FrDepositSearchReqDto serachDto){
        try{
            return iFrenchiseDepositService.getFrenchiseDeposit(serachDto);
        }catch(Exception e){
            
            throw new BillingException(e.getMessage());
        }
    }

    @RequestMapping(value = "/details/{frDepositId}" , method = RequestMethod.GET)
    public Response getFrenchiseDepositDetails(@PathVariable Integer frDepositId){
        try{
            return iFrenchiseDepositService.getFrenchiseDepositDetails(frDepositId);
        }catch(Exception e){
            
            throw new BillingException(e.getMessage());
        }
    }
}
