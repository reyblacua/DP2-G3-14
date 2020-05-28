<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="answers">
    <h2>Answers</h2>

    <table id="answersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${answers}" var="answer">
            <tr>
<!-- 				<td> -->
<%--                     <spring:url value="/announcement/{answerId}" var="answerUrl"> --%>
<%--                         <spring:param name="answerId" value="${answer.id}"/> --%>
<%--                     </spring:url> --%>
<%--                     <a href="${fn:escapeXml(announcementUrl)}"><c:out value="${announcement.name}"/></a> --%>
<!--                 </td> -->
                <td>
                    <petclinic:localDate date="${answer.date}" pattern="yyyy-MM-dd"/>
                </td>
                <td>
                    <c:out value="${answer.description}"/>
                </td>         
            </tr>
        </c:forEach>
        </tbody>
    </table>
<%--     <spring:url value="/announcements/new" var="addUrl">

    </spring:url> --%>
<%--     <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Answer</a> --%>
</petclinic:layout>
