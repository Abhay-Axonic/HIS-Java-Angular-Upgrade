angular
		.module('myApp')
		.controller(
				'detailsLoanInApprovalBloodBankController',
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
							$scope.common = $stateParams.loanInDetailObj;
								
							console.log("common",$scope.common);
							$scope.searchList=[];
							$scope.searchDetails = function(){
								
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										loanNumber:$scope.common.loanInNumber,
										loanInId:$scope.common.loanInId
								};
								$scope.searchList=[];
								$rootScope.startLoader();
								var URI1 = BASE_URL + ROOT_URL + "/api/bloodbank/stock/searchLoanInDetails"; 
								GenericService.serviceAction("POST",URI1,data).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.searchList = response.data.listObject;
									console.log("searchList",$scope.searchList);
									}
								});

							}
							$scope.searchDetails();
							$scope.statusList=[];
							$scope.finalSaveList=[];
							$scope.status="";
							$scope.addObjToList=function(info,ckt){
								
								if(ckt==1){
										
									angular.forEach($scope.statusList,function(value,key){
											
											if(info.bloodBankStockId==value.bloodBankStockId){
												$scope.status=value.stockStatusId;
											}
		
										});
										
										var newData={};
										newData.bloodBankStockId=info.bloodBankStockId;
										newData.stockStatusId=$scope.status;
										newData.updatedDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newData.updatedBy=$scope.userId;
										newData.tTDCheck=info.tTDCheck;
										
										$scope.finalSaveList.push(newData);
										$scope.status="";
										console.log("$scope.finalSaveList",$scope.finalSaveList);
								
								}else{
									 var index = $scope.finalSaveList.indexOf(info.bloodBankStockId);
									 $scope.finalSaveList.splice(index,1);
									 console.log("$scope.finalSaveList",$scope.finalSaveList);
								}
							}
							$scope.updateStatus= function(info,loanInStatus){
								
								var newDa={};
								newDa.bloodBankStockId=info.bloodBankStockId;
								newDa.stockStatusId=loanInStatus;
								$scope.statusList.push(newDa);
								
								$scope.flag=false;
								angular.forEach($scope.finalSaveList,function(value,key){
									
									if(value.bloodBankStockId==info.bloodBankStockId){
										$scope.flag=true;
										$scope.finalSaveList[key].stockStatusId=loanInStatus;
									}

								});
								
								console.log("$scope.finalSaveList",$scope.finalSaveList);
								
							}
							
							
							$scope.updateLoanInStatus = function()
							{
								$scope.flag=false;
								angular.forEach($scope.finalSaveList,function(value,key){
									
									if(value.stockStatusId==""){
										$scope.flag=true;
										
									}

								});
								if($scope.finalSaveList.length>0){
										
									if($scope.flag!=true){
											$rootScope.startLoader();
											console.log("$scope.finalSaveList",$scope.finalSaveList);
											var URI = BASE_URL + ROOT_URL+ "/api/bloodbank/stock/updateLoanInStatus";
												GenericService
												.serviceAction(METHOD_POST, URI,$scope.finalSaveList )
												.then(
														function(response) {
			
															if (response.data.status == "success") {
																$scope.finalSaveList=[];
																$scope.searchList=[];
																$rootScope.stopLoader();
																growl.success(response.data.message,
																		{
																			title : 'success!'
																		});
																$scope.common={};
																$scope.loanInStatus="";
															}else if (response.data.status == "error") {
																$rootScope.stopLoader();
																growl.error(response.data.message,
																		{
																			title : 'error!'
																		});
																$scope.loanInStatus="";
															}
														});
										}else {
											
											growl.warning("Please Select Action of all Selected Check Box",
													{
														title : 'warning!'
													});
										}
								}else{

									growl.warning("Please Select Atleast One Check Box",
											{
												title : 'warning!'
											});
								}
							}
			}]);
							