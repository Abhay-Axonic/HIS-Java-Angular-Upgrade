package com.param.bloodBank.dao;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBankTypeComponentMasterDto;
import com.param.bloodBank.model.BloodBankTypeComponentMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})

public class BloodBankTypeComponentMasterDaoImpl extends GenericDao< BloodBankTypeComponentMaster, Integer> implements IBloodBankTypeComponentMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankTypeComponentMasterDaoImpl.class);


	public BloodBankTypeComponentMasterDaoImpl() {
		super(BloodBankTypeComponentMaster.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveBloodBankTypeComponentMaster(BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto)
			throws ApplicationException {
		try{
			BloodBankTypeComponentMaster bloodBankTypeComponentMaster=new BloodBankTypeComponentMaster();
			bloodBankTypeComponentMaster.setBloodBagTypeId(bloodBankTypeComponentMasterDto.getBloodBagTypeId());
			bloodBankTypeComponentMaster.setBloodComponentId(bloodBankTypeComponentMasterDto.getBloodComponentId());
			bloodBankTypeComponentMaster.setOrganisationId(bloodBankTypeComponentMasterDto.getOrganizationId());
			bloodBankTypeComponentMaster.setUnitId(bloodBankTypeComponentMasterDto.getUnitId());
			bloodBankTypeComponentMaster.setCreatedBy(bloodBankTypeComponentMasterDto.getCreatedBy());
			bloodBankTypeComponentMaster.setStatus(bloodBankTypeComponentMasterDto.getStatus());
			bloodBankTypeComponentMaster.setIsDeleted('N');
			bloodBankTypeComponentMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodBankTypeComponentMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			bloodBankTypeComponentMaster.setUpdatedBy(bloodBankTypeComponentMasterDto.getUpdatedBy());
			bloodBankTypeComponentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBankTypeComponentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			save(bloodBankTypeComponentMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveBloodBankTypeComponentMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateBloodBankTypeComponentMaster(List<BloodBankTypeComponentMasterDto> listBloodBankTypeComponentMasterDto)
			throws ApplicationException {
		try{
			
			
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_BLOOD_BANK_TYPE_COMPONENT_MASTER")
					.setParameter("isDeleted", 'Y')
					.setParameter("organizationId", listBloodBankTypeComponentMasterDto.getFirst().getOrganizationId())
					.setParameter("unitId", listBloodBankTypeComponentMasterDto.getFirst().getUnitId())
					.setParameter("bloodBagTypeId", listBloodBankTypeComponentMasterDto.getFirst().getBloodBagTypeId())
					.executeUpdate();
			if (result > 0) {
				for (Iterator iterator = listBloodBankTypeComponentMasterDto.iterator(); iterator.hasNext();) {
					BloodBankTypeComponentMasterDto listBloodBankTypeComponentMaster = (BloodBankTypeComponentMasterDto) iterator
							.next();
					BloodBankTypeComponentMaster bloodBankTypeComponentMaster=new BloodBankTypeComponentMaster();
					bloodBankTypeComponentMaster.setBloodComponentId(listBloodBankTypeComponentMaster.getBloodComponentId());
					bloodBankTypeComponentMaster.setBloodBagTypeId(listBloodBankTypeComponentMaster.getBloodBagTypeId());
					bloodBankTypeComponentMaster.setOrganisationId(listBloodBankTypeComponentMaster.getOrganizationId());
					bloodBankTypeComponentMaster.setUnitId(listBloodBankTypeComponentMaster.getUnitId());
					bloodBankTypeComponentMaster.setIsDeleted('N');
					bloodBankTypeComponentMaster.setCreatedBy(listBloodBankTypeComponentMaster.getCreatedBy());
					bloodBankTypeComponentMaster.setStatus(listBloodBankTypeComponentMaster.getStatus());
					bloodBankTypeComponentMaster.setUpdatedBy(listBloodBankTypeComponentMaster.getUpdatedBy());
					bloodBankTypeComponentMaster.setCreatedDate(GlobalCommonDateUtils.getDate(listBloodBankTypeComponentMaster.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					bloodBankTypeComponentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(listBloodBankTypeComponentMaster.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

					
					save(bloodBankTypeComponentMaster);
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
			} 
		} catch (HibernateException he) {
			logger.error("updateBloodBankTypeComponentMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBloodBankTypeComponentMasterStatus(Integer bloodBagTypeId, Integer organizationId,
			Character status, Integer unitId) throws ApplicationException {
		try{
			if (bloodBagTypeId != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_BLOOD_BANK_TYPE_COMPONENT_MASTER_STATUS")
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setCharacter("status", status)
						.setParameter("bloodBagTypeId", bloodBagTypeId)
						.executeUpdate();
				if (result > 0) {
					if (status.equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if (status.equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateBloodBankTypeComponentMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateBloodBankTypeComponentMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankTypeComponentMasterList(Integer offset, Integer noOfRecordsPerPage)
			throws ApplicationException {
		try{
			List<BloodBankTypeComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_LIST")
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodBankTypeComponentMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getBloodBankTypeComponentMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBloodBankTypeComponentMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankTypeComponentMasterById(Integer bloodBagTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			List<BloodBankTypeComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_BY_ID")
					.setParameter("bloodBagTypeId", bloodBagTypeId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBankTypeComponentMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getBloodBankTypeComponentMasterById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBloodBankTypeComponentMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodBankTypeComponentMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			
			List<BloodBankTypeComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BLOOD_BANK_TYPE_COMPONENT_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBankTypeComponentMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getActiveBloodBankTypeComponentMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankTypeComponentMasterCount(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			BigInteger bloodBankTypeComponentMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_COUNT")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
				if (bloodBankTypeComponentMasterCount !=null ) {
					if(bloodBankTypeComponentMasterCount.compareTo(BigInteger.ZERO) == 1){
						return new Response(SUCCESS, SUCCESS_CODE, null, null, bloodBankTypeComponentMasterCount);
					}
					else {
						return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
					}
					
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE,COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
				}
		} catch (HibernateException he) {
			logger.error("getBloodBankTypeComponentMasterCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBloodBankTypeComponentMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkActiveBloodBankTypeComponentMasterAvaiable(
			BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto) throws ApplicationException {
		try{
			List<BloodBankTypeComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_ACTIVE_BLOOD_BANK_TYPE_COMPONENT_MASTER_AVAILABLE")
					.setParameter("bloodBagTypeId", bloodBankTypeComponentMasterDto.getBloodBagTypeId())
					.setParameter("organizationId", bloodBankTypeComponentMasterDto.getOrganizationId())
					.setParameter("unitId", bloodBankTypeComponentMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodBankTypeComponentMasterDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("checkActiveBloodBankTypeComponentMasterAvaiable failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("checkActiveBloodBankTypeComponentMasterAvaiable failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBagList(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<BloodBankTypeComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BAG_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBankTypeComponentMasterDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getBloodBagList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBloodBagList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodComponentList(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<BloodBankTypeComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_COMPONENT_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBankTypeComponentMasterDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getBloodComponentList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBloodComponentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDetailedComponentList(Integer bloodBagTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			List<BloodBankTypeComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_COMPONENT_LIST")
					.setParameter("bloodBagTypeId", bloodBagTypeId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBankTypeComponentMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("getDetailedComponentList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDetailedComponentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
