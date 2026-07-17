package com.param.procurement.quotation.service.consignmentPOInvoice;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ConsignmentStockPOInvoice;
import com.param.entity.model.procurement.ConsignmentStockPOInvoiceDetail;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.quotation.dao.consignmentPOInvoice.IConsignmentStockPOInvoiceDao;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPODetailResponseDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPORequestDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPOResponseDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConsignmentStockPOInvoiceServicesImpl implements IConsignmentStockPOInvoiceService {

	final static Logger logger = Logger.getLogger(ConsignmentStockPOInvoiceServicesImpl.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	IConsignmentStockPOInvoiceDao consignmentStockPOInvoiceDao;
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Override
	@Transactional
	public List<ConsignmentStockPOResponseDto> getConsignmentStockPOInvoiceList(ConsignmentStockPORequestDto consignmentStockPORequestDto) {
		List<ConsignmentStockPOResponseDto> ConsignmentStockPOResponseDto = new ArrayList<ConsignmentStockPOResponseDto>();
		consignmentStockPORequestDto.setToDate((new DateTime(consignmentStockPORequestDto.getToDate())).plusDays(1).toDate());
		try {
			ConsignmentStockPOResponseDto = consignmentStockPOInvoiceDao.getConsignmentPOInvoiceList(consignmentStockPORequestDto);
			return ConsignmentStockPOResponseDto;
		} catch(Exception e){
			logger.error("getConsignmentStockPOInvoiceList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public List<ConsignmentStockPOResponseDto> getConsignmentStockPOList(
			ConsignmentStockPORequestDto consignmentStockPORequest) {
		List<ConsignmentStockPOResponseDto> consignmentStockPOResponseList = new ArrayList<ConsignmentStockPOResponseDto>();
		consignmentStockPORequest.setToDate((new DateTime(consignmentStockPORequest.getToDate())).plusDays(1).toDate());

		List<ConsignmentStockPOResponseDto> consignmentStockPOResponse = consignmentStockPOInvoiceDao.searchConsignmentPOBP(consignmentStockPORequest);
		List<Integer> poIds = consignmentStockPOResponse.stream().map(ConsignmentStockPOResponseDto::getId)
				.collect(Collectors.toList());
		List<ConsignmentStockPODetailResponseDto> poDetailsResp = new ArrayList<ConsignmentStockPODetailResponseDto>();
		if (poIds != null && !poIds.isEmpty())
			poDetailsResp = consignmentStockPOInvoiceDao.getConsignmentPoDetails(poIds);

		for (ConsignmentStockPOResponseDto POSearchResponse : consignmentStockPOResponse) {
			List<ConsignmentStockPODetailResponseDto> purchaseOrderDetailResponseList = new ArrayList<ConsignmentStockPODetailResponseDto>();

			List<ConsignmentStockPODetailResponseDto> specificPoDetailsResp = poDetailsResp.stream()
					.filter(x -> x.getPoId().equals(POSearchResponse.getId())).collect(Collectors.toList());
			consignmentStockPOResponseList.add(POSearchResponse);
			for (ConsignmentStockPODetailResponseDto purchaseOrderDetailResponse : specificPoDetailsResp) {

				purchaseOrderDetailResponseList.add(purchaseOrderDetailResponse);
				if (purchaseOrderDetailResponseList != null && purchaseOrderDetailResponseList.size() > 0) {
					POSearchResponse.setItems(purchaseOrderDetailResponseList);
					
				}
			}
		}
		return consignmentStockPOResponseList;
	}

	@Override
	@Transactional
	public CommonResponse saveConsignmentStockPOInvoice(List<ConsignmentStockPOResponseDto> consignmentStockPOResponseDtoList) throws Exception {
		CommonResponse response = new CommonResponse();		
		//try {
			String invoiceNumber = consignmentStockPOResponseDtoList.getFirst().getInvoiceNumber();
			BigInteger countInvoiceNumber= consignmentStockPOInvoiceDao.getInvoiceNumberCount(invoiceNumber);
			if(countInvoiceNumber.longValue() > 0) {
				throw new CustomException("Invoice number already exists");
			}
			
			for (ConsignmentStockPOResponseDto consignmentStockPOResponseDto : consignmentStockPOResponseDtoList) {
				
				ConsignmentStockPOInvoice consignmentStockPOInvoice = new ConsignmentStockPOInvoice(
						new Status(com.param.common.constants.Status.Pending.getStatus()),
						new AssetType(consignmentStockPOResponseDto.getAssetTypeId()),
						new Store(consignmentStockPOResponseDto.getRequestingStoreId()),
						new Store(consignmentStockPOResponseDto.getIssuingStoreId()),
						new Vendor(consignmentStockPOResponseDto.getVendorId()),
						new PurchaseOrder(consignmentStockPOResponseDto.getId()),
						new GoodReceiptNote(consignmentStockPOResponseDto.getGrnId()),
						invoiceNumber , Util.getDate(consignmentStockPOResponseDto.getInvoiceDate()));						
						
				for (ConsignmentStockPODetailResponseDto item : consignmentStockPOResponseDto.getItems()) {
					ConsignmentStockPOInvoiceDetail cpoiDetail = null;
					ConsignmentStockPOInvoiceDetail consignmentStockPOInvoiceDetail = new ConsignmentStockPOInvoiceDetail(
							item.getRemark(),
							item.getSpecification(),
							new Status(com.param.common.constants.Status.Pending.getStatus()),
							new GoodReceiptNoteDetail(item.getGrnDetailId()),
							new PurchaseOrderDetail(item.getId()));
					/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
					consignmentStockPOInvoiceDetail.setCop(item.getCop());
					consignmentStockPOInvoiceDetail.setMrp(item.getMrp());
					consignmentStockPOInvoiceDetail.setDiscount(item.getDiscount());
					consignmentStockPOInvoiceDetail.setTax(new Tax(item.getTaxId()));
					consignmentStockPOInvoiceDetail.setTaxPercentage(item.getTaxPercentage());
					consignmentStockPOInvoiceDetail.setTotalAmount(item.getTotalAmount());
					consignmentStockPOInvoiceDetail.setTotalDiscount(item.getDiscountAmount());
					consignmentStockPOInvoiceDetail.setTaxAmount(item.getTaxAmount());
					consignmentStockPOInvoiceDetail.setTotalOtherAmount(item.getTotalOtherAmount());
					consignmentStockPOInvoiceDetail.setNetAmount(item.getNetAmount());
					/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
							
					consignmentStockPOInvoiceDetail.setCreateDefaults(consignmentStockPOResponseDto.getOrganizationId()!=null ? consignmentStockPOResponseDto.getOrganizationId() : sessionService.getOrganizationId(),consignmentStockPOResponseDto.getUnitId() !=null ? consignmentStockPOResponseDto.getUnitId() :sessionService.getUnitId() ,consignmentStockPOResponseDto.getUserId()!=null ? consignmentStockPOResponseDto.getUserId() : sessionService.getUserId());
					cpoiDetail = consignmentStockPOInvoice.addConsignmentPOInvoiceDetailList(consignmentStockPOInvoiceDetail);
					cpoiDetail.setCreateDefaults(consignmentStockPOResponseDto.getOrganizationId()!=null ? consignmentStockPOResponseDto.getOrganizationId() : sessionService.getOrganizationId(),consignmentStockPOResponseDto.getUnitId() !=null ? consignmentStockPOResponseDto.getUnitId() :sessionService.getUnitId() ,consignmentStockPOResponseDto.getUserId()!=null ? consignmentStockPOResponseDto.getUserId() : sessionService.getUserId());
					

				}

				consignmentStockPOInvoice.setCreateDefaults(consignmentStockPOResponseDto.getOrganizationId()!=null ? consignmentStockPOResponseDto.getOrganizationId() : sessionService.getOrganizationId(),consignmentStockPOResponseDto.getUnitId() !=null ? consignmentStockPOResponseDto.getUnitId() :sessionService.getUnitId() ,consignmentStockPOResponseDto.getUserId()!=null ? consignmentStockPOResponseDto.getUserId() : sessionService.getUserId());
				
				consignmentStockPOInvoiceDao.saveConsignmentPOInvoice(consignmentStockPOInvoice);

				response.setId(consignmentStockPOInvoice.getId().longValue());
			}
			
			response.setMessage("Consignment Po Invoice Detail Saved Successfully");
		/*} catch (Exception e) {
			
			response.setMessage("Error while Saving Consignment Po Invoice Detail");
		}*/
		return response;
	}

	@Override
	@Transactional
	public ConsignmentStockPOResponseDto getConsignmentStockPOInvoiceDetail(ConsignmentStockPOResponseDto consignmentStockPOResponseDto) {
		/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
		List<ConsignmentStockPODetailResponseDto> consignmentStockPODetailResponseList = consignmentStockPOInvoiceDao.getConsignmentStockPOInvoiceDetail(consignmentStockPOResponseDto);
		/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
		consignmentStockPOResponseDto.setItems(consignmentStockPODetailResponseList);
		return consignmentStockPOResponseDto;
	}

	@Override
	@Transactional
	public CommonResponse updateConsignmentStockPOInvoiceDetail(
			ConsignmentStockPOResponseDto consignmentStockPOResponseDto) {
		CommonResponse response = new CommonResponse();
		ConsignmentStockPOInvoice consignmentStockPOInvoice = consignmentStockPOInvoiceDao.updateConsignmentStockPOInvoice(consignmentStockPOResponseDto);

		consignmentStockPOInvoice.setInvoiceNumber(consignmentStockPOResponseDto.getInvoiceNumber()!=null ? consignmentStockPOResponseDto.getInvoiceNumber():consignmentStockPOInvoice.getInvoiceNumber());
		for (ConsignmentStockPOInvoiceDetail consignmentStockPOInvoiceDetail : consignmentStockPOInvoice.getConsignmentStockPOInvoiceDetailList()) {
			for (ConsignmentStockPODetailResponseDto itemDetail : consignmentStockPOResponseDto.getItems()) {
				if (itemDetail.getId().equals(consignmentStockPOInvoiceDetail.getId())) {
					consignmentStockPOInvoiceDetail.setRemark(itemDetail.getRemark());
					consignmentStockPOInvoiceDetail.setSpecification(itemDetail.getSpecification());
					consignmentStockPOInvoiceDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockPOInvoice.getUnitId()));
					consignmentStockPOInvoiceDetail.setUpdatedByUser(consignmentStockPOResponseDto.getUserId()!=null ? consignmentStockPOResponseDto.getUserId() : sessionService.getUserId());
					
					/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
					consignmentStockPOInvoiceDetail.setCop(itemDetail.getCop());
					consignmentStockPOInvoiceDetail.setMrp(itemDetail.getMrp());
					consignmentStockPOInvoiceDetail.setDiscount(itemDetail.getDiscount());
					consignmentStockPOInvoiceDetail.setTax(new Tax(itemDetail.getTaxId()));
					consignmentStockPOInvoiceDetail.setTaxPercentage(itemDetail.getTaxPercentage());
					consignmentStockPOInvoiceDetail.setTotalAmount(itemDetail.getTotalAmount());
					consignmentStockPOInvoiceDetail.setTotalDiscount(itemDetail.getDiscountAmount());
					consignmentStockPOInvoiceDetail.setTaxAmount(itemDetail.getTaxAmount());
					consignmentStockPOInvoiceDetail.setTotalOtherAmount(itemDetail.getTotalOtherAmount());
					consignmentStockPOInvoiceDetail.setNetAmount(itemDetail.getNetAmount());
					/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
				}
			}
		}
		/*for (ConsignmentStockPODetailResponseDto item : consignmentStockPOResponseDto.getItems()) {
			try {
				 consignmentStockPOInvoiceDao.updateConsignmentStockPOInvoiceDetail(item);
				 response.setMessage("Consignment Po Invoice Detail Update Successfully");
			}catch (Exception e) {
				
				response.setMessage("Error while Updating Consignment Po Invoice Detail");
			}
		}*/
		
		return response;
	}

	@Override
	@Transactional
	public CommonResponse updateConsignmentStockPOInvoice(List<ConsignmentStockPOResponseDto> consignmentStockPOResponseDto) {
		CommonResponse response = new CommonResponse();
		try {
			for (ConsignmentStockPOResponseDto consignmentStockPO : consignmentStockPOResponseDto) {

				ConsignmentStockPOInvoice consignmentStockPOInvoice = consignmentStockPOInvoiceDao.updateConsignmentStockPOInvoice(consignmentStockPO);
				Status status = null;
				if(consignmentStockPO.getCancelReasonId()!= null) {
					status = new Status(com.param.common.constants.Status.Cancelled.getStatus());
					consignmentStockPOInvoice.setCancelReason(new CancelReason(consignmentStockPO.getCancelReasonId()));
					consignmentStockPOInvoice.setCancelNote(consignmentStockPO.getCancelNote());
				}else {
					status = new Status(com.param.common.constants.Status.Closed.getStatus());
				}	
				for (ConsignmentStockPOInvoiceDetail consignmentStockPOInvoiceDetail : consignmentStockPOInvoice.getConsignmentStockPOInvoiceDetailList()) {
									
					consignmentStockPOInvoiceDetail.setStatus(status);
					consignmentStockPOInvoiceDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockPOInvoice.getUnitId()));
					consignmentStockPOInvoiceDetail.setUpdatedByUser(consignmentStockPO.getUserId()!=null?consignmentStockPO.getUserId():sessionService.getUserId());
				}
				consignmentStockPOInvoice.setStatus(status);
				consignmentStockPOInvoice.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockPOInvoice.getUnitId()));
				consignmentStockPOInvoice.setUpdatedByUser(consignmentStockPO.getUserId()!=null?consignmentStockPO.getUserId():sessionService.getUserId());
			}
			response.setMessage("Consignment Po Invoice Saved Successfully");
		} catch (Exception e) {
			logger.error("updateConsignmentStockPOInvoice failed", e);
			throw new ServiceException(e.getMessage());
		}

		return response;
	}

	@Override
	@Transactional
	public Object getTaxList(Integer orgId) {		
		return GlobalDao.getTaxListBP(orgId);
	}

	@Override
	@Transactional
	public Object cancelPurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto) {


		ConsignmentStockPOInvoice consignmentStockPOInvoice = consignmentStockPOInvoiceDao.getPurchaseOrderInvoice(purchaseOrderDto.getId());

		for (ConsignmentStockPOInvoiceDetail detail : consignmentStockPOInvoice.getConsignmentStockPOInvoiceDetailList()) {
			detail.setStatus(new Status(purchaseOrderDto.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockPOInvoice.getUnitId()));
			detail.setUpdatedByUser(purchaseOrderDto.getUserId());
		}

		consignmentStockPOInvoice.setCancelReason(new CancelReason(purchaseOrderDto.getCancelReasonId()));
		consignmentStockPOInvoice.setCancelNote(purchaseOrderDto.getCancelNote());

		consignmentStockPOInvoice.setStatus(new Status(purchaseOrderDto.getStatusId()));
	//	consignmentStockPOInvoice.setApprovalStatus(new Status(purchaseOrderDto.getApprovalStatusId()));

		consignmentStockPOInvoice.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentStockPOInvoice.getUnitId()));
		consignmentStockPOInvoice.setUpdatedByUser(purchaseOrderDto.getUserId());

		consignmentStockPOInvoiceDao.update(consignmentStockPOInvoice);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order Invoice cancelled successfully");

		return response;
	
	}
}
