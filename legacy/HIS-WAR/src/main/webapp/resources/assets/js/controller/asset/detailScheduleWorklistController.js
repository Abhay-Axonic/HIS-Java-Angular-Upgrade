/**
 * @author  Sufiyan
 */

angular.module('myApp').controller(
		'detailScheduleWorklistController',
		['$scope','$rootScope','$state','$http','GenericService','$sessionStorage','$cookies',
		'growl','$localStorage','$stateParams','CONSTANTS','BillingGenericService',
		function($scope, $rootScope, $state, $http,	GenericService,$sessionStorage,$cookies,
				growl, $localStorage,$stateParams,CONSTANTS,BillingGenericService) {
			
			$scope.assetObject = $stateParams.assetDataObject;
			console.log("Param Data ",$scope.assetObject);
			
			if($scope.assetObject.assetScheduleId == null){
				$state.go('scheduleWorklist');
			}
			
			if($scope.assetObject.pmsType =="InHouse"){
				$scope.pmsTypeSelected ="InHouse";
			}else{
				$scope.pmsTypeSelected ="OutSource";
			}
			
			$scope.init = function(){
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
				if(cookieObjectStore==undefined){
					$rootScope.storePopup();
					angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
				} else {
					$scope.storeId = cookieObjectStore != null ? cookieObjectStore.storeId : undefined;
				}
				$scope.showAdd = true;
				$scope.showSave = true;
				$scope.showView = true;
				
			  	$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				
				$scope.assetChecklist = [];
				$scope.checklistEquipmentList = [];
				$scope.storeList = [];
				$scope.itemResponseList = [];
				$scope.spareConsumptionList = [];
				
				$scope.spareConsumptionObj = {
						itemCode : "",
						itemName: "",
						qty : "",
						itemId : ""
				};
				// Fix for ANGULAR_PENDING_FINDINGS.md entry 40: the spare-consumption
				// store dropdown used to be bound to $scope.scheduleWorklistObj.storeId,
				// a variable never defined anywhere in this controller (copy-paste
				// residue from the sibling scheduleWorklist screen), crashing
				// addSpareConsumption() unconditionally. $scope.storeList (populated
				// below) already has the real data; this is just a real, own model
				// for the dropdown's selection.
				$scope.selectedStoreId = undefined;

				$rootScope.startLoader();
				var data = {
						unitId:$scope.unitId,
						organizationId:$scope.organizationId
						};								
				var URI = BASE_URL + ROOT_URL+ "/global/equipmentchecklist/getAllEquipmentChecklistMasterList";
				BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							$scope.checklistEquipmentList = [];
							if (response.data.status == "success"){
									$scope.checklistEquipmentList = response.data.listObject;
									console.log("$scope.checklistEquipmentList",$scope.checklistEquipmentList);
							}
						});
				
				var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
	            GenericService.serviceAction("GET", URI, {})
	            	.then(function (response) {
	                    $scope.storeList = response.data.listObject;
						console.log("$scope.storeList",$scope.storeList);
	                });	
	            
	            
				if($scope.assetObject.pmsDetailsId !=null && $scope.assetObject.pmsDetailsId !=0 ){
					var objPMS = {pmsDetailsId : $scope.assetObject.pmsDetailsId,
					        unitId:$scope.unitId,
							organizationId: $scope.organizationId};
					console.log("Obj to ",objPMS);
					var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchPMSByPMSId";
					GenericService.serviceAction("POST", URI, objPMS).then(
						function(response) {
							if(response.data.status=="success"){
								console.log("PMS OBJECT ",response.data.object);
								var Object = response.data.object;
								$scope.assetObject.assetScheduleId = Object.assetScheduleId;
								$scope.ticketNo = Object.ticketNo;
								$scope.pmsDetails = Object.pmsDetails;
								$scope.pmsRemarks = Object.pmsRemarks;
								$scope.workOrderStatus = Object.workOrderStatus !== undefined && Object.workOrderStatus !== null && Object.workOrderStatus !== "" ? Object.workOrderStatus : 'N';
								$scope.completionDate = Object.completionDate;
								$scope.completionTime= new Date(Object.completionTime);
								$scope.underObservationDate = Object.underObservationDate;
								$scope.underObservationTime = new Date(Object.underObservationTime);
								$scope.spareConsumptionList  = Object.getSpareConsumptionList!=null?Object.getSpareConsumptionList:[];
							}
					});
				}else{
					$scope.assetObject.pmsDetailsId = 0; 
				}
				$rootScope.stopLoader();
			}
			$scope.init();
			
		
			$scope.dateOptions = {  'year-format': "'yyyy'" };
			$scope.openDatePickerC = function() {
				$scope.datepickerC.opened = true;	};
			$scope.openDatePickerUO = function() {
				$scope.datepickerUO.opened = true;	};
			
			$scope.datepickerC = {	opened : false	};
			$scope.datepickerUO = {	opened : false	};
			
			$scope.getCheckListByAssetScheduleId= function(assetScheduleId,action){
				console.log("getCheckListByAssetScheduleId ()");
				if(action=='view'){
					$scope.showAdd = false;
					$scope.showView = false;
					$scope.showSave = false;
				}else{
					$scope.showAdd = true;
					$scope.showView = true;	
					$scope.showSave = true;
				}
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
							$scope.assetChecklist = [];
							$scope.assetChecklist = response.data.listObject;
						});
				}$rootScope.stopLoader();
			}
			
			$scope.searchItemByCodeOrName = function(){
				var dataForSearch = {
						itemCode: $scope.spareConsumptionObj.itemCode != undefined ? $scope.spareConsumptionObj.itemCode : "",
						itemName: $scope.spareConsumptionObj.itemName != undefined ? $scope.spareConsumptionObj.itemName : "",
						itemId: $scope.assetObject.itemNameId ,
						unitId:$scope.unitId,
						organizationId:$scope.organizationId
					};
				console.log("dataForSearch  ",dataForSearch);
				$rootScope.startLoader();
                var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/getCorrectiveMainSparesComsumptionByitemId";
                return GenericService.serviceAction("POST",URI,dataForSearch).then(function(response){
                    if (response.data.status == 'success'){
                        $rootScope.stopLoader();
                        console.log("AUTO sUGGESTION  ",response.data.listObject);
                        $scope.itemResponseList = [];
                        $scope.itemResponseList = response.data.listObject;
                        return response.data.listObject;
                        } 
                    $rootScope.stopLoader();
                    
                    });
			}
			$scope.selectItem = function(item)
			{
				$scope.spareConsumptionObj.itemCode=item.itemCode;
				$scope.spareConsumptionObj.itemName=item.itemName;
			}
			$scope.addChecklistToAsset = function(){
				console.log("addChecklistToAsset ()");
				console.log("$scope.addChecklist 1 ()",$scope.assetChecklist);
				for(let i=0;i<$scope.checklistEquipmentList.length;i++){
					if($scope.checklistEquipmentList[i].checklistEquipmentMasterId == $scope.checklistMapperObj.checklistEquipmentMasterId){
						var data = {
								assetScheduleChecklistId :0,
								assetScheduleId : $scope.assetObject.assetScheduleId,
								checklistNameId:	$scope.checklistEquipmentList[i].checklistEquipmentMasterId,
								checklistName : $scope.checklistEquipmentList[i].checklistName,
								checklistCode : $scope.checklistEquipmentList[i].checklistCode
							};$scope.assetChecklist.push(data);
					 }
				}
				console.log("$scope.addChecklist 2()",$scope.assetChecklist);
			}
			
			$scope.addAndUpdateChecklistData = function(){
				console.log("addAndUpdateChecklist ()");
				var obj={getChecklistData:$scope.assetChecklist,
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
							growl.success('Asset Schedule Check List Updated sucessfully!!!.',{title : 'Success!'});
						} else {
							growl.error('something wrong!!!!.',{title : 'Error!'});
						}
					});
			}
			
			$scope.removeChecklistToAsset = function(index) {
				console.log("removeChecklistToAsset ()");
				if($scope.assetChecklist[index].assetScheduleChecklistId != 0){
					var assetScheduleChecklistId = $scope.assetChecklist($scope.spareConsumptionList[index].assetScheduleChecklistId);
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
								growl.success('Asset Schedule Check List deleted sucessfully!!!.',{title : 'Success!'});
								
							} else {
								growl.error('something wrong!!!!.',{title : 'Error!'});
							}
						});
				}
				$scope.assetChecklist.splice(index,1);
			};		
			
			
			$scope.addSpareConsumption = function(){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
				if($scope.selectedStoreId != undefined ){
					var storeId = $scope.selectedStoreId;
					var itemObject = {};
					if($scope.itemResponseList.length == 0) {
						growl.error('Please input an Item...!!',	{title : 'Error!'});return false;
					}else{
						console.log("spareConsumptionList before",$scope.spareConsumptionList);
						angular.forEach($scope.itemResponseList,function(value,key) {
							if(value.itemCode == $scope.spareConsumptionObj.itemName || value.itemCode == $scope.spareConsumptionObj.itemCode){
								itemObject.spareConsumptionId = 0;
								itemObject.assetId = value.itemId;
								itemObject.assetName = value.itemName;
								itemObject.assetCode = value.itemCode;
								itemObject.storeId = Number($scope.selectedStoreId);
								itemObject.serialNo = "";
								itemObject.assetQty=1;
								itemObject.remark="";
								$scope.spareConsumptionList.push(itemObject);
							}
						});
						console.log("spareConsumptionList after",$scope.spareConsumptionList);
					}
				}else{
					growl.error('Please Select a Store...!!',	{title : 'Error!'});return false;
				}
			}
			
			$scope.setPMSSrNo = function(index,pmsSrNo){
				$scope.spareConsumptionList[index].serialNo = pmsSrNo;
			}

			$scope.setPMSRemark = function(index,pmsRemark){
				$scope.spareConsumptionList[index].remark = pmsRemark;	
			}
			
			$scope.removeSpareFromList = function(index){
				console.log("i ",index);
				if($scope.spareConsumptionList[index].spareConsumptionId != 0){
					$scope.deleteSpareFromList($scope.spareConsumptionList[index].spareConsumptionId);
				}
				$scope.spareConsumptionList.splice(index,1);
			}	
			
			$scope.deleteSpareFromList = function(spareConsumptionId){
				console.log("to delete ",spareConsumptionId);
				var obj={
						spareConsumptionId : spareConsumptionId,
						createdBy:$scope.userId,
						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
				};
				console.log("obj",obj);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/deleteSpareById";
				GenericService.serviceAction("POST", URI, obj).then(
					function(response) {
						$rootScope.stopLoader();
						if (response.data.status == "success") {
							growl.success('Spare deleted sucessfully!!!.',{title : 'Success!'});
						} else {
							growl.error('something wrong!!!!.',{title : 'Error!'});
						}
					});
			}
			
			$scope.fetchPMSDetailsById = function(){
				var objPMS = {pmsDetailsId : $scope.assetObject.pmsDetailsId,
				        unitId:$scope.unitId,
						organizationId: $scope.organizationId};
				console.log("Obj to ",objPMS);
				var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/fetchPMSByPMSId";
				GenericService.serviceAction("POST", URI, objPMS).then(
					function(response) {
						if(response.data.status=="success"){
							console.log("PMS OBJECT ",response.data.object);
							var Object = response.data.object;
							$scope.assetObject.assetScheduleId = Object.assetScheduleId;
							$scope.ticketNo = Object.ticketNo;
							$scope.pmsDetails = Object.pmsDetails;
							$scope.pmsRemarks = Object.pmsRemarks;
							$scope.workOrderStatus = Object.workOrderStatus !== undefined && Object.workOrderStatus !== null && Object.workOrderStatus !== "" ? Object.workOrderStatus : 'N';
							$scope.completionDate = Object.completionDate;
							$scope.completionTime= Object.completionTime;
							$scope.underObservationDate = Object.underObservationDate;
							$scope.underObservationTime = Object.underObservationTime;
							$scope.spareConsumptionList  = Object.getSpareConsumptionList!=null?Object.getSpareConsumptionList:[];
						}
				});
			}
			
			$scope.savePMSDetails  = function(){
				if($scope.workOrderStatus=='C' &&(($scope.completionDate==undefined || $scope.completionDate == "")|| $scope.completionTime == undefined)){
					growl.warning('Please Select a Completion Date and Time...!!',	{title : 'warning!'});return false;
				}

				if($scope.workOrderStatus=='O' &&(($scope.underObservationDate==undefined || $scope.underObservationDate == "")|| $scope.underObservationTime == undefined)){
					growl.warning('Please Select Date and Time for Under Observation...!!',	{title : 'warning!'});return false;
				}
				
				if($scope.ticketNo==undefined || $scope.ticketNo == null || $scope.ticketNo == ""){
					growl.warning('Please enter ticket no...!!',	{title : 'warning!'});return false;
				}

				if($scope.workOrderStatus=='C' &&($scope.completionDate !== undefined && $scope.completionTime !== undefined)){
                    $scope.underObservationDate = "";
                    $scope.underObservationTime = undefined;
				}

                if($scope.workOrderStatus=='O' &&($scope.underObservationDate !== undefined && $scope.underObservationTime !== undefined)){
                    $scope.completionDate = "";
                    $scope.completionTime = undefined;
                }

                if($scope.workOrderStatus=='S'){
                    $scope.completionDate = "";
                    $scope.completionTime = undefined;
                    $scope.underObservationDate = "";
                    $scope.underObservationTime = undefined;
                }

				var obj = {
					pmsDetailsId: $scope.assetObject.pmsDetailsId,
					assetScheduleId : $scope.assetObject.assetScheduleId,
					pmsType: $scope.pmsTypeSelected,
					ticketNo: $scope.ticketNo,
					pmsDetails: $scope.pmsDetails,
					pmsRemarks: $scope.pmsRemarks,
					workOrderStatus: $scope.workOrderStatus !== undefined && $scope.workOrderStatus !== null && $scope.workOrderStatus !== "" ? $scope.workOrderStatus :'N',
					completionDate: $scope.completionDate,
					completionTime: $scope.completionTime,
					underObservationDate: $scope.underObservationDate,
					underObservationTime: $scope.underObservationTime,
					getChecklistData: $scope.assetChecklist,
					getSpareConsumptionList: $scope.spareConsumptionList,
					createdBy: $scope.userId,
					createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy: $scope.userId,
					updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					unitId:$scope.unitId,
					organizationId: $scope.organizationId
				};
				console.log("to save obj ",obj);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveOrUpdatePMSDetails";
				GenericService.serviceAction("POST", URI, obj).then(
					function(response) {
						$rootScope.stopLoader();
						if (response.data.status == "success") {
							if($scope.assetObject.pmsDetailsId == 0 )
								growl.success('PMS Details Saved sucessfully...!!',	{title : 'Success!'});
							else growl.success('PMS Details Updated sucessfully...!!',	{title : 'Success!'});
							
							console.log("response after save ",response.data.object);
							$scope.assetObject.pmsDetailsId = response.data.object.pmsDetailsId;
							$scope.fetchPMSDetailsById();
						} else {
							growl.error('Something went wrong...!!',	{title : 'Error!'});
						}
					});
				
			}

            $scope.clickCountC = 0;
            $scope.clickCountO = 0;
            $scope.clickCountS = 0;
            $scope.workOrderStatus = 'N';
            $scope.getWorkOrderStatus = function(){
                 if($scope.workOrderStatus !== undefined && $scope.workOrderStatus !== null && $scope.workOrderStatus !== "" && $scope.workOrderStatus ==='C'){
                       if($scope.clickCountC == 0){
                            $scope.workOrderStatus = 'C';
                            $scope.clickCountC = 1
                            $scope.clickCountO = 0;
                            $scope.clickCountS = 0;
                       }
                       else{
                            $scope.workOrderStatus = 'N';
                            $scope.clickCountC = 0;
                       }
                 }
                 else if($scope.workOrderStatus !== undefined && $scope.workOrderStatus !== null && $scope.workOrderStatus !== "" && $scope.workOrderStatus ==='O'){
                       if($scope.clickCountO == 0){
                            $scope.workOrderStatus = 'O';
                            $scope.clickCountO = 1
                            $scope.clickCountC = 0;
                            $scope.clickCountS = 0;
                       }
                       else{
                            $scope.workOrderStatus = 'N';
                            $scope.clickCountO = 0;
                       }
                 }
                 else if($scope.workOrderStatus !== undefined && $scope.workOrderStatus !== null && $scope.workOrderStatus !== "" && $scope.workOrderStatus ==='S'){
                       if($scope.clickCountS == 0){
                            $scope.workOrderStatus = 'S';
                            $scope.clickCountS = 1
                            $scope.clickCountO = 0;
                            $scope.clickCountC = 0;
                       }
                       else{
                            $scope.workOrderStatus = 'N';
                            $scope.clickCountS = 0;
                       }
                 }
                 else{
                    $scope.workOrderStatus = 'N';
                    $scope.clickCountC = 0;
                    $scope.clickCountO = 0;
                    $scope.clickCountS = 0;
                 }
            }

			
			
}]);
