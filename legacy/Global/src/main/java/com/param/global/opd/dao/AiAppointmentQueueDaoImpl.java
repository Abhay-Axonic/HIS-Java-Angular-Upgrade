package com.param.global.opd.dao;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.global.AIAppointmentQueueDto;
import com.param.opd.appointment.model.AIAppointmentQueue;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
public class AiAppointmentQueueDaoImpl  extends GenericDao<AIAppointmentQueue, Integer> implements IAiAppointmentQueueDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AiAppointmentQueueDaoImpl.class);


    public AiAppointmentQueueDaoImpl() {
        super(AIAppointmentQueue.class);
    }

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Response saveAIAppointmentInQueue(AIAppointmentQueueDto aiAppointmentQueueDto) throws ApplicationException {
        try{

            String sql = "select count(*) from  opd.ai_appointment_queue  where appointment_id = :appointmentId ";
            BigInteger count = (BigInteger) sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("appointmentId",aiAppointmentQueueDto.getAppointmentId()).uniqueResult();


            if(count.intValue()>0) {
                return new Response<>(SUCCESS, SUCCESS_CODE, "Appointment Already updated in Queue", null, null);
            }


            String appoSql = " select sm.doctor_id as \"doctorId\",am.created_by  as \"createdBy\"  from opd.m_appointment_master am inner join opd.m_slot_master sm on \n" +
                    " am.slot_id =sm.slot_id  where am.appointment_id = :appointmentId limit 1 ";

            AIAppointmentQueueDto dto = (AIAppointmentQueueDto) sessionFactory.getCurrentSession().createSQLQuery(appoSql)
                    .setParameter("appointmentId",aiAppointmentQueueDto.getAppointmentId())
                    .setResultTransformer(Transformers.aliasToBean(AIAppointmentQueueDto.class)).uniqueResult();
            aiAppointmentQueueDto.setDoctorId(dto.getDoctorId());
            aiAppointmentQueueDto.setChannelId(dto.getChannelId());
            aiAppointmentQueueDto.setCreatedBy(dto.getCreatedBy());

            AIAppointmentQueue  aiAppointmentQueue = new AIAppointmentQueue();
            aiAppointmentQueue.setAppointmentId(aiAppointmentQueueDto.getAppointmentId());
            aiAppointmentQueue.setStatus("PENDING");
            aiAppointmentQueue.setDoctorId(aiAppointmentQueueDto.getDoctorId());
            aiAppointmentQueue.setChannelId(1);
            aiAppointmentQueue.setCreatedBy(aiAppointmentQueueDto.getCreatedBy());
            aiAppointmentQueue.setUpdatedBy(aiAppointmentQueueDto.getCreatedBy());
            aiAppointmentQueue.setAiPayloadJson(aiAppointmentQueueDto.getAiPayloadJson());
            aiAppointmentQueue.setIsPayloadFinalize('N');

            sessionFactory.getCurrentSession().save(aiAppointmentQueue);
            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return new Response<>(ERROR, ERROR_CODE, null, null, null);
        }
    }

    @Override
    public Response updateQueueByAppointmentId(AIAppointmentQueueDto aiAppointmentQueueDto) throws ApplicationException {
        try{

            String sql1 = "select count(*) from  appointment.ai_appointment_queue  where appointment_id = :appointmentId ";
            BigInteger count = (BigInteger) sessionFactory.getCurrentSession().createSQLQuery(sql1)
                    .setParameter("appointmentId",aiAppointmentQueueDto.getAppointmentId()).uniqueResult();


            if(count.intValue() != 1) {
                return new Response(ERROR, null, "No Appointment Found In Queue", null,null);
            }

            String sql = "update appointment.ai_appointment_queue set consulted_by = :consultedBy, " +
                    " status= :status where appointment_id = :appointmentId ";
            sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("status","COMPLETED")
                    .setParameter("consultedBy",aiAppointmentQueueDto.getDoctorId())
                    .setParameter("appointmentId",aiAppointmentQueueDto.getAppointmentId()).executeUpdate();


            String getDoctordepartment = " select department_id from doctor.doctor_unit_department_mapper dudm where doctor_id " +
                    " = :doctorId limit 1";

            Integer departmentId =   (Integer) sessionFactory.getCurrentSession().createSQLQuery(getDoctordepartment)
                    .setParameter("doctorId",aiAppointmentQueueDto.getDoctorId())
                    .uniqueResult();


            //update Appointment table
            String sql4 = "update appointment.appointment_master set unit_id=:unitId,doctor_id = :doctorId,department_id= :departmentId " +
                    " where appointment_id = :appointmentId ";
            sessionFactory.getCurrentSession().createSQLQuery(sql4)
                    .setParameter("doctorId",aiAppointmentQueueDto.getDoctorId())
                    .setParameter("departmentId",departmentId)
                    .setParameter("unitId",aiAppointmentQueueDto.getUnitId())
                    .setParameter("appointmentId",aiAppointmentQueueDto.getAppointmentId()).executeUpdate();



            String sql5 = "  select CONCAT(dm.first_name, ' ', dm.last_name)  from doctor.doctor_master dm where doctor_id =:doctorId  limit 1";
            String doctorName =     (String) sessionFactory.getCurrentSession().createSQLQuery(sql5)
                    .setParameter("doctorId",aiAppointmentQueueDto.getDoctorId()).uniqueResult();

            return new Response(SUCCESS, null, null, null,doctorName);

        }catch(Exception e){
        logger.error("updateQueueByAppointmentId failed", e);
            return new Response(ERROR, null, null, null,null);
        }
    }

    @Override
    public Response getAIAppointmentCount(AIAppointmentQueueDto dto) {
      try{
                Date currentDate = TimeZoneComponent.getDateByzone(dto.getUnitId());
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
                String sql = "SELECT count(*) " +
                       "FROM opd.ai_appointment_queue aq " +
                       "INNER JOIN opd.m_appointment_master am " +
                       "    ON aq.appointment_id = am.appointment_id " +
                       "WHERE aq.status = 'PENDING' " +
                       "    AND am.unit_id = :unitId " +
                       "    AND TO_CHAR(am.appointment_date, 'YYYY-MM-DD') = :appointmentDate";

                BigInteger count =  (BigInteger) sessionFactory.getCurrentSession().createSQLQuery(sql)
                        .setParameter("appointmentDate",formattedDate)
                        .setParameter("unitId",dto.getUnitId())
                        .uniqueResult();

                return new Response(SUCCESS, null, null, null,count.intValue());

                }catch(Exception e){
                    logger.error(e.getMessage(), e);
                    return new Response<>(ERROR, ERROR_CODE, null, null, null);
                }
            }

    @Override
    public Response getAISummaryData(AIAppointmentQueueDto dto) {
        try{
            Date currentDate = TimeZoneComponent.getDateByzone(dto.getUnitId());
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
            String sql = "SELECT ai_payload_json " +
                    "FROM opd.ai_appointment_queue aq " +
                    "INNER JOIN opd.m_appointment_master am " +
                    "    ON aq.appointment_id = am.appointment_id " +
                    "WHERE am.appointment_id = :appointmentId " +
                    "    AND TO_CHAR(am.appointment_date, 'YYYY-MM-DD') = :appointmentDate limit 1";

            String aiSummary =  (String) sessionFactory.getCurrentSession().createSQLQuery(sql)
                    .setParameter("appointmentDate",formattedDate)
                    .setParameter("appointmentId",dto.getAppointmentId())
                    .uniqueResult();

            return new Response(SUCCESS, null, null, null,aiSummary);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return new Response<>(ERROR, ERROR_CODE, null, null, null);
        }
    }


}

