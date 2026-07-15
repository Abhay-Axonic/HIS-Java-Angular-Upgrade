package com.param.billing.global.transaction.service;

import in.param.exception.ApplicationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.dto.BillListResDto;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.transaction.dao.IBillPayeeDetailsDao;
import com.param.billing.global.transaction.dao.IDoctorShareCRNoteDao;
import com.param.billing.global.transaction.dto.DoctorAdvanceDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDetailsDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.utility.TimeZoneComponent;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class DoctorShareCRNoteServiceImpl implements IDoctorShareCRNoteService,ICommonConstants
{

	@Autowired
	IDoctorShareCRNoteDao iDoctorShareCRNoteDao;
	@Autowired
	IBillingDao iBillingDao;
	@Autowired
	IBillPayeeDetailsDao iBillPayeeDetailsDao;
	@Autowired
	private IBillingMasterService iBillingMasterService;
	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao; 
	
	@Override
	@Transactional
	public Response getPendingBills(BillListReqDto reqDto) {
		try{
			List<BillListResDto> finalListBills = new ArrayList<>();
			Response res = iDoctorShareCRNoteDao.getPendingBills(reqDto);
			if(res.getListObject()!=null && res.getListObject().size()>0)
			{
				List<BillListResDto> listBills = res.getListObject(); 
				for(BillListResDto billList : listBills)
				{
					Response resDoctor = iDoctorShareCRNoteDao.doctorsListForDoctorCr(billList.getBillingMasterId());
					if((resDoctor.getListObject()!=null && resDoctor.getListObject().size()>0) || resDoctor.getObject()!=null)
					{
						finalListBills.add(billList);
					}
				}
			}
			if(finalListBills.size()>0) {
				
			
			if(reqDto.getOffset()!=null  && reqDto.getNoOfRecordsPerPage()!=null) {
				//pagination list
				int record=finalListBills.size() - ( reqDto.getOffset()+reqDto.getNoOfRecordsPerPage());
				if(record<0) {
					List<BillListResDto> finalListBillssublist=finalListBills.subList(reqDto.getOffset(), finalListBills.size());
					return new Response<>(SUCCESS, null, null, finalListBillssublist, finalListBills.size());	
				}
				List<BillListResDto> finalListBillssublist=finalListBills.subList(reqDto.getOffset(), reqDto.getOffset()+reqDto.getNoOfRecordsPerPage());
				return new Response<>(SUCCESS, null, null, finalListBillssublist, finalListBills.size());	
			    }
			}
			else {
				//old list request
				return new Response<>(SUCCESS, null, null, finalListBills, null);
			}
			
		}catch(Exception e)
		{
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response getPendingBillsSearch(BillListReqDto reqDto) {
		try{
			List<BillListResDto> finalListBills = new ArrayList<>();
			Response res = iDoctorShareCRNoteDao.getPendingBillsSearch(reqDto);
			if(res.getListObject()!=null && res.getListObject().size()>0)
			{
				List<BillListResDto> listBills = res.getListObject(); 
				for(BillListResDto billList : listBills)
				{
					Response resDoctor = iDoctorShareCRNoteDao.doctorsListForDoctorCr(billList.getBillingMasterId());
					if(resDoctor.getListObject()!=null && resDoctor.getListObject().size()>0)
					{
						finalListBills.add(billList);
					}
				}
			}
			if(finalListBills.size()>0) {
				
			
			if(reqDto.getOffset()!=null  && reqDto.getNoOfRecordsPerPage()!=null) {
				//pagination list
				int record=finalListBills.size() - ( reqDto.getOffset()+reqDto.getNoOfRecordsPerPage());
				if(record<0) {
					List<BillListResDto> finalListBillssublist=finalListBills.subList(reqDto.getOffset(), finalListBills.size());
					return new Response<>(SUCCESS, null, null, finalListBillssublist, finalListBills.size());	
				}
				List<BillListResDto> finalListBillssublist=finalListBills.subList(reqDto.getOffset(), reqDto.getOffset()+reqDto.getNoOfRecordsPerPage());
				return new Response<>(SUCCESS, null, null, finalListBillssublist, finalListBills.size());	
			    }
			}
			else {
				//old list request
				return new Response<>(SUCCESS, null, null, finalListBills, null);
			}
			
		}catch(Exception e)
		{
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response requestForDoctorCr(DoctorShareCrNoteDto reqDto) {
		try{
			Response res = null;
			if(reqDto.getDoctorCrId()!=null && reqDto.getDoctorCrId()>0)
			{
				//update
				 res = iDoctorShareCRNoteDao.updateRequestForDoctorCr(reqDto);
			}else{
				//add
				res = iDoctorShareCRNoteDao.requestForDoctorCr(reqDto);
			}
			if(res.getStatus().equals(SUCCESS))
			{
				if(reqDto.getOctorShareCrNoteDetailsDtosList()!=null && reqDto.getOctorShareCrNoteDetailsDtosList().size()>0)
				{
					for(DoctorShareCrNoteDetailsDto dto :reqDto.getOctorShareCrNoteDetailsDtosList())
					{
						if(dto.getDoctorCrDetailsId()!=null && dto.getDoctorCrDetailsId()>0)
						{
							//update
							dto.setUpdatedBy(reqDto.getUpdatedBy());
							iDoctorShareCRNoteDao.updateRequestForDoctorCrDetails(dto);
						}
						else{
							//add
							dto.setDoctorCrId(reqDto.getDoctorCrId()!=null?reqDto.getDoctorCrId():(Integer)res.getObject());
							iDoctorShareCRNoteDao.requestForDoctorCrDetails(dto);
						}
					}
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, "Request raised successfully...!!", null, null);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorCrRequests(BillListReqDto reqDto) {
		try{
			
			if(reqDto.getDoctorId()!=null)
				return iDoctorShareCRNoteDao.getDoctorCrRequests(reqDto);
			else
				return iDoctorShareCRNoteDao.getReferralCrRequests(reqDto);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response getCrRequestDetails(Integer doctorCrId) {
		try{
			return iDoctorShareCRNoteDao.getCrRequestDetails(doctorCrId);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response doctorsListForDoctorCr(Integer billId) {
		try{
			return iDoctorShareCRNoteDao.doctorsListForDoctorCr(billId);
		}catch(Exception e)
		{
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response acceptDoctorCrRequests(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		try{
			Integer billPayeeDetailsIdId = 0;
			Double amountpaid = 0.00;
			Double payeeDueAmt = 0.00;
			
			BillingMaster billingMasterObj =iBillingDao.getBillMasterObject(doctorShareCrNoteDetailsDto.getBillId());
			Response billPayeeDtlsRes =iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(doctorShareCrNoteDetailsDto.getBillId());
			
			if(billingMasterObj.getOutstanding()<doctorShareCrNoteDetailsDto.getAmountPaid())
				amountpaid = billingMasterObj.getOutstanding();
			else
				amountpaid = doctorShareCrNoteDetailsDto.getAmountPaid();
			
			List<BillPayeeDetailsResDto> listBillPayeeDetailsResDto = billPayeeDtlsRes.getListObject();
			
			for(BillPayeeDetailsResDto detailsResDto : listBillPayeeDetailsResDto) 
			{
				if(detailsResDto.getPayeeId().equals(doctorShareCrNoteDetailsDto.getSelfPayeeId()))
				{
					billPayeeDetailsIdId = detailsResDto.getBillPayeeDetailsId();
					payeeDueAmt = detailsResDto.getDueAmount();
				}
			}
			BillPaymentCollectionReqDto paymentCollectionDto = new BillPaymentCollectionReqDto();
				paymentCollectionDto.setBillingMasterId(doctorShareCrNoteDetailsDto.getBillId());
				paymentCollectionDto.setBillPayeeDetailsId(billPayeeDetailsIdId);
				paymentCollectionDto.setDateOfReciept(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(doctorShareCrNoteDetailsDto.getOrgUnitId()),"dd-M-yyyy HH:mm:ss"));
				paymentCollectionDto.setPayeeId(doctorShareCrNoteDetailsDto.getSelfPayeeId());
				paymentCollectionDto.setOrgnizationId(billingMasterObj.getOrganisationId());
				paymentCollectionDto.setUnitId(billingMasterObj.getUnitId());
				paymentCollectionDto.setIsPreOrPostBilling(doctorShareCrNoteDetailsDto.getIsPreBilling());
				paymentCollectionDto.setIsFinancialClearance('N');
				paymentCollectionDto.setEncounterId(billingMasterObj.getEncounterId());
				paymentCollectionDto.setAdmissionId(billingMasterObj.getAdmissionId());
				paymentCollectionDto.setPatientId(billingMasterObj.getPatientId());
				//paymentCollectionDto.setPayeeDueAmount(billingMasterObj.getOutstanding());
				paymentCollectionDto.setPayeeDueAmount(payeeDueAmt);
				paymentCollectionDto.setReceiptType(1);
				paymentCollectionDto.setCreatedBy(doctorShareCrNoteDetailsDto.getCreatedBy());
				paymentCollectionDto.setUpdatedBy(doctorShareCrNoteDetailsDto.getCreatedBy());
				paymentCollectionDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterObj.getUnitId()),"dd-M-yyyy HH:mm:ss"));
				paymentCollectionDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterObj.getUnitId()),"dd-M-yyyy HH:mm:ss"));
				paymentCollectionDto.setTotalSelfPayable(billingMasterObj.getSelfPayable());
				paymentCollectionDto.setTotalCreditPayable(billingMasterObj.getCreditPayable());
				paymentCollectionDto.setIsInterimOrFinalBill(billingMasterObj.getIsInterimOrFinalBill());
				paymentCollectionDto.setVisitTypeId(billingMasterObj.getVisitTypeId());
				paymentCollectionDto.setIsNursingAssessmentRequired('N');
			
				if(billingMasterObj.getEncounterId()!=null && billingMasterObj.getEncounterId()>0) {
					paymentCollectionDto.setAppointmentId(iAppointmentSchedulingDao.getAppointmentId(billingMasterObj.getEncounterId()));
					paymentCollectionDto.setIsNursingAssessmentRequired(iAppointmentSchedulingDao.getNursingAssessmentRequired(billingMasterObj.getEncounterId()));
				}
				
				List<PaymentReceiptDetailsDto> receiptDetailsDtosList = new LinkedList<>();
					PaymentReceiptDetailsDto detailsDto = new PaymentReceiptDetailsDto();
						detailsDto.setPaymentModeId(11);// Doctor's Credit
						detailsDto.setAmount(amountpaid);
						detailsDto.setCreatedBy(detailsDto.getCreatedBy());
						detailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterObj.getUnitId()),"dd-M-yyyy HH:mm:ss"));
						detailsDto.setUpdatedBy(detailsDto.getCreatedBy());
						detailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterObj.getUnitId()),"dd-M-yyyy HH:mm:ss"));
						detailsDto.setDateOfTransaction(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterObj.getUnitId()),"dd-M-yyyy HH:mm:ss"));
					receiptDetailsDtosList.add(detailsDto);
				
				paymentCollectionDto.setPaymentReceiptDetailsDtosList(receiptDetailsDtosList);
				paymentCollectionDto.setAmount(amountpaid);
				paymentCollectionDto.setIsTransferToActiveDeposit('N');
			iBillingMasterService.collectPayment(paymentCollectionDto);
			
			iDoctorShareCRNoteDao.acceptDoctorCrRequests(doctorShareCrNoteDetailsDto.getDoctorCrDetailsId(),amountpaid);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response rejectDoctorCrRequests(DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		try{
			
			return iDoctorShareCRNoteDao.rejectDoctorCrRequests(doctorShareCrNoteDetailsDto);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response<TDoctorShareRespDto, Integer> getInactiveCredit(
			DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		try{
			
			Response res = iDoctorShareCRNoteDao.getInactiveCredit(doctorShareCrNoteDetailsDto);
			return res;
		}catch(Exception e)
		{
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response<TDoctorShareRespDto, Integer> getDoctorCrDetailsForDoctorSharePaymentVoucher(
			DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		try{
			
			if(doctorShareCrNoteDetailsDto.getDoctorId()!=null)
				return iDoctorShareCRNoteDao.getDoctorCrDetailsForDoctorSharePaymentVoucher(doctorShareCrNoteDetailsDto);
			else
				return iDoctorShareCRNoteDao.getReferralCrDetailsForReferralSharePaymentVoucher(doctorShareCrNoteDetailsDto);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			return iDoctorShareCRNoteDao.saveDoctorAdvance(doctorAdvanceDto);
		}catch(Exception e)
		{
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response getDoctorAdvanceData(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			return iDoctorShareCRNoteDao.getDoctorAdvanceData(doctorAdvanceDto);
		}catch(Exception e)
		{
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response updateDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			
			return iDoctorShareCRNoteDao.updateDoctorAdvanceDetails(doctorAdvanceDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	@Transactional
	public Response updateStatusDoctorAdvanceDetails(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			return iDoctorShareCRNoteDao.updateStatusDoctorAdvanceDetails(doctorAdvanceDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response acceptDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			return iDoctorShareCRNoteDao.acceptDoctorAdvance(doctorAdvanceDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response rejectDoctorAdvance(DoctorAdvanceDto doctorAdvanceDto) {
		try{
			return iDoctorShareCRNoteDao.rejectDoctorAdvance(doctorAdvanceDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
