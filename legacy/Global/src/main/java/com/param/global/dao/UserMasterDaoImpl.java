package com.param.global.dao;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.global.utility.UserComponent;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.master.global.model.UserLoginLog;
import com.param.adt.master.global.model.UserMaster;
import com.param.duoAuthentication.dto.DuoApplicationPropertiesDto;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.LdapConfigurationMasterDto;
import com.param.global.dto.MailDto;
import com.param.global.dto.OrganizationUnitUserMapperDto;
import com.param.global.dto.UserDetailsDto;
import com.param.global.dto.UserLoginLogDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.mail.MailingServiceImpl;
import com.param.global.model.UnitSubscriptionDetails;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.unit.service.IwhatsAppServiceImpl;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class UserMasterDaoImpl extends GenericDao<UserMaster, Integer> implements IUserMasterDao, ICommonConstants {
    public UserMasterDaoImpl() {
        super(UserMaster.class);
    }

    @Autowired
    private SessionFactory SessionFactory;

    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private IwhatsAppServiceImpl iwhatsAppServiceImpl;

    final static Logger logger = Logger.getLogger(UserMasterDaoImpl.class);

    @Override
    public Response getOrganizationList(String url) throws ApplicationException {
        try {


            StringBuilder query = new StringBuilder(
                    "SELECT mom.organization_id as \"organizationId\" ," +
                            "mom.organization_name as \"organizationName\" ," +
                            "mom.organization_logo as \"organizationLogo\" ," +
                            "mom.is_duo_authentication_required as \"isDuoAuthenticationRequired\" " +
                            "FROM m_organization_master mom " +
                            " where mom.status  = 'A'  "
            );


            if (url.equals("localhost")) {
                query.append(" ORDER BY mom.organization_name ");
            } else {
                query.append("and lower(mom.app_url )=:url ");
            }

           Query createQuery = SessionFactory.getCurrentSession()
                    .createSQLQuery(query.toString());
            if (url.equals("localhost")) {
            } else {
                createQuery.setParameter("url",url.toLowerCase());
            }
            List<OrganizationMasterDto> hospitalMasterDtoList =createQuery.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).list();


            List<Integer> orgIds = new ArrayList<Integer>();
            for(OrganizationMasterDto dto : hospitalMasterDtoList){
                orgIds.add(dto.getOrganizationId());
            }

            StringBuilder query1 = new StringBuilder(
                    "SELECT df.organization_id as \"organizationId\" ," +
                            "df.screen_image_id as \"screenImageId\" ," +
                            "df.image_url as \"imageURL\" ," +
                            "df.image_type_id as \"imageTypeId\" ," +
                            "df.logo_name as \"logoName\" " +
                            "FROM  m_different_types_screen_image df " +
                            "where  df.status  = 'A' and df.organization_id in("+orgIds.toString().replaceAll("\\[", "").replaceAll("\\]", "") +") "
            );

            List<OrganizationMasterDto> hospitalMasterDtoList1 = SessionFactory.getCurrentSession().createSQLQuery(query1.toString()).setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).list();

            for(OrganizationMasterDto dto : hospitalMasterDtoList){

                List<OrganizationMasterDto> filteredList = hospitalMasterDtoList1
                        .stream()
                        .filter(ageGrp -> (ageGrp.getOrganizationId().equals(dto.getOrganizationId())))
                        .collect(Collectors.toList());
                dto.setDifferentTypesScreenImageList(filteredList);
            }

            return new Response(SUCCESS, null, null, hospitalMasterDtoList, null);
        } catch (HibernateException he) {
            logger.error("getOrganizationList failed: " + he.getMessage(), he);
        } catch (Exception e) {
            logger.error("getOrganizationList failed: " + e.getMessage(), e);
        }
return new Response(ERROR, null, null, null, null);
    }

    @Override
    public Response getUnitFromOrganizationId(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
        try {
            List<UnitMaster> listUnitDepartmentMapperDto = SessionFactory.getCurrentSession().getNamedQuery("GET_UNIT_BY_ORGANIZATION_ID")
                    .setParameter("Organization_ID", organizationMasterDto.getOrganizationId())
                    .setResultTransformer(Transformers.aliasToBean(UnitMaster.class)).list();
            return new Response(SUCCESS, null, null, listUnitDepartmentMapperDto, null);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
            logger.error("getUnitFromOrganizationId failed: " + e.getMessage(), e);
        }
return new Response(ERROR, null, null, null, null);

    }


    @Override
    public Response validateUniqueUserName(UserMasterDto userMasterDto) throws ApplicationException {
        try {
            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession().getNamedQuery("USER_LOGIN")
                    .setParameter("userName", userMasterDto.getUserName().toLowerCase())
                    .setParameter("password", userMasterDto.getPassword().toLowerCase())
                    .setParameter("orgId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            return new Response(SUCCESS, null, null, null, listUserMasterDto);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
            logger.error("validateUniqueUserName failed: " + e.getMessage(), e);
        }
return new Response(ERROR, null, null, null, null);
    }


    @Override
    public Response userLogin(UserMasterDto userMasterDto) throws ApplicationException {
        try {

            String encryptedPassword = GlobalCommonDateUtils.encrypt(userMasterDto.getPassword().trim());

            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession()
                    .getNamedQuery("USER_LOGIN")
                    .setParameter("userName", userMasterDto.getUserName().trim())
                    .setParameter("orgId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            if (listUserMasterDto != null) {
                return new Response(SUCCESS, null, null, null, listUserMasterDto);
            } else {
                return new Response(ERROR, null, null, null, null);
            }

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
            logger.error("userLogin failed: " + e.getMessage(), e);
        }
return new Response(ERROR, null, null, null, null);
    }


    @Override
    public Response getHospitalUnitUserForLogin(OrganizationUnitUserMapperDto organizationUnitUserMapperDto) throws ApplicationException {
        try {
            List<OrganizationUnitUserMapperDto> listOrganizationUnitUserMapperDto = SessionFactory.getCurrentSession().getNamedQuery("GET_HOSPITAL_UNIT_USER_MAPPER_FOR_LOGIN")
                    .setParameter("organizationId", organizationUnitUserMapperDto.getOrganizationlId())
                    .setParameter("userId", organizationUnitUserMapperDto.getUserId())
                    .setParameter("unitId", organizationUnitUserMapperDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(OrganizationUnitUserMapperDto.class))
                    .list();

            if (listOrganizationUnitUserMapperDto != null) {
                return new Response(SUCCESS, null, null, listOrganizationUnitUserMapperDto, null);
            }
        } catch (HibernateException he) {
            logger.error("getHospitalUnitUserForLogin failed: " + he.getMessage(), he);
        } catch (Exception e) {
            logger.error("getHospitalUnitUserForLogin failed: " + e.getMessage(), e);
        }
        return new Response(ERROR, null, null, null, null);
    }


    @Override
    public Response saveUserMaster(UserMasterDto userMasterDto) throws ApplicationException {
        try {
            UserMaster userMaster = mapper.map(userMasterDto, UserMaster.class, "UserMasterDto_to_UserMaster");
            userMaster.setCreatedDate(TimeZoneComponent.getDateByzone(userMasterDto.getUnitId()));
            userMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(userMasterDto.getUnitId()));
            userMaster = save(userMaster);

            Integer userId = userMaster.getUserId();
            userMasterDto.setUserId(userId);
            return new Response(SUCCESS, null, null, null, userId);

        } catch (HibernateException he) {
            logger.error("saveUserMaster failed: " + he.getMessage(), he);
        } catch (Exception e) {
            logger.error("saveUserMaster failed: " + e.getMessage(), e);
        }
return new Response(ERROR, null, null, null, null);
    }


    @Override
    public Response saveUserMaster(UserMaster userMaster) throws ApplicationException {
        try {
            userMaster = save(userMaster);
            Integer userId = userMaster.getUserId();
            return new Response(SUCCESS, null, null, null, userId);

        } catch (HibernateException he) {
            logger.error("saveUserMaster failed: " + he.getMessage(), he);
        } catch (Exception e) {
            logger.error("saveUserMaster failed: " + e.getMessage(), e);
        }
return new Response(ERROR, null, null, null, null);
    }


    @Override
    public Response getUserRoleByUserId(int userId, int orgId, int orgUnitId) throws ApplicationException {
        try {
            List<UserRoleMasterDto> userRoleIds = SessionFactory.getCurrentSession().getNamedQuery("GET_ROLE_BY_USER_ID")
                    .setParameter("orgId", orgId)
                    .setParameter("unitId", orgUnitId)
                    .setParameter("userId", userId)
                    .setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class))
                    .list();
            return new Response(SUCCESS, null, null, userRoleIds, null);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("getUserRoleByUserId failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }


    @Override
    public Response getUsersByName(UserMasterDto userMasterDto) throws ApplicationException {
        try {
            List<UserMasterDto> listUserMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_USERS_BY_NAME")
                    .setParameter("organizationId", userMasterDto.getOrganizationId())
                    .setParameter("userName", "%" + userMasterDto.getUserName().toLowerCase().trim() + "%")
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUserMasterDto, null);
        } catch (HibernateException he) {
        logger.error("getUsersByName failed", he);
        } catch (Exception e) {
        logger.error("getUsersByName failed", e);
        }
return null;
    }


    @Override
    public Response checkEmailAvailability(UserMasterDto userMasterDto) {
        Response mailingResponse = null;
        try {


            List<Integer> userIds = sessionFactory.getCurrentSession().getNamedQuery("EMAIL_EXIST_COUNT")
                    .setParameter("email", userMasterDto.getEmailId().toLowerCase().trim())
                    .list();
            if (userIds != null && userIds.size() > 0) {

                UserMasterDto userDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery("Select user_mst.otp as \"otp\"," +
                                "  to_char(user_mst.otp_expiry_date,'YYYY-MM-dd HH:MI:SS')  AS \"otpExpiratyDate\"" +
                                "  ,otp_time_stamp as \"otpTimeStamp\",otp_count as \"otpCount\", blocked as \"blocked\" from public.m_user_master user_mst where user_id = :userId")
//					.setInteger("userId", userMasterDto.getUserId())
                        .setParameter("userId", userIds.getFirst())
                        .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();

                boolean isUserBlocked= checkOtpValidity(userDto,false);
                updateBlockStatus(userDto,userIds,isUserBlocked);
                if(!isUserBlocked)  {
                    return new Response(ERROR, null, OTP_LIMIT_EXHAUST, null, false);
                }

                boolean isOtpValidated= checkOtpValidity(userDto,true);
                if(!isOtpValidated)  return new Response(ERROR, null, WAIT_FOR_THREE_MINUEST, null, false);
//				 if(userMasterDto.getUserId()!=null) {

                char[] otp = GlobalCommonDateUtils.generateOTP(4);
                String otpResult = String.valueOf(otp);
                MailingServiceImpl mailingObject = new MailingServiceImpl();
                Calendar date = Calendar.getInstance();
                long time = date.getTimeInMillis();
                Date afterAddingTwoMins = new Date(time + 120000);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDt = formatter.format(afterAddingTwoMins);
                mailingResponse = iwhatsAppServiceImpl.sendOTPOverMail(userMasterDto.getEmailId().toLowerCase().trim(), otpResult);
                sessionFactory.getCurrentSession().createSQLQuery("Update public.m_user_master set otp = '" + otpResult + "', otp_expiry_date = '" + formattedDt + "' "
                                + " ,otp_time_stamp=:otpTimeStamp where user_id  IN (:userIDS) ")
                        .setParameterList("userIDS", userIds)
                        .setTimestamp("otpTimeStamp", userDto.getOtpTimeStamp()!= null ? userDto.getOtpTimeStamp() : Timestamp.from(Instant.now()))
                        .executeUpdate();
                return new Response(SUCCESS, SUCCESS_CODE, EMAIL_SENT, null, null);
//					 }
            } else {
                return new Response(ERROR, ERROR_CODE, EMAIL_ID_NOT_EXIST, null, null);
            }
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("checkEmailAvailability failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    private void updateBlockStatus(UserMasterDto userDto, List<Integer> userIds,boolean isUserBlocked) {
        if (!isUserBlocked) {
            sessionFactory.getCurrentSession().createSQLQuery("Update public.m_user_master set blocked=TRUE where user_id  IN (:userIDS) ")
                    .setParameterList("userIDS", userIds)
                    .executeUpdate();
        } else {
            if (userDto.isBlocked()) {
                userDto.setOtpTimeStamp(null);
                sessionFactory.getCurrentSession().createSQLQuery("Update public.m_user_master set blocked=FALSE,otp_count=0,otp_time_stamp=null where user_id  IN (:userIDS) ")
                        .setParameterList("userIDS", userIds)
                        .executeUpdate();
            }
        }
    }


    @Override
    public Response otpVerification(UserMasterDto userMasterDto) {

        try {


            List<Integer> userIds = sessionFactory.getCurrentSession().getNamedQuery("EMAIL_EXIST_COUNT")
                    .setParameter("email", userMasterDto.getEmailId().toLowerCase().trim())
                    .list();
            if (userIds != null && userIds.size() > 0) {


                UserMasterDto userDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery("Select user_mst.otp as \"otp\"," +
                                "  to_char(user_mst.otp_expiry_date,'YYYY-MM-dd HH:MI:SS')  AS \"otpExpiratyDate\"" +
                                "  ,otp_time_stamp as \"otpTimeStamp\",otp_count as \"otpCount\",blocked as \"blocked\" from public.m_user_master user_mst where user_id = :userId")
//					.setInteger("userId", userMasterDto.getUserId())
                        .setParameter("userId", userIds.getFirst())

                        .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();

                if (userMasterDto.getOtp().trim().equals(userDto.getOtp())) {
                    SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String currentDate = sm.format(TimeZoneComponent.getDateByzone(userMasterDto.getUnitId()));
                    Date checkDate = GlobalCommonDateUtils.getDate(currentDate, "yyyy-M-dd HH:mm:ss");
                    Date otpDate = GlobalCommonDateUtils.getDate(userDto.getOtpExpiratyDate(), "yyyy-M-dd HH:mm:ss");

                    //if(otpDate.after(checkDate))
                    //{

                    sessionFactory.getCurrentSession().createSQLQuery("Update public.m_user_master set otp_count ="+0+", otp_time_stamp =:otpCurrentTimeStamp"
                                    + " where user_id  IN (:userIDS) ")
                            .setParameterList("userIDS", userIds)
                            .setTimestamp("otpCurrentTimeStamp", userMasterDto.getOtpTimeStamp())
                            .executeUpdate();
                    return new Response(SUCCESS, userIds.getFirst() + "", OTP_VERIFIED, null, true);
                    //}
                    //return new Response(OTP_EXPIRED, null, null, null,false);
                }else{
                    boolean isUserBlocked= checkOtpValidity(userDto,false);
                    Instant currentTimestamp = Instant.now();
                    Timestamp otpCurrentTimeStamp = Timestamp.from(currentTimestamp);

                    updateBlockStatus(userDto,userIds,isUserBlocked);

                    if(!isUserBlocked)  {
                        return new Response(ERROR, null, OTP_LIMIT_EXHAUST, null, false);
                    }

                    Integer otpCountFromDb = userDto.getOtpCount();
                    otpCountFromDb = otpCountFromDb+1;

                    sessionFactory.getCurrentSession().createSQLQuery("Update public.m_user_master set otp_count ="+otpCountFromDb+", otp_time_stamp =:otpCurrentTimeStamp"
                                    + " where user_id  IN (:userIDS) ")
                            .setParameterList("userIDS", userIds)
                            .setTimestamp("otpCurrentTimeStamp",otpCurrentTimeStamp)
                            .executeUpdate();

                    return new Response(ERROR, null, INVALID_OTP, null, false);
                }
            }
            return new Response(ERROR, null, EMAIL_NOT_MAPPED_TO_RECEIVED_OTP, null, false);



        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("otpVerification failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    private boolean checkOtpValidity(UserMasterDto userMasterDto,boolean otpTimerCheck) {
        try {
            if(otpTimerCheck){
                long minutesDifference = 0L;
                Timestamp dbTimestamp = userMasterDto.getOtpTimeStamp();
                if(dbTimestamp== null) return  true;
                Instant currentTimestamp = Instant.now();
                long differenceInMillis = currentTimestamp.toEpochMilli() - dbTimestamp.getTime();
                minutesDifference = Math.abs(differenceInMillis / (1000 * 60));
                if( minutesDifference >= 3){
                    userMasterDto.setOtpTimeStamp(Timestamp.from(currentTimestamp));
                    return true;
                }
                else{
                    return false;
                }
            }

            long hoursDifference = 0L;

                Timestamp dbTimestamp = userMasterDto.getOtpTimeStamp();
                if(dbTimestamp== null) return  true;
                Instant currentTimestamp = Instant.now();
                long differenceInMillis = currentTimestamp.toEpochMilli() - dbTimestamp.getTime();
                hoursDifference = Math.abs(differenceInMillis / (1000 * 60 * 60));

                if (userMasterDto.getOtpCount() <= 4) {
                    return true;
                } else if (userMasterDto.getOtpCount() >= 5 && hoursDifference >= 24) {
                    userMasterDto.setOtpCount(0);
                    return true;
                } else if (userMasterDto.getOtpCount() >= 5 && hoursDifference < 24) {
                    return false;
                }
        }catch(Exception e){
        logger.error("checkOtpValidity failed", e);
        }
        return false;
    }

    @Override
    public Response forgotPasswordGeneration(UserMasterDto userMasterDto) {
        try {
            String encryptedPwd = GlobalCommonDateUtils.encrypt(userMasterDto.getPassword());
            Calendar c = Calendar.getInstance();
            c.setTime(TimeZoneComponent.getDateByzone(userMasterDto.getUnitId()));
            c.add(Calendar.YEAR, 1);
            Date expDate = c.getTime();
            String passwordExpiry = GlobalCommonDateUtils.getStringDate(expDate, "dd-MM-yyyy HH:mm:ss");

            sessionFactory.getCurrentSession().createSQLQuery("Update public.m_user_master set password = '" + encryptedPwd + "', password_expiry_date = '" + passwordExpiry + "' where user_id = '" + userMasterDto.getUserId() + "'")
                    .executeUpdate();
            return new Response(SUCCESS, SUCCESS_CODE, PASSWORD_UPDATED, null, null);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("forgotPasswordGeneration failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    @Override
    public Response resetPassword(UserMasterDto userMasterDto) {
        try {
            UserMasterDto userDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery("Select user_mst.password as \"password\""
                            + " from public.m_user_master user_mst where user_id = :userId")
                    .setParameter("userId", userMasterDto.getUserId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            String decryptPwd = GlobalCommonDateUtils.decrypt(userDto.getPassword());
            //System.out.println(decryptPwd);
            if (decryptPwd.equals(userMasterDto.getExistingPassword())) {
                String encryptedPwd = GlobalCommonDateUtils.encrypt(userMasterDto.getUserPwd());


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String stringDate = formatter.format(TimeZoneComponent.getDateByzone(userMasterDto.getUnitId()));
                Date futureDate = DateUtils.addDays(formatter.parse(stringDate), 365);
                String expDate = GlobalCommonDateUtils.getStringDate(futureDate, "dd-MM-yyyy");
                sessionFactory.getCurrentSession().createSQLQuery("Update public.m_user_master set password = '" + encryptedPwd + "', password_expiry_date = '" + expDate + " 00:00:00" + "' where user_id = '" + userMasterDto.getUserId() + "'")
                        .executeUpdate();
                return new Response(SUCCESS, SUCCESS_CODE, PASSWORD_UPDATED, null, null);
            }else {
            	return new Response(ERROR, null, "Existing password is incorrect.", null, null);
            }


        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("resetPassword failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }


    @Override
    public Response resendUserName(UserMasterDto userMasterDto) {
        try {
            if (userMasterDto.getEmailId() != null) {
                MailingServiceImpl mailingServiceObj = new MailingServiceImpl();
                mailingServiceObj.resendUserName(userMasterDto.getEmailId(), userMasterDto.getUserName());
                return new Response(SUCCESS, SUCCESS_CODE, EMAIL_SENT, null, null);
            } else {
                return new Response(EMAIL_ID_NOT_EXIST, null, null, null, null);
            }
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("resendUserName failed", e);
        }
return new Response(ERROR, null, null, null, null);

    }

    @Override
    public Response resendUserNameAndPassword(UserMasterDto userMasterDto) {
        try {
            if (userMasterDto.getEmailId() != null) {
                String decryptedPwd = GlobalCommonDateUtils.decrypt(userMasterDto.getPassword());
                MailingServiceImpl mailingServiceObj = new MailingServiceImpl();
                mailingServiceObj.resendUserNameAndPassword(userMasterDto.getEmailId(), userMasterDto.getUserName(), userMasterDto.getPassword());
                return new Response(SUCCESS, SUCCESS_CODE, EMAIL_SENT, null, null);
            } else {
                return new Response(EMAIL_ID_NOT_EXIST, null, null, null, null);
            }
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("resendUserNameAndPassword failed", e);
        }
return new Response(ERROR, null, null, null, null);

    }


    @Override
    public Response getUnitName(UserMasterDto userMasterDto) {

        try {
            List<UserMasterDto> unitList = sessionFactory.getCurrentSession().getNamedQuery("GET_UNIT_BY_USER_EMAIL")
                    .setParameter("email", userMasterDto.getEmailId().trim())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, unitList, null);
        } catch (Exception e) {

            throw e;
        }

    }

    @Override
    public Response getPass() {
        try {
            List<UserMasterDto> unitList = sessionFactory.getCurrentSession().createSQLQuery("select user_id as \"userId\", "
                            + "user_name as \"userName\",password as \"password\" "
                            + "from public.m_user_master order by 1 asc")
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
            return null;
        } catch (Exception e) {

            throw e;
        }
    }

    @Override
    public Response getUserNameById(UserMasterDto userMasterDto) {
        try {
            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession()
                    .getNamedQuery("GET_USERNAME_BY_USERID")
                    .setParameter("userId", userMasterDto.getUserId())
                    .setParameter("organizationId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            return new Response(SUCCESS, SUCCESS_CODE, null, null, listUserMasterDto.getUserName());
        } catch (Exception e) {

            throw e;
        }

    }

    @Override
    public List<SmsUtilityConfigInfoDto> getAdminDetaisForSmsUtility(Integer unitId) {
        try {
            String query = "select "
                    + " 	emp.mobile_number as \"mobileNo\","
                    + " emp.email as \"toEmailId\"   "
                    + " from "
                    + " 	m_employee_master emp "
                    + " where "
                    + " 	emp.user_id in ( "
                    + " 	select "
                    + " 		urm.user_id "
                    + " 	from "
                    + " 		t_user_role_mapper urm  "
                    + " 	where "
                    + " 		urm.role_id =( "
                    + " 		select "
                    + " 			admin_role_id "
                    + " 		from "
                    + " 			t_unit_application_billing_configuration "
                    + " 		where "
                    + " 			unit_id =:unitId "
                    + " 			and status = 'A') "
                    + " 		and urm.unit_id =:unitId "
                    + "		and urm.status='A') "
                    + " union "
                    + " select "
                    + " 	doc.mobile_number as \"mobileNo\","
                    + " doc.email as \"toEmailId\" "
                    + " from "
                    + " 	doctor.m_doctor_master doc "
                    + " where "
                    + " 	doc.user_id in ( "
                    + " 	select "
                    + " 		urm.user_id "
                    + " 	from "
                    + " 	t_user_role_mapper urm "
                    + " 	where "
                    + " 		urm.role_id =( "
                    + " 		select "
                    + " 			admin_role_id "
                    + " 		from "
                    + " 			t_unit_application_billing_configuration "
                    + " 		where "
                    + " 			unit_id =:unitId "
                    + " 			and status = 'A') "
                    + " 		and urm.unit_id =:unitId "
                    + "		and urm.status='A') ";


            List<SmsUtilityConfigInfoDto> adminsMobileNoList = SessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();

            return adminsMobileNoList;
        } catch (Exception e) {

            throw e;
        }
    }

    @Override
    public Response getPasswordAndUserName() throws ApplicationException {
        try {
            List<UserDetailsDto> unitList = sessionFactory.getCurrentSession()
                    .createSQLQuery("select userMaster.user_id as \"userId\", roleMstr.role_name as \"roleName\","
                            + " (case when empMstr.user_id is null then "
                            + " concat(coalesce(docMstr.first_name,''),' ' ,coalesce(docMstr.last_name))"
                            + " else concat(coalesce(empMstr.first_name,''),' ' ,coalesce(empMstr.last_name))end) as \"userName\","
                            + " userMaster.user_name as \"loginName\",userMaster.password as \"password\" "
                            + " from public.m_user_master userMaster "
                            + " left join doctor.m_doctor_master docMstr on userMaster.user_id=docMstr.user_id and docMstr.status='A' "
                            + " left join public.m_employee_master empMstr on userMaster.user_id=empMstr.user_id and empMstr.status='A' "
                            + " inner join public.t_user_role_mapper roleMppr on roleMppr.user_id=userMaster.user_id and roleMppr.status='A' "
                            + " inner join public.m_role_master roleMstr on roleMppr.role_id=roleMstr.role_id and roleMstr.status='A' ")
                    .setResultTransformer(Transformers.aliasToBean(UserDetailsDto.class)).list();

            List<UserDetailsDto> dtoList = new ArrayList<>();
            for (Iterator iterator = unitList.iterator(); iterator.hasNext(); ) {
                UserDetailsDto userMasterDto = (UserDetailsDto) iterator.next();
                if (userMasterDto.getLoginName() != null && userMasterDto.getPassword() != null) {
                    String decryptPwd = GlobalCommonDateUtils.decrypt(userMasterDto.getPassword().trim());
                    userMasterDto.setPassword(decryptPwd);
                    dtoList.add(userMasterDto);
                }

            }
            return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
        } catch (Exception e) {

            throw e;
        }
    }

    @Override
    public Response getUserlogDetails(UserLoginLogDto dto) throws ApplicationException {
        try {
            List<UserLoginLogDto> dtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_USER_DETAILS_LOG_BY_USER_ID")
                    .setParameter("orgId", dto.getOrganizationId())
                    .setParameter("unitId", dto.getUnitId())
                    .setParameter("userId", dto.getUserId())
//					.setParameter("macAddress", dto.getMacAddress())
                    .setResultTransformer(Transformers.aliasToBean(UserLoginLogDto.class)).list();
            return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, dtoList);
        } catch (HibernateException he) {
        logger.error("getUserlogDetails failed", he);
        } catch (Exception e) {
        logger.error("getUserlogDetails failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response saveUserLogDetails(UserLoginLogDto logDto) throws ApplicationException {
        try {
            UserLoginLog log = new UserLoginLog();
            log.setUserId(logDto.getUserId());
            log.setIsLogin(logDto.getIsLogin());
            log.setIsLogout(logDto.getIsLogout());
            log.setCreatedBy(logDto.getUserId());
            log.setUpdatedBy(logDto.getUserId());
            log.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(logDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
            log.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(logDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
            log.setLogTime(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(logDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
            log.setOrganizationId(logDto.getOrganizationId());
            log.setUnitId(logDto.getUnitId());
            log.setMacAddress(logDto.getMacAddress());
            log.setStatus(logDto.getStatus());
            sessionFactory.getCurrentSession().save(log);
            return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
        } catch (HibernateException he) {
        logger.error("saveUserLogDetails failed", he);
        } catch (Exception e) {
        logger.error("saveUserLogDetails failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response updateUserLog(UserLoginLogDto logDto) throws ApplicationException {
        try {
            sessionFactory.getCurrentSession().createQuery("UPDATE UserLoginLog  "
                            + " SET status=:status , isLogin=:isLogin ,isLogout=:isLogout "
                            + " WHERE logId=:logId")
                    .setParameter("logId", logDto.getLogId())
                    .setParameter("status", logDto.getStatus())
                    .setParameter("isLogin", logDto.getIsLogin())
                    .setParameter("isLogout", logDto.getIsLogout())
                    .executeUpdate();
            return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
        } catch (HibernateException he) {
        logger.error("updateUserLog failed", he);
        } catch (Exception e) {
        logger.error("updateUserLog failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response checkOtherLoginByMacAddress(UserLoginLogDto dto) throws ApplicationException {
        try {
            List<UserLoginLogDto> dtoList = null;
           if (dto.getMacAddress() != null) {
               dtoList = sessionFactory.getCurrentSession().getNamedQuery("CHECK_OTHER_LOGIN")
                       .setParameter("macAddress", dto.getMacAddress())
                       .setParameter("userId", dto.getUserId() != null ? dto.getUserId() : 0)
                       .setParameter("unitId", dto.getUnitId() != null ? dto.getUnitId() : 0)
                       .setParameter("orgId", dto.getOrganizationId() != null ? dto.getOrganizationId() : 0)
                       .setResultTransformer(Transformers.aliasToBean(UserLoginLogDto.class)).list();
               return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
           } else {
               return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
           }

        } catch (HibernateException he) {
        logger.error("checkOtherLoginByMacAddress failed", he);
        } catch (Exception e) {
        logger.error("checkOtherLoginByMacAddress failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response updatePreviousLogin(UserLoginLogDto dto) throws ApplicationException {
        try {
            sessionFactory.getCurrentSession().createQuery("UPDATE UserLoginLog  "
                            + " SET status='I'"
                            + " WHERE logId=:logId")
                    .setParameter("logId", dto.getLogId())
                    .executeUpdate();
            return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
        } catch (HibernateException he) {
        logger.error("updatePreviousLogin failed", he);
        } catch (Exception e) {
        logger.error("updatePreviousLogin failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response getUserlogDetailsByMacAddress(UserLoginLogDto dto) throws ApplicationException {
        try {
            List<UserLoginLogDto> dtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_USER_DETAILS_LOG_BY_MACADDRESS")
                    .setParameter("orgId", dto.getOrganizationId())
                    .setParameter("unitId", dto.getUnitId())
                    .setParameter("userId", dto.getUserId())
                    .setParameter("macAddress", dto.getMacAddress())
                    .setResultTransformer(Transformers.aliasToBean(UserLoginLogDto.class)).list();
            return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, dtoList);
        } catch (HibernateException he) {
        logger.error("getUserlogDetailsByMacAddress failed", he);
        } catch (Exception e) {
        logger.error("getUserlogDetailsByMacAddress failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response checkOtherLoginByUserId(UserLoginLogDto dto) throws ApplicationException {
        try {
            List<UserLoginLogDto> dtoList = sessionFactory.getCurrentSession().getNamedQuery("CHECK_OTHER_LOGIN_BY_USER_ID")
                    .setParameter("orgId", dto.getOrganizationId())
                    .setParameter("unitId", dto.getUnitId())
                    .setParameter("userId", dto.getUserId())
                    .setResultTransformer(Transformers.aliasToBean(UserLoginLogDto.class)).list();
            return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, dtoList);
        } catch (HibernateException he) {
        logger.error("checkOtherLoginByUserId failed", he);
        } catch (Exception e) {
        logger.error("checkOtherLoginByUserId failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response getUserListOfSchedulingForUpdate(List<Integer> unitIds) throws ApplicationException {
        try {
            List<UserLoginLogDto> dtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_USER_LIST_OF_SCHEDULING_TO_UPDATE")
                    .setParameterList("unitIds", unitIds)
                    .setResultTransformer(Transformers.aliasToBean(UserLoginLogDto.class)).list();
            return new Response(SUCCESS, SUCCESS_CODE, null, dtoList, null);
        } catch (HibernateException he) {
        logger.error("getUserListOfSchedulingForUpdate failed", he);
        } catch (Exception e) {
        logger.error("getUserListOfSchedulingForUpdate failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response updateSchedulingLog(List<Integer> logIds) throws ApplicationException {
        try {
            List<List<Integer>> partitionedList = new ArrayList<>();
            Integer totalSize = logIds.size();
            Integer batchSize = 500;

            for (int i = 0; i < totalSize; i += batchSize) {
                int end = Math.min(i + batchSize, totalSize);
                partitionedList.add(new ArrayList<>(logIds.subList(i, end)));
            }

            for(List<Integer> logIdsList : partitionedList) {
                sessionFactory.getCurrentSession().createSQLQuery("UPDATE t_user_login_log "
                                + " SET status='I'"
                                + " WHERE log_id IN (:logIds) ")
                        .setParameterList("logIds", logIdsList)
                        .executeUpdate();
            }
            return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
        } catch (HibernateException he) {
        logger.error("updateSchedulingLog failed", he);
        } catch (Exception e) {
        logger.error("updateSchedulingLog failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    @Override
    public Response getUsersByNameOnType(UserMasterDto userMasterDto) throws ApplicationException {
        try {
            List<UserMasterDto> listUserMasterDto = sessionFactory.getCurrentSession().getNamedQuery("SEARCH_ACTIVE_USERS_BY_NAME_ON_TYPE")
                    .setParameter("organizationId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUserMasterDto, null);
        } catch (HibernateException he) {
        logger.error("getUsersByNameOnType failed", he);
        } catch (Exception e) {
        logger.error("getUsersByNameOnType failed", e);
        }
return null;
    }

    @Override
    public Response getUnitByCredentials(OrganizationMasterDto organizationMasterDto) throws ApplicationException {
        try {
            String encryptedPassword = GlobalCommonDateUtils.encrypt(organizationMasterDto.getPassword().trim());

            String validateUser = " select "
                    + " 		user_id as \"userId\" "
                    + " 	from "
                    + " 		m_user_master userMst "
                    + " 	where "
                    + " 		lower(userMst.user_name)=lower(:userName) "
                    + " 		and lower(userMst.password) =lower(:password)"
                    + " 		and userMst.organization_id =:orgId "
                    + " 		and userMst.status = 'A' "
                    + " 	order by user_id desc limit 1 ";

            UserMasterDto userMasterDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery(validateUser)
                    .setParameter("orgId", organizationMasterDto.getOrganizationId())
                    .setParameter("userName", organizationMasterDto.getUserName().trim())
                    .setParameter("password", encryptedPassword)
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();

            if (userMasterDto != null && userMasterDto.getUserId() > 0) {
                List<UnitMasterDto> listUserMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_UNIT_LIST_BY_USER_ORG")
                        .setParameter("orgId", organizationMasterDto.getOrganizationId())
                        .setParameter("userName", organizationMasterDto.getUserName().trim())
                        .setParameter("password", encryptedPassword)
                        .setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
                if (listUserMasterDto.size() > 0)
                    return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUserMasterDto, null);
                else
                    return new Response<>(ERROR, ERROR_CODE, "No unit(s) mapped to the user with selected organization...!!", null, null);
            } else {
                return new Response<>(ERROR, ERROR_CODE, "Invalid Credentials...!!", null, null);
            }

        } catch (HibernateException he) {
        logger.error("getUnitByCredentials failed", he);
        } catch (Exception e) {
        logger.error("getUnitByCredentials failed", e);
        }
return null;
    }

    @Override
    public Integer getIsCiniUser(Integer orgId) throws ApplicationException {
        try {

            String cliniUser = " select "
                    + " 		user_id as \"userId\" "
                    + " 	from "
                    + " 		m_user_master userMst "
                    + " 	where "
                    + " 		userMst.organization_id =:orgId "
                    + " 		and userMst.status = 'A' "
                    + "		and userMst.is_super_user='Y' "
                    + " 	order by user_id desc limit 1 ";

            Integer userId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(cliniUser)
                    .setParameter("orgId", orgId)
                    .uniqueResult();

            if (userId != null && userId > 0)
                return userId;

            return 0;
        } catch (Exception e) {
        logger.error("getIsCiniUser failed", e);
        }
return 0;
    }

    @Override
    public Response getPharmacyUserRoleType(UserLoginLogDto dto) throws ApplicationException {
        try {
            List<UserLoginLogDto> list = SessionFactory.getCurrentSession().getNamedQuery("GET_IS_PHARMACY_ROLE")
                    .setParameter("organizationId", dto.getOrganizationId())
                    .setParameter("unitId", dto.getUnitId())
                    .setParameter("userId", dto.getUserId())
                    .setResultTransformer(Transformers.aliasToBean(UserLoginLogDto.class)).list();
            return new Response(SUCCESS, null, null, list, null);
        } catch (Exception e) {
        logger.error("getPharmacyUserRoleType failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    public Response getUnitExist(UserMasterDto userMasterDto) throws ApplicationException {
        try {

            String validateUser = " select "
                    + " 		id as \"id\", "
                    + " 		failed_count as \"failedCount\" "
                    + " 	from "
                    + " 		public.t_unit_subscription_check userMst"
                    + " 	where "
                    + " 		 userMst.organization_id =:orgId "
                    + " 		and userMst.unit_id =:unitId "
                    + " 	order by id desc limit 1 ";

            UserMasterDto resUserMasterDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery(validateUser)
                    .setParameter("orgId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();

            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, resUserMasterDto);

        } catch (HibernateException he) {
        logger.error("getUnitExist failed", he);
        } catch (Exception e) {
        logger.error("getUnitExist failed", e);
        }
return null;
    }

    @Override
    public Response getUnitExistCheckByDate(UserMasterDto userMasterDto) throws ApplicationException {
        try {

            String validateUser = " select "
                    + " 		id as \"id\", "
                    + " 		failed_count as \"failedCount\" "
                    + " 	from "
                    + " 		public.t_unit_subscription_check userMst"
                    + " 	where "
                    + " 		userMst.organization_id =:orgId "
                    + " 		and userMst.unit_id =:unitId "
                    + " 		and userMst.created_date = :createdDate "
                    + " 	order by id desc limit 1 ";

            UserMasterDto resUserMasterDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery(validateUser)
                    .setParameter("orgId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setDate("createdDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(userMasterDto.getUnitId()), "yyyy-M-dd"))
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();

            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, resUserMasterDto);

        } catch (HibernateException he) {
        logger.error("getUnitExistCheckByDate failed", he);
        } catch (Exception e) {
        logger.error("getUnitExistCheckByDate failed", e);
        }
return null;
    }

    @Override
    public Response updatePassword(UserMasterDto dto) throws ApplicationException {
        try {

            UserMaster userMaster = findById(dto.getUserId());

            if (dto.getPassword() != null) {
                String encryptedPed = GlobalCommonDateUtils.encrypt(dto.getPassword());
                userMaster.setPassword(encryptedPed);
            }

            if (dto.getPwdExpiry() != null) {
                String date = dto.getPwdExpiry() + " 00:00:00";
                userMaster.setPwdExpiry(date);
            }

            update(userMaster);

            Response userResponse =  this.getUserDataById(dto);
            UserMasterDto userResponseMasterDto = (UserMasterDto) userResponse.getObject();
            UserComponent.updateUserDetailsForAuth(userResponseMasterDto);

            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

        } catch (HibernateException he) {
            throw he;
        } catch (Exception e) {
            throw e;
        }
//        return null;

    }

    @Override
    public MailDto getMailDetails(Integer orgId, Integer unitId) throws ApplicationException {
        try {

            String query = " select "
                    + " 		applicationConfig.smtp_host as \"host\", "
                    + " 		applicationConfig.smtp_port as \"port\", "
                    + " 		applicationConfig.smtp_user_id as \"id\", "
                    + " 		applicationConfig.smtp_user_pwd as \"password\" "
                    + " 	from "
                    + " 		public.t_unit_application_billing_configuration applicationConfig "
                    + " 	where "
                    + " 		applicationConfig.organization_id =:orgId "
                    + " 		and applicationConfig.unit_id =:unitId "
                    + "		and applicationConfig.status='A' "
                    + " 	limit 1 ";

            MailDto mailDto = (MailDto) sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameter("orgId", orgId)
                    .setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(MailDto.class)).uniqueResult();

            return mailDto;

        } catch (HibernateException he) {
        logger.error("getMailDetails failed", he);
        } catch (Exception e) {
        logger.error("getMailDetails failed", e);
        }
return null;
    }

    @Override
    public ApplicationConfiguration getApplicationConfiguration(Integer organizationId, Integer unitId) {

        String hql = "SELECT applicationConfiguration FROM ApplicationConfiguration AS applicationConfiguration "
                + "WHERE applicationConfiguration.unit.unitId = :unitId AND applicationConfiguration.organization.organizationId = :orgId AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
        Query query = getSession().createQuery(hql);
        query.setParameter("unitId", unitId);
        query.setParameter("orgId", organizationId);

        return (ApplicationConfiguration) query.uniqueResult();

    }

    @Override
    public Response validateLoginForDoctorApp(UserMasterDto userMasterDto) throws ApplicationException {

        try {

            String encryptedPassword = GlobalCommonDateUtils.encrypt(userMasterDto.getPassword().trim());

            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession()
                    .getNamedQuery("USER_LOGIN_FOR_DOCTOR_APP")
                    .setParameter("userName", userMasterDto.getUserName().trim())
                    .setParameter("password", encryptedPassword)
                    .setParameter("orgId", userMasterDto.getOrganizationId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            if (listUserMasterDto != null) {
                return new Response(SUCCESS, null, null, null, listUserMasterDto);
            } else {
                return new Response(ERROR, null, null, null, null);
            }

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("validateLoginForDoctorApp failed", e);
        }
return new Response(ERROR, null, null, null, null);

    }

    @Override
    public Response updateLabPinUserMaster(UserMasterDto userMaDto) throws ApplicationException {
        try {

            UserMaster mstr = findById(userMaDto.getUserId());
            mstr.setLabPin(userMaDto.getLabPin());
            update(mstr);
            return new Response(SUCCESS, null, null, null, mstr);


        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("updateLabPinUserMaster failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    @Override
    public Response getUserDetailsByUserName(String username) throws ApplicationException {
        try {
            String query = "select " +
                    "	user_name as \"userName\"," +
                    "	organization_id as \"organizationId\"," +
                    "	unit_id as \"unitId\" ," +
                    "	password as \"password\" " +
                    " from " +
                    "	m_user_master " +
                    " where user_name like :username ";
            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameter("username","%"+username+"%")
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            if (listUserMasterDto != null) {
                return new Response(SUCCESS, null, null, null, listUserMasterDto);
            } else {
                return new Response(ERROR, null, null, null, null);
            }

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("getUserDetailsByUserName failed", e);
        }
return new Response(ERROR, null, null, null, null);

    }

    @Override
    public Response getLdapConfigurationData(DuoApplicationPropertiesDto duoApplicationPropertiesDto)
            throws ApplicationException {
        try {
            String query = "select "
                    + "	ld.ldap_configuration_id as \"ldapConfigurationId\","
                    + "	ld.initial_context_factory as \"initialContextFactory\","
                    + "	ld.security_authentication as \"securityAuthentication\","
                    + "	ld.organization_id as \"organizationId\","
                    + "	ld.unit_id as \"unitId\" ,"
                    + "	ld.security_credentials as \"securityCredentials\" ,"
                    + "	ld.provider_url as \"providerUrl\" ,"
                    + "	ld.security_principal as \"securityPrincipal\" "
                    + " from " + "	public.t_ldap_configuration ld "
                    + " where "
                    + " ld.organization_id =:organizationId "
                    + " and ld.unit_id =:unitId ";

            LdapConfigurationMasterDto listUserMasterDto = (LdapConfigurationMasterDto) SessionFactory
                    .getCurrentSession().createSQLQuery(query)
                    .setParameter("organizationId",duoApplicationPropertiesDto.getOrganizationId())
                    .setParameter("unitId",duoApplicationPropertiesDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(LdapConfigurationMasterDto.class)).uniqueResult();
            if (listUserMasterDto != null) {
                return new Response(SUCCESS, null, null, null, listUserMasterDto);
            } else {
                return new Response(ERROR, null, null, null, null);
            }

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("getLdapConfigurationData failed", e);
        }
return new Response(ERROR, null, null, null, null);

    }

    @Override
    public Response userLoginForDuoAuthentication(UserMasterDto userMasterDto) throws ApplicationException {

        try {
            String query = "SELECT 	distinct(userMst.userId) as userId, "
                    + "userMst.password as password,"
                    + "unitUser.unitId as unitId,"
                    + "userMst.organizationId as organizationId,"
                    + "to_char(userMst.createdDate,'DD-MM-YYYY') as createdDate,"
                    + "userMst.createdBy as createdBy,"
                    + "userMst.userName as userName, "
                    + "userMst.status as status, "
                    + "userMst.pwdExpiry as pwdExpiry, "
                    + "doc.doctorId as doctorId,doc.isRMO as isRMO, "
                    + " unitUser.isRenderedServiceCancelled as isRenderedServiceCancelled, "
                    + " unitUser.isUpdatePatientReg as isUpdatePatientReg, "
                    + " unitUser.isUpdateIpdChargesRemark as isUpdateIpdChargesRemark, "
                    + "employee.employeeId as employeeId, "
                    + "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
                    + "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
                    + "employee.firstName as firstName, "
                    + "concat('Dr. ',coalesce(doc.firstName)) as docFirstName, "
                    + "unitMaster.unitCountryId as unitCountryId, "
                    + "unitMaster.unitStateId as unitStateId, "
                    + "unitMaster.subscriptionDate as subscriptionDate, "
                    + "unitMaster.unitDistrictId as unitDistrictId, "
                    + "unitMaster.unitCityId as unitCityId, "
                    + "unitMaster.unitAreaId as unitAreaId, "
                    + "unitMaster.postCode as unitZipCode, "
                    + "userMst.isSuperUser as isSuperUser,unitUser.updateDischargeSummary as updateDischargeSummary "
                    + "FROM	UserMaster userMst " + "INNER JOIN userMst.unitUserMapper unitUser "
                    + "INNER JOIN unitUser.unitMaster unitMaster "
                    + "LEFT JOIN userMst.doctorMastersList doc "
                    + "LEFT JOIN userMst.employeeMastersList employee "
                    + "LEFT JOIN employee.prefixMaster prefix "
                    + "WHERE lower(userMst.userName)=lower(:userName) "
                    + " AND userMst.organizationId =:orgId"
                    + " AND userMst.status ='A'"
                    + " AND unitMaster.unitId =:unitId"
                    + " AND unitUser.status ='A' ";
            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession().createQuery(query)
                    .setParameter("userName", userMasterDto.getUserName().trim())
                    .setParameter("orgId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            if (listUserMasterDto != null) {
                return new Response(SUCCESS, null, null, null, listUserMasterDto);
            } else {
                return new Response(ERROR, null, null, null, null);
            }

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("userLoginForDuoAuthentication failed", e);
        }
return new Response(ERROR, null, null, null, null);

    }

    @Override
    public Response getUnitByCredentialsForDuoLogin(OrganizationMasterDto organizationMasterDto)
            throws ApplicationException {
        try {

            String validateUser = " select "
                    + " 		user_id as \"userId\" "
                    + " 	from "
                    + " 		m_user_master userMst "
                    + " 	where "
                    + " 		lower(userMst.user_name)=lower(:userName) "
                    + " 		and userMst.organization_id =:orgId "
                    + " 		and userMst.status = 'A' "
                    + " 	order by user_id desc limit 1 ";

            UserMasterDto userMasterDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery(validateUser)
                    .setParameter("orgId", organizationMasterDto.getOrganizationId())
                    .setParameter("userName", organizationMasterDto.getUserName().trim())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();

            if (userMasterDto != null && userMasterDto.getUserId() > 0) {
                String query = "select "
                        + " 	unit.unit_id as \"unitId\", "
                        + " 	unit.subscription_date as \"subscriptionDate\", "
                        + " 	subUnit.amount_paid as \"amountPaid\", "
                        + " 	userMst.is_super_user as \"isSuperUser\", "
                        + " 	unit.unit_name as \"unitName\" "
                        + " from "
                        + " 	t_user_unit_mapper mppr "
                        + " inner join m_unit_master unit on "
                        + " 	unit.unit_id = mppr.unit_id "
                        + " inner join m_user_master userMst on mppr.user_id = userMst.user_id "
                        + " left join public.m_unit_subscription_details subUnit on "
                        + " subUnit.unit_id = unit.unit_id and subUnit.status='A' "
                        + " where "
                        + " 	mppr.user_id = ( "
                        + " 	select "
                        + " 		user_id "
                        + " 	from "
                        + " 		m_user_master userMst "
                        + " 	where "
                        + " 		lower(userMst.user_name)=lower(:userName) "
                        + " 		and userMst.organization_id =:orgId "
                        + " 		and userMst.status = 'A' "
                        + " 	limit 1) "
                        + " 		and mppr.status = 'A' "
                        + "		and unit.status='A' "
                        + "order by unit.unit_id ";

                List<UnitMasterDto> listUserMasterDto = sessionFactory.getCurrentSession().createSQLQuery(query)
                        .setParameter("orgId", organizationMasterDto.getOrganizationId())
                        .setParameter("userName", organizationMasterDto.getUserName().trim())
                        .setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
                if (listUserMasterDto.size() > 0)
                    return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUserMasterDto, null);
                else
                    return new Response<>(ERROR, ERROR_CODE, "No unit(s) mapped to the user with selected organization...!!", null, null);
            } else {
                return new Response<>(ERROR, ERROR_CODE, "Invalid Credentials...!!", null, null);
            }

        } catch (HibernateException he) {
        logger.error("getUnitByCredentialsForDuoLogin failed", he);
        } catch (Exception e) {
        logger.error("getUnitByCredentialsForDuoLogin failed", e);
        }
return null;
    }

    @Override
    public Response getUserDataById(UserMasterDto userMasterDto) throws ApplicationException {
        try {
            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession().getNamedQuery("USER_DATA_BY_ID")
                    .setParameter("userId", userMasterDto.getUserId())
                    .setParameter("orgId", userMasterDto.getOrganizationId())
                    .setParameter("unitId", userMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            return new Response(SUCCESS, null, null, null, listUserMasterDto);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
            throw e;
        }
//        return new Response(ERROR, null, null, null, null);
    }

    @Override
    public List<UserMasterDto> getListOfUsers() throws ApplicationException {
        try {
            String query = "select "
                    + "	user_name as \"userName\","
                    + "	organization_id as \"organizationId\","
                    + "	unit_id as \"unitId\" ,"
                    + "	user_id as \"userId\", "
                    + "	password as \"password\" "
                    + " from	m_user_master "
                    + " where status='A' order by user_id ";
            List<UserMasterDto> listUserMasterDtoList = SessionFactory.getCurrentSession().createSQLQuery(query)
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
            if (listUserMasterDtoList != null) {
                return listUserMasterDtoList;
            } else {
                return null;
            }

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("getListOfUsers failed", e);
        }
return null;

    }

    @Override
    public Response getUserDataByIdNew(UserMasterDto userMasterDtos) throws ApplicationException {
        try {
            UserMasterDto listUserMasterDto = (UserMasterDto) SessionFactory.getCurrentSession().getNamedQuery("USER_DATA_BY_ID_NEW")
                    .setParameter("userId", userMasterDtos.getUserId())
                    .setParameter("orgId", userMasterDtos.getOrganizationId())
                    .setParameter("unitId", userMasterDtos.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class))
                    .setMaxResults(1).uniqueResult();
            return new Response(SUCCESS, null, null, null, listUserMasterDto);
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("getUserDataByIdNew failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    @Transactional
    public Response saveSubscriptionDetails(UserMasterDto userMasterDto) throws ApplicationException {
        try {
            Response userDto = getSubscriptionDetails(userMasterDto);
            List<UserMasterDto> userDtoList = (List<UserMasterDto>) userDto.getListObject();
            if (userDtoList.size() > 0) {

                if ("true".equals(userMasterDto.getAmountPaid())) {

                    boolean isTrueEntryAvailable = false;
                    boolean isFalseEntry = false;
                    for (UserMasterDto dto : userDtoList) {
                        if ("true".equals(dto.getAmountPaid())) {
                            isTrueEntryAvailable = true;
                        } else if ("false".equals(dto.getAmountPaid())) {

                            isFalseEntry = true;
                            Date currentDateTime = TimeZoneComponent.getDateByzone(userMasterDto.getUnitId());
                            int rowsAffected = sessionFactory.getCurrentSession().createSQLQuery(
                                            "UPDATE public.m_unit_subscription_details " +
                                                    "SET subscription_start_date = :subscriptionStartDate, " +
                                                    "subscription_end_date = :subscriptionEndDate, " +
                                                    "subscription_type = :subscriptionType, " +
                                                    "created_date = :createdDate, " +
                                                    "status = :status, " +
                                                    "amount_paid = :amountPaid " +
                                                    "WHERE unit_id = :unitId AND subscription_id = :subId")
                                    .setParameter("subscriptionStartDate", userMasterDto.getSubscriptionStartDate())
                                    .setParameter("subscriptionEndDate", userMasterDto.getSubscriptionEndDate())
                                    .setParameter("subscriptionType", userMasterDto.getSubscriptionType())
                                    .setParameter("createdDate", currentDateTime)
                                    .setParameter("status", 'A') // Use 'A' as a character, not a string.
                                    .setParameter("amountPaid", userMasterDto.getAmountPaid())
                                    .setParameter("unitId", dto.getUnitId())
                                    .setParameter("subId", dto.getSubscriptionId())
                                    .executeUpdate();
                            String inputDateString = userMasterDto.getSubscriptionEndDate().toString();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                            Date parsedDate = dateFormat.parse(inputDateString);

                            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            String formattedDateString = outputDateFormat.format(parsedDate);

                            sessionFactory.getCurrentSession()
                                    .createSQLQuery("UPDATE public.m_unit_master " +
                                            "SET subscription_date = :subscriptionDate " +
                                            "WHERE unit_id = :unitId")
                                    .setParameter("subscriptionDate", formattedDateString)
                                    .setParameter("unitId", userMasterDto.getUnitId())
                                    .executeUpdate();
                        }
                    }

                    if (isFalseEntry) {
                        System.out.println("False entry updated.................");

                    } else if (!isTrueEntryAvailable) {
                        return saveNewSubscription(userMasterDto);
                    } else {
                        for (UserMasterDto userDtoNew : userDtoList) {
                            if ("true".equals(userDtoNew.getAmountPaid()) && "A".equals(userDtoNew.getSubscriptionStatus() + "")) {
                                int subscriptionId = userDtoNew.getSubscriptionId();

                                int rowsUpdated = sessionFactory.getCurrentSession().createSQLQuery(
                                                "UPDATE public.m_unit_subscription_details SET status = 'I' WHERE subscription_id = :subscriptionId")
                                        .setParameter("subscriptionId", subscriptionId).executeUpdate();

                                System.out.println("Rows updated: " + rowsUpdated);
                            }
                        }
                        return saveNewSubscription(userMasterDto);
                    }

                } else if ("false".equals(userMasterDto.getAmountPaid())) {

                    boolean isFalseEntryAvailable = false;
                    for (UserMasterDto dto : userDtoList) {
                        if ("false".equals(dto.getAmountPaid())) {
                            isFalseEntryAvailable = true;
                            break;
                        }
                    }

                    if (!isFalseEntryAvailable) {

                        for (UserMasterDto userDtoNew : userDtoList) {
                            if ("A".equals(userDtoNew.getSubscriptionStatus() + "")) {
                                int subscriptionId = userDtoNew.getSubscriptionId();

                                int rowsUpdated = sessionFactory.getCurrentSession().createSQLQuery(
                                                "UPDATE public.m_unit_subscription_details SET status = 'I' WHERE subscription_id = :subscriptionId")
                                        .setParameter("subscriptionId", subscriptionId).executeUpdate();

                                System.out.println("Rows updated: " + rowsUpdated);
                            }
                        }

                        return saveNewSubscription(userMasterDto);

                    } else {
                        for (UserMasterDto dto : userDtoList) {
                            if (!"true".equals(dto.getAmountPaid())) {

                                Date currentDateTime = TimeZoneComponent.getDateByzone(userMasterDto.getUnitId());
                                int rowsAffected = sessionFactory.getCurrentSession()
                                        .createSQLQuery("UPDATE public.m_unit_subscription_details " +
                                                "SET subscription_start_date = :subscriptionStartDate, " +
                                                "subscription_end_date = :subscriptionEndDate, " +
                                                "subscription_type = :subscriptionType, " +
                                                "created_date = :createdDate, " +
                                                "amount_paid = :amountPaid " +
                                                "WHERE unit_id = :unitId AND subscription_id = :subId")
                                        .setParameter("subscriptionStartDate", userMasterDto.getSubscriptionStartDate())
                                        .setParameter("subscriptionEndDate", userMasterDto.getSubscriptionEndDate())
                                        .setParameter("subscriptionType", userMasterDto.getSubscriptionType())
                                        .setParameter("createdDate", currentDateTime)
                                        .setParameter("amountPaid", userMasterDto.getAmountPaid())
                                        .setParameter("unitId", userMasterDto.getUnitId())
                                        .setParameter("subId", dto.getSubscriptionId())
                                        .executeUpdate();


                                String inputDateString = userMasterDto.getSubscriptionEndDate().toString();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                                Date parsedDate = dateFormat.parse(inputDateString);

                                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                String formattedDateString = outputDateFormat.format(parsedDate);

                                sessionFactory.getCurrentSession()
                                        .createSQLQuery("UPDATE public.m_unit_master " +
                                                "SET subscription_date = :subscriptionDate " +
                                                "WHERE unit_id = :unitId")
                                        .setParameter("subscriptionDate", formattedDateString)
                                        .setParameter("unitId", userMasterDto.getUnitId())
                                        .executeUpdate();
                                return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
                            }
                        }

                    }

                }

            } else {

                return saveNewSubscription(userMasterDto);
            }

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    private Response saveNewSubscription(UserMasterDto userMasterDto) throws ParseException {
        return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
    }

    @Override
    @Transactional
    public Response getSuperAdminUser(Integer orgId, Integer unitId, Integer userId) throws ApplicationException {
        try {

            UserMasterDto userDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery("Select user_mst.is_super_user as \"isSuperUser\" "
                           + "  from public.m_user_master user_mst where organization_id = :orgId and user_id =:userId limit 1")
                    .setParameter("userId", userId)
                    .setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            return new Response(SUCCESS, SUCCESS_CODE, null, null, userDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

    }

    @Override
    @Transactional
    public Response getSubscriptionDetails(UserMasterDto userMasterDto) throws ApplicationException {
        try {

            Optional<UserMasterDto> userMasterDtoOptional = Optional.ofNullable(userMasterDto);

            if (userMasterDtoOptional.isPresent() && userMasterDtoOptional.get().getOrganizationId() != null && userMasterDtoOptional.get().getUnitId() != null) {
                Optional<List<UserMasterDto>> userDtoListOptional = Optional.ofNullable(sessionFactory)
                        .map(sf -> sessionFactory.getCurrentSession())
                        .map(session -> session.createSQLQuery("Select subUser.subscription_type as \"subscriptionType\", subUser.amount_paid as \"amountPaid\", subUser.organization_id as \"organizationId\",subUser.unit_id as \"unitId\",subUser.status as \"subscriptionStatus\",subUser.subscription_id as \"subscriptionId\","
                                        + "  to_char(subUser.subscription_start_date,'YYYY-MM-dd HH:MI:SS')  AS \"subscriptionStartDateUnitMaster\" ,"
                                        + "to_char(subUser.created_date,'YYYY-MM-dd HH:MI:SS') as \"createdDate\" ,"
                                        + "  to_char(subUser.subscription_end_date,'YYYY-MM-dd HH:MI:SS')  AS \"subscriptionEndDateUnitMaster\""
                                        + "  from public.m_unit_subscription_details subUser where organization_id = :orgId and unit_id = :unitId order by subUser.subscription_id desc ")
                                .setParameter("orgId", userMasterDto.getOrganizationId())
                                .setParameter("unitId", userMasterDto.getUnitId())
                                .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class))
                                .list());

                return userDtoListOptional.map(userDtoList -> new Response(SUCCESS, SUCCESS_CODE, null, userDtoList, null))
                        .orElse(new Response(ERROR, ERROR_CODE, null, null, null));
            } else {

                return new Response(ERROR, ERROR_CODE, null, null, null);
            }

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

    }

    @Override
    @Transactional
    public String getAllowedDomainsList() throws ApplicationException {
        String configuredDomains = (String)sessionFactory.getCurrentSession().createSQLQuery("SELECT allowed_domains FROM public.domain_config").uniqueResult();
        return configuredDomains;
    }
    
	@Override
	public Response getFailedLog(UserMasterDto masterDto) throws ApplicationException {

        try {

            UserMasterDto userDto = (UserMasterDto) sessionFactory.getCurrentSession().createSQLQuery("  select login_block_time as \"loginBlockTime\" , " + 
            		" login_failed_count as \"loginFailedCount\" " + 
            		"  from public.m_user_master  where user_id = :userId limit 1 ")
                    .setInteger("userId", masterDto.getUserId())
                    .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            return new Response(SUCCESS, SUCCESS_CODE, null, null, userDto);
        } catch (Exception e) {
        logger.error("getFailedLog failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    
	}

	@Override
	public Response updateLoginAttempt(Timestamp otpCurrentTimeStamp, Integer i, Integer userId) throws ApplicationException {


        try {

        	if(i == 5) {
        		
        		int rowsUpdated = sessionFactory.getCurrentSession().createSQLQuery(
                        "  update  public.m_user_master  set login_block_time =:otpCurrentTimeStamp ,login_failed_count =:count , "
                        + "   blocked = true ,otp_time_stamp =:otpCurrentTimeStamp1 ,otp_count =:otpCount  "
                        + " where user_id =:userId ")
                .setInteger("count", i)
                .setTimestamp("otpCurrentTimeStamp", otpCurrentTimeStamp)
                .setInteger("otpCount", i)
                .setTimestamp("otpCurrentTimeStamp1", otpCurrentTimeStamp)
                .setInteger("userId", userId)
                .executeUpdate(); 

           
                 return new Response(SUCCESS, null, null, null, null);
        		
        		
        	}else {
        		
        		int rowsUpdated = sessionFactory.getCurrentSession().createSQLQuery(
                        "  update  public.m_user_master  set login_block_time =:otpCurrentTimeStamp ,login_failed_count =:count  where user_id =:userId ")
                .setInteger("count", i)
                .setTimestamp("otpCurrentTimeStamp", otpCurrentTimeStamp)
                .setInteger("userId", userId)
                .executeUpdate(); 

           
                 return new Response(SUCCESS, null, null, null, null);
        	}
        	   
            

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("updateLoginAttempt failed", e);
        }
return new Response(ERROR, null, null, null, null);

    
	}

    @Override
    public Response createOrganizationFromWeb(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException {
        try {
            Integer orgId = (Integer) sessionFactory
                    .getCurrentSession()
                    .createSQLQuery("SELECT public.insert_organization_and_unit_new(:org_name, :unit_name, :p_country_id, :p_state_id, :p_district_id, :p_post_code, :p_city_id, :p_area_id, :p_app_url, :p_organization_address, :p_organization_contact, :p_organization_email_id, :p_app_id)")
                    .setString("org_name", organizationMasterForWebDto.getOrganizationName())
                    .setString("unit_name", organizationMasterForWebDto.getUnitName())
                    .setInteger("p_country_id", organizationMasterForWebDto.getCountryId())
                    .setInteger("p_state_id", organizationMasterForWebDto.getStateId())
                    .setInteger("p_district_id", organizationMasterForWebDto.getDistrictId())
                    .setInteger("p_post_code", organizationMasterForWebDto.getPostalCode())
                    .setInteger("p_city_id", organizationMasterForWebDto.getCityId())
                    .setInteger("p_area_id", organizationMasterForWebDto.getAreaId())
                    .setString("p_app_url", organizationMasterForWebDto.getAppUrl())
                    .setString("p_organization_address", organizationMasterForWebDto.getAddress())
                    .setString("p_organization_contact", organizationMasterForWebDto.getContactNumber())
                    .setString("p_organization_email_id", organizationMasterForWebDto.getOrganizationEmailId())
                    .setInteger("p_app_id", organizationMasterForWebDto.getAppId())
                    .uniqueResult();


            OrganizationMasterDto organizationUserDetails = (OrganizationMasterDto) sessionFactory
                    .getCurrentSession()
                    .createSQLQuery("select um.user_name as  \"userName\",um.password as \"password\", um.user_id as \"userId\", umm.time_zone as \"unitTimeZone\" ,umm.unit_id as \"unitId\" from public.m_user_master um" +
                            " inner join  m_unit_master umm  on um.organization_id=umm.organization_id " +
                            " where umm.organization_id=:orgId and user_name ='Admin' limit 1")
                    .setInteger("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();

            String password = GlobalCommonDateUtils.generateRandomString();

            String encryptedPassword = GlobalCommonDateUtils.encrypt(password);
            sessionFactory.getCurrentSession()
                    .createSQLQuery("update public.m_user_master set password=:password " +
                            " where organization_id=:orgId and user_name ='Admin' ")
                    .setInteger("orgId", orgId)
                    .setString("password", encryptedPassword).executeUpdate();

            organizationMasterForWebDto.setUserName(organizationUserDetails.getUserName());
            organizationMasterForWebDto.setPassword(encryptedPassword);
            organizationMasterForWebDto.setUserId(organizationUserDetails.getUserId());
            organizationMasterForWebDto.setUnitTimeZone(organizationUserDetails.getUnitTimeZone());
            organizationMasterForWebDto.setUnitId(organizationUserDetails.getUnitId());



            sessionFactory
                    .getCurrentSession()
                    .createSQLQuery("INSERT INTO public.payment_details (payment_details_json, organization_id, user_id,contact_number,contact_person,personal_email_id) " +
                            "VALUES (:paymentDetails, :orgId, :userId, :contactNumber, :contactPerson, :emailId)")
                    .setString("paymentDetails", organizationMasterForWebDto.getPaymentDetails())
                    .setInteger("orgId", orgId)
                    .setInteger("userId", organizationMasterForWebDto.getUserId())
                    .setString("contactNumber", organizationMasterForWebDto.getContactNumber())
                    .setString("contactPerson", organizationMasterForWebDto.getContactPerson())
                    .setString("emailId", organizationMasterForWebDto.getEmailId())
                    .executeUpdate();

            ObjectMapper objectMapper = new ObjectMapper();
            TreeMap<String, Object> paymentDetailsMap = objectMapper.readValue(organizationMasterForWebDto.getPaymentDetails(),
                    TreeMap.class);

            //convert into finacial value to double
            paymentDetailsMap.put("amount",getValueAsDouble(paymentDetailsMap.get("amount")));
            paymentDetailsMap.put("subscriptionDiscount",getValueAsDouble(paymentDetailsMap.get("subscriptionDiscount")));
            paymentDetailsMap.put("totalAmount",getValueAsDouble(paymentDetailsMap.get("totalAmount")));
            paymentDetailsMap.put("taxAmount",getValueAsDouble(paymentDetailsMap.get("taxAmount")));
            paymentDetailsMap.put("amountPaid",getValueAsDouble(paymentDetailsMap.get("amountPaid")));


            String updateSubscriptionConfig = " UPDATE public.m_unit_subscription_config  " +
                    "    SET subscription_amount = :amount," +
                    "    subscription_discount = :subscriptionDiscount," +
                    "    subscription_net_amount = :totalAmount," +
                    "    subscription_tax = :taxAmount," +
                    "    subscription_type = :planType where organization_id= :orgId";

            sessionFactory
                    .getCurrentSession()
                    .createSQLQuery(updateSubscriptionConfig)
                    .setParameter("amount",(Double) paymentDetailsMap.get("amount"))
                    .setParameter("subscriptionDiscount",(Double) paymentDetailsMap.get("subscriptionDiscount"))
                    .setParameter("totalAmount",(Double) paymentDetailsMap.get("totalAmount"))
                    .setParameter("taxAmount",(Double) paymentDetailsMap.get("taxAmount"))
                    .setParameter("planType",(String) paymentDetailsMap.get("planType"))
                    .setParameter("orgId",orgId)
                    .executeUpdate();


            String updateSubscriptionDetails = " UPDATE public.t_unit_subscription_details set  " +
                    "    subscription_amount = pgp_sym_encrypt( :amount, 'ssshhhhhhhhhhh!!!!'),"+
                    "    subscription_discount = pgp_sym_encrypt( :subscriptionDiscount, 'ssshhhhhhhhhhh!!!!'),"+
                    "    subscription_tax = pgp_sym_encrypt( :taxAmount, 'ssshhhhhhhhhhh!!!!'),"+
                    "    paid_amount = pgp_sym_encrypt( :amountPaid, 'ssshhhhhhhhhhh!!!!')," +
                    "    subscription_net_amount = pgp_sym_encrypt( :totalAmount, 'ssshhhhhhhhhhh!!!!')," +
                    "    subscription_start_date = pgp_sym_encrypt( :startDate, 'ssshhhhhhhhhhh!!!!')," +
                    "    payment_order_id = pgp_sym_encrypt( :TransactionId, 'ssshhhhhhhhhhh!!!!')," +


                    "    subscription_end_date = pgp_sym_encrypt( :endDate, 'ssshhhhhhhhhhh!!!!')," +
                    "    subscription_type = :planType ,amount_paid_status = 'PAID'  "+
                    "    where organization_id= :orgId" ;

            sessionFactory
                    .getCurrentSession()
                    .createSQLQuery(updateSubscriptionDetails)
                    .setParameter("amount",String.valueOf( paymentDetailsMap.get("amount")))
                    .setParameter("subscriptionDiscount",String.valueOf(paymentDetailsMap.get("subscriptionDiscount")))
                    .setParameter("taxAmount",String.valueOf(paymentDetailsMap.get("taxAmount")))
                    .setParameter("amountPaid",String.valueOf( paymentDetailsMap.get("amount")))
                    .setParameter("totalAmount",String.valueOf(paymentDetailsMap.get("totalAmount")))

                    .setParameter("TransactionId",(String) paymentDetailsMap.get("TransactionId"))


                    .setParameter("startDate",(String) paymentDetailsMap.get("startDate"))
                    .setParameter("endDate",(String) paymentDetailsMap.get("endDate"))
                    .setParameter("planType",(String) paymentDetailsMap.get("planType"))
                    .setParameter("orgId",orgId)
                    .executeUpdate();

            organizationMasterForWebDto.setOrganizationId(orgId);

            return new Response(SUCCESS, null, null, null, organizationMasterForWebDto);
        } catch (Exception e) {
        logger.error("createOrganizationFromWeb failed", e);
        }
return new Response(ERROR, ERROR_CODE, null, null, null);
    }

    private static Double getValueAsDouble(Object value) {
        try {
            if (value == null) {
                return 0.0;
            }
            if (value instanceof Integer integer) {
                return integer.doubleValue();
            } else if (value instanceof Float float1) {
                return float1.doubleValue();
            } else if (value instanceof Double double1) {
                return double1;
            } else {
                return 0.0;
            }
        }catch (Exception e){
        logger.error("getValueAsDouble failed", e);
        }
return 0.0;
    }

    @Override
    public Map<String, String> getawsConfigMap() throws ApplicationException {
        try {
            // Query execution and transformation to a single Map<String, String>
            Map<String, String> awsConfigMap = (Map<String, String>) sessionFactory.getCurrentSession()
                    .createSQLQuery("select x_api_key as \"apiKey\",url as \"url\", beanstalk_environment as \"beanstalkEnvironment\", html_template as \"htmlTemplate\" from public.aws_config limit 1")
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP) // Transforms the result to Map<String, String>
                    .uniqueResult(); // Retrieves a single row as a map

            return awsConfigMap;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Response getAllUsersByOrgId(Integer organizationId) throws ApplicationException {
        try {
            List<OrganizationMasterDto> organizationUserDetails =  sessionFactory
                    .getCurrentSession()
                    .createSQLQuery("select um.user_name as  \"userName\",um.password as \"password\", um.user_id as \"userId\", umm.time_zone as \"unitTimeZone\" ,umm.unit_id as \"unitId\" from public.m_user_master um" +
                            " inner join  m_unit_master umm  on um.organization_id=umm.organization_id " +
                            " where umm.organization_id=:orgId  ")
                    .setInteger("orgId", organizationId)
                    .setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).list();

            return  new Response(SUCCESS,SUCCESS_CODE,null,organizationUserDetails,null);

        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

    }

    @Override
    public Response getWebUserId(Integer unitId) throws ApplicationException {
        try {
            UserMasterDto organizationUserDetails = (UserMasterDto) sessionFactory
                .getCurrentSession()
                .createSQLQuery(" select web_app_user_id as \"webAppUseId\" from t_unit_application_billing_configuration tuabc   where unit_id  = :unitId and status  = 'A' limit 1 ")
                .setInteger("unitId", unitId)
                .setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).uniqueResult();
            return  new Response(SUCCESS,SUCCESS_CODE,null,null,organizationUserDetails);

        } catch (Exception e) {
        logger.error("getWebUserId failed", e);
        }
return  new Response(ERROR,ERROR_CODE,"",null,null);
    }

    @Override
    public Response generateSMSOtp(UserMasterDto userMasterDto, HttpServletRequest request) throws ApplicationException {
        Response mailingResponse = null;
        try {
                char[] otp = GlobalCommonDateUtils.generateOTP(4);
                String otpResult = String.valueOf(otp);
                HttpSession session = request.getSession();
                session.setAttribute("otp_"+userMasterDto.getMobileNumber(), otpResult);

                String responseMsg = "Hello, @Otp is your OTP for logging in to AxonCare. Please don't share your OTP with anyone.-AxonCare";
                String msg = responseMsg.replace("@UserName", "Hello").replace("@Otp", otpResult);

                String mobileNumber = userMasterDto.getMobileNumber();
                sendSmsToMobileNumber(mobileNumber,msg);
                return new Response(SUCCESS, null, null, null, null);

        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("generateSMSOtp failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    @Override
    public Response validateSMSOtp(UserMasterDto userMasterDto, HttpServletRequest request) throws ApplicationException {
        try {
            HttpSession session = request.getSession();
            String sessionOtp = (String) session.getAttribute("otp_" + userMasterDto.getMobileNumber());
            if (sessionOtp != null && sessionOtp.equals(userMasterDto.getOtp())) {
                session.removeAttribute("otp_" + userMasterDto.getMobileNumber());
                return new Response(SUCCESS, null, null, null, null);
            } else {
                return new Response(ERROR, null, "Invalid OTP", null, null);
            }
        } catch (HibernateException he) {
            throw new HibernateException(he.getMessage());
        } catch (Exception e) {
        logger.error("validateSMSOtp failed", e);
        }
return new Response(ERROR, null, null, null, null);
    }

    private void sendSmsToMobileNumber(String mobileNoWithCode, String msg) throws ApplicationException {
        try {
            mobileNoWithCode = mobileNoWithCode.length() <= 10
                    ? "+91" + mobileNoWithCode
                    : mobileNoWithCode;
            String message = msg;
            message = URLEncoder.encode(message, "UTF-8");
            message = message.replace("%E2%80%99", "'");


            String smsSendingLink = "http://api.msg91.com/api/v2/sendsms";
            String sendingType = "POST";
            Integer smsRouteId = 4;
            String smsDltTeId = "1307173398170095513";
            String smsSender = "AXHPVL";
            String smsAuthKey = "412534ALtesM00o66b1ca0aP1";



            HttpResponse<String> asString = Unirest.post(smsSendingLink).header("authkey", smsAuthKey)
                    .header("content-type", "application/json")
                    .body("" + "{ \"sender\": \"" + smsSender + "\", "
                            + "\"route\": \"" + smsRouteId + "\", "
                            + "\"DLT_TE_ID\": \"" + smsDltTeId + "\", " + " \"country\": \"0\", " + "\"sms\":" + " [ "
                            + "{ \"message\": \"" + message + "\", "
                            + "\"to\": [ \"" + mobileNoWithCode + "\"]" + " } "
                            + "] " + "}")
                    .asString();
            System.out.println("SMS Sending Msg " + asString.getBody());
        }catch (Exception e) {
        logger.error("sendSmsToMobileNumber failed", e);
        }
    }



}
	
