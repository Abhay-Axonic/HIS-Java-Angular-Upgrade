package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.MiscellaneousExpense;
import com.param.billing.global.model.MiscellaneousExpenseReciept;
import com.param.billing.global.model.MiscellaneousExpenseRecieptDetails;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDetailsDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.utility.TimeZoneComponent;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MiscellaneousExpenseDaoImpl implements IMiscellaneousExpenseDao, ICommonConstants {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IMpesaMasterDao iMpesaMasterDao;

	@Override
	public Response saveExpense(MiscellaneousExpenseDto miscellaneousExpenseDto) 
	{
		try {
			
			MiscellaneousExpense expense = new MiscellaneousExpense();
				expense.setAccountHead(miscellaneousExpenseDto.getAccountHead());
				expense.setAmount(miscellaneousExpenseDto.getAmount());
				expense.setBillDateTime(GlobalCommonDateUtils.getDate(miscellaneousExpenseDto.getBillDateTime(), "yyyy-MM-dd HH:mm:ss"));
				expense.setBillNumber(miscellaneousExpenseDto.getBillNumber());
				expense.setBillStatus(miscellaneousExpenseDto.getBillStatus());
				expense.setBillType(miscellaneousExpenseDto.getBillType());
				expense.setName(miscellaneousExpenseDto.getName());
				expense.setCreatedBy(miscellaneousExpenseDto.getCreatedBy());
				expense.setUpdatedBy(miscellaneousExpenseDto.getCreatedBy());
				expense.setOrgId(miscellaneousExpenseDto.getOrgId());
				expense.setUnitId(miscellaneousExpenseDto.getUnitId());
				expense.setRemark(miscellaneousExpenseDto.getRemark());
				expense.setSystemGenBillNumber(miscellaneousExpenseDto.getSystemGenBillNumber());
				expense.setStatus('A');
				expense.setIsCancelled('N');
				expense.setCreatedDate(TimeZoneComponent.getDateByzone(miscellaneousExpenseDto.getUnitId()));
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(miscellaneousExpenseDto.getUnitId()));
			int id = (int) sessionFactory.getCurrentSession().save(expense);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, id);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateExpense(MiscellaneousExpenseDto miscellaneousExpenseDto) 
	{
		try {
			
			MiscellaneousExpense expense = (MiscellaneousExpense) sessionFactory.getCurrentSession().get(MiscellaneousExpense.class,miscellaneousExpenseDto.getExpenseId());
				expense.setAccountHead(miscellaneousExpenseDto.getAccountHead());
				expense.setAmount(miscellaneousExpenseDto.getAmount());
				expense.setBillDateTime(GlobalCommonDateUtils.getDate(miscellaneousExpenseDto.getBillDateTime(), "yyyy-MM-dd HH:mm:ss"));
				expense.setBillNumber(miscellaneousExpenseDto.getBillNumber());
				expense.setBillStatus(miscellaneousExpenseDto.getBillStatus());
				expense.setBillType(miscellaneousExpenseDto.getBillType());
				expense.setName(miscellaneousExpenseDto.getName());
				expense.setRemark(miscellaneousExpenseDto.getRemark());
				expense.setUpdatedBy(miscellaneousExpenseDto.getCreatedBy());
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(miscellaneousExpenseDto.getUnitId()));
			sessionFactory.getCurrentSession().update(expense);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveExpenseReciept(MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try {
			
			MiscellaneousExpenseReciept miscellaneousExpenseReciept = new MiscellaneousExpenseReciept();
				miscellaneousExpenseReciept.setAmount(miscellaneousExpenseDto.getRecieptAmount());
				miscellaneousExpenseReciept.setExpenseId(miscellaneousExpenseDto.getExpenseId());
				miscellaneousExpenseReciept.setRecieptCancelled('N');
				miscellaneousExpenseReciept.setDateOfReciept(GlobalCommonDateUtils.getDate(miscellaneousExpenseDto.getBillDateTime(), "yyyy-MM-dd HH:mm:ss"));
				miscellaneousExpenseReciept.setCreatedBy(miscellaneousExpenseDto.getCreatedBy());
				miscellaneousExpenseReciept.setUpdatedBy(miscellaneousExpenseDto.getCreatedBy());
				miscellaneousExpenseReciept.setOrgId(miscellaneousExpenseDto.getOrgId());
				miscellaneousExpenseReciept.setUnitId(miscellaneousExpenseDto.getUnitId());
				miscellaneousExpenseReciept.setStatus('A');
				miscellaneousExpenseReciept.setCreatedDate(TimeZoneComponent.getDateByzone(miscellaneousExpenseDto.getUnitId()));
				miscellaneousExpenseReciept.setUpdatedDate(TimeZoneComponent.getDateByzone(miscellaneousExpenseDto.getUnitId()));
			int id = (int) sessionFactory.getCurrentSession().save(miscellaneousExpenseReciept);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, id);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveExpenseRecieptDetails(List<MiscellaneousExpenseRecieptDetailsDto> miscellaneousExpenseRecieptDetailsDtosList,Integer expenseRecieptId) {
		try {

			for(MiscellaneousExpenseRecieptDetailsDto miscellaneousExpenseRecieptDetailsDto : miscellaneousExpenseRecieptDetailsDtosList)
			{
				MiscellaneousExpenseRecieptDetails expenseRecieptDetails = new MiscellaneousExpenseRecieptDetails();
				
				expenseRecieptDetails.setAccountNumber(miscellaneousExpenseRecieptDetailsDto.getAccountNumber());
				expenseRecieptDetails.setAmount(miscellaneousExpenseRecieptDetailsDto.getAmount());
				expenseRecieptDetails.setBankId(miscellaneousExpenseRecieptDetailsDto.getBankId());
				expenseRecieptDetails.setCardHolderName(miscellaneousExpenseRecieptDetailsDto.getCardHolderName());
				expenseRecieptDetails.setCardNumber(miscellaneousExpenseRecieptDetailsDto.getCardNumber());
				expenseRecieptDetails.setCardTypeId(miscellaneousExpenseRecieptDetailsDto.getCardTypeId());
				if(miscellaneousExpenseRecieptDetailsDto.getChequeDate()!=null)
					expenseRecieptDetails.setChequeDate(GlobalCommonDateUtils.getDate(miscellaneousExpenseRecieptDetailsDto.getChequeDate(),"yyyy-MM-dd HH:mm:ss"));
				
				expenseRecieptDetails.setChequeNumber(miscellaneousExpenseRecieptDetailsDto.getChequeNumber());
				expenseRecieptDetails.setDateOfTransaction(TimeZoneComponent.getDateByzone(expenseRecieptDetails.getUnitId()));
				expenseRecieptDetails.setExpenseRecieptId(expenseRecieptId);
				if(miscellaneousExpenseRecieptDetailsDto.getExpiryDate()!=null)
					expenseRecieptDetails.setExpiryDate(GlobalCommonDateUtils.getDate(miscellaneousExpenseRecieptDetailsDto.getExpiryDate(),"yyyy-MM-dd HH:mm:ss"));
				
				expenseRecieptDetails.setHospitalAccountId(miscellaneousExpenseRecieptDetailsDto.getHospitalAccountId());
				expenseRecieptDetails.setOrgId(expenseRecieptDetails.getOrgId());
				expenseRecieptDetails.setUnitId(expenseRecieptDetails.getUnitId());
				expenseRecieptDetails.setPaymentModeId(miscellaneousExpenseRecieptDetailsDto.getPaymentModeId());
				expenseRecieptDetails.setTransactionNo(miscellaneousExpenseRecieptDetailsDto.getTransactionNo());
				expenseRecieptDetails.setTransactionTypeId(miscellaneousExpenseRecieptDetailsDto.getTransactionTypeId());
				if(miscellaneousExpenseRecieptDetailsDto.getTransTransactionDate()!=null)
					expenseRecieptDetails.setTransTransactionDate(GlobalCommonDateUtils.getDate(miscellaneousExpenseRecieptDetailsDto.getTransTransactionDate(),"yyyy-MM-dd HH:mm:ss"));
				
				expenseRecieptDetails.setIsCancle('N');
				expenseRecieptDetails.setStatus('A');
				expenseRecieptDetails.setCreatedBy(miscellaneousExpenseRecieptDetailsDto.getCreatedBy());
				expenseRecieptDetails.setCreatedDate(TimeZoneComponent.getDateByzone(expenseRecieptDetails.getUnitId()));
				expenseRecieptDetails.setUpdatedBy(miscellaneousExpenseRecieptDetailsDto.getUpdatedBy());
				expenseRecieptDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(expenseRecieptDetails.getUnitId()));
				
				MpesaMasterDto masterDto = new MpesaMasterDto();
				
				if (miscellaneousExpenseRecieptDetailsDto.getPaymentModeId() == 5  && (miscellaneousExpenseRecieptDetailsDto.getSkipMpesaValidation()!=null && miscellaneousExpenseRecieptDetailsDto.getSkipMpesaValidation())) 
				{
					masterDto.setMpesaTxcode(miscellaneousExpenseRecieptDetailsDto.getCardNo());
					masterDto.setUnitId(expenseRecieptDetails.getUnitId());
					masterDto.setOrganizationId(expenseRecieptDetails.getOrgId());
					masterDto.setUpdatedBy(miscellaneousExpenseRecieptDetailsDto.getCreatedBy());
					masterDto.setMpesaAmount(miscellaneousExpenseRecieptDetailsDto.getAmount());
					iMpesaMasterDao.validateMpesaTrancastion(masterDto);
					
				}
				
				sessionFactory.getCurrentSession().save(expenseRecieptDetails);
			}
			
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfExpenses(MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_EXPENSE_LIST").getQueryString());
			
			if(miscellaneousExpenseDto.getFromDate()!=null)
			{
				query.append( " 	and to_char(tme.bill_date_time,'YYYY-MM-DD') >=:fromDate "
						 	+ " 	and to_char(tme.bill_date_time,'YYYY-MM-DD') <=:toDate ");
			}
			
			if(miscellaneousExpenseDto.getBillType()!=null)
				query.append( " and tme.bill_type =:billType ");
			
			if(miscellaneousExpenseDto.getAccountHead()!=null)
				query.append(" and tme.account_head =:accountHead");
			
			if(miscellaneousExpenseDto.getBillStatus()!=null)
				query.append("	and tme.bill_status =:billStatus ");
			
			if(miscellaneousExpenseDto.getBillNumber()!=null && miscellaneousExpenseDto.getBillNumber()!= "")
				query.append("	and lower(tme.bill_number) like :billNumber ");
			
			query.append("order by 1 desc ");
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", miscellaneousExpenseDto.getOrgId())
					.setParameter("unitId", miscellaneousExpenseDto.getUnitId());

			if(miscellaneousExpenseDto.getFromDate()!=null) {
				sqlQuery.setParameter("fromDate", miscellaneousExpenseDto.getFromDate().substring(0, 10))
						.setParameter("toDate", miscellaneousExpenseDto.getToDate().substring(0,10));
			}

			if(miscellaneousExpenseDto.getBillType()!=null)
				sqlQuery.setParameter("billType", miscellaneousExpenseDto.getBillType());

			if(miscellaneousExpenseDto.getAccountHead()!=null)
				sqlQuery.setParameter("accountHead", miscellaneousExpenseDto.getAccountHead());

			if(miscellaneousExpenseDto.getBillStatus()!=null)
				sqlQuery.setParameter("billStatus", miscellaneousExpenseDto.getBillStatus());

			if(miscellaneousExpenseDto.getBillNumber()!=null && miscellaneousExpenseDto.getBillNumber()!= "")
				sqlQuery.setParameter("billNumber", "%" + miscellaneousExpenseDto.getBillNumber().toLowerCase() + "%");

			List<MiscellaneousExpenseDto> listBills = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(MiscellaneousExpenseDto.class))
					.list();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, null);
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getExpensesDetailsById(Integer expenseId) {
		try {
			List<MiscellaneousExpenseRecieptDto> miscellaneousExpenseRecieptDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXPENSE_RECIEPT_LIST")
					.setParameter("expenseId", expenseId)
					.setResultTransformer(Transformers.aliasToBean(MiscellaneousExpenseRecieptDto.class)).list();
			
			for(MiscellaneousExpenseRecieptDto expenseRecieptDto : miscellaneousExpenseRecieptDtosList)
			{
				List<MiscellaneousExpenseRecieptDetailsDto> miscellaneousExpenseRecieptDetailsDtosList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_EXPENSE_RECIEPT_DETAILS_LIST")
						.setParameter("expenseRecieptId", expenseRecieptDto.getExpenseRecieptId())
						.setResultTransformer(Transformers.aliasToBean(MiscellaneousExpenseRecieptDetailsDto.class)).list();
				
				expenseRecieptDto.setMiscellaneousExpenseRecieptDetailsDtosList(miscellaneousExpenseRecieptDetailsDtosList);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, miscellaneousExpenseRecieptDtosList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelExpenseById(MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try {
			
			MiscellaneousExpense expense = (MiscellaneousExpense) sessionFactory.getCurrentSession().get(MiscellaneousExpense.class,miscellaneousExpenseDto.getExpenseId());
				expense.setCancelledBy(miscellaneousExpenseDto.getCancelledBy());
				expense.setCancelledReasonId(miscellaneousExpenseDto.getCancelledReasonId());
				expense.setCancelRemark(miscellaneousExpenseDto.getCancelRemark());
				expense.setBillCancelDatetime(TimeZoneComponent.getDateByzone(miscellaneousExpenseDto.getUnitId()));
				expense.setUpdatedBy(miscellaneousExpenseDto.getCreatedBy());
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(miscellaneousExpenseDto.getUnitId()));
				expense.setBillStatus(3); //cancelled
				expense.setStatus('I');
				expense.setIsCancelled('Y');
			sessionFactory.getCurrentSession().update(expense);
			
			
			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_miscellaneous_expense_reciept set reciept_cancelled = 'Y', status='I' where expense_id =:expenseId ")
					.setParameter("expenseId" , miscellaneousExpenseDto.getExpenseId())
					.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "Cancelled Successfully..!!", null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelExpenseRecieptById(MiscellaneousExpenseRecieptDto miscellaneousExpenseRecieptDto) {
		try {
			
			MiscellaneousExpenseReciept expense = (MiscellaneousExpenseReciept) sessionFactory.getCurrentSession().get(MiscellaneousExpenseReciept.class,miscellaneousExpenseRecieptDto.getExpenseRecieptId());
				expense.setRecieptCancelBy(miscellaneousExpenseRecieptDto.getRecieptCancelBy());
				expense.setRecieptCancelReasonId(miscellaneousExpenseRecieptDto.getRecieptCancelReasonId());
				expense.setRecieptCancelRemark(miscellaneousExpenseRecieptDto.getRecieptCancelRemark());
				expense.setRecieptCancelDatetime(TimeZoneComponent.getDateByzone(miscellaneousExpenseRecieptDto.getUnitId()));
				expense.setUpdatedBy(miscellaneousExpenseRecieptDto.getCreatedBy());
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(miscellaneousExpenseRecieptDto.getUnitId()));
				expense.setStatus('I');
				expense.setRecieptCancelled('Y');
			sessionFactory.getCurrentSession().update(expense);
			
			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_miscellaneous_expense set bill_status = 1 where expense_id =:expenseId ")
					.setParameter("expenseId", miscellaneousExpenseRecieptDto.getExpenseId())
					.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "Cancelled successfully..!!", null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
