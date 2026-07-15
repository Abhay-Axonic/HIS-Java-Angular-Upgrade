angular
		.module('myApp')
		.controller(
				'drAdvancedApprovalController',
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
						'GenericServiceFileUpload',
						'growl',
						'Pubnub',
						'$stateParams',
						'PaginationService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl,Pubnub,$stateParams,PaginationService) {
						
							
							$scope.init = function()
							{
								$rootScope.loginpage = true;
								
								// for datePicker
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									
								};
								
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
									
								
								$scope.openDatePickerFrom = function() {
									$scope.datepickerFrom.opened = true;
								};

								$scope.datepickerFrom = {
									opened : false
								};
								
								
								$scope.openDatePickerTo = function() {
									$scope.datepickerTo.opened = true;
								};

								$scope.datepickerTo = {
									opened : false
								};
								
								$scope.datepickerFrom = new Date();
								$scope.datepickerTo = new Date();
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
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
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.specialityId = null;
								var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
								var obj = {
											organizationId : $scope.organizationId,
											unitId         :$scope.unitId
									    }
								BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									console.log(response);
									if(response.data.status == "success"){
										$scope.specialityMasterList = response.data.listObject;
										console.log('splList',$scope.specialityMasterList);
									}
								});
								
								
								
							}
							
							$scope.init();
							
							
							$scope.searchAdvanceList = function()
							{
								$rootScope.startLoader();
									var commonObj = {
											orgUnitId:$scope.unitId,
											orgId:$scope.organizationId,
											fromDate: $rootScope.getFormattedDate($scope.datepickerFrom,false),
											toDate : $rootScope.getFormattedDate($scope.datepickerTo,false)
										};
										var URI = BASE_URL + ROOT_URL + "/api/doctor/advance/get/list";
										GenericService.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													
													$scope.pagination.data = [];
													$scope.doctorAdvanceList1 = [];
													$scope.doctorAdvanceList = [];
	
													if (response.data.status == "success") {
														$scope.doctorAdvanceList1 = response.data.listObject;
														
														angular.forEach($scope.doctorAdvanceList1,function(value,key){
															if(value.requestStatus == '1'){
																$scope.doctorAdvanceList.push(value);
															}
														})
														
														
														if($scope.doctorAdvanceList.length > 0){            
								                            PaginationService.searchData($scope.doctorAdvanceList , $scope.pagination);
								                          }
														
														$rootScope.stopLoader();
													}
													else
														{
													     	$rootScope.stopLoader();
														}
												});
							}
							$scope.searchAdvanceList();
							
							
							$scope.getDoctorDetails = function(item)
							{
								$scope.doctorId  = item.doctorId;
								$scope.drCreditAmount = item.requestedAdvance;
								$scope.drCreditAmountOld = item.requestedAdvance;
								$scope.doctorAdvanceDetailsId = item.doctorAdvanceDetailsId;
								$scope.doctorAdvanceId = item.doctorAdvanceId;
							}
							
							$scope.drCreditAccept = function()
							{
								 if($scope.drCreditAmount == '' || $scope.drCreditAmount == null || $scope.drCreditAmount == 0)
									{
										growl.warning('Amount should be greater than ZERO(0).',{title : "WARNING!"
										});
										return false;
									}else if($scope.drCreditAmount > $scope.drCreditAmountOld)
										{
											growl.warning('Amount should be greater than '+ $scope.drCreditAmountOld,{title : "WARNING!"
											});
											$scope.drCreditAmount = $scope.drCreditAmountOld;
											return false;
										}
								 else{
											$rootScope.startLoaderPopup();
											var commonObj = {
													doctorAdvanceDetailsId : $scope.doctorAdvanceDetailsId,
													approvedAdvance : $scope.drCreditAmount,
													remark : $scope.drCreditRemark,
													doctorAdvanceId : $scope.doctorAdvanceId,
													doctorId:$scope.doctorId,
													orgId:$scope.organizationId,
													orgUnitId:$scope.unitId,
													createdBy:$scope.userId
												};
											if($scope.drCreditRemark == '' || $scope.drCreditRemark == undefined)
												{
												commonObj.remark  = '';
												}
											console.log("commonObj",commonObj);
											//return false;
												var URI = BASE_URL + ROOT_URL + "/api/doctor/advance/accept";
										GenericService
												.serviceAction("POST", URI, commonObj)
												.then(
														function(response) {
			
															if (response.data.status == "success") {
																growl
																.success(
																		'Saved successfully...!!',
																		{
																			title : 'Success!'
																		});
																$scope.searchAdvanceList();
																$scope.init();
																$scope.initPopupData();
																$rootScope.stopLoaderPopup();
																angular.element('#acceptCreditNoteModal').modal('hide');
															}
															else
																{
																growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
															     	$rootScope.stopLoaderPopup();
																}
														});
									}
							}
							
							
							
							$scope.drCreditReject = function()
							{
								 
											$rootScope.startLoader();
											var commonObj = {
													doctorAdvanceDetailsId : $scope.doctorAdvanceDetailsId
												};
											if($scope.drCreditRemark == '' || $scope.drCreditRemark == undefined)
												{
												commonObj.remark  = '';
												}
											console.log("commonObj",commonObj);
											//return false;
												var URI = BASE_URL + ROOT_URL + "/api/doctor/advance/reject";
										GenericService
												.serviceAction("POST", URI, commonObj)
												.then(
														function(response) {
			
															if (response.data.status == "success") {
																growl
																.success(
																		'Saved successfully...!!',
																		{
																			title : 'Success!'
																		});
																$scope.searchAdvanceList();
																$scope.init();
																$scope.initPopupData();
																$rootScope.stopLoader();
																angular.element('#confirmPopForDoctorAdvanced').modal('hide');
															}
															else
																{
																growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
															     	$rootScope.stopLoader();
																}
														});
									
							}
							
							$scope.initPopupData = function()
							{
								$scope.doctorId  = '';
								$scope.amount = 0;
								$scope.remark = '';
								$scope.specialityId = ''
								
							}
							
						} ]);