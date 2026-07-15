/**
 * 
 */

angular.module('myApp')
		.controller("icpcCodeTemplateMappingController",
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
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.roleId = cookieObject.roleId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.selectedSystemList=[];	
			$scope.selectedICPCdescripstion=[];
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.templateId="";
				$scope.common = {
						 icpcMpprId:"",
						 templateId:"",
					     systemId:"",
						 empCode:"",
						 empName:"",
						 contactNo:"",
						 roleName:"",
						 title:""
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.getActiveList = function(){
				
			var URI = BASE_URL + ROOT_URL + "/api/global/getListOfICPCSystems";
		
			GenericService.serviceAction("GET",URI).then(function(response){
				if(response.data.status == "success"){
				$rootScope.stopLoader();					
				$scope.getActiveList = [];
				
				angular.forEach(response.data.listObject,
						function(value,index) {
							var data = {};
							data.id = value.icpcSystemId;
							data.label = value.icpcSystemDesc;
							$scope.getActiveList.push(data);
						});
				
				console.log("getActiveRoleList",$scope.getActiveList);
				}
			});
			}
			$scope.getActiveList();
			
			$scope.getListICPC = function(){
				
				var URI = BASE_URL + ROOT_URL + "";
				var data = {
						 organizationId:$scope.organisationId,
					      unitId:$scope.unitId,
					      
					    
					}
				return GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == "success")
							console.log("getListByName", response.data.listObject);
						return response.data.listObject;
					});
						
				}
				
			
		$scope.getSaveTemplateList = function(){
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getICPCTemplateMappingList";
				var data = {
						 orgId:$scope.organisationId,
						 orgUnitId:$scope.unitId,
					    
					}
				return GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == "success")
							$scope.saveTemplateList=response.data.listObject;
							console.log("getListByName", response.data.listObject);
						return response.data.listObject;
					});
						
				}
		//$scope.getSaveTemplateList();
		
		$scope.getDetailedListOfICPCSystems = function(templateId){
			$scope.detailedListOfICPCSystems=[];
			$scope.loadingMsg="Loading...";
			var URI = BASE_URL + ROOT_URL + "/api/global/getDetailedListOfICPCSystems";
			var data = {
					 orgId:$scope.organisationId,
					 orgUnitId:$scope.unitId,
					 templateId:templateId
				    
				}
			return GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == "success")
						$scope.detailedListOfICPCSystems=response.data.listObject;
					if($scope.detailedListOfICPCSystems.length==0)
						$scope.loadingMsg="No record found";
						console.log("detailedListOfICPCSystems", $scope.detailedListOfICPCSystems);
					return response.data.listObject;
				});
					
			}
		
		$scope.getDetailedListOfICPCSystemsDescription = function(templateId){
			$scope.detailedListOfICPCSystemsDescription=[];
			$scope.loadingMsg="Loading...";
			var URI = BASE_URL + ROOT_URL + "/api/global/getDetailedListOfICPCSystemsDescription";
			var data = {
					 orgId:$scope.organisationId,
					 orgUnitId:$scope.unitId,
					 templateId:templateId
				}
			return GenericService.serviceAction("POST",URI,data).then(function(response){
					if (response.data.status == "success")
						$scope.detailedListOfICPCSystemsDescription=response.data.listObject;
					if($scope.detailedListOfICPCSystemsDescription.length==0)
						$scope.loadingMsg="No record found";
						console.log("detailedListOfICPCSystemsDescription", $scope.detailedListOfICPCSystemsDescription);
					return response.data.listObject;
				});
					
			}
		
			$scope.templateList = [];
                $scope.getTemplateList = function(){
		  		
		  		var URI = BASE_URL + ROOT_URL + "/api/global/getTemplateListForICPC";
		  		var data = {
		  				orgId:$scope.organisationId,
		  				orgUnitId:$scope.unitId
		  		}
		  		GenericService.serviceAction("POST",URI,data).then(function(response){
		  			if(response.data.status == "success"){
				  			$rootScope.stopLoader();					
				  			$scope.templateList = [];
				  			$scope.templateList=response.data.listObject;

		  			console.log("templateList",$scope.templateList);
		  			}
		  		});
		  		}
		  		$scope.getTemplateList();
			
		
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								 orgId:organisationId,
								 orgUnitId:unitId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
							};
				
				var data2 = {
						 orgId:organisationId,
						 orgUnitId:unitId,
							};
				
				var URI1 = BASE_URL + ROOT_URL + "/api/global/getICPCTemplateMappingList";
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getICPCTemplateMappingCount";
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.saveTemplateList = response.data.listObject;
					console.log("commonList",$scope.saveTemplateList);
					}
				});
				
				GenericService.serviceAction("POST",URI2,data2).then(function(response){
					if(response.status == 200){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
								
			}		
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						 orgId:organisationId,
						 orgUnitId:unitId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				//get saved list
				var URI = BASE_URL + ROOT_URL + "/api/global/getICPCTemplateMappingList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.saveTemplateList = response.data.listObject;
					console.log("commonList",$scope.saveTemplateList);
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
					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			$scope.saveBtnFlag = true;
			$scope.updateScope = true;
			$scope.updateBtnFlag = false;
			
			
			$scope.save = function(){
				$scope.saveBtnFlag = true;
				$scope.updateScope = true;
				$scope.updateBtnFlag = false;
				if($scope.templateId==""){
					 growl.warning(
								'Select template!!!',
								{
									title : 'Warning!'
								});
					return false;
				}else if($scope.selectedSystemList.length==0){
					 growl.warning(
								'Select at least one ICPC System!!!',
								{
									title : 'Warning!'
								});
					return false;
				}else if($scope.selectedICPCdescripstion.length==0){
					 growl.warning(
								'Select at least one ICPC Description!!!',
								{
									title : 'Warning!'
								});
					return false;
				}
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$rootScope.startLoader();
					var data1={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:'A',
							isDeleted:'N',
							orgId:$scope.organisationId,
							orgUnitId:$scope.unitId,
							roleId:$scope.roleId,
							createdDate:$scope.dt,
							createdBy:$scope.createdBy,
							templateId:$scope.templateId,
							selectedSystemList:$scope.selectedSystemList,
													
					}
					
					$scope.selectedICPCList=[];
					angular.forEach($scope.selectedICPCdescripstion,
							function(value, key) {
						angular.forEach($scope.dumyList,
								function(value1, key1) {
							if(value.id == value1.id)
								{
								var newlist={}; 
								newlist.id=value1.id;
								newlist.mIcpcSystemId=value1.mIcpcSystemId;
								$scope.selectedICPCList.push(newlist)
								}
								});
						     
							});
					
					data1.selectedICPCList=$scope.selectedICPCList;
					console.log("data",data1);
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/saveICPCTemplateMapping";
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.saveBtnFlag = true;
							$scope.updateScope = true;
							$scope.updateBtnFlag = false;
							$scope.selectedSystemList=[];	
							$scope.selectedICPCdescripstion=[];
							$scope.templateId="";
							//$scope.getSaveTemplateList();
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error("Already exist",
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
			
			$scope.prevTemplateId="";
			$scope.showUpdateBtn = function(id){
				try{
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var data={
							orgId:$scope.organisationId,
							orgUnitId:$scope.unitId,
							templateId:id
					}
					$scope.selectedSystemList=[];
					$scope.selectedICPCdescripstion=[];
					$scope.icpcSavedList=[];
					var templateID=id;
					$scope.templateId= id.toString();
					$scope.prevTemplateId=$scope.templateId;
					var URI = BASE_URL + ROOT_URL + "/api/global/getDetailedListOfICPCSystems";
					
					return GenericService.serviceAction("POST",URI,data).then(function(response){
							if (response.data.status == "success"){
							$scope.templateId= templateID.toString();
							angular.forEach(response.data.listObject, function( value, index) {
								var newAr = {};
								newAr.id = value.mIcpcSystemId;
								newAr.label = value.icpcSystemDesc;
								$scope.selectedSystemList.push(newAr);
								console.log("Selected System List",$scope.selectedSystemList)
							});
							$scope.icpcSavedList=response.data.object.selectedICPCList;
							var promise=$scope.clicked('selectedSystemList');
							promise.then(function(response) {
							angular.forEach($scope.icpcSavedList, function( value, index) {
								var newAr = {};
								newAr.id = value.mIcpcId;
								newAr.label = value.icpcDesc;
								$scope.selectedICPCdescripstion.push(newAr);
								console.log("selected icpc list",$scope.selectedICPCdescripstion)
							});
							$rootScope.stopLoader();
							});
							
							}
							
						});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
	
			$scope.update = function(){
				$rootScope.startLoader();
				$scope.saveBtnFlag = false;
				$scope.updateScope = true;
				$scope.updateBtnFlag = true;
				try{
					var date = new Date();  
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
					var data1={	
							templateId:$scope.templateId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							createdDate:$scope.dt,
							createdBy:$scope.createdBy,
							orgId:$scope.organisationId,
							orgUnitId:$scope.unitId,
							status:'A',
							isDeleted:"N",
							selectedSystemList:$scope.selectedSystemList,
							prevTemplateId:$scope.prevTemplateId
						}
					$scope.selectedICPCList=[];
					angular.forEach($scope.selectedICPCdescripstion,
							function(value, key) {
						angular.forEach($scope.dumyList,
								function(value1, key1) {
							if(value.id == value1.id)
								{
								var newlist={}; 
								newlist.id=value1.id;
								newlist.mIcpcSystemId=value1.mIcpcSystemId;
								$scope.selectedICPCList.push(newlist)
								}
								});
						     
							});
					
					data1.selectedICPCList=$scope.selectedICPCList;
					var URI = BASE_URL + ROOT_URL + "/api/global/updateICPCTemplateMapping";
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							
							$rootScope.stopLoader();
							$scope.saveBtnFlag = true;
							$scope.updateScope = false;
							$scope.updateBtnFlag = false;
							$scope.selectedSystemList=[];	
							$scope.selectedICPCdescripstion=[];
							$scope.templateId="";
							//$scope.getSaveTemplateList();
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error("Already exist",
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
					
					
					$rootScope.startLoader();
					var data={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							orgId:$scope.organisationId,
							orgUnitId:$scope.unitId,
							status:status,
							templateId:Id
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/updateICPCTemplateMappingStatus";
					console.log("update status",data);
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
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
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							orgId:$scope.organisationId,
							orgUnitId:$scope.unitId,
							status:'D',
							templateId:id
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/updateICPCTemplateMappingStatus";
					console.log("update status",$scope.common); //return false;
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							growl.success('Removed Successfully',
									{
										title : 'Success!'
									});
							$scope.selectedSystemList=[];	
							$scope.selectedICPCdescripstion=[];
							$scope.templateId="";
							//$scope.getSaveTemplateList();
							$scope.initMaster();
							//$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$rootScope.stopLoader();
					});
				}catch(e){
					console.log("Exception",e);
				}
				}
			
			$scope.clicked = function(type) {
				
				if (type == 'selectedSystemList') {
					
					//$scope.selectedICPCList = [];
					$scope.icpcDesciptionList =[];
					if ($scope.selectedSystemList.length > 0) {
						var selectedSystemType = [];
						angular.forEach($scope.selectedSystemList,
								function(value, index) {
							    selectedSystemType.push(value.id);
								});
						var data = {
								
							systemIdList : selectedSystemType
						}
						
					
						console.log(data);
						var URI = BASE_URL+ ROOT_URL+ "/api/global/getICPCSystemsCodesBySystemId";
						return BillingGenericService
								.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success") {
												$scope.getIcpcDesciptionList = [];
												$scope.dumyList= [];
											angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.mIcpcId;
																	data.mIcpcSystemId = value.mIcpcSystemId
																	data.label = value.icpcDesc;
																	console.log("data",JSON.stringify(data));
																		   $scope.getIcpcDesciptionList.push(data);
																		   $scope.dumyList.push(data);
																		
																});
											}
											console.log("getIcpcDesciptionList2",$scope.getIcpcDesciptionList);
											return response;
										});
					}

				}
			}

			 $scope.dropDownSetting1 = {
				        enableSearch: true,
				        scrollableHeight: '400px', 
				        scrollable: true
				    };
			/**Crud Operations End**/
}]);