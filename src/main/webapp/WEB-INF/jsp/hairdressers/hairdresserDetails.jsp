<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="hairdressers">

    <h2>Hairdresser Information</h2>


    <table class="table table-striped">
    	<tr>
            <th>Name</th>
            <td><c:out value="${hairdresser.firstName} ${hairdresser.lastName}"/></td>
        </tr>
        <tr>
            <th>Specialty</th>
            <td><c:out value="${hairdresser.specialties}"/></td>
        </tr>
        <tr>
            <th>Active</th>
            <td>
            <c:if test="${hairdresser.active == true}">
            		<c:out value="Yes"/>
            </c:if>
            <c:if test="${hairdresser.active == false}">
            		<c:out value="No"/>            
            </c:if>
            </td>
        </tr>
    </table>
    
    <c:if test="${hairdresser.active==true}">
    	<spring:url value="/hairdressers/{hairdresserId}/appointments/new" var="addAppointmentUrl">
    		<spring:param name="hairdresserId" value="${hairdresser.id}"/>
    	</spring:url>
    	<a href="${fn:escapeXml(addAppointmentUrl)}" class="btn btn-default">Add New Appointment</a>
    </c:if>
</petclinic:layout>