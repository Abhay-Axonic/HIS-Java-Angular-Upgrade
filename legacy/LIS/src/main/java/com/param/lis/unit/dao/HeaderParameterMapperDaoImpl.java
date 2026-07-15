package com.param.lis.unit.dao;

import org.springframework.stereotype.Repository;

import com.param.data.dto.HeaderParameterMapperDto;
import com.param.entity.lis.unit.HeaderParameterMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class HeaderParameterMapperDaoImpl extends GenericDao<HeaderParameterMapper, Integer>
implements IHeaderParameterMapperDao, ICommonConstants, IError, ITransactionConstants
{

	public HeaderParameterMapperDaoImpl() {
		super(HeaderParameterMapper.class);
	}

	
	@Override
	public Response saveHeaderParameterMapper(HeaderParameterMapperDto dto) throws ApplicationException {
		try{
			HeaderParameterMapper master = new HeaderParameterMapper();
				master.setHeaderId(dto.getHeaderId());
				master.setParameterId(dto.getParameterId());
				master.setParaSequence(dto.getParaSequence());
				master.setUnitId(dto.getUnitId());
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setOrganizationId(dto.getOrganizationId());
				if(dto.getHeaderParameterMapperId()!=null) {
					
					master.setStatus(dto.getStatus());
					master.setHeaderParameterMapperId(dto.getHeaderParameterMapperId());
					master = update(master);
					
				}else {
					master.setStatus('A');
					master = save(master);
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_200, HEADER_ADD_SUCC, null, master);
		 } catch (Exception e)
        {
            throw e;
        }
	}
	

}
