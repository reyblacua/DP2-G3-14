<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="Inscriptions">

	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    
    <jsp:body>
        <h2>Inscriptions</h2>

        <form:form modelAttribute="inscription" class="form-horizontal" action="/courses/${course.id}/inscription/new">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Date:" name="date"/>

                <petclinic:selectField label="Pet:" name="pet" size="3" names="${pets}"/>
			</div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="isPaid" value="${false}"/>
                
<%--                     <input type="hidden" name="inscriptionId" value="${inscription.id}"/> --%>
                </div>
            </div>
            
             <button class="btn btn-default" type="submit">Save Inscription</button>
        </form:form>


    </jsp:body>

</petclinic:layout>
