package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.OutSourceDetailMaster;
import com.param.entity.lis.histo.OutSourceMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.histopathology.transaction.dto.OutSourceDetailMasterDto;
import com.param.lis.histopathology.transaction.dto.OutSourceMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class OutSourcePendingListForSampleDispatchDaoImpl extends GenericDao<OutSourceMaster, Integer>
implements IOutSourcePendingListForSampleDispatchDao, ICommonConstants, IError,ITransactionConstants
{
	
	final static Logger logger = Logger.getLogger(OutSourcePendingListForSampleDispatchDaoImpl.class);
	
	public OutSourcePendingListForSampleDispatchDaoImpl() 
	{
		super(OutSourceMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private IOutSourceDetailsDao iOutSourceDetailsDao;
	
	/*@Autowired
	private SessionFactory sessionFactory;*/
	
	@Override
	public Response outSourcePendingListForSampleDispatch(OutSourceMasterDto outSourceMasterDto) throws ApplicationException {
		try
		{
			
			Date date = TimeZoneComponent.getDateByzone(outSourceMasterDto.getOrgUnitId());
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PENDING_OUT_SOURCE_SAMPLE_DISPATCH_LIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append(" AND to_char(lab_sample_dtls.updated_date,'YYYY-MM-DD') >= :startDate"
					+ " AND to_char(lab_sample_dtls.updated_date,'YYYY-MM-DD') <= :endDate");
			 if (outSourceMasterDto.getAssignUnit() != null && outSourceMasterDto.getAssignUnit() > 0)
		          sb.append("  and lab_sample_dtls.assign_unit  =:assignUnit ");
			 
			 if (outSourceMasterDto.getUhid()!= null && outSourceMasterDto.getUhid() != "")
				 sb.append( " AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:uhid),'%') ");
			 
			sb.append("   ORDER BY lab_sample_dtls.updated_date  ");
			Query sql = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", outSourceMasterDto.getOrgId())
					.setParameter("orgUnitId", outSourceMasterDto.getOrgUnitId())
					.setParameter("sampleStatusId", outSourceMasterDto.getSampleStatusId())
					.setParameter("startDate",  GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameter("endDate",  GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"));
			if (outSourceMasterDto.getAssignUnit() != null && outSourceMasterDto.getAssignUnit() > 0) {
				sql.setParameter("assignUnit", outSourceMasterDto.getAssignUnit());
			}
			if (outSourceMasterDto.getUhid()!= null && outSourceMasterDto.getUhid() != ""){
				sql.setParameter("uhid", outSourceMasterDto.getUhid());
			}
			sql.setFirstResult(outSourceMasterDto.getOffset() != null ? outSourceMasterDto.getOffset() : 0);
			sql.setMaxResults(outSourceMasterDto.getRecordPerPage() != null ? outSourceMasterDto.getRecordPerPage() : 50);
			List<OutSourceMasterDto> listOutSourceMasterDto = sql.setResultTransformer(Transformers.aliasToBean(OutSourceMasterDto.class)).list();
			
			if(listOutSourceMasterDto.isEmpty())
			{
				return new Response(ERROR, ERR_500, "No Out Source Records Found.", null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listOutSourceMasterDto, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getoutSourcePendingListForSampleDispatchCount(OutSourceMasterDto outSourceMasterDto) throws ApplicationException {
		try
		{
			Date date = TimeZoneComponent.getDateByzone(outSourceMasterDto.getOrgUnitId());
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PENDING_OUT_SOURCE_SAMPLE_DISPATCH_COUNT")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append("	 and to_char(lab_sample_dtls.updated_date,'YYYY-MM-DD') >=:startDate"
					+ "	 and to_char(lab_sample_dtls.updated_date,'YYYY-MM-DD') <=:endDate");
			 if (outSourceMasterDto.getAssignUnit() != null && outSourceMasterDto.getAssignUnit() > 0)
		          sb.append("  and lab_sample_dtls.assign_unit  =:assignUnit");
			 
			Query sql = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", outSourceMasterDto.getOrgId())
					.setParameter("orgUnitId", outSourceMasterDto.getOrgUnitId())
					.setParameter("startDate",  GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameter("endDate",  GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameterList("sampleStatusIds", Arrays.asList(outSourceMasterDto.getSampleStatusId()))
					.setParameter("startDate",  GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameter("endDate",  GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"));
			if (outSourceMasterDto.getAssignUnit() != null && outSourceMasterDto.getAssignUnit() > 0){
				sql.setParameter("assignUnit", outSourceMasterDto.getAssignUnit());
			}
			BigInteger outSourceMasterCount =	(BigInteger) sql.uniqueResult();
			if (outSourceMasterCount!=null)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, outSourceMasterCount);
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response saveOutSourceDetails(OutSourceMasterDto outSourceMasterDto)
			throws ApplicationException 
	{
		try
		{
		 outSourceMasterDto.setSampleStatus(2);
		 OutSourceMaster outSourceMst = mapper.map(outSourceMasterDto, OutSourceMaster.class,"OutSourceMasterDtoToOutSourceMaster");
		 OutSourceMaster tSpecimenMasterObj = update(outSourceMst);
		  if(!outSourceMasterDto.getListOutSourceDetailMasterDto().isEmpty())
			  {		
			  OutSourceDetailMaster master = null;
				  for (Iterator iterator = outSourceMasterDto.getListOutSourceDetailMasterDto().iterator(); iterator.hasNext();) 
				  {
					  OutSourceDetailMasterDto outSourceDetailMasterDto = (OutSourceDetailMasterDto) iterator.next();
					  master = new OutSourceDetailMaster();
					  master.setCourierNumber(outSourceDetailMasterDto.getCourierNumber());
					  master.setRemarks(outSourceDetailMasterDto.getRemarks());
					  master.setOrgId(outSourceDetailMasterDto.getOrgId());
					  master.setOrgUnitId(outSourceDetailMasterDto.getOrgUnitId());
					  master.setStatus(outSourceDetailMasterDto.getStatus());
					  master.setExpectedDate(outSourceDetailMasterDto.getExpectedDate());
					  master.setResource(outSourceDetailMasterDto.getResource());
					  master.setOutSourcedId(tSpecimenMasterObj.getOutSourcedId());
					  master.setSampleSendThroughId(outSourceDetailMasterDto.getSampleSendThroughId());
					  master.setUpdatedBy(outSourceDetailMasterDto.getUpdatedBy());
					  master.setUpdatedDate(outSourceDetailMasterDto.getUpdatedDate());
					  master.setCreatedBy(outSourceDetailMasterDto.getCreatedBy());
					  master.setCreatedDate(outSourceDetailMasterDto.getCreatedDate());
	  				  sessionFactory.getCurrentSession().save(master);
				  }
				  return new Response(SUCCESS, SUCCESS_200, OUT_SOURCE_DETAILS_SAVE_SUCCESS, null, null);
			  }
		  else 
		  {
			  return new Response(ERROR, ERR_500, OUT_SOURCE_DETAILS_SAVE_FAIL, null, null);
		  }
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getOutSourceDetailsByOutSourceId(OutSourceDetailMasterDto outSourceDetailMasterDto)
			throws ApplicationException {
		try
		{
			return iOutSourceDetailsDao.getOutSourceDetailsByOutSourceId(outSourceDetailMasterDto);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	
	

	

}
