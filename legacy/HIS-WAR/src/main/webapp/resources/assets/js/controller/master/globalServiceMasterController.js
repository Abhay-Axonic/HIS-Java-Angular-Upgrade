(function () {
    "use strict";
    function globalServiceMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, growl,GenericServiceParamHeader,GenericServiceFileUpload,BillingGenericService,fileReader,$timeout,$state) {
        var self = this;

		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
    	$scope.storeName = $scope.cookieObjectStore.storeName;
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.storeId = cookieObject.storeId;
		
		var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
		
		if (cookieObjectAppConfigSecond == undefined) {
			$state.go('login');
			return;
		} 
		else {
			console.log('cookieObjectAppConfigSecond',
					cookieObjectAppConfigSecond);
		}
		
         //Default Constructor
        function initializeController() {

			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('applicationConfiguration');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.fromUnitId = 0;
			$scope.searchServiceName = '';
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				 $state.go('applicationConfiguration');
			  return;
			 }
			 else
			  {
				 if((cookieObjectAppConfig.defaultSelfTariffId == '' || cookieObjectAppConfig.defaultSelfTariffId == null) && (cookieObjectAppConfig.companyPatientCategory == '' || cookieObjectAppConfig.companyPatientCategory == null))
					 {
					  $state.go('applicationConfiguration');
					  return;
					 }
			   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			   
			  }
			$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
			$scope.companyPatientCategory = cookieObjectAppConfig.companyPatientCategory;
			$scope.insurancePatientCategory = cookieObjectAppConfig.insurancePatientCategory;
			

			$scope.msg="No records found...!!";
			$scope.selectedTariffList=[];	
			$scope.selectedPaymentEnt=[];	
			$scope.selectedVisitType=[];
			$scope.selectedBillingCategory=[];
			$scope.selectedPatientTypeList=[];
			$scope.selectedSpeacilityList = [];
			$scope.selectedSubSpecialityList = [];
			$scope.selectedServiceList = [];
			$scope.duplicateList = [];
			
			$scope.tariffMuliSelectList =[];
			$scope.selectedTariffList=[];	
			$scope.selectedPaymentEnt=[];	
			$scope.selectedVisitType=[];
			$scope.selectedBillingCategory=[];
			$scope.selectedPatientTypeList=[];
			$scope.selectedSpeacilityList = [];
			$scope.selectedSubSpecialityList = [];
			$scope.selectedServiceList = [];
			$scope.allDetailsList=[];
			//$scope.paymentEntitlementMultiselectList = [];
			$scope.tariffId=null;
			$rootScope.loginpage = true;
			$scope.country = {}
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.disableSaveBtn = true;
			$scope.enableApplyBtn = true;
			$scope.enableDatePicker = true;
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup

			$scope.effectiveDate = '';
			$scope.selectedTariffList=[];
			$scope.selectedPaymentEntitlementList=[];
			$scope.paymentEntitlementId=null;
			
			
			$scope.clearData = function(){
				$scope.msg="No records found...!!";
				$scope.selectedTariffList=[];	
				$scope.selectedPaymentEnt=[];	
				$scope.selectedVisitType=[];
				$scope.selectedBillingCategory=[];
				$scope.selectedPatientTypeList=[];
				$scope.selectedSpeacilityList = [];
				$scope.selectedSubSpecialityList = [];
				$scope.selectedServiceList = [];
				$scope.duplicateList = [];
				$scope.selectedSpeacility ="";
				$scope.selectedSubSpeciality="";
				$scope.tariffMuliSelectList =[];
				$scope.selectedTariffList=[];	
				$scope.selectedPaymentEnt=[];	
				$scope.selectedVisitType=[];
				$scope.selectedBillingCategory=[];
				$scope.selectedPatientTypeList=[];
				$scope.selectedSpeacilityList = [];
				$scope.selectedSubSpecialityList = [];
				$scope.selectedServiceList = [];
				$scope.allDetailsList=[];
				$scope.selectedVisitType=[];
				$scope.selectedServiceListnew =[];
				$scope.tariffId=null;
				$rootScope.loginpage = true;
				$scope.country = {}
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.disableSaveBtn = true;
				$scope.enableApplyBtn = true;
				$scope.enableDatePicker = true;
				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup
				$scope.billingCategory = [];
				

				$scope.effectiveDate = '';
				$scope.selectedTariffList=[];
				$scope.selectedPaymentEntitlementList=[];
				$scope.paymentEntitlementId=null;
			}
			var paymentEnDesc = "Self";
			$scope.paymentEntitlementTypeList =[];
			var paymentEntData = {};
			paymentEntData.id = $scope.paymentEntitlementId;
			paymentEntData.lable = paymentEnDesc;
			$scope.paymentEntitlementTypeList.push(paymentEntData);
			
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
									
									angular.forEach(response.data.listObject,
											function(value,index) {
												var data = {};
												data.id = value.tariffId;
												data.label = value.tariffDesc;
												$scope.tariffMuliSelectList.push(data);
									});
									
								}
							});	
				
		}
  
        $scope.searchServices = function(searchKeyword){
			$scope.servicesList=[];
			$scope.searchKeyword=searchKeyword;
					var serData = {
							searchKeyword : searchKeyword,
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/searchGlablServiceMaster";
					return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.servicesList = response.data.listObject;
							return $scope.servicesList;
						}
						
					});
		}
        $scope.subSpecialityActive =function(){
		var specialityType = [];
		angular.forEach($scope.masterSpeacilityList,
				function(value, index) {
					specialityType.push(value.id);
				});
		var data = {
			organizationId : $scope.organizationId,
			unitId : $scope.unitId,
			specialityIdList : specialityType
		}
		
		
		var URI = BASE_URL+ ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYARRAYFORTARIFF;
		BillingGenericService
				.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							console.log(response);
							if (response.data.status == "success") {
								$scope.masterSubSpecialityList = [];
								
								angular.forEach(response.data.listObject,
												function(value,index) {
													var data = {};
													data.subSpecialityId = value.subSpecialityId;
													data.subSpecialityName = value.subSpecialityName;
													$scope.masterSubSpecialityList.push(data);
												});
								
							
							}
							console.log("subspeciality",$scope.subSpecialityList);
							
						});
        }


        $scope.getMasterData =function(){

                     var serData = {
           							searchKeyword : $scope.searchServiceName,
           							organizationId : $scope.organizationId,
           							golbalSpeciality:$scope.selectedSpeacility,
           							golbalSubSpeciality:$scope.selectedSubSpeciality,
           					}
           					var URI = BASE_URL + ROOT_URL + "/api/global/getAllGlablServiceMaster";
           					 BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
           						if(response.data.status == "success"){
           						    $scope.getMasterDataList =[];
           							$scope.getMasterDataList = response.data.listObject;
           							$scope.serviceNameString =response.data.object;
                                     console.log("final service List ",$scope.getMasterDataList);
                                     console.log("service Name String ",$scope.serviceNameString);
           						 }
           		      		  	});
        }
        $scope.getMasterData();


        $scope.activeSpeciality=function(){
			var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
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
									$scope.masterSpeacilityList = [];
									angular.forEach(response.data.listObject,
													function(value,index) {
														var data = {};
														data.id = value.specialityId;
														data.label = value.specialityName;
														$scope.masterSpeacilityList.push(data);
													});
									 $scope.subSpecialityActive();
								}
								console.log("masterSpeacilityList",$scope.masterSpeacilityList);
							});	
		}
		$scope.activeSpeciality();
		
        
        $scope.glablServiceMasterSpecialityServices = function(searchKeyword){
			$scope.glablServiceMasterSpecialityList=[];
			var data = {}
			var URI = BASE_URL + ROOT_URL + "/api/global/glablServiceMasterSpecialityName";
			 BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.glablServiceMasterSpecialityList = response.data.listObject;
					$scope.speacilityList =[];
					angular.forEach($scope.glablServiceMasterSpecialityList,function(v,k){
						var newArr ={};
						newArr.id = v.specialityName;
						newArr.label = v.specialityName;
						$scope.speacilityList.push(newArr);
					});
				}
			});
		}
        $scope.glablServiceMasterSpecialityServices();
        
        $scope.getSelectedSubSpeciality = function(searchKeyword){
			$scope.selectedSubSpecialityList =[];
			var subSpecialityObj ={
					id:$scope.selectedSubSpeciality
			}
			$scope.selectedSubSpecialityList.push(subSpecialityObj);
        }
			
        $scope.glablServiceMasterSubSpecialityServices = function(searchKeyword){
        	
        	 if($scope.tariffId == undefined || $scope.tariffId == null || $scope.tariffId == '' || $scope.tariffId == '-'){
          	   growl.warning("Please Select Tariff",{
  					title : ERROR_MSG
  				});
               $scope.selectedSpeacilityList=[];
          	   return false;
             }
        	 
			$scope.glablServiceMasterSubSpecialityList=[];
			$scope.selectedSpeacilityList =[];
			var specialityObj ={
					id:$scope.selectedSpeacility
			}
			$scope.selectedSpeacilityList.push(specialityObj
					);
			if($scope.selectedSpeacilityList.length == 0){
				$scope.selectedSubSpecialityList=[];
				$scope.subSpecialityList=[];
			}
			 $scope.specialityString ='';
			angular.forEach($scope.selectedSpeacilityList ,function(v,k){
				 if(k == 0){
            	   var String ="'"+ v.id+"'";
            	   $scope.specialityString= $scope.specialityString.concat(String);
            	 }
            	 else{
            	      var String = "'"+ v.id+"'";
            	      $scope.specialityString= $scope.specialityString.concat(","+String);
            	 }
			});
			if($scope.specialityString != ''){
				
			var data = {
					golbalSpeciality:$scope.selectedSpeacility
			}
			var URI = BASE_URL + ROOT_URL + "/api/global/glablServiceMasterSubSpecialityName";
			 BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.glablServiceMasterSubSpecialityList = response.data.listObject;
					$scope.selectedSubSpecialityList=[];
					$scope.subSpecialityList =[];
					angular.forEach($scope.glablServiceMasterSubSpecialityList,function(v,k){
						var newArr ={};
						newArr.id = v.subSpecialityName;
						newArr.label = v.subSpecialityName;
						$scope.subSpecialityList.push(newArr);
					});
				}
			});
			}
		}
        
        
    	$scope.getBillingBedCategoryAddNew = function(visitId)
		{
				var URI = BASE_URL+ ROOT_URL+ "/adt/getBillingBedCategoryByVistTypeId/"+visitId+"/"+$scope.organizationId+"/"+$scope.unitId;
			GenericService.serviceAction("GET", URI, "")
					.then(function(response) {
								if (response.data.status == "success") {
									angular.forEach(response.data.listObject,
													function(value,index) {
														var data = {};
														data.id = value.billingBedCategoryId;
														data.label = value.billingBedDesc;
														data.rate = 0;
														var found = $scope.selectedBillingCategory.find(ar=>ar.id == value.billingBedCategoryId );
														if(found == undefined){
															$scope.selectedBillingCategory.push(data);
														}
														var found1 = $scope.billingCategory.find(ar=>ar.id == value.billingBedCategoryId );
														if(found1 == undefined){
															$scope.billingCategory.push(data);
														}
													});
									
									
									console.log('selectedBillingCategory list',$scope.selectedBillingCategory);
								}
							});
			if($scope.selectedVisitType.length == 0){
				$scope.billingCategory = [];
				$scope.selectedBillingCategory = [];
			}
		
		}
    	   $scope.addServiceToTempServiceList = function($item, $model, $label)
		    {
    		  
		    }  
    	   
    	   $scope.selectedServiceListnew =[];
    	   $scope.applyData = function()
		    {

   			$scope.servicesList=[];
   	       $scope.headersList =[];  
           $scope.selectedServiceListUpload =[];
           $scope.propertyListWithoutSpacesNew =[];
           if($scope.tariffId == undefined || $scope.tariffId == null || $scope.tariffId == '' || $scope.tariffId == '-'){
        	   growl.warning("Please Select Tariff",{
					title : ERROR_MSG
				});
        	   return false;
           }
           
           if($scope.selectedSpeacilityList.length ==0){
        	   growl.warning("Please Select Department",{
					title : ERROR_MSG
				});
        	   return false;
           }
           
           if($scope.selectedSubSpecialityList.length ==0){
        	   growl.warning("Please Select Sub Department",{
					title : ERROR_MSG
				});
       	      return false;
           }
   			 $scope.specialityString ='';
   				angular.forEach($scope.selectedSpeacilityList ,function(v,k){
   					 if(k == 0){
   	            	   var String ="'"+ v.id+"'";
   	            	   $scope.specialityString= $scope.specialityString.concat(String);
   	            	 }
   	            	 else{
   	            	      var String = "'"+ v.id+"'";
   	            	      $scope.specialityString= $scope.specialityString.concat(","+String);
   	            	 }
   				});
   				$scope.subSpecialityString ='';
   				angular.forEach($scope.selectedSubSpecialityList ,function(v,k){
   					 if(k == 0){
   	            	   var String ="'"+ v.id+"'";
   	            	   $scope.subSpecialityString= $scope.subSpecialityString.concat(String);
   	            	 }
   	            	 else{
   	            	      var String = "'"+ v.id+"'";
   	            	      $scope.subSpecialityString= $scope.subSpecialityString.concat(","+String);
   	            	 }
   				});
   				if($scope.specialityString != ''){
   					var serData = {
   							searchKeyword : $scope.searchServiceName,
   							golbalSpeciality:$scope.selectedSpeacility,
   							golbalSubSpeciality:$scope.selectedSubSpeciality,
   					}
   					var URI = BASE_URL + ROOT_URL + "/api/global/glablServiceMaster";
   					 BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
   						if(response.data.status == "success"){
   							$scope.servicesList = response.data.listObject;
   						 $scope.selectedServiceList =[];
   		      		    if(response.data.listObject == null || response.data.listObject.length == 0){
   		      		        growl.warning("No Service Found ",
  		     					{
  		     					title : 'warning!'
  		     					});
   		      		        return false;
   		      		     }
   		      		   
   		               angular.forEach($scope.servicesList,function(value,key){
   		            	   var newdd ={};
	   		             var matchtariffdesc = $scope.tariffList.find(function(masterTarrif) {
							    return masterTarrif.tariffId.toString() === $scope.tariffId;
							  });
	   		              if (matchtariffdesc) {
							var  tariffDesc = matchtariffdesc.tariffDesc;
						  }
					  
		   		           var checkDuplication = $scope.selectedServiceListnew.find(function(masterTarrif) {
							    return masterTarrif.tariffDesc === tariffDesc
							      &&  masterTarrif.serviceStandardCode == value.serviceStandardCode
							      &&  masterTarrif.serviceStandardName == value.serviceStandardName
							      &&  masterTarrif.specialityName == value.specialityName
							      &&  masterTarrif.subSpecialityName == value.subSpecialityName;
							  }); 
		   		           
   		         		   newdd = value;
   		         		   newdd.tariffDesc = tariffDesc;
   		         		   newdd.billingCategory =$scope.billingCategory;
   		         		   newdd.selected =true;
   		         	      if(checkDuplication == undefined){
   		         	 	   $scope.selectedServiceList.push(newdd);
   		         	     }else{
   		         	    	growl.warning("Service is already exist",
   		     					{
   		     					title : 'warning!'
   		     					});
   		         	     }
   		               });
   		             
   		        	       $scope.selectedServiceListnew = $scope.selectedServiceListnew.concat($scope.selectedServiceList);
   		                
   						}
   						
   					});
   				}
   			  
		    }
        $scope.getSelectedValue = function() 
		{
        	
			console.log("tariffList",$scope.selectedTariffList);
				
				var newu ={};
				newu.id=$scope.tariffId;
				$scope.selectedTariffList.push(newu);
			
			$scope.selectedVisitType = [];
			$scope.selectedPaymentEnt = [];
			$scope.selectedPatientTypeListTest = [];
			var tariffIdsList = [];
			
			tariffIdsList.push($scope.tariffId);
			console.log("tariffId",tariffIdsList);
			var data = {
					tariffIdList : tariffIdsList,
				}
			if (tariffIdsList.length > 0) {
				var URI = BASE_URL
						+ ROOT_URL
						+ GETPAYMENTENTITLEMENTLISTBYTARIFFIDLIST;
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.paymentEntitlementList = response.data.listObject;
										console.log('paymentEntitlementModeldata list',$scope.paymentEntitlementList);
									}
								});
				
					var URI = BASE_URL+ ROOT_URL+ "/api/unit/getVisitTypeListByTariffIdList";
					GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
										if (response.data.status == "success") {
											angular.forEach(response.data.listObject,
															function(value,index) 
															{
																var data = {};
																data.id = value.visitTypeId;
																data.label = value.visitTypeName;
																$scope.selectedVisitType.push(data);
															});
											
											$scope.selectedBillingCategory = [];
											$scope.billingCategoryAdd = [];
											$scope.billingCategory = [];
											angular.forEach($scope.selectedVisitType, function(v,k){
												$scope.getBillingBedCategoryAddNew(v.id);
											})
											
											console.log('selectedVisitType list',$scope.selectedVisitType);
										}
									});
					
					
					var URI = BASE_URL+ ROOT_URL+ "/api/unit/getPatientTypeListByTariffIdList";
					GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
										if (response.data.status == "success") {
											$scope.selectedPatientTypeList = [];
											angular.forEach(response.data.listObject,
															function(value,index) {
																var data = {};
																data.id = value.patientTypeId;
																data.label = value.patientCategory;
																$scope.selectedPatientTypeListTest.push(data);
															});
											console.log('selectedPatientType list',$scope.selectedPatientTypeListTest);
										}
									});
					
					
			}
			
		}
        
        
    	$scope.activePatientType=function(){
			var data = {
					organizationId : $scope.organizationId,
					unitId : $scope.unitId
				}
			var URI = BASE_URL + ROOT_URL
				+ "/global/patientcategory/"+$scope.organizationId;
			GenericService
					.serviceAction("GET", URI, data)
					.then(function(response) {
								$scope.patientTypeListMain = [];
								if (response.data.status == "success") {
									angular.forEach(response.data.listObject,
													function(value,index) {
														var data = {};
														data.id = value.id;
														data.label = value.name;
														$scope.patientTypeListMain.push(data);
													});
									console.log('patientTypeList',$scope.patientTypeListMain);
								}
							});
		}
		$scope.activePatientType();
		
        $scope.exportToExcel = function () {
        	
        	
    		growl.warning("Please do not edit service name, service code, speciality, subspeciality and billing bed category  or add extra data other than downloaded data excel !!",
					{
					title : 'warning!'
					});
    		
    		
    		if($scope.selectedServiceListnew == undefined || $scope.selectedServiceListnew == null || $scope.selectedServiceListnew.length ==0){
    			return false;
    		}
        	var billingCategoryLabels = $scope.billingCategory.map(function (category) {
                return category.label;
            });
        	 var rows = [['Tariff', 'Service Code', 
            	'Service Name',
            	'Department',
            	'Sub Department']];
            $scope.secondColumn = [];
            $scope.secondColumn[0]= 'Is Doctor Share';
            $scope.secondColumn[1]= 'Is Referral Share';
            $scope.secondColumn[2]= 'Rate Editable';
            $scope.secondColumn[3]= 'Is Auto Render';
            $scope.secondColumn[4]= 'Is Discount';   
            $scope.secondColumn[5]= 'Is Appointable';
            $scope.secondColumn[6]= 'Is Quantity Editable';
            $scope.secondColumn[7]= 'Is Outsource';
            $scope.secondColumn[8]= 'Sample Collection';
            $scope.secondColumn[9]= 'Is Outsource Job';
            $scope.secondColumn[10]= 'Is tax applicable';
            $scope.secondColumn[11]= 'Is package';
            $scope.secondColumn[12]= 'Is procedure';
            $scope.secondColumn[13]= 'Is Review Applicable';
            $scope.secondColumn[14]= 'Is POC';
            $scope.secondColumn[15]= 'Is Applicable for Contract';
            $scope.secondColumn[16]= 'Pre Requisite';
            $scope.secondColumn[17]= 'Is Panel';
            $scope.secondColumn[18]= 'Is bill of Material';
            $scope.secondColumn[19]= 'Is Bed Side';
            $scope.secondColumn[20]= 'Is Pacs Required';
            $scope.secondColumn[21]= 'Is Periodicity';
            $scope.secondColumn[22]= 'Is Cyclic Appointment Applicable';
            // Concatenate the headers with billingCategory labels
             rows[0] = rows[0].concat(billingCategoryLabels);
             rows[0] = rows[0].concat($scope.secondColumn);
            // Iterate through users and add data to rows array
             
             var count =1;
            angular.forEach($scope.selectedServiceListnew, function (user,index) {
            	if(user.selected == true){
            	var countNew =count++;
            	$scope.fileNameForDownload =user.tariffDesc;
                rows.push([user.tariffDesc,user.serviceStandardCode,
                	user.serviceStandardName,
                	user.specialityName,
                	user.subSpecialityName]);
                var billingCategoryLabelsrow = user.billingCategory.map(function (category) {
                    return category.rate[index];
                });
                $scope.secondColumnRow = [];
                $scope.secondColumnRow[0]= 	user.isDocReq;
                $scope.secondColumnRow[1]=	user.isRefDoctorShare;
                $scope.secondColumnRow[2]=	user.isRateEditable;
                $scope.secondColumnRow[3]=	user.isAutoRender;
                $scope.secondColumnRow[4]=	user.isDiscount;
                $scope.secondColumnRow[5]=	user.isAppointable;
                $scope.secondColumnRow[6]=	user.isQuantityEditable;
                $scope.secondColumnRow[7]=	user.isOutsource;
                $scope.secondColumnRow[8]=	user.sampleCollection;
                $scope.secondColumnRow[9]=	user.isOutsourceJob;
                $scope.secondColumnRow[10]=	user.isTaxApplicable;
                $scope.secondColumnRow[11]=	user.isPackage;
                $scope.secondColumnRow[12]=	user.isProcedure;
                $scope.secondColumnRow[13]=	user.isReviewApplicable;
                $scope.secondColumnRow[14]=	user.isPoc;
                $scope.secondColumnRow[15]=	user.isApplicableForContract;
                $scope.secondColumnRow[16]=	user.preRequisite;
                $scope.secondColumnRow[17]= user.isPanel;
                $scope.secondColumnRow[18]= user.billOfMaterial;
                $scope.secondColumnRow[19]=	user.isBedSide;
                $scope.secondColumnRow[20]=	user.isPacsReq;
                $scope.secondColumnRow[21]=	user.isPeriodicity;
                $scope.secondColumnRow[22]=	user.isCyclicFlag;
                var lastIndex = rows.length - 1;
                rows[lastIndex] = rows[lastIndex].concat(billingCategoryLabelsrow);
                rows[lastIndex] = rows[lastIndex].concat($scope.secondColumnRow);
            }
            });

            var csvContent = "data:text/csv;charset=utf-8,";
            // Convert rows array to CSV format
            rows.forEach(function (rowArray) {
                var row = rowArray.join(",");
                csvContent += row + "\r\n";
            });

            // Create a blob from the CSV content
            //var blob = new Blob([csvContent], { type: 'text/csv' });
            var blob = new Blob([rows.map(row => row.join(',')).join('\r\n')], { type: 'text/csv' });
            var link = document.createElement('a');
            
            // Create a download link with the blob
            link.href = window.URL.createObjectURL(blob);
            link.download = $scope.fileNameForDownload+'.csv';
           // Set the read-only mode
          //  link.setAttribute('data-read-only', 'A1');
            // Append the link to the body
            document.body.appendChild(link);

            // Trigger the click event on the link to start the download
            link.click();

            // Remove the link from the body
            document.body.removeChild(link);
        };


        $scope.uploadEncounterDoc = function()
        {
             var fd = new FormData();
                fd.append('encounterId', 0);
                fd.append('visitTypeId', 2);
                fd.append('admissionId', 0);
                fd.append('patientId',0 );
                fd.append('documentTypeId',12 );
                fd.append('createdBy',$scope.userId );
                fd.append('updatedBy', $scope.userId);
                fd.append('unitId', $scope.unitId);
                fd.append('organisationId', 2);
                fd.append('expenseId', 0);
                fd.append('file',$scope.csvfile );
                fd.append('expenseId', 0);
                fd.append('grnId',0);
                fd.append('poId',0);
                fd.append('incidentReportingMapperId',0);
                fd.append('companyExpenseId',0);
                fd.append('remark','');
                fd.append('createdDate',null);
                fd.append('fileEditedName',null);
                fd.append('opbId',0);
                fd.append('vendorId',0);
                fd.append('vendorId',0);
                var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
                return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                {
                    return response;
                });
        }


        $scope.AIFunction = function() {
         var file ="";
                    var fileInput = document.getElementById('xlf');
                     file = fileInput.files[0];
                     $scope.billingCategorySavedListsss=[];
                    if (file) {
        	 $rootScope.startLoader();

              var fd = new FormData();
                 fd.append('encounterId', 0);
                 fd.append('visitTypeId', 2);
                 fd.append('admissionId', 0);
                 fd.append('patientId',0 );
                 fd.append('documentTypeId',12 );
                 fd.append('createdBy',$scope.userId );
                 fd.append('updatedBy', $scope.userId);
                 fd.append('unitId', $scope.unitId);
                 fd.append('organisationId', 2);
                 fd.append('expenseId', 0);
                 fd.append('file',$scope.csvfile );
                 fd.append('expenseId', 0);
                 fd.append('grnId',0);
                 fd.append('poId',0);
                 fd.append('incidentReportingMapperId',0);
                 fd.append('companyExpenseId',0);
                 fd.append('remark','');
                 fd.append('createdDate',null);
                 fd.append('fileEditedName',null);
                 fd.append('opbId',0);
                 fd.append('vendorId',0);
                 fd.append('vendorId',0);
                 var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
                 return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                 {
                     $rootScope.startLoader();
                     $scope.oploadedUrl = response.data.object;
                    var data = JSON.stringify({
                                          "s3csv_url":  $scope.oploadedUrl,
                                          "target_service_names": $scope.serviceNameString
                                      });

                     $.ajax({
                            url : "https://aidata.axonichealth.com/process_csv",
                            type : "POST",
                            contentType : "application/json",
                            dataType : "json",
                            data : data,
                            success : function(response){
                                if (response.status == "success") {
                                    $scope.showAILetterButton = false;
                                     $scope.names = response.duplicate_names;
                                    var downloadUrl = response.download_link; // URL from where to download

                                  									var config = {
                                  									    headers: {
                                  									        'Access-Control-Allow-Origin': '*',
                                  									        'Access-Control-Allow-Methods': 'HEAD, GET, POST, PUT, PATCH, OPTIONS, DELETE',
                                  									        'Access-Control-Allow-Headers': 'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token',
                                  									        'Allow': 'GET, POST, PUT, DELETE, OPTIONS, HEAD',
                                  									        'Accept': 'application/json, text/javascript, */*',
                                  									        'Content-Type': 'application/json'
                                  									    },
                                  									    responseType: 'blob', // Important for handling binary data
                                  									    withCredentials: true // Enables credentials (CORS issue might require this)
                                  									};


                                  									$http.get(downloadUrl, config).then(function(response) {
                                                                        var blob = new Blob([response.data], { type: response.headers('Content-Type') });
                                                                        var reader = new FileReader();

                                                                        reader.onload = function(event) {
                                                                         $rootScope.startLoader();
                                                                            var fileContent = event.target.result; // This contains the file's text
                                                                            console.log(fileContent); // Debugging purpose
                                                                             var csvData = fileContent;
                                                                                                var lines = csvData.split('\n');
                                                                                                var headers = lines[0].split(',');
                                                                                                $scope.selectedServiceListnew =[];
                                                                                                $scope.selectedServiceListUpload =[];
                                                                                                $scope.headersList =lines[0].split(',');
                                                                                                $scope.propertyListWithoutSpaces = $scope.headersList.map(function(property) {
                                                                                                    return property.replace(/\s/g, '');
                                                                                                });
                                                                                                console.log("propertyListWithoutSpaces",$scope.propertyListWithoutSpaces);

                                                                                                var dataTriff = lines[1].split(',');
                                                                                                var arrayTariff = $scope.propertyListWithoutSpaces;
                                                                                                // Find the index of "SubDepartment"
                                                                                                 var indexOfTariffTest = arrayTariff.indexOf("Tariff");
                                                                                                var matchtariffdesc = $scope.tariffList.find(function(masterTarrif) {
                                                                            					    return masterTarrif.tariffDesc === dataTriff[indexOfTariffTest];;
                                                                            					  });
                                                                            		              if (matchtariffdesc) {
                                                                            					    $scope.tariffId = matchtariffdesc.tariffId;
                                                                            					    $scope.getSelectedValue();
                                                                            				    }

                                                                            		              $timeout( function(){

                                                                                                var array = $scope.propertyListWithoutSpaces;
                                                                                                // Find the index of "SubDepartment"
                                                                                                 var indexOfTariff = array.indexOf("Tariff");
                                                                                                 var indexOfServiceCode = array.indexOf("ServiceCode");
                                                                                                 var indexOfServiceName = array.indexOf("ServiceName");
                                                                                                 var indexOfDepartment = array.indexOf("Department");
                                                                                                 var indexOfSubDepartment = array.indexOf("SubDepartment");
                                                                                                 var indexOfIsDoctorShare = array.indexOf("IsDoctorShare");
                                                                                                 var indexOfIsReferralShare = array.indexOf("IsReferralShare");
                                                                                                 var indexOfRateEditable = array.indexOf("RateEditable");
                                                                                                 var indexOfIsAutoRender = array.indexOf("IsAutoRender");
                                                                                                 var indexOfIsDiscount = array.indexOf("IsDiscount");
                                                                                                 var indexOfIsAppointable = array.indexOf("IsAppointable");
                                                                                                 var indexOfIsQuantityEditable = array.indexOf("IsQuantityEditable");
                                                                                                 var indexOfIsOutsource = array.indexOf("IsOutsource");
                                                                                                 var indexOfSampleCollection = array.indexOf("SampleCollection");
                                                                                                 var indexOfIsOutsourceJob = array.indexOf("IsOutsourceJob");
                                                                                                 var indexOfIstaxapplicable = array.indexOf("Istaxapplicable");
                                                                                                 var indexOfIspackage = array.indexOf("Ispackage");
                                                                                                 var indexOfIsprocedure = array.indexOf("Isprocedure");
                                                                                                 var indexOfIsReviewApplicable = array.indexOf("IsReviewApplicable");
                                                                                                 var indexOfIsPOC = array.indexOf("IsPOC");
                                                                                                 var indexOfIsApplicableforContract = array.indexOf("IsApplicableforContract");
                                                                                                 var indexOfPreRequisite = array.indexOf("PreRequisite");
                                                                                                 var indexOfIsPanel = array.indexOf("IsPanel");
                                                                                                 var indexOfIsbillofMaterial = array.indexOf("IsbillofMaterial");
                                                                                                 var indexOfIsBedSide = array.indexOf("IsBedSide");
                                                                                                 var indexOfIsPacsRequired = array.indexOf("IsPacsRequired");
                                                                                                 var indexOfIsPeriodicity = array.indexOf("IsPeriodicity");
                                                                                                 var indexOfIsCyclicAppointmentApplicable = array.indexOf("IsCyclicAppointmentApplicable");
                                                                                                // Log the result
                                                                                               var count =  indexOfSubDepartment+1;
                                                                                                var jsonData = [];
                                                                                                var jsonDataNew = [];
                                                                                                $scope.rowListNew =[];
                                                                                                for (var i = 1; i < lines.length; i++) {
                                                                                                    var data = lines[i].split(',');
                                                                                                    var item = {};
                                                                                                    var itemNew = {};
                                                                                                    for (var j = 0; j < headers.length; j++) {
                                                                                                        item[headers[j]] = data[j];
                                                                                                    }
                                                                                                    for (var j = 0; j < $scope.propertyListWithoutSpaces.length; j++) {
                                                                                                    	itemNew[$scope.propertyListWithoutSpaces[j]] = data[j];
                                                                                                    }
                                                                                                    jsonData.push(item);
                                                                                                    jsonDataNew.push(itemNew);
                                                                                                    var jjj ={};
                                                                                                    jjj.propertyListWithoutSpacesNew =data;
                                                                                                    $scope.rowListNew.push(jjj);
                                                                                                }
                                                                                             //  $scope.billingCategory=[];
                                                                                                $scope.billingCategoryTest =[];
                                                                                                    var datade = lines[0].split(',');
                                                                                                    $scope.billingCategoryTest =[];
                                                                                                    $scope.billingCategorySavedList=[];
                                                                                                    for (var j = indexOfSubDepartment+1; j < indexOfIsDoctorShare; j++) {
                                                                                                    	var billbed ={};
                                                                                                    	billbed.label = datade[j];
                                                                                                    	billbed.index = j;
                                                                                                    	  var BILLINGBEDID =null;
                                                                                    					  var MATCHBILLINGBEDID = $scope.billingCategory.find(function(v) {
                                                                                    						    return v.label === billbed.label;
                                                                                    						  });
                                                                                    					  if (MATCHBILLINGBEDID) {
                                                                                    						  billbed.id = MATCHBILLINGBEDID.id;
                                                                                    					  }
                                                                                                        $scope.billingCategorySavedList.push(billbed);
                                                                                                     //   $scope.billingCategory.push(billbed);
                                                                                                    }


                                                                                                for (var i = 1; i < lines.length; i++) {
                                                                                                    var data = lines[i].split(',');
                                                                                                    var item = {};
                                                                                                    var itemNew = {};
                                                                                                    for (var j = 0; j <= indexOfSubDepartment; j++) {
                                                                                                        item[headers[j]] = data[j];
                                                                                                    }

                                                                                                    for (var h = indexOfSubDepartment+1; h < indexOfIsDoctorShare; h++) {
                                                                                                    	var billingbedObject={};
                                                                                                    	billingbedObject.value =data[h];
                                                                                                        $scope.billingCategoryTest.push(billingbedObject);
                                                                                                    }
                                                                                                    console.log("$scope.billingCategoryTest",$scope.billingCategoryTest);
                                                                                                    console.log("$scope.billingCategory",$scope.billingCategory);
                                                                                                    console.log("$scope.billingCategorySavedList",$scope.billingCategorySavedList);
                                                                                                  if(data[indexOfServiceName] !=undefined){
                                                                                                	itemNew.tariffDesc = data[indexOfTariff];
                                                                                                    itemNew.serviceStandardCode = data[indexOfServiceCode];
                                                                                                    itemNew.serviceStandardName = data[indexOfServiceName];
                                                                                                    itemNew.specialityName = data[indexOfDepartment];
                                                                                                    itemNew.subSpecialityName = data[indexOfSubDepartment];
                                                                                                    itemNew.isDocReq= data[indexOfIsDoctorShare];
                                                                                                    itemNew.isRefDoctorShare= data[indexOfIsReferralShare];
                                                                                                    itemNew.isRateEditable= data[indexOfRateEditable];
                                                                                                    itemNew.isAutoRender= data[indexOfIsAutoRender];
                                                                                                    itemNew.isDiscount= data[indexOfIsDiscount];
                                                                                                    itemNew.isAppointable= data[indexOfIsAppointable];
                                                                                                    itemNew.isQuantityEditable= data[indexOfIsQuantityEditable];
                                                                                                    itemNew.isOutsource= data[indexOfIsOutsource];
                                                                                                    itemNew.sampleCollection= data[indexOfSampleCollection];
                                                                                                    itemNew.isOutsourceJob= data[indexOfIsOutsourceJob];
                                                                                                    itemNew.isTaxApplicable= data[indexOfIstaxapplicable];
                                                                                                    itemNew.isPackage= data[indexOfIspackage];
                                                                                                    itemNew.isProcedure= data[indexOfIsprocedure];
                                                                                                    itemNew.isReviewApplicable= data[indexOfIsReviewApplicable];
                                                                                                    itemNew.isPoc= data[indexOfIsPOC];
                                                                                                   // itemNew.isApplicableForContract= data[indexOfIsApplicableforContract];
                                                                                                    itemNew.preRequisite= data[indexOfPreRequisite];
                                                                                                    itemNew.isPanel= data[indexOfIsPanel];
                                                                                                    itemNew.billOfMaterial= data[indexOfIsbillofMaterial];
                                                                                                    itemNew.isBedSide= data[indexOfIsBedSide];
                                                                                                    itemNew.isPacsReq= data[indexOfIsPacsRequired];
                                                                                                    itemNew.isPeriodicity= data[indexOfIsPeriodicity];
                                                                                                    itemNew.isCyclicFlag= "Y";

                                                                                                    $scope.billingCategorySavedListsss=[];
                                                                                                    angular.forEach($scope.billingCategorySavedList,function(value,indd){
                                                                                                    	var nenen ={};
                                                                                                    	nenen.label=value.label;
                                                                                                    	nenen.rate ={};
                                                                                                    	nenen.rate[i-1]  = data[value.index];
                                                                                                    	nenen.id=value.id;
                                                                                                    	$scope.billingCategorySavedListsss.push(nenen);
                                                                                                    });

                                                                                                    $scope.billingCategory = $scope.billingCategorySavedList;
                                                                                                    itemNew.billingCategory = $scope.billingCategorySavedListsss;
                                                                                                    itemNew.selected =true;
                                                                                                    $scope.selectedServiceListnew.push(itemNew);
                                                                                                  }
                                                                                                }
                                                                                                console.log("data of file",jsonData);
                                                                                                console.log("data of file",jsonDataNew);
                                                                                                console.log("header",JSON.stringify($scope.propertyListWithoutSpaces));
                                                                                                console.log("$scope.propertyListWithoutSpacesNew",$scope.propertyListWithoutSpacesNew);
                                                                                                console.log("$scope.billingCategorySavedListsss",$scope.billingCategorySavedListsss);
                                                                                                console.log("finalData",$scope.selectedServiceListnew);
                                                                                                console.log("$scope.propertyListWithoutSpacesNew",$scope.propertyListWithoutSpacesNew);
                                                                                                $rootScope.stopLoader();
                                                                            		              },8000); // Display on UI
                                                                        };

                                                                        reader.readAsText(blob); // Read the file as text
                                                                    }).catch(function(error) {
                                                                    $rootScope.stopLoader();
                                                                        console.error("File download failed:", error);
                                                                    });




                                }
                                else{
                                    growl.error("AI site is down");
                                    $rootScope.stopLoader();
                                }
                            },
                            error : function() {
                                growl.error("Please check AI configuration,Server is down!!");
                                $rootScope.stopLoader();
                            }
                        })


                 });
             } else {
                            growl.error("No file selected.",
                                    {
                                error : 'warning!'
                                    });
                             $rootScope.stopLoader();

                        }

        }


        $scope.headersList =[];  
        $scope.selectedServiceListUpload =[];
        $scope.propertyListWithoutSpacesNew =[];
        $scope.readFile = function() {
        	 $rootScope.startLoader();
            $scope.headersList =[];  
            $scope.selectedServiceListUpload =[];
            $scope.propertyListWithoutSpacesNew =[];
        	 var file ="";
            var fileInput = document.getElementById('xlf');
             file = fileInput.files[0];
             $scope.billingCategorySavedListsss=[];
            if (file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var csvData = e.target.result;
                    var lines = csvData.split('\n');
                    var headers = lines[0].split(',');
                    $scope.selectedServiceListnew =[];
                    $scope.selectedServiceListUpload =[];
                    $scope.headersList =lines[0].split(',');
                    $scope.propertyListWithoutSpaces = $scope.headersList.map(function(property) {
                        return property.replace(/\s/g, '');
                    });
                    console.log("propertyListWithoutSpaces",$scope.propertyListWithoutSpaces);
                    
                    var dataTriff = lines[1].split(',');
                    var arrayTariff = $scope.propertyListWithoutSpaces;
                    // Find the index of "SubDepartment"
                     var indexOfTariffTest = arrayTariff.indexOf("Tariff");
                    var matchtariffdesc = $scope.tariffList.find(function(masterTarrif) {
					    return masterTarrif.tariffDesc === dataTriff[indexOfTariffTest];;
					  });
		              if (matchtariffdesc) {
					    $scope.tariffId = matchtariffdesc.tariffId;
					    $scope.getSelectedValue();
				    }
			  
		              $timeout( function(){
		              
                    var array = $scope.propertyListWithoutSpaces;
                    // Find the index of "SubDepartment"
                     var indexOfTariff = array.indexOf("Tariff");
                     var indexOfServiceCode = array.indexOf("ServiceCode");
                     var indexOfServiceName = array.indexOf("ServiceName");
                     var indexOfDepartment = array.indexOf("Department");
                     var indexOfSubDepartment = array.indexOf("SubDepartment");
                     var indexOfIsDoctorShare = array.indexOf("IsDoctorShare");
                     var indexOfIsReferralShare = array.indexOf("IsReferralShare");
                     var indexOfRateEditable = array.indexOf("RateEditable");
                     var indexOfIsAutoRender = array.indexOf("IsAutoRender");
                     var indexOfIsDiscount = array.indexOf("IsDiscount");
                     var indexOfIsAppointable = array.indexOf("IsAppointable");
                     var indexOfIsQuantityEditable = array.indexOf("IsQuantityEditable");
                     var indexOfIsOutsource = array.indexOf("IsOutsource");
                     var indexOfSampleCollection = array.indexOf("SampleCollection");
                     var indexOfIsOutsourceJob = array.indexOf("IsOutsourceJob");
                     var indexOfIstaxapplicable = array.indexOf("Istaxapplicable");
                     var indexOfIspackage = array.indexOf("Ispackage");
                     var indexOfIsprocedure = array.indexOf("Isprocedure");
                     var indexOfIsReviewApplicable = array.indexOf("IsReviewApplicable");
                     var indexOfIsPOC = array.indexOf("IsPOC");
                     var indexOfIsApplicableforContract = array.indexOf("IsApplicableforContract");
                     var indexOfPreRequisite = array.indexOf("PreRequisite");
                     var indexOfIsPanel = array.indexOf("IsPanel");
                     var indexOfIsbillofMaterial = array.indexOf("IsbillofMaterial");
                     var indexOfIsBedSide = array.indexOf("IsBedSide");
                     var indexOfIsPacsRequired = array.indexOf("IsPacsRequired");
                     var indexOfIsPeriodicity = array.indexOf("IsPeriodicity");
                     var indexOfIsCyclicAppointmentApplicable = array.indexOf("IsCyclicAppointmentApplicable");
                    // Log the result
                   var count =  indexOfSubDepartment+1;
                    var jsonData = [];
                    var jsonDataNew = [];
                    $scope.rowListNew =[];
                    for (var i = 1; i < lines.length; i++) {
                        var data = lines[i].split(',');
                        var item = {};
                        var itemNew = {};
                        for (var j = 0; j < headers.length; j++) {
                            item[headers[j]] = data[j];
                        }
                        for (var j = 0; j < $scope.propertyListWithoutSpaces.length; j++) {
                        	itemNew[$scope.propertyListWithoutSpaces[j]] = data[j];
                        }
                        jsonData.push(item);
                        jsonDataNew.push(itemNew);
                        var jjj ={};
                        jjj.propertyListWithoutSpacesNew =data;
                        $scope.rowListNew.push(jjj);
                    }
                 //  $scope.billingCategory=[];
                    $scope.billingCategoryTest =[];
                        var datade = lines[0].split(',');
                        $scope.billingCategoryTest =[];
                        $scope.billingCategorySavedList=[];
                        for (var j = indexOfSubDepartment+1; j < indexOfIsDoctorShare; j++) {
                        	var billbed ={};
                        	billbed.label = datade[j];
                        	billbed.index = j;
                        	  var BILLINGBEDID =null;
        					  var MATCHBILLINGBEDID = $scope.billingCategory.find(function(v) {
        						    return v.label === billbed.label;
        						  });
        					  if (MATCHBILLINGBEDID) {
        						  billbed.id = MATCHBILLINGBEDID.id;
        					  }
                            $scope.billingCategorySavedList.push(billbed);
                         //   $scope.billingCategory.push(billbed);
                        }
                   
                    
                    for (var i = 1; i < lines.length; i++) {
                        var data = lines[i].split(',');
                        var item = {};
                        var itemNew = {};
                        for (var j = 0; j <= indexOfSubDepartment; j++) {
                            item[headers[j]] = data[j];
                        }
                    
                        for (var h = indexOfSubDepartment+1; h < indexOfIsDoctorShare; h++) {
                        	var billingbedObject={};
                        	billingbedObject.value =data[h];
                            $scope.billingCategoryTest.push(billingbedObject);
                        }
                        console.log("$scope.billingCategoryTest",$scope.billingCategoryTest);
                        console.log("$scope.billingCategory",$scope.billingCategory);
                        console.log("$scope.billingCategorySavedList",$scope.billingCategorySavedList);
                      if(data[indexOfServiceName] !=undefined){
                    	itemNew.tariffDesc = data[indexOfTariff];
                        itemNew.serviceStandardCode = data[indexOfServiceCode];
                        itemNew.serviceStandardName = data[indexOfServiceName];
                        itemNew.specialityName = data[indexOfDepartment];
                        itemNew.subSpecialityName = data[indexOfSubDepartment];
                        itemNew.isDocReq= data[indexOfIsDoctorShare];
                        itemNew.isRefDoctorShare= data[indexOfIsReferralShare];
                        itemNew.isRateEditable= data[indexOfRateEditable];
                        itemNew.isAutoRender= data[indexOfIsAutoRender];
                        itemNew.isDiscount= data[indexOfIsDiscount];
                        itemNew.isAppointable= data[indexOfIsAppointable];
                        itemNew.isQuantityEditable= data[indexOfIsQuantityEditable];
                        itemNew.isOutsource= data[indexOfIsOutsource];
                        itemNew.sampleCollection= data[indexOfSampleCollection];
                        itemNew.isOutsourceJob= data[indexOfIsOutsourceJob];
                        itemNew.isTaxApplicable= data[indexOfIstaxapplicable];
                        itemNew.isPackage= data[indexOfIspackage];
                        itemNew.isProcedure= data[indexOfIsprocedure];
                        itemNew.isReviewApplicable= data[indexOfIsReviewApplicable];
                        itemNew.isPoc= data[indexOfIsPOC];
                       // itemNew.isApplicableForContract= data[indexOfIsApplicableforContract];
                        itemNew.preRequisite= data[indexOfPreRequisite];
                        itemNew.isPanel= data[indexOfIsPanel];
                        itemNew.billOfMaterial= data[indexOfIsbillofMaterial];
                        itemNew.isBedSide= data[indexOfIsBedSide];
                        itemNew.isPacsReq= data[indexOfIsPacsRequired];
                        itemNew.isPeriodicity= data[indexOfIsPeriodicity];
                        itemNew.isCyclicFlag= "Y";
                       
                        $scope.billingCategorySavedListsss=[];
                        angular.forEach($scope.billingCategorySavedList,function(value,indd){
                        	var nenen ={};
                        	nenen.label=value.label;
                        	nenen.rate ={};
                        	nenen.rate[i-1]  = data[value.index];
                        	nenen.id=value.id;
                        	$scope.billingCategorySavedListsss.push(nenen);
                        });
                        
                        $scope.billingCategory = $scope.billingCategorySavedList;
                        itemNew.billingCategory = $scope.billingCategorySavedListsss;
                        itemNew.selected =true;
                        $scope.selectedServiceListnew.push(itemNew);
                      }
                    }
                    console.log("data of file",jsonData);
                    console.log("data of file",jsonDataNew);
                    console.log("header",JSON.stringify($scope.propertyListWithoutSpaces));
                    console.log("$scope.propertyListWithoutSpacesNew",$scope.propertyListWithoutSpacesNew);
                    console.log("$scope.billingCategorySavedListsss",$scope.billingCategorySavedListsss);
                    console.log("finalData",$scope.selectedServiceListnew);
                    console.log("$scope.propertyListWithoutSpacesNew",$scope.propertyListWithoutSpacesNew);
                    $rootScope.stopLoader();
		              },5000);
                    
                };
                reader.readAsText(file);
            } else {
            	growl.error("No file selected.",
    					{
            		error : 'warning!'
    					});
            	 $rootScope.stopLoader();
                //console.error('No file selected.');
            }
        };
		
		$scope.saveGlobalList = function() {
			try {
				
				var finalData ={
						tariffId:$scope.tariffId
				};
				
				if($scope.selectedServiceListnew  == undefined || $scope.selectedServiceListnew  == null || $scope.selectedServiceListnew.length == 0){
					growl.warning("Please Add Service !!",{
						title : 'Warning!'
					});
					return false;
				}
				
				$rootScope.startLoader();
				
				  $scope.globalServiceMapperList =[];
				  console.log("final save list ",JSON.stringify($scope.selectedServiceListnew));
				  console.log("masterSpeacilityList ",$scope.masterSpeacilityList);
				  console.log("masterSubSpecialityList ",$scope.masterSubSpecialityList);
				
				  angular.forEach($scope.selectedServiceListnew, function(serviceItem,index1) {
					  if(serviceItem.selected == true){
					  var specialityId =null;
					  var matchingSpeciality = $scope.masterSpeacilityList.find(function(masterSpeciality) {
						    return masterSpeciality.label === serviceItem.specialityName;
						  });
					  if (matchingSpeciality) {
						   specialityId = matchingSpeciality.id;
					  }
					  var subSpecialityId =null;
					  var matchingSubSpeciality = $scope.masterSubSpecialityList.find(function(masterSpeciality) {
						    return masterSpeciality.subSpecialityName === serviceItem.subSpecialityName;
						  });
					  if (matchingSubSpeciality) {
						  subSpecialityId = matchingSubSpeciality.subSpecialityId;
					  }
					  angular.forEach(serviceItem.billingCategory, function(billingCategoryItem,index2) {
						  if(billingCategoryItem.rate[index1] != undefined && billingCategoryItem.rate[index1] != null && billingCategoryItem.rate[index1] != "" ){
					    var newGlobalServiceMapper = {
					      serviceStandardName: serviceItem.serviceStandardName,
					      serviceStandardCode: serviceItem.serviceStandardCode,
					      specialityName: serviceItem.specialityName,
					      specialityId: specialityId,
					      tariffId:$scope.tariffId,
					      subSpecialityName: serviceItem.subSpecialityName,
					      subSpecialityId: subSpecialityId,
					      billingBedCategoryId: billingCategoryItem.id,
					      billingBedDesc: billingCategoryItem.label,
					      rate: billingCategoryItem.rate[index1] != undefined && billingCategoryItem.rate[index1] != null && billingCategoryItem.rate[index1] != "" ? billingCategoryItem.rate[index1]:0  ,
					      isDocReq:serviceItem.isDocReq != 'I'? serviceItem.isDocReq:'N',
					      isRefDoctorShare:serviceItem.isRefDoctorShare != 'I'? serviceItem.isRefDoctorShare:'N',
					      isRateEditable:serviceItem.isRateEditable != 'I'? serviceItem.isRateEditable:'N',
					      isAutoRender:serviceItem.isAutoRender,
					      isDiscount:serviceItem.isDiscount != 'I'? serviceItem.isDiscount:'N',
					      isAppointable:serviceItem.isAppointable,
					      isQuantityEditable:serviceItem.isQuantityEditable,
					      isOutsource:serviceItem.isOutsource,
					      sampleCollection:serviceItem.sampleCollection,
					      isOutsourceJob:serviceItem.isOutsourceJob,
					      isTaxApplicable:serviceItem.isTaxApplicable  != 'I'? serviceItem.isTaxApplicable:'N',
					      isPackage:serviceItem.isPackage,
					      isProcedure:serviceItem.isProcedure,
					      isPoc:serviceItem.isPoc,
					      isApplicableForContract:serviceItem.isApplicableForContract,
					      preRequisite:serviceItem.preRequisite,
					      isPanel:serviceItem.isPanel,
					      billOfMaterial:serviceItem.billOfMaterial,
					      isBedSide:serviceItem.isBedSide,
					      isPacsReq:serviceItem.isPacsReq,
					      isPeriodicity:serviceItem.isPeriodicity,
					      isCyclicFlag:serviceItem.isCyclicFlag,
					      isCyclicFlag:serviceItem.isCyclicFlag,
					      organizationId:$scope.organizationId,
						  unitId:$scope.unitId,
						  createdBy:$scope.userId,
						   updatedBy:$scope.userId,
						   paymentEntitlementId:1,
					    };

					    $scope.globalServiceMapperList.push(newGlobalServiceMapper);
					  }
					  });
				     }
					});
				  
				  
				  $scope.globalServiceMapperListFinal =[];
				  $scope.globalServiceMapperListFinalWithPatientType =[];
				  angular.forEach($scope.globalServiceMapperList, function (v, k) {
					    angular.forEach($scope.selectedVisitType, function (visitTypeItem, visitTypeIndex) {
					        var newData = angular.copy(v); // Copy the object to avoid modifying the original
					        newData.visitTypeId = visitTypeItem.id; // Assuming 'id' is the property you want to assign
					        $scope.globalServiceMapperListFinal.push(newData);
					    });
					});
				  
				  angular.forEach($scope.globalServiceMapperListFinal, function (value, k) {
					    angular.forEach($scope.patientTypeListMain, function (patientTypeItem, patientTypeIndex) {
					        var newPatientType = angular.copy(value); // Copy the object to avoid modifying the original
					        newPatientType.patientTypeId = patientTypeItem.id; // Assuming 'id' is the property you want to assign
					        $scope.globalServiceMapperListFinalWithPatientType.push(newPatientType);
					    });
					});
				  
				  finalData.globalServiceMapperList = $scope.globalServiceMapperListFinalWithPatientType;
				  console.log("$scope.globalServiceMapperList",$scope.globalServiceMapperListFinalWithPatientType);
				// return false;
                	var URI = BASE_URL + ROOT_URL + "/api/global/saveGlobalMasterData"
        			GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
        					finalData, NOTIFICATION_MSG_STATUS_TRUE)
        					.then(function(response) {
        						if(response.data.status == "success"){
        							$state.reload();
        							$rootScope.stopLoader();
        						}
        						else{
        							$rootScope.stopLoader();
        						}
        			});
               
			} catch (e) {
				$rootScope.stopLoader();
				console.log("Exception-", e);
			}
		}
		
	    /***============================================Code file upload ==============================================*/
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("globalServiceMaster", {
            url: "/globalServiceMaster",
            templateUrl: "views/master/GlobalMaster/global-service-master.html",
            controller: "GlobalServiceMaster.Controller",
            title: 'Global Service Master',
            resolve : {
                data: function($q, $state, $timeout) {
                    var deferred = $q.defer();
                    $timeout(function() {
                        var arr = JSON.parse(localStorage.getItem('permissions'))
                        .modules.filter(function(item){
                           return (item.id == 1071);
                        });
                        if(arr.length <= 0){
                            $state.go("accessDenied")
                            deferred.reject();
                         } else {
                            deferred.resolve();
                         }
                    });

                    return deferred.promise;
                  }
             },
            controllerAs: "vm",
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("GlobalServiceMaster.Controller", globalServiceMasterController)
})();