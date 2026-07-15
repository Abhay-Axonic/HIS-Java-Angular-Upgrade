package com.param.procurement.purchase.service.goodReceiptNote;

import org.springframework.util.MultiValueMap;

import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.entity.model.procurement.GrnSettlement;
import com.param.global.service.items.CustomException;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDropdownDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStatusDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GrnSettledRequest;

public interface IGoodReceiptNoteService {

	GRNDropdownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getGRNList(GRNRequest grnRequest);

	Object updateGoodReceiptNote(GRNRequest grnRequest);

	Object saveGoodReceiptNote(GRNRequest grnRequest) throws Exception;

	Object getGoodReceiptNote(Integer id, Integer unitId, Integer orgId);

	Object cancelGoodReceiptNote(GRNRequest grnRequest);

	Object approveGoodReceiptNote(GRNStatusDto grnStatusDto);

	Object previousGrnDetails(Integer vendorId, Integer itemid, Integer grnId);

	Object settledGoodReceiptNote(GrnSettledRequest grnSettledRequest) throws CustomException;
	Object settledGoodReceiptNoteSave(GrnSettledRequest grnSettledRequest) throws CustomException;
	Object previousNewGrnDetails(Integer vendorId, Integer itemid);

	Integer saveGrnSettlement(GrnSettlement grnsettle);

	void updateGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId, Double amount);

	GrnNetAmount getGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId);

	
	Object getInvoiceNumber(GRNRequest grnRequest);

	void updateBonusBatchExpiry(Integer id);

	void saveBonusBatchExpiry(GrnBonusBatchExpiry grnBatchExpiry1);

}
