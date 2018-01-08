<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<my:pagetemplate title="Most active hunters">
    <jsp:attribute name="body">
      <c:set var="end" value="forests"/>

          <div class="container">
              <h4>Limit search to forest</h4>
              <form:form method="post" action="${pageContext.request.contextPath}/hunters/best" cssClass="form-horizontal">
                  <div class="form-group">
                      <div class="col-sm-10">
                          <select name="forestId">
                              <c:choose>
                                  <c:when test="${selectedForestId == null}">
                                      <option value="${null}" selected="selected"></option>
                                  </c:when>
                                  <c:otherwise>
                                      <option value="${null}"></option>
                                  </c:otherwise>
                              </c:choose>
                              <c:forEach items="${forests}" var="forest">
                                  <c:choose>
                                    <c:when test="${selectedForestId != null and forest.id == selectedForestId}">
                                        <option value="${forest.id}" selected="selected">${forest.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${forest.id}">${forest.name}</option>
                                    </c:otherwise>
                                </c:choose>
                              </c:forEach>
                          </select>
                      </div>
                  </div>
                  <div class="form-group">
                      <input type="submit" class="btn btn-primary" value="Search"/>
                  </div>
            </form:form>
          </div>

        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Number of visits</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${hunters}" var="hunter">
                <tr>
                    <td>
                        <my:a href="/hunters/read/${hunter.key.id}"><c:out value="${hunter.key.firstName} ${hunter.key.surname}"/></my:a>
                    </td>

                    <td>
                        <c:out value="${hunter.value}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </jsp:attribute>
</my:pagetemplate>