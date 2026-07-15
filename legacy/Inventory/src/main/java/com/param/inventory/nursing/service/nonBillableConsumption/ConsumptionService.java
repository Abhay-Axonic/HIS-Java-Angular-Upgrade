package com.param.inventory.nursing.service.nonBillableConsumption;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.NonBillableConsumption;
import com.param.entity.model.inventory.NonBillableConsumptionDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.BatchResponse1;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.nursing.dao.nonBillableConsumption.IConsumptionDao;
import com.param.inventory.nursing.dto.nonBillableConsumption.BatchRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.BatchResponse;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionDropdownResponse;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionResponse;
import com.param.inventory.nursing.service.consumableAcceptance.IConsumableAcceptanceService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service("ConsumptionService")
public class ConsumptionService implements IConsumptionService {

	@Autowired
	IConsumptionDao ConsumptionDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IConsumableAcceptanceService ConsumableAcceptanceService;

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.NonBillableConsumption.getScreen();

	@Override
	@Transactional
	public Object getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		ConsumptionDropdownResponse response = new ConsumptionDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object saveConsumption(ConsumptionRequest request) {
		Integer storeId = request.getStoreId();
		// Integer unitId=sessionService.getUnitId();
		/*
		 * //System.out.println("Unit Id:-"+unitId);
		 * //System.out.println("Screen Id  :-"+screenId);
		 * //System.out.println("Store Id  :-"+storeId);
		 * 
		 * Store store = GlobalDao.getStore(storeId);
		 * //System.out.println("Store :-"+store.getStoreId()+" "+store.getStoreName());
		 * Unit unit = GlobalDao.getUnit(unitId);
		 */
		// System.out.println("Unit :-"+unit.getUnitId()+" "+unit.getUnitName());

		String consumptionNo = GlobalService.getBillNoByGeneratedCode(storeId, request.getUnitId(), screenId);
		// System.out.println("Function Called ");
		// System.out.println("Consumption No : "+consumptionNo);

		NonBillableConsumption consumption = new NonBillableConsumption(consumptionNo, new Store(request.getStoreId()),
				new Status(request.getStatusId()), new Status(request.getApprovalStatusId()), request.getRemark());
		consumption.setRequestFromId(request.getRequestFromId()!=null ? request.getRequestFromId():null);
		//sessionService.setEntityDefaults(consumption);
		if(request.getId()!=null) {
			consumption.setId(request.getId());
		}
		consumption.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (BatchRequest batchRequest : request.getBatch()) {
			/*
			 * if (batchRequest.getUomTypeId()!=null && batchRequest.getUomUnitId()!=null) {
			 * NonBillableConsumptionDetail nonBillableConsumptionDetail=new
			 * NonBillableConsumptionDetail(new Batch(batchRequest.getBatchId()), new
			 * UomType(batchRequest.getUomTypeId()), new
			 * UomUnit(batchRequest.getUomUnitId()), batchRequest.getConsumedQuantity(), new
			 * Status(batchRequest.getStatusId()));
			 * sessionService.setEntityDefaults(consumption);
			 * consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail); }
			 * else { NonBillableConsumptionDetail nonBillableConsumptionDetail=new
			 * NonBillableConsumptionDetail(new Batch(batchRequest.getBatchId()),
			 * batchRequest.getConsumedQuantity(), new Status(batchRequest.getStatusId()));
			 * sessionService.setEntityDefaults(consumption);
			 * consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail); }
			 */

			/* new code start for clubbatch and consignmentlogic by minesh */
			List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batchRequest.getBatchId(),
					storeId,request.getUnitId());
			int issueQtyFromRequest = batchRequest.getConsumedQuantity();
			for (StoreStock storeStockObj : StoreStockList) {
				if (issueQtyFromRequest != 0) {
					if (batchRequest.getUomTypeId() != null && batchRequest.getUomUnitId() != null) {
						Long storeAvaliableQuantity = Util.ConvertQuantity(
								Long.valueOf(storeStockObj.getAvailableQty()),
								storeStockObj.getBatch().getBatchUomList(), new UomType(batchRequest.getUomTypeId()));
						Long batchId = storeStockObj.getBatch().getId();
						if (issueQtyFromRequest <= storeAvaliableQuantity) {
							NonBillableConsumptionDetail nonBillableConsumptionDetail = new NonBillableConsumptionDetail(
									new Batch(batchId), new UomType(batchRequest.getUomTypeId()),
									new UomUnit(batchRequest.getUomUnitId()), issueQtyFromRequest,
									new Status(batchRequest.getStatusId()));
							nonBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							//sessionService.setEntityDefaults(consumption);
							consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail);
							issueQtyFromRequest = 0;
						} else {
							int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
							NonBillableConsumptionDetail nonBillableConsumptionDetail = new NonBillableConsumptionDetail(
									new Batch(batchId), new UomType(batchRequest.getUomTypeId()),
									new UomUnit(batchRequest.getUomUnitId()), storeAvaliableQuantityINT,
									new Status(batchRequest.getStatusId()));
							nonBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							//sessionService.setEntityDefaults(consumption);
							consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail);
							issueQtyFromRequest = issueQtyFromRequest - storeAvaliableQuantityINT;
						}
					} else {// getUomTypeId() and getUomUnitId is null
						Long storeAvaliableQuantity = Util.ConvertQuantity(
								Long.valueOf(storeStockObj.getAvailableQty()),
								storeStockObj.getBatch().getBatchUomList(), new UomType(batchRequest.getUomTypeId()));
						Long batchId = storeStockObj.getBatch().getId();
						if (issueQtyFromRequest <= storeAvaliableQuantity) {
							NonBillableConsumptionDetail nonBillableConsumptionDetail = new NonBillableConsumptionDetail(
									new Batch(batchId), issueQtyFromRequest, new Status(batchRequest.getStatusId()));
							nonBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							//sessionService.setEntityDefaults(consumption);
							consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail);

							issueQtyFromRequest = 0;
						} else {
							int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
							NonBillableConsumptionDetail nonBillableConsumptionDetail = new NonBillableConsumptionDetail(
									new Batch(batchId), storeAvaliableQuantityINT,
									new Status(batchRequest.getStatusId()));
							nonBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
							//sessionService.setEntityDefaults(consumption);
							consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail);

							issueQtyFromRequest = issueQtyFromRequest - storeAvaliableQuantityINT;
						}
					}
				}
			}
			/* new code end for clubbatch and consignmentlogic by minesh */

		}
		if (request.getRequestFrom() != null) {

			if (request.getRequestFrom().equals("Lab") || request.getRequestFrom().equals("Radiology")
					|| request.getRequestFrom().equals("DepartmentConsumption")) {
				consumption.setRequestFrom(request.getRequestFrom());
			}
		}

		NonBillableConsumption nonBillableConsumption = ConsumptionDao.saveConsumption(consumption);

		CommonResponse response = new CommonResponse();
		response.setId(consumption.getId().longValue());
		response.setMessage("Department consumption saved successfully");

		if (request.getRequestFrom() != null
				&& (request.getRequestFrom().equals("Lab") || request.getRequestFrom().equals("Radiology") || request.getRequestFrom().equals("DepartmentConsumption"))
				&& request.getBatch().size() > 0) {
			ConsumptionRequest req = new ConsumptionRequest();
			req.setId(nonBillableConsumption.getId());
			List<Integer> consumptionIds = new ArrayList<>();
			consumptionIds.add(nonBillableConsumption.getId());
			req.setStatusId(com.param.common.constants.Status.Pending.getStatus());
			req.setApprovalStatusId(com.param.common.constants.Status.PendingApproval.getStatus());
			req.setConsumptionId(consumptionIds);
			req.setUserId(request.getUserId());
			this.submitConsumption(req);
			req.setStatusId(8);
			req.setApprovalStatusId(5);
			req.setUnitId(request.getUnitId());
			ConsumableAcceptanceService.approveConsumableAcceptance(req);
			response.setMessage("Department consumption saved successfully");

		}

		return response;
	}

	@Override
	@Transactional
	public Object getConsumptionList(ConsumptionRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ConsumptionDao.getConsumptionList(request, screenId);
	}

	@Override
	@Transactional
	public Object getConsumptionDetail(Integer id,Integer orgId, Integer unitId) {
		ConsumptionResponse response = ConsumptionDao.getConsumption(id);
		response.setBatch(ConsumptionDao.getConsumptionDetail(id));

		/*
		 * start code for resolved HIS-10494 While Updating consumption then batch
		 * number is not getting display in non billable consumption screen
		 */
		ItemsRequest itemsRequest = new ItemsRequest();
		itemsRequest.setAssetTypeId(1);
		itemsRequest.setStoreId(response.getStoreId());
		for (BatchResponse b : response.getBatch()) {
			itemsRequest.setItemCode(b.getItemCode());
			itemsRequest.setItemName(b.getItemName());
			itemsRequest.setIsThreeUOMApplicable(itemsRequest.getIsThreeUOMApplicable());
			List<BatchListResponse> batchList = ConsumptionDao
					.batchListStoreWiseForNonBillableConsumption(itemsRequest);
			// reference from batchListBillableConsumptionNew(ItemsServiceImpl.java L :
			// 1605)
			List<Long> batchIds = batchList.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
			List<UomResponse> uomListAll = new ArrayList<UomResponse>();
			if (!batchIds.isEmpty()) {
				uomListAll = GlobalDao.getBatchUomListBP(batchIds);
			}
			for (BatchListResponse batch : batchList) {
				/*
				 * start code for resolved HIS-10253 Store Stock Dashboard - Billable
				 * Consumption -
				 */
				BatchResponse1 updatedBatch = GlobalDao.getUpdatedBatchDetails(itemsRequest.getStoreId(),
						BigInteger.valueOf(batch.getBatchId()),unitId);
				if (updatedBatch != null) {
					batch.setMrp(updatedBatch.getMrp());
					batch.setBatchNo(updatedBatch.getBatchno());
					batch.setExpiry(updatedBatch.getExpiry());
				}
				if (itemsRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
					List<UomResponse> uomList = uomListAll.stream()
							.filter(x -> x.getBatchId().equals(batch.getBatchId())).collect(Collectors.toList());
					if (uomList != null && !uomList.isEmpty()) {
						batch.setUomResponses(uomList);
						UomResponse ipUom = Util.getIpUom(uomList);
						if (ipUom != null) {
							Integer conversion = Util.getIpUomConversion(uomList);
							batch.setUom(ipUom.getUomTypeId(), ipUom.getUomType(), ipUom.getUomUnitId(),
									ipUom.getUomUnit());
							batch.setConversion(conversion);
							batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
						}
					}
				}
				
				/*
				 * end code for resolved HIS-10253 Store Stock Dashboard - Billable Consumption
				 * -
				 */
			}
			// reference from batchListBillableConsumptionNew(ItemsServiceImpl.java L :
			// 1605)

			b.setBatches(batchList);
		}
		/*
		 * end code for resolved HIS-10494 While Updating consumption then batch number
		 * is not getting display in non billable consumption screen
		 */

		return response;
	}

	@Override
	@Transactional
	public Object updateConsumption(ConsumptionRequest request) {

		NonBillableConsumption consumption = ConsumptionDao.getNonBillableConsumption(request.getId());
		consumption.updateNonBillableConsumption(request.getRemark(),request.getUserId());

		for (NonBillableConsumptionDetail detail : consumption.getNonBillableConsumptionDetails()) {
			Integer id = null;

			for (BatchRequest batchRequest : request.getBatch()) {
				if (batchRequest.getId() != null && batchRequest.getId().equals(detail.getId())) {
					id = batchRequest.getId();
					// detail.setConsumedQty(batchRequest.getConsumedQuantity());

					/*
					 * start code for resolved HIS-10494 While Updating consumption then batch
					 * number is not getting display in non billable consumption screen
					 */
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
							batchRequest.getBatchId(), consumption.getStore().getStoreId(),request.getUnitId());
					int issueQtyFromRequest = batchRequest.getConsumedQuantity();
					for (StoreStock storeStockObj : StoreStockList) {
						Long batchId = storeStockObj.getBatch().getId();
						Long storeAvaliableQuantity = Util.ConvertQuantity(
								Long.valueOf(storeStockObj.getAvailableQty()),
								storeStockObj.getBatch().getBatchUomList(), new UomType(batchRequest.getUomTypeId()));
						if (batchRequest.getUomTypeId() != null && batchRequest.getUomUnitId() != null) {
							if (issueQtyFromRequest <= storeAvaliableQuantity) {
								detail.setBatch(new Batch(batchRequest.getBatchId()));
								detail.setUomType(new UomType(batchRequest.getUomTypeId()));
								detail.setUomUnit(new UomUnit(batchRequest.getUomUnitId()));
								detail.setConsumedQty(issueQtyFromRequest);
								detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
								//detail.setUpdatedBy(new User(request.getUserId()));
								detail.setUpdatedByUser(request.getUserId());
								break;
							} else {
								int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
								NonBillableConsumptionDetail nonBillableConsumptionDetail = new NonBillableConsumptionDetail(
										new Batch(batchId), new UomType(batchRequest.getUomTypeId()),
										new UomUnit(batchRequest.getUomUnitId()), storeAvaliableQuantityINT,
										new Status(batchRequest.getStatusId()));
								nonBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
								//sessionService.setEntityDefaults(consumption);
								consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail);
								issueQtyFromRequest = issueQtyFromRequest - storeAvaliableQuantityINT;
							}
						} else {// getUomTypeId() and getUomUnitId is null
							if (issueQtyFromRequest <= storeAvaliableQuantity) {
								detail.setBatch(new Batch(batchRequest.getBatchId()));
								detail.setConsumedQty(issueQtyFromRequest);
								detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
								detail.setUpdatedByUser(request.getUserId());
								//detail.setUpdatedBy(new User(request.getUserId()));
								break;
							} else {
								int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
								NonBillableConsumptionDetail nonBillableConsumptionDetail = new NonBillableConsumptionDetail(
										new Batch(batchId), storeAvaliableQuantityINT,
										new Status(batchRequest.getStatusId()));
								nonBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
								//sessionService.setEntityDefaults(consumption);
								consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail);
								issueQtyFromRequest = issueQtyFromRequest - storeAvaliableQuantityINT;
							}
						}
					}

					/*
					 * end code for resolved HIS-10494 While Updating consumption then batch number
					 * is not getting display in non billable consumption screen
					 */

					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
			//detail.setUpdatedBy(detail.getUser());
		}

		for (BatchRequest batchRequest : request.getBatch()) {
			if (batchRequest.getId() == null) {
				NonBillableConsumptionDetail nonBillableConsumptionDetail = new NonBillableConsumptionDetail(
						new Batch(batchRequest.getBatchId()), new UomType(batchRequest.getUomTypeId()),
						new UomUnit(batchRequest.getUomUnitId()), batchRequest.getConsumedQuantity(),
						new Status(batchRequest.getStatusId()));
				nonBillableConsumptionDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				//sessionService.setEntityDefaults(nonBillableConsumptionDetail);
				consumption.addNonBillableConsumptionDetail(nonBillableConsumptionDetail);
			}
		}

		ConsumptionDao.updateConsumption(consumption);

		CommonResponse response = new CommonResponse();
		response.setId(consumption.getId().longValue());
		response.setMessage("Department consumption updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object submitConsumption(ConsumptionRequest request) {
		for (Integer id : request.getConsumptionId()) {
			NonBillableConsumption consumption = ConsumptionDao.getNonBillableConsumption(id);

			consumption.setStatus(new Status(request.getStatusId()));
			consumption.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (NonBillableConsumptionDetail detail : consumption.getNonBillableConsumptionDetails()) {
				if (!detail.getIsDeleted()) {
					detail.setStatus(new Status(com.param.common.constants.Status.PendingApproval.getStatus()));
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
					detail.setUpdatedByUser(request.getUserId());
					//detail.setUpdatedBy(detail.getUser());
				}
			}

			consumption.setApprovedOn(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
			consumption.setApprovedBy(new User(request.getUserId()));

			consumption.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
			consumption.setUpdatedByUser(request.getUserId());
			//consumption.setUpdatedBy(consumption.getUser());

			ConsumptionDao.updateConsumption(consumption);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Department consumption approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelConsumption(ConsumptionRequest request) {

		NonBillableConsumption consumption = ConsumptionDao.getNonBillableConsumption(request.getId());

		for (NonBillableConsumptionDetail detail : consumption.getNonBillableConsumptionDetails()) {
			detail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
			//detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		consumption.setCancelReason(new CancelReason(request.getCancelReasonId()));
		consumption.setCancelNote(request.getCancelNote());

		consumption.setStatus(new Status(request.getStatusId()));

		consumption.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
		consumption.setUpdatedByUser(request.getUserId());
		//consumption.setUpdatedBy(consumption.getUser());

		ConsumptionDao.updateConsumption(consumption);

		CommonResponse response = new CommonResponse();
		response.setMessage("Department consumption cancelled successfully");

		return response;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Response getConsumptionDetailsByTemplateTransactionId(ConsumptionRequest request) throws ApplicationException {
		return ConsumptionDao.getConsumptionDetailsByTemplateTransactionId(request);
	}
}
