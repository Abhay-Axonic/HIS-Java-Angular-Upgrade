package com.param.global.otims.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.dozer.DozerFactory;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureOrderComponentDetailsDto;
import com.param.otims.model.ProcedureOrderComponentDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings("rawtypes")
public class ProcedureOrderComponentDetailsDaoImpl extends GenericDao<ProcedureOrderComponentDetails, Integer> implements IProcedureOrderComponentDetailsDao, ICommonConstants{

	public ProcedureOrderComponentDetailsDaoImpl() {
		super(ProcedureOrderComponentDetails.class);
	}

	@Autowired
	private DozerFactory factory;
	
	@Override
	public Response saveProcedureOrderComponentDetails(ProcedureOrderComponentDetailsDto dto)throws ApplicationException {
		try{
			ProcedureOrderComponentDetails details = factory.map(dto, ProcedureOrderComponentDetails.class, null);
			details.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details = save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, details);
		}catch(Exception e){
			
			throw e;
		}
	}

}