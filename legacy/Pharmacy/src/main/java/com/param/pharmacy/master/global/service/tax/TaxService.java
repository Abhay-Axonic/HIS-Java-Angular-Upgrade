package com.param.pharmacy.master.global.service.tax;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.tax.ITaxDao;
import com.param.pharmacy.master.global.dto.tax.GetTaxListResponse;
import com.param.pharmacy.master.global.dto.tax.GetTaxResponse;
import com.param.pharmacy.master.global.dto.tax.TaxDetailsResponse;
import com.param.pharmacy.master.global.dto.tax.TaxRequest;
import com.param.pharmacy.master.global.dto.tax.TaxRequestItem;
import com.param.pharmacy.master.global.dto.tax.TaxResponseList;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("TaxService")
public class TaxService implements ITaxService {

	@Autowired
	ITaxDao TaxDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<GetTaxResponse> getAll(Integer orgId) {
		List<GetTaxResponse> responseList = new ArrayList<GetTaxResponse>();

		for (Tax tax : TaxDao.getAll(orgId)) {
			GetTaxResponse taxResponse = new GetTaxResponse(tax.getId(), tax.getCode(), tax.getName(),
					tax.getTaxPercentage(), tax.getIsActive());
			taxResponse.setGeneralLedgerId(tax.getGeneralLedgerId());
			for (TaxDetail taxDetail : tax.getTaxDetailList()) {
				if (!taxDetail.getIsDeleted()) {
					taxResponse.addItems(new GetTaxListResponse(taxDetail.getId(),
							taxDetail.getTaxComponent().getComponent(), taxDetail.getTaxPercentage()));
				}
			}
			responseList.add(taxResponse);
		}
		return responseList;
	}

	@Override
	@Transactional
	public TaxDetailsResponse getById(Integer id) {
		TaxDetailsResponse responseList = new TaxDetailsResponse();

		Tax tax = TaxDao.getById(id);

		if (tax != null) {

			responseList = new TaxDetailsResponse(tax.getId(), tax.getCode(), tax.getName(), tax.getTaxPercentage(),
					tax.getIsActive());
			responseList.setGeneralLedgerId(tax.getGeneralLedgerId());
			for (TaxDetail taxDetail : tax.getTaxDetailList()) {
				if (!taxDetail.getIsDeleted()) {
					responseList.addItems(new TaxRequestItem(taxDetail.getId(), taxDetail.getTaxComponent().getId(),
							taxDetail.getTaxPercentage()));
				}
			}
			
			responseList.add(responseList);
		} else {
		}
		return responseList;
	}

	@Override
	@Transactional
	public CommonResponse updateTax(TaxRequest request) throws CustomeException {

		Tax tax = TaxDao.getTax(request.getId());
		CommonResponse response = new CommonResponse();
		response.setMessage("Tax updated successfully");

		tax.updateTax(request.getCode() != null ? request.getCode() : tax.getCode(),
				request.getName() != null ? request.getName() : tax.getName(),
				request.getTaxPercentage() != null ? request.getTaxPercentage() : tax.getTaxPercentage(),
				request.getStatus() != null ? request.getStatus() : tax.getIsActive());

		if (request.getItems() != null) {
			for (TaxDetail detail : tax.getTaxDetailList()) {
				Integer id = null;

				for (TaxRequestItem item : request.getItems()) {
					if (item.getId() != null && item.getId().equals(detail.getId())) {
						id = item.getId();
						detail.updateTaxDetail(new TaxComponent(item.getTaxComponentId()), item.getTaxPercentage());
						break;
					}
				}

				if (id == null) {
					detail.setIsDeleted(true);
				} else {
					detail.setIsDeleted(false);
				}

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(tax.getUnitId()));
				detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			}

			if (request.getItems() != null) {
				for (TaxRequestItem item : request.getItems()) {
					if (item.getId() == null) {
						tax.addTaxDetailList(
								new TaxDetail(new TaxComponent(item.getTaxComponentId()), item.getTaxPercentage()));
					}
				}
			}
		}
		for (TaxDetail detail : tax.getTaxDetailList()) {
			if(detail.getOrganizationId()==null || detail.getUnit()==null)
			{
				detail.setOrganizationId(tax.getOrganizationId());
				detail.setUnitId(tax.getUnitId());
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(tax.getUnitId()));
				detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
				detail.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
				detail.setAddedDate(TimeZoneComponent.getDateByzone(tax.getUnitId()));
			}
		}
		Long countFromItem = new Long(0);
		Long countFromTaxComponent = new Long(0);
		Boolean UnableToUpdateStatus = false;
		if (!tax.getIsActive()) {
			countFromItem = TaxDao.getTaxFromItem(request.getId());

		} else {
			countFromTaxComponent = TaxDao.getTaxFromTaxComponent(request.getId());
		}
		if (countFromItem <= 0 && countFromTaxComponent <= 0 && countFromTaxComponent <= 0) {
			UnableToUpdateStatus = false;
			tax.setUpdatedDate(TimeZoneComponent.getDateByzone(tax.getUnitId()));
			tax.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			if (request.getStatus() != null) {
				tax.setIsActive(request.getStatus());
			}
		} else if (countFromItem > 0) {
			UnableToUpdateStatus = true;
			if (request.getStatus() != null) {
				tax.setIsActive(true);
			}
			response.setMessage("Tax is assigned to Item");
		} else if (countFromTaxComponent > 0) {
			UnableToUpdateStatus = true;
			if (request.getStatus() != null) {
				tax.setIsActive(false);
			}
			response.setMessage("Tax Component is not Active");
		}
		tax.setGeneralLedgerId(request.getGeneralLedgerId());
		TaxDao.updateTax(tax);
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					tax.getIsActive());
			throw customeException;
		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse saveTax(TaxRequest request) {

		Tax tax = new Tax(request.getCode(), request.getName(), request.getTaxPercentage());
		//sessionService.setEntityDefaults(tax);

		tax.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		for (TaxRequestItem item : request.getItems()) {
			TaxDetail taxDetail=new TaxDetail(new TaxComponent(item.getTaxComponentId()), item.getTaxPercentage());
		//	sessionService.setEntityDefaults(taxDetail);
			taxDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			tax.addTaxDetailList(taxDetail);
		}
		tax.setGeneralLedgerId(request.getGeneralLedgerId());
		Tax taxresponse = TaxDao.saveTax(tax);

		CommonResponse response = new CommonResponse();
		response.setId(taxresponse.getId().longValue());
		response.setMessage("Tax saved successfully");

		return response;
	}

	@Override
	@Transactional
	public List<TaxResponseList> getDropdownList(Integer orgId) {
		List<TaxResponseList> response = new ArrayList<TaxResponseList>();

		for (Tax tax : TaxDao.getTaxList(orgId)) {
			response.add(new TaxResponseList(tax.getId(), tax.getName()));
		}
		return response;
	}

}
