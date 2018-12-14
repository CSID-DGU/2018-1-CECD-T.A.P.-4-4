/* PositiveNegativeBarChart6.js
**Author : YeongWoo Kim
**patent nation donut code here(hide)
*/
var PositiveNegativeBar_Sheet6 = function(divName, svgName, jsonName){
	var classification = new Array(), nationCode = new Array();		//구분 하기위한 배열
	var classDataSet = [], nationDataSet = [], /*colorDataSet = new Array()*/ companyDataSet = new Array();
	var fakeNation = new Array();
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
		var i = 0, classSize, nationSize;
		for(var d in json){
			j = 0;
			switch(i){
			case 0:
				nationSize = json[i]["NationSize"];
				for(var n in json[i]){
					if(json[i][n] != json[i]["NationSize"]) nationCode[j++] = json[i][n];
				}
				break;
			case 1:
				for(var n in json[i]){
					fakeNation[j++] = json[i][n];
				}
				break;
			case 2:
				classSize = json[i]["ClassSize"];
				for(var n in json[i]){
					if(json[i][n] != json[i]["ClassSize"]) classification[j++] = json[i][n];
				}
				break;
			default:
				companyDataSet[i-2] = json[d]["CompanyName"];
				for(var j = 0;j<classSize;j++){
					classDataSet.push({
						"SmallClass" : classification[j],
						"value": json[d]["Class"+classification[j]],
						"CompanyName": json[d]["CompanyName"]
					});
				}
				for(var j = 0;j<nationSize;j++){
					nationDataSet.push({
						"NationCode": nationCode[j],
						"value": json[d]["Nation"+nationCode[j]],
						"CompanyName": json[d]["CompanyName"]
					});
				}
				break;
			}
			i++;
		}
		
		var svg = d3.select('#'+svgName);
		var halfWidth = (parseInt(svg.style("width")) - 100) / 2;								//width / 2 를 통하여 좌우측으로 그래프
		var height, width;
		
		var company_labelArea = 300;
		var leftArea = 400,
			rightArea = 400;
		var right_offset = company_labelArea + leftArea;
		
		var chgDiv = $('#'+divName).attr("height", companyDataSet.length * 20.17);
		var chgSvg = $('#'+svgName).attr("height", companyDataSet.length * 20.15);
		
		height = (parseInt(svg.style("height")) - companyDataSet.length * 0.15);
		width = (parseInt(svg.style("width")));
		
		var svgG = svg.append("g")
						.attr("width", width)
						.attr("height", companyDataSet.length + 50)
						.attr("transform", "translate(30, 0)");
		
		var yScale = d3.scaleBand()
						.domain(companyDataSet)
						.range([0, height])
						.padding(20);
		
		var xScaleLeft = d3.scaleLinear()
							.domain([d3.max(nationDataSet, function(d, i){
								if(i%4 == 0){
									var total = 0;
									for(var j = 0;j<nationSize;j++){
										total += d["value"];
									}
									return total * 1.10;
								}
							}), 0]).nice()
							.range([0, leftArea]);
		
		var xScaleRight = d3.scaleLinear()
							.domain([0, d3.max(classDataSet, function(d, i){
								if(i%4 == 0){
									var total = 0;
									for(var j = 0;j < classSize;j++){
										total += d["value"]
									}
									return total * 1.10;
								}
							})]).nice()
							.range([right_offset, (right_offset + rightArea)]);
		
		var xAxisLeft = svg.append("g")
							.attr("class", "leftXAxis")
							.attr("width", leftArea)
							.attr("height", companyDataSet.length * 0.1)
							.attr("transform", "translate(30, "+companyDataSet.length * 20.005+")")
							.call(d3.axisBottom(xScaleLeft));
		
		var xAxisRight = svg.append("g")
							.attr("class", "rightXAxis")
							.attr("width", rightArea)
							.attr("height", companyDataSet.length * 0.1)
							.attr("x", right_offset)
							.attr("transform", "translate(30, "+companyDataSet.length*20.005+")")
							.call(d3.axisBottom(xScaleRight));
		
		var leftRect = svgG.selectAll("rect.leftRect")
							.data(nationDataSet)
							.enter().append("rect")
							.attr("class", "leftRect")
							.attr("width", function(d){
								return leftArea - xScaleLeft(d.value);
							})
							.attr("height", function(d,i){
								if(i<4){
									return yScale(nationDataSet[i+nationSize].CompanyName) - yScale(nationDataSet[i].CompanyName) - companyDataSet.length * 0.008;
								}
								else{
									return yScale(nationDataSet[i].CompanyName) - yScale(nationDataSet[i-nationSize].CompanyName) - companyDataSet.length * 0.008;
								}
							})
							.attr("x", function(d, i){
								if(i%4 == 0){
									return xScaleLeft(nationDataSet[i].value);
								}
								var val = 0, pos = 0;
								for(var j = 1;j < i%4; j++){
									val += nationDataSet[i-j].value;
								}
								pos += xScaleLeft(val + d.value);
								return pos;
							})
							.attr("y", function(d, i){
								if(i < 4) return yScale(nationDataSet[i].CompanyName) - companyDataSet.length * 0.015;
								return yScale(nationDataSet[i].CompanyName) - companyDataSet.length * 0.012;
							})
							.attr("fill", function(d, i){
								return fkColorSet[fakeNation[i%4]]["50"];
							});
		
		svgG.selectAll("text.leftValue")
				.data(nationDataSet)
				.enter().append("text")
				.attr("class", "leftValue")
				.attr("x", function(d, i){
					if(i%4 == 0){
						return xScaleLeft(nationDataSet[i].value * 0.5);
					}
					var val = 0, pos = 0;
					for(var j = 1;j < i%4; j++){
						val += nationDataSet[i-j].value;
					}
					pos += xScaleLeft(val + (d.value / 2)); //pos는 위치
					return pos;
				})
				.attr("y", function(d){
					return yScale(d.CompanyName);
				})
				.attr("dy", ".30em")
				.text(function(d){
					if(d.value != 0){
						return d.value;
					}
				});

		svgG.selectAll("text.name")
			.data(companyDataSet)
			.enter().append("text")
			.attr("class", "name")
			.attr("x", leftArea + 10)
			.attr("y", function(d, i){return yScale(d);})
			.attr("dy", ".30em")
			.attr("width", company_labelArea)
			.attr("text-anchor", "left")
			.style("font-size", "11.5px")
			.style("letter-spacing", "-1px")
			.style("word-spacing", "-2px")
			.style("font-weight", "bold")
			.style("overflow", "hidden")
			.style("text-overflow", "ellipsis")
			.style("white-space", "nowrap")
			.text(function(d){return d;});
		
		var rightRect = svgG.selectAll("rect.rightRect")
							.data(classDataSet)
							.enter().append("rect")
							.attr("class", "rightRect")
							.attr("width", function(d){
								return xScaleRight(d.value) - right_offset;
							})
							.attr("height", function(d, i){
								if(i<4){
									return yScale(classDataSet[i+classSize].CompanyName) - yScale(classDataSet[i].CompanyName) - companyDataSet.length * 0.008;
								}
								else{
									return yScale(classDataSet[i].CompanyName) - yScale(classDataSet[i-classSize].CompanyName) - companyDataSet.length * 0.008;
								}
							})
							.attr("x", function(d, i){
								if(i == 0 || classDataSet[i-1].CompanyName != d.CompanyName){
									return right_offset;
								}
								var val = 0, pos = 0;
								for(var j = 1;j < i%4; j++){
									val += classDataSet[i-j].value;
								}
								pos += xScaleRight(val);
								return pos;
							})
							.attr("y", function(d, i){
								if(i < 4) return yScale(classDataSet[i].CompanyName) - companyDataSet.length * 0.015;
								else return yScale(classDataSet[i].CompanyName) - companyDataSet.length * 0.012;
							})
							.attr("fill", function(d, i){
								return fkColorSet[classDataSet[i%4].SmallClass]["100"];
							});
		
		svgG.selectAll("text.rightValue")
			.data(classDataSet)
			.enter().append("text")
			.attr("class", "rightValue")
			.attr("x", function(d, i){
				if(i%4 == 0){
					return xScaleRight(classDataSet[i].value * 0.5);
				}
				var val = 0, pos = 0;
				for(var j = 1;j < i%4; j++){
					val += classDataSet[i-j].value;
				}
				pos += xScaleRight(val + (d.value / 2)); //pos는 위치
				return pos;
			})
			.attr("y", function(d){
				return yScale(d.CompanyName);
			})
			.attr("dy", ".30em")
			.text(function(d){
				if(d.value != 0){
					return d.value;
				}
			});
	})
};