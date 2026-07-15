'use strict';

/**
 * @Author By  Sidheshwar Lamture
 * @name myApp.controller:formulationTypeToDosageTypeMapperController
 * @description #formulationTypeToDosageTypeMapperController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'formulationTypeToDosageTypeMapperController',
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
								
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							$scope.initMaster = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.searchKeyword="";
								$scope.commonObj = {
										formulationTypeId : "",
										dosageForm : "",
										factorId :1,
										fromConversionFactor:"",
										fromConversionFactorUnit:"",
										toConversionFactor:"",
										toConversionFactorUnit:"",
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
								$scope.commonList=[];
								
								if($scope.searchKeyword!=""){
									data1.searchKeyword=$scope.searchKeyword;
								}
							    var URI1 = BASE_URL + ROOT_URL+"/api/global/getFormulationTypeToDosageMapperList" ;
								GenericService.serviceAction("POST", URI1, data1).then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												   $scope.commonList=[];
													$scope.commonList=response.data.listObject;
													$scope.commonListCount = $scope.commonList.length;
													$scope.setPage(1,false);
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

								var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI, data).then(function(response) {
											$rootScope.stopLoader();
										    $scope.commonList=response.data.listObject;
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

							$scope.searchRecord=function(){
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							}
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.save();
							}

							  
							$scope.formulationTypeList=[];
							$scope.getFormulationTypeList=function(){
								var URI1 = BASE_URL + ROOT_URL +"/api/global/formulation/types/"+$scope.organizationId; 
								GenericService.serviceAction("GET",URI1,{}).then(function(response){
										$scope.formulationTypeList=response.data;
								});
							}
							$scope.getFormulationTypeList();
							
							//get dose unit list
							$scope.unitList=[];
							$scope.getUnitList=function(){
								var URI = BASE_URL + ROOT_URL
								+ "/api/globals/strength/units/"+$scope.organizationId;
								
						  GenericService.serviceAction("GET", URI, {})
							.then(
									function(response) {
											$scope.unitList = response.data;
									});
							}
							$scope.getUnitList();

               
           

				//for SAVE data

				$scope.save = function() {
					try {

						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						
						if($scope.commonObj.formulationTypeId==""){
							growl.warning(
									'Formulation type required !!',
									{
										title : 'Warning!'
									});
						return false;
						}
						
						if($scope.commonObj.dosageForm==""){
							growl.warning(
									'Dosage form required !!',
									{
										title : 'Warning!'
									});
						return false;
						}
						
						
						if($scope.commonObj.factorId==1){
							if($scope.commonObj.fromConversionFactor=="" || $scope.commonObj.fromConversionUnit=="" ||  $scope.commonObj.toConversionFactor=="" || $scope.commonObj.toConversionUnit==""){
								 growl.warning(
											'fill all conversion fields !!',
											{
												title : 'Warning!'
											});
								return false;
							}
						}
							
							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								
								$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL+ "/api/global/saveFormulationTypeToDosageMapper";
							GenericService.serviceAction("POST", URI,$scope.commonObj).then(function(response) {

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
				
				$scope.showUpdateBtn = function(obj) {
					try {
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;

                        var date = new Date();
					    $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						$scope.commonObj = {
								formulationTypeId : obj.formulationTypeId,
								dosageForm : obj.dosageForm,
								factorId :obj.factorId,
								organizationId :$scope.organizationId,
								unitId :$scope.unitId,
								createdBy :$scope.userId,
								updatedBy :$scope.userId,
								createdDate :$scope.dt,
								updatedDate :$scope.dt,
								status :'A'
						};

						if(obj.factorId==1){
							$scope.commonObj.fromConversionFactor=obj.fromConversionFactor;
							$scope.commonObj.fromConversionFactorUnit=obj.fromConversionFactorUnit;
							$scope.commonObj.toConversionFactor=obj.toConversionFactor;
							$scope.commonObj.toConversionFactorUnit=obj.toConversionFactorUnit;
						}
						
						$scope.commonObj.formulationDosageMappId=obj.formulationDosageMappId;
						
					} catch (e) {
						console.log("Exception-", e);
					}
				}

				
				
				//For update 
				$scope.update = function() {
					try {
						
                            var date = new Date();
						     $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						     $rootScope.startLoader();
							
						var URI = BASE_URL + ROOT_URL+ "/api/global/updateFormulationTypeToDosageMapper";
						GenericService.serviceAction("POST", URI,  $scope.commonObj).then(function(response) {

							if (response.data.status == 'success') {
									growl.success("Updated Successfully",
											{
										title : 'Success!'
											});	
								$scope.initMaster();
								$scope.initMasterList(
								$scope.organizationId,
								$scope.offset,
								$scope.noOfRecordsPerPage,
								$scope.unitId);
							}else if (response.data.status == 'error') {
								growl.error(response.data.message,
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
									formulationDosageMappId:id
								};
						var URI = BASE_URL + ROOT_URL+ "/api/global/updateFormulationTypeToDosageMapperStatus";
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
