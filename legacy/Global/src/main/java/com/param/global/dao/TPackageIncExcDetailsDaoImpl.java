package com.param.global.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageIncExcDetailsDto;
import com.param.service.global.model.TPackageIncExcDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TPackageIncExcDetailsDaoImpl extends GenericDao<TPackageIncExcDetails, Integer> implements ITPackageIncExcDetailsDao, ICommonConstants{

	public TPackageIncExcDetailsDaoImpl() {
		super(TPackageIncExcDetails.class);
	}

	@Override
	public Response saveTPackageIncExcDetails(TPackageIncExcDetailsDto detailsDto) throws ServiceException {
		try{
			TPackageIncExcDetails details = new TPackageIncExcDetails();
			detailsDto.setPackageIncExcId(detailsDto.getParentPackageId());
			details.setCreatedBy(detailsDto.getCreatedBy());
			details.setCreatedDate(detailsDto.getCreatedDate() != null && !detailsDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(detailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(detailsDto.getOrgUnitId()));
			details.setIsInclusiveService(detailsDto.getIsInclusiveService());
			details.setIsServiceItem(detailsDto.getIsServiceItem());
			details.setItemId(detailsDto.getItemId());
			details.setOrgId(detailsDto.getOrgId());
			details.setOrgUnitId(detailsDto.getOrgUnitId());
			details.setPackageCategoryConsumableId(detailsDto.getPackageCategoryConsumableId());
			details.setPackageId(detailsDto.getPackageId());
			details.setServiceId(detailsDto.getServiceId());
			details.setStatus(ACTIVE);
			details.setServicePrice(detailsDto.getServicePrice());
			details.setUpdatedBy(detailsDto.getUpdatedBy());
			details.setUpdatedDate(detailsDto.getUpdatedDate() != null && !detailsDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(detailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(detailsDto.getOrgUnitId()));
			details.setParentPackageId(detailsDto.getParentPackageId());
			details.setIsGroupwiseExlusion(detailsDto.getIsGroupwiseExlusion()!=null ? detailsDto.getIsGroupwiseExlusion() : 0);
			details.setIsExcluded(detailsDto.getIsExcluded());
			if(detailsDto.getIsGroupwiseExlusion()!=null && detailsDto.getIsGroupwiseExlusion().intValue()==2) {
				details.setDeptId(detailsDto.getSpecialityId());
				details.setSubDeptId(detailsDto.getSubSpecialityId());			
			}
			details = save(details);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, details);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTPackageIncExcDetailsForBillingByPackage(int packageId) throws ServiceException {
		try{
			List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_INC_EXC_DETAILS_FOR_BILLING_BY_PACKAGE").setParameter("packageId", packageId).setResultTransformer(Transformers.aliasToBean(TPackageIncExcDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageIncExcDetailsDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getTPackageIncExcDetailsForBillingByPackageConsumptionId(int packageConsumptionMasterId)
			throws ServiceException {
		try{
			List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_CONSUMPTION_INC_EXC_DETAILS_FOR_BILLING").setParameter("packageConsumptionMasterId", packageConsumptionMasterId).setResultTransformer(Transformers.aliasToBean(TPackageIncExcDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageIncExcDetailsDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getTPackageIncExcDetailsForServiceByPacakgeId(int packageId) throws ServiceException {
		try{
			List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_INC_EXC_SERIVCE_BY_PACKAGE_ID").setParameter("packageId", packageId).setResultTransformer(Transformers.aliasToBean(TPackageIncExcDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageIncExcDetailsDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getTPackageIncExcDetailsForItemsByPacakgeId(int packageId) throws ServiceException {
		try{
			List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PACKAGE_INC_EXC_ITEM_BY_PACKAGE_ID").setParameter("packageId", packageId).setResultTransformer(Transformers.aliasToBean(TPackageIncExcDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageIncExcDetailsDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}



}
