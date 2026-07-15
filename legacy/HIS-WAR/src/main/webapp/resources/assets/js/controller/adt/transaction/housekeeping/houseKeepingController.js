'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:houseKeepingController
 * @description #houseKeepingController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'houseKeepingController',
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
						'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, $stateParams) {
							$scope.userFlag=false;
							/* init() function for form object create. */
							$scope.init = function() {

								
								$rootScope.startLoader();
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.saveFlag = true;
								$scope.markAsCleanFlag = true;
								
								$scope.bedsearch = {
									floorId : 0,
									nursingStationId : 0,
									bedCategoryId : 0,
									wardId : 0,
									housekeepingStatusId : 0,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,

								}

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.bedHousekeeping = {}
								$scope.itemSelecteds = {};
								
								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

								
								
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEFLOORLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.activeFloorList = [];

													if (response.data.status == "success") {
														$scope.activeFloorList = response.data.listObject;
														$rootScope.stopLoader();
													}
													else if(response.data.status == "error")
													{
													   $rootScope.stopLoader();
													}
												});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEBEDCATEGORYLISTBYUNIT;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.bedCategoryList = [];

													if (response.data.status == "success") {
														$scope.bedCategoryList = response.data.listObject;
														 $rootScope.stopLoader();
													}
													else if(response.data.status == "error")
													{
													   $rootScope.stopLoader();
													}
												});

								

								var URI = BASE_URL + ROOT_URL
										+ GETBEDLISTFORHOUSEKEEPING;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.housekeepingBedList = [];

													if (response.data.status == "success") {
														$scope.housekeepingList = response.data.listObject;
														
														angular
														.forEach( $scope.housekeepingList, function( value, index) {
																	var newArr = {};
																	newArr.housekeepingId = value.housekeepingId;
																	newArr.bedId = value.bedId;
																	newArr.priorityId = value.priorityId;
																	if(value.assignedPersonId != null)
																	newArr.assignedPersonId = value.assignedPersonId.toString();
																	else
																		newArr.assignedPersonId = value.assignedPersonId;	
																	newArr.floorName = value.floorName;
																	newArr.nursingStationDesc = value.nursingStationDesc;
																	newArr.bedCategoryDesc = value.bedCategoryDesc;
																	newArr.bedNumber = value.bedNumber;
																	newArr.priorityDesc = value.priorityDesc;
																	newArr.note = value.note;
																	newArr.housekeepingStatusDesc = value.housekeepingStatusDesc;
																	newArr.housekeepingStatusId = value.housekeepingStatusId.toString();
																	newArr.userName = value.userName;
																	newArr.reasonDesc = value.reasonDesc;
																	newArr.enableDD = false;
																	$scope.housekeepingBedList.push(newArr);
																});
														$rootScope.stopLoader();
														
													}
													else if(response.data.status == "error")
														{
														   $rootScope.stopLoader();
														}
												});
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getActiveUserListForHouskeeping";
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.usersList = [];

											if (response.data.status == "success") {
												$scope.usersList = response.data.listObject;
												$rootScope.stopLoader();
											}
											else if(response.data.status == "error")
											{
											   $rootScope.stopLoader();
											}
										});

							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.updateHouseKeepingRequest();
							}
							
							$scope.selectItem = function(item) {
								$scope.userFlag=true;
								$scope.itemSelecteds = [];
								console.log("Item object ",item);
								// If checkbox is checked
								if ($scope.bedHousekeeping) 
								{
									if(item.assignedPersonId !=null && item.assignedPersonId >0 && item.housekeepingStatusId!=3)
										{
											$scope.saveFlag = true;
											$scope.markAsCleanFlag = false;
										}
									else{
											$scope.saveFlag = false;
											$scope.markAsCleanFlag = true;
										}
									$scope.itemSelecteds = item;
								}
								console.log($scope.itemSelecteds);
								
								angular.forEach($scope.housekeepingBedList,function(value,key) {
										value.enableDD = (Number(value.housekeepingId) === Number($scope.itemSelecteds.housekeepingId)?true:false);
								});
								
							}
							
							
							$scope.getBedById = function(commonType) {

								console.log($scope.bedsearch);

								var commonData = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								switch (commonType) {
								case 'floor':

									commonData.floorId = $scope.bedsearch.floorId;

									var URI = BASE_URL + ROOT_URL
											+ GETWARDLISTBYFLOORIDORGUNITID;
									GenericService
											.serviceAction("POST", URI,
													commonData)
											.then(
													function(response) {
														$scope.wardList = [];

														if (response.data.status == "success") {
															$scope.wardList = response.data.listObject;

														}
													});
									break;
								case 'ward':

									commonData.wardId = $scope.bedsearch.wardId;

									var URI = BASE_URL
											+ ROOT_URL
											+ GETACTIVENURSINGSTATIONLISTBYWARDID;
									GenericService
											.serviceAction("POST", URI,
													commonData)
											.then(
													function(response) {
														$scope.nursingStationList = [];
														$scope.nursingStationList1 =  [];
														
														if (response.data.status == "success") {
															$scope.nursingStationList1 = response.data.listObject; 
															angular.forEach($scope.nursingStationList1 , function(value,key){ // resolve
																																// HIS-14918
																 var found = $scope.nursingStationList.find(ar=>ar.nursingStationId == value.nursingStationId);
																   if(found==undefined)
																	   {
																	   $scope.nursingStationList.push(value);
																	   }
																   else{
																   }
															});
														}
													});
									break;
								default:

								}

							}

							$scope.bedSearchByMultipleCriteria = function() {
								$rootScope.startLoader();
								// $scope.bedsearch.isVirtual =
								// ($scope.bedsearch.isVirtual == true) ? "Y":
								// "N";
								console.log($scope.bedsearch);
								// return false;
								var URI = BASE_URL + ROOT_URL
										+ SEARCHBEDBYMULTIPLECRITERIAFORHOUSEKEEPING;
								GenericService
										.serviceAction("POST", URI,
												$scope.bedsearch)
										.then(
												function(response) {
													$scope.housekeepingBedList = [];

													if (response.data.status == "success") {
	                                                   $scope.housekeepingList = response.data.listObject;
														
														angular
														.forEach( $scope.housekeepingList, function( value, index) {
																	var newArr = {};
																	newArr.housekeepingId = value.housekeepingId;
																	newArr.bedId = value.bedId;
																	newArr.priorityId = value.priorityId;
																	newArr.assignedPersonId = value.assignedPersonId;
																	newArr.floorName = value.floorName;
																	newArr.nursingStationDesc = value.nursingStationDesc;
																	newArr.bedCategoryDesc = value.bedCategoryDesc;
																	newArr.bedNumber = value.bedNumber;
																	newArr.priorityDesc = value.priorityDesc;
																	newArr.note = value.note;
																	newArr.housekeepingStatusDesc = value.housekeepingStatusDesc;
																	newArr.housekeepingStatusId = value.housekeepingStatusId.toString();
																	newArr.userName = value.userName;
																	$scope.housekeepingBedList.push(newArr);
																});
														 $rootScope.stopLoader();

													}
													else if(response.data.status == "error")
														{
														  $rootScope.stopLoader();
														}
													
												});

							}

							$scope.updateMarkedAsClean = function()
							{
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one Bed...!!',
													{
														title : 'Warning!'
													});
									return false;
								} else {
									$rootScope.startLoader();
									if( $scope.itemSelecteds.housekeepingStatusId == '2' || $scope.itemSelecteds.housekeepingStatusId == '3')
										{
										$rootScope.startLoader();
										// alert();
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');
									var data = {
										housekeepingId : $scope.itemSelecteds.housekeepingId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										status : 'A',
										bedId : $scope.itemSelecteds.bedId,
										priorityId : $scope.itemSelecteds.priorityId,
										assignedPersonId :  $scope.itemSelecteds.assignedPersonId
									};
									console.log(data);
									// return false;
									var URI = BASE_URL + ROOT_URL
											+ UPDATEFORMARKEDASCLEAR;
									GenericService
											.serviceAction("POST", URI,
													data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$rootScope.stopLoader();
															growl
																	.success(
																			'Marked as clean..!!!',
																			{
																				title : 'Success!'
																			});
															$scope.init();
															$rootScope.stopLoader();
														} else  if (response.data.status == "error")
														{
															$rootScope.stopLoader();
															growl
																	.error(
																			'Something went wrong...!!',
																			{
																				title : 'Error!'
																			});
														}

													});
										}
									else
										{
										growl
										.warning(
												'Please assigned person before marked as clean.',
												{
													title : 'Warning!'
												});
										$rootScope.stopLoader();
										}
								}
							}
							
							$scope.updateHouseKeepingRequest = function()
							{
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one Bed...!!',
													{
														title : 'Warning!'
													});
									return false;
								} else {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										
										if($scope.itemSelecteds.assignedPersonId == null){
											growl
											.warning(
													'Please assign person...!!',
													{
														title : 'Warning!'
													});
										}
										else
											{
											$rootScope.startLoader();
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');
										var data = {
											housekeepingId : $scope.itemSelecteds.housekeepingId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											updatedBy : $scope.userId,
											updatedDate : $scope.currentDate,
											status : 'A',
											assignedPersonId :  $scope.itemSelecteds.assignedPersonId,
											priorityId : $scope.itemSelecteds.priorityId
										};
										console.log(data);
										// return false;
										var URI = BASE_URL + ROOT_URL
												+ UPDATEHOUSEKEEPING;
										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == "success") {
																growl
																		.success(
																				'Housekeeping Request Sent Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$scope.init();
																$rootScope.stopLoader();
															} else if (response.data.status == "error"){
																$rootScope.stopLoader();
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
															}

														});
									}
									}
								}
							}
							
							$scope.displayNote = function(note)
							{
								$scope.noteDoctor = note;
							}
								
					
							
							
						} ]);
