var rocketLauncherApp = angular.module('rocketLauncherApp', []);

rocketLauncherApp.controller('RocketController', function ($scope, $http, $interval) {
	
	$scope.sendCommand = function(command) {
		$http.post('/api/rocket/' + command);
	}
	$scope.showCamera = false;
	
	$http.head('/camera/frame').success(function() {
		$interval(function() {
			$scope.showCamera = true;
			$scope.cameraUrl = '/camera/frame?' + Math.random();
		}, 2000);
	});
	
});