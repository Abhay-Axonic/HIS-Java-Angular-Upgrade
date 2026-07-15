package com.param.workbench.dao;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.param.global.dto.OrderMasterDto;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.param.billing.global.smartcard.dto.ItemDetailsResponseDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.radiology.model.RadiologyMapper;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Repository

@SuppressWarnings({ "unchecked", "rawtypes", "unused"})
public class RISWorkbenchDaoImpl implements IRISWorkbenchDao,ICommonConstants,IError{

	private static final Logger logger = Logger.getLogger(RISWorkbenchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private SessionService sessionService; 
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Override
	public Response getRadiologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {			
				StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_RADIOLOGY_WORKLIST").getQueryString());
			
				query.append(" AND  orderdetails.service_rendered = 0	and orderdetails.is_order_visible_dept = 'A' ");			
				/*query.append(radiologyOrdersListDto.getFromDate() != null ? " AND ordermaster.order_date between '" + radiologyOrdersListDto.getFromDate().substring(0, 10)+" 00:00:00' and  '" + radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00' " : "") ;
				query.append(radiologyOrdersListDto.getPatientId() !=null && radiologyOrdersListDto.getPatientId() > 0 ? " and ordermaster.patient_id="+radiologyOrdersListDto.getPatientId() : "");			
				query.append(radiologyOrdersListDto.getServiceId()!=null && radiologyOrdersListDto.getServiceId() > 0 ? " and orderdetails.service_id="+radiologyOrdersListDto.getServiceId() : "");			
			    query.append(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0 ? " and modalitymstr.modality_id IN :modalityList " : "");			 
				query.append(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0 ? " and ordermaster.visit_type_id IN :visitTypeList " : "");*/
			if (radiologyOrdersListDto.getFromDate() != null) {
				query.append(" AND ordermaster.order_date BETWEEN :fromDate AND :toDate");
			}
			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId");
			}
			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				query.append(" AND orderdetails.service_id = :serviceId");
			}
			if (radiologyOrdersListDto.getModalityList() != null && !radiologyOrdersListDto.getModalityList().isEmpty()) {
				query.append(" AND modalitymstr.modality_id IN :modalityList");
			}
			if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
				query.append(" AND ordermaster.visit_type_id IN :visitTypeList");
			}
				query.append(" order by \"isCritical\" desc ,\"orderDetailsId\" desc ");
			
				Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());

				if (radiologyOrdersListDto.getFromDate() != null) {
					finalQuery.setParameter("fromDate", Timestamp.valueOf(radiologyOrdersListDto.getFromDate().substring(0, 10) + " 00:00:00"));
					finalQuery.setParameter("toDate", Timestamp.valueOf(radiologyOrdersListDto.getToDate().substring(0, 10) + " 00:00:00"));
				}
				if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
					finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());
				}
				if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
					finalQuery.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
				}
				if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0)
				{
					finalQuery.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
				}
				
				if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size()>0) {
					finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
				}

				List<RadiologyOrdersListDto> listServiceMasterDto = finalQuery
						.setParameter("unitId", radiologyOrdersListDto.getUnitId())
						.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
						.setParameter("radiologySpecialityId", radiologyOrdersListDto.getRadiologySpecialityId())
						.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
				
				
				List<RadiologyOrdersListDto> list = listServiceMasterDto;
				List<Integer> OrderDetailsIds = list.stream().map(RadiologyOrdersListDto::getOrderDetailsId ).collect(Collectors.toList());
				if(!OrderDetailsIds.isEmpty() && OrderDetailsIds != null && OrderDetailsIds.size() > 0) {
				 String querys =" select\r\n" + 
				 		"		distinct (tds.order_detail_id) as \"orderDetailsId\", " + 
				 		"		tds.credit_note_id as  \"creditNoteId\"  " + 
				 		"	from " + 
				 		"		billing.t_credit_note_detail tds " + 
				 		"	inner join billing.m_credit_note_master crd on " + 
				 		"		crd.credit_note_id = tds.credit_note_id " + 
				 		"	where " + 
				 		"	 tds.status = 'A' " + 
				 		"	and crd.status = 'A' " + 
				 		"   and tds.order_detail_id in (:OrderDetailsIds)";
				 List<RadiologyOrdersListDto> creditNoteList = sessionFactory
					        .getCurrentSession()
					        .createSQLQuery(querys)
					        .addScalar("orderDetailsId")  // Assuming orderDetailsId is of type Long
					        .addScalar("creditNoteId")   // Assuming creditNoteId is of type Long
					        .setParameterList("OrderDetailsIds", OrderDetailsIds)
					        .setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class))
					        .list();
				  
				
				if(creditNoteList != null && creditNoteList.size()>0){
					for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
						RadiologyOrdersListDto dummyserviceCount = creditNoteList.stream()
								  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
								  .findAny()
								  .orElse(null);
						if(dummyserviceCount!=null){
							labSampleMasterDto.setCreditNoteId(dummyserviceCount.getCreditNoteId());
						}
					}
				}
				 
				 List<RadiologyOrdersListDto> doctorShareList =  sessionFactory.getCurrentSession()
							.createSQLQuery(
									" select " + 
									 		"	    distinct (tds.ord_dtl_id) as \"orderDetailsId\", " + 
									 		"		tdsvd.doctor_share_id as \"doctorShareId\" " + 
									 		"	from " + 
									 		"		billing.t_doctor_share tds " + 
									 		"	inner join billing.t_doc_share_voucher_dtls tdsvd on " + 
									 		"		tds.doctor_share_id = tdsvd .doctor_share_id " + 
									 		"	where " + 
									 		"		tds.doc_id is not null " + 
									 		"	 and tds.ord_dtl_id in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
							.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
				 
					if(doctorShareList != null && doctorShareList.size()>0){
						for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
							RadiologyOrdersListDto dummyserviceCountA = doctorShareList.stream()
									  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
									  .findAny()
									  .orElse(null);
							if(dummyserviceCountA!=null){
								labSampleMasterDto.setDoctorShareId(dummyserviceCountA.getCreditNoteId());
							}
						}
					}
					
					 List<RadiologyOrdersListDto> referalList =  sessionFactory.getCurrentSession()
								.createSQLQuery(
										" 	 select " + 
										"	distinct (orderdetails.order_details_id) as \"orderDetailsId\", " + 
										"    refM.referral_name AS \"referralName\", " + 
										"    refM.referral_id AS \"referralId\"  " + 
										"FROM " + 
										"    public.t_order_details AS orderdetails " + 
										"INNER JOIN " + 
										"    public.t_order_master AS ordermaster ON orderdetails.order_id = ordermaster.order_id " + 
										"LEFT JOIN " + 
										"    t_referal_details AS dtls1 ON " + 
										"    ( " + 
										"        (ordermaster.encounter_id IS NOT NULL AND dtls1.encounter_id = ordermaster.encounter_id) " + 
										"        OR dtls1.admission_id = ordermaster.admission_id " + 
										"    ) " + 
										"LEFT JOIN " + 
										"    adt.m_referral_master AS refM ON dtls1.referral_id = refM.referral_id  " + 
										"where " + 
										"	 orderdetails.order_details_id  in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
								.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
					 
						if(referalList != null && referalList.size()>0){
							for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
								RadiologyOrdersListDto dummyserviceCountRef = referalList.stream()
										  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
										  .findAny()
										  .orElse(null);
								if(dummyserviceCountRef!=null){
									labSampleMasterDto.setReferralId(dummyserviceCountRef.getReferralId());
									labSampleMasterDto.setReferralName(dummyserviceCountRef.getReferralName());
								}
							}
						}
						
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (Exception e) {
			logger.error("getRadiologyWorkbenchWorklist failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	@Transactional
	public Response getRadiologyWorkbenchWorklistPagination(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {

			List<RadiologyOrdersListDto> listServiceMasterDto2 = null;

			StringBuilder query1 = new StringBuilder(
					sessionFactory.getCurrentSession().getNamedQuery("GET_RADIOLOGY_WORKLIST").getQueryString());

			// StringBuilder query3 = new StringBuilder(
			// sessionFactory.getCurrentSession().getNamedQuery("GET_RADIOLOGY_WORKLIST").getQueryString());

			// query 1
			query1.append(" AND  orderdetails.service_rendered = 0	and orderdetails.is_order_visible_dept = 'A' ");
			// query1.append(radiologyOrdersListDto.getFromDate() != null ? " AND
			// to_char(ordermaster.order_date, 'yyyy-mm-dd') between '"
			// + radiologyOrdersListDto.getFromDate() + "' and '" +
			// radiologyOrdersListDto.getToDate()
			// + "' "
			// : "");
			// query1.append(" AND appMstr.appointment_date is null ");
			/*query1.append(radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0
					? " and ordermaster.patient_id=" + radiologyOrdersListDto.getPatientId()
					: "");
			query1.append(radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0
					? " and orderdetails.service_id=" + radiologyOrdersListDto.getServiceId()
					: "");
			query1.append(radiologyOrdersListDto.getModalityList() != null
					&& radiologyOrdersListDto.getModalityList().size() > 0
							? " and modalitymstr.modality_id IN :modalityList "
							: "");
			query1.append(
					radiologyOrdersListDto.getVisitList() != null && radiologyOrdersListDto.getVisitList().size() > 0
							? " and ordermaster.visit_type_id IN :visitTypeList "
							: "");
			query1.append(
					radiologyOrdersListDto.getDeptList() != null && radiologyOrdersListDto.getDeptList().size() > 0
							? " and orderdetails.service_rendering_deptid IN :deptList "
							: "");

			query1.append(" AND (CASE WHEN appMstr.appointment_date IS NULL THEN "
					+ (radiologyOrdersListDto.getFromDate() != null
							? "to_char(ordermaster.order_date, 'yyyy-mm-dd') BETWEEN '"
									+ radiologyOrdersListDto.getFromDate() + "'and '"
									+ radiologyOrdersListDto.getToDate() + "' "
							: "")
					+ "ELSE "
					+ (radiologyOrdersListDto.getFromDate() != null
							? " to_char(appMstr.appointment_date, 'yyyy-mm-dd') BETWEEN '"
									+ radiologyOrdersListDto.getFromDate() + "'and'"
									+ radiologyOrdersListDto.getToDate() + "' "
							: "")
					+ "END) ");*/

			query1.append(radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0
					? " and ordermaster.patient_id = :patientId "
					: "");
			query1.append(radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0
					? " and orderdetails.service_id = :serviceId "
					: "");
			query1.append(radiologyOrdersListDto.getModalityList() != null && !radiologyOrdersListDto.getModalityList().isEmpty()
					? " and modalitymstr.modality_id IN :modalityList "
					: "");
			query1.append(radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()
					? " and ordermaster.visit_type_id IN :visitTypeList "
					: "");
			query1.append(radiologyOrdersListDto.getDeptList() != null && !radiologyOrdersListDto.getDeptList().isEmpty()
					? " and orderdetails.service_rendering_deptid IN :deptList "
					: "");

			query1.append(" AND (CASE WHEN appMstr.appointment_date IS NULL THEN "
					+ (radiologyOrdersListDto.getFromDate() != null
					? " to_char(ordermaster.order_date, 'yyyy-mm-dd') BETWEEN :fromDate AND :toDate "
					: "")
					+ " ELSE "
					+ (radiologyOrdersListDto.getFromDate() != null
					? " to_char(appMstr.appointment_date, 'yyyy-mm-dd') BETWEEN :fromDate AND :toDate "
					: "")
					+ " END) ");


			// query1.append(" union all ");
			// query 3
			/*
			 * query3.
			 * append(" AND  orderdetails.service_rendered = 0	and orderdetails.is_order_visible_dept = 'A' "
			 * ); query3.append(radiologyOrdersListDto.getFromDate() != null ?
			 * " AND to_char(appMstr.appointment_date, 'yyyy-mm-dd') between '" +
			 * radiologyOrdersListDto.getFromDate() + "' and  '" +
			 * radiologyOrdersListDto.getToDate() + "' " : "");
			 * query3.append(radiologyOrdersListDto.getPatientId() != null &&
			 * radiologyOrdersListDto.getPatientId() > 0 ? " and ordermaster.patient_id=" +
			 * radiologyOrdersListDto.getPatientId() : "");
			 * query3.append(radiologyOrdersListDto.getServiceId() != null &&
			 * radiologyOrdersListDto.getServiceId() > 0 ? " and orderdetails.service_id=" +
			 * radiologyOrdersListDto.getServiceId() : "");
			 * query3.append(radiologyOrdersListDto.getModalityList() != null &&
			 * radiologyOrdersListDto.getModalityList().size() > 0 ?
			 * " and modalitymstr.modality_id IN :modalityList " : ""); query3.append(
			 * radiologyOrdersListDto.getVisitList() != null &&
			 * radiologyOrdersListDto.getVisitList().size() > 0 ?
			 * " and ordermaster.visit_type_id IN :visitTypeList " : ""); query3.append(
			 * radiologyOrdersListDto.getDeptList() != null &&
			 * radiologyOrdersListDto.getDeptList().size() > 0 ?
			 * " and orderdetails.service_rendering_deptid IN :deptList " : "");
			 */
			// query1.append(query3);
			query1.append(" order by \"isCritical\" desc ,\"orderDetailsId\" desc ");
			// query3.append(" order by \"isCritical\" desc ,\"orderDetailsId\" desc ");
			Query finalQuery1 = sessionFactory.getCurrentSession().createSQLQuery(query1.toString());

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				finalQuery1.setParameter("patientId", radiologyOrdersListDto.getPatientId());
			}
			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				finalQuery1.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
			}
			if (radiologyOrdersListDto.getFromDate() != null) {
				finalQuery1.setParameter("fromDate", radiologyOrdersListDto.getFromDate());
				finalQuery1.setParameter("toDate", radiologyOrdersListDto.getToDate());
			}

			/*if (radiologyOrdersListDto.getModalityList() != null && radiologyOrdersListDto.getModalityList().size() > 0) {
				finalQuery1.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
			}

			if (radiologyOrdersListDto.getVisitList() != null && radiologyOrdersListDto.getVisitList().size() > 0) {
				finalQuery1.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
			}

			if (radiologyOrdersListDto.getDeptList() != null && radiologyOrdersListDto.getDeptList().size() > 0) {
				finalQuery1.setParameterList("deptList", radiologyOrdersListDto.getDeptList());
			}*/

			if (radiologyOrdersListDto.getRadiologySpecialityId() != null) {
				Integer count = 0;

				/*
				 * List<RadiologyOrdersListDto> listServiceMasterDto1 = finalQuery
				 * .setParameter("unitId", radiologyOrdersListDto.getUnitId())
				 * .setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
				 * .setParameter("radiologySpecialityId",
				 * radiologyOrdersListDto.getRadiologySpecialityId())
				 * .setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class))
				 * .list();
				 * 
				 * if(listServiceMasterDto1 != null && listServiceMasterDto1.size()>0) { count =
				 * listServiceMasterDto1.size(); }
				 */

				if (radiologyOrdersListDto.getModalityList() != null
						&& radiologyOrdersListDto.getModalityList().size() > 0) {
					finalQuery1.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
				}

				if (radiologyOrdersListDto.getVisitList() != null && radiologyOrdersListDto.getVisitList().size() > 0) {
					finalQuery1.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
				}

				if (radiologyOrdersListDto.getDeptList() != null && radiologyOrdersListDto.getDeptList().size() > 0) {
					finalQuery1.setParameterList("deptList", radiologyOrdersListDto.getDeptList());
				}


				List<RadiologyOrdersListDto> listServiceMasterDto = finalQuery1
						.setParameter("unitId", radiologyOrdersListDto.getUnitId())
						.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
						.setParameter("radiologySpecialityId", radiologyOrdersListDto.getRadiologySpecialityId())
//					.setFirstResult(radiologyOrdersListDto.getOffset() != null ? radiologyOrdersListDto.getOffset()  : 0)
//					.setMaxResults(radiologyOrdersListDto.getNoOfRecordsPerPage() != null ? radiologyOrdersListDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();

				// Query finalQuery =
				// sessionFactory.getCurrentSession().createSQLQuery(query3.toString());


				
				List<RadiologyOrdersListDto> list = listServiceMasterDto;
				List<Integer> OrderDetailsIds = list.stream().map(RadiologyOrdersListDto::getOrderDetailsId ).collect(Collectors.toList());
				if(!OrderDetailsIds.isEmpty() && OrderDetailsIds != null && OrderDetailsIds.size() > 0) {
				 String query =" select\r\n" + 
				 		"		distinct (tds.order_detail_id) as \"orderDetailsId\", " + 
				 		"		tds.credit_note_id as  \"creditNoteId\"  " + 
				 		"	from " + 
				 		"		billing.t_credit_note_detail tds " + 
				 		"	inner join billing.m_credit_note_master crd on " + 
				 		"		crd.credit_note_id = tds.credit_note_id " + 
				 		"	where " + 
				 		"	 tds.status = 'A' " + 
				 		"	and crd.status = 'A' " + 
				 		"   and tds.order_detail_id in (:OrderDetailsIds)";
				 List<RadiologyOrdersListDto> creditNoteList = sessionFactory
					        .getCurrentSession()
					        .createSQLQuery(query)
					        .addScalar("orderDetailsId")  // Assuming orderDetailsId is of type Long
					        .addScalar("creditNoteId")   // Assuming creditNoteId is of type Long
					        .setParameterList("OrderDetailsIds", OrderDetailsIds)
					        .setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class))
					        .list();
				  
				
				if(creditNoteList != null && creditNoteList.size()>0){
					for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
						RadiologyOrdersListDto dummyserviceCount = creditNoteList.stream()
								  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
								  .findAny()
								  .orElse(null);
						if(dummyserviceCount!=null){
							labSampleMasterDto.setCreditNoteId(dummyserviceCount.getCreditNoteId());
						}
					}
				}
				 
				 List<RadiologyOrdersListDto> doctorShareList =  sessionFactory.getCurrentSession()
							.createSQLQuery(
									" select " + 
									 		"	    distinct (tds.ord_dtl_id) as \"orderDetailsId\", " + 
									 		"		tdsvd.doctor_share_id as \"doctorShareId\" " + 
									 		"	from " + 
									 		"		billing.t_doctor_share tds " + 
									 		"	inner join billing.t_doc_share_voucher_dtls tdsvd on " + 
									 		"		tds.doctor_share_id = tdsvd .doctor_share_id " + 
									 		"	where " + 
									 		"		tds.doc_id is not null " + 
									 		"	 and tds.ord_dtl_id in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
							.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
				 
					if(doctorShareList != null && doctorShareList.size()>0){
						for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
							RadiologyOrdersListDto dummyserviceCountA = doctorShareList.stream()
									  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
									  .findAny()
									  .orElse(null);
							if(dummyserviceCountA!=null){
								labSampleMasterDto.setDoctorShareId(dummyserviceCountA.getCreditNoteId());
							}
						}
					}
					
					 List<RadiologyOrdersListDto> referalList =  sessionFactory.getCurrentSession()
								.createSQLQuery(
										" 	 select " + 
										"	distinct (orderdetails.order_details_id) as \"orderDetailsId\", " + 
										"    refM.referral_name AS \"referralName\", " + 
										"    refM.referral_id AS \"referralId\"  " + 
										"FROM " + 
										"    public.t_order_details AS orderdetails " + 
										"INNER JOIN " + 
										"    public.t_order_master AS ordermaster ON orderdetails.order_id = ordermaster.order_id " + 
										"LEFT JOIN " + 
										"    t_referal_details AS dtls1 ON " + 
										"    ( " + 
										"        (ordermaster.encounter_id IS NOT NULL AND dtls1.encounter_id = ordermaster.encounter_id) " + 
										"        OR dtls1.admission_id = ordermaster.admission_id " + 
										"    ) " + 
										"LEFT JOIN " + 
										"    adt.m_referral_master AS refM ON dtls1.referral_id = refM.referral_id  " + 
										"where " + 
										"	 orderdetails.order_details_id  in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
								.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
					 
						if(referalList != null && referalList.size()>0){
							for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
								RadiologyOrdersListDto dummyserviceCountRef = referalList.stream()
										  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
										  .findAny()
										  .orElse(null);
								if(dummyserviceCountRef!=null){
									labSampleMasterDto.setReferralId(dummyserviceCountRef.getReferralId());
									labSampleMasterDto.setReferralName(dummyserviceCountRef.getReferralName());
								}
							}
						}
						
				}

				return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, count);
			} else {
				return new Response<>(ERROR, ERR_500,
						"Radiology department is not configured in application configuration", null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	/*@Override
	public Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update OrderDetailsMaster set "
					+ "serviceRendered=1, "
					+ "ordRenderDatetime=now(), "
					+ "updatedBy=:updatedBy "
					+ "WHERE orderDetailsId=:orderDetailsId")
				.setParameter("updatedBy",radiologyOrdersListDto.getUpdatedBy())
				.setParameter("orderDetailsId", radiologyOrdersListDto.getOrderDetailsId())
			.executeUpdate();
			
			return new Response(SUCCESS, null, "Request accepted successfully...!!", null, null);

		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
*/
	@Override
	public Response saveRadiologyMapper(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			RadiologyMapper mapper =new RadiologyMapper();
			mapper.setAdmissionId(radiologyOrdersListDto.getAdmissionId());
			mapper.setEncounterId(radiologyOrdersListDto.getEncounterId());
			mapper.setOrderDetailsId(radiologyOrdersListDto.getOrderDetailsId());
			mapper.setCreatedBy(radiologyOrdersListDto.getUserId());
			mapper.setUpdatedBy(radiologyOrdersListDto.getUserId());
			mapper.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			mapper.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			mapper.setReasonId(radiologyOrdersListDto.getReasonId());
			mapper.setStatus('A');
			mapper.setIsCancel(radiologyOrdersListDto.getIsCritical()); // is Critical flag used here as Iscancel
			mapper.setUnitId(radiologyOrdersListDto.getUnitId());
			mapper.setOrganizationId(radiologyOrdersListDto.getOrganizationId());
			sessionFactory.getCurrentSession().save(mapper);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		 }catch(HibernateException he) {
		 logger.error("saveRadiologyMapper failed", he);
		 }catch(Exception e) {
		logger.error("saveRadiologyMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRadiologyResultEntrylist(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
				StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_RADIOLOGY_WORKLIST").getQueryString());
				query.append(" AND  orderdetails.service_rendered = 1 ");
				query.append(" AND (templatetxn.is_provisional_release = 'N' or templatetxn.is_provisional_release is null) and (templatetxn.is_final_release is null )  ");
			
				/*query.append(radiologyOrdersListDto.getFromDate() != null ? " AND rmppr.updated_date between '" + radiologyOrdersListDto.getFromDate().substring(0, 10)+" 00:00:00' and  '" + radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00' " : "");
				query.append(radiologyOrdersListDto.getPatientId() !=null && radiologyOrdersListDto.getPatientId() > 0 ? " and ordermaster.patient_id="+radiologyOrdersListDto.getPatientId() : "");
				query.append(radiologyOrdersListDto.getServiceId()!=null && radiologyOrdersListDto.getServiceId() > 0 ? " and orderdetails.service_id="+radiologyOrdersListDto.getServiceId() : "");			
				*/

			if (radiologyOrdersListDto.getFromDate() != null && radiologyOrdersListDto.getToDate() != null) {
				query.append(" AND rmppr.updated_date BETWEEN :fromDate AND :toDate ");
			}

			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId ");
			}

			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				query.append(" AND orderdetails.service_id = :serviceId ");
			}

			if (radiologyOrdersListDto.getModalityList() != null && !radiologyOrdersListDto.getModalityList().isEmpty()) {
				query.append(" AND modalitymstr.modality_id IN (:modalityList) ");
			}
			if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
				query.append(" AND ordermaster.visit_type_id IN (:visitTypeList) ");
			}
			//	query.append(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0 ? " and modalitymstr.modality_id IN :modalityList " : "");
				//query.append(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0 ? " and ordermaster.visit_type_id IN :visitTypeList " : "" );
			    query.append(" order by \"isCritical\" desc ,\"orderDetailsId\" desc ");
			
			    Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
			
				if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0)
				{
					finalQuery.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
				}
				if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0)
				{
					finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
				}

				if (radiologyOrdersListDto.getFromDate() != null) {
					finalQuery.setParameter("fromDate", Timestamp.valueOf(radiologyOrdersListDto.getFromDate().substring(0, 10) + " 00:00:00"));
				}
				if (radiologyOrdersListDto.getToDate() != null) {
					finalQuery.setParameter("toDate", Timestamp.valueOf(radiologyOrdersListDto.getToDate().substring(0, 10) + " 23:59:59"));
				}
				if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
					finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());}
				if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
					finalQuery.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
				}
				if(radiologyOrdersListDto.getRadiologySpecialityId() != null){
				
				List<RadiologyOrdersListDto> listServiceMasterDto = finalQuery
					.setParameter("unitId", radiologyOrdersListDto.getUnitId())
					.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
					.setParameter("radiologySpecialityId", radiologyOrdersListDto.getRadiologySpecialityId())
					.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
				
				
				
				List<RadiologyOrdersListDto> list = listServiceMasterDto;
				List<Integer> OrderDetailsIds = list.stream().map(RadiologyOrdersListDto::getOrderDetailsId ).collect(Collectors.toList());
				if(!OrderDetailsIds.isEmpty() && OrderDetailsIds != null && OrderDetailsIds.size() > 0) {
				 String querys =" select\r\n" + 
				 		"		distinct (tds.order_detail_id) as \"orderDetailsId\", " + 
				 		"		tds.credit_note_id as  \"creditNoteId\"  " + 
				 		"	from " + 
				 		"		billing.t_credit_note_detail tds " + 
				 		"	inner join billing.m_credit_note_master crd on " + 
				 		"		crd.credit_note_id = tds.credit_note_id " + 
				 		"	where " + 
				 		"	 tds.status = 'A' " + 
				 		"	and crd.status = 'A' " + 
				 		"   and tds.order_detail_id in (:OrderDetailsIds)";
				 List<RadiologyOrdersListDto> creditNoteList = sessionFactory
					        .getCurrentSession()
					        .createSQLQuery(querys)
					        .addScalar("orderDetailsId")  // Assuming orderDetailsId is of type Long
					        .addScalar("creditNoteId")   // Assuming creditNoteId is of type Long
					        .setParameterList("OrderDetailsIds", OrderDetailsIds)
					        .setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class))
					        .list();

					//if(creditNoteList != null && creditNoteList.size()>0){

						Map<Integer, Integer> creditNoteMap = creditNoteList.stream()
								.collect(Collectors.toMap(RadiologyOrdersListDto::getOrderDetailsId, RadiologyOrdersListDto::getCreditNoteId));

					/*for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
						RadiologyOrdersListDto dummyserviceCount = creditNoteList.stream()
								  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
								  .findAny()
								  .orElse(null);
						if(dummyserviceCount!=null){
							labSampleMasterDto.setCreditNoteId(dummyserviceCount.getCreditNoteId());
						}
					}*/
				//}
				 
				 List<RadiologyOrdersListDto> doctorShareList =  sessionFactory.getCurrentSession()
							.createSQLQuery(
									" select " + 
									 		"	    distinct (tds.ord_dtl_id) as \"orderDetailsId\", " + 
									 		"		tdsvd.doctor_share_id as \"doctorShareId\" " + 
									 		"	from " + 
									 		"		billing.t_doctor_share tds " + 
									 		"	inner join billing.t_doc_share_voucher_dtls tdsvd on " + 
									 		"		tds.doctor_share_id = tdsvd .doctor_share_id " + 
									 		"	where " + 
									 		"		tds.doc_id is not null " + 
									 		"	 and tds.ord_dtl_id in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
							.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
				 
					//if(doctorShareList != null && doctorShareList.size()>0){

						Map<Integer, Integer> doctorShareMap = doctorShareList.stream()
								.collect(Collectors.toMap(RadiologyOrdersListDto::getOrderDetailsId, RadiologyOrdersListDto::getDoctorShareId));


						/*for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
							RadiologyOrdersListDto dummyserviceCountA = doctorShareList.stream()
									  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
									  .findAny()
									  .orElse(null);
							if(dummyserviceCountA!=null){
								labSampleMasterDto.setDoctorShareId(dummyserviceCountA.getCreditNoteId());
							}
						}*/
					//}
					
					 List<RadiologyOrdersListDto> referalList =  sessionFactory.getCurrentSession()
								.createSQLQuery(
										" 	 select " + 
										"	distinct (orderdetails.order_details_id) as \"orderDetailsId\", " + 
										"    refM.referral_name AS \"referralName\", " + 
										"    refM.referral_id AS \"referralId\"  " + 
										"FROM " + 
										"    public.t_order_details AS orderdetails " + 
										"INNER JOIN " + 
										"    public.t_order_master AS ordermaster ON orderdetails.order_id = ordermaster.order_id " + 
										"LEFT JOIN " + 
										"    t_referal_details AS dtls1 ON " + 
										"    ( " + 
										"        (ordermaster.encounter_id IS NOT NULL AND dtls1.encounter_id = ordermaster.encounter_id) " + 
										"        OR dtls1.admission_id = ordermaster.admission_id " + 
										"    ) " + 
										"LEFT JOIN " + 
										"    adt.m_referral_master AS refM ON dtls1.referral_id = refM.referral_id  " + 
										"where " + 
										"	 orderdetails.order_details_id  in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
								.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
					 
					//	if(referalList != null && referalList.size()>0){

							Map<Integer, RadiologyOrdersListDto> referralMap = referalList.stream()
									.collect(Collectors.toMap(RadiologyOrdersListDto::getOrderDetailsId, dto -> dto));

						/*	for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
								RadiologyOrdersListDto dummyserviceCountRef = referalList.stream()
										  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
										  .findAny()
										  .orElse(null);
								if(dummyserviceCountRef!=null){
									labSampleMasterDto.setReferralId(dummyserviceCountRef.getReferralId());
									labSampleMasterDto.setReferralName(dummyserviceCountRef.getReferralName());
								}
							}*/
						//}

					// --- Merge Data into Original List ---
					for (RadiologyOrdersListDto dto : listServiceMasterDto) {
						Integer orderId = dto.getOrderDetailsId();
						if (creditNoteMap.containsKey(orderId)) {
							dto.setCreditNoteId(creditNoteMap.get(orderId));
						}
						if (doctorShareMap.containsKey(orderId)) {
							dto.setDoctorShareId(doctorShareMap.get(orderId));
						}
						if (referralMap.containsKey(orderId)) {
							RadiologyOrdersListDto ref = referralMap.get(orderId);
							dto.setReferralId(ref.getReferralId());
							dto.setReferralName(ref.getReferralName());
						}
					}
						
				}
				
					return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
				}else{
					return new Response<>(ERROR, ERR_500, "Radiology department is not configured in application configuration", null, null);
				}
		}catch (Exception e) {
		logger.error("getRadiologyResultEntrylist failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getradiologyReportReleaseWorkList(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			
				StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_RADIOLOGY_WORKLIST").getQueryString());
				query.append(" AND  orderdetails.service_rendered = 1 ");
				query.append(" AND templatetxn.template_transaction_id is not null and templatetxn.is_provisional_release='Y' and  (templatetxn.is_final_release is null or templatetxn.is_final_release='N') ");
			
				/*query.append(radiologyOrdersListDto.getFromDate() != null ? " AND rmppr.updated_date between '" + radiologyOrdersListDto.getFromDate().substring(0, 10)+" 00:00:00' and  '" + radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00' " :"");
				query.append(radiologyOrdersListDto.getPatientId() !=null && radiologyOrdersListDto.getPatientId() > 0 ? " and ordermaster.patient_id="+radiologyOrdersListDto.getPatientId() :"");			
				query.append(radiologyOrdersListDto.getServiceId()!=null && radiologyOrdersListDto.getServiceId() > 0 ? " and orderdetails.service_id="+radiologyOrdersListDto.getServiceId() : "" );			
			    query.append(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0 ? " and modalitymstr.modality_id IN :modalityList " :"");			 
			    query.append(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0 ? " and ordermaster.visit_type_id IN :visitTypeList " : "");
			    */

			if (radiologyOrdersListDto.getFromDate() != null && radiologyOrdersListDto.getToDate() != null) {
				query.append(" AND rmppr.updated_date BETWEEN :fromDate AND :toDate ");
			}
			if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
				query.append(" AND ordermaster.patient_id = :patientId ");
			}
			if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
				query.append(" AND orderdetails.service_id = :serviceId ");
			}
			if (radiologyOrdersListDto.getModalityList() != null && !radiologyOrdersListDto.getModalityList().isEmpty()) {
				query.append(" AND modalitymstr.modality_id IN (:modalityList) ");
			}
			if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
				query.append(" AND ordermaster.visit_type_id IN (:visitTypeList) ");
			}
				query.append(" order by \"isCritical\" desc ,\"orderDetailsId\" desc ");
			
				Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
					if (radiologyOrdersListDto.getFromDate() != null && radiologyOrdersListDto.getToDate() != null) {
						finalQuery.setParameter("fromDate", Timestamp.valueOf(radiologyOrdersListDto.getFromDate().substring(0, 10) + " 00:00:00"));
						finalQuery.setParameter("toDate", Timestamp.valueOf(radiologyOrdersListDto.getToDate().substring(0, 10) + " 23:59:59"));
					}
					if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
						finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());
					}
					if (radiologyOrdersListDto.getServiceId() != null && radiologyOrdersListDto.getServiceId() > 0) {
						finalQuery.setParameter("serviceId", radiologyOrdersListDto.getServiceId());
					}
					if(radiologyOrdersListDto.getModalityList()!=null && radiologyOrdersListDto.getModalityList().size() > 0)
					{
						finalQuery.setParameterList("modalityList", radiologyOrdersListDto.getModalityList());
					}
					if(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0)
					{
						finalQuery.setParameterList("visitTypeList", radiologyOrdersListDto.getVisitList());
					}
				
				if(radiologyOrdersListDto.getRadiologySpecialityId() != null){
					
					List<RadiologyOrdersListDto> listServiceMasterDto = finalQuery
							.setParameter("unitId", radiologyOrdersListDto.getUnitId())
							.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
							.setParameter("radiologySpecialityId", radiologyOrdersListDto.getRadiologySpecialityId())
							.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
					
					
					List<RadiologyOrdersListDto> list = listServiceMasterDto;
					List<Integer> OrderDetailsIds = list.stream().map(RadiologyOrdersListDto::getOrderDetailsId ).collect(Collectors.toList());
					if(!OrderDetailsIds.isEmpty() && OrderDetailsIds != null && OrderDetailsIds.size() > 0) {
					 String querys =" select\r\n" + 
					 		"		distinct (tds.order_detail_id) as \"orderDetailsId\", " + 
					 		"		tds.credit_note_id as  \"creditNoteId\"  " + 
					 		"	from " + 
					 		"		billing.t_credit_note_detail tds " + 
					 		"	inner join billing.m_credit_note_master crd on " + 
					 		"		crd.credit_note_id = tds.credit_note_id " + 
					 		"	where " + 
					 		"	 tds.status = 'A' " + 
					 		"	and crd.status = 'A' " + 
					 		"   and tds.order_detail_id in (:OrderDetailsIds)";
					 List<RadiologyOrdersListDto> creditNoteList = sessionFactory
						        .getCurrentSession()
						        .createSQLQuery(querys)
						        .addScalar("orderDetailsId")  // Assuming orderDetailsId is of type Long
						        .addScalar("creditNoteId")   // Assuming creditNoteId is of type Long
						        .setParameterList("OrderDetailsIds", OrderDetailsIds)
						        .setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class))
						        .list();
					  
					
					if(creditNoteList != null && creditNoteList.size()>0){
						for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
							RadiologyOrdersListDto dummyserviceCount = creditNoteList.stream()
									  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
									  .findAny()
									  .orElse(null);
							if(dummyserviceCount!=null){
								labSampleMasterDto.setCreditNoteId(dummyserviceCount.getCreditNoteId());
							}
						}
					}
					 
					 List<RadiologyOrdersListDto> doctorShareList =  sessionFactory.getCurrentSession()
								.createSQLQuery(
										" select " + 
										 		"	    distinct (tds.ord_dtl_id) as \"orderDetailsId\", " + 
										 		"		tdsvd.doctor_share_id as \"doctorShareId\" " + 
										 		"	from " + 
										 		"		billing.t_doctor_share tds " + 
										 		"	inner join billing.t_doc_share_voucher_dtls tdsvd on " + 
										 		"		tds.doctor_share_id = tdsvd .doctor_share_id " + 
										 		"	where " + 
										 		"		tds.doc_id is not null " + 
										 		"	 and tds.ord_dtl_id in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
								.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
					 
						if(doctorShareList != null && doctorShareList.size()>0){
							for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
								RadiologyOrdersListDto dummyserviceCountA = doctorShareList.stream()
										  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
										  .findAny()
										  .orElse(null);
								if(dummyserviceCountA!=null){
									labSampleMasterDto.setDoctorShareId(dummyserviceCountA.getCreditNoteId());
								}
							}
						}
						
						 List<RadiologyOrdersListDto> referalList =  sessionFactory.getCurrentSession()
									.createSQLQuery(
											" 	 select " + 
											"	distinct (orderdetails.order_details_id) as \"orderDetailsId\", " + 
											"    refM.referral_name AS \"referralName\", " + 
											"    refM.referral_id AS \"referralId\"  " + 
											"FROM " + 
											"    public.t_order_details AS orderdetails " + 
											"INNER JOIN " + 
											"    public.t_order_master AS ordermaster ON orderdetails.order_id = ordermaster.order_id " + 
											"LEFT JOIN " + 
											"    t_referal_details AS dtls1 ON " + 
											"    ( " + 
											"        (ordermaster.encounter_id IS NOT NULL AND dtls1.encounter_id = ordermaster.encounter_id) " + 
											"        OR dtls1.admission_id = ordermaster.admission_id " + 
											"    ) " + 
											"LEFT JOIN " + 
											"    adt.m_referral_master AS refM ON dtls1.referral_id = refM.referral_id  " + 
											"where " + 
											"	 orderdetails.order_details_id  in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
									.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
						 
							if(referalList != null && referalList.size()>0){
								for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
									RadiologyOrdersListDto dummyserviceCountRef = referalList.stream()
											  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
											  .findAny()
											  .orElse(null);
									if(dummyserviceCountRef!=null){
										labSampleMasterDto.setReferralId(dummyserviceCountRef.getReferralId());
										labSampleMasterDto.setReferralName(dummyserviceCountRef.getReferralName());
									}
								}
							}
							
					}
					
					return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
					
				}else{
					return new Response<>(ERROR, ERR_500, "Radiology department is not configured in application configuration", null, null);
				}
					
				}catch (Exception e) {
				 throw e;	
				}
//			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getradiologyWorkListForPrinting(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
			try {
						
			
					StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_RADIOLOGY_WORKLIST").getQueryString());
					
					query.append(" AND  orderdetails.service_rendered = 1 ");
					query.append(" AND templatetxn.template_transaction_id is not null and templatetxn.is_final_release='Y' ");
					
					/*query.append( radiologyOrdersListDto.getFromDate() != null ? " AND rmppr.updated_date between '" + radiologyOrdersListDto.getFromDate().substring(0, 10)+" 00:00:00' and  '" + radiologyOrdersListDto.getToDate().substring(0, 10) + " 24:00:00' " :" ");
					query.append( radiologyOrdersListDto.getPatientId() !=null && radiologyOrdersListDto.getPatientId() > 0 ? " and ordermaster.patient_id="+radiologyOrdersListDto.getPatientId() : "");
					query.append(radiologyOrdersListDto.getDeptList()!=null && radiologyOrdersListDto.getDeptList().size() > 0 ? " and orderdetails.service_rendering_deptid IN :deptList " : "");
					query.append(radiologyOrdersListDto.getServiceList()!=null && radiologyOrdersListDto.getServiceList().size() > 0 ? " and orderdetails.service_id IN :serviceList " : "");					
					query.append(radiologyOrdersListDto.getVisitList()!=null && radiologyOrdersListDto.getVisitList().size() > 0 ? " and ordermaster.visit_type_id IN :visitList " :"" );					
					*/

				if (radiologyOrdersListDto.getFromDate() != null) {
					query.append(" AND to_char(rmppr.updated_date,'YYYY-MM-DD') >=:fromDt ");
				}
				if (radiologyOrdersListDto.getToDate() != null ) {
					query.append(" AND to_char(rmppr.updated_date,'YYYY-MM-DD') <=:toDt ");
				}
				if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
					query.append(" AND ordermaster.patient_id = :patientId ");
				}
				if (radiologyOrdersListDto.getDeptList() != null && !radiologyOrdersListDto.getDeptList().isEmpty()) {
					query.append(" AND orderdetails.service_rendering_deptid IN (:deptList) ");
				}
				if (radiologyOrdersListDto.getServiceList() != null && !radiologyOrdersListDto.getServiceList().isEmpty()) {
					query.append(" AND orderdetails.service_id IN (:serviceList) ");
				}
				if (radiologyOrdersListDto.getVisitList() != null && !radiologyOrdersListDto.getVisitList().isEmpty()) {
					query.append(" AND ordermaster.visit_type_id IN (:visitList) ");
				}


				query.append(" order by \"isCritical\" desc ,\"orderDetailsId\" desc ");
					
					Query finalQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString());

				if (radiologyOrdersListDto.getFromDate() != null) {
					finalQuery.setParameter("fromDt", radiologyOrdersListDto.getFromDate());}
				if (radiologyOrdersListDto.getToDate() != null ){
					finalQuery.setParameter("toDt", radiologyOrdersListDto.getToDate());}
				if (radiologyOrdersListDto.getPatientId() != null && radiologyOrdersListDto.getPatientId() > 0) {
					finalQuery.setParameter("patientId", radiologyOrdersListDto.getPatientId());
				}

					if(radiologyOrdersListDto.getDeptList()!=null && radiologyOrdersListDto.getDeptList().size() > 0)
					{
						finalQuery.setParameterList("deptList", radiologyOrdersListDto.getDeptList());
					}
					if(radiologyOrdersListDto.getServiceList()!=null && radiologyOrdersListDto.getServiceList().size() > 0)
					{
						finalQuery.setParameterList("serviceList", radiologyOrdersListDto.getServiceList());
					}
					if(radiologyOrdersListDto.getVisitList() !=null && radiologyOrdersListDto.getVisitList().size() > 0)
					{
						finalQuery.setParameterList("visitList", radiologyOrdersListDto.getVisitList());
					}
					
					if(radiologyOrdersListDto.getRadiologySpecialityId() != null){
						
					List<RadiologyOrdersListDto> listServiceMasterDto = finalQuery
					.setParameter("unitId", radiologyOrdersListDto.getUnitId())
					.setParameter("orgId", radiologyOrdersListDto.getOrganizationId())
					.setParameter("radiologySpecialityId", radiologyOrdersListDto.getRadiologySpecialityId())
					.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
					
					
					
					List<RadiologyOrdersListDto> list = listServiceMasterDto;
					List<Integer> OrderDetailsIds = list.stream().map(RadiologyOrdersListDto::getOrderDetailsId ).collect(Collectors.toList());
					if(!OrderDetailsIds.isEmpty() && OrderDetailsIds != null && OrderDetailsIds.size() > 0) {
					 String querys =" select\r\n" + 
					 		"		distinct (tds.order_detail_id) as \"orderDetailsId\", " + 
					 		"		tds.credit_note_id as  \"creditNoteId\"  " + 
					 		"	from " + 
					 		"		billing.t_credit_note_detail tds " + 
					 		"	inner join billing.m_credit_note_master crd on " + 
					 		"		crd.credit_note_id = tds.credit_note_id " + 
					 		"	where " + 
					 		"	 tds.status = 'A' " + 
					 		"	and crd.status = 'A' " + 
					 		"   and tds.order_detail_id in (:OrderDetailsIds)";
					 List<RadiologyOrdersListDto> creditNoteList = sessionFactory
						        .getCurrentSession()
						        .createSQLQuery(querys)
						        .addScalar("orderDetailsId")  // Assuming orderDetailsId is of type Long
						        .addScalar("creditNoteId")   // Assuming creditNoteId is of type Long
						        .setParameterList("OrderDetailsIds", OrderDetailsIds)
						        .setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class))
						        .list();
					  
					
					if(creditNoteList != null && creditNoteList.size()>0){
						for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
							RadiologyOrdersListDto dummyserviceCount = creditNoteList.stream()
									  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
									  .findAny()
									  .orElse(null);
							if(dummyserviceCount!=null){
								labSampleMasterDto.setCreditNoteId(dummyserviceCount.getCreditNoteId());
							}
						}
					}
					 
					 List<RadiologyOrdersListDto> doctorShareList =  sessionFactory.getCurrentSession()
								.createSQLQuery(
										" select " + 
										 		"	    distinct (tds.ord_dtl_id) as \"orderDetailsId\", " + 
										 		"		tdsvd.doctor_share_id as \"doctorShareId\" " + 
										 		"	from " + 
										 		"		billing.t_doctor_share tds " + 
										 		"	inner join billing.t_doc_share_voucher_dtls tdsvd on " + 
										 		"		tds.doctor_share_id = tdsvd .doctor_share_id " + 
										 		"	where " + 
										 		"		tds.doc_id is not null " + 
										 		"	 and tds.ord_dtl_id in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
								.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
					 
						if(doctorShareList != null && doctorShareList.size()>0){
							for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
								RadiologyOrdersListDto dummyserviceCountA = doctorShareList.stream()
										  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
										  .findAny()
										  .orElse(null);
								if(dummyserviceCountA!=null){
									labSampleMasterDto.setDoctorShareId(dummyserviceCountA.getCreditNoteId());
								}
							}
						}
						
						 List<RadiologyOrdersListDto> referalList =  sessionFactory.getCurrentSession()
									.createSQLQuery(
											" 	 select " + 
											"	distinct (orderdetails.order_details_id) as \"orderDetailsId\", " + 
											"    refM.referral_name AS \"referralName\", " + 
											"    refM.referral_id AS \"referralId\"  " + 
											"FROM " + 
											"    public.t_order_details AS orderdetails " + 
											"INNER JOIN " + 
											"    public.t_order_master AS ordermaster ON orderdetails.order_id = ordermaster.order_id " + 
											"LEFT JOIN " + 
											"    t_referal_details AS dtls1 ON " + 
											"    ( " + 
											"        (ordermaster.encounter_id IS NOT NULL AND dtls1.encounter_id = ordermaster.encounter_id) " + 
											"        OR dtls1.admission_id = ordermaster.admission_id " + 
											"    ) " + 
											"LEFT JOIN " + 
											"    adt.m_referral_master AS refM ON dtls1.referral_id = refM.referral_id  " + 
											"where " + 
											"	 orderdetails.order_details_id  in ("+OrderDetailsIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ")
									.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).list();
						 
							if(referalList != null && referalList.size()>0){
								for (RadiologyOrdersListDto labSampleMasterDto : listServiceMasterDto) {
									RadiologyOrdersListDto dummyserviceCountRef = referalList.stream()
											  .filter(order -> labSampleMasterDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
											  .findAny()
											  .orElse(null);
									if(dummyserviceCountRef!=null){
										labSampleMasterDto.setReferralId(dummyserviceCountRef.getReferralId());
										labSampleMasterDto.setReferralName(dummyserviceCountRef.getReferralName());
									}
								}
							}
							
					}
					
					return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
			
					}else{
						return new Response<>(ERROR, ERR_500, "Radiology department is not configured in application configuration", null, null);
					}
			
		}catch (Exception e) {
		logger.error("getradiologyWorkListForPrinting failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	@Override
	public ByteArrayOutputStream printRadiologyReport(RadiologyOrdersListDto dto) throws ApplicationException {
		try {
			
			PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
			/*String query = "select template_object from t_template_transaction ttt where template_transaction_id ="+dto.getTemplateTransactionId();
			
			String tempObj  = (String)sessionFactory.getCurrentSession().createSQLQuery(query).uniqueResult();
				dto.setTemplateContent(tempObj);*/
			
			
			RadiologyOrdersListDto header = (RadiologyOrdersListDto)sessionFactory.getCurrentSession().createSQLQuery(
				"select header as \"header\", footer as \"footer\" , signture_img as \"signtureImg\" , latter_head_img as \"latterHeadImg\" "
				+ "from  public.m_report_header where organization_id = "+dto.getOrganizationId()
				+ " and unit_id = " + dto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).uniqueResult();
			
			   ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
			   PdfWriter pdfWriter = null;
			   String referedName = "     -";
			   if(dto.getReferralName()!= null) {
				   referedName = dto.getReferralName();
			   }
			   
			   String latter = "<br><br><br><br>";
			   String body = dto.getTemplateContent()+"<br>";
			   //body = body.replace("<li>","<li>&bull;   ");
			   
			   Map<String,String> qMapResultsHearder = new HashMap<String, String>();
			   Map<String,String> qMapResultsFirst=null;
			   Map<String,String> qMapResultsFooter = null;
			   
			    String   patientHeader = header.getHeader();
			    if(patientHeader == null) {
			    	patientHeader = "";
			    }else {
				   
			    	
                    patientHeader = patientHeader.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
                    	qMapResultsHearder.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
                    patientHeader = patientHeader.replace("@date",dto.getOrderDate());
                    	qMapResultsHearder.put("@date", dto.getOrderDate());
                    patientHeader = patientHeader.replace("@uhid",dto.getUhidNumber());
                    	qMapResultsHearder.put("@uhid", dto.getUhidNumber());
                    patientHeader = patientHeader.replace("@releaseDate",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy"));
                    	qMapResultsHearder.put("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy"));
                    patientHeader = patientHeader.replace("@age",dto.getAgeString());
                    	qMapResultsHearder.put("@age", dto.getAgeString());
                    patientHeader = patientHeader.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
                    	qMapResultsHearder.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
                    patientHeader = patientHeader.replace("@referral",referedName);
                    	qMapResultsHearder.put("@referral",referedName);
                    patientHeader = patientHeader.replace("@visitNo",dto.getVisitNumber());
				    	qMapResultsHearder.put("@visitNo",dto.getVisitNo());
				    patientHeader = patientHeader.replace("@orderNo",dto.getOrderNo());
				    	qMapResultsHearder.put("@orderNo",dto.getOrderNo());
				    	
				    Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(patientHeader);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{	
							    consentMapperDto.setPatientId(dto.getPatientId());
							    consentMapperDto.setEncounterId(dto.getEncounterId());
							    consentMapperDto.setAdmissionId(dto.getAdmissionId());
							    consentMapperDto.setAdmissionId(dto.getAdmissionId());
							    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
							    consentMapperDto.setTemplateDesc(patientHeader);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFirst = (Map<String, String>) res.getResObject();
							for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
							{
								qMapResultsHearder.put(itr.getKey(), itr.getValue());
							}
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							patientHeader = consentMapperDto.getTemplateDesc();
						}
					}
			    }
			   
			    String footer = header.getFooter();
			    if(footer == null) {
			    	footer = "";
			    }else {
			    	footer = footer.replace("@signatureImg",header.getSigntureImg());
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(footer);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{
							Integer count = 0;
							for(String q : paramNames)
							{
								if (qMapResultsHearder.containsKey(q))
								{
									footer = footer.replace(q, qMapResultsHearder.get(q));
								}else{
									count++;
								}
							}
							if(count > 0)
							{
									consentMapperDto.setTemplateDesc(footer);
								Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
								qMapResultsFooter = (Map<String, String>) res.getResObject(); 
								consentMapperDto = (PatientConsentMapperDto) res.getObject();
								footer = consentMapperDto.getTemplateDesc();
								
								for(Map.Entry<String,String> itr : qMapResultsFooter.entrySet())
								{
									qMapResultsHearder.put(itr.getKey(), itr.getValue());
								}
							}
							
						}
					}	
			    }
			   
			    if(dto.getTemplateContent()!=null)
			    {
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(dto.getTemplateContent());
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{
							Integer count = 0;
							String tempContent = dto.getTemplateContent();
							for(String q : paramNames)
							{
								if (qMapResultsHearder.containsKey(q))
								{
									tempContent = tempContent.replace(q, qMapResultsHearder.get(q));
								}else{
									count++;
								}
							}
							if(count > 0)
							{
									consentMapperDto.setTemplateDesc(tempContent);
								Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
								consentMapperDto = (PatientConsentMapperDto) res.getObject();
								tempContent = consentMapperDto.getTemplateDesc();
							}
							dto.setTemplateContent(tempContent);
							body = tempContent; 
						}
					}	
			    }
			    
			    
				try {
												
						StringBuffer sbHeader = new StringBuffer(patientHeader);
						StringBuffer sbBody = new StringBuffer(body);
						StringBuffer sbFooter = new StringBuffer(footer);
						StringBuffer sbBr = new StringBuffer(latter);
				        Document documentPDF =new Document();
						pdfWriter = PdfWriter.getInstance(documentPDF,byteStream);
				        pdfWriter.setLinearPageMode();
				        pdfWriter.setFullCompression();
				        documentPDF.open();
				        
				        HTMLWorker htmlWorker = new HTMLWorker(documentPDF);
				        
				        
				        if(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="")
				        {
				        	latter = header.getLatterHeadImg();
				        	StringReader laImg = new StringReader(latter.toString());
					        
					        htmlWorker.parse(laImg);
				        }
				        else
			        	{
				        	StringReader srBr = new StringReader(sbBr.toString());
				        	htmlWorker.parse(srBr);
					        srBr.close();
			        	}
				        
				        StringReader srHeader = new StringReader(sbHeader.toString());
				       
				        LineSeparator lsp= new LineSeparator();
				        if(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="")
				        	lsp.setOffset(-1);
				        else
				        	lsp.setOffset(-15);
				        
				        
				        Chunk linebreak = new Chunk(lsp);
				        documentPDF.add(linebreak);
				        htmlWorker.parse(srHeader);
				        srHeader.close();
				        
				        StringReader srBody = new StringReader(sbBody.toString());
				        	
				        LineSeparator lsp1= new LineSeparator();
				        lsp.setOffset(-15);
				        Chunk linebreak1 = new Chunk(lsp1);
				        documentPDF.add(linebreak1);
				        htmlWorker.parse(srBody);
				        srBody.close();
				        
				        StringReader srFooter = new StringReader(sbFooter.toString());
				        htmlWorker.parse(srFooter);
				        LineSeparator lsp2= new LineSeparator();
				        lsp.setOffset(-15);
				        Chunk linebreak2 = new Chunk(lsp2);
				        documentPDF.add(linebreak2);
				        srFooter.close();
				        
				        htmlWorker.close();
				        documentPDF.close();
			            pdfWriter.close();
			            byteStream.close();
				    } catch (FileNotFoundException e) {
				    logger.error("printRadiologyReport failed", e);
				    } catch (IOException e) {
				    logger.error("printRadiologyReport failed", e);
				    } catch (DocumentException e) {
				    logger.error("printRadiologyReport failed", e);
				    }
					return byteStream;
				
			}catch (HibernateException he) {
			logger.error("printRadiologyReport failed", he);
			} catch (Exception e) {
			logger.error("printRadiologyReport failed", e);
			}
		return null;
	}

	
	@Override
	public ByteArrayOutputStream labPrintReport(RadiologyOrdersListDto dto) throws ApplicationException {
		try {
			
				PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
				/*String query = "select template_object from t_template_transaction ttt where template_transaction_id ="+dto.getTemplateTransactionId();
			
				String tempObj  = (String)sessionFactory.getCurrentSession().createSQLQuery(query).uniqueResult();
				dto.setTemplateContent(tempObj);*/
			
			
				RadiologyOrdersListDto header = (RadiologyOrdersListDto)sessionFactory.getCurrentSession().createSQLQuery(
						"select lab_header as \"header\", lab_footer as \"footer\" , signture_img as \"signtureImg\" , latter_head_img as \"latterHeadImg\" "
						+ "from  public.m_report_header where organization_id = "+dto.getOrganizationId()
						+ " and unit_id = " + dto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).uniqueResult();
			
			   ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
			   PdfWriter pdfWriter = null;
			   String referedName = "     -";
			   if(dto.getReferralName()!= null) {
				   referedName = dto.getReferralName();
			   }
			   
			   String latter = "<br><br><br><br>";
			   String body = dto.getTemplateContent()+"<br>";
			   body = body.replace("<li>","<li>&bull;   ");
			   
			   Map<String,String> qMapResultsHearder = new HashMap<String, String>();
			   Map<String,String> qMapResultsFirst=null;
			   Map<String,String> qMapResultsFooter = null;
			   
			    String   patientHeader = header.getHeader();
			    if(patientHeader == null) {
			    	patientHeader = "";
			    }else {
				   
			    	
			    	patientHeader = patientHeader.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
			    		qMapResultsHearder.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
			        patientHeader = patientHeader.replace("@date",dto.getOrderDate());
			        	qMapResultsHearder.put("@date", dto.getOrderDate());
			        patientHeader = patientHeader.replace("@uhid",dto.getUhidNumber());
			        	qMapResultsHearder.put("@uhid", dto.getUhidNumber());
			        patientHeader = patientHeader.replace("@releaseDate",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy"));
			        	qMapResultsHearder.put("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy"));
			        patientHeader = patientHeader.replace("@age",dto.getAgeString());
			        	qMapResultsHearder.put("@age", dto.getAgeString());
			        patientHeader = patientHeader.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
			        	qMapResultsHearder.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
			        patientHeader = patientHeader.replace("@referral",referedName);
			        	qMapResultsHearder.put("@referral",referedName);
			        patientHeader = patientHeader.replace("@visitNo",dto.getVisitNo());
			        	qMapResultsHearder.put("@visitNo",dto.getVisitNo());
			        patientHeader = patientHeader.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
			        	qMapResultsHearder.put("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
			        patientHeader = patientHeader.replace("@sampleNo",dto.getLabSampleNo());
			        	qMapResultsHearder.put("@sampleNo",dto.getLabSampleNo());
			        patientHeader = patientHeader.replace("@orderNo",dto.getOrderNo());
			        	qMapResultsHearder.put("@orderNo",dto.getOrderNo());
				    	
				    Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(patientHeader);
					if(matcher!=null)
					{
						while (matcher.find())
						{
						    paramNames.add(matcher.group());
						}
						if(paramNames.size()>0)
						{	
							    consentMapperDto.setPatientId(dto.getPatientId());
							    consentMapperDto.setEncounterId(dto.getEncounterId());
							    consentMapperDto.setAdmissionId(dto.getAdmissionId());
							    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
							    consentMapperDto.setTemplateDesc(patientHeader);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFirst = (Map<String, String>) res.getResObject();
							for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
							{
								qMapResultsHearder.put(itr.getKey(), itr.getValue());
							}
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							patientHeader = consentMapperDto.getTemplateDesc();
						}
					}
					
			    }
			   
			    String footer = header.getFooter();
			    if(footer == null) {
			    	footer = "";
			    }else {
			    	footer = footer.replace("@signatureImg",header.getSigntureImg());
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(footer);
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{
						Integer count = 0;
						for(String q : paramNames)
						{
							if (qMapResultsHearder.containsKey(q))
							{
								footer = footer.replace(q, qMapResultsHearder.get(q));
							}else{
								count++;
							}
						}
						if(count > 0)
						{
								consentMapperDto.setTemplateDesc(footer);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFooter = (Map<String, String>) res.getResObject(); 
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							footer = consentMapperDto.getTemplateDesc();
							
							for(Map.Entry<String,String> itr : qMapResultsFooter.entrySet())
							{
								qMapResultsHearder.put(itr.getKey(), itr.getValue());
							}
						}
						
					}
			    }
			   
			    if(dto.getTemplateContent()!=null)
			    {
			    	Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(dto.getTemplateContent());
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{
						Integer count = 0;
						String tempContent = dto.getTemplateContent();
						for(String q : paramNames)
						{
							if (qMapResultsHearder.containsKey(q))
							{
								tempContent = tempContent.replace(q, qMapResultsHearder.get(q));
							}else{
								count++;
							}
						}
						if(count > 0)
						{
								consentMapperDto.setTemplateDesc(tempContent);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							tempContent = consentMapperDto.getTemplateDesc();
						}
						dto.setTemplateContent(tempContent);
						body = tempContent; 
					}
			    }
			    
			    
				try {
												
						StringBuffer sbHeader = new StringBuffer(patientHeader);
						StringBuffer sbBody = new StringBuffer(body);
						StringBuffer sbFooter = new StringBuffer(footer);
						StringBuffer sbBr = new StringBuffer(latter);
				        Document documentPDF =new Document();
						pdfWriter = PdfWriter.getInstance(documentPDF,byteStream);
				        pdfWriter.setLinearPageMode();
				        pdfWriter.setFullCompression();
				        documentPDF.open();
				        
				        HTMLWorker htmlWorker = new HTMLWorker(documentPDF);
				        
				        
				        if(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="")
				        {
				        	latter = header.getLatterHeadImg();
				        	StringReader laImg = new StringReader(latter.toString());
					        
					        htmlWorker.parse(laImg);
				        }
				        else
			        	{
				        	StringReader srBr = new StringReader(sbBr.toString());
				        	htmlWorker.parse(srBr);
					        srBr.close();
			        	}
				        
				        StringReader srHeader = new StringReader(sbHeader.toString());
				       
				        LineSeparator lsp= new LineSeparator();
				        if(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="")
				        	lsp.setOffset(-1);
				        else
				        	lsp.setOffset(-15);
				        
				        
				        Chunk linebreak = new Chunk(lsp);
				        documentPDF.add(linebreak);
				        htmlWorker.parse(srHeader);
				        srHeader.close();
				        
				        StringReader srBody = new StringReader(sbBody.toString());
				        	
				        LineSeparator lsp1= new LineSeparator();
				        lsp.setOffset(-15);
				        Chunk linebreak1 = new Chunk(lsp1);
				        documentPDF.add(linebreak1);
				        htmlWorker.parse(srBody);
				        srBody.close();
				        
				        StringReader srFooter = new StringReader(sbFooter.toString());
				        htmlWorker.parse(srFooter);
				        LineSeparator lsp2= new LineSeparator();
				        lsp.setOffset(-15);
				        Chunk linebreak2 = new Chunk(lsp2);
				        documentPDF.add(linebreak2);
				        srFooter.close();
				        
				        htmlWorker.close();
				        documentPDF.close();
			            pdfWriter.close();
			            byteStream.close();
				    } catch (FileNotFoundException e) {
				    logger.error("labPrintReport failed", e);
				    } catch (IOException e) {
				    logger.error("labPrintReport failed", e);
				    } catch (DocumentException e) {
				    logger.error("labPrintReport failed", e);
				    }
					return byteStream;
				
			}catch (HibernateException he) {
			logger.error("labPrintReport failed", he);
			} catch (Exception e) {
			logger.error("labPrintReport failed", e);
			}
		return null;
	}
	@Override
	public Response printReportTest(RadiologyOrdersListDto dto) {
	try{
		
		PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
		/*String query = "select template_object from t_template_transaction ttt where template_transaction_id ="+dto.getTemplateTransactionId();
		
		String tempObj  = (String)sessionFactory.getCurrentSession().createSQLQuery(query).uniqueResult();
			dto.setTemplateContent(tempObj);*/
		
		
		RadiologyOrdersListDto header = (RadiologyOrdersListDto)sessionFactory.getCurrentSession().createSQLQuery(
			"select header as \"header\", footer as \"footer\" , signture_img as \"signtureImg\" , latter_head_img as \"latterHeadImg\","
		    + " footer_img as \"footerImg\", mail_footer as \"mailFooter\", header_without_img as \"headerWithoutImg\", "
		    + " header_pdf as \"headerPdf\", letter_head_image_pdf as \"letterHeadImagePdf\" "
			+ "from  public.m_report_header where organization_id = "+dto.getOrganizationId()
			+ " and unit_id = " + dto.getUnitId())
			.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).uniqueResult();

		RadiologyOrdersListDto skipRISValidationFlag = (RadiologyOrdersListDto)sessionFactory.getCurrentSession().createSQLQuery(
				"select tuabc.skip_ris_report_entry as \"isSkipRISResultValidation\" from"
				+	" t_unit_application_billing_configuration tuabc where tuabc.organization_id=:OrgId "
				+	" and tuabc.unit_id=:unitId")
				.setParameter("OrgId", dto.getOrganizationId())
				.setParameter("unitId", dto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).uniqueResult();
		
		   ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
		   PdfWriter pdfWriter = null;
		   String referedName = "     -";
		   if(dto.getReferralName()!= null) {
			   referedName = dto.getReferralName();
		   }
		   
		   String latter = "<br><br><br><br>";
		   String body = dto.getTemplateContent()+"<br>";
		   //body = body.replace("<li>","<li>&bull;   ");
		   
		   Map<String,String> qMapResultsHearder = new HashMap<String, String>();
		   Map<String,String> qMapResultsHearderWithoutImg = new HashMap<String, String>();
		   Map<String,String> qMapResultsFirst=null;
		   Map<String,String> qMapResultsFooter = null;
		   String reportStatus = "Initiate";
           if(dto.getIsProvisionalRelease() == null && dto.getIsFinalRelease()== null){
               reportStatus = "Initiate";
           }else if((dto.getIsProvisionalRelease() == 'N' || dto.getIsProvisionalRelease() == null) && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N') ){
               reportStatus = "Initiate";
           }else if(dto.getIsProvisionalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N')){
               reportStatus = "Provisional";
           }else if(dto.getIsFinalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N') ){
               reportStatus = "Final";
           }else if(dto.getIsFinalRelease() == 'Y' && dto.getIsProvisionalRelease()== 'Y') {
               reportStatus = "Final";
           }	   	
		    String   patientHeader = header.getHeader();
			String   patientHeaderPdf = header.getHeaderPdf();

		    if(patientHeader == null) {
		    	patientHeader = "";
		    }else {

				if(dto.getIsProvisionalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N')){
					patientHeader = patientHeader.replace("@isProvisionalRelease", "Provisional Release");
					qMapResultsHearder.put("@isProvisionalRelease", "Provisional Release");

				}else{
					patientHeader = patientHeader.replace("@isProvisionalRelease", "");
					qMapResultsHearder.put("@isProvisionalRelease", "");
				}

				if(skipRISValidationFlag.getIsSkipRISResultValidation() == 'Y' && skipRISValidationFlag.getIsSkipRISResultValidation() != null){

					patientHeader = patientHeader.replace("@releaseDate", "@date");
					qMapResultsHearder.put("@releaseDate", "@date");

					patientHeader = patientHeader.replace("@reportValidationDate", "@date");
					qMapResultsHearder.put("@reportValidationDate", "@date");

				}

		    	patientHeader = patientHeader.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
            	qMapResultsHearder.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
//            patientHeader = patientHeader.replace("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate() );
//            	qMapResultsHearder.put("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate());
            patientHeader = patientHeader.replace("@uhid",dto.getUhidNumber());
            	qMapResultsHearder.put("@uhid", dto.getUhidNumber());
//            patientHeader = patientHeader.replace("@releaseDate",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
//            	qMapResultsHearder.put("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
            patientHeader = patientHeader.replace("@age",dto.getAgeString());
            	qMapResultsHearder.put("@age", dto.getAgeString());
            patientHeader = patientHeader.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            	qMapResultsHearder.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            patientHeader = patientHeader.replace("@referral",referedName);
            	qMapResultsHearder.put("@referral",referedName);
            patientHeader = patientHeader.replace("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo() : dto.getVisitNumber());
		    	qMapResultsHearder.put("@visitNo",dto.getVisitNo());
		    patientHeader = patientHeader.replace("@orderNo",dto.getOrderNo());
		    	qMapResultsHearder.put("@orderNo",dto.getOrderNo());
		    patientHeader = patientHeader.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
		        	qMapResultsHearder.put("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
	
		    patientHeader = patientHeader.replace("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
				   	qMapResultsHearder.put("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
				   	
		    patientHeader = patientHeader.replace("@reportStatus",reportStatus);
		        	qMapResultsHearder.put("@reportStatus",reportStatus);
		    patientHeader = patientHeader.replace("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
			        	qMapResultsHearder.put("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");	   	
			    	
			
			if((patientHeader.contains("@ward") || patientHeader.contains("@floor") || patientHeader.contains("@bed") 
					|| patientHeader.contains("@consultingDrName")) && (dto.getOrderDetailsId()!=null)) {
				//get wardNAme and bed details by order Details Id
		    	 Response wardRes = iEncounterMasterService.getWardAndFloorAndConsultingDoctorDetails(dto);		
		    	 if(wardRes.getObject()!=null) {
		    		 AdmissionDto adtDto = (AdmissionDto)wardRes.getObject();
		    		 if(patientHeader.contains("@ward")) {
	 			    	  patientHeader = patientHeader.replace("@ward",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
	 			    	  qMapResultsHearder.put("@ward",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
	 			      }
	 			      
	 			      if(patientHeader.contains("@floor")) {
	 			    	  patientHeader = patientHeader.replace("@floor",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
	 			    	  qMapResultsHearder.put("@floor",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
	 			      }
	 			      
	 			      if(patientHeader.contains("@bed")) {
	 			    	  patientHeader = patientHeader.replace("@bed", adtDto.getBedNumber()!= null ?adtDto.getBedNumber(): "-");
	 			    	  qMapResultsHearder.put("@bed",adtDto.getBedNumber() != null ?adtDto.getBedNumber(): "-");
	 			      }
	 			      
	 			      if(patientHeader.contains("@consultingDrName")) {
	 			    	  patientHeader = patientHeader.replace("@consultingDrName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
	 			    	  qMapResultsHearder.put("@consultingDrName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
	 			      }
		    	 }else {
		    		 if(patientHeader.contains("@ward")) {
	 			    	  patientHeader = patientHeader.replace("@ward", "-");
	 			    	  qMapResultsHearder.put("@ward", "-");
	 			      }
	 			      
	 			      if(patientHeader.contains("@floor")) {
	 			    	  patientHeader = patientHeader.replace("@floor", "-");
	 			    	  qMapResultsHearder.put("@floor", "-");
	 			      }
	 			      
	 			      
	 			      if(patientHeader.contains("@bed")) {
	 			    	  patientHeader = patientHeader.replace("@bed",  "-");
	 			    	  qMapResultsHearder.put("@bed","-");
	 			      }
	 			      
	 			      if(patientHeader.contains("@consultingDrName")) {
	 			    	  patientHeader = patientHeader.replace("@consultingDrName", "-");
	 			    	  qMapResultsHearder.put("@consultingDrName","-");
	 			      }
		    	 }
			}else {
				if(patientHeader.contains("@ward")) {
			    	  patientHeader = patientHeader.replace("@ward", "-");
			    	  qMapResultsHearder.put("@ward", "-");
			      }
			      
			      if(patientHeader.contains("@floor")) {
			    	  patientHeader = patientHeader.replace("@floor", "-");
			    	  qMapResultsHearder.put("@floor", "-");
			      }
			      
			      
			      if(patientHeader.contains("@bed")) {
			    	  patientHeader = patientHeader.replace("@bed",  "-");
			    	  qMapResultsHearder.put("@bed","-");
			      }
			      
			      if(patientHeader.contains("@consultingDrName")) {
			    	  patientHeader = patientHeader.replace("@consultingDrName", "-");
			    	  qMapResultsHearder.put("@consultingDrName","-");
			      }
			}
				
			    Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(patientHeader);
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{	
						    consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
						    consentMapperDto.setTemplateDesc(patientHeader);
						Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
						qMapResultsFirst = (Map<String, String>) res.getResObject();
						for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
						{
							qMapResultsHearder.put(itr.getKey(), itr.getValue());
						}
						consentMapperDto = (PatientConsentMapperDto) res.getObject();
						patientHeader = consentMapperDto.getTemplateDesc();
					}
				}
		    }
		   
		    // patient header without letter img
		    
		    String   patientHeaderWithoutImg = header.getHeaderWithoutImg();
		    if(patientHeaderWithoutImg == null) {
		    	patientHeaderWithoutImg = "";
		    }else {
			   
		    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
		    		qMapResultsHearderWithoutImg.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
//		    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate() );
//            		qMapResultsHearderWithoutImg.put("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate());
            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@uhid",dto.getUhidNumber());
            		qMapResultsHearderWithoutImg.put("@uhid", dto.getUhidNumber());
//            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
//            		qMapResultsHearderWithoutImg.put("@releaseDate",  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@age",dto.getAgeString());
            		qMapResultsHearderWithoutImg.put("@age", dto.getAgeString());
            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            		qMapResultsHearderWithoutImg.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@referral",referedName);
            		qMapResultsHearderWithoutImg.put("@referral",referedName);
            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo() : dto.getVisitNumber());
            		qMapResultsHearderWithoutImg.put("@visitNo",dto.getVisitNo());
            	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@orderNo",dto.getOrderNo());
		    		qMapResultsHearderWithoutImg.put("@orderNo",dto.getOrderNo());
		    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
		    		qMapResultsHearderWithoutImg.put("@printTime", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
	
		    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
		    		qMapResultsHearderWithoutImg.put("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
                	   	
		    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@reportStatus",reportStatus);
		    		qMapResultsHearderWithoutImg.put("@reportStatus",reportStatus);
		    	patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
		    		qMapResultsHearderWithoutImg.put("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");	   	
			    	
		    		
		    		
		    		if((patientHeaderWithoutImg.contains("@ward") || patientHeaderWithoutImg.contains("@floor") || patientHeaderWithoutImg.contains("@bed") 
							|| patientHeaderWithoutImg.contains("@consultingDrName")) && (dto.getOrderDetailsId()!=null)) {
						//get wardNAme and bed details by order Details Id
				    	 Response wardRes = iEncounterMasterService.getWardAndFloorAndConsultingDoctorDetails(dto);		
				    	 if(wardRes.getObject()!=null) {
				    		 AdmissionDto adtDto = (AdmissionDto)wardRes.getObject();
				    		 if(patientHeaderWithoutImg.contains("@ward")) {
				    			 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@ward",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
				    			 qMapResultsHearderWithoutImg.put("@ward",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
			 			      }
			 			      
			 			      if(patientHeaderWithoutImg.contains("@floor")) {
			 			    	 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@floor",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
			 			    	qMapResultsHearderWithoutImg.put("@floor",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
			 			      }
			 			      
			 			      if(patientHeaderWithoutImg.contains("@bed")) {
			 			    	 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@bed", adtDto.getBedNumber()!= null ?adtDto.getBedNumber(): "-");
			 			    	qMapResultsHearderWithoutImg.put("@bed",adtDto.getBedNumber() != null ?adtDto.getBedNumber(): "-");
			 			      }
			 			      
			 			      if(patientHeaderWithoutImg.contains("@consultingDrName")) {
			 			    	 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@consultingDrName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
			 			    	qMapResultsHearderWithoutImg.put("@consultingDrName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
			 			      }
				    	 }else {
				    		 if(patientHeaderWithoutImg.contains("@ward")) {
				    			 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@ward", "-");
				    			 qMapResultsHearderWithoutImg.put("@ward", "-");
			 			      }
			 			      
			 			      if(patientHeaderWithoutImg.contains("@floor")) {
			 			    	 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@floor", "-");
			 			    	qMapResultsHearderWithoutImg.put("@floor", "-");
			 			      }
			 			      
			 			      
			 			      if(patientHeaderWithoutImg.contains("@bed")) {
			 			    	 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@bed",  "-");
			 			    	qMapResultsHearderWithoutImg.put("@bed","-");
			 			      }
			 			      
			 			      if(patientHeaderWithoutImg.contains("@consultingDrName")) {
			 			    	 patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@consultingDrName", "-");
			 			    	qMapResultsHearderWithoutImg.put("@consultingDrName","-");
			 			      }
				    	 }
					}else {
						if(patientHeaderWithoutImg.contains("@ward")) {
							patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@ward", "-");
							qMapResultsHearderWithoutImg.put("@ward", "-");
					      }
					      
					      if(patientHeaderWithoutImg.contains("@floor")) {
					    	  patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@floor", "-");
					    	  qMapResultsHearderWithoutImg.put("@floor", "-");
					      }
					      
					      
					      if(patientHeaderWithoutImg.contains("@bed")) {
					    	  patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@bed",  "-");
					    	  qMapResultsHearderWithoutImg.put("@bed","-");
					      }
					      
					      if(patientHeaderWithoutImg.contains("@consultingDrName")) {
					    	  patientHeaderWithoutImg = patientHeaderWithoutImg.replace("@consultingDrName", "-");
					    	  qMapResultsHearderWithoutImg.put("@consultingDrName","-");
					      }
					}	
		    		
			    Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(patientHeaderWithoutImg);
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{	
						    consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
						    consentMapperDto.setTemplateDesc(patientHeaderWithoutImg);
						Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
						qMapResultsFirst = (Map<String, String>) res.getResObject();
						for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
						{
							qMapResultsHearderWithoutImg.put(itr.getKey(), itr.getValue());
						}
						consentMapperDto = (PatientConsentMapperDto) res.getObject();
						patientHeaderWithoutImg = consentMapperDto.getTemplateDesc();
					}
				}
		    }
		    
		    
		    
		    
		    
		    String footer = header.getFooter();
		    if(footer == null) {
		    	footer = "";
		    }else {
		    	footer = footer.replace("@signatureImg",header.getSigntureImg());
		    	Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(footer);
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{
						Integer count = 0;
						for(String q : paramNames)
						{
							if (qMapResultsHearder.containsKey(q))
							{
								footer = footer.replace(q, qMapResultsHearder.get(q));
							}else{
								count++;
							}
						}
						if(count > 0)
						{
							consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
							consentMapperDto.setTemplateDesc(footer);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFooter = (Map<String, String>) res.getResObject(); 
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							footer = consentMapperDto.getTemplateDesc();
							
							for(Map.Entry<String,String> itr : qMapResultsFooter.entrySet())
							{
								qMapResultsHearder.put(itr.getKey(), itr.getValue());
							}
						}
						
					}
				}	
		    }
		   
		    
		    
		    String mailFooter = header.getMailFooter();
		    if(mailFooter == null) {
		    	mailFooter = "";
		    }else {
		    	mailFooter = mailFooter.replace("@signatureImg",header.getSigntureImg());
		    	Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(mailFooter);
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{
						Integer count = 0;
						for(String q : paramNames)
						{
							if (qMapResultsHearder.containsKey(q))
							{
								mailFooter = mailFooter.replace(q, qMapResultsHearder.get(q));
							}else{
								count++;
							}
						}
						if(count > 0)
						{
							consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
							consentMapperDto.setTemplateDesc(mailFooter);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFooter = (Map<String, String>) res.getResObject(); 
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							mailFooter = consentMapperDto.getTemplateDesc();
							
							for(Map.Entry<String,String> itr : qMapResultsFooter.entrySet())
							{
								qMapResultsHearder.put(itr.getKey(), itr.getValue());
							}
						}
						
					}
				}	
		    }
		    
		    
		    
		    if(dto.getTemplateContent()!=null)
		    {
		    	Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(dto.getTemplateContent());
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{
						Integer count = 0;
						String tempContent = dto.getTemplateContent();
						for(String q : paramNames)
						{
							if (qMapResultsHearder.containsKey(q))
							{
								tempContent = tempContent.replace(q, qMapResultsHearder.get(q));
							}else{
								count++;
							}
						}
						if(count > 0)
						{
								consentMapperDto.setTemplateDesc(tempContent);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							tempContent = consentMapperDto.getTemplateDesc();
						}
						dto.setTemplateContent(tempContent);
						body = tempContent; 
					}
				}	
		    
	RadiologyOrdersListDto resDto = new RadiologyOrdersListDto();
	Integer PrintFlag = 0;
	if(dto.getPrintWithHeader().equals(PrintFlag)) {
	//	resDto.setLatterHeadImg(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="" ?header.getLatterHeadImg():latter);

		
		String latterHeadImg = header.getLatterHeadImg();
		if(latterHeadImg == null) {
			
			resDto.setLatterHeadImg(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="" ?header.getLatterHeadImg():latter);
			
	    }else {
	    	
	    	if(dto.getPaymentEntitlementId() == SELF) {
	    		if(latterHeadImg.contains("@companyDesc")) {
	    			latterHeadImg = latterHeadImg.replace("@companyDesc","WALKIN CLIENT");
	    		}
	    		if(latterHeadImg.contains("@comAddress")) {
	    			latterHeadImg = latterHeadImg.replace("@comAddress",dto.getUnitName());
	    		}
	    	}else {
	    		if(latterHeadImg.contains("@companyDesc")) {
	    			latterHeadImg = latterHeadImg.replace("@companyDesc",dto.getCompanyDesc());
	    		}
	    		if(latterHeadImg.contains("@comAddress")) {
	    			latterHeadImg = latterHeadImg.replace("@comAddress",dto.getComAddress());
	    		}
	    	}
	    	
	    	
	    	resDto.setLatterHeadImg(latterHeadImg);
	    }
	
	
	}else {
		resDto.setLatterHeadImg(latter);
		resDto.setLetterHeadImagePdf(latter);
	}
	
	
	resDto.setHeader(patientHeader);
	resDto.setFooter(footer);
	resDto.setTemplateContent(body);
	resDto.setMailFooter(mailFooter);
	resDto.setFooterImg(header.getFooterImg()!=null ? header.getFooterImg(): null);
    resDto.setHeaderWithoutImg(patientHeaderWithoutImg);

	if(patientHeaderPdf == null) {
		patientHeaderPdf = "";
	}else {

		if(dto.getIsProvisionalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N')){
			patientHeaderPdf = patientHeaderPdf.replace("@isProvisionalRelease", "Provisional Release");
			qMapResultsHearder.put("@isProvisionalRelease", "Provisional Release");

		}else{
			patientHeaderPdf = patientHeaderPdf.replace("@isProvisionalRelease", "");
			qMapResultsHearder.put("@isProvisionalRelease", "");
		}

		if(skipRISValidationFlag.getIsSkipRISResultValidation() == 'Y' && skipRISValidationFlag.getIsSkipRISResultValidation() != null){

			patientHeaderPdf = patientHeaderPdf.replace("@releaseDate", "@date");
			qMapResultsHearder.put("@releaseDate", "@date");

			patientHeaderPdf = patientHeaderPdf.replace("@reportValidationDate", "@date");
			qMapResultsHearder.put("@reportValidationDate", "@date");

		}

		patientHeaderPdf = patientHeaderPdf.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
		qMapResultsHearder.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
//            patientHeaderPdf = patientHeaderPdf.replace("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate() );
//            	qMapResultsHearder.put("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate());
		patientHeaderPdf = patientHeaderPdf.replace("@uhid",dto.getUhidNumber());
		qMapResultsHearder.put("@uhid", dto.getUhidNumber());
//            patientHeaderPdf = patientHeaderPdf.replace("@releaseDate",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
//            	qMapResultsHearder.put("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
		patientHeaderPdf = patientHeaderPdf.replace("@age",dto.getAgeString());
		qMapResultsHearder.put("@age", dto.getAgeString());
		patientHeaderPdf = patientHeaderPdf.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
		qMapResultsHearder.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
		patientHeaderPdf = patientHeaderPdf.replace("@referral",referedName);
		qMapResultsHearder.put("@referral",referedName);
		patientHeaderPdf = patientHeaderPdf.replace("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo() : dto.getVisitNumber());
		qMapResultsHearder.put("@visitNo",dto.getVisitNo());
		patientHeaderPdf = patientHeaderPdf.replace("@orderNo",dto.getOrderNo());
		qMapResultsHearder.put("@orderNo",dto.getOrderNo());
		patientHeaderPdf = patientHeaderPdf.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));
		qMapResultsHearder.put("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy HH:mm:ss"));

		patientHeaderPdf = patientHeaderPdf.replace("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
		qMapResultsHearder.put("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");

		patientHeaderPdf = patientHeaderPdf.replace("@reportStatus",reportStatus);
		qMapResultsHearder.put("@reportStatus",reportStatus);
		patientHeaderPdf = patientHeaderPdf.replace("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
		qMapResultsHearder.put("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");


		if((patientHeaderPdf.contains("@ward") || patientHeaderPdf.contains("@floor") || patientHeaderPdf.contains("@bed")
				|| patientHeaderPdf.contains("@consultingDrName")) && (dto.getOrderDetailsId()!=null)) {
			//get wardNAme and bed details by order Details Id
			Response wardRes = iEncounterMasterService.getWardAndFloorAndConsultingDoctorDetails(dto);
			if(wardRes.getObject()!=null) {
				AdmissionDto adtDto = (AdmissionDto)wardRes.getObject();
				if(patientHeaderPdf.contains("@ward")) {
					patientHeaderPdf = patientHeaderPdf.replace("@ward",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
					qMapResultsHearder.put("@ward",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
				}

				if(patientHeaderPdf.contains("@floor")) {
					patientHeaderPdf = patientHeaderPdf.replace("@floor",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
					qMapResultsHearder.put("@floor",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
				}

				if(patientHeaderPdf.contains("@bed")) {
					patientHeaderPdf = patientHeaderPdf.replace("@bed", adtDto.getBedNumber()!= null ?adtDto.getBedNumber(): "-");
					qMapResultsHearder.put("@bed",adtDto.getBedNumber() != null ?adtDto.getBedNumber(): "-");
				}

				if(patientHeaderPdf.contains("@consultingDrName")) {
					patientHeaderPdf = patientHeaderPdf.replace("@consultingDrName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
					qMapResultsHearder.put("@consultingDrName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
				}
			}else {
				if(patientHeaderPdf.contains("@ward")) {
					patientHeaderPdf = patientHeaderPdf.replace("@ward", "-");
					qMapResultsHearder.put("@ward", "-");
				}

				if(patientHeaderPdf.contains("@floor")) {
					patientHeaderPdf = patientHeaderPdf.replace("@floor", "-");
					qMapResultsHearder.put("@floor", "-");
				}


				if(patientHeaderPdf.contains("@bed")) {
					patientHeaderPdf = patientHeaderPdf.replace("@bed",  "-");
					qMapResultsHearder.put("@bed","-");
				}

				if(patientHeaderPdf.contains("@consultingDrName")) {
					patientHeaderPdf = patientHeaderPdf.replace("@consultingDrName", "-");
					qMapResultsHearder.put("@consultingDrName","-");
				}
			}
		}else {
			if(patientHeaderPdf.contains("@ward")) {
				patientHeaderPdf = patientHeaderPdf.replace("@ward", "-");
				qMapResultsHearder.put("@ward", "-");
			}

			if(patientHeaderPdf.contains("@floor")) {
				patientHeaderPdf = patientHeaderPdf.replace("@floor", "-");
				qMapResultsHearder.put("@floor", "-");
			}


			if(patientHeaderPdf.contains("@bed")) {
				patientHeaderPdf = patientHeaderPdf.replace("@bed",  "-");
				qMapResultsHearder.put("@bed","-");
			}

			if(patientHeaderPdf.contains("@consultingDrName")) {
				patientHeaderPdf = patientHeaderPdf.replace("@consultingDrName", "-");
				qMapResultsHearder.put("@consultingDrName","-");
			}
		}

		pattern = Pattern.compile("@\\w+");
		 paramNames = new ArrayList<>();
		 matcher = pattern.matcher(patientHeaderPdf);
		if(matcher!=null)
		{
			while (matcher.find())
			{
				paramNames.add(matcher.group());
			}
			if(paramNames.size()>0)
			{
				consentMapperDto.setPatientId(dto.getPatientId());
				consentMapperDto.setEncounterId(dto.getEncounterId());
				consentMapperDto.setAdmissionId(dto.getAdmissionId());
				consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
				consentMapperDto.setTemplateDesc(patientHeaderPdf);
				Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
				qMapResultsFirst = (Map<String, String>) res.getResObject();
				for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
				{
					qMapResultsHearder.put(itr.getKey(), itr.getValue());
				}
				consentMapperDto = (PatientConsentMapperDto) res.getObject();
				patientHeaderPdf = consentMapperDto.getTemplateDesc();
			}
		}
	}

	if(dto.getPrintWithHeader().equals(PrintFlag)) {

		String latterHeadImgPdf = header.getLetterHeadImagePdf();
		if(latterHeadImgPdf == null) {

			resDto.setLetterHeadImagePdf(header.getLetterHeadImagePdf()!=null && header.getLetterHeadImagePdf()!="" ?header.getLetterHeadImagePdf():latter);

		}else {

			if(dto.getPaymentEntitlementId() == SELF) {
				if(latterHeadImgPdf.contains("@companyDesc")) {
					latterHeadImgPdf = latterHeadImgPdf.replace("@companyDesc","WALKIN CLIENT");
				}
				if(latterHeadImgPdf.contains("@comAddress")) {
					latterHeadImgPdf = latterHeadImgPdf.replace("@comAddress",dto.getUnitName());
				}
			}else {
				if(latterHeadImgPdf.contains("@companyDesc")) {
					latterHeadImgPdf = latterHeadImgPdf.replace("@companyDesc",dto.getCompanyDesc());
				}
				if(latterHeadImgPdf.contains("@comAddress")) {
					latterHeadImgPdf = latterHeadImgPdf.replace("@comAddress",dto.getComAddress());
				}
			}
			resDto.setLetterHeadImagePdf(latterHeadImgPdf);
		}
	}

	resDto.setHeaderPdf(patientHeaderPdf);
	      /* String appendPath="RISReport";
	       // String rootPath = System.getProperty("catalina.home");
			String rootPath = System.getProperty("catalina.base") + File.separator + "webapps";
			 
			File dir = new File(
					rootPath + File.separator + "SharadDocument" + File.separator + "CHT" + File.separator + appendPath);
			// File dir = new File("C:" + File.separator + "BW" + File.separator +
			// "documents" + File.separator + appendPath);

			// System.out.println(" dir Path : " + dir);
			if (!dir.exists())
				dir.mkdirs();

			// Create the file on server
			File serverFile = new File(dir.getAbsolutePath() + File.separator);

			//logger.info(" File Utility Directory created at -Name- : " + dir.getAbsolutePath() + File.separator + "  - "+ TimeZoneComponent.getDateByzone(unitId));
			// used java regular expression to remove html tag
			String c="";
			c=patientHeader+body+footer;
			//String s1 = c.replaceAll("<.*?>", "");
		//	s1=s1.replaceAll("&nbsp;", "");
			
			String onlyPath = serverFile + File.separator;
			 
			
			 try {
			      FileWriter myWriter = new FileWriter(onlyPath + "report.html");
			      myWriter.write(c);
			      myWriter.close();
			      System.out.println("Successfully wrote to the file.");
			    } catch (IOException e) {
			      System.out.println("An error occurred.");
			      
			    }*/
			
			 
	
	
	
	return new Response<>(SUCCESS, SUCCESS_CODE, null, null, resDto);
	}
	}catch (HibernateException he) {
	logger.error("printReportTest failed", he);
	} catch (Exception e) {
	logger.error("printReportTest failed", e);
	}
	return null;
	}

	public String setConcentValuesForPdf(String patientHeader, RadiologyOrdersListDto dto, RadiologyOrdersListDto skipRISValidationFlag){
		try{
			PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();

			Map<String,String> qMapResultsHearder = new HashMap<String, String>();

			String reportStatus = "Initiate";
			if(dto.getIsProvisionalRelease() == null && dto.getIsFinalRelease()== null){
				reportStatus = "Initiate";
			}else if((dto.getIsProvisionalRelease() == 'N' || dto.getIsProvisionalRelease() == null) && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N') ){
				reportStatus = "Initiate";
			}else if(dto.getIsProvisionalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N')){
				reportStatus = "Provisional";
			}else if(dto.getIsFinalRelease() == 'Y' && (dto.getIsFinalRelease() == null || dto.getIsFinalRelease()== 'N') ){
				reportStatus = "Final";
			}else if(dto.getIsFinalRelease() == 'Y' && dto.getIsProvisionalRelease()== 'Y') {
				reportStatus = "Final";
			}


			String referedName = "     -";
			if(dto.getReferralName()!= null) {
				referedName = dto.getReferralName();
			}



		}catch(Exception e){
		logger.error("setConcentValuesForPdf failed", e);
		}
		return null;
	}
	
	@Override
	public Response printReportTestLab(RadiologyOrdersListDto dto) {
	try{
		
		PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
		/*String query = "select template_object from t_template_transaction ttt where template_transaction_id ="+dto.getTemplateTransactionId();
		
		String tempObj  = (String)sessionFactory.getCurrentSession().createSQLQuery(query).uniqueResult();
			dto.setTemplateContent(tempObj);*/
		Character isOtherLogoRequired = 'N';
		String letterHeadImageTariff = "";
		if(dto.getFromLisTemplate() != null && dto.getFromLisTemplate().equals(Boolean.TRUE)) {
			isOtherLogoRequired = (Character) sessionFactory.getCurrentSession().createSQLQuery(
			"select other_org_logo_required "
					+ "from  public.t_unit_application_billing_configuration where organization_id = " + dto.getOrganizationId()
					+ " and unit_id = " + dto.getUnitId()).uniqueResult();
		}

		if((isOtherLogoRequired != null && isOtherLogoRequired == 'Y')&&(dto.getTariffId() != null && dto.getTariffId() > 0)){
			letterHeadImageTariff = (String) sessionFactory.getCurrentSession().createSQLQuery(
					"select letter_head_image_tariff "
							+ "from  service.m_tariff_master where organization_id =:orgId "
							+ " and unit_id =:unitId "
							+ " and tariff_id =:tariffId "
							+ " and status ='A' ")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("tariffId", dto.getTariffId())
					.uniqueResult();
		}
		
		RadiologyOrdersListDto header = (RadiologyOrdersListDto)sessionFactory.getCurrentSession().createSQLQuery(
			"select lab_header as \"header\", lab_footer as \"footer\" , signture_img as \"signtureImg\" , latter_head_img as \"latterHeadImg\", "
		    + " footer_img as \"footerImg\" , lab_header_without_img as \"labHeaderWithoutImg\", letter_head_image_other_org as \"letterHeadImageTariff\" "
			+ "from  public.m_report_header where organization_id = "+dto.getOrganizationId()
			+ " and unit_id = " + dto.getUnitId())
			.setResultTransformer(Transformers.aliasToBean(RadiologyOrdersListDto.class)).uniqueResult();
		
		   ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
		   PdfWriter pdfWriter = null;
		   String referedName = "     -";
		   if(dto.getReferralName()!= null) {
			   referedName = dto.getReferralName();
		   }
		   
		   String latter = "<br><br><br><br>";
		   String body = dto.getTemplateContent()+"<br>";
		   //body = body.replace("<li>","<li>&bull;   ");
		   
		   Map<String,String> qMapResultsHearder = new HashMap<String, String>();
		   Map<String,String> qMapResultsHearderWithoutImg = new HashMap<String, String>();
		   Map<String,String> qMapResultsFirst=null;
		   Map<String,String> qMapResultsFooter = null;
		   
		    String   patientHeader = header.getHeader();
		    
		    if(patientHeader == null) {
		    	patientHeader = "";
		    }else {
		    	 
		    	patientHeader = patientHeader.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
            	qMapResultsHearder.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
            patientHeader = patientHeader.replace("@date",dto.getOrderDtTime()!=null ? dto.getOrderDtTime(): dto.getOrderDate());
            	qMapResultsHearder.put("@date", dto.getOrderDtTime()!=null ? dto.getOrderDtTime(): dto.getOrderDate());
            patientHeader = patientHeader.replace("@uhid",dto.getUhidNumber());
            	qMapResultsHearder.put("@uhid", dto.getUhidNumber());
            patientHeader = patientHeader.replace("@releaseDate",dto.getReportReleaseDate()!=null ? dto.getReportReleaseDate(): GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
            	qMapResultsHearder.put("@releaseDate", dto.getReportReleaseDate()!=null ? dto.getReportReleaseDate(): GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
            patientHeader = patientHeader.replace("@age",dto.getAgeString());
            	qMapResultsHearder.put("@age", dto.getAgeString());
            patientHeader = patientHeader.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            	qMapResultsHearder.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            patientHeader = patientHeader.replace("@referral",referedName);
            	qMapResultsHearder.put("@referral",referedName);
            patientHeader = patientHeader.replace("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo() :"-");
		    	qMapResultsHearder.put("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo(): "-");
		    patientHeader = patientHeader.replace("@orderNo",dto.getOrderNo());
		    	qMapResultsHearder.put("@orderNo",dto.getOrderNo());
		    patientHeader = patientHeader.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
		        	qMapResultsHearder.put("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
		    patientHeader = patientHeader.replace("@sampleNo",dto.getLabSampleNo());
		        	qMapResultsHearder.put("@sampleNo",dto.getLabSampleNo());
		    patientHeader = patientHeader.replace("@reportStatus",(dto.getIsProvisionalRelease() == null && dto.getIsFinalRelease()== null ) ? "Initiate" : dto.getIsProvisionalRelease()!=null && dto.getIsProvisionalRelease() == 'Y' ? "Provisional" : "Final"  );
                  qMapResultsHearder.put("@reportStatus",(dto.getIsProvisionalRelease() == null && dto.getIsFinalRelease()== null ) ? "Initiate" : dto.getIsProvisionalRelease()!=null && dto.getIsProvisionalRelease() == 'Y' ? "Provisional" : "Final" );
		    patientHeader = patientHeader.replace("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
			        	qMapResultsHearder.put("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
		        	
		      if(patientHeader.contains("@reportEntryDate")) {
		    	  patientHeader = patientHeader.replace("@reportEntryDate",dto.getReportEntryDate() != null ?dto.getReportEntryDate(): "-");
		        	qMapResultsHearder.put("@reportEntryDate",dto.getReportEntryDate() != null ?dto.getReportEntryDate(): "-");
		      }
		      
		      if(patientHeader.contains("@reportValidationDate")) {
		    	  patientHeader = patientHeader.replace("@reportValidationDate",dto.getReportValidationDate() != null ?dto.getReportValidationDate(): "-");
		        	qMapResultsHearder.put("@reportValidationDate",dto.getReportValidationDate() != null ?dto.getReportValidationDate(): "-");
		      }
		      
		      if(patientHeader.contains("@reportReleaseDate")) {
		    	  patientHeader = patientHeader.replace("@reportReleaseDate",dto.getReportReleaseDate() != null ?dto.getReportReleaseDate(): "-");
		        	qMapResultsHearder.put("@reportReleaseDate",dto.getReportReleaseDate() != null ?dto.getReportReleaseDate(): "-");
		      }
			    
		      if(patientHeader.contains("@sampleType")) {
		    	  patientHeader = patientHeader.replace("@sampleType",dto.getSampleType() != null ?dto.getSampleType(): "-");
		        	qMapResultsHearder.put("@sampleType",dto.getSampleType() != null ?dto.getSampleType(): "-");
		      }
		      
		      if(patientHeader.contains("@collectionTime")) {
		    	  patientHeader = patientHeader.replace("@collectionTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");
		    	  qMapResultsHearder.put("@collectionTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");
		      }
		      
		      
				if((patientHeader.contains("@wardName") || patientHeader.contains("@floorName") || patientHeader.contains("@bedNumber") 
						|| patientHeader.contains("@consultingDoctorName")) && (dto.getOrderDetailsId()!=null)) {
					//get wardNAme and bed details by order Details Id
			    	 Response wardRes = iEncounterMasterService.getWardAndFloorAndConsultingDoctorDetails(dto);		
			    	 if(wardRes.getObject()!=null) {
			    		 AdmissionDto adtDto = (AdmissionDto)wardRes.getObject();
			    		 if(patientHeader.contains("@wardName")) {
		 			    	  patientHeader = patientHeader.replace("@wardName",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
		 			    	  qMapResultsHearder.put("@wardName",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
		 			      }
		 			      
		 			      if(patientHeader.contains("@floorName")) {
		 			    	  patientHeader = patientHeader.replace("@floorName",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
		 			    	  qMapResultsHearder.put("@floorName",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
		 			      }
		 			      
		 			      if(patientHeader.contains("@bedNumber")) {
		 			    	  patientHeader = patientHeader.replace("@bedNumber", adtDto.getBedNumber()!= null ?adtDto.getBedNumber(): "-");
		 			    	  qMapResultsHearder.put("@bedNumber",adtDto.getBedNumber() != null ?adtDto.getBedNumber(): "-");
		 			      }
		 			      
		 			      if(patientHeader.contains("@consultingDoctorName")) {
		 			    	  patientHeader = patientHeader.replace("@consultingDoctorName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
		 			    	  qMapResultsHearder.put("@consultingDoctorName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
		 			      }
			    	 }else {
			    		 if(patientHeader.contains("@wardName")) {
		 			    	  patientHeader = patientHeader.replace("@wardName", "-");
		 			    	  qMapResultsHearder.put("@wardName", "-");
		 			      }
		 			      
		 			      if(patientHeader.contains("@floorName")) {
		 			    	  patientHeader = patientHeader.replace("@floorName", "-");
		 			    	  qMapResultsHearder.put("@floorName", "-");
		 			      }
		 			      
		 			      
		 			      if(patientHeader.contains("@bedNumber")) {
		 			    	  patientHeader = patientHeader.replace("@bedNumber",  "-");
		 			    	  qMapResultsHearder.put("@bedNumber","-");
		 			      }
		 			      
		 			      if(patientHeader.contains("@consultingDoctorName")) {
		 			    	  patientHeader = patientHeader.replace("@consultingDoctorName", "-");
		 			    	  qMapResultsHearder.put("@consultingDoctorName","-");
		 			      }
			    	 }
				}else {
					if(patientHeader.contains("@wardName")) {
				    	  patientHeader = patientHeader.replace("@wardName", "-");
				    	  qMapResultsHearder.put("@wardName", "-");
				      }
				      
				      if(patientHeader.contains("@floorName")) {
				    	  patientHeader = patientHeader.replace("@floorName", "-");
				    	  qMapResultsHearder.put("@floorName", "-");
				      }
				      
				      
				      if(patientHeader.contains("@bedNumber")) {
				    	  patientHeader = patientHeader.replace("@bedNumber",  "-");
				    	  qMapResultsHearder.put("@bedNumber","-");
				      }
				      
				      if(patientHeader.contains("@consultingDoctorName")) {
				    	  patientHeader = patientHeader.replace("@consultingDoctorName", "-");
				    	  qMapResultsHearder.put("@consultingDoctorName","-");
				      }
				}
			    Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(patientHeader);
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{	
						    consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
						    consentMapperDto.setLabSampleDtlsId(dto.getLabSampleDetailsId());
						    consentMapperDto.setTemplateDesc(patientHeader);
						Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
						qMapResultsFirst = (Map<String, String>) res.getResObject();
						for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
						{
							qMapResultsHearder.put(itr.getKey(), itr.getValue());
						}
						consentMapperDto = (PatientConsentMapperDto) res.getObject();
						patientHeader = consentMapperDto.getTemplateDesc();
					}
				}
		    }
		   
		    
 // patient header without letter img
		    
		    String   patientLabHeaderWithoutImg = header.getLabHeaderWithoutImg();
		    if(patientLabHeaderWithoutImg == null) {
		    	patientLabHeaderWithoutImg = "";
		    }else {
			   
		    	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@patient",dto.getPrefixCode()+" "+dto.getPatientName());
		    		qMapResultsHearderWithoutImg.put("@patient", dto.getPrefixCode()+" "+dto.getPatientName());
		    	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate() );
            		qMapResultsHearderWithoutImg.put("@date",dto.getOrderDateTime()!=null ? dto.getOrderDateTime() :dto.getOrderDate());
            	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@uhid",dto.getUhidNumber());
            		qMapResultsHearderWithoutImg.put("@uhid", dto.getUhidNumber());
            	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@releaseDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
            		qMapResultsHearderWithoutImg.put("@releaseDate",  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
            	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@age",dto.getAgeString());
            		qMapResultsHearderWithoutImg.put("@age", dto.getAgeString());
            	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@gender",dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            		qMapResultsHearderWithoutImg.put("@gender", dto.getGenderId()==1 ? "Male" : dto.getGenderId()==2 ? "Female" : "Other");
            	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@referral",referedName);
            		qMapResultsHearderWithoutImg.put("@referral",referedName);
            	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@visitNo",dto.getVisitNo()!=null ? dto.getVisitNo() : dto.getVisitNumber());
            		qMapResultsHearderWithoutImg.put("@visitNo",dto.getVisitNo());
            	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@orderNo",dto.getOrderNo());
		    		qMapResultsHearderWithoutImg.put("@orderNo",dto.getOrderNo());
		    	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
		    		qMapResultsHearderWithoutImg.put("@printTime",GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd/MM/yyyy hh:mm aa"));
		    		 
		    	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@sampleNo",dto.getLabSampleNo());
		    		qMapResultsHearderWithoutImg.put("@sampleNo",dto.getLabSampleNo());
		    		
		    	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
		    		qMapResultsHearderWithoutImg.put("@BillNo",dto.getBillNo()!=null ? dto.getBillNo() : "-");
                	   	
		    	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@reportStatus", (dto.getIsProvisionalRelease() == null && dto.getIsFinalRelease()== null ) ? "Initiate" : dto.getIsProvisionalRelease()!=null && dto.getIsProvisionalRelease() == 'Y' ? "Provisional" : "Final" );
		    		qMapResultsHearderWithoutImg.put("@reportStatus",(dto.getIsProvisionalRelease() == null && dto.getIsFinalRelease()== null ) ? "Initiate" : dto.getIsProvisionalRelease()!=null && dto.getIsProvisionalRelease() == 'Y' ? "Provisional" : "Final" );
		    	patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");
		    		qMapResultsHearderWithoutImg.put("@registerDate",dto.getRegisterDate() != null ?dto.getRegisterDate(): "-");	   	
			    	
		    	if(patientLabHeaderWithoutImg.contains("@reportEntryDate")) {
		    		patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@reportEntryDate",dto.getReportEntryDate() != null ?dto.getReportEntryDate(): "-");
		    		qMapResultsHearderWithoutImg.put("@reportEntryDate",dto.getReportEntryDate() != null ?dto.getReportEntryDate(): "-");
				}	
		    	
		    	 if(patientLabHeaderWithoutImg.contains("@reportValidationDate")) {
		    		 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@reportValidationDate",dto.getReportValidationDate() != null ?dto.getReportValidationDate(): "-");
		    		 qMapResultsHearderWithoutImg.put("@reportValidationDate",dto.getReportValidationDate() != null ?dto.getReportValidationDate(): "-");
			      }
			      
			      if(patientLabHeaderWithoutImg.contains("@reportReleaseDate")) {
			    	  patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@reportReleaseDate",dto.getReportReleaseDate() != null ?dto.getReportReleaseDate(): "-");
			    	  qMapResultsHearderWithoutImg.put("@reportReleaseDate",dto.getReportReleaseDate() != null ?dto.getReportReleaseDate(): "-");
			      }
			      
			      if(patientLabHeaderWithoutImg.contains("@sampleType")) {
			    	  patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@sampleType",dto.getSampleType() != null ?dto.getSampleType(): "-");
			    	  qMapResultsHearderWithoutImg.put("@sampleType",dto.getSampleType() != null ?dto.getSampleType(): "-");
			      }
			      
			      if(patientLabHeaderWithoutImg.contains("@collectionTime")) {
			    	  patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@collectionTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");
			    	  qMapResultsHearderWithoutImg.put("@collectionTime",dto.getOrderDtTime() != null ?dto.getOrderDtTime(): "-");
			      }
			      
			      
		    		
		    		if((patientLabHeaderWithoutImg.contains("@wardName") || patientLabHeaderWithoutImg.contains("@floorName") || patientLabHeaderWithoutImg.contains("@bedNumber") 
							|| patientLabHeaderWithoutImg.contains("@consultingDoctorName")) && (dto.getOrderDetailsId()!=null)) {
						//get wardNAme and bed details by order Details Id
				    	 Response wardRes = iEncounterMasterService.getWardAndFloorAndConsultingDoctorDetails(dto);		
				    	 if(wardRes.getObject()!=null) {
				    		 AdmissionDto adtDto = (AdmissionDto)wardRes.getObject();
				    		 if(patientLabHeaderWithoutImg.contains("@wardName")) {
				    			 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@wardName",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
				    			 qMapResultsHearderWithoutImg.put("@wardName",adtDto.getWardName() != null ?adtDto.getWardName(): "-");
			 			      }
			 			      
			 			      if(patientLabHeaderWithoutImg.contains("@floorName")) {
			 			    	 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@floorName",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
			 			    	qMapResultsHearderWithoutImg.put("@floorName",adtDto.getFloorName() != null ?adtDto.getFloorName(): "-");
			 			      }
			 			      
			 			      if(patientLabHeaderWithoutImg.contains("@bedNumber")) {
			 			    	 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@bedNumber", adtDto.getBedNumber()!= null ?adtDto.getBedNumber(): "-");
			 			    	qMapResultsHearderWithoutImg.put("@bedNumber",adtDto.getBedNumber() != null ?adtDto.getBedNumber(): "-");
			 			      }
			 			      
			 			      if(patientLabHeaderWithoutImg.contains("@consultingDoctorName")) {
			 			    	 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@consultingDoctorName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
			 			    	qMapResultsHearderWithoutImg.put("@consultingDoctorName",adtDto.getDoctorName() != null ?adtDto.getDoctorName(): "-");
			 			      }
				    	 }else {
				    		 if(patientLabHeaderWithoutImg.contains("@wardName")) {
				    			 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@wardName", "-");
				    			 qMapResultsHearderWithoutImg.put("@wardName", "-");
			 			      }
			 			      
			 			      if(patientLabHeaderWithoutImg.contains("@floorName")) {
			 			    	 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@floorName", "-");
			 			    	qMapResultsHearderWithoutImg.put("@floorName", "-");
			 			      }
			 			      
			 			      
			 			      if(patientLabHeaderWithoutImg.contains("@bedNumber")) {
			 			    	 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@bedNumber",  "-");
			 			    	qMapResultsHearderWithoutImg.put("@bedNumber","-");
			 			      }
			 			      
			 			      if(patientLabHeaderWithoutImg.contains("@consultingDoctorName")) {
			 			    	 patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@consultingDoctorName", "-");
			 			    	qMapResultsHearderWithoutImg.put("@consultingDoctorName","-");
			 			      }
				    	 }
					}else {
						if(patientLabHeaderWithoutImg.contains("@wardName")) {
							patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@wardName", "-");
							qMapResultsHearderWithoutImg.put("@wardName", "-");
					      }
					      
					      if(patientLabHeaderWithoutImg.contains("@floorName")) {
					    	  patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@floorName", "-");
					    	  qMapResultsHearderWithoutImg.put("@floorName", "-");
					      }
					      
					      
					      if(patientLabHeaderWithoutImg.contains("@bedNumber")) {
					    	  patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@bedNumber",  "-");
					    	  qMapResultsHearderWithoutImg.put("@bedNumber","-");
					      }
					      
					      if(patientLabHeaderWithoutImg.contains("@consultingDoctorName")) {
					    	  patientLabHeaderWithoutImg = patientLabHeaderWithoutImg.replace("@consultingDoctorName", "-");
					    	  qMapResultsHearderWithoutImg.put("@consultingDoctorName","-");
					      }
					}	
		    		
			    Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(patientLabHeaderWithoutImg);
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{	
						    consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
						    consentMapperDto.setLabSampleDtlsId(dto.getLabSampleDetailsId());
						    consentMapperDto.setTemplateDesc(patientLabHeaderWithoutImg);
						Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
						qMapResultsFirst = (Map<String, String>) res.getResObject();
						for(Map.Entry<String,String> itr : qMapResultsFirst.entrySet())
						{
							qMapResultsHearderWithoutImg.put(itr.getKey(), itr.getValue());
						}
						consentMapperDto = (PatientConsentMapperDto) res.getObject();
						patientLabHeaderWithoutImg = consentMapperDto.getTemplateDesc();
					}
				}
		    }
		    
		    
		    String footer = header.getFooter();
		    if(footer == null) {
		    	footer = "";
		    }else {
		    	footer = footer.replace("@signatureImg",header.getSigntureImg());
		    	
		    	 if(footer.contains("@reportRecall") && dto.getIsReportRecall()) {
		    		 footer = footer.replace("@reportRecall","This is an Amended and Revised Report");
			      }
		    	
		    	Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(footer);
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{
						Integer count = 0;
						for(String q : paramNames)
						{
							if (qMapResultsHearder.containsKey(q))
							{
								footer = footer.replace(q, qMapResultsHearder.get(q));
							}else{
								count++;
							}
						}
						if(count > 0)
						{
							consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());
						    consentMapperDto.setLabSampleDtlsId(dto.getLabSampleDetailsId());
							consentMapperDto.setTemplateDesc(footer);
							consentMapperDto.setReportEntryBy(dto.getReportEntryBy());
							consentMapperDto.setValidatedBy(dto.getValidatedBy());
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							qMapResultsFooter = (Map<String, String>) res.getResObject(); 
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							footer = consentMapperDto.getTemplateDesc();
							
							for(Map.Entry<String,String> itr : qMapResultsFooter.entrySet())
							{
								qMapResultsHearder.put(itr.getKey(), itr.getValue());
							}
						}
						
					}
				}	
		    }
		   
		    if(dto.getTemplateContent()!=null)
		    {
		    	Pattern pattern = Pattern.compile("@\\w+");
				List<String> paramNames = new ArrayList<>();
				Matcher matcher = pattern.matcher(dto.getTemplateContent());
				if(matcher!=null)
				{
					while (matcher.find())
					{
					    paramNames.add(matcher.group());
					}
					if(paramNames.size()>0)
					{
						Integer count = 0;
						String tempContent = dto.getTemplateContent();
						for(String q : paramNames)
						{
							if (qMapResultsHearder.containsKey(q))
							{
								tempContent = tempContent.replace(q, qMapResultsHearder.get(q));
							}else{
								count++;
							}
						}
						if(count > 0)
						{
							consentMapperDto.setPatientId(dto.getPatientId());
						    consentMapperDto.setEncounterId(dto.getEncounterId());
						    consentMapperDto.setAdmissionId(dto.getAdmissionId());
						    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());	
							consentMapperDto.setTemplateDesc(tempContent);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							tempContent = consentMapperDto.getTemplateDesc();
						}
						dto.setTemplateContent(tempContent);
						body = tempContent; 
					}
				}	
		    
	RadiologyOrdersListDto resDto = new RadiologyOrdersListDto();
	Integer PrintFlag = 0;
	if(dto.getPrintWithHeader().equals(PrintFlag)) {
		
		String latterHeadImg = header.getLatterHeadImg();
		if(latterHeadImg == null) {
			
			resDto.setLatterHeadImg(header.getLatterHeadImg()!=null && header.getLatterHeadImg()!="" ?header.getLatterHeadImg():latter);
			
	    }else {
	    	
	    	if(dto.getPaymentEntitlementId() == SELF) {
	    		if(latterHeadImg.contains("@companyDesc")) {
	    			latterHeadImg = latterHeadImg.replace("@companyDesc","WALKIN CLIENT");
	    		}
	    		if(latterHeadImg.contains("@comAddress")) {
	    			latterHeadImg = latterHeadImg.replace("@comAddress",dto.getUnitName());
	    		}
	    	}else {
	    		if(latterHeadImg.contains("@companyDesc")) {
	    			latterHeadImg = latterHeadImg.replace("@companyDesc",dto.getCompanyDesc());
	    		}
	    		if(latterHeadImg.contains("@comAddress")) {
	    			latterHeadImg = latterHeadImg.replace("@comAddress",dto.getComAddress());
	    		}
	    	}
	    	
	    	
	    	resDto.setLatterHeadImg(latterHeadImg);
	    }

		if((isOtherLogoRequired != null && isOtherLogoRequired =='Y')&&(letterHeadImageTariff != null && !letterHeadImageTariff.trim().isEmpty())){
			if(header.getLetterHeadImageTariff() != null && !header.getLetterHeadImageTariff().trim().isEmpty()) {
				if (header.getLetterHeadImageTariff().contains("@otherOrgLogo")) {
					String otherOrgLogo = header.getLetterHeadImageTariff().replace("@otherOrgLogo", letterHeadImageTariff);
					resDto.setLetterHeadImageTariff(otherOrgLogo);
					resDto.setLatterHeadImg(otherOrgLogo);
				}
			}
		}

	}else {
		resDto.setLatterHeadImg(latter);
	}
	
	resDto.setHeader(patientHeader);
	resDto.setFooter(footer);
	resDto.setTemplateContent(body);
	resDto.setFooterImg(header.getFooterImg()!=null ? header.getFooterImg():null);
	resDto.setLabHeaderWithoutImg(patientLabHeaderWithoutImg);
	return new Response<>(SUCCESS, SUCCESS_CODE, null, null, resDto);
	}
	}catch (HibernateException he) {
	logger.error("printReportTestLab failed", he);
	} catch (Exception e) {
	logger.error("printReportTestLab failed", e);
	}
	return null;
	}


	@Override
	public Response updateTemplateTransForRadiologyReportUrl(RadiologyOrdersListDto radiologyOrdersListDto, String str)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createSQLQuery("update  public.t_template_transaction set " + 
					"radiology_report_url ='"+str+"'"+
					"WHERE org_id =" + radiologyOrdersListDto.getOrganizationId()+
					" and unit_id =" + radiologyOrdersListDto.getUnitId()+
					" and template_transaction_id ="+radiologyOrdersListDto.getTemplateTransactionId())
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("updateTemplateTransForRadiologyReportUrl failed", he);
		} catch (Exception e) {
		logger.error("updateTemplateTransForRadiologyReportUrl failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getPayee(RadiologyOrdersListDto orderDto) throws ApplicationException {
		{
			try {






				if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE) {
					OrderMasterDto orderDtoNew=  (OrderMasterDto) sessionFactory.getCurrentSession()
							.getNamedQuery("GET_COMPANY_ID_BY_ORDER_ID_RADIOLOGY")
							.setParameter("orderId", orderDto.getOrderId())
							.setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class)).uniqueResult();

					BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
							.createSQLQuery("select count(*) from adt.t_insurance ti where insurance_id  =:payeeId and  block_report_print = 'Y' ").setParameter("payeeId", orderDtoNew.getInsuranceId()).uniqueResult();
					return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());

				}else  if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
					BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
							.createSQLQuery("SELECT count(*) FROM public.t_company_master where company_id  =:payeeId and  block_report_print = 'Y' ").setParameter("payeeId", orderDto.getPayeeId()).uniqueResult();
					return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());
				}


			}catch(Exception e) {

				throw e;
			}
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response checkOutStandingAmount(Integer encounterId) throws ApplicationException {
		{
			try {
				BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery("select count(*)  from billing.m_billing_master  where encounter_id  =:encounterId and outstanding >0 ").setParameter("encounterId", encounterId).uniqueResult();
				return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());

			} catch (HibernateException he) {
			logger.error("checkOutStandingAmount failed", he);
			} catch (Exception e) {
			logger.error("checkOutStandingAmount failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
	}


}
