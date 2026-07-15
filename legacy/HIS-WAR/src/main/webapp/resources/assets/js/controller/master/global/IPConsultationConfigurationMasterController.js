/**
 * @author Kaustubh
 * @date 03-04-2018 Controller for doctorConsulationMaster unit level
 */

angular
		.module("myApp")
		.controller(
				"IPConsultationConfigurationMasterController",
				[
						'$scope',
						'$rootScope',
						'BillingGenericService',
						'GenericService',
						'$sessionStorage',
						'PagerService',
						'promiseFactory',
						'$cookies',
						'$state',
						'promiseFactoryWithObject',
						'growl',
						function($scope, $rootScope, BillingGenericService,
								GenericService, $sessionStorage, PagerService,
								promiseFactory, $cookies, $state,
								promiseFactoryWithObject, growl) {

							$scope.init = function() {

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$rootScope.loginpage = true;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.specialityMasterList = [];
								$scope.servicesList = [];

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.unitServiceMapperList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								$scope.pager = {};
								$scope.page;
								$scope.userId = cookieObject.userId;
								$scope.currentDate = moment(new Date()).format(
										'DD-MM-YYYY HH:mm:ss');

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.disableField = false;

								$scope.doctorConsultationObj = {
									specialityId : null,
									doctorId :null,
									unitId : cookieObject.unitId,
									updatedId:$scope.userId,
									organizationId : cookieObject.organizationId,
									doctorIPConsultationServiceMapperId : null,
									firstConsultationServiceId : null,
									routineConsultationServiceId : null,
									noOfRoutineServices : null,
									followupRoutineServiceId : null,
									icuPrimaryConsultationServiceId : null
								}

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								}
								//-----Get doctorIPConsultationList-----//
							    
								$scope.init();
							    
								$scope.initDoctorIPConsultationList = function(offset,noOfRecordsPerPage){
									try{
										
										offset = offset  != null ? offset  : 0;
										noOfRecordsPerPage =noOfRecordsPerPage != null ? noOfRecordsPerPage
												: 10;
										
										var URI = BASE_URL + ROOT_URL + API + GLOBAL + GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST;
									    
									    var URI1 = BASE_URL + ROOT_URL + API + GLOBAL + "/getDoctorIPConsultationServiceMapperListCount";
									    
										var obj = {
													organizationId :$scope.organizationId,
													unitId         :$scope.unitId,
													offset         :offset,
													noOfRecordsPerPage :noOfRecordsPerPage
											    };
										
										var data1 = {
												organizationId :$scope.organizationId,
												unitId         :$scope.unitId,
										    };
										
										
										promiseFactoryWithObject.setPromisesWithObject(URI, URI1, "POST", "POST",obj,data1)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.doctorIPConsultationList = [];
													$scope.doctorIPConsultationList = response[0].listObject;
													$scope.commonListCount = response[1].object;
													$scope.setPage(1, false);
												});
									}catch (e) {
										console.log("Exception",e.message);
									}
								}
								$scope.initDoctorIPConsultationList($scope.offset,$scope.noOfRecordsPerPage);
								//------------ Get Speciality Master List ------------//
								$scope.getSpecialityMasterList = function(){
									try{
										var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
										var obj = {
													organizationId : $scope.organizationId,
													unitId         :$scope.unitId
											    }
										BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											console.log(response);
											if(response.data.status == "success")
												$scope.specialityMasterList = response.data.listObject;
										});
									}catch (e) {
										console.log("Exception",e.message);
									}
								}
								$scope.getSpecialityMasterList();
								
								//------------ Get Doctors By SpecialityId ----------//
								$scope.getDoctorsBySpecialityId = function(specialityId){
									$rootScope.startLoader();
									//$scope.unitService.subSpecialityId="";
									$scope.doctorList=[];
									  var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
									  var obj = {
												organizationId : $scope.organizationId,
												unitId         : $scope.unitId,
												specialityId   : specialityId
										    }
									BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										console.log("Doctor list",response);
										if(response.data.status == "success")
											$scope.doctorList = response.data.listObject;
									});
								}
								
								
								
								
								//------------- Get All Services by Speciality and sub-speciality id=1000(Consultation) ---------------//
								$scope.getServicesBySpecialityId = function(){
									try{
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL+ "/billing/service/consultations/org/"+$scope.organizationId+"/unit/"+$scope.unitId+"/splId/0/subSplId/0";
										BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status == "success")
												$scope.servicesList = response.data.listObject;
												console.log('service list',$scope.servicesList)
										});
									}catch (e) {
										console.log("Exception",e.message);
									}
								}
								$scope.getServicesBySpecialityId();
								
								// function for popup ok button call
							    $scope.PopupOkBtn = function() {
							     $scope.popUpFlag = false;
							     $scope.saveDoctorCosultationMaper();
							    }
								
							    $scope.saveDoctorCosultationMaper= function (){
									try{
										if ($scope.popUpFlag) {
									         $scope.showModal = true;

									        } else {
											/*$rootScope.startLoader();*/
											var URI = BASE_URL + ROOT_URL + API + GLOBAL + SAVE_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER;
											$scope.doctorConsultationObj.createdDate=$scope.currentDate;
											$scope.doctorConsultationObj.updatedDate=$scope.currentDate;
											$scope.doctorConsultationObj.createdId=$scope.userId;	
											$scope.doctorConsultationObj.updatedId=$scope.userId;	
											$scope.doctorConsultationObj.status='A';
											console.log($scope.doctorConsultationObj);
											//return false;
											BillingGenericService.serviceAction(METHOD_POST,URI, $scope.doctorConsultationObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
												$scope.init();
												$scope.getSpecialityMasterList();
												$scope.getServicesBySpecialityId();
											});
									       }
								}catch (e) {
									console.log("Exception",e.message);
								}

								}
								
								
								// getDetailsById
								$scope.getDetailsById = function(id) {

									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									$scope.disableField=true;
									

									var data = {

											doctorIPConsultationServiceMapperId : id,
											
									}
									console.log(data);
									var URI = BASE_URL + ROOT_URL + API + GLOBAL+ GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_ID+S+id+S+$scope.organizationId+S+$scope.unitId;
									
									BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										
										if (response.data.status == "success") {
											
											$scope.doctorConsultationObj.doctorIPConsultationServiceMapperId = response.data.listObject[0].doctorIPConsultationServiceMapperId;
											$scope.doctorConsultationObj.specialityId = response.data.listObject[0].specialityId.toString();
											$scope.getDoctorsBySpecialityId(response.data.listObject[0].specialityId);
											$scope.doctorConsultationObj.doctorId = response.data.listObject[0].doctorId.toString();
											$scope.doctorConsultationObj.firstConsultationServiceId = response.data.listObject[0].firstConsultationServiceId.toString();
											$scope.doctorConsultationObj.routineConsultationServiceId = response.data.listObject[0].routineConsultationServiceId.toString();
											$scope.doctorConsultationObj.followupRoutineServiceId = response.data.listObject[0].followupRoutineServiceId.toString();
											$scope.doctorConsultationObj.icuPrimaryConsultationServiceId = response.data.listObject[0].icuPrimaryConsultationServiceId.toString();

										}
										
									});
								}
								
								// updateDoctorCosultation
								$scope.updateDoctorCosultationMaper = function() {

								
									$scope.doctorConsultationObj.updatedDate=$scope.currentDate;
									$scope.doctorConsultationObj.updatedId=$scope.userId;
									var URI = BASE_URL + ROOT_URL + API + GLOBAL+ UPDATE_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER;
									
									BillingGenericService.serviceAction(METHOD_POST,URI, $scope.doctorConsultationObj, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
										if (response.data.status == "success") {
											$scope.init();
											$scope.getSpecialityMasterList();
											$scope.getServicesBySpecialityId();
									
										}
										
									});
								}
								
								$scope.getDoctorIPConsultationList = function(offset,noOfRecordsPerPage){
									$scope.doctorIPConsultationList = [];
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
											: 10;
									var URI = BASE_URL + ROOT_URL + API + GLOBAL + GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST;
									   var obj = {
												organizationId :$scope.organizationId,
												unitId         :$scope.unitId,
												offset         :offset,
												noOfRecordsPerPage :noOfRecordsPerPage
										    };
									GenericService.serviceAction("POST", URI, obj)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.doctorIPConsultationList = response.data.listObject;
													});
								}
								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages) {
										return;
									}
									$scope.pager = PagerService.GetPager(
											$scope.commonListCount, page,
											$scope.noOfRecordsPerPage);
									if (flag) {
										$scope.getDoctorIPConsultationList($scope.pager.startIndex,$scope.pager.pageSize);
									}
								}
								$scope.setNoOfRecords = function() {
									$scope.initDoctorIPConsultationList($scope.offset,$scope.noOfRecordsPerPage);
								};
								
							}]);