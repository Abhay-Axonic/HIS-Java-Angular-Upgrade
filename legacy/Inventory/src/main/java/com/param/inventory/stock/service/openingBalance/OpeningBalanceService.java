package com.param.inventory.stock.service.openingBalance;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.OpbTaxComponent;
import com.param.entity.model.inventory.OpbUomMapper;
import com.param.entity.model.inventory.OpeningBalShelfItem;
import com.param.entity.model.inventory.OpeningBalance;
import com.param.entity.model.inventory.OpeningBalanceDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Rack;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.stock.dao.openingBalance.IOpeningBalanceDao;
import com.param.inventory.stock.dto.openingBalance.ItemRequest;
import com.param.inventory.stock.dto.openingBalance.ItemResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceDetailsResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceItem;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;
import com.param.inventory.stock.dto.openingBalance.RackResponse;
import com.param.inventory.stock.dto.openingBalance.ShelfResponse;
import com.param.inventory.stock.dto.openingBalance.StorageDetails;
import com.param.inventory.stock.dto.openingBalance.StorageLocationDetails;
import com.param.pharmacy.master.unit.dao.rack.IRackDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class OpeningBalanceService implements IOpeningBalanceService {

	private Integer screenId = Screen.OpeningBalance.getScreen();

	private Integer lotNoScreenId = Screen.LotNoCreation.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IOpeningBalanceDao OpeningBalanceDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IRackDao rackDao;
	@Autowired
	IItemsDao ItemsDao;

	@Override
	@Transactional
	public Object getOpeningBalanceList(OpeningBalanceRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		/*
		 * List<OpeningBalanceResponse> response = new
		 * ArrayList<OpeningBalanceResponse>();
		 * 
		 * for (OpeningBalance openingBalance :
		 * OpeningBalanceDao.searchOpeningBalance(request)) { OpeningBalanceResponse
		 * result = new OpeningBalanceResponse(openingBalance.getId(),
		 * openingBalance.getAddedDate(), openingBalance.getOpbNo(),
		 * openingBalance.getStore().getStoreName(),
		 * openingBalance.getAddedBy().getUserName(),
		 * openingBalance.getStatus().getId(), openingBalance.getStatus().getStatus());
		 * 
		 * response.add(result); }
		 * 
		 * return response;
		 */

		return OpeningBalanceDao.searchOpeningBalanceBetterPerformance(request);
	}

	@Override
	@Transactional
	public Object saveOpeningBalance(OpeningBalanceRequest request) {

		/*
		 * Integer storeId = request.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String opbNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);

		OpeningBalance openingBalance = new OpeningBalance(opbNumber, new Status(request.getStatusId()),
				new Store(request.getStoreId()));

		OpeningBalanceDetail detail = null;
		for (ItemRequest item : request.getItems()) {

			/*
			 * detail = new OpeningBalanceDetail(item.getCopAmount(), item.getBatchNo(),
			 * item.getCop(), item.getExpiryDate(), new Item(item.getItemId()),
			 * item.getMarkup(), item.getMrp(), item.getQuantity(), item.getRemark(), new
			 * Status(item.getStatusId()), item.getTaxAmount(), new Tax(item.getTaxId()),
			 * item.getTaxPercent(), new UomType(item.getUomTypeId()), new
			 * UomUnit(item.getUomUnitId()), new UomType(item.getMrpUomTypeId()));
			 */

			String batchNumber = null;
			Date expiryDate = null;
			boolean isBatchFlag = true;
			if (item.getBatchNo() != null && item.getIsBatchReq()) {
				batchNumber = item.getBatchNo();
				expiryDate = item.getExpiryDate();
			} else if (!item.getIsBatchReq()) // as discussed with vishal , mama , We can alse add batch if batch
												// required false
			{
				// batchNumber="NA";
				batchNumber = item.getBatchNo() != null ? item.getBatchNo() : null;
				expiryDate = item.getExpiryDate() != null ? item.getExpiryDate() : null;
				isBatchFlag = false;

			}

			else if ((item.getBatchNo() == null || item.getBatchNo().equalsIgnoreCase("NA")) && !item.getIsBatchReq()) {
				// batchNumber="NA";
				isBatchFlag = false;

			}

			// Double mrpLeaseAmount = Util.GetMrpLeaseQuantity(item.getMrp(),
			// detail.getGrnUomMappersList(), new UomType(item.getMrpUomTypeId()));

			OpeningBalanceDetail openingBalanceDetail = new OpeningBalanceDetail(item.getCopAmount(), batchNumber,
					item.getCop(), expiryDate, new Item(item.getItemId()), item.getMarkup(), item.getMrp(),
					item.getQuantity(), item.getRemark(), new Status(item.getStatusId()), item.getTaxAmount(),
					new Tax(item.getTaxId()), item.getTaxPercent(),
					item.getUomTypeId() != null ? new UomType(item.getUomTypeId()) : null,
					item.getUomUnitId() != null ? new UomUnit(item.getUomUnitId()) : null,
					item.getMrpUomTypeId() != null ? new UomType(item.getMrpUomTypeId()) : null, item.getLcop());
			// sessionService.setEntityDefaults(openingBalanceDetail);
			openingBalanceDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
					request.getUserId());

			String lotNo = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
					lotNoScreenId);

			if (!item.getIsBatchReq())
				openingBalanceDetail.setLotNumber(lotNo);

			detail = openingBalance.addOpeningBalanceDetail(openingBalanceDetail);

			for (TaxDetail taxDetail : GlobalDao.getTaxDetail(item.getTaxId())) {
				Integer taxCompId = taxDetail.getTaxComponent().getId();
				Double taxPercentage = taxDetail.getTaxPercentage();
				Double amt = item.getCopAmount();

				Double calTaxAmt = (amt * taxPercentage) / 100;
				OpbTaxComponent opbTaxComponent = new OpbTaxComponent(calTaxAmt,
						new OpeningBalanceDetail(openingBalance.getOpeningBalanceDetailList().getFirst().getId()),
						taxPercentage, new TaxComponent(taxCompId));
				// sessionService.setEntityDefaults(opbTaxComponent);
				opbTaxComponent.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				detail.addOpbTaxCom(opbTaxComponent);
			}

			for (UomResponse uom : item.getUom()) {
				OpbUomMapper opbUomMapper = new OpbUomMapper(uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
						uom.getStoreUom(), new UomType(uom.getUomTypeId()), new UomUnit(uom.getUomUnitId()));
				// sessionService.setEntityDefaults(opbUomMapper);
				opbUomMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				detail.addUom(opbUomMapper);
			}

			for (StorageDetails storageDetails : item.getStorageDetails()) {
				// Long batchId;
				/*
				 * ShelfItem shelfItem = new ShelfItem(new Shelf(storageDetails.getShelfId()),
				 * new Batch(item.getBatchId()), new Store(request.getStoreId()), new
				 * Rack(storageDetails.getRackId()), storageDetails.getRemark());
				 * sessionService.setEntityDefaults(shelfItem);
				 */

				OpeningBalShelfItem shelfItem = new OpeningBalShelfItem(new Shelf(storageDetails.getShelfId()),
						new Store(request.getStoreId()), new Rack(storageDetails.getRackId()),
						storageDetails.getRemark(), new OpeningBalanceDetail(openingBalanceDetail.getId()));
				// sessionService.setEntityDefaults(shelfItem);
				shelfItem.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				detail.addOpeningBalanceShelfItemList(shelfItem);
				// OpeningBalanceDao.saveOpeningBalShelfItem(shelfItem);
			}

//			openingBalance.addOpeningBalanceDetail(detail);
		}
		// sessionService.setEntityDefaults(openingBalance);
		openingBalance.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		OpeningBalanceDao.saveOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setId(openingBalance.getId().longValue());
		response.setMessage("Opening balance saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getOpeningBalance(Integer id) {
		OpeningBalance openingBalance = OpeningBalanceDao.getOpeningBalace(id);

		OpeningBalanceDetailsResponse response = new OpeningBalanceDetailsResponse(openingBalance.getId(),
				openingBalance.getAddedDate(), openingBalance.getOpbNo(), openingBalance.getStore().getStoreId(),
				openingBalance.getStore().getStoreName(), openingBalance.getAddedBy().getUserName(),
				openingBalance.getStatus().getStatus(), openingBalance.getStatus().getId());

		for (OpeningBalanceDetail detail : OpeningBalanceDao.getOpeningbalaceDetail(id)) {
			/*
			 * Long storeStock =
			 * GlobalDao.getStoreStockByItem(openingBalance.getStore().getStoreId(),
			 * detail.getItem().getId()); Long stockInTransit =
			 * GlobalDao.getstockInTransitByItem(openingBalance.getStore().getStoreId(),
			 * detail.getItem().getId()); Integer maxStoreQuantity =
			 * GlobalDao.getStoreMaxQuantityByItem(openingBalance.getStore().getStoreId(),
			 * detail.getItem().getId());
			 * 
			 * Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			 * 
			 * Long maxStoreQty = Util.GetMaxStoreConvertedQty(maxOrderQuantity,
			 * detail.getUomList(), detail.getUomType());
			 */

			List<RackResponse> racks = new ArrayList<RackResponse>();
			for (Rack rack : openingBalance.getStore().getRack()) {
				List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>();
				for (Shelf shelf : rack.getShelfList()) {
					List<OpeningBalanceItem> itemList = new ArrayList<OpeningBalanceItem>();
					for (ShelfItem shelfItem : shelf.getShelfItem()) {
						// System.out.println(shelfItem.getBatch().getId());
						OpeningBalanceItem itemObject = new OpeningBalanceItem(shelfItem.getBatch().getItem().getCode(),
								shelfItem.getBatch().getItem().getName(), shelfItem.getBatch().getBatchNo());
						itemList.add(itemObject);
					}
					ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
					shelfs.add(shelfObject);
				}
				RackResponse rackObject = new RackResponse(rack.getId(), rack.getName(), shelfs);
				racks.add(rackObject);
			}

			List<StorageLocationDetails> storageLocationDetails = OpeningBalanceDao.getStogareLocation(detail.getId());
			List<StorageDetails> storageDetails = new ArrayList<>();

			for (StorageLocationDetails s : storageLocationDetails) {
				StorageDetails sl = new StorageDetails();
				sl.setShelfItemId(s.getShelfitemid());
				sl.setRackId(s.getRackid());
				sl.setRackName(s.getRackname());
				sl.setRemark(s.getRemark());
				sl.setShelfId(s.getShelfid());
				sl.setShelfName(s.getShelfname());
				storageDetails.add(sl);
			}

			response.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(),
					detail.getItem().getName(), detail.getUomType() != null ? detail.getUomType().getId() : null,
					detail.getUomType() != null ? detail.getUomType().getType() : null,
					detail.getUomUnit() != null ? detail.getUomUnit().getId() : null,
					detail.getUomUnit() != null ? detail.getUomUnit().getUnits() : null,
					detail.getMrpUomType() != null ? detail.getMrpUomType().getId() : null,
					PopulateUom(OpeningBalanceDao.getOpbUomMapperList(detail.getId())), detail.getBatchNo(),
					detail.getExpiryDate(), detail.getQuantity(), detail.getCop(), detail.getMrp(),
					detail.getAmountCop(), detail.getMarkup(), detail.getTax() != null ? detail.getTax().getId() : null,
					detail.getTax() != null ? detail.getTax().getName() : null, detail.getTaxPercentage(),
					detail.getTaxAmount(), detail.getRemark(), detail.getStatus().getId(),
					getTaxList(openingBalance.getOrganization().getOrganizationId()), racks, detail.getLcop(),
					storageDetails, detail.getItem().getIsBatchRequired(), detail.getLotNumber()));

		}
		return response;
	}

	@Override
	@Transactional
	public Object getOpeningBalancePT(Integer id, Integer unitId, Integer orgId) {
		OpeningBalanceDetailsResponse response = OpeningBalanceDao.getOpeningBalacePT(id);

		List<RackResponse> racks = getRack(response.getStoreId());
		List<ItemResponse> items = OpeningBalanceDao.getOpeningbalaceDetailPT(id);
		List<StorageDetails> storageDetails = OpeningBalanceDao
				.getStogareLocationPT(items.stream().map(ItemResponse::getId).collect(Collectors.toList()));

		for (StorageDetails s : storageDetails) {
			s.setId(s.getShelfItemId());

		}

		List<UomResponse> uomResponses = OpeningBalanceDao
				.getOpbUomMapperListPT(items.stream().map(ItemResponse::getId).collect(Collectors.toList()));
		List<TaxDropDown> taxDropDowns = getTaxList(orgId);

		for (ItemResponse item : items) {
			item.setRacks(racks);
			item.setStorageDetails(storageDetails.stream().filter(x -> x.getOpeningBalDetId().equals(item.getId()))
					.collect(Collectors.toList()));
			item.setUom(uomResponses.stream().filter(x -> x.getOpbDtlId().equals(item.getId()))
					.collect(Collectors.toList()));
			item.setTaxList(taxDropDowns);
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(response.getStoreId(), item.getItemId(),
					orgId);
			Long storeStock = ItemsDao.getStockAvailable(response.getStoreId(), item.getItemId(), unitId);
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(response.getStoreId(), item.getItemId());
			Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(response.getStoreId(), item.getItemId(),
					unitId);
			Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(response.getStoreId(), item.getItemId(),
					unitId);
			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
					approvedPRAndPOQuantity, approvedPOQuantity);
			// System.out.println("maxOrderQuantity:-" + maxOrderQuantity);

			item.setMaxOrderQuantity(maxOrderQuantity);
			item.setStockAvailable(storeStock);
		}
		response.setItems(items);
		return response;
	}

	private List<RackResponse> getRack(Integer storeId) {
		List<RackResponse> racks = new ArrayList<RackResponse>();
		List<Rack> rackList = rackDao.getRackByStoreId(storeId);
		for (Rack rack : rackList) {
			List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>();
			for (Shelf shelf : rack.getShelfList()) {
				List<OpeningBalanceItem> itemList = new ArrayList<OpeningBalanceItem>();
				for (ShelfItem shelfItem : shelf.getShelfItem()) {
					OpeningBalanceItem itemObject = new OpeningBalanceItem(shelfItem.getBatch().getItem().getCode(),
							shelfItem.getBatch().getItem().getName(), shelfItem.getBatch().getBatchNo());
					itemList.add(itemObject);
				}
				ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
				shelfs.add(shelfObject);
			}
			RackResponse rackObject = new RackResponse(rack.getId(), rack.getName(), shelfs);
			racks.add(rackObject);
		}
		return racks;
	}

	private List<TaxDropDown> getTaxList(Integer orgId) {
		List<TaxDropDown> taxList = new ArrayList<TaxDropDown>();
		for (Tax tax : GlobalDao.getTaxList(orgId)) {
			TaxDropDown taxDto = new TaxDropDown(tax.getId(), tax.getName(), tax.getTaxPercentage());
			taxList.add(taxDto);
		}
		return taxList;
	}

	private List<UomResponse> PopulateUom(List<OpbUomMapper> uomMapper) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (OpbUomMapper uom : uomMapper) {
			response.add(new UomResponse(uom.getId(), uom.getUomType().getType(), uom.getUomType().getId(),
					uom.getUomUnit().getId(), uom.getUomUnit().getUnits(), uom.getConversions(), uom.getIpUom(),
					uom.getOpUom(), uom.getStoreUom()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object updateOpeningBalance(OpeningBalanceRequest request) {
		OpeningBalance openingBalance = OpeningBalanceDao.getOpeningBalace(request.getId());

		for (OpeningBalanceDetail detail : openingBalance.getOpeningBalanceDetailList()) {
			Integer id = null;

			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateOpeningBalaceDetails(
							item.getBatchNo() != null && !item.getBatchNo().isEmpty() ? item.getBatchNo() : null,
							item.getExpiryDate(), item.getUomTypeId() != null ? new UomType(item.getUomTypeId()) : null,
							item.getUomUnitId() != null ? new UomUnit(item.getUomUnitId()) : null,
							item.getMrpUomTypeId() != null ? new UomType(item.getMrpUomTypeId()) : null,
							item.getQuantity(), item.getCop(), item.getMrp(), item.getCopAmount(), item.getMarkup(),
							new Tax(item.getTaxId()), item.getTaxPercent(), item.getTaxAmount(), item.getRemark(),
							new Status(item.getStatusId()));

					for (UomResponse uom : item.getUom()) {
						for (OpbUomMapper opbUomMapper : detail.getUomList()) {
							if (uom.getId() != null && uom.getId().equals(opbUomMapper.getId())) {
								opbUomMapper.UpdateOpbUomMapper(uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
										uom.getStoreUom(), new UomType(uom.getUomTypeId()),
										new UomUnit(uom.getUomUnitId()), TimeZoneComponent.getDateByzone(request.getUnitId()), new User(request.getUserId()));
								break;
							}
						}
					}

					List<OpeningBalShelfItem> loc = OpeningBalanceDao.getStorageLocationById(detail.getId());

					for (OpeningBalShelfItem l : loc) {
						Integer shelfItemId = null;
						for (StorageDetails storageDetails : item.getStorageDetails()) {
							if (storageDetails.getId() != null && storageDetails.getId().equals(l.getId())) {
								shelfItemId = storageDetails.getId();
								l.updateShelfItem(new Shelf(storageDetails.getShelfId()),
										new Rack(storageDetails.getRackId()), true, request.getUserId());

							}

							if (shelfItemId == null) {
								l.setIsDeleted(true);
							}
							/*
							 * OpeningBalShelfItem shelfItem = new OpeningBalShelfItem(new
							 * Shelf(storageDetails.getShelfId()), new Store(request.getStoreId()), new
							 * Rack(storageDetails.getRackId()), storageDetails.getRemark() , new
							 * OpeningBalanceDetail(detail.getId()));
							 * sessionService.setEntityDefaults(shelfItem);
							 * detail.addOpeningBalanceShelfItemList(shelfItem);
							 */
						}

					}

					break;
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			// detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		for (ItemRequest item : request.getItems()) {

			if (item.getId() == null) {
				OpeningBalanceDetail detail = new OpeningBalanceDetail(item.getCopAmount(),
						item.getBatchNo() != null && !item.getBatchNo().isEmpty() ? item.getBatchNo() : null,
						item.getCop(), item.getExpiryDate(), new Item(item.getItemId()), item.getMarkup(),
						item.getMrp(), item.getQuantity(), item.getRemark(), new Status(item.getStatusId()),
						item.getTaxAmount(), new Tax(item.getTaxId()), item.getTaxPercent(),
						item.getUomTypeId() != null ? new UomType(item.getUomTypeId()) : null,
						item.getUomUnitId() != null ? new UomUnit(item.getUomUnitId()) : null,
						item.getMrpUomTypeId() != null ? new UomType(item.getMrpUomTypeId()) : null);

				if (!item.getIsBatchReq())
					detail.setLotNumber(item.getLotNo());

				// sessionService.setEntityDefaults(detail);
				detail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());

				for (UomResponse uom : item.getUom()) {
					OpbUomMapper opbUomMapper = new OpbUomMapper(uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
							uom.getStoreUom(), new UomType(uom.getUomTypeId()), new UomUnit(uom.getUomUnitId()));
					// sessionService.setEntityDefaults(opbUomMapper);
					opbUomMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());
					detail.addUom(opbUomMapper);

				}
				for (StorageDetails storageDetails : item.getStorageDetails()) {
					OpeningBalShelfItem shelfItem = new OpeningBalShelfItem(new Shelf(storageDetails.getShelfId()),
							new Store(request.getStoreId()), new Rack(storageDetails.getRackId()),
							storageDetails.getRemark(), new OpeningBalanceDetail(detail.getId()));
					// sessionService.setEntityDefaults(shelfItem);
					shelfItem.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
					detail.addOpeningBalanceShelfItemList(shelfItem);
					// OpeningBalanceDao.saveOpeningBalShelfItem(shelfItem);
				}
				openingBalance.addOpeningBalanceDetail(detail);
			}
		}

		OpeningBalanceDao.updateOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setId(openingBalance.getId().longValue());
		response.setMessage("Opening balance updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelOpeningBalance(OpeningBalanceRequest request) {
		OpeningBalance openingBalance = OpeningBalanceDao.getOpeningBalace(request.getId());

		for (OpeningBalanceDetail detail : openingBalance.getOpeningBalanceDetailList()) {
			detail.setStatus(new Status(request.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
			// detail.setUpdatedBy(openingBalance.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}
		openingBalance.setCancelReason(new CancelReason(request.getCancelReasonId()));
		openingBalance.setCancelNote(request.getCancelNote());

		openingBalance.setStatus(new Status(request.getStatusId()));
		openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
		// openingBalance.setUpdatedBy(openingBalance.getUser());
		openingBalance.setUpdatedByUser(request.getUserId());

		OpeningBalanceDao.updateOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setMessage("Opening balance cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveOpeningBalance(OpeningBalanceRequest request) {

		for (Integer opbId : request.getOpeningBalanceId()) {

			OpeningBalance openingBalance = OpeningBalanceDao.getOpeningBalace(opbId);
			// openingBalance.setApprovedBy(openingBalance.getUser());
			openingBalance.setApprovedBy(new User(request.getUserId()));
			openingBalance.setApprovedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
			Integer storeId = openingBalance.getStore().getStoreId();

			for (OpeningBalanceDetail details : openingBalance.getOpeningBalanceDetailList()) {
				if (details.getIsActive() && !details.getIsDeleted()) {
					Batch batchTemp = OpeningBalanceDao.saveBatch(InsertApprovedItems(details, storeId, request));
				//	Batch batch =  grnDao.saveOrUpdateBatch(batchToSave);
					for (OpeningBalShelfItem openingBalShelfItem : details.getOpeningBalShelfItem()) {

						ShelfItem shelfItem = new ShelfItem(new Shelf(openingBalShelfItem.getShelf().getId()),
								new Batch(batchTemp.getId()), new Store(openingBalShelfItem.getStore().getStoreId()),
								new Rack(openingBalShelfItem.getRack().getId()), openingBalShelfItem.getRemark());
						// sessionService.setEntityDefaults(shelfItem);
						shelfItem.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
								request.getUserId());
						OpeningBalanceDao.saveShelfItem(shelfItem);
					}
					Integer batchData=OpeningBalanceDao.getBatchId(details.getBatchNo(),request.getUnitId());
					details.setStatus(new Status(request.getStatusId()));
					details.setApprovedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
					details.setBatchId(batchData);
					// details.setApprovedBy(details.getUser());
					details.setApprovedBy(new User(request.getUserId()));

					details.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
					// details.setUpdatedBy(details.getUser());
					details.setUpdatedByUser(request.getUserId());
				}
			}
			openingBalance.setStatus(new Status(request.getStatusId()));
			openingBalance.setApprovedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
			// openingBalance.setApprovedBy(openingBalance.getUser());
			openingBalance.setApprovedBy(new User(request.getUserId()));
			openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
			// openingBalance.setUpdatedBy(openingBalance.getUser());
			openingBalance.setUpdatedByUser(request.getUserId());

			OpeningBalanceDao.updateOpeningBalance(openingBalance);
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Opening balance approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveOpeningBalanceDetail(OpeningBalanceRequest request) {
		OpeningBalance openingBalance = OpeningBalanceDao.getOpeningBalace(request.getId());
		// openingBalance.setApprovedBy(openingBalance.getUser());
		openingBalance.setApprovedBy(new User(request.getUserId()));
		openingBalance.setApprovedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
		Integer storeId = openingBalance.getStore().getStoreId();

		for (OpeningBalanceDetail details : openingBalance.getOpeningBalanceDetailList()) {
			for (Integer id : request.getOpeningBalanceDetailId()) {
				if (details.getId().equals(id)
						&& !details.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus())) {
					OpeningBalanceDao.saveBatch(InsertApprovedItems(details, storeId, request));

					details.setStatus(new Status(request.getStatusId()));
					details.setApprovedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
					// details.setApprovedBy(details.getUser());
					details.setApprovedBy(new User(request.getUserId()));
					break;
				}
			}

			if (!details.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus())) {
				details.setStatus(new Status(request.getStatusId()));
			}

			details.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
			// details.setUpdatedBy(details.getUser());
			details.setUpdatedByUser(request.getUserId());
		}

		openingBalance.setStatus(new Status(request.getStatusId()));
		openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
		// openingBalance.setUpdatedBy(openingBalance.getUser());
		openingBalance.setUpdatedByUser(request.getUserId());

		OpeningBalanceDao.updateOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setMessage("Opening balance approved successfully");

		return response;
	}

	@Transactional
	private Batch InsertApprovedItems(OpeningBalanceDetail detail, Integer storeId, OpeningBalanceRequest request) {
		Batch batch = null;
		Integer leaseQuantity = 0;
		Double copLeaseAmount = 0.0;
		Double mrpLeaseAmount = 0.0;
		Double taxLeaseAmount = 0.0;
		Double lcopLeaseAmount = 0.0;
		boolean isBatchFlag = true;
		if (detail.getItem().getIsBatchRequired()) {

			leaseQuantity = Util.GetLeaseQuantity(detail.getQuantity(), detail.getUomList(), detail.getUomType());

			copLeaseAmount = Util.GetMrpLeaseQuantity(detail.getCop(), detail.getUomList(), detail.getMrpUomType());

			mrpLeaseAmount = Util.GetMrpLeaseQuantity(detail.getMrp(), detail.getUomList(), detail.getMrpUomType());

			taxLeaseAmount = Util.GetMrpLeaseQuantity(detail.getTaxAmount(), detail.getUomList(), detail.getUomType());
			 if(detail.getLcop()!=null)
			 {
				 lcopLeaseAmount = detail.getLcop();
			 }
			
		     Batch oldBatch = GlobalDao.getBatchForNa(detail.getBatchNo() != null ? detail.getBatchNo() : null,
						detail.getItem().getId(), detail.getOpeningBalance().getStore().getStoreId(), copLeaseAmount,
						mrpLeaseAmount, request.getUnitId(),
						detail.getExpiryDate() != null ? detail.getExpiryDate() : null);
		    /* Batch oldAvearageBatch = GlobalDao.getBatchForCopAvg(detail.getBatchNo() != null ? detail.getBatchNo() : null,
						detail.getItem().getId(), detail.getOpeningBalance().getStore().getStoreId(), request.getUnitId(),
						detail.getExpiryDate() != null ? detail.getExpiryDate() : null);*/
		     
		     Boolean batchFlag=false;
				if (oldBatch != null) {
					oldBatch.setQuantity(oldBatch.getQuantity() + leaseQuantity);
					
					for (StoreStock s : oldBatch.getStoreStockList()) {
						if (s.getStore().getStoreId().equals(detail.getOpeningBalance().getStore().getStoreId())) {
							s.setTotalQty(s.getTotalQty() + leaseQuantity);
							s.setAvailableQty(s.getAvailableQty() + leaseQuantity);
							oldBatch.setCurrentQuantity(s.getAvailableQty());
							batchFlag=true;
							if(batchFlag.equals(true))
							{
							break;	
							}
						}

					}

					com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
					StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, (oldBatch.getCurrentQuantity()),
							new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
							leaseQuantity, mrpLeaseAmount, new Store(storeId), oldBatch,detail.getId());
					// sessionService.setEntityDefaults(stockTransaction);
					stockTransaction.setTransitNo(detail.getOpeningBalance().getOpbNo());
					stockTransaction.setTransactionDetialsId(detail.getId());
					stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());
					stockTransaction.setInQty(leaseQuantity);
					stockTransaction.setTransactionId(detail.getOpeningBalance().getId());
					stockTransaction.setItem(detail.getItem());
					oldBatch.addStockTransaction(stockTransaction);
					return oldBatch;	
			}
			else
			{
				batch = new Batch(mrpLeaseAmount,
						detail.getBatchNo() != null && !detail.getBatchNo().isEmpty() ? detail.getBatchNo() : null,
						copLeaseAmount, leaseQuantity, detail.getExpiryDate() != null ? detail.getExpiryDate() : null,
						new Item(detail.getItem().getId()), mrpLeaseAmount, detail.getLotNumber(), detail.getMarkup(),
						leaseQuantity, mrpLeaseAmount, mrpLeaseAmount, taxLeaseAmount, new Tax(detail.getTax().getId()),
						detail.getTaxPercentage(),
						detail.getUomUnit() != null ? new UomUnit(detail.getUomUnit().getId()) : null, true);
				batch.setLcop(lcopLeaseAmount);
				batch.setActualLendingPrice(lcopLeaseAmount);
				batch.setIsConsignment(false);
				batch.setIsBatchRequired(isBatchFlag);
				// sessionService.setEntityDefaults(batch);
				batch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				for (OpbUomMapper opbUom : detail.getUomList()) {
					BatchUomMapper batchUomMapper = new BatchUomMapper(opbUom.getConversions(), opbUom.getIpUom(),
							opbUom.getOpUom(), opbUom.getStoreUom(), new UomType(opbUom.getUomType().getId()),
							new UomUnit(opbUom.getUomUnit().getId()));
					// sessionService.setEntityDefaults(batchUomMapper);
					batchUomMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
					batch.addBatchUom(batchUomMapper);
				}
				StoreStock storeStock = new StoreStock(leaseQuantity,
						new Store(detail.getOpeningBalance().getStore().getStoreId()), 0, new Batch(batch.getId()));
				storeStock.setTotalQty(leaseQuantity);
				// sessionService.setEntityDefaults(storeStock);
				storeStock.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				batch.addStoreStock(storeStock);

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, leaseQuantity,
						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
						leaseQuantity, mrpLeaseAmount, new Store(storeId), new Batch(batch.getId()),detail.getId());
				// sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setTransitNo(detail.getOpeningBalance().getOpbNo());
				stockTransaction.setTransactionDetialsId(detail.getId());
				stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				stockTransaction.setInQty(leaseQuantity);
				stockTransaction.setTransactionId(detail.getOpeningBalance().getId());
				stockTransaction.setItem(detail.getItem());
				batch.addStockTransaction(stockTransaction);	
			}
			
			
			
		} else {
			/*leaseQuantity = detail.getQuantity();
			copLeaseAmount = detail.getCop();
			mrpLeaseAmount = detail.getMrp();
			taxLeaseAmount = detail.getTaxAmount();*/
			
			leaseQuantity = Util.GetLeaseQuantity(detail.getQuantity(), detail.getUomList(), detail.getUomType());

			copLeaseAmount = Util.GetMrpLeaseQuantity(detail.getCop(), detail.getUomList(), detail.getMrpUomType());

			mrpLeaseAmount = Util.GetMrpLeaseQuantity(detail.getMrp(), detail.getUomList(), detail.getMrpUomType());

			taxLeaseAmount = Util.GetMrpLeaseQuantity(detail.getTaxAmount(), detail.getUomList(), detail.getUomType());
			isBatchFlag = false;
			Batch oldBatch = GlobalDao.getBatchForNa(detail.getBatchNo() != null ? detail.getBatchNo() : null,
					detail.getItem().getId(), detail.getOpeningBalance().getStore().getStoreId(), detail.getCop(),
					detail.getMrp(), request.getUnitId(),
					detail.getExpiryDate() != null ? detail.getExpiryDate() : null);
			if (oldBatch != null) {
				oldBatch.setQuantity(oldBatch.getQuantity() + leaseQuantity);
				
				for (StoreStock s : oldBatch.getStoreStockList()) {
					if (s.getStore().getStoreId().equals(detail.getOpeningBalance().getStore().getStoreId())) {
						s.setTotalQty(s.getTotalQty() + leaseQuantity);
						s.setAvailableQty(s.getAvailableQty() + leaseQuantity);
						oldBatch.setCurrentQuantity(s.getAvailableQty());
					}

				}

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, (oldBatch.getCurrentQuantity()),
						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
						leaseQuantity, mrpLeaseAmount, new Store(storeId), oldBatch,detail.getId());
				// sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setTransitNo(detail.getOpeningBalance().getOpbNo());
				stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				stockTransaction.setTransactionDetialsId(detail.getId());
				stockTransaction.setInQty(leaseQuantity);
				stockTransaction.setTransactionId(detail.getOpeningBalance().getId());
				stockTransaction.setItem(detail.getItem());
				oldBatch.addStockTransaction(stockTransaction);
				return oldBatch;

			} else {
				batch = new Batch(mrpLeaseAmount,
						detail.getBatchNo() != null && !detail.getBatchNo().isEmpty() ? detail.getBatchNo() : null,
						copLeaseAmount, leaseQuantity, detail.getExpiryDate() != null ? detail.getExpiryDate() : null,
						new Item(detail.getItem().getId()), mrpLeaseAmount, detail.getLotNumber(), detail.getMarkup(),
						leaseQuantity, mrpLeaseAmount, mrpLeaseAmount, taxLeaseAmount, new Tax(detail.getTax().getId()),
						detail.getTaxPercentage(),
						detail.getUomUnit() != null ? new UomUnit(detail.getUomUnit().getId()) : null, true);
				batch.setLcop(detail.getLcop() != null ? detail.getLcop() : 0);
				batch.setActualLendingPrice(detail.getLcop() != null ? detail.getLcop() : 0);
				batch.setIsConsignment(false);
				batch.setIsBatchRequired(isBatchFlag);
				// sessionService.setEntityDefaults(batch);
				batch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				for (OpbUomMapper opbUom : detail.getUomList()) {
					BatchUomMapper batchUomMapper = new BatchUomMapper(opbUom.getConversions(), opbUom.getIpUom(),
							opbUom.getOpUom(), opbUom.getStoreUom(), new UomType(opbUom.getUomType().getId()),
							new UomUnit(opbUom.getUomUnit().getId()));
					// sessionService.setEntityDefaults(batchUomMapper);
					batchUomMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());
					batch.addBatchUom(batchUomMapper);
				}
				StoreStock storeStock = new StoreStock(leaseQuantity,
						new Store(detail.getOpeningBalance().getStore().getStoreId()), 0, new Batch(batch.getId()));
				storeStock.setTotalQty(leaseQuantity);
				// sessionService.setEntityDefaults(storeStock);
				storeStock.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				batch.addStoreStock(storeStock);

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
				StockTransaction stockTransaction = new StockTransaction(copLeaseAmount, leaseQuantity,
						new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), mrpLeaseAmount,
						leaseQuantity, mrpLeaseAmount, new Store(storeId), new Batch(batch.getId()),detail.getId());
				// sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setTransitNo(detail.getOpeningBalance().getOpbNo());
				stockTransaction.setTransactionDetialsId(detail.getId());
				stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				stockTransaction.setInQty(leaseQuantity);
				stockTransaction.setTransactionId(detail.getOpeningBalance().getId());
				stockTransaction.setItem(detail.getItem());
				batch.addStockTransaction(stockTransaction);
			}
		}

		return batch;
	}
}
