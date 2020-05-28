<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="courses">

    <h2>Course Information</h2>


    <table class="table table-striped">
    	<tr>
            <th>Title</th>
            <td><c:out value="${course.name}"/></td>
        </tr>
        <tr>
            <th>Dangerous pet</th>
            <td><c:out value="${course.dangerousAllowed}"/></td>
        </tr>
        <tr>
            <th>Pet type</th>
            <td><c:out value="${course.petType}"/></td>
        </tr>
        <tr>
            <th>Start Date</th>
            <td><c:out value="${course.startDate}"/></td>
        </tr>
        <tr>
            <th>Finish Date</th>
            <td><c:out value="${course.finishDate}"/></td>
        </tr>
        <tr>
            <th>Capacity</th>
            <td><c:out value="${course.capacity}"/></td>
        </tr>
        <tr>
            <th>Trainer</th>
            <td><c:out value="${course.trainer.name}"/></td>
        </tr>
        <tr>
            <th>Cost</th>
            <td><c:out value="${course.cost}"/></td>
        </tr>
    </table>
    <spring:url value="/courses/{courseId}/inscription/new" var="inscriptionCreateUrl">
    	<spring:param name="courseId" value="${course.id}"/>
    </spring:url> 
    <a href="${fn:escapeXml(inscriptionCreateUrl)}">Create Inscription</a> 

</petclinic:layout>