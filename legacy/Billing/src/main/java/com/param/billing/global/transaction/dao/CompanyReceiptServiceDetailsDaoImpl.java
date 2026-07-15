package com.param.billing.global.transaction.dao;

import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.dto.CompanyReceiptServiceDetailsDto;
import com.param.billing.global.transaction.model.CompanyReceiptServiceDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes"})
public class CompanyReceiptServiceDetailsDaoImpl extends GenericDao<CompanyReceiptServiceDetails, Integer> implements ICompanyReceiptServiceDetailDao, ICommonConstants{

	public CompanyReceiptServiceDetailsDaoImpl() {
		super(CompanyReceiptServiceDetails.class);
	}
	

	@Override
	public Response saveCompanyReceiptServiceDetail(CompanyReceiptServiceDetailsDto masterDto) throws ApplicationException {
		try{
			CompanyReceiptServiceDetails companyReceiptServiceDtls = new CompanyReceiptServiceDetails();
				companyReceiptServiceDtls.setCreatedBy(masterDto.getCreatedBy());
				companyReceiptServiceDtls.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
				companyReceiptServiceDtls.setOrgId(masterDto.getOrgId());
				companyReceiptServiceDtls.setOrgUnitId(masterDto.getOrgUnitId());
				companyReceiptServiceDtls.setDisallowedAmount(masterDto.getDisallowedAmount());
				companyReceiptServiceDtls.setReceivedAmount(masterDto.getReceivedAmount());
				companyReceiptServiceDtls.setStatus(ACTIVE);
				
				if(masterDto.getServiceId()!=null && masterDto.getServiceId()>0)
					companyReceiptServiceDtls.setServiceId(masterDto.getServiceId());
				
				if(masterDto.getItemId()!=null && masterDto.getItemId()>0)
					companyReceiptServiceDtls.setItemId(masterDto.getItemId());
				
				companyReceiptServiceDtls.setComReceiptDtlId(masterDto.getComReceiptDtlId());
				companyReceiptServiceDtls.setTdsAmount(masterDto.getTdsAmount());
				companyReceiptServiceDtls.setOtherDeduction(masterDto.getOtherDeduction());
			companyReceiptServiceDtls = save(companyReceiptServiceDtls);
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, null, companyReceiptServiceDtls);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	
	

}
