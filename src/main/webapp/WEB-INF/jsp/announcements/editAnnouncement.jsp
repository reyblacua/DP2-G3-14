<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="Announcements">
    <jsp:body>
        <h2>Announcements</h2>

        <form:form modelAttribute="announcement" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Title:" name="name"/>
                <petclinic:inputField label="Pet Name:" name="petName"/>
                <petclinic:inputField label="Description:" name="description"/>
                 <div class="control-group">
                   <petclinic:selectField name="type" label="Type of pet:" names="${types}" size="5"/> 
                </div>
				<label class="col-sm-2 control-label">Can be adopted:</label>
				<form:select path="canBeAdopted">
					<form:option value="True">Yes</form:option>
					<form:option value="False">No</form:option>
				</form:select>		
				</div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="announcementId" value="${announcement.id}"/>
                    <button class="btn btn-default" type="submit">Save Announcement</button>
                </div>
            </div>
        </form:form>


    </jsp:body>

</petclinic:layout>
