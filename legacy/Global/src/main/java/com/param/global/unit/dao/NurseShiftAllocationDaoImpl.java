package com.param.global.unit.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.BedIdNurseShiftAllocation;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.model.NurseShiftAllocation;
import com.param.global.model.NurseShiftAllocationDetails;
import com.param.global.unit.dto.NurseShiftAllocationDetailsDto;
import com.param.global.unit.dto.NurseShiftAllocationDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NurseShiftAllocationDaoImpl extends GenericDao<NurseShiftAllocationDetails, Integer>
		implements INurseShiftAllocationDao, ICommonConstants, IError {

	public NurseShiftAllocationDaoImpl() {
		super(NurseShiftAllocationDetails.class);
	}

	final static Logger logger = Logger.getLogger(NurseShiftAllocationDetails.class);

	@Override
	public Response getCheckNurseAlocation(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			String fdate=nurseShiftAllocationDto.getFromDate().substring(0, 10);
			List<NurseShiftAllocationDto> NurseAllocationDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECK_NURSE_ALLOACTION_BY_NURSE_ID")
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setParameter("wardId", nurseShiftAllocationDto.getWardId())
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("fromdate", nurseShiftAllocationDto.getFromDate().substring(0, 10))
					.setParameter("todate", nurseShiftAllocationDto.getToDate().substring(0, 10))
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, NurseAllocationDto, null);

		} catch (HibernateException he) {
		logger.error("getCheckNurseAlocation failed", he);
		} catch (Exception e) {
		logger.error("getCheckNurseAlocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveNurseShiftAllocation(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		//System.out.println(nurseShiftAllocationDto);
		try {
			NurseShiftAllocationDto nurseShiftAllocationDto2=(NurseShiftAllocationDto)sessionFactory.getCurrentSession()
					.createSQLQuery("select nsa.nurse_shift_allocation_id as  \"nurseShiftAllocationId\" "
					+ " from public.m_nurse_shift_allocation nsa "
					+ "where nsa.nurse_id=:nurseId and nsa.org_id=:orgId and nsa.unit_id=:unitId and nsa.ward_id=:wardId ")
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("wardId", nurseShiftAllocationDto.getWardId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).uniqueResult();
			
			NurseShiftAllocation nurseShiftAllocation = new NurseShiftAllocation();
			
			nurseShiftAllocation.setFromDate(
					GlobalCommonDateUtils.getDate(nurseShiftAllocationDto.getFromDate(), "yyyy-M-dd HH:mm:ss"));
			nurseShiftAllocation.setToDate(
					GlobalCommonDateUtils.getDate(nurseShiftAllocationDto.getToDate(), "yyyy-M-dd HH:mm:ss"));
			nurseShiftAllocation.setWardId(nurseShiftAllocationDto.getWardId());
			nurseShiftAllocation.setNurseId(nurseShiftAllocationDto.getNurseId());
			nurseShiftAllocation.setUnitId(nurseShiftAllocationDto.getUnitId());
			nurseShiftAllocation.setOrgId(nurseShiftAllocationDto.getOrgId());
			nurseShiftAllocation.setStatus(nurseShiftAllocationDto.getStatus());
			nurseShiftAllocation.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
			nurseShiftAllocation.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
			nurseShiftAllocation.setStatus(nurseShiftAllocationDto.getStatus());
			nurseShiftAllocation.setCreatedBy(nurseShiftAllocationDto.getCreatedBy());
			nurseShiftAllocation.setCreatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
			nurseShiftAllocation.setStatus('A');
			
			if(nurseShiftAllocationDto2==null) {
				sessionFactory.getCurrentSession().save(nurseShiftAllocation);
			}else {
				nurseShiftAllocation.setNurseShiftAllocationId(nurseShiftAllocationDto2.getNurseShiftAllocationId());
				sessionFactory.getCurrentSession().update(nurseShiftAllocation);
			}
			//make inactive previous shift allocation
			List<String> shiftDates=new ArrayList<String>();
			for(NurseShiftAllocationDetailsDto nurseShiftDto : nurseShiftAllocationDto.getShiftList()) {
				shiftDates.add(nurseShiftDto.getShiftDate().split(" ")[0]);
			}
			
			sessionFactory.getCurrentSession().createSQLQuery("update  public.m_nurse_shift_allocation_details " 
					+"set is_deleted ='Y' " 
					+"where  nurse_id =:nurseId " 
					+"and nurse_shift_allocation_id=(select nsa.nurse_shift_allocation_id  from public.m_nurse_shift_allocation nsa " 
					+"where nsa.nurse_id=:nurseId and nsa.ward_id=:wardId and nsa.unit_id =:unitId and nsa.org_id=:orgId) " 
					+"and to_char(shift_date,'DD-MM-YYYY') in :shiftDate " 
					+"and unit_id =:unitId and org_id=:orgId ")
			.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
			.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
			.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
			.setParameter("wardId", nurseShiftAllocationDto.getWardId())
			.setParameterList("shiftDate", shiftDates)
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createSQLQuery("update  public.m_bed_id_nurse_shift_allocation   " 
					+" set is_deleted='Y',is_bed_assigned ='N' " 
					+" where nurse_shift_allocation_details_id in ( " 
					+" select  nsad.nurse_shift_allocation_details_id from public.m_nurse_shift_allocation_details  nsad " 
					+" where  nsad.nurse_id =:nurseId " 
					+" and nsad.nurse_shift_allocation_id=(select nsa.nurse_shift_allocation_id  from public.m_nurse_shift_allocation nsa " 
					+" where nsa.nurse_id=:nurseId and nsa.ward_id=:wardId and nsa.unit_id =:unitId and nsa.org_id=:orgId ) "
					+" and to_char(nsad.shift_date,'DD-MM-YYYY') in :shiftDate " 
					+" and nsad.unit_id =:unitId and nsad.org_id =:orgId )")
			.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
			.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
			.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
			.setParameter("wardId", nurseShiftAllocationDto.getWardId())
			.setParameterList("shiftDate", shiftDates)
			.executeUpdate();
			
            for(NurseShiftAllocationDetailsDto nurseShiftDto : nurseShiftAllocationDto.getShiftList()) {
					//System.out.println("inside Loop");
					NurseShiftAllocationDetails nurseShiftAllocationDetails = new NurseShiftAllocationDetails();
					if(nurseShiftAllocationDto2!=null) {
						nurseShiftAllocationDetails.setNurseShiftAllocationId(nurseShiftAllocationDto2.getNurseShiftAllocationId());
					}else {
						nurseShiftAllocationDetails.setNurseShiftAllocationId(nurseShiftAllocation.getNurseShiftAllocationId());
					}
					nurseShiftAllocationDetails.setNurseId(nurseShiftAllocationDto.getNurseId());
					nurseShiftAllocationDetails.setShiftDate(GlobalCommonDateUtils.getDate(nurseShiftDto.getShiftDate(), "dd-M-yyyy HH:mm:ss"));
					nurseShiftAllocationDetails.setShiftId(nurseShiftDto.getShiftId());
					nurseShiftAllocationDetails.setUnitId(nurseShiftAllocationDto.getUnitId());
					nurseShiftAllocationDetails.setOrgId(nurseShiftAllocationDto.getOrgId());
					nurseShiftAllocationDetails.setCreatedBy(nurseShiftAllocationDto.getCreatedBy());
					nurseShiftAllocationDetails.setCreatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
					nurseShiftAllocationDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
					nurseShiftAllocationDetails.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
					nurseShiftAllocationDetails.setStatus(nurseShiftAllocationDto.getStatus());
					nurseShiftAllocationDetails.setIsDeleted('N');
					sessionFactory.getCurrentSession().save(nurseShiftAllocationDetails);
                    
					for (NurseShiftAllocationDetailsDto nursebedIdShiftDto : nurseShiftDto.getSelectedbedList()) {
						BedIdNurseShiftAllocation bedIdNurseShiftAllocation = new BedIdNurseShiftAllocation();
						bedIdNurseShiftAllocation.setBedId(nursebedIdShiftDto.getId());
						bedIdNurseShiftAllocation.setNurseId(nurseShiftAllocationDto.getNurseId());
						bedIdNurseShiftAllocation.setShiftDate(GlobalCommonDateUtils.getDate(nurseShiftDto.getShiftDate(), "dd-M-yyyy HH:mm:ss"));
						bedIdNurseShiftAllocation.setNurseShiftAllocationDetailsId(nurseShiftAllocationDetails.getNurseShiftAllocationDetailsId());
						bedIdNurseShiftAllocation.setIsBedAssigned('Y');
						bedIdNurseShiftAllocation.setIsDeleted('N');
						bedIdNurseShiftAllocation.setUnitId(nurseShiftAllocationDto.getUnitId());
						bedIdNurseShiftAllocation.setOrgId(nurseShiftAllocationDto.getOrgId());
						bedIdNurseShiftAllocation.setCreatedBy(nurseShiftAllocationDto.getCreatedBy());
						bedIdNurseShiftAllocation.setCreatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
						bedIdNurseShiftAllocation.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
						bedIdNurseShiftAllocation.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
						bedIdNurseShiftAllocation.setStatus(nurseShiftAllocationDto.getStatus());
						sessionFactory.getCurrentSession().save(bedIdNurseShiftAllocation);
					}
				}
				return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, null);
	}   catch(Exception e){
		throw e;
	}
	
	}

	@Override
	public Response updateNurseShiftAllocation(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {

			NurseShiftAllocation nurseShiftAllocation =(NurseShiftAllocation)sessionFactory.getCurrentSession().createQuery(" From NurseShiftAllocation nurseAllObj where nurseAllObj.nurseShiftAllocationId=:nurseShiftAllocationId ")
					.setParameter("nurseShiftAllocationId", nurseShiftAllocationDto.getNurseShiftAllocationId()).uniqueResult();
			nurseShiftAllocation.setNurseShiftAllocationId(nurseShiftAllocationDto.getNurseShiftAllocationId());
			nurseShiftAllocation.setWardId(nurseShiftAllocationDto.getWardId());
			nurseShiftAllocation.setNurseId(nurseShiftAllocationDto.getNurseId());
			nurseShiftAllocation.setUnitId(nurseShiftAllocationDto.getUnitId());
			nurseShiftAllocation.setOrgId(nurseShiftAllocationDto.getOrgId());
			nurseShiftAllocation.setStatus(nurseShiftAllocationDto.getStatus());
			nurseShiftAllocation.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
			nurseShiftAllocation.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
			sessionFactory.getCurrentSession().update(nurseShiftAllocation);

			for (NurseShiftAllocationDetailsDto nurseShiftDto : nurseShiftAllocationDto.getShiftList()) {
				NurseShiftAllocationDetails nurseShiftAllocationDetails =(NurseShiftAllocationDetails)sessionFactory.getCurrentSession()
						.createQuery("From NurseShiftAllocationDetails nurseAllDet where nurseAllDet.nurseShiftAllocationDetailsId=:nurseShiftAllocationDetailsId ")
						.setParameter("nurseShiftAllocationDetailsId", nurseShiftDto.getNurseShiftAllocationDetailsId()).uniqueResult();
				nurseShiftAllocationDetails.setNurseShiftAllocationDetailsId(nurseShiftDto.getNurseShiftAllocationDetailsId());
				nurseShiftAllocationDetails.setNurseShiftAllocationId(nurseShiftAllocationDto.getNurseShiftAllocationId());
				nurseShiftAllocationDetails.setNurseId(nurseShiftAllocationDto.getNurseId());
				nurseShiftAllocationDetails.setShiftDate(GlobalCommonDateUtils.getDate(nurseShiftDto.getShiftDate(), "dd-M-yyyy HH:mm:ss"));
				nurseShiftAllocationDetails.setShiftId(nurseShiftDto.getShiftId());
				nurseShiftAllocationDetails.setUnitId(nurseShiftAllocationDto.getUnitId());
				nurseShiftAllocationDetails.setOrgId(nurseShiftAllocationDto.getOrgId());
				nurseShiftAllocationDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
				nurseShiftAllocationDetails.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
				nurseShiftAllocationDetails.setStatus(nurseShiftAllocationDto.getStatus());
				nurseShiftAllocationDetails.setIsDeleted('N');
				sessionFactory.getCurrentSession().update(nurseShiftAllocationDetails);
				
				Integer count=sessionFactory.getCurrentSession().createQuery("update BedIdNurseShiftAllocation bnsa set bnsa.isDeleted='Y',bnsa.isBedAssigned='N' where bnsa.nurseId=:nurseId and bnsa.nurseShiftAllocationDetailsId=:nurseShiftAllocationDetailsId ")
						.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
						.setParameter("nurseShiftAllocationDetailsId", nurseShiftDto.getNurseShiftAllocationDetailsId()).executeUpdate();
                
				for (NurseShiftAllocationDetailsDto nursebedIdShiftDto : nurseShiftDto.getSelectedbedList()) {
					BedIdNurseShiftAllocation bedIdNurseShiftAllocation =new BedIdNurseShiftAllocation();
					bedIdNurseShiftAllocation.setBedId(nursebedIdShiftDto.getId());
					bedIdNurseShiftAllocation.setNurseId(nurseShiftAllocationDto.getNurseId());
					bedIdNurseShiftAllocation.setShiftDate(GlobalCommonDateUtils.getDate(nurseShiftDto.getShiftDate(), "dd-M-yyyy HH:mm:ss"));
					bedIdNurseShiftAllocation.setNurseShiftAllocationDetailsId(nurseShiftAllocationDetails.getNurseShiftAllocationDetailsId());
					bedIdNurseShiftAllocation.setIsBedAssigned('Y');
					bedIdNurseShiftAllocation.setIsDeleted('N');
					bedIdNurseShiftAllocation.setUnitId(nurseShiftAllocationDto.getUnitId());
					bedIdNurseShiftAllocation.setOrgId(nurseShiftAllocationDto.getOrgId());
					//bedIdNurseShiftAllocation.setCreatedBy(nurseShiftAllocationDto.getCreatedBy());
					//bedIdNurseShiftAllocation.setCreatedDate(TimeZoneComponent.getDateByzone(unitId));
					bedIdNurseShiftAllocation.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
					bedIdNurseShiftAllocation.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
					bedIdNurseShiftAllocation.setStatus(nurseShiftAllocationDto.getStatus());
					sessionFactory.getCurrentSession().save(bedIdNurseShiftAllocation);
				}
			}

			return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response activeInactiveNurseShiftAllocation(Integer orgId, Integer unitId,
			Integer nurseShiftAllocationDetailsId, Character shiftStatus) throws ApplicationException {
		try {
			NurseShiftAllocationDetails nurseShiftAllocationDetails = findById(nurseShiftAllocationDetailsId);
			if (nurseShiftAllocationDetails.getNurseShiftAllocationDetailsId() != 0) {
				nurseShiftAllocationDetails.setStatus(shiftStatus);
				NurseShiftAllocationDetails NurseShifDetails = update(nurseShiftAllocationDetails);
				if (NurseShifDetails.getStatus() == ACTIVE) {
					return new Response(SUCCESS, null, null, null, null);
				} else if (NurseShifDetails.getStatus() == INACTIVE) {
					return new Response(SUCCESS, null, null, null, null);
				} else {
					if (shiftStatus == ACTIVE) {
						return new Response(SUCCESS, null, null, null, null);
					} else {
						return new Response(SUCCESS, null, null, null, null);
					}
				}
			} else {
				return new Response(SUCCESS, null, null, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, null, null, null);
		}
	}

	@Override
	public Response getNurseShiftDetailsById(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			List<NurseShiftAllocationDto> NurseAllocationDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSE_SHIFT_DETAILS_BY_ID")
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setParameter("wardId", nurseShiftAllocationDto.getWardId())
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, NurseAllocationDto, null);

		} catch (HibernateException he) {
		logger.error("getNurseShiftDetailsById failed", he);
		} catch (Exception e) {
		logger.error("getNurseShiftDetailsById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveNurseAllocationList(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			List<NurseShiftAllocationDto> nurseList = sessionFactory.getCurrentSession()
					.createSQLQuery("select nsad.nurse_shift_allocation_id as \"nurseShiftAllocationId\",nsad.nurse_id  as \"nurseId\",CONCAT  (em.first_name, ' ', em.middle_name, ' ',em.last_name)  as \"nurseName\", " 
			                + "nsad.ward_id as \"wardId\", wm.ward_name as \"wardName\",nsad.status as\"status\" "
							+" FROM public.m_nurse_shift_allocation nsad" 
							+" join public.m_employee_master em on nsad.nurse_id=em. user_id"
							+" join adt.m_ward_master wm on nsad.ward_id=wm.ward_id"
							+" where nsad.org_id=:orgId and nsad.unit_id=:unitId and nsad.status='A' and nsad.nurse_id=:nurseId ")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			
			List<NurseShiftAllocationDto> shiftList = sessionFactory.getCurrentSession()
					.createSQLQuery("select nsad.nurse_shift_allocation_details_id as \"nurseShiftAllocationDetailsId\", nsad.shift_id as \"shiftId\",sm.shift_name as \"shiftName\",to_char(nsad.shift_date, 'DD-MM-YYYY') as \"shiftDate\",nsad.nurse_id  as \"nurseId\" " 
							+" FROM public.m_nurse_shift_allocation_details nsad " 
							+" join public.m_shift_master sm on  nsad.shift_id = sm.shift_id"
							+ " where nsad.org_id=:orgId and nsad.unit_id=:unitId and nsad.status='A' and nsad.nurse_id=:nurseId"
							+ " AND (to_char(nsad.shift_date, 'YYYY-MM-DD') between :formDate AND :toDate )")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setParameter("formDate", nurseShiftAllocationDto.getFromDate())
					.setParameter("toDate", nurseShiftAllocationDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			
			List<NurseShiftAllocationDto> nurseBedList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT binsa.bed_nurse_shift_allocation_id as \"bedNurseShiftAllocationId\",  binsa.nurse_shift_allocation_details_id as \"nurseShiftAllocationDetailsId\"," 
							+" binsa.bed_id as \"bedId\",bm.bed_number as \"bedName\",binsa.nurse_id as \"nurseId\" " 
							+" FROM public.m_bed_id_nurse_shift_allocation binsa"  
							+" join  adt.m_bed_master bm on binsa.bed_id= bm.bed_id"
							+ " where binsa.org_id=:orgId and binsa.unit_id=:unitId and binsa.is_deleted='N' and binsa.status='A' and binsa.nurse_id=:nurseId")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			
			NurseShiftAllocationDto nurseShiftAllocationDtoFinal=new NurseShiftAllocationDto();
			nurseShiftAllocationDtoFinal.setNurseList(nurseList);
			nurseShiftAllocationDtoFinal.setNurseShiftList(shiftList);
			nurseShiftAllocationDtoFinal.setNurseBedList(nurseBedList);

		return new Response(SUCCESS, null, null,null, nurseShiftAllocationDtoFinal);

		} catch (HibernateException he) {
		logger.error("getActiveNurseAllocationList failed", he);
		} catch (Exception e) {
		logger.error("getActiveNurseAllocationList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response listNurseAllocation(NurseShiftAllocationDto nurseShiftAllocationDto) throws ApplicationException {
		try {
			List<NurseShiftAllocationDto> nurseList = sessionFactory.getCurrentSession()
					.createSQLQuery("select nsad.nurse_shift_allocation_id as \"nurseShiftAllocationId\",nsad.nurse_id  as \"nurseId\",CONCAT  (em.first_name, ' ', em.middle_name, ' ',em.last_name)  as \"nurseName\", " 
			                + "nsad.ward_id as \"wardId\", wm.ward_name as \"wardName\",nsad.status as\"status\" "
							+" FROM public.m_nurse_shift_allocation nsad" 
							+" join public.m_employee_master em on nsad.nurse_id=em. user_id"
							+" join adt.m_ward_master wm on nsad.ward_id=wm.ward_id"
							+" where nsad.org_id=:orgId and nsad.unit_id=:unitId  ")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			
			List<NurseShiftAllocationDto> shiftList = sessionFactory.getCurrentSession()
					.createSQLQuery("select nsad.nurse_shift_allocation_details_id as \"nurseShiftAllocationDetailsId\", nsad.shift_id as \"shiftId\",sm.shift_name as \"shiftName\",to_char(nsad.shift_date, 'DD-MM-YYYY') as \"shiftDate\",nsad.nurse_id  as \"nurseId\" " 
							+" FROM public.m_nurse_shift_allocation_details nsad " 
							+" join public.m_shift_master sm on  nsad.shift_id = sm.shift_id"
							+ " where nsad.org_id=:orgId and nsad.unit_id=:unitId"
							+ " AND (to_char(nsad.shift_date, 'YYYY-MM-DD') between :formDate AND :toDate )"
							+ " AND nsad.is_deleted='N' "
							+ " order by \"shiftDate\" ")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("formDate", nurseShiftAllocationDto.getFromDate())
					.setParameter("toDate", nurseShiftAllocationDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			
			List<NurseShiftAllocationDto> nurseBedList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT binsa.bed_nurse_shift_allocation_id as \"bedNurseShiftAllocationId\",  binsa.nurse_shift_allocation_details_id as \"nurseShiftAllocationDetailsId\"," 
							+" binsa.bed_id as \"bedId\",bm.bed_number as \"bedName\",binsa.nurse_id as \"nurseId\" " 
							+" FROM public.m_bed_id_nurse_shift_allocation binsa"  
							+" join  adt.m_bed_master bm on binsa.bed_id= bm.bed_id"
							+ " where binsa.org_id=:orgId and binsa.unit_id=:unitId and binsa.is_deleted='N' ")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			
			NurseShiftAllocationDto nurseShiftAllocationDtoFinal=new NurseShiftAllocationDto();
			nurseShiftAllocationDtoFinal.setNurseList(nurseList);
			nurseShiftAllocationDtoFinal.setNurseShiftList(shiftList);
			nurseShiftAllocationDtoFinal.setNurseBedList(nurseBedList);

				return new Response(SUCCESS, null, null,null, nurseShiftAllocationDtoFinal);
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getNurseShiftByDetailsId(NurseShiftAllocationDto nurseShiftAllocationDto)
			throws ApplicationException {
		try {
			NurseShiftAllocationDto NurseAllocationDto = (NurseShiftAllocationDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSE_ALLOACTION_BY_DETAILS_ID")
					.setParameter("nurseShiftAllocationDetailsId",
							nurseShiftAllocationDto.getShiftList().getFirst().getNurseShiftAllocationDetailsId())
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())

					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, NurseAllocationDto);

		} catch (HibernateException he) {
		logger.error("getNurseShiftByDetailsId failed", he);
		} catch (Exception e) {
		logger.error("getNurseShiftByDetailsId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getNurseShiftWithBedAllocated(NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto)
			throws ApplicationException {
		try {
			List<NurseShiftAllocationDto> NurseAllocationDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECK_NURSE_ALLOACTION_BED_BY_SHIFT_ID")
					.setParameter("shiftId", nurseShiftAllocationDetailsDto.getShiftId())
					.setParameter("orgId", nurseShiftAllocationDetailsDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, NurseAllocationDto, null);

		} catch (HibernateException he) {
		logger.error("getNurseShiftWithBedAllocated failed", he);
		} catch (Exception e) {
		logger.error("getNurseShiftWithBedAllocated failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getTotalRecordNurseShiftAllocation(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			BigInteger totalRecordNurseShiftAllocation = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_NURSE_ALLOACTION")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.uniqueResult();

			if (totalRecordNurseShiftAllocation.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, null, null, null, totalRecordNurseShiftAllocation);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}
		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response checkbedIdbyshiftdate(NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto)
			throws ApplicationException {
		try {
		    List<NurseShiftAllocationDto> nurseAllocationDtolist=  sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT  nsad.shift_id as \"shiftId\",nsad.nurse_shift_allocation_details_id as \"nurseShiftAllocationDetailsId\" "
								+ " FROM public.m_nurse_shift_allocation_details nsad "
								+ " where to_char(nsad.shift_date, 'DD-MM-YYYY')=:shiftDate "
								+ " and nsad.shift_id=:shiftId "
								+ " and nsad.org_id =:orgId and nsad.unit_id=:unitId and nsad.is_deleted='N' ")
					.setParameter("shiftDate", nurseShiftAllocationDetailsDto.getShiftDate())
					.setParameter("shiftId", nurseShiftAllocationDetailsDto.getShiftId())
					.setParameter("orgId", nurseShiftAllocationDetailsDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
		    List<NurseShiftAllocationDto> finalBedlist=new ArrayList<NurseShiftAllocationDto>();
		    if(nurseAllocationDtolist.size()>0){
		    	for(NurseShiftAllocationDto nurseShiftAllocationDto:nurseAllocationDtolist){
		    		List<NurseShiftAllocationDto> bedlist=new ArrayList<NurseShiftAllocationDto>();
		    	 bedlist=sessionFactory.getCurrentSession().createSQLQuery("select bed_id as \"bedId\" "
		    	 				+ " from m_bed_id_nurse_shift_allocation where nurse_shift_allocation_details_id=:nurseShiftALlDetailId and org_id=:orgId and unit_id=:unitId and is_deleted='N' and status='A' ")
			    			 .setParameter("nurseShiftALlDetailId", nurseShiftAllocationDto.getNurseShiftAllocationDetailsId())  
			    			 .setParameter("orgId", nurseShiftAllocationDetailsDto.getOrgId())
							.setParameter("unitId", nurseShiftAllocationDetailsDto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
		    	 finalBedlist.addAll(bedlist);
		    	}
		    }
		   
			return new Response(SUCCESS, null, null, finalBedlist, null);

		} catch (HibernateException he) {
		logger.error("checkbedIdbyshiftdate failed", he);
		} catch (Exception e) {
		logger.error("checkbedIdbyshiftdate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getbedlistbywardId(NurseShiftAllocationDto nurseShiftAllocationDto) throws ApplicationException {
		try {
			List<NurseShiftAllocationDto> bedList = sessionFactory.getCurrentSession()
					.createSQLQuery("select bed_id as \"bedId\", bed_number as \"bedName\""  
							+" FROM adt.m_bed_master " 
							+" where organization_id=:orgId and unit_id=:unitId and ward_id=:wardId and status='A'"
							+ " ORDER BY bed_number ASC ")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("wardId", nurseShiftAllocationDto.getWardId())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, bedList, null);

		} catch (HibernateException he) {
		logger.error("getbedlistbywardId failed", he);
		} catch (Exception e) {
		logger.error("getbedlistbywardId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getbedlistbyNurseId(NurseShiftAllocationDto nurseShiftAllocationDto) throws ApplicationException {
		try {
			List<NurseShiftAllocationDto> bedListByNurse = sessionFactory.getCurrentSession()
					.createSQLQuery("select binsa.bed_id as \"bedId\", bm.bed_number as \"bedName\",nallmast.ward_id as \"wardId\" "
							+ "FROM public.m_bed_id_nurse_shift_allocation binsa "
							+ "inner join  adt.m_bed_master bm on binsa.bed_id= bm.bed_id "
							+ "inner join public.m_nurse_shift_allocation_details nalldet on nalldet.nurse_shift_allocation_details_id=binsa.nurse_shift_allocation_details_id "
							+ "  inner join public.m_nurse_shift_allocation nallmast on nallmast.nurse_shift_allocation_id=nalldet.nurse_shift_allocation_id "
							+ " where binsa.org_id=:orgId and binsa.unit_id=:unitId and  binsa.nurse_id=:nurseId and binsa.is_deleted='N' and binsa.status='A' and to_char(binsa.shift_date, 'DD-MM-YYYY')=:shiftDate")
					.setParameter("orgId", nurseShiftAllocationDto.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto.getUnitId())
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setParameter("shiftDate", nurseShiftAllocationDto.getShiftDate())
					.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, bedListByNurse, null);

		} catch (HibernateException he) {
		logger.error("getbedlistbyNurseId failed", he);
		} catch (Exception e) {
		logger.error("getbedlistbyNurseId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateNurseShiftAllocationDashboardStatus(NurseShiftAllocationDto nurseShiftAllocationDto) {
		try{
		NurseShiftAllocation nurseShiftAllocation =(NurseShiftAllocation)sessionFactory.getCurrentSession().createQuery(" From NurseShiftAllocation nurseAllObj where nurseAllObj.nurseShiftAllocationId=:nurseShiftAllocationId ")
				.setParameter("nurseShiftAllocationId", nurseShiftAllocationDto.getNurseShiftAllocationId()).uniqueResult();
		nurseShiftAllocation.setNurseShiftAllocationId(nurseShiftAllocationDto.getNurseShiftAllocationId());
		nurseShiftAllocation.setUnitId(nurseShiftAllocationDto.getUnitId());
		nurseShiftAllocation.setOrgId(nurseShiftAllocationDto.getOrgId());
		nurseShiftAllocation.setStatus(nurseShiftAllocationDto.getStatus());
		nurseShiftAllocation.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
		nurseShiftAllocation.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
		sessionFactory.getCurrentSession().update(nurseShiftAllocation);

		for (NurseShiftAllocationDto nurseShiftDto : nurseShiftAllocationDto.getNurseShiftList()) {
			NurseShiftAllocationDetails nurseShiftAllocationDetails =(NurseShiftAllocationDetails)sessionFactory.getCurrentSession()
					.createQuery("From NurseShiftAllocationDetails nurseAllDet where nurseAllDet.nurseShiftAllocationDetailsId=:nurseShiftAllocationDetailsId ")
					.setParameter("nurseShiftAllocationDetailsId", nurseShiftDto.getNurseShiftAllocationDetailsId()).uniqueResult();
			nurseShiftAllocationDetails.setNurseShiftAllocationDetailsId(nurseShiftDto.getNurseShiftAllocationDetailsId());
			nurseShiftAllocationDetails.setUnitId(nurseShiftAllocationDto.getUnitId());
			nurseShiftAllocationDetails.setOrgId(nurseShiftAllocationDto.getOrgId());
			nurseShiftAllocationDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()));
			nurseShiftAllocationDetails.setUpdatedBy(nurseShiftAllocationDto.getUpdatedBy());
			nurseShiftAllocationDetails.setStatus(nurseShiftAllocationDto.getStatus());
			sessionFactory.getCurrentSession().update(nurseShiftAllocationDetails);
			
			Integer count=sessionFactory.getCurrentSession().createQuery("update BedIdNurseShiftAllocation bnsa set bnsa.status=:status,bnsa.updatedBy=:updatedBy,bnsa.updatedDate=:updatedDate where bnsa.nurseId=:nurseId and bnsa.nurseShiftAllocationDetailsId=:nurseShiftAllocationDetailsId ")
					.setCharacter("status", nurseShiftAllocationDto.getStatus())
					.setParameter("updatedBy", nurseShiftAllocationDto.getUpdatedBy())
					.setDate("updatedDate", TimeZoneComponent.getDateByzone(nurseShiftAllocationDto.getUnitId()))
					.setParameter("nurseId", nurseShiftAllocationDto.getNurseId())
					.setParameter("nurseShiftAllocationDetailsId", nurseShiftDto.getNurseShiftAllocationDetailsId()).executeUpdate();
		}
		return new Response(SUCCESS, null, "Status Updated", null, null);

	} catch (HibernateException he) {
	logger.error("updateNurseShiftAllocationDashboardStatus failed", he);
	} catch (Exception e) {
	logger.error("updateNurseShiftAllocationDashboardStatus failed", e);
	}
	return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAssignedBedList(NurseShiftAllocationDto nurseShiftAllocationDto1) {
		try {
			 List<NurseShiftAllocationDto> finalBedlist=new ArrayList<NurseShiftAllocationDto>();
			
		 for(NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto:nurseShiftAllocationDto1.getShiftList()) {
		    List<Integer> nurseShiftAllocationDetailsIdList=  sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT  nsad.nurse_shift_allocation_details_id  "
								+ " FROM public.m_nurse_shift_allocation_details nsad "
								+ " where to_char(nsad.shift_date, 'DD-MM-YYYY')=:shiftDate "
								+ " and nsad.shift_id=:shiftId "
								+ " and nsad.org_id =:orgId and nsad.unit_id=:unitId and nsad.is_deleted='N' "
								+ " and nsad.nurse_id!=:nurseId ")
					.setParameter("shiftDate", nurseShiftAllocationDetailsDto.getShiftDate())
					.setParameter("shiftId", nurseShiftAllocationDetailsDto.getShiftId())
					.setParameter("orgId", nurseShiftAllocationDto1.getOrgId())
					.setParameter("unitId", nurseShiftAllocationDto1.getUnitId())
					.setParameter("nurseId", nurseShiftAllocationDto1.getNurseId())
					//.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class))
					.list();
		    if(nurseShiftAllocationDetailsIdList.size()>0){
		    		List<NurseShiftAllocationDto> bedlist=new ArrayList<NurseShiftAllocationDto>();
		    	    bedlist=sessionFactory.getCurrentSession().createSQLQuery("select bnsa.bed_id as \"bedId\","
		    	    		+ " bnsa.nurse_shift_allocation_details_id as \"nurseShiftAllocationDetailsId\","
		    	    		+ "  to_char(bnsa.shift_date, 'DD-MM-YYYY') as \"shiftDate\" "
	    	 				+ " from m_bed_id_nurse_shift_allocation bnsa"
	    	 				+ " where bnsa.nurse_shift_allocation_details_id in :nurseAlloShiftDetId "
	    	 				+ " and bnsa.org_id=:orgId and bnsa.unit_id=:unitId and bnsa.is_deleted='N' and bnsa.status='A' ")
			    			.setParameter("orgId", nurseShiftAllocationDto1.getOrgId())
							.setParameter("unitId", nurseShiftAllocationDto1.getUnitId())
							.setParameterList("nurseAlloShiftDetId", nurseShiftAllocationDetailsIdList)
							.setResultTransformer(Transformers.aliasToBean(NurseShiftAllocationDto.class)).list();
		    	 finalBedlist.addAll(bedlist);
		    }
		 }
		return new Response(SUCCESS, null, null, finalBedlist, null);

		} catch (HibernateException he) {
		logger.error("getAssignedBedList failed", he);
		} catch (Exception e) {
		logger.error("getAssignedBedList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	
	}

}
