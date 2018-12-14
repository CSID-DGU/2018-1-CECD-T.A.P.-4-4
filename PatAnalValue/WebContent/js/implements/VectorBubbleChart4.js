/**VectorBubbleChart4.js
 **Author : YeongWoo Kim
 **Vectored Bubble Chart
 */

var VectorBubbleChart_Sheet4 = function(svgName, jsonName){
	var data = new Array();
	//var colorDataSet = new Array();
	var classification, grayClassification;
	/*
	$.getJSON("./ColorSettings.json", function(json, error){
		for(var d in json){
			colorDataSet[json[d]["classification"]] = new Array();
			for(var col in json[d]){
				if(col != "classification") colorDataSet[json[d]["classification"]][col] = json[d][col];
			}
		}
	})
	*/
	
	$.getJSON(".//"+jsonName+".json", function(json, error){
		for(var d in json){
			//json 전체를 d로 쪼개서 읽음
			//json[d]["year"] 등으로 불러 올 것
			if(json[d]["classification"] === undefined){
				data.push({
					year: json[d]["Year"],
					x: json[d]["x"],
					y: json[d]["y"]
				})
			}
			else{
				classification = json[d]["classification"];
			}
		}
		
		data.sort(function(a,b){
			if(a.x > b.x)
				return 1;
			else if(a.x < b.x)
				return -1;
			return 0;
		})
		
		var svg = d3.select('#'+svgName);
		var width = parseInt(svg.style("width")) - 70;
		var height = parseInt(svg.style("height")) - 30;
		
		var svgG = svg.append("g")
					.attr("transform", "translate(30,0)");
		
		var xMax = d3.max(data, function(d){return d.x;});
		
		var xAxisMax = d3.range(xMax * 1.12);
		
		var xScale = d3.scaleBand()
						.domain(xAxisMax.map(function(d){return d;}))
						.range([0, width]).padding(0.5);
		
		var yScale = d3.scaleLinear()
						.domain([0, d3.max(data, function(d){return d.y + d.y * 0.2;})])
						.range([height, 0]);
		
		var line = d3.line()
					.x(function(d){return xScale(d.x);})
					.y(function(d){return yScale(d.y);});
		
		svgG.append("g")
			.attr("transform", "translate(0, " + (height) + ")")			// 여기서 height 줄이면 x축이 올라가게 됩니다.
			.call(d3.axisBottom(xScale).tickValues(xScale.domain()
										.filter(function(d, i){
											if(xMax * 1.12 < 50)
												return !(i%5);
											else if(xMax * 1.12 < 100)
												return !(i%10);
											else
												return !(i%20);
										})));
	
		svgG.append("g")
			.attr("class", "leftAxis")
			.call(d3.axisLeft(yScale).ticks(10));
		
		var circles = svgG.selectAll("circle")
						.data(data)
						.enter()
						.append("circle");
		
		
		var circleAttr = circles.attr("cx", function(d){return xScale(d.x);})
								.attr("cy", function(d){return yScale(d.y);})
								.attr("r", function(d){return (d.year)*10;})
								.style("fill", function(d, i){
									if(i%2 == 1){
										return fkColorSet["blank"]["50"];
									}
									else{
										return fkColorSet[classification]["100"];
									}
								});
		
		data.sort(function(a,b){
			if(a.year > b.year)
				return 1;
			else if(a.year < b.year)
				return -1;
			return 0;
		})
		
		var curvedLine = d3.line()
							.curve(d3.curveCardinal)
							.x(function(d){return xScale(d.x);})
							.y(function(d){return yScale(d.y);});
		
		svgG.append("path")
			.data([data])
			.attr("class", "line")
			.attr("d", curvedLine(data));
	})
};