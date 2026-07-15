package com.param.global.billing.service;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.DepositCollectionReqDto;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.DepositSearchReqDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.billing.dto.VisitTypeEncounterReqDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionNoteDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDepositMasterService {
	public Response addDeposit(DepositCollectionReqDto reqDto)throws BillingException;
	public Response rechargeLoyaltyCard(DepositCollectionReqDto reqDto)throws BillingException;
	public Response addDepositAgainstAdmission(DepositCollectionReqDto reqDto)throws BillingException;
	public Response updateDepositAdmission(DepositMasterDto depositMasterDto)throws BillingException;
	public Response searchDeposit(DepositSearchReqDto dto)throws BillingException;
	public Response getEncountersForDeposit(VisitTypeEncounterReqDto reqDto)throws BillingException;
	public Response updateDepositMaster(PaymentDepositDto paymentDepositDto)throws BillingException;
	public Response getAvailableDepositByPatient(int patientId, int unitId)throws BillingException;
	public Response getDepositListForRefund(int orgId, int unitId, int patientId,int depositTypeId)throws BillingException;
	public Response getDepositDetailsByDepositMaster(int orgId, int unitId, int depositMasterId)throws BillingException;
	public Response getDepositDetailsByDepositId(int depositMasterId)throws BillingException;
	public Response updateDepositDetails(PaymentDepositDetailsDto paymentDepositDetailsDto)throws BillingException;
	@Transactional
	Response updateReservationDeposit(AdmissionNoteDto admissionNoteDto) throws ApplicationException;
	public Response transferToAvtiveDeposit(PaymentRecieptMasterDto paymentRecieptMasterDto);
	public Response saveTransferToActiveDepositFromPaymentCollection(DepositMasterDto forTransferToActiveDeposit);
	Response getAvailableDepositByPatient(int orgId, int unitId, int patientId, int encounterId, int visitTypeId, int otReservFlag, Boolean depositType)
			throws BillingException;
	
	public Response getDepositConsumtionDetails(Integer depositId) throws ApplicationException;
	public Response getMySqlData() throws ApplicationException;
	
	public Response updateSignatureImage(int orgId,int unitId, int depositId, @RequestParam("file") MultipartFile inputFile)throws BillingException;
	public Response getDocIdFromVisitId(Integer admissionId, Integer encounterId)throws ApplicationException;
	public Response ccAvenueCheckvalidation(String transactionNo)throws ApplicationException;
	public Response getGRNDetailsByGRNMaster(int orgId, int unitId, int gRNId)throws ApplicationException;

    Response cancelDepositReceipt(DepositMasterDto reqDto) throws ApplicationException;

    Response validateDepositAccess(Integer userId, Integer unitId, Integer organizationId)throws ApplicationException;
}
