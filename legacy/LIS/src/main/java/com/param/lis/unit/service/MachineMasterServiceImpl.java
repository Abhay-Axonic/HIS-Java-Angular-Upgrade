package com.param.lis.unit.service;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dao.IMachineMasterDao;
import com.param.lis.unit.dto.MachineMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class MachineMasterServiceImpl implements IMachineMasterService, ICommonConstants, IError {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IMachineMasterDao iMachineMasterDao ;
	

	
	@Override
	@Transactional
	public Response saveMachineMaster(MachineMasterDto machineMasterDto) throws ApplicationException {

		try {
			MachineMasterDto machineMstrDto = (MachineMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MACHINE_BY_CODE")
					.setParameter("code", machineMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", machineMasterDto.getOrganisationId())
					.setParameter("unitId", machineMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MachineMasterDto.class)).uniqueResult();
			if (machineMstrDto == null) {
				machineMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(machineMasterDto.getUnitId()).getTime());
				Response response = iMachineMasterDao.saveMachineMaster(machineMasterDto);
				return response;
			} else {
				return new Response(ERROR, ERR_500, MACHINE_TEST_CODE_EXISIS, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getMachineMasterById(Integer organisationId, Integer unitId, Integer machineId)
			throws ApplicationException {
		try {
			return iMachineMasterDao.getMachineMasterById(organisationId, unitId, machineId);
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response updateMachineMaster(MachineMasterDto machineMasterDto) throws ApplicationException {
		try {
			MachineMasterDto machineMtrDto = (MachineMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_MACHINE_BY_CODE")
					.setParameter("code", machineMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", machineMasterDto.getOrganisationId())
					.setParameter("unitId", machineMasterDto.getUnitId())
					.setParameter("machineId", machineMasterDto.getMachineId())
					.setResultTransformer(Transformers.aliasToBean(MachineMasterDto.class)).uniqueResult();
			if (machineMtrDto == null) {
				//System.out.println(TimeZoneComponent.getDateByzone(unitId).getTime());
				machineMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(machineMasterDto.getUnitId()).getTime());
				return iMachineMasterDao.updateMachineMaster(machineMasterDto);
			} else {
				return new Response(ERROR, ERR_500, MACHINE_TEST_CODE_EXISIS, null, null);
			}

		} catch (Exception e) {
			
			throw e;
		}

	}


	@Override
	@Transactional
	public Response activateInactivateMachineMaster(Integer organisationId, Integer machineId, Character machineStatus)
			throws ApplicationException {
		try {
			return iMachineMasterDao.activateInactivateMachineMaster(organisationId, machineId, machineStatus);
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response listMachineMaster(Integer organisationId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			return iMachineMasterDao.listMachineMaster(organisationId, unitId, offset, recordPerPage);
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getTotalMachineMasterRecord(Integer organisationId, Integer unitId) throws ApplicationException {
		try {
			return iMachineMasterDao.getTotalMachineMasterRecord(organisationId, unitId);
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getActiveMachineList(MachineMasterDto machineMasterDto) throws ApplicationException {
		try {
			return iMachineMasterDao.getActiveMachineList(machineMasterDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	}
