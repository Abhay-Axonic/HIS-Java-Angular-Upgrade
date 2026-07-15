package com.param.adt.admission.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.VisitorAgainstPatient;
import com.param.adt.admission.model.VisitorPatientMapper;
import com.param.adt.common.IADTConstants;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.global.VisitorDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PassIssuedDaoImpl extends GenericDao<VisitorAgainstPatient, Integer> implements IPassIssuedDao,ICommonConstants,IADTConstants
{

	public PassIssuedDaoImpl() {
		super(VisitorAgainstPatient.class);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response getVisitorTypeList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISITOR_TYPE_LIST")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveVisitorAgainsPatient(VisitorDto visitorDto) throws ApplicationException {
		try {
			VisitorAgainstPatient visitorAgainstPatient = mapper.map(visitorDto, VisitorAgainstPatient.class,
					"VisitorDto_to_VisitorAgainstPatient");
		
			
			sessionFactory.getCurrentSession().save(visitorAgainstPatient);
			return new Response(SUCCESS, null, null, null, visitorAgainstPatient);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response saveVisitorPatientMapper(VisitorDto visitorDto) throws ApplicationException {
		try {
				VisitorPatientMapper visitorPatientMapper = mapper.map(visitorDto, VisitorPatientMapper.class,
						"VisitorDto_to_VisitorPatientMapper");
				visitorPatientMapper.setCreatedDate(TimeZoneComponent.getDateByzone(0));
				visitorPatientMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
				sessionFactory.getCurrentSession().save(visitorPatientMapper);
				
			return new Response(SUCCESS, null, VISTORS_SAVED, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVisitorsList(VisitorDto visitorDto) throws ApplicationException {
		try {
			List<VisitorDto> visitorDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_VISITORS_LIST")
					.setParameter("organizationId", visitorDto.getOrganizationId())
					.setParameter("unitId", visitorDto.getUnitId())
					.setParameter("admissionId", visitorDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(VisitorDto.class)).list();
			return new Response(SUCCESS, null, null, visitorDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateVisitorsDetails(VisitorPatientMapper visitorPatientMapper, int visitorPatientId,
			Integer visitorPatientMapperId) throws ApplicationException 
	{
		try {
			Session session = sessionFactory.getCurrentSession();
			
			//<-----
			
			/* If the Visitor pass status is A(Accept) or L(Lost) then the status from VisitorAgainstPatient will be I(Inactive) 
			 But status will remain A(active) if we extend the visitors pass */   
			if(visitorPatientMapper.getVisitorPassStatus()=='A'||visitorPatientMapper.getVisitorPassStatus()=='L')
			{
				Query q = session.createQuery("UPDATE VisitorAgainstPatient vap SET "
						+ "vap.status = 'I', "
						+ "vap.updatedBy = :updatedBy, "
						+ "vap.updatedDate = :updatedDate "
						+ "WHERE vap.visitorPatientId = :visitorPatientId");
				q.setParameter("updatedBy", visitorPatientMapper.getUpdatedBy());
				q.setParameter("updatedDate", visitorPatientMapper.getUpdatedDate());
				q.setParameter("visitorPatientId", visitorPatientId);
				q.executeUpdate();
			}
			
			//----->
			
			//<-----
				// Making previous entry inactive 
				Query q=session.createQuery("update VisitorPatientMapper vpm set"
						+"vpm.status='I', "
						+"vpm.updatedBy=:updatedBy, "
						+"vpm.updatedDate=:updatedDate, "
						+"where vpm.visitorPatientMapperId=:visitorPatientId ");
			q.setParameter("updatedBy", visitorPatientMapper.getUpdatedBy());
			q.setParameter("updatedDate", visitorPatientMapper.getUpdatedDate());
			q.setParameter("visitorPatientId", visitorPatientId);
				q.executeUpdate();
			//----->
			
			
			
			//<-----
				//Adding new entry in mapper 
				session.save(visitorPatientMapper);
			//----->
			
//			session.close();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getBedListByWardId(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			List<BedMasterDto> bedMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_BY_WARD_ID")
					.setParameter("organizationId", bedMasterDto.getOrganizationId())
					.setParameter("unitId", bedMasterDto.getUnitId())
					.setParameter("wardId", bedMasterDto.getWardId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListByMultipleCriteria(AdmissionDto admissionDto) throws ApplicationException {
		try {

			Query queryString = sessionFactory.getCurrentSession().getNamedQuery("GET_VISITOR_PATIENT_LIST");
			String arr[] = queryString.getQueryString().split(" union ");
			
			StringBuilder stringQuery = new StringBuilder(arr[0]);
			if (admissionDto.getFloorId() != null && admissionDto.getFloorId() > 0) {
				stringQuery.append(" AND bm.floor_id = :floorId");
			}
			if (admissionDto.getWardId()!=null && admissionDto.getWardId() > 0) {
				stringQuery.append(" AND bm.ward_id = :wardId");
			}
			if (admissionDto.getBedId()!=null && admissionDto.getBedId() > 0) {
				stringQuery.append(" AND bm.bed_id = :bedId");
			}

			if(arr.length>1 ) {
				stringQuery.append(" UNION ");
				stringQuery.append(arr[1]);
				if (admissionDto.getFloorId() != null && admissionDto.getFloorId() > 0) {
					stringQuery.append(" AND bm.floor_id = :floorId");
				}
				if (admissionDto.getWardId() != null && admissionDto.getWardId() > 0) {
					stringQuery.append(" AND bm.ward_id = :wardId");
				}
				if (admissionDto.getBedId() != null && admissionDto.getBedId() > 0) {
					stringQuery.append(" AND bm.bed_id = :bedId");
				}
			}
			stringQuery.append(" order by \"admissionId\" desc");
			Query newQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

			Query sql = newQuery.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("organizationId", admissionDto.getOrganizationId());
			if (admissionDto.getFloorId() != null && admissionDto.getFloorId() > 0) {
				sql.setParameter("floorId", admissionDto.getFloorId());
			}
			if (admissionDto.getWardId()!=null && admissionDto.getWardId() > 0) {
				sql.setParameter("wardId", admissionDto.getWardId());
			}
			if (admissionDto.getBedId()!=null && admissionDto.getBedId() > 0) {
				sql.setParameter("bedId", admissionDto.getBedId());
			}
			if(arr.length>1 ) {
				if (admissionDto.getFloorId() != null && admissionDto.getFloorId() > 0) {
					sql.setParameter("floorId", admissionDto.getFloorId());
				}
				if (admissionDto.getWardId() != null && admissionDto.getWardId() > 0) {
					sql.setParameter("wardId", admissionDto.getWardId());
				}
				if (admissionDto.getBedId() != null && admissionDto.getBedId() > 0) {
					sql.setParameter("bedId", admissionDto.getBedId());
				}
			}
			List<AdmissionDto> admissionDtosList = sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			return new Response(SUCCESS, null, COMMON_ERROR_MESSAGE, admissionDtosList, null);
			
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	private StringBuilder searchPatient(AdmissionDto admissionDto, String stringQuery) {
		int floor = admissionDto.getFloorId();
		int ward = admissionDto.getWardId();
		int bed =admissionDto.getBedId();

		StringBuilder ANDFloor = new StringBuilder(" AND bm.floorId=" + floor);
		StringBuilder ANDWard = new StringBuilder(" AND bm.wardId=" + ward);
		StringBuilder ANDBed = new StringBuilder(" AND bm.bedId=" + bed);
		

		StringBuilder query = new StringBuilder(stringQuery);
		
		if(floor>0 && ward==0 && bed==0)
		{
			query.append(ANDFloor);
		}
		else if(floor>0 && ward>0 && bed==0)
		{
			query.append(ANDFloor).append(ANDWard);
		}
		else if(floor>0 && ward>0 && bed>0)
		{
			query.append(ANDFloor).append(ANDWard).append(ANDBed);
		}
		return query;
	}

	@Override
	public Response getPassNumberAlreadyIssueOrNot(VisitorDto visitorDto) throws ApplicationException {
		try{
			Long passNumberCount = (Long)sessionFactory.getCurrentSession().getNamedQuery("GET_PASSNUMBER_ALREADY_ISSUE_OR_NOT")
					.setParameter("organizationId", visitorDto.getOrganizationId())
					.setParameter("unitId", visitorDto.getUnitId())
					.setParameter("passNumber", visitorDto.getPassNumber().trim())
					.uniqueResult();
			return new Response(SUCCESS, null, null, null, passNumberCount);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkNoOfPassIssueAgainstAdmission(VisitorDto visitorDto) throws ApplicationException {
		try{
			List<VisitorDto> visitorDtosList = sessionFactory.getCurrentSession().getNamedQuery("CHECK_NO_OF_PASS_ISSUE_AGAINST_ADMISSION")
					.setParameter("organizationId", visitorDto.getOrganizationId())
					.setParameter("unitId", visitorDto.getUnitId())
					.setParameter("admissionId", visitorDto.getAdmissionId())
					.setParameter("bedCategoryId", visitorDto.getBedCategoryId())
					.setParameter("visitTypeId", visitorDto.getVisitTypeId())
					.setParameter("visitorPassTypeId", visitorDto.getVisitorPassTypeId())
					.setResultTransformer(Transformers.aliasToBean(VisitorDto.class)).list();
			return new Response(SUCCESS, null, null, visitorDtosList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVisitorPassList(AdmissionDto admissionDto) throws ApplicationException {
		try{
			Integer count=0;
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISITOR_PATIENT_LIST")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset()  : 0)
					.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
			List<AdmissionDto> listCount = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISITOR_PATIENT_LIST")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
			if(listCount != null && listCount.size()>0) {
				count= listCount.size();
				}
			
			return new Response(SUCCESS, null, null, admissionDtosList, count);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
}
