<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="Appointments">
    <jsp:body>
        <h2>Appointments</h2>

        <form:form modelAttribute="appointment" class="form-horizontal" action="/hairdressers/${hairdresser.id}/appointments/new">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Title:" name="name"/>
                <petclinic:inputField label="Description:" name="description"/>
                <petclinic:inputField label="Date:" name="date"/>
           		<petclinic:selectField label="Pet:" name="pet" size="3" names="${allPets}"/>
           		<input type="hidden" name="isPaid" value="false"/>
			</div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
<!--                <input type="hidden" name="appointmentId" value="${appointment.id}"/>  			-->
                    <button class="btn btn-default" type="submit">Save Appointment</button>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
