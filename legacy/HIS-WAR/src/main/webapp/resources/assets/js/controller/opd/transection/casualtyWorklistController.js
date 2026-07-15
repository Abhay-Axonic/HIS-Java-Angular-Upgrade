'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:casualtyWorklistController
 * @description #casualtyWorklistController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'casualtyWorklistController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceParamHeader',
						'GenericServiceFileUpload',
						'growl',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl) {

							/*alert("encounterQueueManagementController");*/

							/* init() function for form object create. */
							$scope.init = function() {

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};

								

								// for datePicker2
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
								$scope.openDatePickerPdd = function() {
									$scope.datepickerPdd.opened = true;
								};

								$scope.datepickerPdd = {
									opened : false
								};

								
								$scope.datepicker = new Date();
								$scope.datepickerPdd = new Date();

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.roleId = 2;
								$scope.cookieDoctorId = cookieObject.doctorId;
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								 
								$scope.casualtySpecialityId = cookieObjectAppConfig.casualtySpecialityId;
								$scope.isKiosk = cookieObjectAppConfig.isKiosk;
                                $scope.isKioskAnnouncementRequired = cookieObjectAppConfig.isKioskAnnouncementRequired;
								//alert($scope.cookieDoctorId);
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								console.log('cookieObject',cookieObject);

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};
								var headerObj = {
										organization_id:$scope.organizationId,
										unit_id : $scope.unitId
								}
								
								 $scope.encounterSearchObject = {
							    		 'organizationId': $scope.organizationId,
										 'unitId':$scope.unitId,
							     }
								
								//
								
								//get cabin list by doctor id
								var URI = BASE_URL + ROOT_URL + "/api/opd/getCabinListByDoctorId/"+$scope.cookieDoctorId+"/"+$scope.unitId;
								GenericService
								.serviceAction("GET", URI, "")
								.then(
										function(response) {
											//$scope.listOfAdmittedPatientList = [];
											$scope.cabinListForDoctor = [];

											if (response.data.status == "success") {
												$scope.cabinListForDoctor = response.data.listObject;
												
												if($rootScope.doctorCabinObject == undefined)
										    	 {
										    	 
										    	 if($scope.cabinListForDoctor.length > 0)
										    		 {
										    		 	if($scope.cabinListForDoctor.length==1)
										    		 		{
										    		 			$rootScope.doctorCabinObject = $scope.cabinListForDoctor[0];
										    		 		}else{
										    		 			angular.element('#cabinModal').modal('show');   
										    		 		}
										    		 }
										    	 
										    	 }
												
											}
										});
								
								//end cabin list
										
								
										  
										     
								//
						
							}

							$scope.init();
						
							
							$scope.selectsetCabin = function()
							{
								if($scope.doctorCabin == undefined || $scope.doctorCabin.length == 0){
									growl.warning('Please select cabin..!!');
									return false;
								}else{
								  $rootScope.doctorCabinObject = JSON.parse($scope.doctorCabin);
								  //$scope.doctorCabinObject = JSON.parse($scope.doctorCabin);
								  console.log("$scope.doctorCabinObject",$rootScope.doctorCabinObject);
								  angular.element('#cabinModal').modal('hide');
								}
							}
							
							// console.log("$rootScope.doctorCabinObject",$rootScope.doctorCabinObject);  
						     if($rootScope.doctorCabinObject == undefined)
						    	 {
						    	   angular.element('#cabinModal').modal('show');   
						    	 }
						     
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								//$scope.saveAppointmentSchedule();
							}
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function()
							{
								if($scope.cookieDoctorId != null)
								{
									
							     var docFlag = false;	
								 var specDocObj = {
							    			unitId:$scope.unitId,
							    			organizationId:$scope.organizationId,
							    			doctorId:$scope.cookieDoctorId
							    }
								 
								var URI = BASE_URL + ROOT_URL
								+ GETSPECIALITYLISTBYDOCTORID;
						BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											
											// $rootScope.startLoader();
											if (response.data.status == "success")
												{
													
												  
												  var docSpeIds = [];
												  $scope.doctorCasName = null;
												  $scope.docSpecialityList = response.data.listObject;
												  
												  angular.forEach($scope.docSpecialityList,function(value,index){
													   docSpeIds.push(value.specialityId);
													   if(value.specialityId == $scope.casualtySpecialityId)
														   {
														   $scope.doctorCasName = value.doctorName;
														   }
													});
													
												  console.log("$scope.docSpecialityList",$scope.docSpecialityList);
												  if(docSpeIds.indexOf($scope.casualtySpecialityId) == -1){
													 // alert("faalse");
													}else{
														docFlag = true;
														
														$scope.encounterSearchObject.fromDate = moment($scope.datepicker).format('YYYY-MM-DD');
														$scope.encounterSearchObject.toDate = moment($scope.datepickerPdd).format('YYYY-MM-DD');
														
														console.log("FT",$scope.datepicker);
														console.log("FT",$scope.datepickerPdd);
														if($scope.cookieDoctorId != null && docFlag == true)
														{
															//$scope.encounterSearchObject.doctorId = $scope.cookieDoctorId;
															//$scope.encounterSearchObject.casualtySpecialityId = $scope.casualtySpecialityId;
															console.log("$scope.encounterSearchObject",$scope.encounterSearchObject);
															  //  return false;
															
													    var URI = BASE_URL + ROOT_URL
														+ GETCASUALTYENCOUNTERLIST;
													   
														GenericServiceParamHeader
														.serviceAction(METHOD_POST, URI,
																$scope.encounterSearchObject,
																NOTIFICATION_MSG_STATUS_FALSE,"","")
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	$scope.allEncounterList = [];
																	if (response.data.status == "success")
																		
																		$scope.allEncounterList =response.data.listObject;
																	console.log("$scope.allEncounterList",$scope.allEncounterList);
																});
													   }
														//alert("true");
													}
												}
												
										});
							 }
						
							}
							$scope.searchEncounter();
							
							
							$scope.callPatient = function(info,idx)
							{
								
								
								if($rootScope.doctorCabinObject != undefined || $rootScope.doctorCabinObject != null)
								{
							if(info.callCount < 3)
								{	
									if(info.callCount < 2){
										if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
												$scope.isKioskAnnouncementRequired == 'Y'){
											 $scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $rootScope.doctorCabinObject.cabinDesc+' , thank you .');
										}
									}
									
									if(info.callCount == 2){
										if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
												$scope.isKioskAnnouncementRequired == 'Y'){
											$scope.speech(' Token number, '+info.tokenNumber+', please visit '+ $rootScope.doctorCabinObject.cabinDesc+' , This is Your Last call, thank you .');
										}
									}
									
									info.callCount = info.callCount + 1;
								
								console.log('info.callCount',info.callCount);	
								var URI = BASE_URL + ROOT_URL + "/api/opd/nursing/kiosk/callCount/"+info.encounterId+"/"+info.callCount+"/"+0;
								
								GenericService
								.serviceAction("GET",URI,'')
								.then(function(response) {
											console.log("reee",response);
											if (response.data.status == "success") {
												
												if(info.cabinId == null){
													//start for update doctorId
													var updObj = {
															encounterId : info.encounterId,
															doctorId : $scope.cookieDoctorId,
															unitId:$scope.unitId,
											    			organizationId:$scope.organizationId,
											    			cabinId : $rootScope.doctorCabinObject.cabinId,
											    			doctorDeskArrival: "N"
													}
																											
														//console.log(data);
														//return false;
														var URI = BASE_URL + ROOT_URL
																+ UPDATEDOCTORIDFORENCOUNTER; 

														BillingGenericService
																.serviceAction(METHOD_POST,
																		URI, updObj,
																		NOTIFICATION_MSG_STATUS_TRUE)
																.then(
																		function(response) {
																			console
																					.log(response);
																			// $rootScope.startLoader();
																			if (response.data.status == "success")
																				{
																					$scope.allEncounterList[idx].cabinId = response.data.object;
																					$scope.allEncounterList[idx].doctorDeskArrival = "N";
																				}
																			
																				 
																		});
												}
														//End for update doctorId
												
											}
										});
								}
						}
							else
								{
								   angular.element('#cabinModal').modal('show'); 
								}
							}
							
							
							
							
							 $scope.markArrival=function(encounterObj,idx)
								{
									
								 if($rootScope.doctorCabinObject != undefined || $rootScope.doctorCabinObject != null)
									{
								//start for update doctorId
									var updObj = {
											encounterId : encounterObj.encounterId,
											doctorId : $scope.cookieDoctorId,
											unitId:$scope.unitId,
							    			organizationId:$scope.organizationId,
							    			cabinId : $rootScope.doctorCabinObject.cabinId,
							    			doctorDeskArrival: "Y"
									}
																							
										//console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ UPDATEDOCTORIDFORENCOUNTER;

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, updObj,
														NOTIFICATION_MSG_STATUS_TRUE)
												.then(
														function(response) {
															console
																	.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																{
																	$scope.allEncounterList[idx].cabinId = response.data.object;
																	$scope.allEncounterList[idx].doctorDeskArrival = "Y";
																}
															
																 
														});
										//End for update doctorId
									}
									
								}
							 
							 $scope.speech = function (theText) {
							      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
							  };
							 
							 
							$scope.gotoCoverSheet = function(encounterObj)
							{
								//alert();
							if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '2' ){
								if(encounterObj.consultationStartTime == null || encounterObj.consultationStartTime == '')
								{
									//alert("CONS START TIME NULL");
									var headerObj={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
										var data = {
											'assignmentStartTime':encounterObj.assignmentStartTimeDate,
											'assignmentStopTime':encounterObj.assignmentStopTimeDate,
											'isAssignmentCompStatus':encounterObj.isAssignmentCompStatus,
											'assignmentStartBy':encounterObj.assignmentStartBy,
											'assignmentStopBy': encounterObj.assignmentStopBy,
											'assignmentStartRoleId':encounterObj.assignmentStartRoleId,
											'assignmentStopRoleId': encounterObj.assignmentStopRoleId,
											'isConsultationStatus': "N",
											'consultationStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											'consultationStopTime': encounterObj.consultationStopTime,
											'consultationStartBy': $scope.userId,
											'consultationStopBy': "",
											'consultationStartRoleId':$scope.roleId,
											'consultationStopRoleId': "",
											'encounterId': encounterObj.encounterId,
											'patientId': encounterObj.patientId
										}
										
											
										console.log('Consultant DATA',data);
										//return false;
										var paramObjForPer={};
										var URI = BASE_URL + ROOT_URL
										+ ENCOUNTERDETAILS;
										GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
											.then(
													function(response) {
														console.log(response);
														console.log(encounterObj.encounterId);
														
														// $rootScope.startLoader();
														if (response.data.status == "success")
															{
																console.log("in success");
																var cookieObject = 
																{
																		"patientName" :encounterObj.patientName,
																		"uhidNumber" : encounterObj.uhidNumber,
																		"genderCode" : encounterObj.genderCode,
																		"age":  encounterObj.age,
																		"dob": encounterObj.dob,
																		"encounterId":encounterObj.encounterId,
																		"genderId":encounterObj.genderId,
																		"roleId" :$scope.roleId,
																		"patientId":encounterObj.patientId,
																		"doctorId":encounterObj.DoctorId,
																		"specialityId":encounterObj.specialityId,
																		"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																		"doctorName":encounterObj.doctorName,
																		"prefixId":encounterObj.prefixId,
																		"genderId":encounterObj.genderId,
																		"appointmentId":encounterObj.appointmentId,
																		"visitTypeId" :1,
																		"contractId" : encounterObj.contractId,
																		"payeeId" : encounterObj.payeeId,
																		"patientCategoryId" : encounterObj.patientCategoryId
																		
																}
																 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
																console.log("EO",cookieObject);
																//return false;
																$state.go("OPDCoverSheet");
														
													}
													});
										
								}
								else
								{
									
									//alert("CON ADDED");
									//return false;
									var cookieObject = 
									{
											"patientName" :encounterObj.patientName,
											"uhidNumber" : encounterObj.uhidNumber,
											"genderCode" : encounterObj.genderCode,
											"age":  encounterObj.age,
											"dob": encounterObj.dob,
											"encounterId":encounterObj.encounterId,
											"genderId":encounterObj.genderId,
											"roleId" :$scope.roleId,
											"patientId":encounterObj.patientId,
											"doctorId":encounterObj.doctorId,
											"specialityId":encounterObj.specialityId,
											"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
											"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1,
											"contractId" : encounterObj.contractId,
											"payeeId" : encounterObj.payeeId,
											"patientCategoryId" : encounterObj.patientCategoryId
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
							$interval(function() {
								$scope.searchEncounter();
					          }, 10000);
							
						 
						} ]);