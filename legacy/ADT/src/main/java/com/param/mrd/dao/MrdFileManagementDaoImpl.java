package com.param.mrd.dao;

import com.param.adt.mrd.MrdFileAllocation;
import com.param.adt.mrd.MrdFileAllocationStatus;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.mrd.dto.MrdFileListResponseDto;
import com.param.mrd.dto.MrdFileManagementDto;
import com.param.mrd.dto.MrdFileStatusUpdateDto;
import com.param.mrd.dto.SearchNoResponseDto;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.param.global.common.ICommonConstants.*;

@Repository
public class MrdFileManagementDaoImpl extends GenericDao<MrdFileAllocation, Integer>  implements IMrdFileManagementDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IGlobalUnitMasterDao iGlobalUnitMasterDao;

    @Autowired
    private IMrdFileStatusDao iMrdFileStatusDao;

    public MrdFileManagementDaoImpl() {
        super(MrdFileAllocation.class);
    }

    @Override
    public Response searchNumberByPatientIdAndType(String visitType, Integer patientId) {
        try {
//            String admissionQuery = "select a.admission_id as \"admissionId\",a.admission_number as \"admissionNo\"" +
//                    "from adt.t_admission a" +
//                    "inner join patient.t_patient_registration p on p.patient_id = a.patient_id" +
//                    "and  p.patient_id = " + patientId;
//            String encounterQuery = "select e.encounter_id as \"encounterId\",e.encounter_number as \"encounterNo\"" +
//                    "from opd.t_encounter_master e" +
//                    "inner join patient.t_patient_registration p on p.patient_id = e.patient_id" +
//                    "and  p.patient_id = " + patientId;
            String query = "select a.admission_id as \"admissionId\",a.admission_number as \"admissionNo\", null as \"encounterId\"," +
                    "null as \"encounterNo\",a.visit_type_id as \"visitTypeId\" " +
                    "               from adt.t_admission a " +
                    "               inner join patient.t_patient_registration p on p.patient_id = a.patient_id " +
                    "   inner join adt.t_admission_details tad on a.admission_id  = tad.admission_id " +
                    " where tad.is_discharge  = 'D' and p.patient_id =:patientId " +
                    "   union   " +
                    "   select null as \"admissionId\",null as \"admissionNo\",e.encounter_id as \"encounterId\", " +
                    "   e.encounter_number as \"encounterNo\",coalesce(e.visit_type_id,1) as \"visitTypeId\" " +
                    "                    from opd.t_encounter_master e " +
                    "                    inner join patient.t_patient_registration p on p.patient_id = e.patient_id " +
                    "where e.status_id in (9,6) and  p.patient_id =:patientId ";

            List list = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("patientId", patientId)
                    .setResultTransformer(Transformers.aliasToBean(SearchNoResponseDto.class)).list();


            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        } catch (Exception e) {
            return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
        }
    }

    @Override
    public Response saveFileAllocation(MrdFileManagementDto mrdDto) {
        try {


//            String query = "SELECT EXISTS(select 1 from t_mrd_file_allocation  "
//                    + "  where upper(file_no) like upper(:fileNo) "
//                    + "  and patient_id=:patientId "
//                    + "  and encounter_id=:encounterId or admission_id=:admissionId "
//                    + "  and organization_id =:organizationId"
//                    + "  and unit_id=: unitId ";

            String query = "select exists (select 1 from adt.t_mrd_file_management where rack_id =:rackId and row_id =:rowId and box_id =:boxId " +
                    "and unit_id =:unitId and organization_id =:orgId)";
            System.out.println("exists query " + query);

            Boolean isExist = (Boolean) sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("rackId", mrdDto.getRackId())
                    .setParameter("rowId", mrdDto.getRowId())
                    .setParameter("boxId", mrdDto.getBoxId())
                    .setParameter("orgId", mrdDto.getOrganizationId())
                    .setParameter("unitId", mrdDto.getUnitId())
                    .uniqueResult();

            System.out.println("is exists " + isExist);
            Integer statusId = (Integer) sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_FILE_STATUS_ID")
                    .setParameter("status", mrdDto.getStatus())
                    .uniqueResult();

            if (!isExist) {


                MrdFileAllocation fileAllocation = new MrdFileAllocation();
                fileAllocation.setPatientId(mrdDto.getPatientId());
                fileAllocation.setRackId(mrdDto.getRackId());
                fileAllocation.setRowId(mrdDto.getRowId());
                fileAllocation.setBoxId(mrdDto.getBoxId());
                fileAllocation.setFileNo(mrdDto.getFileNo());
                fileAllocation.setAdmissionId(mrdDto.getAdmissionId() != null ? mrdDto.getAdmissionId() : 0);
                fileAllocation.setEncounterId(mrdDto.getEncounterId() != null ? mrdDto.getEncounterId() : 0);
                fileAllocation.setStatusId(statusId);
                fileAllocation.setOrganizationId(mrdDto.getOrganizationId());
                fileAllocation.setUnitId(mrdDto.getUnitId());
                fileAllocation.setCreatedBy(mrdDto.getCreatedBy());
                fileAllocation.setUpdatedBy(mrdDto.getCreatedBy());
                fileAllocation.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(mrdDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
                fileAllocation.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(mrdDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
                fileAllocation.setAllocationNumber(iGlobalUnitMasterDao.getGeneratedNo(0, fileAllocation.getUnitId(), IBillingCommonConstants.MRD_FILE_ALLOCATION));
                save(fileAllocation);
                System.out.println("saved successfully");
                return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

            } else {
                System.out.println("already exists");

                return new Response(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
            }
        } catch (Exception e) {

            return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, e.getMessage());
        }
    }

    @Override
    public Response updateFileAllocation(MrdFileManagementDto mrdDto) {
        try {
            String query = "select exists (select 1 from adt.t_mrd_file_management where rack_id =:rackId and row_id =:rowId and box_id =:boxId " +
                    "and unit_id =:unitId and organization_id =:orgId and mrd_file_id !=:allocationId)";
            System.out.println("exists query " + query);
            Integer statusId = (Integer) sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_FILE_STATUS_ID")
                    .setParameter("status", mrdDto.getStatus())
                    .uniqueResult();
            System.out.println("statusId " + statusId);

            Boolean isExist = (Boolean) sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("rackId", mrdDto.getRackId())
                    .setParameter("rowId", mrdDto.getRowId())
                    .setParameter("boxId", mrdDto.getBoxId())
                    .setParameter("orgId", mrdDto.getOrganizationId())
                    .setParameter("unitId", mrdDto.getUnitId())
                    .setParameter("allocationId", mrdDto.getAllocationId())
                    .uniqueResult();
            System.out.println("isExist " + isExist);

            if (!isExist) {
                Optional<MrdFileAllocation> allocationOptional = Optional.of(findById(mrdDto.getAllocationId()));

                MrdFileAllocation fileAllocation = allocationOptional.get();
                fileAllocation.setRackId(mrdDto.getRackId());
                fileAllocation.setRowId(mrdDto.getRowId());
                fileAllocation.setBoxId(mrdDto.getBoxId());
                fileAllocation.setFileNo(mrdDto.getFileNo());
                fileAllocation.setStatusId(statusId);
                fileAllocation.setOrganizationId(mrdDto.getOrganizationId());
                fileAllocation.setUnitId(mrdDto.getUnitId());
                fileAllocation.setUpdatedBy(mrdDto.getCreatedBy());
                fileAllocation.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(mrdDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
                update(fileAllocation);
                System.out.println("updated successfully");
                return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
            } else {
                System.out.println("already exists");
                return new Response(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
            }

        } catch (Exception e) {
            return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, e.getMessage());
        }
    }

    @Override
    public List<MrdFileListResponseDto> getMrdFiles(Integer patientId, Integer unitId, Integer organizationId, String status) {
        status = status.equalsIgnoreCase("All") ? null :status;
        
        StringBuilder query = new StringBuilder().append("select " +
                "distinct m.mrd_file_id as \"allocationId\"," +
                " m.allocation_number as \"allocationNumber\"," +
                " case " +
                "  when m.admission_id is null then 'OPD'" +
                "  else 'IPD' " +
                " end as \"visitType\", " +
                " coalesce(ad.admission_number, " +
                " e.encounter_number) as \"admissionEncounterNumber\"," +
                " r.rack_id as \"rackId\"," +
                " r.rack_description as \"rackDescription\"," +
                " b.box_id as \"boxId\"," +
                " b.box_description as \"boxDescription\"," +
                " w.row_id as \"rowId\"," +
                " w.row_description as \"rowDescription\"," +
                " m.file_no as \"fileNo\"," +
                " coalesce(s.file_status_description," +
                " 'Available') as \"fileStatus\"," +
                " coalesce (tmfms.issue_no,'-' ) as \"issuedNo\"," +
                " coalesce (tmfms.request_no,'-' ) as \"requestedNo\"," +
                " coalesce(tmfms.status," +
                " 'A') as \"status\"," +
                " to_char(coalesce(ad.created_date," +
                " e.created_date)," +
                " 'DD/MM/YYYY') as \"admissionEncounterDate\"," +
                " coalesce(to_char(d.discharge_date," +
                " 'DD/MM/YYYY')," +
                " '-') as \"dischargeDate\"," +
                " m.encounter_id as \"encounterId\"," +
                " (case " +
                "  when reqemp.employee_id is not null then concat(reqemp.first_name, " +
                "  ' ', " +
                "  reqemp.last_name) " +
                "  when reqdoc.doctor_id is not null then concat('Dr. ', " +
                "  reqdoc.first_name, " +
                "  ' ', " +
                "  reqdoc.last_name) " +
                "  else '-' " +
                " end) as \"requestedBy\", " +
                " (case " +
                "  when acpemp.employee_id is not null then concat(acpemp.first_name, " +
                "  ' ', " +
                "  acpemp.last_name) " +
                "  when acpdoc.doctor_id is not null then concat('Dr. ', " +
                "  acpdoc.first_name, " +
                "  ' ', " +
                "  acpdoc.last_name) " +
                "  else '-' " +
                " end) as \"acceptedBy\", " +
                " m.admission_id as \"admissionId\", " +
                " m.patient_id as \"patientId\", " +
                " coalesce(to_char(m.created_date, " +
                " 'DD/MM/YYYY'), " +
                " '-') as \"createdDate\", " +
                " tmfms.requested_by as \"requestedById\" " +
                "from " +
                " adt.t_mrd_file_management m " +
                "inner join public.m_mrd_rack_master r on " +
                " r.rack_id = m.rack_id " +
                "inner join public.m_mrd_box_master b on " +
                " b.box_id = m.box_id " +
                "inner join public.m_mrd_row_master w on " +
                " w.row_id = m.row_id " +
                "left join adt.t_admission ad on " +
                " ad.admission_id = m.admission_id " +
                "left join opd.t_encounter_master e on " +
                " e.encounter_id = m.encounter_id " +
                "left join adt.t_discharge d on " +
                " d.admission_id = ad.admission_id  and d.status ='A' " +
                "left join adt.t_mrd_file_management_status tmfms on " +
                " tmfms.allocation_id = m.mrd_file_id " +
                "left join adt.m_mrd_file_status s on " +
                " s.file_status_id = tmfms.status_id " +
                "left join public.m_user_master req on " +
                " tmfms.requested_by = req.user_id " +
                "left join public.m_employee_master reqemp on " +
                " req.user_id = reqemp.user_id " +
                "left join doctor.m_doctor_master reqdoc on " +
                " req.user_id = reqdoc.user_id " +
                "left join public.m_user_master acp on " +
                " tmfms.accepted_by = acp.user_id " +
                "left join public.m_employee_master acpemp on " +
                " acp.user_id = acpemp.user_id " +
                "left join doctor.m_doctor_master acpdoc on " +
                " acp.user_id = acpdoc.user_id " +
                "where " +
                " m.patient_id =:patientId  " +
                " and m.organization_Id =:orgId " +
                " and m.unit_Id =:unitId " +
                " and (:mrdStatus is null or s.file_status_description =:mrdStatus) " +
                " order by status asc ;");

//        StringBuilder query = new StringBuilder().append("select distinct m.mrd_file_id as \"allocationId\", m.allocation_number as \"allocationNumber\",  " +
//                "                      case when m.admission_id is null then 'OPD' else 'IPD' end as \"visitType\",  " +
//                "                      coalesce(ad.admission_number,e.encounter_number) as \"admissionEncounterNumber\",  " +
//                "                      r.rack_id as \"rackId\", r.rack_description AS \"rackDescription\", b.box_id as \"boxId\",  " +
//                "  b.box_description as \"boxDescription\",  " +
//                "                      w.row_id as \"rowId\", w.row_description as \"rowDescription\",m.file_no as \"fileNo\",  " +
//                "                      s1.file_status_description as \"fileStatus\",  " +
//                "                      case when s.file_status_description = 'Requested' then tmfms.status_no else '-' end as \"requestedNo\", " +
//                "                      case when (s3.file_status_description = 'Issued' and tmfms2.parent_id is not null) then tmfms2.status_no else '-' end as \"issuedNo\",     " +
//                "                      coalesce (tmfms2.status,'A') as \"status\", " +
//                "                      to_char(coalesce(ad.created_date,e.created_date),'DD/MM/YYYY') as \"admissionEncounterDate\",  " +
//                "                      coalesce(to_char(d.discharge_date,'DD/MM/YYYY'),'-') as \"dischargeDate\", m.encounter_id as \"encounterId\",  " +
//                "   (case when reqemp.employee_id is not null then concat(reqemp.first_name, ' ', reqemp.last_name) " +
//                "    when reqdoc.doctor_id is not null then concat('Dr. ', reqdoc.first_name, ' ', reqdoc.last_name)  " +
//                "    else '-' end) as \"requestedBy\",  " +
//                "  (case when acpemp.employee_id is not null then concat(acpemp.first_name, ' ', acpemp.last_name) " +
//                "    when acpdoc.doctor_id is not null then concat('Dr. ', acpdoc.first_name, ' ', acpdoc.last_name)  " +
//                "    else '-' end) as \"acceptedBy\",  " +
//                "                      m.admission_id as \"admissionId\", m.patient_id as \"patientId\", " +
//                "  coalesce(to_char(m.created_date,'DD/MM/YYYY'),'-') as \"createdDate\"  " +
//                "                      from adt.t_mrd_file_management m  " +
//                "                      inner join public.m_mrd_rack_master r on r.rack_id = m.rack_id  " +
//                "                      inner join public.m_mrd_box_master b on b.box_id = m.box_id  " +
//                "                      inner join public.m_mrd_row_master w on w.row_id = m.row_id  " +
//                "                      left join adt.t_admission ad on ad.admission_id = m.admission_id  " +
//                "                      left join opd.t_encounter_master e on e.encounter_id = m.encounter_id  " +
//                "                      left join adt.t_discharge d on d.admission_id  = ad.admission_id  " +
//                "                      left join adt.t_mrd_file_management_status tmfms on tmfms.allocation_id = m.mrd_file_id " +
//                "                      left  join adt.m_mrd_file_status s on s.file_status_id =tmfms.status_id   " +
//                "                      left  join adt.m_mrd_file_status s1 on s1.file_status_id =m.status_id " +
//                "    left join public.m_user_master req on s.updated_by  = req.user_id " +
//                "    left join public.m_employee_master reqemp on req.user_id = reqemp.user_id  " +
//                "    left join doctor.m_doctor_master reqdoc on req.user_id = reqdoc.user_id   " +
//                "    left join public.m_user_master acp on s.updated_by  = acp.user_id " +
//                "    left join public.m_employee_master acpemp on req.user_id = acpemp.user_id  " +
//                "    left join doctor.m_doctor_master acpdoc on req.user_id = acpdoc.user_id   " +
//                "    left join adt.t_mrd_file_management_status tmfms2 on tmfms.mrd_file_status_id = tmfms2.parent_id  and  tmfms2.allocation_id = m.mrd_file_id " +
//                "    left  join adt.m_mrd_file_status s3 on s3.file_status_id =tmfms2.status_id " +
//                " where m.patient_id =:patientId and m.organization_Id =:orgId and m.unit_Id =:unitId " +
//                     " and (:mrdStatus is null or s1.file_status_description =:mrdStatus) ");
//                if(status!=null && status.equalsIgnoreCase("Issued"))
//                    query.append(" and tmfms2.allocation_id = tmfms.allocation_id ");


        System.out.println("String sql " + query);
        List<MrdFileListResponseDto> list = sessionFactory.getCurrentSession()
                .createSQLQuery(query.toString())
                .setParameter("patientId", patientId)
                .setParameter("orgId", organizationId)
                .setParameter("unitId", unitId)
                .setParameter("mrdStatus",status)
                .setResultTransformer(Transformers.aliasToBean(MrdFileListResponseDto.class)).list();
        System.out.println("Dao list size" + list.size());
        return list;
    }
    @Transactional
    @Override
    public Response updateMrdAllocationStatus(List<MrdFileStatusUpdateDto> mrdDto) {
        List<MrdFileAllocationStatus> list = new ArrayList<>();
        try {
            mrdDto.forEach(mrdFileStatusUpdateDto -> {
                Optional<MrdFileAllocation> allocationOptional = Optional.of(findById(mrdFileStatusUpdateDto.getAllocationId()));
                if (allocationOptional.isPresent()) {
                    Integer statusId = (Integer) sessionFactory.getCurrentSession()
                            .getNamedQuery("GET_FILE_STATUS_ID")
                            .setParameter("status", mrdFileStatusUpdateDto.getFileStatus())
                            .uniqueResult();
                    System.out.println("statusId " + statusId);
                    allocationOptional.get().setStatusId(statusId);
                    allocationOptional.get().setUpdatedBy(mrdFileStatusUpdateDto.getUpdatedBy());

                    update(allocationOptional.get());
                    Integer parentId = (Integer) sessionFactory.getCurrentSession()
                            .getNamedQuery("GET_FILE_STATUS_ID_AS_PARENT_ID")
                            .setParameter("allocationId",mrdFileStatusUpdateDto.getAllocationId())
                            .uniqueResult();
                    if (mrdFileStatusUpdateDto.getFileStatus()!=null) {
                        MrdFileAllocationStatus mrdFileAllocationStatus = new MrdFileAllocationStatus();
                        mrdFileAllocationStatus.setAllocationId(mrdFileStatusUpdateDto.getAllocationId());
                        mrdFileAllocationStatus.setFileNo(allocationOptional.get().getFileNo());
                        mrdFileAllocationStatus.setStatusId(statusId);
                        if (mrdFileStatusUpdateDto.getFileStatus().equalsIgnoreCase("Requested")) {

                            sessionFactory.getCurrentSession()
                                    .getNamedQuery("INACTIVE_FILE_ALLOCATION_STATUS_BY_ALLOCATION_ID")
                                    .setParameter("allocationId", mrdFileAllocationStatus.getAllocationId())
                                    .executeUpdate();

                            allocationOptional.get().setRequestedBy(mrdFileStatusUpdateDto.getUpdatedBy());
                            mrdFileAllocationStatus.setRequestedBy(mrdFileStatusUpdateDto.getUpdatedBy());
                            mrdFileAllocationStatus.setRequestedNo( iGlobalUnitMasterDao.getGeneratedNo(0, allocationOptional.get().getUnitId(), IBillingCommonConstants.MRD_FILE_ALLOCATION_REQUEST_NO));
                            mrdFileAllocationStatus.setStatus(ACTIVE);
                        }
                        else if (mrdFileStatusUpdateDto.getFileStatus().equalsIgnoreCase("Issued")) {
                            allocationOptional.get().setAcceptedBy(mrdFileStatusUpdateDto.getUpdatedBy());
                            sessionFactory.getCurrentSession()
                                    .getNamedQuery("INACTIVE_FILE_ALLOCATION_STATUS_BY_ALLOCATION_ID")
                                    .setParameter("allocationId", mrdFileAllocationStatus.getAllocationId())
                                    .executeUpdate();
                            mrdFileAllocationStatus.setRequestedBy(mrdFileStatusUpdateDto.getRequestedById());
                            mrdFileAllocationStatus.setRequestedNo(mrdFileStatusUpdateDto.getRequestedNo());
                            mrdFileAllocationStatus.setAcceptedBy(mrdFileStatusUpdateDto.getUpdatedBy());
                            mrdFileAllocationStatus.setIssuedNo( iGlobalUnitMasterDao.getGeneratedNo(0, allocationOptional.get().getUnitId(), IBillingCommonConstants.MRD_FILE_ALLOCATION_ISSUE_NO));
                            mrdFileAllocationStatus.setStatus(ACTIVE);
                            mrdFileAllocationStatus.setParentId(parentId);
                        }
                        else {
                            //inactive all record with allocation id
                            if (mrdFileStatusUpdateDto.getFileStatus().equalsIgnoreCase("Available")) {
                                mrdFileAllocationStatus.setStatus(ACTIVE);
                            }else{
                                mrdFileAllocationStatus.setRequestedNo(mrdFileStatusUpdateDto.getRequestedNo());
                                mrdFileAllocationStatus.setIssuedNo(mrdFileStatusUpdateDto.getIssuedNo());
                                mrdFileAllocationStatus.setParentId(parentId);
                                mrdFileAllocationStatus.setStatus(INACTIVE);
                            }

                            sessionFactory.getCurrentSession()
                                    .getNamedQuery("INACTIVE_FILE_ALLOCATION_STATUS_BY_ALLOCATION_ID")
                                    .setParameter("allocationId", mrdFileAllocationStatus.getAllocationId())
                                    .executeUpdate();
                            mrdFileAllocationStatus.setParentId(parentId);

                        }
                        mrdFileAllocationStatus.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(allocationOptional.get().getUnitId()), "dd-MM-yyyy HH:mm:ss"));
                        mrdFileAllocationStatus.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(allocationOptional.get().getUnitId()), "dd-MM-yyyy HH:mm:ss"));
                        mrdFileAllocationStatus.setCreatedBy(mrdFileStatusUpdateDto.getUpdatedBy());
                        mrdFileAllocationStatus.setUpdatedBy(mrdFileStatusUpdateDto.getUpdatedBy());
                        list.add(mrdFileAllocationStatus);
//                        Integer mrdFileAllocationStatusId = (Integer)sessionFactory.getCurrentSession().save(mrdFileAllocationStatus);
                    }
                }
            });
            iMrdFileStatusDao.saveMrdFileStatus(list);
            return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e){
            throw new ApplicationException(e.getMessage(), e);
    }
    }
    @Transactional
    @Override
    public Response validateMrdUser(Integer userId, Integer unitId, Integer organizationId) {
        try {
            String sql = "SELECT EXISTS(select 1 from t_user_role_mapper turm inner join m_role_master mrm on mrm.role_id  = turm.role_id " +
                    "where turm.user_id =:userId and mrm.role_type_id = 17 " +
                    "and turm.organisation_id =:orgId and turm.unit_id =:unitId " +
                    "and turm.status = 'A' and mrm.status = 'A') ";
            Boolean isUserValid = (Boolean) sessionFactory.getCurrentSession()
                    .createSQLQuery(sql)
                    .setParameter("userId", userId)
                    .setParameter("orgId", organizationId)
                    .setParameter("unitId", unitId)
                    .uniqueResult();
            Map<String,Object> map = new HashMap<>();
            map.put("isMrdUser",isUserValid);
            return new Response(SUCCESS, SUCCESS_CODE, null, null, map);
        }catch (Exception e){
            throw new ApplicationException(e.getMessage(), e);
        }
    }
}
