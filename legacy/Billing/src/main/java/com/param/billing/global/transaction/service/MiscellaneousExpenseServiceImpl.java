package com.param.billing.global.transaction.service;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.config.dao.IRequestDiscountDao;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDetailsDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDto;
import com.param.billing.global.transaction.config.dto.RequestApprovalDto;
import com.param.billing.global.transaction.dao.IMiscellaneousExpenseDao;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.utility.TimeZoneComponent;
@Service
@SuppressWarnings("rawtypes")
public class MiscellaneousExpenseServiceImpl implements IMiscellaneousExpenseService,ICommonConstants, IBillingCommonConstants {

	@Autowired
	IMiscellaneousExpenseDao iMiscellaneousExpenseDao;
	
	@Autowired
	IBillingMasterService iBillingMasterService;
	
	@Autowired
	IRequestDiscountDao iRequestDiscountDao;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	@Transactional
	public Response saveExpense(MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try{
			Double recieptAmount = 0.00;
			Double finalRecieptAmount = 0.00;
			if(miscellaneousExpenseDto.getMiscellaneousExpenseRecieptDetailsDtosList()!=null && miscellaneousExpenseDto.getMiscellaneousExpenseRecieptDetailsDtosList().size()>0)
			{
				for(MiscellaneousExpenseRecieptDetailsDto expenseRecieptDetailsDto : miscellaneousExpenseDto.getMiscellaneousExpenseRecieptDetailsDtosList())
				{
					recieptAmount = recieptAmount + expenseRecieptDetailsDto.getAmount();
				}
				if(miscellaneousExpenseDto.getExpenseId()!=null)
				{
					finalRecieptAmount = recieptAmount+(miscellaneousExpenseDto.getReceivedAmount() != null ? miscellaneousExpenseDto.getReceivedAmount() : 0.00);
					
					if((finalRecieptAmount).equals(miscellaneousExpenseDto.getAmount()))
						miscellaneousExpenseDto.setBillStatus(2);
					else
						miscellaneousExpenseDto.setBillStatus(1);
					
					iMiscellaneousExpenseDao.updateExpense(miscellaneousExpenseDto);
				}else{
					
					if(recieptAmount.equals(miscellaneousExpenseDto.getAmount()))
						miscellaneousExpenseDto.setBillStatus(2);
					else
						miscellaneousExpenseDto.setBillStatus(1);
					
					String billNo = iGlobalUnitMasterDao.getGeneratedNo(0, miscellaneousExpenseDto.getUnitId(), MIS_EXP);
					miscellaneousExpenseDto.setSystemGenBillNumber(billNo);
					
					Response expenseRes = iMiscellaneousExpenseDao.saveExpense(miscellaneousExpenseDto);
					miscellaneousExpenseDto.setExpenseId((Integer)expenseRes.getObject());
				}
				
				miscellaneousExpenseDto.setRecieptAmount(recieptAmount);
					
				Response expenseRecieptRes = iMiscellaneousExpenseDao.saveExpenseReciept(miscellaneousExpenseDto);
				Integer expenseRecieptId = (Integer)expenseRecieptRes.getObject();
				
				iMiscellaneousExpenseDao.saveExpenseRecieptDetails(miscellaneousExpenseDto.getMiscellaneousExpenseRecieptDetailsDtosList(),expenseRecieptId);

			}
						
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, miscellaneousExpenseDto.getExpenseId());
		}catch(Exception e){
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getListOfExpenses(MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try{
			return iMiscellaneousExpenseDao.getListOfExpenses(miscellaneousExpenseDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getExpensesDetailsById(Integer expenseId) {
		try{
			return iMiscellaneousExpenseDao.getExpensesDetailsById(expenseId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response cancelExpenseById(MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try{
			return iMiscellaneousExpenseDao.cancelExpenseById(miscellaneousExpenseDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response cancelReceiptById(MiscellaneousExpenseRecieptDto miscellaneousExpenseDto) {
		try{
			return iMiscellaneousExpenseDao.cancelExpenseRecieptById(miscellaneousExpenseDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response requetForApproval(MiscellaneousExpenseDto expenseRecieptDto) {
		try{
			
			for(RequestApprovalDto requestApprovalDto : expenseRecieptDto.getApprovalDtosList())
			{
				RequestDiscountDto requestDiscountDto = new RequestDiscountDto();
					requestDiscountDto.setOrganizationId(expenseRecieptDto.getOrgId());
					requestDiscountDto.setUnitId(expenseRecieptDto.getUnitId());
					requestDiscountDto.setRequestDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(expenseRecieptDto.getUnitId()), "dd-MM-yyyy HH:mm:ssss"));
					requestDiscountDto.setReqDisAmt(new BigDecimal(requestApprovalDto.getAmount()));
					requestDiscountDto.setRequestSentTo(requestApprovalDto.getRequestSentTo());
					requestDiscountDto.setRequestSentBy(expenseRecieptDto.getRequestSentBy());
					requestDiscountDto.setRequestNumber(iBillingMasterService.generateRequestNumber());
					requestDiscountDto.setCreatedBy(expenseRecieptDto.getRequestSentBy());
					requestDiscountDto.setUpdatedBy(expenseRecieptDto.getRequestSentBy());
					requestDiscountDto.setApprovedDiscAmt(new BigDecimal("0.00"));
					requestDiscountDto.setReqStatus(1); // pending
					requestDiscountDto.setRequestType(11); // For Expense Approval
					requestDiscountDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(expenseRecieptDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
					requestDiscountDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(expenseRecieptDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
					requestDiscountDto.setExpenseId(requestApprovalDto.getExpenseId());
				iRequestDiscountDao.requestDiscount(requestDiscountDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
