package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.TRestainingRequestDetailsMaster;
import com.param.entity.lis.histo.TRestainingRequestMaster;
import com.param.lis.common.dto.CommonDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.histopathology.transaction.dto.RestainReqDto;
import com.param.lis.histopathology.transaction.dto.TBlockMasterDto;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TRestainingRequestDetailsMasterDto;
import com.param.lis.histopathology.transaction.dto.TRestainingRequestMasterDto;
import com.param.lis.histopathology.transaction.dto.TSlideMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class RestainRequestDaoImpl  extends GenericDao<TRestainingRequestMaster, Integer>
implements IRestainRequestDao, ICommonConstants, IError,ITransactionConstants{

  public RestainRequestDaoImpl() {
    super(TRestainingRequestMaster.class);
  }
  
  @Autowired
  private DozerBeanMapper mapper;
  
  @Autowired
  private IRestainingRequestDetailsDao tRestainingRequestDetailsDao;
  
  final static Logger logger = Logger.getLogger(RestainRequestDaoImpl.class);

  @Override
  public Response getSlidesDetails(TSubSpecimanMasterDto tSubSpecimanMasterDto)
      throws ApplicationException {
    try 
    
    { 
    	if(tSubSpecimanMasterDto.getIsHistoCyto()!=null  && tSubSpecimanMasterDto.getIsHistoCyto()=='H')
    	{
    		 List<TGrossMasterDto> listTGrossMasterDto =
    	               sessionFactory.getCurrentSession().getNamedQuery("GET_GROSS_LIST_FOR_RESTAINING")
    	              .setParameter("orgId", tSubSpecimanMasterDto.getOrgId())
    	              .setParameter("orgUnitId", tSubSpecimanMasterDto.getOrgUnitId())
    	              .setParameter("labSampleDtlsId", tSubSpecimanMasterDto.getLabSampleDtlsId())
    	              .setResultTransformer(Transformers.aliasToBean(TGrossMasterDto.class)).list();

    	      if (!listTGrossMasterDto.isEmpty())
    	      {
    	        for (Iterator iterator = listTGrossMasterDto.iterator(); iterator.hasNext();) 
    	        {
    	          List<TRestainingRequestDetailsMasterDto> listTRestainingRequestDetailsMasterDto = new ArrayList<>();
    	          TGrossMasterDto tGrossMasterDto = (TGrossMasterDto) iterator.next();
    	          List<TRestainingRequestDetailsMasterDto> tRestainingRequestDetailsMstDto =
    	              sessionFactory.getCurrentSession().getNamedQuery("GET_BLOCK_LIST_FOR_RESTAINING")
    	             .setParameter("orgId", tGrossMasterDto.getOrgId())
    	             .setParameter("orgUnitId", tGrossMasterDto.getOrgUnitId())
    	             .setParameter("tGrossId", tGrossMasterDto.gettGrossId())
    	             .setResultTransformer(Transformers.aliasToBean(TRestainingRequestDetailsMasterDto.class)).list();
    	         
    	          if (!tRestainingRequestDetailsMstDto.isEmpty())
    	          {
    	            for (Iterator iterator1 = tRestainingRequestDetailsMstDto.iterator(); iterator1.hasNext();) 
    	            {
    	              TRestainingRequestDetailsMasterDto tRestainingRequestDetailsMasterDto = (TRestainingRequestDetailsMasterDto) iterator1.next();
    	              List<CommonDto> listCommonDto =
    	                  sessionFactory.getCurrentSession().getNamedQuery("GET_SLIDE_LIST_FOR_RESTAINING")
    	                 .setParameter("orgId", tRestainingRequestDetailsMasterDto.getOrgId())
    	                 .setParameter("orgUnitId", tRestainingRequestDetailsMasterDto.getOrgUnitId())
    	                 .setParameter("tBlockId", tRestainingRequestDetailsMasterDto.gettBlockId())
    	                 .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
    	              tRestainingRequestDetailsMasterDto.setListSlidesNo(listCommonDto);
    	              listTRestainingRequestDetailsMasterDto.add(tRestainingRequestDetailsMasterDto);
    	            }
    	          }
    	          tGrossMasterDto.setListTRestainingRequestDetailsMasterDto(listTRestainingRequestDetailsMasterDto);
    	        }
    	}
     
        return new Response(SUCCESS, SUCCESS_200, null, listTGrossMasterDto, null);
      } else if(tSubSpecimanMasterDto.getIsHistoCyto()!=null  && tSubSpecimanMasterDto.getIsHistoCyto()=='C')
      {
    	  List<TGrossMasterDto> grossList = new ArrayList<>();
    	  List<TRestainingRequestDetailsMasterDto> restinReqDetails = new ArrayList<>();
    	  
    	  TRestainingRequestDetailsMasterDto dto = new TRestainingRequestDetailsMasterDto();
    	  
          List<CommonDto> listCommonDto =
              sessionFactory.getCurrentSession().getNamedQuery("GET_SLIDE_LIST_FOR_RESTAINING_FOR_CYTO")
              .setParameter("orgId", tSubSpecimanMasterDto.getOrgId())
              .setParameter("orgUnitId", tSubSpecimanMasterDto.getOrgUnitId())
              .setParameter("tSubSpecimanId", tSubSpecimanMasterDto.gettSubSpecimanId())
             .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
          dto.setListSlidesNo(listCommonDto);
          restinReqDetails.add(dto);
          TGrossMasterDto grossDto = new TGrossMasterDto();
          grossDto.setListTRestainingRequestDetailsMasterDto(restinReqDetails);
        	grossList.add(grossDto);
          return new Response(SUCCESS, SUCCESS_200, null, grossList, null);
        }
  

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
	return null;
  }

  @Override
  public Response saveRestainRequest(TRestainingRequestMasterDto tRestainingRequestMasterDto)
      throws ApplicationException 
  {
    try {
          if(tRestainingRequestMasterDto!=null)
          {
            tRestainingRequestMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(tRestainingRequestMasterDto.getOrgUnitId()).getTime());
            TRestainingRequestMaster tRestainingRequestMaster = mapper.map(tRestainingRequestMasterDto, TRestainingRequestMaster.class,"TRestainingRequestMasterDtoToTRestainingRequestMaster");
            TRestainingRequestMaster tRestainingRequestMst =  save(tRestainingRequestMaster);
            for (Iterator iterator = tRestainingRequestMst.getListTRestainingRequestDetailsMaster().iterator(); iterator
                .hasNext();) {
              TRestainingRequestDetailsMaster tRestainingRequestDetailsMaster = (TRestainingRequestDetailsMaster) iterator.next();
              tRestainingRequestDetailsMaster.setLabSampleDtlsId(tRestainingRequestMst.getLabSampleDtlsId());
              tRestainingRequestDetailsMaster.settRestainingReqId(tRestainingRequestMst.gettRestainingReqId());
              tRestainingRequestDetailsMaster.setIsDeleted('N');
              tRestainingRequestDetailsDao.saveRestainingRequestDetails(tRestainingRequestDetailsMaster);
            }
              return new Response(SUCCESS, SUCCESS_200, RESTAIN_REQUEST_SAVE_SUCCESS, null, null);
          }
          else {
            return new Response(SUCCESS, SUCCESS_200, RESTAIN_REQUEST_SAVE_FAIL, null, null);
          }
  } catch (Exception e) {
    logger.error("Exection", e);
    throw e;
  }
  }

  @Override
  public Response getRestainRequestWorkList(SearchDto searchDto)
      throws ApplicationException {
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
    	    .createQuery(getRestainRequestWorkListQuery(searchDto))
          .setParameter("orgId", searchDto.getOrgId())
          .setParameter("orgUnitId", searchDto.getOrgUnitId());
		if (searchDto.getFromDt() != null){
			sql.setParameter("fromDt", searchDto.getFromDt());
		}
		if (searchDto.getToDt() != null){
			sql.setParameter("toDt", searchDto.getToDt());
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
			sql.setParameter("patientId", searchDto.getPatientId());
		}
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()){
			sql.setParameterList("visitTypes", visitTypes);
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
			sql.setParameterList("subDepts", subDeptIds);
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
			sql.setParameterList("testTypes",testTypes  );
		}
		if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
			sql.setParameter("labSampleId", searchDto.getLabSampleId());
		}
		if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()){
			sql.setParameterList("specimanTypes", specimanTypes);
		}
		 		sql.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0);
				 sql.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
		List<TRestainingRequestMasterDto> listTRestainingRequestMasterDto = sql.setResultTransformer(Transformers.aliasToBean(TRestainingRequestMasterDto.class)).list();

      if (listTRestainingRequestMasterDto.isEmpty()) {
        return new Response(SUCCESS, SUCCESS_200, "No Records Found.", null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listTRestainingRequestMasterDto, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getRestainRequestWorkListCount(SearchDto searchDto)
      throws ApplicationException {
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

     Query sql = sessionFactory.getCurrentSession().createQuery(getRestainRequestWorkListQueryCount(searchDto))
              .setParameter("orgId", searchDto.getOrgId())
              .setParameter("orgUnitId", searchDto.getOrgUnitId());
		if (searchDto.getFromDt() != null){
			sql.setParameter("fromDt", searchDto.getFromDt());
		}
		if (searchDto.getToDt() != null){
			sql.setParameter("toDt", searchDto.getToDt());
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
			sql.setParameter("patientId", searchDto.getPatientId());
		}
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()){
			sql.setParameterList("visitTypes", visitTypes);
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
			sql.setParameterList("subDepts", subDeptIds);
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
			sql.setParameterList("testTypes", testTypes);
		}
		if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
			sql.setParameter("labSampleId", searchDto.getLabSampleId());
		}
		if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()){
			sql.setParameterList("specimanTypes", specimanTypes);
		}
		Long restainWorkListCount =  (Long) sql.uniqueResult();
      if (restainWorkListCount != null) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, restainWorkListCount);
      } else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getRestainRequestWorkListDetails(Integer tRestainingReqId, Integer orgId,
      Integer orgUnitId) throws ApplicationException {
    try {
      List<TRestainingRequestDetailsMasterDto> listAgainstSlide = sessionFactory.getCurrentSession()
          .getNamedQuery("RESTAIN_REUEST_DETAILS_AGAINST_SLIDE")
          .setParameter("orgId", orgId)
          .setParameter("orgUnitId", orgUnitId)
          .setParameter("tRestainingReqId", tRestainingReqId)
          .setResultTransformer(Transformers.aliasToBean(TRestainingRequestDetailsMasterDto.class)).list();
      
     List<TRestainingRequestDetailsMasterDto> listAgainstNewSlide = sessionFactory.getCurrentSession()
          .getNamedQuery("RESTAIN_REUEST_DETAILS_AGAINST_NEW_SLIDE")
          .setParameter("orgId",orgId)
          .setParameter("orgUnitId", orgUnitId)
          .setParameter("tRestainingReqId", tRestainingReqId)
          .setResultTransformer(Transformers.aliasToBean(TRestainingRequestDetailsMasterDto.class)).list();

     if(!listAgainstSlide.isEmpty()||!listAgainstNewSlide.isEmpty()) 
     {
       listAgainstSlide.addAll(listAgainstNewSlide);
        List<TRestainingRequestDetailsMasterDto> filteredList = listAgainstSlide
                                                  .stream()
                                                  .sorted(Comparator.comparing(TRestainingRequestDetailsMasterDto::gettRestainingDetailId))
                                                  .collect(Collectors.toList());
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, filteredList, null);    
     }
     else 
     {
       return new Response(SUCCESS, SUCCESS_200, "No Record Found..", null, null);    
     }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getRestainRequestWorkListSlides(RestainReqDto dto) throws ApplicationException 
  {
		try {
			if (dto.getIsHistoCyto() != null && dto.getIsHistoCyto() == 'H') {
				List<TGrossMasterDto> listTGrossMasterDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_GROSS_LIST_FOR_RE_SLIDE_CREATION").setParameter("orgId", dto.getOrgId())
						.setParameter("orgUnitId", dto.getOrgUnitId())
						.setParameter("tRestainingDetailId", dto.gettRestainingDetailId())
						.setResultTransformer(Transformers.aliasToBean(TGrossMasterDto.class)).list();

				if (listTGrossMasterDto != null && !listTGrossMasterDto.isEmpty()) {
					for (Iterator iterator = listTGrossMasterDto.iterator(); iterator.hasNext();) {
						TGrossMasterDto tGrossMasterDto = (TGrossMasterDto) iterator.next();
						List<TBlockMasterDto> listTBlockMasterDto = sessionFactory.getCurrentSession()
								.getNamedQuery("GET_BLOCK_LIST_FOR_RE_SLIDE_CREATION")
								.setParameter("orgId", tGrossMasterDto.getOrgId())
								.setParameter("orgUnitId", tGrossMasterDto.getOrgUnitId())
								.setParameter("tRestainingDetailId", dto.gettRestainingDetailId())
								.setResultTransformer(Transformers.aliasToBean(TBlockMasterDto.class)).list();
						tGrossMasterDto.setListTBlockMaster(listTBlockMasterDto);
						if (!listTBlockMasterDto.isEmpty()) {
							for (Iterator iterator1 = listTBlockMasterDto.iterator(); iterator1.hasNext();) {
								TBlockMasterDto tBlockMasterDto = (TBlockMasterDto) iterator1.next();
								List<TSlideMasterDto> listTSlideMasterDto = sessionFactory.getCurrentSession()
										.getNamedQuery("GET_SLIDE_LIST_FOR_RE_SLIDE_CREATION")
										.setParameter("orgId", tBlockMasterDto.getOrgId())
										.setParameter("orgUnitId", tBlockMasterDto.getOrgUnitId())
										.setParameter("tRestainingDetailId", dto.gettRestainingDetailId())
										.setParameter("tRestainingSubDetailId", dto.gettRestainingSubDetailId())
										.setParameter("isNew", dto.getIsNew())
										.setResultTransformer(Transformers.aliasToBean(TSlideMasterDto.class)).list();
								tBlockMasterDto.setListTSlideMaster(listTSlideMasterDto);
							}
						}
					}
					return new Response(SUCCESS, SUCCESS_200, null, listTGrossMasterDto, null);
				}

			} else if (dto.getIsHistoCyto() != null && dto.getIsHistoCyto() == 'C') {

				List<TSlideMasterDto> list = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SLIDE_LIST_FOR_RE_SLIDE_CREATION")
						.setParameter("orgId", dto.getOrgId())
						.setParameter("orgUnitId", dto.getOrgUnitId())
						.setParameter("tRestainingDetailId", dto.gettRestainingDetailId())
						.setParameter("tRestainingSubDetailId", dto.gettRestainingSubDetailId())
						.setParameter("isNew", dto.getIsNew())
						.setResultTransformer(Transformers.aliasToBean(TSlideMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, list, null);
			}
			return new Response(ERROR, ERR_500, "No Gross Found..", null, null);
    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getMaxSlideNumber(RestainReqDto dto)
      throws ApplicationException {
    try 
    {
    	StringBuffer sb = new StringBuffer(sessionFactory.getCurrentSession().getNamedQuery("GET_MAX_SLIDE_NUMBER").getQueryString());
		if (dto.gettBlockId() != null && dto.gettBlockId() > 0) {
			sb.append(" AND tSlideMaster.tBlockId = :tBlockId");
		}
		if (dto.gettSubSpecimanId()!=null && dto.gettSubSpecimanId()>0) {
			sb.append(" AND tSlideMaster.tSubSpecimanId = :subSpecimanId");
		}
      Query sql = sessionFactory.getCurrentSession().createQuery(sb.toString())
              .setParameter("orgId", dto.getOrgId())
              .setParameter("orgUnitId",dto.getOrgUnitId());
		if (dto.gettBlockId() != null && dto.gettBlockId() > 0) {
			 sql.setParameter("tBlockId",dto.gettBlockId());
		}
		if (dto.gettSubSpecimanId()!=null && dto.gettSubSpecimanId()>0) {
			 sql.setParameter("subSpecimanId",dto.gettSubSpecimanId());
		}
		BigInteger maxLideNumber = (BigInteger) sql.uniqueResult();
      if (maxLideNumber != null)
      {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, maxLideNumber);
      } else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  public String getRestainRequestWorkListQuery(SearchDto searchDto) {

		StringBuffer query = new StringBuffer();
		query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_RESTAIN_WORKLIST").getQueryString());

		if (searchDto.getFromDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDt ");
		}
		if (searchDto.getToDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDt ");
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			query.append(" AND patientReg.patientId =:patientId  ");
		}
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			query.append(" AND visitMst.visitTypeId IN (:visitTypes)");
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			query.append(" AND testMst.deptId IN (:subDepts)");
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			query.append(" AND testMst.testId in (:testTypes)");
		}
		if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0) {
			query.append(" AND labSampleMst.labSampleId =:labSampleId ");
		}
		if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()) {
			query.append(" AND testMst.specimanId in (:specimanTypes)");
		}
		query.append(" order by tRestainingRequestMaster.tRestainingReqId ");

		return query.toString();
	}

	public String getRestainRequestWorkListQueryCount(SearchDto searchDto) {

		StringBuffer query = new StringBuffer();
		query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_RESTAIN_WORKLIST_COUNT").getQueryString());

		if (searchDto.getFromDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDt ");
		}
		if (searchDto.getToDt() != null) {
			query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDt ");
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			query.append(" AND patientReg.patientId =:patientId  ");
		}
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			query.append(" AND visitMst.visitTypeId IN (:visitTypes)");
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			query.append(" AND testMst.deptId IN (:subDepts)");
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			query.append(" AND testMst.testId in (:testTypes)");
		}
		if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0) {
			query.append(" AND labSampleMst.labSampleId =:labSampleId  ");
		}
		if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()) {
			query.append(" AND testMst.specimanId in (:specimanTypes)");
		}
		return query.toString();
	}

}
