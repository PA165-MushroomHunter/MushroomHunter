<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:pagetemplate title="Sign Up">
<jsp:attribute name="body">

    <form:form method="post" action="${pageContext.request.contextPath}/auth/register/"
               modelAttribute="userRegister" cssClass="form-horizontal">

        <div class="form-group ${firstName_error?'has-error':''}">
          <form:label path="firstName" cssClass="col-sm-2 control-label">First name</form:label>
          <div class="col-sm-10">
            <form:input path="firstName" cssClass="form-control"/>
            <form:errors path="firstName" cssClass="help-block"/>
          </div>
        </div>

        <div class="form-group ${surname_error?'has-error':''}">
          <form:label path="surname" cssClass="col-sm-2 control-label">Last name</form:label>
          <div class="col-sm-10">
            <form:input path="surname" cssClass="form-control"/>
            <form:errors path="surname" cssClass="help-block"/>
          </div>
        </div>

       <div class="form-group ${userNickname_error?'has-error':''}">
         <form:label path="userNickname" cssClass="col-sm-2 control-label">Nick name</form:label>
         <div class="col-sm-10">
           <form:input path="userNickname" cssClass="form-control"/>
           <form:errors path="userNickname" cssClass="help-block"/>
         </div>
       </div>

       <div class="form-group ${personalInfo_error?'has-error':''}">
         <form:label path="personalInfo" cssClass="col-sm-2 control-label">Personal info</form:label>
         <div class="col-sm-10">
           <form:textarea path="personalInfo" cssClass="form-control"/>
           <form:errors path="personalInfo" cssClass="help-block"/>
         </div>
       </div>


      <div class="form-group ${unencryptedPassword_error?'has-error':''}">
        <form:label path="unencryptedPassword" cssClass="col-sm-2 control-label">Password</form:label>
        <div class="col-sm-10">
          <form:password path="unencryptedPassword" cssClass="form-control"/>
          <form:errors path="unencryptedPassword" cssClass="help-block"/>
        </div>
      </div>

      <button class="btn btn-primary" type="submit">Sign up</button>
    </form:form>

</jsp:attribute>
</my:pagetemplate>