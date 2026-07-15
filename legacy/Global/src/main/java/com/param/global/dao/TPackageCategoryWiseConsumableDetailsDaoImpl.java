package com.param.global.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageCategoryWiseConsumableDetailsDto;
import com.param.service.global.model.TPackageCategoryWiseConsumableDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TPackageCategoryWiseConsumableDetailsDaoImpl extends GenericDao<TPackageCategoryWiseConsumableDetails, Integer> implements ITPackageCategoryWiseConsumableDetailsDao, ICommonConstants{

	public TPackageCategoryWiseConsumableDetailsDaoImpl() {
		super(TPackageCategoryWiseConsumableDetails.class);
	}

	@Override
	public Response saveTPackageCategoryWiseConsumableDetails(TPackageCategoryWiseConsumableDetailsDto detailsDto)throws ServiceException {
		try{
			TPackageCategoryWiseConsumableDetails details = new TPackageCategoryWiseConsumableDetails();
			details.setParentPackageId(detailsDto.getParentPackageId());
			details.setCategoryId(detailsDto.getCategoryId());
			details.setCreatedBy(detailsDto.getCreatedBy());
			details.setCreatedDate(detailsDto.getCreatedDate() != null && !detailsDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(detailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(detailsDto.getOrgUnitId()));
			details.setNumberToBeUse(detailsDto.getNumberToBeUse());
			details.setOrgId(detailsDto.getOrgId());
			details.setOrgUnitId(detailsDto.getOrgUnitId());
			details.setPackageId(detailsDto.getPackageId());
			details.setStatus(ACTIVE);
			details.setUpdatedBy(detailsDto.getUpdatedBy());
			details.setUpdatedDate(null);
			details = save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, details);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getTPackageCategoryWiseConsumableDetails(Integer packageId) throws ServiceException {
		try {
			List<TPackageCategoryWiseConsumableDetailsDto> listTPackageCategoryWiseConsumableDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CATEGORY_WISE_CONSUMABLE_DETAILS_BY_PACKAGE").setParameter("packageId", packageId)
					.setResultTransformer(Transformers.aliasToBean(TPackageCategoryWiseConsumableDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageCategoryWiseConsumableDetailsDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

}
