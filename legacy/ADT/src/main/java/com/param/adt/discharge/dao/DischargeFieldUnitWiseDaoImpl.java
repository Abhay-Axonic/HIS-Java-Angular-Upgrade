package com.param.adt.discharge.dao;

import java.util.List;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.adt.discharge.model.DischargeSummaryTemplateMaster;
import com.param.global.billing.dto.RequestDiscountDto;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.adt.discharge.model.DischargeFieldUnitWise;
import com.param.adt.discharge.model.DischargeFieldUnitWiseDTO;
import com.param.adt.discharge.model.DischargeSummaryFieldMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeFieldUnitWiseDaoImpl extends GenericDao<DischargeFieldUnitWise, Integer>
		implements IDischargeFieldUnitWiseDao, ICommonConstants {

	public DischargeFieldUnitWiseDaoImpl() {
		super(DischargeFieldUnitWise.class);
	}

	@Override
	public Response saveDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {
			DischargeFieldUnitWise dischargeFieldUnitWise = new DischargeFieldUnitWise();

			dischargeFieldUnitWise.setDischargeFieldId(dischargeFieldUnitWiseDTO.getDischargeFieldId());
			dischargeFieldUnitWise.setDischargeChangedName(dischargeFieldUnitWiseDTO.getDischargeChangedName());
			dischargeFieldUnitWise.setCreatedBy(dischargeFieldUnitWiseDTO.getCreatedBy());
			dischargeFieldUnitWise.setCreatedDate(
					GlobalCommonDateUtils.getDate(dischargeFieldUnitWiseDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeFieldUnitWise.setUpdatedBy(dischargeFieldUnitWiseDTO.getUpdatedBy());
			dischargeFieldUnitWise.setUpdatedDate(
					GlobalCommonDateUtils.getDate(dischargeFieldUnitWiseDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeFieldUnitWise.setStatus(dischargeFieldUnitWiseDTO.getStatus());
			dischargeFieldUnitWise.setUnitId(dischargeFieldUnitWiseDTO.getUnitId());
			dischargeFieldUnitWise.setOrganizationId(dischargeFieldUnitWiseDTO.getOrganizationId());
			save(dischargeFieldUnitWise);

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dischargeFieldUnitWise);

		} catch (HibernateException he) {
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeFieldUnitWiseByUnitwisetidNotDischargeid(
			DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {
			List<DischargeFieldUnitWiseDTO> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_FIELD_UNIT_WISE_BY_UNITWISEID")
					.setParameter("dischargeFieldUnitWiseId", dischargeFieldUnitWiseDTO.getDischargeFieldUnitWiseId())
					.setParameter("dischargeFieldId", dischargeFieldUnitWiseDTO.getDischargeFieldId())
					.setResultTransformer(Transformers.aliasToBean(DischargeFieldUnitWiseDTO.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {

			DischargeFieldUnitWise dischargeFieldUnitWise = findById(
					dischargeFieldUnitWiseDTO.getDischargeFieldUnitWiseId());
			dischargeFieldUnitWise.setDischargeFieldId(dischargeFieldUnitWiseDTO.getDischargeFieldId());
			dischargeFieldUnitWise.setDischargeChangedName(dischargeFieldUnitWiseDTO.getDischargeChangedName());
			dischargeFieldUnitWise.setUpdatedBy(dischargeFieldUnitWiseDTO.getUpdatedBy());
			dischargeFieldUnitWise.setUpdatedDate(
					GlobalCommonDateUtils.getDate(dischargeFieldUnitWiseDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(dischargeFieldUnitWise);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dischargeFieldUnitWise);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeFieldUnitWiseStatus(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {
			DischargeFieldUnitWise dischargeFieldUnitWise = findById(
					dischargeFieldUnitWiseDTO.getDischargeFieldUnitWiseId());

			dischargeFieldUnitWise.setStatus(dischargeFieldUnitWiseDTO.getStatus());
			dischargeFieldUnitWise.setUpdatedBy(dischargeFieldUnitWiseDTO.getUpdatedBy());
			dischargeFieldUnitWise.setUpdatedDate(
					GlobalCommonDateUtils.getDate(dischargeFieldUnitWiseDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(dischargeFieldUnitWise);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		} catch (Exception e) {

		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeFieldUnitWiseById(Integer dischargeFieldUnitWiseId) {
		try {
			
//			Object object = findById(dischargeFieldUnitWiseId);
			Object object = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_FIELD_UNIT_WISE_BY_ID")
					.setParameter("dischargeFieldUnitWiseId", dischargeFieldUnitWiseId)
					.setResultTransformer(Transformers.aliasToBean(DischargeFieldUnitWiseDTO.class)).uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, object);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getDischargeFieldUnitWiseMasterList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {
		try {

			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_DISCHARGE_FIELD_UNIT_WISE_LIST").getQueryString());

			if(dischargeFieldUnitWiseDTO.getSearchKeyword()!=null && !dischargeFieldUnitWiseDTO.getSearchKeyword().equals("") && !dischargeFieldUnitWiseDTO.getSearchKeyword().isEmpty()) {
				query.append(" AND ( "
						+ " lower(dischargeSummaryFieldMaster.discharge_field_desc) like :searchKeyword "
						+ " OR lower(dischargeFieldUnitWise.discharge_changed_name) like :searchKeyword "
						+ " ) ");
			}

			query.append(" ORDER BY dischargeFieldUnitWise.discharge_field_unit_wise_id");

			Query  createQuery =sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("organizationId", dischargeFieldUnitWiseDTO.getOrganizationId())
					.setParameter("unitId", dischargeFieldUnitWiseDTO.getUnitId())
					.setFirstResult(dischargeFieldUnitWiseDTO.getOffset() != null ? dischargeFieldUnitWiseDTO.getOffset() : 0)
					.setMaxResults(dischargeFieldUnitWiseDTO.getNoOfRecordsPerPage() != null ? dischargeFieldUnitWiseDTO.getNoOfRecordsPerPage() : 10);
			if(dischargeFieldUnitWiseDTO.getSearchKeyword()!=null && !dischargeFieldUnitWiseDTO.getSearchKeyword().equals("") && !dischargeFieldUnitWiseDTO.getSearchKeyword().isEmpty()) {
				createQuery.setParameter("searchKeyword", "%" + dischargeFieldUnitWiseDTO.getSearchKeyword().toLowerCase() + "%");
			}
			List<DischargeFieldUnitWiseDTO> list=createQuery.setResultTransformer(Transformers.aliasToBean(DischargeFieldUnitWiseDTO.class))
					.list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public Response getDischargeFieldUnitWiseCount(Integer organizationId, Integer unitId,  String searchKeyword) {
		try {

			Session session = sessionFactory.getCurrentSession();
			StringBuilder queryCount=new StringBuilder(session.getNamedQuery("GET_DISCHARGE_FIELD_UNIT_WISE_COUNT").getQueryString());

			if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

				queryCount.append(" AND ( "
						+ " lower(dischargeSummaryFieldMaster.discharge_field_desc) like :searchKeyword "
						+ " OR lower(dischargeFieldUnitWise.discharge_changed_name) like :searchKeyword "
						+ " ) ");
			}

			Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(queryCount.toString())
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId);
			if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

				createQuery.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");
			}
			Object count=createQuery.uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getActiveDischargeFieldUnitWise(Integer organizationId, Integer unitId) {
		try {
			List<DischargeFieldUnitWiseDTO> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DISCHARGE_FIELD_UNIT_WISE")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DischargeFieldUnitWiseDTO.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkDischargeFeildIdAlradyExist(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {
		try {

            String qry = "SELECT discharge_field_id AS \"dischargeFieldId\" " +
                    "FROM public.m_discharge_field_unit_wise " +
                    "WHERE discharge_field_id = :dischargeFieldId " +
                    "AND organization_id = :organizationId " +
                    "AND unit_id =:unitId "+
                    "AND status = 'A'";
			StringBuffer sb = new StringBuffer(qry);


			List<DischargeFieldUnitWiseDTO> list = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("dischargeFieldId", dischargeFieldUnitWiseDTO.getDischargeFieldId())
					.setParameter("organizationId", dischargeFieldUnitWiseDTO.getOrganizationId())
					.setParameter("unitId", dischargeFieldUnitWiseDTO.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeFieldUnitWiseDTO.class)).list();


			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {

			throw e;
		}
	}

	@Override
	public Response getDischargeFieldUnitWiseList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {
		try {
//			List<DischargeFieldUnitWise> list = findAll();
			 List<DischargeFieldUnitWiseDTO> list=sessionFactory.getCurrentSession()
			 .getNamedQuery("GET_ACTIVE_DISCHARGE_FIELD_UNIT_WISE_TEMPLATE")
			 .setParameter("organizationId", dischargeFieldUnitWiseDTO.getOrganizationId())
			 .setParameter("unitId", dischargeFieldUnitWiseDTO.getUnitId())
			 .setResultTransformer(Transformers.aliasToBean(DischargeFieldUnitWiseDTO.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
