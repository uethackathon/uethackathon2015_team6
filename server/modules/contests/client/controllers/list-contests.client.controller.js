'use strict';

// contests controller
angular.module('contests').controller('listContestsController', ['$scope', '$timeout', '$location', '$http', 'Authentication',
  function ($scope, $timeout, $location, $http, Authentication) {
    $scope.authentication = Authentication;
    $scope.contests = [];

    $scope.find = function(){
    	console.log(11);
	    $http.get('/contest/list').success(function(response){
	    	$timeout(function() {
	            $scope.contests = response;
	        }, 100);     
	    });	
    }
    
    
  }
]);
