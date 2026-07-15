'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:cabinDisplayController
 * @description #cabinDisplayController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'cabinDisplayController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericServiceForWithoutLogin',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'growl',
						'orgId',
						'unitId',
						'doctorId',

						function($scope, $cookies, $rootScope, GenericServiceForWithoutLogin,
								BillingGenericService, $state, $timeout,$interval,growl,orgId,unitId,doctorId) {

							
							/* init() function for form object create. */
							$scope.init = function() {

								$scope.unitId = unitId;
								$scope.organizationId = orgId;
								$scope.doctorId = doctorId;
								
								$scope.tokenList = [];
								$rootScope.loginpage = false;
								
								$scope.unitBillingAppConfig = {};
								var URI = BASE_URL + ROOT_URL
								+ "/api/unit/applicationConfiguration/billing/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
								GenericServiceForWithoutLogin.serviceAction(METHOD_GET, URI,"",
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success")
												{
													$scope.unitBillingAppConfig = response.data.object;
												}
												
										});
								

							}
							
							$scope.init();

							$scope.getCoEmpVerificationReq = function(){
								try{
									var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
									var obj = {
												organizationId : $scope.organizationId,
												unitId         :$scope.unitId
										    }
									GenericServiceForWithoutLogin.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.unitConfigurationObject = response.data.listObject[0];
											$scope.tokenNumberReq = $scope.unitConfigurationObject.tokenNumber;
											$scope.init();
										}
									});
								}catch (e) {
									console.log("Exception",e.message);
								}
							}
							$scope.getCoEmpVerificationReq();


							 $scope.firstImg;
                            $scope.imageBackground = function(){
                                            var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/3"
                                                GenericServiceForWithoutLogin.serviceAction("GET", URI).then(function(response) {
                                                if(response.data.object != null){
                                                $scope.firstImg=response.data.object.imageURL;
                                                console.log($scope.firstImg);
                                                }
                                                });
                                            }
                                         $scope.imageBackground();
							
							 $scope.tokenList15 = [];
							 $scope.tokenList30 = [];
							 $scope.tokenList45 = [];
							 $scope.tokenListDisplay=[];
							$scope.getCabinDoctorList =function (){
//								alert();
								$scope.currentDate = new Date();

								$scope.commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										currentDate: moment($scope.currentDate).format("YYYY-MM-DD"),
										doctorId : $scope.doctorId,
								};
								console.log("test", $scope.commonObj );
//								return false;
								
								var URI = BASE_URL + ROOT_URL+ "/api/opd/token/getDoctorTokenList";
								
								GenericServiceForWithoutLogin.serviceAction("POST", URI, $scope.commonObj).then(
										function(response) {
											$scope.tokenList = [];
											
											 $scope.count = 0;
											if (response.data.status == "success") 
											{
												
												if(response.data.listObject != null){
													
													angular.forEach(response.data.listObject,function(value,index){
														if(value.doctorDeskArrival!=null && value.doctorDeskArrival == 'N' && value.doctorLatestCall == true){
														    $scope.tokenList.push(value);
														}
													}); 
													
													angular.forEach(response.data.listObject,function(value,index){
														if(value.doctorDeskArrival!=null && value.doctorLatestCall == false && value.doctorDeskArrival =='N'){
														    $scope.tokenList.push(value);
														}
													});
													
													angular.forEach(response.data.listObject,function(value,index){
														if(value.doctorDeskArrival==null){
														    $scope.tokenList.push(value);
														}
													});
												
												}
												
												$scope.tokenList15 = [];
												 $scope.tokenList30 = [];
												 $scope.tokenList45 = [];
												 $scope.tokenListDisplay=[];
												
												if($scope.tokenNumberReq == 'M'){
													angular.forEach($scope.tokenList,function(value,index){
														if(index < 16){
														    $scope.tokenList15.push(value);
														}
														if(index > 15 && index < 32 ){
														    $scope.tokenList30.push(value);
														}
														if(index > 32 ){
														    $scope.tokenList45.push(value);
														}
														if(value.cabinDesc != undefined && value.cabinDesc != null && value.cabinDesc != ''){
															$scope.tokenListDisplay.push(value);
														}													
													}); 
													$scope.count = $scope.tokenList.length;
												}
												

											}
										});
							    }
						
						/*$scope.demo = function(){
							$scope.count = 0;
							$scope.tokenList = [];
							$scope.tokenList15 = [];
							 $scope.tokenList30 = [];
							 $scope.tokenList45 = [];
							 $scope.tokenListDisplay = [];
							for (let i = 0; i < 45; i++) {
								 
								var obj = {};
								obj.appointmentTypeId = 1
								//obj.cabinDesc= "Cabin - 1";
								obj.doctorDeskArrival="N";
								obj.doctorLatestCall = true;
								obj.doctorName ="Suresh  Borkar";
								obj.patientFullName = "Ramesh   Jadhav";
								obj.patientName = "Mr. Ramesh   Jadhav";
								$scope.tokenList.push(obj);
								
								}
							
						
							
							angular.forEach($scope.tokenList,function(value,index){
								if(index < 16){
								    $scope.tokenList15.push(value);
								}
								if(index > 15 && index < 32 ){
								    $scope.tokenList30.push(value);
								}
								if(index > 32 ){
								    $scope.tokenList45.push(value);
								}
								if(value.cabinDesc != undefined && value.cabinDesc != null && value.cabinDesc != ''){
									$scope.tokenListDisplay.push(value);
								}
							}) 
							
							$scope.count = $scope.tokenList.length;
							
						}*/
							
							
							
						//$scope.demo();
							$interval(function() {
								$scope.getCabinDoctorList();
								//$scope.demo();
					          }, 10000);
								
						} ]);