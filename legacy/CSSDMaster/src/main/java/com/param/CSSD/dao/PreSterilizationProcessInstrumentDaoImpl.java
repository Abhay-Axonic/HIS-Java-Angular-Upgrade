package com.param.CSSD.dao;

import java.util.List;



import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSD.model.PreSterilizationProcessInstrument;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PreSterilizationProcessInstrumentDaoImpl extends GenericDao<PreSterilizationProcessInstrument, Integer>
		implements IPreSterilizationProcessInstrumentDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(PreSterilizationProcessInstrumentDaoImpl.class);


	public PreSterilizationProcessInstrumentDaoImpl() {
		super(PreSterilizationProcessInstrument.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response getPreSterilizationProcessEquipmentList(Integer kitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList = statelessSession
					.getNamedQuery("GET_PRE_STERILIZATION_PROCESS_EQUIPMENT_LIST")
					.setParameter("kitId", kitId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessEquipmentDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPreSterilizationProcessEquipmentList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessEquipmentList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			;
		}
	}

	@Override
	public Response updatePreSterilizationProcessEquipment(PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto) {
		try {
			PreSterilizationProcessInstrument preSterilizationProcessEquipment = mapper.map(preSterilizationProcessEquipmentDto, PreSterilizationProcessInstrument.class,
					"PreSterilizationProcessEquipmentDto_to_PreSterilizationProcessEquipment");
			preSterilizationProcessEquipment.setCreated_date(TimeZoneComponent.getDateByzone(0));
			preSterilizationProcessEquipment.setUpdated_date(TimeZoneComponent.getDateByzone(0));
			preSterilizationProcessEquipment = update(preSterilizationProcessEquipment);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, preSterilizationProcessEquipment);
		} catch (Exception e) {
			logger.error("updatePreSterilizationProcessEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response createPreSterilizationProcessEquipment(PreSterilizationProcessInstrument preSterilizationProcessEquipment) {
		try {
			
			preSterilizationProcessEquipment = save(preSterilizationProcessEquipment);
			return new Response(SUCCESS, "201","Created", null, null);
		} catch (Exception e) {
			logger.error("createPreSterilizationProcessEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@Override
	public Response cleanEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			for (PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto2 : preSterilizationProcessEquipmentDtoList) {
			
				PreSterilizationProcessInstrument preSterilizationProcessEquipment=
						(PreSterilizationProcessInstrument)session.get(PreSterilizationProcessInstrument.class, preSterilizationProcessEquipmentDto2.getId());
					
					preSterilizationProcessEquipment.setStatus("cleaned");
					update(preSterilizationProcessEquipment);
					
					
					
					List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList2 = session
							.getNamedQuery("CHECK_KIT_STATUS_CLEANED")
							.setParameter("kitId", preSterilizationProcessEquipment.getKitId())
							.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
					
					if(preSterilizationProcessEquipmentDtoList2.isEmpty())
					{
						Query query=session.createQuery("UPDATE PreSterilizationProcess set status='cleaned' WHERE kitId=:kitId")
								.setParameter("kitId", preSterilizationProcessEquipmentDto2.getKitId());
						@SuppressWarnings("unused")
						int result=query.executeUpdate();
						
					}
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, "Equipment cleaned.", null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, "500", "Internal Server Error", null, null);
	}*/
	
	@Override
	public Response cleanEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto2 : preSterilizationProcessEquipmentDtoList) {
			
				PreSterilizationProcessInstrument preSterilizationProcessEquipment=
						(PreSterilizationProcessInstrument)session.get(PreSterilizationProcessInstrument.class, preSterilizationProcessEquipmentDto2.getId());
					
					preSterilizationProcessEquipment.setStatus("Cleaned");
					update(preSterilizationProcessEquipment);
					
					
					
					Long totalInstrumentCount = (Long)session
							.createQuery("SELECT count(*) FROM PreSterilizationProcessInstrument instr WHERE instr.transitionNumber=:transitionNumber")
							.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber())
							.uniqueResult();
					
					Long inspectedInstrumentCount = (Long)session
							.createQuery("SELECT count(*) FROM PreSterilizationProcessInstrument instr WHERE instr.transitionNumber=:transitionNumber AND instr.status='Cleaned'")
							.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber())
							.uniqueResult();
					
					if(totalInstrumentCount == inspectedInstrumentCount)
					{
						Query query1=session.createQuery("UPDATE PreSterilizationProcess set status='Completed',transition='Cleaned' WHERE transitionNumber=:transitionNumber")
								.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber());
						@SuppressWarnings("unused")
						int result1=query1.executeUpdate();
					}else if(inspectedInstrumentCount > 0)
					{
						Query query1=session.createQuery("UPDATE PreSterilizationProcess set status='Pending',transition='Cleaned' WHERE transitionNumber=:transitionNumber")
								.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber());
						@SuppressWarnings("unused")
						int result1=query1.executeUpdate();
						}
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, "Equipment inspected.", null, null);
		} catch (Exception e) {
			logger.error("cleanEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response inspectEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto2 : preSterilizationProcessEquipmentDtoList) {
			
				PreSterilizationProcessInstrument preSterilizationProcessEquipment=
						(PreSterilizationProcessInstrument)session.get(PreSterilizationProcessInstrument.class, preSterilizationProcessEquipmentDto2.getId());
					
					preSterilizationProcessEquipment.setStatus("Inspected");
					update(preSterilizationProcessEquipment);
					
					
					
					Long totalInstrumentCount = (Long)session
							.createQuery("SELECT count(*) FROM PreSterilizationProcessInstrument instr WHERE instr.transitionNumber=:transitionNumber")
							.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber())
							.uniqueResult();
					
					Long inspectedInstrumentCount = (Long)session
							.createQuery("SELECT count(*) FROM PreSterilizationProcessInstrument instr WHERE instr.transitionNumber=:transitionNumber AND instr.status='Inspected'")
							.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber())
							.uniqueResult();
					
					if(totalInstrumentCount == inspectedInstrumentCount)
					{
						Query query1=session.createQuery("UPDATE PreSterilizationProcess set status='Completed',transition='Inspected' WHERE transitionNumber=:transitionNumber")
								.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber());
						@SuppressWarnings("unused")
						int result1=query1.executeUpdate();
					}else if(inspectedInstrumentCount > 0)
					{
						Query query1=session.createQuery("UPDATE PreSterilizationProcess set status='Pending',transition='Inspected' WHERE transitionNumber=:transitionNumber")
								.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber());
						@SuppressWarnings("unused")
						int result1=query1.executeUpdate();
						}
				
			}
			
			
			
			return new Response(SUCCESS, SUCCESS_CODE, "Equipment inspected.", null, null);
		} catch (Exception e) {
			logger.error("inspectEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response disInfectEquipment(List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto2 : preSterilizationProcessEquipmentDtoList) {
			
				PreSterilizationProcessInstrument preSterilizationProcessEquipment=
						(PreSterilizationProcessInstrument)session.get(PreSterilizationProcessInstrument.class, preSterilizationProcessEquipmentDto2.getId());
					
					preSterilizationProcessEquipment.setStatus("Disinfected");
					update(preSterilizationProcessEquipment);
					
					
					
					Long totalInstrumentCount = (Long)session
							.createQuery("SELECT count(*) FROM PreSterilizationProcessInstrument instr WHERE instr.transitionNumber=:transitionNumber")
							.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber())
							.uniqueResult();
					
					Long disinfectedInstrumentCount = (Long)session
							.createQuery("SELECT count(*) FROM PreSterilizationProcessInstrument instr WHERE instr.transitionNumber=:transitionNumber AND instr.status='Disinfected'")
							.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber())
							.uniqueResult();
					
					if(totalInstrumentCount == disinfectedInstrumentCount)
					{
						Query query1=session.createQuery("UPDATE PreSterilizationProcess set status='Completed',transition='Disinfected'  WHERE transitionNumber=:transitionNumber")
								.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber());
						@SuppressWarnings("unused")
						int result1=query1.executeUpdate();
					}else if(disinfectedInstrumentCount > 0)
					{
						Query query1=session.createQuery("UPDATE PreSterilizationProcess set status='Pending',transition='Disinfected' WHERE transitionNumber=:transitionNumber")
								.setParameter("transitionNumber", preSterilizationProcessEquipment.getTransitionNumber());
						@SuppressWarnings("unused")
						int result1=query1.executeUpdate();
					}
				
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, "Equipment disinfected.", null, null);
		} catch (Exception e) {
			logger.error("disInfectEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	@Override
	public Response getEquipmentListPendingForInspection(Integer kitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList = statelessSession
					.getNamedQuery("GET_EQUIPMENT_LIST_PENDING_FOR_INSPECTION")
					.setParameter("kitId", kitId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessEquipmentDtoList, null);
		} catch (HibernateException he) {
			logger.error("getEquipmentListPendingForInspection failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForInspection failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			;
		}
	}
	
	@Override
	public Response getEquipmentListPendingForDisInfection(Integer kitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList = statelessSession
					.getNamedQuery("GET_EQUIPMENT_LIST_PENDING_FOR_DIS_INFECTION")
					.setParameter("kitId", kitId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessEquipmentDtoList, null);
		} catch (HibernateException he) {
			logger.error("getEquipmentListPendingForDisInfection failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForDisInfection failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			;
		}
	}
	
	
	@Override
	public Response getEquipmentListPendingForKitFormation(Integer kitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList = statelessSession
					.getNamedQuery("GET_EQUIPMENT_LIST_PENDING_FOR_KIT_FORMATION")
					.setParameter("kitId", kitId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessEquipmentDtoList, null);
		} catch (HibernateException he) {
			logger.error("getEquipmentListPendingForKitFormation failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForKitFormation failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			;
		}
	}


}
