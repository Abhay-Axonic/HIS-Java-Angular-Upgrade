package com.param.lis.global.dao;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.math.BigInteger;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.entity.lis.global.AntibioticMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.AntibioticMasterDto;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.HourMasterDto;

@Repository
@SuppressWarnings("rawtypes")
public class AntibioticMasterDaoImpl extends  GenericDao<AntibioticMaster, Integer> implements IAntibioticMasterDao, ICommonConstants, IError{

	public AntibioticMasterDaoImpl() {
		super(AntibioticMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response addAntibioticMaster(AntibioticMasterDto antibioticMasterDto)throws ApplicationException {
		try{
			AntibioticMaster antibioticMaster = new AntibioticMaster();
			antibioticMaster.setCreatedBy(antibioticMasterDto.getCreatedBy());
			antibioticMaster.setCreatedDate(antibioticMasterDto.getCreatedDate());
			antibioticMaster.setCode(antibioticMasterDto.getCode());
			antibioticMaster.setDesc(antibioticMasterDto.getDesc());
			antibioticMaster.setStatus(antibioticMasterDto.getStatus());
			antibioticMaster.setOrgId(antibioticMasterDto.getOrgId());
			antibioticMaster.setUpdatedBy(antibioticMasterDto.getUpdatedBy());
			antibioticMaster.setUpdatedDate(antibioticMasterDto.getUpdatedDate());
			antibioticMaster = save(antibioticMaster);
			return new Response<>(SUCCESS, SUCCESS_200,ANTIBIOTIC_ADD_SUCC, null,null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkAntibioticMaster(AntibioticMasterDto antibioticMasterDto)throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listAntibioticsMaster = sessionFactory.getCurrentSession().getNamedQuery("GET_ANTIBIOTIC_MASTER_CODE").setParameter("code", antibioticMasterDto.getCode().trim().toLowerCase()).setParameter("orgId", antibioticMasterDto.getOrgId()).setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listAntibioticsMaster, null);
		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getAntibioticMasterById(Integer orgId, Integer  antibioticGroupId)
			throws ApplicationException {
		
		try
		{
			GlobalCommonDto containerMaster = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTIC_MASTER_ID").setParameter("orgId", orgId)
					.setParameter("antibioticId", antibioticGroupId)
                    .setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class))					
					.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, containerMaster);

		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateAntibioticMaster(AntibioticMasterDto antibioticMasterDto)
			throws ApplicationException {
		try
		{
			AntibioticMaster reportTypeMaster = mapper.map(antibioticMasterDto, AntibioticMaster.class,"AntibioticMasterDtoTOAntibioticMaster");
			update(reportTypeMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response ActivateInactivateAntibioticMaster(Integer orgId,
			Integer unitId, Character antibioticStatus)
			throws ApplicationException {

		try
		{
			AntibioticMaster antibioticMaster = findById(unitId);
			if (antibioticMaster.getAntibioticId() != 0)
			{
				antibioticMaster.setStatus(antibioticStatus);;
				AntibioticMaster antibioticMst = update(antibioticMaster);
				return new Response(SUCCESS, SUCCESS_200, ANTIBIOTIC_ACTIVATE_SUCC, null, antibioticMst);
				
			} else
			{
				return new Response(ERROR, ERR_500, ANTIBIOTIC_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listAntibioticMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listAntibioticMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_ANTIBIOTIC_MASTER_LIST").setParameter("orgId", orgId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listAntibioticMasterDto, null);
		
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getToTalAntibioticMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			BigInteger getAntibioticsMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_ANTIBIOTIC__MASTER_RECORD").setParameter("orgId", orgId).uniqueResult();
			
			
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
	public Response updateCheckeAntibioticMasterCodeAvaiable(
			AntibioticMasterDto atibioticMasterDto) throws ApplicationException {
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_ANTIBIOTIC_MASTER_CODE")
					.setParameter("code",atibioticMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", atibioticMasterDto.getOrgId())
					.setParameter("antibioticId",	atibioticMasterDto.getAntibioticId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public Response getListAntibioticMaster(Integer orgId) throws ApplicationException {
		try
		{
			List<AntibioticMasterDto> listAntibioticMasterDto= sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_ANTIBIOTIC_MASTER")
					.setParameter("orgId", orgId)//minesh
					.setResultTransformer(Transformers.aliasToBean(AntibioticMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listAntibioticMasterDto, null);
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkAntibioticMasterDescription(AntibioticMasterDto antibioticMasterDto)
			throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listAntibioticsMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTIC_MASTER_DESCRIPTION")
					.setParameter("desc", antibioticMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", antibioticMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listAntibioticsMaster, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateCheckeAntibioticMasterDescriptionAvaiable(AntibioticMasterDto antibioticMasterDto)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_ANTIBIOTIC_MASTER_DESCRIPTION")
					.setParameter("desc",antibioticMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", antibioticMasterDto.getOrgId())
					.setParameter("antibioticId",	antibioticMasterDto.getAntibioticId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
