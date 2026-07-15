angular
		.module('myApp')
		.controller(
				'accountPayableMasterController',
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
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage ,PaginationService) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
						
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
								
				
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};
							
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}

							$scope.pagination = {
				                    currentPage: 1,
				                    totalPages: 0,
				                    pageSize: '10',
				                    search: '',
				                    searchData: PaginationService.searchData,
				                    setTotalPages: PaginationService.setTotalPages,
				                    getPagesList: PaginationService.getPagesList,
				                    setPage: PaginationService.setPage,
				                    setFirstPage: PaginationService.setFirstPage,
				                    setPreviousPage: PaginationService.setPreviousPage,
				                    setNextPage: PaginationService.setNextPage,
				                    setLastPage: PaginationService.setLastPage,
				                    sortBy: PaginationService.sortBy
				                  }
							
							$scope.initAccountPayable = function(){
								$scope.accountPay = { 
										"accPayableMstId" :0,
										"accCode" : "",
										"accDesc" : "",
										"accPayableCode" :"",
										"accEmail":"",
										"accMobile" : "",
										"accDay" : "",
										"accWorkFromTime" : "",
										"accWorkToTime" : "",
										"accValidityFromDate" : "",
										"accValidityToDate" :"",
										"accCreditLimit" :"",
										"accPaymentTerms":"",
										"accAddress" : "",
										"accCountryId" : "",
										"accStateId" : "",
										"accCityId" : "",
										"accAreaId" : "",
										"accZipcode" :"",
										"accDistrictId":"",
										"accContactNumber":"",
										"status" : "A",
										"deptId" : "",
										"subDeptId" : "",
										"orderId" : "",
										"createdBy" : $scope.userId,
										"createdDate" : "",
										"updatedBy" : $scope.userId,
										"updatedDate" : "",
										"orgId" : $scope.organizationId,
										"orgUnitId" : $scope.unitId
								}
							}
							
							$scope.initAccountPayable();
						
							$scope.getCountryList = function() {
								var data = {};

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST + $scope.organizationId;
								GenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.countryList = [];
													if (response.data.status == "success")
														$scope.countryList = response.data.listObject;
													console
															.log($scope.countryList)
												});
							}
							$scope.getCountryList();
							// get calling code by using country id start
							/*$scope.getCallingCode = function(data) {
								var URI = BASE_URL + ROOT_URL
										+ GETSINGLECOUNTRY;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														

													} else {
														alert("Error!!");
													}
												});
							}*/
							
							// get nationality code by using country id end
							$scope.getStateByCountry = function(id) {
								console.info(id);
								var data = {
									countryId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETSTATELISTBYCOUNTRYID;
								GenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.stateList = [];
														$scope.stateList = response.data.listObject;
														console.log("$scope.stateList",$scope.stateList);
													}
												});
							}
							// get discrict
							
							$scope.getDistrictByState = function(id, type) {
								var data = {
									stateId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDISTRICTLISTBYSTATEID;
								GenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.districtList = [];
														$scope.districtList = response.data.listObject;

													}
												});

							}
							
							$scope.getCityByDistrict = function(id) {

								var data = {
									districtId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETCITYLISTBYDISTRICTID;
								GenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.cityList = [];
														$scope.cityList = response.data.listObject;

													}

												});

							}

							$scope.getAreaByCity = function(id) {

								var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETAREALISTBYCITY;
								GenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.areaList = [];
														$scope.areaList = response.data.listObject;
													}

												});

							}
							
							$scope.saveAccountPay = function(){
								
								if($scope.accountPay.accCode == undefined || $scope.accountPay.accCode == null || $scope.accountPay.accCode == ""){
									growl.warning("Please Enter Code",
											{
												title : 'warning!'
											});
									return false;
								}
							
								if($scope.accountPay.accDesc == undefined || $scope.accountPay.accDesc == null || $scope.accountPay.accDesc == ""){
									growl.warning("Please Enter Description",
											{
												title : 'warning!'
											});
									return false;
								}
								
								if($scope.accountPay.accPayableCode == undefined || $scope.accountPay.accPayableCode == null || $scope.accountPay.accPayableCode == ""){
									growl.warning("Please Enter Account Payable Code",
											{
												title : 'warning!'
											});
									return false;
								}
								
								if($scope.accountPay.accEmail == undefined || $scope.accountPay.accEmail == null || $scope.accountPay.accEmail == ""){
									growl.warning("Please Enter Email",
											{
												title : 'warning!'
											});
									return false;
								}
								
								
								if($scope.accountPay.accMobile == undefined || $scope.accountPay.accMobile == null || $scope.accountPay.accMobile == ""){
									growl.warning("Please Enter Mobile",
											{
												title : 'warning!'
											});
									return false;
								}
								
								if($scope.accountPay.accDay == undefined || $scope.accountPay.accDay == null || $scope.accountPay.accDay == ""){
									growl.warning("Please Select Day",
											{
												title : 'warning!'
											});
									return false;
								}
								if($scope.accountPay.accCreditLimit == undefined || $scope.accountPay.accCreditLimit == null || $scope.accountPay.accCreditLimit == ""){
									growl.warning("Please Enter Credit Limit",
											{
												title : 'warning!'
											});
									return false;
								}
								
								if($scope.accountPay.accPaymentTerms == undefined || $scope.accountPay.accPaymentTerms == null || $scope.accountPay.accPaymentTerms == ""){
									growl.warning("Please Enter Payment Terms",
											{
												title : 'warning!'
											});
									return false;
								}

								if($scope.accountPay.accAddress == undefined || $scope.accountPay.accAddress == null || $scope.accountPay.accAddress == ""){
									growl.warning("Please Enter Address",
											{
												title : 'warning!'
											});
									return false;
								}
								$scope.accountPay.accValidityFromDate =moment($scope.accountPay.accValidityFromDate).format('DD-MM-YYYY HH:mm:ss');
								$scope.accountPay.accValidityToDate =moment($scope.accountPay.accValidityToDate).format('DD-MM-YYYY HH:mm:ss');
								$scope.accountPay.createdDate =moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								$scope.accountPay.updatedDate =moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								
								console.log("$scope.accountPay", $scope.accountPay);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/api/global/saveAccountPayable";
							
								GenericService.serviceAction("POST", URI,$scope.accountPay).then(function(response){
									if (response.data.status == 'success')
									{	
										$scope.getAccountPay();
										$rootScope.stopLoader();
										$scope.initAccountPayable();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										
									
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										$scope.initAccountPayable();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}else if(response.data.status == 'Already Exists.')
									{
										$rootScope.stopLoader();
										$scope.initAccountPayable();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
								
							}
							
							    
								$scope.accountList = [];
								$scope.getAccountPay = function(){
									var data = {
											orgId : $scope.organizationId,
											orgUnitId : $scope.unitId
											
									};
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/api/global/getAccountPayable";
							
								GenericService.serviceAction("POST", URI,data).then(function(response){
									if (response.data.status == 'success')
									{	
										$scope.pagination.data=[];
										$scope.accountList = response.data.listObject;
										
										 if($scope.accountList.length > 0){            
					                         PaginationService.searchData($scope.accountList, $scope.pagination);
					                       }
										console.log("$scope.accountList",$scope.accountList);
										
										
										$rootScope.stopLoader();
										
										
									}
								});
								
							}
								
							$scope.getAccountPay();
							$scope.updateStatus = function(id,status){
								var data = {
										accPayableMstId:id,
											status:status
								};
							
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL +"/api/global/updateAccountPayableStatus";
						
							GenericService.serviceAction("POST", URI,data).then(function(response){
								if (response.data.status == 'success')
								{	
									$scope.getAccountPay();
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
							
						}
							
							$scope.updateAccount=function(info){
								
								console.log("updateIfo",info);
								$scope.getStateByCountry(info.accCountryId);
								$scope.getDistrictByState(info.accStateId);
								$scope.getCityByDistrict(info.accDistrictId);
								$scope.getAreaByCity(info.accCityId);
								$scope.accountPay = { 
										"accPayableMstId" :info.accPayableMstId,
										"accCode" : info.accCode,
										"accDesc" :  info.accDesc,
										"accPayableCode" : info.accPayableCode,
										"accEmail": info.accEmail,
										"accMobile" : info.accMobile,
										"accDay" :info.accDay,
										"accWorkFromTime" :info.accWorkFromTime,
										"accWorkToTime" : info.accWorkToTime,
										"accValidityFromDate" :moment(info.accValidityFromDate,"DD-MM-YYYY").toDate(),
										"accValidityToDate" :moment (info.accValidityToDate,"DD-MM-YYYY").toDate(),
										"accCreditLimit" :info.accCreditLimit,
										"accPaymentTerms":info.accPaymentTerms,
										"accAddress" : info.accAddress,
										"accCountryId" :info.accCountryId,
										"accZipcode" :info.accZipcode,
										"accContactNumber":info.accContactNumber,
										"status" : info.status,
										"deptId" : info.deptId,
										"subDeptId" : info.subDeptId,
										"orderId" : info.orderId,
										"accStateId" : info.accStateId,
										"accCityId" : info.accCityId,
										"accAreaId" : info.accAreaId,
										"accDistrictId":info.accDistrictId,
										"createdBy" : $scope.userId,
										"createdDate" : "",
										"updatedBy" : $scope.userId,
										"updatedDate" : "",
										"orgId" : $scope.organizationId,
										"orgUnitId" : $scope.unitId
										
								}
								
							}
			}]);
							