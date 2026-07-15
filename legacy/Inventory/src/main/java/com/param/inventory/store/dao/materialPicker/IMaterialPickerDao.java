package com.param.inventory.store.dao.materialPicker;

import java.util.List;

import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.MaterialPicker;
import com.param.entity.model.inventory.MaterialPickerDetail;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;

public interface IMaterialPickerDao {

	List<MaterialPickerResponse> searchMaterialPickerList(MaterialPickerRequest materialPickerRequset,
			Integer screenId);

	MaterialPickerResponse getMaterialPicker(Integer id);

	MaterialPicker getMaterialPickerByIndent(Integer id);
	
	List<MaterialPickerDetailResponse> getMaterialPickerDetails(Integer id);

	MaterialIssue saveMaterialIssue(MaterialIssue materialIssue);
	
	MaterialPicker fetchMaterialPicker(Integer id);
	
	List<MaterialPickerDetail> fetchMaterialPickerDetail(Integer id);
	
	
	//Material Issue methods
	List<MaterialPicker> searchMaterialPicker(MaterialPickerRequest materialPickerRequset, Integer screenId);	

	List<MaterialPickerDetail> getMaterialPickerDetail(Integer id);

	List<MaterialIssueBatchMapper> getMaterialPickerBatchList(Integer id);

	MaterialPickerResponse getPickerData(Integer id);

	MaterialIssue getMaterialIssue(Integer id);

	void updateMaterialIssue(MaterialIssue maIssue);

	void saveMaterialIssueDetails(MaterialIssueDetail issueDetail);



}
