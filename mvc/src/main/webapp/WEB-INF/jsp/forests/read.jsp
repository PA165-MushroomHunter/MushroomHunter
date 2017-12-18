<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate title="Forest details">
<jsp:attribute name="body">
    <c:set var="end" value="forests"/>
    <script>
        function openModal(suffix) {
            var modal = $("#modal_" + suffix);
            if (modal)
                modal.modal('show');
        }

        function closeModal(suffix) {
            var modal = $("#modal_" + suffix);
            if (modal)
                modal.modal('hide');
        }
    </script>

    <table class="table"> <%-- TODO: change size, add create visit button--%>
        <caption>Forest '<c:out value="${forests.name}"/>'</caption>
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Delete</th>
            <th>Update</th>
        </tr>
        </thead>

        <tbody>
        <tr>
            <td>
                <c:out value="${forests.name} "/>
            </td>

            <td>
                <c:out value="${forests.description}"/>
            </td>

            <td>
                <button class="glyphicon glyphicon-trash btn" onclick=" openModal(${forests.id}) "></button>

                <my:modal_template suffix="${forests.id}" title="Delete forest">
                  <jsp:attribute name="body">
                      <strong>Are you sure you want to delete forest '<c:out
                              value="${forests.name}"/>'?</strong>
                  </jsp:attribute>
                  <jsp:attribute name="footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal"
                              onclick="closeModal(${forests.id})">Close
                      </button>
                    <form style="float: right; margin-left: 10px" method="post"
                          action="${pageContext.request.contextPath}/${end}/delete/${forests.id}">
                        <input type="submit" class="btn btn-primary" value="Delete"/>
                    </form>
                  </jsp:attribute>
                </my:modal_template>
            </td>

            <td>
                <button class="glyphicon glyphicon-edit btn" onclick="location.href='${pageContext.request.contextPath}/${end}/edit/${forests.id}'">
                </button>
            </td>
        </tr>

        </tbody>

    <%-- visits table --%>
    <pre>
        <my:forest_visit_table_template tableName="${forests.name}'s visits"/>
    </pre>

</jsp:attribute>
</my:pagetemplate>