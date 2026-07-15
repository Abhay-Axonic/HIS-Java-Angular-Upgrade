/**
 * 
 */

angular.module('myApp')
		.controller("bloodBankTypeComponentMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.bagTypeList=[];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.getComponentList=[];
			$scope.componentList = [];
			$scope.selectBloodBankTypeComponentMasterList=[];
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						bloodBankTypeComponentId:"",
						bloodComponentId:"",
						bloodComponentDesc:"",
						bloodBagTypeDesc:"",
						bloodBagTypeId:"",
						bloodComponentCode:""


						

			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.initBagTypeComponentMaster = function(){
				$scope.bagTypeComponentMaster =
				{
					 bloodBankTypeComponentId:$scope.common.bloodBankTypeComponentId,
					 bloodComponentId:$scope.common.bloodComponentId,
					 bloodComponentDesc:$scope.common.bloodComponentDesc,
					 bloodComponentCode:$scope.common.bloodComponentCode,
					 bloodBagTypeId:$scope.common.bloodBagTypeId,
					 bloodBagTypeDesc:$scope.common.bloodBagTypeDesc,
				     status:$scope.common.status,
					 createdBy:$scope.createdBy,
					 updatedBy:$scope.updatedBy,
				     organizationId:$scope.organizationId,
				     unitId:$scope.unitId,
				     title:$scope.common.title,
				     isDeleted: "N"
				 }
				
				}
			
			//Get component List
			  		
			$scope.searchGroupList = function() {
				
				var data = {};
				var paramObj = {
						bloodComponentDesc : ""
				};
				var headerObj = {									
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				}

				var URI = BASE_URL + ROOT_URL+ GET_BLOOD_COMPONENT_LIST+S+$scope.organizationId+S+$scope.unitId;
				
				return GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
						.then(function(response) {
							$scope.componentList=[];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.componentList= response.data.listObject;
									console.log("componentList",$scope.componentList);
								});

			}
	
			$scope.searchGroupList();
			
			//Get detailed component List
			$scope.tempSelectcomponent=[];
			$scope.addcomponent = function(bloodComponentId,bloodComponentDesc){
				
				var flag = false;
				angular.forEach($scope.tempSelectcomponent,function(value,index){
					if(value.bloodComponentId == bloodComponentId){
						$scope.tempSelectcomponent.splice(index,1);
						$scope.getComponentList[value.bloodComponentId] = false;
						flag = true;
					}
				})
				if(!flag){
					var temp = {bloodComponentId:bloodComponentId,bloodComponentDesc:bloodComponentDesc};
					$scope.tempSelectcomponent.push(temp);
				}
			}
			$scope.selectcomponent = function()
			{
				$scope.selectBloodBankTypeComponentMasterList = angular.copy($scope.tempSelectcomponent);
			}
			//Get component List end
			
			//Get Template List 
			$scope.getbagTypeList = function(){
		  		
		  		var URI = BASE_URL + ROOT_URL + GET_BLOOD_BAG_LIST+S+$scope.organizationId+S+$scope.unitId;
		  		var data = {
		  				orgId:$scope.organizationId,
		  			      unitId:$scope.unitId
		  		}
		  		GenericService.serviceAction("GET",URI,data).then(function(response){
		  			if(response.data.status == "success"){
				  			$rootScope.stopLoader();					
				  			$scope.bagTypeList = [];
				  			$scope.bagTypeList=response.data.listObject;

		  			console.log("bagTypeList",$scope.getbagTypeList);
		  			}
		  		});
		  		}
		  		$scope.getbagTypeList();
		  	//Get Template List end
		  		
		  	//Get Detailed List By Template Id
			$scope.getDetailedComponentList = function(bloodBagTypeId) {
				try {
					var data = "";
					$rootScope.startLoader();	
					$scope.getListObject=[];
					var URI = BASE_URL + ROOT_URL+GET_DETAILED_COMPONENT_LIST+S+bloodBagTypeId+S+$scope.organizationId+S+$scope.unitId;
					console.log("URI", URI);
					GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.getListObject  = response.data.listObject;
										console.log("getListObject",JSON.stringify($scope.getListObject));
									});
					$rootScope.stopLoader();
				} catch (e) {
					console.log(e.message)
				}

			}
			
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
				var data1 = "";
				
				var data2 = "";
				
				var URI1 = BASE_URL + ROOT_URL +GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_LIST+S+offset+S+noOfRecordsPerPage; 
				var URI2 = BASE_URL + ROOT_URL +GET_BLOOD_BANK_TYPE_MASTER_COMPONENT_COUNT+S+$scope.organizationId+S+$scope.unitId;
					
				
				GenericService.serviceAction("GET",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("GET",URI2,data2).then(function(response){
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
				
				var data = "";
				
				var URI = BASE_URL + ROOT_URL + GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_LIST+S+offset+S+noOfRecordsPerPage;
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
				if (flag)
				{
					$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
	
			
			$scope.save = function() 
			{
				$scope.$broadcast('show-errors-check-validity');
//				if ($scope.commonform.$valid)
//				{
					try {
						if($scope.common.bloodBagTypeId == undefined || $scope.common.bloodBagTypeId == ""){
							growl.error("Please select Blood Bag",
									{
										title : 'error'
									});
							return false;
						}
						
						if($scope.getComponentList == undefined || $scope.getComponentList.length == 0){
							growl.error("Please Select Component Type",
									{
										title : 'error'
									});
							return false;
						}
						
						if($scope.selectBloodBankTypeComponentMasterList == undefined || $scope.selectBloodBankTypeComponentMasterList.length == 0){
							growl.error("Please Add Component Type",
									{
										title : 'error'
									});
							return false;
						}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						
						$rootScope.startLoader();
						$scope.initBagTypeComponentMaster();
						$scope.listbagTypeComponentMaster =
						{
								 bloodComponentId:$scope.common.bloodComponentId,
								 bloodComponentDesc:$scope.common.bloodComponentDesc,
								 bloodBagTypeId:$scope.common.bloodBagTypeId,
								 bloodBagTypeDesc:$scope.common.bloodBagTypeDesc,
							     status:$scope.common.status,
							     organizationId:$scope.organizationId,
							     unitId:$scope.unitId,
							     status:"A",
							     isDeleted: "N",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 updatedDate:$scope.dt,
							     selectBloodBankTypeComponentMasterList:[]
						     
						 }
										    	 
					    	 
						angular.forEach($scope.selectBloodBankTypeComponentMasterList,function(value,index){
								$scope.listbagTypeComponentMaster.selectBloodBankTypeComponentMasterList.push(value.bloodComponentId);
						})
						console.log("bagTypeComponentMaster",JSON.stringify($scope.listbagTypeComponentMaster));
						var URI = BASE_URL + ROOT_URL + SAVE_BLOOD_BANK_TYPE_COMPONENT_MASTER;
						GenericService.serviceAction("POST", URI, $scope.listbagTypeComponentMaster).then(function(response) 
						{	
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.selectBloodBankTypeComponentMasterList=[];
								$scope.componentList=[];
								$scope.searchGroupList();
								$scope.getComponentList=[];
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
					} catch (e) {
						console.log("Exception",e.message);
					}
					
//				}
			}
			
			
			$scope.showUpdateBtn = function(id)
			{
				try {
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					var URI = BASE_URL + ROOT_URL +GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_BY_ID+S+id+S+$scope.organizationId+S+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
					{
						console.log("data",response.data);
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.common.bloodBagTypeId = response.data.listObject[0].bloodBagTypeId + "";
							$scope.selectBloodBankTypeComponentMasterList = [];
							$scope.tempcomponent = [];
							
							angular.forEach($scope.getComponentList,function(value,index){
								$scope.getComponentList[index] = false;
							var i = $scope.tempSelectcomponent.indexOf(value);
							$scope.tempSelectcomponent.splice(i,1);
							})
							
							angular.forEach(response.data.listObject,function(value,index){
								var temp = {bloodComponentId:value.bloodComponentId,bloodComponentDesc:value.bloodComponentDesc};
								$scope.tempSelectcomponent.push(temp);
								$scope.getComponentList[value.bloodComponentId] = true;
								$scope.selectBloodBankTypeComponentMasterList.push(temp);
							});
							
							console.log("$scope.common",$scope.common);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							alert("Error In Fetching Data");
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
				
			}
			
			$scope.update = function() 
			{
				$scope.$broadcast('show-errors-check-validity');
//				if ($scope.commonform.$valid)
//				{
					try {
						if($scope.common.bloodBagTypeId == undefined || $scope.common.bloodBagTypeId == ""){
							growl.error("Please select Blood Bag",
									{
										title : 'error'
									});
							return false;
						}
						
						if($scope.getComponentList == undefined || $scope.getComponentList.length == 0){
							growl.error("Please Select Component Type",
									{
										title : 'error'
									});
							return false;
						}
						
						if($scope.selectBloodBankTypeComponentMasterList == undefined || $scope.selectBloodBankTypeComponentMasterList.length == 0){
							growl.error("Please Add Component Type",
									{
										title : 'error'
									});
							return false;
						}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');		
						
						$rootScope.startLoader();
						$scope.initBagTypeComponentMaster();
						$scope.listbagTypeComponentMaster =
						{
								 bloodComponentId:$scope.common.bloodComponentId,
								 bloodComponentDesc:$scope.common.bloodComponentDesc,
								 bloodBagTypeId:$scope.common.bloodBagTypeId,
								 bloodBagTypeDesc:$scope.common.bloodBagTypeDesc,
							     status:$scope.common.status,
							     organizationId:$scope.organizationId,
							     unitId:$scope.unitId,
							     status:"A",
							     isDeleted: "N",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 updatedDate:$scope.dt,
							     selectBloodBankTypeComponentMasterList:[]
						
						 }
						angular.forEach($scope.selectBloodBankTypeComponentMasterList,function(value,index){
								$scope.listbagTypeComponentMaster.selectBloodBankTypeComponentMasterList.push(value.bloodComponentId);
						})
						console.log("bagTypeComponentMaster",JSON.stringify($scope.listbagTypeComponentMaster));
					
						var URI = BASE_URL + ROOT_URL +UPDATE_BLOOD_BANK_TYPE_COMPONENT_MASTER;
					    GenericService.serviceAction("POST", URI, $scope.listbagTypeComponentMaster).then(function(response) 
						{
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.selectBloodBankTypeComponentMasterList=[];
								$scope.componentList=[];
								$scope.searchGroupList();
								$scope.getComponentList=[];
								$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
					} catch (e) 
					{
						console.log("Exception",e.message);
					}
					
//				}
			}
			
			$scope.updateStatus = function(id,status)
			{
				try {
					$rootScope.startLoader();
					$scope.initBagTypeComponentMaster();
					var URI = BASE_URL + ROOT_URL +UPDATE_BLOOD_BANK_TYPE_COMPONENT_MASTER_STATUS+S+id+S+$scope.organizationId+S+$scope.unitId+S+status;
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("GET", URI, $scope.bagTypeComponentMaster).then(function(response) 
					{
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.selectBloodBankTypeComponentMasterList=[];
							$scope.componentList=[];
							$scope.searchGroupList();
							$scope.getComponentList=[];
							$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
				
			}
			
		
			
			
			/**Crud Operations End**/
}]);