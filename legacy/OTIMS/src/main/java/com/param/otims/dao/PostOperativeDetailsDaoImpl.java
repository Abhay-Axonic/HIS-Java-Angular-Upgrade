package com.param.otims.dao;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.PostOperativeDetailsDto;
import com.param.otims.model.PostOperativeDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PostOperativeDetailsDaoImpl  extends GenericDao<PostOperativeDetails, Integer> implements IPostOperativeDetailsDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(PostOperativeDetailsDaoImpl.class);

	public PostOperativeDetailsDaoImpl() {
		super(PostOperativeDetails.class);
	}

	@Override
	public Response savePostOperativeDetails(PostOperativeDetailsDto dto)
			throws ApplicationException {
		try{
			PostOperativeDetails master = new PostOperativeDetails();
			master.setPostOpNote(dto.getPostOpNote());
			master.setProReqId(dto.getProReqId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setProReqId(dto.getProReqId());
			master.setStatus('A');
			if(dto.getPostOperativeId() != null) {
				master.setPostOperativeId(dto.getPostOperativeId());
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
	public Response getPostOperativeDetails(PostOperativeDetailsDto postOperativeDetailsDto)
			throws ApplicationException {
		try{
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_POSTOPERATIVE_DETAILS_BY_PRO_REQ_ID").getQueryString();
			PostOperativeDetailsDto obj = (PostOperativeDetailsDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("proReqId", postOperativeDetailsDto.getProReqId())
					.setResultTransformer(Transformers.aliasToBean(PostOperativeDetailsDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, obj);
		 } catch (Exception e)
        {
            throw e;
        }
      //  return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


}
