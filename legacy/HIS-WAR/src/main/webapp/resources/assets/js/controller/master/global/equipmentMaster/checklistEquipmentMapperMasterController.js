angular
		.module('myApp')
		.controller(
				'checklistEquipmentMapperMasterController',
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
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,PaginationService) {
							
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
								
								$scope.checklistMapperObj ={
										"checklistEquipmentMapperMasterId":null,
										"checklistEquipmentMasterId":"",
										"assetCategoryId":"",
										"productCategoryId":"",
										"itemCode":"",
										"itemName":"",
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
									var URI = BASE_URL + ROOT_URL+ "/global/equipmentchecklist/getAllEquipmentChecklistMasterList";
									BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
												$scope.checklistEquipmentList = [];
												if (response.data.status == "success"){
													$rootScope.stopLoader();
														$scope.checklistEquipmentList = response.data.listObject;
														console.log("$scope.checklistEquipmentList", 	$scope.checklistEquipmentList);
												}
											});
									
									var URI = BASE_URL+ROOT_URL+"/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId;
									GenericService.serviceAction("GET", URI, {}).then(
									function (response) {
										console.log(response.data);
										$scope.assetCategoriesList = response.data;
										$rootScope.stopLoader();
									},
									function (err) {
										$rootScope.stopLoader();
									});
									
									
									var data = {
											unitId:$scope.unitId,
											organizationId:$scope.organizationId
											};								
									var URI = BASE_URL + ROOT_URL+ "/global/equipmentchecklistmapper/getAllEquipmentChecklistMapperMasterList";
									BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
												$scope.checklistEquipmentMapperList = [];
												if (response.data.status == "success"){
														$rootScope.stopLoader();
														console.log(response.data.listObject);
														 $scope.pagination.data=[];
														$scope.checklistEquipmentMapperList = response.data.listObject;
														
														 if($scope.checklistEquipmentMapperList.length > 0){            
									                         PaginationService.searchData($scope.checklistEquipmentMapperList, $scope.pagination);
									                       }
														console.log("$scope.checklistEquipmentMapperList", 	$scope.checklistEquipmentMapperList);
												}
											});
									
									
								
							}
							$scope.init();
							

							$scope.getProductCategoryList = function(id) {
							var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id+"/"+$scope.organizationId;
							$rootScope.startLoader();
							GenericService.serviceAction("GET", URI, {}).then(
							function (response) {
								console.log(response.data);
								$scope.productCategoriesList = response.data;
								$rootScope.stopLoader();
							},
							function (err) {
								$rootScope.stopLoader();
							});
							}


							
							$scope.saveChecklistEquipmentMapperMaster = function(){
								$rootScope.startLoader();
								if($scope.checklistMapperObj.checklistEquipmentMasterId==""){
									$rootScope.stopLoader();
									growl.warning("Please select checklist name",
											{
												title : 'warning!'
											});
								}else if($scope.checklistMapperObj.assetCategoryId==""){
									$rootScope.stopLoader();
									growl.warning("Please select asset category",
											{
												title : 'warning!'
											});
								}else if($scope.checklistMapperObj.productCategoryId==""){
									$rootScope.stopLoader();
									growl.warning("Please select product category",
											{
												title : 'warning!'
											});
								}else if($scope.checklistMapperObj.itemCode==""){
									$rootScope.stopLoader();
									growl.warning("Please select item code",
											{
												title : 'warning!'
											});
								}else if($scope.checklistMapperObj.itemName==""){
									$rootScope.stopLoader();
									growl.warning("Please select item name",
											{
												title : 'warning!'
											});
								}else{
								
								var URI = BASE_URL + ROOT_URL +"/global/equipmentchecklistmapper/saveChecklistEquipmentMapperMaster";
								console.log("$scope.checklistMapperObj", 	$scope.checklistMapperObj);
								GenericService.serviceAction("POST", URI,$scope.checklistMapperObj).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
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
							
							$scope.updateStatus = function(checklistEquipmentMapperMasterId,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											checklistEquipmentMapperMasterId :checklistEquipmentMapperMasterId,
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/equipmentchecklistmapper/updateChecklistEquipmentMapperStatus";
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
							
							$scope.update = function(info,label){
								if(info!=null){
									$scope.checklistMapperObj.checklistEquipmentMapperMasterId = info.checklistEquipmentMapperMasterId 
									$scope.checklistMapperObj.checklistEquipmentMasterId = info.checklistEquipmentMasterId;
									$scope.checklistMapperObj.assetCategoryId = info.assetCategoryId;
									$scope.checklistMapperObj.productCategoryId = info.productCategoryId;
									$scope.checklistMapperObj.itemCode = info.itemCode;
									$scope.checklistMapperObj.itemName = info.itemName;
									$scope.getProductCategoryList(info.assetCategoryId);
									$scope.saveBtnFlag = false;// add by minesh for update button title
							}
							
						}	
			}]);
							