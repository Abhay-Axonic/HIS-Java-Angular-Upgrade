package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.billing.global.transaction.model.TCompanyInvoiceServiceDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TCompanyInvoiceServiceDetailsDaoImpl extends GenericDao<TCompanyInvoiceServiceDetails, Integer> implements ITCompanyInvoiceServiceDetailsDao,ICommonConstants{

	public TCompanyInvoiceServiceDetailsDaoImpl() {
		super(TCompanyInvoiceServiceDetails.class);
	}

	
	@Override
	public Response saveCompanyInvoiceServiceDetails(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto)
			throws ApplicationException {
		try{
			TCompanyInvoiceServiceDetails companyInvoiceServiceDetails = new TCompanyInvoiceServiceDetails();
				companyInvoiceServiceDetails.setInvoiceId(companyInvoiceServiceDetailsDto.getInvoiceId());
				companyInvoiceServiceDetails.setInvoiceDetailId(companyInvoiceServiceDetailsDto.getInvoiceDetailId());
				companyInvoiceServiceDetails.setServiceId(companyInvoiceServiceDetailsDto.getServiceId());
				companyInvoiceServiceDetails.setCreditPayableAmount(companyInvoiceServiceDetailsDto.getCreditPayableAmount());
				companyInvoiceServiceDetails.setCreatedBy(companyInvoiceServiceDetailsDto.getCreatedBy());
				companyInvoiceServiceDetails.setReceivedAmount(companyInvoiceServiceDetailsDto.getReceivedAmount());
				companyInvoiceServiceDetails.setDisallowedAmount(companyInvoiceServiceDetailsDto.getDisallowedAmount());
				companyInvoiceServiceDetails.setDueAmount(companyInvoiceServiceDetailsDto.getDueAmount());
				companyInvoiceServiceDetails.setCrNetAmount(companyInvoiceServiceDetailsDto.getCrNetAmount());
				companyInvoiceServiceDetails.setOrgId(companyInvoiceServiceDetailsDto.getOrgId());
				companyInvoiceServiceDetails.setOrgUnitId(companyInvoiceServiceDetailsDto.getOrgUnitId());
				companyInvoiceServiceDetails.setUpdatedBy(companyInvoiceServiceDetailsDto.getUpdatedBy());
				companyInvoiceServiceDetails.setStatus('A');
				companyInvoiceServiceDetails.setBillDetailId(companyInvoiceServiceDetailsDto.getBillDetailId());
				companyInvoiceServiceDetails.setCreatedBy(1);
				companyInvoiceServiceDetails.setUpdatedBy(1);
				companyInvoiceServiceDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(companyInvoiceServiceDetailsDto.getOrgUnitId()), "dd-M-yyyy HH:mm:ss"));
				companyInvoiceServiceDetails.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(companyInvoiceServiceDetailsDto.getOrgUnitId()), "dd-M-yyyy HH:mm:ss"));
				companyInvoiceServiceDetails.setItemId(companyInvoiceServiceDetailsDto.getItemId());
				companyInvoiceServiceDetails.settSaleDetailsId(companyInvoiceServiceDetailsDto.gettSaleDetailsId());
			companyInvoiceServiceDetails = save(companyInvoiceServiceDetails);
			return new  Response(SUCCESS, SUCCESS_CODE, null, null, companyInvoiceServiceDetails);
			
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getCompInvServDetailsByInvDtls(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto)
			throws ApplicationException {
		try{
			List<TCompanyInvoiceServiceDetailsDto> listTCompanyInvoiceServiceDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_COMP_INVOICE_SERVICE_DTLS_BY_INVOICE")
						.setParameter("invoiceDetailId", companyInvoiceServiceDetailsDto.getInvoiceDetailId()).
						setResultTransformer(Transformers.aliasToBean(TCompanyInvoiceServiceDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTCompanyInvoiceServiceDetailsDto, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response updateRecvAndDisallAmtByInvDtls(TCompanyInvoiceServiceDetailsDto dto)
			throws ApplicationException {
		try{
			//Ganesh
			StringBuilder sb = new StringBuilder("update billing.t_company_invoice_service_details set received_amount = coalesce(received_amount, 0)+:receivedAmount , "
					+ "disallowed_amount = coalesce(disallowed_amount, 0)+:disallowedAmount , "
					+ "due_amount = :dueAmount, "
					+ "tds_amount=coalesce(tds_amount, 0)+:tdsAmount, "
					+ "other_deduction=coalesce(other_deduction, 0)+:otherDeduction "
					+ "where invoice_detail_id =:invoiceDetailId ");

			if((dto.getServiceId()!=null&&dto.getServiceId()>0))
				sb.append(" and service_id =:serviceId ");

			if(dto.getItemId()!=null&&dto.getItemId()>0)
				sb.append(" and item_id =:itemId ");

			if(dto.getCompInvSerDetlsId()!=null&&dto.getCompInvSerDetlsId()>0)
				sb.append(" and comp_inv_ser_detls_id =:compInvSerDetailsId ");

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).setParameter("invoiceDetailId", dto.getInvoiceDetailId())
							.setParameter("receivedAmount", dto.getReceivedAmount())
							.setParameter("dueAmount", dto.getDueAmount())
							.setParameter("otherDeduction", dto.getOtherDeduction())
							.setParameter("disallowedAmount" , dto.getDisallowedAmount())
							.setParameter("tdsAmount" , dto.getTdsAmount());

			if((dto.getServiceId()!=null&&dto.getServiceId()>0))
				sqlQuery.setParameter("serviceId", dto.getServiceId());

			if((dto.getItemId()!=null&&dto.getItemId()>0))
				sqlQuery.setParameter("itemId", dto.getItemId());

			if((dto.getCompInvSerDetlsId()!=null&&dto.getCompInvSerDetlsId()>0))
				sqlQuery.setParameter("compInvSerDetailsId", dto.getCompInvSerDetlsId());

			Integer result = sqlQuery.executeUpdate();
			
			if(result != null){
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAmountDtlsByCompServInvId(int compInvSerDetlsId)throws ApplicationException {
		try{
			List<TCompanyInvoiceServiceDetailsDto> listTCompanyInvoiceServiceDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_AMT_DTLS_BY_SERVICE_AND_INV_DTLS_ID").setParameter("compInvSerDetlsId", compInvSerDetlsId).setResultTransformer(Transformers.aliasToBean(TCompanyInvoiceServiceDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTCompanyInvoiceServiceDetailsDto, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
