/**
 * Created by shekhargulati on 10/06/14.
 */
 
var app = angular.module('todoapp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngAnimate'
]);

$(document).ready(function () {
  var trigger = $('.hamburger'),
  overlay = $('.overlay'),
  isClosed = false;

	trigger.click(function () {
	  hamburger_cross();      
	});

	function hamburger_cross() {

	  if (isClosed == true) {          
	    overlay.hide();
	    trigger.removeClass('is-open');
	    trigger.addClass('is-closed');
	    isClosed = false;
	  } else {   
	    overlay.show();
	    trigger.removeClass('is-closed');
	    trigger.addClass('is-open');
	    isClosed = true;
	   }
	}
	  
	  $('[data-toggle="offcanvas"]').click(function () {
	    $('#wrapper').toggleClass('toggled');
	  }); 
	  
});
 
app.config(function ($routeProvider) {
    $routeProvider.when('/todoList', {
        templateUrl: 'views/toDoList.html',
        controller: 'ListCtrl'
    })
    .when('/createTodo', {
        templateUrl: 'views/createTodo.html',
        controller: 'CreateCtrl'
    })
    .when('/userList', {
        templateUrl: 'views/userList.html',
        controller: 'UserListCtrl'
    })
    .when('/createUser', {
        templateUrl: 'views/createUser.html',
        controller: 'CreateUserCtrl'
    })
    .when('/quizz', {
        templateUrl: 'views/quizz.html',
        controller: 'quizzCtrl'
    })
    .when('/quizzCat/:id', {
        templateUrl: 'views/quizzCat.html',
        controller: 'quizzCatCtrl'
    })
    .when('/createQuizzCat', {
        templateUrl: 'views/createQuizzCat.html',
        controller: 'CreateCatCtrl'
    })
    .when('/noQuestions/:id', {
        templateUrl: 'views/noQuestion.html',
        controller: 'quizzCatCtrl'
    })
    .when('/createQuizzQuest/:id', {
        templateUrl: 'views/createQuizzQuest.html',
        controller: 'CreateQuestCtrl'
    })
    .when('/quizzQuest/:id', {
        templateUrl: 'views/quizzQuest.html',
        controller: 'selectQuestCtrl'
    })
    .otherwise({
        redirectTo: '/todoList'
    })
});
 
app.controller('ListCtrl', function ($scope, $http, $location, $route) {
    $http.get('/api/v1/todos').success(function (data) {
        $scope.todos = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
 
    $scope.todoStatusChanged = function (todo) {
        console.log(todo);
        $http.put('/api/v1/todos/' + todo.id, todo).success(function (data) {
            console.log('status changed');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
    
    $scope.deleteToDo = function (todo) {
    	$http.put('/api/v1/delete/' + todo.id).success(function (data) {
            console.log('todo deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
    
});
 
app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.todo = {
        done: false
    };
    
	$http.get('/api/v1/users').success(function (data) {
		$scope.users = data;
	});
    
    $scope.createTodo = function () {
        console.log($scope.todo);
        $http.post('/api/v1/todos', $scope.todo).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});


app.controller('UserListCtrl', function ($scope, $http, $route) {
    $http.get('/api/v1/users').success(function (data) {
        $scope.users = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.deleteUser = function (user) {
    	$http.put('/api/v1/delete', user).success(function (data) {
            console.log('user deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});
 
app.controller('CreateUserCtrl', function ($scope, $http, $location) {
 
    $scope.createUser = function () {
        console.log($scope.user);
        $http.post('/api/v1/users', $scope.user).success(function (data) {
            $location.path('/userList');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});


app.controller('quizzCtrl', function ($scope, $http, $location, $route) {
	$http.get('/api/v1/quizzCats').success(function (data) {
        $scope.cats = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.openCat = function (){
		$location.path('/quizzCat/' + this.cat.id);
	}
	
	$scope.restartCat = function(cat){
		$http.put('/api/v1/restartCat/' + cat.id).success(function (data) {
            console.log('category restarted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
	
	$scope.deleteCat = function(cat){
		$http.put('/api/v1/deleteCat/' + cat.id).success(function (data) {
            console.log('category deleted');
            $route.reload();
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
});

app.controller('quizzCatCtrl', function ($scope, $http, $location, $routeParams) {
	$http.get('/api/v1/quizzCat/' + $routeParams.id).success(function (data) {
        $scope.cat = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.openCat = function (){
		$location.path('/quizzCat/' + this.cat.id);
	}
	
	$scope.playCat = function(){
		$http.get('/api/v1/playCat/' + $scope.cat.id).success(function (data) {
	        var id = data;
	        if(id == 0){
	        	$location.path('/noQuestions/' + $scope.cat.id);
	        }
	        else{
	        	$location.path('/quizzQuest/' + $scope.cat.id);
	        }
	    }).error(function (data, status) {
	        console.log('Error ' + data)
	    })
	}
	
	$scope.deleteQuest = function(question){
		$http.put('/api/v1/deleteQuest/' + $scope.cat.id, question.id).success(function (data) {
            console.log('question deleted');
            $scope.cat = data;
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
	}
});

app.controller('CreateCatCtrl', function ($scope, $http, $location) {
    
    $scope.createCat = function () {
        console.log($scope.cat);
        $http.post('/api/v1/cat', $scope.cat).success(function (data) {
            $location.path('/quizz');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('CreateQuestCtrl', function ($scope, $http, $location, $routeParams) {
	$scope.catId = $routeParams.id;
    
	$scope.createQuest = function () {
        console.log($scope.quest);
        $http.post('/api/v1/newQuest/' + $scope.catId, $scope.quest).success(function (data) {
            $location.path('/quizzCat/' + $scope.catId);
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('selectQuestCtrl', function ($scope, $http, $location, $routeParams) {
	$scope.catId = $routeParams.id;
	
	$scope.radioValue = '';
	
	$scope.success = null;
	
	$http.get('/api/v1/quest/' + $routeParams.id).success(function (data) {
        $scope.quest = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })
    
    $scope.radioChanged = function(){
		$scope.answerSelected = this.proposition;
	}
    
    $scope.answerQuest = function () {
        $http.put('/api/v1/updateQuest/' + $scope.catId + '/' +$scope.quest.id, $scope.answerSelected).success(function (data) {
            $scope.success = data;
            
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
    
    $scope.nextQuest = function() {
    	$http.get('/api/v1/findNextQuestion/' + $scope.catId).success(function (data) {
	        var id = data;
	        if(id == 0){
	        	$location.path('/noQuestions/' + $scope.catId);
	        }
	        else{
	        	$http.get('/api/v1/quest/' + $scope.catId).success(function (data) {
	                $scope.quest = data;
	                $scope.success = null;
	            }).error(function (data, status) {
	                console.log('Error ' + data)
	            })
	        }
	    }).error(function (data, status) {
	        console.log('Error ' + data)
	    })
    }
});