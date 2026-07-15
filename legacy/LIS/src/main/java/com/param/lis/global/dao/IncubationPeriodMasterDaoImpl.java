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
import com.param.entity.lis.global.IncubationPeriodMaster;
import com.param.global.common.ServiceException;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.IncubationPeriodMasterDto;

@Repository
@SuppressWarnings("rawtypes")
public class IncubationPeriodMasterDaoImpl extends  GenericDao<IncubationPeriodMaster, Integer> implements IIncubationPeriodMasterDao, ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(IncubationPeriodMasterDaoImpl.class);

	public IncubationPeriodMasterDaoImpl() {
		super(IncubationPeriodMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response addIncubationPeriodMaster(IncubationPeriodMasterDto incubationPeriodMasterDto)throws ApplicationException {
		try{
			IncubationPeriodMaster incubationPeriodMaster = new IncubationPeriodMaster();
			incubationPeriodMaster.setIncubationPeriodCode(incubationPeriodMasterDto.getIncubationPeriodCode());
			if(incubationPeriodMasterDto.getIncubationTimeId()==1)
			{
				incubationPeriodMaster.setIncubationPeriodTime(incubationPeriodMasterDto.getIncubationValue()+" Hours");
				incubationPeriodMaster.setIncubationInHours(incubationPeriodMasterDto.getIncubationValue());
			}
			if(incubationPeriodMasterDto.getIncubationTimeId()==2)
			{
				incubationPeriodMaster.setIncubationPeriodTime(incubationPeriodMasterDto.getIncubationValue()+" Days");
				incubationPeriodMaster.setIncubationInHours(incubationPeriodMasterDto.getIncubationValue()*24);
			}
			incubationPeriodMaster.setIncubationValue(incubationPeriodMasterDto.getIncubationValue());
			incubationPeriodMaster.setIncubationTimeId(incubationPeriodMasterDto.getIncubationTimeId());
			incubationPeriodMaster.setStatus('A');
			incubationPeriodMaster.setCreatedBy(incubationPeriodMasterDto.getCreatedBy());
			incubationPeriodMaster.setCreatedDate(incubationPeriodMasterDto.getCreatedDate());
			incubationPeriodMaster.setOrgId(incubationPeriodMasterDto.getOrgId());
			incubationPeriodMaster.setUpdatedBy(incubationPeriodMasterDto.getUpdatedBy());
			incubationPeriodMaster.setUpdatedDate(incubationPeriodMasterDto.getUpdatedDate());
			incubationPeriodMaster = save(incubationPeriodMaster);
			return new Response<>(SUCCESS, SUCCESS_200,INCUBATION_PERIOD_MASTER_ADD_SUCC, null,null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkIncubationPeriodMaster(IncubationPeriodMasterDto incubationPeriodMasterDto)throws ApplicationException {
		try
		{
			List<IncubationPeriodMasterDto> listIncubationPeriodMasterFactory = sessionFactory.getCurrentSession().getNamedQuery("GET_REPORT_TYPE_BY_INCUBATION_PERIOD_CODE").setParameter("incubationPeriodCode", incubationPeriodMasterDto.getIncubationPeriodCode().trim().toLowerCase()).setParameter("orgId", incubationPeriodMasterDto.getOrgId()).setResultTransformer(Transformers.aliasToBean(IncubationPeriodMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listIncubationPeriodMasterFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getIncubationPeriodMasterById(Integer orgId, Integer  incubationPeriodId)
			throws ApplicationException {
		
		try
		{
			IncubationPeriodMasterDto incubationPeriodMaster = (IncubationPeriodMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REPORT_TYPE_BY_INCUBATION_PERIOD_ID")
					.setParameter("orgId", orgId)
					.setParameter("incubationPeriodId", incubationPeriodId)
                    .setResultTransformer(Transformers.aliasToBean(IncubationPeriodMasterDto.class))					
					.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, incubationPeriodMaster);

		} catch (Exception e)
		{
			logger.error("Exception in getIncubationPeriodMasterById", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateIncubationPeriodMaster(IncubationPeriodMasterDto incubationPeriodMasterDto)
			throws ApplicationException {
		try
		{
			IncubationPeriodMaster reportTypeMaster = mapper.map(incubationPeriodMasterDto, IncubationPeriodMaster.class,"IncubationPeriodMasterDtoTOIncubationPeriodMaster");
			update(reportTypeMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e)
		{
			logger.error("Exception in updateIncubationPeriodMaster", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response ActivateInactivateIncubationPeriodMaster(Integer orgId,
			Integer incubationPeriodId, Character incubationPeriodStatus)
			throws ApplicationException {

		try
		{
			IncubationPeriodMaster IncubationPeriodMaster = findById(incubationPeriodId);
			if (IncubationPeriodMaster.getIncubationPeriodId() != 0)
			{
				IncubationPeriodMaster.setStatus(incubationPeriodStatus);
				IncubationPeriodMaster bacteriaMst = update(IncubationPeriodMaster);
				return new Response(SUCCESS, SUCCESS_200,INCUBATION_PERIOD_MASTER_ACTIVATE_SUCC, null, bacteriaMst);
				
			} else
			{
				return new Response(ERROR, ERR_500,INCUBATION_PERIOD_MASTER_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			logger.error("Exception in ActivateInactivateIncubationPeriodMaster", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listIncubationPeriodMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			List<IncubationPeriodMasterDto> listIncubationPeriodMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_INCUBATION_PERIOD_MASTER_LIST").setParameter("orgId", orgId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(IncubationPeriodMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listIncubationPeriodMasterDto, null);
		
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getToTalIncubationPeriodMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			BigInteger incubationPeriodMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_INCUBATION_PERIOD_RECORD").setParameter("orgId", orgId).uniqueResult();
			
			
			if (incubationPeriodMasterCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, incubationPeriodMasterCount);
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
	public Response updateCheckIncubationPeriodCodeAvaiable(
			IncubationPeriodMasterDto incubationPeriodMasterDto)
			throws ApplicationException {
		try {
			List<IncubationPeriodMaster> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_REPORT_TYPE_BY_INCUBATION_PERIOD_CODE")
					.setParameter("incubationPeriodCode",incubationPeriodMasterDto.getIncubationPeriodCode().trim().toLowerCase())
					.setParameter("orgId", incubationPeriodMasterDto.getOrgId())
					.setParameter("incubationPeriodId",	incubationPeriodMasterDto.getIncubationPeriodId())
					.setResultTransformer(Transformers.aliasToBean(IncubationPeriodMaster.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkForUniqueIncubationTime(IncubationPeriodMasterDto incubationPeriodDto)
			throws ApplicationException {
		try
		{
			List<IncubationPeriodMasterDto> listIncubationPeriodMasterFactory = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCUBATION_PERIOD_MASTER_BY_TIME_AND_VALUE")
					.setParameter("incubationTimeId", incubationPeriodDto.getIncubationTimeId())
					.setParameter("incubationValue", incubationPeriodDto.getIncubationValue())
					.setParameter("orgId", incubationPeriodDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(IncubationPeriodMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listIncubationPeriodMasterFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateCheckIncubationPeriodDescriptionAvaiable(IncubationPeriodMasterDto incubationPeriodMasterDto)
			throws ApplicationException {
		try {
			List<IncubationPeriodMaster> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_REPORT_TYPE_BY_INCUBATION_PERIOD_DESCRIPTION")
					.setParameter("incubationTimeId", incubationPeriodMasterDto.getIncubationTimeId())
					.setParameter("incubationValue", incubationPeriodMasterDto.getIncubationValue())
					.setParameter("orgId", incubationPeriodMasterDto.getOrgId())
					.setParameter("incubationPeriodId",	incubationPeriodMasterDto.getIncubationPeriodId())
					.setResultTransformer(Transformers.aliasToBean(IncubationPeriodMaster.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
