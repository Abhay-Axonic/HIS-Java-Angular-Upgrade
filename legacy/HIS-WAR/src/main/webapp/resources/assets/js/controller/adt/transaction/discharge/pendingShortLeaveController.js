'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:pendingShortLeaveController
 * @description #pendingShortLeaveController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'pendingShortLeaveController',
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
								$state,$stateParams) {
							
							/* init() function for form object create. */
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.doctorId = cookieObject.doctorId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.itemSelectedsAccept = [];
								
								var data = {
										organizationId : $scope.organizationId,
								};
								/*var shortLeaveStatusList = [];
								shortLeaveStatusList.push(1);*/
								
								var commonObj = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									doctorId : $scope.doctorId
								};
								
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
						
										if(!angular.equals({},$stateParams.bedManagmentObj))
										{
											$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
										}				
						
										if ($scope.bedManagmentCookiesObj != null) 
										{
											/*$scope.bedManagmentCookiesObj = JSON
													.parse($cookies
															.get('bedManagmentCookies'));
											console.log($scope.bedManagmentCookiesObj);*/
											commonObj.admissionId  =  $scope.bedManagmentCookiesObj.admissionId;
											//$cookies.remove('bedManagmentCookies');
										  } else {
											$scope.bedManagmentCookiesObj = null;
										}
								var URI = BASE_URL + ROOT_URL
										+ GETSHORTLEAVEREQUESTLISTFORDOCTOR;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.shortLeaveRequestList = [];

													if (response.data.status == "success") {
														$scope.shortLeaveRequestList = response.data.listObject;

													}
												});
								

							}

							$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveShortLeaveRequest();
							}

							$scope.getAdmissionDataAccept = function(item)
							{
								//alert("sc");
								//console.log(item);
								$scope.itemSelectedsAccept = [];
								$scope.itemSelectedsAccept = item;
								console.log($scope.itemSelectedsAccept);
								
							}
							
							$scope.saveAcceptance = function()
							{
								
								if($('#noteTransfer').val() == '' || $('#noteTransfer').val() == null || $('#noteTransfer').val() == undefined){
									growl.warning('Please add acceptance note...!!',
											{
												title : 'Warning!'
											});
									return false;
								}else{
									$rootScope.startLoader();
									console.log($rootScope.itemSelectedsAccept);
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');
								
									var tansferNote = $('#noteTransfer').val();
									var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											shortLeaveId:$scope.itemSelectedsAccept.shortLeaveId,
												updatedBy:$scope.userId,
												updatedDate:$scope.currentDate,
													authorizedBy:$scope.userId,
														doctorsNote :tansferNote											
											
									};
									
									console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL
											+ ACCEPTSHOTLEAVEBYDOCTOR;
									GenericService
											.serviceAction("POST", URI,
													data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Request accepted...!!',
																			{
																				title : 'Success!'
																			});
															//commonNote
															angular.element(
															'#commonNote')
															.modal('hide');
															$scope.init();
														} else {
															growl
																	.error(
																			'Something went wrong...!!',
																			{
																				title : 'Error!'
																			});
														}
														 $rootScope.stopLoader();
													});
								}
							}
							
							$scope.saveAcceptReject = function()
							{
								
								if($('#rejectReasonIdDrpDN').val()== ""){
									 growl.warning(
												'Please select reject reason...!!',
												{
													title : 'Warning!'
												});
									 angular.element('#accepatReject').modal('show');
									return false;
								}else{
									angular.element('#accepatReject').modal('hide');
									$rootScope.startLoader();
									console.log($rootScope.itemSelectedsAccept);
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');
								
									//var noteReject = $('#noteReject').val();
									//var rejectReasonId = $('#rejectReasonId').val();
									var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											shortLeaveId:$scope.itemSelectedsAccept.shortLeaveId,
											updatedBy:$scope.userId,
											updatedDate:$scope.currentDate,
											doctorsNote :$('#noteRejectDrpDN').val(),
											rejectionReasonId : $('#rejectReasonIdDrpDN').val(),
									};
									
									console.log(data);
								  //return false;
									var URI = BASE_URL + ROOT_URL
											+ REJECTSHOTLEAVEBYDOCTOR;
									GenericService
											.serviceAction("POST", URI,
													data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															growl
																	.success(
																			'Request Rejected...!!',
																			{
																				title : 'Success!'
																			});
															//accepatReject
															
															$scope.init();
														} else {
															growl
																	.error(
																			'Something went wrong...!!',
																			{
																				title : 'Error!'
																			});
														}
														 $rootScope.stopLoader();
													});
									}
								
								
							}
							

						} ]);
