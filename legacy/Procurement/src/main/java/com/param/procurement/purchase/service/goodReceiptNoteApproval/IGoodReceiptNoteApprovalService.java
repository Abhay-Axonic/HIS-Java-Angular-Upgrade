package com.param.procurement.purchase.service.goodReceiptNoteApproval;

import java.math.BigInteger;
import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDropdownDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStatusDto;

public interface IGoodReceiptNoteApprovalService {

	GRNDropdownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getGRNList(GRNRequest grnRequest);

	Object getGoodReceiptNote(Integer id, Integer unitId, Integer orgId);

	CommonResponse approveGoodReceiptNote(List<GRNStatusDto> grnStatusDtoList);

	CommonResponse approveGoodReceiptNoteDetail(GRNRequest grnRequest);

	Object getGRNSettlementList(GRNRequest grnRequest);

	Object getGoodReceiptNoteSettledList(GRNRequest grnRequest);

	Object getGoodReceiptNoteSettledDetails(Integer grnid ,Integer unitId);

	CommonResponse saveAutoPhysicalInspection(PhysicalInspection phyInspection, Integer storeId);
	
	Object getGRNSettlementDetails(Integer id,Integer unitId, Integer orgId);

	boolean updateBatchIdForBonusGrn(Integer key, Long value);

	BigInteger alreadyGoodReceiptNoteExits(List<Integer> grnIds);

}
