package com.param.billing.global.transaction.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.dto.CreditNotesForAddendumResDto;
import com.param.billing.global.dto.PatientOutstandingDto;
import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.dto.RefundCreditNoteResDto;
import com.param.billing.global.transaction.config.dao.IRequestDiscountDao;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.CreditNoteMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingStatusMasterConstant;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class CreditNoteMasterDaoImpl extends GenericDao<CreditNoteMaster, Integer> implements ICreditNoteMasterDao, ICommonConstants {

	public CreditNoteMasterDaoImpl() {
		super(CreditNoteMaster.class);
	}

	@Autowired
	IRequestDiscountDao iRequestDiscountDao;
	
	@Override
	public Response saveCreditNote(CreditNoteMasterDto creditNoteMasterDto) throws ApplicationException 
	{
		try{
			CreditNoteMaster master = new CreditNoteMaster();
				master.setCreditNoteNo(creditNoteMasterDto.getCreditNoteNo());
				master.setBillPayeeId(creditNoteMasterDto.getBillPayeeId());
				master.setCreditNoteDate(creditNoteMasterDto.getCreditNoteDate() != null && !creditNoteMasterDto.getCreditNoteDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteMasterDto.getCreditNoteDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteMasterDto.getUnitId()));
				master.setBillId(creditNoteMasterDto.getBillId());
				master.setCancelOrDiscount(creditNoteMasterDto.getCancelOrDiscount());
				master.setCrNoteAgainst(creditNoteMasterDto.getCrNoteAgainst());
				master.setCrTotalAmount(creditNoteMasterDto.getCrTotalAmount());
				master.setCrTaxAmount(creditNoteMasterDto.getCrTaxAmount());
				master.setCrNetAmount(creditNoteMasterDto.getCrNetAmount());
				master.setCrOutAdjustmentAmount(creditNoteMasterDto.getCrOutAdjustmentAmount());
				master.setCrDeuAmount(creditNoteMasterDto.getCrDeuAmount());
				master.setCrReasonId(creditNoteMasterDto.getCrReasonId());
				master.setCeRemark(creditNoteMasterDto.getCeRemark());
				master.setOrgnisationId(creditNoteMasterDto.getOrgnisationId());
				master.setUnitId(creditNoteMasterDto.getUnitId());
				master.setStatus(creditNoteMasterDto.getStatus());
				master.setCreatedBy(creditNoteMasterDto.getCreatedBy());
				master.setCreatedDate(creditNoteMasterDto.getCreatedDate() != null && !creditNoteMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteMasterDto.getUnitId()));
				master.setUpdatedBy(creditNoteMasterDto.getUpdatedBy());
				master.setUpdatedDate(creditNoteMasterDto.getUpdatedDate() != null && !creditNoteMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteMasterDto.getUnitId()));
				master.setCounterId(creditNoteMasterDto.getCounterId());
				master.setIsApproved(creditNoteMasterDto.getIsApproved());
				master.setDiscountRoundoff(creditNoteMasterDto.getDiscountRoundoff());
				master.setSelfPayable(creditNoteMasterDto.getSelfPayable());
				master.setCreditPayable(creditNoteMasterDto.getCreditPayable());
				master.setPackageConsumptionAmt(creditNoteMasterDto.getPackageConsumptionAmt());
				master.setBillPayeeDetailsId(creditNoteMasterDto.getBillPayeeDetailsId());
				master.setCreditNoteRemark(creditNoteMasterDto.getCreditNoteRemark());
			    master.setSignRoundOff(creditNoteMasterDto.getSignRoundOff());
			    master.setAssociateCompanyId(creditNoteMasterDto.getAssociateCompanyId());
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public BigInteger generateCreditNoteNumber() throws BillingException {
		try{
			BigInteger id = (BigInteger)sessionFactory.getCurrentSession().getNamedQuery("GENERATE_CREDIT_NOTE_NO").uniqueResult();
			return id;
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getCreditNoteListForRefund(RefundCnSearchDto reqDto) throws ApplicationException {
		try{
		//	Date fromDate =(reqDto.getFromDate()!= null && !reqDto.getToDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-M-yyyy HH:mm:ss"):TimeZoneComponent.getDateByzone(unitId);
		//	Date toDate = (reqDto.getFromDate()!= null && !reqDto.getToDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-M-yyyy HH:mm:ss"):GlobalCommonDateUtils.getNextDateInDate(fromDate.getTime());
			if(reqDto.getFromDate() == null || reqDto.getToDate() == null || reqDto.getFromDate().isEmpty() || reqDto.getToDate().isEmpty())
			{
				reqDto.setFromDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
				reqDto.setToDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
			}
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_CREDIT_NOTE_LIST_FOR_REFUND").getQueryString());
			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				query.append(" and ( trim(lower(pat.first_name)) like :patientName or trim(lower(pat.middle_name)) like :patientName or trim(lower(pat.last_name)) like :patientName )");

			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
				query.append(" and to_char(crnotemst.credit_note_date,'YYYY-MM-DD') >=:fromDate ");

			if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
				query.append(" and to_char(crnotemst.credit_note_date,'YYYY-MM-DD') <=:toDate ");

			if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
				query.append(" and trim(lower(pat.uhid_number)) like lower(:uhidNumber) ");

			if(reqDto.getRefundCnNo()!= null  && !reqDto.getRefundCnNo().isEmpty())
				query.append(" and lower(crnotemst.credit_note_no) like :refundCnNo ");

			if(reqDto.getRefundCrNo()!= null  && !reqDto.getRefundCrNo().isEmpty())
				query.append(" and lower(crnotemst.credit_note_no) like :refundCrNo ");

			query.append(" order by crnotemst.credit_note_date desc ");
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						 .setParameter("orgId", reqDto.getOrgId())
						 .setParameter("unitId", reqDto.getUnitId());

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				sqlQuery.setParameter("patientName", "%" + reqDto.getPatientName().toLowerCase() + "%");

			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
				sqlQuery.setParameter("fromDate", reqDto.getFromDate());

			if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
				sqlQuery.setParameter("toDate", reqDto.getToDate());

			if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
				sqlQuery.setParameter("uhidNumber", "%" + reqDto.getUhidNumber() + "%");

			if(reqDto.getRefundCnNo()!= null  && !reqDto.getRefundCnNo().isEmpty())
				sqlQuery.setParameter("refundCnNo", "%" + reqDto.getRefundCnNo().toLowerCase() + "%");

			if(reqDto.getRefundCrNo()!= null  && !reqDto.getRefundCrNo().isEmpty())
				sqlQuery.setParameter("refundCrNo", "%" + reqDto.getRefundCrNo().toLowerCase() + "%");


			List<RefundCreditNoteResDto> listRefundCreditNoteResDto = sqlQuery
					                                                 .setResultTransformer(Transformers.aliasToBean(RefundCreditNoteResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listRefundCreditNoteResDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDueAmountByCreditNoteId(int creditNoteId, double crDeuAmount,String receivedFrom) {
		try 
		{
			sessionFactory.getCurrentSession().getNamedQuery("UPDATE_CR_DUE_AMOUNT_BY_CREDIT_NOTE").setParameter("crDeuAmount", crDeuAmount).setParameter("creditNoteId", creditNoteId).setParameter("receivedFrom", receivedFrom).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) { 
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getCreditNotesByBillForAddendum(int billId) throws ApplicationException {
		try{
			List<CreditNotesForAddendumResDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_CREDIT_NOTES_BY_BILL_FOR_ADDENDUM").setParameter("billId", billId).setResultTransformer(Transformers.aliasToBean(CreditNotesForAddendumResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response checkCreditNotePeningAgainstBill(Integer billId) throws ApplicationException {
		try 
		{
			BigInteger count = (BigInteger)sessionFactory.getCurrentSession().createSQLQuery("select count(*) from billing.m_credit_note_master  where  is_approved = 'N' and  bill_id = :billId and cr_deu_amount > 0 and status = 'A' ").setParameter("billId", billId).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count.intValue());
		} catch (Exception e) { 
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response checkFinalBillAgaistAdmission(Integer admissionId) throws ApplicationException {
		try 
		{
			BigInteger count = (BigInteger)sessionFactory.getCurrentSession().createSQLQuery("select " + 
					"	count(*) " + 
					"from " + 
					"	billing.m_billing_master " + 
					"where " + 
					"	admission_id= :admissionId " + 
					"	and is_interim_or_final_bill = 2 " + 
					"	and bill_status_id = 3").setParameter("admissionId", admissionId).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count.intValue());
		} catch (Exception e) { 
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}


	public Response cancelCreditNote(Integer creditNoteId,Integer billId) throws ApplicationException {
		try
		{
			CreditNoteMaster creditNoteMaster = null;
			if(creditNoteId!=null && creditNoteId>0) 
			{
				creditNoteMaster = (CreditNoteMaster) sessionFactory.getCurrentSession().get(CreditNoteMaster.class, creditNoteId);
				
				this.updateCrPayeeDetails(creditNoteMaster.getBillId(),creditNoteMaster.getBillPayeeDetailsId(),creditNoteMaster.getCrNetAmount(),false,false,creditNoteMaster);
				
				sessionFactory.getCurrentSession().createSQLQuery("update billing.m_credit_note_master set status  = 'I' where credit_note_id =:creditNoteId")
						.setParameter("creditNoteId", creditNoteId).executeUpdate();
				sessionFactory.getCurrentSession().createSQLQuery("update billing.t_credit_note_detail set status  = 'I' where credit_note_id =:creditNoteId")
						.setParameter("creditNoteId", creditNoteId).executeUpdate();
				
				/*sessionFactory.getCurrentSession().createSQLQuery("update billing.m_credit_note_master set status  = 'I' where credit_note_id =:creditNoteId")
				.setInteger("creditNoteId", creditNoteId).executeUpdate();*/
				
			}
			if(billId!=null && billId >0 )
			{
				sessionFactory.getCurrentSession().createSQLQuery("update billing.m_credit_note_master set status = 'I' where bill_id =:billId")
						.setInteger("billId", billId).executeUpdate();
				sessionFactory.getCurrentSession().createSQLQuery("update " +
								"billing.t_credit_note_detail " +
								"set " +
								"status = 'I' " +
								"where " +
								"credit_note_id in (select coalesce(cn.credit_note_id,0) " +
								"from " +
								"billing.m_credit_note_master cn where cn.bill_id =:billId and cn.status = 'A')")
						.setParameter("billId", billId).executeUpdate();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, "Credit Note cancelled successfully.", null, creditNoteMaster);
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response checkPatientOutstanding(Integer patientId) throws ApplicationException {
		try
		{
			PatientOutstandingDto outstandingDto = (PatientOutstandingDto) sessionFactory.getCurrentSession().createSQLQuery("with allow_refund as ( "
							+ "	select "
							+ "		( "
							+ "		select "
							+ "			coalesce(sum(bill.outstanding), 0) "
							+ "		from "
							+ "			billing.m_billing_master bill "
							+ "		where "
							+ "			bill.bill_cancelled = 'N' "
							+ "			and bill.status = 'A' "
							+ "			and bill.patient_id = patient.patient_id) as outstanding, "
							+ "		( "
							+ "		select "
							+ "			coalesce(sum(sale.outstanding_amount), 0) "
							+ "		from "
							+ "			pharmacy.t_sale sale "
							+ "		inner join public.t_unit_application_billing_configuration config on "
							+ "			sale.unit_id = config.unit_id "
							+ "		where "
							+ "			sale.patient_id = patient.patient_id "
							+ "			and sale.status_id <> 9 "
							+ "			and (sale.visit_type_id = 1 "
							+ "			and sale.is_consolidated_bill = 'N')) as pharmacyotstanding, "
							+ "		( "
							+ "		select "
							+ "			coalesce(sum(deposit.available_deposit) , 0) "
							+ "		from "
							+ "			billing.m_deposit_master deposit "
							+ "		where "
							+ "			deposit.status = 'A' "
							+ "			and deposit.patient_id = patient.patient_id ) as availabledeposit "
							+ "	from "
							+ "		patient.t_patient_registration patient "
							+ "	where "
							+ "		patient.patient_id = :patientId ) "
							+ "	select "
							+ "		(case "
							+ "			when (ar.outstanding + ar.pharmacyotstanding) > ar.availabledeposit then 1 "
							+ "			else 0 "
							+ "		end) as \"status\",(ar.outstanding + ar.pharmacyotstanding) as \"outstandingAmt\" "
							+ "	from "
							+ "		allow_refund ar")
					.setParameter("patientId", patientId)
					.setResultTransformer(Transformers.aliasToBean(PatientOutstandingDto.class))
					.uniqueResult();


			if(outstandingDto != null && outstandingDto.getOutstandingAmt()!=null && outstandingDto.getStatus().equals(1)){
				String msg = "Please clear due amount of "+outstandingDto.getOutstandingAmt();
				return new Response<>(ERROR, ERROR_CODE, msg, null, outstandingDto.getStatus());
			}
			else{
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, outstandingDto.getStatus());
			}

		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}


	@Override
	public Response updateCrPayeeDetails(Integer billId,Integer billPayeeDetailsId,Double amount,boolean addSubtract,boolean isSelfDueClear, CreditNoteMaster creditNoteMaster) throws ApplicationException {
		try
		{
			BillingMaster billingMaster = getBillByBillIdId(billId);
			
			if(addSubtract)
			{
				billingMaster.setOutstanding(billingMaster.getOutstanding() - amount);
				
				/*if((billingMaster.getOutstanding() + amount) > billingMaster.getNetAmount() ){
					billingMaster.setOutstanding(billingMaster.getNetAmount()-amount);
				}else {
					billingMaster.setOutstanding(billingMaster.getOutstanding() - amount);
				}*/
				
				
				if(billingMaster.getBillStatusId()==IBillingStatusMasterConstant.PENDING)
				{
					billingMaster.setBillStatusId(billingMaster.getOutstanding()==0 ? IBillingStatusMasterConstant.SETTLED : IBillingStatusMasterConstant.PENDING);
				}else{
					billingMaster.setBillStatusId(billingMaster.getOutstanding().equals(0.00) ? IBillingStatusMasterConstant.SETTLED : 
						(billingMaster.getOutstanding().equals(billingMaster.getNetAmount())
								&& billingMaster.getSelfPayable()>0 ? IBillingStatusMasterConstant.PENDING : IBillingStatusMasterConstant.ONHOLD));
				}		
				
				if(billingMaster.getIsInterimOrFinalBill()==2 && isSelfDueClear && billingMaster.getAdmissionId()!=null)
				{
					iRequestDiscountDao.updateAdmissionsIsFinancialClearance(billingMaster.getAdmissionId());
				}
				
				sessionFactory.getCurrentSession().update(billingMaster);

			}
			else{ //on cancellation of credit note
				
				if(creditNoteMaster.getIsApproved()=='Y' ) {
					
					BillPayeeDetails billPayeeDetails = getBillDetailsObjByBillPayeeDetailsId(billPayeeDetailsId);
					if(billPayeeDetails.getCrNetAmt() != null) {
						billPayeeDetails.setCrNetAmt(billPayeeDetails.getCrNetAmt()-amount);
					}
				
					billPayeeDetails.setDueAmount(billPayeeDetails.getDueAmount()+amount > billPayeeDetails.getBillAmount() ? billPayeeDetails.getBillAmount() : billPayeeDetails.getDueAmount()+amount);
					
					sessionFactory.getCurrentSession().update(billPayeeDetails);
					
					billingMaster.setOutstanding(billingMaster.getOutstanding()+amount >= billingMaster.getNetAmount() ? billingMaster.getNetAmount() : billingMaster.getOutstanding()+amount);
					
			
					billingMaster.setBillStatusId(billingMaster.getOutstanding().equals(0.00) ? IBillingStatusMasterConstant.SETTLED : 
													(billingMaster.getOutstanding().equals(billingMaster.getNetAmount())
															&& billingMaster.getSelfPayable()>0 ? IBillingStatusMasterConstant.PENDING : IBillingStatusMasterConstant.ONHOLD));
				
					sessionFactory.getCurrentSession().update(billingMaster);
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		//	throw new BillingException(e.getMessage());
		}
	}

	@Override
	public List<CreditNoteDetailsDto> getCreditNoteDetailsByCreditNoteId(Integer creditNoteId) {
		try
		{
			String query = " select " 
						 + " 	tcnd.order_detail_id as \"orderDetailsId\", " 
						 + " 	tcnd.cancel_or_discount as \"cancelOrDiscount\", "
						 + "	tcnd.unit_id as \"unitId\" " 
						 + " from " 
						 + " 	billing.t_credit_note_detail tcnd " 
						 + " where " 
						 + " 	credit_note_id =:creditNoteId " 
						 + " 	and status ='A' ";
			List<CreditNoteDetailsDto> creditNoteDetailsDtosList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("creditNoteId", creditNoteId)
					.setResultTransformer(Transformers.aliasToBean(CreditNoteDetailsDto.class)).list();
			
			return creditNoteDetailsDtosList;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	public BillPayeeDetails getBillDetailsObjByBillPayeeDetailsId(Integer billPayeeDetailsId)	throws ApplicationException {
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(BillPayeeDetails.class);
			c.add(Restrictions.eq("billPayeeDetailsId", billPayeeDetailsId));
			c.add(Restrictions.eq("status", 'A'));
			List<BillPayeeDetails> billDetailsList = c.list();

			return billDetailsList.getFirst();
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}
	
	@Override
	public BillingMaster getBillByBillIdId(Integer billId)	throws ApplicationException {
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(BillingMaster.class);
			c.add(Restrictions.eq("billingMasterId", billId));
			c.add(Restrictions.eq("status", 'A'));
			List<BillingMaster> billingMasters = c.list();

			return billingMasters.getFirst();
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}
	
	@Override
	public CreditNoteMaster getCreditNoteById(Integer creditNoteId)	throws ApplicationException {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(CreditNoteMaster.class);
				c.add(Restrictions.eq("creditNoteId", creditNoteId));
			List<CreditNoteMaster> creditNoteMastersList = c.list();

			return creditNoteMastersList.getFirst();
		} catch (Exception e) {
			
			throw e;
		}
		//return null;
	}

	@Override
	public Double getRoundOffByBillId(Integer billId) throws ApplicationException {
		try{
			Double roundOff = (Double)sessionFactory.getCurrentSession().createSQLQuery("select " +
							" coalesce(sum(coalesce(mcnm.discount_roundoff,0)),0) " +
							" from " +
							" billing.m_credit_note_master mcnm " +
							" where " +
							" bill_id = :billId " +
							" and mcnm.status  = 'A'")
					.setParameter("billId",billId)
					.uniqueResult();
			return roundOff;
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getCreditNoteDetailsByCreditNoteId(CreditNoteDetailsDto creditNoteDetailsDto)
			throws ApplicationException {
		try
		{
			String query = " select " 
						 + " 	tcnd.order_detail_id as \"orderDetailsId\", " 
						 + " 	tcnd.cancel_or_discount as \"cancelOrDiscount\", "
						 + "	tcnd.unit_id as \"unitId\" " 
						 + " from " 
						 + " 	billing.t_credit_note_detail tcnd " 
						 + " where " 
						 + " 	tcnd.credit_note_id =:creditNoteId " 
						 + " 	and tcnd.status ='A' "
						 + "    and tcnd.cancel_or_discount = 1";
			List<CreditNoteDetailsDto> creditNoteDetailsDtosList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("creditNoteId", creditNoteDetailsDto.getCreditNoteId())
					.setResultTransformer(Transformers.aliasToBean(CreditNoteDetailsDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, creditNoteDetailsDtosList, null);
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@Override
	public void inActiveOrderDetails(List<Integer> orderIds) {
		sessionFactory.getCurrentSession().createSQLQuery(" update t_order_details set status ='I' where  order_id  in (select order_id from t_order_master ord where ord.encounter_id = :encounterId); ")
				.setParameter("encounterId", orderIds).executeUpdate();
	}

	@Override
	public void inActiveOrderDetailsForWeb(Integer orderIds) {
		sessionFactory.getCurrentSession().createSQLQuery(" update t_order_details set status ='I' where  order_id  in (select order_id from t_order_master ord where ord.encounter_id = :encounterId); ")
				.setParameter("encounterId", orderIds).executeUpdate();
	}

}
