angular
		.module('myApp')
		.controller(
				'transfusionTransmittedDiseasesController',
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
							
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.searchKeyword = "";
							
							$scope.comObj={
									fromDate:""	,
									toDate:"",
									bloodBagNo:""
							};
							
							$scope.comObj.fromDate = new Date();
							$scope.comObj.toDate = new Date();
							
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
							
							//search Details 
							$scope.fDate="";
							$scope.tDate="";
							$scope.searchDetails = function()
							{
								$scope.startLoader();
								if(($scope.comObj.fromDate=="") && ($scope.comObj.toDate=="")){
									$scope.fDate=$scope.comObj.fromDate;
									$scope.tDate=$scope.comObj.toDate;
								}
								else
									{
									$scope.fDate = moment($scope.comObj.fromDate).format('YYYY-MM-DD');
									$scope.tDate = moment($scope.comObj.toDate).format('YYYY-MM-DD');
									}
								
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										bloodBagNo:$scope.comObj.bloodBagNo,
										fromDate:$scope.fDate,
										toDate:$scope.tDate
								};
								var URI = BASE_URL + ROOT_URL + SEARCH_BAG_DETAILS;

								GenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {

													$scope.searchDonorList = [];
													if (response.data.status == "success") {
														$scope.searchList = response.data.listObject;
														console.log("$scope.searchList",$scope.searchList);
														$scope.comObj.bloodBagNo = "";
														$scope.stopLoader();
													
													} else {
														$scope.stopLoader();
														growl.warning("No Result Found!",
																		{
																			title : ERROR_MSG
																		});
													}
												});
							}
							$scope.searchDetails();
							//get Blood Bag  Details
							$scope.getBloodBagDetails = function(info){
								if(info.transfusionTransmittedDiseasesStatus == 5)
								{
									growl.error("Blood Bag Has been Discarded",
											{
											title: 'Error!'
											});
								}else{
								$state.go('detailsTransfusionTransmittedDiseases',
										{
											bloodBagObjResult: info
										});
								}
							}
							
			}]);
							