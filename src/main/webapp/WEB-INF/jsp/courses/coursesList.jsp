<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="courses">
    <h2>Courses</h2>

    <c:choose>
    	<c:when test="${!isempty}">


		    <table id="coursesTable" class="table table-striped">
		        <thead>
		        <tr>
		            <th>Title</th>
		            <th>Pet Type</th>
		            <th>Capacity</th>
		            <th>Dangerous Pets</th>
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach items="${courses}" var="course">
		            <tr>
						<td>
		                    <spring:url value="/courses/{courseId}" var="courseUrl">
		                        <spring:param name="courseId" value="${course.id}"/>
		                    </spring:url>
		                    <a href="${fn:escapeXml(courseUrl)}"><c:out value="${course.name}"/></a>
		                </td>
		                <td>
		                    <c:out value="${course.petType}"/>
		                </td>
		                <td>
		                    <c:out value="${course.capacity}"/>
		                </td>
						<td>
		                    <c:out value="${course.dangerousAllowed}"/>
		                </td>              
		            </tr>
		        </c:forEach>
		        </tbody>
		    </table>
		</c:when>    
    <c:otherwise>
        There are not courses yet.
    </c:otherwise>
</c:choose>
    
    <spring:url value="/inscriptions/" var="inscriptionsUrl">
    </spring:url>
    <a href="${fn:escapeXml(inscriptionsUrl)}" class="btn btn-default">My Inscriptions</a>
    
</petclinic:layout>
