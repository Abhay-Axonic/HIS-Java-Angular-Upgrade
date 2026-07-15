package com.param.billing.global.transaction.dao;

import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsDto;
import com.param.billing.global.transaction.model.TDoctorShareSplitLogs;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings("rawtypes")
public class TDoctorShareSplitDaoImpl extends GenericDao<TDoctorShareSplitLogs, Integer> implements IDoctorShareSplitDao, ICommonConstants{

	public TDoctorShareSplitDaoImpl() {
		super(TDoctorShareSplitLogs.class);
	}

	@Override
	public Response saveDoctorShareSplit(TDoctorShareSplitLogsDto masterDto)
			throws ServiceException {
		try{
			TDoctorShareSplitLogs tDoctorShareSplitLogs = new TDoctorShareSplitLogs();
				tDoctorShareSplitLogs.setCreatedBy(masterDto.getCreatedBy());
				tDoctorShareSplitLogs.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
				tDoctorShareSplitLogs.setOrgId(masterDto.getOrgId());
				tDoctorShareSplitLogs.setDoctorShareId(masterDto.getDoctorShareId());
				tDoctorShareSplitLogs.setStatus(ACTIVE);
				tDoctorShareSplitLogs.setDocId(masterDto.getDocId());
				tDoctorShareSplitLogs.setOrgUnitId(masterDto.getOrgUnitId());
				tDoctorShareSplitLogs.setUpdatedBy(masterDto.getUpdatedBy());
				tDoctorShareSplitLogs.setNewSharePercentage(masterDto.getNewSharePercentage());
				tDoctorShareSplitLogs.setShareAmount(masterDto.getShareAmount());
				tDoctorShareSplitLogs.setPriority(masterDto.getPriority());
				tDoctorShareSplitLogs.setParentDocShareId(masterDto.getParentDocShareId());
			tDoctorShareSplitLogs = save(tDoctorShareSplitLogs);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDoctorShareSplitLogs);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorShareSplit(TDoctorShareSplitLogs masterDto)
			throws ServiceException {
		try{
			masterDto.setStatus('A');
			masterDto = update(masterDto);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, masterDto);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response findByIdDoctorShareSplit(Integer id)throws ServiceException {
		try{
			TDoctorShareSplitLogs tDoctorShareSplitLogs = findById(id);
			if (tDoctorShareSplitLogs.getDoctorShareId() != 0)
			{
		     	return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDoctorShareSplitLogs);
			}else {
		     	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, tDoctorShareSplitLogs);
			}
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	

}
