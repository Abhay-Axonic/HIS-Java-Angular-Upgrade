angular.module("myApp").controller("doctorSlabController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','GenericService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,GenericService){
	try{
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		
		//$scope.init();
		
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		 if(cookieObjectAppConfig == undefined){
			 $state.go('applicationConfiguration');
		  return;
		 }
		 else
		  {
		   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
		   
		  }
		// $scope.isProcessShareAgainstSettledBill = cookieObjectAppConfig.isProcessShareAgainstSettledBill;
		 
		 $scope.billStatusId = 3;
		 
		/**DatePicker Code  */
		
		$scope.fromDate = moment().startOf('day').toDate();
	    $scope.toDate = moment().startOf('day').toDate();
		$scope.fromDatePopup = moment().startOf('day').toDate();
	    $scope.toDatePopup = moment().startOf('day').toDate();
	    $scope.completed='N';
		$scope.pending = 'N';
		$scope.outsourced = 'N';
		$scope.disableInsurance = true;
		
		$scope.dateOptions =
		{
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : "",//new Date()
		};

		$scope.open = function() {
			$scope.popup.opened = true;
		};

		$scope.popup =
		{
			opened : false
		};
		
		$scope.popup1 =
		{
			opened : false
		};
		
		$scope.open1 = function() 
		{
			$scope.popup1.opened = true;
		};
		
		$scope.openPopup = function() {
			$scope.popupSer.opened = true;
		};

		$scope.popupSer =
		{
			opened : false
		};
		
		$scope.popup1Ser =
		{
			opened : false
		};
		
		$scope.open1Popup = function() 
		{
			$scope.popup1Ser.opened = true;
		};
		
		$scope.selectedDoctorList = [];
		
		$scope.tDoctorShareRespDto={
				"doctorShareId": "",
				"billDtlId": "",
				"visitTypeId": "",
				"patientId": "",
				"billingMasterId": "",
				"visitTypeCode": "",
				"doctorName": "",
				"specialityName": "",
				"patientName": "",
				"orderNo": " ",
				"orderDate": " ",
				"orderSchDate": " ",
				"serviceStandardCode": " ",
				"serviceStandardName": " ",
				"ordDocSplId": " ",
				"taxId": " ",
				"ordDtlId": "",
				"visitId": "",
				"admissionId": " ",
				"serviceId": "",
				"unitRate": "",
				"totalAmount": "",
				"netAmount": "",
				"encounterDate": " ",
				"encounterNumber": " ",
				"taxType": " ",
				"docId": "",
				"docSpecId": "",
				"sharePer": "",
				"hospShare": "",
				"docShare": "",
				"processedShareAmt":"",
				"dueShareAmt":  "",
				"isBillCrDr": " ",
				"isActive": " ",
				"isOnBillPayment": "",
				"discount":"",
				"concession": "",
				"taxAmt": "",
				"billNumber": " ",
				"orgUnitId": "",
				"orgId": $scope.orgId,
				"status": "A",
				"createdDate": " ",
				"createdBy": $scope.userId,
				"updatedDate": " ",
				"updatedBy": " ",
				"uhidNumber": " ",
				"processedShareDate": " ",
				"dueShareDate": " "
		}
		
		$scope.tDocShareVoucherDto={
				"docShareVoucheId": " ",
				"voucherNo": "",
				"totalDocShareAmout":"",
				"orgId": $scope.orgId,
				"orgUnitId": $scope.unitId,
				"status": "A",
				"createdDate": " ",
				"createdBy":$scope.userId,
				"updatedDate": " ",
				"updatedBy": " ",
				"remark":""
				
		}
		
		$scope.TDocShareVoucherDtlsDto={
				"voucherDtlsId": " ",
				"docShareVoucheId": " ",
				"doctorShareId": "",
				"processedAmt": "",
				"orgUnitId": $scope.unitId,
				"orgId": $scope.orgId,
				"status": "A",
				"createdDate": " ",
				"createdBy": $scope.userId,
				"updatedDate": " ",
				"updatedBy": " "
		}
		
		var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.orgId
			};
		
		
		var URI = BASE_URL + ROOT_URL
		+ GETACTIVEDOCTORLIST;
		GenericService
		.serviceAction("POST", URI, commonObj)
		.then(
				function(response) {
					$scope.doctorsList = [];

					if (response.data.status == "success") {
						$scope.doctorsList = response.data.listObject;

					}
				});

		  $scope.getVisitTypeList = function(searchPackageObj) {
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
			  $rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getVisitTypeList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.visityTypeList = response.data.listObject;
						  console.log("visityTypeList",JSON.stringify($scope.visityTypeList));
					}
					$rootScope.stopLoader();
				});
			  }
			$scope.getVisitTypeList();
			
			$scope.processDocShare = function() {
				  $rootScope.startLoader();
				    var URI = BASE_URL + ROOT_URL + "/api/billing/process/doctorShare/"+$scope.unitId;
				    BillingGenericService.serviceAction(METHOD_GET,URI,"",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							$rootScope.stopLoader();
						}
						
					});
				  }
			$scope.processDocShare();
			
		
		  $scope.init = function(){
			  $scope.initDoctorShareProcessObj();
			  $scope.doctorShareProcessList = [];
			  $scope.getSpecialityList();
			  $scope.total = 0;
				$scope.grandTotal = 0;
				$scope.discountAmt = 0;
				$scope.gstAmt = 0;
				$scope.doctorSharAmt =0;
				$scope.hosSharAmt = 0;
				$scope.processedShareAmt = 0;
				$scope.dueShareAmt = 0;
				$scope.finaltotal = 0;
				$scope.finaltotalAmount = 0;
				$scope.disallowedAmount =0;
				
			 // $scope.calculateAmounts();
				$scope.splitDocShare = {doctorId:"",sharePer:"",amount:0.00,doctorName:""};
				$scope.splitDocShareList = [];
				$scope.splitDoctorList = [];
				
				 $scope.search = '';
				  $scope.orderServiceId = '';
		    	  $scope.orderServiceName = ''
		    	  $scope.patientName = '';
				  $scope.uhidNumber = '';
				  $scope.mobileNumber ='';
				  $scope.patientId = '';
				  $scope.billNumber = '';
				  
		  }
		  $scope.initDoctorShareProcessObj = function(){
			   $scope.doctorShareProcessObj =$scope.doctorShareProcessObj = {orgId:$scope.orgId,"visitTypeId":"","fromDate":"","toDate":"","search":"","companyTypeId":"","companyId":"","associateCompanyId":""};
			  //,unitId:$scope.unitId
		  }
		  $scope.initSplitDocShare = function(){
			  $scope.splitDocShare = {doctorId:"",sharePer:"",amount:0.00,doctorName:""};
		  }
		  
		  
		  
		  /*========================= Serach Tab Funcationality Start==================*/
		  
		  

		  $scope.getSpecialityList = function() {
			  
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId}; 
			  $rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVESPECIALITY;
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.specialityList = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
	     }
		  
		  $scope.init();
		  
		  
		  
		  $scope.getSpecialityByDoctor = function(docId,idx)
			{
				 var specDocObj = {
			    			unitId:$scope.unitId,
			    			organizationId:$scope.orgId,
			    			doctorId:docId
			    }
			    var URI = BASE_URL + ROOT_URL
				+ GETSPECIALITYLISTBYDOCTORID;
		BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							console.log(response);
							
							// $rootScope.startLoader();
							if (response.data.status == "success")
								{
								//$scope.docSpecialityList = response.data.listObject;
								$scope.billServicesList[idx].docSpecialityList = response.data.listObject;
								
								/*$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
						            return docList.doctorId == docId;
						           })[0];*/
									
								$scope.billServicesList[idx].docSpecialityId = response.data.listObject[0].specialityId;
								}
								
						});
		}
		  
		  $scope.getdoctorShareProcesssListRefresh = function()
		  {
			  $scope.init();
			  $scope.selectedAll = false;
			  $scope.fromDate = moment().startOf('day').toDate();
			  $scope.toDate = moment().startOf('day').toDate();
			  $scope.completed='N';
			  $scope.pending = 'N';
			  $scope.outsourced = 'N';
			  $scope.tDoctorShareRespDto={
						"doctorShareId": "",
						"billDtlId": "",
						"visitTypeId": "",
						"patientId": "",
						"billingMasterId": "",
						"visitTypeCode": "",
						"doctorName": "",
						"specialityName": "",
						"patientName": "",
						"orderNo": " ",
						"orderDate": " ",
						"orderSchDate": " ",
						"serviceStandardCode": " ",
						"serviceStandardName": " ",
						"ordDocSplId": " ",
						"taxId": " ",
						"ordDtlId": "",
						"visitId": "",
						"admissionId": " ",
						"serviceId": "",
						"unitRate": "",
						"totalAmount": "",
						"netAmount": "",
						"encounterDate": " ",
						"encounterNumber": " ",
						"taxType": " ",
						"docId": "",
						"docSpecId": "",
						"sharePer": "",
						"hospShare": "",
						"docShare": "",
						"processedShareAmt":"",
						"dueShareAmt":  "",
						"isBillCrDr": " ",
						"isActive": " ",
						"isOnBillPayment": "",
						"discount":"",
						"concession": "",
						"taxAmt": "",
						"billNumber": " ",
						"orgUnitId": "",
						"orgId": $scope.orgId,
						"status": "A",
						"createdDate": " ",
						"createdBy": $scope.userId,
						"updatedDate": " ",
						"updatedBy": " ",
						"uhidNumber": " ",
						"processedShareDate": " ",
						"dueShareDate": " "
				}
				
				$scope.tDocShareVoucherDto={
						"docShareVoucheId": " ",
						"voucherNo": "",
						"totalDocShareAmout":"",
						"orgId": $scope.orgId,
						"orgUnitId": $scope.unitId,
						"status": "A",
						"createdDate": " ",
						"createdBy":$scope.userId,
						"updatedDate": " ",
						"updatedBy": " ",
						"remark":""
						
				}
				
				$scope.TDocShareVoucherDtlsDto={
						"voucherDtlsId": " ",
						"docShareVoucheId": " ",
						"doctorShareId": "",
						"processedAmt": "",
						"orgUnitId": $scope.unitId,
						"orgId": $scope.orgId,
						"status": "A",
						"createdDate": " ",
						"createdBy": $scope.userId,
						"updatedDate": " ",
						"updatedBy": " "
				}
			  $scope.initDoctorShareProcessObj();
			  $scope.initSplitDocShare();
			  $scope.specialityId = '';
			  $scope.doctorId = '';
			  //$scope.selectedServiceType = '';
			  //$scope.selectedSubServiceType = '';
			  $scope.search = '';
			  $scope.orderServiceId = '';
	    	  $scope.orderServiceName = ''
	    	  $scope.patientName = '';
			  $scope.uhidNumber = '';
			  $scope.mobileNumber ='';
			  $scope.patientId = '';
			  $scope.billNumber = '';
			  $scope.selectedDoctorList = [];
			  $scope.processDocShare();
		  }
		  
		  
		/*$scope.getDoctorBySpeciality = function(id) {

				var data = {
					specialityId : id,
					unitId : $scope.unitId,
					organizationId : $scope.orgId
				};
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
						+ GETDOCTORBYSPECIALITYID;
				
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.doctorList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.doctorList = response.data.listObject;
									
									$scope.doctorListOption = [];
									angular.forEach(response.data.listObject,function(value,index) {
												var data = {};
												data.id=value.doctorId;
												data.label=value.firstName+' '+value.lastName;
												$scope.doctorListOption.push(data);
											
											});
									
									  $rootScope.stopLoader();
								});

			}*/
		  
		  $scope.getActiveDoctorsList = function(id) {

				var data = {
					unitId : $scope.unitId,
					organizationId : $scope.orgId
				};
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
						+ "/api/global/doctor/active/plain";
				
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.doctorList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.doctorList = response.data.listObject;
									
									$scope.doctorListOption = [];
									angular.forEach(response.data.listObject,function(value,index) {
												var data = {};
												data.id=value.doctorId;
												data.label=value.doctorName;
												$scope.doctorListOption.push(data);
											
											});
									
									  $rootScope.stopLoader();
								});

			}
		
		  $scope.getActiveDoctorsList();
		
		
		$scope.getSubSpecialityList = function()
        {
        	var selectedServiceTypeString = $scope.selectedServiceType;
        	var selectedServiceType = JSON.parse(selectedServiceTypeString);
        	// alert(selectedServiceType.specialityId);
        	// alert($scope.selectedServiceType.specialityId);
				var specialityType = [];
				specialityType.push(selectedServiceType.specialityId);
						
				var data = {
					organizationId : $scope.orgId,
					unitId : $scope.unitId,
					specialityIdList : specialityType
				}
				$rootScope.startLoader();
				console.log(data);
				var URI = BASE_URL+ ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYARRAYFORTARIFF;
				BillingGenericService
						.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
						.then(function(response) {
									console.log(response);
									if (response.data.status == "success") {
										$scope.subSpecialityList = response.data.listObject;
									}
									$rootScope.stopLoader();
								});
        }
		
		    $scope.search = '';
	        $scope.selectedForFuture = 'N';
		   $scope.searchServices = function(searchKeyword){
	        	
	        	//console.log($scope.selectedServiceType);
	        	/*if($scope.selectedServiceType != undefined){
	        	var selectedServiceTypeString = $scope.selectedServiceType;
	        	var selectedServiceType = JSON.parse(selectedServiceTypeString);
	        	}
	        	
	        	if($scope.selectedSubServiceType != undefined){
	        	var selectedSubServiceTypeString = $scope.selectedSubServiceType;
	        	var selectedSubServiceType = JSON.parse(selectedSubServiceTypeString);
	        	}
	        	
	        	if($scope.selectedServiceType != undefined && $scope.selectedSubServiceType != undefined)
	        	{
		    		$scope.servicesList=[];
		    		$rootScope.startLoader();
		    		var URI = BASE_URL + ROOT_URL + "/global/UnitServiceMapper/search/"+searchKeyword+"/speciality/"+selectedServiceType.specialityId+"/subSpeciality/"+selectedSubServiceType.subSpecialityId+"/unit/"+$scope.unitId+"/org/"+$scope.orgId;
		    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
		    			if(response.data.status == "success"){
		    				$scope.servicesList = response.data.listObject;
		    			}
		    			$rootScope.stopLoader();
		    			return $scope.servicesList;
		    		});
	        	}
	        	else
	        		{*/
		        		$scope.servicesList=[];
		        		$rootScope.startLoader();
			    		var URI = BASE_URL + ROOT_URL + "/global/UnitServiceMapper/searchWithoutSpecialitySubSpeciality/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.orgId;
			    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			    			if(response.data.status == "success"){
			    				$scope.servicesList = response.data.listObject;
			    			}
			    			$rootScope.stopLoader();
			    			return $scope.servicesList;
			    		});
	        		/*}*/
	    	}
		
		  $scope.addServiceToTempServiceList = function($item, $model, $label){
	    		console.log($item);
	    		$scope.orderServiceId = $item.serviceMasterId;
	    		$scope.orderServiceName = $item.serviceStandardName;
	    		
	    		/*var specialityData = {
	    				specialityId : $item.specialityId,
	    				specialityName : $item.specialityName,
	    				isRenderingSpeciality : $item.isRenderingSpeciality
	    		}
	    		$scope.selectedServiceType = specialityData;
	    		
	    		var subSpecialityData = {
	    				subSpecialityId : $item.subSpecialityId,
	    				subSpecialityName : $item.subSpecialityName,
	    		}
	    		$scope.selectedSubServiceType = subSpecialityData;*/
	    		
	    		//$scope.getSubSpecialityList();
	    		// $scope.search = "";
	    	}
		  
		  
		  
		  $scope.searchGroupList = function(keyword) {

				var data = {
					patientName : keyword,
					unitId : $scope.unitId,
					organizationId : $scope.orgId
				}
				console.log(data);
				//$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
						+ PATIENTAUTOFILLSEARCH;
				
				return BillingGenericService
						.serviceAction(METHOD_POST, URI,
								data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										return response.data.listObject;
									//$rootScope.stopLoader();
								});

			}
		  
		  $scope.searchGroupListPopup = function(keyword) {

				var data = {
					patientName : keyword,
					unitId : $scope.unitId,
					organizationId : $scope.orgId
				}
				console.log(data);
				//$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
						+ PATIENTAUTOFILLSEARCH;
				
				return BillingGenericService
						.serviceAction(METHOD_POST, URI,
								data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										return response.data.listObject;
									//$rootScope.stopLoader();
								});

			}
		  
		  $scope.selectPatientIdPopup = function(info, model,
					label) {
				
				/*$scope.patientName = info.patientName;
				$scope.uhidNumber = info.uhidNumber;
				$scope.mobileNumber = info.mobileNumber;*/
				$scope.patientIdPopup = info.patientId;
				
			}
		  
			$scope.selectPatientId = function(info, model,
					label) {
				
				$scope.patientName = info.patientName;
				$scope.uhidNumber = info.uhidNumber;
				$scope.mobileNumber = info.mobileNumber;
				$scope.patientId = info.patientId;
			}
		  
		  
						
		  $scope.getSplitDetailsList = function(docShrId)
		  {
			  
			     $rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/split/details/"+docShrId;
			    BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.splitDetailsNewList = response.data.object;
						 console.log("$scope.splitDetailsNewList",JSON.stringify($scope.splitDetailsNewList));
					}
					$rootScope.stopLoader();
					
				});
		  }
		  
		  
		  $scope.getCurrentDoctor = function(item,idx)
		  {
			  
			  $scope.primaryDoctorShareObject = angular.copy(item);
			  $scope.primaryDoctorShareObjectIndex = idx;
			  $scope.changeDoctorId =item.docId;
			  angular.element('#doctorChangeModol').modal('show');
			  
		  }
		  
		  $scope.changeDoctorForPrimary = function()
		  {
			  //$rootScope.startLoader();
			  var data = {
					  	docId:$scope.changeDoctorId,
					  	doctorSlabTransactionId : $scope.primaryDoctorShareObject.doctorShareSplitId,
			  }
			  console.log("data",data);
			 //return false;
			  var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/doctor/change";
			    BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						
						
						$scope.splitDocShareList[$scope.primaryDoctorShareObjectIndex].docId=$scope.changeDoctorId;
						$scope.doctorShareProcessList[$scope.parentListIndex].docId = $scope.changeDoctorId; 
						
						var docObjName = $.grep($scope.doctorList, function (doctorObj) {
				   	           return doctorObj.doctorId== $scope.changeDoctorId;
				   	       })[0];
						var finalDocName = docObjName.firstName+' '+docObjName.lastName;
						
						$scope.splitDocShareList[$scope.primaryDoctorShareObjectIndex].doctorName =finalDocName;
						$scope.doctorShareProcessList[$scope.parentListIndex].doctorName =finalDocName;
						
						angular.element('#doctorChangeModol').modal('hide');
//						$rootScope.stopLoader();
					}
					else
						{
						   $rootScope.stopLoader();
						}
				});
			    
			  
			  
		  }
		  
		  $scope.getServicesForDoctorShare = function()
		  {
				 var fromDate= $scope.fromDate==undefined?"":moment($scope.fromDatePopup.getTime()).format('YYYY-MM-DD');
				  var toDate=$scope.toDate==undefined?"":moment($scope.toDatePopup.getTime()).format('YYYY-MM-DD');
				  
			  var param = {
							      orgId:$scope.orgId,
								  orgUnitId:$scope.unitId,
								  visitTypeId:$scope.visitTypeIdPopup,
								  patientId:$scope.patientIdPopup != undefined ? $scope.patientIdPopup:null,
								  billNumber : $scope.billNumberPopup,
								  fromDate:fromDate,
								  toDate:toDate,
					      };
			  
			  
			  $rootScope.startLoaderPopup();
			  $scope.billServicesList = [];
			  var URI = BASE_URL + ROOT_URL + "/api/billing/getPendingServicesForDoctorShareByBillId";
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						$scope.billServicesList = response.data.listObject;
						$scope.visitTypeIdPopup = '';
						$scope.patientIdPopup = '';
						$scope.billNumberPopup = '';
						$scope.patientName = '';
						$scope.fromDatePopup = moment().startOf('day').toDate();
					    $scope.toDatePopup = moment().startOf('day').toDate();
						
						$rootScope.stopLoaderPopup();
					}else{
						$rootScope.stopLoaderPopup();
					}
				});
				
			  

			    /* $rootScope.startLoaderPopup();
			    var URI = BASE_URL + ROOT_URL + "/api/billing/getPendingForDoctorShare"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						
						$scope.visitTypeIdPopup = '';
						$scope.patientIdPopup = '';
						$scope.billNumberPopup = '';
						$scope.getdoctorShareProcesssListRefresh();
						 
					}else{
						$rootScope.stopLoaderPopup();
					}
					
					
				});*/
			  
		  }
		  
		  
		  
		  $scope.getDiscountPercentage = function(item){
			  
			  var disPer = parseFloat(0);
			  if(item.discount > 0 && (item.discountPercentage == null || item.discountPercentage == 0 || item.discountPercentage == '')){
				  disPer = isNaN(((parseFloat(item.discount)/(parseFloat(item.grossAmount) - parseFloat(item.concession)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(item.discount)/(parseFloat(item.grossAmount) - parseFloat(item.concession)))*100).toFixed(2);
				  return disPer;
					}else{
						  return disPer;
					}
			  
			  
		  }
		  
		  $scope.getdoctorShareProcesssList = function(doctorShareProcessObj) {
			  $scope.selectedAll = false;
			 var fromDate= $scope.fromDate==undefined?"":moment($scope.fromDate.getTime()).format('YYYY-MM-DD 00:00:00');
			  var toDate=$scope.toDate==undefined?"":moment($scope.toDate.getTime()).format('YYYY-MM-DD 24:00:00');
			
			  
			  if ($scope.selectedDoctorList.length == 0){
					growl.warning("Please select doctor.",{
						title : WARNING_MSG
					});
					
					return;
				}
			 
			  if( $scope.doctorShareProcessObj.companyTypeId==3)
		    	{
					 if($scope.doctorShareProcessObj.companyId == '' || typeof($scope.doctorShareProcessObj.companyId) == "undefined"){
							growl.warning("Please select company...!!",{
								title : 'Warning'
							});
							return false;
						}
				}else if($scope.doctorShareProcessObj.companyTypeId==2){
					if( $scope.doctorShareProcessObj.companyId == '' || typeof($scope.doctorShareProcessObj.companyId) == "undefined"){
							growl.warning("Please select company and insurance company...!!",{
								title : 'Warning'
							});
							return false;
						}
					if( $scope.doctorShareProcessObj.associateCompanyId == '' || typeof($scope.doctorShareProcessObj.associateCompanyId) == "undefined"){
						growl.warning("Please select insurance company...!!",{
							title : 'Warning'
						});
						return false;
					}
				}
					
			 
				
				
				
				$scope.doctorListForSearch = [];
				angular.forEach($scope.selectedDoctorList,function(value,index){
					$scope.doctorListForSearch.push(value.id);
				});
				
				console.log("$scope.doctorListForSearch ",$scope.doctorListForSearch);
				
			  var param = {
							      orgId:$scope.orgId,
								  orgUnitId:$scope.unitId,
								  visitTypeId:$scope.doctorShareProcessObj.visitTypeId,
								  fromDate:fromDate,
								  toDate:toDate,
								  patientId:$scope.patientId != undefined ? $scope.patientId:null,
								  doctorIds:$scope.doctorListForSearch,
								  orderServiceId :$scope.orderServiceId,
								  billNumber : $scope.billNumber,
								 // isProcessShareAgainstSettledBill : $scope.isProcessShareAgainstSettledBill,
								  billStatusId : $scope.billStatusId,
								  companyTypeId : $scope.doctorShareProcessObj.companyTypeId,
								  companyId : $scope.doctorShareProcessObj.companyId,
								  associateCompanyId : $scope.doctorShareProcessObj.associateCompanyId,
								  
					      };
			  
			     $rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/list"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						
						 $scope.total = parseFloat(0);
							$scope.grandTotal =  parseFloat(0);
							$scope.discountAmt =  parseFloat(0);
							$scope.disallowedAmount =  parseFloat(0);
							$scope.doctorSharAmt =  parseFloat(0);
							$scope.hosSharAmt =  parseFloat(0);
							$scope.processedShareAmt =  parseFloat(0);
							$scope.dueShareAmt =  parseFloat(0);
							$scope.billStatusIdDisabled = $scope.billStatusId;
						 $scope.doctorShareProcessList = response.data.listObject;
						// $scope.initCalculateAmounts();
						 console.log("$scope.doctorShareProcessList",JSON.stringify($scope.doctorShareProcessList));
					}
					$rootScope.stopLoader();
					
				});
			  }
		  
		  $scope.getdoctorShareProcesssListForSearchFilter = function(serchKey) {
			  $scope.selectedAll = false;
			 var fromDate= $scope.fromDate==undefined?"":moment($scope.fromDate.getTime()).format('YYYY-MM-DD 00:00:00');
			  var toDate=$scope.toDate==undefined?"":moment($scope.toDate.getTime()).format('YYYY-MM-DD 24:00:00');
				
				$scope.doctorListForSearch = [];
				angular.forEach($scope.selectedDoctorList,function(value,index){
					$scope.doctorListForSearch.push(value.id);
				});
				
				console.log("$scope.doctorListForSearch ",$scope.doctorListForSearch);
				
			  var param = {
							      orgId:$scope.orgId,
								  orgUnitId:$scope.unitId,
								  visitTypeId:$scope.doctorShareProcessObj.visitTypeId,
								  fromDate:fromDate,
								  toDate:toDate,
								  patientId:$scope.patientId != undefined ? $scope.patientId:null,
								  doctorIds:$scope.doctorListForSearch,
								  orderServiceId :$scope.orderServiceId,
								  billNumber : $scope.billNumber,
								  //isProcessShareAgainstSettledBill : $scope.isProcessShareAgainstSettledBill,
								  billStatusId : $scope.billStatusId,
								  patientName : serchKey
					      };
			  
			     $rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						
						 $scope.total = parseFloat(0);
							$scope.grandTotal =  parseFloat(0);
							$scope.discountAmt =  parseFloat(0);
							$scope.disallowedAmount =  parseFloat(0);
							$scope.doctorSharAmt =  parseFloat(0);
							$scope.hosSharAmt =  parseFloat(0);
							$scope.processedShareAmt =  parseFloat(0);
							$scope.dueShareAmt =  parseFloat(0);
							$scope.billStatusIdDisabled = $scope.billStatusId;
						 $scope.doctorShareProcessList = response.data.listObject;
						// $scope.initCalculateAmounts();
						 console.log("$scope.doctorShareProcessList",JSON.stringify($scope.doctorShareProcessList));
					}
					$rootScope.stopLoader();
					
				});
			  }
		  
		
		  
		  
		  $scope.getCompaniesByCompanyType = function(companyTypeId){
		    	
			  $scope.doctorShareProcessObj.associateCompanyId ="";
			  $scope.doctorShareProcessObj.companyId ="";
		    	if(companyTypeId != ""){
		    		 $scope.companies = [];
		    		if(companyTypeId==3)
			    		$scope.disableInsurance=true;
			    	else
			    		$scope.disableInsurance=false;
		    		
		    		var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:companyTypeId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList";
				    $rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							 $scope.companies = response.data.listObject;
						}
						$rootScope.stopLoader();
					});
		    	}
		    }
		  
		  
		  $scope.getInsuranceByCompanyId = function(companyId){
			  $scope.associateCompanies =  [];
		    	if(companyId != ""){
			    	var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster";
				    $rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							 $scope.associateCompanies = response.data.listObject;
						}
						$rootScope.stopLoader();
					});
		    	}
		    }
		  
		  
		  /*========================= Serach Tab Funcationality End==================*/
		  
		  $scope.initCalculateAmounts = function(){
				var processedShareAmt=0;
				var dueShareAmt=0;
			    var total = 0;
				var totalAmount=0;
				var discountAmt=0;
				var gstAmt=0;
				var doctorSharAmt=0;
				var hosSharAmt=0;
				angular.forEach($scope.doctorShareProcessList,function(value,index){
					total = parseFloat(total) + (isNaN(value.netAmount) ? 0 : parseFloat(value.netAmount));
					totalAmount = parseFloat(totalAmount) + (isNaN(value.totalAmount) ? 0 : parseFloat(value.totalAmount));
					discountAmt = parseFloat(discountAmt) + (isNaN(value.discount) ? 0 : parseFloat(value.discount));
					gstAmt = parseFloat(gstAmt) + (isNaN(value.taxAmt) ? 0 : parseFloat(value.taxAmt));
					doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
					hosSharAmt = parseFloat(hosSharAmt) + (isNaN((value.netAmount-(value.netAmount*value.sharePer)/100)) ? 0 : parseFloat((value.netAmount-(value.netAmount*value.sharePer)/100)));
					processedShareAmt = parseFloat(processedShareAmt) + (isNaN(value.tempProcessedShareAmt) ? 0 : parseFloat(value.tempProcessedShareAmt));
					dueShareAmt = parseFloat(dueShareAmt) + (isNaN(value.dueShareAmt) ? 0 : parseFloat(value.dueShareAmt));
				});
				$scope.total = $rootScope.parseFloatNum(totalAmount);
				$scope.grandTotal = $rootScope.parseFloatNum(total);
				$scope.discountAmt = $rootScope.parseFloatNum(discountAmt);
				$scope.gstAmt = $rootScope.parseFloatNum(gstAmt);
				$scope.doctorSharAmt = $rootScope.parseFloatNum(doctorSharAmt);
				$scope.hosSharAmt = $rootScope.parseFloatNum(hosSharAmt);
				$scope.processedShareAmt = $rootScope.parseFloatNum(processedShareAmt);
				$scope.dueShareAmt = $rootScope.parseFloatNum(dueShareAmt);
		  }
		  
		  
		  var processedShareAmt=0;
			var dueShareAmt=0;
		    var total = 0;
			var totalAmount=0;
			var discountAmt=0;
			var gstAmt=0;
			var doctorSharAmt=0;
			var hosSharAmt=0;
			
		  $scope.calculateAmounts = function(docShare){
			  if (docShare != undefined) 
			  docShare.dueShareAmt = (docShare.tempDueShare - docShare.tempProcessedShareAmt) >=0 ? (docShare.tempDueShare - docShare.tempProcessedShareAmt) : 0;
			  if(docShare.dueShareAmt == 0){
				  docShare.tempProcessedShareAmt = docShare.tempDueShare;
				  docShare.dueShareAmt  = (docShare.dueShareAmt).toFixed(2);
			  }else
				   {
				    docShare.dueShareAmt  = (docShare.dueShareAmt).toFixed(2);
				  }
			  
			  /*if (docShare != undefined) 
			  $scope.processedShareAmt = parseFloat($scope.processedShareAmt) + (isNaN(docShare.tempProcessedShareAmt) ? parseFloat(0) : parseFloat(docShare.tempProcessedShareAmt));
			  $scope.dueShareAmt = parseFloat($scope.doctorSharAmt) - (isNaN(docShare.processedShareAmt) ? parseFloat(0) : parseFloat(docShare.processedShareAmt));
			  */
				var processedShareAmt=0;
				var dueShareAmt=0;
			   /* var total = 0;
				var totalAmount=0;
				var discountAmt=0;
				var gstAmt=0;
				var doctorSharAmt=0;
				var hosSharAmt=0;*/
				
				
				
				angular.forEach($scope.doctorShareProcessList,function(value,index){
					/*total = parseFloat(total) + (isNaN(value.netAmount) ? 0 : parseFloat(value.netAmount));
					totalAmount = parseFloat(totalAmount) + (isNaN(value.totalAmount) ? 0 : parseFloat(value.totalAmount));
					discountAmt = parseFloat(discountAmt) + (isNaN(value.discount) ? 0 : parseFloat(value.discount));
					gstAmt = parseFloat(gstAmt) + (isNaN(value.taxAmt) ? 0 : parseFloat(value.taxAmt));
					doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
					hosSharAmt = parseFloat(hosSharAmt) + (isNaN((value.netAmount-(value.netAmount*value.sharePer)/100)) ? 0 : parseFloat((value.netAmount-(value.netAmount*value.sharePer)/100)));*/
					 if(value.doctorShare == true){
						
						dueShareAmt = parseFloat(dueShareAmt) + (value.dueShareAmt == "" || (isNaN(value.dueShareAmt)) ? 0 : parseFloat(value.dueShareAmt));
						processedShareAmt = parseFloat(processedShareAmt) + ((value.tempProcessedShareAmt == "" || isNaN(value.tempProcessedShareAmt)) ? 0 : parseFloat(value.tempProcessedShareAmt));
						//processedShareAmt = parseFloat(processedShareAmt) + dueShareAmt;
					 }
				});
				
				$scope.processedShareAmt = $rootScope.parseFloatNum(processedShareAmt);
				$scope.dueShareAmt = (dueShareAmt).toFixed(2);
				console.log("dd");
		  }
		  
		  
		  $scope.checkCalculateAmounts = function(docShare){
			  if (docShare != undefined) 
				 // docShare.tempProcessedShareAmt = docShare.dueShareAmt;
				  var finaltotal=0;
                 
			  var total = 0;
				var totalAmount=0;
				var discountAmt=0;
				var disallowedAmountAmt=0;
				var doctorSharAmt=0;
				var hosSharAmt=0;
				var dueShareAmt = 0;
				var processedShareAmt = 0;
			  
			  angular.forEach($scope.doctorShareProcessList,function(value,index){
				  if(value.doctorShare == true){
					total = parseFloat(total) + (isNaN(value.netAmount) ? 0 : parseFloat(value.netAmount));
					totalAmount = parseFloat(totalAmount) + (isNaN(value.totalAmount) ? 0 : parseFloat(value.totalAmount));
					discountAmt = parseFloat(discountAmt) + (isNaN(value.discount) ? 0 : parseFloat(value.discount));
					disallowedAmountAmt = parseFloat(disallowedAmountAmt) + (isNaN(value.disallowedAmount) ? 0 : parseFloat(value.disallowedAmount));
					//doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
					processedShareAmt =  parseFloat(processedShareAmt) +(isNaN(value.dueShareAmt) ? 0 : parseFloat(value.dueShareAmt));
					doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN(value.docShare) ? 0 : parseFloat(value.docShare));
					value.tempProcessedShareAmt = (isNaN(value.dueShareAmt) ? 0 : parseFloat(value.dueShareAmt));
					value.dueShareAmt = 0;
					hosSharAmt = parseFloat(hosSharAmt) + (isNaN(value.hospShare) ? 0 : parseFloat(value.hospShare));
					dueShareAmt  = parseFloat(dueShareAmt) + (isNaN(value.dueShareAmt) ? 0 : parseFloat(value.dueShareAmt));
				  }
					/* if(value.doctorShare == true){
						processedShareAmt = parseFloat(processedShareAmt) + ((value.tempProcessedShareAmt == "" || isNaN(value.tempProcessedShareAmt)) ? 0 : parseFloat(value.tempProcessedShareAmt));
						dueShareAmt = parseFloat(dueShareAmt) + (value.dueShareAmt == "" || (isNaN(value.dueShareAmt)) ? 0 : parseFloat(value.dueShareAmt));
					 }*/
				});
				
					
				$scope.total = $rootScope.parseFloatNum(totalAmount);
				$scope.grandTotal = $rootScope.parseFloatNum(total);
				$scope.discountAmt = $rootScope.parseFloatNum(discountAmt);
				$scope.disallowedAmount = $rootScope.parseFloatNum(disallowedAmountAmt);
				$scope.doctorSharAmt = $rootScope.parseFloatNum(doctorSharAmt);
				$scope.hosSharAmt = $rootScope.parseFloatNum(hosSharAmt);
				$scope.processedShareAmt = $rootScope.parseFloatNum(processedShareAmt);
				$scope.dueShareAmt = $rootScope.parseFloatNum(dueShareAmt);
		  }
		  
		  $scope.checkCalculateAmountsBillWise = function(docShare){
			  if (docShare.doctorShare == true) {
				 
					
			  }else{
				  $scope.selectedAll = false;	
			  }
				
		  }
		  
		  
		 // $scope.getdoctorShareProcesssList();
		  
		 
		  $scope.getBillDetails = function(item,idx)
		  {
			  $rootScope.startLoaderPopup();
			  $scope.billServicesList = [];
			  var URI = BASE_URL + ROOT_URL + "/api/billing/getPendingServicesForDoctorShareByBillId/"+item.billId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						$scope.billServicesList = response.data.listObject;
						$rootScope.stopLoaderPopup();
					}else{
						$rootScope.stopLoaderPopup();
					}
				});
		  }
		  $scope.getBillDetailsPopUpClose = function()
			{
			  	if($scope.billServicesList.length > 0)
			  		{
			  		  $('#closePopupBillDetails').modal('show');
			  		}else{
			  			$('#getBillDetailsModel').modal('hide');
			  		}
				
			}
		  
		  $scope.yesPopupCloseBillDetails = function()
			{
				
					
					
					/*     $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareListSingle.totalDocShare;
						 $scope.doctorShareProcessList[$scope.parentListIndex].docShare = $scope.doctorShareListSingle.docShare;
						 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.doctorShareListSingle.hospShare;
						 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = $scope.doctorShareListSingle.dueShareAmt -  $scope.doctorShareListSingle.processedShareAmt;
						 $scope.doctorShareProcessList[$scope.parentListIndex].sharePer =$scope.doctorShareListSingle.sharePer;*/
						 
					
				$('#closePopupBillDetails').modal('hide');
				$('#getBillDetailsModel').modal('hide');
				
			}
			$scope.noPopupCloseBillDetails = function()
			{
				
				$('#closePopupBillDetails').modal('hide');
				
			}
			$scope.saveDoctorShareBillService = function()
			{
				
				var listDoctorShareRespDto = [];
				var listDoctorShareRespDtoFinal = [];
				listDoctorShareRespDto  =  angular.copy($scope.billServicesList);
					var i = 1;
				  angular.forEach(listDoctorShareRespDto,function(value,index){
					  if(value.doctorSharePopup && value.doctorId > 0 && value.docSpecialityId > 0){
					  value.multidelete("docSpecialityList","doctorSharePopup");
					  listDoctorShareRespDtoFinal.push(value);
					  }
					  if(i == listDoctorShareRespDto.length)
					  {
						  $rootScope.startLoaderPopup();
							var URI = BASE_URL + ROOT_URL + "/api/billing/processDocShareForAlreadyBilledServices";
							BillingGenericService.serviceAction(METHOD_POST,URI, listDoctorShareRespDtoFinal,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
								if (response.data.status == "success"){
									$scope.init();
									$('#pendingDocShareModel').modal('hide');
									$scope.billServicesList =  [];
									$scope.getdoctorShareProcesssListRefresh();
								   $rootScope.stopLoaderPopup();
								}else{
									 $rootScope.stopLoaderPopup();
								}
							});
					  }
					  i++;
					});
			}
			
			$scope.getClear = function(){
				$scope.billServicesList =  [];
				$scope.visitTypeIdPopup = '';
				$scope.patientIdPopup = '';
				$scope.billNumberPopup = '';
				$scope.patientName = '';
			}
		  
			/** code for setting all checkboxes */
			$scope.checkAll = function() {
				
				if ($scope.selectedAll) {
					$scope.selectedAll = true;
					angular.forEach(
									$scope.doctorShareProcessList,
									function(doctorShareProcessObj) {
										
										doctorShareProcessObj.doctorShare = $scope.selectedAll;
										
									});
				} else {
					$scope.selectedAll = false;
					$scope.getdoctorShareProcesssList();
				}
				
			}
			
			$scope.checkAllPopup = function(){
				if ($scope.selectedAllPopup) {
					$scope.selectedAllPopup = true;
					var co = 1;
					angular.forEach($scope.billServicesList,function(value,key) {
										
						value.doctorSharePopup = true;
									});
				} else {
					$scope.selectedAll = false;
					angular.forEach($scope.billServicesList,function(value,key) {
						
						value.doctorSharePopup = false;
									});
				}
			}
			
			$scope.splitDocSharePopUp = function()
			{
				$('#closePopup').modal('show');
			}
			
			$scope.yesPopupClose = function()
			{
				
				$scope.doctorShareListSingle.doctorShare = ($scope.doctorShareListSingle.doctorShare == undefined)? false:$scope.doctorShareListSingle.doctorShare;
					if($scope.doctorShareListSingle.doctorShare == false){
					     $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareListSingle.totalDocShare;
						 $scope.doctorShareProcessList[$scope.parentListIndex].docShare = $scope.doctorShareListSingle.docShare;
						 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.doctorShareListSingle.hospShare;
						 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = $scope.doctorShareListSingle.dueShareAmt -  $scope.doctorShareListSingle.processedShareAmt;
						 $scope.doctorShareProcessList[$scope.parentListIndex].sharePer =$scope.doctorShareListSingle.sharePer;
					}else{
				
				     $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareListSingle.totalDocShare;
					 $scope.doctorShareProcessList[$scope.parentListIndex].docShare = $scope.doctorShareListSingle.docShare;
					 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.doctorShareListSingle.hospShare;
					 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = $scope.doctorShareListSingle.dueShareAmt -  $scope.doctorShareListSingle.processedShareAmt;
					 $scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt = $scope.doctorShareListSingle.dueShareAmt;
					 $scope.processedShareAmt = parseFloat($scope.processedShareAmt) + parseFloat($scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt);
					 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = 0;
					 $scope.doctorShareProcessList[$scope.parentListIndex].sharePer =$scope.doctorShareListSingle.sharePer;
					}
						 
					
				$('#closePopup').modal('hide');
				$('#roomRentModal').modal('hide');
				
			}
			$scope.noPopupClose = function()
			{
				
				$('#closePopup').modal('hide');
				
			}
			
			//Check true check
			  var isCheckProcessAmountZeroTrue;
				function checkCheckProcessAmountZeroOrNotTrue(arr) {
					return arr
							.some(function(arr) {
								if(arr.doctorShare === true)
									{
										return true;
									}
								else
									{
										return false;
									}
							});
				}
				
			//Check amount chck
			  var isCheckProcessAmountZero;
				function checkCheckProcessAmountZeroOrNot(arr) {
					return arr
							.some(function(arr) {
								if(arr.doctorShare === true && (arr.tempProcessedShareAmt == 0 || arr.tempProcessedShareAmt == 0.00 || arr.tempProcessedShareAmt == null || arr.tempProcessedShareAmt == undefined))
									{
										return true;
									}
								else
									{
										return false;
									}
							});
				}
				
				
		  $scope.saveDoctorShareProcess = function(){
			  
			  isCheckProcessAmountZeroTrue=checkCheckProcessAmountZeroOrNotTrue($scope.doctorShareProcessList)
			  if (!isCheckProcessAmountZeroTrue) {
					
					growl.warning("Please select atleast one..!!",{
						title : "WARNING"
					});
					
					return false;
				}
				else
					{
								  
								var i = 1;
								$scope.doctorListForSave = [];
								angular.forEach($scope.doctorShareProcessList,function(value, key) 
								{
									
									if (value.doctorShare == true) 
									{
										value.multidelete("doctorShare");
										$scope.doctorListForSave.push(value);
									}
									
									
									if($scope.doctorShareProcessList.length == i){
										console.log($scope.doctorListForSave);
										
										var param = {
										        listtDoctorShareRespDto:$scope.doctorListForSave,
												orgId:$scope.orgId,
												unitId:$scope.unitId,
												userId:$scope.userId,
												remark : $scope.remark
										    }
								 console.log("param",JSON.stringify(param));
								
									
								var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/voucher";
								BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success")
										$scope.init();
									$scope.selectedDoctorList = [];
									$scope.billNumber ='';
									
									if(response.data.object.length>0)
									{
										angular.element('#notConfigured').modal('show');
										$scope.message = response.data.object;
									}
									
									$rootScope.stopLoader();
								});
								
							
							
									}
									i++;
								});
									 
						
					}
			  
			  
			}
		  
		  $scope.getSplitDoctorBySpeciality = function(id) {
			  console.log($scope.changeSpecialityId);
			  if(id > 0){	
			  if(id != null && id != "" && typeof(id) != "undefined"){
				  	$scope.splitDoctorList = [];
					var data = {specialityId : id,unitId : $scope.unitId,organizationId : $scope.orgId};
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
					
					BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
											$scope.splitDoctorList = response.data.listObject;
										$rootScope.stopLoader();
					});
			  }
		  }
			}
		  
		  $scope.splitDoctorShare = function(share,idx){
			  $rootScope.startLoader();
			  
			/*  if(share.doctorShare){
				  share.dueShareAmt =  share.tempProcessedShareAmt;
				  $scope.doctorShareProcessList[idx].dueShareAmt = share.tempProcessedShareAmt;
				  $scope.processedShareAmt = parseFloat($scope.processedShareAmt) - parseFloat(share.tempProcessedShareAmt);
				  //$scope.dueShareAmt = parseFloat() + 
				  $scope.doctorShareProcessList[idx].tempProcessedShareAmt = 0;
				  share.tempProcessedShareAmt = 0;
				  
			  }*/
			  
			  $scope.splitDocShare.doctorId = '';
			  $scope.splitDocShare.sharePer = 0;
			  $scope.splitDocShare.amount = 0;
			  
			    $scope.splitDocShareList = [];
			  	$scope.parentListIndex = idx;
				var tempDoctorShareListSingle = angular.copy(share);
				$scope.doctorShareListSingle = tempDoctorShareListSingle;
			 /* 	$scope.hospitalShareAmountPopup = share.hospShare;
			  	$scope.hospitalShareAmountPopupForCheck = share.hospShare;
			  	$scope.hospitalShareCheckboxPopupForCheck = share.doctorShare;
				$scope.hospitalDueShareAmtPopupForCheck = share.tempProcessedShareAmt;*/
			  	$scope.doctorShareAmountPopup =0;
			  	
			  	
			    var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/split/details/"+share.doctorSlabTransactionId;
			    BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						
						 $scope.splitDocShareList = response.data.object;
						
						 console.log("$scope.splitDetailsNewList",JSON.stringify($scope.splitDetailsNewList));
						 $scope.splitDocShareList.forEach(function(value){
							 $scope.doctorShareAmountPopup =$scope.doctorShareAmountPopup + value.newDoctorRevenue;
							});
					}
					$rootScope.stopLoader();
					
				});
			
			    //NEW END
			    
		  }
		  
		  $scope.validateSplitSharePerPopup = function(item,idx)
		  {
			 // $scope.doctorShareProcessList
			  if(item.newRevenuePercentage > 100)
				  {
					  growl.warning("Percentage should be less then 100%.",{
							title : "WARNING"
						});
					   $scope.splitDocShareList[idx].newSharePercentage = item.oldRevenuePercentage;
						return false;
				  }
			  else
				  {
				  var tmpOldShrAmt = $scope.splitDocShareList[idx].oldDoctorRevenue;
				  var hosSh = parseFloat($scope.doctorShareProcessList[$scope.parentListIndex].netAmount);
				 
				  //var spliAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].unitRate * item.newSharePercentage)/100;
				  var spliAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].netAmount * item.newRevenuePercentage)/100;
				  var tmpSA = parseFloat(0);
				  
				  angular.forEach($scope.splitDocShareList,function(value,index){
						if(idx != index){
							tmpSA = tmpSA + parseFloat(value.newDoctorRevenue);
						}
						});
				  var tmpSub  = parseFloat($scope.doctorShareProcessList[$scope.parentListIndex].netAmount) - parseFloat(tmpSA);
				  if(spliAmt > tmpSub)
					  {
						  growl.warning("You have only "+ tmpSub,{
								title : "WARNING"
							});
						   $scope.splitDocShareList[idx].newRevenuePercentage = item.oldRevenuePercentage;
							return false;
					  }
				  else
					  {
						  ///$scope.hospitalShareAmountPopup = hosSh - spliAmt;
						 ///var hosSharPopAmt = parseFloat(hosSh - spliAmt).toFixed(2);
						  $scope.splitDocShareList[idx].newDoctorRevenue = spliAmt;
						  $scope.splitDocShareList[idx].oldRevenuePercentage = item.newRevenuePercentage;
			  			   $scope.splitDocShareList[idx].oldDoctorRevenue = spliAmt;
				  			   
			  			// $scope.doctorShareAmountPopup = 0;
			  			 var docShrAmtPopUp = parseFloat(0);
			  			 
			  			var tempPerMainDoctor = parseFloat(0);
						  var tempAmtMainDoctor = parseFloat(0);
						  
			  			 angular.forEach($scope.splitDocShareList,function(value,index){
								 docShrAmtPopUp =docShrAmtPopUp + parseFloat(value.newDoctorRevenue);
								 if(index == 0)
						    	 {
						    	 tempPerMainDoctor = parseFloat(value.newDoctorRevenue);
						    	 tempAmtMainDoctor =parseFloat(value.newDoctorRevenue);
						    	 }
								});
			  			 
			  			
						  
			  			   $scope.doctorShareAmountPopup = parseFloat(docShrAmtPopUp);
			  			 $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue = $scope.doctorShareAmountPopup;
							
			  			   
			  			   
					  }
				  			 
				  }
		  }
		 /* $scope.validateSplitShareAmtPopup = function(item,idx)
		  {
			  var hosShr = parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.splitDocShareList[idx].oldsharAmt);
			
				  if(item.shareAmount > hosShr)
					  {
						   growl.warning("No sufficient fund",{
								title : "WARNING"
							});
						   $scope.splitDocShareList[idx].shareAmount = item.oldsharAmt;
							return false;
					  }
				  else 
					  {
					    var spliPer = (item.shareAmount/hosShr)*100;
					    
					    $scope.splitDocShareList[idx].newSharePercentage = parseFloat(spliPer).toFixed(2);
					    $scope.splitDocShareList[idx].oldSharePercentage =parseFloat(spliPer).toFixed(2);
					    $scope.splitDocShareList[idx].oldsharAmt = item.shareAmount;
					    
					  $scope.hospitalShareAmountPopup = (hosShr - parseFloat(item.shareAmount)).toFixed(2);
					 // $scope.doctorShareAmountPopup = 0;
					  var docShrAmtPopUp = parseFloat(0);
					  angular.forEach($scope.splitDocShareList,function(value,index){
							// $scope.doctorShareAmountPopup =$scope.doctorShareAmountPopup + value.shareAmount;
							 docShrAmtPopUp =docShrAmtPopUp + parseFloat(value.shareAmount);
							});
					  $scope.doctorShareAmountPopup = parseFloat(docShrAmtPopUp);
					  $scope.totalHospitaAndDoctorShareAmount =parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.doctorShareAmountPopup);
						 $scope.doctorShareProcessList[$scope.parentListIndex].docShare = $scope.doctorShareAmountPopup;
						 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.hospitalShareAmountPopup;
						 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = $scope.doctorShareProcessList[$scope.parentListIndex].docShare -  $scope.doctorShareProcessList[$scope.parentListIndex].processedShareAmt;
					  }
				
		  }*/
		  
		  
		  $scope.validateSplitShareAmtPopup = function(item,idx)
		  {
			  
			  if(item.newDoctorRevenue == '' || item.newDoctorRevenue == undefined || item.newDoctorRevenue == null){
				  item.newDoctorRevenue  = parseFloat(0);
			  }
			  
			  var tmpSA = parseFloat(0);
			  
			  angular.forEach($scope.splitDocShareList,function(value,index){
					if(idx != index){
						tmpSA = tmpSA + parseFloat(value.newDoctorRevenue);
					}
					});
			
			  
			  //test
			  
			  var tmpOldShrAmt = $scope.splitDocShareList[idx].oldDoctorRevenue;
			  
			  var spliAmt = item.newDoctorRevenue;
			  var tmpSub  = parseFloat($scope.doctorShareProcessList[$scope.parentListIndex].netAmount) - parseFloat(tmpSA);
			
			  
			  //Test
			  
			  if(parseFloat(spliAmt) > parseFloat(tmpSub))
					  {
						   growl.warning("You can add more "+parseFloat(tmpSub),{
								title : "WARNING"
							});
						   $scope.splitDocShareList[idx].newDoctorRevenue = item.oldDoctorRevenue;
							return false;
					  }
				  else 
					  {
					  
					  var tmpOldShrAmt = $scope.splitDocShareList[idx].oldDoctorRevenue;
					  
					  var hosShr = parseFloat(tmpSub) + parseFloat($scope.splitDocShareList[idx].oldDoctorRevenue);
					  
					  //if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
						 	//if($scope.referralShareAmountPopupOld > $scope.referralShareAmountPopup)
					  
					   // var spliPer = (item.shareAmount/$scope.doctorShareProcessList[$scope.parentListIndex].unitRate)*100;
					  var spliPer = (item.newDoctorRevenue/$scope.doctorShareProcessList[$scope.parentListIndex].netAmount)*100;
					    //var spliAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].unitRate * item.newSharePercentage)/100;
					    
					    
					    $scope.splitDocShareList[idx].newRevenuePercentage = parseFloat(spliPer).toFixed(2);
					    $scope.splitDocShareList[idx].newRevenuePercentage =parseFloat(spliPer).toFixed(2);
					    $scope.splitDocShareList[idx].oldDoctorRevenue = item.newDoctorRevenue;
					
					 // $scope.doctorShareAmountPopup = 0;
					  var docShrAmtPopUp = parseFloat(0);
					  var tempPerMainDoctor = parseFloat(0);
					  var tempAmtMainDoctor = parseFloat(0);
					  angular.forEach($scope.splitDocShareList,function(value,index){
							// $scope.doctorShareAmountPopup =$scope.doctorShareAmountPopup + value.shareAmount;
							 docShrAmtPopUp =docShrAmtPopUp + parseFloat(value.newDoctorRevenue);
							 if(index == 0)
					    	 {
					    	 tempPerMainDoctor = parseFloat(value.newRevenuePercentage);
					    	 tempAmtMainDoctor =parseFloat(value.newDoctorRevenue);
					    	 }
							});
					  
				  			
					  $scope.doctorShareAmountPopup = parseFloat(docShrAmtPopUp).toFixed(2);
					     $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue = $scope.doctorShareAmountPopup;
						 
					
						 
							 
						 
					  }
				
		  }
		  
		  $scope.calculateAmtFromPer = function(item,idx)
		  {
			  console.log("ITEM",item);
			  console.log(idx);
			  if(item.doctorShareAmt > item.netAmount)
				  {
				  			
				          $scope.doctorShareProcessList[idx].sharePer = item.oldSharePer;
				          $scope.doctorShareProcessList[idx].doctorShareAmt = ((item.netAmount*item.oldSharePer)/100).toFixed(2);
				          $scope.doctorShareProcessList[idx].hospShare = item.oldHospShare;
						  growl.warning("Doctor share amount should be less then net amount..!!",{
								title : "WARNING"
							});
							
							return false;
				  }
			  else
				  {
				    var per = (item.doctorShareAmt/item.netAmount) * 100;
				    $scope.doctorShareProcessList[idx].sharePer = per;
				    var tempAmt = (item.netAmount*item.oldSharePer)/100;
				    //$scope.doctorShareProcessList[idx].oldSharePer = per;
				    if(item.doctorShareAmt > tempAmt)
				    	{
				    			var diff = item.doctorShareAmt - tempAmt;
				    			 $scope.doctorShareProcessList[idx].hospShare = (item.hospShare - diff).toFixed(2);
				    			 $scope.doctorShareProcessList[idx].dueShareAmt = item.doctorShareAmt;
				    	}
				    if(tempAmt > item.doctorShareAmt)
			    	{
			    			var diff =  tempAmt - item.doctorShareAmt;
			    			 $scope.doctorShareProcessList[idx].hospShare = (item.hospShare + diff).toFixed(2);
			    			 $scope.doctorShareProcessList[idx].dueShareAmt = item.doctorShareAmt;
			    	}
				    if(tempAmt == item.doctorShareAmt)
			    	{
			    			var diff =  tempAmt - item.doctorShareAmt;
			    			 $scope.doctorShareProcessList[idx].hospShare = (item.oldHospShare + diff).toFixed(2);
			    			 $scope.doctorShareProcessList[idx].dueShareAmt = item.doctorShareAmt;
			    	}
				    
				  }
			 
		  }
		  
		  /*$scope.calculateSplitShare = function(){
			  var t = (($scope.currRefShare.netAmount*$scope.currRefShare.sharePer)/100).toFixed(2);
			  if($scope.splitDocShare.amount > t){
				  growl.error("Percentage should be less than assign doctor share percentage.",{
						title : ERROR_MSG
					});
				  $scope.splitDocShare.sharePer = 0;
				  $scope.splitDocShare.amount = 0;
				  return false;
			  }
			  else
				  {
				  
				  $scope.splitDocShare.netAmount = $scope.currRefShare.netAmount;
				  $scope.splitDocShare.sharePer = ($scope.splitDocShare.amount/t)*100;
				  }
			 
		  }*/
		  
		  
		  $scope.calculateSplitSharePer = function(){
			  if($scope.splitDocShare.sharePer > 0){
			 /* var splitAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].unitRate *  $scope.splitDocShare.sharePer)/100;*/
				  var splitAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].netAmount *  $scope.splitDocShare.sharePer)/100;
			  var hosShr = $scope.hospitalShareAmountPopup;
			  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
				  {
				  //hosShr = hosShr + $scope.referralShareAmountPopup;
				  hosShr = parseFloat(hosShr) + parseFloat($scope.referralShareAmountPopup);
				  }
			  if(splitAmt > hosShr){
				  growl.warning("You have only "+hosShr,{
						title : WARNING_MSG
					});
				  $scope.splitDocShare.sharePer = 0;
				  $scope.splitDocShare.amount = 0;
				  $scope.splitDocShare.doctorId = '';
				  return false;
			  }
			  else
				  {
				  
				  $scope.splitDocShare.amount = splitAmt;
				 // $scope.splitDocShare.sharePer = $scope.splitDocShare.sharePer;
				  }
		  }
		  }
		  
		  $scope.calculateSplitShareAmt = function()
		  {
			  if($scope.splitDocShare.amount > 0){
			  //var splitPer = ($scope.splitDocShare.amount/$scope.doctorShareProcessList[$scope.parentListIndex].unitRate)*100;
			   var splitPer = ($scope.splitDocShare.amount/$scope.doctorShareProcessList[$scope.parentListIndex].netAmount)*100;
			  
			  var hosShr = $scope.hospitalShareAmountPopup;
			  var splicDocAmt = parseFloat($scope.splitDocShare.amount);
			  
			  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
			  {
			  hosShr = parseFloat(hosShr) + parseFloat($scope.referralShareAmountPopup);
			  }
			  
			  if(splicDocAmt > hosShr){
				  growl.warning("You have only "+hosShr,{
						title : WARNING_MSG
					});
				  $scope.splitDocShare.sharePer = 0;
				  $scope.splitDocShare.amount = 0;
				  $scope.splitDocShare.doctorId = '';
				  return false;
			  }
			  else
				  {
				  
				 // $scope.splitDocShare.amount = $scope.splitDocShare.amount;
				  $scope.splitDocShare.sharePer = parseFloat(splitPer).toFixed(2);
				  }
		  }
		  }
		  
		  
		  
		  $scope.addSplitDocShare = function(){
			  
			  if($scope.splitDocShare.sharePer > 0 || $scope.splitDocShare.amount > 0){
				  
			  if($scope.splitDocShare.doctorId != null && $scope.splitDocShare.doctorId != ""){

				  var existingDoctors = [];
				  
				  $scope.splitDocShareList.forEach((elem) =>{existingDoctors.push(elem.docId.toString())});
				  
				  if(existingDoctors.indexOf($scope.splitDocShare.doctorId) != -1){
					  growl.warning("Doctor already exists",{
							title : WARNING_MSG
						});
					  $scope.splitDocShare.doctorId = '';
					  $scope.splitDocShare.sharePer = 0;
					  $scope.splitDocShare.amount = 0;
					  return;
				  }
				  
				  
				  
				  
				  
				  $scope.doctorList.forEach((elem) => {
					  	if(elem.doctorId.toString() == $scope.splitDocShare.doctorId.toString()){
					  		$scope.splitDocShare.doctorName = elem.firstName + " " + elem.lastName;
					  	}
					  })
					  
					 
				  /*$scope.splitDocShare.sharePer = ($scope.splitDocShare.amount / $scope.currRefShare.netAmount) * 100;
				  
				   $scope.currRefShare.dueShareAmt = $scope.currRefShare.dueShareAmt - $scope.splitDocShare.amount;
				   if( $scope.currRefShare.dueShareAmt > 0)
					   {
					   $scope.currRefShare.sharePer = $scope.currRefShare.sharePer - $scope.splitDocShare.sharePer; 
					   }
				   else
					   {
					   $scope.currRefShare.sharePer = 0; 
					   }*/
					  var tmpDi  =parseFloat( $scope.doctorShareProcessList[$scope.parentListIndex].netAmount) - parseFloat($scope.doctorShareAmountPopup);
					  if(parseFloat( $scope.doctorShareProcessList[$scope.parentListIndex].netAmount) == parseFloat($scope.doctorShareAmountPopup)){
						  growl.warning("You have no amount to add.",{
								title : WARNING_MSG
							});
						  return;
					  }
					  else if (parseFloat( $scope.doctorShareProcessList[$scope.parentListIndex].netAmount) > parseFloat($scope.doctorShareAmountPopup))
					  {
						  if(parseFloat($scope.splitDocShare.amount) > parseFloat(tmpDi)){
							  growl.warning("You have only "+parseFloat(tmpDi).toFixed(2),{
									title : WARNING_MSG
								});
							  return;
						  }else{
							  
						  var data = {
						   docId : $scope.splitDocShare.doctorId,
						   doctorName : $scope.splitDocShare.doctorName,
						   newRevenuePercentage : $scope.splitDocShare.sharePer,
						   oldRevenuePercentage : $scope.splitDocShare.sharePer,
						   newDoctorRevenue : $scope.splitDocShare.amount,
						   oldDoctorRevenue : $scope.splitDocShare.amount,
						   doctorShareSplitId : null,
						   billDtlId : $scope.splitDocShare.billDtlId,
				   }
				   
				  $scope.splitDocShareList.push(data);
				  
				  var tempPerMainDoctor = parseFloat(0);
				  var tempAmtMainDoctor = parseFloat(0);
				  angular.forEach($scope.splitDocShareList,function(value,index){
					     if(index == 0)
					    	 {
					    	 tempPerMainDoctor = parseFloat(value.newRevenuePercentage);
					    	 tempAmtMainDoctor =parseFloat(value.newRevenuePercentage);
					    	 }
						});
				  
				  
				  
					 
				  var docShrAmtPopup = parseFloat($scope.doctorShareAmountPopup) + parseFloat($scope.splitDocShare.amount);
				  $scope.doctorShareAmountPopup = (docShrAmtPopup).toFixed(2);
				  
					 $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue = $scope.doctorShareAmountPopup;
					 
				   $scope.initSplitDocShare();
					  }
			  }
			  }else{	
				  growl.warning("Please select a doctor",{
						title : WARNING_MSG
					});
				  return;
			  }
		  }
			  else
				  {
					  growl.warning("Percentage Or amount should be greater then ZERO(0).",{
							title : WARNING_MSG
						});
					  return;
				  
				  }
		  }
		  
		  $scope.removeSplitShare = function(item,index){
			 
			  $scope.inactiveObj = angular.copy(item);
			  $scope.inactiveObjIndex = index;
			  
			  $('#popupForInactive').modal('show');
			 
			  
		  }
		  
		  $scope.inactiveRecord = function()
		  {
			 var item =  $scope.inactiveObj;
			  var index = $scope.inactiveObjIndex;
			  
			  if(item.doctorShareSplitId > 0 && item.doctorShareId > 0)
				  {
				  
				  

				  $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue = parseFloat( $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue) -  parseFloat(item.newDoctorRevenue);
				 
						  var data = {
								  doctorShareSplitId : item.doctorShareSplitId,
								  billDtlId : item.billDtlId,
								  doctorSlabTransactionId : $scope.doctorShareProcessList[$scope.parentListIndex].doctorSlabTransactionId,
								  doctorRevenue : $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue
						  }
						  console.log("data",data);
						// return false;
						    var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/doctor/inactive";
						    BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success"){
									
									var docShrAmtPopup = parseFloat($scope.doctorShareAmountPopup) + parseFloat(item.newDoctorRevenue);
									  $scope.doctorShareAmountPopup = (docShrAmtPopup).toFixed(2);
									  
										 
									  $('#popupForInactive').modal('hide');
									$rootScope.stopLoader();
									growl.success("Inactive Successfully..!!",{
										title : SUCCESS_MSG
									});
									return;
								}
								else
									{
									   $rootScope.stopLoader();
									   growl.error("Problem with inactive..!!",{
											title : ERROR_MSG
										});
										return;
									}
							});
				  }
			  else
				  {
				  
					  var docShrAmtPopup = parseFloat($scope.doctorShareAmountPopup) - parseFloat(item.newDoctorRevenue);
					  $scope.doctorShareAmountPopup = (docShrAmtPopup).toFixed(2);
					  
					  $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue = parseFloat( $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue) -  parseFloat(item.newDoctorRevenue);
					  $scope.splitDocShareList.splice(index,1);
					  $('#popupForInactive').modal('hide');
				  }
		  }
		  
		  $scope.validateSplitShare = function(splitDoc){
			  var totalSplitAmt = (($scope.currRefShare.netAmount*$scope.currRefShare.sharePer)/100);
				$scope.splitDocShareList.forEach(function(value){
					totalSplitAmt = parseFloat(totalSplitAmt) + parseFloat(value.docShare);
				});
				
				var currentShare = parseFloat((parseFloat(splitDoc.sharePer) / 100 ) * $scope.currRefShare.netAmount);
				totalSplitAmt = parseFloat(totalSplitAmt) + parseFloat(currentShare);
				if(parseFloat(totalSplitAmt) > parseFloat($scope.currRefShare.netAmount)){
					growl.warning("You are exceeding total amount",{
						title : WARNING_MSG
					});
					splitDoc.sharePer = 0;
					splitDoc.docShare = 0;
					return;
				}
		  }
		  
		  $scope.validateSplitShareViv = function(splitDoc,index)
		  {
			  console.log( $scope.splitDocShareList);
			 var totalSplitAmt = (($scope.currRefShare.netAmount*$scope.currRefShare.sharePer)/100);
				$scope.splitDocShareList.forEach(function(value){
					totalSplitAmt = parseFloat(totalSplitAmt) + parseFloat(value.docShare);
				});
				
				//var currentShare = parseFloat((parseFloat(splitDoc.sharePer) / 100 ) * $scope.currRefShare.netAmount);
				splitDoc.sharePer = (splitDoc.docShare/$scope.currRefShare.netAmount)*100;
				
				totalSplitAmt = parseFloat(totalSplitAmt) + parseFloat(splitDoc.docShare);
				if(parseFloat(totalSplitAmt) > parseFloat($scope.currRefShare.netAmount)){
					growl.warning("You are exceeding total amount",{
						title : WARNING_MSG
					});
					splitDoc.sharePer = 0;
					splitDoc.docShare = 0;
					return;
				}
		  }
		  
		  $scope.validateCurrSplitShare = function(){
			  var totalSplitAmt = (($scope.currRefShare.netAmount*$scope.currRefShare.sharePer)/100);
				$scope.splitDocShareList.forEach(function(value){
					totalSplitAmt = parseFloat(totalSplitAmt) + parseFloat(value.docShare);
				});
				
				if(parseFloat(totalSplitAmt) > parseFloat($scope.currRefShare.netAmount)){
					growl.warning("You are exceeding total amount",{
						title : WARNING_MSG
					});
					$scope.currRefShare.sharePer = 0;
					$scope.currRefShare.docShare = 0;
					return;
				}	
		  }
		  
		  /*$scope.saveSplitShare = function(){
			  if($scope.splitDocShareList.length > 0){
				  var currDocShare = (($scope.currRefShare.netAmount * $scope.currRefShare.sharePer)/100);
				  var currDueShare = parseFloat(currDocShare) - parseFloat($scope.currRefShare.processedShareAmt);
				  var totalSplitAmt = currDocShare;
					$scope.splitDocShareList.forEach(function(value){
						totalSplitAmt = parseFloat(totalSplitAmt) + parseFloat(value.amount);
					});
				  var currHospShare = parseFloat($scope.currRefShare.netAmount) - parseFloat(totalSplitAmt); 
				  //current doctor share
				  var doctorShare = {docId:$scope.currRefShare.docId,doctorShareId:$scope.currRefShare.doctorShareId,
						  docShare:currDocShare,dueShareAmt:currDueShare,hospShare:currHospShare,sharePer:$scope.currRefShare.sharePer};
				  
				  //splitted doctor shares
				  var splittedShares = [];
				  $scope.splitDocShareList.forEach(function(value){
					let temp = {docShare:value.docShare,docId:value.doctorId,sharePer:value.sharePer,docShare:value.docShare,docSpecId:value.specialityId};
					splittedShares.push(temp);
				  });
				  
				  var param = {listTDoctorShareRespDto :splittedShares,tDoctorShareRespDto:doctorShare,orgId:$scope.orgId,orgUnitId:$scope.unitId};
				  console.log("Param",param);
				  $rootScope.startLoader();
				  var URI = BASE_URL + ROOT_URL + "/api/billing/saveDoctorShareSplit";
				  BillingGenericService.serviceAction(METHOD_POST, URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						if (response.data.status == "success"){
							$scope.splitDoctorList = [];
							$scope.currRefShare = {};
							$scope.getdoctorShareProcesssList();
							$('#roomRentModal').modal('hide');
							$rootScope.stopLoader();
						}else{
							$rootScope.stopLoader();
						}
				  });
				  
				  
			  }else{
				  growl.error("Please add at least one doctor",{
						title : ERROR_MSG
					});
			  }
		  }*/
		  
		  $scope.saveSplitShare = function(){
			  console.log("$scope.splitDocShareList",$scope.splitDocShareList);
			  console.log($scope.doctorShareListSingle);
			  
			  if($scope.splitDocShareList.length > 0){
				  
				  
				  //current doctor share
				  var doctorShare = { doctorSlabTransactionId: $scope.doctorShareProcessList[$scope.parentListIndex].doctorSlabTransactionId,
						  doctorRevenue : $scope.doctorShareProcessList[$scope.parentListIndex].doctorRevenue,
						  };
				  
				  
				  //splitted doctor shares
				  var splittedShares = [];
				  $scope.splitDocShareList.forEach(function(value){
					let temp = {
							newDoctorRevenue:value.newDoctorRevenue,
							docId:value.docId,
							newRevenuePercentage:value.newRevenuePercentage,
							doctorSlabTransactionId : value.doctorShareSplitId,
 							};
					
					splittedShares.push(temp);
				  });
				  
				  var param = {
						  listTDoctorShareRespDto :splittedShares,
						  tDoctorShareRespDto:doctorShare,
						  orgId:$scope.orgId,
						  orgUnitId:$scope.unitId,
						  };
				  
				  console.log("Param",param);
				  console.log("Param",JSON.stringify(param));
				// return false;
				  $rootScope.startLoader();
				  var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/splitRevenue";
				  BillingGenericService.serviceAction(METHOD_POST, URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						if (response.data.status == "success"){
							$scope.splitDoctorList = [];
							$scope.currRefShare = {};
							//$scope.getdoctorShareProcesssList();
							// $scope.calculateAmounts();
							$('#roomRentModal').modal('hide');
							$rootScope.stopLoader();
						}else{
							$rootScope.stopLoader();
						}
				  });
				  
				  
			  }else{
				  growl.warning("Please add at least one doctor",{
						title : WARNING_MSG
					});
			  }
		  }
		
	}catch(e){
		console.log(e.message);
	}
	
}]);