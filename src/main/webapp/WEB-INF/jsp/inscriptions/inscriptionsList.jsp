<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="inscriptions">
    <h2>Inscriptions</h2>
        
        <c:choose>
    		<c:when test="${!isempty}">
       

    <table id="inscriptionsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Course</th>
            <th>Pet</th>
            <th>Is paid</th>
        </tr>
        </thead>
        <tbody>
        
        
        <c:forEach items="${inscriptions}" var="inscription">
            <tr>
				<td>
                    <spring:url value="/inscriptions/{inscriptionId}" var="inscriptionUrl">
                        <spring:param name="inscriptionId" value="${inscription.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(inscriptionUrl)}"><c:out value="${inscription.course.name}"/></a>
                </td>
                <td>
                    <c:out value="${inscription.pet}"/>
                </td>
                <td>
                    <c:out value="${inscription.isPaid}"/>
                </td>
            </tr>
        </c:forEach>
        
        
        </tbody>
    </table>

            </c:when>    
    <c:otherwise>
        You have not inscriptions yet.
    </c:otherwise>
</c:choose>
    
</petclinic:layout>
