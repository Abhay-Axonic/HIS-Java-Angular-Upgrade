package com.param.billing.global.transaction.config.dao;

import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.RequestDiscount;
import com.param.billing.global.transaction.model.RequestDiscountDetails;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class RequestDiscountDaoImpl implements IRequestDiscountDao,ICommonConstants{

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response requestDiscount(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			
			RequestDiscount requestDiscount = mapper.map(requestDiscountDto, RequestDiscount.class,
					"RequestDiscountDto_to_RequestDiscount");
			Integer reqId = (Integer) sessionFactory.getCurrentSession().save(requestDiscount);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, reqId);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getRequestedDiscount(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GETR_EQUESTED_DISCOUNT").getQueryString());
			
			stringQuery.append(" and requestdis0_.req_status = 1 " + 
					"	and requestdis0_.request_sent_to =:userId" );
			
			
			Date fromDate = requestDiscountDto.getFromDate() != null && !requestDiscountDto.getFromDate().isEmpty()	? GlobalCommonDateUtils.getDate(requestDiscountDto.getFromDate(), "dd-MM-yyyy"): GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(requestDiscountDto.getUnitId()), "dd-MM-yyyy");
			Date toDate = requestDiscountDto.getToDate() != null && !requestDiscountDto.getToDate().isEmpty()? GlobalCommonDateUtils.getDate(requestDiscountDto.getToDate(), "dd-MM-yyyy"): GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(requestDiscountDto.getUnitId()), "dd-MM-yyyy");
			
			if(requestDiscountDto.getFromDate() != null && requestDiscountDto.getToDate() != null)
				stringQuery.append(" and to_char(requestdis0_.request_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			
			if(requestDiscountDto.getVisitTypeId() != null )
				stringQuery.append(" and requestdis0_.visit_type_id =:visitTypeId ");
			
			if(requestDiscountDto.getPatientId() != null )
				stringQuery.append(" and requestdis0_.patient_id =:patientId ");
			
			if(requestDiscountDto.getRequestBy() != null )
				stringQuery.append(" and requestdis0_.request_sent_by =:requestBy ");
			
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
					.setParameter("orgId", requestDiscountDto.getOrganizationId())
					.setParameter("unitId", requestDiscountDto.getUnitId())
					.setParameter("userId", requestDiscountDto.getRequestSentTo());

			if(requestDiscountDto.getFromDate() != null && requestDiscountDto.getToDate() != null) {
				sqlQuery.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(fromDate, "yyyy-MM-dd"))
						.setParameter("toDate", GlobalCommonDateUtils.getStringDate(toDate, "yyyy-MM-dd"));
			}

			if(requestDiscountDto.getVisitTypeId() != null )
				sqlQuery.setParameter("visitTypeId", requestDiscountDto.getVisitTypeId());

			if(requestDiscountDto.getPatientId() != null )
				sqlQuery.setParameter("patientId", requestDiscountDto.getPatientId());

			if(requestDiscountDto.getRequestBy() != null )
				sqlQuery.setParameter("requestBy", requestDiscountDto.getRequestBy());


			List<RequestDiscountDto> requestDiscountDtosList = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(RequestDiscountDto.class)).list();
			
			return new Response(SUCCESS, null, null, requestDiscountDtosList, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response requestDiscountDetails(RequestDiscountDetailsDto requestDiscountDetailsDto) throws ApplicationException {
		try {
				RequestDiscountDetails requestDiscount = mapper.map(requestDiscountDetailsDto, RequestDiscountDetails.class,
						"RequestDiscountDetailsDto_to_RequestDiscountDetails");
				sessionFactory.getCurrentSession().save(requestDiscount);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getRequestDetails(Integer reqDiscId) throws ApplicationException {
		try{
			
			List<RequestDiscountDto> requestDiscountDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REQUEST_DETAILS")
					.setParameter("reqDiscId", reqDiscId)
					.setResultTransformer(Transformers.aliasToBean(RequestDiscountDetailsDto.class)).list();
			
			return new Response(SUCCESS, null, null, requestDiscountDtosList, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response approveRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set approvedDiscAmt=:approvedDiscAmt,remark=:remark,reqStatus=2,updatedBy=:updatedBy,updatedDate=:updatedDate WHERE reqDiscId=:reqDiscId ")
			.setParameter("approvedDiscAmt", requestDiscountDto.getApprovedDiscAmt())
			.setParameter("reqDiscId", requestDiscountDto.getReqDiscId())
			.setParameter("updatedBy", requestDiscountDto.getUpdatedBy())
			.setParameter("updatedDate", GlobalCommonDateUtils.getDate(requestDiscountDto.getUpdatedDate(),"yyyy-M-dd HH:mm:yyyy"))
			.setParameter("remark", requestDiscountDto.getRemark())
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response approveRequestDetails(RequestDiscountDetailsDto detailsDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscountDetails set approvedDiscAmt=:approvedDiscAmt,updatedBy=:updatedBy,updatedDate=:updatedDate WHERE reqDiscDetailsId=:reqDiscDetailsId ")
			.setParameter("approvedDiscAmt", detailsDto.getApprovedDiscAmt())
			.setParameter("reqDiscDetailsId", detailsDto.getReqDiscDetailsId())
			.setParameter("updatedBy", detailsDto.getUpdatedBy())
			.setParameter("updatedDate", GlobalCommonDateUtils.getDate(detailsDto.getUpdatedDate(),"yyyy-M-dd HH:mm:yyyy"))
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response cancelRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			
			if(requestDiscountDto.getReqDiscId() != null){
				
				sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set reqStatus=3 , remark=:remark WHERE reqDiscId=:reqDiscId ")
				.setParameter("reqDiscId", requestDiscountDto.getReqDiscId())
				.setParameter("remark", requestDiscountDto.getRemark())
				.executeUpdate();
				
				if(requestDiscountDto.getBillId() != null) {
				 sessionFactory.getCurrentSession().createQuery("UPDATE BillingMaster set billStatusId=1 WHERE billingMasterId=:billId ")
					.setParameter("billId", requestDiscountDto.getBillId())
					.executeUpdate();
				}
				/*if(requestDiscountDto.gettRefundCnId()!=null && requestDiscountDto.gettRefundCnId()>0)
				{
					sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_refund_cn set is_approved = 'R' where t_refund_cn_id  =:tRefundCnId ")
					.setInteger("tRefundCnId", requestDiscountDto.gettRefundCnId())
					.executeUpdate();
				}*/
				if(requestDiscountDto.getCreditNoteId()!=null && requestDiscountDto.getCreditNoteId()>0)
				{
					sessionFactory.getCurrentSession().createSQLQuery(" update billing.m_credit_note_master set is_approved = 'R',status  = 'I',credit_note_remark =:creditNoteRemark  where credit_note_id  =:creditNoteId ")
						.setParameter("creditNoteId", requestDiscountDto.getCreditNoteId())
						.setParameter("creditNoteRemark", requestDiscountDto.getCreditNoteRemark())
					.executeUpdate();
					
					sessionFactory.getCurrentSession().createSQLQuery("update billing.t_credit_note_detail set status  = 'I' where credit_note_id =:creditNoteId")
						.setParameter("creditNoteId", requestDiscountDto.getCreditNoteId()).executeUpdate();
					
				}
				if( requestDiscountDto.gettRefundCnId()!=null && requestDiscountDto.gettRefundCnId()>0  )
				{
					sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_refund_cn set is_approved = 'R' where t_refund_cn_id  =:tRefundCnId ")
							.setParameter("tRefundCnId", requestDiscountDto.gettRefundCnId())
							.executeUpdate();
					if(requestDiscountDto.getRefundRemark() != null) {
						
						sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_refund_cn set remark =:remark  where t_refund_cn_id  =:tRefundCnId ")
						.setParameter("tRefundCnId", requestDiscountDto.gettRefundCnId())
						.setParameter("remark", requestDiscountDto.getRefundRemark())
						.executeUpdate();
					}

					if(requestDiscountDto.getDepositId() != null && requestDiscountDto.getDepositId()>0){
						sessionFactory.getCurrentSession().createSQLQuery("call billing.reject_refund(:depositId,cast(:amount as double precision))" )
								.setParameter("depositId", requestDiscountDto.getDepositId())
								.setParameter("amount", requestDiscountDto.getRefundAmount())
								.executeUpdate();
					}


				}
				if( requestDiscountDto.getExpenseId()!=null && requestDiscountDto.getExpenseId()>0  )
				{
						sessionFactory.getCurrentSession().createQuery("UPDATE MiscellaneousExpense set remark=:remark WHERE expenseId=:expenseId ")
						.setParameter("expenseId", requestDiscountDto.getExpenseId())
						.setParameter("remark", requestDiscountDto.getExpenseRemark())
						.executeUpdate();
				}
				if( requestDiscountDto.getCompanyReceiptsExpenseId()!=null && requestDiscountDto.getCompanyReceiptsExpenseId()>0  )
				{
						sessionFactory.getCurrentSession().createQuery("UPDATE CompanyReceiptsExpense set remark=:remark WHERE companyReceiptsExpenseId=:companyReceiptsExpenseId ")
						.setParameter("companyReceiptsExpenseId", requestDiscountDto.getCompanyReceiptsExpenseId())
						.setParameter("remark", requestDiscountDto.getCompanyReciptRemark())
						.executeUpdate();
				}
				return new Response(SUCCESS, null, null, null, null);
				
			}else{
				
				return new Response(SUCCESS, null, null, null, null);
				
			}
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	

	
	@Override
	public Response approvedWithoutBill(Integer requestDiscountId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set reqStatus=4 WHERE reqDiscId=:reqDiscId ")
			.setParameter("reqDiscId", requestDiscountId)
			.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	@Override
	public Response getDiscountRequestForOPBilling(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			//req.encounterId=:encounterId
			
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DISC_REQ_DTLS_FOR_OP_BILLING").getQueryString());

			if(requestDiscountDto.getEncounterId()!=null)
				stringQuery.append("AND requestdis.encounter_id =:encounterId ");

			if(requestDiscountDto.getAdmissionId()!=null)
				stringQuery.append("AND requestdis.admission_id =:admissionId ");

			if(requestDiscountDto.getBillId()!=null)
				stringQuery.append(" AND requestdis.bill_id =:billId ");
			else
				stringQuery.append(" and (requestdis.bill_id is null)");

			if(requestDiscountDto.getRequestType()!=null && requestDiscountDto.getRequestType()>0)
				stringQuery.append("AND requestdis.request_type =:requestType ");

			stringQuery.append(" order by requestdis.req_disc_id desc ");
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

			if(requestDiscountDto.getEncounterId()!=null)
				sqlQuery.setParameter("encounterId", requestDiscountDto.getEncounterId());

			if(requestDiscountDto.getAdmissionId()!=null)
				sqlQuery.setParameter("admissionId", requestDiscountDto.getAdmissionId());

			if(requestDiscountDto.getBillId()!=null)
				sqlQuery.setParameter("billId", requestDiscountDto.getBillId());

			if(requestDiscountDto.getRequestType()!=null && requestDiscountDto.getRequestType()>0)
				sqlQuery.setParameter("requestType", requestDiscountDto.getRequestType());

			List<RequestDiscountDto> requestDiscountDtosList = sqlQuery.setResultTransformer(Transformers.aliasToBean(RequestDiscountDto.class)).list();

			return new Response(SUCCESS, null, null, requestDiscountDtosList, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getRequestedDiscountsForDashboard(RequestDiscountDto requestDiscountDto)
			throws ApplicationException {
		try{
			
			List<RequestDiscountDto> requestDiscountDtosList = sessionFactory.getCurrentSession().getNamedQuery("GETR_EQUESTED_DISCOUNT")
					.setParameter("orgId", requestDiscountDto.getOrganizationId())
					.setParameter("unitId", requestDiscountDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RequestDiscountDto.class)).list();
			
			return new Response(SUCCESS, null, null, requestDiscountDtosList, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateBillIdToRequestDiscountDetails(Integer billMasterId, Integer reqDiscId)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set billId=:billId,updatedDate=:updatedDate WHERE reqDiscId=:reqDiscId ")
			.setParameter("billId", billMasterId)
			.setParameter("reqDiscId", reqDiscId)
			.setParameter("updatedDate", TimeZoneComponent.getDateByzone(0))
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response approveRequestForFinancialClearance(RequestDiscountDto requestDiscountDto)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set reqStatus=2,updatedBy=:updatedBy,updatedDate=:updatedDate WHERE reqDiscId=:reqDiscId ")
			.setParameter("reqDiscId", requestDiscountDto.getReqDiscId())
			.setParameter("updatedBy", requestDiscountDto.getUpdatedBy())
			.setDate("updatedDate", GlobalCommonDateUtils.getDate(requestDiscountDto.getUpdatedDate(),"yyyy-M-dd HH:mm:yyyy"))
			.executeUpdate();
			
			 sessionFactory.getCurrentSession().createQuery("UPDATE BillingMaster set billStatusId=2 WHERE billingMasterId=:billId ")
			.setParameter("billId", requestDiscountDto.getBillId())
			.executeUpdate();
			 
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateAdmissionsIsFinancialClearance(Integer admissionId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession()
			.createQuery(" UPDATE Admission set isFinancialCreance='Y' "
					+    " WHERE admissionId=:admissionId ")
			.setParameter("admissionId", admissionId)
			.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Response updateEncounterIsFinancialClearance(Integer encounterId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession()
			.createQuery("UPDATE EncounterMaster set isFinancialClearance='Y' "
					+ "WHERE encounterId=:encounterId ")
			.setParameter("encounterId", encounterId)
			.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Response updateDischargeStatusAfterFinancialClearance(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createSQLQuery("update adt.t_discharge set "
															+ "discharge_status_id =3 "
			 												+"where admission_id =:admissionId ")
			.setParameter("admissionId", requestDiscountDto.getAdmissionId())
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response approveCnRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set reqStatus=2,updatedBy=:updatedBy,updatedDate=:updatedDate WHERE reqDiscId=:reqDiscId ")
			.setParameter("reqDiscId", requestDiscountDto.getReqDiscId())
			.setParameter("updatedBy", requestDiscountDto.getUpdatedBy())
			.setParameter("updatedDate", TimeZoneComponent.getDateByzone(requestDiscountDto.getUnitId()))
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Response approveAndApply(Integer ReqDicId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set reqStatus=4,updatedDate=now() WHERE reqDiscId=:reqDiscId ")
			.setParameter("reqDiscId", ReqDicId)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateCnRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			if(requestDiscountDto.getCreditNoteId()!=null && requestDiscountDto.getCreditNoteId()>0){
				sessionFactory.getCurrentSession().createQuery(" UPDATE CreditNoteMaster set isApproved = 'Y' WHERE creditNoteId = :creditNoteId ")
						.setParameter("creditNoteId", requestDiscountDto.getCreditNoteId())
						.executeUpdate();
			}
			if(requestDiscountDto.getCreditNoteRemark()!=null && requestDiscountDto.getCreditNoteId()>0){
				sessionFactory.getCurrentSession().createQuery(" UPDATE CreditNoteMaster set creditNoteRemark =:creditNoteRemark WHERE creditNoteId = :creditNoteId ")
						.setParameter("creditNoteId", requestDiscountDto.getCreditNoteId())
						.setParameter("creditNoteRemark", requestDiscountDto.getCreditNoteRemark())
						.executeUpdate();
			}
			if(requestDiscountDto.gettRefundCnId()!=null  && requestDiscountDto.gettRefundCnId()>0){
				sessionFactory.getCurrentSession().createQuery(" UPDATE RefundCNMaster set isApproved = 'Y' WHERE tRefundCnId = :tRefundCnId ")
						.setParameter("tRefundCnId", requestDiscountDto.gettRefundCnId())
						.executeUpdate();
			}
			if(requestDiscountDto.getRefundRemark()!=null  && requestDiscountDto.gettRefundCnId()>0){
				sessionFactory.getCurrentSession().createQuery(" UPDATE RefundCNMaster set remark=:remark WHERE tRefundCnId = :tRefundCnId ")
						.setParameter("tRefundCnId", requestDiscountDto.gettRefundCnId())
						.setParameter("remark", requestDiscountDto.getRefundRemark())
						.executeUpdate();
			}

			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}



	@Override
	public Response updateRefundRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			  /*sessionFactory.getCurrentSession().createQuery(" UPDATE RefundCNMaster set isApproved = 'Y' WHERE tRefundCnId = :tRefundCnId ")
			.setInteger("tRefundCnId", requestDiscountDto.gettRefundCnId())
			.executeUpdate();*/
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response completeCreditNoteDiscountRequest(Integer reqDiscId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set reqStatus=4,updatedDate=now() WHERE reqDiscId=:reqDiscId ")
			.setParameter("reqDiscId", reqDiscId)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getBadDebitRequestObject(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DISC_REQ_DTLS_FOR_BAD_DEBIT").getQueryString());
			if(requestDiscountDto.getBillId()!=null)
				stringQuery.append(" where requestdis.bill_id =:billId ");
			else if(requestDiscountDto.gettSaleId()!=null)
				stringQuery.append(" where requestdis.sale_id =:tSaleId ");

			if(requestDiscountDto.getRequestType()!=null && requestDiscountDto.getRequestType()>0)
				stringQuery.append(" AND requestdis.request_type =:requestType ");

			stringQuery.append(" order by requestdis.req_disc_id desc ");
			
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			if(requestDiscountDto.getBillId()!=null)
				sqlQuery.setParameter("billId", requestDiscountDto.getBillId());
			else if (requestDiscountDto.gettSaleId()!=null)
				sqlQuery.setParameter("tSaleId", requestDiscountDto.gettSaleId());

			if(requestDiscountDto.getRequestType()!=null && requestDiscountDto.getRequestType()>0)
				sqlQuery.setParameter("requestType", requestDiscountDto.getRequestType());

			List<RequestDiscountDto> requestDiscountDtosList = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(RequestDiscountDto.class)).list();
			return new Response(SUCCESS, null, null, requestDiscountDtosList, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response getCancelDiscountReqByBillId(Integer billId) throws ApplicationException {
		try{
			
			String query = "update RequestDiscount set billId = null  , reqStatus = 3 where billId =:billId ";
			sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("billId", billId)
					.executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	public Response getUHIDMergeReuestApprovalData(RequestDiscountDto requestDiscountDto) {
		try {
			
			Integer fromPatientId = requestDiscountDto.getFromPatientId();
			Integer toPatientId = requestDiscountDto.getToPatientId();
			Integer reqSentBy = requestDiscountDto.getRequestSentBy();
    		Integer reqSentTo = requestDiscountDto.getRequestSentTo();
			Integer reqId = requestDiscountDto.getReqDiscId();
			
			if(fromPatientId!=null && fromPatientId >0)
			{
				String hql = "DELETE FROM PatientDetails WHERE patientId = :patientId";
				int deletedEntities = sessionFactory.getCurrentSession().createQuery(hql)
				        .setParameter("patientId", fromPatientId)
				        .executeUpdate();
			}
			
			int flag = (int)sessionFactory.getCurrentSession().createSQLQuery("CALL patient.patient_merge_log(:fromPatientId, :toPatientId, :reqSentBy :, :reqSentTo, :reqId)")
					.setParameter("fromPatientId", fromPatientId)
					.setParameter("toPatientId", toPatientId)
					.setParameter("reqSentBy", reqSentBy)
					.setParameter("reqSentTo", reqSentTo)
					.setParameter("reqId", reqId)
				.uniqueResult();
					
			
		
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);
			
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response cancelDiscountRequestOnCreditNoteCancellation(Integer creditNoteId)	throws ApplicationException {
		try {

			sessionFactory.getCurrentSession().createQuery("UPDATE RequestDiscount set reqStatus=3 WHERE creditNoteId=:creditNoteId ")
			.setParameter("creditNoteId", creditNoteId)
			.executeUpdate();

			return new Response<>(SUCCESS,null,null,null,null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updateExpenseRemarkRequest(RequestDiscountDto requestDiscountDto) {
		try {
			if( requestDiscountDto.getExpenseId()!=null && requestDiscountDto.getExpenseId()>0  )
			{
			sessionFactory.getCurrentSession().createQuery("UPDATE MiscellaneousExpense set remark=:remark WHERE expenseId=:expenseId ")
			.setParameter("expenseId", requestDiscountDto.getExpenseId())
			.setParameter("remark", requestDiscountDto.getExpenseRemark())
			.executeUpdate();
			}
			if( requestDiscountDto.getCompanyReceiptsExpenseId()!=null && requestDiscountDto.getCompanyReceiptsExpenseId()>0  )
			{
					sessionFactory.getCurrentSession().createQuery("UPDATE CompanyReceiptsExpense set remark=:remark WHERE companyReceiptsExpenseId=:companyReceiptsExpenseId ")
					.setParameter("companyReceiptsExpenseId", requestDiscountDto.getCompanyReceiptsExpenseId())
					.setParameter("remark", requestDiscountDto.getCompanyReciptRemark())
					.executeUpdate();
			}
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getOrderDetailsIdByCreditNoteId(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		 try{
			 List<RequestDiscountDto> creditDetailsObj = sessionFactory.getCurrentSession().createSQLQuery(
					" select order_detail_id as \"orderDetailsId\"  from billing.t_credit_note_detail tcnd where credit_note_id =:creditNoteId ")
					 .setParameter("creditNoteId", requestDiscountDto.getCreditNoteId())
					.setResultTransformer(Transformers.aliasToBean(RequestDiscountDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, creditDetailsObj, null);
	        } catch (Exception e)
	        {
	            throw e;
	        }
	}

	@Override
	public Response cancelRequestFinancialRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			if(requestDiscountDto.getBillId() != null){
				
				String query = " UPDATE billing.t_request_discount SET " + "	req_status= 2 ,	"
						+ "	bill_id = null " 
						+ " WHERE bill_id =:billId "
						+ " and request_type = 2 ";

				SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
				sqlQuery.setParameter("billId", requestDiscountDto.getBillId())
						.executeUpdate();
				return new Response(SUCCESS, null, null, null, null);
				
			}else{
				return new Response(SUCCESS, null, null, null, null);
			}
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response requestDiscountBillList(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			
			RequestDiscount requestDiscount = mapper.map(requestDiscountDto, RequestDiscount.class,
					"RequestDiscountDto_to_RequestDiscount");
			Integer reqId = (Integer) sessionFactory.getCurrentSession().save(requestDiscount);
			
			sessionFactory.getCurrentSession().createSQLQuery("update billing.m_billing_master set "
					+ " bill_status_id = 1 "
					+" where billing_master_id =:billId ")
            .setParameter("billId", requestDiscountDto.getBillId())
            .executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, reqId);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updateEncounterMasterStatus(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
				if(requestDiscountDto.getEncounterId() != null) {
				 sessionFactory.getCurrentSession().createQuery("UPDATE EncounterMaster set statusId=5 WHERE encounterId=:encounterId ")
					.setParameter("encounterId", requestDiscountDto.getEncounterId())
					.executeUpdate();
			    }
	     	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		}

}
