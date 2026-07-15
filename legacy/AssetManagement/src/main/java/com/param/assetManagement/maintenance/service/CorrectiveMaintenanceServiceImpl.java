package com.param.assetManagement.maintenance.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceDao;
import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceWorklistDao;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceDto;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class CorrectiveMaintenanceServiceImpl  implements ICorrectiveMaintenanceService,ICommonConstants,IBillingCommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceServiceImpl.class);

	@Autowired
	private ICorrectiveMaintenanceDao iCorrectiveMaintenanceDao;
	
	@Autowired
	private ICorrectiveMaintenanceWorklistDao iCorrectiveMaintenanceWorklistDao;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	public Response saveCorrectiveMaintenance(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			Response res = iCorrectiveMaintenanceDao.checkForUniqueItemRequest(correctiveMaintenanceDto);
			if(res.getObject()!=null && correctiveMaintenanceDto.getCorrectiveMaintenanceId() == null) {
				return res;
			}else {
			if(correctiveMaintenanceDto.getCorrectiveMaintenanceId() == null) {
				String callLogNumber = iGlobalUnitMasterDao.getGeneratedNo(0, correctiveMaintenanceDto.getUnitId(), CALL_LOG_NUMBER);
				correctiveMaintenanceDto.setCallLogNumber(callLogNumber);
			}
			Response resp =  iCorrectiveMaintenanceDao.saveCorrectiveMaintenance(correctiveMaintenanceDto);
				
				if(resp.getObject()!=null ) {
					
					if(correctiveMaintenanceDto.getCorrectiveMaintenanceId() == null && correctiveMaintenanceDto.getReferenceId() !=null) {
						
						return iCorrectiveMaintenanceDao.updateReopen(correctiveMaintenanceDto.getReferenceId());
						
					}
				
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
		} catch (Exception e) {
			logger.error("Error while saving corrective maintenance", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCorrectiveMaintenanceListByMultipleCriteria(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iCorrectiveMaintenanceDao.getCorrectiveMaintenanceListByMultipleCriteria(correctiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error while getting corrective maintenance list by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getSerialNoByItemId(Integer itemId, Integer unitId, Integer organizationId)
			throws ApplicationException {
		try {
			return iCorrectiveMaintenanceDao.getSerialNoByItemId(itemId, unitId, organizationId);
		} catch (Exception e) {
			logger.error("Error while getting serial no by item id", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response ExportedToCorrectiveMaintenanceWorklist(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iCorrectiveMaintenanceDao.ExportedToCorrectiveMaintenanceWorklist(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while exporting to corrective maintenance worklist", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response CorrectiveMaintenanceRequestCompleteByRequester(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			return iCorrectiveMaintenanceDao.CorrectiveMaintenanceRequestCompleteByRequester(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while completing corrective maintenance request by requester", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCorrectiveMaintenancePendingToNewRequest(List<CorrectiveMaintenanceDto> correctiveMaintenanceDto) throws ApplicationException {
		try {
			
				for (CorrectiveMaintenanceDto dto : correctiveMaintenanceDto) {
						iCorrectiveMaintenanceDao.updateCorrectiveMaintenancePendingToNewRequest(dto);
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("Error while updating corrective maintenance pending to new request", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response cancelCorrectiveMaintenanceRequest(CorrectiveMaintenanceDto correctiveMaintenanceDto)
			throws ApplicationException {
		try {
			 iCorrectiveMaintenanceWorklistDao.cancelCorrectiveMaintenanceRequestFromWorklist(correctiveMaintenanceDto);
			return iCorrectiveMaintenanceDao.cancelCorrectiveMaintenanceRequest(correctiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error while cancelling corrective maintenance request", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
    @Override
    public Response getPMSDetailsByItemId(CorrectiveMaintenanceDto correctiveMaintenanceDto)
            throws ApplicationException {
        try {
            Response manufacturerRes = iCorrectiveMaintenanceWorklistDao.getManufacturerDetailsByItemId(correctiveMaintenanceDto);
            Response vendorRes =  iCorrectiveMaintenanceWorklistDao.getVendorDetailsByItemId(correctiveMaintenanceDto);
            Response accessoryRes =  iCorrectiveMaintenanceWorklistDao.getAccessoryDetailsByItemId(correctiveMaintenanceDto);
            Response insuranceRes =  iCorrectiveMaintenanceWorklistDao.getInsuranceDetailsByItemId(correctiveMaintenanceDto);
            
            correctiveMaintenanceDto.setManufacturerList(manufacturerRes.getListObject());
            correctiveMaintenanceDto.setVendorList(vendorRes.getListObject());
            correctiveMaintenanceDto.setAccessoryList(accessoryRes.getListObject());
            correctiveMaintenanceDto.setInsuranceList(insuranceRes.getListObject());
            return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, correctiveMaintenanceDto);
        } catch (Exception e) {
            logger.error("Error while getting PMS details by item id", e);
            throw new ServiceException(e.getMessage());
        }
    }
}
