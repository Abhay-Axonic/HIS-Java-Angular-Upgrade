package com.param.exception;


import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackLogService {

    @Autowired
    private ExceptionDao exceptionDao;

    @Autowired
    private RestTemplate restTemplate;

    private static String URL = "https://clinivan.backlog.com/api/v2/issues";

    @Async("asyncExecutor")
    public void createErrorLog(ContentCachingRequestWrapper request, Exception ex) throws SQLException, JSONException {
        ErrorLog error = new ErrorLog();
        error.setUrl(request.getRequestURL().toString());
        error.setParameters(new Gson().toJson(request.getParameterMap()));
        error.setMethod(request.getMethod());
        error.setPayLoad(request.getPayLoad());
        Throwable rootCause = ex;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        error.setClassName(rootCause.getStackTrace()[0].getClassName());
        error.setMethodName(rootCause.getStackTrace()[0].getMethodName());
        error.setProjectId("24797");//HIS
        error.setIssueTypeId("103843");
        error.setPriorityId("2");//High
        error.setStartDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        error.setException(ExceptionUtils.getStackTrace(ex));
        this.createBackLogTicket(error);
    }

    public void createBackLogTicket(ErrorLog log) throws JSONException, SQLException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        //headers.set("Authorization","Bearer RndQfZMYvd");
//        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//        map.add("apiKey ", "RndQfZMYvd");
//        map.add("projectId ", log.getProjectId());
//        map.add("summary", log.getSummary());
//        map.add("description ", log.getDescription());
//        map.add("startDate ", log.getStartDate());
//        map.add("issueTypeId ", log.getIssueTypeId());//Bug->103843
//        map.add("priorityId", log.getPriorityId());//2-> High
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity( URL, request , String.class);
//        System.out.println("response "+response.getBody());
//        JSONObject jsonObject = new JSONObject(response.getBody());
//        String issueKey = jsonObject != null ? jsonObject.getString("issueKey"): "";
//        log.setIssueNo(issueKey);
        exceptionDao.addErrorLog(log);

    }





}
