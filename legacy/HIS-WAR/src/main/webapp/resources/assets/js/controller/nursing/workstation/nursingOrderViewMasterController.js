/**
 *  @Author By  Sneha D
 */

angular.module('myApp')
		.controller("nursingOrderViewMasterController",
		['$scope','$rootScope','$state','$timeout','$cookies','GenericService','BillingGenericService','PagerService','promiseFactory','$sessionStorage','growl','reportService',
		function($scope,$rootScope,$state,$timeout,$cookies, GenericService,BillingGenericService,PagerService,promiseFactory,$sessionStorage,growl,reportService){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.billing = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$scope.masterObj = {};
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$scope.patientCategoryId = cookieObjectAppConfig.patientCategoryId;

			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
			$scope.openDatePicker = function() {
				$scope.datepicker.opened = true;
			};
			
			$scope.openDatePickerPatiSearch = function() {
				$scope.datepicker1.opened = true;
				};

				$scope.datepicker1 = {
				opened : false
				};
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup
			
			//For Get Active Gender List
			$scope.getSexMasterList = function() {
				var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
				BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.sexMasterList = response.data.listObject;
				});
			}
			
			$scope.getIdentificationTypeList = function(){
				var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
				BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						$scope.identificationTypeList = response.data.listObject;
				});
			}
			
		
			$scope.getCardTypesList = function(){
				var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
				var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.cardTypeList = response.data.listObject;
					}
				});
			}
			
			$scope.initPaymentDetailsObj = function(){
				$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:""});
			}
	   
			
			$scope.patientSearchObj = {
				      "uhIdNumber"      : "",
				      "patientName"     : "",
				      "genderId"        : "",
				      "mobileNo"        : "",
				      "birthDate"       : "",
				      "identificationNo":"",
				      "patientType"   :"1",
				      "unitId"          :$scope.unitId,
				      "organizationId"  :$scope.organizationId
				    }
			
			$scope.searchPatient = function()
			{
				$rootScope.startLoader();
				$scope.DischargeSummaryFinalList=[];
				$scope.patientTemplateList=[];
				if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
				{
					growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
					$scope.patientList = [];
					$rootScope.stopLoader();

					return false;
				}	
				var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
				var patSearchObj = angular.copy($scope.patientSearchObj);
				if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
					var dt = new Date($scope.patientSearchObj.birthDate);
					patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" +  dt.getFullYear();
				} 
				BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.searchPatientList = [];
					if (response.data.status == "success")
						{
						$scope.patientList = response.data.listObject;
						patSearchObj = {};
						
						$scope.initPaymentDetailsObj();
						$scope.listPaymentDetails = [];
						$scope.patientDetailsList = [];
						$scope.patientDetails = {};
						$scope.patientSearchObj.birthDate="";
						$rootScope.stopLoader();
						}
					else if(response.data.listObject==null){
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$scope.patientSearchObj.birthDate="";
						$scope.patientList=[];
						return false;
					}
					else{
						$rootScope.stopLoader();
					}
					
				});
			}
			
			
			$scope.patientId=null;
			$scope.getPatientDetailsById = function(patientId){
				$scope.patientId=patientId;
				$scope.patientDetailsList = [];
				$scope.patientDetails = {};
				$scope.masterObj = {};
				$scope.DischargeSummaryFinalList=[];
				$scope.patientTemplateList=[];
				var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.patientDetailsList = response.data.listObject;
						$scope.patientDetails = response.data.listObject[0];
						
						$scope.patientId=$scope.patientDetailsList[0].patientId;
						
						$scope.getNursingOrdrPatientList($scope.patientId);
						console.log("patientId",$scope.patientId);
						console.log("PatientDetailList:",$scope.patientDetailsList);
						console.log("PatientDetail:",$scope.patientDetails);
					}
				});
				
			
			}
			
			//for date
			$scope.getFormattedDate = function(date,isTime){
				var tempDate = new Date(date);
				var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
				var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
				var year = tempDate.getFullYear();
				if(isTime)
					return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+getSeconds();
				else
					return date + "-" + month + "-" + year + " 00:00:00";
			}
			
			

			$scope.init = function(){
				
				$scope.identificationTypeList = [];
				$scope.sexMasterList = [];
				$scope.getSexMasterList();
				$scope.getIdentificationTypeList();
				$scope.patientDetailsList = [];
				$scope.patientDetails = {}
				$scope.billing = true;
				$scope.deposit = {};
				$scope.paymentDetails = {};
				$scope.initPaymentDetailsObj();
				$scope.listPaymentDetails = [];
				$scope.getCardTypesList();
				$scope.dueAmount = parseFloat(0).toFixed(2);
				$scope.visitNoList = [];
			}
			$scope.init();
			$scope.tempPatientlist = [];
			$scope.getNursingOrdrPatientList = function() {
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL+ "/api/opd/getListOfPatientsOfNursingOrders";
				var data={
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						patientId:$scope.patientId
				
				};
				GenericService.serviceAction("POST", URI, data).then(function(response) {
							if (response.data.status == "success") {
								$rootScope.stopLoader();
								$scope.getPatientlist = [];
								$scope.tempPatientlist = [];
								$scope.getPatientlist = response.data.listObject;
								$scope.tempPatientlist = angular.copy(response.data.listObject);
								console.log("getPatientlist",$scope.getPatientlist);
							}
						});
			}
			$scope.getNursingOrdrPatientList();
			$scope.nursingOrederedListByPatientId = [];
			 $scope.getListOfNursingOrderMapper = function(patientId) {
					var URI = BASE_URL + ROOT_URL+ "/api/opd/getNursingOrderMapperList";
					var data = {
							             organizationId : $scope.organizationId,
							             unitId:$scope.unitId,
							             patientId:patientId
                             }
					GenericService.serviceAction("POST", URI, data).then(function(response) {

								$scope.nursingOrederedListByPatientId = [];
								$scope.nursingOrederedListByPatientId = response.data.listObject;
                              console.log("nursingOrederedListByPatientId",$scope.nursingOrederedListByPatientId);
						
					var currentDt  = moment(new Date()).format('YYYY-MM-DD'); 
					angular.forEach($scope.nursingOrederedListByPatientId,function(value,index){
						var endDate =  moment(moment(value.stopDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
						if(currentDt > endDate){
							value.disable = true;
						}
						
					});
					
					});
		        }
			
			$scope.currentIdx = '';
			 $scope.oldStatus = '';
			 $scope.oldRemark = '';
			 $scope.updateObject=function(id,index) {
				 $scope.remark = '';
				 $scope.currentIdx =  index;
				 if($scope.getPatientlist[index].remark != "" && $scope.getPatientlist[index].remark != null &&$scope.getPatientlist[index].remark != "null") {
				  $scope.remark=$scope.getPatientlist[index].remark;
				 }
				//  $scope.nursingOrderMapperId=id;
				  $scope.nurseStatus=$scope.getPatientlist[index].nurseStatus;
				  $scope.patientIdforNur = id;

				  $scope.oldStatus = $scope.tempPatientlist[index].nurseStatus;
					  $scope.oldRemark = $scope.tempPatientlist[index].remark;
			 }
			 $scope.updateNursingOrderStatus = function() {
				 $rootScope.startLoader();
				  
				      var data = {
							  //  "nursingOrderMapperId":$scope.nursingOrderMapperId,
								"remark":$scope.remark,
								"nurseStatus":$scope.nurseStatus,
								"updatedBy":$scope.userId,
								"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								"patientId":$scope.patientIdforNur
						 }
				     
					var URI = BASE_URL + ROOT_URL+ "/api/opd/updateNursingOrdersStatusByNurse";
					
					GenericService.serviceAction("POST", URI, data).then(function(response) {
						if (response.data.status == "success") {
							growl.success(
									response.data.message,
									{
										title : 'Success!'
									});
							
							$scope.getNursingOrdrPatientList();
							$scope.init();
							
							$rootScope.stopLoader();
							
					  	}
						
							});
		        }
			 
			 
			 $scope.stop=function(index,flag,info){
				 
				 $scope.showUpdateNursingOrderMapper (index,flag,info)
				 
			 }
			 $scope.showUpdateNursingOrderMapper = function(index,flag,info) {
				 $rootScope.startLoader();
				 $scope.patientId=info.patientId;
				      var data = {
				    		  
								"checkStatus":flag,
								"updatedBy":$scope.userId,
								"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								"patientId":info.patientId,
								"nursingOrderMapperId":info.nursingOrderMapperId
						 }
				     
					var URI = BASE_URL + ROOT_URL+ "/api/opd/updateNursingOrdersStartOrStop";
					
					GenericService.serviceAction("POST", URI, data).then(function(response) {
//						$('#acceptForPopupPending').modal('hide');
//						$('#acceptForPopupComplete').modal('hide');
						if (response.data.status == "success") {
							growl.success(
									response.data.message,
									{
										title : 'Success!'
									});
							
							
							 $scope.getListOfNursingOrderMapper( $scope.patientId);
							$scope.init();
							
							$rootScope.stopLoader();
							
					  	}
						
							});
		        }
			 $scope.cancelNursingOrder = function(){
				 $scope.getPatientlist[$scope.currentIdx].nurseStatus = $scope.oldStatus;
				 $scope.getPatientlist[$scope.currentIdx].remark = $scope.oldRemark ;
			 }
			
}]);