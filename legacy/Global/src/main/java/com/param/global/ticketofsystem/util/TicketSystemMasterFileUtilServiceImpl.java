package com.param.global.ticketofsystem.util;
import in.param.exception.ApplicationException;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import com.param.global.org.dto.OrganizationMasterDto;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.param.global.common.Response;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.ticketofsystem.doa.TicketSystemMasterFileDaoImpl;
import com.param.global.ticketofsystem.dto.TicketSystemMasterFilesDto;
import com.param.global.common.HISFileUploadsPath;
import com.param.global.common.ServiceException;
import static com.param.global.common.ICommonConstants.*;

import org.apache.log4j.Logger;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class TicketSystemMasterFileUtilServiceImpl implements ITicketSystemMasterFileUtilService {

	private static final Logger logger = Logger.getLogger(TicketSystemMasterFileUtilServiceImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response uploadTicketSystemMasterFile(MultipartFile[] files,
			TicketSystemMasterFilesDto ticketSystemMasterFilesDto,UnitMasterDto unitMasterDto) {
		try { 
			 
				 List<String> uploadedFileUri = new LinkedList<String>();
				 for (MultipartFile multipartFile : files) {
					String filePath = filePathAndSetPermission(multipartFile, ticketSystemMasterFilesDto, unitMasterDto);
					uploadedFileUri.add(filePath);
				 }
				 return new Response<>(SUCCESS, SUCCESS_CODE, null, uploadedFileUri, null);
			 
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Transactional
	public String filePathAndSetPermission(MultipartFile file, TicketSystemMasterFilesDto ticketSystemMasterFilesDto, UnitMasterDto unitMasterDto ) throws Exception {
		
		try {
			//AWS Credential Details

			String queryAWSCred = "select access_key as \"awsAccessKey\", secret_key as \"awsSecretKey\" from t_aws_credentials " +
					" where status = 'A' order by aws_key_id desc limit 1";
			OrganizationMasterDto awsCredDto = (OrganizationMasterDto) sessionFactory.getCurrentSession().createSQLQuery(queryAWSCred)
					.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();

			String ACCESS_KEY = awsCredDto != null && awsCredDto.getAwsAccessKey() != null ? awsCredDto.getAwsAccessKey() :"";
			String SECRET_KEY = awsCredDto != null && awsCredDto.getAwsSecretKey() != null ? awsCredDto.getAwsSecretKey() :"";

//			AWSCredentials awsCredentials = new BasicAWSCredentials(HISFileUploadsPath.ACCESS_KEY, HISFileUploadsPath.SECRET_KEY);
			AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentType(file.getContentType());
	        
	        //write a file on s3 server
	        String bucket = HISFileUploadsPath.BUCKET_NAME; 
			String organizationUnit =  unitMasterDto.getUnitFolder();
			String keyName = unitMasterDto.getOrganizationId()+"_"+unitMasterDto.getUnitId()+"_"+ticketSystemMasterFilesDto.getTicketMasterId() + "_" + file.getOriginalFilename();
			s3Client.putObject(bucket + "/" + organizationUnit+HISFileUploadsPath.TICKET_SYSTEM_MASTER_FILE, keyName, file.getInputStream(), metadata);
	        
	        //Setting Public Permissions to file
			s3Client.setObjectAcl(bucket + "/" +organizationUnit+HISFileUploadsPath.TICKET_SYSTEM_MASTER_FILE, keyName, CannedAccessControlList.PublicRead);
			return HISFileUploadsPath.AWS_BUCKET_COMMON_PATH + bucket+ "/" +organizationUnit + HISFileUploadsPath.TICKET_SYSTEM_MASTER_FILE+"/" + keyName;
			
		} catch (Exception e)//	Add Proper exception
		{
			String exceptionErrorMsg = TicketSystemMasterFileDaoImpl.class.getName() + " : " + e.getMessage();
			throw e;
		}
		
	}

	
}
