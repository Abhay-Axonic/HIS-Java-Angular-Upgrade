package com.param.bloodBank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBagLoanInDao;
import com.param.bloodBank.dao.IBloodBankStockDao;
import com.param.bloodBank.dao.IBloodBankStockDetailsDao;
import com.param.bloodBank.dto.BloodBagLoanInDto;
import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.model.BloodBagLoanIn;
import com.param.bloodBank.model.BloodBankStock;
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

public class BloodBankStockServiceImpl implements IBloodBankStockService,ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankStockServiceImpl.class);


	@Autowired
	private IBloodBankStockDao iBloodBankStockDao;
	
	@Autowired
	private IBloodBankStockDetailsDao iBloodBankStockDetailsDao;
	
	@Autowired
	private IBloodBagLoanInDao iBloodBagLoanInDao;
	

	@Override
	public Response saveBloodBankStock(List<BloodBankStockDto> list) throws ApplicationException {
		try {
			
			Long incrementedId = iBloodBagLoanInDao.getNextValBloodBagStockId();
			String loanNumber = generateBloodBagLoanNo(incrementedId);
			BloodBagLoanInDto bloodBagLoandto = new BloodBagLoanInDto();
			bloodBagLoandto.setLoanInNumber(loanNumber);
			bloodBagLoandto.setCreatedBy(list.getFirst().getCreatedBy());
			bloodBagLoandto.setUpdatedBy(list.getFirst().getUpdatedBy());
			bloodBagLoandto.setUpdatedDate(list.getFirst().getUpdatedDate());
			bloodBagLoandto.setCreatedDate(list.getFirst().getCreatedDate());
			bloodBagLoandto.setUnitId(list.getFirst().getUnitId());
			bloodBagLoandto.setOrganizationId(list.getFirst().getOrganizationId());
			Response loanInres = iBloodBagLoanInDao.savaloanIn(bloodBagLoandto);
			
			Response res = null;
			
			if(loanInres.getObject()!=null) {
				BloodBagLoanIn mstr = (BloodBagLoanIn) loanInres.getObject();
				
					if(list.size()>0 && list!=null)
					{
						for (BloodBankStockDto dto : list) {
							dto.setLoanNumber(loanNumber);
							dto.setLoanInId(mstr.getLoanInId());
							res = iBloodBankStockDao.saveBloodBankStock(dto);
						}
					}
					
			}		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error("saveBloodBankStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchBloodBankStock(BloodBankStockDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDao.searchBloodBankStock(dto);

		} catch (Exception e) {
			logger.error("searchBloodBankStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	private String generateBloodBagLoanNo(Long incrementedId) throws ApplicationException {
		try {
			
			//System.out.println(incrementedId);
			String loanNoPrefix="LN";
			StringBuilder loanNo=new StringBuilder(loanNoPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				loanNo = length == 1 ? (loanNo.append("000000").append(incrementedId))
						: length == 2 ? (loanNo.append("00000").append(incrementedId))
								: length == 3 ? (loanNo.append("0000").append(incrementedId))
										: length == 4 ? (loanNo.append("000").append(incrementedId))
												: length == 5 ? (loanNo.append("00").append(incrementedId))
														: length == 6 ? (loanNo.append("0").append(incrementedId))
																: length == 7 ? (loanNo.append(incrementedId)) : loanNo.append("");
				return loanNo.toString();
			} catch (Exception e) {
				logger.error("generateBloodBagLoanNo failed", e);
				throw new ServiceException(e.getMessage());
			}
			
	}

	@Override
	public Response updateBloodBankStockStatus(BloodBankStockDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDao.updateBloodBankStockStatus(dto);

		} catch (Exception e) {
			logger.error("updateBloodBankStockStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateLoanInStatus(BloodBankStockDto dto) throws ApplicationException {
		try {
			
					if(dto.gettTDCheck() == true) {
						
						if(dto.getStockStatusId() == 6 ) {
								
								dto.setStockStatusId(9);
								iBloodBankStockDao.updateLoanInStatus(dto);
								return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
								
						}else {
							
							iBloodBankStockDao.updateLoanInStatus(dto);
							return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
						}
					}
					else
					{
						Response res=  iBloodBankStockDao.updateLoanInStatus(dto);
						
						if(res.getObject()!=null && dto.getStockStatusId() == 6) {
							
								BloodBankStock bloodBankStock = new BloodBankStock();
								bloodBankStock = (BloodBankStock) res.getObject();
								
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
									BloodBankStockDetailsDto mstr = new BloodBankStockDetailsDto();
									mstr = (BloodBankStockDetailsDto) bloodBankStockDetailsRes.getObject();
									int availableStock = mstr.getAvailableStock();
									availableStock +=bloodBankStock.getBloodUnit();
									mstr.setAvailableStock(availableStock);
									mstr.setUpdatedBy(bloodBankStock.getUpdatedBy());
									mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
									iBloodBankStockDetailsDao.updateBloodBankStockDetails(mstr);
								}else {
								
									BloodBankStockDetailsDto mstr = new BloodBankStockDetailsDto();
								
									mstr.setBloodBagTypeId(bloodBankStock.getBloodBagTypeId());
									mstr.setAvailableStock(bloodBankStock.getBloodUnit());
									mstr.setBloodBankId(bloodBankStock.getBloodBankId());
									mstr.setBloodBagCapacityId(bloodBankStock.getBloodBagCapacityId());
									mstr.setBloodGroupId(bloodBankStock.getBloodGroupId());
									mstr.setCreatedBy(bloodBankStock.getCreatedBy());
									mstr.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bloodBankStock.getUnitId()),"dd-M-yyyy HH:ss:mm"));
									mstr.setOrganizationId(bloodBankStock.getOrganizationId());
									mstr.setStatus(bloodBankStock.getStatus());
									mstr.setUnitId(bloodBankStock.getUnitId());
									mstr.setUpdatedBy(bloodBankStock.getUpdatedBy());
									mstr.setBloodComponentId(bloodBankStock.getBloodComponentId());
									mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bloodBankStock.getUnitId()),"dd-M-yyyy HH:ss:mm"));
									return	iBloodBankStockDetailsDao.saveBloodBankStockDetails(mstr);
								
							}
							
							
						}
					}			
			return null;
			
		} catch (Exception e) {
			logger.error("updateLoanInStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchLoanInDetails(BloodBankStockDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDao.searchLoanInDetails(dto);

		} catch (Exception e) {
			logger.error("searchLoanInDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAvailableBloodGroupByBankID(BloodBankStockDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDao.getAvailableBloodGroupByBankID(dto);

		} catch (Exception e) {
			logger.error("getAvailableBloodGroupByBankID failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankStockListByTTDStatus(BloodBankStockDto bloodBankStockDto) throws ApplicationException {
		try {
			return iBloodBankStockDao.getBloodBankStockListByTTDStatus(bloodBankStockDto);

		} catch (Exception e) {
			logger.error("getBloodBankStockListByTTDStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getBagNoListByComponentAndGroupId(BloodBankStockDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDao.getBagNoListByComponentAndGroupId(dto);

		} catch (Exception e) {
			logger.error("getBagNoListByComponentAndGroupId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchLoanInData(BloodBagLoanInDto dto) throws ApplicationException {
		try {
			return iBloodBagLoanInDao.searchLoanInData(dto);

		} catch (Exception e) {
			logger.error("searchLoanInData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getLoanInDataForApproval(BloodBagLoanInDto dto) throws ApplicationException {
		try {
			return iBloodBagLoanInDao.getLoanInDataForApproval(dto);

		} catch (Exception e) {
			logger.error("getLoanInDataForApproval failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorBloodInStock(BloodBankStockDto bloodBankStock) throws ApplicationException {
		try {
			
			Response  res =  iBloodBankStockDao.updateDonorBloodInStock(bloodBankStock);
			
			if(res.getStatus().equals(SUCCESS)) {
				
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
					BloodBankStockDetailsDto mstr = new BloodBankStockDetailsDto();
					mstr = (BloodBankStockDetailsDto) bloodBankStockDetailsRes.getObject();
					int availableStock = mstr.getAvailableStock();
					availableStock += 1 ; // 1 for blood bag QTY
					mstr.setAvailableStock(availableStock);
					mstr.setUpdatedBy(bloodBankStock.getUpdatedBy());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBankStock.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
					iBloodBankStockDetailsDao.updateBloodBankStockDetails(mstr);
				}else {
				
					BloodBankStockDetailsDto mstr = new BloodBankStockDetailsDto();
					mstr.setBloodBagTypeId(bloodBankStock.getBloodBagTypeId());
					mstr.setAvailableStock(1); // 1 for blood bag QTY
					mstr.setBloodBankId(bloodBankStock.getBloodBankId());
					mstr.setBloodBagCapacityId(bloodBankStock.getBloodBagCapacityId());
					mstr.setBloodGroupId(bloodBankStock.getBloodGroupId());
					mstr.setCreatedBy(bloodBankStock.getCreatedBy());
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bloodBankStock.getUnitId()),"dd-M-yyyy HH:ss:mm"));
					mstr.setOrganizationId(bloodBankStock.getOrganizationId());
					mstr.setStatus(bloodBankStock.getStatus());
					mstr.setUnitId(bloodBankStock.getUnitId());
					mstr.setUpdatedBy(bloodBankStock.getUpdatedBy());
					mstr.setBloodComponentId(bloodBankStock.getBloodComponentId());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(bloodBankStock.getUnitId()),"dd-M-yyyy HH:ss:mm"));
					iBloodBankStockDetailsDao.saveBloodBankStockDetails(mstr);
				
				}
				
				iBloodBankStockDetailsDao.updateRedCellStatus(bloodBankStock.getRedCellId());
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("updateDonorBloodInStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchBagNoListByMultipleCriteria(BloodBankStockDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDao.searchBagNoListByMultipleCriteria(dto);

		} catch (Exception e) {
			logger.error("searchBagNoListByMultipleCriteria failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
