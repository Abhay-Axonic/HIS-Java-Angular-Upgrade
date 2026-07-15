package com.param.lis.unit.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.MachineParameterMpper;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineParameterMpperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PathologyMachineParameterDaoimpl extends GenericDao<MachineParameterMpper, Integer>
           implements IPathologyMachineParameterDao, ICommonConstants, IError {

	public PathologyMachineParameterDaoimpl() {
		super(MachineParameterMpper.class);
	}

	@SuppressWarnings("unused")
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(MachineParameterMpper.class);

	@Override
	public Response saveMachineParameterMapping(MachineParameterMpperDto machineParameterMpperDto)
			throws ApplicationException {
		try {
		for(MachineParameterMpperDto machineparaMapping: machineParameterMpperDto.getMachinePathalogyParameterMpperDtoList()) {
			MachineParameterMpper machineParameterMpper= new MachineParameterMpper();
			machineParameterMpper.setMachinParameterId(machineparaMapping.getMachinParameterId());
			machineParameterMpper.setParameterId(machineparaMapping.getParameterId());
			if(machineparaMapping.getMachineParameterMpprId()!=null) {
				machineParameterMpper.setMachineParameterMpprId(machineparaMapping.getMachineParameterMpprId());
				}
			machineParameterMpper.setMachineId(machineparaMapping.getMachineId());
			machineParameterMpper.setStatus(machineparaMapping.getStatus());
			machineParameterMpper.setCreatedBy(machineparaMapping.getCreatedBy());
			machineParameterMpper.setCreatedDate(TimeZoneComponent.getDateByzone(machineparaMapping.getUnitId()));
			machineParameterMpper.setOrganisationId(machineparaMapping.getOrganisationId());
			machineParameterMpper.setUnitId(machineparaMapping.getUnitId());
			machineParameterMpper.setUpdatedBy(machineparaMapping.getUpdatedBy());
			machineParameterMpper.setUpdatedDate(TimeZoneComponent.getDateByzone(machineparaMapping.getUnitId()));	
			sessionFactory.getCurrentSession().saveOrUpdate(machineParameterMpper);
			}
		return new Response(SUCCESS, null, "Machine Mapped Successfully", null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getPathalogyMachineParameterList(MachineParameterMpperDto machineParameterMpperDto)
			throws ApplicationException {
		try {
			List<MachineParameterMpperDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery("select DISTINCT mpm.machine_id as \"machineId\",mm.machine_desc as \"machineName\"  " 
							+ "from lab.t_machine_parameter_mppr mpm " 
							+ "join lab.m_machine_master mm on  mpm.machine_id= mm.machine_id " 
							+ "where mpm.organisation_id=:organizationId and mpm.unit_id=:unitId ")
					.setParameter("organizationId", machineParameterMpperDto.getOrganisationId())
					.setParameter("unitId", machineParameterMpperDto.getUnitId())
					//.setFirstResult(machineParameterMpperDto.getOffset() != null ? machineParameterMpperDto.getOffset()  : 0)
					//.setMaxResults(machineParameterMpperDto.getNoOfRecordsPerPage() != null ? machineParameterMpperDto.getNoOfRecordsPerPage()  : 50)
					.setResultTransformer(Transformers.aliasToBean(MachineParameterMpperDto.class)).list();

		return new Response(SUCCESS, null, null, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getPathalogyMachineParameterListByMachineId(MachineParameterMpperDto machineParameterMpperDto) {
		try {
			List<MachineParameterMpperDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery("select mpm.machine_parameter_mppr_id as \"machineParameterMpprId\",mpm.machine_id as \"machineId\",mm.machine_desc as \"machineName\", mpm.machin_parameter_id as \"machinParameterId\"," 
							+" mp.machine_parameter_desc as \"machineParameterDesc\", pm.parameter_id as \"parameterId\",pm.parameter_name as \"parameterName\","
							+ " mpm.created_by as \"createdBy\", mpm.updated_by as \"updatedBy\",mpm.organisation_id as \"organisationId\", mpm.unit_id as \"unitId\",mpm.status as \"status\" "  
							+" from lab.t_machine_parameter_mppr mpm " 
							+" join lab.m_machine_parameter mp on mpm.machin_parameter_id =mp.machin_parameter_id " 
							+" join lab.m_parameter_master pm on mpm.parameter_id= pm.parameter_id " 
							+" join lab.m_machine_master mm on  mpm.machine_id= mm.machine_id "
							+" where mpm.organisation_id=:organizationId and mpm.unit_id=:unitId "
							+ " and mpm.machine_id=:machineId and mpm.status='A' ")
					.setParameter("organizationId", machineParameterMpperDto.getOrganisationId())
					.setParameter("unitId", machineParameterMpperDto.getUnitId())
					.setParameter("machineId", machineParameterMpperDto.getMachineId())
					.setResultTransformer(Transformers.aliasToBean(MachineParameterMpperDto.class)).list();
          if(list.size()>0) {
        	  return new Response(SUCCESS, null, null, list, null);
          }
          else {
        	  return new Response(ERROR, null, null, list, null);
          }
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updateMachineParaMapping(MachineParameterMpperDto machineParameterMpperDtoList) {
		try {
			for(MachineParameterMpperDto machineparaMapping: machineParameterMpperDtoList.getInactivePathalogyParameterMpperDtoList()) {
				MachineParameterMpper machineParameterMpper= new MachineParameterMpper();
				machineParameterMpper.setMachinParameterId(machineparaMapping.getMachinParameterId());
				machineParameterMpper.setParameterId(machineparaMapping.getParameterId());
				if(machineparaMapping.getMachineParameterMpprId()!=null) {
				machineParameterMpper.setMachineParameterMpprId(machineparaMapping.getMachineParameterMpprId());
				}
				machineParameterMpper.setMachineId(machineparaMapping.getMachineId());
				machineParameterMpper.setStatus(machineparaMapping.getStatus());
				machineParameterMpper.setCreatedBy(machineparaMapping.getCreatedBy());
				machineParameterMpper.setCreatedDate(TimeZoneComponent.getDateByzone(machineparaMapping.getUnitId()));
				machineParameterMpper.setOrganisationId(machineparaMapping.getOrganisationId());
				machineParameterMpper.setUnitId(machineparaMapping.getUnitId());
				machineParameterMpper.setUpdatedBy(machineparaMapping.getUpdatedBy());
				machineParameterMpper.setUpdatedDate(TimeZoneComponent.getDateByzone(machineparaMapping.getUnitId()));	
				sessionFactory.getCurrentSession().saveOrUpdate(machineParameterMpper);
				}
			return new Response(SUCCESS, null, null, null, null);
			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				throw e;
			}
	}

	@Override
	public Response updatePathalogyMachineParameterStatus(MachineParameterMpperDto machineParameterMpperDto) {
		try {
			int count=sessionFactory.getCurrentSession().createQuery(" update  MachineParameterMpper path set path.status =:status where path.machineId=:macId and path.organisationId=:orgId "
					+ " and path.unitId=:unitId ")
					.setParameter("macId", machineParameterMpperDto.getMachineId())
					.setParameter("orgId", machineParameterMpperDto.getOrganisationId())
					.setParameter("unitId", machineParameterMpperDto.getUnitId())
					.setParameter("status", machineParameterMpperDto.getStatus())
					.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				throw e;
			}
	}

	
	
}
