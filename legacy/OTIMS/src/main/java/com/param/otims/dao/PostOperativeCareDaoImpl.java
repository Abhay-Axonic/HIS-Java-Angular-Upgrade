package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OTCompletionNoteDto;
import com.param.global.otims.dto.PostOperativeCareDto;
import com.param.otims.model.PostOperativeCare;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PostOperativeCareDaoImpl extends GenericDao<PostOperativeCare, Integer> implements IPostOperativeCareDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(PostOperativeCareDaoImpl.class);

	public PostOperativeCareDaoImpl() {
		super(PostOperativeCare.class);
	}

	@Override
	public Response savePostOperativeCare(PostOperativeCareDto dto) throws ApplicationException {
		try{
			PostOperativeCare master = new PostOperativeCare();
			master.setProReqId(dto.getProReqId());
			//master.setFollowUpNote(dto.getFollowUpNote());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setProReqId(dto.getProReqId());
			master.setStatus('A');
			if(dto.getPostOperativeCareId() != null) {
				master.setPostOperativeCareId(dto.getPostOperativeCareId());
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
	public Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proReqId)
			throws ApplicationException {
		try{
			List<OTCompletionNoteDto> listProcedureConfigurationDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NOTELIST_BY_PRO_REQ_AND_CONFIG_ID")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("proReqId", proReqId)
					.setResultTransformer(Transformers.aliasToBean(OTCompletionNoteDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listProcedureConfigurationDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getPostOperativeCare(Integer proReqId) throws ApplicationException {
		try{
			PostOperativeCareDto dto = (PostOperativeCareDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_POST_CARE_DATA_BY_PRO_REQ_ID")
					.setParameter("proReqId", proReqId)
					.setResultTransformer(Transformers.aliasToBean(PostOperativeCareDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
}
