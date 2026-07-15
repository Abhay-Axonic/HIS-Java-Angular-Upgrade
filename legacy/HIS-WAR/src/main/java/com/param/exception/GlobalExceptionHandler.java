package com.param.exception;


import com.amazonaws.services.iotdata.model.MethodNotAllowedException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.service.items.CustomException;
import com.param.global.utility.ErrorCorrelationUtil;
import in.param.exception.ApplicationException;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.QueryParameterException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Exception Handler
 *
 * @author Ganesh Chaudhari
 */

@ControllerAdvice
public class GlobalExceptionHandler implements IError {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private BackLogService backLogService;

    @Autowired
    private RequestMappingHandlerMapping handler;



    // Handle Method Not Allowed (405) Exception
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleMethodNotAllowed(TypeMismatchException ex) {
        logger.warn("Method Not Allowed: {}", ex.getMessage());
        return new ResponseEntity<Object>(new Response("error",
                ERR_405,
//                ERR_405_MESSAGE, null, null, ex),HttpStatus.METHOD_NOT_ALLOWED);
                ERR_405_MESSAGE, null, null, null),HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler({Exception.class,SQLException.class, QueryParameterException.class,CustomException.class,ApplicationException.class,MethodArgumentNotValidException.class,InvalidFormatException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleAllExceptions(HttpServletRequest request, Exception ex)
    {
        try {


            if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(ex), "Broken pipe")
              ||StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(ex), "An established connection was aborted by the software in your host machine")) {
                String brokenPipeCorrelationId = ErrorCorrelationUtil.newCorrelationId();
                logger.error(ErrorCorrelationUtil.tag(brokenPipeCorrelationId, "exception log excluded"), ex);
                return new ResponseEntity<Object>(new Response("error",
                        ERR_500,
                        ErrorCorrelationUtil.clientMessage(brokenPipeCorrelationId), null, null, null), HttpStatus.INTERNAL_SERVER_ERROR);        //socket is closed, cannot return any response
            }else
                // Handle Bad Request (400) specifically
                if (ex instanceof MethodArgumentNotValidException || ex instanceof HttpMessageNotReadableException) {
                    logger.error("Bad Request - Invalid parameters or malformed request", ex);
//                    return new ResponseEntity<>(new Response("error", "HIS-400", "Bad request: Invalid parameters", null, null, ex), HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<>(new Response("error", "HIS-400", "Bad request: Invalid parameters", null, null, null), HttpStatus.BAD_REQUEST);

                }
            else
                if (ex instanceof InvalidFormatException || ex instanceof InvalidFormatException) {
//                    return new ResponseEntity<>(new Response("error", "HIS-400", "Bad request: Invalid parameters", null, null, ex), HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<>(new Response("error", "HIS-400", "Bad request: Invalid parameters", null, null, null), HttpStatus.BAD_REQUEST);

                }
                else
                if (ex instanceof HttpRequestMethodNotSupportedException || ex instanceof HttpRequestMethodNotSupportedException) {
//                    return new ResponseEntity<Object>(new Response("error",
//                            ERR_405,
//                            ERR_405_MESSAGE, null, null, ex),HttpStatus.METHOD_NOT_ALLOWED);

                    return new ResponseEntity<Object>(new Response("error",
                            ERR_405,
                            ERR_405_MESSAGE, null, null, null),HttpStatus.METHOD_NOT_ALLOWED);
                }
                else
                {
                String correlationId = ErrorCorrelationUtil.newCorrelationId();
                logger.error(ErrorCorrelationUtil.tag(correlationId, "exception log consider"), ex);
                int res = handler.getHandler(request).toString().indexOf("org.springframework.http.ResponseEntity");
                if (request != null && request instanceof ContentCachingRequestWrapper) {
                    try {
                        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
                        backLogService.createErrorLog(wrapper, ex);
                    } catch (Exception e) {
                        logger.error("Failed to persist error log via backLogService; original exception was: {}",
                                ExceptionUtils.getStackTrace(ex), e);
                    }
                }
                if (res > 0)
//                    return new ResponseEntity<Object>(new ErrorResponse("HIS-1001", "Error while processing data", ex),
//                            HttpStatus.INTERNAL_SERVER_ERROR);
                    return new ResponseEntity<Object>(new ErrorResponse("HIS-1001", ErrorCorrelationUtil.clientMessage(correlationId), null),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                else
//                    return new ResponseEntity<Object>(new Response("error",
//                            ERR_500,
//                            ERR_500_MESSAGE, null, null, ex), HttpStatus.INTERNAL_SERVER_ERROR);
                    return new ResponseEntity<Object>(new Response("error",
                            ERR_500,
                            ErrorCorrelationUtil.clientMessage(correlationId), null, null, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            String correlationId = ErrorCorrelationUtil.newCorrelationId();
            logger.error(ErrorCorrelationUtil.tag(correlationId, "Unhandled error in centralized exception handler"), e);
            return new ResponseEntity<Object>(new Response("error",
                    ERR_500,
                    ErrorCorrelationUtil.clientMessage(correlationId), null, null, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
  /*  private ResponseEntity<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        // Log Method Not Allowed exception
        logger.warn("Method Not Allowed: {}", ex.getMessage());
        return new ResponseEntity<Object>(new Response("error",
                ERR_405,
                ERR_405_MESSAGE, null, null, ex),HttpStatus.METHOD_NOT_ALLOWED);
    }
*/
}
