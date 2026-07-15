package com.param.billing.global.transaction.service;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.ICreditNoteDetailsDao;
import com.param.billing.global.transaction.dao.IDocShareVoucherDao;
import com.param.billing.global.transaction.dao.IDocShareVoucherLoggerDao;
import com.param.billing.global.transaction.dao.IDoctorShareCRNoteDao;
import com.param.billing.global.transaction.dao.IDoctorShareDao;
import com.param.billing.global.transaction.dao.IDoctorShareSplitDao;
import com.param.billing.global.transaction.dao.IDoctorSlabDao;
import com.param.billing.global.transaction.dto.BillDetailsByBillIdDto;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDetailsDto;
import com.param.billing.global.transaction.dto.DoctorSlabTransactionDto;
import com.param.billing.global.transaction.dto.TDocSharePaymentVoucherLoggerDto;
import com.param.billing.global.transaction.dto.TDocShareVoucherDto;
import com.param.billing.global.transaction.dto.TDoctorShareLoggerReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
import com.param.billing.global.transaction.model.TDoctorShare;
import com.param.billing.global.transaction.model.TDoctorShareSplitLogs;
import com.param.global.common.ServiceException;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.service.IOrderMasterService;
import com.param.utility.TimeZoneComponent;
import com.param.global.service.IOrderDetailsMasterService;
import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class TDoctorShareServiceImpl implements ITDoctorShareService, ICommonConstants{

	@Autowired
	private IDoctorShareDao  iDoctorShareDao;
	
	@Autowired
	private IDocShareVoucherLoggerDao iDocShareVoucherLoggerDao;
	
	@Autowired
	private IDocShareVoucherDao iDocShareVoucherDao;
	
	@Autowired
	IBillingDetailsDao  iBillingDetailsDao;
	
	@Autowired 
	private IDoctorShareSplitDao iDoctorShareSplitDao;
	
	@Autowired
	IDoctorShareCRNoteDao iDoctorShareCRNoteDao;
	
	@Autowired 
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IOrderMasterService iOrderMasterService;
	
	@Autowired
	private IBillingMasterDao billingMasterDao; 
	
	@Autowired
	private IDoctorSlabDao iDoctorSlabDao; 
	
	@Autowired
	ICreditNoteDetailsDao iCreditNoteDetailsDao;
	
	@Autowired
	IOrderDetailsMasterService IOrderDetailsMasterService;
	
	@Override
	@Transactional
	public Response getDoctorShareList(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException {
		try {
			return iDoctorShareDao.getDoctorShareList(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorShareListCount(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException {
		try {
			return iDoctorShareDao.getDoctorShareListCount(tDoctorShareDto);
		} catch (Exception e) {

			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response getDoctorReferralList(TDoctorShareReqDto reqDto)
	{
	try {
			return iDoctorShareDao.getDoctorReferralList(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	@Transactional
	public Response saveDoctorShareList(TDoctorShareProcessReqDto reqDto) throws ApplicationException {
		try {
			return iDoctorShareDao.saveDoctorShareList(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorSharePaymentVoucherList(TDoctorShareReqDto reqDto) throws ApplicationException {
		try {
			
			return iDoctorShareDao.getDoctorSharePaymentVoucherList(reqDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response getDoctorRefferalPaymentVoucherList(TDoctorShareReqDto reqDto) throws ApplicationException {
		try {
			return iDoctorShareDao.getDoctorRefferalPaymentVoucherList(reqDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorSharePaymentVoucherDtlsByDoctorShareId(TDoctorShareReqDto tDoctorShareDto)
			throws ApplicationException {
		try {
			return iDoctorShareDao.getDoctorSharePaymentVoucherDtlsByDoctorShareId(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveDoctorSharePaymentVoucherList(TDoctorShareLoggerReqDto reqDto) throws ApplicationException {
		try {
			
				Double dummyConsumedDocAdvance = (reqDto.getConsumedDocAdvance() > 0.00 ? reqDto.getConsumedDocAdvance() : 0.00);
				Double dummyConsumedDocCredit = (reqDto.getConsumedDocCredit() > 0.00 ? reqDto.getConsumedDocCredit() : 0.00);
				Double dummyRetainerShipAmt = (reqDto.getRetainerShipAmt() > 0.00 ? reqDto.getRetainerShipAmt() : 0.00);
				Double dummyTDSAmt = (reqDto.getTdsAmt() > 0.00 ? reqDto.getTdsAmt() : 0.00);
				Double paidAmount = 0.00;
				Response masterRes= iDocShareVoucherLoggerDao.saveDoctorSharePaymentVoucher(reqDto);
				Integer paymentVoucherId=(Integer)masterRes.getObject();
				for(TDocSharePaymentVoucherLoggerDto mstDto:reqDto.getListTDocSharePaymentVoucherLoggerDto()) 
				{
						paidAmount = mstDto.getPaidAmt();
						if(paidAmount > 0.00 && dummyConsumedDocCredit > 0.00)
						{
							if(dummyConsumedDocCredit >= paidAmount)
							{
								//dummyConsumedDocCredit = dummyConsumedDocCredit-paidAmount;
								paidAmount = 0.00;
							}else{
								paidAmount = paidAmount-dummyConsumedDocCredit;
								//dummyConsumedDocCredit = 0.00;
							}
						}
						if(paidAmount > 0.00 && dummyConsumedDocAdvance > 0.00)
						{
							if(dummyConsumedDocAdvance >= paidAmount)
							{
								//dummyConsumedDocAdvance = dummyConsumedDocAdvance-paidAmount;
								paidAmount = 0.00;
							}else{
								paidAmount = paidAmount-dummyConsumedDocAdvance;
								//dummyConsumedDocAdvance = 0.00;
							}
						}
						if(paidAmount > 0.00 && dummyRetainerShipAmt > 0.00)
						{
							if(dummyRetainerShipAmt >= paidAmount)
							{
								dummyRetainerShipAmt = dummyRetainerShipAmt-paidAmount;
								paidAmount = 0.00;
							}else{
								paidAmount = paidAmount-dummyRetainerShipAmt;
								//dummyRetainerShipAmt = 0.00;
							}
						}
						
						
						if(paidAmount > 0.00 && dummyTDSAmt > 0.00)
						{
							if(dummyTDSAmt >= paidAmount)
							{
								dummyTDSAmt = dummyTDSAmt-paidAmount;
								paidAmount = 0.00;
							}else{
								paidAmount = paidAmount-dummyTDSAmt;
								//dummyRetainerShipAmt = 0.00;
							}
						}
						
						mstDto.setConsumedDoctorAdvance(dummyConsumedDocAdvance);
						mstDto.setConsumedDoctorCr(dummyConsumedDocCredit);
						mstDto.setRetainerShipAmt(dummyRetainerShipAmt);
						mstDto.setTdsAmt(dummyTDSAmt);
						mstDto.setPaidAmt(paidAmount);
						mstDto.setPaymentVoucherId(paymentVoucherId);
                        mstDto.setDocShareVoucherId(mstDto.getVoucherLoggerId());
					Response result= iDocShareVoucherLoggerDao.saveDoctorSharePaymentVoucherList(mstDto);
					if(result.getStatus().equals(SUCCESS)) {
						
						TDocShareVoucherDto  masterDto = new TDocShareVoucherDto();
							masterDto.setUpdatedBy(mstDto.getCreatedBy());
							masterDto.setDocShareVoucheId(mstDto.getVoucherLoggerId());
							masterDto.setDueShareAmt(paidAmount+dummyRetainerShipAmt+dummyConsumedDocCredit+dummyConsumedDocAdvance+dummyTDSAmt);
						iDocShareVoucherDao.updateDocShareVoucher(masterDto);
					}
				}
				
				for(DoctorShareCrNoteDetailsDto detailsDto : reqDto.getDoctorShareCrNoteDetailsDtosList())
				{
					 iDoctorShareCRNoteDao.updateAcceptedDoctorCredit(detailsDto,0);
				}
				
				iDoctorShareCRNoteDao.updateAcceptedDoctorAdvance(dummyConsumedDocAdvance,reqDto.getDoctorAdvanceId());
				
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	@Transactional
	public Response saveDoctorShareSplit(TDoctorShareSplitLogsReqDto reqDto) throws ApplicationException{
		try {
			
			  // Get Doctor Data From DoctorShareId
			  Response docShareResult=iDoctorShareDao.getDocShareDataByDoctorShareId(reqDto.gettDoctorShareRespDto().getDoctorShareId());
				if(docShareResult.getStatus().equals(SUCCESS) && docShareResult.getObject() != null) 
				{
					TDoctorShare tDoctorShare = (TDoctorShare)docShareResult.getObject();
					Integer doctorShareId = tDoctorShare.getDoctorShareId();
						tDoctorShare.setDocShare(reqDto.gettDoctorShareRespDto().getDocShare());
						tDoctorShare.setDueShareAmt(reqDto.gettDoctorShareRespDto().getDueShareAmt());
						tDoctorShare.setHospShare(reqDto.gettDoctorShareRespDto().getHospShare());
						tDoctorShare.setSharePer(reqDto.gettDoctorShareRespDto().getSharePer());
						tDoctorShare.setTotalDocShare(reqDto.gettDoctorShareRespDto().getTotalDocShare());
					iDoctorShareDao.updateDoctorShareData(tDoctorShare);
					
					if(reqDto.gettDoctorShareRespDto().getRefDocShareId() != null && reqDto.gettDoctorShareRespDto().getRefDocShareId() > 0) 
					{
						iDoctorShareDao.updateDoctorReferralData(reqDto.gettDoctorShareRespDto().getRefDocShareId(),reqDto.gettDoctorShareRespDto().getRefDocShare(),reqDto.gettDoctorShareRespDto().getHospShare());
					}
					
					if(reqDto.getListTDoctorShareRespDto()!= null && reqDto.getListTDoctorShareRespDto().size() > 0)
					{
						for(TDoctorShareRespDto tDoctorShareRespDto : reqDto.getListTDoctorShareRespDto())
						{
							Integer chidDocShareId = null;
							if(tDoctorShareRespDto.getDoctorShareId()!=null)
							{
								if(!(doctorShareId.equals(tDoctorShareRespDto.getDoctorShareId())))
								{
									Response docShare=iDoctorShareDao.getDocShareDataByDoctorShareId(tDoctorShareRespDto.getDoctorShareId());
									TDoctorShare tDoctorShareInner = (TDoctorShare)docShare.getObject();
										tDoctorShareInner.setDocShare(tDoctorShareRespDto.getDocShare());
										tDoctorShareInner.setDueShareAmt(tDoctorShareRespDto.getDocShare());
										tDoctorShareInner.setSharePer(tDoctorShareRespDto.getSharePer());
										tDoctorShareInner.setTotalDocShare(tDoctorShareRespDto.getDocShare());
									iDoctorShareDao.updateDoctorShareData(tDoctorShareInner);
								}
							}else{
								TDoctorShare clonedDocShare = (TDoctorShare)tDoctorShare.clone();
									clonedDocShare.setDoctorShareId(null);
									clonedDocShare.setDocId(tDoctorShareRespDto.getDocId());
									clonedDocShare.setSharePer(tDoctorShareRespDto.getSharePer());
									clonedDocShare.setDueShareAmt(tDoctorShareRespDto.getDocShare());
									clonedDocShare.setHospShare(0.0);
									clonedDocShare.setDocShare(tDoctorShareRespDto.getDocShare());
									clonedDocShare.setProcessedShareAmt(0.0);
									clonedDocShare.setIsShareSplit('Y');
									clonedDocShare.setDocSpecId(tDoctorShareRespDto.getDocSpecId());
									clonedDocShare.setParentDocShareId(doctorShareId);
									clonedDocShare.setTotalDocShare(tDoctorShareRespDto.getDocShare());
							  Response res = iDoctorShareDao.saveDoctorShareMaster(clonedDocShare);
							  chidDocShareId = (Integer)res.getObject();
							}
							if(tDoctorShareRespDto.getDoctorShareSplitId()!=null)
							{
								Response res = iDoctorShareSplitDao.findByIdDoctorShareSplit(tDoctorShareRespDto.getDoctorShareSplitId());
								TDoctorShareSplitLogs tDoctorShareSplitLogs = (TDoctorShareSplitLogs)res.getObject();
									tDoctorShareSplitLogs.setNewSharePercentage(tDoctorShareRespDto.getSharePer());
									tDoctorShareSplitLogs.setShareAmount(tDoctorShareRespDto.getDocShare());
								iDoctorShareSplitDao.updateDoctorShareSplit(tDoctorShareSplitLogs);
							}else{
								TDoctorShareSplitLogsDto tDoctorShareSplitLogsDto=new TDoctorShareSplitLogsDto();
									tDoctorShareSplitLogsDto.setNewSharePercentage(tDoctorShareRespDto.getSharePer());
									tDoctorShareSplitLogsDto.setDoctorShareId(chidDocShareId);
									tDoctorShareSplitLogsDto.setDocId(tDoctorShareRespDto.getDocId());
									tDoctorShareSplitLogsDto.setShareAmount(tDoctorShareRespDto.getDocShare());
									tDoctorShareSplitLogsDto.setOrgId(reqDto.getOrgId());
									tDoctorShareSplitLogsDto.setOrgUnitId(reqDto.getOrgUnitId());
									tDoctorShareSplitLogsDto.setParentDocShareId(doctorShareId);
								iDoctorShareSplitDao.saveDoctorShareSplit(tDoctorShareSplitLogsDto);
							}
						}
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				  }
			 
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getDocShareSplitDetails(Integer docShareId) throws ServiceException {
			try {
				return iDoctorShareDao.getDocShareSplitDetails(docShareId);
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getProcessDocShare(Integer unitId) throws ServiceException {
		try{
			
			Set<Integer> billDtlsIds = new HashSet<>();
			int startPoint = 0;
			int maxResults = 1000;
			boolean isSync = false;
			while(!isSync){
				com.param.global.common.Response response = iDoctorShareDao.getDataFromBillingDtls(startPoint, maxResults,unitId);
				if(response.getCode().equals(SUCCESS_CODE) && response.getListObject() != null && response.getListObject().size() > 0)
				{
					ListIterator<TDoctorShareRespDto> tDoctorShareRespDtoItr = response.getListObject().listIterator();
					while(tDoctorShareRespDtoItr.hasNext())
					{
						TDoctorShareRespDto tDoctorShareRespDto = tDoctorShareRespDtoItr.next();
						if(tDoctorShareRespDto.getSharePer()!=null && tDoctorShareRespDto.getIsDocShare()==0)
						{
							//calculate doctor due share
							double amount = (tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt())+ (tDoctorShareRespDto.getDiscount() + tDoctorShareRespDto.getConcession());
							double taxAmount =0;
                            if(tDoctorShareRespDto.getTaxAmt()!= null){
                                amount = amount - tDoctorShareRespDto.getTaxAmt();
                                taxAmount =  tDoctorShareRespDto.getTaxAmt();
                            }
							double docShare = tDoctorShareRespDto.getSharePer();
							double hospShare =(tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt()) - (docShare + tDoctorShareRespDto.getRefSharePer() + taxAmount);
							double docSharePer = (tDoctorShareRespDto.getSharePer() / (amount - (tDoctorShareRespDto.getDiscount() + tDoctorShareRespDto.getConcession()) )) * 100;
							docSharePer = (double) Math.round(docSharePer * 100) / 100;
							
							double netAmt = (tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt());
							tDoctorShareRespDto.setNetAmount(netAmt);
							tDoctorShareRespDto.setTotalAmount((tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt()) + (tDoctorShareRespDto.getDiscount() + tDoctorShareRespDto.getConcession()));
							tDoctorShareRespDto.setSharePer(docSharePer);
							tDoctorShareRespDto.setOldSharePer(docSharePer);
							tDoctorShareRespDto.setDueShareAmt(docShare);
							tDoctorShareRespDto.setDocShare(docShare);
							tDoctorShareRespDto.setHospShare(hospShare>=0.00 ? hospShare : 0.00);
							tDoctorShareRespDto.setOldHospShare(hospShare);
							tDoctorShareRespDto.setIsShareSplit('N');
							tDoctorShareRespDto.setTotalDocShare(docShare);
							//tDoctorShareRespDto.setReferralId(null);
							com.param.global.common.Response result=  iDoctorShareDao.saveDoctorShareData(tDoctorShareRespDto);
							if(result.getStatus().equals(SUCCESS)) 
							{
								
								TDoctorShareSplitLogsDto tDoctorShareSplitLogsDto=new TDoctorShareSplitLogsDto();
									tDoctorShareSplitLogsDto.setOldSharePercentage(0);
									tDoctorShareSplitLogsDto.setNewSharePercentage(docSharePer);
									tDoctorShareSplitLogsDto.setShareAmount(docShare);
									tDoctorShareSplitLogsDto.setPriority(1);
									tDoctorShareSplitLogsDto.setDoctorShareId((Integer)result.getObject());
									tDoctorShareSplitLogsDto.setDocId(tDoctorShareRespDto.getDocId());
									tDoctorShareSplitLogsDto.setParentDocShareId((Integer)result.getObject());
								iDoctorShareSplitDao.saveDoctorShareSplit(tDoctorShareSplitLogsDto);
							}
							billDtlsIds.add(tDoctorShareRespDto.getBillDtlId());
						}
						if(tDoctorShareRespDto.getRefSharePer()!=null && tDoctorShareRespDto.getIsDocShare()==1)
						{
							//calculate doctor due share
							double amount = (tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt()) + (tDoctorShareRespDto.getDiscount() + tDoctorShareRespDto.getConcession());
								double taxAmount =0;
	                            if(tDoctorShareRespDto.getTaxAmt()!= null){
	                                amount = amount - tDoctorShareRespDto.getTaxAmt();
	                                taxAmount =  tDoctorShareRespDto.getTaxAmt();
	                            }
								double refShare = tDoctorShareRespDto.getRefSharePer();
								double hospShare =  (tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt()) - (refShare + tDoctorShareRespDto.getSharePer() + taxAmount);
								double refSharePer = (tDoctorShareRespDto.getRefSharePer() / (amount -(tDoctorShareRespDto.getDiscount() + tDoctorShareRespDto.getConcession()) )) * 100;
								
								refSharePer = (double) Math.round(refSharePer * 100) / 100;
								
								double netAmt = (tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt());
								
								tDoctorShareRespDto.setNetAmount(netAmt);
								tDoctorShareRespDto.setTotalAmount((tDoctorShareRespDto.getTotalAmount() + tDoctorShareRespDto.getPackageConsumptionAmt()) + (tDoctorShareRespDto.getDiscount() + tDoctorShareRespDto.getConcession()));
								tDoctorShareRespDto.setSharePer(refSharePer);
								tDoctorShareRespDto.setOldSharePer(refSharePer);
								tDoctorShareRespDto.setDueShareAmt(refShare);
								tDoctorShareRespDto.setDocShare(refShare);
								tDoctorShareRespDto.setHospShare(hospShare);
								tDoctorShareRespDto.setOldHospShare(hospShare);
								tDoctorShareRespDto.setIsShareSplit('N');
								tDoctorShareRespDto.setTotalDocShare(refShare);
								tDoctorShareRespDto.setReferralId(tDoctorShareRespDto.getDocId());
								tDoctorShareRespDto.setOrdDocSplId(null);
								tDoctorShareRespDto.setDocId(null);
							iDoctorShareDao.saveDoctorShareData(tDoctorShareRespDto);
							billDtlsIds.add(tDoctorShareRespDto.getBillDtlId());
						}
						
						//doctorSlab
						if(tDoctorShareRespDto.getIsDocShare()==0)
						{
							DoctorSlabTransactionDto doctorSlabTransactionDto = new DoctorSlabTransactionDto();
								doctorSlabTransactionDto.setBillDetailId(tDoctorShareRespDto.getBillDtlId());
								doctorSlabTransactionDto.setDoctorId(tDoctorShareRespDto.getDocId());
								doctorSlabTransactionDto.setDoctorRevenue(new BigDecimal(tDoctorShareRespDto.getNetAmount()-tDoctorShareRespDto.getTaxAmt()));
								doctorSlabTransactionDto.setOrganizationId(tDoctorShareRespDto.getOrgId());
								doctorSlabTransactionDto.setUnitId(tDoctorShareRespDto.getOrgUnitId());
								doctorSlabTransactionDto.setRevenuePercentage(new BigDecimal(100));
								doctorSlabTransactionDto.setProcessAmount(new BigDecimal(0));
							iDoctorSlabDao.saveDoctorSlabTransaction(doctorSlabTransactionDto);	
						}
						
					}
					startPoint = maxResults;
					maxResults = maxResults + 1000;
					iBillingDetailsDao.updateBillDetailsIsDoctorSharePostedByBillId(new LinkedList<Integer>(billDtlsIds));
				}else{
					isSync = true;
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Transactional
	@Override
	public Response changeDoctor(TDoctorShareRespDto masterDto) throws ServiceException {
		try{
			return iDoctorShareDao.updateDoctor(masterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Transactional
	@Override
	public Response updateStatus(TDoctorShareRespDto masterDto) throws ServiceException {
		try{
			return iDoctorShareDao.updateStatus(masterDto);
			
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getPendingServicesForDoctorShareByBillId(TDoctorShareReqDto tDoctorShareDto) {
		try{
			return iDoctorShareDao.getPendingServicesForDoctorShareByBillId(tDoctorShareDto);
			
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response processDocShareForAlreadyBilledServices(List<BillDetailsByBillIdDto> billDetailsList) {
		try{

			Integer unitId = 0;
			Set<Integer> billDtlsIds = new HashSet<>();
			for(BillDetailsByBillIdDto billIdDto : billDetailsList)
			{
				if(billIdDto.getDoctorId()!=null && billIdDto.getDoctorId() > 0)
				{
					OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(billIdDto.getOrderDetailsId());
					com.param.global.common.Response res = iOrderDetailsMasterDao.getVisitDetailsForRenderingService(orderDetailsMasterDto.getOrderDetailsId());
					AdmissionsForBatchProcessDto vistDetailsObj = (AdmissionsForBatchProcessDto) res.getObject();
					if(orderDetailsMasterDto.getDocSharePer()==null && billIdDto.getDoctorId()!=null && billIdDto.getDoctorId() > 0)
					{
						OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
							orderDetails.setOrdDocId(billIdDto.getDoctorId());
							orderDetails.setOrdDocSplId(billIdDto.getDocSpecialityId());
							orderDetails.setServiceId(billIdDto.getServiceId());
							orderDetails.setOrgUnitId(orderDetailsMasterDto.getOrgUnitId());
							orderDetails.setTariffId(vistDetailsObj.getTariffId());
							orderDetails.setOrdTotalAmt(orderDetailsMasterDto.getOrdTotalAmt());
							orderDetails.setOrdDiscount(orderDetailsMasterDto.getOrdDiscount());
							orderDetails.setDepartmentId(billIdDto.getSpecialityId());
							orderDetails.setOrderDate(orderDetailsMasterDto.getOrderDate());
							orderDetails.setOrdConcession(orderDetailsMasterDto.getOrdConcession());
							orderDetails.setOrderQty(orderDetailsMasterDto.getOrderQty() != null ? orderDetailsMasterDto.getOrderQty() : 1);
							orderDetails.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership()!=null ? orderDetailsMasterDto.getDiscountOwnership() : 0);
						orderDetailsMasterDto.setDocSharePer(new BigDecimal(iOrderMasterService.getDoctorSharePercentage(orderDetails,vistDetailsObj.getVisitTypeId(),vistDetailsObj.getBillingBedCategoryId(),vistDetailsObj.getPatientTypeId(),vistDetailsObj.getPaymentEntitlementId())));
						orderDetailsMasterDto.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership()!=null ? orderDetailsMasterDto.getDiscountOwnership() : 0);
						orderDetailsMasterDto.setHospitalShare((orderDetailsMasterDto.getOrdTotalAmt().subtract((orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())))).subtract(orderDetailsMasterDto.getDocSharePer()));
					}
					else
					{
						orderDetailsMasterDto.setDocSharePer(null);
						orderDetailsMasterDto.setHospitalShare(null);
					}
						orderDetailsMasterDto.setOrdDocId(billIdDto.getDoctorId());
						orderDetailsMasterDto.setOrdDocSplId(billIdDto.getDocSpecialityId());
					iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto,null);
					billingMasterDao.updateBillDtlsForDocShare(billIdDto.getBillingDetailsId(),billIdDto.getDoctorId(),billIdDto.getDocSpecialityId());

					TDoctorShareRespDto tDoctorShareRespDto = new TDoctorShareRespDto();
						//calculate doctor due share
						tDoctorShareRespDto.setOrdDtlId(orderDetailsMasterDto.getOrderDetailsId());
						tDoctorShareRespDto.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
						tDoctorShareRespDto.setDocId(orderDetailsMasterDto.getOrdDocId());
						tDoctorShareRespDto.setDocSpecId(orderDetailsMasterDto.getOrdDocSplId());
						tDoctorShareRespDto.setConcession(orderDetailsMasterDto.getOrdConcession().doubleValue());
						tDoctorShareRespDto.setBillDtlId(billIdDto.getBillingDetailsId());
						tDoctorShareRespDto.setAdmissionId(vistDetailsObj.getAdmissionId()>0 ? vistDetailsObj.getAdmissionId() : null);
						tDoctorShareRespDto.setVisitTypeId(vistDetailsObj.getVisitTypeId());
						tDoctorShareRespDto.setServiceId(orderDetailsMasterDto.getServiceId());
						tDoctorShareRespDto.setUnitRate(orderDetailsMasterDto.getOrdTotalAmt().doubleValue());
						tDoctorShareRespDto.setDueShareDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
						tDoctorShareRespDto.setTotalAmount(orderDetailsMasterDto.getOrdTotalAmt().doubleValue());
						tDoctorShareRespDto.setDiscount(orderDetailsMasterDto.getOrdDiscount().doubleValue());
						tDoctorShareRespDto.setTaxAmt(orderDetailsMasterDto.getTaxAmount().doubleValue());
						tDoctorShareRespDto.setStatus(ACTIVE);
						tDoctorShareRespDto.setNetAmount(orderDetailsMasterDto.getNetAmount().doubleValue());
						tDoctorShareRespDto.setTaxId(orderDetailsMasterDto.getTaxId());
						tDoctorShareRespDto.setOrgId(orderDetailsMasterDto.getOrgId());
						tDoctorShareRespDto.setOrgUnitId(orderDetailsMasterDto.getOrgUnitId());
						tDoctorShareRespDto.setVisitId(vistDetailsObj.getEncounterId()>0 ? vistDetailsObj.getEncounterId() : null);
						double amount = tDoctorShareRespDto.getTotalAmount() - tDoctorShareRespDto.getDiscount() - tDoctorShareRespDto.getConcession();
						double docShare = orderDetailsMasterDto.getDocSharePer().doubleValue();
						double hospShare = amount - docShare - (orderDetailsMasterDto.getRefDocSharePer() !=null  ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00);
						double docSharePer = (docShare / amount) * 100;
						docSharePer = (double) Math.round(docSharePer * 100) / 100;
						tDoctorShareRespDto.setSharePer(docSharePer);
						tDoctorShareRespDto.setOldSharePer(docSharePer);
						tDoctorShareRespDto.setDueShareAmt(docShare);
						tDoctorShareRespDto.setDocShare(docShare);
						tDoctorShareRespDto.setHospShare(hospShare>=0.00 ? hospShare : 0.00);
						tDoctorShareRespDto.setOldHospShare(amount - docShare - (orderDetailsMasterDto.getRefDocSharePer() !=null  ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00));
						tDoctorShareRespDto.setIsShareSplit('N');
						tDoctorShareRespDto.setTotalDocShare(docShare);
						//tDoctorShareRespDto.setReferralId(null);
						com.param.global.common.Response result=  iDoctorShareDao.saveDoctorShareData(tDoctorShareRespDto);
						if(result.getStatus().equals(SUCCESS)) 
						{
							
							TDoctorShareSplitLogsDto tDoctorShareSplitLogsDto=new TDoctorShareSplitLogsDto();
								tDoctorShareSplitLogsDto.setOldSharePercentage(0);
								tDoctorShareSplitLogsDto.setNewSharePercentage(docSharePer);
								tDoctorShareSplitLogsDto.setShareAmount(docShare);
								tDoctorShareSplitLogsDto.setPriority(1);
								tDoctorShareSplitLogsDto.setDoctorShareId((Integer)result.getObject());
								tDoctorShareSplitLogsDto.setDocId(tDoctorShareRespDto.getDocId());
								tDoctorShareSplitLogsDto.setParentDocShareId((Integer)result.getObject());
							iDoctorShareSplitDao.saveDoctorShareSplit(tDoctorShareSplitLogsDto);
						}
						
						DoctorSlabTransactionDto doctorSlabTransactionDto = new DoctorSlabTransactionDto();
							doctorSlabTransactionDto.setBillDetailId(tDoctorShareRespDto.getBillDtlId());
							doctorSlabTransactionDto.setDoctorId(tDoctorShareRespDto.getDocId());
							doctorSlabTransactionDto.setDoctorRevenue(new BigDecimal(tDoctorShareRespDto.getNetAmount()-tDoctorShareRespDto.getTaxAmt()));
							doctorSlabTransactionDto.setOrganizationId(tDoctorShareRespDto.getOrgId());
							doctorSlabTransactionDto.setUnitId(tDoctorShareRespDto.getOrgUnitId());
							doctorSlabTransactionDto.setRevenuePercentage(new BigDecimal(100));
							doctorSlabTransactionDto.setProcessAmount(new BigDecimal(0));
						iDoctorSlabDao.saveDoctorSlabTransaction(doctorSlabTransactionDto);	
						
						
						billDtlsIds.add(tDoctorShareRespDto.getBillDtlId());
				}
				iBillingDetailsDao.updateBillDetailsIsDoctorSharePostedByBillId(new LinkedList<Integer>(billDtlsIds));
			}
			return new Response<>(SUCCESS, COMMON_SUCCESS, SUCCESS_MESSAGE, null, unitId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response changeDoctorRefeeral(TDoctorShareRespDto masterDto) {
		try{
			return iDoctorShareDao.changeDoctorRefeeral(masterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response saveDoctorReferralList(TDoctorShareProcessReqDto reqDto) throws ApplicationException {
		try {
			return iDoctorShareDao.saveDoctorReferralList(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response getDoctorReferralPaymentVoucherDtlsByReferralId(TDoctorShareReqDto tDoctorShareDto)
			throws ApplicationException {
		try {
			return iDoctorShareDao.getDoctorReferralPaymentVoucherDtlsByReferralId(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response<TDoctorShareLoggerReqDto, Integer> saveDoctorReferralPaymentVoucherList(
			TDoctorShareLoggerReqDto reqDto) {
		try {
			
			Double dummyConsumedDocAdvance = (reqDto.getConsumedDocAdvance() != null &&  reqDto.getConsumedDocAdvance() > 0.00 ? reqDto.getConsumedDocAdvance() : 0.00);
			Double dummyConsumedDocCredit = (reqDto.getConsumedDocCredit() != null && reqDto.getConsumedDocCredit() > 0.00 ? reqDto.getConsumedDocCredit() : 0.00);
			Double dummyRetainerShipAmt = (reqDto.getRetainerShipAmt() !=null && reqDto.getRetainerShipAmt() > 0.00 ? reqDto.getRetainerShipAmt() : 0.00);	
			Double paidAmount = 0.00;
			Response masterRes= iDocShareVoucherLoggerDao.saveDoctorSharePaymentVoucher(reqDto);
			Integer paymentVoucherId=(Integer)masterRes.getObject();
			for(TDocSharePaymentVoucherLoggerDto mstDto:reqDto.getListTDocSharePaymentVoucherLoggerDto()) 
			{
				paidAmount = mstDto.getPaidAmt();
				if(paidAmount > 0.00 && dummyConsumedDocCredit > 0.00)
				{
					if(dummyConsumedDocCredit >= paidAmount)
					{
						//dummyConsumedDocCredit = dummyConsumedDocCredit-paidAmount;
						paidAmount = 0.00;
					}else{
						paidAmount = paidAmount-dummyConsumedDocCredit;
						//dummyConsumedDocCredit = 0.00;
					}
				}
				if(paidAmount > 0.00 && dummyConsumedDocAdvance > 0.00)
				{
					if(dummyConsumedDocAdvance >= paidAmount)
					{
						//dummyConsumedDocAdvance = dummyConsumedDocAdvance-paidAmount;
						paidAmount = 0.00;
					}else{
						paidAmount = paidAmount-dummyConsumedDocAdvance;
						//dummyConsumedDocAdvance = 0.00;
					}
				}
				if(paidAmount > 0.00 && dummyRetainerShipAmt > 0.00)
				{
					if(dummyRetainerShipAmt >= paidAmount)
					{
						dummyRetainerShipAmt = dummyRetainerShipAmt-paidAmount;
						paidAmount = 0.00;
					}else{
						paidAmount = paidAmount-dummyRetainerShipAmt;
						//dummyRetainerShipAmt = 0.00;
					}
				}
				mstDto.setConsumedDoctorAdvance(dummyConsumedDocAdvance);
				mstDto.setConsumedDoctorCr(dummyConsumedDocCredit);
				mstDto.setRetainerShipAmt(dummyRetainerShipAmt);
				mstDto.setPaidAmt(paidAmount);
				mstDto.setPaymentVoucherId(paymentVoucherId);
				Response result= iDocShareVoucherLoggerDao.saveDoctorSharePaymentVoucherList(mstDto);
				if(result.getStatus().equals(SUCCESS)) {
					
					TDocShareVoucherDto  masterDto = new TDocShareVoucherDto();
						masterDto.setUpdatedBy(mstDto.getCreatedBy());
						masterDto.setDocShareVoucheId(mstDto.getVoucherLoggerId());
						masterDto.setDueShareAmt(mstDto.getPaidAmt()+mstDto.getRetainerShipAmt()+dummyConsumedDocAdvance+dummyConsumedDocCredit);
					iDocShareVoucherDao.updateDocShareVoucher(masterDto);
					
					
					for(DoctorShareCrNoteDetailsDto detailsDto : reqDto.getDoctorShareCrNoteDetailsDtosList())
					{
						 iDoctorShareCRNoteDao.updateAcceptedDoctorCredit(detailsDto,0);
					}
					
				}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorShareProcessedVoucherList(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException {
		try{
			return iDoctorShareDao.getDoctorShareProcessedVoucherList(tDoctorShareDto);
			
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updateDocShareAfterCreditNote(CreditNoteMasterDto creditNoteMasterDto) {
		try{
			
			for(CreditNoteDetailsDto creditNoteDetailsDto : creditNoteMasterDto.getListCreditNoteDetails())
			{
				List<TDoctorShareRespDto> list = iCreditNoteDetailsDao.getCrDetailsOrderDetailsId(creditNoteDetailsDto.getOrderDetailsId());
				if(creditNoteDetailsDto.getCancelOrDiscount()==1) //if service is cancelled then deactivating the doctor share
				{
					if(list!=null && list.size()>0)
						for(TDoctorShareRespDto doctorShareRespDto : list)
							iDoctorShareDao.deactivateDocShareAndSplit(doctorShareRespDto.getDoctorShareId(),null);
					
				}else{
					//recalculate doc share in case discount is applied
					if(list!=null && list.size()>0)
					{
						OrderDetailsMaster orderDetailsMaster = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(creditNoteDetailsDto.getOrderDetailsId());
						IOrderDetailsMasterService.updateOrderDetailsObj(orderDetailsMaster,list.get(0).getCrNetAmount());
						
						for(TDoctorShareRespDto doctorShareRespDto : list)
						{
							iDoctorShareDao.deactivateDocShareAndSplit(doctorShareRespDto.getDoctorShareId(),doctorShareRespDto.getBillDtlId());
						}
					}
				}
			}
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response updateDocShareAfterCreditNoteCancellation(CreditNoteMasterDto creditNoteMasterDto) {
		try{
			
			for(CreditNoteDetailsDto creditNoteDetailsDto : creditNoteMasterDto.getListCreditNoteDetails())
			{
				if(creditNoteDetailsDto.getCancelOrDiscount()==1) //if service is cancelled then deactivating the doctor share
				{
					iDoctorShareDao.allowBillDtailToReacalulateDocShare(creditNoteDetailsDto.getOrderDetailsId());
				}else{
					//recalculate doc share in case discount is applied
					List<TDoctorShareRespDto> list = iCreditNoteDetailsDao.getCrDetailsOrderDetailsId(creditNoteDetailsDto.getOrderDetailsId());
					if(list!=null && list.size()>0)
					{
						OrderDetailsMaster orderDetailsMaster = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(creditNoteDetailsDto.getOrderDetailsId());
						IOrderDetailsMasterService.updateOrderDetailsObj(orderDetailsMaster,list.get(0).getCrNetAmount());
						
						for(TDoctorShareRespDto doctorShareRespDto : list)
						{
							iDoctorShareDao.deactivateDocShareAndSplit(doctorShareRespDto.getDoctorShareId(),doctorShareRespDto.getBillDtlId());
						}
					}
				}
			}
			
			
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
