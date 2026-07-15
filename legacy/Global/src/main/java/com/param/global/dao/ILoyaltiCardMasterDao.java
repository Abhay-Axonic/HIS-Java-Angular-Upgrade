package com.param.global.dao;

import java.math.BigInteger;

import com.param.global.common.Response;
import com.param.global.dto.LoyaltiCardMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface ILoyaltiCardMasterDao {
	
	public Response saveLoyaltiCardMaster(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
	
	public Response updateLoyaltiCardMasterStatus(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
		
	public Response getLoyaltiCardMasterList(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
	
	public Response getLoyaltiCardMasterCount(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
		
	public Response checkLoyaltyCardNumberExistOrNot(int orgId,String cardNo) throws ApplicationException;
	

}
