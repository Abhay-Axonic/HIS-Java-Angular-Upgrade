package com.param.billing.global.transaction.dao;

import java.util.List;
import java.util.ListIterator;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.dto.CompanyInvoiceDetailsResDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceDetailsDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.billing.global.transaction.model.MCompanyInvoiceMaster;
import com.param.billing.global.transaction.model.TCompanyInvoiceDetails;
import com.param.billing.global.transaction.model.TCompanyInvoiceServiceDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TCompanyInvoiceDetailsDaoImpl extends GenericDao<TCompanyInvoiceDetails, Integer> implements ITCompanyInvoiceCreationDao, ICommonConstants{

	public TCompanyInvoiceDetailsDaoImpl() {
		super(TCompanyInvoiceDetails.class);
	}

	@Override
	public com.param.global.common.Response saveCompanyInvoiceCreationMasterList(TCompanyInvoiceDetailsDto masterDto)
			throws ServiceException {
		try{
			TCompanyInvoiceDetails tCompanyInvoiceDetails = new TCompanyInvoiceDetails();
				tCompanyInvoiceDetails.setCreatedBy(masterDto.getCreatedBy());
				tCompanyInvoiceDetails.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()).getTime());
				tCompanyInvoiceDetails.setOrgId(masterDto.getOrgId());
				tCompanyInvoiceDetails.setUnitId(masterDto.getUnitId());
				tCompanyInvoiceDetails.setCompanyInvoiceId(masterDto.getCompanyInvoiceId());
				tCompanyInvoiceDetails.setBillId(masterDto.getBillId());
				tCompanyInvoiceDetails.setBillPayeeId(masterDto.getBillPayeeId());
				tCompanyInvoiceDetails.setDueAmount(masterDto.getDueAmount());
				tCompanyInvoiceDetails.setBillAmount(masterDto.getBillAmount());
				tCompanyInvoiceDetails.setCrNetAmount(masterDto.getCrNetAmount());
				tCompanyInvoiceDetails.setPayableAmount(masterDto.getPayableAmount());
				tCompanyInvoiceDetails.settSaleId(masterDto.gettSaleId());
				tCompanyInvoiceDetails.setAssociateCompanyId(masterDto.getAssociateCompanyId());
				tCompanyInvoiceDetails.setBillPayeeDetailsId(masterDto.getBillPayeeDetailsId());
			tCompanyInvoiceDetails = save(tCompanyInvoiceDetails);
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, null, tCompanyInvoiceDetails);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getCompanyInvoiceDetailsListByInvoiceId(int invoiceId) throws ApplicationException { 
		try{
			List<CompanyInvoiceDetailsResDto> listCompanyInvoiceDetailsResDto = sessionFactory.getCurrentSession().getNamedQuery("GET_INVOICE_DETAILS_FOR_SETTLEMENT_BY_INVOICE_ID")
					.setInteger("invoiceId", invoiceId)
							.setResultTransformer(Transformers.aliasToBean(CompanyInvoiceDetailsResDto.class)).list();
			
			
			ListIterator<CompanyInvoiceDetailsResDto> invoDtlsItr = listCompanyInvoiceDetailsResDto.listIterator();
			
			while(invoDtlsItr.hasNext()){
				
				CompanyInvoiceDetailsResDto comInvDetailsDto = invoDtlsItr.next();
				Integer compantInvDetailsId = comInvDetailsDto.getCompanyInvoiceDetailId();
				Integer organisationId = comInvDetailsDto.getOrganisationId();
				Integer unitId = comInvDetailsDto.getUnitId();
				
				List<TCompanyInvoiceServiceDetailsDto> listTCompanyInvoiceServiceDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_COMP_INVOICE_SERVICE_DTLS_BY_INVOICE")
						.setParameter("invoiceDetailId", compantInvDetailsId)
						.setParameter("organisationId", organisationId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(TCompanyInvoiceServiceDetailsDto.class)).list();
				comInvDetailsDto.settCompanyInvoiceServiceDetailsDto(listTCompanyInvoiceServiceDetailsDto);
				comInvDetailsDto.settCompanyInvoiceServiceDetailsDtoOld(listTCompanyInvoiceServiceDetailsDto);
				
				StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SPONSOR_DETAILS_BY_ADMISSION_FOR_UPDATE_PAYEE").getQueryString());
				
				if(comInvDetailsDto.getAdmissionId()!=null && comInvDetailsDto.getAdmissionId()>0)
					query.append(" 	AND dtls.admission_id =:admissionId ");

				if(comInvDetailsDto.getEncounterId()!=null && comInvDetailsDto.getEncounterId()>0)
					query.append(" AND	dtls.encounter_id =:encounterId ");

				if(comInvDetailsDto.getBillPayeeId()!=null && comInvDetailsDto.getBillPayeeId()>0)
					query.append(" AND	dtls.payee_id =:billPayeeId ");

				query.append(" order by  1 Desc limit 1 ");
				
				Query sqlQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString());

				if(comInvDetailsDto.getAdmissionId()!=null && comInvDetailsDto.getAdmissionId()>0)
					sqlQuery.setParameter("admissionId", comInvDetailsDto.getAdmissionId());

				if(comInvDetailsDto.getEncounterId()!=null && comInvDetailsDto.getEncounterId()>0)
					sqlQuery.setParameter("encounterId", comInvDetailsDto.getEncounterId());

				if(comInvDetailsDto.getBillPayeeId()!=null && comInvDetailsDto.getBillPayeeId()>0)
					sqlQuery.setParameter("billPayeeId", comInvDetailsDto.getBillPayeeId());

				List<PatientSponsorDetailsDto> listsponserdetails = sqlQuery
						.setResultTransformer(Transformers.aliasToBean(PatientSponsorDetailsDto.class)).list();

				if(!listsponserdetails.isEmpty()){
				comInvDetailsDto.setClaimNumber(listsponserdetails.get(0).getClaimNumber());
				}
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listCompanyInvoiceDetailsResDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateDueAmount(int id, double amount) throws ApplicationException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_COMPANY_INVOICE_DETAILS_DUE_AMOUNT").setParameter("id", id).setParameter("amount", amount).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateInvoiceDueAmountByInvoiceDtlsId(int id, double amount) throws ApplicationException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_COMPANY_INVOICE_MASTER_DUE_AMOUNT_BY_DTLS_ID").setParameter("id", id).setParameter("amount", amount).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response getCompanyInvoiceDetailsByBillDetails(Integer billId,Integer billPayeeDetailsId) throws ApplicationException {
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TCompanyInvoiceDetails.class);
			c.add(Restrictions.eq("billPayeeDetailsId", billPayeeDetailsId));
			c.add(Restrictions.eq("billId", billId));
			
			if(c.list()!=null && c.list().size()>0)
			{
			
				TCompanyInvoiceDetails companyInvoiceDetails = (TCompanyInvoiceDetails) c.list().get(0);
				
				MCompanyInvoiceMaster companyInvoiceMaster = (MCompanyInvoiceMaster) sessionFactory.getCurrentSession().get(MCompanyInvoiceMaster.class, companyInvoiceDetails.getCompanyInvoiceId());
	
				Criteria serviceDetails = sessionFactory.getCurrentSession().createCriteria(TCompanyInvoiceServiceDetails.class);
				serviceDetails.add(Restrictions.eq("invoiceId", companyInvoiceDetails.getCompanyInvoiceId()));
				serviceDetails.add(Restrictions.eq("invoiceDetailId", companyInvoiceDetails.getCompanyInvoiceDetailId()));
				serviceDetails.add(Restrictions.eq("status", 'A'));
				List<TCompanyInvoiceServiceDetails> companyInvoiceServiceDetailsList = serviceDetails.list();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, companyInvoiceServiceDetailsList,companyInvoiceDetails,companyInvoiceMaster);
			}
			return null;
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public void updateTCompanyInvoiceDetails(TCompanyInvoiceDetails companyInvoiceDetails) {
		try{
			sessionFactory.getCurrentSession().update(companyInvoiceDetails);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public void updateTCompanyInvoiceServiceDetails(TCompanyInvoiceServiceDetails details) {
		try{
			sessionFactory.getCurrentSession().update(details);
		}catch(Exception e){
			
			throw e;
		}
		
	}
	
}
