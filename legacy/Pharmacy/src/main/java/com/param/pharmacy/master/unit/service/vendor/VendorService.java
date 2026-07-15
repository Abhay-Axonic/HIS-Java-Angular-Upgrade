package com.param.pharmacy.master.unit.service.vendor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.common.dto.CommonResponse;
//import com.param.entity.dto.CommonResponse;
import com.param.entity.model.master.Area;
import com.param.entity.model.master.City;
import com.param.entity.model.master.Country;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.State;
import com.param.entity.model.master.Vendor;
import com.param.entity.org.model.GeneralLedgerMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.dao.IDoctorMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.AllergyMasterDto;
import com.param.global.dto.RegistrationFieldConfigurationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.dao.IGeneralLedgerMasterServiceDao;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.item.ItemRequest;
import com.param.pharmacy.master.unit.dao.vendor.IVendorDao;
import com.param.pharmacy.master.unit.dto.vendor.AreaDto;
import com.param.pharmacy.master.unit.dto.vendor.CityDto;
import com.param.pharmacy.master.unit.dto.vendor.CountryDto;
import com.param.pharmacy.master.unit.dto.vendor.PaymentModeDto;
import com.param.pharmacy.master.unit.dto.vendor.StateDto;
import com.param.pharmacy.master.unit.dto.vendor.VendorDetailsResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorDropdownRespose;
import com.param.pharmacy.master.unit.dto.vendor.VendorListResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorRequest;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.EmrService;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("VendoService")
public class VendorService implements IVendorService {
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IVendorDao VendorDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	private IGeneralLedgerMasterServiceDao iGeneralLedgerMasterServiceDao;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<VendorListResponse> getAll(Integer orgId, Integer unitId) {
		return VendorDao.getAll(orgId, unitId);
	}
	
	@Override
	@Transactional
	public List<VendorDto> getActiveVendorList(Integer unitId){
		return VendorDao.getActiveVendorList(unitId);
	}
	
	
	@Override
	@Transactional
	public VendorDetailsResponse getById(Integer id) {
		VendorDetailsResponse response = new VendorDetailsResponse();

		Vendor vendor = VendorDao.getById(id);

		if (vendor != null) {
			response.setId((Integer) vendor.getId());
			//response.setManufacturerId((Integer) vendor.getManufacturer().getId());
			//response.setManufacturerName((String) vendor.getManufacturer().getName());
			response.setPaymentModeId((Integer) vendor.getPaymentMode().getPaymentModeId());
			response.setPaymentModeDesc((String) vendor.getPaymentMode().getPaymentModeDesc());
			response.setCode((String) vendor.getCode());
			response.setName((String) vendor.getName());
			response.setAddress((String) vendor.getAddress());
			response.setCreditDays((String) vendor.getCreditDays());

			response.setZipcode((String) vendor.getZipcode());
			response.setPaymentTerm((String) vendor.getPaymentTerm());
			response.setAreaId( vendor.getArea()!=null ? vendor.getArea().getAreaId() :null);
			response.setCityId(vendor.getCity() !=null ? vendor.getCity().getCityId() : null);
			response.setStateId(vendor.getState()!=null ? vendor.getState().getStateId():null);
			response.setCountryId(vendor.getCountry().getCountryId());

			response.setContactPersonName((String) vendor.getContactPersonName());
			response.setDrugLicenseNo((String) vendor.getDrugLicenseNo());
			response.setGstNo((String) vendor.getGstNo());
			response.setPhoneNumber((String) vendor.getPhoneNumber());
			response.setEmailId((String) vendor.getEmailId());
			response.setPaymentTerm((String) vendor.getPaymentTerm());
			response.setIsLocalPurchase((Boolean) vendor.getIsLocalPurchase());
			response.setStatus((Boolean) vendor.getIsActive());
			response.setGeneralLedgerId(vendor.getGeneralLedgerId());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(VendorRequest vendorRequest) {
		CommonResponse response = new CommonResponse();
		
		CommonResponse itemNameResponse = this.checkForUniqueVendorName(vendorRequest);
		 if(itemNameResponse.getListObject().size() > 0 && itemNameResponse.getListObject() != null) {
			    response.setMessage("Vendor Name is Already Exists.");
			    response.setCode("500");
			    return response;
			}
		 
		 
		//if (vendorRequest.getManufacturerIds() != null) {

			//for (ManufacturerIds ids : vendorRequest.getManufacturerIds()) {
				Vendor vendor = new Vendor();
			//	sessionService.setEntityDefaults(vendor);
				vendor.setCreateDefaults(vendorRequest.getOrganizationId()!=null ? vendorRequest.getOrganizationId() : sessionService.getOrganizationId(),vendorRequest.getUnitId() !=null ? vendorRequest.getUnitId() :sessionService.getUnitId() ,vendorRequest.getUserId()!=null ? vendorRequest.getUserId() : sessionService.getUserId());
				vendor.setServiceProvider(true);
				PaymentMode paymentMode = new PaymentMode();
				paymentMode.setPaymentModeId(vendorRequest.getPaymentModeId());

				Area area = new Area();
				area.setAreaId(vendorRequest.getAreaId());

				City city = new City();
				city.setCityId(vendorRequest.getCityId());

				State state = new State();
				state.setStateId(vendorRequest.getStateId());

				Country country = new Country();
				country.setCountryId(vendorRequest.getCountryId());

				
				vendor.setCode(vendorRequest.getCode());
				vendor.setName(vendorRequest.getName());
				vendor.setAddress(vendorRequest.getAddress());
				vendor.setCreditDays(vendorRequest.getCreditDays());
				vendor.setContactPersonName(vendorRequest.getContactPersonName());
				if(vendorRequest.getDrugLicenseNo()!=null)
				{
					vendor.setDrugLicenseNo(vendorRequest.getDrugLicenseNo());
				}
				if(vendorRequest.getGstNo()!=null)
				{
					vendor.setGstNo(vendorRequest.getGstNo());
				}
				
				vendor.setPhoneNumber(vendorRequest.getPhoneNumber());
				if(vendorRequest.getEmailId()!=null)
				{
					vendor.setEmailId(vendorRequest.getEmailId());
				}
				
				vendor.setPaymentTerm(vendorRequest.getPaymentTerm());
				vendor.setIsLocalPurchase(vendorRequest.getIsLocalPurchase());
				vendor.setIsActive(vendorRequest.getStatus());
			//	if (ids.getId() != null) vendor.setManufacturer(new Manufacturer(ids.getId()));
				vendor.setPaymentMode(paymentMode);
				vendor.setAddress(vendorRequest.getAddress());
				if(vendorRequest.getZipcode()!=null)
				{
					vendor.setZipcode(vendorRequest.getZipcode());
				}
				
				vendor.setPaymentTerm(vendorRequest.getPaymentTerm());
				if(vendorRequest.getAreaId()!=null)
				{
					vendor.setArea(area);
				}
				if(vendorRequest.getStateId()!=null)
				{
					vendor.setState(state);	
				}
				if(vendorRequest.getCityId()!=null)
				{
					vendor.setCity(city);
				}
				
				
				
				vendor.setCountry(country);
				vendor.setGeneralLedgerId(vendorRequest!=null && vendorRequest.getGeneralLedgerId()!=null ? vendorRequest.getGeneralLedgerId():0);
				response.setId(VendorDao.create(vendor).longValue());
				response.setCode("200");
				response.setMessage("Vendor Inserted Successfully");

				vendor.setId(response.getId().intValue());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("create");
				payload.setData(vendor);
				request.setKey(MasterKeyConstants.VendorMasterHISKey.toString());
				request.setPayload(payload);
				IEmrService emrService = new EmrService();
				//EmrService.publish(request);
			//}
		//}

		return response;
	}
	
	@Override
	@Transactional
	public CommonResponse checkForUniqueVendorName(VendorRequest vendorRequest) {
		try {
			String sql = " select ven.id "
					+ " from  public.m_vendor ven "
					+ " where ven.organization_id =:orgId "
					+ " and ven.unit_id =:unitId "
					+ " and LOWER(ven.name) Like :name ";
			
			List<VendorRequest> vendorRequestList = sessionFactory.getCurrentSession()
					.createSQLQuery(sql)
					.setParameter("orgId", vendorRequest.getOrganizationId())
					.setParameter("unitId", vendorRequest.getUnitId())
					.setParameter("name", "%" + vendorRequest.getName().toLowerCase()+ "%")
					.setResultTransformer(Transformers.aliasToBean(VendorRequest.class)).list();
			
			return new CommonResponse<>("success", "200", null, vendorRequestList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public CommonResponse update(VendorRequest vendorRequest) throws CustomeException {
		CommonResponse response = new CommonResponse();

		
		
		//for (ManufacturerIds ids : vendorRequest.getManufacturerIds()) {
			
			//Vendor vendor = VendorDao.getByIdAndManufacturerId(vendorRequest.getId() , ids.getId());
			Vendor vendor = VendorDao.getById(vendorRequest.getId());
			if(vendor!=null)
			{
				/*if (vendorRequest.getManufacturerId() != null && vendorRequest.getManufacturerId() > 0) {
					Manufacturer manufacturer = VendorDao.getManufacturer(vendorRequest.getManufacturerId());
					vendor.setManufacturer(manufacturer);
				}*/

				if (vendorRequest.getPaymentModeId() != null && vendorRequest.getPaymentModeId() > 0) {
					PaymentMode paymentMode = VendorDao.getPaymentMode(vendorRequest.getPaymentModeId());
					vendor.setPaymentMode(paymentMode);
				}

				if (vendorRequest.getAreaId() != null && vendorRequest.getAreaId() > 0) {
					Area area = VendorDao.getArea(vendorRequest.getAreaId());
					vendor.setArea(area);
				}

				if (vendorRequest.getCityId() != null && vendorRequest.getCityId() > 0) {
					City city = VendorDao.getCity(vendorRequest.getCityId());
					vendor.setCity(city);
				}

				if (vendorRequest.getStateId() != null && vendorRequest.getStateId() > 0) {
					State state = VendorDao.getState(vendorRequest.getStateId());
					vendor.setState(state);
				}

				if (vendorRequest.getCountryId() != null && vendorRequest.getCountryId() > 0) {
					Country country = VendorDao.getCountry(vendorRequest.getCountryId());
					vendor.setCountry(country);
				}

				if (vendorRequest.getZipcode() != null && vendorRequest.getZipcode() != "") {
					vendor.setZipcode(vendorRequest.getZipcode());
				}

				if (vendorRequest.getPaymentTerm() != null && vendorRequest.getPaymentTerm() != "") {
					vendor.setPaymentTerm(vendorRequest.getPaymentTerm());
				}

				if (vendorRequest.getCode() != null && vendorRequest.getCode() != "") {
					vendor.setCode(vendorRequest.getCode());
				}

				if (vendorRequest.getName() != null && vendorRequest.getName() != "") {
					vendor.setName(vendorRequest.getName());
				}

				if (vendorRequest.getAddress() != null && vendorRequest.getAddress() != "") {
					vendor.setAddress(vendorRequest.getAddress());
				}

				if (vendorRequest.getContactPersonName() != null && vendorRequest.getContactPersonName() != "") {
					vendor.setContactPersonName(vendorRequest.getContactPersonName());
				}

				if (vendorRequest.getDrugLicenseNo() != null && vendorRequest.getDrugLicenseNo() != "") {
					vendor.setDrugLicenseNo(vendorRequest.getDrugLicenseNo());
				}

				if (vendorRequest.getGstNo() != null && vendorRequest.getGstNo() != "") {
					vendor.setGstNo(vendorRequest.getGstNo());
				}

				if (vendorRequest.getPhoneNumber() != null && vendorRequest.getPhoneNumber() != "") {
					vendor.setPhoneNumber(vendorRequest.getPhoneNumber());
				}

				if (vendorRequest.getEmailId() != null && vendorRequest.getEmailId() != "") {
					vendor.setEmailId(vendorRequest.getEmailId());
				}

				if (vendorRequest.getPaymentTerm() != null) {
					vendor.setPaymentTerm(vendorRequest.getPaymentTerm());
				}

				if (vendorRequest.getIsLocalPurchase() != null) {
					vendor.setIsLocalPurchase(vendorRequest.getIsLocalPurchase());
				}

				if (vendorRequest.getStatus() != null) {
					vendor.setIsActive(vendorRequest.getStatus());
				}

				
				Long countFromManufacture = new Long(0);
				Boolean UnableToUpdateStatus = false;
				/*if (!vendor.getIsActive()) {
					countFromManufacture = VendorDao.getVendorFromManufacturer(vendorRequest.getId());
				}*/
				if (countFromManufacture <= 0) {
					UnableToUpdateStatus = false;
					vendor.setUpdatedDate(TimeZoneComponent.getDateByzone(vendor.getUnitId()));
					vendor.setUpdatedByUser(vendorRequest.getUserId()!=null ? vendorRequest.getUserId() : sessionService.getUserId());
					if (vendorRequest.getStatus() != null) {
						vendor.setIsActive(vendorRequest.getStatus());
					}
				} else {/*
					UnableToUpdateStatus = true;
					if (vendorRequest.getStatus() != null) {
						vendor.setIsActive(false);
					}
					response.setMessage("Manufacturer is not Active");
				*/}
				vendor.setCreditDays(vendorRequest.getCreditDays()!=null ? vendorRequest.getCreditDays() :"0");
				vendor.setGeneralLedgerId(vendorRequest.getGeneralLedgerId()!=null ? vendorRequest.getGeneralLedgerId():0);
				response.setId(VendorDao.update(vendor).longValue());
				if (UnableToUpdateStatus) {
					CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
							vendor.getIsActive());
					throw customeException;
				}	
			}
			else
			{/*

				Vendor vendor1 = new Vendor();
			//	sessionService.setEntityDefaults(vendor1);
				vendor1.setCreateDefaults(vendorRequest.getOrganizationId()!=null ? vendorRequest.getOrganizationId() : sessionService.getOrganizationId(),vendorRequest.getUnitId() !=null ? vendorRequest.getUnitId() :sessionService.getUnitId() ,vendorRequest.getUserId()!=null ? vendorRequest.getUserId() : sessionService.getUserId());
				vendor1.setServiceProvider(true);
				PaymentMode paymentMode = new PaymentMode();
				paymentMode.setPaymentModeId(vendorRequest.getPaymentModeId());

				Area area = new Area();
				area.setAreaId(vendorRequest.getAreaId());

				City city = new City();
				city.setCityId(vendorRequest.getCityId());

				State state = new State();
				state.setStateId(vendorRequest.getStateId());

				Country country = new Country();
				country.setCountryId(vendorRequest.getCountryId());

				
				vendor1.setCode(vendorRequest.getCode());
				vendor1.setName(vendorRequest.getName());
				vendor1.setAddress(vendorRequest.getAddress());
				vendor1.setContactPersonName(vendorRequest.getContactPersonName());
				if(vendorRequest.getDrugLicenseNo()!=null)
				{
					vendor1.setDrugLicenseNo(vendorRequest.getDrugLicenseNo());
				}
				if(vendorRequest.getGstNo()!=null)
				{
					vendor1.setGstNo(vendorRequest.getGstNo());
				}
				
				vendor1.setPhoneNumber(vendorRequest.getPhoneNumber());
				if(vendorRequest.getEmailId()!=null)
				{
					vendor1.setEmailId(vendorRequest.getEmailId());
				}
				
				vendor1.setPaymentTerm(vendorRequest.getPaymentTerm());
				vendor1.setIsLocalPurchase(vendorRequest.getIsLocalPurchase());
				vendor1.setIsActive(true);
				if (ids.getId() != null)
					vendor1.setManufacturer(new Manufacturer(ids.getId()));
				vendor1.setPaymentMode(paymentMode);
				vendor1.setAddress(vendorRequest.getAddress());
				if(vendorRequest.getZipcode()!=null)
				{
					vendor1.setZipcode(vendorRequest.getZipcode());
				}
				
				vendor1.setPaymentTerm(vendorRequest.getPaymentTerm());
				if(vendorRequest.getAreaId()!=null)
				{
					vendor1.setArea(area);
				}
				if(vendorRequest.getStateId()!=null)
				{
					vendor1.setState(state);	
				}
				if(vendorRequest.getCityId()!=null)
				{
					vendor1.setCity(city);
				}
				
				
				
				vendor1.setCountry(country);

				response.setId(VendorDao.create(vendor1).longValue());
				 //EmrService.publish(request);
				
			*/}
			
		//}

		
		response.setMessage("Vendor Updated Successfully");

		return response;
	}

	@Override
	@Transactional
	public List<CountryDto> getCountryList() {
		List<CountryDto> response = new ArrayList<CountryDto>();

		for (com.param.entity.model.master.Country country : VendorDao.getCountryList()) {
			response.add(new CountryDto(country.getCountryId(), country.getCountryName()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<StateDto> getStates(Integer countryId,Integer orgId) {
		List<StateDto> response = new ArrayList<StateDto>();

		for (State state : VendorDao.getStates(countryId,orgId)) {
			response.add(new StateDto(state.getStateId(), state.getStateName()));

		}
		return response;
	}

	@Override
	@Transactional
	public List<CityDto> getCities(Integer stateId, Integer orgId) {
		List<CityDto> response = new ArrayList<CityDto>();

		for (City city : VendorDao.getCities(stateId,orgId)) {
			response.add(new CityDto(city.getCityId(), city.getCityName()));

		}
		return response;
	}

	@Override
	@Transactional
	public List<AreaDto> getAreas(Integer cityId,Integer orgId) {
		List<AreaDto> response = new ArrayList<AreaDto>();

		for (Area area : VendorDao.getAreas(cityId,orgId)) {
			response.add(new AreaDto(area.getAreaId(), area.getAreaName()));

		}
		return response;
	}

	@Override
	@Transactional
	public List<PaymentModeDto> getPaymentModes() {
		List<PaymentModeDto> response = new ArrayList<PaymentModeDto>();

		for (PaymentMode paymentMode : VendorDao.getPaymentModes()) {
			response.add(new PaymentModeDto(paymentMode.getPaymentModeId(), paymentMode.getPaymentModeDesc()));

		}
		return response;
	}

	@Override
	@Transactional
	public VendorDropdownRespose getDropdowns(Integer unitId, Integer orgId) {
		VendorDropdownRespose response = new VendorDropdownRespose();

		for (com.param.entity.model.master.Country country : VendorDao.getCountryList()) {
			response.addCountry(new CountryDto(country.getCountryId(), country.getCountryName()));
		}

		for (PaymentMode paymentMode : VendorDao.getPaymentModes()) {
			response.addPaymentMode(
					new PaymentModeDto(paymentMode.getPaymentModeId(), paymentMode.getPaymentModeDesc()));
		}

		for (Manufacturer manufacturer : GlobalDao.getManufacturerList(orgId)) {
			response.addManufacturer(
					new com.param.global.dto.generic.Manufacturer(manufacturer.getId(), manufacturer.getName()));
		}
		Response res = iUnitApplicationBillingConfigurationDao.getUnitApplicationRegistrationFieldConfig(
				sessionService.getOrganizationId(), sessionService.getUnitId());
		List<RegistrationFieldConfigurationDto> registrationFieldConfiguration = (List<RegistrationFieldConfigurationDto>) res
				.getListObject();
		if (registrationFieldConfiguration.size() > 0) {
			response.setMobileDigit(registrationFieldConfiguration.get(0).getMobileDigit());
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse CreateGLCode(VendorRequest vendorRequest) {
		
		 GeneralLedgerMasterDto generalLedgerMasterDto = new GeneralLedgerMasterDto();
		 String vendorName =null;
		 Response res = null;
		 vendorName = vendorRequest.getName();
		 generalLedgerMasterDto.setGeneralLedgerName(vendorName);
		 generalLedgerMasterDto.setGeneralLedgerCode(vendorRequest.getCode());
		 generalLedgerMasterDto.setCreatedBy(vendorRequest.getUserId());
		 generalLedgerMasterDto.setUpdatedBy(vendorRequest.getUserId());
		 generalLedgerMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(vendorRequest.getUnitId()), "dd-M-yyyy HH:mm:ss"));
		 generalLedgerMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(vendorRequest.getUnitId()), "dd-M-yyyy HH:mm:ss"));
		 generalLedgerMasterDto.setStatus('A');
		 generalLedgerMasterDto.setOrganizationId(vendorRequest.getOrganizationId());
		 
		   Response responseS = iGeneralLedgerMasterServiceDao.getGeneralLedgerMasterByName(generalLedgerMasterDto);
		 
			if (responseS.getListObject().size() > 0 && responseS.getListObject() != null) {
				
				List<GeneralLedgerMasterDto> generalLedgerMasterDto1 = responseS.getListObject();
				for (GeneralLedgerMasterDto dto : generalLedgerMasterDto1) 
				{
					generalLedgerMasterDto.setGeneralLedgerId(dto.getGeneralLedgerId());
				}
			} else {
				 Response saveGenResponse = iGeneralLedgerMasterServiceDao.saveGeneralLedgerMaster(generalLedgerMasterDto);
				 GeneralLedgerMaster generalLedgerMaster = (com.param.entity.org.model.GeneralLedgerMaster)saveGenResponse.getObject();
				 generalLedgerMasterDto.setGeneralLedgerId(generalLedgerMaster.getGeneralLedgerId());
			}
		 
	        	CommonResponse response = new CommonResponse();
				Vendor vendor = new Vendor();
				vendor.setCreateDefaults(vendorRequest.getOrganizationId()!=null ? vendorRequest.getOrganizationId() : sessionService.getOrganizationId(),vendorRequest.getUnitId() !=null ? vendorRequest.getUnitId() :sessionService.getUnitId() ,vendorRequest.getUserId()!=null ? vendorRequest.getUserId() : sessionService.getUserId());
				vendor.setServiceProvider(true);
				PaymentMode paymentMode = new PaymentMode();
				paymentMode.setPaymentModeId(vendorRequest.getPaymentModeId());

				Area area = new Area();
				area.setAreaId(vendorRequest.getAreaId());

				City city = new City();
				city.setCityId(vendorRequest.getCityId());

				State state = new State();
				state.setStateId(vendorRequest.getStateId());

				Country country = new Country();
				country.setCountryId(vendorRequest.getCountryId());

				
				vendor.setCode(vendorRequest.getCode());
				vendor.setName(vendorRequest.getName());
				vendor.setAddress(vendorRequest.getAddress());
				vendor.setCreditDays(vendorRequest.getCreditDays());
				vendor.setContactPersonName(vendorRequest.getContactPersonName());
				if(vendorRequest.getDrugLicenseNo()!=null)
				{
					vendor.setDrugLicenseNo(vendorRequest.getDrugLicenseNo());
				}
				if(vendorRequest.getGstNo()!=null)
				{
					vendor.setGstNo(vendorRequest.getGstNo());
				}
				
				vendor.setPhoneNumber(vendorRequest.getPhoneNumber());
				if(vendorRequest.getEmailId()!=null)
				{
					vendor.setEmailId(vendorRequest.getEmailId());
				}
				
				vendor.setPaymentTerm(vendorRequest.getPaymentTerm());
				vendor.setIsLocalPurchase(vendorRequest.getIsLocalPurchase());
				vendor.setIsActive(vendorRequest.getStatus());
			//	if (ids.getId() != null) vendor.setManufacturer(new Manufacturer(ids.getId()));
				vendor.setPaymentMode(paymentMode);
				vendor.setAddress(vendorRequest.getAddress());
				if(vendorRequest.getZipcode()!=null)
				{
					vendor.setZipcode(vendorRequest.getZipcode());
				}
				
				vendor.setPaymentTerm(vendorRequest.getPaymentTerm());
				if(vendorRequest.getAreaId()!=null)
				{
					vendor.setArea(area);
				}
				if(vendorRequest.getStateId()!=null)
				{
					vendor.setState(state);	
				}
				if(vendorRequest.getCityId()!=null)
				{
					vendor.setCity(city);
				}
				
				
				
				vendor.setCountry(country);

				response.setId(VendorDao.CreateGLCode(vendor).longValue());
				response.setMessage("Vendor Inserted Successfully");

				vendor.setId(response.getId().intValue());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("create");
				payload.setData(vendor);
				request.setKey(MasterKeyConstants.VendorMasterHISKey.toString());
				request.setPayload(payload);
				IEmrService emrService = new EmrService();
				//EmrService.publish(request);
			//}
		//}
				VendorDto vendorDto = new VendorDto();
				  //update doctor master
				  vendorDto.setVendorId(response.getId().intValue());
				  vendorDto.setGeneralLedgerId(generalLedgerMasterDto.getGeneralLedgerId());
				 Response bvdf = VendorDao.updateVenderMasterLedgerId(vendorDto);
				 
		return response;
	}

}
