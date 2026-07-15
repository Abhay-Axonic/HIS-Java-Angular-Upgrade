/**
 * @author  Sufiyan
 */

angular
		.module('myApp')
		.controller(
				'createAssetSchedulerController',
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
						'$stateParams',
						'CONSTANTS',
						'BillingGenericService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,CONSTANTS,BillingGenericService) {
							
							$scope.assetInfo = $stateParams.assetInfoObj;
							console.log($scope.assetInfo);
							
							$scope.init = function(){
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								console.log("$scope.assetInfo in $stateParams of createAssetSchedulerController ",$scope.assetInfo);
								if($scope.assetInfo.itemNameId == null || $scope.assetInfo.itemNameId == undefined){
									$state.go('assetPMSScheduler');
								}else{
									var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
									if(cookieObjectStore==undefined){
										$rootScope.storePopup();
										angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
									}
									$scope.storeId = cookieObjectStore.storeId;
									$scope.showAdd = true;
									$scope.showView = true;
									$scope.saveFFF = true;
									
									 $rootScope.startLoader();
								      var URI = BASE_URL+ROOT_URL+"/api/global/frequencyList/"+$scope.organizationId;
								    	  /*CONSTANTS.MASTER.GLOBAL.CLINICAL.FREQUENCY_API;*/
								      var data = {};
								      GenericService.serviceAction("GET", URI, {}).then(function(response) {
								    	  $rootScope.stopLoader();
								    	  console.log(response.data);
								    	  $scope.pmsFrequencyList = response.data;
								      });
								  	
								      $scope.dateOptions = {
                                        formatYear : 'yyyy',
                                        showWeeks : false,
                                        maxDate: moment(new Date()).add(5, 'years').toDate(),
                                        minDate: new Date()
                                      };

                                      $scope.openDatePickerS = function() {
                                        $scope.datepickerS.opened = true;
                                      };
                                      $scope.openDatePickerE = function() {
                                        $scope.datepickerE.opened = true;
                                      };

                                      $scope.datepickerS = {
                                            opened : false
                                      };
                                      $scope.datepickerE = {
                                            opened : false
                                      };

									$scope.schedulerObj ={
											"assetScheduleId":0,
											"itemNameId":$scope.assetInfo.itemNameId,
											"serialNo":$scope.assetInfo.serialNo,
											"assetCategory": $scope.assetInfo.assetCategory,
											"itemCode": $scope.assetInfo.itemCode,
											"itemName": $scope.assetInfo.itemName,
											"serialNo": $scope.assetInfo.serialNo,
											"vendorName": $scope.assetInfo.vendor,
											"vendorId": $scope.assetInfo.vendorId,
											"manufacturerId": $scope.assetInfo.manufacturerId,
//											"pmsFrequency": $scope.assetInfo.pmsFrequencyId,
											"pmsFrequency": "",
											"pmsType":"",
//											"startDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),/*new Date($scope.assetInfo.startDate),*/
//											"endDate": moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),/*new Date($scope.assetInfo.endDate),*/
                                            "startDate":"",
                                            "endDate": "",
											"createdBy":$scope.userId,
											"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											"status":"A",
											"processStatus":0,
											"updatedBy":$scope.userId,
											"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											"unitId":$scope.unitId ,
											"organizationId":$scope.organizationId
									}
									
									var data = {
											unitId:$scope.unitId,
											organizationId:$scope.organizationId
											};								
									var URI = BASE_URL + ROOT_URL+ "/global/equipmentchecklist/getAllEquipmentChecklistMasterList";
									BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
									.then(function(response) {
												$scope.checklistEquipmentList = [];
												if (response.data.status == "success"){
													$rootScope.stopLoader();
														$scope.checklistEquipmentList = response.data.listObject;
												}
											});
									$scope.addChecklist = [ {
										assetScheduleChecklistId : 0,
										assetScheduleId : 0,
										checklistNameId : 0,
										checklistName : '',
										checklistCode : ''									
									}];
									$scope.getChecklistData = [ {
										assetScheduleChecklistId : 0,
										assetScheduleId:0,
										checklistNameId : 0,
										checklistName : '',
										checklistCode : ''									
									}];
									
									$scope.selectedAssetScheduleId=0;
									
									var obj = {
											"itemNameId":$scope.assetInfo.itemNameId,
											"serialNo":$scope.assetInfo.serialNo,
											"unitId":$scope.unitId ,
											"organizationId":$scope.organizationId
									        };
									console.log("Object for fetchAssetSchedule ",obj);
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchAssetSchedule";
									GenericService.serviceAction("POST", URI, obj).then(
										function(response) {
											console.log("createAssetScheduleList ", response.data.listObject);
											$rootScope.stopLoader();
											$scope.createAssetScheduleList = [];
											$scope.createAssetScheduleList = response.data.listObject;
											if($scope.createAssetScheduleList.length>0){
												$scope.saveFFF = false;
											}
										});
								}
								
							}
							$scope.init();
							
							
							$scope.getProductCategoryList = function () {
								var id = $scope.assetSearchObj.assetCategory;
					            var URI = BASE_URL+ROOT_URL+"/api/global/products/categories/" + id + "/"+ $scope.organizationId;
					            $rootScope.startLoader();
					            GenericService.serviceAction("GET", URI, {}).then(
					                function (response) {
					                	$scope.productCategoriesList = response.data;
					                    $rootScope.stopLoader();
					                },
					                function (err) {
					                    $rootScope.stopLoader();
					                });
					        }

                            $scope.createAssetSchedule = function(){
                                if (!$scope.schedulerObj.pmsFrequency) {
                                    growl.warning('Select PMS frequency', { title: 'warning!' });
                                    return;
                                }
                                if (!$scope.schedulerObj.pmsType) {
                                    growl.warning('Select PMS type', { title: 'warning!' });
                                    return;
                                }

                                if (!$scope.schedulerObj.startDate) {
                                    growl.warning('Please select from date', { title: 'warning!' });
                                    return false;
                                }
                                if (!$scope.schedulerObj.endDate) {
                                    growl.warning('Please select end date', { title: 'warning!' });
                                    return false;
                                }

                                $scope.dateSlotList = [{}];

                                var date = new Date();
                                $scope.dt = moment(date).format('DD-MM-YYYY HH:ss:mm');

                                function calcDate(startDate, endDate) {
                                    var dateFormat = 'DD-MM-YYYY';
                                    var freqMap = {
                                        'D': { amount: 1, unit: 'days', label: 'Daily' },
                                        'W': { amount: 1, unit: 'weeks', label: 'Weekly' },
                                        'M': { amount: 1, unit: 'months', label: 'Monthly' },
                                        'Q': { amount: 3, unit: 'months', label: 'Quarterly' },
                                        'HY': { amount: 6, unit: 'months', label: 'Half Yearly' },
                                        'Y': { amount: 1, unit: 'years', label: 'Yearly' },
                                    };

                                    var frequency = freqMap[$scope.schedulerObj.pmsFrequency];

                                    $scope.createAssetScheduleList = [];

                                    var currentDate = moment(startDate);
                                    var endMoment = moment(endDate);

                                    while (currentDate.isSameOrBefore(endMoment)) {
                                        var data = {
                                            assetScheduleId: 0,
                                            itemNameId: $scope.schedulerObj.itemNameId,
                                            itemCode: $scope.schedulerObj.itemCode,
                                            itemName: $scope.schedulerObj.itemName,
                                            serialNo: $scope.schedulerObj.serialNo,
                                            vendor: $scope.schedulerObj.vendorName,
                                            vendorId: $scope.schedulerObj.vendorId,
                                            pmsFrequency: frequency.label,
                                            nextDate: currentDate.toDate(),
                                            pmsType: $scope.schedulerObj.pmsType,
                                            remark: '',
                                            storeId: $scope.storeId,
                                            manufacturerId: $scope.schedulerObj.manufacturerId,
                                            processStatus: 1,
                                            status: 'A',
                                            unitId: $scope.unitId,
                                            organizationId: $scope.organizationId,
                                            createdBy: $scope.userId,
                                            createdDate: $scope.dt,
                                            updatedBy: $scope.userId,
                                            updatedDate: $scope.dt,
                                            getChecklistData: []
                                        };
                                        $scope.createAssetScheduleList.push(data);

                                        currentDate.add(frequency.amount, frequency.unit);
                                    }
                                }

                                var startDate = new Date($scope.schedulerObj.startDate);
                                var endDate = new Date($scope.schedulerObj.endDate);
                                calcDate(startDate, endDate);
                            };

							$scope.i = "";
							$scope.setIndex = function(index){
								console.log(" setIndex ()");
								console.log(" selectedAssetScheduleId ",$scope.selectedAssetScheduleId);
								$scope.i = index;
								$('#addViewChecklistModal').show('show');
								$scope.showAdd = true;
								$scope.showView = true;
								if($scope.selectedAssetScheduleId==0){
									$scope.addChecklist = [];
									console.log("List ",$scope.createAssetScheduleList[index]);
									angular.forEach($scope.createAssetScheduleList[index].getChecklistData,function(value,key) {
											 $scope.addChecklist.push(value);
									});
								}
							}
							
							$scope.getIndex = function(index){
								console.log(" getIndex ()",index);
								console.log(" selectedAssetScheduleId ",$scope.selectedAssetScheduleId);
								$scope.showAdd = false;
								$scope.i = index;
								$scope.showView = false;
								if($scope.selectedAssetScheduleId==0){
									$scope.addChecklist = [];
									console.log("List ",$scope.createAssetScheduleList[index]);
									angular.forEach($scope.createAssetScheduleList[index].getChecklistData,function(value,key) {
											 $scope.addChecklist.push(value);
									});
								}
							}
							
							
							$scope.addChecklistData = function(){
								console.log(" addChecklistData ()");
								console.log(" selectedAssetScheduleId ",$scope.selectedAssetScheduleId);
								if($scope.selectedAssetScheduleId !=0){
									$scope.addAndUpdateChecklist();
								}else{
									if($scope.addChecklist.length>0){
										$scope.createAssetScheduleList[$scope.i].getChecklistData = $scope.addChecklist;
									}else{
										growl.warning('Please add atleast one checkList!!!!.',{title : 'warning!'});
									}
									console.log($scope.createAssetScheduleList);
									
								}
								
							}
							// Add for Checklist Popup
							$scope.addChecklistToAsset = function(){
								console.log("addChecklistToAsset ()");
								console.log("$scope.addChecklist 1 ()",$scope.addChecklist);
								for(let i=0;i<$scope.checklistEquipmentList.length;i++){
									if($scope.checklistEquipmentList[i].checklistEquipmentMasterId == $scope.checklistMapperObj.checklistEquipmentMasterId){
										var data = {
												assetScheduleChecklistId :0,
												assetScheduleId : $scope.selectedAssetScheduleId,
												checklistNameId : $scope.checklistEquipmentList[i].checklistEquipmentMasterId,
												checklistName : $scope.checklistEquipmentList[i].checklistName,
												checklistCode : $scope.checklistEquipmentList[i].checklistCode
											};$scope.addChecklist.push(data);
									 }
								}
								console.log("$scope.addChecklist 2()",$scope.addChecklist);
							}
							
							// Remove for Checklist Popup
							$scope.removeChecklistToAsset = function(index) {
								$scope.addChecklist.splice(index,1);
								$scope.createAssetScheduleList[$scope.i].getChecklistData.splice(index,1);
							}		
							
							$scope.saveAssetSchedule = function(){
								console.log("saveAssetSchedule ()");
								console.log($scope.createAssetScheduleList);
								if($scope.createAssetScheduleList.length != 0){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveAssetSchedule";
									GenericService.serviceAction("POST", URI, $scope.createAssetScheduleList).then(
											function(response) {
											$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success('Asset Schedule Saved sucessfully...!!',	{title : 'Success!'});
												$scope.fetchAssetSchedule();
											} else {
												growl.error('Something went wrong...!!',	{title : 'Error!'});
											}
										});
								}else{
									growl.error('No Data to Save...!!',	{title : 'Error!'});
								}
							}
							
							$scope.updateAssetSchedule = function(obj){
								console.log("updateAssetSchedule ()");
								if(obj.assetScheduleId == 0){
									growl.error('Asset Not Scheduled yet!!!!.',{title : 'Error!'});
									return false;
								}else{
									$rootScope.startLoader();
									console.log("obj",obj);
									obj.updatedBy = $scope.userId;
									obj.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/updateAssetSchedule";
									GenericService.serviceAction("POST", URI, obj).then(
										function(response) {
										$rootScope.stopLoader();
											if (response.data.status == "success") {
												growl.success('Asset Schedule Updated Successfully!!!.',{title : 'Success!'});
												$scope.fetchAssetSchedule();
											} else {
												growl.error('something wrong!!!!.',{title : 'Error!'});
											}
									});
								}
							}
							
							$scope.fetchAssetSchedule = function(){
								console.log("fetchAssetSchedule ()");
								var obj = {	
										"itemNameId":$scope.assetInfo.itemNameId,
										"serialNo":$scope.assetInfo.serialNo,
										"unitId":$scope.unitId ,
										"organizationId":$scope.organizationId
								}
								console.log("object ",obj);
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchAssetSchedule";
								GenericService.serviceAction("POST", URI, obj).then(
									function(response) {
										console.log("createAssetScheduleList ",response.data.listObject);
										$rootScope.stopLoader();
										$scope.createAssetScheduleList = [];
										$scope.createAssetScheduleList = response.data.listObject;
									});
								
							}
							
							$scope.getCheckListByAssetScheduleId= function(assetScheduleId){
								console.log("getCheckListByAssetScheduleId ()");
								console.log(" selectedAssetScheduleId ",$scope.selectedAssetScheduleId);
								if(assetScheduleId != 0){
									var obj = {
											assetScheduleId:assetScheduleId,
											unitId:$scope.unitId ,
									        organizationId:$scope.organizationId
									        };
									console.log("object  ",obj);
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId";
									GenericService.serviceAction("POST", URI, obj).then(
										function(response) {
											$rootScope.stopLoader();
											$scope.addChecklist = [];
											$scope.addChecklist = response.data.listObject;
										});
								}
							}
						
							$scope.setCheckList= function(index,assetScheduleId){
								console.log("setCheckList ()");
								$scope.i = index;
								$scope.addChecklist.length = 0;
								$('#addViewChecklistModal').show('show');
								$scope.selectedAssetScheduleId = assetScheduleId;
								$scope.getCheckListByAssetScheduleId(assetScheduleId);
							}
							
							$scope.addToCheckList = function(index,assetScheduleId){
								console.log("addToCheckList ()");
								$scope.selectedAssetScheduleId = assetScheduleId;
								$scope.getCheckListByAssetScheduleId(assetScheduleId);
								console.log(" selectedAssetScheduleId ",$scope.selectedAssetScheduleId);
							}
							
							$scope.addAndUpdateChecklist= function(){
								console.log("addAndUpdateChecklist ()");
								if($scope.addChecklist.length > 0){
									var obj={getChecklistData:$scope.addChecklist,
										createdBy:$scope.userId,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										unitId:$scope.unitId ,
								        organizationId:$scope.organizationId};
									console.log("Check List Data Object ",obj);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/addAndUpdateChecklist";
								GenericService.serviceAction("POST", URI, obj).then(
									function(response) {
										$rootScope.stopLoader();
										if (response.data.status == "success") {
											growl.success('Asset Schedule Check List Updated Successfully!!!.',{title : 'Success!'});
										} else {
											growl.error('something wrong!!!!.',{title : 'Error!'});
										}
									});$scope.selectedAssetScheduleId = 0;
								}else{
									growl.error('No Data to Save !!!!.',{title : 'Error!'});
								}	
							}
							
							$scope.deleteChecklist= function(assetScheduleChecklistId){
								console.log("deleteChecklist ()");
								var obj={
										assetScheduleChecklistId : assetScheduleChecklistId,
										createdBy:$scope.userId,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								};
								console.log("obj",obj);
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/deleteChecklist";
								GenericService.serviceAction("POST", URI, obj).then(
									function(response) {
										$rootScope.stopLoader();
										if (response.data.status == "success") {
											growl.success('Asset Schedule Check List deleted Successfully!!!.',{title : 'Success!'});
											$scope.getCheckListByAssetScheduleId($scope.selectedAssetScheduleId);
										} else {
											growl.error('something wrong!!!!.',{title : 'Error!'});
										}
									});
							}
							
}]);
