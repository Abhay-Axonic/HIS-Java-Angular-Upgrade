package com.param.global.doctor.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.param.global.common.GlobalCommonDateUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.doctor.model.OrdersetMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OrderSetReqDto;
import com.param.global.doctor.dto.OrdersetCreationReqDto;
import com.param.global.doctor.dto.OrdersetInvestigationDetailsDto;
import com.param.global.doctor.dto.OrdersetMasterDto;
import com.param.global.doctor.dto.OrdersetMedicationDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OrdersetMasterDaoImpl extends GenericDao<OrdersetMaster, Integer> implements IOrdersetMasterDao, ICommonConstants{

	public OrdersetMasterDaoImpl() {
		super(OrdersetMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveOrderset(OrdersetMasterDto dto) throws ApplicationException {
		try{
			OrdersetMaster master = new OrdersetMaster();
			master.setIcpcId(dto.getIcpcId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setDiagnosisId(dto.getDiagnosisId());
			master.setDoctorId(dto.getDoctorId());
			master.setOrdersetName(dto.getOrdersetName());
			master.setOrdersetTypeId(dto.getOrdersetTypeId());
			master.setOrgId(dto.getOrgId());
			master.setSpecialityId(dto.getSpecialityId());
			master.setStatus(dto.getStatus());
			master.setUnitId(dto.getUnitId());
			master.setIsDeleted('N');
//			master.setUnitId(dto.getUnitId());
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOrderSet(OrderSetReqDto reqDto) throws ApplicationException {
		try{
			String query1 = sessionFactory.getCurrentSession().createSQLQuery( "select "
					+ "	order_set.orderset_master_id as \"ordersetMasterId\", "
					+ "	order_set.orderset_name as \"ordersetName\" "
					+ "from "
					+ "	doctor.m_orderset_master order_set "
					+ "where "
					+ "	order_set.status = 'A' "
					+ "	and order_set.org_id = :orgId "
					+ "	and order_set.unit_id = :unitId "
					+ "	and order_set.diagnosis_id = :diagnosisId "
					+ "	and order_set.orderset_type_id = 1"
					+ " and order_set.is_deleted!='Y'").getQueryString();
			
			String query2 = sessionFactory.getCurrentSession().createSQLQuery(" union select "
					+ "	order_set.orderset_master_id as \"ordersetMasterId\", "
					+ "	order_set.orderset_name as \"ordersetName\" "
					+ "from "
					+ "	doctor.m_orderset_master order_set "
					+ "where "
					+ "	order_set.status = 'A' "
					+ "	and order_set.org_id = :orgId "
					+ "	and order_set.unit_id = :unitId "
					+ "	and order_set.diagnosis_id = :diagnosisId "
					+ " and order_set.is_deleted!='Y'").getQueryString();

			String query3 = sessionFactory.getCurrentSession().createSQLQuery(" union select "
					+ "	order_set.orderset_master_id as \"ordersetMasterId\", "
					+ "	order_set.orderset_name as \"ordersetName\" "
					+ "from "
					+ "	doctor.m_orderset_master order_set "
					+ "where "
					+ "	order_set.status = 'A' "
					+ "	and order_set.org_id = :orgId "
					+ "	and order_set.unit_id = :unitId "
					+ "	and order_set.diagnosis_id = :diagnosisId "
					+ "	and order_set.orderset_type_id = 3"
					+ " and order_set.is_deleted!='Y'").getQueryString();
			
			StringBuilder q1 = new StringBuilder(query1);
			
			if(reqDto.getDoctorId()!=0 || reqDto.getSpecialityId()!=0)
			{
				q1.append(query2);
				q1.append(reqDto.getSpecialityId()!=0?" and order_set.speciality_id = "+reqDto.getSpecialityId():"");
				q1.append(reqDto.getDoctorId()!=0? "  and order_set.doctor_id = "+reqDto.getDoctorId():"");

				q1.append(query3);
				q1.append(reqDto.getSpecialityId()!=0?" and order_set.speciality_id = "+reqDto.getSpecialityId():"");
			}

		
			List<OrdersetMasterDto> listOrdersetMasterDto = sessionFactory.getCurrentSession().createSQLQuery(q1.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("diagnosisId", reqDto.getDiagnosisId())
					.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOrdersetMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOrderSetOrders(OrderSetReqDto reqDto) throws ApplicationException {
		try{
			List<OrdersetMasterDto> listOrdersetMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORDER_SET")
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameterList("ordersetMasterId", reqDto.getOrdersetMasterId())
					.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOrdersetMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getInvestigationByOrderSet(OrdersetMasterDto reqDto) throws ApplicationException {
		try{
			List<OrdersetInvestigationDetailsDto> listOrdersetInvestigationDetails = sessionFactory.getCurrentSession().getNamedQuery("GET_INVESTIGATION_BY_ORDERSET")
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("ordersetMasterId",reqDto.getOrdersetMasterId())
						.setResultTransformer(Transformers.aliasToBean(OrdersetInvestigationDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOrdersetInvestigationDetails, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getMedicationByOrderset(OrdersetMasterDto reqDto) throws ApplicationException {
		try{
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(reqDto.getUnitId()));
			
			List<OrdersetMedicationDetailsDto> listOrdersetMedicationDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_MEDICATION_BY_ORDERSET")
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("expDate", toDayDate)
					.setParameter("ordersetMasterId",reqDto.getOrdersetMasterId())
						.setResultTransformer(Transformers.aliasToBean(OrdersetMedicationDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOrdersetMedicationDetailsDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOrderSetsList(OrdersetMasterDto dto) throws ApplicationException {
		try{
			List<OrdersetMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER")
					.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();

			List<OrdersetMasterDto> listIcpc = sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER_FOR_ICPC")
					.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null,null,listIcpc);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOrderSetsListCount(OrdersetMasterDto dto) throws ApplicationException {
		try{
			BigInteger count = (BigInteger) sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER_COUNT")
					.setParameter("unitId", dto.getUnitId())
						.uniqueResult();

			BigInteger countForICPC = (BigInteger) sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER_COUNT_FOR_ICPC")
					.setParameter("unitId", dto.getUnitId())
						.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count,countForICPC);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response orderSetsDetails(OrdersetMasterDto dto) throws ApplicationException {
		try{
			if (dto.getOrdersetMasterId() != 0) {
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(dto.getUnitId()));

				OrdersetMasterDto dto1 = new OrdersetMasterDto();
				OrdersetCreationReqDto orderSetReqDto = new OrdersetCreationReqDto();
				if(dto.getOrderSetActiveTab()==1) {
					dto1 = (OrdersetMasterDto) sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER_BY_ID")
							.setParameter("ordersetMasterId", dto.getOrdersetMasterId())
							.setParameter("unitId", dto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).uniqueResult();
				}else{
					dto1 = (OrdersetMasterDto) sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER_BY_ID_FOR_ICPC")
							.setParameter("ordersetMasterId", dto.getOrdersetMasterId())
							.setParameter("unitId", dto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).uniqueResult();
				}

				List<OrdersetInvestigationDetailsDto> listOrdersetInvestigationDetails = sessionFactory.getCurrentSession().getNamedQuery("GET_INVESTIGATION_BY_ORDERSET")
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("ordersetMasterId", dto1.getOrdersetMasterId())
						.setResultTransformer(Transformers.aliasToBean(OrdersetInvestigationDetailsDto.class)).list();

				List<OrdersetMedicationDetailsDto> listOrdersetMedicationDetailsDto = sessionFactory.getCurrentSession().getNamedQuery("GET_MEDICATION_BY_ORDERSET")
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("expDate", toDayDate)
						.setParameter("ordersetMasterId", dto1.getOrdersetMasterId())
						.setResultTransformer(Transformers.aliasToBean(OrdersetMedicationDetailsDto.class)).list();
				orderSetReqDto.setOrdersetMaster(dto1);
				orderSetReqDto.setOrdersetMedications(listOrdersetMedicationDetailsDto);
				orderSetReqDto.setOrdersetInvestigations(listOrdersetInvestigationDetails);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, orderSetReqDto);
			}else{
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				List<OrdersetCreationReqDto> responseList = new ArrayList<OrdersetCreationReqDto>();
				List<OrdersetMasterDto> ordersetMasters = null;

				if(dto.getOrderSetActiveTab()==1) {
					ordersetMasters = sessionFactory.getCurrentSession()
							.getNamedQuery("LIST_ORDER_MASTER_BY_DIAGNOSIS_ID")
							.setParameter("diagnosisId", dto.getDiagnosisId())
							.setParameter("unitId", dto.getUnitId())
							.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class))
							.list();
				}else {
					ordersetMasters = sessionFactory.getCurrentSession()
							.getNamedQuery("LIST_ORDER_MASTER_BY_ICPC_ID")
							.setParameter("icpcId", dto.getIcpcId())
							.setParameter("unitId", dto.getUnitId())
							.setParameter("doctorId", dto.getDoctorId())
							.setParameter("specialityId", dto.getSpecialityId())
							.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class))
							.list();
				}
				if (ordersetMasters != null && !ordersetMasters.isEmpty()) {
					for (OrdersetMasterDto master : ordersetMasters) {
						OrdersetCreationReqDto orderSetReqDto = new OrdersetCreationReqDto();
						List<OrdersetInvestigationDetailsDto> listOrdersetInvestigationDetails =
								sessionFactory.getCurrentSession()
										.getNamedQuery("GET_INVESTIGATION_BY_ORDERSET")
										.setParameter("orgId", dto.getOrgId())
										.setParameter("unitId", dto.getUnitId())
										.setParameter("ordersetMasterId", master.getOrdersetMasterId())
										.setResultTransformer(Transformers.aliasToBean(OrdersetInvestigationDetailsDto.class))
										.list();
						List<OrdersetMedicationDetailsDto> listOrdersetMedicationDetailsDto =
								sessionFactory.getCurrentSession()
										.getNamedQuery("GET_MEDICATION_BY_ORDERSET")
										.setParameter("orgId", dto.getOrgId())
										.setParameter("unitId", dto.getUnitId())
										.setParameter("expDate", toDayDate)
										.setParameter("ordersetMasterId", master.getOrdersetMasterId())
										.setResultTransformer(Transformers.aliasToBean(OrdersetMedicationDetailsDto.class))
										.list();
						orderSetReqDto.setOrdersetMaster(master);
						orderSetReqDto.setOrdersetInvestigations(listOrdersetInvestigationDetails);
						orderSetReqDto.setOrdersetMedications(listOrdersetMedicationDetailsDto);
						responseList.add(orderSetReqDto);
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, responseList, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response changeStatusOrderSet(Integer ordersetMasterId, Character status) throws ApplicationException {
		try{
			Integer count = (Integer) sessionFactory.getCurrentSession().getNamedQuery("UPDATE_ORDER_SET_STATUS")
					.setParameter("status", status)
					.setParameter("ordersetMasterId", ordersetMasterId)
						.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, count);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response orderSetSearch(int unitId, String searchkeyWord,int orderSetActiveTab) throws ApplicationException {
		try{
			List<OrdersetMasterDto> list = null;
			if(orderSetActiveTab==1) {
				String query = sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER").getQueryString();
				StringBuilder sb = new StringBuilder(query);
				sb.append(" and lower(osm.orderset_name) like lower('%" + searchkeyWord + "%')");
				 list = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();
			}else {
				String query = sessionFactory.getCurrentSession().getNamedQuery("LIST_ORDER_MASTER_FOR_ICPC").getQueryString();
				StringBuilder sb = new StringBuilder(query);
				sb.append(" and lower(osm.orderset_name) like lower('%" + searchkeyWord + "%')");
				list = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateOrderSetsDetails(OrdersetMasterDto dto) throws ApplicationException {
		 try {
				
			 Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_ORDER_SETS_DETAILS")
						.setParameter("isDeleted", 'Y')
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("ordersetMasterId", dto.getOrdersetMasterId())
						.executeUpdate();
				if (result > 0) {
					OrdersetMaster master = new OrdersetMaster();
					master.setCreatedBy(dto.getCreatedBy());
					master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.setDiagnosisId(dto.getDiagnosisId());
					master.setIcpcId(dto.getIcpcId());
					master.setDoctorId(dto.getDoctorId());
					master.setOrdersetName(dto.getOrdersetName());
					master.setOrdersetTypeId(dto.getOrdersetTypeId());
					master.setOrgId(dto.getOrgId());
					master.setSpecialityId(dto.getSpecialityId());
					master.setStatus(dto.getStatus());
					master.setUnitId(dto.getUnitId());
					master.setIsDeleted('N');
					master = save(master);
				
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, master);

					}
				
				
				} catch (HibernateException he) {
			 		throw he;
				} catch (Exception e) {
					throw e;
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response checkOrderset(OrdersetMasterDto dto) throws ApplicationException {

		try{

			boolean isCreatedOrderset = false;
			List<OrdersetMasterDto> ordersetList = null;
			if(dto.getDiagnosisId()!=null || (dto.getOrderSetActiveTab()!=null && dto.getOrderSetActiveTab()==1)) {
				StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_ORDER_SET_BY_DIAGNOSIS").getQueryString());

				ordersetList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("diagnosisId", dto.getDiagnosisId())
						.setParameter("ordersetName", dto.getOrdersetName())
						.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();
			}else{
				StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_ORDER_SET_BY_ICPC_ID").getQueryString());

				ordersetList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("icpcId", dto.getIcpcId())
						.setParameter("ordersetName", dto.getOrdersetName())
						.setResultTransformer(Transformers.aliasToBean(OrdersetMasterDto.class)).list();
			}
			if(ordersetList.size()>0){

				for(OrdersetMasterDto ordersetMasterIdx: ordersetList) {
					if(ordersetMasterIdx.getOrdersetTypeId()==1){
						isCreatedOrderset = true;
						return new Response<>(SUCCESS, SUCCESS_CODE, "Oderset Already Present Globally", ordersetList, isCreatedOrderset);
					} else if (ordersetMasterIdx.getOrdersetTypeId()==3) {
						if(dto.getOrdersetTypeId()==1){
							if(dto.getOrdersetMasterId()==0) {
								removeOrederset(ordersetMasterIdx.getOrdersetMasterId(), dto, ordersetList);
							}
							//update
							return new Response<>(SUCCESS, SUCCESS_CODE, null, ordersetList, isCreatedOrderset);
						} else if (dto.getOrdersetTypeId()==3) {
							if(Objects.equals(dto.getSpecialityId(), ordersetMasterIdx.getSpecialityId())){
								isCreatedOrderset = true;
								return new Response<>(SUCCESS, SUCCESS_CODE, "Oderset Already Present", ordersetList, isCreatedOrderset);
							}
						} else if (dto.getOrdersetTypeId()==2) {
							if(Objects.equals(dto.getSpecialityId(), ordersetMasterIdx.getSpecialityId())){
								isCreatedOrderset = true;
								return new Response<>(SUCCESS, SUCCESS_CODE, "Oderset Already Present", ordersetList, isCreatedOrderset);
							}
						}
					} else if (ordersetMasterIdx.getOrdersetTypeId()==2) {
						if(dto.getOrdersetTypeId()==1){
							if(dto.getOrdersetMasterId()==0) {
								removeOrederset(ordersetMasterIdx.getOrdersetMasterId(), dto, ordersetList);
							}
							//update
							return new Response<>(SUCCESS, SUCCESS_CODE, null, ordersetList, isCreatedOrderset);
						}else if (dto.getOrdersetTypeId()==3) {
							if(Objects.equals(dto.getSpecialityId(), ordersetMasterIdx.getSpecialityId())){
								if(dto.getOrdersetMasterId()==0) {
									removeOrederset(ordersetMasterIdx.getOrdersetMasterId(), dto, ordersetList);
								}
								//update
								return new Response<>(SUCCESS, SUCCESS_CODE, null, ordersetList, isCreatedOrderset);
							}
						} else if (dto.getOrdersetTypeId()==2) {
							if(Objects.equals(dto.getSpecialityId(), ordersetMasterIdx.getSpecialityId()) && Objects.equals(dto.getDoctorId(), ordersetMasterIdx.getDoctorId())){
								isCreatedOrderset = true;
								return new Response<>(SUCCESS, SUCCESS_CODE, "Oderset Already Present", ordersetList, isCreatedOrderset);
							}
						}
					}
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, ordersetList, isCreatedOrderset);

		}catch (Exception e){
			throw e;
		}

	}

	private void removeOrederset(int ordersetMasterId, OrdersetMasterDto dto, List<OrdersetMasterDto> ordersetList) {

		if(dto.getOrdersetTypeId()==1){
			for(OrdersetMasterDto idx: ordersetList){
				sessionFactory.getCurrentSession().createSQLQuery("delete from doctor.t_orderset_medication_details where orderset_master_id = :ordersetMasterId ")
						.setParameter("ordersetMasterId", idx.getOrdersetMasterId())
						.executeUpdate();

				sessionFactory.getCurrentSession().createSQLQuery("delete from doctor.t_orderset_investigation_details where orderset_master_id = :ordersetMasterId ")
						.setParameter("ordersetMasterId", idx.getOrdersetMasterId())
						.executeUpdate();

				delete(idx.getOrdersetMasterId());
			}
		}else {
			sessionFactory.getCurrentSession().createSQLQuery("delete from doctor.t_orderset_medication_details where orderset_master_id = :ordersetMasterId ")
					.setParameter("ordersetMasterId", ordersetMasterId)
					.executeUpdate();

			sessionFactory.getCurrentSession().createSQLQuery("delete from doctor.t_orderset_investigation_details where orderset_master_id = :ordersetMasterId ")
					.setParameter("ordersetMasterId", ordersetMasterId)
					.executeUpdate();

			delete(ordersetMasterId);
		}

	}

}
