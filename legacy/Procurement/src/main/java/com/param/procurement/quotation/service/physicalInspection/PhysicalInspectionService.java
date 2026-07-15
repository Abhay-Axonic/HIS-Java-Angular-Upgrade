package com.param.procurement.quotation.service.physicalInspection;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.Organization;
import com.param.entity.model.master.Rack;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Unit;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.entity.model.procurement.GrnBonusUomMapper;
import com.param.entity.model.procurement.GrnUomMapper;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PhysicalInspectionDetail;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.global.PurchaseOrderStagedDcDate;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.procurement.purchase.dao.purchaseOrder.IPurchaseOrderDao;
import com.param.procurement.purchase.dto.goodReceiptNote.IGrnBonusBatchExpiryDao;
import com.param.procurement.quotation.dao.physicalInspection.IPhysicalInspectionDao;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionDetails;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionItem;
import com.param.procurement.quotation.dto.physicalInspection.GrnBonusUomMapperResponse;
import com.param.procurement.quotation.dto.physicalInspection.GrnUomMapperResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhyscicalInspectionItem;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDetails;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDropdown;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSaveItem;
import com.param.procurement.quotation.dto.physicalInspection.RackResponse;
import com.param.procurement.quotation.dto.physicalInspection.RejectReasonResponse;
import com.param.procurement.quotation.dto.physicalInspection.ShelfResponse;
import com.param.procurement.quotation.dto.physicalInspection.StorageDetails;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class PhysicalInspectionService implements IPhysicalInspectionService {

	final static Logger logger = Logger.getLogger(PhysicalInspectionService.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	IItemsDao ItemsDao;

	@Autowired
	IGoodReceiptNoteDao grnDao;
	
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IPhysicalInspectionDao PhysicalInspectionDao;
	
	@Autowired
	private IPurchaseOrderDao purchaseOrderDao;
	
    @Autowired
    IGrnBonusBatchExpiryDao BonusBatchExpiryDao;

	private Integer screenId = Screen.PhysicalInspection.getScreen();
	
	@Override
	@Transactional
	public List<PhysicalInspectionResponse> getPhysicalnspectionList(PhysicalInspectionRequest physicalInspectionRequest) {
		List<PhysicalInspectionResponse> physicalInspectionList = new ArrayList<PhysicalInspectionResponse>();
		physicalInspectionRequest.setToDate((new DateTime(physicalInspectionRequest.getToDate())).plusDays(1).toDate());
		for(PhysicalInspection physicalInspections : PhysicalInspectionDao.getPhysicalnspectionList(physicalInspectionRequest, screenId))
		{
			PhysicalInspectionResponse physicalInspection = new PhysicalInspectionResponse(physicalInspections.getId(), physicalInspections.getAddedDate(), 
					physicalInspections.getGoodReceiptNote().getGrnNumber(), physicalInspections.getGoodReceiptNote().getStore().getStoreName(), 
					physicalInspections.getGoodReceiptNote().getAssetType().getType(), physicalInspections.getGoodReceiptNote().getInvoiceNumber(), physicalInspections.getGoodReceiptNote().getInvoiceDate(),
					physicalInspections.getGoodReceiptNote().getDcNumber(), physicalInspections.getGoodReceiptNote().getDcDate(), physicalInspections.getGoodReceiptNote().getVendor().getName(), 
					physicalInspections.getGoodReceiptNote().getAddedBy().getUserName(), physicalInspections.getStatus().getStatus(), physicalInspections.getStatus().getId(),
					physicalInspections.getGoodReceiptNote().getStore().getStoreId(),
					physicalInspections.getGoodReceiptNote().getIsConsignment());
			physicalInspectionList.add(physicalInspection);
		}
		return physicalInspectionList;
	}
	
	@Override
	@Transactional
	public PhysicalInspectionDetails getPhysicalnspectionDetails(PhysicalInspectionRequest physicalInspectionRequest)
	{
		PhysicalInspectionDetails physicalInspectionDetails = new PhysicalInspectionDetails();
		PhysicalInspection physicalInspection = PhysicalInspectionDao.getPhysicalnspectionDetailsForSearch(physicalInspectionRequest);
		
		List<PhyscicalInspectionItem> items = new ArrayList<PhyscicalInspectionItem>();
		PhyscicalInspectionItem item = new PhyscicalInspectionItem();
		for(PhysicalInspectionDetail physicalInspectionDetail : PhysicalInspectionDao.getPhysicalInspectionDetailByInspectionId(physicalInspection.getId(), physicalInspectionRequest.getUnitId()!=null ? physicalInspectionRequest.getUnitId() : 0))
		{
			if(physicalInspectionDetail.getPhysicalInspection().getGoodReceiptNote().getAssetType().getId().equals(Constants.getConsumableId())) {
			List<GrnUomMapperResponse> grnUomMapperResponse = new ArrayList<GrnUomMapperResponse>();
			for(GrnUomMapper grnUomMapper : physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomMappersList())
			{
				GrnUomMapperResponse grnUom = new GrnUomMapperResponse(grnUomMapper.getId(), grnUomMapper.getUomType().getId(), grnUomMapper.getUomUnit().getId(),
						grnUomMapper.getConversions(), grnUomMapper.getIpUom(), grnUomMapper.getOpUom(), grnUomMapper.getStoreUom());
				grnUomMapperResponse.add(grnUom);
			}
			List<GrnBonusUomMapperResponse> grnBonusUomMapperResponse = new ArrayList<GrnBonusUomMapperResponse>();
			for(GrnBonusUomMapper grnBonusUomMapper : physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnBonusUomMappersList())
			{
				GrnBonusUomMapperResponse grnBonusUomMappe = new GrnBonusUomMapperResponse(grnBonusUomMapper.getId(), grnBonusUomMapper.getUomType().getId(),
						grnBonusUomMapper.getUomUnit().getId(), grnBonusUomMapper.getConversions(), grnBonusUomMapper.getIpUom(), grnBonusUomMapper.getOpUom(),
						grnBonusUomMapper.getStoreUom());
				grnBonusUomMapperResponse.add(grnBonusUomMappe);
			}
			//physicalInspectionDetail.getLcop();
		/*	Long batchId=PhysicalInspectionDao.getBatchId(physicalInspectionDetail.getGoodReceiptNoteDetail().getId());
			Batch b=GlobalDao.getBatch(batchId);*/
			item = new PhyscicalInspectionItem(physicalInspectionDetail.getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getId(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getCode(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getName(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnApprovedQuantity(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomType().getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomType().getType(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomUnit().getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomUnit().getUnits(), grnUomMapperResponse, 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity() !=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity() :null, physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType().getId():null, 
							physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType() !=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType().getType() :null, physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit().getId():null, 
									physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit() !=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit().getUnits() :null, grnBonusUomMapperResponse, 	physicalInspectionDetail.getGoodReceiptNoteDetail().getCop(),
				    physicalInspectionDetail.getGoodReceiptNoteDetail().getMrp(), physicalInspectionDetail.getGoodReceiptNoteDetail().getSpecification(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getRemark(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnReceivedQuantity(), physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusReceivedQuantity(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber(), physicalInspectionDetail.getGoodReceiptNoteDetail().getExpiryDate(), physicalInspectionDetail.getGoodReceiptNoteDetail().getManufacturer().getName(),null);
			if(physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity()!=null)
			{
				item.setBonusApprovedQuantity(physicalInspectionDetail.getApprovedBonusQuantity()!=null ? physicalInspectionDetail.getApprovedBonusQuantity() :physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity());
			}
			item.setGrnRejectQty(physicalInspectionDetail.getGoodReceiptNoteDetail().getRejectedQuantity()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getRejectedQuantity() :0);
			}
			else if(physicalInspectionDetail.getPhysicalInspection().getGoodReceiptNote().getAssetType().getId().equals(Constants.getFixedAssetId())){
				item = new PhyscicalInspectionItem(physicalInspectionDetail.getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getId(), 
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getCode(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getName(), 
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnApprovedQuantity(), 
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity(),physicalInspectionDetail.getGoodReceiptNoteDetail().getCop(),
					    physicalInspectionDetail.getGoodReceiptNoteDetail().getMrp(), physicalInspectionDetail.getGoodReceiptNoteDetail().getSpecification(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getRemark(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnReceivedQuantity(), physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusReceivedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber(), physicalInspectionDetail.getGoodReceiptNoteDetail().getExpiryDate(), physicalInspectionDetail.getGoodReceiptNoteDetail().getManufacturer().getName(), null);
				item.setGrnRejectQty(physicalInspectionDetail.getGoodReceiptNoteDetail().getRejectedQuantity()!=null ? physicalInspectionDetail.getGoodReceiptNoteDetail().getRejectedQuantity() :0);

			}
			item.setApprovedQuantity(physicalInspectionDetail.getApprovedQuantity());
			item.setRejectQuantity(physicalInspectionDetail.getRejectQuantity());
			item.setStatusId(physicalInspectionDetail.getStatus().getId());
			item.setStatus(physicalInspectionDetail.getStatus().getStatus());			
			if(physicalInspectionDetail.getGoodReceiptNoteDetail().getPurchaseOrderDetail()!=null)

			{
				item.setReceivedItemSpecification(physicalInspectionDetail.getGoodReceiptNoteDetail().getPurchaseOrderDetail().getSpecification());
			}
			List<GrnBonusBatchExpiry> getList= BonusBatchExpiryDao.getGrnBonusBatchExpiryByGrnDetailId(physicalInspectionDetail.getGoodReceiptNoteDetail().getId());
            List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
            for (GrnBonusBatchExpiry getList1 : getList) {
                GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
                getList2.setBatchNo(getList1.getBatchNo());
                getList2.setBonusQuantity(getList1.getBonusQuantity());
                getList2.setExpiryDate(getList1.getExpiryDate());
                getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
                getList3.add(getList2);
            }
            item.setGrnBonusBatchExpiry(getList3);
			items.add(item);
		}
		List<RackResponse> racks = new ArrayList<RackResponse>();
		for(Rack rack : physicalInspection.getGoodReceiptNote().getStore().getRack())
		{
			List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>();
			for(Shelf shelf : rack.getShelfList())
			{
				List<PhyscicalInspectionItem> itemList = new ArrayList<PhyscicalInspectionItem>();
				for(ShelfItem shelfItem : shelf.getShelfItem())
				{
					//System.out.println(shelfItem.getBatch().getId());
					PhyscicalInspectionItem itemObject = new PhyscicalInspectionItem(shelfItem.getBatch().getItem().getCode(), shelfItem.getBatch().getItem().getName(),
							shelfItem.getBatch().getBatchNo());
					itemList.add(itemObject);
				}
				ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
				shelfs.add(shelfObject);
			}
			RackResponse rackObject = new RackResponse(rack.getId(), rack.getName(), shelfs);
			racks.add(rackObject);
		}
		List<RejectReasonResponse> rejectReasonList = new ArrayList<RejectReasonResponse>();
		for(RejectReason rejectReasons : GlobalDao.getRejectReasonList(physicalInspection.getUnit().getUnitId()))
		{
			RejectReasonResponse rejectReason = new RejectReasonResponse(rejectReasons.getId(), rejectReasons.getReason());
			rejectReasonList.add(rejectReason);
		}
	
		physicalInspectionDetails = new PhysicalInspectionDetails(physicalInspection.getId(), physicalInspection.getGoodReceiptNote().getGrnNumber(), physicalInspection.getAddedDate(),
				physicalInspection.getGoodReceiptNote().getInvoiceNumber(), physicalInspection.getGoodReceiptNote().getInvoiceDate(), physicalInspection.getGoodReceiptNote().getDcNumber(),  physicalInspection.getGoodReceiptNote().getDcDate(), 
				physicalInspection.getGoodReceiptNote().getRequestType().getId(), physicalInspection.getGoodReceiptNote().getRequestType().getType(), physicalInspection.getGoodReceiptNote().getStore().getStoreId(),
				physicalInspection.getGoodReceiptNote().getStore().getStoreName(), physicalInspection.getGoodReceiptNote().getVendor().getId(), physicalInspection.getGoodReceiptNote().getVendor().getName(),
				physicalInspection.getGoodReceiptNote().getAssetType().getId(), physicalInspection.getGoodReceiptNote().getAssetType().getType(), items, racks,rejectReasonList, physicalInspection.getAddedBy().getUserName(),
				physicalInspection.getGoodReceiptNote().getIsConsignment());
		physicalInspectionDetails.setStatusId(physicalInspection.getStatus().getId());
		physicalInspectionDetails.setStatus(physicalInspection.getStatus().getStatus());
		physicalInspectionDetails.setRemark(physicalInspection.getRemark());
		if(physicalInspection.getGoodReceiptNote().getDueDate() != null) {
			physicalInspectionDetails.setDueDate(GlobalCommonDateUtils.getAvalDate((physicalInspection.getGoodReceiptNote().getDueDate()), "dd/MM/yyyy"));
		}
		return physicalInspectionDetails;
	}
	
	
	/*added by Minesh */
	@Override
	@Transactional
	public ConsignmentStockConversionDetails getConsignmentStockConversionDetails(PhysicalInspectionRequest physicalInspectionRequest)
	{
		ConsignmentStockConversionDetails consignmentStockConversionDetails = new ConsignmentStockConversionDetails();
		PhysicalInspection physicalInspection = PhysicalInspectionDao.getPhysicalnspectionDetailsForSearch(physicalInspectionRequest);
		
		List<ConsignmentStockConversionItem> items = new ArrayList<ConsignmentStockConversionItem>();
		
		List<Batch> batchLists = new ArrayList<Batch>();
		
		ConsignmentStockConversionItem item = new ConsignmentStockConversionItem();
		for(PhysicalInspectionDetail physicalInspectionDetail : PhysicalInspectionDao.getPhysicalInspectionDetailByInspectionId(physicalInspection.getId(), physicalInspection.getUnit().getUnitId()))
		{
			if(physicalInspectionDetail.getPhysicalInspection().getGoodReceiptNote().getAssetType().getId().equals(Constants.getConsumableId())) {
			List<GrnUomMapperResponse> grnUomMapperResponse = new ArrayList<GrnUomMapperResponse>();
			for(GrnUomMapper grnUomMapper : physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomMappersList())
			{
				GrnUomMapperResponse grnUom = new GrnUomMapperResponse(grnUomMapper.getId(), grnUomMapper.getUomType().getId(), grnUomMapper.getUomUnit().getId(),
						grnUomMapper.getConversions(), grnUomMapper.getIpUom(), grnUomMapper.getOpUom(), grnUomMapper.getStoreUom());
				grnUomMapperResponse.add(grnUom);
			}
			List<GrnBonusUomMapperResponse> grnBonusUomMapperResponse = new ArrayList<GrnBonusUomMapperResponse>();
			for(GrnBonusUomMapper grnBonusUomMapper : physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnBonusUomMappersList())
			{
				GrnBonusUomMapperResponse grnBonusUomMappe = new GrnBonusUomMapperResponse(grnBonusUomMapper.getId(), grnBonusUomMapper.getUomType().getId(),
						grnBonusUomMapper.getUomUnit().getId(), grnBonusUomMapper.getConversions(), grnBonusUomMapper.getIpUom(), grnBonusUomMapper.getOpUom(),
						grnBonusUomMapper.getStoreUom());
				grnBonusUomMapperResponse.add(grnBonusUomMappe);
			}
			item = new ConsignmentStockConversionItem(physicalInspectionDetail.getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getId(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getCode(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getName(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnApprovedQuantity(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomType().getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomType().getType(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomUnit().getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnUomUnit().getUnits(), grnUomMapperResponse, 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity(), physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType().getId(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomType().getType(), physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit().getId(), 
					physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusUomUnit().getUnits(), grnBonusUomMapperResponse, 	physicalInspectionDetail.getGoodReceiptNoteDetail().getCop(),
				    physicalInspectionDetail.getGoodReceiptNoteDetail().getMrp(), physicalInspectionDetail.getGoodReceiptNoteDetail().getSpecification(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getRemark(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnReceivedQuantity(), physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusReceivedQuantity(),
					physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber(), physicalInspectionDetail.getGoodReceiptNoteDetail().getExpiryDate(), physicalInspectionDetail.getGoodReceiptNoteDetail().getManufacturer().getName());
		}
			else if(physicalInspectionDetail.getPhysicalInspection().getGoodReceiptNote().getAssetType().getId().equals(Constants.getFixedAssetId())){
				item = new ConsignmentStockConversionItem(physicalInspectionDetail.getId(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getId(), 
						physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getCode(), physicalInspectionDetail.getGoodReceiptNoteDetail().getItem().getName(), 
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBatchNo(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnApprovedQuantity(), 
						physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusApprovedQuantity(),physicalInspectionDetail.getGoodReceiptNoteDetail().getCop(),
					    physicalInspectionDetail.getGoodReceiptNoteDetail().getMrp(), physicalInspectionDetail.getGoodReceiptNoteDetail().getSpecification(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getRemark(), physicalInspectionDetail.getGoodReceiptNoteDetail().getGrnReceivedQuantity(), physicalInspectionDetail.getGoodReceiptNoteDetail().getBonusReceivedQuantity(),
						physicalInspectionDetail.getGoodReceiptNoteDetail().getLotNumber(), physicalInspectionDetail.getGoodReceiptNoteDetail().getExpiryDate(), physicalInspectionDetail.getGoodReceiptNoteDetail().getManufacturer().getName());
			}
			item.setApprovedQuantity(physicalInspectionDetail.getApprovedQuantity());
			item.setRejectQuantity(physicalInspectionDetail.getRejectQuantity());
			item.setStatusId(physicalInspectionDetail.getStatus().getId());
			item.setStatus(physicalInspectionDetail.getStatus().getStatus());		
			
			/*added by Minesh*/
			item.setBatchId(physicalInspectionDetail.getGoodReceiptNoteDetail().getBatch().getId());
			List<Batch> batchList = PhysicalInspectionDao.getBatchListFromItem(item.getItemId(), item.getBatchNo(), item.getLotNo(), physicalInspection.getUnitId());
			
			int totalConsignmentStock = 0;
			int availableConsignmentStock = 0;
			int convertedConsignmentStock = 0;
			int availableConvertedStock = 0;
			
			List<Long> batchIds = batchList.stream().map(x -> x.getId()).collect(Collectors.toList());
			List<UomResponse> uomListAll = new ArrayList<UomResponse>();
			if(!batchIds.isEmpty())
			{
				 uomListAll = GlobalDao.getBatchUomListBP(batchIds);
			}
			for ( Batch batch : batchList) {
				List<UomResponse> uomList = uomListAll.stream().filter(x -> x.getBatchId().equals(batch.getId())).collect(Collectors.toList());
				if (uomList != null && !uomList.isEmpty()) {
					UomResponse ipUom = Util.getIpUom(uomList);
					if (ipUom != null) {
						Integer conversion = Util.getIpUomConversion(uomList);
						totalConsignmentStock += batch.getQuantity() / conversion;						
						if(!batch.getIsConsignment()) {
							convertedConsignmentStock += batch.getQuantity() / conversion;
							availableConvertedStock += batch.getCurrentQuantity() / conversion;
						}else {
							availableConsignmentStock += batch.getCurrentQuantity() / conversion;
						}
					}
					
				}
			}
			
			item.setTotalConsignmentStock(totalConsignmentStock);
			item.setAvailableConsignmentStock(availableConsignmentStock);
			item.setConvertedConsignmentStock(BigInteger.valueOf(convertedConsignmentStock));
			item.setConversionQuantity(null);
			item.setAvailableConvertedStock(availableConvertedStock);
			item.setBatch(batchList);
			
			//batchLists.addAll(new ArrayList<>(batchList));
			/*end added by Minesh*/
			
			if(physicalInspectionDetail.getGoodReceiptNoteDetail().getPurchaseOrderDetail()!=null)
			{
				item.setReceivedItemSpecification(physicalInspectionDetail.getGoodReceiptNoteDetail().getPurchaseOrderDetail().getSpecification());
			}
			items.add(item);
			
		}
		List<RackResponse> racks = new ArrayList<RackResponse>();
		/*for(Rack rack : physicalInspection.getGoodReceiptNote().getStore().getRack())
		{
			List<ShelfResponse> shelfs = new ArrayList<ShelfResponse>();
			for(Shelf shelf : rack.getShelfList())
			{
				List<PhyscicalInspectionItem> itemList = new ArrayList<PhyscicalInspectionItem>();
				for(ShelfItem shelfItem : shelf.getShelfItem())
				{
					//System.out.println(shelfItem.getBatch().getId());
					PhyscicalInspectionItem itemObject = new PhyscicalInspectionItem(shelfItem.getBatch().getItem().getCode(), shelfItem.getBatch().getItem().getName(),
							shelfItem.getBatch().getBatchNo());
					itemList.add(itemObject);
				}
				ShelfResponse shelfObject = new ShelfResponse(shelf.getId(), shelf.getName(), itemList);
				shelfs.add(shelfObject);
			}
			RackResponse rackObject = new RackResponse(rack.getId(), rack.getName(), shelfs);
			racks.add(rackObject);
		}*/
		List<RejectReasonResponse> rejectReasonList = new ArrayList<RejectReasonResponse>();
		/*for(RejectReason rejectReasons : GlobalDao.getRejectReasonList())
		{
			RejectReasonResponse rejectReason = new RejectReasonResponse(rejectReasons.getId(), rejectReasons.getReason());
			rejectReasonList.add(rejectReason);
		}*/
	
		consignmentStockConversionDetails = new ConsignmentStockConversionDetails(physicalInspection.getId(), physicalInspection.getGoodReceiptNote().getGrnNumber(), physicalInspection.getAddedDate(),
				physicalInspection.getGoodReceiptNote().getInvoiceNumber(), physicalInspection.getGoodReceiptNote().getInvoiceDate(), physicalInspection.getGoodReceiptNote().getDcNumber(),  physicalInspection.getGoodReceiptNote().getDcDate(), 
				physicalInspection.getGoodReceiptNote().getRequestType().getId(), physicalInspection.getGoodReceiptNote().getRequestType().getType(), physicalInspection.getGoodReceiptNote().getStore().getStoreId(),
				physicalInspection.getGoodReceiptNote().getStore().getStoreName(), physicalInspection.getGoodReceiptNote().getVendor().getId(), physicalInspection.getGoodReceiptNote().getVendor().getName(),
				physicalInspection.getGoodReceiptNote().getAssetType().getId(), physicalInspection.getGoodReceiptNote().getAssetType().getType(), items, racks,rejectReasonList, physicalInspection.getAddedBy().getUserName(),
				physicalInspection.getGoodReceiptNote().getIsConsignment());
		consignmentStockConversionDetails.setStatusId(physicalInspection.getStatus().getId());
		consignmentStockConversionDetails.setStatus(physicalInspection.getStatus().getStatus());
		//consignmentStockConversionDetails.setBatch(batchLists);
		
		return consignmentStockConversionDetails;
	}
	
	
	@Override
	@Transactional
	public Object savePhysicalnspection(PhysicalInspectionSave physicalInspectionSave)
	{
		PhysicalInspection physicalInspection = PhysicalInspectionDao.getPhysicalnspectionDetailsForSearch(physicalInspectionSave);

		for (PhysicalInspectionSaveItem item : physicalInspectionSave.getItems()) {
			Integer mainLineApprovedQty=item.getApprovedQuantity();
			PhysicalInspectionDetail physicalInspectionDetail = PhysicalInspectionDao.getPhysicalInspectionDetailById(item.getId(),physicalInspectionSave.getUnitId());
			GoodReceiptNoteDetail goodReceiptNoteDetail2 = physicalInspectionDetail.getGoodReceiptNoteDetail();
			if(physicalInspectionSave.getAutoPhysicalInspectionSave()==null)
			{
				physicalInspectionSave.setAutoPhysicalInspectionSave(false);
			}
			if(physicalInspectionSave.getAutoPhysicalInspectionSave().equals(true))
			{
			List<GrnBonusBatchExpiry> getList= BonusBatchExpiryDao.getGrnBonusBatchExpiryByGrnDetailId(goodReceiptNoteDetail2.getId());
            List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
            for (GrnBonusBatchExpiry getList1 : getList) {
                GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
                getList2.setBatchNo(getList1.getBatchNo());
                getList2.setBonusQuantity(getList1.getBonusQuantity());
                getList2.setExpiryDate(getList1.getExpiryDate());
                getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
                getList3.add(getList2);
            }
            item.setGrnBonusBatchExpiry(getList3);
			}
			List<Batch> batchIds=PhysicalInspectionDao.getBatchIds(goodReceiptNoteDetail2.getId());
			if(batchIds.size()>0)
			{
                if(item.getGrnBonusBatchExpiry().size()>0)
                {
                    for (GrnBonusBatchExpiry bonusBatches : item.getGrnBonusBatchExpiry()) {
				Boolean checkAmount1 = false;
				Integer approvedQty1 = 0;
                item.setApprovedQuantity(0);
                item.setBonusApprovedQuantity(bonusBatches.getBonusQuantity());
				try {
					
					if(item.getApprovedQuantity() != null)
					{
						approvedQty1 = item.getApprovedQuantity();
						checkAmount1 = true;
					}

				} catch (Exception e) {
					logger.error("savePhysicalnspection failed", e);
					throw new ServiceException(e.getMessage());
				}
				if (checkAmount1 == true) { 
					
					physicalInspectionDetail.setApprovedQuantity(approvedQty1);
					
				}else {
					
					approvedQty1 = PhysicalInspectionDao.getGrnApproveedQty(item.getId());
					physicalInspectionDetail.setApprovedQuantity(approvedQty1);
					
				}
				
			if(item.getRejectQuantity() != null)
			{
				physicalInspectionDetail.setRejectQuantity(item.getRejectQuantity());
				physicalInspectionDetail.setRejectReason(new RejectReason(item.getRejectReasonId()));
				physicalInspectionDetail.setRejectNote(item.getRejectNote());
			}
			physicalInspectionDetail.setApprovedBonusQuantity(item.getBonusApprovedQuantity());
			physicalInspectionDetail.setLcop(item.getLcop());
			physicalInspectionDetail.setStatus(new Status(item.getStatusId()));
			physicalInspectionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(physicalInspectionSave.getUnitId()));
			physicalInspectionDetail.setUpdatedByUser(physicalInspectionSave.getUserId());
			
			GoodReceiptNoteDetail goodReceiptNoteDetail1 = physicalInspectionDetail.getGoodReceiptNoteDetail();
			
			//sessionService.setEntityDefaults(goodReceiptNoteDetail);
			Integer batchId1=GlobalDao.getBonusBatchId(bonusBatches.getGrnBonusBatchExpiryId());
			//Batch b=goodReceiptNoteDetail.getBatch();
			Batch b1=GlobalDao.getBatchById(batchId1.longValue());
			bonusBatches.setBatchId(batchId1.longValue());
			//Batch batch = goodReceiptNoteDetail.getBatch();
			//sessionService.setEntityDefaults(batch);
			if(item.getStorageDetails()!=null)
			{
				for(StorageDetails storageDetails : item.getStorageDetails())
				{
					ShelfItem shelfItem = new ShelfItem(new Shelf(storageDetails.getShelfId()), new Batch(batchId1.longValue()), 
									new Store(goodReceiptNoteDetail1.getGoodReceiptNote().getStore().getStoreId()),
									new Rack(storageDetails.getRackId()), storageDetails.getRemark());
						//sessionService.setEntityDefaults(shelfItem);
					
						shelfItem.setAddedByUser(physicalInspectionSave.getUserId());
						shelfItem.setUpdatedByUser(physicalInspectionSave.getUserId());
						shelfItem.setAddedDate(TimeZoneComponent.getDateByzone(physicalInspection.getUnitId()));
						shelfItem.setUpdatedDate(TimeZoneComponent.getDateByzone(physicalInspection.getUnitId()));
						shelfItem.setOrganizationId(physicalInspectionSave.getOrganizationId());
						shelfItem.setUnitId(physicalInspectionSave.getUnitId());
						shelfItem.setOrganization(new Organization(physicalInspectionSave.getOrganizationId()));
						shelfItem.setUnit(new Unit(physicalInspectionSave.getUnitId()));
					
					
					PhysicalInspectionDao.saveShelfItem(shelfItem);
				}	
			}
			
			
			Integer leaseQuantity1 = item.getApprovedQuantity();
			Integer bonusLeaseQuantity1 = 0;
			if(item.getBonusApprovedQuantity()!=null && item.getBonusApprovedQuantity() > 0)
			{
			  bonusLeaseQuantity1 = item.getBonusApprovedQuantity();
			}
			
			Integer totalAvailableQty1 = 0;
			
			Integer rejectedQuantity1 = 0;
			if(physicalInspection.getGoodReceiptNote().getAssetType().getId().equals(Constants.getConsumableId())) {
				/*Integer batchId=GlobalDao.getBatchId(goodReceiptNoteDetail.getId());
				Batch b1=GlobalDao.getBatchById(batchId.longValue());*/
				leaseQuantity1 = Util.GetLeaseQuantity(leaseQuantity1, goodReceiptNoteDetail1.getGrnBonusUomMappersList(), goodReceiptNoteDetail1.getGrnUomType());
				if( goodReceiptNoteDetail1.getBonusUomType()!=null) {
					bonusLeaseQuantity1 = Util.GetLeaseQuantity(bonusLeaseQuantity1, goodReceiptNoteDetail1.getGrnBonusUomMappersList(), goodReceiptNoteDetail1.getBonusUomType());
					
				}
				
				rejectedQuantity1 = Util.GetLeaseQuantity(rejectedQuantity1, goodReceiptNoteDetail1.getGrnBonusUomMappersList(), goodReceiptNoteDetail1.getGrnUomType());
			}
			if(b1.getIsQuarantine().equals(true))
			{
				Integer qty=((b1.getQuarantineQuantity()!=null ? b1.getQuarantineQuantity() : 0)-leaseQuantity1)-bonusLeaseQuantity1;
				b1.setQuarantineQuantity(qty <= 0 ? 0 : qty);
			//	b1.setLcop(item.getLcop()!=null ? item.getLcop() :goodReceiptNoteDetail.getLcop()!=null ? goodReceiptNoteDetail.getLcop():0 );
				b1.setQuantity((leaseQuantity1 +bonusLeaseQuantity1));
				//batch.setCurrentQuantity(leaseQuantity+rejectedQuantity);
				b1.setCurrentQuantity((leaseQuantity1+bonusLeaseQuantity1));
				
			   for(StoreStock s : b1.getStoreStockList() )
			   {
				   
				   if(s.getStore().getStoreId().equals(physicalInspection.getGoodReceiptNote().getStore().getStoreId()))
				   {
					    s.setAvailableQty((leaseQuantity1 + bonusLeaseQuantity1));
					    totalAvailableQty1 = (leaseQuantity1 + bonusLeaseQuantity1);
						s.setTotalQty((leaseQuantity1 + bonusLeaseQuantity1) ) ;
						b1.setRejectedQuantity(rejectedQuantity1);
						b1.setQuarantine(false);	  
						break;
				   }
				   
				   
			   }
				
					
			}
			else
			{
				Integer qty=((b1.getQuarantineQuantity()!=null ? b1.getQuarantineQuantity() : 0)-leaseQuantity1)-bonusLeaseQuantity1;
				b1.setQuarantineQuantity(qty <= 0 ? 0 : qty);
			//	b1.setLcop(item.getLcop()!=null ? item.getLcop() :goodReceiptNoteDetail.getLcop()!=null ? goodReceiptNoteDetail.getLcop():0 );
				b1.setQuantity( b1.getQuantity() +(leaseQuantity1 +bonusLeaseQuantity1));
				//batch.setCurrentQuantity(leaseQuantity+rejectedQuantity);
				b1.setCurrentQuantity(b1.getCurrentQuantity()+(leaseQuantity1+bonusLeaseQuantity1));
				
				  for(StoreStock s : b1.getStoreStockList() )
				   {
					  if(s.getStore().getStoreId().equals(physicalInspection.getGoodReceiptNote().getStore().getStoreId()))
					   {
						    s.setAvailableQty(s.getAvailableQty() +(leaseQuantity1 + bonusLeaseQuantity1));
						    totalAvailableQty1 = s.getAvailableQty();
						    s.setTotalQty(s.getTotalQty()+(leaseQuantity1 + bonusLeaseQuantity1) ) ;
							b1.setRejectedQuantity(rejectedQuantity1);
							b1.setQuarantine(false);	 
							break;
					   }
					  
				   }
				
				
			}
			com.param.entity.model.master.Screen screen1 = GlobalDao.getScreen(screenId);
			PhysicalInspectionDao.updateBatchQuantityAndFlag(b1);
			 Boolean intrasit1 = false;
			if(rejectedQuantity1 != null && rejectedQuantity1>0) {
				intrasit1 = true;
			}else {
				intrasit1 = false;
			}
			
			PhysicalInspectionDao.updateStockTransactionForBonusBatch(goodReceiptNoteDetail1.getId(),totalAvailableQty1,(leaseQuantity1+bonusLeaseQuantity1),screen1.getId(),screen1.getCode(),intrasit1,rejectedQuantity1,batchId1);
			}
		}
			}
			if(batchIds.size()>0)
			{
				item.setBonusApprovedQuantity(0);
				item.setApprovedQuantity(mainLineApprovedQty);
			}
				Boolean checkAmount = false;
				Integer approvedQty = 0;
				try {
					
					if(item.getApprovedQuantity() != null)
					{
						approvedQty = item.getApprovedQuantity();
						checkAmount = true;
					}

				} catch (Exception e) {
					logger.error("savePhysicalnspection failed", e);
					throw new ServiceException(e.getMessage());
				}
				if (checkAmount == true) { 
					
					physicalInspectionDetail.setApprovedQuantity(approvedQty);
					
				}else {
					
					approvedQty = PhysicalInspectionDao.getGrnApproveedQty(item.getId());
					physicalInspectionDetail.setApprovedQuantity(approvedQty);
					
				}
				
			if(item.getRejectQuantity() != null)
			{
				physicalInspectionDetail.setRejectQuantity(item.getRejectQuantity());
				physicalInspectionDetail.setRejectReason(new RejectReason(item.getRejectReasonId()));
				physicalInspectionDetail.setRejectNote(item.getRejectNote());
			}
			physicalInspectionDetail.setApprovedBonusQuantity(item.getBonusApprovedQuantity());
			physicalInspectionDetail.setLcop(item.getLcop());
			physicalInspectionDetail.setStatus(new Status(item.getStatusId()));
			physicalInspectionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(physicalInspectionSave.getUnitId()));
			physicalInspectionDetail.setUpdatedByUser(physicalInspectionSave.getUserId());
			
			GoodReceiptNoteDetail goodReceiptNoteDetail = physicalInspectionDetail.getGoodReceiptNoteDetail();
			
			//sessionService.setEntityDefaults(goodReceiptNoteDetail);
			Integer batchId=GlobalDao.getBatchId(goodReceiptNoteDetail.getId());
			//Batch b=goodReceiptNoteDetail.getBatch();
			Batch b=GlobalDao.getBatchById(batchId.longValue());
			//Batch batch = goodReceiptNoteDetail.getBatch();
			//sessionService.setEntityDefaults(batch);
			if(item.getStorageDetails()!=null)
			{
				for(StorageDetails storageDetails : item.getStorageDetails())
				{
					ShelfItem shelfItem = new ShelfItem(new Shelf(storageDetails.getShelfId()), new Batch(batchId.longValue()), 
									new Store(goodReceiptNoteDetail.getGoodReceiptNote().getStore().getStoreId()),
									new Rack(storageDetails.getRackId()), storageDetails.getRemark());
						//sessionService.setEntityDefaults(shelfItem);
					
						shelfItem.setAddedByUser(physicalInspectionSave.getUserId());
						shelfItem.setUpdatedByUser(physicalInspectionSave.getUserId());
						shelfItem.setAddedDate(TimeZoneComponent.getDateByzone(physicalInspection.getUnitId()));
						shelfItem.setUpdatedDate(TimeZoneComponent.getDateByzone(physicalInspection.getUnitId()));
						shelfItem.setOrganizationId(physicalInspectionSave.getOrganizationId());
						shelfItem.setUnitId(physicalInspectionSave.getUnitId());
						shelfItem.setOrganization(new Organization(physicalInspectionSave.getOrganizationId()));
						shelfItem.setUnit(new Unit(physicalInspectionSave.getUnitId()));
					
					
					PhysicalInspectionDao.saveShelfItem(shelfItem);
				}	
			}
			
			
			Integer leaseQuantity = item.getApprovedQuantity();
			Integer bonusLeaseQuantity = 0;
			if(item.getBonusApprovedQuantity()!=null && item.getBonusApprovedQuantity() > 0)
			{
			  bonusLeaseQuantity = item.getBonusApprovedQuantity();
			}
			
			Integer totalAvailableQty = 0;
			
			Integer rejectedQuantity = item.getRejectQuantity() != null ? item.getRejectQuantity() : 0;
			if(physicalInspection.getGoodReceiptNote().getAssetType().getId().equals(Constants.getConsumableId())) {
				/*Integer batchId=GlobalDao.getBatchId(goodReceiptNoteDetail.getId());
				Batch b=GlobalDao.getBatchById(batchId.longValue());*/
				leaseQuantity = Util.GetLeaseQuantity(leaseQuantity, goodReceiptNoteDetail.getGrnUomMappersList(), goodReceiptNoteDetail.getGrnUomType());
				if( goodReceiptNoteDetail.getBonusUomType()!=null) {
					bonusLeaseQuantity = Util.GetLeaseQuantity(bonusLeaseQuantity, goodReceiptNoteDetail.getGrnBonusUomMappersList(), goodReceiptNoteDetail.getBonusUomType());
					
				}
				
				rejectedQuantity = Util.GetLeaseQuantity(rejectedQuantity, goodReceiptNoteDetail.getGrnUomMappersList(), goodReceiptNoteDetail.getGrnUomType());
			}
			if(b.getIsQuarantine().equals(true))
			{
				Integer qty=((b.getQuarantineQuantity()!=null ? b.getQuarantineQuantity() : 0)-leaseQuantity)-bonusLeaseQuantity;
				b.setQuarantineQuantity(qty <= 0 ? 0 : qty);
			//	b.setLcop(item.getLcop()!=null ? item.getLcop() :goodReceiptNoteDetail.getLcop()!=null ? goodReceiptNoteDetail.getLcop():0 );
				b.setQuantity((leaseQuantity +bonusLeaseQuantity));
				//batch.setCurrentQuantity(leaseQuantity+rejectedQuantity);
				b.setCurrentQuantity((leaseQuantity+bonusLeaseQuantity));
				
			   for(StoreStock s : b.getStoreStockList() )
			   {
				   
				   if(s.getStore().getStoreId().equals(physicalInspection.getGoodReceiptNote().getStore().getStoreId()))
				   {
					    s.setAvailableQty((leaseQuantity + bonusLeaseQuantity));
					    totalAvailableQty = (leaseQuantity + bonusLeaseQuantity);
						s.setTotalQty((leaseQuantity + bonusLeaseQuantity) ) ;
						b.setRejectedQuantity(rejectedQuantity);
						b.setQuarantine(false);	  
						break;
				   }
				   
				   
			   }
				
					
			}
			else
			{
				Integer qty=((b.getQuarantineQuantity()!=null ? b.getQuarantineQuantity() : 0)-leaseQuantity)-bonusLeaseQuantity;
				b.setQuarantineQuantity(qty <= 0 ? 0 : qty);
			//	b.setLcop(item.getLcop()!=null ? item.getLcop() :goodReceiptNoteDetail.getLcop()!=null ? goodReceiptNoteDetail.getLcop():0 );
				b.setQuantity( b.getQuantity() +(leaseQuantity +bonusLeaseQuantity));
				//batch.setCurrentQuantity(leaseQuantity+rejectedQuantity);
				b.setCurrentQuantity(b.getCurrentQuantity()+(leaseQuantity+bonusLeaseQuantity));
				
				  for(StoreStock s : b.getStoreStockList() )
				   {
					  if(s.getStore().getStoreId().equals(physicalInspection.getGoodReceiptNote().getStore().getStoreId()))
					   {
						    s.setAvailableQty(s.getAvailableQty() +(leaseQuantity + bonusLeaseQuantity));
						    totalAvailableQty = s.getAvailableQty();
						    s.setTotalQty(s.getTotalQty()+(leaseQuantity + bonusLeaseQuantity) ) ;
							b.setRejectedQuantity(rejectedQuantity);
							b.setQuarantine(false);	 
							break;
					   }
					  
				   }
				
				
			}
			com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
			PhysicalInspectionDao.updateBatchQuantityAndFlag(b);
			Boolean intrasit = false;
			if(rejectedQuantity != null && rejectedQuantity>0) {
				intrasit = true;
			}else {
				intrasit = false;
			}
			
			PhysicalInspectionDao.updateStockTransaction(goodReceiptNoteDetail.getId(),totalAvailableQty,(leaseQuantity+bonusLeaseQuantity),screen.getId(),screen.getCode(),intrasit,rejectedQuantity,physicalInspectionSave.getAutoPhysicalInspectionSave());
            Integer grnBonusApprovedQty=0;
            Integer grnBonusReceivedQty=0;
			if(item.getGrnBonusBatchExpiry().size()>0)
            {
                for (GrnBonusBatchExpiry bonusBatches : item.getGrnBonusBatchExpiry()) {
                    grnBonusApprovedQty=grnBonusApprovedQty+bonusBatches.getBonusQuantity();
                    grnBonusReceivedQty=grnBonusReceivedQty+bonusBatches.getBonusQuantity();
                GrnBonusBatchExpiry grnBatchExpiry1 = new GrnBonusBatchExpiry();
                grnBatchExpiry1.setBatchId(bonusBatches.getBatchId());
                grnBatchExpiry1.setBatchNo(bonusBatches.getBatchNo());
                grnBatchExpiry1.setExpiryDate(bonusBatches.getExpiryDate());
                grnBatchExpiry1.setBonusQuantity(bonusBatches.getBonusQuantity());
                grnBatchExpiry1.setGrnDetailId(goodReceiptNoteDetail.getId());
                grnBatchExpiry1.setOrganizationId(physicalInspectionSave.getOrganizationId());
                grnBatchExpiry1.setUnitId(physicalInspectionSave.getUnitId());
                grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(physicalInspectionSave.getUnitId()));
                grnBatchExpiry1.setUpdatedByUser(physicalInspectionSave.getUserId()!=null ? physicalInspectionSave.getUserId() : sessionService.getUserId());
                grnBatchExpiry1.setAddedByUser(physicalInspectionSave.getUserId()!=null ? physicalInspectionSave.getUserId() : sessionService.getUserId());
                grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(physicalInspectionSave.getUnitId()));
                grnBatchExpiry1.setGrnBonusBatchExpiryId(bonusBatches.getGrnBonusBatchExpiryId());
                if(physicalInspectionSave.getAutoPhysicalInspectionSave().equals(false))
                {
                BonusBatchExpiryDao.updateBonusBatchExpiryApproval(grnBatchExpiry1);
                }
                }
                
            }
			if(item.getGrnBonusBatchExpiry().size()>0)
            {
			physicalInspectionDetail.setApprovedBonusQuantity(grnBonusApprovedQty);
            }
		}
		
		physicalInspection.setStatus(new Status(physicalInspectionSave.getStatusId()));
		physicalInspection.setRemark(physicalInspectionSave.getRemark());
		physicalInspection.setUpdatedDate(TimeZoneComponent.getDateByzone(physicalInspection.getUnitId()));
		physicalInspection.setOrganizationId(physicalInspectionSave.getOrganizationId());
		physicalInspection.setUnitId(physicalInspectionSave.getUnitId());
		physicalInspection.setUpdatedByUser(physicalInspectionSave.getUserId());
		
		PhysicalInspectionDao.savePhysicalnspection(physicalInspection);
		updateGoodReceiptNoteClosedStatus(physicalInspectionSave);
	
		CommonResponse response = new CommonResponse();
		response.setId(physicalInspection.getId().longValue());
		response.setMessage("Physical Inspection saved successfully");

		return response;
	}
	
	private void updateGoodReceiptNoteClosedStatus(PhysicalInspectionSave physicalInspectionSave) {
		PhysicalInspection physicalInspection = PhysicalInspectionDao.getPhysicalnspectionDetailsForSearch(physicalInspectionSave);
		GoodReceiptNote goodReceiptNote = physicalInspection.getGoodReceiptNote();
		goodReceiptNote.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		
		//added by minesh for conversion status maintain
		if(goodReceiptNote.getIsConsignment()) {
			goodReceiptNote.setConversionStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
			}
		// end added by minesh for conversion status maintain
		
		for (PhysicalInspectionSaveItem item : physicalInspectionSave.getItems()) {
				PhysicalInspectionDetail physicalInspectionDetail = PhysicalInspectionDao.getPhysicalInspectionDetailById(item.getId(),physicalInspectionSave.getUnitId());
				GoodReceiptNoteDetail goodReceiptNoteDetail = physicalInspectionDetail.getGoodReceiptNoteDetail();
				
				/*if(goodReceiptNoteDetail.getGrnApprovedQuantity().equals(item.getApprovedQuantity()))
				{
					goodReceiptNoteDetail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));	
				}
				else
				{*/
					goodReceiptNoteDetail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
				//}
				
				//sessionService.setEntityDefaults(goodReceiptNoteDetail);	
				goodReceiptNote.addGoodReceiptNoteDetailList(goodReceiptNoteDetail);
		}
		//sessionService.setEntityDefaults(goodReceiptNote);	
		grnDao.updateGoodReceiptNote(goodReceiptNote);
		
		//Check if GRN is from PO. If from PO close PO
		if(goodReceiptNote.getPurchaseOrder() != null)
			updatePurchaseOrderAndRequestClosedStatus(goodReceiptNote.getPurchaseOrder(), physicalInspectionSave);
	}
	
	private void updatePurchaseOrderAndRequestClosedStatus(PurchaseOrder po, PhysicalInspectionSave physicalInspectionSave) {
		PurchaseRequest pr = null;
		Integer poId=po.getId();
		List<Integer> poDetailsId = new ArrayList<>();
		Integer statusId=8;	
		//List<PurchaseOrderDetail> poDetailData = po.getPurchaseOrderDetailList();
		for (PurchaseOrderDetail poDetails : po.getPurchaseOrderDetailList() )
		{
			poDetailsId.add(poDetails.getId());
		}
		Integer poSize=poDetailsId.size();
		Integer grnSize=PhysicalInspectionDao.getGrnSize(poId,poDetailsId);
		if(poSize.equals(grnSize))
		{
			 statusId=8;	
		}
		else
		{
			 statusId=18;
		}
		
		for (PhysicalInspectionSaveItem item : physicalInspectionSave.getItems()) {
			PhysicalInspectionDetail physicalInspectionDetail = PhysicalInspectionDao.getPhysicalInspectionDetailById(item.getId(),physicalInspectionSave.getUnitId());
			GoodReceiptNoteDetail goodReceiptNoteDetail = physicalInspectionDetail.getGoodReceiptNoteDetail();
			PurchaseOrderDetail pod = goodReceiptNoteDetail.getPurchaseOrderDetail();
			//PurchaseOrderDetail podetails=PhysicalInspectionDao.getPoDetails(pod.getId());
			PurchaseOrderDetail podetails =  physicalInspectionDetail.getGoodReceiptNoteDetail().getPurchaseOrderDetail();
		//	PurchaseOrderDetail podetails =  purchaseOrderDao.getPurchaseOrderDetailById(physicalInspectionDetail.getGoodReceiptNoteDetail().getId());
			List<Integer> poDetailIds = new ArrayList();
			poDetailIds.add(podetails.getId());
			List<PurchaseOrderStagedDcDate> grnStagedLists = GlobalDao.getPurchaseOrderStagedListDcDateBP(poDetailIds);
			Integer receivedQty=0;
			
			for(PurchaseOrderStagedDcDate g :grnStagedLists)
			{
				receivedQty= receivedQty+g.getStagedReceivedQuantity();
				
			}
			
			/*if()
			{
				
			}
			else
			{
				
			}*/
			//System.out.println(pod.toString());
			
			if(pod != null)
			{
				//receivedQty=receivedQty+item.getApprovedQuantity();
				if(podetails.getPoApprovedQuantity().equals(receivedQty))
				{
					pod.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
					po.setStatus(new Status(statusId));
				}
				else
				{
					pod.setStatus(new Status(com.param.common.constants.Status.Approved.getStatus()));
					po.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
				}
				
				
				
				PurchaseRequestDetail prd = pod.getPutchRequestDetail();
				if(prd != null)
				{
					pr = prd.getPurchaseRequest();
					if(pr != null)
						pr.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
					
					//if(prd.getApprovedQuantity().equals(item.getApprovedQuantity()))
					//{
						prd.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
						//pr.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
					//}
					//else
					//{
						//prd.setStatus(new Status(com.param.common.constants.Status.Approved.getStatus()));	
						//pr.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
					//}
					
					
				}
			}
		}
	}
	
	@Override
	@Transactional
	public PhysicalInspectionDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId)
	{
		try {
			PhysicalInspectionDropdown response = new PhysicalInspectionDropdown();

			for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
				response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
			}

			for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
				response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
			}

			for (AssetType assetType : GlobalDao.getAssetTypeList(0, 0)) {
				response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(),
						assetType.getType()));
			}
			return response;
		}catch (Exception e){
			logger.error("getDropdownList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}