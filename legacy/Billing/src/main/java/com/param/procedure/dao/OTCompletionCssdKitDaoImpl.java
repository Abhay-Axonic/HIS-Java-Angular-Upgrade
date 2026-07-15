package com.param.procedure.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.otims.model.OTCompletionCssdKit;
import com.param.procedure.dto.OTCompletionCssdKitDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OTCompletionCssdKitDaoImpl extends GenericDao<OTCompletionCssdKit, Integer> implements IOTCompletionCssdKitDao, ICommonConstants{

	public OTCompletionCssdKitDaoImpl() {
		super(OTCompletionCssdKit.class);
	}


	@Override
	public Response saveOtCompletionCssdKit(OTCompletionCssdKitDto dto) throws ApplicationException {
		try{
			
			if(dto.getOtCompletionCssdKitId()!=null) {
				OTCompletionCssdKit master = findById(dto.getOtCompletionCssdKitId());
				master.setKitTransitionNo(dto.getKitTransitionNo());
				master.setStatus(dto.getStatus());
				master = update(master);
				
			}else {
				
				OTCompletionCssdKit master = new OTCompletionCssdKit();
				master.setKitCode(dto.getKitCode());
				master.setKitName(dto.getKitName());
				master.setKitId(dto.getKitId());
				master.setKitTransitionNo(dto.getKitTransitionNo());
				master.setBatchId(dto.getBatchId()!=null? dto.getBatchId() :null);
				master.setStoreId(dto.getStoreId());
				master.setStatus('A');
				master.setOrganizationId(dto.getOrganizationId());
				master.setUnitId(dto.getUnitId());
				master.setCreatedBy(dto.getCreatedBy());
				master.setProReqId(dto.getProReqId());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}


	@Override
	public Response getOtCSSDKit(int proReqId) throws ApplicationException {
		try{
			List<OTCompletionCssdKitDto> listOTCSSDDto = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_CSSD_KIT_BY_PRO_REQ_ID")
					.setParameter("id", proReqId)
					.setResultTransformer(Transformers.aliasToBean(OTCompletionCssdKitDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOTCSSDDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}


	@SuppressWarnings("unused")
	@Override
	public Response updateIsUsedFlag(OTCompletionCssdKitDto otCompletionCssdKitDto, char c) {
		try{	
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update cssd.t_issued_kit_transition_mapper set is_kit_used =:isKitUsed where transition_number =:kitTransitionNo ")
					.setParameter("kitTransitionNo", otCompletionCssdKitDto.getKitTransitionNo())
					.setParameter("isKitUsed", c)
					.executeUpdate();
			
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

}
