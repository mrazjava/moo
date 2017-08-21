var app = angular.module('mooChatDemo', ['ngStomp']);

app.controller('MooConnectController', function($scope, $http) {

	$scope.mooLogin = function() {
		console.log("moo login: " + $scope.usr.nickName);
		angular.element(document.querySelector('#loginButton'))[0].style.display = "none";
		angular.element(document.querySelector('#logoutButton'))[0].style.display = "inline";
		angular.element(document.querySelector('#msgOutPanel'))[0].style.display = "block";
		angular.element(document.querySelector('#nickName'))[0].disabled = true;
		
		$http.post('/moo/login', $scope.usr.nickName).success(function(data) {
			console.log("login result: " + data);
		});
	};

	$scope.mooLogout = function() {
		console.log("moo logout: " + $scope.usr.nickName);
		
		$http.post('/moo/logout', $scope.usr.nickName).success(function(data) {
			console.log("logout result: " + data);
		});

		angular.element(document.querySelector('#loginButton'))[0].style.display = "inline";
		angular.element(document.querySelector('#logoutButton'))[0].style.display = "none";
		angular.element(document.querySelector('#nickName'))[0].disabled = false;
		angular.element(document.querySelector('#msgOutPanel'))[0].style.display = "none";
	};
});

app.controller('MooMsgController', function ($scope, $http) {
	  
	  $scope.mooMsg = function() {
		$scope.usr.nickName = angular.element(document.querySelector('#nickName'))[0].value;
	  	console.log("msg out: " + $scope.usr);
	  	
	  	$http.post('/moo/msg', $scope.usr);
	  	angular.element(document.querySelector('#msgOut'))[0].value = '';
	  };
});

app.controller('ChatMsgController', function ($stomp, $scope, $http) {
	  $scope.events = [];

	  $http.get('/latest-events').success(function(data) {
		  $scope.events = data;
	  });
    $stomp.connect('http://localhost:8080/chat-websocket', {})
          .then(function (frame) {
              var subscription = $stomp.subscribe('/topic/viewchats', 
                  function (payload, headers, res) {
                      $scope.events = payload;
                      $scope.$apply($scope.events);
              });
       });
});