/**
 * patentCompareNationChart3.js
 * Author : YeongWoo Kim
 * each nation patent per year chart(polygonal - bar chart)
 */

var inner_outer_Sheet3 = function(svgName, jsonName){
	var fakeClassification;
	$.getJSON(".//"+jsonName+".json", function(json, error){
		var innerDataSet=[], outerDataSet=[];
		$.each(json, function(d, i){
			if(json[d]["classification"] !== undefined){
				fakeClassification = json[d]["classification"];
			}
			else if(String(i.year).substr(0,1) == "I"){
				innerDataSet.push({
					year: String(i.year).substr(3,5),
					value: i.value
				})
			}
			else{
				outerDataSet.push({
					year: String(i.year).substr(3,5),
					value: i.value
				})
			}
		})
		var maxValueInner = d3.max(innerDataSet, function(d){return d.value;});
		var maxValueOuter = d3.max(outerDataSet, function(d){return d.value;});
		var maxValue = Math.max(maxValueInner, maxValueOuter);
		
		var svg = d3.select('#'+svgName);
		var width = parseInt(svg.style("width")) - 70;
		var height = parseInt(svg.style("height")) - 30;
		
		var svgG = svg.append("g")
					.attr("transform", "translate(30,0)");
		
		var xScale = d3.scaleBand()
						.domain(innerDataSet.map(function(d){return d.year;}))
						.range([0,width]).padding(0.5);
		var yScale = d3.scaleLinear()
						.domain([0, maxValue + maxValue * 0.3]).nice()
						.range([height, 0]);
		
		var line = d3.line()
					.x(function(d){return xScale(d.year);})
					.y(function(d){return yScale(d.value);});
		
		svgG.selectAll("rect.bar")
			.data(innerDataSet)
			.enter().append("rect")
			.attr("class", "bar")
			.attr("height", function(d, i){return height-yScale(d.value)})
			.attr("width", xScale.bandwidth())
			.attr("x", function(d, i) {return xScale(d.year)})
			.attr("y", function(d, i) {return yScale(d.value)})
			.attr("fill", function(d) {return fkColorSet[fakeClassification]["100"];});
		
		svgG.append("path")
			.data([outerDataSet])
			.attr("class", "line")
			.attr("d", line(outerDataSet));

		svgG.append("g")
			.attr("transform", "translate(0, " + (height) + ")")
			.call(d3.axisBottom(xScale));

		svgG.append("g")
			.attr("class", "leftAxis")
			.call(d3.axisLeft(yScale).ticks(10));
	})
};