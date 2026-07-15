package com.param.pharmacy.master.unit.dao.pharmacyTariff;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.unit.dto.pharmacyTariff.PharmacyTariffDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PharmacyTariffDaoImpl implements IPharmacyTariffDao,ICommonConstants{

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(PharmacyTariffDaoImpl.class);

	@Override
	public Response savePharmacyTariff(PharmacyTariffDto pharmacyTariffDto) throws ApplicationException {
		try{
			PharmacyTariff pharmacyTariff = new PharmacyTariff();
				pharmacyTariff.setAssetCategoryId(pharmacyTariffDto.getAssetCategoryId());
				pharmacyTariff.setProductCategoryId(pharmacyTariffDto.getProductCategoryId());
				pharmacyTariff.setMarkUpDown(pharmacyTariffDto.getMarkUpDown());
				pharmacyTariff.setMarkUpDownPer(pharmacyTariffDto.getMarkUpDownPer());
				pharmacyTariff.setPaymentEntitlementId(pharmacyTariffDto.getPaymentEntitlementId());
				pharmacyTariff.setStatus('A');
				pharmacyTariff.setCreatedDate(TimeZoneComponent.getDateByzone(pharmacyTariffDto.getUnitId()));
				pharmacyTariff.setUpdatedDate(TimeZoneComponent.getDateByzone(pharmacyTariffDto.getUnitId()));
				pharmacyTariff.setCreatedBy(pharmacyTariffDto.getCreatedBy());
				pharmacyTariff.setUpdatedBy(pharmacyTariffDto.getCreatedBy());
				pharmacyTariff.setOrganizationId(pharmacyTariffDto.getOrganizationId());
				pharmacyTariff.setUnitId(pharmacyTariffDto.getUnitId());
			sessionFactory.getCurrentSession().save(pharmacyTariff);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getAllPharmacyTariff(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<PharmacyTariffDto> pharmacyTariffDtosList =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PHARMACY_TARIFF_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PharmacyTariffDto.class)).list();
			return new Response(SUCCESS, null, null, pharmacyTariffDtosList, null);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatus(Integer id,Character status) throws ApplicationException {
		try{
			Integer res =  sessionFactory.getCurrentSession()
					.createQuery("UPDATE PharmacyTariff set status=:status where id=:id ")
					.setParameter("id", id)
					.setParameter("status", status)
					.executeUpdate();
			return new Response(SUCCESS, null, null, null, res);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePharmacyTariff(PharmacyTariffDto pharmacyTariffDto) throws ApplicationException {
		try{
			Integer res =   sessionFactory.getCurrentSession()
					.createQuery("UPDATE PharmacyTariff set markUpDown=:markUpDown, markUpDownPer=:markUpDownPer WHERE id=:id ")
					.setParameter("id", pharmacyTariffDto.getId())
					.setParameter("markUpDown", pharmacyTariffDto.getMarkUpDown())
					.setDouble("markUpDownPer", pharmacyTariffDto.getMarkUpDownPer())
					.executeUpdate();
			return new Response(SUCCESS, null, null, null, res);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
