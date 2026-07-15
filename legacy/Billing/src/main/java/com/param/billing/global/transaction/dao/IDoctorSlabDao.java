package com.param.billing.global.transaction.dao;

import com.param.billing.global.model.DoctorSlabTransaction;
import com.param.billing.global.transaction.dto.DoctorSlabTransactionDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IDoctorSlabDao {

	Response saveDoctorSlabTransaction(DoctorSlabTransactionDto doctorSlabTransactionDto);

	Response getlistForDoctorSlab(TDoctorShareReqDto tDoctorShareDto);

	Response splitRevenue(TDoctorShareSplitLogsReqDto masterDto);

	Response getRevenueTransactionById(Integer doctorSlabTransactionId);

	Response updateDoctorSlabTransaction(DoctorSlabTransaction doctorSlabTransaction);

	Response getDoctorSlabSplitDetails(Integer id);

	Response changeDoctor(TDoctorShareRespDto masterDto);

	Response inactiveSplitData(TDoctorShareRespDto masterDto);

	Response getVoucherDetails(TDoctorShareReqDto tDoctorShareDto);

	Response voucherSlabConsumptionDetails(Integer docShareVoucherId);

}
