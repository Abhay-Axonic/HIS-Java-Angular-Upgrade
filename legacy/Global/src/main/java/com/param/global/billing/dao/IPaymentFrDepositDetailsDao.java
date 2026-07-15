package com.param.global.billing.dao;

import com.param.billing.global.model.FranchiseDepositDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.FranchiseDepositDetailsDto;
import com.param.global.common.Response;
import in.param.common.dao.IGenericDao;

public interface IPaymentFrDepositDetailsDao extends IGenericDao<FranchiseDepositDetails, Integer> {

    public Response savePaymentFrDepositDetails(FranchiseDepositDetailsDto dto)throws BillingException;
}
