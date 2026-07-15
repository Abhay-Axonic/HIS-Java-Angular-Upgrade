angular.module("myApp").controller("addNewDoctorReferralShareMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','GenericService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,GenericService ){
	try{
		$rootScope.loginpage = true;
		
		
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId= cookieObject.organizationId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		 $scope.multiselectObj = {
		    		"id":"",
		    		"label":""
		    }
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				$state.go('applicationConfiguration');
				return;
			}
			else
				{
					console.log('cookieObjectAppConfig',cookieObjectAppConfig);
					
				}
			
			$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		    
		 $scope.initSubSpeciality = function(){
			 $scope.subSpeciality = {subSpecialityName:"",subSpecialityId:""};
		}
		$scope.initSpeciality = function(){
			 $scope.speciality = {specialityName:"",specialityId:""};
		}
		 
		
		$scope.dateOptionsEffectiveDate = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		//for New/Registerd
		$scope.openDatePickerSearch = function() {
			$scope.datepickerSearch.opened = true;
		};

		$scope.datepickerSearch = {
			opened : false
		};
	
		$scope.dateOptionsEffectiveDateList = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		
		  $scope.openDatePickerSearchList = function(index) {

              for (var i = $scope.addNewDoctorWiseShareList.length - 1; i >= 0; i--) {
                  if (i === index) {
                 	 $scope.datepickerSearchList.opened[i] = true;
                  } else {
                 	
                 	 $scope.datepickerSearchList.opened[i] = false;
                  }
              }
          };


         $scope.datepickerSearchList = {
             opened : [ false ],

         };
         
         $scope.openDatePickerSearchListser = function(index) {

             for (var i = $scope.addServicesNewDoctorWiseShareList.length - 1; i >= 0; i--) {
                 if (i === index) {
                	 $scope.datepickerSearchListser.opened[i] = true;
                 } else {
                	
                	 $scope.datepickerSearchListser.opened[i] = false;
                 }
             }
         };


        $scope.datepickerSearchListser = {
            opened : [ false ],

        };
        
		$scope.init = function(){
			$scope.selectedServiceList = [];
			//$scope.setOrgUnit($scope.organizationId,$scope.unitId);
			servicesFactory.initServiceSearch($scope);
			 $scope.searchServObj = {
					 isRefDoctorShare:"Y",
						orgId: $scope.orgId,
						unitId: $scope.unitId 
				};
			$scope.bedBillingCategoryList=[];
			$scope.selectedBedBillingCategoryList=[];
			$scope.patientTypeList=[];
			$scope.selectedpatientTypeList=[];
			$scope.specialityList = [];
			$scope.initSpeciality();
			$scope.initSubSpeciality();
			$scope.groupDetailsList = [];
			$scope.addNewDoctorWiseShareList=[];
			$scope.addServicesNewDoctorWiseShareList=[];
			$scope.selectedSpecialityList=[];
			$scope.selectedVisitType = [];
			$scope.selectedDoctorList = [];
			$scope.admissionRequest = {};
			$scope.addServicesNewDoctorWiseShareListServiceRate=[];
			$scope.selectedPaymententitlementList = [];
			
			var data = {
					organizationId : $scope.organizationId,
					unitId : $scope.unitId
				}
			
			// for getting active tariff master
			var URI = BASE_URL + ROOT_URL
					+ GETACTIVETARIFFMASTERLIST;
			GenericService
					.serviceAction("POST", URI, data)
					.then(
							function(response) {
								if (response.data.status == "success") {
									$scope.tariffList = response.data.listObject;
									console.log("ActiveTariffMasterList",$scope.tariffList);
									
								}
							});
			

			var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
			var obj={organizationId : $scope.organizationId};
				BillingGenericService
						.serviceAction(METHOD_POST, URI,
								obj,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										//$scope.specialityList = [];
										//$scope.specialityList = response.data.listObject;
									$scope.specialityListOption = [];
									console.log("response.data.listObject",response.data.listObject);
									angular.forEach(response.data.listObject, function(value, key) {
										var multiselectObj = angular.copy($scope.multiselectObj);
											multiselectObj.id = value.id;
											multiselectObj.label = value.desc;
											$scope.specialityListOption.push(multiselectObj);
										});
									
								});
				
				
				$scope.paymententitlementList = [
		            {
							'id':3,
							'label':'Company'
	                  },
	                  {
							'id':2,
							'label':'Insurance'
	                  },
	                  {
							'id':1,
							'label':'Self'
	                  },
		        ];
				
				
				
						var data = "";
						//var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPatientTypeByTariffId/"+$scope.organizationId+"/"+$scope.unitId+"/"+16;
						var URI = BASE_URL + ROOT_URL + GLOBAL + GET_PATIENT_CATEGORY;  
						var param = {
								"unitId":$scope.unitId,
								"organizationId":$scope.orgId
							}
						console.log("URI", URI);
						BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log("response.data.listObject",response.data.listObject);
									angular.forEach(response.data.listObject, function(value, key) {
										var multiselectObj = angular.copy($scope.multiselectObj);
										multiselectObj.id = value.patientCategoryId;
										multiselectObj.label = value.patientCategory;
											$scope.patientTypeList.push(multiselectObj);
										});
								});
						  
				}
		
		
		$scope.init();
		
		
		/*$scope.getReferralList = function(){
			var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
			var obj={organizationId : $scope.organizationId};
				BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
						$scope.referralTypeList = [];
						if (response.data.status == "success") {
							$scope.referralTypeList = response.data.listObject;
						}
				});
			};
			$scope.getReferralList();*/
			
			/*$scope.getReferralMasterList = function(referralTypeId){
				var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByReferralTypeId";
				var obj=
				      { 
						 organizationId:$scope.organizationId,
						 referralTypeId:referralTypeId
				      };
					BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
							$scope.referralMasterList = [];
							if (response.data.status == "success") {
								$scope.referralMasterList = response.data.listObject;
							}
					});
				};*/
		
		
		
		
		
		
		//service factory methods 
		//$scope.searchServices = function(searchObj){servicesFactory.searchServices(searchObj,$scope)}
		$scope.getSubSpecialityBySpecialityId = function(specialityId){servicesFactory.getSubSpecialityBySpecialityId(specialityId,$scope)}
		$scope.selectAllServices = function(flag){servicesFactory.selectAllServices(flag,$scope)}
		//$scope.setOrgUnit = function(orgId,unitId,scope){	orgId,unitId,$scope)}

		
	   $scope.firstTabLevel = 1 ;
		
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		
		$scope.getSelectedValue = function() 
		{
			
			$scope.visitTypeListOption = [];
			var tariffIdsList = [];
			
			tariffIdsList.push($scope.admissionRequest.tariffId);
			console.log("tariffId",tariffIdsList);
			var data = {
					tariffIdList : tariffIdsList,
				}
			if (tariffIdsList.length > 0) {
				
					var URI = BASE_URL+ ROOT_URL+ "/api/unit/getVisitTypeListByTariffIdList";
					GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
										if (response.data.status == "success") {
											$scope.visitTypeListFinal = [];
											$scope.visitTypeListFinal = response.data.listObject;
											angular.forEach(response.data.listObject,
															function(value,index) 
															{
																var data = {};
																data.id = value.visitTypeId;
																data.label = value.visitTypeName;
																$scope.visitTypeListOption.push(data);
															});
											
											console.log('selectedVisitType list',$scope.visitTypeListOption);
										}
									});
			}
			
		}
		
		$scope.searchServiceList = [];
		$scope.searchServices = function(searchServObj)
		{
			
				var URI = BASE_URL + ROOT_URL + "/api/packages/services/search";
				BillingGenericService.serviceAction(METHOD_POST,URI, searchServObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					
					if (response.data.status == "success"){
					
						//$scope.searchServiceListTemp = response.data.listObject;
						angular.forEach(response.data.listObject,
								function(value,index) {
							 isServiceExistsForRemoveNotSelected=checkServiceIdExistsForRemoveNotSelected($scope.searchServiceList,value)
							  if (!isServiceExistsForRemoveNotSelected) {
								  $scope.searchServiceList.push(value);
							  }
								});
						
					    $scope.searchServObj = {
					    		isRefDoctorShare:"Y",
								orgId: $scope.orgId,
								unitId: $scope.unitId 
						};
					}
				});
			
		}
		
		
		//Check Service in exist
		  var isServiceExistsForRemoveNotSelected;
			function checkServiceIdExistsForRemoveNotSelected(arr, val) {
				return arr
						.some(function(arr) {
							if(val.serviceMasterId === arr.serviceMasterId)
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
			
		$scope.getBillingBedCategory=
		{
				onClose  : function () {
					console.log($scope.selectedVisitType);
					var visitTypeId = [];
					angular.forEach($scope.selectedVisitType,
							function(value,index) 
							{
								visitTypeId.push(value.id);
							});
					
					var data = {
							unitId: $scope.unitId,
							organizationId:$scope.organizationId,
							visitTypeListIds : visitTypeId
					}
					
					$scope.selectedBillingCategory = [];
					var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeIdMultiple";
				GenericService.serviceAction("POST", URI, data)
						.then(function(response) {
									if (response.data.status == "success") {
										$scope.bedBillingCategoryListReference = [];
										$scope.bedBillingCategoryListReference = response.data.listObject;
										$scope.bedBillingCategoryList = [];
										angular.forEach(response.data.listObject,
														function(value,index) {
															var data = {};
															data.id = value.billingBedCategoryId;
															data.label = value.billingBedDesc;
															var found = $scope.bedBillingCategoryList.find(ar=>ar.id==value.billingBedCategoryId);
															   if(found==undefined)
																   {
																      $scope.bedBillingCategoryList.push(data);
																   }
														});
									}
								});
						}
			}
		
		
		//========================= get SpecialityList=====================
	/*	var commonObj = {
			unitId : $scope.unitId,				
			organizationId : $scope.organizationId
		}

		var URI = BASE_URL + ROOT_URL
				+ GETACTIVESPECIALITY;
	
		BillingGenericService
				.serviceAction(METHOD_POST, URI,
						commonObj,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.specialityList = response.data.listObject;
						});*/
		
		//========================= get Doctor By Speciality=====================
		$scope.getDoctorBySpeciality = function(id) {

			var data = {
				specialityId : id,
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
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
							});

		}
		  
	}catch(e){
		console.log(e.message);
	}
	
	
	//Test start
	
	$scope.getDoctorsList=
	{
			onClose  : function () {
				console.log($scope.selectedSpecialityList);
				var speciality = [];
				angular.forEach($scope.selectedSpecialityList,
						function(value,index) 
						{
					      speciality.push(value.id);
						});
				
				 var data = {
			       		  organizationId:$scope.organizationId,
			       		  //unitId:$scope.unitId,
			       		  referalTypeIds:speciality
			         }
				
				 console.log(data);
				 $scope.selectedDoctorList = [];
		         var URI = BASE_URL + ROOT_URL
					+ "/adt/getReferralListByReferralTypeIds";
			BillingGenericService
					.serviceAction(METHOD_POST, URI,
							data,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									{
									$scope.doctorListOption = [];
									angular.forEach(response.data.listObject,function(value,index) {
												var data = {};
												data.id=value.referralId+'_'+value.referralTypeId;
												data.label=value.referralName;
												$scope.doctorListOption.push(data);
											
											});
																							 
									}
									
							});
					}
		}
	//Test End

	 //get speciality list for autocomplete
	  $scope.getSpecialityList = function(){
		  var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
		  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
		    BillingGenericService.serviceAction("POST", URI, param).then(function(response){
		    	if(response.data.status == "success")
		    		$scope.specialityList = response.data.listObject;
		    	 console.log("specialityList",JSON.stringify($scope.specialityList));
		    });
	  }
	  $scope.getSpecialityList();

	  $scope.selectSpeciality = function($item, $model,$label)
	  {
	       $scope.speciality.specialityId = $item.specialityId;
	       if($item.specialityId != ""){
	    	   $scope.initSubSpeciality();
	    	   $scope.subSpecialityList = [];
	    	   var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+$scope.orgId+"/unitId/"+$scope.unitId+"/specialityId/"+$item.specialityId;
	       //var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/"+$item.specialityId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					$scope.subSpecialityList = response.data.listObject;
			});
	       }
	  }
	  
	  $scope.selectSubSpeciality = function($item, $model,$label){
		  $scope.subSpeciality.subSpecialityId = $item.subSpecialityMasterId;
	  }

	  $scope.bedBillingCategoryName = function(id){
			for(var i = 0; i < $scope.bedBillingCategoryList.length ; i++){
				if($scope.bedBillingCategoryList[i].id == id)
					return $scope.bedBillingCategoryList[i].label;
			}
	   }
	  
	  $scope.paymentEntitlementName = function(id)
	  {
		  for(var i = 0; i < $scope.paymententitlementList.length ; i++){
				if($scope.paymententitlementList[i].id == id)
					return $scope.paymententitlementList[i].label;
			}
	  }
		  
	  
	  $scope.patientTypeName = function(id){
			for(var i = 0; i < $scope.patientTypeList.length ; i++){
				if($scope.patientTypeList[i].id == id)
					return $scope.patientTypeList[i].label;
			}
	   }
	  
	  $scope.departmentName = function(id){
			for(var i = 0; i < $scope.specialityListOption.length ; i++){
				if($scope.specialityListOption[i].id == id)
					return $scope.specialityListOption[i].label;
				
			}
	   }
	  
	  $scope.doctorName = function(id){
			for(var i = 0; i < $scope.doctorListOption.length ; i++){
				if($scope.doctorListOption[i].id == id)
					return $scope.doctorListOption[i].label;
			}
	   }
  
	  
	  $scope.checkPerGreater = function(per)
		{
			if(per > 100)
				{
					  growl.warning("Percentage should be less than 100.",{
							title : WARNING_MSG
						});
					  $scope.share = 0;
						return false;
				}
			
		}
		 $scope.checkServicePer = function(serviceShare)
		  	{
			  if(serviceShare > 100){
				  growl.warning("Percentage should be less than 100.",{
						title : WARNING_MSG
					});
				  $scope.serviceShare = 0;
					return false;
			  }else{
				  if($scope.serviceShare > 0)
					{
					   $scope.amount=0;
					} 
			  }
				
		  		
		  	}
	
	
	  $scope.addNewDoctorWiseShareList=[];
	  $scope.previousAddedList=[];
	  $scope.isFirstTimeAdded=false;
	  
	  var isServiceExistsForGroup;
		function checkServiceIdExistsForGroup(arr, val) {
			return arr
					.some(function(arr) {
						if(val.departmentId === arr.departmentId && val.visitTypeId === arr.visitTypeId && val.tariffId === arr.tariffId &&  val.paymentEntitlementId === arr.paymentEntitlementId && val.patientTypeId === arr.patientTypeId && val.bedBillingCategoryId === arr.bedBillingCategoryId  && arr.doctorId === val.doctorId)
							{
								return true;
							}
						else
							{
								return false;
							}
					});
		}
		 $scope.getListBlank = function()
	  	 {
	  		 $scope.searchServiceList = [];
	  		this.selectAllSev = false;
	  	 }
	  	
		
	  $scope.addCAPAmountDetails = function(){
		  
		  
		  if($scope.selectedSpecialityList.length == 0)
		  {
			  growl.warning("Please Select Speciality",{
					title : WARNING_MSG
				});
				return;
		  }
		  else if($scope.selectedDoctorList.length == 0)
		  {
			  growl.warning("Please select referral",{
					title : WARNING_MSG
				});
				return;
		  }
	  else if($scope.admissionRequest.tariffId == '' || $scope.admissionRequest.tariffId == null || $scope.admissionRequest.tariffId == undefined)
		  {
			  growl.warning("Please Select Tariff",{
					title : WARNING_MSG
				});
				return;
		  }
	  else if($scope.selectedVisitType.length == 0)
		  {
			  growl.warning("Please Select VisitType",{
					title : WARNING_MSG
				});
				return;
		  
		  }
	  else if($scope.selectedPaymententitlementList.length == 0)
	  {
		  growl.warning("Please Select paymententitlement",{
				title : WARNING_MSG
			});
			return;
	  
	  }
	  else if($scope.selectedpatientTypeList.length == 0)
		  {
			  growl.warning("Please Select PatientType",{
					title : WARNING_MSG
				});
				return;
		  }
	  else if($scope.selectedBedBillingCategoryList.length == 0)
	  {
		  growl.warning("Please Select Billing Bed Category",{
				title : WARNING_MSG
			});
			return;
	  }
	  else if($scope.speciality.specialityId == '' || typeof($scope.speciality.specialityId) == "undefined"){
			growl.warning("Please select a group",{
				title : WARNING_MSG
			});
			return;
		}
	  else if($scope.share == '' || typeof($scope.share) == "undefined"){
			growl.warning("Please Select Percentage",{
				title : WARNING_MSG
			});
			return;
		}
	  else if($scope.effectiveDate == undefined || $scope.effectiveDate == null || $scope.effectiveDate == ''){
		  growl.warning("Please select effective date.",{
				title : WARNING_MSG
			});
			return;
	  }
	  else{
		  
			var temp = {departmentId:$scope.speciality.specialityId, 
			        groupDesc:$scope.speciality.specialityName,
					share:$scope.share,effectiveDate:$scope.effectiveDate
					};
			
			/*$scope.groupDesc=$scope.speciality.specialityName;
			$scope.subGroupDesc=$scope.subSpeciality.subSpecialityName;
			$scope.departmentId=$scope.speciality.specialityId;
			$scope.subDepartmentId=$scope.subSpeciality.subSpecialityId;*/
			$scope.groupDetailsList.push(temp);
			
			//$scope.addNewDoctorWiseShareList = [];
			arra=cartesian($scope.selectedSpecialityList,$scope.selectedDoctorList,$scope.selectedVisitType,$scope.selectedPaymententitlementList,$scope.selectedpatientTypeList,$scope.selectedBedBillingCategoryList);
			
			for(i=0;i<arra.length;i++){
				
				var tempObj={departmentId:$scope.speciality.specialityId,
						departmentName:$scope.speciality.specialityName,
						doctorId:"",
						doctorName:"",
						patientTypeId:"",
						patientName:"",
						bedBillingCategoryId:"",
						bedBillingCategoryName:"",
						specialityId:"", 
						specialityName:"",
						share:$scope.share,
						effectiveDate:$scope.effectiveDate};
				
				var tempArr=arra[i];
				$scope.specialityId =tempArr[0].id;
				$scope.doctorId =tempArr[1].id;
				$scope.visitTypeId =tempArr[2].id;
				$scope.paymententitlementId =tempArr[3].id;
				$scope.patientTypeId =tempArr[4].id;
				$scope.bedBillingCategoryId =tempArr[5].id;
				if(($scope.visitTypeId == 1 && $scope.bedBillingCategoryId == $scope.opdBillingCatId) || ($scope.visitTypeId != 1 && $scope.opdBillingCatId !=$scope.bedBillingCategoryId))
				{
					
					 var splittedStringArray = $scope.doctorId.split("_");
               	  var doctorId =splittedStringArray[0]; 
               	  var specialityId =splittedStringArray[1];
					
               	if($scope.specialityId == specialityId)  
				{
				var newTempObj = angular.copy(tempObj);
				
				newTempObj.specialityId=$scope.specialityId;
				newTempObj.specialityName=$scope.departmentName($scope.specialityId);
				
				newTempObj.doctorId=doctorId;
				newTempObj.doctorName=$scope.doctorName(doctorId+'_'+specialityId);

				newTempObj.tariffId=$scope.admissionRequest.tariffId;
				newTempObj.tariffName=$scope.tariffName($scope.admissionRequest.tariffId);
				
				newTempObj.visitTypeId=$scope.visitTypeId;
				newTempObj.visitTypeName=$scope.visitTypeName($scope.visitTypeId);
				
				newTempObj.paymentEntitlementId=$scope.paymententitlementId;
				newTempObj.paymentEntitlementName=$scope.paymentEntitlementName($scope.paymententitlementId);
				
				newTempObj.patientTypeId=$scope.patientTypeId;
				newTempObj.patientName=$scope.patientTypeName($scope.patientTypeId);
				
				newTempObj.bedBillingCategoryId=$scope.bedBillingCategoryId;
				newTempObj.bedBillingCategoryName=$scope.bedBillingCategoryName($scope.bedBillingCategoryId);
				
				newTempObj.effectiveDate=$scope.effectiveDate;
				isServiceExistsForGroup=checkServiceIdExistsForGroup($scope.addNewDoctorWiseShareList,newTempObj)
				  if (!isServiceExistsForGroup) {
						$scope.addNewDoctorWiseShareList.push(newTempObj);
				  }
				//$scope.addNewDoctorWiseShareList.push(newTempObj);
				console.log("addNewDoctorWiseShareList",JSON.stringify($scope.addNewDoctorWiseShareList));
				
					}//IF For Speciality Check
				
				}//For IF
	         
				}//For Loop
			
	  }
			
		}
	  $scope.removeNewDoctorWiseShareList = function(index,obj){
			 $scope.addNewDoctorWiseShareList.splice(index,1);
		
		}
	
	  $scope.mainAmtBlank = function(item,idx)
	  	{
			if($scope.serviceShare > 0)
				{
				   $scope.amount=0;
				}
	  		
	  	}
	  	$scope.mainPerBlank = function(item,idx)
	  	{
	  		
	  		
	  		if($scope.amount > 0)
			{
	  			$scope.serviceShare=0;
			}
	  	}
	  	
	  //========================== Filter get Service Name =========================== 
	  
	  $scope.tariffName = function(id){
		  var tariffName = $.grep($scope.tariffList, function (tar) {
  	           return tar.tariffId== id;
  	       })[0].tariffDesc;
		  return tariffName;
		  
	   }
	  
	  $scope.visitTypeName = function(id){
		   var visitTypeName = $.grep($scope.visitTypeListFinal, function (visit) {
 	           return visit.visitTypeId == id;
 	       })[0].visitTypeName;
		  return visitTypeName;
		  
	   }
	  
	  $scope.serviceName = function(id){
			for(var i = 0; i < $scope.selectedServiceList.length ; i++){
				if($scope.selectedServiceList[i].serviceMasterId == id)
				return $scope.selectedServiceList[i].serviceDescription;
			}
	   }
	  
	  $scope.groupName = function(id){
			for(var i = 0; i < $scope.selectedServiceList.length ; i++){
				if($scope.selectedServiceList[i].serviceMasterId == id)
				return $scope.selectedServiceList[i].groupDesc;
			}
	   }
	  
	  $scope.subGroupName = function(id){
			for(var i = 0; i < $scope.selectedServiceList.length ; i++){
				if($scope.selectedServiceList[i].serviceMasterId == id)
				return $scope.selectedServiceList[i].subGroup;
			}
	   }
	  
	  
	  $scope.checkPerGreaterGroup = function(per,idx)
		{
			if(per > 100)
				{
					  growl.warning("Percentage should be less than 100.",{
							title : WARNING_MSG
						});
					  $scope.addNewDoctorWiseShareList[idx].share = $scope.share;
						return false;
				}
			
		}

	//========================== add Selected Services =========================== 
	  $scope.addSelectedServicesDetails = function(){
		  //New START
		 
		  if($scope.selectedSpecialityList.length == 0)
		  {
			  growl.warning("Please Select Speciality",{
					title : WARNING_MSG
				});
				return;
		  }
		  else if($scope.selectedDoctorList.length == 0)
		  {
			  growl.warning("Please select referral.",{
					title : WARNING_MSG
				});
				return;
		  }
	  else if($scope.admissionRequest.tariffId == '' || $scope.admissionRequest.tariffId == null || $scope.admissionRequest.tariffId == undefined)
		  {
			  growl.warning("Please Select Tariff",{
					title : WARNING_MSG
				});
				return;
		  }
	  else if($scope.selectedVisitType.length == 0)
		  {
			  growl.warning("Please Select VisitType",{
					title : WARNING_MSG
				});
				return;
		  
		  }
	  else if($scope.selectedPaymententitlementList.length == 0)
	  {
		  growl.warning("Please Select paymententitlement",{
				title : WARNING_MSG
			});
			return;
	  
	  }
	  else if($scope.selectedpatientTypeList.length == 0)
		  {
			  growl.warning("Please Select PatientType",{
					title : WARNING_MSG
				});
				return;
		  }
	  else if($scope.selectedBedBillingCategoryList.length == 0)
	  {
		  growl.warning("Please Select Billing Bed Category",{
				title : WARNING_MSG
			});
			return;
	  }
	  else if(($scope.serviceShare == '' || typeof($scope.serviceShare) == "undefined") && ($scope.amount == '' || typeof($scope.amount) == "undefined")){
			growl.warning("Please add Percentage OR Amount",{
				title : WARNING_MSG
			});
			return;
		}
	  else if($scope.effectiveDate == undefined || $scope.effectiveDate == null || $scope.effectiveDate == ''){
		  growl.warning("Please select effective date.",{
				title : WARNING_MSG
			});
			return;
	  }
	  else if($scope.selectedServiceList.length < 1 || $scope.selectedServiceList == undefined){
			growl.warning("Please select a service",{
				title : WARNING_MSG
			});
			return;
		}
	  else{
	      $scope.servicesTempPatientTypeList=[];
	     $scope.servicesTempBedBillingCategoryList=[];
		  $scope.servicesTempServiceMasterIdList=[];
		  if($scope.selectedpatientTypeList.length < 1 || $scope.selectedpatientTypeList == undefined){
		  $scope.servicesTempPatientTypeList=[0];
		}
	 
	  if($scope.selectedBedBillingCategoryList.length < 1 || $scope.selectedBedBillingCategoryList == undefined){
		  $scope.servicesTempBedBillingCategoryList=[0];
		}
	  if($scope.selectedServiceList.length < 1 || $scope.selectedServiceList == undefined){
		  $scope.servicesTempServiceMasterIdList=[0];
		}
	 
	  angular.forEach($scope.selectedpatientTypeList, function(value, key)
				{ 
		  			$scope.servicesTempPatientTypeList.push(value.id);
		  		  
				});
	  
	
	  angular.forEach($scope.selectedBedBillingCategoryList, function(value, key)
				{ 
		  			$scope.servicesTempBedBillingCategoryList.push(value.id);
		  		  
				});
	  
	
	  
	        angular.forEach($scope.selectedServiceList, function(value, key)
				{ 
		  			$scope.servicesTempServiceMasterIdList.push(value.serviceMasterId);
		  		  
				});
	  

	  
		var newTemp = {departmentId:$scope.speciality.specialityId,
					groupDesc:$scope.speciality.specialityName,
					serviceShare:$scope.serviceShare,amount:$scope.amount,effectiveDate:$scope.effectiveDate,};
		$scope.groupDesc=$scope.speciality.specialityName;
		$scope.departmentId=$scope.speciality.specialityId;
		$scope.groupDetailsList.push(newTemp);
		
		
		
		/*serviceArra=cartesian($scope.servicesTempPatientTypeList,
				$scope.servicesTempBedBillingCategoryList,$scope.servicesTempServiceMasterIdList);*/
		serviceArra=cartesian($scope.selectedSpecialityList,$scope.selectedDoctorList,$scope.selectedVisitType,$scope.selectedPaymententitlementList,$scope.selectedpatientTypeList,$scope.selectedBedBillingCategoryList,$scope.servicesTempServiceMasterIdList);
		console.log("cartesian :: " + serviceArra.length);
		var tempArray = [];
		for(i=0;i<serviceArra.length;i++){
			
			var serviceTempObj={departmentId:$scope.speciality.specialityId,departmentName:$scope.speciality.specialityName,
					tariffId:"",tariffName:"",
					visitTypeId:"",visitTypeName:"",
					patientTypeId:"",patientName:"",bedBillingCategoryId:"",
					bedBillingCategoryName:"",departmentId:"", subDepartmentId:"",
					groupDesc:"",subGroupDesc:"",amount:$scope.amount,
					serviceShare:$scope.serviceShare,effectiveDate:"",servicesMstId:"",serviceName:""};
			
					var tempArr=serviceArra[i];
					$scope.specialityId =tempArr[0].id;
					$scope.doctorId =tempArr[1].id;
					$scope.visitTypeId =tempArr[2].id;
					$scope.paymententitlementId =tempArr[3].id;
					$scope.patientTypeId =tempArr[4].id;
					$scope.bedBillingCategoryId =tempArr[5].id;
					$scope.servicesMstId =tempArr[6];
					
			if(($scope.visitTypeId == 1 && $scope.bedBillingCategoryId == $scope.opdBillingCatId) || ($scope.visitTypeId != 1 && $scope.opdBillingCatId !=$scope.bedBillingCategoryId))
			{
				 var splittedStringArray = $scope.doctorId.split("_");
              	  var doctorId =splittedStringArray[0]; 
              	  var specialityId =splittedStringArray[1];
					
              	if($scope.specialityId == specialityId)  
				{
					var serviceNewTempObj = angular.copy(serviceTempObj);
					
					serviceNewTempObj.departmentId=$scope.specialityId;
					serviceNewTempObj.departmentName=$scope.departmentName($scope.specialityId);
					
					serviceNewTempObj.doctorId=doctorId;
					serviceNewTempObj.doctorName=$scope.doctorName(doctorId+'_'+specialityId);
					
					serviceNewTempObj.tariffId=$scope.admissionRequest.tariffId;
					serviceNewTempObj.tariffName=$scope.tariffName($scope.admissionRequest.tariffId);
					
					serviceNewTempObj.visitTypeId=$scope.visitTypeId;
					serviceNewTempObj.visitTypeName=$scope.visitTypeName($scope.visitTypeId);
					
					serviceNewTempObj.paymentEntitlementId=$scope.paymententitlementId;
					serviceNewTempObj.paymentEntitlementName=$scope.paymentEntitlementName($scope.paymententitlementId);
					
					serviceNewTempObj.patientTypeId=$scope.patientTypeId;
					serviceNewTempObj.patientName=$scope.patientTypeName($scope.patientTypeId);
					
					serviceNewTempObj.bedBillingCategoryId=$scope.bedBillingCategoryId;
					serviceNewTempObj.bedBillingCategoryName=$scope.bedBillingCategoryName($scope.bedBillingCategoryId);
					
					serviceNewTempObj.groupDesc=$scope.groupName($scope.servicesMstId);
					
					
					serviceNewTempObj.amount=$scope.amount;
					
					serviceNewTempObj.effectiveDate=$scope.effectiveDate;
					
					serviceNewTempObj.servicesMstId=$scope.servicesMstId;
					
					serviceNewTempObj.serviceName=$scope.serviceName($scope.servicesMstId);
					
					serviceNewTempObj.organizationId=$scope.orgId;
					serviceNewTempObj.unitId=$scope.unitId;
					
					//tempArray.push(serviceNewTempObj);
					$scope.addServicesNewDoctorWiseShareListServiceRate.push(serviceNewTempObj);
					/*if((i+1)%10 == 0){
						$scope.addServicesNewDoctorWiseShareListServiceRate = $scope.addServicesNewDoctorWiseShareListServiceRate.concat(tempArray);
						tempArray = [];
					}else if(i == (serviceArra.length - 1)){
						$scope.addServicesNewDoctorWiseShareListServiceRate = $scope.addServicesNewDoctorWiseShareListServiceRate.concat(tempArray);
					}*/
			}//Child IF
		}//For IF
		}//For For Loop
		console.log("$scope.addServicesNewDoctorWiseShareListServiceRate",$scope.addServicesNewDoctorWiseShareListServiceRate);
		console.log("param",JSON.stringify($scope.addServicesNewDoctorWiseShareListServiceRate));
		$rootScope.startLoader();
		var data = {
				globalDocShareServiceRateDtosList:$scope.addServicesNewDoctorWiseShareListServiceRate
		}
		var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getRatesForSelectedService";
		BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			    $rootScope.stopLoader();
				$scope.addServicesNewDoctorWiseShareList = response.data.listObject;
			    $scope.addServicesNewDoctorWiseShareListServiceRate = [];
		});
		}
		  //New END
		  
		}
	  $scope.removeServicesNewDoctorWiseShareList = function(index,obj){
			 $scope.addServicesNewDoctorWiseShareList.splice(index,1);
		
		}
	
	  
	/*  $scope.getPerBlank = function(item,idx)
	  	{
	  		
	  		$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = isNaN(($scope.addServicesNewDoctorWiseShareList[idx].amount/$scope.addServicesNewDoctorWiseShareList[idx].rate)*100) ? 0 : ($scope.addServicesNewDoctorWiseShareList[idx].amount/$scope.addServicesNewDoctorWiseShareList[idx].rate)*100;
	  	}
	  	
	  	$scope.getAmtBlank = function(item,idx)
	  	{
	  		
	  		$scope.addServicesNewDoctorWiseShareList[idx].amount = isNaN(($scope.addServicesNewDoctorWiseShareList[idx].rate * ($scope.addServicesNewDoctorWiseShareList[idx].serviceShare)/100)) ? 0 : ($scope.addServicesNewDoctorWiseShareList[idx].rate * ($scope.addServicesNewDoctorWiseShareList[idx].serviceShare)/100); 
	  	}*/
	  $scope.getAmtBlank = function(item,idx)
	  	{
	  		/*if($scope.addServicesNewDoctorWiseShareList[idx].serviceShare > 0)
			{
	  		  $scope.addServicesNewDoctorWiseShareList[idx].amount=0;
			}*/
	  		if($scope.addServicesNewDoctorWiseShareList[idx].serviceShare > 100)
	  			{
		  			growl.warning("Percantage should be less than 100.",{
						title : WARNING_MSG
					});
		  			if($scope.amount > 0){
		  				
		  				/*if($scope.amount >= $scope.addServicesNewDoctorWiseShareList[idx].rate){
		  					$scope.addServicesNewDoctorWiseShareList[idx].amount = $scope.addServicesNewDoctorWiseShareList[idx].rate;
						}else{
							$scope.addServicesNewDoctorWiseShareList[idx].amount =$scope.amount; 
						}*/
		  				
		  				if($scope.amount >= $scope.addServicesNewDoctorWiseShareList[idx].rate){
		  					$scope.addServicesNewDoctorWiseShareList[idx].amount = $scope.addServicesNewDoctorWiseShareList[idx].rate;
		  					$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = 100;
						}else{
							$scope.addServicesNewDoctorWiseShareList[idx].amount =$scope.amount; 
							$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = (($scope.amount/$scope.addServicesNewDoctorWiseShareList[idx].rate)*100).toFixed(2);
						}
		  				
		  				
		  				//$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = 0;
		  			}
		  			if($scope.serviceShare > 0){
		  			
		  				$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = $scope.serviceShare;
		  				$scope.addServicesNewDoctorWiseShareList[idx].amount =  (($scope.addServicesNewDoctorWiseShareList[idx].rate*$scope.serviceShare)/100).toFixed(2);
		  			}
					return;
					
					
					
	  			
	  			}else{
	  		     $scope.addServicesNewDoctorWiseShareList[idx].amount = isNaN(($scope.addServicesNewDoctorWiseShareList[idx].rate * ($scope.addServicesNewDoctorWiseShareList[idx].serviceShare)/100)) ? 0 : ($scope.addServicesNewDoctorWiseShareList[idx].rate * ($scope.addServicesNewDoctorWiseShareList[idx].serviceShare)/100); 
	  		
	  			}
	  		}
	  
	  $scope.getPerBlank = function(item,idx)
	  	{
	  		/*if($scope.addServicesNewDoctorWiseShareList[idx].amount > 0)
			{
	  		$scope.addServicesNewDoctorWiseShareList[idx].serviceShare=0;
			}*/
	  		if($scope.addServicesNewDoctorWiseShareList[idx].amount > $scope.addServicesNewDoctorWiseShareList[idx].rate){
	  			growl.warning("Service amount should be less than  "+$scope.addServicesNewDoctorWiseShareList[idx].rate,{
					title : WARNING_MSG
				});
	  			
	  			if($scope.amount > 0){
	  					if($scope.amount >=  $scope.addServicesNewDoctorWiseShareList[idx].rate)
	  						{
		  						 $scope.addServicesNewDoctorWiseShareList[idx].amount = $scope.addServicesNewDoctorWiseShareList[idx].rate;
			  				     $scope.addServicesNewDoctorWiseShareList[idx].serviceShare = 100;
	  						}else{
	  							$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = (($scope.amount/$scope.addServicesNewDoctorWiseShareList[idx].rate)*100).toFixed(2);
	  							 $scope.addServicesNewDoctorWiseShareList[idx].amount =$scope.amount;
	  						}
	  					
	  			}
	  			if($scope.serviceShare > 0){
	  				
	  				$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = $scope.serviceShare;
	  				$scope.addServicesNewDoctorWiseShareList[idx].amount =  (($scope.addServicesNewDoctorWiseShareList[idx].rate*$scope.serviceShare)/100).toFixed(2);
	  				 
	  			}
				return;
				
	  			
	  		}else{
	  			$scope.addServicesNewDoctorWiseShareList[idx].serviceShare = isNaN(($scope.addServicesNewDoctorWiseShareList[idx].amount/$scope.addServicesNewDoctorWiseShareList[idx].rate)*100) ? 0 : (($scope.addServicesNewDoctorWiseShareList[idx].amount/$scope.addServicesNewDoctorWiseShareList[idx].rate)*100).toFixed(2);
	  		}
	  		
	  	}
	  
	  
	  //=============================== ADD SERVICES =======================
	  
		/*$scope.addServices = function(){
			$scope.selectedServiceList = [];
			$scope.notSelectedServiceList = [];
				var groupIds = [];
				angular.forEach($scope.groupDetailsList,function(value,index){
					groupIds.push(value.departmentId);
				});
			angular.forEach($scope.searchServiceList,function(value,key){
				if(value.isSelected){
					$scope.selectedServiceList.push(value);
					//console.log("selectedServiceList",JSON.stringify($scope.selectedServiceList));
					
				}else{
					if(groupIds.indexOf(value.specialityId) == -1)
						$scope.notSelectedServiceList.push(value);
					
				}
			});
		}*/
	  
		$scope.addServices = function(){
			$scope.selectedServiceList = [];
			/*$scope.notSelectedServiceList = [];*/
				var groupIds = [];
				angular.forEach($scope.groupDetailsList,function(value,index){
					groupIds.push(value.departmentId);
				});
				
			angular.forEach($scope.searchServiceList,function(value,key){
				if(value.isSelected){
					$scope.selectedServiceList.push(value);
				}
			});
		}
		
		$scope.removeSelectedService = function(index){
			$scope.selectedServiceList.splice(index,1);
		}
	  
	  
	  
		$scope.saveIndividualDocShareGroupWiseDetails = function(){
			if($scope.addNewDoctorWiseShareList.length == 0 && $scope.addServicesNewDoctorWiseShareList.length == 0){
				growl.warning("Please add at least one group or service.",{
					title : WARNING_MSG
				});
				return;
			}else{
			$rootScope.startLoader();
			//Individual Doctor Share Group Wise Details
			console.log("$scope.addNewDoctorWiseShareList",$scope.addNewDoctorWiseShareList);
			console.log("$scope.addServicesNewDoctorWiseShareList",$scope.addServicesNewDoctorWiseShareList);
			var listTIndividualDocShareGroupWiseDto = [];
			angular.forEach($scope.addNewDoctorWiseShareList,function(value,index){
				var temp = {
						specialityId :value.specialityId,
						orgId:value.orgId,
						unitId :value.unitId,
						departmentId: value.departmentId,
						docShare:value.share,
						status:"A",
						effectiveDate:new Date(value.effectiveDate).getTime(),
						doctorId:value.doctorId,
						billingBedCategoryId:value.bedBillingCategoryId,
					/*	visitTypeId:value.visitTypeId,*/
						patientTypeId:value.patientTypeId,
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						tariffId:value.tariffId,
						visitTypeId:value.visitTypeId,
						paymentEntitlementId:value.paymentEntitlementId,
					};
				listTIndividualDocShareGroupWiseDto.push(temp);
			});
			
			
			
			//bed category details
			var listTIndividualDocShareServiceWiseDto = [];
			angular.forEach($scope.addServicesNewDoctorWiseShareList,function(value,index){
				var temp = {
						specialityId : value.departmentId,
						orgId : value.orgId,
						serviceId : value.servicesMstId,
						docShare : value.serviceShare,
						amount : value.amount,
						status : "A",
						effectiveDate :new Date(value.effectiveDate).getTime(),
						doctorId : value.doctorId,
						billingBedCategoryId : value.bedBillingCategoryId,
						visitTypeId : value.visitTypeId,
						patientTypeId : value.patientTypeId,
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						tariffId:value.tariffId,
						visitTypeId:value.visitTypeId,
						paymentEntitlementId:value.paymentEntitlementId,
						
				};
				listTIndividualDocShareServiceWiseDto.push(temp);
			});
		
			var param = {listTIndividualDocShareGroupWiseDto:listTIndividualDocShareGroupWiseDto,listTIndividualDocShareServiceWiseDto:listTIndividualDocShareServiceWiseDto
					,orgId:$scope.organizationId,unitId:$scope.unitId}

			// console.log("param",JSON.stringify(param));
			
			 //console.log("listTIndividualDocShareGroupWiseDto",JSON.stringify(listTIndividualDocShareGroupWiseDto));
			// console.log("listTIndividualDocShareServiceWiseDto",JSON.stringify(listTIndividualDocShareServiceWiseDto));
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/saveReferralShare";
			BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
					{
					$scope.init();
				    $scope.serviceShare = 0;
				    $scope.amount = 0;
				    $scope.share = 0;
					$scope.effectiveDate = '';
				$rootScope.stopLoader();
					}else{
						$rootScope.stopLoader();
					}
			});
		}
		}
	  
	  
	  
	  function cartesian() {
		    var r = [], arg = arguments, max = arg.length-1;
		    function helper(arr, i) {
		        for (var j=0, l=arg[i].length; j<l; j++) {
		            var a = arr.slice(0); // clone arr
		            a.push(arg[i][j]);
		            if (i==max)
		                r.push(a);
		            else
		                helper(a, i+1);
		        }
		    }
		    helper([], 0);
		    return r;
		}
	
	
}]);