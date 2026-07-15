package com.param.bloodBank.service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBankStockDao;
import com.param.bloodBank.dao.IBloodBankStockDetailsDao;
import com.param.bloodBank.dao.ICrossMatchRegisterDao;
import com.param.bloodBank.dao.IIssueBloodBagDao;
import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.dto.IssueBloodBagDetailMapperDto;
import com.param.bloodBank.dto.IssueBloodBagDto;
import com.param.bloodBank.model.BloodBankStock;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unused"})
public class IssueBloodBagServiceImpl implements IIssueBloodBagService, ICommonConstants,IBillingCommonConstants{

	final static Logger logger = Logger.getLogger(IssueBloodBagServiceImpl.class);


	
	@Autowired
	IIssueBloodBagDao iIssueBloodBagDao;
	
	@Autowired
	private IBloodBankStockDao iBloodBankStockDao;
	
	@Autowired
	private IBloodBankStockDetailsDao iBloodBankStockDetailsDao;
	
	@Autowired
	ICrossMatchRegisterDao iCrossMatchRegisterDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	    
	
	@Override
	public Response saveIssueBloodBag(IssueBloodBagDto issueBloodBagDto) throws ApplicationException {
		try {
			String issueNumber = iGlobalUnitMasterDao.getGeneratedNo(0, issueBloodBagDto.getUnitId(), BLOOD_BAG_ISSUE_NUMBER);
			issueBloodBagDto.setBloodBagIssueNo(issueNumber);
			Response res = iIssueBloodBagDao.saveIssueBloodBag(issueBloodBagDto);
			
			if(res.getObject()!=null) {
				BloodBankStockDetailsDto BloodBankStockD = new BloodBankStockDetailsDto();
				int availableStock=0;
				int count=0;
				int qty = 0;
				if(issueBloodBagDto.getBloodBagIssueList().size()>0 && issueBloodBagDto.getBloodBagIssueList()!=null)
				{
					for (IssueBloodBagDetailMapperDto dto : issueBloodBagDto.getBloodBagIssueList()) {
						
						Response res1 = iIssueBloodBagDao.updateCrossMatchingDetailsStatus(dto);
						qty++;
						if(dto.getIssueBloodBagStatusId() == 2) {

							BloodBankStockDto bloodBankStockDto = new BloodBankStockDto();	
							bloodBankStockDto.setBloodBankStockId(dto.getBloodBankStockId());	
							bloodBankStockDto.setUpdatedBy(dto.getUpdatedBy());
							bloodBankStockDto.setStockStatusId(6);//for instock 6
							bloodBankStockDto.setUpdatedDate(dto.getUpdatedDate());
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
						availableStock +=count;
						BloodBankStockD.setAvailableStock(availableStock);
						BloodBankStockD.setUpdatedBy(issueBloodBagDto.getUpdatedBy());
						BloodBankStockD.setUpdatedDate(GlobalCommonDateUtils.getDate(issueBloodBagDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
						iBloodBankStockDetailsDao.updateBloodBankStockDetails(BloodBankStockD);
					}
					issueBloodBagDto.setBloodUnitQty(qty);
					return iCrossMatchRegisterDao.updateCrossMatchingReqQtyByCrossMatchId(issueBloodBagDto);
				}
			}
		} catch (Exception e) {
			logger.error("saveIssueBloodBag failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	@Transactional
	public Response getIssueBloodBagOnIssueAcceptanceScreen(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)
			throws ApplicationException {
		try {
			
			return iIssueBloodBagDao.getIssueBloodBagOnIssueAcceptanceScreen(bloodBagIssueAcceptanceDto);
			
		} catch (HibernateException he) {
			logger.error("getIssueBloodBagOnIssueAcceptanceScreen failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getIssueBloodBagOnIssueAcceptanceScreen failed", e);
			throw new ServiceException(e.getMessage());
		}
		}


	@Override
	public Response updateIssuedStatusByIssueBloodBagDetailsId(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)
			throws ApplicationException {
try {
			
			return iIssueBloodBagDao.updateIssuedStatusByIssueBloodBagDetailsId(bloodBagIssueAcceptanceDto);
			
		} catch (HibernateException he) {
			logger.error("updateIssuedStatusByIssueBloodBagDetailsId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateIssuedStatusByIssueBloodBagDetailsId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

}
