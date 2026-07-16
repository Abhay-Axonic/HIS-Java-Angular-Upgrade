package com.param.global.dao;

import com.param.adt.master.global.model.ProductionReleaseEmailNotificationConfigMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ProductionReleaseEmailNotificationConfigDto;
import com.param.global.razorpay.AxiosConfig;
import com.param.global.utility.UserComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
@Repository
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class ProductionReleaseEmailNotificationConfigDaoImpl extends GenericDao<ProductionReleaseEmailNotificationConfigMapper, Integer> implements IProductionReleaseEmailNotificationConfigDao, ICommonConstants {
        final static Logger logger = Logger.getLogger(ProductionReleaseEmailNotificationConfigDaoImpl.class);

        public ProductionReleaseEmailNotificationConfigDaoImpl() {
            super(ProductionReleaseEmailNotificationConfigMapper.class);
        }



    @Override
    @Transactional

    public Response sendNotificationOnEmailOfReleaseDetails() throws ApplicationException {
        try {
            //get config table list

            String query="select send_to as \"sendTo\",subject as \"subject\" ,message as \"message\"  from public.production_release_email_notification_config " ;


            List<ProductionReleaseEmailNotificationConfigDto> ProductionReleaseEmailNotificationConfigDtoList = (List<ProductionReleaseEmailNotificationConfigDto>) sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setResultTransformer(Transformers.aliasToBean(ProductionReleaseEmailNotificationConfigDto.class)).list();
            // check already send or not
            for (ProductionReleaseEmailNotificationConfigDto dto : ProductionReleaseEmailNotificationConfigDtoList) {
                String instanceName = null;
                String versionNumber = null;
                try {
                    Resource resource = new ClassPathResource("jdbc.properties");
                    InputStreamReader input = new InputStreamReader(resource.getInputStream());
                    Properties properties = new Properties();
                    properties.load(input);
                    instanceName = String.valueOf(properties.getProperty("instanceName"));
                    versionNumber = String.valueOf(properties.getProperty("versionNumber"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                dto.setInstanceName(instanceName);
                dto.setVersionNumber(versionNumber);
                Response res = this.getProductionReleaseEmailNotificationConfig(dto);
                try {
                    if (res.getListObject() != null && res.getListObject().size() > 0) {

                        return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
                    } else {

                        //SAVE MAPPER
                        ProductionReleaseEmailNotificationConfigMapper mapper = new ProductionReleaseEmailNotificationConfigMapper();
                        mapper.setSendTo(dto.getSendTo());
                        mapper.setMessage(dto.getMessage());
                        mapper.setInstanceName(instanceName);
                        mapper.setVersionNumber(versionNumber);
                        mapper.setIsEmailSend('Y');
                        save(mapper);

                        // Create a SimpleDateFormat instance with both date and time
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                        // Get the current date and time
                        Date currentDate = new Date();

                        // Format the current date and time
                        String formattedDateTime = dateFormat.format(currentDate);

                        String message = dto.getMessage();
                        String subject = dto.getSubject();
                        message = message.replaceAll("@instantName", instanceName);
                        message = message.replaceAll("@versionNumber", versionNumber);
                        message = message.replaceAll("@date", formattedDateTime);

                        subject = subject.replaceAll("@instantName", instanceName);
                        subject = subject.replaceAll("@versionNumber", versionNumber);
                        JSONObject json = new JSONObject();
                        try {
                            json.put("to", dto.getSendTo());
                            json.put("from", "info@axonichealth.com");
                            json.put("subject", subject);
                            json.put("data", message);


                            OkHttpClient client = new OkHttpClient();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, json.toString());
                            System.out.println("Json for whatsapp request : " + json.toString());

                            Request request = new Request.Builder()
                                    .url("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail")
                                    .method("POST", body)
                                    .addHeader("Content-Type", "application/json")
                                    .build();

                            okhttp3.Response response = client.newCall(request).execute();
                            String responseBody = response.body().string();
                            return new Response(EMAIL_SENT, null, responseBody, null, null);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return new Response<>(ERROR, ERROR_CODE, null, null, null);
                }
            }

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Response getProductionReleaseEmailNotificationConfig(ProductionReleaseEmailNotificationConfigDto dto) {

       String query="""
                select is_email_send as  "isEmailSend" \
                from public.production_release_email_notification_config_mapper mapper
                where mapper.instance_name =:instanceName \
                and mapper.version_number =:versionNumber\s\
               """;


        List<ProductionReleaseEmailNotificationConfigDto> mapperList = (List<ProductionReleaseEmailNotificationConfigDto>) sessionFactory.getCurrentSession()
                .createSQLQuery(query)
                .setParameter("instanceName", dto.getInstanceName())
                .setParameter("versionNumber", dto.getVersionNumber())
                .setResultTransformer(Transformers.aliasToBean(ProductionReleaseEmailNotificationConfigDto.class)).uniqueResult();

        return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, mapperList, null);
    }
}
