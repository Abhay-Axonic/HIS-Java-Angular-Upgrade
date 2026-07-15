package com.param.inventory.store.dao.rejectedMaterialAcceptance;

import java.util.List;

import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.RejectDetail;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.rejectMaterialAcceptance.RejectMaterialResponse;

public interface IRejectedMaterialAcceptanceDao {

//	List<Reject> searchRejectMaterial(MaterialPickerRequest request, Integer screenId);
	
	List<RejectMaterialResponse> searchRejectMaterial(MaterialPickerRequest request, Integer screenId);

	Reject getReject(Integer id);

	List<RejectDetail> getRejectDetail(Integer id);

	RejectDetail getRejectDetailById(Integer id);

	Reject updateReject(Reject reject);

	Reject save(Reject rejectNew);

	Integer updatereturn(Integer returnId,Integer acceptQty);

	

}
