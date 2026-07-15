package com.param.pharmacy.master.unit.dao.vendor;

import java.util.List;

import com.param.pharmacy.master.unit.dto.vendor.VendorListResponse;
import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Area;
import com.param.entity.model.master.City;
import com.param.entity.model.master.Country;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.State;
import com.param.entity.model.master.Vendor;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@Repository("VendorDao")
public class VendorDao extends AbstractDao<Integer, Vendor> implements IVendorDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(VendorDao.class);


	@Override
	public List<VendorListResponse> getAll(Integer orgId, Integer unitId) {
		String hql =
				"	SELECT vendor.id AS id, manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, "
				+ "		paymentMode.id AS paymentModeId, paymentMode.paymentModeDesc AS paymentModeDesc, "
				+ "		vendor.code AS code, vendor.name AS name, vendor.address AS address, country.countryName AS country, "
				+ "		city.cityName AS city, state.stateName AS state, area.areaName AS area, vendor.zipcode AS zipcode, "
				+ "		vendor.contactPersonName AS contactPersonName, vendor.drugLicenseNo AS drugLicenseNo, vendor.gstNo AS gstNo, "
				+ "		vendor.phoneNumber As phoneNumber, vendor.emailId AS emailId, vendor.paymentTerm AS paymentTerm, "
				+ " 	vendor.isLocalPurchase AS isLocalPurchase, vendor.isActive AS status,vendor.generalLedgerId as generalLedgerId "
				+ "	FROM Vendor AS vendor "
				+ "		LEFT JOIN vendor.manufacturer AS manufacturer WITH manufacturer.isDeleted = false "
				+ "		INNER JOIN vendor.paymentMode AS paymentMode "
				+ " 	INNER JOIN vendor.country AS country "
				+ " 	left JOIN vendor.city AS city "
				+ " 	left JOIN vendor.state AS state "
				+ " 	left JOIN vendor.area AS area "
                + " WHERE vendor.isDeleted = false AND "
                + " vendor.organization.organizationId =:organizationId "
				+ " AND vendor.unit.unitId =:unitId "
				+ "	ORDER BY vendor.addedDate desc";

		Query query = getSession().createQuery(hql);
        query.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(VendorListResponse.class));

		return query.list();
	}
	
	
	@Override
	public List<VendorDto> getActiveVendorList(Integer unitId) {

		String hql = "SELECT vendor.id as vendorId, vendor.name as name FROM Vendor as vendor "
				+ " WHERE vendor.isActive = true AND vendor.isDeleted = false and vendor.unit.unitId = :unitId "
				+ "  ORDER BY vendor.name ASC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId);
		query.setResultTransformer(Transformers.aliasToBean(VendorDto.class));
		return query.list();

	}

	@Override
	public Vendor getById(Integer id) {
		Criteria criteria = getSession().createCriteria(Vendor.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (Vendor) criteria.uniqueResult();
	}

	@Override
	public Integer create(Vendor vendor) {
		getSession().save(vendor);
		return vendor.getId();
	}

	@Override
	public Integer update(Vendor vendor) {
		getSession().update(vendor);
		;
		return vendor.getId();
	}

	@Override
	public Manufacturer getManufacturer(Integer id) {
		return (Manufacturer) getSession().get(Manufacturer.class, id);
	}

	@Override
	public PaymentMode getPaymentMode(Integer id) {
		return (PaymentMode) getSession().get(PaymentMode.class, id);
	}

	@Override
	public Area getArea(Integer id) {
		return (Area) getSession().get(Area.class, id);
	}

	@Override
	public City getCity(Integer id) {
		return (City) getSession().get(City.class, id);
	}

	@Override
	public State getState(Integer id) {
		return (State) getSession().get(State.class, id);
	}

	@Override
	public Country getCountry(Integer id) {
		return (Country) getSession().get(Country.class, id);
	}

	@Override
	public List<Country> getCountryList() {
		String hql = "SELECT country FROM Country AS country where (country.organization.organizationId is null or country.organization.organizationId = :organizationId ) \r\n" + "ORDER BY country.countryName";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public List<State> getStates(Integer countryId,Integer orgId) {
		String hql = "SELECT state FROM State AS state "
				+ "	INNER JOIN state.country as country WITH country.countryId = :countryId  where  (state.organization.organizationId is null or state.organization.organizationId = :organizationId)  "
				+ "ORDER BY state.stateName ";

		Query query = getSession().createQuery(hql);
		query.setParameter("countryId", countryId);
		query.setParameter("organizationId", orgId!=null ? orgId: sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<City> getCities(Integer stateId,Integer orgId) {
		String hql = "SELECT city FROM City AS city "
				+ "	INNER JOIN city.state as state WITH state.stateId = :stateId  where (city.organization.organizationId is null or city.organization.organizationId = :organizationId) " + "ORDER BY city.cityName ";

		Query query = getSession().createQuery(hql);
		query.setParameter("stateId", stateId);
		query.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<Area> getAreas(Integer cityId,Integer orgId) {
		String hql = "SELECT area FROM Area AS area " + "	INNER JOIN area.city as city WITH city.cityId = :cityId "
				+ " where (area.organization.organizationId is null or area.organization.organizationId = :organizationId) ORDER BY area.areaName ";

		Query query = getSession().createQuery(hql);
		query.setParameter("cityId", cityId);
		query.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

	@Override
	public List<PaymentMode> getPaymentModes() {
		String hql = "SELECT paymentMode FROM PaymentMode AS paymentMode \r\n" + "ORDER BY paymentMode.paymentModeDesc";

		Query query = getSession().createQuery(hql);
		return query.list();
	}

	public java.util.List<Manufacturer> getManufacturerList() {
		String hql = "SELECT manufacturer FROM Manufacturer AS manufacturer \r\n"
				+ "WHERE manufacturer.isActive = true AND manufacturer.isDeleted = false AND manufacturer.organization.organizationId = :organizationId\r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public Long getVendorFromManufacturer(Integer id) {
		String hql = " SELECT count(manufacturer) from Manufacturer as manufacturer "
				+ "INNER JOIN manufacturer.vendors As vendor WITH vendor.id = :id AND vendor.isDeleted = false "
				+ "WHERE manufacturer.isActive = false AND manufacturer.isDeleted = false " +
				" AND manufacturer.organization.organizationId =:organizationId";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId",sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Vendor getByIdAndManufacturerId(Integer id, Integer manufacturerId) {

		String hql = "SELECT vendor FROM Vendor AS vendor inner join vendor.manufacturer as manufacturer \r\n" 
				+ " WHERE vendor.id=:id AND  manufacturer.id =:manufacturerId  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("manufacturerId", manufacturerId);
		List<Vendor>  vendorList =query.list();
		if(vendorList.size()>0)
		{
			return vendorList.get(0);
			
		}
		else
		{
			return null;
		}
	
	}


	@Override
	public Integer CreateGLCode(Vendor vendor) {
		getSession().save(vendor);
		return vendor.getId();
	}


	@Override
	public Response updateVenderMasterLedgerId(VendorDto vendorDto) throws ApplicationException {
		try{
			 sessionFactory.getCurrentSession().createSQLQuery("UPDATE public.m_vendor  set general_ledger_id=:generalLedgerId "
						+ "WHERE id=:id ")
				.setParameter("id", vendorDto.getVendorId())
				.setParameter("generalLedgerId", vendorDto.getGeneralLedgerId())
			.executeUpdate();
			
			return null;
			
			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			}
	}
}
