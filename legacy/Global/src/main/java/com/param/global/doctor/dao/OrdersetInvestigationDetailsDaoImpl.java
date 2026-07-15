package com.param.global.doctor.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.param.doctor.model.OrdersetInvestigationDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OrdersetInvestigationDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OrdersetInvestigationDetailsDaoImpl extends GenericDao<OrdersetInvestigationDetails, Integer> implements IOrdersetInvestigationDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(OrdersetInvestigationDetailsDaoImpl.class);


	public OrdersetInvestigationDetailsDaoImpl(){
		super(OrdersetInvestigationDetails.class);
	}

	
	@Override
	public Response saveOrdersetInvestigationDetails(OrdersetInvestigationDetailsDto dto) throws ApplicationException {
		try{
			OrdersetInvestigationDetails details = new OrdersetInvestigationDetails();
			details.setCreatedBy(dto.getCreatedBy());
			details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details.setOrdersetMasterId(dto.getOrdersetMasterId());
			details.setOrgId(dto.getOrgId());
			details.setServiceId(dto.getServiceId());
			details.setSpecialityId(dto.getSpecialityId());
			details.setSubSpecialityId(dto.getSubSpecialityId());
			details.setUnitId(dto.getUnitId());
			details.setIsDeleted('N');
			
			details = save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, details);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response updateOrdersetInvestigationDetails(OrdersetInvestigationDetailsDto dto, int oldOrdersetMasterId) throws ApplicationException {
		 try {
			if(dto.getOrdersetInvestigationDetailsId() > 0) {
			 Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_ORDER_SET_INVESTIGATION_DETAILS")
						.setParameter("isDeleted", 'Y')
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("ordersetMasterId", oldOrdersetMasterId)
						.executeUpdate();
				if (result > 0) {
						OrdersetInvestigationDetails details = new OrdersetInvestigationDetails();
						details.setCreatedBy(dto.getCreatedBy());
						details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
						details.setOrdersetMasterId(dto.getOrdersetMasterId());
						details.setOrgId(dto.getOrgId());
						details.setServiceId(dto.getServiceId());
						details.setSpecialityId(dto.getSpecialityId());
						details.setSubSpecialityId(dto.getSubSpecialityId());
						details.setUnitId(dto.getUnitId());
						details.setIsDeleted('N');
						details = save(details);
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, details);

					}
			}else{
						OrdersetInvestigationDetails details = new OrdersetInvestigationDetails();
						details.setCreatedBy(dto.getCreatedBy());
						details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
						details.setOrdersetMasterId(dto.getOrdersetMasterId());
						details.setOrgId(dto.getOrgId());
						details.setServiceId(dto.getServiceId());
						details.setSpecialityId(dto.getSpecialityId());
						details.setSubSpecialityId(dto.getSubSpecialityId());
						details.setUnitId(dto.getUnitId());
						details.setIsDeleted('N');
						
						details = save(details);
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, details);
					}
				} catch (HibernateException he) {
				logger.error("updateOrdersetInvestigationDetails failed", he);
				} catch (Exception e) {
				logger.error("updateOrdersetInvestigationDetails failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


	private OrdersetInvestigationDetails saveOrUpdate(OrdersetInvestigationDetails details) {
		// TODO Auto-generated method stub
		return null;
	}
	
}