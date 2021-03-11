<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.infranics.demo.cloudfoundry.model.JpaScreeningClinic"%>
<!DOCTYPE html>
<head>
<meta charset="UTF-8">
<title>코로나19 선별진료소 리스트</title>
<script src="../../resources/js/jquery-2.2.4.min.js"></script>
<link rel="stylesheet"
	href="../../resources/js/bootstrap.min.css">

</head>

<style>
.button {
	background-color: #4CAF50; /* Green */
	border: none;
	color: white;
	padding: 15px 32px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
}
</style>

<body>
	<div style="text-align: center; padding-top: 50px;">
		<h2>
			코로나19 선별진료소 리스트
			<div id="content44" style="text-align: right">
				<button type="button" onclick="moveProvide()" class="btn btn-info button-height">유저 프로바이드 서비스 테스트 화면</button>
				<button type="button" onclick="moveStorage()" class="btn btn-info button-height">스토리지 서비스 테스트 화면</button>
			</div>
			</h2>
	</div>

		<br />
	<div id="content" style="text-align: center">
		<div style="margin-bottom: 1rem;">
			<select
				style="height: calc(1.5em + .75rem + 2px); border: 1px solid #ced4da;"
				id="sel_option">
				<option value="0">-SELECT-</option>
				<option value="1">시도명</option>
				<option value="2">시군구명</option>
				<option value="3">기관명</option>
			</select>
			<input
				type="text" id="keyword" class="form-control"
				style="width: 50%; display: initial; text-align: center" />
			<button type="button" onclick="search()"
				class="btn btn-info button-height">검색</button>
		</div>

	</div>

	<div id="tmp" style=""></div>
	<!-- 
	<table border="1">
		<tr>
			<th>시도명</th>
			<th>시군구명</th>
			<th>전화번호</th>
			<th>기관명</th>
		</tr>
	</table>
	 -->
	<script>

<%
		ArrayList<JpaScreeningClinic> al = (ArrayList<JpaScreeningClinic>)request.getAttribute("list");
%>
		window.onload = function () {
			
			var tmp = "";

			tmp += "<table id=\"grid\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
			tmp += "	<tr>";
			tmp += "		<th>시도명</th>";
			tmp += "		<th>시군구명</th>";
			tmp += "		<th>전화번호</th>";
			tmp += "		<th>기관명</th>";
			tmp += "		<th>-</th>";
			tmp += "	</tr>";
<%
			for (int i = 0; i < al.size(); i++) {
				%>
				tmp += "	<tr>";
			    tmp += "		<td><%=al.get(i).getSidoNm() %></td>";
			    tmp += "		<td><%=al.get(i).getSgguNm()%></td>";
			    tmp += "		<td><%=al.get(i).getTelno() %></td>";
				tmp += "		<td><%=al.get(i).getYadmNm() %></td>";
				tmp += "		<td align='center'><button onClick='goMap(\"<%=al.get(i).getSidoNm() %>\",\"<%=al.get(i).getSgguNm()%>\",\"<%=al.get(i).getYadmNm() %>\")' class='btn btn-info button-height'>지도</button></td>";
				tmp += "	</tr>";
<%
			}
%>
			tmp += "</table>";
			$("#tmp").html(tmp);
		}

		function goMap(sidoNm, sgguNm, yadmNm) {
			var url = "https://www.mohw.go.kr/react/ncov_map_page.jsp?hospitalCd=03&region="
					+ sidoNm + "&town=" + sgguNm + "&hospitalNm=" + yadmNm;
			var win = window.open(url);
		}

		function moveStorage() {
			window.location.href = window.location.origin + '/storage';
		}

		function moveProvide() {
			window.location.href = window.location.origin + '/user-provide';
		}

		function search() {
			var optVal = $("#sel_option option:selected").val();
			var tmp = "";
			

			tmp += "<table id=\"grid\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
			tmp += "	<tr>";
			tmp += "		<th>시도명</th>";
			tmp += "		<th>시군구명</th>";
			tmp += "		<th>전화번호</th>";
			tmp += "		<th>기관명</th>";
			tmp += "		<th>-</th>";
			tmp += "	</tr>";
			let flag;
			let Sidoname;
			let Sgguname;
			let Yadmname;

<%
			for (int i = 0; i < al.size(); i++) {
				%>
			flag = true;
			Sidoname = "<%=al.get(i).getSidoNm()%>";
			Sgguname = "<%=al.get(i).getSgguNm()%>";
			Yadmname = "<%=al.get(i).getYadmNm()%>";
				if(optVal == 1 && (Sidoname.indexOf($("#keyword").val()) < 0)){
					flag = false;
				}else if(optVal == 2 && (Sgguname.indexOf($("#keyword").val()) < 0)){
					flag = false;
				}else if(optVal == 3 && (Yadmname.indexOf($("#keyword").val()) < 0)){
					flag = false;
				}
				if(flag){
					tmp += "	<tr>";
					tmp += "		<td><%=al.get(i).getSidoNm() %></td>";
					tmp += "		<td><%=al.get(i).getSgguNm()%></td>";
					tmp += "		<td><%=al.get(i).getTelno() %></td>";
					tmp += "		<td><%=al.get(i).getYadmNm() %></td>";
					tmp += "		<td align='center'><button onClick='goMap(\"<%=al.get(i).getSidoNm() %>\",\"<%=al.get(i).getSgguNm()%>\",\"<%=al.get(i).getYadmNm() %>\")' class='btn btn-info button-height'>지도</button></td>";
				}
			<%
			}
            %>
			tmp += "</table>";

			$("#tmp").html(tmp);
		}
	</script>


</body>
</html>
