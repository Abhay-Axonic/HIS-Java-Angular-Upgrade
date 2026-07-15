package com.param.billing.global.transaction.service;

import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.transaction.dto.DoctorAdvanceDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDetailsDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IDoctorShareCRNoteService {

	Response getPendingBills(BillListReqDto reqDto);

	Response requestForDoctorCr(DoctorShareCrNoteDto reqDto);

	Response getDoctorCrRequests(BillListReqDto reqDto);

	Response getCrRequestDetails(Integer doctorCrId);

	Response doctorsListForDoctorCr(Integer billId);

	Response acceptDoctorCrRequests(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

	Response rejectDoctorCrRequests(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

	Response<TDoctorShareRespDto, Integer> getInactiveCredit(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

	Response<TDoctorShareRespDto, Integer> getDoctorCrDetailsForDoctorSharePaymentVoucher(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto);

	Response saveDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto);

	Response getDoctorAdvanceData(DoctorAdvanceDto doctorAdvanceDto);

	Response updateDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto);

	Response updateStatusDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto);

	Response acceptDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto);

	Response rejectDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto);

	Response<TDoctorShareRespDto, Integer> getPendingBillsSearch(BillListReqDto reqDto);

}
