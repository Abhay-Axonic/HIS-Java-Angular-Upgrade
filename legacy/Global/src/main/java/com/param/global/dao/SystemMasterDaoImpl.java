package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.MasterOfSytemDto;
import com.param.global.dto.SystemMasterDto;
import com.param.global.model.SystemMaster;
import com.param.global.model.SystemMasterGenderMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SystemMasterDaoImpl extends GenericDao<SystemMaster, Integer>
		implements ISystemMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(SystemMasterDaoImpl.class);


	public SystemMasterDaoImpl() {
		super(SystemMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getListOfSystemMaster(SystemMasterDto systemMasterDto) throws ApplicationException {
		try {
			Session statlessSession = sessionFactory.getCurrentSession();
		
			/* List<GeneralExaminationAppoMapperDto> listGeneralExaminationAppoMapperDto =
			  statlessSession.getNamedQuery("GET_LIST_SYSTEM_MASTER")
			  .setParameter("unitId", systemMasterDto.getUnitId())
			  .setParameter("organizationId",
			  systemMasterDto.getOrganizationId()).list();
			  statlessSession.close();*/
			 

			List<SystemMaster> listSystemMaster = statlessSession
					.createQuery(
							"from SystemMaster p where"
//							+ " unit_id=:unitId "
							+ "  organization_id=:organizationId "
							+ " AND status = 'A' and (typeId=:typeId OR typeId=3) AND (genderId=:genderId OR genderId=4)")
					.setParameter("genderId", systemMasterDto.getGenderId())
//					.setParameter("unitId", systemMasterDto.getUnitId())
					.setParameter("typeId", systemMasterDto.getTypeId())
					.setParameter("organizationId", systemMasterDto.getOrganizationId()).list();

			for (int i = 0; i < listSystemMaster.size(); i++) {

				for (int j = 0; j < listSystemMaster.get(i).getSystemObervationMasterList().size(); j++) {
					listSystemMaster.get(i).getSystemObervationMasterList().get(j).setSystemMaster(null);

					/*
					 * for(int k=0; k <
					 * listSystemMaster.get(i).getSystemObervationMasterList().
					 * get(j).getSystemObervationPropertyMasterList().size();
					 * k++){
					 * listSystemMaster.get(i).getSystemObervationMasterList().
					 * get(j).getSystemObervationPropertyMasterList().get(k) }
					 */
				}

			}
			/*
			 * Iterator i = listSystemMaster.iterator(); while(i.hasNext()){
			 * SystemMaster sys = (SystemMaster) i.next(); Iterator i1 =
			 * sys.getSystemObervationMasterList().iterator();
			 * while(i1.hasNext()){
			 * 
			 * } }
			 */

			return new Response(SUCCESS, SUCCESS_CODE, null, listSystemMaster, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getListOfSystemMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveSystemMaster(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			SystemMaster systemMaster = new SystemMaster();
			systemMaster.setSystemName(masterOfSytemDto.getSystemName());
			systemMaster.setSystemCode(masterOfSytemDto.getSystemCode());
			systemMaster.setStatus(masterOfSytemDto.getStatus());
			systemMaster.setOrganizationId(masterOfSytemDto.getOrganizationId());
			systemMaster.setUnitId(masterOfSytemDto.getUnitId());
			systemMaster.setCreatedBy(masterOfSytemDto.getCreatedBy());
			systemMaster.setUpdatedBy(masterOfSytemDto.getUpdatedBy());
			systemMaster.setCreatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
			systemMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
			systemMaster.setTypeId(masterOfSytemDto.getTypeId());
			 sessionFactory.getCurrentSession().save(systemMaster);
			
			  for(MasterOfSytemDto genderListObj:masterOfSytemDto.getGenderIdList()){
				  SystemMasterGenderMapper systemMasterGenderMapper= new SystemMasterGenderMapper();
				  systemMasterGenderMapper.setSystemId(systemMaster.getSystemId());
				  systemMasterGenderMapper.setGenderId(genderListObj.getId());
				 // systemMasterGenderMapper.setGenderDesc(genderListObj.getGenderDesc());
				  systemMasterGenderMapper.setUnitId(masterOfSytemDto.getUnitId());
				  systemMasterGenderMapper.setOrganizationId(masterOfSytemDto.getOrganizationId());
				  systemMasterGenderMapper.setCreatedBy(masterOfSytemDto.getCreatedBy());
				  systemMasterGenderMapper.setStatus('A');
				  systemMasterGenderMapper.setCreatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
				  systemMasterGenderMapper.setUpdatedBy(masterOfSytemDto.getCreatedBy());
				  systemMasterGenderMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
					     sessionFactory.getCurrentSession().save(systemMasterGenderMapper);

				   }
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveSystemMaster failed", he);
		} catch (Exception e) {
		logger.error("saveSystemMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getSystemMaster(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			List<MasterOfSytemDto> listmasterOfSytemDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SYSTEM_LIST")
//					.setParameter("unitId", masterOfSytemDto.getUnitId())
					.setParameter("organizationId", masterOfSytemDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MasterOfSytemDto.class)).list();
			return new Response(SUCCESS, null, null, listmasterOfSytemDto, null);

		} catch (HibernateException he) {
		logger.error("getSystemMaster failed", he);
		} catch (Exception e) {
		logger.error("getSystemMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateSystemMasterStatus(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {

			SystemMaster systemMaster = findById(masterOfSytemDto.getSystemId());
			systemMaster.setStatus(masterOfSytemDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			systemMaster.setUpdatedBy(masterOfSytemDto.getUpdatedBy());
			systemMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(masterOfSytemDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(systemMaster);
			return new Response<MasterOfSytemDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateSystemMasterStatus failed", he);
			return new Response<MasterOfSytemDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateSystemMasterStatus failed", e);
			return new Response<MasterOfSytemDto, Integer>(ERROR, null, null, null, null);
		}
	}

	// added by dinesh jagatap on 21-04-18 Start
	@Override
	public Response getListOfSystemMasterById(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			MasterOfSytemDto listSystemMaster = (MasterOfSytemDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_SYSTEM_MASTER_BY_ID")
					.setParameter("systemId", masterOfSytemDto.getSystemId())
					.setParameter("organizationId", masterOfSytemDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MasterOfSytemDto.class)).uniqueResult();
			
			List<MasterOfSytemDto> genderList = 	sessionFactory.getCurrentSession()
				     .createSQLQuery(" select smgm.system_id as\"systemId\",smgm.gender_id as\"id\",gm.gender_name as\"genderDesc\"" 
								+ " from  emr.m_system_master_gender_mapper smgm "
								+ " join public.m_gender_master gm  on  smgm.gender_id=gm.gender_id "
								+ " WHERE smgm.organization_id =:orgId "
//								+ " AND  smgm.unit_id =:unitId "
				                + " AND smgm.status ='A' "
				                + " AND smgm.system_id =:systemId ") 
				        .setParameter("orgId", masterOfSytemDto.getOrganizationId())
//						.setParameter("unitId", masterOfSytemDto.getUnitId())
					    .setParameter("systemId", masterOfSytemDto.getSystemId())
						.setResultTransformer(Transformers.aliasToBean(MasterOfSytemDto.class)).list();
			return new Response(SUCCESS, null, null, genderList, listSystemMaster);
		} catch (Exception e) {
		logger.error("getListOfSystemMasterById failed", e);
			return new Response<MasterOfSytemDto, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response updateSystemMaster(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			SystemMaster systemMaster = new SystemMaster();
			systemMaster.setSystemId(masterOfSytemDto.getSystemId());
			systemMaster.setSystemName(masterOfSytemDto.getSystemName());
			systemMaster.setSystemCode(masterOfSytemDto.getSystemCode());
			systemMaster.setStatus(masterOfSytemDto.getStatus());
			systemMaster.setOrganizationId(masterOfSytemDto.getOrganizationId());
			systemMaster.setUnitId(masterOfSytemDto.getUnitId());
			systemMaster.setCreatedBy(masterOfSytemDto.getCreatedBy());
			systemMaster.setUpdatedBy(masterOfSytemDto.getUpdatedBy());
			systemMaster.setCreatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
			systemMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
			systemMaster.setTypeId(masterOfSytemDto.getTypeId());
			 sessionFactory.getCurrentSession().update(systemMaster);
			
			Integer count=sessionFactory.getCurrentSession().createQuery(" update SystemMasterGenderMapper smgm set status='I' where smgm.systemId=:systemId "	)
	     			.setParameter("systemId", masterOfSytemDto.getSystemId()).executeUpdate();
			
			for(MasterOfSytemDto genderListObj:masterOfSytemDto.getGenderIdList()){
				  SystemMasterGenderMapper systemMasterGenderMapper= new SystemMasterGenderMapper();
				  systemMasterGenderMapper.setSystemId(systemMaster.getSystemId());
				  systemMasterGenderMapper.setGenderId(genderListObj.getId());
				 // systemMasterGenderMapper.setGenderDesc(genderListObj.getGenderDesc());
				  systemMasterGenderMapper.setUnitId(masterOfSytemDto.getUnitId());
				  systemMasterGenderMapper.setOrganizationId(masterOfSytemDto.getOrganizationId());
				  systemMasterGenderMapper.setCreatedBy(masterOfSytemDto.getCreatedBy());
				  systemMasterGenderMapper.setStatus('A');
				  systemMasterGenderMapper.setCreatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
				  systemMasterGenderMapper.setUpdatedBy(masterOfSytemDto.getCreatedBy());
				  systemMasterGenderMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(masterOfSytemDto.getUnitId()));
					     sessionFactory.getCurrentSession().save(systemMasterGenderMapper);

				   }
			return new Response<MasterOfSytemDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("updateSystemMaster failed", e);
		}
		return new Response<MasterOfSytemDto, Integer>(ERROR, null, null, null, null);
	}

	@Override
	public Response getListOfSystemMasterByType(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			// if (masterOfSytemDto.getTypeId() == 1 ||
			// masterOfSytemDto.getTypeId() == 3) {

			List<SystemMaster> listSystemMasterType = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_SYSTEM_MASTER_BY_TYPE")
					.setParameter("typeId", masterOfSytemDto.getTypeId())
//					.setParameter("unitId", masterOfSytemDto.getUnitId())
					.setParameter("organizationId", masterOfSytemDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MasterOfSytemDto.class)).list();
			return new Response(SUCCESS, null, null, listSystemMasterType, null);

		} catch (Exception e) {
		logger.error("getListOfSystemMasterByType failed", e);
		}
		return new Response<MasterOfSytemDto, Integer>(ERROR, null, null, null, null);
	}

	@Override
	public Response getSystemGenderListById(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
		List<MasterOfSytemDto> genderList = sessionFactory.getCurrentSession()
			     .createSQLQuery(" select smgm.system_id as\"systemId\",smgm.gender_id as\"id\",gm.gender_name as\"genderDesc\"" 
							+ " from  emr.m_system_master_gender_mapper smgm "
							+ " join public.m_gender_master gm  on  smgm.gender_id=gm.gender_id "
							+ " WHERE smgm.organization_id =:orgId "
//							+ " AND  smgm.unit_id =:unitId "
			                + " AND smgm.status ='A' "
			                + " AND smgm.system_id =:systemId ") 
			        .setParameter("orgId", masterOfSytemDto.getOrganizationId())
//					.setParameter("unitId", masterOfSytemDto.getUnitId())
				    .setParameter("systemId", masterOfSytemDto.getSystemId())
					.setResultTransformer(Transformers.aliasToBean(MasterOfSytemDto.class)).list();
		return new Response(SUCCESS, null, null, genderList, null);
	} catch (Exception e) {
	logger.error("getSystemGenderListById failed", e);

	return new Response<MasterOfSytemDto, Integer>(ERROR, null, null, null, null);
	}
}
}
