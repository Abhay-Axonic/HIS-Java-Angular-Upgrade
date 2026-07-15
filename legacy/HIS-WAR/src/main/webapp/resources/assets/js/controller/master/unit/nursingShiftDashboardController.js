'use strict';

/**
 * @Author By Sidheshwar Lamture
 * @name myApp.controller:nurseAllocationMsasterController
 * @description #nurseAllocationMsasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nursingShiftDashboardController',
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

							var date = new Date();
							$scope.dt = moment(date).format(
									'DD-MM-YYYY HH:mm:ss');
							
							   $scope.dateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
								};
						        

							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
								opened : false
							};

							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
								opened : false
							};

							$scope.openFromDatePickerObj = function() {
								$scope.fromDatepickerObj.opened = true;
							};

							$scope.fromDatepickerObj = {
								opened : false
							};

							$scope.openToDatePickerObj = function() {
								$scope.toDatepickerObj.opened = true;
							};

							$scope.toDatepickerObj = {
								opened : false
							};

							$scope.fromDate = '';
							$scope.toDate = '';
							$scope.showRoaster = 'N';
							$scope.selectedbedList = [];
							// console.log($scope.selectedbedList)

							var bedsIdList = [];
							$scope.bedIdList = [];

							/** List And Pagination Start* */
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,
										$scope.offset,
										$scope.noOfRecordsPerPage,
										$scope.unitId);
							};

							$scope.showRoasterFun = function() {
								var fromDateobj, toDateobj;
								$scope.showRoaster = 'N';
								fromDateobj = $scope.fromDate;
								toDateobj = $scope.toDate;
								$scope.initMasterList($scope.organizationId,
										$scope.offset,
										$scope.noOfRecordsPerPage,
										$scope.unitId, fromDateobj, toDateobj);
							};

							$scope.initMasterList = function(organizationId,
									offset, noOfRecordsPerPage, unitId,
									fromDateobj, toDateobj) {
								var nurseIdObj = $scope.userId;
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var toDate = moment(new Date(), "DD-MM-YYYY")
										.add('days', 15).toDate();
								var data1 = {
									orgId : organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage,
									unitId : unitId,
									// fromDate :moment(new
									// Date()).format('YYYY-MM-DD'),
									// toDate
									// :moment(toDate).format('YYYY-MM-DD')
									fromDate : moment(fromDateobj).format(
											'YYYY-MM-DD'),
									toDate : moment(toDateobj).format(
											'YYYY-MM-DD'),
									nurseId : nurseIdObj
								// fromDate :"2019-05-30",
								// toDate :"2019-06-13"
								};

								var data2 = {
									orgId : organizationId,
									unitId : unitId,
									fromDate : "28-05-2019",
									toDate : "13-06-2019"
								};

								var URI1 = BASE_URL
										+ ROOT_URL
										+ "/api/global/getActiveNurseAllocationList";
								// count api for nurse allocation list
								var URI2 = BASE_URL
										+ ROOT_URL
										+ "/api/adt/unit/getActiveNurseAllocationMasterByNurseId/"
										+ nurseIdObj + "/" + organizationId
										+ "/" + unitId;
								$scope.shiftListNurse = [];

								var fDate = moment(fromDateobj).format(
										'DD-MM-YYYY');
								var fromDate = moment(new Date(), "DD-MM-YYYY")
										.toDate();
								var length = Math.floor((Date.UTC(toDateobj
										.getFullYear(), toDateobj.getMonth(),
										toDateobj.getDate()) - Date.UTC(
										fromDateobj.getFullYear(), fromDateobj
												.getMonth(), fromDateobj
												.getDate()))
										/ (1000 * 60 * 60 * 24));
								$scope.dateDiffForShow=length;
								
								var i = 1, year, month, day, shiftDate;
								$scope.nurseShiftListFinal = [];
								var data = {
									shiftDate : fDate
								};

								$scope.nurseShiftListFinal.push(data);

								for (i = 1; i <= length; i++) {
									var pdate = moment(fromDateobj,
											"DD-MM-YYYY").add('days', i)
											.toDate();
									year = pdate.getFullYear();
									month = pdate.getMonth();
									month = (Number(pdate.getMonth()) + 1) <= 9 ? "0"
											+ (Number(pdate.getMonth()) + 1)
											: (Number(pdate.getMonth()) + 1);
									date = pdate.getDate() <= 9 ? "0"
											+ pdate.getDate() : pdate.getDate();
									shiftDate = date + "-" + month + "-" + year;
									var data = {
										shiftDate : shiftDate
									};
									$scope.nurseShiftListFinal.push(data);
								}
								$rootScope.stopLoader();
								console.log("$scope.nurseShiftListFinal",
										$scope.nurseShiftListFinal);

								var data = {};
								GenericService
										.serviceAction("GET", URI2, data)
										.then(
												function(response1) {
													if (response1.data.status == "success") {
														var p;
														$scope.opNurseShiftList=[];
														for(var p=0;p<=length;p++){
															$scope.opNurseShiftList.push(response1.data.listObject[0])
														}
														
													}
													
												});
								
													GenericService
															.serviceAction(
																	"POST",
																	URI1, data1)
															.then(
																	function(
																			response) {
																		if (response.data.status == "success") {
																			$scope.shiftListNurse = response.data.object.nurseShiftList;
																			$scope.shiftListNurseFinal = [];
																			$scope.listNurse = response.data.object.nurseList;
																			$scope.bedListNurse = response.data.object.nurseBedList;
																			
																			if($scope.listNurse.length==0){
																				growl
																						.warning(
																								'Rostering is not created.!!',
																								{
																									title : 'Warning!'
																								});
																				return false;
																		}
																			
																			var h;
																			$scope.shiftBedListNurseFinal = [];
																			$scope.finalNurseList = [];

																			for (h = 0; h < $scope.listNurse.length; h++) {
																				var count = 0, i, j, k;
																				$scope.finalShiftList = [];
																				$scope.finalBedList = [];
																				$scope.shiftAllList = [];
																				for (i = 0; i < $scope.shiftListNurse.length; i++) {
																					var bedNo = "";
																					var flag = 0;
																					for (j = 0; j < $scope.bedListNurse.length; j++) {
																						if ($scope.shiftListNurse[i].nurseShiftAllocationDetailsId == $scope.bedListNurse[j].nurseShiftAllocationDetailsId
																								&& $scope.listNurse[h].nurseId == $scope.bedListNurse[j].nurseId) {
																							$scope.finalBedList
																									.push($scope.bedListNurse[j]);
																							bedNo += $scope.bedListNurse[j].bedName
																									+ ",";
																						}
																					}

																					if ($scope.listNurse[h].nurseId == $scope.shiftListNurse[i].nurseId) {

																						var bedNumber = bedNo
																								.slice(
																										0,
																										bedNo.length - 1);
																						var newObj = {
																							shiftName : $scope.shiftListNurse[i].shiftName,
																							bedName :$scope.listNurse[h].wardName+"-"+bedNumber,
																							nurseId : $scope.listNurse[h].nurseId,
																							shiftDate : $scope.shiftListNurse[i].shiftDate
																						}
																						$scope.finalShiftList
																								.push($scope.shiftListNurse[i]);
																						/*
																						 * $scope.shiftListNurseFinal
																						 * .push(newObj);
																						 */
																						$scope.shiftAllList
																								.push(newObj);
																						count++;
																					}

																				}

																				angular
																						.forEach(
																								$scope.nurseShiftListFinal,
																								function(
																										value,
																										index) {
																									var cFlag = false;
																									angular
																											.forEach(
																													$scope.shiftAllList,
																													function(
																															value1,
																															index) {
																														if (value.shiftDate == value1.shiftDate) {
																															cFlag = true;
																															$scope.shiftListNurseFinal
																																	.push(value1)
																														}
																													});
																									if (cFlag == false) {
																										var newObj = {
																											shiftName : '',
																											bedName : '',
																											shiftDate : '',
																											nurseId : $scope.listNurse[h].nurseId
																										}
																										$scope.shiftListNurseFinal
																												.push(newObj);
																										
																									}
																								});

																				var nurseObj = {
																					nurseShiftAllocationId : $scope.listNurse[h].nurseShiftAllocationId,
																					status : $scope.listNurse[h].status,
																					nurseId : $scope.listNurse[h].nurseId,
																					nurseName : $scope.listNurse[h].nurseName,
																					wardId : $scope.listNurse[h].wardId,
																					wardName : $scope.listNurse[h].wardName,
																					shiftList : $scope.finalShiftList,
																					bedList : $scope.finalBedList
																				}
																				$scope.finalNurseList
																						.push(nurseObj);
																			}

																			console
																					.log(
																							"nurse ShiftAllocation Bed List:",
																							$scope.shiftListNurseFinal);
																			console
																					.log(
																							"final Nurse list:",
																							$scope.finalNurseList);
																			$scope.showRoaster = 'Y';
																		}
																		
																		$rootScope
																				.stopLoader();
																	});
											

							}

							/*	$scope.initMasterList($scope.organizationId,
								$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);*/

							/** List And Pagination End* */

						} ]);
