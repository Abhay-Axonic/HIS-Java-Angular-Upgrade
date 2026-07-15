angular
		.module('myApp')
		.controller(
				'checklistItemMasterController',
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
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,PagerService ) {
							
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
							
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							$scope.getChecklistItemList = function(organizationId,offset, noOfRecordsPerPage, unitId){
								
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
								
								var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage
										};								
								var URI = BASE_URL + ROOT_URL+ "/global/otims/getChecklistItemMasterList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											$scope.checklistItemList = [];
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.checklistItemList = response.data.listObject;
													$scope.commonListCount = response.data.object;
													$scope.setPage(1,false);
											}
										});
						}
					//	$scope.getChecklistItemList();
							$scope.setNoOfRecords = function() {
								$scope.getChecklistItemList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.checklistItemObj ={
									"checklistItemMasterId":null,
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

							$scope.saveCheckListItem = function(){
								
								if($scope.checklistItemObj.code == ""){
									
									growl.warning("please enter checklist Item code ",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.checklistItemObj.description == ""){
									
									growl.warning("please enter checklist Item description ",
											{
												title : 'warning!'
											});
									return false;
									
								}else{
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL +"/global/otims/saveChecklistItemMaster";
									console.log("$scope.checklistItemObj", 	$scope.checklistItemObj);
									GenericService.serviceAction("POST", URI,$scope.checklistItemObj).then(function(response){
										if (response.data.status == 'success')
										{	$rootScope.stopLoader();
											$scope.checklistItemObj.code="";
											$scope.checklistItemObj.description="";
											$scope.getChecklistItemList();
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
								
								
							}
							
							$scope.updateStatus = function(checklistItemMasterId,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											checklistItemMasterId :checklistItemMasterId,
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/otims/updateChecklistItemMasterStatus";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.getChecklistItemList();
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
							
							$scope.checklistItemObj.checklistItemMasterId = info.checklistItemMasterId;
							
							$scope.checklistItemObj.code = info.code;
							
							$scope.checklistItemObj.description = info.description;
							
							$scope.checklistItemObj.status = info.status;
						}	
							
				$scope.masterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
					          $rootScope.startLoader();
					           console.log("offset", offset);
					          offset = offset != null ? offset : 0;
					           noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

					$rootScope.startLoader();
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
					
					var data = {
							unitId:unitId,
							organizationId:organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage,
							
							};								
					var URI = BASE_URL + ROOT_URL+ "/global/otims/getChecklistItemMasterList";
					BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
					.then(function(response) {
								$scope.checklistItemList = [];
								if (response.data.status == "success"){
									$rootScope.stopLoader();
										$scope.checklistItemList = response.data.listObject;
										console.log("$scope.checklistItemList", 	$scope.checklistItemList);
								}
							});
				}
				$scope.pager = {};
				$scope.page;

				$scope.setPage = function(page, flag) {
					if (page < 1 || page > $scope.pager.totalPages) {
						return;
					}
					$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage,$scope.unitId);
					if (flag) {
						    $scope.masterList($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize,$scope.unitId);
					}
				}

				$scope.getChecklistItemList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

			}]);
							