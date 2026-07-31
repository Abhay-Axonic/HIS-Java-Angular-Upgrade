package com.param.global.dao.global;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.billing.global.transaction.config.model.RegistrationFieldConfiguration;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.FloorMasters;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.asset.AssetIndent;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.WorkingCondition;
import com.param.entity.model.inventory.returnableBonusBatchExpiry;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Company;
import com.param.entity.model.master.Currency;
import com.param.entity.model.master.DiscountCategory;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.FixedAssetType;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.KitType;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.Order;
import com.param.entity.model.master.OtherCharge;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.entity.model.master.PrefixConfiguration;
import com.param.entity.model.master.PrefixRunningNumber;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ReturnReason;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.ScrapReason;
import com.param.entity.model.master.Screen;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemMapper;
import com.param.entity.model.master.StoreType;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.entity.model.master.Unit;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Ved;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.AssignPickerDetailAddedItems;
import com.param.entity.model.pharmacy.BatchAllocation;
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
import com.param.entity.model.pharmacy.SaleType;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseOrderOtherChargesMapper;
import com.param.entity.model.procurement.PurchaseOrderStaged;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.billing.dto.SaleDetailsDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.GRNQuantity;
import com.param.global.dto.GoodReceiptNoteDetailDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.global.AgainstGrnDetailResponse;
import com.param.global.dto.global.AgainstGrnRequest;
import com.param.global.dto.global.AgainstGrnResponse;
import com.param.global.dto.global.AgainstPOSearchResponse;
import com.param.global.dto.global.AgainstPurchaseOrder;
import com.param.global.dto.global.AgainstPurchaseRequest;
import com.param.global.dto.global.AgainstWORequest;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.DateRequest;
import com.param.global.dto.global.DoctorSpecialityResp;
import com.param.global.dto.global.GRNAgainstApprovedPOQuantity;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.POOtherCharges;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.global.PatientDetailRequest;
import com.param.global.dto.global.PatientSearchDetails;
import com.param.global.dto.global.PoAccessoriesMapperGlobal;
import com.param.global.dto.global.Prefix;
import com.param.global.dto.global.PurchaseOrderDetailResponse;
import com.param.global.dto.global.PurchaseOrderStagedDcDate;
import com.param.global.dto.global.PurchaseRequestDetailResponse;
import com.param.global.dto.global.QuotationRequestDetailResponse;
import com.param.global.dto.global.Rack;
import com.param.global.dto.global.SalesType;
import com.param.global.dto.global.Shelf;
import com.param.global.dto.global.TariffResponse;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.BatchResponse1;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsDetailsResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.org.common.dao.PrefixConfigDto;
import com.param.global.pharmacy.dto.*;
import com.param.service.global.model.SaleDetailPackageMapper; 

public interface IGlobalDao {

	Integer getBatchQuantity(Long batchId,Integer unitId);

	Integer getStoreStockByBatch(Integer storeId, Long batchId,Integer itemId);
	
	Integer getStoreStockByBatch(Integer storeId, Long batchId);

	Long getStoreStockByBatchList(Integer storeId, List<Long> batchIds);

	Integer getTransitQuantityByBatch(Integer storeId, Long batchId);

	/*
	 * new implementation Integer getStoreStockByBatch(Integer storeId, Long
	 * batchId, Boolean isIndent);
	 */

	Integer getStoreStockByBatchPharmacy(Integer storeId, Long batchId);

	Batch getBatch(Integer storeId, Long batchId, Integer unitId);
	
	List<Batch> getBatchList(Integer storeId, Long batchId,Integer itemId,Integer unitId);

	Batch getBatchPharmacy(Integer storeId, Long batchId,Integer unitId);

	Admission getAdmission(Integer admissionId);

	DiscountCategory getDiscountCategory(Integer discountCategoryId,Integer unitId);

	DiscountType getDiscountType(Integer discountTypeId,Integer unitId);

	Order getOrder(Integer orderId);

	PatientRegistration getPatient(Integer patientId);

	Admission admissionPatient(PatientDetailRequest patientDetailsSearch, Integer unitId);

	User userSearch(Integer id);

	List<Encounter> encounterPatient(PatientDetailRequest patientDetailsSearch, Integer unitId);

	Status getStatus(Integer statusId);

	Store getStore(Integer storeId);

	VisitType getVisitType(Integer visitTypeId);

	Screen getScreen(Integer screenId);

	SaleDetail getSaleDetailById(Long id);

	List<ScreenStatus> getScreenStatus(Integer screenId);

	List<SaleType> getSalesTypeList();

	List<Prefix> getPrefixList(Integer orgId);

	List<com.param.global.dto.global.Gender> getGenderList();

	List<DiscountCategory> getDiscountCategoryList(Integer orgId, Integer unitId);

	List<Store> getStoreList(Integer unitId);

	List<Store> getLinenStoreList(Integer unitId);

	List<Store> getUserStoreList(Integer unitId, Integer orgId,Integer storeId);

	List<Store> getUserActiveStoreList(Integer storeId, Integer unitId, Integer orgId);

	List<Store> getLinenUserStoreList(Integer unitId, Integer userId);

	List<StoreType> getStoreTypeList(Integer orgId);

	List<IndentType> getIndentTypeList(Integer orgId,Integer unitId);

	List<Priority> getPriorityList();

	List<VisitType> getVisitTypeList();

	/*
	 * List<Batch> getBatchDetails(Integer id);
	 * 
	 * List<Batch> getBatchDetails(Integer id, Integer storeId);
	 */

	/* New Implementation */
	List<Batch> getBatchNotApplicableDetails(Integer id,Integer unitId,Integer storeId, BatchProperty batchProperty);

	List<Batch> getBatchDetails(Integer id, Integer unitId, Integer storeId, BatchProperty batchProperty);
	/* End */

	/*List<Batch> getBatchDetailsPharmacy(Integer id,Integer unitId);*/

	List<Batch> getBatchDetailsPharmacyConsignment(Integer id, Integer storeId, Integer unitId);

	List<Batch> getBatchListPharmacy(Integer itemId, Long batchId, Integer storeId,Integer unitId);

	List<Batch> getBatchDetailsPharmacy(Integer id, Integer storeId, Integer unitId);

	Long getStoreStockByItem(Integer storeId, Integer itemId, Integer unitId);

	Long getStoreStockByItem(Integer storeId, Integer itemId, Long batchId,Integer unitId);

	Long getstockInTransitByItem(Integer storeId, Integer itemId, Integer unitId);

	Long getStoreStockByItemPharmacy(Integer storeId, Integer itemId, Integer unitId);

	StoreStock getStoreStockByBatchAndStore(Long batchId, Integer storeId,Integer unitId);
	StoreStock getStoreStockByBatchAndStore1(Long batchId, Integer storeId,Integer unitId);
	StoreStock getStoreStockByBatchAndStoreItemId(Long batchId, Integer storeId,Integer unitId, Integer itemId);
	
	List<StoreStock> getStoreStockByBatchAndStoreByItem(Long batchId, Integer storeId,Integer itemId,Integer unitId);

	StoreStock getStoreStockByBatchAndStorePharmacy(Long batchId, Integer storeId,Integer unitId);

	Long getLastMonthSale(Integer storeId, Integer itemId, Integer unitId);

	Long getLastMonthSale(Integer itemId);// for Kit Item Master

	Long getCurrentMonthSale(Integer storeId, Integer itemId, Integer unitId);

	Long getCurrentMonthSale(Integer itemId);// for Kit Item Master

	Integer getStoreMaxQuantityByItem(Integer storeId, Integer itemId, Integer orgId);

	Long getStoreStockInTransitByItem(Integer storeId, Integer itemId);

	Long getStoreStockInTransitByItem(Integer itemId);// for Kit item Master

	List<AssetType> getAssetTypeList(Integer unitId, Integer orgId);

	List<KitType> getKitTypeList();

	List<AssetCategory> getAssetCategoryList(Integer assetTypeId, Integer unitId, Integer orgId);

	List<ProductCategory> getProductCategoryList(Integer assetCategoryId, Integer orgId);

	List<UomResponse> getUnitOfMeasurementListByItem(Integer itemId, Integer orgId);

	Integer getPurchaseConversionByItem(Integer itemId, Integer orgId);

	Integer getStockConversionByItem(Integer itemId, Integer orgId);

	Integer getDispenseConversionByItem(Integer itemId, Integer orgId);

	List<FormulationType> getFormulationTypeList(Integer orgId);

	List<Manufacturer> getManufacturersList(Integer orgId);

	List<UomUnit> getUomUnitsList(Integer orgId);

	List<Status> getStatusList();

	List<PMSFrequency> getPMSFrequency(Integer orgId);

	List<WorkingCondition> getWorkingConditions();

	List<SaleType> getSaleTypeList();

	Currency getCurrency(Integer unitId);

	List<RejectReason> getRejectReasonList(Integer unitId);

	List<CancelReason> getCancelReasonList();

	List<StoreIndent> getStoreIndentList(Integer fromStoreId, Integer toStoreId, Integer unitId);

	List<StoreIndentDetail> getStoreIndentDetailsList(Integer storeIndentId);

	List<PharmacologicalClassification> getPharmacologicalClassificationList(Integer unitId);

	List<Manufacturer> getManufacturerList(Integer orgId);

	List<StrengthUnit> getStrengthUnitList(Integer orgId);

	List<StorageUnit> getStorageUnitList(Integer orgId);

	List<Generic> getGenericList(Integer productCategoryId,Integer orgId, String searchGenericName);

	List<Tax> getTaxList(Integer orgId);

	List<TaxDropDown> getTaxListForWorkOrder(Integer orgId);

	List<PatientRegistration> patientSearchByUhid(String uhid, Integer orgId);

	List<PatientSearchDetails> salesReturnPatientSearchByUhid(String uhid, Integer orgId);

	List<DoctorSpecialityMapperDto> doctorSearchByName(Integer orgId, Integer unitId, String name);

	List<WardMasters> getWardList(Integer unitId);

	//List<DoctorSpecialityMapper> doctorSearchByIdValue(Integer id);

	AdmissionDetail getPatientWardBedDetails(Integer adminssionId);

	List<StoreIndentDetail> getStoreIndentDetails(List<Integer> storeIndentDetailIds);

	List<UomResponse> getBatchUomList(Long batchId);

	List<UomResponse> getBatchUomListPharmacy(Long batchId);

	ItemsDetailsResponse itemsDetail(int id);

	List<Manufacturer> getManufacturerList(String name, Integer orgId);

	List<Vendor> getVendorList(String name, Integer unitId);

	Integer getVendorIdByBatchNo(String batchNo);

	String getVendorNameByVendorId(Integer vendorId);

	List<Vendor> getServiceProviderList(String name, Integer orgId);

	List<Company> getInsuranceCompanyList(String name, Integer unitId, Integer organizationId);

	User getUserById(Integer id);

	List<UserMasterDto> getUserByName(String name, Integer unitId, Integer orgId);

	List<User> getUser(Integer orgId,Integer unitId);

	Item getItemById(Integer id);

	List<PurchaseRequest> searchLocalPuchaseRequest(AgainstPurchaseRequest purchaseRequestResponse,
			List<Integer> statusIds);

	List<QuotationRequest> searchLocalQuotationRequest(AgainstPurchaseRequest purchaseRequestResponse,
			List<Integer> statusIds);

	List<WorkOrderCreation> searchLocalWOQuotationRequest(AgainstWORequest againstWORequest, List<Integer> statusIds,Integer unitId, Integer orgId);

	List<PurchaseRequestDetailResponse> getItemsDetailsAgainstPr(Long Id, Integer itemId);

	List<QuotationRequestDetailResponse> getItemsDetailsAgainstQr(Integer Id, Integer itemId);

	List<OtherCharge> getOtherCharges();

	List<PaymentMode> getPaymentModeList();

	Batch getBatch(Long id);

	// minesh
	Batch getConvertedBatch(Long id);

	Batch getExistedConvertedBatch(Long id);

	List<Batch> getBatchListForConversion(String lotNo, Integer storeId, Integer unitId);

	List<Batch> getBatchListForConversion(int grnId,Integer unitId);

	List<Batch> getSameBatchListFromStoreSQL(String batchNum, Integer storeId,Integer unitId);// sort by current quantity for material issue.

	List<Batch> getSameBatchList(Integer itemId, String batchNum, String lotNum,Integer storeId,Integer unitId);// sort by current quantity.

	List<Batch> getSameBatchListHQL(Integer itemId, String batchNum, String lotNum,Integer unitId, Integer storeId);// sort by current quantity.

	List<StoreStock> getStoreStockListByBatchAndStore(List<Long> batchIds, Integer storeId,Integer unitId);// store list from storeid and batch list for material issue.

	List<StoreStock> getStoreStockListByBatchIdAndStoreId(Long batchId, Integer storeId,Integer unitId);// store list from storeid and  batch list for material issue.
	List<StoreStock> getStoreStockListByBatchIdAndStoreId(Long batchId, Integer storeId, String lotNo,Integer unitId);// store list from storeid and  batch list for material issue.
	
	List<Long> getConsignmentBatchIds(Long batchId, Integer storeId, Integer unitId);// get consignmet and its converted batch id for reallocation in walkinsale.
	List<Long> getConsignmentBatchIds(Integer itemId,String batchNo, String lotNo,Integer storeId,Integer unitId);// get consignmet and its converted batch id for reallocation in walkinsale.

	BigInteger consignmentBatchCount (Integer itemId,String batchNo, String lotNo,String expiryDate,Integer unitId);// consignment batch count
	Long consignmentBatchId(Integer itemId,String batchNo, String lotNo);// consignment batch id
	
	List<Long> getConsignmentBatchListIdWithExpDate(Long batchId, Integer storeId);// consignment batchId With Batch Id and exp Date same
	List<StoreStock> getStoreStockListByBatchIdAndStoreIdWithExpireDate(Long batchId, Integer storeId,Integer unitId);// store list from storeid and  batch list for WalkInSale.
	
	GoodReceiptNote getGoodReceiptNote(String batchNum, String lotNum);// sort by current quantity.

	Batch getBatchByBatchUom(Long id);

	Batch getBatchByBatchLotNo(String lotNo);

	Batch getBatchPharmacy(Long id);

	Batch getUomByBatchUomMapper(Long id);

	List<PurchaseOrder> searchPurchaseOrder(AgainstPurchaseOrder againstPurchaseOrder, List<Integer> statusIds);

	List<PurchaseOrderDetailResponse> getItemsDetailsAgainstPO(Integer Id, Integer itemId);

	PurchaseOrder getPurchaseOrderList(Integer id);

	List<PurchaseOrderStaged> getPurchaseOrderStaged(Integer id);

	StoreStock saveStoreStockLeaseQuantity(StoreStock storeStock);

	StoreStock updateStoreStockLeaseQuantity(StoreStock storeStock);

	Batch updateBatchLeaseQuantity(Batch batch);

	StockTransaction saveStockTransaction(StockTransaction stockTransaction);

	PurchaseRequest getPurchaseRequestList(Long id);

	QuotationRequest getQuotationRequestList(Integer id);

	List<IssueDetailBatchMapper> getIssueDetailBatchMapper(Integer id);

	IssueDetailBatchMapper getIssueDetailBatchMapperById(Integer id);

	IssueDetailBatchMapper updateIssueDetailBatchMapper(IssueDetailBatchMapper issueDetailBatchMapper);

	List<IssueDetail> getIssueDeatilByIssueId(Integer issueId);

	Batch saveBatch(Batch batch);

	List<FixedAssetType> getFixedAssetTypes();

	List<ReturnReason> getReturnReason(Integer unitId);

	List<Rack> getRacks(Integer storeId, Integer orgId);

	List<Shelf> getShelves(Integer rackId);

	List<AgainstGrnResponse> getGrn(AgainstGrnRequest request);

	// List<AgainstGrnResponse> getBarcodeGrn(AgainstGrnRequest request);//for
	// barcode show approval Qty

	List<AgainstGrnDetailResponse> getBarcodeGrnDetails(List<Integer> grnId, Integer assetTypeId);// for barcode show
																									// approval Qty

	List<AgainstGrnDetailResponse> getGrnDetails(List<Integer> grnId, Integer assetTypeId);

	List<ReturnableGatepassDetail> getReturnableGatepassDetails(Integer grnDetailId);

	List<ScrapReason> getScrapReasonList();

	List<ReasonMaster> getReasonList();

	List<ContractType> getContractType();

	List<PurchaseRequestDetailResponse> getFixedItemsDetailsAgainstPr(Long Id, Integer itemId);

	List<QuotationRequestDetailResponse> getFixedItemsDetailsAgainstQr(Integer Id, Integer itemId);

	List<com.param.global.dto.global.PurchaseOrderStaged> getPurchaseOrderStagedList(Integer id);

	List<PurchaseOrderOtherChargesMapper> getPurchaseOrderOtherChargesMapper(Integer id);

	List<TaxComponent> getTaxComponentList();

	List<RouteOfAdministration> getFormulationTypeAndROADropdown();

	List<AssetCategory> getAssetCategory(Integer orgId);

	List<Ved> getVedList();

	Long getPurchaseRequestLeaseQunatity(Integer storeId, Integer itemId, Integer unitId);

	List<Screen> getScreens();

	PrefixConfiguration getPrefixConfiguration(Integer screenId);

	Unit getUnit(Integer unitId);

	Long getQuotationRequestLeaseQunatity(Integer storeId, Integer itemId, Integer unitId);

	List<PurchaseOrderDetailResponse> getFixedItemsDetailsAgainstPO(Integer Id, Integer itemId);

	List<PurchaseOrderDetail> getPurchaseOrderHistory(Integer itemId, Integer unitId);

	String getPrefixRunningNumber(Integer unitId, int screenId, Integer storeId);

	PrefixRunningNumber updatePrefixRunningNumbar(PrefixRunningNumber prefixRunningNumber);

	List<TaxDetail> getTaxDetail(Integer taxId);

	int getAccessTypeId(int accessTypeId);

	ApplicationConfiguration getApplicationConfigurationByUnitId(Integer unitId);

	List<RequestType> getRequestType();

	Batch getBatchFromSaleDetail(Long id);

	List<BatchListResponse> smartIssue(List<Integer> itemId, Integer storeId);

	// This Method Create for COnsignmentoutward UomList Return purpose
	List<Batch> getBatchDetails(Integer id, Integer storeId,Integer unitId);

	List<Batch> getBatchDetailsForAsset(Integer itemId, Integer toStoreId, BatchProperty batchProperty, Integer unitId);

	Integer getStoreMaxQuantityByItemFromAssetAcc(Integer storeId, Integer id, Integer orgId);

	List<com.param.global.dto.global.AssetType> getAssetTypeListPerformance();

	List<com.param.global.dto.global.AssetType> getAssetTypeListPT();

	List<com.param.global.dto.generic.PharmacologicalClassification> getPharmacologicalClassificationListPT(Integer orgId,String searchClassificationName);

	List<com.param.global.dto.generic.Manufacturer> getManufacturerListPT(Integer orgId,String searchManufactureName);

	List<com.param.global.dto.generic.StrengthUnit> getStrengthUnitListPT(Integer orgId);

	List<com.param.global.dto.generic.StorageUnit> getStorageUnitListPT(Integer orgId);

	List<com.param.global.dto.generic.Tax> getTaxListPT();

	List<com.param.global.dto.global.FormulationType> getFormulationTypeListPT(Integer orgId);

	List<com.param.global.dto.global.Status> getScreenStatusBetterPerformance(Integer screenId);

	List<com.param.global.dto.global.VisitType> getVisitTypeListBetterPerformance();

	List<UomResponse> getConsignedInwordBatchUomList(Long batchId);

	PurchaseOrderDetail getPurchaseOrderDetailsById(Integer id);

	List<PurchaseOrderStagedDcDate> getPurchaseOrderStagedListDcDate(Integer id);

	List<PurchaseOrderDetail> getPurchaseOrdersByRequestDetailId(Integer id);

	/*
	 * List<PurchaseOrderDetail> getPurchaseOrdersByQuotationRequestDetailId(Integer
	 * id);
	 */
	Batch getBatchForStockConsumption(Integer storeeId, Long batchId, Integer unitId);

	List<Batch> getBatchDetailsCSSD(Integer itemId, Integer unitId, Integer storeId, BatchProperty batchProperty);

	List<UomResponse> getUnitOfMeasurementListByItemBP(List<Integer> consumabelItemIds, Integer orgId);

	List<OtherChargesResponse> getOtherChargesBP(Integer unitId);

	List<TaxDropDown> getTaxListBP(Integer orgId);

	List<UomResponse> getUnitOfMeasurementListByItemBettoerPerformance(List<Integer> itemIds, Integer orgId);

	List<Quantities> getStoreStockByItemBP(Integer storeId, List<Integer> itemIds, Integer unitId);

	List<Quantities> getStoreStockInTransitByItemBP(Integer storeId, List<Integer> itemIds, Integer unitId);

	List<Quantities> getLastMonthSaleBP(Integer storeId, List<Integer> itemIds, Integer unitId);

	List<Quantities> getStoreMaxQuantityByItemBP(Integer storeId, List<Integer> itemIds, Integer orgId);

	List<Quantities> getCurrentMonthSaleBP(Integer storeId, List<Integer> itemIds, Integer unitId);

	List<com.param.global.dto.global.Store> getUserStoreListBP(Integer userId, Integer unitId);

	List<com.param.global.dto.global.Store> getStoreListBP(Integer unitId);

	List<com.param.global.dto.global.Status> getScreenStatusBP(Integer screenId);

	List<com.param.global.dto.global.AssetType> getAssetTypeListBP();

	List<AgainstPOSearchResponse> searchPurchaseOrderBP(AgainstPurchaseOrder againstPurchaseOrder,
			List<Integer> statusIds);

	List<PurchaseOrderDetailResponse> getPoDetailsResponse(List<Integer> poIds);

	List<AgainstPOSearchResponse> getPurchaseOrderListBP(List<Integer> poIds);

	List<PurchaseOrderDetailResponse> getItemsDetailsAgainstPOBP(List<Integer> consumablePoIds,
			List<Integer> consumableItemIds);

	Collection<? extends PurchaseOrderDetailResponse> getFixedItemsDetailsAgainstPOBP(List<Integer> fixedPoIds,
			List<Integer> fixedleItemIds);

	List<com.param.global.dto.global.PurchaseOrderStaged> getPurchaseOrderStagedListBP(List<Integer> poDetailIds);

	List<PurchaseOrderStagedDcDate> getPurchaseOrderStagedListDcDateBP(List<Integer> poDetailIds);

	List<POOtherCharges> getAppliedOtherChargesForPO(List<Integer> poDetailIds);

	List<POOtherCharges> getPurchaseOrderOtherChargesMapperBP(Integer poDetailIds);

	List<GRNAgainstApprovedPOQuantity> getStoreStockByItemBP(List<Integer> storeIds, List<Integer> poDetailsItemIds, Integer unitId);

	List<GRNAgainstApprovedPOQuantity> getStoreStockInTransitByItemBP(List<Integer> storeIds,
			List<Integer> poDetailsItemIds);

	List<GRNAgainstApprovedPOQuantity> getStoreMaxQuantityByItemBP(List<Integer> storeIds,
			List<Integer> poDetailsItemIds);

	List<GRNAgainstApprovedPOQuantity> getPurchaseRequestLeaseQunatity(List<Integer> storeIds,
			List<Integer> poDetailsItemIds, Integer unitId);

	List<GRNQuantity> getGrnReceivedAndApprovedQtySum(List<Integer> poDetailIds);

	Long getConsignemntOutwordBatchWiseQty(Long batchId);

	List<SalesType> getSalesTypeListBP();

	public List<com.param.global.dto.global.DiscountCategory> getDiscountCategoryListBP(Integer orgId , Integer unitId);

	//List<DoctorSpecialityResp> doctorSearchByIdValueBP(List<Integer> doctorIds);

	List<BatchResponse> getBatchDetailsPharmacyBP(List<Integer> itemIds, Integer unitId,Integer storeId);

	List<UomResponse> getBatchUomListPharmacyBP(List<Long> collect);
	List<UomResponse> getBatchUomListPharmacyByItemId(Integer itemId);

	//List<DoctorSpecialityResp> doctorSearchByIdValueBP(Integer doctorId);

	List<TaxDetail> getTaxDetailBP(List<Integer> list);

	List<BatchResponse> getBatchForAssignPicker(List<Integer> assignPickerDetailIds);

	List<Quantities> getStoreStockByItemPharmacyBP(Integer storeId, List<Integer> itemIds, Integer unitId);

	List<BatchResponse> getBatchListPharmacyBP(List<Integer> itemIds, List<Long> batchIds, Integer storeId);

	PharamcyBillPayeeDetails savePharamcyBillPayedetails(PharamcyBillPayeeDetails pharamcyBillPayeeDetails);

	Integer updateDueAmountByBillBilPayeeId(Integer saleId, Integer payeeId, Double amount);

	Integer updateSaleMasterIsInvoiceCreationBySaleId(Integer saleId, Integer payeeId) throws BillingException;

	List<SaleDetailsDto> getBillingDetailsBySaleId(Integer saleId, Integer payeeId) throws BillingException;

	Integer updatePharamcyDetailsPaidAmount(Integer saleId, Double amount, Integer status, Integer entitlementId, Double amt) throws BillingException;

	Sale getSaleForBackOffice(Long id);

	Integer updatePharamcyBillStataus(Integer saleId, Integer status) throws BillingException;

	List<PharmacyTariff> getTariff(List<Integer> prodCatIds, int entitlementId);

	public ItemsDetailsResponse itemsDetailTariff(int id);

	TempPatientRegistration getPatientById(Integer id);

	Response getDetailsBySaleDetailsId(OrderDetailsMasterDto orderDto);

	Response getSaleDetailsPayeeMapperBySaleDtlsId(SaleDetailPayeeMapperDto dto) throws Exception;

	Response getCapDetails(ContractConsumptionDto dto) throws Exception;

	List<Rack> getRackList(List<Long> batchIds, Integer storeId);

	List<UomResponse> getBatchUomListBP(List<Long> batchIds);

	List<User> getPickerUsers(Integer unitId);

	SaleDtoChangeOfPayee getSale(Long id, Integer paymentEntitlementId, Integer patientClassId, Boolean b);

	Sale updateSale(Sale sale);

	List<PMSFrequency> getFrequencyList(List<PMSFrequency> frequency) throws Exception;

	Long getStoreStockByItemForAssignPicker(Integer storeId, Integer itemId);

	List<com.param.entity.model.master.Rack> getRacksForOpeningBalance(Integer storeId, Integer orgId);

	List<Vendor> getAllVendorList(Integer unitId,String vendorName);

	String doctorSpeciality(Integer organizationId, Integer unitId);

	List<ReasonMaster> getreasonDropdownList(Integer orgId);

	List<PatientSearchDetails> getOPPatients(String name, Integer unitId);

	List<PatientSearchDetails> getIPDCERPatients(String name, Integer visitType, Integer unitId);

	Prescription getPrescription(Integer prescriptionId);

	List<IssueDetail> getIssueDetailsByPickerDetail(Integer id);

	BigInteger getTotalIssuedQty(Integer patientId, Integer id);

	BigInteger getConsumedQty(Integer patientId, Integer itemId, Long batchId, Integer issueDetailId);

	Integer getReturnQty(Integer admissionId, Long id);

	List<PatientSearchDetails> getIPDCERPatientsIssueReturn(String name, Integer visitType, Integer unitId);

	List<PatientSearchDetails> getIPDCERPatientsPatientIndent(String name, Integer visitType, Integer unitId);

	List<BatchResponse> getBatchListByItemId(Long itemIds, Integer storeId, Integer unitId);

	List<Batch> getBatchListByItemIdForBillableConsumption(Long itemIds, Integer storeId);

	List<Batch> getConsignmentBatchDetails(String batchNo, long batchId, Integer toStoreId,
			BatchProperty batchProperty);

	Long getMaxOrderQuantity(Integer maxStoreQuantity, Long storeStock, Long stockInTransit,
			Long approvedPRAndPOQuantity, Long approvedPOQuantity);

	PrefixConfigDto getPrefixConfiguration(Integer storeId, Integer unitId, Integer screenId);

	BigInteger getConsignmentBatchWiseQty(String batchNo, String lotNo, Boolean isConsignment);
	//added by minesh as per disucss with Divyeshsir
	BigInteger getConsignmentBatchWiseStoreQty(String batchNo, String lotNo, Boolean isConsignment,int storeId);

	PrefixRunningNumber getPrefixLatestNumber(Integer storeId, Integer unitId, Integer screenId);

	String getDefaultPrefix(Integer storeId, Integer unitId, Integer screenId);

	PatientConsession getPatientConsessionAdmission(Integer itemId, Integer admissionId, Integer patientCategoryId,
			Integer assetCategoryId, Integer prodCatId);

	PatientConsession getPatientConsessionEncounter(Integer itemId, Integer encounterId, Integer patientCategoryId,
			Integer assetCategoryId, Integer prodCatId, Integer patientClassId, Integer entitlementId);

	Integer getPatientCategoryId(Integer encounterId);

	List<TariffResponse> getItemProdCategories(List<Integer> itemIds);

	Integer getPatientCategoryIdAdmission(Integer admissionId);

	BigInteger getClubBatchWiseAvailableStock(String batchNo, String lotNo, Boolean isConsignment, Integer itemId,
			Integer storeId , Integer unitId);

	Long save(PrefixRunningNumber prefixRunningNumber);
	
	List<SaleDetailPackageMapperDto> getSalePackageMpperListbyDetailsId(Integer saleDetailsId,Integer saleId);
	List<SaleDetailPayeeMapperDto> getSalePayeeMpperListbyDetailsId(Integer saleDetailsId,Integer saleId);
	
	//sales Return
	List<SaleDetailPackageMapperDto> getSalePackageMpperListbyDetailsIdReturn(Integer saleDetailsId,BigDecimal netAmount);
	List<SaleDetailPayeeMapperDto> getSalePayeeMpperListbyDetailsIdReturn(Integer saleDetailsId,BigDecimal netAmount,Integer returnQty);

	BigInteger getClubBatchWiseAvailableStockLotNo(String lotNo, Boolean isConsignment, Integer itemId,
			Integer storeId);
	
	Sale getSaleById(long saleId);
	SaleDetail getSaleDetailsById(long saleDetailsId);

	PrescriptionDetail savePrescriptionDetailForSubstitute(PrescriptionDetail prescriptionDetail);

	PrescriptionFrequencyDetail savePrescriptionFrequencyDetail(PrescriptionFrequencyDetail pfd);

	AssignPickerDetail saveAssignPickerDetailItem(AssignPickerDetail assignPickerDetail);

	AssignPickerDetailAddedItems saveAssignPickerDetailAddedItem(AssignPickerDetailAddedItems addedItems);

	IssueDetailsDto getIssueDtlsByPrescriptionId(Integer id);

	IssueDetail saveIssueDetailForSubstitute(IssueDetail issueDtlNew);

	IssueDetailsDto getIssueDetailsById(Integer id, Integer issueDetail);

	List<SubstituteItemDto> getListOfSubstitutes(Integer assignPickerId, Integer assignPickerDetailId);

	List<SubstituteItemDto> getListOfPendingIssuesSubstitutes(Integer id);

	List<PurchaseOrderDetail> getPurchaseOrdersWithCanelByRequestDetailId(Integer id);

	Long getCancelQuotationRequestLeaseQunatity(Integer storeId, Integer id, Integer unitId);

	List<SubstituteItemDto> getListOfSubstitutesWithoutAssignPicker(Integer id);

	List<SubstituteItemDto> getListOfNarcoticIssuesSubstitutes(Integer id);

	List<SubstituteItemDto> getListOfNarcoticIssuesWithoutPickerSubstitutes(Integer id);

	PrescriptionDetailsForOPSalesWorklistDto getPresDtlsByPresIdForWalkinSales(Integer prescriptionId, Integer itemId);

	List<SubstituteItemDto> getListOfSubstitutesForOpSales(Long prescriptionId);

	Response getPatientNameSearchByName(PatientDetailRequest patientDetailsSearch);

	Integer getVendorCop(Integer vendorId , Integer id);

	List<QuotationVendor> getQuotationVendorSearchList(AgainstPurchaseRequest againstPurchaseRequest,
			List<Integer> statusIds, Integer unitId);
	List<PoAccessoriesMapperGlobal> getPoAccessoriesMapperBP(List<Integer> Ids);

	Long getLastMonthSaleNew(Integer storeId, Integer itemId, Integer unitId);

	List<StoreIndent> getStoreIndentListWithDate(Integer fromStoreId, Integer toStoreId, DateRequest dateRequest);

	Admission admissionPatientForNursing(PatientDetailRequest patientDetailsSearch, Integer unitid);

	Doctor GetDoctorById(Integer doctorId);

	DoctorSpecialityResp getDoctorSpecialityResp(Integer doctorId,Integer unitid);

	/* start code for resolved HIS-10253 Store Stock Dashboard - Billable Consumption - */
	BatchResponse1 getUpdatedBatchDetails(int storeId, BigInteger bigInteger,Integer unitId);
	/* end code for resolved HIS-10253 Store Stock Dashboard - Billable Consumption - */

	List<QuotationRequestDetail> getQuotationRequestDetailList(Integer id, Integer vendorId);

	List<Integer> getQuotationRequestDetailIds(Integer id, Integer vendorId);

	List<QuotationRequestDetail> getQuotationRequestDetailListByIds(List<Integer> quotationRequestDetailIds);

	List<FloorMasters> getFloorList(Integer unitId);

	Prescription getPrescriptionData(Integer prescriptionId);

	PrescriptionDetail getListPrescriptionDetails(Integer prescriptionDetailId);
	
	List<PatientSearchDetails> billListPatientSearch(String name, Integer unitId);

	List<UserMasterDto> inWardBySearchUser(Integer organizationId, Integer unitId, String name);

	BigInteger getClubBatchWiseAvailableStockWithoutLot(String batchNo, Boolean isConsignment, Integer itemid,
			Integer storeid , Integer unitId);

	List<ReasonMaster> getCancelReasonDropdownList(int orgId);
	Response updatePharamcyBillPayees(List<PharamcyBillPayeeDetails> listToBeUpdated);

	StoreItemMapper getItemMinMaxData(int itemId, int unitId, int storeId);

	ReceiverResponse getReceiverDetails(Long id , Integer unitId);

	List<AssetIndent> getAssetIndentListWithDate(Integer fromStoreId, Integer toStoreId, DateRequest dateRequest);

	List<AssetIndentDetail> getAssetIndentDetailsList(Integer id);

	AssetIndentDetail getAssetIndentDetail(Integer id);
	List<ReasonMaster> getreasonDropdownListByTypeId(Integer orgId , Integer reasonTypeId);

	Response getBatchDetailsByPresDetailsId(Integer id);

	List<IssueDetail>  getIssueDetail(Integer id); 
	
	GoodReceiptNoteDetailDto getLastGrnuomandMrp(Integer organizationId, Integer unitId, Integer itemId);
	GoodReceiptNoteDetailDto getLastPouomandMrp(Integer organizationId, Integer unitId, Integer itemId);

	String getEntitlementDesc(Integer admissionId);

	PurchaseOrderDetailResponse getPOReqStore(Integer id);

	Batch getBatchForNa(String batchNo, Integer id, Integer storeId, Double cop, Double mrp, Integer unitId , Date expiryDate);
	Batch getBatchForCopAvg(String batchNo, Integer id, Integer storeId, Integer unitId , Date expiryDate);
	List<StoreStock> getStoreStockByBatchAndStoreList(Long batchId, Integer requestStoreId, Integer unitId);

	void updateStoreStockTransitQty(StoreStock storeStockList);

	Integer getBatchId(Integer id);

	Batch getBatchById(long longValue);

	List<AgainstGrnDetailResponse> getApprovedGrnDetails(List<Integer> list, Integer assetTypeID);

	Integer executeBatchForMappers(List<SaleDetailPayeeMapper> payeeList, List<SaleDetailPackageMapper> packageMprList);

	BatchAllocation getBatchAllocation(Integer batchAllocationId);
	List<Batch> getBatchDetailsForPatientIssueAcceptanceNew(Integer id, Integer storeId, Integer unitId);
	

	Sale updateSaleBatchUpdate(Sale sale);

	Boolean getVendorSelectionMapper(Integer id, Integer vendorId);

	Long getStoreIndentQuantityByItem(Integer storeId, Integer id, Integer unitId);


	List<PatientSearchDetails> getIPDCERPatientsAll(String name, Integer unitId);

	AdmissionDto admissionPatient1(Integer admissionId);

	Double patientclasspercentage(Integer itemId, Integer organizationId, Integer unitId, Integer entitlementId);

	String getUserNameByUserId(Integer addedByUser, Integer unitId);

	BillableConsumptionSave getBillableConsumId(Integer integer);

	Integer getTotalQuantity(Integer itemId, Integer issueId, Integer storeId, Integer unitId, Long batchId);

	List<Batch> getBatchDetailsPharmacyConsignment1(Integer id, Integer storeId, Integer unitId);

	List<ReturnableGatepassDetail> getAvailQty(Integer grnDetailId);

	Double getNetAmount(List<Integer> poIds);

	Double getRountOffAmount(List<Integer> poIds);

	List<SaleDetailPayeeMapperDto> getSalePayeeMpperListbyDetailsIdReturn(Integer saleDetailsId);

	Double getActualRateFromOpeningBalance(Long batchid, Integer unitId);

	boolean getSubItemAtCOPFlag(BigInteger prescriptionDetailid, Integer unitId);

	String getEntitlementContractName(Integer admissionId);

	Integer getBonusBatchId(Integer id);

	List<GrnBonusBatchExpiry> getGrnBonusBatchExpiryByGrnDetailId(Integer grnDetailId);

	Integer getSumOfReturnedQty(Long batchId);

	Integer getBonusTypeId(Integer id);

	Integer getBonusApprovedQty(Integer id);

	List<returnableBonusBatchExpiry> getReturnableBonusBatchExpiryByGrnDetailId(Integer id);

	Integer getIsFinalDischarge(Integer admissionId);

	List<Batch> getKitItemsAndBatchOnWalkInSales(List<Integer> itemIds, Integer unitId, BatchProperty batchProperty,
			Integer storeId, Boolean fromWalkInPrescription);

	ItemsDetailsResponse itemsDetailTariffForKit(Integer id, Integer storeId);

	List<UomResponse> getKitItemsUomList(List<Integer> itemIds, Integer unitId);

	Integer getIsPendingStatus(Integer grnDetailId);

	String pharmacyPRNFormat(Integer unitId);

	boolean isPRNRequired(Integer unitId);

	List<UomResponse> getReturnableStatus(List<Long> batchIds);

	List<Batch> getBatchDetailsPharmacyForBarcodeScan(Integer batchId, Integer storeId, Integer unitId);

	
    Integer getMaterialAcceptedQty(Integer issueDetailId);

    Integer getMaterialReturnedQty(Integer issueDetailId);
    
	List<AgainstGrnDetailResponse> getBonusBatchDetails(int id);

	String getColorCode(Integer genericId,Integer orgId,Integer unitId);

	List<ItemsDetailsListResponse> getGenericColorMapping(int unitId, int orgId);

	List<Long> getPrescriptionDetails(List<Long> ids);

	List<PatientSponsorDetailsDto> getAdmiassionDetails(List<Integer> admissionIds);

	List<UomResponse> getBatchUomListAll(List<Long> batchIds);

	Integer getReturnableStatus(Long batchId);
	

	boolean isManualBarcodeRequired(Integer unitId);

	List<UomResponse> getUnitOfMeasurementList(Integer orgId);

	List<UomResponse> getStoreStockAll(Integer storeId, Integer unitId);

	List<UomResponse> getstockInTransitAll(Integer storeId, Integer unitId);

	List<UomResponse> getLastMonthSaleNewAll(Integer storeId, Integer unitId);

	List<UomResponse> getCurrentMonthSaleAll(Integer storeId, Integer unitId);

	List<UomResponse> getStoreMaxQuantityAll(Integer storeId, Integer orgId);

	List<UomResponse> getAllUomListAll(Integer organizationId);

	List<Batch> getBatchAll(Integer storeId, Integer unitId);

	List<Batch> getBatchDetailsAll(Integer unitId, Integer toStoreId,List<Integer> itemIds);
	List<Batch> getBatchDetailsPharmacyConsignmentAll(Integer storeId, Integer unitId, List<Integer> subDetailsIds);

	List<UomResponse> getAllUomListAllByUnit(Integer unitId);

	List<UomResponse> getStoreStockByItemPharmacyAll(List<Integer> subDetailsIds,Integer storeId, Integer unitId);

	List<Batch> getBatchAll(List<Long> batchIds);

	List<StoreStock> getStoreStockByBatchAndStoreAll(List<Long> batchIds, Integer storeId, Integer unitId);

	List<PrescriptionDetailsForOPSalesWorklistDto> getPresDtlsByPresIdForWalkinSales(Integer prescriptionId,
			List<Integer> parentItemIds);

	List<Batch> getBatchDetailsPharmacyConsignmentAll(Integer itemId, Integer storeId, Integer unitId);

	List<Batch> getBatchDetailsWalkInPrescription(Integer id, Integer unitId, Integer storeId, BatchProperty batchProperty);

	Integer getIsFinalBill(Integer EncounterId, Integer addmissionId);

	List<UomResponse> getUnitOfMeasurementListByItemIds(List<Integer> itemsIds, Integer organizationId);

	List<Batch> getBatchDetailsOnReturnAcceptance(Integer id, Integer storeId, Integer unitId);

	String getcarrier(Integer id);

	RegistrationFieldConfiguration getRegistrationFieldConfigurationByUnitId(Integer unitId);

    List<CommonBillingItemsDto> getGrnInfoByBatchId(List<Long> batchIds);

//    Integer updateBatchCurrentQuantityWithStoreStockQuantity(Batch batch);

    Response updateBatchQuantityForBillable(List<BatchListResponse> batchList);
}
