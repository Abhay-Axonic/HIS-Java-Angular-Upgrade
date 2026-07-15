/**
 * 
 */

angular.module('myApp')
		.controller("roleCreationMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			$scope.vaccinationListFlag=false;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
           $scope.vaccinationSelectedlist =[];
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						roleId:"",
						roleName:"",
						roleTypeId:"",
						//doctorTypeId:"",
						roleType:"",
						doctorType:"",
						dashboardId:"",
						isCounterApplicable :"N",
						prophylacticVaccinationApplication :"N",
						vaccination :[]
				};
				// $scope.common.prophylacticVaccinationApplication=false;
			    $scope.selectedOptions=[];
                $scope.vaccinationSelectedlist = [];
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();




		/*	role Type*/
	$scope.role =	[{
		        "roleTypeId": 1,
		        "roleType": "Doctor"
		    },
		    {
		        "roleTypeId": 2,
		        "roleType": "Nurse"
		    },
		    {
		        "roleTypeId": 3,
		        "roleType": "Not Applicable"
		        	
		    },
		    {
		        "roleTypeId": 4,
		        "roleType": "Staff"
		        	
		    },
		    {
		        "roleTypeId": 5,
		        "roleType": "Picker"
		        	
		    },
		    {
		        "roleTypeId": 6,
		        "roleType": "Cash Desk Management"
		        	
		    },
		    {
		        "roleTypeId": 7,
		        "roleType": "Pharmacy"
		        	
		    },
		    {
		        "roleTypeId": 8,
		        "roleType": "OT Surgeon"
		        	
		    },
		    {
		        "roleTypeId": 9,
		        "roleType": "OT Anaesthesia"
		        	
		    },
		    {
		        "roleTypeId": 10,
		        "roleType": "OT Manager"
		        	
		    },
		    {
		        "roleTypeId": 11,
		        "roleType": "OT Nurse"
		        	
		    },
		    {
		        "roleTypeId": 12,
		        "roleType": "OT Admin"
		        	
		    },
		    {
		        "roleTypeId": 13,
		        "roleType": "Dialysis Technician"
		        	
		    },
		    {
		        "roleTypeId": 14,
		        "roleType": "Pathologist"
		        	
		    },
		    {
		        "roleTypeId": 15,
		        "roleType": "Phlebotomist"
		        	
		    },
            {
                "roleTypeId": 17,
                "roleType": "MRD"

            },
		    {
                "roleTypeId": 18,
                "roleType": "Housekeeping"

            }
		]
	
	
	$scope.doctorType =	[
		{
        "doctorTypeId": 1,
	        "doctorType": "RMO"
	    },
	    {
	        "doctorTypeId": 2,
	        "doctorType": "Consultant"
	    }
];
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organisationId:organisationId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId
							};
				
				var data2 = {
						      organisationId:organisationId,
						      unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserRoleMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getUserRoleMasterCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					angular.forEach($scope.commonList, function (value, key) {
						if(value.roleTypeId == 1){
							value.roleType = "Doctor";
						}
						if(value.roleTypeId == 2){
							value.roleType = "Nurse";
						}
						if(value.roleTypeId == 3){
							value.roleType = "Not Applicable";
						}
						if(value.roleTypeId == 4){
							value.roleType = "Staff";
						}
						if(value.roleTypeId == 5){
							value.roleType = "Picker";
						}
						if(value.roleTypeId == 6){
							value.roleType = "Cash Desk Management";
						}
						if(value.roleTypeId == 7){
							value.roleType = "Pharmacy";
						}
						if(value.roleTypeId == 8){
							value.roleType = "OT Surgeon";
						}
						if(value.roleTypeId == 9){
							value.roleType = "OT Anaesthesia";
						}
						if(value.roleTypeId == 10){
							value.roleType = "OT Manager";
						}
						if(value.roleTypeId == 11){
							value.roleType = "OT Nurse";
						}
						if(value.roleTypeId == 12){
							value.roleType = "OT Admin";
						}
						if(value.roleTypeId == 13){
							value.roleType = "Dialysis Technician";
						}
						if(value.roleTypeId == 14){
							value.roleType = "Pathologist";
						}
						if(value.roleTypeId == 15){
							value.roleType = "Phlebotomist";
						}
						if(value.roleTypeId == 17){
                            value.roleType = "MRD";
                        }
						if(value.roleTypeId == 18){
                            value.roleType = "Housekeeping";
                        }
						
						/*if(value.roleTypeId == null|value.roleTypeId == undefined){
							value.roleType = " ";
						}*/
						
					});
					
					/*angular.forEach($scope.commonList, function (value, key) {
						if(value.doctorTypeId == 1){
							value.doctorType = "RMO";
						}
						if(value.doctorTypeId == 2){
							value.doctorTypeId = "Consultant";
						}
						
					});
					console.log("roleListType",$scope.commonList);*/
					}
				});
				GenericService.serviceAction("POST",URI2,data2).then(function(response){
					if(response.data.status == "success"){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
				
			}		
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organisationId:organisationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getUserRoleMasterList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					angular.forEach($scope.commonList, function (value, key) {
						if(value.roleTypeId == 1){
							value.roleType = "Doctor";
						}
						if(value.roleTypeId == 2){
							value.roleType = "Nurse";
						}
						if(value.roleTypeId == 3){
							value.roleType = "Not Applicable";
						}
						if(value.roleTypeId == 4){
							value.roleType = "Staff";
						}
						if(value.roleTypeId == 5){
							value.roleType = "Picker";
						}
						if(value.roleTypeId == 6){
							value.roleType = "Cash Desk Management";
						}
						if(value.roleTypeId == 7){
							value.roleType = "Pharmacy";
						}
						if(value.roleTypeId == 8){
							value.roleType = "OT Surgeon";
						}
						if(value.roleTypeId == 9){
							value.roleType = "OT Anaesthesia";
						}
						if(value.roleTypeId == 10){
							value.roleType = "OT Manager";
						}
						if(value.roleTypeId == 11){
							value.roleType = "OT Nurse";
						}
						if(value.roleTypeId == 12){
							value.roleType = "OT Admin";
						}
						if(value.roleTypeId == 13){
							value.roleType = "Dialysis Technician";
						}
						if(value.roleTypeId == 14){
							value.roleType = "Pathologist";
						}
						if(value.roleTypeId == 15){
							value.roleType = "Phlebotomist";
						}
						if(value.roleTypeId == 17){
                            value.roleType = "MRD";
                        }
						
						/*if(value.roleTypeId == null|value.roleTypeId == undefined){
							value.roleType = " ";
						}*/
						
					});
					/*angular.forEach($scope.commonList, function (value, key) {
						if(value.doctorTypeId == 1){
							value.doctorType = "RMO";
						}
						if(value.doctorTypeId == 2){
							value.doctorTypeId = "Consultant";
						}
						
					});
					console.log("commonList",$scope.commonList);*/
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
					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}


			
			
			$scope.save = function(){
				console.log($scope.common)
				 $scope.$broadcast('show-errors-check-validity');
				if ($scope.commonform.$valid) {
				try{
					
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					
					$scope.common.dashboardId=$scope.common.dashboardId,
					$scope.common.createdBy=$scope.createdBy,
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.createdDate=$scope.dt,
					$scope.common.status='A',
					$scope.common.unitId=$scope.unitId,
					$scope.common.organisationId=$scope.organisationId,
					$scope.common.updatedDate=$scope.dt
					$scope.common.roleTypeId=$scope.common.roleTypeId!="" ? $scope.common.roleTypeId : 3;
					$scope.common.vaccination = [];
					
					if($scope.common.prophylacticVaccinationApplication=="Y"){
						if($scope.selectedOptions.length==0){
						       growl.warning('Please select Vaccination..!!');
						     return false;
						      }
						 angular.forEach($scope.selectedOptions, function (value, key) {
						  $scope.common.vaccination.push(value.id);
						 });
						 
						}
						$scope.showModal = !$scope.showModal;
						$rootScope.startLoader();
					
					//return false;
					var URI = BASE_URL + ROOT_URL + "/api/global/saveUserRoleMaster";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",$scope.common);  
					
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
						    $scope.vaccinationListFlag=false;
						    $scope.vaccinationSelectedlist=[];
							$rootScope.stopLoader();
							growl.success("Role Creation Successfully",
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							//$scope.common.roleTypeId="3";
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						$rootScope.stopLoader();

					});
				
					
				}catch(e){
					console.log("Exception-",e);
				}
				}
			}
		
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/api/global/getUserRoleMasterById/"+id+"/"+$scope.organisationId+"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
						    $scope.common = response.data.listObject[0];
                            $scope.common.roleTypeId=response.data.listObject[0].roleTypeId;
                            $scope.selectedOptions=[];
                            $scope.vaccinationSelectedlist = [];
                             if(response.data.listObject[0].prophylacticVaccinationApplication == "Y"){
                                             $scope.vaccinationListFlag=true;
                                             $scope.vaccinationSelectedlist = [];
                                             angular.forEach(response.data.listObject[0].vaccinations, function (value, key) {
                                            	 var data = {};
                                            	 data.id = value.vaccinationId;
                                            	 $scope.selectedOptions.push(data);
                                            	 $scope.vaccinationSelectedlist.push(value.vaccinationId);
                                             });
                                             
                             }
							//$scope.common.doctorTypeId=response.data.listObject[0].doctorTypeId;
							console.log($scope.common)
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}

			$scope.viewFunction = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +"/api/global/getUserRoleMasterById/"+id+"/"+$scope.organisationId+"/"+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("View",response);

						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.roleTypeId=response.data.listObject[0].roleTypeId;
							//$scope.common.doctorTypeId=response.data.listObject[0].doctorTypeId;
							console.log($scope.common)
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});

				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				 $scope.$broadcast('show-errors-check-validity');
					if ($scope.commonform.$valid) {
				try{
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
					$scope.common.updatedBy=$scope.updatedBy;
					$scope.common.updatedDate=$scope.dt;
					$scope.common.unitId=$scope.unitId;
					$scope.common.organisationId=$scope.organisationId;
					$scope.common.dashboardId=$scope.common.dashboardId;

					//$scope.common.vaccination =$scope.selectedOptions;
                       var vaccinationListUpdate=[]
                      					var temp=$scope.updateListData;
                                               var updateListVaccination = $scope.selectedOptions;
                                                $scope.common.vaccinations =$scope.updateListData;
                                               var idValues = updateListVaccination.map(function(obj) {
                                                   return obj.id;
                                               });
                                            $scope.common.vaccination =idValues;

					var URI = BASE_URL + ROOT_URL + "/api/global/updateUserRoleMaster";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",$scope.common);
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
					}
			}
			
			$scope.updateStatus = function(Id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.roleId=Id,
					
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateUserRoleMasterStatus";
					console.log("update status",$scope.common);
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}
			
			$scope.getDashBoardList = function(name){
				$scope.dashboardList=[];
				var data = {
						 organizationId:$scope.organisationId,
					}
				var URI = BASE_URL + ROOT_URL + "/api/getDashboardMasterList";
				 GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == "success")
							$scope.dashboardList=response.data.listObject;
							console.log("getDashBoardList",$scope.dashboardList);
					});
						
				}
			$scope.getDashBoardList();


			
			/**Crud Operations End**/


			$scope.vaccinationListFlag=false;
            $scope.vacccinationFlag=function(flag){
                if(flag=='Y'){
                   $scope.vaccinationListFlag=true;
                }
                else if(flag=='N'){
                $scope.vaccinationListFlag=false;
                }
            }

             // Start of Vaccination Name dropdown function
                $scope.activeVaccinationList = [];
                $scope.getVaccineNameActiveList = function() {
                    var URI = BASE_URL+ ROOT_URL+ "/api/global/getVaccination";
                    var data = {
                                   organisationId : $scope.organisationId,
                                   unitId : $scope.unitId
                               }
                    GenericService.serviceAction("POST", URI, data).then(function(response) {
                                 var activeList=response.data.listObject;
                               angular.forEach(activeList, function(option) {
                                       option.label = option.vaccinationName;
                                          option.id = option.vaccinationId;
                                       delete option.name;
                                   });
                                $scope.activeVaccinationList = activeList
                                $rootScope.stopLoader();
                            });
                }
                $scope.getVaccineNameActiveList();
                // End of Vaccination Name dropdown function
                  $scope.selectedOptions = [];

                  $scope.dropdownSettings = {
                      scrollableHeight: '300px',
                      scrollable: true,
                      enableSearch: true,
                      // Add more settings as needed
                  };
                  $scope.selectedActive= function(selectedOption){
                     var vList=[];
                     vList=selectedOption;
                     $scope.updateListData=selectedOption;
                  angular.forEach(vList,function(value) {
                  $scope.vaccinationSelectedlist.push(value.id);
                   	});

                  }
}]);