'use strict';

/**
 * @Author By  Vivek
 * @name myApp.controller:drCreditNoteApprovalController
 */
angular
		.module('myApp')
		.controller(
				'drCreditNoteApproval',
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
								$scope.cookieDoctorId = cookieObject.doctorId;
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEREASONLIST;
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.reasonList = [];

											if (response.data.status == "success") 
											{
												$scope.reasonList = response.data.listObject;
												$rootScope.stopLoader();
											}
											else if (response.data.status == "error") 
												{
											     $rootScope.stopLoader();
												}
										});
						
								
								
							}
							
							$scope.init();
							
							
							$scope.searchDoctorCreditShare = function()
							{
								if($scope.cookieDoctorId != null)
								{
									$rootScope.startLoader();
										var commonObj = {
												unitId:$scope.unitId,
												orgId:$scope.organizationId,
												fromDate: $rootScope.getFormattedDate($scope.datepickerFrom,false),
												toDate : $rootScope.getFormattedDate($scope.datepickerTo,false),
												doctorId:$scope.cookieDoctorId
											};
											var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorCr/pending/requests";
									GenericService
											.serviceAction("POST", URI,commonObj)
											.then(
													function(response) {
														$scope.doctorCreditShareList = [];
		
														if (response.data.status == "success") {
															$scope.doctorCreditShareList = response.data.listObject;
															$rootScope.stopLoader();
		
														}
														else
															{
														     	$rootScope.stopLoader();
															}
													});
								}
							}
							$scope.searchDoctorCreditShare();
						
							
							
							$scope.searchReferralCreditShare = function()
							{
									$rootScope.startLoader();
										var commonObj = {
												unitId:$scope.unitId,
												orgId:$scope.organizationId,
												fromDate: $rootScope.getFormattedDate($scope.datepickerFrom,false),
												toDate : $rootScope.getFormattedDate($scope.datepickerTo,false),
											};
											var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorCr/pending/requests";
									GenericService
											.serviceAction("POST", URI,commonObj)
											.then(
													function(response) {
														$scope.doctorCreditShareList = [];
		
														if (response.data.status == "success") {
															$scope.doctorCreditShareList = response.data.listObject;
															$rootScope.stopLoader();
		
														}
														else
															{
														     	$rootScope.stopLoader();
															}
													});
							}
							$scope.searchReferralCreditShare();
							
							//Check Service in inclusion
							  var isExists;
								function checkExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												if(val.doctorId === arr.doctorId)
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
							
							/*$scope.add = function()
							{
								if($scope.doctorId == '' || $scope.doctorId == null || $scope.doctorId == undefined)
									{
										growl.warning("Please select doctor..!!",{
											title : WARNING_MSG
										});
										return false;
									}	
								else if($scope.amount == '' || $scope.amount == 0 || $scope.amount == undefined)
									{
										growl.warning("Amount should be greater then ZERO(0)..!!",{
											title : WARNING_MSG
										});
										return false;
									}else
										{
										
										var temp = {doctorId:$scope.doctorId};
										isExists=checkExistsOrNot($scope.genreateDoctorCreditList,temp)
										  if (!isExists) {
											  var docName = $.grep($scope.doctorList, function (docList) {
										            return docList.doctorId == $scope.doctorId;
										           })[0].doctorName;
													
												
													var data = {
															doctorId:$scope.doctorId,
															doctorCredit:$scope.amount,
															doctorName : docName,
															note:$scope.remark,
															billId:$scope.slectedObject.billingMasterId,
															orgUnitId:$scope.unitId,
															orgId:$scope.organizationId,
															updatedBy:$scope.userId,
															createdBy:$scope.userId,
															
													}
													$scope.genreateDoctorCreditList.push(data);
										  }
										  else
											  {
												  growl.warning("Doctor already exist..!!",{
														title : WARNING_MSG
													});
													return false;
											  }
										
										
											
										}
							}*/
							
							$scope.getDoctorDetails = function(item)
							{
								$scope.slectedObject ={};
								$scope.slectedObject = item;
								$scope.outstanding = item.outstanding;
								$scope.drCreditAmount = item.doctorCredit;
							
							}
							
							$scope.getDoctorDetailsReject = function(item)
							{
								$scope.slectedObject ={};
								$scope.slectedObject = item;
							
							}
							
							/*$scope.removeItem = function(idx)
							{
								$scope.genreateDoctorCreditList.splice(idx,1);
							}*/
								
								
							
							$scope.drCreditAccept = function()
							{
								
								if($scope.drCreditAmount > 0)
									{
									if($scope.drCreditAmount > $scope.slectedObject.doctorCredit)
										{
											growl.warning("You can only approve till "+ $scope.slectedObject.doctorCredit+"...!!",{
												title : WARNING_MSG
											});
											return false;
										
										}else if($scope.outstanding < $scope.drCreditAmount)
										{
											growl.warning("You can only approve till "+ $scope.outstanding +", as this is the outstanding for the existing bill...!!",{
												title : WARNING_MSG
											});
											$scope.drCreditAmount = $scope.outstanding;
											return false;
										
										}else{
										  $rootScope.startLoaderPopup();
										
										var commonObj = {
												createdBy:$scope.userId,
												billId:$scope.slectedObject.billId,
												amountPaid:$scope.drCreditAmount,
												doctorCrDetailsId: $scope.slectedObject.doctorCrDetailsId,
												selfPayeeId:$scope.selfPayeeId,
												isPreBilling:$scope.isPreOrPostBilling,
												note : $scope.drCreditRemark
											
											};
										console.log("commonObj",commonObj);
										//return false;
											var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorCr/accept/requests";
											GenericService
													.serviceAction("POST", URI, commonObj)
													.then(
															function(response) {
																$scope.doctorCreditShareList = [];
				
																if (response.data.status == "success") {
																	 $scope.genreateDoctorCreditList = [];
																	
																	$scope.searchDoctorCreditShare();
																	$scope.init();
																	$rootScope.stopLoaderPopup();
																	angular.element('#acceptCreditNoteModal').modal('hide');
																	growl.success("Doctor credit note accepted successfully...!! ",{
																		title : SUCCESS_MSG
																	});
																	return false;
																}
																else
																	{
																	growl.error("Something went wrong.",{
																		title : ERROR_MSG
																	});
																	return false;
																     	$rootScope.stopLoaderPopup();
																	}
															});
									}
									}else
										{
												growl.warning("Amount should be greater than ZERO(0).",{
													title : WARNING_MSG
												});
												return false;
										}
							}
							
							$scope.drCreditReject = function()
							{
								if($scope.rejectReasonId == '' || $scope.rejectReasonId == null || $scope.rejectReasonId == undefined){
									
									growl.warning("Please select reason.",{
										title : WARNING_MSG
									});
									return false;
								}
								else
									{
									 $rootScope.startLoaderPopup();
										
										var commonObj = {
												createdBy:$scope.userId,
												doctorCrId:$scope.slectedObject.doctorCrId,
												rejectReasonId:$scope.rejectReasonId,
												doctorCrDetailsId: $scope.slectedObject.doctorCrDetailsId,
												doctorCredit: $scope.slectedObject.doctorCredit,
												rejectionNote : $scope.rejectNote,
												updatedBy:$scope.userId,
											
											};
										console.log("commonObj",commonObj);
										//return false;
											var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorCr/reject/requests";
											GenericService
													.serviceAction("POST", URI, commonObj)
													.then(
															function(response) {
																$scope.doctorCreditShareList = [];
				
																if (response.data.status == "success") {
																	 $scope.genreateDoctorCreditList = [];
																	
																	$scope.searchDoctorCreditShare();
																	$scope.init();
																	$rootScope.stopLoaderPopup();
																	angular.element('#RejectModal').modal('hide');
																	
																	growl.success("Doctor credit note rejected successfully..!!",{
																		title : SUCCESS_MSG
																	});
																	return false;
																}
																else
																	{
																		growl.error("Something went wrong.",{
																			title : ERROR_MSG
																		});
																		return false;
																     	$rootScope.stopLoaderPopup();
																	}
															});
									}
							}
							
							
						} ]);