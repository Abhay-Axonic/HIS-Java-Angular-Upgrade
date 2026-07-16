package com.param.opd.coversheet.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.mims.cds.FastTrackDSM;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ApplicationConfigurationDto;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.DrugMasterDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.dto.FrequencyMasterDTO;
import com.param.opd.coversheet.dto.GeneralExaminationAppoMapperDto;
import com.param.opd.coversheet.dto.ICPCSystemDTO;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationInstructionDTO;
import com.param.opd.coversheet.dto.MedicationListDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.dto.RoaMasterDto;
import com.param.opd.coversheet.model.PrescriptionDetailsMaster;
import com.param.opd.coversheet.model.PrescriptionFrequencyMaster;
import com.param.opd.coversheet.model.PrescriptionMaster;
import com.param.opd.encounter.dto.EncounterDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import java.io.IOException;


import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class MedicationDAOImpl extends GenericDao<PrescriptionMaster, Integer>
		implements IMedicationDAO, ICommonConstants {

	final static Logger logger = Logger.getLogger(MedicationDAOImpl.class);
	public MedicationDAOImpl() {
		super(PrescriptionMaster.class);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	private ServletContext servletContext;
	
	@Override
	public Response saveMedication(MedicationDTO medicationDTOs) throws ApplicationException {
		/*try {
			
			MedicationModel medicationModel = mapper.map(medicationDTOs, MedicationModel.class, "MedicationModelDto_to_MedicationModel");
				medicationModel = save(medicationModel);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}*/
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateMedication(MedicationDTO medicationDTO) throws ApplicationException {
		/*try {
			MedicationModel medicationModel = findById(medicationDTO.getMedication_id());
			if (medicationModel != null) {
				medicationModel.setDrug_id(medicationDTO.getDrug_id());
				medicationModel.setDose(medicationDTO.getDose());
				medicationModel.setRoa_id(medicationDTO.getRoa_id());
				medicationModel.setFrequency_id(medicationDTO.getFrequency_id());
				medicationModel.setDuration(medicationDTO.getDuration());
				medicationModel.setDuration_unit(medicationDTO.getDuration_unit());
				medicationModel.setStart_date(CommonDateUtils.getDate(medicationDTO.getStart_date(), "dd-MM-yyyy"));
				medicationModel.setEnd_date(CommonDateUtils.getDate(medicationDTO.getEnd_date(), "dd-MM-yyyy"));
				medicationModel.setInstruction_id(medicationDTO.getInstruction_id());
				medicationModel.setFavorite_status(medicationDTO.getFavorite_status());
				medicationModel.setPatient_id(medicationDTO.getPatient_id());
				medicationModel.setUpdated_date(new Date());
				medicationModel.setUpdated_by(medicationDTO.getUpdated_by());
				medicationModel.setEncounter_id(medicationDTO.getEncounter_id());
				medicationModel.setUnit_id(medicationDTO.getUnit_id());
				medicationModel.setOrganisation_id(medicationDTO.getOrganisation_id());
				MedicationModel medicationModelId = update(medicationModel);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}*/
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response deleteMedication(Integer medicationId) throws ApplicationException {
		/*try {
			MedicationModel medicationModel = findById(medicationId);
			if (medicationModel != null) {
				medicationModel.setMedication_status(FALSE);
				MedicationModel medicationModelId = update(medicationModel);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}*/
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMedicIntructions() throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<MedicationInstructionDTO> medicationDTOList = statlessSession.getNamedQuery("GET_MEDIC_INSTRUCTIONS")
					.setResultTransformer(Transformers.aliasToBean(MedicationInstructionDTO.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, medicationDTOList, null);
		} catch (HibernateException he) {
		logger.error("getMedicIntructions failed", he);
		} catch (Exception e) {
		logger.error("getMedicIntructions failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMedicFavorites(Integer doctorId) throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<MedicationDTO> medicationDTOList = statlessSession.getNamedQuery("GET_FAVORITE_MEDICATION")
					.setParameter("DOCID", doctorId).setResultTransformer(Transformers.aliasToBean(MedicationDTO.class))
					.list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, medicationDTOList, null);
		} catch (HibernateException he) {
		logger.error("getMedicFavorites failed", he);
		} catch (Exception e) {
		logger.error("getMedicFavorites failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public List<DrugMasterDto> getDrugList(Integer unitId, Integer orgId, String searchDrug) throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<DrugMasterDto> listDrugMasterDto = statlessSession.getNamedQuery("GET_DRUG_LIST_ROA")
					.setParameter("MEDICATION_NAME", searchDrug+"%")
//					.setParameter("UNITID", unitId)
					.setParameter("ORGID", orgId)
					.setResultTransformer(Transformers.aliasToBean(DrugMasterDto.class)).list();
			statlessSession.close();
			return listDrugMasterDto;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getDrugList failed", e);
		}
		return null;
	}

	private Integer getDrugUsingNameAndROA(String medicationName, Integer roa_id, String dose) throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			Integer strs = (Integer) statlessSession.getNamedQuery("GET_DRUG_USING_ROA")
					.setParameter("MEDICATION_NAME", medicationName).setParameter("ROA_ID", roa_id)
					.setParameter("DOSE", dose)
					.setParameter("UNITID", 1).setParameter("ORGID", 1).uniqueResult();
			statlessSession.close();
			return strs;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getDrugUsingNameAndROA failed", e);
		}
		return null;
	}

	public Response getLastOrder(Integer patientId,Integer encounterId) throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<MedicationDTO> listMedicationDto = statlessSession.getNamedQuery("GET_LAST_ORDER")
					.setParameter("PATIENT_ID", patientId)
					.setParameter("ENCOUNTERID", encounterId)
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getLastOrder failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}
	
	@Override
	public Response getActiveMedication(Integer patientId, String currentDate, Integer unitId) throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//		    String toDayDate = simpleDateFormat.format(new Date());
		    String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
			String query = "SELECT "
					+ " pres.id as \"prescriptionId\",pres.prescription_no as \"prescriptionNo\",to_char(presDtl.added_date, 'HH24:MI') as \"addedTime\","
					+ " presDtl.order_qty as \"orderQty\",  coalesce(cast((bt.rate) as decimal(10, 2)), 0) as \"rate\","
					+ " presDtl.is_medication_stop as \"isMedicationStop\", muu.unit as \"selectedUom\","
                    + " presDtl.justification_stop as \"justificationStop\", "
					+ " presDtl.priority_id as \"priorityId\", "
                    + " presDtl.is_discharge_medication as \"isDischarge\", pres.medication_type as \"medicationType\", "
                    + " presDtl.duration as \"durationCount\", "
					+ " presDtl.duration_unit as \"durationUnit\", "+
                    	" presDtl.statflag as \"isStat\",  "
                    + " presDtl.instruction_desc \"instructionText\", "
                    + "  presDtl.id as \"prescriptionDetailsid\","
					+ " presDtl.item_id as \"itemId\", "
					 + "itm.formulation_type_id as \"formulationTypeId\", "
					 + "itm.generic_id as \"genericId\", "
					+ " case when (itm.name is null) then presDtl.item_name else itm.name end AS \"itemName\", "
					+ " case when (gen.name is null) then presDtl.generic_name else gen.name end AS \"genericName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ "su.id as \"doseUnitId\", "
					+ " su.unit as \"strengthDesc\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " presDtl.is_non_formulary as \"isNonFormulary\", "
					+ " presDtl.is_non_formulary as \"nonFormularyAdvice\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " freq.no_of_administration_time as \"noOfAdministrationTime\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " roa.description as \"roaDesc\", "
					+ " presDtl.order_qty as \"quantity\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\"," 
					+ " preFreDtl.start_date as \"startDateNew\"," 
					+ " preFreDtl.stop_date as \"endDateNew\",  (case  when empmst.employee_id is not null then " +
					"   concat(empmst.first_name, ' ', empmst.last_name)  when doctmst.doctor_id is not null then " +
					"   concat('Dr. ', doctmst.first_name, ' ', doctmst.last_name)  else 'Admin' end) as \"userName\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " pres.encounter_id as \"encounterId\" ,"
					+ " concat('Dr. ',coalesce(doctmst.first_name),' ',coalesce(doctmst.middle_name),' ',coalesce(doctmst.last_name)) as \"doctorName\","
					+ " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
					+ " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
					+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " LEFT JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
					+ " LEFT JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " LEFT JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " LEFT JOIN doctor.m_doctor_master doctmst on doctmst.doctor_id=pres.doctor_id "
					+ " LEFT JOIN public.m_user_master um on pres.added_by = um.user_id  left join public.m_employee_master empmst on um.user_id = empmst.user_id "
					+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id"
					+"  LEFT JOIN m_unit_of_measurement muom on muom.item_id   = presDtl.item_id  and  muom.ip_uom  = true and muom.is_active  = true    left join m_uom_unit muu on muu.id = muom.uom_unit_id "
					+ " left join (select max(b.id) as batchId , b.item_id as itemId from inventory.t_batch b "
					+ " where b.unit_id = :unitId and is_active = true and to_char(b.expiry_date, 'yyyy-mm-dd') >= :expDate group by b.item_id  )  "
					+ " batchRate on batchRate.itemId = presDtl.item_id  "
					+ " left join inventory.t_batch bt on bt.id = batchRate.batchId "
					+ " WHERE pres.patient_id=:patientId "
					+ " AND presDtl.is_medication_stop='N'  "
					+ " AND presDtl.status_id != 9 "
					+ " AND preFreDtl.stop_date >='"+currentDate+"' ";
			
			
			List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", patientId)
					 .setParameter("expDate", toDayDate)
					 .setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getActiveMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getFrequencyDetails() throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<FrequencyMasterDTO> listFrequencyDTOs = statlessSession.getNamedQuery("GET_FREQUENCY_DETAILS")
					.setResultTransformer(Transformers.aliasToBean(FrequencyMasterDTO.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listFrequencyDTOs, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getFrequencyDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getRoaMasters() throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<RoaMasterDto> roaMasterDtos = statlessSession.getNamedQuery("GET_ROA_MASTERS")
					.setResultTransformer(Transformers.aliasToBean(RoaMasterDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getRoaMasters failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getDrugListByTypeSearch(Integer unitId, Integer orgId, String searchDrug) {
		try {
			
			
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//			String toDayDate = simpleDateFormat.format(new Date());
			String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
			if(!searchDrug.equals("")) {
				searchDrug = searchDrug.replaceAll("[%$]", "");
				String query = " select " 
			+ "    item.id as \"drugId\", "
						+ "    concat(item.name, ' | ', 'Qty : ',  coalesce(stock.totalQty,0) ) as \"medicationName\", "
//						+ "   coalesce((select CAST((b.rate) as decimal(10, 2))    " + 
//						" from inventory.t_batch b    " + 
//						" inner join inventory.t_store_stock S    " + 
//						" on B.id = S.batch_id    " + 
//						" where b.item_id = item.id   " + 
//						" and s.store_id in (   " + 
//						"  select store_id    " + 
//						" from m_store_item_mapper msim    " + 
//						" where item_id = item.id and unit_id = :unitId and store_id in    " + 
//						" ( select ms.store_id from m_store ms   " + 
//						" where ms.unit_id = :unitId and ms.is_pharmacy = true))    " + 
//						" and b.unit_id = :unitId   " + 
//						" and s.unit_id = :unitId   " + 
//						" and to_char(b.expiry_date, 'yyyy-mm-dd') >=:expDate   " + 
//						" and b.is_quarantine = false   " + 
//						" order by b.id desc limit 1),0) as \"rate\","
						+ " coalesce((r.rate ), 0) as \"rate\", "
						+ "    item.is_dose_calculation_applicable as \"isDoseCalculationApplicable\",  "
						+ "    item.dose_calculation_applicable_data as \"doseCalculationApplicableData\",  "
						+ "    item.generic_id as \"genericId\",  " 
						+ "    item.cims_code as \"cimsCode\",  " 
						+ "    item.cims_type as \"cimsType\",  " 
						+ "    generic.name as \"genericName\",  "
						+ "    item.formulation_type_id as \"formulationTypeId\",  "
						+ "    item.strength as \"strength\",  "
						+ "    item.strength_unit_id as \"strengthUnitId\",  "
						+ "    item.is_narcotic as \"isNarcotic\",  " 
						+ "    strnUn.unit as \"unitDesc\"  "
						+ " from  " + "    public.m_item item  " 
						+ " inner join public.m_generic generic on  "
						+ "    generic.id = item.generic_id  " 
						+ " left join public.m_strength_unit strnUn on  "
						+ "    strnUn.id = item.strength_unit_id  " 
						+ "     " + " left join (  "
						+ "    select  " 
						+ "        item.id as itemId,  "
						+ "        sum(stock.available_qty) as totalQty  " 
						+ "    from  "
						+ "        public.m_item item  " 
						+ "    inner join inventory.t_batch batch on  "
						+ "        batch.item_id = item.id  " 
						+ "        and (batch.is_deleted = false  "
						+ "            and batch.is_active = true) and batch.is_quarantine =false  "
						+ "            and batch.unit_id =:unitId and (batch.expiry_date >='" 
						+ toDayDate + "'  or batch.expiry_date is null)"
						+ "        inner join inventory.t_store_stock stock on  "
						+ "            stock.batch_id = batch.id  "
						+ "        inner join public.m_store ms  on stock.store_id =ms.store_id  "
						+ "            and stock.unit_id =:unitId and ms.is_pharmacy =true  "
						+ "            and (stock.is_deleted = false  "
						+ "                and stock.is_active = true  "
						+ "                and stock.available_qty > 0)  " 
						+ "        group by  "
						+ "            item.id) stock on  " 
						+ "    stock.itemId = item.id     " 
						
						+ " left join (	select max(cast((b.rate) as decimal(10, 2))) rate , b.item_id from inventory.t_batch b "  
						+ "	inner join inventory.t_store_stock S on B.id = S.batch_id inner join m_store ms on s.store_id = ms.store_id "  
						+ "	inner join m_store_item_mapper msim on msim.store_id=ms.store_id and msim.item_id=b.item_id " + 
						"	where to_char(b.expiry_date, 'yyyy-dd-mm') >= :expDate and b.is_quarantine = false and b.unit_id =:unitId " + 
						"	and s.unit_id =:unitId	and ms.is_pharmacy = true and ms.unit_id =:unitId	group by  b.item_id ) r on r.item_id=item.id "
						
						+ " where "
						+ " item.organization_id =:orgId " 
						+ " and item.is_consumable = true "
						+ " and item.is_active = true " 
						+ " and item.is_deleted = false "
						+ " and lower(item.name) like lower('%" 
						+ searchDrug
						+ "%') order by coalesce(stock.totalQty,0) Desc";
				
				List<MedicationListDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId",unitId)
						 .setParameter("expDate", toDayDate)
						.setParameter("orgId",orgId)
						.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
						}else {
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
						}
						} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
						} catch (Exception e) {
						logger.error("getDrugListByTypeSearch failed", e);
						}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getRoaListByFormulationTypeId(Integer unitId, Integer orgId, Integer formulationTypeId,Integer genericId, Integer patientId) throws ApplicationException {
		try {
			
			Response res = checkGenericId(genericId,patientId, unitId, orgId);
			String query = "SELECT roa.id as \"roa_id\", "
						 + " roa.description as \"roa_details\" "
						 + " FROM public.m_formulation_and_roa fnr "
						 + " INNER JOIN public.m_route_of_administration roa ON roa.id=fnr.roa_id "
						 + " WHERE fnr.is_active=TRUE "
						 + " AND fnr.is_deleted=FALSE "
						 + " AND fnr.organization_id=:orgId "
//						 + "AND fnr.unit_id=:unitId "
						 + " AND fnr.formulation_type_id=:formulationTypeId"
						 + " ORDER BY roa.description ASC";
			
			List<RoaMasterDto> roaMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("formulationTypeId",formulationTypeId)
					.setParameter("orgId", orgId)
//					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(RoaMasterDto.class)).list();
			int size =(res.getListObject()!=null&&!res.getListObject().isEmpty())?res.getListObject().size():0;
			
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtosList, size);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getRoaListByFormulationTypeId failed", e);
		}
		return new Response(ERROR, SUCCESS_CODE, null, null, null);
	}

	@Override
	public Response getFrequencyList(Integer unitId, Integer orgId, String searchPar) throws ApplicationException {
		try {
			String query = "SELECT frequency.id as \"frequency_id\", "
						 + "frequency.description as \"frequency\", "
						 + "frequency.no_of_administration_time as \"noOfAdministrationTime\", "
						 + "frequency.code as \"code\""
						 + "FROM public.m_frequency frequency "
						 + "WHERE frequency.organization_id=:orgId "
//						 + "AND frequency.unit_id=:unitId "
						 + "AND frequency.is_active=TRUE "
						 + "AND frequency.is_deleted=FALSE "
						 + "AND lower(frequency.description) ILIKE '%"+searchPar+"%' ";
			
			List<FrequencyMasterDTO> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("unitId",unitId)
					.setParameter("orgId",orgId)
					.setResultTransformer(Transformers.aliasToBean(FrequencyMasterDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getFrequencyList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	}

	@Override
	public Response getUOMList(Integer unitId, Integer orgId, Integer itemId) throws ApplicationException {
		try {
			String query ="SELECT item.id as \"drugId\", "
					+ " uunit.id as \"uomId\", "
					+ " uunit.unit as \"uom\", "
					+ " uom.uom_type_id as \"uomTypeId\", "
					+ " uom.conversions as \"conversions\", "
					+ " uunit.unit as \"unit\" "
					+ " FROM public.m_item item "
					+ " INNER JOIN public.m_unit_of_measurement uom ON uom.item_id=item.id "
					+ " INNER JOIN public.m_uom_unit uunit ON uom.uom_unit_id = uunit.id  "	
					+ " WHERE item.organization_id=:orgId "
//					+ " AND item.unit_id=:unitId "
					+ " AND item.id=:itemId "
					+ " AND op_uom=TRUE "	
					+ " AND item.is_active=TRUE "
					+ " AND item.is_deleted=FALSE ";
			List<FrequencyMasterDTO> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("unitId",unitId)
					.setParameter("orgId",orgId)
					.setParameter("itemId", itemId)
					.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getUOMList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	}

	@Override
	public Response savePrescription(PrescriptionDto prescriptionDto) throws ApplicationException {
		try {
			
			if(prescriptionDto.getDoctorId() == null) {
			String sql = " select doctor_id as \"doctorId\" ,speciality_id as \"specialityId\" from adt.t_admission ta where admission_id =:id " + 
					" union all  " + 
					" select doctor_id as \"doctorId\", dept_id as \"specialityId\" from opd.t_encounter_master ta where encounter_id =:id ";

			PrescriptionDto dto = (PrescriptionDto)sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("id", prescriptionDto.getAdmissionId() != null ? prescriptionDto.getAdmissionId() :prescriptionDto.getEncounterId())
					.setMaxResults(1)
					.setResultTransformer(Transformers.aliasToBean(PrescriptionDto.class)).uniqueResult();
			if(dto.getDoctorId()!= null && dto.getSpecialityId() != null) {
				prescriptionDto.setDoctorId(dto.getDoctorId());
				prescriptionDto.setSpecialityId(dto.getSpecialityId());
				prescriptionDto.setDeptId(dto.getSpecialityId());
			}
			
			
			}
			 prescriptionDto.setIsOtims(prescriptionDto.getIsOtims() != null ? prescriptionDto.getIsOtims() :false);

			PrescriptionMaster prescriptionMaster = mapper.map(prescriptionDto, PrescriptionMaster.class,
					"PrescriptionMasterDto_to_PrescriptionMaster");
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				String json = objectMapper
						.writerWithDefaultPrettyPrinter()
						.writeValueAsString(prescriptionMaster);
				System.out.println(json);
			} catch (Exception e) {
			logger.error("savePrescription failed", e);
			}
			prescriptionMaster.setAddedDate(TimeZoneComponent.getDateByzone(prescriptionMaster.getUnitId()));
			prescriptionMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(prescriptionMaster.getUnitId()));
			prescriptionMaster.setPrescriptionDate(TimeZoneComponent.getDateByzone(prescriptionMaster.getUnitId()));

			if(prescriptionDto.getPrescriptionDetailsDtosList()!=null) {
			 if(prescriptionDto.getPrescriptionDetailsDtosList().getFirst().getIsDischarge()!=null && prescriptionDto.getPrescriptionDetailsDtosList().getFirst().getIsDischarge().equals(true)){
				 prescriptionMaster.setReconciliationStatusId(2);//pending
				 prescriptionMaster.setReconciliationDone(false);
			 }else {
				 prescriptionMaster.setReconciliationDone(true);
			 }
			}else {
				prescriptionMaster.setReconciliationDone(true);
			}
			prescriptionMaster.setDoctorId((prescriptionDto.getDoctorId()!=null && prescriptionDto.getDoctorId()>0)?prescriptionDto.getDoctorId():null);
			sessionFactory.getCurrentSession().save(prescriptionMaster);
			prescriptionDto.setPrescriptionId(prescriptionMaster.getPrescriptionId());
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDto);
		} catch (HibernateException he) {
		logger.error("savePrescription failed", he);
		} catch (Exception e) {
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response savePrescriptionDetais(PrescriptionDetailsDto prescriptionDetailsDto) throws ApplicationException {
		try {
				PrescriptionDetailsMaster prescriptionDetailsMaster = mapper.map(prescriptionDetailsDto, PrescriptionDetailsMaster.class,
						"PrescriptionDetailsMasterDto_to_PrescriptionDetailsMaster");
				if(prescriptionDetailsDto.getIsDischarge()!=null) {
					prescriptionDetailsMaster.setDisPrescribedQty(prescriptionDetailsDto.getOrderQty());
				}
				if(prescriptionDetailsMaster!=null) {
					String sql = "select ms.store_id as \"storeId\" from m_store ms where ms.unit_id = :unitId and ms.is_active = true order by 1 desc limit 1";
					Query query = getSession().createSQLQuery(sql);
					query.setParameter("unitId", prescriptionDetailsDto.getUnitId());
					Integer convertedBatchHql = (Integer) query.uniqueResult();
					prescriptionDetailsMaster.setStoreId(convertedBatchHql);
					}
				prescriptionDetailsMaster.setIsMedicationStop(prescriptionDetailsMaster.getIsMedicationStop()=='\u0000'?'N':prescriptionDetailsMaster.getIsMedicationStop());
				sessionFactory.getCurrentSession().saveOrUpdate(prescriptionDetailsMaster);
				prescriptionDetailsDto.setPrescriptionDetailsid(prescriptionDetailsMaster.getPrescriptionDetailsId());
			
			
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDetailsDto);
		} catch (HibernateException he) {
		logger.error("savePrescriptionDetais failed", he);
		} catch (Exception e) {
		logger.error("savePrescriptionDetais failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response savePrescriptionFrequencyDetails(PrescriptionDetailsDto prescriptionDetailsDto)
			throws ApplicationException {
		try {

			PrescriptionFrequencyMaster prescriptionFrequencyMaster = mapper.map(prescriptionDetailsDto, PrescriptionFrequencyMaster.class,
					"PrescriptionDetailsMasterDto_to_PrescriptionFrequencyMaster");
			sessionFactory.getCurrentSession().saveOrUpdate(prescriptionFrequencyMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDetailsDto);
		} catch (HibernateException he) {
		logger.error("savePrescriptionFrequencyDetails failed", he);
		} catch (Exception e) {
		logger.error("savePrescriptionFrequencyDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getEncounterMedicationHistory(MedicationDTO medicationDTO) throws ApplicationException {
		try {
			
			String query ="SELECT distinct(encounter.encounterId) as encounterId,"
			+ "to_char(encounter.encounterDate,'dd/mm/yyyy') as encounterDateString, "
			+ "encounter.doctorId as doctorId, "
			+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
			+ "docSpec.specialityId as specialityId, "
			+ "speciality.specialityName as specialityName "
			//+ "'OP' as visitType "
			+ "FROM EncounterMaster encounter "
			+ "INNER JOIN encounter.doctorMaster doc "
			+ "INNER JOIN doc.docotrSpecialityMapperslist docSpec "
			+ "INNER JOIN docSpec.specialityMaster speciality "
			+ "WHERE encounter.organizationId=:orgId "
			//+ "AND encounter.unitId=:unitId "
			+ "AND encounter.doctorId=:doctorId "
			//+ "AND encounter.deptId=:specialityId "
			+ "AND encounter.patientId=:patientId "
			+ "AND encounter.encounterId!=:encounterId ";
			
			
			List<EncounterDetailsDto> roaMasterDtos = sessionFactory.getCurrentSession().createQuery(query)
					//.setParameter("unitId",medicationDTO.getUnitId())
					.setParameter("orgId",medicationDTO.getOrganizationId())
					.setParameter("doctorId", medicationDTO.getDoctorId())
					//.setParameter("specialityId", medicationDTO.getSpecialityId())
					.setParameter("patientId", medicationDTO.getPatientId())
					.setParameter("encounterId", medicationDTO.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(EncounterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getEncounterMedicationHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	}
	@Override
	public Response getMedicationHistoryByEncounterId(MedicationDTO medicationDTO) throws ApplicationException {
		try {
			
			String query = "SELECT presDtl.item_id as \"itemId\", " + 
					" itm.name as \"itemName\", " + 
					" preFreDtl.dose as \"doseDesc\", " + 
					" su.unit as \"strengthDesc\", " + 
					" preFreDtl.frequency_id as \"frequencyId\", " +
					" preFreDtl.dose_unit_id as \"doseUnitId\",  " +
					" itm.is_dose_calculation_applicable as \"isDoseCalculationApplicable\", " +
					" itm.dose_calculation_applicable_data as \"doseCalculationApplicableData\", " +
					" freq.no_of_administration_time as \"noOfAdministrationTime\",  " +
					" freq.description as \"frequencyDesc\", " + 
					" preFreDtl.route_id as \"roaId\", " + 
					" presDtl.order_qty as \"quantity\", " +
					" presDtl.is_non_formulary as \"isNonFormulary\",   " +
					" roa.description as \"roaDesc\", " +
					" itm.formulation_type_id as \"formulationTypeId\", " +
					" itm.generic_id as \"genericId\", " +
					" to_char(preFreDtl.start_date, 'dd/mm/yyyy HH:mm:ss') as \"startDate\",   " +
					" to_char(preFreDtl.stop_date, 'dd/mm/yyyy HH:mm:ss') as \"stopDate\",   " +
					" to_char(preFreDtl.stop_date, 'dd/mm/yyyy HH:mm:ss') as \"endDate\",   " +
					" presDtl.instruction_id as \"instructionId\" ," +
					" presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\", " +
					" presDtl.is_non_formulary as \"nonFormularyAdvice\", " +
					" instr.instruction_desc as \"instructionDesc\", " + 
					" presDtl.is_medication_stop as \"isMedicationStop\","+
					" presDtl.duration_unit as \"durationUnit\","+
					" presDtl.duration as \"durationCount\","+
					" gen.name as \"genericName\" " +
					" FROM pharmacy.t_prescription pres " +
					" INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
					" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
					" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
					" INNER JOIN public.m_item itm on itm.id=presDtl.item_id " + 
					" LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
					" INNER JOIN public.m_generic gen on itm.generic_id = gen.id " +
					" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " +
					" LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id " +
					" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id " ;
//					" WHERE (to_char(prefredtl.stop_date, 'DD/MM/YYYY') < :stopDate or pres.is_medication_stop= 'Y')";
//                    " WHERE to_char(prefredtl.stop_date, 'yyyy-mm-dd') < :stopDate ";
			StringBuffer s1 = new StringBuffer(query);
			if(medicationDTO.getEncounterId()!=null) {
				s1.append(" WHERE pres.encounter_id = :encounterId " +
                        " and (presDtl.is_non_formulary <> 'Y' " +
                        " OR presDtl.is_non_formulary IS NULL)");
			}else {
				s1.append(" WHERE pres.admission_id = :admissionId" +
                        " and (presDtl.is_non_formulary <> 'Y' " +
                        " OR presDtl.is_non_formulary IS NULL)");
			}

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(s1.toString());

			if(medicationDTO.getEncounterId() != null){
				sqlQuery.setParameter("encounterId", medicationDTO.getEncounterId());
			}
			if(medicationDTO.getAdmissionId() != null){
				sqlQuery.setParameter("admissionId", medicationDTO.getAdmissionId());
			}

			List<MedicationDTO> roaMasterDtos = sqlQuery
//					.setParameter("stopDate", medicationDTO.getStopDate())
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();

            List <MedicationDTO> nonFormularyList = this.getMedicationHistoryByEncounterIdNonFormulary(medicationDTO);
            if(nonFormularyList != null & !(nonFormularyList.isEmpty())){
                roaMasterDtos.addAll(nonFormularyList);
            }

			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMedicationHistoryByEncounterId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	}

	@Override
	public Response checkGenericId(Integer genericId, Integer patientId,Integer unitId, Integer orgId) throws ApplicationException {
		try {
			String query ="SELECT apm.allergy_patient_mapp_id as \"allergyPatientMapperId\" "
					+ " FROM patient.t_allergy_patient_mapper apm "
					+ " WHERE apm.organization_id=:orgId "
//					+ " AND apm.unit_id=:unitId "
//					+ " AND apm.generic_id=:genericId "
					+ " AND apm.status='A' ";
			
			StringBuffer s1 = new StringBuffer(query);
			//s1.append((patientId!=null&&patientId!=0)? " AND apm.patient_id = "+patientId:"");
			//s1.append((genericId!=null&&genericId!=0)? " AND apm.generic_id = "+genericId:"");
			if (patientId != null && patientId != 0) {
				s1.append(" AND apm.patient_id = :patientId");
			}
			if (genericId != null && genericId != 0) {
				s1.append(" AND apm.generic_id = :genericId");
			}

			Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(s1.toString())
//					.setParameter("unitId",unitId)
					.setParameter("orgId",orgId);
//					.setParameter("genericId", genericId)

			if (patientId != null && patientId != 0) {
				createQuery.setParameter("patientId", patientId);
			}

			if (genericId != null && genericId != 0) {
				createQuery.setParameter("genericId", genericId);
			}

			List<AllergyPatientMapperDto> allergyPatientMapperDto=createQuery.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, allergyPatientMapperDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("checkGenericId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		
	}

	@Override
	public Response getMedicationByAdmissionId(Integer admissionId) throws ApplicationException {
		try {
			
			String query = "SELECT presDtl.item_id as \"itemId\", "
					+ " itm.name as \"itemName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " su.unit as \"strengthDesc\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " roa.description as \"roaDesc\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " pres.priority_id as \"priorityId\", "
					+ " pres.medication_type as \"medicationType\", "
					+ " presDtl.statflag as \"statflag\", "
					+ " presDtl.status_id as \"statusId\", "
					+ " presDtl.order_qty as \"orderQty\", "
					+ " gen.name as \"genericName\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " INNER JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " WHERE pres.admission_id=:admissionId ";
//			        + " WHERE presDtl.status_id !=9 ";
			
			
			List<MedicationDTO> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMedicationByAdmissionId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	}

	@Override
	public Response getMedicationDetails(PrescriptionDto prescriptionDto) throws ApplicationException {
try {
			
			String query = "SELECT "
					+ "pres.id as \"prescriptionId\", "
					+ "pres.prescription_no as \"prescriptionNo\", "
					+ "pres.medication_type as \"medicationType\", "
					+ "  presDtl.is_medication_stop as \"isMedicationStop\", " + 
					" presDtl.justification_stop as \"justificationStop\", "
					+ " presDtl.item_id as \"itemId\", "
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " presDtl.is_non_formulary as \"isNonFormulary\", "
					+ " presDtl.priority_id as \"priorityId\", "
					+ " case when(itm.name is null) then presDtl.item_name else itm.name end as \"itemName\", "
					+  " case when(gen.name is null) then presDtl.generic_name else gen.name end as \"genericName\","
					+ " su.unit as \"strengthDesc\", " 
					+ " preFreDtl.start_date as \"startDateNew\"," 
					+ " preFreDtl.stop_date as \"endDateNew\", "
					+ " su.id as \"doseUnitId\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " presDtl.statflag as \"statflag\", "
					+ " presDtl.status_id as \"statusId\", "
					+ " roa.description as \"roaDesc\", "
					+ " presDtl.order_qty as \"orderQty\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " to_char(presDtl.added_date,'dd/mm/yyyy') as \"addedDate\"," 
					+ " to_char(presDtl.added_date, 'HH24:MI') as \"addedTime\"," 
					+ " (case " 
					+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "  
					+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
					+ " else 'Admin' end) as \"userName\","	
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\","
					+ "    itm.cims_code as \"cimsCode\",  " 
					+ "    itm.cims_type as \"cimsType\",  "
					+ " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
                    + " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
					+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\","
					+ " muu.unit as \"selectedUom\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " inner join m_unit_of_measurement muom on muom.item_id   = presDtl.item_id  and  muom.ip_uom  = true and muom.is_active  = true " + 
					"   inner join m_uom_unit muu on muu.id = muom.uom_unit_id  "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id "
					+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
					+ " left JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " left JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
					+ " left join public.m_user_master um on pres.added_by = um.user_id " 
					+ " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
					+ " left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
					+ " WHERE pres.patient_id=:patientId "
					+ " AND presDtl.is_medication_stop='N'  "
			        + " AND presDtl.status_id != 9 ";

			

			StringBuffer sb = new StringBuffer(query);
			if(prescriptionDto.getEncounterId()!=null){
				sb.append(" AND pres.encounter_id = :encounterId");
				sb.append("  order by pres.id asc" );
			}
			if(prescriptionDto.getAdmissionId()!=null){
				sb.append(" AND pres.admission_id = :admissionId");
				sb.append("  order by pres.id asc" );
			}			
			Query createQuery= getSession().createSQLQuery(sb.toString())
			.setParameter("patientId", prescriptionDto.getPatientId());


	if (prescriptionDto.getEncounterId() != null) {
		createQuery.setParameter("encounterId", prescriptionDto.getEncounterId());
	}

	if (prescriptionDto.getAdmissionId() != null) {
		createQuery.setParameter("admissionId", prescriptionDto.getAdmissionId());
	}


	List<MedicationDTO> listMedicationDto=createQuery.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMedicationDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getICPCSYSTEMList() throws ApplicationException {
		try {
			String query ="SELECT icpcSys.icpc_system_id as \"icpcSystemId\", "
					+ " icpcSys.icpc_system_desc as \"icpcSystemDesc\" "
					+ " FROM emr.m_icpc_system icpcSys "
					+ " WHERE icpcSys.status='A' "
					+ " order by icpcSys.icpc_system_desc asc ";
			List<ICPCSystemDTO> icpcSystemMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, icpcSystemMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getICPCSYSTEMList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	}

	@Override
	public Response getICPCMasterList(Integer icpcSyatemId, String searchICPC) throws ApplicationException {
		try {
			String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "WHERE icpc.status='A' "
						 + "AND icpc.icpc_system_id=:icpcSyatemId "
						 + "AND lower(icpc.icpc_desc) LIKE '%"+searchICPC+"%' ";
			
			List<ICPCSystemDTO> icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("icpcSyatemId",icpcSyatemId)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getICPCMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getRoaListByFormulationTypeId(Integer unitId, Integer orgId, Integer patientId)
			throws ApplicationException {
try {
			Integer genericId=0;
			Response res = checkGenericId(genericId,patientId,unitId, orgId);
/*			String query = "SELECT fnr.roa_id as \"roa_id\", "
						 + "roa.description as \"roa_details\" "
						 + "FROM public.m_formulation_and_roa fnr "
						 + "INNER JOIN public.m_route_of_administration roa ON roa.id=fnr.roa_id "
						 + "WHERE fnr.is_active=TRUE "
						 + "AND fnr.is_deleted=FALSE "
						 + "AND fnr.organization_id=:orgId "
						 + "AND fnr.unit_id=:unitId ";*/
			String query = " select roa.id as \"roa_id\" ,"
					+ " roa.description as \"roa_details\" "
					+ " from m_route_of_administration roa "
					+ " where roa.is_active=true AND roa.organization_id=:orgId " 
//					+ " AND roa.unit_id=:unitId "
					+ "order by roa.description asc";
			
			List<RoaMasterDto> roaMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", orgId)
//					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(RoaMasterDto.class)).list();
			int size =(res.getListObject()!=null&&!res.getListObject().isEmpty())?res.getListObject().size():0;
			
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtosList, size);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getRoaListByFormulationTypeId failed", e);
		}
		return new Response(ERROR, SUCCESS_CODE, null, null, null);
	}
	
	@Override
	public Response getMasterListOfICPCSystem(Integer icpcSystemId, String searchICPC) throws ApplicationException {
		try {
			String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "WHERE icpc.status='A' "
						 + "AND icpc.icpc_system_id=:icpcSystemId "
						 + "AND lower(icpc.icpc_desc) LIKE '%"+searchICPC.toLowerCase()+"%' ";
//						 + "AND icpc.icpc_desc LIKE '%"+searchICPC+"%' ";
			
			List<ICPCSystemDTO> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("icpcSystemId",icpcSystemId)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMasterListOfICPCSystem failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


/*	@Override
	public Response getICPCMasterListByICPCSystemId(Integer icpcSystemId) throws ApplicationException {
		try {
			String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "WHERE icpc.status='A' "
						 + "AND icpc.icpc_system_id=:icpcSystemId "
						 + "order by icpc.icpc_code ";
			
			List<ICPCSystemDTO> icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("icpcSystemId",icpcSystemId)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
		} catch (HibernateException he) {
			h
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getICPCDescriptionByICPCId(Integer icpcId) throws ApplicationException {
		try {
			String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "WHERE icpc.status='A' "
						 + "AND icpc.icpc_id=:icpcId ";
			
			ICPCSystemDTO icpcMasterDtos = (ICPCSystemDTO) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("icpcId",icpcId)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, icpcMasterDtos);
		} catch (HibernateException he) {
			h
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getICPCSystemNameById(Integer icpcSystemId) throws ApplicationException {
		try {
			String query = "SELECT icpc.icpc_system_id  as \"icpcSystemId\", "
						 + "icpc.icpc_system_desc  as \"icpcSystemDesc\" "
						 + "FROM emr.m_icpc_system icpc "
						 + "WHERE icpc.icpc_system_id=:icpcSystemId ";
			
			ICPCSystemDTO icpcMasterDtos = (ICPCSystemDTO) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("icpcSystemId",icpcSystemId)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, icpcMasterDtos);
		} catch (HibernateException he) {
			h
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	
*/
	@Override
	public Response getDateWiseListOfPatientMedication(Integer patientId, Integer  organizationId, String fromDate, String toDate) throws ApplicationException {
		try {
			
			/*String query = "SELECT distinct("
					+ " ent.encounter_id) as \"encounterId\","
					+ " to_char(ent. encounter_date, 'DD/MM/YYYY') as \"encounterDate\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " INNER JOIN opd.t_encounter_master ent on ent.encounter_id=pres.encounter_id "
					+ " WHERE pres.patient_id=:patientId "
					+ " AND pres.encounter_id=:encounterId "
					+ " AND pres.organization_id=:organizationId "
					+ " AND pres.unit_id=:unitId ";*/
			String query = " SELECT "+
					" to_char(ent.encounter_date, 'DD/MM/YYYY') as \"encounterDate\", " + 
					" to_char(pres.prescription_date , 'DD/MM/YYYY') as \"prescriptionDate\" " +
					" FROM pharmacy.t_prescription pres " + 
					" INNER JOIN opd.t_encounter_master ent on ent.encounter_id=pres.encounter_id " + 
					" WHERE pres.patient_id=:patientId" + 
					" AND pres.organization_id=:organizationId" 
					+ " AND to_char(pres.prescription_date , 'YYYY-MM-DD')  BETWEEN  '"+fromDate+"' AND '"+toDate+"'"
					+ " union " + 
					"  SELECT "+
					" to_char(admDtl.doa, 'DD/MM/YYYY') as \"encounterDate\", " + 
					" to_char(pres.prescription_date , 'DD/MM/YYYY') as \"prescriptionDate\" " +
					" FROM pharmacy.t_prescription pres " + 
					" INNER JOIN adt.t_admission adm on adm.admission_id=pres.admission_id" + 
					" INNER JOIN adt.t_admission_details admDtl on admDtl.admission_id=pres.admission_id" + 
					" WHERE pres.patient_id=:patientId" + 
					" AND pres.organization_id=:organizationId "
					+ " AND to_char(pres.prescription_date , 'YYYY-MM-DD')  BETWEEN  '"+fromDate+"' AND '"+toDate+"'";
			
			List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", patientId)
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getDateWiseListOfPatientMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getMedicationListById(Integer prescriptionId, Integer organizationId, Integer unitId)
			throws ApplicationException {
try {
	String query = "SELECT "
			+ "pres.id as \"prescriptionId\", "
			+ " presDtl.item_id as \"itemId\", "
			+ " itm.name as \"itemName\", "
			+ " preFreDtl.dose as \"doseDesc\", "
			+ " su.unit as \"strengthDesc\", " 
			+ "su.id as \"doseUnitId\", "
			+ " presDtl.statflag as \"isStat\" , " 
			+ " presDtl.priority_id as \"priorityId\", "
			+ " preFreDtl.frequency_id as \"frequencyId\", "
			+ " freq.description as \"frequencyDesc\", "
			+ " preFreDtl.route_id as \"roaId\", "
			+ " roa.description as \"roa_details\", "
			+ " itm.formulation_type_id as \"formulationTypeId\", "
			+ " itm.generic_id as \"genericId\", "
//			+ " pres.priority_id as \"priorityId\" , " 
			+ " presDtl.order_qty as \"quantity\", "
			+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
			+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
			+ " presDtl.instruction_id as \"instructionId\" ,"
			+ " instr.instruction_desc as \"instructionDesc\", "
			+ " gen.name as \"genericName\", "
			+ " pres.encounter_id as \"encounterId\","
			+ " pres.admission_id as \"admissionId\","
			+ " presDtl.id as \"prescriptionDetailsid\","
			+ " preFreDtl.id as \"prescriptionFrequencyId\"  "
			+ " FROM pharmacy.t_prescription pres "
			+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
			+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
			+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
			+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
			+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
			+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
			+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
			+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
			+ " WHERE pres.id=:prescriptionId "
			+ " AND presDtl.unit_id=:unitId"
			+ " AND presDtl.organization_id=:organizationId";
			
			List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("prescriptionId", prescriptionId)
					.setParameter("unitId", unitId)
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMedicationListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getOPDMedicationListById(Integer prescriptionId,Integer prescriptionDetailsid ,Integer patientId, Integer itemId, Integer unitId, Integer organizationId)
			throws ApplicationException {
try {
		String pattern = "yyyy-MM-dd";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//	    String toDayDate = simpleDateFormat.format(new Date());
	    String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
			String query = "SELECT "
					+ " distinct(pres.id) as \"prescriptionId\", "
					+ " pres.prescription_no as \"prescriptionNo\", "+
					"   coalesce((select CAST((b.rate) as decimal(10, 2))    " + 
					" from inventory.t_batch b    " + 
					" inner join inventory.t_store_stock S    " + 
					" on B.id = S.batch_id    " + 
					" where b.item_id = itm.id   " + 
					" and s.store_id in (   " + 
					"  select store_id    " + 
					" from m_store_item_mapper msim    " + 
					" where item_id = itm.id and unit_id = :unitId and store_id in    " + 
					" ( select ms.store_id from m_store ms   " + 
					" where ms.unit_id = :unitId and ms.is_pharmacy = true))    " + 
					" and b.unit_id = :unitId   " + 
					" and s.unit_id = :unitId   " + 
					" and to_char(b.expiry_date, 'yyyy-mm-dd') >=:expDate   " + 
					" and b.is_quarantine = false   " + 
					" order by b.id desc limit 1),0) as \"rate\","
					+ " presDtl.item_id as \"itemId\", "
					+ " presDtl.strength as \"strength\","
					+ " presDtl.priority_id as \"priorityId\", "
					+ " itm.is_dose_calculation_applicable as \"isDoseCalculationApplicable\", "
					+ " itm.dose_calculation_applicable_data as \"doseCalculationApplicableData\", "
					+ " itm.name as \"searchItem\", "
					+ " preFreDtl.dose as \"dose\", "
					+ " su.unit as \"strengthDesc\", " 
					+ " su.id as \"doseUnitId\", "
					+ " presDtl.statflag as \"isStat\" , " 
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " preFreDtl.frequency_id as \"frequencyId\","
					+ " fr.no_of_administration_time as \"noOfAdministrationTime\", "
					+ " freq.description as \"searchItemFre\", "
					+ " fnr.roa_id as \"roa_id\", "
					+ " roa.description as \"roa_details\", "
					+ " itm.formulation_type_id as \"formulationTypeId\", "
					+ " itm.generic_id as \"genericId\", "
					//+ " pres.priority_id as \"priorityId\" , " 
					+ " presDtl.order_qty as \"quantity\", "
					+ "presDtl.is_discharge_medication as \"isDischargeMedication\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"medicationStartDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"medicationEndDate\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " gen.name as \"genericName\", "
					+ " pres.encounter_id as \"encounterId\","
					+ " pres.admission_id as \"admissionId\","
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\","
					+ " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
                    + " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
					+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
					+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
				    + " LEFT JOIN public.m_formulation_and_roa fnr ON roa.id=fnr.roa_id "
					+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id"
					+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
					+ " INNER JOIN public.m_frequency fr ON preFreDtl.frequency_id = fr.id "
					+ " WHERE pres.id=:prescriptionId "
					+ " AND presDtl.id=:prescriptionDetailsid "
					+ " AND pres.patient_id=:patientId "
					+ " AND presDtl.item_id=:itemId"
					+ " AND presDtl.unit_id=:unitId"
					+ " AND presDtl.organization_id=:organizationId";
			
			
			List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("prescriptionId", prescriptionId)
					.setParameter("prescriptionDetailsid", prescriptionDetailsid)
					.setParameter("patientId", patientId)
					.setParameter("itemId", itemId)
					.setParameter("unitId", unitId)
					.setParameter("organizationId", organizationId)
					 .setParameter("expDate", toDayDate)
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getOPDMedicationListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePrescription(PrescriptionDto prescriptionDto) throws ApplicationException {
		try {

			PrescriptionMaster prescriptionMaster = mapper.map(prescriptionDto, PrescriptionMaster.class,
					"PrescriptionMasterDto_to_PrescriptionMaster");
			prescriptionMaster.setAddedDate(TimeZoneComponent.getDateByzone(prescriptionMaster.getUnitId()));
			prescriptionMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(prescriptionMaster.getUnitId()));
			prescriptionMaster.setPrescriptionDate(TimeZoneComponent.getDateByzone(prescriptionMaster.getUnitId()));
			 if(prescriptionDto.getPrescriptionDetailsDtosList().getFirst().getIsDischarge()!=null && prescriptionDto.getPrescriptionDetailsDtosList().getFirst().getIsDischarge().equals('Y')){
				 prescriptionMaster.setReconciliationDone(false);
				 prescriptionMaster.setReconciliationStatusId(2);
			 }else {
				 prescriptionMaster.setReconciliationDone(true);
			 }
			prescriptionMaster.setDoctorId((prescriptionDto.getDoctorId()!=null && prescriptionDto.getDoctorId()>0)?prescriptionDto.getDoctorId():null);
			prescriptionMaster = update(prescriptionMaster);
			prescriptionDto.setPrescriptionId(prescriptionMaster.getPrescriptionId());
			prescriptionDto.setPrescriptionNo(prescriptionMaster.getPrescriptionNo());
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, prescriptionDto);
		} catch (HibernateException he) {
		logger.error("updatePrescription failed", he);
		} catch (Exception e) {
		logger.error("updatePrescription failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updatePrescriptionDetail(PrescriptionDetailsDto prescriptionDetailsDto)
			throws ApplicationException {
		try {

			PrescriptionDetailsMaster prescriptionDetailsMaster = mapper.map(prescriptionDetailsDto, PrescriptionDetailsMaster.class,
					"PrescriptionDetailsMasterDto_to_PrescriptionDetailsMaster");
			prescriptionDetailsMaster.setIsMedicationStop(prescriptionDetailsMaster.getIsMedicationStop()=='\u0000'?'N':prescriptionDetailsMaster.getIsMedicationStop());
			 sessionFactory.getCurrentSession().update(prescriptionDetailsMaster);
//			prescriptionDetailsDto.setPrescriptionDetailsid(prescriptionDetailsDto.getPrescriptionDetailsid());
			 if(prescriptionDetailsMaster!=null) {
					String sql = "select ms.store_id as \"storeId\" from m_store ms where ms.unit_id = :unitId and ms.is_active = true order by 1 desc limit 1";
					Query query = getSession().createSQLQuery(sql);
					query.setParameter("unitId", prescriptionDetailsDto.getUnitId());
					Integer convertedBatchHql = (Integer) query.uniqueResult();
					prescriptionDetailsMaster.setStoreId(convertedBatchHql);
					}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, prescriptionDetailsDto);
		} catch (HibernateException he) {
		logger.error("updatePrescriptionDetail failed", he);
		} catch (Exception e) {
		logger.error("updatePrescriptionDetail failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	private PrescriptionDetailsMaster update(PrescriptionDetailsMaster prescriptionDetailsMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updatePrescriptionFrequencyDetails(PrescriptionDetailsDto prescriptionDetailsDto)
			throws ApplicationException {
		try {

			PrescriptionFrequencyMaster prescriptionFrequencyMaster = mapper.map(prescriptionDetailsDto, PrescriptionFrequencyMaster.class,
					"PrescriptionDetailsMasterDto_to_PrescriptionFrequencyMaster");
			sessionFactory.getCurrentSession().update(prescriptionFrequencyMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updatePrescriptionFrequencyDetails failed", he);
		} catch (Exception e) {
		logger.error("updatePrescriptionFrequencyDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getDischargeMedication(PrescriptionDto medicationDTOs) throws ApplicationException {
     
		try {
			
			String query = "SELECT "
					+ "pres.id as \"prescriptionId\", "
					+ " presDtl.item_id as \"itemId\", "
					+ " case when (itm.name is null) then presDtl.item_name else itm.name end AS \"itemName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " su.unit as \"strengthDesc\", " 
					+ "su.id as \"doseUnitId\", "
					+ " presDtl.statflag as \"isStat\" , " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " roa.description as \"roa_details\", "
					+ " itm.formulation_type_id as \"formulationTypeId\", "
					+ " mft.type as \"formulationType\", "			
					+ " presDtl.is_medication_stop as \"isMedicationStopHis\", "
					+ " itm.generic_id as \"genericId\", "
					+ " pres.priority_id as \"priorityId\" , " 
					+ " presDtl.order_qty as \"quantity\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " case when (gen.name is null) then presDtl.generic_name else gen.name end AS \"genericName\", "
					+ " pres.encounter_id as \"encounterId\","
					+ " pres.admission_id as \"admissionId\","
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ "('Dr. ' || coalesce(doctorMaster.first_name)|| ' ' || coalesce(doctorMaster.middle_name)|| ' ' || coalesce(doctorMaster.last_name)) as \"doctorName\" ,"
					+ " preFreDtl.id as \"prescriptionFrequencyId\"  "
					+ " FROM pharmacy.t_prescription pres "
					+ " left JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " left JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " left JOIN public.m_formulation_type mft on mft.id=itm.formulation_type_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " left JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " left join doctor.m_doctor_master doctorMaster on pres.doctor_id = doctorMaster.doctor_id"
					//+ " inner join pharmacy.t_issue issue on issue.prescription_id=pres.id "
					+ " WHERE pres.admission_id=:admissionId "
					+ " AND presDtl.is_discharge_medication=:dischargeMedicationFlag "
					+ " AND presDtl.unit_id=:unitId "
					+ " AND presDtl.organization_id=:organizationId"
					+ " AND presDtl.is_medication_stop='N'  "
					+ " AND presDtl.status_id != 9  ";
					//+ " AND issue.status_id=8 ";
			List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("admissionId", medicationDTOs.getAdmissionId())
					.setParameter("unitId", medicationDTOs.getUnitId())
					.setParameter("organizationId", medicationDTOs.getOrganizationId())
					.setParameter("dischargeMedicationFlag",medicationDTOs.getDischargeMedicationFlag())
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getDischargeMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMedicationDetailsByDoctorId(PrescriptionDto prescriptionDto) throws ApplicationException {

try {
/*   
	     
	added_by           
	added_date         
	status_id          
	priority_id        
	encounter_id       
	patient_id         
	prescription_mongo_
	reconciliation_stat
	reconciliation_done
	employee_id        
	dept_id            
	justification_stop 
	is_medication_stop 
	temp_doctor_id     
	temp_patient_id    
	cancel_note        
	is_non_formulary   
	is_otims           
	medication_type    
	cov_template_id   */ 
			String query = "SELECT "
					+ " pres.id as \"prescriptionId\", "
					+ " pres.medication_type as \"medicationType\", "
					+ " pres.organization_id as \"organizationId\", "
					+ " pres.prescription_no as \"prescriptionNo\", "
					+ " pres.unit_id as \"unitId\", "
					+ " pres.status_id as \"statusIdPres\", "
					+ " pres.visit_type_id as \"visitTypeId\", "
					+ " pres.doctor_id as \"doctorId\", "
					+ " pres.dept_id as \"deptId\", "
					+ " presDtl.is_medication_stop as \"isMedicationStop\", "
					+ " presDtl.priority_id as \"priorityId\", "
					+ " pres.added_by as \"addedBy\", "
					+ " presDtl.duration as \"durationCount\", "
					+ " presDtl.duration_unit as \"durationUnit\", "
					+ " presDtl.justification_stop \"justificationStop\", "
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " presDtl.item_id as \"itemId\", "
//					+ "  issue.status_id as \"statusIdIssue\","
//					+ " itm.name as \"itemName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " workbench.result_id as \"resultId\", "
					+ " workbench.workbench_added_date as \"workbenchAddedDate\", "
					+ " workbench.workbench_ended_date as \"workbenchEndDate\", "
					+ " workbench.patient_workbench_id as \"patientWorkbenchId\", "
					+ " workbench.remark as \"workbenchRemark\", "
					+ " case when (itm.name is null) then presDtl.item_name else itm.name end AS \"itemName\", "
					+ " case when (gen.name is null) then presDtl.generic_name else gen.name end AS \"genericName\", "
					+ " su.unit as \"strengthDesc\", " 
                    + " su.id as \"doseUnitId\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " presDtl.is_non_formulary as \"isNonFormulary\", "
					+ " presDtl.status_id as \"statusId\", "
					+ " presDtl.statflag as \"statflag\", "
					+ " roa.description as \"roaDesc\", "
					+ " presDtl.order_qty as \"orderQty\", "
					+ " itm.generic_id as \"genericId\", "
					+ " itm.formulation_type_id as \"formulationTypeId\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " preFreDtl.start_date as \"startDateNew\"," 
					+ " preFreDtl.stop_date as \"endDateNew\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"addedDate\"," 
					+ " to_char(preFreDtl.added_date,'DD/MM/YYYY') as \"createdDate1\","
					+ " presDtl.added_date as \"addedDateNew\","
					+ " to_char(presDtl.added_date, 'HH24:MI') as \"addedTime\"," 
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\" , "
					+ " pres. is_otims as \"isOtims\","
					+ "    itm.cims_code as \"cimsCode\",  " 
					+ "    itm.cims_type as \"cimsType\",  "
					+ " (case " 
					+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "  
					+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
					+ " else 'Admin' end) as \"userName\","	
//					+ " gen.name as \"genericName\","
					+ "presDtl.is_discharge_medication as \"isDischargeMedication\",presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
					+ " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
					+ " ( case when ( select   count (*) from pharmacy.t_issue_detail DSSSSSSSSA   where   DSSSSSSSSA.prescription_detail_id = presDtl.id   and DSSSSSSSSA.status_id != 2   ) > 0 then 2 else null end ) as \"issueId\","
					+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\", "
					+ " muu.unit as \"selectedUom\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " left JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
//					+ "  LEFT JOIN pharmacy.t_issue_detail issue ON issue.prescription_detail_id = presDtl.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " left join m_unit_of_measurement muom on muom.item_id   = presDtl.item_id  and  muom.ip_uom  = true and muom.is_active  = true "
					+ "   left join m_uom_unit muu on muu.id = muom.uom_unit_id  "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id "
					+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id "
					+ " left JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " left JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
					+ " left join public.m_user_master um on pres.added_by = um.user_id "
					+ " left join public.m_employee_master empmst on um.user_id = empmst.user_id "
					+ " left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
					+ " left join adt.t_admitted_patient_orders_workbench workbench on pres.id = workbench.record_id "
				/*	+ " left join pharmacy.t_issue_detail issue on "
					+ "	issue.prescription_detail_id = presDtl.id "
					+ " and issue.status_id!= 2"*/
					+ " WHERE pres.patient_id=:patientId "
//			        + " AND pres.doctor_id=:doctorId "
			        + " AND presDtl.is_medication_stop='N'  "
			        + " AND presDtl.status_id != 9 "
					;
			
			StringBuffer sb = new StringBuffer(query);
			if(prescriptionDto.getEncounterId()!=null){
				sb.append(" AND pres.encounter_id = :encounterId");
//				sb.append("  order by pres.id asc " );
			}
			if(prescriptionDto.getAdmissionId()!=null){
				sb.append(" AND pres.admission_id = :admissionId");
//				sb.append("  order by pres.id asc" );
			}
			/*if(prescriptionDto.getDoctorId()!= null && prescriptionDto.getDoctorId()!= 0){
				sb.append(" AND pres.doctor_id =" +prescriptionDto.getDoctorId());
			}*/
			sb.append("  order by presDtl.id asc" );

			Query createQuery= getSession().createSQLQuery(sb.toString())
			.setParameter("patientId", prescriptionDto.getPatientId());
//			.setParameter("doctorId", prescriptionDto.getDoctorId())

	if (prescriptionDto.getEncounterId() != null) {
		createQuery.setParameter("encounterId", prescriptionDto.getEncounterId());
	}

	if (prescriptionDto.getAdmissionId() != null) {
		createQuery.setParameter("admissionId", prescriptionDto.getAdmissionId());
	}

	List<MedicationDTO> list=createQuery.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();

		/*	List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", patientId)
					.setParameter("doctorId", doctorId)
					.setParameter("encounterId", encounterId)
					.setParameter("currentDate", currentDate)
					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();*/
			return new Response(SUCCESS, SUCCESS_CODE, null, list ,null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMedicationDetailsByDoctorId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	
	}

	@Override
	public Response getStoreIdForNursing(PrescriptionDto medicationDTOs) throws ApplicationException {
try {
				
				if(medicationDTOs.getEncounterId()!=null) {
					List<PrescriptionDto> list=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_STORE_ID_FOR_NURSING_ENCOUNTER_ID")
							.setParameter("patientId", medicationDTOs.getPatientId())
							.setParameter("unitId", medicationDTOs.getUnitId())
							.setParameter("organizationId", medicationDTOs.getOrganizationId())
							.setParameter("encounterId", medicationDTOs.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(PrescriptionDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

				}else {
					List<PrescriptionDto> list=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_STORE_ID_FOR_NURSING_ADMISSION_ID")
							.setParameter("patientId", medicationDTOs.getPatientId())
							.setParameter("unitId", medicationDTOs.getUnitId())
							.setParameter("organizationId", medicationDTOs.getOrganizationId())
							.setParameter("admissionId", medicationDTOs.getAdmissionId())
							.setResultTransformer(Transformers.aliasToBean(PrescriptionDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

				}
			

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getStoreIdForNursing failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	
	}

	@Override
	public Response getMedicationDetailsForMedicationHistory(PrescriptionDto prescriptionDto)
			throws ApplicationException {
try {
			
			String query = "SELECT "
					+ "pres.id as \"prescriptionId\", "
					+ "  presDtl.is_medication_stop as \"isMedicationStop\", " + 
					" presDtl.justification_stop as \"justificationStop\", "
					+ " pres.encounter_id as \"encounterId\", "
					+ " pres.admission_id as \"admissionId\", "
					+ " presDtl.item_id as \"itemId\", "
					+ " itm.name as \"itemName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " su.unit as \"strengthDesc\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " presDtl.statflag as \"statflag\", "
					+ " roa.description as \"roaDesc\", "
					+ " presDtl.order_qty as \"orderQty\", "
					+ " to_char(preFreDtl.added_date,'dd/mm/yyyy') as \"createdDate\"," 
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " to_char(pres.updated_date,'dd/mm/yyyy') as \"updatedDate\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\" , "
					+ " gen.name as \"genericName\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " WHERE pres.patient_id=:patientId "
					+ " AND (presDtl.is_medication_stop='Y' "
					+ " or to_char(preFreDtl.stop_date,'YYYY-MM-DD')<=:prescriptionDate) ";
			
			

			StringBuffer sb = new StringBuffer(query);
//			if(prescriptionDto.getEncounterId()!=null){
//				sb.append(" AND pres.encounter_id = "+prescriptionDto.getEncounterId());
//				sb.append(" order by pres.id asc ");
//			}
//			if(prescriptionDto.getAdmissionId()!=null){
//				sb.append(" AND pres.admission_id =" +prescriptionDto.getAdmissionId());
//				sb.append(" order by pres.id asc " );
//			}			
			List<MedicationDTO> listMedicationDto= getSession().createSQLQuery(sb.toString())
			.setParameter("patientId", prescriptionDto.getPatientId())
			.setParameter("prescriptionDate", prescriptionDto.getPrescriptionDate())
			.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMedicationDetailsForMedicationHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response getListOfLastEncounterAndAdmission(PrescriptionDto medicationDTOs) throws ApplicationException {
	try {
	if(medicationDTOs.getVisitTypeId()==OP) {
		String query ="select" 
				+"	distinct (encounterMaster.encounter_id) as \"encounterId\"," 
				+"	to_char(" 
				+ "	encounterMaster.encounter_date," 
				+ "	'dd/mm/yyyy'" 
				+ "	) as \"visitDate\" ," 
				+"	encounterMaster.encounter_date as \"visitDateNew\" ," 
				+ " visit.visit_type_code as \"visitTypeCode\", " 
				+ "	encounterMaster.doctor_id as \"doctorId\" ," 
				+ "	('Dr. ' || coalesce(doctorMaster.first_name)|| ' ' || coalesce(doctorMaster.middle_name)|| ' ' || coalesce(doctorMaster.last_name)" 
				+ "	) as \"doctorName\" ," 
				+ "	encounterMaster.dept_id as \"specialityId\" ," 
				+ "	specialityMaster.speciality_name as \"specialityName\" "
				+ " from" 
				+ "	opd.t_encounter_master encounterMaster" 
				+ " inner join doctor.m_doctor_master doctorMaster on" 
				+ " 	encounterMaster.doctor_id = doctorMaster.doctor_id" 
			/*	+ " inner join doctor.t_doctor_speciality_mapper doctorMaster1 on" 
				+ "	doctorMaster.doctor_id = doctorMaster1.doctor_id" */
				+ " inner join doctor.m_speciality_master specialityMaster on" 
				+ "	encounterMaster.dept_id = specialityMaster.speciality_id "
				+ " left join public.m_visit_type_master visit on " + 
				"	visit.visit_type_id=encounterMaster.visit_type_id" 
				+ " where" 
				+ "	encounterMaster.organization_id =:orgId" 
				//+ "	and encounterMaster.unit_id =:unitId" 
				+ "	and encounterMaster.doctor_id =:doctorId" 
				+ "	and encounterMaster.dept_id =:specialityId" 
				+ "	and encounterMaster.patient_id =:patientId" 
//				+ "	and pres.visit_type_id =:visitTypeId" 
				+ "	and encounterMaster.encounter_id != :encounterId" ;


	List<EncounterDetailsDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
	//.setParameter("unitId",medicationDTOs.getUnitId())
	.setParameter("orgId",medicationDTOs.getOrganizationId())
	.setParameter("doctorId", medicationDTOs.getDoctorId())
	.setParameter("specialityId", medicationDTOs.getSpecialityId())
	.setParameter("patientId", medicationDTOs.getPatientId())
//	.setParameter("visitTypeId", medicationDTOs.getVisitTypeId())
	.setParameter("encounterId", medicationDTOs.getEncounterId())
	.setResultTransformer(Transformers.aliasToBean(EncounterDetailsDto.class)).list();
	return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
	}
	else if(medicationDTOs.getVisitTypeId()!=OP)
	{
	String query ="select" 
	+"	distinct(adm.admission_id) as \"admissionId\"," 
	+ "	to_char(adtl.doa, 'DD/MM/YYYY') as \"visitDate\"," 
	+ "	adtl.doa as \"visitDateNew\"," 
	+ "	adm.speciality_id as \"specialityId\"," 
	+ "	dept.speciality_name as \"specialityName\","
	+ " visit.visit_type_code as \"visitTypeCode\", " 
	+ "	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\" "
	+ "	from" 
	+ "	adt.t_admission adm " 
	+ " inner join adt.t_admission_details adtl on " 
	+ "	adtl.admission_id = adm.admission_id " 
	+ " inner join doctor.m_doctor_master doc on " 
	+ "	doc.doctor_id=adm.doctor_id"
	+ " inner join doctor.m_speciality_master dept on " 
	+ "	dept.speciality_id=adm.speciality_id "
	+ " inner join public.m_visit_type_master visit on " + 
	"	visit.visit_type_id=adm.visit_type_id "
	+ " where" 
	//+ "	adm.admission_id !=:admissionId" 
	+ "	adm.doctor_id=:doctorId" 
	+ "	and adm.speciality_id=:specialityId" 
	+ "	and adm.patient_id=:patientId" 
	//+ "	and adm.unit_id=:unitId" 
	+ "	and adm.organization_id=:orgId" 
	+ "	and adm.visit_type_id=:visitTypeId" 
	+ "	and adtl.admission_details_id = (select admission_details_id" 
	+ "	from adt.t_admission_details" 
	+ "	where admission_id = adm.admission_id" 
	+ "	order by admission_id desc limit 1)" 
	+ "	and adm.status='I'" 
	+ "	and adm.patient_id=adm.patient_id" 
	+ " order by adm.admission_id desc	" ;


	List<EncounterDetailsDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
	//.setParameter("unitId",medicationDTOs.getUnitId())
	.setParameter("orgId",medicationDTOs.getOrganizationId())
	.setParameter("doctorId", medicationDTOs.getDoctorId())
	.setParameter("specialityId", medicationDTOs.getSpecialityId())
	.setParameter("patientId", medicationDTOs.getPatientId())
	.setParameter("visitTypeId", medicationDTOs.getVisitTypeId())
//	.setParameter("admissionId", medicationDTOs.getAdmissionId())
	.setResultTransformer(Transformers.aliasToBean(EncounterDetailsDto.class)).list();
	return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
	}
	return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (HibernateException he) {
	throw new HibernateException(he.getMessage());
	} catch (Exception e) {
	logger.error("getListOfLastEncounterAndAdmission failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
	}

	/*@Override
	public Response saveJustificationForMedicationStop(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			PrescriptionDetailsMaster prescriptionDetailsMaster=findById(medicationDTOs.getPrescriptionDetailsid());
			prescriptionDetailsMaster.setIsMedicationStop(medicationDTOs.getIsMedicationStop());
			prescriptionDetailsMaster.setJustificationStop(medicationDTOs.getJustificationStop());
			prescriptionDetailsMaster.setUpdatedBy(medicationDTOs.getUpdatedBy());
			prescriptionDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(medicationDTOs.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(prescriptionDetailsMaster);
	
        return new Response(SUCCESS, SUCCESS_CODE, "Medication discontinued successfully.", null, null);
		
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

	@Override
	public Response getLastEncounterAndAdmissionList(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			if(medicationDTOs.getVisitTypeId()==OP) {
				String query ="select" 
						+"	distinct (encounterMaster.encounter_id) as \"encounterId\"," 
						+"	to_char(" 
						+ "	encounterMaster.encounter_date," 
						+ "	'dd/mm/yyyy'" 
						+ "	) as \"visitDate\" ," 
						+ " visit.visit_type_code as \"visitTypeCode\", " 
						+ "	encounterMaster.doctor_id as \"doctorId\" ," 
						+ "	('Dr. ' || coalesce(doctorMaster.first_name)|| ' ' || coalesce(doctorMaster.middle_name)|| ' ' || coalesce(doctorMaster.last_name)" 
						+ "	) as \"doctorName\" ," 
						+ "	encounterMaster.dept_id as \"specialityId\" ," 
						+ "	specialityMaster.speciality_name as \"specialityName\" "
						+ " from" 
						+ "	opd.t_encounter_master encounterMaster" 
						+ " inner join doctor.m_doctor_master doctorMaster on" 
						+ " 	encounterMaster.doctor_id = doctorMaster.doctor_id" 
					/*	+ " inner join doctor.t_doctor_speciality_mapper doctorMaster1 on" 
						+ "	doctorMaster.doctor_id = doctorMaster1.doctor_id" */
						+ " inner join doctor.m_speciality_master specialityMaster on" 
						+ "	encounterMaster.dept_id = specialityMaster.speciality_id "
						+ " left join public.m_visit_type_master visit on " + 
						"	visit.visit_type_id=encounterMaster.visit_type_id" 
						+ " where" 
						+ "	encounterMaster.organization_id =:orgId" 
						//+ "	and encounterMaster.unit_id =:unitId" 
						+ "	and encounterMaster.doctor_id =:doctorId"
//						+ "	and encounterMaster.dept_id =:specialityId" 
						+ "	and encounterMaster.patient_id =:patientId" 
//						+ "	and pres.visit_type_id =:visitTypeId" 
						+ "	and encounterMaster.encounter_id != :encounterId"
						+ " order by encounterMaster.encounter_id desc" ;


			List<EncounterDetailsDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
			//.setParameter("unitId",medicationDTOs.getUnitId())
			.setParameter("orgId",medicationDTOs.getOrganizationId())
			.setParameter("doctorId", medicationDTOs.getDoctorId())
//			.setParameter("specialityId", medicationDTOs.getSpecialityId())
			.setParameter("patientId", medicationDTOs.getPatientId())
//			.setParameter("visitTypeId", medicationDTOs.getVisitTypeId())
			.setParameter("encounterId", medicationDTOs.getEncounterId())
			.setResultTransformer(Transformers.aliasToBean(EncounterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
			}
			else if(medicationDTOs.getVisitTypeId()!=OP)
			{
			String query ="select" 
			+"	distinct(adm.admission_id) as \"admissionId\"," 
			+ "	to_char(adtl.doa, 'DD/MM/YYYY') as \"visitDate\"," 
			+ "	adm.speciality_id as \"specialityId\"," 
			+ "	dept.speciality_name as \"specialityName\","
			+ " visit.visit_type_code as \"visitTypeCode\", " 
			+ "	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\" "
			+ "	from" 
			+ "	adt.t_admission adm " 
			+ " inner join adt.t_admission_details adtl on " 
			+ "	adtl.admission_id = adm.admission_id " 
			+ " inner join doctor.m_doctor_master doc on " 
			+ "	doc.doctor_id=adm.doctor_id"
			+ " inner join doctor.m_speciality_master dept on " 
			+ "	dept.speciality_id=adm.speciality_id "
			+ " inner join public.m_visit_type_master visit on " + 
			"	visit.visit_type_id=adm.visit_type_id "
			+ " where" 
			//+ "	adm.admission_id !=:admissionId" 
			+ "	adm.doctor_id=:doctorId"
//			+ "	and adm.speciality_id=:specialityId" 
			+ "	and adm.patient_id=:patientId"
			//+ "	and adm.unit_id=:unitId" 
			+ "	and adm.organization_id=:orgId" 
//			+ "	and adm.visit_type_id=:visitTypeId" 
			+ "	and adtl.admission_details_id = (select admission_details_id" 
			+ "	from adt.t_admission_details" 
			+ "	where admission_id = adm.admission_id" 
			+ "	order by admission_id desc limit 1)" 
			+ "	and adm.status='I'"
			+ "	and adm.patient_id=adm.patient_id" 
			+ " order by adm.admission_id desc limit 1" ;


			List<EncounterDetailsDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
			//.setParameter("unitId",medicationDTOs.getUnitId())
			.setParameter("orgId",medicationDTOs.getOrganizationId())
			.setParameter("doctorId", medicationDTOs.getDoctorId())
//			.setParameter("specialityId", medicationDTOs.getSpecialityId())
			.setParameter("patientId", medicationDTOs.getPatientId())
//			.setParameter("visitTypeId", medicationDTOs.getVisitTypeId())
//			.setParameter("admissionId", medicationDTOs.getAdmissionId())
			.setResultTransformer(Transformers.aliasToBean(EncounterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
			} catch (Exception e) {
			logger.error("getLastEncounterAndAdmissionList failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);	
			}

	@Override
	public Response getGenericListForGenericSearch(Integer unitId, Integer orgId, String searchDrug)
			throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//			String toDayDate = simpleDateFormat.format(new Date());
			String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
			 
			String query = " select " 
					 + " 	item.id as \"drugId\", " 
					 + " 	item.generic_id as \"genericId\", " 
					 + " 	concat(item.name, ' | ', 'Qty : ',"
					 + "    coalesce((stock.totalQty ),0) ) as \"medicationName\", " 
					 + " 	generic.name || ' | ' || item.name || ' | Qty : ' || coalesce((stock.totalQty ),0)  as \"genericName\", " 
					 + " 	item.formulation_type_id as \"formulationTypeId\", " 
					 + " 	item.strength as \"strength\", " 
					 + " 	item.strength_unit_id as \"strengthUnitId\", " 
					 + " 	strnUn.unit as \"unitDesc\" " 
					 + " from " 
					 + " 	public.m_item item " 
					 + " inner join public.m_generic generic on " 
					 + " 	generic.id = item.generic_id " 
					 + " left join public.m_strength_unit strnUn on " 
					 + " 	strnUn.id = item.strength_unit_id left join (\r\n" + 
					 "    select\r\n" + 
					 "        item.id as itemId,\r\n" + 
					 "        sum(stock.available_qty) as totalQty\r\n" + 
					 "    from\r\n" + 
					 "        public.m_item item\r\n" + 
					 "    inner join inventory.t_batch batch on\r\n" + 
					 "        batch.item_id = item.id\r\n" + 
					 "        and (batch.is_deleted = false\r\n" + 
					 "            and batch.is_active = true) and batch.is_quarantine =false\r\n" + 
					 "            and batch.unit_id =:unitId and batch.expiry_date >='"+ toDayDate +"'" + 
					 "        inner join inventory.t_store_stock stock on\r\n" + 
					 "            stock.batch_id = batch.id\r\n" + 
					 "        inner join public.m_store ms  on stock.store_id =ms.store_id\r\n" + 
					 "            and stock.unit_id =:unitId and ms.is_pharmacy =true\r\n" + 
					 "            and (stock.is_deleted = false\r\n" + 
					 "                and stock.is_active = true\r\n" + 
					 "                and stock.available_qty > 0)\r\n" + 
					 "        group by\r\n" + 
					 "            item.id) stock on\r\n" + 
					 "    stock.itemId = item.id " 
					 + " where " 
					 + " 	item.organization_id =:orgId " 
					 + " 	and item.unit_id =:unitId " 
					 + " 	and item.is_active = true " 
					 + " 	and item.is_deleted = false " 
					 + " 	and lower(generic.name) like lower('%"+searchDrug+"%') " ;
			
			List<MedicationListDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",unitId)
					.setParameter("orgId",orgId)
					.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getGenericListForGenericSearch failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	
	@Override
	public Response getGenericListForBrandNameSearch(Integer unitId, Integer orgId, Integer genericId)
			throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//			String toDayDate = simpleDateFormat.format(new Date());
			String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
			 
			String query = " select " 
					 + " 	item.id as \"drugId\", " 
					 + " 	item.generic_id as \"genericId\", " 
					 + "    coalesce((stock.totalQty ),0) as \"Qty\", "
					 + "    item.name  as \"medicationName\", " 
					 + " 	generic.name  || ' | Qty : ' || coalesce((stock.totalQty ),0)  as \"genericName\", " 
					 + " 	item.formulation_type_id as \"formulationTypeId\", " 
					 + " 	item.strength as \"strength\", " 
					 + " 	item.strength_unit_id as \"strengthUnitId\", " 
					 + " 	strnUn.unit as \"unitDesc\" " 
					 + " from " 
					 + " 	public.m_item item " 
					 + " inner join public.m_generic generic on " 
					 + " 	generic.id = item.generic_id " 
					 + " left join public.m_strength_unit strnUn on " 
					 + " 	strnUn.id = item.strength_unit_id left join (\r\n" + 
					 "    select\r\n" + 
					 "        item.id as itemId,\r\n" + 
					 "        sum(stock.available_qty) as totalQty\r\n" + 
					 "    from\r\n" + 
					 "        public.m_item item\r\n" + 
					 "    inner join inventory.t_batch batch on\r\n" + 
					 "        batch.item_id = item.id\r\n" + 
					 "        and (batch.is_deleted = false\r\n" + 
					 "            and batch.is_active = true) and batch.is_quarantine =false\r\n" + 
					 "            and batch.unit_id =:unitId and batch.expiry_date >='"+ toDayDate +"'" + 
					 "        inner join inventory.t_store_stock stock on\r\n" + 
					 "            stock.batch_id = batch.id\r\n" + 
					 "        inner join public.m_store ms  on stock.store_id =ms.store_id\r\n" + 
					 "            and stock.unit_id =:unitId and ms.is_pharmacy =true\r\n" + 
					 "            and (stock.is_deleted = false\r\n" + 
					 "                and stock.is_active = true\r\n" + 
					 "                and stock.available_qty > 0)\r\n" + 
					 "        group by\r\n" + 
					 "            item.id) stock on\r\n" + 
					 "    stock.itemId = item.id " 
					 + " where " 
					 + " 	item.organization_id =:orgId " 
					 + " 	and item.unit_id =:unitId " 
					 + " 	and item.is_active = true " 
					 + " 	and item.is_deleted = false " 
					 + "    and item.is_deleted = false and generic_id =:genericId "
					 + "    and ((stock.totalQty )>0) ";
					// + " 	and lower(generic.name) like lower('%"+genericId+"%') " ;
			
			List<MedicationListDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",unitId)
					.setParameter("orgId",orgId)
					.setParameter("genericId",genericId)
					.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getGenericListForBrandNameSearch failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPrescriptionIdByEncounterId(Integer encounterId, Boolean boolean1) throws ApplicationException {
		try {
			String query = "  select " + 
					"	cast(pres.id as int) as \"prescriptionId\" ,pres.status_id as \"statusId\" ,"
					+ " presDtl.statflag as \"statflag\" " + 
					"from " + 
					"	pharmacy.t_prescription pres " + 
					" INNER JOIN pharmacy.t_prescription_detail presDtl on presDtl.prescription_id = pres.id" + 
					" where " + 
					"	pres.encounter_id =:encounterId "
					+ " and pres.status_id not in(4,9) " 
					+ " and presDtl.statflag ="+ boolean1
					+ " and presDtl.status_id not in(4,9) " 
					
					;
			
			List<PrescriptionDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("encounterId",encounterId)
					.setResultTransformer(Transformers.aliasToBean(PrescriptionDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getPrescriptionIdByEncounterId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePrescriptionStatusId(Integer encounterId) throws ApplicationException {
		try {
//			String query = "  update pharmacy.t_prescription pres set status_id=18 "
//					+ " where pres.encounter_id =:encounterId" ;
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update pharmacy.t_prescription pres set status_id=18 " 
					+ " where pres.encounter_id ="+encounterId)
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("updatePrescriptionStatusId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response searchMedicationByBrandName(Integer unitId, Integer orgId, String searchKeyword)
			throws ApplicationException {
		try {
			if(!searchKeyword.equals("")) {
				String query =" select " 
						 + " 	item.id as \"drugId\", " 
						 + " 	item.name as \"medicationName\", " 
						 + " 	item.generic_id as \"genericId\", " 
						 + " 	generic.name as \"genericName\", " 
						 + " 	item.formulation_type_id as \"formulationTypeId\", " 
						 + " 	item.strength as \"strength\", " 
						 + " 	item.strength_unit_id as \"strengthUnitId\", " 
						 + " 	strnUn.unit as \"unitDesc\" " 
						 + " from " 
						 + " 	public.m_item item " 
						 + " inner join public.m_generic generic on " 
						 + " 	generic.id = item.generic_id " 
						 + " left join public.m_strength_unit strnUn on " 
						 + " 	strnUn.id = item.strength_unit_id " 
						 + " where " 
						 + " 	item.organization_id =:orgId " 
						 + " 	and item.is_consumable = true " 
						 + " 	and item.is_active = true " 
						 + " 	and item.is_deleted = false " 
						 + " 	and lower(item.name) like lower('%"+searchKeyword+"%') ";
				
				List<MedicationListDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
						//.setParameter("unitId",unitId)
						.setParameter("orgId",orgId)
						.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
			}else {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("searchMedicationByBrandName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	
	}

	@Override
	public Response getOrderedMedicationsForDoctorApp(PrescriptionDto prescriptionDto) throws ApplicationException {
    try {
			
			String query = "SELECT "
					+ " pres.id as \"prescriptionId\", "
					+ " pres.status_id as \"statusIdPres\", "
					+ " presDtl.is_medication_stop as \"isMedicationStop\", "
					+ " presDtl.justification_stop \"justificationStop\", "
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " presDtl.item_id as \"itemId\", "
//					+ "  issue.status_id as \"statusIdIssue\","
//					+ " itm.name as \"itemName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " case when (itm.name is null) then presDtl.item_name else itm.name end AS \"itemName\", "
					+ " case when (gen.name is null) then presDtl.generic_name else gen.name end AS \"genericName\", "
					+ " su.unit as \"strengthDesc\", " 
                    + " su.id as \"doseUnitId\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " presDtl.is_non_formulary as \"isNonFormulary\", "
					+ " presDtl.status_id as \"statusId\", "
					+ " presDtl.statflag as \"statflag\", "
					+ " roa.description as \"roaDesc\", "
					+ " presDtl.order_qty as \"orderQty\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\" , "
					+ " pres. is_otims as \"isOtims\","
					+ " (case " 
					+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "  
					+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
					+ " else 'Admin' end) as \"userName\","	
//					+ " gen.name as \"genericName\","
					+ "presDtl.is_discharge_medication as \"isDischargeMedication\",presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
					+ " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
					+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " left JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
//					+ "  LEFT JOIN pharmacy.t_issue_detail issue ON issue.prescription_detail_id = presDtl.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
					+ " left JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " left JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
					+ " left join public.m_user_master um on pres.added_by = um.user_id " 
					+ " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
					+ " left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
					+ " WHERE pres.patient_id=:patientId "
			        + " AND presDtl.is_medication_stop='N'  "
			        + " AND presDtl.status_id != 9 "
					;
			
			StringBuffer sb = new StringBuffer(query);
			if(prescriptionDto.getEncounterId()!=null){
				sb.append(" AND pres.encounter_id = :encounterId");
			}
			if(prescriptionDto.getAdmissionId()!=null){
				sb.append(" AND pres.admission_id = :admissionId");
			}
			sb.append("  order by presDtl.id desc " );

			 Query createQuery=getSession().createSQLQuery(sb.toString())
			.setParameter("patientId", prescriptionDto.getPatientId());

		if (prescriptionDto.getEncounterId() != null) {
			createQuery.setParameter("encounterId", prescriptionDto.getEncounterId());
		}

		if (prescriptionDto.getAdmissionId() != null) {
			createQuery.setParameter("admissionId", prescriptionDto.getAdmissionId());
		}

		List<MedicationDTO> list=createQuery.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list ,null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getOrderedMedicationsForDoctorApp failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCheckIsNarcoticItem(Integer itemId) throws ApplicationException {
		 try {
			/*	
				String query = "select itm.is_narcotic as \"isNarcotic\" from public.m_item itm "
						+ " where itm.id =:itemId "
						;
				
				
				List<MedicationListDto> list =sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("itemId", itemId)
				.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();*/
			 String sql = "select itm.is_narcotic as \"isNarcotic\" from public.m_item itm "
						+ " where itm.id =:itemId ";
				Query query = getSession().createSQLQuery(sql);
				query.setParameter("itemId", itemId);
				Boolean isNarco = (Boolean) query.uniqueResult();
				PrescriptionDetailsDto obj=new PrescriptionDetailsDto();
				obj.setIsNarcotic(isNarco);

				return new Response(SUCCESS, SUCCESS_CODE, null, null ,obj);

			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
			logger.error("getCheckIsNarcoticItem failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}

	@Override
	public Response getOrderedQty(Integer itemId, Integer prescriptionId, Integer encounterId)
			throws ApplicationException {
		try {
			String query = " select dtl.order_qty  as \"orderQty\", CAST (dtl.id as int)  as \"prescriptionDetailsid\" ," + 
					"	 CAST (presFreq.id as int) as \"prescriptionFrequencyId\" "
					+ " from pharmacy.t_prescription_detail dtl  " + 
					"	inner join pharmacy.t_prescription pres on pres.id=dtl.prescription_id " + 
					"	inner join pharmacy.t_prescription_frequency_detail presFreq on presFreq.prescription_detail_id=dtl.id " + 
					"	where dtl.item_id=:itemId " + 
					"	and dtl.prescription_id=:prescriptionId " + 
					"	and pres.encounter_id=:encounterId " + 
					"	and pres.status_id not in(4,9) " ;
			
			List<PrescriptionDetailsDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("itemId",itemId)
					.setParameter("prescriptionId",prescriptionId)
					.setParameter("encounterId",encounterId)
					.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getOrderedQty failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getFrequentlyUsedMedicationList(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//		    String toDayDate = simpleDateFormat.format(new Date());
		    String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(medicationDTOs.getUnitId()));
			String query = /*" select  " + 
					"	presDtl.item_id as \"itemId\",  " + 
				//	"	--pres.id as \"prescriptionId\",  " + 
					"	pres.status_id as \"statusIdPres\",  " + 
					"	presDtl.is_medication_stop as \"isMedicationStop\",  " + 
					"	presDtl.justification_stop \"justificationStop\",  " + 
					"	presDtl.instruction_desc \"instructionText\",  " + 
					"	preFreDtl.dose as \"doseDesc\",  " + 
					"	case  " + 
					"		when (itm.name is null) then presDtl.item_name  " + 
					"		else itm.name  " + 
					"	end as \"itemName\",  " + 
					"	case  " + 
					"		when (gen.name is null) then presDtl.generic_name  " + 
					"		else gen.name  " + 
					"	end as \"genericName\",  " + 
					"	su.unit as \"strengthDesc\",  " + 
					"	su.id as \"doseUnitId\",  " + 
					"	preFreDtl.frequency_id as \"frequencyId\",  " + 
					"	freq.description as \"frequencyDesc\",  " + 
					"	preFreDtl.route_id as \"roaId\",  " + 
					"	presDtl.is_non_formulary as \"isNonFormulary\",  " + 
					"	presDtl.status_id as \"statusId\",  " + 
					"	presDtl.statflag as \"statflag\",  " + 
					"	roa.description as \"roaDesc\",  " + 
					"	presDtl.order_qty as \"orderQty\",  " + 
					"/*	to_char(preFreDtl.start_date, 'dd/mm/yyyy') as \"startDate\",  " + 
					"	to_char(preFreDtl.stop_date, 'dd/mm/yyyy') as \"endDate\",  " + 
					"	to_char(presDtl.added_date, 'dd/mm/yyyy') as \"addedDate\",  " + 
					"	to_char(presDtl.added_date, 'HH24:MI') as \"addedTime\",  
					"	presDtl.instruction_id as \"instructionId\" ,  " + 
					"	instr.instruction_desc as \"instructionDesc\",  " + 
					"	--presDtl.id as \"prescriptionDetailsid\",  " + 
					"	--preFreDtl.id as \"prescriptionFrequencyId\" ,  " + 
					"	pres. is_otims as \"isOtims\",  " + 
					"	/*(case  " + 
					"		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name)  " + 
					"		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
					"		else 'Admin'  " + 
					"	end) as \"userName\",  
					"	presDtl.is_discharge_medication as \"isDischargeMedication\",  " + 
//					"	--presDose.id as \"prescribeDoseUnitId\",  " + 
//					"	--presDose.unit as \"prescribeDoseUnit\",  " + 
					"	presDtl.prescribe_dose as \"prescribeDose\",  " + 
					"	fdm.dosage_form as \"dosageForm\",  " + 
					"	presDtl.factor_id as \"factorId\",  " + 
					"	presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" ,  " + 
					"	count(*)  " + 
					" from  " + 
					"	pharmacy.t_prescription_detail presDtl  " + 
					" left join pharmacy.t_prescription pres on  " + 
					"	presDtl.prescription_id = pres.id  " + 
					" inner join pharmacy.t_prescription_frequency_detail preFreDtl on  " + 
					"	preFreDtl.prescription_detail_id = presDtl.id  " + 
					" left join public.m_strength_unit su on  " + 
					"	preFreDtl.dose_unit_id = su.id  " + 
					" left join public.m_strength_unit presDose on  " + 
					"	presDtl.prescribe_dose_unit_id = presDose.id  " + 
					" left join public.m_item itm on  " + 
					"	itm.id = presDtl.item_id  " + 
					" left join public.m_route_of_administration roa on  " + 
					"	roa.id = preFreDtl.route_id  " + 
					" left join public.m_generic gen on  " + 
					"	itm.generic_id = gen.id  " + 
					" inner join public.m_frequency freq on  " + 
					"	freq.id = preFreDtl.frequency_id  " + 
					" left join public.m_instruction instr on  " + 
					"	instr.instruction_id = presDtl.instruction_id  " + 
					" left join public.m_fomulation_dosage_mapper fdm on  " + 
					"	fdm.formulation_dosage_mapp_id = presDtl.formulation_dose_mapp_id  " + 
					"/*left join public.m_user_master um on  " + 
					"	pres.added_by = um.user_id  " + 
					"left join public.m_employee_master empmst on  " + 
					"	um.user_id = empmst.user_id  " + 
					"left join doctor.m_doctor_master docmst on  " + 
					"	um.user_id = docmst.user_id  
					" where  " + 
					"	pres.doctor_id = :doctorId " + 
					"	and presDtl.is_medication_stop = 'N'  " + 
					"	and presDtl.status_id != 9  " + 
					" group by  " + 
					"	presDtl.item_id,  " + 
					"	itm.name,  " + 
					"	gen.name,  " + 
					"	presDtl.item_name,  " + 
					"	presDtl.generic_name,  " + 
//					"	--pres.id,  " + 
					"	presDtl.is_medication_stop,  " + 
					"	presDtl.justification_stop,  " + 
					"	presDtl.instruction_desc,  " + 
					"	presDtl.instruction_desc,  " + 
					"	preFreDtl.dose ,  " + 
					"	su.unit,  " + 
					"	su.id,  " + 
					"	preFreDtl.frequency_id,  " + 
					"	freq.description,  " + 
					"	presDtl.order_qty,  " + 
					"	presDtl.instruction_id,  " + 
					"	instr.instruction_desc,  " + 
//					"	--presDtl.id,  " + 
//					"	--preFreDtl.id,  " + 
					"	pres. is_otims,  " + 
					"	presDtl.is_discharge_medication,  " + 
					"	presDtl.prescribe_dose,  " + 
					"	presDtl.formulation_dose_mapp_id,  " + 
					"	pres.status_id,  " + 
//					"	--pres.id,  " + 
					"	preFreDtl.route_id,  " + 
					"	presDtl.is_non_formulary,  " + 
					"	presDtl.status_id,  " + 
					"	presDtl.statflag,  " + 
					"	roa.description,  " + 
					"	/*preFreDtl.start_date,  " + 
					"	preFreDtl.stop_date,  " + 
					"	presDtl.added_date,  " + 
					"	presDtl.added_date,  " + 
					"	empmst.employee_id,  " + 
					"	empmst.first_name,  " + 
					"	empmst.last_name,  " + 
					"	docmst.doctor_id,  " + 
					"	presDose.id, " + 
					"	fdm.dosage_form,  " + 
					"	presDtl.factor_id  " + 
					" order by  " + 
					"	count(*) desc  " + 
					" limit 20";*/
					
					" select " 
							+ "	distinct (presDtl.item_id) as \"drugId\", " 
							+ "	itm.name as \"itemName\", "
							+ "	gen.name as \"genericName\", " 
							+ "	itm.generic_id as \"genericId\", "
							+ "	concat(itm.name, ' | ', 'Qty : ', "
							+ " coalesce((select sum(S.available_qty) from inventory.t_batch b "
							+ " inner join inventory.t_store_stock S on B.id = S.batch_id "
							+ " where b.item_id = presDtl.item_id and s.store_id in "
							+ " ( select store_id from m_store_item_mapper msim "
							+ " where item_id = presDtl.item_id and unit_id = :unitId and store_id in "
							+ " ( select ms.store_id from m_store ms where ms.unit_id = :unitId and ms.is_pharmacy = true)) "
							+ " and b.unit_id = :unitId and s.unit_id = :unitId "
							+ " and to_char(b.expiry_date, 'yyyy-mm-dd') >=:expDate "
							+ " and b.is_quarantine = false), 0) ) as \"medicationName\", "
							+ "	coalesce((select cast((b.rate) as decimal(10, 2)) "
							+ " from inventory.t_batch b inner join inventory.t_store_stock S on B.id = S.batch_id "
							+ " where b.item_id = presDtl.item_id and s.store_id in "
							+ " ( select store_id from m_store_item_mapper msim "
							+ " where item_id = presDtl.item_id and unit_id =:unitId and store_id in "
							+ " ( select ms.store_id from m_store ms where ms.unit_id =:unitId and ms.is_pharmacy = true)) "
							+ " and b.unit_id = :unitId and s.unit_id = :unitId "
							+ "  and to_char(b.expiry_date, 'yyyy-mm-dd') >=:expDate "
							+ " and b.is_quarantine = false order by b.id desc limit 1), 0) as \"rate\", "
							+ "	itm.formulation_type_id as \"formulationTypeId\", " 
							//+ "	itm.strength as \"strength\", "
							+ "	itm.strength_unit_id as \"strengthUnitId\", " 
							+ "	itm.is_narcotic as \"isNarcotic\", "
							+ "	su.unit as \"unitDesc\", " 
							+ "	count(*) as \"countMed\" " 
							+ " from "
							+ "	pharmacy.t_prescription_detail presDtl " + " left join pharmacy.t_prescription pres on "
							+ "	presDtl.prescription_id = pres.id "
							+ " inner join pharmacy.t_prescription_frequency_detail preFreDtl on "
							+ "	preFreDtl.prescription_detail_id = presDtl.id "
							+ " left join public.m_strength_unit su on " + "	preFreDtl.dose_unit_id = su.id "
							+ " left join public.m_strength_unit presDose on "
							+ "	presDtl.prescribe_dose_unit_id = presDose.id " + " left join public.m_item itm on "
							+ "	itm.id = presDtl.item_id " + " left join public.m_generic gen on "
							+ "	itm.generic_id = gen.id " + " where " + "	pres.doctor_id = :doctorId   "
							+ "	AND pres.organization_id = :organizationId   " + "	AND pres.unit_id = :unitId   " + " "
							+ "	group by presDtl.item_id, " + "	itm.generic_id, " + "	itm.name, " + "	gen.name, "
							+ "	itm.formulation_type_id, " + "	itm.strength, " + "	itm.strength_unit_id, "
							+ "	itm.is_narcotic, " + "	su.unit " + " order by " + "	count(*) desc " + " limit 10";

			List<PrescriptionDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", medicationDTOs.getOrganizationId())
					.setParameter("unitId", medicationDTOs.getUnitId())
					.setParameter("doctorId", medicationDTOs.getDoctorId())
					.setParameter("expDate", toDayDate)
					.setResultTransformer(Transformers.aliasToBean(PrescriptionDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getFrequentlyUsedMedicationList failed", he);
		} catch (Exception e) {
		logger.error("getFrequentlyUsedMedicationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOPDMedicationListByIdForNonFormulary(Integer prescriptionId, Integer prescriptionDetailsid,
			Integer patientId, Integer unitId, Integer organizationId) throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String toDayDate = simpleDateFormat.format(new Date());
		    
				String query = "SELECT "
						+ " distinct(pres.id) as \"prescriptionId\", "
						+ " pres.prescription_no as \"prescriptionNo\", "
						+ " presDtl.item_id as \"itemId\", "
						+ " presDtl.strength as \"strength\","
						+ " presDtl.item_name as \"searchItem\", "
						+ " presDtl.generic_name as \"genericName\", "
						+ " preFreDtl.dose as \"dose\", "
						+ " su.unit as \"strengthDesc\", " 
						+ " su.id as \"doseUnitId\", "
						+ " presDtl.statflag as \"isStat\" , " 
						+ " presDtl.instruction_desc \"instructionText\", "
						+ " preFreDtl.frequency_id as \"frequencyId\","
						+ " fr.no_of_administration_time as \"noOfAdministrationTime\", "
						+ " freq.description as \"searchItemFre\", "
						+ " fnr.roa_id as \"roa_id\", "
						+ " roa.description as \"roa_details\", "
						+ " itm.formulation_type_id as \"formulationTypeId\", "
						+ " itm.generic_id as \"genericId\", "
						+ " pres.priority_id as \"priorityId\" , " 
						+ " presDtl.order_qty as \"quantity\", "
						+ "presDtl.is_discharge_medication as \"isDischargeMedication\", "
						+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"medicationStartDate\"," 
						+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"medicationEndDate\", "
						+ " presDtl.instruction_id as \"instructionId\" ,"
						+ " instr.instruction_desc as \"instructionDesc\", "
						+ " pres.encounter_id as \"encounterId\","
						+ " pres.admission_id as \"admissionId\","
						+ " presDtl.id as \"prescriptionDetailsid\","
						+ " preFreDtl.id as \"prescriptionFrequencyId\","
						+ " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
	                    + " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
						+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" "
						+ " FROM pharmacy.t_prescription pres "
						+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
						+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
						+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
						+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
						+ " LEFT JOIN public.m_item itm on itm.id=presDtl.item_id "
						+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					    + " LEFT JOIN public.m_formulation_and_roa fnr ON roa.id=fnr.roa_id "
						+ " LEFT JOIN public.m_generic gen on itm.generic_id = gen.id "
						+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
						+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id"
						+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
						+ " INNER JOIN public.m_frequency fr ON preFreDtl.frequency_id = fr.id "
						+ " WHERE pres.id=:prescriptionId "
						+ " AND presDtl.id=:prescriptionDetailsid "
						+ " AND pres.patient_id=:patientId "
						+ " AND presDtl.unit_id=:unitId"
						+ " AND presDtl.organization_id=:organizationId";
				
				
				List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("prescriptionId", prescriptionId)
						.setParameter("prescriptionDetailsid", prescriptionDetailsid)
						.setParameter("patientId", patientId)
						.setParameter("unitId", unitId)
						.setParameter("organizationId", organizationId)
						.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
			logger.error("getOPDMedicationListByIdForNonFormulary failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}

	@Override
	public Response saveCIMMS(PrescriptionDto prescriptionDto) throws ApplicationException {
		 FastTrackDSM ft = new FastTrackDSM();
		try {
			String cimmsData = null;
			String query = " select apps.cimms_data as \"cimmsData\"  from public.t_unit_application_billing_configuration apps where "  
						+ " apps.organization_id =:organizationId"  
					    + " and apps.unit_id =:unitId";
			
			List<ApplicationConfigurationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",prescriptionDto.getUnitId())
					.setParameter("organizationId",prescriptionDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ApplicationConfigurationDto.class)).list();
			String response = null;
			String outputString = null;
			String  finalResponse = null;
			String  gg = null;
			String  ss = null;
			
			if(list != null && list.size() == 1) {
				cimmsData= list.getFirst().getCimmsData();
			}
			
	        if(cimmsData == null) {

				gg = "Please Configure CIMMS Database On C Drive And Set In  Application Configuration table ";
			  return new Response(SUCCESS, SUCCESS_CODE, null, null, gg);
			}
			Response<JSONObject, Integer> responsz = null;
			JSONObject jsonNew = new JSONObject(cimmsData); 
			
			
			String REQ_XML = prescriptionDto.getReqXml();
//		       String ServerDirPath =  "/resources/assets/FasttrackData_ind.mr2";
//			   String ServerDirPath =  "/resources/assets/FastTrackData_IN_MRRCH_Q32022.mr2";
//			   String ServerDirPath =  "C:/FastTrackData_IN_MRRCH_Q32022.mr2";
//       	       String dirPath = servletContext.getRealPath(ServerDirPath);
       	       
    	       String dirPath = jsonNew.get("path").toString();
    	       String passward =jsonNew.get("passward").toString();
    	       
//       	       if (!ft.open(dirPath, "GDDBDEMO"))
//			    if (!ft.open(dirPath, "Z3ggECJ5W7"))
//       	       if (!ft.open(dirPath, "Z3ggECJ5W7"))
//		            System.err.println("Unable to open data file");
			
			//String ServerDirPath =  "/resources/assets/FasttrackData_ind.mr2";
//			   String ServerDirPath =  "/resources/assets/FastTrackData_IN_MRRCH_Q32022.mr2";
    	      // String dirPath = servletContext.getRealPath(ServerDirPath);
    	       
			    if (!ft.open(dirPath, passward))
//			    if (!ft.open(dirPath, "Z3ggECJ5W7"))
			    	{
			    	gg = "Unable to open data file";
		            logger.error("Unable to open data file");
			    	} else {
		        	
		        	 String str = new String(ft.requestXml(REQ_XML));
		        	 response = ft.requestXml(REQ_XML);
		        	 finalResponse = convertXMLToHTML(str);
		        	 gg = finalResponse.replace("\r\n\t", " ");
		        	 ss = gg.replace(" \f \r\"\' ", " ");
		           
		        }
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, gg);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("saveCIMMS failed", e);
		}
		finally {
			if (ft != null) {
				try {
					//database connection close
					ft.release();
				} catch (HibernateException e) {
					
					throw e;
				}
			}
		}
		
		
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	
	}

	private String convertXMLToHTML(String str) {
		String outputString  = null;
	    try
	    {
	    
	    	 String ServerDirPath =  "/resources/assets/MIMSStylesheet-CDSDefault_byRanking-EN_4Feb2022.xsl";
     	       String dirPath = servletContext.getRealPath(ServerDirPath);
	    	Source xslTransformationFile = new StreamSource(dirPath);
	    	StringWriter writer = new StringWriter();
	      Result xmlOutputFile = new StreamResult(writer);   
	      TransformerFactory tf = TransformerFactory.newInstance();  
	      Transformer transformer2 = tf.newTransformer(xslTransformationFile);
	      StreamSource src = new StreamSource(new StringReader(str));
	      transformer2.transform(src, xmlOutputFile);
	       outputString = writer.toString();
	      
	      
	    }
	    catch (TransformerException trne)
	    {
	    logger.error("convertXMLToHTML failed", trne);
	    }
	    catch (Exception e)
	    {
	    logger.error("convertXMLToHTML failed", e);
	    }
		return outputString;
	  }

	@Override
	public Response getPresDtlDataByPresId(Integer prescriptionId, Integer organizationId, Integer unitId) {
		try {
			String query = "SELECT "
//					+ "pres.id as \"prescriptionId\", "
					+ " presDtl.item_id as \"itemId\", "
					+ " itm.name as \"itemName\", "
					+ "su.id as \"doseUnitId\", "
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " gen.name as \"genericName\", "
					+ " presDtl.id as \"prescriptionDetailsidNew\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\" ,"
					+ " 	mis.total_volume as \"infusionTotalVolume\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " inner join opd.m_infusion_solution mis on " + 
					"	presDtl.infusion_solution_id = mis.infusion_solution_id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " WHERE pres.id=:prescriptionId "
					+ " AND presDtl.unit_id=:unitId"
					+ " AND presDtl.organization_id=:organizationId";
					
					List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("prescriptionId", prescriptionId)
							.setParameter("unitId", unitId)
							.setParameter("organizationId", organizationId)
							.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

				} catch (HibernateException he) {
					throw new HibernateException(he.getMessage());
				} catch (Exception e) {
				logger.error("getPresDtlDataByPresId failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
			}

	@Override
	public Response searchDrugByGenericNameOrItemName(Integer unitId, Integer orgId, String searchDrug)
			throws ApplicationException {
		try {
			
			
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//			String toDayDate = simpleDateFormat.format(new Date());
			String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId));
			if(!searchDrug.equals("")) {
				String query = " select " 
			+ "    item.id as \"drugId\", "
						+ "    concat(item.name, ' | '," + 
						"    generic.name , ' | ', 'Qty : ',  coalesce(stock.totalQty,0) ) as \"medicationName\", "
						+ " coalesce((r.rate ), 0) as \"rate\", "
						+ "    item.is_dose_calculation_applicable as \"isDoseCalculationApplicable\",  "
						+ "    item.dose_calculation_applicable_data as \"doseCalculationApplicableData\",  "
						+ "    item.generic_id as \"genericId\",  " 
						+ "    item.cims_code as \"cimsCode\",  " 
						+ "    item.cims_type as \"cimsType\",  " 
						+ "    generic.name as \"genericName\",  "
						+ "    item.formulation_type_id as \"formulationTypeId\",  "
						+ "    item.strength as \"strength\",  "
						+ "    item.strength_unit_id as \"strengthUnitId\",  "
						+ "    item.is_narcotic as \"isNarcotic\",  " 
						+ "    strnUn.unit as \"unitDesc\"  "
						+ " from  " + "    public.m_item item  " 
						+ " inner join public.m_generic generic on  "
						+ "    generic.id = item.generic_id  " 
						+ " left join public.m_strength_unit strnUn on  "
						+ "    strnUn.id = item.strength_unit_id  " 
						+ "     " + " left join (  "
						+ "    select  " 
						+ "        item.id as itemId,  "
						+ "        sum(stock.available_qty) as totalQty  " 
						+ "    from  "
						+ "        public.m_item item  " 
						+ "    inner join inventory.t_batch batch on  "
						+ "        batch.item_id = item.id  " 
						+ "        and (batch.is_deleted = false  "
						+ "            and batch.is_active = true) and batch.is_quarantine =false  "
						+ "            and batch.unit_id =:unitId and (batch.expiry_date >='" 
						+ toDayDate + "'  or batch.expiry_date is null)"
						+ "        inner join inventory.t_store_stock stock on  "
						+ "            stock.batch_id = batch.id  "
						+ "        inner join public.m_store ms  on stock.store_id =ms.store_id  "
						+ "            and stock.unit_id =:unitId and ms.is_pharmacy =true  "
						+ "            and (stock.is_deleted = false  "
						+ "                and stock.is_active = true  "
						+ "                and stock.available_qty > 0)  " 
						+ "        group by  "
						+ "            item.id) stock on  " 
						+ "    stock.itemId = item.id     " 
						
						+ " left join (	select max(cast((b.rate) as decimal(10, 2))) rate , b.item_id from inventory.t_batch b "  
						+ "	inner join inventory.t_store_stock S on B.id = S.batch_id inner join m_store ms on s.store_id = ms.store_id "  
						+ "	inner join m_store_item_mapper msim on msim.store_id=ms.store_id and msim.item_id=b.item_id " + 
						"	where to_char(b.expiry_date, 'yyyy-dd-mm') >= :expDate and b.is_quarantine = false and b.unit_id =:unitId " + 
						"	and s.unit_id =:unitId	and ms.is_pharmacy = true and ms.unit_id =:unitId	group by  b.item_id ) r on r.item_id=item.id "
						
						+ " where "
						+ " item.organization_id =:orgId " 
						+ " and item.is_consumable = true "
						+ " and item.is_active = true " 
						+ " and item.is_deleted = false "
						+ "     AND ((lower(item.name) LIKE lower('%"+ searchDrug+ "%') or item.name LIKE '%"+ searchDrug+ "%')"
						+ " or (lower(generic.name) LIKE lower('%"+ searchDrug+ "%') or generic.name LIKE '%"+ searchDrug+ "%'))"  
//						+ " and (lower(item.name) like lower('%"+ searchDrug+ "%') "
//						+ "or lower(item.name) like lower('%"+ searchDrug +"%')) "
								+ " order by coalesce(stock.totalQty,0) Desc";
				
				List<MedicationListDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId",unitId)
						 .setParameter("expDate", toDayDate)
						.setParameter("orgId",orgId)
						.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
						}else {
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
						}
						} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
						} catch (Exception e) {
						logger.error("searchDrugByGenericNameOrItemName failed", e);
						}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePrescriptionDetailList(CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try
		{
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update pharmacy.t_prescription_detail  set is_active = false"
			+ " where cov_template_id="+coversheetTemplateMasterDto.getCovTemplateId())
			.executeUpdate();
//			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updatePrescriptionDetailList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	
	}

	@Override
	public Response getItemListWithSubstituteForNewEMR(MedicationListDto dto) throws ApplicationException {

		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(dto.getUnitId()));
		if (!dto.getSearchItem().equals("")) {
			String query = " select " + "    item.id as \"drugId\", "
					+ "    concat(item.name, ' | ', 'Qty : ',  coalesce(stock.totalQty,0) ) as \"medicationName\", "
					+ " coalesce((r.rate ), 0) as \"rate\", "
					+ "    item.generic_id as \"genericId\",  "
					+ "    generic.name as \"genericName\"  "
					+ " from  "
					+ "    public.m_item item  " 
					+ " inner join public.m_generic generic on  "
					+ "    generic.id = item.generic_id  "
					+ " left join public.m_strength_unit strnUn on  "
					+ "    strnUn.id = item.strength_unit_id  "
					+ "     " + " left join (  " + "    select  "
					+ "        item.id as itemId,  " + "        sum(stock.available_qty) as totalQty  " + "    from  "
					+ "        public.m_item item  " + "    inner join inventory.t_batch batch on  "
					+ "        batch.item_id = item.id  " + "        and (batch.is_deleted = false  "
					+ "            and batch.is_active = true) and batch.is_quarantine =false  "
					+ "            and batch.unit_id =:unitId and (batch.expiry_date >='" + toDayDate
					+ "'  or batch.expiry_date is null)" + "        inner join inventory.t_store_stock stock on  "
					+ "            stock.batch_id = batch.id  "
					+ "        inner join public.m_store ms  on stock.store_id =ms.store_id  "
					+ "            and stock.unit_id =:unitId and ms.is_pharmacy =true  "
					+ "            and (stock.is_deleted = false  " + "                and stock.is_active = true  "
					+ "                and stock.available_qty > 0)  " + "        group by  "
					+ "            item.id) stock on  " + "    stock.itemId = item.id     "

					+ " left join (	select max(cast((b.rate) as decimal(10, 2))) rate , b.item_id from inventory.t_batch b "
					+ "	inner join inventory.t_store_stock S on B.id = S.batch_id inner join m_store ms on s.store_id = ms.store_id "
					+ "	inner join m_store_item_mapper msim on msim.store_id=ms.store_id and msim.item_id=b.item_id "
					+ "	where to_char(b.expiry_date, 'yyyy-dd-mm') >= :expDate and b.is_quarantine = false and b.unit_id =:unitId "
					+ "	and s.unit_id =:unitId	and ms.is_pharmacy = true and ms.unit_id =:unitId	group by  b.item_id ) r on r.item_id=item.id "

					+ " where " + " item.organization_id =:orgId " + " and item.is_consumable = true "
					+ " and item.is_active = true " + " and item.is_deleted = false "
					+ " and lower(item.name) like lower('%" + dto.getSearchItem()
					+ "%')  order by coalesce(stock.totalQty,0) Desc";

			List<MedicationListDto> roaMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", dto.getUnitId()).setParameter("expDate", toDayDate).setParameter("orgId", dto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();

			for (MedicationListDto medicationListDto : roaMasterDtos) {
				List<MedicationListDto> substituteList=sessionFactory.getCurrentSession()
						.createSQLQuery(" select " + "    item.id as \"drugId\", "
						+ "    concat(item.name, ' | ', 'Qty : ',  coalesce(stock.totalQty,0) ) as \"medicationName\", "
						+ " coalesce((r.rate ), 0) as \"rate\", "
						+ "    item.is_dose_calculation_applicable as \"isDoseCalculationApplicable\",  "
						+ "    item.dose_calculation_applicable_data as \"doseCalculationApplicableData\",  "
						+ "    item.generic_id as \"genericId\",  "
						+ "    generic.name as \"genericName\"  "
						+ " from  "
						+ "    public.m_item item  " + " inner join public.m_generic generic on  "
						+ "    generic.id = item.generic_id  " + " left join public.m_strength_unit strnUn on  "
						+ "    strnUn.id = item.strength_unit_id  " + "     " + " left join (  " + "    select  "
						+ "        item.id as itemId,  " + "        sum(stock.available_qty) as totalQty  " + "    from  "
						+ "        public.m_item item  " + "    inner join inventory.t_batch batch on  "
						+ "        batch.item_id = item.id  " + "        and (batch.is_deleted = false  "
						+ "            and batch.is_active = true) and batch.is_quarantine =false  "
						+ "            and batch.unit_id =:unitId and (batch.expiry_date >='" + toDayDate
						+ "'  or batch.expiry_date is null)" + "        inner join inventory.t_store_stock stock on  "
						+ "            stock.batch_id = batch.id  "
						+ "        inner join public.m_store ms  on stock.store_id =ms.store_id  "
						+ "            and stock.unit_id =:unitId and ms.is_pharmacy =true  "
						+ "            and (stock.is_deleted = false  " + "                and stock.is_active = true  "
						+ "                and stock.available_qty > 0)  " + "        group by  "
						+ "            item.id) stock on  " + "    stock.itemId = item.id     "

						+ " left join (	select max(cast((b.rate) as decimal(10, 2))) rate , b.item_id from inventory.t_batch b "
						+ "	inner join inventory.t_store_stock S on B.id = S.batch_id inner join m_store ms on s.store_id = ms.store_id "
						+ "	inner join m_store_item_mapper msim on msim.store_id=ms.store_id and msim.item_id=b.item_id "
						+ "	where to_char(b.expiry_date, 'yyyy-dd-mm') >= :expDate and b.is_quarantine = false and b.unit_id =:unitId "
						+ "	and s.unit_id =:unitId	and ms.is_pharmacy = true and ms.unit_id =:unitId	group by  b.item_id ) r on r.item_id=item.id "

						+ " where " + " item.organization_id =:orgId " + " and item.is_consumable = true "
						+ " and item.is_active = true " + " and item.is_deleted = false "
						+ " and item.generic_id ="+medicationListDto.getGenericId()
						+ " and item.id !="+ medicationListDto.getDrugId())
				
				.setParameter("unitId", dto.getUnitId()).setParameter("expDate", toDayDate).setParameter("orgId", dto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
				medicationListDto.setListSubstitute(substituteList);
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, roaMasterDtos, null);
		} else {
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}
	}

	@Override
	public Response getSubstituteListForNewEMR(MedicationListDto dto) throws ApplicationException {
		try {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(dto.getUnitId()));

				List<MedicationListDto> substituteList=sessionFactory.getCurrentSession()
						.createSQLQuery(" select " + "    item.id as \"drugId\", "
						+ "    concat(item.name, ' | ', 'Qty : ',  coalesce(stock.totalQty,0) ) as \"medicationName\", "
						+ " coalesce((r.rate ), 0) as \"rate\", "
						+ "    item.is_dose_calculation_applicable as \"isDoseCalculationApplicable\",  "
						+ "    item.dose_calculation_applicable_data as \"doseCalculationApplicableData\",  "
						+ "    item.generic_id as \"genericId\",  "
						+ "    generic.name as \"genericName\"  "
						+ " from  "
						+ "    public.m_item item  " + " inner join public.m_generic generic on  "
						+ "    generic.id = item.generic_id  " + " left join public.m_strength_unit strnUn on  "
						+ "    strnUn.id = item.strength_unit_id  " + "     " + " left join (  " + "    select  "
						+ "        item.id as itemId,  " + "        sum(stock.available_qty) as totalQty  " + "    from  "
						+ "        public.m_item item  " + "    inner join inventory.t_batch batch on  "
						+ "        batch.item_id = item.id  " + "        and (batch.is_deleted = false  "
						+ "            and batch.is_active = true) and batch.is_quarantine =false  "
						+ "            and batch.unit_id =:unitId and (batch.expiry_date >='" + toDayDate
						+ "'  or batch.expiry_date is null)" + "        inner join inventory.t_store_stock stock on  "
						+ "            stock.batch_id = batch.id  "
						+ "        inner join public.m_store ms  on stock.store_id =ms.store_id  "
						+ "            and stock.unit_id =:unitId and ms.is_pharmacy =true  "
						+ "            and (stock.is_deleted = false  " + "                and stock.is_active = true  "
						+ "                and stock.available_qty > 0)  " + "        group by  "
						+ "            item.id) stock on  " + "    stock.itemId = item.id     "

						+ " left join (	select max(cast((b.rate) as decimal(10, 2))) rate , b.item_id from inventory.t_batch b "
						+ "	inner join inventory.t_store_stock S on B.id = S.batch_id inner join m_store ms on s.store_id = ms.store_id "
						+ "	inner join m_store_item_mapper msim on msim.store_id=ms.store_id and msim.item_id=b.item_id "
						+ "	where to_char(b.expiry_date, 'yyyy-dd-mm') >= :expDate and b.is_quarantine = false and b.unit_id =:unitId "
						+ "	and s.unit_id =:unitId	and ms.is_pharmacy = true and ms.unit_id =:unitId	group by  b.item_id ) r on r.item_id=item.id "

						+ " where " + " item.organization_id =:orgId " + " and item.is_consumable = true "
						+ " and item.is_active = true " + " and item.is_deleted = false "
						+ " and item.generic_id ="+dto.getGenericId()
						+ " and item.id !="+ dto.getDrugId())
				
				.setParameter("unitId", dto.getUnitId())
				.setParameter("expDate", toDayDate)
				.setParameter("orgId", dto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(MedicationListDto.class)).list();
				
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, substituteList, null);
	}catch(HibernateException he) { 
		throw new HibernateException(he.getMessage());
	}catch(Exception e) {
	logger.error("getSubstituteListForNewEMR failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Integer getFormularyMedicationCount(Integer presId) {
			String sql = "  select cast(count(*) as int) " + 
					"from " + 
					" pharmacy.t_prescription_detail presDtl" + 
					" where " + 
					"	presDtl.prescription_id ="+presId+" "
					+ " and presDtl.is_non_formulary ='N'"
					+ " and presDtl.status_id not in(4,9) " 
					
					;
			
			Query  query= sessionFactory.getCurrentSession().createSQLQuery(sql);
	        Integer count= (Integer) query.uniqueResult();
	        return count;
	}

	@Override
	public void updateIsFormularyFlagForPrescription(Integer encounterId, Integer prescriptionId) {
		String sql = "  update pharmacy.t_prescription set is_non_formulary='N'  " + 
				" where " + 
				" id ="+prescriptionId+" "
				+ " and encounter_id ="+encounterId;
		
		Query  query= sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.executeUpdate();
		
	}

	@Override
	public void updateIsFormularyFlagForPrescriptionY(Integer encounterId, Integer prescriptionId) {
		String sql = "  update pharmacy.t_prescription set is_non_formulary='Y'  " + 
				" where " + 
				" id ="+prescriptionId+" "
				+ " and encounter_id ="+encounterId;
		
		Query  query= sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.executeUpdate();
		
	}

	@Override
	public Response getNonFormularyItemForWorklistPharmacy(Integer prescriptionId,  Integer unitId,
			Integer organizationId) throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String toDayDate = simpleDateFormat.format(new Date());
		    
				String query = "SELECT "
						+ " distinct(pres.id) as \"prescriptionId\", "
						+ " pres.prescription_no as \"prescriptionNo\", "
						+ " presDtl.item_id as \"itemId\", "
						+ " presDtl.strength as \"strength\","
						+ " presDtl.item_name as \"searchItem\", "
						+ " presDtl.generic_name as \"genericName\", "
						+ " preFreDtl.dose as \"dose\", "
						+ " su.unit as \"strengthDesc\", " 
						+ " su.id as \"doseUnitId\", "
						+ " presDtl.statflag as \"isStat\" , " 
						+ " presDtl.instruction_desc \"instructionText\", "
						+ " presDtl.duration \"durationCount\", "
						+ " presDtl.duration_unit \"durationUnit\", "
						+ " preFreDtl.frequency_id as \"frequencyId\","
						+ " fr.no_of_administration_time as \"noOfAdministrationTime\", "
						+ " freq.description as \"searchItemFre\", "
						+ " fnr.roa_id as \"roa_id\", "
						+ " roa.description as \"roa_details\", "
						+ " itm.formulation_type_id as \"formulationTypeId\", "
						+ " itm.generic_id as \"genericId\", "
						+ " pres.priority_id as \"priorityId\" , " 
						+ " presDtl.order_qty as \"quantity\", "
						+ "presDtl.is_discharge_medication as \"isDischargeMedication\", "
						+ " to_char(preFreDtl.start_date,'YYYY-MM-DD') as \"startDate\"," 
						+ " to_char(preFreDtl.stop_date,'YYYY-MM-DD') as \"stopDate\", "
						+ " presDtl.instruction_id as \"instructionId\" ,"
						+ " instr.instruction_desc as \"instructionDesc\", "
						+ " pres.encounter_id as \"encounterId\","
						+ " pres.admission_id as \"admissionId\","
						+ " presDtl.id as \"prescriptionDetailsid\","
						+ " preFreDtl.id as \"prescriptionFrequencyId\","
						+ " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\","
	                    + " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",presDtl.factor_id as \"factorId\","
						+ " presDtl.formulation_dose_mapp_id as \"formulationDosageMappId\" "
						+ " FROM pharmacy.t_prescription pres "
						+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
						+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
						+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
						+ " LEFT JOIN public.m_strength_unit presDose ON presDtl.prescribe_dose_unit_id = presDose.id " 
						+ " LEFT JOIN public.m_item itm on itm.id=presDtl.item_id "
						+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					    + " LEFT JOIN public.m_formulation_and_roa fnr ON roa.id=fnr.roa_id "
						+ " LEFT JOIN public.m_generic gen on itm.generic_id = gen.id "
						+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
						+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id"
						+ " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id "
						+ " INNER JOIN public.m_frequency fr ON preFreDtl.frequency_id = fr.id "
						+ " WHERE pres.id=:prescriptionId "
						+ " AND presDtl.unit_id=:unitId"
						+ " AND presDtl.organization_id=:organizationId"
						+ " AND presDtl.status_id != 9 "
			            + " AND presDtl.is_non_formulary='Y'";
				
				
				List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("prescriptionId", prescriptionId)
						.setParameter("unitId", unitId)
						.setParameter("organizationId", organizationId)
						.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

			} catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				throw e;
			}
		}

	@Override
	public Response savePrescriptionDetaisForPharmacy(PrescriptionDetailsDto prescriptionDetailsDto)
			throws ApplicationException {
		try {
			PrescriptionDetailsMaster prescriptionDetailsMaster = mapper.map(prescriptionDetailsDto, PrescriptionDetailsMaster.class,
					"PrescriptionDetailsMasterDto_to_PrescriptionDetailsMaster");
			if(prescriptionDetailsDto.getIsDischarge()!=null) {
				prescriptionDetailsMaster.setDisPrescribedQty(prescriptionDetailsDto.getOrderQty());
			}
			if(prescriptionDetailsMaster!=null) {
				String sql = "select ms.store_id as \"storeId\" from m_store ms where ms.unit_id = :unitId and ms.is_active = true order by 1 desc limit 1";
				Query query = getSession().createSQLQuery(sql);
				query.setParameter("unitId", prescriptionDetailsDto.getUnitId());
				Integer convertedBatchHql = (Integer) query.uniqueResult();
				prescriptionDetailsMaster.setStoreId(convertedBatchHql);
				}
			prescriptionDetailsMaster.setIsMedicationStop(prescriptionDetailsMaster.getIsMedicationStop()=='\u0000'?'N':prescriptionDetailsMaster.getIsMedicationStop());
			sessionFactory.getCurrentSession().save(prescriptionDetailsMaster);
			prescriptionDetailsDto.setPrescriptionDetailsid(prescriptionDetailsMaster.getPrescriptionDetailsId());
		
		
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDetailsDto);
	} catch (HibernateException he) {
	logger.error("savePrescriptionDetaisForPharmacy failed", he);
	} catch (Exception e) {
	logger.error("savePrescriptionDetaisForPharmacy failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response getIssueDetailsId(PrescriptionDetailsDto dto) throws ApplicationException {
		try {
			
			String sql = " select id as \"issueDetailsId\"  from pharmacy.t_issue_detail where prescription_detail_id =:id";

			PrescriptionDto dtozxcccc = (PrescriptionDto)sessionFactory.getCurrentSession().createSQLQuery(sql)
					             .setParameter("id", dto.getPrescriptionDetailsid())
					             .setResultTransformer(Transformers.aliasToBean(PrescriptionDto.class)).uniqueResult();
			
			
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dtozxcccc);
		} catch (HibernateException he) {
		logger.error("getIssueDetailsId failed", he);
		} catch (Exception e) {
		logger.error("getIssueDetailsId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

    public List<MedicationDTO> getMedicationHistoryByEncounterIdNonFormulary(MedicationDTO medicationDTO) throws ApplicationException {
        List<MedicationDTO> roaMasterDtos = new ArrayList<>();
        try {

            String query = "SELECT " +
                    " presDtl.item_name as \"itemName\", " +
                    " preFreDtl.dose as \"doseDesc\", " +
                    " su.unit as \"strengthDesc\", " +
                    " preFreDtl.frequency_id as \"frequencyId\", " +
                    " preFreDtl.dose_unit_id as \"doseUnitId\",  " +
                    " itm.is_dose_calculation_applicable as \"isDoseCalculationApplicable\", " +
                    " itm.dose_calculation_applicable_data as \"doseCalculationApplicableData\", " +
                    " freq.no_of_administration_time as \"noOfAdministrationTime\",  " +
                    " freq.description as \"frequencyDesc\", " +
                    " preFreDtl.route_id as \"roaId\", " +
                    " presDtl.order_qty as \"quantity\", " +
                    " presDtl.is_non_formulary as \"isNonFormulary\",   " +
                    " roa.description as \"roaDesc\", " +
                    " itm.formulation_type_id as \"formulationTypeId\", " +
                    " itm.generic_id as \"genericId\", " +
                    " to_char(preFreDtl.start_date, 'dd/mm/yyyy HH:mm:ss') as \"startDate\",   " +
                    " to_char(preFreDtl.stop_date, 'dd/mm/yyyy HH:mm:ss') as \"stopDate\",   " +
                    " to_char(preFreDtl.stop_date, 'dd/mm/yyyy HH:mm:ss') as \"endDate\",   " +
                    " presDtl.instruction_id as \"instructionId\" ," +
                    " presDtl.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\", " +
                    " presDtl.is_non_formulary as \"nonFormularyAdvice\", " +
                    " instr.instruction_desc as \"instructionDesc\", " +
                    " presDtl.is_medication_stop as \"isMedicationStop\","+
                    " presDtl.duration_unit as \"durationUnit\","+
                    " presDtl.duration as \"durationCount\","+
                    " presDtl.generic_name as \"genericName\" " +
                    " FROM pharmacy.t_prescription pres " +
                    " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " +
                    " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " +
                    " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " +
                    " LEFT JOIN public.m_item itm on itm.id=presDtl.item_id " +
                    " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " +
                    " LEFT JOIN public.m_generic gen on itm.generic_id = gen.id " +
                    " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " +
                    " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=presDtl.formulation_dose_mapp_id " +
                    " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id " ;
            StringBuffer s1 = new StringBuffer(query);
            if(medicationDTO.getEncounterId()!=null) {
                s1.append(" WHERE pres.encounter_id = :encounterId " +
                        " and presDtl.is_non_formulary = 'Y' " +
                        " and presDtl.is_non_formulary IS NOT NULL");
            }else {
                s1.append(" WHERE pres.admission_id = :admissionId " +
                        " and presDtl.is_non_formulary = 'Y'" +
                        " and presDtl.is_non_formulary IS NOT NULL");
            }

            Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(s1.toString());

            if(medicationDTO.getEncounterId() != null){
                sqlQuery.setParameter("encounterId", medicationDTO.getEncounterId());
            }
            if(medicationDTO.getAdmissionId() != null){
                sqlQuery.setParameter("admissionId", medicationDTO.getAdmissionId());
            }

            roaMasterDtos = sqlQuery
                    .setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();

            return roaMasterDtos;

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("getMedicationHistoryByEncounterIdNonFormulary failed", e);
        }
        return roaMasterDtos;
    }
	

//	@Override
//	public Response getPriorityOfPrescriptionAccordingToPresDtl(PrescriptionDetailsDto prescriptionDetailsDto) {
//		try {
//			String query =" select " + 
//					"	presDtl.id as \"prescriptionDetailId\",  " + 
//					"	presDtl.priority_id as \"priorityId\", " + 
//					"	presDtl.prescription_id as \"prescriptionId\" " + 
//					" from " + 
//					"	pharmacy.t_prescription_detail presDtl " + 
//					" where " + 
//					"	presDtl.prescription_id = :prescriptionId " + 
//					"	and presDtl.priority_id = 2 " + 
//					"	and pres.organization_id = :organizationId " + 
//					"	and pres.unit_id = :unitId";
//
//			List<PrescriptionDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("organizationId", prescriptionDetailsDto.getOrganizationId())
//					.setParameter("unitId", prescriptionDetailsDto.getUnitId())
//					.setParameter("prescriptionId", prescriptionDetailsDto.getPrescriptionId())
//					.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsDto.class)).list();
//
//			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
//
//		} catch (HibernateException he) {
//			h
//		} catch (Exception e) {
//			
//		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
//	}
//
//	
//	@Override
//	public Response updatePriorityOfPrescriptionAccordingToPresDtl(Integer id) throws ApplicationException {
//		try {
//			
//			Session session=sessionFactory.getCurrentSession();
//			session.createSQLQuery(" update pharmacy.t_prescription set priority_id = 2"
//			+ " where id="+id)
//			.executeUpdate();
//			
//			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
//			
//			} catch (HibernateException he) {
//				h
//			} catch (Exception e) {
//				
//			}
//			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
//	}



}
