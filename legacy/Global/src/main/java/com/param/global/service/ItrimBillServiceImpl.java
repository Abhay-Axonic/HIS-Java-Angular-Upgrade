package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IIntrimBillDao;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class ItrimBillServiceImpl implements IItrimBillService,ICommonConstants {

	final static Logger logger = Logger.getLogger(ItrimBillServiceImpl.class);

	@Autowired
	IIntrimBillDao iIntrimBillDao;
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	public Response saveIntrimBill(OrderMasterDto orderMasterDto) throws ApplicationException {
		try{
			
			iIntrimBillDao.inActivePreviosRecords(orderMasterDto);
			
			orderMasterDto.setOrderStatus('A');
			orderMasterDto.setIntrimBillNo(getIntrimBillNo(orderMasterDto.getOrgUnitId()));
			Response res = iIntrimBillDao.saveIntrimBill(orderMasterDto);
			
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null && orderMasterDto.getListOrderDetailsMasterDto() != null) 
			{
				for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto()) 
				{
						orderDetailsMasterDto.setIntrimBillId((Integer)res.getObject());
						orderDetailsMasterDto.setStatus((orderDetailsMasterDto.getStatus()!=null)?orderDetailsMasterDto.getStatus():'A');
						orderDetailsMasterDto.setIsAppointableRadiology(orderDetailsMasterDto.getIsAppointableRadiology()!=null ? orderDetailsMasterDto.getIsAppointableRadiology():'N');
						orderDetailsMasterDto.setIsProcedure(orderDetailsMasterDto.getIsProcedure()!='\0' ? orderDetailsMasterDto.getIsProcedure() : 'N');
						orderDetailsMasterDto.setIsCapService(orderDetailsMasterDto.getIsCapService()!='\0' ? orderDetailsMasterDto.getIsCapService() :'N');
						orderDetailsMasterDto.setIsProcedureService(orderDetailsMasterDto.getIsProcedureService()!='\0' ? orderDetailsMasterDto.getIsProcedureService() : 'N');
						orderDetailsMasterDto.setIsPackageService(orderDetailsMasterDto.getIsPackageService()!='\0'? orderDetailsMasterDto.getIsPackageService() : 'N');
						orderDetailsMasterDto.setDocSharePer(null);
					iIntrimBillDao.saveIntrimBillDetails(orderDetailsMasterDto);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res);
			}

		}catch(Exception e){
			logger.error("saveIntrimBill failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	private String getIntrimBillNo(Integer unitId) {
		try {
				return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.INTRIM_BILL_NUMBER);
			} catch (Exception e) {
				logger.error("getIntrimBillNo failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	

}
