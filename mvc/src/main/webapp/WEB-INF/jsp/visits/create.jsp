<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate title="Register new Visit">
<jsp:attribute name="body">

    <form:form method="post" action="${pageContext.request.contextPath}/visits/create/"
               modelAttribute="registerVisit" cssClass="form-horizontal">

        <form:input type="hidden" path="hunter" value="${hunter}" />

        <div class="form-group ${note_error?'has-error':''}">
            <form:label path="note" cssClass="col-sm-2 control-label">Note</form:label>
            <div class="col-sm-10">
                <form:input path="note" cssClass="form-control"/>
                <form:errors path="note" cssClass="help-block"/>
            </div>
        </div>

        <div class="form-group ${date_error?'has-error':''}">
            <form:label path="date" cssClass="col-sm-2 control-label">Date (YYYY-MM-DD)</form:label>
            <div class="col-sm-10">
                <form:input path="date" cssClass="form-control"/>
                <form:errors path="date" cssClass="help-block"/>
            </div>
        </div>

        <div class="form-group ${forest_error?'has-error':''}">
            <form:label path="forest" cssClass="col-sm-2 control-label" >Forest</form:label>
            <div class="col-sm-10">
                <form:select path="forest" name="forest">
                    <c:forEach items="${forests}" var="forest">
                        <form:option value="${forest.id}">${forest.name}</form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="forest" cssClass="help-block"/>
            </div>
        </div>

        <div class="form-group ${mushrooms_error?'has-error':''}">
            <form:label path="mushrooms" cssClass="col-sm-2 control-label">Mushrooms found</form:label>
            <div class="col-sm-10">
                <form:select path="mushrooms" name="mushrooms" multiple="true" items="${mushrooms}" itemLabel="name" itemValue="id"/>
                <form:errors path="mushrooms" cssClass="help-block"/>
            </div>
        </div>

      <button class="btn btn-primary" type="submit">Register new visit</button>
    </form:form>

</jsp:attribute>
</my:pagetemplate>