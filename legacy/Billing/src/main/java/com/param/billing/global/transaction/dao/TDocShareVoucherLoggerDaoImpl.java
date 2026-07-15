package com.param.billing.global.transaction.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDocSharePaymentVoucherLoggerDto;
import com.param.billing.global.transaction.dto.TDoctorShareLoggerReqDto;
import com.param.billing.global.transaction.model.TDocSharePaymentVoucher;
import com.param.billing.global.transaction.model.TDocSharePaymentVoucherLogger;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes"})
public class TDocShareVoucherLoggerDaoImpl extends GenericDao<TDocSharePaymentVoucherLogger, Integer> implements IDocShareVoucherLoggerDao, ICommonConstants{

	public TDocShareVoucherLoggerDaoImpl() {
		super(TDocSharePaymentVoucherLogger.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	public Response saveDoctorSharePaymentVoucherList(TDocSharePaymentVoucherLoggerDto masterDto)
			throws ServiceException {
		try{
				
				TDocSharePaymentVoucherLogger tDocShareVoucher = new TDocSharePaymentVoucherLogger();
					tDocShareVoucher.setCreatedBy(masterDto.getCreatedBy());
					tDocShareVoucher.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
					tDocShareVoucher.setOrgId(masterDto.getOrgId());
					tDocShareVoucher.setStatus(ACTIVE);
					tDocShareVoucher.setOrgUnitId(masterDto.getOrgUnitId());
					tDocShareVoucher.setVoucherNo(masterDto.getVoucherNo());
					tDocShareVoucher.setProcessedShareAmt(masterDto.getProcessedShareAmt());
					tDocShareVoucher.setRetainerShipAmt(masterDto.getRetainerShipAmt());
					tDocShareVoucher.setTdsAmt(masterDto.getTdsAmt());
					tDocShareVoucher.setPaidAmt(masterDto.getPaidAmt());
					tDocShareVoucher.setConsumedDoctorAdvance(masterDto.getConsumedDoctorAdvance());
					tDocShareVoucher.setConsumedDoctorCr(masterDto.getConsumedDoctorCr());
					tDocShareVoucher.setPaymentVoucherId(masterDto.getPaymentVoucherId());
                    tDocShareVoucher.setDocShareVoucherId(masterDto.getDocShareVoucherId());
				tDocShareVoucher = save(tDocShareVoucher);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, tDocShareVoucher.getVoucherLoggerId());
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveDoctorSharePaymentVoucher(TDoctorShareLoggerReqDto masterDto)
			throws ServiceException {
		try{
				
			TDocSharePaymentVoucher tDocShareVoucher = new TDocSharePaymentVoucher();
					tDocShareVoucher.setCreatedBy(masterDto.getCreatedBy());
					tDocShareVoucher.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()).getTime());
					tDocShareVoucher.setOrgId(masterDto.getOrgId());
					tDocShareVoucher.setStatus(ACTIVE);
					tDocShareVoucher.setOrgUnitId(masterDto.getUnitId());
					tDocShareVoucher.setTotalProcessedAmt(masterDto.getProcessedAmt());
					tDocShareVoucher.setTotalRetainerShipAmt(masterDto.getRetainerShipAmt());
					tDocShareVoucher.setTdsAmt(masterDto.getTdsAmt());
					tDocShareVoucher.setTotalConsumedDoctorAdvance(masterDto.getConsumedDocAdvance());
					tDocShareVoucher.setTotalConsumedDoctorCr(masterDto.getConsumedDocCredit());
					tDocShareVoucher.setTotalPaidAmt(masterDto.getPaidAmt());
					tDocShareVoucher.setPaymentVoucherNo(iGlobalUnitMasterDao.getGeneratedNo(0, masterDto.getUnitId(),161));
					tDocShareVoucher.setDoctorId(masterDto.getDoctorId());
					tDocShareVoucher.setReferralId(masterDto.getReferralId());
				Integer tDocShareVoucherId = (Integer)sessionFactory.getCurrentSession().save(tDocShareVoucher);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, tDocShareVoucherId);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


}
