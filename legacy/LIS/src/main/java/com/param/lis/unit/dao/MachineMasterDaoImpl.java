package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.entity.lis.unit.MachineMaster;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineMasterDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MachineMasterDaoImpl extends GenericDao<MachineMaster, Integer>
            implements IMachineMasterDao, ICommonConstants, IError {

	public MachineMasterDaoImpl() {
		super(MachineMaster.class);
	}
	
	@Autowired
	private DozerBeanMapper mapper;

	final static Logger logger = Logger.getLogger(MachineMaster.class);

	@Override
	public Response saveMachineMaster(MachineMasterDto machineMasterDto) throws ApplicationException {
		
		try {
			MachineMaster machineMaster = mapper.map(machineMasterDto, MachineMaster.class,
					"MachineMasterDtoTOmachineMasterDto");
			
			machineMaster.setStatus('A');
			
			MachineMaster machineMst = save(machineMaster);
			if (machineMst != null) {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_ADD_SUCC, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_ADD_FAIL, null, null);
			}
		} catch (HibernateException er) {
			logger.error("Exection", er);throw er;
		} catch (Exception e) {
			logger.error("Exection", e);throw e;
		}
	}

	@Override
	public Response getMachineMasterById(Integer organisationId, Integer unitId, Integer machineId)
			throws ApplicationException {
		try {
			MachineMasterDto machineMasterDto = (MachineMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MACHINE_BY_MACHINE_ID")
					.setParameter("orgId", organisationId)
					.setParameter("machineId", machineId).setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MachineMasterDto.class)).uniqueResult();

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, machineMasterDto);

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}

	}

	@Override
	public Response updateMachineMaster(MachineMasterDto machineMasterDto) throws ApplicationException {
		try {
			MachineMaster machineMaster = mapper.map(machineMasterDto, MachineMaster.class,
					"MachineMasterDtoTOmachineMasterDto");
			
			machineMaster.setStatus('A');
			
			MachineMaster machineMat = update(machineMaster);
			if (machineMat != null) {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_UPDATE_SUCC, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_UPDATE_FAIL, null, null);
			}
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response activateInactivateMachineMaster(Integer organisationId, Integer machineId, Character machineStatus)
			throws ApplicationException {
	try {
		MachineMaster machineMaster = findById(machineId);
		if (machineMaster.getMachineId() != 0) {
			machineMaster.setStatus(machineStatus);
			MachineMaster machineMst = update(machineMaster);
			if (machineMst.getStatus() == ACTIVE) {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_ACTIVATE_SUCC, null, null);
			} else if (machineMst.getStatus() == INACTIVE) {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_INACTIVATE_SUCC, null, null);
			} else {
				if (machineStatus == ACTIVE) {
					return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_ACTIVATE_FAIL, null, null);
				} else {
					return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_INACTIVATE_FAIL, null, null);
				}
			}
		} else {
			return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_NOT_FOUND, null, null);
		}

	} catch (Exception e) 	{
		logger.error("Exection", e);
		throw e;
	}
}

	@Override
	public Response listMachineMaster(Integer organisationId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
	try {
		List<MachineMasterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_PAGINATED_MACHINE_MASTER_LIST")
				.setParameter("orgId", organisationId)
				.setParameter("unitId", unitId).setFirstResult(offset != null ? offset : 0)
				.setMaxResults(recordPerPage != null ? recordPerPage : 10)
				.setResultTransformer(Transformers.aliasToBean(MachineMasterDto.class)).list();

		if (listTechniqueMasterDto.isEmpty()) {
			return new Response(ERROR, ERR_500, MACHINE_TEST_RECORDS_NOT_FOUND, null, null);
		} else {
			return new Response(SUCCESS, SUCCESS_200, null, listTechniqueMasterDto, null);
		}

	} catch (Exception e) {
		logger.error("Exection", e);
		throw e;
	}
}

	@Override
	public Response getTotalMachineMasterRecord(Integer organisationId, Integer unitId) throws ApplicationException {
		try {
			BigInteger MachineMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_MACHINE_RECORD")
					.setParameter("unitId", unitId)
					.setParameter("orgId", organisationId)
					.uniqueResult();

			if (MachineMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, MachineMasterTotalRecordCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getActiveMachineList(MachineMasterDto machineMasterDto) throws ApplicationException {
		try {
			List<MachineMasterDto> machineMtrDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_MACHINE_LIST")
					.setParameter("orgId", machineMasterDto.getOrganisationId())
					.setParameter("unitId", machineMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MachineMasterDto.class)).list();
			return new Response(SUCCESS, null, null, machineMtrDto, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		} finally {
			 ;
		}
	}
}
