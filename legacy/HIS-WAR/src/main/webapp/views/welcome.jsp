<!DOCTYPE html>
<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title>Angularjs Spring Mvc Example</title>
	    
	    <!-- AngularJs Javascript File -->
	    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.5/angular.min.js"></script>
	    
	    <!-- Bootstrap Css -->
	    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	    <script type="text/javascript">
	    var app = angular.module("myApp", []);
	    app.controller("loginCtrl", function($scope,$http) {
	    	  var fetch_usr_app_url = window.location.href;
	    	  
	 	        $scope.submit = function () {
	            $http({
                    method: "POST",
                    url: fetch_usr_app_url,
                    params: {"username":$scope.username,"password":$scope.password}
                }).then(function(response) {
                    console.log("Success");
                    console.log(response.data);
                    $scope.array=response.data;
                    window.location.href = response.data.trim();      
                }, function(response) {
                    console.log("Failure");
                });
	        }
	    });
	    </script>
	</head>
	<body ng-app="myApp" ng-controller="loginCtrl">
	<form ng-submit="submit()">
	    <div id="angularSpring" class="container">
	        <h1 align="center" class="text-primary">Login</h1>
	        <hr />
	        
	        <!------ AngularJs and Spring Mvc Example ------>
	        <center >
	        <table cellpadding="2">
	        <tr><td><h6>Username:</h6></td><td><input type="text"  ng-model="username"></td>  </tr>
	        <tr><td><h6>Password:</h6></td><td><input type="password"  ng-model="password"></td>  </tr>
	        <tr><td colspan="2" align="center"><input type="submit" class="btn btn-primary"></td>  </tr>
	         </table>
	            
	        </center>
	    </div>
	    </form>
	</body>
</html>