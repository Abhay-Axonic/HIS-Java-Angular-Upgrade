package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IDocumentTypeMasterDao;
import com.param.adt.master.global.dto.DocumentTypeMasterDto;
import com.param.adt.master.global.model.DocumentTypeMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class DocumentTypeMasterServiceImpl implements IDocumentTypeMasterService, ICommonConstants {

	
	@Autowired 
	IDocumentTypeMasterDao iDocumentTypeMasterDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;

	private Cache<String, List<DocumentTypeMasterDto>> documentTypeMasterCache;

	@PostConstruct
	public void initCache() {
		documentTypeMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}
	
	@Override
	public Response addDocumentTypeMaster(DocumentTypeMasterDto documentTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iDocumentTypeMasterDao.getDocumentTypeByName(documentTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				/*documentTypeMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				documentTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res =iDocumentTypeMasterDao.addDocumentTypeTypeMaster(documentTypeMasterDto);
				
				if(res.getObject()!=null) {
					documentTypeMasterCache.invalidateAll();
					DocumentTypeMaster dto = new DocumentTypeMaster();
					dto = (DocumentTypeMaster) res.getObject();
					documentTypeMasterDto.setDocumentTypeId(dto.getDocumentTypeId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(documentTypeMasterDto);
					request.setKey(MasterKeyConstants.Document_type.toString());
					request.setPayload(payload);
					////EmrService.publish(request);
					//iOtimsService.publish(request);
					
				}
				
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			
		}
			return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDocumentTypeMasterList(DocumentTypeMasterDto documentTypeMasterDto) throws ApplicationException {
		try {
			return iDocumentTypeMasterDao.getDocumentTypeMasterList(documentTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDocumentTypeMaster(DocumentTypeMasterDto documentTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iDocumentTypeMasterDao.getDocumentTypeByNameNotId(documentTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				/*documentTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response res = iDocumentTypeMasterDao.updateDocumentTypeMaster(documentTypeMasterDto);

				if(res.getStatus().equals(SUCCESS)){
					documentTypeMasterCache.invalidateAll();
				}

				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(documentTypeMasterDto);
				request.setKey(MasterKeyConstants.Document_type.toString());
				request.setPayload(payload);
				////EmrService.publish(request);
				//iOtimsService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDocumentTypeById(DocumentTypeMasterDto documentTypeMasterDto) throws ApplicationException {
		try {
			return iDocumentTypeMasterDao.getDocumentTypeById(documentTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForDocumentType(DocumentTypeMasterDto documentTypeMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iDocumentTypeMasterDao.getDocumentTypeById(documentTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				/*documentTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				iDocumentTypeMasterDao.updateHolidayStatus(documentTypeMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveDocumentTypeList() throws ApplicationException {
		try {
			List<DocumentTypeMasterDto> responseCache = documentTypeMasterCache.getIfPresent("DocumentTypeMaster");
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iDocumentTypeMasterDao.getActiveDocumentTypeList();

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				documentTypeMasterCache.put("DocumentTypeMaster", res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDocumentTypeCount(DocumentTypeMasterDto documentTypeMasterDto) throws ApplicationException {
		try {
			return iDocumentTypeMasterDao.getCount(documentTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
