/**PositioningBubbleChart10.js
 **Author : Yeong Woo Kim
 **Positioning Bubble grid chart
 */

var PositioningBubbleChart_Sheet10 = function(svgName, jsonName){
	var nationData = new Array(), classData = new Array();
	var nationSize, classSize;
	var dataset = [];
	/*
	var colorDataSet = new Array();
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
		var i = 0;
		for(var d in json){
			switch(i){
			case 0:
				nationSize = json[d]["NationSize"];
				j = 0;
				for(var n in json[i]){
					if(json[d]["NationSize"] != json[i][n]) nationData[j++] = json[i][n];
				}
				break;
			case 1:
				classSize = json[d]["ClassSize"];
				j = 0;
				for(var n in json[i]){
					if(json[d]["ClassSize"] != json[i][n])classData[j++] = json[i][n];
				}
				break;
			default:
				dataset.push({
					"Classification": json[d]["Classification"],
					"NationCode": json[d]["NationCode"],
					"Value": json[d]["Value"],
					"x": (nationData.indexOf(json[d]["NationCode"])+1),
					"y": (classData.indexOf(json[d]["Classification"])+1)
				});
				break;
			}
			i++;
		}
		
		var svg = d3.select('#'+svgName);
		var width = parseInt(svg.style("width")) - 30;
		var height = parseInt(svg.style("height")) - 30;
		
		var svgG = svg.append("g")
						.attr("transform", "translate(30,0)");
		
		//maximum 범위 추가
		//체크 격자를 추가 하기 위해서 x와 y의 interval 을 추가
		var maxX = d3.max(dataset, function(d){return d.x;}) + 1;
		var maxY = d3.max(dataset, function(d){return d.y;}) + 1;
		var maxValue = d3.max(dataset, function(d){return d.Value;});		//value 중 max인 값을 찾아서 itvX와 itvY 를 할 때 중간 값을 최대로 잡아야 함\
		
		var itvX = parseInt(width / maxX), itvY = parseInt(height / maxY);
		var gridWidthX = itvX * maxX, gridHeightY = itvY * maxY;
		var itvMin = Math.min(itvX, itvY);
		var maxR = (itvMin / 2 * 0.95);
		
		var gridData = svgG;
		
		for(i = 0;i <= gridWidthX;i+=itvX){
			gridData.append("line")
					.attr("x1", i)
					.attr("y1", 0)
					.attr("x2", i)
					.attr("y2", gridHeightY)
					.style("stroke", "rgb(0, 0, 0)")
			        .style("stroke-width", 2);
		}
		for(i = 0;i <= gridHeightY;i+=itvY){
			gridData.append("line")
					.attr("x1", 0)
					.attr("y1", i)
					.attr("x2", gridWidthX)
					.attr("y2", i)
					.style("stroke", "rgb(0, 0, 0)")
			        .style("stroke-width", 2);
		}
		
		for(i=itvY;i<gridHeightY;i+=itvY){
			svg.append("rect")
				.attr("x", 0)
				.attr("y", i-(itvY/3))
				.attr("width", itvX * 2 / 3)
				.attr("height", itvY/3 * 2)
				.style("stroke-width", 2)
				.style("stroke", 'black')
				.attr("fill", "white");
		}
		
		for(i=itvX;i<gridWidthX;i+=itvX){
			svg.append("rect")
				.attr("x", i-(itvX/4))
				.attr("y", gridHeightY - (itvY/3))
				.attr("width", itvX/4 + itvX/2)
				.attr("height", itvY/3 * 2)
				.style("stroke-width", 2)
				.style("stroke", 'black')
				.attr("fill", "white");
		}
		
		var rectLeftValues = svg.selectAll("text.rectLeftValues")
								.data(nationData)
								.enter().append("text")
								.attr("class", "rectLeftValues")
								.attr("x", 30)
								.attr("y", function(d, i){
									return i * itvY + itvY * 0.97;
								})
								.attr("dy","0.5em")
								.attr("font-family", "sans-serif")
								.attr("font-size", "13px")
								.attr("font-weight", "bold")
								.text(function(d, i){
									return classData[i];
								})
								
		var rectRightValues = svg.selectAll("text.rectRightValues")
								.data(nationData)
								.enter().append("text")
								.attr("class", "rectRightValues")
								.attr("x", function(d, i){
									return i * itvX + itvX * 0.97 + 12;
								})
								.attr("y", gridHeightY)
								.attr("dy","0.5em")
								.attr("font-family", "sans-serif")
								.attr("font-size", "13px")
								.attr("font-weight", "bold")
								.text(function(d, i){
									return nationData[i];
								})
		
		var circles = svgG.selectAll("circle")
							.data(dataset)
							.enter()
							.append("circle");

		var circleAttr = circles.attr("cx", function(d){return d.x*itvX;})
						.attr("cy", function(d){return d.y*itvY;})
						.attr("r", function(d){
							if((maxR * d.Value) / maxValue >= 10) return (maxR * d.Value) / maxValue;
							else return 10;
						})
						.style("fill", function(d, i){
							if((d.x + d.y) % 2 == 0){
								return fkColorSet[d.Classification]["50"];
							}
							else{
								return fkColorSet["blank"]["50"];
							}
						});
		
		var cirValue = svgG.selectAll("text.values")
						.data(dataset)
						.enter().append("text")
						.attr("class", "values")
						.attr("x", function(d){
							return d.x * itvX - itvX * 0.05;
						})
						.attr("y", function(d){
							return d.y * itvY - itvY * 0.05;
						})
						.attr("dy","0.5em")
						.attr("font-family", "sans-serif")
						.attr("font-size", "13px")
						.attr("font-weight", "bold")
						.text(function(d, i){
							return dataset[i].Value;
						})
	})
};