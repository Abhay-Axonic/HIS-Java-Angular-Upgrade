package com.param.global.pharmacy.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.pharmacy.BatchAllocation;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.common.Response;
import com.param.global.dto.AllDataResponse;
import com.param.global.dto.AvailableQty;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.pharmacy.dto.ItemRequest;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.WalkInSalesRequest;

public interface IWalkInSalesDao {
	
	Integer allocateQuantity(BatchAllocation batchAllocation);
	List<Long> allocateQuantity(List<BatchAllocation> batchAllocation);

	BatchAllocation getBatchAllocation(Integer allocationId);

	Integer updateAllocatedQuantity(BatchAllocation batchAllocation);
	
	List<Long> updateAllocatedQuantity(List<BatchAllocation> batchAllocationlist);

	List<BatchAllocation> getBatchAllocations(Long tempBillId);
	BatchAllocation getBatchAllocationsNew(Long tempBillId);

	boolean deleteBatchAllocation(Integer allocationId);

	boolean deleteBatchAllocations(Long tempBillId);

	Sale saveBill(Sale sale);

	StockTransaction saveStockTransaction(StockTransaction stockTransaction);
	
	Prescription getPrescription(Long id);
	
	PrescriptionDetail getPrescriptionDetail(Long id);
	
	Prescription updatePrescription(Prescription prescription);

	BatchAllocation getBatchAllocation(Long tempBillId, Long batchId);

	SaleDetail saveTax(SaleDetail saleDetail);
	
	TempPatientRegistration savePatient(TempPatientRegistration tempPatientRegistration);
	
	Integer updateConsolidatedBillStatus(Long id,Integer billId);
	
	Integer updateConsolidatedBillStatusBySaleDetails(List<Integer> pharmacyServiceOrderDetailsIds,Integer billId);
	
	Integer updateConsolidatedBillStatusId(Integer billId,Integer statusId);

	Integer sameQtySaleReturnSaleStatusUpdate(List<Integer> saleIds,Integer statusId);
	
	Integer updateOrderMasterAndSaleDetailsMaster(List<Integer> orderDetailIds,Set<Integer> saleDetailId) throws Exception;
	
	Integer inactiveOrderDetailsBySaleId(Integer saleId,Integer billId) throws Exception;
	
	Integer inactiveSaleBySaleId(Integer saleId) throws Exception;

	Sale getSaleBySaleId(Long saleId);

	List<AllDataResponse> allDataList( Integer itemid , Integer storeId, Integer orgId, Integer unitId);

	AvailableQty availableQty(Integer storeid, Integer itemid, Integer batchNo, Integer uontypeid, Integer unitId);

	Prescription savePrescription(Prescription p);

	List<BatchAllocation> getBatchAllocation(Date date);

	BatchAllocation saveBatchAllocation(BatchAllocation batchAllocation);

	List<BatchAllocation> getBatchAllocationByTempBillId(Long tempBillId);
	
	public Response getAdmissionsById(int admissionId)throws BillingException;
	
	List<AllDataResponse> allExpiredDataList(Integer itemid, Integer storeId, Integer unitId);
	
	Response updateIssueDetails(Long id);
	
	Response updateIssue(Long saleId);
	
	Integer activatePharmacyBillAfterBillCancel(Integer billId);
	
	List<BatchAllocation>  getBatchAllocation(Integer orgId, Integer unitId);
	
	BigInteger issueCountByPrescritpion(Integer prescriptionId);
	
	CommonResponse updateBill(WalkInSalesRequest request);
	
	CommonResponse getStatus(Integer prescriptionId);
	
	Integer getIssueDetialsStatus(Integer prescriptionId);
	
	void updateIssueDetailsStatus(Long saleDetailId, Integer sts);
	
	List<SaleDetail> getSaleDetailsBySaleId(Long ids)throws BillingException;
	
	void updateTempDoctorId(Long id, Integer tempDoctorId);
	List<SaleDetailPackageMapperDto> getPharmacyDepositConsumsedAmount(Long saleId);
	Integer updateDepositDetails(List<SaleDetailPackageMapperDto> getDepositDetails);
	void updateDoctorId(Long id, Integer doctorId);
	CommonResponse getStatus(Long id);
	void updateSalePaymentModeIdInDepositMaster(Integer depositMasterIdToUpdateUsingSalePaymentMode, Long id);
	Response updatediscountCategory(OrderDetailsMasterDto detailsMasterDto);
	List<PaymentDepositDetailsDto> getSalePaymentModeDetailsConsumsedAmount(Long saleId, Integer userId);
	
}