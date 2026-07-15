package com.param.global.bi.report.service;

import javax.transaction.Transactional;

import com.param.global.bi.report.dto.BIReportsCommonDto;
import com.param.global.bi.report.dto.BIReportsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({ "rawtypes" })
public interface IBIReportsService {

	Response getBIReportsAll(BIReportsDto bIReportsDto) throws ApplicationException;

	Response getBIReportsAllForCsv(BIReportsDto bIReportsDto) throws ApplicationException;

	Response getPendingAndPartialBillPayments(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getUnitWiseDailyPatientCountWithGender(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getPendingAndPartialBillPaymentsForCsv(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getDailyPatientCountWithGenderForCsv(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getUnitWiseDailyCollection(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getUnitWiseDailyCollectionForCsvO(BIReportsCommonDto bIReportsCommonDto) throws ApplicationException;

	Response getBIReportsAllCollectionList(BIReportsCommonDto bIReportsCommonDto)throws ApplicationException;

	Response getBIReportsAllCollectionNew(BIReportsCommonDto bIReportsCommonDto)throws ApplicationException;

	Response getAllCollectionList(BIReportsCommonDto bIReportsCommonDto)throws ApplicationException;

	Response getBIReportsRoomOccupancy(BIReportsCommonDto bIReportsCommonDto)throws ApplicationException;

	Response getBIReportsForDashboard(BIReportsCommonDto bIReportsCommonDto)throws ApplicationException;



}
