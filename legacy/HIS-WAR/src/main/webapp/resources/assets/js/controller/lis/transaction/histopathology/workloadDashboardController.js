
/**
 * @author Suraj kumbhoje
 */

angular
	.module('myApp')
	.controller(
		"workloadDashboardController",
		[
			'$scope',
			'$rootScope',
			'$state',
			'$cookies',
			'$filter',
			'GenericService',
			'PagerService',
			'promiseFactoryWithObject',
			'$sessionStorage',
			'growl',
			'BillingGenericService',
			'reportService',
			'$timeout',
			function($scope, $rootScope, $state, $cookies, $filter,
					 GenericService, PagerService, promiseFactoryWithObject,
					 $sessionStorage, growl,BillingGenericService,reportService,$timeout) {

				$rootScope.loginpage = true;
				$scope.LISDynamicLabel = "workload Dashboard Controller";

				$scope.firstLevel = true;
				$scope.secondLevel = true;
				$scope.thirdLevel = false;

				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 50;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;
				$scope.storeListCount;
				$scope.labResultTestDtls;

				/*** Check user is Loggedin */
				var cookieObject,cookieObjectAppConfig;
				[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;

				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
				if(cookieObjectAppConfig == undefined){
					$state.go('login');
					return;
				}
				else
				{
					console.log('cookieObjectAppConfig',cookieObjectAppConfig);

				}
				$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
				$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
				$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
				$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
				$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
				$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
				$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
				$scope.isGstVisible = cookieObjectAppConfig.isGstVisible;
				var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
				if (cookieObjectAppConfigSecond != undefined) {
				$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
				$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
				}
				$scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
				if($scope.isBillManipulationAllowed == true){
					$scope.filterDummyService = 1;
				}else{
					$scope.filterDummyService = 2;
				}
				/*** Lab Configuration */
				var labConfig  =    $scope.checkLabConfig();
				$scope.deptId = labConfig.labSpecialityId;
				$scope.subDeptId = 0;
				$scope.manualSampleCollectionTime = labConfig.manualSampleCollectionTime;
				$scope.departmentList= [];
				$scope.listLabDashBoardDto=[];
				$scope.visityTypeList = [];
				$scope.specimanTypeList =[];
				$scope.testList = [];

				$scope.selectedVisitType = [];
				$scope.selectedspecimanTypeList = [];
				$scope.selectedtestList = [];
				$scope.selectedDepartMentList = [];


				/**DatePicker Code  */

				$scope.fromDate = moment().startOf('day').toDate();
				$scope.toDate = moment().startOf('day').toDate();
				$scope.completed='N';
				$scope.pending = 'N';
				$scope.outsourced = 'N';

				$scope.dateOptions =
					{
						formatYear : 'yyyy',
						showWeeks : false,
						maxDate : new Date(),
					};

				$scope.open = function() {
					$scope.popup.opened = true;
				};

				$scope.popup =
					{
						opened : false
					};

				$scope.popup1 =
					{
						opened : false
					};

				$scope.open1 = function()
				{
					$scope.popup1.opened = true;
				};



				$scope.LabDashBoardDto={

					"currentStatus":"",
					"isOutSource":"",
					"sendToDept":"",
					"statusId":"",
					"isArrival":"",
					"deptId":$scope.deptId,
					"orgId":$scope.orgId,
					"orgUnitId":$scope.orgUnitId,
					"createdBy":$scope.userId,
					"createdDate":"",
					"updatedBy":$scope.userId,
				}



				$scope.setNoOfRecords = function() {
					$scope.initLabDashBoard($scope.orgId,
						$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
						$scope.noOfRecordsPerPage);
				};

				$scope.initLabDashBoard = function(orgId,
												   orgUnitId,deptId,subDeptId, offset, noOfRecordsPerPage) {
					try {
						$rootScope.startLoader();
						var data = {
							"orgId":orgId,
							"orgUnitId":orgUnitId,
							"deptId":deptId,
							"offset":offset,
							"recordPerPage":noOfRecordsPerPage		,
							"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"patientId" :  $scope.selectedPatient,
							"outsourced" : $scope.outsourced,
							"outSource" : $scope.pending,
							"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
							"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
							"completed" : $scope.completed,
							"sampleStatusId" : $scope.statusId,
							"labSampleDtlsId" : $scope.labSampleDtlsId,
							"visitTypes" :$scope.selectedVisitType,
							"specimanTypes":$scope.selectedspecimanTypeList,
							"testTypes" : $scope.selectedtestList,
							"subDepts" : $scope.selectedDepartMentList,
							"searchField":$scope.searchKeyword
						};
						var data1 = {
							"orgId":orgId,
							"orgUnitId":orgUnitId,
							"deptId":deptId,
							"offset":offset,
							"noOfRecordsPerPage":noOfRecordsPerPage,
							"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"searchField":$scope.searchKeyword
						};
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
							: 50;
						var URI1 = BASE_URL
							+ROOT_URL + LIS
							//+ LAB_DASHBORD_LIST;
							+ LAB_DASHBORD_SEARCH;

						var URI2 = BASE_URL
							+ROOT_URL + LIS
							+ LAB_DASHBORD_LIST_COUNT;

						console.log("URI1", URI1);
						console.log("URI2", URI2);
						promiseFactoryWithObject
							.setPromisesWithObject(URI1, URI2, METHOD_POST,
								METHOD_POST,data,data1)
							.then(
								function(response) {
									$rootScope.stopLoader();
									$scope.labDashBoardList = response[0].listObject;
									$scope.commonListCount = response[1].object;
									$scope.labDashBoardListForFiltter  = $scope.labDashBoardList;
									$scope.filterByDummyServices();
									$scope.setPage(1,false);

								});
					} catch (e) {
						console.log(e.message);
					}
				}

				$scope.initLabDashBoard($scope.orgId,
					$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
					$scope.noOfRecordsPerPage);

				$scope.clearFields = function()
				{
					$scope.patientId = undefined;
					$scope.selectedPatient = null;
					$scope.selectedVisitType = [];
					$scope.orderStatusId = '';
					$scope.priorityId = '';
					$scope.uhid = '';
					$scope.selectedSampleNo='';
					$scope.selectedtestList = [];
					$scope.selectedDepartMentList = [];
					$scope.selectedspecimanTypeList = [];
					$scope.selectedspecimanTypeList = [];
					$scope.fromDate = moment().toDate();
					$scope.toDate = moment().toDate();
					$scope.outsourced ='';
					$scope.pending  ='';
					$scope.completed ='';
					$scope.statusId ='';
					$scope.selectedCompleted =false;
					$scope.selectedPending = false;
					$scope.selectedOutsourced = false;
					$scope.labSampleDtlsId ='';
					$scope.selectedCompleted = '';
					$scope.doctorName='';
					$scope.referDoctorId='';
					$scope.paymentEntitlementId="";
					$scope.selectedCompanyList=[];
					$scope.selectedassociateCompanyMasterList=[];
					$scope.initLabDashBoard($scope.orgId,
						$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
						$scope.noOfRecordsPerPage);



				};

				$scope.getLabDashBoardList = function(orgId,
													  orgUnitId,deptId,subDeptId,offset,noOfRecordsPerPage) {
					try {
						$rootScope.startLoader();
						var data = {
							"orgId":orgId,
							"orgUnitId":orgUnitId,
							"deptId":deptId,
							"offset":offset,
							"recordPerPage":noOfRecordsPerPage,
							"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"patientId" :  $scope.selectedPatient,
							"outsourced" : $scope.outsourced,
							"outSource" : $scope.pending,
							"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
							"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
							"completed" : $scope.completed,
							"sampleStatusId" : $scope.statusId,
							"labSampleDtlsId" : $scope.labSampleDtlsId,
							"visitTypes" :$scope.selectedVisitType,
							"specimanTypes":$scope.selectedspecimanTypeList,
							"testTypes" : $scope.selectedtestList,
							"subDepts" : $scope.selectedDepartMentList,
							"searchField":$scope.searchKeyword
						};
						var URI = BASE_URL+ROOT_URL+ LIS
							+ LAB_DASHBORD_SEARCH;
						//+ LAB_DASHBORD_LIST;
						console.log("URI", URI);

						GenericService
							.serviceAction(METHOD_POST, URI, data)
							.then(
								function(response) {
									$rootScope.stopLoader();
									$scope.labDashBoardList  = response.data.listObject;
									$scope.labDashBoardListForFiltter  = $scope.labDashBoardList;
									$scope.filterByDummyServices();
								});
					} catch (e) {
						console.log(e.message)
					}

				}

				$scope.filterByDummyServices= function()
				{
					$scope.labDashBoardList  =[]
					if($scope.filterDummyService==1)//all
					{
						$scope.labDashBoardList = angular.copy($scope.labDashBoardListForFiltter);

					}else if($scope.filterDummyService==2)//real
					{
						angular.forEach($scope.labDashBoardListForFiltter, function (value, key)
						{
							if(value.isDummyService==false)
								$scope.labDashBoardList.push(value);
						});


					}else if($scope.filterDummyService==3 )//dummy
					{
						angular.forEach($scope.labDashBoardListForFiltter, function (value, key)
						{
							if(value.isDummyService==true)
								$scope.labDashBoardList.push(value);
						});

					}
				}


				/*
                 *  Get Sample Details List
                 */
				$scope.getSampleDtlsList = function(labSampleDtlsId) {
					try {
						var data = "";
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL
							+ LIS
							+ GET_LAB_SAMPLE_DETAILS
							+ S + labSampleDtlsId
							+ S + $scope.orgId
							+ S + $scope.orgUnitId;
						console.log("URI", URI);
						GenericService
							.serviceAction("GET", URI, data)
							.then(
								function(response) {

									if(response.data.status=='success')
									{
										$rootScope.stopLoader();
										$("#detailsModal").modal('show');
										$scope.sampleDtlsList  = response.data.listObject;
									}
									else if(response.data.status=='error')
									{
										$rootScope.stopLoader();
									}

								});
					} catch (e) {
						console.log(e.message)
					}

				}



				/*====================================SEARCH DETAILS=====================================*/

				$scope.dropDownSetting = {
					scrollableHeight: '100px',
					scrollable: true
				};

				$scope.multiselectObj = {
					"id":"",
					"label":""
				}

				/** Service Autocomplete List * */
				$scope.getMicroPatientList = function(searchKeyword)
				{
					if(searchKeyword.length == 3)
					{
						try
						{
							var data = {
								"orgId":$scope.orgId,
								"orgUnitId":$scope.orgUnitId,
								"deptId":$scope.deptId,
								"subDeptId":$scope.subDeptId,
								"searchKeyword":searchKeyword,
							}

							var URI = BASE_URL
								+ ROOT_URL
								+ LIS
								+ SEARCH_DOCTOR_DETAILS
							console.log("URI", URI);
							return GenericService.serviceAction("POST", URI,
								data).then(function(response) {
								console.log("response.data.listObject",response.data.listObject);
								$scope.patientListNew =response.data.listObject;
								return response.data.listObject;
							});
						} catch (e) {
							console.log(e.message)
						}
					}	else {
						var filteredList = $filter('filter')($scope.patientListNew, { $: searchKeyword });
						if (filteredList.length === 0) {
							$scope.filteredPatientList = null;
						} else {
							$scope.filteredPatientList = filteredList;
						}
						return $scope.filteredPatientList;
					}
				};
				$scope.selectPatient = function($item, $model,$label)
				{
					$scope.selectedPatient = $item.id;
					// $scope.PatientName= $item.firstName +"  "+$item.lastName;
					$scope.uhid=$item.uhid;
				}




				/*get Visi tType List*/
				$scope.getVisitTypeList = function()
				{
					try
					{
						var data = {
							"orgId":$scope.orgId,
							"orgUnitId":$scope.orgUnitId,
							"deptId":$scope.deptId,
							"subDeptId":$scope.subDeptId,
							"searchKeyword":"",
						}

						var URI = BASE_URL
							+ ROOT_URL
							+ LIS_HISTOPATHOLOGY
							+ HISTO_MICRO_VISIT_LIST
						console.log("URI", URI);
						GenericService.serviceAction("POST", URI,
							data).then(function(response) {
							console.log("response.data.listObject",response.data.listObject);
							angular.forEach(response.data.listObject, function(value, key) {
								var multiselectObj = angular.copy($scope.multiselectObj);
								multiselectObj.id = value.id;
								multiselectObj.label = 	value.label;
								$scope.visityTypeList.push(multiselectObj);
							});


						});
					} catch (e) {
						console.log(e.message)
					}
				}

				$scope.getVisitTypeList();


				$scope.getSpecimanList = function()
				{
					try
					{
						var data = {
							"orgId":$scope.orgId,
							"orgUnitId":$scope.orgUnitId,
							"deptId":$scope.deptId,
							"subDeptId":$scope.subDeptId,
							"searchKeyword":"",
						}

						var URI = BASE_URL
							+ ROOT_URL
							+ LIS_HISTOPATHOLOGY
							+ HISTO_MICRO_SPECIMAN_LIST
						console.log("URI", URI);
						GenericService.serviceAction("POST", URI,
							data).then(function(response)
						{
							console.log("response.data.listObject",response.data.listObject);
							$scope.specimanTypeList =  response.data.listObject;
						});
					} catch (e) {
						console.log(e.message)
					}
				}
				$scope.getSpecimanList();

				$scope.getTestList = function()
				{
					try
					{
						var data = {
							"orgId":$scope.orgId,
							"orgUnitId":$scope.orgUnitId,
							"deptId":$scope.deptId,
							"subDeptId":$scope.subDeptId,
							"searchKeyword":"",
						}

						var URI = BASE_URL
							+ ROOT_URL
							+ LIS_HISTOPATHOLOGY
							+ HISTO_MICRO_TEST_LIST
						console.log("URI", URI);
						GenericService.serviceAction("POST", URI,
							data).then(function(response) {
							console.log("response.data.listObject",response.data.listObject);
							$scope.testList =  response.data.listObject;
						});
					} catch (e) {
						console.log(e.message)
					}
				}
				$scope.getTestList();

				$scope.getDepartMentList = function(orgId,orgUnitId) {
					try {
						var data = "";
						var URI = BASE_URL + ROOT_URL
							+ LIS_COMMON + GET_DEPARMENT_LIST
							+ S + orgId + S + orgUnitId;
						console.log("URI", URI);
						GenericService.serviceAction("GET", URI, data)
							.then(function(response) {
								angular.forEach(response.data.listObject, function(value, key) {
									var multiselectObj = angular.copy($scope.multiselectObj);
									multiselectObj.id = value.id;
									multiselectObj.label = 	value.name;
									$scope.departmentList.push(multiselectObj);
								});
							});
					} catch (e) {
						console.log(e.message)
					}
				}
				$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);


				$scope.getSampleStatusList = function(orgId) {
					try {
						var data = "";
						var URI = BASE_URL + ROOT_URL
							+ LIS_COMMON + GET_SAMPLE_STATUS_DETAILS
							+ S + orgId;
						console.log("URI", URI);
						GenericService.serviceAction("GET", URI, data)
							.then(function(response) {
								$scope.sampleStatusList = response.data.listObject;
							});
					} catch (e) {
						console.log(e.message)
					}
				}
				$scope.getSampleStatusList($scope.orgId);


				/*get Sample Number List*/
				$scope.getSampleNoList = function(searchKeyword)
				{
					try
					{
						var data = {
							"orgId":$scope.orgId,
							"orgUnitId":$scope.orgUnitId,
							"searchKeyword":searchKeyword,
						}

						var URI = BASE_URL
							+ ROOT_URL
							+ LIS
							+ SEARCH_SAMPLE_NO_DETAILS
						console.log("URI", URI);

						return GenericService.serviceAction("POST", URI,
							data).then(function(response) {
							console.log("response.data.listObject",response.data.listObject);
							return response.data.listObject;
						});

					} catch (e) {
						console.log(e.message)
					}
				}


				$scope.selectSampleNo= function($item, $model,$label)
				{
					$scope.selectedSampleNo = $item.sampleNo;
					$scope.labSampleDtlsId = $item.labSampleDtlsId;

				}

				$scope.saveDetails = function() {


					$rootScope.startLoader();

					$scope.frozenSectObj.ackRemark = $scope.ackRemark;
					$scope.listLabDashBoardDto.push($scope.frozenSectObj);
					var URI = BASE_URL + ROOT_URL
						+ LIS + LAB_DASHBORD_SEARCH;

					try {
						console.log("$scope.listLabDashBoardDto", URI);

						GenericService
							.serviceAction("POST", URI,
								$scope.listLabDashBoardDto)
							.then(
								function(response) {
									if (response.data.status == 'success') {
										$rootScope.stopLoader();
										$scope.initLabDashBoard($scope.orgId,
											$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,
											$scope.noOfRecordsPerPage);
										growl.success(response.data.message,
											{
												title : 'Success!'
											});
										$scope
											.$broadcast('show-errors-reset');

									} else if (response.data.status == 'error') {
										growl
											.error(
												response.data.message,
												{
													title : 'Error!'
												});
									}
								});
					} catch (e) {
						console.log("Exception", e.message);
					}
				}

				$scope.pager = {};
				$scope.page;
				$scope.setPage = function(page, flag)
				{
					if (page < 1 )
					{
						return;
					}
					$scope.pager = PagerService.GetPager(
						$scope.commonListCount, page,
						$scope.noOfRecordsPerPage);
					if (flag) {
						$scope.getLabDashBoardList(
							$scope.orgId, $scope.orgUnitId,$scope.deptId, $scope.subDeptId,
							$scope.pager.startIndex,
							$scope.pager.pageSize);
					}
				}

				/** code for checkOutsourced  checkboxes */
				$scope.checkOutsourced  = function() {
					if ($scope.selectedOutsourced) {
						$scope.selectedOutsourced = true;
						$scope.outsourced = 'Y';
					} else {
						$scope.selectedOutsourced = false;
						$scope.outsourced = 'N';
					}

				}

				/** code for checkPending checkboxes */
				$scope.checkPending  = function() {
					if ($scope.selectedPending) {
						$scope.selectedPending = true;
						$scope.pending  = 'Y';
					} else {
						$scope.selectedPending = false;
						$scope.pending = 'N';
					}

				}

				/** code for checkCompleted checkboxes */
				$scope.checkCompleted  = function() {
					if ($scope.selectedCompleted) {
						$scope.selectedCompleted = true;
						$scope.completed='Y';
					} else {
						$scope.selectedCompleted = false;
						$scope.completed='N';
					}

				}

				$scope.searchWorkLoadDashBoardList = function()
				{
					$rootScope.startLoader();
					try
					{
						if($scope.paymentEntitlementId != undefined && $scope.paymentEntitlementId != null || $scope.paymentEntitlementId != ""){

							if( $scope.paymentEntitlementId == 2 && ($scope.selectedassociateCompanyMasterList == null || $scope.selectedassociateCompanyMasterList == undefined
								|| $scope.selectedassociateCompanyMasterList.length == 0)){
								$rootScope.stopLoader();
								growl.warning('Please Select Insurance',{
									title : 'warning'
								});
								return false;
							}

							if( $scope.paymentEntitlementId == 3 && ($scope.selectedCompanyList == null || $scope.selectedCompanyList == undefined
								|| $scope.selectedCompanyList.length == 0)){
								$rootScope.stopLoader();
								growl.warning('Please Select Company',{
									title : 'warning'
								});
								return false;
							}
						}
						var ids = [];
						if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 2){
							//insurance
							if($scope.selectedassociateCompanyMasterList!=null && $scope.selectedassociateCompanyMasterList!=undefined && $scope.selectedassociateCompanyMasterList.length>0){
								angular.forEach($scope.selectedassociateCompanyMasterList,function(val,idx){
									ids.push(val);
								});
							}

						}else if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 3){
							//company
							if($scope.selectedCompanyList!=null && $scope.selectedCompanyList!=undefined && $scope.selectedCompanyList.length>0){
								angular.forEach($scope.selectedCompanyList,function(val,idx){
									ids.push(val);
								});
							}
						}
						if($scope.doctorName == undefined || $scope.doctorName == null || $scope.doctorName ==""){
							$scope.referDoctorId = null;
						}
						var searchObj = {

							"orgId" : $scope.orgId,
							"orgUnitId" :$scope.orgUnitId,
							"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"patientId" :  $scope.selectedPatient,
							"outsourced" : $scope.outsourced,
							"outSource" : $scope.pending,
							"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
							"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
							"completed" : $scope.completed,
							"sampleStatusId" : $scope.statusId,
							"labSampleDtlsId" : $scope.labSampleDtlsId,
							"visitTypes" :$scope.selectedVisitType,
							"specimanTypes":$scope.selectedspecimanTypeList,
							"testTypes" : $scope.selectedtestList,
							"subDepts" : $scope.selectedDepartMentList,
							"deptId":$scope.deptId,
							"offset":$scope.offset,
							"recordPerPage":$scope.noOfRecordsPerPage,
							"searchField":$scope.searchKeyword,
							"paymentEntitlementId":parseInt($scope.paymentEntitlementId),
							"ids" : ids	,
							"referralDocId":$scope.referDoctorId
						}


						var isValidDate = $rootScope.isValidDate(searchObj.fromDt,searchObj.toDt);
						if(!isValidDate){
							$rootScope.stopLoader();
							growl.warning("From date canot be greater than to Date ",
								{
									title : 'warning!'
								});
							return false;
						}


						console.log("labDashBoardList",JSON.stringify(searchObj));
						var URI = BASE_URL
							+ ROOT_URL
							+ LIS
							+ LAB_DASHBORD_SEARCH
						console.log("URI", URI);

						var data1 = {
							"orgId":$scope.orgId,
							"orgUnitId":$scope.orgUnitId,
							"deptId":$scope.deptId,
							"offset":$scope.offset,
							"noOfRecordsPerPage":$scope.noOfRecordsPerPage,
							"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							"searchField":$scope.searchKeyword
						};

						var URI2 = BASE_URL
							+ROOT_URL + LIS
							+ LAB_DASHBORD_LIST_COUNT;

						console.log("URI2", URI2);
						promiseFactoryWithObject
							.setPromisesWithObject(URI, URI2, METHOD_POST,
								METHOD_POST,searchObj,data1)
							.then(
								function(response) {
									$rootScope.stopLoader();
									$scope.labDashBoardList = response[0].listObject;
									$scope.commonListCount = response[1].object;
									$scope.labDashBoardListForFiltter  = $scope.labDashBoardList;
									$scope.filterByDummyServices();
									$scope.setPage(1,false);
								});


					} catch (e) {
						console.log(e.message)
					}
				}

				$scope.workloadDashboardUniversalSearch = function(searchKeyword)
				{
					if(searchKeyword.length >= 3){
						try
						{
							if($scope.paymentEntitlementId != undefined && $scope.paymentEntitlementId != null || $scope.paymentEntitlementId != ""){

								if( $scope.paymentEntitlementId == 2 && ($scope.selectedassociateCompanyMasterList == null || $scope.selectedassociateCompanyMasterList == undefined
									|| $scope.selectedassociateCompanyMasterList.length == 0)){
									$rootScope.stopLoader();
									growl.warning('Please Select Insurance',{
										title : 'warning'
									});
									return false;
								}

								if( $scope.paymentEntitlementId == 3 && ($scope.selectedCompanyList == null || $scope.selectedCompanyList == undefined
									|| $scope.selectedCompanyList.length == 0)){
									$rootScope.stopLoader();
									growl.warning('Please Select Company',{
										title : 'warning'
									});
									return false;
								}
							}
							var ids = [];
							if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 2){
								//insurance
								if($scope.selectedassociateCompanyMasterList!=null && $scope.selectedassociateCompanyMasterList!=undefined && $scope.selectedassociateCompanyMasterList.length>0){
									angular.forEach($scope.selectedassociateCompanyMasterList,function(val,idx){
										ids.push(val);
									});
								}

							}else if($scope.paymentEntitlementId!="" && $scope.paymentEntitlementId!=undefined && $scope.paymentEntitlementId!=null && $scope.paymentEntitlementId == 3){
								//company
								if($scope.selectedCompanyList!=null && $scope.selectedCompanyList!=undefined && $scope.selectedCompanyList.length>0){
									angular.forEach($scope.selectedCompanyList,function(val,idx){
										ids.push(val);
									});
								}
							}
							if($scope.doctorName == undefined || $scope.doctorName == null || $scope.doctorName ==""){
								$scope.referDoctorId = null;
							}
							$rootScope.startLoader();
							var data = {
								"orgId":$scope.orgId,
								"orgUnitId":$scope.orgUnitId,
								"deptId":$scope.deptId,
								"offset":$scope.offset,
								"recordPerPage":$scope.noOfRecordsPerPage		,
								"fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
								"toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
								"patientId" :  $scope.selectedPatient,
								"outsourced" : $scope.outsourced,
								"outSource" : $scope.pending,
								"uhidNumber" : ($scope.uhid==undefined ||$scope.uhid== null) ?"":$scope.uhid,
								"sampleNo" : ($scope.selectedSampleNo == undefined || $scope.selectedSampleNo==null)?"":$scope.selectedSampleNo,
								"completed" : $scope.completed,
								"sampleStatusId" : $scope.statusId,
								"labSampleDtlsId" : $scope.labSampleDtlsId,
								"visitTypes" :$scope.selectedVisitType,
								"specimanTypes":$scope.selectedspecimanTypeList,
								"testTypes" : $scope.selectedtestList,
								"subDepts" : $scope.selectedDepartMentList,
								"searchField":searchKeyword,
								"paymentEntitlementId":parseInt($scope.paymentEntitlementId),
								"ids" : ids	,
								"referralDocId":$scope.referDoctorId
							};
							var URI = BASE_URL
								+ROOT_URL + LIS
								+ "/Dashboard/UniversalSearch";
							GenericService.serviceAction("POST", URI, data).then(
								function(response) {
									$rootScope.stopLoader();
									$scope.labDashBoardList = response.data.listObject;
									$scope.commonListCount = response.data.object;
									$scope.labDashBoardListForFiltter  = $scope.labDashBoardList;
									$scope.filterByDummyServices();
									$scope.setPage(1,false);
								});

						} catch (e) {
							console.log(e.message)
						}
					}else  if(searchKeyword.length == 0){
						$scope.initLabDashBoard($scope.orgId,$scope.orgUnitId,$scope.deptId,$scope.subDeptId,$scope.offset,$scope.noOfRecordsPerPage);
					}
				}

				/**====================================================================================*/

				$scope.getPendingServicesByEncounterId = function(){
					$rootScope.startLoader();

					var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};

					var ReasonURI = BASE_URL + ROOT_URL
						+ GETACTIVEREASONLIST;
					GenericService.serviceAction("POST", ReasonURI, commonObj)
						.then(
							function(response) {
								$scope.reasonList = [];
								if (response.data.status == "success") {
									$scope.reasonList = response.data.listObject;
								}
							});


					$scope.patientDetailsList = [];
					$scope.patientDetails = {}
					var URI = BASE_URL + ROOT_URL + "/api/opd/op/pending/services/encounter/"+$scope.encounterId;
					BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							$scope.pendingServicesList = [];
							$scope.pendingServicesList = response.data.listObject;
							if($scope.pendingServicesList.length > 0)
							{
								$scope.billDisabled = true;
								$scope.pendingCountN = $scope.pendingServicesList.length;
							}
							else
							{
								$scope.billDisabled = false;
							}
						}
					});
					$rootScope.stopLoader();
				}

				$scope.getAutoRenderedServicesByPatient=function(){

					$rootScope.startLoaderPopup();
					$scope.pendingServiceListPrePost = [];

					var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};

					var ReasonURI = BASE_URL + ROOT_URL
						+ GETACTIVEREASONLIST;
					GenericService.serviceAction("POST", ReasonURI, commonObj)
						.then(
							function(response) {
								$scope.reasonList = [];
								if (response.data.status == "success") {
									$scope.reasonList = response.data.listObject;
								}
							});


					var URI = null;

					if($scope.isPreOrPostBilling=='Y')
						URI = BASE_URL + ROOT_URL + "/api/billing/service/preBilling/orderedServices";
					else
						URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";

					var obj={
						"visitTypeId":1,
						"encounterId":$scope.encounterId,
						"billId" : $scope.billId
					}
					console.log("AUTORENDER",obj);
					//return false;
					BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
					{

						if(response.data.status == "success")
						{
							var tempAutoRendServList = [];
							angular.forEach(response.data.listObject,function(value,index)
							{
								if(value.isPackage==false)
								{
									var found = value.orderDetailsPackageMapperDtosList.find(element => element.isPackageService == 'Y');
									if(found==undefined){
										value.packageMasterId = value.packageId;
										value.isServices = false;
										tempAutoRendServList.push(value);
									}
								}
								var data = value.orderDetailsPayeeMapperDtosList.find(element => element.isInvoiceGenerated == 'Y');
								value.isInvoiceGenerated = data!=null ? data.isInvoiceGenerated : 'N';
							});
							$scope.pendingServiceListPrePost = tempAutoRendServList;
							if($scope.pendingServiceListPrePost.length > 0){
								$scope.addToBillFlag = false;
								$scope.pendingCountY = $scope.pendingServiceListPrePost.length;
							}

							$rootScope.stopLoaderPopup();
						}
						else
						{
							$rootScope.stopLoaderPopup();
						}
					});
				}
				$scope.amountPaidflag = true;
				$scope.selectedUhid = "";
				$scope.selectedPatientId = "";
				$scope.amountPaid ="";

				$scope.getSampleBillDtlsList = function(bill){
					console.log("info",bill);


					if(bill.intermediateSelfDue!=null &&  bill.intermediateSelfDue>0){

						$scope.billDisabled=true;

						growl.warning("Self content of Intermediate bill(s) of this admission is not yet settled ..!!",{
							title : WARNING_MSG
						});


					}

					$scope.billDetailsPatientInfo = {};
					$scope.billDetailsPatientInfo.patientName = bill.patientDetails;
					$scope.billDetailsPatientInfo.uhidNumber=bill.uhid;
					$scope.billDetailsPatientInfo.birthDate=bill.birthDate;
					$scope.billDetailsPatientInfo.ageString=bill.ageString;
					$scope.billDetailsPatientInfo.imagePath=bill.imagePath;
					$scope.billDetailsPatientInfo.genderId=bill.genderId;

					if(bill.mobileNo!= null){
						$scope.mobileNumber = parseInt(bill.mobileNo);
					}

					$scope.setFirstTabLevel(1);
					$scope.excessAmount = parseFloat(0).toFixed(2);
					$scope.transferToActiveDeposit = 'N'
					$scope.amountPaid = bill.amountPaid;
					$scope.selectedUhid = bill.uhid;
					$scope.selectedPatientId = bill.patientId;
					$scope.rountOffString = '';
					console.log("BILL",bill);
					$scope.validatTextFieldFlag = true;
					//alert("bill.outstanding"+bill.outstanding);
					//$scope.billDisabled = true;
					//$scope.payeeId = "";
					$scope.billServicesList=[];
					$scope.paymentReceiptsDetailsList=[];
					$scope.listPaymentDetails = [];
					$scope.payeeList = [];
					$scope.totalAmtQuaBase = 0;
					$scope.totalAmtConcession = 0;
					$scope.totalAmtDiscount = 0;
					$scope.totalAmtGst = 0;
					$scope.totalNetPayableAmt = 0;
					$scope.payeeDueAmtFinal = 0;
					$scope.payeeDueAmtStatic = 0;
					$scope.patientIdForReport = bill.patientId;
					$scope.visitTypeId =bill.visitTypeId;
					$scope.referalDocId = bill.referalDocId;
					$scope.consumedAmt = parseFloat(0);
					$scope.payeeId = $scope.selfPayeeIdCookies;
					$scope.isPaidAppointment = bill.isPaidAppointment!=null ? bill.isPaidAppointment : 'N';
					$scope.billDisabled = (bill.billStatusId == 3 || (bill.intermediateSelfDue!=null &&  bill.intermediateSelfDue>0)) ? true : false;
					$scope.billId = bill.billingMasterId;
					$scope.isInterimOrFinalBill = bill.isInterimOrFinalBill!=null ? bill.isInterimOrFinalBill : 'N' ;
					$scope.isNursingAssessmentRequired = bill.isNursingAssessmentRequired!=null ? bill.isNursingAssessmentRequired : 'N';
					$scope.disallowedAmount = bill.disallowedAmount;
					$scope.disallowedAgainstInvoiceDtlsId = bill.disallowedAgainstInvoiceDtlsId;
					$scope.doctorId = bill.doctorId;
					$scope.totalAmount = bill.netAmount;
					$scope.dueAmount = bill.outstanding;
					$scope.oldDueAmount =bill.outstanding;
					$scope.billDueAmount = bill.outstanding;
					$scope.totalSelfPayable = bill.totalSelfPayable;
					$scope.totalCreditPayable=bill.totalCreditPayable;
					$scope.currentBill = bill;
					$scope.admissionId = bill.visitTypeId != 1 ? bill.visitId : null;
					$scope.isFinancialClearance = false;
					$('#isFinancialClearance').prop('checked',false);
					//Vivek added
					$scope.encounterId = bill.visitId;
					$scope.appointmentId = bill.appointmentId;
					//End Vivek Added

					$scope.pendingCountY = 0;
					$scope.pendingCountN = 0;
					if($scope.visitTypeId == 1 && $scope.isPreOrPostBilling == 'N'){
						$scope.getPendingServicesByEncounterId();
					}
					if($scope.visitTypeId == 1 && $scope.isPreOrPostBilling == 'Y'){
						$scope.getAutoRenderedServicesByPatient();
					}


					//get payer details
//									$scope.initPaymentDetails();
					$scope.mpesaObject={};
//									$scope.getLoyaltyCardList($scope.selectedPatientId);
					var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+bill.billingMasterId;
					BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
							$scope.payeeList = response.data.listObject;
							$scope.billServicesList = response.data.object.listObject;
							$scope.paymentReceiptsDetailsList=response.data.object.object;
							var totalAmtQuaBase = parseFloat(0);
							var totalAmtConcession = parseFloat(0);
							var totalAmtDiscount = parseFloat(0);
							var totalAmtGst = parseFloat(0);
							var totalNetPayableAmt = parseFloat(0);
							var discountRoundOff = parseFloat(0);
							var totalSelfPayable = parseFloat(0);
							var totalCreditPayable = parseFloat(0);
							var totalPackageConsumptionAmt = parseFloat(0);


							angular.forEach($scope.billServicesList,function(value,key)
							{
								angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
								{
									if(value1.isPackageService=='N')
									{

										if(value.packageConsumptionAmt == 0){
											totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
										}else{
											totalAmtQuaBase = totalAmtQuaBase + parseFloat(value1.serviceAmount);
										}

										totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
										totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
										totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
										totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
										totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
										totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
										discountRoundOff = parseFloat(value.roundOff);
										totalPackageConsumptionAmt = totalPackageConsumptionAmt + parseFloat(value.packageConsumptionAmt);
									}
								});

							});

							$scope.totalAmtQuaBase =totalAmtQuaBase;
							$scope.totalAmtConcession = totalAmtConcession;
							$scope.totalAmtDiscount =totalAmtDiscount+Number(discountRoundOff);
							$scope.rountOffString = Number(discountRoundOff)!=null ? '(RoundOff : '+discountRoundOff+')' :'';
							$scope.totalAmtGst = (totalAmtGst).toFixed(2);
							$scope.totalNetPayableAmt = totalNetPayableAmt-(Number(discountRoundOff));
							$scope.totalPackageConsumptionAmt = totalPackageConsumptionAmt;

							angular.forEach($scope.payeeList,function(value1,key)
							{
								if(value1.payeeId == $scope.selfPayeeIdCookies)
								{
									$scope.payeeDueAmtFinal = Number(parseFloat(value1.dueAmount).toFixed(2));
									$scope.payeeDueAmtStatic = Number(parseFloat(value1.dueAmount).toFixed(2));
								}

							});

							angular.forEach($scope.payeeList,function(value,key){

								if(value.payeeId==$scope.payeeId && (value.billAmount==0 || value.dueAmount==0))
									$scope.billDisabled = true;
							})
						}
					});

				}
				$scope.firstTabLevel = 1 ;

				$scope.transferToActiveDeposit = "N";

				$scope.setFirstTabLevel= function(newTab)
				{
					$scope.firstTabLevel = newTab;
				};
				$scope.isSetFirstTabLevel = function(tabNum)
				{
					return $scope.firstTabLevel === tabNum;
				};

				$scope.closePopup = function(){
					$scope.billDisabled = false;
					$scope.transferToActiveDeposit = "N";
					this.transferToActiveDeposit = "N";
					$scope.receivedFrom = "";
					$scope.identificationNumber = "";
//									$scope.calculateTotalDueRefAmount();
					$scope.listPaymentDetails = [];
					$scope.mpesaObject={};
//									$scope.searchBills($scope.billSearch);
				}

				$scope.changePayee = function(payeeId){
					$scope.listPaymentDetails = [];
					//alert("bill.outstandingChabge"+$scope.dueAmount);

					$scope.calculateTotalDueRefAmount();
					$scope.payeeId = payeeId;
					if(payeeId > $scope.selfPayeeIdCookies)
					{
						$scope.billDisabled = true;
					}
					else
					{
						$scope.billDisabled = false;
					}
				}

				$scope.rePrintOPDBillReceipt=function(index,isReprint){

					var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/"+isReprint+"/"+$scope.userId+"/"+$scope.paymentReceiptsDetailsList[index].receiptId;
					BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
					{
						$rootScope.stopLoader();
						if (response.data.status == "success")
						{
							var data =  {

								"appName": "HIS",
								"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								"reportDesignFilePath": "OPDPaymentReceipt.rpt",
								"reportFormat": "PDF",
								"exportFileName": "PaymentReciept",
								"reportParams": [
									{
										"paramName": "@BillID",
										"paramType": "Integer",
										"paramValue": $scope.paymentReceiptsDetailsList[index].billingMasterId
									},
									{
										"paramName": "@PaymentID",
										"paramType": "Integer",
										"paramValue": $scope.paymentReceiptsDetailsList[index].receiptId
									},
									{
										"paramName": "@dep_consumption_id",
										"paramType": "Integer",
										"paramValue": ($scope.paymentReceiptsDetailsList[index].depositConsumptionId!=null ? $scope.paymentReceiptsDetailsList[index].depositConsumptionId : 0 )
									},
									{
										"paramName": "@isDuplicate",
										"paramType": "Integer",
										"paramValue": isReprint !=null ? isReprint : 1
									}
								]
							};
							reportService.generate(REPORT_API_URI1,data);
						}
					});

				}

				$scope.printReport = function(labSampleDtlsId){
					var ids=[];
					ids.push(labSampleDtlsId);

					var data =
						{
							"appName": "HIS",
							"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							"reportDesignFilePath": "Multireportprint.rpt",
							"reportFormat": "PDF",
							"exportFileName": "pathologyReport",
							"reportParams": [
								{
									"paramName": "lab_sample_dtls_id",
									"paramType": "String",
									"paramValue": ids.toString()
								},
								{
									"paramName": "isOriginal",
									"paramType": "Integer",
									"paramValue": 1
								},
								{
									"paramName": "isHeader",
									"paramType": "Integer",
									"paramValue": 0
								}
							]
						};
					reportService.generate(REPORT_API_URI1,data);

				}
				$scope.displayInternalNotes=function(info){
					$scope.internalNotes=info.internalNotes;
				}

				$scope.insuranceId= "";
				$scope.companyId="";
				$scope.paymentEntitlementId="";
				$scope.paymentEntitlementList = [];
				$scope.getPaymentEntitlementList = function() {
					var data = "";
					var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.organizationId;
					GenericService.serviceAction("GET", URI, data).then(function(response){
						$scope.paymentEntitlementList= response.data.listObject;
					});
				}
				$scope.getPaymentEntitlementList();

				$scope.selectedCompanyList = [];
				$scope.selectedassociateCompanyMasterList = [];
				$scope.companyMasterList = [];
				$scope.getCompanyMasterList = function(companyTypeId) {
					var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:companyTypeId};
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
					BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							$scope.companyMasterList = [];
							$scope.selectedCompanyList = [];
							$scope.selectedassociateCompanyMasterList = [];
							if(response.data.listObject!=null && response.data.listObject.length>0){
								angular.forEach(response.data.listObject,function(value,index){
									var newArr = {};
									newArr.id = value.id;
									newArr.label = value.name;
									$scope.companyMasterList.push(newArr);
								});
							}
						}
						$rootScope.stopLoader();
					});
				}

				$scope.associateCompanyMasterList = [];
				$scope.getAssociateCompanyMasterList = function() {
					var companyIds = [];
					if($scope.selectedCompanyList!=null && $scope.selectedCompanyList!=undefined && $scope.selectedCompanyList.length>0){
						angular.forEach($scope.selectedCompanyList,function(val,idx){
							companyIds.push(val.id);
						});
					}
					if(companyIds.length>0 && $scope.paymentEntitlementId == 2){
						var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyIds:companyIds};
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMasterByMultipleCompanyIds"
						BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								$scope.associateCompanyMasterList = [];
								$scope.selectedassociateCompanyMasterList = [];
								if(response.data.listObject!=null && response.data.listObject.length>0){
									angular.forEach(response.data.listObject,function(val,idx){
										var newArr = {};
										var newArr = {};
										newArr.id = val.id;
										newArr.label = val.name;
										$scope.associateCompanyMasterList.push(newArr);
									});
								}
							}
							$rootScope.stopLoader();
						});
					}

				}

				// Search Surgen with Typehead
				$scope.referDoctorId="";
				$scope.doctorName="";
				$scope.searchGroupSurgen = function(keyword) {

					var data = {
						organizationId : $scope.organizationId,
						desc : keyword
					}

					var URI = BASE_URL + ROOT_URL+ "/adt/search/getReferralListByReferralName";

					return BillingGenericService.serviceAction(METHOD_POST, URI, data).then(
						function(response) {
							if (response.data.status == "success")
								return response.data.listObject;
						});

				}

				$scope.selectDoctor = function($item, $model, $label, idx) {
					$scope.doctorName = $item.desc;
					$scope.referDoctorId = $item.referralId;
				}

				$scope.initDate= function(){
					$scope.reportEntryDate = null;
					$scope.reportEntrytime = null;
					$scope.validationDate = null;
					$scope.validationtime = null;
					$scope.releaseDate = null;
					$scope.releasetime = null;
					$scope.collectionDate = null;
					$scope.collectiontime = null;
					$scope.acceptDate =  null;
					$scope.acceptDatetime = null;

				}

				// $scope.initClockPicker = function()
				//  {
				//  	$('.clockpicker').clockpicker();
				//  }
				// $scope.collectiontime = null;
				$scope.showRow = true;
				$scope.collectiontime = $scope.collectiontime || '';
				$scope.releasetime = $scope.releasetime || '';
				$scope.acceptDatetime = $scope.acceptDatetime || '';
				$scope.validationtime = $scope.validationtime || '';
				$scope.reportEntrytime = $scope.reportEntrytime || '';
				var clockInitialized = false;

				$scope.initClockPicker = function () {
					setTimeout(function () {
						var $input = $('.clockpicker input');

						if ($input.data('clockpicker')) return;

						$input.clockpicker({
							autoclose: true,   // commit happens here
							appendTo: 'body'
						});

						// Update Angular when value changes
						$input.on('change', function () {
							var value = this.value;
							var clockName = this.name;
							$scope.$applyAsync(function () {
								if(clockName!== undefined && clockName !== null && clockName === 'collectiontime'){
									$scope.collectiontime = value;
								}
								else if(clockName!== undefined && clockName !== null && clockName === 'acceptDatetime'){
									$scope.acceptDatetime = value;
								}
								else if(clockName!== undefined && clockName !== null && clockName === 'reportEntrytime'){
									$scope.reportEntrytime = value;
								}
								else if(clockName!== undefined && clockName !== null && clockName === 'validationtime'){
									$scope.validationtime = value;
								}
								else if(clockName!== undefined && clockName !== null && clockName === 'releasetime'){
									$scope.releasetime = value;
								}
							});
						});

					}, 0);
				};
				//
				// $scope.initClockPicker();

				// $scope.initClockPicker = function () {
				// 	setTimeout(function () {
				//
				// 		$('.clock-input').each(function () {
				// 			var $input = $(this);
				//
				// 			if ($input.data('clockpicker')) return;
				//
				// 			$input.clockpicker({
				// 				autoclose: true,
				// 				appendTo: 'body'
				// 			});
				//
				// 			// 🔑 THIS is the only reliable hook
				// 			$input.on('change', function () {
				// 				var modelName = $input.data('model');
				// 				var value = $input.val();
				//
				// 				$scope.$applyAsync(function () {
				// 					$scope[modelName] = value;
				// 				});
				// 			});
				// 		});
				//
				// 	}, 0);
				// };

				$scope.initClockPicker();


				$scope.dateOptions1 =
					{
						formatYear : 'yyyy',
						showWeeks : false,
					};

				$scope.open2 = function() {
					$scope.popup2.opened = true;
				};

				$scope.popup2 =
					{
						opened : false
					};

				$scope.open3 = function() {
					$scope.popup3.opened = true;
				};

				$scope.popup3 =
					{
						opened : false
					};

				$scope.open4 = function() {
					$scope.popup4.opened = true;
				};

				$scope.popup4 =
					{
						opened : false
					};


				$scope.open5 = function() {
					$scope.popup5.opened = true;
				};

				$scope.popup5 =
					{
						opened : false
					};


				$scope.open6 = function() {
					$scope.popup6.opened = true;
				};

				$scope.popup6 =
					{
						opened : false
					};


				$scope.detailsId = null;
				$scope.getDateListBysampleDetailsId = function(labSampleDtlsId){
					$scope.initDate();
					try {
						$rootScope.startLoader();
						$scope.detailsId = labSampleDtlsId;
						var data = {
							"labSampleDtlsId" : labSampleDtlsId,
						};
						var URI = BASE_URL+ROOT_URL+"/LIS/transaction/getDatesByLabDetialsId";

						GenericService .serviceAction(METHOD_POST, URI, data).then(
							function(response) {
								$rootScope.stopLoader();
								$scope.labObj  = response.data.object;
								if(response.data.object!=null)
								{

									if($scope.labObj.collectedSampleGenDatetime != null){
										var collection = $scope.labObj.collectedSampleGenDatetime.split(' ');
										$scope.collectionDate =   moment(collection[0], 'DD-MM-YYYY').toDate();
										$scope.collectiontime = collection[1];
									}
									if($scope.labObj.sampleAcceptDate != null){
										var sampleAcceptDate = $scope.labObj.sampleAcceptDate.split(' ');
										$scope.acceptDate =   moment(sampleAcceptDate[0], 'DD-MM-YYYY').toDate();
										$scope.acceptDatetime = sampleAcceptDate[1];

									}

									if($scope.labObj.entryDate != null){
										var entry = $scope.labObj.entryDate.split(' ');
										$scope.reportEntryDate =   moment(entry[0], 'DD-MM-YYYY').toDate();
										$scope.reportEntrytime = entry[1];
									}
									if($scope.labObj.validateDate != null){
										var validation = $scope.labObj.validateDate.split(' ');
										$scope.validationDate =   moment(validation[0], 'DD-MM-YYYY').toDate();
										$scope.validationtime = validation[1];

									}
									if($scope.labObj.releaseDate != null){
										var release = $scope.labObj.releaseDate.split(' ');
										$scope.releaseDate = moment(release[0], 'DD-MM-YYYY').toDate();
										$scope.releasetime = release[1];
									}

									$("#sampleDates").modal('show');
								}
							});
					} catch (e) {
						console.log(e.message)
					}

				}

				$scope.updateDates = function(){

					try {
						$rootScope.startLoader();
						var collectionDate1=moment($scope.collectionDate).format("YYYY-MM-DD");
						var acceptDate1=moment($scope.acceptDate).format("YYYY-MM-DD");
						var entryDate1=moment($scope.reportEntryDate).format("YYYY-MM-DD");
						var validateDate1=moment($scope.validationDate).format("YYYY-MM-DD");
						var releaseDate1=moment($scope.releaseDate).format("YYYY-MM-DD");

						if(collectionDate1=="Invalid date" && acceptDate1=="Invalid date" && validateDate1=="Invalid date" && releaseDate1=="Invalid date" && entryDate1=="Invalid date")
						{
							growl.error("Please select Atleast One Date");
							return false;

						}

						var data = {
							"labSampleDtlsId" : $scope.detailsId,
							"screenId" :$scope.labObj.screenId
						};


						if(collectionDate1!="Invalid date")
						{

							data.collectedSampleGenDatetime = collectionDate1+" "+$scope.collectiontime +":00";

						}

						if(acceptDate1!="Invalid date")
						{

							data.sampleAcceptDate = acceptDate1+" "+$scope.acceptDatetime +":00";

						}

						if(entryDate1!="Invalid date")
						{

							data.entryDate = entryDate1+" "+$scope.reportEntrytime +":00";

						}
						if(validateDate1!="Invalid date")
						{

							data.validateDate = validateDate1+" "+$scope.validationtime +":00";

						}
						if(releaseDate1!="Invalid date")
						{
							data.releaseDate = releaseDate1+" "+$scope.releasetime +":00";

						}



						var URI = BASE_URL+ROOT_URL+"/LIS/transaction/updateDatesByLabDetialsId";

						GenericService .serviceAction(METHOD_POST, URI, data).then(
							function(response) {
								if(response.data.status == "success"){
									$rootScope.stopLoader();
									growl.success(response.data.status,
										{
											title : 'success!'
										});
									$("#sampleDates").modal('hide');
								}else if(response.data.status == "error"){
									$rootScope.stopLoader();
									growl.error("Error");
								}

							});
					} catch (e) {
						console.log(e.message)
					}

				}


			} ]);