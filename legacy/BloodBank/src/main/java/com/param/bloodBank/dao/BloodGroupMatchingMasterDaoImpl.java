package com.param.bloodBank.dao;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodGroupMatchingMasterDto;
import com.param.bloodBank.model.BloodGroupMatchingMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodGroupMatchingMasterDaoImpl  extends GenericDao<BloodGroupMatchingMaster, Integer> implements IBloodGroupMatchingMasterDao , ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodGroupMatchingMasterDaoImpl.class);


	
	public BloodGroupMatchingMasterDaoImpl() {
		super(BloodGroupMatchingMaster.class);
	}
	
	
	@Autowired
	SessionFactory sessionFactory;


	@Override
	public Response saveBloodGroupMatchingMaster(List<BloodGroupMatchingMasterDto> bloodBagMatchingMasterDto) {
		try {
			
			bloodBagMatchingMasterDto.getFirst().getComponentId();
			
			List<BloodGroupMatchingMasterDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(" select blood_group_matching_id as \"bloodGroupMatchingId\" "
									+" from blood_bank.m_blood_group_matching_master  "
									+ "where component_id="+bloodBagMatchingMasterDto.getFirst().getComponentId()+" "
									+ " and organization_id="+bloodBagMatchingMasterDto.getFirst().getOrganizationId()+" and unit_id="+bloodBagMatchingMasterDto.getFirst().getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodGroupMatchingMasterDto.class)).list();
			if(list.size()>0) {
				
				Integer count = sessionFactory.getCurrentSession()
						.createSQLQuery("UPDATE blood_bank.m_blood_group_matching_master " 
										+ "SET status='I' " 
										+ "where component_id="+bloodBagMatchingMasterDto.getFirst().getComponentId()+" "
										+ " and organization_id="+bloodBagMatchingMasterDto.getFirst().getOrganizationId()+" and unit_id="+bloodBagMatchingMasterDto.getFirst().getUnitId())
						                .executeUpdate();
				
			}
			for (BloodGroupMatchingMasterDto bloodGroupMatchinList : bloodBagMatchingMasterDto) {
				BloodGroupMatchingMaster bloodBagCapacityMaster=new BloodGroupMatchingMaster();
				bloodBagCapacityMaster.setAvailability(bloodGroupMatchinList.getAvailability());
				bloodBagCapacityMaster.setDonorBloodGroupId(bloodGroupMatchinList.getDonorBloodGroupId());
				bloodBagCapacityMaster.setRecipientBloodGroupId(bloodGroupMatchinList.getRecipientBloodGroupId());
				bloodBagCapacityMaster.setComponentId(bloodGroupMatchinList.getComponentId());
				bloodBagCapacityMaster.setOrganizationId(bloodGroupMatchinList.getOrganizationId());
				bloodBagCapacityMaster.setUnitId(bloodGroupMatchinList.getUnitId());
				bloodBagCapacityMaster.setStatus(bloodGroupMatchinList.getStatus());
				bloodBagCapacityMaster.setCreatedBy(bloodGroupMatchinList.getCreatedBy());
				bloodBagCapacityMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodGroupMatchinList.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				bloodBagCapacityMaster.setUpdatedBy(bloodGroupMatchinList.getUpdatedBy());
				bloodBagCapacityMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodGroupMatchinList.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				
				save(bloodBagCapacityMaster);
				
			
				
			}
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodGroupMatchingMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodGroupMatchingMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response getBloodGroupMatchingMasterById(Integer componentId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodGroupMatchingMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_GROUP_MATCHING_MASTER_LIST")
					.setParameter("componentId", componentId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodGroupMatchingMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodGroupMatchingMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodGroupMatchingMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response getBloodGroupsByComponentAndDonorBloodGroupId(BloodGroupMatchingMasterDto bloodGroupMatchingMasterDto) throws ApplicationException {
		try {
			List<BloodGroupMatchingMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_GROUP_LIST_BY_COMPONENT_AND_DONOR_BLOOD_GROUP_ID")
					.setParameter("componentId", bloodGroupMatchingMasterDto.getComponentId())
					.setParameter("donorBloodGroupId", bloodGroupMatchingMasterDto.getDonorBloodGroupId())
					.setParameter("organizationId", bloodGroupMatchingMasterDto.getOrganizationId())
					.setParameter("unitId", bloodGroupMatchingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodGroupMatchingMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodGroupsByComponentAndDonorBloodGroupId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodGroupsByComponentAndDonorBloodGroupId failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	
}
