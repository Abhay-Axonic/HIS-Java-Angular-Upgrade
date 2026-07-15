package com.param.global.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageBedCategoryDetailDto;
import com.param.service.global.model.TPackageBedCategoryDetail;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes","unchecked" })
public class TPackageBedCategoryDetailDaoImpl extends GenericDao<TPackageBedCategoryDetail, Integer>
		implements ITPackageBedCategoryDetailDao, ICommonConstants {

	public TPackageBedCategoryDetailDaoImpl() {
		super(TPackageBedCategoryDetail.class);
	}

	@Override
	public Response saveTPackageBedCategoryDetail(TPackageBedCategoryDetailDto detailDto) throws ServiceException {
		try {
			TPackageBedCategoryDetail detail = new TPackageBedCategoryDetail();
			detail.setParentPackageId(detailDto.getParentPackageId());
			detail.setApplicableDays(detailDto.getApplicableDays());
			detail.setBedCategoryId(detailDto.getBedCategoryId());
			detail.setBillingBedCategoryId(detailDto.getBillingBedCategoryId());
			detail.setCreatedBy(detailDto.getCreatedBy());
			detail.setCreatedDate(detailDto.getCreatedDate() != null && !detailDto.getCreatedDate().isEmpty()
					? GlobalCommonDateUtils.getDate(detailDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")
					: TimeZoneComponent.getDateByzone(detailDto.getOrgUnitId()));
			detail.setOrgId(detailDto.getOrgId());
			detail.setOrgUnitId(detailDto.getOrgUnitId());
			detail.setPackageId(detailDto.getPackageId());
			detail.setStatus(ACTIVE);
			detail.setTotalAmount(detailDto.getTotalAmount());
			detail.setUpdatedBy(detailDto.getUpdatedBy());
			detail.setUpdatedDate(null);
			detail = save(detail);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, detail);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTPackageBedCategoryDetail(Integer packageId) throws ServiceException {
		try {
			List<TPackageBedCategoryDetailDto> listTPackageBedCategoryDetailDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_DETAILS_BY_PACKAGE").setParameter("packageId", packageId)
					.setResultTransformer(Transformers.aliasToBean(TPackageBedCategoryDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageBedCategoryDetailDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTPackageBedCategoryDetailBilling(Integer packageConsumptionMasterId) throws ServiceException {
		try {
			List<TPackageBedCategoryDetailDto> listTPackageBedCategoryDetailDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_CATEGORY_FOR_BILLING").setParameter("packageConsumptionMasterId", packageConsumptionMasterId)
					.setResultTransformer(Transformers.aliasToBean(TPackageBedCategoryDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageBedCategoryDetailDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientApplicableDaysByAdmissionId(Integer admissionId) throws ServiceException {
		try {
			List<TPackageBedCategoryDetailDto> listTPackageBedCategoryDetailDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_TRANSER_LOG").setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(TPackageBedCategoryDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageBedCategoryDetailDto, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

}
