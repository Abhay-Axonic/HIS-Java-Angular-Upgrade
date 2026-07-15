/**
 * @Author By  Sidheshwar Lamture
 * @name myApp.controller:diagnosticNonSurgicalProcedureConfigController
 * @description #diagnosticNonSurgicalProcedureConfigController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'diagnosticNonSurgicalProcedureConfigController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {

							/* init() function for form object create. */
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							$scope.popUpFlag = true;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$rootScope.loginpage = true;
								$scope.visitTypeId =1;
								
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							
							$scope.initMaster = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								
								$scope.configObj= {
										diagnosticProcedureConfigId : "",
										specialityId : "",
										procedureServiceId : "",
										cptCode : "",
										cptDescription :"",
										isPACSRequired : "",
										patientInstruction :"",
										procedureDuration :"",
										organizationId :$scope.organizationId,
										unitId :$scope.unitId,
										createdBy :$scope.userId,
										updatedBy :$scope.userId,
										createdDate :$scope.dt,
										updatedDate :$scope.dt,
										status :'A'
								};
								
								
							}
							$scope.initMaster();
							
						
							
							/** List And Pagination Start* */
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,
										$scope.offset,
										$scope.noOfRecordsPerPage,
										$scope.unitId);
							};

							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
								console.log($scope.visitTypeId)
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
								var data1 = {
										organizationId : organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										unitId : unitId
								};

								var data2 = {
										organizationId : organizationId,
										unitId : unitId
								};

								var URI1 = BASE_URL + ROOT_URL+ "/api/unit/clinical/getDiagnosticProcedureConfigList";
								var URI2 = BASE_URL + ROOT_URL+ "/api/unit/clinical/getDiagnosticProcedureConfigCount";

								GenericService.serviceAction("POST", URI1, data1).then(function(response) {
									     $rootScope.stopLoader();		
									if (response.data.status == "success") {
												$scope.diagnosticProcConfigList=response.data.listObject;
												console.log("diagnostic proc config list:",$scope.diagnosticProcConfigList);
											}
										});
								
								GenericService.serviceAction("POST", URI2, data2).then(function(response) {
											if (response.data.status == "success") {
												$scope.commonListCount = response.data.object;
												$scope.setPage(1,true);
												console.log("proc config count",response.data.object);
											}
										});
							   }

							   $scope.masterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
								          $rootScope.startLoader();
								           console.log("offset", offset);
								          offset = offset != null ? offset : 0;
								           noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

								var data = {
										organizationId : organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										unitId : unitId
								};

								var URI = BASE_URL + ROOT_URL+ "/api/unit/clinical/getDiagnosticProcedureConfigList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI, data).then(function(response) {
											$rootScope.stopLoader();
											$scope.diagnosticProcConfigList=[];
											$scope.diagnosticProcConfigList=response.data.listObject;;
											console.log("diagnostic proc config list:",$scope.diagnosticProcConfigList);
								});
								
								
							}
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 && page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage,
										$scope.unitId);
								if (flag) {
									    $scope.masterList($scope.organizationId,
										$scope.pager.startIndex,
										$scope.pager.pageSize,
										$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							/** List And Pagination End* */

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.save();
							}

							  
							$scope.specialityList=[];
							$scope.getActiveSpecialityList=function(){
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVESPECIALITY;
							var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
							GenericService.serviceAction("POST",URI,data)
							.then(
									function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.specialityList = response.data.listObject;
									});
							}
							$scope.getActiveSpecialityList();

							$scope.selectProcService = function(info, model,
									label) {
								console.log(info);
								$scope.configObj.procedureServiceId=info.serviceId;
							}
							
							$scope.searchProcServices = function(searchKey) {
								var data = {									
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									serviceStandardName :searchKey,
									//procedureTypeId :3
									procedureTypeIdList :[1,2]
								}
								var URI = BASE_URL + ROOT_URL+"/api/global/getProcedureListByProcedureType";
								
								return GenericService.serviceAction("POST",URI,data)
										.then(function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
													return response.data.listObject;
													console.log("service list",response.data.listObject);
												});

							}
					
							$scope.getSavePatientInstruction=function(obj){
								$scope.configObj.patientInstruction=obj.patientInstruction;
							}
           
            //for save data

				$scope.save = function() {
					try {

						if ($scope.popUpFlag == false) {
							$rootScope.startLoader();
						}
						var date = new Date();
						$scope.currentDate= moment(date).format('DD-MM-YYYY HH:mm:ss');

						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {

							var data = {	
									
									diagnosticProcedureConfigId : $scope.configObj.diagnosticProcedureConfigId,
									specialityId : 	$scope.configObj.specialityId ,
									procedureServiceId : $scope.configObj.procedureServiceId, 
									cptCode : $scope.configObj.cptCode, 
									cptDescription :$scope.configObj.cptDescription,
									isPACSRequired :$scope.configObj.isPACSRequired,
									patientInstruction :$scope.configObj.patientInstruction,
									procedureDuration : $scope.configObj.procedureDuration,
									organizationId :$scope.configObj.organizationId,
									unitId :$scope.configObj.unitId,
									createdBy :$scope.userId,
									updatedBy :$scope.userId,
									createdDate :$scope.currentDate,
									updatedDate :$scope.currentDate,
									status :'A'
									
								};
							var URI = BASE_URL + ROOT_URL+ "/api/unit/clinical/saveDiagnosticProcedureConfig";
							console.log("Save-URI-", URI);
							console.log(" save OBJ", data);

							GenericService.serviceAction("POST", URI,data).then(function(response) {

								if (response.data.status == 'success') {
									growl.success(response.data.message,
											{
												title : 'Success!'
											});

									// $scope.$broadcast('show-errors-reset');
									$scope.initMaster();
									 $scope.serviceId="";
									$scope.initMasterList(
											$scope.organizationId,
											$scope.offset,
											$scope.noOfRecordsPerPage,
											$scope.unitId);
								    } else if (response.data.status == 'error') {
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								   }
								$rootScope.stopLoader();
							});
							
						}

					} catch (e) {
						console.log("Exception-", e);
					}
				}

				$scope.showDetailsBtn = function(data) {
					$scope.configDetails = data;
				}
				//op ip er day
				 $scope.serviceId="";
				$scope.showUpdateBtn = function(id) {
					try {
						$rootScope.startLoader();
						$scope.saveBtnFlag = false;
						$scope.updateBtnFlag = true;
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ "/api/unit/clinical/getDiagnosticProcedureConfigById" + S+ id + S + $scope.organizationId+ S + $scope.unitId;
						console.log("EDIT", URI);
						GenericService.serviceAction("GET", URI).then(function(response) {
									console.log("Edit",response);
									if (response.data.status == "success") {
										$scope.configObj.diagnosticProcedureConfigId =response.data.object.diagnosticProcedureConfigId;
										$scope.configObj.specialityId =response.data.object.specialityId;
									    $scope.configObj.procedureServiceId =response.data.object.procedureServiceId;
									    $scope.serviceId=response.data.object.serviceName;
										$scope.configObj.cptCode =response.data.object.cptCode;
										$scope.configObj.cptDescription =response.data.object.cptDescription;
										$scope.configObj.patientInstruction =response.data.object.patientInstruction;
										$scope.configObj.procedureDuration =response.data.object.procedureDuration;
									} else if (response.data.status == 'error') {
										growl.error(
												response.data.message,
												{
													title : 'Error!'
												});
									}
									$rootScope.stopLoader();
								});


					} catch (e) {
						console.log("Exception-", e);
					}
				}

			
				
			
				//For update 
				$scope.update = function() {
					try {
						
                            var date = new Date();
                            $scope.currentDate = moment(date).format('DD-MM-YYYY HH:mm:ss');
						     $rootScope.startLoader();
						     var data = {
						    			diagnosticProcedureConfigId : $scope.configObj.diagnosticProcedureConfigId,
										specialityId : 	$scope.configObj.specialityId ,
										procedureServiceId : $scope.configObj.procedureServiceId, 
										cptCode : $scope.configObj.cptCode, 
										cptDescription :$scope.configObj.cptDescription,
										isPACSRequired :$scope.configObj.isPACSRequired,
										patientInstruction :$scope.configObj.patientInstruction,
										procedureDuration : $scope.configObj.procedureDuration,
										organizationId :$scope.configObj.organizationId,
										unitId :$scope.configObj.unitId,
										updatedBy :$scope.userId,
										updatedDate :$scope.currentDate,
										status :'A'
									};
						     
							
						var URI = BASE_URL + ROOT_URL+ "/api/unit/clinical/updateDiagnosticProcedureConfig";
						console.log("Update-URI-", URI);
						console.log("Update OBJ",  data);
						GenericService.serviceAction("POST", URI,  data).then(function(response) {

							if (response.data.status == 'success') {
									growl.success("Updated Successfully",
											{
										title : 'Success!'
											});	
								// $scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.initMasterList(
								$scope.organizationId,
								$scope.offset,
								$scope.noOfRecordsPerPage,
								$scope.unitId);
							} else if (response.data.status == 'error') {
								growl.error(
										response.data.message,
										{
											title : 'Error!'
										});
							}
							$rootScope.stopLoader();
						});

					} catch (e) {
						console.log("Exception-", e);
					}
				}


				//update status function 
				$scope.updateStatus = function(id, status) {
					try {
						var date = new Date();
						$scope.dt = moment(date).format(
								'DD-MM-YYYY HH:mm:ss');

						 var data = {
									organizationId :$scope.organizationId,
									unitId :$scope.unitId,
									updatedBy :$scope.userId,
									updatedDate :$scope.dt,
									status :status,
									diagnosticProcedureConfigId : $scope.configObj.diagnosticProcedureConfigId
								};
						var URI = BASE_URL + ROOT_URL+ "/api/unit/clinical/updateDiagnosticProcedureConfigStatus";
						console.log("update status",data);
						// return false;
						GenericService.serviceAction("POST", URI,data).then(
										function(response) {
											if (response.data.status == 'success') {
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												// $scope.$broadcast('show-errors-reset');
												        $scope.initMaster();
												        $scope.initMasterList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage,
																$scope.unitId);
											} else if (response.data.status == 'error') {
												growl.error(
														response.data.message,
														{
															title : 'Error!'
														});
											}
											$rootScope.stopLoader();
										});
					} catch (e) {
						console.log("Exception", e);
					}
				}

				/** Crud Operations End* */
               
               
               
          
						} ]);
