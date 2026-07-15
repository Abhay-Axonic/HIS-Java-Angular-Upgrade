package com.param.inventory.store.dao.returnableGatepass;

import java.util.List;

import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassAccessories;
import com.param.inventory.store.dto.returnableGatepass.ItemResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassResponse;
import com.param.inventory.store.dto.returnableGatepass.accessoriesResponse;

public interface IReturnableGatepassDao {

	List<ReturnableGatepassResponse> getReturnableGatepassList(ReturnableGatepassRequest request, Integer screenId);

	ReturnableGatepass saveReturnableGatepass(ReturnableGatepass gatepass);

	ReturnableGatepassResponse getReturnableGatepassResponse(Integer id);

	List<ItemResponse> getReturnableGatepassDetails(Integer id);
	
	List<ItemResponse> getReturnableGatepassResponseFixedType(Integer id);

	ReturnableGatepass updateReturnableGatepass(ReturnableGatepass gatepass);
	
	ReturnableGatepass getReturnableGatepass(Integer id);
	
	Integer getReturnableGatepassAssetType(Integer id);

	List<ReturnableGatepassAccessories> getReturnableGatepassAccessoriesList(Integer id);

	Integer getQuantity(Integer id);

	Integer getid(Integer id);

	Integer getDetailApprovedQantity(Integer id,Integer reqTypeId);

	Integer getDetailApprovedQantity1(Integer gatepassId);

	Double totalDiscountAmount(Integer gatepassId);

	Double totalAmount(Integer gatepassId);

	void updateIntransitQty(Integer id, Integer leaseRejectedQty, Boolean inTransit, Integer screenId, String string);


    List<ReturnableGatepassResponse> checkReturnableGatePass(List<ReturnableGatepassRequest> request);

	Double totalDiscountAmountUsingBatchId(Long id, Integer getpassDetailsId);

	Double totalAmountUsingBatchId(Long id, Integer getpassDetailsId);
}
