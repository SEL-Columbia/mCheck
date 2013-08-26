<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Registered Mothers</title>
    <link href="../../css/bootstrap.css" rel="stylesheet">
</head>

<body style="padding-top: 40px;padding-bottom: 40px;">
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="#">mCheck</a>
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <h2>Registered Mothers</h2>
            <table class="table table-bordered table-condensed">
                <thead style="background-color: #08C; color: #ffffff; text-decoration: none;">
                <tr>
                    <th>#</th>
                    <th style="">Mother Name</th>
                    <th>FormHub ID</th>
                    <th>Phone No.</th>
                    <th>Registration Date</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${mothers}" var="mother">
                    <tr>
                        <td>${mothers.indexOf(mother) + 1}</td>
                        <td>${mother.name()}</td>
                        <td>${mother.formHubID()}</td>
                        <td>${mother.contactNumber()}</td>
                        <td>${mother.registrationDate()}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>