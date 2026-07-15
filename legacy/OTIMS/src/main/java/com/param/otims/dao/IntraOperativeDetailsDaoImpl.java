package com.param.otims.dao;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.global.otims.dto.IntraOperativeDetailsDto;
import com.param.global.otims.dto.IntraOperativeImageDto;
import com.param.otims.model.IntraOperativeDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class IntraOperativeDetailsDaoImpl extends GenericDao<IntraOperativeDetails, Integer> implements IIntraOperativeDetailsDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(IntraOperativeDetailsDaoImpl.class);

	public IntraOperativeDetailsDaoImpl() {
		super(IntraOperativeDetails.class);
	}

	@Override
	public Response savePreOperativeDetails(IntraOperativeDetailsDto dto)
			throws ApplicationException {
		try{
			IntraOperativeDetails master = new IntraOperativeDetails();
			master.setProReqId(dto.getProReqId());
			master.setWhoChecklistStatus(dto.getWhoChecklistStatus());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setProReqId(dto.getProReqId());
			master.setStatus('A');
			if(dto.getIntraOperativeId() != null) {
				master.setIntraOperativeId(dto.getIntraOperativeId());
				master = update(master);
			}else {
				master = save(master);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getIntraOperativeByProReqId(IntraOperativeDetailsDto dto) throws ApplicationException {
		try{
			IntraOperativeDetailsDto obj = (IntraOperativeDetailsDto)sessionFactory.getCurrentSession().getNamedQuery("GET_INTRA_OPERATIVE_DATA").setInteger("proReqId", dto.getProReqId()).setResultTransformer(Transformers.aliasToBean(IntraOperativeDetailsDto.class)).uniqueResult();
			ChecklistMasterDto obj1 = (ChecklistMasterDto)sessionFactory.getCurrentSession().getNamedQuery("GET_PROC_ORDER_REQ_ID").setInteger("proReqId", dto.getProReqId()).setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).uniqueResult();
			if(obj != null ) {
				obj.setProcedureOrderId(obj1.getProcedureOrderId());
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveIntraVideo(IntraOperativeImageDto dto) {
		try{
			IntraOperativeDetails master = findById(dto.getIntraOperativeId());
					master.setVideoUrl(dto.getImageUrl());
					master = update(master);
			
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}
}
