/**
 * 
 */

angular.module('myApp')
		.controller("OTIMSroleCreationController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
		
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			/**Crud Operations Start**/
		
			$scope.common = {
					roleId:"",
					roleName:""
			};
			
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				var data = {
						orgId:$scope.organisationId,
						unitId:$scope.unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/otimsRole/getAllOtimsRoleMasterList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
				});
			}
			
			$scope.masterList();
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				
				try{
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					
					$scope.common.createdBy=$scope.createdBy,
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.createdDate=$scope.dt,
					$scope.common.status='A',
					$scope.common.unitId=$scope.unitId,
					$scope.common.orgId=$scope.organisationId,
					$scope.common.updatedDate=$scope.dt
					
					
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/otimsRole/saveOtimsRoleMaster";
				
					
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.masterList();
							$scope.common={};
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//						
						}else if(response.data.status == 'Already Exists.')
						{
							$rootScope.stopLoader();
							$scope.common={};
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						} 
						else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$rootScope.stopLoader();

					});
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
		
			$scope.showUpdateBtn = function(info){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					$scope.common.roleName = info.roleName;
					$scope.common.roleId =info.roleId;
					$scope.common.orgId = info.orgId;
					$scope.common.unitId = info.unitId;
					console.log("common",$scope.common);
					$rootScope.stopLoader();
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data = {
							roleId:$scope.common.roleId,
							updatedBy:$scope.updatedBy,
							roleName:$scope.common.roleName,
							updatedDate:$scope.dt,
							orgId:$scope.common.orgId,
							unitId:$scope.common.unitId
					};
					var URI = BASE_URL + ROOT_URL +"/api/global/otimsRole/updateOtimsRoleMaster";
					GenericService.serviceAction(METHOD_POST,URI,data).then(function(response){
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.masterList();
							$scope.common={};
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = true;
							$scope.common={};
						}else if(response.data.status == 'Already Exists.')
						{
							$rootScope.stopLoader();
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = true;
							$scope.common={};
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						} 
						else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = true;
							$scope.common={};
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
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.roleId=Id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/otimsRole/updateOtimsRoleMasterStatus";
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.common={};
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							$scope.common={};
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