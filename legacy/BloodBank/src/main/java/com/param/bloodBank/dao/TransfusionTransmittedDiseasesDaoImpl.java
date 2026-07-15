package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBagCapacityMasterDto;
import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDetailsDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.bloodBank.model.TransfusionTransmittedDiseases;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureSchedulingDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class TransfusionTransmittedDiseasesDaoImpl extends GenericDao<TransfusionTransmittedDiseases, Integer> implements ITransfusionTransmittedDiseasesDao , ICommonConstants{

	final static Logger logger = Logger.getLogger(TransfusionTransmittedDiseasesDaoImpl.class);


	
	public TransfusionTransmittedDiseasesDaoImpl() {
		super(TransfusionTransmittedDiseases.class);
	}
	

	@Autowired
	SessionFactory sessionFactory;
	

	@Override
	public Response saveTransfusionTransmittedDiseases(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException {
		
		try {
				TransfusionTransmittedDiseases transfusionTransmittedDiseases = new TransfusionTransmittedDiseases();
				transfusionTransmittedDiseases.setBarcode(transfusionTransmittedDiseasesDto.getBarcode()!=null?transfusionTransmittedDiseasesDto.getBarcode():null );
				transfusionTransmittedDiseases.setBloodBagCollectionId(transfusionTransmittedDiseasesDto.getBloodBagCollectionId());
				transfusionTransmittedDiseases.setBloodBagNo(transfusionTransmittedDiseasesDto.getBloodBagNo());
				transfusionTransmittedDiseases.setBloodGroupId(transfusionTransmittedDiseasesDto.getBloodGroupId());
				transfusionTransmittedDiseases.setCreatedBy(transfusionTransmittedDiseasesDto.getCreatedBy());
				transfusionTransmittedDiseases.setCreatedDate(GlobalCommonDateUtils.getDate(transfusionTransmittedDiseasesDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				transfusionTransmittedDiseases.setOrganizationId(transfusionTransmittedDiseasesDto.getOrganizationId());
				transfusionTransmittedDiseases.setStatus('A');
				transfusionTransmittedDiseases.setTransfusionTransmittedDiseasesStatus(transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesStatus());
				transfusionTransmittedDiseases.setTtdDoneById(transfusionTransmittedDiseasesDto.getTtdDoneById());
				transfusionTransmittedDiseases.setUnitId(transfusionTransmittedDiseasesDto.getUnitId());
				transfusionTransmittedDiseases.setUpdatedBy(transfusionTransmittedDiseasesDto.getCreatedBy());
				transfusionTransmittedDiseases.setUpdatedDate(GlobalCommonDateUtils.getDate(transfusionTransmittedDiseasesDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				transfusionTransmittedDiseases.setDonorId(transfusionTransmittedDiseasesDto.getDonorId()!=null?transfusionTransmittedDiseasesDto.getDonorId():null);
				transfusionTransmittedDiseases.setBloodBankStockId(transfusionTransmittedDiseasesDto.getBloodBankStockId()!=null?transfusionTransmittedDiseasesDto.getBloodBankStockId():null);
				transfusionTransmittedDiseases.setDonorNumber(transfusionTransmittedDiseasesDto.getDonorNumber());
				transfusionTransmittedDiseases.settTDCheck(transfusionTransmittedDiseasesDto.gettTDCheck()!=null?transfusionTransmittedDiseasesDto.gettTDCheck():false);
				transfusionTransmittedDiseases=save(transfusionTransmittedDiseases);
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, transfusionTransmittedDiseases);
		}
		 catch (Exception e) {
		 	logger.error("saveTransfusionTransmittedDiseases failed", e);
		 	throw new ServiceException(e.getMessage());
		 }
	}
	
	@Override
	public Response checkUniqueReq(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)throws ApplicationException {
		try {
			TransfusionTransmittedDiseasesDto dto= (TransfusionTransmittedDiseasesDto)sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_UNIQUE_REC")
				.setParameter("bloodBagNo",transfusionTransmittedDiseasesDto.getBloodBagNo() )
				.setParameter("barCode",transfusionTransmittedDiseasesDto.getBarcode())
				.setResultTransformer(Transformers.aliasToBean(TransfusionTransmittedDiseasesDto.class)).uniqueResult();
			
			
				return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
			
			} catch (HibernateException he) {
				logger.error("checkUniqueReq failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("checkUniqueReq failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response searchTTD(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_TTD_DETAILS").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(transfusionTransmittedDiseasesDto.getFromDate()!=null && transfusionTransmittedDiseasesDto.getToDate()!=null && transfusionTransmittedDiseasesDto.getFromDate()!="" && transfusionTransmittedDiseasesDto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}

			if(transfusionTransmittedDiseasesDto.getBloodBagNo()!=null && transfusionTransmittedDiseasesDto.getBloodBagNo()!=""){
				subQuery.append(" AND mstr.bloodBagNo=:bloodBagNo ");
			}
		
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery.setParameter("organizationId", transfusionTransmittedDiseasesDto.getOrganizationId())
					.setParameter("unitId", transfusionTransmittedDiseasesDto.getUnitId());


			if(transfusionTransmittedDiseasesDto.getFromDate()!=null && transfusionTransmittedDiseasesDto.getToDate()!=null && transfusionTransmittedDiseasesDto.getFromDate()!="" && transfusionTransmittedDiseasesDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",transfusionTransmittedDiseasesDto.getFromDate());
				actualQuery.setParameter("toDate",transfusionTransmittedDiseasesDto.getToDate());
			}

			if(transfusionTransmittedDiseasesDto.getBloodBagNo()!=null && transfusionTransmittedDiseasesDto.getBloodBagNo()!=""){
				actualQuery.setParameter("bloodBagNo",transfusionTransmittedDiseasesDto.getBloodBagNo());
			}


			List<TransfusionTransmittedDiseasesDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(TransfusionTransmittedDiseasesDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("searchTTD failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateTransfusionTransmittedDiseases(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException {
		try {
			TransfusionTransmittedDiseases transfusionTransmittedDiseases = new TransfusionTransmittedDiseases();
			transfusionTransmittedDiseases.setTransfusionTransmittedDiseasesId(transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesId());
			transfusionTransmittedDiseases.setBarcode(transfusionTransmittedDiseasesDto.getBarcode());
			transfusionTransmittedDiseases.setBloodBagCollectionId(transfusionTransmittedDiseasesDto.getBloodBagCollectionId());
			transfusionTransmittedDiseases.setBloodBagNo(transfusionTransmittedDiseasesDto.getBloodBagNo());
			transfusionTransmittedDiseases.setBloodGroupId(transfusionTransmittedDiseasesDto.getBloodGroupId());
			transfusionTransmittedDiseases.setCreatedBy(transfusionTransmittedDiseasesDto.getCreatedBy());
			transfusionTransmittedDiseases.setCreatedDate(GlobalCommonDateUtils.getDate(transfusionTransmittedDiseasesDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			transfusionTransmittedDiseases.setOrganizationId(transfusionTransmittedDiseasesDto.getOrganizationId());
			transfusionTransmittedDiseases.setStatus('A');
			transfusionTransmittedDiseases.setTransfusionTransmittedDiseasesStatus(transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesStatus());
			transfusionTransmittedDiseases.setTtdDoneById(transfusionTransmittedDiseasesDto.getTtdDoneById());
			transfusionTransmittedDiseases.setUnitId(transfusionTransmittedDiseasesDto.getUnitId());
			transfusionTransmittedDiseases.setUpdatedBy(transfusionTransmittedDiseasesDto.getCreatedBy());
			transfusionTransmittedDiseases.setUpdatedDate(GlobalCommonDateUtils.getDate(transfusionTransmittedDiseasesDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			transfusionTransmittedDiseases.setDonorId(transfusionTransmittedDiseasesDto.getDonorId());
			transfusionTransmittedDiseases.setDonorNumber(transfusionTransmittedDiseasesDto.getDonorNumber());
			
			transfusionTransmittedDiseases=update(transfusionTransmittedDiseases);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, transfusionTransmittedDiseases);
	}
	 catch (Exception e) {
	 	logger.error("updateTransfusionTransmittedDiseases failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
	}
	
	@Override
	public Response getTTDListOnRedCellGroupingAndSerum(
			TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) throws ApplicationException {
		try {
		
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_ACCEPTED_TTD_LIST_ON_RED_CELL_GROUPING_AND_SERUM").getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(transfusionTransmittedDiseasesDto.getFromDate()!=null && transfusionTransmittedDiseasesDto.getToDate()!=null && transfusionTransmittedDiseasesDto.getFromDate()!="" && transfusionTransmittedDiseasesDto.getToDate()!=""){
				subQuery.append(" and to_char(mstr.created_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			if(transfusionTransmittedDiseasesDto.getBloodBagNo()!=null && transfusionTransmittedDiseasesDto.getBloodBagNo()!=""){
				subQuery.append(" AND lower(mstr.blood_bag_no) LIKE :bloodBagNo ");
			}
			if(transfusionTransmittedDiseasesDto.getFromScreen() ==1){
				subQuery.append(" and ( tbrc.grouping_status is null or tbrc.grouping_status = 'N' ) ");
			}
			if(transfusionTransmittedDiseasesDto.getFromScreen() ==2){
				subQuery.append(" and( tbrc.serum_status is null or tbrc.serum_status = 'N' )");
			}
			if(transfusionTransmittedDiseasesDto.getFromScreen() ==3){
				subQuery.append(" and ( tbrc.serum_status  = 'Y'  and tbrc.grouping_status = 'Y' ) and  tbrc.blood_confirmation_status = 4 "); //for pending 
			}
			
			query = query.concat(subQuery.toString());
			Query actualQuery=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", transfusionTransmittedDiseasesDto.getUnitId())
					.setParameter("organizationId", transfusionTransmittedDiseasesDto.getOrganizationId());


			if(transfusionTransmittedDiseasesDto.getFromDate()!=null && transfusionTransmittedDiseasesDto.getToDate()!=null && transfusionTransmittedDiseasesDto.getFromDate()!="" && transfusionTransmittedDiseasesDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",transfusionTransmittedDiseasesDto.getFromDate());
				actualQuery.setParameter("toDate",transfusionTransmittedDiseasesDto.getToDate());
			}
			if(transfusionTransmittedDiseasesDto.getBloodBagNo()!=null && transfusionTransmittedDiseasesDto.getBloodBagNo()!=""){
				actualQuery.setParameter("bloodBagNo", "%" + transfusionTransmittedDiseasesDto.getBloodBagNo().toLowerCase() + "%");
			}


			List<TransfusionTransmittedDiseasesDto> list=actualQuery.setResultTransformer(Transformers.aliasToBean(TransfusionTransmittedDiseasesDto.class)).list();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
	}
	 catch (Exception e) {
	 	logger.error("getTTDListOnRedCellGroupingAndSerum failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
	}
	
}
