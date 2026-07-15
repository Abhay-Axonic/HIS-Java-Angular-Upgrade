package com.param.opd.coversheet.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;
import com.param.opd.coversheet.model.DoctorFavorites;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorFavoritesDaoImpl extends GenericDao<DoctorFavorites, Integer> implements IDoctorFavoritesDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(DoctorFavoritesDaoImpl.class);


	public DoctorFavoritesDaoImpl() {
		super(DoctorFavorites.class);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveDoctorFavorites(DoctorFavoritesDto doctorFavoritesDto) {
		try {
			DoctorFavorites doctorFavorites = mapper.map(doctorFavoritesDto, DoctorFavorites.class,
					"DoctorFavoritesDto_to_DoctorFavorites");
			doctorFavorites = save(doctorFavorites);
			return new Response(SUCCESS, SUCCESS_CODE, "Added to Favorites !!", null, null);
		} catch (HibernateException he) {
		logger.error("saveDoctorFavorites failed", he);
		} catch (Exception e) {
		logger.error("saveDoctorFavorites failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response isDoctorFavoritesExist(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException {
		try {
			List<DoctorFavoritesDto> listMedicationDto = sessionFactory.getCurrentSession().getNamedQuery("IS_DOCTOR_FAVORITES_EXIST")
                    .setParameter("doctorId", doctorFavoritesDto.getDoctorId())
                    .setParameter("itemId", doctorFavoritesDto.getItemId())
                    .setParameter("roaId", doctorFavoritesDto.getRoaId()!= null ? doctorFavoritesDto.getRoaId() :0)
                    .setParameter("doseId", doctorFavoritesDto.getDoseUnitId()!=null ? doctorFavoritesDto.getDoseUnitId(): 0)
                    .setParameter("doseUnit", doctorFavoritesDto.getDose()!=null ? doctorFavoritesDto.getDose() : 0.0)
                    .setParameter("frequencyId", doctorFavoritesDto.getFrequencyId())
                    .setParameter("orgId", doctorFavoritesDto.getOrganizationId())
                    .setParameter("unitId", doctorFavoritesDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(DoctorFavoritesDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("isDoctorFavoritesExist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusForDoctorFavorites(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException {
		try {
			if(doctorFavoritesDto.getDoctorFavoriteId() != null) {
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorFavorites docFav set "
					+ "docFav.status='I', "
					+ "docFav.updatedBy=:updatedBy, "
					+ "docFav.updatedDate=:updatedDate "
					+ "WHERE docFav.doctorId=:doctorId "
					+ "AND docFav.doctorFavoriteId=:doctorFavoriteId "
					+ "AND docFav.organizationId=:orgId "
					+ "AND docFav.unitId=:unitId ")
					.setParameter("doctorId", doctorFavoritesDto.getDoctorId())
					.setParameter("doctorFavoriteId", doctorFavoritesDto.getDoctorFavoriteId())
					.setParameter("orgId", doctorFavoritesDto.getOrganizationId())
					.setParameter("unitId", doctorFavoritesDto.getUnitId())
					.setParameter("updatedBy", doctorFavoritesDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(doctorFavoritesDto.getUpdatedDate(),"yyyy-M-dd HH:mm:ss")).executeUpdate();
			}else {
				sessionFactory.getCurrentSession().createQuery("UPDATE DoctorFavorites docFav set "
						+ "docFav.status='I', "
						+ "docFav.updatedBy=:updatedBy, "
						+ "docFav.updatedDate=:updatedDate "
						+ "WHERE docFav.doctorId=:doctorId "
						+ "AND docFav.itemId=:itemId "
						+ "AND docFav.organizationId=:orgId "
						+ "AND docFav.unitId=:unitId ")
				        .setParameter("doctorId", doctorFavoritesDto.getDoctorId())
						.setParameter("itemId", doctorFavoritesDto.getItemId())
						.setParameter("orgId", doctorFavoritesDto.getOrganizationId())
						.setParameter("unitId", doctorFavoritesDto.getUnitId())
						.setParameter("updatedBy", doctorFavoritesDto.getUpdatedBy())
						.setParameter("updatedDate", GlobalCommonDateUtils.getDate(doctorFavoritesDto.getUpdatedDate(),"yyyy-M-dd HH:mm:ss")).executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, "Removed from Favorites !!", null, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("updateStatusForDoctorFavorites failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response getDoctorFavoritesListByDoctorId(DoctorFavoritesDto doctorFavoritesDto) throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(doctorFavoritesDto.getUnitId()));
		    
			List<DoctorFavoritesDto> listMedicationDto;
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_FAVORITES_LIST_BY_DOC_ID").getQueryString();
			StringBuffer subQuery=new StringBuffer();
			if(doctorFavoritesDto.getDoctorId()!=null){
				subQuery.append(" AND docFav.doctor_id = :doctorId");
			}
			query = query.concat(subQuery.toString());
		Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("doctorId", doctorFavoritesDto.getDoctorId())
					.setParameter("orgId", doctorFavoritesDto.getOrganizationId())
					.setParameter("unitId", doctorFavoritesDto.getUnitId())
					 .setParameter("expDate", toDayDate);
			if (doctorFavoritesDto.getDoctorId() != null) {
				createQuery.setParameter("doctorId", doctorFavoritesDto.getDoctorId());
			}

			listMedicationDto=createQuery.setResultTransformer(Transformers.aliasToBean(DoctorFavoritesDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getDoctorFavoritesListByDoctorId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response doctorFavoritesServiceListByDocId(DoctorFavoritesDto doctorFavoritesDto)
			throws ApplicationException {
		try {
			
			List<DoctorFavoritesDto> listMedicationDto;
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_FAVORITES_SERVICE_LIST_BY_DOC_ID").getQueryString();
			StringBuffer subQuery=new StringBuffer();
			if(doctorFavoritesDto.getDoctorId()!=null){
				subQuery.append(" AND tom.ord_doctor_id=:doctorId ");
			}
			subQuery.append(" AND  msm.speciality_id in( select speciality_id from t_unit_speciality_mapper where unit_id =:unitId and is_rendering_speciality = 'Y' )");
		   	subQuery.append(" group by msm.service_master_id,msm.service_standard_name,msm.speciality_id,msm.sub_speciality_id,spl.speciality_name, mssm.sub_speciality_name ");
			
			subQuery.append(" order by  count(*) desc limit 10");
			query = query.concat(subQuery.toString());
			 Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", doctorFavoritesDto.getUnitId());
			if (doctorFavoritesDto.getDoctorId() != null) {
				createQuery.setParameter("doctorId", doctorFavoritesDto.getDoctorId());
			}

			listMedicationDto =createQuery.setResultTransformer(Transformers.aliasToBean(DoctorFavoritesDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("doctorFavoritesServiceListByDocId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMostlyUsedFavoritesServiceList(DoctorFavoritesDto doctorFavoritesDto)
			throws ApplicationException {
		try {
					
		    List<DoctorFavoritesDto> listMedicationDto;
		    
		    String query=sessionFactory.getCurrentSession().getNamedQuery("GET_MOSTLY_USED_FAVORITES_SERVICE_LIST").getQueryString();
		    StringBuffer subQuery=new StringBuffer();
		    if(doctorFavoritesDto.getDoctorId()!=null){
				subQuery.append(" AND tom.ord_doctor_id= :doctorId");
		    }
		    subQuery.append(" group by msm.service_master_id,msm.service_standard_name,msm.speciality_id,msm.sub_speciality_id,spl.speciality_name, mssm.sub_speciality_name ");
			
			subQuery.append(" order by  count(*) desc limit 10");
			query = query.concat(subQuery.toString());
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", doctorFavoritesDto.getUnitId())
					.setParameter("orgId", doctorFavoritesDto.getOrganizationId());
			if (doctorFavoritesDto.getDoctorId() != null) {
				createQuery.setParameter("doctorId", doctorFavoritesDto.getDoctorId());
			}

			listMedicationDto =createQuery.setResultTransformer(Transformers.aliasToBean(DoctorFavoritesDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMostlyUsedFavoritesServiceList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


}
