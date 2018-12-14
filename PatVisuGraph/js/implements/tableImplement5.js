/**tableImplement5.js
 **Author : YeongWoo Kim
 **Html5 Table Implementation in javascript(hide)
 */

var dataTable_Sheet5 = function(divName, jsonName){
		$.getJSON('.//'+jsonName+'.json', function(json, error){
			var tableData = new Array();
			var nations = new Array();
			var nationData = new Array(), perNationData = new Array();
			var i = 0, cntNation = 0;
			var j = 0;
			for(var d in json){
				if(i == 0){
					cntNation = json[d]["cntNations"];
				}
				else if(i == 1){
					for(var n in json[i]){
						nations[j] = json[i][n];
						j++;
					}
					$('#'+divName)
					.append('<tr>')
						.append('<th rowspan = "3">출원인</th>')
						.append('<th rowspan = "3">출원인<br>국적</th>')
						.append('<th colspan = "' +(cntNation+1)+'">주요 IP 시장국(건수, %)</th>')
						.append('<th rowspan = "3">특허출원<br>증가율<br>(최근 5년)</th>')
						.append('<th rowspan = "3">주력기술분야<br><br>(중분류)</th>')
						.append('<th rowspan = "3">주력기술분야<br><br>(소분류)</th>')
					.append('</tr>');
					$('#'+divName)
					.append('<tr>');
					for(j = 0;j < cntNation;j++){
						$('#'+divName).append('<th>'+nations[j]+'</th>');
					}
					$('#'+divName).append('<th>IP시장국<br>종합</th>');
					$('#'+divName).append('</tr>');
				}
				else{
					nationData[i-2] = new Array();
					tableData[i-2] = new Array();
					perNationData[i-2] = new Array();
					tableData[i-2].push({
						CompanyName: json[d]["CompanyName"],
						AppNation: json[d]["AppNation"],
						TotalNation: json[d]["TotalNation"],
						midClassification: json[d]["midClassification"],
						smallClassification: json[d]["smallClassification"],
						RecentPatent: json[d]["RecentPatent"]
					})
					for(var j = 0;j < cntNation;j++){
						var str = "per"+nations[j];
						nationData[i-2][j] = json[d][nations[j]];
						perNationData[i-2][j] = json[d][str];
					}
				}
				i++;
			}

			
			for(var j = 0;j < i - 2;j++){
				$('#'+divName)
				.append('<tr>')
				.append('<td rowspan="2">'+tableData[j][0]["CompanyName"]+'</td>')
				.append('<td rowspan="2">'+tableData[j][0]["AppNation"]+'</td>');
				for(var k = 0;k < cntNation;k++){
					$('#'+divName).
					append('<td>'+nationData[j][k]+'<br/>'+perNationData[j][k]+"%"+'</td>');
				}
				$('#'+divName)
				.append('<td rowspan="2">'+tableData[j][0]["TotalNation"]+'</td>')
				.append('<td rowspan="2">'+tableData[j][0]["RecentPatent"]+"%"+'</td>')
				.append('<td rowspan="2">'+tableData[j][0]["midClassification"]+'</td>')
				.append('<td rowspan="2">'+tableData[j][0]["smallClassification"]+'</td>')
				.append('</tr>');
			}
	})
}