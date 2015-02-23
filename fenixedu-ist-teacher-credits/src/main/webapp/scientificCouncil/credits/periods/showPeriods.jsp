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
		
		<fr:form action="/defineCreditsPeriods.do">
			<fr:edit name="teacherCreditsBean" id="teacherCreditsBeanID">
				<fr:schema type="pt.ist.fenixedu.teacher.dto.teacherCredits.TeacherCreditsPeriodBean" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
				 	<fr:slot name="executionPeriod" key="label.execution-period" layout="menu-select-postback">
				      <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.NotClosedExecutionPeriodsProvider" />
				      <fr:property name="format" value="${year} - ${semester}º Sem." />
				      <fr:property name="destination" value="postBack" />
				    </fr:slot>
				</fr:schema>
				<fr:destination name="postBack" path="/defineCreditsPeriods.do?method=showPeriods"/>		
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tdclear"/>			
				</fr:layout>			
			</fr:edit>
			<logic:present role="role(MANAGER)">
				<bean:define id="executionYearOid" name="teacherCreditsBean" property="executionPeriod.executionYear.externalId"/>
				<logic:equal name="teacherCreditsBean" property="executionPeriod.executionYear.annualCreditsState.isCreditsClosed" value="false">
					<logic:equal name="teacherCreditsBean" property="executionPeriod.executionYear.annualCreditsState.isFinalCreditsCalculated" value="true">
						<p><html:link page='<%="/annualTeachingCreditsDocument.do?method=getAnnualTeachingCreditsPdf&executionYearOid=" + executionYearOid %>'>
							<bean:message key="link.teacherCredits.close"/>
						</html:link></p>
						<bean:message key="label.teacherCredits.close.message"/>
					</logic:equal>
				</logic:equal>
			</logic:present>
		</fr:form>
		<h3 class="mtop15 mbottom05"><bean:message key="label.teacher"/></h3>
		<fr:view name="teacherCreditsBean" layout="tabular">
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
				<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
			</fr:layout>
		</fr:view>
		<html:link page="/defineCreditsPeriods.do?method=prepareEditTeacherCreditsPeriod" paramName="teacherCreditsBean" paramProperty="executionPeriod.externalId" paramId="executionPeriodId">
			<bean:message key="link.change" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>
		
		<h3 class="mtop15 mbottom05"><bean:message key="label.departmentAdmOffice" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
		<fr:view name="teacherCreditsBean" layout="tabular">
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
				<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
			</fr:layout>
		</fr:view>
		<html:link page="/defineCreditsPeriods.do?method=prepareEditDepartmentAdmOfficeCreditsPeriod" paramName="teacherCreditsBean" paramProperty="executionPeriod.externalId" paramId="executionPeriodId">
			<bean:message key="link.change" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</html:link>			
		
		<logic:present name="editInterval">
			<bean:define id="editInterval" name="editInterval" />
		</logic:present>
		<logic:notPresent name="editInterval">
			<bean:define id="editInterval" value="" />
		</logic:notPresent>
		
		<h3 class="mtop15 mbottom05">Período para definir créditos unitários (ck) para as UC's partilhadas</h3>
		<bean:define id="readOnly" value="true"/>
		<bean:define id="action" value="/defineCreditsPeriods.do?method=prepareEditAnnualCreditsDates&editInterval=sharedUnitCredits"/>
		<logic:equal name="editInterval" value="sharedUnitCredits">
			<bean:define id="readOnly" value="false"/>
			<bean:define id="action" value="/defineCreditsPeriods.do?method=editAnnualCreditsDates&editInterval=sharedUnitCredits"/>
		</logic:equal>
		<fr:form id="sharedUnitCreditsForm" action="<%=action %>">
			<fr:edit name="teacherCreditsBean">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="pt.ist.fenixedu.teacher.dto.teacherCredits.TeacherCreditsPeriodBean">
					<fr:slot name="sharedUnitCreditsBeginDate" key="label.beginDate" layout="null-as-label" readOnly="<%=Boolean.valueOf(readOnly.toString())%>"/>
					<fr:slot name="sharedUnitCreditsEndDate" key="label.endDate" layout="null-as-label"  readOnly="<%=Boolean.valueOf(readOnly.toString()) %>"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
				<fr:destination name="invalid" path="/defineCreditsPeriods.do?method=showPeriods"/>
				<fr:destination name="cancel" path="/defineCreditsPeriods.do?method=showPeriods"/>
			</fr:edit>
			<logic:equal name="editInterval" value="sharedUnitCredits">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="link.change" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></html:submit>
			</logic:equal>
			<logic:notEqual name="editInterval" value="sharedUnitCredits">
				<a href="#" onclick="javascript:document.getElementById('sharedUnitCreditsForm').submit();"><bean:message key="link.change" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></a>
			</logic:notEqual>
		</fr:form>

		<h3 class="mtop15 mbottom05">Período para definir créditos unitários (ck) para as restantes UC's</h3>		
		<bean:define id="readOnly" value="true"/>
		<bean:define id="action" value="/defineCreditsPeriods.do?method=prepareEditAnnualCreditsDates&editInterval=unitCredits"/>
		<logic:equal name="editInterval" value="unitCredits">
			<bean:define id="readOnly" value="false"/>
			<bean:define id="action" value="/defineCreditsPeriods.do?method=editAnnualCreditsDates&editInterval=unitCredits"/>
		</logic:equal>
		<fr:form id="unitCreditsForm" action="<%=action %>">
			<fr:edit name="teacherCreditsBean">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="pt.ist.fenixedu.teacher.dto.teacherCredits.TeacherCreditsPeriodBean">
					<fr:slot name="unitCreditsBeginDate" key="label.beginDate" layout="null-as-label" readOnly="<%=Boolean.valueOf(readOnly.toString()) %>"/>
					<fr:slot name="unitCreditsEndDate" key="label.endDate" layout="null-as-label" readOnly="<%=Boolean.valueOf(readOnly.toString()) %>"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
				<fr:destination name="invalid" path="/defineCreditsPeriods.do?method=showPeriods"/>
				<fr:destination name="cancel" path="/defineCreditsPeriods.do?method=showPeriods"/>
			</fr:edit>
			<logic:equal name="editInterval" value="unitCredits">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="link.change" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></html:submit>
			</logic:equal>
			<logic:notEqual name="editInterval" value="unitCredits">
				<a href="#" onclick="javascript:document.getElementById('unitCreditsForm').submit();"><bean:message key="link.change" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></a>
			</logic:notEqual>
		</fr:form>
			
		<h3 class="mtop15 mbottom05">Outras datas</h3>
		<fr:edit name="teacherCreditsBean" property="annualCreditsState">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="pt.ist.fenixedu.teacher.domain.credits.AnnualCreditsState">
				<fr:slot name="orientationsCalculationDate" layout="null-as-label"/>
				<fr:slot name="finalCalculationDate" layout="null-as-label"/>
				<fr:slot name="closeCreditsDate" layout="null-as-label"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
			
</logic:present>

<script type="text/javascript">
function check(e,v){
	if (e.className == "dnone")
  	{
	  e.className = "dblock";
	  v.value = "-";
	}
	else {
	  e.className = "dnone";
  	  v.value = "+";
	}
}
</script>