package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.TSlideMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.histopathology.transaction.dto.TBlockMasterDto;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TSlideMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SlideCreationDaoImpl extends GenericDao<TSlideMaster, Integer>
		implements ISlideCreationDao, ICommonConstants, IError, ITransactionConstants {

	final static Logger logger = Logger.getLogger(SlideCreationDaoImpl.class);

	public SlideCreationDaoImpl() {
		super(TSlideMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	IRestainingRequestDetailsDao iRestainingRequestDetailsDao;

	@Override
	public Response getSlideCreationWorkList(SearchDto searchDto) throws ApplicationException {
		try {

			List<Integer>  subDeptIds = new ArrayList<Integer>();
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();
			List<Integer>  specimanTypes = new ArrayList<Integer>();

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				visitTypes = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				subDeptIds = searchDto.getSubDepts().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				testTypes = searchDto.getTestTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
				specimanTypes = searchDto.getSpecimanTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			Query sql = sessionFactory.getCurrentSession()
					.createQuery(getSlideCreationQuery(searchDto))
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("histoSampleStatus", BLOCK_CREATED)
					.setParameter("cytoSampleStatus", SPECIMAN_ACCEPTED);
			if (searchDto.getFromDt() != null){
				sql.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null){
				sql.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				sql.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				sql.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				sql.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				sql.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
				sql.setParameter("labSampleId", searchDto.getLabSampleId());
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				sql.setParameterList("specimanTypes", specimanTypes);
			}
				sql.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0);
					sql.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
			List<TSubSpecimanMasterDto> listTSubSpecimanMasterDto	= sql.setResultTransformer(Transformers.aliasToBean(TSubSpecimanMasterDto.class)).list();

			if (listTSubSpecimanMasterDto.isEmpty()) {
				return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, null, listTSubSpecimanMasterDto, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getSlideCreationWorkListCount(SearchDto searchDto) throws ApplicationException {
		try {
			List<Integer>  subDeptIds = new ArrayList<Integer>();
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();
			List<Integer>  specimanTypes = new ArrayList<Integer>();

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				visitTypes = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				subDeptIds = searchDto.getSubDepts().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				testTypes = searchDto.getTestTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
				specimanTypes = searchDto.getSpecimanTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			Query sql = sessionFactory.getCurrentSession()
					.createQuery(getSlideCreationQueryCount(searchDto))
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("histoSampleStatus", BLOCK_CREATED)
					.setParameter("cytoSampleStatus", SPECIMAN_ACCEPTED);
			if (searchDto.getFromDt() != null){
				sql.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null){
				sql.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				sql.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				sql.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				sql.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				sql.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
				sql.setParameter("labSampleId", searchDto.getLabSampleId());
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				sql.setParameterList("specimanTypes", specimanTypes);
			}

			Long subSpeciamanCount = (Long)	sql.uniqueResult();
			if (subSpeciamanCount != null) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, subSpeciamanCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getCreatedBlockList(TSubSpecimanMasterDto tSubSpecimanMasterDto) throws ApplicationException {
		try {
			List<TGrossMasterDto> listTGrossMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GROSS_LIST_FOR_SLIDE_CREATION")
					.setParameter("orgId", tSubSpecimanMasterDto.getOrgId())
					.setParameter("orgUnitId", tSubSpecimanMasterDto.getOrgUnitId())
					.setParameter("labSampleDtlsId", tSubSpecimanMasterDto.getLabSampleDtlsId())
					.setResultTransformer(Transformers.aliasToBean(TGrossMasterDto.class)).list();

			if (!listTGrossMasterDto.isEmpty()) {
				for (Iterator iterator = listTGrossMasterDto.iterator(); iterator.hasNext();) {
					TGrossMasterDto tGrossMasterDto = (TGrossMasterDto) iterator.next();
					List<TBlockMasterDto> listTBlockMasterDto = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_BLOCK_LIST_FOR_SLIDE_CREATION")
							.setParameter("orgId", tGrossMasterDto.getOrgId())
							.setParameter("orgUnitId", tGrossMasterDto.getOrgUnitId())
							.setParameter("tGrossId", tGrossMasterDto.gettGrossId())
							.setResultTransformer(Transformers.aliasToBean(TBlockMasterDto.class)).list();
					tGrossMasterDto.setListTBlockMaster(listTBlockMasterDto);
					if (!listTBlockMasterDto.isEmpty()) {
						for (Iterator iterator1 = listTBlockMasterDto.iterator(); iterator1.hasNext();) {
							TBlockMasterDto tBlockMasterDto = (TBlockMasterDto) iterator1.next();
							List<TSlideMasterDto> listTSlideMasterDto = sessionFactory.getCurrentSession()
									.getNamedQuery("GET_SLIDE_LIST_FOR_SLIDE_CREATION")
									.setParameter("orgId", tBlockMasterDto.getOrgId())
									.setParameter("orgUnitId", tBlockMasterDto.getOrgUnitId())
									.setParameter("tBlockId", tBlockMasterDto.gettBlockId())
									.setResultTransformer(Transformers.aliasToBean(TSlideMasterDto.class)).list();
							tBlockMasterDto.setListTSlideMaster(listTSlideMasterDto);
						}
					}
				}
				return new Response(SUCCESS, SUCCESS_200, null, listTGrossMasterDto, null);
			} else {

				return new Response(ERROR, ERR_500, "No Gross Found..", null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response createSlides(List<TGrossMasterDto> listTGrossMasterDto) throws ApplicationException {
		try {
			if (!listTGrossMasterDto.isEmpty()) {
				LabSampleDetailsMaster labSampleDetailsMaster = new LabSampleDetailsMaster();
				for (Iterator iterator = listTGrossMasterDto.iterator(); iterator.hasNext();) {
					TGrossMasterDto tGrossMasterDto = (TGrossMasterDto) iterator.next();
					for (Iterator iterator2 = tGrossMasterDto.getListTBlockMaster().iterator(); iterator2.hasNext();) {
						TBlockMasterDto tBlockMasterDto = (TBlockMasterDto) iterator2.next();
						for (Iterator iterator3 = tBlockMasterDto.getListTSlideMaster().iterator(); iterator3
								.hasNext();) {
							TSlideMasterDto tSlideMasterDto = (TSlideMasterDto) iterator3.next();
							TSlideMaster tSlideMaster = mapper.map(tSlideMasterDto, TSlideMaster.class,
									"TSlideMasterDtoToTSlideMaster");
							tSlideMaster.setLabSampleDtlsId(tBlockMasterDto.getLabSampleDtlsId());
							tSlideMaster.settBlockId(tBlockMasterDto.gettBlockId());
							tSlideMaster.settGrossId(tGrossMasterDto.gettGrossId());
							tSlideMaster.setCreatedDate(TimeZoneComponent.getDateByzone(tSlideMaster.getOrgUnitId()).getTime());
							tSlideMaster.setIsDeleted('N');
							tSlideMaster.settSubSpecimanId(tGrossMasterDto.gettSubSpecimanId());
							TSlideMaster tslideMst = save(tSlideMaster);
							labSampleDetailsMaster.setUpdatedBy(tslideMst.getCreatedBy());
							labSampleDetailsMaster.setUpdatedDate(tslideMst.getCreatedDate());
							labSampleDetailsMaster.setLabSampleDtlsId(tslideMst.getLabSampleDtlsId());
							labSampleDetailsMaster.setPerformedUnit(tslideMst.getOrgUnitId());
							labSampleDetailsMaster.setAssignUnit(tslideMst.getOrgUnitId());
							labSampleDetailsMaster.setOrgId(tslideMst.getOrgId());
							labSampleDetailsMaster.setOrgUnitId(tslideMst.getOrgUnitId());
							if (tSlideMasterDto.gettRestainingSubDetailId() != null
									&& tSlideMasterDto.gettRestainingSubDetailId() > 0) {
								iRestainingRequestDetailsDao.updateRestainSlideCreated(
										tSlideMasterDto.gettRestainingSubDetailId(), tSlideMasterDto.getOrgId(),
										tSlideMasterDto.getOrgUnitId());
							}
						}
					}
				}
				return new Response(SUCCESS, SUCCESS_200, SLIDES_CREATE_SUCCESS, null, labSampleDetailsMaster);
			} else {
				return new Response(ERROR, ERR_500, SLIDES_CREATE_FAIL, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			
			throw e;
		}
	}

	@Override
	public Response createSlidesCytology(List<TSlideMasterDto> listTSlideMaster) throws ApplicationException {
		try {
			if (!listTSlideMaster.isEmpty() && listTSlideMaster.size()>0) {
				LabSampleDetailsMaster labSampleDetailsMaster = new LabSampleDetailsMaster();
				for (Iterator iterator = listTSlideMaster.iterator(); iterator.hasNext();) {
					TSlideMasterDto tSlideMasterDto = (TSlideMasterDto) iterator.next();
					TSlideMaster tSlideMaster = mapper.map(tSlideMasterDto, TSlideMaster.class,
							"TSlideMasterDtoToTSlideMaster");
					tSlideMaster.setCreatedDate(TimeZoneComponent.getDateByzone(tSlideMasterDto.getOrgUnitId()).getTime());
					tSlideMaster.setIsDeleted('N');
					TSlideMaster tslideMst = save(tSlideMaster);
					labSampleDetailsMaster.setUpdatedBy(tslideMst.getCreatedBy());
					labSampleDetailsMaster.setUpdatedDate(tslideMst.getCreatedDate());
					labSampleDetailsMaster.setLabSampleDtlsId(tslideMst.getLabSampleDtlsId());
					labSampleDetailsMaster.setPerformedUnit(tslideMst.getOrgUnitId());
					labSampleDetailsMaster.setAssignUnit(tslideMst.getOrgUnitId());
					labSampleDetailsMaster.setOrgId(tslideMst.getOrgId());
					labSampleDetailsMaster.setOrgUnitId(tslideMst.getOrgUnitId());
					if (tSlideMasterDto.gettRestainingSubDetailId() != null
							&& tSlideMasterDto.gettRestainingSubDetailId() > 0) {
						iRestainingRequestDetailsDao.updateRestainSlideCreated(
								tSlideMasterDto.gettRestainingSubDetailId(), tSlideMasterDto.getOrgId(),
								tSlideMasterDto.getOrgUnitId());
					}
				}
				return new Response(SUCCESS, SUCCESS_200, SLIDES_CREATE_SUCCESS, null, labSampleDetailsMaster);
			} else {
				return new Response(ERROR, ERR_500, SLIDES_CREATE_FAIL, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			
			throw e;
		}
	}

	public String getSlideCreationQuery(SearchDto searchDto) {

		StringBuffer query = new StringBuffer();
		query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_SLIDE_CREATION_LIST").getQueryString());

		if (searchDto.getFromDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDt ");
		}
		if (searchDto.getToDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDt ");
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			query.append(" AND patientReg.patientId =:patientId ");
		}
		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
			query.append(" AND visitMst.visitTypeId IN (:visitTypes)");
		}
		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
			query.append(" AND testMst.deptId IN (:subDepts)");
		}
		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
			query.append(" AND testMst.testId in (:testTypes)");
		}
		if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0) {
			query.append(" AND labSampleMst.labSampleId =:labSampleId ");
		}
		if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
			query.append(" AND testMst.specimanId in (:specimanTypes)");
		}
		query.append(" AND ((labSampleDetailsMst.sampleStatusId = :cytoSampleStatus AND  testMst.isHistoCyto = 'C')  OR (labSampleDetailsMst.sampleStatusId = :histoSampleStatus AND  testMst.isHistoCyto = 'H'))");
		query.append(" order by labSampleDetailsMst.updatedDate");

		return query.toString();
	}

	public String getSlideCreationQueryCount(SearchDto searchDto) {

		StringBuffer query = new StringBuffer();
		query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_SLIDE_CREATION_LIST_COUNT").getQueryString());

		if (searchDto.getFromDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDt ");
		}
		if (searchDto.getToDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDt ");
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			query.append(" AND patientReg.patientId =:patientId ");
		}
		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
			query.append(" AND visitMst.visitTypeId IN (:visitTypes)");
		}
		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
			query.append(" AND testMst.deptId IN (:subDepts)");
		}
		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
			query.append(" AND testMst.testId in (:testTypes)");
		}
		if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0) {
			query.append(" AND labSampleMst.labSampleId =:labSampleId");
		}
		if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
			query.append(" AND testMst.specimanId in (:specimanTypes)");
		}
		query.append(" AND ((labSampleDetailsMst.sampleStatusId = :cytoSampleStatus AND  testMst.isHistoCyto = 'C')  OR (labSampleDetailsMst.sampleStatusId = :histoSampleStatus AND  testMst.isHistoCyto = 'H'))");
		return query.toString();
	}

	@Override
	public Response getSubSpecimanBySpecimanId(int tSpecimanId) throws ApplicationException {
		try{
			List<TSubSpecimanMasterDto> listTSubSpecimanMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIMAN_DETAIL")
					.setParameter("tSpecimanId", tSpecimanId)
					.setResultTransformer(Transformers.aliasToBean(TGrossMasterDto.class)).list();
			if (listTSubSpecimanMasterDto.isEmpty()) {
				return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_200, null, listTSubSpecimanMasterDto, null);
			}
			} catch (Exception e)
				{
			logger.error("Exection", e);
			throw e;
				}
		 }
	}
	

