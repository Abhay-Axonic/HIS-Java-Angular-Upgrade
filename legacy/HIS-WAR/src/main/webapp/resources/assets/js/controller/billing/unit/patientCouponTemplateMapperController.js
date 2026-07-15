
angular.module('myApp')
		.controller("patientCouponTemplateMapperController",
		['$scope','$rootScope','$state','$cookies','GenericService','BillingGenericService','PagerService','promiseFactory','$sessionStorage','growl','reportService','$location','GenericServiceFileUpload',
		function($scope,$rootScope,$state, $cookies, GenericService,BillingGenericService,PagerService,promiseFactory,$sessionStorage,growl,reportService,$location,GenericServiceFileUpload){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			
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
			$scope.couponTemplateDetailsList = [];
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			
			$scope.couponTemplateMasterId = null;
			$scope.expireDays=null;
			$scope.checkDate= function()
			{
				if($scope.patientSearchObj.birthDate == undefined || $scope.patientSearchObj.birthDate == null || $scope.patientSearchObj.birthDate == ''){
					   growl.warning("Date is invalid",{
							title : "warning"
						});
						$scope.patientSearchObj.birthDate = new Date();
					
					   return false;
				}
			
			}
			
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
			
			$scope.getBankList = function(){
				var URI = BASE_URL + ROOT_URL + "/api/billing/bankMaster";
				var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.bankList = response.data.listObject;
					}
				});
			}
			
			//ADDED LATER GANESH
			$scope.getHospitalBankList = function()
			{
				var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+$scope.organizationId+"/"+$scope.unitId;
				BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			       {
					if (response.data.status == "success")
					{
						$scope.hospitalBankList = response.data.listObject;
					}
				  });
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
			$scope.loadMoreData = function()
	        {
	            $scope.offset+=200;
	            $scope.loadMoreFlag=true;
	            $scope.searchPatient($scope.patientSearchObj);
	        }
	        $scope.offset=0;
	        $scope.loadMoreFlag=false;
	        $scope.noOfRecordsPerPage1=200;
	        $scope.patientList = [];
			$scope.searchPatient = function()
			{
				$rootScope.startLoader();
				if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
				{
					growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
					$scope.patientList = [];
					$rootScope.stopLoader();
					return false;
				}	
				var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
				if($scope.loadMoreFlag==true)
	            {
	            	$scope.patientSearchObj.offset=$scope.offset;
	            $scope.loadMoreFlag=false;
	            }
	        else
	            {
	        	$scope.patientSearchObj.offset=0;
	            $scope.loadMoreFlag=false;
	            $scope.patientList = [];
	            }
	            $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage1;
				var patSearchObj = angular.copy($scope.patientSearchObj);
				if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
					var dt = new Date($scope.patientSearchObj.birthDate);
					patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
				} 
				BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.searchPatientList = [];
					if (response.data.status == "success")
						{
						angular.forEach(response.data.listObject,function(value,index)
								{
							$scope.patientList.push(value);
								})
						patSearchObj = {};
					
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
			
		
			$scope.getPatientDetailsById = function(patientId){
				$scope.patientDetailsList = [];
				$scope.patientDetails = {}
				$scope.patientCouponMapperDetailsList = [];
				$scope.couponTemplateDetailsList = [];
				$scope.expireDays = '';
				$scope.couponTemplateMasterId = null;
				$scope.setFirstTabLevel(1);
				var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.patientDetailsList = response.data.listObject;
						$scope.patientDetails = response.data.listObject[0];
						$scope.patientDetails.patientName = $scope.patientDetails.prefixCode+" "+$scope.patientDetails.firstName+" "+$scope.patientDetails.middleName+" "+$scope.patientDetails.lastName;
						$scope.patientDetails.age = $scope.patientDetails.ageString;
						$scope.patientDetails.patientId = patientId;
						$scope.patientId = patientId;
					
					}
				});
			}
			
			
			
			
			$scope.getCouponTemplateMasterList = function()
			{
					 var data={

								"organizationId":$scope.organizationId,
								"unitId":$scope.unitId
					 }
					
				var URI = BASE_URL + ROOT_URL +"/api/billing/coupon/getCouponTemplateMaster";
			GenericService
				.serviceAction("POST", URI,data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							if (response.data.status == "success"){
								$scope.couponList = [];
								 angular.forEach( response.data.listObject,function(value,index) {
									 if(value.status == 'A'){
										 $scope.couponList.push(value);
									 }
								 });
								
								
								
								
							}
							
						});
				
			}
			
			$scope.getCouponTemplateMasterList();
			
			$scope.couponTemplateDescription = '';
			$scope.getExpiryDays = function(couponTemplateMasterId){
				
				var coupon = $scope.couponList.filter(function(item) {
					  return item.couponTemplateMasterId === couponTemplateMasterId;
					})[0];
				$scope.expireDays = coupon.expireDays;
				
			}
			
			$scope.orderDetailsId = null;
			$scope.addCouponDetailsToList = function() {
				
				if($scope.patientDetails.patientId == undefined || $scope.patientDetails.patientId == null || $scope.patientDetails.patientId == ''){
					 growl.warning("Please select patient",
								{
									title : 'Warning!'
								});
						    return false;
					
				}
				
				 var data={
							"couponTemplateMasterId":$scope.couponTemplateMasterId ,
							"screenId":1,
							"patientId":$scope.patientDetails.patientId,
				 }
				
			var URI = BASE_URL + ROOT_URL +"/api/billing/coupon/getCouponTemplateDetails";
				 GenericService .serviceAction("POST", URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(
					function(response) {
						if (response.data.status == "success"){
							$scope.orderDetailsId = null;
							$scope.couponTemplateDetailsList  = response.data.object.couponTemplateDetailsDtoList;
							$scope.couponTemplateDescription = response.data.object.couponTemplateDescription;
							$scope.orderDetailsId = response.data.object.orderDetailsId;
							 angular.forEach($scope.couponTemplateDetailsList,function(value,index) {
								 value.consumption = 'N';
							 });
							
						}else{
							growl.error(response.data.message,{
								title : ERROR_MSG
							});
						}
						
					});
				
			}
			
			$scope.specialityList=[];
			$scope.subSpecialityList=[];
			$scope.groupDetailList=[];
			$scope.groupDetailsObj = {
					specialityId: '',
					subSpecialityId: ''
				};
			
			$scope.serviceDetailsObj = {
					specialityId: '',
					subSpecialityId: '',
					markupStatus: '',
					specialityName: '',
					subSpecialityName: '',
					percentage: '',
					serviceId: '',
					consumption : 'N'	
				};
			$scope.specialityList= [];
			$scope.serviceDetailList=[];
			$scope.serviceListBySpeciality=[];
			$scope.getSpecialityList = function () {
				var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
				var obj = { organizationId: $scope.organizationId, unitId: $scope.unitId };
				GenericService.serviceAction(METHOD_POST, URI, obj).then(function (response) {
					if (response.data.status == "success")
						$scope.specialityList = response.data.listObject;
				});
			}
			$scope.saveBtnFlag = true;
			$scope.getSpecialityList();
			
			
			$scope.searchServices = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword+"---"+0;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
				
				return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
			
		    $scope.addServiceToTempServiceList = function($item, $model, $label)
		    {
		    	$scope.selectedServiceId = $item.serviceMasterId;
		    	$scope.selectedServiceName = $item.serviceStandardName +'/' + $item.serviceStandardCode;
		    	
		    	$scope.groupDetailsObj.specialityName = $item.specialityName;
				$scope.groupDetailsObj.specialityId = $item.specialityId; 
				
				$scope.groupDetailsObj.subSpecialityName = $item.subSpecialityName;
				$scope.groupDetailsObj.subSpecialityId = $item.subSpecialityId; 
				
			}
		    
		  
			
			var isGroupExists;
			function checkGroupExistsOrNot(arr, val) {
				return arr
						.some(function(arr) {
							if(val.specialityId == parseInt(arr.specialityId) && (arr.subSpecialityId == '' || arr.subSpecialityId == null) && (arr.serviceId == '' || arr.serviceId == null || arr.serviceId == undefined))
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
			
			
			var isGroupSubgroupExists;
			function checkGroupSubGroupExistsOrNot(arr, val) {
				return arr
						.some(function(arr) {
							if(val.specialityId == parseInt(arr.specialityId) && val.subSpecialityId == parseInt(arr.subSpecialityId)  && (arr.serviceId == '' || arr.serviceId == null || arr.serviceId == undefined))
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
			
			
				var isServiceExists;
			function checkSerivceExistsOrNot(arr, val) {
				return arr.some(function(arr) {
							if(val.specialityId === parseInt(arr.specialityId) && val.subSpecialityId === parseInt(arr.subSpecialityId)  && val.serviceId === parseInt(arr.serviceId))
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
			
			
			
			
			
			$scope.removeProcedure = function(index){
				$scope.procedureWiseList.splice(index,1);
			}
			
			
			$scope.popupForSaveDetails = function(){
				
				if($scope.couponTemplateMasterId == '' || $scope.couponTemplateMasterId == null)
				{
				    growl.warning("Please apply coupon template",
						{
							title : 'Warning!'
						});
				    return false;

				}

			else if($scope.couponTemplateDescription == '' || $scope.couponTemplateDescription == null)
			{
			    growl.warning("Please enter coupon template description",
					{
						title : 'Warning!'
					});
			    return false;

			}
			else if($scope.expireDays == '' || $scope.expireDays == null)
			{
			    growl.warning("Please enter expire days",
					{
						title : 'Warning!'
					});
			    return false;

			}
			else{
			
				if($scope.couponTemplateDetailsList.length == null ||  $scope.couponTemplateDetailsList.length == 0){
					growl.warning("Please add atleast one service",
							{
								title : 'Warning!'
							});
				}
				
				var per = false;
				angular.forEach($scope.couponTemplateDetailsList,function(value,index) {
				if((value.amount == 0 || value.amount == "") && (value.percentage == 0 || value.percentage == ""))
					{
					per= true;
					}
				})
				
				if(per){
					growl.warning("Please enter percentage or amount",
							{
								title : 'Warning!'
							});
				}else{
					
					$scope.startLoader();
					 $scope.couponDetailsList = []; 
					 angular.forEach($scope.couponTemplateDetailsList,function(value,index) {
							
						 var newArr = {};
							newArr.serviceId = value.serviceId;
							newArr.specialityId = value.specialityId,
							newArr.percentage =value.percentage,
							newArr.amount =value.amount,
							newArr.subSpecialityId = value.subSpecialityId,
							newArr.organizationId = $scope.organizationId,
							newArr.unitId= $scope.unitId,
							newArr.createdBy= $scope.userId,
							newArr.updatedBy= $scope.userId,
							newArr.status= 'A',
							newArr.couponTemplateMasterId = $scope.couponTemplateMasterId,
							newArr.couponTemplateDetailsId = value.couponTemplateDetailsId,
							newArr.couponTemplateDescription = $scope.couponTemplateDescription,
							newArr.expireDays = $scope.expireDays,
							newArr.patientId = $scope.patientDetails.patientId,
							newArr.consumption = value.consumption,
							newArr.orderDetailsId = $scope.orderDetailsId,
							 $scope.couponDetailsList.push(newArr);
							
							});
					
					 var URI = BASE_URL + ROOT_URL+ "/api/billing/Patientcoupon/savePatientCouponTemplateMapper";
						GenericService.serviceAction("POST", URI, $scope.couponDetailsList).then(
								function(response) {
									$scope.couponTemplateDetailsList = [];
									$scope.stopLoader();
									$scope.expireDays = '';
									$scope.couponTemplateMasterId = null;
									if (response.data.status == "success")
									{
										growl.success("Save successfully",
												{
													title : 'Success!'
												});
										
										var selectedTemplateTransactionId = [];
										angular.forEach(response.data.listObject ,function (value,key){
                                    		
                                        			selectedTemplateTransactionId.push(value.patientCouponTemplateMapperId);
                                    			
                                    		});
										
										
										var data =  {
									                 "appName": "HIS",
									                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									                 "reportDesignFilePath": "patientCouponTemplate.rpt",
									                 "reportFormat": "PDF",
									                 "exportFileName": "Coversheet",
									                 "reportParams": [
									                	 {
											                  "paramName": "patient_coupon_template_mapper_id",
											                  "paramType": "String",
											                  "paramValue": selectedTemplateTransactionId.toString()
										                },
										               
							                 ]
							              };
									    reportService.generate(REPORT_API_URI1,data);
										
									}	
									
								});
					
				}
				
			}
				
				
			}
			
			$scope.printBill = function(value){
				
				var selectedTemplateTransactionId = [];
            		
                selectedTemplateTransactionId.push(value.patientCouponTemplateMapperId);
            			
				
				
				var data =  {
			                 "appName": "HIS",
			                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			                 "reportDesignFilePath": "patientCouponTemplate.rpt",
			                 "reportFormat": "PDF",
			                 "exportFileName": "Coversheet",
			                 "reportParams": [
			                	 {
					                  "paramName": "patient_coupon_template_mapper_id",
					                  "paramType": "String",
					                  "paramValue": selectedTemplateTransactionId.toString()
				                },
				               
	                 ]
	              };
			    reportService.generate(REPORT_API_URI1,data);
				
			}
			
			$scope.getSubSpecialityBySpeciality = function(obj)
			{
				$scope.groupDetailsObj.specialityName = obj.specialityName;
				$scope.groupDetailsObj.specialityId = obj.specialityId; 
				
				$scope.groupDetailsObj.subSpecialityName = null;
				$scope.groupDetailsObj.subSpecialityId = null; 
				
				
				var data = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
					data.specialityId = obj.specialityId;
					var URI = BASE_URL + ROOT_URL
							+ GETSUBSPECIALITYBYSPECIALITYID;
					GenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.subSpecialityList = [];
										if (response.data.status == "success")
											$scope.subSpecialityList = response.data.listObject;
									});
				
			}
			
			$scope.getSubSpecialityDetails = function(obj2)
			{
				$scope.groupDetailsObj.subSpecialityName = obj2.subSpecialityName;
				$scope.groupDetailsObj.subSpecialityId = obj2.subSpecialityId; 
				
			}
			
			
			
			$scope.addGroupDetailsToList = function()
			{
				var saveCoupon = false;
				angular.forEach($scope.couponTemplateDetailsList,function(value,index) {
					if(value.couponTemplateDetailsId != undefined || value.couponTemplateDetailsId != null){
						saveCoupon = true;
					}
				});
				
				if(saveCoupon == false)
				{
				    growl.warning("Please apply coupon template",
						{
							title : 'Warning!'
						});
				    return false;

				}
			else
				if(($scope.groupDetailsObj.specialityId == '' || $scope.groupDetailsObj.specialityId == null) &&  ($scope.selectedServiceId == '' || $scope.selectedServiceId == null || $scope.selectedServiceId == undefined))
					{
					    growl.warning("Please select specialty/service",
							{
								title : 'Warning!'
							});
					    return false;

					}
				else
					{
					
					var data={
							specialityId : $scope.groupDetailsObj.specialityId,
							specialityName : $scope.groupDetailsObj.specialityName,
							subSpecialityId : $scope.groupDetailsObj.subSpecialityId,
							subSpecialityName : $scope.groupDetailsObj.subSpecialityName,
							serviceId : $scope.selectedServiceId,
					    	serviceName : $scope.selectedServiceName,
							percentage: "",
							organizationId : $scope.organizationId,
							unitId: $scope.unitId,
							createdBy: $scope.userId,
							updatedBy: $scope.userId,
							status: 'A',
							amount : "",
							consumption :'N'
					};
					
					
					$scope.couponTemplateDetailsList.push(data);
					console.log($scope.groupDetailList);
					$scope.groupDetailsObj = {};
					$scope.selectedServiceId = null;
			    	$scope.selectedServiceName = null;
			    	$scope.search = '';
			    	$scope.obj = null;
			    	$scope.obj1 = null;
			    	$scope.percentage = '';
					
				}
				
			}
			
			$scope.removeAddedGroup = function(groupDetailList,index) {
				
				if(groupDetailList.couponTemplateDetailsId != undefined && groupDetailList.couponTemplateDetailsId != null) {
					groupDetailList.status = 'I';
					$scope.saveRemoveList.push(groupDetailList);
				}
				
				$scope.couponTemplateDetailsList.splice(index,1)
			}
			
		
			
			
		$scope.getServicesBySpecialityAndSubSpeciality = function()
		{
			console.log($scope.serviceDetailsObj);
			
			 $scope.data={

						"subSpecialityIdList":[$scope.serviceDetailsObj.subSpecialityId],
						"orgnisationId":$scope.organizationId,
						"unitId":$scope.unitId
						
			 }
			 var URI = BASE_URL + ROOT_URL+ "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
			GenericService
			.serviceAction(METHOD_POST, URI, $scope.data,
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(
					function(response) {
						console.log(response);
						$scope.startLoader();
						if (response.data.status == "success")
							$scope.stopLoader();
						console.log("spl List",response.data.listObject);
						
						angular.forEach(response.data.listObject,function(value,index) {
							var newArr = {};
							newArr.serviceId = value.serviceId;
							newArr.serviceName = value.serviceStandardName;
							newArr.specialityId = value.specialityId,
							newArr.specialityName = value.specialityName,
							newArr.percentage = '',
							newArr.subSpecialityId = value.subSpecialityId,
							newArr.subSpecialityName = value.subSpecialityName,
							newArr.organizationId = $scope.organizationId,
							newArr.unitId= $scope.unitId,
							newArr.createdBy= $scope.userId,
							newArr.updatedBy= $scope.userId,
							$scope.serviceListBySpeciality.push(newArr);
							});
					
							console.log($scope.serviceListBySpeciality);
						
					});
		}
		
		$scope.patientCouponMapperDetailsList = [];
		$scope.getPatientCouponMapperData = function(){
			
			if($scope.patientDetails.patientId == undefined || $scope.patientDetails.patientId == null || $scope.patientDetails.patientId == ''){
				
				return false;
				
			}else{
				$scope.startLoader();
				 var data={

							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId,
							"patientId":$scope.patientDetails.patientId
				 }
				
				 var URI = BASE_URL + ROOT_URL +"/api/billing/Patientcoupon/getMappedPatientCouponTemplate";
				 GenericService.serviceAction("POST", URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(
					function(response) {
						console.log(response);
						$scope.stopLoader();
						if (response.data.status == "success"){
							
							$scope.patientCouponMapperDetailsList = response.data.listObject;
							
						}
						
					});
				
			}
			
		}
		
		  $scope.getClassForColor = function(info)
          {

          	 if(info.status=='I'){
          		 
                     return 'td-bg-red';
                     
                 }else if (info.consume==true) 
                 {
                      return 'td-bg-gray';
                      
                 }else if(info.status=='A' && info.consume== false){
              	   
                     return 'td-bg-green';
                 }
                
          }
		
		  
		  $scope.cancelCoupon = function(info){
			  $scope.startLoader();
				 var data={

							"status":'I',
							"patientCouponTemplateMapperId":info.patientCouponTemplateMapperId
				 }
				
				 var URI = BASE_URL + ROOT_URL +"/api/billing/Patientcoupon/forceStopPatientCouponTemplate";
				 GenericService.serviceAction("POST", URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(
					function(response) {
						console.log(response);
						$scope.stopLoader();
						if (response.data.status == "success"){
							
							growl.success("Stop successfully",
									{
										title : 'Success!'
									});
							$scope.getPatientCouponMapperData();
						}
						
					});
				
		  }

		  $scope.checkAmountAndPer = function(flag,index){
			  
			  if(flag == 1){
				  
				  if($scope.couponTemplateDetailsList[index].percentage >100){
					  growl.warning("Percentage should not be greater than 100 %",{
							title : "WARNING"
						});
					  $scope.couponTemplateDetailsList[index].percentage = 0;
				  }
				  
				  $scope.couponTemplateDetailsList[index].amount = 0;
			  }else{
				  $scope.couponTemplateDetailsList[index].percentage = 0;
			  }
			  
		  }
		  
		
}]);