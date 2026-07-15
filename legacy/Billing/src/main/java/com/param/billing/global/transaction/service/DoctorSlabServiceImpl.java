package com.param.billing.global.transaction.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.model.DoctorSlabTransaction;
import com.param.billing.global.transaction.dao.IDoctorShareDao;
import com.param.billing.global.transaction.dao.IDoctorSlabDao;
import com.param.billing.global.transaction.dto.DoctorSlabTransactionDto;
import com.param.billing.global.transaction.dto.TDocShareVoucherDtlsDto;
import com.param.billing.global.transaction.dto.TDocShareVoucherDto;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
import com.param.billing.unit.dao.IDoctorSlabMasterDao;
import com.param.billing.unit.dto.DoctorSlabDto;
import com.param.billing.unit.dto.DoctorSlabVoucherDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.service.OrderMasterServiceImpl;

@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class DoctorSlabServiceImpl implements IDoctorSlabService,ICommonConstants{

	@Autowired
	IDoctorSlabDao iDoctorSlabDao;
	
	@Autowired
	IDoctorSlabMasterDao iDoctorSlabMasterDao;
	
	@Autowired
	IDoctorShareDao iDoctorShareDao;
	
	@Override
	@Transactional
	public Response getlistForDoctorSlab(TDoctorShareReqDto tDoctorShareDto) {
		try{
			return iDoctorSlabDao.getlistForDoctorSlab(tDoctorShareDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response splitRevenue(TDoctorShareSplitLogsReqDto reqDto) {
		try{

			Response revTransactionRes=iDoctorSlabDao.getRevenueTransactionById(reqDto.gettDoctorShareRespDto().getDoctorSlabTransactionId());
				if(revTransactionRes.getStatus().equals(SUCCESS) && revTransactionRes.getObject() != null) 
				{
					DoctorSlabTransaction doctorSlabTransaction = (DoctorSlabTransaction)revTransactionRes.getObject();
					Integer doctorSlabTransactionId = doctorSlabTransaction.getId();
						
					if(reqDto.getListTDoctorShareRespDto()!= null && reqDto.getListTDoctorShareRespDto().size() > 0)
					{
						for(TDoctorShareRespDto tDoctorShareRespDto : reqDto.getListTDoctorShareRespDto())
						{
							if(tDoctorShareRespDto.getDoctorSlabTransactionId()!=null)
							{
								if(!(doctorSlabTransactionId.equals(tDoctorShareRespDto.getDoctorSlabTransactionId())))
								{
									Response revTransactionResInner=iDoctorSlabDao.getRevenueTransactionById(tDoctorShareRespDto.getDoctorSlabTransactionId());
									DoctorSlabTransaction doctorSlabTransactionInner = (DoctorSlabTransaction)revTransactionResInner.getObject();
									doctorSlabTransactionInner.setDoctorRevenue(tDoctorShareRespDto.getNewDoctorRevenue());
									doctorSlabTransactionInner.setRevenuePercentage(tDoctorShareRespDto.getNewRevenuePercentage());
									iDoctorSlabDao.updateDoctorSlabTransaction(doctorSlabTransactionInner);
								}else{
										doctorSlabTransaction.setDoctorRevenue(tDoctorShareRespDto.getNewDoctorRevenue());
										doctorSlabTransaction.setRevenuePercentage(tDoctorShareRespDto.getNewRevenuePercentage());
									iDoctorSlabDao.updateDoctorSlabTransaction(doctorSlabTransaction);
								}
							}else{
								DoctorSlabTransactionDto doctorSlabTransactionDto = new DoctorSlabTransactionDto();
									doctorSlabTransactionDto.setBillDetailId(doctorSlabTransaction.getBillDetailId());
									doctorSlabTransactionDto.setDoctorId(tDoctorShareRespDto.getDocId());
									doctorSlabTransactionDto.setOrganizationId(doctorSlabTransaction.getOrganizationId());
									doctorSlabTransactionDto.setUnitId(doctorSlabTransaction.getUnitId());
									doctorSlabTransactionDto.setRevenuePercentage(tDoctorShareRespDto.getNewRevenuePercentage());
									doctorSlabTransactionDto.setDoctorRevenue(tDoctorShareRespDto.getNewDoctorRevenue());
									doctorSlabTransactionDto.setSplitRevenueAgainst(doctorSlabTransactionId);
								iDoctorSlabDao.saveDoctorSlabTransaction(doctorSlabTransactionDto);	
							}
						}
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				  }
			 
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getDoctorSlabSplitDetails(Integer id) {
		try{
			return iDoctorSlabDao.getDoctorSlabSplitDetails(id);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Transactional
	@Override
	public Response changeDoctor(TDoctorShareRespDto masterDto) throws ServiceException {
		try{
			return iDoctorSlabDao.changeDoctor(masterDto);
		}catch(Exception e){
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response inactiveSplitData(TDoctorShareRespDto masterDto) {
		try{
			return iDoctorSlabDao.inactiveSplitData(masterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveVoucher(TDoctorShareProcessReqDto tDoctorShareProcessReqDto) 
	{
		try{
			Set<Integer> doctorIds = new HashSet<>();
			Map<Integer,String> doctors = new HashMap<Integer, String>();
			String doctorNames ="";
			for(TDoctorShareRespDto doctorShareRespDto : tDoctorShareProcessReqDto.getListtDoctorShareRespDto())
			{
				doctorIds.add(doctorShareRespDto.getDocId());
				doctors.put(doctorShareRespDto.getDocId(), doctorShareRespDto.getDoctorName());
				
			}
			
			for(Integer docId : doctorIds)
			{
				Stream<TDoctorShareRespDto> stream = tDoctorShareProcessReqDto.getListtDoctorShareRespDto().stream();
				List<TDoctorShareRespDto> listByDoctor = stream.filter(str -> str.getDocId().equals(docId)).collect(Collectors.toList());
				DoctorSlabDto doctorSlabDto = new DoctorSlabDto();
				doctorSlabDto.setUnitId(tDoctorShareProcessReqDto.getUnitId());
				doctorSlabDto.setDoctorId(docId);
				Response slabRes = iDoctorSlabMasterDao.getdoctorSlabListByDoctorId(doctorSlabDto);
				
				if(slabRes.getListObject()!=null && slabRes.getListObject().size()>0)
				{
					List<DoctorSlabDto> slabList = slabRes.getListObject();
					
					BigDecimal totalAmount = BigDecimal.ZERO;
					
					List<TDocShareVoucherDtlsDto> tDocShareVoucherDtlsList = new LinkedList<>();
					for(TDoctorShareRespDto shareRespDto : listByDoctor)
					{
						TDocShareVoucherDtlsDto tDocShareVoucherDtls = new TDocShareVoucherDtlsDto();
							tDocShareVoucherDtls.setOrgId(shareRespDto.getOrgId());
							tDocShareVoucherDtls.setStatus(ACTIVE);
							tDocShareVoucherDtls.setOrgUnitId(shareRespDto.getOrgUnitId());
							tDocShareVoucherDtls.setProcessedAmt(shareRespDto.getDoctorRevenue().doubleValue());
							tDocShareVoucherDtls.setDoctorSlabTransactionId(shareRespDto.getDoctorSlabTransactionId());
							tDocShareVoucherDtls.setCreatedBy(tDoctorShareProcessReqDto.getUserId());
							tDocShareVoucherDtls.setUpdatedBy(tDoctorShareProcessReqDto.getUserId());
						totalAmount = totalAmount.add(shareRespDto.getDoctorRevenue());
						
						tDocShareVoucherDtlsList.add(tDocShareVoucherDtls);
					}
					
					Double diff = null;
					int i = 0;
					Double totalRevenue = 0.00;
					List<DoctorSlabVoucherDetailsDto> finalSlabList = new LinkedList<>();
					for(DoctorSlabDto dto : slabList)
					{
						i++;
						Double revenueSlabWise = 0.00;
						diff = totalAmount.doubleValue() - dto.getSlabTo();
						if(diff < 0)
						{
							diff = totalAmount.doubleValue() - dto.getSlabFrom();
							DoctorSlabVoucherDetailsDto doctorSlabVoucherDetailsDto = new DoctorSlabVoucherDetailsDto(); 
								doctorSlabVoucherDetailsDto.setDoctorSlabMasterId(dto.getDoctorSlabMasterId());
								doctorSlabVoucherDetailsDto.setDoctorId(docId);
								revenueSlabWise = dto.getPercentage() * 0.01 * diff;
								doctorSlabVoucherDetailsDto.setRevenueProcessed(new BigDecimal(OrderMasterServiceImpl.df2.format(revenueSlabWise)));	
								doctorSlabVoucherDetailsDto.setRevenueGeneratedAgainst(new BigDecimal(OrderMasterServiceImpl.df2.format(diff)));
							finalSlabList.add(doctorSlabVoucherDetailsDto);
							totalRevenue = totalRevenue + revenueSlabWise;
							break;
						}else{
							
							double amountToBeCalculated = 0.00;
							if(i==slabList.size())
								amountToBeCalculated = diff + (dto.getSlabTo()-dto.getSlabFrom());
							else
								amountToBeCalculated = dto.getSlabTo()-dto.getSlabFrom();
								
							DoctorSlabVoucherDetailsDto doctorSlabVoucherDetailsDto = new DoctorSlabVoucherDetailsDto(); 
								doctorSlabVoucherDetailsDto.setDoctorSlabMasterId(dto.getDoctorSlabMasterId());
								doctorSlabVoucherDetailsDto.setDoctorId(docId);
								revenueSlabWise = dto.getPercentage() * 0.01 * amountToBeCalculated;
								doctorSlabVoucherDetailsDto.setRevenueProcessed(new BigDecimal(OrderMasterServiceImpl.df2.format(revenueSlabWise)));
								doctorSlabVoucherDetailsDto.setRevenueGeneratedAgainst(new BigDecimal(OrderMasterServiceImpl.df2.format(amountToBeCalculated)));
							finalSlabList.add(doctorSlabVoucherDetailsDto);
							totalRevenue = totalRevenue + revenueSlabWise;
						}
					}
					
					TDocShareVoucherDto tDocShareVoucher = new TDocShareVoucherDto();
						tDocShareVoucher.setOrgId(tDoctorShareProcessReqDto.getOrgId());
						tDocShareVoucher.setStatus(ACTIVE);
						tDocShareVoucher.setDocId(docId);
						tDocShareVoucher.setOrgUnitId(tDoctorShareProcessReqDto.getOrgId());
						tDocShareVoucher.setUpdatedBy(tDoctorShareProcessReqDto.getUserId());
						tDocShareVoucher.setTotalDocShareAmout(totalRevenue);
						tDocShareVoucher.setRemark(tDoctorShareProcessReqDto.getRemark());
						tDocShareVoucher.setProcessedShareAmt(totalRevenue);
						tDocShareVoucher.setDueShareAmt(totalRevenue);
						tDocShareVoucher.setType(3);
					
					TDoctorShareProcessReqDto doctorShareProcessReqDto = new TDoctorShareProcessReqDto();
						doctorShareProcessReqDto.settDocShareVoucherDto(tDocShareVoucher);
						doctorShareProcessReqDto.setListTDocShareVoucherDtlsDto(tDocShareVoucherDtlsList);
						doctorShareProcessReqDto.setDoctorSlabVoucherDetailsDtosList(finalSlabList);
						doctorShareProcessReqDto.setOrgId(tDoctorShareProcessReqDto.getOrgId());
						doctorShareProcessReqDto.setUnitId(tDoctorShareProcessReqDto.getUnitId());
						doctorShareProcessReqDto.setUserId(tDoctorShareProcessReqDto.getUserId());
					iDoctorShareDao.saveDoctorSlabVoucher(doctorShareProcessReqDto);
				}else{
					if(doctorNames.length()==0)
						doctorNames = doctorNames.concat("Dr. "+doctors.get(docId));
					else
						doctorNames = doctorNames.concat(", Dr. "+doctors.get(docId));
					
				}
				
					
			}
			
			if(doctorNames.length()>0)
			{
				String message = "Doctor slab is not configured for : "+doctorNames;
				return new Response<>(SUCCESS, SUCCESS_CODE, message, null, message);
			}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getVoucherDetails(TDoctorShareReqDto tDoctorShareDto) {
		try{
			return iDoctorSlabDao.getVoucherDetails(tDoctorShareDto);
		}catch(Exception e){
			
			throw e;
		}
		//return null;
	}

	@Override
	@Transactional
	public Response voucherSlabConsumptionDetails(Integer docShareVoucherId) {
		try{
			return iDoctorSlabDao.voucherSlabConsumptionDetails(docShareVoucherId);
		}catch(Exception e){
			
			throw e;
		}
		//return null;
	}

}
