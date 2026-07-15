package com.param.procurement.quotation.service.quotationRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestAccessoriesMapper;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.quotation.dao.quotationRequest.IQuotationRequestDao;
import com.param.procurement.quotation.dto.quotationRequest.PurchaseRequestDto;
import com.param.procurement.quotation.dto.quotationRequest.QRAccessoriesMapper;
import com.param.procurement.quotation.dto.quotationRequest.QuotationReqDropDownDto;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDetailDto;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDetailsResponse;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDto;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestResponse;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestSearchResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class QuotationRequestService implements IQuotationRequestService {

	private Integer screenId = Screen.QuotationEnquiry.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;


	@Autowired
	IQuotationRequestDao quotationRequestDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public QuotationReqDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		QuotationReqDropDownDto quotationDropDownDto = new QuotationReqDropDownDto();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			quotationDropDownDto
					.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			quotationDropDownDto.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0,0)) {
			quotationDropDownDto
					.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return quotationDropDownDto;
	}

	@Override
	@Transactional
	public Object getQuotationRequestList(QuotationRequestDto quotationRequestDto) {
		quotationRequestDto.setToDate((new DateTime(quotationRequestDto.getToDate())).plusDays(1).toDate());

		List<QuotationRequestSearchResponse> quotationReqSearchList = new ArrayList<QuotationRequestSearchResponse>();

		for (QuotationRequest quotationRequest : quotationRequestDao.searchQuotationRequestList(quotationRequestDto,
				screenId)) {

			QuotationRequestSearchResponse quotationReqSearch = new QuotationRequestSearchResponse(
					quotationRequest.getId(), quotationRequest.getQrNo(), quotationRequest.getAddedDate(),
					quotationRequest.getStore().getStoreName(), quotationRequest.getAssetType().getType(),
					quotationRequest.getRequestType().getType(), quotationRequest.getRemark(),
					quotationRequest.getAddedBy().getUserName(), quotationRequest.getStatus().getStatus(),
					quotationRequest.getApprovalStatus().getStatus(), quotationRequest.getSendTechnicalApproval(),
					quotationRequest.getSendVendorSelection(), quotationRequest.getRequestForTechnicalApproval());

			quotationReqSearchList.add(quotationReqSearch);

		}
		return quotationReqSearchList;
	}

	@Override
	@Transactional
	public Object saveQuotationRequest(QuotationRequestDto quotationRequestDto) {

		/*Random rand = new Random();
		int num = rand.nextInt(1000);
		String qrNo = "QR-" + num;
		*/
		
		/*Integer storeId = quotationRequestDto.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String qrNo = GlobalService.getBillNoByGeneratedCode(quotationRequestDto.getStoreId(), quotationRequestDto.getUnitId()!=null ? quotationRequestDto.getUnitId(): sessionService.getUnitId(), screenId);
		
		HashMap<Integer, PurchaseRequestDetail> hm = new HashMap();

		QuotationRequest quotation = new QuotationRequest(new Status(quotationRequestDto.getStatusId()),
				new Store(quotationRequestDto.getStoreId()), new Status(quotationRequestDto.getApprovalStatusId()),
				new AssetType(quotationRequestDto.getAssetTypeId()),
				new RequestType(quotationRequestDto.getRequestTypeId()), qrNo,
				quotationRequestDto.getRemark() != null ? quotationRequestDto.getRemark() : null, false, false);
		quotation.setCreateDefaults(quotationRequestDto.getOrganizationId()!=null ? quotationRequestDto.getOrganizationId() : sessionService.getOrganizationId(),quotationRequestDto.getUnitId() !=null ? quotationRequestDto.getUnitId() :sessionService.getUnitId() ,quotationRequestDto.getUserId()!=null ? quotationRequestDto.getUserId() : sessionService.getUserId());
		for (QuotationRequestDetailDto item : quotationRequestDto.getItems()) {
			
			if (isQuantityValidated(quotationRequestDto.getAssetTypeId(), quotationRequestDto.getStoreId(), item.getItemId(),
					item.getQuantity(), item.getUomTypeId(), 0,quotationRequestDto.getOrganizationId(),quotationRequestDto.getUnitId())) {
			
			QuotationRequestDetail qrDetail = null;
			
			//consumable
			if (quotationRequestDto.getAssetTypeId().equals(Constants.getConsumableId())) {
				
				Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(), getUom(item.getItemId(),quotationRequestDto.getOrganizationId()),
						new UomType(item.getUomTypeId()));
			qrDetail = new QuotationRequestDetail(new Item(item.getItemId()), item.getQuantity(),
					new Status(item.getStatusId()), new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
					item.getWarrantyPeriod(), item.getPaymentTerms(), item.getSupportTerms(),
					item.getTechnicalSpecification(),leaseQuantity,item.getQuantity());
			qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :null);
			}
			
			//fixed
			else if (quotationRequestDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				qrDetail = new QuotationRequestDetail(new Item(item.getItemId()), item.getQuantity(),
						new Status(item.getStatusId()),
						item.getWarrantyPeriod(), item.getPaymentTerms(), item.getSupportTerms(),
						item.getTechnicalSpecification(),item.getQuantity());
				qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :null);
			}
			if (quotationRequestDto.getRequestTypeId() == com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType() && item.getPrIds() != null) {
			for(PurchaseRequestDto purchaseRequestDto : item.getPrIds()) {
				List<PurchaseRequestDetail> purchaseRequestDetails = quotationRequestDao.getPurchaseRequestDetails(purchaseRequestDto.getPrDetailId()); 
				for (PurchaseRequestDetail prDetail :purchaseRequestDetails) {
					//qrDetail.addPurchaseRequestDetailList(prDetail);
					hm.put(prDetail.getId(), prDetail);	
					qrDetail.addPurchaseRequestDetailList(prDetail);
				    }
				}
			}
			if (item.getExpectedAccessories() != null) {
				for (QRAccessoriesMapper qraccess : item.getExpectedAccessories()) {
					QuotationRequestAccessoriesMapper quotationRequestAccessoriesMapper=new QuotationRequestAccessoriesMapper(qraccess.getDetails(), qraccess.getName());
					quotationRequestAccessoriesMapper.setCreateDefaults(quotationRequestDto.getOrganizationId()!=null ? quotationRequestDto.getOrganizationId() : sessionService.getOrganizationId(),quotationRequestDto.getUnitId() !=null ? quotationRequestDto.getUnitId() :sessionService.getUnitId() ,quotationRequestDto.getUserId()!=null ? quotationRequestDto.getUserId() : sessionService.getUserId());
					
					qrDetail.addQuotationRequestAccessoriesMapperList(quotationRequestAccessoriesMapper);
				}
			}
			qrDetail.setCreateDefaults(quotationRequestDto.getOrganizationId()!=null ? quotationRequestDto.getOrganizationId() : sessionService.getOrganizationId(),quotationRequestDto.getUnitId() !=null ? quotationRequestDto.getUnitId() :sessionService.getUnitId() ,quotationRequestDto.getUserId()!=null ? quotationRequestDto.getUserId() : sessionService.getUserId());
			
			quotation.addQuotationRequestDetailList(qrDetail);
			} else {
				// CustomException custom = new CustomException("Purchase request Quantity is
				// Invalid");
				throw new DataIntegrityViolationException("Please enter valid Quantity");

			}
			
			

		}
		
		for (QuotationRequestDetailDto item : quotationRequestDto.getItems()) {
			if (quotationRequestDto.getRequestTypeId() == com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType() && item.getPrIds() != null) {
			for(QuotationRequestDetail qrDetails : quotation.getQuotationRequestDetailsList()) {
				Iterator<Map.Entry<Integer, PurchaseRequestDetail>> itr = hm.entrySet().iterator();
				while(itr.hasNext())
				{    Map.Entry<Integer, PurchaseRequestDetail> entry = itr.next();
		             
		            	 for(PurchaseRequestDto purchaseRequestDto : item.getPrIds()) {
		            		 for(Integer prDetailId : purchaseRequestDto.getPrDetailId()) {
		            			 if(entry.getKey().equals(prDetailId)) {
		            				// qrDetails.addPurchaseRequestDetailList(entry.getValue());
		            			 }
		            		 }
		            	 }
		             }
		        }
			}
		}
		
		/*if (quotationRequestDto.getRequestTypeId() == com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType() && item.getPrIds() != null) {				
			for(PurchaseRequestDto purchaseRequestDto : item.getPrIds()) {
				List<PurchaseRequestDetail> purchaseRequestDetails = quotationRequestDao.getPurchaseRequestDetails(purchaseRequestDto.getPrDetailId()); 
				for (PurchaseRequestDetail prDetail :purchaseRequestDetails) {
					qrDetail.addPurchaseRequestDetailList(prDetail);
					
				}
						  
			}
		}*/

	//	sessionService.setEntityDefaults(quotation);
		quotationRequestDao.saveQuotationRequest(quotation);

		CommonResponse response = new CommonResponse();
		response.setId(quotation.getId().longValue());
		response.setMessage("Quotation saved successfully");
		return response;

	}

	@Override
	@Transactional
	public Object updateQuotationRequest(QuotationRequestDto quotationRequestDto) {

		QuotationRequest quotation = quotationRequestDao.getQuotationRequest(quotationRequestDto.getId());

		quotation.updateQuotationRequest(
				quotationRequestDto.getRemark() != null ? quotationRequestDto.getRemark() : quotation.getRemark());

		for (QuotationRequestDetail qrDetail : quotation.getQuotationRequestDetailsList()) {
			Integer id = null;

			for (QuotationRequestDetailDto item : quotationRequestDto.getItems()) {
				if (item.getId() != null && item.getId().equals(qrDetail.getId())) {
					id = item.getId();

					if(quotation.getAssetType().getId().equals(Constants.getConsumableId())) {
						Integer leaseQuantity = 0;
					if(item.getQuantity()!=null && item.getUomTypeId()!=null) {
						 leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(), getUom(qrDetail.getItem().getId(),quotationRequestDto.getOrganizationId()),
								new UomType(item.getUomTypeId()));
					}
					
					qrDetail.updateQuotationRequestDetail(item.getQuantity() != null ? item.getQuantity() : qrDetail.getQuantity(),
							new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
							item.getWarrantyPeriod(), item.getPaymentTerms(), item.getSupportTerms(),
							item.getTechnicalSpecification(),leaseQuantity,item.getQuantity() != null ? item.getQuantity() : qrDetail.getQuantity());
					qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :qrDetail.getQrPendingQuantity());
					}
					
					else if(quotation.getAssetType().getId().equals(Constants.getFixedAssetId())) {
						qrDetail.updateQuotationRequestDetail(item.getQuantity() != null ? item.getQuantity() : qrDetail.getQuantity(),
								item.getWarrantyPeriod(), item.getPaymentTerms(), item.getSupportTerms(),
								item.getTechnicalSpecification(),item.getQuantity() != null ? item.getQuantity() : qrDetail.getLeaseQuantity());
						qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :qrDetail.getQrPendingQuantity());
					}
					
					for (QuotationRequestAccessoriesMapper qrAccess : qrDetail
							.getQuotationRequestAccessoriesMapperList()) {
						Integer accessId = null;
						if(item.getExpectedAccessories()!=null && !item.getExpectedAccessories().isEmpty()) {
						for (QRAccessoriesMapper qrAccessDto : item.getExpectedAccessories()) {
							if (qrAccessDto.getId() != null && qrAccessDto.getId().equals(qrAccess.getId())) {
								accessId = qrAccessDto.getId();
								qrAccess.updateQuotationRequestAccessoriesMapper(qrAccessDto.getDetails(),
										qrAccessDto.getName());
								}
							}
						}
						if (accessId == null) {
							qrAccess.setIsDeleted(true);
						}

						qrAccess.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
						qrAccess.setUpdatedByUser(quotationRequestDto.getUserId());
					}
					if(item.getExpectedAccessories()!=null && !item.getExpectedAccessories().isEmpty()) {
					for (QRAccessoriesMapper qrAccessDto : item.getExpectedAccessories()) {
						if (qrAccessDto.getId() == null) {
							QuotationRequestAccessoriesMapper qrMapper=	new QuotationRequestAccessoriesMapper(
									qrAccessDto.getDetails(), qrAccessDto.getName());
							qrMapper.setCreateDefaults(quotationRequestDto.getOrganizationId()!=null ? quotationRequestDto.getOrganizationId() : sessionService.getOrganizationId(),quotationRequestDto.getUnitId() !=null ? quotationRequestDto.getUnitId() :sessionService.getUnitId() ,quotationRequestDto.getUserId()!=null ? quotationRequestDto.getUserId() : sessionService.getUserId());
							
							qrDetail.addQuotationRequestAccessoriesMapperList(qrMapper);
							}
						}
					}

				}
			}
			if (id == null) {
				qrDetail.setIsDeleted(true);
			}

			qrDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
			qrDetail.setUpdatedByUser(quotationRequestDto.getUserId());

		}
		HashMap<Integer, PurchaseRequestDetail> hm = new HashMap();
		for (QuotationRequestDetailDto item : quotationRequestDto.getItems()) {/*
			if (item.getId() == null) {
				QuotationRequestDetail qrDetail = null;
				if(quotationRequestDto.getAssetTypeId().equals(Constants.getConsumableId())) {
					Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(), getUom(item.getItemId(),quotationRequestDto.getOrganizationId()),
							new UomType(item.getUomTypeId()));
				qrDetail = quotation.addQuotationRequestDetailList(new QuotationRequestDetail(
						new Item(item.getItemId()), item.getQuantity(), new Status(item.getStatusId()),
						new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()), item.getWarrantyPeriod(),
						item.getPaymentTerms(), item.getSupportTerms(), item.getTechnicalSpecification(),leaseQuantity));
				qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :item.getQrPendingQuantity());
				}
				else if(quotationRequestDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					qrDetail = quotation.addQuotationRequestDetailList(new QuotationRequestDetail(
							new Item(item.getItemId()), item.getQuantity(), new Status(item.getStatusId()),
							item.getWarrantyPeriod(),item.getPaymentTerms(), item.getSupportTerms(), item.getTechnicalSpecification(),item.getQuantity()));
					qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :item.getQrPendingQuantity());
				}

				if (item.getExpectedAccessories() != null) {
					for (QRAccessoriesMapper qraccess : item.getExpectedAccessories()) {
						qrDetail.addQuotationRequestAccessoriesMapperList(
								new QuotationRequestAccessoriesMapper(qraccess.getDetails(), qraccess.getName()));
					}
				}
				if (quotationRequestDto
						.getRequestTypeId() == com.param.common.constants.RequestType.AgainstPurchaseRequest
								.getRequestType()
						&& item.getPrId() != null) {
					List<PurchaseRequestDetail> purchaseRequestDetails = quotationRequestDao
							.getPurchaseRequestDetails(item.getPrId());
					for (PurchaseRequestDetail prDetail : purchaseRequestDetails) {
						qrDetail.addPurchaseRequestDetailList(prDetail);
					}
				}

			}
		*/
			
		

			if (item.getId() == null) {
			
			QuotationRequestDetail qrDetail = null;
			
			//consumable
			if (quotationRequestDto.getAssetTypeId().equals(Constants.getConsumableId())) {
				
				Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(), getUom(item.getItemId(),quotationRequestDto.getOrganizationId()),
						new UomType(item.getUomTypeId()));
			qrDetail = new QuotationRequestDetail(
					new Item(item.getItemId()), item.getQuantity(), new Status(item.getStatusId()),
					new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()), item.getWarrantyPeriod(),
					item.getPaymentTerms(), item.getSupportTerms(), item.getTechnicalSpecification(),leaseQuantity);
			qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :item.getQrPendingQuantity());
			}
			
			//fixed
			else if (quotationRequestDto.getAssetTypeId().equals(Constants.getFixedAssetId())) {
				qrDetail = new QuotationRequestDetail(new Item(item.getItemId()), item.getQuantity(), new Status(item.getStatusId()),
						item.getWarrantyPeriod(),item.getPaymentTerms(), item.getSupportTerms(), item.getTechnicalSpecification(),item.getQuantity());
				qrDetail.setQrPendingQuantity(item.getQrPendingQuantity()!=null ? item.getQrPendingQuantity() :item.getQrPendingQuantity());
			}
			if (quotationRequestDto.getRequestTypeId() == com.param.common.constants.RequestType.AgainstPurchaseRequest.getRequestType() && item.getPrIds() != null) {
			for(PurchaseRequestDto purchaseRequestDto : item.getPrIds()) {
				List<PurchaseRequestDetail> purchaseRequestDetails = quotationRequestDao.getPurchaseRequestDetails(purchaseRequestDto.getPrDetailId()); 
				for (PurchaseRequestDetail prDetail :purchaseRequestDetails) {
					//qrDetail.addPurchaseRequestDetailList(prDetail);
					hm.put(prDetail.getId(), prDetail);									
				    }
				}
			}
			if (item.getExpectedAccessories() != null) {
				for (QRAccessoriesMapper qraccess : item.getExpectedAccessories()) {
					QuotationRequestAccessoriesMapper quotationRequestAccessoriesMapper=new QuotationRequestAccessoriesMapper(qraccess.getDetails(), qraccess.getName());
					quotationRequestAccessoriesMapper.setCreateDefaults(quotationRequestDto.getOrganizationId()!=null ? quotationRequestDto.getOrganizationId() : sessionService.getOrganizationId(),quotationRequestDto.getUnitId() !=null ? quotationRequestDto.getUnitId() :sessionService.getUnitId() ,quotationRequestDto.getUserId()!=null ? quotationRequestDto.getUserId() : sessionService.getUserId());
					
					qrDetail.addQuotationRequestAccessoriesMapperList(quotationRequestAccessoriesMapper);
				}
			}
			qrDetail.setCreateDefaults(quotationRequestDto.getOrganizationId()!=null ? quotationRequestDto.getOrganizationId() : sessionService.getOrganizationId(),quotationRequestDto.getUnitId() !=null ? quotationRequestDto.getUnitId() :sessionService.getUnitId() ,quotationRequestDto.getUserId()!=null ? quotationRequestDto.getUserId() : sessionService.getUserId());
			
			quotation.addQuotationRequestDetailList(qrDetail);
			
			
			}

		
		
		
		}
		quotation.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
		quotation.setUpdatedByUser(quotationRequestDto.getUserId());

		quotationRequestDao.updateQuotationRequest(quotation);

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Request updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getQuotationRequest(Integer id) {

		QuotationRequest quotation = quotationRequestDao.getQuotationRequest(id);

		QuotationRequestResponse qrResponse = new QuotationRequestResponse(quotation.getId(), quotation.getQrNo(),
				quotation.getAddedDate(), quotation.getStore().getStoreId(), quotation.getStore().getStoreName(),
				quotation.getAssetType().getId(), quotation.getAssetType().getType(),
				quotation.getRequestType().getId(), quotation.getRequestType().getType(),
				quotation.getRemark() != null ? quotation.getRemark() : null, quotation.getStatus().getId(),
				quotation.getStatus().getStatus(), quotation.getApprovalStatus().getId(),
				quotation.getApprovalStatus().getStatus(), quotation.getAddedBy().getUserName());

		for (QuotationRequestDetail detail : quotation.getQuotationRequestDetailsList()) {
			if (detail.getIsDeleted() != true && detail.getIsActive() != false) {
				
				Long storeStock = GlobalDao.getStoreStockByItem(
						detail.getQuotationRequest().getStore().getStoreId(),
						detail.getItem().getId(),0);
				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(
						detail.getQuotationRequest().getStore().getStoreId(),
						detail.getItem().getId());
				Long maxStoreQuantity = (long)GlobalDao.getStoreMaxQuantityByItem(detail.getQuotationRequest().getStore().getStoreId(),	detail.getItem().getId(),null);
				Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
				
				if (qrResponse.getAssetTypeId().equals(Constants.getConsumableId())) {
					QuotationRequestDetailsResponse data=new QuotationRequestDetailsResponse(detail.getId(), detail.getItem().getId(),
							detail.getItem().getName(), detail.getItem().getCode(),
							detail.getQuantity() != null ? detail.getQuantity() : 0, detail.getUomType()!=null ? detail.getUomType().getId() : null,
									detail.getUomType()!=null ? detail.getUomType().getType() : null, detail.getUomUnit()!=null ? detail.getUomUnit().getId() : null, detail.getUomUnit()!=null ? detail.getUomUnit().getUnits() : null,maxStoreQuantity,maxOrderQuantity,
							detail.getWarrantyPeriod() != null ? detail.getWarrantyPeriod() : null,
							detail.getExpectedPayment() != null ? detail.getExpectedPayment() : null,
							detail.getExpectedSupportTerm() != null ? detail.getExpectedSupportTerm() : null,
							detail.getExpectedTechnicalSpecification() != null ? detail.getExpectedTechnicalSpecification()
									: null,
							GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(),null),
							getExpectedAccessories(detail), quotationRequestDao.getPurchaseRequestDetail(detail.getId()), detail.getItem().getIsThreeUOMApplicable());
					data.setQrPendingquantity(detail.getQrPendingQuantity());
				qrResponse.addItems(data);
				}
				else if(qrResponse.getAssetTypeId().equals(Constants.getFixedAssetId())) {
					QuotationRequestDetailsResponse data1=new QuotationRequestDetailsResponse(detail.getId(), detail.getItem().getId(),
							detail.getItem().getName(), detail.getItem().getCode(),
							detail.getQuantity() != null ? detail.getQuantity() : 0,maxStoreQuantity,maxOrderQuantity,
							detail.getWarrantyPeriod() != null ? detail.getWarrantyPeriod() : null,
							detail.getExpectedPayment() != null ? detail.getExpectedPayment() : null,
							detail.getExpectedSupportTerm() != null ? detail.getExpectedSupportTerm() : null,
							detail.getExpectedTechnicalSpecification() != null ? detail.getExpectedTechnicalSpecification()
									: null,
							getExpectedAccessories(detail), quotationRequestDao.getPurchaseRequestDetail(detail.getId()), detail.getItem().getIsThreeUOMApplicable());
					
					data1.setQrPendingquantity(detail.getQrPendingQuantity());
					qrResponse.addItems(data1);
					
				}
			}
		}

		return qrResponse;
	}

	@Transactional
	private List<QRAccessoriesMapper> getExpectedAccessories(QuotationRequestDetail detail) {
		List<QuotationRequestAccessoriesMapper> qrAccessMapperList = detail.getQuotationRequestAccessoriesMapperList();
		List<QRAccessoriesMapper> qrAccesoriesMapper = new ArrayList<QRAccessoriesMapper>();
		for (QuotationRequestAccessoriesMapper qrAccess : qrAccessMapperList) {
			QRAccessoriesMapper qrAccessMapper = new QRAccessoriesMapper(qrAccess.getId(),
					qrAccess.getAccessoriesDetail(), qrAccess.getName());
			qrAccesoriesMapper.add(qrAccessMapper);
		}
		return qrAccesoriesMapper;

	}

	@Override
	@Transactional
	public Object approveQuotationRequest(QuotationRequestDto quotationRequestDto) {

		for (Integer id : quotationRequestDto.getQrId()) {
			QuotationRequest quotation = quotationRequestDao.getQuotationRequest(id);

			quotation.setStatus(new Status(quotationRequestDto.getStatusId()));
			quotation.setApprovalStatus(new Status(quotationRequestDto.getApprovalStatusId()));

			for (QuotationRequestDetail detail : quotation.getQuotationRequestDetailsList()) {
				detail.setStatus(new Status(quotationRequestDto.getApprovalStatusId()));
				

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
				detail.setUpdatedByUser(quotationRequestDto.getUserId());
			}

			quotation.setApprovedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
			quotation.setApprovedBy(new User(quotationRequestDto.getUserId()!=null ? quotationRequestDto.getUserId() : sessionService.getUserId()));

			quotation.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
			quotation.setUpdatedByUser(quotationRequestDto.getUserId());

			quotationRequestDao.updateQuotationRequest(quotation);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Request approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelQuotationRequest(QuotationRequestDto quotationRequestDto) {

		QuotationRequest quotation = quotationRequestDao.getQuotationRequest(quotationRequestDto.getId());

		for (QuotationRequestDetail detail : quotation.getQuotationRequestDetailsList()) {
			detail.setStatus(new Status(quotationRequestDto.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
			detail.setUpdatedByUser(quotationRequestDto.getUserId());
		}
		quotation.setCancelReason(new CancelReason(quotationRequestDto.getCancelReasonId()));
		quotation.setCancelNote(quotationRequestDto.getCancelNote());

		quotation.setStatus(new Status(quotationRequestDto.getStatusId()));
		quotation.setApprovalStatus(new Status(quotationRequestDto.getApprovalStatusId()));

		quotation.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
		quotation.setUpdatedByUser(quotationRequestDto.getUserId());

		quotationRequestDao.updateQuotationRequest(quotation);

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Request cancelled successfully");

		return response;
	}

	
	public Boolean isQuantityValidated(Integer assetTypeId, Integer storeId, Integer itemId, Integer quantity,
			Integer uomTypeId, Integer currentLeaseQunatity,Integer orgId, Integer unitid) {

		Long storeStock = GlobalDao.getStoreStockByItem(storeId, itemId,unitid);
		Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, itemId);
		Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, itemId,orgId);
		Long prLeaseQunatity = quotationRequestDao.getLeaseQunatity(storeId, itemId);
		if (prLeaseQunatity < currentLeaseQunatity) {
			prLeaseQunatity = currentLeaseQunatity - prLeaseQunatity;
		} else if (prLeaseQunatity > currentLeaseQunatity) {
			prLeaseQunatity = prLeaseQunatity - currentLeaseQunatity;
		}
		
		Integer leaseQuantity = quantity;
		if (assetTypeId.equals(Constants.getConsumableId())) {
			leaseQuantity = Util.GetLeaseQuantity(quantity, getUom(itemId,orgId), new UomType(uomTypeId));
		}
		if (maxStoreQuantity < (prLeaseQunatity + leaseQuantity + stockInTransit + storeStock)) {
			return false;
		}

		return true;
	}

	public List<PurchaseOrderUomMapper> getUom(Integer itemId,Integer orgId) {

		List<UomResponse> uomResponse = GlobalDao.getUnitOfMeasurementListByItem(itemId,orgId);
		List<PurchaseOrderUomMapper> prUomResponse = new ArrayList<PurchaseOrderUomMapper>();
		for (UomResponse uom : uomResponse) {
			PurchaseOrderUomMapper prUom = new PurchaseOrderUomMapper(new UomType(uom.getUomTypeId()),
					new UomUnit(uom.getUomUnitId()), uom.getConversion(), uom.getIpUom(), uom.getOpUom(),
					uom.getStoreUom());
			prUomResponse.add(prUom);
		}
		return prUomResponse;
	}
}
