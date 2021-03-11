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
    .label {
        display: inline-block;
        padding: .5em .75em;
        color: #999;
        font-size: inherit;
        line-height: normal;
        vertical-align: middle;
        background-color: #fdfdfd;
        cursor: pointer;
        border: 1px solid #ebebeb;
        border-bottom-color: #e2e2e2;
        border-radius: .25em;
    }
    .input[type="file"] {
        position: absolute;
        width: 1px;
        height: 1px;
        padding: 0;
        margin: -1px;
        overflow: hidden;
        clip:rect(0,0,0,0);
        border: 0;
    }

</style>
<body>
    <div style="text-align: center; padding-top: 50px;">
        <h2>
            스토리지 서비스 연결 및 업로드 테스트
            <div id="content44" style="text-align: right">
                <button type="button" onclick="moveProvide()" class="btn btn-info button-height">유저 프로바이드 서비스 테스트 화면</button>
                <button type="button" onclick="moveCovid19()" class="btn btn-info button-height">무료 선별 진료소 화면</button>
            </div>
        </h2>
    </div>
    <br />

<div id="tmp" style="width:48%; float:left" >
    <table id='grid' class="table table-striped table-bordered" style="width:98%;">
        <catpion>
            스토리지 서비스 접속 정보

        </catpion>
        <tr>
            <th>AUTH_URL</th>
            <td>
                <input type="text" id="auth_url" size="70">
            </td>
            </td>
        </tr>
        <tr>
            <th>아이디</th>
            <td>
                <input type="text" id="id" size="70">
            </td>
            </td>
        </tr>
        <tr>
            <th>비밀번호</th>
            <td>
                <input type="text" id="password" size="70">
            </td>
            </td>
        </tr>
        <tr>
            <th>
                <button type="button" onclick="init_storage()" class="btn btn-info button-height">연결</button>
            </th>
            <td>
                <input type="checkbox" id="login" disabled="true">
            </td>
            </td>
        </tr>
    </table>

</div>
<div id="tmp2" style="width:48%; float: right"></div>
    <div id="tmp3" style="width:100%; float: left"></div>
    <div id="tmp4" style="width:100%; float: right"></div>
<script type="text/javascript" language="JavaScript">
    function moveCovid19() {
        window.location.href = window.location.origin + '/index';
    }
    function moveProvide() {
        window.location.href = window.location.origin + '/user-provide';
    }

    window.onload = function () {

    }


    function init_storage() {
        var param = {
            password : $('#password').val(),
            authUrl : $('#auth_url').val(),
            username : $('#id').val()
        };
        $.ajax({
            url: "/storage",
            method: "POST",
            data: JSON.stringify(param),
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function(data) {
                $("input:checkbox[id='login']").prop("checked",true);
                list_storage();

            },
            error: function(data){
                $("input:checkbox[id='login']").prop("checked",false);
            },
            complete : function(data) {

            }
        });
    }

    function list_storage() {
        $.ajax({
            url: "/storage/containers",
            method: "GET",
            data: null,
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function(data) {
                console.log(data);
                var tmp2 = "";
                tmp2 += "<table id=\"grid2\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
                tmp2 += "<catpion>컨테이너 리스트 <input type=\"text\" id=\"container_new_name\" size=\"40\"> <button onClick='createContainer()' class='btn btn-info button-height'>생성하기</button></catpion>"
                tmp2 += "	<tr>";
                tmp2 += "		<th>Container name</th>";
                tmp2 += "		<th>상세정보</th>";
                tmp2 += "		<th>-</th>";
                tmp2 += "	</tr>";
                for(var key in data['result']) {
                    tmp2 += "	<tr>";
                    tmp2 += "		<td>"+data['result'][key]+"</td>";
                    tmp2 += "		<td align='center'><button onClick='listObject(\""+data['result'][key]+"\")' class='btn btn-info button-height'>열기</button></td>";
                    tmp2 += "		<td align='center'><button onClick='deleteContainer(\""+data['result'][key]+"\")' class='btn btn-info button-height'>제거하기</button></td>";
                    tmp2 += "	</tr>";
                }
                tmp2 += "</table>";
                $("#tmp2").html(tmp2);
                $("#tmp3").html("");
                $("#tmp4").html("");
            },
            error: function(data){
                console.log(data);
            },
            complete : function(data) {

            }
        });
    }

    function createContainer(){
        var param = {
            name : $('#container_new_name').val(),
        };
        $.ajax({
            url: "/storage/container",
            method: "POST",
            data: JSON.stringify(param),
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function(data) {
            },error: function(data){
            },complete : function(data) {
                list_storage();
            }
        });
    }

    function deleteContainer(name){

        $.ajax({
            url: "/storage/container/"+name,
            method: "DELETE",
            data: null,
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function(data) {
            },error: function(data){
            },complete : function(data) {
                list_storage();
            }
        });
    }

    /////////////////////////////////////////
    function listObject(name) {
        $.ajax({
            url: "/storage/container/"+name+"/objects",
            method: "GET",
            data: null,
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function(data) {

                var tmp3 = "";
                tmp3 += "<table id=\"grid2\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
                tmp3 += "<catpion>오브젝트 리스트 <label for=\"create_object\">업로드</label><input type=\"file\" id=\"create_object\" size=\"40\"> <button onClick='createObject()' class='btn btn-info button-height'>파일업로드</button></catpion>"
                tmp3 += "	<tr>";
                tmp3 += "		<th>Object name</th>";
                tmp3 += "		<th>상세정보</th>";
                tmp3 += "		<th>-</th>";
                tmp3 += "	</tr>";
                for(var key in data['result']) {
                    tmp3 += "	<tr>";
                    tmp3 += "		<td>"+data['result'][key]+"</td>";
                    tmp3 += "		<td align='center'><button onClick='summaryObject(\""+data['result'][key]+"\")' class='btn btn-info button-height'>열기</button></td>";
                    tmp3 += "		<td align='center'><button onClick='deleteObject(\""+data['result'][key]+"\")' class='btn btn-info button-height'>삭제</button></td>";
                    tmp3 += "	</tr>";
                }
                tmp3 += "</table>";
                $("#tmp3").html(tmp3);
                $("#tmp4").html("");

            },
            error: function(data){
                console.log(data);
            },
            complete : function(data) {

            }
        });
    }


    function summaryObject(object) {
        var param = {
            name : object
        };
        $.ajax({
            url: "/storage/container/object/",
            method: "PUT",
            data:  JSON.stringify(param),
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function(data) {
                var tmp4 = "";
                tmp4 += "<table id=\"grid2\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
                tmp4 += "<catpion>오브젝트 상세</catpion>"
                tmp4 += "	<tr>";
                tmp4 += "		<th>Object name</th>";
                tmp4 += "		<th>Public Url</th>";
                tmp4 += "		<th>Type</th>";
                tmp4 += "		<th>용량</th>";
                tmp4 += "	</tr>";
                tmp4 += "	<tr>";
                tmp4 += "		<td>"+data['name']+"</td>";
                // tmp4 += "		<td>"+data['public_url']+"</td>";
                tmp4 += "		<td><a href=\"javascript:void(0);\" onclick='Download(\""+data['name']+"\")'>"+data['public_url']+"</a></td>";
                tmp4 += "		<td>"+data['type']+"</td>";
                tmp4 += "		<td>"+data['length']+"</td>";
                tmp4 += "	</tr>";
                tmp4 += "</table>";
                $("#tmp4").html(tmp4);

            },
            error: function(data){
                console.log(data);
            },
            complete : function(data) {

            }
        });
    }

    function createObject() {
        let file = $("#create_object")[0].files[0];
        let formData = new FormData();
        formData.append("file", file);
        $.ajax({
            url: "/storage/container/object",
            method: "POST",
            data: formData,
            dataType: 'json',
            processData : false,
            contentType: false,
            success: function (data) {
                var tmp3 = "";
                tmp3 += "<table id=\"grid2\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
                tmp3 += "<catpion>오브젝트 리스트 <label for=\"create_object\">업로드</label><input type=\"file\" id=\"create_object\" size=\"40\"> <button onClick='createObject()' class='btn btn-info button-height'>파일업로드</button></catpion>"
                tmp3 += "	<tr>";
                tmp3 += "		<th>Object name</th>";
                tmp3 += "		<th>상세정보</th>";
                tmp3 += "		<th>-</th>";
                tmp3 += "	</tr>";
                for(var key in data['result']) {
                    tmp3 += "	<tr>";
                    tmp3 += "		<td>"+data['result'][key]+"</td>";
                    tmp3 += "		<td align='center'><button onClick='summaryObject(\""+data['result'][key]+"\")' class='btn btn-info button-height'>열기</button></td>";
                    tmp3 += "		<td align='center'><button onClick='deleteObject(\""+data['result'][key]+"\")' class='btn btn-info button-height'>삭제</button></td>";
                    tmp3 += "	</tr>";
                }
                tmp3 += "</table>";
                $("#tmp3").html(tmp3);
            }, error: function (xhr, status, error) {

            }, complete: function (data) {

            }
        });
    }
    function Download(name) {
        console.log(name);
        var reqDownloadUrl = "/download/url";
        reqDownloadUrl += "?name=" + name;
        reqDownloadUrl = encodeURI(reqDownloadUrl);

        location.href = reqDownloadUrl;
    }
    function deleteObject(object) {
        var param = {
            name : object
        };
        $.ajax({
            url: "/storage/container/object",
            method: "DELETE",
            data:  JSON.stringify(param),
            dataType: 'json',
            //async: false,
            contentType: "application/json",
            success: function (data) {
                var tmp3 = "";
                tmp3 += "<table id=\"grid2\" class=\"table table-striped table-bordered\" style=\"width:98%;\">";
                tmp3 += "<catpion>오브젝트 리스트 <label for=\"create_object\">업로드</label><input type=\"file\" id=\"create_object\" size=\"40\"> <button onClick='createObject()' class='btn btn-info button-height'>파일업로드</button></catpion>"
                tmp3 += "	<tr>";
                tmp3 += "		<th>Object name</th>";
                tmp3 += "		<th>상세정보</th>";
                tmp3 += "	</tr>";
                for(var key in data['result']) {
                    tmp3 += "	<tr>";
                    tmp3 += "		<td>"+data['result'][key]+"</td>";
                    tmp3 += "		<td align='center'><button onClick='summaryObject(\""+data['result'][key]+"\")' class='btn btn-info button-height'>열기</button></td>";
                    tmp3 += "		<td align='center'><button onClick='deleteObject(\""+data['result'][key]+"\")' class='btn btn-info button-height'>삭제</button></td>";
                    tmp3 += "	</tr>";
                }
                tmp3 += "</table>";
                $("#tmp3").html(tmp3);
            }, error: function (xhr, status, error) {

            }, complete: function (data) {

            }
        });
    }




</script>


</body>
</html>
