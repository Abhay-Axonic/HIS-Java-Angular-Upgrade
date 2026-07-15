angular
		.module('myApp')
		.controller(
				'mpesaApplicationConfigurationController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.mpesaObjectList =[];
							$scope.getmpesaList = function(){
								$rootScope.startLoader();
								var data = {
										organizationId:$scope.organizationId
										};								
								var URI = BASE_URL + ROOT_URL+ "/api/global/unit/getMpesaAppConfigList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.mpesaObjectList = response.data.listObject;
													console.log("$scope.mpesaObjectList", 	$scope.mpesaObjectList);
											}
										});
						}
						$scope.getmpesaList();
						
						$scope.unitList = [];
						$scope.getUnit = function () {
							var data = {
								organizationId: $scope.organizationId
							};
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + UNITLIST;
							GenericService.serviceAction("POST", URI, data).then(function (response) {
								if (response.data.status == "success") {
									$scope.unitList = response.data.listObject;
									$rootScope.stopLoader();
								}
								else {
									$rootScope.stopLoader();
								}
							});
						}
						$scope.getUnit();
							$scope.mpesaObj ={
									"mpesaApplicationConfigurationId":null,
									"businessShortcode":"",
									"stkPushPassword":"",
									"partyB":"",
									"appKey":"",
									"appSecretKey":"",
									"terminalMsisdn":"",
									"validationPassword":"",
									"createdBy":$scope.userId,
									"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"status":"A",
									"updatedBy":$scope.userId,
									"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"unitId":"",
									"organizationId":$scope.organizationId
							}

							$scope.saveMpesa = function(){
								$rootScope.startLoader();
								
								if($scope.mpesaObj.unitId ==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Select Hospital",
											{
												title : 'warning!'
											});
								}else if($scope.mpesaObj.businessShortcode ==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Enter Business Shortcode",
											{
												title : 'warning!'
											});
								}else if($scope.mpesaObj.partyB==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Enter Party B",
											{
												title : 'warning!'
											});
								}else if($scope.mpesaObj.terminalMsisdn==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Enter Terminal MSISDN",
											{
												title : 'warning!'
											});
								}else if($scope.mpesaObj.validationPassword==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Enter Validation Password",
											{
												title : 'warning!'
											});
								}else if($scope.mpesaObj.stkPushPassword==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Enter STK Push Password ",
											{
												title : 'warning!'
											});
								}else if($scope.mpesaObj.appKey==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Enter App Key",
											{
												title : 'warning!'
											});
								}else if($scope.mpesaObj.appSecretKey==""){
									
									$rootScope.stopLoader();
									growl.warning("Please Enter App Secret Key",
											{
												title : 'warning!'
											});
								}else{
								var URI = BASE_URL + ROOT_URL +"/api/global/unit/saveMpesaAppConfig";
								console.log("$scope.mpesaObj", 	$scope.mpesaObj);
								GenericService.serviceAction("POST", URI,$scope.mpesaObj).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
										$scope.mpesaObj.appKey = "";
										$scope.mpesaObj.appSecretKey = "";
										$scope.mpesaObj.businessShortcode = "";
										$scope.mpesaObj.mpesaApplicationConfigurationId = "";
										$scope.mpesaObj.partyB = "";
										$scope.mpesaObj.unitId = "";
										$scope.mpesaObj.stkPushPassword = "";
										$scope.mpesaObj.terminalMsisdn ="";
										$scope.mpesaObj.validationPassword = "";
										$scope.getmpesaList();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}else if(response.data.status == 'Already Exists.')
									{
										$rootScope.stopLoader();
										$scope.mpesaObj.appKey = "";
										$scope.mpesaObj.appSecretKey = "";
										$scope.mpesaObj.businessShortcode = "";
										$scope.mpesaObj.mpesaApplicationConfigurationId = "";
										$scope.mpesaObj.partyB = "";
										$scope.mpesaObj.status = "";
										$scope.mpesaObj.stkPushPassword = "";
										$scope.mpesaObj.terminalMsisdn ="";
										$scope.mpesaObj.validationPassword = "";
										$scope.getmpesaList();
										
										growl.error("You Have Already Configured Mpesa Credentials",
												{
													title : 'Error!'
												});
										
									}
								});
								}
							}
							
							$scope.updateStatus = function(mpesaApplicationConfigurationId,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											mpesaApplicationConfigurationId :mpesaApplicationConfigurationId,
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateMpesaAppConfigStatus";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.getmpesaList();
											$rootScope.stopLoader();
											
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
								}catch(e){
									console.log("Exception",e);
								}
							}
							
						$scope.update = function(info){
							$rootScope.startLoader();
							$scope.mpesaObj.appKey = info.appKey;
							$scope.mpesaObj.appSecretKey = info.appSecretKey;
							$scope.mpesaObj.businessShortcode = info.businessShortcode;
							$scope.mpesaObj.mpesaApplicationConfigurationId = info.mpesaApplicationConfigurationId;
							$scope.mpesaObj.partyB = info.partyB;
							$scope.mpesaObj.status = info.status;
							$scope.mpesaObj.stkPushPassword = info.stkPushPassword;
							$scope.mpesaObj.terminalMsisdn = info.terminalMsisdn;
							$scope.mpesaObj.validationPassword = info.validationPassword;
							$scope.mpesaObj.unitId = info.unitId;
							$rootScope.stopLoader();
						}	
							
			}]);
							