load('js/env.rhino.1.2.js');
load('js/jquery-1.6.2.js');
load('js/highcharts.src.envjs.js');
load('js/exporting.src.envjs.js');

//Element.prototype.getBBox = function(){
//	return {
//		height: 15,
//		width: 30
//	};
//};


// new getBBox, as found in https://github.com/one2team/highcharts-serverside-export
Element.prototype.getBBox = function() {
	var w = 10;
	if (this.tagName == "text") {
		var s = this.textContent;
		w = s.length * 6;
	}

	return {
		width : w,
		height : 16
	};
};

var container = document.createElement('div');
document.body.appendChild(container);

chartOptions.chart.renderTo = container;

var chart = new Highcharts.Chart(chartOptions);


// save for useage in java
var svg = chart.getSVG();