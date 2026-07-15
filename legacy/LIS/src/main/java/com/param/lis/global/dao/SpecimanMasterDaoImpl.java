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
import com.param.entity.lis.global.SpecimanMaster;
import com.param.global.common.ServiceException;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.SpecimanMasterDto;

@Repository
@SuppressWarnings("rawtypes")
public class SpecimanMasterDaoImpl extends  GenericDao<SpecimanMaster, Integer> implements ISpecimanMasterDao, ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(SpecimanMasterDaoImpl.class);

	public SpecimanMasterDaoImpl() {
		super(SpecimanMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response addSpecimanMaster(SpecimanMasterDto specimanMasterDto)throws ApplicationException {
		try{
			SpecimanMaster specimanMaster = new SpecimanMaster();
			specimanMaster.setCreatedBy(specimanMasterDto.getCreatedBy());
			specimanMaster.setCreatedDate(specimanMasterDto.getCreatedDate());
			specimanMaster.setCode(specimanMasterDto.getCode());
			specimanMaster.setDesc(specimanMasterDto.getDesc());
			specimanMaster.setStatus(specimanMasterDto.getStatus());
			specimanMaster.setOrgId(specimanMasterDto.getOrgId());
			specimanMaster.setUpdatedBy(specimanMasterDto.getUpdatedBy());
			specimanMaster.setUpdatedDate(specimanMasterDto.getUpdatedDate());
			specimanMaster = save(specimanMaster);
			return new Response<>(SUCCESS, SUCCESS_200,SPECIMAN_ADD_SUCC, null,specimanMaster);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkSpecimanMaster(SpecimanMasterDto specimanMasterDto)throws ApplicationException {
		try
		{
			List<SpecimanMasterDto> listUnitMasteDtosessionFactory = sessionFactory.getCurrentSession().getNamedQuery("GET_SPECIMAN_TYPE_BY_SPECIMAN_CODE").setParameter("code", specimanMasterDto.getCode().trim().toLowerCase()).setParameter("orgId", specimanMasterDto.getOrgId()).setResultTransformer(Transformers.aliasToBean(SpecimanMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasteDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getSpecimanMasteById(Integer orgId, Integer  specimanId)
			throws ApplicationException {
		
		try
		{
			SpecimanMasterDto containerMaster = (SpecimanMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIMAN_TYPE_BY_SPECIMAN_ID").setParameter("orgId", orgId)
					.setParameter("specimanId", specimanId)
                    .setResultTransformer(Transformers.aliasToBean(SpecimanMasterDto.class))					
					.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, containerMaster);

		} catch (Exception e)
		{
			logger.error("Exception getting speciman master by id", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateSpecimanMaster(SpecimanMasterDto specimanMasterDto)
			throws ApplicationException {
		try
		{
			SpecimanMaster reportTypeMaster = mapper.map(specimanMasterDto, SpecimanMaster.class,"SpecimanMasterDtoTOSpecimanMaster");
			reportTypeMaster =update(reportTypeMaster);
			return new Response(SUCCESS, null, null, null, reportTypeMaster);
		} catch (Exception e)
		{
			logger.error("Exception updating speciman master", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response ActivateInactivateSpecimanMaster(Integer orgId,
			Integer unitId, Character unitStatus)
			throws ApplicationException {

		try
		{
			SpecimanMaster specimanMaster = findById(unitId);
			if (specimanMaster.getSpecimanId() != 0)
			{
				specimanMaster.setStatus(unitStatus);;
				SpecimanMaster antibioticMst = update(specimanMaster);
				return new Response(SUCCESS, SUCCESS_200,SPECIMAN_ACTIVATE_SUCC, null, antibioticMst);
				
			} else
			{
				return new Response(ERROR, ERR_500,SPECIMAN_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			logger.error("Exception activating/inactivating speciman master", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listSpecimanMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			List<SpecimanMasterDto> listUnitMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_SPECIMAN_MASTER_LIST").setParameter("orgId", orgId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(SpecimanMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasterDto, null);
		
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getToTalSpecimanMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			BigInteger getToTalSpecimanMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_SPECIMAN_RECORD").setParameter("orgId", orgId).uniqueResult();
			
			
			if (getToTalSpecimanMasterCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getToTalSpecimanMasterCount);
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
	public Response updateCheckReportTypeCodeAvaiable(
			SpecimanMasterDto specimanMasterDto) throws ApplicationException {
		try {
			List<SpecimanMasterDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_SPECIMAN_TYPE_BY_SPECIMAN_CODE")
					.setParameter("code",specimanMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", specimanMasterDto.getOrgId())
					.setParameter("specimanId",	specimanMasterDto.getSpecimanId())
					.setResultTransformer(Transformers.aliasToBean(SpecimanMasterDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public Response specimanGrossRequriedInSpecimanMaster(Integer orgId,
			Integer specimanId, Character specimanGross)
			throws ApplicationException {
		try
		{
			SpecimanMaster specimanMaster = findById(specimanId);
			if (specimanMaster.getSpecimanId() != 0)
			{
				specimanMaster.setSpecimanGross(specimanGross);
				SpecimanMaster specimanMst = update(specimanMaster);
				return new Response(SUCCESS, SUCCESS_200,SPECIMAN_GROSS_SUCC, null, specimanMst);
				
			} else
			{
				return new Response(ERROR, ERR_500,SPECIMAN_GROSS_FAIL, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public Response specimanBlockRequriedInSpecimanMaster(Integer orgId,
			Integer specimanId, Character specimanBlock)
			throws ApplicationException {
		try
		{
			SpecimanMaster specimanMaster = findById(specimanId);
			if (specimanMaster.getSpecimanId() != 0)
			{
				specimanMaster.setSpecimanBlock(specimanBlock);
				SpecimanMaster specimanMst = update(specimanMaster);
				return new Response(SUCCESS, SUCCESS_200,SPECIMAN_GROSS_SUCC, null, specimanMst);
				
			} else
			{
				return new Response(ERROR, ERR_500,SPECIMAN_GROSS_FAIL, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkSpecimanMasterDesc(SpecimanMasterDto specimanMasterDto) throws ApplicationException {
		try
		{
			List<SpecimanMasterDto> listUnitMasteDtosessionFactory = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIMAN_TYPE_BY_SPECIMAN_DESC")
					.setParameter("desc", specimanMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", specimanMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(SpecimanMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasteDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateCheckReportTypeDescAvaiable(SpecimanMasterDto specimanMasterDto) throws ApplicationException {
		try {
			List<SpecimanMasterDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_SPECIMAN_TYPE_BY_SPECIMAN_DESC")
					.setParameter("desc",specimanMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", specimanMasterDto.getOrgId())
					.setParameter("specimanId",	specimanMasterDto.getSpecimanId())
					.setResultTransformer(Transformers.aliasToBean(SpecimanMasterDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
