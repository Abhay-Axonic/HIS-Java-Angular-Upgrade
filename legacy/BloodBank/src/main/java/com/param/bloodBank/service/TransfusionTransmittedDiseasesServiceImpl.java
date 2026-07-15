package com.param.bloodBank.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.param.bloodBank.dao.IBloodBagCollectionDao;
import com.param.bloodBank.dao.IBloodBankStockDao;
import com.param.bloodBank.dao.IBloodBankStockDetailsDao;
import com.param.bloodBank.dao.ITransfusionTransmittedDiseasesDao;
import com.param.bloodBank.dao.ITransfusionTransmittedDiseasesDetailsDao;
import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDetailsDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.bloodBank.model.BloodBankStock;
import com.param.bloodBank.model.TransfusionTransmittedDiseases;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class TransfusionTransmittedDiseasesServiceImpl implements ITransfusionTransmittedDiseasesService ,ICommonConstants {

	final static Logger logger = Logger.getLogger(TransfusionTransmittedDiseasesServiceImpl.class);

	
	@Autowired
	ITransfusionTransmittedDiseasesDao iTransfusionTransmittedDiseasesDao;
	
	@Autowired
	ITransfusionTransmittedDiseasesDetailsDao iTransfusionTransmittedDiseasesDetailsDao;
	
	@Autowired 
	IBloodBagCollectionDao iBloodBagCollectionDao;
	
	@Autowired
	private IBloodBankStockDao iBloodBankStockDao;
	
	@Autowired
	private IBloodBankStockDetailsDao iBloodBankStockDetailsDao;
	
	@Override
	public Response saveTransfusionTransmittedDiseases(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException {
		
			try {
				
				if(transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesId() != null) {
					Response res =  iTransfusionTransmittedDiseasesDao.updateTransfusionTransmittedDiseases(transfusionTransmittedDiseasesDto);
					
					if( res.getObject()!=null && transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesStatus() == 1 && transfusionTransmittedDiseasesDto.getBloodBankStockId() != null && transfusionTransmittedDiseasesDto.gettTDCheck() == true) {
						BloodBankStockDto  dto1 = new BloodBankStockDto();
						dto1.setBloodBankStockId(transfusionTransmittedDiseasesDto.getBloodBankStockId());
						dto1.setUpdatedBy(transfusionTransmittedDiseasesDto.getUpdatedBy());
						dto1.setUpdatedDate(transfusionTransmittedDiseasesDto.getUpdatedDate());
						dto1.setStockStatusId(6);
					
						Response res4=  iBloodBankStockDao.updateLoanInStatus(dto1);
						
						if(res4.getObject()!=null) {
							
								BloodBankStock bloodBankStock = new BloodBankStock();
								bloodBankStock = (BloodBankStock) res4.getObject();
								
							  bloodBankStock = this.updateBlodBankStock(bloodBankStock);
								
						}
					}
					
					for(Iterator it=transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesDetails().iterator();it.hasNext();){
					
						TransfusionTransmittedDiseasesDetailsDto dto = (TransfusionTransmittedDiseasesDetailsDto)it.next();
						dto.setTransfusionTransmittedDiseasesId(transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesId());
						
						if(dto.getTransfusionTransmittedDiseasesDetailsId() != null) {
							
							iTransfusionTransmittedDiseasesDetailsDao.updateTransfusionTransmittedDiseasesDetails(dto);
						}
						else {
							
							iTransfusionTransmittedDiseasesDetailsDao.saveTransfusionTransmittedDiseasesDetails(dto);
						}
					}
					
					
				}else {
				
					Response response = iTransfusionTransmittedDiseasesDao.checkUniqueReq(transfusionTransmittedDiseasesDto);
					int bloodBagCollectionId = 0;
					if(response.getObject()!=null){
					
						return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
					}else{
					
						Response respon =  iTransfusionTransmittedDiseasesDao.saveTransfusionTransmittedDiseases(transfusionTransmittedDiseasesDto);
						
						
						if(respon.getObject()!=null &&  transfusionTransmittedDiseasesDto.getBloodBankStockId()!=null) {
							
							int bloodBankStockId =  transfusionTransmittedDiseasesDto.getBloodBankStockId();
							Response updateStockTtdFlag = iBloodBankStockDao.updateStockTtdFlag(bloodBankStockId);

						}
						
						if( respon.getObject()!=null && transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesStatus() == 1 && transfusionTransmittedDiseasesDto.getBloodBankStockId() != null && transfusionTransmittedDiseasesDto.gettTDCheck() == true) {
							BloodBankStockDto  dto1 = new BloodBankStockDto();
							dto1.setBloodBankStockId(transfusionTransmittedDiseasesDto.getBloodBankStockId());
							dto1.setUpdatedBy(transfusionTransmittedDiseasesDto.getUpdatedBy());
							dto1.setUpdatedDate(transfusionTransmittedDiseasesDto.getUpdatedDate());
							dto1.setStockStatusId(6);
						
							Response res4=  iBloodBankStockDao.updateLoanInStatus(dto1);
							
							if(res4.getObject()!=null) {
								
									BloodBankStock bloodBankStock = new BloodBankStock();
									bloodBankStock = (BloodBankStock) res4.getObject();
									
								  bloodBankStock = this.updateBlodBankStock(bloodBankStock);
									
							}
						}
						
						if(respon.getObject() !=null && respon.getStatus().equals("success") ) {
							TransfusionTransmittedDiseases dtot = new TransfusionTransmittedDiseases();
							dtot=(TransfusionTransmittedDiseases) respon.getObject();
							
							for(Iterator it=transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesDetails().iterator();it.hasNext();){
					
								TransfusionTransmittedDiseasesDetailsDto dto = (TransfusionTransmittedDiseasesDetailsDto)it.next();
								dto.setTransfusionTransmittedDiseasesId(dtot.getTransfusionTransmittedDiseasesId());
							
								iTransfusionTransmittedDiseasesDetailsDao.saveTransfusionTransmittedDiseasesDetails(dto);
								
								
								
							}
							if(respon.getObject()!=null && dtot.getBloodBagCollectionId()!=null) {
								bloodBagCollectionId = dtot.getBloodBagCollectionId();
								iBloodBagCollectionDao.updateForwordedTo(bloodBagCollectionId);
							}
						}
						return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);					
					}
				
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);		
			} catch (Exception e) {
				logger.error("saveTransfusionTransmittedDiseases failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	private BloodBankStock updateBlodBankStock(BloodBankStock bloodBankStock) {

			BloodBankStockDetailsDto bloodBankStockDetailsDto = new BloodBankStockDetailsDto();
			bloodBankStockDetailsDto.setBloodBankId(bloodBankStock.getBloodBankId());
			bloodBankStockDetailsDto.setBloodBagCapacityId(bloodBankStock.getBloodBagCapacityId());
			bloodBankStockDetailsDto.setBloodGroupId(bloodBankStock.getBloodGroupId());
			bloodBankStockDetailsDto.setBloodBagTypeId(bloodBankStock.getBloodBagTypeId());
			bloodBankStockDetailsDto.setUnitId(bloodBankStock.getUnitId());
			bloodBankStockDetailsDto.setBloodComponentId(bloodBankStock.getBloodComponentId());
			bloodBankStockDetailsDto.setOrganizationId(bloodBankStock.getOrganizationId());

			Response bloodBankStockDetailsRes = iBloodBankStockDetailsDao.searchBloodBankStockDetails(bloodBankStockDetailsDto);

			if (bloodBankStockDetailsRes.getObject() != null) {
					BloodBankStockDetailsDto mstr = new BloodBankStockDetailsDto();
					mstr = (BloodBankStockDetailsDto) bloodBankStockDetailsRes.getObject();
					int availableStock = mstr.getAvailableStock();
					availableStock += bloodBankStock.getBloodUnit();
					mstr.setAvailableStock(availableStock);
					mstr.setUpdatedBy(bloodBankStock.getUpdatedBy());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBankStock.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
					iBloodBankStockDetailsDao.updateBloodBankStockDetails(mstr);
			} else {

					BloodBankStockDetailsDto mstr = new BloodBankStockDetailsDto();

					mstr.setBloodBagTypeId(bloodBankStock.getBloodBagTypeId());
					mstr.setAvailableStock(bloodBankStock.getBloodUnit());
					mstr.setBloodBankId(bloodBankStock.getBloodBankId());
					mstr.setBloodBagCapacityId(bloodBankStock.getBloodBagCapacityId());
					mstr.setBloodGroupId(bloodBankStock.getBloodGroupId());
					mstr.setCreatedBy(bloodBankStock.getCreatedBy());
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bloodBankStock.getUnitId()), "dd-M-yyyy HH:ss:mm"));
					mstr.setOrganizationId(bloodBankStock.getOrganizationId());
					mstr.setStatus(bloodBankStock.getStatus());
					mstr.setUnitId(bloodBankStock.getUnitId());
					mstr.setUpdatedBy(bloodBankStock.getUpdatedBy());
					mstr.setBloodComponentId(bloodBankStock.getBloodComponentId());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bloodBankStock.getUnitId()), "dd-M-yyyy HH:ss:mm"));
					iBloodBankStockDetailsDao.saveBloodBankStockDetails(mstr);

			}
		return bloodBankStock;
	}

	@Override
	public Response searchTTD(@RequestBody TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) {
		try {
			
				return iTransfusionTransmittedDiseasesDao.searchTTD(transfusionTransmittedDiseasesDto);
		
		} catch (Exception e) {
			logger.error("searchTTD failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTTDDetailsById(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)throws ApplicationException {
		try {
			
			return iTransfusionTransmittedDiseasesDetailsDao.getTTDDetailsById(transfusionTransmittedDiseasesDto);
		}
	catch (Exception e) {
		logger.error("getTTDDetailsById failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	@Transactional
	public Response getTTDListOnRedCellGroupingAndSerum(
			TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException {
		try {
			return iTransfusionTransmittedDiseasesDao.getTTDListOnRedCellGroupingAndSerum(transfusionTransmittedDiseasesDto);
		}
	catch (Exception e) {
		logger.error("getTTDListOnRedCellGroupingAndSerum failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	
}
