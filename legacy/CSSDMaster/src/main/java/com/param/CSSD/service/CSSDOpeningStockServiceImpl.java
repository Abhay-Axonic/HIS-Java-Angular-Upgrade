package com.param.CSSD.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.CSSD.dao.ICSSDOpeningStockDao;
import com.param.CSSD.dao.IPreSterilizationProcessDao;
import com.param.CSSD.dto.CssdOpeningBalanceRequest;
import com.param.CSSD.dto.CssdOpeningStockDto;
import com.param.CSSD.model.CssdOpeningBalanceDetailStock;
import com.param.CSSD.model.CssdOpeningBalanceStock;
import com.param.common.constants.Screen;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CSSDOpeningStockServiceImpl implements ICSSDOpeningStockService,ICommonConstants{

	final static Logger logger = Logger.getLogger(CSSDOpeningStockServiceImpl.class);


	@Autowired
	private ICSSDOpeningStockDao icssdOpeningStockDao;

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private IPreSterilizationProcessDao iPreSterilizationProcessDao;
	
	private Integer screenId = Screen.OpeningBalanceImplant.getScreen();
	
	@Override
	@Transactional
	public Response saveOpeningStock(CssdOpeningStockDto cssdOpeningStockDto) {
		try {
			return icssdOpeningStockDao.saveOpeningStock(cssdOpeningStockDto);
		} catch (Exception e) {
			logger.error("saveOpeningStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCssdOpeningBalanceList(CssdOpeningBalanceRequest cssdOpeningStockDto) {
		try {
			return icssdOpeningStockDao.getCssdOpeningBalanceList(cssdOpeningStockDto);
		} catch (Exception e) {
			logger.error("getCssdOpeningBalanceList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response approveCssdOpeningBalanceStock(CssdOpeningBalanceRequest request) {
		try {
			for (Integer opbId : request.getOpeningBalanceImplantId()) {

				CssdOpeningBalanceStock openingBalance = icssdOpeningStockDao.getCssdOpeningBalanceById(opbId);

				Integer storeId = openingBalance.getStoreId();
				
				Map<Integer, List<CssdOpeningBalanceDetailStock>> map = new HashMap<>();
				for (CssdOpeningBalanceDetailStock details : openingBalance.getCssdOpeningBalanceDetailStockList()) {
					if (details.getIsActive() && !details.getIsDeleted()) {
						
						icssdOpeningStockDao.saveBatch(InsertApprovedItems(details, storeId));
						
						if(details.getIsSterilize().equals(true)) {
							icssdOpeningStockDao.saveStockTransactionLog(details,storeId);
							if(details.getIsSterilize().equals(true) && details.getIsKit().equals('Y')) {
							Integer kitId=details.getKitId();
							//List<CssdOpeningBalanceDetailStock> kitInstrumentListforIssue=new ArrayList<CssdOpeningBalanceDetailStock>();
							if (map.containsKey(kitId)) {
								map.get(kitId).add(details);
							}else {
								List<CssdOpeningBalanceDetailStock> kitInstrumentListforIssue=new ArrayList<CssdOpeningBalanceDetailStock>();
								kitInstrumentListforIssue.add(details);
								map.put(details.getKitId(),kitInstrumentListforIssue);
							}
							}
						 
						 if(details.getIsKit().equals('N'))
						 iPreSterilizationProcessDao.saveSterilizedStockForIssue(details,storeId);
						}
                         details.setStatusId(request.getStatusId());
						details.setApproveDate(TimeZoneComponent.getDateByzone(0));
						details.setApprovedBy(sessionService.getUserId());
						//details.setApprovedQuantity(details.getInstrumentQuantity());
						details.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
						details.setUpdatedBy(details.getUpdatedBy());
					}
				}
				
				//save sterilized stock of kit
				if(!(map.isEmpty())) {
					 iPreSterilizationProcessDao.saveSterilizedKitStockForIssue(storeId,map);
				}
				
				openingBalance.setStatusId(request.getStatusId());
				openingBalance.setApproveDate(TimeZoneComponent.getDateByzone(0));
				openingBalance.setApprovedBy(sessionService.getUserId());

				openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
				openingBalance.setUpdatedBy(sessionService.getUserId());

				icssdOpeningStockDao.updateCssdOpeningBalance(openingBalance);
			}
			return new Response<>(SUCCESS, null, "CSSD Opening Balance approved successfully", null, null);
		} catch (Exception e) {
			logger.error("approveCssdOpeningBalanceStock failed", e);
			throw new ServiceException(e.getMessage());
		}

	}


	private Batch InsertApprovedItems(CssdOpeningBalanceDetailStock detail, Integer storeId) 
	{

		Batch batch = new Batch();
		batch.setItem(new Item(detail.getItem().getId()));
		batch.setQuantity(detail.getInstrumentQuantity());
		batch.setCurrentQuantity(detail.getInstrumentQuantity());
		batch.setExpiryDate(detail.getSterilizationExpiryDate());
		batch.setBatchNo(detail.getBatchNo());
		batch.setIsOpb(true);
		batch.setCreateDefaults(sessionService.getOrganizationId(), sessionService.getUnitId(), sessionService.getUserId());
		//sessionService.setEntityDefaults(batch);
		StoreStock storeStock=	new StoreStock(detail.getInstrumentQuantity(), new Store(detail.getCssdOpeningBalanceStock().getStore().getStoreId()),detail.getInstrumentQuantity(), new Batch(batch.getId()));
		//sessionService.setEntityDefaults(storeStock);
		batch.addStoreStock(storeStock);

		//com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction=	new StockTransaction();
		stockTransaction.setQuantity(detail.getInstrumentQuantity());
		stockTransaction.setBatch(new Batch(batch.getId()));
		stockTransaction.setStore( new Store(storeId));
		stockTransaction.setCurrentBal(detail.getInstrumentQuantity());
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(sessionService.getOrganizationId(), sessionService.getUnitId(), sessionService.getUserId());
		batch.addStockTransaction(stockTransaction);
		return batch;
	}

	@Override
	@Transactional
	public Response cancelCssdOpeningBalanceStock(CssdOpeningBalanceRequest request) {
		try {
			
			CssdOpeningBalanceStock openingBalance = icssdOpeningStockDao.getCssdOpeningBalanceById(request.getId());

			for (CssdOpeningBalanceDetailStock details : openingBalance.getCssdOpeningBalanceDetailStockList()) {
				if (details.getIsActive() && !details.getIsDeleted()) {
					details.setStatusId(request.getStatusId());
					details.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
					details.setUpdatedBy(details.getUpdatedBy());
				}
			}
			openingBalance.setCancelReason(new CancelReason(request.getCancelReasonId()));
			openingBalance.setCancelNote(request.getCancelNote());
			openingBalance.setStatusId(request.getStatusId());
			openingBalance.setCancelReason(new CancelReason(request.getCancelReasonId()) );
			openingBalance.setStatus(new Status(request.getStatusId()));
			openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			openingBalance.setUpdatedBy(sessionService.getUserId());
			icssdOpeningStockDao.updateCssdOpeningBalance(openingBalance);
		return new Response<>(SUCCESS, null, "CSSD Opening Balance cancelled successfully", null, null);
	} catch (Exception e) {
		logger.error("cancelCssdOpeningBalanceStock failed", e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	@Transactional
	public Response getCssdOpeningBalanceImplantByOpeningBalanceId(CssdOpeningBalanceRequest request) {
		try {
	   return icssdOpeningStockDao.getCssdOpeningBalanceImplantByOpeningBalanceId(request);
	} catch (Exception e) {
		logger.error("getCssdOpeningBalanceImplantByOpeningBalanceId failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	@Transactional
	public Response updateCssdOpeningBalanceStock(CssdOpeningStockDto cssdOpeningStockDto) {
		try {
			return icssdOpeningStockDao.updateCssdOpeningBalanceStock(cssdOpeningStockDto);
		} catch (Exception e) {
			logger.error("updateCssdOpeningBalanceStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
