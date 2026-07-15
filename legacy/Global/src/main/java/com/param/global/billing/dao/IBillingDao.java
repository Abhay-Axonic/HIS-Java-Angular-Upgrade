package com.param.global.billing.dao;

import com.param.billing.global.CostEstimateDto;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.dto.BillingStatusMapperDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;

import in.param.common.dao.IGenericDao;
@SuppressWarnings("rawtypes")
public interface IBillingDao extends IGenericDao<BillingMaster, Integer> {

	BillingMaster getBillMasterObject(Integer billId);
	
	Response updateBillMaster(BillingMaster billingMasterObj);

	Response saveBillingDetails(BillingDetailsDto billingDetailsDto);

	Response getBillPayeeDetailsByBillId(Integer billId);

	int updatePayeeDetailsByPayeeAndBillId(OrderDetailsPayeeMapperDto detailsPayeeMapperDto, Integer billId, Integer unitid);

	Response saveBillPayeeDetails(BillPayeeDetailsDto billPayeeDetailsDto);

	Response saveBillStatus(BillingStatusMapperDto mapperDto);
	
	AdmissionsForBatchProcessDto getBillByEncounterId(Integer encounterId);

	Response saveEstimate(CostEstimateDto costEstimationDto);

	Response saveEstimateDetails(CostEstimateDto costEstimationDto);

	AdmissionListIpdChargesResDto getAdmissionsById(int admissionId) throws BillingException;

	Response updateLabSampleStatus(Integer labSampleDetailsId)throws BillingException;

	AdmissionsForBatchProcessDto getBillDataByAdmissionId(Integer admissionId);

	Response updateAdmissionIsFinanceFlag(BillingMasterDto billingMasterDto)throws BillingException;

	Response changeBillStatus(Integer billId);

	Response getPaymentEntitlement(Integer billingMasterId);
	
	Response getPayeeDetailsByPayeeAndBillId(Integer billId)throws BillingException;

	Response updatePayeePriority(BillPayeeDetailsDto billPayeeDetailsDto);
	
	TempPatientRegistration savePatient(TempPatientRegistration tempPatientRegistration);

	Response updateAdmissionsIsFinancialClearanceN(BillingMasterDto billingMasterDto);

	Response getPackageConsumeAmountItemwise(Long id, Integer orderDetailsId)throws BillingException;


}
