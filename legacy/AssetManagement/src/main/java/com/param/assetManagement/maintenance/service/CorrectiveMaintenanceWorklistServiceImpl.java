package com.param.assetManagement.maintenance.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceDao;
import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceWorklistDao;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceWorklistDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceWorklist;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes"})
public class CorrectiveMaintenanceWorklistServiceImpl implements ICorrectiveMaintenanceWorklistService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceWorklistServiceImpl.class);

	@Autowired
	ICorrectiveMaintenanceWorklistDao  iCorrectiveMaintenanceWorklistDao;
	
	@Autowired
	ICorrectiveMaintenanceDao  iCorrectiveMaintenanceDao;

	@Override
	public Response saveCorrectiveMaintenanceWorklist(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto)
			throws ApplicationException {
		try {
			
			Response res =  iCorrectiveMaintenanceWorklistDao.saveCorrectiveMaintenanceWorklist(correctiveMaintenanceWorklistDto);
				if(res.getStatus().equals(SUCCESS)) {
					if(res.getObject()!=null) {
						CorrectiveMaintenanceWorklist mstr = (CorrectiveMaintenanceWorklist)res.getObject();
						CorrectiveMaintenanceDto dto = new CorrectiveMaintenanceDto();
						dto.setCorrectiveMaintenanceId(mstr.getCorrectiveMaintenanceId());
						dto.setMaintenanceStatusId(correctiveMaintenanceWorklistDto.getMaintenanceWorklistStatusId()); // 3 for Assigned
						iCorrectiveMaintenanceDao.ExportedToCorrectiveMaintenanceWorklist(dto);
					}
				}
			 return res;
		} catch (Exception e) {
			logger.error("Error saving corrective maintenance worklist", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCorrectiveMaintenanceWorklistListByMultipleCriteria(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto) throws ApplicationException {
		try {
			
			return iCorrectiveMaintenanceWorklistDao.getCorrectiveMaintenanceWorklistListByMultipleCriteria(correctiveMaintenanceWorklistDto);

		} catch (Exception e) {
			logger.error("Error fetching corrective maintenance worklist list by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
