/*
 * 
 */
angular.module('myApp')
		.controller("administrationWorklistMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','cancelReasonService','CONSTANTS','commonDetailService','$window','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,cancelReasonService,CONSTANTS, commonDetailService,$window,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			 //Default Constructor
			 var self = this;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			$scope.selectAllRows = false;
			$scope.SelectedItemsIds = [];
			$scope.SelectItem;
			$scope.SelectAllItems;
			
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			  var cookieObjectAppConfig = {};
	            [cookieObjectAppConfig] = $rootScope.checkCookies();

	            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
	            $scope.taxInclusive = cookieObjectAppConfig.taxInclusive;
	            $scope.nurseRole = cookieObjectAppConfig.nurseRole;	
				$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
	            $scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
	            var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
			    $scope.nursingStationsWardsListId = nursingStationsWardsList;

			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			console.log("cookieObjecttttttttttt",$cookies.getObject('cookieObject'));
			var cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
		    if(cookieObjectStore==undefined){
		      $rootScope.storePopup();
		      angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
		    }
			
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			$scope.storeId =cookieObject.storeId;
			
			$scope.initMaster = function(){
				
				
				$scope.dateOptions = {
						formatYear : 'yyyy',
						showWeeks : false,
						maxDate : new Date()
					};
					$scope.todateOptions =
					{
						formatYear : 'yyyy',
						showWeeks : false,
						maxDate : new Date(),
						minDate : new Date(),
					};

					//Date Object For Search DOB
					$scope.openDatePickerSearch = function() {
						$scope.datepickerSearch.opened = true;
					};

					$scope.datepickerSearch = {
						opened : false
					};

					//Date Object For Reshedule
					$scope.openDatePickerReschedule = function() {
						$scope.datepickerReschedule.opened = true;
					};

					$scope.datepickerReschedule = {
						opened : false
					};

					//Date For From Date on Search

					$scope.openDatePickerFromDate = function() {
						$scope.datepickerFromDate.opened = true;
					};

					$scope.datepickerFromDate = {
						opened : false
					};

					//Date For To Date on Search

					$scope.openDatePickerToDate = function() {
						$scope.datepickerToDate.opened = true;
					};

					$scope.datepickerToDate = {
						opened : false
					};

					$scope.datepickerFromDate = new Date();
					$scope.datepickerToDate = new Date();
					

					$scope.dateOptionsSlot = {
							formatYear : 'yyyy',
							showWeeks : false,
							minDate : new Date()
						};
					
						$scope.openDatePicker = function() {
							$scope.datepicker.opened = true;
						};

						$scope.datepicker = {
							opened : false
						};
						
						$scope.datepicker = new Date();

				
				
				
				$scope.billing = true;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						patientId:"",
						doctorId:"",
						admissionId:"",
						encounterId:"",
						itemId:"",
						patientName:"",
						doctorName:"",
						prescriptionDate:"",
						prescriptionDate:"",
						specialityId:" ",
						specialityName:""
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.visitTypeId = 2;
			 $scope.visitTypes = [
                 { id: 2, name: 'IP' },
                 { id: 4, name: 'ER' },
                 { id: 3, name: 'DC' }
             ];
			
			   $scope.initializeController = function() {
	                $rootScope.loginpage = true;
	                self.model = {
	                    wardList: [{
	                        "wardName": "All"
	                    }]
	               
	                };

	                self.model.search = '';
	                self.model.prevSearch = '';
	                self.model.patientNames = [];
	                $scope.patientSearchObj.uhidNumber = '';
	                $scope.patientSearchObj.patientName = '';
	                $scope.patientSearchObj.genderId = '';
	                $scope.patientSearchObj.visitTypeId = 2;
	                $scope.birthDate = '';
	                $scope.patientSearchObj.identificationTypeId = '';
	                $scope.patientSearchObj.identificationNumber = '';
	            }
			   $scope.initializeController();
			$scope.patientDetails = {
					"patientName" : "",
					"genderCode" : "",
					"age" : "",
					"uhIdNumber" : "",
					"dob" : "",
					"doctorFullName" : "",
					"dateOfAdmission" : "",
					"patientDischargeDate" : "",
					"wardName" : "",
					"bedNumber" : "",
					"imagePath":"",
					"patientId":"",
					"visitTypeId":"",
					"admissionId":"",
					"genderId":"",
				}
			

			
			
	        $scope.isWardChanged = function()
            {      
                self.model.patientName = "";
                self.model.uhid = "";
                self.model.visitNumber = "";
                self.model.visitNumberText = "";
                $scope.searchPatientList = [];
                $scope.patientSearchObj.uhidNumber = '';
                $scope.patientSearchObj.patientName = '';
                $scope.patientSearchObj.genderId = '';
                $scope.birthDate = '';
                $scope.patientSearchObj.identificationTypeId = '';
                $scope.patientSearchObj.identificationNumber = '';
    
                //start for nursing
                var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
                GenericService.serviceAction("GET",URI,{}).then(function(response){
                    if (response.data.status == "success") {
                        $scope.departmentNursingStationMapperDtoslist = [];			  
                        $scope.departmentNursingStationMapperDtoslist = response.data.listObject;
                        //alert($scope.roleName);
                        if($scope.nurseRole == 1){
                            if($scope.departmentNursingStationMapperDtoslist.length > 0)
                            {
                                if($scope.departmentNursingStationMapperDtoslist.length > 1){
                                    angular.element('#nursingStationModal').modal('show');
                                    //$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
                                }
                                else
                                {
                                    var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                                    GenericService.serviceAction("GET",URI,{}).then(function(response){
                                        if (response.data.status == "success") {
                                            $scope.departmentNursingStationListC = {};
                                            // console.log("fff",response.data.object);
                                            $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
                                            sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                                            sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                                            sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                                            sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
                                            //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
                                            sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
                                            $scope.wardListForGetBedList = response.data.object;
                                        }
                                    });
                                }
                            }
                        }
                    }
                });        
            }
    
            $scope.departmentNursing = {};
            $scope.selectsetNursingStation = function()
            {
                self.model.wardIdFromService = [];
               
                if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
                    growl.warning('Please select nursing station..!!');
                    return false;
                }else{
                $scope.wardListForGetBedList=[];
                var data = JSON.parse($scope.departmentNursing);
                //console.log("$scope.departmentNursing",$scope.departmentNursing);
                //console.log("$scope.departmentNursing ID",data.nursingStationId);
                var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                    GenericService.serviceAction("GET",URI,{}).then(function(response){
                        if (response.data.status == "success") {
                            $scope.departmentNursingStationListC = {};
                            //console.log('nursingStationsWardsList',response.data.object);
                            $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
                            sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                            sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                            sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                            sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
                            $scope.nursingStationId = data.nursingStationId;
                            sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
                            $scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
                            $scope.wardListForGetBedList = response.data.object;
                            self.model.ward.id = $scope.wardListForGetBedList;
                            $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
                            self.model.wardIdFromService = $scope.wardListForGetBedList;
                            angular.element('#nursingStationModal').modal('hide');
                        }
                    });
                }
            }
    
            $scope.genderList = function () {
                var data = {};
                var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
                BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
                .then(function(response) {
                    console.log(response);
                    $scope.genderList = [];
                    if (response.data.status == "success")
                        $scope.genderList = response.data.listObject;
                });
            }
            $scope.genderList();
    
            $scope.identificationType = function () {
                var data = {};
                var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
                BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
                .then(function(response) {
                    console.log(response);
                    $scope.identificationList = [];
                    if (response.data.status == "success")
                        $scope.identificationList = response.data.listObject;
                });
            }
            $scope.identificationType();
            
            
            $scope.openDatePicker = function () {
                $scope.datepicker.opened = true;
            };
    
            $scope.birthDateOptions = {
                formatYear: 'yyyy',
                showWeeks: false,
                maxDate: new Date()
            };
    
            $scope.datepicker = {
                opened: false
            };
    
            $scope.searchPatient = function (patientSearchData) {
                self.model.patientName = "";
                self.model.uhid = "";
                self.model.visitNumber = "";
                self.model.visitNumberText = "";
                $scope.searchPatientList = [];
                if (!!$scope.birthDate) {
                    var birthDate = moment($scope.birthDate).format('YYYY-MM-DD');
                } else {
                    var birthDate = undefined;
                }
                if($scope.wardListForGetBedList == undefined){
                    $scope.wardListForGetBedList = $scope.nursingStationsWardsListId;
                }
                var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteria/global';
                var data = {
                    "uhidNumber": patientSearchData.uhidNumber,
                    "patientName": patientSearchData.patientName,
                    "genderId": patientSearchData.genderId,
                    "mobileNumber": patientSearchData.mobileNumber,
                    "birthDate": birthDate,
                    "identificationNumber": patientSearchData.identificationNumber,
                    "identificationTypeId": patientSearchData.identificationTypeId,
                    "unitId": $scope.unitId,
                    "organizationId": $scope.organizationId,
                    "wardList": $scope.wardListForGetBedList,
                    "visitTypeId": $scope.visitTypeId 
                }
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $scope.searchPatientList = response.data.listObject; 
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                });
            }

            $scope.getPatientDetail = function (patient) {
            	$scope.admissionId = patient.admissionId ? patient.admissionId : undefined;
            	$scope.age = patient.age ? patient.age : undefined;
            	$scope.patientDetail = patient.patientName ? patient.patientName : undefined;
            	$scope.uhIdNumber = patient.uhIdNumber ? patient.uhIdNumber : undefined;
            	$scope.dob = patient.birthDate? patient.birthDate : undefined;
            	$scope.doa = patient.doa ? patient.doa : undefined;
            	$scope.pdd = patient.pdd ? patient.pdd : undefined;
            	$scope.wardName = patient.wardName ? patient.wardName : undefined;
            	$scope.bedNo = patient.bedNumber ? patient.bedNumber : undefined;
            	$scope.patientProfile = patient.patientProfile;
            	$scope.genderId = patient.genderId;
                $scope.patientId = patient.patientId;
                $scope.doctorDetail = patient.doctorName ? patient.doctorName : undefined;
                
            }
	
			$scope.masterList = function() {
				$rootScope.startLoader();
				var data = {
						      organizationId:$scope.organizationId,
						      unitId:$scope.unitId,
						      offset:0,
							  noOfRecordsPerPage:10,
							  fromDate: moment( $scope.datepickerFromDate).format('YYYY-MM-DD'),
							  toDate:moment( $scope.datepickerToDate).format('YYYY-MM-DD'),
							  admissionId : $scope.admissionId 
							};
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/getAdministrationWorkListList"; 
					
				
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					$scope.commonListCount = response.data.object;
					console.log("AdministrationList",$scope.commonList);
					}
				});
			}

	
			
			/**List And Pagination End**/
			
			$scope.listOfMedications = [];
			$scope.listOfMedicationsTest = [];
			$scope.listOfMedicationsTestFinal = [];
			$scope.cancelObj = {};
			$scope.medicationsList = function(info) {
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
				$scope.listOfMedications = [];
				$scope.listOfMedicationsTest = [];
				$scope.validateOrderQty=false;//sneha
				$scope.listOfMedicationsTestFinal = [];
//				$scope.cancelObj = {};
				console.log("data of line item",info);
				info.dob = info.dob.split(' ')[0];
				$scope.patientDetails.patientId=info.patientId;
				$scope.patientDetails.encounterId=info.encounterId;
				$scope.patientDetails.prescriptionId=info.prescriptionId;
				$scope.patientDetails.doctorId=info.doctorId;
				$scope.patientDetails.doctorName=info.doctorName;
				$scope.patientDetails.patientName=info.patientName;
				$scope.patientDetails.uhidNumber=info.uhidNumber;
				$scope.patientDetails.doctorFullName=info.doctorName;
				$scope.patientDetails.storeId=info.storeId;
				$scope.patientDetails.genderCode=info.genderCode;
				$scope.patientDetails.doa=moment(info.doa).format('DD-MM-YYYY');
				$scope.patientDetails.pdd=moment(info.pdd).format('DD-MM-YYYY');
				$scope.patientDetails.dob=info.dob;
				$scope.patientDetails.dobNew=info.dob;
				
				$scope.patientId=info.patientId;
				$scope.patientDetails.imagePath=info.imagePath;
				$scope.patientDetails.genderId=info.genderId;
				$scope.cancelObj = info;
				$scope.selectAllRows = false;
				$rootScope.allergydata($scope.patientDetails);
					
				$rootScope.startLoader();
				var data = {
					      encounterId : info.encounterId,
					      prescriptionId : info.prescriptionId,
						      storeId:$cookies.getObject('cookieObject').storeId,
						      unitId:$scope.unitId,
						      organizationId : $scope.organizationId
							};
				
				var URI = BASE_URL + ROOT_URL + "/api/opd/getDetailedListForAdministrationWorklist"; 
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if(response.data.status == "success"){
										
					$scope.listOfMedicationsTest = response.data.listObject;
					
                    angular.forEach($scope.listOfMedicationsTest , function (value2,key2){
                    	value2.isSelected = 'N';
                        if(value2.administrationTime != null){
                        	value2.administrationTime  = value2.administrationTime;
                        }
                        if(value2.administrationDate != null){
                            value2.dateAdministrationDate  = moment(value2.administrationDate,"DD-MM-YYYY").toDate();
                        }
                        
                        });
					
					$rootScope.stopLoader();
					}
				});
					
			}
			$scope.itemBarcode = "";
            $scope.validateEquealBatchOrItem = function(event,barcode){
                var enterCode = 13;
                if (!barcode && event.which === enterCode) {
                       barcode = $scope.itemBarcode;
                     }
                    if(!!barcode){
                        
                    	
                    	var array = barcode.split(' ');
    	  				var checkFlag = false;
                    	var compareQty = false;
    	  				 angular.forEach($scope.listOfMedicationsTest , function (value,key){
    	                     
    	  					 if(checkFlag == false && value.saleDetailId != null && value.itemId == parseInt(array[0]) && value.batchId ==  parseInt(array[1])){
    	  						 	
    	  						 
    	  						 if(value.consumableQuantity == value.administratorQty){
    	    	                     	
    	  							compareQty = true;
    	  							
    	    	                     }else{
    	    	                    	 
    	    	                    	 	value.isSelected = 'Y';
    	    	  						 	checkFlag = true;
    	    	  						 	$scope.itemBarcode = "";
    	    	  						 	
    	    	  						  if(value.administrationDate == null){
    	    	  							value.dateAdministrationDate  = new Date();
    	    	                          }
    	    	                          if(value.administrationTime == null){
    	    	                        	  value.administrationTime  = moment(new Date()).format('HH:mm');
    	    	                          }
    	    	                     }
    	  						 
    	  						
    	                        }
    	                        
    	                     });
    	  				
    	  				 if(compareQty == true){
    	                     	growl.warning("Quantity given should be greater than administrator quantity",
    									{
    										title : 'Warning!'
    									});
    							return false;
    	                     	
    	                     }
    	  				
                        if(checkFlag == false){
                        	growl.warning('Please enter valid barcode number',
                                    {
                                        title : 'Warning!'
                                    });
                        	return false;
                           }
                        
                    }
                   
            }
			
			
			 $scope.BatchChange = function (index,item,batch) {
				 
				 item.availableStockQuantity=batch.availableStockQuantity;
				 $scope.onChangeBatch(item);
			 }
			 
			 
			 $scope.changeDate = function(info){
				 
				 if(info.isSelected == 'Y'){
					 
						var data ={
					    		
					    		 "prescriptionDetailId": info.prescriptionDetailId,
					    		 "admissionId" : $scope.admissionId ,
								  
					    };
						var URI3 = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/checkObservation";
					    
						GenericService.serviceAction("POST", URI3, data).then(function(response) {
							
									
									if(response.data.listObject != null && response.data.listObject.length>0){
										
										 if(info.consumableQuantity == info.administratorQty){
						                     	info.isSelected = 'N';
						                     	growl.warning("Quantity given should be greater than administrator quantity",
														{
															title : 'Warning!'
														});
												return false;
						                     	
						                     }
											 
											  if(info.administrationDate == null){
												  info.dateAdministrationDate  = new Date();
						                        }
						                        if(info.administrationTime == null){
						                        	info.administrationTime  = moment(new Date()).format('HH:mm');
						                        }
									}else{
										
										info.isSelected = 'N';
										 growl.warning("Please complete Test Dose Administration Result.",
					    							{
					    								title : 'Warning!'
					    							});
									}
							
								});
						$rootScope.stopLoader();
					 
                        
				 }else{
					 info.dateAdministrationDate  = null;
					 info.administrationTime  = null;
				 }
			 }

			$scope.batchExpiryMsgShow = function(item){
                var date = moment(new Date()).format("MM");
				var year = moment(new Date()).format("YYYY");
				var dateCal = parseInt(date) + 1;
				var expDate = moment(item.batchId.expiry).format("MM");
				var expYear = moment(item.batchId.expiry).format("YYYY");
				if((dateCal == expDate || date == expDate) && year == expYear){
                    growl.warning("Batch No. " + item.batchId.batchNo + " is near expiry");
                }  
            }
			
			 $scope.ConvertQtyOrdered = function (item, uom, uomList) {
		            $scope.newUOM = uomList;
		            $scope.selectedUomList = [];
		            $scope.selectedUomList.push(uomList);
		            var stockAvailable = item.availableStockQuantity || 0;
		            if (!!uom.uomTypeId) {
			            if ( uom.uomTypeId == 1 ) {
			        
			                item.newavailableStockQuantity = Math.ceil(stockAvailable / ($scope.newUOM[0].conversion * $scope.newUOM[1].conversion * $scope.newUOM[2].conversion));
			            } else if ( uom.uomTypeId == 2) {
			                item.newavailableStockQuantity = Math.ceil(stockAvailable / ( $scope.newUOM[1].conversion *  $scope.newUOM[2].conversion));
			            } else if ( uom.uomTypeId == 3) {
			                item.newavailableStockQuantity = Math.ceil(stockAvailable / ( $scope.newUOM[2].conversion));
			            }
		            }else{
		                 item.newavailableStockQuantity = 0;
		            }
		        }
			  
			// function for initializa clockpicker
			$scope.initClockPicker = function() {
				$('.clockpicker').clockpicker();
			}
			
			//Administration Date
			  $scope.dateOptions = {
              		formatYear : 'yyyy',
						showWeeks : false,
//						minDate: new Date()
					}; 
			  


			$scope.openAdministrationDate = function(index) {

                for (var i = $scope.listOfMedicationsTest.length - 1; i >= 0; i--) {
                    if (i === index) {
                        $scope.dateAdministrationDate.opened[i] = true;
                    } else {
                   	
                        $scope.dateAdministrationDate.opened[i] = false;
                    }
                    
                }
            };
            
            $scope.dateAdministrationDate = {
                opened : [ false ]

            };
            $scope.dateAdministrationDate = new Date();
			
			
			
			
			
			
			/*$scope.saveBilableConsumption= function(data){
				
				console.log(data);
				
			}*/
		
			$scope.getBatchList=function(info,index){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
				$scope.indexs=index;
//				 $rootScope.startLoaderPopup();
				var data={
						organizationId:$scope.organizationId,
					     unitId:$scope.unitId,
					     itemId:info.itemId,
					      storeId:$cookies.getObject('cookieObject').storeId
				    }
				   var URI = BASE_URL+ROOT_URL+"/api/opd/getBatchListForAdministrationWorklist";
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if(response.data.status == "success"){
//						 $rootScope.stopLoaderPopup();					
					$scope.batchList = response.data.listObject;
					console.log("batchList",$scope.batchList);
					$scope.listOfMedications[$scope.indexs].batchList=$scope.batchList;
				
					}
				});
					
			}
			
			$scope.getUOMListByBatchId=function(info,index){
            if(!$rootScope.storeReady()){ return; } // store-accessor campaign: no store-scoped call before the store pick (incl. load-time, QF-AS/AT)
				$scope.indexs=index;
//				 $rootScope.startLoaderPopup();
				var data={
						organizationId:$scope.organizationId,
					     unitId:$scope.unitId,
					     batchId:info.batchId,
					     storeId:$cookies.getObject('cookieObject').storeId
				    }
				   var URI = BASE_URL+ROOT_URL+"/api/opd/getUomListByBatchIdForAdministrationWorklist";
					GenericService.serviceAction("POST",URI,data).then(function(response){
					if(response.data.status == "success"){
//						 $rootScope.stopLoaderPopup();				
					$scope.UOMList = response.data.listObject;
					console.log("UOMList",$scope.UOMList);
					$scope.listOfMedications[$scope.indexs].UOMList=$scope.UOMList;
				
					}
				});
					
			}
			$scope.getUOMTypeId=function(id){
				angular.forEach($scope.UOMList,function(value,index){
					if(id==value.uomUnitId){
						$scope.listOfMedications[$scope.indexs].uomTypeId=value.uomTypeId;
					}
				});
			}
			
			$scope.getbatchDetail=function(info){
	  			angular.forEach($scope.batchList,function(value,index){
	  				if(info.batchId == value.batchId){
	  				//	var date=value.expiryDate;
	  				//	var array = date.split('/');
	  				//	var newDate = array[1]+'/'+array[2];
	  					$scope.listOfMedications[$scope.indexs].expiryDate = value.expiryDate;
	  				}
	  			});
	  			
	  			}
			
			$scope.getQuantityDetail=function(uom){
	  			angular.forEach($scope.UOMList,function(value,index){
	  				if(uom.uomUnitId == value.uomUnitId){
	  					$scope.listOfMedications[$scope.indexs].availableQty=value.availableQty;
	  				}
	  				
	  			});
	  			
	  			}
			
			$scope.SelectAllItems = function(){
				if ($scope.selectAllRows) {
					$scope.itemSelectedList = {
							items: []
						  };
					$scope.SelectedItemsIds = $scope.listOfMedications.map(function (item) {
						item.isSelected = true;
						$scope.itemSelectedList.items.push(item);
						$("#billconsumption").show();
						$("#billconsumptionbtn").hide();
						return item.itemId;
					});
					$scope.itemsSelect=$scope.itemSelectedList.items;
				} else {
					$scope.SelectedItemsIds = [];
					$scope.itemSelectedList = {
							items: []
						  };
					$scope.listOfMedications.forEach(function (item) {
						item.isSelected = false;
						$("#billconsumption").hide();
						$("#billconsumptionbtn").show();
					});
				}
			}
			$scope.SelectAllItems();

			var isSelected = function (item) {
				return item.isSelected;
			}
			$scope.itemsSelect=[];
			$scope.SelectItems = function(item){
				var selectDate= $scope.itemSelectedList.items;
				
				console.log("selected data",selectDate);
				
				$scope.itemsSelect=$scope.itemSelectedList.items
				
				$scope.SelectedItemsIds = selectDate.map(function (item) {
					item.isSelected = true;
					//item.batchId = item.batch[0],
					//item.uomUnitId = item.batch[0].uom[0],
					$("#billconsumption").show();
					$("#billconsumptionbtn").hide();
					return item.itemId;
				});
				var itemIds = $scope.SelectedItemsIds;
				if (item.isSelected == true) {
					$("#billconsumption").show();
					$("#billconsumptionbtn").hide();
					itemIds.push(item.itemId);
				} else {
					var index = itemIds.indexOf(item.itemId);
					$("#billconsumption").hide();
					$("#billconsumptionbtn").show();
					if (index > -1) {
						itemIds.splice(index, 1);
					}
				}
				var list = $scope.listOfMedications;
				if (list.every(isSelected)) {
					$scope.selectAllRows = true;
				} else {
					$scope.selectAllRows = false;
				}
			}
			
			//-----------Save Billable Consumption-------------//
			$scope.onChangeBatch=function(item){
				 var sellingPrice = !!item.batchId ? commonDetailService.fixedDecimal(item.batchId.sellingPrice) : 0;
	             item.mrp = commonDetailService.calculateRate(item.batchId.uom, item.uomUnitId.uomTypeId, sellingPrice);
//	           self.model.beforeCalculation = item;
		           var data = {
		               "mrp": item.mrp,
//		               "isTaxInc": self.model.taxInclusive,
		            //    "markup": !!item.markUpDown ? item.markUpDownPer : null,
		            //    "markdown": !item.markUpDown ? item.markUpDownPer : null,
					 	"markup": item.markUpDown,
                		"markUpDownPer" : item.markUpDownPer,
		               "discount": item.discount != 0 || item.discount != undefined ?  item.discount : 0,
		               "isDiscPercent": !!item.discountType && item.discountType == 1 ? true : false,
		               "tax": item.taxPercentage,
		               "qty": item.quantityNew,
		               "patientClassPercentage": item.patientClassPercentage,
		              // "paymentEntitlementTypeId":  $scope.entitlementObj.paymentEntitlementTypeId

		            }
		            $scope.patientClassPercentage = data.patientClassPercentage;

		            var response = commonDetailService.calculateAmount(data);
		            $scope.finalResponse=response;
		            console.log("finalResponse",$scope.finalResponse);
		            $scope.rate = $scope.finalResponse.rate;
		            $scope.taxAmount = $scope.finalResponse.taxAmount;
		            $scope.totalAmount = $scope.finalResponse.totalAmount;
		            $scope.netAmount = $scope.finalResponse.netAmount;
		            $scope.patientClassAmount = $scope.finalResponse.patientClassAmount;
		            $scope.unitPatientClassAmount = $scope.finalResponse.unitPatientClassAmount;
			}
			
			$scope.itemSelectedList = {
					items: []
				  };
			
			$scope.isValidate=false;
			$scope.validateDetails=function(){
				$scope.isValidate=true;
				$scope.totalSumConsume=0;
				$scope.totalSumQty=0;
				$scope.totalSumAvailQty=0;
				angular.forEach($scope.listOfMedications,function(value,index){
					var consumableQuantityNew=value.consumableQuantity1;
					var quantityNew=value.quantity;
//					var quantityNewAvail=value.quantityNew;
					$scope.totalSumConsume += parseInt(consumableQuantityNew);
					$scope.totalSumQty += parseInt(quantityNew);
//					$scope.totalSumAvailQty +=parseInt(quantityNewAvail);
					
					if(value.isSelected==true){
						var quantityNewAvail=value.quantityNew;
						$scope.totalSumAvailQty +=parseInt(quantityNewAvail);
						
					if((value.batchId==null || value.batchId==undefined) || (value.batchId.batchId==null || value.batchId.batchId==undefined)){
						$scope.isValidate=false;
						growl.error("Please select batch.",
								{
									title : 'Error!'
								});
						return false;
						$rootScope.stopLoaderPopup();
					}
					else if((value.uomUnitId==null || value.uomUnitId==undefined) || (value.uomUnitId.uomTypeId==null || value.uomUnitId.uomTypeId==undefined)){
						$scope.isValidate=false;
						growl.error("Please select uom.",
								{
									title : 'Error!'
								});
						return false;
						$rootScope.stopLoaderPopup();
					}
					else if(value.quantityNew==null || value.quantityNew==undefined || value.quantityNew==""){
						$scope.isValidate=false;
						growl.error("Please enter quantity.",
								{
									title : 'Error!'
								});
						return false;
						$rootScope.stopLoaderPopup();
					}
					else if(value.quantityNew > value.quantity1 || value.quantityNew > value.quantity){
						$scope.isValidate=false;
						growl.warning("Please enter valid quantity.",
								{
									title : 'Warning!'
								});
						return false;
						$rootScope.stopLoaderPopup();
					}
					else if(value.dateAdministrationDate==null || value.dateAdministrationDate==undefined || value.dateAdministrationDate==""){
						
						growl.warning("Please select Date", {
							title : 'warning!'
						});
						$rootScope.stopLoader();
						  return false;
						
					}
					else if((value.consumableQuantity1!=undefined && ((parseInt(value.consumableQuantity1)+parseInt(value.quantityNew))==(parseInt(value.quantity)))) || 
							parseInt(value.quantityNew)==(parseInt(value.quantity))){
						$scope.isValidate=true;
						$scope.statusId=4;
					}
					else if(value.quantityNew < value.quantity1 || value.quantityNew < value.quantity){
						$scope.isValidate=true;
						$scope.statusId=18;
					}
					/*var consumableQuantityNew=value.consumableQuantity1;
					var quantityNew=value.quantity;
					var quantityNewAvail=value.quantityNew;
					$scope.totalSumConsume += parseInt(consumableQuantityNew);
					$scope.totalSumQty += parseInt(quantityNew);
					$scope.totalSumAvailQty +=parseInt(quantityNewAvail);
//					$scope.totalSumConsume+=parseInt(consumableQuantityNew);
//					$scope.totalSumQty+=parseInt(orderedQty);
*/				}
                });
				if($scope.isValidate==false){
					return false;
				}
                console.log("val1", $scope.totalSumConsume);
                console.log("val2", $scope.totalSumQty);
                console.log("val3",$scope.totalSumAvailQty);
                if(($scope.totalSumQty == $scope.totalSumConsume)|| ($scope.totalSumQty ==$scope.totalSumAvailQty) ||
                		($scope.totalSumQty ==(parseInt($scope.totalSumAvailQty)+ parseInt($scope.totalSumConsume)))){
                	$scope.statusIdNew=4
                }else{
                	$scope.statusIdNew=18
                }
			}
			
			
			$scope.validateQuantity=function(info){
				$scope.validateOrderQty=false;
				if((info.quantityNew > info.quantity1 || info.quantityNew > info.quantity)||
						(info.quantityNew > (parseInt(info.quantity)-parseInt(info.consumableQuantity1)))
						|| (parseInt(info.consumableQuantity1)+parseInt(info.quantityNew)) > parseInt(info.quantity)){
					growl.warning("Please enter valid quantity.",
							{
								title : 'Warning!'
							});
					
					$scope.validateOrderQty=true;
					$rootScope.stopLoaderPopup();
					return false;
				}
				
			}
			
			
			$scope.saveBilableConsumption= function(info){
				$scope.items = [];
				
				var checkRisk = false;
				angular.forEach($scope.listOfMedicationsTest,function(value,index){
					if(value.isSelected == 'Y'){
							var newArr={};
							newArr.administrationWorklistTimeDetailsId = value.administrationWorklistTimeDetailsId,
		      				newArr.administrationDate=moment(value.dateAdministrationDate).format("DD-MM-YYYY"),
							newArr.administrationTime=value.administrationTime,
							newArr.administrationRemark =value.administrationRemark,
							newArr.unitId = $scope.unitId,
							newArr.organizationId = $scope.organizationId,
							newArr.createdBy = $scope.userId,
							newArr.saleDetailsId = value.saleDetailId,
							newArr.administratorQty = value.administratorQty,
							newArr.prescriptionDetailId = value.prescriptionDetailId;
							$scope.items.push(newArr);
							
							if(value.isHighRiskProduct == true && (value.administrationRemark == null || value.administrationRemark =="")){
								checkRisk = true;
							}
						}
                });
				
				if(checkRisk == true){
					growl.warning("Please enter remark.",
							{
								title : 'Warning!'
							});
					return false;
				}
				
				if($scope.items.length == 0){
					growl.warning("Please select medication.",
							{
								title : 'Warning!'
							});
					return false;
				}
				 $rootScope.startLoaderPopup();
				var URI = BASE_URL + ROOT_URL
						+"/api/opd/saveAdministratorWorkList";
				
				GenericService.serviceAction("POST", URI, $scope.items).then(
								function(response) {
									$rootScope.stopLoaderPopup();
									if (response.status == 200) {
										growl.success(response.data.message,
														{
															title : 'Success!'
														});
										angular.element('#details-modal').modal('hide');
										$scope.initMaster();
										$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);									} 
										

								});
				
			}

			$scope.removeItem = function (item) {
				var newArray = [];

				$scope.listOfMedications.map(function (obj) {
					if (obj !== item) {
						newArray.push(obj);
					}
				});
				$scope.listOfMedications = newArray;
				if($scope.listOfMedications.length > 1){
					$scope.flagRemove = true;
				}
				else{
					$scope.flagRemove = false;
				}
			}
			
			//cancel 
			$scope.prescriptionId = 0;
			$scope.isReasonValid = false;
			 $scope.rejectNote = "";
             $scope.cancelReason = "";
			 $scope.selectedRequestIds = [];
			 
			$scope.isCancel = function (item) {
		            if (item.statusName == 'Completed' || item.statusName == 'Cancelled' ) {
		                return true;
		            } 
		    }

		    $scope.cancelBillPopup = function (item) { 
		            $scope.prescriptionId = item.prescriptionId;
		            var popup = angular.element('#confirmCancel');
		            popup.modal('show');
		    }
		        
		    $scope.modalDismiss = function () {
		            var popup = angular.element('#confirmCancel');
		            popup.modal('hide');
		    } 
		    
		    $scope.validetReason = function () {
		            var isValid = true;

		            if (!$scope.cancelReason) {
		                $scope.isReasonValid = true;
		                isValid = false;
		            }

		            return isValid;
		     }
		    
		    $scope.cancelAdministrationWorklist = function(){
		            if ($scope.validetReason()) {
		            	   var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.CANCEL_API;

		                var data = {
		                    "id": $scope.prescriptionId,
		                    "statusId": $scope.cancelReason ? parseInt($scope.cancelReason) : undefined,
		                    "cancelNote": $scope.rejectNote
		                }

		                $rootScope.startLoader();
		                GenericService.serviceAction("PATCH", URI, data).then(
		                    function (response) {
		                        $scope.selectedRequestIds = [];
		                        $scope.modalDismiss('confirmCancel');
		                        $scope.rejectNote = "";
		                        $scope.cancelReason = "";
		                        $scope.patientsList();
		                        $rootScope.stopLoader();
		                        if(response.statusText == "OK"){
		                            growl.success("Status Cancelled Successfully");    
		                        }
		                    },
		                    function (err) {
		                        $rootScope.stopLoader();
		                        growl.error(err.data.message);
		                    });
		            }
		        }
		    
		    $scope.reasons = [];	
		    cancelReasonService.GetReason().then(function (reason) {
                $scope.reasons = reason;
            });
		    
			$scope.getClassForColor = function(info)
			{

				if(info.isHighRiskProduct == true){
					return 'td-bg-red';
				}
			}
			$scope.patientDocumnetInformation = function(info) {
				$scope.patientDocumentFlipBook ={};
				$scope.patientDocumentFlipBook =info;
				
				
				
					var data =
					{
					"encounterId":$scope.patientDocumentFlipBook.encounterId,
					"patientId":$scope.patientDocumentFlipBook.patientId,
					"unitId":$scope.unitId,
					"organisationId":$scope.organizationId,
					"userId":$scope.userId
					}
					
					
				var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
				BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						$scope.patientDocumentListTest = [];
						if (response.data.status == "success") 
						{
							$scope.patientDocumentListTest = response.data.listObject;
							if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
								$('#patientDocumentOption').modal('show');
							}
							$rootScope.stopLoader();
						}
						else if(response.data.status == "error")
							{
							$rootScope.stopLoader();
							}
					});
		
	          };
	          
	          $scope.convertDateToTimestamp = function(dateString) {
	                let parts = dateString.split("-");
	                let day = parseInt(parts[0], 10);
	                let month = parseInt(parts[1], 10) - 1; 
	                let year = parseInt(parts[2], 10);
	                let date = new Date(year, month, day);
	                let timestampInMilliseconds = date.getTime();
	                let timestampInSeconds = timestampInMilliseconds / 1000;
	                return timestampInSeconds;
	            };
	            
		     		$scope.generateFlipbookData = function() {
		                  let flipbookDatalist = [];

			     			var filteredlist=[];
			     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
			     		    	
			     		    	if(filterObj.documentPath != null){
			     				var flipbookDataobj={};
			     				 let createdDate = filterObj.createdDate;
			     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
			     				flipbookDataobj.timestamp=timestamp;
			     				flipbookDataobj.url=filterObj.documentPath;
			     				flipbookDataobj.tags=[];
			     				flipbookDataobj.tags.push("Document");
			     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
			     				flipbookDatalist.push(flipbookDataobj);
			     		    	}
			     				
			     			});

		                let data = {
		                    flipbookData: flipbookDatalist,
		                    logo: $scope.firstImgaa,
		                    privacy: "public"
		                };

		                let baseUrlLocation = $window.location.href;
		                let temp = baseUrlLocation.split('/');
		                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

		                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
		                
		        		try {
		        			var param={};
		        				$.ajax({
		        			        url : generateFlipbookurl,
		        					type : "POST",
		        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
		        					contentType : "application/json",
		        					dataType : "json",
		        					data : JSON.stringify(data),
		        			        //headers:getHeaders(),
		        					success : function(response){
		        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
		        							  window.open(''+response.flipbookUrl, '_blank');
		        						}else{
		        							$.notify("Error occurred","error");
		        						}
		        			    	}
		        			});
		        	     } catch (e) {
		        	 		console.log(e );
		        		}
		            };
		    
		            
		            $scope.initClockPicker1 = function() 
		            { 
		            	$('.clockpicker').clockpicker();
		            }
		            
		            
		            $scope.dateOptions =
		    		{
		    			formatYear : 'yyyy',
		    			showWeeks : false,
		    		};
		            
		            $scope.open = function() {
		    			$scope.popup.opened = true;
		    		};

		    		$scope.popup =
		    		{
		    			opened : false
		    		};
		            
		            $scope.dateOptions1 =
		    		{
		    			formatYear : 'yyyy',
		    			showWeeks : false,
		    		};
		            
		            $scope.open1 = function() {
		    			$scope.popup.opened1 = true;
		    		};

		    		$scope.popup =
		    		{
		    			opened1 : false
		    		};

		    		$scope.item = {};
		    		$scope.saveBtn = true;
		            $scope.getAntibioticTest = function(obj){
		            	$scope.item = {};
		            	$scope.saveBtn = false;
		            	
		    			var data ={
		    		    		
		    		    		 "prescriptionDetailId": obj.prescriptionDetailId,
		    		    		 "admissionId" : $scope.admissionId,
		    					  
		    		    };
		    			var URI3 = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/getAntibioticTestDose";
		    		    
		    			GenericService.serviceAction("POST", URI3, data).then(function(response) {
		    				
							if(response.data.object == 2){
								
								growl.warning("Already administrator time captured.",
		    							{
		    								title : 'Warning!'
		    							});
		    					return false;
								
							}else {
								
								if(response.data.object == 1){
									$scope.saveBtn = true;
								}
								
								$scope.item = response.data.listObject[0];
								
								if($scope.item.observationDate != undefined && $scope.item.observationDate != null){
					        		$scope.item.observationDate  = moment($scope.item.observationDate,"YYYY-MM-DD").toDate(); 
					        	}
					        	if($scope.item.adminDate != undefined && $scope.item.adminDate != null){
					        		$scope.item.adminDate  =  moment($scope.item.adminDate,"YYYY-MM-DD").toDate(); 
					        	}
					        	if($scope.item.roa != undefined && $scope.item.roa != null){
					        		$scope.item.roa  =  $scope.item.roa.toString();
					        	}
					        	if($scope.item.allergyId != undefined && $scope.item.allergyId != null){
					        		$scope.item.allergyId  =  $scope.item.allergyId.toString();
					        	}
								 
								
								$scope.responseList = [];
								var URI = BASE_URL
								+ ROOT_URL
								+ "/api/opd/getRoaList"
								+ "?unitId="
								+ $scope.unitId
								+ "&orgId="
								+ $scope.organizationId
								+ "&formulationTypeId="
								+ $scope.item.formulationTypeId
								+ "&genericId="
								+ $scope.item.genericId
								+ "&patientId="
								+  $scope.patientId;

						GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI)
								.then(
										function(response) {
											if (response.data.status == "success") {
												if (response.data.listObject.length !== 0) {
													$scope.responseList = response.data.listObject;
													
												}
											}
										});
								angular.element('#antiBioTest').modal('show');
								
							}
							
						});
		    			$rootScope.stopLoader();
		    			
		    		
		            }
		            
		            
		            $scope.saveAntibioticPopop =function(flag){
		            	
		            	if($scope.item.stopAdministration != undefined && $scope.item.stopAdministration == true){
		            		 angular.element('#testAntiPopup1').modal('show');
		            	}else{
		            		$scope.saveAntibiotic(flag);
		            	}
		            	
		            }
		            
		            $scope.saveAntibiotic = function(flag){
		            	
		            	
		            	if($scope.item.adminDate == undefined || $scope.item.adminDate == null || $scope.item.adminDate == ""){
		            		growl.warning("Please select admin date ",
		    						{
		    							title : 'Warning!'
		    						});
		            		
		            		return false;
		            	}
		            	
		            	if($scope.item.adminTime == undefined || $scope.item.adminTime == null || $scope.item.adminTime == ""){
		            		growl.warning("Please select admin time ",
		    						{
		    							title : 'Warning!'
		    						});
		            		
		            		return false;
		            	}
		            	
		            	
		            	if(flag == true && $scope.item.antibioticTestDoseId == undefined && $scope.item.antibioticTestDoseId == null){
		            		growl.warning("Please save first administration  date and time then result ",
		    						{
		    							title : 'Warning!'
		    						});
		            		
		            		return false;
		            	}
		            	
		            	if(flag == true && ($scope.item.observationDate == undefined || $scope.item.observationDate == null || $scope.item.observationDate == "" )){
		            		growl.warning("Please select observation date ",
		    						{
		    							title : 'Warning!'
		    						});
		            		
		            		return false;
		            	}
		            	
		            	
		            	if(flag == true && ($scope.item.observationTime == undefined || $scope.item.observationTime == null || $scope.item.observationTime == "" )){
		            		growl.warning("Please select observation time ",
		    						{
		    							title : 'Warning!'
		    						});
		            		
		            		return false;
		            	}
		            	var date = new Date();
		            	$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
		            	$scope.item.unitId = $scope.unitId;
		            	$scope.item.organizationId = $scope.organizationId
		            	$scope.item.updatedBy = $scope.userId;
		            	$scope.item.createdBy = $scope.userId;
		            	$scope.item.status = 'A';
		            	$scope.item.createdDate = $scope.dt;
		    			$scope.item.updatedDate = $scope.dt;
		    			$scope.item.allergyId = $scope.item.allergyId !== undefined && $scope.item.allergyId !== null && $scope.item.allergyId !== "" && !isNaN($scope.item.allergyId) ? $scope.item.allergyId : parseInt($scope.item.allergyId);
		    			$scope.item.patientId = $scope.patientDetails.patientId;
		            	$scope.item.admissionId = $scope.admissionId ;
		            	if($scope.item.observationDate != undefined && $scope.item.observationDate != null){
		            		$scope.item.observationDate  =moment($scope.item.observationDate).format('DD-MM-YYYY');  
		            	}
		            	if($scope.item.adminDate != undefined && $scope.item.adminDate != null){
		            		$scope.item.adminDate  = moment($scope.item.adminDate).format('DD-MM-YYYY');  
		            	}
		            	
		            	var URI3 = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/saveAntibioticTestDose";
		    		    
		    			GenericService.serviceAction("POST", URI3, $scope.item).then(function(response) {
		    				
		    							if (response.data.status == "success") {
		    								
		    								growl.success(response.data.message,
		    										{
		    											title : 'Success!'
		    										});
		    								$scope.medicationsList($scope.cancelObj);
		    								 var popup = angular.element('#antiBioTest');
		    						            popup.modal('hide'); 
		    							}
		    				

		    					});
		            	
		            }
		            
		            
		            $scope.getSeverityMaster = function(){

		                var data = {};
		                var headerObj = {
		                    unit_id : $scope.unitId,
		                    organization_id : $scope.organizationId
		                };

		                var URI = BASE_URL + ROOT_URL + GETSEVERITYMASTER;
		                   GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
		                         NOTIFICATION_MSG_STATUS_FALSE,data,headerObj)
		                     .then(
		                        function(response) {
		                            console.log(response);
		                            if (response.data.status == "success")
		                                $scope.severityList = response.data.listObject;
		                           });
		            }

		            $scope.getSeverityMaster();
		            
		       
		            
		    
		}]);