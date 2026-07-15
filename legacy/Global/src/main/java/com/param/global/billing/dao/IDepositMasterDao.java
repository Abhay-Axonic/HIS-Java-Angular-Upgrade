package com.param.global.billing.dao;

import com.param.billing.global.model.DepositMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.DepositSearchReqDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.VisitTypeEncounterReqDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionNoteDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDepositMasterDao extends IGenericDao<DepositMaster, Integer>{
	public Response saveDeposit(DepositMasterDto dto)throws BillingException;
	public Response updateDepositAdmission(DepositMasterDto depositMasterDto)throws BillingException;
	public Long getCurrentId()throws BillingException;
	public Response searchDeposit(DepositSearchReqDto dto)throws BillingException;
	public Response getEncountersByPatIdForDeposit(VisitTypeEncounterReqDto reqDto)throws BillingException;
	public Response getAdmissionByPatVisitForDeposit(VisitTypeEncounterReqDto reqDto)throws BillingException;
	public Response updateDepositMaster(PaymentDepositDto paymentDepositDto)throws ApplicationException;
	public Response getDepositDetailsByDepositId(Integer depositId)throws ApplicationException;
	public Response getAvailableDepositByPatient(int patientId, int unitId)throws BillingException;
	public Response getDepositListForRefund(int orgId, int unitId, int patientId,int depositTypeId)throws BillingException;
	public Response getDepositDetailsByDepositMaster(int orgId, int unitId, int depositMasterId)throws BillingException;
	public Response updateDepositMasterRefundAmount(DepositMasterDto depositMasterDto)throws BillingException;
	public Response updateDepositDetailsRefundAmount(PaymentDepositDetailsDto paymentDepositDetailsDto)throws BillingException;
	public Response updateDepositDetails(PaymentDepositDetailsDto paymentDepositDetailsDto)throws BillingException;
	Response updateReservationDeposit(AdmissionNoteDto admissionNoteDto) throws ApplicationException;
	Response getAvailableDepositByPatient(int orgId, int unitId, int patientId, int encounterId, int vistTypeId, int otReservFlag, Boolean depositType)
			throws BillingException;
	
	public Response getDepositConsumtionDetails(Integer depositId) throws ApplicationException;
	Response getDocIdFromVisitId(Integer admissionId, Integer encounterId) throws BillingException;
	public Response getMySqlData() throws ApplicationException;
	
	public Response updateSignatureImage(int orgId,int unitId, int depositId,byte[] singnateureIamge,String path)throws BillingException;
	public DepositMaster getDepositAgainstReceipt(Integer paymentRecieptId, Integer billingMasterId);
	public Response updateDepositObject(DepositMaster depositMaster);
	public Response ccAvenueCheckvalidation(String transactionNo)throws BillingException;
	public Response getGRNDetailsByGRNMaster(int orgId, int unitId, int gRNId)throws BillingException;

    Integer cancelDepositReceipt(DepositMasterDto dto)throws BillingException;

    Response validateDepositAccess(Integer userId, Integer unitId, Integer organizationId)throws BillingException;
}
