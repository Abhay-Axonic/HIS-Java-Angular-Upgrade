package com.param.pharmacy.op.dao.salesWorkList;

import java.util.List;

import com.param.entity.model.master.Item;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.pharmacy.dto.SubstituteItemDto;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetailItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.prescriptionDetailResp;
import com.param.pharmacy.op.dto.SaleDetailResp;
import com.param.pharmacy.op.dto.SalesWorkQty;
import com.param.pharmacy.op.dto.salesWorkList.FrequencyResponse;
import com.param.pharmacy.op.dto.salesWorkList.PrescriptionDetailItemResponse;
import com.param.pharmacy.op.dto.salesWorkList.PrescriptionDetailResponse;
import com.param.pharmacy.op.dto.salesWorkList.ResponseWorkListSearch;
import com.param.pharmacy.op.dto.salesWorkList.WorkListSearch;

public interface ISalesWorkListDao {

	List<Prescription> worklistSearchByFields(WorkListSearch WorkListSearch, Integer screenId);

	Prescription prescriptionByIdValue(Long prescriptionId);

	List<PrescriptionDetail> prescriptionDetailsByIdValue(Long prescriptionId);

	List<SubstituteItemDto> getSubstituteById(Integer id, Integer itemId, Boolean isNarcotic, Integer storeId, Integer formulationTypeId);

	Long getDispensedQuantity(Long id);

	List<PrescriptionFrequencyDetail> prescriptionFreqDetailByPresDetailId(Long id);

	Sale getSaleValueByPrescriptionId(Long prescriptionId);

	Long getBatchId(Integer itemId, Long saleId);
	
	List<SaleDetail> getSaleDetail(Long prescriptionDetailId);

	List<ResponseWorkListSearch> worklistSearchByFieldsBP(WorkListSearch workListSearch, Integer screenId,Integer unitId);

	List<PrescriptionDetailItemResponse> getSubstituteByIdBP(Integer id, Integer itemId);

	List<PrescriptionDetailResponse> prescriptionByIdValueBP(Long prescriptionId);

	List<PrescriptionDetailItemResponse> prescriptionDetailsByIdValueBP(Long prescriptionId);

	List<SalesWorkQty> getBatchIdBP(List<Integer> itemIds, Long saleId);

	List<SalesWorkQty> getDispensedQuantityBP(List<Long> detailIds, Integer unitId);

	List<FrequencyResponse> prescriptionFreqDetailByPresDetailIdBP(List<Long> detailIds);

	List<SaleDetailResp> getSaleDetailBP(List<Long> detailIds);

	List<prescriptionDetailResp> prescriptionDetailsByIdValueBP(List<Long> saleIds);

	List<prescriptionDetailResp> getSaleDetailBYSaleIdBP(List<Long> saleIds);

	List<HandoverDetailItem> getSaleDetailBYSaleIdBP(Long id);

	List<PharmacologyItem> getSubstituteByIdAssignPickerBP(Integer id, Integer itemId);

	List<ResponseWorkListSearch> worklistSearchByFieldsAndDepartment(WorkListSearch workListSearch, Integer screenId,
			List<Integer> deptId, Integer unitId);

	Prescription getSalesWorkList(Integer id);

	Prescription updateSalesWorkList(Prescription prescription);

	List<PrescriptionDetailItemResponse> getissuedItemList(Long prescriptionId);

	List<PharmacologyItem> getissuedItemListForNewIssueItems(Integer id);

	void inActiveSaleDetailForSubItems(Integer saleDetailId);


}
