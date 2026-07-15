angular
		.module('myApp')
		.controller(
				'detailsLoanOutBloodBankController',
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
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.test = false;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.common = $stateParams.loanOutDetailObj;
								
							console.log("common",$scope.common);
							$scope.searchKeyword = "";
							
							$scope.loanOutDetailsList = [];
							$scope.getLoanOutDetails = function(bloodBagLoanOutId){
								$scope.startLoader();
								$scope.loanOutDetailsList = [];
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										bloodBagLoanOutId : bloodBagLoanOutId,
								};
								var URI = BASE_URL + ROOT_URL + "/api/bloodbag/loanOut/getLoanOutDeatilsList";

								GenericService.serviceAction(METHOD_POST, URI, data)
										.then(function(response) {
													$scope.loanOutDetailsList = [];
													if (response.data.status == "success") {
														$scope.loanOutDetailsList = response.data.listObject;
														$scope.stopLoader();
													} else {
														$scope.stopLoader();
														growl.warning("No Result Found!",
																		{
																			title : ERROR_MSG
																		});
													}
												});
								$scope.stopLoader();
								
							}
							if($scope.common.bloodBagLoanOutId!=undefined){
								$scope.getLoanOutDetails($scope.common.bloodBagLoanOutId); 
							}
							
							
							
							
							$scope.selectAll = false;
							$scope.checkUncheckAll = function(){
								if($scope.loanOutDetailsList!=null && $scope.loanOutDetailsList.length>0){
									if($scope.selectAll){
										angular.forEach($scope.loanOutDetailsList,function(value,index){
											value.isSelected = true;
										});
									}else{
										angular.forEach($scope.loanOutDetailsList,function(value,index){
											value.isSelected = false;
										});
									}
								}
							}

							$scope.checkUncheck = function(){
								var cnt = 0;
								angular.forEach($scope.loanOutDetailsList,function(value,index){
									if(value.isSelected){
										cnt = cnt+1;
									}
								});
								if(cnt == $scope.loanOutDetailsList.length){
									$scope.selectAll = true;
								}else{
									$scope.selectAll = false;
								}
							}
							
							
							
							$scope.saveLoanOutApproval = function(){
								$scope.startLoader();
								$scope.finalLoanOutList = [];
								var loanOutObj ={
								unitId:$scope.unitId,
								organizationId:$scope.organizationId,
								bloodBagLoanOutId : $scope.common.bloodBagLoanOutId,
								updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								approvedBy : $scope.userId
								};

								if($scope.loanOutDetailsList!=null && $scope.loanOutDetailsList.length>0){
									var foundIsSelected = $scope.loanOutDetailsList.find(ar=>ar.isSelected);
									if(foundIsSelected == undefined)
									{
										growl.warning("Please Select Blood Bag for save !!",
												{
											title : 'Warning!'
												});
											$rootScope.stopLoader();
											return false;
									}

									angular.forEach($scope.loanOutDetailsList,function(value,key){
										if(value.isSelected){
											var newArr = {};
											newArr.bloodBagLoanOutDetailsId = value.bloodBagLoanOutDetailsId;
											newArr.loanOutStatusId = value.loanOutStatusId;
											newArr.bloodBankStockId = value.bloodBankStockId;
											newArr.bagCapacityId = value.bagCapacityId;
											newArr.bagTypeId = value.bagTypeId;
											newArr.bloodBankId = value.bloodBankId;
											newArr.bloodGroupId = value.bloodGroupId;
											newArr.componentId =  value.componentId;
											newArr.unitId = $scope.unitId;
											newArr.organizationId = $scope.organizationId;
											newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.updatedBy = $scope.userId;
											$scope.finalLoanOutList.push(newArr);
										}
									});
									
									loanOutObj.bloodBagLoanOutDetailsList = $scope.finalLoanOutList
									
								}else{
									growl.warning("Please select loan Out",
											{
											title : 'warning!'
											});
										$rootScope.stopLoader();
										return false;
									}


								var URI = BASE_URL + ROOT_URL + "/api/bloodbag/loanOut/loanOutListApproval";

								GenericService.serviceAction(METHOD_POST, URI, loanOutObj).then(function(response) {
										if (response.data.status == "success") {
											$scope.stopLoader();
											$state.go('loanOutBloodBankApproval');
										}
									});
									$scope.stopLoader();
								}
			}]);
							