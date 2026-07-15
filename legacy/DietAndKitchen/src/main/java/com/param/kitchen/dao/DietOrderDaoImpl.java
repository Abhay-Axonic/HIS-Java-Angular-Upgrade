package com.param.kitchen.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.kitchen.dto.DietOrderDto;
import com.param.kitchen.dto.DietOrderReponseDto;
import com.param.kitchen.dto.DietOrderRequestDto;
import com.param.kitchen.dto.PatientDetailsDto;

import in.param.exception.ApplicationException;


@Component
public class DietOrderDaoImpl implements IDietOrderDao,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietOrderDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	DozerBeanMapper mapper;
	@Autowired
	IDietPlanningDao iDietPlanningDao;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietOrderByMultipleFields(DietOrderRequestDto dietOrderRequestDto,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(dietOrderRequestDto.getFloorId()!=null)
			{
				condition.append(" AND dietOrder.floorId=:floorId ");
			}
			if(dietOrderRequestDto.getWardId()!=null)
			{
				condition.append(" AND dietOrder.wardId=:wardId ");
			}
			if(dietOrderRequestDto.getMealId()!=null && dietOrderRequestDto.getMealId()!=0)
			{
				condition.append(" AND dietOrder.mealId=:mealId ");
			}
			
			
			String query1 = "SELECT dietOrder.orderId as orderId, dietOrder.patientId as patientId,"
					+ " dietOrder.floorId as floorId, dietOrder.wardId as wardId,dietOrder.bedId as bedId, dietOrder.mealNotes as mealNotes, "
					+ " dietOrder.mealId as mealId,mealMaster.name as mealName,dishMaster.dishDescription as dishName, dietOrder.quantity as quantity,"
					+ " dietOrder.status as status "
					+ " from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster INNER JOIN dietOrder.mealMaster as mealMaster WHERE dietOrder.status='A' "
					+ " AND to_char(dietOrder.dietDate, 'yyyy-mm-dd')='" + dietOrderRequestDto.getDietDate() + "' "
					+ condition + " AND dietOrder.isIssued=FALSE AND dietOrder.isAccepted=FALSE "
					+ " AND dietOrder.isRejected=FALSE AND dietOrder.rejectionStatus=FALSE AND dietOrder.organizationId="+organizationId+" ";
			
			/*String query1 = "SELECT dishMaster.dishDescription as dishName,"
					+ " sum(dietOrder.quantity) as quantity from DietOrder dietOrder "
					+ " INNER JOIN dietOrder.dishMaster as dishMaster WHERE dietOrder.status='A' "
					+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietOrderRequestDto.getDietDate()+" "+condition+" AND dietOrder.isIssued=FALSE "
					+ " AND dietOrder.isRejected=FALSE GROUP BY dishMaster.dishDescription ";*/
			
			List<DietOrderDto> dietOrderList=new ArrayList<>();
				Query query = sessionFactory.getCurrentSession()
						     .createQuery(query1);


			if (dietOrderRequestDto.getFloorId() != null) {
				query.setParameter("floorId", dietOrderRequestDto.getFloorId());
			}
			if (dietOrderRequestDto.getWardId() != null) {
				query.setParameter("wardId", dietOrderRequestDto.getWardId());
			}
			if(dietOrderRequestDto.getMealId()!=null && dietOrderRequestDto.getMealId()!=0){
				query.setParameter("mealId", dietOrderRequestDto.getMealId());
			}


			query.setResultTransformer(Transformers.aliasToBean(DietOrderDto.class));
				dietOrderList = query.list();
			
				
				Map<String, DietOrderReponseDto> mapOfDtos = new HashMap();

				if (!dietOrderList.isEmpty()) {

					String key = "";
					for (DietOrderDto dietOrderDto : dietOrderList) {

						key = dietOrderDto.getDishName().concat(dietOrderDto.getMealName());
						
							if (mapOfDtos.containsKey(key)) {
								Integer value =null;
								if(dietOrderDto.getDishName() != null && dietOrderDto.getQuantity() != null ) {
									 value = mapOfDtos.get(key).getQuantity() + (dietOrderDto.getQuantity());
								}
								
								mapOfDtos.get(key).setQuantity( value!= null ? value :0);
								
								Response resp = getPatientDetailsByPatientId(dietOrderDto.getPatientId(),organizationId,unitId);
								PatientDetailsDto patientDetailsDto=(PatientDetailsDto)resp.getObject();
								if(!mapOfDtos.get(key).getPatientMap().containsKey(dietOrderDto.getPatientId()))
								{
									mapOfDtos.get(key).getPatientMap().put(dietOrderDto.getPatientId(),patientDetailsDto);
								}
								
							} else {
								DietOrderReponseDto dietOrderReponseDto=new DietOrderReponseDto();
								dietOrderReponseDto.setDishName(dietOrderDto.getDishName());
								dietOrderReponseDto.setMealName(dietOrderDto.getMealName());
								dietOrderReponseDto.setQuantity(dietOrderDto.getQuantity());
								dietOrderReponseDto.setMealNotes(dietOrderDto.getMealNotes()!=null ? dietOrderDto.getMealNotes():"");
								Map<Integer,PatientDetailsDto> patientMap= new HashMap();
								
								Response resp = getPatientDetailsByPatientId(dietOrderDto.getPatientId(),organizationId,unitId);
								PatientDetailsDto patientDetailsDto=(PatientDetailsDto)resp.getObject();
								patientMap.put(dietOrderDto.getPatientId()!= null ? dietOrderDto.getPatientId() : 0,patientDetailsDto);
								dietOrderReponseDto.setPatientMap(patientMap);
								
								mapOfDtos.put(key, dietOrderReponseDto);
							}
							
							List<PatientDetailsDto> patientMaplist = new ArrayList<>(mapOfDtos.get(key).getPatientMap().values());
							mapOfDtos.get(key).setPatientList(patientMaplist);
							if(patientMaplist != null && patientMaplist.size() != 0) {
								for (PatientDetailsDto dto : patientMaplist) {
									if( dto != null && (dto.getPatientId() != null && dto.getPatientId() != 0)) {
										String queryStr="select diet.meal_notes as \"mealNotes\"   " + 
												" from kitchen.diet_order diet where diet.patient_id =" + dto.getPatientId()+
												" and diet.organization_id =" + organizationId +
												" and diet.unit_id =" + unitId+
												" and diet.status ='A'   "
												+" and to_char(diet.diet_date,'yyyy-mm-dd')= '"+dietOrderRequestDto.getDietDate() + "' "
												+ " and diet.meal_notes is not null"
												+ " and meal_id = "+dietOrderDto.getMealId()
//												+ " and order_id = "+dietOrderDto.getOrderId()
												+" order by 1  desc limit 1";
										Query query11 = sessionFactory.getCurrentSession()
											     .createSQLQuery(queryStr) 
												.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));
										PatientDetailsDto list = (PatientDetailsDto)query11.uniqueResult();
										
										dto.setMealNotes(list != null && list.getMealNotes() != null ? list.getMealNotes():"");
									}
									
								}
							}
							
					}

				}

				List<DietOrderReponseDto> list = new ArrayList<>(mapOfDtos.values());

					
			if(!list.isEmpty())
			{
				return new Response(SUCCESS, null, null,list, null);
			}
			else
			{
				return new Response(SUCCESS, null,"No Diet Order is available.",null, null);
			}
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getPatientDetailsByPatientId(Integer patientId,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
				Query query = sessionFactory.getCurrentSession()
						     .getNamedQuery("GET_ADMITTED_PATIENT_LIST_BY_PATIENT_ID") 
						     .setParameter("patientId",patientId)
						     .setParameter("organizationId",organizationId)
						     .setParameter("unitId",unitId)
							.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));
					patientDetailsList = query.list();
			
							
			if(!patientDetailsList.isEmpty())
			{
				return new Response(SUCCESS, null, null,null,patientDetailsList.getFirst());
			}
			else
			{
				return new Response(SUCCESS, null,"No patient is available.",null, null);
			}

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
