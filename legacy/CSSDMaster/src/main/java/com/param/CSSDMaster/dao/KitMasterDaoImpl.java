package com.param.CSSDMaster.dao;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSDMaster.dto.InstrumentMasterDto;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;
import com.param.CSSDMaster.dto.KitMasterDto;
import com.param.CSSDMaster.dto.SurgeonDto;
import com.param.CSSDMaster.model.KitMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitMasterDaoImpl extends GenericDao<KitMaster, Integer> implements IKitMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(KitMasterDaoImpl.class);


	public KitMasterDaoImpl() {
		super(KitMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response getAllKitList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_KIT_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setFirstResult(offset != null ?offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(KitMasterDto.class)).list();
			/*
			 * if (!MasterDtoList.isEmpty()) { for (KitMasterDto kitMasterDto :
			 * MasterDtoList) {
			 * 
			 * List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
			 * .getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID").setParameter("kitId",
			 * kitMasterDto.getKitId())
			 * .setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class))
			 * .list(); kitMasterDto.setKitInstrumentList(kitInstrumentList); } }
			 */
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getAllKitList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAllKitList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			
		}
	}
	
	
	@Override
	public Response getAllSurgeonList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<SurgeonDto> surgeonDtoList = statelessSession.getNamedQuery("GET_ALL_SURGEON_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(SurgeonDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, surgeonDtoList, null);
		} catch (HibernateException he) {
			logger.error("getAllSurgeonList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAllSurgeonList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			
		}
	}
	
	@Override
	public Response getActiveKitList(Integer organizationId,Integer unitId,Integer storeId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitMasterDto> MasterDtoList = statelessSession.getNamedQuery("GET_ACTIVE_KIT_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setParameter("storeId",storeId)
					.setResultTransformer(Transformers.aliasToBean(KitMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveKitList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveKitList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response searchKitByKitName(Integer organizationId,Integer unitId,String name) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitMasterDto> MasterDtoList = statelessSession.getNamedQuery("SEARCH_KIT_LIST_BY_KIT_NAME")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setParameter("name", "%" + name.toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(KitMasterDto.class)).list();
			
			if (!MasterDtoList.isEmpty()) {
				for (KitMasterDto kitMasterDto : MasterDtoList) {

					List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
							.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID").setParameter("kitId", kitMasterDto.getKitId())
							.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
					kitMasterDto.setKitInstrumentList(kitInstrumentList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("searchKitByKitName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("searchKitByKitName failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response searchInstrumentByKitName(Integer organizationId,Integer unitId,Integer storeId,String name) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitMasterDto> MasterDtoList = statelessSession.getNamedQuery("SEARCH_INSTURMENT_LIST_BY_KIT_NAME")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setParameter("storeId",storeId)
					.setParameter("name", "%" + name.toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(KitMasterDto.class)).list();
			
			if (!MasterDtoList.isEmpty()) {
				for (KitMasterDto kitMasterDto : MasterDtoList) {

					List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
							.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID").setParameter("kitId", kitMasterDto.getKitId())
							.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
					kitMasterDto.setKitInstrumentList(kitInstrumentList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("searchInstrumentByKitName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("searchInstrumentByKitName failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	@Override
	public Response searchInstrumentAsKit(Integer organizationId,Integer unitId,String name) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitMasterDto> MasterDtoList = statelessSession.createSQLQuery(" select code as\"code\",name as \"name\",id as\"kitId\" "
					+ " from public.m_item where (is_cssd=true or is_sterilize=true) "
					+ " AND ( lower(name) LIKE :name  or lower(code) LIKE :name ) "
					+ " AND organization_id=:organizationId  AND unit_id=:unitId ")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setParameter("name", "%" + name.toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(KitMasterDto.class)).list();
			if (!MasterDtoList.isEmpty()) {
				for (KitMasterDto kitMasterDto : MasterDtoList) {

					List<KitInstrumentMapperDto> kitInstrumentList = new ArrayList<>();
							KitInstrumentMapperDto mapperDto = new KitInstrumentMapperDto();
							mapperDto.setInstrumentId(kitMasterDto.getKitId());
							mapperDto.setInstrumentCode(kitMasterDto.getCode());
							mapperDto.setInstrumentName(kitMasterDto.getName());
							mapperDto.setQuantity(1);
							kitInstrumentList.add(mapperDto);
					kitMasterDto.setKitInstrumentList(kitInstrumentList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, MasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("searchInstrumentAsKit failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("searchInstrumentAsKit failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	
	@Override
	public Response searchInstrumentByName(Integer organizationId,Integer unitId,Integer storeId,String name) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<InstrumentMasterDto> instrumentList = statelessSession.getNamedQuery("SEARCH_INSTRUMENT_BY_NAME")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setParameter("storeId",storeId)
					.setParameter("name", "%" + name.toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(InstrumentMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, instrumentList, null);
		} catch (HibernateException he) {
			logger.error("searchInstrumentByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("searchInstrumentByName failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response getAllInstrumentList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<InstrumentMasterDto> instrumentList = statelessSession.createSQLQuery(" select code as\"instrumentCode\",name as \"instrumentName\",id as\"instrumentId\" "
					+ " from public.m_item where is_cssd=true or is_sterilize=true "
					+ " AND organization_id=:organizationId  AND unit_id=:unitId ")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(InstrumentMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, instrumentList, null);
		} catch (HibernateException he) {
			logger.error("getAllInstrumentList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAllInstrumentList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response searchInstrumentByMultipleFields(String code,String name,Integer instrumentNo,Integer organizationId,Integer unitId,Integer storeId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			
			StringBuilder condition=new StringBuilder();
			
			if(code!=null)
			{
				//condition.append(" AND lower(inst.instrumentCode) LIKE '"+"%"+code.toLowerCase()+"%"+"' ");
				condition.append(" AND lower(inst.instrumentCode) LIKE :code ");
			}
			if(name!=null)
			{
				//condition.append(" AND lower(inst.instrumentName) LIKE '"+"%"+name.toLowerCase()+"%"+"' ");
				condition.append(" AND lower(inst.instrumentName) LIKE :name ");
			}
			if(instrumentNo!=null)
			{
				condition.append(" AND inst.instrumentId=:instrumentNo ");
			}
			
			
			String query = "SELECT inst.instrumentId as instrumentId,"
					+ " inst.instrumentName as instrumentName,"
					+ " inst.instrumentCode as instrumentCode " 
					+ " FROM Item inst "
					+ " where inst.organizationId=:organizationId and inst.unitId=:unitId and inst.storeId=:storeId "
					+ condition;

			Query createQuery= statelessSession.createQuery(query)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setParameter("storeId",storeId);


			if(code!=null)
			{
				createQuery.setParameter("code", "%" + code.toLowerCase() + "%");
			}
			if(name!=null)
			{
				createQuery.setParameter("name", "%" + name.toLowerCase() + "%");
			}
			if(instrumentNo!=null)
			{
				createQuery.setParameter("instrumentNo", instrumentNo);
			}


			List<InstrumentMasterDto> instrumentList =createQuery.setResultTransformer(Transformers.aliasToBean(InstrumentMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, instrumentList, null);
		} catch (HibernateException he) {
			logger.error("searchInstrumentByMultipleFields failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("searchInstrumentByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	
	
	
	
	
	@Override
	public Response getKitWithEquipments(Integer organizationId,Integer unitId,Integer storeId,Integer kitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			KitMasterDto MasterDto =(KitMasterDto) statelessSession.getNamedQuery("GET_KIT_BY_KIT_ID")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setParameter("storeId",storeId)
					.setParameter("kitId", kitId).setResultTransformer(Transformers.aliasToBean(KitMasterDto.class))
					.uniqueResult();
			if (MasterDto != null) {
				
					List<KitInstrumentMapperDto> kitEquipmentList = statelessSession
							.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID").setParameter("kitId", MasterDto.getKitId())
							.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
					MasterDto.setKitInstrumentList(kitEquipmentList);
			}

			statelessSession.close();

			return new Response(SUCCESS, null, null,null,MasterDto);
		} catch (HibernateException he) {
			logger.error("getKitWithEquipments failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitWithEquipments failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response updateKit(KitMasterDto masterDto) {
		try {

			Session session = sessionFactory.getCurrentSession();
			KitMaster kitMaster = (KitMaster) session.load(KitMaster.class, masterDto.getKitId());
			kitMaster.setCode(masterDto.getCode());
			kitMaster.setName(masterDto.getName());
			kitMaster.setSurgeon(masterDto.getSurgeon());
			kitMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ? masterDto.getUnitId() :0));
			kitMaster.setUpdated_by(masterDto.getUpdated_by());
			session.update(kitMaster);

			/*
			 * StatelessSession statelessSession =
			 * sessionFactory.openStatelessSession();
			 * statelessSession.getNamedQuery(
			 * "DELETE_KIT_EQUIPMENT_MAPPER_LIST") .setParameter("kitId",
			 * masterDto.getKitId()) .executeUpdate(); KitMaster kitMaster =
			 * mapper.map(masterDto, KitMaster.class,
			 * "KitMasterDto_to_KitMaster"); kitMaster.setUpdated_date(new
			 * Date()); kitMaster = update(kitMaster);
			 */
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error("updateKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response updateKitStatus(KitMasterDto masterDto) {
		try {

			Session session = sessionFactory.getCurrentSession();
			KitMaster kitMaster = (KitMaster) session.get(KitMaster.class, masterDto.getKitId());
			
			kitMaster.setIs_active(true);
			kitMaster.setIs_deleted(false);
			kitMaster.setStatus(masterDto.getStatus());
			kitMaster.setUpdated_date(TimeZoneComponent.getDateByzone(masterDto.getUnitId()!=null ? masterDto.getUnitId() :0));
			kitMaster.setUpdated_by(masterDto.getUpdated_by());
			session.update(kitMaster);
			if(masterDto.getStatus()=='A')
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			else
			{
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);
			}
			
		} catch (Exception e) {
			logger.error("updateKitStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response createKit(KitMasterDto kitMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			KitMaster kitMaster = mapper.map(kitMasterDto, KitMaster.class, "KitMasterDto_to_KitMaster");
			kitMaster.setCreated_date(TimeZoneComponent.getDateByzone(kitMasterDto.getUnitId() !=null ? kitMasterDto.getUnitId() :0));
			kitMaster.setIs_active(true);
			kitMaster.setIs_deleted(false);
			kitMaster.setUpdated_date(TimeZoneComponent.getDateByzone(kitMasterDto.getUnitId()!=null ? kitMasterDto.getUnitId() :0));
 		    kitMaster.setStatus('A');
			Integer kitId = (Integer)session.save(kitMaster);
			return new Response(SUCCESS, "201", "Created Successfully", null, kitId);
		} catch (HibernateException he) {
			logger.error("createKit failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("createKit failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getKitByNameAndCode(String name,String code,Integer orgId,Integer unitId,Integer storeId ) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			KitMasterDto MasterDto =(KitMasterDto) session.getNamedQuery("SEARCH_KIT_LIST_BY_KIT_NAME_AND_CODE")
					.setParameter("organizationId", orgId)
					.setParameter("unitId",unitId)
					//.setParameter("storeId",storeId)
					.setParameter("name", name)
					.setParameter("code",code).setResultTransformer(Transformers.aliasToBean(KitMasterDto.class))
					.uniqueResult();
			if (MasterDto != null) {
				
					List<KitInstrumentMapperDto> kitEquipmentList = session
							.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID").setParameter("kitId", MasterDto.getKitId())
							.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
					MasterDto.setKitInstrumentList(kitEquipmentList);
			}
			
			return new Response(SUCCESS, null, null, null,MasterDto );

		} catch (HibernateException he) {
			logger.error("getKitByNameAndCode failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitByNameAndCode failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}


	@Override
	public Response getKitListcount(KitMasterDto kitMasterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(KitMaster.class); 
			c.add(Restrictions.eq("organizationId", kitMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			logger.error("getKitListcount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getEquipmentList(KitMasterDto kitMasterDto) {
		
		try {
			List<KitMasterDto> listTrustMasterDto = (List<KitMasterDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID")
					.setParameter("kitId", kitMasterDto.getKitId())
				 .setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
			return new Response(SUCCESS, null, null, listTrustMasterDto, null);
	} catch (HibernateException he) {
		logger.error("getEquipmentList failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getEquipmentList failed", e);
		throw new ServiceException(e.getMessage());
	}
}
}