package com.param.billing.global.transaction.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.dto.CompanyReceiptMasterDto;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.dto.BillingPaymentDetailsReqDto;
import com.param.billing.global.model.PaymentDepositConsumption;
import com.param.billing.global.model.PaymentDepositConsumptionDetails;
import com.param.billing.global.transaction.config.dto.MCompanyInvoiceMasterDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceDetailsDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementReqDto;
import com.param.billing.master.dto.PaymentDepositConsumptionDetailsDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PaymentDepositConsumptionDaoImpl extends GenericDao<PaymentDepositConsumption,Integer>implements IPaymentDepositConsumptionDao,ICommonConstants{

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IDepositMasterService iDepositMasterService;
	
	public PaymentDepositConsumptionDaoImpl() {
		super(PaymentDepositConsumption.class);
	}
	
	
	@Override
	public Response addDepositConsumption(BillPaymentCollectionReqDto reqDto) throws ApplicationException {
		try{
				PaymentDepositConsumption paymentDepositConsumption = new PaymentDepositConsumption();
					paymentDepositConsumption.setOrganizationId(reqDto.getOrgnizationId());
					paymentDepositConsumption.setUnitId(reqDto.getUnitId());
					paymentDepositConsumption.setDepConsumptionDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()),"dd-M-yyyy HH:mm:ss"));
					paymentDepositConsumption.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()),"dd-M-yyyy HH:mm:ss"));
					paymentDepositConsumption.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()),"dd-M-yyyy HH:mm:ss"));
					paymentDepositConsumption.setUpdatedBy(reqDto.getCreatedBy());
					paymentDepositConsumption.setCreatedBy(reqDto.getCreatedBy());
					paymentDepositConsumption.setBillId(reqDto.getBillingMasterId());
					paymentDepositConsumption.setBillPayeeDetailsId(reqDto.getBillPayeeDetailsId());
					paymentDepositConsumption.setConsumedAmount(reqDto.getTotalDepositConsumption());
					paymentDepositConsumption.setDepositeConsumptionNumber(reqDto.getDepositeConsumptionNumber());
					paymentDepositConsumption.setCounterId(reqDto.getCounterId());
					paymentDepositConsumption.setStatus('A');
				paymentDepositConsumption=save(paymentDepositConsumption);
			
			return new Response(SUCCESS,SUCCESS_CODE,COMMON_SUCCESS,null,paymentDepositConsumption.getDepConsumptionId());
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR,COMMON_ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
	}


	@Override
	public Response addDepositConsumptionDetails(PaymentDepositDetailsDto paymentDepositDetailsDto)
			throws ApplicationException {
		try{
			
			PaymentDepositConsumptionDetails paymentDepositConsumptionDetails = new PaymentDepositConsumptionDetails();
				paymentDepositConsumptionDetails.setOrganizationId(paymentDepositDetailsDto.getOrganizationId());
				paymentDepositConsumptionDetails.setUnitId(paymentDepositDetailsDto.getUnitId());
				paymentDepositConsumptionDetails.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(paymentDepositDetailsDto.getUnitId()),"dd-M-yyyy HH:mm:ss"));
				paymentDepositConsumptionDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(paymentDepositDetailsDto.getUnitId()),"dd-M-yyyy HH:mm:ss"));
				paymentDepositConsumptionDetails.setUpdatedBy(paymentDepositDetailsDto.getUpdatedBy());
				paymentDepositConsumptionDetails.setCreatedBy(paymentDepositDetailsDto.getUpdatedBy());
				paymentDepositConsumptionDetails.setConsumedAmount(paymentDepositDetailsDto.getDepositConsumed());
				paymentDepositConsumptionDetails.setDepositId(paymentDepositDetailsDto.getDepositId());
				paymentDepositConsumptionDetails.setDepConsumptionId(paymentDepositDetailsDto.getDepConsumptionId());
				paymentDepositConsumptionDetails.setStatus('A');
				paymentDepositConsumptionDetails.setRemark(paymentDepositDetailsDto.getRemark());
				paymentDepositConsumptionDetails.setCompanyReceiptsExpenseId(paymentDepositDetailsDto.getCompanyReceiptsExpenseId());
			sessionFactory.getCurrentSession().save(paymentDepositConsumptionDetails);
			
			return new Response(SUCCESS,SUCCESS_CODE,COMMON_SUCCESS,null,null);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR,COMMON_ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
	}


	@Override
	public Long getNextValDepositeConsumptionId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery("select nextval('billing.payment_deposite_consumption_seq')");
				Long id = ((BigInteger) query.uniqueResult()).longValue();

				SQLQuery query2 = session.createSQLQuery("select setval('billing.payment_deposite_consumption_seq'," + (id-1) + ")");
				query2.uniqueResult();

				return id;
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}
	@Override
	public List< BillPaymentCollectionReqDto>  getPaymentRecieptDetails(Integer billId) throws ApplicationException {
		try {
			List<BillPaymentCollectionReqDto> detailsByBillIdDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAYMENT_RECIEPTS_AGAINST_BILL")
					.setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(BillPaymentCollectionReqDto.class)).list();
			return detailsByBillIdDtosList;
		} catch (Exception e) {
			throw e;
		}
		//return null;
	}


	@Override
	public Response addDepositAfterCancelReciept(Integer depositConsumptionId) {
			try {
			
				Double balanceConsumedAmount = 0.0;
				Double amountNeedToAdd = 0.0;
				
				Criteria c=sessionFactory.getCurrentSession().createCriteria(PaymentDepositConsumptionDetails.class); 
				c.add(Restrictions.eq("depConsumptionId", depositConsumptionId));
				c.add(Restrictions.eq("status", 'A'));
				List<PaymentDepositConsumptionDetails> billDetailsList = c.list();
				
				
				for(PaymentDepositConsumptionDetails consumptionDetails : billDetailsList)
				{
					sessionFactory.getCurrentSession()
							.createSQLQuery(
									"  update billing.m_deposit_master set available_deposit = available_deposit + :amt, "
											+ " consumed_deposit = consumed_deposit - :amt "
											+ " where deposit_master_id = :depositMasterId")
							.setParameter("depositMasterId", consumptionDetails.getDepositId())
							.setParameter("amt", new Double(OrderMasterServiceImpl.df2.format(consumptionDetails.getConsumedAmount())))
						.executeUpdate();
					
					
					Response res3 = iDepositMasterService.getDepositDetailsByDepositId(consumptionDetails.getDepositId());
					
					List<PaymentDepositDetailsDto> detailsByDepositIdDtosList = res3.getListObject();
					ListIterator<PaymentDepositDetailsDto> listIterator = detailsByDepositIdDtosList.listIterator();
					
					balanceConsumedAmount =  consumptionDetails.getConsumedAmount();
					
					if (consumptionDetails.getConsumedAmount() > 0) 
					{
						while (listIterator.hasNext()) 
						{
							PaymentDepositDetailsDto paymentDepositDetailsDto = listIterator.next();
	
							paymentDepositDetailsDto.setDepConsumptionId(depositConsumptionId);
	
							Double detailsAmount = Math.round(paymentDepositDetailsDto.getAmount() * 100.0) / 100.0;
	
							if(balanceConsumedAmount >= paymentDepositDetailsDto.getConsumedAmount())
							{
								paymentDepositDetailsDto.setBalanceAmount(paymentDepositDetailsDto.getBalanceAmount() + paymentDepositDetailsDto.getConsumedAmount());
								paymentDepositDetailsDto.setConsumedAmount(0.00);
								
								balanceConsumedAmount = balanceConsumedAmount - detailsAmount;
								
							}else {
								paymentDepositDetailsDto.setBalanceAmount(paymentDepositDetailsDto.getBalanceAmount() + balanceConsumedAmount);
								paymentDepositDetailsDto.setConsumedAmount(paymentDepositDetailsDto.getConsumedAmount() - balanceConsumedAmount);
								
								balanceConsumedAmount = 0.00;
							}
	
							iDepositMasterService.updateDepositDetails(paymentDepositDetailsDto);
	
						}
					}
						consumptionDetails.setStatus('I');
					sessionFactory.getCurrentSession().update(consumptionDetails);
					
				}
				
				
				sessionFactory.getCurrentSession()
				.createSQLQuery(
						"  update billing.t_payment_deposite_consumption set status='I' "
								+ " where dep_consumption_id = :depConsumptionId")
				.setParameter("depConsumptionId",depositConsumptionId)
			.executeUpdate();
				
				
			return new Response<>(SUCCESS, SUCCESS_CODE, null, billDetailsList, null);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
		}


	@Override
	public Response getBillPharmacyPayeeByBillIdCreditNote(int saleId) throws ApplicationException {
		try {
			String sqlQuery1 = " select count(id)  from pharmacy.t_return where sale_id=:saleId ";
			BigInteger count=(BigInteger) getSession().createSQLQuery(sqlQuery1.toString())
					.setParameter("saleId", saleId)
					.uniqueResult();
			boolean isPharmacyReturn;
			if(count.intValue()>0)
			{
				 isPharmacyReturn=true;
			}
			else
			{
				isPharmacyReturn=false;
			}
			List<BillPaymentCollectionReqDto> detailsByBillIdDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PHARMACY_PAYMENT_RECIEPTS_AGAINST_BILL")
					.setParameter("saleId", saleId)
					.setResultTransformer(Transformers.aliasToBean(BillPaymentCollectionReqDto.class)).list();
			for (BillPaymentCollectionReqDto billPaymentCollectionReqDto : detailsByBillIdDtosList) {
				billPaymentCollectionReqDto.setIsPharmacyReturn(isPharmacyReturn);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, detailsByBillIdDtosList, null);
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public Response cancelPharmacyReceipt(BillPaymentCollectionReqDto dto) throws ApplicationException {
		try{	
			
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date currentDate = new Date();
			Timestamp timestamp = new Timestamp(currentDate.getTime());
			Integer count = null;
			if(dto.getReceiptType() == 1) {
				
				String sqlQuery = " select organization_id  from pharmacy.t_sale_payment_mode where id =:paymentReceiptId and is_active = false ";

				 count = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery.toString())
						 .setParameter("paymentReceiptId", dto.getPaymentRecieptId())
						 .uniqueResult();
				
			}else {
				
				String sqlQuery = " select organization_id from billing.t_payment_deposite_consumption   where status  = 'I' and dep_consumption_id = :PaymentRecieptId  ";

				 count = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery.toString())
						.setParameter("PaymentRecieptId", dto.getPaymentRecieptId()).uniqueResult();
				
			}
				
			if(count != null) {
				
				return new Response(ERROR, COMMON_ERROR_CODE, "This receipt already cancel by other user", null, null);
			}	
			
			String sqlQuery = " select deposit_amount as \"depositAmount\" from billing.m_deposit_master where pharmacy_sale_mode_id=:paymentReceiptId  and status ='A' ";

			BillPaymentCollectionReqDto Obj1 = (BillPaymentCollectionReqDto)sessionFactory.getCurrentSession().createSQLQuery(sqlQuery.toString())
					.setParameter("paymentReceiptId", dto.getPaymentRecieptId())
					.setResultTransformer(Transformers.aliasToBean(BillPaymentCollectionReqDto.class)).uniqueResult();
			
			String query = " select bill_amount as \"totalBillAmount\" , due_amount as \"payeeDueAmount\" from pharmacy.t_pharmacy_bill_payee_details where t_sale_id = :saleId and payee_id  = :payeeId  and status ='A' ";

			BillPaymentCollectionReqDto Obj = (BillPaymentCollectionReqDto)sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("saleId", dto.getSaleId())
					.setParameter("payeeId", dto.getPayeeId())
					.setResultTransformer(Transformers.aliasToBean(BillPaymentCollectionReqDto.class)).uniqueResult();
			if(Obj!=null) {
				
				sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_pharmacy_bill_payee_details "
								+" set due_amount  =due_amount + :amount "
								+" where t_sale_id =:saleId "
								+" and payee_id =:payeeId ")
						.setParameter("amount", (dto.getAmount()-(Obj1!=null ? Obj1.getDepositAmount():0.00)))
						.setParameter("saleId", dto.getSaleId())
						.setParameter("payeeId", dto.getPayeeId())
						.executeUpdate();
				
				Integer status = 2;
				Double selfTotalAmount=0.00;
				if(Obj.getPayeeDueAmount()!=0)
                {
                 selfTotalAmount = dto.getAmount()+Obj.getPayeeDueAmount();
                }
				else
				{
                	 selfTotalAmount = dto.getAmount()-(Obj1!=null ? Obj1.getDepositAmount():0.00);
                }
				
				if(selfTotalAmount.equals(Obj.getTotalBillAmount())) {
					status = 2;
				}else {
					status = 18;
				}
				
				sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_sale "
								+" set outstanding_amount = outstanding_amount + :amount ,"
								+" paid_amount = paid_amount - :amount1 , "
								+" status_id =:status "
								+" where id =:saleId ")
						.setParameter("amount", (dto.getAmount()-(Obj1!=null ? Obj1.getDepositAmount():0.00)))
						.setParameter("amount1", dto.getAmount())
						.setParameter("status", status)
						.setParameter("saleId", dto.getSaleId())
						.executeUpdate();
				
				if(dto.getReceiptType() == 1) {
					
					sessionFactory.getCurrentSession().createSQLQuery("update pharmacy.t_sale_payment_mode set is_active = false , is_deleted = true  " +
									" where amount =:amount "+
									" and sale_id =:saleId ")
							.setParameter("amount", dto.getAmount())
							.setParameter("saleId", dto.getSaleId())
					.executeUpdate();



					String sqlQuerys = "update pharmacy.t_sale_payment_mode "
							+" set  updated_date =:dateString , "
							+" cancel_by =:cancelBy "
							+" where id =:id ";
					sessionFactory.getCurrentSession().createSQLQuery(sqlQuerys.toString())
							.setParameter("dateString", timestamp, StandardBasicTypes.TIMESTAMP)
							.setParameter("cancelBy", dto.getCreatedBy())
							.setParameter("id", dto.getPaymentRecieptId())
							.executeUpdate();
					
				}else {
					
					sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_payment_deposite_consumption  set status = 'I'  where dep_consumption_id  =:paymentReceiptId ")
						.setParameter("paymentReceiptId", dto.getPaymentRecieptId())
						.executeUpdate();
					
					String sqlQuerys = "update billing.t_payment_deposite_consumption"
							+" set  updated_date =:dateString , "
							+" cancel_by =:cancelBy "
							+" where id =:id ";
					sessionFactory.getCurrentSession().createSQLQuery(sqlQuerys.toString())
							.setParameter("dateString",  timestamp, StandardBasicTypes.TIMESTAMP)
							.setParameter("cancelBy", dto.getCreatedBy())
							.setParameter("id", dto.getPaymentRecieptId())
							.executeUpdate();
					
					String query1 = " select tpdc.deposit_id  as \"depositId\" , tpdc.consumed_amount as \"consumedAmount\" from billing.t_payment_deposite_consumption_details  tpdc where tpdc.dep_consumption_id  =:PaymentRecieptId ";

					List<PaymentDepositConsumptionDetailsDto> list = sessionFactory.getCurrentSession().createSQLQuery(query1.toString())
							.setParameter("PaymentRecieptId", dto.getPaymentRecieptId())
							.setResultTransformer(Transformers.aliasToBean(PaymentDepositConsumptionDetailsDto.class)).list();
					
					for (PaymentDepositConsumptionDetailsDto pat : list) {
						sessionFactory.getCurrentSession().createSQLQuery("update billing.m_deposit_master  " +
										" set available_deposit = available_deposit + :consumedAmount , "
										+" consumed_deposit = consumed_deposit - :consumedAmount "
										+"   where deposit_master_id IN(:depositId)")
								.setParameter("consumedAmount", pat.getConsumedAmount())
								.setParameter("depositId", pat.getDepositId())
						.executeUpdate();
					}
					
				
				}
				String sqlQuery1 = " select order_id from  public.t_order_details where sale_detail_id in (select id from pharmacy.t_sale_detail where sale_id =:saleId) ";
				List<Integer> orderIds = (List<Integer>)sessionFactory.getCurrentSession().createSQLQuery(sqlQuery1.toString())
						.setParameter("saleId", dto.getSaleId())
						.list();
				
				if(orderIds != null && orderIds.size()>0) {
					sessionFactory.getCurrentSession().createSQLQuery(" update public.t_order_details set status='A' where order_id in (:orderIds) ")
					.setParameterList("orderIds", orderIds)
					.executeUpdate();
				}
			
				
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
				
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			
		}catch (HibernateException he) {
			he.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response checkInvoiceSettleOrNotByBillId(CompanyInvoiceSettlementReqDto reqDto) throws ApplicationException {

		try {
			
			//to solve concurrency issue
			List<Integer> billIds= null;
			List<Integer> saleIds= null;
			List<Integer> invoiceIds= new ArrayList<>();;
			List<BillingPaymentDetailsReqDto> dueAmountList= null;
			List<BillingPaymentDetailsReqDto> dueAmountListResult= null;
			List<BillingPaymentDetailsReqDto> dueAmountListNew= null;
			List<BillingPaymentDetailsReqDto> billList = null;
			List<BillingPaymentDetailsReqDto> pharmacyList = null;
			CompanyReceiptMasterDto companyReceiptDto = reqDto.getCompanyReceipt();
			
			 billIds =  companyReceiptDto.getReceiptDetails().stream().filter(obj->obj.getBillId() !=null && obj.getBillId()>0).map(obj->obj.getBillId()).collect(Collectors.toList());
			 saleIds =  companyReceiptDto.getReceiptDetails().stream().filter(obj->obj.getSaleId() != null && obj.getSaleId()>0).map(obj->obj.getSaleId()).collect(Collectors.toList());
			 
			 double sum = companyReceiptDto.getReceiptDetails()
					    .stream()
					    .mapToDouble(x -> x.getTotalAmount().doubleValue())
					    .sum();
			 
			 double sum2 = 0;
			 double sum1Check = 0;
			 double sum2Check = 0;
			 Map<Integer,Double> receivedAmounts = reqDto.getCompanyRecptRecAmount();
				if(receivedAmounts != null && receivedAmounts.size() > 0)
				{
					List<Integer> invoiceIdsNew = new ArrayList<>();
			        
			        
					for(Map.Entry<Integer, Double> recAmt : receivedAmounts.entrySet()) {
						invoiceIdsNew.add(recAmt.getKey().intValue());
					}
						 String qryNew = " select due_amount as \"dueAmount\" from billing.m_company_invoice_master where company_invoice_id in (:invoiceIdsNew)";
					     StringBuffer sb = new StringBuffer(qryNew);
					     dueAmountListResult = sessionFactory.getCurrentSession()
								.createSQLQuery(sb.toString())
							    .setParameterList("invoiceIdsNew", invoiceIdsNew)
								.setResultTransformer(Transformers.aliasToBean(BillingPaymentDetailsReqDto.class)).list();
					  
					   if(dueAmountListResult != null && billIds.size()>0) {
					    	  sum2 = dueAmountListResult.stream()
									    .mapToDouble(x -> x.getDueAmount())
									    .sum();
					     }
					if(dueAmountListResult != null && billIds.size()>0) {
						 sum1Check = (double) Math.round(sum);
						
						 sum2Check = (double) Math.round(sum2);
						
						if( Integer.parseInt(String.valueOf(sum1Check).split("\\.")[0]) > Integer.parseInt(String.valueOf(sum2Check).split("\\.")[0]) ){
							 dueAmountListNew = dueAmountListResult;
						 }
					 }
				}
				 
				 
			 if(billIds != null && billIds.size()>0) {
				 String qry = " select bill_status_id as \"statusId\" from  billing.m_billing_master  " + 
					     " where billing_master_id in (:billIds) and bill_status_id= 3";
			     StringBuffer sb = new StringBuffer(qry);
			     billList = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
					    .setParameterList("billIds", billIds)
						.setResultTransformer(Transformers.aliasToBean(BillingPaymentDetailsReqDto.class)).list();
			 }
			 if(saleIds != null && saleIds.size()>0) {
				 
				 String qry = " select status_id as \"statusId\" from  pharmacy.t_sale " + 
					     " where id in (:saleIds) and status_id= 4";
			     StringBuffer sb = new StringBuffer(qry);
			     pharmacyList = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						 .setParameterList("saleIds", saleIds)
						.setResultTransformer(Transformers.aliasToBean(BillingPaymentDetailsReqDto.class)).list();
			 }
			 
			 List<BillingPaymentDetailsReqDto> finalList = null;
			 
			 if(billList != null && billList.size() > 0) {
				finalList = billList;
			 }
			 if(pharmacyList != null && pharmacyList.size() > 0) {
					finalList = pharmacyList;
			 }
			 if(dueAmountListNew != null && dueAmountListNew.size() > 0) {
					finalList = dueAmountListNew;
				 }
			 if(billList != null && pharmacyList != null  && billList.size() > 0 && pharmacyList.size() > 0) {
				 finalList = Stream.concat(billList.stream(), pharmacyList.stream())
                     .collect(Collectors.toList());
			 }
			return new Response(SUCCESS, null, null, finalList, null);
		} catch (Exception e) {
			
			throw e;
		}
	
	}



}
