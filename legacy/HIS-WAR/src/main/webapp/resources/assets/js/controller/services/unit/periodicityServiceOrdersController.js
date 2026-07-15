(function(){
	'use strict'
	
	function periodicityServOrderController($scope, $rootScope, BillingGenericService, growl, $cookies, $state,GenericService){
		var self = this;
		
		function init(){
			$rootScope.loginpage = true;
			self.model = {
			        fromDateOptions: {
			          formatYear: 'yyyy',
			          showWeeks: false,
			          minDate: new Date()
			        },
			        toDateOptions:{
			        	formatYear: 'yyyy',
			        	showWeeks: false
			        }
			   }
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}
			
			
			var appConfigObject = $cookies.getObject('cookieObjectAppConfig');
			$scope.radioLogySpecialityId = appConfigObject.radiologySpecialityId;
			
			$scope.roleName = appConfigObject.roleName;
			$scope.nurseRole = appConfigObject.nurseRole;	 
			$scope.doctorRole = appConfigObject.doctorRole;
			$scope.isShowPatientByBed=appConfigObject.isShowPatientByBed;
			
			$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
			$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
			
			$scope.equipmentHideShow = false;
			
			$scope.wardList = [];
			var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
			 if(nursingStationsWardsList == undefined){
				 growl.warning(
							'No ward mapped with selected nursing station !!',
							{
								title : 'Success!'
							});
			 }
			 else
			  {
			   console.log('nursingStationsWardsList',nursingStationsWardsList);
			   $scope.wardList = nursingStationsWardsList; 
			   /*console.log('nursingStationCookie',nursingStationCookie);
			   $scope.nursingStationDesc=nursingStationCookie;
			   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
				console.log("departmentNursingStationMapperDtoslist",$scope.departmentNursingStationMapperDtoslist);*/
			  }
			 
			 $scope.nurseBedList = [];
				var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
				 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y' ){
					 growl.warning(
								'No bed mapped with nurse !!',
								{
									title : 'Success!'
								});
				 }
				 else
				  {
				   $scope.nurseBedList =mappedNurseBedList; 
				  console.log('Nurse Bed List',$scope.nurseBedList);
				  }
			 
			 
				 $scope.openDatePicker = function() {
						$scope.datepicker.opened = true;
					};

					$scope.datepicker = {
						opened : false
					};
			
			self.model.unitId = cookieObject.unitId;
			self.model.organizationId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			self.model.periodicityServ = {fromDate: new Date(),unitId:self.model.unitId,createdBy:self.model.userId},
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
		    $scope.searchPatient = searchPatient;
			$scope.getPatientDetailsByIndex = getPatientDetailsByIndex;
		   // self.model.selectPatient = selectPatient;
		    self.model.selectPService = selectPService;
		    self.model.searchPServices = searchPServices;
		    self.model.initClockPicker = initClockPicker;
		    self.model.addPeriodicityServOrder = addPeriodicityServOrder;
		    self.model.removePeriodicityServOrder = removePeriodicityServOrder;
		    self.model.editPeriodicityServOrder = editPeriodicityServOrder;
		    self.model.orderPeriodicityService = orderPeriodicityService;
		    self.model.getPeriodicityServicesByAdmissionId = getPeriodicityServicesByAdmissionId;
		    self.model.periodicityServOrdersList = [];
		    self.model.editFlag = false;
		    self.model.editIndex = null;
		    $scope.admissionType = '2';
		    self.model.toDateOptions.minDate = new Date(self.model.periodicityServ.fromDate);
		    
		    $scope.patientSearchObj = {
					"uhIdNumber"      : "",
					"patientName"     : "",
					"genderId"        : "",
					"mobileNo"        : "",
					"birthDate"       : "",
					"identificationNo":"",
					"patientType"	  :"1",
					"unitId"          :self.model.unitId,
					"organizationId"  :self.model.organizationId
			}
		    
		}
		
		 var OpenFromDate = function () {
		      self.model.fromDateOpened = true;
		    }
		 var OpenToDate = function () {
		      self.model.toDateOpened = true;
		    }
		 
		 
		 var searchPatient = function(patientsearchObj){

		     $scope.admissionDetails = null;
             self.model.periodicityServ.uhidNumber='';
             self.model.periodicityServ.patientName ='';//+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
             self.model.periodicityServ.visitTypeId ='';
             self.model.periodicityServ.wardName ='';
             self.model.periodicityServ.bedNumber ='';
             self.model.periodicityServ.admissionNumber ='';
         	self.model.periodicityServ.serviceName='';
			self.model.periodicityServ.fromTime='';
			self.model.periodicityServ.toDate='';
			self.model.periodicityServ.toTime='';
             self.model.periodicityServOrdersList = [];
			 if($scope.nurseRole == 1 && $scope.wardList.length > 0){
			 
				$rootScope.startLoader();
				patientsearchObj.bedList=[];
				if ($scope.isShowPatientByBed == 'Y') {
					if($scope.nurseBedList.length>0){
						patientsearchObj.bedList=$scope.nurseBedList;
					}else{
						$scope.wardList=[];	
					}
				}
				if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
				{
					growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
					$scope.patientList = [];
					$rootScope.stopLoader();
					return false;
				}	
				if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
					var dt = new Date(patientsearchObj.birthDate);
					patientsearchObj.birthDate= (dt.getMonth() + 1) + "-" + dt.getDate() + "-" +  dt.getFullYear();
				} 
				patientsearchObj.visitTypeId =null;
				patientsearchObj.wardList = $scope.wardList;
				var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaForIPD";
				//var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
				BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					$scope.patientList = response.data.listObject;
					if(response.data.listObject==null){
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$scope.patientSearchObj.birthDate="";
						$scope.patientList=[];
						$rootScope.stopLoader();
						return false;
					}
					$scope.patientAdmissionList = [];
					$scope.patientDetails ={};
					$scope.admissionDetails = {};
					$scope.patientSearchObj.birthDate="";
				});
			 }
			}
		 

			var getPatientDetailsByIndex = function(index){
				$rootScope.startLoader();
			    
				/*$scope.patientDetails.age=($scope.patientList[index].age);
				$scope.patientDetails.patientName=($scope.patientList[index].patientName);
				$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
				$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
				$scope.patientDetails.patientId=($scope.patientList[index].patientId);
				$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
				$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
				
				$scope.patientFullName=($scope.patientList[index].patientName)+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
				$scope.uhidNum=($scope.patientList[index].uhIdNumber);*/
				self.model.periodicityServ.uhidNumber=($scope.patientList[index].uhIdNumber);
				self.model.periodicityServ.patientName = $scope.patientList[index].patientName;//+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
				$scope.admissionType = $scope.patientList[index].visitTypeId;
				self.model.periodicityServ.visitTypeId = $scope.patientList[index].visitTypeId;
				self.model.periodicityServ.patientId = $scope.patientList[index].patientId;
				$scope.admissionId = ($scope.patientList[index].admissionId);
				
				self.model.periodicityServ.serviceName='';
				self.model.periodicityServ.fromTime='';
				self.model.periodicityServ.toDate='';
				self.model.periodicityServ.toTime='';
				self.model.periodicityServOrdersList = [];
				 
				
				//get admission details by id
				if($scope.admissionId != null && typeof($scope.admissionId) != "undefined")
				{
					$scope.loadingServiceFlag = true;
					$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
					BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.admissionDetails = response.data.object;
							
							
						if($scope.admissionDetails.isFinalBill == "Y"){
								growl.warning("Final bill is already generated for this admission..!!",{
									title : "WARNING"
								});
								$scope.admissionDetails = null;
								self.model.periodicityServ.uhidNumber='';
								self.model.periodicityServ.patientName ='';//+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
								self.model.periodicityServ.visitTypeId ='';
								self.model.periodicityServ.wardName ='';
								self.model.periodicityServ.bedNumber ='';
								self.model.periodicityServ.admissionNumber ='';
								searchPatient($scope.patientSearchObj);
								return false
								
							}else{
								
							/*$scope.dateOfAdmission = typeof($scope.admissionDetails.doa) != "undefined" ? $scope.admissionDetails.doa : "";
							$scope.patientDischargeDate = typeof($scope.admissionDetails.pdd) != "undefined" ? $scope.admissionDetails.pdd : "";
							$scope.bedNumber = typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
							$scope.doctorFullName = typeof($scope.admissionDetails.doctorName) != "undefined" ? $scope.admissionDetails.doctorName : "";*/
							//console.log($scope.dateOfAdmission);
								self.model.periodicityServ.bedNumber = 	typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
								self.model.periodicityServ.wardName = 	typeof($scope.admissionDetails.wardNo) != "undefined" ? $scope.admissionDetails.wardNo : ""; 
								self.model.periodicityServ.admissionNumber = typeof($scope.admissionDetails.admissionNumber) != "undefined" ? $scope.admissionDetails.admissionNumber : "";
								self.model.periodicityServ.admissionId = $scope.admissionDetails.admissionId;
						 }
						}
						$rootScope.stopLoader();
					});
					getPeriodicityServicesByAdmissionId($scope.admissionId);
				}
				else
					{
					$rootScope.stopLoader();
					growl.warning("No Record Found..!!",{
						title : ERROR_MSG
					});
					return false;
					
					}
				//Assignment to model (Santosh)
				//self.model.patientDetails.patientId=($scope.patientList[index].patientId);//4
				$(".patienttSearchResult").toggle();
				$rootScope.stopLoader();
			};
			
		 
		 
	   /* var searchPatient = function(name){
	    	var URI = BASE_URL + ROOT_URL + "/api/periodicity/patients/"+self.model.organizationId+"/"+self.model.unitId+"/"+self.model.periodicityServ.visitTypeId+"/"+name;
	    	return BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	    		return response.data.listObject;
			});
	    }
	    
	    var selectPatient = function(item, model, label){
	    	self.model.periodicityServ = item;
	    	self.model.periodicityServ.visitTypeId = item.visitTypeId.toString();
	    	self.model.periodicityServOrdersList = [];
	    	getPeriodicityServicesByAdmissionId(item.admissionId);
	    }*/
		
			
			$scope.getSexMasterList = function(){
				var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
				BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					$scope.sexMasterList = response.data.listObject;
				});
			}
			$scope.getSexMasterList();
			
			//Idetification List
			$scope.getIdentificationList = function(){
				var URI = BASE_URL + ROOT_URL+ GETACTIVEIDENTIFICATION;
				GenericService.serviceAction("GET", URI, "").then(function(response) {
					$scope.identificationList = [];
					if (response.data.status == "success") {
						$scope.identificationTypeList = response.data.listObject;

					}
				});
			}
			$scope.getIdentificationList();
			
	    var searchPServices = function(serviceName)
	    {
	    	var data={
					"visitTypeId" : self.model.periodicityServ.visitTypeId,
					"unitIdId" : self.model.unitId,
					"serviceName" : serviceName
			};
	    	var URI = BASE_URL + ROOT_URL + "/api/global/search/services/periodicity";
			return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				return response.data.listObject;
			});
		}
	    
	    var selectPService = function(item, model, label)
	    {
	    	//check service config available or not
	    	var URI = BASE_URL + ROOT_URL + "/api/periodicity/config/getRate/"+item.serviceId+"/"+self.model.organizationId+"/"+self.model.unitId+"/"+self.model.periodicityServ.visitTypeId+"/"+item.periodicityServiceConfigId;
	    	$rootScope.startLoader();
	    	BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	    		
				if(response.data.object != null){
					self.model.periodicityServ.serviceName = item.serviceName;
			    	self.model.periodicityServ.serviceId = item.serviceId;
			    	self.model.periodicityServ.equipmentId = item.equipmentId;
			    	self.model.periodicityServ.equipmentName = item.equipmentName;
			    	self.model.periodicityServ.productCategoryId = item.productCategoryId;
			    	self.model.periodicityServ.assetCategoryId = item.assetCategoryId;
			    	self.model.periodicityServ.toDate = null;
			    	self.model.periodicityServ.toTime = null;
			    	self.model.periodicityServ.perServConfig = response.data.object;
			    	/*if(response.data.object.assetCategoryId > 0 && response.data.object.productCategoryId > 0){
			    	$scope.getItemList(response.data.object.assetCategoryId,response.data.object.productCategoryId);
			    	$scope.equipmentHideShow = true;
			    	}
			    	else
			    		{
			    			$scope.equipmentHideShow = false;
			    		}*/
				}else{
					growl.error("Configuration for service not available please add",{
						title : ERROR_MSG
					});
					self.model.periodicityServ.serviceName = "";
			    	self.model.periodicityServ.serviceId = "";
				}
				$rootScope.stopLoader();
			});
		}
	    
	    var initClockPicker = function() {
			$('.clockpicker').clockpicker();
		}
	    
	    var addPeriodicityServOrder = function(periodicityServ){
	    	
	    	
	    	if(periodicityServ.serviceId == '' || periodicityServ.serviceId == null)
    		{
	    		growl.error("Please select service",{
					title : ERROR_MSG
				});
	    		return false;
    		}
	    	else if(periodicityServ.fromDate == undefined || periodicityServ.fromDate == null || periodicityServ.fromDate == '')
    		{
	    		growl.error("Please select from time",{
					title : ERROR_MSG
				});
	    		return false;
    		}
	    	else if(periodicityServ.toDate != null && periodicityServ.toTime == null)
	    	{
	    		growl.error("Please select to time",{
					title : ERROR_MSG
				});
	    		return false;
	    	}
	    	
	    	else{
	    		
	    		var sdt = moment(periodicityServ.fromDate).format('YYYY-MM-DD');
		    	sdt = sdt +' '+periodicityServ.fromTime;
		    	
		    	var tdt = moment(periodicityServ.toDate).format('YYYY-MM-DD');
		    	tdt = tdt +' '+periodicityServ.toTime;
		    	if(sdt > tdt){
		    		growl.warning("To date and time should be greater then start date and time.",{
						title : WARNING_MSG
					});
		    		return false;
	    		}
		    	else{
	    	if(self.model.editFlag){
	    		periodicityServ.unitId = self.model.unitId;
		    	periodicityServ.organisationId = self.model.organizationId;
		    	periodicityServ.status = 'A';
		    	periodicityServ.fromDateString =  moment(periodicityServ.fromDate).format('DD-MM-YYYY');
		    	periodicityServ.toDateString =  moment(periodicityServ.toDate).format('DD-MM-YYYY');
		    	/*if(periodicityServ.equipmentId > 0)
	    		{
		    		var itemName = $.grep($scope.searchItemList, function (doc) {
	    	            return doc.itemId == periodicityServ.equipmentId;
	    	        })[0].itemName;
		    		periodicityServ.equipmentName = itemName;
	    		}*/
	    		self.model.periodicityServOrdersList[self.model.editIndex] = angular.copy(periodicityServ);
	    		self.model.editFlag = false;
	    		self.model.editIndex = null;
	    		$scope.equipmentHideShow = false;
	    		
	    	}else{
	    		$scope.equipmentHideShow = false;
		    	periodicityServ.unitId = self.model.unitId;
		    	periodicityServ.organisationId = self.model.organizationId;
		    	periodicityServ.periodicityServiceOrderId = null;
		    	periodicityServ.createdBy = self.model.userId;
		    	periodicityServ.orderStatusId = 1;//DEFUALT PENDING
		    	periodicityServ.tatTimeName = periodicityServ.perServConfig.tatTimeName;
		    	periodicityServ.measurementUnitId = periodicityServ.perServConfig.measurementUnitId;
		    	periodicityServ.duration = periodicityServ.perServConfig.duration;
		    	periodicityServ.specialityId = periodicityServ.perServConfig.departmentId;
		    	periodicityServ.serviceRendered = 0;
		    	periodicityServ.status = 'A';
		    	
		    	periodicityServ.fromDateString =  moment(periodicityServ.fromDate).format('DD-MM-YYYY');
		    	 
		    	if(periodicityServ.toDate == undefined || periodicityServ.toDate == null){
		    		periodicityServ.toDateString = '-';
		    		periodicityServ.toDate = null;
		    	}
		    	else
		    		{
		    		periodicityServ.toDateString =  moment(periodicityServ.toDate).format('DD-MM-YYYY');
		    		}
		    	
		    	/*if(periodicityServ.equipmentId > 0)
		    		{
			    		var itemName = $.grep($scope.searchItemList, function (doc) {
		    	            return doc.itemId == periodicityServ.equipmentId;
		    	        })[0].itemName;
			    		periodicityServ.equipmentName = itemName;
		    		}*/
		    	self.model.periodicityServOrdersList.push(angular.copy(periodicityServ));
	    	}
	    	self.model.periodicityServ.serviceName = null;
	    	self.model.periodicityServ.serviceId = null;
	    	self.model.periodicityServ.fromDate = null;
	    	self.model.periodicityServ.fromTime = null;
	    	self.model.periodicityServ.toDate = null;
	    	self.model.periodicityServ.toTime = null;
	    	}
	    	}
	    }
	    
	    var removePeriodicityServOrder = function(index){
	    	 self.model.periodicityServOrdersList.splice(index,1);
	    }
	    
	    var editPeriodicityServOrder = function(index){
	    	let tempPeriodicityServ = self.model.periodicityServOrdersList[index];
	    	self.model.periodicityServ.serviceName = tempPeriodicityServ.serviceName;
	    	self.model.periodicityServ.serviceId = tempPeriodicityServ.serviceId;
	    	//self.model.periodicityServ.perServConfig = tempPeriodicityServ.data.object;
	    	self.model.periodicityServ.fromDate = new Date(tempPeriodicityServ.fromDate);
	    	self.model.periodicityServ.toDate = (tempPeriodicityServ.toDate != null && tempPeriodicityServ.toDate != "") ? new Date(tempPeriodicityServ.toDate) : null;
	    	
	    	self.model.periodicityServ.fromDateString = moment(tempPeriodicityServ.fromDate).format('DD-MM-YYYY');
	    	self.model.periodicityServ.toDateString = moment(tempPeriodicityServ.toDate).format('DD-MM-YYYY');
	    	
	    	self.model.periodicityServ.fromTime = tempPeriodicityServ.fromTime;
	    	self.model.periodicityServ.toTime = tempPeriodicityServ.toTime;
	    	self.model.periodicityServ.periodicityServiceOrderId = tempPeriodicityServ.periodicityServiceOrderId;
	    	self.model.periodicityServ.updatedBy = self.model.userId;
	    	self.model.periodicityServ.createdBy = tempPeriodicityServ.createdBy;
	    	self.model.periodicityServ.createdDate = tempPeriodicityServ.createdDate;
	    	self.model.periodicityServ.status = status;
	    	self.model.periodicityServ.tatTimeName = tempPeriodicityServ.tatTimeName;
	    	self.model.periodicityServ.measurementUnitId = tempPeriodicityServ.measurementUnitId;
	    	self.model.periodicityServ.duration = tempPeriodicityServ.duration;
	    	self.model.periodicityServ.specialityId = tempPeriodicityServ.specialityId;
	    	self.model.periodicityServ.visitTypeId = tempPeriodicityServ.visitTypeId;
	    	self.model.periodicityServ.serviceRendered =0;
	    	self.model.periodicityServ.orderNo =tempPeriodicityServ.orderNo;
	    	self.model.periodicityServ.orderDetailId =tempPeriodicityServ.orderDetailId;
	    	/*if(tempPeriodicityServ.equipmentId > 0)
	    		{
	    		 self.model.periodicityServ.equipmentId = tempPeriodicityServ.equipmentId;
	    		 $scope.equipmentHideShow = true;
	    		 $scope.getItemList(tempPeriodicityServ.assetCategoryId,tempPeriodicityServ.productCategoryId);
	    		}
	    	else
	    		{
	    		 $scope.equipmentHideShow = false;
	    		}*/
	    	
	    	self.model.editFlag = true;
	    	self.model.editIndex = index;
	    }
	    
	    var orderPeriodicityService = function(){
	    	
	    	if(self.model.periodicityServOrdersList.length > 0)
	    		{
	    		var pServiceOrderList = [];
               angular.forEach(self.model.periodicityServOrdersList, function (value, key) {
					
					if(value.serviceRendered == 0 || value.serviceRendered == undefined || value.periodicityServiceOrderId == null)
						{
							if(value.status == 'A')
							{
								value.roomId = $scope.admissionDetails.roomId;
								value.wardId = $scope.admissionDetails.wardId;
								value.bedId = $scope.admissionDetails.bedId;
								pServiceOrderList.push(value);
							}
						}
					
				});


	    	//var pServiceOrderList = angular.copy(self.model.periodicityServOrdersList);
	    	pServiceOrderList.forEach((elem) => {
	    		elem.multidelete("fromDateString","toDateString","perServConfig","patientName","uhidNumber","wardName","bedNumber","admissionNumber","serviceName","tatTimeName","serviceRendered","productCategoryId","assetCategoryId","equipmentName");
	    		//elem.fromDate = $rootScope.getFormattedDate(elem.fromDate,false);
	    		elem.fromDate = moment(elem.fromDate).format('DD-MM-YYYY');
	    		if(elem.toDate != undefined || elem.toDate != null)
	    			{
	    			 elem.toDate = moment(elem.toDate).format('DD-MM-YYYY');
	    			}
	    		else{
	    			elem.multidelete("toDate","toTime");
	    		}
	    		elem.defaultSelfTariffId = $scope.defaultSelfTariffId;
	    		elem.selfPayeeId  = $scope.selfPayeeId;
	    		elem.noOfUnit = 0;
	    		elem.chargesResDto = $scope.admissionDetails;
	    		
	    	});
	    	//save p service orders
	    	console.log("pServiceOrderList",pServiceOrderList);

	    	if(pServiceOrderList.length > 0){
                    $rootScope.startLoader();
                    var URI = BASE_URL + ROOT_URL + "/api/periodicity/order";
                    BillingGenericService.serviceAction(METHOD_POST,URI, pServiceOrderList, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
                        if(response.data.status == "success"){
                            //init();
                            getPeriodicityServicesByAdmissionId($scope.admissionId);
                        }
                        $rootScope.stopLoader();
                    });
                    }else
                    {
                        growl.warning("Please add at least one order.",{
                            title : WARNING_MSG
                        });
                        return false;
                    }
	    	}
	    	else{
                   growl.warning("Please add at least one order.",{
                        title : WARNING_MSG
                    });
                    return false;
            	}
	    }
	    
	    
		$scope.getItemList = function(assetsCatId,prodCatId){
			var assetsCatIds = [];
			var prodCatIds = [];
			assetsCatIds.push(assetsCatId);
			prodCatIds.push(prodCatId);
		var data = {
				assetCategoryList : assetsCatIds,
				productCategoryList : prodCatIds,
				orgId:self.model.organizationId,
				unitId : self.model.unitId
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
	    
	    $scope.getFromDate = function()
	    {
	    	self.model.toDateOptions.minDate = new Date(self.model.periodicityServ.fromDate);
	    	
	    }
	    
	    $scope.getClearData = function(type)
	    {
	    	$scope.admissionType = type;
	    	$scope.patientList = [];
	    	self.model.periodicityServOrdersList = [];
	    	self.model.periodicityServ = {fromDate: new Date(),unitId:self.model.unitId,createdBy:self.model.userId};
	    }
	    
	    $scope.updateStatus = function(item)
	    {
	    	console.log("ITEM",item);
	    	var data = {
	    			orderDetailsId : item.orderDetailId,
	    			periodicityServiceOrderId:item.periodicityServiceOrderId,
	    			status:item.status,
	    			updatedBy:self.model.userId
	    	}
	    	$rootScope.startLoader();
	    	var URI = BASE_URL + ROOT_URL + "/api/periodicity/order/cancel";
	    	BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	    		if(response.data.status == "success"){
	    			init();
	    			growl.success("Updated ",{
						title : SUCCESS_MSG
					});
	    			getPeriodicityServicesByAdmissionId($scope.admissionId);
	    		}
	    		$rootScope.stopLoader();
	    	});
	    	
	    	
	    }
	    var getPeriodicityServicesByAdmissionId = function(admissionId){
	    	self.model.periodicityServOrdersList = [];
	    	var URI = BASE_URL + ROOT_URL + "/api/periodicity/order/"+admissionId;
	    	BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
	    		if(response.data.status == "success"){
	    			/*for(response.data.listObject, function(value,key){
	    				response.data.listObject;
	    			})*/
	    			response.data.listObject.forEach((ele)=>{
	    				let fromDate = (ele.fromDate != "" && ele.fromDate != null) ? new Date(ele.fromDate.split("-")[2]+"-"+ele.fromDate.split("-")[1]+"-"+ele.fromDate.split("-")[0]) : null;
	    				let toDate = (ele.toDate != "" && ele.toDate != null) ? new Date(ele.toDate.split("-")[2]+"-"+ele.toDate.split("-")[1]+"-"+ele.toDate.split("-")[0]) : null;
	    				var periodicityServTemp = {
	    						orderNo:ele.orderNo,
	    						orderDetailId:ele.orderDetailId,
	    						admissionId:ele.admissionId,
	    						fromDate:fromDate,
	    						fromDateString:moment(fromDate).format('DD-MM-YYYY'),
	    						fromTime:ele.fromTime,
	    						noOfUnit:ele.noOfUnit,
	    						serviceId:ele.serviceId,
	    						serviceName:ele.serviceName,
	    						toDate:toDate,
	    						toTime:ele.toTime,
	    						patientId:ele.patientId,
	    						visitTypeId:ele.visitTypeId,
	    						wardId:ele.wardId,
	    						bedId:ele.bedId,
	    						unitId:ele.unitId,
	    						organisationId:ele.organisationId,
	    						createdBy:ele.createdBy,
	    						orderStatusId:ele.orderStatusId,
	    						periodicityServiceOrderId : ele.periodicityServiceOrderId,
	    						createdBy : ele.createdBy,
	    		    			createdDate : ele.createdDate,
	    		    			status : ele.status,
	    		    			tatTimeName:ele.tatTimeName,
	    		    			measurementUnitId : ele.measurementUnitId,
	    		    			duration : ele.duration,
	    		    			serviceRendered : ele.serviceRendered,
	    		    			equipmentId : ele.equipmentId,
	    		    			equipmentName : ele.equipmentName,
	    		    			assetCategoryId : ele.assetCategoryId,
	    		    			productCategoryId : ele.productCategoryId
	    				};
	    				if(toDate == null){
	    					periodicityServTemp.toDateString = '-';
	    				}else
	    					{
	    					periodicityServTemp.toDateString = moment(toDate).format('DD-MM-YYYY');
	    					}
	    				self.model.periodicityServOrdersList.push(periodicityServTemp);
	    			});
	    		}
	    		else
	    			growl.error(response.data.status,{
						title : ERROR_MSG
					});
	    	});		
	    	
	    }
	    
		init();
	}
	
	function config($stateProvider){
		$stateProvider.state('periodicityService', {
		    url: '/periodicityService',
		    templateUrl: 'views/Nursing/periodicity-Service.html',
		    controller: "PeriodicityServOrder.Controller",
		    title: 'Periodicity Service',
		    controllerAs: "vm"
		  })
	}
	
	angular
    .module("myApp")
    .config(config)
    .controller("PeriodicityServOrder.Controller", periodicityServOrderController);
})();