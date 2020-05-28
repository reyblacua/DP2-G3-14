<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="hairdressers">
    <h2>Hairdressers</h2>
    
    <c:choose>
    	<c:when test="${!isempty}">

    		<table id="hairdressersTable" class="table table-striped">
        		<thead>
        		<tr>
            		<th>Name</th>
            		<th>Specialty</th>
        		</tr>
        		</thead>
        		<tbody>
        		<c:forEach items="${hairdressers}" var="hairdresser">
            		<tr>
						<td>
                    		<spring:url value="/hairdressers/{hairdresserId}" var="hairdresserUrl">
                        		<spring:param name="hairdresserId" value="${hairdresser.id}"/>
                    		</spring:url>
                    		<a href="${fn:escapeXml(hairdresserUrl)}"><c:out value="${hairdresser.firstName} ${hairdresser.lastName}"/></a>
                		</td>
                		<td>
                    		<c:out value="${hairdresser.specialties}"/>
                		</td>            
            		</tr>
        		</c:forEach>
        		</tbody>
    		</table>
    	</c:when>
    	<c:otherwise>
    		There are not hairdressers yet
    	</c:otherwise>
    </c:choose>
</petclinic:layout>
