package com.param.procurement.quotation.service.consignmentPOInvoice;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.common.dto.CommonResponse;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPORequestDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPOResponseDto;

public interface IConsignmentStockPOInvoiceService {
	
	List<ConsignmentStockPOResponseDto> getConsignmentStockPOInvoiceList(ConsignmentStockPORequestDto consignmentStockPORequestDto);
	
	List<ConsignmentStockPOResponseDto> getConsignmentStockPOList(ConsignmentStockPORequestDto consignmentStockPORequest);
	
	@SuppressWarnings("rawtypes")
	CommonResponse saveConsignmentStockPOInvoice(List<ConsignmentStockPOResponseDto> consignmentStockPOResponseDtoList) throws Exception;
	
	ConsignmentStockPOResponseDto getConsignmentStockPOInvoiceDetail(ConsignmentStockPOResponseDto consignmentStockPOResponseDto);
	CommonResponse updateConsignmentStockPOInvoiceDetail(ConsignmentStockPOResponseDto consignmentStockPOResponseDto);
	
	CommonResponse updateConsignmentStockPOInvoice(List<ConsignmentStockPOResponseDto> consignmentStockPOResponseDto);
	
	Object getTaxList(Integer orgId);

	Object cancelPurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto);

}
