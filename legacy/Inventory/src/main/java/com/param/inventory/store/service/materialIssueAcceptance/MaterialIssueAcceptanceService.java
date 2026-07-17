package com.param.inventory.store.service.materialIssueAcceptance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.RejectDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.materialIssue.IMaterialIssuePendingDao;
import com.param.inventory.store.dao.materialIssueAcceptance.IMaterialIssueAcceptanceDao;
import com.param.inventory.store.dao.materialPicker.MaterialPickerDao;
import com.param.inventory.store.dao.storeIndent.IStoreIndentDao;
import com.param.inventory.store.dto.materialIssueAcceptance.ItemRequest;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueRequest;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueResponse;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueItemResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class MaterialIssueAcceptanceService implements IMaterialIssueAcceptanceService {

	private Integer screenId = Screen.MaterialIssueAcceptance.getScreen();
	
	private Integer rejectScreenId = Screen.MaterialRejectAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IMaterialIssueAcceptanceDao MaterialIssueAcceptanceDao;

	@Autowired
	IMaterialIssuePendingDao MaterialIssuePendingDao;

	@Autowired
	IStoreIndentDao StoreIndentDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired MaterialPickerDao materialPickerDao;

	@Override
	@Transactional
	public StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {

		StoreIndentDropdownResponse response = new StoreIndentDropdownResponse();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		return response;

	}

	@Override
	@Transactional
	public Object getMaterialIssueAcceptanceList(MaterialPickerRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<MaterialIssueResponse> materialIssueResponseList = new ArrayList<MaterialIssueResponse>();
		for(MaterialIssue materialIssue: MaterialIssueAcceptanceDao.searchAllMaterialIssueAcceptanceList(request, screenId) )
		{
			MaterialIssueResponse materialIssueResponse= new MaterialIssueResponse();
			materialIssueResponse.setFromStoreId(materialIssue.getFromStore().getStoreId());
			materialIssueResponse.setFromStore(materialIssue.getFromStore().getStoreName());
			materialIssueResponse.setToStoreId(materialIssue.getToStore().getStoreId());
			materialIssueResponse.setToStore(materialIssue.getToStore().getStoreName());
			materialIssueResponse.setId(materialIssue.getId());
			materialIssueResponse.setIsConsignment(materialIssue.getIsConsignment());
			materialIssueResponse.setIssueDate(materialIssue.getAddedDate());
			materialIssueResponse.setRemark(materialIssue.getRemark());
			materialIssueResponse.setStatus(materialIssue.getStatus().getStatus());
			materialIssueResponse.setIssueNo(materialIssue.getIssueNo());
			materialIssueResponse.setStatusNew(materialIssue.getStatusNew().getStatus());
			
			if(materialIssue.getMaterialPicker() != null && materialIssue.getMaterialPicker().getStoreIndent() != null )
			{
				materialIssueResponse.setIndentNo(materialIssue.getStoreIndent().getIndentNo());
				if(materialIssue.getMaterialPicker().getStoreIndent().getPriority() != null)
				{
					materialIssueResponse.setPriority(materialIssue.getMaterialPicker().getStoreIndent().getPriority().getPriorityDesc());
				}
				if(materialIssue.getMaterialPicker().getStoreIndent().getIndentType() != null)
				{
					materialIssueResponse.setIndentType(materialIssue.getMaterialPicker().getStoreIndent().getIndentType().getType());
				}

			}
			/* start code for resolved HIS-10116 Priority and Type Not Getting Display in Material Issue Acceptance */
			else if(materialIssue.getStoreIndent() != null) {
				materialIssueResponse.setPriority(materialIssue.getStoreIndent().getPriority().getPriorityDesc());
				materialIssueResponse.setIndentType(materialIssue.getStoreIndent().getIndentType().getType());
			}
			else if(materialIssue.getStoreIndent() == null) {
				materialIssueResponse.setIndentType("Direct");
			}
		
			/* end code for resolved HIS-10116 Priority and Type Not Getting Display in Material Issue Acceptance */
			
			materialIssueResponseList.add(materialIssueResponse);
			
		}
		return materialIssueResponseList;
	}

	@Override
	@Transactional
	public Object getMaterialIssueAcceptanceDetails(Integer id,Integer orgId, Integer unitId) {

		MaterialIssue materialIssue = MaterialIssuePendingDao.getMaterialIssue(id);

		MaterialIssueResponse response = new MaterialIssueResponse();
		response.setId(materialIssue.getId());
		response.setIssueDate(materialIssue.getAddedDate());
		response.setIssueNo(materialIssue.getIssueNo());
		if(materialIssue.getMaterialPicker() != null && materialIssue.getMaterialPicker().getStoreIndent() != null )
		{
			response.setIndentNo(materialIssue.getStoreIndent().getIndentNo());
			if(materialIssue.getMaterialPicker().getStoreIndent().getPriority() != null)
			{
				response.setPriority(materialIssue.getMaterialPicker().getStoreIndent().getPriority().getPriorityDesc());
			}
			if(materialIssue.getMaterialPicker().getStoreIndent().getIndentType() != null)
			{
				response.setIndentType(materialIssue.getMaterialPicker().getStoreIndent().getIndentType().getType());
			}

		}
		response.setFromStoreId(materialIssue.getFromStore().getStoreId());
		response.setFromStore(materialIssue.getFromStore().getStoreName());
		response.setToStoreId(materialIssue.getToStore().getStoreId());
		response.setToStore(materialIssue.getToStore().getStoreName());
		response.setId(materialIssue.getId());
		response.setIsConsignment(materialIssue.getIsConsignment());
		response.setRemark(materialIssue.getRemark());
		response.setStatus(materialIssue.getStatus().getStatus());

		int index=0;
		for (MaterialIssueDetail materialIssueDetail : MaterialIssuePendingDao.getMaterialIssueDetail(id)) {

			if(materialIssueDetail.getMaterialIssueBatchMappers()!=null && materialIssueDetail.getMaterialIssueBatchMappers().size()>0 )
			{
			
		
			Long storeStock = GlobalDao.getStoreStockByItem(materialIssue.getFromStore().getStoreId(),
					materialIssueDetail.getItem().getId(),unitId);
			Long availableStock = Util.ConvertQuantity(storeStock, 
					materialIssueDetail.getMaterialIssueBatchMappers()!=null && materialIssueDetail.getMaterialIssueBatchMappers().size()>0 ?
					materialIssueDetail.getMaterialIssueBatchMappers().getFirst().getBatch().getBatchUomList():null, materialIssueDetail.getMaterialIssueBatchMappers()!=null && materialIssueDetail.getMaterialIssueBatchMappers().size()>0 ?  materialIssueDetail.getMaterialIssueBatchMappers().getFirst().getUomType():null);

			List<MaterialPickerDetailResponse> materialPickerDetails = MaterialIssuePendingDao.getMaterialIssuePendingDetail(id);
			Integer indentQty = 0;
			if(!materialPickerDetails.isEmpty()){
				indentQty =  materialPickerDetails.get(index++).getIndentQuantity();
			}
			//Integer indentQty= MaterialIssuePendingDao.getMaterialIssuePendingDetail(id).get(index++).getIndentQuantity();

			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(materialIssue.getFromStore().getStoreId(),
					materialIssueDetail.getItem().getId());
//			Long tQuantity = Util.ConvertQuantity(stockInTransit, materialIssueDetail.getMaterialIssueBatchMappers().get(0).getBatch().getBatchUomList(), materialIssueDetail.getMaterialIssueBatchMappers().get(0).getUomType());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(materialIssue.getFromStore().getStoreId(),
					materialIssueDetail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			Long maxStoreQty = Util.ConvertQuantity(maxOrderQuantity,materialIssueDetail.getMaterialIssueBatchMappers().getFirst().getBatch().getBatchUomList(), materialIssueDetail.getMaterialIssueBatchMappers().getFirst().getUomType());
			Integer pendingQunatity = 0;
			if(materialIssue.getStoreIndent() != null )
			{
			  pendingQunatity = indentQty - materialIssueDetail.getIssueQuantity();
			}
			if(pendingQunatity < 0) pendingQunatity = 0;

			for (MaterialIssueBatchMapper materialIssueBatchMapper : materialIssueDetail
					.getMaterialIssueBatchMappers()) {
				if(materialIssueBatchMapper.getIsDeleted().equals(false))
				{
					Batch batchObj = materialIssueBatchMapper.getBatch();
					
					MaterialIssueItemResponse materialPickerDetailResponse = new MaterialIssueItemResponse(
							materialIssueDetail.getId(), materialIssueDetail.getItem().getId(),
							materialIssueDetail.getItem().getCode(), materialIssueDetail.getItem().getName(),
							materialIssueDetail.getItem().getGeneric().getName(),
							materialIssueBatchMapper.getUomType() != null ? materialIssueBatchMapper.getUomType().getId() : null,
							materialIssueBatchMapper.getUomType() != null ? materialIssueBatchMapper.getUomType().getType() : null,
							materialIssueBatchMapper.getUomUnit() != null ? materialIssueBatchMapper.getUomUnit().getId() : null,
							materialIssueBatchMapper.getUomUnit() != null ? materialIssueBatchMapper.getUomUnit().getUnits() : null,indentQty,
							/*materialIssueDetail.getMaterialPickerDetail() != null ?
									materialIssueDetail.getMaterialPickerDetail().getStoreIndentDetail() != null ? 
											materialIssueDetail.getMaterialPickerDetail().getStoreIndentDetail().getIndentQuantity(): null : null,
*/							pendingQunatity, materialIssueBatchMapper.getIssueQuantity(), availableStock, batchObj.getId(),
							batchObj.getBatchNo(), batchObj.getExpiryDate(), maxStoreQty,
							materialIssueBatchMapper.getId(), materialIssueBatchMapper.getAcceptedQuantity(),
							materialIssueBatchMapper.getRejectQuantity(),materialIssueBatchMapper.getStatus().getId(), materialIssueDetail.getItem().getIsThreeUOMApplicable(),
							GlobalDao.getUnitOfMeasurementListByItem(materialIssueDetail.getItem().getId(), orgId));
					
					materialPickerDetailResponse.setIsConsignment(batchObj.getIsConsignment());
					response.addItems(materialPickerDetailResponse);
				}
			}
		}
	}
		return response;
	}

	@Override
	@Transactional
	public Object saveMaterialIssueAcceptance(MaterialIssueRequest request) {

		screen = GlobalDao.getScreen(screenId);

		MaterialIssue materialIssue = MaterialIssuePendingDao.getMaterialIssue(request.getId());
		if(materialIssue.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus()))         {
            
            throw new RuntimeException("Already in process");
        }
		int counter = 0;
		for (MaterialIssueDetail detail : MaterialIssuePendingDao.getMaterialIssueDetail(request.getId())) {
			
			for (MaterialIssueBatchMapper batchMapper : MaterialIssuePendingDao.getMaterialIssueBatchList(detail.getId())) {

				
				for (ItemRequest batches : request.getItems()) {

					if (batches.getBatchMapperId() != null && batches.getBatchMapperId().equals(batchMapper.getId())) {

						batchMapper.updateMaterialIssueBatchMapper(batches.getBatchMapperId(),
								batches.getAcceptedQuantity(), batches.getRejectNote(),
								(batches.getRejectQuantity() != null ? batches.getRejectQuantity() : 0),
								new RejectReason(batches.getRejectReasonId()),new Status(com.param.common.constants.Status.Completed.getStatus()));
						
						
						updateStoreStock(materialIssue, batchMapper.getBatch(),
								batches.getAcceptedQuantity(), batchMapper.getUomType(),batches.getIsConsignment(),request, batchMapper , materialIssue);
						
						if(materialIssue.getStoreIndent() != null)
						{
						StoreIndent storeIndent = materialIssue.getStoreIndent();
					//	storeIndent.setUpdatedBy(new User(request.getUserId()));
						storeIndent.setUpdatedByUser(request.getUserId());
						storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						if(request.getNewStatusId()==18)
						{
							storeIndent.setStatus(new Status(request.getNewStatusId()));
						}
						else
						{
						storeIndent.setStatus(new Status(request.getStatusId()));
						}
						}
						/*
						StoreIndentDetail storeIndentDetail = detail.getStoreIndentDetail();
						storeIndentDetail.setInTransitQuantity(storeIndentDetail.getInTransitQuantity() + batches.getAcceptedQuantity());
						*/
					}
				}
				if(batchMapper.getStatus().getId().equals(com.param.common.constants.Status.Pending.getStatus()))
				{
					 counter ++;
				}
			}
		}
		if(request.getNewStatusId()!=18)
		{
			materialIssue.setStatus(new Status(request.getStatusId()) );
		}
		materialIssue.setStatusNew(new Status(request.getStatusId()));
		if(counter == 0)
		{
//			materialIssue.setStatus(new Status(request.getStatusId()));
		//	materialIssue.setUpdatedBy(materialIssue.getUser());
			materialIssue.setUpdatedByUser(request.getUserId());
			materialIssue.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		}
		/*Integer storeId = request.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String rejectNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), rejectScreenId);

		Boolean flag = false;
		Reject reject = new Reject(new Store(materialIssue.getFromStore().getStoreId()),
				new Store(materialIssue.getToStore().getStoreId()), new User(request.getUserId()), TimeZoneComponent.getDateByzone(request.getUnitId()), rejectNumber,
				new MaterialIssue(materialIssue.getId()), new Status(2));
	//	sessionService.setEntityDefaults(reject);	
		reject.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (ItemRequest item : request.getItems()) {
			if (item.getRejectQuantity() > 0) {
				flag = true;
				
				RejectDetail rejectDetail=new RejectDetail(new RejectReason(item.getRejectReasonId()), item.getRejectNote(),
						item.getRejectQuantity(), new MaterialIssueBatchMapper(item.getBatchMapperId()));
			//	sessionService.setEntityDefaults(rejectDetail);	
				rejectDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				reject.addRejectDetail(rejectDetail);
			}
		}
		if (flag == true) {
			MaterialIssueAcceptanceDao.saveReject(reject);
		}

		CommonResponse response = new CommonResponse();
		response.setId(materialIssue.getId().longValue());
		response.setMessage("Material Issue Acceptance Updated successfully");
		return response;
	}
	private void saveStockTransaction(Boolean flag,Integer leaseQuantity, StoreStock storeStock, MaterialIssueRequest request, Integer integer, String returnBillNumber, MaterialIssueBatchMapper d, Integer leaseStockInTransitRejectedQuantity) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), storeStock.getStore(),
				new Batch(storeStock.getBatch().getId()),integer);
		stockTransaction.setTransitNo(returnBillNumber);
		stockTransaction.setItem(storeStock.getBatch().getItem());
		stockTransaction.setIsTransit(flag);
		Integer indentQty = 0;
		if(leaseStockInTransitRejectedQuantity != null  && leaseStockInTransitRejectedQuantity>0) {
			stockTransaction.setIsTransit(true);
			stockTransaction.setOutQty(leaseStockInTransitRejectedQuantity);
			indentQty = leaseQuantity + leaseStockInTransitRejectedQuantity;
		}else {
			stockTransaction.setIsTransit(false);
			indentQty = leaseQuantity;
		}
		stockTransaction.setInQty(leaseQuantity);
		stockTransaction.setTransactionId(request.getId());
		
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		MaterialIssueAcceptanceDao.updateMaterialIssueStoreTransaction(integer,20);
		MaterialIssueAcceptanceDao.updateMaterialIssueIndendStoreTransaction(d.getMaterialIssueDetail().getId(),113,indentQty,storeStock.getBatch().getId().intValue());
		GlobalDao.saveStockTransaction(stockTransaction);
	}
	private void updateStoreStock(MaterialIssue issue, Batch batch, Integer acceptedQuantity, UomType uomType , Boolean isConsignment,MaterialIssueRequest request, MaterialIssueBatchMapper d, MaterialIssue materialIssue) {

		
		
		 if(materialIssue.getStatus().getId().equals(com.param.common.constants.Status.Closed))
		 {
			 
			 throw new RuntimeException("Already in process");
		 }
		
		
		boolean hasStock = false;
		
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(issue.getToStore().getStoreId(), batch.getId());
		
		Integer avaialableStoreStockOfFromStore = GlobalDao.getStoreStockByBatch(issue.getFromStore().getStoreId(), batch.getId());
        if(avaialableStoreStockOfFromStore == null)
        {
        	avaialableStoreStockOfFromStore=0;
        }
		
		Integer leaseQuantity = Util.GetLeaseQuantity(acceptedQuantity, batch.getBatchUomList(), uomType);
		Integer leaseStockInTransitQuantity = Util.GetLeaseQuantity(d.getIssueQuantity(), batch.getBatchUomList(), uomType);
		Integer leaseStockInTransitRejectedQuantity = Util.GetLeaseQuantity(d.getRejectQuantity(), batch.getBatchUomList(), uomType);
		
		Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(issue.getToStore().getStoreId(), batch.getId());
		
		Integer balanceQuantity = avaialableStoreStockOfFromStore + leaseQuantity;
		
		Integer tQty = (inTransitQuantity!=null ?inTransitQuantity:0 ) - leaseStockInTransitQuantity;
		if(tQty < 0)
		{
			tQty=0;
		}
		
		List<StoreStock> fromStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == issue.getToStore().getStoreId()).collect(Collectors.toList());
		if(fromStoreStock!=null &&  fromStoreStock.size()>0) 
		{
			for(StoreStock s : fromStoreStock)
			{
				if(s.getStore().getStoreId().equals(issue.getToStore().getStoreId()))
				{
					s.settQty(tQty <0 ? 0:tQty);
					//saveStockTransaction(true,0, s, request,batch.getItem().getId(),d.getMaterialIssueDetail().getMaterialIssue().getIssueNo());

				
				}
				break;	
			}
			//fromStoreStock.get(0).setAvailableQty(balanceQuantity);
			//fromStoreStock.get(0).setTotalQty(fromStoreStock.get(0).getTotalQty()+leaseQuantity);

			// fromStoreStock.get(0).setTotalQty(fromStoreStock.get(0).getTotalQty()-leaseQuantity);

		}else {
			/*StoreStock storeStockNew = new StoreStock(leaseQuantity, new Store(issue.getFromStore().getStoreId()), new Batch(batch.getId()));
			storeStockNew.setTotalQty(leaseQuantity);
			storeStockNew.setIsConsignment(isConsignment);
		//	sessionService.setEntityDefaults(storeStockNew);
			storeStockNew.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			MaterialIssueAcceptanceDao.saveStoreStock(storeStockNew);
			StockTransaction ts=new StockTransaction(avaialableStoreStockOfFromStore, screen, screen.getCode(),
					leaseQuantity, batch.getStoreStockList().get(0).getStore());
			ts.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			
			batch.addStockTransaction(ts);*/
		}
		
		List<StoreStock> toStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == issue.getFromStore().getStoreId()).collect(Collectors.toList());
		Boolean stockFlag=true;
		if(toStoreStock!=null &&  toStoreStock.size()>0) {
		//	toStoreStock.get(0).settQty(tQty);

			//start code by minesh for resolved bug no HIS-8018, HIS-8415
			//toStoreStock.get(0).setTotalQty(toStoreStock.get(0).getTotalQty()-leaseQuantity);
			//end code by minesh for resolved bug no HIS-8018, HIS-8415
			for(StoreStock s : toStoreStock)
			{
				if(s.getStore().getStoreId().equals(issue.getFromStore().getStoreId()))
				{
					s.setAvailableQty((s.getAvailableQty()!=null ? s.getAvailableQty() : 0)+leaseQuantity);
					s.setTotalQty(s.getTotalQty() <s.getAvailableQty()?s.getAvailableQty() : s.getTotalQty() );
					s.settQty((s.gettQty()!=null ? s.gettQty() : 0)+leaseStockInTransitRejectedQuantity);
					stockFlag=false;
					saveStockTransaction(true,leaseQuantity, s, request,d.getId(),d.getMaterialIssueDetail().getMaterialIssue().getIssueNo(),d,leaseStockInTransitRejectedQuantity);

				}
				break;	
			}
			 
		}
		if(stockFlag.equals(true)) {
			StoreStock storeStockNew = new StoreStock(leaseQuantity, new Store(issue.getFromStore().getStoreId()), new Batch(batch.getId()));
			storeStockNew.setIsConsignment(isConsignment);
			storeStockNew.setTotalQty(leaseQuantity);
			storeStockNew.settQty(leaseStockInTransitRejectedQuantity);
		//	sessionService.setEntityDefaults(storeStockNew);	
			storeStockNew.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			StoreStock s1=MaterialIssueAcceptanceDao.saveStoreStock(storeStockNew);
			saveStockTransaction(true,leaseQuantity, s1, request,d.getId(),d.getMaterialIssueDetail().getMaterialIssue().getIssueNo(),d,leaseStockInTransitRejectedQuantity);

			
			/*StockTransaction std=new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseQuantity, batch.getStoreStockList().get(0).getStore(),d.getId());
			std.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addStockTransaction(std);*/
		}
		
		
//		for (StoreStock storeStock : batch.getStoreStockList()) {
//			
//			if (storeStock.getStore().getStoreId().equals((issue.getFromStore().getStoreId()))) {
//
//				hasStock = true;
//				
//				storeStock.settQty(tQty);
//				
//				break;
//
//			}
//			
//	    if (storeStock.getStore().getStoreId() != ((issue.getFromStore().getStoreId()))) {
//		    if (storeStock.getStore().getStoreId() == ((issue.getToStore().getStoreId())) &&
//		    	storeStock.getBatch().getId() == (batch.getId())) {
//
//		    	storeStock.setAvailableQty(balanceQuantity);
//		    	
//		    	batch.addStockTransaction(new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
//						leaseQuantity, batch.getStoreStockList().get(0).getStore()));
//		    }
//		    
//		  else if (storeStock.getStore().getStoreId() != ((issue.getToStore().getStoreId())) &&
//			    	storeStock.getBatch().getId() == (batch.getId())) {
//		    	
//			StoreStock storeStockNew = new StoreStock(leaseQuantity, new Store(issue.getToStore().getStoreId()), new Batch(batch.getId()));
//			
//			MaterialIssueAcceptanceDao.saveStoreStock(storeStockNew);
//			
//			batch.addStockTransaction(new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
//					leaseQuantity, batch.getStoreStockList().get(0).getStore()));
//	    	}
//	      }
//	    }
	}
	


}
