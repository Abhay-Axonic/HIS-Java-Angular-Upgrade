package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.MachineParameter;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineParameterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MachineParameterDaoImpl extends GenericDao<MachineParameter, Integer>
		implements IMachineParameterDao, ICommonConstants, IError {

	public MachineParameterDaoImpl() {
		super(MachineParameter.class);
	}
	final static Logger logger = Logger.getLogger(MachineParameter.class);
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveMachineParameterMaster(MachineParameterDto machinePatraMstrDto) throws ApplicationException {
		try{
			
			MachineParameter machinepara= new MachineParameter();
				machinepara.setMachineParameterDesc(machinePatraMstrDto.getMachineParameterDesc());
		        machinepara.setMachineId(machinePatraMstrDto.getMachineId());
		        machinepara.setCode(machinePatraMstrDto.getCode());
		        machinepara.setStatus(machinePatraMstrDto.getStatus());
		        machinepara.setCreatedDate(TimeZoneComponent.getDateByzone(machinePatraMstrDto.getUnitId()).getTime());
		        machinepara.setCreatedBy(machinePatraMstrDto.getCreatedBy());
		        machinepara.setOrganisationId(machinePatraMstrDto.getOrganisationId());
		        machinepara.setUnitId(machinePatraMstrDto.getUnitId());
		        sessionFactory.getCurrentSession().save(machinepara);
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			throw e;
		}
	}


	@Override
	public Response getMachineParameterMasterById(Integer organisationId, Integer unitId, Integer machinParameterId)
			throws ApplicationException {
		try {
			MachineParameterDto machineParaMasterDto = (MachineParameterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MACHINE_PARAMETER_BY_MACHINE_ID")
					.setParameter("orgId", organisationId)
					.setParameter("machinParameterId", machinParameterId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MachineParameterDto.class)).uniqueResult();

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, machineParaMasterDto);

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, HEADER_NOT_FOUND, null, null);
		}

	}


	@Override
	public Response updateMachineParaMaster(MachineParameterDto machineParameterDto) throws ApplicationException {
		try {
			MachineParameter machineparameterMaster = mapper.map(machineParameterDto, MachineParameter.class,
					"MachinePatraMstrDtoTOMachinePatraMstrDto");
			
			machineparameterMaster.setStatus('A');
			
			MachineParameter machineparaMst = update(machineparameterMaster);
			if (machineparaMst != null) {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_UPDATE_SUCC, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_UPDATE_FAIL, null, null);
			}
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, MACHINE_TEST_ADD_FAIL, null, null);
		}
	}


	@Override
	public Response activateInactivateMachineParaMaster(Integer organisationId, Integer machinParameterId,
			Character machineStatus) throws ApplicationException {
		try {
			MachineParameter machineParaMaster = findById(machinParameterId);
			if (machineParaMaster.getMachinParameterId() != 0) {
				machineParaMaster.setStatus(machineStatus);
				MachineParameter machineParaMst = update(machineParaMaster);
				if (machineParaMst.getStatus() == ACTIVE) {
					return new Response(SUCCESS, SUCCESS_200, MACHINE_TEST_ACTIVATE_SUCC, null, null);
				} else if (machineParaMst.getStatus() == INACTIVE) {
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
			return new Response(ERROR, ERR_500, MACHINE_TEST_NOT_FOUND, null, null);
		}
	}


	@Override
	public Response listMachineParaMaster(Integer organisationId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			List<MachineParameterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_MACHINE_PARAMETER_MASTER_LIST")
					.setParameter("orgId", organisationId)
					.setParameter("unitId", unitId).setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(MachineParameterDto.class)).list();

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
	public Response getTotalMachineParaMasterRecord(Integer organisationId, Integer unitId)
	throws ApplicationException{
	try {
		BigInteger MachineParaMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_TOTAL_MACHINE_PARAMETER_RECORD")
				.setParameter("unitId", unitId)
				.setParameter("orgId", organisationId)
				.uniqueResult();

		if (MachineParaMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, MachineParaMasterTotalRecordCount);
		} else {
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
		}

	} catch (Exception e) {
		logger.error("Exection", e);
		throw e;
	}
}


	@Override
	public Response getActiveMachineParaList(MachineParameterDto machineParameterDto)
			throws ApplicationException {
	
		try {
			List<MachineParameterDto> machineMtrDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_MACHINE_PARAMETER_LIST")
					.setParameter("orgId", machineParameterDto.getOrganisationId())
					.setParameter("unitId", machineParameterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MachineParameterDto.class)).list();
			return new Response(SUCCESS, null, null, machineMtrDto, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		} finally {
			 ;
		}
	}


	@Override
	public Response getActiveMachineList(MachineParameterDto machineParameterDto) throws ApplicationException {
		try {
			List<MachineParameterDto> machineParaMtrDto = sessionFactory.getCurrentSession()
					.getNamedQuery("ACTIVE_MACHINE_PARAMETER_LIST")
					.setParameter("orgId", machineParameterDto.getOrganisationId())
					.setParameter("unitId", machineParameterDto.getUnitId())
					.setParameter("machineId", machineParameterDto.getMachineId())
					.setResultTransformer(Transformers.aliasToBean(MachineParameterDto.class)).list();
			return new Response(SUCCESS, null, null, machineParaMtrDto, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		} finally {
			 ;
		}
	}


	@Override
	public Response getActiveParaMachineList(MachineParameterDto machineParameterDto) throws ApplicationException {
		try {
			List<MachineParameterDto> machineMtrDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MACHINE_PARAMETER_LIST")
					.setParameter("orgId", machineParameterDto.getOrganisationId())
					.setParameter("unitId", machineParameterDto.getUnitId())
					.setParameter("machineId", machineParameterDto.getMachineId())
					.setResultTransformer(Transformers.aliasToBean(MachineParameterDto.class)).list();
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
