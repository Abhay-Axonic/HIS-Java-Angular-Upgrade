package com.param.bloodBank.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IntraTransfusionMonitoringMapper;
import com.param.bloodBank.dto.TransfusionDetailsMasterDto;
import com.param.bloodBank.model.BloodBagIssueAcceptance;
import com.param.bloodBank.model.IntraTransfusionMonitoring;
import com.param.bloodBank.model.TransfusionDetailMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SubSpecialityMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBagIssueAcceptanceDaoImpl extends GenericDao<BloodBagIssueAcceptance, Integer> implements IBloodBagIssueAcceptanceDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagIssueAcceptanceDaoImpl.class);


	public BloodBagIssueAcceptanceDaoImpl() {
		super(BloodBagIssueAcceptance.class);
	}
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveBloodBagIssueAcceptance(List<BloodBagIssueAcceptanceDto> bloodBagIssueAcceptanceDto)throws ApplicationException {
		
	try {
		for (BloodBagIssueAcceptanceDto dto : bloodBagIssueAcceptanceDto) {
			
			BloodBagIssueAcceptance mstr = new BloodBagIssueAcceptance();
			mstr.setAcceptedBy(dto.getAcceptedBy());
			mstr.setBloodBagCapacityId(dto.getBloodBagCapacityId());
			mstr.setBloodBagComponenId(dto.getBloodBagComponenId());
			mstr.setBloodBagIssueAcceptanceStatusId(dto.getBloodBagIssueAcceptanceStatusId());
			mstr.setBloodBagNo(dto.getBloodBagNo());
			mstr.setBloodBagReasonId(dto.getBloodBagReasonId());
			mstr.setBloodBagReasonNote(dto.getBloodBagReasonNote());
			mstr.setBloodBagTypeId(dto.getBloodBagTypeId());
			mstr.setCreatedBy(dto.getCreatedBy());
			mstr.setBloodGroupId(dto.getBloodGroupId());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			mstr.setExported(false);
			mstr.setIssueDate(GlobalCommonDateUtils.getDate(dto.getIssueDate(), "dd-M-yyyy HH:mm:ss"));
			mstr.setIssueNo(dto.getIssueNo());
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setPatientId(dto.getPatientId());
			mstr.setRequisitionNumber(dto.getRequisitionNumber());
			mstr.setStatus(dto.getStatus());
			mstr.setUhid(dto.getUhid());
			mstr.setUnitId(dto.getUnitId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			mstr=save(mstr);
		}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			

		} catch (HibernateException he) {
			logger.error("saveBloodBagIssueAcceptance failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveBloodBagIssueAcceptance failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getListOnPendingTransfusion(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)
			throws ApplicationException {
		try{
			
			List<BloodBagIssueAcceptanceDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BAG_ISSUE_ACCEPTANCE_LIST")
					.setParameter("organizationId", bloodBagIssueAcceptanceDto.getOrganizationId())
					.setParameter("unitId", bloodBagIssueAcceptanceDto.getUnitId())
					.setParameter("fromDate", bloodBagIssueAcceptanceDto.getFromDate())
					.setParameter("toDate", bloodBagIssueAcceptanceDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(BloodBagIssueAcceptanceDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);

		} catch (HibernateException he) {
			logger.error("getListOnPendingTransfusion failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getListOnPendingTransfusion failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveTransfusionDetails(TransfusionDetailsMasterDto transfusionDetailsMasterDto)
			throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			TransfusionDetailMaster transfusionDetailMaster = mapper.map(transfusionDetailsMasterDto,TransfusionDetailMaster.class,
								"TransfusionDetailsMasterDto_to_TransfusionDetailMaster");
			
			if(transfusionDetailsMasterDto.getTransfusionDetailId()!=null && transfusionDetailsMasterDto.getTransfusionDetailId()!=0) {
				 session.update(transfusionDetailMaster);
			}
			else {
				transfusionDetailsMasterDto.setTransfusionDetailId((Integer) session.save(transfusionDetailMaster));
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, transfusionDetailsMasterDto);
		}catch (HibernateException he) {
			logger.error("saveTransfusionDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}    

	@Override
	public Response saveIntraTransfusionMonitoringMapper(TransfusionDetailsMasterDto transfusionDetailsMasterDto)
			throws ApplicationException {
		try{	
            List<IntraTransfusionMonitoringMapper> itransfusionMapperlist=transfusionDetailsMasterDto.getIntraTransfusionMonitoringMapperlist();
            Session session=sessionFactory.getCurrentSession();
            System.out.println("List: size"+itransfusionMapperlist.size());
            if (itransfusionMapperlist != null && !itransfusionMapperlist.isEmpty()){
            	
            for(IntraTransfusionMonitoringMapper itransfusionMapperDto:itransfusionMapperlist) {
//            	if(itransfusionMapperDto.getIntraTransfusionMonitoringMapperId() != null) continue;
            	
            	itransfusionMapperDto.setTransfusionDetailId(transfusionDetailsMasterDto.getTransfusionDetailId());
            	itransfusionMapperDto.setPatientId(transfusionDetailsMasterDto.getPatientId());
            	
            	IntraTransfusionMonitoring intraTransfusionMonitoring= new IntraTransfusionMonitoring();
            	intraTransfusionMonitoring.setPatientId(transfusionDetailsMasterDto.getPatientId());
            	intraTransfusionMonitoring.setTransfusionDetailId(transfusionDetailsMasterDto.getTransfusionDetailId());
            	intraTransfusionMonitoring.setIncubationStartTime(itransfusionMapperDto.getIncubationStartTime());
            	intraTransfusionMonitoring.setPulse(itransfusionMapperDto.getPulse());
            	intraTransfusionMonitoring.setTemperature(itransfusionMapperDto.getTemperature());
            	intraTransfusionMonitoring.setRespirationRate(itransfusionMapperDto.getRespirationRate());
            	intraTransfusionMonitoring.setBpDiastolic(itransfusionMapperDto.getBpDiastolic());
            	intraTransfusionMonitoring.setBpSystolic(itransfusionMapperDto.getBpSystolic());
            	intraTransfusionMonitoring.setCreatedBy(transfusionDetailsMasterDto.getCreatedBy());
            	intraTransfusionMonitoring.setCreatedDate(GlobalCommonDateUtils.getDate(transfusionDetailsMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
            	intraTransfusionMonitoring.setUpdatedBy(transfusionDetailsMasterDto.getUpdatedBy());
            	intraTransfusionMonitoring.setUnitId(transfusionDetailsMasterDto.getUnitId());
            	intraTransfusionMonitoring.setOrganizationId(transfusionDetailsMasterDto.getOrganizationId());
            	intraTransfusionMonitoring.setUpdatedBy(transfusionDetailsMasterDto.getUpdatedBy());
            	intraTransfusionMonitoring.setStatus(itransfusionMapperDto.getStatus());
            	intraTransfusionMonitoring.setCreatedDate(GlobalCommonDateUtils.getDate(transfusionDetailsMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
            if(itransfusionMapperDto.getIntraTransfusionMonitoringMapperId()!=null)
            {
            	intraTransfusionMonitoring.setIntraTransfusionMonitoringMapperId(itransfusionMapperDto.getIntraTransfusionMonitoringMapperId());
            	session.update(intraTransfusionMonitoring);
            }else{
            	session.save(intraTransfusionMonitoring);
            	}
            }}
        	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			logger.error("saveIntraTransfusionMonitoringMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveIntraTransfusionMonitoringMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public TransfusionDetailsMasterDto getTransfusionDetailsList(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
	try {
		TransfusionDetailsMasterDto transfusionDetailsMasterDto = (TransfusionDetailsMasterDto) sessionFactory.getCurrentSession()
			    .createSQLQuery("SELECT " + 
			               "    mtd.v_site_id AS \"vSiteId\", " + 
			               "    mtd.iv_access_id AS \"ivAccessId\"," + 
			               "    mtd.donor_pulse AS \"donorPulse\"," + 
			               "    mtd.donor_temperature AS \"donorTemperature\"," + 
			               "    mtd.donor_bp_systolic AS \"donorBPSystolic\"," + 
			               "    mtd.donor_bp_diastolic AS \"donorBpDiastolic\"," + 
			               "    mtd.donor_respiration_rate AS \"donarRespirationRate\"," + 
			               "    mtd.start_date AS \"startDate\"," + 
			               "    mtd.start_time AS \"startTime\"," + 
			               "    mtd.transfusion_id AS \"transfusionId\"," + 
			               "    mtd.save_as_draft_flag AS \"saveAsDraftFlag\"," +
			               "    mtd.termination_id AS \"terminationId\" " + 
			               "FROM " + 
			               "    blood_bank.m_transfusion_details mtd " + 
			               "WHERE " + 
			               "    mtd.organization_id = :organizationId " + 
			               "    AND mtd.unit_id = :unitId " + 
			               "    AND mtd.patient_id = :patientId " + 
			                " AND mtd.status ='A' ")
			    .setParameter("patientId", patientId)
			    .setParameter("organizationId", organizationId)
			    .setParameter("unitId", unitId)
			    .setResultTransformer(Transformers.aliasToBean(TransfusionDetailsMasterDto.class))
			    .uniqueResult();

				return transfusionDetailsMasterDto;
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			logger.error("getTransfusionDetailsList failed", e);
			throw new ServiceException(e.getMessage());
		} 
	}

	@Override
	public List<IntraTransfusionMonitoringMapper> getintraTransfusionDetailsList(Integer patientId,
			Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<IntraTransfusionMonitoringMapper> intraTransfusionMonitoringMapperList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT " + 
							   "    mppr.intra_transfusion_monitoring_mapper_id AS \"intraTransfusionMonitoringMapperId\"," +
				               "    mppr.status AS \"status\"," + 
				               "    mppr.start_time AS \"incubationStartTime\"," + 
				               "    mppr.donor_pulse AS \"pulse\"," + 
				               "    mppr.donor_temperature AS \"temperature\"," + 
				               "    mppr.donor_respiration_rate AS \"respirationRate\"," + 
				               "    mppr.donor_bp_systolic AS \"bpSystolic\"," + 
				               "    mppr.donor_bp_diastolic AS \"bpDiastolic\" " + 
				               "FROM " + 
				               "    blood_bank.t_intra_transfusion_monitoring_mapper mppr " + 
				               "WHERE " + 
				               "    mppr.organization_id = :organizationId " + 
				               "    AND mppr.unit_id = :unitId " + 
				               "    AND mppr.patient_id = :patientId "
				               + " AND mppr.status ='A' "
				               + " ORDER BY mppr.start_time desc " )
					
					.setParameter("patientId", patientId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(IntraTransfusionMonitoringMapper.class)).list();
//			return new Response(SUCCESS, null, null, transfusionDetailsMasterDtoList, null);
				return intraTransfusionMonitoringMapperList;
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			logger.error("getintraTransfusionDetailsList failed", e);
			throw new ServiceException(e.getMessage());
		} 
	}

	@Override
	public Response updateTransfusionDetailsStatus(IntraTransfusionMonitoringMapper intraTransfusionMonitoringMapper)
			throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery(" update  blood_bank.t_intra_transfusion_monitoring_mapper"
					+ " SET  status = 'I' "
					+ " where intra_transfusion_monitoring_mapper_id ='"+intraTransfusionMonitoringMapper.getIntraTransfusionMonitoringMapperId()+"' ")
					.executeUpdate();
			
			if(count>0) {
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, intraTransfusionMonitoringMapper);
			}
			else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}catch (HibernateException he) {
			logger.error("updateTransfusionDetailsStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateTransfusionDetailsStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
