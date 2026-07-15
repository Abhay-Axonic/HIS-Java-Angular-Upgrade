package com.param.billing.global.transaction.dao;

import java.util.List;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.TDoctorShare;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorShareDao extends IGenericDao<TDoctorShare, Integer>{
	public Response getDoctorShareList(TDoctorShareReqDto tDoctorShareDto)throws ApplicationException;

	public Response getDoctorShareListCount(TDoctorShareReqDto tDoctorShareDto)throws ApplicationException;
	public Response saveDoctorShareList(TDoctorShareProcessReqDto tDoctorShareProcessReqDto)throws ApplicationException;
	public Response getDoctorSharePaymentVoucherList(TDoctorShareReqDto reqDto) throws ApplicationException;
	public Response getDoctorSharePaymentVoucherDtlsByDoctorShareId(TDoctorShareReqDto tDoctorShareReqDto) throws ApplicationException;
	//save Doctor share data through cron job
	public com.param.global.common.Response saveDoctorShareData(TDoctorShareRespDto reqDto) throws ApplicationException;
	public com.param.global.common.Response  getDataFromBillingMst(int startPoint, int maxResults) throws ApplicationException;
	public Response getDocShareDataByDoctorShareId(Integer  docShareId)throws ApplicationException;
	public Response saveDoctorShareMaster(TDoctorShare tDoctorShare)throws ApplicationException;
	public Response updateDoctorShareMaster(TDoctorShareRespDto tDoctorShare)throws ApplicationException;
	public Response getDocShareSplitDetails(Integer docShareId)throws ApplicationException;
	public com.param.global.common.Response getDataFromBillingMst(int startPoint, int maxResults, List list)throws ApplicationException;
	com.param.global.common.Response getDataFromBillingDtls(int startPoint, int maxResults, int unitId)throws ApplicationException;
	public com.param.global.common.Response updateDoctorShareData(TDoctorShare tDoctorShare);
	public Response updateDoctor(TDoctorShareRespDto masterDto);
	Response updateStatus(TDoctorShareRespDto masterDto);
	public Response getPendingServicesForDoctorShareByBillId(TDoctorShareReqDto tDoctorShareDto);
	Response getDoctorReferralList(TDoctorShareReqDto reqDto) throws ApplicationException;
	public Response changeDoctorRefeeral(TDoctorShareRespDto masterDto);
	Response saveDoctorReferralList(TDoctorShareProcessReqDto reqDto) throws ApplicationException;
	Response getDoctorReferralPaymentVoucherDtlsByReferralId(TDoctorShareReqDto reqDto) throws ApplicationException;
	Response getDoctorRefferalPaymentVoucherList(TDoctorShareReqDto reqDto) throws ApplicationException;
	Response getDoctorShareDataByOrderDetailsId(Integer ordDtlsId) throws ApplicationException;
	Response docSharefindById(Integer DocShareId) throws ApplicationException;
	Response saveDoShareObject(TDoctorShare tDoctorShare) throws ApplicationException;
	Response updateDoctorReferralData(Integer refDocShareId, Double refDocShare,Double hospShare);
	Response inactiveDocShare(Integer docShareId);
	Response saveDoctorSlabVoucher(TDoctorShareProcessReqDto reqDto) throws ApplicationException;
	public Response getDoctorShareProcessedVoucherList(TDoctorShareReqDto tDoctorShareDto)throws ApplicationException;
	public Response deactivateDocShareAndSplit(Integer docDocShareId,Integer billDtlsId);
	public void allowBillDtailToReacalulateDocShare(Integer orderDetailsId);
	
}
