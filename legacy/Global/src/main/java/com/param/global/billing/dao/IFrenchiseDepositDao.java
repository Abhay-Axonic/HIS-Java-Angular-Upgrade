package com.param.global.billing.dao;

import com.param.billing.global.model.FranchiseDepositMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.FrDepositSearchReqDto;
import com.param.global.billing.dto.FranchiseDepositMasterDto;
import com.param.global.billing.dto.FranchiseDepositReqDto;
import com.param.global.common.Response;

public interface IFrenchiseDepositDao {

    public Response addFrenchiseDeposit(FranchiseDepositMasterDto dto)throws BillingException;
    public Response getFrenchiseDeposit(FrDepositSearchReqDto serchDto)throws BillingException;
    public Response getFrenchiseDepositDetails(Integer frDepositId)throws BillingException;
}
