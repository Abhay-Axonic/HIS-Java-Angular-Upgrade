package com.param.billing.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.CardTypeMaster;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.dao.ICardTypeMasterDao;
import com.param.billing.global.dto.CardTypeMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings("rawtypes")
public class CardTypeMasterServiceImpl implements ICardTypeMasterService, ICommonConstants{

	@Autowired
	private ICardTypeMasterDao iCardTypeMasterDao;

	private Cache<Integer, List<CardTypeMasterDto>> cardTypeMasterCache;

	@PostConstruct
	public void initCache() {
		cardTypeMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	@Transactional
	public Response gerActiveCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			List<CardTypeMasterDto> responseCache = cardTypeMasterCache.getIfPresent(cardTypeMasterDto.getOrgnisationId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iCardTypeMasterDao.gerActiveCardTypeMaster(cardTypeMasterDto);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				cardTypeMasterCache.put(cardTypeMasterDto.getOrgnisationId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response addCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iCardTypeMasterDao.getCardTypeByName(cardTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				
				Response res = iCardTypeMasterDao.addCardTypeMaster(cardTypeMasterDto);

				if(res.getStatus().equals(SUCCESS)){
					cardTypeMasterCache.invalidate(cardTypeMasterDto.getOrgnisationId());
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCardTypeMasterList(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			return iCardTypeMasterDao.getCardTypeMasterList(cardTypeMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForCardType(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			
			Response res =iCardTypeMasterDao.updateCardTypeStatus(cardTypeMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
				Integer filterId = (Integer) res.getObject();
				cardTypeMasterCache.invalidate(cardTypeMasterDto.getOrgnisationId() != null ? cardTypeMasterDto.getOrgnisationId() : filterId);
			}

				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iCardTypeMasterDao.getCardTypeByNameNotId(cardTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*countryMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response res =iCardTypeMasterDao.updateCardTypeMaster(cardTypeMasterDto);

				if(res.getStatus().equals(SUCCESS) && res.getObject() != null){
					CardTypeMaster master = (CardTypeMaster) res.getObject();
					cardTypeMasterCache.invalidate(master.getOrgnisationId());
				}

				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}
	
	
}
