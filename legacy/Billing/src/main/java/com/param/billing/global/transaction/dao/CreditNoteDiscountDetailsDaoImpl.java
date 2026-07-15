package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.dto.CreditNoteDiscountDetailsDto;
import com.param.billing.global.transaction.model.CreditNoteDiscoutDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class CreditNoteDiscountDetailsDaoImpl extends GenericDao<CreditNoteDiscoutDetails, Integer> implements ICreditNoteDiscountDetailsDao, ICommonConstants  {

	public CreditNoteDiscountDetailsDaoImpl() {
		super(CreditNoteDiscoutDetails.class);
	}

	@Override
	public Response saveCreditNoteDiscountDetails(CreditNoteDiscountDetailsDto creditNoteDiscountDetailsDto)
			throws ApplicationException {
		try{
			CreditNoteDiscoutDetails master = new CreditNoteDiscoutDetails();
			master.setCreditNoteDetailId(creditNoteDiscountDetailsDto.getCreditNoteDetailId());
			master.setBillingDetailsId(creditNoteDiscountDetailsDto.getBillingDetailsId());
			master.setDiscountCategoryId(creditNoteDiscountDetailsDto.getDiscountCategoryId());
			master.setDiscountPercentage(creditNoteDiscountDetailsDto.getDiscountPercentage());
			master.setDiscountAmount(creditNoteDiscountDetailsDto.getDiscountAmount());
			master.setDiscountGivenBy(creditNoteDiscountDetailsDto.getDiscountGivenBy());
			master.setOrgnisationId(creditNoteDiscountDetailsDto.getOrgnisationId());
			master.setUnitId(creditNoteDiscountDetailsDto.getUnitId());
			master.setCreatedBy(creditNoteDiscountDetailsDto.getCreatedBy());
			master.setCreatedDate(creditNoteDiscountDetailsDto.getCreatedDate() != null && !creditNoteDiscountDetailsDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteDiscountDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteDiscountDetailsDto.getUnitId()));
			master.setUpdatedBy(creditNoteDiscountDetailsDto.getUpdatedBy());
			master.setUpdatedDate(creditNoteDiscountDetailsDto.getUpdatedDate() != null && !creditNoteDiscountDetailsDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(creditNoteDiscountDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(creditNoteDiscountDetailsDto.getUnitId()));
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response creditNoteDiscountDetails(int billingDetailsId) throws BillingException {
		try {
			List<CreditNoteDiscountDetailsDto> listCreditNoteDiscountDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_CREDIT_NOTE_DISOUNCT_DETAILS")
					.setParameter("billingDetailsId", billingDetailsId)
					.setResultTransformer(Transformers.aliasToBean(CreditNoteDiscountDetailsDto.class)).list();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listCreditNoteDiscountDetailsDto, null);
		}catch (Exception e) {
			
			throw e;
		}
	}

}
