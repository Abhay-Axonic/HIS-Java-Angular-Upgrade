package com.param.global.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.packages.model.TPackageConsumptionEoDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PackageConsumptionEODetailDto;
import com.param.global.dto.TPackageConsumptionEoDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes"})
public class TPackageConsumptionEoDetailsDaoImpl extends GenericDao<TPackageConsumptionEoDetails, Integer> implements ITPackageConsumptionEoDetailsDao, ICommonConstants{

	public TPackageConsumptionEoDetailsDaoImpl() {
		super(TPackageConsumptionEoDetails.class);
	}

	@Override
	public Response saveTPackageConsumptionEoDetails(TPackageConsumptionEoDetailsDto dto) throws BillingException {
		try{
			TPackageConsumptionEoDetails details = new TPackageConsumptionEoDetails();
			details.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
			details.setIsServiceItemBed(dto.getIsServiceItemBed());
			details.setPacakgeEighterorGroupId(dto.getPacakgeEighterorGroupId());
			details.setProductCatId(dto.getProductCatId());
			details.setBedCatId(dto.getBedCatId());
			details.setTotalQuantity(dto.getTotalQuantity());
			details.setBalanceQuantity(dto.getBalanceQuantity());
			details.setGroupPrice(dto.getGroupPrice());
			details.setOrganisationId(dto.getOrganisationId());
			details.setUnitId(dto.getUnitId());
			details.setCreatedBy(dto.getCreatedBy());
			details.setStatus('A');
			details.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details.setUpdatedBy(dto.getUpdatedBy());
			details.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			details = save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, details);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getTPackageConsumptionEoDetailsByGrpIdConsumptionId(
			Integer packageEoGroupId, Integer packageConsumptionId)
			throws BillingException {
		try{
			List list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_CONSUMPTION_EO_DETAILS")
					.setParameter("packageConsumptionMasterId",packageConsumptionId)
					.setParameter("packageEOGroupId", packageEoGroupId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionEODetailDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateTPackageConsumptionEoQtyCancel(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId)throws BillingException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PACKAGE_CONUMPTION_EO_BALANCE_QTY")
					.setParameter("packageId", packageId)
					.setParameter("patientId", serviceId)
					.setParameter("encounterId", encounterId)
					.setParameter("serviceId", serviceId)
					.setParameter("qty", 1).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

}
