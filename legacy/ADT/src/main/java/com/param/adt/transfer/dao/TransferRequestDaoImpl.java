package com.param.adt.transfer.dao;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.templet.dto.AcceptedTransferCareDto;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.model.TransferRequest;
import com.param.global.dto.AdmissionDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TransferRequestDaoImpl extends GenericDao<TransferRequest, Integer>
		implements ITransferRequestDao, ICommonConstants {

	public TransferRequestDaoImpl() {
		super(TransferRequest.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getTransferRequestList(TransferDto transferDto) throws ApplicationException {
		try{
		String searchKeyword = transferDto.getSearchKeyword();
		String query = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_TRANSFER_REQ_LIST").getQueryString();

		StringBuffer subQuery = new StringBuffer();
		StringBuffer subQueryForCount = new StringBuffer();
		String query1 = query ;
		int count = 0;
		List<AdmissionDto> transferRequestListForCount = new ArrayList();
		String finalQuery;
		List<AdmissionDto> transferRequestList;
		if (transferDto.getBedList()!=null) {
			finalQuery = query.replace("transfer.from_ward_id IN :wards", "transfer.from_bed_id IN :bedList");
			String finalQuery1 = finalQuery;

			if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

				subQuery.append(" AND ( "
						+ " lower(pr.uhid_number) like :searchKeyword "
						+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name))) like :searchKeyword "
						+ " OR lower(ward.ward_name) like :searchKeyword "
						+ " OR lower(bm.bed_number) like :searchKeyword "
						+ " OR lower(bm2.bed_number) like :searchKeyword "
						+ " OR lower(bm.bed_number) like :searchKeyword "
						+ " ) ");
			}

			subQueryForCount.append(" AND transfer.transfer_status_id!=1  AND traReq.transfer_type_id=2 ");
			finalQuery1 = finalQuery1.concat(subQuery.toString());
			finalQuery1 = finalQuery1.concat(subQueryForCount.toString());

			transferRequestListForCount = sessionFactory.getCurrentSession().createSQLQuery(finalQuery1.toString())
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameterList("bedList", transferDto.getBedList())
					.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			if(transferDto.getOffset()!=null && transferDto.getNoOfRecordsPerPage()!=null) {
				subQuery.append(" AND transfer.transfer_status_id!=1  AND traReq.transfer_type_id=2 ");
				subQuery.append(" LIMIT "+transferDto.getNoOfRecordsPerPage()+" OFFSET "+transferDto.getOffset());
			}
			finalQuery = finalQuery.concat(subQuery.toString());
			
			transferRequestList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameterList("bedList", transferDto.getBedList())
					.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%")
					//.setParameter("transferTypeId",transferDto.getTransferTypeId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

		} else {

			if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

				subQuery.append(" AND ( "
						+ " lower(pr.uhid_number) like :searchKeyword "
						+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name))) like :searchKeyword "
						+ " OR lower(ward.ward_name) like :searchKeyword "
						+ " OR lower(bm.bed_number) like :searchKeyword "
						+ " OR lower(bm2.bed_number) like :searchKeyword "
						+ " OR lower(bm.bed_number) like :searchKeyword "
						+ " ) ");
			}

			StringBuffer subQuery1 = new StringBuffer();
			subQuery1.append(" AND transfer.transfer_status_id!=1  AND traReq.transfer_type_id=2 ");

			query1 = query1.concat(subQuery.toString());
			query1 = query1.concat(subQuery1.toString());

			transferRequestListForCount = sessionFactory.getCurrentSession().createSQLQuery(query1.toString())
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameterList("wards", transferDto.getWardList())
					.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			if(transferDto.getOffset()!=null && transferDto.getNoOfRecordsPerPage()!=null) {
				subQuery.append(" AND transfer.transfer_status_id!=1  AND traReq.transfer_type_id=2 ");
				subQuery.append(" LIMIT "+transferDto.getNoOfRecordsPerPage()+" OFFSET "+transferDto.getOffset());
			}
			query = query.concat(subQuery.toString());
			
			transferRequestList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameterList("wards", transferDto.getWardList())
					.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%")
					//.setParameter("transferTypeId",transferDto.getTransferTypeId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

		}
		if(transferRequestListForCount!=null && transferRequestListForCount.size()>0) {
			count = transferRequestListForCount.size();
		}
		return new Response(SUCCESS, null, null, transferRequestList, count);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveTransferRequest(TransferDto transferDto) throws ApplicationException {
		try {

			TransferRequest transferRequest = mapper.map(transferDto, TransferRequest.class,"TransferDto_to_TransferRequest");
				transferRequest.setCreatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
				transferRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
			transferRequest.setStatus('A');
			transferRequest = save(transferRequest);
			if(transferDto.getTransferTypeId()==1)
			{
				// <----- Updating transfer status
				Session session = sessionFactory.getCurrentSession();
				// Transaction tr =session.beginTransaction();
				Query query = session.createQuery("update Transfer tr set "
						+ "tr.toBedCategoryId = :toBedCategoryId, "
						+ "tr.transferRequestId = :transferRequestId, "
						+ "tr.transferStatusId = :transferStatusId "
						+ "where tr.transferId = :transferId");
				query.setParameter("toBedCategoryId", transferDto.getToBedCategoryId());
				query.setParameter("transferRequestId", transferRequest.getTransferRequestId());
				query.setParameter("transferStatusId", transferDto.getTransferStatusId());
				query.setParameter("transferId", transferDto.getTransferId());
				query.executeUpdate();
				// tr.commit();
				// ----->
			}
			else
			{
				// <----- Updating transfer status
				Session session = sessionFactory.getCurrentSession();
				// Transaction tr =session.beginTransaction();
				Query query = session.createQuery("update Transfer tr set "
						+ " tr.toBedCategoryId=:toBedCategoryId, "
						+ "	tr.toBedId=:toBedId, "
						+ " tr.toBillingBedCategoryId=:toBillingBedCategoryId, "
						+ " tr.toWardId=:toWardId, "
						+ " tr.toRoomId=:toRoomId, "
						+ " tr.transferRequestId=:transferRequestId, "
						+ " tr.transferStatusId=:transferStatusId "
						+ " where tr.transferId=:transferId ");
				query.setParameter("toBedCategoryId", transferDto.getToBedCategoryId());
				query.setParameter("toBillingBedCategoryId", transferDto.getToBillingBedCategoryId());
				query.setParameter("toBedId", transferDto.getToBedId());
				query.setParameter("toWardId", transferDto.getToWardId());
				query.setParameter("toRoomId", transferDto.getToRoomId());
				query.setParameter("transferRequestId", transferRequest.getTransferRequestId());
				query.setParameter("transferStatusId", transferDto.getTransferStatusId());
				query.setParameter("transferId", transferDto.getTransferId());
				query.executeUpdate();
				// tr.commit();
				// ----->
			}
			
            if(transferDto.getNewVisitTypeId()!=null) {
            	
            	Session session = sessionFactory.getCurrentSession();
				// Transaction tr =session.beginTransaction();
				Query query = session.createQuery("update Admission adt set "
						+ " adt.visitTypeId=:newVisitTypeId , "
						+ "	adt.doctorId=:treatingDoctorId, "
						+ " adt.specialityId=:specialityId "
						+ " where adt.admissionId=:admissionId "
						+ " and adt.unitId=:unitId "
						+ " and adt.organizationId=:organizationId "
						);
				query.setParameter("newVisitTypeId", transferDto.getNewVisitTypeId());
				query.setParameter("treatingDoctorId", transferDto.getTreatingDoctorId());
				query.setParameter("specialityId", transferDto.getSpecialityId());
				query.setParameter("admissionId", transferDto.getAdmissionId());
				query.setParameter("unitId", transferDto.getUnitId());
				query.setParameter("organizationId", transferDto.getOrganizationId());
				query.executeUpdate();
				
	          /*sessionFactory.getCurrentSession().
	    		 createSQLQuery(" update public.t_order_master  set visit_type_id=:visitTypeId where admission_id=:admissionId "	)
	   			.setParameter("visitTypeId",transferDto.getNewVisitTypeId())
	   			.setParameter("admissionId", transferDto.getAdmissionId()).executeUpdate();*/
               }

			return new Response(SUCCESS, null, null, null, transferRequest);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getTransferRequestListByAdmissionId(TransferDto transferDto) throws ApplicationException {
		try{
			int count  = 0;
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TRANSFER_REQ_LIST_BY_ADMISSION_ID_FOR_B2B")
					.getQueryString();
			StringBuffer subQuery = new StringBuffer();
			if(transferDto.getOffset()!=null && transferDto.getNoOfRecordsPerPage()!=null) {
				subQuery.append(" LIMIT "+transferDto.getNoOfRecordsPerPage()+" OFFSET "+transferDto.getOffset());
			}
			query = query.concat(subQuery.toString());
			
			List<AdmissionDto> transferRequestList = sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.setParameter("organizationId", transferDto.getOrganizationId())
				.setParameter("unitId", transferDto.getUnitId())
				.setParameter("admissionId", transferDto.getAdmissionId())
				//.setParameter("transferTypeId",transferDto.getTransferTypeId())
				.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
			if(transferRequestList!=null && transferRequestList.size()>0) {
			List<AdmissionDto> transferRequestListForCount = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TRANSFER_REQ_LIST_BY_ADMISSION_ID_FOR_B2B")
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("admissionId", transferDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				count = transferRequestListForCount.size();
			}	
		return new Response(SUCCESS, null, null, transferRequestList, count);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response acceptedTransferOfCareList(AcceptedTransferCareDto acceptedTransferCareDto) throws ApplicationException {
		
		try{
			Integer count=0;
			
			List<AcceptedTransferCareDto> acceptedTransferOfCareList = sessionFactory.getCurrentSession()
		        .getNamedQuery("ACCEPTED_TRANSFER_OF_CARE_LIST")
				.setParameter("orgId", acceptedTransferCareDto.getOrganizationId())
				.setParameter("unitId", acceptedTransferCareDto.getUnitId())
				.setParameter("doctorId", acceptedTransferCareDto.getDoctorId())
				.setFirstResult(acceptedTransferCareDto.getOffset() != null ? acceptedTransferCareDto.getOffset()  : 0)
				.setMaxResults(acceptedTransferCareDto.getNoOfRecordsPerPage() != null ? acceptedTransferCareDto.getNoOfRecordsPerPage()  : 10)
				.setResultTransformer(Transformers.aliasToBean(AcceptedTransferCareDto.class)).list();
			
			List<AcceptedTransferCareDto> acceptedTransferOfCareList1 = sessionFactory.getCurrentSession()
			        .getNamedQuery("ACCEPTED_TRANSFER_OF_CARE_LIST")
					.setParameter("orgId", acceptedTransferCareDto.getOrganizationId())
					.setParameter("unitId", acceptedTransferCareDto.getUnitId())
					.setParameter("doctorId", acceptedTransferCareDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(AcceptedTransferCareDto.class)).list();
			
			 if(acceptedTransferOfCareList1 != null && acceptedTransferOfCareList1.size()>0) {
		    	  count = acceptedTransferOfCareList1.size();
		      }
			 
		    return new Response(SUCCESS, null, null, acceptedTransferOfCareList, count);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateclinicatnotes(AcceptedTransferCareDto acceptedTransferCareDto) throws ApplicationException {
		try {
	      Integer count=sessionFactory.getCurrentSession().
	    		 createSQLQuery(" update adt.t_transfer_of_care_request  set discharge_summary_note=:notes where transfer_of_care_id=:transferOfCareId "	)
	   			.setParameter("transferOfCareId", acceptedTransferCareDto.getTransferOfCareId())
	   			.setParameter("notes", acceptedTransferCareDto.getNote()).executeUpdate();
	      
			return new Response<>(SUCCESS, null, null, null, null);
		}   catch(Exception e){
			throw e;
		}
			}

	@Override
	public Response allTransferOfcareNotes(AcceptedTransferCareDto acceptedTransferCareDto)
			throws ApplicationException {
		try{
			List<AcceptedTransferCareDto> acceptedTransferOfCareList = sessionFactory.getCurrentSession()
		        .createSQLQuery("SELECT tcr.transfer_of_care_id as\"transferOfCareId\",tcr.admission_id as\"admissionId\", "
		        		+ " tcr.request_by as\"requestFrom\", tcr.request_to as\"requestTo\","
		        		+ " tcr.note as\"note\", tcr.discharge_summary_note as\"dischargeSummaryNote\""
		        		+ " FROM adt.t_transfer_of_care_request tcr "
		        		+ " where admission_id=:admissionId ")
				.setParameter("admissionId", acceptedTransferCareDto.getAdmissionId())
				.setResultTransformer(Transformers.aliasToBean(AcceptedTransferCareDto.class)).list();
		return new Response(SUCCESS, null, null, acceptedTransferOfCareList, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveLaborTransferRequest(TransferDto transferDto)
			throws ApplicationException {
		try{
			TransferRequest transferRequest = mapper.map(transferDto, TransferRequest.class, "TransferDto_to_TransferRequest");
			transferRequest.setCreatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
			transferRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(transferDto.getUnitId() != null ? transferDto.getUnitId() : 0));
			transferRequest = save(transferRequest);

			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("update Transfer tr set tr.toBedCategoryId=:toBedCategoryId, tr.transferRequestId=:transferRequestId, "
					+ "tr.transferStatusId=:transferStatusId where tr.admissionId=:admissionId and tr.status='A'");

			query.setParameter("toBedCategoryId", transferDto.getToBedCategoryId());
			query.setParameter("transferRequestId", transferRequest.getTransferRequestId());
			query.setParameter("transferStatusId", transferDto.getTransferStatusId());
			query.setParameter("admissionId", transferDto.getAdmissionId());
			query.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response checkTransferRequestExistence(TransferDto transferDto) throws ApplicationException {
		try{
			 
		TransferDto dto =(TransferDto) sessionFactory.getCurrentSession()
		        .createSQLQuery(" select tran.transfer_id as \"transferId\" "
		        		+ " from adt.t_transfer tran where  tran.status='A' and tran.admission_id=:admissionId and tran.organization_id=:orgId "
		        		+ " and tran.unit_id=:unitId and tran.transfer_status_id=4  "
		        		)
				.setParameter("admissionId", transferDto.getAdmissionId())
				.setParameter("orgId", transferDto.getOrganizationId())
				.setParameter("unitId", transferDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TransferDto.class)).uniqueResult();
		if(dto!=null){
		    return new Response(SUCCESS, null, null, null, dto);
		}else{
			return new Response(ERROR, null, null, null, null);
		 }
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

		
}
