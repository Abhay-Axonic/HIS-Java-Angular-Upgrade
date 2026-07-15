'use strict';

/**
 * @Author Sidheshwar Lamture
 * @name myApp.controller:hemodialysisWorkloadController
 * @description #hemodialysisWorkloadController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'hemodialysisWorkloadController',
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
						'$compile',
						'growl',
						'reportService','PagerService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,growl,reportService,PagerService) {

							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							
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
								
								$rootScope.loginpage = true;
								
								
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
									$scope.roleId = 2;

									$rootScope.loginpage = true;
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;

									// for popup
									$scope.showModal = false;
									$scope.buttonClicked = "";
									$scope.popUpFlag = true;
									// end for popup
									$scope.date = new Date();
								    $scope.today = moment($scope.date).format('DD-MM-YYYY');
									//start radiology
									$scope.visitTypeList = [ {
										id : 3,
										label : " Day Care"
									}, {
										id : 4,
										label : " ER"
									},
									{
										id : 2,
										label : " IP "
									},
									{
										id : 1,
										label : " OP "
									} ];
									$scope.selectedVisitType = [];
									$scope.selectedHemoChairList=[];
									
									var data = {organizationId : $scope.organizationId};
									var headerObj = {
										organization_id : $scope.organizationId,
										unit_id : $scope.unitId
									}
									var paramObj = {};


									//For Get Active Reason List
									var URI = BASE_URL + ROOT_URL
											+ GETACTIVEREASONLIST;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$scope.reasonList = [];

														if (response.data.status == "success") {
															$scope.reasonList = response.data.listObject;

														}
													});

									$scope.encounterSearchObject = {
										'patientId' : 0,
										'organizationId' : $scope.organizationId,
										'unitId' : $scope.unitId,
										'serviceId':0
									}
									
								}

								$scope.init();
								
								
								$scope.loadingMsg="";
								$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
									$rootScope.startLoader();
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
									var visitList =[];
									$scope.dialysisPatientList = [];
									$scope.loadingMsg="Loading...";
									$scope.encounterSearchObject.offset=offset;
									$scope.encounterSearchObject.noOfRecordsPerPage=noOfRecordsPerPage;
									angular.forEach($scope.selectedVisitType,
											function(value,index) {
										         visitList.push(value.id);
											});
								
									/*angular.forEach($scope.selectedHemoChairList,
											function(value,index) {
										      modalityList.push(value.id);
											});*/
									
									$rootScope.startLoader();
									$scope.encounterSearchObject.fromDate = moment(
											$scope.datepickerFromDate).format(
											'YYYY-MM-DD');
									$scope.encounterSearchObject.toDate = moment(
											$scope.datepickerToDate).format(
											'YYYY-MM-DD');
									$scope.encounterSearchObject.visitList = visitList;
									$scope.encounterSearchObject.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;

									if($scope.searchKeyword!=""){
										$scope.encounterSearchObject.searchKeyword=	$scope.searchKeyword;
									}else{
										$scope.encounterSearchObject.searchKeyword=	null;
									}
									
									var URI1 = BASE_URL + ROOT_URL + "/api/hemodialysis/getPatientListForDialysisWorkload";
									GenericService.serviceAction("POST", URI1, $scope.encounterSearchObject).then(function(response) {
												
												if (response.data.status == "success")
													{
													angular.forEach(response.data.listObject,function(value,index){
														value.technicianList=$scope.technicianList;
														value.dialysisMachineList=$scope.dialysisMachineList;
														value.technicianAcceptanceFlag=angular.copy(value.technicianAcceptance);
														$scope.dialysisPatientList.push(value);
														
													});
													}
												
												if($scope.dialysisPatientList.length==0){
													$scope.loadingMsg="No records found...";
												}
												
												$scope.commonListCount = response.data.object;
												$scope.setPage(1,true);
												$rootScope.stopLoader();
											});
									
								   }
								
								
								//Search Appointment List By Filters
								$scope.searchEncounter = function(organizationId,offset, noOfRecordsPerPage, unitId) {
									console.log($scope.selectedHemoChairList)
									console.log($scope.selectedVisitType);
									 offset = offset != null ? offset : 0;
							         noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
							         
									var visitList =[];
									
									angular.forEach($scope.selectedVisitType,
											function(value,index) {
										         visitList.push(value.id);
											});
									
									$rootScope.startLoader();
									$scope.encounterSearchObject.offset=offset;
									$scope.encounterSearchObject.noOfRecordsPerPage=noOfRecordsPerPage;
									$scope.encounterSearchObject.fromDate = moment(
											$scope.datepickerFromDate).format(
											'YYYY-MM-DD');
									$scope.encounterSearchObject.toDate = moment(
											$scope.datepickerToDate).format(
											'YYYY-MM-DD');
									
									$scope.encounterSearchObject.visitList = visitList;
									$scope.encounterSearchObject.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
									
									console.log("cookieObjectAppConfig.hemodialysisSpecialityId"+cookieObjectAppConfig.hemodialysisSpecialityId);

									var paramObj = {};
									var headers = {};

									//return false;
									var URI = BASE_URL + ROOT_URL
											+ "/api/hemodialysis/getPatientListForDialysisWorkload";

									GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													$scope.encounterSearchObject,
													NOTIFICATION_MSG_STATUS_FALSE,
													"", "")
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														$scope.dialysisPatientList = [];
														if (response.data.status == "success")
															{
															angular.forEach(response.data.listObject,function(value,index){
																value.technicianList=$scope.technicianList;
																value.dialysisMachineList=$scope.dialysisMachineList;
																value.technicianAcceptanceFlag=angular.copy(value.technicianAcceptance);
																$scope.dialysisPatientList.push(value);
															});
															}
														$rootScope.stopLoader();
															});

								}
								//$scope.searchEncounter(); 

								
								
								
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
										    $scope.searchEncounter($scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize,
											$scope.unitId);
									}
								}

								$scope.initMasterList($scope.organizationId,
								$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

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
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															return response.data.listObject;
													});

								}
								
								$scope.searchKeyword="";
								$scope.serachPatient= function(searchKeyword){
									//orgId, unitId, offset, noOfRecordsPerPage
									if(searchKeyword!=null && searchKeyword!=""){
										$scope.searchKeyword=searchKeyword;
									}else{
										$scope.searchKeyword="";
									}
									if($scope.encounterSearchObject.patientId=="" || $scope.encounterSearchObject.patientId==0 || $scope.patientName==null || $scope.uhidNumber==null){
										$scope.encounterSearchObject.patientId=0;
									}
										$scope.initMasterList($scope.organizationId,
												$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								}
								
								$scope.setNoOfRecords = function() {
									$scope.initMasterList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								};
								
								
								$scope.selectPatientId = function(info, model,
										label) {

									$scope.patientName = info.patientName;
									$scope.uhidNumber = info.uhidNumber;
									$scope.mobileNumber = info.mobileNumber;

									$scope.encounterSearchObject.patientId = info.patientId;
								}
								
								
								
			    	//Get dialysis technician list	
						$scope.technicianList =[];
						$scope.getDialysisTechnicianList = function() {
									var data = {
										unitId : $scope.unitId,
										organisationId : $scope.organizationId
									};
									var URI = BASE_URL + ROOT_URL+ "/api/global/getDialysisTechnicianList";
									$scope.technicianList=[];
									GenericService.serviceAction("POST",URI,data)
											.then(function(response) {
														if (response.data.status == "success")
															$scope.technicianList = response.data.listObject;
									 });

								};	
			               $scope.getDialysisTechnicianList();
			               
				           	//Get dialysis technician list	
							  $scope.dialysisMachineList =[];
							  $scope.getDialysisMachineList = function() {
										var data = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
										var URI = BASE_URL + ROOT_URL+ "/api/hemodialysis/getDialysisMachines";
										$scope.dialysisMachineList=[];
										GenericService.serviceAction("POST",URI,data)
												.then(function(response) {
															if (response.data.status == "success"){
																$scope.dialysisMachineList = response.data.listObject;	
															}
																
										 });

									};	
				               $scope.getDialysisMachineList();
				               
								
			               $scope.transferObj={};
							$scope.acceptRequestHemodialysis = function(item) {
								
								var currDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
								var dataForTransReq={
								organizationId : $scope.organizationId,
					    		unitId : $scope.unitId,
					    		updatedBy : $scope.userId,
					    		updatedDate : currDate,
					    		specialityId : cookieObjectAppConfig.hemodialysisSpecialityId,
					    		visitTypeId : item.visitTypeId,
					    		dialysisAppMapperId:item.dialysisAppMapperId,
					    		technicianAcceptance:'A',
					    		technicianId:item.technicianId,
					    		machineId:item.machineId
								}
								
									$scope.transferObj=dataForTransReq;
									$('#acceptNephrologyReqModal').modal('show'); 
							}   
							
							
							$scope.processRejectRequest=function(){
								$('#rejectNephrologyReqModal').modal('hide'); 
								$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/hemodialysis/processDialysisRequest";
							GenericService.serviceAction("POST",URI,$scope.rejectReqObj).then(function(response){
								if(response.data.status == "success"){
									 growl.success(
												'Dialysis request has been rejected!!',
												{
													title : 'Success!'
												});
									$scope.init();
									//$scope.searchEncounter();
									$scope.initMasterList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
									$rootScope.stopLoader();
								 }
							 });
							}
							
							$scope.processAcceptRequest=function(){
								$('#acceptNephrologyReqModal').modal('hide'); 
								$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/hemodialysis/processDialysisRequest";
							GenericService.serviceAction("POST",URI,$scope.transferObj).then(function(response){
								//$scope.encounterList = response.data.listObject;
								if(response.data.status == "success"){
									 growl.success(
											 response.data.message,
												{
													title : 'Success!'
												});
									$scope.init();
									//$scope.searchEncounter();
									$scope.initMasterList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
									$rootScope.stopLoader();
								 }else{
									 growl.warning(
												'Bed is already occupied !!',
												{
													title : 'Warning!'
												});
									 $rootScope.stopLoader();
									return false;
								 }
							 });
							}
							
							
							$scope.stillOnSamePage=function(){
								return false;
							}
							
							$scope.rejectReqObj={};
							$scope.rejectRequest = function(item) {
								var currDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
								var dataForRejectReq={
								organizationId : $scope.organizationId,
					    		unitId : $scope.unitId,
					    		updatedBy : $scope.userId,
					    		updatedDate : currDate,
					    		visitTypeId : item.visitTypeId,
					    		dialysisDoctorId:$scope.doctorId,
					    		dialysisAppMapperId:item.dialysisAppMapperId,
					    		technicianAcceptance:'R',
					    		technicianId:item.technicianId
								
								}
								$scope.rejectReqObj=dataForRejectReq;
								$('#rejectNephrologyReqModal').modal('show');
							}
			               
							$scope.clearSearchObject = function() {
								$scope.encounterSearchObject = {
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'patientId' : 0,
									'serviceId' : 0
								}

								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								$scope.selectedHemoChairList=[];
								$scope.patientName = '';
								$scope.uhidNumber = '';

								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
								$scope.initMasterList($scope.organizationId,
										$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							}
						} ]);