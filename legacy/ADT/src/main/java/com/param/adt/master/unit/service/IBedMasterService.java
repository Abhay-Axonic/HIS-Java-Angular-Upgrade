package com.param.adt.master.unit.service;

import com.param.adt.master.global.dto.BedMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBedMasterService {
	
	Response addBed(BedMasterDto bedMasterDto)throws ApplicationException;
	
	Response getBedMasterList(BedMasterDto bedMasterDto)throws ApplicationException;

	Response updateBedMaster(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getbedById(Integer bedId)throws ApplicationException;

	Response updateStatus(BedMasterDto bedMasterDto)throws ApplicationException;

	Response bedListCount(BedMasterDto bedMasterDto) throws ApplicationException;

	Response getBedCategoryAvailabilityCount(int organizationId, int unitId)throws ApplicationException;

	Response getDataForBedManagementData(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getBedMovingBedList(BedMasterDto bedMasterDto)throws ApplicationException;

	Response movingBedListCount(BedMasterDto bedMasterDto)throws ApplicationException;

	Response activeMovingBedList(BedMasterDto bedMasterDto)throws ApplicationException;
	
	Response postBedMagementCount(Integer unitId) throws ApplicationException;

	Response getDialysisChair(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getDataForBedManagementDataSearch(BedMasterDto bedMasterDto);

	Response bedMasterUniversalSearch(BedMasterDto bedMasterDto) throws ApplicationException;

	Response getPaymentEntitlmentbyAdmissionId(BedMasterDto bedMasterDto) throws ApplicationException;

	Response getIcuMonitorData(BedMasterDto bedMasterDto) throws ApplicationException;
}
