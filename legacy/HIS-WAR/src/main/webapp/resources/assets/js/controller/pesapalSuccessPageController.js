'use strict';

angular
		.module('myApp')
		.controller(
				'pesapalSuccessPageController',
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
						'PagerService','$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$location) {

							var cookieObject = $cookies
									.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}

							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;

							function getUrlVars() {
								var vars = [], hash;
								var hashes = window.location.href.slice(
										window.location.href.indexOf('?') + 1)
										.split('&');
								for (var i = 0; i < hashes.length; i++) {
									hash = hashes[i].split('=');
									vars.push(hash[0]);
									vars[hash[0]] = hash[1];
								}
								return vars;
							}
							var param = getUrlVars()["pesapal_transaction_tracking_id"];
							var param1 = getUrlVars()["pesapal_merchant_reference"];
							var res = param1.split("#");
							$scope.transactionId = param;
							$scope.merchantId = res[0];
							$rootScope.startLoader();
							
							var URI1 = BASE_URL + ROOT_URL
									+ "/api/global/getPesapayPaymentStatus";
							var data = {
								"transactionReferenceId" : $scope.merchantId,
								"transactionId" : $scope.transactionId,
								"unitId" : $scope.unitId,
								"organizationId" : $scope.organizationId
							}
							var baseUrl=location.protocol + "//" + location.hostname +
				               (location.port && ":" + location.port);
							 var redirectUrl = baseUrl + ROOT_URL+"/#/pesapalProcessPage";
							 
							 data.callbackUrl=redirectUrl;
							GenericService
									.serviceAction("POST", URI1, data)
									.then(
											function(response) {
												/*var quaryParam="?pesapal_transaction_tracking_id="+transactionId+"&pesapal_merchant_reference="+merchantId+"#";
												var refinedUrl=window.location.href.replace(quaryParam,"#");
												window.history.pushState(null, null, refinedUrl);*/
												if (response.data.object.transactionStatus == 'COMPLETED') {
													
												} else if (response.data.object.transactionStatus == 'PENDING') {
													console
															.log('payment is pending');
												} else if (response.data.object.transactionStatus == 'FAILED') {
													console
															.log('payment is failed');
												} else if(response.data.object.transactionStatus=='CONSUMED'){
					 								 growl.warning(
																'Already used ',
																{
																	title : 'Warning!'
																});
					 							}else {
													console
															.log('Invalid Transaction Id');
												}
												var tempAppointmentObj = JSON.parse(sessionStorage.getItem('tempAppointmentObjet'));
												if(tempAppointmentObj != undefined){
													tempAppointmentObj.isFromPesaPalSuccessPage='Y';
													tempAppointmentObj.pesapalPaymentStatus=response.data.object.transactionStatus;
													tempAppointmentObj.pesaPalAmount = response.data.object.amount;
													tempAppointmentObj.resTransactionId = response.data.object.transactionId;
													tempAppointmentObj.transactionType = response.data.object.transactionType;
													 sessionStorage.setItem('tempAppointmentObjet', JSON.stringify(tempAppointmentObj));
													 //$state.go('paymentInformation');
													// $location.search({});
													// $location.path('/paymentInformation');
													 //$state.go("paymentInformation",{}, {inherit: false})
													/*$state.go("paymentInformation", {
															patientAppoObjPayment:{},
														});*/
												}
												var baseUrl=location.protocol + "//" + location.hostname +
									               (location.port && ":" + location.port);
												 var redirectUrl = baseUrl + ROOT_URL+"/#/paymentInformation";
												 window.open(redirectUrl,"_self");
												$rootScope.stopLoader();
											});

						} ]);
