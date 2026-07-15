package com.param.pharmacy.master.global.service.frequency;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.TranslatedFrequencyMasterDto;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.FrequencyDetail;
import com.param.pharmacy.master.global.dao.frequency.IFrequencyDao;
import com.param.pharmacy.master.global.dto.frequency.FrequencyDetailRabbitDto;
import com.param.pharmacy.master.global.dto.frequency.FrequencyDetailsResponse;
import com.param.pharmacy.master.global.dto.frequency.FrequencyRabbitDto;
import com.param.pharmacy.master.global.dto.frequency.FrequencyRequest;
import com.param.pharmacy.master.global.dto.frequency.FrequencyRequestItem;
import com.param.pharmacy.master.global.dto.frequency.GetFrequencyListResponse;
import com.param.pharmacy.master.global.dto.frequency.GetFrequencyResponse;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("FrequencyService")
public class FrequencyService implements IFrequencyService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(FrequencyService.class);

	@Autowired
	IFrequencyDao FrequencyDao;
	
	@Autowired
	IEmrService EmrService;
	

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional


	public List<GetFrequencyResponse> getAll(Integer orgId) {
		List<GetFrequencyResponse> responseList = new ArrayList<GetFrequencyResponse>();

		for (Frequency frequency : FrequencyDao.getAll(orgId)) {
			GetFrequencyResponse frequencyResponse = new GetFrequencyResponse(frequency.getId(), frequency.getCode(),
					frequency.getDescription(), frequency.getNoOfAdministrationTime(), frequency.getIsActive());
			for (FrequencyDetail frequencyDetail : frequency.getFrequencyDetailList()) {
				if (!frequencyDetail.getIsDeleted()) {
					
					List<GetFrequencyListResponse> getlist = FrequencyDao.getFrequencyDetailsById(frequency.getId());
					List<GetFrequencyListResponse> responseListpara = new ArrayList<GetFrequencyListResponse>();

					for (GetFrequencyListResponse dto1 : getlist) {
						GetFrequencyListResponse dto = new GetFrequencyListResponse();
						dto.setId(dto1.getId());
						dto.setAdministrationTime(dto1.getAdministrationTime());
						dto.setPreBufferAdministrationTime(dto1.getPreBufferAdministrationTime());
						dto.setPostBufferAdministrationTime(dto1.getPostBufferAdministrationTime());
						responseListpara.add(dto);
					}
					frequencyResponse.setItems(responseListpara);
					/*frequencyResponse.addItems(new GetFrequencyListResponse(frequencyDetail.getId(), // frequencyDetail.getSlot(),
							frequencyDetail.getAdministrationTime(), frequencyDetail.getPreBufferAdministrationTime(),
							frequencyDetail.getPostBufferAdministrationTime()));*/
				}
			}
			responseList.add(frequencyResponse);
		}
		return responseList;
	
	}

	@Override
	@Transactional
	public Object updateFrequency(FrequencyRequest request) {

		Frequency frequency = FrequencyDao.getFrequency(request.getId());

		frequency.updateFrequency(request.getCode() != null ? request.getCode() : frequency.getCode(),
				request.getDescription() != null ? request.getDescription() : frequency.getDescription(),
				request.getNoOfAdministrationTime() != null ? request.getNoOfAdministrationTime()
						: frequency.getNoOfAdministrationTime(),
				request.getStatus() != null ? request.getStatus() : frequency.getIsActive());

		if (request.getItems() != null) {
			for (FrequencyDetail detail : frequency.getFrequencyDetailList()) {
				Integer id = null;

				for (FrequencyRequestItem item : request.getItems()) {
					if (item.getId() != null && item.getId().equals(detail.getId())) {
						id = item.getId();
						detail.updateFrequencyDetail(item.getAdministrationTime(),
								item.getPostBufferAdministrationTime(), item.getPreBufferAdministrationTime());
						break;
					}
				}

				if (id == null) {
					detail.setIsDeleted(true);
				}

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(frequency.getUnitId()));
				detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			}

			if (request.getItems() != null) {
				for (FrequencyRequestItem item : request.getItems()) {
					if (item.getId() == null)
					{
						FrequencyDetail frequencyDetail=new FrequencyDetail(item.getPostBufferAdministrationTime(),
								item.getAdministrationTime(), item.getPreBufferAdministrationTime());
						//sessionService.setEntityDefaults(frequencyDetail);
						frequencyDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
						frequency.addFrequencyDetailList(frequencyDetail);
					}
				}
			}
		}
		frequency.setUpdatedDate(TimeZoneComponent.getDateByzone(frequency.getUnitId()));
		frequency.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		Frequency frequencyObj = FrequencyDao.update(frequency);
		TranslatedFrequencyMasterDto dto=new TranslatedFrequencyMasterDto();
		if(request.getAddedLanguageList()!=null && !request.getAddedLanguageList().isEmpty()) {
			Response resDto = FrequencyDao.inactiveTranslatedFrequency(request);

			for (TranslatedFrequencyMasterDto tranDto : request.getAddedLanguageList()) {
				dto.setConvertedText(tranDto.getConvertedText());
				dto.setDesc(tranDto.getDesc());
				dto.setFrequencyId(frequencyObj.getId());
				dto.setLanguageId(tranDto.getLanguageId());
				dto.setCreatedBy(frequencyObj.getUpdatedBy().getUserId());
				dto.setCreatedDate(frequencyObj.getUpdatedDate().toString());
				dto.setUpdatedBy(frequencyObj.getUpdatedBy().getUserId());
				dto.setUpdatedDate(frequencyObj.getUpdatedDate().toString());
				dto.setOrganizationId(frequencyObj.getOrganizationId());
				dto.setUnitId(frequencyObj.getUnitId());
				dto.setStatus('A');
				FrequencyDao.updateTranslatedFrequency(dto);

			}

		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Frequency updated successfully");
		response.setId(frequencyObj.getId().longValue());
		/*FrequencyRabbitDto frequencyRabbitDto = new FrequencyRabbitDto(frequencyObj.getId(),frequencyObj.getCode(),frequencyObj.getDescription(),
				frequencyObj.getNoOfAdministrationTime(),frequencyObj.getOrganization().getOrganizationId(),
				frequencyObj.getUnit().getUnitId(),frequencyObj.getAddedBy().getUserId(),frequencyObj.getAddedDate(),
				frequencyObj.getUpdatedBy().getUserId(),frequencyObj.getUpdatedDate(),
				frequencyObj.getIsActive(),frequencyObj.getIsDeleted());

		for (FrequencyDetail detail : frequencyObj.getFrequencyDetailList()) {

			frequencyRabbitDto.addFrequencyDetails(new FrequencyDetailRabbitDto(detail.getId(), detail.getAdministrationTime(),
					detail.getPostBufferAdministrationTime(),detail.getPreBufferAdministrationTime(),frequency.getId(),
					detail.getOrganization().getOrganizationId(),detail.getUnit().getUnitId(),detail.getAddedBy().getUserId(),
					detail.getAddedDate(),detail.getUpdatedBy().getUserId(),detail.getUpdatedDate(),detail.getIsActive(),
					detail.getIsDeleted()));
		}
		Request req = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(frequencyRabbitDto);
		req.setKey(MasterKeyConstants.FrequencyMasterHISKey.toString());
		req.setPayload(payload);*/
		//EmrService.publish(req);
		return response;
	}

	@Override
	@Transactional
	public CommonResponse saveFrequency(FrequencyRequest request) {

		Frequency frequency = new Frequency(request.getCode(), request.getDescription(),
				request.getNoOfAdministrationTime());
	//	sessionService.setEntityDefaults(frequency);
		frequency.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		
		for (FrequencyRequestItem item : request.getItems()) {
			FrequencyDetail frequencyDetail=new FrequencyDetail(item.getAdministrationTime(),
					item.getPostBufferAdministrationTime(), item.getPreBufferAdministrationTime());
			//sessionService.setEntityDefaults(frequencyDetail);
			frequencyDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			
			frequency.addFrequencyDetailList(frequencyDetail);
		}
		Frequency frequencyresponse = FrequencyDao.saveFrequency(frequency);

		CommonResponse response = new CommonResponse();
		response.setId(frequencyresponse.getId().longValue());
		response.setMessage("Frequency saved successfully");

		TranslatedFrequencyMasterDto dto=new TranslatedFrequencyMasterDto();
		if(request.getAddedLanguageList()!=null && !request.getAddedLanguageList().isEmpty()) {
//			Response resDto = FrequencyDao.inactiveTranslatedFrequency(request);

			for (TranslatedFrequencyMasterDto tranDto : request.getAddedLanguageList()) {
				dto.setConvertedText(tranDto.getConvertedText());
				dto.setDesc(tranDto.getDesc());
				dto.setFrequencyId(frequencyresponse.getId());
				dto.setLanguageId(tranDto.getLanguageId());
				dto.setCreatedBy(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
				dto.setCreatedDate(frequencyresponse.getUpdatedDate().toString());
				dto.setUpdatedBy(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
				dto.setUpdatedDate(frequencyresponse.getUpdatedDate().toString());
				dto.setOrganizationId(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId());
				dto.setUnitId(request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId());
				dto.setStatus('A');
				FrequencyDao.saveTranslatedFrequency(dto);

			}
		}

		FrequencyRabbitDto frequencyRabbitDto = new FrequencyRabbitDto(frequencyresponse.getId(),frequencyresponse.getCode(),frequencyresponse.getDescription(),
				frequencyresponse.getNoOfAdministrationTime(),request.getOrganizationId(),
				request.getUnitId(),request.getUserId(),frequencyresponse.getAddedDate(),
				request.getUserId(),frequencyresponse.getUpdatedDate(),
				frequencyresponse.getIsActive(),frequencyresponse.getIsDeleted());
		if (frequencyresponse != null) {
			
			for (FrequencyDetail detail : frequencyresponse.getFrequencyDetailList()) {

				frequencyRabbitDto.addFrequencyDetails(new FrequencyDetailRabbitDto(detail.getId(), detail.getAdministrationTime(),
						detail.getPostBufferAdministrationTime(),detail.getPreBufferAdministrationTime(),frequencyresponse.getId(),
						request.getOrganizationId(),request.getUnitId(),request.getUserId(),
						detail.getAddedDate(),request.getUserId(),detail.getUpdatedDate(),detail.getIsActive(),
						detail.getIsDeleted()));
			}
		}
		Request req = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(frequencyRabbitDto);
		req.setKey(MasterKeyConstants.FrequencyMasterHISKey.toString());
		req.setPayload(payload);
		//EmrService.publish(req);

		return response;
	}

	@Override
	@Transactional
	public FrequencyDetailsResponse getById(Integer id) {
		FrequencyDetailsResponse responseList = new FrequencyDetailsResponse();

		Frequency frequency = FrequencyDao.getById(id);

		if (frequency != null) {

			responseList = new FrequencyDetailsResponse(frequency.getId(), frequency.getCode(),
					frequency.getDescription(), frequency.getNoOfAdministrationTime(), frequency.getIsActive());

			/*for (FrequencyDetail detail : frequency.getFrequencyDetailList()) {
				if (!detail.getIsDeleted()) {
					responseList.addItems(new FrequencyRequestItem(detail.getId(), detail.getAdministrationTime(),
							detail.getPreBufferAdministrationTime(), detail.getPostBufferAdministrationTime()));
				}
			}
			responseList.add(responseList);*/
			
			
			
			for (FrequencyDetail frequencyDetail : frequency.getFrequencyDetailList()) {
				if (!frequencyDetail.getIsDeleted()) {
					
					List<FrequencyRequestItem> getlist = FrequencyDao.getFrequencyDetailsByIdForUpdate(frequency.getId());
					List<FrequencyRequestItem> responseListpara = new ArrayList<FrequencyRequestItem>();
					for (FrequencyRequestItem dto1 : getlist) {
						FrequencyRequestItem dto = new FrequencyRequestItem();
						dto.setId(dto1.getId());
						dto.setAdministrationTimeString(dto1.getAdministrationTimeString());
						dto.setPreBufferAdministrationTimeString(dto1.getPreBufferAdministrationTimeString());
						dto.setPostBufferAdministrationTimeString(dto1.getPostBufferAdministrationTimeString());
						responseListpara.add(dto);
					}
					responseList.setItems(responseListpara);
					
				}
			}
			List <TranslatedFrequencyMasterDto> translatedFrequencyMasterDtoList = FrequencyDao.getTranslatedFrequencyListForUpdate(frequency);
			responseList.setAddedLanguageList(translatedFrequencyMasterDtoList);
			responseList.add(responseList);
		} else {
		}
		return responseList;
	}

	@Override
	public Response getConvertedLanguageListById(FrequencyRequest request)
			throws ApplicationException {
		try {
			return FrequencyDao.getConvertedLanguageListById(request);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * @Override
	 * 
	 * @Transactional public List<FrequencyResponseList> getDropdownList() {
	 * List<FrequencyResponseList> response = new
	 * ArrayList<FrequencyResponseList>();
	 * 
	 * for (Frequency frequency : FrequencyDao.getFrequencyList()) {
	 * response.add(new
	 * FrequencyResponseList(frequency.getNoOfAdministrationTime()); } return
	 * response; }
	 */

}
