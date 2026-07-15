'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:counterWorklistController
 * @description #counterWorklistController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'counterWorklistController',
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
								$scope.roleId = 3;

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

								//For Get Encounter List
								/*var URI = BASE_URL + ROOT_URL
										+ GETLISTOFENCOUNTERMASTER;
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.allEncounterList = response.data.listObject;
													console.log('allEncounterList',$scope.allEncounterList);
												});*/
								
								
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
							
							//Function For Get Doctor List By Speciality
							$scope.getDoctorBySpeciality = function(id) {
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								//Condition For All Doctor Or By Speciality ID
								if(id > 0)
									{
										data.specialityId = id;
										var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;

									}
								else
								{
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEDOCTORLIST;
								}
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.doctorList = response.data.listObject;
												});
									}
							$scope.getClinicByDoctorId = function(doctorId)
							{
								var data = {};
								var paramObj = {
										'doctor_id' : doctorId
								};
								var headers={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETCLINICMASTERLISTBYDOCTORID;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI,
										data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.clinicList = [];
											if (response.data.status == "success")
												
												$scope.clinicList =response.data.listObject;
												console.log("Clnic List",$scope.clinicList);
										});
							}
							
							
							$scope.getNursingStationByClinicId = function(clinicId)
							{
								var data = {};
								
								var paramObj = {
										'clinic_id' : clinicId
								};
								
								var headers={
										'organization_id': $scope.organizationId,
										'unit_id':$scope.unitId,
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETLISTOFNURSINGSTATIONBYCLINICID;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI,
										data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											//$scope.clinicList = [];
											if (response.data.status == "success")
												
												console.log("Clnic Nursing List",response.data.listObject[0].nursingStationId);
											$scope.encounterSearchObject.nursingStationId = response.data.listObject[0].nursingStationId.toString();
										});
							}
							
							
							$scope.nursingStationChange = function()
							{
								$scope.encounterSearchObject.doctorId = '0';
								$scope.encounterSearchObject.clinicId = '0';
							}
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function()
							{
								$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.organizationId = $scope.organizationId;
                                $scope.encounterSearchObject.unitId = $scope.unitId;
								
								console.log($scope.encounterSearchObject);
								
								var paramObj = {};
								var headers={
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETSEARCHEDLISTENCOUNTERMASTER;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
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
								
								$scope.clinicList = [];
								
								$scope.patientName ='';
								$scope.uhidNumber = '';
								$scope.mobileNumber = '';
								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							}
							
							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}
							
							$scope.selectPatientId = function(info, model,
									label) {
								
								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;
								
								$scope.encounterSearchObject.patientId = info.patientId;
							}
						
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							$scope.getAppointmentDetails = function(item)
							{
								$scope.appointmentDate = item.appointmentDate;
								$scope.appointmentFromTimeString = item.fromTimeString;
								$scope.appointmentRemark = item.remark;
							}
							
							$scope.getRefralDetails = function(item)
							{
								$scope.referralTypeName = item.referralTypeName;
								$scope.referralName = item.referralName;
								$scope.referralRemark = item.referralRemark;
							}
							
							$scope.gotoCoverSheet = function(encounterObj)
							{
								
							if(encounterObj.statusId != '1' && encounterObj.statusId != '8' && $scope.roleId == '3' ){
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
																	"visitTypeId" :1
																	
															}
															 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
															console.log("EO",cookieObject);
															//return false;
															$state.go("OPDCoverSheet");
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
											"visitTypeId" :1
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
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
											'patientId': encounterObj.patientId
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
																	"doctorName":encounterObj.doctorName,
																	"prefixId":encounterObj.prefixId,
																	"genderId":encounterObj.genderId,
																	"appointmentId":encounterObj.appointmentId,
																	"visitTypeId" :1,
																	"contractId" : encounterObj.contractId,
																	"payeeId" : encounterObj.payeeId
																	
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
											"doctorName":encounterObj.doctorName,
											"prefixId":encounterObj.prefixId,
											"genderId":encounterObj.genderId,
											"appointmentId":encounterObj.appointmentId,
											"visitTypeId" :1,
											"contractId" : encounterObj.contractId,
											"payeeId" : encounterObj.payeeId
											
									}
									 sessionStorage.setItem('cookieEncounterObject', JSON.stringify(cookieObject));	
									console.log("EO",cookieObject);
									//return false;
									$state.go("OPDCoverSheet");
								}
								
							}
								
								
							}
							
							
							
							/**========================DOCUMENT UPLOAD =========================================*/
							
							$scope.getDocumentType = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.documentTypeList = [];
										if (response.data.status == "success") {
											$scope.documentTypeList = response.data.listObject;
										}
									});
							}
							$scope.getDocumentType();
							
							$scope.encounterDetailsList = [];
							$scope.encounterDetails = {
								  encounterDocId : "",
								  encounterId : "",
								  patientId : "",
								  documentPath : "",
								  documentTypeId : "",
								  status : 'A',
								  createdDate : "",
								  createdBy : $scope.userId,
								  updatedDate : "",
								  updatedBy : $scope.userId,
								  unitId : $scope.unitId,
								  organisationId : $scope.organizationId,
								  file : {}
								}
							
							$scope.showDocUPloadPoup   = function(encounterObj)
							{
								$scope.encounterDetails.encounterId = encounterObj.encounterId;
								$scope.encounterDetails.patientId = encounterObj.patientId;	
								if($scope.encounterDetailsList.length>0)
									{
									  $('#uploadDetails').modal('show');
									}
								else{
									 $('#uploadDetails').modal('show');
									var encounterDetails = angular.copy($scope.encounterDetails);
									encounterDetails.patientId = encounterObj.patientId;
									encounterDetails.encounterId = encounterObj.encounterId;
									$scope.encounterDetailsList.push(encounterDetails);
								}
							}
							
							$scope.addImageDetails = function()
							{ 
								var encounterDetails = angular.copy($scope.encounterDetails);
								$scope.encounterDetailsList.push(encounterDetails);
							};

						  $scope.removeImageDetails = function(deleteIndex)
							{ 
							   $scope.encounterDetailsList.splice(deleteIndex, 1);
						    }		
							
							
						  
							
						  
							
							
							$scope.uploadEncounterDoc = function(Obj)
							{
								 var fd = new FormData();
							        fd.append('encounterId', Obj.encounterId);
							        fd.append('patientId',Obj.patientId );
							        fd.append('documentTypeId', Obj.documentTypeId);
							        fd.append('createdBy',Obj.createdBy );
							        fd.append('updatedBy', Obj.updatedBy);
							        fd.append('unitId', Obj.unitId);
							        fd.append('organisationId', Obj.organisationId);
							        fd.append('file',Obj.file );
							        fd.append('expenseId', 0);
							        fd.append('grnId',0);
									fd.append('poId',0);
							        fd.append('incidentReportingMapperId',0);
							        fd.append('companyExpenseId',0);
							        fd.append('createdDate',null);
									fd.append('fileEditedName',null);
									fd.append('opbId',0);
									fd.append('vendorId',0);
							        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
							        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										return response;
									});
							}
							 $scope.uploadPatientImage = function()
							    {
								 $rootScope.startLoader();
								  $scope.count = 0;
								 angular.forEach($scope.encounterDetailsList, function(value, key) 
									{
										var promise = $scope.uploadEncounterDoc(value);
										promise.then(function(response) 
												{
												  if(response.data.status == "success")
													  {
													    $scope.count++;
													    if($scope.encounterDetailsList.length==$scope.count)
														 {
														   growl.success("Document Uploaded Successfully.",
																	{
																		title : 'Success!'
																	});
														   $rootScope.stopLoader();
														 }
												      }
												  else 
													{
														growl.error('Something went wrong.',
																{
																	title : 'Error!'
																});
													}
											    });
											
									});
								
							    }
							 
							 $scope.getPatientDocumentList = function(encounterObj)
							 {
									$rootScope.startLoader();
									var data =
											{
											"encounterId":encounterObj.encounterId,
											"patientId":encounterObj.patientId,
											"unitId":$scope.unitId,
											"organisationId":$scope.organizationId,
											"userId":$scope.userId
									}
									var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
									BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.patientDocumentList = [];
											if (response.data.status == "success") 
											{
												$('#encounterUploadDocumentDetailsModal').modal('show');
												$scope.patientDocumentList = response.data.listObject;
												$rootScope.stopLoader();
											}
											else if(response.data.status == "error")
												{
												$rootScope.stopLoader();
												}
										});
								}
							 $scope.openImage = function(index)
							 { 
								 $rootScope.startLoader();
								 var imageUrl = $scope.patientDocumentList[index].documentPath;
								 var href = $rootScope.currentHrefDoccument();
								 $.fancybox.open({
				            	    width: 600, // or whatever
			                        height: 320,
				            	    href:href+imageUrl,
				            	    type: 'iframe'
				            	});
								 $rootScope.stopLoader();
							 } 
						} ]);