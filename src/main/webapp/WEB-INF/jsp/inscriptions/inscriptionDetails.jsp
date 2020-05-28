<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="courses">

    <h2>Inscription Information</h2>


    <table class="table table-striped">
    	<tr>
            <th>Course</th>
            <td><c:out value="${inscription.course.name}"/></td>
        </tr>
        <tr>
            <th>Pet</th>
            <td><c:out value="${inscription.pet}"/></td>
        </tr>
        <tr>
            <th>Inscription Date</th>
            <td><c:out value="${inscription.date}"/></td>
        </tr>
        <tr>
            <th>Is paid</th>
            <td><c:out value="${inscription.isPaid}"/></td>
        </tr>
        <c:if test="${inscription.payment!=null}">
        
			<tr>
	            <th>Payment</th>
	            <td></td>
	        </tr>
			<tr>
	            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Payment amount</th>
	            <td><c:out value="${inscription.payment.amount}"/> EUR</td>
	        </tr>
	       	<tr>
	       	   <th>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Payment date</th>
	       		<td><c:out value="${inscription.payment.date}"/></td>
	       	</tr>
       	</c:if>
    </table>
    
        
    <spring:url value="/inscriptions/delete/{inscriptionId}" var="inscriptionDeleteUrl">
    	<spring:param name="inscriptionId" value="${inscription.id}"/>
    </spring:url> 
    <a href="${fn:escapeXml(inscriptionDeleteUrl)}">Delete Inscription</a> 

</petclinic:layout>