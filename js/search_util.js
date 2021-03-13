/* 시/도 목록 가져오기 */
data = [];
function load_si_data() {
	$.ajax({
		url : "data/code.txt",
		type : "GET",
		async : false,
		success : function(response) {
			data = response.split('\n');
			data.splice(0,1); // 첫번째 목록 데이터 제거
			$.each(data,function(index,item) {
				item_list = item.split('\t');

				if(item_list[2].trim()=='존재') {
					var item_name_list = item_list[1].split(' ');
					if(item_name_list.length==1) {
						$("#si").append("<option value='" + item_list[1] +"'>" + item_list[1] + "</option>");
					}
				}
			});
		},
		error : function() {
			console.log('data load error');
		}
	});
}

/* 시/도 버튼 변경시 구 데이터 불러오기 */
function load_gu_data() {
	// 구
		init_gu();
		init_dong();

		$.each(data,function(index,item) {
			var item_list = item.split('\t');
			if(item_list[2].trim()=='존재') {
				var item_name_list = item_list[1].split(' ');
				if(item_name_list[0] == $("select[name='si']").val() && item_name_list.length==2) {
					$("#gu").append("<option value='" + item_name_list[1] +"'>"+ item_name_list[1] + "</option>");
				} 
			}
		});
}

/* 구 버튼 변경시 동 데이터 가져오기 */
function load_dong_data() {
		init_dong();

		$.each(data,function(index,item) {
			var item_list = item.split('\t');
			if(item_list[2].trim()=='존재') {
				var item_name_list = item_list[1].split(' ');
				if(item_name_list[0] == $("select[name='si']").val() && item_name_list[1] == $("select[name='gu']").val() && item_name_list.length==3 ) {
					$("#dong").append("<option value='" + item_name_list[2] + "'>" + item_name_list[2] + "</option>");
				}
			}
		});
}

/* 구 옵션 초기화 */
function init_gu() {
	$("#gu").empty();
	$("#gu").append('<option name="gu_init" value="">시/군/구</option>');
}

/* 동 옵션 초기화 */
function init_dong() {
	$("#dong").empty();
	$("#dong").append('<option name="dong_init" value="">읍/면/동</option>');
}

/* 아파트 데이터 서칭 */
function apart_search(si,gu,dong,str) {
	var code=0;

	$.each(data,function(index,item) {
		var item_list = item.split('\t');
		if(item_list[2].trim()=='존재') {
			if(item_list[1]==str) {
				code = item_list[0];
			}
		}
	});

	$.ajax({
		url : 'apartment.jsp',
		type : 'post',
		data : "code="+code,
		success : function(response) {
			var arr = response.split('\n');
			$.each(arr,function(index,item) {
				if(item.trim()!="") {
					var item_list = item.split("|");
					google_geocoding(si,gu,dong,item_list[0],item_list[3],0);
					var $div = $('<div class="item"></div>');
					$('<p class="item-name">' + '아파트명 : ' +item_list[3] +'</p>').appendTo($div);
					$('<p class="item-money">' + '가격 : ' + item_list[1] + '</p>').appendTo($div);
					$('<p class="item-square">' + '토지면적 : ' + item_list[2] + '</p>').appendTo($div);
					$('<p class="item-category">' + '거래구분 : 아파트' + '</p>').appendTo($div);
					$div.appendTo(".content-left");
				}
			});
		},
		error : function() {
			console.log('apertment.jsp load error!');
		}
	});
}

/* 다세대주택 서칭 */
function housing_search(si,gu,dong,str) {
	var code=0;

	$.each(data,function(index,item) {
		var item_list = item.split('\t');
		if(item_list[2].trim()=='존재') {
			if(item_list[1]==str) {
				code = item_list[0];
			}
		}
	});

	$.ajax({
		url : 'housing.jsp',
		type : 'post',
		data : "code="+code + "&dong=" + dong,
		success : function(response) {
			var arr = response.split('\n');
			$.each(arr,function(index,item) {
				if(item.trim()!="") {
					var item_list = item.split("|");
					google_geocoding(si,gu,dong,item_list[0],item_list[3],1);
					var $div = $('<div class="item"></div>');
					$('<p class="item-name">' + '연립다세대 : ' +item_list[3] +'</p>').appendTo($div);
					$('<p class="item-money">' + '가격 : ' + item_list[1] + '</p>').appendTo($div);
					$('<p class="item-square">' + '토지면적 : ' + item_list[2] + '</p>').appendTo($div);
					$('<p class="item-category">' + '거래구분 : 연립다세대' + '</p>').appendTo($div);
					$div.appendTo(".content-left");
				}
			});
		},
		error : function() {
			console.log('apertment.jsp load error!');
		}
	});
}