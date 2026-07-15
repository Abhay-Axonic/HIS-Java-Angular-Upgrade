package com.param.billing.global.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AccountPayableMasterDto;
import com.param.global.model.AccountPayableMaster;
import in.param.common.dao.GenericDao;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class AccountPayableMasterDaoImpl extends GenericDao<AccountPayableMaster, Integer>implements IAccountPayableMasterDao, ICommonConstants{

	public AccountPayableMasterDaoImpl() {
		super(AccountPayableMaster.class);
		
	}

	@Override
	public Response saveAccountPayable(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
		try {
			AccountPayableMasterDto dto =null;
			if(accountPayableMasterDto.getAccCode()!=null && accountPayableMasterDto.getAccPayableCode()!=null && accountPayableMasterDto.getOrgId()!=null && accountPayableMasterDto.getOrgUnitId()!=null && accountPayableMasterDto.getAccPayableMstId()==0)
			{
				dto = (AccountPayableMasterDto)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_CODE")
					.setParameter("orgId", accountPayableMasterDto.getOrgId())
					.setParameter("unitId", accountPayableMasterDto.getOrgUnitId())
					.setParameter("code",accountPayableMasterDto.getAccCode().toLowerCase())
					.setParameter("payCode",accountPayableMasterDto.getAccPayableCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(AccountPayableMasterDto.class)).uniqueResult();
			}else if(accountPayableMasterDto.getAccCode()!=null && accountPayableMasterDto.getAccPayableCode()!=null && accountPayableMasterDto.getOrgId()!=null && accountPayableMasterDto.getOrgUnitId()!=null && accountPayableMasterDto.getAccPayableMstId()!=0) {
				dto = (AccountPayableMasterDto)sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_FOR_UNIQUE_CODE_WITH_ID")
						.setParameter("orgId", accountPayableMasterDto.getOrgId())
						.setParameter("unitId", accountPayableMasterDto.getOrgUnitId())
						.setParameter("code",accountPayableMasterDto.getAccCode().toLowerCase())
						.setParameter("payCode",accountPayableMasterDto.getAccPayableCode().toLowerCase())
						.setParameter("accPayableMstId", accountPayableMasterDto.getAccPayableMstId())
						.setResultTransformer(Transformers.aliasToBean(AccountPayableMasterDto.class)).uniqueResult();
			}
			if(dto!=null)
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			
			}
			
			
			AccountPayableMaster mstr = new AccountPayableMaster();
			mstr.setAccAddress(accountPayableMasterDto.getAccAddress());
			mstr.setAccAreaId(accountPayableMasterDto.getAccAreaId());
			mstr.setAccCityId(accountPayableMasterDto.getAccCityId());
			mstr.setAccCode(accountPayableMasterDto.getAccCode());
			mstr.setAccContactNumber(accountPayableMasterDto.getAccContactNumber());
			mstr.setAccCountryId(accountPayableMasterDto.getAccCountryId());
			mstr.setAccCreditLimit(accountPayableMasterDto.getAccCreditLimit());
			mstr.setAccDay(accountPayableMasterDto.getAccDay());
			mstr.setAccDesc(accountPayableMasterDto.getAccDesc());
			mstr.setAccDistrictId(accountPayableMasterDto.getAccDistrictId());
			mstr.setAccEmail(accountPayableMasterDto.getAccEmail());
			mstr.setAccMobile(accountPayableMasterDto.getAccMobile());
			mstr.setAccPayableCode(accountPayableMasterDto.getAccPayableCode());
			mstr.setAccPaymentTerms(accountPayableMasterDto.getAccPaymentTerms());
			mstr.setAccStateId(accountPayableMasterDto.getAccStateId());
			mstr.setAccZipcode(accountPayableMasterDto.getAccZipcode());
			mstr.setCreatedBy(accountPayableMasterDto.getCreatedBy());
			mstr.setDeptId(accountPayableMasterDto.getDeptId());
			mstr.setOrderId(accountPayableMasterDto.getOrderId());
			mstr.setOrgId(accountPayableMasterDto.getOrgId());
			mstr.setOrgUnitId(accountPayableMasterDto.getOrgUnitId());
			mstr.setStatus(accountPayableMasterDto.getStatus());
			mstr.setSubDeptId(accountPayableMasterDto.getSubDeptId());
			mstr.setUpdatedBy(accountPayableMasterDto.getUpdatedBy());
			mstr.setAccValidityFromDate(GlobalCommonDateUtils.getDate(accountPayableMasterDto.getAccValidityFromDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setAccValidityToDate(GlobalCommonDateUtils.getDate(accountPayableMasterDto.getAccValidityToDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(accountPayableMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(accountPayableMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			if(accountPayableMasterDto.getAccWorkFromTime()!=null && accountPayableMasterDto.getAccWorkFromTime() !="") {
				LocalTime fromTime1 = LocalTime.parse(accountPayableMasterDto.getAccWorkFromTime());
				LocalDate today = LocalDate.now();
				Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				mstr.setAccWorkFromTime(fromTime);
			}
			
			if(accountPayableMasterDto.getAccWorkToTime()!=null && accountPayableMasterDto.getAccWorkToTime() !="") {
				LocalTime toTime1 = LocalTime.parse(accountPayableMasterDto.getAccWorkToTime());
				LocalDate today = LocalDate.now();
				Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				mstr.setAccWorkToTime(toTime);
			}
			
			if(accountPayableMasterDto.getAccPayableMstId()==0) {
				
				mstr = save(mstr);
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			}else {
				
				mstr.setAccPayableMstId(accountPayableMasterDto.getAccPayableMstId());
				mstr = update(mstr);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}
			
			
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAccountPayable(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
		try {
			
			List<AccountPayableMasterDto> accountPayableMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACCOUNT_PAY_LIST")
					.setParameter("organizationId", accountPayableMasterDto.getOrgId())
					.setParameter("unitId", accountPayableMasterDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(AccountPayableMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, accountPayableMaster, null);
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateAccountPayableStatus(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
		try {
			AccountPayableMaster mstr = findById(accountPayableMasterDto.getAccPayableMstId());
			mstr.setStatus(accountPayableMasterDto.getStatus());
			mstr = update(mstr);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getActiveAccountPayable(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
try {
			
			List<AccountPayableMasterDto> accountPayableMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_ACCOUNT_PAY_LIST")
					.setParameter("organizationId", accountPayableMasterDto.getOrgId())
					.setParameter("unitId", accountPayableMasterDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(AccountPayableMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, accountPayableMaster, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}
