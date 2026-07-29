package com.param.billing.unit.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.model.DoctorSlabMaster;
import com.param.billing.unit.dto.DoctorSlabDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class DoctorSlabMasterDaoImpl extends GenericDao<DoctorSlabMaster, Integer> implements IDoctorSlabMasterDao,ICommonConstants, IError{

	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public DoctorSlabMasterDaoImpl() {
		super(DoctorSlabMaster.class);
	}

	@Override
	public Response doctorSlabSave(DoctorSlabDto dto) throws ApplicationException {
		try{
			DoctorSlabMaster master = new DoctorSlabMaster();
				master.setDoctorId(dto.getDoctorId());
				master.setSlabFrom(dto.getSlabFrom());
				master.setSlabTo(dto.getSlabTo());
				master.setPercentage(dto.getPercentage());
			//	master.setAmount(dto.getAmount() != null ? dto.getAmount() : null);
				master.setOrganizationId(dto.getOrganizationId());
				master.setUnitId(dto.getUnitId());
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedBy(dto.getUpdatedBy());
			
				if(dto.getDoctorSlabMasterId() != null) {
					master.setDoctorSlabMasterId(dto.getDoctorSlabMasterId());
					master.setStatus(dto.getStatus());
					master = update(master);
				}else {
					master.setStatus('A');
					master = save(master);
				}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getdoctorSlabList(DoctorSlabDto doctorSlabDto) throws ApplicationException {
		try
        {
			List<DoctorSlabDto> list = sessionFactory.getCurrentSession().createSQLQuery(
				"select distinct mdsm.doctor_id  as \"doctorId\", mdsm.status as \"status\"," + 
				"concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\" " + 
				"from billing.m_doctor_slab_master mdsm " + 
				"inner join doctor.m_doctor_master doc  on doc.doctor_id  = mdsm .doctor_id " + 
				"where  mdsm.organization_id  =:orgId and mdsm.unit_id =:unitId ")
				.setParameter("orgId", doctorSlabDto.getOrganizationId())
				.setParameter("unitId", doctorSlabDto.getUnitId())
				.setFirstResult(doctorSlabDto.getOffset() != null ? doctorSlabDto.getOffset() : 0)
				.setMaxResults(doctorSlabDto.getNoOfRecordsPerPage() != null ? doctorSlabDto.getNoOfRecordsPerPage() : 10)	
				.setResultTransformer(Transformers.aliasToBean(DoctorSlabDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getdoctorSlabListByDoctorId(DoctorSlabDto doctorSlabDto) throws ApplicationException {
		try
        {
			List<DoctorSlabDto> list = sessionFactory.getCurrentSession().createSQLQuery(
				"select  "
				+	"	mdsm.doctor_id  as \"doctorId\"," 
				+ 	"	mdsm.doctor_slab_master_id  as \"doctorSlabMasterId\"," 
				+ 	"	mdsm.percentage as \"percentage\"," 
				+ 	"	mdsm.slab_from as \"slabFrom\"," 
				+ 	"	mdsm.slab_to as \"slabTo\"," 
				+ 	"	mdsm.status as\"status\"," 
				+ 	"	mdsm.amount as \"amount\" " 
				+ 	" from billing.m_doctor_slab_master mdsm " 
				+ 	" where "
				+ 	"	mdsm.status = 'A' " 
				+	" 	and mdsm.doctor_id =:doctorId "
				+ 	" 	and mdsm.unit_id =:unitId "
				+ 	" order by slab_from ")
					.setParameter("doctorId", doctorSlabDto.getDoctorId())
					.setParameter("unitId", doctorSlabDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(DoctorSlabDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateSlabStatusByDoctorId(DoctorSlabDto doctorSlabDto) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			// BR-followup: this update previously filtered on doctor_id only, so
			// toggling one doctor's slab status in one unit silently flipped that
			// same doctor's status in EVERY unit/organization they have slab rows
			// in -- doctorSlabSave (above) scopes rows by unit_id/organization_id
			// on create, so the update must scope by the same two columns to match.
			Integer count=session.createSQLQuery(" update billing.m_doctor_slab_master set status=:status "
					+ " where doctor_id=:doctorId and unit_id=:unitId and organization_id=:organizationId ")
					.setParameter("doctorId", doctorSlabDto.getDoctorId())
					.setParameter("status", doctorSlabDto.getStatus())
					.setParameter("unitId", doctorSlabDto.getUnitId())
					.setParameter("organizationId", doctorSlabDto.getOrganizationId())
					.executeUpdate();
			
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
