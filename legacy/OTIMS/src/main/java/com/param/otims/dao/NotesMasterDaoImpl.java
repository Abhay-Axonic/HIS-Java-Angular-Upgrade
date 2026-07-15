package com.param.otims.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.NotesMasterDto;
import com.param.otims.model.NotesMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class NotesMasterDaoImpl extends GenericDao<NotesMaster, Integer> implements INotesMasterDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(NotesMasterDaoImpl.class);

	public NotesMasterDaoImpl() {
		super(NotesMaster.class);
	}

	@Override
	public Response saveNotesMaster(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			List<NotesMasterDto> dto = new ArrayList<NotesMasterDto>();
			if(notesMasterDto.getCode()!=null && notesMasterDto.getDescription()!=null) {	
				dto = sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE_NOTE_MASTER_SAVE") //change by minesh for save from CHECK_FOR_UNIQUE_CHECKLIST_MASTER to 
							.setParameter("organizationId", notesMasterDto.getOrganizationId())
							.setParameter("unitId", notesMasterDto.getUnitId())
							.setParameter("code", notesMasterDto.getCode().toLowerCase())
							.setParameter("description", notesMasterDto.getDescription().toLowerCase())
							.setResultTransformer(Transformers.aliasToBean(NotesMasterDto.class)).list();
			}
			if(!dto.isEmpty()) //change condation by minesh
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			}
			NotesMaster mstr = new NotesMaster();
			mstr.setCode(notesMasterDto.getCode());
			mstr.setDescription(notesMasterDto.getDescription());
			mstr.setNoteType(notesMasterDto.getNoteType());
			mstr.setTemplate(notesMasterDto.getTemplate());
			mstr.setCreatedBy(notesMasterDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(notesMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(notesMasterDto.getOrganizationId());
			mstr.setStatus(notesMasterDto.getStatus());
			mstr.setUnitId(notesMasterDto.getUnitId());
			mstr.setUpdatedBy(notesMasterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(notesMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setTemplateFormatId(notesMasterDto.getTemplateFormatId());
			mstr.setFormTemplateKeywords(notesMasterDto.getFormTemplateKeywords());
			mstr.setFormTemplateId(notesMasterDto.getFormTemplateId());
			mstr.setTitle(notesMasterDto.getTitle());
			mstr.setTemplateTypeId(notesMasterDto.getTemplateTypeId());
			mstr.setTemplateId(notesMasterDto.getTemplateId());
			if(notesMasterDto.getNoteMasterId()!=null) {
				mstr.setNoteMasterId(notesMasterDto.getNoteMasterId());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}else {
				save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			}
				} catch (HibernateException he) {
					logger.error(he.getMessage(), he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}
	}

	//added by minesh for update NotesMaster.
	@Override
	public Response updateNotesMaster(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			List<NotesMasterDto> noteList  = sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_NOTE_MASTER_NOT_ID")
					.setParameter("organizationId", notesMasterDto.getOrganizationId())
					.setParameter("unitId", notesMasterDto.getUnitId())
					.setParameter("noteMasterId", notesMasterDto.getNoteMasterId())
					.setParameter("code", notesMasterDto.getCode().toLowerCase())
					.setParameter("description", notesMasterDto.getDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(NotesMasterDto.class)).list();
			
			if(!noteList.isEmpty()) //change condation by minesh
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			}else {
				NotesMaster mstr = new NotesMaster();
				mstr.setNoteMasterId(notesMasterDto.getNoteMasterId());
				mstr.setCode(notesMasterDto.getCode());
				mstr.setDescription(notesMasterDto.getDescription());
				mstr.setNoteType(notesMasterDto.getNoteType());
				mstr.setTemplate(notesMasterDto.getTemplate());
				mstr.setCreatedBy(notesMasterDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(notesMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrganizationId(notesMasterDto.getOrganizationId());
				mstr.setStatus(notesMasterDto.getStatus());
				mstr.setUnitId(notesMasterDto.getUnitId());
				mstr.setUpdatedBy(notesMasterDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(notesMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setTemplateFormatId(notesMasterDto.getTemplateFormatId());
				mstr.setFormTemplateKeywords(notesMasterDto.getFormTemplateKeywords());
				mstr.setFormTemplateId(notesMasterDto.getFormTemplateId());
				mstr.setTitle(notesMasterDto.getTitle());
				mstr.setTemplateTypeId(notesMasterDto.getTemplateTypeId());
				mstr.setTemplateId(notesMasterDto.getTemplateId());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
				}
			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}//end update NotesMaster.


	@Override
	public Response updateNotesMasterStatus(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			NotesMaster mstr = findById(notesMasterDto.getNoteMasterId());
			mstr.setStatus(notesMasterDto.getStatus());
			mstr.setUpdatedBy(notesMasterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(notesMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNotesMasterList(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			List<NotesMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NOTELIST_MASTER_LIST")
					.setParameter("organizationId", notesMasterDto.getOrganizationId())
					.setParameter("unitId", notesMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NotesMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNotesMasterListByNoteTypeId(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			List<NotesMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NOTELIST_MASTER_LIST_BY_NOTE_TYPE_ID")
					.setParameter("organizationId", notesMasterDto.getOrganizationId())
					.setParameter("unitId", notesMasterDto.getUnitId())
					.setParameter("noteType", notesMasterDto.getNoteType())
					.setResultTransformer(Transformers.aliasToBean(NotesMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNotesMasterListPagination(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			List<NotesMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NOTELIST_MASTER_LIST")
					.setParameter("organizationId", notesMasterDto.getOrganizationId())
					.setParameter("unitId", notesMasterDto.getUnitId())
					.setFirstResult(notesMasterDto.getOffset() != null ? notesMasterDto.getOffset()  : 0)
					.setMaxResults(notesMasterDto.getNoOfRecordsPerPage() != null ? notesMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(NotesMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNotesMasterListCount(NotesMasterDto notesMasterDto) throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(NotesMaster.class);
			crit.add(Restrictions.eq("organizationId", notesMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", notesMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
