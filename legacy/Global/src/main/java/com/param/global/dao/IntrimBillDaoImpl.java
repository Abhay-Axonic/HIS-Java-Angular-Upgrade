package com.param.global.dao;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.IntrimBill;
import com.param.billing.global.transaction.model.IntrimBillDetails;
import com.param.billing.global.transaction.model.IntrimBillPayeeDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.model.OrderDetailsPayeeMapper;

import in.param.exception.ApplicationException;

import java.util.Date;

@Repository
@SuppressWarnings("rawtypes")
public class IntrimBillDaoImpl implements IIntrimBillDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(IntrimBillDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response saveIntrimBill(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
				IntrimBill intrimBill =mapper.map(orderMasterDto, IntrimBill.class, "OrderMasterDto_to_IntrimBill");
				Integer id = (Integer)sessionFactory.getCurrentSession().save(intrimBill);
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);
		}catch (Exception e) {
			logger.error("saveIntrimBill failed", e);
			throw new ServiceException(e.getMessage());
		}

	}


	@Override
	public Response saveIntrimBillDetails(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try {
				IntrimBillDetails intrimBillDetails =mapper.map(orderDetailsMasterDto, IntrimBillDetails.class, "OrderDetailsMasterDto_to_IntrimBillDetails");
			intrimBillDetails.setIsCapService(intrimBillDetails.getIsCapService()=='\u0000'?'N':intrimBillDetails.getIsCapService());
			intrimBillDetails.setIsPackageService(intrimBillDetails.getIsPackageService()=='\u0000'?'N':intrimBillDetails.getIsPackageService());
			intrimBillDetails.setIsProcedure(intrimBillDetails.getIsProcedure()=='\u0000'?'N':intrimBillDetails.getIsProcedure());
			intrimBillDetails.setIsProcedureService(intrimBillDetails.getIsProcedureService()=='\u0000'?'N':intrimBillDetails.getIsProcedureService());
				int id = (int)sessionFactory.getCurrentSession().save(intrimBillDetails);
				if(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList()!=null && orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList().size()>0)
				{
					for(OrderDetailsPayeeMapperDto dto : orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList())
					{
						IntrimBillPayeeDetails detailsPayeeMapper = new IntrimBillPayeeDetails();
							detailsPayeeMapper.setIntrimBillDetailsId(id);	
							detailsPayeeMapper.setOrderDetailsId(dto.getOrderDetailsId());
							detailsPayeeMapper.setContractId(dto.getContractId());
							detailsPayeeMapper.setPayeeId(dto.getPayeeId());
							detailsPayeeMapper.setPayeeAmount(dto.getPayeeAmount());
							detailsPayeeMapper.setServiceId(dto.getServiceId());
							detailsPayeeMapper.setOrganizationId(orderDetailsMasterDto.getOrgId());
							detailsPayeeMapper.setUnitId(orderDetailsMasterDto.getOrgUnitId());
							detailsPayeeMapper.setStatus('A');
						sessionFactory.getCurrentSession().save(detailsPayeeMapper);	
					}
				}
				
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
			logger.error("saveIntrimBillDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response inActivePreviosRecords(OrderMasterDto orderMasterDto) {
		try{
			String query = "UPDATE IntrimBill SET orderStatus = :orderStatus, updatedDate = :updatedDate " +
					"WHERE admissionId = :admissionId AND orderStatus = :currentOrderStatus";

			sessionFactory.getCurrentSession()
					.createQuery(query)
					.setParameter("orderStatus", "I")  // Parameter for the new orderStatus
					.setParameter("updatedDate", new Date())  // Parameter for the current timestamp (updatedDate)
					.setParameter("admissionId", orderMasterDto.getAdmissionId())  // Parameter for admissionId
					.setParameter("currentOrderStatus", "A")  // Parameter for the current orderStatus
					.executeUpdate();

		}catch (Exception e) {
			logger.error("inActivePreviosRecords failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
