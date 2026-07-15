package com.param.billing.global.transaction.service;

import java.util.List;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.BillDetailsByBillIdDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.dto.TDoctorShareLoggerReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ITDoctorShareService {
	public Response getDoctorShareList(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException ;

	public Response getDoctorShareListCount(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException ;

	public Response saveDoctorShareList(TDoctorShareProcessReqDto reqDto) throws ApplicationException; 
	public Response saveDoctorSharePaymentVoucherList(TDoctorShareLoggerReqDto reqDto) throws ApplicationException; 
	public Response getDoctorSharePaymentVoucherList(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException;
	public Response getDoctorSharePaymentVoucherDtlsByDoctorShareId(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException;
	public Response saveDoctorShareSplit(TDoctorShareSplitLogsReqDto masterDto)throws ServiceException;
	public Response getDocShareSplitDetails(Integer docShareId)throws ServiceException;
	public Response getProcessDocShare(Integer docShareId)throws ServiceException;
	public Response changeDoctor(TDoctorShareRespDto masterDto)throws ServiceException;
	Response updateStatus(TDoctorShareRespDto masterDto) throws ServiceException;
	public Response getPendingServicesForDoctorShareByBillId(TDoctorShareReqDto tDoctorShareDto);
	public Response processDocShareForAlreadyBilledServices(List<BillDetailsByBillIdDto> billDetailsList);
	Response getDoctorReferralList(TDoctorShareReqDto reqDto) throws ApplicationException;
	public Response changeDoctorRefeeral(TDoctorShareRespDto masterDto);
	Response saveDoctorReferralList(TDoctorShareProcessReqDto reqDto) throws ApplicationException;
	Response getDoctorReferralPaymentVoucherDtlsByReferralId(TDoctorShareReqDto tDoctorShareDto)throws ApplicationException;
	public Response<TDoctorShareLoggerReqDto, Integer> saveDoctorReferralPaymentVoucherList(TDoctorShareLoggerReqDto reqDto);
	Response getDoctorRefferalPaymentVoucherList(TDoctorShareReqDto reqDto) throws ApplicationException;
	public Response getDoctorShareProcessedVoucherList(TDoctorShareReqDto tDoctorShareDto)throws ApplicationException;
	public Response updateDocShareAfterCreditNote(CreditNoteMasterDto creditNoteMasterDto);
	Response updateDocShareAfterCreditNoteCancellation(CreditNoteMasterDto creditNoteMasterDto);
	
}
