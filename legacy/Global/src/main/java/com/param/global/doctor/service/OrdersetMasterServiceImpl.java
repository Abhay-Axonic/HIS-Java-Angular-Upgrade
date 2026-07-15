package com.param.global.doctor.service;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.doctor.model.OrdersetMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dao.IOrdersetInvestigationDetailsDao;
import com.param.global.doctor.dao.IOrdersetMasterDao;
import com.param.global.doctor.dao.IOrdersetMedicationDetailsDao;
import com.param.global.doctor.dto.OrderSetReqDto;
import com.param.global.doctor.dto.OrdersetCreationReqDto;
import com.param.global.doctor.dto.OrdersetInvestigationDetailsDto;
import com.param.global.doctor.dto.OrdersetMasterDto;
import com.param.global.doctor.dto.OrdersetMedicationDetailsDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OrdersetMasterServiceImpl implements IOrdersetMasterService, ICommonConstants{

	@Autowired
	private IOrdersetMasterDao iOrdersetMasterDao;
	@Autowired
	private IOrdersetMedicationDetailsDao iOrdersetMedicationDetailsDao;
	@Autowired
	private IOrdersetInvestigationDetailsDao iOrdersetInvestigationDetailsDao;
	
	@Override
	@Transactional
	public Response createOrderset(OrdersetCreationReqDto reqDto) throws ApplicationException {
		try{
			//save order set master
			if(reqDto.getOrdersetMaster() != null){
				OrdersetMasterDto ordersetDto = reqDto.getOrdersetMaster();

				Response getOrdersetByOrdersetName = iOrdersetMasterDao.checkOrderset(ordersetDto);

				if(Objects.equals(getOrdersetByOrdersetName.getObject(),false)) {

					Response ordersetRes = iOrdersetMasterDao.saveOrderset(ordersetDto);
					if (ordersetRes.getCode().equals(SUCCESS_CODE) && ordersetRes.getObject() != null) {
						OrdersetMaster orderset = (OrdersetMaster) ordersetRes.getObject();

						//save order set medications
						if (reqDto.getOrdersetMedications() != null && reqDto.getOrdersetMedications().size() > 0) {
							ListIterator<OrdersetMedicationDetailsDto> ordersetMedItr = reqDto.getOrdersetMedications().listIterator();
							while (ordersetMedItr.hasNext()) {
								OrdersetMedicationDetailsDto ordersetMed = ordersetMedItr.next();
								ordersetMed.setOrdersetMasterId(orderset.getOrdersetMasterId());
								iOrdersetMedicationDetailsDao.saveOrdersetMedicationDetails(ordersetMed);
							}
						}

						//save order set investigations
						if (reqDto.getOrdersetInvestigations() != null && reqDto.getOrdersetInvestigations().size() > 0) {
							ListIterator<OrdersetInvestigationDetailsDto> ordersetInvItr = reqDto.getOrdersetInvestigations().listIterator();
							while (ordersetInvItr.hasNext()) {
								OrdersetInvestigationDetailsDto ordersetInvDto = ordersetInvItr.next();
								ordersetInvDto.setOrdersetMasterId(orderset.getOrdersetMasterId());
								iOrdersetInvestigationDetailsDao.saveOrdersetInvestigationDetails(ordersetInvDto);
							}
						}
						return new Response<>(SUCCESS, SUCCESS_CODE, ORDER_SET_CREATED, null, null);
					}
				}else {
					return new Response<>(ERROR, NOT_FOUND_CODE, getOrdersetByOrdersetName.getMessage(), null, null);
				}
			}
			return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOrderSet(OrderSetReqDto reqDto) throws ApplicationException {
		try{
			return iOrdersetMasterDao.getOrderSet(reqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOrderSetOrders(List<OrderSetReqDto> reqDto) throws ApplicationException {
		try{
			List<OrdersetCreationReqDto> ordersetCreationReqDtoList = new ArrayList<OrdersetCreationReqDto>();
			
			
			for (OrderSetReqDto dto : reqDto) {
				Response orderSetRes =  iOrdersetMasterDao.getOrderSetOrders(dto);
				if(orderSetRes.getCode().equals(SUCCESS_CODE) && orderSetRes.getListObject() != null)
				{
					List<OrdersetMasterDto> list = orderSetRes.getListObject();
				  for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					
					  OrdersetMasterDto ordersetMasterDto = (OrdersetMasterDto) iterator.next();
					
					  OrdersetCreationReqDto ordersetCreationReqDto = new OrdersetCreationReqDto();
					  ordersetCreationReqDto.setOrdersetMaster(ordersetMasterDto);
					  
					if(orderSetRes.getCode().equals(SUCCESS_CODE) && orderSetRes.getListObject() != null)
					{
						List<OrdersetInvestigationDetailsDto> investigation = this.getInvestigationByOrderSet(ordersetMasterDto).getListObject();
						List<OrdersetMedicationDetailsDto> medication = this.getMedicationByOrderset(ordersetMasterDto).getListObject();
						ordersetCreationReqDto.setOrdersetInvestigations(investigation);
						ordersetCreationReqDto.setOrdersetMedications(medication);
					}	
					ordersetCreationReqDtoList.add(ordersetCreationReqDto);
				  }
				 }
			}
			  return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, ordersetCreationReqDtoList, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getInvestigationByOrderSet(OrdersetMasterDto reqDto) throws ApplicationException {
		try{
			return iOrdersetMasterDao.getInvestigationByOrderSet(reqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMedicationByOrderset(OrdersetMasterDto reqDto) throws ApplicationException {
		try{
			return iOrdersetMasterDao.getMedicationByOrderset(reqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOrderSetsList(OrdersetMasterDto dto) throws ApplicationException {
		try{
			return iOrdersetMasterDao.getOrderSetsList(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOrderSetsListCount(OrdersetMasterDto dto) throws ApplicationException {
		try{
			return iOrdersetMasterDao.getOrderSetsListCount(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response orderSetsDetails(OrdersetMasterDto dto) throws ApplicationException {
		try{
			return iOrdersetMasterDao.orderSetsDetails(dto);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response changeStatusOrderSet(Integer ordersetMasterId, Character status) throws ApplicationException {
		try{
			return iOrdersetMasterDao.changeStatusOrderSet(ordersetMasterId,status);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response orderSetSearch(int unitId, String searchkeyWord,int orderSetActiveTab) throws ApplicationException {
		try{
			return iOrdersetMasterDao.orderSetSearch(unitId,searchkeyWord,orderSetActiveTab);
		}catch(Exception e)
		{
			
			throw e;
		}
	}

	@Override
	public Response updateOrderSetsDetails(OrdersetCreationReqDto reqDto) throws ApplicationException {
		try{
			//save order set master
			if(reqDto.getOrdersetMaster() != null){
				OrdersetMasterDto ordersetDto = reqDto.getOrdersetMaster();
				int oldOrdersetMasterId= reqDto.getOrdersetMaster().getOrdersetMasterId();
				Response getOrdersetByOrdersetName = iOrdersetMasterDao.checkOrderset(ordersetDto);

				if(getOrdersetByOrdersetName.getListObject().size()<=1 ) {
					Response ordersetRes = iOrdersetMasterDao.updateOrderSetsDetails(ordersetDto);
					if (ordersetRes.getCode().equals(SUCCESS_CODE) && ordersetRes.getObject() != null) {
						OrdersetMaster orderset = (OrdersetMaster) ordersetRes.getObject();
						if (orderset != null) {
							//save order set medications
							if (reqDto.getOrdersetMedications() != null && reqDto.getOrdersetMedications().size() > 0) {
								ListIterator<OrdersetMedicationDetailsDto> ordersetMedItr = reqDto.getOrdersetMedications().listIterator();
								while (ordersetMedItr.hasNext()) {
									OrdersetMedicationDetailsDto ordersetMed = ordersetMedItr.next();
									ordersetMed.setOrdersetMasterId(orderset.getOrdersetMasterId());
									iOrdersetMedicationDetailsDao.updateOrdersetMedicationDetails(ordersetMed, oldOrdersetMasterId);
								}
							}
							//save order set investigations
							if (reqDto.getOrdersetInvestigations() != null && reqDto.getOrdersetInvestigations().size() > 0) {
								ListIterator<OrdersetInvestigationDetailsDto> ordersetInvItr = reqDto.getOrdersetInvestigations().listIterator();
								while (ordersetInvItr.hasNext()) {
									OrdersetInvestigationDetailsDto ordersetInvDto = ordersetInvItr.next();
									ordersetInvDto.setOrdersetMasterId(orderset.getOrdersetMasterId());
									iOrdersetInvestigationDetailsDao.updateOrdersetInvestigationDetails(ordersetInvDto, oldOrdersetMasterId);
								}
							}
						}
						return new Response<>(SUCCESS, SUCCESS_CODE, "Order set updated..!!", null, null);

					}
				}else{
					return new Response<>(ERROR, NOT_FOUND_CODE, getOrdersetByOrdersetName.getMessage(), null, null);
				}
			}else {
				
			}
			return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
		}catch(Exception e){
			throw e;
		}
	}

}
