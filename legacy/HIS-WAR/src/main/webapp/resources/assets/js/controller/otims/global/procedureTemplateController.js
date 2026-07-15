angular
		.module('myApp')
		.controller(
				'procedureTemplateController',
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
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$stateParams) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							$scope.procedureTemplateObj ={
									procedureConfigId:null,
									specialityId:"",
									cptCode:"",
									service:"",
									procedureTypeId:"",
									cptDescription:"",
									duration:"",
									anesthesiaTypeId:"",
									serviceId:"",
									procedureTypeId:"",
									operationTheatreTypeId:""
								
							}
							
							$scope.common = $stateParams.commonObj;
							$scope.infoObj = $stateParams.infoObj;
							console.log("$scope.common",$scope.common)
							console.log("$scope.infoObj ",$scope.infoObj)
							if($scope.infoObj.isView){
								$scope.addBtn = true;
								
							}else{
								$scope.addBtn = false;
								
							}
							$scope.specialityList = [];
							$scope.procedureTypes = [];
							$scope.anesthesiaTypes = [];
							
							$scope.consentDetailList = [];
							$scope.checklistDetailList = [];
							$scope.staffDetailList = [];
							$scope.selectedItemList = [];
							$scope.equipmentDetailList = [];
							$scope.finalServiceList = [];
							$scope.noteList = [];
							
							/****************************************************************  Patient Education Tab START      *****************************************************************/
							$scope.patientEduObj = {
									patientEducationTempale:"",
									procedureConfigPatientEducationId:null,
									organizationId:$scope.organizationId,
									unitId:$scope.unitId,
									createdBy:$scope.userId,
									updatedBy:$scope.userId,
									status:'A',
									createdDate:$scope.dt,
									updatedDate:$scope.dt
							};
							
							/****************************************************************  Patient Education Tab END      *****************************************************************/

							$scope.patientEducationTempale="";
							
							$scope.getProcedureTemplate = function(procedureConfigId){
								
								var URI = BASE_URL + ROOT_URL + "/api/otims/getProcedureConfigTemplate";
								var obj = {
										orgId:$scope.organizationId, 
										unitId:$scope.unitId,
										procedureConfigId:procedureConfigId
										};
								BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									//	$scope.procedureTemplateObj = response.data.object;
										$scope.consentDetailList = response.data.object.consentDetailList;
									$scope.checklistDetailList = response.data.object.checklistDetailList;
									$scope.staffDetailList = response.data.object.staffDetailList;
									$scope.selectedItemList = response.data.object.inventoryItemList;
									$scope.finalServiceList = response.data.object.serviceList;
									$scope.noteList = response.data.object.noteList;
									$scope.equipmentDetailList = response.data.object.equipmentDetailList;
									$scope.patientEduObj.patientEducationTempale = response.data.object.patientEduObj.patientEducationTempale;
									$scope.patientEduObj.procedureConfigPatientEducationId = response.data.object.patientEduObj.procedureConfigPatientEducationId;
									console.log("$scope.consentDetailListsadvfbghjkl;",$scope.consentDetailList);
								});
							}
							

							if($scope.common.procedureConfigId!=undefined){
								$scope.procedureTemplateObj.specialityId =   $scope.common.specialityId;
								$scope.procedureTemplateObj.cptCode =   $scope.common.cptCode;
								$scope.procedureTemplateObj.service =   $scope.common.service;
								$scope.procedureTemplateObj.procedureTypeId =   $scope.common.procedureTypeId;
								$scope.procedureTemplateObj.cptDescription =   $scope.common.cptDescription;
								$scope.procedureTemplateObj.duration =   $scope.common.duration;
								$scope.procedureTemplateObj.anesthesiaTypeId =   $scope.common.anesthesiaTypeId;
								$scope.procedureTemplateObj.serviceId =   $scope.common.serviceId;
								$scope.procedureTemplateObj.procedureTypeId =   $scope.common.procedureTypeId;
								$scope.procedureTemplateObj.operationTheatreTypeId =   $scope.common.operationTheatreTypeId;
								$scope.procedureTemplateObj.procedureConfigId = $scope.common.procedureConfigId;
								$scope.getProcedureTemplate($scope.common.procedureConfigId);
							}
							
							$scope.getSpecialityList = function(){
								var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
								var obj = {
										organizationId:$scope.organizationId, 
										unitId:$scope.unitId
										};
								BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									angular.forEach(response.data.listObject,function(v,k){
										if(v.isSurgicalCodeNew == 'Y'){
											$scope.specialityList.push(v);
										}
										
									});
									console.log("$scope.specialityList",$scope.specialityList);
								});
							}
							
							$scope.getSpecialityList();
							
							
							$scope.searchProcServices = function(serviceName){
								$rootScope.startLoader();
								if($scope.procedureTemplateObj.specialityId == undefined || $scope.procedureTemplateObj.specialityId == null || $scope.procedureTemplateObj.specialityId == ""){
									$rootScope.stopLoader();
									growl.warning("Please select Speciality",
											{
												title : 'warning!'
											});
									return false;
								}
								var URI = BASE_URL + ROOT_URL + "/api/global/search/services/procedure/"+$scope.organizationId+"/"+$scope.unitId+"/"+serviceName+"/"+$scope.procedureTemplateObj.specialityId;
								return BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										$rootScope.stopLoader();
										return response.data.listObject;
								});
							}
							
							$scope.selectProcService = function(item, model, label){
								$scope.procedureTemplateObj.serviceId = item.serviceId;
								$scope.procedureTemplateObj.cptCode = item.serviceStandardCode;
							}
							
							
							$scope.getProcedureTypes = function(){
								var URI = BASE_URL + ROOT_URL + "/api/procedure/types/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										$scope.procedureTypes = response.data.listObject;
								});
							}
							$scope.getProcedureTypes();
							
							
							$scope.getAnesthesiaTypes = function(){
								var URI = BASE_URL + ROOT_URL + "/api/procedure/anesthesia/types/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										$scope.anesthesiaTypes = response.data.listObject;
								});
							}
							
							$scope.getAnesthesiaTypes();
							
							
							$scope.operationTheatreTypeList = [];
							$scope.getOperationTheatreTypeList = function(){
								var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTheatreTypeList";
								var obj = {
										orgId:$scope.organizationId, 
										unitId:$scope.unitId
										};
								BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										$scope.operationTheatreTypeList = response.data.listObject;
									console.log("$scope.operationTheatreTypeList",$scope.operationTheatreTypeList);
								});
							}
							
							$scope.getOperationTheatreTypeList();
							
							
							
/****************************************************************  consent Tab START  *****************************************************************/
							$scope.consentTypeObj={
								consentTypeMasterId:"",
								consentTypeMasterName:""
							};
							$scope.consentObj = {
									consentMasterId:"",
									consentMasterName:"",
									filePath:"",
									templateType:"",
									template:"",
									documentName:""
							};
							
							$scope.consentTypeList = [];
							$scope.getConsentTypes = function(){
								var URI = BASE_URL + ROOT_URL + "/adt/getActiveConsentTypeList/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										$scope.consentTypeList = response.data.listObject;
								});
							}
							
							$scope.getConsentTypes();
							
							$scope.consentList = [];
							$scope.getconsentListByTypeId = function(obj){
								$scope.consentList = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/adt/getConsentListByConsentTypeId";
								var obj = {
										consentTypeMasterId:obj.consentTypeMasterId,
										unitId : $scope.unitId
										};
								BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$rootScope.stopLoader();
										$scope.consentList = response.data.listObject;
									console.log("$scope.consentList",$scope.consentList);
									}
								});
							}
							
							//$scope.consentDetailList = [];
							$scope.isConsentExist=false;
							$scope.consentAdded=false;
							$scope.addConsentDetails = function(){
								try{
									$rootScope.startLoader();
									$scope.isConsentExist=false;
									$scope.consentAdded=false;
									
									angular.forEach($scope.consentDetailList,function(value,index){
										if($scope.consentObj.consentMasterId == value.consentId && value.status == 'A'){
											$scope.isConsentExist=true;
										}else if($scope.consentObj.consentMasterId == value.consentId && value.status == 'I'){
											value.status='A';
											
											$scope.consentAdded=true;
										}
									});
									
									if($scope.consentTypeObj.consentTypeMasterId==""){
										$rootScope.stopLoader();
										growl.warning("Please select Consent Type",
												{
													title : 'warning!'
												});
									}else if($scope.consentObj.consentMasterId==""){
										$rootScope.stopLoader();
										growl.warning("Please select Consent",
												{
													title : 'warning!'
												});
									}else{
										
										if($scope.isConsentExist){
											growl.warning("Consent Already Exist",
													{
														title : 'warning!'
													});
										
											$rootScope.stopLoader();
										}else{
											if(!$scope.consentAdded){
											var newArr = {};
											newArr.procedureConfigConsentId = null;
										//	newArr.consentTypeMasterId = $scope.consentTypeObj.consentTypeMasterId;
											newArr.consentTypeMasterName = $scope.consentTypeObj.consentTypeMasterName;
											newArr.consentId = $scope.consentObj.consentMasterId;
											newArr.consentMasterName = $scope.consentObj.consentMasterName;
											newArr.organizationId=$scope.organizationId,
											newArr.unitId=$scope.unitId,
											newArr.status="A",
											newArr.createdBy=$scope.userId,
											newArr.templateType=$scope.consentObj.templateType,
											newArr.documentName=$scope.consentObj.documentName,
											newArr.template=$scope.consentObj.template,
											newArr.filePath=$scope.consentObj.filePath,
											newArr.createdDate=$scope.dt,
											newArr.updatedBy=$scope.userId,
											newArr.updatedDate=$scope.dt,
											$scope.consentDetailList.push(newArr);
											
											}
											
										}
									}
									$scope.consentTypeObj.consentTypeMasterId = "";
									$scope.consentTypeObj.consentTypeMasterName = "";
									$scope.consentObj.consentMasterId = "";
									$scope.consentObj.consentMasterName = "";
									$scope.consentList = [];
									$rootScope.stopLoader();
								}catch(e){
									$rootScope.stopLoader();
									console.log("Exception",e);
								}
							}
							
							$scope.removeConsent = function(idx){
								console.log("$scope.consentDetailListbefore",$scope.consentDetailList);
								if($scope.consentDetailList[idx].procedureConfigConsentId!=null){
									$scope.consentDetailList[idx].status="I";
								}else{
									$scope.consentDetailList.splice(idx,1);
								}
								console.log("$scope.consentDetailListAfter",$scope.consentDetailList);
							}
							
							$scope.currentIndex="";
							$scope.getCurrentIndex = function(index){
								$scope.currentIndex=index;
							}
/****************************************************************  consent Tab END  *****************************************************************/
							
							
							
							
/****************************************************************  checklist Tab START  *****************************************************************/
							
							$scope.checklistObj ={
									description:"",
									checklistMasterId:"",
									roleId:"",
									roleName:""
								}	
							
							$scope.stageObj ={
									stageId:"",
									stageName:""
								}	
								
								$scope.checklistNameList = [];
								$scope.getcheckListByTypeId = function(stageId){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/otims/getChecklistMasterByStageId";
									var obj = {
											stageId:stageId,
											organizationId:$scope.organizationId, 
											unitId:$scope.unitId
											};
									BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.checklistNameList = response.data.listObject;
										console.log("$scope.checklistNameList",$scope.checklistNameList);
										}
									});
								}
							
								//$scope.checklistDetailList = [];
								$scope.isChecklistExist=false;
								$scope.checklistAdded=false;
								$scope.addChecklistDetails = function(){
									try{
										$rootScope.startLoader();
										
										$scope.isChecklistExist=false;
										$scope.checklistAdded=false;
										angular.forEach($scope.checklistDetailList,function(value,index){
											if($scope.checklistObj.checklistMasterId == value.checklistId && value.status == 'A'){
												$scope.isChecklistExist=true;
											}else if($scope.checklistObj.checklistMasterId == value.checklistId && value.status == 'I'){
												value.status='A';
											
												$scope.checklistAdded=true;
											}
										});
										
										if($scope.stageObj.stageId==""){
											$rootScope.stopLoader();
											growl.warning("Please select Stage",
													{
														title : 'warning!'
													});
										}else if($scope.checklistObj.checklistMasterId==""){
											$rootScope.stopLoader();
											growl.warning("Please select Checklist",
													{
														title : 'warning!'
													});
										}else{
											
											if($scope.isChecklistExist){
												growl.warning("Checklist Already Exist",
														{
															title : 'warning!'
														});
												
												$rootScope.stopLoader();
											}else{
										if(!$scope.checklistAdded){
										var newArr = {};
										newArr.procedureConfigChecklistId = null;
										newArr.checklistId = $scope.checklistObj.checklistMasterId;
										newArr.description = $scope.checklistObj.description;
										newArr.stageId = $scope.stageObj.stageId;
										newArr.roleName = $scope.checklistObj.roleName;
										//newArr.roleId = $scope.checklistObj.roleId;
										newArr.organizationId=$scope.organizationId;
										newArr.unitId=$scope.unitId;
										newArr.createdBy=$scope.userId;
										newArr.createdDate=$scope.dt;
										newArr.updatedBy=$scope.userId;
										newArr.status="A",
										newArr.updatedDate=$scope.dt;
										$scope.checklistDetailList.push(newArr);
										
										console.log("$scope.checklistDetailList",$scope.checklistDetailList);
										}
									
											}
										}
										$scope.checklistObj.checklistMasterId = "";
										$scope.checklistObj.description = "";
										$scope.checklistObj.roleName = "";
										$scope.checklistObj.roleId = "";
										$scope.stageObj.stageId = "";
										$scope.checklistNameList = [];
										$rootScope.stopLoader();
									}catch(e){
										$rootScope.stopLoader();
										console.log("Exception",e);
									}
								}
								
								$scope.checklistItemList = [];
								$scope.getChecklistNameListById = function(info){
									try{
										$rootScope.startLoader();
										$scope.checklistItemList = [];
										var URI = BASE_URL + ROOT_URL + "/global/otims/getChecklistMasterDetailsByMasterId";
										var obj = {
												checklistMasterId:info.checklistId,
												organizationId:$scope.organizationId, 
												unitId:$scope.unitId
												};
										BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success"){
												$rootScope.stopLoader();
												$scope.checklistItemList = response.data.listObject;
											console.log("$scope.checklistItemList",$scope.checklistItemList);
											}
										});
									}catch(e){
										$rootScope.stopLoader();
										console.log("Exception",e);
									}
								}
								
								$scope.removeChecklist = function(idx){
									if($scope.checklistDetailList[idx].procedureConfigChecklistId!=null){
										$scope.checklistDetailList[idx].status="I";
										}else{
										$scope.checklistDetailList.splice(idx,1);
									}
								}
/****************************************************************  checklist Tab END  *****************************************************************/
					
								
/****************************************************************  staff Tab END  *****************************************************************/
								$scope.staffObj ={
										roleId:"",
										roleName:""
								};
								$scope.commonStaffObj = {
										staffNumber:"",
								}
								$scope.otimsRoleList = [];
								$scope.otimsRole = function() {
									$rootScope.startLoader();
									var data = {
											organisationId:$scope.organizationId,
											unitId:$scope.unitId
											};
									
									var URI = BASE_URL + ROOT_URL + "/api/global//getActiveOTRoleList";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$rootScope.stopLoader();
										$scope.otimsRoleList = response.data.listObject;
										console.log("otimsRoleList",$scope.otimsRoleList);
									});
								}				
								$scope.otimsRole();
								
							//	$scope.staffDetailList = [];
								$scope.isStaffExist=false;
								$scope.staffAdded=false;
								$scope.addStaffDetails = function(){
									try{
										$rootScope.startLoader();
								
										$scope.isStaffExist=false;
										$scope.staffAdded=false;
										angular.forEach($scope.staffDetailList,function(value,index){
											if($scope.staffObj.roleId == value.roleId && value.status == 'A'){
												$scope.isStaffExist=true;
											}else if($scope.staffObj.roleId == value.roleId && value.status == 'I'){
												value.status='A';
												$scope.staffAdded=true;
											}
										});
										
										if($scope.staffObj.roleId==""){
											$rootScope.stopLoader();
											growl.warning("Please select Role",
													{
														title : 'warning!'
													});
										}else if($scope.commonStaffObj.staffNumber==""){
											$rootScope.stopLoader();
											growl.warning("Please Enter Number",
													{
														title : 'warning!'
													});
										}else{
											if($scope.isStaffExist){
												growl.warning("Staff Already Exist",
														{
															title : 'warning!'
														});
												
												$rootScope.stopLoader();
											}else{
											if(!$scope.staffAdded){
										var newArr = {};
										newArr.procedureConfigStaffId = null;
										newArr.roleId = $scope.staffObj.roleId;
										newArr.roleName = $scope.staffObj.roleName;
										newArr.staffNumber = $scope.commonStaffObj.staffNumber;
										newArr.organizationId=$scope.organizationId;
										newArr.unitId=$scope.unitId;
										newArr.createdBy=$scope.userId;
										newArr.createdDate=$scope.dt;
										newArr.updatedBy=$scope.userId;
										newArr.updatedDate=$scope.dt;
										newArr.status="A",
										$scope.staffDetailList.push(newArr);
										
											}
										
											}
										}
										$scope.staffObj.roleId = "";
										$scope.staffObj.roleName = "";
										$scope.commonStaffObj.staffNumber = "";
										$rootScope.stopLoader();
									}catch(e){
										$rootScope.stopLoader();
										console.log("Exception",e);
									}
								}
								
								$scope.removeStaff = function(idx){
									
									if($scope.staffDetailList[idx].procedureConfigStaffId!=null){
										$scope.staffDetailList[idx].status="I";
										}else{
										$scope.staffDetailList.splice(idx,1);
									}
								}
								
/****************************************************************  staff Tab END  *****************************************************************/

						
						
						
/****************************************************************  Inventory Tab START  *****************************************************************/
								$scope.selectAllRows = false;
								$scope.assetCategoryObj = {
										id:"",
										category:""
								}
								
								$scope.productCategoryObj = {
										id:"",
										category:""
								}
								
								$scope.assetCategories = [];
								$scope.getAssetCategoryList = function () {
									var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/1/"+$scope.unitId+"/"+$scope.organizationId; //1 for consumable asset type
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.assetCategories = response.data;
										console.log("$scope.assetCategories",$scope.assetCategories);
										
									});
								   }
								
								$scope.getAssetCategoryList();
								
								
								$scope.productCategories = {};
								$scope.getProductCategoryList = function (id) {
									var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+id+"/"+$scope.organizationId;
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.productCategories = response.data;
										console.log("$scope.productCategories",$scope.productCategories);
										
									});
								}
								
								$scope.itemObj = {
										itemCode:"",
										itemName:""
								}
								$scope.searchNewItemList = [];
								$scope.searchNewItem = function () {
									var data = {
											 	"assetTypeId": 1,
										        "assetCategoryId": $scope.assetCategoryObj.id,
										        "productCategoryId": $scope.productCategoryObj.id,
										        "itemCode": $scope.itemObj.itemCode,
										        "itemName": $scope.itemObj.itemName
											};
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/global/items/getItemList/onTemplet";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										$rootScope.stopLoaderPopup();
										$scope.searchNewItemList = response.data;
										console.log("$scope.searchNewItemList",$scope.searchNewItemList);
									});
							}
								
								$scope.selectAllItems = function(){
									
									if($scope.searchNewItemList.length > 0 ){
										
										angular.forEach($scope.searchNewItemList,function(value,index) {
											if($scope.selectAllRows){
												value.isSelected = true;
											}else{
												value.isSelected = false;
											}
										});
									}else{
										growl.warning("Please search first then select item",
												{
													title : 'warning!'
												});
										$scope.selectAllRows = false;
									}
								}
								
								$scope.isItemChecked=true;
							//	$scope.selectedItemList = [];
								$scope.isItemExist=false;
								$scope.itemAdded = false;
								$scope.addItems = function(){
								//	$scope.selectedItemList = [];
									$scope.isItemExist=false;
									$scope.isItemCheck=false;
									$scope.itemAdded = false;
									angular.forEach($scope.searchNewItemList,function(value,index) {
										if(value.isSelected){
											
											if($scope.selectedItemList.length>0){
												angular.forEach($scope.selectedItemList,function(value1,index1) {
													if(value1.itemId == value.itemId && value1.status == 'A'){
														$scope.isItemExist = true;
													}else if(value1.itemId == value.itemId && value1.status == 'I'){
														value1.status = 'A';
														$scope.itemAdded = true;
													}
												});
												if($scope.isItemExist){
													$scope.isItemCheck=true;
												}
												else{
													if(!$scope.itemAdded){
													var newArr = {};
													newArr.procedureConfigInventoryItemId = null;
												//	newArr.assetCategory = value.assetCategory;
													newArr.itemName = value.itemName;
													newArr.itemCode = value.itemCode;
													newArr.itemId = value.itemId;
													newArr.qty = 1;
													newArr.organizationId=$scope.organizationId;
													newArr.unitId=$scope.unitId;
													newArr.createdBy=$scope.userId;
													newArr.createdDate=$scope.dt;
													newArr.updatedBy=$scope.userId;
													newArr.updatedDate=$scope.dt;
													newArr.status="A";
													$scope.selectedItemList.push(newArr);
													console.log("$scope.selectedItemList",$scope.selectedItemList);
													
												}
												}
											}else{
												if(!$scope.itemAdded){
												var newArr = {};
												newArr.procedureConfigInventoryItemId = null;
											//	newArr.assetCategory = value.assetCategory;
												newArr.itemName = value.itemName;
												newArr.itemCode = value.itemCode;
												newArr.itemId = value.itemId;
												newArr.qty = 1;
												newArr.organizationId=$scope.organizationId;
												newArr.unitId=$scope.unitId;
												newArr.createdBy=$scope.userId;
												newArr.createdDate=$scope.dt;
												newArr.updatedBy=$scope.userId;
												newArr.updatedDate=$scope.dt;
												newArr.status="A";
												$scope.selectedItemList.push(newArr);
												console.log("$scope.selectedItemList",$scope.selectedItemList);
												
												}
											}
										}
									});
									
									
									if($scope.isItemCheck){
										growl.warning("Selected Item Already Exist",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										
										}
									$scope.searchNewItemList = [];
									$scope.assetCategoryObj="";
									$scope.productCategoryObj= "";
									$rootScope.stopLoader();
								}
								
								$scope.removeItems = function(indx){
									
									if($scope.selectedItemList[indx].procedureConfigInventoryItemId!=null){
										$scope.selectedItemList[indx].status="I";
										}else{
										$scope.selectedItemList.splice(indx,1);
									}
								}
								
/****************************************************************  Inventory Tab END  *****************************************************************/
						
								
/****************************************************************  Equipment Tab START  *****************************************************************/

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
								$scope.getEquipmentNamesByEquipmentId = function (equipmentTypeId) {
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/unit/fixed/asset/getAssetNameListByAssetCategoryId/"+equipmentTypeId; 
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.equipmentNameList = response.data;
										console.log("$scope.equipmentNameList",$scope.equipmentNameList);
										$rootScope.stopLoader();	
									});
								   }
								
								$scope.isEquipmentExist = false;
								$scope.equipmentAdded = false;
								$scope.addEquipmentDetails = function(){
									try{
										$rootScope.startLoader();
								
										$scope.isEquipmentExist = false;
										$scope.equipmentAdded = false;
										
										angular.forEach($scope.equipmentDetailList,function(value,index){
											if(value.itemId == $scope.equipmentNameObj.id && value.status == 'A'){
												$scope.isEquipmentExist = true;
											}else if(value.itemId == $scope.equipmentNameObj.id && value.status == 'I'){
												value.status = 'A'
												$scope.equipmentAdded = true;
											}
										});
										
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
												
											}else{
												if(!$scope.equipmentAdded){
										var newArr = {};
										newArr.procedureConfigEquipmentId =null;
										newArr.equipmentTypeId = $scope.equipmentTypeObj.id;
										newArr.equipmentTypeName = $scope.equipmentTypeObj.category;
										newArr.itemId = $scope.equipmentNameObj.id;
										newArr.itemName =$scope.equipmentNameObj.name;
										newArr.organizationId=$scope.organizationId;
										newArr.unitId=$scope.unitId;
										newArr.createdBy=$scope.userId;
										newArr.createdDate=$scope.dt;
										newArr.updatedBy=$scope.userId;
										newArr.updatedDate=$scope.dt;
										newArr.qty = 1;
										newArr.status="A",
										$scope.equipmentDetailList.push(newArr);
										}
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
								
								$scope.removeEquipment = function(index){
									if($scope.equipmentDetailList[index].equipmentTypeId!=null){
										$scope.equipmentDetailList[index].status="I";
										}else{
										$scope.equipmentDetailList.splice(index,1);
									}
								}
/****************************************************************  Equipment Tab END  *****************************************************************/
	

/****************************************************************  Services Tab START  *****************************************************************/

							$scope.search="";
							$scope.searchServices = function(searchKeyword){
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
							$scope.addServicesList = [];
							$scope.addServiceToTempServiceList = function($item, $model, $label){
								  
								var flag = false;
								angular.forEach($scope.addServicesList,function(value,key){
									
									if(value.serviceId == $item.serviceMasterId){
										flag = true;
									}
									
								});
								
								if(flag == false){
									console.log($item);
									
									var newdata ={}
									newdata.serviceId = $item.serviceMasterId;
									newdata.serviceStandardCode = $item.serviceStandardCode;
									newdata.serviceStandardName = $item.serviceStandardName;
									newdata.specialityId = $item.specialityId;
									newdata.specialityName = $item.specialityName;
									newdata.subSpecialityId = $item.subSpecialityId;
									newdata.subSpecialityName = $item.subSpecialityName;
									newdata.status = "";
									$scope.addServicesList.push(newdata);
								}else{
									growl.warning("This service is  already added ",
											{
												title : 'warning!'
											});
									
								}
							 }
							
							//$scope.finalServiceList=[];
							
						/*	$scope.addSrvices = function(){
							
								angular.forEach($scope.addServicesList,function(value,index){
									if(value.status){
									var newdata ={}
									newdata.procedureConfigServiceId =null;
									newdata.serviceId = value.serviceId;
									newdata.organizationId=$scope.organizationId;
									newdata.unitId=$scope.unitId;
									newdata.createdBy=$scope.userId;
									newdata.updatedBy=$scope.userId;
									newdata.status='A';
									newdata.createdDate=$scope.dt;
									newdata.updatedDate=$scope.dt;
									newdata.serviceStandardCode = value.serviceStandardCode;
									newdata.serviceStandardName = value.serviceStandardName;
									newdata.specialityName = value.specialityName;
									newdata.subSpecialityName = value.subSpecialityName;
									$scope.finalServiceList.push(newdata);
								
									}
								});
								$scope.addServicesList = [];
								console.log("finalServiceList",$scope.finalServiceList);
							}*/
							$scope.serviceFlag = false;
							$scope.serviceAddedFlag  = false;
							$scope.addSrvices = function(){
								$scope.serviceFlag = false;
								$scope.serviceAddedFlag  = false;
								angular.forEach($scope.addServicesList,function(value,index){
									var newdata ={}
									newdata.procedureConfigServiceId =null;
									newdata.serviceId = value.serviceId;
									newdata.organizationId=$scope.organizationId;
									newdata.unitId=$scope.unitId;
									newdata.createdBy=$scope.userId;
									newdata.updatedBy=$scope.userId;
									newdata.status='A';
									newdata.createdDate=$scope.dt;
									newdata.updatedDate=$scope.dt;
									newdata.serviceStandardCode = value.serviceStandardCode;
									newdata.serviceStandardName = value.serviceStandardName;
									newdata.specialityName = value.specialityName;
									newdata.subSpecialityName = value.subSpecialityName;
									$scope.serviceFlag = false;
									angular.forEach($scope.finalServiceList,function(value1,index1){
										
										if(value1.serviceId == value.serviceId && value1.status == 'A'){
											$scope.serviceFlag  = true;
										}else if(value1.serviceId == value.serviceId && value1.status == 'I'){
											value1.status = 'A';
											$scope.serviceAddedFlag  = true;
										}
									});
									if(!$scope.serviceAddedFlag){
									if($scope.serviceFlag == false){
										$scope.finalServiceList.push(newdata);
									}
									}
								});
								
								if($scope.serviceFlag){
									growl.warning("This service is  already added ",
											{
												title : 'warning!'
											});
								}
								$scope.addServicesList = [];
							}
							
							$scope.removeTempAddedServices = function(index){
								
								$scope.addServicesList.splice(index,1);
							}
							
							$scope.removeService = function(index){
								
								if($scope.finalServiceList[index].procedureConfigServiceId!=null){
									$scope.finalServiceList[index].status="I";
									}else{
									$scope.finalServiceList.splice(index,1);
								}
							}
/****************************************************************  Services Tab END  *****************************************************************/
/****************************************************************  Note Tab Start    *****************************************************************/
							
							$scope.ckValue = "";
							$scope.noteTypeList = [];
							$scope.ckObj = {};
							
							$scope.getNoteType = function(id){
								$scope.noteTypeList = [];
								$rootScope.startLoader();
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										noteType: id
										};
								
								var URI = BASE_URL + ROOT_URL + "/global/otims/getNotesMasterListByNoteTypeId";
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									$rootScope.stopLoader();
									angular.forEach(response.data.listObject , function(v,k){
										if(v.templateFormatId != 3){
											$scope.noteTypeList.push(v);
										}
									});
									
									console.log("noteTypeList",$scope.noteTypeList);
									$rootScope.stopLoader();
								});
								
							}	
							
							$scope.selectNote = function(){
								
								$scope.ckValue = $scope.ckObj.template;
							}
							
							//$scope.noteList = [];
							$scope.isNoteExist = false;
							$scope.noteAdded = false;
							$scope.addNote = function(){
								$scope.noteAdded = false;
								if($scope.ckObj.noteMasterId == undefined || $scope.ckObj.noteMasterId == ""){
									
									growl.warning("Please select note",
											{
												title : 'warning!'
											});
									
								}else{
									
									$scope.ckflag = false;
									angular.forEach($scope.noteList,function(value,key){
										
										if(value.noteMasterId == $scope.ckObj.noteMasterId && value.status == 'A'){
											$scope.ckflag = true;
										}else if(value.noteMasterId == $scope.ckObj.noteMasterId && value.status == 'I'){
											value.status = 'A'
											$scope.noteAdded = true;
										}
										
									});
									if(!$scope.noteAdded){
									if($scope.ckflag == false){
									var newdata ={}
										newdata.procedureConfigNoteId =null;			
										newdata.description =$scope.ckObj.description;
										newdata.template = $scope.ckValue;
										newdata.noteMasterId = $scope.ckObj.noteMasterId;
										newdata.noteType = $scope.ckObj.noteType;
										newdata.organizationId=$scope.organizationId;
										newdata.unitId=$scope.unitId;
										newdata.createdBy=$scope.userId;
										newdata.updatedBy=$scope.userId;
										newdata.status='A';
										newdata.createdDate=$scope.dt;
										newdata.updatedDate=$scope.dt;
										$scope.noteList.push(newdata);
									}
									else{
										growl.warning("This note is already added ",
												{
													title : 'warning!'
												});
										
									}
									}
									$scope.ckObj = {};
									$scope.ckValue ="";
									console.log( "$scope.noteList",$scope.noteList);
								}
							}
							
							$scope.ckValueShow = "";
							$scope.editObj = {};
							$scope.indx="";
							$scope.showNote = function(info,indx){
								
								$scope.ckValueShow = info.template;
								$scope.editObj = info;
								
								$scope.indx = indx;
							}
							
							$scope.addeditsNote = function (){
								
								console.log( $scope.noteList);
								$scope.noteList[$scope.indx].template = $scope.ckValueShow;
								
								$scope.ckValueShow = "";
								$scope.editObj = {};
							}
							
							$scope.removeNote = function(idex){
								
								if($scope.noteList[idex].procedureConfigNoteId!=null){
									$scope.noteList[idex].status="I";
									}else{
									$scope.noteList.splice(idex,1);
								}
								
							}
							
/****************************************************************  Note Tab END      *****************************************************************/
													
							$scope.saveProcedureTemplate = function (){
								try{
									
									if($scope.procedureTemplateObj.specialityId == undefined ||$scope.procedureTemplateObj.specialityId == ""){
										growl.warning("Please select speciality",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.procedureTemplateObj.serviceId == undefined ||$scope.procedureTemplateObj.serviceId == ""){
											growl.warning("Please select procedure",
													{
														title : 'warning!'
													});
											$scope. procedureTemplateObj.service = ""
											return false;
									}else if($scope.procedureTemplateObj.procedureTypeId == undefined ||$scope.procedureTemplateObj.procedureTypeId == ""){
										growl.warning("Please select procedure type",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.procedureTemplateObj.cptCode == undefined ||$scope.procedureTemplateObj.cptCode == ""){
										growl.warning("Please enter procedure CPT code",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.procedureTemplateObj.cptDescription == undefined ||$scope.procedureTemplateObj.cptDescription == ""){
										growl.warning("Please enter procedure CPT description",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.procedureTemplateObj.duration == undefined ||$scope.procedureTemplateObj.duration == ""){
										growl.warning("Please enter procedure duration",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.procedureTemplateObj.operationTheatreTypeId == undefined ||$scope.procedureTemplateObj.operationTheatreTypeId == ""){
										growl.warning("Please select operation theatre type ",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.procedureTemplateObj.anesthesiaTypeId == undefined ||$scope.procedureTemplateObj.anesthesiaTypeId == ""){
										growl.warning("Please select anesthesia type",
												{
													title : 'warning!'
												});
										return false;
										
									}else {
										

										$scope.patientEduObj.patientEducationTempale = $scope.patientEducationTempale;
										$rootScope.startLoader();
										$scope.param={
											procedureConfigId:$scope.procedureTemplateObj.procedureConfigId,
											cptCode:$scope.procedureTemplateObj.cptCode,
											cptDescription:$scope.procedureTemplateObj.cptDescription,
											duration:$scope.procedureTemplateObj.duration,
											specialityId:$scope.procedureTemplateObj.specialityId,
											serviceId:$scope.procedureTemplateObj.serviceId,
											procedureTypeId:$scope.procedureTemplateObj.procedureTypeId,
											anesthesiaTypeId:$scope.procedureTemplateObj.anesthesiaTypeId,
											operationTheatreTypeId:$scope.procedureTemplateObj.operationTheatreTypeId,
											status:"A",
											orgId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											createdDate:$scope.dt,
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											consentDetailList:$scope.consentDetailList,
											checklistDetailList:$scope.checklistDetailList,
											staffDetailList:$scope.staffDetailList,
											inventoryItemList:$scope.selectedItemList,
											equipmentDetailList:$scope.equipmentDetailList,
											serviceList:$scope.finalServiceList,
											noteList:$scope.noteList,
											patientEduObj:$scope.patientEduObj
										};
										console.log("$scope.param",$scope.param);
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/otims/saveProcedureConfigTemplate";
										console.log("$scope.param",$scope.param);
										GenericService.serviceAction("POST",URI,$scope.param).then(function(response){
											if (response.data.status == 'success')
											{
												//$scope.getNotesMasterList();
												$scope.procedureTemplateObj ={};
												$scope.consentDetailList = [];
												$scope.checklistDetailList = [];
												$scope.staffDetailList = [];
												$scope.selectedItemList = [];
												$scope.equipmentDetailList = [];
												$scope.finalServiceList = [];
												$scope.noteList = [];
												$scope.patientEducationTempale = "";
												$rootScope.stopLoader();
												
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$state.go('procedureConfigurations');
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
									console.log("Exception",e);
								}
							}
							
							$scope.openImage = function(index)
                            { 
								$rootScope.startLoader();
								var imageUrl = $scope.consentDetailList[$scope.currentIndex].filePath;
								var href = $rootScope.currentHrefDoccument();
								$.fancybox.open({
									width: 600, // or whatever
									height: 320,
									href:href+imageUrl,
									type: 'iframe'
								});
                       $rootScope.stopLoader();
                      }
							$scope.firstLevelTab=1 ;
							
							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstLevelTab === tabNum;
								
							};
							$scope.setFirstTabLevel = function(newTab){
								$scope.firstLevelTab = newTab;
							}
							$scope.setFirstTabLevel(1);
							
							$scope.showPatientEducation = function(){
								$scope.patientEducationTempale = $scope.patientEduObj.patientEducationTempale
							}
}]);
							