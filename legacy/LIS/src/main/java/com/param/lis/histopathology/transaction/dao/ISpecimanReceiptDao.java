package com.param.lis.histopathology.transaction.dao;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dto.specimanReceiptDto;
import in.param.exception.ApplicationException;

	

@SuppressWarnings("rawtypes")
public interface ISpecimanReceiptDao 
{
	
	public Response listSpecimanReceipt(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException;
	public Response getTotalSpecimanReceiptRecord(SearchDto searchDto,Character isPreOrPostBilling) throws ApplicationException;
	public Response collectionSpeciman(LabSampleMaster labSampleMaster)throws ApplicationException;
	public Response updateLabSampleDetailsMaster (LabSampleDetailsMaster labSampleDetailsMaster) throws ApplicationException;
}
