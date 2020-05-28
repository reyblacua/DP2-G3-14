<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="appointments">

    <h2>Appoinment Information</h2>


    <table class="table table-striped">
    	<tr>
            <th>Name</th>
            <td><c:out value="${appointment.name}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${appointment.description}"/></td>
        </tr>
        <tr>
            <th>Date</th>
            <td><c:out value="${appointment.date.toString().split('T')[0]} || ${appointment.date.toString().split('T')[1]}"/></td>
        </tr>
        <tr>
            <th>Is paid</th>
            <td>
            	<c:if test="${appointment.isPaid == true}">
            		<c:out value="Yes"/>
            	</c:if>
            	<c:if test="${appointment.isPaid == false}">
            		<c:out value="No"/>            
            	</c:if>
            </td>
        </tr>
        <tr>
            <th>Hairdresser</th>
            <td><c:out value="${appointment.hairdresser.firstName} ${appointment.hairdresser.lastName}"/></td>
        </tr>
        <tr>
            <th>Owner</th>
            <td><c:out value="${appointment.owner.firstName}  ${appointment.owner.lastName}"/></td>
        </tr>
        <tr>
            <th>Pet</th>
            <td><c:out value="${appointment.pet.name}  (${appointment.pet.type}) "/></td>
        </tr>
        <c:if test="${appointment.isPaid==true}">
        	<tr>
        	    <th>Payment</th>
        	    <td></td>
        	</tr>
        	<tr>
        	    <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount</th>
        	    <td><c:out value="${appointment.payment.amount}"/></td>
        	</tr>
        	<tr>
        	    <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</th>
        	    <td><c:out value="${appointment.payment.date}"/></td>
        	</tr>
        </c:if>
    </table>
    
    <spring:url value="/appointments/delete/{appointmentId}" var="appointmentDeleteUrl">
    	<spring:param name="appointmentId" value="${appointment.id}"/>
    </spring:url> 
    <a href="${fn:escapeXml(appointmentDeleteUrl)}" class="btn btn-default">Delete Appointment</a> 

</petclinic:layout>