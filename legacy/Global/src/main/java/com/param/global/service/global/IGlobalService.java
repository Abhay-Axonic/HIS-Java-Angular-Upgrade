package com.param.global.service.global;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.common.Response;
import com.param.global.dto.StoreStockRequestDto;
import com.param.global.dto.global.AgainstGrnRequest;
import com.param.global.dto.global.AgainstPurchaseOrder;
import com.param.global.dto.global.AgainstPurchaseRequest;
import com.param.global.dto.global.AgainstQuotationRequest;
import com.param.global.dto.global.AgainstWORequest;
import com.param.global.dto.global.AssetCategory;
import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.CancelReason;
import com.param.global.dto.global.Currency;
import com.param.global.dto.global.DateRequest;
import com.param.global.dto.global.DirectPurchaseRequestResponse;
import com.param.global.dto.global.Doctor;
import com.param.global.dto.global.FixedAssetType;
import com.param.global.dto.global.Floor;
import com.param.global.dto.global.FormulationType;
import com.param.global.dto.global.FormulationTypeAndROAList;
import com.param.global.dto.global.ItemDropdownResponse;
import com.param.global.dto.global.Manufacturer;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.Patient;
import com.param.global.dto.global.PatientDetailRequest;
import com.param.global.dto.global.PatientDetailResponse;
import com.param.global.dto.global.PatientIssueDropdown;
import com.param.global.dto.global.ProductCategory;
import com.param.global.dto.global.PurchaseOrderHistory;
import com.param.global.dto.global.RejectReason;
import com.param.global.dto.global.SaleType;
import com.param.global.dto.global.ScrapReason;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;
import com.param.global.dto.global.StoreIndentResponse;
import com.param.global.dto.global.StoreType;
import com.param.global.dto.global.StorewiseGenericMapperDropdown;
import com.param.global.dto.global.TariffRequest;
import com.param.global.dto.global.TariffResponse;
import com.param.global.dto.global.TaxComponentDropdown;
import com.param.global.dto.global.UomUnit;
import com.param.global.dto.global.User;
import com.param.global.dto.global.Ward;
import com.param.global.service.items.CustomException;

import in.param.exception.ApplicationException;

public interface IGlobalService {

	List<AssetType> getAssetTypeList(Integer unitId, Integer orgId);

	List<AssetCategory> getAssetCategoryList(Integer assetTypeId, Integer unitId, Integer orgId);

	List<ProductCategory> getProductCategoryList(Integer assetCategoryId, Integer orgId);

	List<FormulationType> getFormulationTypeList(Integer orgId);

	List<Manufacturer> getManufacturersList(Integer orgId);

	List<UomUnit> getUomUnitsList(Integer orgId);

	Currency getCurrency(Integer unitId);

	List<Status> getStatusList();

	List<VisitType> getVisitTypeList();

	PatientIssueDropdown getPatientIssueDropdown(Integer orgId, Integer unitId, Integer userId);

	List<SaleType> getSaleTypeList();

	List<RejectReason> getRejectReasonList(Integer unitId);

	List<CancelReason> getCancelReasonList();

	List<Patient> patientSearch(String name, Integer visitType, Integer unitId);

	List<Patient> patientSearchByUhid(String uhid, Integer orgId);

	List<Doctor> doctorSearch(Integer orgId, Integer unitId, String name);

	List<Ward> getWardList();

	List<Floor> getFloorList();

	List<Store> getUserStoreList(Integer orgId, Integer unitId, Integer userId);

	List<StoreType> getStoreTypeList(Integer orgId);

	List<StoreIndentResponse> getStoreIndentList(Integer fromStoreId, Integer toStoreId ,  DateRequest dateRequest);

	PatientDetailResponse patientIndentPatientSearch(PatientDetailRequest patientDetailsSearch);

	User userSearch(Integer id);

	AdmissionDetail getPatientWardBedDetails(Integer adminssionId);

	Object getManufacturerList(String name, Integer orgId);

	Object getVendorList(String name, Integer unitId);

	Object getServiceProviderList(String name, Integer orgId);

	Object getInsuranceCompanyList(String name, Integer unitId);

	Object getAgainstPurchseRequestSearch(AgainstPurchaseRequest purchaseRequestResponse);
	
	Object getAgainstQuotationRequestSearch(AgainstPurchaseRequest againstPurchaseRequest);
	
	Object getAgainstWOQuotationRequestSearch(AgainstWORequest againstWORequest);

	Object getItemsDetailsAgainstPurchseRequest(List<AgainstPurchaseRequest> againstPurchaseRequestList, Integer orgId, Integer unitId);
	
	Object getItemsDetailsAgainstQuotationRequest(List<AgainstQuotationRequest> againstQuotationRequestList, Integer orgId, Integer unitId);

	List<OtherChargesResponse> getOtherCharges( Integer unitId);

	DirectPurchaseRequestResponse getItemDetailsDirectPurchaseRequest(AgainstPurchaseRequest againstPurchaseRequest);

	Object getAgainstPurchseOrderSearch(AgainstPurchaseOrder againstPurchaseOrder);

	Object getItemDetailsAgainstPurchaseOrder(List<AgainstPurchaseOrder> againstPurchaseOrderList, Integer orgId, Integer unitId);

	List<FixedAssetType> getFixedAssetTypes();

	Object getRacks(Integer storeId, Integer orgId);

	Object getShelves(Integer rackId);

	Object getAgainstGrn(AgainstGrnRequest request);
	
	Object getBarcodeAgainstGrn(AgainstGrnRequest request); //For Barcode Printing get Approval qty

	List<ScrapReason> getScrapReasonList(Integer orgId);

	List<User> inWardBySearch(String name, Integer unitId, Integer orgId);

	ItemDropdownResponse getItemDropdowns(Integer unitId, Integer orgId);

	List<TaxComponentDropdown> getTaxComponentList();

	List<FormulationTypeAndROAList> getFormulationTypeAndROADropdown();

	StorewiseGenericMapperDropdown getStorewiseGenericDropdown(Integer unitId,Integer orgId);

	List<PurchaseOrderHistory> getPurchaseOrderHistoryByItemId(Integer itemId, Integer unitId , Integer poId);

    Integer getGrnQuantity(Integer poDetailId);

	PurchaseOrderDetail getPurchaseOrderDetail(Integer poDetailId);

	String manageStockConsumption(StoreStockRequestDto store);
	Integer  updatePharamcyDetailsPaidAmount(Integer saleId, Double amount, Integer status, Integer entitlementId, Double amt)throws BillingException;

	PatientDetailResponse patientIndentPatientSearchIndent(PatientDetailRequest patientDetailsSearch, Integer unitId, Integer orgId) throws Exception;
	
	DirectPurchaseRequestResponse getItemDetailsDirectPurchaseRequestWithPendingQty(AgainstPurchaseRequest againstPurchaseRequest);

	List<PMSFrequency> getFrequencyList(Integer orgId);

	Object getAllVendorList(Integer unitId,String vendorName);

	void cancelPrescription(Integer prescriptionId) throws CustomException;

    List<Patient> billListPatientSearch(String name, Integer unitId);

	List<Patient> patientSearchIssueReturn(Integer unitId, String name, Integer visitType);

	List<Patient> patientSearchPatientIndent(Integer unitId, String name, Integer visitType);

	List<Patient> salesReturnPatientSearchByUhid(String uhid, Integer visitType, Integer orgId);

	List<TariffResponse> getTariffData(TariffRequest tariffReq);
	
	PharmacyBillCommonDto updateSale(PharmacyBillCommonDto dto);

	Response getPatientNameSearchByName(PatientDetailRequest patientDetailsSearch);

	String getBillNoByGeneratedCode(int storeId, Integer unitId, Integer screenId);
	
	
	//Ganesh
	public List<Batch> getBatchDetailsGlobal(Integer id, Integer unitId, BatchProperty batchProperty, int storeId);
	//public List<Batch> getBatchListGlobal(Integer storeId, Long batchId,Integer itemId);

	Object getItemDetailsAgainstQuotationRequestByVendorId(
			List<AgainstQuotationRequest> againstQuotationRequestList, Integer vendorId, Integer orgId, Integer unitId);

	Object getAgainstrjectedGrn(AgainstGrnRequest request);

	PatientDetailResponse  patientIndentPatientSearchIndentForNursing(PatientDetailRequest patientDetailsSearch) throws Exception;

	List<User> inWardBySearchUser(Integer organizationId, Integer unitId, String name) throws ApplicationException;

	Object getAgainstGrnReturn(AgainstGrnRequest request);

	List<Batch> getBatchDetailsGlobalWalkInPrescription(Integer id, Integer unitId, BatchProperty batchProperty, Integer storeId);
}