package com.param.global.controller;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.param.global.dto.TwilioUserChatDetailsDto;
import com.param.global.dto.UserChatDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.Response;
import com.amazonaws.services.apigateway.model.Model;
import com.google.gson.Gson;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.global.common.ICommonConstants;
import com.param.global.dto.TwilioCallBackDto;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.service.IAppointmentMasterService;
import com.param.opd.appointment.dto.AppointmentMasterDto;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import com.twilio.rest.video.v1.Room;

import com.twilio.rest.video.v1.room.*;
import com.twilio.rest.video.v1.room.ParticipantReader;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "rawtypes", "unchecked" ,"unused"})
public class TwilioController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(TwilioController.class);

	String twilioAccountSid = System.getenv("TWILIO_ACCOUNT_SID");
	String twilioApiKey = System.getenv("TWILIO_API_KEY");
	String twilioApiSecret = System.getenv("TWILIO_API_SECRET");
	String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
	@Autowired
	private IAppointmentMasterService iAppointmentMasterService;
	
	@Autowired
	private IUserChatDetailsService iUserChatDetailsService;

	
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Autowired
	 private IAppointmentMasterDao iAppointmentMasterDao;
	 
	
	@Autowired
	private Gson gson;
	
	@Autowired
	FCMService fcmService;


	/*@RequestMapping(value = "/generateToken", method = RequestMethod.POST)
	public @ResponseBody ResponseTwilio generateToken(@RequestBody String room) {
		String identity = "dataroom";
		VideoGrant grant = new VideoGrant();
		grant.setRoom(room);

		// Create access token
		AccessToken token = new AccessToken.Builder(twilioAccountSid, twilioApiKey, twilioApiSecret).identity(identity)
				.grant(grant).build();
		System.out.println(token.toJwt());
		return  ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, token.toJwt());

	}
*/

	@RequestMapping(value = "/token/{identity}/{room}", method = RequestMethod.GET)
	public @ResponseBody String token(@PathVariable("identity") String identity, @PathVariable("room") String room) {
		// String room = "dataroom";
		VideoGrant grant = new VideoGrant();
		grant.setRoom(room);

		// Create access token
		AccessToken token = new AccessToken.Builder(twilioAccountSid, twilioApiKey, twilioApiSecret).identity(identity)
				.grant(grant).build();
		System.out.println(token.toJwt());
		return token.toJwt();

	}

	@RequestMapping(value = "/createRoom", method = RequestMethod.POST)
	public @ResponseBody ResponseTwilio createRoom() {
		Twilio.init(twilioAccountSid, twilioApiKey);
		Room room = Room.creator().setUniqueName("CHT").create();

		System.out.println(room.getSid());
		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, room.getSid());

	}

	@RequestMapping(value = "/roomList", method = RequestMethod.POST)
	public @ResponseBody ResponseTwilio roomList(@RequestBody String roomName) {
		Twilio.init(twilioAccountSid, twilioApiKey);
		Room room = Room.fetcher(roomName).fetch();

		System.out.println(room.getUniqueName());
		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, room.getUniqueName());
	}
	/*
	 * @RequestMapping(value = "/connectToRoom") public String
	 * connectToRoom(Model model) { return "patient/twilio-video-call"; }
	 */

	@RequestMapping(value = "/connectToRoom")
	public String connectToRoom(Model model) {
		return "admin/index";
	}

	@RequestMapping(value = "/joinAgainRoom")
	public String joinAgainRoom(Model model) {
		return "admin/index2";
	}

	@RequestMapping(value = "/StatusCallbacks", method = RequestMethod.POST)
	public @ResponseBody ResponseTwilio StatusCallbacks(@RequestBody TwilioCallBackDto twilioCallBackDto) {
		System.out.println(twilioCallBackDto.toString());
		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, null);
	}

	@RequestMapping(value = "/deleteParticipants", method = RequestMethod.POST)
	public @ResponseBody ResponseTwilio deleteParticipants() {
		Participant.updater("DailyStandup", "Alice").setStatus(Participant.Status.DISCONNECTED).update();
		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, null);
	}
	
	@RequestMapping(value = "/deleteParticipants/{identity}/{room}", method = RequestMethod.GET)
	public @ResponseBody ResponseTwilio deleteParticipants(@PathVariable("identity") String identity, @PathVariable("room") String room) {
		try{
			Twilio.init(twilioAccountSid, AUTH_TOKEN);
			 
	    	Participant.updater(room,
	    			identity).setStatus(Participant.Status.DISCONNECTED).update();
		}catch(Exception e){
		logger.error(e.getMessage(), e);
		return new ResponseTwilio(ERROR, ERROR_CODE, null, null, null);
		}
		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, null);
	}

	@RequestMapping(value = "/appointmentDetailsByAppointIdTwilio", method = RequestMethod.POST)
	public @ResponseBody ResponseTwilio appointmentDetailsByAppointIdTwilio( @RequestBody AppointmentMasterDto dto) {
		try {
			Integer appointmentId =dto.getAppointmentId();
			com.param.global.common.Response appointDetail = iAppointmentMasterDao.getAppointmentDetails(appointmentId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return null;
	}
	

	
	@RequestMapping(value = "/checkParti/{room}", method = RequestMethod.GET)
	public @ResponseBody ResponseTwilio checkParti(@PathVariable("room") Integer room) {
		Twilio.init(twilioAccountSid, AUTH_TOKEN);
		ResourceSet<Participant> participants = Participant.reader("dataroom_"+room).setStatus(Participant.Status.CONNECTED)
                .read();
		int count=0;
		String indentity="";
		for (Participant participant : participants) {
			count++;
			indentity=participant.getIdentity();
        }
		URI url = null;
		Response commonResponse1 = null;
/*        if(count==1 && "Doctor".equalsIgnoreCase(indentity)) {
        	
					try {
						PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
						pushNotificationRequest.setMessage("Doctor has joined the video consultation. Click here to join. ");
						pushNotificationRequest.setTitle("Video call ");
						pushNotificationRequest.setToken("d2ycpInLR7yy34OV4xj9V9:APA91bHi4B4qTvfrxoSxT0TCEOb6vX_gUPsfSiH3d7s22arflZBHWUJb268sJ5U-DDwqWSk9HUr3vkImf-FIjhe6U2r7ogfWFOX2vXCz_fAS24QDr3blk1dlPZ7iJt-CGq78iA-GNBor");
						Map<String, String> appData = new HashMap<>();
						appData.put("name", "PushNotification");
						fcmService.sendMessage(appData, pushNotificationRequest);
					} catch (Exception e) {
						e.printStackTrace();
					}
			 
			return  new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null, null);
		
        }*/
		
		return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null);

	}
	
	
	@RequestMapping(value = "/api/saveTwilioMsg" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody com.param.global.common.Response saveTwilioMsg(@RequestBody AppointmentStatusSyncDto dto){
    	return  iUserChatDetailsService.saveTwilioMsg(dto);
		}
	
	@RequestMapping(value = "/api/deleteParticipants" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseTwilio deleteParticipants1(@RequestBody AppointmentStatusSyncDto dto){
		try{
			Twilio.init(twilioAccountSid, AUTH_TOKEN);
			 
			Participant.updater("dataroom_"+dto.getAppointmentId(), "Patient").setStatus(Participant.Status.DISCONNECTED).update();
			Participant.updater("dataroom_"+dto.getAppointmentId(), "Doctor").setStatus(Participant.Status.DISCONNECTED).update();
			return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null);
		}catch(Exception e){
		logger.error(e.getMessage(), e);
		return new ResponseTwilio(ERROR, ERROR_CODE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/api/getVideoataForNotification" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody com.param.global.common.Response getVideoataForNotification(@RequestBody AppointmentStatusSyncDto dto){
    	return  iUserChatDetailsService.getVideoataForNotification(dto);
		}
	
	@RequestMapping(value = "/api/changeStatusofuserChatDetailsId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody com.param.global.common.Response changeStatusofuserChatDetailsId(@RequestBody AppointmentStatusSyncDto dto){
    	return  iUserChatDetailsService.changeStatusofuserChatDetailsId(dto);
		}


	@RequestMapping(value = "/getMessagesForCliniTalk", method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseTwilio getMessage(@RequestBody UserChatDetailsDto input) throws InterruptedException {
		List<TwilioUserChatDetailsDto>  listDto=iUserChatDetailsService.getChatsByToUserIdChatIdTwilio(input).getListObject();
		try {
			if (listDto != null && listDto.size() > 0) {
				return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null);
			}else {
				return new ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null);
			}
		}catch (Exception e) {
		logger.error("method failed", e);
			return new ResponseTwilio(ERROR, null, e.getMessage(), null);
		}
	}

}
