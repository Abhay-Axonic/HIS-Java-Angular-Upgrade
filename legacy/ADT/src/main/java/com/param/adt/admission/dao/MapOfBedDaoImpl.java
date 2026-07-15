package com.param.adt.admission.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.model.Transfer;
import com.param.global.dto.AdmissionDto;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MapOfBedDaoImpl extends GenericDao<Transfer, Integer> implements IMapOfBedDao,ICommonConstants 
{

	public MapOfBedDaoImpl() {
		super(Transfer.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getTransferRequestListByAdmissoinId(TransferDto transferDto) {
		try {
			int  count = 0;
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TRANSFER_REQ_LIST_BY_ADMISSION_ID")
					.getQueryString();
			StringBuffer subQuery = new StringBuffer();
			if(transferDto.getOffset()!=null && transferDto.getNoOfRecordsPerPage()!=null) {
				subQuery.append(" LIMIT :noOfRecordsPerPage OFFSET :offset");
			}
			if(transferDto.getWardList()!=null && transferDto.getWardList().size() > 0) {
				subQuery.append(" and bm.ward_id IN (:wardList)");
			}
			query = query.concat(subQuery.toString());
			Query sql = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("organizationId", transferDto.getOrganizationId())
					.setParameter("unitId", transferDto.getUnitId())
					.setParameter("admissionId", transferDto.getAdmissionId());
			if(transferDto.getOffset()!=null && transferDto.getNoOfRecordsPerPage()!=null) {
				sql.setParameter("offset", transferDto.getOffset());
				sql.setParameter("noOfRecordsPerPage", transferDto.getNoOfRecordsPerPage());
			}
			if(transferDto.getWardList()!=null && transferDto.getWardList().size() > 0) {
				sql.setParameter("wardList", transferDto.getWardList());
			}
			List<TransferDto> pendingTransferRequestList= sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
			if(pendingTransferRequestList!=null && pendingTransferRequestList.size()>0) {
				Query sql2 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_TRANSFER_REQ_LIST_BY_ADMISSION_ID")
						.setParameter("organizationId", transferDto.getOrganizationId())
						.setParameter("unitId", transferDto.getUnitId())
						.setParameter("admissionId", transferDto.getAdmissionId());
				if(transferDto.getOffset()!=null && transferDto.getNoOfRecordsPerPage()!=null) {
					sql.setParameter("offset", transferDto.getOffset());
					sql.setParameter("noOfRecordsPerPage", transferDto.getNoOfRecordsPerPage());
				}
				if(transferDto.getWardList()!=null && transferDto.getWardList().size() > 0) {
					sql.setParameter("wardList", transferDto.getWardList());
				}
				List<TransferDto> pendingTransferRequestListForCount= sql2.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				count = pendingTransferRequestListForCount.size();
			}
			return new Response(SUCCESS, null, null, pendingTransferRequestList, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
