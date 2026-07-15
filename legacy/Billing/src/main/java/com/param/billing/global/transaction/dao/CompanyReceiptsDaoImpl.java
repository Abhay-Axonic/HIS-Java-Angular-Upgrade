package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.CompanyReceiptsExpense;
import com.param.billing.global.model.CompanyReceiptsExpenseReciept;
import com.param.billing.global.model.CompanyReceiptsExpenseRecieptDetails;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsDto;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsExpenseRecieptDto;
import com.param.billing.global.transaction.config.dto.CompanyRecieptsReceiptDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class CompanyReceiptsDaoImpl extends GenericDao<CompanyReceiptsExpense, Integer> implements ICompanyReceiptsDao, ICommonConstants{
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	public CompanyReceiptsDaoImpl() {
		super(CompanyReceiptsExpense.class);
	}


	@Override
	public Response saveExpense(CompanyReceiptsDto companyReceiptsDto) {
		try {
			
			CompanyReceiptsExpense expense = new CompanyReceiptsExpense();
				expense.setAmount(companyReceiptsDto.getAmount());
				expense.setBalanceAmount(companyReceiptsDto.getAmount());
				expense.setBillDateTime(GlobalCommonDateUtils.getDate(companyReceiptsDto.getBillDateTime(), "yyyy-MM-dd HH:mm:ss"));
				expense.setBillNumber(companyReceiptsDto.getBillNumber());
				expense.setBillStatus(companyReceiptsDto.getBillStatus());
				expense.setCompanyTypeId(companyReceiptsDto.getCompanyTypeId());
				expense.setCompanyId(companyReceiptsDto.getCompanyId());
				expense.setInsuranceCompanyId(companyReceiptsDto.getInsuranceCompanyId());
				expense.setName(companyReceiptsDto.getName());
				expense.setCreatedBy(companyReceiptsDto.getCreatedBy());
				expense.setUpdatedBy(companyReceiptsDto.getCreatedBy());
				expense.setOrgId(companyReceiptsDto.getOrgId());
				expense.setUnitId(companyReceiptsDto.getUnitId());
				expense.setRemark(companyReceiptsDto.getRemark());
				expense.setStatus('A');
				expense.setCreatedDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()));
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()));
				expense.setCreditNoteId(companyReceiptsDto.getCreditNoteId());
			int id = (int) sessionFactory.getCurrentSession().save(expense);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, id);
		} catch (HibernateException he) {
			throw he;
			
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateExpense(CompanyReceiptsDto companyReceiptsDto) {
		try {
			
			CompanyReceiptsExpense expense = (CompanyReceiptsExpense) sessionFactory.getCurrentSession().get(CompanyReceiptsExpense.class,companyReceiptsDto.getCompanyReceiptsExpenseId());
				expense.setCompanyTypeId(companyReceiptsDto.getCompanyTypeId());
				expense.setAmount(companyReceiptsDto.getAmount());
				expense.setBalanceAmount(companyReceiptsDto.getAmount());
				expense.setBillDateTime(GlobalCommonDateUtils.getDate(companyReceiptsDto.getBillDateTime(), "yyyy-MM-dd HH:mm:ss"));
				expense.setBillNumber(companyReceiptsDto.getBillNumber());
				expense.setBillStatus(companyReceiptsDto.getBillStatus());
				expense.setCompanyId(companyReceiptsDto.getCompanyId());
				expense.setInsuranceCompanyId(companyReceiptsDto.getInsuranceCompanyId());
				expense.setName(companyReceiptsDto.getName());
				expense.setRemark(companyReceiptsDto.getRemark());
				expense.setUpdatedBy(companyReceiptsDto.getCreatedBy());
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()));
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
	public Response saveExpenseReciept(CompanyReceiptsDto companyReceiptsDto) {
	try {
			
		CompanyReceiptsExpenseReciept companyReceiptsExpenseReciept = new CompanyReceiptsExpenseReciept();
		companyReceiptsExpenseReciept.setAmount(companyReceiptsDto.getRecieptAmount());
		companyReceiptsExpenseReciept.setCompanyReceiptsExpenseId(companyReceiptsDto.getCompanyReceiptsExpenseId());
		companyReceiptsExpenseReciept.setRecieptCancelled('N');
		companyReceiptsExpenseReciept.setDateOfReciept(GlobalCommonDateUtils.getDate(companyReceiptsDto.getBillDateTime(), "yyyy-MM-dd HH:mm:ss"));
		companyReceiptsExpenseReciept.setCreatedBy(companyReceiptsDto.getCreatedBy());
		companyReceiptsExpenseReciept.setUpdatedBy(companyReceiptsDto.getCreatedBy());
		companyReceiptsExpenseReciept.setOrgId(companyReceiptsDto.getOrgId());
		companyReceiptsExpenseReciept.setUnitId(companyReceiptsDto.getUnitId());
		companyReceiptsExpenseReciept.setStatus('A');
		companyReceiptsExpenseReciept.setRecieptNumber(iGlobalUnitMasterDao.getGeneratedNo(0, companyReceiptsDto.getUnitId(), IBillingCommonConstants.COMPANY_RECEIPTS));
		companyReceiptsExpenseReciept.setCreatedDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()));
		companyReceiptsExpenseReciept.setUpdatedDate(TimeZoneComponent.getDateByzone(companyReceiptsDto.getUnitId()));
			int id = (int) sessionFactory.getCurrentSession().save(companyReceiptsExpenseReciept);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, id);
		} catch (HibernateException he) {
			throw he;
			
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveExpenseRecieptDetails(List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDtoList,
			Integer expenseRecieptId) {

		try {

			for(CompanyRecieptsReceiptDetailsDto companyRecieptsReceiptDetailsDto : companyRecieptsReceiptDetailsDtoList)
			{
				CompanyReceiptsExpenseRecieptDetails companyReceiptsExpenseRecieptDetails = new CompanyReceiptsExpenseRecieptDetails();
				
				companyReceiptsExpenseRecieptDetails.setAccountNumber(companyRecieptsReceiptDetailsDto.getAccountNumber());
				companyReceiptsExpenseRecieptDetails.setAmount(companyRecieptsReceiptDetailsDto.getAmount());
				companyReceiptsExpenseRecieptDetails.setBankId(companyRecieptsReceiptDetailsDto.getBankId());
				companyReceiptsExpenseRecieptDetails.setCardHolderName(companyRecieptsReceiptDetailsDto.getCardHolderName());
				//companyReceiptsExpenseRecieptDetails.setCardNumber(companyRecieptsReceiptDetailsDto.getCardNumber());
				companyReceiptsExpenseRecieptDetails.setCardNumber((companyRecieptsReceiptDetailsDto.getCardNo()!= null?companyRecieptsReceiptDetailsDto.getCardNo():companyRecieptsReceiptDetailsDto.getCardNumber()));
				companyReceiptsExpenseRecieptDetails.setCardTypeId(companyRecieptsReceiptDetailsDto.getCardTypeId());
				if(companyRecieptsReceiptDetailsDto.getChequeDate()!=null)
					companyReceiptsExpenseRecieptDetails.setChequeDate(GlobalCommonDateUtils.getDate(companyRecieptsReceiptDetailsDto.getChequeDate(),"yyyy-MM-dd HH:mm:ss"));
				
				companyReceiptsExpenseRecieptDetails.setChequeNumber(companyRecieptsReceiptDetailsDto.getChequeNumber());
				companyReceiptsExpenseRecieptDetails.setDateOfTransaction(TimeZoneComponent.getDateByzone(companyReceiptsExpenseRecieptDetails.getUnitId()));
				companyReceiptsExpenseRecieptDetails.setCompanyReceiptsExpenseRecieptId(expenseRecieptId);
				if(companyRecieptsReceiptDetailsDto.getExpiryDate()!=null)
					companyReceiptsExpenseRecieptDetails.setExpiryDate(GlobalCommonDateUtils.getDate(companyRecieptsReceiptDetailsDto.getExpiryDate(),"yyyy-MM-dd HH:mm:ss"));
				
				companyReceiptsExpenseRecieptDetails.setHospitalAccountId(companyRecieptsReceiptDetailsDto.getHospitalAccountId());
				companyReceiptsExpenseRecieptDetails.setOrgId(companyReceiptsExpenseRecieptDetails.getOrgId());
				companyReceiptsExpenseRecieptDetails.setUnitId(companyReceiptsExpenseRecieptDetails.getUnitId());
				companyReceiptsExpenseRecieptDetails.setPaymentModeId(companyRecieptsReceiptDetailsDto.getPaymentModeId());
				companyReceiptsExpenseRecieptDetails.setTransactionNo(companyRecieptsReceiptDetailsDto.getTransactionNo());
				companyReceiptsExpenseRecieptDetails.setTransactionTypeId(companyRecieptsReceiptDetailsDto.getTransactionTypeId());
				if(companyRecieptsReceiptDetailsDto.getTransTransactionDate()!=null)
					companyReceiptsExpenseRecieptDetails.setTransTransactionDate(GlobalCommonDateUtils.getDate(companyRecieptsReceiptDetailsDto.getTransTransactionDate(),"yyyy-MM-dd HH:mm:ss"));
				
				companyReceiptsExpenseRecieptDetails.setIsCancle('N');
				companyReceiptsExpenseRecieptDetails.setStatus('A');
				companyReceiptsExpenseRecieptDetails.setCreatedBy(companyRecieptsReceiptDetailsDto.getCreatedBy());
				companyReceiptsExpenseRecieptDetails.setCreatedDate(TimeZoneComponent.getDateByzone(companyReceiptsExpenseRecieptDetails.getUnitId()));
				companyReceiptsExpenseRecieptDetails.setUpdatedBy(companyRecieptsReceiptDetailsDto.getUpdatedBy());
				companyReceiptsExpenseRecieptDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(companyReceiptsExpenseRecieptDetails.getUnitId()));
				
				sessionFactory.getCurrentSession().save(companyReceiptsExpenseRecieptDetails);
			}
			
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getListOfExpenses(CompanyReceiptsDto companyReceiptsDto) {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_COMPANY_RECEIPTS_EXPENSE_LIST").getQueryString());
			
			if(companyReceiptsDto.getFromDate()!=null)
			{
				query.append( " and to_char(tme.bill_date_time,'YYYY-MM-DD') >=:fromDate "
						 	+ " and to_char(tme.bill_date_time,'YYYY-MM-DD') <=:toDate ");
			}
			
			if(companyReceiptsDto.getCompanyTypeId()!=null)
				query.append( " and tme.company_type_id =:companyTypeId ");
			
			if(companyReceiptsDto.getCompanyId()!=null)
				query.append( " and tme.company_id =:companyId ");
			
			if(companyReceiptsDto.getInsuranceCompanyId()!=null)
				query.append( " and tme.insurance_company_id =:insuranceCompanyId ");
			
			
			if(companyReceiptsDto.getBillStatus()!=null)
				query.append("	and tme.bill_status =:billStatus");
			
			if(companyReceiptsDto.getBillNumber()!=null && companyReceiptsDto.getBillNumber()!= "")
				query.append("	and lower(tme.bill_number) like '%"+companyReceiptsDto.getBillNumber().toLowerCase()+"%' ");
			
			query.append("order by 1 desc ");
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", companyReceiptsDto.getOrgId())
					.setParameter("unitId", companyReceiptsDto.getUnitId());

			if(companyReceiptsDto.getFromDate()!=null) {
				sqlQuery.setParameter("fromDate", companyReceiptsDto.getFromDate().substring(0,10));
				sqlQuery.setParameter("toDate", companyReceiptsDto.getToDate().substring(0,10));
			}

			if(companyReceiptsDto.getCompanyTypeId()!=null)
				sqlQuery.setParameter("companyTypeId", companyReceiptsDto.getCompanyTypeId());

			if(companyReceiptsDto.getCompanyId()!=null)
				sqlQuery.setParameter("companyId", companyReceiptsDto.getCompanyId());

			if(companyReceiptsDto.getInsuranceCompanyId()!=null)
				sqlQuery.setParameter("insuranceCompanyId", companyReceiptsDto.getInsuranceCompanyId());

			if(companyReceiptsDto.getBillStatus()!=null)
				sqlQuery.setParameter("billStatus", companyReceiptsDto.getBillStatus());

			if(companyReceiptsDto.getBillNumber() !=null && !companyReceiptsDto.getBillNumber().isEmpty())
				sqlQuery.setParameter("billNumber", "%" + companyReceiptsDto.getBillNumber().toLowerCase() + "%");

			List<CompanyReceiptsDto> listBills = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(CompanyReceiptsDto.class))
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
	public Response getExpensesDetailsById(Integer companyReceiptsExpenseId) {
		try {
			List<CompanyReceiptsExpenseRecieptDto> companyReceiptsExpenseRecieptDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_RECEIPTS_EXPENSE_RECIEPT_LIST")
					.setParameter("expenseId", companyReceiptsExpenseId)
					.setResultTransformer(Transformers.aliasToBean(CompanyReceiptsExpenseRecieptDto.class)).list();
			
			for(CompanyReceiptsExpenseRecieptDto expenseRecieptDto : companyReceiptsExpenseRecieptDtoList)
			{
				List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDtoList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_COMPANY_RECEIPTS_EXPENSE_RECIEPT_DETAILS_LIST")
						.setParameter("expenseRecieptId", expenseRecieptDto.getCompanyReceiptsExpenseRecieptId())
						.setResultTransformer(Transformers.aliasToBean(CompanyRecieptsReceiptDetailsDto.class)).list();
				
				expenseRecieptDto.setCompanyRecieptsReceiptDetailsDtoList(companyRecieptsReceiptDetailsDtoList);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, companyReceiptsExpenseRecieptDtoList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response cancelExpenseRecieptById(CompanyReceiptsExpenseRecieptDto companyReceiptsExpenseRecieptDto) {
		try {
			
			CompanyReceiptsExpenseReciept expense = (CompanyReceiptsExpenseReciept) sessionFactory.getCurrentSession().get(CompanyReceiptsExpenseReciept.class,companyReceiptsExpenseRecieptDto.getCompanyReceiptsExpenseRecieptId());
				expense.setRecieptCancelBy(companyReceiptsExpenseRecieptDto.getRecieptCancelBy());
				expense.setRecieptCancelReasonId(companyReceiptsExpenseRecieptDto.getRecieptCancelReasonId());
				expense.setRecieptCancelRemark(companyReceiptsExpenseRecieptDto.getRecieptCancelRemark());
				expense.setRecieptCancelDatetime(TimeZoneComponent.getDateByzone(companyReceiptsExpenseRecieptDto.getUnitId()));
				expense.setUpdatedBy(companyReceiptsExpenseRecieptDto.getCreatedBy());
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(companyReceiptsExpenseRecieptDto.getUnitId()));
				expense.setStatus('I');
				expense.setRecieptCancelled('Y');
			sessionFactory.getCurrentSession().update(expense);
			
			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_company_receipts_expense set bill_status = 1 where company_receipts_expense_id =:companyReceiptsExpenseId ")
					.setParameter("companyReceiptsExpenseId", companyReceiptsExpenseRecieptDto.getCompanyReceiptsExpenseId())
					.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "Cancelled successfully..!!", null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response cancelExpenseById(CompanyReceiptsDto companyReceiptsDto) {
		/*try {
			
			CompanyReceiptsExpense expense = (CompanyReceiptsExpense) sessionFactory.getCurrentSession().get(CompanyReceiptsExpense.class,companyReceiptsDto.getCompanyReceiptsExpenseId());
				expense.setCancelledBy(companyReceiptsDto.getCancelledBy());
				expense.setCancelledReasonId(companyReceiptsDto.getCancelledReasonId());
				expense.setCancelRemark(companyReceiptsDto.getCancelRemark());
				expense.setBillCancelDatetime(TimeZoneComponent.getDateByzone(unitId));
				expense.setUpdatedBy(companyReceiptsDto.getCreatedBy());
				expense.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
				expense.setBillStatus(3); //cancelled
				expense.setStatus('I');
				expense.setIsCancelled('Y');
			sessionFactory.getCurrentSession().update(expense);
			
			
			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_miscellaneous_expense_reciept set reciept_cancelled = 'Y', status='I' where expense_id =" + miscellaneousExpenseDto.getExpenseId())
					.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, "Cancelled Successfully..!!", null, null);
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);*/
		return null;
	}


	@Override
	public Response companyReceiptsByCompany(CompanyReceiptsDto companyReceiptsDto) {
		try {
			
			String query = " select " 
								 + " 	company_receipts_expense_id as \"companyReceiptsExpenseId\", " 
								 + " 	company_id as \"companyId\",  " 
								 + " 	insurance_company_id as \"insuranceCompanyId\", " 
								 + " 	amount as \"amount\", " 
								 + " 	balance_amount as \"balanceAmount\", " 
								 + " 	balance_amount as \"oldBalanceAmount\", " 
								 + " 	(amount-balance_amount) as \"consumedAmount\", " 
								 + " 	(amount-balance_amount) as \"oldConsumedAmount\", "
								 + " 	bill_number as \"billNumber\", " 
								 + " 	to_char(bill_date_time,'YYYY-MM-DD' )as \"billDateTime\" " 
								 + " from " 
								 + " 	billing.t_company_receipts_expense " 
								 + " where 	 " 
								 + " 	balance_amount > 0 " 
								 + " 	and status ='A' " 
								 + " 	and bill_status = 2 ";
			
			
			if(companyReceiptsDto.getCompanyId() != null && companyReceiptsDto.getCompanyId() > 0){
				query = query.concat(" and company_id =:companyId ");
			}
			
			if(companyReceiptsDto.getInsuranceCompanyId() != null && companyReceiptsDto.getInsuranceCompanyId()  > 0){
				query = query.concat(" and insurance_company_id =:insuranceCompanyId ");
			}
											
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());

			if(companyReceiptsDto.getCompanyId() != null && companyReceiptsDto.getCompanyId() > 0)
				sqlQuery.setParameter("companyId", companyReceiptsDto.getCompanyId());

			if(companyReceiptsDto.getInsuranceCompanyId() != null && companyReceiptsDto.getInsuranceCompanyId()  > 0)
				sqlQuery.setParameter("insuranceCompanyId", companyReceiptsDto.getInsuranceCompanyId());

			List<CompanyReceiptsDto> listBills = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(CompanyReceiptsDto.class))
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
	public Response companyReceiptsConsumeptionDtls(CompanyReceiptsDto companyReceiptsDto) {
		try {
			String query = "select "
					+ "	mst.company_receipt_number as \"billNumber\", "
					+ "	to_char(mst.company_receipt_date,'DD-MM-YYYY') as \"billDateTime\", "
					+ "	cast(cast(tpdcd.consumed_amount as numeric(10,2))as float8) as \"consumedAmount\" "
					+ "from "
					+ "	billing.t_payment_deposite_consumption_details as tpdcd "
					+ "inner join billing.m_company_receipt_master mst on "
					+ "	mst.deposit_consumption_id = tpdcd.dep_consumption_id "
					+ "where "
					+ "	tpdcd.company_receipts_expense_id =:companyReceiptsExpenseId";
			List<CompanyReceiptsDto> listBills=	sessionFactory.getCurrentSession().createSQLQuery(query)
			.setParameter("companyReceiptsExpenseId", companyReceiptsDto.getCompanyReceiptsExpenseId())
			.setResultTransformer(Transformers.aliasToBean(CompanyReceiptsDto.class))
			.list();
					
				/*				
				List<CompanyReceiptsDto> listBills = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setInteger("companyReceiptsExpenseId", companyReceiptsDto.getCompanyReceiptsExpenseId())
						.setResultTransformer(Transformers.aliasToBean(CompanyReceiptsDto.class))
						.list();*/
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listBills, null);
		}catch(Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public List<CompanyReceiptsExpense> getCompanyReceiptsExpenseByCreditNoteId(Integer creditNoteId)	throws ApplicationException {
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(CompanyReceiptsExpense.class);
			c.add(Restrictions.eq("creditNoteId", creditNoteId));
			c.add(Restrictions.eq("status", 'A'));
			List<CompanyReceiptsExpense> billDetailsList = c.list();

			return billDetailsList;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public void inactiveComapnyReciept(Integer companyReceiptsExpenseId) {
		try {
			
			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_company_receipts_expense set bill_status = 3 where company_receipts_expense_id =:companyReceiptsExpenseId ")
					.setParameter("companyReceiptsExpenseId", companyReceiptsExpenseId)
					.executeUpdate();
			
			sessionFactory.getCurrentSession().createSQLQuery(
					"update billing.t_company_receipts_expense_reciept "
					+ "	set status = 'I', "
					+ "	reciept_cancelled='Y',"
					+ "	reciept_cancel_remark='Credit Note Cancelled' "
					+ "	where company_receipts_expense_id =:companyReceiptsExpenseId ")
					.setParameter("companyReceiptsExpenseId", companyReceiptsExpenseId)
					.executeUpdate();
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		
	}


}
