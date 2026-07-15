package com.param.billing.global.transaction.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.dto.CompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.config.dto.MCompanyInvoiceMasterDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementListResDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementSearchReqDto;
import com.param.billing.global.transaction.model.MCompanyInvoiceMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MCompanyInvoiceMasterDaoImpl extends GenericDao<MCompanyInvoiceMaster, Integer> implements ICompanyInvoiceCreationMasterDao, ICommonConstants{

	public MCompanyInvoiceMasterDaoImpl() {
		super(MCompanyInvoiceMaster.class);
	}

	@Override
	public com.param.global.common.Response saveCompanyInvoiceCreationMasterList(MCompanyInvoiceMasterDto masterDto)
			throws ServiceException {
		try{
			MCompanyInvoiceMaster mCompanyInvoiceMaster = new MCompanyInvoiceMaster();
				mCompanyInvoiceMaster.setCreatedBy(masterDto.getCreatedBy());
				mCompanyInvoiceMaster.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()).getTime());
				mCompanyInvoiceMaster.setOrgId(masterDto.getOrgId());
				mCompanyInvoiceMaster.setUnitId(masterDto.getUnitId());
				mCompanyInvoiceMaster.setDueAmount(masterDto.getDueAmount());
				mCompanyInvoiceMaster.setCrNetAmount(masterDto.getCrNetAmount());
				mCompanyInvoiceMaster.setTotalAmount(masterDto.getTotalAmount());
				mCompanyInvoiceMaster.setPayeeId(masterDto.getPayeeId());
				mCompanyInvoiceMaster.setAssociateCompanyId(masterDto.getAssociateCompanyId());
				mCompanyInvoiceMaster.setPaymentEntitlementId(masterDto.getPaymentEntitlementId());
				mCompanyInvoiceMaster.setInvoiceNumber(masterDto.getInvoiceNumber());
				mCompanyInvoiceMaster.setInvoiceDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()), "dd-M-yyyy").getTime());
				mCompanyInvoiceMaster.setInvoiceNumberSystemGen(masterDto.getInvoiceNumberSystemGen());
			mCompanyInvoiceMaster = save(mCompanyInvoiceMaster);
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, null, mCompanyInvoiceMaster);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public com.param.global.common.Response getcompanyRemainderList(
			CompanyInvoiceCreationReqDto reqDto) throws BillingException {
		try {
			
			 Date fromDate =(reqDto.getFromDate()!= null && !reqDto.getFromDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-M-yyyy HH:mm:ss"):TimeZoneComponent.getDateByzone(0);
			 Date toDate =(reqDto.getToDate()!= null && !reqDto.getToDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-M-yyyy HH:mm:ss"):TimeZoneComponent.getDateByzone(0);
		
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_COMPANY_INVOICE_REMAINDER_BY_PAYEE_ID").getQueryString());

			if(reqDto.getPayeeId() != null)
				query.append(" and im.payee_id =:payeeId ");

			if(reqDto.getAssociateCompanyId() != null)
				query.append(" and im.associate_company_id =:associateCompanyId ");
			
			 query.append(" AND im.invoice_date <='"+fromDate+"' AND im.invoice_date >='"+toDate+"'");
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId",1);

			if(reqDto.getPayeeId() != null)
				sqlQuery.setParameter("payeeId", reqDto.getPayeeId());

			if(reqDto.getAssociateCompanyId() != null)
				sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

			List<MCompanyInvoiceMasterDto> listCompanyInvoiceCreationList = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(MCompanyInvoiceMasterDto.class)).list();
			return new com.param.global.common.Response <>(SUCCESS, SUCCESS_CODE, null, listCompanyInvoiceCreationList, null);
			
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response (ERROR, SERVER_ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCompInvoiceForSettlement(CompanyInvoiceSettlementSearchReqDto reqDto) throws BillingException {
		try{
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_INVOICE_LIST_FOR_COMPANY_SETTLEMENT").getQueryString());

			if(reqDto.getCompanyId() != null)
				query.append(" and inv.payee_id =:companyId ");

			if(reqDto.getAssociateCompanyId() != null)
				query.append(" and inv.associate_company_id =:associateCompanyId ");

			if( reqDto.getVisitTypeId()!=null) {
				query.append(" and mbm.visit_type_id =:visitTypeId ");
			}

//			query.append(reqDto.getIsPendingOrSettled()!=null && reqDto.getIsPendingOrSettled()=='S' ?  " and inv.due_amount = 0 ":" and inv.due_amount > 0 ");
			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty() && reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
			{
				Date from = GlobalCommonDateUtils.getDate(reqDto.getFromDate(),"dd-MM-yyyy");
				Date to = GlobalCommonDateUtils.getDate(reqDto.getToDate(),"dd-MM-yyyy");
				if(reqDto.getUhidNumber()!="" && reqDto.getUhidNumber()!=null) {
					query = query.append(" and inv.company_invoice_id  in ((select invc.company_invoice_id from billing.m_company_invoice_master invc " + 
							"	inner join billing.t_company_invoice_details tcid on invc.company_invoice_id  = tcid.company_invoice_id " + 
							"	inner join billing.m_billing_master mbm on tcid.bill_id  = mbm.billing_master_id " + 
							"	inner join patient.t_patient_registration tpr on tpr.patient_id  = mbm.patient_id " + 
							"	where lower(tpr.uhid_number)  like :uhidNumber " +
							"   and invc.organisation_id =:orgId " +
							"	and invc.unit_id =:unitId  " +
							"	and invc.due_amount > 0 " + 
							"	and invc.invoice_date between :from and :to limit 1) " +
							"	, (select invnc.company_invoice_id from billing.m_company_invoice_master invnc" + 
							"	inner join billing.t_company_invoice_details tcid on invnc.company_invoice_id  = tcid.company_invoice_id " + 
							"	inner join pharmacy.t_sale sale on sale.id = tcid.t_sale_id " + 
							"	inner join patient.t_patient_registration tpr on tpr.patient_id  = sale.patient_id " + 
							"	where lower(tpr.uhid_number)  like :uhidNumber " +
							"   and invnc.organisation_id =:orgId " +
							"	and invnc.unit_id =:unitId  " +
							"	and invnc.due_amount > 0 " + 
							"	and invnc.invoice_date between :from and :to limit 1))");
				}
				
				/*if( reqDto.getVisitTypeId()!=null) {
					query = query.append(" and inv.company_invoice_id  in ((select invc.company_invoice_id from billing.m_company_invoice_master invc " + 
							"	inner join billing.t_company_invoice_details tcid on invc.company_invoice_id  = tcid.company_invoice_id " + 
							"	inner join billing.m_billing_master mbm on tcid.bill_id  = mbm.billing_master_id " + 
							"	inner join patient.t_patient_registration tpr on tpr.patient_id  = mbm.patient_id " + 
//							"	where lower(tpr.uhid_number)  like '%"+reqDto.getUhidNumber().toLowerCase()+"%' " +
							"   where  invc.organisation_id = " +reqDto.getOrgId() +
							"   and  mbm.visit_type_id = " +reqDto.getVisitTypeId() +
							"	and invc.unit_id =  " + reqDto.getUnitId()+
							"	and invc.due_amount > 0 " + 
							"	and invc.invoice_date between '"+from+"' and '"+to+"'  limit 1) " + 
							"	, (select invnc.company_invoice_id from billing.m_company_invoice_master invnc" + 
							"	inner join billing.t_company_invoice_details tcid on invnc.company_invoice_id  = tcid.company_invoice_id " + 
							"	inner join pharmacy.t_sale sale on sale.id = tcid.t_sale_id " + 
							"	inner join patient.t_patient_registration tpr on tpr.patient_id  = sale.patient_id " + 
//							"	where lower(tpr.uhid_number)  like '%"+reqDto.getUhidNumber().toLowerCase()+"%' " +
							"   where  invnc.organisation_id = " +reqDto.getOrgId() +
							"   and  sale.visit_type_id = " +reqDto.getVisitTypeId() +
							"	and invnc.unit_id =  " + reqDto.getUnitId()+
							"	and invnc.due_amount > 0 " + 
							"	and invnc.invoice_date between '"+from+"' and '"+to+"' limit 1))");
				}*/
				
				
				
				
				query.append(" and inv.invoice_date between :from and :to  order by inv.company_invoice_id desc");
				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", reqDto.getOrgId())
						.setParameter("unitId", reqDto.getUnitId())
						.setParameter("from", from)
						.setParameter("to", to);

				if(reqDto.getCompanyId() != null)
					sqlQuery.setParameter("companyId", reqDto.getCompanyId());

				if(reqDto.getAssociateCompanyId() != null)
					sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

				if(reqDto.getVisitTypeId() != null)
					sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

				if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty() && reqDto.getToDate() != null && !reqDto.getToDate().isEmpty()){
					if(reqDto.getUhidNumber()!="" && reqDto.getUhidNumber()!=null) {
						sqlQuery.setParameter("uhidNumber","%" + reqDto.getUhidNumber().toLowerCase() + "%")
								.setParameter("orgId", reqDto.getOrgId())
								.setParameter("unitId", reqDto.getUnitId())
								.setParameter("from", from)
								.setParameter("to", to);
					}
				}

				List<CompanyInvoiceSettlementListResDto> result = sqlQuery
						.setResultTransformer(Transformers.aliasToBean(CompanyInvoiceSettlementListResDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, result, null);
			}else{
				query.append(" order by inv.company_invoice_id desc ");
				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", reqDto.getOrgId())
						.setParameter("unitId", reqDto.getUnitId());

				if(reqDto.getCompanyId() != null)
					sqlQuery.setParameter("companyId", reqDto.getCompanyId());

				if(reqDto.getAssociateCompanyId() != null)
					sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

				if(reqDto.getVisitTypeId() != null)
					sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

				List<CompanyInvoiceSettlementListResDto> result = sqlQuery.setResultTransformer(Transformers.aliasToBean(CompanyInvoiceSettlementListResDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, result, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCompanyInvoiceDueAmount(int id, double amount) throws BillingException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_COMPANY_INVOICE_MASTER_DUE_AMOUNT").setParameter("id", id).setParameter("amount", amount).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCompanyInvoiceDueAmount(Double availableDeposit, Integer companyReceiptsExpenseId)
			throws BillingException {
		try{
			SQLQuery result = sessionFactory.getCurrentSession().createSQLQuery("update billing.t_company_receipts_expense set balance_amount = (:availableDeposit) "
				+ "where company_receipts_expense_id =:companyReceiptsExpenseId");
			result.setParameter("availableDeposit", availableDeposit);
			result.setParameter("companyReceiptsExpenseId", companyReceiptsExpenseId);
			result.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCompanyInvoiceSettlementDate(int id, double amount) throws BillingException {
		try{
	
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_COMPANY_INVOICE_MASTER_SETTLEMENT_DATE").setParameter("id", id).setParameter("updatedDate",
					GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss")).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	

	

}
