package com.param.billing.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.model.DepositMatrixConfigMaster;
import com.param.billing.unit.dto.DepositMatrixConfigMasterDto;
import com.param.billing.unit.dto.DepositMatrixReqDto;
import com.param.billing.unit.dto.DepositMatrixResDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DepositMatrixConfigDaoImpl extends GenericDao<DepositMatrixConfigMaster, Integer> implements IDepositMatrixConfigDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(DepositMatrixConfigDaoImpl.class);

	public DepositMatrixConfigDaoImpl() {
		super(DepositMatrixConfigMaster.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response getDepositMatrixList(int orgId, int unitId, int offset, int recordPerPage)
			throws ApplicationException {
		try {
			List depositMatrixConfigMasterList = sessionFactory.getCurrentSession().getNamedQuery("GET_DEPOSIT_MATRIX_LIST")
					.setInteger("orgId", orgId)
					.setInteger("unitId", unitId)
					.setFirstResult(offset != 0 ? offset : 0)
					.setMaxResults(recordPerPage != 0 ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(DepositMatrixConfigMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, depositMatrixConfigMasterList, null);
		} catch (Exception e) {
			logger.error("getDepositMatrixList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDepositMatrixConfig(DepositMatrixConfigMasterDto depositMatrixConfigMasterDto)
			throws ApplicationException {
		try {
			DepositMatrixConfigMaster master=new DepositMatrixConfigMaster();
			master.setPatientTypeId(depositMatrixConfigMasterDto.getPatientTypeId());
			master.setPaymentEntitlementId(depositMatrixConfigMasterDto.getPaymentEntitlementId());
			master.setBedCategoryId(depositMatrixConfigMasterDto.getBedCategoryId());
			master.setAgainstDeposit(depositMatrixConfigMasterDto.getAgainstDeposit());
			master.setSurgical(depositMatrixConfigMasterDto.getSurgical());
			master.setNonSurgical(depositMatrixConfigMasterDto.getNonSurgical());
			master.setStatus(depositMatrixConfigMasterDto.getStatus());
			// BR-batch2-org-trust fix (2026-08-03, live-proven): bind JWT-verified tenant only.
			master.setOrganisationId(verifiedIdentityService.getVerifiedOrganizationId());
			master.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			// (old code wrote client-supplied org/unit -- org-1 session created org-999 row)
			master.setCreatedBy(depositMatrixConfigMasterDto.getCreatedBy());
			master.setCreatedDate(depositMatrixConfigMasterDto.getCreatedDate() != null && !depositMatrixConfigMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(depositMatrixConfigMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(depositMatrixConfigMasterDto.getUnitId()));
			master.setUpdatedBy(depositMatrixConfigMasterDto.getUpdatedBy());
			master.setUpdatedDate(depositMatrixConfigMasterDto.getUpdatedDate() != null && !depositMatrixConfigMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(depositMatrixConfigMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(depositMatrixConfigMasterDto.getUnitId()));
			save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);

		} catch (HibernateException he) {
			logger.error("saveDepositMatrixConfig failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDepositMatrixConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDepositMatrixConfig(int mDepositMatrixId) throws ApplicationException {
		try {
			// BATCH-4 org-trust fix: the BY_ID read was unscoped — a forged victim ID returned
			// cross-tenant config. Bind the verified org/unit.
			DepositMatrixConfigMasterDto dto = (DepositMatrixConfigMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPOSIT_MATRIX_CONFIG_BY_ID")
					.setParameter("mDepositMatrixId", mDepositMatrixId)
					.setParameter("orgId", verifiedIdentityService.getVerifiedOrganizationId())
					.setParameter("unitId", verifiedIdentityService.getVerifiedUnitId())
					.setResultTransformer(Transformers.aliasToBean(DepositMatrixConfigMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);

		} catch (HibernateException he) {
			logger.error("getDepositMatrixConfig failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDepositMatrixConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDepositMatrixConfig(DepositMatrixConfigMasterDto dto)
			throws ApplicationException {
		try {
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			DepositMatrixConfigMaster master=findById(dto.getmDepositMatrixId());
			// BR-batch2-org-trust fix (2026-08-03, live-proven): reject rows not owned
			// by the verified tenant (old code overwrote the row's org/unit with DTO
			// values after findById -- full row-steal shape).
			if (master == null || master.getOrganisationId() == null
					|| !master.getOrganisationId().equals(verifiedOrgId)
					|| master.getUnitId() == null
					|| !master.getUnitId().equals(verifiedUnitId)) {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Cross-tenant update rejected", null, null);
			}
			master.setPatientTypeId(dto.getPatientTypeId());
			master.setPaymentEntitlementId(dto.getPaymentEntitlementId());
			master.setBedCategoryId(dto.getBedCategoryId());
			master.setAgainstDeposit(dto.getAgainstDeposit());
			master.setSurgical(dto.getSurgical());
			master.setNonSurgical(dto.getNonSurgical());
			master.setStatus(dto.getStatus());
			master.setOrganisationId(dto.getOrganisationId());
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master=update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, master);

		} catch (HibernateException he) {
			logger.error("updateDepositMatrixConfig failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDepositMatrixConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDepositMatrixConfigStatus(int mDepositMatrixId, char status) throws ApplicationException {
		try {
		      Integer count = (Integer) sessionFactory.getCurrentSession()
		    		  						.// BR-batch2-org-trust fix (2026-08-03, live-proven): toggle was a write-by-ID GET
		      // (UPDATE keyed only by id, zero org/unit in WHERE -- org-1 session flipped
		      // org-999 victim A->I). Now scoped by the verified tenant.
		      getNamedQuery("UPDATE_DEPOSIT_MATRIX_STATUS")
		    	  		  			.setParameter("mDepositMatrixId", mDepositMatrixId)
		    	  		  			.setParameter("status", status)
		    	  		  			.setParameter("orgId", verifiedIdentityService.getVerifiedOrganizationId())
		    	  		  			.setParameter("unitId", verifiedIdentityService.getVerifiedUnitId())
		    	  		  			.executeUpdate();
		      if (count>0)
		      {
		        return new Response(SUCCESS, SUCCESS_CODE, COMMON_STATUS_SUCCESS, null,count);
		      } else {
		        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_STATUS_FAIL, null, BigInteger.ZERO);
		      }
		} catch (HibernateException he) {
			logger.error("updateDepositMatrixConfigStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDepositMatrixConfigStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDepositMatrixConfigCount(int orgId, int unitId) throws ApplicationException {
		try {
			Long count = (Long) sessionFactory.getCurrentSession()
		    		  						.getNamedQuery("GET_DEPOSIT_MATRIX_CONFIG_COUNT")
		    		  						.setParameter("orgId", orgId)
		    		  						.setParameter("unitId", unitId)
		    		  						.uniqueResult();
			 if (count>1) {
		        return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,
		        		count);
		      } else {
		        return new Response(ERROR, SUCCESS_CODE, SUCCESS_MESSAGE, null, BigInteger.ZERO);
		      }
		} catch (HibernateException he) {
			logger.error("getDepositMatrixConfigCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDepositMatrixConfigCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkDepositMatrixExist(DepositMatrixConfigMasterDto dto)
			throws ApplicationException {
		try {
		
		      Long existCount = (Long) sessionFactory.getCurrentSession()
		    		  						.getNamedQuery("GET_DEPOSIT_MATRIX_EXIST_OR_NOT")
		    		  						.setParameter("mDepositMatrixId", dto.getmDepositMatrixId()==null?0:dto.getmDepositMatrixId())
		    		  						.setParameter("orgId", dto.getOrganisationId())
		    		  						.setParameter("unitId", dto.getUnitId())
		    		  						.setParameter("patientTypeId", dto.getPatientTypeId())
		    		  						.setParameter("paymentEntitlementId", dto.getPaymentEntitlementId())
		    		  						.setParameter("bedCategoryId", dto.getBedCategoryId())
		    		  						.setParameter("againstDeposit", dto.getAgainstDeposit())
					/*
					 * .setBigDecimal("surgical",dto.getSurgical())
					 * .setBigDecimal("nonsurgical",dto.getNonSurgical())
					 */
		    		  						.uniqueResult();
			
		      if (existCount>1) {
		        return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,existCount);
		      } else {
		        return new Response(ERROR, SUCCESS_CODE, SUCCESS_MESSAGE, null, 0L);
		      }
		} catch (HibernateException he) {
			logger.error("checkDepositMatrixExist failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("checkDepositMatrixExist failed", e);
			throw new ServiceException(e.getMessage());
		}
   }

	@Override
	public Response getDepositMatrixConfigList(DepositMatrixReqDto depositMatrixReqDto) throws ApplicationException {
		try {
			
			String query = null;
			if(depositMatrixReqDto.getSergicalOrMedical()==1)
			{
				query = "select "
						+ "  matrix_config.m_deposit_matrix_id as \"mDepositMatrixId\", "
						+ "  bed_category_mst.billing_bed_desc as \"bedCategory\", "
						+ "  matrix_config.surgical as \"reservationDeposit\" "
						+ "from "
						+ "  billing.m_deposit_matrix_config matrix_config "
						+ "  inner join adt.m_billing_bed_category_master bed_category_mst on matrix_config.bed_category_id = bed_category_mst.billing_bed_category_id "
						+ "where "
						+ "  matrix_config.organisation_id = :organisationId "
						+ "  and matrix_config.unit_id = :unitId "
						+ "  and matrix_config.status = 'A' "
						+ "  and bed_category_mst.status = 'A' "
						+ "  and matrix_config.patient_type_id = :patientTypeId "
						+ "  and matrix_config.payment_entitlement_id = :paymentEntitlementId "
						+ "  and matrix_config.bed_category_id = :bedCategoryId "
						+ "  and matrix_config.against_deposit = :againstDeposit";
			}
			else if(depositMatrixReqDto.getSergicalOrMedical()==2)
			{
				query = "select "
						+ "  matrix_config.m_deposit_matrix_id as \"mDepositMatrixId\", "
						+ "  bed_category_mst.billing_bed_desc as \"bedCategory\", "
						+ "  matrix_config.non_surgical as \"reservationDeposit\" "
						+ "from "
						+ "  billing.m_deposit_matrix_config matrix_config "
						+ "  inner join adt.m_billing_bed_category_master bed_category_mst on matrix_config.bed_category_id = bed_category_mst.billing_bed_category_id "
						+ "where "
						+ "  matrix_config.organisation_id = :organisationId "
						+ "  and matrix_config.unit_id = :unitId "
						+ "  and matrix_config.status = 'A' "
						+ "  and bed_category_mst.status = 'A' "
						+ "  and matrix_config.patient_type_id = :patientTypeId "
						+ "  and matrix_config.payment_entitlement_id = :paymentEntitlementId "
						+ "  and matrix_config.bed_category_id = :bedCategoryId "
						+ "  and matrix_config.against_deposit = :againstDeposit";
			}
			List depositMatrixList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organisationId", depositMatrixReqDto.getOrgansationId())
					.setParameter("unitId", depositMatrixReqDto.getUnitId())
					.setParameter("patientTypeId", depositMatrixReqDto.getPatientTypeId())
					.setParameter("paymentEntitlementId", depositMatrixReqDto.getPaymentEntitlementId())
					.setParameter("bedCategoryId", depositMatrixReqDto.getBedCategoryId())
					.setParameter("againstDeposit", depositMatrixReqDto.getAgainstDeposit())
					.setResultTransformer(Transformers.aliasToBean(DepositMatrixResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, depositMatrixList, null);
		} catch (Exception e) {
			logger.error("getDepositMatrixConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}