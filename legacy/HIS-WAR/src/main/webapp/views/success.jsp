<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <title></title>
	    
	    <!-- AngularJs Javascript File -->
	    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.5/angular.min.js"></script>
	    
	    <!-- Bootstrap Css -->
	    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	    <script type="text/javascript">
	    var app = angular.module("myApp", []);
	    app.controller("successCtrl", function($scope,$http) {
	    	var testUrl=(window.location.pathname == "duo-callback" ? "":window.location.pathname)+"#/dashboard";
	    	var finalUrl=testUrl.replace("duo-callback", "");
	    	window.location.replace(finalUrl);
	    });
	    </script>
	</head>
	<body ng-app="myApp" ng-controller="successCtrl">
	
	</body>
</html>