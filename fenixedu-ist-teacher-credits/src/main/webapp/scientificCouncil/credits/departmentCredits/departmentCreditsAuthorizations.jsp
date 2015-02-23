<%--

    Copyright © 2011 Instituto Superior Técnico

    This file is part of FenixEdu Teacher Credits.

    FenixEdu Teacher Credits is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Teacher Credits is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Teacher Credits.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message key="title.department.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<logic:present name="success"> 
	<logic:equal name="success" value="true"> 
		<span class="success0 mbottom05" >
			<bean:message key="label.department.credits.success" />
		</span> 
	</logic:equal>
	<logic:equal name="success" value="false"> 
		<span class="error0 mbottom05">
			<bean:message key="label.department.credits.error" />
		</span>
	</logic:equal>
</logic:present>


<fr:edit id="departmentCreditsAuthorizationBean" name="departmentCreditsAuthorizationBean" action="/departmentCreditsAuthorizations.do?method=addRoleDepartmentCredits" >
	<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="pt.ist.fenixedu.teacher.dto.DepartmentCreditsAuthorizationBean" >
		<fr:slot name="department" key="label.department" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
	        <fr:property name="from" value="availableDepartments"/>
			<fr:property name="format" value="\${name}"/>
	        <fr:property name="sortBy" value="name" />
	        <fr:property name="destination" value="postback" />
   		</fr:slot>
   		<fr:slot name="person" layout="autoComplete" key="label.user" required="true">
			<fr:property name="size" value="80"/>
			<fr:property name="format" value="\${name} / (\${username})"/>
			<fr:property name="args" value="slot=name"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchPersons"/>
			<fr:property name="indicatorShown" value="true"/>		
			<fr:property name="errorStyleClass" value="error0"/>
			<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:destination name="postback" path="/departmentCreditsAuthorizations.do?method=prepareDepartmentCredits"/>
		<fr:destination name="invalid" path="/departmentCreditsAuthorizations.do?method=prepareDepartmentCredits"/>
	</fr:layout>
</fr:edit>

<logic:notEmpty name="departmentCreditsAuthorizationBean" property="department">
	<bean:define id="department" name="departmentCreditsAuthorizationBean" property="department" type="org.fenixedu.academic.domain.Department"/>
	<fr:view name="departmentCreditsAuthorizationBean" property="department.associatedPersons" >
		<fr:schema type="org.fenixedu.academic.domain.Person" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
		    <fr:slot name="username" key="label.user"/>
		    <fr:slot name="name" key="label.name"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value="acenter,,thclear"/>
			<fr:property name="link(delete)" value="<%=String.format("/departmentCreditsAuthorizations.do?method=removeRoleDepartmentCredits&departmentId=%s",department.getExternalId())%>" />
			<fr:property name="key(delete)" value="label.department.credits.removeRole" />
			<fr:property name="param(delete)" value="externalId/personId"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
