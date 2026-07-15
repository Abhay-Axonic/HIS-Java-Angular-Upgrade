package com.param.billing.unit.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.billing.unit.dto.TPeriodicityServiceDetailsDto;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TPeriodicityServiceOrdersDaoImpl extends GenericDao<TPeriodicityServiceOrders, Integer> implements ITPeriodicityServiceOrdersDao, ICommonConstants{

	public TPeriodicityServiceOrdersDaoImpl() {
		super(TPeriodicityServiceOrders.class);
	}
	
	@Override
	public Response saveTPeriodicityServiceOrders(TPeriodicityServiceOrdersDto dto) throws BillingException {
		try{
			TPeriodicityServiceOrders order = new TPeriodicityServiceOrders();
				order.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				order.setFromDate((dto.getFromDate() != null && !dto.getFromDate().isEmpty()) ? GlobalCommonDateUtils.getDate(dto.getFromDate(), "dd-MM-yyyy") : null);
				order.setToDate((dto.getToDate() != null && !dto.getToDate().isEmpty()) ? GlobalCommonDateUtils.getDate(dto.getToDate(), "dd-MM-yyyy") : null);
				order.setStatus(dto.getStatus());
				order.setAdmissionId(dto.getAdmissionId());
				order.setBedId(dto.getBedId());
				order.setCreatedBy(dto.getCreatedBy());
				order.setFromTime(dto.getFromTime());
				order.setToTime(dto.getToTime());
				order.setLastUpdation((dto.getLastUpdation() != null && !dto.getLastUpdation().isEmpty()) ? GlobalCommonDateUtils.getDate(dto.getLastUpdation(), "dd-MM-yyyy HH:mm:ss") : null);
				order.setNoOfUnit(dto.getNoOfUnit());
				order.setOrderDetailId(dto.getOrderDetailId());
				order.setOrganisationId(dto.getOrganisationId());
				order.setPatientId(dto.getPatientId());
				order.setPeriodicityServiceOrderId(dto.getPeriodicityServiceOrderId());
				order.setServiceId(dto.getServiceId());
				order.setEquipmentId(dto.getEquipmentId());
				order.setUnitId(dto.getUnitId());
				order.setUpdatedBy(dto.getUpdatedBy());
				order.setVisitTypeId(dto.getVisitTypeId());
				order.setWardId(dto.getWardId());
				order.setOrderStatusId(dto.getOrderStatusId());
				order.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			order = save(order);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTPeriodicityServiceOrderForBatchProcessing(Date toDate, int startPoint, int maxResults)throws BillingException {
		StatelessSession session = null;
		try{
			toDate = GlobalCommonDateUtils.getDate(toDate, "dd-M-yyyy");
			session = sessionFactory.openStatelessSession();
			List<TPeriodicityServiceOrdersDto> listTPeriodicityServiceOrdersDto = session.getNamedQuery("GET_PERIODICITY_FOR_BATCH_PROCESSING")
							.setParameter("toDate", toDate).setFirstResult(startPoint)
							.setMaxResults(maxResults).setResultTransformer(Transformers.aliasToBean(TPeriodicityServiceOrdersDto.class))
							.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPeriodicityServiceOrdersDto, null);
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}

	@Override
	public Response updateTPeriodicityNoOfUnitsById(int noOfUnits, int id, int orderStatusId) throws BillingException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PERIODICITY_NO_OF_UNITS_BY_ID")
						.setParameter("noOfUnits", noOfUnits)
						.setParameter("id", id).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTPeriodicityServicesByAdmissionId(Integer admissionId)
			throws BillingException {
		StatelessSession session = null;
		try{
			session = sessionFactory.openStatelessSession();
			List<TPeriodicityServiceDetailsDto> listTPeriodicityServiceDetailsDto = session.getNamedQuery("GET_PERIODICITY_SERVICE_ORDERS_BY_ADMISSIONID")
							.setParameter("admissionId",admissionId)
							.setResultTransformer(Transformers.aliasToBean(TPeriodicityServiceDetailsDto.class))
							.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPeriodicityServiceDetailsDto, null);
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}
	
	@Override
	public TPeriodicityServiceOrdersDto getTPeriodicityServicesByOrderDetailsId(Integer orderDetailsId)
			throws BillingException {
		StatelessSession session = null;
		try{
			session = sessionFactory.openStatelessSession();
			TPeriodicityServiceOrdersDto dto = (TPeriodicityServiceOrdersDto)session.getNamedQuery("GET_PERIODICITY_SERVICE_ORDERS_BY_ORDERDETAILS_ID")
							.setParameter("orderDetailId",orderDetailsId)
							.setResultTransformer(Transformers.aliasToBean(TPeriodicityServiceOrdersDto.class))
							.uniqueResult();
			return dto;
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}

	@Override
	public Response getAdmissionByIdForBatchPServicePosting(int admissionId) throws ApplicationException {
		try{
			AdmissionsForBatchProcessDto dto = (AdmissionsForBatchProcessDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ADMISSION_DETAILS_BY_ID_FOR_POSTING_PERIODICSERVICES_THROUGH_BATCH")
						.setParameter("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class))
						.uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response cancelOrderedPeriodicityService(Integer periodicityServiceOrderId,Character status) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE TPeriodicityServiceOrders set status =:status WHERE periodicityServiceOrderId =:periodicityServiceOrderId ")
					.setParameter("status", status)
					.setParameter("periodicityServiceOrderId", periodicityServiceOrderId)
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
}
