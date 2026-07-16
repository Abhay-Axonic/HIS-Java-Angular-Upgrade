package com.param.global.otims.dao;


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.IssueAcceptanceDto;
import com.param.global.otims.dto.ImplantDetailsDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.opd.coversheet.model.ImplantDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"unchecked","rawtypes","unused"})
@Repository
public class ImplantDetailsDaoImpl extends GenericDao<ImplantDetails, Integer>implements IImplantDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ImplantDetailsDaoImpl.class);

	public ImplantDetailsDaoImpl() {
		super(ImplantDetails.class);
	} 
	
	@Override
	public Response saveImplantdetails( List<ImplantDetailsDto> implantDetailsDto ,int proReqId, Integer procedureOrderId) throws ApplicationException {
		try{
			//Session session=sessionFactory.getCurrentSession();
		/*	Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_implant_details set status='I' "
					+ " where procedure_order_id='"+procedureOrderId+"' ")
					.executeUpdate();
			*/
			for(ImplantDetailsDto implantList:implantDetailsDto)
			{
				ImplantDetails implantDetails = new ImplantDetails();
				implantDetails.setPatientId(implantList.getPatientId());
				if(implantList.getItemId()!=null){
					implantDetails.setItemId(implantList.getItemId());
					}else{
						implantDetails.setKitId(implantList.getKitId());
					}
				implantDetails.setOrganizationId(implantList.getOrganizationId());
				implantDetails.setUnitId(implantList.getUnitId());
				implantDetails.setCreatedBy(implantList.getCreatedBy());
			//	implantDetails.setProcedureOrderId(procedureOrderId);
				implantDetails.setProcedureOrderId(procedureOrderId==0?implantList.getProcedureOrderId():procedureOrderId);
				implantDetails.setIsExtra(implantList.getIsExtra()!= null ? implantList.getIsExtra() : false);
				if(implantList.getAdmissionId()!=null){
					implantDetails.setAdmissionId(implantList.getAdmissionId());
				}else{
					implantDetails.setEncounterId(implantList.getEncounterId());
				}
				implantDetails.setQty(implantList.getQty());
				implantDetails.setCreatedDate(implantList.getCreatedDate() != null && !implantList.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(implantList.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(implantList.getUnitId()));
				implantDetails.setUpdatedBy(implantList.getUpdatedBy());
				implantDetails.setUpdatedDate(implantList.getUpdatedDate() != null && !implantList.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(implantList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(implantList.getUnitId()));
			
				
				if(implantList.getImplantDetailsId()!=null) {
					implantDetails.setImplantDetailsId(implantList.getImplantDetailsId());
					implantDetails.setStatus(implantList.getStatus());
					sessionFactory.getCurrentSession().update(implantDetails);
				}else {
					implantDetails.setStatus('A');
					sessionFactory.getCurrentSession().save(implantDetails);
				}
				}
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveImplantdetails failed", he);
		} catch (Exception e) {
		logger.error("saveImplantdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getImplantdetailsList(ImplantDetailsDto implantDetailsDto) {
		try{
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IMPLANT_DETAILS_LIST")
					.getQueryString();

			StringBuilder subQuery = new StringBuilder();

// Secure parameterized query
			if (implantDetailsDto.getFromDate() != null && implantDetailsDto.getToDate() != null) {
				subQuery.append(" WHERE to_char(imt_details.created_date, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate");
			}

			if (implantDetailsDto.getCreatedBy() != null) {
				if (subQuery.length() != 0) {
					subQuery.append(" AND");
				} else {
					subQuery.append(" WHERE");
				}
				subQuery.append(" imt_details.created_by = :createdBy");
			}

			if (subQuery.length() != 0) {
				subQuery.append(" AND");
			} else {
				subQuery.append(" WHERE");
			}

			subQuery.append(" imt_details.organization_id = :organizationId AND imt_details.unit_id = :unitId");

			query = query.concat(subQuery.toString());

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

// Set parameters securely
			if (implantDetailsDto.getFromDate() != null && implantDetailsDto.getToDate() != null) {
				sqlQuery.setParameter("fromDate", implantDetailsDto.getFromDate());
				sqlQuery.setParameter("toDate", implantDetailsDto.getToDate());
			}

			if (implantDetailsDto.getCreatedBy() != null) {
				sqlQuery.setParameter("createdBy", implantDetailsDto.getCreatedBy());
			}

			sqlQuery.setParameter("organizationId", implantDetailsDto.getOrganizationId());
			sqlQuery.setParameter("unitId", implantDetailsDto.getUnitId());

			List<ImplantDetailsDto> implantDetailsList = sqlQuery.setResultTransformer(Transformers.aliasToBean(ImplantDetailsDto.class)).list();


			return new Response<>(SUCCESS, SUCCESS_CODE, null, implantDetailsList, null);
		}catch(Exception e){
		logger.error("getImplantdetailsList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getImplantdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IMPLANT_DETAILS")
					.getQueryString();

			StringBuilder subQuery = new StringBuilder();

			boolean hasWhereClause = false;  // Flag to track if WHERE clause has been added

// Add conditions to the subquery with secure parameters
			if (implantDetailsDto.getCreatedDate() != null) {
				subQuery.append(" WHERE to_char(imt_details.created_date, 'DD-MM-YYYY') = :createdDate");
				hasWhereClause = true;
			}

			if (implantDetailsDto.getCreatedBy() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" imt_details.created_by = :createdBy");
				hasWhereClause = true;
			}

			if (implantDetailsDto.getPatientId() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" imt_details.patient_id = :patientId");
				hasWhereClause = true;
			}

			if (implantDetailsDto.getProcedureOrderId() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" imt_details.procedure_order_id = :procedureOrderId");
				hasWhereClause = true;
			}

// Add the organization and unit ID conditions
			subQuery.append(hasWhereClause ? " AND" : " WHERE");
			subQuery.append(" imt_details.organization_id = :organizationId AND imt_details.unit_id = :unitId");

			query = query.concat(subQuery.toString());

// Create the SQL query with the constructed conditions
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

// Set parameters securely
			if (implantDetailsDto.getCreatedDate() != null) {
				sqlQuery.setParameter("createdDate", implantDetailsDto.getCreatedDate());
			}

			if (implantDetailsDto.getCreatedBy() != null) {
				sqlQuery.setParameter("createdBy", implantDetailsDto.getCreatedBy());
			}

			if (implantDetailsDto.getPatientId() != null) {
				sqlQuery.setParameter("patientId", implantDetailsDto.getPatientId());
			}

			if (implantDetailsDto.getProcedureOrderId() != null) {
				sqlQuery.setParameter("procedureOrderId", implantDetailsDto.getProcedureOrderId());
			}

			sqlQuery.setParameter("organizationId", implantDetailsDto.getOrganizationId());
			sqlQuery.setParameter("unitId", implantDetailsDto.getUnitId());

			List<ImplantDetailsDto> implantDetailsList = sqlQuery.setResultTransformer(Transformers.aliasToBean(ImplantDetailsDto.class)).list();


			return new Response<>(SUCCESS, SUCCESS_CODE, null, implantDetailsList, null);
		}catch(Exception e){
		logger.error("getImplantdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getKitdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_KIT_DETAILS")
					.getQueryString();

			StringBuilder subQuery = new StringBuilder();
			boolean hasWhereClause = false;  // Flag to track if WHERE clause has been added

// Add conditions to the subquery with secure parameters
			if (implantDetailsDto.getCreatedDate() != null) {
				subQuery.append(" WHERE to_char(imt_details.created_date, 'DD-MM-YYYY') = :createdDate");
				hasWhereClause = true;
			}

			if (implantDetailsDto.getCreatedBy() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" imt_details.created_by = :createdBy");
				hasWhereClause = true;
			}

			if (implantDetailsDto.getPatientId() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" imt_details.patient_id = :patientId");
				hasWhereClause = true;
			}

			if (implantDetailsDto.getProcedureOrderId() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" imt_details.procedure_order_id = :procedureOrderId");
				hasWhereClause = true;
			}

// Add the organization and unit ID conditions
			subQuery.append(hasWhereClause ? " AND" : " WHERE");
			subQuery.append(" imt_details.organization_id = :organizationId AND imt_details.unit_id = :unitId");

			query = query.concat(subQuery.toString());

// Create the SQL query with the constructed conditions
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

// Set parameters securely
			if (implantDetailsDto.getCreatedDate() != null) {
				sqlQuery.setParameter("createdDate", implantDetailsDto.getCreatedDate());
			}

			if (implantDetailsDto.getCreatedBy() != null) {
				sqlQuery.setParameter("createdBy", implantDetailsDto.getCreatedBy());
			}

			if (implantDetailsDto.getPatientId() != null) {
				sqlQuery.setParameter("patientId", implantDetailsDto.getPatientId());
			}

			if (implantDetailsDto.getProcedureOrderId() != null) {
				sqlQuery.setParameter("procedureOrderId", implantDetailsDto.getProcedureOrderId());
			}
			sqlQuery.setParameter("organizationId", implantDetailsDto.getOrganizationId());
			sqlQuery.setParameter("unitId", implantDetailsDto.getUnitId());
			List<ImplantDetailsDto> kitDetailsList = sqlQuery.setResultTransformer(Transformers.aliasToBean(ImplantDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, kitDetailsList, null);
		}catch(Exception e){
		logger.error("getKitdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchImplantItems(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
				List<ImplantDetailsDto> itemList = (List<ImplantDetailsDto>)sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_IMPLANT_ITEMS_BY_NAME")
					.setParameter("itemName",  implantDetailsDto.getItemName() != null ? implantDetailsDto.getItemName().toLowerCase() + '%' : "")
					.setParameter("organizationId", implantDetailsDto.getOrganizationId())
					.setParameter("unitId", implantDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ImplantDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, itemList, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getImplantDetailsByProcedureId(ImplantDetailsDto implantDetailsDto,int flag) throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IMPLANT_ITEMS")
					.getQueryString();

			StringBuilder subQuery = new StringBuilder();
			boolean hasWhereClause = false;  // Flag to track if WHERE clause is added
			if (implantDetailsDto.getProcedureOrderId() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" implant_details.procedure_order_id = :procedureOrderId");
				hasWhereClause = true;
				if (flag == 0) {
					subQuery.append(" AND implant_details.is_exported = false");
				}
			}
			subQuery.append(hasWhereClause ? " AND" : " WHERE");
			subQuery.append(" implant_details.organization_id = :organizationId AND implant_details.unit_id = :unitId");

			query = query.concat(subQuery.toString());

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

			if (implantDetailsDto.getProcedureOrderId() != null) {
				sqlQuery.setParameter("procedureOrderId", implantDetailsDto.getProcedureOrderId());
			}

			sqlQuery.setParameter("organizationId", implantDetailsDto.getOrganizationId());
			sqlQuery.setParameter("unitId", implantDetailsDto.getUnitId());

			List<ImplantDetailsDto> implantDetailsList = sqlQuery.setResultTransformer(Transformers.aliasToBean(ImplantDetailsDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, implantDetailsList, null);
		}catch(Exception e){
		logger.error("getImplantDetailsByProcedureId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchImplantKit(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			List<ImplantDetailsDto> itemList = (List<ImplantDetailsDto>)sessionFactory.getCurrentSession()
				.getNamedQuery("SEARCH_IMPLANT_KIT_BY_NAME")
				.setParameter("kitName",  implantDetailsDto.getKitName() != null ? implantDetailsDto.getKitName().toLowerCase() + '%' : "")
				.setParameter("organizationId", implantDetailsDto.getOrganizationId())
				.setParameter("unitId", implantDetailsDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(ImplantDetailsDto.class)).list();
			
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, itemList, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getImplantIndentAcceptanceData(IssueAcceptanceDto issueAcceptanceDto) throws ApplicationException {
		try{
			List<IssueAcceptanceDto> issueAcceptanceList = sessionFactory.getCurrentSession().createSQLQuery(
					"select pid.item_id as \"itemId\", "
					+"itm.name as \"itmName\", "
					+"itm.code as \"itemCode\", "
					+"umt.type as \"uomType\", "
					+"umt.id as \"uomTypeId\", "
					+"unt.code as \"uomUnit\", "
					+"unt.id as \"uomUnitId\", "
					+"ptx.id as \"purchaseTaxId\", "
					+"ptx.tax_percentage as \"purchaseTaxPercentage\", "
					+"st.store_name as \"storeName\", "
					+"idbm.amount as \"mrp\", "
					+"idbm.accepted_quantity as\"bookedQty\", "
					+"idbm.issue_quantity as\"issuedQty\", "
					+"to_char(batch.expiry_date,'YYYY-DD-MM') as \"expiryDate\", "
					+"generic.name as \"genericName\", "
					+"batch.batch_no as \"batchNo\", "
					+"batch.id as \"batchId\" "
					+"from pharmacy.t_issue issue "
					+"inner join pharmacy.t_issue_detail issue_detail on issue_detail.issue_id =issue.id "
					+"inner join pharmacy.t_patient_indent_detail pid on issue_detail.patient_indent_detail_id = pid.id "
					+"inner join pharmacy.t_patient_indent pi on pid.patient_indent_id=pi.id "
					+"inner join public.m_item itm on pid.item_id = itm.id "
					+"inner join public.m_uom_type umt on pid.uom_type_id = umt.id "
					+"inner join public.m_uom_unit unt on pid.uom_unit_id = unt.id "
					+"inner join public.m_store st on pi.store_id = st.store_id "
					+"inner join public.m_generic generic on itm.generic_id = generic.id "
					+"inner join pharmacy.t_issue_detail_batch_mapper idbm on issue_detail.id = idbm.issue_detail_id "
					+"inner join inventory.t_batch batch on idbm.batch_id = batch.id "
					+"inner join patient.t_patient_registration pr on pi.patient_id = pr.patient_id "
					+"inner join public.m_tax ptx on itm.p_tax_id = ptx.id "
					+"where issue.status_id=4 AND pr.uhid_number =:uhidNumber "
					).setParameter("uhidNumber",issueAcceptanceDto.getUhidNumber()).setResultTransformer(Transformers.aliasToBean(IssueAcceptanceDto.class)).list();
		
			//System.out.println("issueAcceptanceList"+issueAcceptanceList);
			////System.out.println("issueAcceptanceList"+query.list());
		return new Response<>("success", "200", "success", issueAcceptanceList,null);
		}
		 catch(Exception e){
		 logger.error("getImplantIndentAcceptanceData failed", e);
		 }
		return new Response<>("error", null, null, null,null);
	}

	@Override
	public Response updateImplantDetailsIsExported(List<ImplantDetailsDto> list) throws ApplicationException {
		try{
			for (ImplantDetailsDto implantDetailsDto : list) {
				
				 ImplantDetails mstr = findById(implantDetailsDto.getImplantDetailsId());
				 mstr.setUpdatedBy(implantDetailsDto.getUpdatedBy());
				 mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(implantDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				 mstr.setIsExported(true);
				 update(mstr);
			}
			 
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getImplantDetailsByProcedureIdOnOTIMS(ImplantDetailsDto implantDetailsDto)
			throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_IMPLANT_ITEMS")
					.getQueryString();

			StringBuilder subQuery = new StringBuilder();
			boolean hasWhereClause = false;  // Flag to track if WHERE clause is added

// Add procedure order condition
			if (implantDetailsDto.getProcedureOrderId() != null) {
				subQuery.append(hasWhereClause ? " AND" : " WHERE");
				subQuery.append(" implant_details.procedure_order_id = :procedureOrderId");
				hasWhereClause = true;
			}

// Add organization, status, and unit ID conditions
			subQuery.append(hasWhereClause ? " AND" : " WHERE");
			subQuery.append(" implant_details.organization_id = :organizationId AND implant_details.status = 'A' AND implant_details.unit_id = :unitId");

			query = query.concat(subQuery.toString());
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			if (implantDetailsDto.getProcedureOrderId() != null) {
				sqlQuery.setParameter("procedureOrderId", implantDetailsDto.getProcedureOrderId());
			}
			sqlQuery.setParameter("organizationId", implantDetailsDto.getOrganizationId());
			sqlQuery.setParameter("unitId", implantDetailsDto.getUnitId());
			List<ImplantDetailsDto> implantDetailsList = sqlQuery.setResultTransformer(Transformers.aliasToBean(ImplantDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, implantDetailsList, null);
		}catch(Exception e){
		logger.error("getImplantDetailsByProcedureIdOnOTIMS failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelImplantdetails(OTReservationDto oTReservationdto) throws ApplicationException {
		try{
			String query = "UPDATE otims.t_implant_details SET status = 'I' WHERE procedure_order_id = :procedureOrderId";

			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("procedureOrderId", oTReservationdto.getProcedureOrderMasterDto().getFirst().getProcedureOrderId())
					.executeUpdate();

			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("cancelImplantdetails failed", he);
		} catch (Exception e) {
		logger.error("cancelImplantdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
