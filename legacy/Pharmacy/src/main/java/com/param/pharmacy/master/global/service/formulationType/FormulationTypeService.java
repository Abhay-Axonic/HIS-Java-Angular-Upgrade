package com.param.pharmacy.master.global.service.formulationType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.FormulationType;
import com.param.global.dao.global.IGlobalDao;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.formulationType.IFormulationTypeDao;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeDetailsResponse;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeListResponse;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("FormulationTypeService")
public class FormulationTypeService implements IFormulationTypeService {

	@Autowired
	IFormulationTypeDao FormulationTypeDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<FormulationTypeListResponse> getAll(Integer orgId) {
		List<FormulationTypeListResponse> list = new ArrayList<FormulationTypeListResponse>();

		for (FormulationType formulationType : FormulationTypeDao.getAll(orgId)) {
			FormulationTypeListResponse formulationTypeRes = new FormulationTypeListResponse();

			formulationTypeRes.setId((Integer) formulationType.getId());
			formulationTypeRes.setCode((String) formulationType.getCode());
			formulationTypeRes.setType((String) formulationType.getType());
			formulationTypeRes.setStatus((Boolean) formulationType.getIsActive());
			list.add(formulationTypeRes);
		}
		return list;
	}

	@Override
	@Transactional
	public FormulationTypeDetailsResponse getById(Integer id, Integer orgId) {
		FormulationType formulationType = FormulationTypeDao.getById(id,orgId);

		FormulationTypeDetailsResponse response = new FormulationTypeDetailsResponse();
		response.setId((Integer) formulationType.getId());
		/*
		 * response.setAssetTypeId( (Integer)
		 * formulationType.getProductCategory().getAssetCategory().getAssetType().getId(
		 * )); response.setAssetCategoryId((Integer)
		 * formulationType.getProductCategory().getAssetCategory().getId());
		 * response.setProductCategoryId((Integer)
		 * formulationType.getProductCategory().getId());
		 */
		response.setCode((String) formulationType.getCode());
		response.setType((String) formulationType.getType());
		response.setStatus((Boolean) formulationType.getIsActive());
		response.setUnit(formulationType.getUnit().getUnitName());
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(FormulationTypeRequest formulationTypeRequest) {

		/*
		 * ProductCategory productCategory = new ProductCategory();
		 * productCategory.setId(formulationTypeRequest.getProductCategoryId());
		 */

		FormulationType formulationType = new FormulationType();
		formulationType.setCode(formulationTypeRequest.getCode());
		formulationType.setType(formulationTypeRequest.getType());
		formulationType.setIsActive(formulationTypeRequest.getStatus());
		// formulationType.setProductCategory(productCategory);

		CommonResponse response = new CommonResponse();
		formulationType.setCreateDefaults(formulationTypeRequest.getOrganizationId()!=null ? formulationTypeRequest.getOrganizationId() : sessionService.getOrganizationId(),formulationTypeRequest.getUnitId() !=null ? formulationTypeRequest.getUnitId() :sessionService.getUnitId() ,formulationTypeRequest.getUserId()!=null ? formulationTypeRequest.getUserId() : sessionService.getUserId());
		 
		response.setId(FormulationTypeDao.create(formulationType).longValue());
		response.setMessage("FormulationType Inserted Successfully");
		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(FormulationTypeRequest formulationTypeRequest) throws CustomeException {
		FormulationType formulationType = FormulationTypeDao.getById(formulationTypeRequest.getId(),formulationTypeRequest.getOrganizationId());

		/*
		 * if (formulationTypeRequest.getProductCategoryId() != null &&
		 * formulationTypeRequest.getProductCategoryId() > 0) {
		 * formulationType.setProductCategory(FormulationTypeDao.getId(
		 * formulationTypeRequest.getProductCategoryId())); }
		 */
		if (formulationTypeRequest.getCode() != null && formulationTypeRequest.getCode() != "") {
			formulationType.setCode(formulationTypeRequest.getCode());
		}

		if (formulationTypeRequest.getType() != null && formulationTypeRequest.getType() != "") {
			formulationType.setType(formulationTypeRequest.getType());
		}

		if (formulationTypeRequest.getStatus() != null) {
			formulationType.setIsActive(formulationTypeRequest.getStatus());
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("FormulationType Updated Successfully");
		Long countFromItem = Long.valueOf(0);
		Long countFromFormulaROA = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (!formulationType.getIsActive()) {
			countFromItem = FormulationTypeDao.getFormulationTypeCountFromItem(formulationTypeRequest.getId());
			countFromFormulaROA = FormulationTypeDao
					.getFormulationTypeCountFromFormulaROA(formulationTypeRequest.getId());
		}
		if (countFromItem <= 0 && countFromFormulaROA <= 0) {
			UnableToUpdateStatus = false;
			if (formulationTypeRequest.getStatus() != null) {
				formulationType.setUpdatedDate(TimeZoneComponent.getDateByzone(formulationType.getUnitId()));
				formulationType.setUpdatedByUser(formulationTypeRequest.getUserId()!=null ? formulationTypeRequest.getUserId() : sessionService.getUserId());
				formulationType.setIsActive(formulationTypeRequest.getStatus());
			}
		} else if (countFromFormulaROA > 0) {
			UnableToUpdateStatus = true;
			if (formulationTypeRequest.getStatus() != null) {
				formulationType.setIsActive(true);
			}
			response.setMessage("Formulation Type is assigned to Formulation Type And ROA");
		} else if (countFromItem > 0) {
			if (formulationTypeRequest.getStatus() != null) {
				formulationType.setIsActive(true);
			}
			response.setMessage("Formulation Type is assigned to Items");
		}

		response.setId(FormulationTypeDao.update(formulationType).longValue());
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					formulationType.getIsActive());
			throw customeException;
		}
		return response;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public List<FormulationTypeResponseList> getDropdownList() {
	 * List<TaxResponseList> response = new ArrayList<TaxResponseList>();
	 * 
	 * for (FormulationType formulationType :
	 * FormulationTypeDao.getFormulationTypeList()) { response.add(new
	 * FormulationTypeListResponse(formulationType.getId(),
	 * formulationType.getCode())); } return response; }
	 */

}
