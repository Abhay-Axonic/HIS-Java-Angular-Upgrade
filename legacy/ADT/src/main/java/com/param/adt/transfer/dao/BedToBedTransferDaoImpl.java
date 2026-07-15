package com.param.adt.transfer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.transfer.dto.TransferDetailsByAdmDto;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.model.Transfer;
import com.param.global.dto.AdmissionDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class BedToBedTransferDaoImpl extends GenericDao<Transfer, Integer> implements IBedToBedTransferDao,ICommonConstants {

	public BedToBedTransferDaoImpl() {
		super(Transfer.class);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Response getInitiateTransferRequestList(TransferDto transferDto) throws ApplicationException {
		try {
			String searchKeyword = transferDto.getSearchKeyword();
			if (transferDto.getWardList().size() > 0) {
				String query = sessionFactory.getCurrentSession().getNamedQuery("GET_INITIATE_TRANSFER_REQ_LIST")
						.getQueryString();

				StringBuffer subQuery = new StringBuffer();
				String finalQuery;
				String query1 = query ;
				int count = 0;
			
				List<TransferDto> pendingTransferRequestList;
				List<AdmissionDto> pendingTransferRequestListCount= new ArrayList<>();
				if (transferDto.getBedList() != null) {
					finalQuery = query.replace("transfer.from_ward_id IN :wards", "transfer.from_bed_id IN :bedList");

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						subQuery.append(" AND ( "
								+ " lower(pr.uhid_number) like :searchKeyword "
								+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name))) like :searchKeyword "
								+ " OR lower(bm.bed_number) like :searchKeyword "
								+ " ) ");
					}

					subQuery.append(" order by \"pFirstName\" ");
					finalQuery = finalQuery.concat(subQuery.toString());
					Query q = sessionFactory.getCurrentSession()
							.createSQLQuery(finalQuery.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("bedList", transferDto.getBedList());

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						q.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");
					}
					pendingTransferRequestList = q.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

				} else {

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						subQuery.append(" AND ( "
								+ " lower(pr.uhid_number) like :searchKeyword "
								+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name))) like :searchKeyword "
								+ " OR lower(bm.bed_number) like  :searchKeyword "
								+ " ) ");

					}

					subQuery.append(" order by \"pFirstName\" ");
					query = query.concat(subQuery.toString());
					subQuery.setLength(0);

					Query q = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wards", transferDto.getWardList());

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						q.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");
					}
					pendingTransferRequestListCount = q.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
					
					if(transferDto.getOffset()!=null && transferDto.getNoOfRecordsPerPage()!=null) {
						subQuery.append(" LIMIT "+transferDto.getNoOfRecordsPerPage()+" OFFSET "+transferDto.getOffset());
					}
					query = query.concat(subQuery.toString());

					Query q1  = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wards", transferDto.getWardList());

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						q1.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");
					}
					pendingTransferRequestList = q1.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				}


				return new Response(SUCCESS, null, null, pendingTransferRequestList, pendingTransferRequestListCount.size());
			}
		} catch (HibernateException he) {
			he.printStackTrace();
		
		} catch (Exception e) {
			throw e;
		
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getInitiateTransferRequestCount(TransferDto transferDto) {
		try{

			String searchKeyword = transferDto.getSearchKeyword();
			if (transferDto.getWardList().size() > 0) {
				String query = sessionFactory.getCurrentSession().getNamedQuery("GET_INITIATE_TRANSFER_REQ_COUNT")
						.getQueryString();

				Object count = 0;
				StringBuffer subQuery = new StringBuffer();
				String finalQuery;
				if (transferDto.getBedList() != null) {
					finalQuery = query.replace("transfer.from_ward_id IN :wards", "transfer.from_bed_id IN :bedList");

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						subQuery.append(" AND ( "
								+ " lower(pr.uhid_number) like :searchKeyword "
								+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name))) like :searchKeyword "
								+ " OR lower(bm.bed_number) like :searchKeyword "
								+ " ) ");
					}

					finalQuery = finalQuery.concat(subQuery.toString());

					Query q = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("bedList", transferDto.getBedList());


					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						q.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");
					}
					q.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class));
					count = q.uniqueResult();

				} else {

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {

						subQuery.append(" AND ( "
								+ " lower(pr.uhid_number) like :searchKeyword "
								+ " OR lower(concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name))) like :searchKeyword "
								+ " OR lower(bm.bed_number) like :searchKeyword "
								+ " ) ");
					}

					query = query.concat(subQuery.toString());

					Query q =  sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", transferDto.getOrganizationId())
							.setParameter("unitId", transferDto.getUnitId())
							.setParameterList("wards", transferDto.getWardList());

					if(searchKeyword!=null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {
						q.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");
					}

					count = q.uniqueResult();

				}

				return new Response(SUCCESS, null, null, null, count);
			}

		}catch (Exception e){
			throw e;
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getBedListByWardId(BedMasterDto bedMasterDto) {
		try {
			List<BedMasterDto> pendingTransferRequestList = sessionFactory.getCurrentSession()

					.getNamedQuery("GET_BED_LIST_BY_WARD_ID")
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setParameter("wardId", bedMasterDto.getWardId())
					.setResultTransformer(Transformers.aliasToBean(BedMasterDto.class)).list();
			return new Response(SUCCESS, null, null, pendingTransferRequestList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getTransferListByAdmissionId(Integer admissionId)
			throws ApplicationException {
		
		try {
			List<TransferDetailsByAdmDto> listTransferDetailsbyAdmDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_B2B_TRANSFER_DETAILS_BY_ADMISSIONID")
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(TransferDetailsByAdmDto.class)).list();
			return new Response(SUCCESS, null, null, listTransferDetailsbyAdmDto, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
