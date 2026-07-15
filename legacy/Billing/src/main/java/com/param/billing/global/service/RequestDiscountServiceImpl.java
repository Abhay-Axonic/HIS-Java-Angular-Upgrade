package com.param.billing.global.service;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.config.dao.IRequestDiscountDao;
import com.param.billing.global.transaction.dao.ICreditNoteMasterDao;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.model.CreditNoteDetails;
import com.param.billing.global.transaction.model.CreditNoteMaster;
import com.param.billing.global.transaction.service.ICreditNoteMasterService;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.service.IOrderDetailsMasterService;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class RequestDiscountServiceImpl implements IRequestDiscountService,ICommonConstants {

	final static Logger logger = Logger.getLogger(RequestDiscountServiceImpl.class);

	@Autowired
	IRequestDiscountDao iRequestDiscountDao;

	@Autowired
	IBillingDao iBillingDao;

	@Autowired
	ICreditNoteMasterService iCreditNoteMasterService;

	@Autowired
	ICreditNoteMasterDao iCreditNoteMasterDao;

	@Autowired
	IOrderDetailsMasterService IOrderDetailsMasterService;

	@Override
	@Transactional
	public Response requestDiscount(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			requestDiscountDto.setRequestNumber(this.gererateDiscountRequest());
			Response res = iRequestDiscountDao.requestDiscount(requestDiscountDto);
			if(res.getStatus().equals(SUCCESS))
			{
				if(requestDiscountDto.getRequestDiscountDetailsDtosList()!=null && requestDiscountDto.getRequestDiscountDetailsDtosList().size()>0){
				Integer reqId = (Integer) res.getObject();
				ListIterator<RequestDiscountDetailsDto> iterator = requestDiscountDto.getRequestDiscountDetailsDtosList().listIterator();
				while(iterator.hasNext())
				{
					RequestDiscountDetailsDto detailsDto = iterator.next();
					detailsDto.setReqDiscId(reqId);
					iRequestDiscountDao.requestDiscountDetails(detailsDto);
				  }
				}
			}
			return res;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	private String gererateDiscountRequest() {
		  String SALTCHARS = "1234567890";
	      StringBuilder salt = new StringBuilder("REQDIS");
	      Random rnd = new Random();
	      while (salt.length() < 10) {
	          int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	          salt.append(SALTCHARS.charAt(index));
	      }
	      String saltStr = salt.toString();
	      return saltStr;
	}

	@Override
	@Transactional
	public Response getRequestedDiscount(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
				Response res = iRequestDiscountDao.getRequestedDiscount(requestDiscountDto);
				ListIterator<RequestDiscountDto> iterator = res.getListObject().listIterator();
				while(iterator.hasNext())
				{
					RequestDiscountDto discountDto = iterator.next();

					Response res2 = iRequestDiscountDao.getRequestDetails(discountDto.getReqDiscId());

					discountDto.setRequestDiscountDetailsDtosList(res2.getListObject());
				}
				return res;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response approveRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			Response res = iRequestDiscountDao.approveRequest(requestDiscountDto);
			if(res.getStatus().equals(SUCCESS))
			{
				ListIterator<RequestDiscountDetailsDto> iterator = requestDiscountDto.getRequestDiscountDetailsDtosList().listIterator();
				while(iterator.hasNext())
				{
					RequestDiscountDetailsDto detailsDto = iterator.next();
					iRequestDiscountDao.approveRequestDetails(detailsDto);
				}

			}
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response approveRequestForFinancialClearance(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{

			if(requestDiscountDto.getAdmissionId()!=null)
			{
				if(requestDiscountDto.getReqDiscId() != null){
				Response res = iRequestDiscountDao.approveRequestForFinancialClearance(requestDiscountDto);
				}
					iRequestDiscountDao.updateAdmissionsIsFinancialClearance(requestDiscountDto.getAdmissionId());
					iRequestDiscountDao.updateDischargeStatusAfterFinancialClearance(requestDiscountDto);
					iBillingDao.changeBillStatus(requestDiscountDto.getBillId());
			}else{
				if(requestDiscountDto.getReqDiscId() != null){
					Response res = iRequestDiscountDao.approveRequestForFinancialClearance(requestDiscountDto);
					}
				iRequestDiscountDao.updateEncounterIsFinancialClearance(requestDiscountDto.getEncounterId());
				iRequestDiscountDao.updateEncounterMasterStatus(requestDiscountDto);
				iBillingDao.changeBillStatus(requestDiscountDto.getBillId());
			}
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			Response res =  iRequestDiscountDao.cancelRequest(requestDiscountDto);
			if(requestDiscountDto.getCreditNoteId()!=null && requestDiscountDto.getCreditNoteId()>0)
			{
				Response orderDetailsRes =  iRequestDiscountDao.getOrderDetailsIdByCreditNoteId(requestDiscountDto);
				if(orderDetailsRes.getListObject()!= null && orderDetailsRes.getListObject().size()>0){
					List<RequestDiscountDto> creditDetailsObj = orderDetailsRes.getListObject();

					for (RequestDiscountDto dto : creditDetailsObj) {

						IOrderDetailsMasterService.opdateOrderDetailsAndDoctorShare(dto);
					}

				}
			}

			return res;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDiscountRequestDetailsForOPBilling(RequestDiscountDto requestDiscountDto)
			throws ApplicationException {
		try{
			Response res = iRequestDiscountDao.getDiscountRequestForOPBilling(requestDiscountDto);
			ListIterator<RequestDiscountDto> iterator = res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				RequestDiscountDto discountDto = iterator.next();

				Response res2 = iRequestDiscountDao.getRequestDetails(discountDto.getReqDiscId());

				discountDto.setRequestDiscountDetailsDtosList(res2.getListObject());
			}
			return res;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getRequestedDiscountsForDashboard(RequestDiscountDto requestDiscountDto)
			throws ApplicationException {
		try{
			Response res = iRequestDiscountDao.getRequestedDiscountsForDashboard(requestDiscountDto);
			ListIterator<RequestDiscountDto> iterator = res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				RequestDiscountDto discountDto = iterator.next();

				Response res2 = iRequestDiscountDao.getRequestDetails(discountDto.getReqDiscId());

				discountDto.setRequestDiscountDetailsDtosList(res2.getListObject());
			}
			return res;
	}catch(Exception e){
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	@Transactional
	public Response approveCnRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			Response res = iRequestDiscountDao.approveCnRequest(requestDiscountDto);
			if(res.getStatus().equals(SUCCESS))
			{
				iRequestDiscountDao.updateCnRequest(requestDiscountDto);

				if(requestDiscountDto.getCreditNoteId() != null && requestDiscountDto.getCreditNoteId() > 0) {
					CreditNoteMaster creditNoteMaster = iCreditNoteMasterDao.findById(requestDiscountDto.getCreditNoteId());
					iCreditNoteMasterService.applyCreditNoteOnBill(creditNoteMaster, requestDiscountDto.getSelfPayeeId());
					CreditNoteMasterDto creditNoteMasterDto = new CreditNoteMasterDto();
						creditNoteMasterDto.setCreditNoteId(creditNoteMaster.getCreditNoteId());
						creditNoteMasterDto.setUnitId(creditNoteMaster.getUnitId());
					List<CreditNoteDetailsDto> creditNoteDetailsDtosList = new LinkedList<CreditNoteDetailsDto>();
					for(CreditNoteDetails creditNoteDetails : creditNoteMaster.getListCreditNoteDetails())
					{
						CreditNoteDetailsDto creditNoteDetailsDto = new CreditNoteDetailsDto();
							creditNoteDetailsDto.setOrderDetailsId(creditNoteDetails.getOrderDetailId());
							creditNoteDetailsDto.setCancelOrDiscount(creditNoteDetails.getCancelOrDiscount());
						creditNoteDetailsDtosList.add(creditNoteDetailsDto);
					}
					creditNoteMasterDto.setListCreditNoteDetails(creditNoteDetailsDtosList);
					return new Response<>(SUCCESS, null, null, null, creditNoteMasterDto);
				}

				return new Response<>(SUCCESS, null, null, null, null);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response approveExpenseRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			Response res = iRequestDiscountDao.approveCnRequest(requestDiscountDto);
			if(res.getStatus().equals(SUCCESS))
			{
				if(requestDiscountDto.getExpenseRemark() != null || requestDiscountDto.getCompanyReciptRemark() != null) {
					Response resq = iRequestDiscountDao.updateExpenseRemarkRequest(requestDiscountDto);
				}
			}
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response completeCreditNoteDiscountRequest(Integer reqDiscId) throws ApplicationException {
		try{
			return iRequestDiscountDao.completeCreditNoteDiscountRequest(reqDiscId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getBadDebitRequestObject(RequestDiscountDto requestDiscountDto) throws ApplicationException {

		try{
			Response res = iRequestDiscountDao.getBadDebitRequestObject(requestDiscountDto);
			ListIterator<RequestDiscountDto> iterator = res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				RequestDiscountDto discountDto = iterator.next();

				Response res2 = iRequestDiscountDao.getRequestDetails(discountDto.getReqDiscId());

				discountDto.setRequestDiscountDetailsDtosList(res2.getListObject());
			}
			return res;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response approveCompanyReceiptsExpenseRequest(RequestDiscountDto requestDiscountDto) {
		try{
			Response res = iRequestDiscountDao.approveCnRequest(requestDiscountDto);
			/*if(res.getStatus().equals(SUCCESS))
			{
				iRequestDiscountDao.updateExpenseRequest(requestDiscountDto);
			}*/
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getUHIDMergeReuestApprovalData(RequestDiscountDto requestDiscountDto) {
		try{

			return iRequestDiscountDao.getUHIDMergeReuestApprovalData(requestDiscountDto);

		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelRequestFinancialRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			Response res =  iRequestDiscountDao.cancelRequestFinancialRequest(requestDiscountDto);
			return res;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response requestDiscountBillList(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try{
			requestDiscountDto.setRequestNumber(this.gererateDiscountRequest());
			Response res = iRequestDiscountDao.requestDiscountBillList(requestDiscountDto);
			/*if(res.getStatus().equals(SUCCESS))
			{
				if (requestDiscountDto.getIsFinancialClearance().equals('Y') && requestDiscountDto.getVisitTypeId()!=1)
                {
                    iRequestDiscountDao.updateAdmissionsIsFinancialClearance(requestDiscountDto.getAdmissionId());
                }else if(requestDiscountDto.getIsFinancialClearance().equals('Y') && requestDiscountDto.getVisitTypeId()==1)
                {
                	iRequestDiscountDao.updateEncounterIsFinancialClearance(requestDiscountDto.getEncounterId());
                }
			}*/
			return res;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
