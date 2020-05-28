<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Announcement Information </h2>

    <table class="table table-striped">
    	<tr>
            <th>Title</th>
            <td><c:out value="${announcement.name}"/></td>
        </tr>
       <tr>
            <th>Owner</th>
            <td><c:out value="${announcement.owner.firstName} ${announcement.owner.lastName}"/></td>
        </tr>
        <tr>
            <th>Pet name</th>
            <td><c:out value="${announcement.petName}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${announcement.description}"/></td>
        </tr>
        <tr>
            <th>Can be adopted</th>
            <td><c:out value="${announcement.canBeAdopted}"/></td>
        </tr>
        <tr>
            <th>Type</th>
            <td><c:out value="${announcement.type}"/></td>
        </tr>
    </table>
    
    <c:if test="${!isanonymoususer}">
    
   	   <c:if test="${ismine}">
    	
			     <spring:url value="/announcements/update/{announcementId}" var="announcementUpdateUrl">
			    	<spring:param name="announcementId" value="${announcement.id}"/>
			    </spring:url> 
			    <a href="${fn:escapeXml(announcementUpdateUrl)}" class="btn btn-default">Update Announcement</a> 
			    
			     <spring:url value="/announcements/delete/{announcementId}" var="announcementDeleteUrl">
			    	<spring:param name="announcementId" value="${announcement.id}"/>
			    </spring:url> 
			    <a href="${fn:escapeXml(announcementDeleteUrl)}" class="btn btn-default">Delete Announcement</a> 
	    
			    <spring:url value="/announcements/{announcementId}/answers" var="announcementsAnswersUrl">
			    	<spring:param name="announcementId" value="${announcement.id}"/>
			    </spring:url> 
			    <a href="${fn:escapeXml(announcementsAnswersUrl)}" class="btn btn-default">Answers</a>
	
			    </c:if>
		
		<c:if test="${!ismine && positiveHistory && announcement.canBeAdopted}">
		
				<spring:url value="/announcements/{announcementId}/answer/new" var="newAnswerUrl">
			    	<spring:param name="announcementId" value="${announcement.id}"/>
			    </spring:url> 
			    <a href="${fn:escapeXml(newAnswerUrl)}" class="btn btn-default">Answer to the announcement</a>
	   	</c:if>
	   	<c:if test="${!positiveHistory}">
	   		<h>You can't answer an announcement if you don't have a possitive history</h>
	   	</c:if>
	   	
	      
    </c:if>
    
</petclinic:layout>