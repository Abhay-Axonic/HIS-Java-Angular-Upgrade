package com.param.billing.global.transaction.service;

import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.transaction.dao.IPaymentDepositConsumptionDao;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class PaymentDepositConsumptionServiceImpl implements IPaymentDepositConsumptionService,ICommonConstants{

	@Autowired
	private IDepositMasterService iDepositMasterService;
	
	@Autowired
	private IPaymentDepositConsumptionDao iPaymentDepositConsumptionDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	
	@Override
	public Response consumeDeposit(BillPaymentCollectionReqDto reqDto) throws ApplicationException {
		try{
			
			Double finalConsumption = 0.0;
			Integer depositConsumptionId = 0;
			//save call to deposit consumption 
			reqDto.setDepositeConsumptionNumber(this.getDepositeConsumptionNumber(reqDto.getUnitId()));
			Response res =iPaymentDepositConsumptionDao.addDepositConsumption(reqDto);
			depositConsumptionId = (Integer) res.getObject();
			
			ListIterator<PaymentDepositDto> iterator = reqDto.getPaymentDepositDetailsList().listIterator();
			while(iterator.hasNext())
			{
				PaymentDepositDto depositDto = iterator.next();
				
				if(depositDto.getBillId() != null)
					depositDto.setBillId(reqDto.getBillingMasterId()); 
				if(depositDto.getBillPayeeDetailsId() != null)
					depositDto.setBillPayeeDetailsId(reqDto.getBillPayeeDetailsId()); 
				
				Double consumedAmount = reqDto.getTotalDepositConsumption();
				Double detailsConsumedAmount = depositDto.getConsumedAmount();
				depositDto.setConsumedAmount(detailsConsumedAmount);
				//update call to deposit master
				iDepositMasterService.updateDepositMaster(depositDto);
				
				
				//save call to deposit consumption details
				PaymentDepositDetailsDto paymentDepositDetailsDtoConsumption = new PaymentDepositDetailsDto();	
					paymentDepositDetailsDtoConsumption.setOrganizationId(depositDto.getOrganizationId());
					paymentDepositDetailsDtoConsumption.setUnitId(depositDto.getUnitId());
					paymentDepositDetailsDtoConsumption.setDepositId(depositDto.getDepositMasterId());
					paymentDepositDetailsDtoConsumption.setDepConsumptionId(depositConsumptionId);
					paymentDepositDetailsDtoConsumption.setDepositConsumed(detailsConsumedAmount);
					paymentDepositDetailsDtoConsumption.setUpdatedBy(reqDto.getUpdatedBy());
					paymentDepositDetailsDtoConsumption.setUpdatedDate(reqDto.getUpdatedDate());
					paymentDepositDetailsDtoConsumption.setRemark(depositDto.getRemark());
				iPaymentDepositConsumptionDao.addDepositConsumptionDetails(paymentDepositDetailsDtoConsumption);
				
				Response res3 = iDepositMasterService.getDepositDetailsByDepositId(depositDto.getDepositMasterId());
				
				List<PaymentDepositDetailsDto> detailsByDepositIdDtosList = res3.getListObject();
				ListIterator<PaymentDepositDetailsDto> listIterator = detailsByDepositIdDtosList.listIterator();
				if(consumedAmount>0)
				{
					while(listIterator.hasNext() && consumedAmount>0)
					{
						PaymentDepositDetailsDto paymentDepositDetailsDto = listIterator.next();
						
						paymentDepositDetailsDto.setDepConsumptionId(depositConsumptionId);
						
						Double detailsAmount = Math.round(paymentDepositDetailsDto.getBalanceAmount() * 100.0)/100.0;
						
							if(consumedAmount>=detailsAmount){
								paymentDepositDetailsDto.setConsumedAmount(detailsAmount);
							}else{
								paymentDepositDetailsDto.setConsumedAmount(consumedAmount);
							}
							paymentDepositDetailsDto.setBalanceAmount(detailsAmount-paymentDepositDetailsDto.getConsumedAmount());
							
							//update call to deposit details
								paymentDepositDetailsDto.setCreatedBy(depositDto.getCreatedBy());
								paymentDepositDetailsDto.setUpdatedDate(depositDto.getDepConsumptionDate());
							iDepositMasterService.updateDepositDetails(paymentDepositDetailsDto);
							
							consumedAmount=consumedAmount-paymentDepositDetailsDto.getConsumedAmount();
							finalConsumption=finalConsumption+paymentDepositDetailsDto.getConsumedAmount();
						
						}
					}
				
			}
			return new Response(SUCCESS,null,depositConsumptionId.toString(),null,finalConsumption);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR,COMMON_ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
	}


	@Transactional
	private String getDepositeConsumptionNumber(Integer unitId) {
		try {
			/*Long incrementedId = iPaymentDepositConsumptionDao.getNextValDepositeConsumptionId();
			
			String uhidPrefix=null;
			uhidPrefix = "DC-18-";
			
			
			StringBuilder uhid=new StringBuilder(uhidPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				uhid = length == 1 ? (uhid.append("000000").append(incrementedId))
						: length == 2 ? (uhid.append("00000").append(incrementedId))
								: length == 3 ? (uhid.append("0000").append(incrementedId))
										: length == 4 ? (uhid.append("000").append(incrementedId))
												: length == 5 ? (uhid.append("00").append(incrementedId))
														: length == 6 ? (uhid.append("0").append(incrementedId))
																: length == 7 ? (uhid.append(incrementedId)) : uhid.append("");*/
			
			
				return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.DEPOSIT_CONSUMPTION_NUMBER);
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage(), e);
			}
	}

}
