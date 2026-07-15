/**
 * 
 */


angular.module('myApp')
		.controller("dischargeSummaryFieldMasterController",
				
		['$scope',
			'$rootScope',
			'$state',
			'$cookies',
			'GenericService',
			'PagerService',
			'promiseFactory',
			'$sessionStorage',
			'growl',
			'BillingGenericService',
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
			$scope.disColumnsIds=[];
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
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						dischargeFieldId:"",
						dischargeFieldCode:"",
						dischargeFieldDesc:"",
						summaryFieldDesc:"",
						dischargeFieldTemplateObj:""
							
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			
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
				var data1 = {
					      organizationId:$scope.organizationId,
							offset:offset,
							noOfRecordsPerPage:noOfRecordsPerPage,
							searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null
//							unitId:unitId
							};
				
				var data2 = {organizationId:$scope.organizationId, searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null};
				
				var URI1 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_FIELD_MASTER_LIST; 
				var URI2 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_FIELD_MASTER_COUNT;
					
				
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
						/*if($scope.updateBtnFlag==true){
							console.log($scope.pageNo)
							$scope.setPage($scope.pageNo,true);
						}
						else{*/
							console.log("else"+$scope.pageNo)
							$scope.setPage(1,false);

						//}
					}
				});
				
			}

            $scope.searchDischargeSummaryMaster = function(searchKeyword){

                $scope.loadingMsg="Loading...";
                if(searchKeyword.length>0){

                    $scope.commonList = [];
                    $rootScope.startLoader();
                    var data1 = {
                              organizationId:$scope.organizationId,
                                offset:$scope.offset,
                                noOfRecordsPerPage:$scope.noOfRecordsPerPage,
    //							unitId:unitId
                                searchKeyword: searchKeyword
                                };

                    var data2 = {organizationId:$scope.organizationId, searchKeyword: searchKeyword };

                    var URI1 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_FIELD_MASTER_LIST;
                    var URI2 = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_FIELD_MASTER_COUNT;

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
                                console.log("else"+$scope.pageNo)
                                $scope.setPage(1,false);
                        }
                    });
                }
                if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
                    $scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
                }
            }


			var pageOffset='';
			$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				pageOffset=offset;
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:$scope.organizationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						//unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL +  GET_DISCHARGE_SUMMARY_FIELD_MASTER_LIST;
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
					$scope.masterList($scope.organizationId,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
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
							
							dischargeFieldDesc:$scope.common.dischargeFieldDesc,
							dischargeFieldCode:$scope.common.dischargeFieldCode,
							summaryFieldDesc:$scope.common.summaryFieldDesc,
							dischargeFieldTemplateObj:$scope.common.dischargeFieldTemplateObj,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt
						
						}
				if($scope.common.dischargeFieldDesc && $scope.common.dischargeFieldCode && $scope.common.summaryFieldDesc)
				{
				
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + SAVE_DISCHARGE_SUMMARY_FIELD_MASTER;
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
			
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_ID +S+ id;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.dischargeFieldId=response.data.listObject[0].dischargeFieldId;
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
			
			$scope.pageNo='';
			$scope.update = function(){
				try{
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					$rootScope.startLoader();

					var data={	
								dischargeFieldId:$scope.common.dischargeFieldId,
								dischargeFieldDesc:$scope.common.dischargeFieldDesc,
								dischargeFieldCode:$scope.common.dischargeFieldCode,
								summaryFieldDesc:$scope.common.summaryFieldDesc,
								dischargeFieldTemplateObj:$scope.common.dischargeFieldTemplateObj,
//								organizationId:$scope.organizationId,
								//unitId:$scope.unitId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt
							
						}
								
					var URI = BASE_URL + ROOT_URL + UPDATE_DISCHARGE_SUMMARY_FIELD_MASTER;
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
							/*$scope.initMaster();*/
							$scope.common={}
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						/*$scope.offset=pageOffset;
						console.log($scope.offset)*/
						$scope.initMasterList($scope.offset, $scope.noOfRecordsPerPage);
						/*$scope.pageNo=($scope.offset/10)+1;
					    $scope.setPage($scope.pageNo,true)*/
					  
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(dischargeFieldId,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.dischargeFieldId=dischargeFieldId,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + UPDATE_DISCHARGE_SUMMARY_FIELD_MASTER_STATUS;
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
			
			
			$scope.addTemplate = function(info) {
				
				$scope.value = info.dischargeFieldTemplateObj;
		
			}
			
			$scope.getColumnsDetails = function(info) {
				$rootScope.startLoader();
				$scope.fieldId=info.dischargeFieldId;
				$scope.common.dischargeFieldId=info.dischargeFieldId;
				$scope.common.dischargeFieldDesc=info.dischargeFieldDesc;
				$scope.common.dischargeFieldCode=info.dischargeFieldCode;
				$scope.common.summaryFieldDesc=info.summaryFieldDesc;
				$scope.common.dischargeFieldTemplateObj=info.dischargeFieldTemplateObj;
				$scope.columnsDetailsList=[];
				var data = {
							organizationId:$scope.organizationId,
							dischargeFieldId:info.dischargeFieldId,
						   };
				
				var URI = BASE_URL + ROOT_URL +  "/adt/getDischargeSumColumnDetails";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.columnsDetailsList = response.data.listObject;
					
				});
			}
			$scope.objDisc={
					dischSumTabColMstId:"",
					isColumnRequired:true
			}
			$scope.selectedColumnIds=[];
			$scope.deletedColumnIds=[];
			$scope.addColumnData=function(obj, index){
//				var newArr={};
				$scope.common.dischargeFieldId=obj[0].dischargeFieldId;
				
				angular.forEach(obj,function(value1,key1){
						var newArr={};
						if(key1 == index && value1.isColumnRequired == false){
								
								newArr.dischargeFieldId=value1.dischargeFieldId;
								newArr.dischargeFieldDesc=value1.dischargeFieldDesc;
								newArr.dischSumTabColMstId=value1.dischSumTabColMstId;
								newArr.columnName=value1.columnName;
								newArr.isColumnRequired=value1.isColumnRequired;
								$scope.deletedColumnIds.push(newArr);
							
						}
						
					 });
				
				
				
//				var flag1 = false;
//				
//				$scope.counterFalse= false;
				
				/*angular.forEach($scope.selectedColumnIds,function(value1,index1){
					  if(value1.dischSumTabColMstId == obj.dischSumTabColMstId){
						  $scope.objDisc[value1.dischSumTabColMstId] = true;
						  value1.isColumnRequired = true;
							 $scope.counterFalse = true;
							 flag1 = false;
					  }
					  
					 });
					$scope.selectedAllColumnIds=$scope.selectedColumnIds;
					angular.forEach($scope.selectedAllColumnIds,function(value,index){
						if(value.dischSumTabColMstId == obj.dischSumTabColMstId){
							$scope.selectedColumnIds.splice(index,1);
							$scope.objDisc[value.dischSumTabColMstId] = false;
							value.isColumnRequired = false;
					        flag1 = true;
						}
					});*/
					
				
				
				
			}
			
			
			console.log("$scope.selectedAllColumnIds", $scope.selectedAllColumnIds);
			
			$scope.updateDischSumColumnToPrint = function() {
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				$rootScope.startLoader();
				$scope.selectedAllColumnIds=[];
				angular.forEach($scope.columnsDetailsList, function(value, key){
					angular.forEach(value.listOfColumns, function(value2, key2){
						var newArr={};
						if(value2.isColumnRequired == true){
								$scope.common.dischargeFieldId=value2.dischargeFieldId;
								newArr.dischargeFieldId=$scope.common.dischargeFieldId;
								newArr.dischargeFieldDesc=value.dischargeFieldDesc;
								newArr.dischSumTabColMstId=value2.dischSumTabColMstId;
								newArr.columnName=value2.columnName;
								newArr.tableName=value2.tableName;
								newArr.tableId=value2.tableId;
								newArr.columnName=value2.columnName;
								newArr.isColumnRequired=value2.isColumnRequired;
								$scope.selectedAllColumnIds.push(newArr);
							
						}
					});
				});
				
				var data = {
							dischargeFieldId:$scope.common.dischargeFieldId,
							tabColumnJson:JSON.stringify($scope.selectedAllColumnIds),
							unitId : $scope.unitId,
							organizationId : $scope.organizationId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt
						   };
				data.columnsListData=$scope.selectedAllColumnIds.concat($scope.deletedColumnIds);
				
				var URI = BASE_URL + ROOT_URL +  "/adt/discharge/saveDischargeSumTabColumnsPrintMapper";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					if (response.data.status == 'success')
					{
						$rootScope.stopLoader();
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
						$scope.common={};
						$scope.deletedColumnIds=[];
						$scope.selectedAllColumnIds=[];
					} else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
					}
					$rootScope.stopLoader();
					$scope.selectedColumnIds=[];
				});	
			}
			/**Crud Operations End**/
			$scope.tempListTable=[];
			$scope.tempListTableColumns=[];
			$scope.getColumnsList = function(info) {
				$rootScope.startLoader();
				
				$scope.columnsDetailsList=[];
				var data = {
						unitId:$scope.unitId,
						organizationId:$scope.organizationId,
							dischargeFieldId:info.dischargeFieldId,
						   };
				
				var URI = BASE_URL + ROOT_URL +  "/adt/getDischSumColumnsList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.columnsDetailsList = response.data.listObject;
					$scope.common.dischargeFieldId = $scope.columnsDetailsList[0].dischargeFieldId;
					$scope.common.dischargeFieldDesc = $scope.columnsDetailsList[0].dischargeFieldDesc;
					$scope.common.dischargeFieldCode = $scope.columnsDetailsList[0].dischargeFieldCode;
					$scope.common.summaryFieldDesc = $scope.columnsDetailsList[0].summaryFieldDesc;
//					$scope.common.dischargeFieldColumnJson = $scope.columnsDetailsList[0].dischargeFieldColumnJson;
//					$scope.savedFieldId = [];
//					$scope.savedFieldId = JSON.parse($scope.common.dischargeFieldColumnJson);
//					$scope.tempList=[];
					/*angular.forEach($scope.savedFieldId, function(value, key){
						if($scope.common.dischargeFieldId == value.dischargeFieldId){
							var newArr={};
							newArr.columnName= value.columnName,
								newArr.dischSumTabColMstId= value.dischSumTabColMstId,
								
									newArr.dischargeFieldId = value.dischargeFieldId,
									newArr.isColumnRequired = value.isColumnRequired,
									newArr.tableId = value.tableId,
									newArr.tableName = value.tableName
									$scope.tempList.push(newArr);
						}
						
					});*/
//					$scope.columnsDetailsList.listOfColumns=$scope.tempList;
//					$scope.columnsDetailsList[0].listOfColumns=$scope.tempList;
				});
			}
			
//			$scope.updateDischSumColumnToPrint = function() {
//				var date = new Date();
//				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
//				$rootScope.startLoader();
//				var data = {
//							dischargeFieldId:$scope.common.dischargeFieldId,
//							dischargeFieldDesc:$scope.common.dischargeFieldDesc,
//							dischargeFieldCode:$scope.common.dischargeFieldCode,
//							summaryFieldDesc:$scope.common.summaryFieldDesc,
//							dischargeFieldTemplateObj:$scope.common.dischargeFieldTemplateObj,
//							dischargeFieldColumnJson:JSON.stringify($scope.selectedColumnIds),
//							updatedBy:$scope.updatedBy,
//							updatedDate:$scope.dt
//						   };
//				
//				var URI = BASE_URL + ROOT_URL +  "/adt/updateDischSumColumnsToPrint";
//				console.log("URI", URI);
//				GenericService.serviceAction("POST", URI,data).then(function(response) {
//					if (response.data.status == 'success')
//					{
//						$rootScope.stopLoader();
//						growl.success(response.data.message,
//								{
//									title : 'Success!'
//								});
//						$scope.common={};
//					} else if(response.data.status == 'error')
//					{
//						$rootScope.stopLoader();
//						growl.error(response.data.message,
//								{
//									title : 'Error!'
//								});
//					}
//					$rootScope.stopLoader();
//					$scope.selectedColumnIds=[];
//				});	
//			}
			
			$scope.plusClick=function(){
				/*$(document).on({
					click : function() {
						//$("label.cbxTree-label").toggleClass('fcd-ie8'); //For the stupid ie8
						$(this).toggleClass('open');
						return false;
					}
				}, ".cbxTree-swicth");*/
				
				var x = document.getElementById("minusId");
				  if (x.style.display === "none" || x.style.display == "") {
				    x.style.display = "block";
				  } else {
				    x.style.display = "none";
				  }
			
			}
}]);