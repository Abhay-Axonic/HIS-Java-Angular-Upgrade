package com.param.pharmacy.master.global.service.unitOfMeasurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.postgresql.jdbc2.ArrayAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.UnitOfMeasurement;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.global.GlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.pharmacy.master.global.dao.unitOfMeasurement.IUnitOfMeasurementDao;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UOMResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementDetailsResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementListResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementRequest;
import com.param.service.global.SessionService;

@Service("UnitOfMeasurementService")
public class UnitOfMeasurementService implements IUnitOfMeasurementService, ICommonConstants {

	@Autowired
	IUnitOfMeasurementDao UnitOfMeasurementDao;

	@Autowired
	GlobalDao globalDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public Response getAll(Integer orgId, Integer offset, Integer noOfRecordsPerPage, String itemName) {

		List<UnitOfMeasurementListResponse> uomList = new ArrayList<UnitOfMeasurementListResponse>();

		List<UnitOfMeasurementListResponse> uoms = UnitOfMeasurementDao.getAllUoms(orgId, offset, noOfRecordsPerPage,
				itemName);
		List<Integer> itemIds = uoms.stream().map(x -> x.getItemId()).collect(Collectors.toList());
		List<UomResponse> list1 = UnitOfMeasurementDao.getAllByItemIds(itemIds);

		for (UnitOfMeasurementListResponse uom : uoms) {
			List<UomResponse> uomByItemId = list1.stream().filter(x -> x.getItemId().equals(uom.getItemId()))
					.collect(Collectors.toList());
			uom.setUomList(uomByItemId);
			uomList.add(uom);
		}

		Integer uomsCount = UnitOfMeasurementDao.getAllUomsCount(orgId);
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, uomList, uomsCount);

		

	}

	@Override
	@Transactional
	public UnitOfMeasurementDetailsResponse getById(Integer id, Integer orgId) {
		UnitOfMeasurementDetailsResponse response = new UnitOfMeasurementDetailsResponse();

		UnitOfMeasurement unitOfMeasurement = UnitOfMeasurementDao.getById(id, orgId);

		if (unitOfMeasurement != null) {
			response.setId((Integer) unitOfMeasurement.getId());
			response.setItemId((Integer) unitOfMeasurement.getItem().getId());
			response.setItemCode((String) unitOfMeasurement.getItem().getCode());
			response.setItemName((String) unitOfMeasurement.getItem().getName());
			response.setProductCategoryId(
					(Integer) unitOfMeasurement.getItem().getGeneric().getProductCategory().getId());
			response.setProductCategory(
					(String) unitOfMeasurement.getItem().getGeneric().getProductCategory().getCategory());
			response.setAssetCategoryId(
					(Integer) unitOfMeasurement.getItem().getGeneric().getProductCategory().getAssetCategory().getId());
			response.setAssetCategory((String) unitOfMeasurement.getItem().getGeneric().getProductCategory()
					.getAssetCategory().getCategory());
			response.setStatus((Boolean) unitOfMeasurement.getIsActive());

		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public List<UnitOfMeasurementListResponse> getUomByItemId(Integer itemId, Integer orgId) {
		List<UnitOfMeasurementListResponse> unitOfMeasurementList = UnitOfMeasurementDao.getByItemId(itemId, orgId);

	
		List<UomResponse> uomResponse = UnitOfMeasurementDao.getAllByItemIds(Arrays.asList(itemId));
		for (UnitOfMeasurementListResponse uom : unitOfMeasurementList) {
			uom.setUomList(uomResponse);
		}

		

		return unitOfMeasurementList;
	}

	@Override
	@Transactional
	public CommonResponse create(List<UnitOfMeasurementRequest> unitOfMeasurementRequestList, Integer userId,
			Integer orgId) {

		CommonResponse response = new CommonResponse();
		for (UnitOfMeasurementRequest unitOfMeasurementRequest : unitOfMeasurementRequestList) {

			Item item = new Item();
			item.setCreateDefaults(
					unitOfMeasurementRequest.getOrganizationId() != null ? unitOfMeasurementRequest.getOrganizationId()
							: sessionService.getOrganizationId(),
					unitOfMeasurementRequest.getUnitId() != null ? unitOfMeasurementRequest.getUnitId()
							: sessionService.getUnitId(),
					unitOfMeasurementRequest.getUserId() != null ? unitOfMeasurementRequest.getUserId()
							: sessionService.getUserId());
			item.setId(unitOfMeasurementRequest.getItemId());

			List<UOMResponse> uomList = unitOfMeasurementRequest.getUomList();
			for (UOMResponse uomResp : uomList) {

				UnitOfMeasurement UOMList = UnitOfMeasurementDao.getUOMByItem(unitOfMeasurementRequest.getItemId(),
						uomResp.getUomTypeId(), orgId);

				if (UOMList != null) {

					UOMList.updateunitOfMeasurement(UOMList.getId(), new UomType(uomResp.getUomTypeId()),

							new UomUnit(uomResp.getUomUnitId()), uomResp.getIpUom(), uomResp.getOpUom(),
							uomResp.getConversion(), uomResp.getStoreUom(), uomResp.getStatus());

					UnitOfMeasurementDao.update(UOMList);

					response.setMessage("Unit Of Measurement Updated Successfully");

				}
				if (UOMList == null) {

					UnitOfMeasurement unitOfMeasurement1 = new UnitOfMeasurement(item,
							new UomType(uomResp.getUomTypeId()), new UomUnit(uomResp.getUomUnitId()),
							uomResp.getIpUom(), uomResp.getOpUom(), uomResp.getConversion(), uomResp.getStoreUom(),
							uomResp.getStatus());
					// sessionService.setEntityDefaults(unitOfMeasurement1);
					unitOfMeasurement1.setCreateDefaults(
							unitOfMeasurementRequest.getOrganizationId() != null
									? unitOfMeasurementRequest.getOrganizationId()
									: sessionService.getOrganizationId(),
							unitOfMeasurementRequest.getUnitId() != null ? unitOfMeasurementRequest.getUnitId()
									: sessionService.getUnitId(),
							unitOfMeasurementRequest.getUserId() != null ? unitOfMeasurementRequest.getUserId()
									: sessionService.getUserId());
					response.setId(UnitOfMeasurementDao.create(unitOfMeasurement1).longValue());
					response.setMessage("Unit Of Measurement Inserted Successfully");

				}

			}

		}
		return response;
	}

	@Override
	public List<UnitOfMeasurementListResponse> getByitemId(Integer itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public CommonResponse update(UnitOfMeasurementRequest uomRequest) {
		List<UnitOfMeasurement> uomList = UnitOfMeasurementDao.getAllByItemId(uomRequest.getItemId(),
				uomRequest.getOrganizationId());

		for (UnitOfMeasurement uom : uomList) {
			for (UOMResponse uomResponse : uomRequest.getUomList()) {
				if (uom.getId().equals(uomResponse.getId())) {
					uom.updateUOM(
							new UomType(uomResponse.getUomTypeId() != null ? uomResponse.getUomTypeId()
									: uom.getUomType().getId()),
							new UomUnit(uomResponse.getUomUnitId() != null ? uomResponse.getUomUnitId()
									: uom.getUnit().getUnitId()),
							uomResponse.getConversion() != null ? uomResponse.getConversion() : uom.getConversions(),
							uomResponse.getIpUom() != null ? uomResponse.getIpUom() : uom.getIpUom(),
							uomResponse.getOpUom() != null ? uomResponse.getOpUom() : uom.getOpUom(),
							uomResponse.getStoreUom() != null ? uomResponse.getStoreUom() : uom.getStoreUom());
					uom.setUpdateDefaults(uom.getUpdatedByUser() != null ?uom.getUpdatedByUser()
							: sessionService.getUserId(), uom.getUnitId() != null ? uom.getUnitId()
							: sessionService.getUnitId());
					UnitOfMeasurementDao.update(uom);
				}
			}
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Unit Of Measurement Updated Successfully");
		return response;
	}

	@Override
	public CommonResponse update(UnitOfMeasurementRequest unitOfMeasurementRequest, Integer itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonResponse update1(UnitOfMeasurementRequest uomRequest, Item item) {

		List<UnitOfMeasurement> uomList = UnitOfMeasurementDao.getAllByItemId(uomRequest.getItemId(),
				item.getOrganizationId());
		if (!uomList.isEmpty()) {
			for (UnitOfMeasurement uom : uomList) {
				for (UOMResponse uomResponse : uomRequest.getUomList()) {
					if (uom.getId().equals(uomResponse.getId())) {
						uom.updateUOM(
								new UomType(uomResponse.getUomTypeId() != null ? uomResponse.getUomTypeId()
										: uom.getUomType().getId()),
								new UomUnit(uomResponse.getUomUnitId() != null ? uomResponse.getUomUnitId()
										: uom.getUnit().getUnitId()),
								uomResponse.getConversion() != null ? uomResponse.getConversion()
										: uom.getConversions(),
								uomResponse.getIpUom() != null ? uomResponse.getIpUom() : uom.getIpUom(),
								uomResponse.getOpUom() != null ? uomResponse.getOpUom() : uom.getOpUom(),
								uomResponse.getStoreUom() != null ? uomResponse.getStoreUom() : uom.getStoreUom());
						uom.setUpdateDefaults(item.getUpdatedByUser() != null ?item.getUpdatedByUser()
									: sessionService.getUserId(), item.getUnitId() != null ? item.getUnitId()
									: sessionService.getUnitId());
						UnitOfMeasurementDao.update(uom);
					}
				}
			}
		} else {
			List<UOMResponse> uomList1 = uomRequest.getUomList();
			for (UOMResponse uomResp : uomList1) {
 
				 UnitOfMeasurement unitOfMeasurement1 = new UnitOfMeasurement(item,
							new UomType(uomResp.getUomTypeId()), new UomUnit(uomResp.getUomUnitId()),
							uomResp.getIpUom(), uomResp.getOpUom(), uomResp.getConversion(), uomResp.getStoreUom(),
							uomResp.getStatus());
					// sessionService.setEntityDefaults(unitOfMeasurement1);
					unitOfMeasurement1.setCreateDefaults(
							item.getOrganizationId() != null
									? item.getOrganizationId()
									: sessionService.getOrganizationId(),
									item.getUnitId() != null ? item.getUnitId()
									: sessionService.getUnitId(),
									item.getUpdatedByUser() != null ?item.getUpdatedByUser()
									: sessionService.getUserId());
					UnitOfMeasurementDao.create(unitOfMeasurement1).longValue();
					 

			}
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Unit Of Measurement Updated Successfully");
		return response;

	}

}
