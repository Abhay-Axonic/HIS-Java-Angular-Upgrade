package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.global.common.obj;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.RejectedSampleDto;
import com.param.lis.transaction.dto.SampleForInOutPatient;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LabCentralReceivingDaoImpl extends GenericDao<LabSampleDetailsMaster, Integer>
		implements ILabCentralReceivingDao, ICommonConstants, ITransactionConstants, IError {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private IPatientArrivalDao iPatientArrivalDao;

	public LabCentralReceivingDaoImpl() {
		super(LabSampleDetailsMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	final static Logger logger = Logger.getLogger(LabCentralReceivingDaoImpl.class);

	@Override
	public Response getSampleListForInOutPatient(Integer orgId, Integer orgUnitId, Integer visitTypeId, Integer offset,
			Integer recordPerPage, Character isSendToCrApplicable) throws ApplicationException {
		List<SampleForInOutPatient> listSampleForInOutPatient = null;
		try {
			Date date = TimeZoneComponent.getDateByzone(orgUnitId);
			if (visitTypeId ==  ICommonConstants.OP) {

				Integer sampleStatusId = (isSendToCrApplicable != null && isSendToCrApplicable == 'Y')
						? SAMPLE_SEND_TO_CR
						: SAMPLE_COLLECTED;
				String query = sessionFactory.getCurrentSession().getNamedQuery("COLLECT_SAMPLE_OUT_PATIENT")
						.getQueryString();
				StringBuffer sb = new StringBuffer(query);
				sb.append("  and lbsampledtls.assign_unit  =:orgUnitId " );
				sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') >= :date "
						+ "	 and to_char(order_master.created_date,'YYYY-MM-DD') <=:date");
				sb.append("  ORDER BY  " + "  lbsampledtls.priority_id,  " + "  order_master.created_date  ");
				listSampleForInOutPatient = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
						.setParameter("orgId", orgId)
						.setParameter("orgUnitId", orgUnitId)
						.setParameterList("sampleStatusIds", Arrays.asList(sampleStatusId))
						.setParameter("date", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
						.setParameterList("visitTypeIds", Arrays.asList(ICommonConstants.OP)).setFirstResult(offset != null ? offset : 0)
						.setMaxResults(recordPerPage != null ? recordPerPage : 10)
						.setResultTransformer(Transformers.aliasToBean(SampleForInOutPatient.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listSampleForInOutPatient, null);
			} else if (visitTypeId ==  ICommonConstants.IP) {

				String query = sessionFactory.getCurrentSession().getNamedQuery("COLLECT_SAMPLE_IN_PATIENT")
						.getQueryString();
				StringBuffer sb = new StringBuffer(query);
				sb.append("  and lbsampledtls.assign_unit  =:orgUnitId ");
				sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') >=:date "
						+ "	 and to_char(order_master.created_date,'YYYY-MM-DD') <=:date ");
				sb.append(
						"  ORDER BY  " + "  lbsampledtls.priority_id,  " + "  lbsampledtls.sample_sendtocr_datetime  ");
				listSampleForInOutPatient = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
						.setParameter("orgId", orgId).setParameter("orgUnitId", orgUnitId)
						.setParameterList("sampleStatusIds", COLLECTED_SAMPLE_IP)
						.setParameter("date", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
						.setParameterList("visitTypeIds", Arrays.asList( ICommonConstants.IP,  ICommonConstants.DC,  ICommonConstants.ER))
						.setFirstResult(offset != null ? offset : 0)
						.setMaxResults(recordPerPage != null ? recordPerPage : 10)
						.setResultTransformer(Transformers.aliasToBean(SampleForInOutPatient.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listSampleForInOutPatient, null);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getTotalRecordOutPatient(SearchDto searchDto) throws ApplicationException {
		try {
			Integer sampleStatusId = (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') ? SAMPLE_SEND_TO_CR
					: SAMPLE_COLLECTED;
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_COUNT_FOR_OUT_PATIENT")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
			
			if (searchDto.getFromDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= :fromDate ");

			if (searchDto.getToDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= :toDate ");
			
			Query sqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("visitTypeId", searchDto.getVisitTypeId())
					.setParameterList("sampleStatusIds", Arrays.asList(sampleStatusId))
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("assignUnit", searchDto.getAssignUnit());

			if(searchDto.getFromDt() !=null )
				sqlQuery.setParameter("fromDate", searchDto.getFromDt());

			if(searchDto.getToDt() !=null )
				sqlQuery.setParameter("toDate", searchDto.getToDt());


			BigInteger getTotalRecordForOutPatient = (BigInteger) sqlQuery.uniqueResult();
			if (getTotalRecordForOutPatient.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getTotalRecordForOutPatient);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getTotalRecordINPatient(SearchDto searchDto)
			throws ApplicationException {
		try {
			//Date date = TimeZoneComponent.getDateByzone(unitId);
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COUNT_FOR_IN_PATIENT")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
			/*sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') >= '"
					+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'"
					+ "	 and to_char(order_master.created_date,'YYYY-MM-DD') <='"
					+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'");
			*/
			
			if (searchDto.getFromDt() != null)
				sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') >= :fromDate ");

			if (searchDto.getToDt() != null)
				sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') <= :toDate ");
			

		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypeIds)");
			  }else if (searchDto.getVisitTypeId() != null && searchDto.getVisitTypeId() == ICommonConstants.OP) {
				sb.append(" AND visit_type_master.visit_type_id IN (1)");
				}else{
					sb.append(" AND visit_type_master.visit_type_id IN (2,3,4)");
				}
			Integer sampleStatusId = (searchDto.getIsSendToCrApplicable() != null
					&& searchDto.getIsSendToCrApplicable() == 'Y') ? SAMPLE_SEND_TO_CR : SAMPLE_COLLECTED;
			List<Integer>StatusId = new ArrayList<>();
			StatusId.add(sampleStatusId);
			Query sqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", searchDto.getOrgId())
				//	.setParameterList("visitTypeIds", Arrays.asList(IP, DC))
					.setParameterList("sampleStatusIds", StatusId)
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("assignUnit", searchDto.getAssignUnit());

			if(searchDto.getFromDt() !=null )
				sqlQuery.setParameter("fromDate", searchDto.getFromDt());

			if(searchDto.getToDt() !=null )
				sqlQuery.setParameter("toDate", searchDto.getToDt());

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				List<Integer> visitTypeIds = searchDto.getVisitTypes()
						.stream()
						.map(obj -> obj.getId()) // Replace `getId()` with the correct method
						.collect(Collectors.toList());
				sqlQuery.setParameterList("visitTypeIds", visitTypeIds);
			}

			BigInteger getTotalRecordForOutPatient = (BigInteger) sqlQuery.uniqueResult();
			if (getTotalRecordForOutPatient.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getTotalRecordForOutPatient);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response acceptOrRejectSample(List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto)
			throws ApplicationException {
		try {

			if (!listLabSampleDetailsMasterDto.isEmpty()) {
				Date date = TimeZoneComponent.getDateByzone(listLabSampleDetailsMasterDto.get(0).getOrgUnitId());
				LabSampleDetailsMaster lSampleDetailsMaster;
				for (Iterator iterator = listLabSampleDetailsMasterDto.iterator(); iterator.hasNext();) {
					LabSampleDetailsMasterDto labSampleDetailsMasterDto = (LabSampleDetailsMasterDto) iterator.next();
					LabSampleDetailsMaster labSampleDetailsMst = findById(
							labSampleDetailsMasterDto.getLabSampleDtlsId());
					if (labSampleDetailsMasterDto.getCurrStatus().equals(ACCEPT)) {
						labSampleDetailsMst.setSampleAcceptBy(labSampleDetailsMasterDto.getSampleAcceptBy());
						labSampleDetailsMst.setSamplePendingCount(labSampleDetailsMasterDto.getSamplePendingCount() > 0
								? (labSampleDetailsMasterDto.getSamplePendingCount() - 1)
								: 0);
						labSampleDetailsMst.setSampleAcceptDatetime(date.getTime());
						labSampleDetailsMst.setUpdatedBy(labSampleDetailsMasterDto.getUpdatedBy());
						labSampleDetailsMst.setUpdatedDate(date.getTime());
						labSampleDetailsMst.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
						if (labSampleDetailsMasterDto.getIsCentrifugationReq() == 'Y') {

							labSampleDetailsMst.setSampleStatusId(OP_IP_ACCEPTED);
							lSampleDetailsMaster = update(labSampleDetailsMst);

						} else {

							labSampleDetailsMst.setSampleStatusId(SAMPLE_SEND_TO_DEPT);
							lSampleDetailsMaster = update(labSampleDetailsMst);
						}

						if (lSampleDetailsMaster != null) {
							iPatientArrivalDao.patientNotArrivalData(lSampleDetailsMaster);
							continue;
						} else {
							return new Response(ERROR, ERR_500, SAMPLE_ACCEPT_FAIL, null, null);
						}
					} else if (labSampleDetailsMasterDto.getCurrStatus().equals(REJECT)) {

						labSampleDetailsMst
								.setSampleRejectReasonId(labSampleDetailsMasterDto.getSampleRejectReasonId());
						labSampleDetailsMst.setSampleRejectReason(labSampleDetailsMasterDto.getSampleRejectReason());
						labSampleDetailsMst.setSampleRejectBy(labSampleDetailsMasterDto.getSampleRejectBy());
						labSampleDetailsMst.setSampleRejectDatetime(date.getTime());
						labSampleDetailsMst.setSampleStatusId(OP_IP_REJECTED);
						labSampleDetailsMst.setUpdatedBy(labSampleDetailsMasterDto.getUpdatedBy());
						labSampleDetailsMst.setUpdatedDate(date.getTime());
						labSampleDetailsMst.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
						lSampleDetailsMaster = update(labSampleDetailsMst);
						if (lSampleDetailsMaster != null) {
							continue;
						} else {
							return new Response(ERROR, ERR_500, SAMPLE_ACCEPT_FAIL, null, null);
						}
					}
				}
				return new Response(SUCCESS, SUCCESS_200, SAMPLE_ACCEPT_SUCC, null, null);
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
		return null;

	}

	@Override
	public Response getCentrifugationWorklist(Integer orgId, Integer orgUnitId, Integer deptId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		List<SampleForInOutPatient> centrifugationWorkList = null;
		try {
			Date date = TimeZoneComponent.getDateByzone(orgUnitId);
			String query = sessionFactory.getCurrentSession().getNamedQuery("CENTRIFUGATION_WORK_LIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append("  and lbsampledtls.assign_unit  =:orgUnitId ");
			sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') >=:date "
					+ "	 and to_char(order_master.created_date,'YYYY-MM-DD') <=:date ");
			sb.append("  ORDER BY   order_master.created_date  ");

			centrifugationWorkList = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
					.setParameter("deptId", deptId)
					.setParameter("sampleStatusId", OP_IP_ACCEPTED)
					.setParameter("isCentrifugationReq", 'Y')
					.setParameter("date", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(SampleForInOutPatient.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, centrifugationWorkList, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getTotalcentrifugationWorkList(Integer orgId, Integer orgUnitId, Integer deptId)
			throws ApplicationException {
		try {
			
			Date date = TimeZoneComponent.getDateByzone(orgUnitId);
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_COUNT_CEONTRIFUGATION_WORKLIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append("  and lbsampledtls.assign_unit  =:orgUnitId ");
			sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') >=:date "
					+ "	 and to_char(order_master.created_date,'YYYY-MM-DD') <=:date ");

			BigInteger getTotalCentrifugationWorkCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", orgId)
					.setParameter("deptId", deptId)
					.setParameter("sampleStatusId", OP_IP_ACCEPTED)
					.setParameter("isCentrifugationReq", 'Y')
					.setParameter("orgUnitId", orgUnitId)
					.setParameter("date", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.uniqueResult();

			if (getTotalCentrifugationWorkCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getTotalCentrifugationWorkCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getRejectedSampleList(Integer orgId, Integer orgUnitId, Integer deptId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		List<RejectedSampleDto> listRejectedSampleDto = null;
		try {
			Date date = TimeZoneComponent.getDateByzone(orgUnitId);
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_REJECTED_SAMPLE_LIST").getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append("	 and to_char(order_mst.created_date,'YYYY-MM-DD') >=:date "
					+ "	 and to_char(order_mst.created_date,'YYYY-MM-DD') <=:date ");
			sb.append( "ORDER BY "
			+ "  lbsampledtls.priority_id, " 
			+ "  lbsampledtls.sample_sendtocr_datetime ");
			
			listRejectedSampleDto =   sessionFactory.getCurrentSession()
			        .createSQLQuery(sb.toString())
			        .setParameterList("sampleStatusIds", REJECTED_SAMPLE_IDS)
			    	.setParameter("orgId", orgId)
					.setParameter("deptId", deptId)
					.setParameter("orgUnitId", orgUnitId)
					.setParameter("date", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(RejectedSampleDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listRejectedSampleDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getTotalRecordRejectedSample(SearchDto searchDto)
			throws ApplicationException {
		try {
			//Date date = TimeZoneComponent.getDateByzone(unitId);
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_COUNT_FOR_REJECTED_SAMPLE").getQueryString();
			StringBuffer sb = new StringBuffer(query);
			/*sb.append("	 and to_char(order_mst.created_date,'YYYY-MM-DD') >= '"
					+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'"
					+ "	 and to_char(order_mst.created_date,'YYYY-MM-DD') <='"
					+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'");*/
			
			if (searchDto.getFromDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= :fromDate ");

			if (searchDto.getToDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= :toDate ");
			
			Query sqlQuery = sessionFactory.getCurrentSession()
			        .createSQLQuery(sb.toString())
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("deptId", searchDto.getDeptId())
					.setParameterList("sampleStatusIds", REJECTED_SAMPLE_IDS)
					.setParameter("orgUnitId", searchDto.getOrgUnitId());

			if (searchDto.getFromDt() != null)
				sqlQuery.setParameter("fromDate", searchDto.getFromDt());

			if (searchDto.getToDt() != null)
				sqlQuery.setParameter("toDate", searchDto.getToDt());

			BigInteger getTotalRecordForRejectedSample = (BigInteger) sqlQuery.uniqueResult();
			if (getTotalRecordForRejectedSample.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getTotalRecordForRejectedSample);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response sampleRecollect(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {

			Date date = TimeZoneComponent.getDateByzone(labSampleMasterDto.getOrgUnitId());
			LabSampleMaster labSampleMaster = mapper.map(labSampleMasterDto, LabSampleMaster.class,
					"LabSampleMasterDtoTOLabSampleMaster");
			int labSampleId = (int) sessionFactory.getCurrentSession().save(labSampleMaster);
			for (Iterator iterator = labSampleMasterDto.getListLabSampleDetailsMaster().iterator(); iterator
					.hasNext();) {
				LabSampleDetailsMasterDto labSampleDetailsMasterDto = (LabSampleDetailsMasterDto) iterator.next();
				LabSampleDetailsMaster labSampleDetailsMaster = mapper.map(labSampleDetailsMasterDto,
						LabSampleDetailsMaster.class, "LabSampleDetailsMasterDtoTOLabSampleDetailsMaster");
				// labSampleDetailsMaster.setSampleNo(labSampleMasterDto.getSampleNo());
				labSampleDetailsMaster.setLabSampleDtlsId(0);
				labSampleDetailsMaster.setLabSampleId(labSampleId);
				labSampleDetailsMaster.setSampleStatusId(SAMPLE_COLLECTED);
				labSampleDetailsMaster.setSampleRecollectFlag('Y');
				labSampleDetailsMaster.setCreatedDate(date.getTime());
				labSampleDetailsMaster.setSampleRecollectAgainstId(labSampleDetailsMasterDto.getLabSampleDtlsId());
				labSampleDetailsMaster.setGenderId(labSampleDetailsMasterDto.getGenderId());
				labSampleDetailsMaster.setSampleCollectionBy(labSampleDetailsMasterDto.getSampleCollectionBy());
				labSampleDetailsMaster.setIsAlliquoteReq(labSampleDetailsMasterDto.getIsAlliquoteReq());
				labSampleDetailsMaster.setSamplePendingCount(labSampleDetailsMaster.getSampleReqCount());
				labSampleDetailsMaster.setAssignUnit(labSampleDetailsMasterDto.getAssignUnit());
				labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getPerformedUnit());
				labSampleDetailsMaster.setTestId(labSampleDetailsMasterDto.getTestId());
				labSampleDetailsMaster.setPerformTestId(labSampleDetailsMasterDto.getPerformTestId());
				labSampleDetailsMaster.setSampleBarcode(labSampleDetailsMasterDto.getSampleBarcode());
				labSampleDetailsMaster.setUpdatedDate(date.getTime());
				LabSampleDetailsMaster LabSmpleDetailsMstr = save(labSampleDetailsMaster);
				if (LabSmpleDetailsMstr != null) {
					Integer sampleResult = sessionFactory.getCurrentSession()
							.getNamedQuery("UPDATE_SAMPLE_AFTER_RECOLLECTION").setParameter("sampleRecollectFlag", 'N')
							.setParameter("sampleStatusId", SAMPLE_RECOLLECTED)
							.setParameter("updatedBy", labSampleDetailsMasterDto.getUpdatedBy())
							.setParameter("updatedDate", date)
							.setParameter("labSampleDtlsId", labSampleDetailsMasterDto.getLabSampleDtlsId())
							.executeUpdate();
					if (sampleResult > 0) {
						continue;
					} else {
						return new Response(ERROR, ERR_500, SAMPLE_RECOLLECT_FAIL, null, null);
					}
				} else {
					return new Response(SUCCESS, SUCCESS_200, SAMPLE_RECOLLECT_FAIL, null, labSampleMaster);
				}
			}
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_RECOLLECT_SUCC, null, labSampleMaster);
		} catch (

		Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response sendToDepartMent(List<LabSampleDetailsMasterDto> listlabLabSampleDetailsMasterDto)
			throws ApplicationException {
		try {
		
			if (listlabLabSampleDetailsMasterDto != null) {
				Date date = TimeZoneComponent.getDateByzone(listlabLabSampleDetailsMasterDto.get(0).getOrgUnitId());
				for (Iterator iterator = listlabLabSampleDetailsMasterDto.iterator(); iterator.hasNext();) {

					LabSampleDetailsMasterDto labSampleDetailsMasterDto = (LabSampleDetailsMasterDto) iterator.next();
					LabSampleDetailsMaster labSampleDetailsMaster = findById(
							labSampleDetailsMasterDto.getLabSampleDtlsId());
					labSampleDetailsMaster.setCreatedBy(labSampleDetailsMasterDto.getCreatedBy());
					labSampleDetailsMaster.setCreatedDate(date.getTime());
					labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getUpdatedBy());
					labSampleDetailsMaster.setUpdatedDate(date.getTime());
					labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
					labSampleDetailsMaster.setSampleStatusId(SAMPLE_SEND_TO_DEPT);
					labSampleDetailsMaster.setSampleCentrifugationDatetime(
							labSampleDetailsMasterDto.getSampleCentrifugationDatetime());
					labSampleDetailsMaster
							.setSampleCentrifugationBy(labSampleDetailsMasterDto.getSampleCentrifugationBy());
					labSampleDetailsMaster.setSampWrkStatusId(labSampleDetailsMasterDto.getSampWrkStatusId());
					update(labSampleDetailsMaster);
				}
				return new Response(SUCCESS, SUCCESS_200, SAMPLE_SEND_TO_DEPT_SUCC, null, null);
			} else {
				return new Response(ERROR, ERR_500, SAMPLE_SEND_TO_DEPT_FAIL, null, null);
			}
		} catch (

		Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response samplCollection(LabSampleDetailsMaster labSampleDetailsMaster) throws ApplicationException {
		try {

			LabSampleDetailsMaster labSmpDetailsMst = save(labSampleDetailsMaster);
			if (labSmpDetailsMst != null) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, labSmpDetailsMst);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		} catch (

		Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getFilteredOutPatientList(SearchDto searchDto) throws ApplicationException {
		try {

			String query = null;
			List<Integer> sampleStatus = null;
			List<Integer> visitTypeIds = new ArrayList<Integer>();
			if (searchDto.getVisitTypeId() != null && searchDto.getVisitTypeId().intValue() ==  ICommonConstants.OP) {
				Integer sampleStatusId = (searchDto.getIsSendToCrApplicable() != null
						&& searchDto.getIsSendToCrApplicable() == 'Y') ? SAMPLE_SEND_TO_CR : SAMPLE_COLLECTED;
				query = sessionFactory.getCurrentSession().getNamedQuery("COLLECT_SAMPLE_OUT_PATIENT").getQueryString()
						.toString();
				sampleStatus = Arrays.asList(sampleStatusId);
				visitTypeIds = Arrays.asList(ICommonConstants.OP);
			}
			if (searchDto.getVisitTypeId() != null && (searchDto.getVisitTypeId().intValue() ==  ICommonConstants.IP
					|| searchDto.getVisitTypeId().intValue() ==  ICommonConstants.DC || searchDto.getVisitTypeId().intValue() ==  ICommonConstants.ER)) {
				query = sessionFactory.getCurrentSession().getNamedQuery("COLLECT_SAMPLE_IN_PATIENT").getQueryString()
						.toString();
				//sampleStatus = COLLECTED_SAMPLE_IP;
				Integer sampleStatusId = (searchDto.getIsSendToCrApplicable() != null
						&& searchDto.getIsSendToCrApplicable() == 'Y') ? SAMPLE_SEND_TO_CR : SAMPLE_COLLECTED;
				sampleStatus = Arrays.asList(sampleStatusId);
				//visitTypeIds = Arrays.asList(IP, DC, ER);
				if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				
					for (obj integer : searchDto.getVisitTypes()) {
						visitTypeIds.add(integer.getId());
					}
				}else {
					visitTypeIds = Arrays.asList(ICommonConstants.IP,ICommonConstants.DC,ICommonConstants.ER);
				}
			}

			StringBuilder sb = new StringBuilder(query);

			if (searchDto.getFromDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");

			if (searchDto.getToDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				sb.append(" AND pati_mst.patient_id =:patientId ");

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0)
				sb.append(" AND lbsampledtls.test_id IN ( :testTypes)");

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0)
				sb.append(" AND test_mst.dept_id IN ( :subDepts)");

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				sb.append("  and lbsampledtls.assign_unit =:assignUnit ");

			if(searchDto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%') ");
		      }
		      
		      if(searchDto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_no) LIKE concat('%',LOWER(:sampleNo),'%') ");
		      }
			
			sb.append("  ORDER BY  " + "  lbsampledtls.priority_id,  " + "  lbsampledtls.updated_date DESC ");

			Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

			if (searchDto.getFromDt() != null)
				createdQuery.setParameter("fromDt", searchDto.getFromDt());

			if (searchDto.getToDt() != null)
				createdQuery.setParameter("toDt", searchDto.getToDt());

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				createdQuery.setParameter("patientId", searchDto.getPatientId());

			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty())
				createdQuery.setParameterList("testTypes", searchDto.getTestTypes().stream() .map(obj::getId).collect(Collectors.toList()));

			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty())
				createdQuery.setParameterList("subDepts", searchDto.getSubDepts().stream() .map(obj::getId).collect(Collectors.toList()));

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());

			if(searchDto.getUhidNumber()!="")
		    	  createdQuery.setParameter("uhidNumber",  searchDto.getUhidNumber());

			if(searchDto.getSampleNo()!="")
		    	  createdQuery.setParameter("sampleNo",  searchDto.getSampleNo());

			List<SampleForInOutPatient> listSampleForInOutPatient = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameterList("sampleStatusIds", sampleStatus)
					.setParameterList("visitTypeIds", visitTypeIds)
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(SampleForInOutPatient.class)).list();

			if (listSampleForInOutPatient.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listSampleForInOutPatient, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listSampleForInOutPatient, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getFilteredCentrifugationWorkList(SearchDto searchDto) throws ApplicationException {
		try {

			String query = sessionFactory.getCurrentSession().getNamedQuery("CENTRIFUGATION_WORK_LIST").getQueryString()
					.toString();

			StringBuilder sb = new StringBuilder(query);

			if (searchDto.getFromDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");

			if (searchDto.getToDt() != null)
				sb.append("	 and to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				sb.append(" AND pati_mst.patient_id =:patientId ");

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
				sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0)
				sb.append(" AND lbsampledtls.test_id IN (:testTypes)");

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0)
				sb.append(" AND test_mst.dept_id IN ( :subDepts)");

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				sb.append("  and lbsampledtls.assign_unit =:assignUnit ");

			
			if(searchDto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%') ");
		      }
		      
		      if(searchDto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER(:sampleNo),'%') ");
		      }
		      
			sb.append("  ORDER BY  " + "  lbsampledtls.priority_id,  " + "  lbsampledtls.sample_accept_datetime  ");

			Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());


			if (searchDto.getFromDt() != null)
				createdQuery.setParameter("fromDt", searchDto.getFromDt());

			if (searchDto.getToDt() != null)
				createdQuery.setParameter("toDt", searchDto.getToDt());

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				createdQuery.setParameter("patientId", searchDto.getPatientId());

			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
				List<obj> testTypes = searchDto.getTestTypes();
				List<Integer> testTypeIds = testTypes.stream()
						.map(obj::getId)
						.collect(Collectors.toList());
				createdQuery.setParameterList("testTypes", testTypeIds);
			}

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				List<Integer> visitTypeIds = searchDto.getVisitTypes()
						.stream()
						.map(obj -> obj.getId()) // Replace `getId()` with the correct method
						.collect(Collectors.toList());
				createdQuery.setParameterList("visitTypes", visitTypeIds);
			}

			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
				List<obj> subDepts = searchDto.getSubDepts();
				List<Integer> subDeptIds = subDepts.stream()
						.map(obj::getId)
						.collect(Collectors.toList());
				createdQuery.setParameterList("subDepts", subDeptIds);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());

			if(searchDto.getUhidNumber()!="")
				createdQuery.setParameter("uhidNumber",  searchDto.getUhidNumber());

			if(searchDto.getSampleNo()!="")
				createdQuery.setParameter("sampleNo",  searchDto.getSampleNo());

			List<Integer> statusIds = new ArrayList<Integer>();
			if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusIds = Arrays.asList(OP_IP_ACCEPTED);
				
			}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
				
				statusIds = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
				
			}else {
				
				statusIds = Arrays.asList(SAMPLE_COLLECTED);
			}
			
			List<SampleForInOutPatient> listSampleForInOutPatient = createdQuery
					.setParameter("isCentrifugationReq", 'Y').setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameterList("sampleStatusId", statusIds)
					.setParameter("deptId", searchDto.getDeptId())
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(SampleForInOutPatient.class)).list();

			if (listSampleForInOutPatient.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listSampleForInOutPatient, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listSampleForInOutPatient, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getRejectedSampleListPatientSearch(SearchCommonDto searchDto) throws ApplicationException {
		
		 List<CommonAutoCompleteDto> patientList = null;
		    try {
		      patientList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
		          .getNamedQuery("LAB_PATIENT_SEARCH")
		          .setParameter("orgId", searchDto.getOrgId())
		         // .setInteger("orgUnitId", searchDto.getOrgUnitId())
		          .setParameter("searchKeyword",
		              "%" + searchDto.getSearchKeyword().trim().toLowerCase().trim() + "%")
		          .setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();
		      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

		    } catch (Exception e) {
		      logger.error("Exection", e);
		      throw e;
		    }
	}

	@Override
	public Response getRejectedSampleListSearch(SearchDto searchDto) throws ApplicationException {
		List<RejectedSampleDto> listRejectedSampleDto = null;
		try {
			
			Query query=sessionFactory.getCurrentSession().getNamedQuery("GET_REJECTED_SAMPLE_LIST");
			StringBuilder sb=new StringBuilder(query.getQueryString());
			 if (searchDto.getFromDt() != null ) {
		    	  sb.append(
		            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
		      }
		      if (searchDto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
		      }
		      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
		    	  sb.append(" AND pati_mst.patient_id =:patientId ");
		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
		    	  sb.append(" AND visit_mst.visit_type_id IN (:visitTypes)");

		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0)
		    	  sb.append(" AND lbsampledtls.sub_dept_id IN (:subDepts)");

		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0)
		    	  sb.append(" AND lbsampledtls.test_id  in (:testTypes)");

		      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
		          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");

		      sb.append( " ORDER BY "
		  			+ "  lbsampledtls.priority_id, " 
		  			+ "  lbsampledtls.sample_sendtocr_datetime ");
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());

			if (searchDto.getFromDt() != null)
				createdQuery.setParameter("fromDt", searchDto.getFromDt());

			if (searchDto.getToDt() != null)
				createdQuery.setParameter("toDt", searchDto.getToDt());

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			List<Integer> testTypes =new ArrayList<>();

			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
				testTypes = searchDto.getTestTypes()
						.stream()
						.map(obj -> obj.getId()) // Replace `getId()` with the correct method
						.collect(Collectors.toList());
				createdQuery.setParameterList("testTypes", testTypes);
			}
			List<Integer> visitTypeIds =new ArrayList<>();
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				visitTypeIds = searchDto.getVisitTypes()
						.stream()
						.map(obj -> obj.getId()) // Replace `getId()` with the correct method
						.collect(Collectors.toList());
				createdQuery.setParameterList("visitTypes", visitTypeIds);
			}
			List<Integer> subDepts =new ArrayList<>();
			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
				subDepts = searchDto.getSubDepts()
						.stream()
						.map(obj -> obj.getId()) // Replace `getId()` with the correct method
						.collect(Collectors.toList());
				createdQuery.setParameterList("subDepts", subDepts);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			
			  listRejectedSampleDto =
					  createdQuery
			  .setParameter("orgId", searchDto.getOrgId())
			  .setParameter("orgUnitId", searchDto.getOrgUnitId())
			  .setParameter("deptId", searchDto.getDeptId())
			  .setParameterList("sampleStatusIds", REJECTED_SAMPLE_IDS)
			  .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
			  .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
			  .setResultTransformer(Transformers.aliasToBean(RejectedSampleDto.class)).list();
			  return new Response(SUCCESS, SUCCESS_200, null, listRejectedSampleDto, null);
			 
		 
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getTotalcentrifugationWorkListCount(SearchDto searchDto) throws ApplicationException {
try {
			
		//	Date date = TimeZoneComponent.getDateByzone(unitId);
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_COUNT_CEONTRIFUGATION_WORKLIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append("  and lbsampledtls.assign_unit  =" + searchDto.getOrgUnitId());
			/*sb.append("	 and to_char(order_master.created_date,'YYYY-MM-DD') >= '"
					+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'"
					+ "	 and to_char(order_master.created_date,'YYYY-MM-DD') <='"
					+ GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd") + "'");*/
			
			if (searchDto.getFromDt() != null ) 
		      {
		    	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= :fromDt ");
		      }

		      if (searchDto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
		      }
		      
		      List<Integer> statusIds = new ArrayList<Integer>();
				if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusIds = Arrays.asList(OP_IP_ACCEPTED);
					
				}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
					
					statusIds = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
					
				}else {
					
					statusIds = Arrays.asList(SAMPLE_COLLECTED);
				}

			 Query createdQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("deptId", searchDto.getDeptId())
					.setParameterList("sampleStatusId", statusIds)
					.setParameter("isCentrifugationReq", 'Y')
					.setParameter("orgUnitId", searchDto.getOrgUnitId());

	if (searchDto.getFromDt() != null)
		createdQuery.setParameter("fromDt", searchDto.getFromDt());

	if (searchDto.getToDt() != null)
		createdQuery.setParameter("toDt", searchDto.getToDt());


	BigInteger getTotalCentrifugationWorkCount = (BigInteger) createdQuery.uniqueResult();

			if (getTotalCentrifugationWorkCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, getTotalCentrifugationWorkCount);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exception", e);

//			throw e;
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

}
