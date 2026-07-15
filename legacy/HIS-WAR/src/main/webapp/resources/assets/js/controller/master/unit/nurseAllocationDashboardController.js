'use strict';

/**
 * @Author By Sidheshwar Lamture
 * @name myApp.controller:nurseAllocationMsasterController
 * @description #nurseAllocationMsasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nurseAllocationDashboardController',
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

							
							var date = new Date();
							   $scope.dateOptionss =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : date.setDate((new Date()).getDate() - 90),
									
								};
							   $scope.dateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
								};
						        
							$scope.fromDate = '';
							$scope.toDate = '';
							$scope.showRoaster = 'N';
							$scope.selectedbedList = [];
							// console.log($scope.selectedbedList)
							$scope.finalNurseList = [];
							var bedsIdList = [];
							$scope.bedIdList = [];
							angular.forEach($scope.selectedbedList, function(
									value, index) {
								bedsIdList.push(value.id);
							});

							$scope.firstTabLevel = 1;
							$scope.setFirstTabLevel = function(newTab) {
								if (newTab == 1){
									$scope.isSetFirstTabLevel(1);
								}else if(newTab == 2){
									$scope.isSetFirstTabLevel(2);
								}
								$scope.firstTabLevel=newTab;
							}
							
							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
							};
							
							
							$scope.initMaster = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.popUpFlag = true;
								$scope.commonObj = {
									"roleId" : "",
									"userId" : "",
									"orgId" : $scope.organizationId,
									"unitId" : $scope.unitId,
									"createdBy" : $scope.userId,
									"updatedBy" : $scope.userId,
									"createdDate" : $scope.dt,
									"updatedDate" : $scope.dt,
									"status" : 'A',
									"wardId" : "",
									"fromDate" : "",
									"toDate" : "",
									"nurseId" : "",
									"nurseShiftList" : [],
									"bedIdShiftList" : [],
								};
								$scope.finalNurseList=[];
								$scope.showRoaster='N';
							}
							$scope.initMaster();

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
				
								var length = Math.floor((Date.UTC(toDateobj
										.getFullYear(), toDateobj.getMonth(),
										toDateobj.getDate()) - Date.UTC(fromDateobj
										.getFullYear(), fromDateobj.getMonth(),
										fromDateobj.getDate()))
										/ (1000 * 60 * 60 * 24));
								$scope.dateDiffForShow=length;
								
								if (length < 0) {
									growl
											.error(
													"To Date must be greater than From Date",
													{
														title : 'Error!'
													});
									return false;
								}
								
								$scope.initMasterList($scope.organizationId,
										$scope.offset,
										$scope.noOfRecordsPerPage,
										$scope.unitId, fromDateobj, toDateobj);
							};

							$scope.initMasterList = function(organizationId,
									offset, noOfRecordsPerPage, unitId,
									fromDateobj, toDateobj) {
								$rootScope.startLoader();
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
											'YYYY-MM-DD')
								// fromDate :"2019-05-30",
								// toDate :"2019-06-13"
								};

								var data2 = {
									orgId : organizationId,
									unitId : unitId,
									fromDate : "28-05-2019",
									toDate : "13-06-2019"
								};

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
								$scope.dateDiff=length;
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
								console.log("$scope.nurseShiftListFinal",
										$scope.nurseShiftListFinal);

								var URI1 = BASE_URL + ROOT_URL
										+ "/api/global/listNurseAllocation";
								// count api for nurse allocation list
								// var URI2 = BASE_URL + ROOT_URL+
								// "/api/global/getTotalRecordNurseShiftAllocation/"+organizationId+"/"+unitId;
								$scope.shiftListNurse = [];
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.shiftListNurse = response.data.object.nurseShiftList;
														$scope.shiftListNurseFinal = [];
														$scope.listNurse = response.data.object.nurseList;
														$scope.bedListNurse = response.data.object.nurseBedList;

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
																		bedName : bedNumber,
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

															
													 if($scope.shiftAllList.length>0){
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
															$scope.finalNurseList.push(nurseObj);
														 }
														
														}
														console.log("nurse ShiftAllocation Bed List:",$scope.shiftListNurseFinal);
														console.log("final Nurse list:",$scope.finalNurseList);

													}
													$scope.showRoaster = 'Y';
													$rootScope.stopLoader();
												});

								/*
								 * GenericService.serviceAction("GET", URI2,
								 * data2).then(function(response) { if
								 * (response.data.status == "success") {
								 * $rootScope.stopLoader();
								 * $scope.commonListCount =
								 * response.data.object;
								 * $scope.setPage(1,false);
								 * console.log("nurseAllocationMasterListcount",response.data.object); }
								 * });
								 */

							}

							$scope.masterList = function(organizationId,
									offset, noOfRecordsPerPage, unitId) {
								$rootScope.startLoader();
								console.log("offset", offset);
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;

								var data = {
									organizationId : organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage,
									unitId : unitId,
									fromDate : "01-05-2019",
									toDate : "15-05-2019"
								};

								var URI = BASE_URL + ROOT_URL
										+ "/api/global/listNurseAllocation";
								console.log("URI", URI);
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.nurseShiftList = response.data.listObject;
													;
													console.log("nurse ShiftAllocation List:",$scope.nurseShiftList);
												});

							}
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
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

							/*
							 * $scope.initMasterList($scope.organizationId,
							 * $scope.offset,
							 * $scope.noOfRecordsPerPage,$scope.unitId);
							 */

							/** List And Pagination End* */

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								if ($scope.updateBtnFlag == true) {
									$scope.updateShift();
								} else {
									$scope.saveShift();
								}

							}

			//add function start
				$scope.nurseAllocAndShiftList=[];
				$scope.shiftHeaderList=[];
				$scope.add = function(fromDate, toDate) {
					
					          if(fromDate=="" && toDate==""){
					        	  growl.warning(
											'Select From Date And To Date',
											{
												title : 'Warning!'
											});
					        	  return false;
					          }
					
								console.log(fromDate);
								var fDate = moment(fromDate).format(
										'DD-MM-YYYY');

								var length = Math.floor((Date.UTC(toDate
										.getFullYear(), toDate.getMonth(),
										toDate.getDate()) - Date.UTC(fromDate
										.getFullYear(), fromDate.getMonth(),
										fromDate.getDate()))
										/ (1000 * 60 * 60 * 24));
								$scope.dateDiff=length;
								if ($scope.commonObj.nurseId == undefined) {
									growl.error("Select Nurse", {
										title : 'Error!'
									});
									return false;
								}
								if (length < 0) {
									growl
											.error(
													"To Date must be greater than From Date",
													{
														title : 'Error!'
													});
									return false;
								}
								$scope.isAdded = 'Y';
								var i = 1, year, month, day, shiftDate;
								$scope.nurseShiftList=[];
								var data = {
									shiftDate : fDate,
									shiftId : "",
									selectedbedList : [],
									//bedIdList : $scope.bedIdList
									bedIdList : []
								};

								$scope.nurseShiftList.push(data);

								console.log("$scope.nurseShiftList",
										$scope.nurseShiftList)

								for (i = 1; i <= length; i++) {
									var pdate = moment(fromDate, "DD-MM-YYYY")
											.add('days', i).toDate();
									year = pdate.getFullYear();
									month = pdate.getMonth();
									month = (Number(pdate.getMonth()) + 1) <= 9 ? "0"
											+ (Number(pdate.getMonth()) + 1)
											: (Number(pdate.getMonth()) + 1);
									date = pdate.getDate() <= 9 ? "0"
											+ pdate.getDate() : pdate.getDate();
									shiftDate = date + "-" + month + "-" + year;
									var data = {
										shiftDate : shiftDate,
										shiftId : "",
										selectedbedList : [],
										//bedIdList : $scope.bedIdList
										bedIdList : []
									};
									$scope.nurseShiftList.push(data);
								}

								var nurseAllocAndShiftObj={};
								nurseAllocAndShiftObj.nurseList=$scope.nurseList;
								nurseAllocAndShiftObj.nurseId="";
								nurseAllocAndShiftObj.wardId=$scope.commonObj.wardId;
								nurseAllocAndShiftObj.nurseShiftList=$scope.nurseShiftList;
								nurseAllocAndShiftObj.fromDate=$scope.commonObj.fromDate;
								nurseAllocAndShiftObj.toDate=$scope.commonObj.toDate;
								$scope.nurseAllocAndShiftList.push(nurseAllocAndShiftObj);
								var length=0;
								var listHeader=[];
								angular.forEach($scope.nurseAllocAndShiftList,function(value,index){
									if(value.nurseShiftList.length>length){
										length=value.nurseShiftList.length;
										listHeader=value.nurseShiftList;
										var length = Math.floor((Date.UTC(toDate
												.getFullYear(), toDate.getMonth(),
												toDate.getDate()) - Date.UTC(fromDate
												.getFullYear(), fromDate.getMonth(),
												fromDate.getDate()))
												/ (1000 * 60 * 60 * 24));
										$scope.dateDiff=length;
									}
								});
								$scope.shiftHeaderList=listHeader;
							}

				//add function end
				
							$scope.getWardMasterList = function() {

								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

								var URI = BASE_URL + ROOT_URL
										+ "/api/adt/getActiveWardMasterList";

								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													console.log(response);
													$scope.doctorList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.wardList = response.data.listObject;
												});

							};
							$scope.getWardMasterList();

							$scope.getMappedNurseByWardId = function(wardId) {
								console.log(wardId)
								var URI3 = BASE_URL
										+ ROOT_URL
										+ "/api/adt/unit/getActiveNurseAllocationMasterByWardId"
										+ S + wardId + S
										+ $scope.organizationId + S
										+ $scope.unitId;
								var data = {};
								var data1 = {
									unitId : $scope.unitId,
									orgId : $scope.organizationId,
									wardId : wardId

								};
								
								 GenericService.serviceAction("GET", URI3, data).then(function(response) {
										$scope.nurseList = [];
										$scope.nurseList = response.data.listObject;
										console.log("nurseList:",$scope.nurseList);
										var URI = BASE_URL + ROOT_URL + "/api/global/getbedlistbywardId";
										 GenericService.serviceAction("POST", URI, data1).then(function(response1) {
															$scope.bedList = [];
															$scope.bedIdList = [];
															if (response1.data.status == "success") {
																$scope.bedList = response1.data.listObject;
																angular.forEach($scope.bedList,function(value,index) {
																					var bed = {};
																					bed.id = value.bedId;
																					bed.label = value.bedName;
																					$scope.bedIdList.push(bed);

																});
																console.log($scope.bedIdList);
															}
															//return response1;
										  });
								});
								
							}

							
							$scope.getActiveShiftMasterList = function() {
								var URI3 = BASE_URL
										+ ROOT_URL
										+ "/api/common/getActiveShiftMasterList"
										+ S + $scope.organizationId + S
										+ $scope.unitId;
								var data = {}

								GenericService
										.serviceAction("GET", URI3, data)
										.then(
												function(response) {

													$scope.shiftList = [];
													$scope.shiftList = response.data.listObject;
													console.log("shiftList:",
															$scope.shiftList);
													console.log("shiftList3:",
															$scope.shiftList);

												});
							}
							$scope.getActiveShiftMasterList();
							// check shift for next day
							$scope.checkShift = function(obj, idx,nurseAlloObj) {
								var data = {
									unitId : $scope.unitId,
									orgId : $scope.organizationId,
									shiftId : obj.shiftId,
									shiftDate : obj.shiftDate.toString()
								};
								$scope.listBed = [];
								var URI3 = BASE_URL + ROOT_URL
										+ "/api/global/checkbedIdbyshiftdate";
								 GenericService
										.serviceAction("POST", URI3, data)
										.then(
												function(response) {
													//already allocated bed list
													$scope.assignedBedList = response.data.listObject;
													console.log("assigned bed list:",$scope.assignedBedList);
													obj.selectedbedList=[];
													var selectedShiftBedList=[];
													angular.forEach($scope.nurseAllocAndShiftList,function(value,index){
														var bedShiftList=$.grep(value.nurseShiftList, function (shiftObj) {
													        return (shiftObj.shiftId === obj.shiftId && shiftObj.shiftDate===obj.shiftDate.toString());
													    });
														if(bedShiftList.length>0){
														 angular.forEach(bedShiftList[0].selectedbedList,function(value1,index1){
															 var bedObj={};
															 bedObj.bedId=value1.id;
															 $scope.assignedBedList.push(bedObj);
														 });
														}
													});
													
													angular.forEach($scope.bedList,function(value,index) {
																		var flag = false;
																		angular.forEach($scope.assignedBedList,function(value1,index) {
																							if (value1.bedId == value.bedId) {
																								flag = true;
																							}
																						});

																		if (flag == false) {
																			var bed = {};
																			bed.id = value.bedId;
																			bed.label = value.bedName;
																			$scope.listBed.push(bed);
																		}
															});
													if($scope.updateBtnFlag == true){
														nurseAlloObj.nurseShiftList[idx].selectedbedList=obj.selectedbedList;
													}
													nurseAlloObj.nurseShiftList[idx].bedIdList = $scope.listBed;
												});

							}

							// for SAVE data
							$scope.saveShift = function() {
								try {
									if($scope.nurseAllocAndShiftList == undefined || $scope.nurseAllocAndShiftList == null || $scope.nurseAllocAndShiftList.length==0){
										growl.error("Please Add Data", {
											title : 'Error!'
										});
										return false;
									}
								    var isAllDataFilledNurse=true;
								    var isAllDataFilledshiftId=true;
									angular.forEach($scope.nurseAllocAndShiftList,function(value,index){
										if (value.nurseId == undefined || value.nurseId == '') {
											isAllDataFilled=false;
										}
									for (var i = 0; i < value.nurseShiftList.length; i++) {
										console.log(value.nurseShiftList[i].selectedbedList.length);
										if (value.nurseShiftList[i].shiftId == "" || value.nurseShiftList[i].selectedbedList.length == 0) {
											isAllDataFilledshiftId=false;
										}
									}
									});
									
									if(isAllDataFilledNurse == false){
										growl.error("Please Select Nurse All For Days ", {
											title : 'Error!'
										});
										return false;
									}
									
									if(isAllDataFilledshiftId == false){
										growl.error("Please select all For Shifts", {
											title : 'Error!'
										});
										return false;
									}
										
									
									if ($scope.popUpFlag == false) {
										console.log("$scope.popUpFlag "
												+ $scope.popUpFlag)
										$rootScope.startLoader();
									}
									var date = new Date();
									$scope.dt = moment(date).format(
											'DD-MM-YYYY HH:mm:ss');

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
									} else {
										console.log("SAVENURSESHIFTALLOCATION");
										$scope.commonObj.bedIdShiftList = $scope.selectedbedList;
										$scope.fromDate = moment($scope.commonObj.fromDate).format('DD-MM-YYYY HH:mm:ss');
										$scope.toDate = moment($scope.commonObj.toDate).format('DD-MM-YYYY HH:mm:ss');
										
										$scope.finalNurseShiftListForSave=[];
										angular.forEach($scope.nurseAllocAndShiftList,function(value,index){
										   angular.forEach(value.nurseShiftList,function(value1, index1) {
											value1.shiftDate = value1.shiftDate+ " 00:00:00";
											});
										   var finalObj={};
										   finalObj.wardId=value.wardId;
										   finalObj.nurseId=value.nurseId;
										   finalObj.fromDate=moment(value.fromDate).format('YYYY-MM-DD HH:mm:ss');
										   finalObj.toDate=moment(value.toDate).format('YYYY-MM-DD HH:mm:ss');
										   finalObj.shiftList=value.nurseShiftList;
										   $scope.finalNurseShiftListForSave.push(finalObj);
										});
										var data = {

											orgId : $scope.organizationId,
											unitId : $scope.unitId,
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											// createdDate :$scope.dt,
											// updatedDate :$scope.dt,
											status : 'A',

											/*wardId : $scope.commonObj.wardId,
											fromDate : moment(
													$scope.commonObj.fromDate)
													.format(
															'YYYY-MM-DD HH:mm:ss'),
											toDate : moment(
													$scope.commonObj.toDate)
													.format(
															'YYYY-MM-DD HH:mm:ss'),
											nurseId : $scope.commonObj.nurseId,
											shiftList : $scope.nurseShiftList*/
											nurseShiftList:$scope.finalNurseShiftListForSave
										};
										var URI = BASE_URL + ROOT_URL + "/api/global/saveNurseShiftAllocation";

										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == 'success') {
																growl.success("Roster Created Successfully.",
																				{
																					title : 'Success!'
																				});

																// $scope.$broadcast('show-errors-reset');
																$scope.initMaster();
																$scope.nurseShiftList = [];
																$scope.bedIdList = [];
																$scope.isAdded = 'N';
																$scope.nurseAllocAndShiftList=[];
															} else if (response.data.status == 'error') {
																growl.error(response.data.message,
																				{
																					title : 'Error!'
																				});
																angular.forEach($scope.nurseShiftList,
																				function(value,index) {
																					value.shiftDate = (value.shiftDate).substring(0,10);
																});
															}
															$rootScope.stopLoader();
														});

									}

								} catch (e) {
									console.log("Exception-", e);
								}
							}

							$scope.showUpdateBtn = function(info) {
								try {
									if(info.bedList.length==0){
										 growl.warning(
													'Can not update roster!!',
													{
														title : 'Warning!'
													});
										return false;
									}
									$rootScope.startLoader();
									$scope.nurseAllocAndShiftList=[];
									$scope.saveBtnFlag = false;
									$scope.updateScope = true;
									$scope.updateBtnFlag = true;
									$scope.isAdded = 'N';
									$scope.nurseShiftList = [];
									$scope.commonObj.wardId = info.wardId;
									$scope.commonObj.nurseShiftAllocationId = info.nurseShiftAllocationId;
									var promise = $scope.getBedListByWardId(info.wardId);
									promise.then(function(response) {
										
										        var nurseObj={};
										        $scope.nurseList=[];
										        nurseObj.userId=info.nurseId;
										        nurseObj.nurseName=info.nurseName;
										        $scope.nurseList.push(nurseObj);
										        
												$scope.commonObj.nurseId = info.nurseId;
												var i, j;
												for (i = 0; i < info.shiftList.length; i++) {
													$scope.nurseBedList = [];
													for (j = 0; j < info.bedList.length; j++) {
														if (info.shiftList[i].nurseShiftAllocationDetailsId == info.bedList[j].nurseShiftAllocationDetailsId
																&& info.shiftList[i].nurseId == info.bedList[j].nurseId) {
															var bed = {};
															bed.id = info.bedList[j].bedId;
															bed.label = info.bedList[j].bedName;
															$scope.nurseBedList.push(bed);
														}

													}
													var data = {
														shiftDate : info.shiftList[i].shiftDate,
														shiftId : info.shiftList[i].shiftId,
														selectedbedList : $scope.nurseBedList,
														bedIdList : $scope.bedIdList,
														nurseShiftAllocationDetailsId : info.shiftList[i].nurseShiftAllocationDetailsId

													};
													$scope.nurseShiftList.push(data);
												}
												
												
												$scope.isAdded = 'Y';
												var nurseAllocAndShiftObj={};
												nurseAllocAndShiftObj.nurseList=$scope.nurseList;
												nurseAllocAndShiftObj.nurseId=info.nurseId;
												nurseAllocAndShiftObj.wardId=info.wardId;
												nurseAllocAndShiftObj.nurseShiftList=$scope.nurseShiftList;
												$scope.nurseAllocAndShiftList.push(nurseAllocAndShiftObj);
												var length=0;
												var listHeader=[];
												angular.forEach($scope.nurseAllocAndShiftList,function(value,index){
													if(value.nurseShiftList.length>length){
														length=value.nurseShiftList.length;
														listHeader=value.nurseShiftList;
													}
												});
												$scope.shiftHeaderList=listHeader;
												
												var data = {
														unitId : $scope.unitId,
														orgId : $scope.organizationId,
														shiftList : nurseAllocAndShiftObj.nurseShiftList,
														nurseId : info.nurseId
													};
													$scope.listBed = [];
													var URI3 = BASE_URL + ROOT_URL
															+ "/api/global/getAssignedBedList";
													$rootScope.startLoader();
													 GenericService.serviceAction("POST", URI3, data).then(
																	function(response) {
																		//already allocated bed list
																		$scope.assignedBedList = response.data.listObject;
																		console.log("assigned bed list:",$scope.assignedBedList);
																		  angular.forEach(nurseAllocAndShiftObj.nurseShiftList,function(value,index) {
																			  $scope.listBed=[];
																		   angular.forEach($scope.bedList,function(value1,index1) {
																							var flag = false;
																							angular.forEach($scope.assignedBedList,function(value2,index2) {
																								if (value2.bedId == value1.bedId && value2.shiftDate==value.shiftDate)
																								{
																									flag = true;
																								}
																							});

																							if (flag == false) {
																								var bed = {};
																								bed.id = value1.bedId;
																								bed.label = value1.bedName;
																								$scope.listBed.push(bed);
																							}
																				});
																		   nurseAllocAndShiftObj.nurseShiftList[index].bedIdList = $scope.listBed;
																		});
																		  $rootScope.stopLoader();
																	});
													
											});
									$scope.firstTabLevel = 1;
									$scope.isSetFirstTabLevel(1);
								} catch (e) {
									console.log("Exception-", e);
								}
							}

							// For update
							$scope.updateShift = function() {
								try {
									console.log($scope.nurseShiftList);
									console.log($scope.selectedbedList);
									console.log(bedsIdList);
									if ($scope.popUpFlag == false) {
										console.log("$scope.popUpFlag "
												+ $scope.popUpFlag)
										$rootScope.startLoader();
									}
									var date = new Date();
									$scope.dt = moment(date).format(
											'DD-MM-YYYY HH:mm:ss');

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
									} else {
										$scope.commonObj.bedIdShiftList = $scope.selectedbedList;
										angular.forEach($scope.nurseShiftList,function(value, index) {
															value.shiftDate = value.shiftDate
																	+ " 00:00:00";
														});
										var data = {

											nurseShiftAllocationId : $scope.commonObj.nurseShiftAllocationId,
											orgId : $scope.organizationId,
											unitId : $scope.unitId,
											updatedBy : $scope.userId,
											//createdDate :$scope.dt,
											//updatedDate :$scope.dt,
											status : 'A',
											wardId : $scope.commonObj.wardId,
											//fromDate : moment($scope.commonObj.fromDate).format('DD-MM-YYYY HH:mm:ss'),
											//toDate :  moment($scope.commonObj.toDate).format('DD-MM-YYYY HH:mm:ss'),
											nurseId : $scope.commonObj.nurseId,
											shiftList : $scope.nurseShiftList
										};
										var URI = BASE_URL
												+ ROOT_URL
												+ "/api/global/updateNurseShiftAllocation";
										console.log("Update-URI-", URI);
										console.log(" Update OBJ", data);

										GenericService
												.serviceAction("POST", URI,
														data)
												.then(
														function(response) {

															if (response.data.status == 'success') {
																growl
																		.success(
																				"Roster Updated successfully",
																				{
																					title : 'Success!'
																				});

																$scope.initMaster();
																$scope.nurseShiftList = [];
																$scope.bedIdList = [];
																$scope.isAdded = 'N';
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

							//update status function 
							$scope.updateStatus = function(obj) {
								try {
									var date = new Date();
									$scope.dt = moment(date).format(
											'DD-MM-YYYY HH:mm:ss');
									var data = {
										nurseShiftAllocationId : obj.nurseShiftAllocationId,
										orgId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										//updatedDate :$scope.dt,
										status : obj.status,
										nurseId : obj.nurseId,
										nurseShiftList : obj.shiftList
									};
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/global/updateNurseShiftAllocationDashboardStatus";

									$rootScope.startLoader();
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope
																	.stopLoader();
															growl
																	.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$scope.initMaster();

														} else if (response.data.status == 'error') {
															$rootScope
																	.stopLoader();
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
														}
													});
								} catch (e) {
									console.log("Exception", e);
								}
							}
							
							$scope.checkNurseExist=function(nurseAlloObj){
								var nurseList=$.grep($scope.nurseAllocAndShiftList, function (nurseObj) {
							        return (nurseAlloObj.nurseId === nurseObj.nurseId);
							    });
								if(nurseList.length>1){
									 growl.warning(
												'Nurse already selected',
												{
													title : 'Warning!'
												});
									nurseAlloObj.nurseId="";
									return false;
								}
							}
							
							$scope.getBedListByWardId=function(wardId){
								var data1 = {
									unitId : $scope.unitId,
									orgId : $scope.organizationId,
									wardId : wardId

								};
								var URI = BASE_URL + ROOT_URL + "/api/global/getbedlistbywardId";
								return GenericService.serviceAction("POST", URI, data1).then(function(response1) {
													$scope.bedList = [];
													$scope.bedIdList = [];

													if (response1.data.status == "success") {
														$scope.bedList = response1.data.listObject;
														angular.forEach($scope.bedList,function(value,index) {
																			var bed = {};
																			bed.id = value.bedId;
																			bed.label = value.bedName;
																			$scope.bedIdList.push(bed);

														});
														console.log($scope.bedIdList);
													}
													return response1;
								  });
							
							}
							
							 $scope.dropDownSetting1 = {
								        enableSearch: true,
								        scrollableHeight: '200px', 
								        scrollable: true
								    };
						} ]);
