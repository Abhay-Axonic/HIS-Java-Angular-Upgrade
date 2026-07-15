package com.param.inventory.store.dao.materialInward;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchAccessoriesMapper;
import com.param.entity.model.inventory.InwardsUomMapper;
import com.param.entity.model.inventory.MaterialInward;
import com.param.entity.model.inventory.MaterialInwardAccessories;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.inventory.store.dto.materialInward.CreditNoteVendorWise;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;

public interface IMaterialInwardDao {

	List<MaterialInwardResponse> searchMaterialInwardList(MaterialInwardRequest request, Integer screenId);

	MaterialInward saveMaterialInward(MaterialInward materialInward);

	MaterialInward getMaterialInward(Integer id);

	MaterialInward updateMaterialInward(MaterialInward materialInward);

	List<ReturnableGatepass> searchReturnableGatepassList(MaterialInwardRequest request, Integer screenId);

	List<ReturnableGatepass> searchReturnableGatepassList(Integer id, Integer screenId, Integer unitId);

	ReturnableGatepass searchReturnableGatepass(Integer id, Integer screenId);

	Batch saveBatch(Batch batch);

	List<InwardsUomMapper> getInwardUomMapperList(Integer id);

	List<MaterialInwardAccessories> getInwardAccessoriesList(Integer id);

	BatchAccessoriesMapper saveBatchAccessoriesMapper(BatchAccessoriesMapper batchAccessoriesMapper);

	List<CreditNoteVendorWise> getReturnableGatepassVendorWiseSearch(Integer vendorid);

	List<CreditNoteVendorWise> getReturnableGatepassDetails(Integer mateinwardid);

	ReturnableGatepass getReturnableGatepass(ReturnableGatepass returnableGatepassId);

	Integer getApprovedQuantity(ReturnableGatepass returnableGatepass);

	Integer getNumberOfCreditDays(Integer vendorid);
}
