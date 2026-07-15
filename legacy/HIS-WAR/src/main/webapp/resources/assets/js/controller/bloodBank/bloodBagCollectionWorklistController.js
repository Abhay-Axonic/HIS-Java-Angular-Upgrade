angular
		.module('myApp')
		.controller(
				'bloodBagCollectionWorklistController',
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
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.donorSearchObj = {
									"donorNumber"      : "",
									"unitId"          :$scope.unitId,
									"organizationId"  :$scope.organizationId,
									"donorName":"",
									"fromDate":"",
									"toDate":""
									
							}
							
						
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.searchKeyword="";
								
				
							$scope.donorSearchObj.toDate=new Date();
							$scope.donorSearchObj.fromDate=new Date();
							
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
							
							//search Donor 
							$scope.fDate="";
							$scope.tDate="";
							$scope.searchDonor = function() {
								$scope.startLoader();
								if(($scope.donorSearchObj.fromDate=="") && ($scope.donorSearchObj.toDate=="")){
									$scope.fDate=$scope.donorSearchObj.fromDate;
									$scope.tDate=$scope.donorSearchObj.toDate;
								}
								else
									{
									$scope.fDate = moment($scope.donorSearchObj.fromDate).format('YYYY-MM-DD');
									$scope.tDate = moment($scope.donorSearchObj.toDate).format('YYYY-MM-DD');
									}
								var data ={
										donorNumber:$scope.donorSearchObj.donorNumber,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										donorName:$scope.donorSearchObj.donorName,
										fromDate:$scope.fDate,
										toDate:$scope.tDate	
								};
								var URI = BASE_URL + ROOT_URL +"/api/bloodbank/getDonorAndScreeningAndBagCollectionDetail";

								GenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													
													$scope.searchDonorList = [];
													if (response.data.status == "success"){
														$scope.searchDonorList = response.data.listObject;
														console.log("$scope.searchDonorList",$scope.searchDonorList);
														$scope.donorSearchObj.donorNumber="";
														$scope.donorSearchObj.donorName="";
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
							
							$scope.searchDonor();
							//get Donor Details
							$scope.getDonorDetails = function(info){
							//	$scope.donorSearchObj.uhidNumber = info.uhidNumber;
								//$scope.uhidNumber = info.uhidNumber;
								
								if(info.bloodBagCollectionStatusId != 5){
								$state.go('bloodBagCollection',
										{
											bloodBagCollectionResult: info
										});
								
								}else {
									$scope.stopLoader();
									growl
											.warning(
													"Blood bag has been discarded !",
													{
														title : ERROR_MSG
													});
								}
							}
							
			}]);
							