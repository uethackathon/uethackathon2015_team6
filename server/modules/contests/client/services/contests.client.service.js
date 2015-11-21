'use strict';

angular.module('contests').factory('ContestService',[
  function () {
  	var exports = {
	  	randomPermutation: function(n){
	  		var array = [];
	  		for(var i = 0; i < n; i ++)
	  			array.push(i);
	  		for (var i = array.length - 1; i > 0; i--) {
		        var j = Math.floor(Math.random() * (i + 1));
		        var temp = array[i];
		        array[i] = array[j];
		        array[j] = temp;
		    }
		    return array;
	  	},

	  	//the arr is shuffled by the key (if provided). Otherwise, the key is generated
	  	createShuffle: function(arr, key){
	  		var n = arr.length;
	  		key = key || this.randomPermutation(n);
	  		var tmp = new Array(n);
	  		for(var i = 0; i < n; i++)
	  			tmp[i] = arr[key[i]];
	  		arr = tmp;
	  		console.log(arr);
	  		console.log(key);
	  		return key;
	  	},

	  	//given an arr and key, restore that arr
	  	restoreShuffle: function(arr, key){

	  	}	
  	}
  	
    return exports;
  }
]);
