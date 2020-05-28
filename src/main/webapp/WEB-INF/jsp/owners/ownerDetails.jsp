<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>City</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
         <tr>
            <th>Dangerous Animal License?:</th>
             <c:if test="${owner.dangerousAnimal}">
              <td><c:out value="Yes"/></td>
            </c:if>
			<c:if test="${!owner.dangerousAnimal}">
              <td><c:out value="No"/></td>
            </c:if>
        </tr>
        
         <tr>
            <th>Numerous Animals License?:</th>
             <c:if test="${owner.numerousAnimal}">
              <td><c:out value="Yes"/></td>
            </c:if>
			<c:if test="${!owner.numerousAnimal}">
              <td><c:out value="No"/></td>
            </c:if>
        </tr>
        
         <tr>
            <th>Lives in City?:</th>
            <c:if test="${owner.livesInCity}">
              <td><c:out value="Yes"/></td>
            </c:if>
			<c:if test="${!owner.livesInCity}">
              <td><c:out value="No"/></td>
            </c:if>
        </tr>
        
         <tr>
            <th>Positive History?:</th>
             <c:if test="${owner.positiveHistory}">
              <td><c:out value="Yes"/></td>
            </c:if>
			<c:if test="${!owner.positiveHistory}">
              <td><c:out value="No"/></td>
            </c:if>
        </tr>
    </table>
	<c:if test="${isMe}">
	    <spring:url value="{ownerId}/edit" var="editUrl">
	        <spring:param name="ownerId" value="${owner.id}"/>
	    </spring:url>
	    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a>
	
	    <spring:url value="{ownerId}/pets/new" var="addUrl">
	        <spring:param name="ownerId" value="${owner.id}"/>
	    </spring:url>
	    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>
	</c:if>
    <br/>
    <br/>
    <br/>
    <h2>Pets and Visits</h2>

    <table class="table table-striped" id="pets">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                        <dt>Is vaccinated?:</dt>
                        <dd>
                        <c:if test="${pet.isVaccinated}">
                         <c:out value="Yes"></c:out>
                         </c:if>
                          <c:if test="${!pet.isVaccinated}">
                         <c:out value="No"></c:out>
                         </c:if>
                        </dd>
                        <dt>Dangerous Animal</dt>
                        <dd>
                        <c:if test="${pet.dangerous}">
                         <c:out value="Yes"></c:out>
                         </c:if>
                          <c:if test="${!pet.dangerous}">
                         <c:out value="No"></c:out>
                         </c:if>
                        </dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                            	<c:if test="${isMe}">
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                                </c:if>
                            </td>
                            <td>
                            	<c:if test="${isMe}">
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>
