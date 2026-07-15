package com.param.billing.unit.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.transaction.config.dto.ChargeConversionDetailsDto;
import com.param.billing.global.transaction.model.ChargeConversionDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeConversionDetailsDaoImpl extends GenericDao<ChargeConversionDetails, Integer> implements IChargeConversionDetailsDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(ChargeConversionDetailsDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public ChargeConversionDetailsDaoImpl() {
		super(ChargeConversionDetails.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response saveChargeConversionDetailsList(ChargeConversionDetails requestDto)
			throws ApplicationException {
		try{
			
			ChargeConversionDetails chargeConversionMst = save(requestDto);
			if (chargeConversionMst != null)
			{
				return new Response(SUCCESS, SUCCESS_CODE, null, null, chargeConversionMst);
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
			
			/*ChargeConversionDetails chargeConversionDetails = new ChargeConversionDetails();
			chargeConversionDetails.setCreatedBy(requestDto.getCreatedBy());
			chargeConversionDetails.setCreatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
			chargeConversionDetails.setOrgId(requestDto.getOrgId());
			chargeConversionDetails.setOrgUnitId(requestDto.getOrgUnitId());
			chargeConversionDetails.setBatchId(requestDto.getBatchId());
			chargeConversionDetails.setBedId(requestDto.getBedId());
			chargeConversionDetails.setBillingClassId(requestDto.getBillingClassId());
			chargeConversionDetails.setConversionMasterId(requestDto.getConversionMasterId());
			chargeConversionDetails.setDrugId(requestDto.getDrugId());
			chargeConversionDetails.setNetAmount(requestDto.getNetAmount());
			chargeConversionDetails.setOrdDiscount(requestDto.getOrdDiscount());
			chargeConversionDetails.setOrdConcession(requestDto.getOrdConcession());
			chargeConversionDetails.setIsOutsourced(requestDto.getIsOutsourced());
			chargeConversionDetails.setPackageId(requestDto.getPackageId());
			chargeConversionDetails.setPriorityId(requestDto.getPriorityId());
			chargeConversionDetails.setIsDrug(requestDto.getIsDrug());
			chargeConversionDetails.setOldOrdDtlId(requestDto.getOldOrdDtlId());
			chargeConversionDetails.setOrdCancelled(requestDto.getOrdCancelled());
			chargeConversionDetails.setOrdCancelDatetime(requestDto.getOrdCancelDatetime());
			chargeConversionDetails.setOrderQty(requestDto.getOrderQty());
			chargeConversionDetails.setOrdCancelReasonId(requestDto.getOrdCancelReasonId());
			chargeConversionDetails.setOrdConcession(requestDto.getOrdConcession());
			chargeConversionDetails.setOrdDiscount(requestDto.getOrdDiscount());
			chargeConversionDetails.setOrdDocId(requestDto.getOrdDocId());
			chargeConversionDetails.setTariffId(requestDto.getTariffId());
			chargeConversionDetails.setOrderEmergencyFlag(requestDto.getOrderEmergencyFlag());
			chargeConversionDetails.setOrderDate(requestDto.getOrderDate());
			chargeConversionDetails.setWardId(requestDto.getWardId());
			chargeConversionDetails.setServiceRenderingDeptid(requestDto.getServiceRenderingDeptid());
			chargeConversionDetails.setPriorityId(requestDto.getPriorityId());
			chargeConversionDetails.setRoomId(requestDto.getRoomId());
			chargeConversionDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
			chargeConversionDetails.setUpdatedBy(requestDto.getUpdatedBy());
			chargeConversionDetails.setOrderSchDate(requestDto.getOrderSchDate());
			chargeConversionDetails.setServiceAmt(requestDto.getServiceAmt());
			chargeConversionDetails.setServiceBillId(requestDto.getServiceBillId());
			chargeConversionDetails.setOrdRemarks(requestDto.getOrdRemarks());
			chargeConversionDetails.setServiceChargeable(requestDto.getServiceChargeable());
			chargeConversionDetails.setStatus(ACTIVE);
			chargeConversionDetails.setOrderDetailsId(requestDto.getOrderDetailsId());
			chargeConversionDetails.setServiceRendered(requestDto.getServiceRendered());
			chargeConversionDetails.setServiceChargeable(requestDto.getServiceChargeable());
			chargeConversionDetails.setOrdRenderDatetime(requestDto.getOrdRenderDatetime());
			chargeConversionDetails.setServiceBillId(requestDto.getServiceBillId());
			chargeConversionDetails.setIsAppointableRadiology(requestDto.getIsAppointableRadiology());
			chargeConversionDetails = save(chargeConversionDetails);*/
			//return new Response<>(SUCCESS, SUCCESS_CODE, null, null, chargeConversionDetails);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getOrderDetailsListByOrderId(ChargeConversionDetailsDto chargeConversionMasterDto)
			throws ApplicationException {
		try
		{
			List<ChargeConversionDetailsDto> listChargeConversionDetails= sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORDER_DTLS_LIST_BY_ORDER_DTLS_ID").setParameter("orderDetailsId", chargeConversionMasterDto.getOrderDetailsId())
					.list();
			
			if(listChargeConversionDetails.isEmpty())
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_CODE, null, listChargeConversionDetails, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}



	
}
