package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.model.CompanyMaster;
import com.param.service.global.model.CompanyContarctGradeMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CompanyMasterDaoImpl extends GenericDao<CompanyMaster, Integer>
		implements ICompanyMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(CompanyMasterDaoImpl.class);

	public CompanyMasterDaoImpl() {
		super(CompanyMaster.class);
	}

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	HikariDataSource driverManager;

	@Override
	public Response getActiveCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			List<CompanyMasterDto> listCompanyMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_COMPANY_MASTER_LIST")
					.setParameter("unitId", companyMasterDto.getUnitId())
					.setParameter("orgId", companyMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(CompanyMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listCompanyMasterDto, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
		
			CompanyMaster companyMaster = mapper.map(companyMasterDto, CompanyMaster.class,
					"CompanyMasterDto_to_CompanyMaster");
			companyMaster.setIsTrust("I");
			companyMaster = save(companyMaster);
			// // this changes done  as kaustubh said
		/*	if(companyMasterDto.getListCompanyContarctGradeMapperDto()!=null && companyMasterDto.getListCompanyContarctGradeMapperDto().size()>0){
					ListIterator<CompanyContarctGradeMapperDto> itr = companyMasterDto.getListCompanyContarctGradeMapperDto()
							.listIterator();
					while (itr.hasNext()) {
							CompanyContarctGradeMapper companyContarctGradeMapper = new CompanyContarctGradeMapper();
							CompanyContarctGradeMapperDto companyContarctGradeMapperDto = itr.next();
							companyContarctGradeMapper.setCompanyId(companyMaster.getCompanyId());
							companyContarctGradeMapper.setContractGradeMasterId(companyContarctGradeMapperDto.getContractGradeMasterId());
							companyContarctGradeMapper.setOrgId(companyMasterDto.getOrganizationId());
							companyContarctGradeMapper.setStatus(companyMasterDto.getStatus());
							companyContarctGradeMapper.setOrgUnitId(companyMasterDto.getUnitId());
							companyContarctGradeMapper.setCreatedBy(companyMasterDto.getCreatedBy());
							companyContarctGradeMapper.setUpdatedBy(companyMasterDto.getUpdatedBy());
							companyContarctGradeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(companyMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
							companyContarctGradeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(companyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
							sessionFactory.getCurrentSession().save(companyContarctGradeMapper);
					}
			}*/
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, companyMaster);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			List<CompanyMasterDto> listCompanyMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_MASTER_LIST")
					.setParameter("unitId", companyMasterDto.getUnitId())
					.setParameter("orgId",companyMasterDto.getOrganizationId())
					.setParameter("companyTypeId",companyMasterDto.getCompanyTypeId())
					.setFirstResult(companyMasterDto.getOffset() != null ? companyMasterDto.getOffset() : 0)
					.setMaxResults(companyMasterDto.getNoOfRecordsPerPage() != null ? companyMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CompanyMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listCompanyMasterDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCompanyMasterListById(Integer unitId, Integer orgId, Integer companyId)
			throws ApplicationException {
		try {
			List<CompanyMasterDto> listCompanyMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_MASTER_LIST_BY_ID")
					.setParameter("unitId", unitId)
					.setParameter("orgId",orgId)
					.setParameter("companyId",companyId)
					.setResultTransformer(Transformers.aliasToBean(CompanyMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listCompanyMasterDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			CompanyMaster companyMaster = findById(companyMasterDto.getCompanyId());
			// BR-121-class fix: findById(companyId) alone has zero org/unit filter --
			// companyMasterDto.getOrganizationId()/getUnitId() are the caller's
			// service-layer-verified values by this point, not client-supplied;
			// refuse if the row being loaded doesn't actually belong to that tenant,
			// same "Not found" shape as a genuinely nonexistent id.
			if (companyMaster == null || companyMaster.getOrganizationId() == null
					|| !companyMaster.getOrganizationId().equals(companyMasterDto.getOrganizationId())
					|| companyMaster.getUnitId() == null
					|| !companyMaster.getUnitId().equals(companyMasterDto.getUnitId())) {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			companyMaster.setAddress(companyMasterDto.getAddress());
			companyMaster.setArCodeId(companyMasterDto.getArCodeId());
			companyMaster.setAreaId(companyMasterDto.getAreaId());
			companyMaster.setCityId(companyMasterDto.getCityId());
			companyMaster.setCompanyCode(companyMasterDto.getCompanyCode());
			companyMaster.setCompanyDesc(companyMasterDto.getCompanyDesc());
			companyMaster.setCompanyTypeId(companyMasterDto.getCompanyTypeId());
			companyMaster.setContactPersonName(companyMasterDto.getContactPersonName());
			companyMaster.setContractNo(companyMasterDto.getContractNo());
			companyMaster.setCountryId(companyMasterDto.getCountryId());		
			companyMaster.setUpdatedBy(companyMasterDto.getUpdatedBy());
			companyMaster.setCreditLimit(companyMasterDto.getCreditLimit());
			companyMaster.setDistrictId(companyMasterDto.getDistrictId());
			companyMaster.setEmailId(companyMasterDto.getEmailId());
			companyMaster.setEndDate(GlobalCommonDateUtils.getDate(companyMasterDto.getEndDate(), "dd-M-yyyy HH:mm:ss"));
			companyMaster.setFaxNo(companyMasterDto.getFaxNo());
			companyMaster.setMobileNo(companyMasterDto.getMobileNo());
			companyMaster.setOrganizationId(companyMasterDto.getOrganizationId());
			companyMaster.setPaymentTerms(companyMasterDto.getPaymentTerms());
//			companyMaster.setReportPath(companyMasterDto.getReportPath());
			companyMaster.setResidencePhoneNo(companyMasterDto.getResidencePhoneNo());
			companyMaster.setStartDate(GlobalCommonDateUtils.getDate(companyMasterDto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
			companyMaster.setStateId(companyMasterDto.getStateId());
			companyMaster.setUnitId(companyMasterDto.getUnitId());
			companyMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(companyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			companyMaster.setIsTrust("I");
			companyMaster.setZipcode(companyMasterDto.getZipcode());
			companyMaster.setBlockReportPrint(companyMasterDto.getBlockReportPrint());
			companyMaster.setGeneralLedgerId(companyMasterDto.getGeneralLedgerId());
			update(companyMaster);

			return new Response(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, companyMaster);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response countOfCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(CompanyMaster.class);
			c.add(Restrictions.eq("organizationId", companyMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", companyMasterDto.getUnitId()));
			c.add(Restrictions.eq("companyTypeId", companyMasterDto.getCompanyTypeId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCompanyMasterStatus(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			CompanyMaster companyMaster = findById(companyMasterDto.getCompanyId());
			// BR-121-class fix: findById(companyId) alone has zero org/unit filter at
			// all -- a pure ID-enumeration IDOR, not just a trusted-client-value one.
			// companyMasterDto.getOrganizationId()/getUnitId() are the caller's
			// service-layer-verified values by this point; refuse if the row being
			// loaded doesn't actually belong to that tenant.
			if (companyMaster == null || companyMaster.getOrganizationId() == null
					|| !companyMaster.getOrganizationId().equals(companyMasterDto.getOrganizationId())
					|| companyMaster.getUnitId() == null
					|| !companyMaster.getUnitId().equals(companyMasterDto.getUnitId())) {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			companyMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(companyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			companyMaster.setUpdatedBy(companyMasterDto.getUpdatedBy());
			companyMaster.setStatus(companyMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			return new Response(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, companyMaster.getUnitId());
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateStatusOfCompanyContarctGradeMapper(CompanyMasterDto companyMasterDto)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession()
					.createQuery("UPDATE CompanyContarctGradeMapper mppr " + " SET mppr.status='I'"
							+ " WHERE mppr.companyId=" + companyMasterDto.getCompanyId() + " AND mppr.orgId="
							+ companyMasterDto.getOrganizationId() + " AND mppr.orgUnitId="
							+ companyMasterDto.getUnitId())
					.executeUpdate();
			return new Response(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveCompanyContarctGradeMapper(CompanyContarctGradeMapper companyContarctGradeMapper)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().save(companyContarctGradeMapper);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCompanyMasterListByName(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			List<CompanyMasterDto> listCompanyMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_MASTER_LIST_BY_NAME")
					.setParameter("unitId", companyMasterDto.getUnitId())
					.setParameter("orgId",companyMasterDto.getOrganizationId())
					.setParameter("companyName",companyMasterDto.getCompanyDesc()!=null ? companyMasterDto.getCompanyDesc().toLowerCase() : null)
					.setParameter("faxNo",companyMasterDto.getFaxNo())
					.setParameter("emailId",companyMasterDto.getEmailId()!=null ? companyMasterDto.getEmailId().toLowerCase() : null)
					.setResultTransformer(Transformers.aliasToBean(CompanyMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listCompanyMasterDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCompanyMasterListByNameNotById(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			List<CompanyMasterDto> listCompanyMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("unitId", companyMasterDto.getUnitId())
					.setParameter("orgId",companyMasterDto.getOrganizationId())
					.setParameter("companyName",companyMasterDto.getCompanyDesc()!=null ? companyMasterDto.getCompanyDesc().toLowerCase() : null)
					.setParameter("faxNo",companyMasterDto.getFaxNo())
					.setParameter("emailId",companyMasterDto.getEmailId()!=null ? companyMasterDto.getEmailId().toLowerCase() : null)
					.setParameter("companyId",companyMasterDto.getCompanyId())
					.setResultTransformer(Transformers.aliasToBean(CompanyMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listCompanyMasterDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCompanyContractGradeByCompanyId(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			List<CompanyMasterDto> listCompanyMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_CONTRACT_GRADE_BY_COMPANY_ID")
					.setParameter("companyId",companyMasterDto.getCompanyId())
					.setParameter("orgId",companyMasterDto.getOrganizationId())
					.setParameter("unitId",companyMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CompanyMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listCompanyMasterDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveTPA(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			List<CompanyMasterDto> listCompanyMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TPA")
					.setParameter("unitId", companyMasterDto.getUnitId())
					.setParameter("orgId", companyMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(CompanyMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listCompanyMasterDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCompanyImage(CompanyMasterDto dto) throws ApplicationException {
		try{
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_COMPANY_IMAGE")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("companyId", dto.getCompanyId())
					.setParameter("imagePath", dto.getReportPath()).executeUpdate();
				if(result>0){
					 return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				}

		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Integer addDefaultEntry(Integer unitId,Integer orgId) throws ApplicationException {

		try {
			CompanyMaster companyMaster = new CompanyMaster();
				companyMaster.setOrganizationId(orgId);
				companyMaster.setUnitId(unitId);
				companyMaster.setCompanyDesc("Self");
				companyMaster.setCompanyTypeId(1);
				companyMaster.setStatus('A');
				companyMaster.setCreatedDate(TimeZoneComponent.getDateByzone(unitId));
				companyMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			companyMaster = save(companyMaster);

		return companyMaster.getCompanyId();

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}
	

}
