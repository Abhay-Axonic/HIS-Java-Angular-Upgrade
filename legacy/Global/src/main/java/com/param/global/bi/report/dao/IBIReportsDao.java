package com.param.global.bi.report.dao;

import com.param.global.bi.report.dto.BIReportsCommonDto;
import com.param.global.bi.report.dto.BIReportsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IBIReportsDao {
	Response getBIReportsAll(BIReportsDto bIReportsDto) throws ApplicationException;

	Response getBIReportsForSpecialityAndPayModes(BIReportsDto bIReportsDto) throws ApplicationException;

	Response getBIReportsAllForCsv(BIReportsDto bIReportsDto) throws ApplicationException;

	Response getPendingAndPartialBillPayments(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getUnitWiseDailyPatientCountWithGender(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getUnitWiseDailyCollection(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getBIReportsAllCollectionList(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getBIReportsAllCollectionNew(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getAllCollectionListDateWise(BIReportsCommonDto bIReportsCommonDto, String inpuDateFormat);

	Response getPatientSourceWiseData(BIReportsCommonDto bIReportsCommonDto, String inputDateFormat)throws ApplicationException;

	Response getEntitlementWiseData(BIReportsCommonDto bIReportsCommonDto, String inputDateFormat)throws ApplicationException;

	Response getExpenseData(BIReportsCommonDto bIReportsCommonDto, String inpuDateFormat)throws ApplicationException;

	Response getStatusWiseTestCount(BIReportsCommonDto bIReportsCommonDto, String inputDateFormat)throws ApplicationException;

	Response getBIReportsRoomOccupancy(BIReportsCommonDto bIReportsCommonDto)throws ApplicationException;

	Response getBIReportsForDashboard(BIReportsCommonDto bIReportsCommonDto)throws ApplicationException;



}
