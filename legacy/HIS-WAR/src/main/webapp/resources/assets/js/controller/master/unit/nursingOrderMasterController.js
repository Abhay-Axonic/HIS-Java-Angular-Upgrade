
/**
 * 
 */


angular.module('myApp')
		.controller("nursingOrderMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			$scope.serviceHideFlag = true;
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
			
			var labConfig  =    $scope.checkLabConfig();
			$scope.initMaster = function(){
				
				var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						nursingOrderId:"",
						nursingOrderCode:"",
						nursingOrderDesc:"",
							
				};
				console.log("Common-init",$scope.common);
				$scope.specialityList = [];
				
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVESPECIALITY;
		     BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.specialityList = response.data.listObject;
						});
			}
			$scope.initMaster();
			
			
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
				
				var URI1 = BASE_URL + ROOT_URL + "/api/global/unit/getNursingOrderMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/unit/getNursingOrderMasterCount";
					
				
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
			
			
			//db serarch
			$scope.initMasterSearchList = function(serarchText)
			{
				$rootScope.startLoader();
				if(serarchText!=''){
//					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
					var data1 = {
							    nursingOrderCode:serarchText,
						        organizationId:$scope.organizationId,
								offset: $scope.offset,
								noOfRecordsPerPage:$scope.noOfRecordsPerPage,
								unitId: $scope.unitId
								};
					
					
					
					var URI1 = BASE_URL + ROOT_URL + "/api/global/unit/getNursingOrderMasterListSearch"; 
						
					
					GenericService.serviceAction("POST",URI1,data1).then(function(response){
						if(response.data.status == "success"){
						$rootScope.stopLoader();		
						$scope.commonList = [];
						$scope.commonList = response.data.listObject;
						console.log("commonList",$scope.commonList);
						$scope.commonListCount = response.data.listObject.length;
						console.log("commonListCount",$scope.commonListCount);
						$scope.setPage(1,false);
						}
					});
				}else{
					$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
					
					
				}
				
				
			
				
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
				
				var URI = BASE_URL + ROOT_URL +  "/api/global/unit/getNursingOrderMasterList";
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
			
			//get services 
			$scope.search="";
			$scope.searchServices = function(searchKeyword)
			{ 
				//need to change name (duplicate)
				$scope.servicesList=[];
				console.log("searchKeyword",searchKeyword);
				
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
				
				//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
				return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
			
			
			$scope.checkServiceMappedWithTest = function(serviceId,specialityId)
			{
				     $rootScope.startLoaderPopup();
				     var labSpecialityId = 0;
				     if(specialityId==labConfig.labSpecialityId)
						{
				    	   labSpecialityId = specialityId;
						}
					var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/"+labSpecialityId;
					var data = {serviceMasterId:serviceId,orgId:$scope.organizationId,unitId:$scope.unitId};
				    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
					{
						if(response.data.status == "success")
						{
							$rootScope.stopLoaderPopup();
							if(response.data.object==0)
							{
							   growl
								.warning('Service is Not mapped with Test!!',{
											title : 'Warning!'
										});
							   response.data.object = 1;
						     }
							//return response.data.object;
							return response.data.object+"##"+response.data.code;
						}
						else if(response.data.status == "error"){
							  $rootScope.stopLoaderPopup();
							  return response.data.object;
						   }
					 });
			}
			
			
			   $scope.getSubSpecialityList = function(id)
		        {
		        
						var specialityType = [];
						specialityType.push(id);
								
						var data = {
							organizationId : $scope.organizationId,
							unitId : $scope.unitId,
							specialityIdList : specialityType
						}
						console.log(data);
						var URI = BASE_URL+ ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYARRAYFORTARIFF;
						BillingGenericService
								.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success") {
												$scope.subSpecialityList = response.data.listObject;
												
											}
										});
		        }
			
	        $scope.addServiceToTempServiceList = function($item, $model, $label)
	        {
	        	$scope.serviceHideFlag=true;
	        	var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
	    		promise.then(function(obj) 
	    				{
	    					var  chkObj=obj.split("##");
	        	             if(chkObj[0]>0)
	        	            	 {

			        	            	 $scope.orderServiceId = $item.serviceMasterId;
			        	         		 $scope.orderServiceName = $item.serviceStandardName;
			        	         		 $scope.orderSpecialtyId = $item.specialityId;
			        	         		 $scope.orderSpecialtyName = $item.specialityName;
			        	         		 $scope.orderSubSpecialtyId = $item.subSpecialityId;
			        	             	 $scope.orderSubSpecialtyName = $item.subSpecialityName;
			        	             	// $scope.orderIsRefDoctorShare = $item.isRefDoctorShare;
			        	             	 $scope.status = $item.status;
			        	             	 $scope.selectedServiceTypeSpecialityId = $scope.orderSpecialtyId;
			        	             	 $scope.getSubSpecialityList($scope.orderSpecialtyId);
			        	             	 $scope.selectedSubServiceTypeSubSpecialityId = $scope.orderSubSpecialtyId;
			        	             	 $scope.isAppointable = $item.isAppointable;
			        	             	 $scope.orderDetailsIdP=chkObj[1]!=null && chkObj[1]!=undefined && chkObj[1]>0 ? chkObj[1] : undefined ; 

	        	            	 }
	        	             else{
	        	          

	        	            	 growl
	        	 				.warning(
	        	 						'Service is Not mapped with Test!!',
	        	 						{
	        	 							title : 'Warning!'
	        	 						});
	        	                 }
	    				 });
	    		
	    	}
	        
	        
			$scope.save = function(){
				try{
					
					if($scope.popUpFlag==false)
						{
						  $rootScope.startLoader();
						}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.$broadcast('show-errors-check-validity');
					var data={	
							
							nursingOrderDesc:$scope.common.nursingOrderDesc,
							nursingOrderCode:$scope.common.nursingOrderCode,
							serviceId: $scope.orderServiceId,
							specialityId: $scope.selectedServiceTypeSpecialityId,
							subSpecialityId:$scope.selectedSubServiceTypeSubSpecialityId,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt
						
						}
					if ($scope.nursingOrderMasterForm.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/unit/saveNursingOrderMaster";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							
							$scope.$broadcast('show-errors-reset');
							$scope.search="";
							$scope.orderServiceId="";
							$scope.selectedServiceTypeSpecialityId="";
							$scope.selectedSubServiceTypeSubSpecialityId="";
							
        	         		 $scope.orderServiceName = "";
        	         		 $scope.orderSpecialtyId = "";
        	         		 $scope.orderSpecialtyName = "";
        	         		 $scope.orderSubSpecialtyId ="";
        	             	 $scope.orderSubSpecialtyName = "";
        	             	 
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
				}
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
					var URI = BASE_URL + ROOT_URL + "/api/global/unit/getNursingOrderMasterById" +S+ id +S+ $scope.organizationId +S+ $scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.nursingOrderId=response.data.listObject[0].nursingOrderId;
							if($scope.common.serviceId != null){
								$item={};
								$model={};
								$label={};
								
								$item.serviceMasterId = $scope.common.serviceId;
								$item.serviceStandardName = $scope.common.serviceStandardName;
								$item.specialityId = $scope.common.specialityId;
								$item.specialityName = $scope.common.specialityName;
								$item.subSpecialityId =$scope.common.subSpecialityId;
								$item.subSpecialityName = $scope.common.subSpecialityName;
								$model= $item.serviceStandardName;
								$label= $item.serviceStandardName;
								$scope.search = $scope.common.serviceStandardName;
								$scope.addServiceToTempServiceList($item, $model, $label)	
							}
							
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
					$scope.$broadcast('show-errors-check-validity');
					$rootScope.startLoader();

					var data={	
								nursingOrderId:$scope.common.nursingOrderId,
								nursingOrderDesc:$scope.common.nursingOrderDesc,
								nursingOrderCode:$scope.common.nursingOrderCode,
								serviceId: $scope.orderServiceId,
								specialityId: $scope.selectedServiceTypeSpecialityId,
								subSpecialityId:$scope.selectedSubServiceTypeSubSpecialityId,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt
							
						}
								
					var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateNursingOrderMaster";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data);
					$rootScope.startLoader(); 
					if ($scope.nursingOrderMasterForm.$valid) {
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.search="";
							$scope.orderServiceId="";
							$scope.selectedServiceTypeSpecialityId="";
							$scope.selectedSubServiceTypeSubSpecialityId="";
							
        	         		 $scope.orderServiceName = "";
        	         		 $scope.orderSpecialtyId = "";
        	         		 $scope.orderSpecialtyName = "";
        	         		 $scope.orderSubSpecialtyId ="";
        	             	 $scope.orderSubSpecialtyName = "";
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
					}
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(nursingOrderId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.nursingOrderId=nursingOrderId,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateNursingOrderMasterStatus";
					console.log("update status",$scope.common);
					//return false;
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
					console.log("Exception",e);
				}
			}
			

			/**Crud Operations End**/
}]);