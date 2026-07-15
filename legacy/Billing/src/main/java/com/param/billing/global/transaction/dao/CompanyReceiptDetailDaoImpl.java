package com.param.billing.global.transaction.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.param.billing.global.model.CompanyReceiptDetail;
import com.param.billing.global.transaction.dto.CompanyReceiptDetailDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes"})
public class CompanyReceiptDetailDaoImpl extends GenericDao<CompanyReceiptDetail, Integer> implements ICompanyReceiptDetailDao, ICommonConstants{

	public CompanyReceiptDetailDaoImpl() {
		super(CompanyReceiptDetail.class);
	}

	@Override
	public Response saveCompanyReceiptDetail(CompanyReceiptDetailDto dto) throws ApplicationException {
		try{
			Date today = TimeZoneComponent.getDateByzone(dto.getUnitId());
			CompanyReceiptDetail details = new CompanyReceiptDetail();
				details.setCompanyReceiptId(dto.getCompanyReceiptId());
				details.setNetReceiptAmount(dto.getNetReceiptAmount().doubleValue());
				details.setTdsAmount(dto.getTdsAmount().doubleValue());
				details.setOtherDeduction(dto.getOtherDeduction().doubleValue());
				details.setDisallowedAmount(dto.getDisallowedAmount().doubleValue());
				details.setTotalAmount(dto.getTotalAmount().doubleValue());
				details.setCompanyInvoiceDetailId(dto.getCompanyInvoiceDetailId());
				details.setCreatedDate(today);
				details.setCreatedBy(dto.getCreatedBy());
				details.setUpdatedDate(today);
				details.setUpdatedBy(dto.getCreatedBy());
				details.setUnitId(dto.getUnitId());
				details.setOrgId(dto.getOrgId());
			details = save(details);
			today = null;
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, details);
		}catch(Exception e){
			
			throw e;
		}
	}

}
