package com.param.inventory.store.dao.materialIssue;

import java.util.List;

import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.global.dto.items.UomResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.dto.materialPicker.SubstituteMaterialIssueDetailDto;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.SubstitutePatientConsumableDto;

public interface IMaterialIssuePendingDao {	

	MaterialIssue getMaterialIssue(Integer id);

	List<MaterialIssueDetail> getMaterialIssueDetail(Integer id);
	
	List<MaterialIssueBatchMapper> getMaterialIssueBatchList(Integer id);

	MaterialIssue updateMaterialIssue(MaterialIssue materialIssue);
	
	
	
	
	

	MaterialIssue saveMaterialIssuePending(MaterialIssue materialIssue);

	List<MaterialIssue> searchMaterialIssueList(MaterialPickerRequest request, Integer screenId);

	StoreStock updateStoreStock(StoreStock storeStock);

	MaterialIssueDetail saveMaterialIssuePendingDetails(MaterialIssueDetail detail);

	Long getbatchIdByMaterialIssueDetailsId(Integer id);

	MaterialIssueDetail getMaterialIssueDetailById(Integer id);

	
	
	
	List<MaterialPickerResponse> searchMaterialIssuePendingList(MaterialPickerRequest materialPickerRequest,
			Integer screenId);

	MaterialPickerResponse getMaterialIssuePending(Integer id);

	MaterialPickerResponse getMaterialIssuePendingWithoutPicker(Integer id);
	
	List<MaterialPickerDetailResponse> getMaterialIssuePendingDetail(Integer id);

	MaterialIssueDetail getPickerQty(Integer id);

	MaterialIssueDetail getMaterialPickerData(Integer id);

	void updateMaterialIssuePending(MaterialIssue materialIssue);

	MaterialIssueDetail getMaterialIssueDetailsForSubstitute(Integer parentId);

	List<SubstituteMaterialIssueDetailDto> getSubstituteMaterialIssueDetails(Integer id);

	List<MaterialIssueBatchMapper> getMaterialBatchMapperForSubstitute(Integer id);

	StoreIndentDetail getStoreIndentDetailsForSubstitute(Integer id);

	MaterialIssueDetail saveMaterialIssuePendingDetailsForSubstitute(MaterialIssueDetail mterialDtl);

	List<SubstituteMaterialIssueDetailDto> getSubstituteIssueDetailsForMaterialIssueDetail(Integer materialIssueId);

	List<MaterialIssueBatchMapper> getMaaterialBatchMapperForSubstitute(Integer id);

	SubstituteMaterialIssueDetailDto getMaterialIssueForSub(Integer id);

	void updateStoreIndentStatus(Integer id, Integer status);

	Integer saveMaterialIssue(Integer id);

	List<UomResponse> getStoreStockByItemPharmacyAll(Integer toStoreId, Integer unitId);

	List<MaterialIssueBatchMapper> getMaterialIssueBatchListAll(List<Integer> issueIds);

	List<MaterialIssueDetail> getPickerQtyAll(List<Integer> issueIds);

	List<SubstituteMaterialIssueDetailDto> getSubstituteIssueDetailsForMaterialIssueDetailAll(
			List<Integer> materialIssueIds);

	List<MaterialIssueBatchMapper> getMaaterialBatchMapperForSubstituteAll(List<Integer> subDetailsIds);

}
