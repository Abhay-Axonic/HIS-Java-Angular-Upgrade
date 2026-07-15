angular.module("myApp").controller("preAuthorizationRequestController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','itemFactory','dataService','$state','PagerService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,itemFactory,dataService,$state,PagerService){
	 //$scope.pager = {};
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
		
		$scope.globalShareSearchObj={
				paymentEntitlementId : null
				
		};
		
		
		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.commonList = [];
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		
			
		$scope.init = function(){
			$scope.getPaymentEntitlementList();
			$scope.getCompanyMasterList(3);//companyId 3
			$scope.preAuthReqObj={paymentEntitlementId:"",companyId:"",visitTypeId:""}
			$rootScope.preAuthReqObjWithData={paymentEntitlement:"",insuranceName:"",contract:"",
					insuranceNo:"",visitDate:"",admissionNo:"",validityFrom:"",validityTo:"",companyId:"",visitTypeId:"",patientId:"",patientSponsorDetailsId:"",
					dob:"", uhidNumber:"",  patientName:"",genderCode:"" ,admissionId:"" ,companyTypeId:""}
			$scope.preAuthorizationRequestList=[];
			$scope.patientDetails={};
		}
		
		
		
		$scope.getPaymentEntitlementList = function() {
			var data = "";
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.paymentEntitlementList= response.data.listObject;
				//console.log("paymentEntitlementList",JSON.stringify($scope.paymentEntitlementList));
                $scope.commonListCount = response.data.listObject.length;
                $scope.initMasterList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
                $scope.setPage(1,false);
				$rootScope.stopLoader();
			});
			
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
		$scope.setNoOfRecords = function()
		{
			$scope.initMasterList($scope.orgId,
					$scope.orgUnitId, $scope.offset,
					$scope.noOfRecordsPerPage);
		};
		
		
	

		$scope.initMasterList = function(orgId,
				orgUnitId, offset,
				noOfRecordsPerPage) {

			try {
				$rootScope.startLoader();
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

				
				var param = 
			    {
		    		visitTypeId:$scope.preAuthReqObj.visitTypeId,
		    		paymentEntitlementId:$scope.preAuthReqObj.paymentEntitlementId,
		    		admissionNo:$scope.preAuthReqObj.admissionNo,
		    		companyId:$scope.preAuthReqObj.companyId,
		    		unitId:$scope.unitId,
		    		patientId:$scope.patientId,
		    		recordPerPage:noOfRecordsPerPage,
					offset:offset
			    };
				
				var param1 = 
			    {
		    		visitTypeId:$scope.preAuthReqObj.visitTypeId,
		    		paymentEntitlementId:$scope.preAuthReqObj.paymentEntitlementId,
		    		admissionNo:$scope.preAuthReqObj.admissionNo,
		    		companyId:$scope.preAuthReqObj.companyId,
		    		unitId:$scope.unitId,
		    		patientId:$scope.patientId
		    		
			    };
				 $rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
				$scope.commonList = response.data.listObject;
						  console.log("preAuthorizationRequestList",JSON.stringify($scope.preAuthorizationRequestList));
						
					}
					$rootScope.stopLoader();
				});
			    
			    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListCount"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param1,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						  $scope.commonListCount = response.data.object;
							$scope.setPage(1,false);
						
						  console.log("counnt",$scope.preAuthorizationRequestListCount);
						
						
					}
					$rootScope.stopLoader();
				});
			
				
			} catch (e) {
				console.log(e.message);
			}
		}	
		
		
		
		 $scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
           	try {
					$rootScope.startLoader();
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

					
					var param = 
				    {
			    		visitTypeId:$scope.preAuthReqObj.visitTypeId,
			    		paymentEntitlementId:$scope.preAuthReqObj.paymentEntitlementId,
			    		admissionNo:$scope.preAuthReqObj.admissionNo,
			    		companyId:$scope.preAuthReqObj.companyId,
			    		unitId:$scope.unitId,
			    		patientId:$scope.patientId,
			    		recordPerPage:noOfRecordsPerPage,
						offset:offset
				    };
					
					var param1 = 
				    {
			    		visitTypeId:$scope.preAuthReqObj.visitTypeId,
			    		paymentEntitlementId:$scope.preAuthReqObj.paymentEntitlementId,
			    		admissionNo:$scope.preAuthReqObj.admissionNo,
			    		companyId:$scope.preAuthReqObj.companyId,
			    		unitId:$scope.unitId,
			    		patientId:$scope.patientId
			    		
				    };
				    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							
							$scope.commonList = response.data.listObject;
							
						}
						$rootScope.stopLoader();
					});
				    
				    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListCount"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param1,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							// $scope.preAuthorizationRequestListCount = response.data.object;
						//	 $scope.companyMasterList = response.data.listObject;
							  console.log("counnt",$scope.preAuthorizationRequestListCount);
							 // $scope.commonListCount =  response.data.listObject;
								//$scope.setPage(1,false);
								
						}
						$rootScope.stopLoader();
					});
				
					
				} catch (e) {
					console.log(e.message);
				}
           }	
		   $scope.pager = {};
           $scope.page;
         

           $scope.setPage = function(page, flag) {
             		 if (page < 1 || page > $scope.pager.totalPages) {
                       return;
                   }

                   $scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage, $scope.unitId);
                   
                   if (flag) {
                       $scope.masterList($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize, $scope.unitId);
                   }
           };	
		
		
		
		
		
		
		
		$scope.selectPatientId = function(info, model,
				label) {
			
			$scope.patientName = info.patientName;
			$scope.uhidNumber = info.uhidNumber;
			$scope.mobileNumber = info.mobileNumber;
			$scope.patientId = info.patientId;
		}
		
		
		$scope.getAssociateCompanyMasterList = function(companyId) {
			$scope.tempcompanyId=companyId;
			$rootScope.startLoader();
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.associateCompanyMasterList = response.data.listObject;
						 // console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
					}
					$rootScope.stopLoader();
				});
			  }
			
			$scope.getCompanyMasterList = function(companyTypeId) {
				if(companyTypeId==3){
					
					$scope.isDisable=true;
					
				}else{
					$scope.isDisable=false;
				}
				$rootScope.startLoader();
				  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:companyTypeId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterList = response.data.listObject;
							 //console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
						}
						$rootScope.stopLoader();
					});
				  }
			
			 $scope.getVisitTypeList = function(searchPackageObj) {
				  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getVisitTypeList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.visityTypeList = response.data.listObject;
							  console.log("visityTypeList",JSON.stringify($scope.visityTypeList));
						}
					});
				  }
				$scope.getVisitTypeList();
				
				  /*========================= Serach Tab Funcationality Start==================*/
				
				$scope.searchPreAuthorizationRequest = function() 
				{
				 	 if($scope.preAuthReqObj.paymentEntitlementId == '' || typeof($scope.preAuthReqObj.paymentEntitlementId) == "undefined")
					 {
							growl.warning("Please select payment entitlement...!!",{
								title : 'Warning'
							});
							
							return false;
					  }else if($scope.preAuthReqObj.companyId == '' || typeof($scope.preAuthReqObj.companyId) == "undefined"){
										growl.warning("Please select Company/TPA...!!",{
											title : 'Warning'
										});
										return false;
							
							
				$rootScope.startLoader();
				    var param = 
				    {
			    		visitTypeId:$scope.preAuthReqObj.visitTypeId,
			    		paymentEntitlementId:$scope.preAuthReqObj.paymentEntitlementId,
			    		admissionNo:$scope.preAuthReqObj.admissionNo,
			    		companyId:$scope.preAuthReqObj.companyId,
			    		unitId:$scope.unitId,
			    		patientId:$scope.patientId != undefined ? $scope.patientId:null,
			    		recordPerPage:noOfRecordsPerPage,
						offset:offset
			    		
				    };
				    
				    var param1 = 
				    {
			    		visitTypeId:$scope.preAuthReqObj.visitTypeId,
			    		paymentEntitlementId:$scope.preAuthReqObj.paymentEntitlementId,
			    		admissionNo:$scope.preAuthReqObj.admissionNo,
			    		companyId:$scope.preAuthReqObj.companyId,
			    		unitId:$scope.unitId,
			    		patientId:$scope.patientId != undefined ? $scope.patientId:null,
			    	
			    		
				    };
				    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							$scope.commonList = response.data.listObject;
							  console.log("preAuthorizationRequestList",JSON.stringify($scope.preAuthorizationRequestList));
					
						}
						$rootScope.stopLoader();
					});
			 } 
					  else{
						$scope.initMasterList($scope.organizationId,$scope.unitId, $scope.offset,
								$scope.noOfRecordsPerPage);
				}
			
			 var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListCount"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param1,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.commonListCount = response.data.object;
							$scope.setPage(1,false);
					
					}
					$rootScope.stopLoader();
				});
			}
			
				
				
				
		
				
		
			
			$scope.editViewPreAuthorizationRequest = function(preAutobj,orgId,unitId, offset,
					noOfRecordsPerPage){ 
				try 
				{
					$scope.
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
							: 10;
					 data={
							 orgId:$scope.orgId,
							 unitId:$scope.unitId,
							 noOfRecordsPerPage : noOfRecordsPerPage,
							 offset : offset,
							 paymentEntitlementId:$scope.globalShareSearchObj.paymentEntitlementId!=null?$scope.globalShareSearchObj.paymentEntitlementId:null
					 }
			
			
				dataService.preAuthReqObjWithData.paymentEntitlement=preAutobj.paymentEntitlementDesc;
				dataService.preAuthReqObjWithData.companyTypeId=preAutobj.companyTypeId;
				dataService.preAuthReqObjWithData.insuranceName=preAutobj.insuranceDesc;
				dataService.preAuthReqObjWithData.companyDesc=preAutobj.companyDesc;
				dataService.preAuthReqObjWithData.contract=preAutobj.contractName;
				dataService.preAuthReqObjWithData.insuranceNo=preAutobj.companyTypeId ==2 ?preAutobj.insuranceNumber:'-';
				dataService.preAuthReqObjWithData.visitDate=preAutobj.visitDate;
				dataService.preAuthReqObjWithData.admissionNo=preAutobj.admissionNo;
				dataService.preAuthReqObjWithData.companyId=$scope.preAuthReqObj.companyId;
				dataService.preAuthReqObjWithData.visitTypeId =preAutobj.visitTypeId;
				dataService.preAuthReqObjWithData.patientId=preAutobj.patientId;
				dataService.preAuthReqObjWithData.patientSponsorDetailsId=preAutobj.patientSponsorDetailsId;
				dataService.preAuthReqObjWithData.dob=preAutobj.dob;
				dataService.preAuthReqObjWithData.uhidNumber=preAutobj.uhidNumber;
				dataService.preAuthReqObjWithData.patientName=preAutobj.patientName;
				dataService.preAuthReqObjWithData.genderCode=preAutobj.genderCode;
				dataService.preAuthReqObjWithData.admissionId=preAutobj.admissionId;
				dataService.preAuthReqObjWithData.validityFrom=preAutobj.validFrom;
				dataService.preAuthReqObjWithData.validityTo=preAutobj.validTo;
				dataService.preAuthReqObjWithData.contractId=preAutobj.contractId;
				dataService.preAuthReqObjWithData.dischargeDate=preAutobj.dischargeDate;
				dataService.preAuthReqObjWithData.bedNo=preAutobj.bedNo;
				dataService.preAuthReqObjWithData.age=preAutobj.age;
				dataService.preAuthReqObjWithData.doctorName=preAutobj.doctorName;
				dataService.preAuthReqObjWithData.imagePath=preAutobj.imagePath;
				dataService.preAuthReqObjWithData.genderId=preAutobj.genderId;
				dataService.preAuthReqObjWithData.payeeId=preAutobj.payeeId;
				$scope.patientDetails = {
						 "uhIdNumber":preAutobj.uhidNumber,
						 "patientName":preAutobj.patientName,
						 "birthDate":preAutobj.dob,
						"genderCode":preAutobj.genderCode,
						/* "age":$stateParams.subSpecimenObj.age,*/
						 
				   }
				
				
					   $rootScope.startLoader();
					    var param = {
					    		"payeeId":preAutobj.payeeId,
					    		"organisationId":$scope.orgId,
					    		"unitId":$scope.unitId,
					    		"patientId":preAutobj.patientId, 
					    		};
					    if(preAutobj.visitTypeId!=1)
					    	param.admissionId = preAutobj.admissionId;
					    else
					    	param.encounterId = preAutobj.admissionId;
					    
					    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListByPayeeId"
					    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								 $scope.getPreAuthorizationNewRequestListByPayeeId = response.data.listObject;
								 dataService.addNewRequestListByPayeeId=$scope.getPreAuthorizationNewRequestListByPayeeId;
									$state.go('editViewPreAuthorizationRequest');
									$scope.setPage(1,false);

							}
							$rootScope.stopLoader();
						});
				
				} catch (e) {
					console.log(e.message)
				}
			}
		
			
			
				$scope.init();
			
			
		
	}catch(e){
		console.log(e.message);
	}
}]);	