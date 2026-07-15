/**
 * 
 */

angular.module('myApp')
		.controller("newAccessControlController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.createdBy =1 ;

			
			
			$scope.initMaster = function(){
				$scope.searchName='';
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						actionId:"",
						actionName:"",
						isChild:"N",
						parentActioName:"",
						parentId:"0",
						grandParentActionName:"",
						name:""
						
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();

			$scope.getListByName = function(name){
				
				var URI = BASE_URL + ROOT_URL + "/api/global/searchActionMasterByParentActionName";
				var data = {
						
						actionName:name
					} 
				return GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == "success")
//							$scope.commonList =response.data.listObject;
							console.log("getListByName", response.data.listObject);
						return response.data.listObject;
					});
						
				}
			
//			$scope.getSearchList = function(){
//				
//				var URI = BASE_URL + ROOT_URL + "/api/global/searchActionMasterByParentActionName";
//				var data = {
//						
//						actionName:$scope.searchName
//					}
//				return GenericService.serviceAction("POST",URI,data).then(function(response){
//						if (response.data.status == "success")
//							console.log("getSearchList", response.data.listObject);
//						return response.data.listObject;
//					});
//						
//				}
				
			
			
			$scope.selectParentId = function(info, model,label) {
				console.log(info);
//				$scope.common.parentId = info.parentId;
				$scope.common.parentId = info.actionId;
				$scope.common.parentActioName = info.actionName;
//				$scope.common.grandParentActionName = info.parentActioName;
			}
			/**Crud Operations Start**/
			
		
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initMasterList = function(offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getActionMasterList/"+offset+"/"+noOfRecordsPerPage; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getActionMasterCount";
					
				
				GenericService.serviceAction("GET",URI1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("GET",URI2).then(function(response){
					if(response.data.status == "success"){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
				
			}		
			
			$scope.masterList = function(offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getActionMasterList/"+offset+"/"+noOfRecordsPerPage;
				console.log("URI", URI);
				GenericService.serviceAction("GET", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
				if (flag)
				{
					$scope.masterList($scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					
					if($scope.popUpFlag==false)
						{
						  $rootScope.startLoader();
						}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={	
							
							parentId:$scope.common.parentId,
							actionName:$scope.common.actionName,
							name:$scope.common.name,
							parentActioName:$scope.common.parentActioName,
							actionLink:$scope.actionLink,
							actionIcon:$scope.actionIcon,
							createdBy:$scope.createdBy,
							status:'A',
							isChild:$scope.common.isChild,
							createdDate:$scope.dt,
							grandParentActionName:$scope.common.grandParentActionName,
						
						}
					
					
					console.log("save",data);//return false;

				
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/saveActionMaster";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
			
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					$rootScope.stopLoader();
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			$scope.configDetails={};
			
			
			$scope.showDetailsBtn=function(data){
				$scope.configDetails=data;
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/api/global/getActionMasterById/"+id;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							if($scope.common.parentId > 0){
								$scope.common.isChild = 'Y';
								$scope.common.name = $scope.common.parentactioname;
								$scope.common.parentActioName = $scope.common.parentactioname;
								 $(".inputMenu").show();
							}else{
								$scope.common.isChild = 'N';
								 $(".inputMenu").hide();
							}
							$scope.common.actionId=response.data.listObject[0].actionId.toString();
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					$rootScope.startLoader();
					var data={	
							actionId:$scope.common.actionId,
							parentId:$scope.common.parentId,
							actionName:$scope.common.actionName,
							parentActioName:$scope.common.parentActioName,
							grandParentActionName:$scope.common.grandParentActionName,
							actionLink:$scope.actionLink,
							isChild:$scope.common.isChild,
							actionIcon:$scope.actionIcon
						
						}
								
					var URI = BASE_URL + ROOT_URL + "/api/global/updateActionMaster";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data);
					//return false;
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
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
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(actionId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.status=status,
					$scope.common.actionId=actionId,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateActionMasterStatus";
					console.log("update status",$scope.common); //return false;
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
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
			
			$scope.getRemovedAction = function(id){
				
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.status='D',
					$scope.common.actionId=id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateActionMasterStatus";
					console.log("update status",$scope.common); //return false;
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success('Removed Successfully',
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
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
			
			
			/**Crud Operations End**/
}]);