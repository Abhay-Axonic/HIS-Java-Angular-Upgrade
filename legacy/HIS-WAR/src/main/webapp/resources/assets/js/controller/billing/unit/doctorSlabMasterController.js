angular.module("myApp").controller("doctorSlabMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','PagerService','GenericService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,PagerService,GenericService){
		
	$scope.init = function(){
	
	$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId= cookieObject.organizationId;
		$scope.organizationId =cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		$scope.globalShareSearchObj={
				specialityId:null
				
		};
		
		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		$scope.globalSpecialityList=[];

		
		var commonObj = {
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
			};
		

		var URI = BASE_URL + ROOT_URL + "/adt/getActiveDoctorList";
		GenericService
		.serviceAction("POST", URI, commonObj)
		.then(
				function(response) {
					$scope.doctorsList = [];

					if (response.data.status == "success") {
						$scope.doctorsList = response.data.listObject;

					}
				});
		
		$scope.doctorSlabList = [{
				slabFrom:"0",
				slabTo : "",
				percentage : "",
				status: "A"
		}];
		$scope.doctorSlabId = 0;
		
		$scope.saveBtnFlag = true;
		$scope.updateBtnFlag = false;
	}
	$scope.init();
		

	$scope.addSlab = function(cIndex) {
		
		if($scope.doctorSlabList[cIndex-1].slabTo == ""){
			 growl.warning("Please Enter Slab To ", {
                 title: "Warning"
             });
             return false;
		}
		if($scope.doctorSlabList[cIndex-1].percentage == ""){
			 growl.warning("Please Enter Slab Percentage ", {
                 title: "Warning"
             });
             return false;
		}
		
		if($scope.doctorSlabList[cIndex-1].percentage > 100){
			
			 growl.warning("Slab Percentage less than or equals to 100", {
                 title: "Warning"
             });
			 $scope.doctorSlabList[cIndex-1].percentage = "";
             return false;
		}
		
		if(parseInt($scope.doctorSlabList[cIndex-1].slabTo) <= parseInt($scope.doctorSlabList[cIndex-1].slabFrom)){
			 growl.warning("Slab to  should be greater than slab from ", {
                 title: "Warning"
             });
			 $scope.doctorSlabList[cIndex-1].slabTo = "";
             return false;
		}	
		
		
		  $scope.doctorSlabList.push({slabFrom:$scope.doctorSlabList[cIndex-1].slabTo,
				slabTo : "",
				percentage : "",
				status: "A"});
		};

		$scope.removeSlab = function(index) {
			
			 var le =  $scope.doctorSlabList.length;
			 
				var i = 1;
				
				if(le != 1){
					 var newDataList1 = [];
				angular.forEach($scope.doctorSlabList, function(v) {
					
					if (index != i) {
						newDataList1.push(v);
					}else{
						if(v.doctorSlabMasterId!=null){
							v.status="I";
							newDataList1.push(v);
						}
					}
					
					i++;
				});
				
				$scope.doctorSlabList = newDataList1;
				
				}
			
		};
		
		$scope.saveDoctorSlab = function(){
			if($scope.doctorId == "" || $scope.doctorId == null || $scope.doctorId == undefined){
				 growl
                 .warning("Please select doctor.",
                   {
                    title : 'Warning!'
                   });
				 return false;
			}
			
			$scope.currentDate = new Date();
			$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
			
			/*   var slabFromFound =   $scope.doctorSlabList.find(ar=>(ar.slabFrom == "" || ar.slabFrom ==undefined || ar.slabFrom == null) );
	              if(slabFromFound!=undefined){
	                  $rootScope.stopLoader();
	                  growl.warning("Please Enter Slab From ", {
	                      title: "Warning"
	                  });
	                  return false;
	              }*/
	              
	              var slabToFound =   $scope.doctorSlabList.find(ar=>(ar.slabTo == "" || ar.slabTo ==undefined || ar.slabTo == null) );
	              if(slabToFound!=undefined){
	                  $rootScope.stopLoader();
	                  growl.warning("Please Enter Slab To ", {
	                      title: "Warning"
	                  });
	                  return false;
	              }
	              
	              var percentageFound =   $scope.doctorSlabList.find(ar=>(ar.percentage == "" || ar.percentage ==undefined || ar.percentage == null) );
	              if(percentageFound!=undefined){
	                  $rootScope.stopLoader();
	                  growl.warning("Please Enter Percentage ", {
	                      title: "Warning"
	                  });
	                  return false;
	              }
	              var isValidSlab = true;
	              if($scope.doctorSlabList!=null && $scope.doctorSlabList.length>0){
	            	  angular.forEach($scope.doctorSlabList,function(val,idx){
	            		  if(parseInt(val.slabFrom) >= parseInt(val.slabTo)){
	            			 isValidSlab = false;
	            		 } 
	            	  });
	            	  if(!isValidSlab){
	            		  $rootScope.stopLoader();
		                  growl.warning("Slab to  should be greater than slab from", {
		                      title: "Warning"
		                  });
		                  return false;
	            	  }
	              }
			
			 var tempDataList = [];
				angular.forEach($scope.doctorSlabList, function(value, index) {
						 value.doctorId = $scope.doctorId;
						 value.unitId = $scope.unitId;
						 value.organizationId = $scope.organizationId;
						 value.createdDate = $scope.currentDate;
						 value.updatedDate = $scope.currentDate;
						 value.status = value.status;
						 value.createdBy = $scope.userId;
						 value.updatedBy = $scope.userId;
						 value.amount = null;
						 tempDataList.push(value);
				});
				
				var URI = BASE_URL + ROOT_URL + "/unit/DoctorSlab/doctorSlabSave";
		GenericService.serviceAction("POST", URI,
						tempDataList)
				.then(
						function(response) {

							if (response.data.status == "success") {
								growl.success('Doctor slab added successfully...!!',
												{
													title : 'Success!'
												});
								$scope.doctorId = "";
								$scope.init();
								$scope.getDocotrSlabMasterList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
							} else {
								growl.error(response.data.message,
												{
													title : 'Error!'
												});
							}

						});
				
				
		}
		
		
		$scope.doctorSlabMasterList = [];
		$scope.getDocotrSlabMasterList=function(orgId,unitId, offset,
				noOfRecordsPerPage){ 
			try 
			{
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
				 data={
						 organizationId:$scope.orgId,
						 unitId:$scope.unitId,
						 noOfRecordsPerPage : noOfRecordsPerPage,
						 offset : offset,
				 }
					 
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorSlab/getdoctorSlabList";
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							$scope.doctorSlabMasterList = [];
							$scope.doctorSlabMasterList = response.data.listObject;
							$scope.commonListCount= response.data.object;
							$scope.setPage(1,false);
						});
				  
			} catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.getDocotrSlabMasterList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
		
		$scope.updateStatus = function(id, type) {
			$scope.currentDate = new Date();
			$scope.currentDate = moment($scope.currentDate)
					.format('DD-MM-YYYY HH:mm:ss');

			type = (type == "A") ? "A" : "I";

			var data = {
				doctorId : id,
				status : type,
				updatedBy : $scope.userId,
				updatedDate : $scope.currentDate,
				organizationId : $scope.organizationId,
				unitId : $scope.unitId
			}
			console.log(data);
			var URI = BASE_URL + ROOT_URL + "/unit/DoctorSlab/updateSlabStatusByDoctorId";
			GenericService
					.serviceAction("POST", URI, data)
					.then(
							function(response) {

								if (response.data.status == "success") {
									growl.success('Doctor slab Updated Successfully...!!',
													{
														title : 'Success!'
													});
									$scope.init();
									$scope.getDocotrSlabMasterList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
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
			
		$scope.pager = {};
		$scope.page;

		$scope.setPage = function(page, flag) {
			if (page < 1 || page > $scope.pager.totalPages) {
				return;
			}
			$scope.pager = PagerService.GetPager(
					$scope.commonListCount, page,
					$scope.noOfRecordsPerPage);
			if (flag) {
				$scope.getDocotrSlabMasterList($scope.organizationId,$scope.unitId, $scope.pager.startIndex, $scope.pager.pageSize);
			}
		}
		$scope.setNoOfRecords = function() {
			$scope.getDocotrSlabMasterList($scope.organizationId,$scope.unitId, $scope.offset,
					$scope.noOfRecordsPerPage);
		};
		   
		
		
		
		$scope.doctorSlabDetailsList = [];
		$scope.viewInfo = function(obj,flag){
			try 
			{
				 data={
						 organizationId:$scope.orgId,
						 unitId:$scope.unitId,
						 doctorId:obj.doctorId
				 }
					 
				 var URI = BASE_URL + ROOT_URL + "/unit/DoctorSlab/getdoctorSlabListByDoctorId";
				  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							$scope.doctorSlabDetailsList = [];
							if(flag == 1){
								if(response.data.listObject!=null && response.data.listObject.length>0){
									$scope.doctorSlabDetailsList = response.data.listObject;
									angular.element('#doctorSlabInfo').modal('show');
								}	
							}else{
								if(response.data.listObject!=null && response.data.listObject.length>0){
									$scope.doctorSlabList = [];
								angular.forEach(response.data.listObject,function(value,index){
									$scope.doctorId = value.doctorId;
									var data = {};
									data.slabFrom = value.slabFrom;
									data.slabTo = value.slabTo ; 
									data.percentage = value.percentage ;
									data.doctorSlabMasterId = value.doctorSlabMasterId;
									$scope.doctorSlabList.push(value);
								});
								}
							}	
						});
			
			} catch (e) {
				console.log(e.message)
			}
		}
		
		
		$scope.toChecklimit = function(percentage,index){
			
			if(percentage >100){
				 growl.warning("Percentage should be less than or equals to 100 !!!", {
	                 title: "Warning"
	             });
				 $scope.doctorSlabList[index].percentage = "";			 
			}
			
		}
		
		
		
	}]);