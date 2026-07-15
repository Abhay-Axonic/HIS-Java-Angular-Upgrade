package com.param.global.common;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PatientDialysisRecordDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.otims.dto.IntraOperativeImageDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDocumentDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.opd.encounter.dto.EncounterAssignmenMapperDto;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.opd.encounter.dto.FundSanctionRequestDocumentDto;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;


@Service
public class DocUploadUtility implements HISFileUploadsPath,ICommonConstants{
	
	/*@Autowired
	 * 
	private SessionObject sessionObject;*/
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;

	
	final static Logger logger = Logger.getLogger(DocUploadUtility.class);
	@Autowired
	SessionFactory sessionFactory;
	// India

	public OrganizationMasterDto getAWSCreds(){
		try {
			String queryAWSCred = "select access_key as \"awsAccessKey\", secret_key as \"awsSecretKey\" from t_aws_credentials " +
					" where status = 'A' order by aws_key_id desc limit 1";
			OrganizationMasterDto awsCredDto = (OrganizationMasterDto) sessionFactory.getCurrentSession().createSQLQuery(queryAWSCred)
					.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();
			return awsCredDto;
		}
	catch (Exception e){
		logger.error("getAWSCreds failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Transactional
	public String uploadOrganiazationLogo(MultipartFile multipartFile,OrganizationMasterDto organizationMasterDto) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
		try{

			String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
					"where organization_id = :organizationId";
			Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			query.setParameter("organizationId", organizationMasterDto.getOrganizationId());
			Integer bucketType = (Integer) query.uniqueResult();
			if(bucketType == null) {
				bucketType=1;
			}

			OrganizationMasterDto awsCredDto = getAWSCreds();
			String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
			String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";


		   System.out.println("My bucket Type issssssss:"+ bucketType );
		   System.out.println("UPLOAD CODE Starts here........");			
			if(multipartFile != null && multipartFile.getSize() > 0){
				 if(bucketType == 1) {
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				//write a file on s3 server
				String bucket = BUCKET_NAME;
				String keyName = organizationMasterDto.getOrganizationId() + "_" + multipartFile.getOriginalFilename();
				String orgnization = organizationMasterDto.getOrganizationFolder();
				s3Client.putObject(bucket + "/" +orgnization+ ORGANIZATION_LOGO_BUCKET, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + "/" +orgnization + ORGANIZATION_LOGO_BUCKET, keyName, CannedAccessControlList.PublicRead);
				return AWS_BUCKET_COMMON_PATH +bucket+ "/" +orgnization + ORGANIZATION_LOGO_BUCKET +"/" + keyName;
				 }else {
					 String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
							 "where organization_id = :organizationId";
					 Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
					 query1.setParameter("organizationId", organizationMasterDto.getOrganizationId());
					 String localBucketJson = (String) query1.uniqueResult();


					 Gson gson = new Gson();
					   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
	  
				   MinioClient minioClient = new MinioClient.Builder()
			                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
			                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
			                .build();
				   			   			   
				   System.out.println("MINIO Connectionnn is Createdddddd........");
		            Map<String, String> headers = new HashMap<>();
		            headers.put("X-Amz-Meta-Description", "This is a test file.");

		            String objectName = multipartFile.getOriginalFilename();
		            String contentType =multipartFile.getContentType();

		            // Get InputStream from MultipartFile
		            InputStream inputStream = multipartFile.getInputStream();
	 
		            minioClient.putObject(PutObjectArgs.builder()
		                    .bucket(encounterDocDetailsDtoNew.getBucketName())
		                    .object(objectName)
		                    .contentType(contentType)
		                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
		                    .build());
		                           
		            System.out.println("Multipart file uploaded successfully.");
		            String url = minioClient.getPresignedObjectUrl(
		                    GetPresignedObjectUrlArgs.builder()
		                            .method(Method.GET)
		                            .bucket(encounterDocDetailsDtoNew.getBucketName())
		                            .object(objectName)
		                            .build());
		            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
		            return url;
				   }
			}
	}catch(AmazonServiceException ae){
		logger.error("uploadOrganiazationLogo failed", ae);
		throw new ServiceException(ae.getMessage());
	}catch(IOException ie){
		logger.error("uploadOrganiazationLogo failed", ie);
		throw new ServiceException(ie.getMessage());
	}
		return null;
	}
	
	@Transactional
	public String uploadOrganizationUnitLogo(MultipartFile multipartFile,UnitMasterDto unitMasterDto) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
		try {


			String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
					"where unit_id = :unitId and organization_id = :organizationId";
			Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			query.setParameter("unitId", unitMasterDto.getUnitId());
			query.setParameter("organizationId", unitMasterDto.getOrganizationId());
			Integer bucketType = (Integer) query.uniqueResult();
			if(bucketType == null) {
				bucketType=1;
			}

			OrganizationMasterDto awsCredDto = getAWSCreds();
			String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
			String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			System.out.println("My bucket Type issssssss:"+ bucketType );
			System.out.println("UPLOAD CODE Starts here........");

			if (multipartFile != null && multipartFile.getSize() > 0) {
				if (bucketType == 1) {
					AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentType(multipartFile.getContentType());
					// write a file on s3 server
					String bucket = BUCKET_NAME;
					String organizationUnit = unitMasterDto.getUnitFolder();
					String keyName = unitMasterDto.getUnitId() + "_" + multipartFile.getOriginalFilename();
					s3Client.putObject(bucket + "/" + organizationUnit + UNIT_LOGO_BUCKET, keyName,
							multipartFile.getInputStream(), metadata);
					// Setting Public Permissions to file
					s3Client.setObjectAcl(bucket + "/" + organizationUnit + UNIT_LOGO_BUCKET, keyName,
							CannedAccessControlList.PublicRead);
					return AWS_BUCKET_COMMON_PATH + bucket + "/" + organizationUnit + UNIT_LOGO_BUCKET + "/" + keyName;
				} else {

					String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
							"where unit_id = :unitId and organization_id = :organizationId";
					Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
					query1.setParameter("unitId", unitMasterDto.getUnitId());
					query1.setParameter("organizationId", unitMasterDto.getOrganizationId());
					String localBucketJson = (String) query1.uniqueResult();
					Gson gson = new Gson();
					EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson,
							EncounterDocDetailsDto.class);

					MinioClient minioClient = new MinioClient.Builder()
							.endpoint(encounterDocDetailsDtoNew.getEndpoint())
							.credentials(encounterDocDetailsDtoNew.getAccessKey(),
									encounterDocDetailsDtoNew.getSecretKey())
							.build();

					System.out.println("MINIO Connectionnn is Createdddddd........");
					Map<String, String> headers = new HashMap<>();
					headers.put("X-Amz-Meta-Description", "This is a test file.");

					String objectName = multipartFile.getOriginalFilename();
					String contentType = multipartFile.getContentType();

					// Get InputStream from MultipartFile
					InputStream inputStream = multipartFile.getInputStream();

					minioClient.putObject(PutObjectArgs.builder().bucket(encounterDocDetailsDtoNew.getBucketName())
							.object(objectName).contentType(contentType).stream(inputStream, -1, 10485760) // -1 for  unknown object size, 10MB buffer size
							.build());

					System.out.println("Multipart file uploaded successfully.");
					String url = minioClient
							.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET)
									.bucket(encounterDocDetailsDtoNew.getBucketName()).object(objectName).build());
					System.out.println("MY MINIO URL ISSSSSS.....: " + url);
					return url;
				}
			}
	}catch(AmazonServiceException ae){
		logger.error("uploadOrganizationUnitLogo failed", ae);
		throw new ServiceException(ae.getMessage());
	}catch(IOException ie){
		logger.error("uploadOrganizationUnitLogo failed", ie);
		throw new ServiceException(ie.getMessage());
	}
		return null;
	}
	
	   @Transactional
	   public String uploadPatientImage(MultipartFile multipartFile,OrganizationMasterDto organizationMasterDto,Integer unitId,Integer patientId){
	        try{

				String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						"where unit_id = :unitId and organization_id = :organizationId";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				query.setParameter("unitId", unitId);
				query.setParameter("organizationId", organizationMasterDto.getOrganizationId());
				Integer bucketType = (Integer) query.uniqueResult();
				if(bucketType == null) {
					bucketType=1;
				}

				OrganizationMasterDto awsCredDto = getAWSCreds();
				String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
				String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOAD CODE Starts here........");
	            if(multipartFile != null && multipartFile.getSize() > 0){
	            	if(bucketType == 1) {
	                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	                ObjectMetadata metadata = new ObjectMetadata();
	                metadata.setContentType(multipartFile.getContentType());
	                //write a file on s3 server
	                String bucket = BUCKET_NAME;
	                String organization =  organizationMasterDto.getOrganizationFolder();
	                String keyName = organizationMasterDto.getOrganizationId()+ "_" +unitId+"_"+patientId+ "_" + multipartFile.getOriginalFilename();
	                s3Client.putObject(bucket + "/" + organization+ADMINISTRATIVE_PHOTO_BUCKET, keyName, multipartFile.getInputStream(), metadata);
	                //Setting Public Permissions to file
	                s3Client.setObjectAcl(bucket + "/" +organization+ADMINISTRATIVE_PHOTO_BUCKET, keyName, CannedAccessControlList.PublicRead);
	                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + ADMINISTRATIVE_PHOTO_BUCKET+"/" + keyName;
	            	}else {

						String localBucketJson = (String) sessionFactory.getCurrentSession()
								.createSQLQuery("select local_storage_bucket_json from t_unit_application_billing_configuration where unit_id ="+unitId+" and organization_id ="+organizationMasterDto.getOrganizationId())
								.uniqueResult();

						   Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);

					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint())
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();

					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();

			            try {
							minioClient.putObject(PutObjectArgs.builder()
							        .bucket(encounterDocDetailsDtoNew.getBucketName())
							        .object(objectName)
							        .contentType(contentType)
							        .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
							        .build());
			} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
					| InternalException | InvalidResponseException | NoSuchAlgorithmException
					| ServerException | XmlParserException | IllegalArgumentException e) {
				logger.error("uploadPatientImage failed", e);
				throw new ServiceException(e.getMessage());
			}

			            System.out.println("Multipart file uploaded successfully.");
			            String url;
						try {
							url = minioClient.getPresignedObjectUrl(
							        GetPresignedObjectUrlArgs.builder()
							                .method(Method.GET)
							                .bucket(encounterDocDetailsDtoNew.getBucketName())
							                .object(objectName)
							                .build());
							System.out.println("MY MINIO URL ISSSSSS.....: " + url);
				            return url;
			} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
					| InternalException | InvalidResponseException | NoSuchAlgorithmException
					| XmlParserException | ServerException | IllegalArgumentException e) {
				logger.error("uploadPatientImage failed", e);
				throw new ServiceException(e.getMessage());
			}

					   }
	            }
	    }catch(AmazonServiceException ae){
		logger.error("uploadPatientImage failed", ae);
		throw new ServiceException(ae.getMessage());
	    }catch(IOException ie){
		logger.error("uploadPatientImage failed", ie);
		throw new ServiceException(ie.getMessage());
	    }
	        return null;
	    }
	   
	   @Transactional
	   public String uploadScreenImage(MultipartFile multipartFile,OrganizationMasterDto organizationMasterDto, Integer organisationId, Integer unitId, Integer bucketType){
		   try{

	        	if(bucketType == null) {
					bucketType=1;
				}

			   OrganizationMasterDto awsCredDto = getAWSCreds();
			   String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
			   String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOADING CODE........");
	            if(multipartFile != null && multipartFile.getSize() > 0){
	            	if(bucketType == 1) {
	                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	                ObjectMetadata metadata = new ObjectMetadata();
	                metadata.setContentType(multipartFile.getContentType());
	                //write a file on s3 server
	                String bucket = BUCKET_NAME; 
	                String organization =  organizationMasterDto.getOrganizationFolder();
	                String keyName = organisationId+ "_" +unitId+"_"+ multipartFile.getOriginalFilename();
	                s3Client.putObject(bucket + "/" + organization+ADMINISTRATIVE_PHOTO_BUCKET, keyName, multipartFile.getInputStream(), metadata);
	                //Setting Public Permissions to file
	                s3Client.setObjectAcl(bucket + "/" +organization+ADMINISTRATIVE_PHOTO_BUCKET, keyName, CannedAccessControlList.PublicRead);
	                System.out.println(AWS_BUCKET_COMMON_PATH+ "/" +organization + ADMINISTRATIVE_PHOTO_BUCKET+"/" + keyName);
	                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + ADMINISTRATIVE_PHOTO_BUCKET+"/" + keyName;
	            	}else {
						   
						String localBucketJson = (String) sessionFactory.getCurrentSession()
								.createSQLQuery("select local_storage_bucket_json from t_unit_application_billing_configuration where unit_id ="+unitId+" and organization_id ="+organizationMasterDto.getOrganizationId())
								.uniqueResult();
			   				   
						   Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            try {
							minioClient.putObject(PutObjectArgs.builder()
							        .bucket(encounterDocDetailsDtoNew.getBucketName())
							        .object(objectName)
							        .contentType(contentType)
							        .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
							        .build());
				} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
						| InternalException | InvalidResponseException | NoSuchAlgorithmException
						| ServerException | XmlParserException | IllegalArgumentException e) {
					logger.error("uploadScreenImage failed", e);
					throw new ServiceException(e.getMessage());
				}
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url;
						try {
							url = minioClient.getPresignedObjectUrl(
							        GetPresignedObjectUrlArgs.builder()
							                .method(Method.GET)
							                .bucket(encounterDocDetailsDtoNew.getBucketName())
							                .object(objectName)
							                .build());
							System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
				} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
						| InternalException | InvalidResponseException | NoSuchAlgorithmException
						| XmlParserException | ServerException | IllegalArgumentException e) {
					logger.error("uploadScreenImage failed", e);
					throw new ServiceException(e.getMessage());
				}
			            
					   }
	            }
	        }catch(AmazonServiceException ae){
		logger.error("uploadScreenImage failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(IOException ie){
		logger.error("uploadScreenImage failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
	        return null;
	   }
	   
	   @Transactional
	   public String uploadDonorImage(MultipartFile multipartFile,OrganizationMasterDto organizationMasterDto,Integer unitId,Integer donorId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
	        try{
				String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						"where unit_id = :unitId and organization_id = :organizationId";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				query.setParameter("unitId", unitId);
				query.setParameter("organizationId", organizationMasterDto.getOrganizationId());
				Integer bucketType = (Integer) query.uniqueResult();
				if(bucketType == null) {
					bucketType=1;
				}
	        	
			    OrganizationMasterDto awsCredDto = getAWSCreds();
				String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
				String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOAD CODE Starts here........");	        	
	            if(multipartFile != null && multipartFile.getSize() > 0){
	            	if(bucketType == 1) {
	                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	                ObjectMetadata metadata = new ObjectMetadata();
	                metadata.setContentType(multipartFile.getContentType());
	                //write a file on s3 server
	                String bucket = BUCKET_NAME; 
	                String organization =  organizationMasterDto.getOrganizationFolder();
	                String keyName = organizationMasterDto.getOrganizationId()+ "_" +unitId+"_"+donorId+ "_" + multipartFile.getOriginalFilename();
	                s3Client.putObject(bucket + "/" + organization+ADMINISTRATIVE_PHOTO_DONER_BUCKET, keyName, multipartFile.getInputStream(), metadata);
	                //Setting Public Permissions to file
	                s3Client.setObjectAcl(bucket + "/" +organization+ADMINISTRATIVE_PHOTO_DONER_BUCKET, keyName, CannedAccessControlList.PublicRead);
	                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + ADMINISTRATIVE_PHOTO_DONER_BUCKET+"/" + keyName;
	            	}else {
						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						query1.setParameter("unitId", unitId);
						query1.setParameter("organizationId", organizationMasterDto.getOrganizationId());
						String localBucketJson = (String) query1.uniqueResult();


						Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
	            }
	        }catch(AmazonServiceException ae){
		logger.error("uploadDonorImage failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(IOException ie){
		logger.error("uploadDonorImage failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
	        return null;
	    }
	   
	   @Transactional
	   public String uploadDoctorImage(MultipartFile multipartFile,UnitMasterDto unitMasterDto,Integer doctorId){
		   try{
			   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
					   "where unit_id = :unitId and organization_id = :organizationId";
			   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			   query.setParameter("unitId", unitMasterDto.getUnitId());
			   query.setParameter("organizationId", unitMasterDto.getOrganizationId());
			   Integer bucketType = (Integer) query.uniqueResult();

			   if(bucketType == null) {
					bucketType=1;
				}						
			   String url;
			    OrganizationMasterDto awsCredDto = getAWSCreds();
				String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
				String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOAD CODE Starts here........");
				if(multipartFile != null && multipartFile.getSize() > 0){
					if(bucketType == 1) {
					AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentType(multipartFile.getContentType());
					//write a file on s3 server
					String bucket = BUCKET_NAME; 
					String organizationUnit =  unitMasterDto.getUnitFolder();
					String keyName = unitMasterDto.getOrganizationId()+"_"+unitMasterDto.getUnitId()+"_"+doctorId + "_" + multipartFile.getOriginalFilename();
					s3Client.putObject(bucket + "/" + organizationUnit+CLINICAL_STAFF_PHOTO_BUCKET, keyName, multipartFile.getInputStream(), metadata);
					//Setting Public Permissions to file
					s3Client.setObjectAcl(bucket + "/" +organizationUnit+CLINICAL_STAFF_PHOTO_BUCKET, keyName, CannedAccessControlList.PublicRead);
					return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organizationUnit + CLINICAL_STAFF_PHOTO_BUCKET+"/" + keyName;
					}else {
						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query quer1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						quer1.setParameter("unitId", unitMasterDto.getUnitId());
						quer1.setParameter("organizationId", unitMasterDto.getOrganizationId());
						String localBucketJson = (String) quer1.uniqueResult();
						Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            try {
							minioClient.putObject(PutObjectArgs.builder()
							        .bucket(encounterDocDetailsDtoNew.getBucketName())
							        .object(objectName)
							        .contentType(contentType)
							        .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
							        .build());
			} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
					| InternalException | InvalidResponseException | NoSuchAlgorithmException
					| ServerException | XmlParserException | IllegalArgumentException e) {
				logger.error("uploadDoctorImage failed", e);
				throw new ServiceException(e.getMessage());
			}
			                           
			            System.out.println("Multipart file uploaded successfully.");
			           
						try {
							
							url = minioClient.getPresignedObjectUrl(
							        GetPresignedObjectUrlArgs.builder()
							                .method(Method.GET)
							                .bucket(encounterDocDetailsDtoNew.getBucketName())
							                .object(objectName)
							                .build());
							System.out.println("MY MINIO URL ISSSSSS.....: " + url);	
							return url;
			} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
					| InternalException | InvalidResponseException | NoSuchAlgorithmException
					| XmlParserException | ServerException | IllegalArgumentException e) {
				logger.error("uploadDoctorImage failed", e);
				throw new ServiceException(e.getMessage());
			}
			                        
			            
					   }
					
				}
		}catch(AmazonServiceException ae){
			logger.error("uploadDoctorImage failed", ae);
			throw new ServiceException(ae.getMessage());
		}catch(IOException ie){
			logger.error("uploadDoctorImage failed", ie);
			throw new ServiceException(ie.getMessage());
		}
			return null;
	    }
	   
	   @Transactional
	   public String uploadEmployeeImage(MultipartFile multipartFile,UnitMasterDto unitMasterDto,Integer unitId,Integer employeeId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
		   try{

			   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
					   "where unit_id = :unitId and organization_id = :organizationId";
			   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			   query.setParameter("unitId", unitMasterDto.getUnitId());
			   query.setParameter("organizationId", unitMasterDto.getOrganizationId());
			   Integer bucketType = (Integer) query.uniqueResult();
			   if(bucketType == null) {
					bucketType=1;
				}
			   if(multipartFile != null && multipartFile.getSize() > 0){
					if(bucketType == 1) {

						OrganizationMasterDto awsCredDto = getAWSCreds();
						String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
						String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

						System.out.println("My bucket Type issssssss:"+ bucketType );

					AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentType(multipartFile.getContentType());
					//write a file on s3 server
					String bucket = BUCKET_NAME; 
					String organizationUnit =  unitMasterDto.getUnitFolder();
					String keyName = unitMasterDto.getOrganizationId()+"_"+unitMasterDto.getUnitId()+"_"+employeeId + "_" + multipartFile.getOriginalFilename();
					s3Client.putObject(bucket + "/" + organizationUnit+CLINICAL_STAFF_PHOTO_BUCKET, keyName, multipartFile.getInputStream(), metadata);
					//Setting Public Permissions to file
					s3Client.setObjectAcl(bucket + "/" +organizationUnit+CLINICAL_STAFF_PHOTO_BUCKET, keyName, CannedAccessControlList.PublicRead);
					return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organizationUnit + CLINICAL_STAFF_PHOTO_BUCKET+"/" + keyName;
					}else {
						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						query1.setParameter("unitId", unitMasterDto.getUnitId());
						query1.setParameter("organizationId", unitMasterDto.getOrganizationId());
						String localBucketJson = (String) query1.uniqueResult();
						Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
				}
			}catch(AmazonServiceException ae){
				logger.error("uploadEmployeeImage failed", ae);
				throw new ServiceException(ae.getMessage());
			}catch(IOException ie){
				logger.error("uploadEmployeeImage failed", ie);
				throw new ServiceException(ie.getMessage());
			}
			return null;
	    }
	   
	   @Transactional
	   public String uploadEncounterDoc(MultipartFile multipartFile,String orgFolderPath,EncounterDocDetailsDto encounterDocDetailsDto){
		   try{

			   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
					   "where unit_id = :unitId and organization_id = :organizationId";
			   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			   query.setParameter("unitId", encounterDocDetailsDto.getUnitId());
			   query.setParameter("organizationId", encounterDocDetailsDto.getOrganisationId());
			   Integer bucketType = (Integer) query.uniqueResult();

				
			   if(bucketType == null) {
					bucketType=1;
				}
			   OrganizationMasterDto awsCredDto = getAWSCreds();
			   String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
			   String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOAD CODE Starts here........");
			   if(bucketType == 1) {
					AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentType(multipartFile.getContentType());
					//write a file on s3 server
					String bucket = BUCKET_NAME; 
					String organization =  orgFolderPath;
					String keyName = "";
					if(encounterDocDetailsDto.getEncounterId() != null) {
						keyName = encounterDocDetailsDto.getOrganisationId()+"_"+encounterDocDetailsDto.getUnitId()+"_"+encounterDocDetailsDto.getEncounterId() + "_" + (multipartFile.getOriginalFilename()!=null?multipartFile.getOriginalFilename():encounterDocDetailsDto.getFileName());
					} else {
						keyName = encounterDocDetailsDto.getOrganisationId()+"_"+encounterDocDetailsDto.getUnitId()+ "_" + (multipartFile.getOriginalFilename()!=null?multipartFile.getOriginalFilename():encounterDocDetailsDto.getFileName());
					}
					s3Client.putObject(bucket + "/" + organization+OP_COVERSHEET_BUCKET, keyName, multipartFile.getInputStream(), metadata);
					//Setting Public Permissions to file
					s3Client.setObjectAcl(bucket + "/" +organization+OP_COVERSHEET_BUCKET, keyName, CannedAccessControlList.PublicRead);
					return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + OP_COVERSHEET_BUCKET+"/" + keyName;
			   }else {

				   String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
						   "where unit_id = :unitId and organization_id = :organizationId";
				   Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
				   query1.setParameter("unitId", encounterDocDetailsDto.getUnitId());
				   query1.setParameter("organizationId", encounterDocDetailsDto.getOrganisationId());
				   String localBucketJson = (String) query1.uniqueResult();
				   Gson gson = new Gson();
				   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);

			   MinioClient minioClient = new MinioClient.Builder()
		                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
		                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
		                .build();
			   			   			   
			   System.out.println("MINIO Connectionnn is Createdddddd........");
	            Map<String, String> headers = new HashMap<>();
	            headers.put("X-Amz-Meta-Description", "This is a test file.");

	            String objectName = multipartFile.getOriginalFilename();
	            String contentType =multipartFile.getContentType();

	            // Get InputStream from MultipartFile
	            InputStream inputStream = multipartFile.getInputStream();

	            minioClient.putObject(PutObjectArgs.builder()
	                    .bucket(encounterDocDetailsDtoNew.getBucketName())
	                    .object(objectName)
	                    .contentType(contentType)
	                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
	                    .build());
	                           
	            System.out.println("Multipart file uploaded successfully.");
	            String url = minioClient.getPresignedObjectUrl(
	                    GetPresignedObjectUrlArgs.builder()
	                            .method(Method.GET)
	                            .bucket(encounterDocDetailsDtoNew.getBucketName())
	                            .object(objectName)
	                            .build());
	            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
	            return url;
			   }
			//	}
			}catch(AmazonServiceException ae){
				logger.error("uploadEncounterDoc failed", ae);
				throw new ServiceException(ae.getMessage());
			}catch (Exception e) {
				logger.error("uploadEncounterDoc failed", e);
				throw new ServiceException(e.getMessage());
			}
	    }
	   
	   
	   /*
	    *  upload Insurance Authorizaton Document
	    */ 
	   @Transactional
	   public String uploadInsuranceAuthorizationsDoc(MultipartFile multipartFile, OrganizationMasterDto organizationMasterDto,EncounterDocDetailsDto reqDto) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
		   try{


			   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
					   "where unit_id = :unitId and organization_id = :organizationId";
			   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			   query.setParameter("unitId", reqDto.getUnitId());
			   query.setParameter("organizationId", reqDto.getOrganisationId());
			   Integer bucketType = (Integer) query.uniqueResult();

			   if(bucketType == null) {
					bucketType=1;
				}

			   OrganizationMasterDto awsCredDto = getAWSCreds();
			   String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
			   String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOAD CODE Starts here........");
				if(multipartFile != null && multipartFile.getSize() > 0){
					if(bucketType == 1) {
					AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentType(multipartFile.getContentType());
					//write a file on s3 server
					String bucket = BUCKET_NAME; 
					String keyName = reqDto.getOrganisationId()+"_"+reqDto.getPreauthRequestId()+"_"+reqDto.getPatientId() + "_" + multipartFile.getOriginalFilename();
					s3Client.putObject(bucket + "/" + AUTHORIZATIONS_BUCKET, keyName, multipartFile.getInputStream(), metadata);
					//Setting Public Permissions to file
					s3Client.setObjectAcl(bucket + "/"+ AUTHORIZATIONS_BUCKET, keyName, CannedAccessControlList.PublicRead);
					return AWS_BUCKET_COMMON_PATH + bucket+ "/" + AUTHORIZATIONS_BUCKET+"/" + keyName;
					}else {
						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						query1.setParameter("unitId", reqDto.getUnitId());
						query1.setParameter("organizationId", reqDto.getOrganisationId());
						String localBucketJson = (String) query1.uniqueResult();
						Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
				}
			}catch(AmazonServiceException ae){
				logger.error("uploadInsuranceAuthorizationsDoc failed", ae);
				throw new ServiceException(ae.getMessage());
			}catch(IOException ie){
				logger.error("uploadInsuranceAuthorizationsDoc failed", ie);
				throw new ServiceException(ie.getMessage());
			}
			return null;
	    }
	   
	   @Transactional
	   public String uploadCorporateDoc(MultipartFile multipartFile, String orgFolderPath,EncounterDocDetailsDto reqDto){
		   try{


			   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
					   "where unit_id = :unitId and organization_id = :organizationId";
			   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			   query.setParameter("unitId", reqDto.getUnitId());
			   query.setParameter("organizationId", reqDto.getOrganisationId());
			   Integer bucketType = (Integer) query.uniqueResult();

			   if(bucketType == null) {
					bucketType=1;
				}
			   
				if(multipartFile != null && multipartFile.getSize() > 0){
					if(bucketType == 1) {

						OrganizationMasterDto awsCredDto = getAWSCreds();
						String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
						String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

						System.out.println("My bucket Type issssssss:"+ bucketType );

					AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentType(multipartFile.getContentType());
					//write a file on s3 server
					String bucket = BUCKET_NAME; 
					String organization =  orgFolderPath;
					String keyName = reqDto.getOrganisationId()+"_"+reqDto.getUnitId()+"_"+reqDto.getEncounterId() + "_" + multipartFile.getOriginalFilename();
					s3Client.putObject(bucket + "/" + organization+CORPORATE_CONTRACT_DOCUMENT_BUCKET, keyName, multipartFile.getInputStream(), metadata);
					//Setting Public Permissions to file
					s3Client.setObjectAcl(bucket + "/" +organization+CORPORATE_CONTRACT_DOCUMENT_BUCKET, keyName, CannedAccessControlList.PublicRead);
					return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + CORPORATE_CONTRACT_DOCUMENT_BUCKET+"/" + keyName;
					}else {

						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						query1.setParameter("unitId", reqDto.getUnitId());
						query1.setParameter("organizationId", reqDto.getOrganisationId());
						String localBucketJson = (String) query1.uniqueResult();
						   Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
				}
			}catch(AmazonServiceException ae){
				logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadCorporateDoc failed", ie);
				throw new ServiceException(ie.getMessage());
			}
		   catch (Exception e) {
			   logger.error("uploadCorporateDoc failed", e);
			   throw new ServiceException(e.getMessage());
		   }
			return null;
	    }
	   
	   @Transactional
	   public String uploadCompanyImage(MultipartFile multipartFile,OrganizationMasterDto organizationMasterDto,Integer unitId,Integer companyId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
	        try{
				String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						"where unit_id = :unitId and organization_id = :organizationId";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				query.setParameter("unitId", unitId);
				query.setParameter("organizationId", organizationMasterDto.getOrganizationId());
				Integer bucketType = (Integer) query.uniqueResult();
						   
	        	if(bucketType == null) {
					bucketType=1;
				}
	        	
				OrganizationMasterDto awsCredDto = getAWSCreds();
				String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
				String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOAD CODE Starts here........");
	        	
	            if(multipartFile != null && multipartFile.getSize() > 0){
	            	if(bucketType == 1) {
	                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	                ObjectMetadata metadata = new ObjectMetadata();
	                metadata.setContentType(multipartFile.getContentType());
	                //write a file on s3 server
	                String bucket = BUCKET_NAME; 
	                String organization =  organizationMasterDto.getOrganizationFolder();
	                String keyName = organizationMasterDto.getOrganizationId()+ "_" +unitId+"_"+companyId+ "_" + multipartFile.getOriginalFilename();
	                s3Client.putObject(bucket + "/" + organization+CORPORATE_CONTRACT_DOCUMENT_BUCKET, keyName, multipartFile.getInputStream(), metadata);
	                //Setting Public Permissions to file
	                s3Client.setObjectAcl(bucket + "/" +organization+CORPORATE_CONTRACT_DOCUMENT_BUCKET, keyName, CannedAccessControlList.PublicRead);
	                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + CORPORATE_CONTRACT_DOCUMENT_BUCKET+"/" + keyName;
	            	}else {
						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						query1.setParameter("unitId", unitId);
						query1.setParameter("organizationId", organizationMasterDto.getOrganizationId());
						String localBucketJson = (String) query1.uniqueResult();
						Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
	            }
	        }catch(AmazonServiceException ae){
		logger.error("uploadCompanyImage failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(IOException ie){
		logger.error("uploadCompanyImage failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
	        return null;
	    }
	   
	   @Transactional
	   public String uploadInsuranceImage(MultipartFile multipartFile,OrganizationMasterDto organizationMasterDto,Integer unitId,Integer companyId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
	        try{

				String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						"where unit_id = :unitId and organization_id = :organizationId";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				query.setParameter("unitId", unitId);
				query.setParameter("organizationId", organizationMasterDto.getOrganizationId());
				Integer bucketType = (Integer) query.uniqueResult();
						   
	        	if(bucketType == null) {
					bucketType=1;
				}
				OrganizationMasterDto awsCredDto = getAWSCreds();
				String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
				String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
			   System.out.println("UPLOAD CODE Starts here........");	        	
	            if(multipartFile != null && multipartFile.getSize() > 0){
	            	if(bucketType == 1) {
	                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	                ObjectMetadata metadata = new ObjectMetadata();
	                metadata.setContentType(multipartFile.getContentType());
	                //write a file on s3 server
	                String bucket = BUCKET_NAME; 
	                String organization =  organizationMasterDto.getOrganizationFolder();
	                String keyName = organizationMasterDto.getOrganizationId()+ "_" +unitId+"_"+companyId+ "_" + multipartFile.getOriginalFilename();
	                s3Client.putObject(bucket + "/" + organization+INSURANCE_CONTRACT_DOCUMENT_BUCKET, keyName, multipartFile.getInputStream(), metadata);
	                //Setting Public Permissions to file
	                s3Client.setObjectAcl(bucket + "/" +organization+INSURANCE_CONTRACT_DOCUMENT_BUCKET, keyName, CannedAccessControlList.PublicRead);
	                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + INSURANCE_CONTRACT_DOCUMENT_BUCKET+"/" + keyName;
	            }else {
						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						query1.setParameter("unitId", unitId);
						query1.setParameter("organizationId", organizationMasterDto.getOrganizationId());
						String localBucketJson = (String) query1.uniqueResult();
						Gson gson = new Gson();
					   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
	  
				   MinioClient minioClient = new MinioClient.Builder()
			                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
			                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
			                .build();
				   			   			   
				   System.out.println("MINIO Connectionnn is Createdddddd........");
		            Map<String, String> headers = new HashMap<>();
		            headers.put("X-Amz-Meta-Description", "This is a test file.");

		            String objectName = multipartFile.getOriginalFilename();
		            String contentType =multipartFile.getContentType();

		            // Get InputStream from MultipartFile
		            InputStream inputStream = multipartFile.getInputStream();
	 
		            minioClient.putObject(PutObjectArgs.builder()
		                    .bucket(encounterDocDetailsDtoNew.getBucketName())
		                    .object(objectName)
		                    .contentType(contentType)
		                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
		                    .build());
		                           
		            System.out.println("Multipart file uploaded successfully.");
		            String url = minioClient.getPresignedObjectUrl(
		                    GetPresignedObjectUrlArgs.builder()
		                            .method(Method.GET)
		                            .bucket(encounterDocDetailsDtoNew.getBucketName())
		                            .object(objectName)
		                            .build());
		            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
		            return url;
				   }
	                
	            }
	        }catch(AmazonServiceException ae){
		logger.error("uploadInsuranceImage failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(IOException ie){
		logger.error("uploadInsuranceImage failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
	        return null;
	    }
	/*public String uploadDoctorRegistrationDocuments(MultipartFile multipartFile,DoctorRegistarionDto doctorRegistarionDto){
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				String keyName = doctorRegistarionDto.getDoctorNumber() + "_" + multipartFile.getOriginalFilename();
				s3Client.putObject(bucket + DOCTOR_REG_DOC_BUCKET, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + DOCTOR_REG_DOC_BUCKET, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket +  DOCTOR_REG_DOC_BUCKET + keyName;
			}
		}catch(AmazonServiceException ae){
			//System.out.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadDoctorCoverSheet(MultipartFile multipartFile,AppointmentDoctorCoversheetMapperDto coverSheetDto){
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				String keyName = coverSheetDto.getAppointmentId() + "_" + coverSheetDto.getDoctorId() + "_" + multipartFile.getOriginalFilename();
				s3Client.putObject(bucket + APPOINTMENT_COVER_SHEET_BUCKET, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + APPOINTMENT_COVER_SHEET_BUCKET, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket +  APPOINTMENT_COVER_SHEET_BUCKET + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			//System.out.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadDoctorCoverSheet(InputStream inputStream,AppointmentListDto appointmentListDto){
		try{
			if(inputStream != null && inputStream.available() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType("application/pdf");
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				String keyName = appointmentListDto.getAppointmentId() + "_" + appointmentListDto.getDoctorId() + "_cover_sheet.pdf";
				s3Client.putObject(bucket + APPOINTMENT_COVER_SHEET_BUCKET, keyName, inputStream, metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + APPOINTMENT_COVER_SHEET_BUCKET, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket +  APPOINTMENT_COVER_SHEET_BUCKET + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			//System.out.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadDoctorDictation(MultipartFile multipartFile, DoctorCoverSheetUKDto coverSheetDto){
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				String keyName = TimeZoneComponent.getDateByzone(unitId).getTime() + "_" + coverSheetDto.getPatientId() + "_" + coverSheetDto.getDoctorId();
				s3Client.putObject(bucket + COVERSHEET_UK_DICTATION_BUCKET, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + COVERSHEET_UK_DICTATION_BUCKET, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket +  COVERSHEET_UK_DICTATION_BUCKET + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			//System.out.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadIdentificationDocument(MultipartFile multipartFile , PatientIdentificationDetailsDto patientIdentificationDetailsDto){
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				String keyName = "IDF" + "_" + patientIdentificationDetailsDto.getPatientId() + "_" + patientIdentificationDetailsDto.getIdentificationTypeId();
				String bucket = sessionObject.getBucketName();
				s3Client.putObject(bucket + PATIENT_IDENTIFICATION_DOC, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + PATIENT_IDENTIFICATION_DOC, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + PATIENT_IDENTIFICATION_DOC + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			System.err.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadEmpIdentificationDocument(MultipartFile multipartFile , EmployeeIdentificationDetailsDto employeeIdentificationDetailsDto){
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				String keyName = "IDF" + "_" + employeeIdentificationDetailsDto.getEmployeeId() + "_" + employeeIdentificationDetailsDto.getIdentificationTypeId();
				String bucket = sessionObject.getBucketName();
				s3Client.putObject(bucket + EMPLOYEE_IDENTIFICATION_DOC, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + EMPLOYEE_IDENTIFICATION_DOC, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + EMPLOYEE_IDENTIFICATION_DOC + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			System.err.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadPathologyOrder(InputStream inputStream, OrderMasterDto orderMasterDto){
		try{
			if(inputStream != null && inputStream.available() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType("application/pdf");
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				String keyName = orderMasterDto.getOrderId() + "_" + orderMasterDto.getAppointmentListDto().getAppointmentId() + "_pathology_order.pdf";
				s3Client.putObject(bucket + ORDER_PATHOLOGY, keyName, inputStream, metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + ORDER_PATHOLOGY, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + ORDER_PATHOLOGY + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			System.err.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadMicrobiologyOrder(InputStream inputStream, OrderMasterDto orderMasterDto){
		try{
			if(inputStream != null && inputStream.available() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType("application/pdf");
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				String keyName = orderMasterDto.getOrderId() + "_" + orderMasterDto.getAppointmentListDto().getAppointmentId() + "_microbiology_order.pdf";
				s3Client.putObject(bucket + ORDER_MICROBIOLOGY, keyName, inputStream, metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + ORDER_MICROBIOLOGY, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + ORDER_MICROBIOLOGY + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			System.err.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadRadiologyOrder(InputStream inputStream, OrderMasterDto orderMasterDto){
		try{
			if(inputStream != null && inputStream.available() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType("application/pdf");
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				String keyName = orderMasterDto.getOrderId() + "_" + orderMasterDto.getAppointmentListDto().getAppointmentId() + "_radiology_order.pdf";
				s3Client.putObject(bucket + ORDER_RADIOLOGY, keyName, inputStream, metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + ORDER_RADIOLOGY, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + ORDER_RADIOLOGY + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			System.err.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	public String uploadPatientDocuments(MultipartFile multipartFile , PatientDocumentMapperDto patientDocumentMapperDto){
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				String keyName = "PAT" + "_" + patientDocumentMapperDto.getPatientId() + "_" + patientDocumentMapperDto.getDocumentTypeId() + "_" + patientDocumentMapperDto.getDocumentDate() + "_" + patientDocumentMapperDto.getTitle().replaceAll("\\s", "") + patientDocumentMapperDto.getIndex();
				String bucket = sessionObject.getBucketName();
				s3Client.putObject(bucket + PATIENT_DOCUMENTS, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + PATIENT_DOCUMENTS, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + PATIENT_DOCUMENTS + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			System.err.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	
	public static Response getFileFromUrl(String fileUrl,PatientMasterDto patientMasterDto) throws InvalidPdfException{
		try{
			List<InputStream> list = new ArrayList<InputStream>();
			fileUrl = fileUrl.replaceAll(" ", "%20");
			PdfReader reader = new PdfReader(new URL(fileUrl).openStream());
			//System.out.println(reader);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 PdfStamper stamper = new PdfStamper(reader, baos);//new FileOutputStream("D:/zv.pdf"));
			    stamper.setEncryption(patientMasterDto.getMrnNumber().getBytes(), "clinivantage".getBytes(), PdfWriter.ALLOW_PRINTING,
			    PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
			    stamper.close();
			    reader.close();
			    //System.out.println(baos.toByteArray().length);
			    InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
			    //System.out.println( inputStream.available());
			    inputStream.close();
			    baos.close();
			    list.add(inputStream);
			    return new Response(SUCCESS, null, null, list);
		}
	    catch(InvalidPdfException ipe){
	    	return new Response(ERROR, null, DOCUMENENT_NOT_SUPPORTED, null);
		}catch(Exception e){
			
			return new Response(ERROR, null, DOCUMENENT_NOT_SUPPORTED, null);
		}
	}
	
	
	public Response uploadDocumentWithPassword(List<String> urlList,PatientMasterDto patientMasterDto) throws InvalidPdfException{
		try{
			List<String> passwordProtectedUrlList = new ArrayList<String>();
			for(String url : urlList){
				Response response = getFileFromUrl(url,patientMasterDto);
				if(response.getStatus().equals(SUCCESS)){
					List<InputStream> list = response.getListObject();
				InputStream inputStream = list.get(0);//getFileFromUrl(url);
				if(inputStream != null && inputStream.available() > 0){
						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType("application/pdf");
						int unique_id= (int) ((TimeZoneComponent.getDateByzone(unitId).getTime() / 1000L) % Integer.MAX_VALUE); 
						String keyName = "PPP" + String.valueOf(unique_id);
						String bucket = sessionObject.getBucketName();
						s3Client.putObject(bucket + PATIENT_DOCS_WITH_PASSWORD, keyName, inputStream, metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + PATIENT_DOCS_WITH_PASSWORD, keyName, CannedAccessControlList.PublicRead);
						//get url of file
						s3Client.getResourceUrl(bucket + PATIENT_DOCS_WITH_PASSWORD, keyName);
						//return COMMON_PATH + PATIENT_DOCS_WITH_PASSWORD + "/" + keyName;
						passwordProtectedUrlList.add(COMMON_PATH + bucket + PATIENT_DOCS_WITH_PASSWORD + "/" + keyName);
					}
				}else{
					return  new Response(ERROR, null, DOCUMENENT_NOT_SUPPORTED, null);
				}
			}
			return new Response(SUCCESS, null, null, passwordProtectedUrlList);
		}catch(AmazonServiceException ae){
			System.err.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	
	public String uploadActiveDepositDocuments(MultipartFile multipartFile, ActiveDepositMasterDto appointmentMasterDto){
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				int unique_id= (int) ((TimeZoneComponent.getDateByzone(unitId).getTime() / 1000L) % Integer.MAX_VALUE); 
				String keyName = appointmentMasterDto.getPatientId() + String.valueOf(unique_id) + "_" + multipartFile.getOriginalFilename();
				s3Client.putObject(bucket + ACTIVE_DEPOSIT_MASTER_DOC_BUCKET, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + ACTIVE_DEPOSIT_MASTER_DOC_BUCKET, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + ACTIVE_DEPOSIT_MASTER_DOC_BUCKET + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			//System.out.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}catch(IOException ie){
			i
		}
		return null;
	}
	
	
	public String uploadAudioFiles(InputStream inputStream,DoctorVoiceRecordingDto doctorVoiceRecordingDto){
		try{
			if(inputStream != null){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType("audio/mp3");
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				int unique_id= (int) ((TimeZoneComponent.getDateByzone(unitId).getTime() / 1000L) % Integer.MAX_VALUE); 
				String keyName = doctorVoiceRecordingDto.getPatientId() +"_"+String.valueOf(unique_id) + "_" + TimeZoneComponent.getDateByzone(unitId).getTime();
				s3Client.putObject(bucket + AUDIO_FILES, keyName, inputStream, metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + AUDIO_FILES, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + AUDIO_FILES + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			//System.out.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}
		return null;
	}
	
	public String uploadInsuranceDocuments(MultipartFile multipartFile,PatientInsuranceDetailsDto insuranceDetailsDto)throws IOException{
		try{
			if(multipartFile != null && multipartFile.getSize() > 0){
				AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
				AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(multipartFile.getContentType());
				//write a file on s3 server
				String bucket = sessionObject.getBucketName();
				int unique_id= (int) ((TimeZoneComponent.getDateByzone(unitId).getTime() / 1000L) % Integer.MAX_VALUE); 
				String keyName = insuranceDetailsDto.getPatientId() + String.valueOf(unique_id) + "_" + multipartFile.getOriginalFilename().replaceAll("\\s", "");
				s3Client.putObject(bucket + PATIENT_INSURANCE_DOCS, keyName, multipartFile.getInputStream(), metadata);
				//Setting Public Permissions to file
				s3Client.setObjectAcl(bucket + PATIENT_INSURANCE_DOCS, keyName, CannedAccessControlList.PublicRead);
				return COMMON_PATH + bucket + PATIENT_INSURANCE_DOCS + "/" + keyName;
			}
		}catch(AmazonServiceException ae){
			//System.out.println("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
		}
		return null;
	}*/
	   
	   
	     @Transactional
		 public String uploadCorporateDocumentImage(MultipartFile multipartFile, DoctorMasterDto doctorMasterDto) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
		        try{
					String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
							"where unit_id = :unitId and organization_id = :organizationId";
					Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
					query.setParameter("unitId", doctorMasterDto.getUnitId());
					query.setParameter("organizationId", doctorMasterDto.getOrganizationId());
					Integer bucketType = (Integer) query.uniqueResult();

		        	if(bucketType == null) {
						bucketType=1;
					}		        	
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   		System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");
		            if(multipartFile != null && multipartFile.getSize() > 0){
		            	if(bucketType == 1) {
		                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
		                ObjectMetadata metadata = new ObjectMetadata();
		                metadata.setContentType(multipartFile.getContentType());
		                //write a file on s3 server
		                String bucket = BUCKET_NAME; 
		              //  String organization =  organizationMasterDto.getOrganizationFolder();
		                String keyName = "Organization_"+doctorMasterDto.getOrganizationId()+ "_Unit_" +doctorMasterDto.getUnitId()+"_"+doctorMasterDto.getDoctorId()+ "_" + multipartFile.getOriginalFilename().replace(" ", "_");
		                s3Client.putObject(bucket + "/" + doctorMasterDto.getOrganizationId()+"/"+CERTIFICATE_BUCKET, keyName, multipartFile.getInputStream(), metadata);
		                //Setting Public Permissions to file
		                s3Client.setObjectAcl(bucket + "/" +doctorMasterDto.getOrganizationId()+"/"+CERTIFICATE_BUCKET, keyName, CannedAccessControlList.PublicRead);
		                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +doctorMasterDto.getOrganizationId()+"/" + CERTIFICATE_BUCKET+"/" + keyName;
		            }else {
							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId", doctorMasterDto.getUnitId());
							query1.setParameter("organizationId", doctorMasterDto.getOrganizationId());
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
		            }
	        }catch(AmazonServiceException ae){
		logger.error("uploadCorporateDocumentImage failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(IOException ie){
		logger.error("uploadCorporateDocumentImage failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
		        return null;
		    }

	     @Transactional
		public String updateConsentMasterForFileUploading(MultipartFile file, String organizationFolder,
				int consentMasterId, Integer Org, Integer unitId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
			 try{
				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where organization_id = :organizationId and unit_id = :unitId";
				 Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("organizationId", Org);
				 query.setParameter("unitId", unitId);
				 Integer bucketType = (Integer) query.uniqueResult();

				 if(bucketType == null) {
						bucketType=1;
					}
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

				   System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");				
				   if(bucketType == 1) {
		                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
		                ObjectMetadata metadata = new ObjectMetadata();
		                metadata.setContentType(file.getContentType());
		                String bucket = BUCKET_NAME; 
		                String keyName = "Organization_"+Org+ "_" +consentMasterId+ "_" + file.getOriginalFilename();
		                s3Client.putObject(bucket + "/" + Org+"/"+EPISODIC_DOCUMENTS_LETTERS_BUCKET, keyName, file.getInputStream(), metadata);
		                s3Client.setObjectAcl(bucket + "/" +Org+"/"+EPISODIC_DOCUMENTS_LETTERS_BUCKET, keyName, CannedAccessControlList.PublicRead);
		                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +Org+"/" + EPISODIC_DOCUMENTS_LETTERS_BUCKET+"/" + keyName;
				   }else {

					   String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
							   "where organization_id = :organizationId and unit_id = :unitId";
					   Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
					   query1.setParameter("organizationId", Org);
					   query1.setParameter("unitId", unitId);
					   String localBucketJson = (String) query1.uniqueResult();

					   Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = file.getOriginalFilename();
			            String contentType =file.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = file.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   } 
		           
	        }catch(AmazonServiceException ae){
		logger.error("updateConsentMasterForFileUploading failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(IOException ie){
		logger.error("updateConsentMasterForFileUploading failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
		}

	    @Transactional
		public String uploadDischargeSummaryReport(ByteArrayOutputStream byteStream,
				OrganizationMasterDto organizationMasterDto, Integer unitId, Integer dischargeSummaryId) {
			 try{
				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where unit_id = :unitId and organization_id = :organizationId";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("unitId", unitId);
				 query.setParameter("organizationId", organizationMasterDto.getOrganizationId());
				 Integer bucketType = (Integer) query.uniqueResult();
				 if(bucketType == null) {
						bucketType=1;
					}
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

				   System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");
				   InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
		            if(byteStream!= null){
		            	if(bucketType == 1) {
		                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
		                ObjectMetadata metadata = new ObjectMetadata();
		                metadata.setContentType("application/pdf");
		                //write a file on s3 server
		                String bucket = BUCKET_NAME; 
		                String organization =  organizationMasterDto.getOrganizationFolder();
		                String keyName =  "dischargeSummary_" + dischargeSummaryId + "_" + organizationMasterDto.getOrganizationId() + "_" + unitId ;
		               // InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
		                s3Client.putObject(bucket + "/" + organization+ADMINISTRATIVE_DISCHARGE_SUMMARY_BUCKET, keyName, inputStream, metadata);
		                //Setting Public Permissions to file
		                s3Client.setObjectAcl(bucket + "/" +organization+ADMINISTRATIVE_DISCHARGE_SUMMARY_BUCKET, keyName, CannedAccessControlList.PublicRead);
		                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + ADMINISTRATIVE_DISCHARGE_SUMMARY_BUCKET+"/" + keyName;
		            	}else {
							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createQuery(queryStr1);
							query1.setParameter("unitId", unitId);
							query1.setParameter("organizationId", organizationMasterDto.getOrganizationId());
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");
				            
				            LocalDateTime currentDateTime = LocalDateTime.now();

				           String objectName = "dischargeSummary_" + dischargeSummaryId + "_" + currentDateTime;
				            String contentType ="application/pdf";

				            // Get InputStream from MultipartFile
				            //InputStream inputStream = multipartFile.getInputStream();
				            minioClient.putObject(PutObjectArgs.builder()
				                    .bucket(encounterDocDetailsDtoNew.getBucketName())
				                    .object(objectName)
				                    .contentType(contentType)
				                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
				                    .build());
				                           
				            System.out.println("Multipart file uploaded successfully.");
				            String url = minioClient.getPresignedObjectUrl(
				                    GetPresignedObjectUrlArgs.builder()
				                            .method(Method.GET)
				                            .bucket(encounterDocDetailsDtoNew.getBucketName())
				                           // .object(objectName)
				                            .build());
				            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
						   }
		            }
	        }catch(AmazonServiceException ae){
		logger.error("uploadDischargeSummaryReport failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(Exception ie){
		logger.error("uploadDischargeSummaryReport failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
		        return null;
		}

	    @Transactional
		 public String uploadFundRequestDocuments(MultipartFile multipartFile, String orgFolderPath,FundSanctionRequestDocumentDto reqDto) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException{
			   try{

				   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						   "where unit_id = :unitId and organization_id = :organizationId";
				   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				   query.setParameter("unitId", reqDto.getUnitId());
				   query.setParameter("organizationId", reqDto.getOrgId());
				   Integer bucketType = (Integer) query.uniqueResult();
				   if(bucketType == null) {
						bucketType=1;
					}
				   
					if(multipartFile != null && multipartFile.getSize() > 0){
						if(bucketType == 1) {

							OrganizationMasterDto awsCredDto = getAWSCreds();
							String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
							String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType(multipartFile.getContentType());
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  orgFolderPath;
						String keyName = reqDto.getOrgId()+"_"+reqDto.getUnitId()+"_"+reqDto.getDocumentTypeId() + "_" + multipartFile.getOriginalFilename();
						s3Client.putObject(bucket + "/" + organization+FUND_REQUEST_DOCUMENTS_BUCKET, keyName, multipartFile.getInputStream(), metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+FUND_REQUEST_DOCUMENTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + FUND_REQUEST_DOCUMENTS_BUCKET+"/" + keyName;
						}else {
							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId", reqDto.getUnitId());
							query1.setParameter("organizationId", reqDto.getOrgId());
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");

				            String objectName = multipartFile.getOriginalFilename();
				            String contentType =multipartFile.getContentType();

				            // Get InputStream from MultipartFile
				            InputStream inputStream = multipartFile.getInputStream();
			 
				            minioClient.putObject(PutObjectArgs.builder()
				                    .bucket(encounterDocDetailsDtoNew.getBucketName())
				                    .object(objectName)
				                    .contentType(contentType)
				                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
				                    .build());
				                           
				            System.out.println("Multipart file uploaded successfully.");
				            String url = minioClient.getPresignedObjectUrl(
				                    GetPresignedObjectUrlArgs.builder()
				                            .method(Method.GET)
				                            .bucket(encounterDocDetailsDtoNew.getBucketName())
				                            .object(objectName)
				                            .build());
				            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
						   }
					}
			}catch(AmazonServiceException ae){
				logger.error("uploadFundRequestDocuments failed", ae);
				throw new ServiceException(ae.getMessage());
			}catch(IOException ie){
				logger.error("uploadFundRequestDocuments failed", ie);
				throw new ServiceException(ie.getMessage());
			}
				return null;
		 }

	    @Transactional
		public String uploadPatientDialysisDocument(MultipartFile multipartFile,
				PatientDialysisRecordDto patientDialysisRecordDto) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
			try{
				String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						"where unit_id = :unitId and organization_id = :organizationId";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				query.setParameter("unitId", patientDialysisRecordDto.getUnitId());
				query.setParameter("organizationId", patientDialysisRecordDto.getOrganizationId());
				Integer bucketType = (Integer) query.uniqueResult();
				if(bucketType == null) {
					bucketType=1;
				}
				if(multipartFile != null && multipartFile.getSize() > 0){
					if(bucketType == 1) {

						OrganizationMasterDto awsCredDto = getAWSCreds();
						String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
						String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

					AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentType(multipartFile.getContentType());
					//write a file on s3 server
					String bucket = BUCKET_NAME; 
					String organization =  patientDialysisRecordDto.getOrgFolderPath();
					String keyName = patientDialysisRecordDto.getOrganizationId()+"_"+patientDialysisRecordDto.getUnitId()+"_"
					+patientDialysisRecordDto.getPatientDialysisRecId()+"_"+multipartFile.getOriginalFilename();
					s3Client.putObject(bucket + "/" + organization+PATIENT_DIALYSIS_DOCUMENTS_BUCKET, keyName, multipartFile.getInputStream(), metadata);
					//Setting Public Permissions to file
					s3Client.setObjectAcl(bucket + "/" +organization+PATIENT_DIALYSIS_DOCUMENTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
					return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + PATIENT_DIALYSIS_DOCUMENTS_BUCKET+"/" + keyName;
				}else {
						String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								"where unit_id = :unitId and organization_id = :organizationId";
						Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						query1.setParameter("unitId", patientDialysisRecordDto.getUnitId());
						query1.setParameter("organizationId", patientDialysisRecordDto.getOrganizationId());
						String localBucketJson = (String) query1.uniqueResult();
						Gson gson = new Gson();
					   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
	  
				   MinioClient minioClient = new MinioClient.Builder()
			                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
			                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
			                .build();
				   			   			   
				   System.out.println("MINIO Connectionnn is Createdddddd........");
		            Map<String, String> headers = new HashMap<>();
		            headers.put("X-Amz-Meta-Description", "This is a test file.");

		            String objectName = multipartFile.getOriginalFilename();
		            String contentType =multipartFile.getContentType();

		            // Get InputStream from MultipartFile
		            InputStream inputStream = multipartFile.getInputStream();
	 
		            minioClient.putObject(PutObjectArgs.builder()
		                    .bucket(encounterDocDetailsDtoNew.getBucketName())
		                    .object(objectName)
		                    .contentType(contentType)
		                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
		                    .build());
		                           
		            System.out.println("Multipart file uploaded successfully.");
		            String url = minioClient.getPresignedObjectUrl(
		                    GetPresignedObjectUrlArgs.builder()
		                            .method(Method.GET)
		                            .bucket(encounterDocDetailsDtoNew.getBucketName())
		                            .object(objectName)
		                            .build());
		            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
		            return url;
				   }
				}
			}catch(AmazonServiceException ae){
				logger.error("uploadPatientDialysisDocument failed", ae);
				throw new ServiceException(ae.getMessage());
			}catch(IOException ie){
				logger.error("uploadPatientDialysisDocument failed", ie);
				throw new ServiceException(ie.getMessage());
			}
			return null;
		}

		 
	    @Transactional
		 public String uploadWorkorderRequestDocuments(MultipartFile multipartFile, String orgFolderPath,WorkOrderCreationRequestDocumentDto reqDto) {
			   try{
				   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						   "where unit_id = :unitId and organization_id = :organizationId";
				   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				   query.setParameter("unitId", reqDto.getUnitId());
				   query.setParameter("organizationId", reqDto.getOrgId());
				   Integer bucketType = (Integer) query.uniqueResult();
				   if(bucketType == null) {
						bucketType=1;
					}
					if(multipartFile != null && multipartFile.getSize() > 0){
						if(bucketType == 1) {

							OrganizationMasterDto awsCredDto = getAWSCreds();
							String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
							String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType(multipartFile.getContentType());
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  orgFolderPath;
						String keyName = reqDto.getOrgId()+"_"+reqDto.getUnitId()+"_"+reqDto.getDocumentTypeId() + "_Image_" + multipartFile.getOriginalFilename();
						s3Client.putObject(bucket + "/" + organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, multipartFile.getInputStream(), metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + WORKORDER_REQUEST_DOCUMENTS_BUCKET+"/" + keyName;
						}else {

							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId", reqDto.getUnitId());
							query1.setParameter("organizationId", reqDto.getOrgId());
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");

				            String objectName = multipartFile.getOriginalFilename();
				            String contentType =multipartFile.getContentType();

				            // Get InputStream from MultipartFile
				            InputStream inputStream = multipartFile.getInputStream();
			 
				            try {
								minioClient.putObject(PutObjectArgs.builder()
								        .bucket(encounterDocDetailsDtoNew.getBucketName())
								        .object(objectName)
								        .contentType(contentType)
								        .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
								        .build());
					} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
							| InternalException | InvalidResponseException | NoSuchAlgorithmException
							| ServerException | XmlParserException | IllegalArgumentException e) {
						logger.error("uploadWorkorderRequestDocuments failed", e);
						throw new ServiceException(e.getMessage());
					}
				                           
				            System.out.println("Multipart file uploaded successfully.");
				            String url;
							try {
								url = minioClient.getPresignedObjectUrl(
								        GetPresignedObjectUrlArgs.builder()
								                .method(Method.GET)
								                .bucket(encounterDocDetailsDtoNew.getBucketName())
								                .object(objectName)
								                .build());
								System.out.println("MY MINIO URL ISSSSSS.....: " + url);	   
								  return url;
					} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
							| InternalException | InvalidResponseException | NoSuchAlgorithmException
							| XmlParserException | ServerException | IllegalArgumentException e) {
						logger.error("uploadWorkorderRequestDocuments failed", e);
						throw new ServiceException(e.getMessage());
					}
				                     
				          
						   }
					}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadWorkorderRequestDocuments failed", ie);
				throw new ServiceException(ie.getMessage());
			}
				return null;
		 }

	    @Transactional
		public String uploadIntraOperativeImage(MultipartFile multipartFile, String orgFolderPath,
				IntraOperativeImageDto dto) {
			  try{
				  String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						  "where unit_id = :unitId and organization_id = :organizationId";
				  Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				  query.setParameter("unitId", dto.getUnitId());
				  query.setParameter("organizationId", dto.getOrganizationId());
				  Integer bucketType = (Integer) query.uniqueResult();
				  if(bucketType == null) {
						bucketType=1;
					}
					if(multipartFile != null && multipartFile.getSize() > 0){
						if(bucketType == 1) {

							OrganizationMasterDto awsCredDto = getAWSCreds();
							String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
							String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType(multipartFile.getContentType());
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  orgFolderPath;
						String keyName = dto.getOrganizationId()+"_"+dto.getUnitId()+"_"+dto.getIntraOperativeId() + "_Video_" + multipartFile.getOriginalFilename();
						s3Client.putObject(bucket + "/" + organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, multipartFile.getInputStream(), metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + WORKORDER_REQUEST_DOCUMENTS_BUCKET+"/" + keyName;
						}else {
							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId", dto.getUnitId());
							query1.setParameter("organizationId", dto.getOrganizationId());
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");

				            String objectName = multipartFile.getOriginalFilename();
				            String contentType =multipartFile.getContentType();

				            // Get InputStream from MultipartFile
				            InputStream inputStream = multipartFile.getInputStream();
			 
				            try {
								minioClient.putObject(PutObjectArgs.builder()
								        .bucket(encounterDocDetailsDtoNew.getBucketName())
								        .object(objectName)
								        .contentType(contentType)
								        .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
								        .build());
					} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException
							| InternalException | InvalidResponseException | NoSuchAlgorithmException
							| ServerException | XmlParserException | IllegalArgumentException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					}
				                           
				            System.out.println("Multipart file uploaded successfully for uploadIntraOperativeImage.");
				            String url = null;
							try {
								url = minioClient.getPresignedObjectUrl(
								        GetPresignedObjectUrlArgs.builder()
								                .method(Method.GET)
								                .bucket(encounterDocDetailsDtoNew.getBucketName())
								                .object(objectName)
								                .build());
					} catch (InvalidKeyException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ErrorResponseException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InsufficientDataException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InternalException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InvalidResponseException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (NoSuchAlgorithmException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (XmlParserException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ServerException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (IllegalArgumentException e) {
						logger.error("uploadIntraOperativeImage failed", e);
						throw new ServiceException(e.getMessage());
					}
				            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
						   }
					}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadIntraOperativeImage failed", ie);
				throw new ServiceException(ie.getMessage());
			}
				return null;
		}

	    @Transactional
		public String uploadLabReportHandOverImage(MultipartFile multipartFile, String orgFolderPath,
				Integer orgId  , Integer unitId)  {
			 try{
				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where unit_id = :unitId and organization_id = :organizationId";
				 Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("unitId", unitId);
				 query.setParameter("organizationId",orgId);
				 Integer bucketType = (Integer) query.uniqueResult();
				 if(bucketType == null) {
						bucketType=1;
					}
					if(multipartFile != null && multipartFile.getSize() > 0){
						if(bucketType == 1) {

							OrganizationMasterDto awsCredDto = getAWSCreds();
							String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
							String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType(multipartFile.getContentType());
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  orgFolderPath;
						String keyName = orgId+"_"+unitId+"_Image_" + multipartFile.getOriginalFilename();
						s3Client.putObject(bucket + "/" + organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, multipartFile.getInputStream(), metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + WORKORDER_REQUEST_DOCUMENTS_BUCKET+"/" + keyName;
					}else {

							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId",unitId);
							query1.setParameter("organizationId", orgId);
							String localBucketJson = (String) query1.uniqueResult();
			   				   
						   Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = multipartFile.getOriginalFilename();
			            String contentType =multipartFile.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = multipartFile.getInputStream();
		 
			            try {
							minioClient.putObject(PutObjectArgs.builder()
							        .bucket(encounterDocDetailsDtoNew.getBucketName())
							        .object(objectName)
							        .contentType(contentType)
							        .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
							        .build());
					} catch (InvalidKeyException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ErrorResponseException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InsufficientDataException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InternalException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InvalidResponseException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (NoSuchAlgorithmException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ServerException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (XmlParserException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (IllegalArgumentException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					}
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url=null;
						try {
							url = minioClient.getPresignedObjectUrl(
							        GetPresignedObjectUrlArgs.builder()
							                .method(Method.GET)
							                .bucket(encounterDocDetailsDtoNew.getBucketName())
							                .object(objectName)
							                .build());
					} catch (InvalidKeyException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ErrorResponseException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InsufficientDataException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InternalException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InvalidResponseException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (NoSuchAlgorithmException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (XmlParserException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ServerException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					} catch (IllegalArgumentException e) {
						logger.error("uploadLabReportHandOverImage failed", e);
						throw new ServiceException(e.getMessage());
					}
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
					}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadLabReportHandOverImage failed", ie);
				throw new ServiceException(ie.getMessage());
			}
				return null;
		}

	    @Transactional
		public String uploadpatientConsentVideo(MultipartFile multipartFile, String organizationFolder,
				PatientConsentMapperDto dto) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
			 try{
				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where unit_id = :unitId and organization_id = :organizationId";
				 Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("unitId", dto.getUnitId());
				 query.setParameter("organizationId", dto.getOrganizationId());
				 Integer bucketType = (Integer) query.uniqueResult();
				 if(bucketType == null) {
						bucketType=1;
					}
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

				   System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");
					if(multipartFile != null && multipartFile.getSize() > 0){
						if(bucketType == 1) {
						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType(multipartFile.getContentType());
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  organizationFolder;
						String keyName = dto.getOrganizationId()+"_"+dto.getUnitId()+"_"+dto.getConsentTransactionId() + "Consent_Vide" + multipartFile.getOriginalFilename();
						s3Client.putObject(bucket + "/" + organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, multipartFile.getInputStream(), metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+WORKORDER_REQUEST_DOCUMENTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + WORKORDER_REQUEST_DOCUMENTS_BUCKET+"/" + keyName;
						}else {
							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId", dto.getUnitId());
							query1.setParameter("organizationId", dto.getOrganizationId());
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");

				            String objectName = multipartFile.getOriginalFilename();
				            String contentType =multipartFile.getContentType();

				            // Get InputStream from MultipartFile
				            InputStream inputStream = multipartFile.getInputStream();
			 
				            minioClient.putObject(PutObjectArgs.builder()
				                    .bucket(encounterDocDetailsDtoNew.getBucketName())
				                    .object(objectName)
				                    .contentType(contentType)
				                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
				                    .build());
				                           
				            System.out.println("Multipart file uploaded successfully.");
				            String url = minioClient.getPresignedObjectUrl(
				                    GetPresignedObjectUrlArgs.builder()
				                            .method(Method.GET)
				                            .bucket(encounterDocDetailsDtoNew.getBucketName())
				                            .object(objectName)
				                            .build());
				            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
						   }
					}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadpatientConsentVideo failed", ie);
				throw new ServiceException(ie.getMessage());
			}
				return null;
		}

	       @Transactional
		   public String uploadMultilingualCoversheet(String orgFolderPath,EncounterMasterDto encounterMasterDto){
			   try{
	                //HTML CASESHEET
					ByteArrayOutputStream byteOutPutStreamHtml =null;
					  CloseableHttpClient httpClientHtml = null;
					  InputStream htmlInputStream =null;
	                 byteOutPutStreamHtml = new ByteArrayOutputStream();
	                 httpClientHtml = HttpClientBuilder.create().build();

	       	      String reportParam=" {"
	                      +"'appName': 'HIS',"
	                      +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	                      +"'reportDesignFilePath': 'NewCoverSheet_multi.rpt',"
	                      +"'reportFormat': 'html',"
	                      +"'exportFileName': 'Coversheet',"
	                      +"'reportParams': ["
	                        +"{"
	       	               +"'paramName': 'Patientid',"
	       			       +"'paramType': 'Integer',"
	       			       +"'paramValue':"+encounterMasterDto.getPatientId()
	       	              +"}," 
	       	              +"{"
	       	               +"'paramName': 'Unitid',"
	       			       +"'paramType': 'Integer',"
	       			       +"'paramValue':"+encounterMasterDto.getUnitId()// $scope.unitId
	       	              +"}," 
	       	              +"{"
	       	               +"'paramName': 'Encounter_id',"
	       			       +"'paramType': 'Integer',"
	       			       +"'paramValue':"+encounterMasterDto.getEncounterId()// $scope.encounterDetailsObj.encounterId
	       	              +"}" 
	                      +"]"
	                      +"}" ;
	               
	       	        String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(encounterMasterDto.getOrganizationId(),encounterMasterDto.getUnitId());
	               if(API_URL==null)
	            	   return null;
	       	        HttpPost postHtml = new HttpPost(API_URL);
	                postHtml.addHeader("Content-Type", "application/json");
	                postHtml.setEntity(new StringEntity(reportParam));

	                HttpResponse resultHtml = (HttpResponse) httpClientHtml.execute(postHtml);
	                HttpEntity resEntityHtml = ((org.apache.http.HttpResponse) resultHtml).getEntity();
	                byteOutPutStreamHtml.write(ICommonConstants.GOOGLE_TRANSLATE.getBytes());
	                resEntityHtml.writeTo(byteOutPutStreamHtml);
//	                byteOutPutStreamHtml.write(ICommonConstants.GOOGLE_TRANSLATE_DISC.getBytes());
	                 htmlInputStream = new ByteArrayInputStream(byteOutPutStreamHtml.toByteArray());
				   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						   "where unit_id = :unitId and organization_id = :organizationId";
				   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				   query.setParameter("unitId", encounterMasterDto.getUnitId());
				   query.setParameter("organizationId", encounterMasterDto.getOrganizationId());
				   Integer bucketType = (Integer) query.uniqueResult();
				   if(bucketType == null) {
	 					bucketType=1;
	 				}
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

				   System.out.println("My bucket Type issssssss:"+ bucketType );
	 			   System.out.println("UPLOAD CODE Starts here........");
	 			  if(bucketType == 1) {       
					//if(multipartFile != null && multipartFile.getSize() > 0){
						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType("text/html");
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  orgFolderPath;
						String keyName = encounterMasterDto.getOrganizationId()+"_"+encounterMasterDto.getUnitId()+"_"+encounterMasterDto.getEncounterId() + "_multilingual_coversheet.html";
						s3Client.putObject(bucket + "/" + organization+OP_COVERSHEET_BUCKET, keyName, htmlInputStream, metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+OP_COVERSHEET_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + OP_COVERSHEET_BUCKET+"/" + keyName;
	 			 }else {
					  String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
							  "where unit_id = :unitId and organization_id = :organizationId";
					  Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
					  query1.setParameter("unitId", encounterMasterDto.getUnitId());
					  query1.setParameter("organizationId", encounterMasterDto.getOrganizationId());
					  String localBucketJson = (String) query1.uniqueResult();
					  Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = encounterMasterDto.getOrganizationId()+"_"+encounterMasterDto.getUnitId()+"_"+ "_multilingual_coversheet.html";
			            String contentType ="text/html";

			            // Get InputStream from MultipartFile
			          //  InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(htmlInputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
						
				//	}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadMultilingualCoversheet failed", ie);
				throw new ServiceException(ie.getMessage());
			}
		   catch (Exception e) {
			   logger.error("uploadMultilingualCoversheet failed", e);
			   throw new ServiceException(e.getMessage());
		   }
				return null;
		    }
		   
		   
		   
		  
	    @Transactional
		public String uploadwhatsAppDocumentFile(ByteArrayOutputStream report,
				String fileName, SmsUtilityConfigInfoDto smsInfo) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
			   try{
				   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						   "where unit_id = :unitId and organization_id = :organizationId";
				   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				   query.setParameter("unitId", smsInfo.getUnitId());
				   query.setParameter("organizationId", smsInfo.getOrganizationId());
				   Integer bucketType = (Integer) query.uniqueResult();
				   if(bucketType == null) {
						bucketType=1;
					}
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

				   System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");
				   InputStream inputStream = new ByteArrayInputStream(report.toByteArray());
				   if(inputStream != null && inputStream.available() > 0){
					   if(bucketType == 1) {
					   AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					   AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					   ObjectMetadata metadata = new ObjectMetadata();
					   metadata.setContentType("application/pdf");
					 //write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organizationUnit =  smsInfo.getUnitFolder();
						String keyName = smsInfo.getOrganizationId()+"_"+smsInfo.getUnitId()+"_"+fileName;
						s3Client.putObject(bucket + "/" + organizationUnit+CLINICAL_STAFF_PHOTO_BUCKET, keyName, inputStream, metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organizationUnit+CLINICAL_STAFF_PHOTO_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organizationUnit + CLINICAL_STAFF_PHOTO_BUCKET+"/" + keyName;
					   }else {
						   String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								   "where unit_id = :unitId and organization_id = :organizationId";
						   Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						   query1.setParameter("unitId", smsInfo.getUnitId());
						   query1.setParameter("organizationId", smsInfo.getOrganizationId());
						   String localBucketJson = (String) query1.uniqueResult();
						   Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");
				            String objectName = fileName;
				            String contentType ="application/pdf";

				            // Get InputStream from MultipartFile
				            //InputStream inputStream = multipartFile.getInputStream();
			 
				            minioClient.putObject(PutObjectArgs.builder()
				                    .bucket(encounterDocDetailsDtoNew.getBucketName())
				                    .object(objectName)
				                    .contentType(contentType)
				                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
				                    .build());
				                           
				            System.out.println("Multipart file uploaded successfully.");
				            String url = minioClient.getPresignedObjectUrl(
				                    GetPresignedObjectUrlArgs.builder()
				                            .method(Method.GET)
				                            .bucket(encounterDocDetailsDtoNew.getBucketName())
				                            .object(objectName)
				                            .build());
				            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
						   }
					   }
					
			}catch(AmazonServiceException ae){
				logger.error("uploadwhatsAppDocumentFile failed", ae);
				throw new ServiceException(ae.getMessage());
			}catch(IOException ie){
				logger.error("uploadwhatsAppDocumentFile failed", ie);
				throw new ServiceException(ie.getMessage());
			}
				return null;
		    }

	    @Transactional
		public String uploadLabReportPatientDocuments(MultipartFile file, String organizationFolder,
				Integer organizationId, Integer unitId, Integer labSampleDtlsId,Integer testId)  {
			 try{
				 String url=null;
				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where unit_id = :unitId and organization_id = :organizationId";
				 Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("unitId", unitId);
				 query.setParameter("organizationId", organizationId);
				 Integer bucketType = (Integer) query.uniqueResult();
				 if(bucketType == null) {
						bucketType=1;
					}
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

				   System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");
					if(file != null && file.getSize() > 0){
						if(bucketType == 1) {
						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType(file.getContentType());
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  organizationFolder;
						String keyName = organizationId+"_"+unitId+"_"+labSampleDtlsId + "_" + testId + file.getOriginalFilename();
						s3Client.putObject(bucket + "/" + organization+LAB_REPORTS_BUCKET, keyName, file.getInputStream(), metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+LAB_REPORTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + LAB_REPORTS_BUCKET+"/" + keyName;
						}else {
							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId", unitId);
							query1.setParameter("organizationId", organizationId);
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");

				            String objectName = file.getOriginalFilename();
				            String contentType =file.getContentType();

				            // Get InputStream from MultipartFile
				            InputStream inputStream = file.getInputStream();
			 
				            try {
								minioClient.putObject(PutObjectArgs.builder()
								        .bucket(encounterDocDetailsDtoNew.getBucketName())
								        .object(objectName)
								        .contentType(contentType)
								        .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
								        .build());
					} catch (InvalidKeyException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (ErrorResponseException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (InsufficientDataException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (InternalException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (InvalidResponseException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (NoSuchAlgorithmException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (ServerException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (XmlParserException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					} catch (IllegalArgumentException e1) {
						logger.error("uploadLabReportPatientDocuments failed", e1);
						throw new ServiceException(e1.getMessage());
					}
				                           
				            System.out.println("Multipart file uploaded successfully.");
							try {
								url = minioClient.getPresignedObjectUrl(
								        GetPresignedObjectUrlArgs.builder()
								                .method(Method.GET)
								                .bucket(encounterDocDetailsDtoNew.getBucketName())
								                .object(objectName)
								                .build());
					} catch (InvalidKeyException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ErrorResponseException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InsufficientDataException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InternalException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (InvalidResponseException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (NoSuchAlgorithmException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (XmlParserException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (ServerException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					} catch (IllegalArgumentException e) {
						logger.error("uploadLabReportPatientDocuments failed", e);
						throw new ServiceException(e.getMessage());
					}
				            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
						   }
					}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadLabReportPatientDocuments failed", ie);
				throw new ServiceException(ie.getMessage());
			}
				return null;
		}

	    @SuppressWarnings("unchecked")
		@Transactional
		public String updateQRFile(ByteArrayOutputStream bos, String organizationFolder, String randamNumber, Integer orgId, Integer unitId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
			 try{

				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where organization_id = :orgId and unit_id = :unitId";
				 Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("orgId", orgId);
				 query.setParameter("unitId", unitId);
				 Integer bucketType = (Integer) query.uniqueResult();
				 if(bucketType == null) {
						bucketType=1;
					}
							   //System.out.println("My-UNIT isss"+list.get(0));
					OrganizationMasterDto awsCredDto = getAWSCreds();
					String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
					String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

				   System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");				   
				   InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				   if(inputStream != null && inputStream.available() > 0){
					   if(bucketType == 1) {
					   AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
					   AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
					   ObjectMetadata metadata = new ObjectMetadata();
					   metadata.setContentType("image/png");
					 //write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organizationUnit =  organizationFolder;
						String keyName = orgId+"_"+randamNumber+"_QR.png";
						s3Client.putObject(bucket + "/" + organizationUnit+LAB_REPORTS_BUCKET, keyName, inputStream, metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organizationUnit+LAB_REPORTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organizationUnit + LAB_REPORTS_BUCKET+"/" + keyName;
				   }else {
						   String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								   "where organization_id = :orgId and unit_id = :unitId";
						   Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						   query1.setParameter("orgId", orgId);
						   query1.setParameter("unitId", unitId);
						   String localBucketJson = (String) query1.uniqueResult();
						   Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = orgId+"_"+randamNumber+"_QR.png";
			            String contentType ="image/png";

			            // Get InputStream from MultipartFile
			           // InputStream inputStream = multipartFile.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
					   }
					
			}catch(AmazonServiceException ae){
				logger.error("updateQRFile failed", ae);
				throw new ServiceException(ae.getMessage());
			}catch(IOException ie){
				logger.error("updateQRFile failed", ie);
				throw new ServiceException(ie.getMessage());
			} catch (RuntimeException e) {
				logger.error("updateQRFile failed", e);
				throw new ServiceException(e.getMessage());
			} catch (Exception e) {
				logger.error("updateQRFile failed", e);
				throw new ServiceException(e.getMessage());
			}
				return null;
		}

	    @Transactional
		public String UoploadPacCardForFile(MultipartFile file, String organizationFolder, Integer organizationId, Integer billId, Boolean flag, Integer unitId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
			 try{
				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where organization_id = :organizationId and unit_id = :unitId";
				 Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("organizationId", organizationId);
				 query.setParameter("unitId", unitId);
				 Integer bucketType = (Integer) query.uniqueResult();
				 if(bucketType == null) {
						bucketType=1;
				}
				OrganizationMasterDto awsCredDto = getAWSCreds();
				String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
				String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
				   System.out.println("UPLOAD CODE Starts here........");
				   if(bucketType == 1) {
	                AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	                AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	                ObjectMetadata metadata = new ObjectMetadata();
	                metadata.setContentType(file.getContentType());
	                String bucket = BUCKET_NAME; 
	                String keyName = "Organization_"+organizationId+ "_" +billId+ "_" +flag+ "_" + file.getOriginalFilename();
	                s3Client.putObject(bucket + "/" + organizationId+"/"+EPISODIC_DOCUMENTS_LETTERS_BUCKET, keyName, file.getInputStream(), metadata);
	                s3Client.setObjectAcl(bucket + "/" +organizationId+"/"+EPISODIC_DOCUMENTS_LETTERS_BUCKET, keyName, CannedAccessControlList.PublicRead);
	                return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organizationId+"/" + EPISODIC_DOCUMENTS_LETTERS_BUCKET+"/" + keyName;
				   }else {
					   String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
							   "where organization_id = :organizationId and unit_id = :unitId";
					   Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
					   query1.setParameter("organizationId", organizationId);
					   query1.setParameter("unitId", unitId);
					   String localBucketJson = (String) query1.uniqueResult();
					   Gson gson = new Gson();
						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
		  
					   MinioClient minioClient = new MinioClient.Builder()
				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
				                .build();
					   			   			   
					   System.out.println("MINIO Connectionnn is Createdddddd........");
			            Map<String, String> headers = new HashMap<>();
			            headers.put("X-Amz-Meta-Description", "This is a test file.");

			            String objectName = file.getOriginalFilename();
			            String contentType =file.getContentType();

			            // Get InputStream from MultipartFile
			            InputStream inputStream = file.getInputStream();
		 
			            minioClient.putObject(PutObjectArgs.builder()
			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
			                    .object(objectName)
			                    .contentType(contentType)
			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
			                    .build());
			                           
			            System.out.println("Multipart file uploaded successfully.");
			            String url = minioClient.getPresignedObjectUrl(
			                    GetPresignedObjectUrlArgs.builder()
			                            .method(Method.GET)
			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
			                            .object(objectName)
			                            .build());
			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
			            return url;
					   }
	           
	        }catch(AmazonServiceException ae){
		logger.error("UoploadPacCardForFile failed", ae);
		throw new ServiceException(ae.getMessage());
	        }catch(IOException ie){
		logger.error("UoploadPacCardForFile failed", ie);
		throw new ServiceException(ie.getMessage());
	        }
		}

	    @Transactional
        public String updateRISPdfFile(ByteArrayOutputStream byteOutPutStream, String organizationFolder, String string,
                Integer organizationId, Integer unitId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
             try{

				 String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						 "where organization_id = :organizationId and unit_id = :unitId";
				 Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				 query.setParameter("organizationId", organizationId);
				 query.setParameter("unitId", unitId);
				 Integer bucketType = (Integer) query.uniqueResult();
				 if(bucketType == null) {
 					bucketType=1;
 				}
				OrganizationMasterDto awsCredDto = getAWSCreds();
				String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
				String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

			   System.out.println("My bucket Type issssssss:"+ bucketType );
 			   System.out.println("UPLOAD CODE Starts here........");
                   InputStream inputStream = new ByteArrayInputStream(byteOutPutStream.toByteArray());
                   if(inputStream != null && inputStream.available() > 0){
                	   if(bucketType == 1) {
                       AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
                       AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
                       ObjectMetadata metadata = new ObjectMetadata();
                       metadata.setContentType("application/pdf");
                     //write a file on s3 server
                        String bucket = BUCKET_NAME; 
                        String organizationUnit =  organizationFolder;
                        String keyName = organizationId+"_"+string+"_PDF.pdf";
                        s3Client.putObject(bucket + "/" + organizationUnit+LAB_REPORTS_BUCKET, keyName, inputStream, metadata);
                        //Setting Public Permissions to file
                        s3Client.setObjectAcl(bucket + "/" +organizationUnit+LAB_REPORTS_BUCKET, keyName, CannedAccessControlList.PublicRead);
                        return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organizationUnit + LAB_REPORTS_BUCKET+"/" + keyName;
                	   }else {
						   String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
								   "where organization_id = :organizationId and unit_id = :unitId";
						   Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
						   query1.setParameter("organizationId", organizationId);
						   query1.setParameter("unitId", unitId);
						   String localBucketJson = (String) query1.uniqueResult();
						   Gson gson = new Gson();
   						   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
   		  
   					   MinioClient minioClient = new MinioClient.Builder()
   				                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
   				                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
   				                .build();
   					   			   			   
   					   System.out.println("MINIO Connectionnn is Createdddddd........");
   			            Map<String, String> headers = new HashMap<>();
   			            headers.put("X-Amz-Meta-Description", "This is a test file.");

   			            String objectName = organizationId+"_"+string+"_PDF.pdf";;
   			            String contentType ="application/pdf";

   			            // Get InputStream from MultipartFile
   			           // InputStream inputStream = multipartFile.getInputStream();
   		 
   			            minioClient.putObject(PutObjectArgs.builder()
   			                    .bucket(encounterDocDetailsDtoNew.getBucketName())
   			                    .object(objectName)
   			                    .contentType(contentType)
   			                    .stream(inputStream, -1, 10485760) // -1 for unknown object size, 10MB buffer size
   			                    .build());
   			                           
   			            System.out.println("Multipart file uploaded successfully.");
   			            String url = minioClient.getPresignedObjectUrl(
   			                    GetPresignedObjectUrlArgs.builder()
   			                            .method(Method.GET)
   			                            .bucket(encounterDocDetailsDtoNew.getBucketName())
   			                            .object(objectName)
   			                            .build());
   			            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
   			            return url;
   					   }
                       }
                    
                }catch(AmazonServiceException ae){
                    logger.error("updateRISPdfFile failed", ae);
                    throw new ServiceException(ae.getMessage());
                }catch(IOException ie){
                    logger.error("updateRISPdfFile failed", ie);
                    throw new ServiceException(ie.getMessage());
                }
                return null;
        }
	    @Transactional
		public String uploadCompleteConsultationOPD(InputStream inputStream, EncounterAssignmenMapperDto mpprDto) {
			   try{

				   String queryStr = "select bucket_flag from t_unit_application_billing_configuration " +
						   "where unit_id = :unitId and organization_id = :organizationId";
				   Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
				   query.setParameter("unitId", mpprDto.getUnitId());
				   query.setParameter("organizationId", mpprDto.getOrganizationId());
				   Integer bucketType = (Integer) query.uniqueResult();
				   if(bucketType == null) {
						bucketType=1;
					}
				   
				   Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(mpprDto.getOrganizationId());
					OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
					
					if(inputStream != null ){
						if(bucketType == 1) {

							OrganizationMasterDto awsCredDto = getAWSCreds();
							String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
							String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType("application/pdf");
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						String organization =  organizationMasterDto.getOrganizationFolder();
						
						Integer test =mpprDto.getEncounterId() != null ? mpprDto.getEncounterId() :mpprDto.getAdmissionId();
						String keyName = "";
						if (test != null) {
							keyName = mpprDto.getOrganizationId()+"_"+mpprDto.getUnitId()+"_"+test+ "_" + "complete_consultation.pdf";
						} else {
							keyName = mpprDto.getOrganizationId()+"_"+mpprDto.getUnitId()+ "_" + "complete_consultation.pdf";
						}
						s3Client.putObject(bucket + "/" + organization+CORPORATE_CONTRACT_DOCUMENT_BUCKET, keyName, inputStream, metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +organization+CORPORATE_CONTRACT_DOCUMENT_BUCKET, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +organization + CORPORATE_CONTRACT_DOCUMENT_BUCKET+"/" + keyName;
						}else {

							String queryStr1 = "select local_storage_bucket_json from t_unit_application_billing_configuration " +
									"where unit_id = :unitId and organization_id = :organizationId";
							Query query1 = sessionFactory.getCurrentSession().createSQLQuery(queryStr1);
							query1.setParameter("unitId", mpprDto.getUnitId());
							query1.setParameter("organizationId", mpprDto.getOrganizationId());
							String localBucketJson = (String) query1.uniqueResult();
							Gson gson = new Gson();
							   EncounterDocDetailsDto encounterDocDetailsDtoNew = gson.fromJson(localBucketJson, EncounterDocDetailsDto.class);
			  
						   MinioClient minioClient = new MinioClient.Builder()
					                .endpoint(encounterDocDetailsDtoNew.getEndpoint()) 
					                .credentials(encounterDocDetailsDtoNew.getAccessKey(), encounterDocDetailsDtoNew.getSecretKey())
					                .build();
						   			   			   
						   System.out.println("MINIO Connectionnn is Createdddddd........");
				            Map<String, String> headers = new HashMap<>();
				            headers.put("X-Amz-Meta-Description", "This is a test file.");

				            String objectName = "complete_consultation";
				            String contentType ="application/pdf";

				            // Get InputStream from MultipartFile
				            InputStream inputStreama = inputStream;
			 
				            minioClient.putObject(PutObjectArgs.builder()
				                    .bucket(encounterDocDetailsDtoNew.getBucketName())
				                    .object(objectName)
				                    .contentType(contentType)
				                    .stream(inputStreama, -1, 10485760) // -1 for unknown object size, 10MB buffer size
				                    .build());
				                           
				            System.out.println("Multipart file uploaded successfully.");
				            String url = minioClient.getPresignedObjectUrl(
				                    GetPresignedObjectUrlArgs.builder()
				                            .method(Method.GET)
				                            .bucket(encounterDocDetailsDtoNew.getBucketName())
				                            .object(objectName)
				                            .build());
				            System.out.println("MY MINIO URL ISSSSSS.....: " + url);	            
				            return url;
						   }
					}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch(IOException ie){
				logger.error("uploadCompleteConsultationOPD failed", ie);
				throw new ServiceException(ie.getMessage());
			}
		   catch (Exception e) {
			   logger.error("uploadCompleteConsultationOPD failed", e);
			   throw new ServiceException(e.getMessage());
		   }
				return null;
		    }

	    @Transactional
		public String createS3Folder(String unitName, String organizationName, String title) {

	    	
	    	 // Replace with your AWS credentials and bucket details

			OrganizationMasterDto awsCredDto = getAWSCreds();
			String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
			String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";
	    	
	        String accessKey = ACCESS_KEY;
	        String secretKey = SECRET_KEY;
	        String region = "ap-south-1"; // Example region
	        String bucketName = BUCKET_NAME;
	        String folderName = unitName+"/"+organizationName+"/"+title; // Folder name with trailing slash

	        // Create credentials object
	        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

	        // Build an S3 client with explicit credentials
	        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	                .withRegion(region)
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .build();

	        try {
	            // Create an empty object to represent the folder
	            s3Client.putObject(new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), null));
	            System.out.println("Folder created successfully.");
	        } catch (Exception e) {
	            logger.error("createS3Folder failed", e);
	            throw new ServiceException(e.getMessage());
	        }
			return folderName;
	    
			
		}

		public String updateNABHTemplate(ByteArrayOutputStream outputStream, String randam,
				String organizationName, String unitName, String title) {

			   try{
				   
					if(outputStream != null ){
						OrganizationMasterDto awsCredDto = getAWSCreds();
						String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
						String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";
						InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
						AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
						AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentType("application/pdf");
						//write a file on s3 server
						String bucket = BUCKET_NAME; 
						
						String keyName = randam+"NABH.pdf";
						
						s3Client.putObject(bucket + "/" + unitName+"/"+organizationName+"/"+title, keyName, inputStream, metadata);
						//Setting Public Permissions to file
						s3Client.setObjectAcl(bucket + "/" +unitName+"/"+organizationName+"/"+title, keyName, CannedAccessControlList.PublicRead);
						return AWS_BUCKET_COMMON_PATH + bucket+ "/" +unitName+"/"+organizationName+"/"+title+"/" + keyName;
						}
				}catch(AmazonServiceException ae){
					logger.error("Error Message:    " + ae.getMessage() + "\nHTTP Status Code: " + ae.getStatusCode()+"\nAWS Error Code:   " + ae.getErrorCode()+"\nError Type:       " + ae.getErrorType()+"Request ID:       " + ae.getRequestId());
			}catch (Exception e) {
				logger.error("updateNABHTemplate failed", e);
				throw new ServiceException(e.getMessage());
			}
				return null;
		    
		}

	
}
