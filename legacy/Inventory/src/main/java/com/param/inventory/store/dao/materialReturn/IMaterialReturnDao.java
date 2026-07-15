package com.param.inventory.store.dao.materialReturn;

import java.math.BigInteger;
import java.util.List;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.Return;
import com.param.entity.model.inventory.ReturnDetail;

import com.param.entity.model.inventory.StoreStock;

import com.param.global.common.Response;

import com.param.inventory.store.dto.materialReturn.MaterialResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReturnIssue;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

public interface IMaterialReturnDao {

	Return saveMaterialReturn(Return returnData);

	List<MaterialResponse> searchMaterialReturnList(MaterialReurnRequest request, Integer screenId);

	Return getMaterialReturn(Integer id);

	List<ReturnDetail> getMaterialReturndetail(Integer id);

	Return updateMaterialReturn(Return returnData);

	List<ReasonMaster> getReasonList();

	List<MaterialReturnIssue> getMaterialReturnIssue(PatientIssueReturnRequest patientIssueReturnRequest);
	
	public List<MaterialIssueDetail> getMaterialIssuedetail(Integer id);

	List<MaterialIssueBatchMapper> getMaterialIssueBatchMapper(Integer id, Integer id2);

	List<MaterialReturnIssue> getMaterialReturnIssueData(Integer id);

	BigInteger getReturnQty(Integer id);
	
	/* start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */
	BigInteger getTotalReturnedQty(Integer id,Integer batchId,Integer detailsId);
	BigInteger getTotalComsumedQty(Integer id,Integer batchId);
	/* end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */

	List<MaterialIssueBatchMapper> getMaterialIssueExpireBatchMapper(Integer id, PatientIssueReturnRequest patientIssueReturnRequest);

	BigInteger getTotalTransiQty(Integer id,Integer batchId);

	Return getReturn(Integer id);

	Return updateReturn(Return returnData);

	Reject save(Reject reject);

	StoreStock saveStoreStock(StoreStock storeStock);
	
    Response getMaterialReturnDetails1(List<Integer> ids, Integer fromStoreId, Integer toStoreId);

    Integer getRejectedQty(Integer materialIssueDetailId);

	Integer materialReturnStatus(Integer id);

	 Integer getAcceptedQty(Integer materialIssueDetailId,Integer batchId,Integer id);

	List<MaterialIssueDetail> getMaterialIssuedetailAll(List<Integer> issueIds);

}
