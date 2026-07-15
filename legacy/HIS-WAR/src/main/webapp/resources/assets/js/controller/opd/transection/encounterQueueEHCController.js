/**
 * encounterQueueEHCController.js
 */
'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:encounterQueueEHCController
 * @description #encounterQueueEHCController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'encounterQueueEHCController',
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
						'reportService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl,reportService) {

							
							
							
							
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
								 $state.go('applicationConfiguration');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
							$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
							$scope.casualtySpecialityId = cookieObjectAppConfig.casualtySpecialityId;
							$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
							$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
							$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
							$scope.nurseRole = cookieObjectAppConfig.nurseRole;	
							$scope.roleName = cookieObjectAppConfig.roleName;
							
							$scope.roleId = 3;
							$scope.init = function() {

								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
								
								
								//Date Object For Search DOB
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};
								
								//Date Object For Reshedule
								$scope.openDatePickerReschedule = function() {
									$scope.datepickerReschedule.opened = true;
								};

								$scope.datepickerReschedule = {
									opened : false
								};

								//Date For From Date on Search
								
								$scope.openDatePickerFromDate = function() {
										$scope.datepickerFromDate.opened = true;
								};

								$scope.datepickerFromDate = {
										opened : false
								};
								
								//Date For To Date on Search
								
								$scope.openDatePickerToDate = function() {
										$scope.datepickerToDate.opened = true;
								};

								$scope.datepickerToDate = {
										opened : false
								};
								$scope.slotDate=moment(new Date()).format('DD-MM-YYYY');
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.slotTypeId = 1;

								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};
								var headerObj = {
										organization_id:$scope.organizationId,
										unit_id : $scope.unitId
								}
								var paramObj = {};

								//For Get Nursingstation List
								var URI = BASE_URL + ROOT_URL
										+ GETLISTOFNURSINGSTATION;
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.nursingStationList = response.data.listObject;
												});
												
												//For Get Nursingstation List
												var URI = BASE_URL + ROOT_URL
														+ GETLISTOFAPPOINTMENTSTATUSMASTER;
												GenericServiceParamHeader
														.serviceAction(METHOD_GET, URI,
																data,
																NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		$scope.encounterStatusList = response.data.listObject;
																});
								
								//For Get Active Reason List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});
								
								//For Get Active Speciaity List
									var URI = BASE_URL + ROOT_URL
										+ GETACTIVESPECIALITY;
								     BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.specialityList = response.data.listObject;
												});
								     
								     $scope.encounterSearchObject = {
								    		 'doctorId':0,
								    		 'specialityId':0,
								    		 'patientId':0,
								    		 'specialityId':0,
								    		 'statusId':0,
								    		 'clinicId':0,
								    		 'nursingStationId':0
								     }
							}

							$scope.init();
							
							$scope.getClassForColor = function(info)
							{
								   
								   if(info.isCriticalOrder==1){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
								   }else if(info.encounterTypeId==6){
									   return 'td-bg-green';
								   }
							}
							
							$scope.nursingStationChange = function()
							{
								$scope.encounterSearchObject.doctorId = '0';
								$scope.encounterSearchObject.clinicId = '0';
							}
							
							$scope.searchEncounter = function()
							{
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL + "/api/opd/ehcPatients/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/"+$scope.isPreOrPostBilling;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI,
										"",
										NOTIFICATION_MSG_STATUS_FALSE,"","")
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success")
												
												$scope.allEncounterList =response.data.listObject;
										});
						
							}
							$scope.searchEncounter();
							
							
						
							$scope.clearSearchObject = function()
							{
								$scope.encounterSearchObject = {
							    		 'doctorId':0,
							    		 'specialityId':0,
							    		 'organizationId':$scope.organizationId,
							    		 'unitId':$scope.unitId,
							    		 'patientId':0,
							    		 'specialityId':0,
							    		 'statusId':0,
							    		 'nursingStationId':0,
							    		 'clinicId':0
							     }
								
								
								$scope.patientName ='';
								$scope.uhidNumber = '';
								$scope.mobileNumber = '';
								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							}
							
							
							$scope.getRefralDetails = function(item)
							{
								if(item.referralName==null)
									{
									growl.warning("No referral details found.",{
										title : WARNING_MSG
									});
									return;
									}
								else{
									$scope.referralTypeName = item.referralTypeName;
									$scope.referralName = item.referralName;
									$scope.referralRemark = item.referralRemark;
									$('#detailsForReferal').modal('show');
								}
								
							}
							
							$scope.gotoCoverSheet = function(encounterObj)
							{
							if(encounterObj.statusId != '1'  && $scope.roleId == '3' ){
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
														if (data.status == "success")
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
																	"doctorId":encounterObj.doctorId,
																	"specialityId":encounterObj.specialityId,
																	"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																	"doctorName":encounterObj.doctorName,
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1,
																	"patientCategoryId" :encounterObj.patientCategoryId
																	
															}
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															if($scope.roleName=="nurse"){
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
											"patientCategoryId" : encounterObj.patientCategoryId
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									if($scope.roleName=="nurse"){
										$state.go("OPDCoverSheet");
									}
								}
								
							}
						 }
							
							
					         $scope.getmappedDataCoversheetByUser = function(){
									
									var URI = BASE_URL + ROOT_URL + "/api/opd/getMappedCoversheetDataForUser";
									var data = {
											  userId:$scope.userId,
										      unitId:$scope.unitId
									}
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if(response.data.status == "success"){
										$rootScope.stopLoader();					
										$scope.mappedDataCoversheetByUser = [];
										$scope.mappedDataCoversheetByUser = JSON.parse(response.data.listObject[0].tabAccessIds);
										localStorage.setItem("mappedDataCoversheetByUser", JSON.stringify($scope.mappedDataCoversheetByUser));
										
										console.log("mappedDataCoversheetByUser",$scope.mappedDataCoversheetByUser);
										}
									});
									}
								$scope.getmappedDataCoversheetByUser();
								
							$scope.selectedEncServies = [];
							
							$scope.gotoCoverSheetAssigment = function(encounterObj)
							{
								//alert();
							if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '3'){
								
								if(encounterObj.assignmentStartTime == null || encounterObj.assignmentStartTime == '')
								{
									//alert('ASSIGMENT START NULL');
									var headerObj={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
										var data = {
											'assignmentStartTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											'assignmentStopTime':null,
											'isAssignmentCompStatus':'N',
											'assignmentStartBy':$scope.userId,
											'assignmentStopBy': "",
											'assignmentStartRoleId':$scope.roleId,
											'assignmentStopRoleId':"",
											'isConsultationStatus': "N",
											'consultationStartTime':encounterObj.consultationStartTimeDate,
											'consultationStopTime': null,
											'consultationStartBy': encounterObj.consultationStartBy,
											'consultationStopBy': "",
											'consultationStartRoleId':encounterObj.consultationStartRoleId,
											'consultationStopRoleId': "",
											'encounterId': encounterObj.encounterId,
											'patientId': encounterObj.patientId,
										}
										
										console.log('ASSIGMENT DATA',data);
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
														if (data.status == "success")
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
																	"doctorId":encounterObj.doctorId,
																	"specialityId":encounterObj.specialityId,
																	"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
																	//"doctorName":encounterObj.doctorName,
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1,
																	"patientCategoryId" : encounterObj.patientCategoryId,
																	'encounterTypeId':encounterObj.encounterTypeId,
																	'isHideCyclicOrderTab':'Y'
																	
															}
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															$state.go("OPDCoverSheet");
													});
										
								}
								else
								{
									//alert('ASSIGMENT START MILA');
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
											//"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1,
											"patientCategoryId" : encounterObj.patientCategoryId,
											'encounterTypeId':encounterObj.encounterTypeId,
											'isHideCyclicOrderTab':'Y',
										    'ehcPatient':'Y'
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
							$scope.getDoctorBySpeciality = function(encounter,index) 
							{
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									specialityId :encounter.specialityId,
								};
							    var URI = BASE_URL + ROOT_URL+ GETDOCTORBYSPECIALITYID;

								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorList = [];
													if (response.data.status == "success")
														{
														  $scope.consultationServiceList[index].doctorList = response.data.listObject;
														}
												});
								
								$scope.checkDeptWiseAppointment(encounter,index); 
							}

							
							$scope.getConsultationSerives =function(encounterObj)
							{
								$scope.selectedEncServies.splice(0, $scope.selectedEncServies.length);
								$scope.patientId = encounterObj.patientId;
								$scope.encounterId = encounterObj.encounterId;
								$scope.consultationServiceList = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + '/api/opd/ehcConsultataion/encounterId/'+encounterObj.encounterId+'/'+encounterObj.consultationMasterId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") 
										{
											$scope.consultationServiceList = response.data.listObject;
											if($scope.consultationServiceList.length!=null && $scope.consultationServiceList.length>0)
												{
												angular.forEach($scope.consultationServiceList, function(value, key) {
													   if(value.specialityId!=null && value.specialityId>0 )
													   	{
														   value.doctorId = value.doctorId+"";
														   value.specialityId = value.specialityId+"";
														   value.deptIdTemp = value.specialityId+"";
														   $scope.getDoctorBySpeciality(value,key);
													   	}
													   
													});
												
												  $('#consultationServiceId').modal('show');
												  $rootScope.stopLoader();
												}
											else{
												growl.warning("No Consultation Service Available.",{
													title : WARNING_MSG
												});
												return;
											   }
										}
										else if (response.data.status == "error") 
										{
											$scope.consultationServiceList =[];
											$rootScope.stopLoader();
										}
									});
							}
							
							
							
							/**========================DOCUMENT UPLOAD =========================================*/
							
							$scope.getDocumentType = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + '/api/opd/ehcConsultataion/encounterId/'+encounterObj.encounterId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") 
										{
											$scope.consultationServiceList = response.data.listObject;
											if($scope.consultationServiceList.length!=null && $scope.consultationServiceList.length>0)
												{
												  $('#consultationServiceId').modal('show');
												  $rootScope.stopLoader();
												}
											else{
												growl.warning("No Consultation Service Available.",{
													title : WARNING_MSG
												});
												return;
											   }
										}
										else if (response.data.status == "error") 
										{
											$scope.consultationServiceList =[];
											$rootScope.stopLoader();
										}
									});
							}
							
							$scope.checkDeptWiseAppointment = function(encounterObj,index)
							{
								$scope.consultationServiceList[index].encounterTypeId=1;
								$rootScope.startLoader();
								var specilityObj  = encounterObj.specialityList.find(speciality => speciality.specialityId == encounterObj.specialityId);
								$scope.consultationServiceList[index].deptWiseAppointment = specilityObj.deptWiseAppointment;
								if($scope.consultationServiceList[index].deptWiseAppointment=='Y')
								{
									
//									$scope.consultationServiceList[index].doctorId = null;
									var  data = {
									    	
								    		 unitId          :$scope.unitId,
											 organizationId  :$scope.organizationId,
								    		 patientId : $scope.patientId, //$scope.appointmentMasterObj.patientId
								    		 specialityId : encounterObj.specialityId,
								    		 currentDate :  moment(new Date()).format('YYYY-MM-DD')
								    		 
								     };
								     var URI = BASE_URL + ROOT_URL + GETSYSTEMGENERATEDVISITTYPEID;
								     BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								     {
								       if(response.data.status=="success")
								    	   {
								    	   $scope.consultationServiceList[index].systemGeneratedVisitTypeId=response.data.toString();
								    	   $scope.consultationServiceList[index].userDefinedVisitTypeId=response.data.toString();
									        $rootScope.stopLoader();
								    	   }
								       else if(response.data.status=="error")
								    	   {
								    	    $rootScope.stopLoader();
								    	   }
								     });
								}
							}
							
							
							
							/*$scope.getAppointmentPopup = function(encounterObj,index)
							{
								console.log("encounterObj",encounterObj); 
//								return false;
								 var idx = 0;
								 if($scope.selectedEncServies.length==0)
								 {
								 growl.warning("Please Select Service",{
										title : ERROR_MSG
									});
									return;
								 }
								 
								  if($scope.selectedEncServies.length>0)
									 {
//									   var selectedService  = $scope.selectedEncServies.find(service => service.serviceId == encounterObj.serviceId);
									  var selectedService  =  $scope.selectedEncServies.findIndex((service,key) => service.serviceId == encounterObj.serviceId && index==key);
									   if(selectedService==undefined)
										   {
											   growl.warning("Please Select Service",{
													title : ERROR_MSG
												});
												return;
										   }
									   else{
//										     idx  = $scope.selectedEncServies.findIndex(service => service.serviceId == encounterObj.serviceId);
										     idx  =  $scope.selectedEncServies.findIndex((service,key) => service.serviceId == encounterObj.serviceId && index==key);
										     if($scope.consultationServiceList[index].specialityId==null||$scope.consultationServiceList[index].specialityId=='')
												{
													growl.warning("Please Select Department",{
														title : WARNING_MSG
													});
													return;
												}
												$scope.consultationServiceList[index].deptId = encounterObj.specialityId;
												$scope.consultationServiceList[index].doctorId = encounterObj.doctorId;
												$scope.selectedEncServies[idx].deptId = encounterObj.specialityId;
												$scope.selectedEncServies[idx].doctorId = encounterObj.doctorId;
												$scope.selectedEncServies[idx].encounterTypeId =1;
												
												$scope.slotDetailsList = [];
												if($scope.isAppointmentRequired == 'Y'){
													
													$scope.slotCheckTime = moment(new Date()).format("HH:mm");
													
													var data = {
															appointmentDate :moment(new Date()).format('YYYY-MM-DD'),
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
														};
													
											if($scope.consultationServiceList[index].encounterTypeId == 1)
													if($scope.consultationServiceList[index].deptWiseAppointment == "N"){
															if($scope.consultationServiceList[index].deptId > 0 && $scope.consultationServiceList[index].doctorId > 0)
															{
																
													
																        angular.element('#time_slot_modal').modal('show');
																		data.specialityId = $scope.consultationServiceList[index].deptId;
																		data.doctorId = $scope.consultationServiceList[index].doctorId;
																		data.slotTypeId = 1;
													
																console.log(data);
																//return false;
																}
																else{
														
																	growl.warning("Please Select Department and Doctor",{
																		title : WARNING_MSG
																	});
																	return;
																}
													}
													else
														{
														  
															if($scope.consultationServiceList[index].deptId > 0)
															{
																angular.element('#time_slot_modal').modal('show');
																
																		data.specialityId = $scope.consultationServiceList[index].deptId;
																		data.slotTypeId = 4;
																console.log(data);
																}
																else{
														
																	growl.warning("Please Select Department",{
																		title : WARNING_MSG
																	});
																	return;
																}
														}
													}
												$rootScope.startLoaderPopup();
												$scope.slotMsg='';
												var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
												BillingGenericService
														.serviceAction(METHOD_POST, URI, data,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	if (response.data.status == "success") {
																		$scope.slotDetailsList = response.data.listObject;																	
																			
																		$scope.slotMsg= ($scope.slotDetailsList!=null && $scope.slotDetailsList.length>0) ? '' : 'Slots not available !';
																		$rootScope.stopLoaderPopup(); 
																	}else{
																		$rootScope.stopLoaderPopup(); 
																	}

																});
												
												//slot index
												$scope.idx  =idx;
												$scope.index  =index;
									       }
									 }
								
								
							}*/
							$scope.getAppointmentPopup = function(encounterObj,index)
							{
								console.log("encounterObj",encounterObj); 
								 if(encounterObj.selectServiceFlag==false || encounterObj.selectServiceFlag==undefined)
								 {
									 growl.warning("Please Select Service",{
											title : ERROR_MSG
										});
										return;
								 }
								 
								  if(encounterObj.selectServiceFlag==true)
									 {
									  if($scope.consultationServiceList[index].specialityId==null||$scope.consultationServiceList[index].specialityId=='')
												{
													growl.warning("Please Select Department",{
														title : WARNING_MSG
													});
													return;
												}
												$scope.consultationServiceList[index].deptId = encounterObj.specialityId;
												$scope.consultationServiceList[index].doctorId = encounterObj.doctorId;																								
												
												$scope.slotDetailsList = [];
												if($scope.isAppointmentRequired == 'Y'){
													
													$scope.slotCheckTime = moment(new Date()).format("HH:mm");
													
													var data = {
															appointmentDate :moment(new Date()).format('YYYY-MM-DD'),
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
														};
													
											if($scope.consultationServiceList[index].encounterTypeId == 1)
													if($scope.consultationServiceList[index].deptWiseAppointment == "N"){
															if($scope.consultationServiceList[index].deptId > 0 && $scope.consultationServiceList[index].doctorId > 0)
															{
																
													
																        angular.element('#time_slot_modal').modal('show');
																		data.specialityId = $scope.consultationServiceList[index].deptId;
																		data.doctorId = $scope.consultationServiceList[index].doctorId;
																		data.slotTypeId = 1;
													
																console.log(data);
																//return false;
																}
																else{
														
																	growl.warning("Please Select Department and Doctor",{
																		title : WARNING_MSG
																	});
																	return;
																}
													}
													else
														{
														  
															if($scope.consultationServiceList[index].deptId > 0)
															{
																angular.element('#time_slot_modal').modal('show');
																
																		data.specialityId = $scope.consultationServiceList[index].deptId;
																		data.slotTypeId = 4;
																console.log(data);
																}
																else{
														
																	growl.warning("Please Select Department",{
																		title : WARNING_MSG
																	});
																	return;
																}
														}
													}
												$rootScope.startLoaderPopup();
												$scope.slotMsg='';
												var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
												BillingGenericService
														.serviceAction(METHOD_POST, URI, data,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	if (response.data.status == "success") {
																		$scope.slotDetailsList = response.data.listObject;																	
																			
																		$scope.slotMsg= ($scope.slotDetailsList!=null && $scope.slotDetailsList.length>0) ? '' : 'Slots not available !';
																		$rootScope.stopLoaderPopup(); 
																	}else{
																		$rootScope.stopLoaderPopup(); 
																	}

																});
												
												$scope.index  = index;
									 }
								
								
							}
							$scope.getSlotDetailsPopup = function(item) 
							{
								var slotChk= $scope.consultationServiceList.find(service => service.slotId == item.slotId || service.encounterTime==item.fromTimeString);
								if(slotChk!=undefined){

									growl.warning("Slot "+ item.fromTimeString+" already selected !",{
										title : WARNING_MSG
									});
									$scope.consultationServiceList[$scope.index].encounterTime = '';
									return;
								}
//								$scope.consultationServiceList[$scope.index].doctorId = item.doctorId+"";	
								$scope.consultationServiceList[$scope.index].doctorId = item.doctorId == null ? $scope.consultationServiceList[$scope.index].doctorId +"":item.doctorId+"";	
								$scope.consultationServiceList[$scope.index].specialityname = item.specialityName;
								$scope.consultationServiceList[$scope.index].fromTime = item.fromTimeString2;
								$scope.consultationServiceList[$scope.index].toTime = item.toTimeString2;
								
								$scope.consultationServiceList[$scope.index].appointmentDate = item.appointmentDate;
								$scope.consultationServiceList[$scope.index].slotId = item.slotId;
								
								$scope.consultationServiceList[$scope.index].appointmentDate = item.appointmentDate;
								$scope.consultationServiceList[$scope.index].slotStatusId = 4;
								$scope.consultationServiceList[$scope.index].priorityId  = 1;	
								$scope.consultationServiceList[$scope.index].bookedpatientLength = item.slotPatientDetailsListsSize;
								
								$scope.consultationServiceList[$scope.index].encounterTime = item.fromTimeString;
							
							}
							
							
							
							$scope.initAppointmentMasterObj	 =function(){
							$scope.appointmentMasterObj = {
									 "encounterId"			:"",
									 "patientId"			:"",
									 "visitTypeId"			:"",
									 "doctorId"				:"",
									 "sourceId"				:"",
									 "encounterDate"		:new Date().getTime(),
									 "encounterTime"		:"",
									 "isReferal"			:"A",
									 "deptId"				:"",
									 "subDeptId"			:"",
									 "remark"				:"",
									 "kinDetailsId"			:"",
									 "paymentEntitlementTypeId":"1",
									 "encounterNumber"		:"",
									 "createdDate"			:new Date().getTime(),
									 "createdBy"			:$scope.userId,
									 "updatedDate"			:new Date().getTime(),
									 "updatedBy"			:$scope.userId,
									 "status"				:"A",
									 "unitId"				:$scope.unitId,
									 "organizationId"		:$scope.organizationId,
									 "listKinDetailsDto"	:[],
									 "isScheduledAppointment":"I",
									 "modalityId":"",
									 "specialityId":"",
									 //"subSpecialityId":"",
									 "systemGeneratedVisitTypeId":"",
									 "userDefinedVisitTypeId":"",
									 "reasonIdForChangingVisitType":"",
									 "remarkForChangingVisitType":"",
									 "encounterTypeId":"",
									 "clinicId":"",
									 "referralTypeId":"",
									 "referralId":"",
									 "referralRemark":"",
									 "defaultSelfTariffId" : $scope.defaultSelfTariffId,
									 "isAppointmentRequired" : $scope.isAppointmentRequired,
									 "currentDate" :  moment(new Date()).format('YYYY-MM-DD'),
									 "counterId":$rootScope.counterId
								}
							}
						  $scope.initAppointmentMasterObj();
							
							
							
							$scope.saveAppointmentMaster = function(){
								//console.log($scope.consultationServiceList); return false;
								$scope.selectedEncServies=[];
								angular.forEach($scope.consultationServiceList, function(value, key) {
									if(value.selectServiceFlag==true){
										$scope.selectedEncServies.push(value);
									}
								});
								
								$scope.saveFlag = true;
								 if($scope.selectedEncServies.length==0)
									 {
									 growl.warning("Please Select Service",{
											title : ERROR_MSG
										});
									   $scope.saveFlag = false;
										return;
									 }
								
								 var appointmentList = [];
								 angular.forEach($scope.selectedEncServies, function(value, key) {
									 $scope.initAppointmentMasterObj();
									 
									 if(value.appointmentStatusId==1)
										 {
										if((value.specialityId == '' ||value.specialityId == 0||value.specialityId ==null)){
											growl.warning("Please Select Department",{
												title : ERROR_MSG
											});
											$scope.saveFlag = false;
											return;
										}
											
										if((value.doctorId == ''  || value.doctorId == undefined|| value.doctorId == null) && value.isAppointmentRequired == 'N')
										{
											growl.warning("Please Select Doctor",{
												title : ERROR_MSG
											});
											$scope.saveFlag = false;
											return;
										}
										
										
										
										 
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');
										$scope.currentDateAppDate = moment(
												new Date()).format(
												'YYYY-MM-DD HH:mm:ss');
										if(value.appointmentId > 0)
											{
											value.appointmentObject  = null;
											value.unitTokenNumber =  value.tokenNumber;
											}
										else{
											value.unitTokenNumber =  null;
											value.currentDate =  moment(new Date()).format('YYYY-MM-DD');
											if($scope.isAppointmentRequired == 'Y')
												{
													if(value.slotId == '' || value.slotId == null)
														{
																growl.warning("Please Select time Slot",{
																	title : WARNING_MSG
																});
																$scope.saveFlag = false;
																return;
														}
													else
														{
														
															var dataAppointmentObj = {
																	patientId : $scope.patientId,
																	remark : '',
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	createdDate : $scope.currentDate,
																	updatedDate : $scope.currentDate,
																	createdBy : $scope.userId,
																	updatedBy : $scope.userId,
																	status : 'A',
																	appointmentTypeId : value.encounterTypeId,
																	priorityId : value.priorityId,
																	firstName : $scope.patientDetails.firstName,
																    mobileNumber :$scope.patientDetails.mobileNumber,
																    generateToken : 'Y',
																    appointmentDate : $scope.currentDateAppDate,
																    isDirectEncounter : 'Y',
																	ehcPackageId : '',
																	packageBasePrice : ''
																}
															
															if(value.encounterTypeId != 5)
																{
																   dataAppointmentObj.slotId = value.slotId;
																   dataAppointmentObj.appointmentDate2 = value.appointmentDate;
																   dataAppointmentObj.fromTimeString = value.fromTime;
																   dataAppointmentObj.slotStatusId = value.slotStatusId;
																   dataAppointmentObj.isFromEncounter = 'Y';
																}
															else
																{
																  dataAppointmentObj.slotId = null;
																  dataAppointmentObj.appointmentDate2 = null;
																  dataAppointmentObj.fromTimeString =null;
																  dataAppointmentObj.slotStatusId =null;
																  dataAppointmentObj.doctorName = null;
																  dataAppointmentObj.isFromEncounter = 'Y';
																}
															value.appointmentObject  = dataAppointmentObj;
															$scope.appointmentMasterObj.appointmentObject =dataAppointmentObj;
														}
												}
											
										}
										 value.specialityId = value.deptId;
										 if($scope.selfFlag == "Y")
											{
											 value.slotId =$scope.slotId;
											}
										 value.deptWiseAppointment = value.deptWiseAppointment;
										 
										 
										 $scope.appointmentMasterObj.slotId=value.slotId;
										 $scope.appointmentMasterObj.serviceId=value.serviceId;
										 $scope.appointmentMasterObj.againstEncId= $scope.encounterId;
										 $scope.appointmentMasterObj.patientId = $scope.patientId;
										 $scope.appointmentMasterObj.encounterTypeId =value.encounterTypeId;
										 $scope.appointmentMasterObj.deptId =value.deptId;
										 $scope.appointmentMasterObj.doctorId =value.doctorId;
										 $scope.appointmentMasterObj.specialityId =value.specialityId;
										 $scope.appointmentMasterObj.patientClassId = null;
										 $scope.appointmentMasterObj.deptWiseAppointment = value.deptWiseAppointment;
										 $scope.appointmentMasterObj.isNursingAssessmentRequiredForSpeciality = $scope.isNursingAssessmentRequired;
										 $scope.appointmentMasterObj.defaultSelfTariffId= $scope.defaultSelfTariffId;
										 $scope.appointmentMasterObj.isAppointmentRequired= $scope.isAppointmentRequired;
										 $scope.appointmentMasterObj.currentDate= moment(new Date()).format('YYYY-MM-DD');
										 $scope.appointmentMasterObj.counterId=$rootScope.counterId;
										 $scope.appointmentMasterObj.createdDate=new Date().getTime();
										 $scope.appointmentMasterObj.createdBy=$scope.userId;
										 $scope.appointmentMasterObj.updatedDate=new Date().getTime();
										 $scope.appointmentMasterObj.updatedBy=$scope.userId;
										 $scope.appointmentMasterObj.status="A";
										 $scope.appointmentMasterObj.unitId=$scope.unitId;
										 $scope.appointmentMasterObj.organizationId=$scope.organizationId;
										 $scope.appointmentMasterObj.encounterDate =new Date().getTime();
										 $scope.appointmentMasterObj.consultationMpprId = value.consultationMpprId;
										 $scope.appointmentMasterObj.orderDetailId=value.orderDetailsId;
										 appointmentList.push($scope.appointmentMasterObj);
										}
									});
							 console.log("appointmentList",appointmentList); 
//							 return false;
								 if($scope.saveFlag)
									 {
									 $('#consultationServiceId').modal('hide');
									 var URI = BASE_URL + ROOT_URL + "/api/opd/encounterEhc";
									 console.log('appointmentList',JSON.stringify(appointmentList));
									 $rootScope.startLoader();
									 BillingGenericService.serviceAction(METHOD_POST, URI, appointmentList, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
												if (response.data.status == "success") 
												{
													
												}else{
													
												    }	
											
											});
									 }
								
							  }
							
							
							$scope.printCaseSheet =function(encounterObj)
							{
							    var data =  {
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "OPDCaseSheet.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "OPDCoversheet",
						                 "reportParams": [
									                {
									                  "paramName": "@EncounterId",
									                  "paramType": "Integer",
									                  "paramValue":  encounterObj.encounterId
									                }
									               
				                 ]
				                     };
							    reportService.generate(REPORT_API_URI1,data);
							}
							
						  
							
						} ]);