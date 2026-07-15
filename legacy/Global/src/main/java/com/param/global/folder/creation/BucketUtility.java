package com.param.global.folder.creation;

import in.param.exception.ApplicationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import com.param.global.org.dto.OrganizationMasterDto;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.param.global.common.HISFileUploadsPath;
import org.springframework.transaction.annotation.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class BucketUtility implements HISFileUploadsPath{
	final static Logger logger = Logger.getLogger(BucketUtility.class);



    @Autowired
    SessionFactory sessionFactory;
	
	
	/** Integer flag 
	 * 0: organization
	 *  1: unit*/
    @Transactional
	public Boolean create(Integer flag,String folderName)throws Exception
	{
	  
	  try {

          String queryAWSCred = "select access_key as \"awsAccessKey\", secret_key as \"awsSecretKey\" from t_aws_credentials " +
                  " where status = 'A' order by aws_key_id desc limit 1";
          OrganizationMasterDto awsCredDto = (OrganizationMasterDto) sessionFactory.getCurrentSession().createSQLQuery(queryAWSCred)
                  .setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();

          String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
          String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

	    AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
        
        //System.out.println("Start Time" + TimeZoneComponent.getDateByzone(unitId));
        //read json file
        JSONParser parser = new JSONParser();
        Resource resource = new ClassPathResource("folder-structure.json");
        Object obj = parser.parse(new InputStreamReader(resource.getInputStream()));
        JSONObject jsonObject =  (JSONObject) obj;
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        
        ObjectListing objListing=s3Client.listObjects(BUCKET_NAME, folderName);
        
        if(objListing.getObjectSummaries().size()>0) {
        	return false;
        }
        
        if(flag==0 && folderName!=null)
        {
          JSONArray jArray1 = (JSONArray) jsonObject.get("org");
            for(int i =0; i < jArray1.size(); i++){
                String folder =  folderName+(String)jArray1.get(i);
                //System.out.println();
                // create a PutObjectRequest passing the folder name suffixed by /
                PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,folder, emptyContent, metadata);
                s3Client.putObject(putObjectRequest);
            }
            //System.out.println("End Time" + TimeZoneComponent.getDateByzone(unitId));
            //System.out.println("Folder Created");
            return true;
        }
        else if(flag==1 &&folderName!=null)
        {
            JSONArray jArray = (JSONArray) jsonObject.get("unit");
            for(int i =0; i < jArray.size(); i++){
                String folder =  folderName+(String)jArray.get(i);
                //System.out.println("String Folder  :"+folder);
                // create a PutObjectRequest passing the folder name suffixed by /
                PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,folder, emptyContent, metadata);
                s3Client.putObject(putObjectRequest);
            }
            //System.out.println("End Time" + TimeZoneComponent.getDateByzone(unitId));
            //System.out.println("Folder Created");
            return true;
            
        }
        else {
          return false;
        }
      } catch (Exception e) {
      throw new ApplicationException(e.getMessage(), e);
      }
	}

}
