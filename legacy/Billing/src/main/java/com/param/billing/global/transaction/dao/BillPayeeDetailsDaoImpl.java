
package com.param.billing.global.transaction.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.billing.global.transaction.dto.BillListDetailsCNDto;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.common.Response;
import com.param.global.dto.BillPayeeDetailsDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BillPayeeDetailsDaoImpl extends GenericDao<BillPayeeDetails, Integer> implements IBillPayeeDetailsDao, ICommonConstants,IError{

	public BillPayeeDetailsDaoImpl() {
		super(BillPayeeDetails.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public Response saveBillPayeeDetails(BillPayeeDetailsDto billPayeeDetailsDto) throws ApplicationException {
		try {
			BillPayeeDetails billPayeeDetails = mapper.map(billPayeeDetailsDto, BillPayeeDetails.class, "BillPayeeDetailsDto_to_BillPayeeDetails");
			billPayeeDetails = save(billPayeeDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, billPayeeDetails.getBillPayeeDetailsId());
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getBillPayeeDetailsByBillId(int billId) throws BillingException {
		try {
			List<BillPayeeDetailsResDto> listBillPayeeDetailsResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PAYEE_DETAILS_BY_BILL_ID").setParameter("billId", billId).setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillPayeeDetailsResDto, null);
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateDueAmtByPayeeAndBillId(int billId, int payeeId, double dueAmount,double paidAmount) throws BillingException {
		try {
			sessionFactory.getCurrentSession().getNamedQuery("UPDATE_DUE_AMOUNT_BY_BILL_AND_PAYEE")
			.setParameter("dueAmount", dueAmount)
			.setParameter("paidAmount", paidAmount)
			.setParameter("billId", billId)
			.setParameter("payeeId", payeeId)
			.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateCrAmtByPayeeAndBillId(int billId, int payeeId, double crAmount) throws BillingException {
		try {
			sessionFactory.getCurrentSession().getNamedQuery("UPDATE_CR_AMOUNT_BY_BILL_AND_PAYEE").setParameter("crAmount", crAmount).setParameter("billId", billId).setParameter("payeeId", payeeId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateBillPayeeDueAmountForCompanyInvoice(int billId, int payeeId,int associateCompanyId, double amount,double disallowedAmount,double otherDeduction)throws BillingException {
		try {
			
			StringBuilder query = new StringBuilder("update billing.t_bill_payee_details "
					 		+ " set due_amount = (cast(coalesce(round(cast(due_amount as numeric), 2), 0) as double precision) - (:amount + :disallowedAmount + :otherDeduction) ), "
					 		+ "	paid_amount = (paid_amount + :amount), "
					 		+ " disallowed_amount = coalesce(disallowed_amount,0) + :disallowedAmount, "
					 		+ " other_deduction= coalesce(other_deduction,0) + :otherDeduction "
					 		+ " where bill_id=:billId "
					 		+ " and payee_id=:payeeId "
					 		+ " and status='A' ");
			if(associateCompanyId>0)
				query.append(" and associate_company_id =:associateCompanyId ");
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("billId", billId)
					.setParameter("payeeId", payeeId)
					.setParameter("amount", amount)
					.setParameter("disallowedAmount", disallowedAmount)
					.setParameter("otherDeduction", otherDeduction);

			if(associateCompanyId>0)
				sqlQuery.setParameter("associateCompanyId", associateCompanyId);

			Integer result = sqlQuery.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (Exception e) { 
			
			throw e;
		}
	}
	
	
	//Ganesh
	@Override
	public Response updatePharamcyBillPayeeDueAmountForCompanyInvoice(int saleId, int payeeId,int associateCompanyId, double amount,double disallowedAmount,double otherDeduction)throws BillingException {
		try {
			
			StringBuilder query =new StringBuilder(" update  " 
					 + " 	pharmacy.t_pharmacy_bill_payee_details  " 
					 + "  set " 
					 + " 	due_amount = (due_amount - :amount), "
					 + " 	disallowed_amount = coalesce(disallowed_amount,0) + :disallowedAmount, "
				 	 + " 	other_deduction= coalesce(other_deduction,0) + :otherDeduction "
					 + "  where  " 
					 + " 	t_sale_id =:saleId  " 
					 + " 	and payee_id =:payeeId "); 
			if(associateCompanyId>0)
				query.append(" and associate_company_id =:associateCompanyId ");
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("saleId", saleId)
					.setParameter("payeeId", payeeId)
					.setParameter("disallowedAmount", disallowedAmount)
					.setParameter("amount", amount)
					.setParameter("otherDeduction", otherDeduction);

			if(associateCompanyId>0)
				sqlQuery.setParameter("associateCompanyId", associateCompanyId);

			Integer result = sqlQuery.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		} catch (Exception e) { 
			
			throw e;
		}
	}
	
	@Override
	public Response updatePayeeDetailsByPayeeAndBillId(BillPayeeDetailsResDto billPayeeDetailsResDto) throws BillingException {
		try {
			
			String query = "update billing.t_bill_payee_details "
			 				+" set due_amount =due_amount+:dueAmount, "
			 				+ "bill_amount=bill_amount+:dueAmount, "
			 				+ "updated_date=now() "
			 				+" where bill_id=:billId and payee_id=:payeeId and status='A '";
			
			sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("dueAmount", billPayeeDetailsResDto.getBillAmount())
				.setParameter("billId", billPayeeDetailsResDto.getBillId())
				.setParameter("payeeId", billPayeeDetailsResDto.getPayeeId())
			.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getBillPayeeObjByBillId(int billId) throws BillingException {
		try {
			
			Criteria c=sessionFactory.getCurrentSession().createCriteria(BillPayeeDetails.class); 
				c.add(Restrictions.eq("billId", billId));
				c.add(Restrictions.eq("status", 'A'))
			.setResultTransformer(Transformers.aliasToBean(BillPayeeDetails.class)).list();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, c.list(), null);
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updatePayeeDetailsObject(BillPayeeDetails billPayeeDetails) {
		try {
			
			sessionFactory.getCurrentSession().update(billPayeeDetails);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) { 
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getCreditApproveStatus(Integer billingDetailsId) throws BillingException {
		try
        {
			BillListDetailsCNDto dto = (BillListDetailsCNDto) sessionFactory.getCurrentSession().createSQLQuery(
				" select creditnote21_.is_approved as \"creditApproved\" " + 
				"	from billing.t_credit_note_detail creditnote20_ " + 
				"	inner join billing.m_credit_note_master creditnote21_ on " + 
				"		creditnote20_.credit_note_id = creditnote21_.credit_note_id " + 
				"	where creditnote20_.billing_details_id =:billingDetailsId " +
				"		and creditnote21_.is_approved not in ('R')  order by  creditnote20_.credit_note_detail_id desc limit 1  ")
					.setParameter("billingDetailsId", billingDetailsId)
				.setResultTransformer(Transformers.aliasToBean(BillListDetailsCNDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		
        } catch (Exception e)
        {
            
            throw e;
        }
     //   return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getCreditPayeeDueAmount(Integer billId, Integer payeeId) throws BillingException {
		try
        {
			BillPayeeDetailsDto dto = (BillPayeeDetailsDto) sessionFactory.getCurrentSession().createSQLQuery(
				" 	 select cast(due_amount as float)  as \"dueAmount\" from  billing.t_bill_payee_details  " + 
				" where bill_id =:billId  " +
				" and payee_id =:payeeId " +
				" and status='A'  ")
				.setParameter("billId", billId)
				.setParameter("payeeId", payeeId)
				.setResultTransformer(Transformers.aliasToBean(BillPayeeDetailsDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}

	@Override
	public Double getSelfDueAmount(Integer saleId, Integer payeeId) {
				 String sql ="select  cast(sum(due_amount) as float) from pharmacy.t_pharmacy_bill_payee_details  " 
				 + "  where  " 
				 + " 	t_sale_id =:saleId  " 
				 + " 	and payee_id !=:payeeId "; 
		
			        SQLQuery query = getSession().createSQLQuery(sql);
			        query.setParameter("saleId", saleId);
			        query.setParameter("payeeId", payeeId);
			        return (Double) query.uniqueResult();
	}

}

