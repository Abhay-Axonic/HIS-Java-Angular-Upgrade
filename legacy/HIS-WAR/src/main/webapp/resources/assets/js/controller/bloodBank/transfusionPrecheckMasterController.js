angular
		.module('myApp')
		.controller(
				'transfusionPrecheckMasterController',
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
						'PagerService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,PagerService) {
							
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
							
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var data1 = {
									      	organizationId:organizationId,
											offset:offset,
											noOfRecordsPerPage:noOfRecordsPerPage,
											unitId:unitId
											};
								
								var data2 = {
										      organizationId:organizationId,
										      unitId:unitId
											};
								
								var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/precheckMaster/getBloodTransfusionPrecheckMasterListPagination"; 
								var URI2 = BASE_URL + ROOT_URL + "/global/bloodBank/precheckMaster/getBloodTransfusionPrecheckMasterListCount";
									
								
								GenericService.serviceAction("POST",URI1,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.precheckList = response.data.listObject;
									console.log("commonList",$scope.commonList);
									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){					
										$rootScope.stopLoader();
										$scope.commonListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								console.log("offset", offset);
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var data = {
										organizationId:organizationId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										unitId:unitId
										};
								
								var URI = BASE_URL + ROOT_URL +  "/global/bloodBank/precheckMaster/getBloodTransfusionPrecheckMasterListPagination";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									$rootScope.stopLoader();
									$scope.precheckList = response.data.listObject;
									console.log("commonList",$scope.commonList);
								});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							/*$scope.getprecheckList = function(){
								var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
										};								
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/precheckMaster/getBloodTransfusionPrecheckMasterList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											//$scope.stateList = [];
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.precheckList = response.data.listObject;
													console.log("$scope.precheckList", 	$scope.precheckList);
											}
										});
						}
						$scope.getprecheckList();*/
							
							$scope.precheck ={
									"transfusionPrecheckMasterId":null,
									"code":"",
									"description":"",
									"createdBy":$scope.userId,
									"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"status":"A",
									"updatedBy":$scope.userId,
									"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"unitId":$scope.unitId ,
									"organizationId":$scope.organizationId
							}
						
							$scope.savePrecheck = function(){
								if($scope.precheck.code == undefined || $scope.precheck.code == ""){
									growl.error("Please Enter Code ",
											{
												title : 'error'
											});
									return false;
								}
								if($scope.precheck.description == undefined || $scope.precheck.description == ""){
									growl.error("Please Enter Description ",
											{
												title : 'error'
											});
									return false;
								}
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/global/bloodBank/precheckMaster/saveBloodTransfusionPrecheckMaster";
								console.log("$scope.precheck", 	$scope.precheck);
								GenericService.serviceAction("POST", URI,$scope.precheck).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
										$scope.precheck.code="";
										$scope.precheck.description="";
										$scope.getprecheckList();
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
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
							}
							
							$scope.updateStatus = function(transfusionPrecheckMasterId,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											transfusionPrecheckMasterId :transfusionPrecheckMasterId,
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/bloodBank/precheckMaster/updateBloodTransfusionPrecheckMasterStatus";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.getprecheckList();
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
							
							$scope.precheck.transfusionPrecheckMasterId = info.transfusionPrecheckMasterId;
							
							$scope.precheck.code = info.code;
							
							$scope.precheck.description = info.description;
							
							$scope.precheck.status = info.status;
						}	
							
			}]);
							