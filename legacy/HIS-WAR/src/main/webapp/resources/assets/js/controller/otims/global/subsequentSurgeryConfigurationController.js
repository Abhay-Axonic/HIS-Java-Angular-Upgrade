/**
 * 
 */

angular.module('myApp')
		.controller("subsequentSurgeryConfigurationController",
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
			$scope.orgId = 1;
			$scope.orgUnitId = 1;
			$scope.createdBy =1 ;
			$scope.updatedBy =1 ;
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
					specialityId:"",
					subSequentProcConfigId:null,
					subSequentDetailsList : []
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();				

			
			/**Crud Operations Start**/
			
			$scope.getActiveSpecialityAtUnitLevel=function(){
				try{
						var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
						var obj = {
								organizationId :$scope.orgId,
								unitId         :$scope.orgUnitId
						    }
						BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.specialityMasterList = response.data.listObject;
							$scope.common.specialityId = 0;
							console.log('splList',$scope.specialityMasterList);
						}
					});
				}catch(e){
					console.log("Exception-",e);
				}
			}
			$scope.getActiveSpecialityAtUnitLevel();
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			};
			
			$scope.initMasterList = function(orgId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								orgId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId
							};
				
				var data2 = {
						      orgId:orgId,
						      unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/otims/getSubSequentProcedureConfigList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/otims/getSubSequentProcedureConfigCount";
					
				
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
			
			$scope.masterList = function(orgId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						orgId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/otims/getSubSequentProcedureConfigList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
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
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.orgUnitId);
				if (flag)
				{
					$scope.masterList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.orgUnitId);
				}
			}

			$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.sequenceList = [];
			$scope.addSequenceDetails = function(){
				if($scope.sequenceId == undefined || $scope.sequenceId == null || $scope.sequenceId == ""){
					growl.warning("Please Select Sequence",
							{
								title : 'Warning!'
							});
					return false;
				}
				var isExists = false;
				var isInactiveSts = false;
				if($scope.sequenceList!=null && $scope.sequenceList.length>0){
					angular.forEach($scope.sequenceList,function(value,index){
						if(value.sequenceId == $scope.sequenceId && value.status == 'A'){
							isExists = true;
						}else if(value.sequenceId == $scope.sequenceId && value.status == 'I'){
							value.status = 'A';
							isInactiveSts = true;
						}
					});
				}
				if(isExists){
					growl.warning("Sequence Exists",
							{
								title : 'Warning!'
							});
					return false;
				}
				
				if(!isInactiveSts){
					var newArr = {};
					newArr.subSequentDetailsId = null;
					newArr.sequenceId = $scope.sequenceId;
					if( $scope.sequenceId == 1){
						newArr.sequenceName = "Primary";
					}else if($scope.sequenceId == 2){
						newArr.sequenceName = "Secondary";
					}else if($scope.sequenceId == 3){
						newArr.sequenceName = "Tertiary";
					}else if($scope.sequenceId == 4){
						newArr.sequenceName = "Quaternary";
					}else if($scope.sequenceId == 5){
						newArr.sequenceName = "Quinary";
					}
					newArr.status = 'A';
					newArr.discPercentage = '';
					newArr.subSequentDetailsInfoList = [];
					$scope.sequenceList.push(newArr);
				}
				$scope.sequenceId = "";
			}
			
			
			$scope.save = function(){
				try{
					$scope.$broadcast('show-errors-check-validity');
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					if($scope.common.specialityId == undefined || $scope.common.specialityId == null ){
						$rootScope.stopLoader();
						growl.warning("Please Select Speciality",
								{
									title : 'warning!'
								});
						return false;
					}
					if($scope.sequenceList!=null && $scope.sequenceList.length>0){
						var isFound = false;
						angular.forEach($scope.sequenceList,function(value,index){
							if((value.discPercentage ==undefined || value.discPercentage == '') && (value.discPercentage != null)){
								if(value.discPercentage === 0){
									
								}else{
									isFound = true;
								}
								
							}
						});
						//var found = $scope.sequenceList.find(obj => {return (obj.discPercentage ==undefined || obj.discPercentage == null || obj.discPercentage == "") && (obj.discPercentage<0)})
						if(isFound){
							$rootScope.stopLoader();
							growl.warning("Please Add Discount to Sequence",
									{
										title : 'warning!'
									});
							return false;
						}
					/*	$scope.finalSequenceInfoList = [];
						if($scope.sequenceList && $scope.sequenceList.length>0){
							angular.forEach($scope.sequenceList,function(value,index){
								if(value.subSequentDetailsInfoList!=null && value.subSequentDetailsInfoList.length>0){
									angular.forEach(value.subSequentDetailsInfoList , function(v,idx){
										if(v.discPercentage != 0){
											$scope.finalSequenceInfoList.push(v);
										}
									});
								}
								value.subSequentDetailsInfoList = $scope.finalSequenceInfoList;
								$scope.finalSequenceInfoList = [];
							});
							
						}*/
						$scope.common.subSequentDetailsList = $scope.sequenceList;
					}else{
						$rootScope.stopLoader();
						growl.warning("Please Select Sequence",
								{
									title : 'warning!'
								});
						return false;
					}
					$scope.common.createdBy=$scope.createdBy,
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.createdDate=$scope.dt,
					$scope.common.status='A',
					$scope.common.unitId=$scope.orgUnitId,
					$scope.common.orgId=$scope.orgId,
					$scope.common.updatedDate=$scope.dt
					
					console.log("$scope.common",$scope.common);
					if ($scope.subsequentSurgeryConfigform.$valid) {
					var URI = BASE_URL + ROOT_URL + "/api/otims/saveSubSequentProcedureConfig";
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.sequenceList = [];
							$scope.initMaster();
							$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
					var URI = BASE_URL + ROOT_URL +"/api/otims/getSubSequentProcedureConfigById/"+id+"/"+$scope.orgId+"/"+$scope.orgUnitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.specialityId=response.data.listObject[0].specialityId.toString();
							$scope.getSubSequenceDEtailsById(id,0);
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
			
			
			/*$scope.update = function(){
				try{
					$scope.$broadcast('show-errors-check-validity');
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt
					if($scope.sequenceList!=null && $scope.sequenceList.length>0){
						$scope.common.sequenceList = $scope.sequenceList;
					}				
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateSubSequentProcedureConfig";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",$scope.common);
					$rootScope.startLoader(); 
					if ($scope.subsequentSurgeryConfigform.$valid) {
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage,$scope.orgUnitId);
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
			}*/
			
			$scope.updateStatus = function(contractGradeMasterId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
				//	$scope.common.contractGradeMasterId=contractGradeMasterId,
					$scope.common.subSequentProcConfigId=contractGradeMasterId,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/otims/updateSubSequentProcedureConfigStatus";
					console.log("update status",$scope.common);
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
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
			
			/**Crud Operations End**/
			
			$scope.removeSubsequentDetails = function (index){
				if($scope.sequenceList[index].subSequentDetailsId!=null){
					$scope.sequenceList[index].status = 'I';
				}else{
					$scope.sequenceList.splice(index);
				}
					
			}
			
			$scope.subSequenceDetailsList = [];
			$scope.getSubSequenceDEtailsById = function(id,flag){
				try{
				$rootScope.startLoader();
				$scope.sequenceList = [];
				var data = {
						orgId : $scope.orgId,
						unitId : $scope.orgUnitId,
						subSequentProcConfigId : id
				}
				
				var URI = BASE_URL + ROOT_URL +"/api/otims/getSubSequentDetailsListById";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					
					if(response.data.status == "success"){
						$rootScope.stopLoader();
						$scope.subSequenceDetailsList = [];
						if(response.data.listObject!=null && response.data.listObject.length>0){
							angular.forEach(response.data.listObject,function(value,index){
								if(value.sequenceId== 1 ){
									value.sequenceName = "Primary";
								}else if(value.sequenceId == 2 ){
									value.sequenceName = "Secondary";
								}else if(value.sequenceId == 3 ){
									value.sequenceName = "Tertiary";
								}else if(value.sequenceId == 4 ){
									value.sequenceName = "Quaternary";
								}else if(value.sequenceId == 5 ){
									value.sequenceName = "Quinary";
								}
								if(flag == 1){
									$scope.subSequenceDetailsList.push(value);
								}else{
									if(value.discPercentage ==  null ){
										value.isDisabled = true;
									}
									$scope.sequenceList.push(value);
								}
								
							});
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
			
			$scope.infoIndex;
			$scope.subsequentInfoDetailsList = [];
			$scope.getSubsequentInfoDetails = function(index){
				try{
					$rootScope.startLoader();
					$scope.infoIndex = index;
					
						var data = {
								organizationId : $scope.orgId,
								unitId : $scope.orgUnitId
						}
						
						$scope.subsequentInfoDetailsList = [];
						var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialServices"
						GenericService.serviceAction("POST",URI,data).then(function(response){
							
							if(response.data.status == 'success'){
								if(response.data.listObject.length>0){
									$rootScope.stopLoader();
										if($scope.sequenceList[$scope.infoIndex].subSequentDetailsInfoList == null || $scope.sequenceList[$scope.infoIndex].subSequentDetailsInfoList.length == 0){
											angular.forEach(response.data.listObject,function(v,k){
												var newArr = {};
												newArr.serviceId = v.serviceId;
												newArr.serviceName = v.serviceName;
												newArr.subSequentDetailsInfoId = null;
												newArr.subSequentDetailsId = $scope.sequenceList[$scope.infoIndex].subSequentDetailsId;
												newArr.discPercentage = 0;
												newArr.status = 'A';
												newArr.orgId = $scope.orgId;
												newArr.unitId = $scope.orgUnitId;
												newArr.createdBy = $scope.userId;
												newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
												newArr.updatedBy = $scope.userId;
												newArr.updatedDate =moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
												$scope.subsequentInfoDetailsList.push(newArr);
											});
											$scope.sequenceList[$scope.infoIndex].subSequentDetailsInfoList = $scope.subsequentInfoDetailsList;
										}
									$("#subsequentInfoDetails").show();
									$rootScope.stopLoader();
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
			
			$scope.closeSubsequentInfoDetails = function(){
				
				var obj = $scope.sequenceList[$scope.infoIndex].subSequentDetailsInfoList.find(arr=>{return arr.discPercentage > 0});
				
				if(obj!=undefined){
					$scope.sequenceList[$scope.infoIndex].discPercentage = null;
					$scope.sequenceList[$scope.infoIndex].isDisabled = true;
				}else{
					$scope.sequenceList[$scope.infoIndex].isDisabled = false;
				}
				
				$("#subsequentInfoDetails").hide();
			}
}]);