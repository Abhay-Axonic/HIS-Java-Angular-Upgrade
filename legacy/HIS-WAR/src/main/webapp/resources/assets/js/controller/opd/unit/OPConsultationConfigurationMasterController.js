/**
 * 
 */


/**
 * @Author By  Kaustubh Sakhare
 * @name OPConsulationConfigurationMasterController
 * @description 
 */
angular
		.module('myApp')
		.controller(
				'OPConsulationConfigurationMasterController',
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
						'PagerService',
						'BillingGenericService',
						'PaginationService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,BillingGenericService,PaginationService) {

							// init function description
								$scope.init = function() 
								{

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.currentDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');

								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.commonListCount;
								
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for common popup

								$rootScope.loginpage = true;

								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.disableField=false;
								$scope.doctorList = [];
								$scope.disableDoctor= false;
								
								 $scope.pagination = {
						                    currentPage: 1,
						                    totalPages: 0,
						                    pageSize: '10',
						                    search: '',
						                    searchData: PaginationService.searchData,
						                    setTotalPages: PaginationService.setTotalPages,
						                    getPagesList: PaginationService.getPagesList,
						                    setPage: PaginationService.setPage,
						                    setFirstPage: PaginationService.setFirstPage,
						                    setPreviousPage: PaginationService.setPreviousPage,
						                    setNextPage: PaginationService.setNextPage,
						                    setLastPage: PaginationService.setLastPage,
						                    sortBy: PaginationService.sortBy
						                  }
						
								 
								 $scope.data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										specialityId : "",
										followupVisitDays : "",
										followupVisitCount : "",
										secondaryVisitDays : "",
										secondaryVisitCount : ""
								};
								var commonobj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
								}
								
								
								var URI = BASE_URL + ROOT_URL
										+ GETOPCONSULTATIONCONFIGURATIONLIST;
								
								BillingGenericService.serviceAction(METHOD_POST,URI, commonobj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									console.log(response);
									if(response.data.status == "success")
										$scope.pagination.data = [];
									    $scope.opConsultationConfigurationList = [];
										$scope.opConsultationConfigurationList = response.data.listObject;
										
										if($scope.opConsultationConfigurationList.length > 0){            
					                         PaginationService.searchData($scope.opConsultationConfigurationList, $scope.pagination);
					                       }
								});

							};
							$scope.init();

							
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
							
							// function for popup ok button call
						    $scope.PopupOkBtn = function() {
						     $scope.popUpFlag = false;
						     $scope.saveOPCosultationConfigurationMaster();
						    }
							
							
							$scope.saveOPCosultationConfigurationMaster= function ()
							{
								try{
									if ($scope.popUpFlag) {
								         $scope.showModal = !$scope.showModal;

								        } else {
										/*$rootScope.startLoader();*/
										var URI = BASE_URL + ROOT_URL + SAVEOPCONSULTATIONCONFIGURATION;
										$scope.data.createdDate=$scope.currentDate;
										$scope.data.updatedDate=$scope.currentDate;
										$scope.data.createdBy=$scope.userId;	
										$scope.data.updatedBy=$scope.userId;	
										$scope.data.status='A';
										
										console.log($scope.data);
										//return false;
										BillingGenericService.serviceAction(METHOD_POST,URI, $scope.data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
											$scope.init();
										});
								       }
							}catch (e) {
								console.log("Exception",e.message);
							}

							}
							
							$scope.getDoctorList= function (specialityId,deptWiseAppointment)
							{
										
								var selectedSplObject = $.grep($scope.specialityMasterList, function (splList) {
						            return splList.specialityId == specialityId;
						        });
								
								if(selectedSplObject[0].deptWiseAppointment=='Y')
											{
												$scope.disableDoctor= true;
												$scope.data.doctorId = null;
												$scope.doctorList = [];
											}else{
												try{
													
													$scope.disableDoctor= false;
													
													var data = {
															specialityId : specialityId,
															unitId : $scope.unitId,
															organizationId : $scope.organizationId
														};
														var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
														
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
											}catch (e) {
												console.log("Exception",e.message);
											}
										}
										

							}
							
							// getOPCosultationConfigurationListById
							$scope.getOPCosultationConfigurationListById = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.disableField=true;

								$scope.data.opVisitRuleId = id;
								
								//console.log(data);
								var URI = BASE_URL + ROOT_URL
										+ GETOPCONSULTATIONCONFIGURATIONLISTBYID;
								
								BillingGenericService.serviceAction(METHOD_POST,URI, $scope.data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									
									if (response.data.status == "success") {
										
										$scope.data.specialityId = response.data.listObject[0].specialityId.toString();
										$scope.data.followupVisitDays = response.data.listObject[0].followupVisitDays;
										$scope.data.followupVisitCount = response.data.listObject[0].followupVisitCount;
										$scope.data.secondaryVisitDays = response.data.listObject[0].secondaryVisitDays;
										$scope.data.secondaryVisitCount = response.data.listObject[0].secondaryVisitCount;
										$scope.data.doctorId = response.data.listObject[0].doctorId;
										$scope.getDoctorList($scope.data.specialityId,'Y');
									}
									
								});
							}
							
							$scope.updategetOPCosultationConfigurationMaster = function() {

								$scope.data.updatedDate=$scope.currentDate;
								$scope.data.updatedBy=$scope.userId;
								var URI = BASE_URL + ROOT_URL 
										+ UPDATEOPCONSULTATIONCONFIGURATIONMASTER;
								
								BillingGenericService.serviceAction(METHOD_POST,URI, $scope.data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
									if (response.data.status == "success") {
										$scope.init();
									}
									
								});
							}
							
							$scope.updategetOPCosultationConfigurationMasterStatus = function(id,status) {

								$scope.data.updatedDate=$scope.currentDate;
								$scope.data.updatedBy=$scope.userId;
								$scope.data.opVisitRuleId = id;
								$scope.data.status=status;
								var URI = BASE_URL + ROOT_URL 
										+ UPDATEOPCONSULTATIONCONFIGURATIONMASTERSTATUS;
								
								BillingGenericService.serviceAction(METHOD_POST,URI, $scope.data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
									if (response.data.status == "success") {
										$scope.init();
									}
									
								});
							}
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initBedCategoryMasterList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							
							
							$scope.initBedCategoryMasterList = function(orgId,
									offset, noOfRecordsPerPage) {
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTBEDCATEGORYMASTER;

								var data = {
									organizationId : orgId,
									unitId :$scope.unitId ,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									unitId :$scope.unitId ,
									organizationId : $scope.organizationId
								}

								
							}

						} ]);
