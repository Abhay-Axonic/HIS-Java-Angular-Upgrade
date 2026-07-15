package com.param.billing.global.transaction.service;

import com.param.global.common.Response;

import java.util.List;

import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
@SuppressWarnings("rawtypes")
public interface IDoctorSlabService {

	Response getlistForDoctorSlab(TDoctorShareReqDto tDoctorShareDto);

	Response splitRevenue(TDoctorShareSplitLogsReqDto masterDto);

	Response getDoctorSlabSplitDetails(Integer id);

	Response changeDoctor(TDoctorShareRespDto masterDto);

	Response inactiveSplitData(TDoctorShareRespDto masterDto);

	Response saveVoucher(TDoctorShareProcessReqDto tDoctorShareProcessReqDto);

	Response getVoucherDetails(TDoctorShareReqDto tDoctorShareDto);

	Response voucherSlabConsumptionDetails(Integer docShareVoucherId);

}
