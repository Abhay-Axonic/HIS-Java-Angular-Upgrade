package com.param.pharmacy.master.unit.service.vendor;

import java.util.List;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.vendor.AreaDto;
import com.param.pharmacy.master.unit.dto.vendor.CityDto;
import com.param.pharmacy.master.unit.dto.vendor.CountryDto;
import com.param.pharmacy.master.unit.dto.vendor.PaymentModeDto;
import com.param.pharmacy.master.unit.dto.vendor.StateDto;
//import com.param.entity.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorDetailsResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorDropdownRespose;
import com.param.pharmacy.master.unit.dto.vendor.VendorListResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorRequest;

public interface IVendorService {

	List<VendorListResponse> getAll(Integer orgId, Integer unitId);

	VendorDetailsResponse getById(Integer id);

	CommonResponse create(VendorRequest vendorRequest);

	CommonResponse update(VendorRequest vendorRequest) throws CustomeException;

	List<CountryDto> getCountryList();

	List<StateDto> getStates(Integer countryId, Integer orgId);

	List<CityDto> getCities(Integer stateId, Integer orgId);

	List<AreaDto> getAreas(Integer cityId, Integer orgId);

	List<PaymentModeDto> getPaymentModes();

	VendorDropdownRespose getDropdowns(Integer unitId, Integer orgId);

	List<VendorDto> getActiveVendorList(Integer unitId);

	CommonResponse CreateGLCode(VendorRequest vendorRequest);

	CommonResponse checkForUniqueVendorName(VendorRequest vendorRequest)throws CustomeException;

}
