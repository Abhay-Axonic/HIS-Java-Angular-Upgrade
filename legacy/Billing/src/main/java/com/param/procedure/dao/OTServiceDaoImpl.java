package com.param.procedure.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.dozer.DozerFactory;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.otims.model.OperationTheaterService;
import com.param.procedure.dto.OTServiceDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OTServiceDaoImpl extends GenericDao<OperationTheaterService, Integer> implements IOTServiceDao, ICommonConstants{

	public OTServiceDaoImpl() {
		super(OperationTheaterService.class);
	}

	@Autowired
	private DozerFactory factory;
	
	@Override
	public Response saveOTService(OTServiceDto dto) throws ApplicationException {
		try{
			if(dto.getOtServiceId() == null) {
				OperationTheaterService otService = factory.map(dto, OperationTheaterService.class, null);
				otService.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				otService = save(otService);
			}else {
				OperationTheaterService otService = factory.map(dto, OperationTheaterService.class, null);
				otService.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				otService = update(otService);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOtServicesByProReqId(int id) throws ApplicationException {
		try{
			List<OTServiceDto> listOTServiceDto = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_SERVICES_BY_PRO_REQ_ID").setParameter("id", id).setResultTransformer(Transformers.aliasToBean(OTServiceDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOTServiceDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}
