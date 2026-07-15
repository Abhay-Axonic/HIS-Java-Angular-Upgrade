package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.AdmissionOpdDTO;
import com.param.opd.model.AdmissionModel;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*
 * 
 * Created by Tushar
 */
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdmissionDAO extends GenericDao<AdmissionModel, Integer> implements IAdmissionDAO,ICommonConstants{
	final static Logger logger = Logger.getLogger(AdmissionDAO.class);


	public AdmissionDAO() {
		super(AdmissionModel.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AdmissionModel saveAdmission(AdmissionOpdDTO admissionOpdDTO)
			throws ApplicationException {
		try{
			
			AdmissionModel admissionModel = new AdmissionModel();
			admissionModel.setAdmission_type_id(admissionOpdDTO.getAdmission_type_id());
			admissionModel.setBed_category_id(admissionOpdDTO.getBed_category_id());
			admissionModel.setReason_for_admission_id(admissionOpdDTO.getReason_for_admission_id());
			admissionModel.setAdmission_date(CommonDateUtils.getDate(admissionOpdDTO.getAdmission_date(), "dd-MM-yyyy"));
			admissionModel.setPlan_discharge_date(CommonDateUtils.getDate(admissionOpdDTO.getPlan_discharge_date(), "dd-MM-yyyy"));
			admissionModel.setIs_flexible_status(admissionOpdDTO.isIs_flexible_status());
			admissionModel.setPatient_id(admissionOpdDTO.getPatient_id());
			admissionModel.setCreated_date(TimeZoneComponent.getDateByzone(admissionOpdDTO.getUnit_id()));
			admissionModel.setCreated_by(admissionOpdDTO.getCreated_by());
			admissionModel.setUpdated_date(TimeZoneComponent.getDateByzone(admissionOpdDTO.getUnit_id()));
			admissionModel.setUpdated_by(admissionOpdDTO.getUpdated_by());
			admissionModel.setEncounter_id(admissionOpdDTO.getEncounter_id());
			admissionModel.setUnit_id(admissionOpdDTO.getUnit_id());
			admissionModel.setOrganisation_id(admissionOpdDTO.getOrganisation_id());
			AdmissionModel admission = save(admissionModel);
			return admission;
		}catch (HibernateException he) {
		logger.error("saveAdmission failed", he);
		} catch (Exception e) {
		logger.error("saveAdmission failed", e);
		}
		return null;
	}

	@Override
	public AdmissionModel updateAdmission(AdmissionOpdDTO admissionOpdDTO)
			throws ApplicationException {
		try{
			AdmissionModel admissionModel = findById(admissionOpdDTO.getAdmission_id());
			if(admissionModel != null){
				admissionModel.setAdmission_type_id(admissionOpdDTO.getAdmission_type_id());
				admissionModel.setBed_category_id(admissionOpdDTO.getBed_category_id());
				admissionModel.setReason_for_admission_id(admissionOpdDTO.getReason_for_admission_id());
				admissionModel.setPlan_discharge_date(CommonDateUtils.getDate(admissionOpdDTO.getPlan_discharge_date(), "dd-MM-yyyy"));
				admissionModel.setIs_flexible_status(admissionOpdDTO.isIs_flexible_status());
				admissionModel.setUpdated_date(TimeZoneComponent.getDateByzone(admissionOpdDTO.getUnit_id()));
				admissionModel.setUpdated_by(admissionOpdDTO.getUpdated_by());
				AdmissionModel admission = update(admissionModel);
				return admission;
			}
		}catch (HibernateException he) {
		logger.error("updateAdmission failed", he);
		} catch (Exception e) {
		logger.error("updateAdmission failed", e);
		}
		return null;
	}

	@Override
	public AdmissionModel getAdmission(Integer admissionId,Integer instruction_post_id) throws ApplicationException {
		try{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			final Criteria criteria = statlessSession.createCriteria(AdmissionModel.class)
					.add(Restrictions.eq("admission_id",admissionId));
			AdmissionModel admissionModel = (AdmissionModel) criteria.uniqueResult();
			statlessSession.close();
			return admissionModel;
		}catch (HibernateException he) {
		logger.error("getAdmission failed", he);
		} catch (Exception e) {
		logger.error("getAdmission failed", e);
		}
		return null;
	}

	@Override
	public Response getAdmissionsForBatchProcess(int startPoint, int maxResults, List<Integer> unitIds) throws ApplicationException {
		try{
			
			String quString= 
				"select " 
				+ " 	admission.admission_id as \"admissionId\", " 
				+ " 	admission.patient_id as \"patientId\", " 
				+ " 	admission.visit_type_id as \"visitTypeId\", " 
				+ " 	admission.doctor_id as \"doctorId\", " 
				+ " 	admission.speciality_id as \"specialityId\", " 
				+ " 	spd.contract_id as \"contractId\", " 
				+ " 	spd.payee_id as \"payeeId\", "
				+ "		spd.payment_entitlement_id as \"paymentEntitlementId\", " 
				+ " 	adl.billing_bed_category_id as \"billingBedCategoryId\", " 
				+ " 	bed.bed_category_id as \"bedCategoryId\", " 
				+ " 	patReg.patient_category_id as \"patientTypeId\", "
				+ "		admission.organization_id as \"organizationId\", "
				+ "		admission.unit_id as \"unitId\", "
				+ "     to_char(admission.created_date ,'dd/MM/yyyy HH24:MI:ss') as \"admissionDateTime\", "
				+ "     admission.patient_class_id as \"patientClassId\", "
				+ "     appConfig.default_self_tariff_id as \"defaultSelfTariffId\", "
				+ "     appConfig.self_payee_id as \"selfPayeeId\", "
				+ "		appConfig.default_discount_ownership as \"defaultDiscountOwnership\", "
				+ "		adl.bed_id as \"bedId\", "
				+ "		adl.ward_id as \"wardId\", "
				+ "		adl.room_id as \"roomId\", "
				+ "		coalesce(mbcm.service_id,0) as \"serviceIdForBedCharges\" "
				+ " from " 
				+ " 	adt.t_admission admission " 
				+ " inner join adt.t_admission_details adl on " 
				+ " 	adl.admission_id = admission.admission_id  " 
				+ " inner join opd.t_patient_sponsor_details spd on " 
				+ " 	spd.admission_id = admission.admission_id and spd.priority_id = 1 and spd.status ='A' " 
				+ " inner join adt.m_bed_master bed on "
				+ "		bed.bed_id = adl.bed_id  "
				+ "	inner join adt.m_billing_bed_category_master bedBill on "
				+ "		bedBill.billing_bed_category_id = adl.billing_bed_category_id "
				+ "	inner join adt.m_bed_category_master mbcm on "
				+ "		mbcm.bed_category_id = adl.bed_category_id "
				+ " inner join patient.t_patient_registration patReg on " 
				+ " 	patReg.patient_id = admission.patient_id "
				+ " left join adt.t_discharge dis "
				+ "		on dis.admission_id = admission.admission_id and dis.status='A' " 
				+ " inner join t_unit_application_billing_configuration appConfig on "
				+ "		appConfig.unit_id = admission.unit_id	"
				+ " where " 
				+ " 	admission.status = 'A' "
				+ "		and bedBill.is_periodic_billing='N' " 
				+ " 	and adl.status = 'A' "
				+ " 	and adl.nursing_acceptance = 'Y' "
				+ "		and dis.discharge_id is null "
				+ "		and admission.visit_type_id = 2 "
				+ "     and admission.unit_id in (:unitIds) ";
			
			List<AdmissionsForBatchProcessDto> listAdmissionsForBatchProcessDto = sessionFactory.getCurrentSession().createSQLQuery(quString)
					.setParameterList("unitIds", unitIds)
					.setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listAdmissionsForBatchProcessDto, null);
			
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	public Response getAdmissionsForPeriodicPosting(int startPoint, int maxResults, List<Integer> unitIds) throws ApplicationException {
		try{
			
			String quString= 
				"select " 
				+ " 	admission.admission_id as \"admissionId\", " 
				+ " 	admission.patient_id as \"patientId\", " 
				+ " 	admission.visit_type_id as \"visitTypeId\", " 
				+ " 	admission.doctor_id as \"doctorId\", " 
				+ " 	admission.speciality_id as \"specialityId\", " 
				+ " 	spd.contract_id as \"contractId\", " 
				+ " 	spd.payee_id as \"payeeId\", " 
				+ " 	bed.billing_bed_category_id as \"billingBedCategoryId\", " 
				+ " 	bed.bed_category_id as \"bedCategoryId\", " 
				+ " 	patReg.patient_category_id as \"patientTypeId\", "
				+ "		admission.organization_id as \"organizationId\", "
				+ "		admission.unit_id as \"unitId\", "
				+ "     to_char(admission.created_date ,'dd/MM/yyyy HH24:mm:ss') as \"admissionDateTime\", "
				+ "     admission.patient_class_id as \"patientClassId\", "
				+ "     appConfig.default_self_tariff_id as \"defaultSelfTariffId\", "
				+ "     appConfig.self_payee_id as \"selfPayeeId\", "
				+ "		appConfig.default_discount_ownership as \"defaultDiscountOwnership\", "
				+ "		adl.bed_id as \"bedId\", "
				+ "		adl.ward_id as \"wardId\", "
				+ "		adl.room_id as \"roomId\" "
				+ " from " 
				+ " 	adt.t_admission admission " 
				+ " inner join adt.t_admission_details adl on " 
				+ " 	adl.admission_id = admission.admission_id  " 
				+ " inner join opd.t_patient_sponsor_details spd on " 
				+ " 	spd.admission_id = admission.admission_id and spd.priority_id = 1 and spd.status ='A' " 
				+ " inner join adt.m_bed_master bed on "
				+ "		bed.bed_id = adl.bed_id  "
				+ "	inner join adt.m_billing_bed_category_master bedBill on "
				+ "		bedBill.billing_bed_category_id = bed.billing_bed_category_id     "
				+ " inner join patient.t_patient_registration patReg on " 
				+ " 	patReg.patient_id = admission.patient_id "
				+ " left join adt.t_discharge dis "
				+ "		on dis.admission_id = admission.admission_id "
				+ " inner join t_unit_application_billing_configuration appConfig on "
				+ "		appConfig.unit_id = admission.unit_id	"
				+ " where " 
				+ " 	admission.status = 'A' "
				+ "		and bedBill.is_periodic_billing='Y' " 
				+ " 	and adl.status = 'A' "
				+ " 	and adl.nursing_acceptance = 'Y' "
				+ "		and dis.discharge_id is null "
				+ "		and admission.visit_type_id = 2 "
				+ "     and admission.unit_id in (:unitIds) " ;
			
			List<AdmissionsForBatchProcessDto> listAdmissionsForBatchProcessDto = sessionFactory.getCurrentSession().createSQLQuery(quString)
					.setParameterList("unitIds", unitIds)
					.setFirstResult(startPoint).setMaxResults(maxResults)
					.setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listAdmissionsForBatchProcessDto, null);
			
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response getAdmissionByIdForBatchPServicePosting(int admissionId) throws ApplicationException {
		try{
			AdmissionsForBatchProcessDto dto = (AdmissionsForBatchProcessDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ADMISSION_DETAILS_BY_ID_FOR_POSTING_PERIODICSERVICES_THROUGH_BATCH")
						.setParameter("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class))
						.uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

}
