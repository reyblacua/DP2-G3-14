<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="Answer">

	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <h2>Answer to announcement: <c:out value="${announcement.name}"/></h2>
        

        <form:form modelAttribute="answer" class="form-horizontal" action="/announcements/${announcement.id}/answer/new">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Date:" name="date"/>
                <petclinic:inputField label="Description:" name="description"/>
			</div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="answerId" value="${answer.id}"/>
                    <button class="btn btn-default" type="submit">Send Answer</button>
                </div>
            </div>
        </form:form>


    </jsp:body>

</petclinic:layout>
