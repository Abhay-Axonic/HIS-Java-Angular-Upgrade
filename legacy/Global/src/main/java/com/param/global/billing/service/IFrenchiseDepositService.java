package com.param.global.billing.service;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.DepositCollectionReqDto;
import com.param.global.billing.dto.FrDepositSearchReqDto;
import com.param.global.billing.dto.FranchiseDepositReqDto;
import com.param.global.common.Response;

public interface IFrenchiseDepositService {

    public Response addFrenchiseDeposit(FranchiseDepositReqDto reqDto)throws BillingException;
    public Response getFrenchiseDeposit(FrDepositSearchReqDto serchDto)throws BillingException;
    public Response getFrenchiseDepositDetails(Integer frDepositId)throws BillingException;
}
