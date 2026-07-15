angular
		.module('myApp')
		.controller(
				'drAdvanceController',
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
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl,Pubnub,$stateParams) {
						
							
							$scope.init = function()
							{
								$rootScope.loginpage = true;
								
								// for datePicker
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									
								};
								
								
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
							
							
							$scope.clearField = function()
							{
								$scope.amount = '';
								$scope.doctorId  = '';
								$scope.specialityId = '';
							}
							
							$scope.searchAdvanceList = function()
							{
								$rootScope.startLoader();
									var commonObj = {
											orgUnitId:$scope.unitId,
											orgId:$scope.organizationId,
											fromDate: $rootScope.getFormattedDate($scope.datepickerFrom,false),
											toDate : $rootScope.getFormattedDate($scope.datepickerTo,false)
										};
									
									///commonObj.fromDate = moment($scope.datepickerFrom).format('YYYY-MM-DD');
									//commonObj.toDate = moment($scope.datepickerFrom).format('YYYY-MM-DD');
									
										var URI = BASE_URL + ROOT_URL + "/api/doctor/advance/get/list";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.doctorAdvanceList = [];
	
													if (response.data.status == "success") {
														$scope.doctorAdvanceList = response.data.listObject;
														$rootScope.stopLoader();
													}
													else
														{
													     	$rootScope.stopLoader();
														}
												});
							}
							
							$scope.getDetails = function(item)
							{
								$scope.doctorId  = item.doctorId;
								$scope.amount = item.requestedAdvance;
								$scope.remark = item.remark;
								$scope.specialityId = item.specialityId,
								$scope.getDocList(item.specialityId);
								$scope.doctorAdvanceDetailsId = item.doctorAdvanceDetailsId
							}
							
							$scope.getDeleteAdvanced = function(item)
							{
								$scope.detailsObject = item;
							}
							
							$scope.searchAdvanceList();
							
							$scope.initPopupData = function()
							{
								$scope.doctorId  = '';
								$scope.amount = 0;
								$scope.remark = '';
								$scope.specialityId = ''
								
							}
							
							$scope.getDocList = function()
							{
								
								$rootScope.startLoaderPopup();
								var data = {
										specialityId : $scope.specialityId,
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
								
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													console.log(response);
													$scope.doctorList = [];
													$rootScope.stopLoaderPopup();
													if (response.data.status == "success"){
														$scope.doctorList = response.data.listObject;
													console.log('DoctorList',$scope.doctorList);
													$rootScope.stopLoaderPopup();
												}else{
													$rootScope.stopLoaderPopup();
												}
								});
								
							}
							
							$scope.saveDoctorAdvance = function()
							{
								if($scope.specialityId == '' || $scope.specialityId == null)
									{
											growl.warning('Please select speciality',{title : "WARNING!"
											});
											return false;
									
									}else if($scope.doctorId == '' || $scope.doctorId == null)
										{
											growl.warning('Please select doctor',{title : "WARNING!"
											});
											return false;
										}
									else if($scope.amount == '' || $scope.amount == null || $scope.amount == 0)
									{
										growl.warning('Amount should be greater than ZERO(0).',{title : "WARNING!"
										});
										return false;
									}else{
											$rootScope.startLoaderPopup();
											var commonObj = {
													specialityId : $scope.specialityId,
													doctorId : $scope.doctorId,
													totalAdvance : $scope.amount,
													orgId : $scope.organizationId,
													orgUnitId : $scope.unitId,
													createdBy : $scope.userId,
													remark : $scope.remark
												};
											console.log("commonObj",commonObj);
										//	return false;
												var URI = BASE_URL + ROOT_URL + "/api/doctor/advance/save";
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
																angular.element('#generateDoctorAdvancedModal').modal('hide');
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
							
							$scope.updateDoctorAdvance = function()
							{
								if($scope.specialityId == '' || $scope.specialityId == null)
									{
											growl.warning('Please select speciality',{title : "WARNING!"
											});
											return false;
									
									}else if($scope.doctorId == '' || $scope.doctorId == null)
										{
											growl.warning('Please select doctor',{title : "WARNING!"
											});
											return false;
										}
									else if($scope.amount == '' || $scope.amount == null || $scope.amount == 0)
									{
										growl.warning('Amount should be greater than ZERO(0).',{title : "WARNING!"
										});
										return false;
									}else{
											$rootScope.startLoaderPopup();
											var commonObj = {
													requestedAdvance : $scope.amount,
													remark : $scope.remark,
													doctorAdvanceDetailsId: $scope.doctorAdvanceDetailsId
													
													
												};
											console.log("commonObj",commonObj);
										//	return false;
												var URI = BASE_URL + ROOT_URL + "/api/doctor/advance/update";
										GenericService
												.serviceAction("POST", URI, commonObj)
												.then(
														function(response) {
															if (response.data.status == "success") {
																
																growl
																.success(
																		'Updated successfully...!!',
																		{
																			title : 'Success!'
																		});
																
																$scope.searchAdvanceList();
																$scope.init();
																$scope.initPopupData();
																$rootScope.stopLoaderPopup();
																angular.element('#generateDoctorAdvancedModalUpdate').modal('hide');
															}
															else
																{
																growl
																.error(
																		'Something went wrong..!!',
																		{
																			title : 'Error!'
																		});
															     	$rootScope.stopLoaderPopup();
																}
														});
									}
							}
							
							$scope.updateStatus = function() {
								
								$rootScope.startLoader();
								var data = {
										doctorAdvanceDetailsId : $scope.detailsObject.doctorAdvanceDetailsId,
										status : 'I'
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL + "/api/doctor/advance/status";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Deleted successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope.searchAdvanceList();
														$scope.init();
														$scope.initPopupData();
														$rootScope.stopLoaderPopup();
														$rootScope.stopLoader();
													} else {
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

						
							
						} ]);