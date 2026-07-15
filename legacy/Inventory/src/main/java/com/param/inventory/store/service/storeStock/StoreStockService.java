package com.param.inventory.store.service.storeStock;

import javax.transaction.Transactional;

import com.param.inventory.store.dto.storeStock.StoreStockResponse;
import com.param.inventory.store.dto.storeStock.SubstituteStoreStockDashboardDto;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.BatchResponse1;
import com.param.global.dto.items.ItemsResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.storeStock.IStoreStockDao;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockRequest;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockResponse;
import com.param.inventory.store.dto.storeStock.StockInTransitResponse;
import com.param.inventory.store.dto.storeStock.StoreStockRequest;
import com.param.inventory.store.dto.storeStock.StoreStockRequestDate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("StoreStockService")
public class StoreStockService implements IStoreStockService {

	@Autowired
	IStoreStockDao StoreStockDao;
	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	private ISalesWorkListDao salesWorkListDao;
	
	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;

	@Override
	@Transactional
	public Object getStoreStock(StoreStockRequest request) {
		return StoreStockDao.getStoreStock(request);
	}

	@Override
	@Transactional
	public List<com.param.global.dto.items.BatchResponse1> getBatchDetails(int storeId, int itemId, boolean showDetails,
			Integer unitId) {

		List<com.param.global.dto.items.BatchResponse1> data = StoreStockDao.getBatchDetails(storeId, itemId,
				showDetails, unitId);

		// String batchNo = "";
		// Boolean isConsignment = false;
		for (com.param.global.dto.items.BatchResponse1 r : data) {

			// com.param.global.dto.items.BatchResponse1
			// updatedBatch=StoreStockDao.getUpdatedBatchDetails(storeId,r.getBatchid(),unitId)
			// ;
			if(r.getUomtype()==null && r.getUomunit()==null)
			{
				List<com.param.global.dto.items.BatchResponse1> data1 = StoreStockDao.getUOMUnitType(storeId, itemId, unitId,r.getBatchno());
				for (com.param.global.dto.items.BatchResponse1 r1 : data1) {
					
					r.setUomtype(r1.getUomtype());
					r.setUomunit(r1.getUomunit());
				}
			}

			BigInteger availableQty = consignmentStockConversionDao.getAvailableWithBatchIdStock(
					r.getBatchid().longValue(), (r.getIsconsignment() != null ? r.getIsconsignment() : false), storeId,
					itemId, unitId);
			r.setAvailablestockquantity(availableQty);

			/*
			 * if (r.getLotno() != null && r.getBatchno() !=null ) { boolean
			 * isConsignmentFlag = true; if (r.getIsconsignment() != null) {
			 * isConsignmentFlag = r.getIsconsignment(); } // Integer sCstock = //
			 * consignmentStockConversionDao.getTotalConsignmentStockFromBatch(r.getBatchno(
			 * ),r.getLotno()); if( !r.getIsconsignment()) { // BigInteger availableQty =
			 * consignmentStockConversionDao.getAvailableStock(r.getBatchno(),
			 * r.getLotno(),r.getIsconsignment(), storeId, itemId,unitId); BigInteger
			 * availableQty =
			 * consignmentStockConversionDao.getAvailableStockWithoutLotStock(r.getBatchid()
			 * .intValue(),isConsignmentFlag, storeId, itemId,unitId);
			 * 
			 * r.setAvailablestockquantity(availableQty); }
			 * 
			 * // r.setTotalstock(sCstock);
			 * 
			 * //start code by minesh for resolved bug no HIS-8018, HIS-8415
			 * if(r.getIsconsignment()!= null && r.getIsconsignment()) { BigInteger
			 * totalQtyStock =
			 * consignmentStockConversionDao.getTotalStoreTotalQty(r.getBatchno(),
			 * r.getLotno(), true, storeId, itemId,unitId); if(totalQtyStock.longValue() >
			 * 0) { r.setTotalstock(totalQtyStock); } } //end code by minesh for resolved
			 * bug no HIS-8018, HIS-8415
			 * 
			 * } else { // Integer sCstock = //
			 * consignmentStockConversionDao.getTotalStock(r.getBatchno()); //
			 * r.setTotalstock(sCstock); boolean isConsignmentFlag = true; if
			 * (r.getIsconsignment() != null) { isConsignmentFlag = r.getIsconsignment(); }
			 * 
			 * if(r.getBatchno() != null && !r.getIsconsignment() ) { //
			 * consignmentStockConversionDao.getAvailableStockWithoutLotStock(batchId,
			 * isconsignment, storeId, itemId, unitId) BigInteger availableQty =
			 * consignmentStockConversionDao.getAvailableStockWithoutLotStock(r.getBatchid()
			 * .intValue(),isConsignmentFlag, storeId, itemId,unitId);
			 * r.setAvailablestockquantity(availableQty);
			 * 
			 * //start code by minesh for resolved bug no HIS-8018, HIS-8415
			 * if(r.getIsconsignment()!= null && r.getIsconsignment()) { BigInteger
			 * totalQtyStock =
			 * consignmentStockConversionDao.getTotalStoreTotalQty(r.getBatchno(), null,
			 * true, storeId, itemId,unitId); if(totalQtyStock.longValue() > 0) {
			 * r.setTotalstock(totalQtyStock); } } //end code by minesh for resolved bug no
			 * HIS-8018, HIS-8415
			 * 
			 * } else if(r.getBatchno() == null && r.getLotno() !=null) {
			 * if(r.getIsconsignment() != null && !r.getIsconsignment()) { BigInteger
			 * availableQty =
			 * consignmentStockConversionDao.getAvailableWithoutBatchStock(r.getLotno(),
			 * isConsignmentFlag, storeId, itemId,unitId);
			 * r.setAvailablestockquantity(availableQty); BigInteger availableQty =
			 * consignmentStockConversionDao.getAvailableWithBatchIdStock(r.getBatchid().
			 * longValue(), isConsignmentFlag, storeId, itemId,unitId);
			 * r.setAvailablestockquantity(availableQty); }
			 * 
			 * //start code by minesh for resolved bug no HIS-8018, HIS-8415 if
			 * (r.getIsconsignment() != null && r.getIsconsignment()) { BigInteger
			 * totalQtyStock =
			 * consignmentStockConversionDao.getTotalStoreTotalQty(null,r.getLotno(), true,
			 * storeId, itemId,unitId); if (totalQtyStock.longValue() > 0) {
			 * r.setTotalstock(totalQtyStock); } } //end code by minesh for resolved bug no
			 * HIS-8018, HIS-8415 } else { BigInteger availableQty =
			 * consignmentStockConversionDao.getAvailableWithBatchIdStock(r.getBatchid().
			 * longValue(), isConsignmentFlag, storeId, itemId,unitId);
			 * r.setAvailablestockquantity(availableQty); }
			 * 
			 * //start code for HIS-9206 Fixed asset opening balance showing available
			 * quantity as 0 and consignment flag as blank if(r.getIsconsignment() == null)
			 * { BigInteger availableQty =
			 * consignmentStockConversionDao.getAvailableBatchStock(r.getBatchno(),r.
			 * getLotno(), storeId, itemId,unitId);
			 * r.setAvailablestockquantity(availableQty); r.setIsconsignment(false); } //end
			 * code for HIS-9206 Fixed asset opening balance showing available quantity as 0
			 * and consignment flag as blank }
			 */
		}
		/*
		 * if(!showDetails) { List<com.param.global.dto.items.BatchResponse1>
		 * dataWithoutBatch = StoreStockDao.getBatchNotApplicableDetails(storeId,
		 * itemId,unitId); for (com.param.global.dto.items.BatchResponse1 r :
		 * dataWithoutBatch) { boolean isConsignmentFlag = true; if
		 * (r.getIsconsignment() != null) { isConsignmentFlag = r.getIsconsignment(); }
		 * 
		 * BigInteger availableQty =
		 * consignmentStockConversionDao.getAvailableWithoutBatchStock(r.getLotno(),
		 * isConsignmentFlag, storeId, itemId,unitId);
		 * r.setAvailablestockquantity(availableQty); if (r.getIsconsignment() != null
		 * && r.getIsconsignment()) { BigInteger totalQtyStock =
		 * consignmentStockConversionDao.getTotalStoreTotalQty(null, r.getLotno(), true,
		 * storeId, itemId,unitId); if (totalQtyStock.longValue() > 0) {
		 * r.setTotalstock(totalQtyStock); } } } if(data!=null && data.size()>0)
		 * data.addAll(dataWithoutBatch); else data = dataWithoutBatch; }
		 */
		return data;
	}

	@Override
	@Transactional
	public Object getStoreIndentDateWiseList(StoreStockRequestDate request) {
		List<StoreStockResponse> responseList = new ArrayList<>();
		responseList = StoreStockDao.getStoreIndentDateWiseList(request);

		return responseList;
	}

	@Override
	@Transactional
	public Object getItemWiseBatchDetails(int storeId, int itemId) {
		return StoreStockDao.getItemWiseBatchDetails(storeId, itemId);
	}

	@Override
	@Transactional
	public Object getItemWiseBatchDetailsWithUnit(int itemId, Integer unitId) {
		return StoreStockDao.getItemWiseBatchDetailsWithUnit(itemId, unitId);
	}

	@Override
	@Transactional
	public Object getBatchWiseBatchDetails(int batchId, Integer unitId) {
		return StoreStockDao.getBatchWiseBatchDetails(batchId, unitId);
	}

	@Override
	@Transactional
	public List<ExpiredStoreStockResponse> expiredStoreStockDashboard(ExpiredStoreStockRequest request) {
		// return StoreStockDao.expiredStoreStockDashboard(request);

		List<ExpiredStoreStockResponse> response = StoreStockDao.expiredStoreStockDashboard(request);

		/*
		 * for (ExpiredStoreStockResponse batch : response) { List<UomResponse> uomList
		 * = GlobalDao.getBatchUomList(batch.getBatchId());
		 * 
		 * if (uomList != null && !uomList.isEmpty()) { UomResponse sUom =
		 * Util.getStoreUom(uomList); UomResponse dUom = Util.getOpDispenseUom(uomList);
		 * // UomResponse p Integer conversion = sUom.getConversion() *
		 * dUom.getConversion();
		 * 
		 * batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(),
		 * sUom.getUomUnit()); batch.setAvailableQuantity(batch.getAvailableQuantity() /
		 * conversion); } }
		 */
		return response;
	}

	@Override
	@Transactional
	public List<BatchResponse1> getExpiredBatchDetails(int storeId, int itemId, boolean showDetails, Integer unitId) {

		List<com.param.global.dto.items.BatchResponse1> data = StoreStockDao.getExpiredBatchDetails(storeId, itemId,
				showDetails, unitId);

		// String batchNo = "";
		// Boolean isConsignment = false;
		/*
		 * for (com.param.global.dto.items.BatchResponse1 r : data) { if (r.getLotno()
		 * != null && r.getBatchno() !=null) { // Integer sCstock = //
		 * consignmentStockConversionDao.getTotalConsignmentStockFromBatch(r.getBatchno(
		 * ),r.getLotno()); BigInteger availableQty =
		 * consignmentStockConversionDao.getAvailableStock(r.getBatchno(), r.getLotno(),
		 * r.getIsconsignment(), storeId, itemId,unitId);
		 * r.setAvailablestockquantity(availableQty); // r.setTotalstock(sCstock);
		 * 
		 * //start code by minesh for resolved bug no HIS-8018, HIS-8415
		 * if(r.getIsconsignment()!= null && r.getIsconsignment()) { BigInteger
		 * totalQtyStock =
		 * consignmentStockConversionDao.getTotalStoreTotalQty(r.getBatchno(),
		 * r.getLotno(), true, storeId, itemId,unitId); if(totalQtyStock.longValue() >
		 * 0) { r.setTotalstock(totalQtyStock); } } //end code by minesh for resolved
		 * bug no HIS-8018, HIS-8415
		 * 
		 * } else { // Integer sCstock = //
		 * consignmentStockConversionDao.getTotalStock(r.getBatchno()); //
		 * r.setTotalstock(sCstock); boolean isConsignmentFlag = true; if
		 * (r.getIsconsignment() != null) { isConsignmentFlag = r.getIsconsignment(); }
		 * 
		 * if(r.getBatchno() != null && r.getLotno() ==null) { BigInteger availableQty =
		 * consignmentStockConversionDao.getAvailableWithoutLotStock(r.getBatchno(),
		 * isConsignmentFlag, storeId, itemId,unitId);
		 * r.setAvailablestockquantity(availableQty);
		 * 
		 * //start code by minesh for resolved bug no HIS-8018, HIS-8415
		 * if(r.getIsconsignment()!= null && r.getIsconsignment()) { BigInteger
		 * totalQtyStock =
		 * consignmentStockConversionDao.getTotalStoreTotalQty(r.getBatchno(), null,
		 * true, storeId, itemId,unitId); if(totalQtyStock.longValue() > 0) {
		 * r.setTotalstock(totalQtyStock); } } //end code by minesh for resolved bug no
		 * HIS-8018, HIS-8415
		 * 
		 * } else if(r.getBatchno() == null && r.getLotno() !=null) { BigInteger
		 * availableQty =
		 * consignmentStockConversionDao.getAvailableWithoutBatchStock(r.getLotno(),
		 * isConsignmentFlag, storeId, itemId,unitId);
		 * r.setAvailablestockquantity(availableQty); //start code by minesh for
		 * resolved bug no HIS-8018, HIS-8415 if (r.getIsconsignment() != null &&
		 * r.getIsconsignment()) { BigInteger totalQtyStock =
		 * consignmentStockConversionDao.getTotalStoreTotalQty(null,r.getLotno(), true,
		 * storeId, itemId,unitId); if (totalQtyStock.longValue() > 0) {
		 * r.setTotalstock(totalQtyStock); } } //end code by minesh for resolved bug no
		 * HIS-8018, HIS-8415 }
		 * 
		 * //start code for HIS-9206 Fixed asset opening balance showing available
		 * quantity as 0 and consignment flag as blank if(r.getIsconsignment() == null)
		 * { BigInteger availableQty =
		 * consignmentStockConversionDao.getAvailableBatchStock(r.getBatchno(),r.
		 * getLotno(), storeId, itemId,unitId);
		 * r.setAvailablestockquantity(availableQty); r.setIsconsignment(false); } //end
		 * code for HIS-9206 Fixed asset opening balance showing available quantity as 0
		 * and consignment flag as blank } }
		 */

		return data;

	}

	@Override
	@Transactional
	public Object getStoreIndentDateWiseZeroStockList(StoreStockRequestDate request) {

		List<StoreStockResponse> responseList = new ArrayList<>();
		responseList = StoreStockDao.getStoreIndentDateWiseZeroStockList(request);

		return responseList;

	}

	@Override
	@Transactional
	public List<BatchResponse1> getZeroStockBatchDetails(int storeId, int itemId, boolean showDetails, Integer unitId) {

		List<com.param.global.dto.items.BatchResponse1> data = StoreStockDao.getZeroStockBatchDetails(storeId, itemId,
				showDetails);

		return null;

	}

	@Override
	@Transactional
	public Object getStoreIndentDateWiseZeroWikthStockList(StoreStockRequestDate request) {

		List<StoreStockResponse> responseList = new ArrayList<>();
		responseList = StoreStockDao.getStoreIndentDateWiseZeroWikthStockList(request);

		return responseList;

	}

	@Override
	@Transactional
	public List<ExpiredStoreStockResponse> nextExpiredStoreStockDashboard(ExpiredStoreStockRequest request) {

		// return StoreStockDao.expiredStoreStockDashboard(request);

		List<ExpiredStoreStockResponse> response = StoreStockDao.nextExpiredStoreStockDashboard(request);

		return response;

	}

	@Override
	@Transactional
	public List<BatchResponse1> getNextExpiredBatchDetails(int storeId, int itemId, Integer month, Integer unitId) {

		List<com.param.global.dto.items.BatchResponse1> data = StoreStockDao.getNextExpiredBatchDetails(storeId, itemId,
				month, unitId);

		return data;

	}

	@Override
	@Transactional
	public List<StockInTransitResponse> stockInTransit(ExpiredStoreStockRequest request) {
		// TODO Auto-generated method stub
		List<StockInTransitResponse> data1=new ArrayList<>();
		if(request.getTransitQty().equals(0))
		{
			return data1;
		}
		List<StockInTransitResponse> data=StoreStockDao.stockInTransit(request);
		
		Integer qty=0;
		for(StockInTransitResponse r : data)
		{
			qty=qty+r.getTransitstock();
			data1.add(r);
			if(request.getTransitQty().equals(qty))
			{
			break;	
			}
			else if(qty >request.getTransitQty() )
			{
				qty=qty-r.getTransitstock();
				data1.remove(r);
				
			}
			
		}
		
		return data1;
	}
	
	@Override
	@Transactional
	public List<SubstituteStoreStockDashboardDto> getSubstituteDetailsForStoreStockDashboard(Integer genericId, Integer itemId, Boolean isNarcotic, Integer storeId, Integer unitId, String dateString) throws Exception {
		
		List<SubstituteStoreStockDashboardDto> finalList=new ArrayList<SubstituteStoreStockDashboardDto>();
		
		List<SubstituteStoreStockDashboardDto> subList = StoreStockDao.getSubstituteByIdForStoreStockDB(genericId, itemId, isNarcotic,storeId, unitId, dateString);
		for (SubstituteStoreStockDashboardDto listDto : subList) {
			SubstituteStoreStockDashboardDto subDto=new SubstituteStoreStockDashboardDto();
			subDto.setItemName(listDto.getItemName());
			subDto.setStoreName(listDto.getStoreName());
			subDto.setManufactureName(listDto.getManufactureName());
			subDto.setBatchNo(listDto.getBatchNo());
			subDto.setExpDate(listDto.getExpDate());
			subDto.setCurrentStock(listDto.getCurrentStock());
			subDto.setCop(listDto.getCop());
			subDto.setMrp(listDto.getMrp());
			subDto.setShelf(listDto.getShelf());
			subDto.setItemId(listDto.getItemId());
			subDto.setGenericId(listDto.getGenericId());
			subDto.setNarcotic(listDto.getNarcotic());
			subDto.setLocation(listDto.getLocation());
			subDto.setUom(listDto.getUom());
			subDto.setStoreId(listDto.getStoreId());
			subDto.setUomUnit(listDto.getUomunit());
			finalList.add(subDto);
		}
//		for (Item assignPickerItem : salesWorkListDao.getSubstituteByIdForStoreStockDB(genericId, itemId, isNarcotic,storeId, unitId, dateString)) {
//			List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
//			List<Batch> batchList = GlobalDao.getBatchDetailsPharmacyConsignment(assignPickerItem.getId(), storeId,unitId);
//
//			for (Batch batch : batchList) {
//				medicineBatches.add(Util.PopulateBatchWithStock(batch));
//			}
//
//			Long storeStock = GlobalDao.getStoreStockByItemPharmacy(storeId,
//					assignPickerItem.getId(),unitId);
//			
//			Boolean isOtc=assignPickerItem.getIsOtc()!=null ? assignPickerItem.getIsOtc():null;
//			String manufacturerName=assignPickerItem.getManufacturer().getName()!=null ?assignPickerItem.getManufacturer().getName():null;
//			Integer taxId=assignPickerItem.getSalesTax().getId();
//			Double taxPercentage=assignPickerItem.getSalesTax().getTaxPercentage();
//			Integer productCategoryId=assignPickerItem.getGeneric().getProductCategory().getId()!=null ? assignPickerItem.getGeneric().getProductCategory().getId():null;
////			Integer productCategoryId=assignPickerItem.getProductCategory()!=null ? assignPickerItem.getProductCategory().getId():null;
//			Boolean narcoticFlag=assignPickerItem.getIsNarcotic();
//			if(!medicineBatches.isEmpty() && storeStock != 0 ) {
//				PharmacologyItem pharmacologyItem = new PharmacologyItem(assignPickerItem.getId(),
//						assignPickerItem.getCode(), assignPickerItem.getName(), assignPickerItem.getGeneric().getName(),
//						storeStock, "Rack-001", medicineBatches, taxId, productCategoryId, narcoticFlag, manufacturerName, isOtc,taxPercentage);
//
//				pharmacologyItem.setGenericId(assignPickerItem.getGeneric().getId());
//				List<Integer> itemIds=new ArrayList<>();
//				itemIds.add(assignPickerItem.getId());
//				List<UomResponse> uoms =  GlobalDao.getUnitOfMeasurementListByItemBP(itemIds,assignPickerItem.getOrganizationId());
//				pharmacologyItem.setUoms(uoms);
//				pharmacologyItemList.add(pharmacologyItem);
//			}
//			
//		}
		return finalList;
	}
}
