package com.param.bloodBank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBagLoanOutDao;
import com.param.bloodBank.dao.IBloodBagLoanOutDetailsDao;
import com.param.bloodBank.dao.IBloodBankStockDao;
import com.param.bloodBank.dao.IBloodBankStockDetailsDao;
import com.param.bloodBank.dto.BloodBagLoanOutDetailsDto;
import com.param.bloodBank.dto.BloodBagLoanOutDto;
import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.model.BloodBagLoanOut;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodBagLoanOutServiceImpl implements IBloodBagLoanOutService, ICommonConstants,IBillingCommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagLoanOutServiceImpl.class);


	@Autowired
	IBloodBagLoanOutDao iBloodBagLoanOutDao;
	
	@Autowired 
	IBloodBagLoanOutDetailsDao iBloodBagLoanOutDetailsDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	 
	@Autowired
	private IBloodBankStockDao iBloodBankStockDao;
	
	@Autowired
	private IBloodBankStockDetailsDao iBloodBankStockDetailsDao;
	
	@Override
	public Response saveloanout(BloodBagLoanOutDto dto) throws ApplicationException {
		try {
			
			String loanOutNumber = iGlobalUnitMasterDao.getGeneratedNo(0, dto.getUnitId(), BLOOD_BAG_LOAN_OUT_NO);
			dto.setLoanOutNumber(loanOutNumber);
			
			Response res =  iBloodBagLoanOutDao.saveloanout(dto);
			if(res.getObject()!= null) {
				
				BloodBagLoanOut mstr = (BloodBagLoanOut)res.getObject();
				
				for (BloodBagLoanOutDetailsDto dtailsDto : dto.getBloodBagLoanOutDetailsList()) {
					dtailsDto.setBloodBagLoanOutId(mstr.getBloodBagLoanOutId());
					Response bagRes =	iBloodBagLoanOutDetailsDao.saveBloodBagLoanOutDetails(dtailsDto);
					if(bagRes.getObject()!=null) {
						BloodBankStockDto newDto = new BloodBankStockDto();
						newDto.setBloodBankStockId(dtailsDto.getBloodBankStockId());
						newDto.setStockStatusId(16); //loan out Pending
						newDto.setUpdatedBy(dto.getUpdatedBy());
						newDto.setUpdatedDate(dto.getUpdatedDate());
						iBloodBankStockDao.updateLoanInStatus(newDto);
					}
				}
				
				
			}
			return res;
		
	} catch (Exception e) {
		logger.error("saveloanout failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getLoanOutList(BloodBagLoanOutDto dto) throws ApplicationException {
		try {
			return iBloodBagLoanOutDao.getLoanOutList(dto);
		} catch (Exception e) {
			logger.error("getLoanOutList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getLoanOutDeatilsList(BloodBagLoanOutDto dto) throws ApplicationException {
		try {
			return iBloodBagLoanOutDetailsDao.getLoanOutDeatilsList(dto);
		} catch (Exception e) {
			logger.error("getLoanOutDeatilsList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response loanOutListApproval(BloodBagLoanOutDto dto) throws ApplicationException {
		try {
			Response res = iBloodBagLoanOutDao.approveLoanOut(dto);
			Integer listSize = 0;
			if (res.getObject() != null) {

				for (BloodBagLoanOutDetailsDto dtailsDto : dto.getBloodBagLoanOutDetailsList()) {
					// dtailsDto.setBloodBagLoanOutId(mstr.getBloodBagLoanOutId());
					Response bagRes = iBloodBagLoanOutDetailsDao.approveBloodBagLoanOutDetails(dtailsDto);
					if (bagRes.getObject() != null) {
						BloodBankStockDto newDto = new BloodBankStockDto();
						newDto.setBloodBankStockId(dtailsDto.getBloodBankStockId());
						newDto.setStockStatusId(dtailsDto.getLoanOutStatusId()); // loan out Accept - 7 / or reject - 5
						newDto.setUpdatedBy(dto.getUpdatedBy());
						newDto.setUpdatedDate(dto.getUpdatedDate());
						iBloodBankStockDao.updateLoanInStatus(newDto);
						listSize++;
					}
				}
				
				if(listSize.equals(dto.getBloodBagLoanOutDetailsList().size())) {
					
					List<BloodBagLoanOutDetailsDto> filterList = getFilterList(dto.getBloodBagLoanOutDetailsList());
					
					if(filterList != null) {
						
						for (BloodBagLoanOutDetailsDto dtailsDto : filterList) {
							BloodBankStockDetailsDto BloodBankStockD = new BloodBankStockDetailsDto();
							int count = 1;
							BloodBankStockDetailsDto bloodBankStockDetailsDto = new BloodBankStockDetailsDto();
							bloodBankStockDetailsDto.setBloodBankId(dtailsDto.getBloodBankId());
							bloodBankStockDetailsDto.setBloodBagCapacityId(dtailsDto.getBagCapacityId());
							bloodBankStockDetailsDto.setBloodGroupId(dtailsDto.getBloodGroupId());
							bloodBankStockDetailsDto.setBloodBagTypeId(dtailsDto.getBagTypeId());
							bloodBankStockDetailsDto.setUnitId(dtailsDto.getUnitId());
							bloodBankStockDetailsDto.setBloodComponentId(dtailsDto.getComponentId());
							bloodBankStockDetailsDto.setOrganizationId(dtailsDto.getOrganizationId());

							Response bloodBankStockDetailsRes = iBloodBankStockDetailsDao.searchBloodBankStockDetails(bloodBankStockDetailsDto);
							if(bloodBankStockDetailsRes.getObject()!= null) {
								BloodBankStockD = (BloodBankStockDetailsDto) bloodBankStockDetailsRes.getObject();
								int avaibleStock = BloodBankStockD.getAvailableStock();
								int bagcount = dtailsDto.getBagCount();
								count = avaibleStock - bagcount ;
								BloodBankStockD.setAvailableStock(count);
								BloodBankStockD.setUpdatedBy(dto.getCreatedBy());
								BloodBankStockD.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
								iBloodBankStockDetailsDao.updateBloodBankStockDetails(BloodBankStockD);
							}
							
						}
					}
					
					
				}
				

			}
			return res;

		} catch (Exception e) {
			logger.error("loanOutListApproval failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	private List<BloodBagLoanOutDetailsDto> getFilterList(List<BloodBagLoanOutDetailsDto> bloodBagLoanOutDetailsList) {
		try {
			List<BloodBagLoanOutDetailsDto> list = new ArrayList<BloodBagLoanOutDetailsDto>(); // final return list
			
			List<Integer> findCount = new ArrayList<>(); // find record Index array
			
			int mainIndex = 0;   //first List index
			Integer BagStatus = 7;
			for (BloodBagLoanOutDetailsDto mainDto : bloodBagLoanOutDetailsList) {
				
				if(mainDto.getLoanOutStatusId().equals(BagStatus)) {
					boolean findNextIndex = true; 
					
					// logic for already added or not 
					if(findCount.size()>0) {
						
						for (Integer f : findCount) {
							
							if(f==mainIndex) {
								
								findNextIndex = false;
							}
						}
					}
					
					if(findNextIndex) {
						
						int subIndex = 0;
						int bagCount = 1;
						
						// logic for find same bag occurs or not
						
						for (BloodBagLoanOutDetailsDto dto : bloodBagLoanOutDetailsList) {
							
							if(subIndex !=mainIndex) {
								
								if(mainDto.getBagCapacityId().equals(dto.getBagCapacityId()) && 
									mainDto.getBagTypeId().equals(dto.getBagTypeId()) && 
									mainDto.getBagCapacityId().equals(dto.getBagCapacityId()) &&
									mainDto.getBloodBankId().equals(dto.getBloodBankId()) && 
									mainDto.getBloodGroupId().equals(dto.getBloodGroupId()) && mainDto.getLoanOutStatusId().equals(BagStatus) ) {
									
									findCount.add(subIndex);
									bagCount = bagCount + 1;
								}
								
							}
						
							subIndex++;
						}
						
						mainDto.setBagCount(bagCount);
						list.add(mainDto);
					}
					mainIndex++;
				}
				
				
			}
			
			return list;
		} catch (Exception e) {
			logger.error("getFilterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
		

}
