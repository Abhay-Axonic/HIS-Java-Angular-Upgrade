package com.param.global.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageServicesDetailsDto;
import com.param.service.global.model.TPackageServicesDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TPackageServicesDetailsDaoImpl extends GenericDao<TPackageServicesDetails, Integer>
		implements ITPackageServicesDetailsDao, ICommonConstants {

	public TPackageServicesDetailsDaoImpl() {
		super(TPackageServicesDetails.class);
	}

	@Override
	public Response saveTPackageServicesDetails(TPackageServicesDetailsDto detailsDto) throws ServiceException {
		try {
			TPackageServicesDetails servicesDetails = new TPackageServicesDetails();
			servicesDetails.setApportionedPrice(detailsDto.getApportionedPrice());
			servicesDetails.setCreatedBy(detailsDto.getCreatedBy());
			servicesDetails.setCreatedDate(detailsDto.getCreatedDate() != null && !detailsDto.getCreatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(detailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
					: TimeZoneComponent.getDateByzone(detailsDto.getUnitId()));
			servicesDetails.setIsServiceItem(detailsDto.getIsServiceItem());
			servicesDetails.setItemId(detailsDto.getItemId());
			servicesDetails.setNumberToBeUse(detailsDto.getNumberToBeUse());
			servicesDetails.setOrgnisationId(detailsDto.getOrgnisationId());
			servicesDetails.setPackageEitherOrGroupId(detailsDto.getPackageEitherOrGroupId());
			servicesDetails.setPackageId(detailsDto.getPackageId());
			servicesDetails.setPeriodicityId(detailsDto.getPeriodicityId());
			servicesDetails.setServiceId(detailsDto.getServiceId());
			servicesDetails.setServicePrice(detailsDto.getServicePrice());
			servicesDetails.setStatus(ACTIVE);
			servicesDetails.setConsultationSpecialityId(detailsDto.getConsultationSpecialityId());
			servicesDetails.setUnitId(detailsDto.getUnitId());
			servicesDetails.setUpdatedBy(detailsDto.getUpdatedBy());
			servicesDetails.setUpdatedDate(detailsDto.getUpdatedDate() != null && !detailsDto.getUpdatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(detailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss")
					: TimeZoneComponent.getDateByzone(detailsDto.getUnitId()));
			servicesDetails.setIsCap(1);
			//servicesDetails.setIsCap(detailsDto.getIsCap());
			servicesDetails.setParentPackageId(detailsDto.getParentPackageId());
			servicesDetails = save(servicesDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, servicesDetails);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageServicesByPackageId(int packageId) throws ServiceException {
		try {
			List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_BILLING").setParameter("packageId", packageId)
					.setResultTransformer(Transformers.aliasToBean(TPackageServicesDetailsDto.class)).list();
			return new Response<>(SUCCESS_CODE, SUCCESS_CODE, null, listTPackageServicesDetailsDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageServiceEditByPackageId(int packageId) throws ServiceException {
		try {
			List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_EDIT").setParameter("packageId", packageId)
					.setResultTransformer(Transformers.aliasToBean(TPackageServicesDetailsDto.class)).list();
			return new Response<>(SUCCESS_CODE, SUCCESS_CODE, null, listTPackageServicesDetailsDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageItemEditByPackageId(int packageId) throws ServiceException {
		try {
			List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ITEM_DETAILS_BY_PACKAGE_ID_FOR_EDIT").setParameter("packageId", packageId)
					.setResultTransformer(Transformers.aliasToBean(TPackageServicesDetailsDto.class)).list();
			return new Response<>(SUCCESS_CODE, SUCCESS_CODE, null, listTPackageServicesDetailsDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageEitherOrServiceEditByGroupId(int packageGroupDetailsId) throws ServiceException {
		try {
			List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EITHER_OR_GROUP_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_EDIT")
					.setParameter("packageGroupDetailsId", packageGroupDetailsId)
					.setResultTransformer(Transformers.aliasToBean(TPackageServicesDetailsDto.class)).list();
			return new Response<>(SUCCESS_CODE, SUCCESS_CODE, null, listTPackageServicesDetailsDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageServicesFrTransactionByPackageId(Integer packageId, Integer consumptionMasterId)
			throws ServiceException {
		try {
			List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_BILLING_TRANSACTION")
					.setParameter("packageId", packageId)
					.setParameter("consumptionMasterId", consumptionMasterId)
					.setResultTransformer(Transformers.aliasToBean(TPackageServicesDetailsDto.class)).list();
			return new Response<>(SUCCESS_CODE, SUCCESS_CODE, null, listTPackageServicesDetailsDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageEitherOrServicesByGroupIdForTransaction(Integer packageId, Integer consumptionMasterId,
			Integer groupId) throws ServiceException {
		try {
			List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EITHER_OR_GROUP_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_TRANSACTION")
					.setParameter("packageId", packageId)
					.setParameter("consumptionMasterId", consumptionMasterId)
					.setParameter("groupId", groupId)
					.setResultTransformer(Transformers.aliasToBean(TPackageServicesDetailsDto.class)).list();
			return new Response<>(SUCCESS_CODE, SUCCESS_CODE, null, listTPackageServicesDetailsDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
}
