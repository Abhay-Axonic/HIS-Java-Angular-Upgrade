package com.param.adt.master.unit.service;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.global.dto.CompanyMasterDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitTypeMasterDto;
import com.param.adt.master.unit.dao.IBillingBedCategoryMasterDao;
import com.param.adt.master.unit.dto.BillingBedCategoryMasterDto;
import com.param.adt.master.unit.model.BillingBedVisitTypeMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class BillingBedCategoryMasterServiceImpl implements IBillingBedCategoryMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(BillingBedCategoryMasterServiceImpl.class);

	@Autowired
	IBillingBedCategoryMasterDao iBillingBedCategoryMasterDao;


	@Autowired
	private IOtimsService iOtimsService;

	private Cache<Integer, List<BillingBedCategoryMasterDto>> activeBillingBedCategoryMasterCache;

	@PostConstruct
	public void initCache() {
		activeBillingBedCategoryMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}


	@Override
	public Response getActiveBillingBedCategoryList(BillingBedCategoryMasterDto bedCategoryMasterDto)
			throws ApplicationContextException {
		try {
			List<BillingBedCategoryMasterDto> responseCache = activeBillingBedCategoryMasterCache.getIfPresent(bedCategoryMasterDto.getUnitId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iBillingBedCategoryMasterDao.getActiveBillingBedCategoryList(bedCategoryMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				activeBillingBedCategoryMasterCache.put(bedCategoryMasterDto.getUnitId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			logger.error("getActiveBillingBedCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBillingBedCategoryByBedCat(BillingBedCategoryMasterDto bedCategoryMasterDto)
			throws ApplicationException {
		try {
			return iBillingBedCategoryMasterDao.getActiveBillingBedCategoryByBedCat(bedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getActiveBillingBedCategoryByBedCat failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveBillingBedCategoryMaster(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		Integer savCount=0;
		Response response = iBillingBedCategoryMasterDao.getBillingBedCategoryByName(billingBedCategoryMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Response res = iBillingBedCategoryMasterDao.saveBillingBedCategoryMaster(billingBedCategoryMasterDto);

				if(res.getStatus().equals(SUCCESS)){
					activeBillingBedCategoryMasterCache.invalidate(billingBedCategoryMasterDto.getUnitId());
				}

				Integer billingBedCategoryId=(Integer)res.getObject();
				ListIterator<VisitTypeMasterDto> iterator = billingBedCategoryMasterDto.getVisitTypeMasterDtosList().listIterator();
				while(iterator.hasNext())
				{
					VisitTypeMasterDto masterDto = iterator.next();
					BillingBedVisitTypeMapper bedVisitTypeMapper = new BillingBedVisitTypeMapper();
						bedVisitTypeMapper.setBillingBedCategoryId(billingBedCategoryId);
						bedVisitTypeMapper.setVisitTypeId(masterDto.getId());
						bedVisitTypeMapper.setCreatedBy(billingBedCategoryMasterDto.getCreatedBy());
						bedVisitTypeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
						bedVisitTypeMapper.setUpdatedBy(billingBedCategoryMasterDto.getUpdatedBy());
						bedVisitTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
						bedVisitTypeMapper.setStatus('A');
						bedVisitTypeMapper.setOrganizationId(billingBedCategoryMasterDto.getOrganizationId());
						bedVisitTypeMapper.setUnitId(billingBedCategoryMasterDto.getUnitId());
					Integer i = iBillingBedCategoryMasterDao.saveBillingBedVisitTypeMapper(bedVisitTypeMapper);
					if(i>0)
						savCount++;
				}
				if(savCount.equals(billingBedCategoryMasterDto.getVisitTypeMasterDtosList().size()))
				{
					billingBedCategoryMasterDto.setBillingBedCategoryId(billingBedCategoryId);
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(billingBedCategoryMasterDto);
					request.setKey(MasterKeyConstants.Type_Of_Stay.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					
					return new Response (SUCCESS,SUCCESS_CODE,COMMON_SUCCESS,null,null);
				}
			}
		} catch (Exception e) {
			logger.error("saveBillingBedCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBillingBedCategoryById(int billingBedId, int orgId, int unitId) throws ApplicationException {
		try {
			return iBillingBedCategoryMasterDao.getBillingBedCategoryById(billingBedId, orgId, unitId);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryList(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			
			List<BillingBedCategoryMasterDto> finalList = new LinkedList<>();
			Response res = iBillingBedCategoryMasterDao.getBillingBedCategoryList(billingBedCategoryMasterDto);
			ListIterator<BillingBedCategoryMasterDto> iterator = (ListIterator<BillingBedCategoryMasterDto>) res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				BillingBedCategoryMasterDto bedCategoryMasterDto = iterator.next();
				bedCategoryMasterDto.setOrganizationId(billingBedCategoryMasterDto.getOrganizationId());
				bedCategoryMasterDto.setUnitId(billingBedCategoryMasterDto.getUnitId());
				bedCategoryMasterDto.setVisitTypeMasterDtosList(iBillingBedCategoryMasterDao.getBillingBedCategoryVistList(bedCategoryMasterDto));
				
				finalList.add(bedCategoryMasterDto);
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBillingBedCategoryMaster(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		Response response = iBillingBedCategoryMasterDao.getBillingBedCategoryByNameNotById(billingBedCategoryMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {

				Response res = iBillingBedCategoryMasterDao.updateBillingBedCategoryMaster(billingBedCategoryMasterDto);

				if(res.getStatus().equals(SUCCESS) && res.getObject() != null){
					BillingBedCategoryMaster master = (BillingBedCategoryMaster) res.getObject();
					activeBillingBedCategoryMasterCache.invalidate(master.getUnitId());
				}

				if(billingBedCategoryMasterDto.getRemovedVisitTypeList().size()>0)
				{
					ListIterator<VisitTypeMasterDto> iterator = billingBedCategoryMasterDto.getRemovedVisitTypeList().listIterator();
					while(iterator.hasNext())
					{
						VisitTypeMasterDto masterDto = iterator.next();
						BillingBedVisitTypeMapper bedVisitTypeMapper = new BillingBedVisitTypeMapper();
						bedVisitTypeMapper.setVisitTypeId(masterDto.getId());
						bedVisitTypeMapper.setBillingBedCategoryId(billingBedCategoryMasterDto.getBillingBedCategoryId());
						bedVisitTypeMapper.setUpdatedBy(billingBedCategoryMasterDto.getUpdatedBy());
						bedVisitTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
						bedVisitTypeMapper.setStatus('I');
						
						iBillingBedCategoryMasterDao.updatePreviousVisitTypeList(bedVisitTypeMapper);
					}
				}
				if(billingBedCategoryMasterDto.getNewlyAddedVisitTypeList().size()>0)
				{
					ListIterator<VisitTypeMasterDto> iterator = billingBedCategoryMasterDto.getNewlyAddedVisitTypeList().listIterator();
					while(iterator.hasNext())
					{
						VisitTypeMasterDto masterDto = iterator.next();
						BillingBedVisitTypeMapper bedVisitTypeMapper = new BillingBedVisitTypeMapper();
							bedVisitTypeMapper.setBillingBedCategoryId(billingBedCategoryMasterDto.getBillingBedCategoryId());
							bedVisitTypeMapper.setVisitTypeId(masterDto.getId());
							bedVisitTypeMapper.setCreatedBy(billingBedCategoryMasterDto.getUpdatedBy());
							bedVisitTypeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
							bedVisitTypeMapper.setUpdatedBy(billingBedCategoryMasterDto.getUpdatedBy());
							bedVisitTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(billingBedCategoryMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
							bedVisitTypeMapper.setStatus('A');
							bedVisitTypeMapper.setOrganizationId(billingBedCategoryMasterDto.getOrganizationId());
						iBillingBedCategoryMasterDao.saveBillingBedVisitTypeMapper(bedVisitTypeMapper);
					}
					
				}
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(billingBedCategoryMasterDto);
				request.setKey(MasterKeyConstants.Type_Of_Stay.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			logger.error("updateBillingBedCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBillingBedCategoryMasterStatus(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			Response res = iBillingBedCategoryMasterDao.updateBillingBedCategoryMasterStatus(billingBedCategoryMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null){
				Integer filterId = (Integer) res.getObject();
				activeBillingBedCategoryMasterCache.invalidate(billingBedCategoryMasterDto.getUnitId() != null ? billingBedCategoryMasterDto.getUnitId() : filterId);
			}
			return res;
		} catch (Exception e) {
			logger.error("updateBillingBedCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryMasterCount(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {
			return iBillingBedCategoryMasterDao.getBillingBedCategoryMasterCount(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByVistTypeId(int visitTypeId,int orgId,int unitId)
			throws ApplicationException {
		try {
			return iBillingBedCategoryMasterDao.getBillingBedCategoryByVistTypeId(visitTypeId,orgId,unitId);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByVistTypeId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryByVistTypeIdMultiple(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
		throws ApplicationException {
			try {
				return iBillingBedCategoryMasterDao.getBillingBedCategoryByVistTypeIdMultiple(billingBedCategoryMasterDto);
			} catch (Exception e) {
				logger.error("getBillingBedCategoryByVistTypeIdMultiple failed", e);
				throw new ServiceException(e.getMessage());
			}
	 }

	@Override
	public Response getVisitTypeListByBillingBedCategory(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException {
		try {

			List<VisitTypeMasterDto> visitTypeList =  iBillingBedCategoryMasterDao.getBillingBedCategoryVistList(billingBedCategoryMasterDto);

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, visitTypeList, null);
		} catch (Exception e) {
			logger.error("getVisitTypeListByBillingBedCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@Override
	public Response getActiveBillingBedCategoryMasterList(Integer orgId) throws ApplicationException {
		try {
			return iBillingBedCategoryMasterDao.getActiveBillingBedCategoryMasterList(orgId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

}
