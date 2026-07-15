package com.param.pharmacy.master.global.service.consumableBarcodeConfig;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.ConsumableBarcodeConfig;
import com.param.pharmacy.master.global.dao.consumableBarcodeConfig.IConsumableBarcodeConfigDao;
import com.param.pharmacy.master.global.dto.consumableBarcodeConfig.ConsumableBarcodeConfigRequest;
import com.param.pharmacy.master.global.dto.consumableBarcodeConfig.ConsumableBarcodeConfigResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ConsumableBarcodeConfigService")
public class ConsumableBarcodeConfigService implements IConsumableBarcodeConfigService {

	@Autowired
	IConsumableBarcodeConfigDao ConsumableBarcodeConfigDao;
	
	@Autowired
	private SessionService sessionService;


	 @Override
		@Transactional
		public List<ConsumableBarcodeConfigResponse> getAll(Integer unitId) {
			List<ConsumableBarcodeConfigResponse> responseList = new ArrayList<ConsumableBarcodeConfigResponse>();      
	       
			for (ConsumableBarcodeConfig consumable : ConsumableBarcodeConfigDao.getAll(unitId)) {
				ConsumableBarcodeConfigResponse consumableBarcodeConfigResponse = new ConsumableBarcodeConfigResponse(consumable.getId(), 
						consumable.getIsActive(), consumable.getItemName(),consumable.getItemCode(), consumable.getBatchNo(),consumable.getExpiryDate(),
						consumable.getLotNo(),consumable.getMrp(),consumable.getSalePrice(),consumable.getGrnNo()
						,consumable.getVendorName(), consumable.getVendorCode(),consumable.getHospitalName());
				 
						 		
				responseList.add(consumableBarcodeConfigResponse);
			}
			
			return responseList;
			}
	
	@Override
	@Transactional
	public CommonResponse update(ConsumableBarcodeConfigRequest request) {

		ConsumableBarcodeConfig consumableBarcodeConfig = ConsumableBarcodeConfigDao.getConsumableBarcodeConfig(request.getId());

		/*consumableBarcodeConfig.updateConsumableBarcodeConfig(request.getBatchNo(),request.getExpiryDate(),request.getGrnNo(),
				request.getHospitalName(),request.getItemCode(),request.getItemName(),request.getLotNo(),request.getMrp(),request.getSalePrice(),
				request.getVendorCode(),request.getVendorName());
		*/
		consumableBarcodeConfig.setBatchNo(request.getBatchNo());
		consumableBarcodeConfig.setExpiryDate(request.getExpiryDate());
		consumableBarcodeConfig.setGrnNo(request.getGrnNo());
		consumableBarcodeConfig.setHospitalName(request.getHospitalName());
		
		consumableBarcodeConfig.setItemCode(request.getItemCode());
		consumableBarcodeConfig.setItemName(request.getItemName());
		consumableBarcodeConfig.setLotNo(request.getLotNo());
		consumableBarcodeConfig.setMrp(request.getMrp());
		consumableBarcodeConfig.setSalePrice(request.getSalePrice());
		consumableBarcodeConfig.setVendorCode(request.getVendorCode());
		consumableBarcodeConfig.setVendorName(request.getVendorName());

		consumableBarcodeConfig.setUpdatedDate(TimeZoneComponent.getDateByzone(consumableBarcodeConfig.getUnitId()));
		consumableBarcodeConfig.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		ConsumableBarcodeConfigDao.update(consumableBarcodeConfig);

		CommonResponse response = new CommonResponse();
		response.setMessage("ConsumableBarcodeConfig updated successfully");

		return response;
	}
	
	@Override
	@Transactional
	public CommonResponse saveConsumableBarcodeConfig(ConsumableBarcodeConfigRequest request) {
		
		ConsumableBarcodeConfig consumableBarcodeConfig = new ConsumableBarcodeConfig(request.getItemName(),request.getItemCode(),request.getBatchNo(),
				request.getExpiryDate(),request.getLotNo(),request.getMrp(), request.getSalePrice(),request.getGrnNo(),request.getVendorName(),
				request.getVendorCode(),request.getHospitalName());
		consumableBarcodeConfig.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		ConsumableBarcodeConfig equipmentresponse = ConsumableBarcodeConfigDao.saveConsumableBarcodeConfig(consumableBarcodeConfig);
		CommonResponse response = new CommonResponse();
		response.setId(equipmentresponse.getId().longValue());
		response.setMessage("ConsumableBarcodeConfig saved successfully");

		return response;
	}

}
