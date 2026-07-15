angular
		.module('myApp')
		.controller(
				'addNewLoanInBloodBankController',
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
							$scope.searchKeyword = "";
								
							
						
							
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
									bloodGroupDesc:""
								},
								componentObj:{
									bloodComponentId:"",
									bloodComponentDesc:""
								}
						
							};
							
							$scope.loanInObj = {
									"bloodBankId":"",
									"bloodGroupId":"",
									"shortName":"",
									"expiryDate":"",
									"bloodUnit":"",
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
									"tTDCheck":false
							}
							var data = {
									organizationId:$scope.organizationId,
									unitId:$scope.unitId,
									
									
									};
							
							
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
							
							$scope.bloodBankMasterList=[];
							var URI = BASE_URL + ROOT_URL +  "/bloodBank/getActiveBloodBankMaster/"+$scope.organizationId +"/" + $scope.unitId;
							GenericService.serviceAction("GET", URI,"").then(function(response) {
								$rootScope.stopLoader();
								$scope.bloodBankMasterList = response.data.listObject;
								console.log("bloodBankMasterList",$scope.bloodBankMasterList);
							});
							
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
							
							
							$scope.bloodGroupList = [];
							$scope.getBloodGroupList = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getActiveBloodGroupMaster/"+$scope.organizationId +"/"+ $scope.unitId;
									GenericService
									.serviceAction(METHOD_GET, URI )
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.bloodGroupList = response.data.listObject;
													console.log("bloodGroupList ",response.data.listObject);
												}
											});
							}
							$scope.getBloodGroupList();
							
							$scope.getexpiryDate=function(componentObj){
								
								 	var sdate = new Date();
								 	sdate.setDate(sdate.getDate() + componentObj.expiry);
								 	$scope.loanInObj.expiryDate=sdate;
							}
							
							$scope.loanInList=[];
							$scope.addBloodBagList = function(){
								if($scope.detailsObj.bloodBankObj.bloodBankId=="")
								{
									growl.warning("Please select Blood Bank",
											{
												title : 'warning!'
											});
								}else if($scope.loanInObj.shortName==""){
									growl.warning("Please Enter shortName",
											{
												title : 'warning!'
											});
								}else if($scope.loanInObj.bloodBagNo==""){
									growl.warning("Please Enter BloodBagNo",
											{
												title : 'warning!'
											});
								}else if($scope.loanInObj.expiryDate==""){
									growl.warning("Please Enter expiry Date",
											{
												title : 'warning!'
											});
								}else if($scope.loanInObj.segmentNumber==""){
									growl.warning("Please Enter Segment Number",
											{
												title : 'warning!'
											});
								}else if($scope.loanInObj.batchNo==""){
									growl.warning("Please Enter Batch No",
											{
												title : 'warning!'
											});
								}else if($scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId==""){
									growl.warning("Please Enter Blood Bag Capacity",
											{
												title : 'warning!'
											});
								}else if($scope.detailsObj.bloodBagTypeObj.bloodBagTypeId==""){
									growl.warning("Please Enter Blood Bag Type",
											{
												title : 'warning!'
											});
								}else{
									
									if($scope.loanInList.length>0)
									{
										$scope.flag=false; 
										
										angular.forEach($scope.loanInList,function(value,key){
											
											if(value.bloodBagNo==$scope.loanInObj.bloodBagNo)
											{
												$scope.flag =true;
											}
										});
										if(!$scope.flag){
										var newObj={}
										newObj.bloodBankId=$scope.detailsObj.bloodBankObj.bloodBankId;
										newObj.bloodBankDesc=$scope.detailsObj.bloodBankObj.bloodBankDesc;
										newObj.bloodGroupId=$scope.detailsObj.bloodGroupObj.bloodGroupId;
										newObj.bloodGroupDesc=$scope.detailsObj.bloodGroupObj.bloodGroupDesc;
										newObj.shortName=$scope.loanInObj.shortName;
										newObj.expiryDate=moment($scope.loanInObj.expiryDate).format('DD-MM-YYYY HH:mm:ss');
										//newObj.bloodUnit=$scope.loanInObj.bloodUnit;
										newObj.segmentNumber=$scope.loanInObj.segmentNumber;
										newObj.bloodBagTypeId=$scope.detailsObj.bloodBagTypeObj.bloodBagTypeId;
										newObj.bloodBagTypeDesc=$scope.detailsObj.bloodBagTypeObj.bloodBagTypeDesc;
										newObj.bloodBagCapacityId=$scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId;
										newObj.bloodBagCapacityDesc=$scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityDesc;
										newObj.batchNo=$scope.loanInObj.batchNo;
										newObj.bloodBagNo=$scope.loanInObj.bloodBagNo;
										newObj.isSelected=false;
										newObj.organizationId=$scope.loanInObj.organizationId;
										newObj.unitId=$scope.loanInObj.unitId;
										newObj.createdBy=$scope.loanInObj.createdBy;
										newObj.updatedBy=$scope.loanInObj.updatedBy;
										newObj.createdDate=$scope.loanInObj.createdDate;
										newObj.updatedDate=$scope.loanInObj.updatedDate;
										newObj.status=$scope.loanInObj.status;
										newObj.tTDCheck=$scope.loanInObj.tTDCheck;
										newObj.bloodComponentId=$scope.detailsObj.componentObj.bloodComponentId;
										
										$scope.loanInList.push(newObj);
										
										$scope.loanInObj.TTDCheck=false;
										$scope.loanInObj.shortName="";
										$scope.loanInObj.expiryDate="";
										$scope.loanInObj.bloodUnit="";
										$scope.loanInObj.segmentNumber="";
										$scope.loanInObj.batchNo="";
										$scope.loanInObj.bloodBagNo="";
										$scope.detailsObj={};
										}
										else{
											growl.warning("Please Enter different Blood Bag Number",
													{
														title : 'warning!'
													});
										}
									}else{
								var newObj={}
								newObj.bloodBankId=$scope.detailsObj.bloodBankObj.bloodBankId;
								newObj.bloodBankDesc=$scope.detailsObj.bloodBankObj.bloodBankDesc;
								newObj.bloodGroupId=$scope.detailsObj.bloodGroupObj.bloodGroupId;
								newObj.bloodGroupDesc=$scope.detailsObj.bloodGroupObj.bloodGroupDesc;
								newObj.shortName=$scope.loanInObj.shortName;
								newObj.expiryDate=moment($scope.loanInObj.expiryDate).format('DD-MM-YYYY HH:mm:ss');
								//newObj.bloodUnit=$scope.loanInObj.bloodUnit;
								newObj.segmentNumber=$scope.loanInObj.segmentNumber;
								newObj.bloodBagTypeId=$scope.detailsObj.bloodBagTypeObj.bloodBagTypeId;
								newObj.bloodBagTypeDesc=$scope.detailsObj.bloodBagTypeObj.bloodBagTypeDesc;
								newObj.bloodBagCapacityId=$scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityId;
								newObj.bloodBagCapacityDesc=$scope.detailsObj.bloodBagCapacityObj.bloodBagCapacityDesc;
								newObj.batchNo=$scope.loanInObj.batchNo;
								newObj.bloodBagNo=$scope.loanInObj.bloodBagNo;
								newObj.isSelected=false;
								newObj.organizationId=$scope.loanInObj.organizationId;
								newObj.unitId=$scope.loanInObj.unitId;
								newObj.createdBy=$scope.loanInObj.createdBy;
								newObj.updatedBy=$scope.loanInObj.updatedBy;
								newObj.createdDate=$scope.loanInObj.createdDate;
								newObj.updatedDate=$scope.loanInObj.updatedDate;
								newObj.status=$scope.loanInObj.status;
								newObj.tTDCheck=$scope.loanInObj.tTDCheck;
								newObj.bloodComponentId=$scope.detailsObj.componentObj.bloodComponentId;
								
								$scope.loanInList.push(newObj);
								$scope.loanInObj.TTDCheck=false;
								$scope.loanInObj.shortName="";
								$scope.loanInObj.expiryDate="";
								$scope.loanInObj.bloodUnit="";
								$scope.loanInObj.segmentNumber="";
								$scope.loanInObj.batchNo="";
								$scope.loanInObj.bloodBagNo="";
								$scope.detailsObj={};
								console.log("$scope.loanInList",$scope.loanInList);
								}
								}
							}
							
							
							$scope.changeStatus = function(index ,sts)
							{
								$scope.loanInList[index].isSelected = sts;
							}
							
							  $scope.removeLoanInDetails = function(index){
						    		$scope.loanInList.splice(index, 1);
						    	}
						        
							  
							//save loan In Details
							
						$scope.saveLoanInDetails = function(){
							try{
								$rootScope.startLoader();
								$scope.finalLoanInList = [];
								
								var flag=false;
								angular.forEach($scope.loanInList,function(value,key){
								
									if(value.isSelected){
										var newArr = {};
										newArr.bloodBankId = value.bloodBankId;
										newArr.shortName = value.shortName;
										newArr.bloodBagNo = value.bloodBagNo;
										newArr.segmentNumber = value.segmentNumber;
										newArr.batchNo = value.batchNo;
										newArr.bloodGroupId = value.bloodGroupId;
										newArr.bloodBagCapacityId = value.bloodBagCapacityId;
										newArr.bloodBagTypeId = value.bloodBagTypeId;
										//newArr.bloodUnit = value.bloodUnit;
										newArr.bloodUnit = 1; 
										newArr.expiryDate = value.expiryDate;
										newArr.createdBy = value.createdBy;
										newArr.createdDate = value.createdDate;
										newArr.status = value.status;
										newArr.updatedBy = value.updatedBy;
										newArr.updatedDate = value.updatedDate;
										newArr.unitId = value.unitId;
										newArr.organizationId = value.organizationId;
										newArr.stockStatusId = 4;// In Stock
										newArr.bloodComponentId =value.bloodComponentId;
										newArr.tTDCheck =value.tTDCheck;
										$scope.finalLoanInList.push(newArr);
									}
								});
								if($scope.finalLoanInList.length>0){
									console.log("$scope.loanInList",$scope.finalLoanInList);
									$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/api/bloodbank/stock/saveBloodBankStock";
								GenericService.serviceAction(METHOD_POST, URI ,$scope.finalLoanInList).then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.finalLoanInList = [];
												$scope.loanInList = [];
												$scope.loanInObj = {};
												$scope.detailsObj={};
												growl.success(response.data.message,
														{
															title : 'success!'
														});
												$rootScope.stopLoader();
											}else if (response.data.status == "Already Exists.") {
												growl.error(response.data.message,
														{
															title : 'error!'
														});
												$rootScope.stopLoader();
											}
											else if (response.data.status == "error") {
												$rootScope.stopLoader();
												$scope.finalLoanInList = [];
												$scope.loanInList = [];
												$scope.loanInObj = {};
												$scope.detailsObj={};
												growl.error(response.data.message,
														{
															title : 'error!'
														});
											}
											
										});
								
							//	$rootScope.stopLoader();
								}
								else{
									flag =true;
									growl.warning("Please select loan In",
											{
												title : 'warning!'
											});
									$rootScope.stopLoader();
								}
							}catch(e){
								console.log("Exception-",e);
							}
							
						}
						
						
						$scope.loanInObj.toDate=new Date();
						$scope.loanInObj.fromDate=new Date();
						
						$scope.searchLoanIn = function(){
							
							
							if(($scope.loanInObj.fromDate=="") && ($scope.loanInObj.toDate=="")){
								$scope.fDate=$scope.loanInObj.fromDate;
								$scope.tDate=$scope.loanInObj.toDate;
							}
							else
								{
								$scope.fDate = moment($scope.loanInObj.fromDate).format('YYYY-MM-DD');
								$scope.tDate = moment($scope.loanInObj.toDate).format('YYYY-MM-DD');
								}
							var data ={
									unitId:$scope.unitId,
									organizationId:$scope.organizationId,
									fromDate:$scope.fDate,
									toDate:$scope.tDate	
							};
							$scope.loanInList1=[];
							$rootScope.startLoader();
							/*var URI1 = BASE_URL + ROOT_URL + "/api/bloodbank/stock/searchBloodBankStock"; */
							var URI1 = BASE_URL + ROOT_URL + "/api/bloodbank/stock/searchLoanInData"; 
							GenericService.serviceAction("POST",URI1,data).then(function(response){
								if(response.data.status == "success"){
								$rootScope.stopLoader();					
								$scope.loanInList1 = response.data.listObject;
								console.log("bloodBagTypeList",$scope.loanInList1);
								}
							});

						}
						$scope.searchLoanIn();
						
						$scope.LoanInList =[];
						$scope.setDetails = function(info){
							
							var data ={
									unitId:$scope.unitId,
									organizationId:$scope.organizationId,
									loanNumber:info.loanInNumber,
									loanInId:info.loanInId
							};
							$scope.loanInList1=[];
							$rootScope.startLoader();
							/*var URI1 = BASE_URL + ROOT_URL + "/api/bloodbank/stock/searchBloodBankStock"; */
							var URI1 = BASE_URL + ROOT_URL + "/api/bloodbank/stock/searchBloodBankStock"; 
							GenericService.serviceAction("POST",URI1,data).then(function(response){
								if(response.data.status == "success"){
								$rootScope.stopLoader();					
								$scope.LoanInList = response.data.listObject;
								console.log("LoanInList",$scope.LoanInList);
								}
							});

							
						}
						
						
						$scope.updateStatus = function(id,status){
							
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								
								var data ={
										updatedBy:$scope.userId,
										updatedDate:$scope.dt,
										status:status,
										bloodBankStockId:id
								};
								
								$rootScope.startLoader();
								var URI5 = BASE_URL + ROOT_URL +"/api/bloodbank/stock/updateBloodBankStockStatus";
								console.log("update status",data);
								GenericService.serviceAction("POST",URI5,data).then(function(response){
									if (response.data.status == 'success')
									{
										$scope.searchLoanIn();
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										
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
						
						
			}]);
							