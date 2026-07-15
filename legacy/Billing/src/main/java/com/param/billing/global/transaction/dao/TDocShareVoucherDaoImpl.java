package com.param.billing.global.transaction.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDocShareVoucherDto;
import com.param.billing.global.transaction.model.TDocShareVoucher;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings("rawtypes")
public class TDocShareVoucherDaoImpl extends GenericDao<TDocShareVoucher, Integer> implements IDocShareVoucherDao, ICommonConstants{

	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	public TDocShareVoucherDaoImpl() {
		super(TDocShareVoucher.class);
	}

	@Override
	public Response saveDocShareVoucher(TDocShareVoucherDto masterDto)
			throws ServiceException {
		try{
			TDocShareVoucher tDocShareVoucher = new TDocShareVoucher();
				tDocShareVoucher.setCreatedBy(masterDto.getCreatedBy());
				tDocShareVoucher.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
				tDocShareVoucher.setOrgId(masterDto.getOrgId());
				tDocShareVoucher.setStatus(ACTIVE);
				tDocShareVoucher.setDocId(masterDto.getDocId());
				tDocShareVoucher.setReferralId(masterDto.getReferralId());
				tDocShareVoucher.setOrgUnitId(masterDto.getOrgUnitId());
				tDocShareVoucher.setUpdatedBy(masterDto.getUpdatedBy());
				tDocShareVoucher.setTotalDocShareAmout(masterDto.getTotalDocShareAmout());
				tDocShareVoucher.setVoucherNo(iGlobalUnitMasterDao.getGeneratedNo(0, masterDto.getOrgUnitId(),160));//
				tDocShareVoucher.setRemark(masterDto.getRemark());
				tDocShareVoucher.setProcessedShareAmt(masterDto.getProcessedShareAmt());
				tDocShareVoucher.setDueShareAmt(masterDto.getProcessedShareAmt());
				tDocShareVoucher.setType(masterDto.getType());
				tDocShareVoucher.setDueDate(masterDto.getDueDate());
			tDocShareVoucher = save(tDocShareVoucher);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDocShareVoucher);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDocShareVoucher(TDocShareVoucherDto masterDto) throws ServiceException {
		
		try{
			TDocShareVoucher tDocShareVoucher = findById(masterDto.getDocShareVoucheId());
			if (tDocShareVoucher.getDocShareVoucheId() != 0)
			{
				tDocShareVoucher.setDocShareVoucheId(masterDto.getDocShareVoucheId());
				tDocShareVoucher.setDueShareAmt(tDocShareVoucher.getDueShareAmt() - masterDto.getDueShareAmt());
				tDocShareVoucher.setUpdatedBy(masterDto.getUpdatedBy());
				tDocShareVoucher.setUpdatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
				tDocShareVoucher = update(tDocShareVoucher);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDocShareVoucher);
			}else {
				
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, tDocShareVoucher);
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

}
