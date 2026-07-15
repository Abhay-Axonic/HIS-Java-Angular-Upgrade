package com.param.adt.discharge.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.param.adt.discharge.dao.IDischargeSummaryFieldDao;
import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
@Service
@SuppressWarnings({"rawtypes"})
public class DischargeSummaryFieldServiceImpl implements IDischargeSummaryFieldService, ICommonConstants{

	@Autowired
	IDischargeSummaryFieldDao DischargeSummaryFieldDao;
	@Override
	public Response saveDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		Response res=DischargeSummaryFieldDao.getDischargeSummaryFieldByName(dischargeSummaryFieldDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return DischargeSummaryFieldDao.saveDischargeSummaryField(dischargeSummaryFieldDto);
			}

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		Response res=DischargeSummaryFieldDao.getDischargeSummaryFieldByNameNotId(dischargeSummaryFieldDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return DischargeSummaryFieldDao.updateDischargeSummaryField(dischargeSummaryFieldDto);
			}

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeSummaryFieldStatus(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.updateDischargeSummaryFieldStatus(dischargeSummaryFieldDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDischargeSummaryFieldById(Integer dischargeFieldId) {
		try {
			return DischargeSummaryFieldDao.getDischargeSummaryFieldById(dischargeFieldId);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeSummaryFieldMasterList(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.getDischargeSummaryFieldMasterList(dischargeSummaryFieldDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeSummaryFieldCount(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.getDischargeSummaryFieldCount(dischargeSummaryFieldDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDischargeSummaryField(Integer organisationId) {
		try {
			return DischargeSummaryFieldDao.getActiveDischargeSummaryField(organisationId);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDischargeSummaryFieldMapper(Integer organisationId) {
		try {
			return DischargeSummaryFieldDao.getActiveDischargeSummaryFieldMapper(organisationId);

		} catch (Exception e) {
			throw e;
		}
	}



	@Override
	public Response getDischargeSumColumnDetails(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.getDischargeSumColumnDetails(dischargeSummaryFieldDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischSumColumnsToPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.updateDischSumColumnsToPrint(dischargeSummaryFieldDto);
			
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischSumColumnsDetailsForPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.getDischSumColumnsDetailsForPrint(dischargeSummaryFieldDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischSumColumnsList(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.getDischSumColumnsList(dischargeSummaryFieldDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

//	@Override
//	public Response updateDischSumColumnsRequired(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
//		try {
//			return DischargeSummaryFieldDao.updateDischSumColumnsRequired(dischargeSummaryFieldDto);
//			
//		} catch (Exception e) {
//			
//		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
//	}

	/*@Override
	public Response updateDischSumColumnsRequired(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return DischargeSummaryFieldDao.updateDischSumColumnsRequired(dischargeSummaryFieldDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
}
