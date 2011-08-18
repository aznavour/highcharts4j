load('js/env.rhino.1.2.js');
load('js/jquery-1.6.2.js');
load('js/highcharts.src.envjs.js');
load('js/exporting.src.envjs.js');

Element.prototype.getBBox = function(){
	return {
		height: 15,
		width: 30
	};
};

var container = document.createElement('div');
document.body.appendChild(container);

chartOptions.chart.renderTo = container;

var chart = new Highcharts.Chart(chartOptions);


// save for useage in java
var svg = chart.getSVG();