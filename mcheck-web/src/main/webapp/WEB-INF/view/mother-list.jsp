<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>All registered mothers</title>
    <link href="../../css/bootstrap.css" rel="stylesheet">
</head>

<body>
<div class="container-fluid">
    <h2>All registered mothers</h2>
    <table class="table table-bordered table-condensed">
        <thead style="background-color: #08C; color: #ffffff; text-decoration: none;">
        <tr>
            <th>#</th>
            <th>Mother Name</th>
            <th>FormHub ID</th>
            <th>Phone No.</th>
            <th>Has Problem Bleeding</th>
            <th>Has Fever</th>
            <th>Has Vaginal Discharge</th>
            <th>Has Problem Breathing</th>
            <th>Has Painful Urination</th>
            <th>Has Headache</th>
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
                <td>${mother.hasBleeding()}</td>
                <td>${mother.hasFever()}</td>
                <td>${mother.hasVaginalDischarge()}</td>
                <td>${mother.hasProblemBreathing()}</td>
                <td>${mother.hasPainfulUrination()}</td>
                <td>${mother.hasHeadache()}</td>
                <td>${mother.registrationDate()}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%--
<c:forEach items="${counties}" var="county">
            <option value="<c:out value='${county}'/>"><c:out value='${county}'/></option>
        </c:forEach>
--%>
</body>
</html>