<%--
  Created by IntelliJ IDEA.
  User: zjtpd
  Date: 2021-02-05
  Time: 오후 6:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.infranics.demo.cloudfoundry.model.JpaScreeningClinic"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>Object-Stroge Test</title>
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
<div id="tmp" style="width:48%; float:left" ></div>
<div id="tmp2" style="width:48%; float: right"></div>
<script type="text/javascript" language="JavaScript">

    <%
		ArrayList alr = (ArrayList)request.getAttribute("list");
%>

    window.onload = function () {


        var tmp = "";
        tmp += "<table id=\"grid\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
        tmp += "	<tr>";
        tmp += "		<th>유저 프로바이드 서비스 목록</th>";
        tmp += "		<th>상세보기</th>";
        tmp += "	</tr>";

        <%
                    for (int i = 0; i < alr.size(); i++) {
                        %>
        tmp += "	<tr>";
        tmp += "		<td><%=alr.get(i)%></td>";
        tmp += "		<td align='center'><button onClick='goMap(\"<%=alr.get(i)%>\")' class='btn btn-info button-height'>상세</button></td>";
        tmp += "	</tr>";
        <%
                    }
        %>
        tmp += "</table>";
        $("#tmp").html(tmp);
    }


    function goMap(service_name) {
        $.ajax({
            url: "/user-provide/" + service_name,
            method: "GET",
            data: null,
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function(data) {
               console.log(data);

                var tmp2 = "";
                tmp2 += "<table id=\"grid2\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
                tmp2 += "	<tr>";
                tmp2 += "		<th>KEY</th>";
                tmp2 += "		<th>VALUE</th>";
                tmp2 += "	</tr>";
                for(var key in data) {
                    tmp2 += "	<tr>";
                    tmp2 += "		<td>"+key+"</td>";
                    if (key.toString() === 'credentials'){
                        tmp2 += "<td>";
                        tmp2 += "   <table class=\"table table-striped table-bordered\" >";
                        tmp2 += "       <tr>";
                        tmp2 += "           <th>KEY</th>";
                        tmp2 += "           <th>VALUE</th>";
                        tmp2 += "       </tr>";
                        for(var c_key in data[key]){
                            var c_data = data[key];
                            tmp2 += "	<tr>";
                            tmp2 += "		<td>"+c_key+"</td>";
                            tmp2 += "		<td>"+c_data[c_key]+"</td>";
                            tmp2 += "	</tr>";
                        }
                        tmp2 += "   </table>";
                        tmp2 += "</td>";
                    }else{
                    tmp2 += "		<td>"+data[key]+"</td>";
                    }
                    tmp2 += "	</tr>";

                }
                tmp2 += "</table>";
                $("#tmp2").html(tmp2);

            },
            complete : function(data) {

            }
        });
    }
</script>


</body>
</html>
