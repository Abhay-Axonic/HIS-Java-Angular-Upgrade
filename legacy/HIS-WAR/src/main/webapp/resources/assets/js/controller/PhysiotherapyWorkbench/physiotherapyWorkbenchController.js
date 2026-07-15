'use strict';

/**
 * @Author By  Sidheshwar Lamture
 * @name myApp.controller:physiotherapyWorkbenchController
 * @description #physiotherapyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'physiotherapyWorkbenchController',
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
						'reportService',
						'PaginationService',
						'growl',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService,PaginationService,growl) {

							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
							/* init() function for form object create. */
							$scope.init = function() {

								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
								$scope.todateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date(),
									minDate : new Date(),
								};
								$scope.encounterSearchObject={};
								//Date Object For Search DOB
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};

								//Date Object For reschedule
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
								 
								$scope.searchKeyword ="";
								$scope.selectedVisitType = [];
								$scope.visitTypeIdList=[];
								$scope.visitTypeIdList = [ {
									id : 3,
									label : " Day Care"
								}, {
									id : 4,
									label : " ER"
								}, {
									id : 1,
									label : " OP "
								}, {
									id : 2,
									label : " IP "
								} ];
								
								$scope.selectedModalityList = [];
								console.log("visitType List111:",$scope.visitTypeIdList);

							}

							$scope.init();

							if(cookieObjectAppConfig.physiotherapySpecialityId==null){
								 growl.warning(
											'Physiotherapy department not configured !!',
											{
												title : 'Warning!'
											});
								return false;
							}
							//Search Physiotherapy Appointment List By Filters
							$scope.loadingMessage="";
							$scope.searchEncounter = function() {
								$scope.loadingMessage="Loading...";
								$scope.allEncounterList = [];
								console.log($scope.selectedVisitType);
								console.log($scope.selectedModalityList);
								var visitList =[];
								var modalityList =[];
								
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								angular.forEach($scope.selectedModalityList,
										function(value,index) {
									      modalityList.push(value.id);
										});
								
								$rootScope.startLoader();
								$scope.encounterSearchObject.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								
								$scope.encounterSearchObject.visitList = visitList;
								$scope.encounterSearchObject.physiotherapySpecialityId =cookieObjectAppConfig.physiotherapySpecialityId;
								$scope.encounterSearchObject.unitId=$scope.unitId;
								$scope.encounterSearchObject.organizationId=$scope.organizationId;
								console.log($scope.encounterSearchObject);

								var paramObj = {};
								var headers = {};
								if($scope.encounterSearchObject.patientId==0 || $scope.encounterSearchObject.patientId==undefined || $scope.encounterSearchObject.patientId==""){
									$scope.patientName = '';
									$scope.uhidNumber = '';
								}

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getPatientListForPhysiotherapyWorkbench";
								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												$scope.encounterSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE,
												"", "")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{
														$scope.pagination.data = [];
														$scope.allEncounterList = response.data.listObject;
													     if(!!response && !!response.data && response.data.listObject.length > 0){            
									                         PaginationService.searchData($scope.allEncounterList, $scope.pagination);
									                       }
														}
													if($scope.allEncounterList.length==0){
														$scope.loadingMessage="No record found";
													}
														$rootScope.stopLoader();
														$scope.encounterSearchObject.serviceId =0;
															
														});

							}
							$scope.searchEncounter();

							$scope.clearSearchObject = function() {
								$scope.encounterSearchObject = {
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'patientId' : 0,
									'serviceId' : 0
								}

								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];

								$scope.patientName = undefined;
								$scope.uhidNumber = undefined;
								$scope.search = undefined;
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

							$scope.selectPatientId = function(info, model,
									label) {

								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;

								$scope.encounterSearchObject.patientId = info.patientId;
							}

							$scope.cancelBeforBillSettle = function()
							{
								$scope.checkFlagBillSettle = false;
								$('#checkBillSettlePopUp').modal('hide');
							}
							
							$scope.continueBeforBillSettle = function()
							{
								$scope.checkFlags = 2;
								$scope.checkFlagBillSettle = false;
								$('#checkBillSettlePopUp').modal('hide');
								$scope.acceptRequest($scope.collectionObjSSSSSSS);
							}
							
							$scope.collectionObjSSSSSSS = {};
							$scope.checkFlagBillSettle = false;
							$scope.checkFlags = 1;
                         //Accept patient
							$scope.acceptRequest = function(item) {
								
								
								if(item.visitTypeId == 1){

									var data = {
										    "orgId":$scope.organizationId,
										    "unitId":$scope.unitId,
										    "billingMasterId":item.billingMasterId
										}
									var URI = BASE_URL + ROOT_URL + "/api/billing/getDataForCheckBillStatusByBillNumber";
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.checkBillList=response.data.listObject;
											console.log($scope.checkBillList);
											$scope.billStatusId = $scope.checkBillList[0].billStatusId;
											$scope.paymentEntitlementId = $scope.checkBillList[0].paymentEntitlementId;
											if($scope.paymentEntitlementId != 2  && $scope.paymentEntitlementId != 3){
											if($scope.billStatusId == 3 && $scope.paymentEntitlementId == 1 && $scope.checkFlagBillSettle == false){
												$rootScope.startLoader();
												item.isCritical='N';
												item.unitId=$scope.unitId;
												item.organizationId=$scope.organizationId;
												item.userId=$scope.userId;
												item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
												item.specialityId=$scope.radiologySpecialityId;
												item.updatedBy = $scope.userId;
												item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
												
												var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
												BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
													//$scope.encounterList = response.data.listObject;
													if(response.data.status == "success"){
														$rootScope.stopLoader();
														$scope.init();
														$scope.searchEncounter();
													}else if(response.data.code = 409){
														$rootScope.stopLoader();
														$scope.init();
														$scope.searchEncounter();
													}
												});
											}else{
												if($scope.checkFlags == 1){
													$scope.checkFlagBillSettle = true;
													$('#checkBillSettlePopUp').modal('show');
												}else{
													$rootScope.startLoader();
													item.isCritical='N';
													item.unitId=$scope.unitId;
													item.organizationId=$scope.organizationId;
													item.userId=$scope.userId;
													item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
													item.specialityId=$scope.radiologySpecialityId;
													item.updatedBy = $scope.userId;
													item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
													
													var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
													BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
														//$scope.encounterList = response.data.listObject;
														if(response.data.status == "success"){
															$rootScope.stopLoader();
															$scope.init();
															$scope.searchEncounter();
														}else if(response.data.code = 409){
															$rootScope.stopLoader();
															$scope.init();
															$scope.searchEncounter();
														}
													});
												}
											}
											}else{
												$rootScope.startLoader();
												item.isCritical='N';
												item.unitId=$scope.unitId;
												item.organizationId=$scope.organizationId;
												item.userId=$scope.userId;
												item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
												item.specialityId=$scope.radiologySpecialityId;
												item.updatedBy = $scope.userId;
												item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
												
												var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
												BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
													//$scope.encounterList = response.data.listObject;
													if(response.data.status == "success"){
														$rootScope.stopLoader();
														$scope.init();
														$scope.searchEncounter();
													}else if(response.data.code = 409){
														$rootScope.stopLoader();
														$scope.init();
														$scope.searchEncounter();
													}
												});
											}
										}
									});
								
								}else{
									 $rootScope.startLoader();
										console.log("Accept",item);
										item.unitId=$scope.unitId;
										item.organizationId=$scope.organizationId;
										item.userId=$scope.userId;
										item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
										item.specialityId=cookieObjectAppConfig.physiotherapySpecialityId;
										item.doctorId=item.ordDoctorId;
										item.updatedBy = $scope.userId;
										item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
										
										var URI = BASE_URL + ROOT_URL + "/api/opd/render/services/other";
										BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											//$scope.encounterList = response.data.listObject;
											if(response.data.status == "success"){
												growl.success("Request accepted successfully",
														{
															title : 'Success!'
														});
												 $rootScope.stopLoader();
												$scope.searchEncounter();
											}else{
												 $rootScope.stopLoader();
												$scope.init();
												
											}
											
										});
								}
								
							}

							$scope.getReasonList = function()
						    {
								var data = {organizationId : $scope.organizationId};
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
						    }
							$scope.getReasonList();
							$scope.rejectRequest = function(obj) {
								$scope.rejectReqObj=obj;
								
								
								var URI = BASE_URL + ROOT_URL 
								+ "/api/billing/getOrderDetailsPayeeData/"+obj.orderDetailsId;
					    		  		
					    		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								{	
					    			$scope.orderDetailsPayeeMapper = [];
					    			$scope.orderDetailsPackageMapper = [];
					    			if (response.data.status == "success") 
					    				{
												$scope.orderDetailsPayeeMapper = response.data.listObject;
												$scope.orderDetailsPackageMapper = response.data.object;
												
										}
								});
								
								console.log($scope.rejectReqObj);
								
							}
							
							
							$scope.saveAcceptReject=function(rejectReasonId){
								console.log("reason Id",rejectReasonId);								
								if(rejectReasonId==undefined || rejectReasonId=="")
									{
										growl.warning('Please select reason...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								else
									{
									if(Object.keys($scope.rejectReqObj).length !=0 )
									{
										$('#accepatReject').modal('hide');
										  var data = {};
										   var cancelList =[];
										   var orderedServicesDtosList = {};
										 var data = 
											{
												admissionId :$scope.rejectReqObj.admissionId,
												encounterId :$scope.rejectReqObj.encounterId,
												orderDetailsId:$scope.rejectReqObj.orderDetailsId,
												ordCancelReasonId : rejectReasonId,
												specialityId : $scope.rejectReqObj.specialityId,
												serviceId:$scope.rejectReqObj.serviceId,
												createdBy : $scope.userId,
												orderDetailsPayeeMapperDtosList : $scope.orderDetailsPayeeMapper,
							    				orderDetailsPackageMapperDtosList : $scope.orderDetailsPackageMapper
											}
											console.log('AddCapAmt',data);
											//return false;
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
											BillingGenericService
													.serviceAction(METHOD_POST,
															URI, data,
															NOTIFICATION_MSG_STATUS_TRUE)
													.then(
															function(response) {
																	$scope.init();
																	$scope.searchEncounter();
																	$scope.rejectReqObj={};
																	$scope.rejectReasonId="";
																	$rootScope.stopLoader();
															});
										
										}
									}
								}
								  
						
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.datepickerFromDate.getTime();
								
								if(moment($scope.datepickerToDate).isBefore(moment($scope.datepickerFromDate)))
								{
									 $scope.datepickerToDate=new Date($scope.datepickerFromDate.getTime());
								}
							}
							
							$scope.search="";
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
							$scope.addServiceToTempServiceList = function($item, $model, $label){
								$scope.encounterSearchObject.serviceId = $item.serviceMasterId;
								
							}
						
							$scope.checkPatient = function()
							{
								if($scope.patientName ==undefined)
								{
									$scope.encounterSearchObject.patientId =0;
								}
							}
							
							
					        $scope.getClassForColor = function(info)
							{
								   
								   if(info.isCritical=='Y'){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
								   }else if(info.encounterTypeId==6){
									   return 'td-bg-green';
								   }
							}
					        
						} ]);