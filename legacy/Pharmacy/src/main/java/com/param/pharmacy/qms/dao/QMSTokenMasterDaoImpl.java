package com.param.pharmacy.qms.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dto.QMSTokenMasterDto;
import com.param.qms.model.QMSTokenCallLog;
import com.param.qms.model.QMSTokenMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class QMSTokenMasterDaoImpl extends GenericDao<QMSTokenMaster, Integer>
		implements IQMSTokenMasterDao, ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(QMSTokenMasterDaoImpl.class);

	public QMSTokenMasterDaoImpl() {
		super(QMSTokenMaster.class);
	}

	@Override
	public Response saveToken(QMSTokenMasterDto dto) throws ApplicationException {
		try {
			Integer tokenNumber=getTokenNumber(dto);
			QMSTokenMaster master=new QMSTokenMaster();
			master.setQmsDeptMasterId(dto.getQmsDeptMasterId());
			master.setQmsCounterMasterId(dto.getQmsCounterMasterId());
			master.setQmsKioskMasterId(dto.getQmsKioskMasterId());
			master.setStartLetter(dto.getStartLetter());
			master.setTokenNumber(tokenNumber);
			master.setDummyTokenNumber(tokenNumber);
			master.setStatus('A');
			master.setIsCancel('I');
			master.setIsTransfer('I');
			master.setSrNo(1);
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setAssignedDeptId(dto.getQmsDeptMasterId());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			master.setUnitId(dto.getUnitId());
			master.setOrganisationId(dto.getOrganisationId());	
			Integer id=(Integer)sessionFactory.getCurrentSession().save(master);
			master.setQmsTokenMasterId(id);
			
			//inserting record for maintaining the sequence for displaying
				String sql = "INSERT INTO pharmacy.t_qms_token_sequence(qms_token_master_id,qms_kiosk_master_id,qms_dept_master_id,status,unit_id,organisation_id,created_by,created_date) VALUES(:id ,:qmsKioskMasterId ,:qmsDeptMasterId ,'A',:unitId ,:organisationId ,:createdBy ,:createdDate )";
				Query insertMsgQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
				insertMsgQuery.setParameter("id", id);
				insertMsgQuery.setParameter("qmsKioskMasterId", dto.getQmsKioskMasterId());
				insertMsgQuery.setParameter("qmsDeptMasterId", dto.getQmsDeptMasterId());
				insertMsgQuery.setParameter("unitId", dto.getUnitId());
				insertMsgQuery.setParameter("organisationId", dto.getOrganisationId());
			    insertMsgQuery.setParameter("createdBy", dto.getCreatedBy());
				insertMsgQuery.setParameter("createdDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				insertMsgQuery.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer getTokenNumber(QMSTokenMasterDto dto) throws ApplicationException {
		try {
			String query = "SELECT max(token_number) as \"tokenNumber\" "
					+ " FROM pharmacy.m_qms_token_master "
					+ " WHERE unit_id=:unitId AND organisation_id=:orgId AND qms_dept_master_id=:deptId AND qms_kiosk_master_id=:kioskId "
					+ " AND to_char(created_date,'YYYY-MM-DD')=:createdDate ";
			
			QMSTokenMasterDto tokenDto = (QMSTokenMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", dto.getUnitId())
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("deptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(QMSTokenMasterDto.class)).uniqueResult();
			if(tokenDto.getTokenNumber()!=null)
				return tokenDto.getTokenNumber()+1;
			else
				return Integer.parseInt(dto.getStartNumber());
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public Response getTokenList(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			List<QMSTokenMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_TOKEN_LIST")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("deptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(QMSTokenMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateToken(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			Integer count=0;
			if(dto.getIsCall()=='S'){				
			
			 count=sessionFactory.getCurrentSession().createSQLQuery("UPDATE pharmacy.m_qms_token_master"
					+ " set served_time=:servedTime "
					+ " where qms_token_master_id=:id "
					+ " and organisation_id=:orgId "
					+ " and unit_id=:unitId")
			.setParameter("id", dto.getQmsTokenMasterId())
			.setParameter("servedTime", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"))
			.setParameter("orgId", dto.getOrganisationId())
			.setParameter("unitId", dto.getUnitId())
			.executeUpdate();
			}else if(dto.getIsCall()=='C'){
				 count=sessionFactory.getCurrentSession().createSQLQuery("UPDATE pharmacy.m_qms_token_master"
							+ " set call_time =:callTime "
							+ " where qms_token_master_id=:id "
							+ " and organisation_id=:orgId "
							+ " and unit_id=:unitId")
					.setParameter("id", dto.getQmsTokenMasterId())
					.setParameter("callTime", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"))
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.executeUpdate();
			}else if(dto.getIsCall()=='D'){
				 count=sessionFactory.getCurrentSession().createSQLQuery("UPDATE pharmacy.m_qms_token_master"
							+ " set is_cancel  ='A'"
							+ " where qms_token_master_id=:id "
							+ " and organisation_id=:orgId "
							+ " and unit_id=:unitId")
					.setParameter("id", dto.getQmsTokenMasterId())
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.executeUpdate();
			}
			if(count>0){
				return new Response<>(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, null);
			}else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
			
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response transferToken(QMSTokenMasterDto dto) throws ApplicationException {
		try {
			
			/*List<QMSTokenMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_TOKEN_DETAILS_BY_TOKENID")
//					.setParameter("deptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setParameter("tokenNumber", dto.getTokenNumber())
					.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(QMSTokenMasterDto.class)).list();
			if(dtoList.size()>0){
				String query="UPDATE QMSTokenMaster set srNo=:srNo "						
						+ " where qmsTokenMasterId=:tokenId";
				Integer cnt=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("tokenId", dtoList.get(0).getQmsTokenMasterId())
						.setParameter("srNo", (dtoList.get(0).getSrNo()!=null ? (dtoList.get(0).getSrNo()+1) : 1))
						.executeUpdate();
			}
			*/
			
			String querySeq="UPDATE pharmacy.t_qms_token_sequence set status='I'"
					+ " where qms_kiosk_master_id=:kioskid "
					+ " and qms_token_master_id=:tokenId";
			Integer cntSeq=sessionFactory.getCurrentSession().createSQLQuery(querySeq)
					.setParameter("tokenId", dto.getQmsTokenMasterId())
					.setParameter("kioskid", dto.getQmsKioskMasterId())
					.executeUpdate();
			//inserting record for maintaining the sequence for displaying
			String sql = "INSERT INTO pharmacy.t_qms_token_sequence(qms_token_master_id,qms_kiosk_master_id,qms_dept_master_id,status,unit_id,organisation_id,created_by,created_date) VALUES(:qmsTokenMasterId ,:qmsKioskMasterId ,:qmsDeptMasterId ,'A',:unitId ,:organisationId ,:createdBy ,:createdDate )";
			Query insertMsgQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
			insertMsgQuery.setParameter("qmsTokenMasterId", dto.getQmsTokenMasterId());
			insertMsgQuery.setParameter("qmsKioskMasterId", dto.getQmsKioskMasterId());
			insertMsgQuery.setParameter("qmsDeptMasterId", dto.getQmsDeptMasterId());
			insertMsgQuery.setParameter("unitId", dto.getUnitId());
			insertMsgQuery.setParameter("organisationId", dto.getOrganisationId());
			insertMsgQuery.setParameter("createdBy", dto.getCreatedBy());
			insertMsgQuery.setParameter("createdDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			insertMsgQuery.executeUpdate();
			
			String query="UPDATE QMSTokenMaster set assignedDeptId=:qmsDeptId,isCall='N',qmsDeptMasterId=:qmsDeptId, "
//					+ "qmsCounterMasterId=:counterId ,"
					+ "qmsKioskMasterId=:kioskid "
					+ "where qmsTokenMasterId=:tokenId";
			Integer cnt=sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("tokenId", dto.getQmsTokenMasterId())
					.setParameter("qmsDeptId", dto.getQmsDeptMasterId())
//					.setParameter("counterId", dto.getQmsCounterMasterId())
					.setParameter("kioskid", dto.getQmsKioskMasterId())
					.executeUpdate();
			
			
			String query1="UPDATE QMSTokenCallLog set status='I'"
					+ "where qmsTokenMasterId=:tokenId";
			Integer cnt1=sessionFactory.getCurrentSession().createQuery(query1)
					.setParameter("tokenId", dto.getQmsTokenMasterId())
					.executeUpdate();
			if( cnt>0 && cnt1>0){
			
				/*QMSTokenMaster master=new QMSTokenMaster();
				master.setQmsDeptMasterId(dto.getQmsDeptMasterId());
				master.setQmsCounterMasterId(dto.getQmsCounterMasterId());
				master.setQmsKioskMasterId(dto.getQmsKioskMasterId());
				master.setStartLetter(dto.getStartLetter());
				master.setTokenNumber(Integer.parseInt(dto.getStartNumber()));
				master.setStatus('A');
				master.setIsTransfer('A');
				master.setIsCancel('I');
				master.setCreatedBy(dto.getCreatedBy());
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "dd-MM-yyyy HH:mm:ss"));
				master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "dd-MM-yyyy HH:mm:ss"));
				master.setUnitId(dto.getUnitId());
				master.setOrganisationId(dto.getOrganisationId());	
				sessionFactory.getCurrentSession().save(master);*/
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
			
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveTokenlog(QMSTokenMasterDto dto) throws ApplicationException {
		try {
			
				QMSTokenCallLog master=new QMSTokenCallLog();
				master.setQmsTokenMasterId(dto.getQmsTokenMasterId());
				master.setQmsDeptMasterId(dto.getQmsDeptMasterId());
				master.setQmsCounterMasterId(dto.getQmsCounterMasterId());
				master.setQmsKioskMasterId(dto.getQmsKioskMasterId());
				master.setStatus('A');
				master.setCallLogStatus(dto.getCallLogStatus());
				master.setCreatedBy(dto.getCreatedBy());
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				master.setUnitId(dto.getUnitId());
				master.setOrganisationId(dto.getOrganisationId());	
				sessionFactory.getCurrentSession().save(master);
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateTokenlog(QMSTokenMasterDto dto) throws ApplicationException {
		try {
			Integer cnt1=null;
			String query="UPDATE QMSTokenCallLog set status='I'"
					+ "where qmsTokenMasterId=:tokenId";
			Integer cnt=sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("tokenId", dto.getQmsTokenMasterId())
					.executeUpdate();
			if(dto.getCallLogStatus()==1){
				
//				if(dto.getPreviousQMSTokenMasterId()!=null){
				/*
					String query2="UPDATE QMSTokenMaster set isCall='I'"
							+ "where assignedDeptId=:deptId "
							+ "and qmsKioskMasterId=:kioskId ";
					cnt1=sessionFactory.getCurrentSession().createQuery(query2)
							.setParameter("deptId", dto.getQmsDeptMasterId())
							.setParameter("kioskId", dto.getQmsKioskMasterId())
							.executeUpdate();*/
//				}
				
				/*String query1="UPDATE QMSTokenMaster set isCall='A'"
						+ "where qmsTokenMasterId=:tokenId";
				cnt1=sessionFactory.getCurrentSession().createQuery(query1)
						.setParameter("tokenId", dto.getQmsTokenMasterId())
						.executeUpdate();*/
				
				
				
				
				//inserting broadcast message to db
				if(dto.getQmsCallMassage()!=null && ! dto.getQmsCallMassage().isEmpty()){
					String sql = "INSERT INTO pharmacy.t_broadcast_qms_msg(qms_kiosk_master_id,qms_dept_master_id,message,status,unit_id,organisation_id) VALUES(:qmsKioskMasterId ,:qmsDeptMasterId ,:qmsCallMassage ,'A',:unitId ,:unitId )";
					Query insertMsgQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
					insertMsgQuery.setParameter("qmsKioskMasterId", dto.getQmsKioskMasterId());
					insertMsgQuery.setParameter("qmsDeptMasterId", dto.getQmsDeptMasterId());
					insertMsgQuery.setParameter("qmsCallMassage", dto.getQmsCallMassage());
					insertMsgQuery.setParameter("unitId", dto.getUnitId());
					insertMsgQuery.executeUpdate();
				}
				
				
			}else {
				/*String query1="UPDATE QMSTokenMaster set isCall='I'"
						+ "where qmsTokenMasterId=:tokenId";
				    cnt1=sessionFactory.getCurrentSession().createQuery(query1)
						.setParameter("tokenId", dto.getQmsTokenMasterId())
						.executeUpdate();*/
			}
			
			if(cnt>0){		
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
			
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response resetToken(QMSTokenMasterDto dto) throws ApplicationException {
		try {
			
			Integer updatedToken = dto.getTokenNumber()+2;
			sessionFactory.getCurrentSession()
				.createQuery("update QMSTokenMaster tm set tm.callCount=0" 
						+ " ,tm.dummyTokenNumber=:updatedToken "
						+ " where tm.qmsTokenMasterId=:tokenId ")
				.setParameter("tokenId",dto.getQmsTokenMasterId())
					.setParameter("updatedToken",updatedToken)
			.executeUpdate();
			
			sessionFactory.getCurrentSession()
				.createQuery("update QMSTokenMaster tm set " 
						+ "tm.dummyTokenNumber=tm.dummyTokenNumber - 1 "
						+ "where tm.dummyTokenNumber <=:updatedToken and :tokenNumber < tm.dummyTokenNumber "
						+ "AND to_char(created_date,'YYYY-MM-DD')=:createdDate and qmsTokenMasterId !=:qmsTokenMasterId")
					.setParameter("updatedToken",updatedToken)
					.setParameter("tokenNumber",dto.getTokenNumber())
					.setParameter("createdDate",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"YYYY-MM-DD"))
					.setParameter("qmsTokenMasterId",dto.getQmsTokenMasterId())
			.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTokenListForDisplay(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			List<QMSTokenMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_TOKEN_LIST_FOR_DISPLAY")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("orgId", dto.getOrganisationId())
//					.setParameter("deptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(QMSTokenMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTokenDisplayMessage(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			List<QMSTokenMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_TOKEN_DISPLAY_MESSAGE")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("orgId", dto.getOrganisationId())
//					.setParameter("deptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
//					.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(QMSTokenMasterDto.class)).list();
			
			if(dtoList.size() > 0)
			{
				sessionFactory.getCurrentSession().createSQLQuery("update pharmacy.t_broadcast_qms_msg set "
						+ "status='I'"
						+ "WHERE broadcast_qms_msg_id=:id and qms_kiosk_master_id=:kioskId")
					.setParameter("id", dtoList.getFirst().getBroadcastQmsMsgId())
					.setParameter("kioskId", dtoList.getFirst().getQmsKioskMasterId())
				.executeUpdate();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkingIsCalledOrNot(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			List<QMSTokenMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("CHECKING_IS_CALLED_OR_NOT")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("deptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setParameter("tokenId", dto.getQmsTokenMasterId())
					.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(QMSTokenMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response releaseTokenNumber(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			
			sessionFactory.getCurrentSession().createQuery("update QMSTokenCallLog  "
					+ "set callLogStatus=0" 
					+ " where  qmsKioskMasterId=:kioskId"
					+ " and qmsDeptMasterId=:deptId"
					+ " and qmsCounterMasterId=:counterId"
					+ " and organisationId=:orgId"
					+ " and unitId=:unitId"
					+ " and status='A' and callLogStatus=1 "
					+ " and to_char(created_date,'YYYY-MM-DD')=:createdDate")
			.setParameter("unitId", dto.getUnitId())
			.setParameter("orgId", dto.getOrganisationId())
			.setParameter("deptId", dto.getQmsDeptMasterId())
			.setParameter("counterId", dto.getQmsCounterMasterId())
			.setParameter("kioskId", dto.getQmsKioskMasterId())
			.setParameter("createdDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd"))
		.executeUpdate();
					
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSTokenMaster(QMSTokenMasterDto dto) throws ApplicationException {
		try{
	            sessionFactory.getCurrentSession().createSQLQuery("update pharmacy.m_qms_token_master set is_call = 'N' where qms_kiosk_master_id =:kioskId and qms_dept_master_id =:kioskDeptId and qms_token_master_id <> :kioskTokanId and  qms_counter_master_id = :counterId ")
	            .setParameter("kioskId", dto.getQmsKioskMasterId())
				.setParameter("kioskDeptId",dto.getQmsDeptMasterId())
				.setParameter("kioskTokanId",dto.getQmsTokenMasterId())
				.setParameter("counterId", dto.getQmsCounterMasterId())
			    .executeUpdate();
				sessionFactory.getCurrentSession().createSQLQuery("update pharmacy.m_qms_token_master set is_call = 'N' where qms_kiosk_master_id =:kioskId and qms_dept_master_id <>:kioskDeptId and qms_token_master_id <> :kioskTokanId and  qms_counter_master_id <> :counterId ")
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setParameter("kioskDeptId",dto.getQmsDeptMasterId())
					.setParameter("kioskTokanId",dto.getQmsTokenMasterId())
					.setParameter("counterId", dto.getQmsCounterMasterId())
				.executeUpdate();
				
				sessionFactory.getCurrentSession().createSQLQuery("update pharmacy.m_qms_token_master set is_call = 'A' , qms_counter_master_id = :counterId where qms_kiosk_master_id =:kioskId and qms_dept_master_id =:kioskDeptId and qms_token_master_id = :kioskTokanId")
				.setParameter("kioskId", dto.getQmsKioskMasterId())
				.setParameter("kioskDeptId",dto.getQmsDeptMasterId())
				.setParameter("kioskTokanId",dto.getQmsTokenMasterId())
				.setParameter("counterId", dto.getQmsCounterMasterId())
			   .executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
