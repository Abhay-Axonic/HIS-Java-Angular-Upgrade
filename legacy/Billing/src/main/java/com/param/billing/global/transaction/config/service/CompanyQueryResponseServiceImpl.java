package com.param.billing.global.transaction.config.service;


import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.billing.global.transaction.config.dto.CompanyInvoiceQueryReqDto;
import com.param.billing.global.transaction.config.dto.MCompanyInvoiceQueryMasterDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceQueryDetailDto;
import com.param.billing.global.transaction.dao.ICompanyQueryResponseDetailsDao;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;


@Service
@SuppressWarnings("rawtypes")
public class CompanyQueryResponseServiceImpl implements ICompanyQueryResponseService, ICommonConstants{

	@Autowired
	private ICompanyQueryResponseDetailsDao iCompanyQueryResponseDetailsDao;

	
	@Override
	@Transactional
	public Response saveCompanyQueryResponse(CompanyInvoiceQueryReqDto reqDto) throws ApplicationException {
		try {
			TCompanyInvoiceQueryDetailDto tCompanyInvoiceQueryDetailDto = reqDto.gettCompanyInvoiceQueryDetailDto();
			if(tCompanyInvoiceQueryDetailDto != null){
				tCompanyInvoiceQueryDetailDto.setOrgId(reqDto.getOrgId());
				tCompanyInvoiceQueryDetailDto.setUnitId(reqDto.getUnitId());
				iCompanyQueryResponseDetailsDao.saveCompanyQueryResponseDetails(tCompanyInvoiceQueryDetailDto);
				/*Response mCompanyInvoiceQueryMasterRes = iCompanyQueryResponseMasterDao.saveCompanyQueryResponse(mCompanyInvoiceQueryMasterDto);
				if(mCompanyInvoiceQueryMasterRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && mCompanyInvoiceQueryMasterRes.getObject() != null){
					MCompanyInvoiceQueryMaster mCompanyInvoiceQueryMaster = (MCompanyInvoiceQueryMaster)mCompanyInvoiceQueryMasterRes.getObject();
					Integer companyInvoiceQueryId = mCompanyInvoiceQueryMaster.getCompanyInvoiceQueryId();
					ListIterator<TCompanyInvoiceQueryDetailDto> servicDtlsItr = reqDto.getListTCompanyInvoiceQueryDetailDto().listIterator();
					TCompanyInvoiceQueryDetailDto tCompanyInvoiceQueryDetailDto = null;
					while(servicDtlsItr.hasNext()){
						tCompanyInvoiceQueryDetailDto = servicDtlsItr.next();
						tCompanyInvoiceQueryDetailDto.setQueryId(companyInvoiceQueryId);
						iCompanyQueryResponseDetailsDao.saveCompanyQueryResponseDetails(tCompanyInvoiceQueryDetailDto);
							
					}
				}*/
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
		
	} catch (Exception e) {
		
		throw e;
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	@Override
	@Transactional
	public Response updateCompanyQueryResponseDetailsByCompanyInvoiceReminderId(List<TCompanyInvoiceQueryDetailDto> listReqDto) throws ApplicationException {
		try {
			if(listReqDto != null && listReqDto.size() > 0){
				ListIterator<TCompanyInvoiceQueryDetailDto> compInvQryItr = listReqDto.listIterator();
				while(compInvQryItr.hasNext()){
					TCompanyInvoiceQueryDetailDto reqDto = compInvQryItr.next();
					iCompanyQueryResponseDetailsDao.updateCompanyQueryResponseDetailsByCompanyInvoiceReminderId(reqDto);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			return new Response<>(ERROR, NOT_FOUND_CODE, NOT_FOUND, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response getPendingCompanyQueryResponseDetails(TCompanyInvoiceQueryDetailDto reqDto) throws ApplicationException {
		try {
			return iCompanyQueryResponseDetailsDao.getPendingCompanyQueryResponseDetails(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
