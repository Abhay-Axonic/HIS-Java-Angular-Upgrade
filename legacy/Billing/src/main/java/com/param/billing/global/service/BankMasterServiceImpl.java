package com.param.billing.global.service;

import com.param.billing.global.model.BankMaster;
import com.param.global.dto.CommonDto;
import com.param.global.dto.RelationMasterDto;
import com.param.global.model.RelationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.dao.IBankMasterDao;
import com.param.billing.global.dto.BankMasterDto;

import in.param.exception.ApplicationException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.annotation.PostConstruct;
import java.util.List;
import com.param.global.service.VerifiedIdentityService;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class BankMasterServiceImpl implements IBankMasterService, ICommonConstants {

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Autowired
	private IBankMasterDao iBankMasterDao;

	private Cache<Integer, List<BankMasterDto>> bankMasterCache;
	private Cache<Integer, List<CommonDto>> hospitalBankMasterCache;

	@PostConstruct
	public void initCache() {
		bankMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();

		hospitalBankMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	public void invalidateHospitalBankMasterCache(Integer unitId){
		hospitalBankMasterCache.invalidate(unitId);
	}

	@Override
	@Transactional
	public Response getActiveBankMaster(BankMasterDto bankMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		bankMasterDto.setOrgnisationId(verifiedIdentityService.getVerifiedOrganizationId());
		bankMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			List<BankMasterDto> responseCache = bankMasterCache.getIfPresent(bankMasterDto.getOrgnisationId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iBankMasterDao.getActiveBankMaster(bankMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				bankMasterCache.put(bankMasterDto.getOrgnisationId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveBankMaster(BankMasterDto bankMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		bankMasterDto.setOrgnisationId(verifiedIdentityService.getVerifiedOrganizationId());
		bankMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			Response response=iBankMasterDao.getBankMasterByName(bankMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res = iBankMasterDao.saveBankMaster(bankMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					BankMaster dto = new BankMaster();
					dto = (BankMaster) res.getObject();
					Integer orgId = dto.getOrgnisationId();
					bankMasterCache.invalidate(orgId);
				}
				return res;
			}

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterById(int bankId, int orgId) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client org was trusted)
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		try {
			return iBankMasterDao.getBankMasterById(bankId, orgId);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterList(BankMasterDto bankMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		bankMasterDto.setOrgnisationId(verifiedIdentityService.getVerifiedOrganizationId());
		bankMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iBankMasterDao.getBankMasterList(bankMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
    @Transactional
	public Response updateBankMaster(BankMasterDto bankMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		bankMasterDto.setOrgnisationId(verifiedIdentityService.getVerifiedOrganizationId());
		bankMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
//			Response response=iBankMasterDao.getBankMasterByNameNotId(bankMasterDto);
//			if(response.getListObject().size()>0 && response.getListObject()!=null){
//				return new Response(ERROR, null, ALREADY_EXIST, null, null);
//			}else{
				Response updateRes = iBankMasterDao.updateBankMaster(bankMasterDto);
				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					BankMasterDto master = (BankMasterDto) updateRes.getObject();
					Integer orgId = master.getOrgnisationId();
					bankMasterCache.invalidate(orgId);
				}
				return updateRes;
//			}
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBankMasterStatus(BankMasterDto bankMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		bankMasterDto.setOrgnisationId(verifiedIdentityService.getVerifiedOrganizationId());
		bankMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			Response res = iBankMasterDao.updateBankMasterStatus(bankMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
				Integer filterId = (Integer) res.getObject();
				bankMasterCache.invalidate(bankMasterDto.getOrgnisationId() != null ? bankMasterDto.getOrgnisationId() : filterId);
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBankMasterCount(BankMasterDto bankMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		bankMasterDto.setOrgnisationId(verifiedIdentityService.getVerifiedOrganizationId());
		bankMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iBankMasterDao.getBankMasterCount(bankMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getHospitalBank(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<CommonDto> responseCache = hospitalBankMasterCache.getIfPresent(unitId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iBankMasterDao.getHospitalBank(orgId, unitId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				hospitalBankMasterCache.put(unitId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
