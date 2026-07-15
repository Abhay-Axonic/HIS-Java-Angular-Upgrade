package com.param.billing.global.transaction.dao;

import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDocShareVoucherDtlsDto;
import com.param.billing.global.transaction.model.TDocShareVoucherDtls;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings("rawtypes")
public class TDocShareVoucherDtlsDaoImpl extends GenericDao<TDocShareVoucherDtls, Integer> implements IDocShareVoucherDtlsDao, ICommonConstants{

	public TDocShareVoucherDtlsDaoImpl() {
		super(TDocShareVoucherDtls.class);
	}

	@Override
	public Response saveDocShareVoucherDtls(TDocShareVoucherDtlsDto masterDto)
			throws ServiceException {
		try{
			TDocShareVoucherDtls tDocShareVoucherDtls = new TDocShareVoucherDtls();
				tDocShareVoucherDtls.setCreatedBy(masterDto.getCreatedBy());
				tDocShareVoucherDtls.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
				tDocShareVoucherDtls.setOrgId(masterDto.getOrgId());
				tDocShareVoucherDtls.setStatus(ACTIVE);
				tDocShareVoucherDtls.setOrgUnitId(masterDto.getOrgUnitId());
				tDocShareVoucherDtls.setUpdatedBy(masterDto.getUpdatedBy());
				tDocShareVoucherDtls.setDocShareVoucheId(masterDto.getDocShareVoucheId());
				tDocShareVoucherDtls.setProcessedAmt(masterDto.getProcessedAmt());
				tDocShareVoucherDtls.setUpdatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
				tDocShareVoucherDtls.setDoctorShareId(masterDto.getDoctorShareId());
				tDocShareVoucherDtls.setDoctorSlabTransactionId(masterDto.getDoctorSlabTransactionId());
				tDocShareVoucherDtls.setReferralShareId(masterDto.getReferralShareId());
			tDocShareVoucherDtls = save(tDocShareVoucherDtls);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDocShareVoucherDtls);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

}
