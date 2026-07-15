package com.param.global.unit.dao;

import java.util.ArrayList;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.ReportMasterDetails;
import com.param.global.unit.dto.MetaDataDetailsDto;
import com.param.global.unit.dto.ReportDetailsDto;
import com.param.global.unit.dto.ReportGroupHeaderMasterDto;
import com.param.opd.coversheet.dto.GeneralExaminationAppoMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class ReportDetailsDaoImpl extends GenericDao<ReportMasterDetails, Integer>
		implements IReportDetailsDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(ReportDetailsDaoImpl.class);

	public ReportDetailsDaoImpl() {
		super(ReportMasterDetails.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getReportDetailsList(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			List<ReportDetailsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_REPORT_DETAILS_LIST")
					.setParameter("organizationId", reportDetailsDto.getOrganizationId())
					.setParameter("reportGroupHeaderId", reportDetailsDto.getReportGroupHeaderId())
					.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getReportDetailsList failed", he);
		} catch (Exception e) {
			logger.error("getReportDetailsList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReportDetailsByReportId(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			Integer unitId = reportDetailsDto.getUnitId();
			Integer userId = reportDetailsDto.getUserId();
			List<ReportDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REPORT_DETAILS_BY_REPORT_ID")
					.setParameter("reportId", reportDetailsDto.getReportId())
					.setParameter("organizationId", reportDetailsDto.getOrganizationId())
					// .setParameter("unitId", reportDetailsDto.getUnitId()!=null?
					// reportDetailsDto.getUnitId():0)
					.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();

			ListIterator<ReportDetailsDto> iterator = list.listIterator();
			while (iterator.hasNext()) {
				ReportDetailsDto detailsDto = iterator.next();

				if (detailsDto.getViewName() != null) {
					
					if(detailsDto.getControlName().equals("user")){
						
						List<MetaDataDetailsDto> dataDetailsDtos = sessionFactory.getCurrentSession()
								.createSQLQuery(detailsDto.getViewName())
								.setParameter("orgId", reportDetailsDto.getOrganizationId()).setParameter("unitId", unitId).setParameter("userId",userId)
								.setResultTransformer(Transformers.aliasToBean(MetaDataDetailsDto.class)).list();
						detailsDto.setMetaDataDetailsDtosList(dataDetailsDtos);
						
					}else{
						List<MetaDataDetailsDto> dataDetailsDtos = sessionFactory.getCurrentSession()
								.createSQLQuery(detailsDto.getViewName())
								.setParameter("orgId", reportDetailsDto.getOrganizationId()).setParameter("unitId", unitId)
								.setResultTransformer(Transformers.aliasToBean(MetaDataDetailsDto.class)).list();
						detailsDto.setMetaDataDetailsDtosList(dataDetailsDtos);
					}
					
					
				}

			}

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getReportDetailsByReportId failed", he);
		} catch (Exception e) {
			logger.error("getReportDetailsByReportId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReportGroupHeaderMaster(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			List<ReportDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REPORT_GROUP_HEADER_MASTER_LIST")
					.setParameter("organizationId", reportDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getReportGroupHeaderMaster failed", he);
		} catch (Exception e) {
			logger.error("getReportGroupHeaderMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReportGroupHeaderForUserRoleManagement(ReportDetailsDto reportDetailsDto)
			throws ApplicationException {
		try {/*
				 * // List<ReportDetailsDto> list2=new ArrayList<>(); List<ReportDetailsDto>
				 * list=sessionFactory.getCurrentSession()
				 * .getNamedQuery("GET_REPORT_GROUP_HEADER_MASTER_LIST")
				 * .setParameter("organizationId", reportDetailsDto.getOrganizationId())
				 * .setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list(
				 * );
				 * 
				 * for(ReportDetailsDto dto : list) {
				 * 
				 * List<ReportDetailsDto> list1=sessionFactory.getCurrentSession()
				 * .getNamedQuery("GET_REPORT_DETAILS_LIST") .setParameter("organizationId",
				 * reportDetailsDto.getOrganizationId())
				 * .setParameter("reportGroupHeaderId",dto.getReportGroupHeaderId())
				 * .setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list(
				 * );
				 * 
				 * dto.setListReportDetails(list1); //list.add(dto); }
				 */

			String query = "select " + "	rptmst.report_id as \"reportId\", "
					+ "	rptmst.reportgroupheader_id as \"reportGroupHeaderId\", "
					+ "	mst.reportgroupheader_name as \"reportGroupHeaderName\", "
					+ "	rptmst.report_name as \"reportName\" " + " from " + "	m_reportgroupheader_master mst "
					+ " inner join public.m_reports_master rptmst on "
					+ "	mst.reportgroupheader_id = rptmst.reportgroupheader_id " + " where "
					+ "	rptmst.organization_id = :orgId " + " and mst.status='A' " + "	and rptmst.status='A' "
					+ " order by " + "	rptmst.reportgroupheader_id, " + "	rptmst.report_id asc";

			List<ReportDetailsDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", reportDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();

			Integer prevId = null;
			List<ReportDetailsDto> reportDetailsDtoList = new ArrayList<>();
			List<ReportDetailsDto> subReportList = null;
			ReportDetailsDto newObj = null;
			int i = 1;
			for (ReportDetailsDto dto : list) {

				if (prevId == null || !(prevId.equals(dto.getReportGroupHeaderId()))) {
					if (newObj != null) {
						newObj.setListReportDetails(subReportList);
						reportDetailsDtoList.add(newObj);
					}
					newObj = new ReportDetailsDto();
					newObj.setReportGroupHeaderId(dto.getReportGroupHeaderId());
					newObj.setReportGroupHeaderName(dto.getReportGroupHeaderName());
					subReportList = new ArrayList<>();

					ReportDetailsDto newObj1 = new ReportDetailsDto();
					newObj1.setReportGroupHeaderId(dto.getReportGroupHeaderId());
					newObj1.setReportId(dto.getReportId());
					newObj1.setReportName(dto.getReportName());
					subReportList.add(newObj1);

					if (i == list.size()) {
						newObj.setListReportDetails(subReportList);
						reportDetailsDtoList.add(newObj);
					}
					// Adding array to the json object

					prevId = dto.getReportGroupHeaderId();
				} else {

					ReportDetailsDto newObj1 = new ReportDetailsDto();
					newObj1.setReportGroupHeaderId(dto.getReportGroupHeaderId());
					newObj1.setReportId(dto.getReportId());
					newObj1.setReportName(dto.getReportName());
					subReportList.add(newObj1);

					if (i == list.size()) {
						newObj.setListReportDetails(subReportList);
						reportDetailsDtoList.add(newObj);
					}

				}
				i++;
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, reportDetailsDtoList, null);
		} catch (HibernateException he) {
			logger.error("getReportGroupHeaderForUserRoleManagement failed", he);
		} catch (Exception e) {
			logger.error("getReportGroupHeaderForUserRoleManagement failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMappedMISReportIdsForUser(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			/*
			 * String query ="SELECT distinct(um.user_id) as \"userId\", " +
			 * " um.mis_report_ids as \"misReportIds\" " +
			 * " FROM public.t_user_role_mapper um " +
			 * " WHERE um.organisation_id=:organizationId  " + " AND um.user_id=:userId";
			 * 
			 * List<ReportDetailsDto> list =
			 * sessionFactory.getCurrentSession().createSQLQuery(query)
			 * .setParameter("organizationId", reportDetailsDto.getOrganizationId())
			 * .setParameter("userId", reportDetailsDto.getUserId())
			 * .setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list(
			 * );
			 */

			String query = "SELECT distinct(um.user_id) as \"userId\", " + " um.mis_report_ids as \"misReportIds\" "
					+ " FROM public.t_user_unit_mapper um " + " WHERE um.unit_id=:unitId  " + " AND um.user_id=:userId "
					+ " and status ='A'";
			// update t_user_unit_mapper set mis_report_ids =:array where user_id =:userId
			// and unit_id=:unitId and status ='A'
			List<ReportDetailsDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", reportDetailsDto.getUnitId())
					.setParameter("userId", reportDetailsDto.getUserId())
					.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getMappedMISReportIdsForUser failed", he);
		} catch (Exception e) {
			logger.error("getMappedMISReportIdsForUser failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMISReportFormattesMasterforShowReports(ReportDetailsDto reportDetailsDto)
			throws ApplicationException {
		try {
			String query = "select mrf.id as \"id\"," + "mrf.description as \"description\","
					+ "mrf.file_extension as \"fileExtension\", " + "mrf.format as \"format\" "
					+ "from public.m_mis_report_format mrf"
					+ " ORDER BY mrf.description ASC ";
			List<ReportDetailsDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getMISReportFormattesMasterforShowReports failed", he);
		} catch (Exception e) {
			logger.error("getMISReportFormattesMasterforShowReports failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchMISReport(ReportDetailsDto reportDetailsDto) throws ApplicationException {
		try {
			List<ReportDetailsDto> list2=new ArrayList<>();
			List<ReportDetailsDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery(" SELECT rghm.reportgroupheader_id as \"reportGroupHeaderId\", rghm.reportgroupheader_name as \"reportName\" " + 
						" from public.m_reportgroupheader_master rghm  " + 
						" WHERE rghm.organization_id = :organizationId  " + 
						" and LOWER(rghm.reportgroupheader_name)  like '%"+reportDetailsDto.getSearchString()+"%' " + 
						" order by rghm.reportgroupheader_name  asc ")
				.setParameter("organizationId", reportDetailsDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();
			
			
					List<ReportDetailsDto> list1=sessionFactory.getCurrentSession()
							.createSQLQuery("select " + 
									"	distinct rm.report_id as \"reportId\", " + 
									"	rm.report_name as \"reportName\", " + 
									"	rm.action as \"action\", "+
									"	rm.reportgroupheader_id as \"reportGroupHeaderId\" " + 
									" from " + 
									"	public.m_reports_details rd " + 
									" left join public.m_reports_master rm on " + 
									"	rd.report_id = rm.report_id " + 
									" where " + 
									"	rd.organization_id =:organizationId " + 
									"  and LOWER(rm.report_name)  like '%"+reportDetailsDto.getSearchString()+"%' " + 
									" order by  rm.report_name asc")
							.setParameter("organizationId", reportDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(ReportDetailsDto.class)).list();
			if(list!=null && list.size() > 0) {
				list2.addAll(list);
			}
			if (list1!=null && list1.size() > 0) {
				list2.addAll(list1);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list2, null);
		} catch (HibernateException he) {
			logger.error("searchMISReport failed", he);
		} catch (Exception e) {
			logger.error("searchMISReport failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



}
