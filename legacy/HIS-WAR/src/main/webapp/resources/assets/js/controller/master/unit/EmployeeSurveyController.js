(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function EmployeeSurvey($scope, $rootScope, $http, $state, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        $scope.updateBtnFlag=true;
        var date = new Date();
		$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
		
		
            $scope.ESSList =[];
            $scope.satisfactionList =[];
            $scope.getEmpList =[];

            $scope.geInitialJson = function(){
                $http.get('resources/assets/js/nabhJSON/employee-satisfication-survey.json')
                   .then(function (response) {
                       $scope.ESSList = response.data[0].ESSList;
                       $scope.getEmpList = response.data[0].ESSList;
                       $rootScope.stopLoader();
                   })
                   .catch(function (error) {
                       $rootScope.stopLoader();
                       console.error('Error fetching JSON:', error);
                   });
            }
            $scope.geInitialJson();
            
            
            $scope.stronglyDisagreeFun = function(value,id){
            	if(value == true){
            	$scope.stronglyDisagree = 'Y';
            	}
            console.log("id======",id);	
            	var obj = {
            			id:id
            			
            	}
            	$scope.satisfactionList.push(obj);
            }
            $scope.isSelectedRadio = false;
            $scope.survey={};
            $scope.survey.rating="";
            $scope.saveSurvey = function(){

                angular.forEach($scope.ESSList,function(value){
                    if(value.selectedOption!=""){
                        $scope.isSelectedRadio = true;
                    }
                 });

                 if($scope.isSelectedRadio==false){
                     growl.error("Please select at least one point patient", {
                        title: 'Error!'
                    });
                    $scope.isSelectedRadio = false;
                    $rootScope.stopLoader();
                    return;
                 }

            	var data={
            			satisfactionList:$scope.ESSList,
            			createdBy : $scope.userId,
						updatedBy : $scope.userId,
						userId  : $scope.userId,
						updatedDate : $scope.dt,
						createdDate : $scope.dt,
						organizationId : $scope.organizationId,
						rating : $scope.survey.rating,
						unitId : $scope.unitId,
            			status : 'A'
            	};
            	
            	console.log("final list",data);
            	var URI = BASE_URL + ROOT_URL + "/api/common/empSurvey/saveEmployeeSurvey"
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					if (response.data.status == "success") {
						$rootScope.stopLoader();
//                        angular.forEach($scope.ESSList, function(value) {
//                            value.selectedOption = "";
//                        });
                       $scope.isSelectedRadio = false;
                         $scope.geInitialJson();
                        console.log("$scope.ESSList=============",$scope.ESSList);

						growl.success(response.data.message,
								{
									title : 'success!'
								});
						}else if(response.data.status == 'error'){
						$rootScope.stopLoader();
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
					}
					
				});	
            	
            }  
      
            $scope.getEmployeeSurvey = function(){
            	var data={
            			
						userId  : $scope.userId,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId
            	};
            	
            	var URI = BASE_URL + ROOT_URL + "/api/common/empSurvey/getEmployeeSurveyById"
            	+ "/" + $scope.userId + "/"
				+ $scope.organizationId + "/"
				+ $scope.unitId;
				GenericService.serviceAction("GET", URI,data).then(function(response) {
					if (response.data.status == "success") {
						$rootScope.stopLoader();
						var data = response.data.listObject;
						 $scope.getEmpList;
						 angular.forEach($scope.getEmpList,function(value){
							 if(value.id == data.id)
								 {
								 value.name;
								 value.id;
								 }
						 });
						$scope.ESSList = data;
						var selectedOption = data.selectedOption;
						var data = response.data.listObject;
						var selectedOptions = [];

						angular.forEach(data, function(item) {
						    selectedOptions.push(item.selectedOption);
						});

						$scope.ESSList = $scope.getEmpList.map(function(employee, index) {
						    return {
						        selectedOption: selectedOptions[index],
						        name: employee.name,
						        id: employee.id
						    };
						});

					
						growl.success(response.data.message,
								{
									title : 'success!'
								});
						}else if(response.data.status == 'error'){
						$rootScope.stopLoader();
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
					}
					
				});	
            	
            }
            
//            $scope.getEmployeeSurvey();

            $scope.openDatePickerSearch = function() {
                $scope.datepicker5.opened = true;
            };

            $scope.datepicker5 = {
                opened : false
            };

}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("employeeSurvey", {
            url: "/employeeSurvey",
            title: 'Employee Satisfaction Survey',
            templateUrl: 'views/master/UnitMaster/organizationMaster/employee-survey.html',
            controller: "EmployeeSurvey.Controller",
            controllerAs: "vm"
        });
    }

    angular
    .module("myApp")
    .config(config)
    .controller("EmployeeSurvey.Controller", ['$scope', '$rootScope', '$http', '$state', 'commonDetailService', 'GenericServiceParamHeader', 'StatusService', 'rejectReasonService', '$cookies', 'CONSTANTS', 'GenericService', 'growl', 'PagerService', 'BillingGenericService', 'GenericServiceFileUpload', EmployeeSurvey]);

})();