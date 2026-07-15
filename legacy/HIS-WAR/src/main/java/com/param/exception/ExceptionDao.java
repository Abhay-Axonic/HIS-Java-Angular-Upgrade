package com.param.exception;

import com.param.utility.TimeZoneComponent;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.param.global.common.ServiceException;

@Repository
public class ExceptionDao {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    HikariDataSource driverManager;


    public void addErrorLog(ErrorLog error) throws SQLException {
        Connection connection =null;
        PreparedStatement statement =null;
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
            connection = driverManager.getConnection();
            String sql = "INSERT INTO public.exception_log"
                    + "(url, http_method, parameters, pay_load, class_name, method_name, error_msg, issue_no, created_date) "
                    + "            VALUES('"+error.getUrl()+"', " +
                    "  '"+error.getMethod()+"', " +
                    "  '"+error.getParameters()+"', " +
                    "  '"+error.getPayLoad()+"', " +
                    "  '"+error.getClassName()+"', " +
                    "  '"+error.getMethodName()+"', " +
                    "  '"+error.getException().replaceAll("\\'", "").replaceAll("\\'", "")+"', " +
                    "  '"+error.getIssueNo()+"', " +
                    " '"+date+"');";
             statement = connection.prepareStatement(sql);
            int i = statement.executeUpdate();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }finally {
            connection.setAutoCommit(true);
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }
}
