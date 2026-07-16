package com.param.global.unit.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.INurseShiftAllocationDao;
import com.param.global.unit.dto.NurseShiftAllocationDetailsDto;
import com.param.global.unit.dto.NurseShiftAllocationDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NurseShiftAllocationServiceImpl implements INurseShiftAllocationService, ICommonConstants, IError {

	private static final Logger logger = Logger.getLogger(NurseShiftAllocationServiceImpl.class);

	@Autowired
	INurseShiftAllocationDao iNurseShiftAllocationdao;

	@Override
	public Response saveNurseShiftAllocation(NurseShiftAllocationDto nurseShiftAllocationDto) {
		Response res = iNurseShiftAllocationdao.getCheckNurseAlocation(nurseShiftAllocationDto);

		try {
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				List<NurseShiftAllocationDto> list = res.getListObject();
				nurseShiftAllocationDto.setNurseShiftAllocationId(list.getFirst().getNurseShiftAllocationId());
				return new Response<>(ERROR, ERROR_CODE, "Already Exist", null, null);
			}

			for(NurseShiftAllocationDto nurseList:nurseShiftAllocationDto.getNurseShiftList())
            {
			 nurseShiftAllocationDto.setNurseId(nurseList.getNurseId());
			 nurseShiftAllocationDto.setWardId(nurseList.getWardId());
			 nurseShiftAllocationDto.setShiftList(nurseList.getShiftList());
			 nurseShiftAllocationDto.setFromDate(nurseList.getFromDate());
			 nurseShiftAllocationDto.setToDate(nurseList.getToDate());
			 iNurseShiftAllocationdao.saveNurseShiftAllocation(nurseShiftAllocationDto);
			}

			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("saveNurseShiftAllocation failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNurseShiftAllocation(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
				return iNurseShiftAllocationdao.updateNurseShiftAllocation(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("updateNurseShiftAllocation failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response activeInactiveNurseShiftAllocation(Integer orgId, Integer unitId,
			Integer nurseShiftAllocationDetailsId, Character shiftStatus) throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.activeInactiveNurseShiftAllocation(orgId, unitId,
					nurseShiftAllocationDetailsId, shiftStatus);
		} catch (Exception e) {
			logger.error("activeInactiveNurseShiftAllocation failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getNurseShiftDetailsById(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.getNurseShiftDetailsById(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getNurseShiftDetailsById failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	}

	@Override
	public Response getActiveNurseAllocationList(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.getActiveNurseAllocationList(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getActiveNurseAllocationList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

	}

	@Override
	public Response listNurseAllocation(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.listNurseAllocation(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("listNurseAllocation failed", e);
			return new Response(ERROR, ERR_500, ERR_500, null, null);
		}

	}

	@Override
	public Response getNurseShiftByDetailsId(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.getNurseShiftByDetailsId(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getNurseShiftByDetailsId failed", e);
			return new Response<>(ERROR, ERR_500, ERR_500, null, null);
		}

	}

	@Override
	public Response getNurseShiftWithBedAllocated(NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto)
			throws ApplicationException {

		try {
			return iNurseShiftAllocationdao.getNurseShiftWithBedAllocated(nurseShiftAllocationDetailsDto);
		} catch (Exception e) {
			logger.error("getNurseShiftWithBedAllocated failed", e);
			return new Response<>(ERROR, ERR_500, ERR_500, null, null);
		}

	}

	@Override
	public Response getTotalRecordNurseShiftAllocation(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.getTotalRecordNurseShiftAllocation(orgId,unitId);
		} catch (Exception e) {
			logger.error("getTotalRecordNurseShiftAllocation failed", e);
			return new Response<>(ERROR,ERR_500,ERR_500,null,null);
		}
	}

	@Override
	public Response checkbedIdbyshiftdate(NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto)
			throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.checkbedIdbyshiftdate(nurseShiftAllocationDetailsDto);
		} catch (Exception e) {
			logger.error("checkbedIdbyshiftdate failed", e);
			return new Response<>(ERROR,ERR_500,ERR_500,null,null);
		}
	}

	@Override
	public Response getbedlistbywardId(NurseShiftAllocationDto nurseShiftAllocationDto) {
		try {
			return iNurseShiftAllocationdao.getbedlistbywardId(nurseShiftAllocationDto);
	} catch (Exception e) {
		logger.error("getbedlistbywardId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

	@Override
	public Response getbedlistbyNurseId(NurseShiftAllocationDto nurseShiftAllocationDto) throws ApplicationException {
		try {
			return iNurseShiftAllocationdao.getbedlistbyNurseId(nurseShiftAllocationDto);
        }catch (Exception e) {
		logger.error("getbedlistbyNurseId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
    @Override
	public Response updateNurseShiftAllocationDashboardStatus(NurseShiftAllocationDto nurseShiftAllocationDto) {
		try {
			return iNurseShiftAllocationdao.updateNurseShiftAllocationDashboardStatus(nurseShiftAllocationDto);
	} catch (Exception e) {
		logger.error("updateNurseShiftAllocationDashboardStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

	@Override
	public Response getAssignedBedList(NurseShiftAllocationDto nurseShiftAllocationDto) {
		try {
			return iNurseShiftAllocationdao.getAssignedBedList(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getAssignedBedList failed", e);
			return new Response<>(ERROR,ERR_500,ERR_500,null,null);
		}

	}

}
