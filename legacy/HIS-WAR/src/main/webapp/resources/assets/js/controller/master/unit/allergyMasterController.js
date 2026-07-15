/**
 * 
 */

angular.module('myApp')
		.controller("allergyMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,GenericServiceParamHeader){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig'); 
			$scope.cimsOption = cookieObjectAppConfig.cimsOption;
			$scope.orgUnitId = cookieObject.unitId;
			$scope.orgId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			$scope.popUpFlag = true;
			
			$scope.initAllergyMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						allergyId:"",
						allergyName:"",
						allergyDesc :"",
						allergyTypeId:"",
						allergyTypeDesc:"",
						cimsCode:"",
						cimsType: "",
				};
				
				//getting active allergy type list
				var headerObj = {
						unit_id : $scope.orgUnitId,
			    		 organization_id : $scope.orgId 
			     };
			     
			     var paramObj = {			    		
				     };
			     var data={
			    		 
			     }
				var URI = BASE_URL + ROOT_URL
					+ "/api/global/getAllergyTypeMaster";
				GenericServiceParamHeader
					.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
					.then(function(response) {
						$scope.alergyTypeList=[];
								if (response.data.status == "success")
									$scope.alergyTypeList = response.data.listObject;
								console.log("alergyTypeList",$scope.alergyTypeList);
							});
				console.log("Common-init",$scope.common);
			}
			$scope.initAllergyMaster();			
			
			/**Crud Operations Start**/
			
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			};
			
			$scope.initAllergyList = function(orgId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organizationId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId
							};
				
				var data2 = {
								organizationId:orgId ,
								unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getAllergyMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getAllergyMasterListCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("LISt ",$scope.commonList);
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
			
			$scope.getAllergyList = function(orgId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getAllergyMasterList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
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
					$scope.getAllergyList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.orgUnitId);
				}
			}

			$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			
			/**List And Pagination End**/
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					
//					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var allergyObj = {
							allergyName		:   $scope.common.allergyName,
							allergyDesc		:   $scope.common.allergyDesc,
							cimsCode		:   $scope.common.cimsCode,
							cimsType		:   $scope.common.cimsType,
							allergyTypeId   :   $scope.common.allergyTypeId,
							createdBy		:	$scope.createdBy,
							updatedBy		:	$scope.updatedBy,
							createdDate		:	$scope.dt,
							status			:	'A',
							organizationId	:	$scope.orgId,
							unitId			:	$scope.orgUnitId,
							updatedDate		:	$scope.dt
					}
					 $scope.$broadcast('show-errors-check-validity');
					var URI = BASE_URL + ROOT_URL + "/api/global/saveAllergyMaster";
					console.log("Save-URI-",URI);
					console.log("OBJ",allergyObj);
					 if ($scope.allergyForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					GenericService.serviceAction("POST",URI,allergyObj).then(function(response){
						
						if (response.data.status == 'success')
						{
//							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initAllergyMaster();
							$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
						} else if(response.data.status == 'error')
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
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					
					var URI = BASE_URL + ROOT_URL +"/api/global/getAllergyMasterListById/"+id+"/"+$scope.orgId+"/"+$scope.orgUnitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.allergyTypeId=response.data.listObject[0].allergyTypeId.toString();
							
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
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var allergyObj = {
							allergyName		:   $scope.common.allergyName,
							allergyDesc		:   $scope.common.allergyDesc,
							cimsCode		:   $scope.common.cimsCode,
							cimsType        :   $scope.common.cimsType,
							allergyTypeId   :   $scope.common.allergyTypeId,
							allergyId   	:   $scope.common.allergyId,
							organizationId	:	$scope.orgId,
							unitId			:	$scope.orgUnitId,
							updatedBy		:  $scope.updatedBy,
							updatedDate		:  $scope.dt
					}					
					var URI = BASE_URL + ROOT_URL + "/api/global/updateAllergyMaster";
					console.log("Update-URI-",URI);
					console.log("OBJ",allergyObj);
					 $scope.$broadcast('show-errors-check-validity');
					 if ($scope.allergyForm.$valid) {
					GenericService.serviceAction("POST",URI,allergyObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initAllergyMaster();
							$scope.initAllergyList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var allergyObj = {
							allergyId	 	: 	id,
							status			:	status,
							updatedDate		:	$scope.dt,
							updatedBy		:	$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/updateAllergyMasterStatus";
					
					GenericService.serviceAction("POST",URI,allergyObj).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
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