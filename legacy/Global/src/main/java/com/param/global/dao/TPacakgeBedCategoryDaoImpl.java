package com.param.global.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.packages.model.TPackageBedCategory;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageBedCategoryDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TPacakgeBedCategoryDaoImpl extends GenericDao<TPackageBedCategory, Integer>
		implements ITPacakgeBedCategoryDao, ICommonConstants {

	public TPacakgeBedCategoryDaoImpl() {
		super(TPackageBedCategory.class);
	}

	@Override
	public Response saveTPackageBedCategory(TPackageBedCategoryDto detailsDto) throws ServiceException {
		try {
			TPackageBedCategory bedCategory = new TPackageBedCategory();
			bedCategory.setPackageConsumptionMasterId(detailsDto.getPackageConsumptionMasterId());
			bedCategory.setCreatedBy(detailsDto.getCreatedBy());
			bedCategory.setCreatedDate(detailsDto.getCreatedDate() != null && !detailsDto.getCreatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(detailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
					: TimeZoneComponent.getDateByzone(detailsDto.getUnitId()));
			bedCategory.setOrganisationId(detailsDto.getOrganisationId());
			bedCategory.setStatus(ACTIVE);
			bedCategory.setUnitId(detailsDto.getUnitId());
			bedCategory.setBedCategoryId(detailsDto.getBedCategoryId());
			bedCategory.setBillingBedCategoryId(detailsDto.getBillingBedCategoryId());
			bedCategory.setApplicableDays(detailsDto.getApplicableDays());
			bedCategory = save(bedCategory);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, bedCategory);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageBedCategoryForBilling(int packageId) throws ServiceException {
		try {
			List<TPackageBedCategoryDto> listTPackageBedCategoryDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_FOR_BILLING_BY_PACKAGE").setParameter("packageId", packageId)
					.setResultTransformer(Transformers.aliasToBean(TPackageBedCategoryDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageBedCategoryDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

}
