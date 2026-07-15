package com.param.bloodBank.service;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBankStockDao;
import com.param.bloodBank.dao.IBloodBankStockDetailsDao;
import com.param.bloodBank.dao.ICrossMatchRegisterDao;
import com.param.bloodBank.dao.IPatientRequisitionDetailsDao;
import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.dto.CrossMatchRegisterDetailsDto;
import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.bloodBank.model.BloodBankStock;
import com.param.bloodBank.model.CrossMatchRegister;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class CrossMatchRegisterServiceImpl implements ICrossMatchRegisterService,ICommonConstants{

	final static Logger logger = Logger.getLogger(CrossMatchRegisterServiceImpl.class);


	@Autowired
	private ICrossMatchRegisterDao iCrossMatchRegisterDao;
	
	@Autowired
	IPatientRequisitionDetailsDao iPatientRequisitionDetailsDao;
	
	@Autowired
	private IBloodBankStockDao iBloodBankStockDao;
	
	@Autowired
	private IBloodBankStockDetailsDao iBloodBankStockDetailsDao;

	@Override
	public Response saveCrossMatchRegister(CrossMatchRegisterDto crossMatchRegisterDto) throws ApplicationException {
		try {
			Response res =   iCrossMatchRegisterDao.saveCrossMatchRegister(crossMatchRegisterDto);
				
			if(res.getObject()!=null)
			{ 
				CrossMatchRegister mstr = (CrossMatchRegister)res.getObject();
				crossMatchRegisterDto.setCrossMatchRegisterId(mstr.getCrossMatchRegisterId());
				Response res1 = iCrossMatchRegisterDao.saveCrossMatchRegisterDetails(crossMatchRegisterDto);
				
				if(res1.getStatus().equals(SUCCESS))
				{
					Response res3 =null;
					if(crossMatchRegisterDto.getPendingQty()==0) {
					PatientRequisitionDetailsDto dto =new PatientRequisitionDetailsDto();
					dto.setUpdatedBy(crossMatchRegisterDto.getUpdatedBy());
					dto.setUpdatedDate(crossMatchRegisterDto.getUpdatedDate());
					dto.setBloodUnit(0);
					dto.setPatientRequisitionId(crossMatchRegisterDto.getPatientRequisitionId());
					dto.setExportedToCrossMatch(true);
					 res3 =  iPatientRequisitionDetailsDao.updateExportedToCrossMatchStatus(dto);
					}else {
						
						PatientRequisitionDetailsDto dto =new PatientRequisitionDetailsDto();
						dto.setUpdatedBy(crossMatchRegisterDto.getUpdatedBy());
						dto.setUpdatedDate(crossMatchRegisterDto.getUpdatedDate());
						dto.setBloodUnit(crossMatchRegisterDto.getPendingQty());
						dto.setPatientRequisitionId(crossMatchRegisterDto.getPatientRequisitionId());
						dto.setExportedToCrossMatch(false);
						 res3 =  iPatientRequisitionDetailsDao.updateExportedToCrossMatchStatus(dto);
						
						
					}
					if(res3.getObject()!=null)
					{
						List<CrossMatchRegisterDetailsDto> list = crossMatchRegisterDto.getCrossMatchRegisterDetails();
							
						if(list.size()>0) {
							BloodBankStockDetailsDto BloodBankStockD = new BloodBankStockDetailsDto();
							int availableStock=0;
							int count=0;
								for (CrossMatchRegisterDetailsDto crossMatchRegisterDetailsDto : list) {
								
									if(crossMatchRegisterDetailsDto.getCrossMatchRegisterDetailsStatus() == 1) {
									
											BloodBankStockDto bloodBankStockDto = new BloodBankStockDto();	
											bloodBankStockDto.setBloodBankStockId(crossMatchRegisterDetailsDto.getBloodBankStockId());	
											bloodBankStockDto.setUpdatedBy(crossMatchRegisterDetailsDto.getUpdatedBy());
											bloodBankStockDto.setStockStatusId(7);
											bloodBankStockDto.setUpdatedDate(crossMatchRegisterDetailsDto.getUpdatedDate());
											Response bloodBankStockResponse =  iBloodBankStockDao.updateLoanInStatus(bloodBankStockDto);	
								
											if(bloodBankStockResponse.getObject()!=null) {
									
													BloodBankStock bloodBankStock = new BloodBankStock();
													bloodBankStock = (BloodBankStock) bloodBankStockResponse.getObject();
									
													BloodBankStockDetailsDto bloodBankStockDetailsDto = new BloodBankStockDetailsDto();
													bloodBankStockDetailsDto.setBloodBankId(bloodBankStock.getBloodBankId());
													bloodBankStockDetailsDto.setBloodBagCapacityId(bloodBankStock.getBloodBagCapacityId());
													bloodBankStockDetailsDto.setBloodGroupId(bloodBankStock.getBloodGroupId());
													bloodBankStockDetailsDto.setBloodBagTypeId(bloodBankStock.getBloodBagTypeId());
													bloodBankStockDetailsDto.setUnitId(bloodBankStock.getUnitId());
													bloodBankStockDetailsDto.setBloodComponentId(bloodBankStock.getBloodComponentId());
													bloodBankStockDetailsDto.setOrganizationId(bloodBankStock.getOrganizationId());
								
													Response bloodBankStockDetailsRes = iBloodBankStockDetailsDao.searchBloodBankStockDetails(bloodBankStockDetailsDto);
														
													if(bloodBankStockDetailsRes.getObject()!= null) {
																BloodBankStockD = (BloodBankStockDetailsDto) bloodBankStockDetailsRes.getObject();
																availableStock = BloodBankStockD.getAvailableStock();
																count++;
													}
											}
									}
								
							}
								if(count!=0) {
								availableStock -=count;
								BloodBankStockD.setAvailableStock(availableStock);
								BloodBankStockD.setUpdatedBy(crossMatchRegisterDto.getUpdatedBy());
								BloodBankStockD.setUpdatedDate(GlobalCommonDateUtils.getDate(crossMatchRegisterDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
								return iBloodBankStockDetailsDao.updateBloodBankStockDetails(BloodBankStockD);
								}else{
									
									return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);					
								}
						}
					}
					
				}else
				{
					return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);			
				}
				
			}else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
		} catch (Exception e) {
			logger.error("saveCrossMatchRegister failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);					
	}

	@Override
	@Transactional
	public Response getCrossMatchRegisteredOnBagReservation(CrossMatchRegisterDto dto) throws ApplicationException {
		try {
			return iCrossMatchRegisterDao.getCrossMatchRegisteredOnBagReservation(dto);
	} catch (Exception e) {
		logger.error("getCrossMatchRegisteredOnBagReservation failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response updateCrossMatchRegisteredOnBagReservation(List<CrossMatchRegisterDto> dto)
			throws ApplicationException {
		try {
			return iCrossMatchRegisterDao.updateCrossMatchRegister(dto);
		}catch (Exception e) {
			logger.error("updateCrossMatchRegisteredOnBagReservation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStock(CrossMatchRegisterDto dto) throws ApplicationException {
		try {
			BloodBankStockDto bloodBankStockDto = new BloodBankStockDto();	
			bloodBankStockDto.setBloodBankStockId(dto.getBloodBankStockId());	
			bloodBankStockDto.setUpdatedBy(dto.getCreatedBy());
			bloodBankStockDto.setStockStatusId(6);
			bloodBankStockDto.setUpdatedDate(dto.getCreatedDate());
			Response bloodBankStockResponse =  iBloodBankStockDao.updateLoanInStatus(bloodBankStockDto);	

			if(bloodBankStockResponse.getObject()!=null) {
					BloodBankStockDetailsDto BloodBankStockD = new BloodBankStockDetailsDto();
					BloodBankStock bloodBankStock = new BloodBankStock();
					bloodBankStock = (BloodBankStock) bloodBankStockResponse.getObject();
					int count = 1;
					BloodBankStockDetailsDto bloodBankStockDetailsDto = new BloodBankStockDetailsDto();
					bloodBankStockDetailsDto.setBloodBankId(bloodBankStock.getBloodBankId());
					bloodBankStockDetailsDto.setBloodBagCapacityId(bloodBankStock.getBloodBagCapacityId());
					bloodBankStockDetailsDto.setBloodGroupId(bloodBankStock.getBloodGroupId());
					bloodBankStockDetailsDto.setBloodBagTypeId(bloodBankStock.getBloodBagTypeId());
					bloodBankStockDetailsDto.setUnitId(bloodBankStock.getUnitId());
					bloodBankStockDetailsDto.setBloodComponentId(bloodBankStock.getBloodComponentId());
					bloodBankStockDetailsDto.setOrganizationId(bloodBankStock.getOrganizationId());

					Response bloodBankStockDetailsRes = iBloodBankStockDetailsDao.searchBloodBankStockDetails(bloodBankStockDetailsDto);
					if(bloodBankStockDetailsRes.getObject()!= null) {
						BloodBankStockD = (BloodBankStockDetailsDto) bloodBankStockDetailsRes.getObject();
						count +=BloodBankStockD.getAvailableStock();
						BloodBankStockD.setAvailableStock(count);
						BloodBankStockD.setUpdatedBy(dto.getCreatedBy());
						BloodBankStockD.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
						iBloodBankStockDetailsDao.updateBloodBankStockDetails(BloodBankStockD);
					}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);	
		}catch (Exception e) {
			logger.error("updateStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCrossMatchRegisteredOnIssue(CrossMatchRegisterDto dto) throws ApplicationException {
		try {
			return iCrossMatchRegisterDao.getCrossMatchRegisteredOnIssue(dto);
	} catch (Exception e) {
		logger.error("getCrossMatchRegisteredOnIssue failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getCrossMatchRegisterDetailsByCrossMatchID(CrossMatchRegisterDto dto) throws ApplicationException {
		try {
			return iCrossMatchRegisterDao.getCrossMatchRegisterDetailsByCrossMatchID(dto);
	} catch (Exception e) {
		logger.error("getCrossMatchRegisterDetailsByCrossMatchID failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
}
