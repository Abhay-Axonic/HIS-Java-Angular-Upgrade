package com.param.lis.unit.service;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dao.IMachineParameterDao;
import com.param.lis.unit.dto.MachineParameterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MachineParameterServiceImpl implements IMachineParameterService, ICommonConstants, IError {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IMachineParameterDao iMachineParameterDao;


	
	@Override
	@Transactional
	public Response saveMachineParameterMaster(MachineParameterDto machineParameterDto) throws ApplicationException {
		try{
			
			iMachineParameterDao.saveMachineParameterMaster(machineParameterDto);
		
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMachineParameterMasterById(Integer organisationId, Integer unitId, Integer machinParameterId)
			throws ApplicationException {
		try {
			return iMachineParameterDao.getMachineParameterMasterById(organisationId, unitId, machinParameterId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateMachineParaMaster(MachineParameterDto machineParameterDto) throws ApplicationException {
		try {
			MachineParameterDto machineParaMtrDto = (MachineParameterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_MACHINE_PARAMETER_BY_CODE")
					.setParameter("code", machineParameterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", machineParameterDto.getOrganisationId())
					.setParameter("unitId", machineParameterDto.getUnitId())
					.setParameter("machinParameterId", machineParameterDto.getMachinParameterId())
					.setResultTransformer(Transformers.aliasToBean(MachineParameterDto.class)).uniqueResult();
			if (machineParaMtrDto == null) {
				//System.out.println(TimeZoneComponent.getDateByzone(unitId).getTime());
				machineParameterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(machineParameterDto.getUnitId()).getTime());
				return iMachineParameterDao.updateMachineParaMaster(machineParameterDto);
			} else {
				return new Response(ERROR, ERR_500, MACHINE_TEST_CODE_EXISIS, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}

	}

	@Override
	@Transactional
	public Response activateInactivateMachineParaMaster(Integer organisationId, Integer machinParameterId,
			Character machineStatus) throws ApplicationException {
		try {
			return iMachineParameterDao.activateInactivateMachineParaMaster(organisationId, machinParameterId, machineStatus);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listMachineParaMaster(Integer organisationId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			return iMachineParameterDao.listMachineParaMaster(organisationId, unitId, offset, recordPerPage);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalMachineParaMasterRecord(Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			return iMachineParameterDao.getTotalMachineParaMasterRecord(organisationId, unitId);
		} catch (Exception e) {
			
			throw e;
		}
		
	}

	@Override
	@Transactional
	public Response getActiveMachineParaList(MachineParameterDto machineParameterDto) throws ApplicationException {
		try {
			return iMachineParameterDao.getActiveMachineParaList(machineParameterDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getActiveMachineList(MachineParameterDto machineParameterDto) throws ApplicationException {
		try {
			return iMachineParameterDao.getActiveMachineList(machineParameterDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getActiveParaMachineList(MachineParameterDto machineParameterDto) throws ApplicationException {
		try {
			return iMachineParameterDao.getActiveParaMachineList(machineParameterDto);
		} catch (Exception e) {
			
			throw e;
		}
	}
}
