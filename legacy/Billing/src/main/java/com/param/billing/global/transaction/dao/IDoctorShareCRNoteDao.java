package com.param.billing.global.transaction.dao;

import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.transaction.dto.DoctorAdvanceDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDetailsDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.DoctorShareCrNoteDetails;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
@SuppressWarnings("rawtypes")
public interface IDoctorShareCRNoteDao extends IGenericDao<DoctorShareCrNoteDetails, Integer> {

	Response getPendingBills(BillListReqDto reqDto);

	Response requestForDoctorCr(DoctorShareCrNoteDto reqDto);

	Response updateRequestForDoctorCr(DoctorShareCrNoteDto reqDto);

	Response requestForDoctorCrDetails(DoctorShareCrNoteDetailsDto reqDto);

	Response updateRequestForDoctorCrDetails(DoctorShareCrNoteDetailsDto dto);

	Response getDoctorCrRequests(BillListReqDto reqDto);

	Response getCrRequestDetails(Integer doctorCrId);

	Response doctorsListForDoctorCr(Integer billId);

	Response acceptDoctorCrRequests(Integer doctorCrDetailsId,Double amountPaid);

	Response rejectDoctorCrRequests(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

	Response getInactiveCredit(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

	Response getDoctorCrDetailsForDoctorSharePaymentVoucher(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

	Response updateAcceptedDoctorCredit(DoctorShareCrNoteDetailsDto detailsDto, Integer voucherId);

	Response saveDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto);

	Response acceptDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto);
	
	Response rejectDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto);

	Response getDoctorAdvanceData(DoctorAdvanceDto doctorAdvanceDto);

	Response updateDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto);

	Response updateStatusDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto);

	Response updateAcceptedDoctorAdvance(Double dummyConsumedDocAdvance, Integer doctorAdvanceId);

	void checkOrderDocId(Integer billingMasterId);

	Response getPendingBillsSearch(BillListReqDto reqDto);

	Response getReferralCrRequests(BillListReqDto reqDto);

	Response<TDoctorShareRespDto, Integer> getReferralCrDetailsForReferralSharePaymentVoucher(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

}
