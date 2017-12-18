<%@ tag pageEncoding="utf-8" trimDirectiveWhitespaces="true" dynamic-attributes="attr" %>
<%@attribute name="tableName" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>

<c:set var="hunters_path" value="hunters"/>
<c:set var="visits_path" value="visits"/>
<c:set var="forests_path" value="forests"/>
<c:set var="mushrooms_path" value="mushrooms"/>

<table class="table table-striped">
    <caption><c:out value="${tableName}"/></caption>
    <thead>
    <tr>
        <th>Date</th>
        <th>Hunter</th>
        <th>Found mushrooms</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${visits}" var="visit">
        <tr>
            <td>
                <my:a href="/${visits_path}/read/${visit.id}"><c:out value="${visit.date}"/> </my:a>
            </td>
            <td>
                <my:a href="/${hunters_path}/read/${visit.hunter.id}"><c:out value="${visit.hunter.firstName} "/>
                <c:out value="${visit.hunter.surname}"/></my:a>
            </td>
            <td>
                <c:forEach items="${visit.mushrooms}" var="mushroom">
                    <my:a href="/${mushrooms_path}/read/${mushroom.id}"><c:out value="${mushroom.name} "/></my:a>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>