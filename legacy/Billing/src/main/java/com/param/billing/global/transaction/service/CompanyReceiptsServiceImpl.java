package com.param.billing.global.transaction.service;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.config.dao.IRequestDiscountDao;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsDto;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsExpenseRecieptDto;
import com.param.billing.global.transaction.config.dto.CompanyRecieptsReceiptDetailsDto;
import com.param.billing.global.transaction.config.dto.RequestApprovalDto;
import com.param.billing.global.transaction.dao.ICompanyReceiptsDao;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings("rawtypes")
public class CompanyReceiptsServiceImpl implements ICompanyReceiptsService, ICommonConstants{

	@Autowired
	ICompanyReceiptsDao iCompanyReceiptsDao;
	
	
	@Autowired
	IBillingMasterService iBillingMasterService;
	
	@Autowired
	IRequestDiscountDao iRequestDiscountDao;
	
	@Override
	@Transactional
	public Response saveCompanyReceipts(CompanyReceiptsDto companyReceiptsDto) {
		try{
			Double recieptAmount = 0.00;
			Double finalRecieptAmount = 0.00;
			if(companyReceiptsDto.getCompanyRecieptsReceiptDetailsDto()!=null && companyReceiptsDto.getCompanyRecieptsReceiptDetailsDto().size()>0)
			{
				for(CompanyRecieptsReceiptDetailsDto expenseRecieptDetailsDto : companyReceiptsDto.getCompanyRecieptsReceiptDetailsDto())
				{
					recieptAmount = recieptAmount + expenseRecieptDetailsDto.getAmount();
				}
				if(companyReceiptsDto.getCompanyReceiptsExpenseId()!=null)
				{
					finalRecieptAmount = recieptAmount+(companyReceiptsDto.getReceivedAmount() != null ? companyReceiptsDto.getReceivedAmount() : 0.00);
					
					if((finalRecieptAmount).equals(companyReceiptsDto.getAmount()))
						companyReceiptsDto.setBillStatus(2);
					else
						companyReceiptsDto.setBillStatus(1);
					
					iCompanyReceiptsDao.updateExpense(companyReceiptsDto);
				}else{
					
					if(recieptAmount.equals(companyReceiptsDto.getAmount()))
						companyReceiptsDto.setBillStatus(2);
					else
						companyReceiptsDto.setBillStatus(1);
					
					Response expenseRes = iCompanyReceiptsDao.saveExpense(companyReceiptsDto);
					companyReceiptsDto.setCompanyReceiptsExpenseId((Integer)expenseRes.getObject());
				}
				
				companyReceiptsDto.setRecieptAmount(recieptAmount);
					
				Response expenseRecieptRes = iCompanyReceiptsDao.saveExpenseReciept(companyReceiptsDto);
				Integer expenseRecieptId = (Integer)expenseRecieptRes.getObject();
				
				iCompanyReceiptsDao.saveExpenseRecieptDetails(companyReceiptsDto.getCompanyRecieptsReceiptDetailsDto(),expenseRecieptId);

			}
						
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, companyReceiptsDto.getCompanyReceiptsExpenseId());
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		//return null;
	}

	@Override
	@Transactional
	public Response getListOfExpenses(CompanyReceiptsDto companyReceiptsDto) {
		try{
			return iCompanyReceiptsDao.getListOfExpenses(companyReceiptsDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getExpensesDetailsById(Integer companyReceiptsExpenseId) {
		try{
			return iCompanyReceiptsDao.getExpensesDetailsById(companyReceiptsExpenseId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response cancelReceiptById(CompanyReceiptsExpenseRecieptDto companyReceiptsExpenseRecieptDto) {
		try{
			return iCompanyReceiptsDao.cancelExpenseRecieptById(companyReceiptsExpenseRecieptDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response cancelExpenseById(CompanyReceiptsDto companyReceiptsDto) {
		try{
			return iCompanyReceiptsDao.cancelExpenseById(companyReceiptsDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response requetForCancel(CompanyReceiptsDto companyReceiptsDto) {
		try{
			
			for(RequestApprovalDto requestApprovalDto : companyReceiptsDto.getApprovalDtosList())
			{
				RequestDiscountDto requestDiscountDto = new RequestDiscountDto();
					requestDiscountDto.setOrganizationId(companyReceiptsDto.getOrgId());
					requestDiscountDto.setUnitId(companyReceiptsDto.getUnitId());
					requestDiscountDto.setRequestDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()), "dd-MM-yyyy HH:mm:ssss"));
					requestDiscountDto.setReqDisAmt(new BigDecimal(requestApprovalDto.getAmount()));
					requestDiscountDto.setRequestSentTo(requestApprovalDto.getRequestSentTo());
					requestDiscountDto.setRequestSentBy(companyReceiptsDto.getRequestSentBy());
					requestDiscountDto.setRequestNumber(iBillingMasterService.generateRequestNumber());
					requestDiscountDto.setCreatedBy(companyReceiptsDto.getRequestSentBy());
					requestDiscountDto.setUpdatedBy(companyReceiptsDto.getRequestSentBy());
					requestDiscountDto.setApprovedDiscAmt(new BigDecimal("0.00"));
					requestDiscountDto.setReqStatus(1); // pending
					requestDiscountDto.setRequestType(13); // For Expense Approval
					requestDiscountDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
					requestDiscountDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
					requestDiscountDto.setCompanyReceiptsExpenseId(requestApprovalDto.getCompanyReceiptsExpenseId());
				iRequestDiscountDao.requestDiscount(requestDiscountDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response companyReceiptsByCompany(CompanyReceiptsDto companyReceiptsDto) {
		try{
			return iCompanyReceiptsDao.companyReceiptsByCompany(companyReceiptsDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response companyReceiptsConsumeptionDtls(CompanyReceiptsDto companyReceiptsDto) {
		try{
			return iCompanyReceiptsDao.companyReceiptsConsumeptionDtls(companyReceiptsDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
