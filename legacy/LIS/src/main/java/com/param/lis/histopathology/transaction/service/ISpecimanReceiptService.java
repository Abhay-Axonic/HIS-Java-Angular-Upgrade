package com.param.lis.histopathology.transaction.service;

import java.util.List;

import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dto.specimanReceiptDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ISpecimanReceiptService {
	
	public Response listSpecimanReceipt(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException;
	public Response getTotalSpecimanReceiptRecord(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException;
	public Response collectionSpeciman(List<specimanReceiptDto> listspecimanReceiptDto);

}
