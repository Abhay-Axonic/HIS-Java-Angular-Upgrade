angular
		.module('myApp')
		.controller(
				'donorScreeningWorklistController',
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
									"genderId"        : "",
									"mobileNumber"        : "",
									"dob"       : "",
									"identificationNumber":"",
									"identificationTypeId":"",
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
							$scope.searchKeyword = "";
							
							$scope.donorSearchObj.toDate=new Date();
							$scope.donorSearchObj.fromDate=new Date();
							console.log("$scope.donorSearchObj.fromDate---",$scope.donorSearchObj.fromDate);
								 
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
								var URI = BASE_URL + ROOT_URL + "/api/bloodbank/getDonorRegisttrationAndScreeningDetails";

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
								console.log("info",info);
								//$scope.uhidNumber = info.uhidNumber;
								if(info.screeningResultStatusId != 3){
								$state.go('donorScreening',
										{
											donorObjResult: info
										});
								
								}else {
									
									growl.warning(
													"This donor has been permanently rejected!",
													{
														title : ERROR_MSG
													});
									
								}
							}
							
			}]);
							