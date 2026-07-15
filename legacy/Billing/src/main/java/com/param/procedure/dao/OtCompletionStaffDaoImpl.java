package com.param.procedure.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.procedure.dto.BillOfMaterialDto;
import com.param.otims.model.OtCompletionStaff;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtCompletionStaffDaoImpl extends GenericDao<OtCompletionStaff, Integer> implements IOtCompletionStaffDao, ICommonConstants{

	public OtCompletionStaffDaoImpl() {
		super(OtCompletionStaff.class);
	}

	@Override
	public Response saveOtCompletionStaff(BillOfMaterialDto dto) throws ApplicationException {
	try{
			
			if(dto.getOtCompletionStaffId()!=null) {
				OtCompletionStaff master = findById(dto.getOtCompletionStaffId());
					if(dto.getStatus()=='I') {
						master.setStatus('I');
					}else {
						master.setStatus('A');
				}
				master = update(master);
				
			}else {
			
				OtCompletionStaff master = new OtCompletionStaff();
				master.setProcedureOrderId(dto.getProcedureId()!=null? dto.getProcedureId(): null);
				master.setServiceId(dto.getServiceId());
				master.setDoctorId(dto.getDoctorId());
				master.setSpecilityId(dto.getSpecilityId());
				master.setAmount(dto.getAmount());
				master.setStatus('A');
				master.setUnitId(dto.getUnitId());
				master.setOrgId(dto.getOrganizationId());
				master.setCreatedBy(dto.getCreatedBy());
				master.setProcReqId(dto.getProcReqId());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setIsEditable(dto.getIsEditable()!=null?dto.getIsEditable():false);
					
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response getOtCompletionStaffByProReqId(int id, Integer orgId, Integer unitId, Integer billingBedCategoryId) throws ApplicationException {
		try{
			List<BillOfMaterialDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_COMPLETION_STAFF_LIST")
					.setParameter("id", id)
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
//					.setInteger("billingBedCategoryId", billingBedCategoryId)
					.setResultTransformer(Transformers.aliasToBean(BillOfMaterialDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getDoctorList(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_LIST_BY_SPECIALITY")
					.setParameter("organizationId", billOfMaterialDto.getOrganizationId())
					.setParameter("unitId", billOfMaterialDto.getUnitId())
					.setParameter("specilaityId", billOfMaterialDto.getSpecilityId())
					.setResultTransformer(Transformers.aliasToBean(DoctorSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	} 

}
