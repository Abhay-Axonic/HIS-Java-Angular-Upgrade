'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:logoutController
 * @description #logoutController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'logoutController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'$timeout',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies,$timeout) {
							 //code for inserting logout log details start	
							
							var cookies = $cookies.getAll();
							   $scope.tempUnitId = sessionStorage.getItem("unitId");
							   $scope.tempOrganizationId = sessionStorage.getItem("organizationId");
							   $scope.tempUserId = sessionStorage.getItem("userId");	
							   $rootScope.startLoader();
							   $rootScope.loginpage = false;
							   
							var cookieMacAdress =localStorage.getItem('macAddress');
							var cookieObject = $cookies.getObject('cookieObject');
							console.log("logout cookieMacAdress",cookieMacAdress);
							$scope.cookieDoctorId = cookieObject.doctorId;
							
							
							
									
								 if($rootScope.doctorCabinObject != undefined || $rootScope.doctorCabinObject != null)
									{
									var updObj = {
											doctorId : $scope.cookieDoctorId,
							    			cabinId : $rootScope.doctorCabinObject.cabinId,
									}
									
										var URI = BASE_URL + ROOT_URL+ "/api/global/unit/inactivePreviousDoctorInCabin";

									GenericService.serviceAction(METHOD_POST, URI, updObj, NOTIFICATION_MSG_STATUS_FALSE)
												.then( function(response) {
															
															if (response.data.status == "success")
																{
																
																}
																 
														});
									}
									
								
								 localStorage.removeItem('appOpen');
							
							
							   var data={
									   userId:cookieObject.userId,
										unitId:cookieObject.unitId,
										organizationId:cookieObject.organizationId,
									   isLogin:'N',
									   macAddress:cookieMacAdress
							   }								  
							   var URI = BASE_URL + ROOT_URL+ "/adt/saveUserLogDetails";
							   GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.tempUnitId=null;
												$scope.tempOrganizationId=null;
												$scope.tempUserId=null;
												
												//old code start
												sessionStorage.clear();
												  $scope.unitId = sessionStorage.getItem("unitId");
												   $scope.organizationId = sessionStorage.getItem("organizationId");
												   $scope.userId = sessionStorage.getItem("userId");
												   
												  
												 
												   var cookies = $cookies.getAll();
												   angular.forEach(cookies, function (v, k) {
													   
												       $cookies.remove(k);
												   });
												   // Phase A cookie-bloat migration: these keys moved from cookies to
												   // localStorage (shared across tabs by design, see COOKIE_BLOAT_MIGRATION_PLAN.md
												   // Group 2) and are no longer cleared by the $cookies.getAll() loop above.
												   // (2026-07-09 correction: the store/counter/nursing-station cluster moved
												   // back to sessionStorage per §4a's final cross-tab decision - see open
												   // question 11 - so it's no longer in this localStorage list; it's already
												   // covered by the unconditional sessionStorage.clear() above.)
												   angular.forEach([
												   	'mpesaRigths','labConfig','templateConfig','counterList',
												   	'rmoMappedOPDoctorList','rmoMappedIPWard','macAddress',
												   	'cookieObjectAppConfigSecond','isSkipPharmacySettlement'
												   ], function(k) {
												   	localStorage.removeItem(k);
												   });
												   sessionStorage.removeItem("permissions");
												   sessionStorage.removeItem("roleIds");
												    window.addEventListener('beforeunload', function() {
										                   localStorage.removeItem('appOpen');
										                   console.log('Tab closed, appOpen flag removed.');
										               });
												   if($scope.userId == null)
													   {
														   $rootScope.loginpage = false;
														   $state.go('login');														 
													   }
												   $rootScope.stopLoader();
												 //old code end
											}else{
												$scope.tempUnitId=null;
												$scope.tempOrganizationId=null;
												$scope.tempUserId=null;
												
												//old code start
												sessionStorage.clear();
												  $scope.unitId = sessionStorage.getItem("unitId");
												   $scope.organizationId = sessionStorage.getItem("organizationId");
												   $scope.userId = sessionStorage.getItem("userId");
												   
												  
												 
												   var cookies = $cookies.getAll();
												   angular.forEach(cookies, function (v, k) {
													   
												       $cookies.remove(k);
												   });
												   // Phase A cookie-bloat migration: these keys moved from cookies to
												   // localStorage (shared across tabs by design, see COOKIE_BLOAT_MIGRATION_PLAN.md
												   // Group 2) and are no longer cleared by the $cookies.getAll() loop above.
												   // (2026-07-09 correction: the store/counter/nursing-station cluster moved
												   // back to sessionStorage per §4a's final cross-tab decision - see open
												   // question 11 - so it's no longer in this localStorage list; it's already
												   // covered by the unconditional sessionStorage.clear() above.)
												   angular.forEach([
												   	'mpesaRigths','labConfig','templateConfig','counterList',
												   	'rmoMappedOPDoctorList','rmoMappedIPWard','macAddress',
												   	'cookieObjectAppConfigSecond','isSkipPharmacySettlement'
												   ], function(k) {
												   	localStorage.removeItem(k);
												   });
												   sessionStorage.removeItem("permissions");
												   sessionStorage.removeItem("roleIds");
												    window.addEventListener('beforeunload', function() {
										                   localStorage.removeItem('appOpen');
										                   console.log('Tab closed, appOpen flag removed.');
										               });
												   if($scope.userId == null)
													   {
														  
														   $state.go('login');														 
													   }
												   $rootScope.stopLoader();
											}
										});
							   
							   
							   
							   
							   
							   //code for inserting logout log details end
							   
						/*	sessionStorage.clear();
							  $scope.unitId = sessionStorage.getItem("unitId");
							   $scope.organizationId = sessionStorage.getItem("organizationId");
							   $scope.userId = sessionStorage.getItem("userId");
							   
							  
							 
							   var cookies = $cookies.getAll();
							   angular.forEach(cookies, function (v, k) {
								   
							       $cookies.remove(k);
							   });
							   sessionStorage.removeItem("permissions");
							   sessionStorage.removeItem("roleIds");
							   if($scope.userId == null)
								   {
								   $rootScope.loginpage = false;
								   $state.go('login');								   
								   
								   }
							  */
						} ]);
