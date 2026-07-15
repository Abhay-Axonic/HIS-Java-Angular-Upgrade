package com.param.lis.global.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.MicrobiologyStainMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.MicrobiologyStainMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings("rawtypes")
public class MicrobiologyStainigMasterDaoImpl extends  GenericDao<MicrobiologyStainMaster, Integer>
implements IMicrobiologyStainigMasterDao, ICommonConstants, IError{

	public MicrobiologyStainigMasterDaoImpl() {
		super(MicrobiologyStainMaster.class);
	}


	@Override
	public Response addMicrobStainigMaster(MicrobiologyStainMasterDto MicrobStainMasterDto)throws ApplicationException {
		try{
			MicrobiologyStainMaster microbstainigMaster = new MicrobiologyStainMaster();
			microbstainigMaster.setCreatedBy(MicrobStainMasterDto.getCreatedBy());
			microbstainigMaster.setCreatedDate(MicrobStainMasterDto.getCreatedDate());
			microbstainigMaster.setCode(MicrobStainMasterDto.getCode());
			microbstainigMaster.setDesc(MicrobStainMasterDto.getDesc());
			microbstainigMaster.setStatus(MicrobStainMasterDto.getStatus());
			microbstainigMaster.setOrgId(MicrobStainMasterDto.getOrgId());
			microbstainigMaster.setUpdatedBy(MicrobStainMasterDto.getUpdatedBy());
			microbstainigMaster.setUpdatedDate(MicrobStainMasterDto.getUpdatedDate());
			microbstainigMaster = save(microbstainigMaster);
			return new Response<>(SUCCESS, SUCCESS_200,MICRO_STAINIG_ADD_SUCC, null,null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkMicrobStainigMaster(MicrobiologyStainMasterDto MicrobStainMasterDto)throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listStainigMasterDtosessionFactory = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STAINIG_BY_MICROBSTAINIG_CODE")
					.setParameter("code", MicrobStainMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", MicrobStainMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listStainigMasterDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getMicrobStainigMasterById(Integer orgId, Integer  microStainId)
			throws ApplicationException {
		
		try
		{
			GlobalCommonDto containerMaster = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STAINIG_BY_MICROBSTAINIG_ID").setParameter("orgId", orgId)
					.setParameter("microStainId", microStainId)
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
	public Response updateMicrobStainigMaster(MicrobiologyStainMasterDto MicrobStainMasterDto)
			throws ApplicationException {
	
		try{
			MicrobiologyStainMaster microbstainigMaster = new MicrobiologyStainMaster();
			microbstainigMaster.setMicroStainId(MicrobStainMasterDto.getMicroStainId());
			microbstainigMaster.setCreatedBy(MicrobStainMasterDto.getCreatedBy());
			microbstainigMaster.setCreatedDate(MicrobStainMasterDto.getCreatedDate());
			microbstainigMaster.setCode(MicrobStainMasterDto.getCode());
			microbstainigMaster.setDesc(MicrobStainMasterDto.getDesc());
			microbstainigMaster.setStatus(MicrobStainMasterDto.getStatus());
			microbstainigMaster.setOrgId(MicrobStainMasterDto.getOrgId());
			microbstainigMaster.setUpdatedBy(MicrobStainMasterDto.getUpdatedBy());
			microbstainigMaster.setUpdatedDate(MicrobStainMasterDto.getUpdatedDate());
			microbstainigMaster = update(microbstainigMaster);
			return new Response<>(SUCCESS, SUCCESS_200,MICRO_STAINIG_ADD_SUCC, null,null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response ActivateInactivateMicrobStainigMaster(Integer orgId,
			Integer microStainId, Character organisumGroupStatus)
			throws ApplicationException {

		try
		{
			MicrobiologyStainMaster organisumGroupMaster = findById(microStainId);
			if (organisumGroupMaster.getMicroStainId() != 0)
			{
				organisumGroupMaster.setStatus(organisumGroupStatus);
				MicrobiologyStainMaster organisumGroupMst = update(organisumGroupMaster);
				return new Response(SUCCESS, SUCCESS_200,MICRO_STAINIG_ACTIVATE_SUCC, null, organisumGroupMst);
				
			} else
			{
				return new Response(ERROR, ERR_500,MICRO_STAINIG_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listMicrobStainigMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			List<GlobalCommonDto> lisColonyMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MICROBSTAINIG_MASTER_LIST").setParameter("orgId", orgId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, lisColonyMaster, null);
		
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response getToTalMicrobStainigMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			BigInteger getOrganisumGroupMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_MICROBSTAINIG_RECORD").setParameter("orgId", orgId).uniqueResult();
			
			
			if (getOrganisumGroupMasterCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getOrganisumGroupMasterCount);
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
	public Response updateCheckMicrobStainigMasterCodeAvaiable(
			MicrobiologyStainMasterDto microbiologyStainMasterDto)throws ApplicationException {
			
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_STAINIG_TYPE_BY_MICROBSTAINIG_CODE")
					.setParameter("code",microbiologyStainMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", microbiologyStainMasterDto.getOrgId())
					.setParameter("microStainId",	microbiologyStainMasterDto.getMicroStainId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkMicrobStainigMasterDesc(MicrobiologyStainMasterDto microbstainigMasterDto)
			throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listStainigMasterDtosessionFactory = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STAINIG_BY_MICROBSTAINIG_DESC")
					.setParameter("desc", microbstainigMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", microbstainigMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listStainigMasterDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Response updateCheckMicrobStainigMasterDescAvaiable(MicrobiologyStainMasterDto microbiologyStainMasterDto)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_STAINIG_TYPE_BY_MICROBSTAINIG_DESC")
					.setParameter("desc",microbiologyStainMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", microbiologyStainMasterDto.getOrgId())
					.setParameter("microStainId",	microbiologyStainMasterDto.getMicroStainId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
