
/**
 * 
 */

(function () {
  "use strict";

function vitalQualifierMapperController($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
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


			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			$scope.listOfVitalQualifireMapperDtoS=[];
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						vitalQualifireMapperId:"",
						vitalId:"",
						vitalQualifireId:"",
						vitalQualifireDesc:"",

							
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			// object for History Personal
		     $scope.qualifiresList = [{
		    	 vitalQualifireId:""
			}];
			/**Crud Operations Start**/
		
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
							var URI1 = BASE_URL + ROOT_URL + "/api/global/getVitalQualifireMapperList"; 
							var URI2 = BASE_URL + ROOT_URL + "/api/global/getVitalQualifireMapperCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
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
				
				var URI = BASE_URL + ROOT_URL +  "/api/global/getVitalQualifireMapperList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
				if (flag)
				{
					$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			$scope.selectedPositionList=[];
			$scope.selectedQualifire=[];
			
			$scope.addPosition = function(vitalQualifireId,vitalCategoryId){
				console.log("test",$scope.selectedQualifire);
				var flag = false;
				angular.forEach($scope.selectedPositionList,function(value,index){
					if(value.vitalQualifireId == vitalQualifireId){
//						$scope.selectedQualifire[value.vitalQualifireId] = 'N';
						if($scope.selectedQualifire[value.vitalQualifireId]=='Y'){
							$scope.selectedQualifire[value.vitalQualifireId] = 'Y';
							flag = true;
							}else{
								$scope.selectedQualifire[value.vitalQualifireId] = 'N';
								flag = false;
								
							}
						
					}
				})
				if(!flag){
					if($scope.selectedQualifire[vitalQualifireId]=='Y'){
					var temp = {
							vitalQualifireId:vitalQualifireId,
							vitalCategoryId:vitalCategoryId
							};
					
					$scope.selectedPositionList.push(temp);
					}else{
						angular.forEach($scope.selectedPositionList,function(value,index){
							if(value.vitalQualifireId == vitalQualifireId){
								$scope.selectedPositionList.splice(index,1);
							}
						});
					}
					
					console.log("final Select data",$scope.selectedPositionList);
				}
			}
			
			$scope.save = function(){
				try{
					
					if($scope.popUpFlag==false)
						{
						  $rootScope.startLoader();
						}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
//					$scope.$broadcast('show-errors-check-validity');
						var data={
								vitalId:$scope.common.vitalId,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								createdBy:$scope.createdBy,
								updatedBy:$scope.updatedBy,
								status:'A',
								createdDate:$scope.dt,
								updatedDate:$scope.dt,
								listOfVitalQualifireMapperDtoS:$scope.selectedPositionList
						}
					
//					if ($scope.cabinMasterForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/saveVitalQualifireMapper";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$rootScope.stopLoader();
							$scope.selectedPositionList=[];
							$scope.common.vitalId='';
							$scope.selectedQualifire=[];
							$scope.getListOfQualifireMapper();
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					$rootScope.stopLoader();
//				}
					}
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
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
					$scope.selectedQualifire=[];
					var data={
							organizationId:$scope.organizationId,
								vitalId:id
					};
					var URI = BASE_URL + ROOT_URL + "/api/global/getVitalQualifireMapperListById";
					console.log("EDIT",URI);
					GenericService.serviceAction("POST",URI,data).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject;
							console.log("$scope.commonEdit",$scope.common);
							$scope.common.vitalId=response.data.listObject[0].vitalId;
							angular.forEach($scope.common,function(value,key){
								angular.forEach(value.listOfVitalQualifiresForCategory,function(value1,key1){
									
									angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,key2){
										$scope.selectedQualifire[value2.vitalQualifireId] = 'Y';
										$scope.addPosition(value2.vitalQualifireId,value1.vitalCategoryId);
									});
								});
							});
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
							
								vitalId:$scope.common.vitalId,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								createdBy:$scope.createdBy,
								updatedBy:$scope.updatedBy,
								status:'A',
								createdDate:$scope.dt,
								updatedDate:$scope.dt,
								listOfVitalQualifireMapperDtoS:$scope.selectedPositionList
							
						}
								
					var URI = BASE_URL + ROOT_URL + "/api/global/updateVitalQualifireMapper";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data);
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
									$scope.selectedPositionList=[];
									$scope.common.vitalId='';
									$scope.selectedQualifire=[];
									$scope.getListOfQualifireMapper();
		//							$scope.$broadcast('show-errors-reset');
									$scope.initMaster();
									$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
			
			$scope.updateStatus = function(vitalId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					/*$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					organizationId=$scope.organizationId,
					unitId=$scope.unitId,
					$scope.common.status=status,
					$scope.common.vitalId=vitalId,*/
					var data={
							vitalId:vitalId,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							updatedBy:$scope.updatedBy,
							status:status,
							updatedDate:$scope.dt,
					};
					
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateVitalQualifireMapperStatus";
					console.log("update status",data);
					//return false;
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
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							$scope.getListOfQualifireMapper();
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
			$scope.getVitalQualifireList = function() {
				var data ={
						organizationId:$scope.organizationId,
						vitalCategoryId:$scope.vitalCategoryId,
				};
				
				var URI = BASE_URL + ROOT_URL+ "/api/global/getVitalQualifireListByCategoryId";
				GenericService.serviceAction("POST",URI,data).then(function(response){
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.vitalQualifireList = response.data.listObject;
									console.log("vitalQualifireList", $scope.vitalQualifireList);
									
								});
			}
			$scope.getVitalQualifireList();
			
			$scope.getVitalList = function() {
				$scope.startLoader();
				var data ={};
				var headerObj = {
						unit_id : $scope.unitId,
						organization_id : $scope.organizationId
					};

				
				var paramObjForPer = {
				};
				
				var URI = BASE_URL + ROOT_URL
						+ GETVITALMASTERLIST;
				GenericServiceParamHeader
                .serviceAction(METHOD_GET, URI, data,
                        NOTIFICATION_MSG_STATUS_FALSE,
                        paramObjForPer, headerObj).then(function(response){
									console.log(response);
									if (response.data.status == "success")
										$scope.vitalsList = response.data.listObject;
									console.log("Vital List", $scope.vitalsList);
									
								});
				$scope.stopLoader();
			}
			$scope.getVitalList();
			
			
			$scope.getListOfQualifireMapper = function() {

				var URI = BASE_URL + ROOT_URL + "/api/global/getVitalQualifireMapperList";
				var data = {
						organizationId:$scope.organizationId
				};
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {

									$rootScope.stopLoader();
									$scope.listOfQualifireMapper = [];
									$scope.listOfQualifireMapper = response.data.listObject;
									console.log("listOfQualifireMapper",$scope.listOfQualifireMapper);

								});
			}
			$scope.getListOfQualifireMapper();
			/**Crud Operations End**/
}
function config($stateProvider) {
    $stateProvider.state('vitalQualifierMapper', { 
		url: '/vitalQualifierMapper',
		templateUrl: 'views/master/GlobalMaster/vital-qualifier-mapper.html',
		controller: "vitalQualifierMapperController",
		title: 'Vital Qualifier Mapper',
	})	
  }

  angular
    .module("myApp")
    .config(config)
    .controller("vitalQualifierMapperController", vitalQualifierMapperController);
})();