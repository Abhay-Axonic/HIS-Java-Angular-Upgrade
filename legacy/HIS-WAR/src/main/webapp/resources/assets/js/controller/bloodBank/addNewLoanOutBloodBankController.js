angular
		.module('myApp')
		.controller(
				'addNewLoanOutBloodBankController',
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
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							
						
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
								
							
						
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};
							
							//search Donor 
							$scope.fDate="";
							$scope.tDate="";
				
							$scope.openExpiryDatepicker = function() {
								$scope.expiryDatepicker.opened = true;
							};

							$scope.expiryDatepicker = {
									opened : false
							};
				
							
							// function for initializa clockpicker
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}

							$scope.detailsObj = 
							{
								bloodBankObj:{
									bloodBankDesc:"",
									bloodBankId:""
								},
								bloodBagCapacityObj:{
									bloodBagCapacityId:"",
									bloodBagCapacityDesc:""
						 		},
								bloodBagTypeObj:{
									bloodBagTypeId:"",
									bloodBagTypeDesc:""
								},
								bloodGroupObj:{
									bloodGroupId:"",
									bloodGroup:""
								},
								componentObj: {
									bloodComponentId:"",
									bloodComponentDesc:""
								}
						
							};
							
							$scope.loanOutObj = {
									"bloodBankId":"",
									"bloodGroupId":"",
									"shortName":"",
									"expiryDate":"",
									"bloodUnit":1,
									"segmentNumber":"",
									"bloodBagTypeId":"",
									"bagCapacityId":"",
									"batchNo":"",
									"bloodBagNo":"",
									"stockStatusId":"",
									"createdBy":$scope.userId,
									"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"status":"A",
									"updatedBy":$scope.userId,
									"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"organizationId": $scope.organizationId,
									"unitId": $scope.unitId,
									"fromDate":"",
									"toDate":"",
									"bloodBagNumber":"",
									"bloodBankStockId": ""
							}
							
							$scope.bloodBankMasterList=[];
							var URI = BASE_URL + ROOT_URL +  "/bloodBank/getActiveBloodBankMaster/"+$scope.organizationId +"/" + $scope.unitId;
							GenericService.serviceAction("GET", URI,"").then(function(response) {
								$rootScope.stopLoader();
								$scope.bloodBankMasterList = response.data.listObject;
							});
							
							
							$scope.bloodGroupList = [];
							$scope.getAvailableBloodGroupList = function(bloodBankObj){
								$rootScope.startLoader();
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										bloodBankId:bloodBankObj.bloodBankId
										};
								var URI = BASE_URL + ROOT_URL+ "/api/bloodbank/stock//getAvailableBloodGroupByBankID"
;
									GenericService
									.serviceAction(METHOD_POST, URI , data )
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.bloodGroupList = response.data.listObject;
													console.log("bloodGroupList ",response.data.listObject);
												}
											});
							}
						//	$scope.getAvailableBloodGroupList();
							
							
							$scope.bloodBagCapacityMasterList=[];
							$scope.bloodBagTypeList = [];
							var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/getActiveBloodBagTypeMaster/"+$scope.organizationId +"/"+ $scope.unitId; 
							GenericService.serviceAction("GET",URI1,"").then(function(response){
								if(response.data.status == "success"){
								$rootScope.stopLoader();					
								$scope.bloodBagTypeList = response.data.listObject;
								console.log("bloodBagTypeList",$scope.bloodBagTypeList);
								}
							});
							
							var URI = BASE_URL + ROOT_URL + "/global/bloodBank/getActiveBloodBagCapacityMaster/"+$scope.organizationId +"/"+ $scope.unitId; 
							GenericService.serviceAction("GET",URI,"").then(function(response){
								if(response.data.status == "success"){
								$rootScope.stopLoader();					
								$scope.bloodBagCapacityMasterList = response.data.listObject;
								console.log("bloodBagCapacityMasterList",$scope.bloodBagCapacityMasterList);
								}
							});
							
							
							
							//get component list
							$scope.componentList = [];
							$scope.getComponentList = function(){
								try{
								
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/bloodBank/getActiveBloodComponentMaster"+"/"+ $scope.organizationId +"/"+ $scope.unitId;
									console.log("EDIT",URI);
									GenericService.serviceAction("GET",URI).then(function(response){
										console.log("Edit",response);
										
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.componentList = response.data.listObject;
											
											console.log("$scope.componentList",$scope.componentList);
											
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
							
							$scope.getComponentList();
							
							$scope.getexpiryDate=function(componentObj){
								
							 	var sdate = new Date();
							 	sdate.setDate(sdate.getDate() + componentObj.expiry);
							 	$scope.loanOutObj.expiryDate=sdate;
						}
							
							//get Blood Bag  list
							$scope.bloodBagList = [];
							$scope.getBloodBagList = function(name){
								try{
									$rootScope.startLoader();
									var data = {
											unitId:$scope.unitId,
											organizationId:$scope.organizationId,
											bloodBankId : $scope.detailsObj.bloodBankObj.bloodBankId,
											bloodComponentId:$scope.detailsObj.componentObj.bloodComponentId,
											bloodGroupId:$scope.detailsObj.bloodGroupObj.bloodGroupId,
											bloodBagCapacityId : $scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId,
											bloodBagTypeId : $scope.detailsObj.bloodBagTypeObj.bloodBagTypeId,
											currentDate:moment(new Date()).format('YYYY-MM-DD'),
											bloodBagNo:name
									};
									var URI = BASE_URL + ROOT_URL + "/api/bloodbank/stock/searchBagNoListByMultipleCriteria"
									return GenericService.serviceAction(METHOD_POST,URI,data).then(function(response){
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.bloodBagList = response.data.listObject;
											return $scope.bloodBagList;
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
							
							$scope.selectBloodBag = function(info, model,label) {
								$scope.loanOutObj.bloodBankStockId = info.bloodBankStockId;
								$scope.loanOutObj.shortName = info.shortName;
								$scope.loanOutObj.segmentNumber = info.segmentNumber;
								$scope.loanOutObj.batchNo = info.batchNo;
							}
							
							$scope.loanOutDetails=[];
							$scope.addLoanOutDetails = function(){
								if($scope.detailsObj.bloodBankObj.bloodBankId==undefined || $scope.detailsObj.bloodBankObj.bloodBankId==null || $scope.detailsObj.bloodBankObj.bloodBankId=="")
								{
									growl.warning("Please select Blood Bank",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.detailsObj.bloodGroupObj.bloodGroupId==undefined || $scope.detailsObj.bloodGroupObj.bloodGroupId==null || $scope.detailsObj.bloodGroupObj.bloodGroupId=="")
								{
									growl.warning("Please select Blood Group",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId==undefined || $scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId==null || $scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId==""){
									growl.warning("Please Select Blood Bag Capacity",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.detailsObj.bloodBagTypeObj.bloodBagTypeId==undefined || $scope.detailsObj.bloodBagTypeObj.bloodBagTypeId==null || $scope.detailsObj.bloodBagTypeObj.bloodBagTypeId==""){
									growl.warning("Please Select Blood Bag Type",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.detailsObj.componentObj.bloodComponentId==undefined || $scope.detailsObj.componentObj.bloodComponentId==null || $scope.detailsObj.componentObj.bloodComponentId==""){
									growl.warning("Please Select Component",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.loanOutObj.expiryDate==undefined || $scope.loanOutObj.expiryDate==null || $scope.loanOutObj.expiryDate==""){
									growl.warning("Please Enter expiry Date",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.loanOutObj.bloodBagNo==undefined || $scope.loanOutObj.bloodBagNo==null || $scope.loanOutObj.bloodBagNo==""){
									growl.warning("Please Enter BloodBagNo",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.loanOutObj.shortName==undefined || $scope.loanOutObj.shortName==null || $scope.loanOutObj.shortName==""){
									growl.warning("Please Enter shortName",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.loanOutObj.batchNo==undefined || $scope.loanOutObj.batchNo==null || $scope.loanOutObj.batchNo==""){
									growl.warning("Please Enter Batch No",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.loanOutObj.segmentNumber==undefined || $scope.loanOutObj.segmentNumber==null || $scope.loanOutObj.segmentNumber==""){
									growl.warning("Please Enter Segment Number",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.loanOutObj.bloodUnit==undefined || $scope.loanOutObj.bloodUnit==null || $scope.loanOutObj.bloodUnit==""){
									growl.warning("Please Enter Blood Unit",
											{
												title : 'warning!'
											});
									return false;
								}
								if($scope.loanOutDetails!=null && $scope.loanOutDetails.length>0){
									var isBagExists = false;
									angular.forEach($scope.loanOutDetails , function(value,index){
										if(value.bloodBankId == $scope.detailsObj.bloodBankObj.bloodBankId 
										&& value.bloodGroupId == $scope.detailsObj.bloodGroupObj.bloodGroupId 
										&& value.bloodBagTypeId == $scope.detailsObj.bloodBagTypeObj.bloodBagTypeId
										&& value.bloodBagCapacityId == $scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId
										&& value.bloodComponentId == $scope.detailsObj.componentObj.bloodComponentId
										&& value.bloodBagNo == $scope.loanOutObj.bloodBagNo){
											isBagExists = true;
										}
									});
									if(isBagExists){
										growl.warning("Bag Already Exists..",
												{
													title : 'warning!'
												});
										return false;
									}
								}
								
								var newObj={}
								newObj.bloodBankId=$scope.detailsObj.bloodBankObj.bloodBankId;
								newObj.bloodBankName=$scope.detailsObj.bloodBankObj.bloodBankDesc;
								newObj.bloodGroupId=$scope.detailsObj.bloodGroupObj.bloodGroupId;
								newObj.bloodGroup=$scope.detailsObj.bloodGroupObj.bloodGroup;
								newObj.shortName=$scope.loanOutObj.shortName;
								newObj.expiryDate=moment($scope.loanOutObj.expiryDate).format('DD-MM-YYYY HH:mm:ss');
								newObj.bloodUnit=$scope.loanOutObj.bloodUnit;
								newObj.segmentNumber=$scope.loanOutObj.segmentNumber;
								newObj.bloodBagTypeId=$scope.detailsObj.bloodBagTypeObj.bloodBagTypeId;
								newObj.bloodBagType=$scope.detailsObj.bloodBagTypeObj.bloodBagTypeDesc;
								newObj.bloodBagCapacityId=$scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId;
								newObj.bloodBagCapacity=$scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityDesc;
								newObj.batchNo=$scope.loanOutObj.batchNo;
								newObj.bloodBagNo=$scope.loanOutObj.bloodBagNo;
								newObj.isSelected=false;
								newObj.organizationId=$scope.loanOutObj.organizationId;
								newObj.unitId=$scope.loanOutObj.unitId;
								newObj.createdBy=$scope.loanOutObj.createdBy;
								newObj.updatedBy=$scope.loanOutObj.updatedBy;
								newObj.createdDate=$scope.loanOutObj.createdDate;
								newObj.updatedDate=$scope.loanOutObj.updatedDate;
								newObj.status=$scope.loanOutObj.status;
								newObj.bloodComponentId =$scope.detailsObj.componentObj.bloodComponentId;
								newObj.bloodComponentDesc =$scope.detailsObj.componentObj.bloodComponentDesc;
								newObj.bloodBankStockId = $scope.loanOutObj.bloodBankStockId ;
								$scope.loanOutDetails.push(newObj);
								
								$scope.loanOutObj.shortName="";
								$scope.loanOutObj.expiryDate="";
								$scope.loanOutObj.bloodUnit=1;
								$scope.loanOutObj.segmentNumber="";
								$scope.loanOutObj.batchNo="";
								$scope.loanOutObj.bloodBagNo="";
								$scope.detailsObj = {
										bloodBankObj : {bloodBankDesc:"",bloodBankId:""},
										bloodBagCapacityObj : {bloodBagCapacityId:"",bloodBagCapacityDesc:""},
										bloodBagTypeObj : {bloodBagTypeId:"",bloodBagTypeDesc:""},
										bloodGroupObj : {bloodGroupId:"",bloodGroup:""},
										componentObj : {bloodComponentId:"",bloodComponentDesc:""}
								};
								
							}
							
							$scope.removeLoanOutDetails = function(index){
					    		$scope.loanOutDetails.splice(index, 1);
					    	}
							$scope.selectAll = false;
							$scope.checkUncheckAll = function(){
								if($scope.loanOutDetails!=null && $scope.loanOutDetails.length>0){
									if($scope.selectAll){
										angular.forEach($scope.loanOutDetails,function(value,index){
											value.isSelected = true;
										});
									}else{
										angular.forEach($scope.loanOutDetails,function(value,index){
											value.isSelected = false;
										});
									}
								}	
							}
							
							$scope.checkUncheck = function(){
								var cnt = 0;
								angular.forEach($scope.loanOutDetails,function(value,index){
									if(value.isSelected){
										cnt = cnt+1;
									}
								});
								if(cnt == $scope.loanOutDetails.length){
									$scope.selectAll = true;
								}else{
									$scope.selectAll = false;
								}
							}
							
							$scope.finalLoanOutList = [];
							$scope.saveLoanOutDetails = function(){
								try{
									$rootScope.startLoader();
									$scope.finalLoanOutList = [];
									
									var loanOutObj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											createdBy  :$scope.userId,
											updatedBy : $scope.userId
									};
									
									if($scope.loanOutDetails!=null && $scope.loanOutDetails.length>0){
										
										var foundIsSelected = $scope.loanOutDetails.find(ar=>ar.isSelected);
						                if(foundIsSelected == undefined)
						                    {
						                    growl.warning("Please Select Blood Bag for save !!",
						                             {
						                                 title : 'Warning!'
						                             });
						                     $rootScope.stopLoader();
						                     return false;
						                    }	
											
										angular.forEach($scope.loanOutDetails,function(value,key){
											if(value.isSelected){
												var newArr = {};
												newArr.bloodBankId = value.bloodBankId;
												newArr.shortName = value.shortName;
												newArr.bloodBagNo = value.bloodBagNo;
												newArr.segmentNo = value.segmentNumber;
												newArr.batchNo = value.batchNo;
												newArr.bloodGroupId = value.bloodGroupId;
												newArr.bagCapacityId = value.bloodBagCapacityId;
												newArr.bagTypeId = value.bloodBagTypeId;
												newArr.bagUnitNo = 1; 
												newArr.expiryDate = value.expiryDate;
												newArr.createdBy = value.createdBy;
												newArr.createdDate = value.createdDate;
												newArr.status = value.status;
												newArr.updatedBy = value.updatedBy;
												newArr.updatedDate = value.updatedDate;
												newArr.unitId = value.unitId;
												newArr.organizationId = value.organizationId;
												newArr.loanOutStatusId = 16;// loan out pending
												newArr.componentId =value.bloodComponentId;
												newArr.bloodBankStockId = value.bloodBankStockId ;
												$scope.finalLoanOutList.push(newArr);
											}
										});
										 loanOutObj.bloodBagLoanOutDetailsList  = $scope.finalLoanOutList
									}else{
										flag =true;
										growl.warning("Please select loan In",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									var URI = BASE_URL + ROOT_URL+ "/api/bloodbag/loanOut/saveLoanOut";
									GenericService.serviceAction(METHOD_POST, URI ,loanOutObj).then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.finalLoanOutList = [];
													$scope.loanOutDetails = [];
													$scope.loanOutObj.shortName="";
													$scope.loanOutObj.expiryDate="";
													$scope.loanOutObj.bloodUnit=1;
													$scope.loanOutObj.segmentNumber="";
													$scope.loanOutObj.batchNo="";
													$scope.loanOutObj.bloodBagNo="";
													$scope.detailsObj = {
														bloodBankObj : {bloodBankDesc:"",bloodBankId:""},
														bloodBagCapacityObj : {bloodBagCapacityId:"",bloodBagCapacityDesc:""},
														bloodBagTypeObj : {bloodBagTypeId:"",bloodBagTypeDesc:""},
														bloodGroupObj : {bloodGroupId:"",bloodGroup:""},
														componentObj : {bloodComponentId:"",bloodComponentDesc:""}
													};
													$scope.selectAll = false;
													growl.success(response.data.message,
															{
																title : 'success!'
															});
													$rootScope.stopLoader();
													$state.go('loanOutBloodBank');
												}else if (response.data.status == "Already Exists.") {
													growl.error(response.data.message,
															{
																title : 'error!'
															});
													$rootScope.stopLoader();
												}
												else if (response.data.status == "error") {
													$rootScope.stopLoader();
													$scope.finalLoanOutList = [];
													$scope.loanOutDetails = [];
													$scope.loanOutObj.shortName="";
													$scope.loanOutObj.expiryDate="";
													$scope.loanOutObj.bloodUnit="";
													$scope.loanOutObj.segmentNumber="";
													$scope.loanOutObj.batchNo="";
													$scope.loanOutObj.bloodBagNo="";
													$scope.detailsObj = {
															bloodBankObj : {bloodBankDesc:"",bloodBankId:""},
															bloodBagCapacityObj : {bloodBagCapacityId:"",bloodBagCapacityDesc:""},
															bloodBagTypeObj : {bloodBagTypeId:"",bloodBagTypeDesc:""},
															bloodGroupObj : {bloodGroupId:"",bloodGroup:""},
															componentObj : {bloodComponentId:"",bloodComponentDesc:""}
													};
													growl.error(response.data.message,
															{
																title : 'error!'
															});
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
								
							}
						
						
			}]);
							