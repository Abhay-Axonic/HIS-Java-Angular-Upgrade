package com.param.opd.coversheet.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PendingNursingOrderAdministrationDto;
import com.param.opd.coversheet.dto.PendingNursingOrderAdministrationDto;
import com.param.opd.coversheet.model.PendingNursingOrderAdministrationMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class PendingNursingOrderAdministrationDaoImpl extends GenericDao<PendingNursingOrderAdministrationMaster, Integer> implements IPendingNursingOrderAdministrationDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(PendingNursingOrderAdministrationDaoImpl.class);


	public PendingNursingOrderAdministrationDaoImpl() {
		super(PendingNursingOrderAdministrationMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;

		@Override
		public Response saveActualAdministrationTime(PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto)
				throws ApplicationException {
			try {

				LocalTime time1 = LocalTime.parse( pendingNursingOrderAdministrationDto.getPendingNurOrdAdministrationTime());
				LocalDate today = LocalDate.now();
				Instant time = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				LocalTime admTime1 = LocalTime.parse( pendingNursingOrderAdministrationDto.getAdministrationTime());
				Instant admTime = admTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				PendingNursingOrderAdministrationMaster master = new PendingNursingOrderAdministrationMaster();
				master.setPendingNurOrdAdministrationTime(time);
				master.setPendingNurOrdAdministrationDate(GlobalCommonDateUtils.getDate(pendingNursingOrderAdministrationDto.getPendingNurOrdAdministrationDate(), "yyyy-M-dd HH:ss:mm"));
				master.setFrequencyId(pendingNursingOrderAdministrationDto.getFrequencyId());
				master.setReasonId(pendingNursingOrderAdministrationDto.getReasonId());
				master.setAdmissionId(pendingNursingOrderAdministrationDto.getAdmissionId());
				master.setPatientId(pendingNursingOrderAdministrationDto.getPatientId());
				master.setAdministrationTime(admTime);
				master.setNursingOrderMapperId(pendingNursingOrderAdministrationDto.getNursingOrderMapperId());
				master.setOrganizationId(pendingNursingOrderAdministrationDto.getOrganizationId());
				master.setUnitId(pendingNursingOrderAdministrationDto.getUnitId());
				master.setStatus(pendingNursingOrderAdministrationDto.getStatus());
				master.setRemark(pendingNursingOrderAdministrationDto.getRemark());
				master.setCreatedBy(pendingNursingOrderAdministrationDto.getCreatedBy());
				master.setCreatedDate(GlobalCommonDateUtils.getDate(pendingNursingOrderAdministrationDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				master.setUpdatedBy(pendingNursingOrderAdministrationDto.getUpdatedBy());
				master.setUpdatedDate(GlobalCommonDateUtils.getDate(pendingNursingOrderAdministrationDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				
				save(master);
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
				} catch (HibernateException he) {
				logger.error("saveActualAdministrationTime failed", he);
				} catch (Exception e) {
				logger.error("saveActualAdministrationTime failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getPendingNurOrdListByPatientId(
				PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto) throws ApplicationException {
			try {
				List<PendingNursingOrderAdministrationDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_PENDING_NUR_ORD_LIST_BY_PATIENT_ID")
						.setParameter("patientId", pendingNursingOrderAdministrationDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(PendingNursingOrderAdministrationDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
					
				} catch (HibernateException he) {
				logger.error("getPendingNurOrdListByPatientId failed", he);
				} catch (Exception e) {
				logger.error("getPendingNurOrdListByPatientId failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
}
