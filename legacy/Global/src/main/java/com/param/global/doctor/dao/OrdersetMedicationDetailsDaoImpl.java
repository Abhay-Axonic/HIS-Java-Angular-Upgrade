package com.param.global.doctor.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.param.doctor.model.OrdersetMedicationDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OrdersetMedicationDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OrdersetMedicationDetailsDaoImpl extends GenericDao<OrdersetMedicationDetails, Integer> implements IOrdersetMedicationDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(OrdersetMedicationDetailsDaoImpl.class);


	public OrdersetMedicationDetailsDaoImpl() {
		super(OrdersetMedicationDetails.class);
	}

	
	@Override
	public Response saveOrdersetMedicationDetails(OrdersetMedicationDetailsDto dto) throws ApplicationException {
		try{
			OrdersetMedicationDetails details = new OrdersetMedicationDetails();
			details.setCreatedBy(dto.getCreatedBy());
			details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details.setDose(dto.getDose());
			details.setFrequencyId(dto.getFrequencyId());
			details.setInstructionId(dto.getInstructionId());
			details.setItemMasterId(dto.getItemMasterId());
			details.setOrdersetMasterId(dto.getOrdersetMasterId());
			details.setOrgId(dto.getOrgId());
			details.setUnitId(dto.getUnitId());
			details.setDoseUnitId(dto.getDoseUnitId());
			details.setRouteId(dto.getRouteId());
			details.setIsDeleted('N');
			details = save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, details);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response updateOrdersetMedicationDetails(OrdersetMedicationDetailsDto dto, int oldOrdersetMasterId) throws ApplicationException {
		 try {
				if(dto.getOrdersetMedicationDetailsId() > 0) {
					 Integer result = sessionFactory.getCurrentSession()
								.getNamedQuery("UPDATE_ORDER_SET_MEDICATION_DETAILS")
								.setParameter("isDeleted", 'Y')
								.setParameter("orgId", dto.getOrgId())
								.setParameter("unitId", dto.getUnitId())
								.setParameter("ordersetMasterId", oldOrdersetMasterId)
								.executeUpdate();
						if (result > 0) {
							OrdersetMedicationDetails details = new OrdersetMedicationDetails();
							details.setCreatedBy(dto.getCreatedBy());
							details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
							details.setDose(dto.getDose());
							details.setFrequencyId(dto.getFrequencyId());
							details.setInstructionId(dto.getInstructionId());
							details.setItemMasterId(dto.getItemMasterId());
							details.setOrdersetMasterId(dto.getOrdersetMasterId());
							details.setOrgId(dto.getOrgId());
							details.setUnitId(dto.getUnitId());
							details.setDoseUnitId(dto.getDoseUnitId());
							details.setRouteId(dto.getRouteId());
							details.setIsDeleted('N');
							details = saveOrUpdate(details);
							return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, details);
							}
				}
				else {
					OrdersetMedicationDetails details = new OrdersetMedicationDetails();
					details.setCreatedBy(dto.getCreatedBy());
					details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					details.setDose(dto.getDose());
					details.setFrequencyId(dto.getFrequencyId());
					details.setInstructionId(dto.getInstructionId());
					details.setItemMasterId(dto.getItemMasterId());
					details.setOrdersetMasterId(dto.getOrdersetMasterId());
					details.setOrgId(dto.getOrgId());
					details.setUnitId(dto.getUnitId());
					details.setDoseUnitId(dto.getDoseUnitId());
					details.setRouteId(dto.getRouteId());
					details.setIsDeleted('N');
					details = save(details);
					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, details);
				}
					
				
				
				
				} catch (HibernateException he) {
				logger.error("updateOrdersetMedicationDetails failed", he);
				} catch (Exception e) {
				logger.error("updateOrdersetMedicationDetails failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


	private OrdersetMedicationDetails saveOrUpdate(OrdersetMedicationDetails details) {
		// TODO Auto-generated method stub
		return null;
	}

}
