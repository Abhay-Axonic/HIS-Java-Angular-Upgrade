package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.CreditNoteDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class CreditNoteDetailsDaoImpl extends GenericDao<CreditNoteDetails, Integer> implements ICreditNoteDetailsDao, ICommonConstants{

	public CreditNoteDetailsDaoImpl() {
		super(CreditNoteDetails.class);
	}

	@Override
	public Response saveCreditNoteDetails(CreditNoteDetailsDto creditNoteDetailsDto) throws BillingException 
	{
		try{
			CreditNoteDetails creditNoteDetails = new CreditNoteDetails();
				creditNoteDetails.setCreditNoteId(creditNoteDetailsDto.getCreditNoteId());
				creditNoteDetails.setCrNoteDate(creditNoteDetailsDto.getCrNoteDate() != null && !creditNoteDetailsDto.getCrNoteDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteDetailsDto.getCrNoteDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteDetailsDto.getUnitId()));
				creditNoteDetails.setOrderDtlIdOld(creditNoteDetailsDto.getOrderDtlIdOld());
				creditNoteDetails.setOrderDetailId(creditNoteDetailsDto.getOrderDetailId());
				creditNoteDetails.setOrderDetailIdNew(creditNoteDetailsDto.getOrderDetailIdNew());
				creditNoteDetails.setCrDiscountAmount(creditNoteDetailsDto.getCrDiscountAmount());
				creditNoteDetails.setBillingDetailsId(creditNoteDetailsDto.getBillingDetailsId());
				creditNoteDetails.setCrServiceRateAmount(creditNoteDetailsDto.getCrServiceRateAmount());
				creditNoteDetails.setCrAdditionalDiscount(creditNoteDetailsDto.getCrAdditionalDiscount());
				creditNoteDetails.setCrTaxAmount(creditNoteDetailsDto.getCrTaxAmount());
				creditNoteDetails.setCrNetAmount(creditNoteDetailsDto.getCrNetAmount());
				creditNoteDetails.setOrganisationId(creditNoteDetailsDto.getOrganisationId());
				creditNoteDetails.setUnitId(creditNoteDetailsDto.getUnitId());
				creditNoteDetails.setCreatedBy(creditNoteDetailsDto.getCreatedBy());
				creditNoteDetails.setCreatedDate(creditNoteDetailsDto.getCreatedDate() != null && !creditNoteDetailsDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteDetailsDto.getUnitId()));
				creditNoteDetails.setUpdatedBy(creditNoteDetailsDto.getUpdatedBy());
				creditNoteDetails.setSelfPayable(creditNoteDetailsDto.getSelfPayable());
				creditNoteDetails.setCreditPayable(creditNoteDetailsDto.getCreditPayable());
				creditNoteDetails.setPackageConsumptionAmt(creditNoteDetailsDto.getPackageConsumptionAmt());
				creditNoteDetails.setUpdatedDate(creditNoteDetailsDto.getUpdatedDate() != null && !creditNoteDetailsDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteDetailsDto.getUnitId()));
				creditNoteDetails.setCancelOrDiscount(creditNoteDetailsDto.getCancelOrDiscount());
				creditNoteDetails.setStatus('A');
			creditNoteDetails = save(creditNoteDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, creditNoteDetails);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

    @Override
    public List<TDoctorShareRespDto> getCrDetailsOrderDetailsId(Integer orderDetailsId) {
        try{
            String query = " select " 
            		 + " 	tds.doctor_share_id as \"doctorShareId\", " 
            		 + " 	coalesce ( " 
            		 + " 	(select " 
            		 + " 		sum(cr_net_amount) " 
            		 + " 	from " 
            		 + " 		billing.t_credit_note_detail tcnd " 
            		 + " 	where " 
            		 + " 		order_detail_id =:ordDtlId " 
            		 + " 		and status = 'A'),0) as \"crNetAmount\", " 
            		 + " 	tds.referral_id as \"referralId\", " 
            		 + " 	tds.doc_id as \"docId\", "
            		 + "	tds.bill_dtl_id as \"billDtlId\" " 
            		 + " from " 
            		 + " 	billing.t_doctor_share tds " 
            		 + " where " 
            		 + " 	ord_dtl_id =:ordDtlId " 
            		 + " 	and processed_share_amt is null " 
            		 + " 	and tds.status ='A' ";
            
            List<TDoctorShareRespDto> tDoctorShareRespDtoList= sessionFactory.getCurrentSession().createSQLQuery(query)
            	.setParameter("ordDtlId", orderDetailsId)
            .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
            
            return tDoctorShareRespDtoList;
        }catch(Exception e){
            
        }
        return null;
    }

    /*@Override
    public void updateDocSahre(TDoctorShareRespDto tDoctorShareRespDto) {
        // TODO Auto-generated method stub
        
    }*/

	
	
}
