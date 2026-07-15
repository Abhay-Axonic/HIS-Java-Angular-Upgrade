package com.param.pharmacy.master.unit.dao.vendor;

import java.util.List;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.entity.model.master.Area;
import com.param.entity.model.master.City;
import com.param.entity.model.master.Country;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.State;
import com.param.entity.model.master.Vendor;
import com.param.global.common.Response;
import com.param.pharmacy.master.unit.dto.vendor.VendorListResponse;

public interface IVendorDao {

	List<VendorListResponse> getAll(Integer orgId , Integer unitId);

	Vendor getById(Integer id);
	Vendor getByIdAndManufacturerId(Integer id , Integer manufacturerId);

	Integer create(Vendor vendor);

	Integer update(Vendor vendor);

	Manufacturer getManufacturer(Integer id);

	PaymentMode getPaymentMode(Integer id);

	Area getArea(Integer id);

	City getCity(Integer id);

	State getState(Integer id);

	Country getCountry(Integer id);

	List<Country> getCountryList();

	List<State> getStates(Integer countryId, Integer orgId);

	List<City> getCities(Integer stateId, Integer orgId);

	List<Area> getAreas(Integer cityId, Integer orgId);

	List<PaymentMode> getPaymentModes();

	List<Manufacturer> getManufacturerList();

	Long getVendorFromManufacturer(Integer id);

	List<VendorDto> getActiveVendorList(Integer unitId);

	Integer CreateGLCode(Vendor vendor);

	Response updateVenderMasterLedgerId(VendorDto vendorDto);


}
