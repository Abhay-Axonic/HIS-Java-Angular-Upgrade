package com.param.billing.unit.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.transaction.model.TariffMaster;
import com.param.global.dto.PatientCategoryMasterDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.unit.dao.ITariffMasterDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TariffMasterDto;

import in.param.exception.ApplicationException;

import java.util.List;
import com.param.global.service.VerifiedIdentityService;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TariffMasterServiceImpl implements ITariffMasterService, ICommonConstants {

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	final static Logger logger = Logger.getLogger(TariffMasterServiceImpl.class);

	@Autowired
	ITariffMasterDao iTariffMasterDao;

	private Cache<Integer, List<TariffMasterDto>> tarriffMasterCache;

	@PostConstruct
	public void initCache() {
		tarriffMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	
	@Transactional
	@Override
	public Response saveTariffMaster(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			
			Response response = iTariffMasterDao.getTariffListByName(tariffMasterDto);
			
			if (response.getListObject() != null && response.getListObject().size() > 0)
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			else
			{
				Response res= iTariffMasterDao.saveTariffMaster(tariffMasterDto);
				if(res.getStatus().equals(SUCCESS))
				{
					tarriffMasterCache.invalidate(tariffMasterDto.getUnitId());
					iTariffMasterDao.saveTariffPaymentEntitlementMapper((TariffMasterDto)res.getObject());
					iTariffMasterDao.saveVisitTypeMapper((TariffMasterDto)res.getObject());
					return iTariffMasterDao.saveTariffPatientTypeMapper((TariffMasterDto)res.getObject());
					//return iTariffMasterDao.saveTariffBillingBedCategoryMapper((TariffMasterDto)res.getObject());					
					
				}
				
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getTariffMasterList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iTariffMasterDao.getTariffMasterList(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentEntitlementListByTariffid(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iTariffMasterDao.getPaymentEntitlementListByTariffid(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTariffListById(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iTariffMasterDao.getTariffListById(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateTariffMaster(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			Response response = iTariffMasterDao.getTariffListByNameNotId(tariffMasterDto);
			if (response.getObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Response updateRes = iTariffMasterDao.updateTariffMaster(tariffMasterDto);
				if(updateRes.getStatus().equals(SUCCESS))
				{
					if(updateRes.getObject()!=null) {
						TariffMaster master = (TariffMaster) updateRes.getObject();
						tarriffMasterCache.invalidate(master.getUnitId());
					}

					iTariffMasterDao.tariffPaymentEntitlementMapper(tariffMasterDto);
					iTariffMasterDao.tariffVisitTypeMapper(tariffMasterDto);
					iTariffMasterDao.tariffTariffPatientTypeMapper(tariffMasterDto);
					
					/*if(tariffMasterDto.getApplicablePaymentEntitlementIdListOld().contains(tariffMasterDto.getApplicablePaymentEntitlementIdList())==true){
						return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
					}
					else
					{
						Response res2= iTariffMasterDao.inactiveOldPaymentEntitlements(tariffMasterDto);
						if(res2.getStatus().equals(SUCCESS)&&tariffMasterDto.getApplicablePaymentEntitlementIdList().size()>0)
						{
							iTariffMasterDao.saveTariffPaymentEntitlementMapper(tariffMasterDto);
						}
						return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
					}*/
					
					iTariffMasterDao.saveTariffPaymentEntitlementMapper(tariffMasterDto);
					iTariffMasterDao.saveVisitTypeMapper(tariffMasterDto);
					return iTariffMasterDao.saveTariffPatientTypeMapper(tariffMasterDto);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForTariff(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
				Response res = iTariffMasterDao.updateStatusForTariff(tariffMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					tarriffMasterCache.invalidate(tariffMasterDto.getUnitId() != null ? tariffMasterDto.getUnitId() : filterId);
				}
                return res;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariffPaymentMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.updateStatusForTariffPaymentMapper(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getActiveTariffMasterList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			List<TariffMasterDto> responseCache = tarriffMasterCache.getIfPresent(tariffMasterDto.getUnitId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iTariffMasterDao.getActiveTariffMasterList(tariffMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				tarriffMasterCache.put(tariffMasterDto.getUnitId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentEntitlementListByTariffIdList(TariffMasterDto tariffMasterDto)
			throws ApplicationException {
		try {
			return iTariffMasterDao.getPaymentEntitlementListByTariffIdList(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getVisitTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.getVisitTypeListByTariffIdList(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.getPatientTypeListByTariffIdList(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.getBillingBedCategoryByTariffIdList(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getVisitTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.getVisitTypeListByTariffId(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariffVisitTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.updateStatusForTariffVisitTypeMapper(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.getPatientTypeListByTariffId(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariffPatientTypeMapper(TariffMasterDto tariffMasterDto)
			throws ApplicationException {
		try {
			return iTariffMasterDao.updateStatusForTariffPatientTypeMapper(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException {
		try {
			return iTariffMasterDao.getBillingBedCategoryListByTariffId(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatusForTariffBillingBedCategoryMapper(TariffMasterDto tariffMasterDto)
			throws ApplicationException {
		try {
			return iTariffMasterDao.updateStatusForTariffBillingBedCategoryMapper(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response tariffMasterCount(TariffMasterDto tariffMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iTariffMasterDao.tariffMasterCount(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getTariffMasterListOpdBilling(TariffMasterDto tariffMasterDto) {
		// BATCH-5 org-trust fix: bind verified tenant
		tariffMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		tariffMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iTariffMasterDao.getTariffMasterListOpdBilling(tariffMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
