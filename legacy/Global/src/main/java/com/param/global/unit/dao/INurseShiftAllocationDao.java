package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.NurseShiftAllocationDetailsDto;
import com.param.global.unit.dto.NurseShiftAllocationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface INurseShiftAllocationDao {

	public Response getCheckNurseAlocation(NurseShiftAllocationDto nurseShiftAllocationDto)throws ApplicationException;

	public Response saveNurseShiftAllocation(NurseShiftAllocationDto nurseShiftAllocationDto)throws ApplicationException;

	public Response updateNurseShiftAllocation(NurseShiftAllocationDto nurseShiftAllocationDto)throws ApplicationException;

	public Response activeInactiveNurseShiftAllocation(Integer orgId, Integer unitId,
			Integer nurseShiftAllocationDetailsId, Character shiftStatus)throws ApplicationException;

	public Response getNurseShiftDetailsById(NurseShiftAllocationDto nurseShiftAllocationDto) throws ApplicationException;

	public Response getActiveNurseAllocationList(NurseShiftAllocationDto nurseShiftAllocationDto)throws ApplicationException;

	public Response listNurseAllocation(NurseShiftAllocationDto nurseShiftAllocationDto)throws ApplicationException;

	public Response getNurseShiftByDetailsId(NurseShiftAllocationDto nurseShiftAllocationDto)throws ApplicationException;

	public Response getNurseShiftWithBedAllocated(NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto)
	throws ApplicationException;

	public Response getTotalRecordNurseShiftAllocation(Integer orgId, Integer unitId)
			throws ApplicationException;

	public Response checkbedIdbyshiftdate(NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto)
			throws ApplicationException;

	public Response getbedlistbywardId(NurseShiftAllocationDto nurseShiftAllocationDto)throws ApplicationException;

	public Response getbedlistbyNurseId(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException;
	public Response updateNurseShiftAllocationDashboardStatus(NurseShiftAllocationDto nurseShiftAllocationDto);

	public Response getAssignedBedList(NurseShiftAllocationDto nurseShiftAllocationDto);

}
