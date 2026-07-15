package com.param.global.pharmacy.service;

import in.param.exception.ApplicationException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.param.common.dto.ErrorResponse;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.DiscountCategory;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.Gender;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Order;
import com.param.entity.model.master.Prefix;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.Screen;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.BatchAllocation;
import com.param.entity.model.pharmacy.Instruction;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.pharmacy.SaleDetailTaxComponenet;
import com.param.entity.model.pharmacy.SaleType;
import com.param.entity.model.pharmacy.TempDoctor;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.dao.IpdChargesDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.common.BillingCommonMethods;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.AllDataResponse;
import com.param.global.dto.AvailableQty;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsDiscountDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UpdatePayeeDto;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.ItemsDetailsResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.pharmacy.dao.IWalkInIssueDao;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.pharmacy.dto.ItemRequest;
import com.param.global.pharmacy.dto.ParamacyContractReqDto;
import com.param.global.pharmacy.dto.PatientConsumableIssueSave;
import com.param.global.pharmacy.dto.PatientConsumableIssueSaveItem;
import com.param.global.pharmacy.dto.PatientConsumtionIssueSave;
import com.param.global.pharmacy.dto.PatientConsumtionSaveItem;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.pharmacy.dto.PrescriptionDetailsForOPSalesWorklistDto;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.pharmacy.dto.SaleDetailsChangeOfPayee;
import com.param.global.pharmacy.dto.SaleDtoChangeOfPayee;
import com.param.global.pharmacy.dto.WalkInPrescriptionItemsReq;
import com.param.global.pharmacy.dto.WalkInPrescriptionReq;
import com.param.global.pharmacy.dto.WalkInSalesDropdownResponse;
import com.param.global.pharmacy.dto.WalkInSalesRequest;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IPatientSponsorDetailsService;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
@Service
@SuppressWarnings({ "unchecked", "rawtypes","unused"})
public class WalkInSalesService implements IWalkInSalesService, ICommonConstants {
	final static Logger logger = Logger.getLogger(WalkInSalesService.class);


	@Autowired
	private SessionService sessionService;

	@Autowired
	IWalkInSalesDao WalkInSalesDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IPatientSponsorDetailsService iPatientSponsorDetailsService;

	@Autowired
	IPatientRegistrationDao iPatientRegistrationDao;

	@Autowired
	IContractConsumptionDao iContractConsumptionDao;

	@Autowired
	IOrderMasterService iOrderMasterService;
	
	@Autowired
	IOrderMasterDao iOrderMasterDao;

	@Autowired
	IMPackageMasterService iMPackageMasterService;

	@Autowired
	IMPackageMasterDao iMPackageMasterDao;

	@Autowired
	IBillingService iBillingMasterService;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	private IWalkInIssueDao iWalkInIssueDao;

	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;

	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	
	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao; 

	@Autowired
	IpdChargesDao ipdChargesDao;
	
	@Autowired
	BillingCommonMethods billingCommonMethods;

	private Integer issueScreenId = com.param.common.constants.Screen.PendingIssues.getScreen();
	
	private static boolean isDummyService = false;
	@Override
	@Transactional
	public WalkInSalesDropdownResponse getDropdowns(Integer orgId, Integer unitId) {

		WalkInSalesDropdownResponse response = new WalkInSalesDropdownResponse();

		/*
		 * for (Prefix prefix : GlobalDao.getPrefixList()) { response.addPrefix(new
		 * com.param.global.dto.global.Prefix(prefix.getPrefixId(),
		 * prefix.getPrefixCode())); }
		 */

		response.setPrefix(GlobalDao.getPrefixList(orgId));
		response.setGender(GlobalDao.getGenderList());
		response.setDiscountCategory(GlobalDao.getDiscountCategoryListBP(orgId, unitId));

		/*
		 * for (Gender gender : GlobalDao.getGenderList()) { response.addGender(new
		 * com.param.global.dto.global.Gender(gender.getGenderId(),
		 * gender.getGenderName())); }
		 */

		/*
		 * for (DiscountCategory discountCategory : GlobalDao.getDiscountCategoryList())
		 * { response.addDiscountCategory(new
		 * com.param.global.dto.global.DiscountCategory(discountCategory.getId(),
		 * discountCategory.getCategory())); }
		 */
		return response;
	}

	@Override
	@Transactional
	public CommonResponse allocateItems(WalkInSalesRequest request) throws Exception {
		try {
			if (request.getItem().getQuantity() <= 99999) {
				if (request.getItem().getAllocationIds().size() == 0) {
					return allocateItemQuantity(request);
				} else {
					return updateAllocatedItemQuantity(request);
				}
			} else {
				throw new CustomException("Stock is not available");
			}
		} catch (CustomException e) {
			throw new CustomException("Stock is not available");
		} catch (Exception e) {
		logger.error("allocateItems failed", e);
		}
		return null;
	}

	private boolean revertAllocatedItemQuantityWalkin(BatchAllocation batchAllocation, Integer screenId,
			Integer storeId, Integer userId) {
		Long batchId = batchAllocation.getBatch().getId();

		// start code by minesh for revertAllocatedItemQuantity for club batch or
		// consignment batch(add if condation).
		Batch batchC = GlobalDao.getBatch(batchId);
		BigInteger consignmentCount = GlobalDao.consignmentBatchCount(batchC.getItem().getId(), batchC.getBatchNo(),
				batchC.getLotNo() != null ? batchC.getLotNo() : null, null, batchAllocation.getUnitId());
		if (consignmentCount.longValue() > 0) {
			return revertAllocatedItemQuantityForClubBatchWalkin(batchAllocation, screenId);
		} else {
			// end code by minesh for revertAllocatedItemQuantity for club batch or
			// consignment batch

			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId, 0);// 0
																								// itemId
			//System.out.println("Batch id : "+batchId);
			Batch batch = GlobalDao.getBatch(storeId, batchId, batchAllocation.getUnitId());

			Integer quantity = batchAllocation.getQuantity();

			Integer balanceQuantity = batch.getCurrentQuantity() + quantity;

			batch.setCurrentQuantity(balanceQuantity);
			batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock + quantity);

			batchAllocation.setQuantity(0);
			batchAllocation.setBatch(batch);

			Screen screen = GlobalDao.getScreen(screenId);

			StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					quantity, batch.getRate(), batch.getStoreStockList().get(0).getStore(), batch, 0);

			stockTransaction.setCreateDefaults(
					batchAllocation.getOrganizationId() != null ? batchAllocation.getOrganizationId()
							: sessionService.getOrganizationId(),
					batchAllocation.getUnitId() != null ? batchAllocation.getUnitId() : sessionService.getUnitId(),
					userId != null ? userId : sessionService.getUserId());

			WalkInSalesDao.saveStockTransaction(stockTransaction);

			Integer id = WalkInSalesDao.updateAllocatedQuantity(batchAllocation);
			return id == batchAllocation.getId();
		}
	}

	@Override
	@Transactional
	public CommonResponse deleteAllocation(Integer screenId, Long tempBillId, List<Integer> allocationIds) {
		/*
		 * if (allocationIds != null) { for (Integer allocationId : allocationIds) {
		 * BatchAllocation batchAllocation =
		 * WalkInSalesDao.getBatchAllocation(allocationId); if
		 * (revertAllocatedItemQuantity(batchAllocation, screenId)) {
		 * WalkInSalesDao.deleteBatchAllocation(batchAllocation.getId()); } } } else {
		 * for (BatchAllocation batchAllocation :
		 * WalkInSalesDao.getBatchAllocations(tempBillId)) { if
		 * (revertAllocatedItemQuantity(batchAllocation, screenId)) {
		 * WalkInSalesDao.deleteBatchAllocation(batchAllocation.getId()); } } }
		 * CommonResponse response = new CommonResponse();
		 * response.setMessage("Item quantity allocation deleted successfully"); return
		 * response;
		 */
		return null;
	}

	@Override
	@Transactional
	public CommonResponse deleteAllocationScheduler(Date date) {
		List<BatchAllocation> batchAllocations = WalkInSalesDao.getBatchAllocation(date);
		if (batchAllocations != null && !batchAllocations.isEmpty()) {
			for (BatchAllocation batchAllocation : batchAllocations) {
				Boolean is_success = false;
				try {
					is_success = revertAllocatedItemQuantityScheduled(batchAllocation, 1);
				} catch (Exception e) {
				logger.error("deleteAllocationScheduler: error while reverting quantity for allocation " + batchAllocation.getId(), e);
				}
				if (is_success) {
					try {
						WalkInSalesDao.deleteBatchAllocation(batchAllocation.getId());
					} catch (Exception e) {
					logger.error("deleteAllocationScheduler: error while deleting batch allocation " + batchAllocation.getId(), e);
					}
				}
			}
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Item quantity allocation deleted successfully");

		return response;
	}

	@Override
	@Transactional(rollbackOn = CustomException.class)
	public CommonResponse saveBill(WalkInSalesRequest request) throws Exception {
		/*
		 * try { if (request.ValidateSave()) { return createSale(request); } else {
		 * throw new IllegalArgumentException(request.getValidationMessage()); } } catch
		 * (IllegalArgumentException e) { throw e; } catch (Exception e) { throw e; }
		 */

		if (request.ValidateSave()) {
			return createSale(request);
		} else {
			throw new IllegalArgumentException(request.getValidationMessage());
		}
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public CommonResponse saveBillComplete(WalkInSalesRequest request) throws Exception {
		CommonResponse walkinResponse = this.saveBill(request);
		List<SaleDetail> details = this.getSaleDetailsBySaleId(walkinResponse.getId());
		if (details != null && details.size() > 0) {
			Boolean fromScreen = request.getPrescriptionId() != null;
			this.updateInsertIntoStoreStock(details, walkinResponse.getId(), request, fromScreen);
		}
		return walkinResponse;
	}

	@Override
	@Transactional
	public ItemsDetailsResponse allocateBatch(WalkInSalesRequest request) throws Exception {
		try {
			List<Batch> batchList = GlobalDao.getBatchDetailsPharmacyConsignment(request.getItem().getItemId(),
					request.getStoreId(), request.getUnitId());
			ItemsDetailsResponse response;

			if (batchList.isEmpty()) {
				throw new CustomException("Allocation of batch failed");
			} else {
				boolean batchExists = false;

				response = GlobalDao.itemsDetail(request.getItem().getItemId());

				Integer currentStock = 0;

				for (Batch batch : batchList) {
					if (!batchExists) {
						batchExists = batch.getId().equals(request.getItem().getBatchId());
					}

					response.addBatch(Util.PopulateBatch(batch));
					currentStock = currentStock + batch.getCurrentQuantity();
				}

				response.setCurrentStock(currentStock);

				if (batchExists) {
					BatchResponse batchResponse = response.getBatch().stream()
							.filter(batch -> batch.getBatchId().equals(request.getItem().getBatchId()))
							.collect(Collectors.toList()).get(0);

					Batch batch = batchList.stream().filter(b -> b.getId().equals(request.getItem().getBatchId()))
							.collect(Collectors.toList()).get(0);

					response.setAllocationId(allocateBatchQuantity(request, batch, batchResponse));
				}

				return response;
			}
		} catch (CustomException e) {
			throw new CustomException("Allocation of batch failed");
		} catch (Exception e) {
		logger.error("allocateBatch failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public ItemsDetailsResponse allocateBatchWithTariff(WalkInSalesRequest request) throws Exception {
		try {
				List<Batch> batchList = new ArrayList<>();
			if(request.getBarcodeScanned()){
				batchList = GlobalDao.getBatchDetailsPharmacyForBarcodeScan(request.getItem().getBatchId().intValue(),
						request.getStoreId(), request.getUnitId());
			}
			else{

				batchList = GlobalDao.getBatchDetailsPharmacyConsignment((request.getItem().getItemId()!=null ? request.getItem().getItemId():request.getItem().getBatchId().intValue()),

						request.getStoreId(), request.getUnitId());
			}
			
			ItemsDetailsResponse response;

			if (batchList.isEmpty()) {
//				throw new CustomException("Allocation of batch failed");
				response = new ItemsDetailsResponse();
				return response;
			} else {
				boolean batchExists = false;

				response = GlobalDao.itemsDetailTariff(batchList.get(0).getItem().getId());

				Double patientclasspercentage = GlobalDao.patientclasspercentage(batchList.get(0).getItem().getId(),request.getOrganizationId(),request.getUnitId(),request.getEntitlementId()); 
				response.setPatientClassPercentage(patientclasspercentage);

				List<Integer> prodCatIds = new ArrayList<Integer>();
				prodCatIds.add(response.getProductCategoryId());

				List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, request.getEntitlementId());
				if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
					if (pharmacyTariffs.size() > 1) {
						// throw exception?
						// Don't throw - get first object and set data.
					}

					PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
					response.setMarkUpDown(pharmacyTariff.getMarkUpDown());
					response.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
				}

				Integer currentStock = 0;

				for (Batch batch : batchList) {
					if (!batchExists) {
						batchExists = batch.getId().equals(request.getItem().getBatchId());
					}
					BatchResponse batchWithQty=Util.PopulateBatch(batch);
					batchWithQty.setAvailableStockQuantity(batch.getCurrentQuantity());
					batchWithQty.setTaxId(batch.getTax().getId());
					batchWithQty.setTaxPercentage(batch.getTaxPercentage());
					response.addBatch(batchWithQty);
					currentStock = currentStock + batch.getCurrentQuantity();
				}

				response.setCurrentStock(currentStock);
				response.setIsThreeUOMApplicable(response.getIsThreeUOMApplicable());

				if (batchExists) {
					BatchResponse batchResponse = response.getBatch().stream()
							.filter(batch -> batch.getBatchId().equals(request.getItem().getBatchId()))
							.collect(Collectors.toList()).get(0);

					Batch batch = batchList.stream().filter(b -> b.getId().equals(request.getItem().getBatchId()))
							.collect(Collectors.toList()).get(0);

					//response.setAllocationId(allocateBatchQuantity(request, batch, batchResponse));
				}
				response.setColorCode(GlobalDao.getColorCode(response.getGenericId(),request.getOrganizationId(),request.getUnitId()));
				return response;
			}
		} catch (Exception e) {
			throw new CustomException("Allocation of batch failed");
		}
//		catch (Exception e) {
//
//		}
//		return null;
	}

	/* Private Methods */
	private CommonResponse allocateItemQuantity(WalkInSalesRequest request) throws CustomException {

		try {
			Long batchId = new Long(request.getItem().getBatchId());

			// added code by minesh for consimentLogic
			Batch batchC = (batchId != null && batchId > 0) ? GlobalDao.getBatch(batchId) : null;
			// BigInteger consignmentCount =
			// GlobalDao.consignmentBatchCount(batchC.getItem().getId(),
			// batchC.getBatchNo(), batchC.getLotNo() != null ? batchC.getLotNo() : null,
			// null);
			BigInteger consignmentCount = GlobalDao.consignmentBatchCount(request.getItem().getItemId(),
					(batchC != null && batchC.getBatchNo() != null) ? batchC.getBatchNo() : null,
					(batchC != null && batchC.getLotNo() != null) ? batchC.getLotNo() : null, null,
					request.getUnitId());
			/*
			 * if (consignmentCount.longValue() > 0) { List<StoreStock> StoreStockList =
			 * GlobalDao.getStoreStockListByBatchIdAndStoreId(batchId, request.getStoreId(),
			 * request.getUnitId()); Integer totalBatchCurrentQuantity =
			 * StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum(); int
			 * issueQtyFromRequest = request.getItem().getQuantity(); if
			 * (totalBatchCurrentQuantity >= issueQtyFromRequest) { long batchAllocationId =
			 * 0; for (StoreStock storeStockObj : StoreStockList) { Integer
			 * storeAvaliableQuantity = storeStockObj.getAvailableQty(); Long
			 * batchIdFromStore = storeStockObj.getBatch().getId();
			 * 
			 * if (issueQtyFromRequest != 0) { if (issueQtyFromRequest <=
			 * storeAvaliableQuantity) { return allocateItemQuantityForClubBatch(request,
			 * issueQtyFromRequest, storeAvaliableQuantity, batchIdFromStore);
			 * 
			 * } else { int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
			 * allocateItemQuantityForClubBatch(request, storeAvaliableQuantityINT,
			 * storeAvaliableQuantity, batchIdFromStore); issueQtyFromRequest =
			 * issueQtyFromRequest - storeAvaliableQuantityINT; } } } CommonResponse
			 * response = new CommonResponse(); response.setIds(batchAllocationId);
			 * response.setMessage("Item quantity allocated successfully"); return response;
			 * } else { throw new
			 * CustomException("Quantity entered is exceeding max quantity"); } } // code
			 * end by minesh for consimentLogic and origincode is below in else //
			 * condation. else {
			 */
			/**
			 * BATCH REQUIRED STOCK ALLOCATION Ganesh
			 */
			CommonResponse response = new CommonResponse();
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(request.getStoreId(), batchId,
					request.getItem().getItemId());
			Integer balanceQty = null;
			if (avaialableStoreStock >= request.getItem().getQuantity()) {
				// Batch batch = GlobalDao.getBatch(sessionService.getStoreId(), batchId);
				List<Batch> batchList = GlobalDao.getBatchList(request.getStoreId(), batchId,
						request.getItem().getItemId(), request.getUnitId());
				Integer quantity = request.getItem().getQuantity();
				List<BatchAllocation> batchAllocationList = new ArrayList<>();
				for (Batch batch : batchList) {
					Integer avaialableStoreStockByBatch = GlobalDao.getStoreStockByBatch(request.getStoreId(),
							batch.getId(), null);
					if (quantity > 0 && avaialableStoreStockByBatch > 0 && batch.getQuantity() > 0) {
						Integer balanceQuantity = 0;
						Integer usedQuantity = 0;
						if (batch.getCurrentQuantity() >= quantity) {
							balanceQuantity = batch.getCurrentQuantity() - quantity;
							usedQuantity = quantity;
							// quantity = 0;
						} else {
							balanceQuantity = 0;
							// quantity = quantity - batch.getCurrentQuantity();
							usedQuantity = quantity;
						}
						batch.setCurrentQuantity(balanceQuantity);
						batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStockByBatch - quantity);
						balanceQty = avaialableStoreStockByBatch - quantity;
						BatchAllocation batchAllocation = new BatchAllocation(request.getTempBillId(),
								batch.getStoreStockList().get(0).getStore(), batch, quantity);
						batchAllocation.setIsFromOt(request.getIsFromOt() != null ? request.getIsFromOt() : false);
						// sessionService.setEntityDefaults(batchAllocation);
						batchAllocation.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
								request.getUserId());
						Screen screen = GlobalDao.getScreen(request.getScreenId());

						StockTransaction stockTransaction = new StockTransaction(avaialableStoreStockByBatch, screen,
								screen.getCode(), usedQuantity, batch.getRate(),
								batch.getStoreStockList().get(0).getStore(), batch, 0);
						stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
								request.getUserId());
						// sessionService.setEntityDefaults(stockTransaction);
						WalkInSalesDao.saveStockTransaction(stockTransaction);
						batchAllocationList.add(batchAllocation);
					}
				}
				//System.out.println(batchAllocationList);//NitinSysoutComment
				List<Long> allocationIdss = WalkInSalesDao.allocateQuantity(batchAllocationList);
				if (!allocationIdss.isEmpty()) {
					response.setId(allocationIdss.get(0));
				}

				response.setIds(allocationIdss);
				response.setMessage("Item quantity allocated successfully");
				response.setObject(balanceQty);
				//System.out.println(response);//NitinSysoutComment
				return response;

			} else {
				throw new CustomException("Quantity entered is exceeding max quantity");
			}
			// }
		} catch (CustomException e) {
			throw new CustomException("Allocation of item quantity failed");
		} catch (Exception e) {
		logger.error("allocateItemQuantity failed", e);
		}
		return null;
	}

	// created by minesh for consignment batch issue
	private CommonResponse allocateItemQuantityForClubBatch(WalkInSalesRequest request, int issueQtyFromRequest,
			Integer storeAvaliableQuantity, Long batchIdFromStore) {
		long batchAllocationId = 0;

		Batch batch = GlobalDao.getBatch(request.getStoreId(), batchIdFromStore, request.getUnitId());
		StoreStock storeStockForbatch = GlobalDao.getStoreStockByBatchAndStore(batch.getId(), request.getStoreId(),
				request.getUnitId());
		Screen screen = GlobalDao.getScreen(request.getScreenId());
		Integer balanceQuantity = 0;
		if (batch.getIsConsignment()) {
			// Batch convertedBatch = getConsignmentConvertedBatch(batch,
			// issueQtyFromRequest, request);
			balanceQuantity = storeStockForbatch.getAvailableQty() - issueQtyFromRequest;
			batch.setCurrentQuantity(batch.getCurrentQuantity() - issueQtyFromRequest);
			batch.setQuantity(batch.getQuantity() - issueQtyFromRequest);
			storeStockForbatch.setAvailableQty(storeAvaliableQuantity - issueQtyFromRequest);
			// storeStockForbatch.settQty(storeStockForbatch.gettQty() +
			// issueQtyFromRequest);

			// for convertedBatch.
			// convertedBatch.setCurrentQuantity(convertedBatch.getCurrentQuantity() -
			// issueQtyFromRequest);
			/*
			 * StoreStock storeStock = new StoreStock(0, new Store(request.getStoreId()),
			 * batch); storeStock.setCreateDefaults(request.getOrganizationId(),
			 * request.getUnitId(), request.getUserId()); //
			 * sessionService.setEntityDefaults(storeStock); // storeStock =
			 * GlobalDao.saveStoreStockLeaseQuantity(storeStock); //
			 * storeStock.setAvailableQty(storeStock.getAvailableQty()-issueQtyFromRequest);
			 * storeStock = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
			 */

			BatchAllocation batchAllocation = new BatchAllocation(request.getTempBillId(),
					batch.getStoreStockList().get(0).getStore(), batch, issueQtyFromRequest);
			// sessionService.setEntityDefaults(batchAllocation);
			batchAllocation.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			WalkInSalesDao.saveBatchAllocation(batchAllocation);
			batchAllocationId = batchAllocation.getId().longValue();

			StockTransaction stockTransaction = new StockTransaction(storeAvaliableQuantity, screen, screen.getCode(),
					issueQtyFromRequest, batch.getRate(), storeStockForbatch.getStore(), batch, 0);
			// sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			WalkInSalesDao.saveStockTransaction(stockTransaction);
		} else {
			balanceQuantity = storeStockForbatch.getAvailableQty() - issueQtyFromRequest;

			batch.setCurrentQuantity(balanceQuantity);

			storeStockForbatch.setAvailableQty(storeAvaliableQuantity - issueQtyFromRequest);

			BatchAllocation batchAllocation = new BatchAllocation(request.getTempBillId(),
					batch.getStoreStockList().get(0).getStore(), batch, issueQtyFromRequest);
			batchAllocation.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			// sessionService.setEntityDefaults(batchAllocation);
			WalkInSalesDao.saveBatchAllocation(batchAllocation);
			batchAllocationId = batchAllocation.getId().longValue();

		}

		StockTransaction stockTransactionForBatch = new StockTransaction(storeAvaliableQuantity, screen,
				screen.getCode(), issueQtyFromRequest, batch.getRate(), storeStockForbatch.getStore(), batch, 0);
		stockTransactionForBatch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
				request.getUserId());
		// sessionService.setEntityDefaults(stockTransactionForBatch);
		WalkInSalesDao.saveStockTransaction(stockTransactionForBatch);

		CommonResponse response = new CommonResponse();
		response.setId(batchAllocationId);
		response.setMessage("Item quantity allocated successfully");
		response.setObject(balanceQuantity);
		return response;

		// return batchAllocationId;
	}

	private Batch getConsignmentConvertedBatch(Batch batch, Integer quantity, WalkInSalesRequest request) {
		Batch newBatch = new Batch();
		newBatch.setOrganization(batch.getOrganization());
		newBatch.setUnit(batch.getUnit());
		newBatch.setUomUnit(batch.getUomUnit());
		newBatch.setItem(batch.getItem());
		newBatch.setBatchNo(batch.getBatchNo());
		newBatch.setLotNo(batch.getLotNo());
		newBatch.setQuantity(quantity);// leaseQuantity
		newBatch.setCurrentQuantity(quantity);// leaseQuantity
		newBatch.setExpiryDate(batch.getExpiryDate());
		newBatch.setCop(batch.getCop());
		newBatch.setRate(batch.getRate());
		newBatch.setSellingPrice(batch.getSellingPrice());
		newBatch.setMarkup(batch.getMarkup());
		newBatch.setLendingPrice(batch.getLendingPrice());
		newBatch.setActualLendingPrice(batch.getActualLendingPrice());
		newBatch.setTax(batch.getTax());
		newBatch.setTaxPercentage(batch.getTaxPercentage());
		newBatch.setTaxAmount(batch.getTaxAmount());
		newBatch.setBarcode(batch.getBarcode());
		newBatch.setIsOpb(batch.getIsOpb());
		newBatch.setIsActive(batch.getIsActive());
		newBatch.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		newBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		newBatch.setAddedByUser(request.getUserId());
		newBatch.setUpdatedByUser(request.getUserId());
		newBatch.setIsDeleted(batch.getIsDeleted());
		newBatch.setBarcodePrintCount(batch.getBarcodePrintCount());
		newBatch.setIsQuarantine(batch.getIsQuarantine());
		newBatch.setVendor(batch.getVendor());
		newBatch.setServiceProvider(batch.getServiceProvider());
		newBatch.setWarrantyFrom(batch.getWarrantyFrom());
		newBatch.setWarrantyTo(batch.getWarrantyTo());
		newBatch.setPmsFrequency(batch.getPmsFrequency());
		newBatch.setYearOfManufacture(batch.getYearOfManufacture());
		newBatch.setWorkingCondition(batch.getWorkingCondition());
		newBatch.setModelNumber(batch.getModelNumber());
		newBatch.setIsPharmacy(batch.getIsPharmacy());
		newBatch.setIsIndent(batch.getIsIndent());
		newBatch.setIsReturn(false);
		newBatch.setRejectedQuantity(0);
		newBatch.setReturnedQuantity(0);
		newBatch.setIsConsignment(false);
		newBatch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		for (BatchUomMapper batchUomMapperObj : batch.getBatchUomList()) {
			BatchUomMapper batchUomMapper = new BatchUomMapper(batchUomMapperObj.getConversions(),
					batchUomMapperObj.getIpUom(), batchUomMapperObj.getOpUom(), batchUomMapperObj.getStoreUom(),
					new UomType(batchUomMapperObj.getUomType().getId()),
					new UomUnit(batchUomMapperObj.getUomUnit().getId()));
			batchUomMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			newBatch.addBatchUom(batchUomMapper);
		}

		newBatch = GlobalDao.saveBatch(newBatch);
		return newBatch;
	}

	// created end by minesh for consignment batch issue

	private void updateStoreTransitStock(Integer storeId, Long batchId, Integer quantity, Integer requestStoreId) {

		if (quantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);
				if (batch != null) {
					// Integer leaseQuantity = Util.GetLeaseQuantity(quantity,
					// batch.getBatchUomList(), uomType);

					StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batchId, requestStoreId, null);
					StoreStock storeStockDetail = null;
					if (storeStockList != null) {
						// tQuantity = leaseQuantity + storeStockList.gettQty();
						// availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
						// storeStockList.setAvailableQty(availableQuantity);
						Integer qty = 0;
						if (storeStockList.gettQty() != null) {
							qty = storeStockList.gettQty();
						}
						storeStockList.settQty(qty + quantity);
						// storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
					}

				}
			} catch (Exception e) {
			logger.error("updateStoreTransitStock failed", e);
			}
		}

	}

	private CommonResponse updateAllocatedItemQuantity(WalkInSalesRequest request) throws Exception {
		try {
			CommonResponse response = new CommonResponse();
			List<Long> batchAllocationsIds = new ArrayList<>();
			if (request.getItem().getAllocationIds() != null && request.getItem().getAllocationIds().size() > 0) {
				for (Integer allocationId : request.getItem().getAllocationIds()) {
					BatchAllocation batchAllocation = WalkInSalesDao.getBatchAllocation(allocationId);
					if (batchAllocation != null) {
						if (revertAllocatedItemQuantity(batchAllocation, request)) {
							response = updateEntities(batchAllocation, request);
						} else {
							throw new CustomException("Allocation of item quantity failed");
							/* } */
						}
						if (response.getId() != null)
							batchAllocationsIds.add(response.getId());
					}
					
					
					if(batchAllocation ==null)
					{

					
						return allocateItemQuantity(request);
					
					}
					/*
					 * if
					 * (request.getItem().getBatchId().equals(batchAllocation.getBatch().getId())) {
					 * response = updateEntities(batchAllocation, request);
					 * 
					 * } else {
					 */

				}
				response.setIds(batchAllocationsIds);
				//System.out.println(response);//NitinSysoutComment
				return response;
			}
		} catch (CustomException e) {
			throw new CustomException("Allocation of item quantity failed");
		} catch (Exception e) {
		logger.error("updateAllocatedItemQuantity failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public void revertAllocatedQty(Long saleId, Integer screenId, Integer unitId, String cancelledRemark, Integer cancelledReasonId, Integer userId) throws Exception {
		
		try {
			Sale sale = WalkInSalesDao.getSaleBySaleId(saleId);
			
			//check data cancelled or not 
			//System.out.println("sale.getStatus().getId()" +sale.getStatus().getId());
           
			if(sale == null ){
				
					CommonResponse response = new CommonResponse();
					throw new CustomException("This transaction already cancelled by other user ");
			}
			
			List<SaleDetailPackageMapperDto> getDepositDetails = WalkInSalesDao.getPharmacyDepositConsumsedAmount(saleId);
			if(getDepositDetails != null && getDepositDetails.size()>0){
				for (SaleDetailPackageMapperDto pkgMpper : getDepositDetails) 
				{
					pkgMpper.setCreatedBy(userId);
				}
			}
			Integer getDepositDetails1 =  WalkInSalesDao.updateDepositDetails(getDepositDetails);
			List<PaymentDepositDetailsDto> getPaymentDepositDetailsDto = WalkInSalesDao.getSalePaymentModeDetailsConsumsedAmount(saleId,userId);
			
			if (!revertAllocatedQuantity(sale, screenId, unitId))
				throw new CustomException("Error while reverting the quantity");

			// for Package sum
			Map<Integer, OrderDetailsPackageMapperDto> packageMapForSum = new HashMap<>();
			// for contract sum
			Map<Integer, OrderDetailsPayeeMapperDto> contractMapForSum = new HashMap<>();

			Integer encounterId = sale.getEncounter() != null ? sale.getEncounter().getEncounterId() : null;

			Integer admissionId = sale.getAdmission() != null ? sale.getAdmission().getAdmissionId() : null;

			boolean isPreAuthExsist = iContractConsumptionDao.getPreAuthAmutByVisitId(encounterId, admissionId);

			List<Long> cancelledSaleDetailsIds = new LinkedList<>();
			if (sale != null && sale.getSaleDetailsList() != null && !sale.getSaleDetailsList().isEmpty()) {
				List<SaleDetail> saleDetails = sale.getSaleDetailsList();
				for (SaleDetail saleDetail : saleDetails) 
				{
					cancelledSaleDetailsIds.add(saleDetail.getId());
					// for Package
					Map<Integer, OrderDetailsPackageMapperDto> packageMap = new HashMap<>();

					// for contract
					Map<Integer, OrderDetailsPayeeMapperDto> contractMap = new HashMap<>();
					SaleDetail saleDetailsDto = GlobalDao.getSaleDetailsById(saleDetail.getId());
					Double packageNetAmount = (saleDetailsDto.getNetAmount() != null
							&& saleDetailsDto.getNetAmount() > 0)
									? saleDetailsDto.getNetAmount() + saleDetailsDto.getPackageConsumptionAmt()
									: saleDetailsDto.getPackageConsumptionAmt();
					List<SaleDetailPackageMapperDto> packageList = GlobalDao.getSalePackageMpperListbyDetailsIdReturn(
							saleDetailsDto.getId().intValue(), new BigDecimal(packageNetAmount));
					// For Package
					for (SaleDetailPackageMapperDto mppr : packageList) {
						if (packageMap.containsKey(mppr.getPackageConsumptionMasterId())) {
							OrderDetailsPackageMapperDto mpprDto = packageMap.get(mppr.getPackageConsumptionMasterId());
							mpprDto.setPackageAmount(
									mpprDto.getPackageAmount().add(BigDecimal.valueOf(mppr.getPackageAmount())));
							mpprDto.setServiceAmount(
									mpprDto.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
							packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);

							if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) {
								OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum
										.get(mppr.getPackageConsumptionMasterId());
								mpprDtofinal.setPackageAmount(mpprDtofinal.getPackageAmount()
										.add(BigDecimal.valueOf(mppr.getPackageAmount())));
								mpprDtofinal.setServiceAmount(
										mpprDtofinal.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
							}

						} else {
							OrderDetailsPackageMapperDto mpprDto = new OrderDetailsPackageMapperDto();
							mpprDto.setOrganizationId(sessionService.getOrganizationId());
							mpprDto.setUnitId(unitId);
							mpprDto.setStatus('A');
							mpprDto.setIsPackageService(mppr.getIsPackageItem());
							mpprDto.setPackageId(mppr.getPackageId());
							mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDto.setServiceAmount(BigDecimal.valueOf(mppr.getRate()));
							mpprDto.setPackageConsumptionMasterId(mppr.getPackageConsumptionMasterId());
							mpprDto.setPacakgeConsumptionServiceDetailId(mppr.getPacakgeConsumptionServiceDetailId());
							mpprDto.setPackageConsumptionCapDetailsId(mppr.getPackageConsumptionCapDetailsId());
							mpprDto.setPackageConsumptionEODetailId(mppr.getPackageConsumptionEODetailId());
							mpprDto.setPackageAmount(BigDecimal.valueOf(mppr.getPackageAmount()));
							mpprDto.setIsCapService('N');

							OrderDetailsPackageMapperDto mpprDtoSum = new OrderDetailsPackageMapperDto();
							mpprDtoSum.setOrganizationId(sessionService.getOrganizationId());
							mpprDtoSum.setUnitId(unitId);
							mpprDtoSum.setStatus('A');
							mpprDtoSum.setIsPackageService(mppr.getIsPackageItem());
							mpprDtoSum.setPackageId(mppr.getPackageId());
							mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDtoSum.setServiceAmount(BigDecimal.valueOf(mppr.getRate()));
							mpprDtoSum.setPackageConsumptionMasterId(mppr.getPackageConsumptionMasterId());
							mpprDtoSum
									.setPacakgeConsumptionServiceDetailId(mppr.getPacakgeConsumptionServiceDetailId());
							mpprDtoSum.setPackageConsumptionCapDetailsId(mppr.getPackageConsumptionCapDetailsId());
							mpprDtoSum.setPackageConsumptionEODetailId(mppr.getPackageConsumptionEODetailId());
							mpprDtoSum.setPackageAmount(BigDecimal.valueOf(mppr.getPackageAmount()));
							mpprDtoSum.setIsCapService('N');
							packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);

							if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) {
								OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum
										.get(mppr.getPackageConsumptionMasterId());
								mpprDtofinal.setPackageAmount(mpprDtofinal.getPackageAmount()
										.add(BigDecimal.valueOf(mppr.getPackageAmount())));
								mpprDtofinal.setServiceAmount(
										mpprDtofinal.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
							} else {
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtoSum);
							}
						}
					}

					// pacakge
					List<OrderDetailsPackageMapperDto> ordDtlsPkgMppr = new ArrayList<OrderDetailsPackageMapperDto>();
					for (Entry<Integer, OrderDetailsPackageMapperDto> entry : packageMap.entrySet()) {

						OrderDetailsPackageMapperDto ordDto = entry.getValue();
						ordDto.setPackageAmount(ordDto.getPackageAmount().negate());
						ordDtlsPkgMppr.add(ordDto);
					}

					List<SaleDetailPayeeMapperDto> payeeList = GlobalDao.getSalePayeeMpperListbyDetailsIdReturn(
							saleDetailsDto.getId().intValue(), new BigDecimal(saleDetailsDto.getNetAmount()),
							saleDetail.getQuantity());
					// For Contract
					for (SaleDetailPayeeMapperDto mppr : payeeList) {

						if (contractMap.containsKey(mppr.getPayeeId())) {
							OrderDetailsPayeeMapperDto mpprDto = contractMap.get(mppr.getPayeeId());
							mpprDto.setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
							contractMap.put(mppr.getPayeeId(), mpprDto);
							if (contractMapForSum.containsKey(mppr.getPayeeId())) {
								OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(mppr.getPayeeId());
								mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
								contractMapForSum.put(mppr.getPayeeId(), mpprDtofinal);
							}
						} else {
							OrderDetailsPayeeMapperDto mpprDto = new OrderDetailsPayeeMapperDto();
							mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDto.setOrganizationId(sessionService.getOrganizationId());
							mpprDto.setUnitId(unitId);
							mpprDto.setStatus('A');
							mpprDto.setPayeeAmount(mppr.getPayeeAmount());
							mpprDto.setContractId(mppr.getContractId());
							mpprDto.setPayeeId(mppr.getPayeeId());

							OrderDetailsPayeeMapperDto mpprDtoSum = new OrderDetailsPayeeMapperDto();
							mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDtoSum.setOrganizationId(sessionService.getOrganizationId());
							mpprDtoSum.setUnitId(unitId);
							mpprDtoSum.setStatus('A');
							mpprDtoSum.setPayeeAmount(mppr.getPayeeAmount());
							mpprDtoSum.setContractId(mppr.getContractId());
							mpprDtoSum.setPayeeId(mppr.getPayeeId());

							contractMap.put(mppr.getPayeeId(), mpprDto);
							if (contractMapForSum.containsKey(mppr.getPayeeId())) {
								OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(mppr.getPayeeId());
								mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
								contractMapForSum.put(mppr.getPayeeId(), mpprDtofinal);
							} else {
								contractMapForSum.put(mppr.getPayeeId(), mpprDtoSum);
							}
						}

					}

					// add Cap to Package Contract
					CommonBillingItemsDto itemReq = new CommonBillingItemsDto();
					itemReq.setPatientId(sale.getPatient() != null ? sale.getPatient().getPatientId() : null);
					itemReq.setAdmissionId(sale.getAdmission() != null ? sale.getAdmission().getAdmissionId() : null);
					itemReq.setEncounterId(sale.getEncounter() != null ? sale.getEncounter().getEncounterId() : null);
					itemReq.setProductCategoryId(saleDetailsDto.getProductCategoryId());
					itemReq.setSaleDetailPackageMapperDtoList(packageList);
					itemReq.setSaleDetailPayeeMapperDtoList(payeeList);
					itemReq.setPreAuthExist(isPreAuthExsist);
					this.removeContractItems(itemReq);

					WalkInSalesDao.updateIssueDetails(saleDetail.getId());
					
					saleDetail.setIsActive(false);
					saleDetail.setIsDeleted(true);
				}
			}

			WalkInSalesDao.inactiveOrderDetailsBySaleId(sale.getId().intValue(), null);
			sale.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
			sale.setIsActive(false);
			sale.setIsDeleted(true);
			if(cancelledReasonId!=null)
			{
				sale.setCancelledReasonId(cancelledReasonId);
			}
			if(userId != null) {
				sale.setCancelledById(userId);
			}
			
			if(!cancelledRemark.equals("null"))
			{
				sale.setCancelledRemark(cancelledRemark);
			}
			Prescription prescription = sale.getPrescription();
			if (prescription != null) 
			{
				List<PrescriptionDetail> prescriptionDetails = prescription.getPrescriptionDetailsList();
				if (prescriptionDetails != null && !prescriptionDetails.isEmpty()) 
				{
					Integer totalIssuedQty = 0;
                    Integer totalOrderQty = 0;
					for (PrescriptionDetail pd : prescriptionDetails) 
					{
						for(IssueDetail issueDetail : pd.getIssueDetail())
						{
							if(!cancelledSaleDetailsIds.contains(issueDetail.getSaleDetail().getId()) && issueDetail.getIsActive())
							{
								totalIssuedQty = totalIssuedQty + issueDetail.getIssueQuantity();
							}
						}
						totalOrderQty=totalOrderQty+pd.getOrderQty();
						Status updatedStatus = new Status(com.param.common.constants.Status.New.getStatus());
						if(totalIssuedQty==0)
							updatedStatus = new Status(com.param.common.constants.Status.New.getStatus());
						else if(!totalIssuedQty.equals(totalOrderQty))
							updatedStatus = new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus());
						else if(totalIssuedQty.equals(totalOrderQty))
							updatedStatus = new Status(com.param.common.constants.Status.Completed.getStatus());
						
						for(IssueDetail issueDetail : pd.getIssueDetail())
						{
							if(!cancelledSaleDetailsIds.contains(issueDetail.getSaleDetail().getId()) && issueDetail.getIsActive())
							{
								WalkInSalesDao.updateIssueDetailsStatus(issueDetail.getSaleDetail().getId(),updatedStatus.getId());
							}
						}
						if(pd.getStatus()!=null)
						{
						Integer id = pd.getStatus().getId();
						if(id != null && id!= 9){
							pd.setStatus(updatedStatus);
						}
						}
						
						prescription.setStatus(updatedStatus);
					}
					WalkInSalesDao.updateIssue(saleId);
				}
			}
			
		} catch (Exception e) {
			throw e;
		}

	}

	private boolean revertAllocatedQuantity(Sale sale, Integer screenId, Integer unitId) {
		if (sale != null && sale.getSaleDetailsList() != null && !sale.getSaleDetailsList().isEmpty()) {
			List<SaleDetail> saleDetails = sale.getSaleDetailsList();
			for (SaleDetail saleDetail : saleDetails) {
				Long batchId = saleDetail.getBatch() != null ? saleDetail.getBatch().getId() : null;
				Integer storeId=sale.getStore().getStoreId();
				Screen screen = GlobalDao.getScreen(screenId);
				List<Batch> batchList = GlobalDao.getBatchList(storeId, batchId,
						saleDetail.getItemId(), unitId);
				Integer quantity = saleDetail.getQuantity();
                UomType uomType = new UomType(saleDetail.getUomType().getId());
                Batch batch1 = GlobalDao.getBatch(batchId);
                Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch1.getBatchUomList(), uomType);
                quantity=leaseQuantity;
				for (Batch batch : batchList) {
					if (quantity != null) {

						if (quantity > 0) {
							Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId,
									batch.getId(), saleDetail.getItemId());
							Integer actualQty = 0;
							Integer balanceQuantity = 0;
							if (batch.getQuantity() >= quantity) {
								actualQty = quantity;
								quantity = 0;
								balanceQuantity = batch.getCurrentQuantity() + actualQty;
							} else {
								quantity = quantity - batch.getCurrentQuantity();
								actualQty = batch.getCurrentQuantity();
								balanceQuantity = 0;
							}
							batch.setCurrentQuantity(balanceQuantity);
							batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock + actualQty);
							StockTransaction stockTransaction = new StockTransaction((avaialableStoreStock + actualQty), screen,
									screen.getCode(), actualQty, batch.getRate(),
									batch.getStoreStockList().get(0).getStore(), batch, 0);
							stockTransaction.setTransactionDetialsId(saleDetail.getId().intValue());
							stockTransaction.setTransactionId(sale.getId().intValue());
							stockTransaction.setIsTransit(false);
							stockTransaction.setInQty(actualQty);
							stockTransaction.setCreateDefaults(
									sale.getOrganizationId() != null ? sale.getOrganizationId()
											: sessionService.getOrganizationId(),
									unitId != null ? unitId : sessionService.getUnitId(),
									sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
							WalkInSalesDao.saveStockTransaction(stockTransaction);
						}

					}

				}

				
			}
			return true;
		}
		return false;
	}

	private boolean revertAllocatedItemQuantityScheduled(BatchAllocation batchAllocation, Integer screenId) {
		Batch batch = batchAllocation.getBatch();
		//System.out.println("Batch id : "+batch.getId());
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(batchAllocation.getStore().getStoreId(),
				batch.getId(), 0);// 0 itemId);

		// Batch batch = GlobalDao.getBatch(sessionService.getStoreId(), batchId);

		Integer quantity = batchAllocation.getQuantity();

		Integer balanceQuantity = batch.getCurrentQuantity() + quantity;

		if (avaialableStoreStock == null || avaialableStoreStock < 0) {
			avaialableStoreStock = 0;

		}

		batch.setCurrentQuantity(balanceQuantity);

		for (StoreStock s : batch.getStoreStockList()) {
			if (s.getStore().getStoreId().equals(batchAllocation.getStore().getStoreId())) {

				s.setAvailableQty((avaialableStoreStock + quantity) < 0 ? 0 : avaialableStoreStock + quantity);
				break;
			}
		}
		// batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock +
		// quantity);

		batchAllocation.setQuantity(0);
		batchAllocation.setBatch(batch);

		Screen screen = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
				quantity, batch.getRate(), batchAllocation.getStore(), batch, 0);

		stockTransaction.setUpdatedByUser(1);
		stockTransaction.setAddedByUser(1);
		stockTransaction.setOrganization(batchAllocation.getOrganization());
		stockTransaction.setUnit(batchAllocation.getUnit());

		WalkInSalesDao.saveStockTransaction(stockTransaction);

		Integer id = WalkInSalesDao.updateAllocatedQuantity(batchAllocation);
		return id == batchAllocation.getId();
	}

	private boolean revertAllocatedItemQuantity(BatchAllocation batchAllocation, WalkInSalesRequest request) {
		Long batchId = batchAllocation.getBatch().getId();

		// start code by minesh for revertAllocatedItemQuantity for club batch or
		// consignment batch(add if condation).
		Batch batchC = GlobalDao.getBatch(batchId);
		/*
		 * BigInteger consignmentCount =
		 * GlobalDao.consignmentBatchCount(batchC.getItem().getId(),
		 * batchC.getBatchNo(), batchC.getLotNo() != null ? batchC.getLotNo() : null,
		 * null, request.getUnitId());
		 */
		/*
		 * if (consignmentCount.longValue() > 0) { return
		 * revertAllocatedItemQuantityForClubBatch(batchAllocation, request); } else {
		 */
		// end code by minesh for revertAllocatedItemQuantity for club batch or
		// consignment batch

		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(request.getStoreId(), batchId, 0);// 0
																										// itemId
		//System.out.println("Batch id : "+batchId);
		Batch batch = GlobalDao.getBatch(request.getStoreId(), batchId, request.getUnitId());

		Integer quantity = batchAllocation.getQuantity();

		Integer balanceQuantity = batch.getCurrentQuantity() + quantity;

		batch.setCurrentQuantity(balanceQuantity);
		batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock + quantity);

		batchAllocation.setQuantity(0);
		batchAllocation.setBatch(batch);

		Screen screen = GlobalDao.getScreen(request.getScreenId());

		StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
				quantity, batch.getRate(), new Store(request.getStoreId()), batch, 0);
		stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		WalkInSalesDao.saveStockTransaction(stockTransaction);

		Integer id = WalkInSalesDao.updateAllocatedQuantity(batchAllocation);
		return id == batchAllocation.getId();
		// }
	}

	// created by minesh for consignment batch or club batch
	private boolean revertAllocatedItemQuantityForClubBatch(BatchAllocation batchAllocation,
			WalkInSalesRequest request) {
		boolean updatedFlag = false;
		List<Long> consignmentBatchIds = GlobalDao.getConsignmentBatchIds(batchAllocation.getBatch().getId(),
				request.getStoreId(), request.getUnitId());
		for (BatchAllocation batchAllocationObj : WalkInSalesDao
				.getBatchAllocationByTempBillId(batchAllocation.getTempBillId())) {
			Long batchId = batchAllocationObj.getBatch().getId();
			for (Long consignmentBatchId : consignmentBatchIds) {
				if (consignmentBatchId.equals(batchId) && batchAllocationObj.getQuantity() > 0) {
					Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(request.getStoreId(), batchId, 0);
					Batch batch = GlobalDao.getBatch(request.getStoreId(), batchId, request.getUnitId());
					Integer quantity = batchAllocationObj.getQuantity();
					Integer balanceQuantity = batch.getCurrentQuantity() + quantity;
					batch.setCurrentQuantity(balanceQuantity);
					batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock + quantity);
					batchAllocationObj.setQuantity(0);
					batchAllocationObj.setBatch(batch);

					Screen screen = GlobalDao.getScreen(request.getScreenId());
					StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen,
							screen.getCode(), quantity, batch.getRate(), batch.getStoreStockList().get(0).getStore(),
							batch, 0);
					stockTransaction.setCreateDefaults(batch.getOrganizationId(), batch.getUnitId(),
							batch.getAddedByUser());
					WalkInSalesDao.saveStockTransaction(stockTransaction);
					WalkInSalesDao.updateAllocatedQuantity(batchAllocationObj);
					updatedFlag = true;
					break;
				}
			}
		}

		return updatedFlag;
	}

	private boolean revertAllocatedItemQuantityForClubBatchWalkin(BatchAllocation batchAllocation, Integer screenId) {
		boolean updatedFlag = false;
		List<Long> consignmentBatchIds = GlobalDao.getConsignmentBatchIds(batchAllocation.getBatch().getId(),
				sessionService.getStoreId(), batchAllocation.getUnitId());
		for (BatchAllocation batchAllocationObj : WalkInSalesDao
				.getBatchAllocationByTempBillId(batchAllocation.getTempBillId())) {
			Long batchId = batchAllocationObj.getBatch().getId();
			for (Long consignmentBatchId : consignmentBatchIds) {
				if (consignmentBatchId.equals(batchId) && batchAllocationObj.getQuantity() > 0) {
					Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(sessionService.getStoreId(), batchId,
							0);
					Batch batch = GlobalDao.getBatch(sessionService.getStoreId(), batchId, batchAllocation.getUnitId());
					Integer quantity = batchAllocationObj.getQuantity();
					Integer balanceQuantity = batch.getCurrentQuantity() + quantity;
					batch.setCurrentQuantity(balanceQuantity);
					batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock + quantity);
					batchAllocationObj.setQuantity(0);
					batchAllocationObj.setBatch(batch);

					Screen screen = GlobalDao.getScreen(screenId);
					StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen,
							screen.getCode(), quantity, batch.getRate(), batch.getStoreStockList().get(0).getStore(),
							batch, 0);

					stockTransaction.setCreateDefaults(batch.getOrganizationId(), batch.getUnitId(),
							batch.getAddedByUser());
					WalkInSalesDao.saveStockTransaction(stockTransaction);
					WalkInSalesDao.updateAllocatedQuantity(batchAllocationObj);
					updatedFlag = true;
					break;
				}
			}
		}

		return updatedFlag;
	}

	private CommonResponse updateEntities(BatchAllocation batchAllocation, WalkInSalesRequest request)
			throws CustomException {
		Long batchId = request.getItem().getBatchId();

		// start code by minesh for revertAllocatedItemQuantity for club batch or
		// consignment batch(add if condation).
		Batch batchC = GlobalDao.getBatch(batchId);
		/*
		 * BigInteger consignmentCount =
		 * GlobalDao.consignmentBatchCount(request.getItem().getItemId(), (batchC !=
		 * null) ? batchC.getBatchNo() : null, (batchC != null && batchC.getLotNo() !=
		 * null) ? batchC.getLotNo() : null, null, request.getUnitId()); if
		 * (consignmentCount.longValue() > 0) { return
		 * updateEntitiesForClubBatch(batchAllocation, request); } else {
		 */
		// end code by minesh for revertAllocatedItemQuantity for club batch or
		// consignment batch

		Integer allocationQuantity = request.getItem().getQuantity();
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(request.getStoreId(), batchId,
				request.getItem().getItemId());// 0 itemId
		Integer balanceQty = null;
		if (avaialableStoreStock >= allocationQuantity) {
			Batch batch = GlobalDao.getBatch(request.getStoreId(), batchId, request.getUnitId());
			Integer balanceQuantity = batch.getCurrentQuantity() - allocationQuantity;
			batch.setCurrentQuantity(balanceQuantity);
			batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock - allocationQuantity);
			balanceQty = avaialableStoreStock - allocationQuantity;
			batchAllocation.setQuantity(request.getItem().getQuantity());
			batchAllocation.setBatch(batch);

			Screen screen = GlobalDao.getScreen(request.getScreenId());

			StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					Math.abs(allocationQuantity), batch.getRate(), batch.getStoreStockList().get(0).getStore(), batch,
					0);
			stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			WalkInSalesDao.saveStockTransaction(stockTransaction);

			CommonResponse response = new CommonResponse();
			response.setId(WalkInSalesDao.updateAllocatedQuantity(batchAllocation).longValue());
			response.setObject(balanceQty);
			response.setMessage("Item quantity updated successfully");

			return response;
		} else {
			throw new CustomException("Quantity entered is exceeding max quantity");
		}
		// }
	}

	// created by minesh for consignment or club batch
	private CommonResponse updateEntitiesForClubBatch(BatchAllocation batchAllocation, WalkInSalesRequest request)
			throws CustomException {
		long batchAllocationId = 0;
		if (revertAllocatedItemQuantity(batchAllocation, request)) {
			Long batchId = request.getItem().getBatchId();
			List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batchId,
					request.getStoreId(), request.getUnitId());
			Integer totalAvailableQty = StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum();
			int allocationQtyFromRequest = request.getItem().getQuantity();
			if (allocationQtyFromRequest <= totalAvailableQty) {
				for (StoreStock storeStockObj : StoreStockList) {
					Integer storeAvaliableQuantity = storeStockObj.getAvailableQty();
					Long batchIdFromStore = storeStockObj.getBatch().getId();

					if (allocationQtyFromRequest != 0) {
						if (allocationQtyFromRequest <= storeAvaliableQuantity) {
							allocateItemQuantityForClubBatch(request, allocationQtyFromRequest, storeAvaliableQuantity,
									batchIdFromStore);

							allocationQtyFromRequest = 0;
							break;
						} else {
							int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
							allocateItemQuantityForClubBatch(request, storeAvaliableQuantityINT, storeAvaliableQuantity,
									batchIdFromStore);
							allocationQtyFromRequest = allocationQtyFromRequest - storeAvaliableQuantityINT;
						}
					}
				}
			} else {
				throw new CustomException("Quantity entered is exceeding max quantity");
			}

		}

		CommonResponse response = new CommonResponse();
		response.setId(batchAllocationId);
		response.setMessage("Item quantity updated successfully");
		return response;
	}

	@Override
	@Transactional
	public CommonResponse saveWalkInPrescription(WalkInPrescriptionReq request) {
		CommonResponse c = new CommonResponse();
		TempPatientRegistration tempPatient = new TempPatientRegistration();
		TempDoctor tempDoctor = new TempDoctor();
		Prescription prescription = null;

		tempPatient = new TempPatientRegistration(
				request.getPatientAddress() != null ? request.getPatientAddress() : "", request.getPatientAge(),
				new Gender(request.getPatientGenderId()), request.getPatientName(), new Prefix(request.getPrefixId()),
				"-", request.getMobileNumber());

		tempPatient.setCreateDefaults(
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId(),
				request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		if (request.getPatientId() != null) {
			PatientRegistration patient = GlobalDao.getPatient(request.getPatientId());
			tempPatient.setTempUhid(patient.getUhidNumber());
			tempPatient.setAddress("_");
		}

		if (request.getDoctorId() == null) {
			if (request.getDoctorName() != null) {
				tempDoctor.setDoctorName(request.getDoctorName());
				tempDoctor.setDoctorAddress(request.getDoctorAddress());
				tempDoctor.setCreateDefaults(
						request.getOrganizationId() != null ? request.getOrganizationId()
								: sessionService.getOrganizationId(),
						request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
						request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			}

		}
		prescription = new Prescription();
		String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, sessionService.getUnitId(), 164);
		if (request.getVisitTypeId() != null)
			prescription.setVisitType(new VisitType(request.getVisitTypeId()));
		prescription.setPrescriptionNo(prescriptionNo);
		prescription.setIsIntervention(FALSE);
		prescription.setIsActive(TRUE);
		prescription.setIsDeleted(FALSE);
		prescription.setIsValidate(FALSE);
		prescription.setIsDischarge(FALSE);
		prescription.setReconciliationDone(FALSE);
		prescription.setIsNonFormulary('N');
		prescription.setStatus(new Status(com.param.common.constants.Status.New.getStatus()));
		prescription.setUpdatedByUser(sessionService.getUserId());
		prescription.setAuthorisedBy(sessionService.getUserId());
		prescription.setAuthorisedDate(new Timestamp(TimeZoneComponent.getDateByzone(request.getUnitId()).getTime()));
		prescription.setPrescriptionDate(new Timestamp(TimeZoneComponent.getDateByzone(request.getUnitId()).getTime()));
		prescription.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		prescription.setTempPatient(tempPatient);
		if (tempDoctor.getDoctorName() != null) {
			prescription.setTempDoctor(tempDoctor);
		}

		// Set Normal by default - Divyesh
		prescription.setPriority(new Priority(2));
		// sessionService.setEntityDefaults(prescription);
		prescription.setCreateDefaults(
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId(),
				request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

		for (WalkInPrescriptionItemsReq item : request.getItems()) {
			PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
			prescriptionDetail.setIsDiscontinued(FALSE);
			prescriptionDetail.setIsPreStop(FALSE);
			prescriptionDetail.setIsIntervention(FALSE);
			prescriptionDetail.setIsDeleted(FALSE);
			prescriptionDetail.setIsActive(TRUE);
			prescriptionDetail.setIsNonFormulary('N');
			prescriptionDetail.setStatus(new Status(com.param.common.constants.Status.New.getStatus()));
			prescriptionDetail.setPrnFlag(FALSE);
			if (item.getItemId() != null)
				prescriptionDetail.setItem(new Item(item.getItemId()));
			prescriptionDetail.setStatFlag(false);
			prescriptionDetail.setInfusionFlag(FALSE);
			prescriptionDetail.setIsMedicationStop('N');
			prescriptionDetail.setStore(new Store(sessionService.getStoreId()));
			prescriptionDetail.setOrderQty(item.getQuantity());
			if (item.getInstructionId() != null)
				prescriptionDetail.setInstruction(new Instruction(item.getInstructionId()));

			List<PrescriptionFrequencyDetail> prescriptionFrequencyDetails = new ArrayList<PrescriptionFrequencyDetail>();
			PrescriptionFrequencyDetail freqDtl = new PrescriptionFrequencyDetail();
			freqDtl.setDose(item.getDose());
			freqDtl.setFrequency(new Frequency(item.getFrequencyId()));
			if (item.getDoseUnitId() != null) {
				freqDtl.setStrengthUnit(new StrengthUnit(item.getDoseUnitId()));
			}
			freqDtl.setPrescriptionDetail(prescriptionDetail);
			freqDtl.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			prescriptionFrequencyDetails.add(freqDtl);
			prescriptionDetail.setPrescriptionFrequencyDetails(prescriptionFrequencyDetails);
			prescriptionDetail.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			prescription.addPrescriptionDetails(prescriptionDetail);
			// sessionService.setEntityDefaults(prescriptionDetail);

		}

		WalkInSalesDao.savePrescription(prescription);
		c.setId(prescription.getId());
		c.setMessage("Prescription saved successfully.");
		return c;
	}

	private CommonResponse createSale(WalkInSalesRequest request) throws Exception {
		try {
			isDummyService = request.getIsDummyService();
			if (request.getIsVirtualStore() == null) {

				List<com.param.common.dto.CustomException> errors = new ArrayList<com.param.common.dto.CustomException>();
				List<Long> batchIds = request.getItems().stream().map(CommonBillingItemsDto::getBatchId).collect(Collectors.toList());
				List<Batch> batchAll = GlobalDao.getBatchAll(batchIds);
                List<CommonBillingItemsDto> grnInfoList = GlobalDao.getGrnInfoByBatchId(batchIds);
				List<StoreStock> storeStockAll = GlobalDao.getStoreStockByBatchAndStoreAll(batchIds, request.getStoreId(), request.getUnitId());

				Map<Long, Long> frequencyMap = batchIds.stream()
						.collect(Collectors.groupingBy(x -> x, Collectors.counting()));

				List<Long> duplicates = frequencyMap.entrySet().stream()
						.filter(entry -> entry.getValue() > 1)
						.map(Map.Entry::getKey)
						.collect(Collectors.toList());

				for (CommonBillingItemsDto item : request.getItems()) {
					Integer duplicateQty = 0;
					Integer quantity = item.getQuantity();
					Long batchId = item.getBatchId();
					Integer storeId = request.getStoreId();
					UomType uomType = new UomType(item.getUomTypeId());
					Integer unitId = request.getUnitId();
					if (quantity > 0 && request.getUnitId() != null && request.getOrganizationId() != null) {

						Batch batch = batchAll.stream()
								.filter(order -> batchId.equals(order.getId()))
								.findAny()
								.orElse(null);
						if (batch != null) {

							for (Long batId : duplicates) {
								for (CommonBillingItemsDto item1 : request.getItems()) {
									if (item.getBatchId() == batId.intValue()) {
										UomType uomTypeNew = new UomType(item.getUomTypeId());
										duplicateQty += Util.GetLeaseQuantity(item1.getQuantity(), batch.getBatchUomList(), uomTypeNew);
									}
								}
								;
							}
							Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

							StoreStock storeStockList = storeStockAll.stream()
									.filter(order -> batchId.equals(order.getBatch().getId()))
									.findAny()
									.orElse(null);

                            StoreStock storeStockList1 = GlobalDao.getStoreStockByBatchAndStore1(batchId, storeId, unitId);

                            if(grnInfoList != null && !grnInfoList.isEmpty()){
                                CommonBillingItemsDto grn = grnInfoList.stream()
                                        .filter(order -> batchId.equals(order.getBatchId()))
                                        .findAny()
                                        .orElse(null);

                                if(grn != null && grn.getGrnId() != null && grn.getGrnId() > 0){
                                    item.setGrnId(grn.getGrnId());
                                    item.setGrnNumber(grn.getGrnNumber());
                                }
                            }

							if (storeStockList == null) {

							} else {
								if (storeStockList.getAvailableQty() < leaseQuantity || storeStockList.getAvailableQty() < duplicateQty) {

									com.param.common.dto.CustomException e = new com.param.common.dto.CustomException(
											"Stock is not available", storeStockList.getAvailableQty(),
											(batch.getBatchNo() != null ? batch.getBatchNo() : "NA"));
									errors.add(e);

								}
							}

						}

					}
				}
				if (!errors.isEmpty() && errors.size() > 0) {
					throw new com.param.common.dto.CustomException("Stock is not available", errors);
				}
			}

			TempPatientRegistration tempPatient = null;
			TempDoctor tempDoctor = new TempDoctor();
			Prescription prescription = null;
			Sale sale = null;

			if (request.getPatientId() == null && request.getPaymentEntitlementId() != null
					&& request.getPaymentEntitlementId() == SELF) {
				Response rs = this.savePatient(request);
				PatientRegistrationDto p = (PatientRegistrationDto) rs.getObject();
				request.setTempPatientId(p.getTempPatientId());
			}

			if (request.getTempPatientId() != null && request.getTempPatientId() > 0) {
				tempPatient = GlobalDao.getPatientById(request.getTempPatientId());
			}

			if (request.getDoctorId() == null) {
				tempDoctor.setDoctorAddress(request.getDoctorAddress());
				tempDoctor.setDoctorName(request.getDoctorName());
				tempDoctor.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			}
			Integer tempDoctorId = null;
			Integer mainDoctorId = null;
			if (request.getDoctorType() != null && request.getDoctorType() == 0) {
				mainDoctorId = request.getDoctorId();
			}
			if (request.getDoctorType() != null && request.getDoctorType() == 1) {
//    	   tempDoctor.setId(request.getDoctorId());
				tempDoctorId = request.getDoctorId();
			}

			if (request.getDoctorType() == null && request.getDoctorId() != null) {
				mainDoctorId = request.getDoctorId();
			}

			Store store = new Store(request.getStoreId());

			String billNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
					request.getScreenId());

			if (request.getVisitTypeId() == null) {
				request.setSaleTypeId(3);  // Non Encounter Patient saletype id will be 3
			}
			Doctor doctor = null;
			if (mainDoctorId != null && mainDoctorId > 0) {
				doctor = new Doctor(mainDoctorId);
			}


			if (request.getDoctorType() == null && request.getDoctorId() == null) {


				sale = new Sale((request.getEncounterId() != null) ? new Encounter(request.getEncounterId()) : null,
						(request.getAdmissionId() != null) ? new Admission(request.getAdmissionId()) : null, billNumber,
						request.getCounterId() != null ? request.getCounterId() : null, request.getDiscount(),
						(request.getDiscountCategoryId() != null) ? new DiscountCategory(request.getDiscountCategoryId())
								: null,
						(request.getDiscountTypeId() != null) ? new DiscountType(request.getDiscountTypeId()) : null,
						doctor, request.getNetAmount(),
						(request.getOrderId() != null) ? new Order(request.getOrderId()) : null, request.getNetAmount(), 0.0,
						(request.getPatientId() != null) ? new PatientRegistration(request.getPatientId()) : null,
						request.getRoundOff(), new Status(com.param.common.constants.Status.Pending.getStatus()), store,
						request.getTaxAmount(), request.getTotalAmount(), request.getTotalDiscountAmount(),
						(request.getVisitTypeId() != null) ? new VisitType(request.getVisitTypeId()) : null,
						new SaleType(request.getSaleTypeId()), (request.getDoctorName() != null) ? tempDoctor : null,
						tempPatient, (request.getPrescriptionId() != null)
						? new Prescription(request.getPrescriptionId().longValue()) : null,
						request.getTotalSelfPayable(), request.getTotalCreditPayable(), request.getRemark());
//	        sale.setConsolidatedBill(request.getIsConsolidatedFlag());
			} else {
				sale = new Sale((request.getEncounterId() != null) ? new Encounter(request.getEncounterId()) : null,
						(request.getAdmissionId() != null) ? new Admission(request.getAdmissionId()) : null, billNumber,
						request.getCounterId() != null ? request.getCounterId() : null, request.getDiscount(),
						(request.getDiscountCategoryId() != null) ? new DiscountCategory(request.getDiscountCategoryId())
								: null,
						(request.getDiscountTypeId() != null) ? new DiscountType(request.getDiscountTypeId()) : null,
						doctor, request.getNetAmount(),
						(request.getOrderId() != null) ? new Order(request.getOrderId()) : null, request.getNetAmount(), 0.0,
						(request.getPatientId() != null) ? new PatientRegistration(request.getPatientId()) : null,
						request.getRoundOff(), new Status(com.param.common.constants.Status.Pending.getStatus()), store,
						request.getTaxAmount(), request.getTotalAmount(), request.getTotalDiscountAmount(),
						(request.getVisitTypeId() != null) ? new VisitType(request.getVisitTypeId()) : null,
						new SaleType(request.getSaleTypeId()),
						tempPatient, (request.getPrescriptionId() != null)
						? new Prescription(request.getPrescriptionId().longValue()) : null,
						request.getTotalSelfPayable(), request.getTotalCreditPayable(), request.getRemark());
//	        sale.setConsolidatedBill(request.getIsConsolidatedFlag());
			}


			sale.setSearchPatient(request.getSearchPatient() != null ? request.getSearchPatient() : false);
			sale.setScreenId(request.isOpSales() ? com.param.common.constants.Screen.SaleWorkList.getScreen() : com.param.common.constants.Screen.WalkinSale.getScreen());
			sale.setIsVirtualStore(request.getIsVirtualStore() != null ? request.getIsVirtualStore() : false);
			prescription = WalkInSalesDao
					.getPrescription(request.getPrescriptionId() != null ? request.getPrescriptionId().longValue() : null);

			SaleDetail saleDetail = null;
			boolean isPreAuthExsist = iContractConsumptionDao.getPreAuthAmutByVisitId(request.getEncounterId(),
					request.getAdmissionId());
			List<Integer> parentItemIds = request.getItems().stream().map(CommonBillingItemsDto::getParentItemId).collect(Collectors.toList());
			Stream<Integer> allLotNosStream = parentItemIds.stream();
			parentItemIds = allLotNosStream
					.filter(str -> str != null && str > 0)
					.collect(Collectors.toList());
			List<PrescriptionDetailsForOPSalesWorklistDto> prescriptionDtlAll = GlobalDao
					.getPresDtlsByPresIdForWalkinSales(request.getPrescriptionId(), parentItemIds);
			for (CommonBillingItemsDto item : request.getItems()) {
				Integer unitId = request.getUnitId();
				item.setPreAuthExist(isPreAuthExsist);
				Integer prescriptionDetailId = null;
				if (item.getPrescriptionDetailId() != null) {
					prescriptionDetailId = item.getPrescriptionDetailId();
				}

				int parentFlag = 0;
				if ((item.getPrescriptionDetailId() == null && !request.isWalkInSales())
						&& item.getParentItemId() != null) {

					PrescriptionDetailsForOPSalesWorklistDto prescriptionDtl = prescriptionDtlAll.stream()
							.filter(order -> item.getParentItemId().equals(order.getItemId()))
							.findAny()
							.orElse(null);

					PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
					prescriptionDetail.setAuthorisedBy(
							prescriptionDtl.getAuthorisedBy() != null ? prescriptionDtl.getAuthorisedBy() : null);
					prescriptionDetail.setAuthorisedDate(prescriptionDtl.getAuthorisedDate());
					prescriptionDetail.setPendingQty(prescriptionDtl.getPendingQty());
					prescriptionDetail.setOrderQty(prescriptionDtl.getOrderQty());
					prescriptionDetail.setPrescriptionDate(prescriptionDtl.getPrescriptionDate());
					if (prescriptionDtl.getInstructionId() != null) {
						Instruction ins = new Instruction();
						ins.setId(prescriptionDtl.getInstructionId());
						prescriptionDetail.setInstruction(ins);
					}
					if (prescriptionDtl.getInstructionDesc() != null) {
						prescriptionDetail.setInstructionDesc(prescriptionDtl.getInstructionDesc());
					}
					if (prescriptionDtl.getInstructionText() != null) {
						prescriptionDetail.setInstructionDesc(prescriptionDtl.getInstructionText());
					}
					Store store1 = new Store();
					store1.setStoreId(request.getStoreId());
					prescriptionDetail.setStore(store1);
					prescriptionDetail.setIsDiscontinued(false);
					prescriptionDetail.setIsIntervention(false);
					prescriptionDetail.setIsPreStop(false);
					prescriptionDetail.setIsDeleted(false);
					prescriptionDetail.setIsActive(true);
					prescriptionDetail.setStatFlag(false);
					prescriptionDetail.setIsMedicationStop('N');
					prescriptionDetail.setStrength(prescriptionDtl.getStrength());
					prescriptionDetail.setAddedDate(TimeZoneComponent.getDateByzone(unitId));
					prescriptionDetail.setInfusionFlag(prescriptionDtl.getInfusionflag());
					Item itm = new Item();
					itm.setId(item.getItemId());
					prescriptionDetail.setItem(itm);
					prescriptionDetail.setParentSubstituteId(prescriptionDtl.getPrescriptionDetailsid());
					Prescription pres = new Prescription();
					pres.setId(prescriptionDtl.getPrescriptionId().longValue());
					prescriptionDetail.setPrescription(pres);
					prescriptionDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());
					// sessionService.setEntityDefaults(prescriptionDetail);
					prescriptionDetail = GlobalDao.savePrescriptionDetailForSubstitute(prescriptionDetail);

					PrescriptionFrequencyDetail pfd = new PrescriptionFrequencyDetail();

					PrescriptionDetail pd = new PrescriptionDetail();
					pd.setId(prescriptionDetail.getId().longValue());
					pfd.setPrescriptionDetail(pd);
					pfd.setStartDate(prescriptionDtl.getStartDate());
					pfd.setStopDate(prescriptionDtl.getStopDate());
					if (prescriptionDtl.getRouteId() != null) {
						RouteOfAdministration roa = new RouteOfAdministration();
						roa.setId(prescriptionDtl.getRouteId());
						pfd.setRouteOfAdministration(roa);
					}
					StrengthUnit strnUnit = new StrengthUnit();
					if (prescriptionDtl.getDoseUnitId() != null) {
						strnUnit.setId(prescriptionDtl.getDoseUnitId());
						pfd.setStrengthUnit(strnUnit);
					}
					pfd.setDose(prescriptionDtl.getDose());
					Frequency freq = new Frequency();
					freq.setId(prescriptionDtl.getFrequencyId());
					pfd.setFrequency(freq);
					pfd.setIsActive(true);
					pfd.setIsDeleted(false);
					StrengthUnit strngth = new StrengthUnit();
					if (prescriptionDtl.getDoseUnitId() != null) {
						strngth.setId(prescriptionDtl.getDoseUnitId());
						pfd.setStrengthUnit(strngth);
					}
					pfd.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
					// sessionService.setEntityDefaults(pfd);
					pfd = GlobalDao.savePrescriptionFrequencyDetail(pfd);

					prescriptionDetailId = prescriptionDetail.getId().intValue();
					parentFlag = 1;
				}

				Batch batch = null;
				if (item.getBatchId() != null && item.getBatchId() > 0)
					batch = new Batch(item.getBatchId());

				saleDetail = new SaleDetail(batch, item.getDiscountAmount(), item.getLeaseQuantity(), item.getNetAmount(),
						null,
						(prescriptionDetailId != null) ? new PrescriptionDetail(new Long(prescriptionDetailId), parentFlag)
								: null,
						item.getQuantity(), item.getRate(), item.getTaxAmount(), new Tax(item.getTaxId()),
						item.getTaxPercentage(), item.getTotalAmount(), item.getUnitDiscountAmount(),
						new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
						new Status(com.param.common.constants.Status.Pending.getStatus()), item.getSelfPayable(),
						item.getCreditPayable(), item.getPayeeId(), item.getProductCategoryId(),
						item.getPackageConsumptionAmt());
				saleDetail.setConsessionPercentage(item.getConsessionPercentage());
				saleDetail.setAtCop(item.getAtCop());
				saleDetail.setConsessionAmount(item.getConsessionAmount());
				saleDetail.setUnitConsessionAmount(item.getUnitConsessionAmount());
				saleDetail.setItemId(item.getItemId());
				saleDetail.setConsumedItemId(item.getConsumedItemId());
				saleDetail.setConsumedPcategroyId(item.getConsumedPcategroyId());
				saleDetail.setInstructionId(item.getInstructionId() != null ? item.getInstructionId() : null);
				saleDetail.setInstructionDesc(item.getInstructionDesc() != null ? item.getInstructionDesc() : null);
				saleDetail.setIsDummyService(request.getIsDummyService() == true ? request.getIsDummyService() : false);
                saleDetail.setGrnId(item.getGrnId() != null ? item.getGrnId() : null);
                saleDetail.setGrnNumber(item.getGrnNumber() != null ? item.getGrnNumber() : null);
                saleDetail.setGrnNumber(item.getGrnNumber() != null ? item.getGrnNumber() : null);
                saleDetail.setItemNetAmount(item.getRate() != null ? item.getRate() : null);
				// For package
				List<SaleDetailPackageMapperDto> packageMppr = item.getSaleDetailPackageMapperDtoList();
				if (packageMppr != null && packageMppr.size() > 0) {
					List<SaleDetailPackageMapper> pkgMpprList = new ArrayList<SaleDetailPackageMapper>();
					for (SaleDetailPackageMapperDto dto : packageMppr) {
						SaleDetailPackageMapper mpper = new SaleDetailPackageMapper();
						mpper.setOrganizationId(request.getOrganizationId());
						mpper.setUnitId(request.getUnitId());
						mpper.setItemId(item.getItemId());
						mpper.setStatus('A');
						mpper.setIsPackageItem(dto.getIsPackageItem());
						mpper.setPackageId(dto.getPackageId());
						mpper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
						mpper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
						mpper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
						mpper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
						mpper.setPackageAmount(BigDecimal.valueOf(dto.getPackageAmount()));
						mpper.setPackageQty(dto.getPackageQty());
						mpper.setCapOrQty(dto.getCapOrQty());
						mpper.setRate(dto.getRate() != null ? BigDecimal.valueOf(dto.getRate()) : BigDecimal.ZERO);
						mpper.setSaleDetail(saleDetail);
						pkgMpprList.add(mpper);
					}
					saleDetail.setSaleDetailPackageMapperList(pkgMpprList);
				}

				// For Contract
				List<SaleDetailPayeeMapperDto> payeeMapper = item.getSaleDetailPayeeMapperDtoList();
				if (payeeMapper != null && payeeMapper.size() > 0) {
					List<SaleDetailPayeeMapper> payeeList = new ArrayList<SaleDetailPayeeMapper>();
					for (SaleDetailPayeeMapperDto dto : payeeMapper) {
						SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
						mpper.setOrganizationId(request.getOrganizationId());
						mpper.setUnitId(request.getUnitId());
						mpper.setItemId(item.getItemId());
						mpper.setStatus('A');
						mpper.setContractId(dto.getContractId());
						mpper.setPayeeId(dto.getPayeeId());
						mpper.setAssociateCompanyId(dto.getAssociateCompanyId());
						mpper.setPayeeAmount(dto.getPayeeAmount());
						mpper.setQuantity(dto.getQuantity());
						mpper.setSaleDetail(saleDetail);
						saleDetail.setPayeeId(dto.getPayeeId());
						payeeList.add(mpper);
					}
					saleDetail.setSaleDetailPayeeMapperList(payeeList);
				}
				saleDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				// sessionService.setEntityDefaults(saleDetail);
				if (request.getOrderDate() != null) {
					SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
					Date today = formatter2.parse(request.getOrderDate());
					sale.setAddedDate(today);
					saleDetail.setAddedDate(today);
				}
				sale.addSaleDetailsList(saleDetail);

				if (prescription != null) {
					for (PrescriptionDetail prescriptionDetail : prescription.getPrescriptionDetailsList()) {
						if (item.getPrescriptionDetailId() != null
								&& item.getPrescriptionDetailId().equals(prescriptionDetail.getId())) {
							prescriptionDetail.updatePrescriptionDetail(new Status(item.getStatusId()));
						}
					}
				}

				// actual cap consumption
				item.setEncounterId(request.getEncounterId());
				item.setAdmissionId(request.getAdmissionId());
				item.setPatientId(request.getPatientId());
				this.consumePackageContractItems(item);

			}

			// }

			if (prescription != null) {
				prescription.updatePrescription(new Status(request.getStatusId()));
			}
			if (prescription != null) {
				WalkInSalesDao.updatePrescription(prescription);
			}
			sale.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());

			if (request.getIsVirtualStore() != null) {
				SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
				Date today = formatter2.parse(request.getOrderDate());
				sale.setAddedDate(today);
			}

			sale.setCop(request.getAtCopToSave());
			sale.setEntitlementId(request.getEntitlementId());
			// sessionService.setEntityDefaults(sale);
			CommonResponse globalSaveRes = this.saveGlobalSale(sale);
			if (!request.isWalkInSales()
					|| (request.getAdmissionId() != null && request.getAdmissionId() > 0 && !request.isAddToBill())) {

				if (request.getAdmissionId() != null && request.getAdmissionId() > 0) {
					request.getItems().stream()
							.forEach(item -> item.setStatusId(com.param.common.constants.Status.Closed.getStatus()));
				}

				//System.out.println(sale.toString() + "" + sale.getClass());// NitinSysoutComment
				sale.setId(globalSaveRes.getId());
				this.saveIssueAndIssueDetails(sale, request);
			}
			if (request.getTempBillId() != null) {
				// WalkInSalesDao.deleteBatchAllocations(request.getTempBillId());
			}
			if (request.getPrescriptionId() != null) {
				// WalkInSalesDao.deleteBatchAllocations(request.getPrescriptionId().longValue());
			}
			if (tempDoctorId != null) {
				WalkInSalesDao.updateTempDoctorId(sale.getId(), tempDoctorId);
			}

			try {

				if (sale.getDoctor() != null) {
					if (sale.getDoctor().getDoctorId() != null) {
						Integer doctorId = sale.getDoctor().getDoctorId();
						WalkInSalesDao.updateDoctorId(sale.getId(), doctorId);
					}
				}
			} catch (Exception e) {
				throw e;
			}
			CommonResponse response = new CommonResponse();

			response.setId(sale.getId());
			response.setMessage("Walk in sales bill save successfully");
			if (globalSaveRes != null && globalSaveRes.getIds() != null && globalSaveRes.getIds().size() > 0)
				response.setIds(globalSaveRes.getIds());

			return response;
		}catch (Exception ex) {
			throw ex;
		}
	}

	private void saveStoreStockDetails1(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer unitId, WalkInSalesRequest pendingIssuesSave) throws Exception {

		if (quantity > 0) {

			Batch batch = GlobalDao.getBatch(batchId);
			if (batch != null) {
				Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
				Integer tQuantity = 0;
				Integer availableQuantity = 0;
				Integer currentQuantity = 0;
				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore1(batchId, storeId, unitId);
				StoreStock storeStockDetail = null;
				if (storeStockList == null) {
				} else {

					if (storeStockList.getAvailableQty() < leaseQuantity) {
						// throw new CustomException("Stock is not available with batch " +
						// batch.getBatchNo());
						throw new CustomException(batch.getBatchNo() != null ? batch.getBatchNo() : "NA");
					}
					tQuantity = leaseQuantity + storeStockList.gettQty();
					availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
					if (availableQuantity < 0) {
						availableQuantity = 0;
					}
					storeStockList.setAvailableQty(availableQuantity);

					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
				}
				Batch batchObject = GlobalDao
						.getBatch(pendingIssuesSave.getStoreId() != null ? pendingIssuesSave.getStoreId()
								: sessionService.getStoreId(), storeStockDetail.getBatch().getId(), unitId);
				currentQuantity = batchObject.getCurrentQuantity() - leaseQuantity;
				batchObject.setCurrentQuantity(currentQuantity);
				GlobalDao.updateBatchLeaseQuantity(batchObject);
				saveStockTransaction(leaseQuantity, storeStockDetail, pendingIssuesSave);
			}

		}

	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStockDetail,
			WalkInSalesRequest pendingIssuesSave) {

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(1);
		StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStockDetail.getBatch().getRate(), new Store(sessionService.getStoreId()),
				new Batch(storeStockDetail.getBatch().getId()), 0);
		stockTransaction.setIsTransit(true);

		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(
				pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
				pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);

	}

	@Transactional
	private Boolean revertAllocatedItemQuantityScheduled1(BatchAllocation batchAllocation, Integer screenId) {
		Boolean flag = false;
		Batch batch = batchAllocation.getBatch();
		//System.out.println("Batch id : "+batch.getId());
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(batchAllocation.getStore().getStoreId(),
				batch.getId(), 0);// 0 itemId);

		// Batch batch = GlobalDao.getBatch(sessionService.getStoreId(), batchId);

		Integer quantity = batchAllocation.getQuantity();

		Integer balanceQuantity = batch.getCurrentQuantity() + quantity;

		if (avaialableStoreStock == null || avaialableStoreStock < 0) {
			avaialableStoreStock = 0;

		}

		for (StoreStock s : batch.getStoreStockList()) {
			if (s.getStore().getStoreId().equals(batchAllocation.getStore().getStoreId())) {

				s.setAvailableQty((avaialableStoreStock + quantity) < 0 ? 0 : avaialableStoreStock + quantity);
				break;
			}
		}
		// batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock +
		// quantity);

		batchAllocation.setQuantity(0);
		batchAllocation.setBatch(batch);

		com.param.entity.model.master.Screen screen1 = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen1, screen1.getCode(),
				quantity, batch.getRate(), batchAllocation.getStore(), batch, 0);

		stockTransaction.setUpdatedByUser(1);
		stockTransaction.setAddedByUser(1);
		stockTransaction.setOrganization(batchAllocation.getOrganization());
		stockTransaction.setUnit(batchAllocation.getUnit());

		GlobalDao.saveStockTransaction(stockTransaction);
		flag = true;
		return flag;
	}

	private void saveIssueAndIssueDetails(Sale sale, WalkInSalesRequest request) {

		String billNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
				issueScreenId);
		SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");  
		Date today=new Date();
		try {
			if(request.getOrderDate()!=null)
			{
			today = formatter2.parse(request.getOrderDate());
			}
		} catch (ParseException e) {
		logger.error("saveIssueAndIssueDetails failed", e);
		}
		Status status = new Status((request.getAdmissionId() != null && request.getAdmissionId() > 0)
				? com.param.common.constants.Status.Closed.getStatus()
				: com.param.common.constants.Status.Pending.getStatus());

		Doctor doctor = null;
		if (request.getAdmissionId() != null && request.getAdmissionId() > 0) {
			Response docRes = WalkInSalesDao.getAdmissionsById(request.getAdmissionId());
			AdmissionListIpdChargesResDto obj = (AdmissionListIpdChargesResDto) docRes.getObject();
			
			if(obj != null) {
				if (obj.getDoctorId() != null) {
					doctor = new Doctor(obj.getDoctorId());
				}
			}
			
		} else if (sale.getDoctor() != null) {
			doctor = new Doctor(sale.getDoctor().getDoctorId());
		}

		Issue issue = new Issue(billNumber,
				(sale.getPrescription() != null && sale.getPrescription().getId() != null)
						? new Prescription(sale.getPrescription().getId())
						: null,
				(sale.getVisitType() != null) ? new VisitType(sale.getVisitType().getVisitTypeId()) : null, doctor,
				new Store(request.getStoreId()), status, null);
		if(request.getDoctorId() != null && request.getDoctorId() > 0) {
			issue.setDoctor(new Doctor(request.getDoctorId()));
		}

		issue.setRemark((request.getRemark() != null) ? request.getRemark() : null);
		// (sale.getTempDoctor() != null) ? new TempDoctor(sale.getTempDoctor().getId())
		// : null
		issue.setSale(sale);
		issue.setIsImplant(false);
		issue.setIsConsignment(false);
		issue.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		issue.setAddedDate(today);
		// sessionService.setEntityDefaults(issue);

		if (sale.getEncounter() != null) {
			// issue.setAdmission(new Admission(assignPickerSave.getAdmissionId()));
			issue.setEncounter(new Encounter(sale.getEncounter().getEncounterId()));
		}
		if (request.getAdmissionId() != null) {
			// issue.setAdmission(new Admission(assignPickerSave.getAdmissionId()));
			issue.setAdmission(new Admission(request.getAdmissionId()));
		}
		List<IssueDetail> newItemList = new ArrayList<>();
		int countFlag = 0;
		for (SaleDetail dto : sale.getSaleDetailsList()) {

			IssueDetail newItem = new IssueDetail();

			newItem.setStatus(new Status(request.getItems().get(countFlag).getStatusId()));
			countFlag++;
			/*
			 * if (dto.getStatus()!= null) { newItem.setStatus(new
			 * Status(dto.getStatus().getId())); }
			 */
			/*
			 * stock is already deducted while alloction of batch if
			 * (request.isWalkInSales()) {
			 * saveStoreStockDetails(sessionService.getStoreId(), dto.getBatch().getId(),
			 * dto.getQuantity(), new UomType(dto.getUomType().getId())); }
			 */

			Batch batchObj = GlobalDao.getBatch(dto.getBatch().getId());
			if (batchObj != null) {
				Integer leaseQuantity = Util.GetLeaseQuantity(dto.getQuantity(), batchObj.getBatchUomList(),
						new UomType(dto.getUomType().getId()));

				Double amount = (leaseQuantity * batchObj.getSellingPrice());
				IssueDetailBatchMapper issueDetailBatchMapper = new IssueDetailBatchMapper(batchObj,
						new UomType(dto.getUomType().getId()), new UomUnit(dto.getUomUnit().getId()), leaseQuantity,
						amount);
				newItem.setIssueQuantity(dto.getQuantity());
				issueDetailBatchMapper.setAcceptedQuantity(dto.getQuantity()); 
				if(request.getCashSettlement()!=null && request.getCashSettlement()==false)
				{
					issueDetailBatchMapper.setReturnAcceptedQuantity(0);
					issueDetailBatchMapper.setReturnRejectedQuantity(0);	
				}
				
				issueDetailBatchMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				newItem.addBatch(issueDetailBatchMapper);
			}
			newItem.setTaxAmount(dto.getTaxAmount());
			newItem.setNetAmount(dto.getNetAmount());
			if (dto.getPrescriptionDetail() != null && dto.getPrescriptionDetail().getRevisedOrderedQuantity() == 1) {
				newItem.setIsSubstitute(true);
			} else {
				newItem.setIsSubstitute(false);
			}
			newItem.setIsNarcoticSubstitute(false);
			newItem.setRate(dto.getRate());
			newItem.setTotalAmount(dto.getTotalAmount());
			newItem.setCreditPayable(dto.getCreditPayable());
			newItem.setDiscountAmount(dto.getDiscountAmount());
			newItem.setPackageConsumptionAmt(dto.getPackageConsumptionAmt());
			newItem.setTaxPercentage(dto.getTaxPercentage());
			newItem.setTaxAmount(dto.getTaxAmount());
			newItem.setUnitConsessionAmount(dto.getUnitConsessionAmount());
			newItem.setUnitDiscountAmount(dto.getUnitDiscountAmount());
			newItem.setConsessionAmount(dto.getConsessionAmount());
			newItem.setConsessionPercentage(dto.getConsessionPercentage());
			newItem.setDiscountAmount(dto.getDiscountAmount());
			newItem.setSaleDetail(new SaleDetail(dto.getId()));
			newItem.setPrescriptionDetail(
					dto.getPrescriptionDetail() != null ? new PrescriptionDetail(dto.getPrescriptionDetail().getId())
							: null);
			newItem.setBatch(new Batch(dto.getBatch().getId()));
			newItem.setUomType(new UomType(dto.getUomType().getId()));
			newItem.setUomUnit(new UomUnit(dto.getUomUnit().getId()));
			// newItem.setIsFromAssignPicker(sale.getSaleDetailsList().size()>0?(sale.getSaleDetailsList().get(0).getAssignPickerDetail()!=null
			// ? true:false):false);
			newItem.setIsFromAssignPicker(false);
			// sessionService.setEntityDefaults(newItem);
			newItem.setIssue(issue);
			newItem.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			newItem.setAddedDate(today);
			newItemList.add(newItem);
		}

		issue.setIssueDetail(newItemList);
		issue = iWalkInIssueDao.saveIssue(issue);
		
		iWalkInIssueDao.updateSaleforIssueId(issue,sale);
		
	}

	/*
	 * private void saveStoreStockDetails(Integer storeId, Long batchId, Integer
	 * quantity, UomType uomType) { if (quantity > 0) { try { Batch batch =
	 * GlobalDao.getBatch(batchId); if (batch != null) { Integer leaseQuantity =
	 * Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType); Integer
	 * tQuantity = 0; Integer availableQuantity = 0; Integer currentQuantity = 0;
	 * StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batchId,
	 * sessionService.getStoreId(),null); StoreStock storeStockDetail = null; if
	 * (storeStockList == null) { StoreStock storeStock = new
	 * StoreStock(leaseQuantity, new Store(sessionService.getStoreId()), new
	 * Batch(batchId)); //sessionService.setEntityDefaults(storeStock);
	 * storeStock.setCreateDefaults(sessionService.getOrganizationId(),
	 * sessionService.getUnitId(), sessionService.getUserId()); storeStockDetail =
	 * GlobalDao.saveStoreStockLeaseQuantity(storeStock); } else { tQuantity =
	 * leaseQuantity + storeStockList.gettQty(); availableQuantity =
	 * storeStockList.getAvailableQty() - leaseQuantity; if (availableQuantity < 0)
	 * { availableQuantity = 0; } storeStockList.setAvailableQty(availableQuantity);
	 * 
	 * Integer qty = 0; if (storeStockList.gettQty() != null) { qty =
	 * storeStockList.gettQty(); } //start code to resolve HIS-7397 Walkin sale
	 * settlement does not remove stock from in transit //storeStockList.settQty(qty
	 * + quantity); //end code to resolve HIS-7397 Walkin sale settlement does not
	 * remove stock from in transit
	 * 
	 * // storeStockList.settQty(tQuantity); storeStockDetail =
	 * GlobalDao.updateStoreStockLeaseQuantity(storeStockList); } Batch batchObject
	 * = GlobalDao.getBatch(sessionService.getStoreId(),
	 * storeStockDetail.getBatch().getId(),null); currentQuantity =
	 * batchObject.getCurrentQuantity() - leaseQuantity;
	 * batchObject.setCurrentQuantity(currentQuantity);
	 * GlobalDao.updateBatchLeaseQuantity(batchObject);
	 * saveStockTransaction(leaseQuantity, storeStockDetail); } } catch (Exception
	 * e) {  } } }
	 */

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(issueScreenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()), 0);
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(sessionService.getOrganizationId(), sessionService.getUnitId(),
				sessionService.getUserId());
		GlobalDao.saveStockTransaction(stockTransaction);
	}

	private Long allocateBatchQuantity(WalkInSalesRequest request, Batch batch, BatchResponse batchResponse) {
		BatchAllocation allocation;
		Long allocationId = null;
		List<UomResponse> uomList;

		if (batchResponse != null) {
			uomList = batchResponse.getUom();
		} else {
			uomList = GlobalDao.getBatchUomListPharmacy(request.getItem().getBatchId());
		}

		int quantity = Util.getOpUomConversion(uomList);

		Integer storeStock = GlobalDao.getStoreStockByBatch(request.getStoreId(), request.getItem().getBatchId(), 0);// 0
																														// itemId

		if (storeStock >= quantity) {
			allocation = WalkInSalesDao.getBatchAllocation(request.getTempBillId(), request.getItem().getBatchId());

			if (allocation == null) {
				allocationId = allocateBatchQuantity(request, storeStock, quantity, batch);
			} else {
				allocationId = updateBatchQuantity(request, allocation, storeStock, quantity, batch);
			}
		}
		return allocationId;
	}

	private long allocateBatchQuantity(WalkInSalesRequest request, Integer storeStock, Integer quantity, Batch batch) {
		Integer balanceQuantity = batch.getCurrentQuantity() - quantity;

		batch.setCurrentQuantity(balanceQuantity);
		batch.getStoreStockList().get(0).setAvailableQty(storeStock - quantity);

		BatchAllocation batchAllocation = new BatchAllocation(request.getTempBillId(),
				batch.getStoreStockList().get(0).getStore(), batch, quantity);
		// sessionService.setEntityDefaults(batchAllocation);
		batchAllocation.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		Screen screen = GlobalDao.getScreen(request.getScreenId());

		StockTransaction stockTransaction = new StockTransaction(storeStock, screen, screen.getCode(), quantity,
				batch.getRate(), batch.getStoreStockList().get(0).getStore(), batch, 0);
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		// stockTransaction.setUpdate
		WalkInSalesDao.saveStockTransaction(stockTransaction);

		return WalkInSalesDao.allocateQuantity(batchAllocation).longValue();
	}

	private long updateBatchQuantity(WalkInSalesRequest request, BatchAllocation batchAllocation, Integer storeStock,
			Integer quantity, Batch batch) {
		Integer allocationQuantity = batchAllocation.getQuantity() + quantity;
		Integer balanceQuantity = batch.getCurrentQuantity() - quantity;
		batch.setCurrentQuantity(balanceQuantity);
		batch.getStoreStockList().get(0).setAvailableQty(storeStock - quantity);

		batchAllocation.setQuantity(allocationQuantity);
		batchAllocation.setBatch(batch);

		Screen screen = GlobalDao.getScreen(request.getScreenId());

		StockTransaction stockTransaction = new StockTransaction(storeStock, screen, screen.getCode(),
				Math.abs(quantity), batch.getRate(), batch.getStoreStockList().get(0).getStore(), batch, 0);
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		// stockTransaction.setUpdatedByUser(request.getUserId());
		WalkInSalesDao.saveStockTransaction(stockTransaction);

		return WalkInSalesDao.updateAllocatedQuantity(batchAllocation).longValue();
	}

	@Override
	@Transactional
	public Response consumeItemFromContract(ParamacyContractReqDto dto) throws Exception {
		try {
			List<ItemRequest> items = dto.getRequest().getItems();
			if (items != null && items.size() > 0) {
				ContractConsumptionDto contractDto = new ContractConsumptionDto();
				contractDto.setAdmissionId(dto.getAdmissionId());
				contractDto.setEncounterId(dto.getEncounterId());
				contractDto.setPatientId(dto.getPatientId());
				List<ContractConsumptionDto> resList = iContractConsumptionDao
						.getSponsorDetailsByEncounterPharmacy(contractDto);
				if (resList != null && resList.size() > 0) {
					for (ContractConsumptionDto tContractDto : resList) {
						tContractDto.setContractId(tContractDto.getContractId());
						tContractDto.setEncounterId(
								dto.getEncounterId() == null ? dto.getAdmissionId() : dto.getEncounterId());
						for (ItemRequest item : items) {

							Double creditPayable = item.getCreditPayable();
							Double selfPayable = item.getSelfPayable();
							Double coShareCreaditPayable = 0.00;

							tContractDto.setItemId(item.getItemId());
							tContractDto.setProductCategoryId(item.getProductCategoryId());
							tContractDto.setPatientId(dto.getPatientId());
							ContractConsumptionDto resDto = iContractConsumptionDao
									.getItemDetailsByContractId(tContractDto, null);
							if (resDto != null) {
								// For updating Cap Details
								resDto.setAdmissionId(dto.getAdmissionId());
								resDto.setEncounterId(dto.getEncounterId());
								resDto.setProductCategoryId(item.getProductCategoryId());
								resDto.setPatientId(dto.getPatientId());
								resDto.setContractId(tContractDto.getContractId());
								// 1. for Cap Amount
								if (resDto.getBalanceAmount() != null && resDto.getBalanceAmount() > 0) {
									if (item.getSelfPayable() <= resDto.getBalanceAmount()) {
										selfPayable = 0.00;
										creditPayable = item.getSelfPayable();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									} else {
										selfPayable = item.getSelfPayable() - resDto.getBalanceAmount();
										creditPayable = resDto.getBalanceAmount();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									}
									// set payee
									item.setContractId(resDto.getContractId());
									item.setPayeeId(resDto.getPayeeId());
									if (item.getSaleDetailPayeeMapperDtoList() != null
											&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
									} else {
										List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										mpprList.add(mpprDto);
										item.setSaleDetailPayeeMapperDtoList(mpprList);
									}
									// if(item.getCreditPayable()!=null && item.getCreditPayable()>0 &&
									// item.getCreditItemQuantity()==null){
									if (creditPayable != null && creditPayable > 0) {
										iContractConsumptionDao.updatePharmacyCapQuantity(resDto, creditPayable * -1,
												0);
									}
									// setPayeeId
									if (tContractDto.getPriorityId() != null
											&& tContractDto.getPriorityId().intValue() == 1) {
										item.setPayeeId(resDto.getPayeeId());
									}
									item.setCreditPayable(item.getCreditPayable() + creditPayable);
									item.setSelfPayable(selfPayable);
								}

								item.setIsConsumed(true);
								resDto.setProductCategoryId(item.getProductCategoryId());
							} else {
								item.setPayeeId(sessionService.getSelfPayeeId());
							}

							item.setIsConsumed(true);
						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, "Items Validated Successfully.", items, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, ERROR_CODE, "Failed To Validate Items.", null, null);
	}

	@Override
	//@Transactional
	public Response removeContractItems(CommonBillingItemsDto item) throws Exception {
		try {

			int i = 0;
			if (item.getSaleDetailPayeeMapperDtoList() != null && item.getSaleDetailPayeeMapperDtoList().size() > 0)
				i = iContractConsumptionDao.addCapAmountPharmacy(item);

			if (item.getSaleDetailPackageMapperDtoList() != null && item.getSaleDetailPackageMapperDtoList().size() > 0)
				i = iMPackageMasterDao.cancelPackageItem(item,false,false);

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response addContractCapAmountPharmacy(ContractConsumptionDto contractDto, Integer saleId) throws Exception {
		try {
			CommonBillingItemsDto item = new CommonBillingItemsDto();
			item.setAdmissionId(contractDto.getAdmissionId());
			item.setEncounterId(contractDto.getEncounterId());
			item.setPatientId(contractDto.getPatientId());
			List<SaleDetailPayeeMapperDto> payeeList = GlobalDao.getSalePayeeMpperListbyDetailsId(null,saleId);
			item.setSaleDetailPayeeMapperDtoList(payeeList);
			int i = 0;
			if (item.getSaleDetailPayeeMapperDtoList() != null && item.getSaleDetailPayeeMapperDtoList().size() > 0)
				i = iContractConsumptionDao.addCapAmountPharmacy(item);

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response cancelPharmacyPackageCapping(ContractConsumptionDto contractDto,Integer saleId,boolean isRecalcualtion, boolean pharmacyReturnServiceCal) throws Exception {
		try {
			CommonBillingItemsDto item = new CommonBillingItemsDto();
			item.setAdmissionId(contractDto.getAdmissionId());
			item.setEncounterId(contractDto.getEncounterId());
			item.setPatientId(contractDto.getPatientId());
			List<SaleDetailPackageMapperDto> packageList = GlobalDao.getSalePackageMpperListbyDetailsId(null,saleId);
			item.setSaleDetailPackageMapperDtoList(packageList);
			iMPackageMasterDao.cancelPackageItem(item,isRecalcualtion,pharmacyReturnServiceCal);

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response cancelPharmacyReturnCapping(ContractConsumptionDto contractDto,Integer orderId,boolean isRecalcualtion) throws Exception {
		try {
			
			List<OrderDetailsMasterDto> orderDetailsList = iOrderDetailsMasterService.getItemDetailsFromOrderDetailsForUpdateReturnCalculation(orderId);
			
			for(OrderDetailsMasterDto detailsMasterDto : orderDetailsList)
			{
				List<SaleDetailPackageMapperDto> packageList = GlobalDao.getSalePackageMpperListbyDetailsIdReturn(detailsMasterDto.getSaleDetailId(), detailsMasterDto.getNetAmount());
				List<SaleDetailPayeeMapperDto> payeeList = GlobalDao.getSalePayeeMpperListbyDetailsIdReturn(detailsMasterDto.getSaleDetailId());
				CommonBillingItemsDto itemReq = new CommonBillingItemsDto();
				itemReq.setPatientId(contractDto.getPatientId());
				itemReq.setAdmissionId(contractDto.getAdmissionId() != null ? contractDto.getAdmissionId() : null);
				itemReq.setEncounterId(contractDto.getEncounterId() != null ? contractDto.getEncounterId() : null);
				itemReq.setProductCategoryId(contractDto.getProductCategoryId());
				itemReq.setSaleDetailPackageMapperDtoList(packageList);
				itemReq.setSaleDetailPayeeMapperDtoList(payeeList);
				this.addPackageContractForReturnItems(itemReq,isRecalcualtion);
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response consumePackageContractItems(CommonBillingItemsDto item) throws Exception {
		try {
			int i = 0;

			if (item.isPreAuthExist() && item.getSaleDetailPayeeMapperDtoList() != null
					&& item.getSaleDetailPayeeMapperDtoList().size() > 0)
				i = iContractConsumptionDao.consumeCapAmountPharmacy(item);

			if (item.getSaleDetailPackageMapperDtoList() != null && item.getSaleDetailPackageMapperDtoList().size() > 0)
				i = iMPackageMasterDao.consumePackageItem(item,false);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response consumePackageContractForReturnItems(CommonBillingItemsDto item) throws Exception {
		try {
			int i = 0;

			if (item.isPreAuthExist() && item.getSaleDetailPayeeMapperDtoList() != null
					&& item.getSaleDetailPayeeMapperDtoList().size() > 0)
				i = iContractConsumptionDao.addCapAmountPharmacy(item);

			if (item.getSaleDetailPackageMapperDtoList() != null && item.getSaleDetailPackageMapperDtoList().size() > 0)
				i = iMPackageMasterDao.addPackageItemForReturn(item);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response addPackageContractForReturnItems(CommonBillingItemsDto item,boolean isRecalcualtion) throws Exception {
		try {
			int i = 0;

			if (item.isPreAuthExist() && item.getSaleDetailPayeeMapperDtoList() != null
					&& item.getSaleDetailPayeeMapperDtoList().size() > 0)
				i = iContractConsumptionDao.consumeCapAmountPharmacy(item);

			if (item.getSaleDetailPackageMapperDtoList() != null && item.getSaleDetailPackageMapperDtoList().size() > 0)
				i = iMPackageMasterDao.consumePackageItem(item,isRecalcualtion);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response savePatient(WalkInSalesRequest request) throws Exception {
		try {

			PatientRegistrationDto dto = new PatientRegistrationDto();
			dto.setFirstName(request.getPatientName());
			dto.setMiddleName("");
			dto.setLastName("");
			if (request.getPatientGenderId() != null) {
				dto.setGenderId(request.getPatientGenderId());
			}
			if (request.getPatientAge() != null) {
				dto.setAge(request.getPatientAge().toString());
			}

			dto.setPrefixId(request.getPrefixId());
			if (request.getPatientAddress() != null) {
				dto.setAddress(request.getPatientAddress());
			}

			if (request.getMobileNumber() != null) {
				dto.setMobileNumber(request.getMobileNumber());
			}

			dto.setOrganizationId(request.getOrganizationId() != null ? request.getOrganizationId()
					: sessionService.getOrganizationId());
			dto.setUnitId(request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
			dto.setRegistrationTypeId(7);// for walkin
			dto.setStatus('A');
			dto.setIsBlacklist('N');
			dto.setUhidNumber("UHID-001");
			Response res = iPatientRegistrationDao.savePatientRegistration(dto);
			if (res.getStatus().equals(SUCCESS)) {
				PatientRegistrationDto pRdto = (PatientRegistrationDto) res.getObject();
				dto.setPatientId(pRdto.getPatientId());
				if (request.getPatientId() == null) {
					TempPatientRegistration tempPatient = new TempPatientRegistration(
							request.getPatientAddress() != null ? request.getPatientAddress() : null,
							request.getPatientAge() != null ? request.getPatientAge() : null,
							request.getPatientGenderId() != null ? new Gender(request.getPatientGenderId()) : null,
							request.getPatientName(), new Prefix(request.getPrefixId()), "UHID-1234",
							pRdto.getPatientId(), request.getMobileNumber() != null ? request.getMobileNumber() : null);
					tempPatient.setCreateDefaults(
							request.getOrganizationId() != null ? request.getOrganizationId()
									: sessionService.getOrganizationId(),
							request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
							request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
					tempPatient = WalkInSalesDao.savePatient(tempPatient);
					dto.setTempPatientId(tempPatient.getId());
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * @Override public Response
	 * consumeItemFromContractBillableConsumption(BillableConsumptionSave
	 * billableConsumptionSave, Integer admissionId, Integer encounterId) throws
	 * Exception { try { List<BillableConsumptionItem> items =
	 * billableConsumptionSave.getItems(); if (items != null && items.size() > 0) {
	 * ContractConsumptionDto contractDto = new ContractConsumptionDto();
	 * contractDto.setAdmissionId(admissionId);
	 * contractDto.setEncounterId(encounterId); List<ContractConsumptionDto> resList
	 * = iContractConsumptionDao .getSponsorDetailsByEncounterPharmacy(contractDto);
	 * if (resList != null && resList.size() > 0) { for (ContractConsumptionDto
	 * tContractDto : resList) {
	 * tContractDto.setContractId(tContractDto.getContractId());
	 * tContractDto.setEncounterId(encounterId);
	 * tContractDto.setAdmissionId(admissionId); for (BillableConsumptionItem item :
	 * items) { Double creditPayable = item.getCreditPayable(); Double selfPayable =
	 * item.getSelfPayable(); Double coShareCreaditPayable = 0.00;
	 * tContractDto.setItemId(item.getItemId());
	 * tContractDto.setProductCategoryId(item.getProductCategoryId());
	 * ContractConsumptionDto resDto = iContractConsumptionDao
	 * .getItemDetailsByContractId(tContractDto); if (resDto != null) { // For
	 * updating Cap Details resDto.setAdmissionId(admissionId);
	 * resDto.setEncounterId(encounterId);
	 * resDto.setProductCategoryId(item.getProductCategoryId());
	 * resDto.setContractId(tContractDto.getContractId()); // 1. for Cap Amount if
	 * (resDto.getBalanceAmount() != null && resDto.getBalanceAmount() > 0) { if
	 * (item.getSelfPayable() <= resDto.getBalanceAmount()) { selfPayable = 0.00; //
	 * creditPayable = item.getNetAmount(); creditPayable = item.getSelfPayable();
	 * if (resDto.getCoShare() != null) { coShareCreaditPayable = creditPayable *
	 * (resDto.getCoShare() / 100); creditPayable = creditPayable -
	 * coShareCreaditPayable; selfPayable = selfPayable + coShareCreaditPayable; } }
	 * else { // selfPayable = item.getNetAmount()-resDto.getBalanceAmount();
	 * selfPayable = item.getSelfPayable() - resDto.getBalanceAmount();
	 * creditPayable = resDto.getBalanceAmount(); if (resDto.getCoShare() != null) {
	 * coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
	 * creditPayable = creditPayable - coShareCreaditPayable; selfPayable =
	 * selfPayable + coShareCreaditPayable; } } // set payee // set payee
	 * item.setContractId(resDto.getContractId());
	 * item.setPayeeId(resDto.getPayeeId()); if
	 * (item.getSaleDetailPayeeMapperDtoList() != null &&
	 * item.getSaleDetailPayeeMapperDtoList().size() > 0) { SaleDetailPayeeMapperDto
	 * mpprDto = new SaleDetailPayeeMapperDto();
	 * mpprDto.setContractId(resDto.getContractId());
	 * mpprDto.setPayeeDesc(tContractDto.getCompanyName());
	 * mpprDto.setItemId(item.getItemId()); mpprDto.setPayeeId(resDto.getPayeeId());
	 * mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
	 * item.getSaleDetailPayeeMapperDtoList().add(mpprDto); } else {
	 * List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
	 * SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
	 * mpprDto.setContractId(resDto.getContractId());
	 * mpprDto.setPayeeDesc(tContractDto.getCompanyName());
	 * mpprDto.setItemId(item.getItemId()); mpprDto.setPayeeId(resDto.getPayeeId());
	 * mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
	 * mpprList.add(mpprDto); item.setSaleDetailPayeeMapperDtoList(mpprList); } if
	 * (creditPayable != null && creditPayable > 0) {
	 * iContractConsumptionDao.updatePharmacyCapQuantity(resDto, creditPayable * -1,
	 * 0); } // setPayeeId if (tContractDto.getPriorityId() != null &&
	 * tContractDto.getPriorityId().intValue() == 1) {
	 * item.setPayeeId(resDto.getPayeeId()); }
	 * 
	 * item.setCreditPayable(item.getCreditPayable() + creditPayable);
	 * item.setSelfPayable(selfPayable); } } else {
	 * item.setPayeeId(sessionService.getSelfPayeeId()); } item.setConsumed(true); }
	 * } } return new Response<>(SUCCESS, SUCCESS_CODE,
	 * "Items Validated Successfully.", items, billableConsumptionSave); } } catch
	 * (Exception e) {  } return new Response<>(ERROR,
	 * ERROR_CODE, "Failed To Validate Items.", null, null); }
	 */

	@Override
	@Transactional
	public Response consumeItemFromContractBillablePendingIssue(PendingIssuesSave pendingIssuesSave,
			Integer admissionId, Integer encounterId) throws Exception {
		try {

			List<ItemRequest> items = pendingIssuesSave.getItems();
			if (items != null && items.size() > 0) {
				ContractConsumptionDto contractDto = new ContractConsumptionDto();
				contractDto.setAdmissionId(admissionId);
				contractDto.setEncounterId(encounterId);
				List<ContractConsumptionDto> resList = iContractConsumptionDao
						.getSponsorDetailsByEncounterPharmacy(contractDto);
				if (resList != null && resList.size() > 0) {
					for (ContractConsumptionDto tContractDto : resList) {
						tContractDto.setContractId(tContractDto.getContractId());
						tContractDto.setEncounterId(encounterId);
						tContractDto.setAdmissionId(admissionId);
						for (ItemRequest item : items) {
							Double creditPayable = item.getCreditPayable();
							Double selfPayable = item.getSelfPayable();
							Double coShareCreaditPayable = 0.00;
							tContractDto.setItemId(item.getItemId());
							tContractDto.setProductCategoryId(item.getProductCategoryId());
							ContractConsumptionDto resDto = iContractConsumptionDao
									.getItemDetailsByContractId(tContractDto, null);
							if (resDto != null) {
								// For updating Cap Details
								resDto.setAdmissionId(admissionId);
								resDto.setEncounterId(encounterId);
								resDto.setProductCategoryId(item.getProductCategoryId());
								resDto.setContractId(tContractDto.getContractId());
								// 1. for Cap Amount
								if (resDto.getBalanceAmount() != null && resDto.getBalanceAmount() > 0) {
									if (item.getSelfPayable() <= resDto.getBalanceAmount()) {
										selfPayable = 0.00;
										// creditPayable = item.getNetAmount();
										creditPayable = item.getSelfPayable();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									} else {
										// selfPayable = item.getNetAmount()-resDto.getBalanceAmount();
										selfPayable = item.getSelfPayable() - resDto.getBalanceAmount();
										creditPayable = resDto.getBalanceAmount();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									}
									// set payee
									if (item.getSaleDetailPayeeMapperDtoList() != null
											&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
									} else {
										List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										mpprList.add(mpprDto);
										item.setSaleDetailPayeeMapperDtoList(mpprList);
									}
									if (creditPayable != null && creditPayable > 0) {
										iContractConsumptionDao.updatePharmacyCapQuantity(resDto, creditPayable * -1,
												0);
									}

									// setPayeeId
									if (tContractDto.getPriorityId() != null
											&& tContractDto.getPriorityId().intValue() == 1) {
										item.setPayeeId(resDto.getPayeeId());
									}
									item.setCreditPayable(item.getCreditPayable() + creditPayable);
									item.setSelfPayable(selfPayable);
								}
							}

							else {
								item.setPayeeId(sessionService.getSelfPayeeId());
							}
							item.setIsConsumed(true);
						}
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, "Items Validated Successfully.", items,
							pendingIssuesSave);
				}
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, ERROR_CODE, "Failed To Validate Items.", null, null);
	}

	@Override
	public Response consumeItemFromPatientConsumableIssueSave(PatientConsumableIssueSave patientConsumableIssueSave,
			Integer admissionId) throws Exception {
		try {

			List<PatientConsumableIssueSaveItem> items = patientConsumableIssueSave.getItems();
			if (items != null && items.size() > 0) {
				ContractConsumptionDto contractDto = new ContractConsumptionDto();
				contractDto.setAdmissionId(admissionId);
				List<ContractConsumptionDto> resList = iContractConsumptionDao
						.getSponsorDetailsByEncounterPharmacy(contractDto);
				if (resList != null && resList.size() > 0) {
					for (ContractConsumptionDto tContractDto : resList) {
						tContractDto.setContractId(tContractDto.getContractId());
						tContractDto.setAdmissionId(admissionId);
						for (PatientConsumableIssueSaveItem item : items) {
							Double creditPayable = item.getCreditPayable();
							Double selfPayable = item.getSelfPayable();
							Double coShareCreaditPayable = 0.00;
							tContractDto.setItemId(item.getItemId());
							tContractDto.setProductCategoryId(item.getProductCategoryId());
							ContractConsumptionDto resDto = iContractConsumptionDao
									.getItemDetailsByContractId(tContractDto, null);
							if (resDto != null) {
								// For updating Cap Details
								resDto.setAdmissionId(admissionId);
								resDto.setProductCategoryId(item.getProductCategoryId());
								resDto.setContractId(tContractDto.getContractId());
								// 1. for Cap Amount
								if (resDto.getBalanceAmount() != null && resDto.getBalanceAmount() > 0) {
									if (item.getSelfPayable() <= resDto.getBalanceAmount()) {
										selfPayable = 0.00;
										// creditPayable = item.getNetAmount();
										creditPayable = item.getSelfPayable();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									} else {
										// selfPayable = item.getNetAmount()-resDto.getBalanceAmount();
										selfPayable = item.getSelfPayable() - resDto.getBalanceAmount();
										creditPayable = resDto.getBalanceAmount();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									}
									// set payee
									item.setContractId(resDto.getContractId());
									item.setPayeeId(resDto.getPayeeId());
									if (item.getSaleDetailPayeeMapperDtoList() != null
											&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
									} else {
										List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										mpprList.add(mpprDto);
										item.setSaleDetailPayeeMapperDtoList(mpprList);
									}
									if (creditPayable != null && creditPayable > 0) {
										iContractConsumptionDao.updatePharmacyCapQuantity(resDto, creditPayable * -1,
												0);
									}

									// setPayeeId
									if (tContractDto.getPriorityId() != null
											&& tContractDto.getPriorityId().intValue() == 1) {
										item.setPayeeId(resDto.getPayeeId());
									}
									item.setCreditPayable(item.getCreditPayable() + creditPayable);
									item.setSelfPayable(selfPayable);
								}
							} else {
								item.setPayeeId(sessionService.getSelfPayeeId());
							}
						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, "Items Validated Successfully.", items,
						patientConsumableIssueSave);
			}
		} catch (Exception e) {
		logger.error("consumeItemFromPatientConsumableIssueSave failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, "Failed To Validate Items.", null, null);
	}

	@Override
	public Response consumeItemFromPatientConsumtionSave(PatientConsumtionIssueSave patientConsumtionSaveItem,
			Integer admissionId) throws Exception {
		try {

			List<PatientConsumtionSaveItem> items = patientConsumtionSaveItem.getItems();
			if (items != null && items.size() > 0) {
				ContractConsumptionDto contractDto = new ContractConsumptionDto();
				contractDto.setAdmissionId(admissionId);
				List<ContractConsumptionDto> resList = iContractConsumptionDao
						.getSponsorDetailsByEncounterPharmacy(contractDto);
				if (resList != null && resList.size() > 0) {
					for (ContractConsumptionDto tContractDto : resList) {
						tContractDto.setContractId(tContractDto.getContractId());
						tContractDto.setAdmissionId(admissionId);
						for (PatientConsumtionSaveItem item : items) {
							Double creditPayable = item.getCreditPayable();
							Double selfPayable = item.getSelfPayable();
							Double coShareCreaditPayable = 0.00;
							tContractDto.setItemId(item.getItemId());
							tContractDto.setProductCategoryId(item.getProductCategoryId());
							ContractConsumptionDto resDto = iContractConsumptionDao
									.getItemDetailsByContractId(tContractDto, null);
							if (resDto != null) {
								// For updating Cap Details
								resDto.setAdmissionId(admissionId);
								resDto.setProductCategoryId(item.getProductCategoryId());
								resDto.setContractId(tContractDto.getContractId());
								// 1. for Cap Amount
								if (resDto.getBalanceAmount() != null && resDto.getBalanceAmount() > 0) {
									if (item.getSelfPayable() <= resDto.getBalanceAmount()) {
										selfPayable = 0.00;
										// creditPayable = item.getNetAmount();
										creditPayable = item.getSelfPayable();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									} else {
										// selfPayable = item.getNetAmount()-resDto.getBalanceAmount();
										selfPayable = item.getSelfPayable() - resDto.getBalanceAmount();
										creditPayable = resDto.getBalanceAmount();
										if (resDto.getCoShare() != null) {
											coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
											creditPayable = creditPayable - coShareCreaditPayable;
											selfPayable = selfPayable + coShareCreaditPayable;
										}
									}
									// set payee
									item.setContractId(resDto.getContractId());
									item.setPayeeId(resDto.getPayeeId());
									if (item.getSaleDetailPayeeMapperDtoList() != null
											&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
									} else {
										List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setContractId(resDto.getContractId());
										mpprDto.setPayeeDesc(tContractDto.getCompanyName());
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(resDto.getPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
										mpprList.add(mpprDto);
										item.setSaleDetailPayeeMapperDtoList(mpprList);
									}
									if (creditPayable != null && creditPayable > 0) {
										iContractConsumptionDao.updatePharmacyCapQuantity(resDto, creditPayable * -1,
												0);
									}

									// setPayeeId
									if (tContractDto.getPriorityId() != null
											&& tContractDto.getPriorityId().intValue() == 1) {
										item.setPayeeId(resDto.getPayeeId());
									}
									item.setCreditPayable(item.getCreditPayable() + creditPayable);
									item.setSelfPayable(selfPayable);
								}
							} else {
								item.setPayeeId(sessionService.getSelfPayeeId());
							}
						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, "Items Validated Successfully.", items,
						patientConsumtionSaveItem);
			}
		} catch (Exception e) {
		logger.error("consumeItemFromPatientConsumtionSave failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, "Failed To Validate Items.", null, null);
	}

	@Override
	@Transactional
	public Response updatePayeePharamcy(List<OrderDetailsMasterDto> pharamcyList, BillingMasterDto billingMasterDto)
			throws Exception {
		try {
			if (pharamcyList != null && pharamcyList.size() > 0) 
			{
				Map<Integer, List<OrderDetailsMasterDto>> map = pharamcyList.stream().filter(entity -> entity.getSaleId()!=null).collect(Collectors.groupingBy(OrderDetailsMasterDto::getSaleId));
				SaleDtoChangeOfPayee sale = null;
				List<OrderDetailsMasterDto> dto = null;
				for (Map.Entry<Integer, List<OrderDetailsMasterDto>> entry : map.entrySet()) 
				{
					Integer saleId = entry.getKey();
					dto = (List<OrderDetailsMasterDto>) entry.getValue();
					sale = GlobalDao.getSale(new Long(saleId),billingMasterDto.getPaymentEntitlementId(), billingMasterDto.getPatientClassId(),true);
						sale.setAdmissionId(billingMasterDto.getAdmissionId());
						sale.setOrganizationId(billingMasterDto.getOrganisationId());
						sale.setUnitId(billingMasterDto.getUnitId());
						sale.setTariffId(billingMasterDto.getTariffId());
						sale.setPatientCategoryId(billingMasterDto.getPatientTypeId());
						sale.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
						sale.setPatientId(billingMasterDto.getPatientId());
						sale.setVisitTypeId(billingMasterDto.getVisitTypeId());
						sale.setIsRefReq(dto.get(0).getIsRefDoctorShare());
						sale.setDepartmentId(dto.get(0).getDepartmentId());
						sale.setForceAddPackageId(dto.get(0).getForceAddRemoved().intValue() == 1 ? dto.get(0).getOrderDetailsPackageMapperDtosList().get(0).getPackageId()	: null);
					
						Double saleTotalAmount = 0.00;
						Double saleNetAmount = 0.00;
						Double saleTaxAmount = 0.00;
						
					for (SaleDetailsChangeOfPayee item : sale.getSaledDetails()) 
					{
						
						 List<Integer> itemIds = new ArrayList<>();
						 itemIds.add(item.getItemId());
//					    List<UomResponse> uoms =  GlobalDao.getUnitOfMeasurementListByItemBP(itemIds,billingMasterDto.getOrganisationId());
					    Integer conversion =  1;
					    	
//					    	if (item.getUomTypeId()!=null && item.getUomTypeId() == 1 ) {
//									conversion = conversion * (uoms.get(0).getConversion() * uoms.get(1).getConversion() * uoms.get(2).getConversion());
//							}else if (item.getUomTypeId()!=null && item.getUomTypeId() == 2){
//								conversion = conversion * ( uoms.get(1).getConversion() * uoms.get(2).getConversion());
//							}else{
//								conversion = conversion * (uoms.get(2).getConversion());
//							}
					    	
//					    Integer leaseQuantity = conversion * item.getQuantity();
//					    item.setQuantity(leaseQuantity);
						item.setTotalAmount(GlobalCommonDateUtils.getAmount(item.getTotalAmount()) * conversion);
						item.setTaxAmount(GlobalCommonDateUtils.getAmount(item.getTaxAmount()) * conversion);
						item.setNetAmount(GlobalCommonDateUtils.getAmount(item.getNetAmount()) * conversion);
						item.setRate(GlobalCommonDateUtils.getAmount(item.getRate()) * conversion);
						if (!sale.isTaxInclusive() && item.getTaxPercentage() != null && item.getTaxPercentage() > 0) 
						{
							Double dis = 0d;
							if(dto.get(0).getOrdDiscountPer() != null && dto.get(0).getOrdDiscountPer().doubleValue() > 0) {
								 dis = (item.getTotalAmount() / 100) * dto.get(0).getOrdDiscountPer().doubleValue();
							}
							
							double taxAmt = ((item.getTotalAmount()-(item.getConsessionAmount() + dis))* item.getTaxPercentage() * 0.01);
							item.setNetAmount(GlobalCommonDateUtils.getAmount((item.getTotalAmount()-(item.getConsessionAmount() + dis)) + taxAmt));
							item.setSelfPayable(item.getNetAmount());
							item.setTaxAmount(GlobalCommonDateUtils.getAmount(taxAmt));
						} else {
							Double dis = 0d;
							if(dto.get(0).getOrdDiscountPer() != null && dto.get(0).getOrdDiscountPer().doubleValue() > 0) {
								 dis = (item.getTotalAmount() / 100) * dto.get(0).getOrdDiscountPer().doubleValue();
							}
							
							
							Double tax=GlobalCommonDateUtils.getAmount(item.getTotalAmount()-(item.getConsessionAmount() + dis)) / (1 + (item.getTaxPercentage()/100));
							item.setTaxAmount(GlobalCommonDateUtils.getAmount(item.getTotalAmount()-(item.getConsessionAmount() + dis))-tax);
							item.setNetAmount(GlobalCommonDateUtils.getAmount(item.getTotalAmount()-(item.getConsessionAmount() + dis)));
							item.setSelfPayable(item.getNetAmount());
						}
						
						 saleTotalAmount = saleTotalAmount + item.getTotalAmount();
						 saleNetAmount = saleNetAmount + item.getNetAmount();
						 saleTaxAmount = saleTaxAmount + item.getTaxAmount();
						
						PatientConsession patientConsession = null;
						if (billingMasterDto.getAdmissionId() != null && billingMasterDto.getAdmissionId() > 0) 
						{
							patientConsession = GlobalDao.getPatientConsessionAdmission(item.getItemId(),
									billingMasterDto.getAdmissionId(), billingMasterDto.getPatientTypeId(),
									item.getAssetCategoryId(), item.getProductCategoryId());
						}else{	

							patientConsession = GlobalDao.getPatientConsessionEncounter(item.getItemId(),
									billingMasterDto.getEncounterId(), billingMasterDto.getPatientTypeId(),
									item.getAssetCategoryId(), item.getProductCategoryId(), null, null);
						}	
							
						if(dto.get(0).getConcessionPer()!=null)
						{
							item.setConsessionAmount(GlobalCommonDateUtils.getAmount(item.getTotalAmount()* (dto.get(0).getConcessionPer() * 0.01)));
							item.setConsessionPercentage(dto.get(0).getConcessionPer());
							item.setUnitConsessionAmount(GlobalCommonDateUtils.getAmount(item.getRate() * (dto.get(0).getConcessionPer() * 0.01)));
						}else if(patientConsession != null && patientConsession.isMarkupStatus() == false) 
						{
							item.setConsessionAmount(GlobalCommonDateUtils.getAmount(item.getTotalAmount()* (patientConsession.getPatientClassPercentage() * 0.01)));
							item.setConsessionPercentage(patientConsession.getPatientClassPercentage());
							item.setUnitConsessionAmount(GlobalCommonDateUtils.getAmount(item.getRate() * (patientConsession.getPatientClassPercentage() * 0.01)));
						} else {
							item.setConsessionAmount(0);
							item.setConsessionPercentage(0);
							item.setUnitConsessionAmount(0);
						}
						
						if(dto.get(0).getOrdDiscountPer().compareTo(BigDecimal.ZERO)==-1 )
						{
							item.setDiscountPercentage(item.getOldDiscountPercentage());
						}
						
						if(dto.get(0).getOrdDiscountPer()!=null && !(dto.get(0).getOrdDiscountPer().compareTo(BigDecimal.ZERO)==-1))
						{
							item.setDiscountPercentage(dto.get(0).getOrdDiscountPer().doubleValue());
							item.setDiscountAmount(GlobalCommonDateUtils.getAmount((item.getTotalAmount() - item.getConsessionAmount())	* (dto.get(0).getOrdDiscountPer().doubleValue() * 0.01)));
							item.setUnitDiscountAmount(GlobalCommonDateUtils.getAmount((item.getRate() - item.getUnitConsessionAmount()) * (dto.get(0).getOrdDiscountPer().doubleValue() * 0.01)));
						}
						else if (item.getDiscountPercentage() != null && item.getDiscountPercentage() > 0) 
						{
							item.setDiscountAmount(GlobalCommonDateUtils.getAmount((item.getTotalAmount() - item.getConsessionAmount())	* (item.getDiscountPercentage() * 0.01)));
							item.setUnitDiscountAmount(GlobalCommonDateUtils.getAmount((item.getRate() - item.getUnitConsessionAmount()) * (item.getDiscountPercentage() * 0.01)));
						} else {
							item.setDiscountPercentage(0.00);
							item.setUnitDiscountAmount(0.00);
							item.setDiscountAmount(0.00);
						}
						
						

						List<SaleDetailPackageMapperDto> packageList = GlobalDao.getSalePackageMpperListbyDetailsId(item.getId().intValue(),null);
						List<SaleDetailPayeeMapperDto> payeeList = GlobalDao.getSalePayeeMpperListbyDetailsId(item.getId().intValue(),null);
						item.setSaleDetailPackageMapperDtoList(packageList);
						item.setSaleDetailPayeeMapperDtoList(payeeList);
					}
					
					 sale.setTotalAmount(saleTotalAmount);
					 sale.setTaxAmount(saleTaxAmount);
					 sale.setNetAmount(saleNetAmount);
					 
					//Response res = this.updatePayeePharamcyService(sale, dto.get(0).getOrderId(), dto, billingMasterDto);
				}

				Date date = TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId());
				Date orderDate = GlobalCommonDateUtils.getDate(sale.getOrderDate(), "yyyy-MM-dd HH:mm:ss");
				PharmacyBillCommonDto updatedSale = null;
				Set<Integer> saleDetailsId = new HashSet();

				List<OrderDetailsMaster> detailsList = null;
				Response res = iOrderMasterDao.getMedicationsByOrderId(dto.get(0).getOrderId());
				if (res.getListObject() != null && res.getListObject().size() > 0) {
					detailsList = res.getListObject();
				}

				UnitApplicationConfigurationDto appConfigDto = iOrderMasterDao
						.getPharmacyServiceId(billingMasterDto.getOrganisationId(), billingMasterDto.getUnitId());

				OrderDetailsMaster dtoToAdd = detailsList.stream().filter(orderDetail -> orderDetail.getServiceId() != null)
						.filter(orderDetail -> orderDetail.getServiceId() == appConfigDto.getPharmacyServiceId().intValue())
						.findAny().orElse(null);
	

				OrderDetailsMasterDto odForAdd = dto.stream()
						.filter(orderDetail -> orderDetail.getServiceId() != null)
						.filter(orderDetail -> orderDetail.getServiceId() == appConfigDto.getPharmacyServiceId().intValue())
						.findAny().orElse(null);

				List itemList = sale.getSaledDetails();
				sale.setItemList(itemList);
				sale.setForceAddRemoved(odForAdd.getForceAddRemoved());
				sale.setAddPreAuth(billingMasterDto.isAddPreAuth());
				sale.setRecalculation(billingMasterDto.isRecalculation());
				//sale.setPharmacyReturnServiceRecal(billingMasterDto.getPharmacyReturnServiceRecal() != null ? billingMasterDto.getPharmacyReturnServiceRecal() : 0);
				Response saleResponse = iMPackageMasterService.consumeItemFromPackageContract(sale,(billingMasterDto.getPharmacyReturnServiceRecal() != null ? billingMasterDto.getPharmacyReturnServiceRecal() : 0));
				if (saleResponse.getObject() != null) {
					updatedSale = (PharmacyBillCommonDto) saleResponse.getObject();
					List<CommonBillingItemsDto> items = saleResponse.getListObject();
					for (CommonBillingItemsDto saleDetail : items) 
					{
						if (saleDetail.getSaleDetailPayeeMapperDtoList() == null && saleDetail.getSaleDetailPackageMapperDtoList()==null) 
						{
							List<SaleDetailPayeeMapperDto> list = new ArrayList<SaleDetailPayeeMapperDto>();
							for (int qty = 1; qty <= saleDetail.getSelfQuantity(); qty++) 
							{
								SaleDetailPayeeMapperDto mpper = new SaleDetailPayeeMapperDto();
									mpper.setOrganizationId(billingMasterDto.getOrganisationId());
									mpper.setUnitId(billingMasterDto.getUnitId());
									mpper.setItemId(saleDetail.getItemId());
									mpper.setPayeeId(billingMasterDto.getSelfPayeeId());
									mpper.setStatus('A');
									mpper.setQuantity(1);
									mpper.setPayeeAmount(new BigDecimal(saleDetail.getSelfPayable() / saleDetail.getSelfQuantity()));
									saleDetail.setPayeeId(billingMasterDto.getSelfPayeeId());
								list.add(mpper);
							}
							saleDetail.setSaleDetailPayeeMapperDtoList(list);
						}else if (saleDetail.getSaleDetailPayeeMapperDtoList() == null && saleDetail.getSaleDetailPackageMapperDtoList()!=null)
						{
							List<SaleDetailPayeeMapperDto> list = new ArrayList<SaleDetailPayeeMapperDto>();
							for (SaleDetailPackageMapperDto detailPackageMapper : saleDetail.getSaleDetailPackageMapperDtoList()) 
							{
								if(detailPackageMapper.getPackageAmount()==0)
								{
									SaleDetailPayeeMapperDto mpper = new SaleDetailPayeeMapperDto();
										mpper.setOrganizationId(billingMasterDto.getOrganisationId());
										mpper.setUnitId(billingMasterDto.getUnitId());
										mpper.setItemId(saleDetail.getItemId());
										mpper.setPayeeId(billingMasterDto.getSelfPayeeId());
										mpper.setStatus('A');
										mpper.setQuantity(1);
										mpper.setPayeeAmount(new BigDecimal(detailPackageMapper.getRate()));
									saleDetail.setPayeeId(billingMasterDto.getSelfPayeeId());
									list.add(mpper);
								}
							}
							saleDetail.setSaleDetailPayeeMapperDtoList(list);
						}
						saleDetailsId.add(saleDetail.getId());
					}
				}

				WalkInSalesDao.updateOrderMasterAndSaleDetailsMaster(null, saleDetailsId);

				updatedSale.setOrganizationId(billingMasterDto.getOrganisationId());
				updatedSale.setUnitId(billingMasterDto.getUnitId());

				updatedSale = GlobalService.updateSale(updatedSale);

				double netAmount = 0;
				double taxAmount = 0;
				double ordTotalAmt = 0;
				double totalSelfPayable = 0;
				double totalCreditPayable = 0;
				double totalPackagePayable = 0;
				double totalConcession = 0;
				double totalDiscount = 0;
				// for Package sum
				Map<Integer, OrderDetailsPackageMapperDto> packageMapForSum = new HashMap<>();
				// for contract sum
				Map<Integer, OrderDetailsPayeeMapperDto> contractMapForSum = new HashMap<>();
				Map<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> associateMapForSum = new HashMap<>();
				Set<Integer> payeeIds = new HashSet<>();

				Map<OrderDetailsMaster,Map<List<OrderDetailsPayeeMapperDto>,List<OrderDetailsPackageMapperDto>>> orderDetailsMap = new HashMap<>();
				
				
				for (CommonBillingItemsDto saleDetail : updatedSale.getItemList()) 
				{
					Map<Integer, OrderDetailsPackageMapperDto> packageMap = new HashMap<>();

					// for contract
					Map<Integer, OrderDetailsPayeeMapperDto> contractMap = new HashMap<>();
					Map<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> contractMapAssociatePayee = new HashMap<>();

					OrderDetailsMaster orderDetailsMaster = detailsList.stream()
							.filter(orderDetail -> orderDetail.getSaleDetailId() != null).filter(orderDetail -> saleDetail
									.getId().intValue() == orderDetail.getSaleDetailId().intValue())
							.findAny().orElse(null);

					netAmount = netAmount + saleDetail.getNetAmount();
					taxAmount = taxAmount + (sale.isTaxInclusive() ? 0.00 : saleDetail.getTaxAmount());
					ordTotalAmt = ordTotalAmt + saleDetail.getTotalAmount();

					totalDiscount = totalDiscount + saleDetail.getDiscountAmount();
					totalConcession = totalConcession + saleDetail.getConsessionAmount();
					totalSelfPayable = totalSelfPayable + saleDetail.getSelfPayable();
					totalCreditPayable = totalCreditPayable + saleDetail.getCreditPayable();
					totalPackagePayable = totalPackagePayable + saleDetail.getPackageConsumptionAmt();

					orderDetailsMaster.setSaleDetailId(saleDetail.getId().intValue());
					orderDetailsMaster.setOrderQty(saleDetail.getQuantity());
					orderDetailsMaster.setTaxId(saleDetail.getTaxId());
					orderDetailsMaster.setSaleDetailId(saleDetail.getId().intValue());
					orderDetailsMaster.setServiceAmt(BigDecimal.valueOf(saleDetail.getRate()));
					orderDetailsMaster.setTaxAmount(BigDecimal.valueOf(saleDetail.getTaxAmount()));
					orderDetailsMaster.setOrdTotalAmt(BigDecimal.valueOf(saleDetail.getTotalAmount()));
					orderDetailsMaster.setTaxPer(BigDecimal.valueOf(saleDetail.getTaxPercentage()));
					orderDetailsMaster.setOrdDiscount(BigDecimal.valueOf(saleDetail.getDiscountAmount()));
					orderDetailsMaster.setOrdConcession(BigDecimal.valueOf(saleDetail.getConsessionAmount()));
					orderDetailsMaster.setNetAmount(BigDecimal.valueOf(saleDetail.getNetAmount()));
					orderDetailsMaster.setBatchId(saleDetail.getBatchId() != null ? saleDetail.getBatchId().intValue() : null);
					orderDetailsMaster.setDrugId(saleDetail.getItemId());
					orderDetailsMaster.setIsDrug('Y');
					orderDetailsMaster.setSelfPayable(BigDecimal.valueOf(saleDetail.getSelfPayable()));
					orderDetailsMaster.setCreditPayable(BigDecimal.valueOf(saleDetail.getCreditPayable()));
					orderDetailsMaster.setPackageConsumptionAmt(BigDecimal.valueOf(saleDetail.getPackageConsumptionAmt()));
					orderDetailsMaster.setCreatedDate(orderDate.getTime());
					orderDetailsMaster.setUpdatedDate(date.getTime());
					orderDetailsMaster.setOrderSchDate(orderDate.getTime());
					orderDetailsMaster.setOrderDate(orderDate.getTime());
					orderDetailsMaster.setOrdRenderDatetime(orderDate.getTime());
					orderDetailsMaster.setIsCapService('N');
					orderDetailsMaster.setIsPackageService('N');
					orderDetailsMaster.setIsInvestigationOrder('N');
					orderDetailsMaster.setIsProcedureService('N');
					orderDetailsMaster.setOrderEmergencyFlag('N');
					orderDetailsMaster.setIsProcedure('N');
					orderDetailsMaster.setIsProcedureService('N');
					orderDetailsMaster.setIsAppointableRadiology('N');
					orderDetailsMaster.setOrdCancelled('N');
					orderDetailsMaster.setStatus('A');
					orderDetailsMaster.setServiceIsBilled(0);
					//orderDetailsMaster.setCreatedBy(billingMasterDto.getUpdatedBy());
					orderDetailsMaster.setUpdatedBy(billingMasterDto.getUpdatedBy());
					orderDetailsMaster.setOrgId(billingMasterDto.getOrganisationId());
					orderDetailsMaster.setOrgUnitId(billingMasterDto.getUnitId());
					orderDetailsMaster.setServiceRendered(1);
					orderDetailsMaster.setPayeeId(saleDetail.getPayeeId());
					orderDetailsMaster.setIsOutsourced('N');
					orderDetailsMaster.setIsConsultationService('N');
					orderDetailsMaster.setDebitNoteService(false);
					orderDetailsMaster.setPriorityId(2);
					orderDetailsMaster.setForceAddRemoved(odForAdd.getForceAddRemoved());
					payeeIds.add(saleDetail.getPayeeId());
					orderDetailsMaster.setDiscountCategoryId(pharamcyList.get(0).getDiscountCategoryId()!= null ? pharamcyList.get(0).getDiscountCategoryId() : null );
					// For Package
					for (SaleDetailPackageMapperDto mppr : saleDetail.getSaleDetailPackageMapperDtoList()) 
					{
						if (packageMap.containsKey(mppr.getPackageConsumptionMasterId())) 
						{
							OrderDetailsPackageMapperDto mpprDto = packageMap.get(mppr.getPackageConsumptionMasterId());
							mpprDto.setPackageAmount(
									mpprDto.getPackageAmount().add(BigDecimal.valueOf(mppr.getPackageAmount())));
							mpprDto.setServiceAmount(mpprDto.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
							packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);
							if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) {
								OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum
										.get(mppr.getPackageConsumptionMasterId());
								mpprDtofinal.setPackageAmount(
										mpprDtofinal.getPackageAmount().add(BigDecimal.valueOf(mppr.getPackageAmount())));
								mpprDtofinal.setServiceAmount(
										mpprDtofinal.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
							}
						} else {
							
							
							OrderDetailsPackageMapperDto mpprDto =  billingCommonMethods.createOrderPackageMapperForPharmacyObj(mppr,billingMasterDto,null,null,null);
						/*	
							OrderDetailsPackageMapperDto mpprDto = new OrderDetailsPackageMapperDto();
							mpprDto.setOrganizationId(billingMasterDto.getOrganisationId());
							mpprDto.setUnitId(billingMasterDto.getUnitId());
							mpprDto.setStatus('A');
							mpprDto.setIsPackageService(mppr.getIsPackageItem());
							mpprDto.setPackageId(mppr.getPackageId());
							mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDto.setServiceAmount(BigDecimal.valueOf(mppr.getRate()));
							mpprDto.setPackageConsumptionMasterId(mppr.getPackageConsumptionMasterId());
							mpprDto.setPacakgeConsumptionServiceDetailId(mppr.getPacakgeConsumptionServiceDetailId());
							mpprDto.setPackageConsumptionCapDetailsId(mppr.getPackageConsumptionCapDetailsId());
							mpprDto.setPackageConsumptionEODetailId(mppr.getPackageConsumptionEODetailId());
							mpprDto.setPackageAmount(BigDecimal.valueOf(mppr.getPackageAmount()));
							mpprDto.setIsCapService('N');*/
/*
							OrderDetailsPackageMapperDto mpprDtoSum = new OrderDetailsPackageMapperDto();
							mpprDtoSum.setOrganizationId(billingMasterDto.getOrganisationId());
							mpprDtoSum.setUnitId(billingMasterDto.getUnitId());
							mpprDtoSum.setStatus('A');
							mpprDtoSum.setIsPackageService(mppr.getIsPackageItem());
							mpprDtoSum.setPackageId(mppr.getPackageId());
							mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDtoSum.setServiceAmount(BigDecimal.valueOf(mppr.getRate()));
							mpprDtoSum.setPackageConsumptionMasterId(mppr.getPackageConsumptionMasterId());
							mpprDtoSum.setPacakgeConsumptionServiceDetailId(mppr.getPacakgeConsumptionServiceDetailId());
							mpprDtoSum.setPackageConsumptionCapDetailsId(mppr.getPackageConsumptionCapDetailsId());
							mpprDtoSum.setPackageConsumptionEODetailId(mppr.getPackageConsumptionEODetailId());
							mpprDtoSum.setPackageAmount(BigDecimal.valueOf(mppr.getPackageAmount()));
							mpprDtoSum.setIsCapService('N');
*/
							OrderDetailsPackageMapperDto mpprDtoSum =  billingCommonMethods.createOrderPackageMapperForPharmacyObj(mppr,billingMasterDto,null,null,null);
							
							packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);
							if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) {
								OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum
										.get(mppr.getPackageConsumptionMasterId());
								mpprDtofinal.setPackageAmount(
										mpprDtofinal.getPackageAmount().add(BigDecimal.valueOf(mppr.getPackageAmount())));
								mpprDtofinal.setServiceAmount(
										mpprDtofinal.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
							} else {
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtoSum);
							}

						}
					}

					// pacakge
					List<OrderDetailsPackageMapperDto> ordDtlsPkgMppr = new ArrayList<OrderDetailsPackageMapperDto>();
					for (Entry<Integer, OrderDetailsPackageMapperDto> entry : packageMap.entrySet()) {
						ordDtlsPkgMppr.add(entry.getValue());
					}

					// For Contract
					for (SaleDetailPayeeMapperDto mppr : saleDetail.getSaleDetailPayeeMapperDtoList()) {

						if (mppr.getAssociateCompanyId() != null) {

							Map<Integer, Integer> newAssociatePayee = new HashMap<>();
							newAssociatePayee.put(mppr.getPayeeId(), mppr.getAssociateCompanyId());

							if (contractMapAssociatePayee.containsKey(newAssociatePayee)) {
								OrderDetailsPayeeMapperDto mpprDto = contractMapAssociatePayee.get(newAssociatePayee);
								mpprDto.setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
								contractMapAssociatePayee.put(newAssociatePayee, mpprDto);
								if (associateMapForSum.containsKey(newAssociatePayee)) {
									OrderDetailsPayeeMapperDto mpprDtofinal = associateMapForSum.get(newAssociatePayee);
									mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									mpprDtofinal.setSpiltBillAmt(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									associateMapForSum.put(newAssociatePayee, mpprDtofinal);
								}
							} else {
								
								OrderDetailsPayeeMapperDto mpprDto =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billingMasterDto);
								mpprDto.setAssociateCompanyId(mppr.getAssociateCompanyId());
								/*	OrderDetailsPayeeMapperDto mpprDto = new OrderDetailsPayeeMapperDto();
								mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDto.setOrganizationId(billingMasterDto.getOrganisationId());
								mpprDto.setUnitId(billingMasterDto.getUnitId());
								mpprDto.setStatus('A');
								mpprDto.setPayeeAmount(mppr.getPayeeAmount());
								mpprDto.setSpiltBillAmt(mppr.getPayeeAmount());
								mpprDto.setContractId(mppr.getContractId());
								mpprDto.setPayeeId(mppr.getPayeeId());
								mpprDto.setAssociateCompanyId(mppr.getAssociateCompanyId());

								OrderDetailsPayeeMapperDto mpprDtoSum = new OrderDetailsPayeeMapperDto();
								mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDtoSum.setOrganizationId(billingMasterDto.getOrganisationId());
								mpprDtoSum.setUnitId(billingMasterDto.getUnitId());
								mpprDtoSum.setStatus('A');
								mpprDtoSum.setPayeeAmount(mppr.getPayeeAmount());
								mpprDtoSum.setSpiltBillAmt(mppr.getPayeeAmount());
								mpprDtoSum.setContractId(mppr.getContractId());
								mpprDtoSum.setPayeeId(mppr.getPayeeId());
								mpprDtoSum.setAssociateCompanyId(mppr.getAssociateCompanyId());*/

								contractMapAssociatePayee.put(newAssociatePayee, mpprDto);

								OrderDetailsPayeeMapperDto mpprDtoSum =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billingMasterDto);
								mpprDtoSum.setAssociateCompanyId(mppr.getAssociateCompanyId());
								
								if (associateMapForSum.containsKey(newAssociatePayee)) {
									OrderDetailsPayeeMapperDto mpprDtofinal = associateMapForSum.get(newAssociatePayee);
									mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									mpprDtofinal.setSpiltBillAmt(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									associateMapForSum.put(newAssociatePayee, mpprDtofinal);
								} else {
									associateMapForSum.put(newAssociatePayee, mpprDtoSum);
								}
							}

						} else {
							if (contractMap.containsKey(mppr.getPayeeId())) {
								OrderDetailsPayeeMapperDto mpprDto = contractMap.get(mppr.getPayeeId());
								mpprDto.setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
								contractMap.put(mppr.getPayeeId(), mpprDto);

								if (contractMapForSum.containsKey(mppr.getPayeeId())) {
									OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(mppr.getPayeeId());
									mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									mpprDtofinal.setSpiltBillAmt(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									contractMapForSum.put(mppr.getPayeeId(), mpprDtofinal);
								}

							} else {

								OrderDetailsPayeeMapperDto mpprDto =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billingMasterDto);
							/*	
								OrderDetailsPayeeMapperDto mpprDto = new OrderDetailsPayeeMapperDto();
								mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDto.setOrganizationId(billingMasterDto.getOrganisationId());
								mpprDto.setUnitId(billingMasterDto.getUnitId());
								mpprDto.setStatus('A');
								mpprDto.setPayeeAmount(mppr.getPayeeAmount());
								mpprDto.setSpiltBillAmt(mppr.getPayeeAmount());
								mpprDto.setContractId(mppr.getContractId());
								mpprDto.setPayeeId(mppr.getPayeeId());

								OrderDetailsPayeeMapperDto mpprDtoSum = new OrderDetailsPayeeMapperDto();
								mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDtoSum.setOrganizationId(billingMasterDto.getOrganisationId());
								mpprDtoSum.setUnitId(billingMasterDto.getUnitId());
								mpprDtoSum.setStatus('A');
								mpprDtoSum.setPayeeAmount(mppr.getPayeeAmount());
								mpprDto.setSpiltBillAmt(mppr.getPayeeAmount());
								mpprDtoSum.setContractId(mppr.getContractId());
								mpprDtoSum.setPayeeId(mppr.getPayeeId());*/

								contractMap.put(mppr.getPayeeId(), mpprDto);

								OrderDetailsPayeeMapperDto mpprDtoSum =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billingMasterDto);
								
								if (contractMapForSum.containsKey(mppr.getPayeeId())) 
								{
									OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(mppr.getPayeeId());
									mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									mpprDtofinal.setSpiltBillAmt(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									contractMapForSum.put(mppr.getPayeeId(), mpprDtofinal);
								} else {
									contractMapForSum.put(mppr.getPayeeId(), mpprDtoSum);
								}
							}
						}

					}
					List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList = new ArrayList<OrderDetailsPayeeMapperDto>();

					for (Map.Entry<Integer, OrderDetailsPayeeMapperDto> entry : contractMap.entrySet()) {
						orderDetailsPayeeList.add(entry.getValue());
					}
					for (Map.Entry<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> entry : contractMapAssociatePayee
							.entrySet()) {
						orderDetailsPayeeList.add(entry.getValue());
					}
					iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMaster, orderDetailsPayeeList, ordDtlsPkgMppr);
				}

				Double amountApplicableForTax = (ordTotalAmt-totalConcession)-totalDiscount;
				
				dtoToAdd.setServiceAmt(BigDecimal.valueOf(ordTotalAmt));
				dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(ordTotalAmt));
				dtoToAdd.setOrdConcession(BigDecimal.valueOf(totalConcession));
				dtoToAdd.setPatientClassPercentage(BigDecimal.valueOf((totalConcession / ordTotalAmt) * 100));
				dtoToAdd.setOrdDiscount(BigDecimal.valueOf(totalDiscount));
				dtoToAdd.setTaxPer((!sale.isTaxInclusive() && amountApplicableForTax > 0) ? BigDecimal.valueOf((taxAmount / amountApplicableForTax) * 100) : BigDecimal.ZERO);
				dtoToAdd.setTaxAmount(sale.isTaxInclusive() ? BigDecimal.ZERO : BigDecimal.valueOf(taxAmount));
				dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
				dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
				dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
				
				if(totalPackagePayable>0)
				{
					dtoToAdd.setPackageConsumptionAmt(BigDecimal.valueOf(totalPackagePayable));
				}else{
					dtoToAdd.setPackageConsumptionAmt(BigDecimal.ZERO);
					dtoToAdd.setIsPackageService('N');
				}
				/*
				if(totalPackagePayable>0)
				{
                    dtoToAdd.setServiceAmt(BigDecimal.valueOf(ordTotalAmt));
                    dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(ordTotalAmt));
					dtoToAdd.setOrdConcession(BigDecimal.valueOf(totalConcession));
					dtoToAdd.setPatientClassPercentage(BigDecimal.valueOf((totalConcession/ordTotalAmt)*100));
					dtoToAdd.setOrdDiscount(BigDecimal.valueOf(totalDiscount));
					dtoToAdd.setTaxPer((!sale.isTaxInclusive() && amountApplicableForTax > 0)  ? BigDecimal.valueOf((taxAmount/amountApplicableForTax)*100) : BigDecimal.ZERO);
					dtoToAdd.setTaxAmount(sale.isTaxInclusive() ? BigDecimal.ZERO : BigDecimal.valueOf(taxAmount));
                    dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
                    dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
					dtoToAdd.setPackageConsumptionAmt(BigDecimal.valueOf(totalPackagePayable));
					dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
				}else{
					dtoToAdd.setServiceAmt(BigDecimal.valueOf(ordTotalAmt));
					dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(ordTotalAmt));
					dtoToAdd.setOrdConcession(BigDecimal.valueOf(totalConcession));
					dtoToAdd.setPatientClassPercentage(BigDecimal.valueOf((totalConcession/ordTotalAmt)*100));
					dtoToAdd.setOrdDiscount(BigDecimal.valueOf(totalDiscount));
					dtoToAdd.setTaxPer((!sale.isTaxInclusive() && amountApplicableForTax > 0)  ? BigDecimal.valueOf((taxAmount/amountApplicableForTax)*100) : BigDecimal.ZERO);
					dtoToAdd.setTaxAmount(sale.isTaxInclusive() ? BigDecimal.ZERO : BigDecimal.valueOf(taxAmount));
					dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
					dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
					dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
					dtoToAdd.setPackageConsumptionAmt(BigDecimal.ZERO);
					dtoToAdd.setIsPackageService('N');
				}*/
				dtoToAdd.setServiceId(appConfigDto.getPharmacyServiceId());
				dtoToAdd.setCreatedDate(orderDate.getTime());
				dtoToAdd.setUpdatedDate(date.getTime());
				dtoToAdd.setOrderSchDate(orderDate.getTime());
				dtoToAdd.setOrdRenderDatetime(orderDate.getTime());
				dtoToAdd.setOrderDate(orderDate.getTime());
				dtoToAdd.setIsCapService('N');
				dtoToAdd.setIsInvestigationOrder('N');
				dtoToAdd.setIsProcedureService('N');
				dtoToAdd.setIsProcedure('N');
				dtoToAdd.setIsProcedureService('N');
				dtoToAdd.setOrderEmergencyFlag('N');
				dtoToAdd.setIsAppointableRadiology('N');
				dtoToAdd.setIsDrug('N');
				dtoToAdd.setOrdCancelled('N');
				dtoToAdd.setStatus('A');
				dtoToAdd.setServiceIsBilled(0);
				dtoToAdd.setUpdatedBy(billingMasterDto.getUpdatedBy());
				dtoToAdd.setOrgId(billingMasterDto.getOrganisationId());
				dtoToAdd.setOrgUnitId(billingMasterDto.getUnitId());
				dtoToAdd.setServiceRendered(1);
				dtoToAdd.setIsOutsourced('N');
				dtoToAdd.setIsConsultationService('N');
				dtoToAdd.setDebitNoteService(false);
				dtoToAdd.setPriorityId(2);//sneha to be code 
			//	if(billingMasterDto.getrec)
				
				if(dtoToAdd.getOrdDiscount().doubleValue() > 0.00){
				if(billingMasterDto.isRecalculation()){
					dtoToAdd.setPharmacyDiscountOverride(0);
				}else{
					dtoToAdd.setPharmacyDiscountOverride(1);
				}
				}
				dtoToAdd.setTariffId(billingMasterDto.getTariffId());
				dtoToAdd.setForceAddRemoved(odForAdd.getForceAddRemoved());
				if (billingMasterDto.getReferralId() != null && billingMasterDto.getReferralId() > 0) 
				{
					OrderMasterDto orderMasterDto = new OrderMasterDto();
						orderMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
						orderMasterDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
						orderMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
						orderMasterDto.setPaymentEntitlementTypeId(billingMasterDto.getPaymentEntitlementId());
						orderMasterDto.setReferralId(billingMasterDto.getReferralId());
					
					OrderDetailsMasterDto odDetailsDto = new OrderDetailsMasterDto();
						odDetailsDto.setOrdDiscount(BigDecimal.ZERO);
						odDetailsDto.setOrdConcession(BigDecimal.ZERO);
						odDetailsDto.setEncounterId(billingMasterDto.getEncounterId());
						odDetailsDto.setAdmissionId(billingMasterDto.getAdmissionId());
						odDetailsDto.setOrgId(billingMasterDto.getOrganisationId());
						odDetailsDto.setOrgUnitId(billingMasterDto.getUnitId());
						odDetailsDto.setServiceId(appConfigDto.getPharmacyServiceId());
						odDetailsDto.setDepartmentId(appConfigDto.getServiceSpecilaityId());
						odDetailsDto.setOrdTotalAmt(BigDecimal.valueOf(totalPackagePayable>0?(netAmount + totalPackagePayable):amountApplicableForTax));
						odDetailsDto.setTariffId(billingMasterDto.getTariffId());
						odDetailsDto.setIsDocReq('N');
						odDetailsDto.setIsRefDoctorShare(appConfigDto.getIsRefDoctorShare());
						odDetailsDto.setDiscountOwnership(appConfigDto.getDefaultDiscountOwnership());
						odDetailsDto.setOrderDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
						orderMasterDto.setOrderDetailsMasterDto(odDetailsDto);
					odDetailsDto = iOrderMasterService.calculateRefShareForPharmacyService(orderMasterDto);
					
					if (odDetailsDto.getRefDocSharePer() != null) {
						dtoToAdd.setRefDocSharePer(odDetailsDto.getRefDocSharePer());
						dtoToAdd.setHospitalShare(odDetailsDto.getHospitalShare());
					} else {
						dtoToAdd.setHospitalShare(BigDecimal.valueOf(totalPackagePayable>0?(netAmount + totalPackagePayable):amountApplicableForTax));
					}

				} else {
					dtoToAdd.setHospitalShare(BigDecimal.valueOf(totalPackagePayable>0?(netAmount + totalPackagePayable):amountApplicableForTax));
				}
				
				Integer payeeIdLetest = payeeIds.stream().filter(payeeId -> payeeId!=null && payeeId.intValue() != billingMasterDto.getSelfPayeeId().intValue()).findAny().orElse(null);
				
				if(payeeIdLetest != null){
					dtoToAdd.setPayeeId(payeeIdLetest);
				}else{
					dtoToAdd.setPayeeId(billingMasterDto.getSelfPayeeId());
				}
				
			/*	if (payeeIds.size() > 0) {
					Iterator<Integer> iterator = payeeIds.iterator();
					while (iterator.hasNext()) {
						Integer payeeId = (Integer) iterator.next();
						if (payeeId!=null && payeeId.intValue() != billingMasterDto.getSelfPayeeId().intValue()) {
							dtoToAdd.setPayeeId(payeeId);
						}
					}
				} else {
					dtoToAdd.setPayeeId(billingMasterDto.getSelfPayeeId());
				}*/

				// FinalList PACKAGE
				List<OrderDetailsPackageMapperDto> finalPkgMpprList = new ArrayList<OrderDetailsPackageMapperDto>();
				for (Map.Entry<Integer, OrderDetailsPackageMapperDto> entry : packageMapForSum.entrySet()) 
				{
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					entry.getValue().setServiceAmount(BigDecimal.valueOf(totalPackagePayable>0 ? netAmount : sale.getTotalAmount()));
					finalPkgMpprList.add(entry.getValue());
				}

				// FinalList CONTRACT
				List<OrderDetailsPayeeMapperDto> finalMpprList = new ArrayList<OrderDetailsPayeeMapperDto>();
				for (Map.Entry<Integer, OrderDetailsPayeeMapperDto> entry : contractMapForSum.entrySet()) 
				{
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					entry.getValue().setOrderDetailsId(dtoToAdd.getOrderDetailsId());
					finalMpprList.add(entry.getValue());
				}
				for (Map.Entry<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> entry : associateMapForSum.entrySet()) 
				{
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					entry.getValue().setOrderDetailsId(dtoToAdd.getOrderDetailsId());
					finalMpprList.add(entry.getValue());
				}
				
				OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
				detailsPayeeMapperDto.setOrderDetailsId(dtoToAdd.getOrderDetailsId());
				detailsPayeeMapperDto.setPayeeId(billingMasterDto.getSelfPayeeId().intValue());
				detailsPayeeMapperDto.setServiceId(dtoToAdd.getServiceId());
				detailsPayeeMapperDto.setOrganizationId(dtoToAdd.getOrgId());
				detailsPayeeMapperDto.setUnitId(dtoToAdd.getOrgUnitId());
				detailsPayeeMapperDto.setStatus('A');
				if(finalMpprList.size()==0)
				{
					detailsPayeeMapperDto.setPayeeAmount(dtoToAdd.getNetAmount());
					finalMpprList.add(detailsPayeeMapperDto);
				}else{
					OrderDetailsPayeeMapperDto dummyserviceCount = finalMpprList.stream().filter(order -> order.getPayeeId().equals(billingMasterDto.getSelfPayeeId().intValue())).findAny() .orElse(null);
					
					if(dummyserviceCount == null){
						detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
						finalMpprList.add(detailsPayeeMapperDto);
					}
				}
				
			/*	if(finalMpprList.size()==0)
				{
					OrderDetailsPayeeMapperDto detailsPayeeMapper = new OrderDetailsPayeeMapperDto();
						detailsPayeeMapper.setOrderDetailsId(dtoToAdd.getOrderDetailsId());
						detailsPayeeMapper.setPayeeId(billingMasterDto.getSelfPayeeId().intValue());
						detailsPayeeMapper.setPayeeAmount(dtoToAdd.getNetAmount());
						detailsPayeeMapper.setServiceId(dtoToAdd.getServiceId());
						detailsPayeeMapper.setOrganizationId(dtoToAdd.getOrgId());
						detailsPayeeMapper.setUnitId(dtoToAdd.getOrgUnitId());
						detailsPayeeMapper.setStatus('A');
					finalMpprList.add(detailsPayeeMapper);
				}else{
					boolean selfPayeeExsist = false;
					for(OrderDetailsPayeeMapperDto detailsPayeeMapperDto : finalMpprList)
					{
						if(detailsPayeeMapperDto.getPayeeId().equals(billingMasterDto.getSelfPayeeId().intValue()))
							selfPayeeExsist=true;	
					}
					if(selfPayeeExsist==false)
					{
						OrderDetailsPayeeMapperDto detailsPayeeMapper = new OrderDetailsPayeeMapperDto();
							detailsPayeeMapper.setOrderDetailsId(dtoToAdd.getOrderDetailsId());
							detailsPayeeMapper.setPayeeId(billingMasterDto.getSelfPayeeId().intValue());
							detailsPayeeMapper.setPayeeAmount(BigDecimal.ZERO);
							detailsPayeeMapper.setServiceId(dtoToAdd.getServiceId());
							detailsPayeeMapper.setOrganizationId(dtoToAdd.getOrgId());
							detailsPayeeMapper.setUnitId(dtoToAdd.getOrgUnitId());
							detailsPayeeMapper.setStatus('A');
						finalMpprList.add(detailsPayeeMapper);
					}
					
				}*/

				Response sponserRes =  iPatientSponsorDetailsDao.getSponsoredDetailsForUpdatePayee(billingMasterDto.getAdmissionId(),billingMasterDto.getEncounterId());
				List<PatientSponsorDetailsDto> list = (List<PatientSponsorDetailsDto>) sponserRes.getListObject();
				
				for(OrderDetailsPayeeMapperDto detailsPayeeMapper:finalMpprList)
				{
					Integer priorityId = 0;
					PatientSponsorDetailsDto sponsorStramObj = null;
					if(detailsPayeeMapper.getAssociateCompanyId()!=null && detailsPayeeMapper.getAssociateCompanyId()>0)
					{
						sponsorStramObj = list.stream()
								.filter(sponsor -> sponsor.getPayeeId().equals(detailsPayeeMapper.getPayeeId()) && (detailsPayeeMapper.getAssociateCompanyId()!=null && detailsPayeeMapper.getAssociateCompanyId().equals(sponsor.getInsuranceId())) )
								.findAny().orElse(null);
					}else{
						sponsorStramObj = list.stream()
								.filter(sponsor -> sponsor.getPayeeId().equals(detailsPayeeMapper.getPayeeId()))
								.findAny().orElse(null);
					}
					
					detailsPayeeMapper.setPriority(sponsorStramObj.getPriorityId());
					
				}
				
				List<OrderDetailsPayeeMapperDto> finalMpprListNew = finalMpprList.stream()
						  .sorted(Comparator.comparing(OrderDetailsPayeeMapperDto::getPriority))
						  .collect(Collectors.toList());
				
				if(finalMpprListNew.size()>1)
					dtoToAdd.setCoPayPer(new BigDecimal((dtoToAdd.getSelfPayable().doubleValue()/dtoToAdd.getNetAmount().doubleValue())*100));
				else
					dtoToAdd.setCoPayPer(new BigDecimal(100));	
					
				iOrderDetailsMasterDao.saveOrderDetailsMaster(dtoToAdd, finalMpprListNew, finalPkgMpprList);
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, orderDetailsMap); 
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Pharamcy payee update failed");
		}
	}

	@Override
	//@Transactional
	public Integer sameQtySaleReturnSaleStatusUpdate(List<Integer> saleIds, Integer statusId) throws Exception {
		
		for(Integer saleId : saleIds)	
			WalkInSalesDao.inactiveOrderDetailsBySaleId(saleId,0);
		
		return WalkInSalesDao.sameQtySaleReturnSaleStatusUpdate(saleIds,statusId);
	}

	/*
	 * @Override
	 * 
	 * @Transactional public Response getPharamcySurchargeSericeId(Integer saleId,
	 * Integer paymentEntitlementId, Integer patientClassId, Integer patientTypeId)
	 * throws BillingException, Exception { try { OrderDetailsDiscountDto
	 * OrderDetailsDiscountDtos = new OrderDetailsDiscountDto();
	 * SaleDtoChangeOfPayee sale = GlobalDao.getSale(new Long(saleId),
	 * paymentEntitlementId, patientClassId); Double patientClassPercentage = 0.00;
	 * for (SaleDetailsChangeOfPayee item : sale.getSaledDetails()) {
	 * PatientConsession patientConsession = null; if (sale.getAdmissionId() != null
	 * && sale.getAdmissionId() > 0) { patientConsession =
	 * GlobalDao.getPatientConsessionAdmission(item.getItemId(),
	 * sale.getAdmissionId(), patientTypeId, item.getAssetCategoryId(),
	 * item.getProductCategoryId()); } else { patientConsession =
	 * GlobalDao.getPatientConsessionEncounter(item.getItemId(),
	 * sale.getEncounterId(), patientTypeId, item.getAssetCategoryId(),
	 * item.getProductCategoryId(), null, null); } if (patientConsession != null &&
	 * patientConsession.isMarkupStatus() == true) { patientClassPercentage =
	 * patientClassPercentage + patientConsession.getPatientClassPercentage(); } }
	 * OrderDetailsDiscountDtos.setDiscountPercentage(BigDecimal
	 * .valueOf(GlobalCommonDateUtils.getAmount(patientClassPercentage /
	 * sale.getSaledDetails().size()))); return new Response<>(SUCCESS,
	 * SUCCESS_CODE, null, null, OrderDetailsDiscountDtos); } catch (Exception e) {
	 *  throw new
	 * CustomException("Surcharge calculation Failed"); } }
	 */
	
	@Override
	@Transactional
	public Response updatePayeeRetunService(List<OrderDetailsMasterDto> pharamcyList, BillingMasterDto billingMasterDto)
			throws Exception {
		try {
			List<OrderDetailsMasterDto> orderDetailsList = iOrderDetailsMasterService.getItemDetailsFromOrderDetailsForUpdateReturnCalculation(pharamcyList.get(0).getOrderId());
				billingMasterDto.setOrderDetailsMasterDtosList(pharamcyList);
			Response res = this.pharamcyPackageContractReturn(billingMasterDto, orderDetailsList);

			//iOrderDetailsMasterService.inactiveOldOrderDetails(pharamcyList.get(0).getOrderId());

			return new Response(SUCCESS, null, null, null, res.getObject());
		} catch (Exception e) {
			
			throw new CustomException("Pharamcy payee update failed");
		}
	}

	@Override
	@Transactional
	public Response getPharamcySurchargeSericeId(Integer saleId, Integer paymentEntitlementId, Integer patientClassId,
			Integer patientTypeId) throws BillingException, Exception {
		try {
			OrderDetailsDiscountDto OrderDetailsDiscountDtos = new OrderDetailsDiscountDto();
			SaleDtoChangeOfPayee sale = GlobalDao.getSale(new Long(saleId), paymentEntitlementId, patientClassId,true);
			Double patientClassPercentage = 0.00;
			for (SaleDetailsChangeOfPayee item : sale.getSaledDetails()) {
				PatientConsession patientConsession = null;
				if (sale.getAdmissionId() != null && sale.getAdmissionId() > 0) {
					patientConsession = GlobalDao.getPatientConsessionAdmission(item.getItemId(), sale.getAdmissionId(),
							patientTypeId, item.getAssetCategoryId(), item.getProductCategoryId());
				} else {
					patientConsession = GlobalDao.getPatientConsessionEncounter(item.getItemId(), sale.getEncounterId(),
							patientTypeId, item.getAssetCategoryId(), item.getProductCategoryId(), null, null);
				}
				if (patientConsession != null && patientConsession.isMarkupStatus() == true) {
					patientClassPercentage = patientClassPercentage + patientConsession.getPatientClassPercentage();
				}
			}
			OrderDetailsDiscountDtos.setDiscountPercentage(BigDecimal
					.valueOf(GlobalCommonDateUtils.getAmount(patientClassPercentage / sale.getSaledDetails().size())));
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, OrderDetailsDiscountDtos);
		} catch (Exception e) {
			
			throw new CustomException("Surcharge calculation Failed");
		}
	}

	/*@Override
	@Transactional
	public Response updatePayeePharamcyService(SaleDtoChangeOfPayee sale, Integer orderId,
			List<OrderDetailsMasterDto> orderDetails, BillingMasterDto billingMasterDto) throws Exception {
		try {} catch (Exception e) {
			throw e;
		}
	}*/

	@Override
	@Transactional
	public Response updatePayeeConsumeFromContract(SaleDtoChangeOfPayee sale, Integer orderId,
			UpdatePayeeDto billingMasterDto) throws Exception {
		try {

			return new Response<>(SUCCESS, SUCCESS_CODE, "Items Validated Successfully.", null, sale);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	/** @Code change of Payee Pharamcy */

	@Override
	@Transactional
	public List<AllDataResponse> allDataList(Integer itemid, Integer storeId, Integer orgId, Integer unitId) {
		List<AllDataResponse> list = WalkInSalesDao.allDataList(itemid, storeId, orgId, unitId);
		return list;

	}

	@Override
	@Transactional
	public AvailableQty availableQty(Integer storeid, Integer itemid, Integer batchNo, Integer uontypeid,
			Integer unitId) {
		AvailableQty qty = null;
		// AvailableQty qty = WalkInSalesDao.availableQty(storeid, itemid, batchNo,
		// uontypeid);//origincode

		// added by minesh for clubbatch stock.
		/*
		 * AvailableQty qty = null; Batch batch = GlobalDao.getBatch(new Long(batchNo));
		 * BigInteger consignmentCount = batch != null ?
		 * GlobalDao.consignmentBatchCount(itemid, batch.getBatchNo(), batch.getLotNo()
		 * != null ? batch.getLotNo() : null, null, unitId) : BigInteger.ZERO; if
		 * (consignmentCount.longValue() > 0) { BigInteger availableqty = null;
		 * 
		 * List<Long> batchIds = GlobalDao.getConsignmentBatchListIdWithExpDate(new
		 * Long(batchNo),storeid); for (long batchId:batchIds) { qty =
		 * WalkInSalesDao.availableQty(storeid, itemid, (int)batchId, uontypeid);
		 * availableqty += qty.getAvailableqty()!=null ?qty.getAvailableqty():0; }
		 * 
		 * if (batch.getLotNo() != null) { availableqty =
		 * GlobalDao.getClubBatchWiseAvailableStock(batch.getBatchNo(),
		 * batch.getLotNo(), batch.getIsConsignment(), itemid, storeid, unitId); } else
		 * {
		 * 
		 * availableqty =
		 * GlobalDao.getClubBatchWiseAvailableStockWithoutLot(batch.getBatchNo(),
		 * batch.getIsConsignment(), itemid, storeid, unitId);
		 * 
		 * // availableqty = //
		 * BigInteger.valueOf(batch.getStoreStockList().get(0).getAvailableQty()); } qty
		 * = new AvailableQty(); qty.setAvailableqty(availableqty.intValue()); } // end
		 * code by minesh else { qty = WalkInSalesDao.availableQty(storeid, itemid,
		 * batchNo, uontypeid, unitId); }
		 */
		qty = WalkInSalesDao.availableQty(storeid, itemid, batchNo, uontypeid, unitId);
		return qty;

	}

	public double getNetAmountByQuantity(CommonBillingItemsDto dto, boolean taxInclusive) {

		double rate = dto.getRate();
		Double netAmount = 0.00;
		double totalAmount = 0.00;
		double unitDiscount = dto.getUnitDiscountAmount() == null ? 0 : dto.getUnitDiscountAmount();
		double unitConcession = dto.getUnitConsessionAmount();

		if (dto.getTaxPercentage() != null && dto.getTaxPercentage() > 0 && !taxInclusive) {
			totalAmount = (rate * 1) - (unitConcession + unitDiscount);
			netAmount = totalAmount + (totalAmount * (dto.getTaxPercentage() * 0.01));
			dto.setUnitNetAmount(netAmount);
		} else {
			totalAmount = (rate * 1) - (unitConcession + unitDiscount);
			netAmount = totalAmount;
			dto.setUnitNetAmount(netAmount);
		}

		if (dto.getSelfPayable() < netAmount) {
			netAmount = dto.getSelfPayable();
		}
		if (dto.getContractRate() > 0) {
			netAmount = dto.getContractRate();
		}
		return GlobalCommonDateUtils.getAmount(netAmount);
	}

	@Override
	public Response consumeItemFromContract(PharmacyBillCommonDto dto) throws Exception {
		try {

			Map<Integer, Double> preAuthDetails = new HashMap<>();

			List<CommonBillingItemsDto> items = dto.getItemList();
			items.forEach(item -> {
				item.setNetAmount(item.getSelfPayable());
				item.setIsConsumed(true);
				item.setCreditPayable(0.00);
			});
			
			String orderDate  = dto.getOrderDate() != null ? dto.getOrderDate() : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd HH:mm:ss");
			
			List<ContractConsumptionDto> contractList = new ArrayList<>();
			if (items != null && items.size() > 0) {
				ContractConsumptionDto contractDto = new ContractConsumptionDto();
				contractDto.setAdmissionId(dto.getAdmissionId());
				contractDto.setEncounterId(dto.getEncounterId());
				contractDto.setPatientId(dto.getPatientId());
				contractDto.setOrderDate(orderDate);
				List<ContractConsumptionDto> resList = iContractConsumptionDao.getSponsorDetailsByEncounterPharmacy(contractDto);

				// integer for contract id
				
				Map<Integer, List<ContractConsumptionDto>> map = new HashMap<Integer, List<ContractConsumptionDto>>();
				Map<Map<Map<Integer,Integer>,Integer>,Double> contractProdCatCapMap = new HashMap();
				if (resList != null && resList.size() > 0) {
					for (ContractConsumptionDto tContractDto : resList) 
					{
						dto.setSelfPayeeId(tContractDto.getSelfPayeeId());
						dto.setDefaultSelfTariffId(tContractDto.getDefaultSelfTariffId());
						for (CommonBillingItemsDto item : items) 
						{
							tContractDto.setEncounterId(dto.getEncounterId());
							tContractDto.setAdmissionId(dto.getAdmissionId());
							tContractDto.setProductCategoryId(item.getProductCategoryId());
							tContractDto.setPatientId(dto.getPatientId());
							tContractDto.setOrderDate(orderDate);
							//tContractDto.setItemId((item.getConsumedItemId() != null && item.getConsumedItemId() > 0)? item.getConsumedItemId(): item.getItemId());
							//HIS-21658
							tContractDto.setItemId(item.getItemId());
							
							ContractConsumptionDto resDto = iContractConsumptionDao.getItemDetailsByContractId(
									tContractDto,item.getItemId());
							if (resDto != null || tContractDto.getIsTariffRateApplicable().equals('Y')) 
							{
								Optional<ContractConsumptionDto> result = contractList.stream().parallel()
										.filter(itm -> (resDto != null && ((resDto.getType() == 1
													&& tContractDto.getContractId().equals(itm.getContractId())
													&& tContractDto.getProductCategoryId().equals(itm.getProductCategoryId()))
												|| (resDto.getType() == 2
													&& tContractDto.getContractId().equals(itm.getContractId())
													&& tContractDto.getItemId().equals(itm.getItemId()))))
												|| (tContractDto.getIsTariffRateApplicable().equals('Y')
													&& tContractDto.getContractId().equals(itm.getContractId())))
										.findAny();

								if (!result.isPresent()) 
								{
									ContractConsumptionDto dtoToBeadded = tContractDto ;
										/*new ContractConsumptionDto();
										dtoToBeadded.setContractId(tContractDto.getContractId());
										dtoToBeadded.setCompanyName(tContractDto.getCompanyName());
										dtoToBeadded.setPayeeId(tContractDto.getPayeeId());
										dtoToBeadded.setPriority(tContractDto.getPriority());
										dtoToBeadded.setIsTariffRateApplicable(tContractDto.getIsTariffRateApplicable());
										dtoToBeadded.setCoShare(tContractDto.getCoShare());
										dtoToBeadded.setAssociateCompanyId(tContractDto.getAssociateCompanyId());
										dtoToBeadded.setPreAuthBalAmt(tContractDto.getPreAuthBalAmt());
										dtoToBeadded.setPreauthId(tContractDto.getPreauthId());
										dtoToBeadded.setSelfPayeeId(tContractDto.getSelfPayeeId());
										dtoToBeadded.setDefaultSelfTariffId(tContractDto.getDefaultSelfTariffId());
										dtoToBeadded.setEncounterId(dto.getEncounterId());
										dtoToBeadded.setAdmissionId(dto.getAdmissionId());
										dtoToBeadded.setProductCategoryId(item.getProductCategoryId());
										dtoToBeadded.setPatientId(dto.getPatientId());
*/
									if (tContractDto.getPreauthId() != null)
										preAuthDetails.put(tContractDto.getPreauthId(),tContractDto.getPreAuthBalAmt());

									if (tContractDto.getIsTariffRateApplicable().equals('N')) 
									{
										dtoToBeadded.setType(resDto.getType());
										dtoToBeadded.setBalanceAmount(resDto != null ? resDto.getBalanceAmount() : 0.00);
										dtoToBeadded.setItemId(resDto != null ? item.getItemId() : null);
										dtoToBeadded.setCoShare(resDto.getCoShare());
										if(resDto.getBalanceAmount()!=null)
										{
											Map<Map<Integer,Integer>,Integer> contractProdCat = new HashMap<>();
											Map<Integer,Integer> test = new HashMap<>(); 
											test.put(tContractDto.getContractId(), item.getProductCategoryId());
											contractProdCat.put(test,0);
											
											contractProdCatCapMap.put(contractProdCat, resDto.getBalanceAmount());
										}
										
									}
									contractList.add(dtoToBeadded);
								}

							}

						}
					}

					/*
					 * for (Map.Entry<Integer, List<ContractConsumptionDto>> contract :
					 * map.entrySet()) { contractList.addAll(contract.getValue()); }
					 */
					List<ContractConsumptionDto> sortedList = contractList.stream()
							.sorted(Comparator.comparingInt(ContractConsumptionDto::getPriority))
							.collect(Collectors.toList());
					
					for (CommonBillingItemsDto item : items) 
					{
						if (item.getSelfPayable() != null && item.getSelfPayable() > 0) 
						{
							Integer quantity = item.getSelfQuantity();
							Double totalSelfPayable = 0.00;
							Double selfDiff = 0.00;
							
							for (int qty = 1; qty <= quantity; qty++) 
							{

								Double creditPayable = 0.00;
								Double coShareCreaditPayable = 0.00;
								Double selfPayable = getNetAmountByQuantity(item, dto.isTaxInclusive());
								item.setApportionedRate(0);
								item.setContractRate(0);
								totalSelfPayable = totalSelfPayable + selfPayable;
								if (qty == quantity && totalSelfPayable != item.getNetAmount()) {
									selfDiff = totalSelfPayable - item.getNetAmount();
									selfDiff = Math.round(selfDiff * 100.0) / 100.0;
									selfPayable = selfPayable - selfDiff;
								}

								if (sortedList != null && sortedList.size() > 0) {
									for (ContractConsumptionDto resDto : sortedList) 
									{
										Map<Map<Integer,Integer>,Integer> contractProdCat = new HashMap<>();
										Map<Integer,Integer> test = new HashMap<>(); 
										test.put(resDto.getContractId(), resDto.getProductCategoryId());
										contractProdCat.put(test,0);
										
										if (selfPayable > 0) 
										{
											if (resDto.getIsTariffRateApplicable() != null && resDto.getIsTariffRateApplicable() == 'N') 
											{
												if ((resDto.getType() == 1 && resDto.getProductCategoryId().equals(item.getProductCategoryId()))|| resDto.getType() == 2 && resDto.getItemId().equals(item.getItemId())) {
													
													//System.out.println(contractProdCatCapMap.get(contractProdCat));
													if (contractProdCatCapMap != null && contractProdCatCapMap.get(contractProdCat)!=null && contractProdCatCapMap.get(contractProdCat) > 0) // 1. for Cap Amount
													{
														/*if (selfPayable <= contractProdCatCapMap.get(contractProdCat)) {
															creditPayable = selfPayable;
															selfPayable = 0.00;

															if (resDto.getCoShare() != null) {
																coShareCreaditPayable = creditPayable
																		* (resDto.getCoShare() / 100);
																creditPayable = GlobalCommonDateUtils.getAmount(
																		creditPayable - coShareCreaditPayable);
																selfPayable = GlobalCommonDateUtils
																		.getAmount(selfPayable + coShareCreaditPayable);
															}
														} else {
															selfPayable = selfPayable - contractProdCatCapMap.get(contractProdCat);
															creditPayable = contractProdCatCapMap.get(contractProdCat);
															if (resDto.getCoShare() != null) {
																coShareCreaditPayable = creditPayable
																		* (resDto.getCoShare() / 100);
																creditPayable = GlobalCommonDateUtils.getAmount(
																		creditPayable - coShareCreaditPayable);
																selfPayable = GlobalCommonDateUtils
																		.getAmount(selfPayable + coShareCreaditPayable);
															}
														}*/
														creditPayable = selfPayable <= contractProdCatCapMap.get(contractProdCat) ? selfPayable: contractProdCatCapMap.get(contractProdCat);
														selfPayable = selfPayable <= contractProdCatCapMap.get(contractProdCat) ? 0.00 : selfPayable - contractProdCatCapMap.get(contractProdCat);
														 
														ContractConsumptionDto creditAndSelfAmountDto = billingCommonMethods.updatePreAuthForPharmacyContract(resDto,creditPayable,selfPayable,dto,preAuthDetails);
				                                            selfPayable = creditAndSelfAmountDto.getSelfPayable();
				                                            creditPayable = creditAndSelfAmountDto.getCreaditPayable();
				                                            
				                                        	if (resDto.getPreauthId() != null && preAuthDetails.get(resDto.getPreauthId()) >= 0) {
																
																preAuthDetails.put(resDto.getPreauthId(),preAuthDetails.get(resDto.getPreauthId()) - creditPayable);
															}
														
														/*if (resDto.getCoShare() != null) {
															coShareCreaditPayable = creditPayable
																	* (resDto.getCoShare() / 100);
															creditPayable = GlobalCommonDateUtils.getAmount(
																	creditPayable - coShareCreaditPayable);
															selfPayable = GlobalCommonDateUtils
																	.getAmount(selfPayable + coShareCreaditPayable);
														}
														
														// pre-auth
														if (resDto.getPreauthId() != null
																&& preAuthDetails.get(resDto.getPreauthId()) >= 0) {
															if (creditPayable > preAuthDetails
																	.get(resDto.getPreauthId())) {
																selfPayable = selfPayable + (creditPayable
																		- preAuthDetails.get(resDto.getPreauthId()));
																creditPayable = preAuthDetails
																		.get(resDto.getPreauthId());
															}
															if (dto.getConsume() && creditPayable > 0)
																iContractConsumptionDao.updatePreAuthAmount(
																		resDto.getPreauthId(), creditPayable);

															preAuthDetails.put(resDto.getPreauthId(),
																	preAuthDetails.get(resDto.getPreauthId())
																			- creditPayable);
															// resDto.setPreAuthBalAmt(resDto.getPreAuthBalAmt() -
															// creditPayable);
														}*/

														if (creditPayable != null && creditPayable > 0 && dto.getConsume()) {
															iContractConsumptionDao.updatePharmacyCapQuantity(resDto,
																	creditPayable * -1, 0);
														}

														// set payee
														item.setContractId(resDto.getContractId());
														item.setPayeeId(resDto.getPayeeId());

														if (creditPayable > 0) {
															
															item = billingCommonMethods.createSaleDetailsPayeeObj(item,resDto,creditPayable,false,null,null);

														/*	
															if (item.getSaleDetailPayeeMapperDtoList() != null && item
																	.getSaleDetailPayeeMapperDtoList().size() > 0) {
																SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
																mpprDto.setContractId(resDto.getContractId());
																mpprDto.setPayeeDesc(resDto.getCompanyName());
																mpprDto.setItemId(item.getItemId());
																mpprDto.setPayeeId(resDto.getPayeeId());
																mpprDto.setAssociateCompanyId(
																		resDto.getAssociateCompanyId());
																mpprDto.setQuantity(1);
																mpprDto.setPayeeAmount(
																		BigDecimal.valueOf(creditPayable));
																item.setPayeeId(resDto.getPayeeId());
																item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
															} else {
																List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
																SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
																mpprDto.setContractId(resDto.getContractId());
																mpprDto.setPayeeDesc(resDto.getCompanyName());
																mpprDto.setItemId(item.getItemId());
																mpprDto.setPayeeId(resDto.getPayeeId());
																mpprDto.setAssociateCompanyId(
																		resDto.getAssociateCompanyId());
																mpprDto.setQuantity(1);
																mpprDto.setPayeeAmount(
																		BigDecimal.valueOf(creditPayable));
																mpprList.add(mpprDto);
																item.setPayeeId(resDto.getPayeeId());
																item.setSaleDetailPayeeMapperDtoList(mpprList);
															}*/
															// setPayeeId
															if (resDto.getPriorityId() != null
																	&& resDto.getPriorityId().intValue() == 1) {
																item.setPayeeId(resDto.getPayeeId());
															}
														}

														item.setCreditPayable(item.getCreditPayable() + creditPayable);
														item.setSelfPayable(item.getSelfPayable() - creditPayable);
														//resDto.setBalanceAmount(GlobalCommonDateUtils.getAmount(resDto.getBalanceAmount() - creditPayable));
														contractProdCatCapMap.put(contractProdCat, contractProdCatCapMap.get(contractProdCat)- creditPayable); 
													}
													item.setIsConsumed(true);
													resDto.setProductCategoryId(item.getProductCategoryId());
												}

											}
											// for terrifRate Applicable
											else if (resDto.getIsTariffRateApplicable() != null
													&& resDto.getIsTariffRateApplicable() == 'Y') {
												creditPayable = selfPayable;
												selfPayable = 0.00;
												
	                                            ContractConsumptionDto creditAndSelfAmountDto = billingCommonMethods.updatePreAuthForPharmacyContract(resDto,creditPayable,selfPayable,dto,preAuthDetails);
	                                            selfPayable = creditAndSelfAmountDto.getSelfPayable();
	                                            creditPayable = creditAndSelfAmountDto.getCreaditPayable();
	                                            
	                                        	if (resDto.getPreauthId() != null && preAuthDetails.get(resDto.getPreauthId()) >= 0) {
													
													preAuthDetails.put(resDto.getPreauthId(),preAuthDetails.get(resDto.getPreauthId()) - creditPayable);
												}
												/*if (resDto.getCoShare() != null) {
													coShareCreaditPayable = creditPayable * (resDto.getCoShare() / 100);
													creditPayable = GlobalCommonDateUtils
															.getAmount(creditPayable - coShareCreaditPayable);
													selfPayable = GlobalCommonDateUtils
															.getAmount(selfPayable + coShareCreaditPayable);
												}

												// pre-auth
												if (resDto.getPreauthId() != null
														&& preAuthDetails.get(resDto.getPreauthId()) >= 0) {
													if (creditPayable > preAuthDetails.get(resDto.getPreauthId())) {
														selfPayable = selfPayable + creditPayable
																- preAuthDetails.get(resDto.getPreauthId());
														creditPayable = preAuthDetails.get(resDto.getPreauthId());
													}
													if (dto.getConsume() && creditPayable > 0)
														iContractConsumptionDao.updatePreAuthAmount(
																resDto.getPreauthId(), creditPayable);

													preAuthDetails.put(resDto.getPreauthId(),preAuthDetails.get(resDto.getPreauthId()) - creditPayable);
												}*/

												item.setContractId(resDto.getContractId());
												item.setPayeeId(resDto.getPayeeId());

												if (creditPayable > 0) {
													
													item = billingCommonMethods.createSaleDetailsPayeeObj(item,resDto,creditPayable,false,null,null);
													/*
													if (item.getSaleDetailPayeeMapperDtoList() != null
															&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
														SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
														mpprDto.setContractId(resDto.getContractId());
														mpprDto.setPayeeDesc(resDto.getCompanyName());
														mpprDto.setItemId(item.getItemId());
														mpprDto.setPayeeId(resDto.getPayeeId());
														mpprDto.setAssociateCompanyId(resDto.getAssociateCompanyId());
														mpprDto.setQuantity(1);
														mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
														mpprDto.setIsTariffRateApplicable(1);
														item.setPayeeId(resDto.getPayeeId());
														item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
													} else {
														List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
														SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
														mpprDto.setContractId(resDto.getContractId());
														mpprDto.setPayeeDesc(resDto.getCompanyName());
														mpprDto.setItemId(item.getItemId());
														mpprDto.setPayeeId(resDto.getPayeeId());
														mpprDto.setAssociateCompanyId(resDto.getAssociateCompanyId());
														mpprDto.setPayeeAmount(BigDecimal.valueOf(creditPayable));
														mpprDto.setQuantity(1);
														mpprDto.setIsTariffRateApplicable(1);
														mpprList.add(mpprDto);
														item.setPayeeId(resDto.getPayeeId());
														item.setSaleDetailPayeeMapperDtoList(mpprList);
													}*/
												}
												item.setCreditPayable(item.getCreditPayable() + creditPayable);
												item.setSelfPayable(item.getSelfPayable() - creditPayable);
											}

										}
									}
								}
								
								//for Self
								item.setPayeeId(sessionService.getSelfPayeeId());
								item = billingCommonMethods.createSaleDetailsPayeeObj(item,null,null,false,dto,selfPayable);
								
							/*	if (selfPayable >= 0) // for self
								{
									item.setPayeeId(sessionService.getSelfPayeeId());
									if (item.getSaleDetailPayeeMapperDtoList() != null
											&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(dto.getSelfPayeeId());
										mpprDto.setQuantity(1);
										mpprDto.setPayeeAmount(BigDecimal.valueOf(selfPayable));
										item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
									} else {
										List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(dto.getSelfPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.valueOf(selfPayable));
										mpprDto.setQuantity(1);
										mpprList.add(mpprDto);
										item.setSaleDetailPayeeMapperDtoList(mpprList);
									}
								} else {
									item.setPayeeId(dto.getSelfPayeeId());
									if (item.getSaleDetailPayeeMapperDtoList() != null
											&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(dto.getSelfPayeeId());
										mpprDto.setQuantity(1);
										mpprDto.setPayeeAmount(BigDecimal.ZERO);
										item.getSaleDetailPayeeMapperDtoList().add(mpprDto);
									} else {
										List<SaleDetailPayeeMapperDto> mpprList = new ArrayList<>();
										SaleDetailPayeeMapperDto mpprDto = new SaleDetailPayeeMapperDto();
										mpprDto.setItemId(item.getItemId());
										mpprDto.setPayeeId(dto.getSelfPayeeId());
										mpprDto.setPayeeAmount(BigDecimal.ZERO);
										mpprDto.setQuantity(1);
										mpprList.add(mpprDto);
										item.setSaleDetailPayeeMapperDtoList(mpprList);
									}
								}*/
								item.setIsConsumed(true);
							}
						}
						if (item.getSaleDetailPayeeMapperDtoList() != null
								&& item.getSaleDetailPayeeMapperDtoList().size() > 0) {
							Double itemNet = item.getNetAmount();
							Double amt = 0.00;
							;
							Double diff = 0.00;
							int i = 1;
							for (SaleDetailPayeeMapperDto mpprDto : item.getSaleDetailPayeeMapperDtoList()) {
								amt = amt + mpprDto.getPayeeAmount().doubleValue();
								if (i == item.getSaleDetailPayeeMapperDtoList().size()) {
									diff = amt - itemNet;
									diff = Math.round(diff * 100.0) / 100.0;
									mpprDto.setPayeeAmount(
											new BigDecimal(mpprDto.getPayeeAmount().doubleValue() - diff));
								}
								i++;
							}
						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, "Items Validated Successfully.", items, dto);
			}
		} catch (Exception e) {
		logger.error("consumeItemFromContract failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, "Please Enter Qty Dispensed.", null, null);
	}

	@Override
	public synchronized CommonResponse saveGlobalSale(Sale sale) throws Exception {
		try {

			UnitApplicationConfigurationDto configDto = new UnitApplicationConfigurationDto();
			configDto.setOrganizationId(
					sale.getOrganizationId() != null ? sale.getOrganizationId() : sessionService.getOrganizationId());
			configDto.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
			
			com.param.global.common.Response configRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(configDto);
			if (configRes.getObject() != null) {
				configDto = (UnitApplicationConfigurationDto) configRes.getObject();
			}
			
			if(sale.getIsFromBillableConsumption() == true ){
				configDto.setIsConsolidatedBill('Y');
			}
			
			// for sale
			Map<Integer, BigDecimal> contractSum = new HashMap<>();
			Map<Map<Integer, Integer>, BigDecimal> contractSumAssociate = new HashMap<>();
			Map<Integer, BigDecimal> packageSum = new HashMap<>();
			Double concession = 0.00;
			Double selfPayeSum = 0.0;
			if(sale.getSaleDetailsList()!=null)
			{
			for (SaleDetail item : sale.getSaleDetailsList()) 
			{
				if (item.getDiscountAmount() != null && item.getDiscountAmount() > 0) 
				{
					Double discPer = (item.getDiscountAmount() / (item.getTotalAmount()	- (item.getConsessionAmount() != null ? item.getConsessionAmount() : 0.00))) * 100;
					item.setDiscountPercentage(discPer);
				} else {
					item.setDiscountPercentage(0.00);
				}
				item.setOldDiscountPercentage(item.getDiscountPercentage());
				
				if (item.getConsessionAmount() != null)
					concession = concession + item.getConsessionAmount();

				List<SaleDetailPackageMapper> packageMpprFinal = new ArrayList<SaleDetailPackageMapper>();
				List<SaleDetailPayeeMapper> payeeMpprFinal = new ArrayList<SaleDetailPayeeMapper>();

				// for Package
				List<SaleDetailPackageMapper> packageMppr = item.getSaleDetailPackageMapperList();
				
				if (packageMppr != null && packageMppr.size() > 0) {

					for (SaleDetailPackageMapper dto : packageMppr) {
						dto.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
								: sessionService.getOrganizationId());
						dto.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
						dto.setRate(dto.getRate());
						dto.setSaleDetail(item);
						packageMpprFinal.add(dto);
						if (packageSum.containsKey(dto.getPackageConsumptionMasterId())) {
							packageSum.put(dto.getPackageConsumptionMasterId(),
									packageSum.get(dto.getPackageConsumptionMasterId()).add(dto.getPackageAmount()));
						} else {
							packageSum.put(dto.getPackageConsumptionMasterId(), dto.getPackageAmount());
						}

					}
				} else {
					for (int qty = 1; qty <= item.getQuantity(); qty++) {
						SaleDetailPackageMapper mpper = new SaleDetailPackageMapper();
						mpper.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
								: sessionService.getOrganizationId());
						mpper.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
						mpper.setItemId(item.getItemId());
						mpper.setStatus('A');
						mpper.setIsPackageItem('N');
						mpper.setPackageQty(1);
						mpper.setRate(BigDecimal.valueOf(item.getRate()));
						mpper.setPayeeId(configDto.getSelfPayeeId());
						mpper.setPackageAmount(BigDecimal.ZERO);
						mpper.setSaleDetail(item);
						packageMpprFinal.add(mpper);

						if (packageSum.containsKey(mpper.getPackageConsumptionMasterId())) {
							packageSum.put(mpper.getPackageConsumptionMasterId(), packageSum
									.get(mpper.getPackageConsumptionMasterId()).add(mpper.getPackageAmount()));
						} else {
							packageSum.put(mpper.getPackageConsumptionMasterId(), mpper.getPackageAmount());
						}
					}
				}

				// For Contract
				List<SaleDetailPayeeMapper> payeeMapper = item.getSaleDetailPayeeMapperList();

				if (payeeMapper != null && payeeMapper.size() > 0) {
					for (SaleDetailPayeeMapper dto : payeeMapper) {
						dto.setQuantity(1);
						payeeMpprFinal.add(dto);
						dto.setSaleDetail(item);
						if (dto.getAssociateCompanyId() != null) {
							Map<Integer, Integer> newAssociatePayee = new HashMap<>();
							newAssociatePayee.put(dto.getPayeeId(), dto.getAssociateCompanyId());
							if (contractSumAssociate.containsKey(newAssociatePayee)) {
								contractSumAssociate.put(newAssociatePayee,
										contractSumAssociate.get(newAssociatePayee).add(dto.getPayeeAmount()));
							} else {
								contractSumAssociate.put(newAssociatePayee, dto.getPayeeAmount());
							}
						} else {
							if (contractSum.containsKey(dto.getPayeeId())) {
								contractSum.put(dto.getPayeeId(),
										contractSum.get(dto.getPayeeId()).add(dto.getPayeeAmount()));
							} else {
								contractSum.put(dto.getPayeeId(), dto.getPayeeAmount());
							}
						}

					}
				} else if(packageMppr != null && packageMppr.size() > 0){
					double sum = 0;
					for (SaleDetailPackageMapper dto : packageMppr) 
					{
						if(dto.getPackageAmount().doubleValue()==0)
						{
							SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
							mpper.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
									: sessionService.getOrganizationId());
							mpper.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
							mpper.setItemId(item.getItemId());
							mpper.setPayeeId(configDto.getSelfPayeeId());
							mpper.setStatus('A');
							mpper.setQuantity(1);
							mpper.setPayeeAmount(dto.getRate());
							item.setPayeeId(configDto.getSelfPayeeId());
							mpper.setSaleDetail(item);
							payeeMpprFinal.add(mpper);
							sum = sum + dto.getRate().doubleValue();
						}
					}
					 selfPayeSum = selfPayeSum + sum;;
					contractSum.put(configDto.getSelfPayeeId(), BigDecimal.valueOf(selfPayeSum));
				}else {
					Double netSum = 0.00;
					Double diff = 0.00;
					for (int qty = 1; qty <= item.getQuantity(); qty++) 
					{
						SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
						mpper.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
								: sessionService.getOrganizationId());
						mpper.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
						mpper.setItemId(item.getItemId());
						mpper.setPayeeId(configDto.getSelfPayeeId());
						mpper.setStatus('A');
						mpper.setQuantity(1);
						mpper.setPayeeAmount(BigDecimal
								.valueOf(GlobalCommonDateUtils.getAmount(item.getNetAmount() / item.getQuantity())));

						netSum = netSum + mpper.getPayeeAmount().doubleValue();
						if (qty == item.getQuantity() && !(netSum.equals(item.getNetAmount()))) {
							diff = item.getNetAmount() - netSum;
							diff = Math.round(diff * 100.0) / 100.0;
							diff = mpper.getPayeeAmount().doubleValue() + diff;
							mpper.setPayeeAmount(new BigDecimal(diff));
						}
						item.setPayeeId(configDto.getSelfPayeeId());
						mpper.setSaleDetail(item);
						payeeMpprFinal.add(mpper);

						if (contractSum.containsKey(mpper.getPayeeId())) {
							contractSum.put(mpper.getPayeeId(),
									contractSum.get(mpper.getPayeeId()).add(mpper.getPayeeAmount()));
						} else {
							contractSum.put(mpper.getPayeeId(), mpper.getPayeeAmount());
						}

					}

				}

				// Setting Final List
				item.setSaleDetailPackageMapperList(packageMpprFinal);
				item.setSaleDetailPayeeMapperList(payeeMpprFinal);
				// sessionService.setEntityDefaults(item);
				item.setCreateDefaults(
						sale.getOrganizationId() != null ? sale.getOrganizationId()
								: sessionService.getOrganizationId(),
						sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId(),
						sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
				if (item.getTax() != null && item.getTax().getId() != null) {
					for (TaxDetail taxDetail : GlobalDao.getTaxDetail(item.getTax().getId())) {

						Integer taxCompId = taxDetail.getTaxComponent().getId();
						Double taxPercentage = taxDetail.getTaxPercentage();
						Double disAmt = item.getDiscountAmount() != null ? item.getDiscountAmount() : 0;
						Double amt = item.getTotalAmount() - disAmt;
						Double packageConsumptionAmount=item.getPackageConsumptionAmt()!=null ? item.getPackageConsumptionAmt():0;
//						amt=amt-packageConsumptionAmount;
						//Double calTaxAmt = (amt * taxPercentage) / 100;
                        Double calTaxAmt = (amt * taxPercentage)/(100 + item.getTaxPercentage());
						SaleDetailTaxComponenet saleDetailTaxComponenet = new SaleDetailTaxComponenet(calTaxAmt,
								new SaleDetail(sale.getSaleDetailsList().get(0).getId()), taxPercentage,
								new TaxComponent(taxCompId));
						// sessionService.setEntityDefaults(saleDetailTaxComponenet);
						saleDetailTaxComponenet.setAmount(calTaxAmt);
						saleDetailTaxComponenet.setCreateDefaults(
								sale.getOrganizationId() != null ? sale.getOrganizationId()
										: sessionService.getOrganizationId(),
								sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId(),
								sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
						item.addSaleDetailTaxComponenet(saleDetailTaxComponenet);
					}
				}

			}
		}

			// sessionService.setEntityDefaults(sale);
		/*	sale.setCreateDefaults(
					sale.getOrganizationId() != null ? sale.getOrganizationId() : sessionService.getOrganizationId(),
					sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId(),
					sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());*/
			if (sale.getNetAmount() != null && sale.getNetAmount().doubleValue() == 0) {
				// sale.setStatus(new
				// Status(com.param.common.constants.Status.Settled.getStatus()));
				sale.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
			} else if (sale.getTotalSelfPayable() != null && sale.getTotalSelfPayable().doubleValue() == 0
					&& sale.getTotalCreditPayable() != null && sale.getTotalCreditPayable().doubleValue() > 0) {
				sale.setStatus(new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
			}
			sale.setConcession(concession);
			
			sale.setCounterId(sessionService.getCounterId());
			Double amountValue=0.0;
			sale.setTotalAmount(sale.getTotalAmount()!=null ? sale.getTotalAmount() : amountValue );
			WalkInSalesDao.saveBill(sale);
			List<Integer> orderDetailsIds = new LinkedList<Integer>();
			if (sale.getVisitType() != null && sale.getVisitType().getVisitTypeId() != 1 || (sale.getVisitType() != null
					&& sale.getVisitType().getVisitTypeId() == 1 && configDto.getIsConsolidatedBill() == 'Y')
					|| sale.isOtCompletion()) {
				
				Date date = TimeZoneComponent.getDateByzone(sale.getUnitId());
				
				if(sale.getIsVirtualStore()!= null && sale.getIsVirtualStore() == true){
					date = sale.getAddedDate();
				}
				
				double netAmount = 0;
				double taxAmount = 0;
				double ordTotalAmt = 0;
				double totalSelfPayable = 0;
				double totalCreditPayable = 0;
				double totalPackagePayable = 0;
				double totalConcession = 0;

				// for Package sum
				Map<Integer, OrderDetailsPackageMapperDto> packageMapForSum = new HashMap<>();
				// for contract sum
				Map<Integer, OrderDetailsPayeeMapperDto> contractMapForSum = new HashMap<>();
				Map<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> associateMapForSum = new HashMap<>();

				Set<Integer> payeeIds = new HashSet<>();
				List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<>();

				UnitApplicationConfigurationDto appConfigDto = iOrderMasterService.getPharmacyServiceId(
						sale.getOrganizationId() != null ? sale.getOrganizationId()
								: sessionService.getOrganizationId(),
						sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());

				AdmissionsForBatchProcessDto aBPdto = null;
				AdmissionListIpdChargesResDto admission = null;
				if (sale.getSaleType().getId() == 2
						&& configDto.getIsConsolidatedBill() == 'Y'
						&& sale.getEncounter() != null && sale.getEncounter().getEncounterId() != null) {
					aBPdto = iBillingMasterService.getBillByEncounterId(sale.getEncounter().getEncounterId());
				} else if (sale.getAdmission() != null && sale.getAdmission().getAdmissionId() != null) {
					Response admissionDto = ipdChargesDao.getAdmissionsById(sale.getAdmission().getAdmissionId());
					admission = (AdmissionListIpdChargesResDto) admissionDto.getObject();
				}

				OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setPatientId(sale.getPatient()!=null ?sale.getPatient().getPatientId():0);
				orderMasterDto.setOrgId(sale.getOrganizationId() != null ? sale.getOrganizationId()
						: sessionService.getOrganizationId());
				orderMasterDto.setOrgUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
				orderMasterDto.setOrderDate(date.getTime());
				orderMasterDto.setVisitTypeId(sale.getVisitType().getVisitTypeId());
				orderMasterDto.setOrdDoctorId(sale.getDoctor() != null ? sale.getDoctor().getDoctorId() : null);
				orderMasterDto
						.setEncounterId((sale.getEncounter() != null) ? sale.getEncounter().getEncounterId() : null);
				orderMasterDto
						.setAdmissionId((sale.getAdmission() != null) ? sale.getAdmission().getAdmissionId() : null);
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setOrderStatusId(1);// 1 for pending
				orderMasterDto.setCreatedDate(date.getTime());
				orderMasterDto.setUpdatedDate(date.getTime());
				orderMasterDto.setIsPharmacy('Y');
				orderMasterDto.setCreatedBy(
						sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
				orderMasterDto.setUpdatedBy(
						sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
				orderMasterDto.setSaleId(sale.getId().intValue());
				if (aBPdto != null) {
					orderMasterDto.setBillingBedCategoryId(aBPdto.getBillingBedCategoryId());
					orderMasterDto.setPatientTypeId(aBPdto.getPatientTypeId());
					orderMasterDto.setPaymentEntitlementTypeId(aBPdto.getPaymentEntitlementId());
					orderMasterDto.setReferralId(aBPdto.getReferralId());
				}
				if(sale.getSaleDetailsList()!=null)
				{
				for (SaleDetail detail : sale.getSaleDetailsList()) {
					// for Package
					Map<Integer, OrderDetailsPackageMapperDto> packageMap = new HashMap<>();

					// for contract
					Map<Integer, OrderDetailsPayeeMapperDto> contractMap = new HashMap<>();
					Map<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> contractMapAssociatePayee = new HashMap<>();

					netAmount = netAmount + detail.getNetAmount();
					taxAmount = taxAmount + detail.getTaxAmount();
					ordTotalAmt = ordTotalAmt + detail.getTotalAmount();

					totalConcession = totalConcession + detail.getConsessionAmount();
					totalSelfPayable = totalSelfPayable + detail.getSelfPayable();
					totalCreditPayable = totalCreditPayable + detail.getCreditPayable();
					totalPackagePayable = totalPackagePayable + detail.getPackageConsumptionAmt();

					OrderDetailsMasterDto dto = new OrderDetailsMasterDto();
						dto.setSaleDetailId(detail.getId().intValue());
						dto.setOrderQty(detail.getQuantity());
						dto.setTaxId(detail.getTax() != null ? detail.getTax().getId() : null);
						dto.setSaleDetailId(detail.getId().intValue());
						dto.setServiceAmt(BigDecimal.valueOf(detail.getRate()));
						dto.setTaxAmount(BigDecimal.valueOf(detail.getTaxAmount()));
						dto.setOrdTotalAmt(BigDecimal.valueOf(detail.getTotalAmount()));
						dto.setTaxPer(BigDecimal.valueOf(detail.getTaxPercentage()));
						dto.setOrdConcession(BigDecimal.valueOf(detail.getConsessionAmount()));
						dto.setOrdConcessionPer(BigDecimal.valueOf(detail.getConsessionPercentage()));
						dto.setOrdDiscount(BigDecimal.valueOf(detail.getDiscountAmount()));
						dto.setNetAmount(BigDecimal.valueOf(detail.getNetAmount()));
						dto.setBatchId(detail.getBatch() != null ? detail.getBatch().getId().intValue() : null);
						dto.setDrugId(detail.getItemId());
						dto.setIsDrug('Y');
						dto.setSelfPayable(BigDecimal.valueOf(detail.getSelfPayable()));
						dto.setCreditPayable(BigDecimal.valueOf(detail.getCreditPayable()));
						dto.setPackageConsumptionAmt(BigDecimal.valueOf(detail.getPackageConsumptionAmt()));
						dto.setCreatedDate(date.getTime());
						dto.setUpdatedDate(date.getTime());
						dto.setOrderSchDate(date.getTime());
						dto.setOrdRenderDatetime(date.getTime());
						dto.setIsPackage(false);
						dto.setIsCapService('N');
						dto.setIsPackageService('N');
						dto.setIsInvestigationOrder('N');
						dto.setIsProcedureService('N');
						dto.setOrderEmergencyFlag('N');
						dto.setIsProcedure('N');
						dto.setIsProcedureService('N');
						dto.setIsAppointableRadiology('N');
						dto.setOrdCancelled('N');
						dto.setStatus('A');
						dto.setServiceIsBilled(0);
						dto.setCreatedBy(sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
						dto.setUpdatedBy(sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
						dto.setOrgId(sale.getOrganizationId() != null ? sale.getOrganizationId() : sessionService.getOrganizationId());
						dto.setOrgUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
						dto.setOrderDate(date.getTime());
						dto.setServiceRendered(1);
						dto.setPayeeId(detail.getPayeeId());
						dto.setIsOutsourced('N');
						dto.setIsConsultationService('N');
						dto.setDebitNoteService(false);
						dto.setPriorityId(2);
						payeeIds.add(detail.getPayeeId());

					// For Package
					for (SaleDetailPackageMapper mppr : detail.getSaleDetailPackageMapperList()) {

						if (packageMap.containsKey(mppr.getPackageConsumptionMasterId())) {
							OrderDetailsPackageMapperDto mpprDto = packageMap.get(mppr.getPackageConsumptionMasterId());
							mpprDto.setPackageAmount(mpprDto.getPackageAmount().add(mppr.getPackageAmount()));
							mpprDto.setServiceAmount(mpprDto.getServiceAmount().add(mppr.getRate()));
							packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);

							if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) {
								OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum
										.get(mppr.getPackageConsumptionMasterId());
								mpprDtofinal
										.setPackageAmount(mpprDtofinal.getPackageAmount().add(mppr.getPackageAmount()));
								mpprDtofinal.setServiceAmount(mpprDtofinal.getServiceAmount().add(mppr.getRate()));
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
							}

						} else {
							OrderDetailsPackageMapperDto mpprDto = new OrderDetailsPackageMapperDto();
							mpprDto.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
									: sessionService.getOrganizationId());
							mpprDto.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
							mpprDto.setStatus('A');
							mpprDto.setIsPackageService(mppr.getIsPackageItem());
							mpprDto.setPackageId(mppr.getPackageId());
							mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDto.setServiceAmount(mppr.getRate());
							mpprDto.setPackageConsumptionMasterId(mppr.getPackageConsumptionMasterId());
							mpprDto.setPacakgeConsumptionServiceDetailId(mppr.getPacakgeConsumptionServiceDetailId());
							mpprDto.setPackageConsumptionCapDetailsId(mppr.getPackageConsumptionCapDetailsId());
							mpprDto.setPackageConsumptionEODetailId(mppr.getPackageConsumptionEODetailId());
							mpprDto.setPackageAmount(mppr.getPackageAmount());
							mpprDto.setIsCapService('N');

							OrderDetailsPackageMapperDto mpprDtoSum = new OrderDetailsPackageMapperDto();
							mpprDtoSum.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
									: sessionService.getOrganizationId());
							mpprDtoSum.setUnitId(
									sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
							mpprDtoSum.setStatus('A');
							mpprDtoSum.setIsPackageService(mppr.getIsPackageItem());
							mpprDtoSum.setPackageId(mppr.getPackageId());
							mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
							mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
							mpprDtoSum.setServiceAmount(mppr.getRate());
							mpprDtoSum.setPackageConsumptionMasterId(mppr.getPackageConsumptionMasterId());
							mpprDtoSum
									.setPacakgeConsumptionServiceDetailId(mppr.getPacakgeConsumptionServiceDetailId());
							mpprDtoSum.setPackageConsumptionCapDetailsId(mppr.getPackageConsumptionCapDetailsId());
							mpprDtoSum.setPackageConsumptionEODetailId(mppr.getPackageConsumptionEODetailId());
							mpprDtoSum.setPackageAmount(mppr.getPackageAmount());
							mpprDtoSum.setIsCapService('N');
							packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);

							if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) {
								OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum
										.get(mppr.getPackageConsumptionMasterId());
								mpprDtofinal
										.setPackageAmount(mpprDtofinal.getPackageAmount().add(mppr.getPackageAmount()));
								mpprDtofinal.setServiceAmount(mpprDtofinal.getServiceAmount().add(mppr.getRate()));
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
							} else {
								packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtoSum);
							}
						}
					}
					// pacakge
					List<OrderDetailsPackageMapperDto> ordDtlsPkgMppr = new ArrayList<OrderDetailsPackageMapperDto>();
					for (Entry<Integer, OrderDetailsPackageMapperDto> entry : packageMap.entrySet()) {
						ordDtlsPkgMppr.add(entry.getValue());
					}

					// For Contract
					for (SaleDetailPayeeMapper mppr : detail.getSaleDetailPayeeMapperList()) {

						if (mppr.getAssociateCompanyId() != null) {
							Map<Integer, Integer> newAssociatePayee = new HashMap<>();
							newAssociatePayee.put(mppr.getPayeeId(), mppr.getAssociateCompanyId());

							if (contractMapAssociatePayee.containsKey(newAssociatePayee)) {
								OrderDetailsPayeeMapperDto mpprDto = contractMapAssociatePayee.get(newAssociatePayee);
								mpprDto.setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
								contractMapAssociatePayee.put(newAssociatePayee, mpprDto);
								if (associateMapForSum.containsKey(newAssociatePayee)) {
									OrderDetailsPayeeMapperDto mpprDtofinal = associateMapForSum.get(newAssociatePayee);
									mpprDtofinal
											.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									associateMapForSum.put(newAssociatePayee, mpprDtofinal);
								}
							} else {
								OrderDetailsPayeeMapperDto mpprDto = new OrderDetailsPayeeMapperDto();
								mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDto.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
										: sessionService.getOrganizationId());
								mpprDto.setUnitId(
										sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
								mpprDto.setStatus('A');
								mpprDto.setPayeeAmount(mppr.getPayeeAmount());
								mpprDto.setContractId(mppr.getContractId());
								mpprDto.setPayeeId(mppr.getPayeeId());
								mpprDto.setAssociateCompanyId(mppr.getAssociateCompanyId());

								OrderDetailsPayeeMapperDto mpprDtoSum = new OrderDetailsPayeeMapperDto();
								mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDtoSum.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
										: sessionService.getOrganizationId());
								mpprDtoSum.setUnitId(
										sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
								mpprDtoSum.setStatus('A');
								mpprDtoSum.setPayeeAmount(mppr.getPayeeAmount());
								mpprDtoSum.setContractId(mppr.getContractId());
								mpprDtoSum.setPayeeId(mppr.getPayeeId());
								mpprDtoSum.setAssociateCompanyId(mppr.getAssociateCompanyId());

								contractMapAssociatePayee.put(newAssociatePayee, mpprDto);

								if (associateMapForSum.containsKey(newAssociatePayee)) {
									OrderDetailsPayeeMapperDto mpprDtofinal = associateMapForSum.get(newAssociatePayee);
									mpprDtofinal
											.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									associateMapForSum.put(newAssociatePayee, mpprDtofinal);
								} else {
									associateMapForSum.put(newAssociatePayee, mpprDtoSum);
								}
							}

						} else {
							if (contractMap.containsKey(mppr.getPayeeId())) {
								OrderDetailsPayeeMapperDto mpprDto = contractMap.get(mppr.getPayeeId());
								mpprDto.setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
								contractMap.put(mppr.getPayeeId(), mpprDto);
								if (contractMapForSum.containsKey(mppr.getPayeeId())) {
									OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(mppr.getPayeeId());
									mpprDtofinal
											.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									contractMapForSum.put(mppr.getPayeeId(), mpprDtofinal);
								}
							} else {
								OrderDetailsPayeeMapperDto mpprDto = new OrderDetailsPayeeMapperDto();
								mpprDto.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDto.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDto.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
										: sessionService.getOrganizationId());
								mpprDto.setUnitId(
										sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
								mpprDto.setStatus('A');
								mpprDto.setPayeeAmount(mppr.getPayeeAmount());
								mpprDto.setContractId(mppr.getContractId());
								mpprDto.setPayeeId(mppr.getPayeeId());

								OrderDetailsPayeeMapperDto mpprDtoSum = new OrderDetailsPayeeMapperDto();
								mpprDtoSum.setItemId(mppr.getItemId() != null ? mppr.getItemId().intValue() : null);
								mpprDtoSum.settSaleDetailsId(mppr.getId() != null ? mppr.getId().intValue() : null);
								mpprDtoSum.setOrganizationId(sale.getOrganizationId() != null ? sale.getOrganizationId()
										: sessionService.getOrganizationId());
								mpprDtoSum.setUnitId(
										sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
								mpprDtoSum.setStatus('A');
								mpprDtoSum.setPayeeAmount(mppr.getPayeeAmount());
								mpprDtoSum.setContractId(mppr.getContractId());
								mpprDtoSum.setPayeeId(mppr.getPayeeId());

								contractMap.put(mppr.getPayeeId(), mpprDto);
								if (contractMapForSum.containsKey(mppr.getPayeeId())) {
									OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(mppr.getPayeeId());
									mpprDtofinal
											.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
									contractMapForSum.put(mppr.getPayeeId(), mpprDtofinal);
								} else {
									contractMapForSum.put(mppr.getPayeeId(), mpprDtoSum);
								}
							}
						}

					}
					List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList = new ArrayList<OrderDetailsPayeeMapperDto>();
					for (Map.Entry<Integer, OrderDetailsPayeeMapperDto> entry : contractMap.entrySet()) {
						orderDetailsPayeeList.add(entry.getValue());
					}
					for (Map.Entry<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> entry : contractMapAssociatePayee
							.entrySet()) {
						orderDetailsPayeeList.add(entry.getValue());
					}
					dto.setOrderDetailsPackageMapperDtosList(ordDtlsPkgMppr);
					dto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeList);
					orderDetailsList.add(dto);
				}
			}

				OrderDetailsMasterDto dtoToAdd = new OrderDetailsMasterDto();
					dtoToAdd.setServiceId(appConfigDto.getPharmacyServiceId());
					dtoToAdd.setDepartmentId(appConfigDto.getServiceSpecilaityId());
					dtoToAdd.setIsRefDoctorShare(appConfigDto.getIsRefDoctorShare());
					
					Double amountApplicableForTax = (sale.getTotalAmount()-totalConcession)-(sale.getTotalDiscountAmount() != null ? sale.getTotalDiscountAmount() : 0);
					
					if(totalPackagePayable>0)
					{
						dtoToAdd.setServiceAmt(BigDecimal.valueOf(sale.getTotalAmount()));
						dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(sale.getTotalAmount()));
						dtoToAdd.setOrdConcession(BigDecimal.valueOf(totalConcession));
						dtoToAdd.setPatientClassPercentage(sale.getTotalAmount()>0 ? BigDecimal.valueOf((totalConcession/sale.getTotalAmount())*100) :BigDecimal.ZERO);
						dtoToAdd.setOrdDiscount(BigDecimal.valueOf((sale.getTotalDiscountAmount() != null ? sale.getTotalDiscountAmount() : 0)));
						dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
						dtoToAdd.setTaxAmount(configDto.getTaxInclusive() ? BigDecimal.ZERO: BigDecimal.valueOf(sale.getTaxAmount()));
						dtoToAdd.setTaxPer((!configDto.getTaxInclusive() && amountApplicableForTax>0) ? BigDecimal.valueOf((sale.getTaxAmount()/amountApplicableForTax)*100) : BigDecimal.ZERO);
						dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
						dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
						dtoToAdd.setPackageConsumptionAmt(BigDecimal.valueOf(totalPackagePayable));
					}else{
						dtoToAdd.setServiceAmt(BigDecimal.valueOf(sale.getTotalAmount()));
						dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(sale.getTotalAmount()));
						dtoToAdd.setOrdConcession(BigDecimal.valueOf(totalConcession));
//						dtoToAdd.setPatientClassPercentage(BigDecimal.valueOf((totalConcession/sale.getTotalAmount())*100));
						dtoToAdd.setPatientClassPercentage(sale.getTotalAmount()>0 ? BigDecimal.valueOf((totalConcession/sale.getTotalAmount())*100) :BigDecimal.ZERO);
						dtoToAdd.setOrdDiscount(BigDecimal.valueOf((sale.getTotalDiscountAmount() != null ? sale.getTotalDiscountAmount() : 0)));
						dtoToAdd.setNetAmount(BigDecimal.valueOf(sale.getNetAmount()));
						dtoToAdd.setTaxAmount(configDto.getTaxInclusive() ? BigDecimal.ZERO: BigDecimal.valueOf(sale.getTaxAmount()));
						dtoToAdd.setTaxPer((!configDto.getTaxInclusive() && amountApplicableForTax>0) ? BigDecimal.valueOf((sale.getTaxAmount()/amountApplicableForTax)*100) : BigDecimal.ZERO);
						dtoToAdd.setSelfPayable(sale.getTotalSelfPayable()!=null ? BigDecimal.valueOf(sale.getTotalSelfPayable()):BigDecimal.ZERO);
						dtoToAdd.setCreditPayable(sale.getTotalCreditPayable()!=null ? BigDecimal.valueOf(sale.getTotalCreditPayable()):BigDecimal.ZERO);
						dtoToAdd.setPackageConsumptionAmt(BigDecimal.ZERO);
					}
					dtoToAdd.setCreatedDate(date.getTime());
					dtoToAdd.setUpdatedDate(date.getTime());
					dtoToAdd.setOrderDate(date.getTime());
					dtoToAdd.setOrdRenderDatetime(date.getTime());
					dtoToAdd.setOrderSchDate(date.getTime());
					dtoToAdd.setIsPackage(false);
					dtoToAdd.setIsCapService('N');
					//dtoToAdd.setIsPackageService('N');
					dtoToAdd.setIsInvestigationOrder('N');
					dtoToAdd.setIsProcedureService('N');
					dtoToAdd.setIsProcedure('N');
					dtoToAdd.setIsProcedureService('N');
					dtoToAdd.setOrderEmergencyFlag('N');
					dtoToAdd.setIsAppointableRadiology('N');
					dtoToAdd.setIsDrug('N');
					dtoToAdd.setOrderQty(1);
					dtoToAdd.setOrdCancelled('N');
					dtoToAdd.setStatus('A');
					dtoToAdd.setServiceIsBilled(0);
					dtoToAdd.setCreatedBy(sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
					dtoToAdd.setUpdatedBy(sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
					dtoToAdd.setOrgId(sale.getOrganizationId() != null ? sale.getOrganizationId(): sessionService.getOrganizationId());
					dtoToAdd.setOrgUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
					dtoToAdd.setServiceRendered(1);
					dtoToAdd.setIsOutsourced('N');
					dtoToAdd.setIsConsultationService('N');
					dtoToAdd.setDebitNoteService(false);
					dtoToAdd.setPriorityId(2);
					dtoToAdd.setOrderDate(date.getTime());
					dtoToAdd.setTariffId(configDto.getDefaultSelfTariffId());
					dtoToAdd.setOrdRenderDatetime(date.getTime());
					if(isDummyService)
						dtoToAdd.setDummyService(true);
					
					dtoToAdd.setServiceBillId(0);
					if (admission != null) {
						dtoToAdd.setBedId(admission.getBedId());
						dtoToAdd.setWardId(admission.getWardId());
						dtoToAdd.setRoomId(admission.getRoomId());
						dtoToAdd.setBedCategoryId(admission.getBedBillingCategoryId());
					}
					if (aBPdto != null) {
						dtoToAdd.setBedCategoryId(aBPdto.getBillingBedCategoryId());
					}
				Iterator<Integer> iterator = payeeIds.iterator();
				while (iterator.hasNext()) 
				{
					Integer payeeId = (Integer) iterator.next();
					if (payeeId!=null && payeeId.intValue() != configDto.getSelfPayeeId().intValue()) {
						dtoToAdd.setPayeeId(payeeId);
					}
				}

				// FinalList CONTRACT
				List<OrderDetailsPayeeMapperDto> finalMpprList = new ArrayList<OrderDetailsPayeeMapperDto>();
				for (Map.Entry<Integer, OrderDetailsPayeeMapperDto> entry : contractMapForSum.entrySet()) {
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					entry.getValue().setSpiltBillAmt(entry.getValue().getPayeeAmount());
					finalMpprList.add(entry.getValue());
				}

				for (Map.Entry<Map<Integer, Integer>, OrderDetailsPayeeMapperDto> entry : associateMapForSum
						.entrySet()) {
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					entry.getValue().setSpiltBillAmt(entry.getValue().getPayeeAmount());
					finalMpprList.add(entry.getValue());
				}

				// FinalList PACKAGE
				List<OrderDetailsPackageMapperDto> finalPkgMpprList = new ArrayList<OrderDetailsPackageMapperDto>();
				for (Map.Entry<Integer, OrderDetailsPackageMapperDto> entry : packageMapForSum.entrySet()) 
				{
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					entry.getValue().setServiceAmount(BigDecimal.valueOf(totalPackagePayable>0 ? netAmount : sale.getTotalAmount()));
					finalPkgMpprList.add(entry.getValue());
				}

				Response sponserRes =  iPatientSponsorDetailsDao.getSponsoredDetailsForUpdatePayee((sale.getAdmission() != null) ? sale.getAdmission().getAdmissionId():0,(sale.getEncounter() != null) ? sale.getEncounter().getEncounterId():0);
				List<PatientSponsorDetailsDto> list = (List<PatientSponsorDetailsDto>) sponserRes.getListObject();
				
				for(OrderDetailsPayeeMapperDto detailsPayeeMapper:finalMpprList)
				{
					Integer priorityId = 0;
					PatientSponsorDetailsDto sponsorStramObj = null;
					if(detailsPayeeMapper.getAssociateCompanyId()!=null && detailsPayeeMapper.getAssociateCompanyId()>0)
					{
						sponsorStramObj = list.stream()
								.filter(sponsor -> sponsor.getPayeeId().equals(detailsPayeeMapper.getPayeeId()) && (detailsPayeeMapper.getAssociateCompanyId()!=null && detailsPayeeMapper.getAssociateCompanyId().equals(sponsor.getInsuranceId())) )
								.findAny().orElse(null);
					}else{
						sponsorStramObj = list.stream()
								.filter(sponsor -> sponsor.getPayeeId()!=null ? sponsor.getPayeeId().equals(detailsPayeeMapper.getPayeeId()):(sponsor.getPayeeId()!=null))
								.findAny().orElse(null);
					}
					
					detailsPayeeMapper.setPriority(sponsorStramObj != null && sponsorStramObj.getPriorityId()!=null ? sponsorStramObj.getPriorityId() : 1);
					
				}
				
				List<OrderDetailsPayeeMapperDto> finalMpprListNew = finalMpprList.stream()
						  .sorted(Comparator.comparing(OrderDetailsPayeeMapperDto::getPriority))
						  .collect(Collectors.toList());
				
				
				dtoToAdd.setOrderDetailsPayeeMapperDtosList(finalMpprListNew);
				dtoToAdd.setOrderDetailsPackageMapperDtosList(finalPkgMpprList);

				if(dtoToAdd.getOrderDetailsPayeeMapperDtosList().size()>1)
					dtoToAdd.setCoPayPer(dtoToAdd.getNetAmount().doubleValue()>0 ?new BigDecimal((dtoToAdd.getSelfPayable().doubleValue()/dtoToAdd.getNetAmount().doubleValue())*100) :BigDecimal.ZERO );
				else
					dtoToAdd.setCoPayPer(new BigDecimal(100));
				
				orderDetailsList.add(dtoToAdd);
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);

				if (sale.getSaleType().getId() == 2 && configDto.getIsConsolidatedBill() == 'Y'
						&& sale.getVisitType() != null
						|| (sale.getVisitType() != null && sale.getVisitType().getVisitTypeId() != OP)
						|| sale.isOtCompletion() || sale.getIsFromBillableConsumption() || sale.isFromCrashCart()) {
					Response res = iOrderMasterService.saveOrderMaster(orderMasterDto);
					if (res.getResObject() != null) {
						Integer orderDetailsId = (Integer) res.getResObject();
						orderDetailsIds.add(orderDetailsId);
						if (aBPdto != null && aBPdto.getBillId() != null && aBPdto.getBillId() > 0) {
							OrderDetailsMaster orderDetailsMaster = mapper.map(dtoToAdd, OrderDetailsMaster.class,
									"OrderDetailsMasterDto_to_OrderDetailsMaster");
							orderDetailsMaster.setOrderDetailsId(orderDetailsId);
						}
					}
				}
			}

			if (sale.getSaleType().getId() != null) {
				for (Map.Entry<Map<Integer, Integer>, BigDecimal> entry : contractSumAssociate.entrySet()) {
					Map<Integer, Integer> newMap = entry.getKey();
					Integer payeeId = 0;
					Integer associatePayeeId = 0;
					for (Map.Entry<Integer, Integer> entry2 : newMap.entrySet()) {
						payeeId = entry2.getKey();
						associatePayeeId = entry2.getValue();
					}
					BigDecimal creditPayable = entry.getValue();
					PharamcyBillPayeeDetails pBillPayeeDetails = new PharamcyBillPayeeDetails();
					pBillPayeeDetails.setPayeeId(payeeId);
					pBillPayeeDetails.setAssociateCompanyId(associatePayeeId);
					pBillPayeeDetails.setBillAmount(creditPayable.doubleValue());
					if(sale.getEntitlementId()!=null && sale.getEntitlementId()==1)
					{
						pBillPayeeDetails.setDueAmount(sale.getNetAmount());
					}
					else
					{
					pBillPayeeDetails.setDueAmount(creditPayable.doubleValue());
					}
					pBillPayeeDetails.setCrAmount(0.0);
					pBillPayeeDetails.setOrgId(sale.getOrganizationId() != null ? sale.getOrganizationId()
							: sessionService.getOrganizationId());
					pBillPayeeDetails
							.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
					pBillPayeeDetails.setCreatedBy(
							sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
					pBillPayeeDetails.setCreatedDate(TimeZoneComponent.getDateByzone(sale.getUnitId()).getTime());
					pBillPayeeDetails.settSaleId(sale.getId().intValue());
					pBillPayeeDetails.setStatus('A');
					GlobalDao.savePharamcyBillPayedetails(pBillPayeeDetails);
				}
				for (Map.Entry<Integer, BigDecimal> entry : contractSum.entrySet()) {
					Integer payeeId = entry.getKey();
					BigDecimal creditPayable = entry.getValue();
					PharamcyBillPayeeDetails pBillPayeeDetails = new PharamcyBillPayeeDetails();
					pBillPayeeDetails.setPayeeId(payeeId);
					pBillPayeeDetails.setBillAmount(creditPayable.doubleValue());
					if(sale.getEntitlementId()!=null && sale.getEntitlementId()==1)
					{
						pBillPayeeDetails.setDueAmount(sale.getNetAmount());
					}
					else
					{
					pBillPayeeDetails.setDueAmount(creditPayable.doubleValue());
					}
					pBillPayeeDetails.setCrAmount(0.0);
					pBillPayeeDetails.setOrgId(sale.getOrganizationId() != null ? sale.getOrganizationId()
							: sessionService.getOrganizationId());
					pBillPayeeDetails
							.setUnitId(sale.getUnitId() != null ? sale.getUnitId() : sessionService.getUnitId());
					pBillPayeeDetails.setCreatedBy(
							sale.getAddedByUser() != null ? sale.getAddedByUser() : sessionService.getUserId());
					pBillPayeeDetails.setCreatedDate(TimeZoneComponent.getDateByzone(sale.getUnitId()).getTime());
					pBillPayeeDetails.settSaleId(sale.getId().intValue());
					pBillPayeeDetails.setStatus('A');
					GlobalDao.savePharamcyBillPayedetails(pBillPayeeDetails);
				}
			}
			CommonResponse response = new CommonResponse();
			response.setId(sale.getId());
			response.setMessage("Walk in sales bill save successfully");
			if (orderDetailsIds != null && orderDetailsIds.size() > 0)
				response.setIds(orderDetailsIds);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public  String getPrefixNumber(Integer storeId, Integer unitId, Integer screenId) {
		return iGlobalUnitMasterDao.getGeneratedNo(storeId, unitId, screenId);
	}

	@Override
	@Transactional
	public Response getTerrifData(PharmacyBillCommonDto dto) throws Exception {
		try {

			for (CommonBillingItemsDto item : dto.getItemList()) {
				List<Integer> prodCatId = new ArrayList<Integer>();
				prodCatId.add(item.getProductCategoryId());
				List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatId, dto.getPaymentEntitlementId());
				if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
					PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
					item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
					item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
				}

				PatientConsession patientConsession = null;
				if (dto.getEncounterId() != null && dto.getEncounterId() > 0) {
					patientConsession = GlobalDao.getPatientConsessionEncounter(item.getItemId(), dto.getEncounterId(),
							dto.getPatientCategoryId(), item.getAssetCategoryId(), item.getProductCategoryId(), null,
							null);
				} else if (dto.getAdmissionId() != null && dto.getAdmissionId() > 0) {
					patientConsession = GlobalDao.getPatientConsessionAdmission(item.getItemId(), dto.getAdmissionId(),
							dto.getPatientCategoryId(), item.getAssetCategoryId(), item.getProductCategoryId());
				}

				if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
					item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
					item.setMarkupStatus(patientConsession.isMarkupStatus());
				} else {
					item.setPatientClassPercentage(0);
					item.setMarkupStatus(false);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	// sales return
	@Override
	@Transactional
	public Response pharamcyPackageContractReturn(BillingMasterDto billDto, List<OrderDetailsMasterDto> dtlsList)
			throws Exception {
		try {
 
			if (billDto.getVisitTypeId() != null) 
			{ 
				Date date = TimeZoneComponent.getDateByzone(billDto.getUnitId());
				
				if(billDto.getReturnDate()!= null){
					SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");  
					try {
						date = formatter2.parse(billDto.getReturnDate());
					} catch (ParseException e) {
					logger.error("pharamcyPackageContractReturn failed", e);
					}
				}
				Boolean rcalculate = false;
				if(billDto.getPharmacyReturnServiceRecal() != null) {
					rcalculate = true;
				}
				billDto.setPharmacyReturnServiceRecal(null);
				
                Date orderDate = dtlsList.get(0).getOrderDateString()!=null ? GlobalCommonDateUtils.getDate(dtlsList.get(0).getOrderDateString(),"yyyy-MM-dd HH:mm:ss"):date;
                boolean isDummyService = dtlsList.get(0).getIsDummyService() ? true : false;
				Integer createdBy= dtlsList.get(0).getCreatedBy()!=null ? dtlsList.get(0).getCreatedBy():billDto.getCreatedBy();
                Double netAmount = 0.00;
				Double taxAmount = 0.00;
				Double ordTotalAmt = 0.00;
				Double totalSelfPayable = 0.00;
				Double totalCreditPayable = 0.00;
				Double totalPackagePayable = 0.00;
				Integer orderDetailsId = 0;
				Double toalDiscountAmount = 0.00;
				Double totalConcessionAmount = 0.00;
				// for Package sum
				Map<Integer, OrderDetailsPackageMapperDto> packageMapForSum = new HashMap<>();
				// for contract sum
				Map<Map<Integer,Integer>, OrderDetailsPayeeMapperDto> contractMapForSum = new HashMap<>();

				Set<Integer> payeeIds = new HashSet<>();
				List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<>();

				int orgId = billDto.getOrganisationId() != null ? billDto.getOrganisationId(): sessionService.getOrganizationId();
				int unitId = billDto.getUnitId() != null ? billDto.getUnitId() : sessionService.getUnitId();

				UnitApplicationConfigurationDto appConfigDto = iOrderMasterDao.getPharmacyServiceId(orgId, unitId);
				
				if(billDto.getConsolidated() != null && billDto.getConsolidated() == true){
					appConfigDto.setIsConsolidatedBill('Y');
				}
				
				boolean taxInclusive = appConfigDto.getTaxInclusive();
				AdmissionsForBatchProcessDto aBPdto = null;
				AdmissionListIpdChargesResDto admission = null;
				if (appConfigDto.getIsConsolidatedBill() == 'Y' && billDto.getEncounterId() != null
						&& billDto.getEncounterId() > 0) {
					aBPdto = iBillingMasterService.getBillByEncounterId(billDto.getEncounterId());
				} else if (billDto.getAdmissionId() != null) {
					Response admissionDto = ipdChargesDao.getAdmissionsById(billDto.getAdmissionId());
					admission = (AdmissionListIpdChargesResDto) admissionDto.getObject();
				}
				
				OrderMasterDto orderMasterDto = new OrderMasterDto();
					orderMasterDto.setPatientId(billDto.getPatientId());
					orderMasterDto.setOrgId(orgId);
					orderMasterDto.setOrgUnitId(unitId);
					orderMasterDto.setOrderDate(orderDate.getTime());
					orderMasterDto.setVisitTypeId(billDto.getVisitTypeId());
					orderMasterDto.setEncounterId(billDto.getEncounterId() != null ? billDto.getEncounterId() : null);
					orderMasterDto.setAdmissionId(billDto.getAdmissionId() != null ? billDto.getAdmissionId() : null);
					orderMasterDto.setOrderStatus('A');
					orderMasterDto.setOrderStatusId(1);// 1 for pending
					orderMasterDto.setCreatedDate(orderDate.getTime());
					orderMasterDto.setUpdatedDate(date.getTime());
					orderMasterDto.setIsPharmacy('Y');
					orderMasterDto.setCreatedBy(createdBy);
					orderMasterDto.setUpdatedBy(billDto.getCreatedBy());
					orderMasterDto.setReturnDate(billDto.getReturnDate()!= null ? billDto.getReturnDate(): null);
					orderMasterDto.setSaleId(dtlsList.get(0).getSaleId());
					if(billDto.getOrderDetailsMasterDtosList()!=null && billDto.getOrderDetailsMasterDtosList().get(0).getOrderId()>0)
						orderMasterDto.setOrderId(billDto.getOrderDetailsMasterDtosList().get(0).getOrderId());
					
					if (admission != null) 
					{
						orderMasterDto.setBillingBedCategoryId(admission.getBedBillingCategoryId());
						orderMasterDto.setPatientTypeId(admission.getPatientCategoryId());
						orderMasterDto.setPaymentEntitlementTypeId(admission.getPaymentEntitlementId());
						orderMasterDto.setReferralId(admission.getReferralId());
					}
					boolean isPreAuthExsist = iContractConsumptionDao.getPreAuthAmutByVisitId(billDto.getEncounterId(),	billDto.getAdmissionId());
				for (OrderDetailsMasterDto detail : dtlsList) 
				{
					// for Package
					Map<Integer, OrderDetailsPackageMapperDto> packageMap = new HashMap<>();

					// for contract
					Map<Map<Integer,Integer>, OrderDetailsPayeeMapperDto> contractMap = new HashMap<>();

					SaleDetail saleDetails = GlobalDao.getSaleDetailsById(detail.getSaleDetailId());

					Double ordTotalAmount = 0.00;
					Double ordTaxAmt = 0.00;
					Double ordNetAmount = 0.00;
					Double ordConcession = 0.00;
					Double discountAmt = 0.00;

					Double ordTaxPer = saleDetails.getTaxPercentage() == null ? 0 : saleDetails.getTaxPercentage();
					
					ordTotalAmount = GlobalCommonDateUtils.getAmount(detail.getOrderQty().intValue() * saleDetails.getRate());
					ordConcession = GlobalCommonDateUtils.getAmount(saleDetails.getConsessionPercentage()*0.01*ordTotalAmount);
					discountAmt = GlobalCommonDateUtils.getAmount(saleDetails.getDiscountPercentage()*0.01*(ordTotalAmount-ordConcession));
					ordNetAmount = GlobalCommonDateUtils.getAmount(ordTotalAmount-(ordConcession+discountAmt));
					ordTaxAmt = ((ordTotalAmount-(ordConcession+discountAmt)) * (ordTaxPer * 0.01));
					if (!taxInclusive)
						ordNetAmount = GlobalCommonDateUtils.getAmount(ordNetAmount + ordTaxAmt);

					taxAmount = GlobalCommonDateUtils.getAmount(taxAmount + ordTaxAmt);
					ordTotalAmt = GlobalCommonDateUtils.getAmount(ordTotalAmt + ordTotalAmount);
					toalDiscountAmount = GlobalCommonDateUtils.getAmount(toalDiscountAmount + discountAmt);
					totalConcessionAmount = GlobalCommonDateUtils.getAmount(totalConcessionAmount + ordConcession);
					
					OrderDetailsMasterDto dto = new OrderDetailsMasterDto();
						if(detail.getOrderDetailsId()!=null && detail.getOrderDetailsId()>0)
						{
							dto.setUpdatedBy(billDto.getUpdatedBy());
							dto.setUpdatedDate(date.getTime());
							dto.setCreatedDate(date.getTime());
							dto.setOrderDetailsId(detail.getOrderDetailsId());
						}else{
							dto.setUpdatedBy(createdBy);
							dto.setCreatedDate(orderDate.getTime());
							dto.setUpdatedDate(date.getTime());
						}
						dto.setCreatedBy(createdBy);
						dto.setSaleDetailId(detail.getSaleDetailId());
						dto.setOrderQty(detail.getOrderQty());
						dto.setTaxId(detail.getTaxId());
						dto.setServiceAmt(BigDecimal.valueOf(saleDetails.getRate()));
						dto.setTaxPer(BigDecimal.valueOf(ordTaxPer));
						dto.setTaxAmount(BigDecimal.valueOf(ordTaxAmt));
						dto.setOrdTotalAmt(BigDecimal.valueOf(ordTotalAmount));
						dto.setOrdConcession(BigDecimal.valueOf(ordConcession));
						dto.setOrdConcessionPer(BigDecimal.valueOf(saleDetails.getConsessionPercentage()));
						dto.setNetAmount(BigDecimal.valueOf(ordNetAmount));
						dto.setOrdDiscount(BigDecimal.valueOf(discountAmt));
						dto.setSelfPayable(BigDecimal.ZERO);
						dto.setCreditPayable(BigDecimal.ZERO);
						dto.setPackageConsumptionAmt(BigDecimal.ZERO);
						dto.setBatchId(detail.getBatchId());
						dto.setDrugId(detail.getDrugId());
						dto.setIsDrug('Y');
						dto.setServiceIsBilled(0);
						dto.setPayeeId(saleDetails.getPayeeId());
						dto =  billingCommonMethods.setDefaultOrderDetailsParameter(dto,orderDate,orgId,unitId);
					
					payeeIds.add(saleDetails.getPayeeId());
					double amountCheck = ordNetAmount;
					
					Boolean secondParmacyService = false;
					Double parentServicePackageConsumtion = 0.0;
					Double returnServicePackageConsumtion = 0.0;
					Boolean checkForceInOut = false;
					Boolean previeusRetrunServiceStatus = false;
					Double currentCapForSelfAndIns = 0.0;
					Response orderList =  iBillingMasterService.getPackageConsumeAmountItemwise(saleDetails.getId(),detail.getOrderDetailsId());
					
					if(orderList.getListObject() != null && orderList.getListObject().size()>0) {
						
						List<OrderDetailsPackageMapperDto> list = orderList.getListObject();
						
						for (OrderDetailsPackageMapperDto returnService : list) {
							if(returnService.getPackageQty() >0 && returnService.getForceAddRemoved()!= null && returnService.getForceAddRemoved()!=0) {//Package Qty used as order Qty
								
								checkForceInOut = true;
								
							}else if(returnService.getPackageQty() <0 && returnService.getIsPackageService()== 'N') {
								
								previeusRetrunServiceStatus = true;
							}
						}
						
						if(checkForceInOut == false && previeusRetrunServiceStatus == false) {
							
							for (OrderDetailsPackageMapperDto returnService : list) {
								
								if(returnService.getPackageQty() <0) {//Package Qty used as order Qty
									
									returnServicePackageConsumtion = returnServicePackageConsumtion + returnService.getPackageAmount().doubleValue();
									
								}else {
									
									parentServicePackageConsumtion = parentServicePackageConsumtion + returnService.getPackageAmount().doubleValue();
									
								}
							}
							
							Double parentAndReturnServicePackage = parentServicePackageConsumtion + returnServicePackageConsumtion; 
							
							if(parentAndReturnServicePackage < amountCheck) {
								currentCapForSelfAndIns = amountCheck - parentAndReturnServicePackage;
								amountCheck = parentAndReturnServicePackage;
							}
						}
						
					}
					
					
					List<SaleDetailPackageMapperDto> packageList = GlobalDao.getSalePackageMpperListbyDetailsIdReturn(detail.getSaleDetailId(), BigDecimal.valueOf(ordNetAmount));
					// For Package
				
					for (SaleDetailPackageMapperDto mppr:packageList) 
					{
						if(amountCheck!=0)
						{
							if (packageMap.containsKey(mppr.getPackageConsumptionMasterId())) 
							{
								mppr.setPackageAmount(amountCheck<=mppr.getPackageAmount() ? amountCheck:mppr.getPackageAmount());
								
								OrderDetailsPackageMapperDto mpprDto = packageMap.get(mppr.getPackageConsumptionMasterId());
								mpprDto.setPackageAmount(mpprDto.getPackageAmount().add(BigDecimal.valueOf(mppr.getPackageAmount())));
								//mpprDto.setServiceAmount(mpprDto.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
								packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);

								if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) 
								{
									OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum.get(mppr.getPackageConsumptionMasterId());
									mpprDtofinal.setPackageAmount(mpprDtofinal.getPackageAmount().add(BigDecimal.valueOf(mppr.getPackageAmount())));
									//mpprDtofinal.setServiceAmount(mpprDtofinal.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
									packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
								}

							} else {
								
								OrderDetailsPackageMapperDto mpprDto =  billingCommonMethods.createOrderPackageMapperForPharmacyObj(mppr,null,orgId,unitId,detail);

								packageMap.put(mppr.getPackageConsumptionMasterId(), mpprDto);
								
								OrderDetailsPackageMapperDto mpprDtoSum =  billingCommonMethods.createOrderPackageMapperForPharmacyObj(mppr,null,orgId,unitId,detail);

								if (packageMapForSum.containsKey(mppr.getPackageConsumptionMasterId())) 
								{
									OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum.get(mppr.getPackageConsumptionMasterId());
									mpprDtofinal.setPackageAmount(mpprDtofinal.getPackageAmount().add(BigDecimal.valueOf(mppr.getPackageAmount())));
									//mpprDtofinal.setServiceAmount(mpprDtofinal.getServiceAmount().add(BigDecimal.valueOf(mppr.getRate())));
									packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtofinal);
								} else {
									packageMapForSum.put(mppr.getPackageConsumptionMasterId(), mpprDtoSum);
								}
							}
							dto.setPackageConsumptionAmt(dto.getPackageConsumptionAmt().add(BigDecimal.valueOf(mppr.getPackageAmount())));
							totalPackagePayable = GlobalCommonDateUtils.getAmount(totalPackagePayable + mppr.getPackageAmount().doubleValue());
							amountCheck = GlobalCommonDateUtils.getAmountCap(amountCheck-mppr.getPackageAmount().doubleValue() < 0 ? 0 : amountCheck-mppr.getPackageAmount().doubleValue());
						}else
							break;
					}
					
					if(amountCheck!=0)
					{
						if(!packageMapForSum.isEmpty() && packageMapForSum.containsKey(null))
						{
							OrderDetailsPackageMapperDto mpprDtofinal = packageMapForSum.get(null);
							mpprDtofinal.setServiceAmount(mpprDtofinal.getServiceAmount().add(BigDecimal.valueOf(ordNetAmount.equals(amountCheck) ? ordTotalAmount : amountCheck)));
							packageMapForSum.put(null, mpprDtofinal);
							
						}else{
							
							OrderDetailsPackageMapperDto mpprDto = new OrderDetailsPackageMapperDto();
								mpprDto.setOrganizationId(orgId);
								mpprDto.setUnitId(unitId);
								mpprDto.setStatus('A');
								mpprDto.setIsPackageService('N');
								mpprDto.setItemId(saleDetails.getItemId() != null ? saleDetails.getItemId().intValue() : null);
								mpprDto.settSaleDetailsId(saleDetails.getId() != null ? saleDetails.getId().intValue() : null);
								mpprDto.setServiceAmount(BigDecimal.valueOf(ordNetAmount.equals(amountCheck) ? ordTotalAmount : amountCheck));
								mpprDto.setPackageAmount(BigDecimal.ZERO);
								mpprDto.setIsCapService('N');
						
							OrderDetailsPackageMapperDto mpprDtoSum = new OrderDetailsPackageMapperDto();
								mpprDtoSum.setOrganizationId(orgId);
								mpprDtoSum.setUnitId(unitId);
								mpprDtoSum.setStatus('A');
								mpprDtoSum.setIsPackageService('N');
								mpprDtoSum.setItemId(saleDetails.getItemId() != null ? saleDetails.getItemId().intValue() : null);
								mpprDtoSum.settSaleDetailsId(saleDetails.getId() != null ? saleDetails.getId().intValue() : null);
								mpprDtoSum.setServiceAmount(BigDecimal.valueOf(ordNetAmount.equals(amountCheck) ? ordTotalAmount : amountCheck));
								mpprDtoSum.setPackageAmount(BigDecimal.ZERO);
								mpprDtoSum.setIsCapService('N');
							packageMap.put(null,mpprDto);
							packageMapForSum.put(null, mpprDtoSum);
						}
					}else if(amountCheck ==0 && packageMapForSum.isEmpty()){
						
						OrderDetailsPackageMapperDto mpprDto = new OrderDetailsPackageMapperDto();
						mpprDto.setOrganizationId(orgId);
						mpprDto.setUnitId(unitId);
						mpprDto.setStatus('A');
						mpprDto.setIsPackageService('N');
						mpprDto.setItemId(saleDetails.getItemId() != null ? saleDetails.getItemId().intValue() : null);
						mpprDto.settSaleDetailsId(saleDetails.getId() != null ? saleDetails.getId().intValue() : null);
						mpprDto.setServiceAmount(BigDecimal.valueOf(ordNetAmount.equals(amountCheck) ? ordTotalAmount : amountCheck));
						mpprDto.setPackageAmount(BigDecimal.ZERO);
						mpprDto.setIsCapService('N');
				
						OrderDetailsPackageMapperDto mpprDtoSum = new OrderDetailsPackageMapperDto();
						mpprDtoSum.setOrganizationId(orgId);
						mpprDtoSum.setUnitId(unitId);
						mpprDtoSum.setStatus('A');
						mpprDtoSum.setIsPackageService('N');
						mpprDtoSum.setItemId(saleDetails.getItemId() != null ? saleDetails.getItemId().intValue() : null);
						mpprDtoSum.settSaleDetailsId(saleDetails.getId() != null ? saleDetails.getId().intValue() : null);
						mpprDtoSum.setServiceAmount(BigDecimal.valueOf(ordNetAmount.equals(amountCheck) ? ordTotalAmount : amountCheck));
						mpprDtoSum.setPackageAmount(BigDecimal.ZERO);
						mpprDtoSum.setIsCapService('N');
						
						packageMap.put(null,mpprDto);
						packageMapForSum.put(null, mpprDtoSum);
					
					
					}

					
				
					
					// pacakge
					List<OrderDetailsPackageMapperDto> ordDtlsPkgMppr = new ArrayList<OrderDetailsPackageMapperDto>();
					for (Entry<Integer, OrderDetailsPackageMapperDto> entry : packageMap.entrySet()) 
					{
						OrderDetailsPackageMapperDto ordDto = entry.getValue();
						ordDto.setPackageAmount(ordDto.getPackageAmount().negate());
						ordDtlsPkgMppr.add(ordDto);
					}
					
				
					
					
					List<SaleDetailPayeeMapperDto> payeeList = GlobalDao.getSalePayeeMpperListbyDetailsIdReturn(detail.getSaleDetailId());
					
					double selfPayeeAmount = 0.0;
					double selfPayeePer = 0.0;
					double selfPayeeRemaningAmount = 0.0;
				
					amountCheck = amountCheck + currentCapForSelfAndIns;
					// For Contract
					int count  = 0 ;
					for (SaleDetailPayeeMapperDto mppr : payeeList) 
					{
						count = count +1;
						if(amountCheck!=0 )
						{
							
							if(amountCheck >= mppr.getPayeeAmount().doubleValue() || count == payeeList.size()) {
								
								mppr.setPayeeAmount(amountCheck<=mppr.getPayeeAmount().doubleValue() ? BigDecimal.valueOf(amountCheck):mppr.getPayeeAmount());
								
								Map<Integer,Integer> payeeMap = new HashMap<>();
									payeeMap.put(mppr.getPayeeId(), mppr.getAssociateCompanyId()!=null ? mppr.getAssociateCompanyId():0);
								if (contractMap.containsKey(payeeMap)) 
								{
									OrderDetailsPayeeMapperDto mpprDto = contractMap.get(payeeMap);
									mpprDto.setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
									contractMap.put(payeeMap, mpprDto);
									if (contractMapForSum.containsKey(payeeMap)) 
									{
										OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(payeeMap);
											mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
										contractMapForSum.put(payeeMap, mpprDtofinal);
									}
								} else {
									
									
									OrderDetailsPayeeMapperDto mpprDto =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billDto);
									mpprDto.setAssociateCompanyId(mppr.getAssociateCompanyId());
								
									contractMap.put(payeeMap, mpprDto);
									
									OrderDetailsPayeeMapperDto mpprDtoSum =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billDto);
									mpprDtoSum.setAssociateCompanyId(mppr.getAssociateCompanyId());
									
									if (contractMapForSum.containsKey(payeeMap)) 
									{
										OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(payeeMap);
										mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
										contractMapForSum.put(payeeMap, mpprDtofinal);
									} else {
										contractMapForSum.put(payeeMap, mpprDtoSum);
									}
								}

								if (appConfigDto.getSelfPayeeId() != null && !appConfigDto.getSelfPayeeId().equals(mppr.getPayeeId())) 
								{
									dto.setCreditPayable(dto.getCreditPayable().add(mppr.getPayeeAmount()));
									totalCreditPayable = GlobalCommonDateUtils.getAmount(totalCreditPayable + mppr.getPayeeAmount().doubleValue());
								} else {
									dto.setSelfPayable(dto.getSelfPayable().add(mppr.getPayeeAmount()));
									totalSelfPayable = GlobalCommonDateUtils.getAmount(totalSelfPayable + mppr.getPayeeAmount().doubleValue());
									selfPayeeRemaningAmount = GlobalCommonDateUtils.getAmountCap(selfPayeeRemaningAmount-mppr.getPayeeAmount().doubleValue() < 0 ? 0 : selfPayeeRemaningAmount-mppr.getPayeeAmount().doubleValue());
								}
								amountCheck = GlobalCommonDateUtils.getAmountCap(amountCheck-mppr.getPayeeAmount().doubleValue() < 0 ? 0 : amountCheck-mppr.getPayeeAmount().doubleValue());
								netAmount = GlobalCommonDateUtils.getAmount(netAmount + mppr.getPayeeAmount().doubleValue());
								
							}
							
						}else
							break;
					}
					
					
					if(amountCheck!=0 )
					{
						
						
						for (SaleDetailPayeeMapperDto mppr : payeeList) 
						{
							if(amountCheck!=0 )
							{
								if( mppr.getPayeeId() != appConfigDto.getSelfPayeeId().intValue()) {
									
									mppr.setPayeeAmount(amountCheck<=mppr.getPayeeAmount().doubleValue() ? BigDecimal.valueOf(amountCheck):mppr.getPayeeAmount());
									
									Map<Integer,Integer> payeeMap = new HashMap<>();
										payeeMap.put(mppr.getPayeeId(), mppr.getAssociateCompanyId()!=null ? mppr.getAssociateCompanyId():0);
									if (contractMap.containsKey(payeeMap)) 
									{
										OrderDetailsPayeeMapperDto mpprDto = contractMap.get(payeeMap);
										mpprDto.setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
										contractMap.put(payeeMap, mpprDto);
										if (contractMapForSum.containsKey(payeeMap)) 
										{
											OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(payeeMap);
												mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
											contractMapForSum.put(payeeMap, mpprDtofinal);
										}
									} else {
										
										
										OrderDetailsPayeeMapperDto mpprDto =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billDto);
										mpprDto.setAssociateCompanyId(mppr.getAssociateCompanyId());
									
										contractMap.put(payeeMap, mpprDto);
										
										OrderDetailsPayeeMapperDto mpprDtoSum =  billingCommonMethods.createOrderPayeeMapperObj(mppr,billDto);
										mpprDtoSum.setAssociateCompanyId(mppr.getAssociateCompanyId());
										
										if (contractMapForSum.containsKey(payeeMap)) 
										{
											OrderDetailsPayeeMapperDto mpprDtofinal = contractMapForSum.get(payeeMap);
											mpprDtofinal.setPayeeAmount(mpprDtofinal.getPayeeAmount().add(mppr.getPayeeAmount()));
											contractMapForSum.put(payeeMap, mpprDtofinal);
										} else {
											contractMapForSum.put(payeeMap, mpprDtoSum);
										}
									}

									if (appConfigDto.getSelfPayeeId() != null && !appConfigDto.getSelfPayeeId().equals(mppr.getPayeeId())) 
									{
										dto.setCreditPayable(dto.getCreditPayable().add(mppr.getPayeeAmount()));
										totalCreditPayable = GlobalCommonDateUtils.getAmount(totalCreditPayable + mppr.getPayeeAmount().doubleValue());
									} else {
										dto.setSelfPayable(dto.getSelfPayable().add(mppr.getPayeeAmount()));
										totalSelfPayable = GlobalCommonDateUtils.getAmount(totalSelfPayable + mppr.getPayeeAmount().doubleValue());
									}
									amountCheck = GlobalCommonDateUtils.getAmountCap(amountCheck-mppr.getPayeeAmount().doubleValue() < 0 ? 0 : amountCheck-mppr.getPayeeAmount().doubleValue());
									netAmount = GlobalCommonDateUtils.getAmount(netAmount + mppr.getPayeeAmount().doubleValue());
									
								}
								
							}else
								break;
						}
						
						
						
					}
					
					
					List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList = new ArrayList<OrderDetailsPayeeMapperDto>();
					for (Map.Entry<Map<Integer,Integer>, OrderDetailsPayeeMapperDto> entry : contractMap.entrySet()) 
					{
						OrderDetailsPayeeMapperDto ordDto = entry.getValue();
						ordDto.setPayeeAmount(ordDto.getPayeeAmount().negate());
						orderDetailsPayeeList.add(ordDto);
					}
					dto.setOrderDetailsPackageMapperDtosList(ordDtlsPkgMppr);
					dto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeList);
					
					// sub net amount
					
					dto.setServiceAmt(dto.getServiceAmt());
					dto.setOrdTotalAmt(dto.getOrdTotalAmt().negate());
					dto.setNetAmount(dto.getNetAmount().negate());
					dto.setOrdDiscount(dto.getOrdDiscount().negate());
					dto.setOrdConcession(dto.getOrdConcession().negate());
					dto.setSelfPayable(dto.getSelfPayable().negate());
					dto.setCreditPayable(dto.getCreditPayable().negate());
					dto.setPackageConsumptionAmt(dto.getPackageConsumptionAmt().negate());
					dto.setOrderQty(dto.getOrderQty() * -1);
					if(dto.getNetAmount().equals(dto.getPackageConsumptionAmt()))
						dto.setNetAmount(BigDecimal.ZERO);
					else
						dto.setNetAmount(dto.getNetAmount().subtract(dto.getPackageConsumptionAmt()));
					dto.setServiceRendered(1);
					dto.setServiceIsBilled((aBPdto != null && aBPdto.getBillId() != null && aBPdto.getBillId() > 0) ? 1 : 0);
					orderDetailsList.add(dto);

					// add Cap to Package Contract
					CommonBillingItemsDto itemReq = new CommonBillingItemsDto();
					itemReq.setPatientId(billDto.getPatientId());
					itemReq.setAdmissionId(billDto.getAdmissionId() != null ? billDto.getAdmissionId() : null);
					itemReq.setEncounterId(billDto.getEncounterId() != null ? billDto.getEncounterId() : null);
					itemReq.setProductCategoryId(detail.getProductCategoryId());
					itemReq.setSaleDetailPackageMapperDtoList(packageList);
					itemReq.setSaleDetailPayeeMapperDtoList(payeeList);
					itemReq.setPreAuthExist(isPreAuthExsist);
					
					if(rcalculate == false) {
						this.consumePackageContractForReturnItems(itemReq);
					}
					
					
				}

				OrderDetailsMasterDto dtoToAdd = new OrderDetailsMasterDto();
					if(billDto.getOrderDetailsMasterDtosList()!=null && billDto.getOrderDetailsMasterDtosList().get(0).getOrderId()>0)
					{
						dtoToAdd.setOrderDetailsId(billDto.getOrderDetailsMasterDtosList().get(0).getOrderDetailsId());
						dtoToAdd.setUpdatedBy(billDto.getUpdatedBy());
						dtoToAdd.setUpdatedDate(date.getTime());
						dtoToAdd.setCreatedDate(orderDate.getTime());
					}else{
						dtoToAdd.setUpdatedBy(createdBy);
						dtoToAdd.setCreatedDate(orderDate.getTime());
						dtoToAdd.setUpdatedDate(date.getTime());
					}
					dtoToAdd.setCreatedBy(createdBy);
					dtoToAdd.setServiceId(appConfigDto.getPharmacyServiceId());
					dtoToAdd.setDepartmentId(appConfigDto.getServiceSpecilaityId());
					dtoToAdd.setIsRefDoctorShare(appConfigDto.getIsRefDoctorShare());
					dtoToAdd.setOrderQty(1);
					if(totalPackagePayable>0)
					{
						dtoToAdd.setServiceAmt(BigDecimal.valueOf(netAmount + totalPackagePayable));
						dtoToAdd.setOrderQty(dtoToAdd.getOrderQty() * -1);
						dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(netAmount + totalPackagePayable));
						dtoToAdd.setOrdConcession(BigDecimal.ZERO);
						dtoToAdd.setPatientClassPercentage(BigDecimal.ZERO);
						dtoToAdd.setOrdDiscount(BigDecimal.ZERO);
						dtoToAdd.setTaxPer(BigDecimal.ZERO);
						dtoToAdd.setTaxAmount(BigDecimal.ZERO);
						/*dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
						dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
						dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
						dtoToAdd.setPackageConsumptionAmt(BigDecimal.valueOf(totalPackagePayable));*/
					}else{
						
						Double amountApplicableForTax = (ordTotalAmt-totalConcessionAmount)-toalDiscountAmount;
						
						dtoToAdd.setServiceAmt(BigDecimal.valueOf(ordTotalAmt));
						dtoToAdd.setOrderQty(dtoToAdd.getOrderQty() * -1);
						dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(ordTotalAmt));
						dtoToAdd.setOrdConcession(BigDecimal.valueOf(totalConcessionAmount));
                        dtoToAdd.setPatientClassPercentage((totalConcessionAmount/ordTotalAmt) >0 ? BigDecimal.valueOf((totalConcessionAmount/ordTotalAmt)*100) :BigDecimal.ZERO);
						dtoToAdd.setOrdDiscount(BigDecimal.valueOf(toalDiscountAmount));
						dtoToAdd.setTaxAmount(taxInclusive ? BigDecimal.ZERO: BigDecimal.valueOf(taxAmount));
						dtoToAdd.setTaxPer((!taxInclusive && ordTotalAmt>0) ? BigDecimal.valueOf((taxAmount/amountApplicableForTax)*100) : BigDecimal.ZERO);
						/*dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
						dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
						dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
						dtoToAdd.setPackageConsumptionAmt(BigDecimal.valueOf(totalPackagePayable));*/
					}
					
					dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
					dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
					dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
					dtoToAdd.setPackageConsumptionAmt(BigDecimal.valueOf(totalPackagePayable));
					dtoToAdd.setServiceIsBilled((aBPdto != null && aBPdto.getBillId() != null && aBPdto.getBillId() > 0) ? 1 : 0);
					dtoToAdd.setIsDrug('N');
					if(dtoToAdd.getOrdDiscount().doubleValue() > 0.00){
	                if(billDto.isRecalculation() && dtoToAdd.getOrdDiscount().doubleValue() > 0.00){
	                    dtoToAdd.setPharmacyDiscountOverride(0);
	                }else{
	                    dtoToAdd.setPharmacyDiscountOverride(1);
	                }
					}
					
					dtoToAdd =  billingCommonMethods.setDefaultOrderDetailsParameter(dtoToAdd,orderDate,orgId,unitId);
					
				/*	dtoToAdd.setOrderSchDate(orderDate.getTime());
					dtoToAdd.setOrderDate(orderDate.getTime());
					dtoToAdd.setOrdRenderDatetime(orderDate.getTime());
					dtoToAdd.setIsPackage(false);
					dtoToAdd.setIsCapService('N');
					dtoToAdd.setIsInvestigationOrder('N');
					dtoToAdd.setIsProcedureService('N');
					dtoToAdd.setIsProcedure('N');
					dtoToAdd.setOrderEmergencyFlag('N');
					dtoToAdd.setIsAppointableRadiology('N');
					dtoToAdd.setOrdCancelled('N');
					dtoToAdd.setStatus('A');
					dtoToAdd.setOrgId(orgId);
					dtoToAdd.setOrgUnitId(unitId);
					dtoToAdd.setServiceRendered(1);
					dtoToAdd.setIsOutsourced('N');
					dtoToAdd.setIsConsultationService('N');
					dtoToAdd.setDebitNoteService(false);
					dtoToAdd.setPriorityId(2);*/
					
					
					dtoToAdd.setTariffId(appConfigDto.getDefaultSelfTariffId());
					dtoToAdd.setServiceAmt(dtoToAdd.getServiceAmt());
					dtoToAdd.setOrdTotalAmt(dtoToAdd.getOrdTotalAmt().negate());
					dtoToAdd.setOrdDiscount(dtoToAdd.getOrdDiscount().negate());
					dtoToAdd.setOrdConcession(dtoToAdd.getOrdConcession().negate());
					dtoToAdd.setTaxAmount(dtoToAdd.getTaxAmount().negate());
					dtoToAdd.setNetAmount(dtoToAdd.getNetAmount().negate());
					dtoToAdd.setSelfPayable(dtoToAdd.getSelfPayable().negate());
					dtoToAdd.setCreditPayable(dtoToAdd.getCreditPayable().negate());
					dtoToAdd.setPackageConsumptionAmt(dtoToAdd.getPackageConsumptionAmt().negate());
					if(isDummyService)
						dtoToAdd.setDummyService(isDummyService);
					if (admission != null) {
						dtoToAdd.setBedId(admission.getBedId());
						dtoToAdd.setWardId(admission.getWardId());
						dtoToAdd.setRoomId(admission.getRoomId());
						dtoToAdd.setBedCategoryId(admission.getBedBillingCategoryId());
					}
					if (aBPdto != null) {
						dtoToAdd.setBedCategoryId(aBPdto.getBillingBedCategoryId());
					}

				Iterator<Integer> iterator = payeeIds.iterator();
				while (iterator.hasNext()) {
					Integer payeeId = (Integer) iterator.next();
					if (payeeId!=null && payeeId.intValue() != appConfigDto.getSelfPayeeId().intValue()) {
						dtoToAdd.setPayeeId(payeeId);
					}
				}

				// FinalList CONTRACT
				
				boolean selfEntry = false;
				
				List<OrderDetailsPayeeMapperDto> finalMpprList = new ArrayList<OrderDetailsPayeeMapperDto>();
				for (Map.Entry<Map<Integer,Integer>, OrderDetailsPayeeMapperDto> entry : contractMapForSum.entrySet()) 
				{
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					entry.getValue().setPayeeAmount(entry.getValue().getPayeeAmount().negate());
					finalMpprList.add(entry.getValue());
					
					if(entry.getValue().getPayeeId().equals(appConfigDto.getSelfPayeeId()))
						selfEntry = true;
						
				}
				
				if(selfEntry==false)
				{
					OrderDetailsPayeeMapperDto detailsPayeeMapper = new OrderDetailsPayeeMapperDto();
						detailsPayeeMapper.setPayeeId(appConfigDto.getSelfPayeeId());
						detailsPayeeMapper.setPayeeAmount(BigDecimal.ZERO);
						detailsPayeeMapper.setServiceId(dtoToAdd.getServiceId());
						detailsPayeeMapper.setOrganizationId(dtoToAdd.getOrgId());
						detailsPayeeMapper.setUnitId(dtoToAdd.getOrgUnitId());
						detailsPayeeMapper.setStatus('A');
					finalMpprList.add(detailsPayeeMapper);
				}

				// FinalList PACKAGE
				List<OrderDetailsPackageMapperDto> finalPkgMpprList = new ArrayList<OrderDetailsPackageMapperDto>();
				for (Map.Entry<Integer, OrderDetailsPackageMapperDto> entry : packageMapForSum.entrySet()) 
				{
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					//entry.getValue().setServiceAmount(BigDecimal.valueOf(entry.getValue().getIsPackageService()=='Y' && totalPackagePayable>0 ? netAmount : ordTotalAmt));
					if(entry.getValue().getIsPackageService().equals('N'))
						entry.getValue().setServiceAmount(totalPackagePayable>0 ? BigDecimal.valueOf(netAmount) : BigDecimal.valueOf(ordTotalAmt));
					
					entry.getValue().setPackageAmount(entry.getValue().getPackageAmount().negate());
					finalPkgMpprList.add(entry.getValue());
				}

				dtoToAdd.setOrderDetailsPayeeMapperDtosList(finalMpprList);
				dtoToAdd.setOrderDetailsPackageMapperDtosList(finalPkgMpprList);

				double coShare = (dtoToAdd.getSelfPayable().doubleValue()/dtoToAdd.getNetAmount().doubleValue())*100;
				if(finalMpprList.size()>1)
					dtoToAdd.setCoPayPer(new BigDecimal(coShare));
				else
					dtoToAdd.setCoPayPer(new BigDecimal(100));
			
				
				dtoToAdd.setServiceRendered(1);
				
				orderDetailsList.add(dtoToAdd);
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);

				if((appConfigDto.getIsConsolidatedBill() == 'Y' && billDto.getVisitTypeId()==OP) || billDto.getVisitTypeId()!=OP) 
				{
					Response res2 = iOrderMasterService.saveOrderMaster(orderMasterDto);
					if (res2.getResObject() != null) 
					{
						orderDetailsId = (Integer) res2.getResObject();
						if (aBPdto != null && aBPdto.getBillId() != null && aBPdto.getBillId() > 0) 
						{
							OrderDetailsMaster orderDetailsMaster = mapper.map(dtoToAdd, OrderDetailsMaster.class,
									"OrderDetailsMasterDto_to_OrderDetailsMaster");
							orderDetailsMaster.setOrderDetailsId(orderDetailsId);



						}
					}
				}
				return new Response(SUCCESS, null, null, null, orderDetailsId);
			}
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public List<AllDataResponse> allExpiredDataList(Integer itemid, Integer storeId, Integer unitId) {

		List<AllDataResponse> list = WalkInSalesDao.allExpiredDataList(itemid, storeId, unitId);
		return list;

	}

	@Override
	@Transactional
	public CommonResponse deleteAllocationNew(Integer screenId, Long tempBillId, List<Long> allocationIds,
			Integer unitId, Integer storeId, Integer userId) {

		if (allocationIds != null) {
			for (Long allocationId : allocationIds) {
				BatchAllocation batchAllocation = WalkInSalesDao.getBatchAllocationsNew(allocationId);

				if (batchAllocation != null) {
					batchAllocation.setUnitId(unitId);
					// added above if condition for resolved HIS-10548 Walk In Prescription: After
					// click on Clear button , throwing an error.
					if (revertAllocatedItemQuantityWalkin(batchAllocation, screenId, storeId, userId)) {

						WalkInSalesDao.deleteBatchAllocation(batchAllocation.getId());
					}
				}
			}
		} else {
			for (BatchAllocation batchAllocation : WalkInSalesDao.getBatchAllocations(tempBillId)) {
				batchAllocation.setUnitId(unitId);
				if (revertAllocatedItemQuantityWalkin(batchAllocation, screenId, storeId, userId)) {
					WalkInSalesDao.deleteBatchAllocation(batchAllocation.getId());
				}
			}
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Item quantity allocation deleted successfully");
		return response;

	}

	@Override
	@Transactional
	public CommonResponse revertStock(Integer orgId, Integer unitId) {
		for (BatchAllocation batchAllocation : WalkInSalesDao.getBatchAllocation(orgId, unitId)) {

			Long batchId = batchAllocation.getBatch().getId();
			Integer storeId = batchAllocation.getStore().getStoreId();

			for (StoreStock s : batchAllocation.getBatch().getStoreStockList()) {
				if (s.getStore().getStoreId().equals(storeId)) {
					Integer avaialableStoreStock = s.getAvailableQty();

					avaialableStoreStock = avaialableStoreStock + batchAllocation.getQuantity();
					if (avaialableStoreStock < 0) {
						avaialableStoreStock = 0;
					}

					s.setAvailableQty(avaialableStoreStock);
					GlobalDao.updateStoreStockLeaseQuantity(s);
					WalkInSalesDao.deleteBatchAllocation(batchAllocation.getId());
				}

			}

		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Item quantity allocation deleted successfully");
		return response;
	}

	@Override
	@Transactional
	public Integer inactiveSaleBySaleId(Integer saleId) throws Exception {
		try {
			return WalkInSalesDao.inactiveSaleBySaleId(saleId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response applyDiscountOnPharamcyReturn(List<OrderDetailsMasterDto> dtlsList,	BillingMasterDto billDto) throws Exception 
	{
		OrderDetailsMasterDto mainOrderDetail = dtlsList.get(0);
		
		List<OrderDetailsMaster> detailsList = null;
		Response res = iOrderMasterDao.getMedicationsByOrderId(mainOrderDetail.getOrderId());
		if (res.getListObject() != null && res.getListObject().size() > 0) {
			detailsList = res.getListObject();
		}
		
		for(OrderDetailsMaster detailsMaster : detailsList)
		{
			double ordTotalAmt = detailsMaster.getOrdTotalAmt().doubleValue();
			double concessionPer = mainOrderDetail.getPatientClassPercentage().doubleValue();
			double discountPer = mainOrderDetail.getOrdDiscountPer().doubleValue();
			double concession = (ordTotalAmt*concessionPer*0.01) ;
			double discount = ((ordTotalAmt-concession)*discountPer*0.01) ;
			double netAmount = ordTotalAmt-(concession+discount);
			double oldNetAmount = detailsMaster.getNetAmount().doubleValue();
			double coShare = 0.00;
			double selfPayable = 0.00;
			double creditPayable = 0.00;
			if(detailsMaster.getCreditPayable().doubleValue()!=0)
			{
				coShare = detailsMaster.getSelfPayable().doubleValue()!=0.00 ? (detailsMaster.getSelfPayable().doubleValue()/detailsMaster.getNetAmount().doubleValue())*100 : 0.00;
				selfPayable = coShare*0.01*netAmount;
				creditPayable = netAmount-selfPayable;
			}else{
				coShare = 100.00;
				selfPayable = netAmount;
				creditPayable = 0.00;
			}
			detailsMaster.setPatientClassPercentage(BigDecimal.valueOf(concessionPer));
			detailsMaster.setOrdConcession(BigDecimal.valueOf(concession));
			detailsMaster.setOrdDiscount(BigDecimal.valueOf(discount));
			detailsMaster.setNetAmount(BigDecimal.valueOf(netAmount));
			detailsMaster.setSelfPayable(BigDecimal.valueOf(selfPayable));
			detailsMaster.setCreditPayable(BigDecimal.valueOf(creditPayable));
			
			Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(detailsMaster.getOrderDetailsId(),null);
			List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = res2.getListObject();
			
            long count = orderDetailsPayeeMapperDtosList.stream().filter(obj-> !obj.getPayeeId().equals(billDto.getSelfPayeeId())).count();
            
            for(int i=0; i<orderDetailsPayeeMapperDtosList.size(); i++)
            {
                OrderDetailsPayeeMapperDto detailsPayeeMapperDto = orderDetailsPayeeMapperDtosList.get(i);
                if(detailsPayeeMapperDto.getPayeeId().equals(billDto.getSelfPayeeId()))
                    detailsPayeeMapperDto.setPayeeAmount(detailsMaster.getSelfPayable());
                else{
                    if(count==1)
                    {
                        detailsPayeeMapperDto.setPayeeAmount(detailsMaster.getCreditPayable());
                    }else{
                        double share = detailsPayeeMapperDto.getPayeeAmount().doubleValue()!=0.00 ? (detailsPayeeMapperDto.getPayeeAmount().doubleValue()/oldNetAmount)*100 : 0.00;
                        detailsPayeeMapperDto.setPayeeAmount(BigDecimal.valueOf(share*0.01*netAmount));
                    }
                }
            }
			
			if(orderDetailsPayeeMapperDtosList.size()>1)
				detailsMaster.setCoPayPer(new BigDecimal(coShare));
			else
				detailsMaster.setCoPayPer(new BigDecimal(100));
			
			List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList = new LinkedList<OrderDetailsPackageMapperDto>();
			
			OrderDetailsPackageMapperDto detailsPackageMapper =  billingCommonMethods.createOrderDetailsPackageMapper(null,null,null,detailsMaster);
			
			/*OrderDetailsPackageMapperDto detailsPackageMapper = new OrderDetailsPackageMapperDto();
				detailsPackageMapper.setOrderDetailsId(detailsMaster.getOrderDetailsId());
				detailsPackageMapper.setIsPackageService('N');
				detailsPackageMapper.setIsCapService('N');
				detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
				detailsPackageMapper.setServiceId(detailsMaster.getServiceId());
				detailsPackageMapper.setServiceAmount(detailsMaster.getServiceAmt());
				detailsPackageMapper.setOrganizationId(detailsMaster.getOrgId());
				detailsPackageMapper.setUnitId(detailsMaster.getOrgUnitId());
				detailsPackageMapper.setStatus('A');*/
			orderDetailsPackageMapperDtosList.add(detailsPackageMapper); 
			
			
			iOrderDetailsMasterDao.saveOrderDetailsMaster(detailsMaster, orderDetailsPayeeMapperDtosList, orderDetailsPackageMapperDtosList);
		}
		return null;
	}

	@Override
	@Transactional
	public CommonResponse updateBill(WalkInSalesRequest request) {
		return WalkInSalesDao.updateBill(request);
	}

	@Override
	@Transactional
	public CommonResponse getStatus(Integer prescriptionId) throws Exception {
		try {
			CommonResponse dto =  WalkInSalesDao.getStatus(prescriptionId);
			
			Integer issueSaveStatus =  dto.getId().intValue();
			
			if(issueSaveStatus == com.param.common.constants.Status.Completed.getStatus() || issueSaveStatus == com.param.common.constants.Status.Closed.getStatus()) { 
				
				  return dto;
			  }else {
				  
				  if(issueSaveStatus == com.param.common.constants.Status.PartiallyCompleted.getStatus()) { 
						
					  Integer issueSaveStatus1 = WalkInSalesDao.getIssueDetialsStatus(prescriptionId);
					  
					  CommonResponse dto1= new CommonResponse();
					  dto1.setId(issueSaveStatus1.longValue());
					  
					  if(issueSaveStatus1 == com.param.common.constants.Status.Completed.getStatus() || issueSaveStatus1 == com.param.common.constants.Status.Closed.getStatus()) { 
						  
						  return dto1;
						  
						}else {
							
							return dto1;
						}
					  
				  }else {
					  
					  return dto;
				  }
				  
			  }
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public List<SaleDetail> getSaleDetailsBySaleId(Long ids) throws Exception {
		try {
			return WalkInSalesDao.getSaleDetailsBySaleId(ids);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public void updateInsertIntoStoreStock(List<SaleDetail> details, Long id, WalkInSalesRequest request, Boolean fromScreen)
			throws Exception {
		try {
			
			
			for (CommonBillingItemsDto item : request.getItems()) {

				Integer quantity = item.getQuantity();
				Long batchId = item.getBatchId();
				Integer storeId = request.getStoreId();
				UomType uomType = new UomType(item.getUomTypeId());
				Integer unitId = request.getUnitId();
				if (quantity > 0 && request.getUnitId()!=null && request.getOrganizationId()!=null) {
					Batch batch = GlobalDao.getBatch(batchId);
					if (batch != null) {
						Long saleDetailId = 0l;
						for (SaleDetail itemRequest : details) {
							if(itemRequest.getItemId().equals(item.getItemId()) && itemRequest.getQuantity().equals(item.getQuantity())) {
								saleDetailId = itemRequest.getId();
							}
						}
						Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
						Integer tQuantity = 0;
						Integer availableQuantity = 0;
						Integer currentQuantity = 0;
						StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore1(batchId, storeId, unitId);

								availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
								if (availableQuantity < 0) {
									availableQuantity = 0;
								}
								storeStockList.setAvailableQty(availableQuantity);

								GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
									currentQuantity = batch.getCurrentQuantity() - leaseQuantity;
									batch.setCurrentQuantity(currentQuantity);
									GlobalDao.updateBatchLeaseQuantity(batch);

								saveStockTransaction(leaseQuantity, storeStockList, request,id,saleDetailId,item.getItemId(),fromScreen);

					}

				}
			}

			
		} catch (Exception e) {
			
			throw e;
		}
		
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStockDetail, WalkInSalesRequest request,
			Long id, Long saleDetailId, Integer itemId,Boolean fromScreen) {
		
		com.param.entity.model.master.Screen screen = new Screen();
		if(fromScreen == true) {
			screen = GlobalDao.getScreen(15);
		}else {
			screen = GlobalDao.getScreen(1);
		}
		StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStockDetail.getBatch().getRate(), new Store(request.getStoreId()!=null ? request.getStoreId() : sessionService.getStoreId()),
				new Batch(storeStockDetail.getBatch().getId()), 0);
		stockTransaction.setIsTransit(false);
		stockTransaction.setTransactionId(id.intValue());
		stockTransaction.setTransactionDetialsId(saleDetailId.intValue());
		stockTransaction.setOutQty(leaseQuantity);
		stockTransaction.setItem(new Item(itemId));
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(
				request.getOrganizationId() != null ? request.getOrganizationId()
						: sessionService.getOrganizationId(),
						request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
								request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
		
	}

	@Override
	@Transactional
	public CommonResponse getSaleStatus(Long id) {
		CommonResponse dto =  WalkInSalesDao.getStatus(id);
		return dto;
	}

	@Override
	@Transactional
	public Response updatediscountCategory(OrderDetailsMasterDto detailsMasterDto) {
		try {
			return WalkInSalesDao.updatediscountCategory(detailsMasterDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

}
