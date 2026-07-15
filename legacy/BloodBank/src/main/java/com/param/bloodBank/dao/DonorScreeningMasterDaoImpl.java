package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.CampMasterDto;
import com.param.bloodBank.dto.DefferedTypeMasterDto;
import com.param.bloodBank.model.CampMaster;
import com.param.bloodBank.model.DonorScreeningMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.GenderMasterDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})	
public class DonorScreeningMasterDaoImpl extends GenericDao<DonorScreeningMaster, Integer> implements IDonorScreeningMasterDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorScreeningMasterDaoImpl.class);


	public DonorScreeningMasterDaoImpl() {
		super(DonorScreeningMaster.class);
	}
	@Autowired SessionFactory sessionFactory;
	
	@Override
	public Response saveDonorScreenigMaster(DonorScreeningMasterDto dto)
			throws ApplicationException {
		try{
			List<GenderMasterDto> genderList = dto.getGenderList();
			if(genderList.size()>0 && genderList!=null)
			{
				for (GenderMasterDto genderIdDto : genderList) {
					DonorScreeningMasterDto mst = new DonorScreeningMasterDto();
					mst = (DonorScreeningMasterDto)sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE")
							.setParameter("organizationId", dto.getOrganizationId())
							.setParameter("unitId", dto.getUnitId())
							.setParameter("parameter", dto.getParameter())
							.setParameter("genderId",genderIdDto.getId())
							.setResultTransformer(Transformers.aliasToBean(DonorScreeningMasterDto.class)).uniqueResult();
					
					 if((mst!=null) && (mst.getId()!=null))
					 {
						 return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
					 }
					DonorScreeningMaster master = new DonorScreeningMaster();
					master.setParameter(dto.getParameter());
					master.setGenderId(genderIdDto.getId());
					master.setDeferredTypeId(dto.getDeferredTypeId());
					master.setCreatedBy(dto.getCreatedBy());
					master.setUpdatedBy(dto.getUpdatedBy());
					master.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyy HH:mm:ss"));
					master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyy HH:mm:ss"));
					master.setStatus(dto.getStatus());
					master.setUnitId(dto.getUnitId());
					master.setOrganizationId(dto.getOrganizationId());
					save(master);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, null, null);
			}
			
		}catch(HibernateException he){
			logger.error("saveDonorScreenigMaster failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("saveDonorScreenigMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, null,null, null, null);
	}

	@Override
	public Response getAllDonorScreeningList(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try{
			List<DonorScreeningMasterDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DONOR_SCREENING_LIST")
					.setParameter("organizationId", donorScreeningMasterDto.getOrganizationId())
					.setParameter("unitId", donorScreeningMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DonorScreeningMasterDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, list, null);
			
		}catch(HibernateException he){
			logger.error("getAllDonorScreeningList failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("getAllDonorScreeningList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorScreeningMaster(DonorScreeningMasterDto dto)
			throws ApplicationException {
		try{
			
			List<GenderMasterDto> genderList = dto.getGenderList();
			if(genderList.size()>0 && genderList!=null)
			{
				for (GenderMasterDto genderIdDto : genderList) {
					DonorScreeningMasterDto mst = new DonorScreeningMasterDto();
					mst = (DonorScreeningMasterDto)sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE")
							.setParameter("organizationId", dto.getOrganizationId())
							.setParameter("unitId", dto.getUnitId())
							.setParameter("parameter", dto.getParameter())
							.setParameter("genderId",genderIdDto.getId())
							.setResultTransformer(Transformers.aliasToBean(DonorScreeningMasterDto.class)).uniqueResult();
					
					 if((mst!=null) && (mst.getId()!=null))
					 {
						 return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
					 }
					DonorScreeningMaster master = findById(dto.getId());
					master.setParameter(dto.getParameter());
					master.setGenderId(genderIdDto.getId());
					master.setDeferredTypeId(dto.getDeferredTypeId());
					master.setCreatedBy(dto.getCreatedBy());
					master.setUpdatedBy(dto.getUpdatedBy());
					master.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyy HH:mm:ss"));
					master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyy HH:mm:ss"));
					master.setStatus(dto.getStatus());
					master.setUnitId(dto.getUnitId());
					master.setOrganizationId(dto.getOrganizationId());
					update(master);
				}
				return new Response<>(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, null);
			}
		}catch(HibernateException he){
			logger.error("updateDonorScreeningMaster failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("updateDonorScreeningMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, null,null, null, null);
	}

	@Override
	public Response updateDonorScreeningMasterStatus(DonorScreeningMasterDto dto)
			throws ApplicationException {
		try{
			
			DonorScreeningMaster mstr = findById(dto.getId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			mstr.setStatus(dto.getStatus());
			update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null); 
			}catch(HibernateException he){
				logger.error("updateDonorScreeningMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			}catch(Exception e){
				logger.error("updateDonorScreeningMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getDonorScreeningListByGenderId(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try{
			List<DonorScreeningMasterDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DONOR_SCREENING_LIST_GENDER_ID")
					.setParameter("organizationId", donorScreeningMasterDto.getOrganizationId())
					.setParameter("unitId", donorScreeningMasterDto.getOrganizationId())
					.setParameter("genderId", donorScreeningMasterDto.getGenderId())
					.setResultTransformer(Transformers.aliasToBean(DonorScreeningMasterDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, list, null);
			
		}catch(HibernateException he){
			logger.error("getDonorScreeningListByGenderId failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("getDonorScreeningListByGenderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDefferedTypeMaster() throws ApplicationException {
		try{
			List<DefferedTypeMasterDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEFFERD_TYPE_LIST")
					.setResultTransformer(Transformers.aliasToBean(DefferedTypeMasterDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, list, null);
			
		}catch(HibernateException he){
			logger.error("getDefferedTypeMaster failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("getDefferedTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDonorScreeningListPaginationList(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try {
			List<DonorScreeningMasterDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DONOR_SCREENING_LIST")
					.setParameter("organizationId", donorScreeningMasterDto.getOrganizationId())
					.setParameter("unitId", donorScreeningMasterDto.getOrganizationId())
					.setFirstResult(donorScreeningMasterDto.getOffset() != null ? donorScreeningMasterDto.getOffset()  : 0)
					.setMaxResults(donorScreeningMasterDto.getNoOfRecordsPerPage() != null ? donorScreeningMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(DonorScreeningMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (Exception e) {
			logger.error("getAllDonorScreeningListPaginationList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDonorScreeningListCount(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(DonorScreeningMaster.class);
			crit.add(Restrictions.eq("organizationId", donorScreeningMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", donorScreeningMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
			logger.error("getAllDonorScreeningListCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAllDonorScreeningListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
