package com.param.billing.global.dao;

import java.util.Date;
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
import com.param.billing.global.dto.HospitalAccountMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.model.HospitalAccountMaster;

import in.param.common.dao.GenericDao;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class HospitalAccountMasterDaoImpl extends GenericDao<HospitalAccountMaster, Integer> implements IHospitalAccountMasterDao,ICommonConstants{

	@Autowired
	SessionFactory sessionFactory;
	public HospitalAccountMasterDaoImpl() {
		super(HospitalAccountMaster.class);
	}
	@Override
	public Response getActiveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			List hospitalAccountList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_HOSPITAL_ACCOUNT_MASTER")
					.setParameter("unitId", hospitalAccountMasterDto.getUnitId())
					.setParameter("orgId", hospitalAccountMasterDto.getOrgnizationId())
					.setResultTransformer(Transformers.aliasToBean(HospitalAccountMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, hospitalAccountList, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getHospitalAccountMasterByName(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			List hospitalAccountMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_HOSPITAL_ACCOUNT_MASTER_BY_NAME")
					.setParameter("hospitalAccountDesc", hospitalAccountMasterDto.getHospitalAccountDesc().toLowerCase())
					.setParameter("orgId", hospitalAccountMasterDto.getOrgnizationId())
					.setParameter("unitId", hospitalAccountMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(HospitalAccountMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, hospitalAccountMasterList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response saveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			HospitalAccountMaster hospitalAccountMaster=new HospitalAccountMaster();
			hospitalAccountMaster.setHospitalAccountCode(hospitalAccountMasterDto.getHospitalAccountCode());
			hospitalAccountMaster.setHospitalAccountDesc(hospitalAccountMasterDto.getHospitalAccountDesc());
			hospitalAccountMaster.setIsDefault(hospitalAccountMasterDto.getIsDefault());
			hospitalAccountMaster.setCreatedBy(hospitalAccountMasterDto.getCreatedBy());
			hospitalAccountMaster.setCreatedDate(GlobalCommonDateUtils.getDate(hospitalAccountMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			hospitalAccountMaster.setOrgnizationId(hospitalAccountMasterDto.getOrgnizationId());
			hospitalAccountMaster.setStatus(hospitalAccountMasterDto.getStatus());
			hospitalAccountMaster.setUpdatedBy(hospitalAccountMasterDto.getUpdatedBy());
			hospitalAccountMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(hospitalAccountMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			hospitalAccountMaster.setUnitId(hospitalAccountMasterDto.getUnitId());
			save(hospitalAccountMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getHospitalAccountMasterById(Integer hospitalAccountId, Integer orgId, Integer unitId) {
		try {
			HospitalAccountMasterDto hospitalAccountMasterDto = (HospitalAccountMasterDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_HOSPITAL_ACCOUNT_MASTER_LIST_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("hospitalAccountId", hospitalAccountId)
					.setResultTransformer(Transformers.aliasToBean(HospitalAccountMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, hospitalAccountMasterDto);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getHospitalAccountMasterList(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			List hospitalAccountMasterList = sessionFactory.getCurrentSession().getNamedQuery("GET_HOSPITAL_ACCOUNT_MASTER_LIST")
					.setParameter("orgId", hospitalAccountMasterDto.getOrgnizationId())
					.setParameter("unitId", hospitalAccountMasterDto.getUnitId())
					.setFirstResult(hospitalAccountMasterDto.getOffset() != null ? hospitalAccountMasterDto.getOffset() : 0)
					.setMaxResults(hospitalAccountMasterDto.getNoOfRecordsPerPage() != null ? hospitalAccountMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(HospitalAccountMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, hospitalAccountMasterList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getHospitalAccountMasterByNameNotId(HospitalAccountMasterDto hospitalAccountMasterDto) {
     try {
			
			List hospitalAccountList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_HOSPITAL_ACCOUNT_MASTER_BY_NAME_NOT_ID")
					.setParameter("hospitalAccountId", hospitalAccountMasterDto.getHospitalAccountId())
					.setParameter("hospitalAccountDesc", hospitalAccountMasterDto.getHospitalAccountDesc().toLowerCase())
					.setParameter("orgId", hospitalAccountMasterDto.getOrgnizationId())
					.setParameter("unitId", hospitalAccountMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(HospitalAccountMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, hospitalAccountList, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
try {
			
	      HospitalAccountMaster hospitalAccountMaster=findById(hospitalAccountMasterDto.getHospitalAccountId());
			
	        hospitalAccountMaster.setHospitalAccountCode((hospitalAccountMasterDto.getHospitalAccountCode()));
	        hospitalAccountMaster.setHospitalAccountDesc(hospitalAccountMasterDto.getHospitalAccountDesc());
	        hospitalAccountMaster.setIsDefault(hospitalAccountMasterDto.getIsDefault());
	        hospitalAccountMaster.setOrgnizationId(hospitalAccountMasterDto.getOrgnizationId());
			hospitalAccountMaster.setUpdatedBy(hospitalAccountMasterDto.getUpdatedBy());
			hospitalAccountMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(hospitalAccountMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			hospitalAccountMaster.setUnitId(hospitalAccountMasterDto.getUnitId());
			update(hospitalAccountMaster);
			return new Response<>(SUCCESS,SUCCESS_CODE , COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateHospitalAccountMasterStatus(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			HospitalAccountMaster hospitalAccountMaster=findById(hospitalAccountMasterDto.getHospitalAccountId());
			hospitalAccountMaster.setStatus(hospitalAccountMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			hospitalAccountMaster.setUpdatedBy(hospitalAccountMasterDto.getUpdatedBy());
			hospitalAccountMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(hospitalAccountMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(hospitalAccountMaster);
			return new Response<>(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, hospitalAccountMaster.getUnitId());

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getHospitalAccountMasterCount(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(HospitalAccountMaster.class); 
			c.add(Restrictions.eq("orgnizationId", hospitalAccountMasterDto.getOrgnizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public void updateAllHospitalAccount(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			
			
			sessionFactory.getCurrentSession().createQuery("update HospitalAccountMaster  set "
					+ "isDefault='N' "
					+ "WHERE orgnizationId=:orgId and unitId=:unitId")
				.setParameter("orgId", hospitalAccountMasterDto.getOrgnizationId())
				.setParameter("unitId",hospitalAccountMasterDto.getUnitId())
		.executeUpdate();
			

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		
	}

}
