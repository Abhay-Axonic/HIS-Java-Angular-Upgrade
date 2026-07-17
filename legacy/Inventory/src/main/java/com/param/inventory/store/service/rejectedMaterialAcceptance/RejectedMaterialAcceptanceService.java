package com.param.inventory.store.service.rejectedMaterialAcceptance;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.RejectDetail;
import com.param.entity.model.inventory.Return;
import com.param.entity.model.inventory.ReturnDetail;
import com.param.entity.model.inventory.StockTransaction;
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
import com.param.inventory.store.dao.materialReturnAcceptance.IMaterialReturnAcceptanceDao;
import com.param.inventory.store.dao.materialReturnAcceptance.MaterialReturnAcceptanceDao;
import com.param.inventory.store.dao.rejectedMaterialAcceptance.IRejectedMaterialAcceptanceDao;
import com.param.inventory.store.dto.materialIssueAcceptance.ItemRequest;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueRequest;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialsDropdownResponse;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueItemResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.rejectMaterialAcceptance.RejectMaterialResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("RejectedMaterialAcceptanceService")
public class RejectedMaterialAcceptanceService implements IRejectedMaterialAcceptanceService {

	private Integer screenId = Screen.MaterialRejectAcceptance.getScreen();
	
	private Integer rejectScreenId = Screen.MaterialRejectAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IRejectedMaterialAcceptanceDao RejectedMaterialAcceptanceDao;
	
	@Autowired
	IMaterialReturnAcceptanceDao MaterialReturnAcceptanceDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Override
	@Transactional
	public MaterialsDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		MaterialsDropdownResponse response = new MaterialsDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getRejectMaterialAcceptanceList(MaterialPickerRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		return RejectedMaterialAcceptanceDao.searchRejectMaterial(request, screenId);
	}

	@Override
	@Transactional
	public Object getRejectMaterialAcceptanceDetails(Integer id) {

		Reject reject = RejectedMaterialAcceptanceDao.getReject(id);

		RejectMaterialResponse response = null;

		if (reject.getMaterialIssue() != null) {
			response = new RejectMaterialResponse(reject.getId(), reject.getRejectedDate(), reject.getReturnNo(),
					reject.getToStore().getStoreId(), reject.getToStore().getStoreName(),
					reject.getFromStore().getStoreId(), reject.getFromStore().getStoreName(),
					reject.getRejectedBy().getUserName(),
					(reject.getMaterialIssue() != null
							? reject.getMaterialIssue().getMaterialPicker() != null 
							? reject.getMaterialIssue().getMaterialPicker().getStoreIndent() != null
							? reject.getMaterialIssue().getMaterialPicker().getStoreIndent().getIsConsignment() : null : null
							: null),
					(reject.getMaterialIssue() != null ? reject.getRemark(): null),
					reject.getStatus().getStatus(), reject.getStatus().getId(), reject.getMaterialIssue().getId());
		}
		if (reject.getmReturn() != null) {
			response = new RejectMaterialResponse(reject.getId(), reject.getRejectedDate(), reject.getReturnNo(),
					reject.getToStore().getStoreId(), reject.getToStore().getStoreName(),
					reject.getFromStore().getStoreId(), reject.getFromStore().getStoreName(),
					reject.getRejectedBy().getUserName(),
					(reject.getMaterialIssue() != null
							? reject.getMaterialIssue().getMaterialPicker() != null 
							? reject.getMaterialIssue().getMaterialPicker().getStoreIndent() != null
							? reject.getMaterialIssue().getMaterialPicker().getStoreIndent().getIsConsignment() : null : null
							: null),
					reject.getStatus().getId(), 
					reject.getmReturn() != null ? reject.getmReturn().getId() : null,
					(reject.getmReturn()!= null? reject.getRemark(): null),
					reject.getStatus().getStatus());
		}
		for (RejectDetail rejectDetail : RejectedMaterialAcceptanceDao.getRejectDetail(id)) {

			if (rejectDetail.getMaterialIssueBatchMapper() != null) {

				MaterialIssueBatchMapper materialIssueBatchMapper = rejectDetail.getMaterialIssueBatchMapper();
				Batch batchObj = rejectDetail.getMaterialIssueBatchMapper() != null
						? rejectDetail.getMaterialIssueBatchMapper().getBatch(): null;
				MaterialIssueItemResponse rejectMaterialResponse = new MaterialIssueItemResponse(rejectDetail.getId(),
				materialIssueBatchMapper != null ? materialIssueBatchMapper.getBatch().getItem().getCode(): null,
				materialIssueBatchMapper != null ? materialIssueBatchMapper.getBatch().getItem().getName(): null,
				materialIssueBatchMapper != null? materialIssueBatchMapper.getBatch().getItem().getGeneric().getName(): null,
				rejectDetail.getMaterialIssueBatchMapper() != null? rejectDetail.getMaterialIssueBatchMapper().getId(): null,
						materialIssueBatchMapper != null ? materialIssueBatchMapper.getUomType().getId() : 0,
						materialIssueBatchMapper != null ? materialIssueBatchMapper.getUomType().getType() : null,
						materialIssueBatchMapper != null ? materialIssueBatchMapper.getUomUnit().getId() : 0,
						materialIssueBatchMapper != null ? materialIssueBatchMapper.getUomUnit().getUnits() : null,
						batchObj != null ? batchObj.getId() : 0, batchObj != null ? batchObj.getBatchNo() : null,
						batchObj != null ? batchObj.getExpiryDate() : null,
						rejectDetail.getAcceptedQuantity() != null ? rejectDetail.getAcceptedQuantity() : 0,
						rejectDetail.getRejectedQuantity() != null ? rejectDetail.getRejectedQuantity() : 0,
						rejectDetail.getRejectedNote() != null ? rejectDetail.getRejectedNote() : null,
						rejectDetail.getRejectedRejectQuantity() != null ? rejectDetail.getRejectedRejectQuantity(): 0,
						rejectDetail.getRejectReason() != null ? rejectDetail.getRejectReason().getReason() : null,
					    batchObj != null ? batchObj.getItem().getId() : null, batchObj.getItem().getIsThreeUOMApplicable(),
			    		GlobalDao.getUnitOfMeasurementListByItem(batchObj.getItem().getId(), reject.getOrganizationId()));
				response.addItems(rejectMaterialResponse);
			}

			if (rejectDetail.getReturnDetail() != null) {
				ReturnDetail returnDetail = rejectDetail.getReturnDetail();

				Batch batch = rejectDetail.getReturnDetail() != null ? rejectDetail.getReturnDetail().getBatch() : null;

				MaterialIssueItemResponse rejectMaterialResponse = new MaterialIssueItemResponse(rejectDetail.getId(),
						returnDetail != null ? returnDetail.getBatch().getItem().getCode() : null,
						returnDetail != null ? returnDetail.getBatch().getItem().getName() : null,
						returnDetail != null ? returnDetail.getBatch().getItem().getGeneric().getName() : null,
						returnDetail != null ? returnDetail.getUomType().getId() : 0,
						returnDetail != null ? returnDetail.getUomType().getType() : null,
						returnDetail != null ? returnDetail.getUomUnit().getId() : 0,
						returnDetail != null ? returnDetail.getUomUnit().getUnits() : null,
						batch != null ? batch.getId() : 0, batch != null ? batch.getBatchNo() : null,
						batch != null ? batch.getExpiryDate() : null,
						rejectDetail.getReturnDetail() != null ? rejectDetail.getReturnDetail().getId() : null,
						rejectDetail.getAcceptedQuantity() != null ? rejectDetail.getAcceptedQuantity() : 0,
						rejectDetail.getRejectedQuantity() != null ? rejectDetail.getRejectedQuantity() : 0,
						rejectDetail.getRejectedNote() != null ? rejectDetail.getRejectedNote() : null,
						rejectDetail.getRejectedRejectQuantity() != null ? rejectDetail.getRejectedRejectQuantity() : 0,
						rejectDetail.getRejectReason() != null ? rejectDetail.getRejectReason().getReason() : null,
					    batch != null ? batch.getItem().getId() : null, batch.getItem().getIsThreeUOMApplicable(),
			    		GlobalDao.getUnitOfMeasurementListByItem(batch.getItem().getId(), reject.getOrganizationId())
						);
				response.addItems(rejectMaterialResponse);
			}
		}
	  return response;
	}

	@Override
	@Transactional
	public Object saveRejectMaterialAcceptance(MaterialIssueRequest request) {/*

		screen = GlobalDao.getScreen(screenId);

		Reject reject = RejectedMaterialAcceptanceDao.getReject(request.getId());

	//  Integer storeId = reject.getToStore().getStoreId();

		for (ItemRequest item : request.getItems()) {

			if (item.getRejectQuantity() > 0) {
				
				Integer storeId = RejectedMaterialAcceptanceDao.getReject(request.getId()).getToStore().getStoreId();

				Store storeNew = GlobalDao.getStore(storeId);

				Unit unit = GlobalDao.getUnit(sessionService.getUnitId());

				//System.out.println("Store Id : "+storeId);

				//System.out.println("Unit Id : "+unit.getUnitId());

				String rejectNumber = GlobalService.getBillNoByGeneratedCode(storeNew, unit, rejectScreenId);

				//System.out.println("Reject Number : "+rejectNumber);

				if (reject.getMaterialIssue() != null) {
					Reject rejectNew = new Reject(new Store(reject.getFromStore().getStoreId()),
							new Store(reject.getToStore().getStoreId()), new User(sessionService.getUserId()), TimeZoneComponent.getDateByzone(unitId),
							rejectNumber, new MaterialIssue(request.getIssueId()), new Status(2));
					sessionService.setEntityDefaults(rejectNew);
					
					RejectDetail rejectDetail=new RejectDetail(new RejectReason(item.getRejectReasonId()), item.getRejectNote(),
							item.getRejectQuantity(), new MaterialIssueBatchMapper(item.getBatchMapperId()));
					sessionService.setEntityDefaults(rejectDetail);
					rejectNew.addRejectDetail(rejectDetail);

					RejectedMaterialAcceptanceDao.save(rejectNew);
				}

				if (reject.getmReturn() != null) {

					Reject rejectNew = new Reject(new Store(reject.getFromStore().getStoreId()),
							new Store(reject.getToStore().getStoreId()), new User(sessionService.getUserId()), TimeZoneComponent.getDateByzone(unitId),
							rejectNumber, new Status(2), new Return(request.getReturnId()));

					rejectNew.addRejectDetail(
							new RejectDetail(new RejectReason(item.getRejectReasonId()), item.getRejectNote(),
									item.getRejectQuantity(), new ReturnDetail(item.getReturnDetailId())));

					RejectedMaterialAcceptanceDao.save(rejectNew);
				}
			}

			for (RejectDetail detail : RejectedMaterialAcceptanceDao.getRejectDetail(request.getId())) {

				if (item.getId().equals(detail.getId())) {

					detail.setAcceptedQuantity(item.getAcceptedQuantity());
					detail.setRejectedRejectQuantity(item.getRejectQuantity());
					detail.setAcceptedBy(new User(sessionService.getUserId()));
					detail.setAcceptedOn(TimeZoneComponent.getDateByzone(unitId));
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
					detail.setUpdatedBy(reject.getUser());
					reject.addRejectDetail(detail);
					
					 * updateStoreStock(reject, detail.getMaterialIssueBatchMapper().getBatch(),
					 * item.getAcceptedQuantity(),
					 * detail.getMaterialIssueBatchMapper().getUomType());
					 
					if (reject.getMaterialIssue() != null) {
						updateStoreStock(reject, detail.getMaterialIssueBatchMapper().getBatch(),
								item.getAcceptedQuantity(), detail.getRejectedQuantity(),
								detail.getMaterialIssueBatchMapper().getUomType());
					}

					if (reject.getmReturn() != null) {
						updateStoreStock(reject, detail.getReturnDetail().getBatch(), item.getAcceptedQuantity(),
								detail.getRejectedQuantity(), detail.getReturnDetail().getUomType());
					}
					
					 * updateStoreStock(storeId,
					 * detail.getMaterialIssueBatchMapper().getBatch().getId(),
					 * item.getAcceptedQuantity(),
					 * detail.getMaterialIssueBatchMapper().getUomType());
					 
				}
			}

		}
		reject.setStatus(new Status(request.getStatusId()));

		reject.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		reject.setUpdatedBy(reject.getUser());
		RejectedMaterialAcceptanceDao.updateReject(reject);
		
		 * Boolean flag = false; Reject rejectNew = new Reject(new
		 * Store(reject.getFromStore().getStoreId()), new
		 * Store(reject.getToStore().getStoreId()), new User(sessionService.getUserId()), new
		 * Date(), "RET-101", new MaterialIssue(request.getIssueId()), new Status(2));
		 * 
		 * for (ItemRequest item : request.getItems()) { if (item.getRejectQuantity() >
		 * 0) { flag = true; reject.addRejectDetail( new RejectDetail(new
		 * RejectReason(item.getRejectReasonId()), item.getRejectNote(),
		 * item.getRejectQuantity(), new
		 * MaterialIssueBatchMapper(item.getBatchMapperId()), new
		 * Reject(rejectNew.getId()))); } } if (!reject.getRejectDetails().isEmpty()) {
		 * RejectedMaterialAcceptanceDao.save(rejectNew); }
		 
		CommonResponse response = new CommonResponse();
		response.setId(reject.getId().longValue());
		response.setMessage("Reject Material Acceptance Updated successfully");
		return response;
	*/
		
	


		screen = GlobalDao.getScreen(screenId);

		Reject reject = RejectedMaterialAcceptanceDao.getReject(request.getId());

	//  Integer storeId = reject.getToStore().getStoreId();

		for (ItemRequest item : request.getItems()) {

			if (item.getRejectQuantity() > 0) {
				
//				Integer storeId = RejectedMaterialAcceptanceDao.getReject(request.getId()).getToStore().getStoreId();
                Integer storeId = RejectedMaterialAcceptanceDao.getReject(request.getId()).getFromStore().getStoreId();

				/*Store storeNew = GlobalDao.getStore(storeId);

				Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

				//System.out.println("Store Id : "+storeId);

				//System.out.println("Unit Id : "+unit.getUnitId());

				String rejectNumber = GlobalService.getBillNoByGeneratedCode(storeId, request.getUnitId(), rejectScreenId);

				//System.out.println("Reject Number : "+rejectNumber);

				if (reject.getMaterialIssue() != null) {
//					Reject rejectNew = new Reject(new Store(reject.getFromStore().getStoreId()),
//							new Store(reject.getToStore().getStoreId()), new User(request.getUserId()), TimeZoneComponent.getDateByzone(request.getUnitId()),
                    Reject rejectNew = new Reject(new Store(reject.getToStore().getStoreId()),
                            new Store(reject.getFromStore().getStoreId()), new User(request.getUserId()), TimeZoneComponent.getDateByzone(request.getUnitId()),
							rejectNumber, new MaterialIssue(request.getIssueId()), new Status(2));
				//	sessionService.setEntityDefaults(rejectNew);
					rejectNew.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					
					
					RejectDetail rejectDetail=new RejectDetail(new RejectReason(item.getRejectReasonId()), item.getRejectNote(),
							item.getRejectQuantity(), new MaterialIssueBatchMapper(item.getBatchMapperId()));
				//	sessionService.setEntityDefaults(rejectDetail);
					rejectDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					rejectNew.addRejectDetail(rejectDetail);

					RejectedMaterialAcceptanceDao.save(rejectNew);
				}

				if (reject.getmReturn() != null) {

					Reject rejectNew = new Reject(new Store(reject.getFromStore().getStoreId()),
							new Store(reject.getToStore().getStoreId()), new User(request.getUserId()), TimeZoneComponent.getDateByzone(request.getUnitId()),
							rejectNumber, new Status(2), new Return(request.getReturnId()));
					rejectNew.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());

					RejectDetail rd=	new RejectDetail(new RejectReason(item.getRejectReasonId()), item.getRejectNote(),
							item.getRejectQuantity(), new ReturnDetail(item.getReturnDetailId()));
					rd.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
					rejectNew.addRejectDetail(rd);

					RejectedMaterialAcceptanceDao.save(rejectNew);
				}
			}

			for (RejectDetail detail : RejectedMaterialAcceptanceDao.getRejectDetail(request.getId())) {

				if (item.getId().equals(detail.getId())) {

					detail.setAcceptedQuantity(item.getAcceptedQuantity());
					detail.setRejectedRejectQuantity(item.getRejectQuantity());
					detail.setAcceptedBy(new User(request.getUserId()));
					detail.setAcceptedOn(TimeZoneComponent.getDateByzone(request.getUnitId()));
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			//		detail.setUpdatedBy(reject.getUser());
					detail.setUpdatedByUser(request.getUserId());
					reject.addRejectDetail(detail);
					/*
					 * updateStoreStock(reject, detail.getMaterialIssueBatchMapper().getBatch(),
					 * item.getAcceptedQuantity(),
					 * detail.getMaterialIssueBatchMapper().getUomType());
					 */
					if (reject.getMaterialIssue() != null) {
						updateStoreStock(reject, detail.getMaterialIssueBatchMapper().getBatch(),
								item.getAcceptedQuantity(), item.getRejectQuantity(),
								detail.getMaterialIssueBatchMapper().getUomType(),request, detail);
					}

					if (reject.getmReturn() != null) {
						updateStoreStock(reject, detail.getReturnDetail().getBatch(), item.getAcceptedQuantity(),
								item.getRejectQuantity(), detail.getReturnDetail().getUomType(),request,detail);
					}
					/*
					 * updateStoreStock(storeId,
					 * detail.getMaterialIssueBatchMapper().getBatch().getId(),
					 * item.getAcceptedQuantity(),
					 * detail.getMaterialIssueBatchMapper().getUomType());
					 */
				}
			}

		}
		reject.setStatus(new Status(request.getStatusId()));

		reject.setUpdatedDate(TimeZoneComponent.getDateByzone(reject.getUnitId()));
	//	reject.setUpdatedBy(reject.getUser());
		reject.setUpdatedByUser(request.getUserId());
		RejectedMaterialAcceptanceDao.updateReject(reject);
		RejectedMaterialAcceptanceDao.updatereturn(request.getReturnId(),reject.getRejectDetails().getFirst().getAcceptedQuantity());
		
		
		/*
		 * Boolean flag = false; Reject rejectNew = new Reject(new
		 * Store(reject.getFromStore().getStoreId()), new
		 * Store(reject.getToStore().getStoreId()), new User(sessionService.getUserId()), new
		 * Date(), "RET-101", new MaterialIssue(request.getIssueId()), new Status(2));
		 * 
		 * for (ItemRequest item : request.getItems()) { if (item.getRejectQuantity() >
		 * 0) { flag = true; reject.addRejectDetail( new RejectDetail(new
		 * RejectReason(item.getRejectReasonId()), item.getRejectNote(),
		 * item.getRejectQuantity(), new
		 * MaterialIssueBatchMapper(item.getBatchMapperId()), new
		 * Reject(rejectNew.getId()))); } } if (!reject.getRejectDetails().isEmpty()) {
		 * RejectedMaterialAcceptanceDao.save(rejectNew); }
		 */
		CommonResponse response = new CommonResponse();
		response.setId(reject.getId().longValue());
		response.setMessage("Reject Material Acceptance Updated successfully");
		return response;
	
	
	
	
	
	}

	private void updateStoreStock(Reject reject, Batch batch, Integer acceptedQuantity, Integer rejectQuantity,
			UomType uomType, MaterialIssueRequest request, RejectDetail d) {

		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(reject.getToStore().getStoreId(), batch.getId());

		Integer leaseAcceptQuantity = Util.GetLeaseQuantity(acceptedQuantity, batch.getBatchUomList(), uomType);

		Integer leaseRejectQuantity = Util.GetLeaseQuantity(rejectQuantity, batch.getBatchUomList(), uomType);

		// Integer inTransitQuantity =
		// GlobalDao.getTransitQuantityByBatch(reject.getFromStore().getStoreId(),
		// batch.getId());

		Integer to_inTransitQuantity = GlobalDao.getTransitQuantityByBatch(reject.getToStore().getStoreId(),
				batch.getId());

		Integer from_inTransitQuantity = GlobalDao.getTransitQuantityByBatch(reject.getFromStore().getStoreId(),
				batch.getId());

		//HIS-15715
		Integer balanceQuantity = avaialableStoreStock + leaseAcceptQuantity;

		Integer tQty = to_inTransitQuantity - leaseAcceptQuantity;
		Integer newTQty = from_inTransitQuantity;
		List<StoreStock> fromStoreStock = batch.getStoreStockList().stream()
				.filter(i -> i.getStore().getStoreId() == reject.getFromStore().getStoreId())
				.collect(Collectors.toList());
		if (fromStoreStock != null && fromStoreStock.size() > 0) {
			
			if(newTQty < 0)
			{
				//fromStoreStock.get(0).settQty((fromStoreStock.get(0).gettQty()-leaseRejectQuantity)-leaseAcceptQuantity);	
				
			}
			else
			{
				fromStoreStock.getFirst().settQty(newTQty);	
				//saveStockTransaction(true,leaseAcceptQuantity, fromStoreStock.get(0), request,d.getId(),d.getReject().getReturnNo(),reject,balanceQuantity,d,leaseRejectQuantity);
			}
			
		}

		List<StoreStock> toStoreStock = batch.getStoreStockList().stream()
				.filter(i -> i.getStore().getStoreId() == reject.getToStore().getStoreId())
				.collect(Collectors.toList());
		if (toStoreStock != null && toStoreStock.size() > 0) {

			toStoreStock.getFirst().settQty(tQty);
			toStoreStock.getFirst().setAvailableQty(balanceQuantity);
			saveStockTransaction(true,leaseAcceptQuantity,toStoreStock.getFirst(), request,d.getId(),d.getReject().getReturnNo(),reject,balanceQuantity,d,leaseRejectQuantity);
			//saveStockTransaction(true,tQty, toStoreStock.get(0), request,d.getMaterialIssueBatchMapper().getBatch().getItem().getId(),d.getReject().getReturnNo());

			/*StockTransaction stockTransaction=new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseAcceptQuantity, batch.getStoreStockList().get(0).getStore(),d.getId());
		//	sessionService.setEntityDefaults(stockTransaction);	
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addStockTransaction(stockTransaction);*/
		}

	}
	private void saveStockTransaction(Boolean flag,Integer leaseQuantity, StoreStock storeStock, MaterialIssueRequest request, Integer integer, String returnBillNumber, Reject reject, Integer balanceQuantity, RejectDetail d, Integer leaseRejectQuantity) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(balanceQuantity,
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(),storeStock.getStore(),
				new Batch(storeStock.getBatch().getId()),integer);
		stockTransaction.setTransitNo(returnBillNumber);
		stockTransaction.setItem(storeStock.getBatch().getItem());
		Integer totalRejectedQty=0;
		if(leaseRejectQuantity != null && leaseRejectQuantity >0) {
			stockTransaction.setIsTransit(true);
			stockTransaction.setOutQty(leaseRejectQuantity);
			totalRejectedQty = leaseRejectQuantity + leaseQuantity;
		}else {
			stockTransaction.setIsTransit(false);
			totalRejectedQty = leaseQuantity;
		}
		stockTransaction.setTransactionId(reject.getId());
		stockTransaction.setInQty(leaseQuantity);
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
	
		if(d.getReturnDetail() != null && d.getReturnDetail().getId() != null ) {
			Integer count = MaterialReturnAcceptanceDao.updateIntransitQty(d.getReturnDetail().getId(),46);
			if(count == 0) {
				 MaterialReturnAcceptanceDao.updateIntransitByBatchIdQty(storeStock.getBatch().getId().intValue(),totalRejectedQty ,34);
			}
		}
		
		if(d.getMaterialIssueBatchMapper() != null && d.getMaterialIssueBatchMapper().getId() != null ) {
			Integer count = MaterialReturnAcceptanceDao.updateIntransitQty(d.getMaterialIssueBatchMapper().getId(),29);
			if(count == 0) {
				 MaterialReturnAcceptanceDao.updateIntransitByBatchIdQty(storeStock.getBatch().getId().intValue(),totalRejectedQty ,34);
			}
		}
		
		
		GlobalDao.saveStockTransaction(stockTransaction);
	}
	/*
	 * private void updateStoreStock(Reject reject, Batch batch, Integer
	 * acceptedQuantity, UomType uomType) {
	 * 
	 * Integer avaialableStoreStock =
	 * GlobalDao.getStoreStockByBatch(reject.getToStore().getStoreId(),
	 * batch.getId());
	 * 
	 * Integer leaseQuantity = Util.GetLeaseQuantity(acceptedQuantity,
	 * batch.getBatchUomList(), uomType);
	 * 
	 * Integer inTransitQuantity =
	 * GlobalDao.getTransitQuantityByBatch(reject.getFromStore().getStoreId(),
	 * batch.getId());
	 * 
	 * Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
	 * 
	 * Integer tQty = inTransitQuantity - leaseQuantity;
	 * 
	 * List<StoreStock> fromStoreStock = batch.getStoreStockList().stream().filter(i
	 * -> i.getStore().getStoreId() ==
	 * reject.getFromStore().getStoreId()).collect(Collectors.toList());
	 * if(fromStoreStock!=null && fromStoreStock.size()>0) {
	 * fromStoreStock.get(0).settQty(tQty); }
	 * 
	 * List<StoreStock> toStoreStock = batch.getStoreStockList().stream().filter(i
	 * -> i.getStore().getStoreId() ==
	 * reject.getToStore().getStoreId()).collect(Collectors.toList());
	 * if(toStoreStock!=null && toStoreStock.size()>0) {
	 * toStoreStock.get(0).setAvailableQty(balanceQuantity);
	 * 
	 * batch.addStockTransaction(new StockTransaction(avaialableStoreStock, screen,
	 * screen.getCode(), leaseQuantity,
	 * batch.getStoreStockList().get(0).getStore())); } else { StoreStock
	 * storeStockNew = new StoreStock(leaseQuantity, new
	 * Store(reject.getToStore().getStoreId()), new Batch(batch.getId()));
	 * 
	 * MaterialIssueAcceptanceDao.saveStoreStock(storeStockNew);
	 * 
	 * batch.addStockTransaction(new StockTransaction(avaialableStoreStock, screen,
	 * screen.getCode(), leaseQuantity,
	 * batch.getStoreStockList().get(0).getStore())); }
	 * 
	 * }
	 */

	/*
	 * private Batch updateStoreStock(Integer storeId, Long batchId, Integer
	 * quantity, UomType uomType) { {
	 * 
	 * // get batch needs to be issued // Batch batch = GlobalDao.getBatch(storeId,
	 * batchId);
	 * 
	 * Batch batch = GlobalDao.getBatchByBatchUom(batchId);
	 * 
	 * // Get existing batch store stock Integer avaialableStoreStock =
	 * GlobalDao.getStoreStockByBatch(storeId, batchId);
	 * 
	 * // Get lease quantity Integer leaseQuantity = Util.GetLeaseQuantity(quantity,
	 * batch.getBatchUomList(), uomType);
	 * 
	 * Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
	 * 
	 * // Deduct issued quantity from the batch store stock
	 * batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
	 * 
	 * // Add stock transaction batch.addStockTransaction(new
	 * StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
	 * batch.getStoreStockList().get(0).getStore()));
	 * 
	 * return batch; } }
	 */
}
