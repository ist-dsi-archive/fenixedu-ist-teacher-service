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

<h3><bean:message key="label.teacherCreditsSheet.institutionWorkingTime.optional" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<logic:present name="institutionWorkTime">
	<bean:define id="teacherService" name="institutionWorkTime" property="teacherService"/>
</logic:present>


<jsp:include page="../teacherCreditsStyles.jsp"/>
<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="teacherService" property="teacher.person.username"/></bean:define>
<table class="headerTable"><tr>	
<td><img src="<%= request.getContextPath() + url %>" /></td>
<td >
	<fr:view name="teacherService">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="pt.ist.fenixedu.teacher.domain.teacher.TeacherService">
			<fr:slot name="teacher.person.presentationName" key="label.name"/>
			<fr:slot name="executionPeriod" key="label.period" layout="format">
				<fr:property name="format" value="${name}  ${executionYear.year}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
	   		<fr:property name="classes" value="creditsStyle"/>
		</fr:layout>
	</fr:view>
</td></tr></table>

<bean:define id="executionYearOid" name="teacherService" property="executionPeriod.executionYear.externalId"/>
<bean:define id="teacherOid" name="teacherService" property="teacher.externalId"/>

<p><html:link page="<%="/credits.do?method=viewAnnualTeachingCredits&amp;executionYearOid="+executionYearOid+"&teacherOid="+teacherOid%>"><bean:message key="label.return" bundle="APPLICATION_RESOURCES"/></html:link></p>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message" filter="false" />
	</span>
</html:messages>

<fr:hasMessages><fr:messages type="CONVERSION"><p><span class="error0"><fr:message/></span></p></fr:messages></fr:hasMessages>

<logic:present name="institutionWorkTime">
	<fr:edit id="institutionWorkTime" name="institutionWorkTime" action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"
	schema="edit.institutionWorkTime">
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:present>

<logic:notPresent name="institutionWorkTime">
	<fr:create action="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>" type="pt.ist.fenixedu.teacher.domain.teacher.InstitutionWorkTime"
	schema="create.institutionWorkTime">
		<fr:hidden slot="teacherService" name="teacherService"/>
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:create>
</logic:notPresent>
