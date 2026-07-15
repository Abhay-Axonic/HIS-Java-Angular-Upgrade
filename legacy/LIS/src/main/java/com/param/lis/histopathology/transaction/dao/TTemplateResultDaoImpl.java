 package com.param.lis.histopathology.transaction.dao;

import java.util.List;

import javax.persistence.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.TTemplateResult;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.TTemplateResultDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TMicroExaDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import scala.tools.nsc.typechecker.Macros.Success;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class TTemplateResultDaoImpl extends GenericDao<TTemplateResult, Integer>
implements ITTemplateResultDao, ICommonConstants, IError,ITransactionConstants
{
	
	final static Logger logger = Logger.getLogger(TTemplateResultDaoImpl.class);
	
	public TTemplateResultDaoImpl() 
	{
		super(TTemplateResult.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	private String SUCCESS_CODE;

	@Override
	public Response saveTemplateResult(TTemplateResultDto tTemplateResultDto) throws ApplicationException {
		try
		{
				if(tTemplateResultDto.getTemplateResId()!=null&&tTemplateResultDto.getTemplateResId()!=0) {
					
					TTemplateResult tTemplateRes = mapper.map(tTemplateResultDto, TTemplateResult.class,
							"TTemplateResultDtoToTTemplateResult");
					tTemplateRes.setTgrossid(tTemplateResultDto.gettGrossId());
					tTemplateRes.setGrossCaptureNote(tTemplateResultDto.getGrossDesc());
					tTemplateRes.setTblockid(tTemplateResultDto.gettBlockId());
					tTemplateRes.setBlockCaptureNote(tTemplateResultDto.getBlockDesc());
					tTemplateRes.setTslideid(tTemplateResultDto.gettSlideId());
					tTemplateRes.setMicroCaptureNote(tTemplateResultDto.getSlideDesc1());
					tTemplateRes.setMicroImpression(tTemplateResultDto.getImpressionDesc());
					tTemplateRes.setLabSampleDtlsId(tTemplateResultDto.getLabSampleDtlsId());
					tTemplateRes.setUpdatedDate(TimeZoneComponent.getDateByzone(tTemplateResultDto.getOrgUnitId()).getTime());
					tTemplateRes.setReportReleaseBy((tTemplateResultDto.getReportReleaseBy()!=null && tTemplateResultDto.getReportReleaseBy()>0)?tTemplateResultDto.getReportReleaseBy():tTemplateResultDto.getCreatedBy());
					tTemplateRes.setSampleStatusId(tTemplateResultDto.getSampleStatusId());
					TTemplateResult tTemplateResultData = update(tTemplateRes);
					if (tTemplateResultData != null)
					{
						return new Response(SUCCESS, SUCCESS_200, "Template Result Update Successfully.", null, null);
						
					} else
					{
						return new Response(ERROR, ERR_500, "Failed to Update Template Result.", null, null);
					}
					
					
				}else {
					
					TTemplateResult tTemplateRes = mapper.map(tTemplateResultDto, TTemplateResult.class,
							"TTemplateResultDtoToTTemplateResult");
					if (tTemplateRes != null) {
					tTemplateRes.setTgrossid(tTemplateResultDto.gettGrossId());
					tTemplateRes.setGrossCaptureNote(tTemplateResultDto.getGrossDesc());
					tTemplateRes.setTblockid(tTemplateResultDto.gettBlockId());
					tTemplateRes.setBlockCaptureNote(tTemplateResultDto.getBlockDesc());
					tTemplateRes.setTslideid(tTemplateResultDto.gettSlideId());
					tTemplateRes.setMicroCaptureNote(tTemplateResultDto.getSlideDesc1());
//					tTemplateRes.setMicroCaptureNote(tTemplateResultDto.getSlideCaptureNote());
					tTemplateRes.setMicroImpression(tTemplateResultDto.getImpressionDesc());
					tTemplateRes.setLabSampleDtlsId(tTemplateResultDto.getLabSampleDtlsId());
					tTemplateRes.setCreatedDate(TimeZoneComponent.getDateByzone(tTemplateResultDto.getOrgUnitId()).getTime());
					tTemplateRes.setReportReleaseBy((tTemplateResultDto.getReportReleaseBy()!=null && tTemplateResultDto.getReportReleaseBy()>0)?tTemplateResultDto.getReportReleaseBy():tTemplateResultDto.getCreatedBy());
					tTemplateRes.setSampleStatusId(tTemplateResultDto.getSampleStatusId());
					TTemplateResult tTemplateResultData = save(tTemplateRes);
					if (tTemplateResultData != null)
					{
						return new Response(SUCCESS, SUCCESS_200, "Template Result Added Successfully.", null, null);
						
					}
					}else
					{
						return new Response(ERROR, ERR_500, "Failed to Added Template Result.", null, null);
					}
					
				}
			
		}catch(HibernateException er){
			logger.error("Exection", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exection", e);throw e;
		}
		return new Response(ERROR, ERR_500, "Failed to Added Template Result.", null, null);
	}



  @Override
  public Response getHistopathReport(Integer templateResId, Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try
    {
      TTemplateResult tTemplateRes = findById(templateResId); 
      TTemplateResultDto tTemplateResultDto = 
          mapper.map(tTemplateRes, TTemplateResultDto.class,
          "TTemplateResultDtoToTTemplateResult");
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, tTemplateResultDto);

    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
  }


 @Transactional
@Override
public Response<TTemplateResult, Integer> updateTemplateResult(TTemplateResultDto tTemplateResultDto) {
	try{
			String queryString = "UPDATE lab.t_gross_master " + "SET gross_desc =:grossDesc "
					+ "WHERE t_gross_id =:tGrossId";

			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryString);
			query.setParameter("grossDesc", tTemplateResultDto.getGrossDesc());
			query.setParameter("tGrossId", tTemplateResultDto.gettGrossId());
			query.executeUpdate();


			String querys1 = "update lab.t_block_master " + "set capture_note =:blockDesc"
					+ " where t_block_id =:tBlockId ";

			SQLQuery query1 = sessionFactory.getCurrentSession().createSQLQuery(querys1);
			query1.setParameter("blockDesc", tTemplateResultDto.getBlockDesc());
			query1.setParameter("tBlockId", tTemplateResultDto.gettBlockId());
			query1.executeUpdate();


			String query2 = "UPDATE lab.t_micro_exa_details " + " SET capture_note =:slideDesc1, "
					+ "impression =:impressionDesc, " + " is_nad =:isNad "
					+ " where t_micro_exa_id =:tMicroExaId";
			SQLQuery query3 = sessionFactory.getCurrentSession().createSQLQuery(query2);
			query3.setParameter("slideDesc1", tTemplateResultDto.getSlideDesc1());
			query3.setParameter("impressionDesc", tTemplateResultDto.getImpressionDesc());
			query3.setParameter("isNad", tTemplateResultDto.getIsNad());
			query3.setParameter("tMicroExaId", tTemplateResultDto.gettMicroExaId());
			query3.executeUpdate();	 		
		
	} catch (Exception e){
	 logger.error("Exection", e);
    throw e;
	}
	return new Response(SUCCESS, SUCCESS_200, null, null);
}



@Override
public Response getHistopathReportNo(TTemplateResultDto tTemplateResultDto) {
try{

	Boolean isExists = (Boolean)sessionFactory.getCurrentSession()
			.getNamedQuery("GET_REPORT_NAME_BY_ID")
			.setParameter("reportNo",tTemplateResultDto.getReportNo().toLowerCase())
			.setParameter("orgUnitId", tTemplateResultDto.getOrgUnitId())
			.setParameter("orgId",tTemplateResultDto.getOrgId())
			.uniqueResult();
	
	return new Response(SUCCESS, SUCCESS_CODE, "Success..", null, isExists);
} catch (HibernateException he) {
	logger.error("Exection", he);
	throw new ServiceException(he.getMessage());
} catch (Exception e) {
	logger.error("Exection", e);
	throw new ServiceException(e.getMessage());
}
}


@Override
public Response getHistopathReportDetails(Integer labSampleDtlsId,Integer orgId, Integer orgUnitId) throws ApplicationException {
	try
	{

		String query= "SELECT template_res_id AS \"templateResId\", "
				+ "report_no AS \"reportNo\", "
        		+ "ttr.lab_sample_dtls_id AS \"labSampleDtlsId\", "
        		+ "ttr.t_sub_speciman_id AS \"tSubSpecimanId\", "
        		+ "tssm.sub_speciman_name AS \"subSpecimanName\", "
        		+ "ttr.t_gross_id AS \"tGrossId\", "
        		+ "ttr.gross_capture_note AS \"grossDesc\", "
        		+ "tgm.gross_no as \"grossNo\","
        		+ "ttr.t_block_id AS \"tBlockId\", "
        		+ "ttr.block_capture_note AS \"blockDesc\", "
        		+ "tbm.block_no as \"blockNo\", "
        		+ "ttr.t_slide_id AS \"tSlideId\", "
        		+ "tsm2.slide_no as \"slideNo\", "
        		+ "ttr.micro_capture_note AS \"slideDesc1\", "
        		+ "ttr.micro_impression AS \"impressionDesc\" "
        		+ "FROM lab.t_template_result ttr "
        		+ "left join lab.t_sub_speciman_master tssm on "
        		+ "tssm.t_sub_speciman_id  = ttr.t_sub_speciman_id "
        		+ "inner join lab.t_gross_master tgm on "
        		+ "tgm.t_gross_id  = ttr.t_gross_id "
        		+ "inner join lab.t_block_master tbm on "
        		+ "tbm.t_block_id = ttr.t_block_id "
        		+ "left join lab.t_slide_master tsm2 on "
        		+ "tsm2.t_slide_id  = ttr.t_slide_id "
        		+ "where ttr.org_id =:orgId "
        		+ "and ttr.org_unit_id =:orgUnitId "
        		+ "and ttr.is_deleted = 'N' "
        		+ "and ttr.lab_sample_dtls_id =:labSampleDtlsId "
        		+ "order by ttr.created_date desc limit 1 " ;

	List<TTemplateResultDto> tTemplateResult = sessionFactory.getCurrentSession()
			.createSQLQuery(query)
			.setParameter("orgId", orgId)
			.setParameter("orgUnitId", orgUnitId)
			.setParameter("labSampleDtlsId", labSampleDtlsId)
			.setResultTransformer(Transformers.aliasToBean(TTemplateResultDto.class)).list();
	if(tTemplateResult.isEmpty())
	{
		return new Response(ERROR, ERR_500, "No Records Found..", null, null);
	}else {
		return new Response(SUCCESS, SUCCESS_200, null, tTemplateResult, null);
	}
	
	}catch (Exception e)
	{
	logger.error("Exection", e);
	throw e;
	}
}

}
