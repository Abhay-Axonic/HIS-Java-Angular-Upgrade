package com.param.lis.global.dao;

import java.math.BigInteger;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.entity.lis.global.OrganismGroupMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.OrganisumGroupMasterDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings("rawtypes")
public class OrganisumGroupMasterDaoImpl extends  GenericDao<OrganismGroupMaster, Integer> implements IOrganisumGroupMasterDao, ICommonConstants, IError{

	public OrganisumGroupMasterDaoImpl() {
		super(OrganismGroupMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response addOrganisumGroupMaster(OrganisumGroupMasterDto organisumGroupMasterDto)throws ApplicationException {
		try{
			OrganismGroupMaster organisumGroupMaster = new OrganismGroupMaster();
			organisumGroupMaster.setCreatedBy(organisumGroupMasterDto.getCreatedBy());
			organisumGroupMaster.setCreatedDate(organisumGroupMasterDto.getCreatedDate());
			organisumGroupMaster.setCode(organisumGroupMasterDto.getCode());
			organisumGroupMaster.setDesc(organisumGroupMasterDto.getDesc());
			organisumGroupMaster.setStatus(organisumGroupMasterDto.getStatus());
			organisumGroupMaster.setOrgId(organisumGroupMasterDto.getOrgId());
			organisumGroupMaster.setUpdatedBy(organisumGroupMasterDto.getUpdatedBy());
			organisumGroupMaster.setUpdatedDate(organisumGroupMasterDto.getUpdatedDate());
			organisumGroupMaster = save(organisumGroupMaster);
			return new Response<>(SUCCESS, SUCCESS_200,ORGANISUM_GROUP_ADD_SUCC, null,null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkOrganisumGroupMaster(OrganisumGroupMasterDto organisumGroupMasterDto)throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listUnitMasteDtosessionFactory = sessionFactory.getCurrentSession().getNamedQuery("GET_ORGANISM_GROUP_BY_ORGANISM_CODE").setParameter("code", organisumGroupMasterDto.getCode().trim().toLowerCase()).setParameter("orgId", organisumGroupMasterDto.getOrgId()).setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasteDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getOrganisumGroupMasterById(Integer orgId, Integer  organismGroupId)
			throws ApplicationException {
		
		try
		{
			GlobalCommonDto containerMaster = (GlobalCommonDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORGANISM_GROUP_BY_ORGANISM_ID").setParameter("orgId", orgId)
					.setParameter("organismGroupId", organismGroupId)
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
	public Response updateOrganisumGroupMaster(OrganisumGroupMasterDto organisumGroupMasterDto)
			throws ApplicationException {
		try
		{
			OrganismGroupMaster reportTypeMaster = mapper.map(organisumGroupMasterDto, OrganismGroupMaster.class,"OrganismGroupMasterDtoTOOrganismGroupMaster");
			update(reportTypeMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e)
		{
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response ActivateInactivateOrganisumGroupMaster(Integer orgId,
			Integer organisumGroupId, Character organisumGroupStatus)
			throws ApplicationException {

		try
		{
			OrganismGroupMaster organisumGroupMaster = findById(organisumGroupId);
			if (organisumGroupMaster.getOrganismGroupId() != 0)
			{
				organisumGroupMaster.setStatus(organisumGroupStatus);;
				OrganismGroupMaster organisumGroupMst = update(organisumGroupMaster);
				return new Response(SUCCESS, SUCCESS_200,ORGANISUM_GROUP_ACTIVATE_SUCC, null, organisumGroupMst);
				
			} else
			{
				return new Response(ERROR, ERR_500,ORGANISUM_GROUP_NOT_FOUND, null, null);
			}

		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response listOrganisumGroupMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			List<GlobalCommonDto> lisColonyMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORGANIsm_GROUP_MASTER_LIST").setParameter("orgId", orgId)
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
	public Response getToTalOrganisumGroupMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			BigInteger getOrganisumGroupMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_ORGANISM_GROUP_RECORD").setParameter("orgId", orgId).uniqueResult();
			
			
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
	public Response updateCheckOrganisumGroupMasterCodeAvaiable(
			OrganisumGroupMasterDto organisumGroupMasterDto)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_ORGANISM_GROUP_TYPE_BY_ORGANISM_CODE")
					.setParameter("code",organisumGroupMasterDto.getCode().trim().toLowerCase())
					.setParameter("orgId", organisumGroupMasterDto.getOrgId())
					.setParameter("organismGroupId",	organisumGroupMasterDto.getOrganismGroupId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response checkOrganisumGroupMasterDesc(OrganisumGroupMasterDto organisumGroupMasterDto)
			throws ApplicationException {
		try
		{
			List<GlobalCommonDto> listUnitMasteDtosessionFactory = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORGANISM_GROUP_BY_ORGANISM_DESC")
					.setParameter("desc", organisumGroupMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", organisumGroupMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null, listUnitMasteDtosessionFactory, null);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response updateCheckOrganisumGroupMasterDescAvaiable(OrganisumGroupMasterDto organisumGroupMasterDto)
			throws ApplicationException {
		try {
			List<GlobalCommonDto> listCheckPhlebotomyCodeFactory = sessionFactory
					.getCurrentSession()
					.getNamedQuery("UPDATE_GET_ORGANISM_GROUP_TYPE_BY_ORGANISM_DESC")
					.setParameter("desc",organisumGroupMasterDto.getDesc().trim().toLowerCase())
					.setParameter("orgId", organisumGroupMasterDto.getOrgId())
					.setParameter("organismGroupId",	organisumGroupMasterDto.getOrganismGroupId())
					.setResultTransformer(Transformers.aliasToBean(GlobalCommonDto.class)).list();
			return new Response(SUCCESS, null, null,listCheckPhlebotomyCodeFactory, null);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
