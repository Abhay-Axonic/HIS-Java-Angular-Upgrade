package com.param.service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.service.dto.ContractGradeMasterDto;
import com.param.service.global.model.ContractGradeMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ContractGradeMasterDaoImpl extends GenericDao<ContractGradeMaster, Integer> implements IContractGradeMasterDao, ICommonConstants, IError{

	public ContractGradeMasterDaoImpl() {
		super(ContractGradeMaster.class);

	}
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveContractGradeMaster(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try{
			ContractGradeMaster contractGradeMaster=new ContractGradeMaster();
			contractGradeMaster.setCode(contractGradeMasterDto.getCode());
			contractGradeMaster.setGradeDesc(contractGradeMasterDto.getGradeDesc());
			contractGradeMaster.setOrgId(contractGradeMasterDto.getOrgId());
			contractGradeMaster.setUnitId(contractGradeMasterDto.getUnitId());
			contractGradeMaster.setStatus(contractGradeMasterDto.getStatus());
			contractGradeMaster.setCreatedBy(contractGradeMasterDto.getCreatedBy());
			contractGradeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(contractGradeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			contractGradeMaster.setUpdatedBy(contractGradeMasterDto.getUpdatedBy());
			contractGradeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(contractGradeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(contractGradeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}catch (HibernateException he) {
			}catch (Exception e) {
				
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateContractGradeMaster(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try{
			ContractGradeMaster contractGradeMaster=findById(contractGradeMasterDto.getContractGradeMasterId());
			contractGradeMaster.setCode(contractGradeMasterDto.getCode());
			contractGradeMaster.setGradeDesc(contractGradeMasterDto.getGradeDesc());
			contractGradeMaster.setOrgId(contractGradeMasterDto.getOrgId());
			contractGradeMaster.setUnitId(contractGradeMasterDto.getUnitId());
			contractGradeMaster.setUpdatedBy(contractGradeMasterDto.getUpdatedBy());
			contractGradeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(contractGradeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(contractGradeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}catch (HibernateException he) {
			}catch (Exception e) {
				
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	@Override
	public Response updateContractGradeMasterStatus(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try{
			ContractGradeMaster contractGradeMaster=findById(contractGradeMasterDto.getContractGradeMasterId());
			
			contractGradeMaster.setStatus(contractGradeMasterDto.getStatus());
			contractGradeMaster.setUpdatedBy(contractGradeMasterDto.getUpdatedBy());
			contractGradeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(contractGradeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(contractGradeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}catch (HibernateException he) {
			}catch (Exception e) {
				
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getContractGradeMasterById(Integer contractGradeMasterId, Integer orgId, Integer unitId) throws ApplicationException{
		try {
			List<ContractGradeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTRACT_GRADE_MASTER_BY_ID")
					.setParameter("contractGradeMasterId", contractGradeMasterId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ContractGradeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getContractGradeMasterList(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			List<ContractGradeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTRACT_GRADE_MASTER_List")
					.setParameter("orgId", contractGradeMasterDto.getOrgId())
					.setParameter("unitId", contractGradeMasterDto.getUnitId())
					.setFirstResult(contractGradeMasterDto.getOffset() != null ? contractGradeMasterDto.getOffset() : 0)
					.setMaxResults(contractGradeMasterDto.getNoOfRecordsPerPage() != null ? contractGradeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ContractGradeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getContractGradeMasterCount(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ContractGradeMaster.class);
			crit.add(Restrictions.eq("orgId", contractGradeMasterDto.getOrgId()));
			crit.add(Restrictions.eq("unitId", contractGradeMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getContractGradeMasterByName(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			List<ContractGradeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTRACT_GRADE_MASTER_BY_NAME")
					.setParameter("gradeDesc", contractGradeMasterDto.getGradeDesc().toLowerCase())
					.setParameter("unitId", contractGradeMasterDto.getUnitId())
					.setParameter("orgId", contractGradeMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(ContractGradeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getContractGradeMasterByNameNotId(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			List<ContractGradeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTRACT_GRADE_MASTER_BY_NAME_NOT_ID")
					.setParameter("contractGradeMasterId", contractGradeMasterDto.getContractGradeMasterId())
					.setParameter("gradeDesc", contractGradeMasterDto.getGradeDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ContractGradeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveContractGradeMasterList(ContractGradeMasterDto contractGradeMasterDto) {
		try {
			List<ContractGradeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CONTRACT_GRADE_MASTER_LIST")
					.setParameter("unitId", contractGradeMasterDto.getUnitId())
					.setParameter("orgId", contractGradeMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(ContractGradeMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
	@Override
	public Response getContractGradeMasterNotMappedInMapper(ContractGradeMasterDto contractGradeMasterDto) {
		try {
			List<ContractGradeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTRACT_GRADE_MAPPER_NOT_MAPPED_IN_MAPPER")
					.setParameter("unitId", contractGradeMasterDto.getUnitId())
					.setParameter("orgId", contractGradeMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(ContractGradeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null,  list, null);
			
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
