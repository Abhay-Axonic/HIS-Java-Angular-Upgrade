package com.param.adt.master.unit.dao;

import java.text.SimpleDateFormat;
import java.util.*;

import com.param.adt.master.unit.dto.DoctorFavouriteMedicationsMasterDto;
import com.param.adt.master.unit.model.DoctorFavouriteMedicationsMaster;
import com.param.global.dto.AdmissionDto;
import com.param.global.model.MrdRackMaster;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;
import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DoctorFavouriteMedicationsMasterDaoImpl  extends GenericDao<DoctorFavouriteMedicationsMaster, Integer> implements IDoctorFavouriteMedicationsMasterDao, ICommonConstants{
    public DoctorFavouriteMedicationsMasterDaoImpl() {
        super(DoctorFavouriteMedicationsMaster.class);
    }
    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Response saveDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        try {
            DoctorFavouriteMedicationsMaster favMedicationMaster = new DoctorFavouriteMedicationsMaster();
            if (favMedicationDto.getSpecialityId() != null && favMedicationDto.getSpecialityId() > 0) {
                favMedicationMaster.setDoctorId(favMedicationDto.getDoctorId());
                favMedicationMaster.setSpecialityId(favMedicationDto.getSpecialityId());
                favMedicationMaster.setItemId(favMedicationDto.getItemId());
                favMedicationMaster.setRoaId(favMedicationDto.getRoaId());
                favMedicationMaster.setDoseUnitId(favMedicationDto.getDoseUnitId());
                favMedicationMaster.setFrequencyId(favMedicationDto.getFrequencyId());
                favMedicationMaster.setDose(favMedicationDto.getDose());
                favMedicationMaster.setNoOfAdministrationTime(favMedicationDto.getNoOfAdministrationTime());
                favMedicationMaster.setPrescribedDose(favMedicationDto.getPrescribedDose());
                favMedicationMaster.setPrescribedDoseUnitId(favMedicationDto.getPrescribedDoseUnitId());
                favMedicationMaster.setInstructionDesc(favMedicationDto.getInstructionDesc());
                favMedicationMaster.setFormulationDosageMappId(favMedicationDto.getFormulationDosageMappId());
                favMedicationMaster.setStatflag(favMedicationDto.getStatflag());
                favMedicationMaster.setInstructionId(favMedicationDto.getInstructionId());
                favMedicationMaster.setFactorId(favMedicationDto.getFactorId());
                favMedicationMaster.setIsMedicationStop(favMedicationDto.getIsMedicationStop());
                favMedicationMaster.setIsNonFormulary(favMedicationDto.getIsNonFormulary());
                favMedicationMaster.setDuration(favMedicationDto.getDuration());
                favMedicationMaster.setDurationUnit(favMedicationDto.getDurationUnit());
                favMedicationMaster.setStrength(favMedicationDto.getStrength());
                favMedicationMaster.setOrderQty(favMedicationDto.getOrderQty());
                favMedicationMaster.setStatus(favMedicationDto.getStatus());
                favMedicationMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(favMedicationDto.getUnitId()));
                favMedicationMaster.setCreatedDate(TimeZoneComponent.getDateByzone(favMedicationDto.getUnitId()));
                favMedicationMaster.setUpdatedBy(favMedicationDto.getUpdatedBy());
                favMedicationMaster.setCreatedBy(favMedicationDto.getCreatedBy());
                favMedicationMaster.setUnitId(favMedicationDto.getUnitId());
                favMedicationMaster.setOrganizationId(favMedicationDto.getOrganizationId());
                favMedicationMaster.setItemName(favMedicationDto.getItemName());
                favMedicationMaster.setGenericName(favMedicationDto.getGenericName());
                save(favMedicationMaster);
                return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
            }
        } catch (Exception e) {
            System.out.println(DoctorFavouriteMedicationsMasterDaoImpl.class.getName()+" "+e.getMessage());
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public Response checkIsExist(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {

        try {

            String query=sessionFactory.getCurrentSession().getNamedQuery("IS_DOCTOR_FAVORITES_MEDICATION_EXIST").getQueryString();
            StringBuffer subQuery=new StringBuffer();
            if(favMedicationDto.getDoctorId()!=null){
                subQuery.append(" AND docFav.doctorId=:doctorId ");

            }
            if(favMedicationDto.getSpecialityId()!=null){
                subQuery.append(" AND docFav.specialityId=:specialityId ");

            }
            if(favMedicationDto.getItemId()!=null){
                subQuery.append(" AND docFav.itemId=:itemId ");

            }
            if(favMedicationDto.getRoaId()!=null){
                subQuery.append(" AND docFav.roaId=;roaId ");

            }
            if(favMedicationDto.getDose()!=null){
                subQuery.append(" AND docFav.dose=:dose" );

            }
            if(favMedicationDto.getFrequencyId()!=null){
                subQuery.append(" AND docFav.frequencyId=:frequencyId ");

            }
            if(favMedicationDto.getDoseUnitId()!=null){
                subQuery.append(" AND docFav.doseUnitId=:doseUnitId ");

            }
            query = query.concat(subQuery.toString());
            Query sql = sessionFactory.getCurrentSession().createQuery(query)
                    .setParameter("orgId", favMedicationDto.getOrganizationId() != null ? favMedicationDto.getOrganizationId():0)
                    .setParameter("unitId", favMedicationDto.getUnitId() != null ? favMedicationDto.getUnitId():0);
            if(favMedicationDto.getDoctorId()!=null){
                sql.setParameter("doctorId", favMedicationDto.getDoctorId());
            }
            if(favMedicationDto.getSpecialityId()!=null){
                sql.setParameter("specialityId", favMedicationDto.getSpecialityId());
            }
            if(favMedicationDto.getItemId()!=null){
                sql.setParameter("itemId", favMedicationDto.getItemId());
            }
            if(favMedicationDto.getRoaId()!=null){
                sql.setParameter("roaId", favMedicationDto.getRoaId());
            }
            if(favMedicationDto.getDose()!=null){
                sql.setParameter("dose", favMedicationDto.getDose());
            }
            if(favMedicationDto.getFrequencyId()!=null){
                sql.setParameter("frequencyId", favMedicationDto.getFrequencyId());
            }
            if(favMedicationDto.getDoseUnitId()!=null){
                sql.setParameter("doseUnitId", favMedicationDto.getDoseUnitId());
            }
            List<DoctorFavouriteMedicationsMasterDto> favMedicationsList =  sql.setResultTransformer(Transformers.aliasToBean(DoctorFavouriteMedicationsMasterDto.class)).list();
            return new Response(SUCCESS, SUCCESS_CODE, null, favMedicationsList, null);
        }catch (HibernateException he){
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
        return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
    }

    @Override
    public Response removeFromFavourites(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        try {
            if(favMedicationDto.getFavouriteMedicationId() != null) {
                sessionFactory.getCurrentSession().createQuery("UPDATE DoctorFavouriteMedicationsMaster docFav set "
                                + "docFav.status='I', "
                                + "docFav.updatedBy=:updatedBy, "
                                + "docFav.updatedDate=:updatedDate "
                                + "WHERE docFav.doctorId=:doctorId "
                                + "AND docFav.favouriteMedicationId=:favouriteMedicationId "
                                + "AND docFav.specialityId=:specialityId "
                                + "AND docFav.organizationId=:orgId "
                                + "AND docFav.unitId=:unitId ")
                        .setParameter("doctorId", favMedicationDto.getDoctorId())
                        .setParameter("favouriteMedicationId", favMedicationDto.getFavouriteMedicationId())
                        .setParameter("specialityId", favMedicationDto.getSpecialityId())
                        .setParameter("orgId", favMedicationDto.getOrganizationId())
                        .setParameter("unitId", favMedicationDto.getUnitId())
                        .setParameter("updatedBy", favMedicationDto.getUpdatedBy())
                        .setParameter("updatedDate", GlobalCommonDateUtils.getDate(favMedicationDto.getUpdatedDate(),"yyyy-M-dd HH:mm:ss")).executeUpdate();
            }else {
                sessionFactory.getCurrentSession().createQuery("UPDATE DoctorFavorites docFav set "
                                + "docFav.status='I', "
                                + "docFav.updatedBy=:updatedBy, "
                                + "docFav.updatedDate=:updatedDate "
                                + "WHERE docFav.doctorId=:doctorId "
                                + "AND docFav.itemId=:itemId "
                                + "AND docFav.organizationId=:orgId "
                                + "AND docFav.unitId=:unitId ")
                        .setParameter("doctorId", favMedicationDto.getDoctorId())
                        .setParameter("itemId", favMedicationDto.getItemId())
                        .setParameter("orgId", favMedicationDto.getOrganizationId())
                        .setParameter("specialityId", favMedicationDto.getSpecialityId())
                        .setParameter("unitId", favMedicationDto.getUnitId())
                        .setParameter("updatedBy", favMedicationDto.getUpdatedBy())
                        .setParameter("updatedDate", GlobalCommonDateUtils.getDate(favMedicationDto.getUpdatedDate(),"yyyy-M-dd HH:mm:ss")).executeUpdate();
            }
            return new Response(SUCCESS, SUCCESS_CODE, "Removed from Favorites !!", null, null);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {

        }
        return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
    }

    @Override
    public Response updateDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        Optional<DoctorFavouriteMedicationsMaster> optionalFavMedMaster = Optional.of(findById(favMedicationDto.getFavouriteMedicationId()));
        if (optionalFavMedMaster.isPresent()){
            DoctorFavouriteMedicationsMaster favMedicationMaster = optionalFavMedMaster.get();
            favMedicationMaster.setDoctorId(favMedicationDto.getDoctorId());
            favMedicationMaster.setSpecialityId(favMedicationDto.getSpecialityId());
            favMedicationMaster.setItemId(favMedicationDto.getItemId());
            favMedicationMaster.setRoaId(favMedicationDto.getRoaId());
            favMedicationMaster.setDoseUnitId(favMedicationDto.getDoseUnitId());
            favMedicationMaster.setFrequencyId(favMedicationDto.getFrequencyId());
            favMedicationMaster.setDose(favMedicationDto.getDose());
            favMedicationMaster.setNoOfAdministrationTime(favMedicationDto.getNoOfAdministrationTime());
            favMedicationMaster.setPrescribedDose(favMedicationDto.getPrescribedDose());
            favMedicationMaster.setPrescribedDoseUnitId(favMedicationDto.getPrescribedDoseUnitId());
            favMedicationMaster.setInstructionDesc(favMedicationDto.getInstructionDesc());
            favMedicationMaster.setFormulationDosageMappId(favMedicationDto.getFormulationDosageMappId());
            favMedicationMaster.setStrength(favMedicationDto.getStrength());
            favMedicationMaster.setStatflag(favMedicationDto.getStatflag());
            favMedicationMaster.setInstructionId(favMedicationDto.getInstructionId());
            favMedicationMaster.setFactorId(favMedicationDto.getFactorId());
            favMedicationMaster.setIsMedicationStop(favMedicationDto.getIsMedicationStop());
            favMedicationMaster.setIsNonFormulary(favMedicationDto.getIsNonFormulary());
            favMedicationMaster.setDuration(favMedicationDto.getDuration());
            favMedicationMaster.setDurationUnit(favMedicationDto.getDurationUnit());
            favMedicationMaster.setOrderQty(favMedicationDto.getOrderQty());
//            favMedicationMaster.setStatus(favMedicationDto.getStatus());
            favMedicationMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(favMedicationDto.getUnitId()));
            favMedicationMaster.setUpdatedBy(favMedicationDto.getUpdatedBy());
            favMedicationMaster.setUnitId(favMedicationDto.getUnitId());
            favMedicationMaster.setOrganizationId(favMedicationDto.getOrganizationId());
            update(favMedicationMaster);
            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

        }
        return new Response(ERROR, ERROR_CODE, ID_NOT_EXISTS, null, null);
    }

    @Override
    public Response getDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(favMedicationDto.getUnitId()));

            List<DoctorFavouriteMedicationsMasterDto> listMedicationDto;
            String query=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_FAVORITES_MEDICATION_LIST_BY_DOC_ID").getQueryString();
            StringBuffer subQuery=new StringBuffer();
            if(favMedicationDto.getDoctorId()!=null){
                subQuery.append(" AND docFav.doctor_id=:doctorId ");

            }
            if(favMedicationDto.getSpecialityId()!=null){
                subQuery.append(" AND docFav.speciality_id=:specialityId ");

            }
            query = query.concat(subQuery.toString());
            Query sql = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameter("orgId", favMedicationDto.getOrganizationId())
                    .setParameter("unitId", favMedicationDto.getUnitId());
            if(favMedicationDto.getDoctorId()!=null){
                 sql.setParameter("doctorId", favMedicationDto.getDoctorId());
            }
            if(favMedicationDto.getSpecialityId()!=null){
                sql.setParameter("specialityId", favMedicationDto.getSpecialityId());
            }
//                    .setParameter("expDate", toDayDate)
            listMedicationDto= sql.setResultTransformer(Transformers.aliasToBean(DoctorFavouriteMedicationsMasterDto.class)).list();
            return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
        } catch (HibernateException he) {
            System.out.println(he);
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
        return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
    }

    @Override
    public Response getDoctorFavouriteMedicationForCoversheet(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        try {

//           List<Integer> specialityList =  sessionFactory.getCurrentSession().createQuery("select distinct speciality_id  "
//                            +" from doctor.t_doctor_speciality_mapper tdsm "
//                            +" where tdsm.doctor_id in(:doctorList) "
//                            +" and tdsm.status ='A' ")
//                    .setParameterList("doctorList", favMedicationDto.getDoctorIdList())
//                    .setResultTransformer(Transformers.aliasToBean(DoctorFavoritesDto.class)).list();


            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(favMedicationDto.getUnitId()));

            List<DoctorFavoritesDto> listMedicationDto;
//            String query=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_FAVORITES_MEDICATION_LIST_FOR_COVERSHEET").getQueryString();
//            StringBuffer subQuery=new StringBuffer();
//            subQuery.append(")AS combined_query ");
//            query = query.concat(subQuery.toString());
//            SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);


            Session session = sessionFactory.getCurrentSession();
            SQLQuery actualQuery = session.createSQLQuery(generateQuery(favMedicationDto).toString());
            actualQuery.setParameter("orgId", favMedicationDto.getOrganizationId())
                    .setParameter("unitId", favMedicationDto.getUnitId());
//                    .setParameter("expDate", toDayDate);

            if (favMedicationDto.getDoctorId() != null) {
                actualQuery.setParameter("doctorId", favMedicationDto.getDoctorId());
            }
            if (favMedicationDto.getSpecialityId() != null) {
                actualQuery.setParameter("specialityId", favMedicationDto.getSpecialityId());
            }

            listMedicationDto = actualQuery.setResultTransformer(Transformers.aliasToBean(DoctorFavoritesDto.class)).list();
            return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {

        }
        return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
    }

    private StringBuilder generateQuery(DoctorFavouriteMedicationsMasterDto favMedicationDto) {

        StringBuilder condition = new StringBuilder();
        StringBuilder condition1 = new StringBuilder();

        if (favMedicationDto.getDoctorId() != null) {
            condition.append(" AND (mdfmm.doctor_id = :doctorId)");
            condition1.append(" AND (docFav.doctor_id = :doctorId)");
        }
        if (favMedicationDto.getSpecialityId() != null) {
            condition.append(" AND (mdfmm.speciality_id = :specialityId) ");
        }

        StringBuilder query = new StringBuilder("SELECT DISTINCT * "
                + " FROM (SELECT DISTINCT mdfmm.favourite_medication_id as \"favouriteMedicationId\", 0 AS \"doctorFavoriteId\", "
                + " case when (mdfmm.item_id is null) then 0 else mdfmm.item_id end AS \"itemId\", "
                + " case when (gen.id is null) then 0 else gen.id end AS \"genericId\", "
//                + " coalesce(temp.rate, 0) as \"rate\",  "
                + " case when (itm.name is null) then mdfmm.item_name else itm.name end AS \"itemName\", "
                + " case when (gen.name is null) then mdfmm.generic_name else gen.name end AS \"genericName\", "
                + " itm.is_dose_calculation_applicable as \"isDoseCalculationApplicable\", "
                + " itm.dose_calculation_applicable_data as \"doseCalculationApplicableData\", "
                + " mdfmm.roa_id as \"roaId\",  "
                + " mdfmm.order_qty as \"quantity\",  "
                + " mdfmm.duration as \"durationCount\",  "
                + " roa.description as \"roaDesc\",  "
                + " mdfmm.dose_unit_id as \"doseUnitId\",  "
                + " mdfmm.dose as \"dose\",  "
                + " su.unit as \"doseDesc\",  "
                + " mdfmm.frequency_id as \"frequencyId\",  "
                + " freq.description as \"frequencyDesc\",  "
                + " mdfmm.instruction_id as \"instructionId\" , "
                + " instr.instruction_desc as \"instructionDesc\",  "
                + " mdfmm.statflag as \"isStat\","
                + "  mdfmm.order_qty as \"orderQty\",   " +
                "  mdfmm.is_medication_stop as \"isMedicationStop\",   " +
                " mdfmm.is_non_formulary as \"isNonFormulary\",   " +
                " mdfmm.strength as \"strength\",   " +
                " mdfmm.duration_unit as \"durationUnit\","
                + " mdfmm.instruction_desc \"instructionText\",  "
                + " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\", "
                + " mdfmm.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",mdfmm.factor_id as \"factorId\", "
                + " mdfmm.formulation_dose_mapp_id as \"formulationDosageMappId\", "
                + " fr.no_of_administration_time as \"noOfAdministrationTime\",  "
                + " itm.formulation_type_id as \"formulationTypeId\"  " + "FROM doctor.m_doctor_favorite_medications_master mdfmm  "
                + " LEFT JOIN public.m_item itm on itm.id=mdfmm.item_id  "
                + " LEFT JOIN public.m_route_of_administration roa on roa.id=mdfmm.roa_id  "
                + " LEFT JOIN public.m_generic gen on itm.generic_id = gen.id  "
                + " INNER JOIN public.m_frequency freq on freq.id=mdfmm.frequency_id  "
                + " LEFT JOIN public.m_strength_unit su ON mdfmm.dose_unit_id = su.id  "
                + " INNER JOIN public.m_frequency fr ON mdfmm.frequency_id = fr.id  "
                + " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=mdfmm.formulation_dose_mapp_id  "
                + " LEFT JOIN public.m_instruction instr on instr.instruction_id=mdfmm.instruction_id  "
                + " LEFT JOIN public.m_strength_unit presDose ON mdfmm.prescribe_dose_unit_id = presDose.id "
//                + "  LEFT JOIN ( select  max( cast((b.rate) as decimal(10, 2))) rate , mi.id item_id " +
//                "	from inventory.t_batch b  " +
//                "	join inventory.t_store_stock S on B.id = S.batch_id  " +
//                "	join m_item mi on b.item_id = mi.id  " +
//                "	join m_store ms on s.store_id = ms.store_id  " +
//                "	join m_store_item_mapper msim on msim.item_id = mi.id  " +
//                "	where  " +
//                "	msim.unit_id = :unitId  " +
//                "	and ms.unit_id = :unitId  " +
//                "	and ms.is_pharmacy = 'Y' " +
//                "	and b.unit_id = :unitId  " +
//                "	and s.unit_id = :unitId  " +
//                "	and to_char(b.expiry_date, 'yyyy-mm-dd') >= :expDate  " +
//                "	and b.is_quarantine = 'N'  " +
//                "	group by mi.id  " +
//                "	) as temp on temp.item_id = mdfmm.item_id "
                + " WHERE "
                + " mdfmm.organization_id=:orgId "
                + " AND mdfmm.unit_id=:unitId "
                + " AND mdfmm.status='A' "
                + condition
                + " UNION "
                + " select DISTINCT 0 AS \"favouriteMedicationId\", docFav.doctor_favorite_id as \"doctorFavoriteId\",  "
                + "  docFav.item_id AS \"itemId\", "
                + " gen.id AS \"genericId\", "
//                + " coalesce(temp.rate, 0) as \"rate\",  "
                + " itm.name AS \"itemName\", "
                + " gen.name AS \"genericName\", "
                + " itm.is_dose_calculation_applicable as \"isDoseCalculationApplicable\", "
                + " itm.dose_calculation_applicable_data as \"doseCalculationApplicableData\", "
                + " docFav.roa_id as \"roaId\",  "
                + " docFav.order_qty as \"quantity\",  "
                + " docFav.duration as \"durationCount\",  "
                + " roa.description as \"roaDesc\",  "
                + " docFav.dose_unit_id as \"doseUnitId\",  "
                + " docFav.dose as \"dose\",  "
                + " su.unit as \"doseDesc\",  "
                + " docFav.frequency_id as \"frequencyId\",  "
                + " freq.description as \"frequencyDesc\",  "
                + " docFav.instruction_id as \"instructionId\" , "
                + " instr.instruction_desc as \"instructionDesc\",  "
                + " docFav.statflag as \"isStat\","
                + "  docFav.order_qty as \"orderQty\",   " +
                "  docFav.is_medication_stop as \"isMedicationStop\",   " +
                " docFav.is_non_formulary as \"isNonFormulary\",   " +
                " docFav.strength as \"strength\",   " +
                " docFav.duration_unit as \"durationUnit\","
                + " docFav.instruction_desc \"instructionText\",  "
                + " presDose.id as \"prescribeDoseUnitId\",presDose.unit as \"prescribeDoseUnit\", "
                + " docFav.prescribe_dose as \"prescribeDose\",fdm.dosage_form as \"dosageForm\",docFav.factor_id as \"factorId\", "
                + " docFav.formulation_dose_mapp_id as \"formulationDosageMappId\", "
                + " fr.no_of_administration_time as \"noOfAdministrationTime\",  "
                + " itm.formulation_type_id as \"formulationTypeId\" "
                + " FROM doctor.t_doctor_favorites docFav  "
                + " INNER JOIN public.m_item itm on itm.id=docFav.item_id  "
                + " LEFT JOIN public.m_route_of_administration roa on roa.id=docFav.roa_id  "
                + " INNER JOIN public.m_generic gen on itm.generic_id = gen.id  "
                + " INNER JOIN public.m_frequency freq on freq.id=docFav.frequency_id  "
                + " LEFT JOIN public.m_strength_unit su ON docFav.dose_unit_id = su.id  "
                + " INNER JOIN public.m_frequency fr ON docFav.frequency_id = fr.id  "
                + " LEFT JOIN public.m_fomulation_dosage_mapper fdm on fdm.formulation_dosage_mapp_id=docFav.formulation_dose_mapp_id  "
                + " LEFT JOIN public.m_instruction instr on instr.instruction_id=docFav.instruction_id  "
                + " LEFT JOIN public.m_strength_unit presDose ON docFav.prescribe_dose_unit_id = presDose.id "
//                + "  LEFT JOIN ( select  max( cast((b.rate) as decimal(10, 2))) rate , mi.id item_id " +
//                "	from inventory.t_batch b  " +
//                "	join inventory.t_store_stock S on B.id = S.batch_id  " +
//                "	join m_item mi on b.item_id = mi.id  " +
//                "	join m_store ms on s.store_id = ms.store_id  " +
//                "	join m_store_item_mapper msim on msim.item_id = mi.id  " +
//                "	where  " +
//                "	msim.unit_id = :unitId  " +
//                "	and ms.unit_id = :unitId  " +
//                "	and ms.is_pharmacy = 'Y' " +
//                "	and b.unit_id = :unitId  " +
//                "	and s.unit_id = :unitId  " +
//                "	and to_char(b.expiry_date, 'yyyy-mm-dd') >= :expDate  " +
//                "	and b.is_quarantine = 'N'  " +
//                "	group by mi.id  " +
//                "	) as temp on temp.item_id = docFav.item_id "
                + " WHERE docFav.organization_id=:orgId "
                + " AND docFav.unit_id=:unitId "
                + " AND docFav.status='A' "
                + condition1 +")AS combined_query;");
        return query;

    }
}
