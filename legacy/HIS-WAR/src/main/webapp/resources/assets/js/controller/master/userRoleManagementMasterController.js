/**
 * 
 */

angular.module('myApp')
		.controller("userRoleManagementMasterController",
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
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
			$scope.isAdviceAccess=cookieObjectAppConfig.isAdviceTabVisible;
			
			/*var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
			if(cookieObjectAppConfig == undefined){
				$state.go('applicationConfiguration');
				return;
			}*/
			
			var cookieObject, cookieObjectAppConfig;
			[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
			
			var labConfig  =  $scope.checkLabConfig();
			$scope.isReportRecall = labConfig.isReportRecall;
			$scope.isSuperUser=cookieObject.isSuperUser;
			if (cookieObjectAppConfigSecond != undefined) {
			$scope.roleId=cookieObjectAppConfigSecond.roleId;
			}
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy = cookieObject.userId;
			$scope.roleName = cookieObjectAppConfig.roleName;
			if (cookieObjectAppConfigSecond != undefined) {
			$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
			}
			
			$scope.selectedRoleList=[];	
			$scope.selectedDashboardDesList=[];	
			
			$scope.docRoleId=cookieObjectAppConfig.doctorRoleId;
			$scope.nurseRoleId=cookieObjectAppConfig.nurseRoleId;
			
						
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						userRoleMpprId:"",
						userId:"",
						empCode:"",
						empName:"",
						contactNo:"",
						roleName:"",
						name:"",
					    dashboardId:"",
					    isRecoll: false,
					    isRisRecall : false,
					    isRenderedServiceCancelled: false,
					    isUpdatePatientReg: false,
					    updateDischargeSummary:false,
					    isUpdateIpdChargesRemark: false,
					    manualMpesa : false,
					    enableBlockPrint:false,
					    opdBillCancellation:false,
					    ipdBillCancellation:false,
					    billSettlement:false,
					    ipBillSettlement : false,
					    serviceRateEditable : false,
					    isForceUpdateServicePackage : false,
					    pharmacyBillCancellation : false,
					    pharmacyReceiptCancellation : false,
					    sampleRecollection : false,
					    nursingOpScan : false,
					    nursingIpScan : false,
					    doctorOpScan : false,
					    doctorIpScan : false,
					    changePayeeRights : false,
					    extendPdd : false,
					    isBillManipulationAllowed:false
				};
				console.log("Common-init",$scope.common);
				
				$scope.userOtTabs = {
						userOtTabMapperId:null,
						userId:"",
						otDetails:false,
						procedureDetails:false,
						service:false,
						inventry:false,
						equipment:false,
						cssdKit:false,
						notes:false,
						timeSheet:false,
                        medication:false,
                        implant:false,
                        medicalGas:false,
                        anesthesiaNotes: false,
                        surgeryNotes: false,
                        dischargeNotes : false,
                        followUpNotes : false,
						organizationId:$scope.organisationId,
						unitId:$scope.unitId,
						createdBy:$scope.createdBy,
						updatedBy:$scope.updatedBy,
				};
				$scope.getHeaderTabList = [];
				$scope.tabObj = [];
				$scope.tabObjId = [];
			}
			$scope.initMaster();
			
			
			
			
			$scope.getActiveList = function(){
				
			var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";
			var data = {
					organisationId:$scope.organisationId,
				      unitId:$scope.unitId
			}
			GenericService.serviceAction("POST",URI,data).then(function(response){
				if(response.data.status == "success"){
				$rootScope.stopLoader();					
				$scope.getActiveList = [];
				angular.forEach(response.data.listObject,
								function(value,index) {
									var data = {};
									data.id = value.roleId;
									data.label = value.roleName;
									if($scope.isSuperUser=='Y')
										{
										 $scope.getActiveList.push(data);
										}
									else 
										{
										if(data.id!=response.data.object)
											{
											$scope.getActiveList.push(data);
											}
										}
								});
				console.log("getActiveRoleList",$scope.getActiveList);
				}
			});
			}
			$scope.getActiveList();
			
			$scope.getListByName = function(name){
				
				var URI = BASE_URL + ROOT_URL + "/api/global/searchEmployeeByName";
				var data = {
						 organizationId:$scope.organisationId,
					      unitId:$scope.unitId,
					      firstName:name
					}
				return GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == "success")
							console.log("getListByName", response.data.listObject);
						return response.data.listObject;
					});
						
				}
				
			
			$scope.selectPatientId = function(info, model,label) {
				console.log(info);
				$scope.common.userId = info.userId;
				$scope.common.userId = info.userId;
			}
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organisationId:organisationId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId,
								searchKeyword:  ($scope.searchKeyword != null) ? $scope.searchKeyword : null
							};
				
				var data2 = {
						      organisationId:organisationId,
						      unitId:unitId,
						      searchKeyword:  ($scope.searchKeyword != null) ? $scope.searchKeyword : null
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserRoleMapperList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getUserRoleMapperCount";
					
				
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


		    $scope.searchUserRoleManagementMaster = function(searchKeyword){

                $scope.loadingMsg="Loading...";
                if(searchKeyword.length>0){

                    $scope.commonList = [];
                    $rootScope.startLoader();
                    var data1 = {
                                    organisationId:$scope.organisationId,
                                    offset:$scope.offset,
                                    noOfRecordsPerPage:$scope.noOfRecordsPerPage,
                                    unitId:$scope.unitId,
                                    searchKeyword: searchKeyword
                                };

                    var data2 = {
                                  organisationId:$scope.organisationId,
                                  unitId:$scope.unitId,
                                  searchKeyword: searchKeyword
                                };

                    var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserRoleMapperList";
                    var URI2 = BASE_URL + ROOT_URL + "/api/global/getUserRoleMapperCount";


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
                if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
                    $scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
                }
            }

			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organisationId:organisationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getUserRoleMapperList";
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
			    if($scope.pager.totalPages==0){
			        $scope.pager.totalPages =  undefined;
			    }
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
			
			/*$scope.save = function(){
				
				try{
					
//					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
					$rootScope.startLoader();
					angular.forEach($scope.selectedBillingCategory,
							function(value, index) {
						     $scope.bedBilling(value.id);
							});
					var data1={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:'A',
							organisationId:$scope.organisationId,
							unitId:$scope.unitId,
							userId:$scope.common.userId,
							roleId:$scope.common.roleId,
							createdDate:$scope.dt,
							createdBy:$scope.createdBy,
							userRoleMapperList:$scope.selectedRoleList
													
					}
				
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/saveUserRoleMapper";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data1); //return false;  
					
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.selectedRoleList=[];	
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
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
			}*/
			$scope.selectedUserdasboardList=[];
			$scope.save = function(){
				$scope.saveBtnFlag = true;
				$scope.updateScope = true;
				$scope.updateBtnFlag = false;
				try{
					if($scope.common.userId == undefined || $scope.common.userId == null || $scope.common.userId == "" 
						|| $scope.common.empName == undefined ||$scope.common.empName == null || $scope.common.empName=="")
					{
						$rootScope.stopLoader();
						growl.error("Please Select Employee",
								{
									title : 'error!'
								});
						return false;
					}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$rootScope.startLoader();
					angular.forEach($scope.selectedBillingCategory,
							function(value, index) {
						     $scope.bedBilling(value.id);
							});
					var data1={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:'A',
							organisationId:$scope.organisationId,
							unitId:$scope.unitId,
							userId:$scope.common.userId,
							roleId:$scope.common.roleId,
							dashboardId:$scope.common.dashboardId,
							createdDate:$scope.dt,
							createdBy:$scope.createdBy,
							createdBy:$scope.createdBy,
							userRoleMapperList:$scope.selectedRoleList,
							isRecoll: $scope.common.isRecoll,
							isRisRecall : $scope.common.isRisRecall,
							isRenderedServiceCancelled: $scope.common.isRenderedServiceCancelled,				
							isUpdatePatientReg: $scope.common.isUpdatePatientReg,
							updateDischargeSummary :$scope.common.updateDischargeSummary,
							isUpdateIpdChargesRemark: $scope.common.isUpdateIpdChargesRemark,
							manualMpesa : $scope.common.manualMpesa,
							enableBlockPrint : $scope.common.enableBlockPrint,
							opdBillCancellation : $scope.common.opdBillCancellation,
							ipdBillCancellation : $scope.common.ipdBillCancellation,
							billSettlement : $scope.common.billSettlement,
							ipBillSettlement : $scope.common.ipBillSettlement,
							serviceRateEditable : $scope.common.serviceRateEditable,
							isForceUpdateServicePackage :$scope.common.isForceUpdateServicePackage,
							pharmacyBillCancellation : $scope.common.pharmacyBillCancellation,
						    pharmacyReceiptCancellation : $scope.common.pharmacyReceiptCancellation,
						    sampleRecollection : $scope.common.sampleRecollection,
						    nursingOpScan : $scope.common.nursingOpScan,
						    nursingIpScan : $scope.common.nursingIpScan,
						    doctorOpScan : $scope.common.doctorOpScan,
						    doctorIpScan : $scope.common.doctorIpScan,
						    changePayeeRights : $scope.common.changePayeeRights,
						    extendPdd : $scope.common.extendPdd,
                            ipRenderedDateChange : $scope.common.ipRenderedDateChange,
                            isBillManipulationAllowed : $scope.common.isBillManipulationAllowed,
						    
					}
					
					/*angular.forEach($scope.selectedDashboardDesList,
							function(value, key) {
						angular.forEach($scope.dumyList,
								function(value1, key1) {
							if(value.id == value1.id)
								{
								var newlist={}; 
								newlist.id=value1.id;
								newlist.roleId=value1.roleId;
								$scope.selectedUserdasboardList.push(newlist)
								}
								});
						     
							});
					
					data1.selectedUserdasboardList=$scope.selectedUserdasboardList;*/
					console.log("data",data1);
			
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/saveUserRoleMapper";
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
							$scope.selectedRoleList=[];	
							$scope.selectedDashboardDesList=[];
							$scope.userId="";
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
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
		
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/api/global/getUserById/"+id+"/"+$scope.organisationId+"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
//							$scope.common.roleId = response.data.listObject[0].roleId.toString();
							console.log("edit id ",$scope.common); //return false;
							$scope.common.dashboardId = response.data.listObject[0].dashboardId;
							$scope.selectedRoleList = [];
							$scope.oldSelectedRoleList =[];
							angular .forEach(response.data.listObject, function( value, index) {
								var newAr = {};
								newAr.id = value.roleId;
								$scope.selectedRoleList.push(newAr);
								$scope.oldSelectedRoleList.push(newAr);
							});
							if($scope.selectedRoleList !== undefined && $scope.selectedRoleList !== null && $scope.selectedRoleList.length > 0){

								$scope.clicked('selectedRoleList');
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
					if($scope.common.userId == undefined || $scope.common.userId == null || $scope.common.userId == "" 
						||$scope.common.empName == undefined ||$scope.common.empName == null || $scope.common.empName=="")
					{
						$rootScope.stopLoader();
						growl.error("Please Select Employee",
								{
									title : 'error!'
								});
						return false;
					}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
					$rootScope.startLoader();
					var data1={	
							userRoleMpprId:$scope.common.userRoleMpprId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							organisationId:$scope.organisationId,
							unitId:$scope.unitId,
							dashboardId:$scope.common.dashboardId,
							userId:$scope.common.userId,
							roleId:$scope.common.roleId,
							status:'A',
							userRoleMapperList:$scope.selectedRoleList,
							isRecoll: $scope.common.isRecoll,
							isRisRecall : $scope.common.isRisRecall,
							isRenderedServiceCancelled: $scope.common.isRenderedServiceCancelled,	
							isUpdatePatientReg: $scope.common.isUpdatePatientReg,
							updateDischargeSummary :$scope.common.updateDischargeSummary,
							isUpdateIpdChargesRemark: $scope.common.isUpdateIpdChargesRemark,
							manualMpesa : $scope.common.manualMpesa,
							enableBlockPrint : $scope.common.enableBlockPrint,
							opdBillCancellation : $scope.common.opdBillCancellation,
							ipdBillCancellation : $scope.common.ipdBillCancellation,
							billSettlement : $scope.common.billSettlement,
							ipBillSettlement : $scope.common.ipBillSettlement,
							serviceRateEditable : $scope.common.serviceRateEditable,
							isForceUpdateServicePackage : $scope.common.isForceUpdateServicePackage,
							pharmacyBillCancellation : $scope.common.pharmacyBillCancellation,
						    pharmacyReceiptCancellation : $scope.common.pharmacyReceiptCancellation,
						    sampleRecollection : $scope.common.sampleRecollection,
						    nursingOpScan : $scope.common.nursingOpScan,
						    nursingIpScan : $scope.common.nursingIpScan,
						    doctorOpScan : $scope.common.doctorOpScan,
						    doctorIpScan : $scope.common.doctorIpScan,
						    changePayeeRights : $scope.common.changePayeeRights,
						    extendPdd : $scope.common.extendPdd,
						    ipRenderedDateChange : $scope.common.ipRenderedDateChange,
						    isBillManipulationAllowed : $scope.common.isBillManipulationAllowed,
						    depositReceiptCancellation : $scope.common.depositReceiptCancellation
						}
									
					var URI = BASE_URL + ROOT_URL + "/api/global/updateUserRoleMapper";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data1); //return false;
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.selectedRoleList=[];	
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					
					$rootScope.startLoader();
					var data={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:status,
							userRoleMpprId:Id
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/updateUserRoleMapperStatus";
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
			
		
			$scope.getUserId = function(userId){
				$scope.roleList=[];
				var data= {
						organisationId:$scope.organisationId,
						unitId:$scope.unitId,
						userId:userId
						};
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserRoleNameByUserId"; 
					$rootScope.startLoader();
					return GenericService.serviceAction("POST",URI1,data).then(function(response){
						if(response.data.status == "success"){											
							$scope.roleList = response.data.listObject;
							console.log("roleList",$scope.roleList);
							$rootScope.stopLoader();
							return response;
						}
					});
						
				}

			/********************/
			
		/*$scope.clicked = function(type) {
				
				if (type == 'selectedRoleList') {
					
					$scope.selectedRoleList = [];
					if ($scope.selectedRoleList.length > 0) {
						var selectDashboardList = [];
						angular.forEach($scope.selectedRoleList,
								function(value, index) {
														   selectDashboardList.push(value.id);
														});
						var data = {
										roleIdList : selectDashboardList
									}
						console.log(data);
						var URI = BASE_URL+ ROOT_URL+ "/api/getDashboardMasterListByRoleId";
						GenericService.serviceAction("POST",URI, data)
								.then(function(response) {
											if (response.data.status == "success") {
												$scope.getDashboardDesciptionList = [];
												$scope.dumyList= [];
											angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.dashboardId;
																	data.roleId = value.roleId
																	data.label = value.dashboardDesc;
																	console.log("data",JSON.stringify(data));
																		   $scope.getDashboardDesciptionList.push(data);
																		   $scope.dumyList.push(data);
																		
																});
											}
											console.log("getIcpcDesciptionList2",$scope.getIcpcDesciptionList);
										});
					}

				}
			}*/
			$scope.clicked = function(type) {
				$scope.selectDashboardList=[];
				if (type == 'selectedRoleList') {
					if ($scope.selectedRoleList.length > 0) {
						var selectDashboardList = [];
						angular.forEach($scope.allRoleList,function(value,key){
							var flag=false;
						angular.forEach($scope.selectedRoleList,
								function(value1, key) {
														if(value1.id == value.roleId)
															{
															flag=true;
															
															}
								});
						      if(flag==true){
						    	  var newlist={}; 
									//newlist.id=value1.id;
									newlist.roleId=value.roleId;
									newlist.dashboardId = value.dashboardId;
									newlist.dashboardDesc = value.dashboardDesc;
									$scope.selectDashboardList.push(newlist)  
						      }
						});
				}
				
				}
			}
			
			/**********************/
			$scope.allRoleList=[];
			$scope.getAllRoleList = function() {
				$rootScope.startLoader();
				var data = {
						organizationId:$scope.organisationId,
					     unitId:$scope.unitId
						};
				var URI = BASE_URL+ ROOT_URL+ "/api/getDashboardMasterListByRoleId";
				//var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.allRoleList = response.data.listObject;
					
					console.log("allRoleList",$scope.allRoleList);
				});
			}
			$scope.getAllRoleList();
			/**Crud Operations End**/
			
			$scope.getUserOtTabs = function(userId){
				$scope.setFirstTabLevel(4);
				$rootScope.startLoader();
				$scope.userOtTabs.userId= userId;
				var URI = BASE_URL+ ROOT_URL+ "/api/global/getUserOtTabsByUserId/"+$scope.userOtTabs.userId;
				console.log("URI", URI);
				GenericService.serviceAction("GET", URI,{}).then(function(response) {
					$rootScope.stopLoader();
					if(response.data.listObject != null && response.data.listObject.length>0){
						
						$scope.userOtTabs.cssdKit = response.data.listObject[0].cssdKit;
						$scope.userOtTabs.equipment = response.data.listObject[0].equipment;
						$scope.userOtTabs.inventry = response.data.listObject[0].inventry;
						$scope.userOtTabs.notes = response.data.listObject[0].notes;
						$scope.userOtTabs.otDetails = response.data.listObject[0].otDetails;
						$scope.userOtTabs.procedureDetails = response.data.listObject[0].procedureDetails;
						$scope.userOtTabs.service = response.data.listObject[0].service;
						$scope.userOtTabs.timeSheet = response.data.listObject[0].timeSheet;
						$scope.userOtTabs.userOtTabMapperId = response.data.listObject[0].userOtTabMapperId;
                        $scope.userOtTabs.medication = response.data.listObject[0].medication;
                        $scope.userOtTabs.implant = response.data.listObject[0].implant;
                        $scope.userOtTabs.medicalGas = response.data.listObject[0].medicalGas;
                        $scope.userOtTabs.anesthesiaNotes = response.data.listObject[0].anesthesiaNotes;
                        $scope.userOtTabs.surgeryNotes = response.data.listObject[0].surgeryNotes;
                        $scope.userOtTabs.dischargeNotes  = response.data.listObject[0].dischargeNotes;
                        $scope.userOtTabs.followUpNotes  = response.data.listObject[0].followUpNotes;
                        /*if($scope.userOtTabs.inventry == true){
                        	$(".Inventory_Content").toggle(this.checked);
                        }
                        
                        if($scope.userOtTabs.notes == true){
                        	$(".Notes_Content").toggle(this.checked);
                        }*/
                        
					}
				});
			}
			
			
			$scope.saveUserOtTabs = function() {
				$rootScope.startLoader();
				var URI = BASE_URL+ ROOT_URL+ "/api/global/saveUserOtabsMapper";
				GenericService.serviceAction("POST", URI, $scope.userOtTabs).then(function(response) {
					
					if (response.data.status == 'success')
					{
						$scope.initMaster();
						$rootScope.stopLoader();
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
						
                        $("#infoModal").modal("hide");
					} else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
					}				});
			}
			
			
			//MIS Report Code
			$scope.reportObj = [];
			$scope.reportObjId = [];
			$scope.selectedReprtHeaderId1=[];
			$scope.selectedReprtId1=[];
			$scope.selectedReprtHeaderId=[];
			$scope.selectedReprtId=[];

			$scope.getMISTabInfo=function(info){

				$scope.reportObj = [];
				$scope.reportObjId = [];
				$scope.selectedReprtHeaderId1=[];
				$scope.selectedReprtId1=[];
				$scope.selectedReprtHeaderId=[];
				$scope.selectedReprtId=[];
				
				$scope.userRoleMpprId=info.userRoleMpprId;
				$scope.misUserId=info.userId;
				$scope.savedmisUserId = [];
				$scope.savedmisUserId = JSON.parse(info.misReportIds);
				
			     $scope.setFirstTabLevel(6);
			     
					var data = {
							
							organizationId : $scope.organizationId,
		
					}

					var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getReportGroupHeaderForUserRoleManagement";

					GenericService.serviceAction("POST", URI, data).then(function(response) {

					$rootScope.stopLoader();
					$scope.getHeaderReportList = [];
					$scope.getHeaderReportList = response.data.listObject;
                   //Bind saved Data					
					 angular.forEach($scope.reportObj,function(value,index){
							$scope.reportObj[index] = false;
							var i = $scope.selectedReprtHeaderId1.indexOf(value);
							$scope.selectedReprtHeaderId1.splice(i,1);
							});
						 
					 angular.forEach($scope.reportObjId,function(value,index){
						 $scope.reportObjId[index] = false;
						 var i = $scope.selectedReprtId1.indexOf(value);
						 $scope.selectedReprtId1.splice(i,1);
						 });
					 
				    console.log("saved data list",info.listMISReports);
				    
						 angular.forEach($scope.savedmisUserId,function(value,index){
							 var temp = {
									 		reportGroupHeaderId:value.reportGroupHeaderId
							 			};
							 $scope.selectedReprtHeaderId1.push(temp);
							 $scope.reportObj[value.reportGroupHeaderId] = true;
							 $scope.selectedReprtHeaderId.push(temp);

							 angular.forEach(value.selectedReprtId,function(value1,index1){
								 var report = {
										 reportGroupHeaderId:value.reportGroupHeaderId,
										 reportId:value1.reportId
								 }
								 var report1 = {
										 reportId:value1.reportId
								 }

								 $scope.selectedReprtId1.push(report);
								 $scope.reportObjId[value1.reportId] = true;
								 $scope.selectedReprtId.push(report1);
								 });
							 
						
						 });
						 console.log("$scope.selectedReprtHeaderId1",$scope.selectedReprtHeaderId1);
						 console.log("$scope.selectedReprtId1",$scope.selectedReprtId1);

				});
			};

			$scope.getReportListByReportGroupHeaderId = function(info) {


			var data = {
						organizationId : $scope.organizationId,
						reportGroupHeaderId : info.reportGroupHeaderId
					   }

				var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getReportDetailsList";
	
				GenericService.serviceAction("POST", URI, data).then(function(response) {

				$rootScope.stopLoader();
				$scope.getReportList = [];
				$scope.getReportList = response.data.listObject;
				console.log("getActiveList",$scope.getReportList);

			  });
			}
			
			
			$scope.selectReportHeader = function(info){

				var flag = false;
				angular.forEach($scope.selectedReprtHeaderId1,function(value,index){
				 if(value.reportGroupHeaderId == info.reportGroupHeaderId){
					 
					 $scope.selectedReprtHeaderId1.splice(index,1);
					 $scope.reportObj[value.reportGroupHeaderId] = false;
					 angular.forEach(info.listReportDetails,function(value1,index1){
						 angular.forEach($scope.selectedReprtId1,function(value2,index2){
							 if(value2.reportGroupHeaderId == info.reportGroupHeaderId){
								 $scope.selectedReprtId1.splice(index2,1);
							 }
							 $scope.reportObjId[value1.reportId] = false;
						 });
	                     });
					 flag = true;
				 }
				})
				if(!flag){
					var temp = {reportGroupHeaderId:info.reportGroupHeaderId};
					$scope.selectedReprtHeaderId1.push(temp);
					
					 angular.forEach(info.listReportDetails,function(value1,index1){
	                     $scope.reportObjId[value1.reportId] = true;
	                     var tempaLL = {
	                             reportId:value1.reportId,
	                             reportGroupHeaderId:value1.reportGroupHeaderId
	                         };
	                     $scope.selectedReprtId1.push(tempaLL);
	                    
	                     
	                     });
					}
				
				 console.log("$scope.selectedReprtHeaderId1",$scope.selectedReprtHeaderId1);
				 console.log("$scope.selectedReprtId1",$scope.selectedReprtId1);
			
				}
			
			

			$scope.selectReport = function(demo,list){
				var flag1 = false;
				var length = list.length;
				$scope.counterFalse= false;
					angular.forEach($scope.selectedReprtId1,function(value,index){
						if(value.reportId == demo.reportId){
							$scope.selectedReprtId1.splice(index,1);
							$scope.reportObjId[value.reportId] = false;
					        flag1 = true;
						}
					});
					
				angular.forEach($scope.selectedReprtId1,function(value1,index1){
				  if(value1.reportGroupHeaderId == demo.reportGroupHeaderId){
						 $scope.counterFalse = true;
				  }
				 });
				
			  if(!$scope.counterFalse){
				    
					if($scope.selectedReprtHeaderId1.length > 0){
					angular.forEach($scope.selectedReprtHeaderId1,function(value,index){
						if(value.reportGroupHeaderId == demo.reportGroupHeaderId){
							$scope.selectedReprtHeaderId1.splice(index,1);
				            $scope.reportObj[demo.reportGroupHeaderId] = false;
						}
					});
				  }
				}

				if(!flag1){
					
				  $scope.chechHeaderReprt= false;
				    angular.forEach($scope.selectedReprtId1,function(value,index){
				    	if(value.reportGroupHeaderId == demo.reportGroupHeaderId){
				    		$scope.chechHeaderReprt= true;
				    	}
				    	
				    });
				    
				    if(!$scope.chechHeaderReprt){
			    		   $scope.reportObj[demo.reportGroupHeaderId] = true;
			    		 var temp = {
				    		     reportGroupHeaderId:demo.reportGroupHeaderId
				    		};
					$scope.selectedReprtHeaderId1.push(temp);
			    	}
			 
					var temp = {
						reportId:demo.reportId,
						reportGroupHeaderId:demo.reportGroupHeaderId
					};
					
				    $scope.selectedReprtId1.push(temp);
				    
		
				   
				}
				 console.log("$scope.selectedReprtHeaderId1",$scope.selectedReprtHeaderId1);
				 console.log("$scope.list",list);
				 console.log("$scope.selectedReprtId1",$scope.selectedReprtId1);
				 console.log("$scope.reportObjId",$scope.reportObjId);
				}
			
			
			
			
			$scope.saveUserMISReportList = function() {
			$scope.list1=[];
			$scope.list2=[];
			$scope.selectedReprtHeaderId=[];
			$scope.selectedReprtId=[];
				angular.forEach($scope.selectedReprtHeaderId1, function( value1, index1) {
					$scope.list2=[];	
					var newArr={};
						newArr.reportGroupHeaderId = value1.reportGroupHeaderId;
							angular.forEach($scope.selectedReprtId1, function( value2, index2) {
								if(value1.reportGroupHeaderId==value2.reportGroupHeaderId){
									var newArr2={};
									    newArr2.reportId=value2.reportId;
									    $scope.list2.push(newArr2);
								}
							});
							newArr.selectedReprtId =[];
							newArr.selectedReprtId =$scope.list2;
							$scope.selectedReprtHeaderId.push(newArr);

				});
			console.log("save data",$scope.selectedReprtHeaderId);

			var data = {
						userId:$scope.misUserId,
						misReportIds:JSON.stringify($scope.selectedReprtHeaderId),
						organisationId:$scope.organizationId,
						unitId:$scope.unitId
						}
			console.log("datattt",data);
			 $rootScope.startLoaderPopup();
			var URI = BASE_URL+ ROOT_URL+ "/api/global/saveUserWiseMISReportsList";

			GenericService.serviceAction("POST", URI,data).then(function(response) {
				if (response.data.status == 'success')
				{
					growl.success(response.data.message,
							{
								title : 'Success!'
							});
					$scope.masterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
					 $rootScope.stopLoaderPopup();
			}
			});

			}
			
			 
			$scope.checkedRadio =function(idx,val)
			{
				angular.forEach($scope.getHeaderTabList,function(value,index){
					if(idx == index && value.headerId == val){
						  $('#dayRadioMedi_'+index).prop('checked', true);
						  value.defualtTab = 'Y';
						  
					}else{
						$('#dayRadioMedi_'+index).prop('checked', false);
						  value.defualtTab = 'N';
					}
					angular.forEach(value.listSubTabDetails,function(value1,index1){
						$('#RadioMedi_'+value1.subTabId).prop('checked', false);
						 value.defualtSubTab = 'N';
				    });
					
				});
				
				console.log("radio button select",$scope.getHeaderTabList);
				
			}
			
			$scope.checkedRadioSubTab =function(info,val,list)
			{
				angular.forEach($scope.getHeaderTabList,function(value,index){
					if( value.headerId == info.headerId){
						  $('#dayRadioMedi_'+index).prop('checked', true);
						  value.defualtTab = 'Y';
					}else{
						$('#dayRadioMedi_'+index).prop('checked', false);
						 value.defualtTab = 'N';
					}
					
					angular.forEach(value.listSubTabDetails,function(value1,index1){
						$('#RadioMedi_'+value1.subTabId).prop('checked', false);
						 value1.defualtSubTab = 'N';
					});
					
					
				});
				
				angular.forEach(list,function(value,index){
					if(value.subTabId == val){
						  $('#RadioMedi_'+value.subTabId).prop('checked', true);
						  value.defualtSubTab = 'Y';
					}else{
						$('#RadioMedi_'+value.subTabId).prop('checked', false);
						 value.defualtSubTab = 'N';
					}
					
				});
				
				console.log("radio button select",$scope.getHeaderTabList);
				
			}
			
			
			
			
			// encounter coversheet tab
			$scope.tabObj = [];
			$scope.tabObjId = [];
			$scope.selectedHeaderId1=[];
			$scope.selectedSubTabId1=[];
			$scope.selectedHeaderId=[];
			$scope.selectedSubTabId=[];
			
            $scope.getClinicalHeaderList = function(info){
            	
        
            	var promise = $scope.getUserId(info.userId);
				promise.then(function(response) 
						{
					
					var flag = false;
	            	angular.forEach($scope.roleList,function(value,key){
	            		if(value.roleId == $scope.docRoleId || value.roleId == $scope.nurseRoleId){
	            			flag = true;
	            		}
	            	});
	            	if(!flag){
	            		growl.warning("Access only for doctor or nurse",
								{
									title : 'Success!'
								});
						$scope.getHeaderTabList = [];
//						$("#infoModalCover").hide();
//						$state.refresh();
	            		return false
	            	}
     
            	$scope.tabObj = [];
    			$scope.tabObjId = [];
    			$scope.selectedHeaderId1=[];
    			$scope.selectedSubTabId1=[];
    			$scope.selectedHeaderId=[];
    			$scope.selectedSubTabId=[];
				$scope.CliHeaderUserId=info.userId;
				//encounter list
				$scope.savedtabAccessIds = [];
				$scope.savedtabAccessIds = JSON.parse(info.tabAccessIds);
				console.log("save radio button select",$scope.savedtabAccessIds);
				//Admiission list
				$scope.savedtabAccessIdsIPD = [];
				$scope.savedtabAccessIdsIPD = JSON.parse(info.tabAccessIdsIPD);
				
				
				  $scope.setFirstTabLevel(1);
				  
				var URI = BASE_URL + ROOT_URL + "/api/opd/getActiveClinicalHeaderTabList";
				var data = {}
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if(response.data.status == "success"){
					$scope.getHeaderTabList = response.data.listObject;
					$scope.getHeaderTabListIP = response.data.listObject;
					$scope.test = [];
					if($scope.isAdviceAccess == 'N'){
						angular.forEach($scope.getHeaderTabList, function(val){
							if(val.headerName == "Orders"){
								angular.forEach(val.listSubTabDetails, function(d){
									if(d.subTabName != "Advice"){
										$scope.test.push(d);
									}
								});
								val.listSubTabDetails = [];
								val.listSubTabDetails = $scope.test;
							}
						});
					}
					angular.forEach($scope.getHeaderTabList,function(value,index){
						    value.checkValue = false;
						    value.defualtTab = 'N';
							$('#dayRadioMedi_'+index).prop('checked', false);
							angular.forEach(value.listSubTabDetails,function(value1,index1){
								value1.defualtSubTab = 'N';
								value1.checkValue = false;
								$('#RadioMedi_'+value1.subTabId).prop('checked', false);
							});
							
					});
					
					console.log("radio button select",$scope.getHeaderTabList);
					//Bind saved Data	
					angular.forEach($scope.tabObj,function(value,index){
						$scope.tabObj[index] = false;
						var i = $scope.selectedHeaderId1.indexOf(value);
						$scope.selectedHeaderId1.splice(i,1);
						});
					
					 angular.forEach($scope.tabObjId,function(value,index){
						 $scope.tabObjId[index] = false;
						 var i = $scope.selectedSubTabId1.indexOf(value);
						 $scope.selectedSubTabId1.splice(i,1);
						 });
					 
					 angular.forEach($scope.savedtabAccessIds,function(value,index){
						 var temp = {
								       headerId:value.headerId
						 			};
						 $scope.selectedHeaderId1.push(temp);
						 $scope.tabObj[value.headerId] = true;
						 $scope.selectedHeaderId.push(temp);

						 angular.forEach(value.selectedSubTabId,function(value1,index1){
							 var report = {
									 headerId:value.headerId,
									 subTabId:value1.subTabId
							 }
							 var report1 = {
									 subTabId:value1.subTabId
							 }

							 $scope.selectedSubTabId1.push(report);
							 $scope.tabObjId[value1.subTabId] = true;
							 $scope.selectedSubTabId.push(report1);
							 });
						 
					
					 });
					 
						//Bind admission saved Data	
						angular.forEach($scope.tabObjIP,function(value,index){
							$scope.tabObjIP[index] = false;
							var i = $scope.selectedHeaderIdIP1.indexOf(value);
							$scope.selectedHeaderIdIP1.splice(i,1);
							});
						
						 angular.forEach($scope.tabObjIdIP,function(value,index){
							 $scope.tabObjIdIP[index] = false;
							 var i = $scope.selectedSubTabIdIP1.indexOf(value);
							 $scope.selectedSubTabIdIP1.splice(i,1);
							 });
						 
						 angular.forEach($scope.savedtabAccessIdsIPD,function(value,index){
							 var temp = {
									       headerId:value.headerId
							 			};
							 $scope.selectedHeaderIdIP1.push(temp);
							 $scope.tabObjIP[value.headerId] = true;
							 $scope.selectedHeaderIdIP.push(temp);

							 angular.forEach(value.selectedSubTabId,function(value1,index1){
								 var report = {
										 headerId:value.headerId,
										 subTabId:value1.subTabId
								 }
								 var report1 = {
										 subTabId:value1.subTabId
								 }

								 $scope.selectedSubTabIdIP1.push(report);
								 $scope.tabObjIdIP[value1.subTabId] = true;
								 $scope.selectedSubTabIdIP.push(report1);
								 });
							 
						
						 });
						 
						 //check radio button
						 angular.forEach($scope.getHeaderTabList,function(value,index){
							 if($scope.selectedHeaderId1.length > 0){
									 var found = $scope.selectedHeaderId1.find(ar=>ar.headerId == value.headerId);
									   if(found==undefined)
										   {
										   value.checkValue = false;
										   value.defualtTab = 'N';
											 $('#dayRadioMedi_'+index).prop('checked', false);
										   }else{
											   value.checkValue = true;
										   }
							 }else{
								 value.checkValue = false;
								 value.defualtTab = 'N';
								 $('#dayRadioMedi_'+index).prop('checked', false);
								}
							
							angular.forEach(value.listSubTabDetails,function(value1,index1){
								if($scope.selectedSubTabId1.length > 0){
									 var found1 = $scope.selectedSubTabId1.find(ar=>ar.subTabId == value1.subTabId);
									   if(found1==undefined)
										   {
										   value1.checkValue = false;
										   value1.defualtSubTab = 'N';
											 $('#RadioMedi_'+value1.subTabId).prop('checked', false);
										   }else{
											   value1.checkValue = true;
										   }
								}else{
									 value1.checkValue = false;
									 value1.defualtSubTab = 'N';
									 $('#RadioMedi_'+value1.subTabId).prop('checked', false);
								}
							});
					 });
						 
				
						 angular.forEach($scope.getHeaderTabList,function(value,index){
							 angular.forEach($scope.savedtabAccessIds,function(value1,index1){
								 if(value.headerId == value1.headerId && value1.defualtTab == 'Y'){
									 value.defualtTab = 'Y';
									 $('#dayRadioMedi_'+index).prop('checked', true);
									 // FOE SUB TAB 
									 angular.forEach(value.listSubTabDetails,function(value5,index5){
										 
									 var found1 = value1.selectedSubTabId.find(ar=> ar.subTabId == value5.subTabId && ar.defualtSubTab == 'Y' );
									 if(found1==undefined)
									   {
										   
									   }else{
										   value5.defualtSubTab = 'Y';
											 $('#RadioMedi_'+value5.subTabId).prop('checked', true);
									   }
									 });
								 }
							 });
						 });
						 console.log("FINAL LIST button select",$scope.getHeaderTabList);
						 
					 
					}
				});
						});
			}
			

			
			//header select tab
			$scope.selectHeaderTab = function(info,indexx){
			
				var flag = false;
				angular.forEach($scope.selectedHeaderId1,function(value,index){
				 if(value.headerId == info.headerId){
					 $scope.selectedHeaderId1.splice(index,1);
					 $scope.tabObj[value.headerId] = false;
					 angular.forEach(info.listSubTabDetails,function(value1,index1){
						 angular.forEach($scope.selectedSubTabId1,function(value2,index2){ if(value2.headerId == info.headerId){
							 	if(value2.subTabId == value1.subTabId){
							 		 $scope.selectedSubTabId1.splice(index2,1);
								  $scope.tabObjId[value1.subTabId] = false;
							 	}
								
							 }
						 });
	                     });
					 flag = true;
				 }
				})
				if(!flag){
					var temp = {headerId:info.headerId};
					$scope.selectedHeaderId1.push(temp);
					 angular.forEach(info.listSubTabDetails,function(value1,index1){
	                     $scope.tabObjId[value1.subTabId] = true;
	                     var tempaLL = {
	                    		 subTabId:value1.subTabId,
	                             headerId:value1.headerId
	                         };
	                     $scope.selectedSubTabId1.push(tempaLL);
	                     });
					}
				 console.log("$scope.selectedHeaderId1",$scope.selectedHeaderId1);
				 console.log("$scope.selectedSubTabId1",$scope.selectedSubTabId1);
				 
				 //select radio box
				 angular.forEach($scope.getHeaderTabList,function(value,index){
						 if($scope.selectedHeaderId1.length > 0){
								 var found = $scope.selectedHeaderId1.find(ar=>ar.headerId == value.headerId);
								   if(found==undefined)
									   {
									   value.checkValue = false;
									   value.defualtTab = 'N';
										 $('#dayRadioMedi_'+indexx).prop('checked', false);
									   }else{
										   value.checkValue = true;
									   }
						 }else{
							 value.checkValue = false;
							 value.defualtTab = 'N';
							 $('#dayRadioMedi_'+indexx).prop('checked', false);
							}
						
						angular.forEach(value.listSubTabDetails,function(value1,index1){
							if($scope.selectedSubTabId1.length > 0){
								 var found1 = $scope.selectedSubTabId1.find(ar=>ar.subTabId == value1.subTabId);
								   if(found1==undefined)
									   {
									   value1.checkValue = false;
									   value1.defualtSubTab = 'N';
										 $('#RadioMedi_'+value1.subTabId).prop('checked', false);
									   }else{
										   value1.checkValue = true;
									   }
							}else{
								 value1.checkValue = false;
								 value1.defualtSubTab = 'N';
								 $('#RadioMedi_'+value1.subTabId).prop('checked', false);
							}
						});
				 });
					
				}
			
			
			//select sub  tab
			$scope.selectSubReportTab =  function(demo,list){
				var flag1 = false;
				var length = list.length;
				$scope.counterFalse= false;
					angular.forEach($scope.selectedSubTabId1,function(value,index){
						if(value.subTabId == demo.subTabId){
							$scope.selectedSubTabId1.splice(index,1);
							$scope.tabObjId[value.subTabId] = false;
					        flag1 = true;
						}
					});
					
				angular.forEach($scope.selectedSubTabId1,function(value1,index1){
				  if(value1.headerId == demo.headerId){
						 $scope.counterFalse = true;
				  }
				 });
				
			  if(!$scope.counterFalse){
				    
					if($scope.selectedHeaderId1.length > 0){
					angular.forEach($scope.selectedHeaderId1,function(value,index){
						if(value.headerId == demo.headerId){
							$scope.selectedHeaderId1.splice(index,1);
				            $scope.tabObj[demo.headerId] = false;
						}
					});
				  }
				}

				if(!flag1){
					
				  $scope.chechHeaderReprt= false;
				    angular.forEach($scope.selectedSubTabId1,function(value,index){
				    	if(value.headerId == demo.headerId){
				    		$scope.chechHeaderReprt= true;
				    	}
				    	
				    });
				    
				    if(!$scope.chechHeaderReprt){
			    		   $scope.tabObj[demo.headerId] = true;
			    		 var temp = {
			    				 headerId:demo.headerId
				    		};
					$scope.selectedHeaderId1.push(temp);
			    	}
			 
					var temp = {
						subTabId:demo.subTabId,
						headerId:demo.headerId
					};
					
				    $scope.selectedSubTabId1.push(temp);
				    
		
				   
				}
				 console.log("$scope.selectedHeaderId1",$scope.selectedHeaderId1);
				 console.log("$scope.list",list);
				 console.log("$scope.selectedSubTabId1",$scope.selectedSubTabId1);
				 console.log("$scope.tabObjId",$scope.tabObjId);
				 
				 //select radio box
				 angular.forEach($scope.getHeaderTabList,function(value,index){
					 if($scope.selectedHeaderId1.length > 0){
							 var found = $scope.selectedHeaderId1.find(ar=>ar.headerId == value.headerId);
							   if(found==undefined)
								   {
								   value.checkValue = false;
								   value.defualtTab = 'N';
									 $('#dayRadioMedi_'+index).prop('checked', false);
								   }else{
									   value.checkValue = true;
								   }
					 }else{
						 value.checkValue = false;
						 value.defualtTab = 'N';
						 $('#dayRadioMedi_'+index).prop('checked', false);
						}
					
					angular.forEach(value.listSubTabDetails,function(value1,index1){
						if($scope.selectedSubTabId1.length > 0){
							 var found1 = $scope.selectedSubTabId1.find(ar=>ar.subTabId == value1.subTabId);
							   if(found1==undefined)
								   {
								   value1.checkValue = false;
								   value1.defualtSubTab = 'N';
									 $('#RadioMedi_'+value1.subTabId).prop('checked', false);
								   }else{
									   value1.checkValue = true;
								   }
						}else{
							 value1.checkValue = false;
							 value1.defualtSubTab = 'N';
							 $('#RadioMedi_'+value1.subTabId).prop('checked', false);
							 
						}
					});
			 });
				 
		}
			
			
			
			
			$scope.saveEncounterTabList = function() {
				$scope.list1=[];
				$scope.list2=[];
				$scope.selectedHeaderId=[];
				$scope.selectedSubTabId=[];
				console.log("mainList",$scope.getHeaderTabList);
				
					angular.forEach($scope.selectedHeaderId1, function( value1, index1) {
						$scope.list2=[];	
						var newArr={};
							newArr.headerId = value1.headerId;
								angular.forEach($scope.selectedSubTabId1, function( value2, index2) {
									if(value1.headerId==value2.headerId){
										var newArr2={};
										    newArr2.subTabId=value2.subTabId;
										    $scope.list2.push(newArr2);
									}
								});
							newArr.selectedSubTabId =[];
							newArr.selectedSubTabId =$scope.list2;
							$scope.selectedHeaderId.push(newArr);

					});
					
					angular.forEach($scope.selectedHeaderId,function(value1,index1){
						 var found = $scope.getHeaderTabList.find(ar=>ar.headerId == value1.headerId && ar.defualtTab == 'Y');
						   if(found==undefined)
							   {
							   value1.defualtTab ='N'
							   }else{
								   value1.defualtTab ='Y'
							   }
						 
								   angular.forEach(value1.selectedSubTabId,function(value4,index4){
									   angular.forEach($scope.getHeaderTabList,function(value3,index3){
										   if(value1.headerId == value3.headerId){
											   var found3 = value3.listSubTabDetails.find(ar=>ar.headerId == value1.headerId && ar.subTabId ==value4.subTabId && ar.defualtSubTab == 'Y');
											   if(found3 == undefined)
												   {
												   value4.defualtSubTab ='N'
												   }else{
													   value4.defualtSubTab ='Y'
												   }
										   }
								   });
							   
						   });
					});
					
					
				console.log("save data",$scope.selectedReprtHeaderId);
				
//                return false;
                 
				var data = {
							userId:$scope.CliHeaderUserId,
							tabAccessIds:JSON.stringify($scope.selectedHeaderId),
							organisationId:$scope.organizationId,
							unitId:$scope.unitId
							}
				console.log("datattt",data);
//				return false;
				 $rootScope.startLoaderPopup();
				var URI = BASE_URL+ ROOT_URL+ "/api/global/saveUserWiseCovrsheetTabList";

				GenericService.serviceAction("POST", URI,data).then(function(response) {
					if (response.data.status == 'success')
					{
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
						$scope.masterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
						 $rootScope.stopLoaderPopup();
				}
				});

				}
			
			
			
			
			
			// Admission coversheet tab
			$scope.tabObjIP = [];
			$scope.tabObjIdIP = [];
			$scope.selectedHeaderIdIP1=[];
			$scope.selectedSubTabIdIP1=[];
			$scope.selectedHeaderIdIP=[];
			$scope.selectedSubTabIdIP=[];
			


			
			//header select tab
			$scope.selectHeaderTabIP = function(info){
				var flag = false;
				angular.forEach($scope.selectedHeaderIdIP1,function(value,index){
				 if(value.headerId == info.headerId){
					 $scope.selectedHeaderIdIP1.splice(index,1);
					 $scope.tabObjIP[value.headerId] = false;
					 angular.forEach(info.listSubTabDetails,function(value1,index1){
						 angular.forEach($scope.selectedSubTabIdIP1,function(value2,index2){ if(value2.headerId == info.headerId){
							 	if(value2.subTabId == value1.subTabId){
							 		 $scope.selectedSubTabIdIP1.splice(index2,1);
								  $scope.tabObjIdIP[value1.subTabId] = false;
							 	}
								
							 }
						 });
	                     });
					 flag = true;
				 }
				})
				if(!flag){
					var temp = {headerId:info.headerId};
					$scope.selectedHeaderIdIP1.push(temp);
					 angular.forEach(info.listSubTabDetails,function(value1,index1){
	                     $scope.tabObjIdIP[value1.subTabId] = true;
	                     var tempaLL = {
	                    		 subTabId:value1.subTabId,
	                             headerId:value1.headerId
	                         };
	                     $scope.selectedSubTabIdIP1.push(tempaLL);
	                     });
					}
				 console.log("$scope.selectedHeaderIdIP1",$scope.selectedHeaderIdIP1);
				 console.log("$scope.selectedSubTabIdIP1",$scope.selectedSubTabIdIP1);
				}
			
			
			//select sub  tab
			$scope.selectSubReportTabIP =  function(demo,list){
				var flag1 = false;
				var length = list.length;
				$scope.counterFalse= false;
					angular.forEach($scope.selectedSubTabIdIP1,function(value,index){
						if(value.subTabId == demo.subTabId){
							$scope.selectedSubTabIdIP1.splice(index,1);
							$scope.tabObjIdIP[value.subTabId] = false;
					        flag1 = true;
						}
					});
					
				angular.forEach($scope.selectedSubTabIdIP1,function(value1,index1){
				  if(value1.headerId == demo.headerId){
						 $scope.counterFalse = true;
				  }
				 });
				
			  if(!$scope.counterFalse){
				    
					if($scope.selectedHeaderIdIP1.length > 0){
					angular.forEach($scope.selectedHeaderIdIP1,function(value,index){
						if(value.headerId == demo.headerId){
							$scope.selectedHeaderIdIP1.splice(index,1);
				            $scope.tabObjIP[demo.headerId] = false;
						}
					});
				  }
				}

				if(!flag1){
					
				  $scope.chechHeaderReprt= false;
				    angular.forEach($scope.selectedSubTabIdIP1,function(value,index){
				    	if(value.headerId == demo.headerId){
				    		$scope.chechHeaderReprt= true;
				    	}
				    	
				    });
				    
				    if(!$scope.chechHeaderReprt){
			    		   $scope.tabObjIP[demo.headerId] = true;
			    		 var temp = {
			    				 headerId:demo.headerId
				    		};
					$scope.selectedHeaderIdIP1.push(temp);
			    	}
			 
					var temp = {
						subTabId:demo.subTabId,
						headerId:demo.headerId
					};
					
				    $scope.selectedSubTabIdIP1.push(temp);
				    
		
				   
				}
				 console.log("$scope.selectedHeaderIdIP1",$scope.selectedHeaderIdIP1);
				 console.log("$scope.list",list);
				 console.log("$scope.selectedSubTabIdIP1",$scope.selectedSubTabIdIP1);
				 console.log("$scope.tabObjIdIP",$scope.tabObjIdIP);
				}
			
			
			
			
			$scope.saveAdmissionTabList = function() {
				$scope.list1=[];
				$scope.list2=[];
				$scope.selectedHeaderIdIP=[];
				$scope.selectedSubTabId=[];
					angular.forEach($scope.selectedHeaderIdIP1, function( value1, index1) {
						$scope.list2=[];	
						var newArr={};
							newArr.headerId = value1.headerId;
								angular.forEach($scope.selectedSubTabIdIP1, function( value2, index2) {
									if(value1.headerId==value2.headerId){
										var newArr2={};
										    newArr2.subTabId=value2.subTabId;
										    $scope.list2.push(newArr2);
									}
								});
								newArr.selectedSubTabId =[];
								newArr.selectedSubTabId =$scope.list2;
								$scope.selectedHeaderIdIP.push(newArr);

					});
				console.log("save data",$scope.selectedReprtHeaderId);

				var data = {
							userId:$scope.CliHeaderUserId,
							tabAccessIdsIPD:JSON.stringify($scope.selectedHeaderIdIP),
							organisationId:$scope.organizationId,
							unitId:$scope.unitId
							}
				console.log("datattt",data);
//				return false;
				 $rootScope.startLoaderPopup();
				var URI = BASE_URL+ ROOT_URL+ "/api/global/saveUserWiseCovrsheetTabListIPD";

				GenericService.serviceAction("POST", URI,data).then(function(response) {
					if (response.data.status == 'success')
					{
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
						$scope.masterList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage,$scope.unitId);
						 $rootScope.stopLoaderPopup();
				}
				});

				}
			
}]);