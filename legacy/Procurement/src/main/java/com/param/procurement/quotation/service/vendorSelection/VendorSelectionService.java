package com.param.procurement.quotation.service.vendorSelection;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.procurement.VendorNegotiationDetail;
import com.param.entity.model.procurement.VendorSelection;
import com.param.entity.model.procurement.VendorSelectionDetail;
import com.param.entity.model.procurement.VendorSelectionMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.quotation.dao.vendorSelection.IVendorSelectionDao;
import com.param.procurement.quotation.dto.VendorNegotiationMapperDto;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionDetailRequest;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionDetailResponse;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionDropdown;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionMapperRequest;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionMapperResponse;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionRequest;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionResponse;
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionSearchResponse;
import com.param.utility.TimeZoneComponent;

@Service
public class VendorSelectionService implements IVendorSelectionService {

	private Integer screenId = Screen.VendorSelection.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IVendorSelectionDao vendorSelectionDao;

	@Override
	@Transactional
	public VendorSelectionDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		VendorSelectionDropdown vSelectionDropDown = new VendorSelectionDropdown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			vSelectionDropDown
					.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			vSelectionDropDown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return vSelectionDropDown;
	}

	@Override
	@Transactional
	public Object getVendorSelectionList(VendorSelectionRequest vendorSelectionRequest) {
		vendorSelectionRequest.setToDate((new DateTime(vendorSelectionRequest.getToDate())).plusDays(1).toDate());

		List<VendorSelectionSearchResponse> vendorSelectionSearchList = new ArrayList<VendorSelectionSearchResponse>();

		for (VendorSelection vendorSelection : vendorSelectionDao.searchVendorSelectionList(vendorSelectionRequest,
				screenId)) {
		//	vendorSelection.getVendorSelectionDetailList().get(0).getVendorSelectionMapperList()
			VendorSelectionSearchResponse response = new VendorSelectionSearchResponse(vendorSelection.getId(),
					vendorSelection.getQuotationRequest().getAddedDate(),
					vendorSelection.getQuotationRequest().getQrNo(),
					vendorSelection.getQuotationRequest().getStore().getStoreName(),
					vendorSelection.getQuotationRequest().getAssetType().getType(),
					vendorSelection.getQuotationRequest().getRequestType().getType(),
					vendorSelection.getAddedBy().getUserName(), vendorSelection.getAddedDate(),
					vendorSelection.getStatus().getStatus());

		//	vendorSelectionSearchList.add(response);
			Boolean addFlag=false;
			for( VendorSelectionDetail vsd : vendorSelection.getVendorSelectionDetailList())
			{
				
				for(VendorSelectionMapper vsmapper: vsd.getVendorSelectionMapperList())
				{
					if(!vsmapper.getIsAccept().equals(true))
					{
						addFlag=true;
						
					}
					
				}
				
			}
			
			if(addFlag || addFlag.equals(false)) // HIS-12472 (need to check why this if(addFlag ) )
			{
				vendorSelectionSearchList.add(response);
			}

		}
		return vendorSelectionSearchList;
	}

	@Override
	@Transactional
	public Object updateVendorSelection(VendorSelectionRequest vendorSelectionRequest) {

		VendorSelection vendorSelection = vendorSelectionDao.getVendorSelection(vendorSelectionRequest.getId());

		for (VendorSelectionDetail detail : vendorSelection.getVendorSelectionDetailList()) {
			Integer id = null;

			for (VendorSelectionDetailRequest item : vendorSelectionRequest.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					for (VendorSelectionMapper mapper : detail.getVendorSelectionMapperList()) {

						for (VendorSelectionMapperRequest mapperRequest : item.getVendorSelectionDetails()) {
							if(mapperRequest.getId().equals(mapper.getId()))
							{
								mapper.updateVendorSelectionMapper(mapperRequest.getIsAccept(),
										mapperRequest.getWarrantyPeriod(), mapperRequest.getSupportTerm(),
										mapperRequest.getTechnicalSpecification(), mapperRequest.getExpectedPayment(),
										mapperRequest.getQuantity(), mapperRequest.getRemark(),
										mapperRequest.getRejectNote());

								mapper.setUpdatedDate(TimeZoneComponent.getDateByzone(vendorSelection.getUnitId()));
								mapper.setUpdatedByUser(vendorSelectionRequest.getUserId());
								vendorSelectionDao.updateVendorSelectionMapper(mapper);
								detail.getQuotationRequestDetail().setPendingQuantity(mapperRequest.getQuantity());
								detail.getQuotationRequestDetail().setUpdatedDate(TimeZoneComponent.getDateByzone(vendorSelection.getUnitId()));
								detail.getQuotationRequestDetail().setUpdatedByUser(vendorSelectionRequest.getUserId());	
							}
								
								
							
							
						}
					}
				}
			}

			detail.setStatus(new Status(vendorSelectionRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(vendorSelection.getUnitId()));
			detail.setUpdatedByUser(vendorSelectionRequest.getUserId());
		}

		vendorSelection.setStatus(new Status(vendorSelectionRequest.getStatusId()));

		vendorSelection.setUpdatedDate(TimeZoneComponent.getDateByzone(vendorSelection.getUnitId()));
		vendorSelection.setUpdatedByUser(vendorSelectionRequest.getUserId());

		vendorSelectionDao.updateVendorSelection(vendorSelection);

		CommonResponse response = new CommonResponse();
		response.setMessage("Vendor Selection updated successfully");

		return response;

	}

	@Override
	@Transactional
	public Object getVendorSelection(Integer id) {

		VendorSelection vendorSelection = vendorSelectionDao.getVendorSelection(id);

		VendorSelectionResponse response = new VendorSelectionResponse(vendorSelection.getId(),
				vendorSelection.getQuotationRequest().getAddedDate(), vendorSelection.getQuotationRequest().getQrNo(),
				vendorSelection.getQuotationRequest().getStore().getStoreName(),
				vendorSelection.getQuotationRequest().getAssetType().getType(),
				vendorSelection.getQuotationRequest().getRequestType().getType(),
				vendorSelection.getAddedBy().getUserName(), vendorSelection.getAddedDate(),
				vendorSelection.getStatus().getStatus());

		for (VendorSelectionDetail detail : vendorSelection.getVendorSelectionDetailList()) {

			List<VendorSelectionMapperResponse> mapperList = getVendorSelectionDetails(detail);

			response.addItems(new VendorSelectionDetailResponse(detail.getId(),
					detail.getQuotationRequestDetail().getItem().getId(),
					detail.getQuotationRequestDetail().getItem().getName(),
					detail.getQuotationRequestDetail().getQuantity(),
					detail.getQuotationRequestDetail().getWarrantyPeriod(),
					detail.getQuotationRequestDetail().getExpectedSupportTerm(),
					detail.getQuotationRequestDetail().getExpectedTechnicalSpecification(),
					detail.getQuotationRequestDetail().getExpectedPayment(), null, mapperList));
		}

		return response;
	}

	private List<VendorSelectionMapperResponse> getVendorSelectionDetails(VendorSelectionDetail detail) {

		List<VendorSelectionMapperResponse> vendorsDetailslist = new ArrayList<VendorSelectionMapperResponse>();
		/*
		 * for(VendorSelectionMapper vendorDetails :
		 * detail.getVendorSelectionMapperList()) { VendorSelectionMapperResponse mapper
		 * = new VendorSelectionMapperResponse(vendorDetails.getId(),
		 * vendorDetails.getVendorNegotiationMapper().getVendorNegociationDetail().
		 * getQuotationFillDetail().getQuotationVendorDetail().getQuotationVendor().
		 * getVendor().getName(),
		 * vendorDetails.getVendorNegotiationMapper().getVendorNegociationDetail().
		 * getQuotationFillDetail().getQuotationVendorDetail().getQuotationVendor().
		 * getVqNumber(), vendorDetails.getIsAccept(),
		 * vendorDetails.getWarrantyPeriod(), vendorDetails.getSupportTerm(),
		 * vendorDetails.getTechnicalSpecification(),
		 * vendorDetails.getExpectedPayment(),
		 * vendorDetails.getQuantity(),vendorDetails.getVendorNegotiationMapper().getCop
		 * (), vendorDetails.getRemark(),vendorDetails.getRejectNote(),
		 * vendorDetails.getVendorNegotiationMapper().getVendorwarrentyPeriod(),
		 * vendorDetails.getVendorNegotiationMapper().getVendorSupportTerms(),
		 * vendorDetails.getVendorNegotiationMapper().getVendorTechnicalSpecification(),
		 * vendorDetails.getVendorNegotiationMapper().getVendorExpectedPayment());
		 * vendorsDetailslist.add(mapper);
		 * 
		 * }
		 */
		List<Integer> ids = vendorSelectionDao.getMapperIds(detail.getId());
		List<VendorSelectionMapper> data = new ArrayList<>();
		if(!ids.isEmpty())
		{
			 data = vendorSelectionDao.getVendorSelectionMapperList(detail.getId(), ids);
		}
		

		/*
		 * Collection<VendorSelectionMapper> nonDuplicatedEmployees = data.stream()
		 * .<Map<Integer, VendorSelectionMapper>>
		 * collect(HashMap::new,(m,e)->m.put(e.getVendorNegotiationMapper().getId(), e),
		 * Map::putAll) .values();
		 */

		/*
		 * List<VendorSelectionMapper> unique = data.stream()
		 * .collect(collectingAndThen(toCollection(() -> new
		 * TreeSet<>(comparingString(VendorSelectionMapper::get))), ArrayList::new));
		 */

		// for(VendorSelectionMapper vendorDetails :
		// detail.getVendorSelectionMapperList()) {
		if(data.isEmpty())
		{
			data=detail.getVendorSelectionMapperList();
		}
		for (VendorSelectionMapper vendorDetails : data) {
			Integer mapperId = vendorSelectionDao.getVendorNegotiationMapperId(vendorDetails.getId());
			VendorNegotiationMapperDto vnmap = vendorSelectionDao.getVendorNegotiationMapperByIdNew(mapperId);
			// vendorDetails.getVendorSelectionDetail().getq
			VendorNegotiationDetail vnd=vendorSelectionDao.getVendorNegotiationDetailById(vnmap.getVendornegociationdetailid());
			VendorSelectionMapperResponse mapper = new VendorSelectionMapperResponse(vendorDetails.getId(),
					vendorDetails.getVendor().getName(),
					vnd.getQuotationFillDetail().getQuotationVendorDetail()
							.getQuotationVendor().getVqNumber(),
							vendorDetails.getIsAccept(), vendorDetails.getWarrantyPeriod(), vendorDetails.getSupportTerm(),
					vendorDetails.getTechnicalSpecification(), vendorDetails.getExpectedPayment(),
					vendorDetails.getQuantity(), vnmap.getCop(), vnmap.getRemark(),
					vendorDetails.getRejectNote(), vnmap.getVendorwarrentyperiod(), vnmap.getVendorsupportterms(),
					vnmap.getVendortechnicalspecification(), vnmap.getVendorexpectedpayment());
			vendorsDetailslist.add(mapper);
			// break;
		}

		/*
		 * for(VendorSelectionMapperResponse m1 : vendorsDetailslist) {
		 * Iterator<VendorSelectionMapperResponse> itr = vendorsDetailslist.iterator();
		 * while (itr.hasNext()) { VendorSelectionMapperResponse res = itr.next();
		 * 
		 * 
		 * }
		 * 
		 * for(int i =0 ; i<vendorsDetailslist.size(); i++) {
		 * VendorSelectionMapperResponse m2=vendorsDetailslist.get(i);
		 * 
		 * 
		 * 
		 * }
		 * 
		 * 
		 * 
		 * }
		 */

		/*
		 * List<VendorSelectionMapperResponse> unique = vendorsDetailslist.stream()
		 * .map(VendorSelectionMapperResponse::new) .distinct()
		 * .map(VendorSelectionMapperResponse::unwrap) .collect(Collectors.toList());
		 */

		/*
		 * List<VendorSelectionMapperResponse> unique = vendorsDetailslist.stream()
		 * .collect(collectingAndThen(toCollection(() -> new
		 * TreeSet<>(comparingInt(VendorSelectionMapperResponse::))), ArrayList::new));
		 */
		return vendorsDetailslist;
	}

	@Override
	@Transactional
	public Object cancelVendorSelection(VendorSelectionRequest vendorSelectionRequest) {

		VendorSelection vendorSelection = vendorSelectionDao.getVendorSelection(vendorSelectionRequest.getId());

		for (VendorSelectionDetail detail : vendorSelection.getVendorSelectionDetailList()) {

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(vendorSelection.getUnitId()));
			detail.setUpdatedByUser(vendorSelectionRequest.getUserId());
		}
		vendorSelection.setCancelReason(new CancelReason(vendorSelectionRequest.getCancelReasonId()));
		vendorSelection.setCancelNote(vendorSelectionRequest.getCancelNote());

		vendorSelection.setStatus(new Status(vendorSelectionRequest.getStatusId()));

		vendorSelection.setUpdatedDate(TimeZoneComponent.getDateByzone(vendorSelection.getUnitId()));
		vendorSelection.setUpdatedByUser(vendorSelectionRequest.getUserId());

		vendorSelectionDao.updateVendorSelection(vendorSelection);

		CommonResponse response = new CommonResponse();
		response.setMessage("Vendor Selection cancelled successfully");

		return response;
	}

}
