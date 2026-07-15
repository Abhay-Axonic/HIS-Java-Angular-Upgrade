package com.param.adt.master.global.dao;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.dto.ICPCCodesConfigurationDto;
import com.param.adt.master.global.dto.ICPCCodesConfigurationMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.ICPCCodeConfiguration;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class ICPCCodesConfigurationDaoImpl extends GenericDao<ICPCCodeConfiguration, Integer>implements IICPCCodesConfigurationDao, ICommonConstants{

	public ICPCCodesConfigurationDaoImpl() {
		super(ICPCCodeConfiguration.class);
	}

	@Override
	public Response saveICPCCodesConfiguration(ICPCCodesConfigurationDto iCPCCodesConfigurationDto)
			throws ApplicationException {
		try{
			if(iCPCCodesConfigurationDto.getIcpcCodeConfigurationId()==null){
			ICPCCodesConfigurationDto 	dto = (ICPCCodesConfigurationDto)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_SYSTEM_ID")
					.setParameter("organizationId", iCPCCodesConfigurationDto.getOrganizationId())
					.setParameter("unitId", iCPCCodesConfigurationDto.getUnitId())
					.setParameter("icpcSystemId", iCPCCodesConfigurationDto.getIcpcSystemId())
					.setResultTransformer(Transformers.aliasToBean(ICPCCodesConfigurationDto.class)).uniqueResult();
				if(dto!=null) {
					return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, dto);
			}else {
				ICPCCodeConfiguration iCPCCodeConfiguration = new ICPCCodeConfiguration();
				iCPCCodeConfiguration.setIcpcSystemId(iCPCCodesConfigurationDto.getIcpcSystemId());
				iCPCCodeConfiguration.setOrganizationId(iCPCCodesConfigurationDto.getOrganizationId());
				iCPCCodeConfiguration.setUnitId(iCPCCodesConfigurationDto.getUnitId());
				iCPCCodeConfiguration.setCreatedDate(GlobalCommonDateUtils.getDate(iCPCCodesConfigurationDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				iCPCCodeConfiguration.setUpdatedBy(iCPCCodesConfigurationDto.getUpdatedBy());
				iCPCCodeConfiguration.setUpdatedDate(GlobalCommonDateUtils.getDate(iCPCCodesConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				iCPCCodeConfiguration.setCreatedBy(iCPCCodesConfigurationDto.getCreatedBy());
				iCPCCodeConfiguration.setStatus(iCPCCodesConfigurationDto.getStatus());
				iCPCCodeConfiguration = save(iCPCCodeConfiguration);
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, iCPCCodeConfiguration);
				}
			}else {
				ICPCCodeConfiguration iCPCCodeConfiguration =findById(iCPCCodesConfigurationDto.getIcpcCodeConfigurationId());
				iCPCCodeConfiguration.setIcpcSystemId(iCPCCodesConfigurationDto.getIcpcSystemId());
				iCPCCodeConfiguration.setOrganizationId(iCPCCodesConfigurationDto.getOrganizationId());
				iCPCCodeConfiguration.setUnitId(iCPCCodesConfigurationDto.getUnitId());
				iCPCCodeConfiguration.setCreatedDate(GlobalCommonDateUtils.getDate(iCPCCodesConfigurationDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				iCPCCodeConfiguration.setUpdatedBy(iCPCCodesConfigurationDto.getUpdatedBy());
				iCPCCodeConfiguration.setUpdatedDate(GlobalCommonDateUtils.getDate(iCPCCodesConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				iCPCCodeConfiguration.setCreatedBy(iCPCCodesConfigurationDto.getCreatedBy());
				iCPCCodeConfiguration.setStatus(iCPCCodesConfigurationDto.getStatus());
				iCPCCodeConfiguration = update(iCPCCodeConfiguration);
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, iCPCCodeConfiguration);
			}
	} catch (HibernateException he) {
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	} catch (Exception e) {
		
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

	@Override
	public Response updateICPCCodesConfigurationStatus(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			
			ICPCCodeConfiguration mpr = findById(iCPCCodesConfigurationMapperDto.getIcpcConfigurationId());
			mpr.setStatus(iCPCCodesConfigurationMapperDto.getStatus());
			update(mpr);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}
