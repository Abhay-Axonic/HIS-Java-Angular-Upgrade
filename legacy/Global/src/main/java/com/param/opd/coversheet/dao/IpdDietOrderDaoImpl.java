package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.adt.admission.model.AdmissionDetails;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdDietOrderDto;
import com.param.opd.coversheet.model.IpdDietOrder;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class IpdDietOrderDaoImpl extends GenericDao<IpdDietOrder, Integer> implements IIpdDietOrderDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(IpdDietOrderDaoImpl.class);


	public IpdDietOrderDaoImpl() {
		super(IpdDietOrder.class);
	}

		@Autowired
		SessionFactory sessionFactory;

		@Override
		public Response saveIpdDietOrder(List<IpdDietOrderDto> ipdDietOrderDtoList) throws ApplicationException {
			try {
				for (IpdDietOrderDto  ipdDietOrderList : ipdDietOrderDtoList) {
					IpdDietOrder ipdDietOrder = new IpdDietOrder();
					ipdDietOrder.setDietMealPreferenceId(ipdDietOrderList.getDietMealPreferenceId());
					ipdDietOrder.setAdmissionId(ipdDietOrderList.getAdmissionId());
					ipdDietOrder.setDoctorId(ipdDietOrderList.getDoctorId());
					ipdDietOrder.setPatientId(ipdDietOrderList.getPatientId());
					ipdDietOrder.setDietInstruction(ipdDietOrderList.getDietInstruction());
					ipdDietOrder.setDietStatus(ipdDietOrderList.getDietStatus());
					ipdDietOrder.setDietStartDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getDietStartDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setDietStopDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getDietStopDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setIsNbm(ipdDietOrderList.getIsNbm());
					ipdDietOrder.setCreatedDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setUpdatedDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setCreatedBy(ipdDietOrderList.getCreatedBy());
					ipdDietOrder.setUpdatedBy(ipdDietOrderList.getUpdatedBy());
					ipdDietOrder.setStatus(ipdDietOrderList.getStatus());
					ipdDietOrder.setUnitId(ipdDietOrderList.getUnitId());
					ipdDietOrder.setDietTypeId(ipdDietOrderList.getDietTypeId());
					ipdDietOrder.setOrganisationId(ipdDietOrderList.getOrganisationId());

					save(ipdDietOrder);

					AdmissionDetails admissionDetails = (AdmissionDetails)sessionFactory.getCurrentSession()
							.getNamedQuery("GET_ADMISSION_DETAILS_BY_ADMISSION_ID")
							.setParameter("organizationId", ipdDietOrderList.getOrganisationId())
							.setParameter("unitId", ipdDietOrderList.getUnitId())
							.setParameter("admissionId", ipdDietOrderList.getAdmissionId())
							.setResultTransformer(Transformers.aliasToBean(AdmissionDetails.class)).uniqueResult();

					if(admissionDetails != null){

						StringBuilder que= new StringBuilder("update AdmissionDetails dis set dis.mealPrefId = :mealPrefId");

						if(ipdDietOrderList.getDietTypeId()!= null){
							que.append(", dis.dietType = :dietTypeId");
						}

						que.append(" where dis.admissionId = :admissionId");

						Query updateAdmissionDetailQuery = sessionFactory.getCurrentSession()
//								.createQuery("update AdmissionDetails dis set dis.mealPrefId = :mealPrefId, dis.dietType = :dietTypeId where dis.admissionId = :admissionId")
								.createQuery(que.toString())
								.setParameter("mealPrefId", ipdDietOrderList.getDietMealPreferenceId())
								.setParameter("admissionId", ipdDietOrderList.getAdmissionId());

						if (ipdDietOrderList.getDietTypeId() != null) {
							updateAdmissionDetailQuery.setParameter("dietTypeId", ipdDietOrderList.getDietTypeId());
						}
						updateAdmissionDetailQuery.executeUpdate();

					}


				}
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

				} catch (HibernateException he) {
					throw he;
				} catch (Exception e) {
					throw e;
				}
//				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getIpdDietOrderList(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException {
			try {
				List<IpdDietOrderDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_IPD_DIET_ORDER_LIST")
						.setParameter("organisationId", ipdDietOrderDto.getOrganisationId())
						.setParameter("unitId", ipdDietOrderDto.getUnitId())
						.setParameter("patientId", ipdDietOrderDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(IpdDietOrderDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

				} catch (HibernateException he) {
				logger.error("getIpdDietOrderList failed", he);
				} catch (Exception e) {
				logger.error("getIpdDietOrderList failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response updateIpdDietOrder(List<IpdDietOrderDto> ipdDietOrderDtoList) throws ApplicationException {
			try {
				for (IpdDietOrderDto  ipdDietOrderList : ipdDietOrderDtoList) {
					IpdDietOrder ipdDietOrder = findById(ipdDietOrderList.getIpdDietOrderId());
					ipdDietOrder.setDietMealPreferenceId(ipdDietOrderList.getDietMealPreferenceId());
					ipdDietOrder.setAdmissionId(ipdDietOrderList.getAdmissionId());
					ipdDietOrder.setDoctorId(ipdDietOrderList.getDoctorId());
					ipdDietOrder.setPatientId(ipdDietOrderList.getPatientId());
					ipdDietOrder.setDietInstruction(ipdDietOrderList.getDietInstruction());
					ipdDietOrder.setDietStatus(ipdDietOrderList.getDietStatus());
					ipdDietOrder.setDietStartDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getDietStartDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setDietStopDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getDietStopDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setIsNbm(ipdDietOrderList.getIsNbm());
					ipdDietOrder.setCreatedDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setUpdatedDate(GlobalCommonDateUtils.getDate(ipdDietOrderList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setCreatedBy(ipdDietOrderList.getCreatedBy());
					ipdDietOrder.setUpdatedBy(ipdDietOrderList.getUpdatedBy());
					ipdDietOrder.setStatus(ipdDietOrderList.getStatus());
					ipdDietOrder.setUnitId(ipdDietOrderList.getUnitId());
					ipdDietOrder.setDietTypeId(ipdDietOrderList.getDietTypeId());
					ipdDietOrder.setOrganisationId(ipdDietOrderList.getOrganisationId());

				update(ipdDietOrder);
				}
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

				} catch (HibernateException he) {
				logger.error("updateIpdDietOrder failed", he);
				} catch (Exception e) {
				logger.error("updateIpdDietOrder failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response cancelDietOrder(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException {
			try {
					IpdDietOrder ipdDietOrder = findById(ipdDietOrderDto.getIpdDietOrderId());
					ipdDietOrder.setUpdatedDate(GlobalCommonDateUtils.getDate(ipdDietOrderDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					ipdDietOrder.setUpdatedBy(ipdDietOrderDto.getUpdatedBy());
					ipdDietOrder.setStatus('I');
					ipdDietOrder.setUnitId(ipdDietOrderDto.getUnitId());
					ipdDietOrder.setOrganisationId(ipdDietOrderDto.getOrganisationId());

				update(ipdDietOrder);

					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

				} catch (HibernateException he) {
				logger.error("cancelDietOrder failed", he);
				} catch (Exception e) {
				logger.error("cancelDietOrder failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}
