'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:appointmentListController
 * @description #appointmentListController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'tokenMasterController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'growl',
						'$location',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,growl,$location) {

							
							
							$scope.getAS = function(){
								alert("b");
							$rootScope.$on('customEvent', function(event, message1) {
							      alert(message1);
							    });
						}
							/*$interval(function() {
								$scope.getAS();
								//alert('d');
					          }, 10000); */
							  
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
								
								$scope.tokenList = [];
								$rootScope.loginpage = false;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
							
							}
							
							$scope.init();
							$scope.getTokenList =function (){
//								alert();
								$scope.currentDate = new Date();

								$scope.commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										currentDate: moment($scope.currentDate).format("YYYY-MM-DD")
									};
								console.log("test", $scope.commonObj );
//								return false;
								
								var URI = BASE_URL + ROOT_URL+ "/api/opd/token/getTokenList";
								
									GenericService.serviceAction("POST", URI, $scope.commonObj).then(
										function(response) {
											$scope.tokenList = [];

											if (response.data.status == "success") 
											{
												$scope.tokenList = response.data.listObject;
												console.log("tokenList",$scope.tokenList);

											}
										});
							    }
							$scope.speech = function (theText) {
							      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
							  };
							  
							$scope.getTokenMessageList =function (){
//								alert();

								$scope.broadCastMsg = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId:1
									};
								console.log("test", $scope.commonObj );
//								return false;
								
								var URI = BASE_URL + ROOT_URL+ "/api/opd/token/getTokenMsgList";
								
									GenericService.serviceAction("POST", URI, $scope.broadCastMsg).then(
										function(response) {
											
											$scope.tokenObject = null;

											if (response.data.status == "success") 
											{
												if(response.data.listObject.length > 0){
												$scope.tokenObject = response.data.listObject[0];
												console.log("tokenList",$scope.tokenObject);
												$scope.speech($scope.tokenObject.message);
												}

											}
										});
							    }
							
							/*$scope.speech = function (theText) {
							      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
							  };
							  
							  
							$rootScope.$on('sendTokenScreenEvent', function(event, message) {
								  $scope.speech(message);
							    });*/
							
							
							
							
							
							//$interval($scope.getTokenList(), 3000); 
							
							/*$scope.speech(' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , thank you .');
							
							 $scope.speech = function (theText) {
							      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
							  };*/
						/*	$scope.speech = function (theText) {
							      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
							  };
							  
							$scope.speech(' Token number, 1 , please visit 1 , thank you .');
							
							$scope.speech(' Token number, 2 , please visit 2 , thank you .');*/
							
							$scope.startTokenList = $interval(function() {
								var path = $location.path();
								if(path == '/tokenDisplay'){
									$scope.getTokenList();
								}
								else{
									$interval.cancel($scope.startTokenList);
								}
								//alert('d');
					          }, 10000);
							
							$scope.startTokenMessageList = $interval(function() {
								var path = $location.path();
								if(path == '/tokenDisplay'){
								$scope.getTokenMessageList();
								}else{
									$interval.cancel($scope.startTokenMessageList);
								}
								
								//alert('d');
					          }, 2000);
							
								
						} ]);