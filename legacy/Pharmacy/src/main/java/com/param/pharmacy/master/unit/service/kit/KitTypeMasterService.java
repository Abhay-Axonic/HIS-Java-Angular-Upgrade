package com.param.pharmacy.master.unit.service.kit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.KitType;
import com.param.pharmacy.master.unit.dao.Kit.KitTypeMasterDao;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("KitTypeMasterService")
public class KitTypeMasterService implements IKitTypeMasterService{
	
	@Autowired
	KitTypeMasterDao  KitTypeMasterDao;
	

	@Autowired
	private SessionService sessionService;
	
	@Override
	@Transactional
	public CommonResponse saveKitMasterType(KitTypeMasterRequest request)
	
  	{	
				
				KitType kitType=new KitType(request.getCode(),request.getDescription(),request.getStatus());
			//	sessionService.setEntityDefaults(kitType);	

				kitType.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				KitType KitTypeResponse=KitTypeMasterDao.kitTypeMasterSave(kitType);
				
				

				CommonResponse response = new CommonResponse();
				response.setId(KitTypeResponse.getId().longValue());
				response.setMessage("Kit Type Master saved successfully");

				return response;
  	}
	
	
	
	@Override
	@Transactional
	public CommonResponse update(KitTypeMasterRequest request) {

		KitType kitType = KitTypeMasterDao.getKitType(request.getId());

		/* kitType.updateKitType(request.getCode(),request.getDescription()); */

		kitType.setUpdatedDate(TimeZoneComponent.getDateByzone(kitType.getUnitId()));
		
		if (request.getCode() != null && request.getCode() != "") {
			kitType.setCode(request.getCode());
		}
		if (request.getDescription() != null && request.getDescription() != "") {
			kitType.setCode(request.getDescription());
		}
		
		
		if(request.getStatus() != null)
		{
		kitType.setIsActive(request.getStatus());
		}

		KitTypeMasterDao.update(kitType);

		CommonResponse response = new CommonResponse();
		response.setMessage("Kit Type updated successfully");

		return response;
	}
	
	 @Override
		@Transactional
		public List<KitTypeMasterResponse> getAllKitType() {
		 
			List<KitTypeMasterResponse> responseList = KitTypeMasterDao.getAllKitType() ;    
	       
	
			
			
			return responseList ;
			}
	 
	 @Override
		@Transactional
		public Object getById(Integer id) {
		 
		 KitType responseList = KitTypeMasterDao.getById(id) ;    
		
		 KitTypeMasterResponse response= new KitTypeMasterResponse(responseList.getId(),responseList.getCode(),responseList.getDescription(),responseList.getIsActive());
			
			
			return response ;
			}
	 
	/*
	 * @Override
	 * 
	 * @Transactional public Object getMaterialInwardList(MaterialInwardRequest
	 * request) { request.setToDate((new
	 * DateTime(request.getToDate())).plusDays(1).toDate()); return
	 * MaterialInwardDao.searchMaterialInwardList(request, screenId); }
	 */
	

}