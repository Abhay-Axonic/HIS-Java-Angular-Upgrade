var contextPath = null; 
var stompClient = null;
var twiliomsgId=0;

 function setConnected(connected) {
      document.getElementById('connect').disabled = connected;
     document.getElementById('disconnect').disabled = !connected;
     document.getElementById('conversationDiv').style.visibility 
       = connected ? 'visible' : 'hidden';
     document.getElementById('response').innerHTML = ''; 
 }
 
 function connect() {
//     var socket = new SockJS(contextPath+'/chat/');
     var socket = new SockJS("10.10.0.226"+'/chat/');
     stompClient = Stomp.over(socket);  
     stompClient.connect({}, function(frame) {
//         setConnected(true);
         console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(messageOutput) {
             showMessageOutput(JSON.parse(messageOutput.body));
         });
        
        stompClient.subscribe('/topic/notifications',function(messageOutput){
//        	showNotifications(JSON.parse(messageOutput.body));
        })
         
        getNotifications();
     });
 }
  
 function disconnect() {
     if(stompClient != null) {
         stompClient.disconnect();
     }
     setConnected(false);
     console.log("Disconnected");
 }
  
 var auth = "";
 function sendMessage() {
     var toUserId = $('#toUserId').val();
     var message = $('#submit_message').val();
     message = message.trim();
     if(message != ""){
	     var dateTime = getDateTime();
	     auth = globalUserId+"_"+toUserId;
	     stompClient.send("/app/chat", {"auth-token":auth}, 
	       JSON.stringify({'toUserId':toUserId, 'message':message,'fromUserId':globalUserId,'dateTime':dateTime}));
	     $('#submit_message').val('');
     }
 }
  
 function showMessageOutput(messageOutput) {
	 console.log("showMessageOutput");
	 console.log(messageOutput);
	 var list = messageOutput.listObject;
	 var auth = messageOutput.message;
	 auth = auth.split('_');
	 
	 var object = list != "" && typeof(list) != "undefined" ? list[list.length-1] : null;
	 if(object != null){
		 var fromMsg = object.fromUserId == globalUserId ? 1 : 2;
		 console.log(auth + "..." + globalUserId + ".." + object.fromUserId);
		 if((auth[0] == globalUserId || auth[1] == globalUserId) && ((auth[0] == object.fromUserId || auth[1] == object.fromUserId ) && (auth[0] == object.toUserId || auth[1] == object.toUserId)) && ($('#toUserId').val() == object.fromUserId || $('#toUserId').val() == object.toUserId) ){
		 var msgBody = "";
		 switch(fromMsg){
		 case 1:
			 msgBody = "<div class='chat_message_wrapper chat_message_right'> "
					 + " <ul class='chat_message'> "
					 + " <li> "
					 + " <p> "
					 + object.message
					 + " <span class='chat_message_time'>"+object.dateTime+"</span> "
					 + " </p> "
					 + " </li> "
					 + " </ul> "
					 + " </div> ";
			 break;
		 case 2:
			 msgBody = "<div class='chat_message_wrapper '> " 
					 + " <ul class='chat_message'> "
					 + " <li> "
					 + " <p> "
					 + object.message
					 + " <span class='chat_message_time'>"+object.dateTime+"</span> "
					 + " </p> "
					 + " </li> "
					 + " </ul> "
					 + " </div> ";
			 break;
		 }
		 $('#messageBody').append(msgBody);
		 }
		 $('#chat').animate({"scrollTop": $('#chat')[0].scrollHeight},"slow");
	 }
	 
	 //Send a request for notifications whenever a message is recieved
	 getNotifications();
	 
 }
 
 /* Function for open Chat Popup  */
 var listChats = new Array();
 function openChat(userId,profileName,profilePicture){
		 $('#messageBody').empty();	
		 showChatLoader();
	 	profilePicture = profilePicture != '' ? profilePicture : contextPath+'/resources/assets/common/images/icon/chat-default-user.png';
	 	$('#'+userId+'_notify').text(0);
	 	$('#'+userId+'_notify').hide();
	 	$('#toUserId').val(userId);
	 	$('#chatUserName').text(profileName);
	 	$('#chatUserProfile').attr('src',profilePicture);
		$('#chatPopup').addClass('popup-box-on');
		var param = {toUserId:userId,fromUserId:globalUserId};
		$.ajax({
			url : "chatDetailsByUser",
			type : "post",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(param),
	        headers:getHeaders(),
			success : function(response){
				var status = response.status;
				var list = response.listObject;
				listChats = list;
				list = list.slice(Math.max(list.length - 10, 0));
				$.each(list,function(i,val){
					 var fromMsg = val.fromUserId == globalUserId ? 1 : 2;
					 var msgBody = "";
					 switch(fromMsg){
					 case 1:
						 msgBody = "<div class='chat_message_wrapper chat_message_right'> "
								 + " <ul class='chat_message'> "
								 + " <li> "
								 + " <p> "
								 + val.message
								 + " <span class='chat_message_time'>"+val.dateTime+"</span> "
								 + " </p> "
								 + " </li> "
								 + " </ul> "
								 + " </div> ";
						 break;
					 case 2:
						 msgBody = "<div class='chat_message_wrapper '> " 
								 + " <ul class='chat_message'> "
								 + " <li> "
								 + " <p> "
								 + val.message
								 + " <span class='chat_message_time'>"+val.dateTime+"</span> "
								 + " </p> "
								 + " </li> "
								 + " </ul> "
								 + " </div> ";
						 break;
					 }
					 $('#messageBody').append(msgBody);
				});
				//Mark Chat as Read
				 markChatAsRead(globalUserId,userId);
				hideChatLoader();
				 $('#chat').animate({"scrollTop": $('#chat')[0].scrollHeight},"slow");
				 
			},error : function(){
				console.log("Error");
			}
		})
 }
 
 
 
 $("#removeChatPopup").click(function () {
	$('#chatPopup').removeClass('popup-box-on');
	$('#toUserId').val('');
});


$("#displayChatList").click(function () {
	$('#chatList').addClass('popup-box-on');
	$('#searchUser').val('');
	$('#searchUser').trigger('keyup');
	$('.chat_message_wrapper_chat_list').css('background-color','');
	//send request to check notification
	getNotifications();
	$('#totalNotifications').hide();
});
		  
$("#removeChatList").click(function () {
	$('#chatList').removeClass('popup-box-on');
	getNotifications();
	$('#totalNotifications').show();
});

function getDateTime(){
	 var date = new Date();
    var str = date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear() + " " +  date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    return str;
}

function showChatLoader(){
	$('#chat').animate({"scrollTop": 0},"fast");
	$('.chat-loader').show();
}

function hideChatLoader(){
	$('.chat-loader').hide();
}

$('#searchUser').keyup(function(){
	try{
		// Declare variables 
		  var input, filter, table, tr, td, i;
		  input = document.getElementById("searchUser");
		  filter = input.value.toUpperCase();
		  table = document.getElementById("userListTable");
		  tr = table.getElementsByTagName("tr");

		  // Loop through all table rows, and hide those who don't match the search query
		  for (i = 0; i < tr.length; i++) {
		    td = tr[i].getElementsByTagName("td")[0];
		    if (td) {
		    var text = $(td).children().find('.prof-name').text();
		      if (text.toUpperCase().indexOf(filter) > -1) {
		        tr[i].style.display = "";
		      } else {
		        tr[i].style.display = "none";
		      }
		    } 
		  }
	}catch(e){
		console.log(e.message);
	}
});

$('.chat_message_wrapper_chat_list').click(function(){
	$('.chat_message_wrapper_chat_list').css('background-color','');
	$(this).css('background-color','rgba(29, 0, 255, 0.2)');
	$('#searchUser').val('');
	$('#searchUser').trigger('keyup');
});

//Send Message on enter click
$('#submit_message').keyup(function(e){
	try{
		if(e.keyCode == 13){
			var text = $('#submit_message').val();
			if(text != ""){
				sendMessage();
			}
		}
	}catch(e){
		console.log(e.message);
	}
});

//Mark Chat as Read
function markChatAsRead(fromUserId,toUserId){
	try{
		var dateTime = getDateTime();
		var param = {"dateTime":dateTime,"fromUserId":fromUserId,"toUserId":toUserId};
		$.ajax({
			url : "markChatRead",
			type : "post",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(param),
	        headers:getHeaders(),
			success : function(response){
				console.log(response);
			},error:function(){
				console.log("error")
			}
		});
	}catch(e){
		console.log(e.message);
	}
}

/*function showNotifications(messageOutput){
	try{
		console.log(messageOutput)
		var list = messageOutput.listObject;
		var message = messageOutput.message;
		var fromUserId = 0;
		var count = 0;
		var prevFromUserId = 0;
		var totalNotifications = 0;
		var listNotification = new Array();
		var listUserIds = new Array();
		if(message == globalUserId){
		$.each(list,function(i,val){
			totalNotifications = totalNotifications + 1;
			if(fromUserId != val.fromUserId){
				count = 1;
				fromUserId = val.fromUserId;
				var tempJson = {userId:val.fromUserId,count:count};
				listNotification.push(tempJson);
				listUserIds.push(val.fromUserId);
			}else{
				var index = listUserIds.indexOf(val.fromUserId);
				var tempJson = listNotification[index];
				count = tempJson.count;
				count = count + 1;
				tempJson["count"] = count;
				listNotification[index] = tempJson;
			}
			 //Twilio
			if(totalNotifications>0&&val.message!=''){
				//Twilio changes
				var param = { "appointmentId":val.message};
				$.ajax({
					url : "appointmentDetailsByAppointIdTwilio",
					type : "post",
					dataType : "json",
					async : false,
					contentType : "application/json",
					data : JSON.stringify(param),
					success : function(response){
						console.log(response);
						var dto=response.listObject[0];
						var src="";
						var twiliobaseUrlLocation = window.location.href;
						var twiliotemp = twiliobaseUrlLocation.split('/');
						var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length-1],"");
						if(dto.patientPhoto!=null){
							src=dto.patientPhoto;
						}else if( dto.gender=='Male'){
							 src=twilioBASE_URL_CALL+"resources/assets/common/images/project-images/m.jpg";
						}else{
							 src=twilioBASE_URL_CALL+"resources/assets/common/images/icon/patient.png";  
						}
						var coverpage =twilioBASE_URL_CALL+ "/newCoverPageInTwilio/" + dto.patientId + "/"+ dto.appointmentId + "/" + dto.appointmentStatusId+"/"+dto.appointmentTypeId+"/"+val.userChatDetailsId+"";
						$('#vedioCallAlert').modal();
						$('#twilioDoctorNotifyMsg').empty();
					var html=	'<span class="patientDetailsMsg">'+dto.patientName +'</span>&nbsp; is ready and waiting in the room for the <span class="patientDetailsMsg">Teleconsultation appointment at '+dto.timeSlot.hour+':'+dto.timeSlot.minute+'</span>';
					$('#twilioDoctorNotifyMsg').empty();
					$('#twilioDoctorNotifyMsg').append(html);
					$('#twilioVideoPatientPhoto').prop("src", src);
					$('#twilioGoToCasesheet').val(coverpage);
					$('#twilioappointmentStatusId').val(dto.appointmentStatusId);
					$('#twilioappointmentId').val(dto.appointmentId);
							if (dto.appointmentStatusId == 1) {
								 $('#twilioGoToCasesheetButton').text("Checkin and join");
							}else if (dto.appointmentStatusId == 2) {
							$('#twilioGoToCasesheetButton').text("join");
						
							}
					},error:function(){
						console.log("error")
					}
				});
				
				
//				alert("Patient is wating for you.");
//				totalNotifications=1;
			}
		});
		
		console.log(listNotification);
		var toUserId = $('#toUserId').val();
		$.each(listNotification,function(i,val){
			var userId = val.userId;
			if(toUserId != userId){
				$('#'+userId+'_notify').text(val.count);
				$('#'+userId+'_notify').show();
			}else{
				markChatAsRead(globalUserId,userId);
			}
		});
		
		$('#totalNotifications').text(totalNotifications);
		}
	}catch(e){
		console.log(e.message);
	}
}
*/
function getNotifications(){
	$('.div-circle-doc-bg-pending-chat').hide();
	console.log("Notification call")
    stompClient.send('/app/chat/notifications',{"auth-token":globalUserId},
		 JSON.stringify({"toUserId":globalUserId}));
}

// load data in chat if scrolled to top
var currentScrollIndex = 10;
$('#chat').scroll(function (event) {
    var scroll = $('#chat').scrollTop();
    if(scroll == 0){
    	currentScrollIndex = currentScrollIndex + 10;
    	list = listChats.slice(Math.max(listChats.length - currentScrollIndex, 0));
    	$('#messageBody').empty();
    	$.each(list,function(i,val){
			 var fromMsg = val.fromUserId == globalUserId ? 1 : 2;
			 var msgBody = "";
			 switch(fromMsg){
			 case 1:
				 msgBody = "<div class='chat_message_wrapper chat_message_right'> "
						 + " <ul class='chat_message'> "
						 + " <li> "
						 + " <p> "
						 + val.message
						 + " <span class='chat_message_time'>"+val.dateTime+"</span> "
						 + " </p> "
						 + " </li> "
						 + " </ul> "
						 + " </div> ";
				 break;
			 case 2:
				 msgBody = "<div class='chat_message_wrapper '> " 
						 + " <ul class='chat_message'> "
						 + " <li> "
						 + " <p> "
						 + val.message
						 + " <span class='chat_message_time'>"+val.dateTime+"</span> "
						 + " </p> "
						 + " </li> "
						 + " </ul> "
						 + " </div> ";
				 break;
			 }
			 $('#messageBody').append(msgBody);
		});
    	
    }
});


$(document).on('keyup', '#submit_message', function() {
	try {
		var input = $(this).val();
		
		if(!input.match(/^([a-z A-Z 0-9 -., ]*)$/))
			{
				$("#submit_message").val(" ");
				$.notify("Special character is not allowed","error");
			}
	
	
	} catch (e) {
		console.log(e.message);
	}
});


$(document).on('keyup', '#searchUser', function() {
	try {
		var input = $(this).val();
		
		if(!input.match(/^([a-z A-Z 0-9 -. ]*)$/))
			{
				$("#searchUser").val(" ");
				$.notify("Special character is not allowed","error");
			}
	
	
	} catch (e) {
		console.log(e.message);
	}
});
function twilioGoToCasesheetFunction(){
	var statusId=$('#twilioappointmentStatusId').val();
	var appointmentId=$('#twilioappointmentId').val();

	if(statusId==1){
		var param = { "appointmentId":appointmentId};
		$.ajax({
			type : "post",
			url : "changeStatus",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(param),
			async : false,
	        headers:getHeaders(),
			success : function(response) {
				var url=$('#twilioGoToCasesheet').val();
				 window.location.href=url;
			},
			error : function() {
				console.log("error");
			//	stopLoader();
			}
		});
	}else if(statusId==2) {
		var url=$('#twilioGoToCasesheet').val();
		 window.location.href=url;
	} 
	
	
}

/*(function poll(){
	setInterval(function(){
		debugger;
		 var url= BASE_URL+ ROOT_URL+'/api/getVideoataForNotification';
			var totalNotifications = 0;
			var data =
			{
			"unitId":$('#unitIdf').val(),
			"doctorId":$('#cookieDoctorId').val()
			}
			$.ajax({
				type : "post",
				url : url,
				contentType : "application/json",
		        data : JSON.stringify(data),
				success : function(response) {
									  console.log(response);
									  var list=response.listObject;
									  if(list != null && list.length >0){
									  $.each(list,function(i, dto) {
										  var notificationShownPatientData = dto;
											$('#twilioappointmentId').val(dto.appointmentIdMyLife);
											$('#userChatDetailsId').val( dto.userChatDetailsId);
									        $('#vedioCallAlert').modal();
									        
												totalNotifications = totalNotifications + 1;
																var src = "";
																var twiliobaseUrlLocation = window.location.href;
																var twiliotemp = twiliobaseUrlLocation .split('/');
																var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length - 1],"");
																if (dto.patientPhoto != null) {
																	src = dto.patientPhoto;
																} else if (dto.genderId == 1) {
																	src = twilioBASE_URL_CALL+ "resources/assets/common/images/project-images/m.jpg";
																} else {
																	src = twilioBASE_URL_CALL+ "resources/assets/common/images/icon/patient.png";
																}
																var coverpage = twilioBASE_URL_CALL+ "/newCoverPageInTwilio/"+ dto.patientId
																		+ "/"+ dto.appointmentId+ "/"+ dto.appointmentStatusId
																		+ "/"+ dto.appointmentTypeId+ "/"+ dto.userChatDetailsId+ "";
																$('#vedioCallAlert').modal();
																$('#twilioDoctorNotifyMsg').empty();
																var html = '<span class="patientDetailsMsg">'
																		+ dto.patientName
																		+ '</span> is ready and waiting in the room for the <span class="patientDetailsMsg">Teleconsultation appointment at '
																		+ dto.timeSlot 
																		+ '</span>';
																$(
																		'#twilioDoctorNotifyMsg')
																		.empty();
																$(
																		'#twilioDoctorNotifyMsg')
																		.append(html);
																$( '#twilioGoToCasesheet') .val(coverpage);
																$( '#twilioappointmentStatusId') .val( dto.appointmentStatusId);
															
									  });
									  }
			 $('#totalNotifications').text(totalNotifications);
		},
		error : function() {
			console.log("error");
			stopLoader();
		}
	});
	 },30000);
	})();*/

function pollFirstCall(){
	 if(globalUserId>0){
		    var url= 'http://localhost:8078/HIS-WAR/api/getVideoataForNotification';
			var totalNotifications = 0;
			
			var data =
			{
			"unitId":208,
			"doctorId":356
			}
			
			$.ajax({
				type : "post",
				url : url,
				contentType : "application/json",
		        data : JSON.stringify(data),
				success : function(response) {
											  console.log(response);
											  var list=response.listObject;
											$.each(list,function(i, dto) {
												totalNotifications = totalNotifications + 1;
																/*var src = "";
																var twiliobaseUrlLocation = window.location.href;
																var twiliotemp = twiliobaseUrlLocation .split('/');
																var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length - 1],"");
																if (dto.patientPhoto != null) {
																	src = dto.patientPhoto;
																} else if (dto.genderId == 1) {
																	src = twilioBASE_URL_CALL+ "resources/assets/common/images/project-images/m.jpg";
																} else {
																	src = twilioBASE_URL_CALL+ "resources/assets/common/images/icon/patient.png";
																}
																var coverpage = twilioBASE_URL_CALL+ "/newCoverPageInTwilio/"+ dto.patientId
																		+ "/"+ dto.appointmentId+ "/"+ dto.appointmentStatusId
																		+ "/"+ dto.appointmentTypeId+ "/"+ dto.userChatDetailsId+ "";*/
																//$('#vedioCallAlert').modal();
																$('#twilioDoctorNotifyMsg').empty();
																var html = '<span class="patientDetailsMsg">'
																		+ dto.patientName
																		+ '</span> is ready and waiting in the room for the <span class="patientDetailsMsg">Teleconsultation appointment at '
																		+ dto.timeSlot 
																		+ '</span>';
																$(
																		'#twilioDoctorNotifyMsg')
																		.empty();
																$(
																		'#twilioDoctorNotifyMsg')
																		.append(html);
																$( '#twilioVideoPatientPhoto') .prop("src", src);
																$( '#twilioGoToCasesheet') .val(coverpage);
																$( '#twilioappointmentStatusId') .val( dto.appointmentStatusId);
																$( '#twilioappointmentId') .val(dto.appointmentId);
																$('#userChatDetailsId') .val( dto.userChatDetailsId);
															
					  });
					 $('#totalNotifications').text(totalNotifications);
				},
				error : function() {
					console.log("error");
					//stopLoader();
				}
			});
			 }
}
function changeStatusToPopupShown(){
	var userChatDetailsId =$('#userChatDetailsId') .val();
	var param = { "userChatDetailsId":userChatDetailsId};
	$.ajax({
		type : "post",
		url : "changeStatusofuserChatDetailsId",
		dataType : "json",
		contentType : "application/json",
		data : JSON.stringify(param),
		async : false,
		success : function(response) {
		},
		error : function() {
			console.log("error");
			//stopLoader();
		}
	});
}

function showTwilioNotification(){
	
	debugger;
	 // startLoader();
	 var twiliobaseUrlLocation = window.location.href;
		var twiliotemp = twiliobaseUrlLocation.split('/');
		var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length-1],"");
	 $('#twilioNotificationDiv').empty();
	 var param = {toUserId:globalUserId};
		$.ajax({
			url : "chatDetailsByUserTwilio",
			type : "post",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(param),
	        headers:getHeaders(),
			success : function(response){
				var status = response.status;
				var list = response.listObject;
				 $('#twilioNotificationDiv').empty();
				$.each(list,function(i,val){
					var src=twilioBASE_URL_CALL+ "resources/assets/common/images/project-images/default-pat.png";
					if(val.patientPhoto!=null){
						src=val.patientPhoto;
					}else if( val.genderId==1){
						 src=twilioBASE_URL_CALL+"resources/assets/common/images/project-images/m.jpg";
					}else{
						 src=twilioBASE_URL_CALL+"resources/assets/common/images/icon/patient.png";  
					}
					var checkinbutton="Join";
					if(val.appointmentStatusId==1){
						checkinbutton="Checkin and Join";
					}
					checkinbutton
					 var myvar = '<div class="row padding-top10">'+
					 '		<div class="notificationBoxContaint padding-top-bottom5"'+
					 '			style="padding-left: 0px !important; padding-right: 15px !important; padding-bottom: 10px;">'+
					 '			<div class="row padding-top-bottom5">'+
					 '				<div class="col-lg-1 col-md-1 col-sm-1">'+
					 '					<hr'+
					 '						style="margin-top: 7px !important; margin-bottom: 0px !important; border-top: 10px solid rgba(84, 249, 106, 1) !important; width: 10px; border-radius: 20px !important;">'+
					 '				</div>'+
					 '				<div class="col-lg-6 col-md-8 col-sm-8">'+
					 '					<span>Teleconsultation Alert</span>'+
					 '				</div>'+
					 '				<div class="col-lg-5 col-md-3 col-sm-3 text-right">'+
					 '					<span>'+val.dateTime+'</span>'+
					 '				</div>'+
					 '			</div>'+
					 '			<div class="row">'+
					 '				<div class="col-lg-3 col-md-3 col-sm-3">'+
					 '					<div class="row text-center" style="padding-top: 4px;">'+
					 '						<img'+
					 '							src="'+src+'"'+
					 '							style="width: 70px; height: 70px; border-radius: 50px;">'+
					 '					</div>'+
					 '				</div>'+
					 '				<div class="col-lg-9 col-md-9 col-sm-9 text-right">'+
					 '					<div class="row text-center patientDetailsMsgs"'+
					 '						style="font-size: 12px !important;">'+
					 '						<span class="patientDetailsMsg"'+
					 '							style="font-size: 12px !important;">' +val.patientName+
					 '							</span> is ready and waiting in the room for the'+
					 '						<span class="patientDetailsMsg"'+
					 '							style="font-size: 12px !important;">Teleconsultation'+
					 '							appointment at '+val.timeSlot+'</span>'+
					 '					</div>'+
					 '					<div class="row padding-top10">'+
					 '						<button type="button" class="btn btn-primary"'+
					 '							style="background-color: rgba(208, 222, 159, 1) !important; border-color: rgba(208, 222, 159, 1) !important; border-radius: 10px; min-width: 110px;">Reschedule</button>'+
					 '						<button type="button" class="btn btn-primary width-auto" style="border-radius: 10px;  min-width: 110px;" onclick="openCasesheet('+val.patientId + ','+ val.appointmentId + ','+ val.appointmentStatusId+','+ val.appointmentTypeId+','+ val.userChatDetailsId+');">'+checkinbutton+'</button>'+
					 '					</div>'+
					 '				</div>'+
					 '			</div>'+
					 '		</div>'+
					 '	</div>';
					 
					 $('#twilioNotificationDiv').append(myvar);
					 
				});
				if(list.length==0){
					 $('#twilioNotificationDiv').append('<div style="text-align: center;">No notification.</div>');
				}
				 // stopLoader();	 
			},error : function(){
				console.log("Error");
				 // stopLoader();
			}
		})
	
	
}
function openCasesheet(patientId,appointmentId,appointmentStatusId,appointmentTypeId,userChatDetailsId){
	 var twiliobaseUrlLocation = window.location.href;
		var twiliotemp = twiliobaseUrlLocation.split('/');
		var twilioBASE_URL_CALL = twiliobaseUrlLocation.replace(twiliotemp[twiliotemp.length-1],"");
	
	if(appointmentStatusId==1){
		var param = { "appointmentId":appointmentId};
		$.ajax({
			type : "post",
			url : "changeStatus",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(param),
			async : false,
	        headers:getHeaders(),
			success : function(response) {
				var url=$('#twilioGoToCasesheet').val();
				 window.location.href=url;
			},
			error : function() {
				console.log("error");
				//stopLoader();
			}
		});
	}else{
		var coverpage =twilioBASE_URL_CALL+ "/newCoverPageInTwilio/" + patientId + "/"+ appointmentId + "/" + appointmentStatusId+"/"+appointmentTypeId+"/"+userChatDetailsId+"";
		 window.location.href=coverpage;
	}
	 
}


