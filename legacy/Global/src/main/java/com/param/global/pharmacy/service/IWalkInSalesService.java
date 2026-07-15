package com.param.global.pharmacy.service;

import java.util.Date;
import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.common.Response;
import com.param.global.dto.AllDataResponse;
import com.param.global.dto.AvailableQty;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.UpdatePayeeDto;
import com.param.global.dto.items.ItemsDetailsResponse;
import com.param.global.pharmacy.dto.ItemRequest;
import com.param.global.pharmacy.dto.ParamacyContractReqDto;
import com.param.global.pharmacy.dto.PatientConsumableIssueSave;
import com.param.global.pharmacy.dto.PatientConsumtionIssueSave;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.pharmacy.dto.SaleDtoChangeOfPayee;
import com.param.global.pharmacy.dto.WalkInPrescriptionReq;
import com.param.global.pharmacy.dto.WalkInSalesDropdownResponse;
import com.param.global.pharmacy.dto.WalkInSalesRequest;
@SuppressWarnings("rawtypes")
public interface IWalkInSalesService {

	CommonResponse allocateItems(WalkInSalesRequest request) throws Exception;

	CommonResponse saveBill(WalkInSalesRequest request) throws Exception;

	CommonResponse saveBillComplete(WalkInSalesRequest request) throws Exception;

	CommonResponse deleteAllocation(Integer screenId, Long tempBillId, List<Integer> allocationIds);
	CommonResponse deleteAllocationNew(Integer screenId, Long tempBillId, List<Long> allocationIds, Integer unitId, Integer storeId, Integer userId);
	WalkInSalesDropdownResponse getDropdowns(Integer orgId, Integer unitId);

	ItemsDetailsResponse allocateBatch(WalkInSalesRequest request) throws Exception;
	
	ItemsDetailsResponse allocateBatchWithTariff(WalkInSalesRequest request) throws Exception;
	
	Response savePatient(WalkInSalesRequest request) throws Exception;
	
	
	//Ganesh
	Response consumeItemFromContract(ParamacyContractReqDto dto) throws Exception;
	
	Response consumeItemFromContract(PharmacyBillCommonDto dto) throws Exception;
	
	
	
	
	
	//Response consumeItemFromContractBillableConsumption(BillableConsumptionSave request,Integer admissionId,Integer encounterId) throws Exception;
	Response consumeItemFromContractBillablePendingIssue(PendingIssuesSave pendingIssuesSave,Integer admissionId,Integer encounterId) throws Exception;
	Response consumeItemFromPatientConsumableIssueSave(PatientConsumableIssueSave patientConsumableIssueSave,Integer admissionId) throws Exception;
	Response removeContractItems(CommonBillingItemsDto itemRequest ) throws Exception;
	Response consumePackageContractItems(CommonBillingItemsDto itemRequest) throws Exception;
	Response consumeItemFromPatientConsumtionSave(PatientConsumtionIssueSave patientConsumtionSaveItem,Integer admissionId) throws Exception;
	
	Response updatePayeePharamcy(List<OrderDetailsMasterDto> pharamcyList,BillingMasterDto billingMasterDto) throws Exception;
	//Response updatePayeePharamcyService(SaleDtoChangeOfPayee sale,Integer orderId,List<OrderDetailsMasterDto> orderDetailsList,BillingMasterDto billingMasterDto) throws Exception;
	Response updatePayeeConsumeFromContract(SaleDtoChangeOfPayee sale,Integer orderId,UpdatePayeeDto billingMasterDto) throws Exception;

	void revertAllocatedQty(Long saleId, Integer screenId, Integer unitId, String cancelledRemark, Integer cancelledReasonId, Integer userId) throws Exception;

	List<AllDataResponse> allDataList(Integer itemid , Integer storeId, Integer orgId, Integer unitId);

	AvailableQty availableQty(Integer storeid, Integer itemid, Integer batchNo, Integer uontypeid, Integer unitId);

	CommonResponse saveWalkInPrescription(WalkInPrescriptionReq request);
	

	CommonResponse saveGlobalSale(Sale sale) throws Exception;

	CommonResponse deleteAllocationScheduler(Date date);
	
	String getPrefixNumber(Integer storeId, Integer unitId, Integer screenId);
	
	
	Response getTerrifData(PharmacyBillCommonDto dto)throws Exception;
	
	//Sales Return
   Response pharamcyPackageContractReturn(BillingMasterDto dto,List<OrderDetailsMasterDto> orderDetailsList)throws BillingException, Exception;
   
	//Sales Return
   Response getPharamcySurchargeSericeId(Integer saleId,Integer paymentEntitlementId,Integer patientClassId,Integer patientTypeId)throws BillingException, Exception;

 	List<AllDataResponse> allExpiredDataList(Integer itemid, Integer storeId, Integer unitId);

	CommonResponse revertStock(Integer orgId, Integer unitId);
 	
	Integer inactiveSaleBySaleId(Integer saleId) throws Exception;

	Response updatePayeeRetunService(List<OrderDetailsMasterDto> pharamcyList, BillingMasterDto billingMasterDto)throws Exception;

	Response consumePackageContractForReturnItems(CommonBillingItemsDto item) throws Exception;

	Response applyDiscountOnPharamcyReturn(List<OrderDetailsMasterDto> pharamcyList, BillingMasterDto billingMasterDto) throws Exception;

	CommonResponse updateBill(WalkInSalesRequest request);

	CommonResponse getStatus(Integer prescriptionId)throws Exception;

	/** @param - make sale status complete is return same sale quantity
	 *  @author -Ganesh Chaudhari 
	 * @throws Exception */
	Integer sameQtySaleReturnSaleStatusUpdate(List<Integer> saleIds,Integer statusId) throws Exception;

	List<SaleDetail> getSaleDetailsBySaleId(Long long1)throws Exception;

	void updateInsertIntoStoreStock(List<SaleDetail> details, Long id, WalkInSalesRequest request, Boolean fromScreen)throws Exception;

	CommonResponse getSaleStatus(Long id);

	Response updatediscountCategory(OrderDetailsMasterDto detailsMasterDto);

	Response addContractCapAmountPharmacy(ContractConsumptionDto contractDtoClone, Integer saleId) throws Exception;

	Response cancelPharmacyPackageCapping(ContractConsumptionDto contractDtoClone, Integer saleId, boolean isRecalcualtion, boolean pharmacyReturnServiceCal) throws Exception;

	Response cancelPharmacyReturnCapping(ContractConsumptionDto contractDto, Integer orderId,boolean isRecalcualtion) throws Exception;

	Response addPackageContractForReturnItems(CommonBillingItemsDto item,boolean isRecalcualtion) throws Exception;

	
}