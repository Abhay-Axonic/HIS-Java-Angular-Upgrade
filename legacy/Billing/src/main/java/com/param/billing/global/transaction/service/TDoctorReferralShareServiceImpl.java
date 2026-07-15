package com.param.billing.global.transaction.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dao.IDocShareVoucherDao;
import com.param.billing.global.transaction.dao.IDocShareVoucherDtlsDao;
import com.param.billing.global.transaction.dao.IDoctorReferralShareDao;
import com.param.billing.global.transaction.dto.TDocShareVoucherDtlsDto;
import com.param.billing.global.transaction.dto.TDocShareVoucherDto;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.model.TDocShareVoucher;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class TDoctorReferralShareServiceImpl implements ITDoctorReferralShareService, ICommonConstants{

	@Autowired
	private IDoctorReferralShareDao  iDoctorReferralShareDao;
	
	@Autowired
	private IDocShareVoucherDao iDocShareVoucherDao;
	
	@Autowired
	private IDocShareVoucherDtlsDao  iDocShareVoucherDtlsDao;	
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	@Transactional
	public Response getDoctorReferralShareList(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException {
		try {
			List<TDoctorReferralShareDto>  listDoctorReferralShareDtos = new ArrayList<TDoctorReferralShareDto>();	
			Response result= iDoctorReferralShareDao.getDoctorReferralShareList(tDoctorShareDto);
			if(result.getCode().equalsIgnoreCase(SUCCESS_CODE) && result.getListObject()!= null){
				Iterator itr=result.getListObject().iterator();
				while(itr.hasNext()) {
					TDoctorReferralShareDto dto=(TDoctorReferralShareDto)itr.next();
					TDoctorShareReqDto doctorShareReqDto= new TDoctorShareReqDto();
					doctorShareReqDto.setOrgId(tDoctorShareDto.getOrgId());
					doctorShareReqDto.setOrgUnitId(tDoctorShareDto.getOrgUnitId());
					doctorShareReqDto.setDoctorShareId(dto.getReferralDocId());
					Response resp=iDoctorReferralShareDao.getDoctorReferralShareMasterList(doctorShareReqDto);
					if(resp.getCode().equalsIgnoreCase(SUCCESS_CODE) && resp.getListObject()!= null){
						dto.setPerShareList(resp.getListObject());
					}
					listDoctorReferralShareDtos.add(dto);
				}
				
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listDoctorReferralShareDtos, null);
				
			}
			//return iDoctorReferralShareDao.getDoctorReferralShareList(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@SuppressWarnings({ "unused", "null" })
	@Override
	@Transactional
	public Response saveReferralDoctorShareProcessList(TDoctorShareProcessReqDto reqDto) throws ApplicationException {
		try {
			
			for(TDocShareVoucherDto vdto:reqDto.getListTDocShareVoucherDto()) 
			{
				vdto.setOrgId(reqDto.getOrgId());
				vdto.setOrgUnitId(reqDto.getUnitId());
				Response docShareVoucherRes = iDocShareVoucherDao.saveDocShareVoucher(vdto);
				TDocShareVoucherDtlsDto tDocShareVoucherDtlsDto= new TDocShareVoucherDtlsDto();
				if(docShareVoucherRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && docShareVoucherRes.getObject() != null){
					TDocShareVoucher tDocShareVoucher = (TDocShareVoucher)docShareVoucherRes.getObject();
					Integer docShareVoucheId = tDocShareVoucher.getDocShareVoucheId();
					tDocShareVoucherDtlsDto.setDocShareVoucheId(docShareVoucheId);
					tDocShareVoucherDtlsDto.setOrgId(reqDto.getOrgId());
					tDocShareVoucherDtlsDto.setOrgUnitId(reqDto.getOrgId());
					tDocShareVoucherDtlsDto.setProcessedAmt(tDocShareVoucher.getProcessedShareAmt());
					tDocShareVoucherDtlsDto.setCreatedBy(tDocShareVoucher.getCreatedBy());
					tDocShareVoucherDtlsDto.setUpdatedBy(tDocShareVoucher.getUpdatedBy());
					tDocShareVoucherDtlsDto.setReferralShareId(vdto.getReferralId());
					Response result=  iDocShareVoucherDtlsDao.saveDocShareVoucherDtls(tDocShareVoucherDtlsDto);
					if(result.getStatus().equals(SUCCESS)) {
						TDoctorReferralShareDto  masterDto = new TDoctorReferralShareDto();
						masterDto.setUpdatedBy(vdto.getCreatedBy());
						masterDto.setReferralShareId(vdto.getReferralId());
						masterDto.setDueShareAmt(vdto.getDueShareAmt());
						masterDto.setReferralSharePer(vdto.getReferralSharePer());
						iDoctorReferralShareDao.updateDoctorReferralShare(masterDto);
					}
				}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorReferralSharePaymentVoucherDtlsByReferralShareId(TDoctorShareReqDto tDoctorShareDto)
			throws ApplicationException {
		try {
			return iDoctorReferralShareDao.getDoctorReferralSharePaymentVoucherDtlsByReferralShareId(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveDoctorReferralShare(TDoctorReferralShareDto tDoctorReferralShareDto) throws ServiceException {
		try {
			return iDoctorReferralShareDao.saveDoctorReferralShare(tDoctorReferralShareDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
