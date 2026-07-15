package com.param.service.services;

import com.param.global.common.Response;
import com.param.service.dto.FundInFlowDetailsDto;
import com.param.service.dto.TrustMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IFundInFlowMasterService  {

	public Response getTrustList(Integer orgId, Integer unitId)throws ApplicationException;

	public Response getTrustDetailsById(TrustMasterDto trustMtrustIdasterDto)throws ApplicationException;

	public Response saveFundDeatils(FundInFlowDetailsDto fundInFlowDetailsDto)throws ApplicationException;


	public Response getCollectionList(Integer orgId, Integer unitId,FundInFlowDetailsDto fundInFlowDetailsDto)throws ApplicationException;

	public Response getFundCollectionListByTrust(Integer orgId, Integer unitId,
			FundInFlowDetailsDto fundInFlowDetailsDto)throws ApplicationException;

	public Response getfundInflowDetailsByTrust(FundInFlowDetailsDto fundInFlowDetailsDto)throws ApplicationException;

	public Response getAllfundInflowDetailsByTrust(FundInFlowDetailsDto fundInFlowDetailsDto)throws ApplicationException;

	public Response getInflowDetailsByFundInflowId(FundInFlowDetailsDto fundInFlowDetailsDto)throws ApplicationException;


}
