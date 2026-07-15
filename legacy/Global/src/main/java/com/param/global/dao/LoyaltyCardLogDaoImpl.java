package com.param.global.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.org.model.LoyaltyCardLog;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.LoyaltyCardLogDto;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class LoyaltyCardLogDaoImpl extends GenericDao<LoyaltyCardLog, Integer> implements ILoyaltyCardLogDao,ICommonConstants
{
	public LoyaltyCardLogDaoImpl() {
		super(LoyaltyCardLog.class);
	}

	@Override
	public Response saveLoyaltyCardLog(LoyaltyCardLogDto dto) {
		try{
			 Calendar now = Calendar.getInstance();
			    now.add(Calendar.MINUTE, 3);
			    Date oneMinutesFromNow = now.getTime();
			    
			LoyaltyCardLog master = new LoyaltyCardLog();
			master.setAuthKey(dto.getAuthKey());
			master.setLoyaltyCardNo(dto.getLoyaltyCardNo());
			master.setLoyaltyCardStatus(dto.getLoyaltyCardStatus());
			master.setOtp(dto.getOtp());
			master.setOtpCount(dto.getOtpCount());
			master.setOtpExpiredTime(oneMinutesFromNow);
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganizationId(dto.getOrganizationId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setAmount(dto.getAmount());
			master.setIsVerified('N');
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getLogDataByCardNo(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			List<LoyaltyCardLogDto> list =sessionFactory.getCurrentSession().getNamedQuery("GET_LOYALTI_CARD_LOG_DATA")
				.setParameter("cardNo", dto.getCardNo())	
				.setParameter("authKey", dto.getAuthKey())				
				.setResultTransformer(Transformers.aliasToBean(LoyaltyCardLogDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null,list,null);			
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateLogs(LoyaltyCardLogDto loyaltyCardLogDto) throws ApplicationException {
		try{
			LoyaltyCardLog mstr = findById(loyaltyCardLogDto.getLoyaltyCardLogId());
			mstr.setIsVerified('Y');
			mstr.setLoyaltyCardStatus(loyaltyCardLogDto.getLoyaltyCardStatus());
			update(mstr);
			return new Response<>(SUCCESS, SUCCESS_CODE, null,null,mstr);			
			
		}catch(Exception e){
			
			throw e;
		}
	}

}
