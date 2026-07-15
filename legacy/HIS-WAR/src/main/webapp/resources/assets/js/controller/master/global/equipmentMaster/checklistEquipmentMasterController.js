angular
		.module('myApp')
		.controller(
				'checklistEquipmentMasterController',
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
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService ,PagerService) {
							
							$scope.init = function(){
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
								
								
								$scope.checklistObj ={
										"checklistEquipmentMasterId":null,
										"checklistCode":"",
										"checklistName":"",									
										"createdBy":$scope.userId,
										"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"status":"A",
										"updatedBy":$scope.userId,
										"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"unitId":$scope.unitId ,
										"organizationId":$scope.organizationId
								}
								
									var data = {
											unitId:$scope.unitId,
											organizationId:$scope.organizationId
											};								
									/*var URI = BASE_URL + ROOT_URL+ "/global/equipmentchecklist/getAllEquipmentChecklistMasterList";
									BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
												$scope.checklistEquipmentList = [];
												if (response.data.status == "success"){
													$rootScope.stopLoader();
														$scope.checklistEquipmentList = response.data.listObject;
														console.log("$scope.checklistEquipmentList", 	$scope.checklistEquipmentList);
												}
											});*/
								
							}
							$scope.init();
							
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
											var URI1 = BASE_URL + ROOT_URL + "/global/equipmentchecklist/getAllEquipmentChecklistMasterListPaginationList"; 
								var URI2 = BASE_URL + ROOT_URL + "/global/equipmentchecklist/getAllEquipmentChecklistMasterListPaginationListCount";
									
								
								GenericService.serviceAction("POST",URI1,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.checklistEquipmentList = response.data.listObject;
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
								
								var URI = BASE_URL + ROOT_URL +  "/global/equipmentchecklist/getAllEquipmentChecklistMasterListPaginationList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									$rootScope.stopLoader();
									$scope.checklistEquipmentList = response.data.listObject;
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
							
							
							$scope.saveChecklistEquipmentMaster = function(){
								$rootScope.startLoader();
								if($scope.checklistObj.checklistCode==""){
									$rootScope.stopLoader();
									growl.warning("Please select code",
											{
												title : 'warning!'
											});
								}else if($scope.checklistObj.checklistName==""){
									$rootScope.stopLoader();
									growl.warning("Please select name",
											{
												title : 'warning!'
											});
								}else{
								
								var URI = BASE_URL + ROOT_URL +"/global/equipmentchecklist/saveChecklistEquipmentMaster";
								console.log("$scope.checklistObj", 	$scope.checklistObj);
								GenericService.serviceAction("POST", URI,$scope.checklistObj).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
										$scope.checklistObj.checklistCode="";
										$scope.checklistObj.checklistName="";
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.init();
										$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
							}
							
							$scope.updateStatus = function(checklistEquipmentMasterId,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											checklistEquipmentMasterId :checklistEquipmentMasterId,
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/equipmentchecklist/updateChecklistEquipmentMasterStatus";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.init();
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
							
							$scope.clearChecklistEquipmentSearch = function(){
								$scope.searchKeyword = "";
								$scope.checklistObj.checklistCode="";
								$scope.checklistObj.checklistName="";
								
							}
//							$scope.checklistNameList = [];
//							$scope.getEquipmentChecklistNameListById = function(info,label){
//								$rootScope.startLoader();
//								var data = {
//										unitId:$scope.unitId,
//										organizationId:$scope.organizationId,
//										checklistEquipmentMasterId:info.checklistEquipmentMasterId
//										};								
//								var URI = BASE_URL + ROOT_URL+ "/global/otims/getEquipmentChecklistNameListById";
//								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
//								.then(function(response) {
//											$scope.checklistNameList = [];
//											if (response.data.status == "success"){
//												$rootScope.stopLoader();
//													$scope.checklistNameList = response.data.listObject;
//													if(label==2){
//														$scope.checkListItemNameList  = response.data.listObject;
//													}
//													console.log("$scope.checklistNameList", 	$scope.checklistNameList);
//											}
//										});
//						}
								
							$scope.update = function(info,label){
								if(info!=null){
									//$scope.getEquipmentChecklistNameListById(info,label);
									$scope.checklistObj.checklistEquipmentMasterId =info.checklistEquipmentMasterId;
									$scope.checklistObj.checklistCode = info.checklistCode;
									$scope.checklistObj.checklistName = info.checklistName;
									$scope.saveBtnFlag = false;//added by minesh for update button
							}
							
						}	
			}]);
							