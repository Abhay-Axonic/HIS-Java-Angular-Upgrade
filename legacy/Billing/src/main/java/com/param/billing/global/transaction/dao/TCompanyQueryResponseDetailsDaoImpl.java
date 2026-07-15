package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceQueryDetailDto;
import com.param.billing.global.transaction.model.TCompanyInvoiceQueryDetail;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TCompanyQueryResponseDetailsDaoImpl extends GenericDao<TCompanyInvoiceQueryDetail, Integer> implements ICompanyQueryResponseDetailsDao, ICommonConstants{

	public TCompanyQueryResponseDetailsDaoImpl() {
		super(TCompanyInvoiceQueryDetail.class);
	}

	@Override
	public com.param.global.common.Response saveCompanyQueryResponseDetails(TCompanyInvoiceQueryDetailDto masterDto)
			throws ServiceException {
		try{
			TCompanyInvoiceQueryDetail tCompanyInvoiceQueryDetail = new TCompanyInvoiceQueryDetail();
			tCompanyInvoiceQueryDetail.setCreatedBy(masterDto.getCreatedBy());
			tCompanyInvoiceQueryDetail.setInvoiceDetailId(masterDto.getInvoiceDetailId());
			tCompanyInvoiceQueryDetail.setOrgId(masterDto.getOrgId());
			tCompanyInvoiceQueryDetail.setUnitId(masterDto.getUnitId());
			tCompanyInvoiceQueryDetail.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()).getTime());
			tCompanyInvoiceQueryDetail.setQueryDate(masterDto.getQueryDate() != null && !masterDto.getQueryDate().isEmpty() ? GlobalCommonDateUtils.getDate(masterDto.getQueryDate(), "dd-M-yyyy").getTime() : null);
			tCompanyInvoiceQueryDetail.setRemark(masterDto.getRemark());
			tCompanyInvoiceQueryDetail.setQueryDetails(masterDto.getQueryDetails());
			tCompanyInvoiceQueryDetail.setQueryNo(GlobalCommonDateUtils.generateRandomString());
			tCompanyInvoiceQueryDetail.setResponseDate(masterDto.getResponseDate() != null && !masterDto.getResponseDate().isEmpty() ? GlobalCommonDateUtils.getDate(masterDto.getResponseDate(), "dd-M-yyyy").getTime() : null);
			tCompanyInvoiceQueryDetail.setResponseDetails(masterDto.getResponseDetails());
			tCompanyInvoiceQueryDetail.setQueryStatusId(1);
			tCompanyInvoiceQueryDetail.setCompanyId(masterDto.getCompanyId());
			tCompanyInvoiceQueryDetail = save(tCompanyInvoiceQueryDetail);
			return new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, null, null, tCompanyInvoiceQueryDetail);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
		
	@Override
	public Response updateCompanyQueryResponseDetailsByCompanyInvoiceReminderId(TCompanyInvoiceQueryDetailDto reqDto) throws ServiceException {
		try{
			TCompanyInvoiceQueryDetail tCompanyInvoiceQueryDetail = findById(reqDto.getCompanyInvoiceQueryDetailId());
			if (tCompanyInvoiceQueryDetail.getCompanyInvoiceQueryDetailId()!= 0)
			{
				tCompanyInvoiceQueryDetail.setResponseDetails(reqDto.getResponseDetails());
				tCompanyInvoiceQueryDetail.setResponseDate(reqDto.getResponseDate() != null && !reqDto.getResponseDate().isEmpty() ? GlobalCommonDateUtils.getDate(reqDto.getResponseDate(), "dd-M-yyyy").getTime() : null);
				tCompanyInvoiceQueryDetail.setQueryStatusId(reqDto.getQueryStatusId());
				tCompanyInvoiceQueryDetail = update(tCompanyInvoiceQueryDetail);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, tCompanyInvoiceQueryDetail);
			}else {
				
				return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, tCompanyInvoiceQueryDetail);
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
		
	@Override
	public Response getPendingCompanyQueryResponseDetails(TCompanyInvoiceQueryDetailDto reqDto) throws ServiceException {
		
    try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PENDING_COMPANY_QUERY_LIST").getQueryString());
			//query.append(reqDto.getPackagetypeId() != null ? " and pack.package_type_id ="+reqDto.getPackagetypeId() : " ");
			//query.append(" AND bm .bill_date_time <='2018-07-25' AND bm .bill_date_time >='2017-12-31'");
			List<TCompanyInvoiceQueryDetailDto> pendingCompanyQueryList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("companyInvoiceQueryDetailId", reqDto.getCompanyInvoiceQueryDetailId())
					.setParameter("queryStatusId", reqDto.getQueryStatusId()).setResultTransformer(Transformers.aliasToBean(TCompanyInvoiceQueryDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, pendingCompanyQueryList, null);
		}catch (Exception e) {
				
			throw e;
		}
		//return new Response(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	

}
