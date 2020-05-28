<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="announcements">
    <h2>Announcements</h2>

    <table id="announcementsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Title</th>
            <th>Pet Name</th>
            <th>Can be adopted</th>
            <th>Type</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${announcements}" var="announcement">
            <tr>
				<td>
                    <spring:url value="/announcements/{announcementId}" var="announcementUrl">
                        <spring:param name="announcementId" value="${announcement.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(announcementUrl)}"><c:out value="${announcement.name}"/></a>
                </td>
                <td>
                    <c:out value="${announcement.petName}"/>
                </td>
                <td>
                    <c:out value="${announcement.canBeAdopted}"/>
                </td>
				<td>
                    <c:out value="${announcement.type}"/>
                </td>              
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${!isanonymoususer}">
    
	    <spring:url value="/announcements/new" var="addUrl">
	    </spring:url>
	    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Announcement</a>
    
    </c:if>
</petclinic:layout>
