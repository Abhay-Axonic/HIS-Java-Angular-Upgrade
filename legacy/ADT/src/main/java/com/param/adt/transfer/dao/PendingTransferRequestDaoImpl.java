package com.param.adt.transfer.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.housekeeping.model.Housekeeping;
import com.param.adt.housekeeping.model.HousekeepingLog;
import com.param.adt.master.global.model.BedLogStatus;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.model.Transfer;
import com.param.global.dto.AdmissionDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PendingTransferRequestDaoImpl extends GenericDao<Transfer, Integer>
		implements IPendingTransferRequestDao, ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;

	public PendingTransferRequestDaoImpl() {
		super(Transfer.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getPendingTransferRequestListForDoctor(TransferDto transferDto) throws ApplicationException {
		try {
			
			Integer count=0;
			
			List<TransferDto> pendingTransferRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_DOCTOR")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("docId", transferDto.getDoctorId())
					.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset()  : 0)
					.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			
			List<TransferDto> pendingTransferRequestList1 = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_DOCTOR")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("docId", transferDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			
			 if(pendingTransferRequestList1 != null && pendingTransferRequestList1.size()>0) {
		    	  count = pendingTransferRequestList1.size();
		      }
			 
			
			return new Response(SUCCESS, null, null, pendingTransferRequestList, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptTransferRequestForDoctor(TransferDto newTransferDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			//<-----Updating Transfer Table -->setting transferStatus = 4(Request sent to doctor) 
			Query query = session.createQuery(
					"update Transfer tr set " +
							"tr.updatedBy = :updatedBy, " +
							"tr.updatedDate = :updatedDate, " +
							"tr.transferStatusId = :transferStatusId " +
							"where tr.organizationId = :organizationId " +
							"AND tr.unitId = :unitId " +
							"AND tr.transferRequestId = :transferRequestId");
			query.setParameter("updatedBy", newTransferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(newTransferDto.getUnitId() != null ? newTransferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("transferStatusId", newTransferDto.getTransferStatusId());
			query.setParameter("organizationId", newTransferDto.getOrganizationId());
			query.setParameter("unitId", newTransferDto.getUnitId());
			query.setParameter("transferRequestId", newTransferDto.getTransferRequestId());
			query.executeUpdate();
			//----->
			
			
			//<-----Updating TransferRequest Table -->setting transferStatus = 4(Request sent to doctor)
			Query query2 = session.createQuery(
					"update TransferRequest tr set " +
							"tr.updatedBy = :updatedBy, " +
							"tr.updatedDate = :updatedDate, " +
							"tr.transferStatusId = :transferStatusId, " +
							"tr.doctorNote = :doctorNote " +
							"where tr.organizationId = :organizationId " +
							"AND tr.unitId = :unitId " +
							"AND tr.transferRequestId = :transferRequestId");
			query2.setParameter("updatedBy", newTransferDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(newTransferDto.getUnitId() != null ? newTransferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("transferStatusId", newTransferDto.getTransferStatusId());
			query2.setParameter("doctorNote", newTransferDto.getDoctorNote());
			query2.setParameter("organizationId", newTransferDto.getOrganizationId());
			query2.setParameter("unitId", newTransferDto.getUnitId());
			query2.setParameter("transferRequestId", newTransferDto.getTransferRequestId());
			//System.out.println(query2.toString());
			query2.executeUpdate();
			//----->
			return new Response(SUCCESS, null, "Accepted", null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectTransferRequestForDoctor(TransferDto newTransferDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			
			//<-----Updating Transfer Table -->setting transferStatus = 5(Request reject) 
			Query query = session.createQuery(
					"update Transfer tr set " +
							"tr.updatedBy = :updatedBy, " +
							"tr.updatedDate = :updatedDate, " +
							"tr.status = 'I', " +
							"tr.transferStatusId = 5 " +
							"where tr.organizationId = :organizationId " +
							"AND tr.unitId = :unitId " +
							"AND tr.transferRequestId = :transferRequestId");
			query.setParameter("updatedBy", newTransferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(newTransferDto.getUnitId() != null ? newTransferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("organizationId", newTransferDto.getOrganizationId());
			query.setParameter("unitId", newTransferDto.getUnitId());
			query.setParameter("transferRequestId", newTransferDto.getTransferRequestId());
			query.executeUpdate();
			//----->
			
			Query query2 = session.createQuery(
					"update TransferRequest tr set " +
							"tr.updatedBy = :updatedBy, " +
							"tr.updatedDate = :updatedDate, " +
							"tr.status = 'I', " +
							"tr.transferStatusId = 5, " +
							"tr.doctorNote = :doctorNote, " +
							"tr.doctorReasonId = :doctorReasonId " +
							"where tr.organizationId = :organizationId " +
							"AND tr.unitId = :unitId " +
							"AND tr.transferRequestId = :transferRequestId"
			);
			query2.setParameter("updatedBy", newTransferDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(
					TimeZoneComponent.getDateByzone(newTransferDto.getUnitId() != null ? newTransferDto.getUnitId() : 0), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("doctorNote", newTransferDto.getDoctorNote());
			query2.setParameter("doctorReasonId", newTransferDto.getDoctorReasonId());
			query2.setParameter("organizationId", newTransferDto.getOrganizationId());
			query2.setParameter("unitId", newTransferDto.getUnitId());
			query2.setParameter("transferRequestId", newTransferDto.getTransferRequestId());
			query2.executeUpdate();
			//----->
			
			
			Transfer transfer= findById(newTransferDto.getTransferId());
				transfer.setCreatedDate(TimeZoneComponent.getDateByzone(newTransferDto.getUnitId() != null ? newTransferDto.getUnitId() :0));
				transfer.setUpdatedDate(TimeZoneComponent.getDateByzone(newTransferDto.getUnitId() != null ? newTransferDto.getUnitId() :0));
				transfer.setTransferStatusId(1);
				transfer.setIsPrimary('N');
				transfer.setStatus('A');
				transfer.setCreatedBy(newTransferDto.getUpdatedBy());
				transfer.setUpdatedBy(newTransferDto.getUpdatedBy());
			session.save(transfer);
			
			return new Response(SUCCESS, null, "Rejected", null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPendingTransferRequestListForADT(TransferDto transferDto) {
		try {
			Integer count=0;
			List<TransferDto> pendingTransferRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_ADT")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset()  : 0)
					.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			
			
			 List<AdmissionDto> listcount  = sessionFactory.getCurrentSession()
		    		   .getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_ADT")
		    		   .setParameter("organizationId", transferDto.getOrganizationId())
						.setParameter("unitId", transferDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
		     
		      
		      if(listcount != null && listcount.size()>0) {
		    	  count = listcount.size();
		      }
		      
			return new Response(SUCCESS, null, null, pendingTransferRequestList, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptTransferRequestForADT(TransferDto transferDto) throws ApplicationException {
		try {
			//<-----Updating transfer 
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(
                    "update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, transfer.toBedCategoryId=:toBedCategoryId, transfer.toBillingBedCategoryId=:toBillingBedCategoryId, transfer.toWardId=:toWardId, transfer.toRoomId=:toRoomId, transfer.toBedId=:toBedId, transfer.transferStatusId=6 WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
			query.setParameter("toBedCategoryId", transferDto.getToBedCategoryId());
			query.setParameter("toBillingBedCategoryId", transferDto.getToBillingBedCategoryId());
			query.setParameter("toWardId", transferDto.getToWardId());
			query.setParameter("toRoomId", transferDto.getToRoomId());
			query.setParameter("toBedId", transferDto.getToBedId());
			query.setParameter("transferId", transferDto.getTransferId());
			query.setParameter("updatedBy", transferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			query.setParameter("unitId", transferDto.getUnitId());
			query.setParameter("orgId", transferDto.getOrganizationId());
			query.executeUpdate();
			//------>
			
			//<------Updating transfer request
			Query query2=session.createQuery("update TransferRequest traReq set traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.toWardId=:toWardId,traReq.transferStatusId=6 WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
			query2.setParameter("updatedBy", transferDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("unitId", transferDto.getUnitId());
			query2.setParameter("orgId", transferDto.getOrganizationId());
			query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
			query2.setParameter("toWardId", transferDto.getToWardId());
			query2.executeUpdate();
			//------>
			
			//<-------Booking requested bed so that it should not get allocated to any other 
			Query query3=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=5 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
			query3.setParameter("updatedBy", transferDto.getUpdatedBy());
			query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query3.setParameter("unitId", transferDto.getUnitId());
			query3.setParameter("orgId", transferDto.getOrganizationId());
			query3.setParameter("bedId", transferDto.getToBedId());
			query3.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query4=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
			query4.setParameter("updatedBy", transferDto.getUpdatedBy());
			query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query4.setParameter("unitId", transferDto.getUnitId());
			query4.setParameter("orgId", transferDto.getOrganizationId());
			query4.setParameter("bedId", transferDto.getToBedId());
			query4.executeUpdate();
			
			
			BedLogStatus bedLogStatus=new BedLogStatus();
			bedLogStatus.setBedId(transferDto.getToBedId());
			bedLogStatus.setBedStatusId(5);
			bedLogStatus.setStatus('A');
			bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
			bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
			bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
			bedLogStatus.setUnitId(transferDto.getUnitId());
			sessionFactory.getCurrentSession().save(bedLogStatus);
			//------>	
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectTransferRequestForADT(TransferDto transferDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate,transfer.status='I', transfer.transferStatusId=5 WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
			query.setParameter("transferId", transferDto.getTransferId());
			query.setParameter("updatedBy", transferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query.setParameter("unitId", transferDto.getUnitId());
			query.setParameter("orgId", transferDto.getOrganizationId());
			query.executeUpdate();
			
			Query query2=session.createQuery("update TransferRequest traReq set traReq.adtRejectReason=:adtRejectReason, traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.transferStatusId=5,traReq.status='I',traReq.adtReasonId=:reasonId WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
			query2.setParameter("updatedBy", transferDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("unitId", transferDto.getUnitId());
			query2.setParameter("orgId", transferDto.getOrganizationId());
			query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
			query2.setParameter("reasonId", transferDto.getAdtReasonId());
			query2.setParameter("adtRejectReason", transferDto.getAdtRejectReason());
			query2.executeUpdate();
			
//Start
			

			//<-------Release allocated bed
			if(transferDto.getToBedId()!=null){
			Query query3=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=3 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
				query3.setParameter("updatedBy", transferDto.getUpdatedBy());
				query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query3.setParameter("unitId", transferDto.getUnitId());
				query3.setParameter("orgId", transferDto.getOrganizationId());
				query3.setParameter("bedId", transferDto.getToBedId());
			query3.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query4=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
				query4.setParameter("updatedBy", transferDto.getUpdatedBy());
				query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query4.setParameter("unitId", transferDto.getUnitId());
				query4.setParameter("orgId", transferDto.getOrganizationId());
				query4.setParameter("bedId", transferDto.getToBedId());
			query4.executeUpdate();
			
			BedLogStatus bedLogStatus=new BedLogStatus();
				bedLogStatus.setBedId(transferDto.getToBedId());
				bedLogStatus.setBedStatusId(3);
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
				bedLogStatus.setUnitId(transferDto.getUnitId());
			session.save(bedLogStatus);
			}
			
			Transfer transfer=new Transfer();
			transfer.setOrganizationId(transferDto.getOrganizationId());
			transfer.setUnitId(transferDto.getUnitId());
			transfer.setAdmissionId(transferDto.getAdmissionId());
			transfer.setFromBedCategoryId(transferDto.getFromBedCategoryId());
			transfer.setFromBillingBedCategoryId(transferDto.getFromBillingBedCategoryId());
			transfer.setFromWardId(transferDto.getFromWardId());
			transfer.setFromRoomId(transferDto.getFromRoomId());
			transfer.setFromBedId(transferDto.getFromBedId());
			transfer.setFromDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
			transfer.setFromTime(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
			transfer.setCreatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
			transfer.setUpdatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
			transfer.setTransferStatusId(1);
			transfer.setIsPrimary('N');
			transfer.setStatus('A');
			transfer.setCreatedBy(transferDto.getUpdatedBy());
			transfer.setUpdatedBy(transferDto.getUpdatedBy());
			session.save(transfer);
			//END
			
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptFinalTransferRequest(TransferDto transferDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			
			if(transferDto.getTransferTypeId()!=null)	//
			{
				Query query = session.createQuery(
						"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.toTime=:updatedDate,transfer.toDate=:updatedDate,transfer.updatedDate=:updatedDate, transfer.toBedCategoryId=:toBedCategoryId, transfer.toWardId=:toWardId, transfer.toRoomId=:toRoomId, transfer.toBedId=:toBedId, transfer.transferStatusId=9 WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
				query.setParameter("toBedCategoryId", transferDto.getToBedCategoryId());
				//query.setParameter("toBillingBedCategoryId", transferDto.getToBillingBedCategoryId());
				query.setParameter("toWardId", transferDto.getToWardId());
				query.setParameter("toRoomId", transferDto.getToRoomId());
				query.setParameter("toBedId", transferDto.getToBedId());
				query.setParameter("transferId", transferDto.getTransferId());
				query.setParameter("updatedBy", transferDto.getUpdatedBy());
				query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query.setParameter("unitId", transferDto.getUnitId());
				query.setParameter("orgId", transferDto.getOrganizationId());
				query.executeUpdate();
				//------>
				
				//<------Updating transfer request
				Query query2=session.createQuery("update TransferRequest traReq set traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.toWardId=:toWardId,traReq.toBedCategoryId=:toBedCategoryId ,traReq.transferStatusId=9 WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
				query2.setParameter("updatedBy", transferDto.getUpdatedBy());
				query2.setParameter("updatedDate", TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
				query2.setParameter("unitId", transferDto.getUnitId());
				query2.setParameter("orgId", transferDto.getOrganizationId());
				query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
				query2.setParameter("toWardId", transferDto.getToWardId());
				query2.setParameter("toBedCategoryId", transferDto.getToBedCategoryId());
				query2.executeUpdate();
				//------>
				
				//<-------Booking requested bed so that it should not get allocated to any other 
				Query query3=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=5 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
				query3.setParameter("updatedBy", transferDto.getUpdatedBy());
				query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query3.setParameter("unitId", transferDto.getUnitId());
				query3.setParameter("orgId", transferDto.getOrganizationId());
				query3.setParameter("bedId", transferDto.getToBedId());
				query3.executeUpdate();
				//----->
				
				//<------Adding new entry in bed log status by in-activating previous active record
				Query query4=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
				query4.setParameter("updatedBy", transferDto.getUpdatedBy());
				query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query4.setParameter("unitId", transferDto.getUnitId());
				query4.setParameter("orgId", transferDto.getOrganizationId());
				query4.setParameter("bedId", transferDto.getToBedId());
				query4.executeUpdate();
				
				
				BedLogStatus bedLogStatus=new BedLogStatus();
					bedLogStatus.setBedId(transferDto.getToBedId());
					bedLogStatus.setBedStatusId(5);
					bedLogStatus.setStatus('A');
					bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
					bedLogStatus.setUnitId(transferDto.getUnitId());
				session.save(bedLogStatus);
				//------>	
				
			}
			
			if(transferDto.getTransferTypeId()==null)
			{
				Query query = session.createQuery(
						"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, transfer.transferStatusId=9,transfer.status='I' WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
					query.setParameter("transferId", transferDto.getTransferId());
					query.setParameter("updatedBy", transferDto.getUpdatedBy());
					query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query.setParameter("unitId", transferDto.getUnitId());
					query.setParameter("orgId", transferDto.getOrganizationId());
				query.executeUpdate();
				
				Query query2=session.createQuery("update TransferRequest traReq set traReq.workstationNote=:workStationNote, traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.transferStatusId=9,traReq.status='I' WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
					query2.setParameter("updatedBy", transferDto.getUpdatedBy());
					query2.setParameter("updatedDate", TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
					query2.setParameter("unitId", transferDto.getUnitId());
					query2.setParameter("orgId", transferDto.getOrganizationId());
					query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
					query2.setParameter("workStationNote", transferDto.getWorkstationNote());
				query2.executeUpdate();
				}
				
			
			Transfer master =  findById(transferDto.getTransferId());
			
				Query query3=session.createQuery("update AdmissionDetails admt set admt.bedCategoryId=:bedCategoryId, admt.billingBedCategoryId=:billingBedCategoryId, admt.updatedBy=:updatedBy,admt.updatedDate=:updatedDate,admt.floorId=:floorId,admt.wardId=:wardId,admt.bedId=:bedId,roomId=:roomId WHERE admt.admissionId=:admissionId AND admt.unitId=:unitId AND admt.organizationId=:orgId AND admt.status='A'");
					query3.setParameter("updatedBy", transferDto.getUpdatedBy());
					query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query3.setParameter("unitId", transferDto.getUnitId());
					query3.setParameter("orgId", transferDto.getOrganizationId());
					query3.setParameter("admissionId", transferDto.getAdmissionId());
					query3.setParameter("billingBedCategoryId", master.getToBillingBedCategoryId()!=null ? master.getToBillingBedCategoryId()  : transferDto.getToBillingBedCategoryId());
					query3.setParameter("bedCategoryId", transferDto.getToBedCategoryId());
					query3.setParameter("floorId", transferDto.getToFloorId());
					query3.setParameter("wardId", transferDto.getToWardId());
					query3.setParameter("roomId",transferDto.getToRoomId());
					query3.setParameter("bedId", transferDto.getToBedId());
				
				query3.executeUpdate();
				
				
				//update visit type
		/*		if(transferDto.getNewVisitTypeId()!=null) {
				 Query queryForAdtUpdate=session.createQuery("update Admission adt set adt.visitTypeId=:visitTypeId WHERE adt.admissionId=:admissionId AND adt.unitId=:unitId AND adt.organizationId=:orgId ");
					queryForAdtUpdate.setParameter("visitTypeId", transferDto.getNewVisitTypeId());
					queryForAdtUpdate.setParameter("admissionId", transferDto.getAdmissionId());
					queryForAdtUpdate.setParameter("unitId", transferDto.getUnitId());
					queryForAdtUpdate.setParameter("orgId", transferDto.getOrganizationId());
					queryForAdtUpdate.executeUpdate();
				}*/
				
				if(transferDto.getNewVisitTypeId()!=null) {
					
					if(transferDto.getDoctorId() != null && transferDto.getSpecialityId() != null && transferDto.getAdmissionId() != null ) {
	                 Query queryForAdtUpdate=session.createQuery("update Admission adt set adt.visitTypeId=:visitTypeId, adt.doctorId=:doctorId , adt.specialityId=:specialityId WHERE adt.admissionId=:admissionId AND adt.unitId=:unitId AND adt.organizationId=:orgId ");
	                    queryForAdtUpdate.setParameter("visitTypeId", transferDto.getNewVisitTypeId());
	                    queryForAdtUpdate.setParameter("doctorId", transferDto.getDoctorId());
	                    queryForAdtUpdate.setParameter("specialityId", transferDto.getSpecialityId());
	                    queryForAdtUpdate.setParameter("admissionId", transferDto.getAdmissionId());
	                    queryForAdtUpdate.setParameter("unitId", transferDto.getUnitId());
	                    queryForAdtUpdate.setParameter("orgId", transferDto.getOrganizationId());
	                    queryForAdtUpdate.executeUpdate();
				}
	                    
	                    sessionFactory.getCurrentSession().
	   	    	    	 createSQLQuery(" update public.t_order_master  set visit_type_id=:visitTypeId where admission_id=:admissionId "	)
	   	   			    .setParameter("visitTypeId",transferDto.getNewVisitTypeId())
	   	   			    .setParameter("admissionId", transferDto.getAdmissionId()).executeUpdate();
	                    
	                    
	                    sessionFactory.getCurrentSession().
		    	    	 createSQLQuery(" update pharmacy.t_issue  set visit_type_id=:visitTypeId where admission_id=:admissionId "	)
		   			    .setParameter("visitTypeId",transferDto.getNewVisitTypeId())
		   			    .setParameter("admissionId", transferDto.getAdmissionId()).executeUpdate();
	                }
				
				Integer bedStatusId = 2;
				if(transferDto.getSkipHousekeeping()=='Y')
				{
					bedStatusId=3;
				}
				
				
				//<------Adding new entry in bed log status by in-activating previous active record
				Query query9=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
					query9.setParameter("updatedBy", transferDto.getUpdatedBy());
					query9.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query9.setParameter("unitId", transferDto.getUnitId());
					query9.setParameter("orgId", transferDto.getOrganizationId());
					query9.setParameter("bedId", transferDto.getToBedId());
				query9.executeUpdate();
				
				//<-------change bed status of new bed 
				Query query10=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=4 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
					query10.setParameter("updatedBy", transferDto.getUpdatedBy());
					query10.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query10.setParameter("unitId", transferDto.getUnitId());
					query10.setParameter("orgId", transferDto.getOrganizationId());
					query10.setParameter("bedId", transferDto.getToBedId());
					query10.executeUpdate();
				//----->
					
				BedLogStatus bedLogStatus=new BedLogStatus();
					bedLogStatus.setBedId(transferDto.getToBedId());
					bedLogStatus.setBedStatusId(4);
					bedLogStatus.setStatus('A');
					bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
					bedLogStatus.setUnitId(transferDto.getUnitId());
				session.save(bedLogStatus);
				//------>	
				
				//<-------Releasing previous bed of a patient 
				Query query4=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId="+bedStatusId+" WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
					query4.setParameter("updatedBy", transferDto.getUpdatedBy());
					query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query4.setParameter("unitId", transferDto.getUnitId());
					query4.setParameter("orgId", transferDto.getOrganizationId());
					query4.setParameter("bedId", transferDto.getFromBedId());
				query4.executeUpdate();
				//----->
				
				//<------Adding new entry in bed log status by in-activating previous active record
				Query query5=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
					query5.setParameter("updatedBy", transferDto.getUpdatedBy());
					query5.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query5.setParameter("unitId", transferDto.getUnitId());
					query5.setParameter("orgId", transferDto.getOrganizationId());
					query5.setParameter("bedId", transferDto.getFromBedId());
				query5.executeUpdate();
				
				bedLogStatus=new BedLogStatus();
					bedLogStatus.setBedId(transferDto.getFromBedId());
					bedLogStatus.setBedStatusId(bedStatusId);
					bedLogStatus.setStatus('A');
					bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
					bedLogStatus.setUnitId(transferDto.getUnitId());
				session.save(bedLogStatus);
				//------>	
				
				Transfer transfer=new Transfer();
					transfer.setOrganizationId(transferDto.getOrganizationId());
					transfer.setUnitId(transferDto.getUnitId());
					transfer.setAdmissionId(transferDto.getAdmissionId());
					transfer.setFromBedCategoryId(transferDto.getToBedCategoryId());
					transfer.setFromBillingBedCategoryId(transferDto.getToBillingBedCategoryId());
					transfer.setFromWardId(transferDto.getToWardId());
					transfer.setFromRoomId(transferDto.getToRoomId());
					transfer.setFromBedId(transferDto.getToBedId());
					transfer.setFromDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
					transfer.setFromTime(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
					transfer.setCreatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
					transfer.setUpdatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
					transfer.setTransferStatusId(1);
					transfer.setIsPrimary('N');
					transfer.setStatus('A');
					transfer.setCreatedBy(transferDto.getUpdatedBy());
					transfer.setUpdatedBy(transferDto.getUpdatedBy());
				session.save(transfer);
				
				if(transferDto.getSkipHousekeeping()=='N')
				{
					// <-----Adding new entry in Housekeeping
					Housekeeping housekeeping = new Housekeeping();
						housekeeping.setOrganizationId(transferDto.getOrganizationId());
						housekeeping.setUnitId(transferDto.getUnitId());
						housekeeping.setNursingStationId(transferDto.getNursingStationId());
						housekeeping.setFloorId(transferDto.getFromFloorId());
						housekeeping.setWardId(transferDto.getFromWardId());
						housekeeping.setRoomId(transferDto.getFromRoomId());
						housekeeping.setBedId(transferDto.getFromBedId());
						housekeeping.setBedCategoryId(transferDto.getFromBedCategoryId());
						housekeeping.setPriorityId(2);
						housekeeping.setHousekeepingStatusId(1);
						housekeeping.setStatus('A');
						housekeeping.setCreatedBy(transferDto.getUpdatedBy());
						housekeeping.setUpdatedBy(transferDto.getUpdatedBy());
						housekeeping.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
						housekeeping.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					Integer id = (Integer)session.save(housekeeping);
					
					
					HousekeepingLog housekeepingLog=new HousekeepingLog();
						housekeepingLog.setHousekeepingId(id);
						housekeepingLog.setOrganizationId(transferDto.getOrganizationId());
						housekeepingLog.setUnitId(transferDto.getUnitId());
						housekeepingLog.setPriorityId(2);
						housekeepingLog.setHousekeepingStatusId(1);
						housekeepingLog.setCreatedBy(transferDto.getUpdatedBy());
						housekeepingLog.setUpdatedBy(transferDto.getUpdatedBy());
						housekeepingLog.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
						housekeepingLog.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
						housekeepingLog.setStatus('A');
					session.save(housekeepingLog);
								//end for housekeepinng
					//----->
				}
				
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectFinalTransferRequest(TransferDto transferDto) throws ApplicationException {
	try
	{
		//<-----Updating transfer 
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, transfer.transferStatusId=5 WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
			query.setParameter("transferId", transferDto.getTransferId());
			query.setParameter("updatedBy", transferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query.setParameter("unitId", transferDto.getUnitId());
			query.setParameter("orgId", transferDto.getOrganizationId());
		query.executeUpdate();
		//------>
		
		//<------Updating transfer request
		Query query2=session.createQuery("update TransferRequest traReq set traReq.workstationReasonId=:workstationReasonId, traReq.workstationNote=:workStationNote, traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.transferStatusId=5 WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
			query2.setParameter("updatedBy", transferDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("unitId", transferDto.getUnitId());
			query2.setParameter("orgId", transferDto.getOrganizationId());
			query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
			query2.setParameter("workStationNote", transferDto.getWorkstationNote());
			query2.setParameter("workstationReasonId", transferDto.getWorkstationReasonId());
		query2.executeUpdate();
		//------>
		
		//<-------Booking requested bed so that it should not get allocated to any other patient
		Query query3=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=3 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
			query3.setParameter("updatedBy", transferDto.getUpdatedBy());
			query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query3.setParameter("unitId", transferDto.getUnitId());
			query3.setParameter("orgId", transferDto.getOrganizationId());
			query3.setParameter("bedId", transferDto.getToBedId());
		query3.executeUpdate();
		//----->
		
		//<------Adding new entry in bed log status by in-activating previous active record
		Query query4=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
			query4.setParameter("updatedBy", transferDto.getUpdatedBy());
			query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query4.setParameter("unitId", transferDto.getUnitId());
			query4.setParameter("orgId", transferDto.getOrganizationId());
			query4.setParameter("bedId", transferDto.getToBedId());
		query4.executeUpdate();
		
		Transaction tr=session.beginTransaction();
		BedLogStatus bedLogStatus=new BedLogStatus();
			bedLogStatus.setBedId(transferDto.getToBedId());
			bedLogStatus.setBedStatusId(3);
			bedLogStatus.setStatus('A');
			bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
			bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
			bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
			bedLogStatus.setUnitId(transferDto.getUnitId());
		session.save(bedLogStatus);
		tr.commit();
		session.close();
		//------>	
		return new Response(SUCCESS, null, null, null, null);
	} catch (HibernateException he) {
	} catch (Exception e) {
		
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFinalTransferRequestList(TransferDto transferDto) throws ApplicationException {
		try {

			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_FINAL_TRANSFER_REQUEST_LIST")
					.getQueryString();

			String finalQuery;
			List<TransferDto> pendingTransferRequestList;
			 if(transferDto.getIsFromNursingAcceptancePage()!=null && transferDto.getIsFromNursingAcceptancePage()=='Y') {
				 
				 if (transferDto.getIsShowPatientByBed().equals('N')) {
						pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setParameter("organizationId", transferDto.getOrganizationId())
								.setParameter("unitId", transferDto.getUnitId())
								.setParameterList("wardList", transferDto.getWardList())
								.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
								.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					} else {
						finalQuery = query.replace("transfer.to_ward_id IN (:wardList)",
								"transfer.from_bed_id IN (:bedList)");
						pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
								.setParameter("organizationId", transferDto.getOrganizationId())
								.setParameter("unitId", transferDto.getUnitId())
								.setParameterList("bedList", transferDto.getBedList())
								.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
								.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					}
				 
			 }else {
					if (transferDto.getIsShowPatientByBed().equals('N')) {
						pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setParameter("organizationId", transferDto.getOrganizationId())
								.setParameter("unitId", transferDto.getUnitId())
								.setParameterList("wardList", transferDto.getWardList())
								.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					} else {
						finalQuery = query.replace("transfer.to_ward_id IN (:wardList)",
								"transfer.from_bed_id IN (:bedList)");
						pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
								.setParameter("organizationId", transferDto.getOrganizationId())
								.setParameter("unitId", transferDto.getUnitId())
								.setParameterList("bedList", transferDto.getBedList())
								.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					}
			 }
		
			return new Response(SUCCESS, null, null, pendingTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptTransferRequest(TransferDto transferDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, transfer.transferStatusId=7 WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
				query.setParameter("transferId", transferDto.getTransferId());
				query.setParameter("updatedBy", transferDto.getUpdatedBy());
				query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query.setParameter("unitId", transferDto.getUnitId());
				query.setParameter("orgId", transferDto.getOrganizationId());
			query.executeUpdate();
			
			Query query2=session.createQuery("update TransferRequest traReq set traReq.finalNote=:finalNote, traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.transferStatusId=7 WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
				query2.setParameter("updatedBy", transferDto.getUpdatedBy());
				query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query2.setParameter("unitId", transferDto.getUnitId());
				query2.setParameter("orgId", transferDto.getOrganizationId());
				query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
				query2.setParameter("finalNote", transferDto.getFinalNote());
			query2.executeUpdate();
			
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response rejectTransferRequest(TransferDto transferDto) throws ApplicationException {
		try
		{
			//<-----Updating transfer 
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, transfer.transferStatusId=5 WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
				query.setParameter("transferId", transferDto.getTransferId());
				query.setParameter("updatedBy", transferDto.getUpdatedBy());
				query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query.setParameter("unitId", transferDto.getUnitId());
				query.setParameter("orgId", transferDto.getOrganizationId());
			query.executeUpdate();
			//------>
			
			//<------Updating transfer request
			Query query2=session.createQuery("update TransferRequest traReq set traReq.finalReasonId=:finalReasonId, traReq.finalNote=:finalNote, traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.transferStatusId=5 WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
				query2.setParameter("updatedBy", transferDto.getUpdatedBy());
				query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query2.setParameter("unitId", transferDto.getUnitId());
				query2.setParameter("orgId", transferDto.getOrganizationId());
				query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
				query2.setParameter("finalNote", transferDto.getFinalNote());
				query2.setParameter("finalReasonId", transferDto.getFinalReasonId());
			query2.executeUpdate();
			//------>
			
			//<-------Un-booking requested bed
			if(transferDto.getToBedId()!=null)
			{
				Query query3=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=3 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
					query3.setParameter("updatedBy", transferDto.getUpdatedBy());
					query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query3.setParameter("unitId", transferDto.getUnitId());
					query3.setParameter("orgId", transferDto.getOrganizationId());
					query3.setParameter("bedId", transferDto.getToBedId());
				query3.executeUpdate();
				//----->
				
				//<------Adding new entry in bed log status by in-activating previous active record
				Query query4=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
					query4.setParameter("updatedBy", transferDto.getUpdatedBy());
					query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query4.setParameter("unitId", transferDto.getUnitId());
					query4.setParameter("orgId", transferDto.getOrganizationId());
					query4.setParameter("bedId", transferDto.getToBedId());
				query4.executeUpdate();
				
				BedLogStatus bedLogStatus=new BedLogStatus();
					bedLogStatus.setBedId(transferDto.getToBedId());
					bedLogStatus.setBedStatusId(3);
					bedLogStatus.setStatus('A');
					bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
					bedLogStatus.setUnitId(transferDto.getUnitId());
				session.save(bedLogStatus);
			}
			
			//------>	
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFinalTransferRequestListByAdmission(TransferDto transferDto) throws ApplicationException {
		try {
			List<TransferDto> pendingTransferRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_FINAL_TRANSFER_REQUEST_LIST_BY_ADMISSION_ID")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("admissionId", transferDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			return new Response(SUCCESS, null, null, pendingTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveMovingBed(TransferDto transferDto) throws ApplicationException {
		
			try {

				Session session = sessionFactory.getCurrentSession();
				
//				if(transferDto.getTransferTypeId()==null)
//				{
				Query query = session.createQuery(
						"update Discharge discharge set discharge.updatedBy=:updatedBy,discharge.updatedDate=:updatedDate, discharge.destinationId=:bedId WHERE discharge.dischargeId=:dischargeId AND discharge.unitId=:unitId AND discharge.organizationId=:orgId");
					query.setParameter("dischargeId", transferDto.getDischargeId());
					query.setParameter("updatedBy", transferDto.getUpdatedBy());
					query.setParameter("bedId", transferDto.getToBedId());
					query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query.setParameter("unitId", transferDto.getUnitId());
					query.setParameter("orgId", transferDto.getOrganizationId());
				query.executeUpdate();
				
//				}
				
				Query query3=session.createQuery("update AdmissionDetails admt set admt.updatedBy=:updatedBy,admt.updatedDate=:updatedDate,admt.floorId=null,admt.wardId=null,admt.bedId=:bedId,roomId=null WHERE admt.admissionId=:admissionId AND admt.unitId=:unitId AND admt.organizationId=:orgId AND admt.status='A'");
					query3.setParameter("updatedBy", transferDto.getUpdatedBy());
					query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query3.setParameter("unitId", transferDto.getUnitId());
					query3.setParameter("orgId", transferDto.getOrganizationId());
					query3.setParameter("admissionId", transferDto.getAdmissionId());
					//query3.setParameter("admissionDetailsId", transferDto.getAdmissionDetailsId());
					query3.setParameter("bedId", transferDto.getToBedId());
				
				query3.executeUpdate();
				
				//<-------Releasing previous bed of a patient 
				Query query4=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=2 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
					query4.setParameter("updatedBy", transferDto.getUpdatedBy());
					query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query4.setParameter("unitId", transferDto.getUnitId());
					query4.setParameter("orgId", transferDto.getOrganizationId());
					query4.setParameter("bedId", transferDto.getFromBedId());
				query4.executeUpdate();
				//----->
				
				//<------Adding new entry in bed log status by in-activating previous active record
				Query query5=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
					query5.setParameter("updatedBy", transferDto.getUpdatedBy());
					query5.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					query5.setParameter("unitId", transferDto.getUnitId());
					query5.setParameter("orgId", transferDto.getOrganizationId());
					query5.setParameter("bedId", transferDto.getFromBedId());
				query4.executeUpdate();
				
				BedLogStatus bedLogStatus=new BedLogStatus();
					bedLogStatus.setBedId(transferDto.getFromBedId());
					bedLogStatus.setBedStatusId(2);
					bedLogStatus.setStatus('A');
					bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
					bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
					bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
					bedLogStatus.setUnitId(transferDto.getUnitId());
				session.save(bedLogStatus);
				//------>	
				
				
				
				// <-----Adding new entry in Housekeeping
							Housekeeping housekeeping = new Housekeeping();
								housekeeping.setOrganizationId(transferDto.getOrganizationId());
								housekeeping.setUnitId(transferDto.getUnitId());
//								housekeeping.setNursingStationId(transferDto.getNursingStationId());
//								housekeeping.setFloorId(transferDto.getFromFloorId());
//								housekeeping.setWardId(transferDto.getFromWardId());
//								housekeeping.setRoomId(transferDto.getFromRoomId());
								housekeeping.setBedId(transferDto.getFromBedId());
//								housekeeping.setBedCategoryId(transferDto.getFromBedCategoryId());
								housekeeping.setPriorityId(2);
								housekeeping.setHousekeepingStatusId(1);
								housekeeping.setStatus('A');
								housekeeping.setCreatedBy(transferDto.getUpdatedBy());
								housekeeping.setUpdatedBy(transferDto.getUpdatedBy());
								housekeeping.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
								housekeeping.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
							Integer id = (Integer)session.save(housekeeping);
							
							
							HousekeepingLog housekeepingLog=new HousekeepingLog();
								housekeepingLog.setHousekeepingId(id);
								housekeepingLog.setOrganizationId(transferDto.getOrganizationId());
								housekeepingLog.setUnitId(transferDto.getUnitId());
								housekeepingLog.setPriorityId(2);
								housekeepingLog.setHousekeepingStatusId(1);
								housekeepingLog.setCreatedBy(transferDto.getUpdatedBy());
								housekeepingLog.setUpdatedBy(transferDto.getUpdatedBy());
								housekeepingLog.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
								housekeepingLog.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
								housekeepingLog.setStatus('A');
							session.save(housekeepingLog);
							//end for housekeepinng
				//----->
				
							
//							=========================
							
							
							//<-------Booking requested bed so that it should not get allocated to any other 
							Query query6=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=5 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
							query6.setParameter("updatedBy", transferDto.getUpdatedBy());
							query6.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
							query6.setParameter("unitId", transferDto.getUnitId());
							query6.setParameter("orgId", transferDto.getOrganizationId());
							query6.setParameter("bedId", transferDto.getToBedId());
							query6.executeUpdate();
							//----->
							
							//<------Adding new entry in bed log status by in-activating previous active record
							Query query7=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
							query7.setParameter("updatedBy", transferDto.getUpdatedBy());
							query7.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
							query7.setParameter("unitId", transferDto.getUnitId());
							query7.setParameter("orgId", transferDto.getOrganizationId());
							query7.setParameter("bedId", transferDto.getToBedId());
							query7.executeUpdate();
							
							
							BedLogStatus bed=new BedLogStatus();
							bed.setBedId(transferDto.getToBedId());
							bed.setBedStatusId(5);
							bed.setStatus('A');
							bed.setCreatedBy(transferDto.getUpdatedBy());
							bed.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
							bed.setUpdatedBy(transferDto.getUpdatedBy());
							bed.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
							bed.setOrganizationId(transferDto.getOrganizationId());
							bed.setUnitId(transferDto.getUnitId());
							session.save(bed);
				return new Response(SUCCESS, null, null, null, null);
			} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, null, null, null);
		}

	@Override
	public Response acceptFinalLaborRoomTransferRequest(TransferDto transferDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			//<-------Booking requested bed so that it should not get allocated to any other 
			Query query=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=5 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
			query.setParameter("updatedBy", transferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query.setParameter("unitId", transferDto.getUnitId());
			query.setParameter("orgId", transferDto.getOrganizationId());
			query.setParameter("bedId", transferDto.getToBedId());
			query.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query1=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
			query1.setParameter("updatedBy", transferDto.getUpdatedBy());
			query1.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query1.setParameter("unitId", transferDto.getUnitId());
			query1.setParameter("orgId", transferDto.getOrganizationId());
			query1.setParameter("bedId", transferDto.getToBedId());
			query1.executeUpdate();
			
			
			BedLogStatus bedLogStatusObj=new BedLogStatus();
			bedLogStatusObj.setBedId(transferDto.getToBedId());
			bedLogStatusObj.setBedStatusId(5);
			bedLogStatusObj.setStatus('A');
			bedLogStatusObj.setCreatedBy(transferDto.getUpdatedBy());
			bedLogStatusObj.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatusObj.setUpdatedBy(transferDto.getUpdatedBy());
			bedLogStatusObj.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatusObj.setOrganizationId(transferDto.getOrganizationId());
			bedLogStatusObj.setUnitId(transferDto.getUnitId());
			sessionFactory.getCurrentSession().save(bedLogStatusObj);
			
			Query transferQuery = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, "
					+ "  transfer.toWardId=:toWardId, transfer.toRoomId=:toRoomId, transfer.toBedId=:toBedId,"
					+ " transfer.transferStatusId=9,transfer.status='I' "
					+ "  WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
			transferQuery.setParameter("toWardId", transferDto.getToWardId());
			transferQuery.setParameter("toRoomId", transferDto.getToRoomId());
			transferQuery.setParameter("toBedId", transferDto.getToBedId());
			transferQuery.setParameter("transferId", transferDto.getTransferId());
			transferQuery.setParameter("updatedBy", transferDto.getUpdatedBy());
			transferQuery.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			transferQuery.setParameter("unitId", transferDto.getUnitId());
			transferQuery.setParameter("orgId", transferDto.getOrganizationId());
			transferQuery.executeUpdate();
			
			Query querydata = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, transfer.transferStatusId=9,transfer.status='I' WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
			querydata.setParameter("transferId", transferDto.getTransferId());
			querydata.setParameter("updatedBy", transferDto.getUpdatedBy());
			querydata.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			querydata.setParameter("unitId", transferDto.getUnitId());
			querydata.setParameter("orgId", transferDto.getOrganizationId());
			querydata.executeUpdate();
			
			Query query2=session.createQuery("update TransferRequest traReq set  traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.transferStatusId=9,traReq.status='I' WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
				query2.setParameter("updatedBy", transferDto.getUpdatedBy());
				query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				query2.setParameter("unitId", transferDto.getUnitId());
				query2.setParameter("orgId", transferDto.getOrganizationId());
				query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
			query2.executeUpdate();
			
			
			Query query3=session.createQuery("update AdmissionDetails admt set  admt.updatedBy=:updatedBy,admt.updatedDate=:updatedDate,admt.floorId=:floorId,admt.wardId=:wardId,admt.bedId=:bedId,roomId=:roomId WHERE admt.admissionId=:admissionId AND admt.unitId=:unitId AND admt.organizationId=:orgId AND admt.status='A'");
				query3.setParameter("updatedBy", transferDto.getUpdatedBy());
				query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query3.setParameter("unitId", transferDto.getUnitId());
				query3.setParameter("orgId", transferDto.getOrganizationId());
				query3.setParameter("admissionId", transferDto.getAdmissionId());
				query3.setParameter("floorId", transferDto.getToFloorId());
				query3.setParameter("wardId", transferDto.getToWardId());
				query3.setParameter("roomId",transferDto.getToRoomId());
				query3.setParameter("bedId", transferDto.getToBedId());
			
			query3.executeUpdate();
			
			//<-------Releasing previous bed of a patient 
			Query query4=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=7 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
				query4.setParameter("updatedBy", transferDto.getUpdatedBy());
				query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query4.setParameter("unitId", transferDto.getUnitId());
				query4.setParameter("orgId", transferDto.getOrganizationId());
				query4.setParameter("bedId", transferDto.getFromBedId());
			query4.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query5=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
				query5.setParameter("updatedBy", transferDto.getUpdatedBy());
				query5.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query5.setParameter("unitId", transferDto.getUnitId());
				query5.setParameter("orgId", transferDto.getOrganizationId());
				query5.setParameter("bedId", transferDto.getFromBedId());
			query5.executeUpdate();
			
			BedLogStatus bedLogStatus=new BedLogStatus();
				bedLogStatus.setBedId(transferDto.getFromBedId());
				bedLogStatus.setBedStatusId(2);
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
				bedLogStatus.setUnitId(transferDto.getUnitId());
			session.save(bedLogStatus);
			//------>	
			
		Transfer transfer=new Transfer();
				transfer.setOrganizationId(transferDto.getOrganizationId());
				transfer.setUnitId(transferDto.getUnitId());
				transfer.setAdmissionId(transferDto.getAdmissionId());
				transfer.setFromBedCategoryId(transferDto.getToBedCategoryId());
				transfer.setFromBillingBedCategoryId(transferDto.getToBillingBedCategoryId());
				transfer.setFromWardId(transferDto.getToWardId());
				transfer.setFromRoomId(transferDto.getToRoomId());
				transfer.setFromBedId(transferDto.getToBedId());
				transfer.setFromDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
				transfer.setFromTime(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
				transfer.setCreatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
				transfer.setUpdatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
				transfer.setTransferStatusId(1);
				transfer.setIsPrimary('N');
				transfer.setStatus('A');
				transfer.setCreatedBy(transferDto.getUpdatedBy());
				transfer.setUpdatedBy(transferDto.getUpdatedBy());
			session.save(transfer);
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPendingTransferRequestListOfLaborBed(TransferDto transferDto) throws ApplicationException {
		try {
			
			Integer count=0;
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_LABOR_BED")
					.getQueryString();
			StringBuffer subQuery = new StringBuffer();
			
			if(transferDto.getWardList()!=null && transferDto.getWardList().size() > 0) {
				subQuery.append(" and bm.ward_id IN ( "+ transferDto.getWardList().toString().replace('[', '\n').replace(']', '\n')  +" )");
			}
			query = query.concat(subQuery.toString());
			List<TransferDto> pendingTransferRequestList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset()  : 0)
					.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			
			List<TransferDto> pendingTransferRequestList1 = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();

			 if(pendingTransferRequestList1 != null && pendingTransferRequestList1.size()>0) {
		    	  count = pendingTransferRequestList1.size();
		      }
			
			/*List<TransferDto> pendingTransferRequestList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_LABOR_BED")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset()  : 0)
					.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			
			List<TransferDto> pendingTransferRequestList1 = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_LABOR_BED")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();

			 if(pendingTransferRequestList1 != null && pendingTransferRequestList1.size()>0) {
		    	  count = pendingTransferRequestList1.size();
		      }*/
			
			return new Response(SUCCESS, null, null, pendingTransferRequestList, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response transferFromLaborBedToNormalBed(TransferDto transferDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			//<-------Booking requested bed so that it should not get allocated to any other 
			Query query=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=5 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
			query.setParameter("updatedBy", transferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query.setParameter("unitId", transferDto.getUnitId());
			query.setParameter("orgId", transferDto.getOrganizationId());
			query.setParameter("bedId", transferDto.getToBedId());
			query.executeUpdate();
			//----->
			
			if(transferDto.getIsTransferReqFromHemodialysis()!=null) {
				
				Query addmissionUpdateQuery=session.createQuery("update Admission add set  add.doctorId=:docId,add.specialityId=:specId,add.updatedBy=:updatedBy,"
						+ " add.updatedDate=:updatedDate "
						+ " WHERE add.admissionId=:addId AND add.unitId=:unitId AND add.organizationId=:orgId ");
				addmissionUpdateQuery.setParameter("specId", transferDto.getTreatingDoctorSpecialityId());
				addmissionUpdateQuery.setParameter("docId", transferDto.getTreatingDoctorId());
				addmissionUpdateQuery.setParameter("updatedBy", transferDto.getUpdatedBy());
				addmissionUpdateQuery.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				addmissionUpdateQuery.setParameter("unitId", transferDto.getUnitId());
				addmissionUpdateQuery.setParameter("orgId", transferDto.getOrganizationId());
				addmissionUpdateQuery.setParameter("addId", transferDto.getAdmissionId());
				addmissionUpdateQuery.executeUpdate();
				
				Query updateAppointQuery=session.createQuery("update AppointmentMaster appMaster set appMaster.appointmentStatusId=6,appMaster.updatedBy=:updatedBy,appMaster.updatedDate=:updatedDate "
						+ "WHERE appMaster.admissionId=:addId AND appMaster.unitId=:unitId AND appMaster.organizationId=:orgId");
				updateAppointQuery.setParameter("updatedBy", transferDto.getUpdatedBy());
				updateAppointQuery.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				updateAppointQuery.setParameter("unitId", transferDto.getUnitId());
				updateAppointQuery.setParameter("orgId", transferDto.getOrganizationId());
				updateAppointQuery.setParameter("addId", transferDto.getAdmissionId());
				updateAppointQuery.executeUpdate();
				
				}
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query1=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
			query1.setParameter("updatedBy", transferDto.getUpdatedBy());
			query1.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			query1.setParameter("unitId", transferDto.getUnitId());
			query1.setParameter("orgId", transferDto.getOrganizationId());
			query1.setParameter("bedId", transferDto.getToBedId());
			query1.executeUpdate();
			
			
			BedLogStatus bedLogStatusObj=new BedLogStatus();
			bedLogStatusObj.setBedId(transferDto.getToBedId());
			bedLogStatusObj.setBedStatusId(5);
			bedLogStatusObj.setStatus('A');
			bedLogStatusObj.setCreatedBy(transferDto.getUpdatedBy());
			bedLogStatusObj.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatusObj.setUpdatedBy(transferDto.getUpdatedBy());
			bedLogStatusObj.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatusObj.setOrganizationId(transferDto.getOrganizationId());
			bedLogStatusObj.setUnitId(transferDto.getUnitId());
			sessionFactory.getCurrentSession().save(bedLogStatusObj);
			
			Query transferQuery = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, "
					+ "  transfer.toWardId=:toWardId, transfer.toRoomId=:toRoomId, transfer.toBedId=:toBedId,"
					+ " transfer.transferStatusId=9,transfer.status='I' "
					+ "  WHERE transfer.admissionId=:admissionId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId AND transfer.status='A' ");
			transferQuery.setParameter("toWardId", transferDto.getToWardId());
			transferQuery.setParameter("toRoomId", transferDto.getToRoomId());
			transferQuery.setParameter("toBedId", transferDto.getToBedId());
			transferQuery.setParameter("admissionId", transferDto.getAdmissionId());
			transferQuery.setParameter("updatedBy", transferDto.getUpdatedBy());
			transferQuery.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			transferQuery.setParameter("unitId", transferDto.getUnitId());
			transferQuery.setParameter("orgId", transferDto.getOrganizationId());
			transferQuery.executeUpdate();
		
			
			Query query3=session.createQuery("update AdmissionDetails admt set  admt.updatedBy=:updatedBy,admt.updatedDate=:updatedDate,"
					+ " admt.wardId=:wardId,admt.bedId=:bedId,roomId=:roomId WHERE admt.admissionId=:admissionId AND admt.unitId=:unitId AND admt.organizationId=:orgId AND admt.status='A' ");
				query3.setParameter("updatedBy", transferDto.getUpdatedBy());
				query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query3.setParameter("unitId", transferDto.getUnitId());
				query3.setParameter("orgId", transferDto.getOrganizationId());
				query3.setParameter("admissionId", transferDto.getAdmissionId());
				query3.setParameter("wardId", transferDto.getToWardId());
				query3.setParameter("roomId",transferDto.getToRoomId());
				query3.setParameter("bedId", transferDto.getToBedId());
			
			query3.executeUpdate();
			
			//<-------Releasing previous bed of a patient 
			Query query4=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=3 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
				query4.setParameter("updatedBy", transferDto.getUpdatedBy());
				query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query4.setParameter("unitId", transferDto.getUnitId());
				query4.setParameter("orgId", transferDto.getOrganizationId());
				query4.setParameter("bedId", transferDto.getFromBedId());
			query4.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query5=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
				query5.setParameter("updatedBy", transferDto.getUpdatedBy());
				query5.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				query5.setParameter("unitId", transferDto.getUnitId());
				query5.setParameter("orgId", transferDto.getOrganizationId());
				query5.setParameter("bedId", transferDto.getFromBedId());
			query5.executeUpdate();
			
			BedLogStatus bedLogStatus=new BedLogStatus();
				bedLogStatus.setBedId(transferDto.getFromBedId());
				bedLogStatus.setBedStatusId(3);
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
				bedLogStatus.setUnitId(transferDto.getUnitId());
			session.save(bedLogStatus);
			//------>	
			
		Transfer transfer=new Transfer();
				transfer.setOrganizationId(transferDto.getOrganizationId());
				transfer.setUnitId(transferDto.getUnitId());
				transfer.setAdmissionId(transferDto.getAdmissionId());
				transfer.setFromBedCategoryId(transferDto.getToBedCategoryId());
				transfer.setFromBillingBedCategoryId(transferDto.getToBillingBedCategoryId());
				transfer.setFromWardId(transferDto.getToWardId());
				transfer.setFromRoomId(transferDto.getToRoomId());
				transfer.setFromBedId(transferDto.getToBedId());
				transfer.setFromDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setFromTime(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setTransferStatusId(1);
				transfer.setIsPrimary('N');
				transfer.setStatus('A');
				transfer.setCreatedBy(transferDto.getUpdatedBy());
				transfer.setUpdatedBy(transferDto.getUpdatedBy());
				if(transferDto.getTreatingDoctorId()!=null) {
					transfer.setTreatingDoctorId(transferDto.getDoctorId());
					transfer.setTreatingDoctorSpecialityId(transferDto.getSpecialityId());
				}
			session.save(transfer);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getPreviousAllocatedBedDetails(TransferDto transferDto) throws ApplicationException {
		try {
			TransferDto dto = (TransferDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PREVIOUS_ALLOCATED_BED")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("admissionId", transferDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, dto);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPendingTransferRequestListOfHemodialysisBed(TransferDto transferDto)
			throws ApplicationException {
		try {
			List<TransferDto> pendingTransferRequestList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PENDING_TRANSFER_REQ_LIST_FOR_HEMODIALYSIS_BED")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
			return new Response(SUCCESS, null, null, pendingTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptFinalHemodialysisRoomTransferRequest(TransferDto transferDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			//<-------Booking requested bed so that it should not get allocated to any other 
			Query query=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=5 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
			query.setParameter("updatedBy", transferDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			query.setParameter("unitId", transferDto.getUnitId());
			query.setParameter("orgId", transferDto.getOrganizationId());
			query.setParameter("bedId", transferDto.getToBedId());
			query.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query1=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
			query1.setParameter("updatedBy", transferDto.getUpdatedBy());
			query1.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			query1.setParameter("unitId", transferDto.getUnitId());
			query1.setParameter("orgId", transferDto.getOrganizationId());
			query1.setParameter("bedId", transferDto.getToBedId());
			query1.executeUpdate();
			
			
			BedLogStatus bedLogStatusObj=new BedLogStatus();
			bedLogStatusObj.setBedId(transferDto.getToBedId());
			bedLogStatusObj.setBedStatusId(5);
			bedLogStatusObj.setStatus('A');
			bedLogStatusObj.setCreatedBy(transferDto.getUpdatedBy());
			bedLogStatusObj.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatusObj.setUpdatedBy(transferDto.getUpdatedBy());
			bedLogStatusObj.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			bedLogStatusObj.setOrganizationId(transferDto.getOrganizationId());
			bedLogStatusObj.setUnitId(transferDto.getUnitId());
			sessionFactory.getCurrentSession().save(bedLogStatusObj);
			
			Query transferQuery = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, "
					+ "  transfer.toWardId=:toWardId, transfer.toRoomId=:toRoomId, transfer.toBedId=:toBedId,"
					+ " transfer.transferStatusId=9,transfer.status='I' "
					+ "  WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
			transferQuery.setParameter("toWardId", transferDto.getToWardId());
			transferQuery.setParameter("toRoomId", transferDto.getToRoomId());
			transferQuery.setParameter("toBedId", transferDto.getToBedId());
			transferQuery.setParameter("transferId", transferDto.getTransferId());
			transferQuery.setParameter("updatedBy", transferDto.getUpdatedBy());
			transferQuery.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			transferQuery.setParameter("unitId", transferDto.getUnitId());
			transferQuery.setParameter("orgId", transferDto.getOrganizationId());
			transferQuery.executeUpdate();
			
			Query querydata = session.createQuery(
					"update Transfer transfer set transfer.updatedBy=:updatedBy,transfer.updatedDate=:updatedDate, transfer.transferStatusId=9,transfer.status='I' WHERE transfer.transferId=:transferId AND transfer.unitId=:unitId AND transfer.organizationId=:orgId");
			querydata.setParameter("transferId", transferDto.getTransferId());
			querydata.setParameter("updatedBy", transferDto.getUpdatedBy());
			querydata.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
			querydata.setParameter("unitId", transferDto.getUnitId());
			querydata.setParameter("orgId", transferDto.getOrganizationId());
			querydata.executeUpdate();
			
			Query query2=session.createQuery("update TransferRequest traReq set  traReq.updatedBy=:updatedBy,traReq.updatedDate=:updatedDate,traReq.transferStatusId=9,traReq.status='I' WHERE traReq.transferRequestId=:transferRequestId AND traReq.unitId=:unitId AND traReq.organizationId=:orgId");
				query2.setParameter("updatedBy", transferDto.getUpdatedBy());
				query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				query2.setParameter("unitId", transferDto.getUnitId());
				query2.setParameter("orgId", transferDto.getOrganizationId());
				query2.setParameter("transferRequestId", transferDto.getTransferRequestId());
			query2.executeUpdate();
			
			
			Query query3=session.createQuery("update AdmissionDetails admt set  admt.updatedBy=:updatedBy,admt.updatedDate=:updatedDate,admt.floorId=:floorId,admt.wardId=:wardId,admt.bedId=:bedId,roomId=:roomId WHERE admt.admissionId=:admissionId AND admt.unitId=:unitId AND admt.organizationId=:orgId AND admt.status='A'");
				query3.setParameter("updatedBy", transferDto.getUpdatedBy());
				query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				query3.setParameter("unitId", transferDto.getUnitId());
				query3.setParameter("orgId", transferDto.getOrganizationId());
				query3.setParameter("admissionId", transferDto.getAdmissionId());
				query3.setParameter("floorId", transferDto.getToFloorId());
				query3.setParameter("wardId", transferDto.getToWardId());
				query3.setParameter("roomId",transferDto.getToRoomId());
				query3.setParameter("bedId", transferDto.getToBedId());
			
			query3.executeUpdate();
			
			//<-------Releasing previous bed of a patient 
			Query query4=session.createQuery("update BedMaster bm set bm.updatedBy=:updatedBy,bm.updatedDate=:updatedDate, bm.bedStatusId=7 WHERE bm.bedId=:bedId AND bm.unitId=:unitId AND bm.organizationId=:orgId");
				query4.setParameter("updatedBy", transferDto.getUpdatedBy());
				query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				query4.setParameter("unitId", transferDto.getUnitId());
				query4.setParameter("orgId", transferDto.getOrganizationId());
				query4.setParameter("bedId", transferDto.getFromBedId());
			query4.executeUpdate();
			//----->
			
			//<------Adding new entry in bed log status by in-activating previous active record
			Query query5=session.createQuery("update BedLogStatus bls set bls.updatedBy=:updatedBy,bls.updatedDate=:updatedDate, bls.status='I' WHERE bls.bedId=:bedId AND bls.unitId=:unitId AND bls.organizationId=:orgId AND bls.status='A'");
				query5.setParameter("updatedBy", transferDto.getUpdatedBy());
				query5.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				query5.setParameter("unitId", transferDto.getUnitId());
				query5.setParameter("orgId", transferDto.getOrganizationId());
				query5.setParameter("bedId", transferDto.getFromBedId());
			query5.executeUpdate();
			
			BedLogStatus bedLogStatus=new BedLogStatus();
				bedLogStatus.setBedId(transferDto.getFromBedId());
				bedLogStatus.setBedStatusId(2);
				bedLogStatus.setStatus('A');
				bedLogStatus.setCreatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedBy(transferDto.getUpdatedBy());
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setOrganizationId(transferDto.getOrganizationId());
				bedLogStatus.setUnitId(transferDto.getUnitId());
			session.save(bedLogStatus);
			//------>	
			
		Transfer transfer=new Transfer();
				transfer.setOrganizationId(transferDto.getOrganizationId());
				transfer.setUnitId(transferDto.getUnitId());
				transfer.setAdmissionId(transferDto.getAdmissionId());
				transfer.setFromBedCategoryId(transferDto.getToBedCategoryId());
				transfer.setFromBillingBedCategoryId(transferDto.getToBillingBedCategoryId());
				transfer.setFromWardId(transferDto.getToWardId());
				transfer.setFromRoomId(transferDto.getToRoomId());
				transfer.setFromBedId(transferDto.getToBedId());
				transfer.setFromDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setFromTime(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId():0),"dd-MM-yyyy HH:mm:ss"));
				transfer.setTransferStatusId(1);
				transfer.setIsPrimary('N');
				transfer.setStatus('A');
				transfer.setCreatedBy(transferDto.getUpdatedBy());
				transfer.setUpdatedBy(transferDto.getUpdatedBy());
			session.save(transfer);
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	@Override
	public Response getFinalTransferRequestListForNursingPageCount(TransferDto transferDto)
			throws ApplicationException {
		try {

			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_FINAL_TRANSFER_REQUEST_LIST")
					.getQueryString();

			String finalQuery;
			List<TransferDto> pendingTransferRequestList=null;
			
			if(transferDto.getIsFromNursingAcceptancePage()!=null && transferDto.getIsFromNursingAcceptancePage()=='Y') {
		
				if (transferDto.getIsShowPatientByBed().equals('N')) {
					pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wardList", transferDto.getWardList())
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
				} else {
					finalQuery = query.replace("transfer.from_ward_id IN (:wardList)",
							"transfer.from_bed_id IN (:bedList)");
					pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("bedList", transferDto.getBedList())
							.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
				}
				
			}
			Integer count=0;
			if(pendingTransferRequestList!=null && pendingTransferRequestList.size()>0) {
				count=pendingTransferRequestList.size();
			}
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response universalSearchForTransferRequestList(TransferDto transferDto) throws ApplicationException {
		try {

			String query = sessionFactory.getCurrentSession().getNamedQuery("UNIVERSAL_SEARCH_FOR_TRANSFER_REQUEST_LIST")
					.getQueryString();

			String finalQuery;
			List<TransferDto> pendingTransferRequestList = null;
			 if(transferDto.getIsFromNursingAcceptancePage()!=null && transferDto.getIsFromNursingAcceptancePage()=='Y') {
				 
				 if (transferDto.getIsShowPatientByBed().equals('N')) {
						pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setParameter("organizationId", transferDto.getOrganizationId())
								.setParameter("unitId", transferDto.getUnitId())
								.setParameterList("wardList", transferDto.getWardList())
								.setParameter("patientName", "%"+transferDto.getPatientName().trim().toLowerCase()+"%")
								.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
								.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					} else {
						finalQuery = query.replace("transfer.from_ward_id IN (:wardList)",
								"transfer.from_bed_id IN (:bedList)");
						pendingTransferRequestList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
								.setParameter("organizationId", transferDto.getOrganizationId())
								.setParameter("unitId", transferDto.getUnitId())
								.setParameterList("bedList", transferDto.getBedList())
								.setParameter("patientName", "%"+transferDto.getPatientName().trim().toLowerCase()+"%")
								.setFirstResult(transferDto.getOffset() != null ? transferDto.getOffset() : 0)
								.setMaxResults(transferDto.getNoOfRecordsPerPage() != null ? transferDto.getNoOfRecordsPerPage() : 10)
								.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).list();
					}
			 } 
			return new Response(SUCCESS, null, null, pendingTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	
}


