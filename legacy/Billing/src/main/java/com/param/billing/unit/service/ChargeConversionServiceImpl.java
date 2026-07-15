package com.param.billing.unit.service;

import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.config.dto.ChargeConversionDetailsDto;
import com.param.billing.global.transaction.config.dto.ChargeConversionMasterDto;
import com.param.billing.global.transaction.model.ChargeConversionDetails;
import com.param.billing.global.transaction.model.ChargeConversionMaster;
import com.param.billing.unit.dao.IChargeConversionDetailsDao;
import com.param.billing.unit.dao.IChargeConversionMasterDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.service.dto.ChargeConversionReqDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeConversionServiceImpl implements IChargeConversionService, ICommonConstants {


	@Autowired
	IChargeConversionMasterDao iChargeConversionMasterDao;
	
	@Autowired
	IChargeConversionDetailsDao iChargeConversionDetailsDao;
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Transactional
	@Override
	public Response saveChargeConversionMasterList(ChargeConversionReqDto reqDto) throws ApplicationException {
		try
		{
			ChargeConversionMasterDto chargeConversionDto = reqDto.getChargeConversionMasterDto();
			if(chargeConversionDto != null){
				//save charge conversion master
				chargeConversionDto.setOrgId(reqDto.getOrgId());
				chargeConversionDto.setOrgUnitId(reqDto.getUnitId());
				Response chargeConversionRes = iChargeConversionMasterDao.saveChargeConversionMasterList(chargeConversionDto);
				if(chargeConversionRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && chargeConversionRes.getObject() != null){
					ChargeConversionMaster chargeConversionMaster = (ChargeConversionMaster)chargeConversionRes.getObject();
					Integer conversionMasterId = chargeConversionMaster.getConversionMasterId();
					
					//save charge conversion details
					if(reqDto.getListChargeConversionDetailsDto()!= null && reqDto.getListChargeConversionDetailsDto().size() > 0){
						ListIterator<ChargeConversionDetailsDto> chargeConversionDtlsItr = reqDto.getListChargeConversionDetailsDto().listIterator();
						ChargeConversionDetailsDto chargeConversionDetailsDto = null;
						while(chargeConversionDtlsItr.hasNext()){
							chargeConversionDetailsDto = chargeConversionDtlsItr.next();
							chargeConversionDetailsDto.setConversionMasterId(conversionMasterId);
							chargeConversionDetailsDto.setOrgId(reqDto.getOrgId());
							chargeConversionDetailsDto.setOrgUnitId(reqDto.getUnitId());
							Response orderDtlsRes =  iChargeConversionDetailsDao.getOrderDetailsListByOrderId(chargeConversionDetailsDto);
							if(orderDtlsRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && orderDtlsRes.getListObject() != null)
							{
								
								List<OrderDetailsMaster> listResssDto= orderDtlsRes.getListObject();
								for(OrderDetailsMaster ressDto : listResssDto){
									//save complete order details log
									ChargeConversionDetails  chargeConversionDetails = mapper.map(ressDto, ChargeConversionDetails.class,
											"OrderDetailsMasterTOChargeConversionDetails");
									     chargeConversionDetails.setConversionMasterId(conversionMasterId);
										 chargeConversionDetails.setOrgId(reqDto.getOrgId());
										 chargeConversionDetails.setOrgUnitId(reqDto.getUnitId());
										iChargeConversionDetailsDao.saveChargeConversionDetailsList(chargeConversionDetails);
								}
							}
							// update Service Rate in OrderDtlsMst
							OrderDetailsMasterDto orderDtlsMstDto= new OrderDetailsMasterDto();
							orderDtlsMstDto.setOrderDetailsId(chargeConversionDetailsDto.getOrderDetailsId());
							orderDtlsMstDto.setServiceAmt(chargeConversionDetailsDto.getConvertAmt());
							iOrderDetailsMasterDao.updateOrderDtlsByOrderDtlsId(orderDtlsMstDto);
							
						}
					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, CHARGE_CONVERSION_SAVE, null, null);
			}else 
			{
				return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
			}
		} catch (Exception e)
		{
		throw new ApplicationException(e.getMessage(), e);
	}
	}

}
