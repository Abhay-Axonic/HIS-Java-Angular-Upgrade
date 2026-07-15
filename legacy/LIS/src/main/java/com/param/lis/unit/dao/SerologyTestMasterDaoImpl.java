package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.SerologyTestFindingMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.unit.dto.SerologyTestFindingMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SerologyTestMasterDaoImpl extends GenericDao<SerologyTestFindingMaster, Integer>
		implements ISerologyTestFindingMasterDao, ICommonConstants, IError {

	public SerologyTestMasterDaoImpl() {
		super(SerologyTestFindingMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	final static Logger logger = Logger.getLogger(SerologyTestFindingMaster.class);

	@Override
	public Response saveSerologyTestMaster(SerologyTestFindingMasterDto serologyTestFindingMasterDto) throws ApplicationException {
		try {
			SerologyTestFindingMaster serologyTestFindingMaster = mapper.map(serologyTestFindingMasterDto, SerologyTestFindingMaster.class,
					"SerologyTestFindingMasterDtoTOSerologyTestFindingMaster");
			
			serologyTestFindingMaster.setStatus('A');
			
			SerologyTestFindingMaster serologyrMst = save(serologyTestFindingMaster);
			if (serologyrMst != null) {
				return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_ADD_SUCC, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_ADD_FAIL, null, null);
			}
		} catch (HibernateException er) {
			logger.error("Exection", er);throw er;
		} catch (Exception e) {
			logger.error("Exection", e);throw e;
		}
	}

	@Override
	public Response getSerologyTestMasterById(Integer orgId, Integer unitId, Integer serologyId)
			throws ApplicationException {
		try {
			
			GlobalCommonDto SerologyTestMasterDto = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SEROLOGY_BY_SEROLOGY_ID").setParameter("orgId", orgId)
					.setParameter("serologyId", serologyId).setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).uniqueResult();

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, SerologyTestMasterDto);

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}

	}

	@Override
	public Response updateSerologyMaster(SerologyTestFindingMasterDto serologyTestFindingMasterDto) throws ApplicationException {
		try {
			SerologyTestFindingMaster serologyTestFindingMaster = mapper.map(serologyTestFindingMasterDto, SerologyTestFindingMaster.class,
					"SerologyTestFindingMasterDtoTOSerologyTestFindingMaster");
			
			serologyTestFindingMaster.setStatus('A');
			SerologyTestFindingMaster serologyMat = update(serologyTestFindingMaster);
			if (serologyMat != null) {
				return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_UPDATE_SUCC, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_UPDATE_FAIL, null, null);
			}
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response ActivateInactivateSerologyMaster(Integer orgId, Integer serologyId, Character serologyStatus)
			throws ApplicationException {

		try {
			SerologyTestFindingMaster serologyMaster = findById(serologyId);
			if (serologyMaster.getSerologyId() != 0) {
				serologyMaster.setStatus(serologyStatus);
				SerologyTestFindingMaster departmentMst = update(serologyMaster);
				if (departmentMst.getStatus() == ACTIVE) {
					return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_ACTIVATE_SUCC, null, null);
				} else if (departmentMst.getStatus() == INACTIVE) {
					return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_INACTIVATE_SUCC, null, null);
				} else {
					if (serologyStatus == ACTIVE) {
						return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_ACTIVATE_FAIL, null, null);
					} else {
						return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_INACTIVATE_FAIL, null, null);
					}
				}
			} else {
				return new Response(SUCCESS, SUCCESS_200, SEROLOGY_TEST_NOT_FOUND, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response listSerologyMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_SEROLOGY_MASTER_LIST").setParameter("orgId", orgId)
					.setParameter("unitId", unitId).setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();

			if (listTechniqueMasterDto.isEmpty()) {
				return new Response(ERROR, ERR_500, SEROLOGY_TEST_RECORDS_NOT_FOUND, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, null, listTechniqueMasterDto, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getToTalSerologyMasterRecord(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			BigInteger HeaderMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_SEROLOGY_RECORD").setParameter("unitId", unitId).setParameter("orgId", orgId)
					.uniqueResult();

			if (HeaderMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, HeaderMasterTotalRecordCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response checkForUniqueSerologyTestMasterDesc(SerologyTestFindingMasterDto serologyTestMasterDto)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> list =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SEROLOGY_BY_DESCRIPTION")
					.setParameter("serologyTest", serologyTestMasterDto.getSerologyTest().trim().toLowerCase())
					.setParameter("orgId", serologyTestMasterDto.getOrgId())
					.setParameter("unitId", serologyTestMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, list, null);
			
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@Override
	public Response updateUniqueSerologyTestMasterDesc(SerologyTestFindingMasterDto serologyTestMasterDto)
			throws ApplicationException {
		try {
			
			List<GlobalCommonDto> list =  sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_SEROLOGY_DESCRIPTION")
					.setParameter("serologyTest", serologyTestMasterDto.getSerologyTest().trim().toLowerCase())
					.setParameter("orgId", serologyTestMasterDto.getOrgId())
					.setParameter("unitId", serologyTestMasterDto.getUnitId())
					.setParameter("serologyId", serologyTestMasterDto.getSerologyId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, list, null);
			
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}
}
