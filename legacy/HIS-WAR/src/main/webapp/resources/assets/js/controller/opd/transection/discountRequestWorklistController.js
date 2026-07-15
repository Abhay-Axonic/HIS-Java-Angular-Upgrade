'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:discountRequestWorklistController
 * @description #discountRequestWorklistController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'discountRequestWorklistController',
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
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl) {

							/*alert("encounterQueueManagementController");*/

							/* init() function for form object create. */
							$scope.init = function() {


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
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};

								//For Get Active Reason List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});
								
								     
								     $scope.requestSearchObject = {
								    		 'doctorId':0,
								    		 'specialityId':0,
								    		 'patientId':0,
								    		 'specialityId':0,
								    		 'statusId':0,
								    		 'clinicId':0,
								    		 'nursingStationId':0
								     }
								     
								     var requestDiscObj={
												'organizationId': $scope.organizationId,
												'unitId':$scope.unitId,
												'requestSentTo':$scope.userId
										};
										
										
									   // return false;
									    var URI = BASE_URL + ROOT_URL
										+ "/api/billing/requestedDiscount/user";
								
										GenericService
										.serviceAction("POST", URI, requestDiscObj)
										.then(
												function(response) {
													$scope.requestDiscUsersList = [];

													if (response.data.status == "success") {
														$scope.requestDiscUsersList = response.data.listObject;

													}
												});
										
							}

							$scope.init();
							
							$scope.displayDetails = function(item,idx)
							{
								//$scope.displayDetailsList = item.requestDiscountDetailsDtosList;
								$scope.displayDetailsList = [];
								var totalDisPercentage =(parseFloat(item.approvedDiscAmt)/parseFloat(item.netAmount)*100).toFixed(2);
					    		//alert(totalDisPercentage);
								
								var i = 0;
						    	angular.forEach(item.requestDiscountDetailsDtosList, function (value, key) {
						    		var newArr = [];
						    		newArr.orderDetailsId = value.orderDetailsId;
						    		newArr.orderId = value.orderId;
						    		newArr.reqDiscDetailsId = value.reqDiscDetailsId;
						    		newArr.serviceId = value.serviceId;
						    		newArr.reqDisAmt = value.reqDisAmt;
						    		newArr.netAmount = value.netAmount;
						    		newArr.serviceName = value.serviceName;
						    		
						    		var calDicAmt = ((value.netAmount)*totalDisPercentage/100).toFixed(2);	
						    		newArr.approvedDiscAmt = calDicAmt;
						    		newArr.finalAmount = parseFloat(value.netAmount) - parseFloat(calDicAmt);
						    		
						    		$scope.displayDetailsList.push(newArr);
						    		
						    		i++;
						    	});
						    	$scope.requestDiscUsersList[idx].requestDiscountDetailsDtosList = $scope.displayDetailsList;
							}
							
							$scope.checkAmt = function(info,idx)
							{
								if(info.approvedDiscAmt > info.reqDisAmt )
									{
										growl.warning("Approved Amount should be less then request amount!!",{
											title : ERROR_MSG
										});
										$scope.requestDiscUsersList[idx].approvedDiscAmt = info.reqDisAmt; 
							    		return false;
									}
								/*else
									{
										//data-toggle="modal" data-target="#serviceDetailsModal"
									  angular.element('#serviceDetailsModal').modal('show');
									
									}*/
								
							}
							
							$scope.acceptReq = function(info)
							{
								console.log(info);
								
								$rootScope.startLoader();
								var data = {
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										reqDiscId :info.reqDiscId,
										approvedDiscAmt: info.approvedDiscAmt,
								}
								var discServList = [];
								//$scope.displayDetails(info,idx);
								var totalDisPercentage =(parseFloat(info.approvedDiscAmt)/parseFloat(info.netAmount)*100).toFixed(2);
								angular.forEach(info.requestDiscountDetailsDtosList, function (value, key) {
									var newArr = {};
									
									var calDicAmt = ((value.netAmount)*totalDisPercentage/100).toFixed(2);	
						    		newArr.approvedDiscAmt = calDicAmt;
						    		
									newArr.reqDiscDetailsId=value.reqDiscDetailsId,
									//newArr.approvedDiscAmt=value.approvedDiscAmt,
									newArr.updatedBy = $scope.userId,
									newArr.updatedDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
									discServList.push(newArr);
								});
								data.requestDiscountDetailsDtosList = discServList;
								
								  var URI = BASE_URL + ROOT_URL + "/api/billing/approveRequest";
							
									GenericService
									.serviceAction("POST", URI, data)
									.then(function(response) {
												$rootScope.stopLoader();
												if (response.data.status == "success") {
													growl.success("Approved !!!!",{
														title : 'success'
													});
													$scope.init();
												}
											});
							
							}
							
							/*$scope.acceptReqForFinancial = function(item)
							{
								alert("hhh");
								return false;
								console.log(item);
								
								$rootScope.startLoader();
								var data = {
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										reqDiscId :item.reqDiscId,
										admissionId: item.admissionId,
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/approveRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Approved !!!!",{
													title : 'success'
												});
												$scope.init();
											}
										});
								
								
							}
							
							$scope.rejetReq = function(item)
							{
								alert("F");
								console.log(item);
								
								$rootScope.startLoader();
								var data = {
										reqDiscId :item.reqDiscId,
								}
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
								
								GenericService
								.serviceAction("POST", URI, data)
								.then(function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success("Cancelled !!!!",{
													title : 'success'
												});
												$scope.init();
											}
										});
								
							}*/
							
							$scope.nursingStationChange = function()
							{
								$scope.encounterSearchObject.doctorId = '0';
								$scope.encounterSearchObject.clinicId = '0';
							}
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function()
							{
								$scope.encounterSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
                                $scope.encounterSearchObject.organizationId = $scope.organizationId;
                                $scope.encounterSearchObject.unitId = $scope.unitId;
								
								console.log($scope.encounterSearchObject);
								
								var paramObj = {};
								var headers={
										'Content-Type':'application/json'
								};
								
								
							   // return false;
							    var URI = BASE_URL + ROOT_URL
								+ GETSEARCHEDLISTENCOUNTERMASTER;
						
								GenericServiceParamHeader
								.serviceAction(METHOD_POST, URI,
										$scope.encounterSearchObject,
										NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											$scope.allEncounterList = [];
											if (response.data.status == "success")
												
												$scope.allEncounterList =response.data.listObject;
										});
						
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
										.serviceAction(METHOD_POST, URI,
												data,
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
						
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							$scope.getAppointmentDetails = function(item)
							{
								$scope.appointmentDate = item.appointmentDate;
								$scope.appointmentFromTimeString = item.fromTimeString;
								$scope.appointmentRemark = item.remark;
							}
							
						} ]);