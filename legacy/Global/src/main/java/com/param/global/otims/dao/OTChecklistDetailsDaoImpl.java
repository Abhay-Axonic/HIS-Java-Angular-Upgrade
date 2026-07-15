package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.OTChecklistDetails;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.global.otims.dto.OTChecklistDetailsDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class OTChecklistDetailsDaoImpl extends GenericDao<OTChecklistDetails, Integer>implements IOTChecklistDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(OTChecklistDetailsDaoImpl.class);


public OTChecklistDetailsDaoImpl() {
	super(OTChecklistDetails.class);
}

	@Override
	public Response saveOTChecklistDetails(OTChecklistDetailsDto otChecklistDetailsDto2) throws ApplicationException {
		try {
				OTChecklistDetails mstr = new OTChecklistDetails();
				mstr.setChecklistId(otChecklistDetailsDto2.getChecklistId());
				mstr.setChecklistMasterId(otChecklistDetailsDto2.getChecklistMasterId());
				mstr.setChecklistStatusId(otChecklistDetailsDto2.getChecklistStatusId());
				mstr.setDescription(otChecklistDetailsDto2.getDescription());
				mstr.setProReqId(otChecklistDetailsDto2.getProReqId());
				mstr.setRemark(otChecklistDetailsDto2.getRemark());
				mstr.setStageId(otChecklistDetailsDto2.getStageId());
				mstr.setCreatedBy(otChecklistDetailsDto2.getCreatedBy());
				mstr.setUpdatedBy(otChecklistDetailsDto2.getUpdatedBy());
				mstr.setUnitId(otChecklistDetailsDto2.getUnitId());
				mstr.setOrganizationId(otChecklistDetailsDto2.getOrganizationId());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(otChecklistDetailsDto2.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(otChecklistDetailsDto2.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				mstr.setStatus(otChecklistDetailsDto2.getStatus());
				mstr = save(mstr);
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				} catch (HibernateException he) {
				logger.error("saveOTChecklistDetails failed", he);
				} catch (Exception e) {
			logger.error("saveOTChecklistDetails failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIntraOperativeChecklistByProReqId(OTChecklistDetailsDto oTChecklistDetailsDto)
			throws ApplicationException {
		try{
			List<ChecklistMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_INTRA_OPERATIVE_CHECKLIST_BY_PRO_REQ_ID").setParameter("proReqId", oTChecklistDetailsDto.getProReqId()).setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).list();
			ChecklistMasterDto obj = (ChecklistMasterDto)sessionFactory.getCurrentSession().getNamedQuery("GET_PROC_ORDER_REQ_ID").setParameter("proReqId", oTChecklistDetailsDto.getProReqId()).setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).uniqueResult();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, obj);
		}catch(Exception e){
			
			throw e;
		}
	}
}
