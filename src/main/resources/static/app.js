var phonecatApp = angular.module('rocketLauncherApp', []);

phonecatApp.controller('RocketController', function ($scope, $http) {
	
	$scope.sendCommand = function(command) {
		$http.post('/api/rocket/' + command);
	}
	
});