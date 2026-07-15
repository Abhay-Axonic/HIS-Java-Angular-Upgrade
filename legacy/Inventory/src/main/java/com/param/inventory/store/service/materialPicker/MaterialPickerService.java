package com.param.inventory.store.service.materialPicker;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
import com.param.entity.model.inventory.MaterialPicker;
import com.param.entity.model.inventory.MaterialPickerDetail;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.PickerResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.materialIssue.MaterialIssuePendingDao;
import com.param.inventory.store.dao.materialPicker.IMaterialPickerDao;
import com.param.inventory.store.dto.materialIssuePending.IssueBatch;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.pharmacy.master.global.dao.unitOfMeasurement.UnitOfMeasurementDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("MaterialPickerService")
public class MaterialPickerService implements IMaterialPickerService {

	private Integer screenId = Screen.MaterialPicker.getScreen();

	private Integer issueScreenId = Screen.MaterialIssuePending.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IMaterialPickerDao MaterialPickerDao;

	@Autowired
	UnitOfMeasurementDao unitOfMeasurementDao;

	@Autowired
	IGlobalService GlobalService;
	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;

	@Autowired
	MaterialIssuePendingDao materialIssuePendingDao;
	
	@Override
	@Transactional
	public StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		StoreIndentDropdownResponse response = new StoreIndentDropdownResponse();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
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

		return response;
	}

	@Override
	@Transactional
	public Object getMaterialPickerList(MaterialPickerRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return MaterialPickerDao.searchMaterialPickerList(request, screenId);
	}

	@Override
	@Transactional
	public Object getMaterialPicker(Integer id,Integer unitId, Integer orgId) {
		MaterialPickerResponse response = MaterialPickerDao.getMaterialPicker(id);
		//MaterialPickerResponse resData=MaterialPickerDao.getPickerData(0);
		MaterialIssueDetail mateerialIssueData =materialIssuePendingDao.getMaterialPickerData(response.getId());
		Integer pickerId=0;
		String pickerData="";
		if(mateerialIssueData!=null)
		{
			pickerId=mateerialIssueData.getMaterialIssue().getPicker().getUserId();
			pickerData=mateerialIssueData.getMaterialIssue().getPicker().getUserName();
		}
		response.setPickerId(pickerId);
		response.setPickerName(pickerData);		
		
		for (MaterialPickerDetailResponse pickerDetails : MaterialPickerDao.getMaterialPickerDetails(id)) {
			// Long leaseQuantity = GlobalDao.getStoreStockByItem(response.getToStoreId(),
			// pickerDetails.getItemId());
			// Long quantity = Util.ConvertQuantityByUom(leaseQuantity,
			// unitOfMeasurementDao.getAllByItemId(pickerDetails.getItemId()), new
			// UomType(pickerDetails.getUomTypeId()));
			Long storeStock = GlobalDao.getStoreStockByItemForAssignPicker(response.getToStoreId(),
					pickerDetails.getItemId());

			pickerDetails.setStockAvailable(getAvailableStock(pickerDetails.getItemId(), response.getToStoreId(),
					pickerDetails.getUomTypeId(),orgId,unitId));
			// As discussed temp commented
		//	pickerDetails.setPendingQuantity(getPendingIndentQuantity(pickerDetails.getItemId(),pickerDetails.getPendingQuantity(), pickerDetails.getUomTypeId()));
			
			pickerDetails.setPendingQuantity(pickerDetails.getPendingQuantity());
			BatchProperty batchProperty = new BatchProperty();
			batchProperty.setIsIndent(false);
			List<Batch> batchList = GlobalDao.getBatchDetails(pickerDetails.getItemId(), unitId, response.getToStoreId(),
					batchProperty);

			// StoreStock
			// List<Batch> batchList1 =
			// GlobalDao.getConsignmentBatchDetails(pickerDetails.getBatchNo(),
			// pickerDetails.getBatchId(),response.getToStoreId(), batchProperty);

//			List<Batch> batchList = GlobalDao.getBatchDetails(pickerDetails.getItemId(), response.getToStoreId());
			if (!batchList.isEmpty()) {
				for (Batch batch : batchList) {
					// BigInteger availableQty=

					pickerDetails.addBatches(
							PopulateBatchWithStock(batch, pickerDetails.getItemId(), response.getToStoreId()));

					// pickerDetails.addBatches(PopulateBatchWithStock(batch));
					// changed by minesh as per discuss with Divyeshsir
					// commented because of conflict some one change old code(not an issue).
					// pickerDetails.addBatches(PopulateBatchWithStoreStock(batch,response.getToStoreId()));

				}
			}
			for (MaterialIssueBatchMapper batchMapper : MaterialPickerDao
					.getMaterialPickerBatchList(pickerDetails.getId())) {
				IssueBatch issueBatch=	new IssueBatch(batchMapper.getId(), batchMapper.getBatch().getId(),
						batchMapper.getUomType().getId(), batchMapper.getUomUnit().getId(),
						batchMapper.getIssueQuantity());
						issueBatch.setBatchNo(batchMapper.getBatch().getBatchNo());
						issueBatch.setExpiry(batchMapper.getBatch().getExpiryDate());
						issueBatch.setPickerQty(batchMapper.getIssueQuantity() !=null ? batchMapper.getIssueQuantity() :0);
						issueBatch.setUom(Util.PopulateUom(batchMapper.getBatch()));
						Integer availableQty = 0;

						BigInteger availableQty1 = null;
						if (batchMapper.getBatch().getLotNo() != null && batchMapper.getBatch().getBatchNo() != null) {
							availableQty1 = GlobalDao.getClubBatchWiseAvailableStock(batchMapper.getBatch().getBatchNo(), batchMapper.getBatch().getLotNo(),
									batchMapper.getBatch().getIsConsignment(), pickerDetails.getItemId(), response.getToStoreId() , sessionService.getUnitId());
							// availableQty=consignmentStockConversionDao.getAvailableStock(batch.getBatchNo(),batch.getLotNo(),batch.getIsConsignment(),storeId,itemId);

						} else if (batchMapper.getBatch().getBatchNo() == null && batchMapper.getBatch().getLotNo() != null) {
							availableQty1 = GlobalDao.getClubBatchWiseAvailableStockLotNo(batchMapper.getBatch().getLotNo(), batchMapper.getBatch().getIsConsignment(), pickerDetails.getItemId(), response.getToStoreId());
						} else {
							availableQty1 = BigInteger.valueOf(batchMapper.getBatch().getStoreStockList().get(0).getAvailableQty());
						}
						issueBatch.setAvailableStockQuantity(availableQty1.intValue());
				pickerDetails.addBatch(issueBatch);
			}
			response.addItems(pickerDetails);
		}
		response.setPickers(PopulatePickers(unitId));

		return response;
	}

	public BatchResponse PopulateBatchWithStock(Batch batch, Integer itemId, Integer storeId) {

		Integer availableQty = 0;

		/*
		 * for (StoreStock storeStock : batch.getStoreStockList()) { if
		 * (storeStock.getIsConsignment() == false) { availableQty = availableQty +
		 * storeStock.getAvailableQty(); } else { //availableQty = availableQty +
		 * storeStock.getAvailableQty(); }
		 * 
		 * }
		 */
		BigInteger availableQty1 = null;
		if (batch.getLotNo() != null && batch.getBatchNo() != null) {
			// availableQty1 = GlobalDao.getConsignmentBatchWiseQty(batch.getBatchNo(),
			// batch.getLotNo(),batch.getIsConsignment());
			availableQty1 = GlobalDao.getClubBatchWiseAvailableStock(batch.getBatchNo(), batch.getLotNo(),
					batch.getIsConsignment(), itemId, storeId , sessionService.getUnitId());
			// availableQty=consignmentStockConversionDao.getAvailableStock(batch.getBatchNo(),batch.getLotNo(),batch.getIsConsignment(),storeId,itemId);

		} else if (batch.getBatchNo() == null && batch.getLotNo() != null) {
			availableQty1 = GlobalDao.getClubBatchWiseAvailableStockLotNo(batch.getLotNo(), batch.getIsConsignment(), itemId, storeId);
		} else {
			availableQty1 = BigInteger.valueOf(batch.getStoreStockList().get(0).getAvailableQty());
		}
		BatchResponse batchData=new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				availableQty1.intValue(), batch.getMarkup(), batch.getRate(), Util.PopulateUom(batch));
		return batchData;

	}

	// added by minesh as per discusses with Divyeshsir
	public BatchResponse PopulateBatchWithStoreStock(Batch batch, int storeId) {
		Integer availableQty = 0;

		BigInteger availableQty1 = null;
		if (batch.getLotNo() != null) {
			availableQty1 = GlobalDao.getConsignmentBatchWiseStoreQty(batch.getBatchNo(), batch.getLotNo(),
					batch.getIsConsignment(), storeId);

		} else {
			availableQty1 = BigInteger.valueOf(batch.getStoreStockList().get(0).getAvailableQty());
		}

		return new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(), batch.getSellingPrice(),
				availableQty1.intValue(), batch.getMarkup(), batch.getRate(), Util.PopulateUom(batch));
	}

	@Override
	@Transactional
	public Object saveMaterialPicker(MaterialPickerRequest request) {
		MaterialPicker materialPicker = MaterialPickerDao.fetchMaterialPicker(request.getId());
		materialPicker.setRemark(request.getRemark());
		materialPicker.setStatus(new Status(request.getStatusId()));

		/*Integer storeId = request.getFromStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String issueNumber = GlobalService.getBillNoByGeneratedCode(request.getFromStoreId(), request.getUnitId()!=null?request.getUnitId():sessionService.getUnitId(), issueScreenId);
		MaterialIssue maIssue =MaterialPickerDao.getMaterialIssue(materialPicker.getId());
		if(maIssue!=null)
		{
			maIssue.setIsActive(false);
			maIssue.setIsDeleted(true);
			
			
			for( MaterialIssueDetail detail :maIssue.getMaterialIssueDetails())
			{
				detail.setIsActive(false);
				detail.setIsDeleted(true);
				
				for(MaterialIssueBatchMapper bmapper :detail.getMaterialIssueBatchMappers() )
				{
					bmapper.setIsActive(false);
					bmapper.setIsDeleted(true);
				}
				
			}
			
			MaterialPickerDao.updateMaterialIssue(maIssue);
		}
		
		
		MaterialIssue materialIssue = new MaterialIssue(new User(request.getPickerId()), issueNumber,
				new Status(com.param.common.constants.Status.Pending.getStatus()), materialPicker.getStoreIndent(),
				materialPicker, new Store(materialPicker.getStoreIndent().getFromStore().getStoreId()),
				new Store(materialPicker.getStoreIndent().getToStore().getStoreId()));
		//sessionService.setEntityDefaults(materialIssue);
		materialIssue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (MaterialPickerDetail detail : MaterialPickerDao.fetchMaterialPickerDetail(request.getId())) {

			for (MaterialPickerDetailRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					detail.setPickerQuantity(item.getPickerQuantity() != null ? 0 + item.getPickerQuantity()
							: detail.getPickerQuantity());
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			//		detail.setUpdatedBy(detail.getUser());
					detail.setUpdatedByUser(request.getUserId());

					MaterialIssueDetail issueDetail = new MaterialIssueDetail(detail.getItem(),
							detail.getStoreIndentDetail(), detail, 0);
				//	sessionService.setEntityDefaults(issueDetail);
					issueDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());

					for (BatchResponse batch : item.getBatches()) {

						MaterialIssueBatchMapper materialIssueBatchMapper = new MaterialIssueBatchMapper(
								new Batch(batch.getBatchId()), batch.getPickerQuantity(),
								new UomType(batch.getUomTypeId()), new UomUnit(batch.getUomUnitId()),
								new Status(com.param.common.constants.Status.Pending.getStatus()));
					//	sessionService.setEntityDefaults(materialIssueBatchMapper);
						materialIssueBatchMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
						issueDetail.addBatch(materialIssueBatchMapper);

						/* Start added code by Minesh For clubingbatch and consignment */

						// re-code commented again because of logic change.
						/*
						 * // Batch batchForBatchNo = GlobalDao.getBatch(request.getFromStoreId(),
						 * batch.getBatchId()); Batch batchForBatchNo =
						 * GlobalDao.getBatch(request.getToStoreId(), batch.getBatchId()); // Recode
						 * change base on fronted. // List<Batch> batchList =
						 * GlobalDao.getSameBatchListFromStoreSQL(batchForBatchNo.getBatchNo(),request.
						 * getFromStoreId()); List<Batch> batchList =
						 * GlobalDao.getSameBatchListFromStoreSQL(batchForBatchNo.getBatchNo(),
						 * request.getToStoreId());// Recode change base on s. List<Long> batchIds =
						 * batchList.stream().map(x -> x.getId()).collect(Collectors.toList()); //
						 * List<StoreStock> StoreStockList = //
						 * GlobalDao.getStoreStockListByBatchAndStore(batchIds,request.getFromStoreId())
						 * ; List<StoreStock> StoreStockList =
						 * GlobalDao.getStoreStockListByBatchAndStore(batchIds,
						 * request.getToStoreId());// Recode change base on fronted.
						 * 
						 * int issuingQty = batch.getPickerQuantity(); for (StoreStock storeStockObj :
						 * StoreStockList) { Long issueQuantity =
						 * Util.ConvertQuantity(Long.valueOf(storeStockObj.getAvailableQty()),
						 * storeStockObj.getBatch().getBatchUomList(), new
						 * UomType(batch.getUomTypeId())); Long batchId =
						 * storeStockObj.getBatch().getId(); if (issuingQty != 0) { if (issueQuantity >=
						 * issuingQty) { if (batch != null) { MaterialIssueBatchMapper
						 * materialIssueBatchMapper = new MaterialIssueBatchMapper( new Batch(batchId),
						 * issuingQty, new UomType(batch.getUomTypeId()), new
						 * UomUnit(batch.getUomUnitId()), new
						 * Status(com.param.common.constants.Status.Pending.getStatus()));
						 * sessionService.setEntityDefaults(materialIssueBatchMapper);
						 * issueDetail.addBatch(materialIssueBatchMapper); } issuingQty = 0; } else {
						 * int storeAvaliableQuantity = issueQuantity.intValue(); if (batch != null) {
						 * MaterialIssueBatchMapper materialIssueBatchMapper = new
						 * MaterialIssueBatchMapper( new Batch(batchId), storeAvaliableQuantity, new
						 * UomType(batch.getUomTypeId()), new UomUnit(batch.getUomUnitId()), new
						 * Status(com.param.common.constants.Status.Pending.getStatus()));
						 * sessionService.setEntityDefaults(materialIssueBatchMapper);
						 * issueDetail.addBatch(materialIssueBatchMapper); } issuingQty = issuingQty -
						 * storeAvaliableQuantity; } } } //End added code by Minesh For clubingbatch and
						 * consignment
						 */
					}

					materialIssue.addMaterialIssueDetail(issueDetail);

					break;
				}
			}
		}
		MaterialPickerDao.saveMaterialIssue(materialIssue);

		CommonResponse response = new CommonResponse();
		response.setId(materialIssue.getId().longValue());
		response.setMessage("Material Picker saved successfully");

		return response;
	}

	private List<PickerResponse> PopulatePickers(Integer unitId) {
		List<PickerResponse> pickerResponseList = new ArrayList<PickerResponse>();

		for (User user : GlobalDao.getPickerUsers(unitId)) {
			PickerResponse pickerResponse = new PickerResponse(user.getUserId(), user.getUserName());
			pickerResponseList.add(pickerResponse);
		}

		return pickerResponseList;
	}

	Long getAvailableStock(Integer itemId, Integer storeId, Integer uomTypeId,Integer orgId,Integer unitId) {
		//System.out.println("Item Id : " + itemId);
		//System.out.println("Store Id : " + storeId);
		//System.out.println("UOM Type Id : " + uomTypeId);

		Integer dispenseConversion = GlobalDao.getDispenseConversionByItem(itemId,orgId);
		//System.out.println("Dispense Conversion : " + dispenseConversion);

		Integer stockConversion = GlobalDao.getStockConversionByItem(itemId,orgId);
		//System.out.println("Stock Conversion : " + stockConversion);

		Integer purchaseConversion = GlobalDao.getPurchaseConversionByItem(itemId,orgId);
		//System.out.println("Purchase Conversion : " + purchaseConversion);

		Long originalStockAvailable = GlobalDao.getStoreStockByItem(storeId, itemId,unitId);
		//System.out.println("Original Qty : " + originalStockAvailable);

		Long dispenseConversionQty = originalStockAvailable / dispenseConversion;
		//System.out.println("Dispense Conversion Qty : " + dispenseConversionQty);

		Long stockConversionQty = dispenseConversionQty / stockConversion;
		//System.out.println("Stock Conversion Qty : " + stockConversionQty);

		Long purchaseConversionQty = stockConversionQty / purchaseConversion;
		//System.out.println("Purchase Conversion Qty : " + purchaseConversionQty);

		if (uomTypeId == 1)
			return purchaseConversionQty;
		if (uomTypeId == 2)
			return stockConversionQty;
		if (uomTypeId == 3)
			return dispenseConversionQty;
		return dispenseConversionQty;

	}

	/*Integer getPendingIndentQuantity(Integer itemId, Integer qty, Integer uomTypeId) {
		//System.out.println("Item Id : " + itemId);
		//System.out.println("Qty Id : " + qty);
		//System.out.println("UOM Type Id : " + uomTypeId);

		Integer dispenseConversion = GlobalDao.getDispenseConversionByItem(itemId);
		//System.out.println("Dispense Conversion : " + dispenseConversion);

		Integer stockConversion = GlobalDao.getStockConversionByItem(itemId);
		//System.out.println("Stock Conversion : " + stockConversion);

		Integer purchaseConversion = GlobalDao.getPurchaseConversionByItem(itemId);
		//System.out.println("Purchase Conversion : " + purchaseConversion);

		Integer originalQty = qty;
		//System.out.println("Original Qty : " + originalQty);

		Integer dispenseConversionQty = originalQty / dispenseConversion;
		//System.out.println("Dispense Conversion Qty : " + dispenseConversionQty);

		Integer stockConversionQty = dispenseConversionQty / stockConversion;
		//System.out.println("Stock Conversion Qty : " + stockConversionQty);

		Integer purchaseConversionQty = stockConversionQty / purchaseConversion;
		//System.out.println("Purchase Conversion Qty : " + purchaseConversionQty);

		if (uomTypeId == 1)
			return purchaseConversionQty;
		if (uomTypeId == 2)
			return stockConversionQty;
		if (uomTypeId == 3)
			return dispenseConversionQty;
		return dispenseConversionQty;

	}*/
}
