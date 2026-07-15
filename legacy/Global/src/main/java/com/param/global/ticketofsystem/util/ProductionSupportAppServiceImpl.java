package com.param.global.ticketofsystem.util;
import in.param.exception.ApplicationException;
import static com.param.global.common.ICommonConstants.ERROR;
import static com.param.global.common.ICommonConstants.ERROR_CODE;
import static com.param.global.common.ICommonConstants.SUCCESS;
import static com.param.global.common.ICommonConstants.SUCCESS_CODE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;

import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.param.global.common.Response;
import com.param.global.ticketofsystem.dto.TicketSystemMasterDto;

import junit.framework.Assert;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.loader.plan.build.internal.returns.SimpleEntityIdentifierDescriptionImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;

import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class ProductionSupportAppServiceImpl implements IProductionSupportAppService {

	private static final Logger logger = Logger.getLogger(ProductionSupportAppServiceImpl.class);

	private static final String BACKLOG_API_KEY = System.getenv("BACKLOG_API_KEY");

	@Override
	public Response addTicketToProductionSupport(TicketSystemMasterDto ticketSystemMasterDto ) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			JSONObject jsonObject = new JSONObject(ticketSystemMasterDto.getTicketSystemJson());
			String issueDesc = this.getIssueDesc(ticketSystemMasterDto);
			jsonObject.put("summary",issueDesc);
			jsonObject.put("attachmentId",ticketSystemMasterDto.getAttachmentId());
			String apiKey =  "https://clinivan.backlog.com/api/v2/issues?apiKey=" + BACKLOG_API_KEY;
			String json = jsonObject.toString();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,json);
			Request request = new Request.Builder().url(apiKey)
					.method("POST", body).addHeader("Content-Type", "application/json").build();
			okhttp3.Response response = client.newCall(request).execute();
			JSONObject jsonObject1 = new JSONObject(response.body().string());
			String ticketNumber = jsonObject1.getString("issueKey");
			return new Response<>(SUCCESS, SUCCESS_CODE,ticketNumber, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}
	
	@Override
	public Response uploadFile(MultipartFile[] upfiles){
	 
	 try {
		 Integer[] attachmentId = new Integer[upfiles.length];
		 int index = 0;
		 for (MultipartFile multipartFile : upfiles) {
			 OkHttpClient client = new OkHttpClient().newBuilder()
			  .build();
			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
			  .addFormDataPart("file",multipartFile.getOriginalFilename() ,
			    RequestBody.create(MediaType.parse("application/octet-stream"),
			    this.convert(multipartFile)))
			  .build();
			Request request = new Request.Builder()
			  .url("https://clinivan.backlog.com/api/v2/space/attachment?apiKey=" + BACKLOG_API_KEY)
			  .method("POST", body)
			  .build();
			
				okhttp3.Response response = client.newCall(request).execute();
				JSONObject jsonObject1 = new JSONObject(response.body().string());
				Integer ticketNumber = jsonObject1.getInt("id");
				attachmentId[index] = ticketNumber;
				index++;
		 		}
		 		return new Response<>(SUCCESS, SUCCESS_CODE,null, null, attachmentId);
			} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
 }
	
	private  File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
 
	public String  uploadFile (String apiUrl, MultipartFile file, String fileName) throws Exception{
        String boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the necessary headers for a multipart/form-data POST request
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {

            // Add the file part
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileName).append("\"\r\n");
            writer.append("Content-Type: application/octet-stream\r\n\r\n");

            // Read the file and write it to the output stream
            try (InputStream fileInputStream = file.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            writer.append("\r\n");
            writer.append("--").append(boundary).append("--\r\n");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response if needed
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("Response: " + response.toString());
            }
        } finally {
            connection.disconnect();
        }
        
        return "Done";
	   } 
	
	private String getIssueDesc(TicketSystemMasterDto ticketSystemMasterDto ) throws JSONException{
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(ticketSystemMasterDto.getTicketIssueDescription());
			String desc = jsonObject.getString("Issue Description");
			String reportedBy = jsonObject.getString("Reported By");
			String unit = jsonObject.getString("Unit");
			String ticketIssueDescription = unit+ "- " + desc + "." + " Reported By: "+ reportedBy;
			return ticketIssueDescription;
		} catch (JSONException e) {
			 throw new  JSONException (e);
		}
		
	}

}
