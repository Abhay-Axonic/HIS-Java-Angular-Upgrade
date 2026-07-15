package com.param.adt.master.unit.dao;

import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.global.model.BedMaster;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IBedMasterDao extends IGenericDao<BedMaster, Integer> {

	Response checkBedAlreadyExistOrNot(BedMasterDto bedMasterDto)throws ApplicationException;

	Response addBed(BedMasterDto bedMasterDto)throws ApplicationException;

	Response saveBedLog(BedMasterDto bedMasterDto)throws ApplicationException;

	Long getNextValBedMasterId()throws ApplicationException;

	Response getBedMasterList(BedMasterDto bedMasterDto) throws ApplicationException;

	Response getBedByNameNotId(BedMasterDto bedMasterDto)throws ApplicationException;

	Response updateBedMaster(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getbedById(Integer bedId)throws ApplicationException;

	Response updateStatus(BedMasterDto bedMasterDto)throws ApplicationException;

	Response bedListCount(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getBedCategoryAvailabilityCount(int organizationId, int unitId)throws ApplicationException;
	
	Response getBedByNameAndDesc(BedMasterDto bedMasterDto)throws ApplicationException;

	//Response addBedLog() throws ApplicationException;

	Response getDataForBedManagementData(BedMasterDto bedMasterDto,boolean search)throws ApplicationException;

	Response getBedMovingBedList(BedMasterDto bedMasterDto)throws ApplicationException;

	Response movingBedListCount(BedMasterDto bedMasterDto)throws ApplicationException;

	Response activeMovingBedList(BedMasterDto bedMasterDto)throws ApplicationException;

	Response postBedMagementCount(Integer unitId) throws ApplicationException;

	Response getDialysisChair(BedMasterDto bedMasterDto);

	Response getDataForBedManagementDataForER(BedMasterDto bedMasterDto, boolean search) throws ApplicationException;

	Response bedMasterUniversalSearch(BedMasterDto bedMasterDto) throws ApplicationException;

	Response getBedByStatusId(BedMasterDto bedMasterDto)throws ApplicationException;
	
	Boolean checkBedNumberExistOrNot(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getPaymentEntitlmentbyAdmissionId(BedMasterDto bedMasterDto) throws ApplicationException;

	Integer getConfiguredBedLimit(BedMasterDto bedMasterDto);

	Integer getBedCount(BedMasterDto bedMasterDto);

	Response getIcuMonitorData(BedMasterDto bedMasterDto) throws ApplicationException;

	Response getInterventionDetails(List<Integer> patientIds, Integer unitId, Integer organizationId) throws ApplicationException;
}
