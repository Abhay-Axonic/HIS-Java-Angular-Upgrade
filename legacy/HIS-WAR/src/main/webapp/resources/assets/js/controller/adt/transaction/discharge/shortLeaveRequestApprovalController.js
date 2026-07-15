

/**
 * @Author By  Sidheshwar Lamture
 * @name myApp.controller:pendingShortLeaveController
 * @description #pendingShortLeaveController Controller of the myApp
 */
(function () {
  "use strict";
						function shortLeaveRequestApprovalController($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,PagerService) {
							
							/**Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.listCount;
							
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
								
								var commonObj = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
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
						

								var URI = BASE_URL + ROOT_URL
										+ GETSHORTLEAVEREQUESTLISTACCEPTEDBYDOCTOR;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.shortLeaveRequestList = [];

													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,function(value,index){
															if((value.depositAmount) >= (value.billAmount-value.paidAmount)){
																value.isDisabled=false;
															}else{
																value.isDisabled=true;
															}
															$scope.shortLeaveRequestList.push(value);
														});
														

													}
												});
								

							}

							$scope.init();


							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var commonObj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,

									};
								
								var URI = BASE_URL + ROOT_URL
								+ GETSHORTLEAVEREQUESTLISTACCEPTEDBYDOCTOR;
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.shortLeaveRequestList = [];

											if (response.data.status == "success") {
												$scope.listCount= response.data.object;
												angular.forEach(response.data.listObject,function(value,index){
													if((value.depositAmount) >= (value.billAmount-value.paidAmount)){
														value.isDisabled=false;
													}else{
														value.isDisabled=true;
													}
													$scope.shortLeaveRequestList.push(value);
												});
												$scope.setPage(1,false);
												$rootScope.stopLoader();
											}
										});
						
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								console.log("offset", offset);
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var commonObj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,

									};
								
								var URI = BASE_URL + ROOT_URL
								+ GETSHORTLEAVEREQUESTLISTACCEPTEDBYDOCTOR;
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.shortLeaveRequestList = [];

											if (response.data.status == "success") {
												$scope.listCount= response.data.object;
												angular.forEach(response.data.listObject,function(value,index){
													if((value.depositAmount) >= (value.billAmount-value.paidAmount)){
														value.isDisabled=false;
													}else{
														value.isDisabled=true;
													}
													$scope.shortLeaveRequestList.push(value);
													
												});
												$rootScope.stopLoader();
											}
										});
						
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.listCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
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
								if($('#noteTransfer').val()== ""){
									 growl.warning(
												'Note is required !!',
												{
													title : 'Warning!'
												});
									return false;	
								}
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
											billingsNote :tansferNote											
										
								};
								
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ ACCEPTSHOTLEAVEBYBILLINGUSER;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Accepted Successfully...!!',
																		{
																			title : 'Success!'
																		});
														//commonNote
														angular.element(
														'#commonNote')
														.modal('hide');
														$scope.init();
														$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
							
							$scope.saveAcceptReject = function()
							{
								if($('#noteRejectDrpDN').val()== ""){
									 growl.warning(
												'Note is required !!',
												{
													title : 'Warning!'
												});
									return false;	
								}
								if($('#rejectReasonIdDrpDN').val()==""){
									 growl.warning(
												'Reason is required !!',
												{
													title : 'Warning!'
												});
									return false;	
							    }
								$rootScope.startLoader();
								console.log($rootScope.itemSelectedsAccept);
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
							
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										shortLeaveId:$scope.itemSelectedsAccept.shortLeaveId,
										updatedBy:$scope.userId,
										updatedDate:$scope.currentDate,
										billingsNote :$('#noteRejectDrpDN').val(),
										rejectionReasonId : $('#rejectReasonIdDrpDN').val(),
								};
								
								console.log(data);
							  //return false;
								var URI = BASE_URL + ROOT_URL
										+ REJECTSHOTLEAVEBYBILLINGUSER;
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Rejected Successfully...!!',
																		{
																			title : 'Success!'
																		});
														//accepatReject
														angular.element(
														'#accepatReject')
														.modal('hide');
														$scope.init();
														$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
						function config($stateProvider) {
						    $stateProvider.state('shortLeaveRequestApproval', {
								 url: '/shortLeaveRequestApproval',
								 templateUrl: 'views/master/UnitMaster/billingMaster/short-leave-request-approval.html',
								 controller: "shortLeaveRequestApprovalController",
								 title: 'Short Leave Request Approval',
								})
						  }

						  angular
						    .module("myApp")
						    .config(config)
						    .controller("shortLeaveRequestApprovalController", shortLeaveRequestApprovalController);
						})();