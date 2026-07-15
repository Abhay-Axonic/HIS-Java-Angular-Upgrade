angular.module("myApp").controller("webAppointmentController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','$timeout','growl','paymentCollFactory','reportService','GenericService','$location','$localStorage','loyaltiCardCallFactory',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,$timeout,growl,paymentCollFactory,reportService,GenericService,$location, $localStorage,loyaltiCardCallFactory){
	try{
		
		$rootScope.loginpage = false;
		 var baseUrlLocation = window.location.href;
         var temp = baseUrlLocation.split('//');
         var BASE_URL_CALL = temp[1].split('.');
         var isLocalhost =  (/:/.test(temp[1]) || window.location.hostname.includes("localhost") );
         
         var subdomain = (isLocalhost==true || BASE_URL_CALL[0]=='sme' || BASE_URL_CALL[0]=='qa' || BASE_URL_CALL[0]=='intdev' || BASE_URL_CALL[0]=='demo' ) ? 'localhost' :  BASE_URL_CALL[0]; 
     	 var data2 = "";
         
		var URI = BASE_URL + ROOT_URL + ORGANIZATIONLIST + "/"+subdomain ;
		GenericService
			.serviceAction("GET", URI, data2)
			.then(
				function (response) {
					$scope.organizationList = [];
					if (response.data.status == "success") {
						$scope.organizationList = response.data.listObject;
						if($scope.organizationList.length == 1)
							{
								$scope.organizationId = $scope.organizationList[0].organizationId;
								$scope.disableOrgList = true;
								//$scope.getUnit($scope.organizationId);
							}else{
								$scope.organizationId = $scope.organizationList[0].organizationId;
								$scope.disableOrgList = false;
							}
						$scope.getUnitByUserCredentials();
						$scope.getcountry();
						
					}
				});
		
		$scope.getUnitByUserCredentials = function () {
				var URI = BASE_URL + ROOT_URL+ "/unit/org/"+$scope.organizationId;
				GenericService.serviceAction("GET", URI, {}).then(function(response) 
					{
				    if (response.data.status == "success") {
								$scope.stopLoader();
								$scope.unitList = response.data.listObject;
								$scope.unitId = $scope.unitList[0].unitId;
								$scope.getusersList();
								$scope.getSpecialityMasterList();
								
					}else if (response.data.status == "error") 
					{
						$rootScope.stopLoader();
					}	
				});
			
		};
		
		
		$scope.getusersList = function(){

			var URI = BASE_URL + ROOT_URL + GET_USER_LIST_FOR_STORE+ S +$scope.organizationId +S +$scope.unitId;
			var data = {
					orgId:$scope.organisationId,
					unitId:$scope.unitId
			}
			GenericService.serviceAction("GET",URI,data).then(function(response){
			if(response.data.status == "success"){
					$rootScope.stopLoader();
					$scope.usersList = [];
					$scope.usersList=response.data.listObject;
					$scope.userId =$scope.usersList[0].userId
					console.log("usersList",$scope.getusersList);
					}
			});

			}

		
			var data = {};

		
	
	$scope.getcountry = function(){
		
		var datass = {
				organizationId : $scope.organizationId
		};
	var URI = BASE_URL + ROOT_URL
	+ "/adt/getActivePrefixListForWebPage";
		GenericService
		.serviceAction("POST", URI, datass)
		.then(
				function(response) {
		console.log(response);
		$scope.prefixList = [];
		if (response.data.status == "success")
			$scope.prefixList = response.data.listObject;
	});

var URI = BASE_URL + ROOT_URL
+ "/adt/getActiveRelationListForWebPage";
GenericService
	.serviceAction("POST", URI, datass)
	.then(
			function(response) {
				$scope.relationList = [];
				if (response.data.status == "success") {
					$scope.relationList = response.data.listObject;
				}
			});

		var URI = BASE_URL + ROOT_URL
		+ GETACTIVECOUNTRYLIST+$scope.organizationId;
			GenericService
			.serviceAction("GET", URI, data)
			.then(
					function(response) {
			console.log(response);
			$scope.countryList = [];
			if (response.data.status == "success")
				$scope.countryList = response.data.listObject;
		});
	}
	

		
		var URI = BASE_URL + ROOT_URL
		+ GETACTIVEGENDERLIST;
		GenericService
		.serviceAction("GET", URI, data)
		.then(
				function(response) {
					console.log(response);
					$scope.genderList = [];
					if (response.data.status == "success")
						$scope.genderList = response.data.listObject;
				});
		
		$scope.getAllData = function () {
			$scope.unitId = $scope.unitId;
			$scope.getSpecialityMasterList();
		};
		$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				//minDate : new Date()
			};
		
		$scope.dateOptionsTo = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		
		$scope.openDatePicker = function() { 
			$scope.datepicker.opened = true;
		};
		
		$scope.openDatePickerTo = function() {
			$scope.datepickerTo.opened = true;
		};
		$scope.datepicker = {
			opened : false
		};
		$scope.datepickerTo = {
				opened : false
			};
		$scope.init = function(){
			$scope.appointmentMasterObj = {
					 "encounterId"			:"",
					 "patientId"			:"",
					 "visitTypeId"			:"",
					 "doctorId"				:"",
					 "sourceId"				:"",
					 "encounterDate"		:new Date().getTime(),
					 "encounterTime"		:"",
					 "isReferal"			:"A",
					 "deptId"				:"",
					 "subDeptId"			:"",
					 "remark"				:"",
					 "kinDetailsId"			:"",
					 "paymentEntitlementTypeId":"1",
					 "encounterNumber"		:"",
					 "createdDate"			:new Date().getTime(),
					 "createdBy"			:$scope.userId,
					 "updatedDate"			:new Date().getTime(),
					 "updatedBy"			:$scope.userId,
					 "status"				:"A",
					 "unitId"				:$scope.unitId,
					 "organizationId"		:$scope.organizationId,
					 "listKinDetailsDto"	:$scope.kinDetailsList,
					 "isScheduledAppointment":"I",
					 "modalityId":"",
					 "specialityId":"",
					 //"subSpecialityId":"",
					 "systemGeneratedVisitTypeId":"",
					 "userDefinedVisitTypeId":"",
					 "reasonIdForChangingVisitType":"",
					 "remarkForChangingVisitType":"",
					 "encounterTypeId": "",
					 "clinicId":"",
					 "referralTypeId":"",
					 "referralId":"",
					 "referralRemark":"",
					 "defaultSelfTariffId" : $scope.defaultSelfTariffId,
					 "isAppointmentRequired" : $scope.isAppointmentRequired,
					 "currentDate" :  moment(new Date()).format('YYYY-MM-DD'),
					 "counterId":$rootScope.counterId
				}
			
			$scope.appointmentMasterObj.appointmentDate1 = new Date();
			$scope.patientObj = {

					prefixId : '',
					firstName : '',
					middleName : '',
					lastName : '',
					genderId : '',
					countryCallingCode : '',
					email : '',
					nationalityId : '',
					raceId : '',
					maritalStatusId : '',
					mobileNumber : '',
					dob : '',
					identificationTypeId : '',
					identificationNumber : '',
					isVip : '',
					address : '',
					countryId : '',
					stateId : '',
					districtId : '',
					cityId :'',
					areaId : '',
					zipCode : '',
					phoneCode : '',
					phoneNumber : '',
					companyName : '',
					companyAddress : '',
					companyCountryId : '',
					companyStateId : '',
					companyDistrictId : '',
					companyCityId : '',
					companyAreaId : '',
					companyZipCode : '',
					companyMobileNumber : '',
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					updatedBy : $scope.userId,
					createdBy : $scope.userId,
					status : 'A',
					patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
					permanentAddress:'',
					permanentCountryId:'',
					permanentStateId:'',
					permanentDistrictId:'',
					permanentCityId:'',
					permanentAreaId:'',
					permanentZipCode:'',
					permanentPhoneCode:'',
					permanentPhoneNumber:'',
					aliseName:'',
					identificationExpiryDate:'',
					occupationId:'',
					registrationTypeId:5,
					previousId:'',
					patientId:0,
					patientDetailsId:'',
					imagePath:'',
					thumbImage:'',
					whatsappCallingCode : '',
					whatsappNo:'',
					skypeId:'',
					area:'',
					permanentArea:'',
					kinDetailsId:''
				};
			
		    $scope.kinDetails = {
					prefixId   		: '',
					kinName  		: '',
					relationId 		: '',
					mobileNo 		: '',
					phoneNo 		: '',
					expiry 			: '',
					identificationId: '',
					identificationNo: '',
					isGuarantor 	: 'N',
					address 		: '',
					countryId 		: $scope.unitCountryId!=undefined && $scope.unitCountryId !=null ? $scope.unitCountryId : '',
					stateId 		:  $scope.unitStateId!=undefined && $scope.unitStateId !=null ? $scope.unitStateId : '',
					districtId 		:  $scope.unitDistrictId!=undefined && $scope.unitDistrictId !=null ? $scope.unitDistrictId : '',
					cityId 			: $scope.unitCityId!=undefined && $scope.unitCityId !=null ? $scope.unitCityId : '',
					areaId 			: $scope.unitAreaId!=undefined && $scope.unitAreaId !=null ? $scope.unitAreaId : '',
					postCode 		: '',
					patientId       : '',
					unitId			: $scope.unitId,
					organizationId  : $scope.organizationId,
					createdBy		: $scope.userId,
					updatedBy		: $scope.userId,
//					expiryDateDisabled 		: true,
					createdDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
				};
			$scope.paymentEntitleMentList=
				[{
					'paymentEntitlementName':"Self",
					'paymentEntitlementId':"1",
					'companyName':"Self",
					'tariffName':"Self Tariff",
					'contractName':"",
					'insuranceName':"",
					'insuranceNumber':"",
					'startDate':"",
					'endDate':"",
					'priority':"1",
					'companyId':$scope.selfPayeeIdCookies,
					'payeeId':$scope.selfPayeeIdCookies,
					'tariffId':$scope.defaultSelfTariffId,
					'contractId':null,
					'insuredAmount' : ''
				}];
			
		}
		$scope.init();
		
		$scope.openDatePickerAppointment= function() {
			$scope.datepickerAppo.opened = true;
		};

		$scope.datepickerAppo = {
			opened : false
		};
		
		$scope.getSpecialityMasterList = function(){
			try{
				var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
				var obj = {
							organizationId : $scope.organizationId,
							unitId         :$scope.unitId
					    }
				
				GenericService.serviceAction("POST", URI, obj).then(function(response) 
						{
			
					if(response.data.status == "success"){
						$scope.specialityMasterList = response.data.listObject;
						$scope.specialityMasterListCopy = response.data.listObject;
						console.log('splList',$scope.specialityMasterList);
					}
				});
			}catch (e) {
				console.log("Exception",e.message);
			}
		}
		
		
		
		$scope.getDoctorBySpeciality = function(id) {
			
			$scope.serviceOrderList = [];
			
			var data = {
				specialityId : id,
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
			
			var URI = null;
			URI = BASE_URL + ROOT_URL + "/api/global/appoitments/doctors";
			GenericService.serviceAction("POST", URI, data).then(function(response) 
					{
								console.log(response);
								$scope.doctorList = [];
								// $rootScope.startLoader();
								if (response.data.status == "success"){
									$scope.doctorList = response.data.listObject;
								console.log('DoctorList',$scope.doctorList);
								
							}
			});

		}
		$scope.datediff = function(dateFrom, dateTo)
		{
		    var from = {
		        d: dateFrom.getDate(),
		        m: dateFrom.getMonth(),
		        y: dateFrom.getFullYear()
		    };

		    var to = {
		        d: dateTo.getDate(),
		        m: dateTo.getMonth(),
		        y: dateTo.getFullYear()
		    };

		    var daysFebruary = to.y % 4 != 0 || (to.y % 100 == 0 && to.y % 400 != 0)? 28 : 29;
		    var daysInMonths = [31, daysFebruary, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

		    if (to.d < from.d) {
		        to.d   += daysInMonths[parseInt(to.m)];
		        from.m += 1;
		    }
		    if (to.m < from.m) {
		        to.m   += 12;
		        from.y += 1;
		    }

		    return {
		        days:   to.d - from.d,
		        months: to.m - from.m,
		        years:  to.y - from.y
		    };
		}
		
		$scope.newAge="";
		
		$scope.calculateAge= function()
		{
			if($scope.patientObj.dob == undefined || $scope.patientObj.dob == null || $scope.patientObj.dob == ''){
				 $scope.age = "";
				   growl.warning("Date is invalid",{
						title : "warning"
					});
					$scope.patientObj.dob = new Date();
				
				   return false;
			}else{
			if($scope.patientObj.dob > new Date())
				{
						       $scope.age = "";
							   growl.warning("DOB should not be greater than today's date",{
									title : "warning"
								});
								$scope.patientObj.dob = new Date();
							
							   return false;
				}else{
					
					var dt = moment($scope.patientObj.dob).format('MM/DD/YYYY');
					var nDt = moment(new Date()).format('MM/DD/YYYY');
					var array = dt.split('/');
					var array1 = nDt.split('/');
					var curday = array1[1];
					var curmon =array1[0];
					var curyear =array1[2];
					var calday =array[1];
					var calmon = array[0];
					var calyear = array[2];
					
						var curd = new Date(curyear,curmon-1,curday);
						var cald = new Date(calyear,calmon-1,calday);
						
				                    var diff =  Date.UTC(curyear,curmon-1,curday,0,0,0) - Date.UTC(calyear,calmon-1,calday,0,0,0);
				    
				                    var dife = $scope.datediff(cald,curd); 
				                   // $("#r1").val(dife.years+" yrs, "+dife.months+" m, & "+dife.days+" d");
				                    $scope.ageYear = dife.years;
							        $scope.ageMonth = dife.months;
							        $scope.ageDay = dife.days;
							        $scope.patientObj.age = $scope.ageYear;
				}
		}
		
		}
		
		
		$scope.getAppointmentPopup = function()
		{
			
			if($scope.patientObj.prefixId =="" ||
					$scope.patientObj.firstName == "" || 	
					$scope.patientObj.mobileNumber =="" ||
					$scope.patientObj.dob =="" ||
					$scope.patientObj.genderId ==""||
					$scope.patientObj.address ==""){
         	  
					return false;
             }
			$scope.slotDetailsList = [];

			$scope.slotCheckTime = moment(new Date()).format("HH:mm");
				
				var data = {
						appointmentDate :moment($scope.appointmentMasterObj.appointmentDate1).format('YYYY-MM-DD'),
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
					};
			$scope.appointmentMasterObj.encounterTypeId = 1;
			$scope.deptWiseAppointment = "N";
			
			if($scope.appointmentMasterObj.encounterTypeId == 1||$scope.appointmentMasterObj.encounterTypeId ==6 || $scope.appointmentMasterObj.encounterTypeId ==9)
				{
				if($scope.deptWiseAppointment == "N"){
						if($scope.appointmentMasterObj.deptId > 0 && $scope.appointmentMasterObj.doctorId > 0)
						{
									data.specialityId = $scope.appointmentMasterObj.deptId;
									data.doctorId = $scope.appointmentMasterObj.doctorId;
									data.slotTypeId = 1;
				
							console.log(data);
							}
							else{
					
								growl.warning("Please Select Department and Doctor",{
									title : WARNING_MSG
								});
								return;
							}
				}
				else
					{
						if($scope.appointmentMasterObj.deptId > 0)
						{
									data.specialityId = $scope.appointmentMasterObj.deptId;
									data.slotTypeId = 4;
				
							console.log(data);
							}
							else{
					
								growl.warning("Please Select Department",{
									title : WARNING_MSG
								});
								return;
							}
					}
				}
			
			
			var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
			
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success") {
									  $scope.slotDetailsList = response.data.listObject;
									  angular.element('#time_slot_modal').modal('show');
							       if($scope.slotDetailsList.length ==  0){
									   growl.warning("Slots not available",{
											title : WARNING_MSG
										});
										return;
								  }
								}

							});
			
		}
		
		//get calling code by using country id start
		$scope.getCallingCode=function(data,type){
			var URI = BASE_URL + ROOT_URL
			+ GETSINGLECOUNTRY;
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						if (response.data.status == "success" && response.data.listObject.length >0) {
							$scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
							$scope.patientObj.whatsappCallingCode = response.data.listObject[0].countryCallingCode;
							
							if(type=="PA"){
								$scope.patientObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
							}else{
								$scope.patientObj.phoneCode = response.data.listObject[0].countryCallingCode;
								if($scope.sameAsLocal == 'Y'){
									$scope.patientObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
								}
							}
						} else {
							//alert("Error!!");
						}
					});
		}
		//get calling code by using country id end
		//get nationality code by using country id start
		$scope.getNationalityByContryId=function(data){
			data.organizationId = $scope.organizationId;
			var URI = BASE_URL + ROOT_URL
			+ "/adt/getNationalityByCountryId";
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						if (response.data.status == "success" && response.data.listObject.length > 0) {
							console.log("nationality List ",response.data.listObject[0]);
							$scope.patientObj.nationalityId = response.data.listObject[0].nationalityId;

						} else {
						}
					});
		}
		//get nationality code by using country id end
		
		// for get state by country ID
		$scope.getStateByCountry = function(id,type) {
//			$scope.startLoader();
			var data = {
				countryId : id
			};								
			
			var dataorg = {
					countryId : id,
					organizationId : $scope.organizationId
				};	
			
			$scope.getCallingCode(dataorg,type);
		
			$scope.getNationalityByContryId(data);
			var URI = BASE_URL + ROOT_URL
					+ GETSTATELISTBYCOUNTRYID;
			
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						
						if (response.data.status == "success"){
							if(type == 'P')
								{
								$scope.stateList = [];
								$scope.stateList = response.data.listObject;
								$scope.patientObj.stateId="";
								$scope.patientObj.districtId="";
								$scope.patientObj.cityId="";
								$scope.patientObj.areaId="";
//								$scope.patientObj.zipCode="";
								/*if($scope.sameAsLocal == 'Y'){
									$scope.patientObj.permanentCountryId = id;
								}*/
								}
							else if(type == 'PA')
								{
								$scope.permanentStateList = [];
								$scope.permanentStateList = response.data.listObject;
								
								$scope.patientObj.permanentStateId="";
								$scope.patientObj.permanentDistrictId="";
								$scope.patientObj.permanentCityId="";
								$scope.patientObj.permanentAreaId="";
								$scope.patientObj.permanentZipCode="";
								}
							else if(type == 'L')
								{
								$scope.stateList = [];
								$scope.stateList = response.data.listObject;
								
								}else if(type == 'LTOP')
								{
									$scope.permanentStateList = [];
									$scope.permanentStateList = response.data.listObject;
									
								}else
								{
								$scope.CompanyStateList = [];
								$scope.CompanyStateList = response.data.listObject;
								}
						}
						$scope.stopLoader();
					});

		}

		$scope.getDistrictByState = function(id,type) {
//			$scope.startLoader();
			var data = {
				stateId : id
			};
			var URI = BASE_URL + ROOT_URL
					+ GETDISTRICTLISTBYSTATEID;
			
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						
						if (response.data.status == "success")
						{
							//$scope.districtList = response.data.listObject;
							if(type == 'P')
							{
							$scope.districtList = [];
							$scope.districtList = response.data.listObject;
							$scope.patientObj.districtId="";
							$scope.patientObj.cityId="";
							$scope.patientObj.areaId="";
//							$scope.patientObj.zipCode="";
							}
							else if(type == 'PA')
							{
							$scope.permanentDistrictList = [];
							$scope.permanentDistrictList = response.data.listObject;
							
							$scope.patientObj.permanentDistrictId="";
							$scope.patientObj.permanentCityId="";
							$scope.patientObj.permanentAreaId="";
							$scope.patientObj.permanentZipCode="";
							}else if(type == 'L')
							{
								$scope.districtList = [];
								$scope.districtList = response.data.listObject;
								
							}
						else  if(type == 'LTOP')
						{
							$scope.permanentDistrictList = [];
							$scope.permanentDistrictList = response.data.listObject;
							
						}else
							{
							$scope.companyDistrictList = [];
							$scope.companyDistrictList = response.data.listObject;
							}
						}
						$scope.stopLoader();
					});
			
		}

		$scope.getCityByDistrict = function(id,type) {
//			$scope.startLoader();
			var data = {
				districtId : id
			};
			var URI = BASE_URL + ROOT_URL
					+ GETCITYLISTBYDISTRICTID;
			
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						if (response.data.status == "success")
							//$scope.cityList = response.data.listObject;
						{
							//$scope.districtList = response.data.listObject;
							if(type == 'P')
							{
							 $scope.cityList = [];
							 $scope.cityList = response.data.listObject;
							 $scope.patientObj.cityId="";
							$scope.patientObj.areaId="";
//							$scope.patientObj.zipCode="";
							}
							else if(type == 'PA')
							{
							$scope.permanentCityList = [];
							$scope.permanentCityList = response.data.listObject;
							
							$scope.patientObj.permanentCityId="";
							$scope.patientObj.permanentAreaId="";
							$scope.patientObj.permanentZipCode="";
							}else if(type == 'L')
							{
								 $scope.cityList = [];
								 $scope.cityList = response.data.listObject;
								
							}else if(type == 'LTOP')
							{
								$scope.permanentCityList = [];
								$scope.permanentCityList = response.data.listObject;
							}	
						else
							{
							$scope.CompanyCityList = [];
							$scope.CompanyCityList = response.data.listObject;
							}
						}
						$scope.stopLoader();
					});
		}
		$scope.getAreaByCity = function(id,type) {
//			$scope.startLoader();
			var data = {
				cityId : id
			};
			var URI = BASE_URL + ROOT_URL
					+ GETAREALISTBYCITY;
			
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						if (response.data.status == "success")
							//$scope.areaList = response.data.listObject;
						{
							if(type == 'P')
							{
							$scope.areaList = [];
							 $scope.areaList = response.data.listObject;
							console.log($scope.areaList)
							$scope.patientObj.areaId="";
//							$scope.patientObj.zipCode="";
							}
							else if(type == 'PA')
							{
							$scope.permanentAreaList = [];
							$scope.permanentAreaList = response.data.listObject;
							
							$scope.patientObj.permanentAreaId="";
							$scope.patientObj.permanentZipCode="";
							}else if(type == 'L')
							{
								$scope.areaList = [];
								 $scope.areaList = response.data.listObject;
								console.log($scope.areaList)
							}else if(type == 'LTOP')
							{
								$scope.permanentAreaList = [];
								$scope.permanentAreaList = response.data.listObject;
							}	
						else
							{
							$scope.CompanyAreaList = [];
							$scope.CompanyAreaList = response.data.listObject;
							}
						}
						$scope.stopLoader();
					});
		}
		
	
    
		$scope.getDataByPinCode = function(){
			
			if($scope.patientObj.zipCode != null && $scope.patientObj.zipCode != undefined && $scope.patientObj.zipCode!="" && $scope.areaNotExist=='N' ){
//				$rootScope.startLoader();
				var zipC = $scope.patientObj.zipCode;
				
			var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
			BillingGenericService
					.serviceAction(METHOD_GET,
							URI, "",
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								if (response.data.status == "success"){
									
									if(response.data.object.countryId != '' ){
										$scope.patientObj.countryId = response.data.object.countryId;
									}
									
									if(response.data.object.stateId != ''){
										$scope.getStateByCountry(response.data.object.countryId,'L');
										$scope.patientObj.stateId = response.data.object.stateId;
									}
									
									if(response.data.object.districtId != ''){
										$scope.getDistrictByState(response.data.object.stateId,'L');
										$scope.patientObj.districtId = response.data.object.districtId;
									}
									
									if(response.data.object.cityId != '')
										$scope.getCityByDistrict(response.data.object.districtId,'L');
										$scope.patientObj.cityId = response.data.object.cityId;
									
									if(response.data.object.areaId != ''){
										$scope.getAreaByCity(response.data.object.cityId,'L');
										$scope.patientObj.areaId = response.data.object.areaId;
									}
									
									$rootScope.stopLoader();
								}else{
									$rootScope.stopLoader();
								}
							});
			}
			
		}
		
		$scope.selectGender = function(id)
		{
			if(id > 0){
			var genId = $.grep($scope.prefixList, function (doc) {
	            return doc.prefixId == id;
	        })[0].id;
			
			$scope.patientObj.genderId = genId;
			}
			else
				{
				$scope.patientObj.prefixId = '';
				$scope.patientObj.genderId = '';
				}
			
		}
		
		 $scope.copyContact = 'N';

	        $scope.checkDateIsBefore = function(slobObj)
	    	{
	    	     var endTime = moment(slobObj.originalFromTime, 'hh:mm');
	    	     var startTempTime = moment().format("HH:mm");
	    	     var startTime = moment(startTempTime, 'hh:mm');
	    	     
	    	 	   if ($scope.slotId == slobObj.slotId){
		    		 	return 'bg-green cursor-pointer day-slot-div';
		    		 }
	    		if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > 0 && slobObj.slotPatientDetailsListsSize < slobObj.allowedOverBookingSlots)
	    			{
	    				return 'bg-orange cursor-pointer day-slot-div';
	    			}
	    		if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize == '0')
	    		{
	    			return 'bg-success cursor-pointer day-slot-div';
	    		}
	    		
	    	}
	        
	        
	    	
	    	//Function For Set Value On Popup
	    	$scope.getSlotDetailsPopup = function(item) {
	    		
	    		$scope.encounterDetailsObj = {};
	    		//$scope.regiType = "1";
//	    		$rootScope.startLoader();
	    		var data = {
	    				specialityId : $scope.appointmentMasterObj.deptId,
	    				doctorId : $scope.appointmentMasterObj.doctorId,
	    				patientId : $scope.appointmentMasterObj.patientId,
	    				organizationId : $scope.organizationId,
	    				unitId : $scope.unitId,
	    				slotId : item.slotId,
	    				appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
	    				appointmentDate : item.appointmentDate2,
	    				
	    			};

	    		console.log(data);
	    		$scope.appointmentMasterObj.patientId = 0;
	    		$scope.encounterDetailsObj.appointmentId = 0;
	    		if($scope.encounterDetailsObj.appointmentId > 0){

	    			$scope.doctorName = item.doctorName;
	    			$scope.doctorId = item.doctorId;
	    			$scope.specialityname = item.specialityName;
	    			$scope.fromTime = item.fromTimeString2;
	    			$scope.toTime = item.toTimeString2;
	    			$scope.appointmentDate = item.appointmentDate;
	    			$scope.slotId = item.slotId;
	    			
	    			if(item.slotStatusId==4 || item.slotStatusId==6)
	    			{
	    				$scope.slotStatusId = 6;
	    			}else
	    				$scope.slotStatusId = 4;
	    			
	    			$scope.priorityId  = 1;
	    			$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
	    			//$scope.appointmentMasterObj.encounterTime = item.fromTimeString;
	    			$scope.appointmentMasterObj.encounterTime = item.originalFromTime;
	    			//$scope.appointmentMasterObj.appoitmentDate = item.appointmentDate;
	    			 $scope.encounterDetailsObj.appoitmentDate = item.appointmentDate;
	    			$rootScope.stopLoader();
	    			
	    	}else{
	    		
	    		if( $scope.appointmentMasterObj.patientId > 0){}else{
	    		$scope.doctorName = item.doctorName;
	    		$scope.doctorId = item.doctorId;
	    		$scope.specialityname = item.specialityName;
	    		$scope.fromTime = item.fromTimeString2;
	    		$scope.toTime = item.toTimeString2;
	    		$scope.appointmentDate = item.appointmentDate;
	    		$scope.slotId = item.slotId;
	    		
	    		if(item.slotStatusId==4 || item.slotStatusId==6)
	    		{
	    			$scope.slotStatusId = 6;
	    		}else
	    			$scope.slotStatusId = 4;
	    		
	    		$scope.priorityId  = 1;
	    		$scope.bookedpatientLength = item.slotPatientDetailsListsSize;
	    		//$scope.appointmentMasterObj.encounterTime = item.fromTimeString;
	    		$scope.appointmentMasterObj.encounterTime = item.originalFromTime;
	    		//$scope.appointmentMasterObj.appoitmentDate = item.appointmentDate;
	    		 $scope.encounterDetailsObj.appoitmentDate = item.appointmentDate;
	    		$rootScope.stopLoader();
	    	}
	    	
	    	}
	    	}
	    	
	    	
			$scope.copyPatientObject = function(copyContact){
				if(copyContact  == 'Y'){
					$scope.kinDetails.firstName = $scope.patientObj.firstName;
					$scope.kinDetails.middleName = $scope.patientObj.middleName;
					$scope.kinDetails.lastName =  $scope.patientObj.lastName;
					$scope.kinDetails.address =  $scope.patientObj.address;
					$scope.kinDetails.address1 = $scope.patientObj.address2;
					$scope.kinDetails.address2 = $scope.patientObj.address3;
					$scope.kinDetails.mobileNo = $scope.patientObj.mobileNumber;
					$scope.kinDetails.countryId = $scope.patientObj.countryId;
					$scope.kinDetails.stateId = $scope.patientObj.stateId;
					$scope.kinDetails.cityId = $scope.patientObj.cityId;
					$scope.kinDetails.email = $scope.patientObj.email;
					$scope.kinDetails.postCode = $scope.patientObj.zipCode;
				}else{
					$scope.kinDetails.firstName = "";
					$scope.kinDetails.middleName = "";
					$scope.kinDetails.lastName =  "";
					$scope.kinDetails.address = "";
					$scope.kinDetails.address1 = "";
					$scope.kinDetails.address2 = "";
					$scope.kinDetails.mobileNo = "";
					$scope.kinDetails.countryId = "";
					$scope.kinDetails.stateId = "";
					$scope.kinDetails.cityId = "";
					$scope.kinDetails.email = "";
					$scope.kinDetails.postCode = "";
				}
			}

			
			$scope.saveAppointment = function()
			{
				

				//save patient Registration object
            if($scope.patientObj.prefixId ==""){
         	   growl.warning("Please enter prefix",{
						title : WARNING_MSG
					});
					return false;
             }else if($scope.patientObj.firstName == ""){
             	  growl.warning("Please enter first name",{
						title : WARNING_MSG
					});
					return false;
					
				}else if($scope.patientObj.mobileNumber ==""){
					 growl.warning("Please enter mobile number",{
	  						title : WARNING_MSG
	  					});
	  					return false;
				}else if($scope.patientObj.dob ==""){
					growl.warning("Please enter date of birth ",{
						title : WARNING_MSG
					});
					return false;
					
				}else if($scope.patientObj.genderId ==""){
					growl.warning("Please enter gender",{
						title : WARNING_MSG
					});
					return false;
					
				}else if($scope.patientObj.address ==""){
					growl.warning("Please enter Address",{
						title : WARNING_MSG
					});
					return false;
				}
           
				$scope.kinDetailsList =[];
				$scope.kinDetailsList[0]=(angular.copy($scope.kinDetails));
				$scope.test =[];
				$scope.test[0]=(angular.copy($scope.patientObj));
				$scope.finalString = "";
				$scope.address1 = "";
				$scope.kinName = "";
				//CONCATE address
				angular.forEach($scope.test,function(value,index){
            	   var String = value.address+","+value.address2+","+value.address3;
            	   $scope.address = $scope.finalString.concat(String);
				});
				angular.forEach($scope.kinDetailsList,function(value,index){
	               	   var String = value.address+","+value.address1+","+value.address2;
	               	   var kinName = value.firstName+" "+value.middleName+" "+value.lastName;
	               	   $scope.address1 = $scope.address1.concat(String);
	               	   $scope.kinName = $scope.kinName.concat(kinName);
					});
				//concate address 
				$scope.patientObj.address =$scope.address;
				$scope.kinDetails.address =$scope.address1;
				$scope.kinDetails.kinName =$scope.kinName;
				
				$scope.kinDetailsList =[];
				$scope.kinDetailsList[0]=(angular.copy($scope.kinDetails));
				
			
				$scope.currentDate = new Date();
				$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
				$scope.currentDateAppDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
		        $scope.appointmentMasterObj.patientClassId = null;
		        $scope.appointmentMasterObj.appointmentObject  = null;
			var sponserList = [];
			angular.forEach($scope.paymentEntitleMentList, function (value, key) {
				
				var newArr = {};
				newArr.organizationId = $scope.organizationId ;
				newArr.unitId = $scope.unitId;
				newArr.visitTypeId = 1;
				newArr.paymentEntitlementId =value.paymentEntitlementId;
				newArr.payeeId = value.companyId;
				newArr.priorityId = value.priority;
				newArr.createdBy =$scope.userId;
				newArr.createdDate = $scope.currentDate;
				newArr.updatedBy = $scope.userId;
				newArr.updatedDate = $scope.currentDate;
				newArr.status = 'A';
				newArr.tariffId =value.tariffId;
				newArr.contractId =value.contractId;
				newArr.insuranceNumber = value.insuranceNumber;
				newArr.insuredAmount = value.insuredAmount;
				newArr.insuranceId = value.insuranceId;
				newArr.validityTo = value.endDate;
				newArr.validityFrom = value.startDate;
				newArr.employeeNoManual = value.employeeNoManual;
				
				
				if(value.companyId != $scope.selfPayeeIdCookies && value.priority == 1)
			    {
			     $scope.appointmentMasterObj.contractId = value.contractId;
			     $scope.appointmentMasterObj.companyId = value.companyId;
			    }
				  if($scope.corEmpVerificationFlag && $scope.corEmpObj.typeOfCoEmp==3){
					  newArr.corporateEmpNote=value.corporateEmpNote;
				  }
				
				sponserList.push(newArr);
			});
			 $scope.appointmentMasterObj.patientSponsorDetailsDtosList = sponserList;
			 $scope.appointmentMasterObj.slotId =$scope.slotId;
			
			 //for Hospital Visit
			

				/*if(this.slotId == '' && $scope.appointmentMasterObj.encounterTypeId != 5)
					{
					//$scope.tab=1;
					$scope.secondTabLevel = 1 ;
					growl.warning(" Please select the time slot ",{
						title : WARNING_MSG
					});
					return;
					}
				else
					{*/
						var dataAppointmentObj = {
								remark : $scope.appointmentMasterObj.remark,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								createdDate : $scope.currentDate,
								updatedDate : $scope.currentDate,
								createdBy : $scope.userId,
								updatedBy : $scope.userId,
								status : 'A',
								appointmentTypeId : $scope.appointmentMasterObj.encounterTypeId,
								priorityId : $scope.priorityId,
								firstName : $scope.patientObj.firstName,
							    mobileNumber :$scope.patientObj.mobileNumber,
							    generateToken : 'Y',
							    appointmentDate : moment($scope.appointmentMasterObj.appointmentDate1).format('YYYY-MM-DD'),
							    isDirectEncounter : 'Y',
								ehcPackageId : $scope.EHCpackageId
								//allowedOverBookingSlots : $scope.allowedOverBookingSlots,
							}
						
						if($scope.appointmentMasterObj.encounterTypeId != 5)
							{
							   dataAppointmentObj.slotId = $scope.slotId;
							   dataAppointmentObj.appointmentDate2 = moment($scope.appointmentMasterObj.appointmentDate1).format('DD/MM/YYYY');
							   dataAppointmentObj.fromTimeString = $scope.fromTime;
							   dataAppointmentObj.slotStatusId = $scope.slotStatusId;
							   dataAppointmentObj.isFromEncounter = 'Y';
							   dataAppointmentObj.doctorId = $scope.appointmentMasterObj.doctorId;
							}
						
						if($scope.appointmentMasterObj.encounterTypeId == 3)
						{
							
							if($scope.EHCpackageId == 0 || $scope.EHCpackageId == null)
							{
								//$scope.tab=1;
								$scope.secondTabLevel = 1 ;
								growl.warning("Please Select Package",{
									title : ERROR_MSG
								});
								return;
							}else
								{
								 dataAppointmentObj.ehcPackageId = $scope.EHCpackageId;
								}
						}
						$scope.appointmentMasterObj.appointmentObject  = dataAppointmentObj;
					//}
			
			    $scope.appointmentMasterObj.deptWiseAppointment = $scope.deptWiseAppointment;
			    $scope.appointmentMasterObj.isNursingAssessmentRequiredForSpeciality = $scope.isNursingAssessmentRequired;
				$scope.appointmentMasterObj.appoitmentDate =  moment($scope.appointmentMasterObj.appointmentDate1).format('YYYY-MM-DD');
				$scope.appointmentMasterObj.appoitmentTime = $scope.encounterDetailsObj.fromTimeString;
			    
			if($scope.appointmentMasterObj.patientId == '' || $scope.appointmentMasterObj.patientId == null || $scope.appointmentMasterObj.patientId == 0){
				var flag=false;
				$scope.currentDate = new Date();
				$scope.currentDate = moment($scope.currentDate)
						.format('DD-MM-YYYY HH:mm:ss');
				
					$scope.patientObj.createdDate = $scope.currentDate;
					$scope.patientObj.updatedDate = $scope.currentDate;
					$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
					$scope.patientObj.middleName =  ($scope.patientObj.middleName == null || $scope.patientObj.middleName.trim() == "" || $scope.patientObj.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.middleName));
					$scope.patientObj.lastName =   ($scope.patientObj.lastName == null || $scope.patientObj.lastName.trim() == "" || $scope.patientObj.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.lastName));
					$scope.patientObj.dob = moment($scope.dob).format('DD-MM-YYYY HH:mm:ss');
				    $scope.patientObj.identificationTypeId=null;
					$scope.patientObj.aliseName='';
					$scope.patientObj.permanentAddress = $scope.patientObj.address;
					$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
					$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
					$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
					$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
					$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
					$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
					$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
					$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
					$scope.patientObj.identificationTypeId = null;
					$scope.patientObjCopy = angular.copy($scope.patientObj);
				    console.log($scope.patientObj);
				   $scope.appointmentMasterObj.patientRegistrationDto = $scope.patientObjCopy;
			}
			angular.forEach($scope.kinDetailsList,function(value,key){
				value.multidelete("firstName","middleName","lastName","address1","address2","ISDCode","STDCode","email","phoneNumber");
			});
			
			$scope.appointmentMasterObj.listKinDetailsDto = $scope.kinDetailsList;
			console.log('Encounter obj',$scope.appointmentMasterObj);
			$scope.appointmentMasterObj.multidelete("appointmentDate1");
			$scope.appointmentMasterObj.patientRegistrationDto.multidelete("ISDCode","STDCode","address2","address3","phoneNo");
			$scope.appointmentMasterObj.fromWeb = true;
			
			var appointmentMasterObjCopy = angular.copy($scope.appointmentMasterObj);
			console.log(appointmentMasterObjCopy);
			
			//return false;
//			$rootScope.startLoader();

			var URI = BASE_URL + ROOT_URL + "/api/opd/encounter";
			BillingGenericService.serviceAction(METHOD_POST, URI,appointmentMasterObjCopy, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						if (response.data.status == "success") {
							$scope.init();
							$rootScope.stopLoader();
							$scope.serviceOrderList = [];
							$scope.totalServiceAmt = 0;
							$scope.patientList=[];
							$scope.secondTabLevel = 1 ;
							
							$scope.patientObj = {

									prefixId : '',
									firstName : '',
									middleName : '',
									lastName : '',
									genderId : '',
									countryCallingCode : '',
									email : '',
									nationalityId : '',
									raceId : '',
									maritalStatusId : '',
									mobileNumber : '',
									dob : '',
									identificationTypeId : '',
									identificationNumber : '',
									isVip : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
									aliseName:'',
									identificationExpiryDate:'',
									occupationId:'',
									registrationTypeId:5,
									previousId:'',
									patientId:0,
									patientDetailsId:'',
									whatsappCallingCode : '',
									whatsappNo:'',
									skypeId:'',
									address : '',
									countryId : '',
									stateId : '',
									districtId : '',
									cityId :'',
									areaId : '',
									zipCode : '',
									phoneCode : '',
									phoneNumber : '',
									permanentAddress:'',
									permanentCountryId:'',
									permanentStateId:'',
									permanentDistrictId:'',
									permanentCityId:'',
									permanentAreaId:'',
									permanentZipCode:'',
									permanentPhoneCode:'',
									permanentPhoneNumber:'',
								};
							 $scope.patientAdmittedFlag = false;
								$scope.ageDay = 0;
								$scope.ageMonth = 0;
								$scope.ageYear = 0;
								
								if($scope.unitCountryId > 0)
								{
								  $scope.patientObj.countryId  = $scope.unitCountryId;
								  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
								  $scope.patientObj.stateId  =  $scope.unitStateId;
								  $scope.patientObj.districtId = $scope.unitDistrictId;
								  $scope.patientObj.cityId = $scope.unitCityId;
								  $scope.patientObj.areaId = $scope.unitAreaId;
								  $scope.patientObj.zipCode = $scope.unitZipCode;
								  $scope.getStateByCountry($scope.patientObj.countryId,'L');
								  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
								  $scope.getDistrictByState($scope.patientObj.stateId,'L');
								  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
								  $scope.getAreaByCity($scope.patientObj.cityId,'L');
								}
						}else if(response.data.code == 409)	{
							$scope.init();
							$scope.serviceOrderList = [];
							$scope.totalServiceAmt = 0;
							$scope.patientList=[];
							$scope.secondTabLevel = 1 ;
							
							$scope.patientObj = {

									prefixId : '',
									firstName : '',
									middleName : '',
									lastName : '',
									genderId : '',
									countryCallingCode : '',
									email : '',
									nationalityId : '',
									raceId : '',
									maritalStatusId : '',
									mobileNumber : '',
									dob : '',
									identificationTypeId : '',
									identificationNumber : '',
									isVip : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
									aliseName:'',
									identificationExpiryDate:'',
									occupationId:'',
									registrationTypeId:5,
									previousId:'',
									patientId:0,
									patientDetailsId:'',
									whatsappCallingCode : '',
									whatsappNo:'',
									skypeId:'',
									address : '',
									countryId : '',
									stateId : '',
									districtId : '',
									cityId :'',
									areaId : '',
									zipCode : '',
									phoneCode : '',
									phoneNumber : '',
									permanentAddress:'',
									permanentCountryId:'',
									permanentStateId:'',
									permanentDistrictId:'',
									permanentCityId:'',
									permanentAreaId:'',
									permanentZipCode:'',
									permanentPhoneCode:'',
									permanentPhoneNumber:'',
								};
							 $scope.patientAdmittedFlag = false;
								$scope.ageDay = 0;
								$scope.ageMonth = 0;
								$scope.ageYear = 0;
								
								if($scope.unitCountryId > 0)
								{
								  $scope.patientObj.countryId  = $scope.unitCountryId;
								  $scope.patientObj.permanentCountryId  =  $scope.patientObj.countryId;
								  $scope.patientObj.stateId  =  $scope.unitStateId;
								  $scope.patientObj.districtId = $scope.unitDistrictId;
								  $scope.patientObj.cityId = $scope.unitCityId;
								  $scope.patientObj.areaId = $scope.unitAreaId;
								  $scope.patientObj.zipCode = $scope.unitZipCode;
								  $scope.getStateByCountry($scope.patientObj.countryId,'L');
								  $scope.getStateByCountry( $scope.patientObj.permanentCountryId,'LTOP');
								  $scope.getDistrictByState($scope.patientObj.stateId,'L');
								  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
								  $scope.getAreaByCity($scope.patientObj.cityId,'L');
								}
						
							
						}	
					
					});
			}
			
			
			
			$scope.saveKinDetails = function(obj) {
				
				$scope.currentDate = new Date();
				$scope.currentDate = moment($scope.currentDate)
						.format('DD-MM-YYYY HH:mm:ss');

				var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						createdBy:1,
						createdDate : $scope.currentDate,
						patientId: obj.patientId,
						tPatientId : null,
						dPatientId : null
				}

				
				
				$scope.kinDetailsList =[];
				$scope.kinDetailsList[0]=(angular.copy($scope.kinDetails));
				$scope.kinName = "";
				$scope.address1="";
				
				angular.forEach($scope.kinDetailsList,function(value,index){
	               	   var String = value.address+","+value.address1+","+value.address2;
	               	   var kinName = value.firstName+" "+value.middleName+" "+value.lastName;
	               	   $scope.address1 = $scope.address1.concat(String);
	               	   $scope.kinName = $scope.kinName.concat(kinName);
					});
				//concate address 
				$scope.patientObj.address =$scope.address;
				$scope.kinDetails.address =$scope.address1;
				$scope.kinDetails.kinName =$scope.kinName;
				
				$scope.kinDetailsList =[];
				$scope.kinDetailsList[0]=(angular.copy($scope.kinDetails));
				
				angular.forEach($scope.kinDetailsList,function(value,key){
					value.multidelete("firstName","middleName","lastName","address1","address2","ISDCode","STDCode","email","phoneNumber");
			    	});
				data.kinArray = $scope.kinDetailsList;
				
				console.log(data);

				var URI = BASE_URL + ROOT_URL
						+ SAVEKINDEATILSFORMORTUARYALLOCATION;
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {

									if (response.data.status == "success") {
									
										$rootScope.stopLoader();
										
									} else {
										growl
												.error(
														'Something went wrong...!!',
														{
															title : 'Error!'
														});
									}

								});
					}
			
			
			
			//Save Appointment Schduling
			$scope.saveAppointmentScheduleNew = function() {
				$rootScope.startLoader();
				//save patient Registration object
	               if($scope.patientObj.prefixId ==""){
	            	   growl.warning("Please enter prefix",{
							title : WARNING_MSG
						});
						return false;
	                }else if($scope.patientObj.firstName == ""){
	                	  growl.warning("Please enter first name",{
	  						title : WARNING_MSG
	  					});
	  					return false;
						
					}else if($scope.patientObj.mobileNumber ==""){
						 growl.warning("Please enter mobile number",{
		  						title : WARNING_MSG
		  					});
		  					return false;
					}else if($scope.patientObj.dob ==""){
						growl.warning("Please enter date of birth ",{
	  						title : WARNING_MSG
	  					});
	  					return false;
						
					}else if($scope.patientObj.genderId ==""){
						growl.warning("Please enter gender",{
	  						title : WARNING_MSG
	  					});
	  					return false;
						
					}else if($scope.patientObj.address ==""){
						growl.warning("Please enter Address",{
	  						title : WARNING_MSG
	  					});
	  					return false;
					}
	               
//					$rootScope.startLoaderPopup();
					$scope.btnDisabled = true;
					$scope.currentDate = new Date();
					$scope.currentDate = moment(
							$scope.currentDate).format(
							'DD-MM-YYYY HH:mm:ss');

					var data = {
						slotId : $scope.slotId,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						createdDate : $scope.currentDate,
						updatedDate : $scope.currentDate,
						createdBy :$scope.userId,
						updatedBy :$scope.userId,
						status : 'A',
						appointmentTypeId :$scope.appointmentMasterObj.encounterTypeId,
						slotStatusId : $scope.slotStatusId,
						priorityId : $scope.priorityId,
//						allowedOverBookingSlots : $scope.allowedOverBookingSlots,
						firstName : $scope.patientObj.firstName,
					    mobileNumber :$scope.patientObj.mobileNumber,
					    doctorName :$scope.doctorName,
					    fromTimeString : $scope.fromTime,
					    appointmentDate2 : $scope.appointmentDate,
					    doctorId : $scope.doctorId,
					    specialityId : ($scope.appointmentMasterObj.deptId==undefined) ? $scope.specialityId : $scope.appointmentMasterObj.deptId,//ganesh
					    specialityName: $scope.spName 
					}
					
							data.prefixId = $scope.patientObj.prefixId;
							data.firstName = $scope.patientObj.firstName;
							data.middleName = $scope.patientObj.middleName;
							data.lastName =$scope.patientObj.lastName;
							data.dob =moment($scope.patientObj.dob).format('DD-MM-YYYY HH:mm:ss');
							data.genderId =$scope.patientObj.genderId;
							data.mobileNumber =$scope.patientObj.mobileNumber;
							data.email =$scope.patientObj.email;
							data.registrationTypeId = 5;
							data.patientId = 0;

						var URI = BASE_URL + ROOT_URL
								+ SCHEDULEAPPOINTMENT;

					console.log(data);
					
					GenericService.serviceAction("POST", URI, data).then(
							function(response) {
										if (response.data.status == "success") {
											$rootScope.stopLoader();
											if($scope.kinDetails.firstName != undefined){
												$scope.saveKinDetails(response.data.object);
											}
											growl.success(response.data.message,
    												{
    													title : 'Success!'
    												});
											$scope.copyContact = 'N';
											$scope.init();
											$rootScope.stopLoader();
											$scope.serviceOrderList = [];
											$scope.totalServiceAmt = 0;
											$scope.patientList=[];
											$scope.secondTabLevel = 1 ;
											
											$scope.patientObj = {

													prefixId : '',
													firstName : '',
													middleName : '',
													lastName : '',
													genderId : '',
													countryCallingCode : '',
													email : '',
													nationalityId : '',
													raceId : '',
													maritalStatusId : '',
													mobileNumber : '',
													dob : '',
													identificationTypeId : '',
													identificationNumber : '',
													isVip : '',
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													updatedBy : $scope.userId,
													createdBy : $scope.userId,
													status : 'A',
													patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
													aliseName:'',
													identificationExpiryDate:'',
													occupationId:'',
													registrationTypeId:5,
													previousId:'',
													patientId:0,
													patientDetailsId:'',
													whatsappCallingCode : '',
													whatsappNo:'',
													skypeId:'',
													address : '',
													countryId : '',
													stateId : '',
													districtId : '',
													cityId :'',
													areaId : '',
													zipCode : '',
													phoneCode : '',
													phoneNumber : '',
													permanentAddress:'',
													permanentCountryId:'',
													permanentStateId:'',
													permanentDistrictId:'',
													permanentCityId:'',
													permanentAreaId:'',
													permanentZipCode:'',
													permanentPhoneCode:'',
													permanentPhoneNumber:'',
												};
											 $scope.patientAdmittedFlag = false;
												$scope.ageDay = 0;
												$scope.ageMonth = 0;
												$scope.ageYear = 0;
												$rootScope.stopLoader();
										}else if(response.data.code == 409)	{
											$scope.init();
											$scope.serviceOrderList = [];
											$scope.totalServiceAmt = 0;
											$scope.patientList=[];
											$scope.secondTabLevel = 1 ;
											
											$scope.patientObj = {

													prefixId : '',
													firstName : '',
													middleName : '',
													lastName : '',
													genderId : '',
													countryCallingCode : '',
													email : '',
													nationalityId : '',
													raceId : '',
													maritalStatusId : '',
													mobileNumber : '',
													dob : '',
													identificationTypeId : '',
													identificationNumber : '',
													isVip : '',
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													updatedBy : $scope.userId,
													createdBy : $scope.userId,
													status : 'A',
													patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
													aliseName:'',
													identificationExpiryDate:'',
													occupationId:'',
													registrationTypeId:5,
													previousId:'',
													patientId:0,
													patientDetailsId:'',
													whatsappCallingCode : '',
													whatsappNo:'',
													skypeId:'',
													address : '',
													countryId : '',
													stateId : '',
													districtId : '',
													cityId :'',
													areaId : '',
													zipCode : '',
													phoneCode : '',
													phoneNumber : '',
													permanentAddress:'',
													permanentCountryId:'',
													permanentStateId:'',
													permanentDistrictId:'',
													permanentCityId:'',
													permanentAreaId:'',
													permanentZipCode:'',
													permanentPhoneCode:'',
													permanentPhoneNumber:'',
												};
											 $scope.patientAdmittedFlag = false;
												$scope.ageDay = 0;
												$scope.ageMonth = 0;
												$scope.ageYear = 0;
												
												$rootScope.stopLoader();
											
										}	
									
									});

			}
		
	}catch(e){
		console.log(e.message);
	}
	       
}]);