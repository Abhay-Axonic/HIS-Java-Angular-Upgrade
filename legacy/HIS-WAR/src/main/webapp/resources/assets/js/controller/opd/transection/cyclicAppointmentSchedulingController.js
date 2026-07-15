'use strict';

angular
		.module('myApp')
		.controller(
				'cyclicAppointmentSchedulingController',
				[
					'$scope',
					'$http',
					'$localStorage',
					'$sessionStorage',
					'$cookies',
					'$rootScope',
					'GenericService',
					'BillingGenericService',
					'growl',
					'$state',
					'$stateParams','$filter',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								$stateParams,$modalInstance,$filter) {

							//alert("appointmentListController");

							
							/* init() function for form object create. */
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								if(cookieObjectAppConfig == undefined){
									$state.go('applicationConfiguration');
									return;
								}
								else
									{
								 	console.log('cookieObjectAppConfig',cookieObjectAppConfig);
									}
								
								if(cookieObjectAppConfig.morningFromTime==null){
									$scope.docMorningStartTime = "00:00";
								}else{
									$scope.docMorningStartTime = cookieObjectAppConfig.morningFromTime;
								}
                              if(cookieObjectAppConfig.morningToTime==null){
                            	  $scope.docMorningEndTime = "13:30";
								}else{
									$scope.docMorningEndTime=cookieObjectAppConfig.morningToTime;
								}
                              if(cookieObjectAppConfig.eveningFromTime==null){
                            	  $scope.docEveningStartTime = "13:30";
								}else{
									 $scope.docEveningStartTime =cookieObjectAppConfig.eveningFromTime;
								}
                              if(cookieObjectAppConfig.eveningToTime==null){
                            	  $scope.docEveningEndTime = "23:59";
							   }else{
								   $scope.docEveningEndTime = cookieObjectAppConfig.eveningToTime;
								}
                              	$scope.startDateFormat = cookieObjectAppConfig.startDateFormat;
                                $rootScope.getSexMasterList();
                                $rootScope.getIdentificationTypeList();
                                
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								$scope.isSplitBillUnitFlag = cookieObjectAppConfig.isServiceWiseSplitBill;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
								
								//$scope.tokenList = [];
								$rootScope.loginpage = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
								};
								$scope.dateOptionsCyclic = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
								};

								
								$scope.openDatepickerFrom = function() {
									$scope.datepickerFrom.opened = true;
								};
								
								$scope.datepickerFrom = {
										opened : false
									};
								
								$scope.openDatepickerFromS = function() {
									$scope.datepickerFromS.opened = true;
								};
								
								$scope.datepickerFromS = {
										opened : false
									};
								$scope.dateOptions2 = {
										formatYear : 'yyyy',
										showWeeks : false
									};

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								
								$scope.datePickerService = {
										opened : [ false ],

								};
								
								$scope.openDatePickerForService = function(index) {
									for (var i = $scope.serviceSlotListForReschedule.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datePickerService.opened[i] = true;
										} else {
											$scope.datePickerService.opened[i] = false;
										}
									}
								};
								
								$scope.openDatePickerForServiceSave = function(index) {
									for (var i = $scope.setCreatedSlotListForServiceList.length - 1; i >= 0; i--) {
										if (i === index) {
											$scope.datePickerService.opened[i] = true;
										} else {
											$scope.datePickerService.opened[i] = false;
										}
									}
									angular.forEach($scope.setCreatedSlotListForServiceList,function(value){
										value.dateOptionservice = {
											formatYear : 'yyyy',
											showWeeks : false,
											minDate : new Date()
									};
									});
								};
								
								// function for initializa clockpicker
								$scope.initClockPicker = function() {
									$('.clockpicker').clockpicker();
								}
								//$scope.datepicker =  moment(new Date()).format('DD/MM/YYYY');
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.unitCountryId = cookieObject.unitCountryId;
								$scope.unitStateId = cookieObject.unitStateId;
								$scope.unitDistrictId = cookieObject.unitDistrictId;
								$scope.unitCityId = cookieObject.unitCityId;
								$scope.unitAreaId = cookieObject.unitAreaId;
								$scope.slotDetailsList = [];
								$scope.setCreatedSlotList = [];
								$scope.setCreatedTimeSlotList = [];
								$scope.setCreatedSlotListForServiceList = [];
								$scope.idx = 0;
								$scope.toDate = moment(new Date()).format('DD-MM-YYYY');
								$scope.diff = 0;
								$scope.setCreatedSlotList.reSchedule = 'N';
								
								$scope.fromTime = "";
								$scope.toTime = "";
						
								$scope.cyclic = {
										"patientId" : 0,
										"specialityId" : 0,
										"doctorId" : 0,
										"serviceId" : 0,
										"serviceName" : "",
										"datepickerFrom" : new Date(),
										"datepickerFromS" : new Date(),
										"duration" : 0,
										"durationS" : 0,
										"durationType" : "",
										"durationTypeS" : "",
										"sessions" : 0,
										"sessionsS" : 0,
										"morning" : "N",
										"morningS" : "N",
										"evening" : "N",
										"eveningS" : "N",
										"occurences" : 0,
										"occurencesS" : 0,
										"every" : "",
										"everyS" : "",
										"occurencesType" : "",
										"occurencesTypeS" : "",
										"overBooking" : "N",
										"overBookingS" : "N",
										 "serviceAmount": 0,
										 "serviceMasterId": 0,
										 "subSpecialityId":0,
										 "specialityId":0,
										 "taxId": 0,
										 "taxPercentage": 0,
										 "packageId" :0,
										 "packageConsumptionMasterId":0,
										 "packageConsumptionServiceDetailId":0,
										 "packageEoDetailId":0,
										 "billId":0,
										 "dayM1":'N',
										 "dayM2":'N',
										 "dayM3":'N',
										 "dayM4":'N',
										 "dayM5":'N',
										 "dayM6":'N',
										 "dayM7":'N',
										 "dayE1":'N',
										 "dayE2":'N',
										 "dayE3":'N',
										 "dayE4":'N',
										 "dayE5":'N',
										 "dayE6":'N',
										 "dayE7":'N',

										 "daySM1":'N',
										 "daySM2":'N',
										 "daySM3":'N',
										 "daySM4":'N',
										 "daySM5":'N',
										 "daySM6":'N',
										 "daySM7":'N',
										 "daySE1":'N',
										 "daySE2":'N',
										 "daySE3":'N',
										 "daySE4":'N',
										 "daySE5":'N',
										 "daySE6":'N',
										 "daySE7":'N',
										 "encounterId":"",
										 "packageDuration":"",
										 "packageValidityToDate":"",
										 "admissionId":null,
										 "orderId":null,
										 "visitTypeId":null
								}
								
								$scope.checkMaxNumberOfSession = function(sessions){
	                            	if(sessions > 365){
	                            		  growl.warning(" You entered number of session greater than 365  ",
													{
														title : 'warning!'
													});
	                            		  $scope.cyclic.sessions = 0;
	                            		  $scope.cyclic.sessionsS = 0;
	                            	}
	                            }
								$scope.patientSearchObj = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"identificationNo":"",
										"patientType"	  :"1",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId
								}
								
								 $scope.appointmentSearchObject = {
							    		 'doctorId':0,
							    		 'specialityId':0,
							    		 'organizationId':$scope.organizationId,
							    		 'unitId':$scope.unitId,
							    		 'patientId':0,
							    		 'specialityId':0,
							    		 'subSpecialityId':0,
							    		 'modalityId':0,
							    		 'visitTypeId':0
							     }
								/*$scope.setCreatedSlotListForService = {
										'appointmentDate':'',
										'appointmentTime':'09:00:00'
								}*/
								$scope.searchType = 1;
								
								var data = {};
								
								var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											$scope.genderList = [];
											if (response.data.status == "success")
												$scope.genderList = response.data.listObject;
										});
								
								var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
								BillingGenericService.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
									console.log(response);
									$scope.identificationList = [];
									if (response.data.status == "success")
										$scope.identificationList = response.data.listObject;
								});
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
										
								var URI = BASE_URL + ROOT_URL+ GETACTIVESPECIALITY;
								BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											// $scope.startLoader();
											if (response.data.status == "success")
												$scope.specialityList = response.data.listObject;
										});
								
								
									/*var URI1 = BASE_URL + ROOT_URL + API + GLOBAL + UNIT_SERVICE_MASTER_DETAILS + SLASH + $scope.organizationId + SLASH + $scope.unitId;
										console.log("URI", URI1);
									BillingGenericService.serviceAction(METHOD_GET, URI1, "", NOTIFICATION_MSG_STATUS_FALSE)
												.then(
												function(response) {
													if (response.data.status == "success")
														$scope.serviceList = response.data.listObject;
													console.log("Service list="+response.data.listObject);
													//$rootScope.stopLoader();
												});*/
								
							}
							$scope.init();
							
							
							$scope.firstTabLevel = 1;
							$scope.setFirstTabLevel = function(newTab) {
								var patientObj=angular.equals({},$scope.patientDetails);
								if (newTab == 1){
									if(patientObj==false){
										$scope.getPatientDetailsById($scope.patientDetails.patientId);
									}
									$scope.isSetFirstTabLevel(1);
								}else if(newTab == 2){
                                  if(patientObj==false){
                                	  $scope.getPatientDetailsById($scope.patientDetails.patientId);
									}
									$scope.isSetFirstTabLevel(2);
								}
								$scope.firstTabLevel=newTab;
							}
							
							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
							};
							
							$scope.specialityObj={};
							$scope.deptWiseAppointment='N';
							//Function For Get Doctor List By Speciality
							$scope.getDoctorBySpeciality = function(obj) {
								var date = new Date();
								var currDateApp = moment(date).format('YYYY-MM-DD');
								var object=JSON.parse(obj);
								var data = {
									specialityId : object.specialityId,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									appointmentDate : currDateApp
								};
								$scope.startLoader();
								$scope.cyclic.doctorId=0;
								$scope.cyclic.specialityId=object.specialityId;
								$scope.deptWiseAppointment=object.deptWiseAppointment;
								
								//var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
								var URI = BASE_URL + ROOT_URL + "/api/global/getDoctorListBySpecialityId";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													if (response.data.status == "success"){
														$scope.doctorList = response.data.listObject;
														$scope.stopLoader();
													}else{
														$scope.stopLoader();
													}
												});
									}
							
							$scope.searchPatient = function()
							{
								if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
				           			{
											growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
											});
											return false;
					           		}
								else if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									return false;
								}	
								else{
									$scope.startLoader();
									var patSearchObj = angular.copy($scope.patientSearchObj);
									 if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
										var dt = new Date($scope.patientSearchObj.birthDate);
										patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
										$scope.patientSearchObj.birthDate=patSearchObj.birthDate;
									} 
											 	var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
											 	
													BillingGenericService
															.serviceAction(METHOD_POST,
																	URI, $scope.patientSearchObj,
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(
																	function(response) {
																		$scope.patientList = [];
																		if(response.data.listObject==null){
																			growl.warning(response.data.message,{
																				title : ERROR_MSG
																			});
																			$scope.patientSearchObj.birthDate="";
																			$scope.patientList=[];
																			return false;
																		}
																		else  if(response.data.listObject.length == 0){
																			$scope.stopLoader();
																			growl.warning("No Result Found!",{
																				title : ERROR_MSG
																			});
																		}
																		
																		else{
																			$scope.stopLoader();

																			angular.forEach(response.data.listObject,function(value,key)
																					{
																				  		if(value.registrationTypeId != 5)
																				  			{
																				  			$scope.patientList.push(value);
																				  			}
																				  		
																					});
																			$scope.patientSearchObj.birthDate="";
																		}
																	});
										}
							}
							
							     var currDate = new Date();
							     $scope.currentTime = moment(currDate).format('HH:mm');
							     $scope.currentDateCyclic=moment(currDate).format('YYYY-MM-DD');
						
							     $scope.checkDayExist=function(date){
						    			var flag=false;
						    			
						    			angular.forEach($scope.setCreatedSlotList,function(value,index){
						    				if(moment(value.appointmentDate2).format('YYYY-MM-DD')==moment(date).format('YYYY-MM-DD')){
									    			flag=true;
						    				}
						    			});
						    			
						    			return flag;
						    		}
							     
							     $scope.checkDayExistInService=function(date){
						    			var flag=false;
						    			
						    			angular.forEach($scope.setCreatedSlotListForServiceList,function(value,index){
						    				if(moment(value.appointmentDate).format('YYYY-MM-DD')==moment(date).format('YYYY-MM-DD')){
									    			flag=true;
						    				}
						    			});
						    			
						    			return flag;
						    		} 
							 
//****************************************Generate doctor Schedule and department schedule************************************************************							  
							     
						  //Generate schedule for doctor
							$scope.isAppointmentSchedule='N';
							$scope.generateSchedule = function(obj){
									 var currDate = new Date();
								     $scope.currentTime = moment(currDate).format('HH:mm');
								     $scope.currentDateCyclic=moment(currDate).format('YYYY-MM-DD');
								     
									$scope.slotDetailsList = [];
									$scope.setCreatedSlotList = [];
									$scope.isAppointmentSchedule='Y';
									$scope.cyclic.noOfSessions = obj.sessions;
									$scope.cyclic.toDate="";
									if($scope.patientDetails.patientId == 0 || $scope.patientDetails.patientId == undefined){
										growl.warning("Please select Patient first",{
											title : ERROR_MSG
										});
										return;
									}
									
									if(obj.specialityId == 0 /*|| obj.doctorId == 0*/){
										growl.warning("Please select Speciality and Doctor",{
											title : ERROR_MSG
										});
										$scope.cyclic.specialityName="";
										$scope.cyclic.docName="";
										return;
									}
									
									if(obj.doctorId==null || obj.doctorId==0){
										$scope.cyclic.docName="";
									}
									
									if(obj.sessions==0 || obj.sessions=="" || obj.sessions==undefined){
										growl.warning("Number of session can not be blank",{
											title : ERROR_MSG
										});
										return;
									}
								
									if(obj.occurencesType!=undefined && obj.occurences!=undefined && obj.occurences>0 && obj.every!=undefined && obj.every>0){
										
										if(Number(obj.occurences)>Number(obj.sessions)){
											growl.warning("Number of occurences can not be greater than total sessions",{
												title : ERROR_MSG
											});
											return;
										}
										
								 if(obj.duration!=undefined && obj.duration!=0 && obj.duration!=""){
										var recurrTotalDays=obj.every*(obj.occurencesType=='d'?1:(obj.occurencesType=='w'?7:30));	
										var durationTotalDays=obj.duration*(obj.durationType=='d'?1:(obj.durationType=='w'?7:30));
										if(Number(recurrTotalDays)>Number(durationTotalDays)){
											growl.warning("Duration cannot be greater than total occurences",{
												title : ERROR_MSG
											});
											return;
										}
									}
										
									}	
								
									$scope.currentDate = new Date(obj.datepickerFrom);
								    $scope.startDate = new Date(obj.datepickerFrom);
								    
								    var duraType=obj.durationType == "d" || obj.durationType == "" || obj.duration==undefined?1:obj.durationType == "w"?7:30;
								    if(obj.duration!=undefined && obj.duration!=0 && obj.duration!=""){
							    		$scope.diff = obj.duration*duraType;
							    		$scope.toDate = $scope.currentDate.getTime() + ($scope.diff-1)*24*60*60*1000;
								    }else if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0){
										var occuType=(obj.occurencesType=="d" || obj.occurencesType==""?1:obj.occurencesType=="w"?7:30);
										var duration=Math.round((obj.every*occuType*obj.sessions)/obj.occurences);
										$scope.toDate = $scope.currentDate.getTime() + duration*24*60*60*1000;
									}else{
										$scope.diff = (((obj.duration=="" || obj.duration==0 || obj.duration==undefined)?obj.sessions:obj.duration));
										$scope.toDate = $scope.currentDate.getTime() + ($scope.diff-1)*24*60*60*1000;
									}
								    
									
									if($scope.cyclic.dayM1=='Y' ||$scope.cyclic.dayM2=='Y' ||$scope.cyclic.dayM3=='Y' || $scope.cyclic.dayM4=='Y'
						    				|| $scope.cyclic.dayM5=='Y' || $scope.cyclic.dayM6=='Y' || $scope.cyclic.dayM7=='Y'){
										$scope.cyclic.morning='Y';
									}
									
									if($scope.cyclic.dayE1=='Y' ||$scope.cyclic.dayE2=='Y' ||$scope.cyclic.dayE3=='Y' || $scope.cyclic.dayE4=='Y'
						    				|| $scope.cyclic.dayE5=='Y' || $scope.cyclic.dayE6=='Y' || $scope.cyclic.dayE7=='Y'){
										$scope.cyclic.evening='Y';
									}
									
									if($scope.cyclic.evening === "Y" && $scope.cyclic.morning === "Y"){
										$scope.fromTime = $scope.docMorningStartTime;
										$scope.toTime = $scope.docEveningEndTime;
									}
									else if($scope.cyclic.evening === "Y"){
										$scope.fromTime = $scope.docEveningStartTime;
										$scope.toTime = $scope.docEveningEndTime;
									}else if($scope.cyclic.morning === "Y"){
										$scope.fromTime = $scope.docMorningStartTime;
										$scope.toTime = $scope.docMorningEndTime;
									}else{
										$scope.fromTime = $scope.docMorningStartTime;
										$scope.toTime = $scope.docEveningEndTime;
									}
									
								
									var data = {
											specialityId : obj.specialityId,
											doctorId : (obj.doctorId!=undefined &&  obj.doctorId>0)?obj.doctorId:0,
											fromDate :moment(new Date($scope.startDate)).format('YYYY-MM-DD'),
											toDate :moment(new Date($scope.toDate)).format('YYYY-MM-DD'),
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : 10,
											fromTime : $scope.fromTime,
											toTime : $scope.toTime,
											deptWiseAppointment : $scope.deptWiseAppointment
										};
									
									console.log(data);
									$scope.cyclic.fromDate = moment(obj.datepickerFrom).format($scope.startDateFormat.toUpperCase());
									$scope.cyclic.toDate = moment($scope.toDate).format($scope.startDateFormat.toUpperCase());
									/*$scope.cyclic.fromDate = moment(obj.datepickerFrom).format('DD/MM/YYYY');
									$scope.cyclic.toDate = moment($scope.toDate).format('DD/MM/YYYY');*/
									$scope.startLoader();
									var URI = BASE_URL + ROOT_URL + GETWEEKLYSLOTS;
  BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
				console.log(JSON.stringify(response.data.listObject));
	if (response.data.status == "success") {
															
		if(response.data.listObject.length==0){
						if(obj.doctorId >0 ){
							growl.warning("No slots available for selected doctor !",{
								title : ERROR_MSG
							});
						 return false;	
						}else{
							growl.warning("No slots available for selected department !",{
								title : ERROR_MSG
							});
						  return false;	
						}
					}
		angular.forEach(response.data.listObject,function(value,index)
				{
				var appointmentDate1 =moment(value.appointmentDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase());
				if(appointmentDate1=='Invalid date')
					{
					appointmentDate1='';
					}else
						{
						appointmentDate1=appointmentDate1;
						}
				value["appointmentDate1"]=appointmentDate1;
				});
					$scope.slotDetailsList = response.data.listObject;
					$scope.slotDateList=response.data.object.appointmentDateList;
					
															
 if((obj.dayM1=='N'  && obj.dayM2=='N' && obj.dayM3=='N' && obj.dayM4=='N' && obj.dayM5=='N' 
	&& obj.dayM6=='N' && obj.dayM7=='N' && obj.dayE1=='N' && obj.dayE2=='N' &&obj.dayE3=='N' 
	&& obj.dayE4=='N' && obj.dayE5=='N' && obj.dayE6=='N' && obj.dayE7=='N')){	
	 
	 $scope.endDate = new Date($scope.currentDate.getTime()+60*60*1000);	
		var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
		$scope.addDays;
		var occuType=(obj.occurencesType=="d" || obj.occurencesType==""?1:obj.occurencesType=="w"?7:30);
		var durationType=(obj.durationType=="d" || obj.occurencesType==""?1:(obj.durationType=="w"?7:30));	
		if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0
					&&(obj.duration==undefined || obj.duration==0 || obj.duration=="")){
				 totalDuration=Math.round((obj.every*occuType*obj.sessions)/obj.occurences);
				  addWithEveryRec=obj.every*occuType;	
				  noOfOccurences=Number(obj.occurences);
				  reqPerRecOcc=noOfOccurences;
				  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
		}else if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0){
		  totalDuration=Number(obj.duration)*durationType;	
		  addWithEveryRec=obj.every*occuType;	
		  noOfOccurences=Number(obj.occurences);
		  reqPerRecOcc=noOfOccurences;
		  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
		}else if(obj.duration!=undefined && obj.duration>0){
		   totalDuration=Number(obj.duration)*durationType;
		   noOfOccurences=Number(obj.sessions);
		   if(obj.durationType=="d" || obj.durationType==""){
			   if(totalDuration>noOfOccurences)
		          addWithEveryRec=Math.round(totalDuration/noOfOccurences);
			   else
				   addWithEveryRec=1; 
		   }else{
			   addWithEveryRec=Math.round(totalDuration/noOfOccurences);
		   }
		   reqPerRecOcc=Math.floor(noOfOccurences/totalDuration);
		   $scope.addDays=addWithEveryRec;
		}else{
			   totalDuration=Number(obj.sessions);
			   addWithEveryRec=1;
			   noOfOccurences=Number(obj.sessions);
			   reqPerRecOcc=1;
			   $scope.addDays=1
		}
		 $scope.lastDate=new Date($scope.endDate.getTime()+(totalDuration-1)*24*60*60*1000);
		 
		if($scope.addDays==0){
			$scope.addDays=1;
		}
		if(reqPerRecOcc==0){
			reqPerRecOcc=1;
		}
		
		//$scope.everyWeekFirstDateCounter=angular.copy($scope.endDate);
		$scope.firstRecDate=$scope.endDate;
		 var backOneDate = new Date($scope.endDate.getTime()-1*24*60*60*1000);
		//for recurrence criteria
		$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
		$scope.currPerReqTakenCount=0;
		//first week last date
		//$scope.lastDateofCurrWeek=new Date(backOneDate.getTime() + 7*24*60*60*1000)
		$scope.currentIterationDate=backOneDate;				  

		$scope.tempDate=angular.copy(new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000));
     var flagStop=0,i=0,t,j=0,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;

		for(i;i<=obj.sessions; i++){
				 if(i==0){
					 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
					// $scope.tempDate = angular.copy($scope.currentIterationDate);
				 }
				
				 if($scope.setCreatedSlotList.length==obj.sessions){
					 break;
				 }
				 
				 if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){
					//slot against doctor and department
					for(j=0;j<$scope.slotDetailsList.length;j++){
						   if(moment($scope.currentIterationDate).format('YYYY-MM-DD') === moment($scope.slotDetailsList[j].appointmentDate2).format('YYYY-MM-DD') 
						    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
						    				? ($scope.slotDetailsList[j].originalFromTime>$scope.currentTime ? true:false):true)){ 
					    	
							//if overbooking allowed
					    	if(($scope.cyclic.overBooking=='Y' && $scope.slotDetailsList[j].allowedOverBookingSlots>$scope.slotDetailsList[j].slotPatientDetailsListsSize) 
					    			||($scope.slotDetailsList[j].slotStatusId==1 && $scope.slotDetailsList[j].isVip=='N'))
					    	{
					    	
					    		if($scope.slotDetailsList[j].allowedOverBookingSlots>$scope.slotDetailsList[j].slotPatientDetailsListsSize 
					    				&& $scope.cyclic.overBooking=='Y' && $scope.slotDetailsList[j].slotPatientDetailsListsSize>0){
					    			$scope.slotDetailsList[j].slotStatusId=6;
					    		}else{
					    			$scope.slotDetailsList[j].slotStatusId=4;
					    		}
					    		
					    		$scope.setCreatedSlotList.push($scope.slotDetailsList[j]);
					    		$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
							    break;		
					      }
						}
					}
				 }
				 
				 if(remainingOcc>0 && flagForLastDateApp==true){
					   var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
					   var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
						for(b=1;b<=remainingOcc;b++){
							if($scope.currPerReqTakenCount==reqPerRecOcc){
								break;
							}
							var currentAppObj;
							for(j=0;j<$scope.slotDetailsList.length;j++){
									if($scope.setCreatedSlotList.length==obj.sessions){
										break;
									}
									if($scope.currPerReqTakenCount==reqPerRecOcc){
										break;
									}
								currentAppObj=$scope.slotDetailsList[j];
								  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
										  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
								    		&& (moment(lastDateAppCurrRecc).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
								    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)){ 
									  
									  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
								    			||(currentAppObj.slotStatusId==1)){
								    		
										  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
								    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
								    			currentAppObj.slotStatusId=6;
								    		}else{
								    			currentAppObj.slotStatusId=4;
								    		}
										  $scope.setCreatedSlotList.push(currentAppObj);
										$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
									  }
								  }
							}
				        
						}
					}
					
					 
					 if(counterForApp!=$scope.setCreatedSlotList.length){
				    	   counterForApp = angular.copy($scope.setCreatedSlotList.length);  
				       }		
						
				       
				       flagForLastDateApp=false,remainingOcc=0;
						//next recurence
						if($scope.setCreatedSlotList.length % reqPerRecOcc==0 && $scope.currPerReqTakenCount>0){
							var backDate=new Date($scope.lastRecDate.getTime());
							
							/*if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
								$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
								$scope.tempDate = angular.copy($scope.currentIterationDate);
							}*/
							if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
								$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
								$scope.tempDate = angular.copy($scope.currentIterationDate);
							}
			    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
			    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
			    			 $scope.currPerReqTakenCount=0;
			    			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
									break;
								}
						}else if(counterForApp==$scope.setCreatedSlotList.length && $scope.addDays>1 && $scope.currPerReqTakenCount==0){
							var flagObj=false;
							for(a=1;a<=$scope.addDays;a++){
								if(a%2==1){
									$scope.currentIterationDate=new Date($scope.tempDate.getTime()-(a==1?1:Math.round(a/2))*24*60*60*1000);
								}else{
									$scope.currentIterationDate=new Date($scope.tempDate.getTime()+(a==2?1:a/2)*24*60*60*1000);	
								}
								if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<moment($scope.firstRecDate).format('YYYY-MM-DD')){
									continue;
								}
								
								if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
									continue;
								}
								
								var list=$.grep($scope.slotDateList, function (obj) {
							        return obj === moment($scope.currentIterationDate).format('DD-MM-YYYY');
							    });
								if(list.length>0){
								      flagObj=true;
				    			}
								if(flagObj==true){
									$scope.tempDate = angular.copy($scope.currentIterationDate);
									break;
								}
							}
							
							if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
								break;
							}
							if(flagObj==false){
								if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
										&& $scope.currPerReqTakenCount>0){
									 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
									flagForLastDateApp=true;
								}else{
									if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
										  var backDate=new Date($scope.lastRecDate.getTime());
										$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
										$scope.tempDate = angular.copy($scope.currentIterationDate);
						    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
						    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
						    			 $scope.currPerReqTakenCount=0;
									}else{
										$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
										$scope.tempDate = angular.copy($scope.currentIterationDate);
									}
								}
							}
						}else{
						   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
						   $scope.tempDate = angular.copy($scope.currentIterationDate);
						   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
							        if($scope.currPerReqTakenCount>0){
												 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
												flagForLastDateApp=true;
									}else{
										  var backDate=new Date($scope.lastRecDate.getTime());
										 $scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
										 $scope.tempDate = angular.copy($scope.currentIterationDate);
						    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
						    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
						    			 $scope.currPerReqTakenCount=0;
								}
							}
						   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
								break;
							}
						}
											 
					 if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate.getTime()+1*24*60*60*1000).format('YYYY-MM-DD')){
						 break;
					 }

					 
					//if no slots added then keep counter as previous
					 i=$scope.setCreatedSlotList.length==0?0:($scope.setCreatedSlotList.length-1);
			}
									//************Place all appointment on last date*******

											        if(obj.sessions!=$scope.setCreatedSlotList.length){
											       	 if($scope.setCreatedSlotList.length==0){
														 growl.warning(
																	'Provided conditions are incorrect',
																	{
																		title : 'Warning!'
																	});
														return false; 
													 }
														//if current recurrence iteration last day meet
														var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
														var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
														var currentAppObj;
														for(j=0;j<$scope.slotDetailsList.length;j++){
																if($scope.setCreatedSlotList.length==obj.sessions){
																	break;
																}
															
															currentAppObj=$scope.slotDetailsList[j];
															  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																	  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
															    		&& (moment(lastDateAppCurrRecc).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
															    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)){ 
																  
																  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
															    			||(currentAppObj.slotStatusId==1)){
															    		
																	  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
															    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
															    			currentAppObj.slotStatusId=6;
															    		}else{
															    			currentAppObj.slotStatusId=4;
															    		}
																	  $scope.setCreatedSlotList.push(currentAppObj);
																    	
																  }
															  }
														}
											        }
											        
															
											}else{
                            //**********************Logic for preferred day selection*************


	                      $scope.endDate = new Date($scope.currentDate.getTime()+60*60*1000);	
							var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
							$scope.addDays;
							var occuType=(obj.occurencesType=="d" || obj.occurencesType==""?1:obj.occurencesType=="w"?7:30);
							var durationType=(obj.durationType=="d" || obj.occurencesType==""?1:(obj.durationType=="w"?7:30));	
							if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0
										&&(obj.duration==undefined || obj.duration==0 || obj.duration=="")){
									 totalDuration=Math.round((obj.every*occuType*obj.sessions)/obj.occurences);
									  addWithEveryRec=obj.every*occuType;	
									  noOfOccurences=Number(obj.occurences);
									  reqPerRecOcc=noOfOccurences;
									  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
							}else if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0){
							  totalDuration=Number(obj.duration)*durationType;	
							  addWithEveryRec=obj.every*occuType;	
							  noOfOccurences=Number(obj.occurences);
							  reqPerRecOcc=noOfOccurences;
							  $scope.addDays=Math.floor(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
							}else if(obj.duration!=undefined && obj.duration>0){
							       totalDuration=Number(obj.duration)*durationType;
							       noOfOccurences=Number(obj.sessions);
								   addWithEveryRec=7; 
								   var totalWeek=Math.ceil(totalDuration/7);
								   reqPerRecOcc=Math.floor(noOfOccurences/totalWeek);
								   $scope.addDays=Math.floor(totalDuration/noOfOccurences);
							}else{
								   totalDuration=Number(obj.sessions);
								   addWithEveryRec=1;
								   noOfOccurences=Number(obj.sessions);
								   reqPerRecOcc=1;
								   $scope.addDays=1
							}
							 $scope.lastDate=new Date($scope.endDate.getTime()+(totalDuration-1)*24*60*60*1000);
							 
							if($scope.addDays==0){
								$scope.addDays=1;
							}
							if(reqPerRecOcc==0){
								reqPerRecOcc=1;
							}
							
							//$scope.everyWeekFirstDateCounter=angular.copy($scope.endDate);
							$scope.firstRecDate=$scope.endDate;
							 var backOneDate = new Date($scope.endDate.getTime()-1*24*60*60*1000);
							//for recurrence criteria
							$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
							$scope.currPerReqTakenCount=0;
							//first week last date
							//$scope.lastDateofCurrWeek=new Date(backOneDate.getTime() + 7*24*60*60*1000)
							$scope.currentIterationDate=backOneDate;				  
					
							$scope.tempDate=angular.copy(new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000));
					        var flagStop=0,i=0,t,j=0,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;									
			for(i=$scope.setCreatedSlotList.length;i<=obj.sessions; i++){
							if(i==0){
								 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
								 $scope.tempDate = angular.copy($scope.currentIterationDate);
							 }
								var flagObj=false;
								if(($scope.cyclic.dayM1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.dayM2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.dayM3 == "Y" && $scope.currentIterationDate.getDay()==3)
				    					|| ($scope.cyclic.dayM4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.dayM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
				    					|| ($scope.cyclic.dayM6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.dayM7 == "Y" && $scope.currentIterationDate.getDay()==0) 
				    					||($scope.cyclic.dayE1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.dayE2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.dayE3 == "Y" && $scope.currentIterationDate.getDay()==3)
				    					|| ($scope.cyclic.dayE4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.dayE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
				    					|| ($scope.cyclic.dayE6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.dayE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
								      flagObj=true;
				    			}
								
						//main loop break if condition meets	
						if(obj.sessions==$scope.setCreatedSlotList.length){
							break;
						}	
						
							if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){
								//for morning selection preference
								var currentAppObj;
								var flagCurrenMorTakenCount=0;
								for(j=0;j<$scope.slotDetailsList.length;j++){
									if($scope.currPerReqTakenCount==reqPerRecOcc){
										break;
									}
									currentAppObj=$scope.slotDetailsList[j];
									  if(moment($scope.currentIterationDate).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
									    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
									    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
									    		&& (currentAppObj.originalFromTime>$scope.docMorningStartTime && currentAppObj.originalFromTime<$scope.docMorningEndTime)){ 
										  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
									    			||(currentAppObj.slotStatusId==1)){
									    		if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
									    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
									    			currentAppObj.slotStatusId=6;
									    		}else{
									    			currentAppObj.slotStatusId=4;
									    		}
									    		
									    		if(obj.dayM1 == "Y"){
								    				if(currentAppObj.day == "Monday"){	
											    			$scope.setCreatedSlotList.push(currentAppObj);
											    			flagCurrenMorTakenCount=1;
											    			break;
										    			}
								    		    }
									    		
									    		if(obj.dayM2 == "Y"){
									    			if(currentAppObj.day == "Tuesday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
									    		if(obj.dayM3 == "Y"){
									    			if(currentAppObj.day == "Wednesday"){
									    				$scope.setCreatedSlotList.push(currentAppObj);
									    				flagCurrenMorTakenCount=1;
										    			break;	
									    			}	
									    		}
									    		if(obj.dayM4 == "Y"){
									    			if(currentAppObj.day == "Thursday"){
									    				$scope.setCreatedSlotList.push(currentAppObj);
									    				flagCurrenMorTakenCount=1;
										    			break;	
									    			}	
									    		}
									    		if(obj.dayM5 == "Y"){
									    			if(currentAppObj.day == "Friday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
									    		if(obj.dayM6 == "Y"){
									    			if(currentAppObj.day == "Saturday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
									    		if(obj.dayM7 == "Y"){
									    			if(currentAppObj.day == "Sunday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
							
										  }
									  }  
								}
								$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagCurrenMorTakenCount;
								//morning check end
								//evening preference
								var flagEveCurTakenCount=0;
								var currentAppObj;
								for(j=0;j<$scope.slotDetailsList.length;j++){
									if($scope.currPerReqTakenCount==reqPerRecOcc){
										break;
									}
									currentAppObj=$scope.slotDetailsList[j];
									/*if(($scope.setCreatedSlotList.length==0?1:$scope.setCreatedSlotList.length)%perEveryOcc==0){
										break;
									}*/
									  if(moment($scope.currentIterationDate).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
									    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
									    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
									    		&& currentAppObj.originalFromTime>$scope.docEveningStartTime && (currentAppObj.originalFromTime<$scope.docEveningEndTime)){ 
										  
										  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
									    			||(currentAppObj.slotStatusId==1)){
									    		
											  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
									    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
									    			currentAppObj.slotStatusId=6;
									    		}else{
									    			currentAppObj.slotStatusId=4;
									    		}
									    		
									    		if(obj.dayE1 == "Y"){
								    				if(currentAppObj.day == "Monday"){	
											    			$scope.setCreatedSlotList.push(currentAppObj);
											    			flagEveCurTakenCount=1;
											    			break;
										    			}
								    		    }
								    		
								    		if(obj.dayE2 == "Y"){
								    			if(currentAppObj.day == "Tuesday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
								    		if(obj.dayE3 == "Y"){
								    			if(currentAppObj.day == "Wednesday"){
								    				$scope.setCreatedSlotList.push(currentAppObj);
								    				flagEveCurTakenCount=1;
									    			break;	
								    			}	
								    		}
								    		if(obj.dayE4 == "Y"){
								    			if(currentAppObj.day == "Thursday"){
								    				$scope.setCreatedSlotList.push(currentAppObj);
								    				flagEveCurTakenCount=1;
									    			break;	
								    			}	
								    		}
								    		if(obj.dayE5 == "Y"){
								    			if(currentAppObj.day == "Friday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
								    		if(obj.dayE6 == "Y"){
								    			if(currentAppObj.day == "Saturday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
								    		if(obj.dayE7 == "Y"){
								    			if(currentAppObj.day == "Sunday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
										  }
									  }
								}	
								$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagEveCurTakenCount;
						}
							
							
							if(remainingOcc>0 && flagForLastDateApp==true){
							//if current recurrence iteration last day meet
								var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
								var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
								for(b=1;b<=remainingOcc;b++){
									if($scope.setCreatedSlotList.length==obj.sessions){
										break;
									}
									if($scope.currPerReqTakenCount==reqPerRecOcc){
										break;
									}
								//evening preference check
								var flagEveCurTakenCount=0;	
								var currentAppObj;
								for(j=0;j<$scope.slotDetailsList.length;j++){
									if($scope.setCreatedSlotList.length==obj.sessions){
										break;
									}
									if($scope.currPerReqTakenCount==reqPerRecOcc){
										break;
									}
									currentAppObj=$scope.slotDetailsList[j];
									  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
											  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
									    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
									    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
									    		&& currentAppObj.originalFromTime>$scope.docEveningStartTime && (currentAppObj.originalFromTime<$scope.docEveningEndTime)){ 
										  
										  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
									    			||(currentAppObj.slotStatusId==1)){
									    		
											  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
									    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
									    			currentAppObj.slotStatusId=6;
									    		}else{
									    			currentAppObj.slotStatusId=4;
									    		}
									    		
									    		if(obj.dayE1 == "Y"){
								    				if(currentAppObj.day == "Monday"){	
											    			$scope.setCreatedSlotList.push(currentAppObj);
											    			flagEveCurTakenCount=1;
											    			break;
										    			}
								    		    }
								    		
								    		if(obj.dayE2 == "Y"){
								    			if(currentAppObj.day == "Tuesday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
								    		if(obj.dayE3 == "Y"){
								    			if(currentAppObj.day == "Wednesday"){
								    				$scope.setCreatedSlotList.push(currentAppObj);
								    				flagEveCurTakenCount=1;
								    				break;	
								    			}	
								    		}
								    		if(obj.dayE4 == "Y"){
								    			if(currentAppObj.day == "Thursday"){
								    				$scope.setCreatedSlotList.push(currentAppObj);
								    				flagEveCurTakenCount=1;
								    				break;	
								    			}	
								    		}
								    		if(obj.dayE5 == "Y"){
								    			if(currentAppObj.day == "Friday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
								    		if(obj.dayE6 == "Y"){
								    			if(currentAppObj.day == "Saturday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
								    		if(obj.dayE7 == "Y"){
								    			if(currentAppObj.day == "Sunday"){	
								    			$scope.setCreatedSlotList.push(currentAppObj);
								    			flagEveCurTakenCount=1;
								    			break;
								    			}
								    		}
										  }
									  }
								}
								$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagEveCurTakenCount;
								
								//for morning selection preference
								var flagCurrenMorTakenCount=0;
								var currentAppObj;
								for(j=0;j<$scope.slotDetailsList.length;j++){
									if($scope.setCreatedSlotList.length==obj.sessions){
										break;
									}
									if($scope.currPerReqTakenCount==reqPerRecOcc){
										break;
									}
									currentAppObj=$scope.slotDetailsList[j];
									  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
											  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
									    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
									    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
									    		&& (currentAppObj.originalFromTime>$scope.docMorningStartTime && currentAppObj.originalFromTime<$scope.docMorningEndTime)){ 
										  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
									    			||(currentAppObj.slotStatusId==1)){
									    		if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
									    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
									    			currentAppObj.slotStatusId=6;
									    		}else{
									    			currentAppObj.slotStatusId=4;
									    		}
									    		
									    		if(obj.dayM1 == "Y"){
								    				if(currentAppObj.day == "Monday"){	
											    			$scope.setCreatedSlotList.push(currentAppObj);
											    			flagCurrenMorTakenCount=1;
											    			break;
										    			}
								    		    }
									    		
									    		if(obj.dayM2 == "Y"){
									    			if(currentAppObj.day == "Tuesday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
									    		if(obj.dayM3 == "Y"){
									    			if(currentAppObj.day == "Wednesday"){
									    				$scope.setCreatedSlotList.push(currentAppObj);
									    				flagCurrenMorTakenCount=1;
									    				break;	
									    			}	
									    		}
									    		if(obj.dayM4 == "Y"){
									    			if(currentAppObj.day == "Thursday"){
									    				$scope.setCreatedSlotList.push(currentAppObj);
									    				flagCurrenMorTakenCount=1;
									    				break;	
									    			}	
									    		}
									    		if(obj.dayM5 == "Y"){
									    			if(currentAppObj.day == "Friday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
									    		if(obj.dayM6 == "Y"){
									    			if(currentAppObj.day == "Saturday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
									    		if(obj.dayM7 == "Y"){
									    			if(currentAppObj.day == "Sunday"){	
									    			$scope.setCreatedSlotList.push(currentAppObj);
									    			flagCurrenMorTakenCount=1;
									    			break;
									    			}
									    		}
							
										  }
									  }  
										  
								}//end of morning loop
								$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagCurrenMorTakenCount;
								
								
							 }
							}	
				
					
						 var appAvail=false;
						 if(counterForApp!=$scope.setCreatedSlotList.length){
					    	   counterForApp = angular.copy($scope.setCreatedSlotList.length);  
					    	   appAvail=true;
					       }		
							
					       
					       flagForLastDateApp=false,remainingOcc=0;
							//next recurence
							if($scope.setCreatedSlotList.length % reqPerRecOcc==0 && $scope.currPerReqTakenCount>0){
								var backDate=new Date($scope.lastRecDate.getTime());
								
								/*if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
									$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
									$scope.tempDate = angular.copy($scope.currentIterationDate);
								}*/
								if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
									$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
									$scope.tempDate = angular.copy($scope.currentIterationDate);
								}
				    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
				    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
				    			 $scope.currPerReqTakenCount=0;
				    			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
										break;
									}
							}else if(appAvail==false && $scope.addDays>1 ){
								var flagObj=false;
								for(a=1;a<=$scope.addDays;a++){
									if(a%2==1){
										$scope.currentIterationDate=new Date($scope.tempDate.getTime()-(a==1?1:Math.round(a/2))*24*60*60*1000);
									}else{
										$scope.currentIterationDate=new Date($scope.tempDate.getTime()+(a==2?1:a/2)*24*60*60*1000);	
									}
									if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<moment($scope.firstRecDate).format('YYYY-MM-DD')){
										continue;
									}
									
									if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
										continue;
									}
									
									var flagForSelAvail=false;
			    					if((obj.dayM1 == "Y" && $scope.currentIterationDate.getDay()==1) || (obj.dayM2 == "Y" && $scope.currentIterationDate.getDay()==2) || (obj.dayM3 == "Y" && $scope.currentIterationDate.getDay()==3)
					    					|| (obj.dayM4 == "Y" && $scope.currentIterationDate.getDay()==4) || (obj.dayM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
					    					|| (obj.dayM6 == "Y" && $scope.currentIterationDate.getDay()==6) || (obj.dayM7 == "Y" && $scope.currentIterationDate.getDay()==0)
					    					|| (obj.dayE1 == "Y" && $scope.currentIterationDate.getDay()==1) || (obj.dayE2 == "Y" && $scope.currentIterationDate.getDay()==2) || (obj.dayE3 == "Y" && $scope.currentIterationDate.getDay()==3)
					    					|| (obj.dayE4 == "Y" && $scope.currentIterationDate.getDay()==4) || (obj.dayE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
					    					|| (obj.dayE6 == "Y" && $scope.currentIterationDate.getDay()==6) || (obj.dayE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
			    						flagForSelAvail=true;
					    			}	
									
			    					var list=$.grep($scope.slotDateList, function (obj) {
								        return obj === moment($scope.currentIterationDate).format('DD-MM-YYYY');
								    });
									
									if(list.length>0 && flagForSelAvail==true){
									      flagObj=true;
					    			}
									
									if(flagObj==true){
										$scope.tempDate = angular.copy($scope.currentIterationDate);
										break;
									}
								}
								
								if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
									break;
								}
								if(flagObj==false){
									if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
											&& $scope.currPerReqTakenCount>0){
										 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
										flagForLastDateApp=true;
									}else{
										if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
											  var backDate=new Date($scope.lastRecDate.getTime());
											$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
											$scope.tempDate = angular.copy($scope.currentIterationDate);
							    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
							    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
							    			 $scope.currPerReqTakenCount=0;
										}else{
											$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
											$scope.tempDate = angular.copy($scope.currentIterationDate);
										}
									}
								}
							}else{
							   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
							   $scope.tempDate = angular.copy($scope.currentIterationDate);
							   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
								        if($scope.currPerReqTakenCount>0){
													 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
													flagForLastDateApp=true;
										}else{
											  var backDate=new Date($scope.lastRecDate.getTime());
											 $scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
											 $scope.tempDate = angular.copy($scope.currentIterationDate);
							    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
							    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
							    			 $scope.currPerReqTakenCount=0;
									}
								}
							   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
									break;
								}
							}
						//if no slots added then keep counter as previous
						 i=$scope.setCreatedSlotList.length==0?0:($scope.setCreatedSlotList.length-1);
					}//end of main loop
        
//************************place all appointment on last date*************************	
      
        if(obj.sessions!=$scope.setCreatedSlotList.length){
			//if current recurrence iteration last day meet
        	 if($scope.setCreatedSlotList.length==0){
				 growl.warning(
							'Provided conditions are incorrect',
							{
								title : 'Warning!'
							});
				return false; 
			 }
			var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
			var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
			//evening preference check
			var currentAppObj;
			for(j=0;j<$scope.slotDetailsList.length;j++){
					if($scope.setCreatedSlotList.length==obj.sessions){
						break;
					}
				
				currentAppObj=$scope.slotDetailsList[j];
				  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
						  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
				    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
				    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
				    		&& currentAppObj.originalFromTime>$scope.docEveningStartTime && (currentAppObj.originalFromTime<$scope.docEveningEndTime)){ 
					  
					  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
				    			||(currentAppObj.slotStatusId==1)){
				    		
						  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
				    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
				    			currentAppObj.slotStatusId=6;
				    		}else{
				    			currentAppObj.slotStatusId=4;
				    		}
				    		
				    		if(obj.dayE1 == "Y"){
			    				if(currentAppObj.day == "Monday"){	
						    			$scope.setCreatedSlotList.push(currentAppObj);
					    			}
			    		    }
			    		
			    		if(obj.dayE2 == "Y"){
			    			if(currentAppObj.day == "Tuesday"){	
			    			$scope.setCreatedSlotList.push(currentAppObj);
			    			}
			    		}
			    		if(obj.dayE3 == "Y"){
			    			if(currentAppObj.day == "Wednesday"){
			    				$scope.setCreatedSlotList.push(currentAppObj);
			    			}	
			    		}
			    		if(obj.dayE4 == "Y"){
			    			if(currentAppObj.day == "Thursday"){
			    				$scope.setCreatedSlotList.push(currentAppObj);
			    			}	
			    		}
			    		if(obj.dayE5 == "Y"){
			    			if(currentAppObj.day == "Friday"){	
			    			$scope.setCreatedSlotList.push(currentAppObj);
			    			}
			    		}
			    		if(obj.dayE6 == "Y"){
			    			if(currentAppObj.day == "Saturday"){	
			    			$scope.setCreatedSlotList.push(currentAppObj);
			    			}
			    		}
			    		if(obj.dayE7 == "Y"){
			    			if(currentAppObj.day == "Sunday"){	
			    			$scope.setCreatedSlotList.push(currentAppObj);
			    			}
			    		}
					  }
				  }
			}
		
			
			//for morning selection preference
			var currentAppObj;
			for(j=0;j<$scope.slotDetailsList.length;j++){
					//main loop check break if condition meets	
					if($scope.setCreatedSlotList.length==obj.sessions){
						break;
					}
				
				currentAppObj=$scope.slotDetailsList[j];
				  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
						  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
				    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
				    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
				    		&& (currentAppObj.originalFromTime>$scope.docMorningStartTime && currentAppObj.originalFromTime<$scope.docMorningEndTime)){ 
					  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
				    			||(currentAppObj.slotStatusId==1)){
				    		if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
				    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
				    			currentAppObj.slotStatusId=6;
				    		}else{
				    			currentAppObj.slotStatusId=4;
				    		}
				    		
				    		if(obj.dayM1 == "Y"){
			    				if(currentAppObj.day == "Monday"){	
						    			$scope.setCreatedSlotList.push(currentAppObj);
					    			}
			    		    }
				    		
				    		if(obj.dayM2 == "Y"){
				    			if(currentAppObj.day == "Tuesday"){	
				    			$scope.setCreatedSlotList.push(currentAppObj);
				    			}
				    		}
				    		if(obj.dayM3 == "Y"){
				    			if(currentAppObj.day == "Wednesday"){
				    				$scope.setCreatedSlotList.push(currentAppObj);
				    			}	
				    		}
				    		if(obj.dayM4 == "Y"){
				    			if(currentAppObj.day == "Thursday"){
				    				$scope.setCreatedSlotList.push(currentAppObj);
				    			}	
				    		}
				    		if(obj.dayM5 == "Y"){
				    			if(currentAppObj.day == "Friday"){	
				    			$scope.setCreatedSlotList.push(currentAppObj);
				    			}
				    		}
				    		if(obj.dayM6 == "Y"){
				    			if(currentAppObj.day == "Saturday"){	
				    			$scope.setCreatedSlotList.push(currentAppObj);
				    			}
				    		}
				    		if(obj.dayM7 == "Y"){
				    			if(currentAppObj.day == "Sunday"){	
				    			$scope.setCreatedSlotList.push(currentAppObj);
				    			}
				    		}
		
					  }
				  }  
					  
			}//end of morning loop
      }  
  }
						//****************	Logic for preferred day selection end***********************
												
						$scope.stopLoader();

						console.log($scope.setCreatedSlotList);
			}
							$('#showDoctorModal').modal();
						});
									
									
									
				if(obj.specialityId != 0 || obj.specialityId != undefined){
					angular.forEach($scope.specialityList,function(value,key)
							{	if(value.specialityId == obj.specialityId)
						  			{$scope.cyclic.specialityName = value.specialityName;
						  			//$scope.cyclic.specialityId = value.specialityId;
						  			}
							});
				}
				
				if(obj.doctorId != 0 || obj.doctorId != undefined){
					angular.forEach($scope.doctorList,function(value,key)
							{	if(value.doctorId == obj.doctorId)
						  			{$scope.cyclic.docName = value.doctorName;
						  			//$scope.cyclic.doctorId = value.doctorId;
						  			}
							});
				}
									
	}
							
	//*************************end of slot generation function						
							
							$scope.setOccurenceType = function(val){
								$scope.cyclic.occurencesType = val;
							}
							
							$scope.getSlotsOfDate = function(obj){
								$scope.setCreatedTimeSlotList = [];
								var specId = obj.specialityId;
								
								if($scope.cyclic.morning === "Y" && $scope.cyclic.morning === "Y"){
									$scope.fromTime = $scope.docMorningStartTime;
									$scope.toTime = $scope.docEveningEndTime;
								}else if($scope.cyclic.morning === "Y"){
									$scope.fromTime = $scope.docMorningStartTime;
									$scope.toTime = $scope.docMorningEndTime;
								}else if($scope.cyclic.evening === "Y"){
									$scope.fromTime = $scope.docEveningStartTime;
									$scope.toTime = $scope.docEveningEndTime;
								}else{
									$scope.fromTime = $scope.docMorningStartTime;
									$scope.toTime = $scope.docEveningEndTime;
								}
								var data = {
										specialityId : obj.specialityId,
										doctorId :(obj.doctorId!=undefined && obj.doctorId>0)?obj.doctorId:0,
										appointmentDate :moment($scope.datepicker).format('YYYY-MM-DD'),
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										slotTypeId : 10,
										fromTime : $scope.fromTime,
										toTime : $scope.toTime,
										deptWiseAppointment : $scope.deptWiseAppointment
									};
							
								console.log(data);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
											console.log(response.data.listObject);
											if (response.data.status == "success") {
														$scope.setCreatedTimeSlotList = response.data.listObject;
														var appointmentDate1=moment(response.data.listObject[0].appointmentDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase()); 
														$rootScope.stopLoader();
														if($scope.setCreatedTimeSlotList.length==0){
															$scope.loadingMsg="Slots are not created for selected date...!";
														}
														//console.log($scope.setCreatedTimeSlotList);
													}
													$rootScope.stopLoader();

												});
							}
								
							$scope.getTimeSlotsByDate= function(slots,index){
								$scope.idx = index;
								$scope.setCreatedTimeSlotList = [];
								//$scope.datepicker = new Date(slots.appointmentDate2);
								var array = slots.appointmentDate2.split('/');
								var alignFillDate = "";
								alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
								$scope.datepicker = new Date(alignFillDate);
								
								var array = $scope.cyclic.fromDate.split('/');
								var fromDate = "";
								fromDate = array[2] + '-' + array[1] + '-'+ array[0];
								var fromDate = new Date(fromDate);
								
								var array = $scope.cyclic.toDate.split('/');
								var toDate = "";
								toDate = array[2] + '-' + array[1] + '-'+ array[0];
								var toDate = new Date(toDate);
								
								$scope.hdnAppointmentId = slots.appointmentId;
								$scope.previousSlotId = slots.slotId;
								
								$scope.dateOptions1 = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : fromDate,
										maxDate : toDate
									};
								for(let k=0;k<$scope.slotDetailsList.length;k++){
									 if(moment(slots.appointmentDate1).format($scope.startDateFormat.toUpperCase()) === moment($scope.slotDetailsList[k].appointmentDate1).format($scope.startDateFormat.toUpperCase())){ 
									    	$scope.setCreatedTimeSlotList.push($scope.slotDetailsList[k]);
										}
								}
								$scope.getSlotsOfDate($scope.cyclic);
							}
							
							//on slot select 
							$scope.slotBooking = function(item){
				                var appointmentDate1 =moment(item.appointmentDate2,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase());
								for(let k=0;k<$scope.setCreatedTimeSlotList.length;k++){
									   if(item.slotId === $scope.setCreatedTimeSlotList[k].slotId){ 
										// console.log(item.slotId+"="+$scope.setCreatedTimeSlotList[k]);
										   if($scope.setCreatedTimeSlotList[k].allowedOverBookingSlots>$scope.setCreatedTimeSlotList[k].slotPatientDetailsListsSize
												   && $scope.setCreatedTimeSlotList[k].slotPatientDetailsListsSize>0 && $scope.cyclic.overBooking=='Y'){
											   $scope.setCreatedTimeSlotList[k].slotStatusId=6;
										   }else{
											   //$scope.setCreatedTimeSlotList[k].slotStatusId=4;  
										   }
										   
										   var slotExistObj=$.grep($scope.setCreatedSlotList, function (obj) {
									            return (obj.fromTimeString==item.fromTimeString 
													    && obj.appointmentDate==item.appointmentDate);
									        });
										   if(slotExistObj.length>0){
											   growl.warning(
														'Slot already in use',
														{
															title : 'Warning!'
														});
											return false;  
										   }
										   $('#time_slot_modal').modal('hide');
                                           var appointmentDate1 =moment($scope.setCreatedTimeSlotList[k].appointmentDate2,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase()); 
                                           $scope.appointmentDate1= appointmentDate1;
										   $scope.setCreatedTimeSlotList[k].fromTimeString=$scope.setCreatedTimeSlotList[k].originalFromTime;
										 $scope.setCreatedSlotList[$scope.idx] = $scope.setCreatedTimeSlotList[k];
										 $scope.setCreatedSlotList[$scope.idx].reSchedule = 'Y';
									    	break;
										}
								}
								
							}
							
							//save cyclic appointment
							$scope.saveCyclicAppointments = function(){
								var cnt = 0;
								$scope.finalAppointmentSlotList=[];
								for(let i=0;i<$scope.setCreatedSlotList.length;i++){
									$scope.setCreatedSlotList[i].appointmentTypeId = 1; //doctorConsultaion
									$scope.setCreatedSlotList[i].generateToken = 'N';
									$scope.setCreatedSlotList[i].organizationId = $scope.organizationId;
									$scope.setCreatedSlotList[i].unitId = $scope.unitId;
									$scope.setCreatedSlotList[i].createdBy = $scope.userId;
									$scope.setCreatedSlotList[i].updatedBy = $scope.userId;
									$scope.setCreatedSlotList[i].status = 'A';
									$scope.setCreatedSlotList[i].slotStatusId = $scope.setCreatedSlotList[i].slotStatusId==1?4:$scope.setCreatedSlotList[i].slotStatusId;
									$scope.setCreatedSlotList[i].patientId = $scope.patientDetails.patientId;
									$scope.setCreatedSlotList[i].isFromCoversheet = 'N'; 
									$scope.setCreatedSlotList[i].isCyclicAppointment = 'Y'; 
									$scope.setCreatedSlotList[i].appointmentDate =  $scope.setCreatedSlotList[i].appointmentDate2;
									$scope.setCreatedSlotList[i].appointmentDate2 =  null;
									$scope.finalAppointmentSlotList.push($scope.setCreatedSlotList[i]);
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/opd/cyclicAppointmentScheduling";
								//console.log(JSON.stringify($scope.setCreatedSlotList[i]));
								BillingGenericService.serviceAction(METHOD_POST, URI,$scope.finalAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
									console.log(response);
									if (response.data.status == "success") {
											growl.success("Appointments Added Successfully",
													{
														title : 'Success!'
													});
									  } 
									$scope.getPatientDetailsById($scope.patientDetails.patientId);
									$rootScope.stopLoader();
									$scope.specialityObj={};
									$scope.init();
									});
							}
							
							//save service appointment
							$scope.saveCyclicServiceAppointments=function(){
								 
								$scope.finalAppointmentSlotList=[];
								for(let i=0;i<$scope.setCreatedSlotListForServiceList.length;i++){
									var appointmentObj={};
									
									$scope.setCreatedSlotListForServiceList[i].appointmentTypeId = 10; //cyclic appointment
									$scope.setCreatedSlotListForServiceList[i].generateToken = 'N';
									$scope.setCreatedSlotListForServiceList[i].organizationId = $scope.organizationId;
									$scope.setCreatedSlotListForServiceList[i].unitId = $scope.unitId;
									$scope.setCreatedSlotListForServiceList[i].createdBy = $scope.userId;
									$scope.setCreatedSlotListForServiceList[i].updatedBy = $scope.userId;
									$scope.setCreatedSlotListForServiceList[i].status = 'A';
									$scope.setCreatedSlotListForServiceList[i].patientId = $scope.patientDetails.patientId;
									$scope.setCreatedSlotListForServiceList[i].isFromCoversheet = 'N'; 
									if($scope.cyclic.packageId==0){
										if($scope.orderDetailList.length > 1){
											$scope.setCreatedSlotListForServiceList[i].orderDetailsId=$scope.orderDetailList[i];
										}
										if($scope.orderDetailList.length == 1){
											$scope.setCreatedSlotListForServiceList[i].orderDetailsId=$scope.orderDetailList[0];
										}
									}
									$scope.setCreatedSlotListForServiceList[i].isCyclicAppointment = 'Y';
									$scope.setCreatedSlotListForServiceList[i].appointmentStatusId=1;
									$scope.setCreatedSlotListForServiceList[i].serviceAmount=$scope.cyclic.serviceAmount;
									$scope.setCreatedSlotListForServiceList[i].serviceMasterId=$scope.cyclic.serviceMasterId;
									$scope.setCreatedSlotListForServiceList[i].billId=$scope.cyclic.billId;
									$scope.setCreatedSlotListForServiceList[i].subSpecialityId=$scope.cyclic.subSpecialityId;
									$scope.setCreatedSlotListForServiceList[i].specialityId= $scope.cyclic.specialityId;
									$scope.setCreatedSlotListForServiceList[i].defaultSelfTariffId=$scope.defaultSelfTariffId;
									$scope.setCreatedSlotListForServiceList[i].taxId=$scope.cyclic.taxId;
									$scope.setCreatedSlotListForServiceList[i].taxPercentage=$scope.cyclic.taxPercentage;
									$scope.setCreatedSlotListForServiceList[i].appointmentDate2 = null;
									$scope.setCreatedSlotListForServiceList[i].appointmentDate =  moment($scope.setCreatedSlotListForServiceList[i].appointmentDate).format('YYYY-MM-DD');
									$scope.setCreatedSlotListForServiceList[i].orderDetailsPackageMapperDto={};
									$scope.setCreatedSlotListForServiceList[i].orderDetailsPackageMapperDto.payeeId=$scope.selfPayeeId;
									if($scope.cyclic.visitTypeId==1){
									  $scope.setCreatedSlotListForServiceList[i].encounterId=$scope.cyclic.encounterId;
									}else{
										$scope.setCreatedSlotListForServiceList[i].admissionId=$scope.cyclic.admissionId;
									}
									
									$scope.setCreatedSlotListForServiceList[i].orderId=$scope.cyclic.orderId;
									$scope.setCreatedSlotListForServiceList[i].visitTypeId=$scope.cyclic.visitTypeId;
									
									//set package details if service from package
									if($scope.cyclic.packageId>0){
										$scope.setCreatedSlotListForServiceList[i].parentPackageId=$scope.cyclic.packageId;
										//$scope.setCreatedSlotListForServiceList[i].orderDetailsPackageMapperDto.packageConsumptionMasterId= $scope.cyclic.packageConsumptionMasterId; 
										//$scope.setCreatedSlotListForServiceList[i].orderDetailsPackageMapperDto.pacakgeConsumptionServiceDetailId= $scope.cyclic.packageConsumptionServiceDetailId;
										//$scope.setCreatedSlotListForServiceList[i].orderDetailsPackageMapperDto.packageConsumptionEODetailId=$scope.cyclic.packageEoDetailId;
									}
									appointmentObj=angular.copy($scope.setCreatedSlotListForServiceList[i]);
									delete appointmentObj.dateOptionservice;
									$scope.finalAppointmentSlotList.push(appointmentObj);
								}
								//return false;
								$rootScope.startLoader();
								//var URI = BASE_URL + ROOT_URL + "/api/opd/generateCyclicServiceAppointment";
								var URI = BASE_URL + ROOT_URL + "/api/billing/generateCyclicServiceAppointment";
								//console.log(JSON.stringify($scope.setCreatedSlotList[i]));
								BillingGenericService.serviceAction(METHOD_POST, URI,$scope.finalAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
									console.log(response);
									if (response.data.status == "success") {
											growl.success("Appointments Added Successfully",
													{
														title : 'Success!'
													});
									  } 
									$scope.getPatientDetailsById($scope.patientDetails.patientId);
									$scope.init();
									$rootScope.stopLoader();
									});
							}
							
							// reschedule doctor appointment
							$scope.rescheduleCyclicAppointments=function(){
								$scope.rescheduleAppointmentSlotList=[];
								for(let i=0;i<$scope.setCreatedSlotList.length;i++){
									 if($scope.patientAppointmentSlotList[i].slotId != $scope.setCreatedSlotList[i].slotId){ 
									$scope.setCreatedSlotList[$scope.idx].reSchedule = 'Y';
									$scope.setCreatedSlotList[i].appointmentTypeId = 1; //doctorConsultaion
									$scope.setCreatedSlotList[i].generateToken = 'N';
									$scope.setCreatedSlotList[i].organizationId = $scope.organizationId;
									$scope.setCreatedSlotList[i].unitId = $scope.unitId;
									$scope.setCreatedSlotList[i].createdBy = $scope.userId;
									$scope.setCreatedSlotList[i].updatedBy = $scope.userId;
									$scope.setCreatedSlotList[i].status = 'A';
									$scope.setCreatedSlotList[i].slotStatusId=$scope.setCreatedSlotList[i].slotStatusId==1?4:$scope.setCreatedSlotList[i].slotStatusId;
									//$scope.setCreatedSlotList[i].slotStatusId = 4;
									$scope.setCreatedSlotList[i].slotTypeId = 1;
									$scope.setCreatedSlotList[i].patientId = $scope.patientDetails.patientId;
									$scope.setCreatedSlotList[i].isFromCoversheet = 'N'; 
									$scope.setCreatedSlotList[i].appointmentDate2 = moment($scope.datepicker).format('DD/MM/YYYY');
									$scope.setCreatedSlotList[i].appointmentDate = null;
									$scope.setCreatedSlotList[i].rescheduleReasonId = 1;
									$scope.setCreatedSlotList[i].rescheduledBy = 'C';
									$scope.setCreatedSlotList[i].isCyclicAppointment = 'Y';
									$scope.setCreatedSlotList[i].previousSlotId = $scope.patientAppointmentSlotList[i].slotId;
									$scope.setCreatedSlotList[i].doctorId = $scope.doctorId;
									$scope.setCreatedSlotList[i].specialityId = $scope.specialityId;
									$scope.setCreatedSlotList[i].appointmentId = $scope.patientAppointmentSlotList[i].appointmentId;
									$scope.rescheduleAppointmentSlotList.push($scope.setCreatedSlotList[i]);
								 }
								}
								if($scope.rescheduleAppointmentSlotList.length==0){
									 growl.warning(
												'No change in existing appointment',
												{
													title : 'Warning!'
												});
									return false;
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +  "/api/opd/rescheduleCyclicAppointment";
								//console.log(JSON.stringify($scope.setCreatedSlotList[i]));
								BillingGenericService.serviceAction(METHOD_POST, URI,$scope.rescheduleAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
									console.log(response);
									if (response.data.status == "success") {
											growl.success("Appointments Updated Successfully",
													{
														title : 'Success!'
													});
									} 
									$rootScope.stopLoader();
									});
								
							}
							
							//reschedule cyclic service appointment
							$scope.rescheduleCyclicServiceAppointment=function(){
								$scope.rescheduleAppointmentSlotList=[];
								for(let i=0;i<$scope.serviceSlotListForReschedule.length;i++){
									 if(moment($scope.patientCyclicServiceAppList[i].appointmentDate).format('DD-MM-YYYY') != moment($scope.serviceSlotListForReschedule[i].appointmentDate).format('DD-MM-YYYY') 
											 || $scope.patientCyclicServiceAppList[i].appointmentTime != $scope.serviceSlotListForReschedule[i].appointmentTime ){ 
									
									var newArray={};
									newArray.organizationId = $scope.organizationId;
									newArray.unitId = $scope.unitId;
									newArray.createdBy = $scope.userId;
									newArray.updatedBy = $scope.userId;
									newArray.status = 'A';
									newArray.patientId = $scope.patientDetails.patientId;
									//$scope.serviceSlotListForReschedule[i].appointmentDate =  moment($scope.datepicker).format('DD/MM/YYYY');
									newArray.appointmentDate = moment($scope.serviceSlotListForReschedule[i].appointmentDate).format('DD-MM-YYYY');
									newArray.appointmentTime=$scope.serviceSlotListForReschedule[i].appointmentTime;
									newArray.appointmentId = $scope.patientCyclicServiceAppList[i].appointmentId;
									$scope.rescheduleAppointmentSlotList.push(newArray);
								 }
								}
								if($scope.rescheduleAppointmentSlotList.length==0){
									 growl.warning(
												'No change in existing appointment',
												{
													title : 'Warning!'
												});
									return false;
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +  "/api/opd/rescheduleCyclicServiceAppointment";
								//console.log(JSON.stringify($scope.setCreatedSlotList[i]));
								BillingGenericService.serviceAction(METHOD_POST, URI,$scope.rescheduleAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
									console.log(response);
									if (response.data.status == "success") {
											growl.success("Appointments Updated Successfully",
													{
														title : 'Success!'
													});
									} 
									$rootScope.stopLoader();
									});
								
							}
							
							//get patient doctor appointment
							$scope.getPatientDetailsById = function(patientId){
								$scope.showAppNoRecFound='N';
								$scope.init();
								$scope.patientDetailsList = [];
								$scope.patientDetails = {};
								var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
                                var URI1 = BASE_URL + ROOT_URL + "/api/opd/getCyclicDocWiseAndDeptWiseAppointmentListByPatientId/"+patientId+"/"+$scope.organizationId;
                                $scope.patientDetails.patientId=patientId;
                                GenericService
								.serviceAction("GET", URI, null)
								.then(
										function(response) {
									$scope.patientDetails = response.data.listObject[0];
								 BillingGenericService
									.serviceAction(METHOD_GET, URI1, null,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response1) {
												$scope.serviceAppointmentList=[];
												$scope.appointmentList = [];
												if (response.data.status == "success"){
													angular.forEach(response1.data.listObject,function(value,index){
														if(value.doctorId!=null || value.specialityId!=null){
															$scope.appointmentList.push(value);
														}
													});
													if($scope.appointmentList.length==0){
														$scope.showAppNoRecFound='Y';
													}
													//$scope.appointmentList = response.data.listObject;
													//$scope.patientDetails = response.data.listObject[0];
													//$scope.doctorId = response.data.listObject[0].doctorId;
													//$scope.specialityId = response.data.listObject[0].specialityId;
													//$scope.cyclic.mobileNumber = response.data.listObject[0].mobileNumber;
													//$scope.cyclic.email = response.data.listObject[0].email;
												}
                                                $scope.getServiceAppointmentByPatientId();
											});
										});
							      }
							
							//get patient service appointment
							$scope.showServAppNoRecFound='N';
							$scope.showAppNoRecFound='N';
							$scope.getServiceAppointmentByPatientId=function(){
								$scope.showServAppNoRecFound='N';
								var URI = BASE_URL + ROOT_URL + "/api/global/patient/billingCyclic/"+$scope.patientDetails.patientId;
								 BillingGenericService
									.serviceAction(METHOD_GET, URI, null,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												$scope.serviceAppointmentList=[];
												if (response.data.status == "success"){
													$scope.serviceAppointmentList=response.data.listObject;
													//$scope.cyclic.mobileNumber = response.data.listObject[0].mobileNumber;
													//$scope.cyclic.email = response.data.listObject[0].email;
												}
												if($scope.serviceAppointmentList.length==0){
													$scope.showServAppNoRecFound='Y';
												}
											});
							      
							}
							//get slot list
							$scope.patientAppointmentSlotList=[];
							$scope.getAppointmentDetailsById = function(info){
								$scope.setCreatedSlotList = [];
								$scope.patientAppointmentSlotList=[];
								$scope.appointmentSearchObject.appointmentTypeId = 1 ;
								$scope.appointmentSearchObject.patientId = info.patientId;
								$scope.appointmentSearchObject.doctorId = info.doctorId;
								$scope.appointmentSearchObject.specialityId=info.specialityId;
								//var URI = BASE_URL + ROOT_URL + "/api/global/patient/billingCyclic/"+info.patientId;
								$scope.isAppointmentSchedule='N';
								$scope.appointmentSearchObject.isCyclicAppointment='Y';
								var URI = BASE_URL + ROOT_URL + SEARCHAPPOINTMENTSBYPATIENTID; 
								BillingGenericService
									.serviceAction(METHOD_POST, URI, $scope.appointmentSearchObject,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												if (response.data.status == "success")
													$scope.setCreatedSlotList = response.data.listObject;
												    $scope.patientAppointmentSlotList = angular.copy($scope.setCreatedSlotList);
												    //$scope.patientAppointmentSlotList=response.data.listObject;
													$scope.cyclic.specialityName = info.specialityName;
													//$scope.cyclic.docName = info.doctorName;
													$scope.cyclic.doctorId = response.data.listObject[0].doctorId;
													$scope.doctorId = response.data.listObject[0].doctorId;
													$scope.specialityId = response.data.listObject[0].specialityId;
													$scope.cyclic.specialityId=response.data.listObject[0].specialityId;
													if($scope.doctorId!=null && $scope.doctorId>0){
														$scope.cyclic.docName=response.data.listObject[0].doctorName;
													}else{
														$scope.cyclic.docName="";
													}
													
													var spcObj = $.grep($scope.specialityList, function (disCat) {
											            return disCat.specialityId == $scope.specialityId;
											        })[0];
													 $scope.deptWiseAppointment=spcObj.deptWiseAppointment;
													 
													//$scope.cyclic.specialityId = response.data.listObject[0].specialityId;
													/*$scope.cyclic.fromDate = response.data.listObject[0].appointmentDate;
													$scope.cyclic.toDate = response.data.listObject[response.data.listObject.length-1].appointmentDate;*/
													 var appointmentDate1 =moment(response.data.listObject[0].appointmentDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase()); 
													 $scope.cyclic.fromDate= appointmentDate1;
													 var appointmentDate1 =moment(response.data.listObject[response.data.listObject.length-1].appointmentDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase()); 
													 $scope.cyclic.toDate= appointmentDate1;   
													$scope.cyclic.noOfSessions = response.data.listObject.length;
											        angular.forEach(response.data.listObject,function(value,index)
											                {
											                var appointmentDate1 =moment(value.appointmentDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase());
											                if(appointmentDate1=='Invalid date')
											                    {
											                    appointmentDate2='';
											                    }else
											                        {
											                        appointmentDate1=appointmentDate1;
											                        }
											                value["appointmentDate1"]=appointmentDate1;
											                });
											});
							}
							
							$scope.serviceSlotListForReschedule = [];
							$scope.patientCyclicAppointmentSlotList=[];
							$scope.patientCyclicServiceAppList = [];
							
							//get patient cyclic service appointment for reschedule
							$scope.getPatientServiceAppointments=function(obj){
								$scope.patientCyclicServiceAppList = [];
								$scope.patientCyclicAppointmentSlotList=[];
								$scope.serviceSlotListForReschedule = [];
								//$scope.appointmentSearchObject.appointmentTypeId =1 ;
								$scope.appointmentSearchObject.patientId = $scope.patientDetails.patientId;
								$scope.appointmentSearchObject.serviceId =obj.serviceId;
								//var URI = BASE_URL + ROOT_URL + "/api/global/patient/billingCyclic/"+info.patientId;
								$scope.isAppointmentSchedule='N';
								var URI = BASE_URL + ROOT_URL + "/api/opd/searchCyclicServiceAppointmentsByPatienId"; 
								BillingGenericService
									.serviceAction(METHOD_POST, URI, $scope.appointmentSearchObject,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												if (response.data.status == "success")
													angular.forEach(response.data.listObject,function(value,index){
														value.appointmentDate=moment(value.appointmentDate,"DD/MM/YYYY").toDate();
														$scope.serviceSlotListForReschedule.push(value);
													});
													
												    $scope.patientCyclicServiceAppList = angular.copy($scope.serviceSlotListForReschedule);
												    //$scope.patientAppointmentSlotList=response.data.listObject;
												/*	$scope.cyclic.fromDate = response.data.listObject[0].appointmentDate;
													$scope.cyclic.toDate = response.data.listObject[response.data.listObject.length-1].appointmentDate;
													$scope.cyclic.noOfSessions = response.data.listObject.length;*/
												    var appointmentDate2 =moment(response.data.listObject[0].appointmentDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase()); 
												    $scope.cyclic.fromDateS= appointmentDate2;
                                                    var appointmentDate2 =moment(response.data.listObject[response.data.listObject.length-1].appointmentDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase()); 
                                                    $scope.cyclic.toDateS= appointmentDate2;   
											});
							
							}
							
							
//***********************************get pending service against patient************************
			$scope.getPatPendingServices = function(obj){
				$scope.slotDetailsList = [];
				$scope.setCreatedSlotListForServiceList = [];
				console.log(JSON.stringify(obj));
				var currDate = new Date();
				$scope.currentTime = moment(currDate).format('HH:mm');
				if($scope.patientDetails.patientId == 0 || $scope.patientDetails.patientId == undefined){
					growl.warning("Please select Patient first!",{
						title : ERROR_MSG
					});
					return false;
				}
				
				if(obj.sessionsS==0 || obj.sessionsS=="" || obj.sessionsS==undefined){
					growl.warning("Number of session can not be blank!",{
						title : ERROR_MSG
					});
					return;
				}
				
				if(obj.occurencesTypeS!=undefined && obj.occurencesS!=undefined && obj.occurencesS>0 && obj.everyS!=undefined && obj.everyS>0){
					
					if(Number(obj.occurencesS)>Number(obj.sessionsS)){
						growl.warning("Number of occurences can not be greater than total sessions",{
							title : ERROR_MSG
						});
						return;
					}
					
				if(obj.durationS!=undefined && obj.durationS!=0 && obj.durationS!=""){
					var recurrTotalDays=obj.everyS*(obj.occurencesTypeS=='d'?1:(obj.occurencesTypeS=='w'?7:30));	
					var durationTotalDays=obj.durationS*(obj.durationTypeS=='d'?1:(obj.durationTypeS=='w'?7:30));
					if(Number(recurrTotalDays)>Number(durationTotalDays)){
						growl.warning("Duration cannot be greater than total occurences",{
							title : ERROR_MSG
						});
						return;
					}
				}
					
				}
				
				
				   $scope.currentDate = new Date(obj.datepickerFromS);
				    $scope.startDate = new Date(obj.datepickerFromS);
				    
				    var duraType=obj.durationTypeS == "d" || obj.durationTypeS == "" || obj.durationS==undefined?1:obj.durationTypeS == "w"?7:30;
				    	
				       if(obj.durationS!=undefined && obj.durationS!=0 && obj.durationS!=""){
				    		$scope.diffS = obj.durationS*duraType;
							$scope.toDateS = $scope.currentDate.getTime() + ($scope.diffS-1)*24*60*60*1000;
					    }else if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0){
							var occuType=(obj.occurencesTypeS=="d" || obj.occurencesTypeS==""?1:obj.occurencesTypeS=="w"?7:30);
							var duration=Math.round((obj.everyS*occuType*obj.sessionsS)/obj.occurencesS);
							$scope.toDateS = $scope.currentDate.getTime() + duration*24*60*60*1000;
						}else{
							$scope.diffS = (((obj.durationS=="" || obj.durationS==0 || obj.durationS==undefined)?obj.sessionsS:obj.durationS));
							$scope.toDateS = $scope.currentDate.getTime() + ($scope.diffS-1)*24*60*60*1000;
						}
					
					if($scope.cyclic.daySM1=='Y' || $scope.cyclic.daySM2=='Y' || $scope.cyclic.daySM3=='Y'
	    				|| $scope.cyclic.daySM4=='Y' || $scope.cyclic.daySM5=='Y' || $scope.cyclic.daySM6=='Y'
	    				|| $scope.cyclic.daySM7=='Y'){
						$scope.cyclic.morningS='Y';
					}
					
					if($scope.cyclic.daySE1=='Y' || $scope.cyclic.daySE2=='Y' || $scope.cyclic.daySE3=='Y'
	    				|| $scope.cyclic.daySE4=='Y' || $scope.cyclic.daySE5=='Y' || $scope.cyclic.daySE6=='Y'
	    				|| $scope.cyclic.daySE7=='Y'){
						$scope.cyclic.eveningS='Y';
					}
					
					
					if($scope.cyclic.morningS === "Y" && $scope.cyclic.eveningS === "Y"){
						$scope.fromTime = $scope.docMorningStartTime;
						$scope.toTime = $scope.docEveningEndTime;
					}
					else if($scope.cyclic.morningS === "Y"){
						$scope.fromTime = $scope.docMorningStartTime;
						$scope.toTime = $scope.docMorningEndTime;
					}else if($scope.cyclic.eveningS === "Y"){
						$scope.fromTime = $scope.docEveningStartTime;
						$scope.toTime = $scope.docEveningEndTime;
					}else{
						$scope.fromTime = $scope.docMorningStartTime;
						$scope.toTime = $scope.docEveningEndTime;
					}
					if(obj.serviceName === ""){
						growl.warning("Please select Service!",{
							title : ERROR_MSG
						});	
						return;
					}
				
					/*$scope.cyclic.fromDateS = moment(obj.datepickerFromS).format('DD/MM/YYYY');
					$scope.cyclic.toDateS = moment($scope.toDateS).format('DD/MM/YYYY');*/
					if($scope.startDateFormat!= null){
                    $scope.cyclic.fromDateS = moment(obj.datepickerFromS).format($scope.startDateFormat.toUpperCase());
                    $scope.cyclic.toDateS = moment($scope.toDateS).format($scope.startDateFormat.toUpperCase());
                    }

					$scope.cyclic.noOfSessionsS = obj.sessionsS;
					//$('#showDoctorModalService').modal();
//*****************************Normal case appointment generation************
 if((obj.daySM1=='N'  && obj.daySM2=='N' && obj.daySM3=='N' && obj.daySM4=='N' && obj.daySM5=='N' 
		&& obj.daySM6=='N' && obj.daySM7=='N' && obj.daySE1=='N' && obj.daySE2=='N' &&obj.daySE3=='N' 
		&& obj.daySE4=='N' && obj.daySE5=='N' && obj.daySE6=='N' && obj.daySE7=='N')){							
		$scope.startDate = new Date($scope.currentDate.getTime()+60*60*1000);	
		var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
		$scope.addDays;
		var occuType=(obj.occurencesTypeS=="d" || obj.occurencesTypeS==""?1:obj.occurencesTypeS=="w"?7:30);
		var durationType=(obj.durationTypeS=="d" || obj.durationTypeS=="" || obj.durationTypeS==undefined?1:(obj.durationTypeS=="w"?7:30));
		if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0
				&&(obj.durationS==undefined || obj.durationS==0 || obj.durationS=="")){
			  totalDuration=Math.round((obj.everyS*occuType*obj.sessionsS)/obj.occurencesS);
			  addWithEveryRec=obj.everyS*occuType;	
			  noOfOccurences=Number(obj.occurencesS);
			  reqPerRecOcc=noOfOccurences;
			//  $scope.addDays=Math.floor(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
			  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
		}else if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0){
		  totalDuration=Number(obj.durationS)*durationType;	
		  addWithEveryRec=obj.everyS*occuType;	
		  noOfOccurences=Number(obj.occurencesS);
		  //reqPerRecOcc=Math.floor(noOfOccurences/obj.everyS);
		  reqPerRecOcc=noOfOccurences;
		  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
	
	  }else if(obj.durationS!=undefined && obj.durationS>0){
		   totalDuration=Number(obj.durationS)*durationType;
		   noOfOccurences=Number(obj.sessionsS);
		   if(obj.durationTypeS=="d" || obj.durationTypeS==""){
			   if(totalDuration>noOfOccurences)
		          addWithEveryRec=Math.round(totalDuration/noOfOccurences);
			   else
				   addWithEveryRec=1; 
		   }else{
			   addWithEveryRec=Math.round(totalDuration/noOfOccurences);
		   }
		   reqPerRecOcc=Math.floor(noOfOccurences/totalDuration);
		   $scope.addDays=addWithEveryRec;
	 }else{
		   totalDuration=Number(obj.sessionsS);
		   addWithEveryRec=1;
		   noOfOccurences=Number(obj.sessionsS);
		   reqPerRecOcc=1;
		   $scope.addDays=1;
	}
		
	 $scope.lastDate=new Date($scope.startDate.getTime()+(totalDuration-1)*24*60*60*1000);
	 
	if($scope.addDays==0){
		$scope.addDays=1;
	}
	if(reqPerRecOcc==0){
		reqPerRecOcc=1;
	}
	
	$scope.firstRecDate=$scope.startDate;
	 var backOneDate = new Date($scope.startDate.getTime()-1*24*60*60*1000);
	//for recurrence criteria
	$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
	$scope.currPerReqTakenCount=0;
	$scope.currentIterationDate=backOneDate;				  
	
    $scope.tempDate=angular.copy($scope.currentIterationDate);
	var i,t,j,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;
 for(i=0;i<obj.sessionsS; i++){
	 
		 if(i==0){
			 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
		 }
		
		 if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
			 break;
		 }
		 
	    //service appointment
	  if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){

		  var setCreatedSlotListForService = {
					'appointmentDate':'',
					'appointmentTime':moment($scope.currentDateCyclic).format('YYYY-MM-DD')==moment($scope.currentIterationDate).format('YYYY-MM-DD')?$scope.currentTime:$scope.fromTime
			};
		  
		    setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
	    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
	    	$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
	  }
       
	  if(remainingOcc>0 && flagForLastDateApp==true){
			var lastDateAppCurrReccObj=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
			for(b=1;b<=remainingOcc;b++){
				if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
					break;
				}
				if($scope.currPerReqTakenCount==reqPerRecOcc){
					break;
				}
		    	$scope.setCreatedSlotListForServiceList.push(lastDateAppCurrReccObj); 
		    	$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
			}
		}
	  
	  flagForLastDateApp=false,remainingOcc=0;
		//next recurence
		if(reqPerRecOcc==$scope.currPerReqTakenCount){
			var backDate=new Date($scope.lastRecDate.getTime());
			if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
				$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
				$scope.tempDate = angular.copy($scope.currentIterationDate);
			}
			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
			 $scope.currPerReqTakenCount=0;
			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
					break;
				}
		}else{
		   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
		   $scope.tempDate = angular.copy($scope.currentIterationDate);
		   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
						&& $scope.currPerReqTakenCount>0){
					 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
					flagForLastDateApp=true;
				}else{
				    var backDate=new Date($scope.lastRecDate.getTime());
					$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
					$scope.tempDate = angular.copy($scope.currentIterationDate);
	    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
	    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
	    			 $scope.currPerReqTakenCount=0;
				}
			}
		   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
				break;
			}
		}
		
//if no slots added then keep counter as previous
		 i=$scope.setCreatedSlotListForServiceList.length==0?0:($scope.setCreatedSlotListForServiceList.length-1);
}
		//************Place all appointment on last date*******

		        if(obj.sessionsS!=$scope.setCreatedSlotListForServiceList.length){
		        	 if($scope.setCreatedSlotListForServiceList.length==0){
						 growl.warning(
									'Provided conditions are incorrect',
									{
										title : 'Warning!'
									});
						return false; 
					 }
					var lastDateAppObj=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
					for(var K=0;K<obj.sessionsS;K++){
							if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
								break;
							}
					    	$scope.setCreatedSlotListForServiceList.push(lastDateAppObj); 
						}
					 }

}else{

			//**********************Logic for preferred day selection*************
                $scope.startDate = new Date($scope.currentDate.getTime()+60*60*1000);	
                $scope.endDate=angular.copy($scope.startDate);
					var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
					$scope.addDays;
					var occuType=(obj.occurencesTypeS=="d" || obj.occurencesTypeS==""?1:obj.occurencesTypeS=="w"?7:30);
					var durationType=(obj.durationTypeS=="d" || obj.occurencesTypeS==""?1:(obj.durationTypeS=="w"?7:30));	
					if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0
								&&(obj.durationS==undefined || obj.durationS==0 || obj.durationS=="")){
							 totalDuration=Math.round((obj.everyS*occuType*obj.sessionsS)/obj.occurencesS);
							  addWithEveryRec=obj.everyS*occuType;	
							  noOfOccurences=Number(obj.occurencesS);
							  reqPerRecOcc=noOfOccurences;
							  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
					}else if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0){
					  totalDuration=Number(obj.durationS)*durationType;	
					  addWithEveryRec=obj.everyS*occuType;	
					  noOfOccurences=Number(obj.occurencesS);
					  reqPerRecOcc=noOfOccurences;
					  $scope.addDays=Math.floor(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
					}else if(obj.durationS!=undefined && obj.durationS>0){
					       totalDuration=Number(obj.durationS)*durationType;
					       noOfOccurences=Number(obj.sessionsS);
						   addWithEveryRec=7; 
						   var totalWeek=Math.ceil(totalDuration/7);
						   reqPerRecOcc=Math.floor(noOfOccurences/totalWeek);
						   $scope.addDays=Math.floor(totalDuration/noOfOccurences);
					}else{
						   totalDuration=Number(obj.sessionsS);
						   addWithEveryRec=1;
						   noOfOccurences=Number(obj.sessionsS);
						   reqPerRecOcc=1;
						   $scope.addDays=1
					}
					 $scope.lastDate=new Date($scope.endDate.getTime()+(totalDuration-1)*24*60*60*1000);
					 
					if($scope.addDays==0){
						$scope.addDays=1;
					}
					if(reqPerRecOcc==0){
						reqPerRecOcc=1;
					}
					
					//$scope.everyWeekFirstDateCounter=angular.copy($scope.endDate);
					$scope.firstRecDate=$scope.endDate;
					 var backOneDate = new Date($scope.endDate.getTime()-1*24*60*60*1000);
					//for recurrence criteria
					$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
					$scope.currPerReqTakenCount=0;
					//first week last date
					//$scope.lastDateofCurrWeek=new Date(backOneDate.getTime() + 7*24*60*60*1000)
					$scope.currentIterationDate=backOneDate;				  
			
					$scope.tempDate=angular.copy(new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000));
			        var flagStop=0,i=0,t,j=0,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;		
		
    for(i=$scope.setCreatedSlotListForServiceList.length;i<=obj.sessionsS; i++){
		    	
						 if(i==0){
							 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
							 $scope.tempDate = angular.copy($scope.currentIterationDate);
						 }

							var flagObj=false;
							if(($scope.cyclic.daySM1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySM2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySM3 == "Y" && $scope.currentIterationDate.getDay()==3)
			    					|| ($scope.cyclic.daySM4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
			    					|| ($scope.cyclic.daySM6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySM7 == "Y" && $scope.currentIterationDate.getDay()==0) 
			    					||($scope.cyclic.daySE1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySE2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySE3 == "Y" && $scope.currentIterationDate.getDay()==3)
			    					|| ($scope.cyclic.daySE4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
			    					|| ($scope.cyclic.daySE6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
							      flagObj=true;
			    			}
							
					//main loop break if condition meets	
					if(obj.sessionsS==$scope.setCreatedSlotListForServiceList.length){
						break;
					}	
					
					if(flagObj==true){
						if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){
							//for morning selection preference
							var setCreatedSlotListForService = {
									'appointmentDate':'',
									'appointmentTime':moment($scope.currentDateCyclic).format('YYYY-MM-DD')==moment($scope.currentIterationDate).format('YYYY-MM-DD')?$scope.currentTime:$scope.docMorningStartTime
							
							};
							
							if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
								break;
							}
							var flagCurrenMorTakenCount=0;
							for(j=0;j<=1;j++){
								if($scope.currPerReqTakenCount==reqPerRecOcc){
									break;
								}
								var dayOfDate=moment($scope.currentIterationDate).format('dddd');
							if($scope.cyclic.daySM1 == "Y"){	
								if(dayOfDate=="Monday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagCurrenMorTakenCount=1;
							    	break;
				    			}
				    		}
							
						 if($scope.cyclic.daySM2 == "Y"){
				    			if(dayOfDate=="Tuesday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagCurrenMorTakenCount=1;
							    	break;
				    			}
				    		}
						 
						 if($scope.cyclic.daySM3 == "Y"){
				    			if(dayOfDate=="Wednesday"){
				    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
						    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
						    	flagCurrenMorTakenCount=1;
						    	break;
				    			}
				    		}
						 
						 if($scope.cyclic.daySM4 == "Y"){
				    			if(dayOfDate=="Thursday"){
				    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
						    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
						    	flagCurrenMorTakenCount=1;
						    	break;
				    			}
				    		}
						 
						 if($scope.cyclic.daySM5 == "Y"){
				    			if(dayOfDate=="Friday"){
				    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
						    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService);
						    	flagCurrenMorTakenCount=1;
						    	break;
				    			}
				    		}
						 
						 if($scope.cyclic.daySM6 == "Y"){
				    			if(dayOfDate=="Saturday"){
				    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
						    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
						    	flagCurrenMorTakenCount=1;
						    	break;
				    			}
				    		}
						 
						 if($scope.cyclic.daySM7 == "Y"){
				    			if(dayOfDate=="Sunday"){
				    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
						    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
						    	flagCurrenMorTakenCount=1;
						    	break;
				    			}
				    		}
						}
							$scope.currPerReqTakenCount=flagCurrenMorTakenCount+$scope.currPerReqTakenCount;
							//morning check end
							
							//evening preference
							var setCreatedSlotListForService = {
									'appointmentDate':'',
									'appointmentTime':moment($scope.currentDateCyclic).format('YYYY-MM-DD')==moment($scope.currentIterationDate).format('YYYY-MM-DD')?$scope.currentTime:$scope.docEveningStartTime
							
							};
							if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
								break;
							}
							var flagEveCurTakenCount=0;
							var currentAppObj;
							for(j=0;j<=1;j++){
								if($scope.currPerReqTakenCount==reqPerRecOcc){
									break;
								}
								if($scope.cyclic.daySE1 == "Y"){
					    			if(dayOfDate=="Monday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagEveCurTakenCount=1;
							    	break;
					    			}
					    		}
								
								if($scope.cyclic.daySE2 == "Y"){
					    			if(dayOfDate=="Tuesday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagEveCurTakenCount=1;
							    	break;
					    			}
					    		}
								
								if($scope.cyclic.daySE3 == "Y"){
					    			if(dayOfDate=="Wednesday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagEveCurTakenCount=1;
							    	break;
					    			}
					    		}
							
								if($scope.cyclic.daySE4 == "Y"){
					    			if(dayOfDate=="Thursday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagEveCurTakenCount=1;
							    	break;
					    			}
					    		}
								
								if($scope.cyclic.daySE5 == "Y"){
					    			if(dayOfDate=="Friday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagEveCurTakenCount=1;
							    	break;
					    			}
					    		}
								
								if($scope.cyclic.daySE6 == "Y"){
					    			if(dayOfDate=="Saturday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagEveCurTakenCount=1;
							    	break;
					    			}
					    		}
								
								if($scope.cyclic.daySE7 == "Y"){
					    			if(dayOfDate=="Sunday"){
					    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
							    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
							    	flagEveCurTakenCount=1;
							    	break;
					    			}
					    		}
							}	
							$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagEveCurTakenCount;
				}
			}
   					
				if(remainingOcc>0 && flagForLastDateApp==true){
					 if($scope.setCreatedSlotListForServiceList.length==0){
						 growl.warning(
									'Provided conditions are incorrect',
									{
										title : 'Warning!'
									});
						return false; 
					 }
						var lastDateAppCurrRecc=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
						$scope.currentIterationDate=lastDateAppCurrRecc;
						for(b=1;b<=remainingOcc;b++){
							if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
								break;
							}
							if($scope.currPerReqTakenCount==reqPerRecOcc){
								break;
							}
					    	$scope.setCreatedSlotListForServiceList.push(lastDateAppCurrRecc); 
						}
					}
					
					var appAvail=false;
			       if(counterForApp!=$scope.setCreatedSlotListForServiceList.length){
			    	   counterForApp = angular.copy($scope.setCreatedSlotListForServiceList.length);  
			    	   appAvail=true;
			       }		
					
			       
			       flagForLastDateApp=false,remainingOcc=0;
					//next recurence
					if($scope.setCreatedSlotListForServiceList.length % reqPerRecOcc==0 && $scope.currPerReqTakenCount>0){
						var backDate=new Date($scope.lastRecDate.getTime());
						
						/*if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
							$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
							$scope.tempDate = angular.copy($scope.currentIterationDate);
						}*/
						if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
							$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
							$scope.tempDate = angular.copy($scope.currentIterationDate);
						}
		    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
		    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
		    			 $scope.currPerReqTakenCount=0;
		    			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
								break;
							}
					}else if(appAvail==false && $scope.addDays>1){
						var flagObj=false;
						for(a=1;a<=$scope.addDays;a++){
							if(a%2==1){
								$scope.currentIterationDate=new Date($scope.tempDate.getTime()-(a==1?1:Math.round(a/2))*24*60*60*1000);
							}else{
								$scope.currentIterationDate=new Date($scope.tempDate.getTime()+(a==2?1:a/2)*24*60*60*1000);	
							}
							if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<moment($scope.firstRecDate).format('YYYY-MM-DD')){
								continue;
							}
							
							if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
								continue;
							}
							
							if(($scope.cyclic.daySM1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySM2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySM3 == "Y" && $scope.currentIterationDate.getDay()==3)
			    					|| ($scope.cyclic.daySM4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
			    					|| ($scope.cyclic.daySM6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySM7 == "Y" && $scope.currentIterationDate.getDay()==0) 
			    					||($scope.cyclic.daySE1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySE2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySE3 == "Y" && $scope.currentIterationDate.getDay()==3)
			    					|| ($scope.cyclic.daySE4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
			    					|| ($scope.cyclic.daySE6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
							      flagObj=true;
			    			}
							if(flagObj==true){
								$scope.tempDate = angular.copy($scope.currentIterationDate);
								break;
							}
						}
						
						if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
							break;
						}
						if(flagObj==false){
							if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
									&& $scope.currPerReqTakenCount>0){
								 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
								flagForLastDateApp=true;
							}else{
								if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
									  var backDate=new Date($scope.lastRecDate.getTime());
									$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
									$scope.tempDate = angular.copy($scope.currentIterationDate);
					    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
					    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
					    			 $scope.currPerReqTakenCount=0;
								}else{
									$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
									$scope.tempDate = angular.copy($scope.currentIterationDate);
								}
							}
						}
					}else{
					   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
					   $scope.tempDate = angular.copy($scope.currentIterationDate);
					   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
						   if($scope.currPerReqTakenCount>0){
								 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
								flagForLastDateApp=true;
				        	}else{ 
							     var backDate=new Date($scope.lastRecDate.getTime());
								 $scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
								 $scope.tempDate = angular.copy($scope.currentIterationDate);
				    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
				    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
				    			 $scope.currPerReqTakenCount=0;
				        	}
						}
					   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
							break;
						}
					}
					
					
					//if no slots added then keep counter as previous
					 i=$scope.setCreatedSlotListForServiceList.length==0?0:($scope.setCreatedSlotListForServiceList.length-1);
				}//end of main loop
			        
			//************************place all appointment on last date*************************	
						 if(obj.sessionsS!=$scope.setCreatedSlotListForServiceList.length){
							 if($scope.setCreatedSlotListForServiceList.length==0){
								 growl.warning(
											'Provided conditions are incorrect',
											{
												title : 'Warning!'
											});
								return false; 
							 }
								var lastDateAppObj=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
								for(var K=0;K<obj.sessionsS;K++){
										if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
											break;
										}
										//setCreatedSlotListForService.appointmentDate = moment(lastDateAppCurrRecc,'DD/MM/YYYY').toDate();
								    	$scope.setCreatedSlotListForServiceList.push(lastDateAppObj); 
									}
								 }
			
		    }
					
						$('#showDoctorModalService').modal();
						$scope.stopLoader();		
						console.log($scope.setCreatedSlotListForServiceList);
			
			}
						
							
							$scope.setOccurenceTypeS = function(val){
								$scope.cyclic.occurencesTypeS = val;
							}
							
							$scope.loadingMsg="";
							$scope.getServiceQty = function(obj){
								$scope.loadingMsg="Loading....";
								//obj.patientId = $scope.patientDetails.patientId;
								if($scope.patientDetails.patientId == 0 || $scope.patientDetails.patientId == undefined){
									growl.warning("Please select Patient first!",{
										title : ERROR_MSG
									});
									return;
								}
								$("#pending_service_modal").modal();
								$scope.setCreatedSlotListForService=[];
								var URI1 = BASE_URL + ROOT_URL + API + GLOBAL + UNIT_SERVICE_MASTER_DETAILS + SLASH + $scope.patientDetails.patientId + SLASH + 0 + SLASH + $scope.organizationId + SLASH + $scope.unitId;
								console.log("URI1", URI1);
								$rootScope.startLoader();
								BillingGenericService.serviceAction(METHOD_GET, URI1, "", NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
											console.log(response);
											if (response.data.status == "success")
												$scope.setCreatedSlotListForService = response.data.listObject;
											if($scope.setCreatedSlotListForService.length==0){
												$scope.loadingMsg="No record found....";
											}else{
												$scope.cyclic.billId=0;
											}
                                            angular.forEach($scope.setCreatedSlotListForService,function(value,index)
                                                    {
                                                    var billDate1 =moment(value.billDate,'DD/MM/YYYY').format($scope.startDateFormat.toUpperCase());
                                                    if(billDate1=='Invalid date')
                                                        {
                                                    	billDate1='';
                                                        }else
                                                            {
                                                        	billDate1=billDate1;
                                                            }
                                                    value["billDate1"]=billDate1;
                                                    });
												//$scope.cyclic.sessionsS = response.data.listObject[0].balancePackageQuantity;
											$rootScope.stopLoader();
										});
							}
							
							$scope.orderDetailList=[];
                            $scope.disableShowBtn=false;
							$scope.setServiceDetails = function(obj){
								//console.log(JSON.stringify(obj));
								$scope.cyclic.serviceName = obj.serviceStandardName;
								$scope.cyclic.sessionsS = obj.availableQty;
								 $scope.cyclic.serviceAmount= obj.serviceAmount;
								 $scope.cyclic.serviceMasterId= obj.serviceMasterId;
								 $scope.cyclic.subSpecialityId=obj.subSpecialityId;
								 $scope.cyclic.specialityId=obj.specialityId;
								 $scope.cyclic.taxId=obj.taxId;
								 $scope.cyclic.taxPercentage= obj.taxPercentage;
								 $scope.cyclic.packageId=obj.packageId;
								 $scope.cyclic.packageConsumptionMasterId =obj.packageConsumptionMasterId;
								 $scope.cyclic.packageConsumptionServiceDetailId =obj.packageConsumptionServiceDetailId;
								 $scope.cyclic.packageEoDetailId =obj.packageEoDetailId;
								 $scope.cyclic.billId=obj.id;
								 $scope.cyclic.encounterId=obj.encounterId;
								 $scope.cyclic.packageDuration=obj.packageDuration;
								 $scope.cyclic.packageValidityToDate=obj.packageValidityToDate;
								 $scope.cyclic.admissionId=obj.admissionId;
								 $scope.cyclic.orderId=obj.orderId;
								 $scope.cyclic.visitTypeId=obj.visitTypeId;
								  $scope.disableShowBtn=false;
                                 if(obj.packageId>0){
                                    if(obj.packageId>0){
                                    var pckValidityToDate = new Date(obj.packageValidityToDate);
                                    var currentDate = moment(new Date(), "DD-MM-YYYY").toDate();
                                    if(moment(new Date(pckValidityToDate)).format('YYYY-MM-DD') >= moment(new Date(currentDate)).format('YYYY-MM-DD')){
                                    var length = Math.floor((Date.UTC(pckValidityToDate.getFullYear(), pckValidityToDate.getMonth(),
                                            pckValidityToDate.getDate()) - Date.UTC(currentDate.getFullYear(), 
                                                    currentDate.getMonth(), currentDate.getDate()))
                                            / (1000 * 60 * 60 * 24));
                                        // $scope.cyclic.durationS = $scope.cyclic.packageDuration>0?$scope.cyclic.packageDuration+1:$scope.cyclic.packageDuration;
                                       $scope.cyclic.durationS=length+1;   
                                       $scope.cyclic.durationTypeS='d';
                                     }else{
                                         $scope.cyclic.durationS=0;
                                         $scope.disableShowBtn=true;
                                     }
                                    }
                                 }
                                 
								//for without package service
								 if(obj.packageId==0){
									 $scope.orderDetailList=obj.orderList;
								 }
							}
							
							$scope.closeServicePopup=function(){
								if($scope.cyclic.billId==0){
									growl.warning("Please select service",{
										title : ERROR_MSG
									});
									return;
								}else{
									 angular.element('#pending_service_modal').modal('hide');
								}
							}
							
						$scope.checkDateTime = function(slobObj)
							{
							  if($scope.currentDateCyclic==moment($scope.datepicker).format('YYYY-MM-DD')){
								  if(slobObj.originalFromTime>$scope.currentTime)
									 return true;
								  else
									  return false;
							  }else{
								 return true;
							  }
								
						}
						
					/*	slotPatientDetailsListsSize
						allowedOverBookingSlots*/
						
						$scope.getOverBookSlotCheckPopup = function(item)
						{
							//alert($scope.allowedOverBookingSlots);
							if($scope.allowedOverBookingSlots > 0)
								{
									var data = {
											slotId : item.slotId
									}
									
									//0- Over booking Not Allowed
									//1- Over booking Allowed
									var URI = BASE_URL + ROOT_URL + ISOVERBOOKINGALLOWEDORNOT;
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														if(response.data.object==true)
															{
															
																$scope.doctorName = item.doctorName;
																$scope.specialityname = item.specialityName;
																$scope.fromTime = item.fromTimeString2;
																$scope.toTime = item.toTimeString2;
																$scope.appointmentDate = item.appointmentDate;
																$scope.slotId = item.slotId;
																$scope.slotStatusId = 6;
																$scope.priorityId  = 2;
																$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
															}

													});
									
								}
							
						}
						
						
						$scope.checkDate=function(slobObj){
							if(moment(slobObj.appointmentDate2).format('YYYY-MM-DD')<$scope.currentDateCyclic){
									  return true;
							  }else{
								 return false;
							  }
						}
						
						
						$scope.templateDoctorList =[];
						$scope.getDoctorBySpecialityForTemplate = function() {
							
							var data = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId
							};
							
							$scope.startLoader();
							
							var URI = BASE_URL + ROOT_URL + "/api/global/getDoctorListByOrgId";
							BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
										
												if (response.data.status == "success"){
													$scope.templateDoctorList =[];
													$scope.templateDoctorList = response.data.listObject;
													$scope.stopLoader();
												}else{
													$scope.stopLoader();
												}
											});
								}	
						
						$scope.getDoctorBySpecialityForTemplate();
						$scope.templateId = 0;
						$scope.appointmentSavedMasterList = [];
						$scope.getAppointmentSavedMasterList = function(){
							$scope.appointmentSavedMasterList = [];
							$rootScope.startLoader();
							
							var serData = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
							}
									
								var URI = BASE_URL + ROOT_URL + "/api/getAppointmentTemplate";
								BillingGenericService.serviceAction(METHOD_POST,URI,serData,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
											$rootScope.stopLoader();
											
											 angular.forEach(response.data.listObject,function(value,key){
												 
												 if(value.status == 'A'){
													 $scope.appointmentSavedMasterList.push(value);
												 }
												 
											 });
											
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
									}
						
								});
						}
						
						
						
						$scope.getAppointmentSavedMasterList();
						
						$scope.appointmentTemplateMasterMapperList = [];
						$scope.getAppointmentTemplateMasterMapperData= function() {
						var info = {};
						
						$scope.patientDetails.patientId
						
						if($scope.patientDetails.patientId == undefined || $scope.patientDetails.patientId == null || $scope.patientDetails.patientId == ""){
							growl.warning("Please select Patient",{
								title : "Warning"
							});
							return;
						}
						
						
							if($scope.templateId == 0){
								growl.warning("Please select Template",{
									title : "Warning"
								});
								return;
							}else{
								
								angular.forEach($scope.appointmentSavedMasterList,function(val,inx){
									if(val.appointmentTemplateMasterId == $scope.templateId ){
										info = val;
									}
								});
								
							}
							
							
							$rootScope.startLoader();
							
							var serData = {
									appointmentTemplateMasterId : info.appointmentTemplateMasterId,
							}
									
								var URI = BASE_URL + ROOT_URL + "/api/getAppointmentTemplateMapperData";
								BillingGenericService.serviceAction(METHOD_POST,URI,serData,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
											$rootScope.stopLoader();
											 $scope.appointmentTemplateMasterMapperList = [];
											 $scope.appointmentTemplateMasterMapperList = response.data.listObject; 
											 $scope.templateMasterDoctorList = [];
											 $scope.templateMasterServiceList = [];
											 $scope.doctorInx = 1;
											 $scope.serviceInx = 1;
											 angular.forEach($scope.appointmentTemplateMasterMapperList,function(value,key){
										          
													angular.forEach(value.appointmentTemplateServiceList,function(serObj,key1){
												       var doctorList = [];
														angular.forEach($scope.templateDoctorList,function(docObj,inx){
													          
															if(serObj.consultantSpecialityId != null && serObj.consultantSpecialityId == docObj.specialityId){
																doctorList.push(docObj);
															}
															
														});
														
														if(serObj.consultantSpecialityId != null){
															serObj.doctorList = doctorList;
														}
														
														if(serObj.consultantSpecialityId != null){
															serObj.doctorId = null;
															serObj.appointmentTime = null;
															serObj.sessionId = $scope.doctorInx;
															$scope.mydate = new Date();
															  $scope.newdate = $scope.mydate.setDate($scope.mydate.getDate() +  value.days); 
															 serObj.appointmentDate =   new Date($scope.newdate);//moment(new Date($scope.newdate)).format('YYYY-MM-DD');
															$scope.templateMasterDoctorList.push(serObj);
															$scope.doctorInx = $scope.doctorInx + 1;
														}else{
															 serObj.sessionId = $scope.serviceInx;
															 serObj.appointmentTime = $scope.docMorningStartTime;
															 $scope.mydate = new Date();
															  $scope.newdate = $scope.mydate.setDate($scope.mydate.getDate() +  value.days); 
															 serObj.appointmentDate = new Date($scope.newdate);//moment(new Date($scope.newdate)).format('YYYY-MM-DD');
															 $scope.templateMasterServiceList.push(serObj);
															 $scope.serviceInx =  $scope.serviceInx + 1;
														}
														
													});
										     });
											 
											 $scope.buttonDisbl = false;
											 angular.element('#showTableModal').modal('show');
											 
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
									}
						
								});
						}
						
						
						$scope.dateOptionsAppointmentService = {
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : new Date()
						};


						$scope.datePickerAppointmentService = {
								opened : [ false ],

						};
						
						$scope.openDatePickerAppointmentService = function(index) {
							for (var i = $scope.templateMasterServiceList.length - 1; i >= 0; i--) {
								if (i === index) {

									$scope.datePickerAppointmentService.opened[i] = true;
								} else {
									$scope.datePickerAppointmentService.opened[i] = false;
								}
							}
						};
						
						$scope.dateOptionsAppointmentDoctor = {
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : new Date()
						};
						
						$scope.datePickerAppointmentDoctor = {
								opened : [ false ],

						};
						
						$scope.openDatePickerAppointmentDoctor = function(index) {
							for (var i = $scope.templateMasterDoctorList.length - 1; i >= 0; i--) {
								if (i === index) {

									$scope.datePickerAppointmentDoctor.opened[i] = true;
								} else {
									$scope.datePickerAppointmentDoctor.opened[i] = false;
								}
							}
						};
						//appointmentDate :moment(obj.appointmentDate).format('YYYY-MM-DD'),
						$scope.getDoctorSlots = function(obj,index){
							var deptWiseAppointment = null;
							angular.forEach($scope.templateDoctorList,function(docObj,inx){
								if(docObj.doctorId == obj.doctorId){
									deptWiseAppointment = docObj.deptWiseAppointment;
								}
								
							})
							
							var data = {
									specialityId : obj.consultantSpecialityId,
									doctorId :(deptWiseAppointment != null && deptWiseAppointment == 'N') ? obj.doctorId : undefined,
									appointmentDate :moment(obj.appointmentDate).format('YYYY-MM-DD'),
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : (deptWiseAppointment != null && deptWiseAppointment == 'N') ? 1 : 4
								};
							
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
							BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
										console.log(response.data.listObject);
										if (response.data.status == "success") {
													
											$scope.doctorSlotList = [];
													if(moment(obj.appointmentDate).format('YYYY-MM-DD') == moment(new Date()).format('YYYY-MM-DD')){
														
														angular.forEach(response.data.listObject,function(val,inx){
															
															if(val.originalFromTime >= moment(new Date()).format('HH:mm')){
																$scope.doctorSlotList.push(val);
															}
															
														});
														
														
													}else{
														$scope.doctorSlotList = response.data.listObject;
													}
													
													var checkExits = false;
													angular.forEach($scope.doctorSlotList,function(val,inx){
														
														if(val.slotPatientDetailsList == null && checkExits == false){
															
															var checkSlot = false;
															angular.forEach($scope.templateMasterDoctorList,function(val1,inx){
																
																if(val1.slotId != undefined && val1.slotId != null && val.slotId == val1.slotId){
																	checkSlot = true;
																}
																
															});
															if(checkSlot == false){
																$scope.templateMasterDoctorList[index].slotId = val.slotId;
																$scope.templateMasterDoctorList[index].appointmentTime = val.fromTimeString;
																checkExits = true;
															}
															
														}
													});
													
													
												
													
													$rootScope.stopLoader();
													if($scope.doctorSlotList.length==0){
														$scope.loadingMsg="Slots are not created for selected date...!";
														$scope.templateMasterDoctorList[index].slotId = null;
														$scope.templateMasterDoctorList[index].appointmentTime = "";
													}
												}
												$rootScope.stopLoader();

											});
							
						}
						
						$scope.templateIndex = null;
						$scope.getTimeSlotsByDateForTemplate = function(obj,index){
							$scope.templateIndex = index;
							var deptWiseAppointment = null;
							angular.forEach($scope.templateDoctorList,function(docObj,inx){
								if(docObj.doctorId == obj.doctorId){
									deptWiseAppointment = docObj.deptWiseAppointment;
								}
								
							})
							
							var data = {
									specialityId : obj.consultantSpecialityId,
									doctorId :(deptWiseAppointment != null && deptWiseAppointment == 'N') ? obj.doctorId : undefined,
									appointmentDate :moment(obj.appointmentDate).format('YYYY-MM-DD'),
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : (deptWiseAppointment != null && deptWiseAppointment == 'N') ? 1 : 4
								};
							
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
							BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
										console.log(response.data.listObject);
										if (response.data.status == "success") {
											
                                            $scope.setCreatedTimeSlotList = [];
                                            if(moment(obj.appointmentDate).format('YYYY-MM-DD') == moment(new Date()).format('YYYY-MM-DD')){
                                                
                                                angular.forEach(response.data.listObject,function(val,inx){
                                                    
                                                    if(val.originalFromTime >= moment(new Date()).format('HH:mm')){
                                                        $scope.setCreatedTimeSlotList.push(val);
                                                    }
                                                    
                                                });
                                                
                                            }else{
                                            	$scope.setCreatedTimeSlotList = response.data.listObject;
                                            }
											
											
											
											if($scope.setCreatedTimeSlotList.length==0){
												$scope.loadingMsg="Slots are not created for selected date...!";
											}
												}
												$rootScope.stopLoader();

											});
							
						}
						
						
						$scope.slotBookingTemplate = function(info){
							$scope.templateMasterDoctorList[$scope.templateIndex].slotId = info.slotId;
							$scope.templateMasterDoctorList[$scope.templateIndex].appointmentTime = info.fromTimeString;
							
							 angular.element('#template_time_slot_modal').modal('hide');
						}
						
						$scope.buttonDisbl = false;
						
						$scope.saveTemplateAppointment = function(){
							$scope.buttonDisbl = false;
							var patientConsultationAppointment = [];
							
							var checkAllEnterData = false;
							
							angular.forEach($scope.templateMasterDoctorList,function(docObj,inx){
							
								if(docObj.slotId != undefined && docObj.slotId != null){
									var obj = {
										allowedOverBookingSlots:3,
										appointmentDate: moment(docObj.appointmentDate).format('YYYY-MM-DD'),
										appointmentTypeId: 1,
										createdBy: $scope.userId,
										doctorId: docObj.doctorId,
										fromTimeString:docObj.appointmentTime,
										generateToken: "N",
										isBlockedUnblocked: 2,
										isCyclicAppointment: "Y",
										isFromCoversheet: "N",
										isVip: "N",
										organizationId: $scope.organizationId,
										originalFromTime: docObj.appointmentTime,
										patientId: $scope.patientDetails.patientId,
										registrationNumber: null,
										result: false,
										slotId:  docObj.slotId,
										slotPatientDetailsList: null,
										slotPatientDetailsListsSize: 0,
										slotStatusId: 4,
										slotTypeId: null,
										specialityId: docObj.consultantSpecialityId,
										status: "A",
										toTimeString: docObj.appointmentTime,
										unitId:$scope.unitId,
										updatedBy:$scope.userId
									};
									
									patientConsultationAppointment.push(obj);
									
								}else{
									checkAllEnterData = true;
								}
								
							})
							if(checkAllEnterData == true){
								growl.warning("Please enter all data",{
									title : "Warning"
								});
								return;
							}
							
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/opd/cyclicAppointmentScheduling";
							BillingGenericService.serviceAction(METHOD_POST, URI,patientConsultationAppointment,NOTIFICATION_MSG_STATUS_FALSE)
							.then(function(response) {
								console.log(response);
								if (response.data.status == "success") {
										growl.success("Appointments Added Successfully",
												{
													title : 'Success!'
												});
								  } 
								$scope.getPatientDetailsById($scope.patientDetails.patientId);
								$rootScope.stopLoader();
								$scope.init();
								});
							
							
							var dateList = [];
							angular.forEach($scope.templateMasterServiceList,function(serv,inx){
								
								if(dateList.length == 0){
									
									dateList.push(moment(serv.appointmentDate).format('YYYY-MM-DD'))
									
								}else{
									var checkDate = false;
									angular.forEach(dateList,function(date,index){
										if(moment(serv.appointmentDate).format('YYYY-MM-DD') === moment(date).format('YYYY-MM-DD')){
											checkDate = true;
										}
									});
									
									if(checkDate == false ){
										dateList.push(moment(serv.appointmentDate).format('YYYY-MM-DD'))
									}
								}
							
								
							});
							
							
						
							$rootScope.startLoader();
							var listOrderMasterDto = [];
							var finalData = {
								orgId : $scope.organizationId,
								orgUnitId : $scope.unitId,
							};
							
							
							angular.forEach(dateList,function(date,index){
								
									var data = {
		
										currentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										createdDate : new Date().getTime(),
										updatedDate : new Date().getTime(),
										orgId : $scope.organizationId,
										orgUnitId : $scope.unitId,
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										patientId : $scope.patientDetails.patientId,
										encounterId : null,
										defaultSelfTariffId : $scope.defaultSelfTariffId,
										doctorId : null,
										priorityId : 2,
										visitTypeId : 1,
										orderDate : new Date().getTime(),
										orderStatus : 'A',
										orderStatusId : 1,
										payeeId : $scope.selfPayeeId,
										billingBedCategoryId : $scope.opdBillingCatId,
										paymentEntitlementTypeId :1,
										isPreOrPostBilling :$scope.isPreOrPostBilling
									}
									var finalOrderServiceList = [];
									angular.forEach($scope.templateMasterServiceList,function(value, key) {
														
													if(moment(value.appointmentDate).format('YYYY-MM-DD') === moment(date).format('YYYY-MM-DD')){
														var newArr = {};
														newArr.serviceId = value.serviceId;
														newArr.serviceRenderingDeptId = value.subSpecialityId;
														newArr.status = 'A';
														newArr.serviceRendered = 0;
														newArr.isPackageService = "N";
														newArr.nursingServiceOrderFlag = "N",
														newArr.servicePriorityId = 1,
														newArr.departmentId = value.specialityId;
														newArr.subDepartmentId = value.subSpecialityId;
														newArr.orderSchDate =   new Date(value.appointmentDate).getTime(),
														newArr.isInvestigationOrder = 'N';
													
														finalOrderServiceList.push(newArr);
													}
														
											});
									
									data.listOrderDetailsMasterDto = finalOrderServiceList;
		
									listOrderMasterDto.push(data);
							});
							
							finalData.addToBill = false,
							finalData.listOrderMasterDto = listOrderMasterDto;
						
								var URI = BASE_URL + ROOT_URL+ "/api/opd/saveInvestigationForAppointmentTemplate"
										BillingGenericService.serviceAction(METHOD_POST, URI,finalData,NOTIFICATION_MSG_STATUS_TRUE).then(
												function(response) {
													if (response.data.status == "success") 
													{
														
													}
												});
								$scope.buttonDisbl = true;
								 angular.element('#showTableModal').modal('hide');
							
						}
						
						
						} ]);