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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<jsp:include page="../teacherCreditsStyles.jsp"/>

<h3><bean:message key="label.teacherCreditsSheet.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<logic:present name="professorship">
	
	<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="professorship" property="teacher.person.username"/></bean:define>
	<table class="headerTable"><tr>
	<td><img src="<%= request.getContextPath() + url %>"/></td>
	<td >
		<fr:view name="professorship">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.Professorship">
				<fr:slot name="teacher.person.presentationName" key="label.name"/>
				<fr:slot name="executionCourse.nome" key="label.course"/>
				<fr:slot name="executionCourse.executionPeriod" key="label.execution-period" layout="format">
					<fr:property name="format" value="${name}  ${executionYear.year}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="creditsStyle"/>
			</fr:layout>
		</fr:view>
		</td>
	</tr></table>
	
	
	<logic:messagesPresent>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</logic:messagesPresent>
	<fr:hasMessages><fr:messages><p><span class="error0"><fr:message/></span></p></fr:messages></fr:hasMessages>
	<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
		<span class="error">
			<bean:write name="message" filter="false"/>
		</span>
	</html:messages>

	<bean:define id="professorshipID" name="professorship" property="externalId" />
	<p><html:link page="<%="/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails&professorshipID="+professorshipID %>"><bean:message key="label.return" bundle="APPLICATION_RESOURCES"/></html:link></p>

	<logic:notPresent name="supportLesson">
		<fr:create id="supportLesson" schema="manage.supportLesson" type="pt.ist.fenixedu.teacher.domain.SupportLesson" action="<%="/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails&professorshipID="+professorshipID %>">
			<fr:hidden slot="professorship" name="professorship"/>
		</fr:create>
	</logic:notPresent>
	
	<logic:present name="supportLesson">	
		<fr:edit name="supportLesson" schema="manage.supportLesson" action="<%="/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails&professorshipID="+professorshipID %>">
		</fr:edit>
	</logic:present>
</logic:present>