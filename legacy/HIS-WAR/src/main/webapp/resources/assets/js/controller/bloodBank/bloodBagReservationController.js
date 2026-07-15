angular
		.module('myApp')
		.controller(
				'bloodBagReservationController',
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
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$stateParams) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.commonObj={
									
									"fromDate":"",
									"toDate":""
								}; 
							
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.searchKeyword = "";
							
							$scope.commonObj.toDate=new Date();
							$scope.commonObj.fromDate=new Date();
							
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
						
						//search  
						$scope.search = function() {
							$scope.startLoader();
						
							var data ={
									unitId:$scope.unitId,
									organizationId:$scope.organizationId,
									fromDate:moment($scope.commonObj.fromDate).format('YYYY-MM-DD'),
									toDate:moment($scope.commonObj.toDate).format('YYYY-MM-DD')
							};
							var URI = BASE_URL + ROOT_URL + "/api/bloodbank/crossMatch/getCrossMatchRegisteredOnBagReservation";

							GenericService
									.serviceAction(METHOD_POST, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												
												$scope.searchResultList = [];
												if (response.data.status == "success"){
													$scope.searchResultList = response.data.listObject;
													console.log("$scope.searchResultList",$scope.searchResultList);
													$scope.stopLoader();
												}
												else {
													$scope.stopLoader();
													growl
															.warning(
																	"No Result Found!",
																	{
																		title : ERROR_MSG
																	});
												}
											});
						}
						
						$scope.search();
						
						$scope.temporaryParam=[];
						$scope.unReserveBloodBag = function(info,chk){
							
							if(chk==1){
								
								$scope.temporaryParam.push(info);
								
							}else if(chk==0){
								$scope.temporaryParam.splice(info, 1);
							}
							console.log("$scope.info",info);
							console.log("$scope.temporaryParam",$scope.temporaryParam);
							console.log("chk",chk);
						}
						
						
						$scope.updateCrossMatching = function() {
							
							$scope.finalSaveList = [];
							angular.forEach($scope.temporaryParam,function(value,index) {
								var newArr = {};
								newArr.bloodBankStockId=value.bloodBankStockId,
								newArr.createdBy=value.createdBy,
								newArr.createdDate=value.createdDate+" 00:00:00",
								newArr.crossMatchRegisterId=value.crossMatchRegisterId,
								newArr.organizationId=value.organizationId,
								newArr.patientRequisitionId=value.patientRequisitionId,
								newArr.unitId=value.unitId,
								newArr.crossMatchRegisterDetailsId=value.crossMatchRegisterDetailsId
								$scope.finalSaveList.push(newArr);
							});
							
							
							if($scope.finalSaveList.length>0){
								$scope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/bloodbank/crossMatch/updateCrossMatchRegisteredOnBagReservation";

							GenericService.serviceAction(METHOD_POST, URI,$scope.finalSaveList,NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												
												$scope.searchResultList = [];
												if (response.data.status == "success"){
													$scope.stopLoader();
													growl.success(
															"Updated Successfully!",
															{
																title : SUCCESS_MSG
															});
												}
												else {
													$scope.stopLoader();
													growl
															.warning(
																	"No Result Found!",
																	{
																		title : ERROR_MSG
																	});
												}
											});
							
							}else{
								
								growl
								.warning(
										"Please Select Atleast One Record!",
										{
											title : ERROR_MSG
										});
							}
						}
						
							
}]);			
					