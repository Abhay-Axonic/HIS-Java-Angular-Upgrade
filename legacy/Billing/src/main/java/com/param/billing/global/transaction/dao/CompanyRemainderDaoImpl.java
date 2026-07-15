package com.param.billing.global.transaction.dao;

import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.dto.CompanyInvoiceReminderMasterDto;
import com.param.billing.global.transaction.model.CompanyInvoiceReminderMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class CompanyRemainderDaoImpl extends GenericDao<CompanyInvoiceReminderMaster, Integer> implements ICompanyRemainderDao, ICommonConstants{

	public CompanyRemainderDaoImpl() {
		super(CompanyInvoiceReminderMaster.class);
	}

	@Override
	public com.param.global.common.Response saveCompanyRemainder(CompanyInvoiceReminderMasterDto masterDto)
			throws ServiceException {
		try{
			CompanyInvoiceReminderMaster companyInvoiceReminderMaster = new CompanyInvoiceReminderMaster();
			companyInvoiceReminderMaster.setCreatedBy(masterDto.getCreatedBy());
			companyInvoiceReminderMaster.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()).getTime());
			companyInvoiceReminderMaster.setOrgId(masterDto.getOrgId());
			companyInvoiceReminderMaster.setUnitId(masterDto.getUnitId());
			companyInvoiceReminderMaster.setCompanyId(masterDto.getCompanyId());
			companyInvoiceReminderMaster.setPayeeId(masterDto.getPayeeId());
			companyInvoiceReminderMaster.setCompanyTypeId(masterDto.getCompanyTypeId());
			companyInvoiceReminderMaster.setInvoiceId(masterDto.getInvoiceId());
			companyInvoiceReminderMaster.setReminderDate(masterDto.getReminderDate() != null && !masterDto.getReminderDate().isEmpty() ? GlobalCommonDateUtils.getDate(masterDto.getReminderDate(), "dd-M-yyyy").getTime() : null);
			companyInvoiceReminderMaster.setRemark(masterDto.getRemark());
			companyInvoiceReminderMaster.setResponse(masterDto.getResponse());
			companyInvoiceReminderMaster = save(companyInvoiceReminderMaster);
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, companyInvoiceReminderMaster);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
			
		}
		
	}
	

	@Override
	public Response updateCompanyRemainderByCompanyInvoiceReminderId(CompanyInvoiceReminderMasterDto reqDto) throws ServiceException {
		try{
			CompanyInvoiceReminderMaster companyInvoiceReminderMaster = findById(reqDto.getCompanyInvoiceReminderId());
			if (companyInvoiceReminderMaster.getCompanyInvoiceReminderId()!= 0)
			{
				companyInvoiceReminderMaster.setResponse(reqDto.getResponse());
				companyInvoiceReminderMaster = update(companyInvoiceReminderMaster);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, companyInvoiceReminderMaster);
			}else {
				
				return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, companyInvoiceReminderMaster);
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	

}
