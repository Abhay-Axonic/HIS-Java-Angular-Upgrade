package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.SubSpecialityMasterDto;
import com.param.global.model.SubSpecialityMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class SubSpecialityMasterDaoImpl extends GenericDao<SubSpecialityMaster, Integer>
		implements ISubSpecialityMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(SubSpecialityMasterDaoImpl.class);


	public SubSpecialityMasterDaoImpl() {
		super(SubSpecialityMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getSubSpecialityMasterList(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		try {
			List<SubSpecialityMasterDto> subSpecialityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIALITY_LIST")
					.setParameter("orgId", subSpecialityMasterDto.getOrganizationId())
					.setFirstResult(subSpecialityMasterDto.getOffset() != null ? subSpecialityMasterDto.getOffset() : 0)
					.setMaxResults(subSpecialityMasterDto.getNoOfRecordsPerPage() != null ? subSpecialityMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, subSpecialityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getSubSpecialityMasterList failed", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addSubSpecialityMaster(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		try {
			SubSpecialityMaster subSpecialityMaster = mapper.map(subSpecialityMasterDto, SubSpecialityMaster.class,
					"SubSpecialityMasterDto_to_SubSpecialityMaster");
			subSpecialityMaster.setCreatedDate(TimeZoneComponent.getDateByzone(subSpecialityMasterDto.getUnitId() != null ?subSpecialityMasterDto.getUnitId():0));
			subSpecialityMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(subSpecialityMasterDto.getUnitId() != null ? subSpecialityMasterDto.getUnitId():0));
			subSpecialityMaster.setStatus('A');
			subSpecialityMaster.setIsModality(subSpecialityMaster.getIsModality()=='\u0000'?'N':subSpecialityMaster.getIsModality());
			subSpecialityMaster.setIsCTModality(subSpecialityMaster.getIsCTModality()=='\u0000'?'N':subSpecialityMaster.getIsCTModality());
			subSpecialityMaster.setIsMriModality(subSpecialityMaster.getIsMriModality()=='\u0000'?'N':subSpecialityMaster.getIsMriModality());
			subSpecialityMaster = save(subSpecialityMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		logger.error("addSubSpecialityMaster failed", he);
		} catch (Exception e) {
		logger.error("addSubSpecialityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response editSubSpecialityMaster(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateSubSpecialityMaster(SubSpecialityMasterDto subSpecialityMasterDto)
			throws ApplicationException {
		try {
			// //System.out.println(SpecialityMasterDto.getspecialityId()+"
			// "+SpecialityMasterDto.getSpecialityName());
			SubSpecialityMaster Speciality = findById(subSpecialityMasterDto.getSubSpecialityMasterId());
			Speciality.setSubSpecialityMasterName(subSpecialityMasterDto.getSubSpecialityMasterName());
			Speciality.setSubSpecialityMasterCode(subSpecialityMasterDto.getSubSpecialityMasterCode());
			Speciality.setSubSpecialityMasterName(subSpecialityMasterDto.getSubSpecialityMasterName());
			Speciality.setSpecialityId(subSpecialityMasterDto.getSpecialityId());
			Speciality.setGeneralLedgerId(subSpecialityMasterDto.getGeneralLedgerId());
			Speciality.setSubHeaderName(subSpecialityMasterDto.getSubHeaderName() != null ? subSpecialityMasterDto.getSubHeaderName() : null);
			Speciality.setUpdatedBy(subSpecialityMasterDto.getUpdatedBy());
			Speciality.setUpdatedDate(GlobalCommonDateUtils.getDate(subSpecialityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			Speciality.setIsModality(subSpecialityMasterDto.getIsModality());
			Speciality.setIsCTModality(subSpecialityMasterDto.getIsCTModality());
			Speciality.setIsMriModality(subSpecialityMasterDto.getIsMriModality());
			update(Speciality);
			return new Response<SubSpecialityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateSubSpecialityMaster failed", he);
			return new Response<SubSpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateSubSpecialityMaster failed", e);
			return new Response<SubSpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getSubSpecialityByName(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		try {
			List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIALITY_LIST_BY_NAME")
					.setParameter("subSpecialityName", subSpecialityMasterDto.getSubSpecialityMasterName().toLowerCase())
					.setParameter("orgId", subSpecialityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			if (listSubSpecialityMasterDto != null) {
				return new Response(SUCCESS, null, null, listSubSpecialityMasterDto, null);
			}
		} catch (HibernateException he) {
		logger.error("getSubSpecialityByName failed", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityByName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubActiveSpecialityList() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getSubSpecialityById(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		try {
			List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIALITY_LIST_BY_ID")
					.setParameter("subSpecialityId", subSpecialityMasterDto.getSubSpecialityMasterId())
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			if (listSubSpecialityMasterDto != null) {
				return new Response(SUCCESS, null, null, listSubSpecialityMasterDto, null);
			}
		} catch (HibernateException he) {
		logger.error("getSubSpecialityById failed", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateSubSpecialityStatus(SubSpecialityMasterDto subSpecialityMasterDto) {
		try {
			SubSpecialityMaster subSpeciality = findById(subSpecialityMasterDto.getSubSpecialityMasterId());

			subSpeciality.setStatus(subSpecialityMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			subSpeciality.setUpdatedBy(subSpecialityMasterDto.getUpdatedBy());
			subSpeciality.setUpdatedDate(
					GlobalCommonDateUtils.getDate(subSpecialityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(subSpeciality);
			return new Response<SubSpecialityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateSubSpecialityStatus failed", he);
			return new Response<SubSpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateSubSpecialityStatus failed", e);
			return new Response<SubSpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getSubSpecialityByNameNotId(SubSpecialityMasterDto subspecialityMasterDto)
			throws ApplicationException {
		try {
			List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIALITY_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("subSpecialityName", subspecialityMasterDto.getSubSpecialityMasterName().toLowerCase())
					.setParameter("subSpecialityMasterId", subspecialityMasterDto.getSubSpecialityMasterId())
					.setParameter("orgId", subspecialityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			if (listSubSpecialityMasterDto != null) {
				return new Response(SUCCESS, null, null, listSubSpecialityMasterDto, null);
			}
		} catch (HibernateException he) {
		logger.error("getSubSpecialityByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityByNameNotId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCount(SubSpecialityMasterDto subspecialityMasterDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(SubSpecialityMaster.class);
			c.add(Restrictions.eq("organizationId", subspecialityMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (Exception e) {
		logger.error("getCount failed", e);

			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getSubSpecialityBySpecialityId(SubSpecialityMasterDto subspecialityMasterDto)
			throws ApplicationException {
		try {
				List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SUBSPECIALITY_BY_SPECIALITY_ID")
						.setParameter("specialityId", subspecialityMasterDto.getSpecialityId())
						.setParameter("orgId", subspecialityMasterDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
				return new Response<>(SUCCESS, null, null, listSubSpecialityMasterDto, null);
 		}catch (HibernateException he) {
 		logger.error("getSubSpecialityBySpecialityId failed", he);
 		} catch (Exception e) {
		logger.error("getSubSpecialityBySpecialityId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubspecialityNotInUnit(int orgId,int unitId,int specialityId) throws ApplicationException {
		try {
			List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUBSPECIALITY_BY_SPECIALITY_ID_NOT_MAPPED_TO_UNIT")
					.setParameter("specialityId",specialityId)
					.setParameter("orgId",orgId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, listSubSpecialityMasterDto, null);
		}catch (HibernateException he) {
		logger.error("getSubspecialityNotInUnit failed", he);
		} catch (Exception e) {
	logger.error("getSubspecialityNotInUnit failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSubSpecialityBySpecialityId(SubSpecialityMasterDto masterDto) throws ApplicationException {
		try {
			List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SUBSPECIALITY_BY_SPECIALITY_ID")
					.setParameter("specialityId", masterDto.getSpecialityId())
					.setParameter("orgId", masterDto.getOrganizationId())
					.setParameter("unitId", masterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, listSubSpecialityMasterDto, null);
		}catch (HibernateException he) {
		logger.error("getUnitSubSpecialityBySpecialityId failed", he);
		} catch (Exception e) {
	logger.error("getUnitSubSpecialityBySpecialityId failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityList(int orgId, int unitId) throws ApplicationException {
		try {
			
			List<SubSpecialityMasterDto> subSpecialityMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery(" select " + 
							"	spm.sub_speciality_id as \"subSpecialityMasterId\", " + 
							"	spm.sub_speciality_name as \"subSpecialityMasterName\"," + 
							"	spm.sub_speciality_code as \"subSpecialityMasterCode\"," + 
							"	spm.speciality_id as \"specialityId\"," + 
							"	spm.status as \"status\"," + 
							"	spm.is_modality as \"isModality\" " + 
							" from " + 
							"	doctor.m_sub_speciality_master spm " + 
							" inner join public.t_unit_sub_speciality_mapper mppr on " + 
							"	mppr.sub_speciality_id = spm.sub_speciality_id " + 
							" where " + 
							"	spm.organization_id =:orgId " + 
							"	and mppr.unit_id =:unitId " + 
							" order by " + 
							"	spm.sub_speciality_name ASC ")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, subSpecialityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getSubSpecialityList failed", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityByDoctorId(DoctorMasterDto dto) throws ApplicationException {
		try {
			DoctorMasterDto dto1 =(DoctorMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(" select dsm.sub_speciality_id \"subSpecialityId\" from doctor.m_sub_speciality_master  dsm" + 
							" inner join doctor.m_doctor_master dm" + 
							" on dm.sub_speciality_id=dsm.sub_speciality_id" + 
							" where dm.doctor_id=:doctorId ")
					.setParameter("doctorId", dto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).uniqueResult();
			if(dto1!=null) {
			        return new Response(SUCCESS, null, null, null, dto1);
			}else {
				return new Response(ERROR, null, null, null, null);
			}

		} catch (HibernateException he) {
		logger.error("getSubSpecialityByDoctorId failed", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityByDoctorId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response subSpecialityMasterUniversalSearch(SubSpecialityMasterDto subSpecialityMasterDto)
			throws ApplicationException {
		try {
			List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_SUBSPECIALITY_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", subSpecialityMasterDto.getOrganizationId())
					.setParameter("subSpecialityMasterName", "%"+subSpecialityMasterDto.getSubSpecialityMasterName().trim().toLowerCase()+"%")
					.setFirstResult(subSpecialityMasterDto.getOffset() != null ? subSpecialityMasterDto.getOffset() : 0)
					.setMaxResults(subSpecialityMasterDto.getNoOfRecordsPerPage() != null ? subSpecialityMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listSubSpecialityMasterDto, null);
		}catch (HibernateException he) {
		logger.error("subSpecialityMasterUniversalSearch failed", he);
		} catch (Exception e) {
		logger.error("subSpecialityMasterUniversalSearch failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSubSpecialityBySpecialityList(SubSpecialityMasterDto subSpecialityMasterDto) {
		try {
			if(subSpecialityMasterDto.getSpecialityIds().size() > 0)
			{
				List<SubSpecialityMasterDto> listSubSpecialityMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SUBSPECIALITY_BY_SPECIALITY_IDS")
						.setParameterList("specialityIds", subSpecialityMasterDto.getSpecialityIds())
						.setParameter("orgId", subSpecialityMasterDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
				return new Response<>(SUCCESS, null, null, listSubSpecialityMasterDto, null);
			}
			
		}catch (HibernateException he) {
		logger.error("getSubSpecialityBySpecialityList failed", he);
		} catch (Exception e) {
	logger.error("getSubSpecialityBySpecialityList failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityMRIandCT(SubSpecialityMasterDto subSpecialityMasterDto)
			throws ApplicationException {

		try {
			
			List<SubSpecialityMasterDto> subSpecialityMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery(" select " + 
							"	spm.sub_speciality_id as \"subSpecialityMasterId\"," + 
							"	spm.is_mri_modality as \"isMriModality\", " + 
							"	spm.is_ct_modality as \"isCTModality\" " + 
							" from " + 
							"	doctor.m_sub_speciality_master spm  " + 
							" where " + 
							"	(spm.is_mri_modality = 'Y' or spm.is_ct_modality = 'Y') and  spm.organization_id =:orgId ")
					.setParameter("orgId", subSpecialityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SubSpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, subSpecialityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getSubSpecialityMRIandCT failed", he);
		} catch (Exception e) {
		logger.error("getSubSpecialityMRIandCT failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}

}
