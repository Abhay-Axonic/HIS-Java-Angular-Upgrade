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

import com.param.bloodBank.dto.DonorRejectionMasterDto;
import com.param.bloodBank.model.DonorRejectionMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class DonorRejectionMasterDaoImpl extends GenericDao<DonorRejectionMaster, Integer> implements IDonorRejectionMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorRejectionMasterDaoImpl.class);


	public DonorRejectionMasterDaoImpl() {
		super(DonorRejectionMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveDonorRejectionMaster(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			DonorRejectionMaster donorRejectionMaster=new DonorRejectionMaster();
			donorRejectionMaster.setDonorRejectionCode(donorRejectionMasterDto.getDonorRejectionCode());
			donorRejectionMaster.setDonorRejectionDesc(donorRejectionMasterDto.getDonorRejectionDesc());
			donorRejectionMaster.setOrganizationId(donorRejectionMasterDto.getOrganizationId());
			donorRejectionMaster.setUnitId(donorRejectionMasterDto.getUnitId());
			donorRejectionMaster.setStatus(donorRejectionMasterDto.getStatus());
			donorRejectionMaster.setCreatedBy(donorRejectionMasterDto.getCreatedBy());
			donorRejectionMaster.setCreatedDate(GlobalCommonDateUtils.getDate(donorRejectionMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			donorRejectionMaster.setUpdatedBy(donorRejectionMasterDto.getUpdatedBy());
			donorRejectionMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(donorRejectionMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(donorRejectionMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveDonorRejectionMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveDonorRejectionMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateDonorRejectionMaster(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			DonorRejectionMaster donorRejectionMaster=findById(donorRejectionMasterDto.getDonorRejectionId());
			
			donorRejectionMaster.setDonorRejectionCode(donorRejectionMasterDto.getDonorRejectionCode());
			donorRejectionMaster.setDonorRejectionDesc(donorRejectionMasterDto.getDonorRejectionDesc());
			donorRejectionMaster.setOrganizationId(donorRejectionMasterDto.getOrganizationId());
			donorRejectionMaster.setUnitId(donorRejectionMasterDto.getUnitId());
			donorRejectionMaster.setUpdatedBy(donorRejectionMasterDto.getUpdatedBy());
			donorRejectionMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(donorRejectionMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(donorRejectionMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateDonorRejectionMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateDonorRejectionMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateDonorRejectionMasterStatus(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			DonorRejectionMaster donorRejectionMaster=findById(donorRejectionMasterDto.getDonorRejectionId());
			donorRejectionMaster.setStatus(donorRejectionMasterDto.getStatus());
			donorRejectionMaster.setUpdatedBy(donorRejectionMasterDto.getUpdatedBy());
			donorRejectionMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(donorRejectionMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(donorRejectionMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateDonorRejectionMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateDonorRejectionMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getDonorRejectionMasterList(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			List<DonorRejectionMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DONOR_REJECTION_MASTER_LIST")
					.setParameter("organizationId", donorRejectionMasterDto.getOrganizationId())
					.setParameter("unitId", donorRejectionMasterDto.getUnitId())
					.setFirstResult(donorRejectionMasterDto.getOffset() != null ? donorRejectionMasterDto.getOffset() : 0)
					.setMaxResults(donorRejectionMasterDto.getNoOfRecordsPerPage() != null ? donorRejectionMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DonorRejectionMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getDonorRejectionMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getDonorRejectionMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getDonorRejectionMasterById(Integer donorRejectionId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<DonorRejectionMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DONOR_REJECTION_MASTER_BY_ID")
					.setParameter("donorRejectionId", donorRejectionId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DonorRejectionMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getDonorRejectionMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getDonorRejectionMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveDonorRejectionMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<DonorRejectionMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_DONOR_REJECTION_MASTER")
				.setResultTransformer(Transformers.aliasToBean(DonorRejectionMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveDonorRejectionMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveDonorRejectionMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getDonorRejectionMasterCount(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(DonorRejectionMaster.class);
			crit.add(Restrictions.eq("organizationId", donorRejectionMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", donorRejectionMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getDonorRejectionMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getDonorRejectionMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getDonorRejectionMasterByName(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			List<DonorRejectionMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DONOR_REJECTION_MASTER_BY_NAME")
				.setParameter("donorRejectionDesc", donorRejectionMasterDto.getDonorRejectionDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(DonorRejectionMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getDonorRejectionMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getDonorRejectionMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getDonorRejectionMasterByNameNotId(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			List<DonorRejectionMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DONOR_REJECTION_MASTER_BY_NAME_NOT_ID")
				.setParameter("donorRejectionDesc", donorRejectionMasterDto.getDonorRejectionDesc().toLowerCase())
				.setParameter("donorRejectionId", donorRejectionMasterDto.getDonorRejectionId())
				.setResultTransformer(Transformers.aliasToBean(DonorRejectionMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getDonorRejectionMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getDonorRejectionMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}
