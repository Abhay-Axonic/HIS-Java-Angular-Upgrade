package com.param.inventory.store.service.storeIndentApproval;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.MaterialPicker;
import com.param.entity.model.inventory.MaterialPickerDetail;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.materialPicker.MaterialPickerDao;
import com.param.inventory.store.dao.storeIndent.IStoreIndentDao;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentResponse;
import com.param.inventory.store.dto.storeIndentApproval.ItemApprovalRequest;
import com.param.inventory.store.dto.storeIndentApproval.ItemApprovalResponse;
import com.param.inventory.store.dto.storeIndentApproval.StoreIndentApprovalRequest;
import com.param.inventory.store.dto.storeIndentApproval.StoreIndentApprovalResponse;
import com.param.pharmacy.master.global.dao.unitOfMeasurement.UnitOfMeasurementDao;
import com.param.pharmacy.master.unit.dao.applicationConfiguration.IApplicationConfigurationDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class StoreIndentApprovalService implements IStoreIndentApprovalService {

	private Integer screenId = Screen.StoreIndentApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IStoreIndentDao StoreIndentDao;
	
	@Autowired
	UnitOfMeasurementDao unitOfMeasurementDao;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	private MaterialPickerDao materialPickerDao;

	@Autowired
	IGlobalService GlobalService;


	@Autowired
	IApplicationConfigurationDao ApplicationConfigurationDao;

	private Integer issueScreenId = Screen.MaterialIssuePending.getScreen();

	@Autowired
	IItemsDao ItemsDao;

	@Override
	@Transactional
	public StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		StoreIndentDropdownResponse response = new StoreIndentDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		for (Priority priority : GlobalDao.getPriorityList()) {
			response.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getStoreIndentList(StoreIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<StoreIndentResponse> response = new ArrayList<StoreIndentResponse>();

		for (StoreIndent storeIndent : StoreIndentDao.searchStoreIndent(request, screenId)) {
			StoreIndentResponse result = new StoreIndentResponse(storeIndent.getId(), storeIndent.getAddedDate(),
					storeIndent.getIndentNo(), storeIndent.getFromStore().getStoreId(),
					storeIndent.getFromStore().getStoreName(), storeIndent.getToStore().getStoreId(),
					storeIndent.getToStore().getStoreName(), storeIndent.getPriority().getPriorityId(),
					storeIndent.getPriority().getPriorityDesc(), storeIndent.getIndentType().getType(),
					storeIndent.getIsConsignment(), storeIndent.getAddedBy().getUserName(), storeIndent.getRemark(),
					storeIndent.getStatus().getStatus(), storeIndent.getStatus().getId(),
					storeIndent.getApprovalStatus().getStatus(), storeIndent.getApprovalStatus().getId());
			result.setForceClose(storeIndent.getForceClose()!= null ? storeIndent.getForceClose()  : null);
			response.add(result);
		}
		return response;
	}

	@Override
	@Transactional
	public Object getStoreIndent(Integer id,Integer unitId, Integer orgId) {
		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(id);

		StoreIndentApprovalResponse response = new StoreIndentApprovalResponse(storeIndent.getId(),
				storeIndent.getAddedDate(), storeIndent.getIndentNo(), storeIndent.getFromStore().getStoreId(),
				storeIndent.getFromStore().getStoreName(), storeIndent.getToStore().getStoreId(),
				storeIndent.getToStore().getStoreName(), storeIndent.getPriority().getPriorityId(),
				storeIndent.getPriority().getPriorityDesc(), storeIndent.getIndentType().getType(),
				storeIndent.getIsConsignment(), storeIndent.getAddedBy().getUserName(), storeIndent.getRemark(),
				storeIndent.getStatus().getStatus(), storeIndent.getStatus().getId(),
				storeIndent.getApprovalStatus().getStatus(), storeIndent.getApprovalStatus().getId());
				response.setForceClose(storeIndent.getForceClose());

		for (StoreIndentDetail detail : StoreIndentDao.getStoreIndentDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long availableStock = Util.ConvertQuantityByUom(storeStock, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId());
			Long tQuantity = Util.ConvertQuantityByUom(stockInTransit, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Long lastMonSale = GlobalDao.getLastMonthSaleNew(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long lastMonthSale = Util.ConvertQuantityByUom(lastMonSale, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Long currentMonSale = GlobalDao.getCurrentMonthSale(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),unitId);
			Long currentMonthSale = Util.ConvertQuantityByUom(currentMonSale, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeIndent.getFromStore().getStoreId(),
					detail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			Long maxStoreQty = Util.ConvertQuantityByUom(maxOrderQuantity, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());

			response.addItems(new ItemApprovalResponse(detail.getId(), detail.getItem().getId(),
					detail.getUomType().getId(), detail.getUomUnit().getId(), detail.getIndentQuantity(),
					detail.getStatus().getId(), detail.getItem().getCode(), detail.getItem().getName(),
					detail.getItem().getGeneric().getName(), detail.getItem().getManufacturer().getName(),
					detail.getUomType().getType(), detail.getUomUnit().getUnits(), availableStock, tQuantity,
					lastMonthSale, currentMonthSale, maxStoreQty, detail.getAuthorizedQuantity(),
					detail.getRejectQuantity(), detail.getRejectReason(), detail.getRejectNote(), maxStoreQty,GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(), orgId), detail.getItem().getIsThreeUOMApplicable()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object approveStoreIndent(StoreIndentApprovalRequest request) throws CustomException {

		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(request.getId());
		String iNumber=null;
		Integer storeId = storeIndent.getFromStore().getStoreId();
		String issueNumber = GlobalService.getBillNoByGeneratedCode(storeId, request.getUnitId(), issueScreenId);
		iNumber=issueNumber;
		boolean isMaterialPickerRequired = ApplicationConfigurationDao.getApplicationConfigurationByUnitId(request.getUnitId()).getIssueMaterialPickerRequired();
		MaterialIssue mIssue = new MaterialIssue();
		MaterialPicker mPicker = new MaterialPicker();
		for (StoreIndentDetail detail : StoreIndentDao.getStoreIndentDetail(request.getId())) {

			for (ItemApprovalRequest item : request.getItems())
			{

		if (item.getId() != null && item.getId().equals(detail.getId()) && detail.getStatus().getId()!=5 && detail.getStatus().getId()!=7)
		{
					
					/*MaterialPickerDetail materialPickerDetail=new MaterialPickerDetail(detail, 0);
					materialPickerDetail.getStoreIndentDetail().setPendingQuantity(item.getApprovedQuantity());
					//System.out.println("Pending Indent Quantity : "+materialPickerDetail.getStoreIndentDetail().getPendingQuantity());
					sessionService.setEntityDefaults(materialPickerDetail);
					materialPicker.addMaterialPickerDetail(materialPickerDetail);*/
					//Long inTransit=Util.ConvertQuantityByUom(Long.parseLong(String.valueOf(item.getApprovedQuantity())),detail.getItem().getUnitOfMeasurementList(),detail.getUomType());
					Integer inTransit = 0;
					Integer dispenseConversion=GlobalDao.getDispenseConversionByItem(detail.getItem().getId(),request.getOrganizationId());
					//System.out.println("Approved Qty : "+item.getApprovedQuantity());
					//System.out.println("UOM List : "+unitOfMeasurementDao.getAllByItemId(item.getItemId()));
					//System.out.println("UOM Type : "+item.getUomTypeId());

					Integer pendingQuantity = Util.GetPendingIndentQuantityLeaseQuantityStoreIndent(item.getApprovedQuantity(),unitOfMeasurementDao.getAllByItemId(item.getItemId(),request.getOrganizationId()),detail.getUomType().getId());
					//Integer pendingQuantity = Util.GetPendingIndentQuantityLeaseQuantityStoreIndent(item.getQuantity(),unitOfMeasurementDao.getAllByItemId(item.getItemId(),request.getOrganizationId()), item.getUomTypeId());
					
					//System.out.println("Pending Qty : "+pendingQuantity);
					//System.out.println("Dispense Conversion : "+dispenseConversion);
					Integer stockConversion=GlobalDao.getStockConversionByItem(detail.getItem().getId(),request.getOrganizationId());
					//System.out.println("Stock Conversion : "+stockConversion);
					Integer purchaseConversion=GlobalDao.getPurchaseConversionByItem(detail.getItem().getId(),request.getOrganizationId());
					//System.out.println("Purchase Conversion : "+purchaseConversion);
			        inTransit=item.getApprovedQuantity()*dispenseConversion*stockConversion*purchaseConversion;
					////System.out.println("Purchase Conversion : "+purchaseConversion);


					//System.out.println("In Transit : "+inTransit);

					////System.out.println("In Transit : "+item.getApprovedQuantity());
					////System.out.println("In Transit After Conversion : "+inTransit);
			    	Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, detail.getItem().getId(), request.getOrganizationId());
			    	Long storeStock = ItemsDao.getStockAvailable(storeId, detail.getItem().getId(), request.getUnitId());
			    	Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, detail.getItem().getId());
			    	Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(storeId, detail.getItem().getId(), request.getUnitId());
					// System.out.println("approvedPRAndPOQuantity:-" + approvedPRAndPOQuantity);

					Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(storeId, detail.getItem().getId(), request.getUnitId());
					
					Long maxOrderQuantity =  storeStock+approvedPRAndPOQuantity+approvedPOQuantity; 
					
					Long storeindentQty = GlobalDao.getStoreIndentQuantityByItem(storeId, detail.getItem().getId(), request.getUnitId());
					maxOrderQuantity=maxOrderQuantity+storeindentQty;
					Long maxOrderQuantity1=maxStoreQuantity-maxOrderQuantity;
					//HIS-13383
					if(item.getApprovedQuantity() > maxOrderQuantity1)
					{
						 throw new CustomException("Max order quantity is not available");	
					}
			    	
					detail.approveStoreIndentDetail(item.getApprovedQuantity(),pendingQuantity, item.getRejectNote(),
							item.getRejectQuantity(), item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
							new Status(item.getStatusId()), TimeZoneComponent.getDateByzone(storeIndent.getUnitId()), new User(request.getUserId()),inTransit);
					detail.setPendingQuantity(pendingQuantity);
					/*if(detail.getIndentQuantity()<item.getApprovedQuantity())
					{
							detail.setIndentQuantity(item.getApprovedQuantity());
					}
					else
						if(detail.getIndentQuantity()>item.getApprovedQuantity())
						{
							detail.setIndentQuantity(detail.getIndentQuantity()-item.getRejectQuantity());
						}
*/

			if (isMaterialPickerRequired)
			{
				//Integer storeId = storeIndent.getFromStore().getStoreId();
			//	String issueNumber = GlobalService.getBillNoByGeneratedCode(storeId, sessionService.getUnitId(), issueScreenId);
			//iNumber=issueNumber;
				/*MaterialPicker materialPicker = new MaterialPicker(new Status(com.param.common.constants.Status.Pending.getStatus()), storeIndent);
				MaterialPickerDetail materialPickerDetail=new MaterialPickerDetail(detail, 0);
				materialPickerDetail.getStoreIndentDetail().setPendingQuantity(item.getApprovedQuantity());
				System.out.println("Pending Indent Quantity : "+materialPickerDetail.getStoreIndentDetail().getPendingQuantity());
				sessionService.setEntityDefaults(materialPickerDetail);
				materialPicker.addMaterialPickerDetail(materialPickerDetail);
				sessionService.setEntityDefaults(materialPicker);
				StoreIndentDao.saveMaterialPicker(materialPicker);*/
				
				//MaterialPicker materialPicker = new MaterialPicker(new Status(com.param.common.constants.Status.Pending.getStatus()), storeIndent);
				MaterialPickerDetail materialPickerDetail=new MaterialPickerDetail(detail, 0);
				materialPickerDetail.getStoreIndentDetail().setPendingQuantity(item.getApprovedQuantity());
			//	sessionService.setEntityDefaults(materialPickerDetail);
				materialPickerDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				mPicker.addMaterialPickerDetail(materialPickerDetail);
				/*sessionService.setEntityDefaults(materialPicker);
				StoreIndentDao.saveMaterialPicker(materialPicker);*/

			}
			else
			{

				MaterialIssueDetail issueDetail = new MaterialIssueDetail(detail.getItem(),
						detail, 0);
				issueDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				
				if(storeIndent.getMaterialIssue() != null && storeIndent.getMaterialIssue().size()>0 && storeIndent.getApprovalStatus().getId()==6) {
					
					mIssue.addMaterialIssueDetail(issueDetail);
					
				}else {
					mIssue.addMaterialIssueDetail(issueDetail);
				}


			}

		}
			}
		}
		
		if (isMaterialPickerRequired)
		{
			mPicker.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
			mPicker.setStoreIndent(storeIndent);		
	//		sessionService.setEntityDefaults(mPicker);
			mPicker.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			StoreIndentDao.saveMaterialPicker(mPicker);

		
		}
		else
		{
			if(storeIndent.getMaterialIssue() != null && storeIndent.getMaterialIssue().size()>0 && storeIndent.getApprovalStatus().getId()==6) {
				mIssue.setIssueNo(iNumber);
				mIssue.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
				mIssue.setStoreIndent(storeIndent);
				mIssue.setFromStore(new Store(storeIndent.getFromStore().getStoreId()));
				mIssue.setToStore(new Store(storeIndent.getToStore().getStoreId()));
				mIssue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				mIssue.setStatusNew(new Status(com.param.common.constants.Status.Pending.getStatus()));
				materialPickerDao.saveMaterialIssue(mIssue);
				
			}else {
				mIssue.setIssueNo(iNumber);
				mIssue.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
				mIssue.setStoreIndent(storeIndent);
				mIssue.setFromStore(new Store(storeIndent.getFromStore().getStoreId()));
				mIssue.setToStore(new Store(storeIndent.getToStore().getStoreId()));
				mIssue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				mIssue.setStatusNew(new Status(com.param.common.constants.Status.Pending.getStatus()));
				materialPickerDao.saveMaterialIssue(mIssue);
			}
			
		}
		
		storeIndent.setStatus(new Status(request.getStatusId()));
		storeIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));

		storeIndent.setApprovedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
	//	storeIndent.setApprovedBy(storeIndent.getUser());

		storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
	//	storeIndent.setUpdatedBy(storeIndent.getUser());
		storeIndent.setUpdatedByUser(request.getUserId());
		storeIndent.setApprovedBy(new User(request.getUserId()));

		CommonResponse response = new CommonResponse();
		response.setMessage("Store indent quantity approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelStoreIndent(StoreIndentApprovalRequest request) {

		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(request.getId());

		for (StoreIndentDetail detail : storeIndent.getStoreIndentDetailList()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
		//	detail.setUpdatedBy(storeIndent.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		storeIndent.setCancelReason(new CancelReason(request.getCancelReasonId()));
		storeIndent.setCancelNote(request.getCancelNote());

		storeIndent.setStatus(new Status(request.getStatusId()));
		storeIndent.setApprovalStatus(new Status(com.param.common.constants.Status.Rejected.getStatus()));

		storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
	//	storeIndent.setUpdatedBy(storeIndent.getUser());
		storeIndent.setUpdatedByUser(request.getUserId());

		StoreIndentDao.updateStoreIndent(storeIndent);
		if (storeIndent.getApprovalStatus().getId()== com.param.common.constants.Status.Rejected.getStatus())
		{
			if (materialPickerDao.getMaterialPickerByIndent(storeIndent.getId())==null)
			{
					//System.out.println("No Material Picker ");
			}
			else
			{
				MaterialPicker materialPicker = materialPickerDao.getMaterialPickerByIndent(storeIndent.getId());
				materialPicker.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
			}

		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Store Indent Approval cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Response StoreIndentStatus(Integer id) {
		Integer issueSaveStatus = StoreIndentDao.StoreIndentStatus(id);
		return new Response<>(null, null, null, null, issueSaveStatus); 
	}

	@Override
	@Transactional
	public Response updateStoreIndentForForceClose(StoreIndentApprovalRequest request) {
		Integer issueSaveStatus = StoreIndentDao.updateStoreIndentForForceClose(request);
		return new Response<>(null, null, null, null, issueSaveStatus); 
	}

	

//	@Override
//	@Transactional
//	public Response updateStoreIndentForForceClose(StoreIndentRequest request) {
//		Integer issueSaveStatus = StoreIndentDao.updateStoreIndentForForceClose(request);
//		return new Response<>(null, null, null, null, issueSaveStatus); 
//	}

	
}
