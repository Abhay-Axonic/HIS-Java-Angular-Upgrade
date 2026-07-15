angular
		.module('myApp')
		.controller(
				'detailsCorrectiveMaintenanceScheduleWorklistController',
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
						'$timeout',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,$timeout) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.assetMaintenanceSpecialityId = cookieObjectAppConfig.assetMaintenanceSpecialityId;
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.storeId = cookieObject.storeId;
							$scope.detailsObj = $stateParams.detailsObj;
							
							console.log("$scope.detailsObj",$scope.detailsObj);
							
							$scope.newStoreId = 0;
							var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
							if(cookieObjectStore!=undefined){
								$scope.newStoreId = cookieObjectStore.storeId;
							}
							
							//from date
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
							
							 $scope.fromdateOptions ={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate :new Date(),
							 };
							 
							 $scope.serialNoObj ={};
							// to date
									$scope.openToDatePicker = function() {
										$scope.toDatepicker.opened = true;
									};

									$scope.toDatepicker = {
											opened : false
									};
									
									 $scope.todateOptions ={
												formatYear : 'yyyy',
												showWeeks : false,
												minDate :new Date(),
									 };
							 
							 //asset Up DatePicker
							 $scope.openAssetUpDatepicker = function() {
									$scope.assetUpDatepicker.opened = true;
								};

								$scope.assetUpDatepicker = {
										opened : false
								};
								
								 $scope.assetUpDatepickerOptions ={
											formatYear : 'yyyy',
											showWeeks : false,
											minDate :new Date(),
								 };
							 
								
							//scheduled date Picker
								 $scope.openScheduledDatepicker = function() {

									 $scope.scheduledDatepicker.opened = true;
									};

									$scope.scheduledDatepicker = {
											opened : false
									};
							
									 $scope.scheduledDatepicker ={
												formatYear : 'yyyy',
												showWeeks : false,
												minDate :new Date(),
									 };
								 
								 
							// function for initializa clockpicker
								$scope.initClockPicker = function() {

									$('.clockpicker').clockpicker();
								}
							
							$scope.scheduleTime =$scope.detailsObj.scheduleTime;
							$scope.scheduleDate = moment($scope.detailsObj.scheduleDate,"DD-MM-YYYY").toDate();
							$scope.assignTo = $scope.detailsObj.assignTo;
							$scope.allocationRemark = $scope.detailsObj.allocationRemark;
							$scope.isResolutionDisabled = false;
							$scope.isAllocationDisabled = false;
							$scope.replaceBtn=false;
							$scope.returnBtn=true;
							
							$scope.resolutionObj = {
									correctiveMaintenanceScheduleId : null,
									correctiveMaintenanceWorklistId : "",
									requestAllocationTypeId : 1,
									ticketNo : "",
									resolutionDetails : "",
									vendorId: "",
									isCompleted : false,
									assetUpDate : "",
									assetUpTime : "",
									underObservation : "",
									fromDate : "",
									toDate : "",
									isStart : "",
									maintenanceScheduleStatusId : "",
									completionDate : "",
									remark : "",
									createdBy : $scope.userId,
									createdDate : "",
									status : "A",
									updatedBy : $scope.userId,
									updatedDate : "",
									unitId : $scope.unitId,
									organizationId : $scope.organizationId ,
									correctiveMaintenanceId : "",
									scheduleDate : "",
									scheduleTime : "",
									assignTo : "",
									allocationRemark : "",
									serialNumber : "",
									isDown : "",
									requestDateTime : "",
									downDateTime : "",
									resolutionExpectedDateTime : "",
									requestDetails : "",
									assetName : "",
									assetCode : "",
									assetcategory : "",
									userName : "",
									resolutionExpectedTime : "",
									downTime : "",
									callLogNumber : "",
									requestedByName : "",
									storeId : "",
									isWorklist : "",
									isStart : false,
									correctiveMaintenanceSparesComsumptionDto : []
							};
							
							if($scope.detailsObj.maintenanceWorklistStatusId == 4){
								$scope.resolutionObj.isStart = true;
								$scope.isResolutionDisabled = false;
								$scope.isAllocationDisabled = true;
								
							}else{
								$scope.resolutionObj.isStart = false;
								$scope.isResolutionDisabled = true;
								$scope.isAllocationDisabled = false;
							}
							
							$scope.usersList = [];
							$scope.getEmployeeList = function(){
								try{
									$rootScope.startLoader();
									
									var data = {
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											specialityId : $scope.assetMaintenanceSpecialityId 
									};
									var URI = BASE_URL + ROOT_URL+ "/api/global/getEmployeeListBySpecialityAndSubSpeciality";
									GenericService.serviceAction("POST", URI, data).then(function(response)
											{
													$scope.usersList = [];
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.usersList = response.data.listObject;
													}
											});
									
									}catch(e){
										$rootScope.stopLoader();
										console.log("Exception-",e);
									}
							}
							
							$scope.getEmployeeList();
							
							$scope.updateAllocationDetails = function(){
								$scope.updateObj = {
										correctiveMaintenanceWorklistId : $scope.detailsObj.correctiveMaintenanceWorklistId,
										correctiveMaintenanceId : $scope.detailsObj.correctiveMaintenanceId,
										scheduleDate : moment($scope.scheduleDate).format('DD-MM-YYYY HH:mm:ss') ,
										scheduleTime : $scope.scheduleTime ,
										maintenanceWorklistStatusId : 3, //assigned
										assignTo: $scope.assignTo,
										allocationRemark: $scope.allocationRemark,
										createdBy:$scope.userId,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										status:"A",
										updatedBy:$scope.userId,
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
								}
								if($scope.updateObj.assignTo == ""){
									
									growl.warning("Please Select Assign To",
											{
												title : 'warning!'
											});
									
								}else if($scope.updateObj.scheduleDate == "" || $scope.updateObj.scheduleDate == undefined){
									
									growl.warning("Please select scheduleDate",
											{
												title : 'warning!'
											});
									
								}else if($scope.updateObj.scheduleTime == ""){
									
									growl.warning("Please select scheduleTime",
											{
												title : 'warning!'
											});
									
								}else{
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveCorrectiveMaintenance/worklist";
									
									GenericService.serviceAction("POST",URI,$scope.updateObj).then(function(response){
											
										if (response.data.status == 'success'){
												
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
															title : 'Success!'
													});
											$state.go('correctiveMaintenanceScheduleWorklist');
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
							}
							
							 $scope.vendorObj = {};
	                            $scope.getVendorListByBatch = function(batchId){
	                                $scope.vendorList = [];
	                                var data = {
	                                        batchId : batchId ,
	                                        unitId : $scope.unitId,
	                                        organizationId : $scope.organizationId
	                                }
	                                $rootScope.startLoader();
	                                var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/getvendorlistByBatchId";
	                                
	                                GenericService.serviceAction("POST",URI,data).then(function(response){
	                                        
	                                    if (response.data.status == 'success'){
	                                            
	                                        $rootScope.stopLoader();
	                                        if(response.data.object!=null){
	                                        	$scope.vendorObj = response.data.object;
	                                        }
	                                        } 
	                                    $rootScope.stopLoader();
	                                    });
	                                        
	                            }
	                            
	                            $scope.getVendorList = function(){
	                                $scope.vendorList = [];
	                                $rootScope.startLoader();
	                                var URI = BASE_URL + ROOT_URL + "/api/unit/vendors//"+$scope.organizationId +"/" + $scope.unitId;
	                                
	                                GenericService.serviceAction("GET",URI).then(function(response){
	                                        
	                                    if (response.status == 200){
	                                            
	                                        $rootScope.stopLoader();
	                                        $scope.vendorList = response.data;
	                                        
	                                        } 
	                                    $rootScope.stopLoader();
	                                    });
	                                        
	                            }
							
							$scope.isDisable= false;
							$scope.changeStatus = function(){
								
								if($scope.resolutionObj.requestAllocationTypeId == 0){
									
									//call vendor ByBatch that is Service provider name api
									 $scope.getVendorListByBatch($scope.detailsObj.batchId);
									$scope.isDisable= false;
								}else if($scope.resolutionObj.requestAllocationTypeId == 2){
									
									//call vendor api vender name list
									 $scope.getVendorList();
									$scope.isDisable= false;
								}else{
									
									$scope.resolutionObj.ticketNo = "";
									$scope.resolutionObj.vendorName = "";
									$scope.vendorObj = {};
									$scope.isDisable= true;
								}
								
							}
							
							$scope.isComplete = function(){
								
								var d = new Date();
								var minutes = 0;
								var hours =0;
								
								if(d.getHours()<9){
									hours = "0"+d.getHours();
								}else{
									hours = d.getHours();
								}
								
								if(d.getMinutes()<9){
									minutes = "0"+d.getMinutes();
								}else{
									minutes = d.getMinutes();
								}

								if($scope.resolutionObj.isCompleted){
									$scope.resolutionObj.underObservation = false;
									$scope.resolutionObj.fromDate = "";
									$scope.resolutionObj.toDate = "";
									$scope.resolutionObj.assetUpTime = hours+":"+minutes+":00";
									$scope.resolutionObj.assetUpDate = moment(d,"DD-MM-YYYY").toDate();
									
								}else{
									$scope.resolutionObj.isCompleted = false;
									$scope.resolutionObj.underObservation = true;
									$scope.resolutionObj.assetUpDate = "";
									$scope.resolutionObj.assetUpTime = "";
									$scope.resolutionObj.fromDate = moment(d,"DD-MM-YYYY").toDate();
									$scope.resolutionObj.toDate = moment(d,"DD-MM-YYYY").toDate();
									
									
								}
							}
							
							$scope.updateStartedTask = function(){
								
								if($scope.resolutionObj.isStart){
									
									var dataForUpdate = {
											updatedBy:$scope.userId,
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											unitId:$scope.unitId,
											organizationId:$scope.organizationId,
											correctiveMaintenanceId : $scope.detailsObj.correctiveMaintenanceId,
											maintenanceStatusId : 4 //for work in progress
									}
									
									$rootScope.startLoader();
	                                var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/cancelCorrectiveMaintenanceRequest";
	                                
	                                GenericService.serviceAction("POST",URI,dataForUpdate).then(function(response){
	                                        
	                                    if (response.data.status == 'success'){
	                                            
	                                        $rootScope.stopLoader();
	                                        $scope.isAllocationDisabled = true
	    									$scope.isResolutionDisabled = false;
	                                        } 
	                                    $rootScope.stopLoader();
	                                    });
								}else{
									
									$scope.isAllocationDisabled = false
									$scope.isResolutionDisabled = true;
								}
							}
							
							//spare consumption
							$scope.spareConsumptionObj = {
									itemCode : "",
									itemName: "",
									qty : "",
									itemId : ""
							};
							
							
							$scope.stock = 0 ;
							$scope.itemStock = function(){
								var date = new Date();
					        	date.setDate(date.getDate() + 1);	
								var dataForSearch = {
										itemId: $scope.detailsObj.itemId ,
										fromDate : moment(date).format('YYYY-MM-DD'),
										storeId: $scope.newStoreId,
									};
								$rootScope.startLoader();
                                var URI = BASE_URL + ROOT_URL + "/api/inventory/stores/stock/dashboard/searchdatewise";
                                
                                return GenericService.serviceAction("POST",URI,dataForSearch).then(function(response){
                                    $rootScope.stopLoader();
                                    
                                    if(!!response && !!response.data && response.data.length > 0){  
                                    	
                                    	$scope.stock = response.data[0].currentStock;
                                    	$scope.spareConsumptionObj.stock = $scope.stock;
                                      }
                                    });
							}
							
							
							$scope.searchItemByCodeOrName = function(){
								
								var dataForSearch = {
										itemCode: $scope.spareConsumptionObj.itemCode,
										itemName: $scope.spareConsumptionObj.itemName ,
										itemId: $scope.detailsObj.itemId ,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
									};
								$rootScope.startLoader();
                                var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/getCorrectiveMainSparesComsumptionByitemId";
                                
                                return GenericService.serviceAction("POST",URI,dataForSearch).then(function(response){
                                        
                                    if (response.data.status == 'success'){
                                            
                                        $rootScope.stopLoader();
                                        return response.data.listObject;
                                        } 
                                    $rootScope.stopLoader();
                                    });
							}
							
							$scope.selectItem = function(info){
								
								$scope.itemStock();
								
								$scope.spareConsumptionObj.itemName = info.itemName;
								$scope.spareConsumptionObj.itemCode = info.itemCode;
								$scope.spareConsumptionObj.itemId = info.itemId;
							}
							
							$scope.sparesComsumptionList = [];
							$scope.addSpareDetails = function(){
								var isItemExist = false;
								angular.forEach($scope.sparesComsumptionList,function(value,key){
									
									if(value.itemId ==  $scope.spareConsumptionObj.itemId && value.status =='A'){
										isItemExist = true;
									}
								});
								if(isItemExist){
									growl.warning("Selected Item Already Exist",
											{
												title : 'warning!'
											});
									$scope.spareConsumptionObj = {
											itemCode : "",
											itemName: "",
											qty : "",
											itemId : ""
									};
								}else{
									
									if($scope.spareConsumptionObj.itemName == ""){
										growl.warning("Please Selected Item ",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.spareConsumptionObj.qty == ""){
										growl.warning("Please Selected Item Qty",
												{
													title : 'warning!'
												});
										return false;
									}else if($scope.spareConsumptionObj.stock < $scope.spareConsumptionObj.qty ){
										growl.warning("Spare stock should be greater than enter Qty ",
												{
													title : 'warning!'
												});
										return false;
									}else{
										
										newArr = {};
										newArr.correctiveMaintenanceSparesComsumptionId = null;
										newArr.itemName = $scope.spareConsumptionObj.itemName;
										newArr.itemCode = $scope.spareConsumptionObj.itemCode;
										newArr.itemId = $scope.spareConsumptionObj.itemId;
										newArr.qty = $scope.spareConsumptionObj.qty;
										newArr.sparesComsumptionRemark = "";
										newArr.createdBy = $scope.userId,
										newArr.stock = $scope.spareConsumptionObj.stock;
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										newArr.status = "A",
										newArr.updatedBy = $scope.userId,
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										newArr.unitId = $scope.unitId,
										newArr.organizationId = $scope.organizationId,
										$scope.sparesComsumptionList.push(newArr);
										$scope.spareConsumptionObj = {
												itemCode : "",
												itemName: "",
												qty : "",
												itemId : ""
										};
										$scope.stock = 0;
									}
								}
							}
							
							$scope.tempSpareList = [];
							$scope.removeSpareItems = function(index,info){
								if(info.correctiveMaintenanceSparesComsumptionId!=null){
									info.status='I';
									$scope.tempSpareList.push(info);
									$scope.sparesComsumptionList.splice(index,1);
								}else{
									$scope.sparesComsumptionList.splice(index,1);
								}
							}
							
							$scope.sparesComsumptionList = [];
							$scope.getConsumptionDetails = function(){
								$scope.sparesComsumptionList = [];
								$rootScope.startLoader();
								var data = {
										correctiveMaintenanceScheduleId: $scope.detailsObj.correctiveMaintenanceScheduleId,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
								}
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/getSparesComsumptionlistById";
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success'){
										$rootScope.stopLoader();
										$scope.sparesComsumptionList = response.data.listObject;
										}
									});
							}
							
							
							$scope.getReplaceDetails = function(){
								$scope.replaceDetailsList = [];
								$rootScope.startLoader();
								var data = {
										correctiveMaintenanceScheduleId: $scope.detailsObj.correctiveMaintenanceScheduleId,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
								}
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/getreplaceItemById";
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success'){
										$rootScope.stopLoader();
										$scope.replaceDetailsList = response.data.listObject;
										console.log("$scope.replaceDetailsList",$scope.replaceDetailsList);
										if($scope.replaceDetailsList.length>0){
											$scope.replaceBtn=true;
											$scope.returnBtn=false;
										}
										}
									});
							}
							
							if($scope.detailsObj.correctiveMaintenanceScheduleId!=undefined){
								
								if($scope.detailsObj.assetUpDate == null){
									
									$scope.resolutionObj.fromDate=  moment($scope.detailsObj.fromDate,"DD-MM-YYYY").toDate();
									$scope.resolutionObj.toDate=  moment($scope.detailsObj.toDate,"DD-MM-YYYY").toDate();
								}else{
									
									$scope.resolutionObj.assetUpDate =  moment($scope.detailsObj.assetUpDate,"DD-MM-YYYY").toDate();
									$scope.resolutionObj.assetUpTime= $scope.detailsObj.assetUpTime;
								}
							
								
								if($scope.detailsObj.vendorId !=null){
									$scope.resolutionObj.vendorId= $scope.detailsObj.vendorId;
									if($scope.detailsObj.requestAllocationTypeId == 0){
										//call vendor ByBatch that is Service provider name api
										 $scope.getVendorListByBatch($scope.detailsObj.batchId);
									}else if($scope.detailsObj.requestAllocationTypeId == 2){
										//call vendor api vender name list
										 $scope.getVendorList();
										 $scope.vendorObj.vendorId = $scope.resolutionObj.vendorId;
									}else{
										$scope.resolutionObj.ticketNo = "";
										$scope.resolutionObj.vendorName = "";
										$scope.vendorObj = {};
									}
								}
								$scope.resolutionObj.batchId= $scope.detailsObj.batchId;
								$scope.resolutionObj.completionDate= $scope.detailsObj.completionDate;
								$scope.resolutionObj.correctiveMaintenanceScheduleId= $scope.detailsObj.correctiveMaintenanceScheduleId;
								$scope.resolutionObj.correctiveMaintenanceSparesComsumptionDto= $scope.detailsObj.correctiveMaintenanceSparesComsumptionDto;
								$scope.resolutionObj.createdBy= $scope.detailsObj.createdBy;
								$scope.resolutionObj.createdDate= $scope.detailsObj.createdDate;
							
								$scope.resolutionObj.isCompleted= $scope.detailsObj.isCompleted;
								$scope.resolutionObj.isStart= $scope.detailsObj.isStart;
								$scope.resolutionObj.underObservation= $scope.detailsObj.isUnderObservation;
								$scope.resolutionObj.itemId= $scope.detailsObj.itemId;
								$scope.resolutionObj.maintenanceScheduleStatusId= $scope.detailsObj.maintenanceScheduleStatusId;
								$scope.resolutionObj.organizationId= $scope.detailsObj.organizationId;
								$scope.resolutionObj.remark= $scope.detailsObj.reSolutionRemark;
								$scope.resolutionObj.requestAllocationTypeId= $scope.detailsObj.requestAllocationTypeId;
								$scope.resolutionObj.resolutionDetails= $scope.detailsObj.resolutionDetails;
								$scope.resolutionObj.status= $scope.detailsObj.status;
								$scope.resolutionObj.ticketNo= $scope.detailsObj.ticketNo;
							
								$scope.resolutionObj.unitId= $scope.detailsObj.unitId;
								$scope.resolutionObj.updatedBy= $scope.detailsObj.updatedBy;
								$scope.resolutionObj.updatedDate= $scope.detailsObj.updatedDate;
								
								$scope.getConsumptionDetails();
								$scope.getReplaceDetails();
							}
							
							$scope.serialNumberList = [];
							$scope.getSerialNumbers = function(){
								$scope.serialNumberList = [];
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getSerialNoByItemId/"+$scope.detailsObj.itemId+"/"+$scope.unitId+"/"+$scope.organizationId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                    $rootScope.stopLoader();
						                    
						                    angular.forEach(response.data.listObject ,function(value,index){
						                    	if(value.batchId !=$scope.detailsObj.batchId){
						                    		$scope.serialNumberList.push(value);
						                    	}
						                    });
						                    
						                    if($scope.serialNumberList.length == 0){
						                    	$rootScope.stopLoader();
												growl.warning("Stock Not Available",
														{
															title : 'warning!'
														});
												return false;
						                    }
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							
						
							$scope.replaceAssetObj = {};
							$scope.saveReplace = function(){
								
								if($scope.serialNoObj.batchId== "" || $scope.serialNoObj.batchId == undefined){

									growl.warning("Please Select Serial Number" ,
											{
												title : 'warning!'
											});
									return false;
								}
								
								$scope.replaceAssetObj.correctiveMaintenanceId = $scope.detailsObj.correctiveMaintenanceId;
								$scope.replaceAssetObj.itemId = $scope.detailsObj.itemId;
								$scope.replaceAssetObj.requestedStoreId = $scope.detailsObj.storeId;
								$scope.replaceAssetObj.requestedSerialNoId = $scope.detailsObj.batchId;
								$scope.replaceAssetObj.newSerialNoId = $scope.serialNoObj.batchId;
								$scope.replaceAssetObj.newStoreId = $scope.newStoreId;
								$scope.replaceAssetObj.createdBy = $scope.userId;
								$scope.replaceAssetObj.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								//$scope.replaceAssetObj.status = 'A';
								$scope.replaceAssetObj.updatedBy = $scope.userId;
								$scope.replaceAssetObj.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								$scope.replaceAssetObj.unitId = $scope.unitId;
								$scope.replaceAssetObj.organizationId = $scope.organizationId;
							}
							
							
							$scope.saveDetails = function(){
								var assetDt,fromDt,toDate,completionDate;
								if($scope.resolutionObj.isCompleted){ 
									
									if($scope.resolutionObj.assetUpDate == "" ||  $scope.resolutionObj.assetUpDate == undefined){
										
										growl.warning("Please Select Asset Up Date" ,
												{
													title : 'warning!'
												});
										return false;
									}
									if($scope.resolutionObj.assetUpTime == ""){
										growl.warning("Please Select Asset Up Time" ,
												{
													title : 'warning!'
												});
										return false;
									}
									
									assetDt =  moment($scope.resolutionObj.assetUpDate).format('DD-MM-YYYY HH:mm:ss');
									$scope.resolutionObj.maintenanceScheduleStatusId = 2 ; //for completed
									completionDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								}else{
									
									if($scope.resolutionObj.fromDate == "" ||  $scope.resolutionObj.fromDate == undefined){
										
										growl.warning("Please Select From Date" ,
												{
													title : 'warning!'
												});
										return;
									}else if($scope.resolutionObj.toDate == "" ||  $scope.resolutionObj.toDate == undefined){
										
										growl.warning("Please Select ToDate" ,
												{
													title : 'warning!'
												});
										return;
									}
									$scope.resolutionObj.assetUpTime = null;
									fromDt = moment($scope.resolutionObj.fromDate).format('DD-MM-YYYY HH:mm:ss');
									toDate = moment($scope.resolutionObj.toDate).format('DD-MM-YYYY HH:mm:ss');
									$scope.resolutionObj.maintenanceScheduleStatusId = 4 ; // Work In Progress
								}
								if($scope.tempSpareList.length>0){
									 $scope.sparesComsumptionList =  $scope.sparesComsumptionList.concat($scope.tempSpareList);
								}
								$scope.saveObj = {
										correctiveMaintenanceScheduleId :  $scope.detailsObj.correctiveMaintenanceScheduleId!=null ? $scope.detailsObj.correctiveMaintenanceScheduleId : null,
										correctiveMaintenanceWorklistId : $scope.detailsObj.correctiveMaintenanceWorklistId,
										correctiveMaintenanceId: $scope.detailsObj.correctiveMaintenanceId,
										assetUpTime : $scope.resolutionObj.assetUpTime,
										assetUpDate : assetDt ,
										completionDate : completionDate,
										fromDate : fromDt,
										toDate : toDate,
										isCompleted : $scope.resolutionObj.isCompleted,
										isStart : $scope.resolutionObj.isStart,
										isUnderObservation : $scope.resolutionObj.underObservation,
										maintenanceScheduleStatusId : $scope.resolutionObj.maintenanceScheduleStatusId,
										requestAllocationTypeId : $scope.resolutionObj.requestAllocationTypeId,
										resolutionDetails :  $scope.resolutionObj.resolutionDetails,
										ticketNo : $scope.resolutionObj.ticketNo,
										vendorId : ($scope.vendorObj !=null && $scope.vendorObj.vendorId!=undefined) ? $scope.vendorObj.vendorId : null ,
										remark : $scope.resolutionObj.remark,
										createdBy:$scope.userId,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										status:"A",
										updatedBy:$scope.userId,
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										correctiveMaintenanceSparesComsumptionDto : $scope.sparesComsumptionList,
										correctiveMaintenanceReplacementDto : $scope.replaceAssetObj,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										storeId: $scope.storeId
								};
							
								if($scope.saveObj.requestAllocationTypeId == 0 && $scope.saveObj.ticketNo ==" "){
										growl.warning("Please Enter Ticket No",
											{
												title : 'warning!'
											});
									}else if( $scope.saveObj.requestAllocationTypeId == 0 && $scope.saveObj.VendorId ==" "){
										growl.warning("Selected Equipment don't have vendor",
												{
													title : 'warning!'
												});
								}else if($scope.saveObj.resolutionDetails == ""){
									
									growl.warning("Please Enter Resolution Details",
											{
												title : 'warning!'
											});
									
								}else{
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveCorrectiveMaintenanceSchedule";

									GenericService.serviceAction("POST",URI,$scope.saveObj).then(function(response){
											
										if (response.data.status == 'success'){
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
															title : 'Success!'
													});
											 $timeout(function () { 
												 $state.go('correctiveMaintenanceScheduleWorklist');
											 }, 3000);
											
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
							}
							
							$scope.replaceAsset = function(){
									$scope.getSerialNumbers();
									$("#replaceModal").modal("show");
							}
							
							$scope.returnObj ={};
							$scope.returnAsset = function(){
								$scope.returnObj ={};
								if($scope.replaceDetailsList.length>0){
									$scope.returnObj.newSerialNo = $scope.replaceDetailsList[0].newSerialNo;
									$scope.returnObj.requestedSerialNo = $scope.replaceDetailsList[0].requestedSerialNo;
									$scope.returnObj.assetcategory = $scope.detailsObj.assetcategory;
									$scope.returnObj.assetCode = $scope.detailsObj.assetCode;
									$scope.returnObj.assetName = $scope.detailsObj.assetName;
									$scope.returnObj.correctiveMaintenanceScheduleId = $scope.detailsObj.correctiveMaintenanceScheduleId;
									$scope.returnObj.assetReplacementId = $scope.replaceDetailsList[0].assetReplacementId;
								}
							}
							
							
}]);
