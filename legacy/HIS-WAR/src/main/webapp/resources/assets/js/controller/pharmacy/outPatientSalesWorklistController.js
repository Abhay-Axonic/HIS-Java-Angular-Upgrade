(function() {
	"use strict";

	function outPatientSalesWorklistController($scope, $rootScope, $http,
			CONSTANTS, reportService, GenericService, PaginationService,
			$window, $timeout, $filter, $state, growl, CurrencyService,
			salesWorklistService, pharamcyContractService, screenDataService,
			cancelReasonService, $cookies,$interval,$location, BillingGenericService) {
		var self = this;

		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.cookieObjectStore = $rootScope.getStoreObj(); // store-accessor campaign: null-safe parse + picker trigger, one owner
    	$scope.storeName = $scope.cookieObjectStore != null ? $scope.cookieObjectStore.storeName : ''; // store null -> empty header
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
		
	 
        $scope.singlePageRefresh = cookieObjectAppConfigSecond.singlePageRefresh;
        
		function initializeController() {
			$rootScope.loginpage = true;

			self.model = {
				fromDate : new Date(),
				toDate : new Date(),
				fromDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date(),
					minDate : moment(new Date()).subtract(180, 'days').toDate()
				},
				options : {
					width : 1,
					height : 15,
					displayValue : false,
					backgroundColor : '#ffffff',
					lineColor : '#000000',
					margin : 0
				},
				toDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date()
				},
				visitTypeDataList : [ {
					"type" : "All"
				} ],
				statusDataList : [ {
					"status" : "All"
				} ],
				pagination : {
					currentPage : 1,
					totalPages : 0,
					pageSize : '10',
					search : '',
					searchData : PaginationService.searchData,
					setTotalPages : PaginationService.setTotalPages,
					getPagesList : PaginationService.getPagesList,
					setPage : PaginationService.setPage,
					setFirstPage : PaginationService.setFirstPage,
					setPreviousPage : PaginationService.setPreviousPage,
					setNextPage : PaginationService.setNextPage,
					setLastPage : PaginationService.setLastPage,
					sortBy : PaginationService.sortBy
				}
			};

			// Methods
			CurrencyService.GetCurrency().then(function(currency) {
				self.model.Currency = currency;
			})
			/*
			 * cancelReasonService.GetReason().then(function (reason) {
			 * self.model.reasons = reason; });
			 */

			

			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.SearchpatientNames = SearchpatientNames;
			self.model.SearchdoctorNames = SearchdoctorNames;
			self.model.SearchUhid = SearchUhid;
			self.model.AddUHID = AddUHID;
			self.model.search = '';
			self.model.prevSearch = '';
			self.model.patientNames = [];
			self.model.PatientNameKeyPress = PatientNameKeyPress;
			self.model.uHIDKeyPress = uHIDKeyPress;
			self.model.GetPatientSalesWorklist = GetPatientSalesWorklist;
			self.model.NavigateToDetail = NavigateToDetail;
			self.model.patientLabel = patientLabel;
			self.model.rePrintBillSlip = rePrintBillSlip;
			self.model.isDisable = isDisable;
			self.model.cancelBillPopup = cancelBillPopup;
			self.model.ModalDismiss = ModalDismiss;
			self.model.cancelOP = cancelOP;

			PopulateDropdown();
			cancelReasonDropdown();
		}

		$scope.popupStore = function () {
			var popup = angular.element('#storePopUp');
			popup.modal('show');
		}

		var OpenFromDate = function() {
			self.model.fromDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenToDate = function() {
			self.model.toDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}
		var cancelBillPopup = function(id) {
			self.model.id = id;
			var popup = angular.element('#confirmCancel');
			popup.modal('show');
		}
		var ModalDismiss = function() {
			var popup = angular.element('#confirmCancel');
			popup.modal('hide');
		}
		var ValidetReason = function() {
			var isValid = true;

			if (!self.model.cancelReason) {
				self.model.isReasonValid = true;
				isValid = false;
			}

			return isValid;
		}
		function setStatus(x) {
			var status = self.model.statusDataList.find(function(obj) {
				return obj.status == x;
			});

			return status.id;
		}
		var cancelOP = function(id) {
			if (ValidetReason()) {
				var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.CANCEL_API;

				var data = {
					"id" : self.model.id,
					"statusId" : self.model.cancelReason ? parseInt(self.model.cancelReason)
							: undefined,
					"cancelNote" : self.model.rejectNote
				}

				$rootScope.startLoader();
				GenericService.serviceAction("PATCH", URI, data).then(
						function(response) {
							self.model.SelectedRequestIds = [];
							ModalDismiss('confirmCancel');
							self.model.rejectNote = undefined;
							self.model.cancelReason = undefined;
							// FixedAssetSearch();
							GetPatientSalesWorklist();
							$rootScope.stopLoader();
							if (response.statusText == "OK") {
								growl.success("Status Cancelled Successfully");
							}

						}, function(err) {
							$rootScope.stopLoader();
							growl.error(err.data.message);
						});
			}
		}

		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.userAccess = true;
		$scope.getPharmacyRole = function() {

			var URI = BASE_URL + ROOT_URL + "/adt/getPharmacyUserRoleType";

			var data = {
				"organizationId" : $scope.organizationId,
				"userId" : $scope.userId,
				"unitId" : $scope.unitId,
			};

			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						if (response.data.listObject != null
								&& response.data.listObject.length > 0) {
							$scope.userAccess = false;
						}
					});
		}

		$scope.getPharmacyRole();

		//  $scope.validatesearch = function () {
        //     var isValid = true;
        //     if(self.model.visitType.id == 0){
        //         growl.error("Please select visit type");
        //         isValid = false;
        //     }
        //     return isValid;
        // }

		var SearchpatientNames = function(search) {
			// if($scope.validatesearch()){
				if (search.length < 3)
				return;

				if ((!self.model.prevSearch || self.model.prevSearch != search)
						&& (search.length == 3)) {
					self.model.prevSearch = search;
					var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId
							+ "/" + search + "/" + self.model.visitType.id;
					$rootScope.startLoader();
					return GenericService.serviceAction("GET", URI, {}).then(
							function(response) {
								$rootScope.stopLoader();
								if (!!response.data && response.data.length > 0) {
									self.model.patientNames = response.data;
									$rootScope.stopLoader();
									return $filter('filter')(
											self.model.patientNames, {
												$ : search
											});
								} else {
									$rootScope.stopLoader();
									return PatientNameNotFound(search);
								}
							}, function(err) {
								$rootScope.stopLoader();
								return PatientNameNotFound(search);
							});
				} else {
					if (!!self.model.patientNames
							&& self.model.patientNames.length > 0
							&& self.model.patientNames[0].itemFound != undefined
							&& !self.model.patientNames[0].itemFound) {
						return PatientNameNotFound(search);
					} else {
						return $filter('filter')(self.model.patientNames, {
							$ : search
						});
					}
				}
			// }
		}

		var PatientNameNotFound = function(search) {
			var item = {
				"itemFound" : false,
				"details" : "Patient Not found with name " + search
			};
			self.model.patientNames = [ item ];
			return self.model.patientNames;
		}

		var AddUHID = function(item) {
			self.model.patientData = item;
			self.model.uhid = item.uhid;
			self.model.patientId = item.patientId;
			self.model.patientName = item.patientName;
			self.model.doctorId = item.doctorId;
		}

		var PatientNameKeyPress = function(event) {
			var search = $filter('filter')(self.model.patientNames, {
				$ : self.model.patientName
			});
			if (search.length != 1) {
				self.model.patientData = null;
				self.model.patientId = null;
				self.model.uhid = null;
			} else {
				self.model.patientData = search[0];
				self.model.patientId = search[0].patientId;
				self.model.uhid = search[0].uhid;
			}
		}

		var uHIDKeyPress = function(event) {
			var search = $filter('filter')(self.model.patientNames, {
				$ : self.model.uhid
			});
			if (search.length != 1) {
				self.model.patientData = null;
				self.model.patientId = null;
				self.model.patientName = null;
			} else {
				self.model.patientData = search[0];
				self.model.patientId = search[0].patientId;
				self.model.patientName = search[0].patientName;
			}
		}

		var SearchUhid = function(search) {

			if (search.length < 1)
				return;

			if ((!self.model.prevSearch || self.model.prevSearch != search)
					&& (search.length == 1)) {
				self.model.prevSearch = search;

				var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API + search + '/'
						+ $scope.organizationId;

				$rootScope.startLoader();
				return GenericService.serviceAction("GET", URI, {}).then(
						function(response) {
							$rootScope.stopLoader();
							if (!!response.data && response.data.length > 0) {
								self.model.patientNames = response.data;
								$rootScope.stopLoader();
								return $filter('filter')(
										self.model.patientNames, {
											$ : search
										});
							} else {
								$rootScope.stopLoader();
								return UHIDNotFound(search);
							}
						}, function(err) {
							$rootScope.stopLoader();
							return UHIDNotFound(search);
						});
			} else {
				if (!!self.model.patientNames
						&& self.model.patientNames.length > 0
						&& self.model.patientNames[0].itemFound != undefined
						&& !self.model.patientNames[0].itemFound) {
					return UHIDNotFound(search);
				} else {
					return $filter('filter')(self.model.patientNames, {
						$ : search
					});
				}
			}
		}

		var UHIDNotFound = function(search) {
			var item = {
				"itemFound" : false,
				"details" : "UHID Not found with search " + search
			};
			self.model.patientNames = [ item ];
			return self.model.patientNames;
		}

		var SearchdoctorNames = function(search) {

			if (search.length < 1)
				return;

			if ((!self.model.prevSearch || self.model.prevSearch != search)
					&& (search.length == 1)) {
				self.model.prevSearch = search;
				var URI = CONSTANTS.GLOBAL.DOCTOR_SEARCH_API
						+ $scope.organizationId + "/" + $scope.unitId + "/"
						+ search;

				$rootScope.startLoader();
				return GenericService.serviceAction("GET", URI, {}).then(
						function(response) {
							$rootScope.stopLoader();
							if (!!response.data && response.data.length > 0) {
								self.model.doctorNames = response.data;
								$rootScope.stopLoader();
								return $filter('filter')(
										self.model.doctorNames, {
											$ : search
										});
							} else {
								$rootScope.stopLoader();
								return DoctorNameNotFound(search);

							}
						}, function(err) {
							$rootScope.stopLoader();
							return DoctorNameNotFound(search);
						});
			} else {
				if (!!self.model.doctorNames
						&& self.model.doctorNames.length > 0
						&& self.model.doctorNames[0].itemFound != undefined
						&& !self.model.doctorNames[0].itemFound) {
					return DoctorNameNotFound(search);
				} else {
					return $filter('filter')(self.model.doctorNames, {
						$ : search
					});
				}
			}
		}

		var DoctorNameNotFound = function(search) {
			var item = {
				"itemFound" : false,
				"details" : "Doctor Not found with name " + search
			};
			self.model.patientNames = [ item ];
			return self.model.patientNames;
		}

		var PopulateDropdown = function() {
			var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.DROPDOWN_API;
			self.model.visitType = self.model.visitTypeDataList[0];
			self.model.status = self.model.statusDataList[0];

			$rootScope.startLoader();
			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								$rootScope.stopLoader();
								self.model.visitTypeDataList = self.model.visitTypeDataList
										.concat(response.data.visitType);
								self.model.statusDataList = self.model.statusDataList
										.concat(response.data.status);
								initialCall();
								var visitTypeData = [];
								self.model.visitTypeDataList.map(function(obj) {
									if (obj.id !== 2) {
										visitTypeData.push(obj);
									}
								});
								self.model.visitTypeDataList = visitTypeData;
								// GetPatientSalesWorklist();
							}, function(err) {
								$rootScope.stopLoader();
							});
		}
		var cancelReasonDropdown = function() {

			var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;
			var data={};
			data.organizationId=$scope.organizationId;
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						
						if (response.data.status == "success") {
							self.model.reasons = response.data.listObject;

						}
					});
		
		
		}
		var initialCall = function() {
			if (!screenDataService.getScreenData()) {
				GetPatientSalesWorklist()
			} else {
				screenDataService
						.getScreenData()
						.forEach(
								function(dataobj) {
									if (dataobj.screenName == 'outPatientSalesWorklist') {
										var requestData = dataobj.searchrequest;
										self.model.visitType = self.model.visitTypeDataList
												.find(function(visitType) {
													if (requestData.visitType.id == visitType.id)
														return visitType;
												});
										self.model.status = self.model.statusDataList
												.find(function(status) {
													return requestData.status.id == status.id;
												});
										self.model.patientId = requestData.patientId
										self.model.patientName = requestData.patientName
										self.model.doctorName = requestData.doctorName
//										self.model.fromDate = new Date(
//												requestData.fromDate);
										self.model.toDate = new Date(
												requestData.toDate);
									}
								});
				GetPatientSalesWorklist();
			}
		}

		var ValidateDate = function() {
			var isValid = true;

//			if (self.model.fromDate > self.model.toDate) {
//				growl.error('From Date should lesser than To Date');
//				isValid = false;
//			}

			if (!self.model.fromDate || !self.model.toDate) {
				growl.error('Enter Valid Date');
				isValid = false;
			}

			return isValid;
		}

		var GetPatientSalesWorklist = function() {
			if (ValidateDate()) {
				var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.SEARCH_API;

				var data = {
					"fromDate" : moment(self.model.fromDate).format(
							'YYYY-MM-DD'),
					"toDate" : moment(self.model.toDate).format('YYYY-MM-DD'),
					"visitTypeId" : !!self.model.visitType
							&& !!self.model.visitType.id ? self.model.visitType.id
							: undefined,
					"patientId" : !!self.model.patientId ? self.model.patientId
							: undefined,
					"patientName" : !!self.model.patientName ? self.model.patientName
							: undefined,
					"doctorName" : !!self.model.doctorName ? self.model.doctorName
							: undefined,
					"statusId" : !!self.model.status && !!self.model.status.id ? self.model.status.id
							: undefined,
					"uhid" : !!self.model.uhid && !!self.model.uhid ? self.model.uhid
							: undefined,
					"unitId" : $scope.unitId,
					"organizationId" : $scope.organizationId,
					"userId" : $scope.userId,
					"storeId" : $scope.storeId
				};

				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, data).then(
						function(response) {
							self.model.pagination.data = [];
							self.model.patientSalesWorklist = response.data;
							if (!!response && !!response.data
									&& response.data.length > 0) {
								PaginationService.searchData(
										self.model.patientSalesWorklist,
										self.model.pagination);
							}
							self.model.patientId = undefined;
							$rootScope.stopLoader();
						}, function errorCallback(response) {
							$rootScope.stopLoader();
							self.model.patientId = undefined;
							growl.error('Error');
						});
				var screenData = {
					searchrequest : {
						"fromDate" : moment(self.model.fromDate).format(
								'YYYY-MM-DD'),
						"toDate" : moment(self.model.toDate).format(
								'YYYY-MM-DD'),
						"status" : self.model.status,
						"visitType" : self.model.visitType,
						"patientId" : self.model.patientId,
						"patientName" : self.model.patientName,
						"doctorName" : self.model.doctorName,
					},
					screenName : 'outPatientSalesWorklist',
				}
				screenDataService.setScreenData(screenData);
			}
		}

		var isDisable = function(patientSalesWorklistdata) {
			if (patientSalesWorklistdata.status == 'Completed'
					|| patientSalesWorklistdata.status == 'Partially Completed'
					|| patientSalesWorklistdata.status == 'Cancelled') {
				return true;
			}
			return false;
		}

		var NavigateToDetail = function(bill) {
			salesWorklistService.setWorklistId(bill.id);
			pharamcyContractService.billObj = bill;
			$state.go('outPatientSalesWorklistDetails');
		}

		var patientLabel = function(item) {
			// document.getElementById("patient_Label").innerHTML = "";
			var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.DETAILS_API
					+ $scope.unitId + '/' + $scope.storeId + '/' + item.id;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.patientLabelDetails = response.data;
						$rootScope.stopLoader();
					});
			// self.model.patientLabelDetails.items.forEach(function(item){
			// item.batch.forEach(function(batch) {
			// if (batch.isSelected == true) {
			// item.batchDetails = batch.batchDetails;
			// }
			// });
			// });
			self.model.label_barcode = {
				type : "code128",
				code : self.model.patientLabelDetails.billNo,
			}
			$timeout(
					function() {
						var printContents = $window.document
								.getElementById('patient_Label').innerHTML;
						var popupWin = $window.open('', '_blank',
								'width=300,height=400');
						popupWin.document.open();
						popupWin.document
								.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">'
										+ printContents + '</body></html>');
						popupWin.document.close();
					}, 100);
		}

		/*
		 * var rePrintBillSlip = function (bill) { var data = { "appName":
		 * "HIS", "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		 * "reportDesignFilePath": "get_salesbillsReceipt.rpt", "reportFormat":
		 * "PDF", "exportFileName": "BillSlip", "reportParams": [ { "paramName":
		 * "Id", "paramType": "Integer", "paramValue": bill.id } ] };
		 * reportService.generate(REPORT_API_URI1, data); }
		 */
		/** *Chetana* */
		var rePrintBillSlip = function(bill) {
			if (bill.selectLabel == "Patient Demographics") {

				var data = {
					"appName" : "HIS",
					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
					"reportDesignFilePath" : "get_PrescriptionForSalesworklistDemographic.rpt",
					"reportFormat" : "PDF",
					"exportFileName" : "BillSlip",
					"reportParams" : [ {
						"paramName" : "prescriptionid",
						"paramType" : "Integer",
						"paramValue" : bill.id
					} ]
				};
				reportService.generate(REPORT_API_URI1, data);
			} else if (bill.selectLabel == "Prescription") {
				var data = {
					"appName" : "HIS",
					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
					"reportDesignFilePath" : "get_PrescriptionForSalesworklist.rpt",
					"reportFormat" : "PDF",
					"exportFileName" : "BillSlip",
					"reportParams" : [ {
						"paramName" : "prescriptionid",
						"paramType" : "Integer",
						"paramValue" : bill.id
					} ]
				};
				reportService.generate(REPORT_API_URI1, data);
			} else {
				var data = {
					"appName" : "HIS",
					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
					"reportDesignFilePath" : "get_PrescriptionForSalesworklist.rpt",
					"reportFormat" : "PDF",
					"exportFileName" : "BillSlip",
					"reportParams" : [ {
						"paramName" : "prescriptionid",
						"paramType" : "Integer",
						"paramValue" : bill.id
					} ]
				};
				reportService.generate(REPORT_API_URI1, data);
			}
		}
		
		 /* $interval(function() {
              if($scope.singlePageRefresh != null && $scope.singlePageRefresh > 0)
            	  GetPatientSalesWorklist();
            }, $scope.singlePageRefresh);*/
		
		$scope.refreshopSalesWorkList = $interval(function() {
			var path = $location.path();
			if(path == '/outPatientSalesWorklist'){
				if($scope.singlePageRefresh != null && $scope.singlePageRefresh > 0)
					GetPatientSalesWorklist();
			}
			else{
				$interval.cancel($scope.refreshopSalesWorkList);
			}
			//alert('d');
          }, $scope.singlePageRefresh);
		
		/** ** */
		
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

		
		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"outPatientSalesWorklist",
						{
							url : "/outPatientSalesWorklist",
							templateUrl : 'views/PharmacyInventory/InPatient/out-patient-sales-worklist.html',
							controller : "OutPatientSalesWorklist.Controller",
							title : 'Out Patient Sales Worklist',
							resolve : {

								data : function($q, $state, $timeout) {
									var deferred = $q.defer();

									$timeout(function() {
										var arr = JSON.parse(localStorage
												.getItem('permissions')).modules
												.filter(function(item) {
													return (item.id == 111);
												});
										if (arr.length <= 0) {
											$state.go("accessDenied")
											deferred.reject();
										} else {
											deferred.resolve();
										}
									});

									return deferred.promise;
								}
							},
							controllerAs : "vm"
						});
	}

	angular.module("myApp").config(config).controller(
			"OutPatientSalesWorklist.Controller",
			outPatientSalesWorklistController)
})();