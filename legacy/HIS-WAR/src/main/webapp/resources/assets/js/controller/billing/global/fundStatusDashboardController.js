/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"fundStatusDashboardController",
				[	    	'$scope',
							'$http',
							'$localStorage',
							'$sessionStorage',
							'$cookies',
							'$rootScope',
							'GenericService',
							'PagerService',
							'promiseFactory',
							'BillingGenericService',
							'GenericServiceFileUpload',
							'reportService',
							'growl',
							'$state',
							'$stateParams',
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,
								$stateParams,$modalInstance) {

							$rootScope.loginpage = true;
						    $scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;

						
							/** * Check user is Loggedin */
							var cookieObject, cookieObjectAppConfig;
				            [cookieObject, cookieObjectAppConfig] = $scope.checkCookies();
							
						
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							console.info($scope.orgId)
								var data = {};
							//$(".selectedPageName").text("Machine Master");
							//Date For From Date on Search
							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.trustObj = {
									"trustId" : '',
									"trustName" : '',
									"trustAddress" : '',
									"countryId" :'',
									"stateId":'',
									"districtId" : '',
									"cityId" : '',
									"areaId" : '',
									"zipCode" : '',
									"contactPersonName" :'',
									"contactPersonEmail":'',
									"phoneNo" : '',
									"cellNo" : '',
									"validFrom" : '',
									"patientCategoryId" : '',
									"patientTypeId" : '',
									"disease" :'',
									"maxLimitPerPerson":'',
									"trustStatus" : "A",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"toDate" :'',
									"fromDate" :'',
									"visitTypeId":[]
								}
								
							};
							$scope.initCommon();

							
							$scope.openDatePickerFromDate = function() {
								$scope.datepickerFromDate.opened = true;
							};

							$scope.datepickerFromDate = {
								opened : false
							};

						
					
					$scope.getTrustActiveList = function() {

						var URI = BASE_URL+ ROOT_URL+ "/api/billing/getActiveTrustList";
						var data = {
                                      orgId : $scope.orgId ,
								      unitId:$scope.unitId
								    
								   }
						GenericService.serviceAction("POST", URI, data).then(function(response) {

									$rootScope.stopLoader();
									$scope.getTrustActiveList = [];
									$scope.getTrustActiveList = response.data.listObject;
	                                
	                               

								});
					}
					$scope.getTrustActiveList();
					
					
							//Get trust List
						$scope.getTrustList = function() {
				        var URI = BASE_URL + ROOT_URL +"/api/billing/getTrustList/"+$scope.orgId +"/"+$scope.unitId;
				    GenericService
				        .serviceAction(METHOD_GET, URI,
				                NOTIFICATION_MSG_STATUS_FALSE)
				        .then(
				                function(response) {
				                    console.log(response);
				                    if (response.data.status == "success"){
				                        $scope.trustList = response.data.listObject;
				                    }
				                    
				                });

						}
						$scope.getTrustList();
						
						/*get collect fund list credit*/
						
						$scope.getTrustfundDetails= function(trustId){
							var data = {
									  trustId : trustId,
									  orgId : $scope.orgId ,
								      unitId:$scope.unitId
								};
						    var URI = BASE_URL + ROOT_URL +"/api/billing/getFundCollectionListByTrust/"+$scope.orgId +"/"+$scope.unitId;
						    GenericService
						        .serviceAction(METHOD_POST, URI,
						                data)
						        .then(
						                function(response) {
						                    if (response.data.status == "success"){
						                    	
						                        $scope.CollectList = response.data.listObject;
						                        console.log("list",$scope.CollectList);
						            
						                    }
						                   
						                });
						
							
						}

						$scope.openDatePicker = function() { 
							$scope.datepicker.opened = true;
						};
						$scope.datepicker = {
								opened : false
							};
						// to and from date picker
						$scope.trustObj.toDate= new Date();
						$scope.trustObj.fromDate= new Date();
						$scope.openDatePicker1 = function() { 
							$scope.datepicker1.opened = true;
						};
						$scope.datepicker1 = {
								opened : false
							};
						$scope.openDatePicker2 = function() { 
							$scope.datepicker2.opened = true;
						};
						$scope.datepicker2 = {
								opened : false
							};

							//Get CollectionList
							 $scope.CollectList=[];
							$scope.collectionList = function(toDate,fromDate,trustId) {
								$rootScope.startLoader();
								    var dateTo= moment(toDate).format('YYYY-MM-DD');
									var dateFrom=  moment(fromDate).format('YYYY-MM-DD');
								var data={
									toDate:dateTo,
									fromDate:dateFrom,
									orgId:$scope.orgId,
									unitId:$scope.unitId,
									trustId:trustId			
								};			
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getFundCollectionListByTrust/"+$scope.orgId +"/"+$scope.unitId;
					     //   var URI = BASE_URL + ROOT_URL +"/api/billing/getCollectionList/"+$scope.orgId +"/"+$scope.unitId;
					    GenericService
					        .serviceAction(METHOD_POST, URI,
					                data)
					        .then(
					                function(response) {
					                    if (response.data.status == "success"){
					                        $scope.CollectList = response.data.listObject;
					                        var amount=0;
											angular.forEach( $scope.CollectList,function(value,key){
												amount = amount + parseFloat(value.amount); 
												$scope.totalAmount=amount;
											});
											$rootScope.stopLoader();
					                    }
					                   
					                });

						
							}
							
							$scope.collectionList();
							
			
				}
		
				
				]);