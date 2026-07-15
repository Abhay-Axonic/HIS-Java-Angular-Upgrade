package com.param.global.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.CreditNoteDetails;
import com.param.billing.packages.model.PackageBBCategoryMppr;
import com.param.billing.packages.model.TPackageConsumptionMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.MPackageMasterDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.PackageBBCategoryMpprDto;
import com.param.global.dto.PackageConsumptionMasterResDto;
import com.param.global.dto.PackageConsumptionServiceDetailDto;
import com.param.global.dto.TPackageConsumptionMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
@NamedNativeQueries({
	@NamedNativeQuery(name = "CANCEL_PACKAGE_CONSUMPTION",
					 query = " update billing.t_package_consumption_master "
					 		+" set status = 'I', cancelled_reason_id=:cancelReasonId, cancelled_remark=:cancelledRemark, cancelled_date=now() "
					 		+" where package_id=:packageId and patient_id =:patientId and (encounter_id=:encounterId and visit_type=:visitTypeId) or (admission_id=:encounterId and visit_type=:visitTypeId) ")
})
@Repository
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class TPackageConsumptionMasterDaoImpl extends GenericDao<TPackageConsumptionMaster, Integer> implements ITPackageConsumptionMasterDao, ICommonConstants{

	public TPackageConsumptionMasterDaoImpl() {
		super(TPackageConsumptionMaster.class);
	}
	
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    IMPackageMasterDao iMPackageMasterDao;
    
	@Override
	public Response saveTPackageConsumptionMaster(TPackageConsumptionMasterDto dto) throws BillingException {
		try{
			TPackageConsumptionMaster master = new TPackageConsumptionMaster();
			if(dto.getPackageConsumptionMasterId()!=null && dto.getPackageConsumptionMasterId()>0)
			{
				Integer packageConsumptionMasterId=dto.getPackageConsumptionMasterId();
				Integer resultCount=0;
				resultCount = sessionFactory.getCurrentSession().createSQLQuery("update "
						+ "	billing.t_package_consumption_service_detail set status = 'I' where package_consumption_master_id = :packageConsumptionMasterId ")
						.setParameter("packageConsumptionMasterId", packageConsumptionMasterId).executeUpdate();
				
				resultCount = sessionFactory.getCurrentSession().createSQLQuery("update billing.t_package_consumption_cap_details set status = 'I' "
						+ " where package_consumption_master_id = :packageConsumptionMasterId")
						.setParameter("packageConsumptionMasterId", packageConsumptionMasterId).executeUpdate();
				
				resultCount = sessionFactory.getCurrentSession().createSQLQuery("update billing.t_package_consumption_eo_details set status = 'I' where package_consumption_master_id = :packageConsumptionMasterId	")
						.setParameter("packageConsumptionMasterId", packageConsumptionMasterId).executeUpdate();
				
				resultCount = sessionFactory.getCurrentSession().createSQLQuery("update billing.t_package_bed_category set 	status = 'I' where 	package_consumption_master_id = :packageConsumptionMasterId	")
						.setParameter("packageConsumptionMasterId", packageConsumptionMasterId).executeUpdate();
				
				master = findById(dto.getPackageConsumptionMasterId());
				master.setPackageId(dto.getPackageId());
				master.setRate(dto.getRate());
				sessionFactory.getCurrentSession().update(master);
				this.inactivatePackageForPayee(dto.getPackageConsumptionMasterId());
			}
			else
			{
					master.setAdmissionId(dto.getAdmissionId());
					master.setApplicableNoOfVisit(dto.getApplicableNoOfVisit());
					master.setBalanceDeposite(dto.getBalanceDeposite());
					master.setCancelledBy(dto.getCancelledBy());
					master.setCancelledDate(dto.getCancelledDate() != null && !dto.getCancelledDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCancelledDate(), "dd-M-yyyy") : null);
					master.setCancelledReasonId(dto.getCancelledReasonId());
					master.setCancelledRemark(dto.getCancelledRemark());
					master.setCoShareInPercentage(dto.getCoShareInPercentage());
					master.setCreatedBy(dto.getCreatedBy());
					master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.setDiscountinedBy(dto.getDiscountinedBy());
					master.setDiscountinedDate(dto.getDiscountinedDate() != null && !dto.getDiscountinedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getDiscountinedDate(), "dd-M-yyyy") : null);
					master.setDiscountinuedReasonId(dto.getDiscountinuedReasonId());
					master.setDiscountinuedRemark(dto.getDiscountinuedRemark());
					master.setEncounterId(dto.getEncounterId());
					master.setIsCancelled(dto.getIsCancelled());
					master.setIsDiscountinued(dto.getIsDiscountinued());
					master.setOrderDetailId(dto.getOrderDetailId());
					master.setOrganisationId(dto.getOrganisationId());
					master.setPackageId(dto.getPackageId());
					master.setPackageTypeId(dto.getPackageTypeId());
					master.setPatientId(dto.getPatientId());
					master.setPayeeId(dto.getPayeeId());
					master.setRate(dto.getRate());
					master.setStatus(ACTIVE);
					master.setTariffId(dto.getTariffId());
					master.setTotalDeposite(dto.getTotalDeposite());
					master.setUnitId(dto.getUnitId());
					master.setUpdatedBy(dto.getUpdatedBy());
					master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : null);
					master.setValidityFrom(dto.getValidityFrom() != null && !dto.getValidityFrom().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getValidityFrom(), "dd-M-yyyy HH:mm:ss") : null);
					master.setValidityTo(dto.getValidityTo() != null && !dto.getValidityTo().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getValidityTo(), "dd-M-yyyy HH:mm:ss") : null);
					master.setVisitType(dto.getVisitType());
					master.setEncounterSpecialityId(dto.getEncounterSpecialityId());
					master.setVisisted(dto.isVisisted());
					master.setBillingBedCategoryId(dto.getBillingBedCategoryId());
					master.setIsForceClose(1);
					master.setPackageApplicableDate(dto.getPackageApplicableDate() != null ? GlobalCommonDateUtils.getDate(dto.getPackageApplicableDate(), "dd-MM-yyyy") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getExistingActivePackageByPatientEncounter(
			Integer patientId, Integer encounterId, Integer visitTypeId, Integer organisationId, Integer unitId,boolean isPharamcy,Integer billingBedCategoryId)
			throws ServiceException {
		try 
		{
			List<PackageConsumptionMasterResDto> list = null;
			if(encounterId!=null && encounterId>0) {
			//Date date = TimeZoneComponent.getDateByzone(unitId);
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXISITNG_PACKAGE_DETAILS_BY_ENCOUNTERID_VISITTYPE_PATIENTID").getQueryString());
//			query.append("	and  '"+GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd")+"' >= to_char(pcm.validity_from,'YYYY-MM-DD')  ");
//			query.append("	and '"+GlobalCommonDateUtils.getStringDate(date,"yyyy-MM-dd")+"' <= to_char(pcm.validity_to,'YYYY-MM-DD') ");
			query.append("	order by \"packageConsumptionMasterId\" ");
			 list = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("patientId", patientId)
					.setParameter("encounterId", encounterId)
					.setParameter("visitTypeId",visitTypeId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setBoolean("isPharamcy", isPharamcy)
					.setParameter("billingBedCategoryId", billingBedCategoryId!=null ? billingBedCategoryId : 0)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionMasterResDto.class))
					.list();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageConsumptionServiceDetailsByPackageIdPatientId(
			Integer packageId, Integer patientId) throws ServiceException {
		try {
			List<PackageConsumptionServiceDetailDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACAKAGE_CONSUMPTION_SERVICE_DETAILS_BY_PACKAGEID_PATIENTID")
					.setParameter("packageId",packageId)
					.setParameter("patientId", patientId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionServiceDetailDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response cancelPackageConsumption(int packageId,int patientId,Integer encounterId,Integer cancelReasonId,String cancelRemark,Integer admissionId,Integer visitTypeId,Integer orderDetailsId) throws ServiceException {
		try {
			String query = null;
			Integer result = 0;
			if(cancelReasonId!=null && cancelRemark!=null)
			{
				result = sessionFactory.getCurrentSession().getNamedQuery("CANCEL_PACKAGE_CONSUMPTION")
						.setParameter("packageId", packageId)
						.setParameter("cancelReasonId", cancelReasonId)
						.setParameter("patientId", patientId)
						.setParameter("cancelledRemark", cancelRemark)
						.setParameter("orderDetailsId", orderDetailsId)
						.executeUpdate();
				
			}else{
				query = " update billing.t_package_consumption_master "
						+ " set status = 'I',"
						+ "	is_cancelled ='Y',"
						+ "	is_discountinued='Y',"
						+ "	cancelled_remark=:cancelledRemark, "
						+ "	cancelled_date=now() "
						+ " where package_id=:packageId "
						+ "	and patient_id =:patientId "
						+ "	and order_detail_id =:orderDetailsId ";
				
				result = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("packageId", packageId)
						.setParameter("patientId", patientId)
						.setParameter("cancelledRemark", "Package cancelled by stsyem")
						.setParameter("orderDetailsId", orderDetailsId)
						.executeUpdate();
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOrderedExclusionServices(int packageConsumptionMasterId) throws ServiceException {
		try {
			
			List<PackageConsumptionServiceDetailDto> list = new ArrayList<PackageConsumptionServiceDetailDto>();
			List<PackageConsumptionServiceDetailDto> listService = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCLUSION_EXCLUSION_ORDERED_SERVICES")
					.setParameter("packageConsumptionMasterId",packageConsumptionMasterId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionServiceDetailDto.class))
					.list();
			
			List<PackageConsumptionServiceDetailDto> listItem = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCLUSION_EXCLUSION_ORDERED_ITEMS")
					.setParameter("packageConsumptionMasterId",packageConsumptionMasterId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionServiceDetailDto.class))
					.list();
			
			
			if(listService.size()>0)
				list.addAll(listService);
				listService.addAll(listItem);
			 if(listItem.size()>0)
				 list.addAll(listItem);
			
					
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveMEPackage(Integer patientId, Integer encounterId, Integer visitTypeId,
			Integer organisationId, Integer unitId) throws ServiceException {
		try {
			List<PackageConsumptionMasterResDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXISITNG_PACKAGE_ACTIVE_ME_PACKAGE")
					.setParameter("patientId",patientId)
					.setParameter("visitTypeId",visitTypeId)
					.setParameter("organisationId",organisationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionMasterResDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateNoOfVisitsME(PackageConsumptionMasterResDto obj) throws ServiceException {
		try{
			Integer updateCount = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_MULTI_ENC_VISIT")
					.setParameter("packageConsumptionMasterId", obj.getPackageConsumptionMasterId())
					.setParameter("encounterId", obj.getEncounterId())
					.setParameter("visit", 1)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response expireMEPkgByVisit(Integer encounterId,Integer visitTypeId) throws ServiceException {
		try{
			String query = null;
			if(visitTypeId!=null&& visitTypeId.intValue()==1)
			{
				query =  "update "
						+ "	billing.t_package_consumption_master "
						+ "set "
						+ "	applicable_no_of_visit = applicable_no_of_visit - 1 "
						+ "where "
						+ "	patient_id in (select "
						+ "	distinct coalesce(om.patient_id,0) "
						+ "from "
						+ "	public.t_order_details od "
						+ "inner join public.t_order_master om on "
						+ "	om.order_id = od.order_id "
						+ "inner join public.t_order_details_package_mapper odpckmppr on "
						+ "	od.order_details_id = odpckmppr.order_details_id "
						+ "	and odpckmppr.status = 'A' "
						+ "inner join service.m_package_master pkgmst on "
						+ "	odpckmppr.package_id = pkgmst.package_master_id "
						+ "	and pkgmst.package_type_id = 5 "
						+ " where to_char(om.order_date,'YYYY-MM-DD') =:orderDate and od.status = 'A' and om.encounter_id = :encounterId ) "
						+ " and package_id in ( select "
						+ "	distinct coalesce(odpckmppr.package_id,0) "
						+ "from "
						+ "	public.t_order_details od "
						+ "inner join public.t_order_master om on "
						+ "	om.order_id = od.order_id "
						+ "inner join public.t_order_details_package_mapper odpckmppr on "
						+ "	od.order_details_id = odpckmppr.order_details_id "
						+ "	and odpckmppr.status = 'A' "
						+ "inner join service.m_package_master pkgmst on "
						+ "	odpckmppr.package_id = pkgmst.package_master_id "
						+ "	and pkgmst.package_type_id = 5 "
						+ " where to_char(om.order_date,'YYYY-MM-DD') =:orderDate and od.status = 'A' and om.encounter_id = :encounterId ) "
						+ "	and package_type_id = 5 "
						+ "	and applicable_no_of_visit > 0 and status = 'A' ";
			}
			else 
			{
				query =  "update "
						+ "	billing.t_package_consumption_master "
						+ "set "
						+ "	applicable_no_of_visit = applicable_no_of_visit - 1 "
						+ "where "
						+ "	patient_id in (select "
						+ "	distinct coalesce(om.patient_id,0) "
						+ " from "
						+ "	public.t_order_details od "
						+ " inner join public.t_order_master om on "
						+ "	om.order_id = od.order_id "
						+ "inner join public.t_order_details_package_mapper odpckmppr on "
						+ "	od.order_details_id = odpckmppr.order_details_id "
						+ "	and odpckmppr.status = 'A' "
						+ "inner join service.m_package_master pkgmst on "
						+ "	odpckmppr.package_id = pkgmst.package_master_id "
						+ "	and pkgmst.package_type_id = 5 "
						+ " where to_char(om.order_date,'YYYY-MM-DD') =:orderDate and od.status = 'A' and om.admission_id = :encounterId ) "
						+ " and "
						+ "	package_id in (select "
						+ "	distinct coalesce(odpckmppr.package_id,0) "
						+ " from "
						+ "	public.t_order_details od "
						+ " inner join public.t_order_master om on "
						+ "	om.order_id = od.order_id "
						+ " inner join public.t_order_details_package_mapper odpckmppr on "
						+ "	od.order_details_id = odpckmppr.order_details_id "
						+ "	and odpckmppr.status = 'A' "
						+ " inner join service.m_package_master pkgmst on "
						+ "	odpckmppr.package_id = pkgmst.package_master_id "
						+ "	and pkgmst.package_type_id = 5 "
						+ " where to_char(om.order_date,'YYYY-MM-DD') =:orderDate and od.status = 'A' and om.admission_id = :encounterId ) "
						+ "	and package_type_id = 5 "
						+ "	and applicable_no_of_visit > 0 and status = 'A'";
			}
			Integer updateCount = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("encounterId", encounterId)
					.setParameter("orderDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd"))
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageConsumptionServiceDetailsByPackageIdPatientId(int packageConsumptionMasterId)
			throws ServiceException {
		try {
			List<PackageConsumptionServiceDetailDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACAKAGE_CONSUMPTION_SERVICE_DETAILS_BY_CONSUMPTION_ID")
					.setParameter("packageConsumptionMasterId",packageConsumptionMasterId)
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionServiceDetailDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response expireMEPkgByVisitByPatientId(Integer patientId, Integer visitTypeId) throws ServiceException {
		try {
			String query = null;
			if(visitTypeId!=null&& visitTypeId.intValue()==1)
			{
				query =  "update "
						+ "	billing.t_package_consumption_master "
						+ "set "
						+ "	applicable_no_of_visit = applicable_no_of_visit - 1 "
						+ "where "
						+ "	patient_id in (select "
						+ "	distinct coalesce(om.patient_id,0) "
						+ "from "
						+ "	public.t_order_details od "
						+ "inner join public.t_order_master om on "
						+ "	om.order_id = od.order_id "
						+ "inner join public.t_order_details_package_mapper odpckmppr on "
						+ "	od.order_details_id = odpckmppr.order_details_id "
						+ "	and odpckmppr.status = 'A' "
						+ "inner join 	opd.t_encounter_master encMstr on om.encounter_id=encmstr.encounter_id and encMstr.status_id in (1,5,8,6,4) "
						+ " "
						+ "inner join service.m_package_master pkgmst on "
						+ "	odpckmppr.package_id = pkgmst.package_master_id "
						+ "	and pkgmst.package_type_id = 5 "
						+ " where to_char(om.order_date,'YYYY-MM-DD') =:orderDate and od.status = 'A' and om.patient_id = :patientId ) "
						+ " and package_id in ( select "
						+ "	distinct coalesce(odpckmppr.package_id,0) "
						+ "from "
						+ "	public.t_order_details od "
						+ "inner join public.t_order_master om on "
						+ "	om.order_id = od.order_id "
						+ "inner join public.t_order_details_package_mapper odpckmppr on "
						+ "	od.order_details_id = odpckmppr.order_details_id "
						+ "	and odpckmppr.status = 'A' "
						+ "inner join 	opd.t_encounter_master encMstr on om.encounter_id=encmstr.encounter_id and encMstr.status_id in (1,5,8,6,4) "
						+ "inner join service.m_package_master pkgmst on "
						+ "	odpckmppr.package_id = pkgmst.package_master_id "
						+ "	and pkgmst.package_type_id = 5 "
						+ " where to_char(om.order_date,'YYYY-MM-DD') =:orderDate and od.status = 'A' and om.patient_id = :patientId ) "
						+ "	and package_type_id = 5 "
						+ "	and applicable_no_of_visit > 0 and status = 'A'";
			}
			Integer updateCount = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("patientId", patientId)
					.setParameter("orderDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd"))
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, updateCount);
		}catch (HibernateException he) {
			throw new ServiceException(he.getMessage());
		}
	}	
		
		
	public Response inactivatePackageForPayee(Integer packageConsumptionMasterId) throws ServiceException 
	{
		try 
		{
		 return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response cancelPackageConsumption(Integer orderDetailsId) throws ServiceException {
		try {			
			Integer result = sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_package_consumption_master "
					+ " set status = 'I',is_cancelled ='Y',is_discountinued='Y', cancelled_date=now() "
					+ " where  order_detail_id =:orderDetailsId ")
					.setParameter("orderDetailsId", orderDetailsId)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageServicesByOrderDetailsId(Integer packageId,Integer billId) throws ServiceException {
		try {			
			List<CreditNoteDetails> list = sessionFactory.getCurrentSession()
					.createSQLQuery(" select " 
						 + " 	tod.order_details_id as \"orderDetailId\", " 
						 + " 	tbd.billing_details_id as \"billingDetailsId\", " 
						 + " 	tbd.gross_amount as \"crServiceRateAmount\", " 
						 + " 	todpm.package_amount as \"packageConsumptionAmt\" " 
						 + " from " 
						 + " 	public.t_order_details_package_mapper todpm " 
						 + " inner join public.t_order_details tod on " 
						 + " 	todpm.order_details_id = tod.order_details_id " 
						 + " inner join public.t_order_master tom on " 
						 + " 	tod.order_id = tom.order_id " 
						 + " inner join billing.t_billing_details tbd on " 
						 + " 	tbd.order_details_id = tod.order_details_id " 
						 + " where " 
						 + " 	todpm.package_id =:packageId " 
						 + " 	and tod.service_rendered = 0 " 
						 + " 	and todpm.status = 'A' " 
						 + " 	and todpm.is_package_service = 'Y' " 
						 + " 	and tbd.billing_master_id =:billId ")
					.setParameter("packageId", packageId)
					.setParameter("billId", billId)
					.setResultTransformer(Transformers.aliasToBean(CreditNoteDetails.class)).list();
				
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryIdsByParentPackageId(TPackageConsumptionMasterDto dto)
			throws BillingException {
		try {			
			List<PackageBBCategoryMpprDto> list = sessionFactory.getCurrentSession().createSQLQuery("select "
					+ "	  distinct(pm.billing_bed_category_id) as \"billingBedCategoryId\" "
					+ "from "
					+ "	service.m_parent_package_master mppm "
					+ "inner join service.m_package_master pm on "
					+ "	mppm.parent_package_id = pm.parent_package_id "
					+ "where "
					+ "	mppm.parent_package_id  = :parentPackageId "
					+ "	and pm.status  = 'A'")
					.setParameter("parentPackageId", dto.getParentPackageId())
					.setResultTransformer(Transformers.aliasToBean(PackageBBCategoryMpprDto.class)).list();
			
				
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePackageBbcMppr(PackageBBCategoryMpprDto dto) throws BillingException {
		try{
			   PackageBBCategoryMppr master = new PackageBBCategoryMppr();
				master.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
				master.setBillingBedCategoryId(dto.getBillingBedCategoryId());
				master.setPackageId(dto.getPackageId());
				sessionFactory.getCurrentSession().save(master);
				
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getBillingBedCategoryIdsByPackageConsumptionId(Integer packageConsumptionMasterId)
			throws BillingException {
		try {			
			List<Integer> list = sessionFactory.getCurrentSession().createSQLQuery("select " + 
					"	pbbc.billing_bed_category_id  " + 
					" from " + 
					"	billing.package_billing_bed_category pbbc " + 
					" where " + 
					"	pbbc.package_consumption_master_id = :packageConsumptionMasterId")
					.setParameter("packageConsumptionMasterId", packageConsumptionMasterId)
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updatePackageApplicableDate(TPackageConsumptionMasterDto tPackageConsumptionMasterDto)
			throws BillingException {
		try {	
			
			Response packRes = iMPackageMasterDao.getPackageById(tPackageConsumptionMasterDto.getPackageId());
			MPackageMasterDto mPackage = (MPackageMasterDto) packRes.getObject();

			String dateString = tPackageConsumptionMasterDto.getPackageApplicableDate(); // Assuming this is "2025-01-22"
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedDate = dateFormat.parse(dateString);
			Timestamp packageApplicableTimestamp = new Timestamp(parsedDate.getTime());


			if (mPackage.getPackageTypeId() == 5||mPackage.getPackageTypeId() == 4||mPackage.getPackageTypeId() == 2) 
			{
				Integer validityInDays = mPackage.getValidityPeriodIndays()-1;
				LocalDate ldate =null; 
					
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String date = tPackageConsumptionMasterDto.getPackageApplicableDate().substring(0,10);
				ldate = LocalDate.parse(date, formatter).plusDays(validityInDays);
				
				Date validityDate = GlobalCommonDateUtils.getDateWithEndTime(ldate);
				Timestamp validityDateTimestamp = new Timestamp(parsedDate.getTime());
				sessionFactory.getCurrentSession()
						.createSQLQuery(
								"UPDATE billing.t_package_consumption_master " +
										"SET package_applicable_date = :packageApplicableDate, " +
										"validity_from = :packageApplicableDate, " +
										"validity_to = :validityTo " +
										"WHERE package_consumption_master_id = :packageConsumptionMasterId")
						.setParameter("packageApplicableDate", packageApplicableTimestamp, StandardBasicTypes.TIMESTAMP)
						.setParameter("validityTo", validityDateTimestamp,StandardBasicTypes.TIMESTAMP)
						.setParameter("packageConsumptionMasterId", tPackageConsumptionMasterDto.getPackageConsumptionMasterId())
						.executeUpdate();
			}else{
				sessionFactory.getCurrentSession()
						.createSQLQuery(
								"UPDATE billing.t_package_consumption_master " +
										"SET package_applicable_date = :packageApplicableDate " +
										"WHERE package_consumption_master_id = :packageConsumptionMasterId")
						.setParameter("packageApplicableDate",  packageApplicableTimestamp, StandardBasicTypes.TIMESTAMP)
						.setParameter("packageConsumptionMasterId", tPackageConsumptionMasterDto.getPackageConsumptionMasterId())
						.executeUpdate();

			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getExistingActiveMultivisitPackageForDeactivation(List<Integer> unitIds)throws ServiceException 
	{
		try 
		{
			String query = " select " 
					 + " 	packMaster.package_master_id as \"packageId\", " 
					 + " 	pcm.order_detail_id as \"orderDetailId\", " 
					 + " 	pcm.patient_id as \"patientId\" " 
					 + " from " 
					 + " 	billing.t_package_consumption_master pcm " 
					 + " inner join service.m_package_master packMaster on " 
					 + " 	packMaster.package_master_id = pcm.package_id " 
					 + " inner join public.t_order_details ordDetails on " 
					 + " 	ordDetails.order_details_id = pcm.order_detail_id " 
					 + " left join adt.t_admission ta on " 
					 + " 	ta.patient_id = pcm.patient_id and ta.status ='A' " 
					 + " where " 
					 + " 	pcm.package_type_id = 5 " 
					 + " 	and ordDetails.status = 'A' " 
					 + " 	and pcm.status = 'A' " 
					 + " 	and pcm.is_cancelled = 'N' " 
					 + " 	and pcm.is_discountinued = 'N' " 
					 + " 	and ta.admission_id is null " 
					 + " 	and to_char(pcm.validity_to, 'YYYY-MM-DD HH24:MI:SS') <:date "
					 + "    and pcm.unit_id in (:unitIds) ";

			List<PackageConsumptionMasterResDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameterList("unitIds", unitIds)
					.setParameter("date", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd HH:mm:ss"))
					.setResultTransformer(Transformers.aliasToBean(PackageConsumptionMasterResDto.class))
					.list();
			
			for(PackageConsumptionMasterResDto dto : list)
			{
				this.cancelPackageConsumption(dto.getPackageId(),dto.getPatientId(),null,null,null,null,null,dto.getOrderDetailId());
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
}