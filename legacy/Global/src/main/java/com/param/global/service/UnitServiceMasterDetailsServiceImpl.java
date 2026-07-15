package com.param.global.service;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IServiceBasePriceMasterDao;
import com.param.global.billing.dto.ServiceBasePriceMasterDto;
import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IUnitServiceMasterDetailsDao;
import com.param.global.dto.UnitServiceMasterDetailsDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitServiceMasterDetailsServiceImpl implements IUnitServiceMasterDetailsService, ICommonConstants {
	final static Logger logger = Logger.getLogger(UnitServiceMasterDetailsServiceImpl.class);


	@Autowired
	private IUnitServiceMasterDetailsDao iUnitServiceMasterDetailsDao;
	
	@Autowired
	IServiceBasePriceMasterDao iServiceBasePriceMasterDao;

	@Override
	@Transactional
	public Response saveUnitServiceMasterDetails(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.saveUnitServiceMasterDetails(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getPaginatedUnitServiceMasterDetailsList(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getPaginatedUnitServiceMasterDetailsList(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getUnitServiceMasterDetailsCount(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getUnitServiceMasterDetailsCount(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getUnitServiceMasterDetailsById(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getUnitServiceMasterDetailsById(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response updateUnitServiceMasterDetails(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			Response res =  iUnitServiceMasterDetailsDao.updateUnitServiceMasterDetails(unitServiceMasterDetailsDto);
			if(res.getStatus().equals(SUCCESS))
			{
				ServiceBasePriceMasterDto serviceBasePriceMasterDto = new ServiceBasePriceMasterDto();
					serviceBasePriceMasterDto.setServiceBasePriceId(unitServiceMasterDetailsDto.getServiceBasePriceId());
					serviceBasePriceMasterDto.setUpdatedBy(unitServiceMasterDetailsDto.getUpdatedBy());
					serviceBasePriceMasterDto.setCostOfService(unitServiceMasterDetailsDto.getCostOfService());
					serviceBasePriceMasterDto.setBasePrice(unitServiceMasterDetailsDto.getBasePrice());
				iServiceBasePriceMasterDao.updateServiceBasePriceMaster(serviceBasePriceMasterDto);
				
				if(unitServiceMasterDetailsDto.gettContractServiceDetailsDtoList()!=null)
				{
					List<TContractServiceDetailsDto> contractServiceDetailsDto = new LinkedList<TContractServiceDetailsDto>();
					for(TContractServiceDetailsDto dto : unitServiceMasterDetailsDto.gettContractServiceDetailsDtoList())
					{
						dto.setOrgId(unitServiceMasterDetailsDto.getOrganizationId());
						dto.setUnitId(unitServiceMasterDetailsDto.getUnitId());
						contractServiceDetailsDto.add(dto);
					}
					iUnitServiceMasterDetailsDao.saveContractServiceDetails(contractServiceDetailsDto);
				}
				
				iUnitServiceMasterDetailsDao.updateTestByServiceMaster(unitServiceMasterDetailsDto.getUnitId(),unitServiceMasterDetailsDto.getServiceMasterId(),unitServiceMasterDetailsDto.getIsOutsource());
			}
			return res;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getAllActiveTax(Integer organizationId) throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getAllActiveTax(organizationId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response searchPeriodicityServices(int visitTypeId, int unitId, String serviceName)throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.searchPeriodicityServices(visitTypeId, unitId, serviceName);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchProcedureServices(int orgId, int unitId, String serviceName,int specialityId) throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.searchProcedureServices(orgId, unitId, serviceName,specialityId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcedureTypeList() throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getProcedureTypeList();
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcedureListByProcedureType(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iUnitServiceMasterDetailsDao.getProcedureListByProcedureType(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getPaginatedUnitServiceMasterDetailsListSearch(
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getPaginatedUnitServiceMasterDetailsListSearch(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}
	
	@Override
	@Transactional
	public Response getUnitServiceMasterDetailsListForCyclic(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getUnitServiceMasterDetailsListForCyclic(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getUnitServiceMasterQtyDetailsByServiceId(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getUnitServiceMasterQtyDetailsByServiceId(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getUnitServiceMasterDetailsByServiceId(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.getUnitServiceMasterDetailsByServiceId(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response UnitServiceMasterDetailsForSchedulingWorking(
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.UnitServiceMasterDetailsForSchedulingWorking(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response unitServiceMasterDetailsForSchedulingWorkingDetails(
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			return iUnitServiceMasterDetailsDao.unitServiceMasterDetailsForSchedulingWorkingDetails(unitServiceMasterDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

    @Override
    @Transactional
    public Response getAdminServicesForConfiguration(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
            throws ApplicationException {
        try {
            return iUnitServiceMasterDetailsDao.getAdminServicesForConfiguration(unitServiceMasterDetailsDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    
    }
}
