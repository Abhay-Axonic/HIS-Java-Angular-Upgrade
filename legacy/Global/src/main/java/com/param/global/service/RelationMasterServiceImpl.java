package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.GenderMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.global.utility.UserComponent;
import in.param.exception.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IRelationMasterDao;
import com.param.global.dto.RelationMasterDto;
import com.param.global.model.AreaMaster;
import com.param.global.model.RelationMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class RelationMasterServiceImpl implements IRelationMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(RelationMasterServiceImpl.class);

	@Autowired
	private IRelationMasterDao iRelationDao;

	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<RelationMasterDto>> relationCache;

	@PostConstruct
	public void initCache() {
		relationCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response getRelationMasterList(RelationMasterDto relationMasterDto) throws ApplicationException {

		try {
			return iRelationDao.getRelationMasterList(relationMasterDto);
		} catch (Exception e) {
		logger.error("getRelationMasterList failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addRelationMaster(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iRelationDao.getCountryByName(relationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
				
			} else {
				/*relationMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				relationMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iRelationDao.addCountryMaster(relationMasterDto);
				
				if(res.getObject() != null) {
					RelationMaster dto = new RelationMaster();
					dto = (RelationMaster) res.getObject();
					relationMasterDto.setId(dto.getId());
					Integer orgId = dto.getOrganizationId();
					relationCache.invalidate(orgId);

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(relationMasterDto);
					request.setKey(MasterKeyConstants.Relation.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("addRelationMaster failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRelationById(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			return iRelationDao.getRelationById(relationMasterDto);
		} catch (Exception e) {
		logger.error("getRelationById failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRelationMaster(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iRelationDao.getRelationByNameNotId(relationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*relationMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response updateRes =  iRelationDao.updateRelationMaster(relationMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					RelationMaster master = (RelationMaster) updateRes.getObject();
					Integer orgId = master.getOrganizationId();
					relationCache.invalidate(orgId);
				}

				relationMasterDto.setCreatedBy(relationMasterDto.getUpdatedBy());
				relationMasterDto.setCreatedDate(relationMasterDto.getUpdatedDate());


				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(relationMasterDto);
				request.setKey(MasterKeyConstants.Relation.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateRelationMaster failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForRelation(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iRelationDao.getRelationById(relationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*relationMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iRelationDao.updateRelationStatus(relationMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					relationCache.invalidate(relationMasterDto.getOrganizationId() != null ? relationMasterDto.getOrganizationId() : filterId);
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			}
		} catch (Exception e) {
		logger.error("updateStatusForRelation failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveRelationList(Integer orgId) throws ApplicationException {
		try {
			List<RelationMasterDto> responseCache = relationCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, null, responseCache, null);
			}

			Response res =  iRelationDao.getActiveRelationList(orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				relationCache.put(orgId, res.getListObject());
				return res;
			}
			return res;
		} catch (Exception e) {
		logger.error("getActiveRelationList failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRelationCount(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			return iRelationDao.getCount(relationMasterDto);
		} catch (Exception e) {
		logger.error("getRelationCount failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveRelationListForWebPage(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			return iRelationDao.getActiveRelationListForWebPage(relationMasterDto);
		} catch (Exception e) {
		logger.error("getActiveRelationListForWebPage failed", e);
		}
	return new Response(ERROR, null, null, null, null);
	}

}
