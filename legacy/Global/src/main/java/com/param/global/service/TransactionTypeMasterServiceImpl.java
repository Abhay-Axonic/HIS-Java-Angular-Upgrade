package com.param.global.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.TransactionTypeMaster;
import com.param.global.dto.CommonDto;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.ITransactionTypeMasterDao;
import com.param.global.dto.TransactionTypeMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;

import java.util.List;

@Service
@SuppressWarnings("rawtypes")
public class TransactionTypeMasterServiceImpl implements ITransactionTypeMasterService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(TransactionTypeMasterServiceImpl.class);

	@Autowired
	private ITransactionTypeMasterDao iTransactionTypeMasterDao;

	private Cache<Integer, List<CommonDto>> transationTypeMasterCache;

	@PostConstruct
	public void initCache() {
		transationTypeMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	@Transactional
	public Response getTransactionTypes(int orgId, int unitId) throws ApplicationException {
		try{
			List<CommonDto> responseCache = transationTypeMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iTransactionTypeMasterDao.getTransactionTypes(orgId, unitId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				transationTypeMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTransactionTypeMasterList(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {
			return iTransactionTypeMasterDao.getTransactionTypeMasterList(transactionTypeMasterDto);
		} catch (Exception e) {
			logger.error("getTransactionTypeMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iTransactionTypeMasterDao.getTransactionTypeByName(transactionTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				
				Response res = iTransactionTypeMasterDao.saveTransactionTypeMaster(transactionTypeMasterDto);

				if(res.getStatus().equals(SUCCESS)){
					transationTypeMasterCache.invalidate(transactionTypeMasterDto.getOrgId());
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			logger.error("saveTransactionTypeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iTransactionTypeMasterDao.getTransactionTypeByNameNotId(transactionTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*countryMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iTransactionTypeMasterDao.updateTransactionTypeMaster(transactionTypeMasterDto);

				if(res.getStatus().equals(SUCCESS) && res.getObject() != null){
					TransactionTypeMaster master = (TransactionTypeMaster) res.getObject();
					transationTypeMasterCache.invalidate(master.getOrgId());
				}
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			logger.error("updateTransactionTypeMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForTransactionType(TransactionTypeMasterDto transactionTypeMasterDto)
			throws ApplicationException {
			try {
			
				Response res = iTransactionTypeMasterDao.updateStatusForTransactionType(transactionTypeMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					transationTypeMasterCache.invalidate(transactionTypeMasterDto.getOrgId() != null ? transactionTypeMasterDto.getOrgId() : filterId);
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (Exception e) {
			logger.error("updateStatusForTransactionType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
