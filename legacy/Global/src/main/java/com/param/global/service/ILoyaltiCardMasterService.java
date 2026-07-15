package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.LoyaltiCardMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface ILoyaltiCardMasterService {
	
	public Response saveLoyaltiCardMaster(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
	
	public Response updateLoyaltiCardMasterStatus(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
		
	public Response getLoyaltiCardMasterList(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
	
	public Response getLoyaltiCardMasterCount(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException;
	
	public Response getLoyaltiCardNumber(int orgId,int unitId) throws ApplicationException;
	
	public Response checkLoyaltycardNumber(int orgId,int unitId,String cardNo) throws ApplicationException;
	
	
}
