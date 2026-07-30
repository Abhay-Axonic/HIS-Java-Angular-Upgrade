package com.param.global.unit.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.model.master.MpesaApplicationConfiguration;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.MpesaApplicationConfigurationDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MpesaAppConfigDaoImpl extends GenericDao<MpesaApplicationConfiguration, Integer>
implements IMpesaAppConfigDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(MpesaAppConfigDaoImpl.class);

	public MpesaAppConfigDaoImpl() {
		super(MpesaApplicationConfiguration.class);
	}

	@Override
	public Response saveMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
		
			MpesaApplicationConfiguration mstr = new MpesaApplicationConfiguration();
			
			mstr.setAccountRefernece("re");
			mstr.setAppKey(mpesaApplicationConfigurationDto.getAppKey());
			mstr.setAppSecretKey(mpesaApplicationConfigurationDto.getAppSecretKey());
			mstr.setBusinessShortcode(mpesaApplicationConfigurationDto.getBusinessShortcode());
			mstr.setCallbackUrl("https://be070c6d.ngrok.io/hooks/mpesa");
			mstr.setPartyB(mpesaApplicationConfigurationDto.getPartyB());
			mstr.setStkPushPassword(mpesaApplicationConfigurationDto.getStkPushPassword());
			mstr.setStkTimestamp("20190413123520");
			mstr.setTerminalMsisdn(mpesaApplicationConfigurationDto.getTerminalMsisdn());
			mstr.setTransactionDescription("re");
			mstr.setTransactionType("CustomerPayBillOnline");
			mstr.setValidationPassword(mpesaApplicationConfigurationDto.getValidationPassword());
			mstr.setCreatedBy(mpesaApplicationConfigurationDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(mpesaApplicationConfigurationDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(mpesaApplicationConfigurationDto.getOrganizationId());
			mstr.setStatus(mpesaApplicationConfigurationDto.getStatus());
			mstr.setUnitId(mpesaApplicationConfigurationDto.getUnitId());
			mstr.setUpdatedBy(mpesaApplicationConfigurationDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(mpesaApplicationConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			if(mpesaApplicationConfigurationDto.getMpesaApplicationConfigurationId()!=null) {
				mstr.setMpesaApplicationConfigurationId(mpesaApplicationConfigurationDto.getMpesaApplicationConfigurationId());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}else {
				
				MpesaApplicationConfigurationDto dto = null;
				if(mpesaApplicationConfigurationDto.getOrganizationId()!=null && mpesaApplicationConfigurationDto.getUnitId()!=null) {
					
					dto = (MpesaApplicationConfigurationDto) sessionFactory.getCurrentSession()
								.getNamedQuery("CHECK_FOR_UNIQUE_MPESA_APP_CONFIGURATION")
								.setParameter("organizationId", mpesaApplicationConfigurationDto.getOrganizationId())
								.setParameter("unitId", mpesaApplicationConfigurationDto.getUnitId())
								.setResultTransformer(Transformers.aliasToBean(MpesaApplicationConfigurationDto.class)).uniqueResult();
				}
				if(dto!=null)
				{
					return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
					
				}else {
					
					save(mstr);
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
					
				}
			}
			
		} catch (Exception e) {
			logger.error("saveMpesaAppConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateMpesaAppConfigStatus(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
			MpesaApplicationConfiguration mstr = findById(mpesaApplicationConfigurationDto.getMpesaApplicationConfigurationId());
			mstr.setStatus(mpesaApplicationConfigurationDto.getStatus());
			mstr.setUpdatedBy(mpesaApplicationConfigurationDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(mpesaApplicationConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error("updateMpesaAppConfigStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
			MpesaApplicationConfigurationDto obj = (MpesaApplicationConfigurationDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MPESA_APPLICATION_CONFIGURATION")
					.setParameter("organizationId", mpesaApplicationConfigurationDto.getOrganizationId())
					.setParameter("unitId", mpesaApplicationConfigurationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MpesaApplicationConfigurationDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
		} catch (Exception e) {
			logger.error("getMpesaAppConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getMpesaAppConfigList(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
			List<MpesaApplicationConfigurationDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MPESA_APPLICATION_CONFIGURATION_LIST")
					.setParameter("organizationId", mpesaApplicationConfigurationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MpesaApplicationConfigurationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			logger.error("getMpesaAppConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
