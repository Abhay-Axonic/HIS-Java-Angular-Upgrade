package com.param.pharmacy.master.global.dao.frequency;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.TranslatedFrequencyMasterDto;
import com.param.opd.coversheet.model.TranslatedFrequencyMaster;
import com.param.pharmacy.master.global.dto.frequency.*;
import com.param.utility.TimeZoneComponent;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.FrequencyDetail;
import com.param.service.global.SessionService;

import static com.param.global.common.ICommonConstants.*;

@Repository("FrequencyDao")
@SuppressWarnings({"unchecked","rawtypes"})
public class FrequencyDao extends AbstractDao<Integer, FrequencyResponse> implements IFrequencyDao {

    private static final Logger logger = Logger.getLogger(FrequencyDao.class);

    @Autowired
    private SessionService sessionService;

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;
    
   @Override
	public List<Frequency> getAll(Integer orgId)
	{
		String hql="SELECT frequency FROM Frequency AS frequency"
				+ " where frequency.isActive=true AND frequency.isDeleted=false "
				+ " AND frequency.organization.organizationId=:organizationId " 
				+ "ORDER BY frequency.id DESC";
				
		Query query = getSession().createQuery(hql)
        .setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());

		return query.list();
	}

   @Override
	public Frequency getById(Integer id) {

		String hql = "SELECT frequency FROM Frequency AS frequency "
				+ "WHERE frequency.id = :id  AND frequency.isActive = true AND frequency.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Frequency) query.uniqueResult();
	}
   
	@Override
	public Frequency getFrequency(Integer id) {
		String hql = "SELECT frequency FROM Frequency AS frequency "
				+ "WHERE frequency.id = :id  AND frequency.isDeleted = false ";
		//+ "WHERE rack.id = :id  AND rack.isActive = true AND rack.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Frequency) query.uniqueResult();
	}
	
	@Override
	public List<FrequencyDetail> getFrequencyDetail(Integer id)
	{
		String hql = "SELECT frequencyDetail FROM FrequencyDetail AS frequencyDetail "
            + " WHERE frequencyDetail.frequency.id = :id  AND frequencyDetail.isActive = true AND frequencyDetail.isDeleted = false "
            + " ORDER BY frequencyDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return  query.list();
	}
	

	@Override
	public Frequency saveFrequency(Frequency frequency) {
		getSession().save(frequency);
		return frequency;
	}

	@Override
	public Frequency update(Frequency frequency) {
		getSession().update(frequency);
		return frequency;
	}

	@Override
	public List<GetFrequencyListResponse> getFrequencyDetailsById(Integer id) {
		String sql ="select fdl.id as \"id\", " +
	            "   to_char(fdl.administration_time,'HH24:MI') as \"administrationTime\"," + 
				"	to_char(fdl.pre_buffer_administration_time,'HH24:MI') as \"preBufferAdministrationTime\"," + 
				"	to_char(fdl.post_buffer_administration_time,'HH24:MI') as \"postBufferAdministrationTime\" " + 
				"	from  m_frequency_detail fdl " + 
				"	where fdl.frequency_id=:id"+ 
				" and fdl.is_active = true" +
				" and fdl.is_deleted = false";
		        Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("administrationTime")
				.addScalar("preBufferAdministrationTime").addScalar("postBufferAdministrationTime");
				query.setParameter("id", id);		
				query.setResultTransformer(Transformers.aliasToBean(GetFrequencyListResponse.class));

		return query.list();
	}

	@Override
	public List<FrequencyRequestItem> getFrequencyDetailsByIdForUpdate(Integer id) {
		String sql ="select fdl.id as \"id\", " +
				"  to_char(fdl.administration_time,'HH24:MI') as \"administrationTimeString\"," + 
				"	to_char(fdl.pre_buffer_administration_time,'HH24:MI') as \"preBufferAdministrationTimeString\"," + 
				"	to_char(fdl.post_buffer_administration_time,'HH24:MI') as \"postBufferAdministrationTimeString\" " + 
				"	from  m_frequency_detail fdl " + 
				"	where fdl.frequency_id=:id"+ 
				" and fdl.is_active = true" +
				" and fdl.is_deleted = false";
				
		        Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("administrationTimeString")
				.addScalar("preBufferAdministrationTimeString").addScalar("postBufferAdministrationTimeString");
				query.setParameter("id", id);		
				query.setResultTransformer(Transformers.aliasToBean(FrequencyRequestItem.class));

		return query.list();
	}


	@Override
	public Response inactiveTranslatedFrequency(FrequencyRequest dto) throws ApplicationException {
	   try{

		   sessionFactory.getCurrentSession()
				   .getNamedQuery("UPDATE_TRANSLATED_FREQUENCY")
				   .setParameter("status", 'I')
				   .setParameter("organizationId", dto.getOrganizationId())
				   .setParameter("unitId", dto.getUnitId())
				   .setParameter("frequencyId", dto.getId())
				   .executeUpdate();

	   }catch (HibernateException he) {
		   logger.error(he.getMessage(), he);
		   throw new ServiceException(he.getMessage());
	   } catch (Exception e) {
		   logger.error(e.getMessage(), e);
		   throw new ServiceException(e.getMessage());
	   }
		return null;
	}

	@Override
	public Response updateTranslatedFrequency(TranslatedFrequencyMasterDto dto) {
		try{
			TranslatedFrequencyMaster master=new TranslatedFrequencyMaster();
			master.setConvertedText(dto.getConvertedText());
			master.setDesc(dto.getDesc());
			master.setFrequencyId(dto.getFrequencyId());
			master.setLanguageId(dto.getLanguageId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setStatus('A');
			save(master);

		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveTranslatedFrequency(TranslatedFrequencyMasterDto tranDto) throws ApplicationException {
		try {
			TranslatedFrequencyMaster master = new TranslatedFrequencyMaster();
			master.setConvertedText(tranDto.getConvertedText());
			master.setDesc(tranDto.getDesc());
			master.setFrequencyId(tranDto.getFrequencyId());
			master.setLanguageId(tranDto.getLanguageId());
			master.setCreatedBy(tranDto.getCreatedBy());
			master.setCreatedDate(TimeZoneComponent.getDateByzone(tranDto.getUnitId()));
			master.setUpdatedBy(tranDto.getUpdatedBy());
			master.setUpdatedDate(TimeZoneComponent.getDateByzone(tranDto.getUnitId()));
			master.setOrganizationId(tranDto.getOrganizationId());
			master.setUnitId(tranDto.getUnitId());
			master.setStatus('A');
			save(master);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getConvertedLanguageListById(FrequencyRequest request)
			throws ApplicationException {
		try {
			List<TranslatedFrequencyMasterDto> translatedFrequencyList = sessionFactory.getCurrentSession()
					.createSQLQuery("select tran.frequency_desc as \"desc\", tran.converted_text as \"convertedText\" from public.t_translated_frequency tran "
							+" WHERE tran.status='A'AND tran.frequency_id=:frequencyId "
							+" AND tran.organization_id=:orgId ")
					.setParameter("orgId", request.getOrganizationId())
					.setParameter("frequencyId",request.getFrequencyId() != null ? request.getFrequencyId():0)
					.setResultTransformer(Transformers.aliasToBean(TranslatedFrequencyMasterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, translatedFrequencyList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<TranslatedFrequencyMasterDto> getTranslatedFrequencyListForUpdate(Frequency responseList) {
		try {
			List<TranslatedFrequencyMasterDto> translatedFrequencyList = sessionFactory.getCurrentSession()
					.createSQLQuery("select tran.frequency_desc as \"desc\", tran.converted_text as \"convertedText\", tran.language_id as \"languageId\" from public.t_translated_frequency tran "
							+" WHERE tran.status='A'AND tran.frequency_id=:frequencyId "
							+" AND tran.organization_id=:orgId "
							+" AND tran.unit_id=:unitId ")
					.setParameter("unitId", responseList.getUnitId())
					.setParameter("orgId", responseList.getOrganizationId())
					.setParameter("frequencyId",responseList.getId())
					.setResultTransformer(Transformers.aliasToBean(TranslatedFrequencyMasterDto.class)).list();

			return translatedFrequencyList;
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
