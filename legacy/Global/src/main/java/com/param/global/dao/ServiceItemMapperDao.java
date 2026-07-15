package com.param.global.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.module.service.ServiceItemMapperDto;
import com.param.service.global.ServiceItemMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class ServiceItemMapperDao extends GenericDao<ServiceItemMapper, Integer> implements IServiceItemMapperDao,ICommonConstants,IError
{
	final static Logger logger = Logger.getLogger(ServiceItemMapperDao.class);


	public ServiceItemMapperDao() {
		super(ServiceItemMapper.class);
	}

	
	@Override
	public Response saveServiceItemMapper(List<ServiceItemMapperDto> serviceItemMapperList,Integer serviceId) throws ApplicationException {
		try {
			
			for (ServiceItemMapperDto dto : serviceItemMapperList) {
				
				ServiceItemMapper master =  new ServiceItemMapper();
				master.setItemId(dto.getItemId());
				master.setServiceId(serviceId);
				master.setAssignQty(dto.getAssignQty());
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setOrganizationId(dto.getOrganizationId());
				master.setStatus(ACTIVE);
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveServiceItemMapper failed", he);
		} catch (Exception e) {
		logger.error("saveServiceItemMapper failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	public Response updateServiceItemMapper(List<ServiceItemMapperDto> serviceItemMapperList, Integer serviceMasterId)
			throws ApplicationException {
		try {
			
			for (ServiceItemMapperDto dto : serviceItemMapperList) {
				
				if(dto.getServiceItemMapperId()!=null) {
					
					ServiceItemMapper master = findById(dto.getServiceItemMapperId());
					master.setStatus(dto.getStatus());
					master.setAssignQty(dto.getAssignQty());
					master = update(master);
					
				}else {
				
					ServiceItemMapper master =  new ServiceItemMapper();
					master.setItemId(dto.getItemId());
					master.setServiceId(serviceMasterId);
					master.setAssignQty(dto.getAssignQty());
					master.setCreatedBy(dto.getCreatedBy());
					master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.setOrganizationId(dto.getOrganizationId());
					master.setStatus(ACTIVE);
					master.setUpdatedBy(dto.getUpdatedBy());
					master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master = save(master);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateServiceItemMapper failed", he);
		} catch (Exception e) {
		logger.error("updateServiceItemMapper failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	public Response getServiceItemMapperDetails(ServiceItemMapperDto serviceItemMapperDto) throws ApplicationException {
		try
		{
			List<Integer> serviceIds = new ArrayList();
			if(serviceItemMapperDto.getServiceIds()!=null ) {
				serviceIds.addAll(serviceItemMapperDto.getServiceIds());
			}else {
				serviceIds.add(serviceItemMapperDto.getServiceId());
			}
			List<ServiceItemMapperDto> list =  sessionFactory.getCurrentSession()
					.createSQLQuery(" select seritem.service_item_mapper_id as \"serviceItemMapperId\" ," + 
							" seritem.assign_qty as \"assignQty\" , " + 
							" seritem.item_id as \"itemId\", " +
							" item.code as \"itemCode\","+
							" seritem.service_id as \"serviceId\",  " + 
							" seritem.status as \"status\",  " + 
							" item.name as \"itemName\"  " + 
							" from service.m_service_item_mapper seritem " + 
							" inner join public.m_item item on seritem.item_id = item.id " + 
							" where seritem.service_id in (:serviceId )" + 
							" and seritem.status = 'A' " + 
							" and seritem.organization_id = :orgId ")
					.setParameterList("serviceId", serviceIds)
					.setParameter("orgId", serviceItemMapperDto.getOrganizationId())
                    .setResultTransformer(Transformers.aliasToBean(ServiceItemMapperDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, list, null);

		} catch (Exception e)
		{
		logger.error("getServiceItemMapperDetails failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
}
