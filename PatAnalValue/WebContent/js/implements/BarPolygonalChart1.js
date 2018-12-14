/**BarPolygonalChart1.js
 **Author : YeongWoo Kim
 **Bar chart & Polygonal Chart code(hide)
 */

//TODO : 개별 파일로 해서 parsing 하여 업로드 하는 형식으로 각 그래프마다 1개의 그래프가 있도록 한다(겹치는 부분이 있다고 하면 합쳐도 상관 없음)
var patAll=[];
var fkClass, fkColorSet = new Array();
$.getJSON("./ColorSettings.json", function(json, error){
	for(var d in json){
		fkColorSet[json[d]["classification"]] = new Array();
		for(var col in json[d]){
			if(col != "classification") fkColorSet[json[d]["classification"]][col] = json[d][col];
		}
	}
})

//preset 3개(svg 0 ~ 2) + 함수형(2개씩)
$.getJSON("./result0.json",function(json, error){
	var dataset=[];
	for(var d in json){
		if(json[d]["classification"] === undefined){
			dataset.push({
				year: String(json[d]["year"]).substr(2,4),
				count: json[d]["count"]
			})
		}
		else{
			fkClass = json[d]["classification"];
		}
	}
	patAll = dataset.slice();
	var svg = d3.select("#svg1");
	var width = parseInt(svg.style("width")) - 70;
	var height = parseInt(svg.style("height")) - 30;
	
	var svgG = svg.append("g")
				.attr("transform", "translate(30,0)");
	
	var xScale = d3.scaleBand()
					.domain(dataset.map(function(d){return d.year;}))
					.range([0, width]).padding(0.5);
	
	var yScale = d3.scaleLinear()
					.domain([0, d3.max(dataset, function(d){return d.count + d.count * 0.2;})])
					.range([height, 0]);
	
	var yMax = d3.max(dataset, function(d){return d.count;});
	
	svgG.selectAll("rect.bar")
		.data(dataset)
		.enter()
		.append("rect")
		.attr("class", "bar")
		.attr("height", function(d, i){return height-yScale(d.count)})
		.attr("width", xScale.bandwidth())
		.attr("x", function(d, i) {return xScale(d.year)})
		.attr("y", function(d, i) {return yScale(d.count)})
		.attr("fill", function(d) {return fkColorSet[fkClass]["100"]});
	
	svgG.selectAll("text")
		.data(dataset)
		.enter().append("text")
		.text(function(d) {return d.count})
		.attr("class", "text")
		.attr("x", function(d, i){return xScale(d.year)+xScale.bandwidth()/2})
		.style("text-anchor", "middle")
		.attr("y", function(d, i) {return yScale(d.count)});
	
	svgG.append("g")
		.attr("transform", "translate(0, " + (height) + ")")			// 여기서 height 줄이면 x축이 올라가게 됩니다.
		.call(d3.axisBottom(xScale));
	
	svgG.append("g")
		.call(d3.axisLeft(yScale)
		.ticks(yMax / 20));
})

$.getJSON("./result0.json",function(json, error){
	var dataset=[];
	for(var d in json){
		if(json[d]["classification"] === undefined){
			dataset.push({
				year: String(json[d]["year"]).substr(2,4),
				count: json[d]["count"]
			})
		}
		else{
			fkClass = json[d]["classification"];
		}
	}
	var svg = d3.select("#svg2");
	var width = parseInt(svg.style("width")) - 70;
	var height = parseInt(svg.style("height")) - 30;
	
	var svgG = svg.append("g")
				.attr("transform", "translate(30,0)");
	
	var xScale = d3.scaleBand()
					.domain(dataset.map(function(d){return d.year;}))
					.range([0, width]).padding(0.5);
	
	var yScale = d3.scaleLinear()
					.domain([0, d3.max(dataset, function(d){return d.count + d.count * 0.2;})])
					.range([height, 0]);
	
	var yMax = d3.max(dataset,function(d){return d.count;});
	
	svgG.selectAll("rect.bar")
		.data(dataset)
		.enter()
		.append("rect")
		.attr("class", "bar")
		.attr("height", function(d, i){return height-yScale(d.count)})
		.attr("width", xScale.bandwidth())
		.attr("x", function(d, i) {return xScale(d.year)})
		.attr("y", function(d, i) {return yScale(d.count)})
		.attr("fill", function(d) {return fkColorSet[fkClass]["100"]});
	
	svgG.selectAll("text")
		.data(dataset)
		.enter().append("text")
		.text(function(d) {return d.count})
		.attr("class", "text")
		.attr("x", function(d, i){return xScale(d.year)+xScale.bandwidth()/2})
		.style("text-anchor", "middle")
		.attr("y", function(d, i) {return yScale(d.count)});
	
	svgG.append("g")
		.attr("transform", "translate(0, " + (height) + ")")			// 여기서 height 줄이면 x축이 올라가게 됩니다.
		.call(d3.axisBottom(xScale));
	
	svgG.append("g")
		.call(d3.axisLeft(yScale).ticks(yMax / 5));
})

$.getJSON("./result1.json", function(json, error){
	var dataset= new Array();
	$.each(json, function(d, i){
		dataset.push({
			year: String(i.year).substr(2,4),
			percentage : i.percentage-100
		})
	})
	var svg = d3.select("#svg0");
	
	var width = parseInt(svg.style("width")) - 70;
	var height = parseInt(svg.style("height")) - 30;
	
	var svgG = svg.append("g")
					.attr("transform", "translate(30,0)");
			
	var xScale = d3.scaleBand()
					.domain(dataset.map(function(d){return d.year;}))
					.range([0, width]).padding(0.5);
	
	var xScalePatAll = d3.scaleBand()
						.domain(patAll.map(function(d){return d.year;}))
						.range([0, width]).padding(0.5);
	
	var yScaleRight = d3.scaleLinear()
						.domain([-d3.max(dataset, function(d){return d.percentage;}), d3.max(dataset, function(d){return d.percentage + d.percentage * 0.3;})])
						.range([height, 0]);
	
	var yScaleLeft = d3.scaleLinear()
						.domain([0, d3.max(patAll, function(d){return d.count + d.count * 0.2;})]).nice()
						.range([height, 0]);
	
	var line = d3.line()
				.x(function(d){return xScalePatAll(d.year);})
				.y(function(d){return yScaleLeft(d.count);});
	
	svgG.append("path")
		.data([patAll])
		.attr("class", "line")
		.attr("d", line(patAll));
	
	svgG.selectAll(".bar")
		.data(dataset)
		.enter()
		.append("rect")
		.attr("class", function(d){
			if(d.percentage < 0) 
				return "bar bar--negative";
			else 
				return "bar bar--positive";
		})
		.attr("x", function(d, i) {return xScale(d.year)})
		.attr("y", function(d, i) {
			if(d.percentage > 0 ) {
				return yScaleRight(d.percentage);
			}
			else{
				return yScaleRight(0);
			}
		})
		.attr("height", function(d, i){return Math.abs(yScaleRight(d.percentage) - yScaleRight(0));})
		.attr("width", xScale.bandwidth());
	
	svgG.append("g")
		.attr("transform", "translate(0, " + (height) + ")")			// 여기서 height 줄이면 x축이 올라가게 됩니다.
		.call(d3.axisBottom(xScale));
	
	svgG.append("g")
		.attr("class", "leftAxis")
		.call(d3.axisLeft(yScaleLeft).ticks(10));

	svgG.append("g")
		.attr("class", "rightAxis")
		.attr("transform", "translate(" + width + ", 0)")
		.call(d3.axisRight(yScaleRight).ticks(10));
})

//자동화 람다식 함수
//svgName1 = 첫번째 svg, jsonName1 = 첫번째 json파일
//svgName2 = 두번째 svg, jsonName2 = 두번째 json파일

var dataGraph_sheet1 = function (svgName1, svgName2, jsonName1, jsonName2){
	var patNation=[];
	$.getJSON("./"+jsonName1+".json",function(json, error){
		var dataset=[];
		for(var d in json){
			if(json[d]["classification"] === undefined){
				dataset.push({
					year: String(json[d]["year"]).substr(2,4),
					count: json[d]["count"]
				})
			}
			else{
				fkClass = json[d]["classification"];
			}
		}
		patNation = dataset.slice();
		var svg = d3.select('#'+svgName1);
		var width = parseInt(svg.style("width")) - 70;
		var height = parseInt(svg.style("height")) - 30;
		
		var svgG = svg.append("g")
					.attr("transform", "translate(30,0)");
		
		var xScale = d3.scaleBand()
						.domain(dataset.map(function(d){return d.year;}))
						.range([0, width]).padding(0.5);
		
		var yScale = d3.scaleLinear()
						.domain([0, d3.max(dataset, function(d){return d.count + d.count * 0.2;})])
						.range([height, 0]);
		
		svgG.selectAll("rect.bar")
			.data(dataset)
			.enter()
			.append("rect")
			.attr("class", "bar")
			.attr("height", function(d, i){return height-yScale(d.count)})
			.attr("width", xScale.bandwidth())
			.attr("x", function(d, i) {return xScale(d.year)})
			.attr("y", function(d, i) {return yScale(d.count)})
			.attr("fill", function(d) {return fkColorSet[fkClass]["100"]});
		
		svgG.selectAll("text")
			.data(dataset)
			.enter().append("text")
			.text(function(d) {return d.count})
			.attr("class", "text")
			.attr("x", function(d, i){return xScale(d.year)+xScale.bandwidth()/2})
			.style("text-anchor", "middle")
			.attr("y", function(d, i) {return yScale(d.count)});
		
		svgG.append("g")
			.attr("transform", "translate(0, " + (height) + ")")			// 여기서 height 줄이면 x축이 올라가게 됩니다.
			.call(d3.axisBottom(xScale));
		
		svgG.append("g")
			.call(d3.axisLeft(yScale).ticks(5));
	})
	$.getJSON(".//"+jsonName2+".json", function(json, error){
		var dataset=[];
		$.each(json, function(d, i){
			dataset.push({
				year: String(i.year).substr(2,4),
				percentage : i.percentage-100
			})
		})
		var svg = d3.select('#'+svgName2);
		
		var width = parseInt(svg.style("width")) - 70;
		var height = parseInt(svg.style("height")) - 30;
		
		var svgG = svg.append("g")
						.attr("transform", "translate(30,0)");
				
		var xScale = d3.scaleBand()
						.domain(dataset.map(function(d){return d.year;}))
						.range([0, width]).padding(0.5);
		
		var xScalePatNation = d3.scaleBand()
							.domain(patNation.map(function(d){return d.year;}))
							.range([0, width]).padding(0.5);
		
		var yScaleRight = d3.scaleLinear()
							.domain([-d3.max(dataset, function(d){return d.percentage;}), d3.max(dataset, function(d){return d.percentage + d.percentage * 0.3;})])
							.range([height, 0]);
		
		var yScaleLeft = d3.scaleLinear()
							.domain([0, d3.max(patNation, function(d){return d.count + d.count * 0.2;})]).nice()
							.range([height, 0]);
		
		var line = d3.line()
					.x(function(d){return xScalePatNation(d.year);})
					.y(function(d){return yScaleLeft(d.count);});
		
		svgG.append("path")
			.data([patNation])
			.attr("class", "line")
			.attr("d", line(patNation));
		
		svgG.selectAll(".bar")
			.data(dataset)
			.enter()
			.append("rect")
			.attr("class", function(d){
				if(d.percentage < 0) 
					return "bar bar--negative";
				else 
					return "bar bar--positive";
			})
			.attr("x", function(d, i) {return xScale(d.year)})
			.attr("y", function(d, i) {
				if(d.percentage > 0 ) {
					return yScaleRight(d.percentage);
				}
				else{
					return yScaleRight(0);
				}
			})
			.attr("height", function(d, i){return Math.abs(yScaleRight(d.percentage) - yScaleRight(0));})
			.attr("width", xScale.bandwidth());
		
		svgG.append("g")
			.attr("transform", "translate(0, " + (height) + ")")			// 여기서 height 줄이면 x축이 올라가게 됩니다.
			.call(d3.axisBottom(xScale));
		
		svgG.append("g")
			.attr("class", "leftAxis")
			.call(d3.axisLeft(yScaleLeft).ticks(10));

		svgG.append("g")
			.attr("class", "rightAxis")
			.attr("transform", "translate(" + width + ", 0)")
			.call(d3.axisRight(yScaleRight).ticks(10));
	})
};