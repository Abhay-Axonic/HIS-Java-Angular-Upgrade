package com.param.adt.discharge.dao;

import com.param.adt.common.Response;
import com.param.adt.discharge.model.ShortLeave;
import com.param.global.dto.DischargeDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IShortLeaveDao extends IGenericDao<ShortLeave, Integer>{

	
	Response saveShortLeaveRequest(DischargeDto dischargeDto)throws ApplicationException;

	Response getShortLeaveRequestList(DischargeDto dischargeDto)throws ApplicationException;

	Response getShortLeaveRequestListForDoctor(DischargeDto dischargeDto) throws ApplicationException;

	Response acceptShotLeaveByDoctor(DischargeDto dischargeDto)throws ApplicationException;

	Response rejectShotLeaveByDoctor(DischargeDto dischargeDto)throws ApplicationException;

	Response acceptShotLeaveByBilling(DischargeDto dischargeDto)throws ApplicationException;

	Response rejectShotLeaveByBilling(DischargeDto dischargeDto)throws ApplicationException;

	Response releasePatientForShotLeave(DischargeDto dischargeDto)throws ApplicationException;

	Response getDoctorAcceptedPatientsForShortLeave(DischargeDto dischargeDto)throws ApplicationException;

	Response rejectShotLeaveByNurse(DischargeDto dischargeDto)throws ApplicationException;

	Response getShortLeaveRequestListCount(DischargeDto dischargeDto)throws ApplicationException;

}
