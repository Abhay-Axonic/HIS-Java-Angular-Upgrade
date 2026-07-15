/**
 * 
 */


angular.module('myApp')
		.controller("dischargeSummaryTemplateMasterController",
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
			$scope.searchKeyword=null;
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
			
			$scope.listFieldDetails=[];
			$scope.selectDischargeSummaryTemplateList=[];
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				
				$scope.fieldDetailsMaster={
						dischargeFieldId:""
				 }
				$scope.common = {
						dischargeSummaryTempId:"",
						dischargeSummaryTempCode:"",
						dischargeSummaryTempDesc:"",
						departmentId:"",
						isHtml : 'N'
							
				};
				$scope.listLicenceDetailsMaster=[];
				var licenceDetailsMaster = angular.copy($scope.fieldDetailsMaster);
	        	$scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
				$scope.fieldObject=[];
				
				$scope.listFieldDetails=[];
				var fieldDetailsMaster = angular.copy($scope.fieldDetailsMaster);
	        	$scope.listFieldDetails.push(fieldDetailsMaster);
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			//get summary field list
			 $scope.addLicenceDetails = function(){
			    	if($scope.updateScope ==false)
     	        {
			    	  var licenceDetailsMaster = angular.copy($scope.fieldDetailsMaster);
					  $scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
					  console.log("$scope.listLicenceDetailsMaster",JSON.stringify($scope.listLicenceDetailsMaster));
     	       }
			      else if($scope.updateScope ==true)
		        	{
			    	  var licenceDetailsMaster = angular.copy($scope.fieldDetailsMaster);
//			    	  licenceDetailsMaster.unitId = $scope.unit.unitId;
					  $scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
					  $scope.listLicenceDetailsMaster.splice(deleteIndex, 1);
					  console.log($scope.listLicenceDetailsMaster);
		        	}	
			    }
			 $scope.removeLicenceDetails = function(deleteIndex)
				{
					$scope.listLicenceDetailsMaster.splice(deleteIndex, 1);
			     }
			$scope.getFieldList= function()
			{
				var payload = {
                    organizationId: $scope.organizationId,
                    unitId: $scope.unitId
				}
//				var URI=BASE_URL + ROOT_URL+ GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER+"/"+$scope.organizationId;
				var URI1 = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseListnew";
				GenericService
					.serviceAction("POST", URI1,
							payload)
					.then(
							function(response){
						
							$scope.summaryFieldList = [];
							if(response.data.status=="success")
								 $scope.summaryFieldList = response.data.listObject;
							console.log("summaryFieldList",$scope.summaryFieldList);
							});
			}
			$scope.getFieldList();
			//end summary field list
			
			//get Department List
			$scope.selectedSpeacilityList = [];
			$scope.getSepecialityActiveList = function () {

				var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
				var data = {
					organizationId: $scope.organizationId,
					unitId: $scope.unitId
				}
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success") {
						$rootScope.stopLoader();
						$scope.getSepecialityActiveList = [];
						angular.forEach(response.data.listObject,
							function (value, index) {
								var data = {};
								data.id = value.specialityId;
								data.label = value.specialityName;
								$scope.getSepecialityActiveList.push(data);
							});
						console.log("getSepecialityActiveList", $scope.getSepecialityActiveList);
					}
				});
			}
			$scope.getSepecialityActiveList();
			//End Department List
			//add field
			 $scope.fieldListObj=[];
			$scope.addFieldDetails = function()
			 {
				var obj = JSON.parse($scope.dischargeField);
				var isExists;
				function checkExistsOrNot(arr, val) {
					return arr
							.some(function(arr) {
								return (val.dischargeFieldId === arr.dischargeFieldId);
							});
				}


					isExists = checkExistsOrNot($scope.fieldListObj,obj);
					if (!isExists) 
					{
						var newArr = {};
						newArr.dischargeFieldId = obj.dischargeFieldId;
						newArr.dischargeFieldDesc = obj.dischargeFieldDesc;
		    		 	$scope.fieldListObj.push(newArr);
					} else {
						growl.error("Field is already Added.",
								{
									title : 'Error!'
								});
				
					}
				}
				
				// for remove field
				$scope.removeFieldDetails = function(index)
				{
					$scope.fieldListObj.splice(index,1);
				};
			
			
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
							unitId:unitId,
							searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null
							};
				
				var data2 = {
						      organizationId:organizationId,
						      unitId:unitId,
						      searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null
							};

				var searchKey = ($scope.searchKeyword != undefined && $scope.searchKeyword != null && $scope.searchKeyword != "" ) ? $scope.searchKeyword : null;
				
				var URI1 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST; 
				var URI2 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_COUNT+S+$scope.organizationId +S+ $scope.unitId+S+searchKey;
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
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

			$scope.searchDischargeSummaryTemplate = function(searchKeyword){

                $scope.loadingMsg="Loading...";
                if(searchKeyword.length>0){

                    $scope.commonList = [];
                    $rootScope.startLoader();
                    var data1 = {
                      organizationId:$scope.organizationId,
                      offset:$scope.offset,
                      noOfRecordsPerPage:$scope.noOfRecordsPerPage,
                      unitId:$scope.unitId,
                      searchKeyword: searchKeyword
                    };

                    var data2 = {
                      organizationId:$scope.organizationId,
                      unitId:$scope.unitId,
                      searchKeyword: searchKeyword
                    };

                    var URI1 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST;
                    var URI2 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_COUNT+S+$scope.organizationId +S+ $scope.unitId+S+searchKeyword;

                    GenericService.serviceAction("POST",URI1,data1).then(function(response){
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
                if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
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
				
				var URI = BASE_URL + ROOT_URL +  GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST;
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
				if($scope.pager.totalPages==0){
                    $scope.pager.totalPages=undefined;
                }
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
			
			$scope.save = function(){
				try{

					if($scope.fieldListObj == undefined || $scope.fieldListObj == null || $scope.fieldListObj.length == 0){
                						growl.warning("Please select field",
                									{
                										title : 'Warning!'
                									});
                									return false;
                					}
					if($scope.popUpFlag==false)
					{
					  $rootScope.startLoader();
					}

					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.fieldListTemplate = [];
					angular.forEach($scope.fieldListObj, function(value , key) {
						var data1={	
								dischargeFieldId:value.dischargeFieldId
							}
	 				$scope.fieldListTemplate.push(data1);
						
 		         });
					var data={	
							
							dischargeSummaryTempDesc:$scope.common.dischargeSummaryTempDesc,
							dischargeSummaryTempCode:$scope.common.dischargeSummaryTempCode,
							departmentId:$scope.common.departmentId,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt,
							selectDischargeSummaryTemplateList:$scope.fieldListTemplate,
							selectDischargeTemplateDepartmentList :$scope.selectedSpeacilityList,
							isHtml:$scope.common.isHtml,
						}

				if($scope.common.dischargeSummaryTempDesc && $scope.common.dischargeSummaryTempCode && ($scope.specialityType !=null && $scope.specialityType.length > 0))
					{
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + SAVE_DISCHARGE_SUMMARY_TEMPLATE_MASTER;
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
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							$scope.fieldListObj=[];
							$scope.selectedSpeacilityList=[];
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
				else
					{
					growl.error("Please Fill all details",
							{
								title : 'Error!'
							});
					}
					
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.showDetailsBtn=function(data){
				$scope.configDetails=data;
			}
			
			$scope.showFieldList = function(id){
				try{
					$scope.fieldMasterList=[];
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					var data={};
					var URI = BASE_URL + ROOT_URL + "/adt/getDischargeSummaryTemplateById" +"/"+ id +"/"+ $scope.organizationId +"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI,data).then(function(response){
						console.log("Edit",response);
						if(response.data.status == "success"){
 							$scope.fieldMasterList= response.data.listObject;
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						console.log("fdfd-",$scope.fieldMasterList);
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.saveFieldList=[];
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var data={};
					var URI = BASE_URL + ROOT_URL + "/adt/getDischargeSummaryTemplateById" +"/"+ id +"/"+ $scope.organizationId +"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI,data).then(function(response){
						console.log("Edit",response);
						if(response.data.status == "success"){
 							$rootScope.stopLoader();
 							$scope.fieldListObj= response.data.listObject;
 							$scope.saveFieldList=response.data.listObject;
 							$scope.selectedSpeacilityList=response.data.resObject;
							$scope.common=response.data.object;
							$scope.common.dischargeSummaryTempId=response.data.object.dischargeSummaryTempId;
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						console.log("fdfd-",$scope.fieldListObj);
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{

					if($scope.fieldListObj == undefined || $scope.fieldListObj == null || $scope.fieldListObj.length == 0){
                                						growl.warning("Please select field",
                                									{
                                										title : 'Warning!'
                                									});
                                									return false;
                                					}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					$rootScope.startLoader();
					
					$scope.previousSavedList=[];
					$scope.deptEditedList=[];
					$scope.fieldListTemplate = [];
					
					angular.forEach($scope.fieldListObj, function(value , key) {
						var data1={	
								dischargeFieldId:value.dischargeFieldId
							}
	 				$scope.fieldListTemplate.push(data1);
						
 		         });
					
					angular.forEach($scope.saveFieldList, function(value , key) {
						var data2={	
								dischargeFieldId:value.dischargeFieldId
							}
	 				$scope.previousSavedList.push(data2);
						
 		         });
					
					angular.forEach($scope.selectedSpeacilityList, function(value1 , key1) {
						var data3={	
								id:value1.id
							}
	 				$scope.deptEditedList.push(data3);
						
 		         });
					
					var data={	
							dischargeSummaryTempId:$scope.common.dischargeSummaryTempId,
								dischargeSummaryTempDesc:$scope.common.dischargeSummaryTempDesc,
								dischargeSummaryTempCode:$scope.common.dischargeSummaryTempCode,
								departmentId:$scope.common.departmentId,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								createdBy:$scope.createdBy,
								createdDate:$scope.dt,
								status:'A',
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt,
								selectDischargeSummaryTemplateList:$scope.fieldListTemplate,
								previousSavedList:$scope.previousSavedList,
								selectDischargeTemplateDepartmentList :$scope.deptEditedList,
								isHtml:$scope.common.isHtml,
							
						}
								
					var URI = BASE_URL + ROOT_URL + UPDATE_DISCHARGE_SUMMARY_TEMPLATE_MASTER;
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
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							$scope.fieldListObj=[];
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
			
			$scope.updateStatus = function(dischargeSummaryTempId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.dischargeSummaryTempId=dischargeSummaryTempId,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + UPDATE_DISCHARGE_SUMMARY_TEMPLATE_MASTER_STATUS;
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
			
			$scope.clicked = function(type) {
				
				if (type == 'speciality') {
					
					$scope.selectedSubSpecialityList = [];
					$scope.selectedServiceList = [];
					$scope.subSpecialityList = [];
					$scope.specialityType = [];
					if ($scope.selectedSpeacilityList.length > 0) {
						
						angular.forEach($scope.selectedSpeacilityList,
								function(value, index) {
							$scope.specialityType.push(value.id);
								});
					
						var isExists;
						function checkExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										return (val.id === arr.id);
									});
						}
						
						
					}

				}
			}
			
			$scope.getDeptListById = function(info) {
				$rootScope.startLoader();
				
				var data = {
						dischargeSummaryTempId:info.dischargeSummaryTempId,
						};
				
				var URI = BASE_URL + ROOT_URL + "/adt/getDischargeTempDeptListById";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.deptListById = response.data.listObject;
				});
			}
			/**Crud Operations End**/
}]);