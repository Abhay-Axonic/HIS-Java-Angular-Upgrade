package com.param.lis.global.dao;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.entity.lis.global.AntibioticClassMaster;
import com.param.global.common.ServiceException;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.AntibioticClassMasterDto;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.HourMasterDto;

@Repository
@SuppressWarnings("rawtypes")
public class AntibioticsMasterDaoImpl extends  GenericDao<AntibioticClassMaster, Integer> implements IAntibioticsMasterDao, ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(AntibioticsMasterDaoImpl.class);

	public AntibioticsMasterDaoImpl() {
		super(AntibioticClassMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response addAntibioticsMaster(AntibioticClassMasterDto antibioticClassMasterDto)throws ApplicationException {
		try{
			AntibioticClassMaster antibioticClassMaster = new AntibioticClassMaster();
			antibioticClassMaster.setCreatedBy(antibioticClassMasterDto.getCreatedBy());
			antibioticClassMaster.setCreatedDate(antibioticClassMasterDto.getCreatedDate());
			antibioticClassMaster.setCode(antibioticClassMasterDto.getCode());
			antibioticClassMaster.setDesc(antibioticClassMasterDto.getDesc());
			antibioticClassMaster.setStatus(antibioticClassMasterDto.getStatus());
			antibioticClassMaster.setOrgId(antibioticClassMasterDto.getOrgId());
			antibioticClassMaster.setUpdatedBy(antibioticClassMasterDto.getUpdatedBy());
			antibioticClassMaster.setUpdatedDate(antibioticClassMasterDto.getUpdatedDate());
			antibioticClassMaster = save(antibioticClassMaster);
			return new Response<>(SUCCESS, SUCCESS_200,ANTIBIOTICS_ADD_SUCC, null,null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkAntibioticsMaster(AntibioticClassMasterDto antibioticClassMasterDto)throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listUnitMasteDtosessionFactory = sessionFactory.getCurrentSession().getNamedQuery("GET_ANTIBIOTICS_TYPE_BY_BACTERIA_CODE").setParameter("code", antibioticClassMasterDto.getCode().trim().toLowerCase()).setParameter("orgId", antibioticClassMasterDto.getOrgId()).setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasteDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getAntibioticsMasterById(Integer orgId, Integer  antibioticClassId)
			throws ApplicationException {
		
		try
		{
			GlobalCommonDto containerMaster = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTICS_TYPE_BY_BACTERIA_ID").setParameter("orgId", orgId)
					.setParameter("antibioticClassId", antibioticClassId)
                    .setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class))					
					.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, containerMaster);

		} catch (Exception e)
		{
			logger.error("Exception getting antibiotics master by id", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateAntibioticsMaster(AntibioticClassMasterDto antibioticClassMasterDto)
			throws ApplicationException {
		try
		{
			AntibioticClassMaster reportTypeMaster = mapper.map(antibioticClassMasterDto, AntibioticClassMaster.class,"AntibioticClassMasterDtoTOAntibioticClassMaster");
			update(reportTypeMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e)
		{
			logger.error("Exception updating antibiotics master", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response ActivateInactivateAntibioticsMaster(Integer orgId,
			Integer unitId, Character unitStatus)
			throws ApplicationException {

		try
		{
			AntibioticClassMaster antibioticClassMaster = findById(unitId);
			if (antibioticClassMaster.getAntibioticClassId() != 0)
			{
				antibioticClassMaster.setStatus(unitStatus);;
				AntibioticClassMaster antibioticMst = update(antibioticClassMaster);
				return new Response(SUCCESS, SUCCESS_200,ANTIBIOTICS_ACTIVATE_SUCC, null, antibioticMst);
				
			} else
			{
				return new Response(ERROR, ERR_500,ANTIBIOTICS_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			logger.error("Exception activating/inactivating antibiotics master", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listAntibioticsMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listUnitMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_ANTIBIOTICS_MASTER_LIST").setParameter("orgId", orgId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasterDto, null);
		
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getToTalAntibioticsMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			BigInteger getAntibioticsMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_ANTIBIOTICS_RECORD").setParameter("orgId", orgId).uniqueResult();
			
			
			if (getAntibioticsMasterCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getAntibioticsMasterCount);
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public Response updateCheckAntibioticsMasterCodeAvaiable(
			AntibioticClassMasterDto antibioticClassMasterDto)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_ANTIBIOTICS_TYPE_BY_BACTERIA_CODE")
					.setParameter("code",antibioticClassMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", antibioticClassMasterDto.getOrgId())
					.setParameter("antibioticClassId",	antibioticClassMasterDto.getAntibioticClassId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public Response getListAntibioticsMaster(Integer orgId) throws ApplicationException {
		try
		{
			List<AntibioticClassMasterDto> listAntibioticClassMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTICS_CLASS_LIST_MASTER")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(AntibioticClassMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listAntibioticClassMasterDto, null);
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkForUniqueAntibioticsMasterDesc(AntibioticClassMasterDto antibioticClassMasterDto)
			throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listUnitMasteDtosessionFactory = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTICS_MASTER_BY_DESCRIPTION")
					.setParameter("desc", antibioticClassMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", antibioticClassMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasteDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateCheckAntibioticsMasterDescriptionAvaiable(AntibioticClassMasterDto antibioticClassMasterDto)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_ANTIBIOTICS_TYPE_BY_BACTERIA_DESCRIPTION")
					.setParameter("desc",antibioticClassMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", antibioticClassMasterDto.getOrgId())
					.setParameter("antibioticClassId",	antibioticClassMasterDto.getAntibioticClassId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
