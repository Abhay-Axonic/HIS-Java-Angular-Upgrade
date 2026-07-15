package com.param.inventory.store.service.materialIssueList;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.dao.IEmployeeRegistrationDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.materialIssue.IMaterialIssuePendingDao;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueItemResponse;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueListDropdownResponse;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class MaterialIssueListService implements IMaterialIssueListService {

	private Integer screenId = Screen.MaterialIssue.getScreen();

	private Integer issueScreenId = Screen.MaterialIssuePending.getScreen();
	
	@Autowired
	IEmployeeRegistrationDao iEmployeeRegistrationDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IMaterialIssuePendingDao MaterialIssuePendingDao;

	private com.param.entity.model.master.Screen screen;

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
	public MaterialIssueListDropdownResponse getDropdownsMaterialIssueList(Integer unitId, Integer orgId, Integer userId) {

		MaterialIssueListDropdownResponse response = new MaterialIssueListDropdownResponse();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (User user : GlobalDao.getPickerUsers(unitId)) {
			response.addToCarrier(new com.param.global.dto.global.User(user.getUserId(), user.getUserName()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getMaterialIssueList(MaterialPickerRequest request) {

//		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<MaterialIssueResponse> response = new ArrayList<MaterialIssueResponse>();

		for (MaterialIssue materialIssue : MaterialIssuePendingDao.searchMaterialIssueList(request, screenId)) {

			Boolean forceClose = false;
			 forceClose =( materialIssue.getStoreIndent() != null  && materialIssue.getStoreIndent().getForceClose() != null )? materialIssue.getStoreIndent().getForceClose() : false ;
			if (materialIssue.getStoreIndent() == null) {
				MaterialIssueResponse materialIssueResponse = new MaterialIssueResponse(materialIssue.getId(),
						materialIssue.getAddedDate(), materialIssue.getIssueNo(),
						// materialIssue.getMaterialPicker().getStoreIndent().getIndentNo(),
						materialIssue.getFromStore().getStoreId(), materialIssue.getFromStore().getStoreName(),
						materialIssue.getToStore().getStoreId(), materialIssue.getToStore().getStoreName(),
						// materialIssue.getMaterialPicker().getStoreIndent().getPriority().getPriorityDesc(),
						// materialIssue.getMaterialPicker().getStoreIndent().getIndentType().getType(),
						materialIssue.getIsConsignment(), materialIssue.getRemark(),
						materialIssue.getStatus().getStatus());
				materialIssueResponse.setIndentType("Direct");
				materialIssueResponse.setForceClose(forceClose);
				response.add(materialIssueResponse);
			} else {
				
				materialIssue.getStoreIndent().getForceClose();
				
				MaterialIssueResponse materialIssueResponse = new MaterialIssueResponse(materialIssue.getId(),
						materialIssue.getAddedDate(), materialIssue.getIssueNo(),
						materialIssue.getStoreIndent().getIndentNo(),
						materialIssue.getFromStore().getStoreId(), materialIssue.getFromStore().getStoreName(),
						materialIssue.getToStore().getStoreId(), materialIssue.getToStore().getStoreName(),
						materialIssue.getStoreIndent().getPriority().getPriorityDesc(),
						materialIssue.getStoreIndent().getIndentType().getType(),
						materialIssue.getIsConsignment(), materialIssue.getRemark(),
						materialIssue.getStatus().getStatus());
						materialIssueResponse.setForceClose(forceClose);

				response.add(materialIssueResponse);

			}

		}
		return response;
	}

	@Override
	@Transactional
	public Object saveMaterialIssueList(MaterialPickerRequest request) {

		/*Integer storeId = request.getToStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String issueNumber = GlobalService.getBillNoByGeneratedCode(request.getToStoreId(), request.getUnitId(), issueScreenId);

		//System.out.println("From Store : "+storeId);

		//System.out.println("Unit : "+unit.getUnitId());

		//System.out.println("User : "+sessionService.getUserId());


		/*MaterialIssue materialIssue = new MaterialIssue(new User(request.getCarrierId()), request.getRemark(),
				new Status(request.getStatusId()), new Store(request.getFromStoreId()),
				new Store(request.getToStoreId()), issueNumber, request.getIsConsignment());*/

		MaterialIssue materialIssue = new MaterialIssue(new User(request.getCarrierId()), request.getRemark(),
				new Status(request.getStatusId()), new Store(request.getToStoreId()), 
				new Store(request.getFromStoreId()), issueNumber, request.getIsConsignment());
		materialIssue.setStatusNew(new Status(request.getStatusId()));
		
	//	sessionService.setEntityDefaults(materialIssue);
		materialIssue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		
		for (MaterialPickerDetailRequest items : request.getItems()) {

			MaterialIssueDetail detail = new MaterialIssueDetail(items.getIssueQuantity(), new Item(items.getItemId()));
		//	sessionService.setEntityDefaults(detail);
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			
			for (BatchResponse batch : items.getBatches()) 
			{
				/*if (batch != null) {
				// Batch issueBatch = BatchStoreTransaction(request.getFromStoreId(), batch.getBatchId(), batch.getIssueQuantity(), new UomType(batch.getUomTypeId()));
				Batch issueBatch = BatchStoreTransaction(request.getToStoreId(), batch.getBatchId(), batch.getIssueQuantity(), new UomType(batch.getUomTypeId()));
                issueBatch.setUpdatedBy(new User(sessionService.getUserId()));
                issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
                MaterialIssueBatchMapper materialIssueBatchMapper=new MaterialIssueBatchMapper(new Batch(batch.getBatchId()), batch.getIssueQuantity(),
						new UomType(batch.getUomTypeId()), new UomUnit(batch.getUomUnitId()), new Status(com.param.common.constants.Status.Pending.getStatus()));
                sessionService.setEntityDefaults(materialIssueBatchMapper);    
				detail.addBatch(materialIssueBatchMapper);
			}*/
				
				//start code by minesh for consignmentlogic and clubBatch consumption/transfer.
				if (batch != null) {
				//Batch batch1 = GlobalDao.getBatch(request.getFromStoreId(), batch.getBatchId());
				// batch1 = GlobalDao.getBatch(request.getToStoreId(), batch.getBatchId());
				
				//List<Batch> batchList = GlobalDao.getSameBatchListFromStoreSQL(batch1.getBatchNo(),request.getFromStoreId());
				//List<Batch> batchList = GlobalDao.getSameBatchListFromStoreSQL(batch1.getBatchNo(),request.getToStoreId());
				
				//List<Batch> sortedBatchList = batchList.stream().sorted((s1, s2) -> s1.getIsConsignment() ? -1 : s2.getIsConsignment() ? 1 : 0).collect(Collectors.toList());				
				//List<Long> batchIds = batchList.stream().map(x-> x.getId()).collect(Collectors.toList());
				//List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchAndStore(batchIds,request.getFromStoreId());
				//List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchAndStore(batchIds,request.getToStoreId());
				
				List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batch.getBatchId(), request.getToStoreId(),request.getUnitId());
				
				int issuingQty = batch.getIssueQuantity();
				
				for (StoreStock storeStockObj : StoreStockList) {
					//Long issueQuantity= Util.ConvertQuantity(Long.valueOf(batch.getIssueQuantity()),storeStockObj.getBatch().getBatchUomList() , new UomType(batch.getUomTypeId()));
				
					Integer issueQuantity = Util.GetLeaseQuantity(batch.getIssueQuantity(), storeStockObj.getBatch().getBatchUomList(), new UomType(batch.getUomTypeId()));
				//	Long batchId = storeStockObj.getBatch().getId();
					
					if (issuingQty != 0) {
						if (issueQuantity >= issuingQty) {
							
								/*Batch issueBatch = BatchStoreTransactionForClubedBatch(request.getFromStoreId(), batchId,
										issuingQty, new UomType(batch.getUomTypeId()));*/
								/*
								 * Batch issueBatch =
								 * BatchStoreTransactionForClubedBatch(request.getToStoreId(),
								 * batch.getBatchId(), issueQuantity, new
								 * UomType(batch.getUomTypeId()),request.getUnitId(),request);
								 * issueBatch.setUpdatedByUser(request.getUserId());
								 * issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()
								 * ));
								 */
			                    MaterialIssueBatchMapper materialIssueBatchMapper=new MaterialIssueBatchMapper(new Batch(batch.getBatchId()), issuingQty,
										new UomType(batch.getUomTypeId()), new UomUnit(batch.getUomUnitId()), new Status(com.param.common.constants.Status.Pending.getStatus()));
			                   // sessionService.setEntityDefaults(materialIssueBatchMapper);    
			                    materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
								detail.addBatch(materialIssueBatchMapper);
							
							issuingQty = 0;
						} else {/*
							int storeAvaliableQuantity = issueQuantity.intValue();
							
								Batch issueBatch = BatchStoreTransactionForClubedBatch(request.getFromStoreId(), batchId,
										storeAvaliableQuantity, new UomType(batch.getUomTypeId()));
								Batch issueBatch = BatchStoreTransactionForClubedBatch(request.getToStoreId(), batchId,
										storeAvaliableQuantity, new UomType(batch.getUomTypeId()),request.getUnitId(),request);
			                    issueBatch.setUpdatedByUser(request.getUserId());
			                    issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			                    MaterialIssueBatchMapper materialIssueBatchMapper=new MaterialIssueBatchMapper(new Batch(batchId), batch.getIssueQuantity(),
										new UomType(batch.getUomTypeId()), new UomUnit(batch.getUomUnitId()), new Status(com.param.common.constants.Status.Pending.getStatus()));
			                 //   sessionService.setEntityDefaults(materialIssueBatchMapper);    
			                    materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			                    detail.addBatch(materialIssueBatchMapper);							
							
							issuingQty = issuingQty - storeAvaliableQuantity;
							*/}
						}
					break;
					}
				}
				//end code by minesh for consignmentlogic and clubBatch consumption/transfer.
				
			}

			materialIssue.addMaterialIssueDetail(detail);
		}

		MaterialIssue master = MaterialIssuePendingDao.saveMaterialIssuePending(materialIssue);
		
		if(master != null){
			Long id= new Long(master.getId());//first way.
			for (MaterialPickerDetailRequest items : request.getItems()) {
				for (BatchResponse batch : items.getBatches()) 
				{
					Integer detailsId = 0; 
					for (MaterialIssueDetail materialDetailsDto : master.getMaterialIssueDetails()) {
						
						for (MaterialIssueBatchMapper dto : materialDetailsDto.getMaterialIssueBatchMappers()) {
							
							if(batch.getBatchId().equals(dto.getBatch().getId()) && batch.getIssueQuantity().equals(dto.getIssueQuantity())) {
								detailsId = dto.getId();
							}
						}
					}
					if (batch != null) {
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batch.getBatchId(), request.getToStoreId(),request.getUnitId());
					
					int issuingQty = batch.getIssueQuantity();
					
					for (StoreStock storeStockObj : StoreStockList) {
					
						Integer issueQuantity = Util.GetLeaseQuantity(batch.getIssueQuantity(), storeStockObj.getBatch().getBatchUomList(), new UomType(batch.getUomTypeId()));
						
						if (issuingQty != 0) {
							if (issueQuantity >= issuingQty) {
								
									Batch issueBatch = BatchStoreTransactionForClubedBatch(request.getToStoreId(), batch.getBatchId(),
											issueQuantity, new UomType(batch.getUomTypeId()),request.getUnitId(),request,detailsId,id,master.getIssueNo());
				                    issueBatch.setUpdatedByUser(request.getUserId());
				                    issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							} else {
								
							}
							}
						break;
						}
					}
				}

			}
			
		}
		
		CommonResponse response = new CommonResponse();
		response.setId(materialIssue.getId().longValue());
		response.setMessage("Material Issue saved successfully");

		return response;
	}

	private Batch BatchStoreTransaction(Integer storeId, Long batchId, Integer quantity, UomType uomType,Integer unitId, MaterialPickerRequest request) {

		screen = GlobalDao.getScreen(screenId);

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

		if (batch != null) {
			// Get lease quantity
			Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

			Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

			// Deduct issued quantity from the batch store stock
			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
			
			// start code for set tQty by minesh.
			Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
			Integer tQty = inTransitQuantity + leaseQuantity;
			batch.getStoreStockList().get(0).settQty(tQty);
			// end code of for set tQty by minesh.

			// Add stock transaction
			StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
					batch.getStoreStockList().get(0).getStore(),0);
			//sessionService.setEntityDefaults(stockTransaction);	
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
		return batch;
	}
	
	private Batch BatchStoreTransactionForClubedBatch(Integer storeId, Long batchId, Integer leaseQuantity, UomType uomType,Integer unitId, MaterialPickerRequest request, Integer detailsId, Long id,String prefixNo) {
		screen = GlobalDao.getScreen(screenId);
		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);
		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

		if (batch != null) {
			// Get lease quantity
			//sInteger leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

			Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

			// Deduct issued quantity from the batch store stock
			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
			
			// start code for set tQty by minesh.
			Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
			Integer tQty = inTransitQuantity + leaseQuantity;
			batch.getStoreStockList().get(0).settQty(tQty);
			// end code of for set tQty by minesh.

			// Add stock transaction
			StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
					batch.getStoreStockList().get(0).getStore(),0);
			stockTransaction.setTransactionDetialsId(detailsId);
			stockTransaction.setTransactionId(id.intValue());
			stockTransaction.setOutQty(leaseQuantity);
			stockTransaction.setIsTransit(true);
			stockTransaction.setTransitNo(prefixNo);
			//sessionService.setEntityDefaults(stockTransaction);	
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
		return batch;
	}

	/* Not In Use For Details */
	/*@Override
	@Transactional
	public Object getMaterialIssueListDetails(Integer id) {

		MaterialIssue materialIssue = MaterialIssuePendingDao.getMaterialIssue(id);

		MaterialPickerResponse response = new MaterialPickerResponse(materialIssue.getId(),
				materialIssue.getAddedDate(), materialIssue.getMaterialPicker().getStoreIndent().getIndentNo(),
				materialIssue.getFromStore().getStoreId(), materialIssue.getFromStore().getStoreName(),
				materialIssue.getToStore().getStoreId(), materialIssue.getToStore().getStoreName(),
				materialIssue.getMaterialPicker().getStoreIndent().getPriority().getPriorityDesc(),
				materialIssue.getMaterialPicker().getStoreIndent().getIndentType().getType(),
				materialIssue.getMaterialPicker().getStoreIndent().getIsConsignment(), PopulateCarriers());

		for (MaterialIssueDetail materialIssueDetail : MaterialIssuePendingDao.getMaterialIssueDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(materialIssue.getFromStore().getStoreId(),
					materialIssueDetail.getItem().getId());

			MaterialIssueBatchMapper materialIssueBatchMapper = materialIssueDetail.getMaterialIssueBatchMappers()
					.get(0);

			MaterialPickerDetailResponse materialPickerDetailResp = new MaterialPickerDetailResponse(
					materialIssueDetail.getId(), materialIssueDetail.getItem().getId(),
					materialIssueDetail.getItem().getCode(), materialIssueDetail.getItem().getName(),
					materialIssueDetail.getItem().getGeneric().getName(), materialIssueBatchMapper.getUomType().getId(),
					materialIssueBatchMapper.getUomType().getType(), materialIssueBatchMapper.getUomUnit().getId(),
					materialIssueBatchMapper.getUomUnit().getUnits(), storeStock);

			Batch batchObj = materialIssueBatchMapper.getBatch();

			for (StoreStock storeStock1 : batchObj.getStoreStockList()) {

				if (materialIssue.getFromStore().getStoreId().equals(storeStock1.getStore().getStoreId())) {

					Integer availbleQuantity = storeStock1.getAvailableQty();

					BatchIssueResponse batchIssueResp = new BatchIssueResponse(batchObj.getId(), batchObj.getBatchNo(),
							batchObj.getExpiryDate(), availbleQuantity, PopulateUom(batchObj));

					materialPickerDetailResp.setBatchObj(batchIssueResp);

				}
			}
			response.addItems(materialPickerDetailResp);
		}
		return response;
	}*/

	/*private List<PickerResponse> PopulateCarriers() {
		List<PickerResponse> pickerResponseList = new ArrayList<PickerResponse>();

		for (Store store : GlobalDao.getUserStoreList()) {
			PickerResponse pickerResponse = new PickerResponse(store.getStoreId(), store.getStoreName());
			pickerResponseList.add(pickerResponse);
		}
		return pickerResponseList;
	}*/

	private List<UomResponse> PopulateUom(Batch batch) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (BatchUomMapper uomMapper : batch.getBatchUomList()) {
			response.add(new UomResponse(uomMapper.getUomType().getType(), uomMapper.getUomType().getId(),
					uomMapper.getUomUnit().getId(), uomMapper.getUomUnit().getUnits(), uomMapper.getConversions(),
					uomMapper.getIpUom(), uomMapper.getOpUom(), uomMapper.getStoreUom()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getMaterialIssueDetails(Integer id,Integer orgId, Integer unitId) {

		MaterialIssue materialIssue = MaterialIssuePendingDao.getMaterialIssue(id);

		String userName = "";
		if(materialIssue.getCarrier() != null) {
			userName = iEmployeeRegistrationDao.getUserNamwByUserId( materialIssue.getCarrier().getUserId());
		}
		MaterialIssueResponse response = new MaterialIssueResponse(materialIssue.getId(), materialIssue.getAddedDate(),
				materialIssue.getIssueNo(),
				(materialIssue.getMaterialPicker() != null
						? materialIssue.getMaterialPicker().getStoreIndent().getIndentNo()
						: null),
				materialIssue.getFromStore().getStoreId(), materialIssue.getFromStore().getStoreName(),
				materialIssue.getToStore().getStoreId(), materialIssue.getToStore().getStoreName(),
				(materialIssue.getMaterialPicker() != null
						? materialIssue.getMaterialPicker().getStoreIndent().getPriority().getPriorityDesc()
						: null),
				(materialIssue.getMaterialPicker() != null
						? materialIssue.getMaterialPicker().getStoreIndent().getIndentType().getType()
						: null),
				(materialIssue.getIsConsignment() != null ? materialIssue.getIsConsignment() : null),
				materialIssue.getCarrier() != null ? materialIssue.getCarrier().getUserId() : 0,
				materialIssue.getCarrier() != null ?userName +" - "+ materialIssue.getCarrier().getUserName() : null,
				(materialIssue.getRemark() != null ? materialIssue.getRemark() : null));

		for (MaterialIssueDetail materialIssueDetail : MaterialIssuePendingDao.getMaterialIssueDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(materialIssue.getFromStore().getStoreId(),
					materialIssueDetail.getItem().getId(),unitId);
			Long availableStock = 0l;
			if(materialIssueDetail.getMaterialIssueBatchMappers().size()>0) {
				availableStock =Util.ConvertQuantity(storeStock,
						materialIssueDetail.getMaterialIssueBatchMappers().get(0).getBatch().getBatchUomList(),
						materialIssueDetail.getMaterialIssueBatchMappers().get(0).getUomType());
			}
			

			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(materialIssue.getFromStore().getStoreId(),
					materialIssueDetail.getItem().getId());
			// Long tQuantity = Util.ConvertQuantity(stockInTransit,
			// materialIssueDetail.getMaterialIssueBatchMappers().get(0).getBatch().getBatchUomList(),
			// materialIssueDetail.getMaterialIssueBatchMappers().get(0).getUomType());

			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(materialIssue.getFromStore().getStoreId(),
					materialIssueDetail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			if(maxOrderQuantity < 0){	maxOrderQuantity = 0L;	}
			Long maxStoreQty = 0l;
			if(materialIssueDetail.getMaterialIssueBatchMappers().size()>0) {
				
				maxStoreQty = Util.ConvertQuantity(maxOrderQuantity,
						materialIssueDetail.getMaterialIssueBatchMappers().get(0).getBatch().getBatchUomList(),
						materialIssueDetail.getMaterialIssueBatchMappers().get(0).getUomType());
			}	
					
			Integer pendingQunatity = 0;
			/*
			 * if(materialIssueDetail.getMaterialPickerDetail() != null ) { pendingQunatity=
			 * materialIssueDetail.getMaterialPickerDetail().getStoreIndentDetail().
			 * getAuthorizedQuantity() - materialIssueDetail.getIssueQuantity(); }
			 */
			for (MaterialIssueBatchMapper materialIssueBatchMapper : materialIssueDetail
					.getMaterialIssueBatchMappers()) {
				if(materialIssueBatchMapper.getIsActive().equals(true) && materialIssueBatchMapper.getIsDeleted().equals(false))
				{
					Batch batchObj = materialIssueBatchMapper.getBatch();
	
					MaterialIssueItemResponse materialPickerDetailResp = new MaterialIssueItemResponse(
							materialIssueDetail.getId(), materialIssueDetail.getItem().getId(),
							materialIssueDetail.getItem().getCode(), materialIssueDetail.getItem().getName(),
							materialIssueDetail.getItem().getGeneric().getName(),
							materialIssueBatchMapper.getUomType().getId(), materialIssueBatchMapper.getUomType().getType(),
							materialIssueBatchMapper.getUomUnit().getId(), materialIssueBatchMapper.getUomUnit().getUnits(),
							materialIssueDetail.getMaterialPickerDetail() != null
									? materialIssueDetail.getMaterialPickerDetail().getStoreIndentDetail()
											.getAuthorizedQuantity()
									: null,
							materialIssueDetail.getStoreIndentDetail() != null
									? getPendingQuantity(materialIssueDetail.getItem().getId(),materialIssueDetail.getStoreIndentDetail().getPendingQuantity(),materialIssueBatchMapper.getUomType().getId(),unitId,orgId)
									: null,
							materialIssueBatchMapper.getIssueQuantity(), availableStock, batchObj.getId(), batchObj.getBatchNo(),
							batchObj.getExpiryDate(), maxStoreQty, materialIssueDetail.getItem().getIsThreeUOMApplicable(), 
                            GlobalDao.getUnitOfMeasurementListByItem(materialIssueDetail.getItem().getId(), orgId));
					if(materialPickerDetailResp.getIssueQuantity()>0)
					{
					response.addItems(materialPickerDetailResp);
					}
				}

			}
			if(materialIssueDetail.getMaterialIssueBatchMappers().size()==0) {
				MaterialIssueItemResponse materialPickerDetailResp = new MaterialIssueItemResponse(
						materialIssueDetail.getId(), materialIssueDetail.getItem().getId(),
						materialIssueDetail.getItem().getCode(), materialIssueDetail.getItem().getName(),
						materialIssueDetail.getItem().getGeneric().getName(),
						null, null,
						null, null,
						materialIssueDetail.getMaterialPickerDetail() != null
								? materialIssueDetail.getMaterialPickerDetail().getStoreIndentDetail()
										.getAuthorizedQuantity()
								: null,
						null,
						0, availableStock, null, null,
						null, maxStoreQty, materialIssueDetail.getItem().getIsThreeUOMApplicable(), 
                        GlobalDao.getUnitOfMeasurementListByItem(materialIssueDetail.getItem().getId(), orgId));
				if(materialPickerDetailResp.getIssueQuantity()>0)
				{
					response.addItems(materialPickerDetailResp);
				}
			}
		}
		return response;
	}
	Integer getPendingQuantity(Integer itemId,Integer qty,Integer uomTypeId,Integer unitId,Integer orgId)
	{
		//System.out.println("Item Id : "+itemId);
		//System.out.println("Qty Id : "+qty);
		//System.out.println("UOM Type Id : "+uomTypeId);

		Integer dispenseConversion=GlobalDao.getDispenseConversionByItem(itemId,orgId);
		//System.out.println("Dispense Conversion : "+dispenseConversion);

		Integer stockConversion=GlobalDao.getStockConversionByItem(itemId,orgId);
		//System.out.println("Stock Conversion : "+stockConversion);

		Integer purchaseConversion=GlobalDao.getPurchaseConversionByItem(itemId,orgId);
		//System.out.println("Purchase Conversion : "+purchaseConversion);

		Integer originalQty=qty;
		//System.out.println("Original Qty : "+originalQty);

		Integer dispenseConversionQty=originalQty/dispenseConversion;
		//System.out.println("Dispense Conversion Qty : "+dispenseConversionQty);

		Integer stockConversionQty=dispenseConversionQty/stockConversion;
		//System.out.println("Stock Conversion Qty : "+stockConversionQty);

		Integer purchaseConversionQty=stockConversionQty/purchaseConversion;
		//System.out.println("Purchase Conversion Qty : "+purchaseConversionQty);

		if (uomTypeId==1)
			if(purchaseConversionQty.equals(0))
			{
				return	qty;
			}
			else
			{
				return purchaseConversionQty;	
			}
			
		if (uomTypeId==2)
			return  stockConversionQty;
		if (uomTypeId==3)
			return dispenseConversionQty;
		return dispenseConversionQty;

	}

	@Override
	@Transactional
	public MaterialIssue getMaterialIssueDetailsById(MaterialPickerRequest request , Long id) {
		MaterialIssue res =  MaterialIssuePendingDao.getMaterialIssue(id.intValue());
		
		for (MaterialPickerDetailRequest items : request.getItems()) {
			for (BatchResponse batch : items.getBatches()) 
			{
				Integer detailsId = 0; 
				for (MaterialIssueDetail materialDetailsDto : res.getMaterialIssueDetails()) {
					
					for (MaterialIssueBatchMapper dto : materialDetailsDto.getMaterialIssueBatchMappers()) {
						
						if(batch.getBatchId().equals(dto.getBatch().getId()) && batch.getIssueQuantity().equals(dto.getIssueQuantity())) {
							detailsId = dto.getId();
						}
					}
				}
				if (batch != null) {
				List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batch.getBatchId(), request.getToStoreId(),request.getUnitId());
				
				int issuingQty = batch.getIssueQuantity();
				
				for (StoreStock storeStockObj : StoreStockList) {
				
					Integer issueQuantity = Util.GetLeaseQuantity(batch.getIssueQuantity(), storeStockObj.getBatch().getBatchUomList(), new UomType(batch.getUomTypeId()));
					
					if (issuingQty != 0) {
						if (issueQuantity >= issuingQty) {
							
								Batch issueBatch = BatchStoreTransactionForClubedBatch(request.getToStoreId(), batch.getBatchId(),
										issueQuantity, new UomType(batch.getUomTypeId()),request.getUnitId(),request,detailsId,id,res.getIssueNo());
			                    issueBatch.setUpdatedByUser(request.getUserId());
			                    issueBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						} else {
							
						}
						}
					break;
					}
				}
			}

		}

		
		return res;
	}

}
