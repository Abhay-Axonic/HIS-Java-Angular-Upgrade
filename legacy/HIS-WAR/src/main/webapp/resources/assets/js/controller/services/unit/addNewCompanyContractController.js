angular.module("myApp").controller("addNewCompanyContractController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','itemFactory','GenericService','$http','$timeout','servicesFactory','$stateParams',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,itemFactory,GenericService,$http,$timeout,servicesFactory,$stateParams){
	try{
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.isSmartCardRequired = cookieObjectAppConfig.isSmartCardRequired;
		
		 if(cookieObjectAppConfig == undefined){
			 $state.go('applicationConfiguration');
		  return;
		 }
		 else
		  {
			 if((cookieObjectAppConfig.defaultSelfTariffId == '' || cookieObjectAppConfig.defaultSelfTariffId == null))
				 {
				  $state.go('applicationConfiguration');
				  return;
				 }
		   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
		   
		  }
		$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		$scope.companyPatientCategory = cookieObjectAppConfig.companyPatientCategory;
		$scope.insurancePatientCategory = cookieObjectAppConfig.insurancePatientCategory;
		$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		
		
		$scope.popUpFlag = true;
		
		/**DatePicker Code  */
		
		//$scope.fromDate = moment().startOf('day').toDate();
	   // $scope.toDate = moment().startOf('day').toDate();
	    $scope.completed='N';
		$scope.pending = 'N';
		$scope.outsourced = 'N';
		$scope.isDisable=false;
		
		$scope.contractForImport = {};
		$scope.itemSelecteds = {};
		$scope.fieldDisabled= false;
		$scope.dateOptions =
		{
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : "",
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

		
		
		
		
		
		$scope.initCompanyContarctObj = function(){
			$scope.companyContarctObj = {contractId:"",paymentEntitlementId:"",associateCompanyId:"",contractName:"",validityFrom:"",validityTo:"",gradeId:"",coSharePercentage:"",tariffId:"",isTariffRateApplicable:"",billingBedCategoryId:"",visitTypeId:"",patientTypeId:"",companyId:"",createdBy:"",updatedBy:"",isSmartApplicable:""}
		}
		$scope.init = function(){
			
			$scope.fromDate = null;
			$scope.toDate = null;
			$scope.companyCreditLimit = '';
			  $scope.specialityList = [];
			  $scope.groupDetailsList = [];
			  $scope.capAmountDetailsList = [];
			  $scope.groupWiseDetailsList=[]; 
			  servicesFactory.initServiceSearch($scope);
			  $scope.selectedServiceList = [];
			  $scope.notSelectedServiceList=[];
			  $scope.contractBedCategoryList = [];
			  $scope.billingBedCatoryFlag = false;
			  $scope.isTariffRatesApplicable = false;
			  $scope.searchServiceList = [];
			  $scope.capGroupWiseDetailsList = [];
			  $scope.initSubSpeciality();
			  $scope.initSpeciality();
			  $scope.patientTypeId='';
			  $scope.procedureSearchObj = {};
			  //$scope.setOrgUnit($scope.orgId,$scope.unitId);
			  
			   $scope.itemObj = {"tariffId":$scope.defaultSelfTariffId};
			   itemFactory.initItemFactory($scope);
			   $scope.pharamExcCategoryWiseList = [];
			   $scope.cAPAmtProductCategoryWiseList= [];
			   $scope.selectedItemList=[];
			   $scope.initCompanyContarctObj();
			   $scope.searchServObj = {};
			   $scope.arCode = '';
			   $scope.procedureWiseList = [];
			   $scope.pharamEquipmentList = [];
			   $scope.removeNotSelected = [];
			   $scope.capServiceWiseDetailsList = [];
			   $scope.selectedPackageList = [];
			   $scope.billingBedCategoryPackageList = [];
		}
		
		$scope.initSubSpeciality = function(){
			 $scope.subSpeciality = {subSpecialityName:"",subSpecialityId:""};
		}
		$scope.initSpeciality = function(){
			 $scope.speciality = {specialityName:"",specialityId:""};
		}
		$scope.init();
		
		//service factory methods 
		$scope.searchServices = function(searchObj){servicesFactory.searchServices(searchObj,$scope)}
		$scope.getSubSpecialityBySpecialityId = function(specialityId){servicesFactory.getSubSpecialityBySpecialityId(specialityId,$scope)}
		$scope.selectAllServices = function(flag){servicesFactory.selectAllServices(flag,$scope)}
		$scope.setOrgUnit = function(orgId,unitId,scope){servicesFactory.setOrgUnit($scope.orgId,$scope.unitId,$scope)}
		//item factory methods
		$scope.getProductCatByAssestCat = function(assetCategoryId,scope){itemFactory.getProductCatByAssestCat(assetCategoryId,$scope)}
		$scope.itemSearch = function(itemObj,scope){
			itemFactory.itemSearch(itemObj,$scope)}
		$scope.selectAllItems = function(flag,scope){itemFactory.selectAllItems(flag,$scope)}
		
		$scope.getPackageTypeList = function(orgId,unitId) {
			var data = "";
			var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.packageTypeList = response.data.listObject;
				console.log("packageTypeList",JSON.stringify($scope.packageTypeList));
			});
	}
		$scope.patientTypeId='';
		$scope.getPatientCategoryList = function() {
		var URI = BASE_URL + ROOT_URL+ GETACTIVEPATIENTCATEGORY;
		GenericService.serviceAction("GET", URI, "")
		.then(
				function(response) {
					$scope.patientCategoryList = [];
					if (response.data.status == "success") {
						$scope.patientCategoryList = response.data.listObject;
						console.log("patientCategorylist",$scope.patientCategoryList);
					}
				});
		}
		$scope.getPatientCategoryList();
		
	$scope.getTariffList = function(orgId,unitId) {
			var data = "";
			var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getTariff/"+orgId+"/"+unitId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.tariffList= response.data.listObject;
				console.log("tariffList",JSON.stringify($scope.tariffList));
			});
	}
	
	$scope.getPaymentEntitlementList = function() {
			var data = "";
			var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.paymentEntitlementList= response.data.listObject;
				console.log("paymentEntitlementList",JSON.stringify($scope.paymentEntitlementList));
			});
	}
	
	$scope.ledgerNameSearch = function(keyword)
    {
        var URI = BASE_URL + ROOT_URL
        + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
        var data = {
                "generalLedgerName":keyword
            }
        return GenericService
        .serviceAction("POST", URI, data)
        .then(
                function(response) {
                    if (response.data.status == "success") {
                        $rootScope.stopLoader();
                        return response.data.listObject;
                    }
                });
    }
    $scope.SelectLedgerName =function(ledger)
    {
        $scope.companyContarctObj.generalLedgerId=ledger.generalLedgerId;
    }
    $scope.getLedgerList = function(keyword)
    {
        $scope.ledgerList=[];
       var URI = BASE_URL + ROOT_URL
       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.orgId;
       var data = {
               
           }
        GenericService
       .serviceAction("POST", URI, data)
       .then(
               function(response) {
                   if (response.data.status == "success") {
                       $rootScope.stopLoader();
                       $scope.ledgerList=response.data.listObject;
                   }
               });
    }
    $scope.getLedgerList();
	$scope.getTariffList($scope.orgId,$scope.unitId);
	$scope.getPaymentEntitlementList();
	
	
	 /* $scope.getBedBillingCategoryList = function() {
		    var data = "";
		    var URI = BASE_URL + ROOT_URL + "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId;
		    BillingGenericService.serviceAction("GET", URI, data).then(function(response){
		     $scope.bedBillingCategoryList= response.data.listObject;
		     //console.log("bedBillingCategoryList",JSON.stringify($scope.bedBillingCategoryList));
		    });
		  }
	  $scope.getBedBillingCategoryList();*/
	  
	 
	  
	  
	  $scope.getVisitTypeList = function(searchPackageObj) {
		  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
		    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getVisitTypeList"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					 $scope.visityTypeList = response.data.listObject;
					  //console.log("visityTypeList",JSON.stringify($scope.visityTypeList));
				}
			});
		  }
		$scope.getVisitTypeList();
		
		$scope.getPackageTypeList = function() {
			var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+$scope.orgId+"/"+$scope.unitId;
			BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				$scope.packageTypeList = response.data.listObject;
			});
		}
		$scope.getPackageTypeList();
		
		$scope.getAssociateCompanyMasterList = function(companyId) {
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
				$rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.associateCompanyMasterList = response.data.listObject;
						//  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
					}
					$rootScope.stopLoader();
				});
			    $rootScope.startLoader();
			    //for company Detaills
			    var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyContractGradeByCompanyId/"+companyId;
				GenericService
						.serviceAction("GET", URI, data)
						.then(function(response) {
							if (response.data.status == "success") {
								$scope.gradeListByAssociateCompanyIdList = response.data.listObject;
								
									}
							$rootScope.stopLoader();
								});
			

				var data = {

				}
				console.log(data);
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterListById/unit/"+$scope.unitId+"/org/"+$scope.orgId+"/company/"+companyId;
				GenericService
						.serviceAction("GET", URI, data)
						.then(function(response) {
									if (response.data.status == "success") {
										$scope.CompanyObj=response.data.listObject[0];
										$scope.fromDate = new Date($scope.CompanyObj.startDate);
										$scope.toDate = new Date($scope.CompanyObj.endDate);
										$scope.arCode =response.data.listObject[0].arCode;
										$scope.companyCreditLimit = parseFloat(response.data.listObject[0].creditLimit);
										
										if (!angular.equals({},$stateParams.companyContractObj)) {
										$scope.fromDate = moment($stateParams.companyContractObj.validityFromString,"DD-MM-YYYY").toDate(); 
										$scope.toDate = moment($stateParams.companyContractObj.validityToString,"DD-MM-YYYY").toDate(); 
										}
									} 
									$rootScope.stopLoader();
								});
			    //End For Compan Details
			  }
			//$scope.getAssociateCompanyMasterList();
			
			

			/*$scope.getGradeListByAssociateCompanyId = function(searchPackageObj) {
							  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
							    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getGradeListByAssociateCompanyId"
							    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										 $scope.gradeListByAssociateCompanyIdList = response.data.listObject;
										 // console.log("gradeListByAssociateCompanyIdList",JSON.stringify($scope.gradeListByAssociateCompanyIdList));
									}
								});
							  }
							$scope.getGradeListByAssociateCompanyId();*/
						
			
			
			$scope.configPatientCategoryId = ''; 
			$scope.getCompanyMasterList = function(companyTypeId) {
				if(companyTypeId==3){
					$scope.isDisable=true;
					$scope.configPatientCategoryId = $scope.companyPatientCategory;
					
				}else{
					$scope.isDisable=false;
					$scope.configPatientCategoryId = $scope.insurancePatientCategory;
				}
					
				  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:companyTypeId};
					$rootScope.startLoader();
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterList = response.data.listObject;
							 //console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
						}
						$rootScope.stopLoader();
					});
				  }
				//$scope.getCompanyMasterList();
				
				
				 //get speciality list for autocomplete
			
			// For Speciality under Multiselect added by sneha
			$scope.selectedSpeacilityList = [];
			$scope.selectedSubSpecialityList = [];
			$scope.activeSpeciality=function(){
				var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.orgId
				};
				var URI = BASE_URL + ROOT_URL
						+ GETACTIVESPECIALITY;
				BillingGenericService
						.serviceAction(METHOD_POST, URI,
								commonObj,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									if (response.data.status == "success") {
										$scope.speacilityListMultiSelect = [];
										angular.forEach(response.data.listObject,
														function(value,index) {
															var data = {};
															data.id = value.specialityId;
															data.label = value.specialityName;
															$scope.speacilityListMultiSelect.push(data);
														});
									}
									console.log("speciality",$scope.speacilityListMultiSelect);
								});	
			}
			$scope.activeSpeciality();
			
			$scope.clicked = function(type) {
				
				
				
				if (type == 'speciality') {
					
					$scope.selectedSubSpecialityList = [];
					$scope.selectedServiceList = [];
					$scope.subSpecialityListMultiselect = [];
					if ($scope.selectedSpeacilityList.length > 0) {
						var specialityType = [];
						angular.forEach($scope.selectedSpeacilityList,
								function(value, index) {
									specialityType.push(value.id);
								});
						var data = {
							organizationId : $scope.organizationId,
							unitId : $scope.unitId,
							specialityIdList : specialityType
						}
						
						var isExists;
						function checkExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										return (val.id === arr.id);
									});
						}
						
						console.log(data);
						var URI = BASE_URL+ ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYARRAYFORTARIFF;
						BillingGenericService
								.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success") {
												$scope.subSpecialityListMultiselect = [];
												
												angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.subSpecialityId;
																	data.label = value.subSpecialityName;
																	data.specialityId = value.specialityId;
																	data.specialityName = value.specialityName;
																	console.log("data",JSON.stringify(data));
																	isExists = checkExistsOrNot($scope.subSpecialityListMultiselect,data);
																	if(!isExists)
																		{
																		   $scope.subSpecialityListMultiselect.push(data);
																		}
																});
												
											
											}
											console.log("subspeciality",$scope.subSpecialityListMultiselect);
											
										});
					}

				}
			}
			
				  $scope.getSpecialityList = function(){
						$rootScope.startLoader();
					  var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
					  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
					    BillingGenericService.serviceAction("POST", URI, param).then(function(response){
					    	if(response.data.status == "success")
					    		$scope.specialityList = response.data.listObject;
					    	
					    	$rootScope.stopLoader();
					    	// console.log("specialityList",JSON.stringify($scope.specialityList));
					    });
				  }
				  $scope.getSpecialityList();
			
				  $scope.selectSpeciality = function($item, $model,$label)
				  {
				       $scope.speciality.specialityId = $item.specialityId;
				       if($item.specialityId != ""){
				    	   $rootScope.stopLoader();
				    	   $scope.initSubSpeciality();
				    	   $scope.subSpecialityList = [];
				    	   $rootScope.startLoader();
				     var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId+"/specialityId/"+$item.specialityId;
				       //var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/"+$item.specialityId;
						BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success")
								$scope.subSpecialityList = response.data.listObject;
							
							$rootScope.stopLoader();
							//console.log("subSpecialityList",JSON.stringify($scope.subSpecialityList));
						});
				       }
				  }
				  
				  $scope.selectSubSpeciality = function($item, $model,$label){
					  $scope.subSpeciality.subSpecialityId = $item.subSpecialityMasterId;
				  }
				  
				  //For Procedure
				  
				  
				  $scope.searchProcedures = function(searchKeyword){
					  if($scope.procedureSearchObj.specialityId > 0){
			    		$scope.procedureMasterList=[];
			    		
			    		
			    		var URI = BASE_URL + ROOT_URL 
			    		     + "/global/procedureSearch/"+searchKeyword
			    		     +"/unit/"+$scope.unitId
			    		     +"/org/"+$scope.orgId
			    		     +"/speciality/"+$scope.procedureSearchObj.specialityId
			    		     console.log('URI',URI);
			    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			    			if(response.data.status == "success"){
			    				$scope.procedureMasterList = response.data.listObject;
			    			}
			    			return $scope.procedureMasterList;
			    		});
					  }
					  else
						  {
							  growl.warning("Please select a specialty",{
									title : "WARNING"
								});
								return;
						  }
					
			    	}
				  
				  $scope.selectProcedure = function($item, $model, $label)
			       {
			    	   $scope.procedureSearchObj.procedureId =  $item.serviceMasterId;
			    	   $scope.procedureSearchObj.procedureName =  $item.serviceStandardName;
			    	   $scope.procedureSearchObj.specialityId =  $item.specialityId;
			    	   $scope.procedureSearchObj.specialityName =  $item.specialityName;
			    	   $scope.procedureSearchObj.subSpecialityId =  $item.subSpecialityId;
			    	   $scope.procedureSearchObj.taxId =  $item.taxId;
			    	   $scope.procedureSearchObj.taxPercentage =  $item.taxPercentage;
			       }
				  
				  
				  var isProcedureExists;
					function checkProcedureExistsOrNot(arr, val) {
						return arr
								.some(function(arr) {
									if(val.serviceId == parseInt(arr.serviceId) && val.specialityId == parseInt(arr.specialityId))
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
					
					
					$scope.addProcedure = function(){
						
						
						if($scope.procedureSearchObj.specialityId == '' || typeof($scope.procedureSearchObj.specialityId) == "undefined"){
							growl.warning("Please select specialty",{
								title : "WARNING"
							});
							return;
						}
						if($scope.procedureSearchObj.procedureId == '' || typeof($scope.procedureSearchObj.procedureId) == "undefined"){
							growl.warning("Please select a procedure",{
								title : "WARNING"
							});
							return;
						}
						
					
								var temp = {
										serviceId:$scope.procedureSearchObj.procedureId,
										procedureName:$scope.procedureSearchObj.procedureName, 
										specialityId:  $scope.procedureSearchObj.specialityId,
										specialityName:  $scope.procedureSearchObj.specialityName,
										subSpecialityId:  $scope.procedureSearchObj.subSpecialityId,
										specialityName:  $scope.procedureSearchObj.specialityName,
										taxId:  $scope.procedureSearchObj.taxId,
										taxPercentage:  $scope.procedureSearchObj.taxPercentage,
										capAmount :parseFloat(0), 
								};
					
								isProcedureExists=checkProcedureExistsOrNot($scope.procedureWiseList,temp)
								if (!isProcedureExists) {
									$scope.procedureWiseList.push(temp);
									$scope.procedureSearchObj = {};
								}
								else
									{
										growl.warning("Already added in the list",{
											title : "WARNING"
										});
										return;
									}
						
								//$scope.cAPAmtProductCategoryWiseList = [];
					}
				  
				  
				  //For Procedure
		
				//---------------------Group Exclusion Section -----------------------------
					$scope.addGroupDetails = function(){
						
						if($scope.companyContarctObj.paymentEntitlementId && $scope.companyContarctObj.visitTypeId > 0 && $scope.companyContarctObj.companyId > 0 ){
							if($scope.speciality.specialityId == '' || typeof($scope.speciality.specialityId) == "undefined"){
								growl.error("Please select a group",{
									title : ERROR_MSG
								});
								return;
							}
							
							if($scope.subSpeciality.subSpecialityName == '' || typeof($scope.subSpeciality.subSpecialityName) == "undefined"){
								$scope.subSpeciality.subSpecialityId = '';
							}
							//alert($scope.subSpeciality.subSpecialityId);
							//return false;
							
							if($scope.speciality.specialityId != '' && $scope.subSpeciality.subSpecialityId == '')
								{
									var i = 0;
									angular.forEach($scope.subSpecialityList,function(value,index){
										
											var temp = {departmentId:$scope.speciality.specialityId, subDepartmentId:value.subSpecialityMasterId,
													groupDesc:$scope.speciality.specialityName,subGroupDesc:value.subSpecialityMasterName};
										
										isGroupExists=checkGroupIdExistsOrNot($scope.groupDetailsList,temp)
										  if (!isGroupExists) {
												//$scope.groupWiseDetailsList.push(temp);
											  $scope.groupDetailsList.push(temp);
										  }/*else{
												growl.error("Already group added in group exclusion",{	
													title : ERROR_MSG
												});
												return;
											}*/
										
										
										i++;
										if(i == $scope.subSpecialityList.length)
											{
												$scope.initSubSpeciality();
												$scope.initSpeciality();
											}
									});
						}else if($scope.speciality.specialityId != '' && $scope.subSpeciality.subSpecialityId != '')
							{
							
							
								//test
							
							var tempT = {departmentId:$scope.speciality.specialityId, subDepartmentId:$scope.subSpeciality.subSpecialityId};
						

							  isGroupExists=checkGroupIdExistsOrNot($scope.groupWiseDetailsList,tempT)
							  if (!isGroupExists) {
								  var temp = {departmentId:$scope.speciality.specialityId, subDepartmentId:$scope.subSpeciality.subSpecialityId,
											groupDesc:$scope.speciality.specialityName,subGroupDesc:$scope.subSpeciality.subSpecialityName};
								
								isGroupExists=checkGroupIdExistsOrNot($scope.groupDetailsList,temp)
								  if (!isGroupExists) {
										//$scope.groupWiseDetailsList.push(temp);
									  $scope.groupDetailsList.push(temp);
								  }else{
										growl.error("Already group added in group exclusion",{	
											title : ERROR_MSG
										});
										return;
									}
								$scope.initSubSpeciality();
								$scope.initSpeciality();
							  }
							  else{
								  growl.error("Already group added in group inclusion",{	
										title : ERROR_MSG
									});
									return;
							  }
							//test
								
								
								
							}
							   
						}
						else
							{
								growl.warning("Please Select Payment Entitlement,Company and VisitType First..!",{	
									title : "WARNING"
								});
								return;
							}
					}
					
					$scope.removeGroupDetails = function(index,obj){
						
						$scope.groupDetailsList.splice(index,1);
						
						$scope.selectedServiceList.splice($scope.selectedServiceList.indexOf(obj.subDepartmentId), 1);
						
					
					}
					
					
					$scope.addServices = function(){
						//$scope.selectedServiceList = [];
						//$scope.notSelectedServiceList = [];
							var groupIds = [];
							angular.forEach($scope.groupDetailsList,function(value,index){
								groupIds.push(value.departmentId);
							});
							
							$scope.searchServiceList.forEach(function(value,key){
							if(value.isSelected){
								$scope.selectedServiceList.push(value);
								console.log("selectedServiceList",JSON.stringify($scope.selectedServiceList));
							}/*else{
								if(groupIds.indexOf(value.specialityId) == -1)
									$scope.notSelectedServiceList.push(value);
								console.log("notSelectedServiceList",JSON.stringify($scope.notSelectedServiceList));
							}*/
						});
					}
					
					
					$scope.addGroupWiseServicesDetails = function(){
						//$scope.selectedServiceList = [];
						//$scope.notSelectedServiceList = [];
							var groupIds = [];
							angular.forEach($scope.groupDetailsList,function(value,index){
								groupIds.push(value.departmentId);
							});
							
							$scope.searchServiceList.forEach(function(value,key){
							if(value.isSelected){
								$scope.selectedServiceList.push(value);
								console.log("selectedServiceList",JSON.stringify($scope.selectedServiceList));
							}else{
								if(groupIds.indexOf(value.specialityId) == -1)
									isServiceNameExists=checkServiceMasterIdExistsOrNot($scope.selectedServiceList,value)
									
									if (!isServiceNameExists) {
										$scope.notSelectedServiceList.push(value);
									}else{
										console.log("Already group added in group exclusion",JSON.stringify($scope.notSelectedServiceList));
									}
								
							}
						});
					}
					
					
					$scope.removeSelectedService = function(index){
						$scope.selectedServiceList.splice(index,1);
					}
					
					
					$scope.removeNotSelectedService = function(index,item){
						console.log("removeNotSelectedService",item);
						$scope.removeNotSelected.push(item);
						
						angular.forEach($scope.removeNotSelected,function(value,index){

							$scope.searchServiceList.push(value);
						});
						
						
						$scope.notSelectedServiceList.splice(index,1);
						
					}
					
					$scope.removeNotSelectedServicePackage = function(index,item){
						
						$scope.selectedPackageList.splice(index,1);
						
					}
					
					//--------------------- Bed Category Section -----------------------------
					
					//Check Service in inclusion
					  var isPackageExists;
						function checkPackageExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										if(val.packageId === arr.packageId && val.packageTypeId === arr.packageTypeId && val.billingBedCategory === arr.bedBillingCategoryId)
											{
												return true;
											}
										else
											{
												return false;
											}
									});
						}
					
					//Check Service in inclusion
					  var isBillingBedExists;
						function checkBillingBedExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										if(val.bedCategoryId === arr.bedCategoryId)
											{
												return true;
											}
										else
											{
												return false;
											}
									});
						}
						
						//Check Service in For Package
						  var isBillingBedExistsPackage;
							function checkBillingBedExistsOrNotPackage(arr, val) {
								return arr
										.some(function(arr) {
											if(val.bedCategoryId === arr.bedCategoryId)
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
						
					$scope.bedCatName = function(id){
						angular.forEach($scope.bedBillingCategoryList,function(value,index){
							if(value.billingBedCategoryId == id){
								$scope.bedCategoryName = value.billingBedDesc;
							}
						});
					}
					
					
					$scope.addBedCategory = function(){
						if($scope.bedCategory.bedCategoryId != ""){
							$scope.bedCatName($scope.bedCategory.bedCategoryId);
							var obj = {bedCategoryId:$scope.bedCategory.bedCategoryId,bedCategoryDesc:$scope.bedCategoryName};
							
							isBillingBedExists=checkBillingBedExistsOrNot($scope.contractBedCategoryList,obj)
							if (!isBillingBedExists) {
								$scope.contractBedCategoryList.push(obj);
								isBillingBedExistsPackage=checkBillingBedExistsOrNotPackage($scope.billingBedCategoryPackageList,obj)
								if (!isBillingBedExistsPackage) {
									$scope.billingBedCategoryPackageList.push(obj);
								}
								
							}else{
								growl.error("Already added in list",{	
									title : ERROR_MSG
								});
								return;
							}
							
							
							
						}else{
							growl.error("Please select bed category",{
								title : ERROR_MSG
							});
						}
					}
					
					$scope.removeBedCategory = function(index){
						$scope.contractBedCategoryList.splice(index,1);
					}
					
					  //get bed category list
					  $scope.getBedCategoryList = function(){
						  /*var URI = BASE_URL + ROOT_URL + "/adt/getBedCategoryListByOrgUnit";
						  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
						    BillingGenericService.serviceAction("POST", URI, param).then(function(response){
						    	if(response.data.status == "success")
						    		$scope.bedCategoryList = response.data.listObject;
						    });*/
						    
						    
						    var data = "";
						    var URI = BASE_URL + ROOT_URL + "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId;
						    BillingGenericService.serviceAction("GET", URI, data).then(function(response){
						    	if(response.data.status == "success")
						    	$scope.bedCategoryList= response.data.listObject;
						     //console.log("bedBillingCategoryList",JSON.stringify($scope.bedBillingCategoryList));
						    });
						    
					  }
					  $scope.getBedCategoryList();
		
					//---------------------CAP Amount Section -----------------------------
					  
					  
					  $scope.addCAPAmountDetails = function(){
							if($scope.speciality.specialityId == '' || typeof($scope.speciality.specialityId) == "undefined"){
								growl.error("Please select a group",{
									title : ERROR_MSG
								});
								return;
							}
							if($scope.subSpeciality.subSpecialityId == '' || typeof($scope.subSpeciality.subSpecialityId) == "undefined"){
								growl.error("Please select a sub group",{
									title : ERROR_MSG
								});
								return;
							}
							var temp = {departmentId:$scope.speciality.specialityId, subDepartmentId:$scope.subSpeciality.subSpecialityId,
										groupDesc:$scope.speciality.specialityName,subGroupDesc:$scope.subSpeciality.subSpecialityName};
							
							isGroupExists=checkGroupIdExistsOrNot($scope.groupDetailsList,temp)
							if (!isGroupExists) {
								isGroupExists=checkGroupIdExistsOrNot($scope.capAmountDetailsList,temp)
								if (!isGroupExists) {
									$scope.capAmountDetailsList.push(temp);;
								}else{
									growl.error("Already group added in cap amount",{	
										title : ERROR_MSG
									});
									return;
								}
							}else{
								growl.error("Already group added in group exclusion",{	
									title : ERROR_MSG
								});
								return;
							}
							
							$scope.initSubSpeciality();
							$scope.initSpeciality();
						}
						
					  
					  
					//Check Service in inclusion
					  var isServiceExists;
						function checkServiceIdExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										if(val.serviceId === arr.serviceId)
											{
												return true;
											}
										else
											{
												return false;
											}
									});
						}
					  
					  //Check Group
					  var isGroupExists;
						function checkGroupIdExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										//return (val.departmentId === arr.departmentId);
										/*if(val.departmentId === arr.departmentId && (arr.subDepartmentId == '' || val.subDepartmentId == ''))
											{
												return true;
											}
										else */if(val.departmentId === arr.departmentId && arr.subDepartmentId == val.subDepartmentId)
											{
												return true;
											}
										else
											{
												return false;
											}
									});
						}
						//check subGroup
						 var isSubGroupExists;
							function checkSubGroupIdExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											return (val.subDepartmentId === arr.subSpecialityId);
										});
							}
							
							//check Service Name
							 var isServiceNameExists;
								function checkServiceMasterIdExistsOrNot(arr, value) {
									return arr
											.some(function(arr) {
												return (value.serviceMasterId === arr.serviceMasterId);
											});
								}
								
								//Check pharmacy
								  var isPharmacyExists;
									function checkPharmacyIdExistsOrNot(arr, val) {
										return arr
												.some(function(arr) {
													if(val.productCategoryId == parseInt(arr.productCategoryId) && val.assetCategoryId == parseInt(arr.assetCategoryId))
														{
															return true;
														}
													else
														{
															return false;
														}
												});
									}
									
									//for Equipment
									
									
									
									  var isPharmacyEquipmentExists;
									function checkPharmacyEquipmentExistsOrNot(arr, val) {
										return arr
												.some(function(arr) {
													if(val.productCategoryId == parseInt(arr.productCategoryId) && val.equipmentId == parseInt(arr.equipmentId))
														{
															return true;
														}
													else
														{
															return false;
														}
												});
									}
									
									//Check item
									
									var isItemExists;
									function checkItemIdExistsOrNot(arr, val) {
										return arr
												.some(function(arr) {
													if(val.itemId === arr.itemId )
														{
															return true;
														}
													else
														{
															return false;
														}
												});
									}
									//End
					  
						$scope.removeCAPAmountDetails = function(index,obj){
							  $scope.capAmountDetailsList.splice(index,1);
						
						}
						
						
						
						
						
						//FOR GET VISIT BY TARIFF ID New Work
						
						$scope.getVisitTypeIdbyTariff = function(id) 
						{
							
							$scope.companyContarctObj.visitTypeId = "";
							$scope.companyContarctObj.billingBedCategoryId = "";
                             $rootScope.startLoader();
                             $scope.visitList = [];
							 $scope.visitList.push(id);
							 
							var data = {
									tariffIdList : $scope.visitList,
								}
							
								var URI = BASE_URL+ ROOT_URL+ "/api/unit/getVisitTypeListByTariffIdList";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
											$scope.visitTypeList = [];
													if (response.data.status == "success") {
														$scope.visitTypeList = response.data.listObject;
														/*angular.forEach(response.data.listObject,
																		function(value,index) 
																		{
																			var data = {};
																			data.id = value.visitTypeId;
																			data.label = value.visitTypeName;
																			console.log("data.id",data.id);
																			console.log("data.label",data.label);
																			$scope.selectedVisitType.push(data);
																		});*/
													}
													$rootScope.stopLoader();
												});
								
							
						}
						
						$scope.getSelectedValueForBillingBedCategory=function(visitId){
							
								//$rootScope.startLoader();
								
								if(visitId == 2 || visitId == 4 || visitId == 3)
									{
										$scope.billingBedCatoryFlag = true;
										$scope.contractBedCategoryList = [];
										if($scope.isTariffRatesApplicable == true)
											{
											$scope.firstTabLevel = 3 ;
											}
										else{
											$scope.firstTabLevel = 1;
										}
									}
								else
									{
									$scope.billingBedCatoryFlag = false;
									$scope.contractBedCategoryList = [];
									}
								
								
								 $scope.selectedBillingCategory = [];
									var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+visitId+"/"+$scope.orgId+"/"+$scope.unitId;
								GenericService.serviceAction("GET", URI, "")
										.then(function(response) {
											$scope.bedBillingCategoryList = [];
													if (response.data.status == "success") {
														$scope.bedBillingCategoryList = response.data.listObject;
													}
													//$rootScope.stopLoader();
												});
						}
						
						$scope.getAllServiceForExclusion = function()
						{
							//$scope.specialityIds = [];
							$scope.subSpecialityIds = [];
							angular.forEach($scope.groupDetailsList,function(value,index){
								if(value.departmentId > 0 && value.subDepartmentId > 0)
									{
										$scope.subSpecialityIds.push(value.subDepartmentId);
									}
								/*if(value.departmentId > 0 && value.subDepartmentId == '')
									{
									  $scope.specialityIds.push(value.departmentId);
									}*/
								
							});
							
							//console.log("$scope.specialityIds",$scope.specialityIds);
							console.log("$scope.subSpecialityIds",$scope.subSpecialityIds);
							var data = {
									//specialityIds : $scope.specialityIds,
									subSpecialityList : $scope.subSpecialityIds,
									//tariffId :$scope.companyContarctObj.tariffId,
									organizationId:$scope.orgId,
									unitId : $scope.unitId,
									//effectiveDate : moment(new Date()).format('YYYY-MM-DD'),
									//paymentEntitlementId :$scope.companyContarctObj.paymentEntitlementId,
									//patientTypeId : $scope.configPatientCategoryId,
									//visitTypeId :$scope.companyContarctObj.visitTypeId,
									//billingBedCategoryId : $scope.companyContarctObj.billingBedCategoryId
							}
							$rootScope.startLoader();
								var URI = BASE_URL+ ROOT_URL+ "/api/unit/getServiceListBySubSpecialityIdNotByTariff";
							GenericService.serviceAction("POST", URI, data)
									.then(function(response) {
										$scope.searchServiceList = [];
												if (response.data.status == "success") {
													//$scope.bedBillingCategoryList = response.data.listObject;
													$scope.searchServiceList =[];
													angular.forEach(response.data.listObject,function(value,index){
														isServiceExistsForCapAmountListAlready=checkServiceIdExistsForCapAmountListAlready($scope.capServiceWiseDetailsList,value)
														  if (!isServiceExistsForCapAmountListAlready) {
															  $scope.searchServiceList.push(value);
														  }
													});
													
													  //New 26-7-2019
			      									  
			      							/*		isServiceExistsForCapAmountListAlready=checkServiceIdExistsForCapAmountListAlready($scope.capServiceWiseDetailsList,value)
													  if (!isServiceExistsForCapAmountListAlready) {*/
										               //End 26-7-2019
												
													if($scope.removeNotSelected.length > 0)
														{
														$scope.searchServiceList.push($scope.removeNotSelected);
														}
												}
												$rootScope.stopLoader();
											});
							
						}
						//END FOR GET VISIT BY TARIFF ID New Work
						
						
						
						//---------------------Group Wise Details Section -----------------------------
						  
						  
						  $scope.addGroupWiseDetails = function(){
							  
							  if($scope.companyContarctObj.paymentEntitlementId && $scope.companyContarctObj.visitTypeId > 0 && $scope.companyContarctObj.companyId > 0 ){
								if($scope.selectedSpeacilityList.length == 0){
									growl.error("Please select a group",{
										title : ERROR_MSG
									});
									return;
								}
								
								if($scope.selectedSubSpecialityList.length == 0){
									$scope.selectedSubSpecialityList=[];
								}
								 var coSh = 0;
								   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
									{
									   coSh = 0;
									}
								   else
									   {
									    coSh = $scope.companyContarctObj.coSharePercentage;
									   }
								   
								   
								   var subSpecialityIds = [];
									if($scope.selectedSpeacilityList.length > 0  && $scope.selectedSubSpecialityList.length == 0)
									{
										var i = 0;
										
										angular.forEach($scope.selectedSpeacilityList,function(value1,index1){
											angular.forEach($scope.speacilityListMultiSelect,function(value2,index2){
												if(value1.id == value2.id){
													value1.label = value2.label;
												}
											
											});
										});
										angular.forEach($scope.selectedSpeacilityList,function(value1,index1){
											angular.forEach($scope.subSpecialityListMultiselect,function(value,index){
												if(value1.id == value.specialityId){
													var temp = {
															    departmentId:value1.id,
															    subDepartmentId:value.id,
															    groupDesc:value1.label,
															    subGroupDesc:value.label,
															    apportionedPercent:0,
															    coShare:coSh
														    };
										

										isGroupExists=checkGroupIdExistsOrNot($scope.groupDetailsList,temp)
										if (!isGroupExists) {
											  isGroupExists=checkGroupIdExistsOrNot($scope.groupWiseDetailsList,temp)
											  if (!isGroupExists) {
													$scope.groupWiseDetailsList.push(temp);
													subSpecialityIds.push(value.id);
											  }
										
										}
										i++;
									
											}
										});
										});
										
										$scope.selectedSpeacilityList = [];
										$scope.selectedSubSpecialityList = [];
										$scope.subSpecialityListMultiselect = [];
									}
									else if($scope.selectedSpeacilityList.length > 0  && $scope.selectedSubSpecialityList.length > 0)
									{
										
									
										angular.forEach($scope.selectedSpeacilityList,function(value1,index1){
											angular.forEach($scope.speacilityListMultiSelect,function(value2,index2){
												if(value1.id == value2.id){
													value1.label = value2.label;
												}
											
											});
										});
										
										angular.forEach($scope.selectedSubSpecialityList,function(value1,index1){
											angular.forEach($scope.subSpecialityListMultiselect,function(value2,index2){
												if(value1.id == value2.id){
													value1.label = value2.label;
													value1.specialityId = value2.specialityId;
                                                    value1.specialityName = value2.specialityName;
												}
											
											});
										});
										
										
										angular.forEach($scope.selectedSpeacilityList,function(value1,index1){
										angular.forEach($scope.selectedSubSpecialityList,function(value,index){
												if(value1.id == value.specialityId){
										var temp = {
												/*	departmentId:$scope.speciality.specialityId, 
													subDepartmentId:$scope.subSpeciality.subSpecialityId,
													groupDesc:$scope.speciality.specialityName,
													subGroupDesc:$scope.subSpeciality.subSpecialityName,
													apportionedPercent:0,
													coShare:coSh*/
													
													    departmentId:value1.id,
													    subDepartmentId:value.id,
													    groupDesc:value1.label,
													    subGroupDesc:value.label,
													    apportionedPercent:0,
													    coShare:coSh
												};
										
										isGroupExists=checkGroupIdExistsOrNot($scope.groupDetailsList,temp)
										if (!isGroupExists) {
											  isGroupExists=checkGroupIdExistsOrNot($scope.groupWiseDetailsList,temp)
											  if (!isGroupExists) {
													$scope.groupWiseDetailsList.push(temp);
											  }else{
													growl.error("Already group added in group wise details",{	
														title : ERROR_MSG
													});
													return;
												}
										
										}else{
											growl.error("Already group added in group exclusion",{	
												title : ERROR_MSG
											});
											return;
										}
										subSpecialityIds.push(value.id);
										
												}
										});
										});
										$scope.selectedSpeacilityList = [];
										$scope.selectedSubSpecialityList = [];
										$scope.subSpecialityListMultiselect = [];
									}
								
								
								
								if(subSpecialityIds.length > 0)
								{
								//tt
								var data = {
										//specialityIds : $scope.specialityIds,
										subSpecialityList : subSpecialityIds,
										//tariffId :$scope.companyContarctObj.tariffId,
										organizationId:$scope.orgId,
										unitId : $scope.unitId,
										//effectiveDate : moment(new Date()).format('YYYY-MM-DD'),
										//paymentEntitlementId :$scope.companyContarctObj.paymentEntitlementId,
										//patientTypeId : $scope.configPatientCategoryId,
										visitTypeId :$scope.companyContarctObj.visitTypeId,
										//billingBedCategoryId : $scope.companyContarctObj.billingBedCategoryId
								}
								$rootScope.startLoader();
									var URI = BASE_URL+ ROOT_URL+ "/api/unit/getServiceListBySubSpecialityIdNotByTariff";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
											$scope.searchServiceList = [];
													if (response.data.status == "success") {
														//$scope.bedBillingCategoryList = response.data.listObject;
														
														angular.forEach(response.data.listObject,function(value,index){
															 
					      									  //New 26-7-2019
					      									  
					      									isServiceExistsForCapAmountListAlready=checkServiceIdExistsForCapAmountListAlready($scope.capServiceWiseDetailsList,value)
															  if (!isServiceExistsForCapAmountListAlready) {
												               //End 26-7-2019
														
															value.finalPrice = value.rate;
				     										value.serviceRate = value.serviceRate;
				     										value.apportionedPrice = value.serviceRate;
				     										value.taxPercentage = value.taxPercentage;
				     										value.taxId = value.taxId;
				     										 var amt = (value.finalPrice * coSh)/100;
				     										 value.selfPayable = parseFloat(amt).toFixed(2);
				     										 value.creditPayable = parseFloat(value.finalPrice - amt).toFixed(2);
				     										 value.temp = false;
				     										$scope.notSelectedServiceList.push(value);
				     										//New 26-7-2019
															  }
					      									 //End 26-7-2019
														});
													}
													$rootScope.stopLoader();
												});
							  }
							
								
								
						  }
							  else
								{
									growl.warning("Please Select Payment Entitlement,Company,Tarrif,VisitType and Billing Bed Category First..!",{	
										title : "WARNING"
									});
									return;
								}
						  
							}
							
							$scope.removeGroupWiseDetails = function(index,obj){
								
								isGroupExists=checkGroupIdExistsOrNot($scope.groupDetailsList,obj)
								if (!isGroupExists) {
										  $scope.groupDetailsList.push(obj);
								}
								
								 $scope.groupWiseDetailsList.splice(index,1);
								 
								 
								 angular.forEach($scope.capGroupWiseDetailsList,function(value,index1){
											if(value.subDepartmentId ==obj.subDepartmentId && value.departmentId ==obj.departmentId )
												{
												  $scope.capGroupWiseDetailsList.splice(index1,1);
												}
										});
								 
								 
								/* var temp = {departmentId:$scope.speciality.specialityId, subDepartmentId:$scope.subSpeciality.subSpecialityId,
											groupDesc:$scope.speciality.specialityName,subGroupDesc:$scope.subSpeciality.subSpecialityName};*/
								 
								 for (var i = $scope.notSelectedServiceList.length -1; i >= 0; i--) {
									 var value = $scope.notSelectedServiceList[i];
									 if(value.subSpecialityId == obj.subDepartmentId){
										 $scope.notSelectedServiceList.splice(i,1);
									  }else{
										 // $scope.notSelectedServiceList.push(value);
									  }
								}
								 
								 for (var i = $scope.selectedServiceList.length -1; i >= 0; i--) {
									 var value = $scope.selectedServiceList[i];
									 if(value.subSpecialityId == obj.subDepartmentId){
										 $scope.selectedServiceList.splice(i,1);
									  }else{
										 // $scope.notSelectedServiceList.push(value);
									  }
								}
								 
								 //$scope.notSelectedServiceList.splice($scope.notSelectedServiceList.indexOf(obj.subDepartmentId), 1);
									  
							}
							
							//-----------------------category wise consumables-----------------------------------
							$scope.categoryWise = {};
							$scope.addpharamExcCategory = function(){
								
								
								if($scope.categoryWise.assetCategoryId  == '' || typeof($scope.categoryWise.assetCategoryId ) == "undefined"){
									growl.error("Please select a asset category",{
										title : ERROR_MSG
									});
									return;
								}
								if($scope.categoryWise.productCategoryId == '' || typeof($scope.categoryWise.productCategoryId) == "undefined"){
									growl.error("Please select a product category",{
										title : ERROR_MSG
									});
									return;
								}
								
							
										var productCategory = "";
										angular.forEach($scope.productCategoryList,function(value,index){
											if(value.id == $scope.categoryWise.productCategoryId)
												productCategory = value.category;
										});
										
										//test
										 var temp1 = {assetCategoryId:$scope.categoryWise.assetCategoryId, productCategoryId:$scope.categoryWise.productCategoryId};
		                           	 	 
										 isPharmacyExists=checkPharmacyIdExistsOrNot($scope.cAPAmtProductCategoryWiseList,temp1)
		 									  if (!isPharmacyExists) {
		 										 var temp = {assetCategoryId:$scope.categoryWise.assetCategoryId,productCategoryId:$scope.categoryWise.productCategoryId, category:productCategory, qty:0};
		 										
		 										isPharmacyExists=checkPharmacyIdExistsOrNot($scope.pharamExcCategoryWiseList,temp)
		 										if (!isPharmacyExists) {
		 											$scope.pharamExcCategoryWiseList.push(temp);
		 												  $scope.categoryWise = {};
		 										}
		 										else
		 											{
		 												growl.warning("Already added in the group",{
		 													title : "WARNING"
		 												});
		 												return;
		 											}
		 									  }else
		 										  {
			 										 growl.error("Already product added in inclusion",{	
			 											title : ERROR_MSG
			 										});
			 										return;
		 										  }
										//test
										/*var temp = {assetCategoryId:$scope.categoryWise.assetCategoryId,productCategoryId:$scope.categoryWise.productCategoryId, category:productCategory, qty:0};
										
										isPharmacyExists=checkPharmacyIdExistsOrNot($scope.pharamExcCategoryWiseList,temp)
										if (!isPharmacyExists) {
											$scope.pharamExcCategoryWiseList.push(temp);
												  $scope.categoryWise = {};
										}
										else
											{
												growl.warning("Already added in the group",{
													title : "WARNING"
												});
												return;
											}*/
										//$scope.cAPAmtProductCategoryWiseList = [];
							}
							
							$scope.getPharmactItems = function()
							{
								if($scope.pharamExcCategoryWiseList.length > 0){
								var assetsCatId = [];
								var prodCatId = [];
								angular.forEach($scope.pharamExcCategoryWiseList,function(value,index){
									prodCatId.push(value.productCategoryId);
									assetsCatId.push(value.assetCategoryId);
								});
								
								
								var data = {
										assetCategoryList : assetsCatId,
										productCategoryList : prodCatId,
										orgId:$scope.orgId,
										unitId : $scope.unitId,
								}
								$rootScope.startLoader();
									var URI = BASE_URL+ ROOT_URL+ "/unit/CompanyContract/getItemListByAssetAndProductCategory";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
											$scope.searchItemList = [];
													if (response.data.status == "success") {
														$scope.searchItemList = response.data.listObject;
													}
													$rootScope.stopLoader();
												});
							}
								
							}
							
							$scope.getProductCatByAssestCatForEquipment = function(assetCategoryId)
							{
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+assetCategoryId +"/" + $scope.orgId;
								GenericService.serviceAction(METHOD_GET, URI, data)
								.then(function(response){
									$rootScope.stopLoader();
									if(response.status == 200)
										$scope.productCategoryEquipmentList = response.data;
									else
										$scope.productCategoryEquipmentList = [];
									
									//scope.itemObj.productCategoryId = "";
								});
							}
							
							
							$scope.categoryWiseEquipment = {};
							$scope.addpharamExcCategoryEquipment = function(){
								
								
								if($scope.categoryWiseEquipment.assetCategoryId  == '' || typeof($scope.categoryWiseEquipment.assetCategoryId ) == "undefined"){
									growl.error("Please select a asset category",{
										title : ERROR_MSG
									});
									return;
								}
								if($scope.categoryWiseEquipment.productCategoryId == '' || typeof($scope.categoryWiseEquipment.productCategoryId) == "undefined"){
									growl.error("Please select a product category",{
										title : ERROR_MSG
									});
									return;
								}
								if($scope.categoryWiseEquipment.equipmentId == '' || typeof($scope.categoryWiseEquipment.equipmentId) == "undefined"){
									growl.error("Please select a Equipment",{
										title : ERROR_MSG
									});
									return;
								}
								
							
										/*angular.forEach($scope.productCategoryList,function(value,index){
											if(value.id == $scope.categoryWise.productCategoryId)
												productCategory = value.category;
										});*/
																			
										var itemObj = $.grep($scope.equipmentFinalList, function (disCat) {
								            return disCat.itemId == $scope.categoryWiseEquipment.equipmentId;
								        })[0];
										
										var productCategoryObj = $.grep($scope.productCategoryEquipmentList, function (disCat) {
								            return disCat.id == $scope.categoryWiseEquipment.productCategoryId;
								        })[0];
										
										var temp = {
												//assetCategoryId:$scope.categoryWiseEquipment.assetCategoryId,
												productCategoryId:$scope.categoryWiseEquipment.productCategoryId, 
												category:productCategoryObj.category,
												equipmentId:itemObj.itemId,
												equipmentName:itemObj.itemName,
												//amount:parseFloat(0)
												};
										
										isPharmacyEquipmentExists=checkPharmacyEquipmentExistsOrNot($scope.pharamEquipmentList,temp)
										if (!isPharmacyEquipmentExists) {
											$scope.pharamEquipmentList.push(temp);
										    $scope.categoryWiseEquipment = {};
										}
										else
											{
												growl.warning("Already added in the group",{
													title : "WARNING"
												});
												return;
											}
										//$scope.cAPAmtProductCategoryWiseList = [];
							}
							
							 $scope.getEquipmentList = function(){
								 /* if($scope.procedureSearchObj.specialityId > 0){*/
								 if($scope.categoryWiseEquipment.assetCategoryId  == '' || typeof($scope.categoryWiseEquipment.assetCategoryId ) == "undefined"){
										growl.error("Please select a asset category",{
											title : ERROR_MSG
										});
										return;
									}
								 else if($scope.categoryWiseEquipment.productCategoryId == '' || typeof($scope.categoryWiseEquipment.productCategoryId) == "undefined"){
										growl.error("Please select a product category",{
											title : ERROR_MSG
										});
										return;
									}
								 else{
									 
									 $rootScope.startLoader();
									
						    		$scope.procedureMasterList=[];
						    		
						    		var data = {
						    				productCategoryId :$scope.categoryWiseEquipment.productCategoryId,
						    				unitId : $scope.unitId
						    		}
						    		var URI = BASE_URL + ROOT_URL 
						    		     + "/unit/CompanyContract/getItemListByProductCategoryForEquipment";
						    		     console.log('URI',URI);
						    		BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						    			 $rootScope.stopLoader();
						    			if(response.data.status == "success"){
						    				$scope.equipmentFinalList = response.data.listObject;
						    			}
						    		});
								 }
								/*  }
								  else
									  {
										  growl.warning("Please select a specialty",{
												title : "WARNING"
											});
											return;
									  }*/
								
						    	}
							
							//$scope.capAmountTotal = parseFloat(0);
							$scope.calculateSummaryAmt = function()
							{
								if($scope.capAmountTotalProcedure ==  undefined) // if a is negative,undefined,null,empty value then...
								{
								//$scope.capAmountTotal = parseFloat(0);
								$scope.capAmountTotalGroup = parseFloat(0);
								}
								else
									{
									 // $scope.capAmountTotal = parseFloat(0);
									  $scope.capAmountTotalGroup = parseFloat(0);
									  $scope.capAmountTotalGroup = $scope.capAmountTotalGroup  + parseFloat($scope.capAmountTotalProcedure);
									}
								$scope.capAmountTotal = parseFloat(0);
								angular.forEach($scope.capGroupWiseDetailsList,function(value,index){
									
									$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) + parseFloat(value.subDepartmentCapAmount)).toFixed(2);
									$scope.capAmountTotalGroup = (parseFloat($scope.capAmountTotalGroup) + parseFloat(value.subDepartmentCapAmount)).toFixed(2);
									if($scope.capAmountTotalGroup > $scope.companyCreditLimit)
										{
										$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) - parseFloat(value.subDepartmentCapAmount)).toFixed(2);
										$scope.capAmountTotalGroup = (parseFloat($scope.capAmountTotalGroup) - parseFloat(value.subDepartmentCapAmount)).toFixed(2);
										value.subDepartmentCapAmount = 0;
										var remaingAmt = (parseFloat($scope.companyCreditLimit) - parseFloat($scope.capAmountTotalGroup)).toFixed(2);
										growl.warning("Cap amount greater then company credit limit.</br>Remining Credit Limit : "+remaingAmt,{	
											title : "WARNING"
										});
										return;
										}
								});
								/*}
								else
									{
									angular.forEach($scope.capGroupWiseDetailsList,function(value,index){
										$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) + parseFloat(value.subDepartmentCapAmount)).toFixed(2);
										if($scope.capAmountTotal > $scope.companyCreditLimit)
											{
											$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) - parseFloat(value.subDepartmentCapAmount)).toFixed(2);
											value.subDepartmentCapAmount = 0;
											var remaingAmt = (parseFloat($scope.companyCreditLimit) - parseFloat($scope.capAmountTotal)).toFixed(2);
											growl.warning("Cap amount greater then company credit limit.</br>Remining Credit Limit : "+remaingAmt,{	
												title : "WARNING"
											});
											return;
											}
									});
									
									}*/
								
							}
							/*ng-blur="calculateSummaryAmtProcedure()" */
							$scope.calculateSummaryAmtProcedure = function()
							{
								
								/*if($scope.capAmountTotal ==  undefined) // if a is negative,undefined,null,empty value then...
								{*/
								if($scope.capAmountTotal ==  undefined) // if a is negative,undefined,null,empty value then...
								{
								   // $scope.capAmountTotalProcedure = parseFloat(0);
								    $scope.capAmountTotalProcedureTemp = parseFloat(0);
								}
								else
									{
									//$scope.capAmountTotalProcedure = parseFloat(0);
									$scope.capAmountTotalProcedureTemp = parseFloat(0);
									 $scope.capAmountTotalProcedureTemp = $scope.capAmountTotalProcedureTemp + parseFloat($scope.capAmountTotal);
									}
								 $scope.capAmountTotalProcedure = parseFloat(0);
								    angular.forEach($scope.procedureWiseList,function(value,index){
								    	
										$scope.capAmountTotalProcedure = (parseFloat($scope.capAmountTotalProcedure) + parseFloat(value.capAmount)).toFixed(2);
										$scope.capAmountTotalProcedureTemp = (parseFloat($scope.capAmountTotalProcedureTemp) + parseFloat(value.capAmount)).toFixed(2);
										if($scope.capAmountTotalProcedureTemp > $scope.companyCreditLimit)
											{
											$scope.capAmountTotalProcedure = (parseFloat($scope.capAmountTotalProcedure) - parseFloat(value.capAmount)).toFixed(2);
											$scope.capAmountTotalProcedureTemp = (parseFloat($scope.capAmountTotalProcedureTemp) - parseFloat(value.capAmount)).toFixed(2);
											value.capAmount = 0;
											var remaingAmt = (parseFloat($scope.companyCreditLimit) - parseFloat($scope.capAmountTotalProcedureTemp)).toFixed(2);
											growl.warning("Cap amount greater then company credit limit.</br>Remining Credit Limit : "+remaingAmt,{	
												title : "WARNING"
											});
											return;
											}
									});
								/*    
								}
								else {
									  angular.forEach($scope.procedureWiseList,function(value,index){
											$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) + parseFloat(value.capAmount)).toFixed(2);
											if($scope.capAmountTotal > $scope.companyCreditLimit)
												{
												$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) - parseFloat(value.capAmount)).toFixed(2);
												value.subDepartmentCapAmount = 0;
												var remaingAmt = (parseFloat($scope.companyCreditLimit) - parseFloat($scope.capAmountTotal)).toFixed(2);
												growl.warning("Cap amount greater then company credit limit.</br>Remining Credit Limit : "+remaingAmt,{	
													title : "WARNING"
												});
												return;
												}
										});
								}*/
								/*angular.forEach($scope.procedureWiseList,function(value,index){
									$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) + parseFloat(value.capAmount)).toFixed(2);
									if($scope.capAmountTotal > $scope.companyCreditLimit)
										{
										$scope.capAmountTotal = (parseFloat($scope.capAmountTotal) - parseFloat(value.capAmount)).toFixed(2);
										value.subDepartmentCapAmount = 0;
										var remaingAmt = (parseFloat($scope.companyCreditLimit) - parseFloat($scope.capAmountTotal)).toFixed(2);
										growl.warning("Cap amount greater then company credit limit.</br>Remining Credit Limit : "+remaingAmt,{	
											title : "WARNING"
										});
										return;
										}
								});*/
							}
							
							
							
							$scope.refreshCompanyContract = function(){
								
								console.log("groupWiseDetailsList",$scope.groupWiseDetailsList);
								
								$scope.subSpecialityIds = [];
								angular.forEach($scope.groupWiseDetailsList,function(value,index){
									if(value.departmentId > 0 && value.subDepartmentId > 0)
										{
											$scope.subSpecialityIds.push(value.subDepartmentId);
										}
									
								});
								
								//console.log("$scope.specialityIds",$scope.specialityIds);
								console.log("$scope.subSpecialityIds",$scope.subSpecialityIds);
								var data = {
										subSpecialityList : $scope.subSpecialityIds,
										organizationId:$scope.orgId,
										unitId : $scope.unitId,
										contractId : $scope.oldContractId
								}
								
								var URI = BASE_URL+ ROOT_URL+ "/api/unit/getServiceListBySubSpecialityIdNotByTariff";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
											$scope.searchServiceList = [];
													if (response.data.status == "success") {
														$scope.notSelectedServiceList = [];
														var data = {
												                   orgId : $scope.organizationId,
												                   unitId: $scope.unitId,
												                   contractId:$scope.itemSelecteds.contractId
												         }
														var URI = BASE_URL + ROOT_URL+ "/unit/CompanyContract/getCompanyContractDetailByContractId";
													GenericService.serviceAction("POST", URI, data).then(function(response) {
																if (response.data.status == "success") {
																	
																	angular.forEach(response.data.object.listTContractServiceDetailsDto,function(value,index){
																		var temp2 = {finalPrice:value.finalPrice,serviceRate:value.servicePrice,
																				apportionedPrice:value.apportionedPrice,taxPercentage:value.taxPercentage,
																				selfPayable:value.selfPayable,creditPayable:value.creditPayable,
																				serviceId:value.serviceId,
																				specialityDesc:value.departmentName,
																				subSpecialityDesc:value.subDepartmentName,
																				serviceDesc:value.serviceName,
																				specialityId:value.departmentId,
					                                                            subSpecialityId:value.subDepartmentId,
					                                                            temp : true,

																				
																		};
															           $scope.notSelectedServiceList.push(temp2);
															      });
																	
																}
															});
													  
														
														
														
														
														
														
														
														angular.forEach(response.data.listObject,function(value,index){
															 
					      									  //New 26-7-2019
					      									  
					      									isServiceExistsForCapAmountListAlready=checkServiceIdExistsForCapAmountListAlready($scope.capServiceWiseDetailsList,value)
															  if (!isServiceExistsForCapAmountListAlready) {
												               //End 26-7-2019
														
																  angular.forEach($scope.groupWiseDetailsList,function(value1,index1){
																		if(value1.departmentId == value.specialityId && value1.subDepartmentId == value.subSpecialityId)
																			{
																			   var percentAmount = parseFloat(0);
																			   if(value1.apportionedPercent > 0 ){
																				   percentAmount = isNaN(value1.apportionedPercent) && isNaN(value.serviceRate) ? parseFloat(value.serviceRate) : (parseFloat(value.serviceRate) / 100) *  parseFloat(value1.apportionedPercent);
																			   }
																			var apportionedPrice = value.serviceRate;
																			switch(value1.varianceUpDown){
																			case 1 :
																				apportionedPrice = parseFloat(percentAmount) + parseFloat(value.serviceRate);
																				break;
																			case 2 :
																				apportionedPrice = parseFloat(value.serviceRate) - parseFloat(percentAmount);
																				break;
																			
																			}
																			var gstAmt =  ((parseFloat(apportionedPrice) * value.taxPercentage)/100);
																			value.finalPrice =parseFloat(apportionedPrice) + gstAmt;
																			value.apportionedPrice = $rootScope.parseFloatNum(apportionedPrice);
																			var amt = (value.finalPrice * value1.coShare)/100;
								    										 value.selfPayable = parseFloat(amt).toFixed(2);
								    										 value.creditPayable = parseFloat(value.finalPrice - amt).toFixed(2);
								    										 value.temp = false;
								    										 $scope.notSelectedServiceList.push(value);
																			
																			}
																	});
															/*value.finalPrice = value.rate;
				     										value.serviceRate = value.serviceRate;
				     										value.apportionedPrice = value.serviceRate;
				     										value.taxPercentage = value.taxPercentage;
				     										value.taxId = value.taxId;
				     										 var amt = (value.finalPrice * coSh)/100;
				     										 value.selfPayable = parseFloat(amt).toFixed(2);
				     										 value.creditPayable = parseFloat(value.finalPrice - amt).toFixed(2);
				     										$scope.notSelectedServiceList.push(value);*/
				     										//New 26-7-2019
															  }
					      									 //End 26-7-2019
														});
													}
													$rootScope.stopLoader();
												});
									
									
							}
							
							
							$scope.removepharamExcCategory = function(item,index){
								$scope.pharamExcCategoryWiseList.splice(index,1);
								//selectedItemList
								 for (var i = $scope.selectedItemList.length -1; i >= 0; i--) {
									 var value = $scope.selectedItemList[i];
									 if(value.assetCategoryId == item.assetCategoryId && value.productCategoryId == item.productCategoryId){
										 $scope.selectedItemList.splice(i,1);
									  }else{
										 // $scope.notSelectedServiceList.push(value);
									  }
								}
							}
							
							
						
							$scope.addCAPAmtProductCategoryWise = function(catWise){
								
								var coSh = 0;
								   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
									{
									   coSh = 0;
									}
								   else
									   {
									    coSh = $scope.companyContarctObj.coSharePercentage;
									   }
								   
								   if(catWise.assetCategoryId == '' || catWise.assetCategoryId == undefined || catWise.assetCategoryId == null){
									   growl.warning("Please select asset category first..!!",{	
											title : WARNING_MSG
										});
										return false;
								   }
								   if(catWise.productCategoryId == '' || catWise.productCategoryId == undefined || catWise.productCategoryId == null){
									   growl.warning("Please select product category..!!",{	
											title : WARNING_MSG
										});
										return false;
								   }
								   //cAPAmtProductCategoryWiseList
								  // pharamExcCategoryWiseList
								   //Test
								   
								  /* var temp1 = {assetCategoryId:catWise.assetCategoryId, productCategoryId:catWise.productCategoryId};
                           	 	 
								 isPharmacyExists=checkPharmacyIdExistsOrNot($scope.cAPAmtProductCategoryWiseList,temp1)
 									  if (!isPharmacyExists) {
 											$scope.cAPAmtProductCategoryWiseList.push(temp1);
 									  }*/
								 
								   //Test
								var productCategory = "";
								angular.forEach($scope.productCategoryList,function(value,index){
									if(value.id == catWise.productCategoryId)
										productCategory = value.category;
								});
								var temp = {productCategoryId:catWise.productCategoryId, category:productCategory, qty:0,amount:0,coShare:coSh,assetCategoryId:catWise.assetCategoryId};
								
									isProductExists=checkProductIdExistsOrNot($scope.pharamExcCategoryWiseList,temp)
								if (!isProductExists) {
									
									//Test
									 var temp1 = {assetCategoryId:catWise.assetCategoryId, productCategoryId:catWise.productCategoryId};
	                           	 	 
									 isPharmacyExists=checkPharmacyIdExistsOrNot($scope.cAPAmtProductCategoryWiseList,temp1)
	 									  if (!isPharmacyExists) {
	 											$scope.cAPAmtProductCategoryWiseList.push(temp);
	 									  }else
	 										  {
		 										 growl.error("Already product added in inclusion",{	
		 											title : ERROR_MSG
		 										});
		 										return;
	 										  }
									 
									//Test
									 //OLD
									//$scope.cAPAmtProductCategoryWiseList.push(temp)
								}else{
									growl.error("Already product added in pharmacy exclusion",{	
										title : ERROR_MSG
									});
									return;
								}
								
							}
							
							$scope.changeQuantity = function(qty,idx)
							{
								if(qty > 0)
									{
										$scope.cAPAmtProductCategoryWiseList[idx].amount = 0;
									}
							}
							
							$scope.changeQuantityServiceWise = function(qty,idx)
							{
								if(qty > 0)
									{
										$scope.capServiceWiseDetailsList[idx].serviceCapAmount = 0;
									}
							}
							
							$scope.changeAmount = function(amt,idx)
							{
								if(amt > 0)
								{
									$scope.cAPAmtProductCategoryWiseList[idx].qty = 0;
								}
							}
							$scope.changeAmountServiceWise = function(amt,idx)
							{
								if(amt > 0)
								{
									$scope.capServiceWiseDetailsList[idx].qty = 0;
								}
							}
							
							$scope.removeCAPAmtProductCategoryWise = function(index,item){
								
								$scope.cAPAmtProductCategoryWiseList.splice(index,1);
								
								var temp = {assetCategoryId:item.assetCategoryId,productCategoryId:item.productCategoryId, category:item.category};
								
								isPharmacyExists=checkPharmacyIdExistsOrNot($scope.pharamExcCategoryWiseList,temp)
								if (!isPharmacyExists) {
									$scope.pharamExcCategoryWiseList.push(temp);
								}
								
								 for (var i = $scope.selectedItemList.length -1; i >= 0; i--) {
									 var value = $scope.selectedItemList[i];
									 if(value.assetCategoryId == item.assetCategoryId && value.productCategoryId == item.productCategoryId){
										 $scope.selectedItemList.splice(i,1);
									  }else{
										 // $scope.notSelectedServiceList.push(value);
									  }
								}
								 
								
							}
							
                          $scope.removeCAPAmtSeriveWise = function(index,item){
                        	 /* if($scope.companyContarctObj.paymentEntitlementId && $scope.companyContarctObj.visitTypeId > 0 && $scope.companyContarctObj.companyId > 0 ){
                        	  var coSh = 0;
							   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
								{
								   coSh = 0;
								}
							   else
								   {
								    coSh = $scope.companyContarctObj.coSharePercentage;
								   }
							   
								var data = {};
								
								data.serviceCode =  $scope.capServiceWiseDetailsList[index].serviceCode;
								data.serviceDesc =  $scope.capServiceWiseDetailsList[index].serviceName;
								data.specialityId = $scope.capServiceWiseDetailsList[index].departmentId;
								data.specialityDesc = $scope.capServiceWiseDetailsList[index].groupDesc;
								data.serviceId = $scope.capServiceWiseDetailsList[index].serviceId;
								data.subSpecialityId = $scope.capServiceWiseDetailsList[index].subDepartmentId;
								data.subSpecialityDesc = $scope.capServiceWiseDetailsList[index].subGroupDesc;
								data.taxId  = $scope.capServiceWiseDetailsList[index].taxId;
								data.serviceRate =$scope.capServiceWiseDetailsList[index].basePriceForContract;
								data.apportionedPrice = $scope.capServiceWiseDetailsList[index].basePriceForContract;
								data.taxPercentage = $scope.capServiceWiseDetailsList[index].taxPercentage;
								var taxAmt = ($scope.capServiceWiseDetailsList[index].basePriceForContract * $scope.capServiceWiseDetailsList[index].taxPercentage)/100; 
								data.finalPrice = $scope.capServiceWiseDetailsList[index].basePriceForContract + taxAmt;
								 var amt = (data.finalPrice * coSh)/100;
								 data.selfPayable = parseFloat(amt).toFixed(2);
								 data.creditPayable = parseFloat(data.finalPrice - amt).toFixed(2);
								$scope.notSelectedServiceList.push(data);
                        	  }*/
								$scope.capServiceWiseDetailsList.splice(index,1);
								
								
							}
							
							
							  //Check Product Wise Category
							  var isProductExists;
								function checkProductIdExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												return (val.productCategoryId === arr.productCategoryId);
											});
								}
							
							
							$scope.addCategoryExclusionItems = function(){
								$scope.categoryWiseExclusionItemsList = [];
								angular.forEach($scope.searchItemList,function(value,index){
									if(value.isSelected){
										$scope.categoryWiseExclusionItemsList.push(value);
									}
								});
							}
							
							$scope.removeCategoryExclusionItems = function(index){
								$scope.categoryWiseExclusionItemsList.splice(index,1)
							}
							
							$scope.selectedItemList = [];
							$scope.addItemListServices = function(){
								
								angular.forEach($scope.searchItemList,function(value,key){
									if(value.isSelected){
										
										isItemExists=checkItemIdExistsOrNot($scope.selectedItemList,value)
    									  if (!isItemExists) {
    										  $scope.selectedItemList.push(value);
    									  }
									}
								});
							}
							
							$scope.removeSelectedItemsList = function(item,index){
								$scope.selectedItemList.splice(index,1);
								
								 isPharmacyExists=checkPharmacyIdExistsOrNot( $scope.selectedItemList,item)
								  if (!isPharmacyExists) {
									  
									  for (var i = $scope.cAPAmtProductCategoryWiseList.length -1; i >= 0; i--) {
											 var value = $scope.cAPAmtProductCategoryWiseList[i];
											 if(value.assetCategoryId == item.assetCategoryId && value.productCategoryId == item.productCategoryId){
												 $scope.cAPAmtProductCategoryWiseList.splice(i,1);
											  }else{
												 // $scope.notSelectedServiceList.push(value);
											  }
										}
									  
								  }
							}
							
							  
							//Check Service in inclusion
							  var isServiceExistsForRemoveNotSelected;
								function checkServiceIdExistsForRemoveNotSelected(arr, val) {
									return arr
											.some(function(arr) {
												if(val.serviceId === arr.serviceId)
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
							
							$scope.getInclusionGroupAndSubGroup = function()
							{
								if($scope.groupDetailsList.length > 0){
								$scope.subSpecialityListIN = [];
								$scope.specialityListIN = [];
								angular.forEach($scope.groupDetailsList,function(value,index){
									$scope.subSpecialityListIN.push(value.subDepartmentId);
								});
								angular.forEach($scope.groupDetailsList,function(value,index){
									$scope.specialityListIN.push(value.departmentId);
								});
								
								var data = {
										specialityList : $scope.specialityListIN,
										subSpecialityList : $scope.subSpecialityListIN,
										//tariffId :$scope.companyContarctObj.tariffId,
										organizationId:$scope.orgId,
										unitId : $scope.unitId,
										//effectiveDate : moment(new Date()).format('YYYY-MM-DD'),
										//paymentEntitlementId :$scope.companyContarctObj.paymentEntitlementId,
										//patientTypeId : $scope.configPatientCategoryId,
										visitTypeId :$scope.companyContarctObj.visitTypeId,
										//billingBedCategoryId : $scope.companyContarctObj.billingBedCategoryId
								}
								$rootScope.startLoader();
									var URI = BASE_URL+ ROOT_URL+ "/api/unit/getServiceListBySpecialityIdSubSpecialityList";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
											$scope.searchServiceList = [];
													if (response.data.status == "success") {
														 angular.forEach(response.data.listObject,function(value,index){
															 
															 var coSh = 0;
															   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
																{
																   coSh = 0;
																}
															   else
																   {
																    coSh = $scope.companyContarctObj.coSharePercentage;
																   }
															 var temp1 = {departmentId:value.specialityId, subDepartmentId:value.subSpecialityId,
						          										groupDesc:value.specialityDesc,subGroupDesc:value.subSpecialityDesc,apportionedPercent:0,coShare:coSh};
						                                	 	 
						      									  isGroupExists=checkGroupIdExistsOrNot($scope.groupWiseDetailsList,temp1)
						      									  if (!isGroupExists) {
						      											$scope.groupWiseDetailsList.push(temp1);
						      									  }
						      									  
						      									  
						      									  //New 26-7-2019
						      									  
						      									isServiceExistsForCapAmountListAlready=checkServiceIdExistsForCapAmountListAlready($scope.capServiceWiseDetailsList,value)
																  if (!isServiceExistsForCapAmountListAlready) {
													               //End 26-7-2019
															 
														 isServiceExists=checkServiceIdExistsOrNot($scope.notSelectedServiceList,value)
				     									  if (!isServiceExists) {
				     										// value.finalPrice = value.rate;
				     										// $scope.notSelectedServiceList.push(value);
				     										  
				     										 isServiceExistsForRemoveNotSelected=checkServiceIdExistsForRemoveNotSelected($scope.removeNotSelected,value)
					     									  if (!isServiceExistsForRemoveNotSelected) {
					     										 value.finalPrice = value.rate;
					     										value.serviceRate = value.serviceRate;
					     										value.apportionedPrice = value.serviceRate;
					     										value.taxPercentage = value.taxPercentage;
					     										value.taxId = value.taxId;
					     										 var amt = (value.finalPrice * coSh)/100;
					     										 value.selfPayable = parseFloat(amt).toFixed(2);
					     										 value.creditPayable = parseFloat(value.finalPrice - amt).toFixed(2);
					     										 $scope.notSelectedServiceList.push(value);
					     									  }
														 
				     									  }
														//New 26-7-2019
														 }
														 //End 26-7-2019
														 
														 });
													}
													$rootScope.stopLoader();
												});
								}
								
                                  angular.forEach($scope.selectedServiceList,function(value,index){
                                	  
                                	  var coSh = 0;
									   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
										{
										   coSh = 0;
										}
									   else
										   {
										    coSh = $scope.companyContarctObj.coSharePercentage;
										   }
									   
                                	 	 var temp = {departmentId:value.specialityId, subDepartmentId:value.subSpecialityId,
          										groupDesc:value.specialityDesc,subGroupDesc:value.subSpecialityDesc,apportionedPercent:0,coShare:coSh};
                                	 	 
      									  isGroupExists=checkGroupIdExistsOrNot($scope.groupWiseDetailsList,temp)
      									  if (!isGroupExists) {
      											$scope.groupWiseDetailsList.push(temp);
      									  }
      									  
      									 
      									  //New 26-7-2019
      									  
      									isServiceExistsForCapAmountListAlready=checkServiceIdExistsForCapAmountListAlready($scope.capServiceWiseDetailsList,value)
										  if (!isServiceExistsForCapAmountListAlready) {
							               //End 26-7-2019
											  
      									 isServiceExists=checkServiceIdExistsOrNot($scope.notSelectedServiceList,value)
     									  if (!isServiceExists) {
     										 value.finalPrice = value.rate;
     										value.apportionedPrice = value.serviceRate;
     										value.serviceRate = value.serviceRate;
     										value.taxPercentage = value.taxPercentage;
     										value.taxId = value.taxId;
    										 var amt = (value.finalPrice * coSh)/100;
    										 value.selfPayable = parseFloat(amt).toFixed(2);
    										 value.creditPayable = parseFloat(value.finalPrice - amt).toFixed(2);
    										 
     										
     										 $scope.notSelectedServiceList.push(value);
     									  }
      									 
      									 //New 26-7-2019
                                  			}
      									 //End 26-7-2019
      									  
								});
							}
							
							
							$scope.changefinalPrice = function(fp,idx)
							{
								
								if(fp == '' || fp == undefined || fp == null){
									fp = 0;
								}
								 angular.forEach($scope.groupWiseDetailsList,function(value,index){
     									  if(value.departmentId == $scope.notSelectedServiceList[idx].specialityId && value.subDepartmentId ==  $scope.notSelectedServiceList[idx].subSpecialityId)
     										 {
     										  /*	var amt = (fp * value.coShare)/100;
     										  	$scope.notSelectedServiceList[idx].selfPayable = parseFloat(amt).toFixed(2);
     										  	$scope.notSelectedServiceList[idx].creditPayable = parseFloat(fp - amt).toFixed(2);*/
     										  
     										 var gstAmt =  ((parseFloat(fp) * $scope.notSelectedServiceList[idx].taxPercentage)/100);
 											
 											//apportionedPrice = parseFloat(apportionedPrice) + parseFloat(isNaN(grpDtls.roundOffAmount) ? 0 : grpDtls.roundOffAmount);
 											$scope.notSelectedServiceList[idx].finalPrice =parseFloat(fp) + gstAmt;
 											$scope.notSelectedServiceList[idx].apportionedPrice = $rootScope.parseFloatNum(fp);
 											
 											var amt = ($scope.notSelectedServiceList[idx].finalPrice * value.coShare)/100;
 											$scope.notSelectedServiceList[idx].selfPayable = parseFloat(amt).toFixed(2);
 											$scope.notSelectedServiceList[idx].creditPayable =parseFloat($scope.notSelectedServiceList[idx].finalPrice - amt).toFixed(2);
     										 
     										 
     										 }
								});
								
								
									 
								
								
							}
							$scope.changefinalPricePackage = function(fp,idx)
							{
								if(fp == '' || fp == undefined || fp == null){
									fp = 0;
								}
								
								  var coSh = 0;
								   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
									{
									   coSh = 0;
									}
								   else
									   {
									    coSh = $scope.companyContarctObj.coSharePercentage;
									    }
     								       var gstAmt =  ((parseFloat(fp) * $scope.selectedPackageList[idx].taxPercentage)/100);
 											$scope.selectedPackageList[idx].finalPrice =parseFloat(fp) + gstAmt;
 											$scope.selectedPackageList[idx].apportionedPrice = $rootScope.parseFloatNum(fp);
 											var amt = ($scope.selectedPackageList[idx].finalPrice * coSh)/100;
 											$scope.selectedPackageList[idx].selfPayable = parseFloat(amt).toFixed(2);
 											$scope.selectedPackageList[idx].creditPayable =parseFloat($scope.selectedPackageList[idx].finalPrice - amt).toFixed(2);
								
							}
							
							$scope.changeSelfPayble = function(selfAmt,idx)
							{
								
								if(selfAmt > $scope.notSelectedServiceList[idx].finalPrice){
									growl.warning("Amount should not greater than variance price.",{	
										title : WARNING_MSG
									});
									$scope.notSelectedServiceList[idx].creditPayable = $scope.notSelectedServiceList[idx].finalPrice;
									$scope.notSelectedServiceList[idx].selfPayable = 0;
									return false;
								}else{
									$scope.notSelectedServiceList[idx].creditPayable = $scope.notSelectedServiceList[idx].finalPrice - selfAmt;
								}
								
									
							}
							
							$scope.changeSelfPayblePackage = function(selfAmt,idx)
							{
								
								//$scope.selectedPackageList[idx].creditPayable = $scope.selectedPackageList[idx].finalPrice - selfAmt;
								
								if(selfAmt > $scope.selectedPackageList[idx].finalPrice){
									growl.warning("Amount should not greater than variance price.",{	
										title : WARNING_MSG
									});
									$scope.selectedPackageList[idx].creditPayable = $scope.selectedPackageList[idx].finalPrice;
									$scope.selectedPackageList[idx].selfPayable = 0;
									return false;
								}else{
									$scope.selectedPackageList[idx].creditPayable = $scope.selectedPackageList[idx].finalPrice - selfAmt;
								}
								
								
									
							}
							
							$scope.changeCreditPayble = function(creditAmt,idx)
							{
								if(creditAmt > $scope.notSelectedServiceList[idx].finalPrice){
									growl.warning("Amount should not greater than variance price.",{	
										title : WARNING_MSG
									});
									$scope.notSelectedServiceList[idx].selfPayable = $scope.notSelectedServiceList[idx].finalPrice;
									$scope.notSelectedServiceList[idx].creditPayable = 0;
									return false;
								}else{
									$scope.notSelectedServiceList[idx].selfPayable = $scope.notSelectedServiceList[idx].finalPrice - creditAmt;
								}
								
								
							}
							$scope.changeCreditPayblePackage = function(creditAmt,idx)
							{
								//$scope.selectedPackageList[idx].selfPayable = $scope.selectedPackageList[idx].finalPrice - creditAmt;
								if(creditAmt > $scope.selectedPackageList[idx].finalPrice){
									growl.warning("Amount should not greater than variance price.",{	
										title : WARNING_MSG
									});
									$scope.selectedPackageList[idx].selfPayable = $scope.selectedPackageList[idx].finalPrice;
									$scope.selectedPackageList[idx].creditPayable = 0;
									return false;
								}else{
									$scope.selectedPackageList[idx].selfPayable = $scope.selectedPackageList[idx].finalPrice - creditAmt;
								}
							}
							
							
							 var isGroupExistsCap;
								function checkGroupIdExistsOrNotCap(arr, val) {
									return arr
											.some(function(arr) {
												//return (val.departmentId === arr.departmentId);
												/*if(val.departmentId === arr.departmentId && (arr.subDepartmentId == '' || val.subDepartmentId == ''))
													{
														return true;
													}
												else */if(val.departmentId === arr.departmentId)
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
							
							$scope.getGroupAndSubGroupCapAmount = function()
							{

								
								if($scope.groupWiseDetailsList.length > 0)
									{
										 angular.forEach($scope.groupWiseDetailsList,function(value,index){
											 var temp1 = {departmentId:value.departmentId,
		          										groupDesc:value.groupDesc,subDepartmentCapAmount:0};
											 isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.capGroupWiseDetailsList,temp1)
		      									  if (!isGroupExistsCap) {
		      											$scope.capGroupWiseDetailsList.push(temp1);
		      									  }
											
										 });
									}
								/*angular.forEach($scope.selectedServiceList,function(value,index){
                              	  
                           	 	 var temp = {departmentId:value.specialityId, subDepartmentId:value.subSpecialityId,
     										groupDesc:value.specialityDesc,subGroupDesc:value.subSpecialityDesc,subDepartmentCapAmount:0};
                           	 	 
			                           	  isGroupExists=checkGroupIdExistsOrNot($scope.capGroupWiseDetailsList,temp)
											  if (!isGroupExists) {
													$scope.capGroupWiseDetailsList.push(temp);
											  }
							});*/
								
							}
							
							$scope.getPharmactCapQuantity = function()
							{
								console.log("pharamExcCategoryWiseList",$scope.pharamExcCategoryWiseList);
								
								  var coSh = 0;
								   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
									{
									   coSh = 0;
									}
								   else
									   {
									    coSh = $scope.companyContarctObj.coSharePercentage;
									   }
								   
								$scope.assetCategoryListCap = [];
								$scope.productCategoryCap = [];
								angular.forEach($scope.pharamExcCategoryWiseList,function(value,index){
									$scope.assetCategoryListCap.push(value.assetCategoryId);
									$scope.productCategoryCap.push(value.productCategoryId);
								});
							
								var data = {
										assetCategoryList : $scope.assetCategoryListCap,
										productCategoryList : $scope.productCategoryCap,
										unitId : $scope.unitId,
								}
								if($scope.assetCategoryListCap.length > 0)
									{
								$rootScope.startLoader();
									var URI = BASE_URL+ ROOT_URL+ "/unit/CompanyContract/getProductAssetInclusionList";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													if (response.data.status == "success") {
														 angular.forEach(response.data.listObject,function(value,index){
															 
															 var temp1 = {assetCategoryId:value.assetCategoryId, productCategoryId:value.productCategoryId,
							                             	 			category:value.productCategory,qty:0,amount:0,coShare:coSh};
						                                	 	 
															 isPharmacyExists=checkPharmacyIdExistsOrNot($scope.cAPAmtProductCategoryWiseList,temp1)
						      									  if (!isPharmacyExists) {
						      											$scope.cAPAmtProductCategoryWiseList.push(temp1);
						      									  }
															
														 });
													}
													$rootScope.stopLoader();
												});
								
								  angular.forEach($scope.selectedItemList,function(value,index1){
                                	  
                             	 	 var temp = {assetCategoryId:value.assetCategoryId, productCategoryId:value.productCategoryId,
                             	 			category:value.productCategory,qty:0,amount:0,coShare:coSh};
                             	 	 
                             	 	isPharmacyExists=checkPharmacyIdExistsOrNot($scope.cAPAmtProductCategoryWiseList,temp)
   									  if (!isPharmacyExists) {
   											$scope.cAPAmtProductCategoryWiseList.push(temp);
   											
   										 /*var temp1 = {assetCategoryId:value.assetCategoryId,productCategoryId:value.productCategoryId};
	 										
	 										isPharmacyExists=checkPharmacyIdExistsOrNot($scope.pharamExcCategoryWiseList,temp1)
	 										if (isPharmacyExists) {
	 											$scope.pharamExcCategoryWiseList.splice(index1,1);
	 										}*/
	 										
   											
   									  }
   									
   									  
								});
								
							}
                                 
								
							}
							
							$scope.selectContract = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.contractForImport) {
									$scope.itemSelecteds = item;
								}
							}
							
							$scope.finalImportContract = function()
							{
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one contract...!!',
													{
														title : 'Warning!'
													});
								}
								else
									{
										
										 /* $scope.companyContarctObj = {
												  contractId:$scope.itemSelecteds.contractId,
											      paymentEntitlementId:$scope.itemSelecteds.paymentEntitlementId,
											      associateCompanyId:$scope.itemSelecteds.associateCompanyId,
											      gradeId:$scope.itemSelecteds.gradeId,
											      coSharePercentage:$scope.itemSelecteds.companyShare,
											      tariffId:$scope.itemSelecteds.tariffId,
											      isTariffRateApplicable:$scope.itemSelecteds.isTariffRateApplicable,
											      billingBedCategoryId:$scope.itemSelecteds.billingBedCategoryId,
											      visitTypeId:$scope.itemSelecteds.visitTypeId,
											      patientTypeId:"",
											      companyId:$scope.itemSelecteds.companyId,
											      createdBy:$scope.userId,
											      updatedBy:$scope.userId
											      }
										  
											if($scope.itemSelecteds.paymentEntitlementId==3){
												$scope.isDisable=true;
												$scope.configPatientCategoryId = $scope.companyPatientCategory;
												
											}else{
												$scope.isDisable=false;
												$scope.configPatientCategoryId = $scope.insurancePatientCategory;
											}
											
											$scope.getAssociateCompanyMasterList($scope.itemSelecteds.companyId);
											$scope.getVisitTypeIdbyTariff($scope.itemSelecteds.tariffId);
											$scope.getSelectedValueForBillingBedCategory($scope.itemSelecteds.visitTypeId);
											
											$scope.companyContarctObj.visitTypeId = $scope.itemSelecteds.visitTypeId;
											$scope.companyContarctObj.billingBedCategoryId = $scope.itemSelecteds.billingBedCategoryId;
										  
										
										
										var data = {
								                   orgId : $scope.orgId,
								                   unitId: $scope.unitId,
								                   contractId:$scope.itemSelecteds.contractId
								         }
										var URI = BASE_URL + ROOT_URL+ "/unit/CompanyContract/getCompanyContractDetailByContractId";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
												if (response.data.status == "success") {
													console.log("response",response);
													
													$scope.groupDetailsList = [];
													$scope.groupWiseDetailsList = [];
													$scope.notSelectedServiceList = [];
													$scope.cAPAmtProductCategoryWiseList = [];
													$scope.capGroupWiseDetailsList = [];
													$scope.procedureWiseList = [];
													$scope.pharamExcCategoryWiseList = [];
													$scope.selectedItemList = [];
													angular.forEach(response.data.object.listContractGroupExclusion,function(value,index){
															var temp = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
																		groupDesc:value.groupDesc,subGroupDesc:value.subGroupDesc};
																  $scope.groupDetailsList.push(temp);
						   									
													});
													
													angular.forEach(response.data.object.listContractGroupWiseDetails,function(value,index){
														var temp1 = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
																groupDesc:value.groupDesc,subGroupDesc:value.subGroupDesc,apportionedPercent:value.apportionedPercent,coShare:value.coShare,varianceUpDown:value.varianceUpDown};
											
																$scope.groupWiseDetailsList.push(temp1);
					   									
												    });
													
													angular.forEach(response.data.object.listTContractServiceDetailsDto,function(value,index){
														var temp2 = {finalPrice:value.finalPrice,serviceRate:value.servicePrice,
																apportionedPrice:value.apportionedPrice,taxPercentage:value.taxPercentage,
																selfPayable:value.selfPayable,creditPayable:value.creditPayable,
																serviceId:value.serviceId,finalPrice:value.finalPrice,
																specialityDesc:value.departmentName,
																subSpecialityDesc:value.subDepartmentName,
																serviceDesc:value.serviceName,
																
														};
											           $scope.notSelectedServiceList.push(temp2);
												    });
													
													angular.forEach(response.data.object.contractCapProductList,function(value,index){
														 var temp3 = {productCategoryId:value.productCategoryId, 
																category:value.productCategory, qty:value.quantity,amount:value.amount,coShare:value.prodCatCoShare
																};
											            $scope.cAPAmtProductCategoryWiseList.push(temp3);
												    });
													
													
													
													angular.forEach(response.data.object.listTContractCapDetailsDto,function(value,index){
														var temp4 = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
				          										groupDesc:value.specialityName,subGroupDesc:value.subSpecialityName,subDepartmentCapAmount:value.subDepartmentCapAmount};
											
																$scope.capGroupWiseDetailsList.push(temp4);
					   									
												    });
													$scope.capAmountTotalProcedure = 0;
													angular.forEach(response.data.object.procedureDetailsDtosList,function(value,index){
														var temp5 = {
																serviceId:value.procedureId,
																procedureName:value.procedureName, 
																specialityId:  value.departmentId,
																specialityName:  value.specialityName,
																capAmount :value.capAmount, 
														};
														$scope.capAmountTotalProcedure+=value.capAmount;
																$scope.procedureWiseList.push(temp5);
					   									
												    });
													
													angular.forEach(response.data.object.groupPharmacyExclusionDetailsList,function(value,index){
														var temp6 = {assetCategoryId:value.assetCategoryId,
																productCategoryId:value.productCategoryId, 
																category:value.category, qty:0
																};
														
																$scope.pharamExcCategoryWiseList.push(temp6);
					   									
												    });
													
													angular.forEach(response.data.object.itemList,function(value,index){
														var temp7 = {assetCategoryId:value.assetCategoryId,
																productCategoryId:value.productCategoryId, 
																productCategory:value.category, itemId:value.itemId,
																itemCode:value.itemCode,itemName:value.itemName
																};
														
																$scope.selectedItemList.push(temp7);
					   									
												    });
													
													
													
													$timeout( function(){ $rootScope.stopLoader(); },10000);
												}
											});*/
									
									//$scope.itemSelecteds = $stateParams.companyContractObj;
									//$scope.parentContractId = (($scope.itemSelecteds.oldContractId == null || $scope.itemSelecteds.oldContractId == '') ? $scope.itemSelecteds.contractId : $scope.itemSelecteds.parentContractId);
									$scope.fieldDisabled= false;
									
									  $scope.companyContarctObj = {
											  contractId:$scope.itemSelecteds.contractId,
											  paymentEntitlementId:$scope.itemSelecteds.paymentEntitlementId,
										      associateCompanyId:$scope.itemSelecteds.associateCompanyId,
									          gradeId:$scope.itemSelecteds.gradeId,
										      coSharePercentage:$scope.itemSelecteds.companyShare,
										      tariffId:$scope.itemSelecteds.tariffId,
										      visitTypeId:$scope.itemSelecteds.visitTypeId,
										      patientTypeId:"",
									          companyId:$scope.itemSelecteds.companyId,
										      createdBy:$scope.userId,
										      updatedBy:$scope.userId,
										      contractName : $scope.itemSelecteds.contractName
									      }
								  $scope.isTariffRatesApplicable = ($scope.itemSelecteds.isTariffRateApplicable == 'Y'?true:false);
									$scope.getCompanyMasterList($scope.itemSelecteds.paymentEntitlementId);
									$scope.getAssociateCompanyMasterList($scope.itemSelecteds.companyId);
									$scope.getVisitTypeIdbyTariff($scope.itemSelecteds.tariffId);
									$scope.getSelectedValueForBillingBedCategory($scope.itemSelecteds.visitTypeId);
										
									$scope.companyContarctObj.visitTypeId = $scope.itemSelecteds.visitTypeId;
										
									if($scope.itemSelecteds.contractId!=null && $scope.itemSelecteds.contractId!=undefined){
										var data = {
								                   orgId : $scope.organizationId,
								                   unitId: $scope.unitId,
								                   contractId:$scope.itemSelecteds.contractId
								         }
										var URI = BASE_URL + ROOT_URL+ "/unit/CompanyContract/getCompanyContractDetailByContractId";
									GenericService.serviceAction("POST", URI, data).then(function(response) {
												if (response.data.status == "success") {
													$scope.contractServiceList = [];
													
													$scope.groupDetailsList = [];
												    $scope.groupWiseDetailsList = [];
												    $scope.notSelectedServiceList = [];
													$scope.pharamExcCategoryWiseList = [];
													$scope.capGroupWiseDetailsList = [];
													$scope.cAPAmtProductCategoryWiseList = [];
													$scope.capServiceWiseDetailsList = [];
													$scope.procedureWiseList = [];
													$scope.contractBedCategoryList = [];
													$scope.selectedPackageList = [];
													$scope.selectedItemList = [];
													
													angular.forEach(response.data.object.listContractGroupExclusion,function(value,index){
														var temp = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
															groupDesc:value.groupDesc,subGroupDesc:value.subGroupDesc};
														  $scope.groupDetailsList.push(temp);
														
													});
													
													angular.forEach(response.data.object.listContractGroupWiseDetails,function(value,index){
														var temp1 = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
																groupDesc:value.groupDesc,subGroupDesc:value.subGroupDesc,apportionedPercent:value.apportionedPercent,coShare:value.coShare,varianceUpDown:value.varianceUpDown};
																$scope.groupWiseDetailsList.push(temp1);
													});
													
													angular.forEach(response.data.object.listTContractServiceDetailsDto,function(value,index){
														var temp2 = {finalPrice:value.finalPrice,serviceRate:value.servicePrice,
																apportionedPrice:value.apportionedPrice,taxPercentage:value.taxPercentage,
																selfPayable:value.selfPayable,creditPayable:value.creditPayable,
																serviceId:value.serviceId,
																specialityDesc:value.departmentName,
																subSpecialityDesc:value.subDepartmentName,
																serviceDesc:value.serviceName,
																specialityId:value.departmentId,
	                                                            subSpecialityId:value.subDepartmentId,

																
														};
											           $scope.notSelectedServiceList.push(temp2);
											    });
													
													angular.forEach(response.data.object.groupPharmacyExclusionDetailsList,function(value,index){
													var temp6 = {assetCategoryId:value.assetCategoryId,
																productCategoryId:value.productCategoryId, 
																category:value.category, qty:0
																};
														
																$scope.pharamExcCategoryWiseList.push(temp6);
														});
													/*if(val.productCategoryId == parseInt(arr.productCategoryId) && val.assetCategoryId == parseInt(arr.assetCategoryId))*/
													angular.forEach(response.data.object.listTContractCapDetailsDto,function(value,index){
														var temp4 = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
																groupDesc:value.specialityName,subGroupDesc:value.subSpecialityName,subDepartmentCapAmount:value.departmentCapAmount};
																$scope.capGroupWiseDetailsList.push(temp4);
												    });
													
													angular.forEach(response.data.object.contractCapProductList,function(value,index){
														 var temp3 = {
																 productCategoryId:value.productCategoryId, 
																 assetCategoryId:value.assetCategoryId, 
																category:value.productCategory, 
																qty:value.quantity,
																amount:value.amount,
																coShare:value.prodCatCoShare
																};
											            $scope.cAPAmtProductCategoryWiseList.push(temp3);
												    });
													
													//Need Changes - tillDischarge
													angular.forEach(response.data.object.listContractServiceWiseCapDetails,function(value,index){
														 var temp4 = {departmentId:value.departmentId, 
																     subDepartmentId:value.subDepartmentId,
						     										groupDesc:value.groupDesc,
						     										subGroupDesc:value.subGroupDesc,
						     										serviceCapAmount:value.amount,
						     										applicableDay:value.applicableDays,
						     										qty:value.quantity,
						     										serviceId:value.serviceId,
						     										serviceName:value.serviceName,tillDischarge:(value.tillDischargeFlag == true?'Y':'N')};
											            $scope.capServiceWiseDetailsList.push(temp4);
												    });
													
													
													$scope.capAmountTotalProcedure = 0;
													angular.forEach(response.data.object.procedureDetailsDtosList,function(value,index){
														var temp5 = {
																serviceId:value.procedureId,
																procedureName:value.procedureName, 
																specialityId:  value.departmentId,
																specialityName:  value.specialityName,
																capAmount :value.capAmount, 
														};
														$scope.capAmountTotalProcedure+=value.capAmount;
																$scope.procedureWiseList.push(temp5);
															
												    });
													
													
													angular.forEach(response.data.object.listTContractBedCategoryDetailDto,function(value,index){
														 var temp6 = {bedCategoryId:value.billingBedCategoryId,bedCategoryDesc:value.billingBedCategory};
											            $scope.contractBedCategoryList.push(temp6);
												    });
													
													
													//need change - packageMasterId, packageTypeId
													angular.forEach(response.data.object.listTContractPackageDetailsDto,function(value,index){
														var temp7 = {};
														    temp7.finalPrice = value.finalPrice;
														    temp7.serviceRate = value.servicePrice;
														    temp7.apportionedPrice = value.apportionedPrice;
														    temp7.taxPercentage = (value.taxPercentage != null ? value.taxPercentage : 0);
														    temp7.taxId =value.taxId;
														    temp7.selfPayable = value.selfPayable;
														    temp7.creditPayable = value.creditPayable;
														    temp7.specialityId =  value.departmentId;
														    temp7.specialityDesc = value.departmentName;
														    temp7.subSpecialityId = value.subDepartmentId;
														    temp7.subSpecialityDesc = value.subDepartmentName;
														    temp7.serviceId =  value.serviceId;
														    temp7.serviceDesc = value.serviceName;
														    temp7.packageId =value.packageMasterId ; //need change
														    temp7.packageTypeId =value.packageTypeId; //need change
														    temp7.bedBillingCategoryId = value.billingBedCategoryId; 
														    temp7.billingBedCategoryName = value.billingBedCategoryDesc;
														    temp7.patientTypeId = value.patientCategoryId;
														    temp7.patientTypeDesc = value.patientCategoryDesc;
											                $scope.selectedPackageList.push(temp7);
												    });
													
													angular.forEach(response.data.object.itemList,function(value,index){
														var temp8 = {assetCategoryId:value.assetCategoryId,
																productCategoryId:value.productCategoryId, 
																productCategory:value.category, itemId:value.itemId,
																itemCode:value.itemCode,itemName:value.itemName
																};
																$scope.selectedItemList.push(temp8);
															
												    });
													
												 
												   
													
													$rootScope.stopLoader();
												}
											});
									  }
									
									
										
										
										angular.element('#importFromContractModal').modal('hide');
									}
							}
							
							
							$scope.changeBillingBedCategory = function(billingBedCatId)
							{
								
								  var coSh = 0;
								   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
									{
									   coSh = 0;
									}
								   else
									   {
									    coSh = $scope.companyContarctObj.coSharePercentage;
									   }
								   
								var subSpecialityIds = [];
								
									angular.forEach($scope.groupWiseDetailsList,function(value,index){
										value.apportionedPercent = parseFloat(0);
										value.coShare = coSh;
										value.varianceUpDown = "";
										subSpecialityIds.push(value.subDepartmentId);
									});
									
									   
									
									var data = {
											//specialityIds : $scope.specialityIds,
											subSpecialityList : subSpecialityIds,
											tariffId :$scope.companyContarctObj.tariffId,
											organizationId:$scope.orgId,
											unitId : $scope.unitId,
											effectiveDate : moment(new Date()).format('YYYY-MM-DD'),
											paymentEntitlementId :$scope.companyContarctObj.paymentEntitlementId,
											patientTypeId : $scope.configPatientCategoryId,
											visitTypeId :$scope.companyContarctObj.visitTypeId,
											billingBedCategoryId : billingBedCatId
									}
									if(subSpecialityIds.length > 0)
									{
										$rootScope.startLoader();
											var URI = BASE_URL+ ROOT_URL+ "/api/unit/getServiceListBySubSpecialityId";
										GenericService.serviceAction("POST", URI, data)
												.then(function(response) {
													$scope.searchServiceList = [];
															if (response.data.status == "success") {
																$scope.notSelectedServiceList = [];
																//$scope.bedBillingCategoryList = response.data.listObject;
																
																angular.forEach(response.data.listObject,function(value,index){
																	/*value.finalPrice = value.rate;
																	
																	value.serviceRate = value.serviceRate;
						     										value.taxPercentage = value.taxPercentage;*/
						     										
																	
																	
																	value.finalPrice = value.rate;
						     										value.serviceRate = value.serviceRate;
						     										value.apportionedPrice = value.serviceRate;
						     										value.taxPercentage = value.taxPercentage;
						     										 var amt = (value.finalPrice * coSh)/100;
						     										 value.selfPayable = parseFloat(amt).toFixed(2);
						     										 value.creditPayable = parseFloat(value.finalPrice - amt).toFixed(2);
						     										$scope.notSelectedServiceList.push(value);
																});
															}
															$rootScope.stopLoader(); 
															
															
														});
									}
							}
							
							$scope.firstTabLevel = 1 ;
							
							$scope.assetTypeIdTabWise = 1;
							
							$scope.setFirstTabLevel= function(newTab)
							{
								
								$scope.firstTabLevel = newTab;
								if(newTab == 5)
									{
										$scope.getInclusionGroupAndSubGroup();
									}
								if(newTab == 4)
									{
									  $scope.categoryWise = {};
									  $scope.getGroupAndSubGroupCapAmount();
									  $scope.getPharmactCapQuantity();
									}
								if(newTab == 8)
								{
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.orgId;
									GenericService.serviceAction(METHOD_GET, URI, null)
									.then(function(response){
										$rootScope.stopLoader();
										if(response.status == 200)
											
											$scope.assetCategoryListEquipment = response.data;
									});
								}
								if(newTab == 10)
									{
										if($scope.contractBedCategoryList.length == 0 && ($scope.companyContarctObj.visitTypeId == 2 || $scope.companyContarctObj.visitTypeId == 4))
										{
	 										  growl.error("Please add billing bed category",{	
	 											title : ERROR_MSG
	 										});
	 										 $scope.firstTabLevel = 3;
	 										return;
										}
									
									}
								/*if(newTab == 6)
									{
										$scope.getInclusionGroupAndSubGroup();
									}*/
							};
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							
							
							
							//---------------------calculate Variance Price section -----------------------------
							
							$scope.checkPerValue = function(){
								if($scope.companyContarctObj.coSharePercentage > 100){
									growl.warning("Percentage should be less than 100.",{	
										title : WARNING_MSG
									});
									$scope.companyContarctObj.coSharePercentage = 0;
									return false;
								}
							}
							$scope.calculateVariancePrice = function(val,idx,type){
								
								if(val > 100){
									
									growl.warning("Percentage should be less than 100.",{	
											title : WARNING_MSG
										});
									if(type == 1){
										//groupWiseDetailsList
										if($scope.companyContarctObj.coSharePercentage > 0){
											$scope.groupWiseDetailsList[idx].coShare = $scope.companyContarctObj.coSharePercentage;
										}else{
											$scope.groupWiseDetailsList[idx].coShare = 0;
										}
									}else if(type == 2){
										$scope.groupWiseDetailsList[idx].apportionedPercent = 0;
									}
										return;
								}
								else{
								angular.forEach($scope.groupWiseDetailsList,function(value1,index){
									var idxMain = index;
									if($scope.oldContractId > 0){ 
										angular.forEach($scope.notSelectedServiceList,function(value,index){
											if(value1.departmentId == value.specialityId && value1.subDepartmentId == value.subSpecialityId &&  idxMain == idx)
												{
												var percentAmount = parseFloat(0);
												if(value1.apportionedPercent > 0 ){
													percentAmount = isNaN(value1.apportionedPercent) && isNaN(value.serviceRate) ? parseFloat(value.serviceRate) : (parseFloat(value.serviceRate) / 100) *  parseFloat(value1.apportionedPercent);
												}
												//percentAmount = parseFloat(percentAmount) + (isNaN(value1.roundOffAmount) ? 0 : parseFloat(value1.roundOffAmount));
												var apportionedPrice = value.serviceRate;
												//mark up down
												switch(value1.varianceUpDown){
												case 1 :
													apportionedPrice = parseFloat(percentAmount) + parseFloat(value.serviceRate);
													break;
												case 2 :
													apportionedPrice = parseFloat(value.serviceRate) - parseFloat(percentAmount);
													break;
												
												}
												
												var gstAmt =  ((parseFloat(apportionedPrice) * $scope.notSelectedServiceList[index].taxPercentage)/100);
												
												//apportionedPrice = parseFloat(apportionedPrice) + parseFloat(isNaN(grpDtls.roundOffAmount) ? 0 : grpDtls.roundOffAmount);
												$scope.notSelectedServiceList[index].finalPrice =parseFloat(apportionedPrice) + gstAmt;
												$scope.notSelectedServiceList[index].apportionedPrice = $rootScope.parseFloatNum(apportionedPrice);
												//if(value1.coShare > 0){
												var amt = ($scope.notSelectedServiceList[index].finalPrice * value1.coShare)/100;
	    										 value.selfPayable = parseFloat(amt).toFixed(2);
	    										 value.creditPayable = parseFloat($scope.notSelectedServiceList[index].finalPrice - amt).toFixed(2);
												//}
												
												}
											
										});
								}else{
									angular.forEach($scope.notSelectedServiceList,function(value,index){
										if(value1.departmentId == value.specialityId && value1.subDepartmentId == value.subSpecialityId)
											{
											var percentAmount = parseFloat(0);
											if(value1.apportionedPercent > 0 ){
												percentAmount = isNaN(value1.apportionedPercent) && isNaN(value.serviceRate) ? parseFloat(value.serviceRate) : (parseFloat(value.serviceRate) / 100) *  parseFloat(value1.apportionedPercent);
											}
											//percentAmount = parseFloat(percentAmount) + (isNaN(value1.roundOffAmount) ? 0 : parseFloat(value1.roundOffAmount));
											var apportionedPrice = value.serviceRate;
											//mark up down
											switch(value1.varianceUpDown){
											case 1 :
												apportionedPrice = parseFloat(percentAmount) + parseFloat(value.serviceRate);
												break;
											case 2 :
												apportionedPrice = parseFloat(value.serviceRate) - parseFloat(percentAmount);
												break;
											
											}
											
											var gstAmt =  ((parseFloat(apportionedPrice) * $scope.notSelectedServiceList[index].taxPercentage)/100);
											
											//apportionedPrice = parseFloat(apportionedPrice) + parseFloat(isNaN(grpDtls.roundOffAmount) ? 0 : grpDtls.roundOffAmount);
											$scope.notSelectedServiceList[index].finalPrice =parseFloat(apportionedPrice) + gstAmt;
											$scope.notSelectedServiceList[index].apportionedPrice = $rootScope.parseFloatNum(apportionedPrice);
											//if(value1.coShare > 0){
											var amt = ($scope.notSelectedServiceList[index].finalPrice * value1.coShare)/100;
    										 value.selfPayable = parseFloat(amt).toFixed(2);
    										 value.creditPayable = parseFloat($scope.notSelectedServiceList[index].finalPrice - amt).toFixed(2);
											//}
											
											}
										
									});
								}
								});
								
							}
								
							}
							
							
							
							
							//---------------------SAVE COMPANY CONTARCT DETAILS SECTION -----------------------------
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveCompanyContract();
							}
							
							
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.isTariffRatesApplicable) {
									$scope.isTariffRatesApplicable = true;
									if($scope.companyContarctObj.visitTypeId == 2 || $scope.companyContarctObj.visitTypeId == 4 || $scope.companyContarctObj.visitTypeId == 3)
									{
										$scope.billingBedCatoryFlag = true;
										$scope.contractBedCategoryList = [];
									    $scope.firstTabLevel = 3;
									    //$scope.setFirstTabLevel(3);
										
									}
								} else {
									$scope.isTariffRatesApplicable = false;
									$scope.firstTabLevel = 1;
									//$scope.setFirstTabLevel(1);
								}
							 }
						
							
							//Start for checking 0 cap amount
							  var isCapedGroupExists;
								function checkCapedZeroOrNot(arr) {
									return arr
											.some(function(arr) {
												if(parseInt(arr.subDepartmentCapAmount) == 0 || arr.subDepartmentCapAmount == '')
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
								//End for checking 0 cap amount
								
								//Start for checking 0 phatmacy quantity 
								  var isPharmectQuantityExists;
									function checkPharmectQuantityZeroOrNot(arr) {
										return arr
												.some(function(arr) {
													if((parseInt(arr.qty) == 0 || arr.qty == '') && (arr.amount == 0 || arr.amount == ''))
														{
															return true;
														}
													else
														{
															return false;
														}
												});
									}
									
									//End for checking 0 cap amount
									
									//Start for checking 0 phatmacy quantity 
									  var isProcedureCapedExists;
										function checkProcedureCapedZeroOrNot(arr) {
											return arr
													.some(function(arr) {
														if(parseInt(arr.capAmount) == 0 || arr.capAmount == '')
															{
																return true;
															}
														else
															{
																return false;
															}
													});
										}
										
										//End for checking 0 cap amount
										
										$scope.searchContractForImport = function()
										{
											
											if($scope.searchPaymentEntitlementId == '' || $scope.searchPaymentEntitlementId == undefined)
												{
													growl.warning("Please Select Payment Entitlement.",{	
														title : WARNING_MSG
													});
													return;
												}
											else if($scope.searchCompanyId == '' || $scope.searchCompanyId == undefined)
												{
													growl.error("Please Select Company.",{	
														title : WARNING_MSG
													});
													return;
												
												}
											else if(($scope.searchAssociateCompanyId == '' || $scope.searchAssociateCompanyId == undefined) && $scope.searchPaymentEntitlementId == 2 )
											{
													growl.error("Please Select Insurence.",{	
														title : WARNING_MSG
													});
													return;
											}
											/*else if($scope.searchContractId == '' || $scope.searchContractId == undefined)
											{
													growl.error("Please Select Contract.",{	
														title : WARNING_MSG
													});
													return;
											}*/
											else
												{ 
													 $scope.searchComapnyContarctListImport=[];
													  //var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:"",companyId:"",associateCompanyId:"",tariffId:""};
													  var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:$scope.searchPaymentEntitlementId,companyId:$scope.searchCompanyId,associateCompanyId:null};
													  if($scope.searchPaymentEntitlementId == 2)
														{
															param.associateCompanyId = $scope.searchAssociateCompanyId;
														}
													    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/searchCompanyContract"
													    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
															if (response.data.status == "success"){
																
																angular.forEach(response.data.listObject,function(value,key){
																	if(value.status =='A'){
																		$scope.searchComapnyContarctListImport.push(value);
																	}
																})
//																 $scope.searchComapnyContarctListImport = response.data.listObject;
																/*console.log("searchComapnyContarctList",JSON.stringify($scope.searchComapnyContarctList));*/
															}
														});
												}
										}
								
										
										  var isServiceExistsForCapAmount;
											function checkServiceIdExistsForCapAmount(arr, val) {
												return arr
														.some(function(arr) {
															if(val.serviceId === arr.serviceId)
																{
																	return true;
																}
															else
																{
																	return false;
																}
														});
											}
											
											var isServiceExistsForCapAmountListAlready;
											function checkServiceIdExistsForCapAmountListAlready(arr, val) {
												return arr
														.some(function(arr) {
															if(val.serviceId === arr.serviceId)
																{
																	return true;
																}
															else
																{
																	return false;
																}
														});
											}
											
											
										$scope.searchServicesCapWise = function(searchKeyword){
											$scope.servicesList=[];
											searchKeyword=searchKeyword+"---0";
											var serData = {
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													searchKeyword : searchKeyword
											}
											var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
											//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
											return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
												if(response.data.status == "success"){
													$scope.servicesList = response.data.listObject;
												}
												return $scope.servicesList;
											});
										}
										
										$scope.capServiceWiseDetailsList = [];
										
										$scope.addServiceToTempServiceList = function($item, $model, $label){
											console.log($item);
											
											
											 var temp = {departmentId:$item.specialityId, subDepartmentId:$item.subSpecialityId,
			     										groupDesc:$item.specialityName,subGroupDesc:$item.subSpecialityName,serviceCapAmount:0,applicableDay:1,qty:1,serviceId:$item.serviceMasterId,serviceName:$item.serviceStandardName,serviceCode:$item.serviceStandardCode,taxId:$item.taxId,basePriceForContract:$item.basePriceForContract,taxPercentage:$item.taxPercentage,tillDischarge:'N'};
											 
											   
			                           	 	 
											 isServiceExistsForCapAmount=checkServiceIdExistsForCapAmount($scope.notSelectedServiceList,temp)
														  if (!isServiceExistsForCapAmount) {
															  
															  isServiceExistsForCapAmountListAlready=checkServiceIdExistsForCapAmountListAlready($scope.capServiceWiseDetailsList,temp)
															  if (!isServiceExistsForCapAmountListAlready) {
																  $scope.capServiceWiseDetailsList.push(temp);
												              }
															  else
															  {
																  growl.error("Already Added in list.",{	
																		title : WARNING_MSG
																	});
																	return;
															  }
															  
											              }
														  else
															  {
																  growl.error("This Service already in service wise details, please first delete from there then add.",{	
																		title : WARNING_MSG
																	});
																	return;
															  }
											
											
											
										}
									    
										$scope.searchPackages = function(searchKeyword){
										

											if($scope.companyContarctObj.paymentEntitlementId ==null || $scope.companyContarctObj.paymentEntitlementId == undefined || $scope.companyContarctObj.paymentEntitlementId == '')
												{
													 growl.warning("Please select visit payment entitlement",{	
															title : WARNING_MSG
														});
														return;
												}
											if($scope.companyContarctObj.visitTypeId ==null || $scope.companyContarctObj.visitTypeId == undefined || $scope.companyContarctObj.visitTypeId == '')
												{
													 growl.warning("Please select visit type",{	
															title : WARNING_MSG
														});
														return;
												}
											if($scope.patientTypeId ==null || $scope.patientTypeId == undefined || $scope.patientTypeId == '')
											{
												 growl.warning("Please select patient category",{	
														title : WARNING_MSG
													});
													return;
											}
											
											else
												{
													var packageSearchObj = {};
													packageSearchObj.organisationId = 	$scope.orgId;
													packageSearchObj.unitId = $scope.unitId;
													packageSearchObj.visitTypeId = $scope.companyContarctObj.visitTypeId;//default op
													packageSearchObj.packageTypeId = $scope.packageTypeId;
													packageSearchObj.serviceName = searchKeyword;
													packageSearchObj.paymentEntitlementId =$scope.companyContarctObj.paymentEntitlementId;
													packageSearchObj.patientTypeId = $scope.patientTypeId;
													
													
													if($scope.companyContarctObj.visitTypeId == 1)
														{
														packageSearchObj.bedBillingCategoryId = $scope.opdBillingCatId;
														}
													else
														{
																if($scope.bedBillingCategoryIdPackage == null || $scope.bedBillingCategoryIdPackage=='' || $scope.bedBillingCategoryIdPackage == undefined)
																	{
																		growl.warning("Please select billing bed category",{	
																			title : WARNING_MSG
																		});
																		return false;
																	}
																else
																	{
																	packageSearchObj.bedBillingCategoryId = $scope.bedBillingCategoryIdPackage;
																	}
														}
													return servicesFactory.searchPackageForBilling(packageSearchObj,$scope);
													/*var URI = BASE_URL + ROOT_URL + "/api/packages/bill/services/search";
													return BillingGenericService.serviceAction("POST", URI, packageSearchObj).then(function(response){
														if(response.data.status == "success")
															return response.data.listObject;
													});*/
												}
									}
										
										/*$scope.searchPackages = function(keyword)
										{
											    var packageSearchObj = {};
											
												packageSearchObj.organisationId = $scope.orgId;
												packageSearchObj.unitId = $scope.unitId;
												packageSearchObj.visitTypeId =  $scope.companyContarctObj.visitTypeId;
												packageSearchObj.packageTypeId = $scope.packageTypeId;
												packageSearchObj.serviceName = keyword;
//												packageSearchObj.serviceCode = keyword;
												return servicesFactory.searchPackageForEHC(packageSearchObj,$scope);
										}*/
										/*$scope.selectPackageService = function($item, $model, $label)
										{
											$scope.parentPackageId =  $item.parentPackageId;
										}*/
										
										$scope.selectPackageService = function($item, $model, $label){
											
											var billingBedId = '';
											var billingBedDesc = '';
											var patientTypeIdT = '';
											var patientTypeDesc = '';
											if($scope.companyContarctObj.visitTypeId != 1){
												billingBedId = $.grep($scope.billingBedCategoryPackageList, function (disCat) {
										            return disCat.bedCategoryId == $scope.bedBillingCategoryIdPackage;
										        })[0].bedCategoryId;
												
												billingBedDesc = $.grep($scope.billingBedCategoryPackageList, function (disCat) {
										            return disCat.bedCategoryId == $scope.bedBillingCategoryIdPackage;
										        })[0].bedCategoryDesc;
												
											}else{
												billingBedId = $scope.opdBillingCatId;
											}
											
											if($scope.patientTypeId != null || $scope.patientTypeId != undefined)
												{
												patientTypeIdT = $.grep($scope.patientCategoryList, function (disCat) {
											            return disCat.patientCategoryId == $scope.patientTypeId;
											        })[0].patientCategoryId;
													
												patientTypeDesc = $.grep($scope.patientCategoryList, function (disCat) {
											            return disCat.patientCategoryId == $scope.patientTypeId;
											        })[0].patientCategory;
												}
												
											
											var temp = {packageId:$item.packageMasterId,packageTypeId:$item.packageTypeId,billingBedCategory:billingBedId};
											
													isPackageExists=checkPackageExistsOrNot($scope.selectedPackageList,temp)
													  if (!isPackageExists) {
											
											 var coSh = 0;
											   if($scope.companyContarctObj.coSharePercentage == '' || $scope.companyContarctObj.coSharePercentage == undefined)
												{
												   coSh = 0;
												}
											   else
												   {
												    coSh = $scope.companyContarctObj.coSharePercentage;
												   }
											   
											var value = {};
											var sr = parseFloat($item.basePrice).toFixed(2);
											    var taxAmt = parseFloat((parseFloat($item.basePrice) * $item.taxPercentage )/100).toFixed(2);
											    value.finalPrice = parseFloat(sr) + parseFloat(taxAmt);
	     										value.serviceRate = $item.basePrice;
	     										value.apportionedPrice = $item.basePrice;
	     										if($item.taxPercentage != null){
	     											value.taxPercentage = $item.taxPercentage;
	     										}
	     										else
	     											{
	     											 value.taxPercentage =0;
	     											}
	     										 value.taxId =$item.taxId;
	     										 var amt = (value.finalPrice * coSh)/100;
	     										 value.selfPayable = parseFloat(amt).toFixed(2);
	     										value.creditPayable = parseFloat(value.finalPrice - amt).toFixed(2);
	     										value.specialityId =   $item.specialityId;
	     										value.specialityDesc = $item.specialityName;
	     										value.subSpecialityId = $item.subSpecialityId;
	     										value.subSpecialityDesc = $item.subSpecialityName;
	     										value.serviceId =  $item.serviceMasterId;
	     										value.serviceDesc = $item.serviceStandardName;
	     										value.packageId =$item.packageMasterId ;
	     										value.packageTypeId =$item.packageTypeId;
	     										value.bedBillingCategoryId = billingBedId; 
	     										value.billingBedCategoryName = billingBedDesc;
	     										
	     										value.patientTypeId = patientTypeIdT;
	     										value.patientTypeDesc = patientTypeDesc;
	     										$scope.selectedPackageList.push(value);
	     										
	     										$scope.packageTypeId = '';
												$scope.packageName = '';
												$scope.bedBillingCategoryIdPackage = '';
										 }
													  else
														  {
															  growl.warning("Package already added.",{	
																	title : WARNING_MSG
																});
																return;
														  }
											
										}
										
										/*
										var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
					    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
					    					if(response.data.status == "success"){
					    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
					    						angular.forEach(ehcPackageServiceList,function(val,i){
					    							var newArr = {};
					    			    			newArr.priorityId  = '2';
					    			    			newArr.isOutsourced ='N';
					    			    			newArr.specialityId = val.specialityId;
					    			    			newArr.serviceSpecialityId = val.specialityId;
					    			    			newArr.serviceSubSpecialityId = val.subSpecialityId;
					    			    			newArr.serviceRendered = "0";
					    			    			newArr.serviceMasterId = val.serviceId;
					    			    			newArr.serviceStandardCode = val.serviceCode;
					    			    			newArr.serviceStandardName = val.serviceName;
					    			    			newArr.isDiscountApplicable = "N";
					    			    			newArr.isQuantityEditable = "N";
					    			    			newArr.isRateEditable = "N";
					    			    			newArr.minRateEditable = 0;
					    			    			newArr.maxRateEditable = 0;
					    			    			newArr.specialityName = val.specialityName;
					    			    			newArr.quantity = 1;//val.numberToBeUse;
					    			    			newArr.basePrice = 0;//val.apportionedPrice;
					    			    			newArr.basePriceOld = 0;//val.apportionedPrice;
					    			    			newArr.discountDisabledFlag = 'N';
					    			    			newArr.totalAmt = parseFloat(val.numberToBeUse) * parseFloat(0).toFixed(2);
					    			    			newArr.concession = 0;
					    			    			newArr.discountPercentage = 0;
					    			    			newArr.discount = 0;
					    			    			var dis =0;
					    			    			var disPer =0;
					    			    			newArr.taxPercentage = 0;
					    			    			newArr.discountDetailsList = [];
					    			    			newArr.selfPayable =  "0";
					    			    			newArr.creditPayable =   "0";
					    			    			newArr.oldNetPay = "0";
					    			    			newArr.createdBy  = $scope.userId;
					    			    			newArr.createdDate = new Date().getTime();
					    			    			newArr.packageMasterId = value.packageMasterId;
					    			    			newArr.packageTypeId = value.packageTypeId;
					    			    			newArr.packageId = value.packageMasterId;
					    			    			newArr.isPackageService = 'Y';
					    			    			newArr.apportionedPrice = val.apportionedPrice;
					    			    			
					    			    			newArr.subSpecialityId = val.subSpecialityId; 
					    			    			newArr.ordCancelled = 'N';
					    			    			
					    			    			//ganesh
					    			    			newArr.orderDetailsPayeeMapperDtosList = [];
					    			    			
					    			    			$scope.autoRenderedServicesFinalList.push(newArr);
					    						})
					    					}
					    				});*/
										
							$scope.saveCompanyContract = function(){
								
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.companyContractForm.$valid) {  //validate Check open
									
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										
								  $scope.companyContarctObj.validityFrom =undefined?"":moment($scope.fromDate.getTime()).format('MM-DD-YYYY');
								  $scope.companyContarctObj.validityTo =undefined?"":moment($scope.toDate.getTime()).format('MM-DD-YYYY');
								  $scope.companyContarctObj.status = 'A';
								  console.log('$scope.companyContarctObj',$scope.companyContarctObj);
				             
								if($scope.isTariffRatesApplicable== true){
									
									//$scope.companyContarctObj.tariffId = $scope.defaultSelfTariffId;
									$scope.companyContarctObj.createdBy = $scope.userId;
									$scope.companyContarctObj.updatedBy = $scope.userId;
									$scope.companyContarctObj.isTariffRateApplicable="Y";
									$scope.companyContarctObj.patientTypeId=$scope.configPatientCategoryId;
									var param = {
											orgId:$scope.orgId,
											unitId:$scope.unitId,
											companyContractMasterDto:$scope.companyContarctObj,
											oldContractId : $scope.oldContractId,
											versionNo :$scope.versionNo,
											parentContractId :$scope.parentContractId,
											}
									
									//bed c ategory details
									var listTContractBedCategoryDetailDto = [];
									
									if($scope.contractBedCategoryList.length == 0 && ($scope.companyContarctObj.visitTypeId == 2 || $scope.companyContarctObj.visitTypeId == 4 || $scope.companyContarctObj.visitTypeId == 3))
										{
	 										  growl.error("Please add billing bed category",{	
	 											title : ERROR_MSG
	 										});
	 										 $scope.firstTabLevel = 3;
	 										return;
										}
									else if($scope.contractBedCategoryList.length > 0 && ($scope.companyContarctObj.visitTypeId == 2 || $scope.companyContarctObj.visitTypeId == 4 || $scope.companyContarctObj.visitTypeId == 3)){
									angular.forEach($scope.contractBedCategoryList,function(value,index){
										var temp = {
												billingBedCategoryId : value.bedCategoryId,
												createdBy:$scope.userId
										};
										listTContractBedCategoryDetailDto.push(temp);
									}); 
									}
									param.listTContractBedCategoryDetailDto = listTContractBedCategoryDetailDto;
									// console.log("param isTariffRateApplicable",JSON.stringify(param));
									console.log("param",JSON.stringify(param));
									//return false;
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/createCompanyContarct";
									BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
										if (response.data.status == "success")
											$scope.init();
										$rootScope.stopLoader();
									});
									
								}else{
								
									/*if($scope.capGroupWiseDetailsList.length > 0 && $scope.companyContarctObj.paymentEntitlementId == 3)
										{
											isCapedGroupExists=checkCapedZeroOrNot($scope.capGroupWiseDetailsList)
		 									  if (isCapedGroupExists) {
		 										
		 										  growl.error("Group cap amount should be greater than 0.",{	
		 											title : ERROR_MSG
		 										});
		 										 $scope.firstTabLevel = 4;
		 										return;
		 										
		 									  }
										}*/
									
									if($scope.cAPAmtProductCategoryWiseList.length > 0 && ($scope.companyContarctObj.paymentEntitlementId == 3 || $scope.companyContarctObj.paymentEntitlementId == 2))
									{
										isPharmectQuantityExists=checkPharmectQuantityZeroOrNot($scope.cAPAmtProductCategoryWiseList)
	 									  if (isPharmectQuantityExists) {
	 										
	 										  growl.error("Pharmacy CAP amount should be greater than zero.",{	
	 											title : ERROR_MSG
	 										});
	 										 $scope.firstTabLevel = 4;
	 										return;
	 										
	 									  }
									}
									
									if($scope.procedureWiseList.length > 0)
									{
										isProcedureCapedExists=checkProcedureCapedZeroOrNot($scope.procedureWiseList)
	 									  if (isProcedureCapedExists) {
	 										
	 										  growl.error("Procedure cap amount should be greater than 0.",{	
	 											title : ERROR_MSG
	 										});
	 										 $scope.firstTabLevel = 7;
	 										return;
	 										
	 									  }
									}
									
									
									
									//Start Final Create
									//var param = {};
									
									var param = {
											 orgId:$scope.orgId,
											 unitId:$scope.unitId,
											 companyContractMasterDto:$scope.companyContarctObj,
											 contractGroupPharmacyIncusionDetailsDto:{},
											 listContractGroupExclusion:$scope.groupDetailsList,
											 groupPharmacyExclusionDetailsList:$scope.pharamExcCategoryWiseList,
											 listContractGroupWiseDetails : $scope.groupWiseDetailsList,
											 oldContractId : $scope.oldContractId,
											 versionNo :$scope.versionNo,
											 parentContractId :$scope.parentContractId,
											// $scope.pharamExcCategoryWiseList
											 //$scope.groupWiseDetailsList
											
									};
									//groupDetailsList for exclusion list
									//capGroupWiseDetailsList 
											
									var listTContractServiceDetailsDto = [];
									if($scope.notSelectedServiceList.length > 0){
									angular.forEach($scope.notSelectedServiceList,function(value,index){
										var temp = {
												contractGroupDetailId :value.contractGroupDetailId,
												serviceId:value.serviceId,
												servicePrice : value.serviceRate,
												finalPrice: value.finalPrice,
												selfPayable: value.selfPayable,
												creditPayable: value.creditPayable,
												apportionedPrice: value.apportionedPrice,
												createdBy:$scope.userId,
												isPackage:'N',
												taxPercentage : value.taxPercentage,
												taxId : value.taxId
											};
										listTContractServiceDetailsDto.push(temp);
									});
									}
									
									
									if($scope.selectedPackageList.length > 0){
										angular.forEach($scope.selectedPackageList,function(value,index){
											var temp = {
													contractGroupDetailId :value.contractGroupDetailId,
													serviceId:value.serviceId,
													packageMasterId:value.packageId,
													servicePrice : value.serviceRate,
													finalPrice: value.finalPrice,
													selfPayable: value.selfPayable,
													creditPayable: value.creditPayable,
													apportionedPrice: value.apportionedPrice,
													createdBy:$scope.userId,
													isPackage:'Y',
													billingBedCategoryId: value.bedBillingCategoryId,
													patientCategoryId : value.patientTypeId,
													taxPercentage : value.taxPercentage,
													taxId : value.taxId
												};
											listTContractServiceDetailsDto.push(temp);
										});
										}
									
									
									
									param.listTContractServiceDetailsDto = listTContractServiceDetailsDto;
									
									var procedureListToSave = [];
									if($scope.procedureWiseList.length > 0){
									angular.forEach($scope.procedureWiseList,function(value,index){
										var temp = {
												
												procedureId:value.serviceId,
												capAmount : value.capAmount,
												departmentId : value.specialityId,
												createdBy:$scope.userId,
												taxId:value.taxId,
												taxPercentage:value.taxPercentage,
												
										};
										procedureListToSave.push(temp);
									});
								}
									param.procedureDetailsDtosList = procedureListToSave;
									
									//For Item Inclusion Start
									
									//contract Contract  Pharmacy Exclusion Details 1nd list
									var productCategoryList = [];
									var assetCategoryList = [];
									if($scope.pharamExcCategoryWiseList.length > 0){
									angular.forEach($scope.pharamExcCategoryWiseList,function(value,index){
										productCategoryList.push(parseInt(value.productCategoryId));
										assetCategoryList.push(parseInt(value.assetCategoryId));
									});
								}
									//param.productCategoryList = productCategoryList;
									//param.assetCategoryList = assetCategoryList;
									
									
									var itemList = [];
									//contract Contract  Pharmacy Exclusion Details 2nd list
									if($scope.selectedItemList.length > 0){
									angular.forEach($scope.selectedItemList,function(value,index){
										var data = {};
										data.itemId = value.itemId;
										data.productCategoryId = value.productCategoryId;
										itemList.push(data);
									});
								}
									//param.itemList = itemList;
									
									
									param.contractGroupPharmacyIncusionDetailsDto.productCategoryList = productCategoryList;
									param.contractGroupPharmacyIncusionDetailsDto.assetCategoryList = assetCategoryList;
									param.contractGroupPharmacyIncusionDetailsDto.itemList = itemList;
									//Item Inclusion End
									
									//Cap object
									var listTContractCapDetailsDto = [];
									if($scope.capGroupWiseDetailsList.length > 0){
									angular.forEach($scope.capGroupWiseDetailsList,function(value,index){
										var temp = {
												departmentId : value.departmentId,
												//subDepartmentId:value.subDepartmentId,
												departmentCapAmount:value.subDepartmentCapAmount,
												createdBy:$scope.userId
										};
										listTContractCapDetailsDto.push(temp);
									});
								}
									
									if($scope.cAPAmtProductCategoryWiseList.length > 0){
									angular.forEach($scope.cAPAmtProductCategoryWiseList,function(value,index){
										var temp = {
												
												productCategoryId:value.productCategoryId,
												quantity:value.qty,
												amount:value.amount,
												createdBy:$scope.userId,
												prodCatCoShare:value.coShare
										};
										listTContractCapDetailsDto.push(temp);
									});
									}
									
									
									if($scope.capServiceWiseDetailsList.length > 0){
										angular.forEach($scope.capServiceWiseDetailsList,function(value,index){
											var temp = {
													
													serviceId:value.serviceId,
													//departmentId : value.departmentId,
													//subDepartmentId:value.subDepartmentId,
													applicableDays:value.applicableDay,
													quantity:value.qty,
													amount:value.serviceCapAmount,
													createdBy:$scope.userId,
													tillDischarge:value.tillDischarge
											}; 
											listTContractCapDetailsDto.push(temp);
										});
										}
									
									
									
									param.listTContractCapDetailsDto = listTContractCapDetailsDto;
									
									
									//bed c ategory details
									var listTContractBedCategoryDetailDto = [];
									
									if($scope.contractBedCategoryList.length == 0 && ($scope.companyContarctObj.visitTypeId == 2 || $scope.companyContarctObj.visitTypeId == 4))
										{
	 										  growl.error("Please add billing bed category",{	
	 											title : ERROR_MSG
	 										});
	 										 $scope.firstTabLevel = 3;
	 										return;
										}
									else if($scope.contractBedCategoryList.length > 0 && ($scope.companyContarctObj.visitTypeId == 2 || $scope.companyContarctObj.visitTypeId == 4 || $scope.companyContarctObj.visitTypeId == 3)){
									angular.forEach($scope.contractBedCategoryList,function(value,index){
										var temp = {
												billingBedCategoryId : value.bedCategoryId,
												createdBy:$scope.userId
										};
										listTContractBedCategoryDetailDto.push(temp);
									}); 
								}
									
									
								param.listTContractBedCategoryDetailDto = listTContractBedCategoryDetailDto;
								
								
								
								var equipmentList = [];
									if($scope.pharamEquipmentList.length > 0){
									angular.forEach($scope.pharamEquipmentList,function(value,index){
										var data = {};
										data.itemId = value.equipmentId;
										data.productCategoryId = value.productCategoryId;
										equipmentList.push(data);
									}); 
								}
								
								param.contractGroupPharmacyIncusionDetailsDto.equipmentList = equipmentList;
								
									
								$scope.companyContarctObj.createdBy = $scope.userId;
								$scope.companyContarctObj.updatedBy = $scope.userId;
								$scope.companyContarctObj.isTariffRateApplicable="N";
							
								/*var param = {listTContractGroupPharmacyExclusionDetailsDto:listTContractGroupPharmacyExclusionDetailsDto,listTContractBedCategoryDetailDto:listTContractBedCategoryDetailDto,listTContractCapDetailsDto:listTContractCapDetailsDto,listTContractGroupDetailsDto:listTContractGroupDetailsDto,
										listTContractServiceDetailsDto:listTContractServiceDetailsDto,orgId:$scope.orgId,unitId:$scope.unitId,companyContractMasterDto:$scope.companyContarctObj}*/

								 console.log("param",JSON.stringify(param));
								//return false;
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/createCompanyContarct";
								BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success")
										$scope.init();
									
									$rootScope.stopLoader();
								});
								}
							
									}//pop model close
								}//validate check close
							}
							
							
							$scope.oldContractId = null;
							$scope.versionNo  = null;
							$scope.parentContractId = null;
							 
							//For Update Contract Start
							if (!angular.equals({},$stateParams.companyContractObj)) {
								$scope.getLedgerList();
								$scope.itemSelecteds = $stateParams.companyContractObj;
								$scope.oldContractId  = $scope.itemSelecteds.contractId;
								$scope.versionNo  = $scope.itemSelecteds.versionNo;
								$scope.parentContractId = (($scope.itemSelecteds.oldContractId == null || $scope.itemSelecteds.oldContractId == '') ? $scope.itemSelecteds.contractId : $scope.itemSelecteds.parentContractId);
								$scope.fieldDisabled= true;
								
								  $scope.companyContarctObj = {
										  contractId:$scope.itemSelecteds.contractId,
									      paymentEntitlementId:$scope.itemSelecteds.paymentEntitlementId,
									      associateCompanyId:$scope.itemSelecteds.associateCompanyId,
								          gradeId:$scope.itemSelecteds.gradeId,
									      coSharePercentage:$scope.itemSelecteds.companyShare,
									      tariffId:$scope.itemSelecteds.tariffId,
									      visitTypeId:$scope.itemSelecteds.visitTypeId,
									      patientTypeId:"",
								          companyId:$scope.itemSelecteds.companyId,
									      createdBy:$scope.userId,
									      updatedBy:$scope.userId,
									      contractName : $scope.itemSelecteds.contractName
								      }
							  $scope.isTariffRatesApplicable = ($scope.itemSelecteds.isTariffRateApplicable == 'Y'?true:false);
									/*if($scope.itemSelecteds.paymentEntitlementId==3){
										$scope.isDisable=true;
										$scope.configPatientCategoryId = $scope.companyPatientCategory;
										
									}else{
									$scope.isDisable=false;
										$scope.configPatientCategoryId = $scope.insurancePatientCategory;
								}*/
								$scope.getCompanyMasterList($scope.itemSelecteds.paymentEntitlementId);
								$scope.getAssociateCompanyMasterList($scope.itemSelecteds.companyId);
								$scope.getVisitTypeIdbyTariff($scope.itemSelecteds.tariffId);
								$scope.getSelectedValueForBillingBedCategory($scope.itemSelecteds.visitTypeId);
									
								if($scope.itemSelecteds.contractId!=null && $scope.itemSelecteds.contractId!=undefined){
									var data = {
							                   orgId : $scope.organizationId,
							                   unitId: $scope.unitId,
							                   contractId:$scope.itemSelecteds.contractId
							         }
									var URI = BASE_URL + ROOT_URL+ "/unit/CompanyContract/getCompanyContractDetailByContractId";
								GenericService.serviceAction("POST", URI, data).then(function(response) {
											if (response.data.status == "success") {
												$scope.companyContarctObj.visitTypeId = $scope.itemSelecteds.visitTypeId;
			                                    angular.forEach($scope.ledgerList, function(value , key) {
			                                        if($scope.itemSelecteds.generalLedgerId==value.generalLedgerId)
			                                            {
			                                        	$scope.companyContarctObj.generalLedgerName=value.generalLedgerName;
			                                        	$scope.companyContarctObj.generalLedgerId=value.generalLedgerId;
			                                            }
			                                    });
												$scope.contractServiceList = [];
												
												$scope.groupDetailsList = [];
											    $scope.groupWiseDetailsList = [];
											    $scope.notSelectedServiceList = [];
											    $scope.notSelectedServiceListTemparary = [];
												$scope.pharamExcCategoryWiseList = [];
												$scope.capGroupWiseDetailsList = [];
												$scope.cAPAmtProductCategoryWiseList = [];
												$scope.capServiceWiseDetailsList = [];
												$scope.procedureWiseList = [];
												$scope.contractBedCategoryList = [];
												$scope.selectedPackageList = [];
												$scope.selectedItemList = [];
												
												angular.forEach(response.data.object.listContractGroupExclusion,function(value,index){
													var temp = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
														groupDesc:value.groupDesc,subGroupDesc:value.subGroupDesc};
													  $scope.groupDetailsList.push(temp);
													
												});
												
												angular.forEach(response.data.object.listContractGroupWiseDetails,function(value,index){
													var temp1 = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
															groupDesc:value.groupDesc,subGroupDesc:value.subGroupDesc,apportionedPercent:value.apportionedPercent,coShare:value.coShare,varianceUpDown:value.varianceUpDown};
															$scope.groupWiseDetailsList.push(temp1);
												});
												
												angular.forEach(response.data.object.listTContractServiceDetailsDto,function(value,index){
													var temp2 = {finalPrice:value.finalPrice,serviceRate:value.servicePrice,
															apportionedPrice:value.apportionedPrice,taxPercentage:value.taxPercentage,
															selfPayable:value.selfPayable,creditPayable:value.creditPayable,
															serviceId:value.serviceId,
															specialityDesc:value.departmentName,
															subSpecialityDesc:value.subDepartmentName,
															serviceDesc:value.serviceName,
															specialityId:value.departmentId,
                                                            subSpecialityId:value.subDepartmentId,
                                                            temp : true,

															
													};
										           $scope.notSelectedServiceList.push(temp2);
										    });
												angular.forEach(response.data.object.listTContractServiceDetailsDto,function(value,index){
													var temp2 = {finalPrice:value.finalPrice,serviceRate:value.servicePrice,
															apportionedPrice:value.apportionedPrice,taxPercentage:value.taxPercentage,
															selfPayable:value.selfPayable,creditPayable:value.creditPayable,
															serviceId:value.serviceId,
															specialityDesc:value.departmentName,
															subSpecialityDesc:value.subDepartmentName,
															serviceDesc:value.serviceName,
															specialityId:value.departmentId,
                                                            subSpecialityId:value.subDepartmentId,
                                                            temp : true,

															
													};
										           $scope.notSelectedServiceListTemparary.push(temp2); 
										    });
												angular.forEach(response.data.object.groupPharmacyExclusionDetailsList,function(value,index){
												var temp6 = {assetCategoryId:value.assetCategoryId,
															productCategoryId:value.productCategoryId, 
															category:value.category, qty:0
															};
													
															$scope.pharamExcCategoryWiseList.push(temp6);
													});
												/*if(val.productCategoryId == parseInt(arr.productCategoryId) && val.assetCategoryId == parseInt(arr.assetCategoryId))*/
												angular.forEach(response.data.object.listTContractCapDetailsDto,function(value,index){
													var temp4 = {departmentId:value.departmentId, subDepartmentId:value.subDepartmentId,
															groupDesc:value.specialityName,subGroupDesc:value.subSpecialityName,subDepartmentCapAmount:value.departmentCapAmount};
															$scope.capGroupWiseDetailsList.push(temp4);
											    });
												
												angular.forEach(response.data.object.contractCapProductList,function(value,index){
													 var temp3 = {
															 productCategoryId:value.productCategoryId, 
															 assetCategoryId:value.assetCategoryId, 
															category:value.productCategory, 
															qty:value.quantity,
															amount:value.amount,
															coShare:value.prodCatCoShare
															};
										            $scope.cAPAmtProductCategoryWiseList.push(temp3);
											    });
												
												//Need Changes - tillDischarge
												angular.forEach(response.data.object.listContractServiceWiseCapDetails,function(value,index){
													 var temp4 = {departmentId:value.departmentId, 
															     subDepartmentId:value.subDepartmentId,
					     										groupDesc:value.groupDesc,
					     										subGroupDesc:value.subGroupDesc,
					     										serviceCapAmount:value.amount,
					     										applicableDay:value.applicableDays,
					     										qty:value.quantity,
					     										serviceId:value.serviceId,
					     										serviceName:value.serviceName,tillDischarge:(value.tillDischargeFlag == true?'Y':'N')};
										            $scope.capServiceWiseDetailsList.push(temp4);
											    });
												
												
												$scope.capAmountTotalProcedure = 0;
												angular.forEach(response.data.object.procedureDetailsDtosList,function(value,index){
													var temp5 = {
															serviceId:value.procedureId,
															procedureName:value.procedureName, 
															specialityId:  value.departmentId,
															specialityName:  value.specialityName,
															capAmount :value.capAmount, 
													};
													$scope.capAmountTotalProcedure+=value.capAmount;
															$scope.procedureWiseList.push(temp5);
														
											    });
												
												 
												
												angular.forEach(response.data.object.listTContractBedCategoryDetailDto,function(value,index){
													 var temp6 = {bedCategoryId:value.billingBedCategoryId,bedCategoryDesc:value.billingBedCategory};
										             $scope.contractBedCategoryList.push(temp6);
										             
										             isBillingBedExistsPackage =checkBillingBedExistsOrNotPackage($scope.billingBedCategoryPackageList,temp6)
														if (!isBillingBedExistsPackage) {
															$scope.billingBedCategoryPackageList.push(temp6);
														}
												});
												
												//need change - packageMasterId, packageTypeId
												angular.forEach(response.data.object.listTContractPackageDetailsDto,function(value,index){
													var temp7 = {};
													    temp7.finalPrice = value.finalPrice;
													    temp7.serviceRate = value.servicePrice;
													    temp7.apportionedPrice = value.apportionedPrice;
													    temp7.taxPercentage = (value.taxPercentage != null ? value.taxPercentage : 0);
													    temp7.taxId =value.taxId;
													    temp7.selfPayable = value.selfPayable;
													    temp7.creditPayable = value.creditPayable;
													    temp7.specialityId =  value.departmentId;
													    temp7.specialityDesc = value.departmentName;
													    temp7.subSpecialityId = value.subDepartmentId;
													    temp7.subSpecialityDesc = value.subDepartmentName;
													    temp7.serviceId =  value.serviceId;
													    temp7.serviceDesc = value.serviceName;
													    temp7.packageId =value.packageMasterId ; //need change
													    temp7.packageTypeId =value.packageTypeId; //need change
													    temp7.bedBillingCategoryId = value.billingBedCategoryId; 
													    temp7.billingBedCategoryName = value.billingBedCategoryDesc;
													    temp7.patientTypeId = value.patientCategoryId;
													    temp7.patientTypeDesc = value.patientCategoryDesc;
										                $scope.selectedPackageList.push(temp7);
											    });
												
												angular.forEach(response.data.object.itemList,function(value,index){
													var temp8 = {assetCategoryId:value.assetCategoryId,
															productCategoryId:value.productCategoryId, 
															productCategory:value.category, itemId:value.itemId,
															itemCode:value.itemCode,itemName:value.itemName
															};
															$scope.selectedItemList.push(temp8);
														
											    });
												
											 
											   
												
												$rootScope.stopLoader();
											}
										});
								  }
								
								
							}
							//For Update Contract End
		
	}catch(e){
		console.log(e.message);
	}
}]);	