package com.param.global.service;

import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IOrderDetailsDiscountDao;
import com.param.global.dto.OrderDetailsDiscountDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class OrderDetailsDiscountServiceImpl implements IOrderDetailsDiscountService,ICommonConstants{

	final static Logger logger = Logger.getLogger(OrderDetailsDiscountServiceImpl.class);

	@Autowired
	IOrderDetailsDiscountDao iOrderDetailsDiscountDao; 
	
	@Override
	//@Transactional
	public Response saveCancelledDiscounts(List<OrderDetailsDiscountDto> cancelledDiscountsList)
			throws ApplicationException {
		try {
			
			ListIterator<OrderDetailsDiscountDto> iterator = cancelledDiscountsList.listIterator();
			while(iterator.hasNext())
			{
				OrderDetailsDiscountDto orderDetailsDiscountDto = iterator.next();
					
				iOrderDetailsDiscountDao.saveOrderDetailsDiscount(orderDetailsDiscountDto);
				
				OrderDetailsDiscountDto detailsDiscountDto = new OrderDetailsDiscountDto();
					detailsDiscountDto=orderDetailsDiscountDto;
					detailsDiscountDto.setOldDetailsDiscountId(orderDetailsDiscountDto.getOrderDetailsDiscountId());
					detailsDiscountDto.setOrderDetailsDiscountId(null);
					detailsDiscountDto.setDiscountAmount(orderDetailsDiscountDto.getDiscountAmount().negate());
					detailsDiscountDto.setCreatedBy(detailsDiscountDto.getUpdatedBy());
					detailsDiscountDto.setCreatedDate(detailsDiscountDto.getUpdatedDate());
				iOrderDetailsDiscountDao.saveOrderDetailsDiscount(detailsDiscountDto);
			}
			
			return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			logger.error("saveCancelledDiscounts failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getOrderDetailsDiscountByOrderDetailsId(Integer orderDetailsId) throws ApplicationException {
		try {
			return iOrderDetailsDiscountDao.getOrderDetailsDiscountByOrderDetailsId(orderDetailsId);
		} catch (Exception e) {
			logger.error("getOrderDetailsDiscountByOrderDetailsId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
