package com.param.global.service;

import java.math.BigInteger;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ILoyaltiCardMasterDao;
import com.param.global.dao.ILoyaltyCardConfigurationDao;
import com.param.global.dto.LoyaltiCardMasterDto;
import com.param.global.dto.LoyaltyCardConfigurationDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class LoyaltiCardMasterServiceImpl implements ILoyaltiCardMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(LoyaltiCardMasterServiceImpl.class);


	@Autowired
	ILoyaltiCardMasterDao iLoyaltiCardMasterDao;
	

	@Autowired
	ILoyaltyCardConfigurationDao iLoyaltyCardConfigurationDao;
	
	@Override
	public Response saveLoyaltiCardMaster(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException {
		
		try {
				return iLoyaltiCardMasterDao.saveLoyaltiCardMaster(loyaltiCardMasterDto);

		} catch (Exception e) {
		logger.error("saveLoyaltiCardMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateLoyaltiCardMasterStatus(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException{
		try {
			
				return iLoyaltiCardMasterDao.updateLoyaltiCardMasterStatus(loyaltiCardMasterDto);
		
		} catch (Exception e) {
		logger.error("updateLoyaltiCardMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getLoyaltiCardMasterList(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException{
			try {
				
				return iLoyaltiCardMasterDao.getLoyaltiCardMasterList(loyaltiCardMasterDto);
		
		} catch (Exception e) {
		logger.error("getLoyaltiCardMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLoyaltiCardMasterCount(LoyaltiCardMasterDto loyaltiCardMasterDto) throws ApplicationException{
			try {
				
				return iLoyaltiCardMasterDao.getLoyaltiCardMasterCount(loyaltiCardMasterDto);
		
		} catch (Exception e) {
		logger.error("getLoyaltiCardMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getLoyaltiCardNumber(int orgId, int unitId) throws ApplicationException {
		try {
			boolean flag = true;
			BigInteger obj = null;
			LoyaltyCardConfigurationDto dto = new LoyaltyCardConfigurationDto();
			dto.setUnitId(unitId);
			dto.setOrganisationId(orgId);
			Response res = iLoyaltyCardConfigurationDao.getLoyaltyCardConfigurationCount(dto);
			if(res.getObject()!= null) {
				Long configCount = (Long)res.getObject();
				if(configCount >= 1) {
					
					while (flag) {
						
						 obj  = generateRandom(16);
						Response cardNoRes =  iLoyaltiCardMasterDao.checkLoyaltyCardNumberExistOrNot(orgId,obj.toString());
						if(cardNoRes.getObject()!=null)
						{
							BigInteger count = (BigInteger)cardNoRes.getObject();
							if(count.compareTo(BigInteger.ZERO)==1)
							{
								flag = true;
							}
							else
							{
								flag = false;
							}
						}
					}
						return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
				}else {
						return new Response<>(ERROR, ERROR_CODE, "Please configure loyalty for this unit.", null, null);
				}
				
			}
			
			
	
	} catch (Exception e) {
	logger.error("getLoyaltiCardNumber failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	public static BigInteger generateRandom(int length) {
	     Random random = new Random();
	     char[] digits = new char[length];
	     digits[0] = (char) (random.nextInt(9) + '1');
	     for (int i = 1; i < length; i++) {
	         digits[i] = (char) (random.nextInt(10) + '0');
	     }
	         String stringNo = new String(digits);
	        
	      return new BigInteger(stringNo);
	 }

	@Override
	public Response checkLoyaltycardNumber(int orgId, int unitId, String cardNo) throws ApplicationException {
		try {
			
			LoyaltyCardConfigurationDto dto = new LoyaltyCardConfigurationDto();
			dto.setUnitId(unitId);
			dto.setOrganisationId(orgId);
			Response res = iLoyaltyCardConfigurationDao.getLoyaltyCardConfigurationCount(dto);
			
			if(res.getObject()!= null) {
				
				Long configCount = (Long)res.getObject();
				
				if(configCount >= 1) {
			
						Response cardNoRes =  iLoyaltiCardMasterDao.checkLoyaltyCardNumberExistOrNot(orgId,cardNo);
						if(cardNoRes.getObject()!=null)
						{
							BigInteger count = (BigInteger)cardNoRes.getObject();
							if(count.compareTo(BigInteger.ZERO)==1)
							{
								return new Response<>(ERROR, ERROR_CODE, "Card No is Already Exists.", null, null);
								
							}
							else
							{
								return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
								
							}
						}
				}else {
					
					return new Response<>(ERROR, ERROR_CODE, "Please configure loyalty for this unit.", null, null);
			}
			
		}
	
	} catch (Exception e) {
	logger.error("checkLoyaltycardNumber failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

