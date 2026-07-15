package com.param.batchjobs.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.data.dto.ExceptionDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.SlotMasterDto;
import com.param.utility.TimeZoneComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes", "unchecked" ,"unused"})
@Repository
public class BatchDaoImpl implements IBatchDao,ICommonConstants{

	private static final Logger logger = LoggerFactory.getLogger(BatchDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	@Override
	public boolean isBatchExecuted(String timeZone, List<Integer> unitIds) throws Exception {
		try {
			
			String pattern = "yyyy-MM-dd";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));
			
			List list =sessionFactory.getCurrentSession()
					.createSQLQuery("select updated_date from m_batch_log  where is_called_again='N' AND to_char(updated_date,'yyyy-mm-dd')='"
							+ date + "' and time_zone='"+timeZone+"'")
					.list();
			if(list.size()>0){
			
				Connection connection =null;
				PreparedStatement statement =null;
				try {
						connection = driverManager.getConnection();
						List<String> ipMac = getMACANDIP();
						 statement = connection.prepareStatement("INSERT INTO public.m_batch_log "
				        		+ "(ip_address,mac_address,is_called_again,time_zone)VALUES('"+ipMac.get(0)+"','"+ipMac.get(1)+"','Y','"+timeZone+"');"); 
						int i = statement.executeUpdate();
						//connection.commit();
				}catch(Exception e){
					throw e;
				}finally {
					connection.close();
					if (statement != null) {
						statement.close();
					}
				}
				
				return false;
			}
			else 
				return true;
				
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean addBathLog(String timeZone, List<Integer> unitIds) throws SQLException {
		Connection connection =null;
		PreparedStatement statement =null;
		try {
			String pattern = "yyyy-MM-dd HH:mm:ss";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));

        	
				connection = driverManager.getConnection();
				List<String> ipMac = getMACANDIP();
				 statement = connection.prepareStatement("INSERT INTO public.m_batch_log "
		        		+ "(updated_date,ip_address,mac_address,is_called_again,time_zone)VALUES('"+date+"','"+ipMac.get(0)+"','"+ipMac.get(1)+"','N','"+timeZone+"');"); 
				int i = statement.executeUpdate();
				//connection.commit();
				return true;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}finally {
			connection.close();
			if (statement != null) {
				statement.close();
			}
		}
	}

	public static List<String> getMACANDIP(){

        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            List<String> list = new LinkedList<String>();
            list.add(ip.getHostAddress());
            list.add(sb.toString());
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
       }

	@Override
	public void clainSentToSmartCard() {
		
	}

	@Override
	public boolean isBatchExecutedRoomRent(String timeZone,List<Integer> unitIds) throws Exception {
		try {
			
			String pattern = "yyyy-MM-dd";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));
			
			List list =sessionFactory.getCurrentSession()
					.createSQLQuery("select updated_date from m_batch_log_room_rent  where is_called_again='N' AND to_char(updated_date,'yyyy-mm-dd')='"
							+ date + "' and time_zone='"+timeZone+"'")
					.list();
			if(list.size()>0){
			
				Connection connection =null;
				PreparedStatement statement =null;
				try {
						connection = driverManager.getConnection();
						List<String> ipMac = getMACANDIP();
						 statement = connection.prepareStatement("INSERT INTO public.m_batch_log_room_rent "
				        		+ "(ip_address,mac_address,is_called_again,time_zone)VALUES('"+ipMac.get(0)+"','"+ipMac.get(1)+"','Y','"+timeZone+"');"); 
						int i = statement.executeUpdate();
						//connection.commit();
				}catch(Exception e){
					throw e;
				}finally {
					connection.close();
					if (statement != null) {
						statement.close();
					}
				}
				
				return false;
			}
			else 
				return true;
				
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean addBathLogForRoomRent(String timeZone,List<Integer> unitIds) throws SQLException{
		Connection connection =null;
		PreparedStatement statement =null;
		try {
			
			String pattern = "yyyy-MM-dd HH:mm:ss";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));
        	
				connection = driverManager.getConnection();
				List<String> ipMac = getMACANDIP();
				 statement = connection.prepareStatement("INSERT INTO public.m_batch_log_room_rent "
		        		+ "(updated_date,ip_address,mac_address,is_called_again,time_zone)VALUES('"+date+"','"+ipMac.get(0)+"','"+ipMac.get(1)+"','N','"+timeZone+"');"); 
				int i = statement.executeUpdate();
				//connection.commit();
				return true;
		}catch(Exception e){
			throw e;
		}finally {
			connection.close();
			if (statement != null) {
				statement.close();
			}
		}
		
	}
	
	@Override
	public Response batchJobToUpdateAppointmentStatus() throws ApplicationException {
		try {
			
				Map<String,List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();
				if(timeZoneAndUnitsMap.size()>0) 
				{
					for (Map.Entry<String,List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) 
					{
						Date date = GlobalCommonDateUtils.getDateByzone(entry.getKey());
						List<Integer> unitIds = entry.getValue();
						sessionFactory.getCurrentSession()
						.createSQLQuery(" update " 
						 + " 	opd.m_appointment_master " 
						 + " set appointment_status_id = " 
						 + " 	(case " 
						 + " 		when (appointment_status_id = 1 " 
						 + " 		or appointment_status_id = 8) then 7 " 
						 + " 		when (appointment_status_id = 5 " 
						 + " 		or appointment_status_id = 6) then 9 " 
						 + " 	end), "
						 + " 	updated_date = '"+GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss")+"' " 
						 + " where  appointment_status_id in (1, 8, 5, 6) " 
						 + " 		and app_date_time + interval '24 Hours' < '"+GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss")+"' "
						 + "		and unit_id in (:ids) " )
						.setParameterList("ids", unitIds)
						.executeUpdate();
					
					/*sessionFactory.getCurrentSession()
						.createSQLQuery( " update " 
						 + " 	opd.m_appointment_master " 
						 + " set " 
						 + " 	appointment_status_id = 9, " 
						 + " 	updated_date = '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd HH:mm:ss")+"' " 
						 + " 	where " 
						 + " 		(appointment_status_id = 5 " 
						 + " 		or appointment_status_id = 6) " 
						 + " 		and app_date_time + interval '24 Hours' < '"+GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss")+"' "
						 + "		and unit_id in (:ids) " )
						.setParameterList("ids", unitIds)
						.executeUpdate();*/
					
					sessionFactory.getCurrentSession()
						.createSQLQuery( " update " 
								 + " 	opd.t_encounter_master enc " 
								 + " set " 
								 + " 	status_id = 9, " 
								 + " 	updated_date = '"+GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss")+"' " 
								 + " where " 
								 + " 	unit_id in (:ids) "
								 + "	and appointment_id in ( " 
								 + " 		select " 
								 + " 			mam.appointment_id " 
								 + " 		from " 
								 + " 			opd.m_appointment_master mam " 
								 + " 		where " 
								 + " 			enc.status_id in (5, 6, 8) " 
								 + " 			and enc.appointment_id = mam.appointment_id " 
								 + " 			and mam.app_date_time + interval '24 Hours' < '"+GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss")+"' )")
						.setParameterList("ids", unitIds)
						.executeUpdate();
					
					/*sessionFactory.getCurrentSession()
					.createSQLQuery( " update " 
							 + " 	opd.t_encounter_master enc " 
							 + " set " 
							 + " 	status_id = 7, " 
							 + " 	updated_date = '"+GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss")+"' " 
							 + " where " 
							 + " 	unit_id in (:ids) and appointment_id in ( " 
							 + " 	select " 
							 + " 		mam.appointment_id " 
							 + " 	from " 
							 + " 		opd.m_appointment_master mam " 
							 + " 	where " 
							 + " 		enc.status_id = 8 " 
							 + " 		and enc.appointment_id = mam.appointment_id " 
							 + " 		and mam.app_date_time + interval '24 Hours' < '"+GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd HH:mm:ss")+"' ) " )
					.setParameterList("ids", unitIds)
					.executeUpdate();*/
						
					}
				}
				
		
	return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			// Called per timezone-entry from RevertAllocatedQuantity's unprotected loops (no per-entry
			// try/catch there) — throwing here would abort every remaining hospital unit-group in the
			// same scheduled run. See docs/DECISION_NEEDED_HISWAR_BatchContinuation.md. Log only, preserve
			// existing fallback response (caller discards the return value either way).
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addExceptionLog(ExceptionDto exceptionDto) throws ApplicationException, SQLException {

        Connection connection =null;
		PreparedStatement statement =null;
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
            connection = driverManager.getConnection();
            String sql = "INSERT INTO public.exception_log"
                    + "(pay_load, class_name, method_name, error_msg, created_date) "
                    + "            VALUES('"+exceptionDto.getUnitIds()+"', " + "'"+exceptionDto.getClassName()+"', " +
                    "  '"+exceptionDto.getMethodName()+"', " +
                    "  '"+exceptionDto.getExceptionMessage()+"', "  +
                    " '"+date+"');";
             statement = connection.prepareStatement(sql);
            int i = statement.executeUpdate();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }finally {
            connection.close();
			if (statement != null) {
				statement.close();
			}
        }
		return null;
	}

	
	@Override
	public Response checkBatchJobExecutedOrNotForpostRoomRent(String timeZone, List<Integer> unitIds) throws ApplicationException {

		try {
			
			String pattern = "yyyy-MM-dd";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));
			List list =sessionFactory.getCurrentSession()
					.createSQLQuery("select updated_date from m_batch_log_room_rent  where to_char(updated_date,'yyyy-mm-dd')='"
							+ date + "' and time_zone='"+timeZone+"'")
					.list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	
	}

	@Override
	public Response checkBatchJobExecutedOrNotFormidnightBatch(String timeZone, List<Integer> unitIds)
			throws ApplicationException {
		try {
			
			String pattern = "yyyy-MM-dd";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));

			List list =sessionFactory.getCurrentSession()
					.createSQLQuery("select updated_date from m_batch_log  where to_char(updated_date,'yyyy-mm-dd')='"
							+ date + "' and time_zone='"+timeZone+"'")
					.list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	
	
	}

	@Override
	public Response saveLog(String timeZone, List<Integer> unitId, String func) throws Exception {
		Connection connection =null;
		PreparedStatement stmt= null;
		try {
			//System.out.println("Hemodialysis slot generation.......");
			String pattern = "dd-M-yyyy hh:mm:ss";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	String date = simpleDateFormat.format(new Date());
        	if(unitId != null){
        		 date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitId.get(0)));
        	}

			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			 stmt=connection.prepareStatement("insert into  public.t_check_log_execution (timezone, execution_date, units, function_name)  values(?,?,?,?)");
			stmt.setString(1,timeZone);
			stmt.setString(2,date);  
			stmt.setString(3,(unitId != null ?  unitId.toString().replaceAll("\\[", "").replaceAll("\\]", "") : ""));
			stmt.setString(4, func);
			stmt.executeUpdate();  
			 connection.commit(); 
        
		return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		throw e;
	}finally {
			connection.close();
			connection.setAutoCommit(true);
			if (stmt != null) {
				stmt.close();
			}
		}
      
	}

}
