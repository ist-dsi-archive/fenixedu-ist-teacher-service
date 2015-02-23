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

<logic:present role="role(SCIENTIFIC_COUNCIL)">

	<h2><bean:message key="link.define.periods" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	<html:messages id="message" message="true">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:write name="message"/>
			</span>
		</p>
	</html:messages>
	
	<logic:notEmpty name="teacherCreditsBean">
		<bean:define id="URL">/defineCreditsPeriods.do?executionPeriodId=<bean:write name="teacherCreditsBean" property="executionPeriod.externalId"/></bean:define>
		<fr:form action="<%= URL %>">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="creditsPeriodForm" value="editPeriod"/>
		
		<logic:equal name="teacherCreditsBean" property="teacher" value="true">
			<h3 class="mtop15 mbottom05"><bean:message key="label.teacher" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
			<fr:edit id="teacherCreditsBeanID" name="teacherCreditsBean">
				<fr:schema type="pt.ist.fenixedu.teacher.dto.teacherCredits.TeacherCreditsPeriodBean" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
				    <fr:slot name="beginForTeacher" key="label.beginDate">
				        <fr:validator name="org.fenixedu.academic.ui.renderers.validators.DateTimeValidator">
				            <fr:property name="required" value="true" />
				        </fr:validator>
				    </fr:slot>
				    <fr:slot name="endForTeacher" key="label.endDate">
				        <fr:validator name="org.fenixedu.academic.ui.renderers.validators.DateTimeValidator">
				            <fr:property name="required" value="true" />
				        </fr:validator>
				    </fr:slot>
				</fr:schema>	
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>				
			</fr:edit>
		</logic:equal>
		<logic:equal name="teacherCreditsBean" property="teacher" value="false">
			<h3 class="mtop15 mbottom05"><bean:message key="label.departmentAdmOffice" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
			<fr:edit id="teacherCreditsBeanID" name="teacherCreditsBean">
				<fr:schema type="pt.ist.fenixedu.teacher.dto.teacherCredits.TeacherCreditsPeriodBean" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
				    <fr:slot name="beginForDepartmentAdmOffice" key="label.beginDate">
				        <fr:validator name="org.fenixedu.academic.ui.renderers.validators.DateTimeValidator">
				            <fr:property name="required" value="true" />
				        </fr:validator>
				    </fr:slot>
				    <fr:slot name="endForDepartmentAdmOffice" key="label.endDate">
				        <fr:validator name="org.fenixedu.academic.ui.renderers.validators.DateTimeValidator">
				            <fr:property name="required" value="true" />
				        </fr:validator>
				    </fr:slot>
				</fr:schema>				
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>				
			</fr:edit>
			
		</logic:equal>
		<html:submit><bean:message key="label.submit"/></html:submit>
			<html:cancel onclick="this.form.method.value='showPeriods';this.form.submit();"><bean:message key="button.cancel"/></html:cancel>
		</fr:form>
	</logic:notEmpty>	
</logic:present>