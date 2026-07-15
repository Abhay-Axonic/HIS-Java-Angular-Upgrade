angular.module("myApp").controller("doctorShareProcessController", [ '$scope','$rootScope','BillingGenericService','PagerService','$cookies','$state','growl','GenericService',
    function($scope,$rootScope,BillingGenericService,PagerService,$cookies,$state,growl,GenericService){
    	try{
		$rootScope.loginpage = true;

		/**Variables for pagination */
        $scope.offset = 0;
        $scope.noOfRecordsPerPage = 10;
        $scope.commonList = [];
        $scope.totalNoOfRecords;
        $scope.searchKeyword;
        $scope.commonListCount;

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
		 $scope.serviceRendered = 2;
		/**DatePicker Code  */
		
		$scope.fromDate = moment().startOf('day').toDate();
	    $scope.toDate = moment().startOf('day').toDate();
		$scope.fromDatePopup = moment().startOf('day').toDate();
	    $scope.toDatePopup = moment().startOf('day').toDate();
	    $scope.completed='N';
		$scope.pending = 'N';
		$scope.outsourced = 'N';
		$scope.disableInsurance = true;
		$scope.settlementOrInvoice=1;
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
		$scope.isSaveButtonDisabled = true;	
		$scope.click=false;
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
				$scope.splitDocShare = {specialityId:"",doctorId:"",sharePer:"",amount:0.00,doctorName:""};
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
				  $scope.serviceRendered = 2;
		  }
		  $scope.initDoctorShareProcessObj = function(){
			   $scope.doctorShareProcessObj =$scope.doctorShareProcessObj = {orgId:$scope.orgId,"visitTypeId":"","fromDate":"","toDate":"","search":"","companyTypeId":"","companyId":"","associateCompanyId":""};
			  //,unitId:$scope.unitId
		  }
		  $scope.initSplitDocShare = function(){
			  $scope.splitDocShare = {specialityId:"",doctorId:"",sharePer:"",amount:0.00,doctorName:""};
		  }
		  


            /**List And Pagination Start**/
              $scope.setNoOfRecords = function() {
                  $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
              };

              $scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
              {
                  $rootScope.startLoader();
                  var fromDate= $scope.fromDate==undefined?"":moment($scope.fromDate.getTime()).format('YYYY-MM-DD 00:00:00');
                  var toDate=$scope.toDate==undefined?"":moment($scope.toDate.getTime()).format('YYYY-MM-DD 24:00:00');

                  offset = offset != null ? offset : 0;
                  noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;

                  var param = {
                            orgId:$scope.orgId,
                            orgUnitId:$scope.unitId,
                            fromDate:fromDate,
                            toDate:toDate,
                            patientId:$scope.patientId != undefined ? $scope.patientId:null,
                            doctorIds:$scope.doctorListForSearch,
                            orderServiceId :$scope.orderServiceId,
                            billNumber : $scope.billNumber,
                          //  isProcessShareAgainstSettledBill : $scope.isProcessShareAgainstSettledBill,
                            billStatusId : $scope.billStatusId,
                            serviceRendered:$scope.serviceRendered,
                            settlementOrInvoice : $scope.settlementOrInvoice,
                            offset:$scope.offset,
                            recordPerPage:$scope.noOfRecordsPerPage
                    };

                      var param1 = {
                               orgId:$scope.orgId,
                               orgUnitId:$scope.unitId,
                               fromDate:fromDate,
                               toDate:toDate,
                               patientId:$scope.patientId != undefined ? $scope.patientId:null,
                               doctorIds:$scope.doctorListForSearch,
                               orderServiceId :$scope.orderServiceId,
                               billNumber : $scope.billNumber,
                             //  isProcessShareAgainstSettledBill : $scope.isProcessShareAgainstSettledBill,
                               billStatusId : $scope.billStatusId,
                               serviceRendered:$scope.serviceRendered,
                               settlementOrInvoice : $scope.settlementOrInvoice
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

                      var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareListCount"
                         BillingGenericService.serviceAction(METHOD_POST,URI,param1,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                             if (response.data.status == "success"){

                                  $scope.commonListCount = response.data.object;
                                  $scope.setPage(1,false);
                                 // $scope.initCalculateAmounts();
                                  console.log("$scope.commonListCount========",JSON.stringify($scope.commonListCount));
                             }
                             $rootScope.stopLoader();

                         });

              }
//
//              $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);


            $scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {

                    $rootScope.startLoader();
                    var fromDate= $scope.fromDate==undefined?"":moment($scope.fromDate.getTime()).format('YYYY-MM-DD 00:00:00');
                    var toDate=$scope.toDate==undefined?"":moment($scope.toDate.getTime()).format('YYYY-MM-DD 24:00:00');
                    console.log("offset", offset);
                    offset = offset != null ? offset : 0;
                    noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;

                    var param = {
                            orgId:$scope.orgId,
                            orgUnitId:$scope.unitId,
        //                            visitTypeId:$scope.doctorShareProcessObj.visitTypeId,
                            fromDate:fromDate,
                            toDate:toDate,
                            patientId:$scope.patientId != undefined ? $scope.patientId:null,
                            doctorIds:$scope.doctorListForSearch,
                            orderServiceId :$scope.orderServiceId,
                            billNumber : $scope.billNumber,
                            billStatusId : $scope.billStatusId,
                            serviceRendered:$scope.serviceRendered,
                            settlementOrInvoice : $scope.settlementOrInvoice,
                            offset:offset,
                            recordPerPage:noOfRecordsPerPage
                    };

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

            $scope.pager = {};
            $scope.page;

            $scope.setPage = function(page, flag) {
                if (page < 1 || page > $scope.pager.totalPages)
                {
                    return;
                }
                $scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
                if (flag)
                {
                    $scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
                }
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
									$scope.docShareListToCheckCredit=response.data.listObject;
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
			    var URI = BASE_URL + ROOT_URL + "/api/billing/doctorShare/split/details/"+docShrId;
			    BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.splitDetailsNewList = response.data.listObject;
						 console.log("$scope.splitDetailsNewList",JSON.stringify($scope.splitDetailsNewList));
					}
					$rootScope.stopLoader();
					
				});
		  }
		  
		  
		  $scope.getCurrentDoctor = function(item,idx)
		  {
			  
			  $scope.primaryDoctorShareObject = angular.copy(item);
			  $scope.primaryDoctorShareObjectIndex = idx;
			  $scope.changeSpecialityId =  item.docSpecId; 
			  $scope.changeDoctorId =item.docId;
			  $scope.getSplitDoctorBySpeciality(item.docSpecId);
			  angular.element('#doctorChangeModol').modal('show');
			  
		  }
		  
		  $scope.changeDoctorForPrimary = function()
		  {
			  $rootScope.startLoader();
			  var data = {
					  	docId:$scope.changeDoctorId,
						docSpecId:$scope.changeSpecialityId,
						doctorShareSplitId : $scope.primaryDoctorShareObject.doctorShareSplitId,
						doctorShareId : $scope.primaryDoctorShareObject.doctorShareId,
			  }
			  console.log("data",data);
			 
			    var URI = BASE_URL + ROOT_URL + "/api/billing/process/doctorShare/doctor/change";
			    BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						
						
						$scope.splitDocShareList[$scope.primaryDoctorShareObjectIndex].docId=$scope.changeDoctorId;
						$scope.splitDocShareList[$scope.primaryDoctorShareObjectIndex].docSpecId=$scope.changeSpecialityId;
						$scope.doctorShareProcessList[$scope.parentListIndex].docSpecId = $scope.changeSpecialityId;
						$scope.doctorShareProcessList[$scope.parentListIndex].docId = $scope.changeDoctorId; 
						
						var specObjName = $.grep($scope.specialityList, function (spec) {
				   	           return spec.specialityId== $scope.changeSpecialityId;
				   	       })[0].specialityName;
						
						var docObjName = $.grep($scope.splitDoctorList, function (doctorObj) {
				   	           return doctorObj.doctorId== $scope.changeDoctorId;
				   	       })[0];
						var finalDocName = docObjName.firstName+' '+docObjName.lastName;
						
						$scope.splitDocShareList[$scope.primaryDoctorShareObjectIndex].doctorName =finalDocName;
						$scope.doctorShareProcessList[$scope.parentListIndex].doctorName =finalDocName;
						$scope.doctorShareProcessList[$scope.parentListIndex].specialityName =specObjName;
						
						angular.element('#doctorChangeModol').modal('hide');
						$rootScope.stopLoader();
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
								  serviceRendered : $scope.serviceRendered,
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
								//  isProcessShareAgainstSettledBill : $scope.isProcessShareAgainstSettledBill,
								  billStatusId : $scope.billStatusId,
								  serviceRendered:$scope.serviceRendered,
								  companyTypeId : $scope.doctorShareProcessObj.companyTypeId,
								  companyId : $scope.doctorShareProcessObj.companyId,
								  associateCompanyId : $scope.doctorShareProcessObj.associateCompanyId,
								  settlementOrInvoice : $scope.settlementOrInvoice,
                                  offset:$scope.offset,
                                  recordPerPage:$scope.noOfRecordsPerPage
					      };

				var param1 = {
                          orgId:$scope.orgId,
                          orgUnitId:$scope.unitId,
                          visitTypeId:$scope.doctorShareProcessObj.visitTypeId,
                          fromDate:fromDate,
                          toDate:toDate,
                          patientId:$scope.patientId != undefined ? $scope.patientId:null,
                          doctorIds:$scope.doctorListForSearch,
                          orderServiceId :$scope.orderServiceId,
                          billNumber : $scope.billNumber,
                        //  isProcessShareAgainstSettledBill : $scope.isProcessShareAgainstSettledBill,
                          billStatusId : $scope.billStatusId,
                          serviceRendered:$scope.serviceRendered,
                          companyTypeId : $scope.doctorShareProcessObj.companyTypeId,
                          companyId : $scope.doctorShareProcessObj.companyId,
                          associateCompanyId : $scope.doctorShareProcessObj.associateCompanyId,
                          settlementOrInvoice : $scope.settlementOrInvoice
                  };

			     if($scope.searchKeyword != undefined && $scope.searchKeyword != undefined && $scope.searchKeyword != undefined ){
              				  	  param.patientName = $scope.searchKeyword;
              				  	    param1.patientName = $scope.searchKeyword;
              			  }
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


                var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareListCount"
                BillingGenericService.serviceAction(METHOD_POST,URI,param1,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                    if (response.data.status == "success"){
                         $scope.commonListCount = response.data.object;
                         $scope.setPage(1,false);
                    }
                        $rootScope.stopLoader();
                });
			  }
		  
		  $scope.getdoctorShareProcesssListForSearchFilter = function(serchKey) {
			  $scope.selectedAll = false;
			 var fromDate= $scope.fromDate==undefined?"":moment($scope.fromDate.getTime()).format('YYYY-MM-DD 00:00:00');
			  var toDate=$scope.toDate==undefined?"":moment($scope.toDate.getTime()).format('YYYY-MM-DD 24:00:00');
				
				if ($scope.selectedDoctorList.length == 0){
					growl.warning("Please select doctor.",{
						title : WARNING_MSG
					});
					
					return;
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
								  serviceRendered:$scope.serviceRendered,
								  patientName : serchKey,
								  settlementOrInvoice : $scope.settlementOrInvoice
					      };

			 var param1 = {
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
                      serviceRendered:$scope.serviceRendered,
                      patientName : serchKey,
                      settlementOrInvoice : $scope.settlementOrInvoice
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

				var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareListCount"
                    BillingGenericService.serviceAction(METHOD_POST,URI,param1,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                        if (response.data.status == "success"){
                             $scope.commonListCount = response.data.object;
                             $scope.setPage(1,false);
                        }
                            $rootScope.stopLoader();
                    });
			  }
		  
		
		  $scope.changeStatus = function(settlementOrInvoice){
			  
			  if(settlementOrInvoice ==1 ){
				  $scope.billStatusId =3;
			  }
			  
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
				  
				    var finaltotal=0;
				    var total = 0;
					var totalAmount=0;
					var discountAmt=0;
					var disallowedAmountAmt=0;
					var doctorSharAmt=0;
					var hosSharAmt=0;
					var dueShareAmt = 0;
					var processedShareAmt = 0;
					
					total = parseFloat(total) + (isNaN(docShare.netAmount) ? 0 : parseFloat(docShare.netAmount));
					totalAmount = parseFloat(totalAmount) + (isNaN(docShare.totalAmount) ? 0 : parseFloat(docShare.totalAmount));
					discountAmt = parseFloat(discountAmt) + (isNaN(docShare.discount) ? 0 : parseFloat(docShare.discount));
					disallowedAmountAmt = parseFloat(disallowedAmountAmt) + (isNaN(docShare.disallowedAmount) ? 0 : parseFloat(docShare.disallowedAmount));
					//doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
					processedShareAmt =  parseFloat(processedShareAmt) +(isNaN(docShare.dueShareAmt) ? 0 : parseFloat(docShare.dueShareAmt));
					doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN(docShare.docShare) ? 0 : parseFloat(docShare.docShare));
                    docShare.tempProcessedShareAmt = (isNaN(docShare.dueShareAmt) ? 0 : parseFloat(docShare.dueShareAmt));
					//docShare.tempProcessedShareAmt = (isNaN(docShare.dueShareAmt) ? 0 : parseFloat(docShare.dueShareAmt));
					docShare.dueShareAmt = 0;
					hosSharAmt = parseFloat(hosSharAmt) + (isNaN(docShare.hospShare) ? 0 : parseFloat(docShare.hospShare));
					dueShareAmt  = parseFloat(dueShareAmt) + (isNaN(docShare.dueShareAmt) ? 0 : parseFloat(docShare.dueShareAmt));
					
					$scope.total =  parseFloat($scope.total) + parseFloat(totalAmount);
					$scope.grandTotal = parseFloat($scope.grandTotal) + parseFloat(total);
					$scope.discountAmt =parseFloat($scope.discountAmt) + parseFloat(discountAmt);
					$scope.disallowedAmount = parseFloat($scope.disallowedAmount) + parseFloat(disallowedAmountAmt);
					$scope.doctorSharAmt =parseFloat($scope.doctorSharAmt) + parseFloat(doctorSharAmt);
					$scope.hosSharAmt = parseFloat($scope.hosSharAmt) + parseFloat(hosSharAmt);
					$scope.processedShareAmt = parseFloat($scope.processedShareAmt) + parseFloat(processedShareAmt);
					$scope.dueShareAmt = parseFloat($scope.dueShareAmt) + parseFloat(dueShareAmt);
					
					
			  }else{
				  var finaltotal=0;
				    var total = 0;
					var totalAmount=0;
					var discountAmt=0;
					var disallowedAmountAmt=0;
					var doctorSharAmt=0;
					var hosSharAmt=0;
					var dueShareAmt = 0;
					var processedShareAmt = 0;
					
					total = parseFloat(total) + (isNaN(docShare.netAmount) ? 0 : parseFloat(docShare.netAmount));
					totalAmount = parseFloat(totalAmount) + (isNaN(docShare.totalAmount) ? 0 : parseFloat(docShare.totalAmount));
					discountAmt = parseFloat(discountAmt) + (isNaN(docShare.discount) ? 0 : parseFloat(docShare.discount));
					disallowedAmountAmt = parseFloat(disallowedAmountAmt) + (isNaN(docShare.disallowedAmount) ? 0 : parseFloat(docShare.disallowedAmount));
					//doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
					
					doctorSharAmt = parseFloat(doctorSharAmt) + (isNaN(docShare.docShare) ? 0 : parseFloat(docShare.docShare));
					
					docShare.dueShareAmt = docShare.tempDueShare;
					docShare.tempProcessedShareAmt  = 0;
					//processedShareAmt =  parseFloat(processedShareAmt) +(isNaN(docShare.dueShareAmt) ? 0 : parseFloat(docShare.dueShareAmt));
					hosSharAmt = parseFloat(hosSharAmt) + (isNaN(docShare.hospShare) ? 0 : parseFloat(docShare.hospShare));
					dueShareAmt  = parseFloat(dueShareAmt) + (isNaN(docShare.dueShareAmt) ? 0 : parseFloat(docShare.dueShareAmt));
					
					$scope.total =  parseFloat($scope.total) - parseFloat(totalAmount);
					$scope.grandTotal = parseFloat($scope.grandTotal) - parseFloat(total);
					$scope.discountAmt =parseFloat($scope.discountAmt) - parseFloat(discountAmt);
					$scope.disallowedAmount = parseFloat($scope.disallowedAmount) - parseFloat(disallowedAmountAmt);
					$scope.doctorSharAmt =parseFloat($scope.doctorSharAmt) - parseFloat(doctorSharAmt);
					$scope.hosSharAmt = parseFloat($scope.hosSharAmt) - parseFloat(hosSharAmt);
					$scope.processedShareAmt = parseFloat($scope.processedShareAmt) - parseFloat(dueShareAmt);
					$scope.dueShareAmt = parseFloat($scope.dueShareAmt);
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
			$scope.$watch('billServicesList', function(newVal, oldVal) {  
			    var valuesEntered = false;
		    angular.forEach(newVal, function(value) {
	        if (value.doctorSharePopup && value.doctorId > 0 && value.docSpecialityId > 0) {
	            valuesEntered = true;
				$scope.isSaveButtonDisabled=false;
				$scope.click=false;
	        }
	    });
	    $scope.isSaveButtonDisabled = !valuesEntered;
			}, true);
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
					var co = 1;
					angular.forEach(
									$scope.doctorShareProcessList,
									function(doctorShareProcessObj) {
										
										if(doctorShareProcessObj.doctorShare){
										doctorShareProcessObj.dueShareAmt = doctorShareProcessObj.dueShareAmt + doctorShareProcessObj.tempProcessedShareAmt;
										doctorShareProcessObj.tempProcessedShareAmt = 0;
										}
										doctorShareProcessObj.doctorShare = $scope.selectedAll;
										if(co==$scope.doctorShareProcessList.length){
										 $scope.checkCalculateAmounts(doctorShareProcessObj);
										}
										co++;
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
                   /*  $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = $scope.doctorShareListSingle.dueShareAmt -  $scope.doctorShareListSingle.processedShareAmt;
                     $scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt = $scope.doctorShareListSingle.dueShareAmt;*/
					 $scope.processedShareAmt = parseFloat($scope.processedShareAmt) + parseFloat($scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt);
					// $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = 0;
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
				
				$scope.searchOnlyAuthorisedDoctor = function(){
					$scope.selectedDoctorList = $scope.selectedDoctorListTemp;
					$('#DoctorShareConfirmSave').modal('hide');
					$scope.getdoctorShareProcesssList();
				}
				$scope.closePopup = function(){
					$('#DoctorShareConfirmSave').modal('hide');
				}
				
				
		  $scope.selectedDoctorListTemp = [];
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
						  isCheckProcessAmountZero=checkCheckProcessAmountZeroOrNot($scope.doctorShareProcessList)
							if (isCheckProcessAmountZero) {
								
								growl.warning("Processed share amount should be greater then zero(0)",{
									title : "WARNING"
								});
								
								return false;
							}
							else
								{
								$scope.selectedDoctorListTemp = [];
								var checkStatus = false;
								if($scope.billStatusIdDisabled != 3){
									
									angular.forEach($scope.doctorShareProcessList,function(value, key) {
									
										if (value.doctorShare != undefined && value.doctorShare == true) {

											angular.forEach($scope.doctorList,function(value1, key1) {

												if(value1.doctorId == value.docId && (value1.processShareAgainstUnsettledBill == "" 
																					|| value1.processShareAgainstUnsettledBill == "N" 
																						|| value1.processShareAgainstUnsettledBill == null 
																							|| value1.processShareAgainstUnsettledBill == undefined))
												{
													 checkStatus = true;
												}
												if(value1.doctorId == value.docId && value1.processShareAgainstUnsettledBill == "Y"){
													var data = {};
													data.id = value.docId;
													$scope.selectedDoctorListTemp.push(data);
												}
											});
											
										}
									
									});
								}

								if(checkStatus){
									$('#DoctorShareConfirmSave').modal('show');
									return false;
								}
								  
								var i = 1;
								$scope.doctorListForSave = [];
								angular.forEach($scope.doctorShareProcessList,function(value, key) {
									if($scope.doctorListForSave.indexOf(value.docId) !== -1) {
										}else{
											$scope.doctorListForSave.push(value.docId);
										}
										
										if($scope.doctorShareProcessList.length == i)
											{
												console.log("$scope.doctorListForSave",$scope.doctorListForSave);
												$rootScope.startLoader();
												
												for(var j = 0;j<$scope.doctorListForSave.length;j++){
													
													console.log("Id",$scope.doctorListForSave[j]);
													
													$scope.listtDoctorShareRespDto=[];
													
													var listTDocShareVoucherDtls = [];
													
													  var processShareAmtSave = 0;
													  var dueShareAmtSave = 0;
													  var hospitalShareAmtSave = 0;
													  var doctorShareAmtSave = 0;
													  
													  angular
														.forEach(
																$scope.doctorShareProcessList,
																function(value, key) {
																	
																	var tempDoctorSharAmt=0;
																	var temphosSharAmt=0;
																	var tempProcessShareAmt=0;
																	if (value.doctorShare == true) {
																		
																		if(value.docId == $scope.doctorListForSave[j]){
																		
																		tempDoctorSharAmt = (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
																		temphosSharAmt =  (isNaN((value.netAmount-(value.netAmount*value.sharePer)/100)) ? 0 : parseFloat((value.netAmount-(value.netAmount*value.sharePer)/100)));
																		tempProcessShareAmt = (isNaN(value.tempProcessedShareAmt) ? 0 : parseFloat(value.tempProcessedShareAmt));
																		var tDoctorShareResp = angular.copy($scope.tDoctorShareRespDto);
																		$scope.doctorShareId=value.doctorShareId;
																		tDoctorShareResp.doctorShareId=value.doctorShareId;
																		tDoctorShareResp.billDtlId=value.billDtlId;
																		tDoctorShareResp.visitTypeId=value.visitTypeId;
																		tDoctorShareResp.taxId=value.taxId;
																		tDoctorShareResp.orgUnitId=$scope.unitId;
																		tDoctorShareResp.orgId=$scope.orgId; 
																		tDoctorShareResp.docId=value.docId;
																		tDoctorShareResp.docSpecId=value.docSpecId; 
																		tDoctorShareResp.ordDtlId=value.ordDtlId; 
																		tDoctorShareResp.visitId=value.visitId; 
																		tDoctorShareResp.admissionId=value.admissionId; 
																		tDoctorShareResp.serviceId=value.serviceId; 
																		tDoctorShareResp.hospShare=temphosSharAmt;
																		tDoctorShareResp.docShare=tempDoctorSharAmt;
																		tDoctorShareResp.sharePer=value.sharePer;
																		tDoctorShareResp.unitRate=value.unitRate;
																		tDoctorShareResp.totalAmount=value.totalAmount;
																		tDoctorShareResp.netAmount=value.netAmount;
																		tDoctorShareResp.status="A";
																		tDoctorShareResp.createdDate=value.createdDate;
																		tDoctorShareResp.createdBy=value.createdBy;
																		tDoctorShareResp.updatedBy=value.updatedBy;
																		tDoctorShareResp.processedShareDate=value.processedShareDate;
																		tDoctorShareResp.dueShareDate=value.dueShareDate;
																		tDoctorShareResp.processedShareAmt=tempProcessShareAmt;
																		tDoctorShareResp.dueShareAmt=value.dueShareAmt; 
																		tDoctorShareResp.isBillCrDr=value.isBillCrDr;
																		tDoctorShareResp.isActive=value.isActive;
																		tDoctorShareResp.taxAmt=value.taxAmt;
																		tDoctorShareResp.isOnBillPayment=value.isOnBillPayment;
																		tDoctorShareResp.discount=value.discount;
																		tDoctorShareResp.concession=value.concession;
																		tDoctorShareResp.taxType=value.taxType;
																		$scope.docIdTemp=value.docId;
																		
																		processShareAmtSave = parseFloat(processShareAmtSave) + parseFloat(tempProcessShareAmt);
																		dueShareAmtSave = parseFloat(dueShareAmtSave) + parseFloat(value.dueShareAmt);
																		hospitalShareAmtSave = parseFloat(hospitalShareAmtSave) + parseFloat(temphosSharAmt);
																		doctorShareAmtSave = parseFloat(doctorShareAmtSave) + parseFloat(tempDoctorSharAmt);
																		
																		
																		var temp = {
																				"docShareVoucheId": "",
																				"doctorShareId":value.doctorShareId,
																				"processedAmt":value.tempProcessedShareAmt,
																				"orgUnitId": $scope.unitId,
																				"orgId": $scope.orgId,
																				"status": "A",
																				"createdDate": "",
																				"createdBy": $scope.userId,
																				"updatedDate": "",
																				"updatedBy": ""
																		   };
																	   listTDocShareVoucherDtls.push(temp);
																	   
																	   
																		$scope.listtDoctorShareRespDto.push(tDoctorShareResp);
																	}
																		
																	}
																});
													  $scope.tDocShareVoucherDto={
																"docShareVoucheId": " ",
																"voucherNo": "1",
																"totalDocShareAmout":doctorShareAmtSave,
																"orgId": $scope.orgId,
																"orgUnitId": $scope.unitId,
																"status": "A",
																"docId":$scope.docIdTemp,
																"createdDate": "",
																"createdBy":$scope.userId,
																"updatedDate": "",
																"updatedBy": "",
																"processedShareAmt":processShareAmtSave,
																"dueShareAmt":dueShareAmtSave,
																"remark":$scope.remark,
														}
													  
													  
														
														/*var listTDocShareVoucherDtls = [];
														angular.forEach($scope.doctorShareProcessList,function(value,index){
															var tempDoctorSharAmt=0;
															if (value.doctorShare == true) {
																tempDoctorSharAmt = (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
															var temp = {
																		"docShareVoucheId": "",
																		"doctorShareId":value.doctorShareId,
																		"processedAmt":value.tempProcessedShareAmt,
																		"orgUnitId": $scope.unitId,
																		"orgId": $scope.orgId,
																		"status": "A",
																		"createdDate": "",
																		"createdBy": $scope.userId,
																		"updatedDate": "",
																		"updatedBy": ""
																   };
															   listTDocShareVoucherDtls.push(temp);
															}
														
														});*/
													
														var param = {
																        listtDoctorShareRespDto:$scope.listtDoctorShareRespDto,
																		tDocShareVoucherDto:$scope.tDocShareVoucherDto,
																		listTDocShareVoucherDtlsDto:listTDocShareVoucherDtls,
																		orgId:$scope.orgId,
																		unitId:$scope.unitId,
																		userId:$scope.userId
																    }
														 console.log("param",JSON.stringify(param));
														//return false;
														
														
														
													if(j==$scope.doctorListForSave.length -1)
														{
															var counter=0;
															$scope.creditDays=0;
														angular.forEach($scope.docShareListToCheckCredit,function(value, key) {
																	if(counter==0)
																		{
																		if(value.doctorId==$scope.doctorListForSave[j])
																			{
																			$scope.creditDays=value.creditDays;
																			var today = new Date()
																			var dueDate=new Date(today.getTime() + ($scope.creditDays)*24*60*60*1000);
																			counter++;
																			param.tDocShareVoucherDto.dueDate=dueDate;
																			}
																		}
																})
														var URI = BASE_URL + ROOT_URL + "/api/billing/saveDoctorShareList";
														BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
															if (response.data.status == "success")
																$scope.init();
															$scope.selectedDoctorList = [];
															$scope.billNumber ='';
															$scope.remark = '';
															$rootScope.stopLoader();
														});
														
														}else{
														    if(param.listTDocShareVoucherDtlsDto !== undefined && param.listTDocShareVoucherDtlsDto !== null && param.listTDocShareVoucherDtlsDto.length > 0){
                                                                var URI = BASE_URL + ROOT_URL + "/api/billing/saveDoctorShareList";
                                                                BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                                                                    if (response.data.status == "success")
                                                                        $scope.init();

                                                                    $rootScope.stopLoader();
                                                                });
															}
														}
													
												}
											}
										i++;
											
										});
									 
							//return false;
								/*$scope.listtDoctorShareRespDto=[];
									  
									  angular
										.forEach(
												$scope.doctorShareProcessList,
												function(value, key) {
													var tempDoctorSharAmt=0;
													var temphosSharAmt=0;
													var tempProcessShareAmt=0;
													if (value.doctorShare == true) {
														
														tempDoctorSharAmt = (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
														temphosSharAmt =  (isNaN((value.netAmount-(value.netAmount*value.sharePer)/100)) ? 0 : parseFloat((value.netAmount-(value.netAmount*value.sharePer)/100)));
														tempProcessShareAmt = (isNaN(value.tempProcessedShareAmt) ? 0 : parseFloat(value.tempProcessedShareAmt))+ (isNaN(value.processedShareAmt) || value.processedShareAmt==null ? 0 : parseFloat(value.processedShareAmt));
														var tDoctorShareResp = angular.copy($scope.tDoctorShareRespDto);
														$scope.doctorShareId=value.doctorShareId;
														tDoctorShareResp.doctorShareId=value.doctorShareId;
														tDoctorShareResp.billDtlId=value.billDtlId;
														tDoctorShareResp.visitTypeId=value.visitTypeId;
														tDoctorShareResp.taxId=value.taxId;
														tDoctorShareResp.orgUnitId=$scope.unitId;
														tDoctorShareResp.orgId=$scope.orgId; 
														tDoctorShareResp.docId=value.docId;
														tDoctorShareResp.docSpecId=value.docSpecId; 
														tDoctorShareResp.ordDtlId=value.ordDtlId; 
														tDoctorShareResp.visitId=value.visitId; 
														tDoctorShareResp.admissionId=value.admissionId; 
														tDoctorShareResp.serviceId=value.serviceId; 
														tDoctorShareResp.hospShare=temphosSharAmt;
														tDoctorShareResp.docShare=tempDoctorSharAmt;
														tDoctorShareResp.sharePer=value.sharePer;
														tDoctorShareResp.unitRate=value.unitRate;
														tDoctorShareResp.totalAmount=value.totalAmount;
														tDoctorShareResp.netAmount=value.netAmount;
														tDoctorShareResp.status="A";
														tDoctorShareResp.createdDate=value.createdDate;
														tDoctorShareResp.createdBy=value.createdBy;
														tDoctorShareResp.updatedBy=value.updatedBy;
														tDoctorShareResp.processedShareDate=value.processedShareDate;
														tDoctorShareResp.dueShareDate=value.dueShareDate;
														tDoctorShareResp.processedShareAmt=tempProcessShareAmt;
														tDoctorShareResp.dueShareAmt=value.dueShareAmt; 
														tDoctorShareResp.isBillCrDr=value.isBillCrDr;
														tDoctorShareResp.isActive=value.isActive;
														tDoctorShareResp.taxAmt=value.taxAmt;
														tDoctorShareResp.isOnBillPayment=value.isOnBillPayment;
														tDoctorShareResp.discount=value.discount;
														tDoctorShareResp.concession=value.concession;
														tDoctorShareResp.taxType=value.taxType;
														$scope.docIdTemp=value.docId;
														$scope.listtDoctorShareRespDto.push(tDoctorShareResp);
														
													}
												});
									  $scope.tDocShareVoucherDto={
												"docShareVoucheId": " ",
												"voucherNo": "1",
												"totalDocShareAmout":$scope.doctorSharAmt,
												"orgId": $scope.orgId,
												"orgUnitId": $scope.unitId,
												"status": "A",
												"docId":$scope.docIdTemp,
												"createdDate": "",
												"createdBy":$scope.userId,
												"updatedDate": "",
												"updatedBy": "",
												"processedShareAmt":$scope.processedShareAmt,
												"dueShareAmt":$scope.dueShareAmt,
												"remark":$scope.remark,
										}
										
										var listTDocShareVoucherDtls = [];
										angular.forEach($scope.doctorShareProcessList,function(value,index){
											var tempDoctorSharAmt=0;
											if (value.doctorShare == true) {
												tempDoctorSharAmt = (isNaN((value.netAmount*value.sharePer)/100) ? 0 : parseFloat((value.netAmount*value.sharePer)/100));
											var temp = {
														"docShareVoucheId": "",
														"doctorShareId":value.doctorShareId,
														"processedAmt":value.tempProcessedShareAmt,
														"orgUnitId": $scope.unitId,
														"orgId": $scope.orgId,
														"status": "A",
														"createdDate": "",
														"createdBy": $scope.userId,
														"updatedDate": "",
														"updatedBy": ""
												   };
											   listTDocShareVoucherDtls.push(temp);
											}
										
										});
									
										var param = {
												        listtDoctorShareRespDto:$scope.listtDoctorShareRespDto,
														tDocShareVoucherDto:$scope.tDocShareVoucherDto,
														listTDocShareVoucherDtlsDto:listTDocShareVoucherDtls,
														orgId:$scope.orgId,
														unitId:$scope.unitId,
														userId:$scope.userId
												    }
										 console.log("param",JSON.stringify(param));
										//return false;
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/billing/saveDoctorShareList";
										BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
											if (response.data.status == "success")
												$scope.init();
											
											$rootScope.stopLoader();
										});*/
								}
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
			  //OLD Start
			 /* $scope.currRefShare = angular.copy(share);
			  console.log($scope.currRefShare);
			  //return false;
			  $scope.splitDocShareList = [];*/
			  //OLD End
			  
			  //NEW Start
			  $rootScope.startLoader();
			  var dueShareAmt =  share.dueShareAmt;
			  var tempProcessedShareAmt = share.tempProcessedShareAmt;
			  if(share.doctorShare){
				  /* share.dueShareAmt =  share.tempProcessedShareAmt;
				  $scope.doctorShareProcessList[idx].dueShareAmt = share.tempProcessedShareAmt;
				  $scope.processedShareAmt = parseFloat($scope.processedShareAmt) - parseFloat(share.tempProcessedShareAmt);*/
				
				  
			  }
			  
			  $scope.splitDocShare.specialityId = '';
			  $scope.splitDocShare.doctorId = '';
			  $scope.splitDocShare.sharePer = 0;
			  $scope.splitDocShare.amount = 0;
			  
			    $scope.splitDocShareList = [];
			  	$scope.parentListIndex = idx;
				var tempDoctorShareListSingle = angular.copy(share);
				$scope.doctorShareListSingle = tempDoctorShareListSingle;
			  	$scope.hospitalShareAmountPopup = parseFloat(share.hospShare.toFixed(2));
			  	$scope.hospitalShareAmountPopupForCheck = share.hospShare;
			  	$scope.hospitalShareCheckboxPopupForCheck = share.doctorShare;
				$scope.hospitalDueShareAmtPopupForCheck = share.tempProcessedShareAmt;
			  	$scope.doctorShareAmountPopup =0;
			  	
			  	//for referral Start
				$scope.referralShareAmountPopup = 0;
			    $scope.consumedrefShre =0;
			  	$scope.referralShareAmountPopup = 0;
			  	$scope.referralShareIdPopup = null;
				$scope.referralShareProcessAmountPopup = 0;
			  	$scope.referralFlagForManupulation = true;
			  	$scope.referralFlagForHospital = true;
			  	if(share.refDocShareId > 0)
			  		{
			  		$scope.referralFlagForHospital = false;
			  		$scope.referralShareIdPopup = share.refDocShareId;
			  		$scope.referralShareAmountPopup =share.refDocShare;
			  		$scope.referralShareAmountPopupOld =share.refDocShare;
			  		$scope.consumedrefShre = share.refProcessedShareAmt != null ? share.refProcessedShareAmt:0;
			  		
			  		if(share.refProcessedShareAmt > 0 && share.refProcessedShareAmt != null)
			  			{
			  			$scope.referralFlagForManupulation = false;
			  			$scope.referralShareProcessAmountPopup = share.refProcessedShareAmt;
			  				if(share.refProcessedShareAmt >= $scope.hospitalShareAmountPopup)
			  					{
			  					   $scope.hospitalShareAmountPopup = $scope.hospitalShareAmountPopup;
			  					 $scope.referralShareAmountPopup =share.refDocShare- share.refProcessedShareAmt;
			  				  		$scope.referralShareAmountPopupOld =share.refProcessedShareAmt;
			  					}else{
			  						$scope.hospitalShareAmountPopup = $scope.hospitalShareAmountPopup;
			  						$scope.referralShareAmountPopup =share.refDocShare- share.refProcessedShareAmt;
			  				  		$scope.referralShareAmountPopupOld =share.refProcessedShareAmt;
			  					}
			  			}
			  		}
			  	//For referall End
			  	
			    var URI = BASE_URL + ROOT_URL + "/api/billing/doctorShare/split/details/"+share.doctorShareId;
			    BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						share.dueShareAmt =  dueShareAmt;
						share.tempProcessedShareAmt = tempProcessedShareAmt;
						 $scope.splitDocShareList = response.data.listObject;
						
						 console.log("$scope.splitDetailsNewList",JSON.stringify($scope.splitDetailsNewList));
						 $scope.splitDocShareList.forEach(function(value){
							 $scope.doctorShareAmountPopup =$scope.doctorShareAmountPopup + value.shareAmount;
							});
						 $scope.totalHospitaAndDoctorShareAmount = parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.doctorShareAmountPopup) +  parseFloat($scope.consumedrefShre) ;
					}
					$rootScope.stopLoader();
					
				});
			
			    //NEW END
			    
		  }
		  
		  $scope.validateSplitSharePerPopup = function(item,idx)
		  {
			 // $scope.doctorShareProcessList
			  if(item.newSharePercentage > 100)
				  {
					  growl.warning("Percentage should be less then 100%.",{
							title : "WARNING"
						});
					   $scope.splitDocShareList[idx].newSharePercentage = item.oldSharePercentage;
						return false;
				  }
			  else
				  {
				  var tmpOldShrAmt = $scope.splitDocShareList[idx].oldsharAmt;
				  var hosSh = parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.splitDocShareList[idx].oldsharAmt);
				 
				  //var spliAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].unitRate * item.newSharePercentage)/100;
				  var spliAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].netAmount * item.newSharePercentage)/100;
				  var tmpSub  = 0;
				  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true){
					  tmpSub = hosSh + $scope.referralShareAmountPopup;
				  }
				  if($scope.referralFlagForHospital == true || $scope.referralFlagForHospital == false){
					  tmpSub  = hosSh;
				  }
				  if(spliAmt > tmpSub)
					  {
						  growl.warning("You have only "+ tmpSub,{
								title : "WARNING"
							});
						   $scope.splitDocShareList[idx].newSharePercentage = item.oldSharePercentage;
							return false;
					  }
				  else
					  {
						  ///$scope.hospitalShareAmountPopup = hosSh - spliAmt;
						 ///var hosSharPopAmt = parseFloat(hosSh - spliAmt).toFixed(2);
						  $scope.splitDocShareList[idx].shareAmount = spliAmt;
						  $scope.splitDocShareList[idx].oldSharePercentage = item.newSharePercentage;
			  			   $scope.splitDocShareList[idx].oldsharAmt = spliAmt;
			  			 if($scope.referralFlagForHospital == false || $scope.referralFlagForHospital == false){
			  			 $scope.hospitalShareAmountPopup = (hosSh - parseFloat(item.shareAmount)).toFixed(2);
			  			 }
			  			 if($scope.referralFlagForHospital == true || $scope.referralFlagForHospital == true){
				  			 $scope.hospitalShareAmountPopup = (hosSh - parseFloat(item.shareAmount)).toFixed(2);
				  			 }
				  			   
			  			   
			  			  var flagCheck = false;
						    if($scope.hospitalShareAmountPopup > 0){
						    	
						       var temparyCal = hosSh - parseFloat(item.shareAmount);
						       if(temparyCal <= 0){
						    	   $scope.referralShareIdPopup = $scope.referralShareIdPopup == null?0:$scope.referralShareIdPopup;
						    	   if($scope.referralShareIdPopup > 0  && $scope.referralFlagForManupulation == true){
						    		   flagCheck = true;
						    		   $scope.referralShareAmountPopup = $scope.referralShareAmountPopup - ( parseFloat(item.shareAmount) - hosSh);
						    		   $scope.hospitalShareAmountPopup = 0;
						    	   }
						       }else{
						    	   flagCheck = true;
						    	   $scope.hospitalShareAmountPopup = (hosSh - parseFloat(item.shareAmount)).toFixed(2);
						       }
						       
						       
						    }else{
						    	if(parseFloat(tmpOldShrAmt) >= parseFloat(item.shareAmount))
						    		{
						    		  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true){
						    			  if($scope.referralShareAmountPopupOld > $scope.referralShareAmountPopup)
						    				  {	
						    				        flagCheck = true;
						    				  		var tmpDiff =  tmpOldShrAmt - item.shareAmount;
						    				  		var diffRefe = $scope.referralShareAmountPopupOld - $scope.referralShareAmountPopup;
						    				  		if(tmpDiff >= diffRefe)
						    				  			{
						    				  			var tmpDiffRefe = tmpDiff - diffRefe;
						    				  			 $scope.referralShareAmountPopup = $scope.referralShareAmountPopup + diffRefe;
						    				  			$scope.hospitalShareAmountPopup =$scope.hospitalShareAmountPopup +  tmpDiffRefe;
						    				  			 
						    				  			}else{
						    				  				$scope.referralShareAmountPopup = $scope.referralShareAmountPopup + tmpDiff;
						    				  			}
						    				  }
						    		  }
						    		}
						    }
			  			// $scope.doctorShareAmountPopup = 0;
			  			 var docShrAmtPopUp = parseFloat(0);
			  			 
			  			var tempPerMainDoctor = parseFloat(0);
						  var tempAmtMainDoctor = parseFloat(0);
						  
			  			 angular.forEach($scope.splitDocShareList,function(value,index){
								 docShrAmtPopUp =docShrAmtPopUp + parseFloat(value.shareAmount);
								 if(index == 0)
						    	 {
						    	 tempPerMainDoctor = parseFloat(value.newSharePercentage);
						    	 tempAmtMainDoctor =parseFloat(value.shareAmount);
						    	 }
								});
			  			 
			  			
						  
			  			   $scope.doctorShareAmountPopup = parseFloat(docShrAmtPopUp);
							 $scope.totalHospitaAndDoctorShareAmount = parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.doctorShareAmountPopup);
							 //$scope.hospitalShareAmountPopup = parseFloat(hosSh - spliAmt).toFixed(2);
							  $scope.doctorShareAmountPopup = parseFloat(docShrAmtPopUp).toFixed(2);
							 //$scope.hospitalShareAmountPopup = parseFloat(hosSh - spliAmt);
							 $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareAmountPopup;
							 $scope.doctorShareProcessList[$scope.parentListIndex].docShare = (tempAmtMainDoctor).toFixed(2);
							 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.hospitalShareAmountPopup;
							 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = tempAmtMainDoctor -  $scope.doctorShareProcessList[$scope.parentListIndex].processedShareAmt;
							 $scope.doctorShareProcessList[$scope.parentListIndex].sharePer = (tempPerMainDoctor).toFixed(2);
							 
							//For Referral Share Start
							 if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true && flagCheck == false)
							 {
							 	if($scope.referralShareAmountPopupOld > $scope.referralShareAmountPopup)
							 		{
							 		if(tmpOldShrAmt > item.shareAmount)
							 			{
							 				var tm = tmpOldShrAmt - item.shareAmount;
							 				var tmpRefe = parseFloat($scope.referralShareAmountPopup) + parseFloat(tm);
							 				if($scope.referralShareAmountPopupOld >= tmpRefe){
							 					$scope.referralShareAmountPopup  = parseFloat($scope.referralShareAmountPopup) + parseFloat(tm);
							 				}else{
							 					if($scope.totalHospitaAndDoctorShareAmount == tmpRefe)
							 						{
							 						  $scope.referralShareAmountPopup  = $scope.referralShareAmountPopupOld;
							 						}else{
							 							if(tmpRefe > $scope.referralShareAmountPopupOld)
							 								{
							 								$scope.referralShareAmountPopup  = $scope.referralShareAmountPopupOld;
							 								}else{
							 									var reft = tmpRefe - $scope.referralShareAmountPopupOld;
											 					$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup - tm;
							 								}
							 					
							 						}
							 				}
							 				
							 			}else{
							 				var tm = item.shareAmount - tmpOldShrAmt;
							 				$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup - tm;
							 				var tmpRefe = $scope.referralShareAmountPopup + tm;
							 				if($scope.referralShareAmountPopupOld >= tmpRefe){
							 					//$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup + tm;
							 				}else{
							 					var reft = tmpRefe - $scope.referralShareAmountPopupOld;
							 					$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup + tm;
							 				}
							 			}
							 		}
							 	else if($scope.referralShareAmountPopup > $scope.hospitalShareAmountPopup)
								 		{
								 		  //$scope.referralShareAmountPopup = $scope.referralShareAmountPopup - $scope.splitDocShare.amount;
							 		
							 		if(item.shareAmount > tmpOldShrAmt)
						 			{
						 				var tm = item.shareAmount - tmpOldShrAmt;
						 				$scope.referralShareAmountPopup = $scope.referralShareAmountPopup - tm;
						 				
						 			}
							 		if($scope.referralShareAmountPopupOld == $scope.referralShareAmountPopup)
						 			{
						 			    var tm = tmpOldShrAmt  - item.shareAmount;
						 			   $scope.hospitalShareAmountPopup = $scope.hospitalShareAmountPopup + tm;
						 			  $scope.totalHospitaAndDoctorShareAmount = $scope.totalHospitaAndDoctorShareAmount + $scope.hospitalShareAmountPopup;
						 			}
							 		
								 		}
							 	
							
							 }
							 //For Referral Share END
			  			   
			  			   
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
			  
			  if(item.shareAmount == '' || item.shareAmount == undefined || item.shareAmount == null || parseFloat(item.shareAmount) == 0){
				  item.shareAmount  = parseFloat(0);
					growl.warning('Doctor share amount should be greater than zero!',
							{
							});
					$scope.yesPopupClose();
					return false;
			  }
			  
			  if(item.shareAmount == '' || item.shareAmount == undefined || item.shareAmount == null){
				  item.shareAmount  = parseFloat(0);
			  }
			  
			  var tmpSA = parseFloat(0);
			  
			  angular.forEach($scope.splitDocShareList,function(value,index){
					if(idx != index){
						tmpSA = tmpSA + parseFloat(value.shareAmount);
					}
					});
			  
			 var tmpA = parseFloat(0);
			  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
			  {
				  tmpA = parseFloat(tmpSA) + parseFloat($scope.referralShareAmountPopup);
			  }
			  
				  //if(item.shareAmount > $scope.doctorShareProcessList[$scope.parentListIndex].unitRate)
			  //if(item.shareAmount > $scope.doctorShareProcessList[$scope.parentListIndex].netAmount)
			  
			  //test
			  
			  var tmpOldShrAmt = $scope.splitDocShareList[idx].oldsharAmt;
			  var hosSh = parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.splitDocShareList[idx].oldsharAmt);
			  
			  var spliAmt = item.shareAmount;
			  var tmpSub  = 0;
			  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true){
				  tmpSub = hosSh + $scope.referralShareAmountPopup;
			  }
			  if($scope.referralFlagForHospital == true || $scope.referralFlagForHospital == false){
				  tmpSub  = hosSh;
			  }
			  
			  //Test
			  
			  if(parseFloat(spliAmt) > parseFloat(tmpSub))
					  {
						   growl.warning("You can add more "+parseFloat($scope.hospitalShareAmountPopup),{
								title : "WARNING"
							});
						   $scope.splitDocShareList[idx].shareAmount = item.oldsharAmt;
							return false;
					  }
				  else 
					  {
					  
					  var tmpOldShrAmt = $scope.splitDocShareList[idx].oldsharAmt;
					  
					  var hosShr = parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.splitDocShareList[idx].oldsharAmt);
					  
					  //if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
						 	//if($scope.referralShareAmountPopupOld > $scope.referralShareAmountPopup)
					  
					   // var spliPer = (item.shareAmount/$scope.doctorShareProcessList[$scope.parentListIndex].unitRate)*100;
					  var spliPer = (item.shareAmount/$scope.doctorShareProcessList[$scope.parentListIndex].netAmount)*100;
					    //var spliAmt = ($scope.doctorShareProcessList[$scope.parentListIndex].unitRate * item.newSharePercentage)/100;
					    
					    if($scope.referralFlagForHospital == true || $scope.referralFlagForHospital == false){
				  			 $scope.hospitalShareAmountPopup = (hosShr - parseFloat(item.shareAmount)).toFixed(2);
				  			 }
					    
					    $scope.splitDocShareList[idx].newSharePercentage = parseFloat(spliPer).toFixed(2);
					    $scope.splitDocShareList[idx].oldSharePercentage =parseFloat(spliPer).toFixed(2);
					    $scope.splitDocShareList[idx].oldsharAmt = item.shareAmount;
					    var flagCheck = false;
					    if($scope.hospitalShareAmountPopup >= 0){
					    	
					       var temparyCal = hosShr - parseFloat(item.shareAmount);
					       if(temparyCal <= 0){
					    	   if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true){
					    		   flagCheck = true;
					    		   $scope.referralShareAmountPopup = $scope.referralShareAmountPopup - ( parseFloat(item.shareAmount) - hosShr);
					    		   $scope.hospitalShareAmountPopup = 0;
					    	   }
					       }else{
					    	   flagCheck = true;
					    	   $scope.hospitalShareAmountPopup = (hosShr - parseFloat(item.shareAmount)).toFixed(2);
					       }
					       
					       
					    }else{
					    	if(parseFloat(tmpOldShrAmt) >= parseFloat(item.shareAmount))
					    		{
					    		  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true){
					    			  if($scope.referralShareAmountPopupOld > $scope.referralShareAmountPopup)
					    				  {	
					    				        flagCheck = true;
					    				  		var tmpDiff =  tmpOldShrAmt - item.shareAmount;
					    				  		var diffRefe = $scope.referralShareAmountPopupOld - $scope.referralShareAmountPopup;
					    				  		if(tmpDiff >= diffRefe)
					    				  			{
					    				  			var tmpDiffRefe = tmpDiff - diffRefe;
					    				  			 $scope.referralShareAmountPopup = $scope.referralShareAmountPopup + diffRefe;
					    				  			$scope.hospitalShareAmountPopup =$scope.hospitalShareAmountPopup +  tmpDiffRefe;
					    				  			 
					    				  			}else{
					    				  				$scope.referralShareAmountPopup = $scope.referralShareAmountPopup + tmpDiff;
					    				  			}
					    				  }
					    		  }
					    		}
					    }
					 // $scope.doctorShareAmountPopup = 0;
					  var docShrAmtPopUp = parseFloat(0);
					  var tempPerMainDoctor = parseFloat(0);
					  var tempAmtMainDoctor = parseFloat(0);
					  angular.forEach($scope.splitDocShareList,function(value,index){
							// $scope.doctorShareAmountPopup =$scope.doctorShareAmountPopup + value.shareAmount;
							 docShrAmtPopUp =docShrAmtPopUp + parseFloat(value.shareAmount);
							 if(index == 0)
					    	 {
					    	 tempPerMainDoctor = parseFloat(value.newSharePercentage);
					    	 tempAmtMainDoctor =parseFloat(value.shareAmount);
					    	 }
							});
					  
				  			
					  $scope.doctorShareAmountPopup = parseFloat(docShrAmtPopUp).toFixed(2);
					  $scope.totalHospitaAndDoctorShareAmount =parseFloat($scope.hospitalShareAmountPopup) + parseFloat($scope.doctorShareAmountPopup);
					     $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareAmountPopup;
						 $scope.doctorShareProcessList[$scope.parentListIndex].docShare = (tempAmtMainDoctor).toFixed(2);
						 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.hospitalShareAmountPopup;
						 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = tempAmtMainDoctor -  $scope.doctorShareProcessList[$scope.parentListIndex].processedShareAmt;
						 $scope.doctorShareProcessList[$scope.parentListIndex].sharePer = (tempPerMainDoctor).toFixed(2);
						 
						 //For Referral Share Start
						if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true && flagCheck == false)
							 {
							 	if($scope.referralShareAmountPopupOld > $scope.referralShareAmountPopup)
							 		{
							 		if(tmpOldShrAmt > item.shareAmount)
							 			{
							 				var tm = tmpOldShrAmt - item.shareAmount;
							 				var tmpRefe = parseFloat($scope.referralShareAmountPopup) + parseFloat(tm);
							 				if($scope.referralShareAmountPopupOld >= tmpRefe){
							 					$scope.referralShareAmountPopup  = parseFloat($scope.referralShareAmountPopup) + parseFloat(tm);
							 				}else{
							 					if($scope.totalHospitaAndDoctorShareAmount == tmpRefe)
							 						{
							 						  $scope.referralShareAmountPopup  = $scope.referralShareAmountPopupOld;
							 						}else{
							 							if(tmpRefe > $scope.referralShareAmountPopupOld)
							 								{
							 								$scope.referralShareAmountPopup  = $scope.referralShareAmountPopupOld;
							 								}else{
							 									var reft = tmpRefe - $scope.referralShareAmountPopupOld;
											 					$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup - tm;
							 								}
							 					
							 						}
							 				}
							 				
							 			}else{
							 				var tm = item.shareAmount - tmpOldShrAmt;
							 				$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup - tm;
							 				var tmpRefe = $scope.referralShareAmountPopup + tm;
							 				if($scope.referralShareAmountPopupOld >= tmpRefe){
							 					$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup + tm;
							 				}else{
							 					var reft = tmpRefe - $scope.referralShareAmountPopupOld;
							 					$scope.referralShareAmountPopup  = $scope.referralShareAmountPopup + tm;
							 				}
							 			}
							 		}
							 	else if($scope.referralShareAmountPopup > $scope.hospitalShareAmountPopup)
								 		{
								 		  //$scope.referralShareAmountPopup = $scope.referralShareAmountPopup - $scope.splitDocShare.amount;
							 		if(item.shareAmount > tmpOldShrAmt)
						 			{
						 				var tm = item.shareAmount - tmpOldShrAmt;
						 				$scope.referralShareAmountPopup = $scope.referralShareAmountPopup - tm;
						 				
						 			}
							 		
								 		}
							 	
							
							 }
						 //For Referral Share END
						 
							 
						 
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
				  
				  $scope.netwvAmount = 0;
				  $scope.netwvAmount = $scope.doctorShareProcessList[$scope.parentListIndex].netAmount;
				  
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
				  $scope.splitDocShare.specialityId = '';
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
				  $scope.splitDocShare.specialityId = '';
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
					  $scope.splitDocShare.specialityId = '';
					  $scope.splitDocShare.doctorId = '';
					  $scope.splitDocShare.sharePer = 0;
					  $scope.splitDocShare.amount = 0;
					  return;
				  }
				  
				  $scope.splitDoctorList.forEach((elem) => {
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
				   var data = {
						   docId : $scope.splitDocShare.doctorId,
						   doctorName : $scope.splitDocShare.doctorName,
						   newSharePercentage : $scope.splitDocShare.sharePer,
						   oldSharePercentage : $scope.splitDocShare.sharePer,
						   shareAmount : $scope.splitDocShare.amount,
						   oldsharAmt : $scope.splitDocShare.amount,
						   docSpecId:$scope.splitDocShare.specialityId,
						   doctorShareSplitId : null,
							doctorShareId : null,
				   }
				   
				  $scope.splitDocShareList.push(data);
				  
				  var tempPerMainDoctor = parseFloat(0);
				  var tempAmtMainDoctor = parseFloat(0);
				  angular.forEach($scope.splitDocShareList,function(value,index){
						// $scope.doctorShareAmountPopup =$scope.doctorShareAmountPopup + value.shareAmount;
					    // tempPer =tempPer + parseFloat(value.newSharePercentage);
					     if(index == 0)
					    	 {
					    	 tempPerMainDoctor = parseFloat(value.newSharePercentage);
					    	 tempAmtMainDoctor =parseFloat(value.shareAmount);
					    	 }
						});
				  
				 /* if($scope.hospitalShareAmountPopup > 0){
					  if($scope.hospitalShareAmountPopup >= $scope.splitDocShare.amount){
					  $scope.hospitalShareAmountPopup = ($scope.hospitalShareAmountPopup - $scope.splitDocShare.amount).toFixed(2);
					  }else{
						  $scope.hospitalShareAmountPopup = 0;
					  }
				  }*/
				  
				  if($scope.hospitalShareAmountPopup >= 0){
					  
					  if($scope.hospitalShareAmountPopup >= $scope.splitDocShare.amount){
					  $scope.hospitalShareAmountPopup = ($scope.hospitalShareAmountPopup - $scope.splitDocShare.amount).toFixed(2);
					  }else{
						  
						  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
							 {
							 	if($scope.referralShareAmountPopup > $scope.hospitalShareAmountPopup)
							 		{
							 		$scope.referralShareAmountPopup = $scope.referralShareAmountPopup - ($scope.splitDocShare.amount - $scope.hospitalShareAmountPopup);
							 		}
							 	$scope.hospitalShareAmountPopup = 0;
							   
							 }else{
								 $scope.hospitalShareAmountPopup = 0;
							 }
						  
						  
						  
					  }
				  }else{
					//For Referral Share Start
						 if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
							 {
							 	if($scope.referralShareAmountPopup > $scope.hospitalShareAmountPopup)
							 		{
							 		$scope.referralShareAmountPopup = $scope.referralShareAmountPopup - $scope.splitDocShare.amount;
							 		}
							  
							   
							 }
						 
						 //For Referral Share END
				  }
				  
				  
				  
					 
				  var docShrAmtPopup = parseFloat($scope.doctorShareAmountPopup) + parseFloat($scope.splitDocShare.amount);
				  $scope.doctorShareAmountPopup = (docShrAmtPopup).toFixed(2);

				     $scope.totalHospitaAndDoctorShareAmount = (parseFloat($scope.hospitalShareAmountPopup) + parseFloat(docShrAmtPopup) + parseFloat($scope.consumedrefShre)).toFixed(2);
					 $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareAmountPopup;
					 $scope.doctorShareProcessList[$scope.parentListIndex].docShare = (tempAmtMainDoctor).toFixed(2);
					 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.hospitalShareAmountPopup;
					 $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = tempAmtMainDoctor -  $scope.doctorShareProcessList[$scope.parentListIndex].processedShareAmt;
					 $scope.doctorShareProcessList[$scope.parentListIndex].sharePer = (tempPerMainDoctor).toFixed(2);


					 var checkamsoos = 0;
					  checkamsoos = parseFloat($scope.referralShareAmountPopup) + parseFloat($scope.totalHospitaAndDoctorShareAmount);
					  
					  if( checkamsoos >  $scope.netwvAmount)
						  {
						  growl.warning("Total Amount is getting more than service rate amount",{
								title : WARNING_MSG
							});
						  return;
						  
						  angular.forEach($scope.splitDocShareList,function(v,k){
							  $scope.removeSplitShare(v,k);
						  });
						  }
					 //For Referral Share Start
					/* if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
						 {
						 	if($scope.referralShareAmountPopup > $scope.hospitalShareAmountPopup)
						 		{
						 		$scope.referralShareAmountPopup = $scope.referralShareAmountPopup - $scope.splitDocShare.amount;
						 		}
						  
						   
						 }*/
					 
					 //For Referral Share END
					 
				   $scope.initSplitDocShare();
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
				  
				  var hospitalShareAmountPopupTemp =  (parseFloat($scope.hospitalShareAmountPopup) + parseFloat(item.shareAmount)).toFixed(2);
				  var docShrAmtPopupTemp = parseFloat($scope.doctorShareAmountPopup) - parseFloat(item.shareAmount);
				  var doctorShareAmountPopupTemp = (docShrAmtPopupTemp).toFixed(2);
				  
				  var totalHospitaAndDoctorShareAmountTemp = (parseFloat(hospitalShareAmountPopupTemp) + docShrAmtPopupTemp).toFixed(2);
						  
						  var data = {
									doctorShareSplitId : item.doctorShareSplitId,
									doctorShareId : item.doctorShareId,
									parentDocShareId : $scope.doctorShareProcessList[$scope.parentListIndex].doctorShareId,
									hospShare : hospitalShareAmountPopupTemp,
									totalDocShare : docShrAmtPopupTemp
						  }
						  console.log("data",data);
						// return false;
						    var URI = BASE_URL + ROOT_URL + "/api/billing/process/doctorShare/doctor/inactive";
						    BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success"){
									
									$scope.hospitalShareAmountPopup =  (parseFloat($scope.hospitalShareAmountPopup) + parseFloat(item.shareAmount)).toFixed(2);
									  var docShrAmtPopup = parseFloat($scope.doctorShareAmountPopup) - parseFloat(item.shareAmount);
									  $scope.doctorShareAmountPopup = (docShrAmtPopup).toFixed(2);
									  
									     $scope.totalHospitaAndDoctorShareAmount = (parseFloat($scope.hospitalShareAmountPopup) + docShrAmtPopup).toFixed(2);
										 $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareAmountPopup;
										 $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.hospitalShareAmountPopup;
										 //$scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = $scope.doctorShareProcessList[$scope.parentListIndex].docShare -  $scope.doctorShareProcessList[$scope.parentListIndex].processedShareAmt;
									  $scope.splitDocShareList.splice(index,1);
									  
									  $scope.doctorShareListSingle.totalDocShare = $scope.doctorShareAmountPopup;
									  $scope.doctorShareListSingle.hospShare =  $scope.hospitalShareAmountPopup;
										 
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
				  
					/*  $scope.hospitalShareAmountPopup =  (parseFloat($scope.hospitalShareAmountPopup) + parseFloat(item.shareAmount)).toFixed(2);
					  var docShrAmtPopup = parseFloat($scope.doctorShareAmountPopup) - parseFloat(item.shareAmount);
					  $scope.doctorShareAmountPopup = (docShrAmtPopup).toFixed(2);
					  
					  $scope.totalHospitaAndDoctorShareAmount = (parseFloat($scope.hospitalShareAmountPopup) + docShrAmtPopup).toFixed(2);
					  $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareAmountPopup;
					  $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.hospitalShareAmountPopup;
				
					  
					  $scope.splitDocShareList.splice(index,1);
					  $('#popupForInactive').modal('hide');*/
					  
			
			
				  
				 
				  
					  
					  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true)
						 {
						 	if($scope.referralShareAmountPopupOld >= $scope.referralShareAmountPopup)
						 		{
						 			var dif = $scope.referralShareAmountPopupOld - $scope.referralShareAmountPopup;
						 			if(parseFloat(item.shareAmount) >= parseFloat(dif))
						 				{
						 					var dif1 = parseFloat(item.shareAmount) -  parseFloat(dif);
						 					$scope.referralShareAmountPopup = parseFloat($scope.referralShareAmountPopup) + parseFloat(dif);
						 					$scope.hospitalShareAmountPopup =  (parseFloat($scope.hospitalShareAmountPopup) + parseFloat(dif1)).toFixed(2);
						 					
						 				}else{
						 					$scope.referralShareAmountPopup = parseFloat($scope.referralShareAmountPopup) + parseFloat(item.shareAmount);
						 				}
						 		
						 		}
						 }else{
							  $scope.hospitalShareAmountPopup =  (parseFloat($scope.hospitalShareAmountPopup) + parseFloat(item.shareAmount)).toFixed(2);
							  var docShrAmtPopup = parseFloat($scope.doctorShareAmountPopup) - parseFloat(item.shareAmount);
							  $scope.doctorShareAmountPopup = (docShrAmtPopup).toFixed(2);
							  
							  $scope.totalHospitaAndDoctorShareAmount = (parseFloat($scope.hospitalShareAmountPopup) + docShrAmtPopup).toFixed(2);
							  $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare = $scope.doctorShareAmountPopup;
							  $scope.doctorShareProcessList[$scope.parentListIndex].hospShare = $scope.hospitalShareAmountPopup;
						 }
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

			  if ($scope.savingInProgress) {
                 return;
              }
              $scope.savingInProgress = true;

			  if($scope.splitDocShareList.length > 0){
				  
				 /* var currDocShare = (($scope.currRefShare.netAmount * $scope.currRefShare.sharePer)/100);
				  var currDueShare = parseFloat(currDocShare) - parseFloat($scope.currRefShare.processedShareAmt);
				  var totalSplitAmt = currDocShare;
					$scope.splitDocShareList.forEach(function(value){
						totalSplitAmt = parseFloat(totalSplitAmt) + parseFloat(value.amount);
					});
				  var currHospShare = parseFloat($scope.currRefShare.netAmount) - parseFloat(totalSplitAmt); */
				  
				  var checkAmountDocShareAmount = 0;
				  $scope.splitDocShareList.forEach(function(value){
					  if(parseFloat(value.shareAmount) == 0 ){
						  checkAmountDocShareAmount = 1;
					  }
				  });
				  
				  if(checkAmountDocShareAmount == 1){
					  growl.warning('Doctor share amount should be greater than zero!',
								{
								});
						$scope.yesPopupClose();
						$scope.savingInProgress = false;
						return false;
				  }
				  $('#roomRentModal').modal('hide');

				  $scope.doctorSharAmt = parseFloat($scope.doctorSharAmt) - parseFloat($scope.doctorShareListSingle.docShare);
				  //current doctor share
				  var doctorShare = { doctorShareId: $scope.doctorShareProcessList[$scope.parentListIndex].doctorShareId,
						  doctorShareSplitId: $scope.doctorShareProcessList[$scope.parentListIndex].doctorShareSplitId,
						  docShare : $scope.doctorShareProcessList[$scope.parentListIndex].docShare,
						  hospShare :$scope.doctorShareProcessList[$scope.parentListIndex].hospShare,
						  dueShareAmt:$scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare,
						  sharePer : $scope.doctorShareProcessList[$scope.parentListIndex].sharePer,
						  totalDocShare : $scope.doctorShareProcessList[$scope.parentListIndex].totalDocShare,
						  };
				  
				  //New Added For Referral
				  if($scope.referralShareIdPopup > 0 && $scope.referralFlagForManupulation == true){
					  doctorShare.refDocShare = $scope.referralShareAmountPopup;
					  doctorShare.refDocShareId = $scope.referralShareIdPopup;
				  }
				  
				  $scope.doctorSharAmt = parseFloat($scope.doctorSharAmt) + parseFloat(doctorShare.docShare);
				  
				  if($scope.hospitalShareAmountPopupForCheck >= $scope.doctorShareProcessList[$scope.parentListIndex].hospShare && $scope.hospitalShareCheckboxPopupForCheck == true){
					  var diff = $scope.hospitalShareAmountPopupForCheck -  $scope.doctorShareProcessList[$scope.parentListIndex].hospShare;
					  $scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt = $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt;
					  $scope.hosSharAmt = $scope.hosSharAmt  - diff;
					  $scope.processedShareAmt = $scope.processedShareAmt - $scope.hospitalDueShareAmtPopupForCheck;
					  $scope.processedShareAmt =  $scope.processedShareAmt + $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt;
					  $scope.processedShareAmt = parseFloat($scope.processedShareAmt).toFixed(2);
					  $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = 0;
					  $scope.doctorShareProcessList[$scope.parentListIndex].tempDueShare =  $scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt;
				  }else if($scope.doctorShareProcessList[$scope.parentListIndex].hospShare >= $scope.hospitalShareAmountPopupForCheck && $scope.hospitalShareCheckboxPopupForCheck == true){
					  var diff = $scope.doctorShareProcessList[$scope.parentListIndex].hospShare - $scope.hospitalShareAmountPopupForCheck;
					  $scope.hosSharAmt = $scope.hosSharAmt  + diff;
					  $scope.processedShareAmt = $scope.processedShareAmt - $scope.hospitalDueShareAmtPopupForCheck;
					  $scope.processedShareAmt =  $scope.processedShareAmt + $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt;
					  $scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt = $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt;
					  $scope.processedShareAmt = parseFloat($scope.processedShareAmt).toFixed(2);
					  $scope.doctorShareProcessList[$scope.parentListIndex].dueShareAmt = 0;
					  $scope.doctorShareProcessList[$scope.parentListIndex].tempDueShare =  $scope.doctorShareProcessList[$scope.parentListIndex].tempProcessedShareAmt;
				  }
				  //New end For Referral
				  
				  //splitted doctor shares
				  var splittedShares = [];
				  $scope.splitDocShareList.forEach(function(value){
					let temp = {
							docShare:value.shareAmount,
							docId:value.docId,
							sharePer:value.newSharePercentage,
							docSpecId:value.docSpecId,
							doctorShareSplitId : value.doctorShareSplitId,
							doctorShareId : value.doctorShareId,
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
				  var URI = BASE_URL + ROOT_URL + "/api/billing/saveDoctorShareSplit";
				  BillingGenericService.serviceAction(METHOD_POST, URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						if (response.data.status == "success"){
							$scope.splitDoctorList = [];
							$scope.currRefShare = {};
							$scope.getdoctorShareProcesssList();
							// $scope.calculateAmounts();
							$('#roomRentModal').modal('hide');
							$rootScope.stopLoader();
						}else{
							$rootScope.stopLoader();
						}
						$scope.savingInProgress = false;
				  });
				  
				  
			  }else{
				  growl.warning("Please add at least one doctor",{
						title : WARNING_MSG
					});
				  $scope.savingInProgress = false;
			  }
		  }

        $scope.clearDoctorAndSpeciality = function(object){
            if(!object.doctorSharePopup){
              object.docSpecialityId = "";
              object.doctorId = "";
            }
        };
		
	}catch(e){
		console.log(e.message);
	}
	
}]);
