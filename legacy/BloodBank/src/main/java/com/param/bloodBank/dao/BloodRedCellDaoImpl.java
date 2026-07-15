package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodRedCellDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.bloodBank.model.BloodRedCell;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodRedCellDaoImpl  extends GenericDao<BloodRedCell, Integer> implements IBloodRedCellDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodRedCellDaoImpl.class);


	
	public BloodRedCellDaoImpl() {
		super(BloodRedCell.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveOrUpdateRedCell(BloodRedCellDto dto) throws ApplicationException {
		try {
			
			BloodRedCell mstr = new BloodRedCell();
			mstr.setBloodConfirmationStatus(dto.getBloodConfirmationStatus() != null ? dto.getBloodConfirmationStatus(): 4); // 4 for pending 
			mstr.setCheckedCellGrouping(dto.getCheckedCellGrouping() != null ? dto.getCheckedCellGrouping() : false);
			mstr.setCheckedSerumGrouping(dto.getCheckedSerumGrouping() != null ? dto.getCheckedSerumGrouping() : false);
			mstr.setConfirmationBloodGroupId(dto.getConfirmationBloodGroupId() != null ? dto.getConfirmationBloodGroupId() : null);
			mstr.setConfirmationTechnicianId(dto.getConfirmationTechnicianId() != null ? dto.getConfirmationTechnicianId() : null);
			mstr.setGroupingBloodGroupId(dto.getGroupingBloodGroupId() != null ? dto.getGroupingBloodGroupId() : null);
			mstr.setGroupingIrregularAntibodies(dto.getGroupingIrregularAntibodies() != null ? dto.getGroupingIrregularAntibodies() : null);
			mstr.setGroupingStatus(dto.getGroupingStatus() != null ? dto.getGroupingStatus() : 'N');
			mstr.setGroupingTechnicianId(dto.getGroupingTechnicianId() != null ? dto.getGroupingTechnicianId() : null);
			mstr.setResultA(dto.getResultA() != null ? dto.getResultA() : null);
			mstr.setResultA1(dto.getResultA1() != null ? dto.getResultA1() : null);
			mstr.setResultAB(dto.getResultAB() != null ? dto.getResultAB() : null);
			mstr.setResultACell(dto.getResultACell() != null ? dto.getResultACell() : null);
			mstr.setResultB(dto.getResultB() != null ? dto.getResultB() : null);
			mstr.setResultBCell(dto.getResultBCell() != null ? dto.getResultBCell() : null);
			mstr.setResultD(dto.getResultD() != null ? dto.getResultD() : null);
			mstr.setResultH(dto.getResultH() != null ? dto.getResultH() : null);
			mstr.setResultOBCell(dto.getResultOBCell() != null ? dto.getResultOBCell() : null);
			mstr.setResultOCell(dto.getResultOCell() != null ? dto.getResultOCell() : null);
			mstr.setSerumBloodGroupId(dto.getSerumBloodGroupId() != null ? dto.getSerumBloodGroupId() : null );
			mstr.setSerumIrregularAntibodies(dto.getSerumIrregularAntibodies() != null ? dto.getSerumIrregularAntibodies() : null) ;
			mstr.setSerumStatus(dto.getSerumStatus() != null ? dto.getSerumStatus() : 'N' );
			mstr.setSerumTechnicianId(dto.getSerumTechnicianId() != null ? dto.getSerumTechnicianId() : null );
			mstr.setTransfusionTransmittedDiseasesId(dto.getTransfusionTransmittedDiseasesId());
			mstr.setCreatedBy(dto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setStatus('A');
			mstr.setUnitId(dto.getUnitId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr=save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

	} catch (Exception e) {
		logger.error("saveOrUpdateRedCell failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response updateRedCellGrouping(BloodRedCellDto dto) throws ApplicationException {
	try {
			
			BloodRedCell mstr = findById(dto.getRedCellId());
			mstr.setCheckedCellGrouping(dto.getCheckedCellGrouping() != null ? dto.getCheckedCellGrouping() : false);
			mstr.setGroupingBloodGroupId(dto.getGroupingBloodGroupId() != null ? dto.getGroupingBloodGroupId() : null);
			mstr.setGroupingIrregularAntibodies(dto.getGroupingIrregularAntibodies() != null ? dto.getGroupingIrregularAntibodies() : null);
			mstr.setGroupingStatus(dto.getGroupingStatus() != null ? dto.getGroupingStatus() : 'N');
			mstr.setGroupingTechnicianId(dto.getGroupingTechnicianId() != null ? dto.getGroupingTechnicianId() : null);
			mstr.setResultA(dto.getResultA() != null ? dto.getResultA() : null);
			mstr.setResultA1(dto.getResultA1() != null ? dto.getResultA1() : null);
			mstr.setResultAB(dto.getResultAB() != null ? dto.getResultAB() : null);
			mstr.setResultB(dto.getResultB() != null ? dto.getResultB() : null);
			mstr.setResultD(dto.getResultD() != null ? dto.getResultD() : null);
			mstr.setResultH(dto.getResultH() != null ? dto.getResultH() : null);
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr=update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);

	} catch (Exception e) {
		logger.error("updateRedCellGrouping failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response updateRedCellSerum(BloodRedCellDto dto) throws ApplicationException {
		try {
			
			BloodRedCell mstr = findById(dto.getRedCellId());
			mstr.setCheckedSerumGrouping(dto.getCheckedSerumGrouping() != null ? dto.getCheckedSerumGrouping() : false);
			mstr.setResultACell(dto.getResultACell() != null ? dto.getResultACell() : null);
			mstr.setResultBCell(dto.getResultBCell() != null ? dto.getResultBCell() : null);
			mstr.setResultOBCell(dto.getResultOBCell() != null ? dto.getResultOBCell() : null);
			mstr.setResultOCell(dto.getResultOCell() != null ? dto.getResultOCell() : null);
			mstr.setSerumBloodGroupId(dto.getSerumBloodGroupId() != null ? dto.getSerumBloodGroupId() : null );
			mstr.setSerumIrregularAntibodies(dto.getSerumIrregularAntibodies() != null ? dto.getSerumIrregularAntibodies() : null) ;
			mstr.setSerumStatus(dto.getSerumStatus() != null ? dto.getSerumStatus() : 'N' );
			mstr.setSerumTechnicianId(dto.getSerumTechnicianId() != null ? dto.getSerumTechnicianId() : null );
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr=update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);

	} catch (Exception e) {
		logger.error("updateRedCellSerum failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response updateBloodConfirmed(BloodRedCellDto dto) throws ApplicationException {
		try {
			
			BloodRedCell mstr = findById(dto.getRedCellId());
			mstr.setBloodConfirmationStatus(dto.getBloodConfirmationStatus() != null ? dto.getBloodConfirmationStatus(): 4); // 4 for pending 
			mstr.setConfirmationBloodGroupId(dto.getConfirmationBloodGroupId() != null ? dto.getConfirmationBloodGroupId() : null);
			mstr.setConfirmationTechnicianId(dto.getConfirmationTechnicianId() != null ? dto.getConfirmationTechnicianId() : null);
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr=update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

		} catch (Exception e) {
			logger.error("updateBloodConfirmed failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getRedCellDetails(BloodRedCellDto dto) throws ApplicationException {
		try {
		
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_RED_CELL_DETAILS").getQueryString();
			
			BloodRedCellDto Obj=(BloodRedCellDto) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("redCellId", dto.getRedCellId())
					.setResultTransformer(Transformers.aliasToBean(BloodRedCellDto.class)).uniqueResult();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, Obj);
	}
	 catch (Exception e) {
	 	logger.error("getRedCellDetails failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
	}

	@Override
	public Response getRedCellDetailsOnComponentSeparation(TransfusionTransmittedDiseasesDto dto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_RED_CELL_DETAILS_ON_COMPONENT_SEPARATION").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				subQuery.append(" AND to_char(tbrc.created_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}

			if(dto.getBloodBagNo()!=null && dto.getBloodBagNo()!=""){
				//subQuery.append(" AND lower(mstr.blood_bag_no)  like '%:bloodBagNo%' ");
				subQuery.append(" AND lower(mstr.blood_bag_no) LIKE :bloodBagNo");
			}
		
			query = query.append(subQuery.toString());
			Query actualQuery = session.createSQLQuery(query.toString());
		 actualQuery.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId());

			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				actualQuery.setParameter("fromDate",dto.getFromDate());
				actualQuery.setParameter("toDate",dto.getToDate());
			}

			if(dto.getBloodBagNo()!=null && dto.getBloodBagNo()!=""){
				//actualQuery.setParameter("bloodBagNo", dto.getBloodBagNo().toLowerCase());
				actualQuery.setParameter("bloodBagNo", "%" + dto.getBloodBagNo().toLowerCase() + "%");
			}


			List<BloodRedCellDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(BloodRedCellDto.class)).list();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
	}
	 catch (Exception e) {
	 	logger.error("getRedCellDetailsOnComponentSeparation failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
	}

}
