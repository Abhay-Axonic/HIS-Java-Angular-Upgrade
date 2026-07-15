angular
		.module('myApp')
		.controller(
				'bookAppointmentController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'PagerService','$location','$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$location,$stateParams) {

							$scope.webAppDetailObj = $stateParams.webAppDetailObj;
							var isEmpty=angular.equals('{}',$scope.webAppDetailObj);
                           if(isEmpty==true){
                        	   var orgUnitObj = JSON.parse(sessionStorage.getItem('webAppOrgUnitCookieObj'));
   							   if(orgUnitObj != undefined){
   								$state.go('/webAppointmentBooking/'+(parseInt(orgUnitObj.orgId))+"/"+(parseInt(orgUnitObj.unitId)));
   							  }	
							}

							console.log("$scope.webAppDetailObj ::",$scope.webAppDetailObj);
							$scope.organizationId = $scope.webAppDetailObj.organizationId;
							$scope.unitId = $scope.webAppDetailObj.unitId;
							$scope.doctorId = $scope.webAppDetailObj.doctorId;
							$scope.specialityId = $scope.webAppDetailObj.specialityId;
							$scope.userId= $scope.webAppDetailObj.userId;
							$scope.doctorName=$scope.webAppDetailObj.doctorName;
							$scope.specialityName=$scope.webAppDetailObj.specialityName;
							$scope.doctorImage=$scope.webAppDetailObj.doctorImage;
							$scope.unitAddress="";
							$scope.appCharge="";
							$scope.patientDetails={};
							$scope.patientAppObj={};
							$scope.patientAppObj.slotId=0;
							$scope.patientAppObj.patientId=0;
							$scope.patientAppObj.appointmentTime="";
							$scope.patientAppObj.appointmentDate=moment(new Date()).format("YYYY-MM-DD");
							$scope.isTeleconsultationAppointment=0;
							$scope.patientAppObj.systemDefiedVisitType=1;
							$scope.patientAppObj.patientCategoryId="";
							$scope.searchObj={};
							$scope.searchObj.uhidNumber="";
							
							$scope.loadingMsg="Loading...";
							$scope.doctorSlotList=[];
							$scope.slotCheckTime = moment(new Date()).format("HH:mm");
							
							$scope.slotDatePicker=moment(new Date()).format("DD-MM-YYYY");
							$scope.weekdays=[];
							$scope.weekdays[0] = "Sun";
							$scope.weekdays[1] = "Mon";
							$scope.weekdays[2] = "Tue";
							$scope.weekdays[3] = "Wed";
							$scope.weekdays[4] = "Thu";
							$scope.weekdays[5] = "Fri";
							$scope.weekdays[6] = "Sat";
							$scope.mondayOfWeek={'weekDate':''};
							$scope.sundayOfWeek={'weekDate':''};
							$scope.monthNames = ["January", "February", "March", "April", "May", "June",
								  "July", "August", "September", "October", "November", "December"
								];
							$scope.selectedUnitId="0";
							$scope.selSpecId="0";
							$scope.currentDateForSlot=moment(new Date()).format("DD-MM-YYYY");
							
							$scope.openDatePickerDOB = function() {
								$scope.datepickerDOB.opened = true;
							};

							$scope.datepickerDOB = {
								opened : false
							};
							
							$scope.dateOptionsDob = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							
							$scope.slotDateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							$scope.openSlotDatePicker = function() {
								$scope.slotDatePicker.opened = true;
							};

							$scope.slotDatePicker = {
								opened : false
							};
							$scope.slotDatePicker=new Date();
							
							$scope.docSpecObj={
									"docSpecList":[],
							};
							
							$scope.webAppDetObj={
									"unitDetList":[],
									"doctorDto":{},
									"doctorDegrees":"",
									"orderMasterDto":{},
									};
							
						 function isEmpty(obj) {
						  	    for(var key in obj) {
						  	        if(obj.hasOwnProperty(key))
						  	            return true;
						  	    }
						  	    return false;
						  	}
							 
					 $scope.prefixList = [];
					 $scope.genderList =[];
					 var URI = BASE_URL + ROOT_URL+ "/api/webappointment/getActivePrefixList/"+$scope.organizationId;
					 GenericService.serviceAction(METHOD_GET, URI, {})
							.then(
									function(response) {
										console.log(response);
										if (response.data.status == "success")
											$scope.prefixList = response.data.listObject;
									});
					 $scope.applicationConfig = {};
					 $scope.reqObjAppConfig={};
					 $scope.reqObjAppConfig.organizationId=$scope.organizationId;
					 $scope.reqObjAppConfig.unitId=$scope.unitId;
					 var URI = BASE_URL + ROOT_URL+ "/api/doctorApp/getApplicationConfig/";
					 GenericService.serviceAction("POST", URI, $scope.reqObjAppConfig)
							.then(
									function(response) {
										console.log(response);
										if (response.data.status == "success")
											 $scope.applicationConfig = response.data.object;
									});
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEGENDERLIST;
				GenericService.serviceAction(METHOD_GET, URI, {})
						.then(
								function(response) {
									console.log(response);
									$scope.genderList = [];
									if (response.data.status == "success")
										$scope.genderList = response.data.listObject;
								});
							$scope.getDetailsForBookAppointment=function(){
								var reqObj={
										"organizationId":$scope.organizationId,
										"unitId":$scope.unitId,
										"doctorId":$scope.doctorId,
										"userId":$scope.userId,
										"specialityId":$scope.specialityId,
										"appointmentDate":moment(new Date()).format('DD-MM-YYYY'),
										
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/webappointment/getDetailsForBookAppointment";
								GenericService.serviceAction(METHOD_POST, URI, reqObj).then(function(response) {
													console.log(response);
													if (response.data.status == "success") {
														$scope.webAppDetObj= response.data.object;
														$scope.appCharge=$scope.webAppDetObj.orderMasterDto.listOrderDetailsMasterDto[0].netAmount;
														var obj=$.grep($scope.webAppDetObj.unitDetList, function(unit){ return unit.unitId==$scope.unitId})[0];
														$scope.unitAddress=obj.unitAddress;
														$scope.selectedUnitId=$scope.unitId;
													}

												});
							}
							$scope.getDetailsForBookAppointment();
						
						
						$scope.areaOfExpertise="";
						var specDocObj = {
					    			organizationId:$scope.organizationId,
					    			doctorId:$scope.doctorId
					    }
					    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByDoctorIdByOrg";
				      GenericService.serviceAction(METHOD_POST, URI,specDocObj)
						.then(function(response) {
									console.log(response);
									if (response.data.status == "success")
										 
										$scope.docSpecObj.docSpecList=response.data.listObject;
									    $scope.docSpecialityList=response.data.listObject;;
									    $scope.selSpecId= $scope.webAppDetailObj.specialityId;
									    
									    if($scope.docSpecialityList.length>0){
									    	for(var i=0;i<$scope.docSpecialityList.length;i++){
									    		if(i!=($scope.docSpecialityList.length-1)){
									    			$scope.areaOfExpertise=$scope.areaOfExpertise+$scope.docSpecialityList[i].specialityName+", ";
									    		}else{
									    			$scope.areaOfExpertise=$scope.areaOfExpertise+$scope.docSpecialityList[i].specialityName;
									    		}
									    	}
									    }
	                                 });
				      
							
							$scope.getDoctorSlots=function(){
								$scope.doctorSlotList=[];
								$scope.loadingMsg="Loading...";
								var reqObj={};
								reqObj.organizationId=$scope.organizationId;
								reqObj.unitId=$scope.unitId;
								reqObj.slotTypeId=1;
								reqObj.doctorId=$scope.doctorId;
								reqObj.specialityId=$scope.specialityId;
								reqObj.appointmentDate=$scope.patientAppObj.appointmentDate;
								
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								GenericService.serviceAction(METHOD_POST, URI, reqObj).then(function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success") {
														$scope.doctorSlotList= response.data.listObject;
														console.log($scope.doctorSlotList);
												       if($scope.doctorSlotList.length ==  0){
												    	   $scope.loadingMsg="No slots available";
													  }
													}else{
														$scope.loadingMsg="No slots available";
													}

												});
							}
							
							$scope.getDoctorSlots();
							
							$scope.getChangedUnitData=function(){
							  $scope.unitId=$scope.selectedUnitId;	
							  $scope.patientAppObj.slotId=0;
							  $scope.getDoctorSlots();
							  var obj=$.grep($scope.webAppDetObj.unitDetList, function(unit){ return unit.unitId==$scope.unitId})[0];
							  $scope.unitAddress=obj.unitAddress;
							}
							
							$scope.getChangedSpecialityData=function(){
								  $scope.specialityId=$scope.selSpecId;	
								  $scope.patientAppObj.slotId=0;
								  $scope.getDoctorSlots();
								}
							
							function formatHour12(hour, minute) {
								console.log(hour + ":" + minute);
								if (hour == 23 && minute == 59)
									return "00:00";
								var hour12 = hour;
								if (hour12 == 0) {
									hour12 = 12;
								}
								hour12 = hour12 < 10 ? "0" + hour12 : hour12;

								return hour12 + ":" + formatTime(minute);
							}

							function formatTime(timeObj) {
								return timeObj < 10 ? "0" + timeObj : timeObj;
							}

							function getMonday(d) {
								  var d = new Date(d);
								  var day = d.getDay(),
								      diff = d.getDate() - day + (day == 0 ? -6:1); // adjust when day is sunday
								  return new Date(d.setDate(diff));
								}

							function getSunday(d) {
								  var d = new Date(d);
								  var day = d.getDay(),
								  diff  = (d.getDate() - d.getDay() +7);
								 return new Date(d.setDate(diff));
								}
							
							
							$scope.getWeekCalendar=function(obj,flag)
							{
								$scope.weekArray=[];
                                var date=obj.weekDate;
								$('#month').html($scope.monthNames[date.getMonth()]);
								var monday = getMonday(date);
								var sunday = getSunday(date);
								$scope.mondayOfWeek.weekDate=monday;
								$scope.sundayOfWeek.weekDate=sunday;
							
								for (i = 0; i < 7; i++) {
									var weekDate=  moment(monday).add(i, 'day').toDate();
									var weekObj={};
									weekObj.weekDate=weekDate;
									weekObj.style="";
									if(!flag && weekDate.getDate()==new Date().getDate())
										weekObj.style = 'selected';
									else if(flag && weekDate.getDate()==date.getDate())
										weekObj.style = 'selected';
									else 
										weekObj.style = '';
									$scope.weekArray.push(weekObj);
									}
							}
							
							var currDateObj={'weekDate':new Date()};
							$scope.getWeekCalendar(currDateObj,true);

							$scope.getPreviousWeek=function(obj)
							{
								var date=obj.weekDate;
								var d = moment(date).subtract(1, 'day').toDate();
								var dateObj={'weekDate':d};
								$scope.getWeekCalendar(dateObj,false);
							}
							$scope.getNextWeek=function(obj)
							{
								var date=obj.weekDate;
								var d = moment(date).add(1, 'day').toDate();
								var dateObj={'weekDate':d};
								$scope.getWeekCalendar(dateObj,false);
							}

							  $scope.getSlotByDate=function(obj)
								{
								        
								        var date=obj.weekDate;
								        $scope.slotDatePicker=date;
									    $('#weekDaysRow > th.thstyle').find('.radio').removeClass('selected');
									    $("#"+date.getTime()).addClass('selected');
										//var d = new Date(parseInt(date))
										var appDate=date.getFullYear() + "-" + (date.getMonth()<=8?"0"+(date.getMonth()+1):date.getMonth()+1) + "-" 
										+(date.getDate()<=9?"0"+date.getDate(): date.getDate());
										$scope.patientAppObj.appointmentDate=appDate;
										$scope.getDoctorSlots();
								} 
							  
							  $scope.onDatePickerDateChange=function(){
									$scope.patientAppObj.appointmentDate=moment($scope.slotDatePicker).format('YYYY-MM-DD');
									var dateObj={'weekDate':new Date($scope.patientAppObj.appointmentDate)};
									$scope.getWeekCalendar(dateObj,true);
									$scope.getDoctorSlots();
								}
						
							$scope.currentTime = moment(new Date()).format("HH:mm");  
							$scope.checkDateTime = function(slotObj)
								{
								if($scope.currentDateForSlot==moment(slotObj.appointmentDate2).format('DD-MM-YYYY')){
									  if(slotObj.originalFromTime>$scope.currentTime)
										 return true;
									  else
										  return false;
								  }else{
									 return true;
								  }
									
							} 
						
							$scope.checkDateIsBefore = function(slobObj)
							{
							     var endTime = moment(slobObj.originalFromTime, 'hh:mm');
							     var startTempTime = moment().format("HH:mm");
							     var startTime = moment(startTempTime, 'hh:mm');
							     var slotColor="";
							     if(moment(slobObj.appointmentDate2).format('DD-MM-YYYY')<$scope.currentDateForSlot){
							    	    $("#"+$scope.patientAppObj.slotId).attr('ng-click','');
										return 'bg-grey cursor-pointer day-slot-div';
									} 
								if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > 0 && slobObj.slotPatientDetailsListsSize < slobObj.allowedOverBookingSlots)
									{
										//return 'bg-red1 cursor-pointer day-slot-div';
									 slotColor= 'bg-orange cursor-pointer day-slot-div';
									}
								/*if(slobObj.isBlockedUnblocked == '2' && slobObj.allowedOverBookingSlots>0 && slobObj.slotPatientDetailsListsSize == slobObj.allowedOverBookingSlots)
									{
										//return 'bg-orange cursor-pointer day-slot-div';
										return 'bg-red1 cursor-pointer day-slot-div';
									}*/
								if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize == '0')
								{
									slotColor= 'bg-green cursor-pointer day-slot-div';
								}
								if($scope.patientAppObj.slotId==slobObj.slotId){
									$('#slotDiv > div').find('.slotBtn').removeClass('selected');
								    $("#"+$scope.patientAppObj.slotId).addClass('selected');
								    return "bg-brown cursor-pointer day-slot-div";
								}else{
									return slotColor;
								}
							}
							
							
							$scope.calculateAge= function()
							{
								$('#searchServiceId').val('');
								if($scope.datepickerDOB == undefined || $scope.datepickerDOB == null || $scope.datepickerDOB == ''){
									$scope.patientDetails.age  = "";
									   growl.warning("Date is invalid",{
											title : "warning"
										});
									   $scope.datepickerDOB = new Date();
									
									   return false;
								}else{
									if($scope.datepickerDOB > new Date())
									{
										$scope.patientDetails.age = "";
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepickerDOB = new Date();
												
												   return false;
									}else{
								var dt = moment($scope.datepickerDOB).format('MM/DD/YYYY');
								$scope.patientDetails.age = $rootScope.getAge(dt);
								
								if($scope.selectedSlotId == '3')
									{
									var data ={
											orgId:$scope.organizationId,
											unitId : $scope.unitId,
											age : $scope.patientDetails.age,
											sexId : $scope.patientDetails.genderId
									}
									
									var URI = BASE_URL + ROOT_URL + "/api/packages/ehcPackages";
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														$scope.packageEHCList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success") {
															$scope.packageEHCList = response.data.listObject;
															console.log($scope.packageEHCList);
														}

													});
									}
								}
							}
								
							}
							
							
							$scope.selectSlotFun=function(obj){
								$scope.patientAppObj.slotId=obj.slotId;
								$scope.patientAppObj.appointmentTime=obj.fromTimeString2;
								$scope.patientAppObj.originalFromTime=obj.fromTimeString;
								$scope.patientAppObj.originalToTime=obj.toTimeString;
							}
							
							$scope.listPaymentDetails=[];
							$scope.confirmBooking=function(){
                              $scope.$broadcast('show-errors-check-validity');
								
                                if($scope.patientAppObj.slotId==0){
                                	 growl.warning("Select slot",
     										{
     											title : 'Warning!'
     										});
                                 
                                	 return false;
                                }
                                
								if (!$scope.modalityForm.$valid)
								{ 
									//$scope.showModal = !$scope.showModal;
								} else{
										
										//Appointment Object
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');
										
										
										var dataAppointmentObj = {
												patientId :$scope.patientAppObj.patientId,
												remark : $scope.patientDetails.remark,
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												createdDate : $scope.currentDate,
												updatedDate : $scope.currentDate,
												createdBy : $scope.userId,
												updatedBy : $scope.userId,
												status : 'A',
												appointmentTypeId : "1",
												priorityId :"1",
												slotId :$scope.patientAppObj.slotId,
										        appointmentDate2 :moment($scope.patientAppObj.appointmentDate).format('DD/MM/YYYY'),
										        fromTimeString : $scope.patientAppObj.appointmentTime,
										        appointmentDate :  moment($scope.patientAppObj.appointmentDate).format('YYYY-MM-DD'),
										        slotStatusId : 4,
										        isFromEncounter: 'N',
											}
										
										
										var isFromKiosk = null;
										$scope.currentDate = new Date();
										$scope.currentDate = moment($scope.currentDate)
												.format('DD-MM-YYYY HH:mm:ss');
										$rootScope.startLoader();
										var data = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												doctorId : $scope.doctorId,
												encounterDate : new Date().getTime(),
												isReferal : 'N',
												deptId : $scope.specialityId,
												paymentEntitlementTypeId : 1, //self
												createdBy : $scope.userId,
												createdDate : new Date().getTime(),
												updatedDate : new Date().getTime(),
												updatedBy : $scope.userId,
												status : 'A',
												defaultSelfTariffId : $scope.defaultSelfTariffId,
												isScheduledAppointment:'A',
												specialityId : $scope.specialityId,
												encounterTypeId: 1,
												isAppointmentRequired : 'Y',
												//counterId : 1, ask vishal
												currentDate :  moment(new Date()).format('YYYY-MM-DD'),
												patientCategoryId : $scope.patientAppObj.patientCategoryId,
												totalBillAmt : $scope.appCharge,
												paidAmount : $scope.appCharge,
												systemGeneratedVisitTypeId:$scope.patientAppObj.systemDefiedVisitType ,
												userDefinedVisitTypeId:$scope.patientAppObj.systemDefiedVisitType ,
												isNursingAssessmentRequiredForSpeciality : $scope.patientAppObj.isNursingAssessmentRequired,
												listKinDetailsDto : [],
												patientSponsorDetailsDtosList : [{
													unitId : $scope.unitId,
													organizationId : $scope.organizationId,
													visitTypeId : 1,
													paymentEntitlementId : 1,
													payeeId : $scope.selfPayeeId ,
													priorityId : 1,
													createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													createdBy : $scope.userId,
													updatedBy : $scope.userId,
													status : 'A',
													tariffId : $scope.defaultSelfTariffId,
												}],
												taxAmt : 0,
												serviceAmt : $scope.appCharge,
												billingBedCategoryId : $scope.opdBillingCatId,	 				
												payeeId : $scope.selfPayeeId,
												isPreOrPostBilling : $scope.isPreOrPostBilling,
												deptWiseAppointment: 'N'  ,
												paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
												isTransferToActiveDeposit : $scope.appCharge,
												registrationTypeId : $scope.patientAppObj.typeId
										}
										
										if($scope.patientAppObj.patientId>0)
											{
												data.patientId = $scope.patientAppObj.patientId;
											}else{
													data.patientId = 0,
													data.prefixId = $scope.patientDetails.prefixId,
													data.firstName = $scope.patientDetails.firstName, 
													data.middleName = $scope.patientDetails.middleName,
													data.lastName = $scope.patientDetails.lastName,
													data.genderId = $scope.patientDetails.genderId,
													data.countryCallingCode = "+91",
													data.registrationTypeId = 5,
													data.mobileNumber = $scope.patientDetails.mobileNumber,
													data.dob = moment($scope.datepickerDOB).format('DD-MM-YYYY'),
													data.patientCategoryId = $scope.patientDetails.patientCategoryId,
													data.email = $scope.patientDetails.email
												}
										
										if($scope.patientAppObj.appointmentId > 0)
											{
											  data.appointmentId = $scope.patientAppoObjPayment.appointmentId
											}
										else
											{
												//dataAppointmentObj.generateToken  = 'Y';
											    $scope.flag=false;
												$scope.slotCheckTimeAddOneHours = moment(new Date()).add(1, 'hours').format("HH:mm");
												if(moment($scope.patientAppObj.appointmentDate).format('YYYY-MM-DD')==moment(new Date()).format('YYYY-MM-DD') && $scope.slotCheckTimeAddOneHours >= $scope.patientAppObj.originalFromTime)
												{
													 $scope.flag=true;
													dataAppointmentObj.generateToken  = 'Y';
													dataAppointmentObj.arrivalTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													dataAppointmentObj.isPaidAppointment = 'Y';
												}
												else{
													dataAppointmentObj.generateToken  = 'N';
													data.isPaidAppointment = 'Y';
													dataAppointmentObj.isPaidAppointment = 'Y';
												}
											
												data.appointmentObject = dataAppointmentObj;
											}
										var URI = null;
										
										if($scope.patientAppObj.isPaidAppointment == undefined || $scope.patientAppObj.isPaidAppointment == 'N' || (moment($scope.patientAppObj.appointmentDate).format('YYYY-MM-DD')==moment(new Date()).format('YYYY-MM-DD') && $scope.slotCheckTimeAddOneHours >= $scope.patientAppObj.originalFromTime))
											URI = BASE_URL + ROOT_URL + "/api/billing/kiosk/saveAndPay";
										else
											URI = BASE_URL + ROOT_URL + "/api/billing/kiosk/saveAndPay/future";
										
										console.log('SavaAndPay',data);
										//return false;
										GenericService.serviceAction(METHOD_POST,
												URI, data)
										.then(
												function(response) {
													console
															.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success")
													{
														
														$rootScope.stopLoader();
														
													}
													else if (response.data.code == 409) {
														
														$scope.appointmentAlreadyExistMsg = response.data.message;
														angular.element('#alreadyExistAppointment').modal('show');
														$rootScope.stopLoader();
														return false;
														
													}
												});
							
								
								}
							}
							
							$scope.openPatientSearchModal=function(){
								angular.element('#patientSearchModal').modal('show');
								$scope.searchObj.uhidNumber="";
								$scope.loadingMsg="";
								$scope.searchPatList=[];
							}
							
							
							//Search patient by uhid
							$scope.searchLoadingMsg="";
							$scope.searchPatList=[];
							$scope.searchPatientByUhid=function(){
								if($scope.searchObj.uhidNumber==""){
									 growl.warning("Enter uhid number",
	  										{
	  											title : 'Warning!'
	  										});
									 return false;
								}
								$scope.searchLoadingMsg="Loading...";
								$scope.searchPatList=[];
								var data={
										"uhidNumber":$scope.searchObj.uhidNumber
								     }
								
								var URI = BASE_URL + ROOT_URL + "/api/webappointment/getPatientDetailsByUhid?uhidNumber="+$scope.searchObj.uhidNumber+"&orgId="+$scope.organizationId;
						       GenericService.serviceAction(METHOD_GET, URI,{}).then(
								function(response) {
									console.log(response);
									//$rootScope.startLoader();
									if (response.data.status == "success") {
										$scope.searchPatList=response.data.listObject;
										if($scope.searchPatList.length==0){
											$scope.searchLoadingMsg="No records found";
										}
									}
									

								});
								
							}
							
							
							$scope.disablePatForm=false;
							//get all details of patient and set data to patientdetail object
							$scope.fetchAndFillPatDetail=function(obj){
								$scope.disablePatForm=true;
								angular.element('#patientSearchModal').modal('hide');
								angular.element('#patientAlreadyExist').modal('hide');
								$scope.patientAppObj.patientId=obj.patientId;
								$scope.patientDetails.prefixId=obj.prefixId;
								$scope.patientDetails.firstName=obj.firstName;
								$scope.patientDetails.lastName=obj.lastName;
								$scope.patientDetails.middleName=obj.middleName;
								$scope.patientDetails.genderId=obj.genderId;
								var dob=moment(obj.birthDate).format("YYYY-MM-DD");
								$scope.datepickerDOB=moment(new Date(dob)).toDate();
								$scope.patientDetails.age=obj.ageString;
								$scope.patientDetails.mobileNumber=obj.mobileNumber;
								$scope.patientDetails.email=obj.email;
								 $scope.patientAppObj.patientCategoryId=obj.patientCategoryId;
								//$scope.patientDetails.remark=obj.email;
								
								//check patient is applicable for followup
								
								  var reqObj={
								    "currentDate": $scope.patientAppObj.appointmentDate,//"2021-05-27",
									"doctorId": $scope.doctorId,
									"organizationId": $scope.organizationId,
									"patientId": $scope.patientAppObj.patientId,
									"specialityId": $scope.specialityId,
									"unitId": $scope.unitId
									}
							   var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
							   GenericService.serviceAction(METHOD_POST,URI, reqObj).then(function(response)
							     {
							    	 $scope.patientAppObj.systemDefiedVisitType=response.data.toString();
							    	 $scope.userDefinedVisitTypeId=response.data.toString();
							    	 $scope.getServiceRates();
							     });
							  
							}
							
							
	                        //get service rates
								$scope.getServiceRates=function(){
									$scope.patientIdForReport = $scope.patientAppObj.patientId==undefined ? null:$scope.patientAppObj.patientId;
									$rootScope.startLoader();
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
									var data = {
									        unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											patientId : ($scope.patientAppObj.patientId!=undefined ? $scope.patientAppObj.patientId : null),
											doctorId : $scope.doctorId ,
											specialityId : $scope.specialityId,
											currentDate :  moment(new Date()).format('YYYY-MM-DD'),
											patientCategoryId : $scope.patientAppObj.patientCategoryId!=undefined ? $scope.patientAppObj.patientCategoryId:$scope.defaultPatientCategoryId,
											deptWiseAppointment : 'N',
											userDefinedVisitTypeId	:  $scope.patientAppObj.systemDefiedVisitType
													
									}
									var URI1= BASE_URL + ROOT_URL + "/api/opd/billAmount/appointment"
									GenericService.serviceAction(METHOD_POST,URI1, data)
											.then(
													function(response1) {
														console.log(response1);
														if (response1.data.status == "success")
														{
															$scope.appCharge= response1.data.object.listOrderDetailsMasterDto[0].netAmount;
														}else{
															angular.element('#consultationConfigNotExistPre').modal('show');
															return false;
														}
														$rootScope.stopLoader();
													});
														
							
								}
									
								
							$scope.resetDetails=function(){
								$scope.patientAppObj.systemDefiedVisitType=1;
								$scope.patientAppObj.patientId=0;
								$scope.disablePatForm=false;
								$scope.patientDetails.prefixId="";
								$scope.patientDetails.firstName="";
								$scope.patientDetails.lastName="";
								$scope.patientDetails.middleName="";
								$scope.patientDetails.genderId="";
								$scope.datepickerDOB="";
								$scope.patientDetails.age="";
								$scope.patientDetails.mobileNumber="";
								$scope.patientDetails.email="";
								$scope.patientDetails.remark="";
							}
							
							$scope.isRequestForPay=false;
							$scope.openPaymentPage=function(){
								$scope.isRequestForPay=true;
								if($scope.isAppointmentBooked==false){
								  $scope.bookNormalAppointment();
								}else{
									$scope.goToRazorPay();
								}
							}
							
							$scope.goToRazorPay=function(){
								var data = {
								        unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										amount:$scope.webAppConfDetObj.visitFee,
										razorPayBillAmount:$scope.webAppConfDetObj.visitFee+"00",
										appointmentId:$scope.webAppConfDetObj.appointementId,
										visitTypeId:$scope.webAppConfDetObj.visitTypeId
								}
								$rootScope.startLoader();
								var URI1= BASE_URL + ROOT_URL + "/getOrderIdForRazorPayPayment";//api/global/
								GenericService.serviceAction(METHOD_POST,URI1, data)
										.then(
												function(response1) {
													console.log(response1);
													if (response1.data.status == "success")
													{
														$scope.orderId= response1.data.object.orderId;
													}else{
														growl.error(response1.data.message,
																{
																	title : 'Error!'
																});
													}
													$rootScope.stopLoader();
													var options = {
														    "key": $scope.applicationConfig.razorPayKeyId, // Enter the Key ID generated from the Dashboard
														    "amount": $scope.webAppConfDetObj.visitFee+"00",// Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
														    "currency": "INR",
														    "name": "Clinivantage Healthcare",
														    "description": "Test Transaction",
														    "image": "https://example.com/your_logo",
														    "order_id": $scope.orderId,//"order_9A33XWu170gUtm", //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
														    "callback_url": BASE_URL + ROOT_URL + "/confirmAppointment",///api/global/
														    "prefill": {
														        "name": $scope.webAppConfDetObj.patientName,
														        "email": $scope.webAppConfDetObj.emailId,
														        "contact": $scope.webAppConfDetObj.mobileNumber
														    },
														    "notes": {
														        "address": "Razorpay Corporate Office"
														    },
														    "theme": {
														        "color": "#3399cc"
														    }
														};
														var rzp1 = new Razorpay(options);
														//document.getElementById('rzp-button1').onclick = function(e){
														    rzp1.open();
														    e.preventDefault();
														//}
													$rootScope.stopLoader();
												});
							}
							
							//Normal Appointment
							$scope.patientAvailList=[];
							$scope.isAppointmentBooked=false;
							$scope.bookNormalAppointment=function(){
								   $scope.patientAvailList=[];
								   $scope.$broadcast('show-errors-check-validity');
									
	                                if($scope.patientAppObj.slotId==0){
	                                	 growl.warning("Select slot",
	     										{
	     											title : 'Warning!'
	     										});
	                                 
	                                	 return false;
	                                }
	                                
	                                
									if (!$scope.modalityForm.$valid)
									{ 
										//$scope.showModal = !$scope.showModal;
									} else{
										$rootScope.startLoader();

										
										  $scope.patientObj={
												  "organizationId":$scope.organizationId,
												  "genderId":$scope.patientDetails.genderId,
												  "dob":moment($scope.datepickerDOB).format('DD-MM-YYYY HH:mm:ss'),
												  "mobileNumber":$scope.patientDetails.mobileNumber,
												  "registrationTypeId":1
										  }
										 var URI = BASE_URL + ROOT_URL
									 		+ "/api/global/patient/unique";

									 	GenericService.serviceAction(METHOD_POST,URI, $scope.patientObj)
									 		.then(
									 				function(response) {
									 					console.log(response);
									 					//$rootScope.startLoader();
									 					if (response.data.status == "success")
									 					{
									 						if(response.data.listObject.length>0 && $scope.patientAppObj.patientId==0)
									 							{
									 								$rootScope.stopLoader();
									 								$scope.patientAvailList=response.data.listObject;
									 								angular.element('#patientAlreadyExist').modal('show');
									 							}
									 						else{
									 							$scope.currentDate = new Date();
																$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
															     var data = {
																	slotId :$scope.patientAppObj.slotId,
																	remark : $scope.patientDetails.remark,
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	createdDate : $scope.currentDate,
																	updatedDate : $scope.currentDate,
																	createdBy : $scope.userId,
																	updatedBy : $scope.userId,
																	status : 'A',
																	appointmentTypeId :1,
																	slotStatusId : 4,
																	priorityId :"1",
																	//allowedOverBookingSlots : $scope.allowedOverBookingSlots,
																	firstName : $scope.patientDetails.firstName,
																    mobileNumber :$scope.patientDetails.mobileNumber,
																    doctorName :$scope.doctorName,
																    fromTimeString : $scope.patientAppObj.originalFromTime,
																    appointmentDate2 : moment($scope.patientAppObj.appointmentDate).format("DD/MM/YYYY"),
																    doctorId : $scope.doctorId,
																    specialityId : $scope.specialityId,
																    isTeleconsultationAppointment:$scope.isTeleconsultationAppointment,
																    specialityName: $scope.specialityName 
																}
																
																if($scope.isKiosk == 'N')							
																{
																	data.generateToken = 'Y';
																}
																
																if($scope.patientAppObj.patientId ==0)
																	{
																		data.prefixId = $scope.patientDetails.prefixId;
																		data.firstName = $scope.patientDetails.firstName;
																		data.middleName = $scope.patientDetails.middleName;
																		data.lastName =$scope.patientDetails.lastName;
																		data.dob =moment($scope.datepickerDOB).format('DD-MM-YYYY HH:mm:ss');
																		data.genderId =$scope.patientDetails.genderId;
																		data.mobileNumber =$scope.patientDetails.mobileNumber;
																		data.email =$scope.patientDetails.email;
																		data.registrationTypeId = 1;
																		data.patientId = 0;
																		data.patientCategoryId=$scope.applicationConfig.defaultPatientCategoryId;
																	}else{
																		data.patientId=$scope.patientAppObj.patientId;
																	}
							                                       
																$scope.webAppConfDetObj={};
																$scope.webAppConfDetObj.unitAddress=$scope.unitAddress;
																$scope.webAppConfDetObj.areaOfExperty=$scope.areaOfExpertise;
																$scope.webAppConfDetObj.doctorDegrees=$scope.webAppDetObj.doctorDegrees;
																$scope.webAppConfDetObj.doctorImage=$scope.webAppDetObj.doctorImage;
																$scope.webAppConfDetObj.doctorName=$scope.doctorName;
																$scope.webAppConfDetObj.appointmentDate=$scope.patientAppObj.appointmentDate;
																$scope.webAppConfDetObj.fromTime=$scope.patientAppObj.originalFromTime;
																$scope.webAppConfDetObj.toTime=$scope.patientAppObj.originalToTime;
																$scope.webAppConfDetObj.visitFee=$scope.appCharge;
																$scope.webAppConfDetObj.patientName=$scope.patientDetails.firstName+" "+$scope.patientDetails.lastName;
																$scope.webAppConfDetObj.mobileNumber=$scope.patientDetails.mobileNumber;
																$scope.webAppConfDetObj.emailId=$scope.patientDetails.email;
																$scope.webAppConfDetObj.appointmentReason=$scope.patientDetails.remark;
																$scope.webAppConfDetObj.consultingSpeciality=$scope.specialityName;
																$scope.webAppConfDetObj.isVideoApp=$scope.isTeleconsultationAppointment;
																$scope.webAppConfDetObj.visitTypeId=$scope.patientAppObj.systemDefiedVisitType;
																
																
																	var URI = BASE_URL + ROOT_URL+ SCHEDULEAPPOINTMENT;
																
																	GenericService.serviceAction(METHOD_POST, URI,data).then(
																			function(response) {
																				console.log(response);
																				if (response.data.status == "success") {
																					growl.success("Appointment Booked Successfully",
																							{
																								title : 'Success!'
																							});
																					$scope.isAppointmentBooked=true;
																					var appBookResObj=response.data.object;
																					$scope.webAppConfDetObj.appointementId=appBookResObj.appointmentId;
																					$rootScope.stopLoader();
																					if($scope.isRequestForPay){
																						$scope.goToRazorPay();
																					}
																					else{
																						  $state.go('webAppointmentBookingConfirmation',
																									{
																								     webAppConfirmationObj: $scope.webAppConfDetObj
																									});
																						
																					}
																				}
																				

																			});
									 						}  
									 					}
									 				});
										 
									
									}
							}
							
							$scope.showDOBByAge = function(age) {
								
								 if($('#datepickerDOB').val()!=""){
										var currentYear = (new Date()).getFullYear();
									    var finalYear = currentYear - age;		
									    $scope.datepickerDOB = new Date("01/01/"+finalYear);	
									    $('#searchServiceId').val('');
									}else{
										var currentYear = (new Date()).getFullYear();
									    var finalYear = currentYear - age;
									    $scope.datepickerDOB = new Date("01/01/"+finalYear);
									    $('#searchServiceId').val('');
									}
							}
							
						} ]);
