angular
		.module('myApp')
		.controller(
				'OTIMSdashboardInfoController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						'$stateParams',
						'$timeout',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService, $stateParams,$timeout) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.updateBtnFlag = false;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							if($stateParams.procedureObj.proReqId == null ){
								$state.go('OTIMSdashboard');
								return;
							}
							$scope.common = $stateParams.procedureObj;
							$scope.selectedTableId ="";
							
						//	$scope.uhidNum = $scope.common.uhid;
						//	$scope.dateOfAdmission =  $scope.common.doa;
						//	$scope.patientDischargeDate = $scope.common.pdd;
						
							$scope.patientDetails.patientName = $scope.common.patientName;
							$scope.patientDetails.genderCode = $scope.common.genderCode;
							$scope.patientDetails.age = $scope.common.age;
							$scope.patientDetails.uhIdNumber = $scope.common.uhid;
							$scope.patientDetails.birthDate = $scope.common.dob;
							$scope.patientDetails.dateOfAdmission = $scope.common.doa;
							$scope.patientDetails.patientDischargeDate = $scope.common.pdd;
							$scope.patientDetails.bedNumber = $scope.common.bedNumber;
							$scope.patientDetails.doctorFullName = $scope.common.doctorName;
							$scope.patientDetails.wardName = $scope.common.wardName;
							$scope.patientDetails.visitTypeId = $scope.common.visitTypeId;
							$scope.patientDetails.isFromOtCompletion = true;
							if($scope.patientDetails.visitTypeId!=1 && $scope.patientDetails.dateOfAdmission!=null && $scope.patientDetails.patientDischargeDate!=null){
								var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
								var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
							} 
							
							var splitDate1 = $scope.common.preferedDateTime.split('-');
							var year1  = splitDate1[0];
							var month1 = splitDate1[1];
							var day1 = splitDate1[2];
							$scope.dStartDate = day1+"/"+month1+"/"+year1;
							$scope.bedNumber =  $scope.common.bedNumber;
							$scope.doctorFullName = $scope.common.doctorName;
						
							$scope.equipcheckList = [];
							$scope.staffcheckList = [];
							$scope.OTTObj = {};
							$scope.oldDate = "";
							$scope.oldTime = "";
							$scope.rescheduleOrNot = 0; // 0 for scheduling  and 1 for Re-Scheduling 
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
									
								};

								$scope.datepickerOpen = {
									opened : false
								};

								$scope.openDatepicker = function() {
									$scope.datepickerOpen.opened = true;
								};
								
	                            // function for initializa clockpicker
	                            $scope.initClockPicker = function() {

	                                $('.clockpicker').clockpicker();
	                            }

	                            $scope.common.changeOfDate = "";
	        					$scope.common.changeOfTime  =  "";
	                            
                                $scope.changeOfDate = moment($scope.common.preferedDateTime).toDate();
                                $scope.changeOfTime  = $scope.common.preferedTime ;

/********************************** OT Tab START **********************************************/							
							
                            $scope.operationTheatreTypeId = 0;    
                            angular.forEach($scope.common.procedureOrderMaster,function(value,index){
                            	if(value.isPrimarySecondary == 'Y'){
                            		$scope.operationTheatreTypeId = value.operationTheatreTypeId;
                            	}
                            });
                            
                            $scope.theatreObj ={};
							$scope.tableObj = {};
							
							//Get Active Operation Theatre List
							$scope.activeOperationTheatreList=[];	
							 $scope.getActiveOperationTheatreList = function() {
								// $scope.startLoader();
								var data = {
										orgId:$scope.organizationId, 
										unitId:$scope.unitId
								};
								$scope.operationTheatreTypeId
								//var URI = BASE_URL + ROOT_URL + GET_ALL_ACTIVE_OPERATION_THEATRE_LIST;
								var URI = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreByTypeId" 
								+ S + $scope.operationTheatreTypeId + S + $scope.organizationId + S + $scope.unitId;
								console.log("URI", URI);
								GenericService.serviceAction(METHOD_GET, URI, "").then(function(response) {
									$scope.activeOperationTheatreList = response.data.listObject;
									console.log("activeOperationTheatreList",$scope.activeOperationTheatreList);
								});
								//$scope.stoptLoader();
							}
							 $scope.getActiveOperationTheatreList();
				
							 
							 //get Tabletheatre list by theatre type Id
							 $scope.theatreList = [];
							 $scope.otList = [];
								$scope.getOperationTheatreByOTTypeId = function(theatreTypeId){
									$scope.startLoader();
									$scope.theatreList = [];
									var URI = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreByTypeId" 
									+ S + theatreTypeId + S + $scope.organizationId + S + $scope.unitId;
									console.log("URI", URI);
									GenericService.serviceAction(METHOD_GET, URI, "").then(function(response) {
										$scope.theatreList = response.data.listObject;
										
										var newArr = {};
										newArr.theatreList = $scope.theatreList;
										newArr.selectedTheatreId = "";
										newArr.theatreTypeName = "";
										newArr.tableList = "";
										newArr.color = "";
										newArr.selectedTableId = "";
										$scope.otList.push(newArr);
										
										$scope.stopLoader();
									});
									
								}
							 
						 
							 $scope.activeOperationTableList = [];
							 $scope.otAddedList = [];
							 $scope.getOperationTableByTheatreId = function(theatreId,flag,index) {
								 $scope.startLoader();
								 $scope.activeOperationTableList = [];
									var data = {
											orgId:$scope.organizationId, 
											unitId:$scope.unitId,
											operationTheatreId : theatreId
									};
									
									var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTableListByTheatreId";
									console.log("URI", URI);
									GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
										if(flag == 0){
										$scope.activeOperationTableList = response.data.listObject;
										console.log("activeOperationTableList",$scope.activeOperationTableList);
										}else{
											
											$scope.otList[index].tableList = response.data.listObject;
										}
										$scope.stopLoader();
									});
								}
							
								
								$scope.addOTDetails = function(){
									$scope.startLoader();
									var isExist = false;
									if($scope.otAddedList.length>0){
										angular.forEach($scope.otAddedList,function(value,index){
											isExist = true;
											/*if(value.color == "" || value.color == "green"){
												isExist = true;
											}*/
										});
									}
									
									if($scope.otList.length>0){
										angular.forEach($scope.otList,function(value,index){
											isExist = true;
											/*if(value.color == "" || value.color == "green"){
												isExist = true;
											}*/
										});
									} 
									
									if(isExist){
										growl.warning("Table Already added in List ",
												{
													title : 'warning!'
												});
										$scope.stopLoader();
										return false;
										
									}else{
										
										if($scope.theatreObj.operationTheatreId == "" || $scope.theatreObj.operationTheatreId == undefined){
											growl.warning("Please Select Opration Theatre ",
													{
														title : 'warning!'
													});
											$scope.stopLoader();
											return false;
										}
										if($scope.tableObj.operationTableId == "" || $scope.tableObj.operationTableId == undefined){
											growl.warning("Please Select Opration Table ",
													{
														title : 'warning!'
													});
											$scope.stopLoader();
											return false;
										}
									var newArr = {};
									newArr.selectedTheatreId = $scope.theatreObj.operationTheatreId;
									newArr.theatreTypeName = $scope.theatreObj.theatreName;
									newArr.tableName = $scope.tableObj.tableName;
									newArr.color = "";
		                            newArr.status="A";
									newArr.selectedTableId = $scope.tableObj.operationTableId;
									$scope.otAddedList.push(newArr);
									
									$scope.checkAvailableOTSlot(newArr,0);
			                        $scope.tableObj = {};
			                        $scope.theatreObj = {};

									}
									$scope.stopLoader();
								};
								
								$scope.removeOTDetails = function (index,flag){
									if(flag==0){
										$scope.otList.splice(index);
										
									}else{
									$scope.otAddedList.splice(index);
									}
								}
								
				/********************************** OT Tab END **********************************************/								
				/**************************************** Staff Tab *****************************************/
								$scope.staffObj = {};
								$scope.roleObj = {};
								$scope.otimsRoleList = [];
								$scope.otimsRole = function() {
									$rootScope.startLoader();
									var data = {
											organisationId:$scope.organizationId,
											unitId:$scope.unitId
											};
									
									var URI = BASE_URL + ROOT_URL + "/api/global/getActiveOTRoleList";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$rootScope.stopLoader();
										$scope.otimsRoleList = response.data.listObject;
										console.log("otimsRoleList",$scope.otimsRoleList);
									});
								}				
								$scope.otimsRole();
								
								$scope.staffList = [];
								$scope.otStaffDetailsList = [];
								$scope.getStaffListByRoleId = function(value,flag){
									$scope.startLoader();
									 $scope.staffList = [];
									 var roleId = "";
									 if(flag == 1){
										 roleId = value.roleId; 
									 }else{
										 roleId = value
									 }
										var data = {
												orgId:$scope.organizationId, 
												unitId:$scope.unitId,
												roleId : roleId
										};
										
										var URI = BASE_URL + ROOT_URL + "/global/otims/getStaffListByRoleId";
										console.log("URI", URI);
										GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
											if(flag==0){
											$scope.staffList = response.data.listObject;
											}else{
												
												var newArr = {};
												newArr.roleId = value.roleId;
												newArr.roleName = value.roleName;
												newArr.emailId = value.emailId;
												newArr.staffList = response.data.listObject;
												newArr.color = "";
												newArr.selectedStaffId = "";
												$scope.otStaffDetailsList.push(newArr);
												
											}
											$scope.stopLoader();
										});
								}
								
								$scope.equipmentAssetAndBatchList = [];
								$scope.getEquipmentTypeList = function () {
									var URI = BASE_URL + ROOT_URL + "/api/otims/OTReservation/getEquipmentTyoeAndAssetItem/"+$scope.organizationId+"/"+$scope.unitId; //2 for Fixed asset type
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.equipmentAssetAndBatchList = response.data.listObject;
										console.log("$scope.equipmentTypeList",$scope.equipmentAssetAndBatchList);
										
									});
								   }
								
								$scope.getEquipmentTypeList();
								
								$scope.otAddedStaffDetailsList = [];
								$scope.addStaffSDetails = function(){
									$scope.startLoader();
									
									$scope.isStaffExist = false;
									
									if($scope.otAddedStaffDetailsList.length>0){
										angular.forEach($scope.otAddedStaffDetailsList,function(value,index){
											if(value.roleId == $scope.roleObj.roleId && value.userId == $scope.staffObj.userId){
												$scope.isStaffExist = true;
											}
										});
									}
									
									if($scope.otStaffDetailsList.length>0){
										angular.forEach($scope.otStaffDetailsList,function(value,index){
											if(value.roleId == $scope.roleObj.roleId && value.selectedStaffId.userId == $scope.staffObj.userId){
												$scope.isStaffExist = true;
											}
										});
									}
									
									
									if($scope.roleObj.roleId=="" || $scope.roleObj.roleId == undefined){
										$rootScope.stopLoader();
										growl.warning("Please select Role",
												{
													title : 'warning!'
												});
									}else if($scope.staffObj.userId=="" || $scope.staffObj.userId == undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Staff Name",
												{
													title : 'warning!'
												});
									}else{
										if($scope.isStaffExist){
											growl.warning("Role And Staff Already Exist",
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											
										}else{
										
												var newArr = {};
												newArr.roleId = $scope.roleObj.roleId;
												newArr.roleName = $scope.roleObj.roleName;
												newArr.userId = $scope.staffObj.userId;
												newArr.emailId =$scope.staffObj.emailId;
												newArr.userName = $scope.staffObj.userName;
												newArr.color="";
												$scope.otAddedStaffDetailsList.push(newArr);
												
												$scope.checkAvailabilityOfStaff(newArr,0)
											
										}
									}
									$scope.staffObj = {};
									$scope.roleObj = {};
									$scope.staffList = [];
									$rootScope.stopLoader();
								}
								
								$scope.removeStaffDetails = function(index,flag){
									if(flag == 0){
										$scope.otAddedStaffDetailsList.splice(index,1);
									}else{
									$scope.otStaffDetailsList.splice(index,1);
									}
								}
								
								
								/**************************staff tab end*********************/
								
								/************************************ equipment Tab START ********************************/
								
								$scope.equipmentTypeObj = {
										id:"",
										category:""
								}
								
								$scope.equipmentNameObj = {
										id:"",
										name:""
								}
								
								$scope.equipmentTypeList = [];
								$scope.getEquipmentTypeList = function () {
									var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId; //2 for Fixed asset type
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.equipmentTypeList = response.data;
										console.log("$scope.equipmentTypeList",$scope.equipmentTypeList);
										
									});
								   }
								
								$scope.getEquipmentTypeList();
								
								$scope.equipmentNameList = [];
								$scope.equipmentBasicNameList = [];
								
								$scope.getEquipmentNamesByEquipmentId = function (obj) {
									$rootScope.startLoader();
									var data = {
											unitId:$scope.unitId,
											assetCategoryId:obj.id
									};
									var URI = BASE_URL + ROOT_URL + "/api/global/items/otims/getItemAndBatchList"; 
									BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.equipmentBasicNameList = response.data;
									
											angular.forEach($scope.equipmentBasicNameList,
													function(value,index) {
														var data = {};
														data.id = value.batchId;
														data.itemName = value.itemName;
														data.equipmentName = value.equipmentName;
														data.itemId = value.itemId;
														$scope.equipmentNameList.push(data);
													});
										$rootScope.stopLoader();	
									});
									
								   }				
								
								
								$scope.equipmentDetailList = [];
								$scope.addEquipmentDetails = function(){
									try{
										$rootScope.startLoader();
								
										$scope.isEquipmentExist = false;
										
										if($scope.equipmentDetailList.length>0){
											angular.forEach($scope.equipmentDetailList,function(value,index){
												if(value.batchId == $scope.equipmentNameObj.id){
													$scope.isEquipmentExist = true;
												}
											});
										}
										
										if($scope.equipmentBatchList.length>0){
										angular.forEach($scope.equipmentBatchList,function(value,index){
											if(value.selectedItemBatchObj!=undefined){
											if(value.selectedItemBatchObj.batchId == $scope.equipmentNameObj.id){
												$scope.isEquipmentExist = true;
											}
											}
										});
										}
										if($scope.equipmentTypeObj.id==""){
											$rootScope.stopLoader();
											growl.warning("Please select Equipment Type",
													{
														title : 'warning!'
													});
										}else if($scope.equipmentNameObj.id==""){
											$rootScope.stopLoader();
											growl.warning("Please Select Equipment Name",
													{
														title : 'warning!'
													});
										}else{
											if($scope.isEquipmentExist){
												growl.warning("Equipment Already Exist",
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
												
											}else{
											
										var newArr = {};
										newArr.equipmentTypeId = $scope.equipmentTypeObj.id;
										newArr.equipmentTypeName = $scope.equipmentTypeObj.category;
										newArr.itemId = $scope.equipmentNameObj.itemId;
										newArr.itemName =$scope.equipmentNameObj.itemName;
										newArr.batchId =$scope.equipmentNameObj.id;
			                            newArr.equipmentName =$scope.equipmentNameObj.equipmentName;
										newArr.color= "";
										$scope.equipmentDetailList.push(newArr);
										
										$scope.equipmentTypeObj.id = "";
										$scope.equipmentTypeObj.category = "";
										$scope.equipmentNameObj.id = "";
										$scope.equipmentNameObj.name = "";
										$scope.equipmentNameList = [];
										$scope.checkAvailabilityOfEquipment(newArr,$scope.equipmentDetailList.length-1,1);
										
											}
										}
								
										$scope.equipmentTypeObj.id = "";
										$scope.equipmentTypeObj.category = "";
										$scope.equipmentNameObj.id = "";
										$scope.equipmentNameObj.name = "";
										$scope.equipmentNameList = [];
										$rootScope.stopLoader();
									}catch(e){
										$rootScope.stopLoader();
										console.log("Exception",e);
									}
								}
								
								$scope.removeEquipment = function(index,flag){
									if(flag == 0){
											
											$scope.equipmentDetailList.splice(index,1);
									}else{
										
											$scope.equipmentBatchList.splice(index,1);
									}
									
								}
								
								/************************************************equipment tab End ******************************/
								 
									
									
									$scope.getStaffDetailsByProcedureId = function(procedureOrderList){
										$scope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/procedure/procedureOrderStaff";
											console.log("URI", URI);
											GenericService.serviceAction(METHOD_POST, URI, procedureOrderList).then(function(response) {
											 if(response.data.listObject.length>0){
												 
												 console.log("response.data.listObject StFF LIST --------------",response.data.listObject);
												angular.forEach( response.data.listObject,function(value,index){
													if(value.roleId!=null){
														$scope.getStaffListByRoleId(value,1);
													}
												}) ;
											 }
												$scope.stopLoader();
											});
									}
									
									$scope.equipmentBatchList = [];
									$scope.itemBatchList = [];
									$scope.getEquipmentDetailsByProcedureId = function(procedureOrderList){
										$scope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/procedure/procedureOrderEquipment";
											console.log("URI", URI);
											GenericService.serviceAction(METHOD_POST, URI, procedureOrderList).then(function(response) {
											 if(response.data.listObject.length>0){
												 
												 console.log("response.data.listObject EQUIPMENT LIST --------------",response.data.listObject);
												angular.forEach( response.data.listObject,function(value,index){
													if(value.equipmentTypeId!=null){
														
														$scope.itemAndBatch = [];
														if($scope.equipmentAssetAndBatchList != null && $scope.equipmentAssetAndBatchList.length > 0){
															
															angular.forEach($scope.equipmentAssetAndBatchList,function(value1,index1){

																if(value.equipmentTypeId == value1.assetCategoryId){
																	
																
																	angular.forEach(value1.assetList,function(value2,index2){
																		
																		if(value.itemId == value2.itemId){
																		
																			$scope.itemAndBatch.push(value2);
																		}

																	});

																					 var newArr = {};
																					newArr.equipmentTypeId = value.equipmentTypeId;
																					newArr.equipmentTypeName = value.equipmentTypeName;
																					newArr.color= "";
                                                                                    newArr.itemName = value.itemName;
																					newArr.itemBatchList = $scope.itemAndBatch;
																					newArr.selectedItemBatchObj = "";
																					$scope.equipmentBatchList.push(newArr);
																					$scope.itemAndBatch = [];
																}

															});
															
														}else{
															
															 var newArr = {};
																newArr.equipmentTypeId = value.equipmentTypeId;
																newArr.equipmentTypeName = value.equipmentTypeName;
																newArr.color= "";
																newArr.itemName = value.itemName;
																newArr.itemBatchList = [];
																newArr.selectedItemBatchObj = "";
																$scope.equipmentBatchList.push(newArr);
														}
														
														
											 }
												$scope.stopLoader();
											});
											 }
											 $scope.stopLoader();
											 });
											console.log("$scope.equipmentBatchList",$scope.equipmentBatchList);
									}
									
									 $timeout(function () {
										 $scope.getEquipmentDetailsByProcedureId($scope.common.procedureOrderMaster);
									 }, 5000);
									$scope.getStaffDetailsByProcedureId($scope.common.procedureOrderMaster);
									//automatic add in OT grid
									angular.forEach($scope.common.procedureOrderMaster,function(value1,index){
										if(value1.isPrimarySecondary=="Y"){
											if(value1.operationTheatreTypeId!=null){
												$scope.getOperationTheatreByOTTypeId(value1.operationTheatreTypeId);
											}
											}
									});
									
							
									$scope.finalSum = "";
									$scope.calTim = function(fromTime ,duration){
										$scope.finalSum = "";
										var num = duration; //$scope.common.totalDuration;
					    				var hours = (num / 60);
					    				var rhours = Math.floor(hours);
					    				var minutes = (hours - rhours) * 60;
					    				var rminutes = Math.round(minutes);
					    				var sOtTime =rhours+":" + rminutes;
					    				console.log("___________",rhours+":" + rminutes);
										
					    				var toTime = fromTime ;//$scope.common.preferedTime;
				        				 var selectedToTimeRes = toTime.split(":");
				        				
				        				 var selectedSwitchTimeRes = sOtTime.split(":");
				        				
				        				 var sumofHrs = parseInt(selectedToTimeRes[0]) + parseInt(selectedSwitchTimeRes[0]);
				        				 var sumOfMin =  parseInt(selectedToTimeRes[1]) + parseInt(selectedSwitchTimeRes[1]);
				        				 $scope.finalSum;
					    				
										 if(sumOfMin>=60){
				        					 
				        					 var newsumofHrs = sumofHrs+":"+"00";
				        					 
				        					 var num = sumOfMin ;
				        						var hours = (num / 60);
				        						var rhours = Math.floor(hours);
				        						var minutes = (hours - rhours) * 60;
				        						var rminutes = Math.round(minutes);
				        						var sOtTime1 =rhours+":" + rminutes;
				        						
				        						 var selectedToTimeRes1 = newsumofHrs.split(":");
				        							
				        						 var selectedSwitchTimeRes1 = sOtTime1.split(":");
				        						
				        						 var sumofHrs1 = parseInt(selectedToTimeRes1[0]) + parseInt(selectedSwitchTimeRes1[0]);
				        						 var sumOfMin1 =  parseInt(selectedToTimeRes1[1]) + parseInt(selectedSwitchTimeRes1[1]);
				        						
				        						 $scope.finalSum = (sumofHrs1<10?"0"+sumofHrs1:sumofHrs1)+":"+(sumOfMin1<10?"0"+sumOfMin1:sumOfMin1);
				        				 }else{
				        					 
				        					 $scope.finalSum = (sumofHrs<10?"0"+sumofHrs:sumofHrs)+":"+(sumOfMin<10?"0"+sumOfMin:sumOfMin);
				        					 
				        				 }
										 
										 console.log("finalSum",$scope.finalSum);
										 $scope.finalSum =  $scope.finalSum +":"+"00";
									}
									
									$scope.calTim($scope.common.preferedTime,$scope.common.totalDuration);			
									
				$scope.checkAvailableOTSlot = function(info,flag){
					$scope.startLoader();
				
					var data = {
							orgId : $scope.organizationId, 
							unitId : $scope.unitId,
							otDate :info.otDate == undefined ? $scope.common.preferedDateTime:info.otDate,
							tableId :info.tableId == undefined ? info.selectedTableId :info.tableId ,
							theatreId : info.theatreId == undefined ?info.selectedTheatreId: info.theatreId,
							fromTime : info.fromTime == undefined ?$scope.common.preferedTime:info.fromTime,
							toTime : info.toTime == undefined ? $scope.finalSum:info.toTime
					};
					$scope.OTTObj = data;
					var URI = BASE_URL + ROOT_URL + "/global/otims/toCheckSlotAvailability";
					console.log("URI", URI);
					GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
						if (response.data.status == "success"){
							if(flag==0){
								info.color="green";
							}else{
								
								angular.forEach($scope.otList,function(value,index){
									if($scope.otList[index].color!=""){
										$scope.otList[index].color="green";
									}
								});
								
								  angular.forEach($scope.otAddedList,function(value,index){
									  if($scope.otAddedList[index].color!=""){
										  $scope.otAddedList[index].color="green";
									  }
	                                });
							}
							}else if(response.data.status =="error"){
								if(flag==0){
									info.color="red";											
								}else{
									angular.forEach($scope.otList,function(value,index){
										if($scope.otList[index].color!=""){
											$scope.otList[index].color="red";
										}
									});
								
								 angular.forEach($scope.otAddedList,function(value,index){
									 if($scope.otAddedList[index].color!=""){
										 $scope.otAddedList[index].color="red";
									 }
	                                });
								}
							}
					$scope.stopLoader();
					});
				} 			
			
				
				/******************** checkAvailabilityOfEquipment *****************/
				$scope.checkAvailabilityOfEquipment = function(equipObj,idx,flag){
						$scope.startLoader();
						var batchId ="";
						var itemId ="";
						if(flag==0){
							batchId = equipObj.selectedItemBatchObj.batchId ;
							itemId = equipObj.selectedItemBatchObj.itemId
						}else{
							batchId = equipObj.batchId ;
							itemId = equipObj.itemId
						}
					var isExist = false;
							if($scope.equipmentDetailList.length>0){
								angular.forEach($scope.equipmentDetailList,function(value,index){
									if(value.batchId == equipObj.batchId && index!=idx){
										
										isExist = true;
									}
								});
							}
							
							if($scope.equipmentBatchList.length>0){
								angular.forEach($scope.equipmentBatchList,function(value,index){
									if(value.selectedItemBatchObj.batchId == batchId  && index!=idx){
										isExist = true;
									}
								});
							}
							
							if(isExist){
								growl.warning("Equipment Already Exist",
										{
											title : 'warning!'
										});
								$rootScope.stopLoader();
								equipObj.color = "";
								equipObj= {};
								return false;
							}else{
							var data = {
								orgId : $scope.organizationId, 
								unitId : $scope.unitId,
								batchId : batchId,
								itemId : itemId,
								equipmentSchedulingDate : moment($scope.common.preferedDateTime).format("YYYY-MM-DD"),
								fromTime : $scope.common.preferedTime,
								toTime : $scope.finalSum
							};
							$scope.equipcheckList.push(data);
							console.log("data",data);
							var URI1 = BASE_URL + ROOT_URL + "/global/otims/equipmentScheduling/checkEquipmentavailableOrNot";
							console.log("URI", URI1);
							GenericService.serviceAction(METHOD_POST, URI1, data).then(function(response) {
								if (response.data.status == "success"){
									equipObj.color=response.data.message;
									}else if(response.data.status =="error"){
										equipObj.color=response.data.message;
										$scope.stopLoader();
									}
								$scope.stopLoader();
							});
							}
				} 			
				
				
				/******************** checkAvailabilityOfEquipment  END *****************/
				
				/******************** checkAvailabilityOfStaff  START *****************/
				$scope.checkAvailabilityOfStaff = function(staffObj,flag,idx){
					$scope.startLoader();
					var userId ="";
					if(flag==0){
						userId = staffObj.userId;
					}else{
						userId = staffObj.selectedStaffId.userId;
						var isExist = false;
						if($scope.otStaffDetailsList.length>0){
							angular.forEach($scope.otStaffDetailsList,function(value,index){
								if(value.roleId == staffObj.roleId && value.selectedStaffId.userId == staffObj.selectedStaffId.userId && index!=idx){
									isExist = true;
								}
							});
						}
						
						if($scope.otAddedStaffDetailsList.length>0){
							angular.forEach($scope.otAddedStaffDetailsList,function(value,index){
								if(value.roleId == staffObj.roleId && value.userId == staffObj.selectedStaffId.userId){
									isExist = true;
								}
							});
						}
						
						if(isExist){
							growl.warning("Staff Already Exist",
									{
										title : 'warning!'
									});
							staffObj.selectedStaffId = {};
							staffObj.color = "";
							$rootScope.stopLoader();
							return false;
						}
					}
							var data = {
								orgId : $scope.organizationId, 
								unitId : $scope.unitId,
								userId : userId,
								roleId : staffObj.roleId,
								staffSchedulingDate : moment($scope.common.preferedDateTime).format("YYYY-MM-DD"),
								fromTime : $scope.common.preferedTime,
								toTime : $scope.finalSum
							};
							$scope.staffcheckList.push(data);
							console.log("data",data);
							var URI = BASE_URL + ROOT_URL + "/global/otims/checkStaffAvailableOrNot";
							console.log("URI", URI);
							GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
								if (response.data.status == "success"){
									staffObj.color="green";
									}else if(response.data.status =="error"){
										staffObj.color="red";
									}
								$scope.stopLoader();
							});
				} 			
			
				/******************** checkAvailabilityOfStaff  END *****************/
				
				$scope.setColor = function(info){
					if(info.color == "red"){
						  return {"background-color": "red"};
					}else if(info.color == "green"){
						  return {"background-color": "#b4da80"};
					}else if(info.color == "gray"){
						  return {"background-color": "gray"};
					}
				}
				
				$scope.procedureOTScheduleList = [];
				$scope.procedureStaffScheduleList = [];
				$scope.procedureEquipmentScheduleList = [];
				$scope.saveProcedureSchedule = function(){
					try{
						var isValid = false;
						var isOtValid = false;
						$scope.procedureOTScheduleList = [];
						$scope.procedureStaffScheduleList = [];
						$scope.procedureEquipmentScheduleList = [];
						$rootScope.startLoader();
						var otcount = 0;
						
						
						if($scope.otList.length>0){
							angular.forEach($scope.otList,function(value,index){
								var newArr = {};
								if(value.color == "red" || value.color == ""){
									isValid = true;
									isOtValid = true;
								}
								if(value.color == "green"){
									newArr.proReqId = $scope.common.proReqId,
									newArr.theatreId = parseInt(value.selectedTheatreId),
									newArr.tableId = parseInt(value.selectedTableId);
									newArr.prodeureDate =  moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss');
									newArr.fromTime = $scope.common.preferedTime;
									newArr.toTime = $scope.finalSum;
									newArr.color = value.color;
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.createdBy = $scope.userId;
									newArr.status = 'A';
									newArr.updatedBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedDate = $scope.dt;
									$scope.procedureOTScheduleList.push(newArr);
								}
								
							});
							
							console.log("$scope.procedureOTScheduleList111111",$scope.procedureOTScheduleList);
						}else{
							otcount = otcount + 1;
						}
						if($scope.otAddedList.length>0){
							angular.forEach($scope.otAddedList,function(value,index){
								var newArr = {};
								if(value.color == "red" || value.color == ""){
									isValid = true;
									isOtValid = true;
								}
								if(value.color == "green"){
									newArr.proReqId = $scope.common.proReqId,
									newArr.theatreId = parseInt(value.selectedTheatreId),
									newArr.tableId = parseInt(value.selectedTableId);
									newArr.prodeureDate =  moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss');
									newArr.fromTime = $scope.common.preferedTime;
									newArr.toTime = $scope.finalSum;
									newArr.color = value.color;
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.createdBy = $scope.userId;
									newArr.status = 'A';
									newArr.updatedBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedDate = $scope.dt;
									$scope.procedureOTScheduleList.push(newArr);
								}
							});
							
							console.log("$scope.procedureOTScheduleList2222222",$scope.procedureOTScheduleList);
						}else{
							otcount = otcount + 1;
						}
						
						
						if(otcount == 2){
							isOtValid = true;
						}
						$scope.unselectedStaff = false;
						if($scope.otAddedStaffDetailsList.length>0){
							angular.forEach($scope.otAddedStaffDetailsList,function(value,index){
								var newArr = {};
								if(value.color == "red"){
									isValid = true;
								}
								if(value.color != ""){
									newArr.proReqId = $scope.common.proReqId,
									newArr.roleId = value.roleId,
									//newArr.doctorId = value.doctorId;
									newArr.userId = value.userId;
									newArr.prodeureDate =  moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss');
									newArr.fromTime = $scope.common.preferedTime;
									newArr.toTime = $scope.finalSum;
									newArr.emailId = value.emailId;
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.color = value.color;
									newArr.createdBy = $scope.userId;
									newArr.status = 'A';
									newArr.updatedBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedDate = $scope.dt;
									$scope.procedureStaffScheduleList.push(newArr);
								
								}else{
									$scope.unselectedStaff = true;
								}
							});
							
							console.log("procedureOTScheduleList 1111",$scope.procedureStaffScheduleList);
						}
						
												
						if($scope.otStaffDetailsList.length>0){
							
							angular.forEach($scope.otStaffDetailsList,function(value,index){
								var newArr = {};
								if(value.color == "red"){
									isValid = true;
								}
								if(value.color != ""){
									newArr.proReqId = $scope.common.proReqId,
									newArr.roleId = value.roleId,
									newArr.userId = value.selectedStaffId.userId;
									newArr.prodeureDate =  moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss');
									newArr.fromTime = $scope.common.preferedTime;
									newArr.toTime = $scope.finalSum;
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.emailId = value.selectedStaffId.emailId;;
									newArr.color = value.color;
									newArr.createdBy = $scope.userId;
									newArr.status = 'A';
									newArr.updatedBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedDate = $scope.dt;
									$scope.procedureStaffScheduleList.push(newArr);
								}else{
									$scope.unselectedStaff = true;
								}
								
							});
							
							console.log("procedureOTScheduleList 222",$scope.procedureOTScheduleList);
						}
						
						if($scope.unselectedStaff == true){
							$rootScope.stopLoader();
							growl.warning("Please select  Staff Name",
									{
										title : 'warning!'
									});
							return false;
							
						}
						
						$scope.unselectedEquip = false;
						
						if($scope.equipmentDetailList.length>0){
							angular.forEach($scope.equipmentDetailList,function(value,index){
								var newArr = {};
								if(value.color == "red"){
									isValid = true;
								}else if(value.color == "gray"){
									isValid = true;
								}
								if(value.color != ""){
									newArr.proReqId = $scope.common.proReqId,
									newArr.assetCatId = value.equipmentTypeId,
									newArr.itemId = value.itemId;
									newArr.batchId = value.batchId;
									newArr.prodeureDate = moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss');
									newArr.fromTime = $scope.common.preferedTime;
									newArr.toTime = $scope.finalSum;
									newArr.color = value.color;
									newArr.status = 'A';
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.createdBy = $scope.userId;
									newArr.updatedBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedDate = $scope.dt;
									$scope.procedureEquipmentScheduleList.push(newArr);
								}else{
									$scope.unselectedEquip = true;
								}
							});
							
							console.log("procedureEquipmentScheduleList 111",$scope.procedureEquipmentScheduleList);
						}
							
						if($scope.equipmentBatchList.length>0){
							angular.forEach($scope.equipmentBatchList,function(value,index){
								var newArr = {};
								if(value.color == "red"){
									isValid = true;
								}else if(value.color == "gray"){
									isValid = true;
								}
								if(value.color != ""){
									newArr.proReqId = $scope.common.proReqId,
									newArr.assetCatId = value.equipmentTypeId,
									newArr.itemId = value.selectedItemBatchObj.itemId;
									newArr.batchId = value.selectedItemBatchObj.batchId;
									newArr.prodeureDate = moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss');
									newArr.fromTime = $scope.common.preferedTime;
									newArr.toTime = $scope.finalSum;
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.createdBy = $scope.userId;
									newArr.status = 'A';
									newArr.color = value.color;
									newArr.updatedBy = $scope.userId;
									newArr.createdDate = $scope.dt;
									newArr.updatedDate = $scope.dt;
									$scope.procedureEquipmentScheduleList.push(newArr);
								}else{
									$scope.unselectedEquip = true;
								}
							});
							
							console.log("procedureEquipmentScheduleList 222",$scope.procedureEquipmentScheduleList);
						}
						
						if($scope.unselectedEquip == true){
							$rootScope.stopLoader();
							growl.warning("Please select  Equipment and batch",
									{
										title : 'warning!'
									});
							return false;
						}
						
						if(isOtValid){
							$rootScope.stopLoader();
							growl.warning("OT Resources not available",
									{
										title : 'warning'
									});
							return false;
						}else{
						if(isValid){
							$rootScope.stopLoader();
							
							$("#resourceNotAvailable").modal("show");
						}else{
							$scope.finalSaveProcedureSchedule();
						}
						}
						
						
					}catch(e){
						console.log("Exception",e);
					}
				}
				$scope.previousDate = "";
				$scope.previousTime = "";
				$scope.clearAllList = function(){
					$rootScope.startLoader();
					
					if($scope.changeOfDate == undefined || $scope.changeOfDate == ""){
						$rootScope.stopLoader();
						growl.warning("Please select Change Of Date ",
								{
									title : 'warning!'
								});
						return false;
					}
					
					if($scope.changeOfTime == undefined || $scope.changeOfTime == ""){
						$rootScope.stopLoader();
						growl.warning("Please select Change Of Time ",
								{
									title : 'warning!'
								});
						return false;
					}
					
					$scope.previousDate = $scope.changeOfDate;
					$scope.previousTime = $scope.changeOfTime;
					var currTime = moment(new Date()).format('HH:MM');   
					var oldprocedureDate = moment($scope.changeOfDate).format('YYYY-MM-DD');
					var todayDates = moment(new Date()).format('YYYY-MM-DD');
				
					if(oldprocedureDate == todayDates && currTime > $scope.changeOfTime){
					
						growl.warning("Please select valid Time", {
							title : 'warning!'});
						$rootScope.stopLoader();
						return false;
					
						}else{
							
							$scope.calTim($scope.changeOfTime, $scope.common.totalDuration);
							
							$scope.common.preferedDateTime =  moment($scope.changeOfDate).format('YYYY-MM-DD');
							//$scope.common.preferedTime  =  $scope.changeOfTime+":00";
							$scope.common.preferedTime  =  $scope.changeOfTime;
							
							angular.forEach($scope.staffcheckList,function(value,index){
								
								value.staffSchedulingDate =moment(moment($scope.changeOfDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
								//value.fromTime = $scope.changeOfTime+":00";
								value.fromTime = $scope.changeOfTime;
								value.toTime = $scope.finalSum;
							});
							
							
							angular.forEach($scope.equipcheckList,function(value,index){
								value.equipmentSchedulingDate =moment(moment($scope.changeOfDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
								//value.fromTime = $scope.changeOfTime+":00";
								value.fromTime = $scope.changeOfTime;
								value.toTime = $scope.finalSum;
							});
							
							var URI = BASE_URL + ROOT_URL + "/global/otims/checkStaffListAvailableOrNot"; 
							BillingGenericService.serviceAction(METHOD_POST,URI,$scope.staffcheckList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								$scope.staffChangeDateList = response.data.listObject;
							
									angular.forEach($scope.staffChangeDateList, function(value,index) {
										$rootScope.startLoader();
											angular.forEach($scope.otStaffDetailsList,function(value1,index){
												if(value.roleId == value1.roleId && value1.selectedStaffId.userId == value.userId){
													value1.color  = value.color;
												}
											});
										
											angular.forEach($scope.otAddedStaffDetailsList,function(value1,index){
												if(value1.roleId == value.roleId && value1.userId == value.userId){
													value1.color  = value.color;
												}
											});
										
									});
								$rootScope.stopLoader();	
							});
							
							var URI = BASE_URL + ROOT_URL + "/global/otims/equipmentScheduling/checkEquipmentListAvailableOrNot"; 
							BillingGenericService.serviceAction(METHOD_POST,URI,$scope.equipcheckList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								$scope.equiChangeDateList = response.data.listObject;
								$rootScope.startLoader();
									angular.forEach($scope.equiChangeDateList, function(value,index) {
										
											angular.forEach($scope.equipmentDetailList,function(value1,index){
												if(value1.batchId == value.batchId && value1.itemId == value.itemId){
													value1.color  = value.color;
												}
											});
											
											angular.forEach($scope.equipmentBatchList,function(value1,index){
												if(value1.selectedItemBatchObj.batchId == value.batchId && value1.selectedItemBatchObj.itemId == value.itemId){
													value1.color  = value.color;
												}
											});
									});
								$rootScope.stopLoader();	
							});
							
							$scope.OTTObj.otDate =moment(moment($scope.changeOfDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
						//	$scope.OTTObj.fromTime = $scope.changeOfTime+":00";
							$scope.OTTObj.fromTime = $scope.changeOfTime;
							$scope.OTTObj.toTime = $scope.finalSum;
							$scope.checkAvailableOTSlot($scope.OTTObj,1);	
							
							$rootScope.stopLoader();
						}
					
	
				}
				
				$scope.goToDashboard = function(){
					$state.go('OTIMSdashboard');
					return;
				}
				
				$scope.finalSaveProcedureSchedule = function(){
					
					$scope.param={
							procedureOTScheduleList :$scope.procedureOTScheduleList,
							procedureStaffScheduleList :$scope.procedureStaffScheduleList,
							procedureEquipmentScheduleList : $scope.procedureEquipmentScheduleList,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							proReqId : $scope.common.proReqId,
							organizationId : $scope.organizationId,
							unitId : $scope.unitId,
							createdBy : $scope.userId,
							status : 'A',
							updatedBy : $scope.userId,
							createdDate : $scope.dt,
							updatedDate : $scope.dt,
							procedureName : $scope.common.procedures,
							otType : $scope.common.OTTypes,
							procedureType : $scope.common.procedureTypes,
							scheduleDate : moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss'),
							preferTime : $scope.common.preferedTime,
							totalDuration : $scope.common.totalDuration,
							department : $scope.common.specialityName,
							departmentId : $scope.common.specialityId,
							proceduerStatusId : 3,
							changeProcedureDate : ($scope.changeOfDate!=null && $scope.changeOfDate!=undefined && $scope.changeOfDate!="") ?moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss') : moment($scope.common.preferedDateTime).format('DD-MM-YYYY HH:mm:ss'),
							changeTime : ($scope.changeOfTime!=null && $scope.changeOfTime!=undefined && $scope.changeOfTime!="") ?$scope.changeOfTime:$scope.common.preferedTime,
							oldDate:$scope.oldDate,
							oldTime:$scope.oldTime,
							rescheduleOrNot:$scope.rescheduleOrNot,
							patientName : $scope.common.patientName,
						};
						console.log("$scope.param",$scope.param);
						$scope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/otims/dashboard/saveProcedureSchedulingDetails";
						console.log("$scope.param",$scope.param);
						GenericService.serviceAction("POST",URI,$scope.param).then(function(response){
							if (response.data.status == 'success')
							{
								$scope.stopLoader();
								$scope.operationTheatreTypeId = 0;  
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								 $timeout(function () { 
									 $scope.goToDashboard();
								 }, 3000);
								
							} else if(response.data.status == 'error')
							{
								$scope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
				}
				
				$scope.firstLevelTab = 1;
				$scope.isSetFirstTabLevel = function(tabNumber){
					return $scope.firstLevelTab==tabNumber;
				}
				
			
				$scope.setFirstTabLevel = function(newTab){
					$scope.firstLevelTab = newTab;
				}
				$scope.setFirstTabLevel(1);
				
				$scope.getDetailsByProReqId = function(proReqId){
					
					$scope.param={
							proReqId : proReqId
						};
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/otims/dashboard/getSchedulingByProReqId";
						console.log("$scope.param",$scope.param);
						GenericService.serviceAction("POST",URI,$scope.param).then(function(response){
							if (response.data.status == 'success')
							{
								 
								if(response.data.object != null){
									$scope.oldDate = response.data.object.oldDate;
									$scope.oldTime = response.data.object.oldTime;
									$scope.rescheduleOrNot = 1; // 0 for scheduling  and 1 for Re-Scheduling 
								}
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								
							}
						});
				}
				if($scope.common.proReqId!=undefined){
					$scope.getDetailsByProReqId($scope.common.proReqId);
					
				}
				
				
				$scope.cancelChangeDate = function(){
					if($scope.previousDate!="" && $scope.previousTime !=""){
						 $scope.changeOfDate = $scope.previousDate;
						  $scope.changeOfTime  = $scope.previousTime ;
					}
				}

				$scope.getDefaultSurgeon = function(){
				    var role = "OT SURGEON";
				    $scope.otStaffDetailsList = $scope.otStaffDetailsList.map(function(info, index) {
                        if (info.roleName.toLowerCase() === role.toLowerCase()) {
                            if(info.staffList !== undefined && info.staffList !== null && info.staffList.length>0){
                                var defaultStaff = info.staffList.find(staff => staff.userId === $scope.common.requestBy);
                                if (defaultStaff) {
                                    info.selectedStaffId = defaultStaff;
                                    $scope.checkAvailabilityOfStaff(info, 1, index);
                                }
                            }
                        }
                        return info;
                    });
				}


				
			}]);
		