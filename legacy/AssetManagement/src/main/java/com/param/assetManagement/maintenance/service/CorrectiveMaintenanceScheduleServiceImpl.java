package com.param.assetManagement.maintenance.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceAssetReplacementDao;
import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceDao;
import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceScheduleDao;
import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceSparesComsumptionDao;
import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceWorklistDao;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceScheduleDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceSparesComsumptionDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceWorklistDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceAssetReplacement;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceSchedule;
import com.param.common.constants.Screen;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.service.items.CustomException;
import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class CorrectiveMaintenanceScheduleServiceImpl  implements ICorrectiveMaintenanceScheduleService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceScheduleServiceImpl.class);

	@Autowired
	ICorrectiveMaintenanceScheduleDao iCorrectiveMaintenanceScheduleDao;
	
	@Autowired
	ICorrectiveMaintenanceSparesComsumptionDao iCorrectiveMaintenanceSparesComsumptionDao;
	
	@Autowired
	private ICorrectiveMaintenanceWorklistDao iCorrectiveMaintenanceWorklistDao;
	
	@Autowired
	private ICorrectiveMaintenanceDao iCorrectiveMaintenanceDao;
	
	@Autowired
	private ICorrectiveMaintenanceAssetReplacementDao iCorrectiveMaintenanceAssetReplacementDao;
	
	
	private Integer screenId = Screen.WitnessApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IWalkInSalesDao WalkInSalesDao;

	@Autowired
	private SessionService sessionService;
	
	@Override
	public Response saveCorrectiveMaintenanceSchedule(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto)
			throws ApplicationException {
		try {
			Response res =  iCorrectiveMaintenanceScheduleDao.saveCorrectiveMaintenanceSchedule(correctiveMaintenanceScheduleDto);
				if(res.getObject() != null && res.getStatus().equals(SUCCESS)) {
					CorrectiveMaintenanceSchedule mstr = (CorrectiveMaintenanceSchedule) res.getObject();
					for (CorrectiveMaintenanceSparesComsumptionDto dto : correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceSparesComsumptionDto()) {
						dto.setCorrectiveMaintenanceScheduleId(mstr.getCorrectiveMaintenanceScheduleId());
						iCorrectiveMaintenanceSparesComsumptionDao.saveCorrectiveMaintenanceSparesComsumption(dto);
					}
					CorrectiveMaintenanceDto correctiveMaintenanceDto = new CorrectiveMaintenanceDto();
					correctiveMaintenanceDto.setUpdatedBy(correctiveMaintenanceScheduleDto.getUpdatedBy());
					correctiveMaintenanceDto.setUpdatedDate(correctiveMaintenanceScheduleDto.getUpdatedDate());
					correctiveMaintenanceDto.setMaintenanceStatusId(correctiveMaintenanceScheduleDto.getMaintenanceScheduleStatusId());
					correctiveMaintenanceDto.setCorrectiveMaintenanceId(correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceId());
					
					iCorrectiveMaintenanceWorklistDao.cancelCorrectiveMaintenanceRequestFromWorklist(correctiveMaintenanceDto);
					iCorrectiveMaintenanceDao.cancelCorrectiveMaintenanceRequest(correctiveMaintenanceDto);
					
					CorrectiveMaintenanceWorklistDto dto = new CorrectiveMaintenanceWorklistDto();
					dto.setCorrectiveMaintenanceWorklistId(mstr.getCorrectiveMaintenanceWorklistId());
					dto.setMaintenanceStatusId(mstr.getMaintenanceScheduleStatusId()); // 2 for completed , 4 for work in process
					dto.setUpdatedBy(mstr.getUpdatedBy());
					dto.setUpdatedDate(correctiveMaintenanceScheduleDto.getUpdatedDate());
					iCorrectiveMaintenanceWorklistDao.updateExportedToScheduling(dto);
					
					if(mstr.getCorrectiveMaintenanceScheduleId()!=null && correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceReplacementDto() != null) {
						correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceReplacementDto().setCorrectiveMaintenanceSchedulingId(mstr.getCorrectiveMaintenanceScheduleId());
						Response resrepl = iCorrectiveMaintenanceAssetReplacementDao.saveCorrectiveMaintenanceAssetReplacement(correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceReplacementDto());
						
						if(resrepl.getObject() != null) {
							try {
								
								Long batchId= new Long(correctiveMaintenanceScheduleDto.getCorrectiveMaintenanceReplacementDto().getNewSerialNoId());
								
								allocateItemQuantity(batchId,1, screenId,correctiveMaintenanceScheduleDto.getUnitId(),correctiveMaintenanceScheduleDto.getStoreId(), correctiveMaintenanceScheduleDto);

							} catch (Exception e) {
								logger.error("Error while allocating replaced item quantity during corrective maintenance schedule save", e);
								throw new ServiceException(e.getMessage());
						}
							
						}
					
					}
					
				}
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
		 catch (Exception e) {
			logger.error("Error while saving corrective maintenance schedule", e);
			throw new ServiceException(e.getMessage());
		}

	}


	private Response allocateItemQuantity(Long batchId, Integer approvedDisposedQuantity, Integer screenId,Integer unitId,Integer storeId, CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto)
			throws CustomException {

		try {
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(sessionService.getStoreId(), batchId,0);//0 itemId;

			if (avaialableStoreStock >= approvedDisposedQuantity) {
				Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

				Integer balanceQuantity = batch.getCurrentQuantity() - approvedDisposedQuantity;

				batch.setCurrentQuantity(balanceQuantity);
				batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock - approvedDisposedQuantity);

				com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

				StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
						approvedDisposedQuantity, batch.getRate(), batch.getStoreStockList().get(0).getStore(), batch,0);
//				sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setCreateDefaults(correctiveMaintenanceScheduleDto.getOrganizationId(),correctiveMaintenanceScheduleDto.getUnitId(),
						correctiveMaintenanceScheduleDto.getUpdatedBy());
				WalkInSalesDao.saveStockTransaction(stockTransaction);

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			} else {
				throw new CustomException("Quantity entered is exceeding max quantity");
			}
		} catch (CustomException e) {
			throw new CustomException("Allocation of item quantity failed");
		} catch (Exception e) {
			logger.error("Error while allocating item quantity", e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	public Response getCorrectiveMaintenanceScheduleByMultipleCrit(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) throws ApplicationException {
		try {
				return iCorrectiveMaintenanceScheduleDao.getCorrectiveMaintenanceScheduleByMultipleCrit(correctiveMaintenanceScheduleDto);
			}
		 catch (Exception e) {
			logger.error("Error while fetching corrective maintenance schedule by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCorrectiveMainSparesComsumptionByitemId(CorrectiveMaintenanceSparesComsumptionDto correctiveMaintenanceSparesComsumptionDto)throws ApplicationException {
		try {
			return iCorrectiveMaintenanceSparesComsumptionDao.getCorrectiveMainSparesComsumptionByitemId(correctiveMaintenanceSparesComsumptionDto);
		}
	 catch (Exception e) {
		logger.error("Error while fetching corrective maintenance spares consumption by item id", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getvendorlistByBatchId(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto)throws ApplicationException {
		try {
			return iCorrectiveMaintenanceScheduleDao.getvendorlistByBatchId(correctiveMaintenanceWorklistDto);
		}
	 catch (Exception e) {
		logger.error("Error while fetching vendor list by batch id", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getSparesComsumptionlistById(CorrectiveMaintenanceScheduleDto orrectiveMaintenanceScheduleDto)throws ApplicationException {
		try {
			return iCorrectiveMaintenanceSparesComsumptionDao.getSparesComsumptionlistById(orrectiveMaintenanceScheduleDto);
		}
	 catch (Exception e) {
		logger.error("Error while fetching spares consumption list by id", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public Response getreplaceItemById(CorrectiveMaintenanceScheduleDto orrectiveMaintenanceScheduleDto)
			throws ApplicationException {
		try {
			
			return iCorrectiveMaintenanceAssetReplacementDao.getreplaceItemById(orrectiveMaintenanceScheduleDto);
		}
		catch (Exception e) {
		logger.error("Error while fetching replace item by id", e);
		throw new ServiceException(e.getMessage());
	 	}
	}

	@Override
	public Response getreplaceDetailOnReturnByMultipleCrit(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) throws ApplicationException {
		try {
			
			return iCorrectiveMaintenanceAssetReplacementDao.getreplaceDetailOnReturnByMultipleCrit(correctiveMaintenanceScheduleDto);
		}
		catch (Exception e) {
		logger.error("Error while fetching replace detail on return by multiple criteria", e);
		throw new ServiceException(e.getMessage());
	 	}
	}

	@Override
	public Response getdetailsOfCM(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto)
			throws ApplicationException {
		try {
			return iCorrectiveMaintenanceScheduleDao.getdetailsOfCM(correctiveMaintenanceScheduleDto);
		}
	 catch (Exception e) {
		logger.error("Error while fetching details of corrective maintenance", e);
		throw new ServiceException(e.getMessage());
	}
	}


	@Override
	public Response returnAsset(CorrectiveMaintenanceAssetReplacement correctiveMaintenanceAssetReplacement)
			throws ApplicationException {
		try {
			
			return iCorrectiveMaintenanceAssetReplacementDao.returnAsset(correctiveMaintenanceAssetReplacement);
		}
		catch (Exception e) {
		logger.error("Error while returning asset", e);
		throw new ServiceException(e.getMessage());
	 	}
	}


	@Override
	public Response rejectAsset(CorrectiveMaintenanceAssetReplacement correctiveMaintenanceAssetReplacement)
			throws ApplicationException {
		try {
			
			return iCorrectiveMaintenanceAssetReplacementDao.rejectAsset(correctiveMaintenanceAssetReplacement);
		}
		catch (Exception e) {
		logger.error("Error while rejecting asset", e);
		throw new ServiceException(e.getMessage());
	 	}
	}
}
