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
<%@page import="org.fenixedu.academic.domain.Professorship"%>
<%@page import="pt.ist.fenixedu.teacher.domain.teacher.TeacherService"%>
<%@page import="pt.ist.fenixedu.teacher.domain.SupportLesson"%>
<%@page import="java.util.SortedSet"%>
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<jsp:include page="../teacherCreditsStyles.jsp"/>

<bean:define id="professorship" name="degreeTeachingServiceBean" property="professorship" />

<h3><bean:message key="label.teacherCreditsSheet.professorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

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

<bean:define id="teacher" name="professorship" property="teacher"/>
<bean:define id="teacherId" name="teacher" property="externalId"/>
<bean:define id="executionCourse" name="professorship" property="executionCourse" />
<bean:define id="executionYearOid" name="executionCourse" property="executionPeriod.executionYear.externalId" />

<p><html:link page="<%="/credits.do?method=viewAnnualTeachingCredits&amp;executionYearOid="+executionYearOid+"&teacherOid="+teacherId%>"><bean:message key="label.return" bundle="APPLICATION_RESOURCES"/></html:link></p>


<h3 class="separator2 mtop2"><bean:message key="label.teacherCreditsSheet.shiftProfessorships" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<p class="infoop2">
<bean:message key="label.teaching.service.help.top" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
</p>

 
<logic:messagesPresent><span class="error"><!-- Error messages go here --><html:errors /></span></logic:messagesPresent>
<fr:hasMessages><p><span class="error0"><fr:messages><fr:message/></fr:messages></span></p></fr:hasMessages>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>


<fr:form action="/degreeTeachingServiceManagement.do?method=updateTeachingServices">
	<fr:edit id="degreeTeachingServiceBean" name="degreeTeachingServiceBean" visible="false"/>
	<table class="tstyle4 mtop1">
		<%-- ********************************* HEADER *********************************************** --%>
		<tr>
			<th rowspan="2" width="10%"><bean:message key="label.shift"/></th>
			<th rowspan="2" width="5%"><bean:message key="label.shift.type"/></th>
			<th colspan="5" width="40%"><bean:message key="label.lessons"/></th>
			<th rowspan="2"><bean:message key="label.weeklyAverage" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th rowspan="2"><bean:message key="label.semesterTotal" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th rowspan="2"><bean:message key="label.professorship.percentage"/></th>
			<th><bean:message key="label.teacher.applied"/></th>
			<th rowspan="2" class="thclear"/>			
		</tr>
		<tr>
			<th><bean:message key="label.week" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
			<th><bean:message key="label.day.of.week"/></th>
			<th><bean:message key="label.lesson.start"/></th>
			<th><bean:message key="label.lesson.end"/></th>
			<th><bean:message key="label.lesson.room"/></th>	
			<th><bean:message key="label.teacher"/> - <bean:message key="label.professorship.percentage"/></th>
		</tr> 
		<%-- ********************************* SHIFTS *********************************************** --%>
	
	
	
	
		<logic:iterate id="shiftService" name="degreeTeachingServiceBean" property="shiftServiceSet">
			<bean:define id="shift" name="shiftService" property="shift"/>
			<bean:define id="availablePercentage" name="shiftService" property="availablePercentage"/>
		
			
			<bean:size id="lessonsSize" name="shift" property="associatedLessons" />	
				<bean:define id="shiftOID" name="shift" property="externalId"/>
				<logic:equal name="lessonsSize" value="0">
					<tr>
						<td><bean:write name="shift" property="nome"/></td>
						<td><bean:write name="shift" property="shiftTypesCodePrettyPrint"/></td>
						<td colspan="5"> Não tem aulas </td>
						<td> - </td>
						<td> - </td>
						<td>
							<logic:greaterThan name="availablePercentage" value="0">
								<fr:edit id="<%=shiftOID.toString() %>" name="shiftService" slot="percentage" validator="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator">
									<fr:layout>
										<fr:property name="size" value="4"/>
										<fr:property name="formatText" value="%"/>
									</fr:layout>
								</fr:edit>
							</logic:greaterThan>
						</td>
						<td>
							<fr:view name="shiftService" property="appliedShiftTeachingService"/>
						</td>
						<td class="tdclear tderror1"><fr:hasMessages for="<%=shiftOID.toString() %>"><p><span class="error0"><fr:message for="<%=shiftOID.toString()%>"/></span></p></fr:hasMessages></td>
					</tr>
				</logic:equal>

				<logic:notEqual name="lessonsSize" value="0">
					<logic:iterate id="lesson" name="shift" property="lessonsOrderedByWeekDayAndStartTime" indexId="indexLessons" >
						<tr>
				            <logic:equal name="indexLessons" value="0">
								<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="nome"/></td>
								<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="shiftTypesCodePrettyPrint"/></td>
							</logic:equal>
							
							<td><fr:view name="lesson" property="occurrenceWeeksAsString"/></td>
							<td><fr:view name="lesson" property="weekDay.labelShort"/></td>
							<td><fr:view name="lesson" property="beginHourMinuteSecond"/></td>
							<td><fr:view name="lesson" property="endHourMinuteSecond"/></td>
							<td>
								<logic:notEmpty name="lesson" property="sala">
									<bean:write name="lesson" property="sala.name"/>
								</logic:notEmpty>					
								<logic:empty name="lesson" property="sala">
									-
								</logic:empty>
							</td>
							<logic:equal name="indexLessons" value="0">
								<td rowspan="<%= lessonsSize %>"><fr:view name="shift" property="courseLoadWeeklyAverage"/></td>
								<td rowspan="<%= lessonsSize %>"><bean:write name="shift" property="courseLoadTotalHours"/></td>
								<td rowspan="<%= lessonsSize %>">
									<logic:greaterThan name="availablePercentage" value="0">
										<fr:edit id="<%=shiftOID.toString() %>" name="shiftService" slot="percentage" type="java.lang.Double" validator="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator">
											<fr:layout>
												<fr:property name="size" value="4"/>
												<fr:property name="formatText" value="%"/>
											</fr:layout>
										</fr:edit>
										 <%--
										 <fr:edit id="<%=shiftOID.toString() %>" name="shiftService">
											<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="pt.ist.fenixedu.teacher.domain.credits.util.DegreeTeachingServiceBean$ShiftServiceBean">
												<fr:slot name="percentage" key="">
													<fr:property name="size" value="4"/>
													<fr:property name="formatText" value="%"/>
													<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator"/>
												</fr:slot>
											</fr:schema>
												<fr:layout>
													<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
													<fr:property name="columnClasses" value="headerTable,,tdclear tderror1"/>
												</fr:layout>
										</fr:edit>--%>
									</logic:greaterThan>
								</td>
								<td rowspan="<%= lessonsSize %>">
									<fr:view name="shiftService" property="appliedShiftTeachingService"/>
								</td>		
								<td class="tdclear tderror1"><fr:hasMessages for="<%=shiftOID.toString() %>"><p><span class="error0"><fr:message for="<%=shiftOID.toString()%>"/></span></p></fr:hasMessages></td>				
							</logic:equal>
						</tr>
					</logic:iterate>
				</logic:notEqual>
				 	
		</logic:iterate>
		
	</table>
		<p class="mtop05"><bean:message key="label.teaching.service.help.bottom" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></p>
	

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</fr:form>
	


<h3 class="separator2 mtop2"><bean:message key="label.teacherCreditsSheet.supportLessons" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<bean:define id="link" type="java.lang.String">/supportLessonsManagement.do?method=prepareEdit&amp;page=0&amp;professorshipID=<bean:write name="professorship" property="externalId"/></bean:define>
<html:link page="<%= link %>"><bean:message key="label.support-lesson.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></html:link>

<%	SortedSet<SupportLesson> supportLessonsOrderedByStartTimeAndWeekDay = TeacherService.getSupportLessonsOrderedByStartTimeAndWeekDay((Professorship) professorship);
request.setAttribute("supportLessonList", supportLessonsOrderedByStartTimeAndWeekDay);
%>
						
<logic:notEmpty name="supportLessonList">
	<fr:view name="supportLessonList">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="pt.ist.fenixedu.teacher.domain.SupportLesson">
			<fr:slot name="weekDayObject.labelShort" key="label.support-lesson.weekday"/>
			<fr:slot name="startTimeHourMinuteSecond" key="label.support-lesson.start-time"/>
			<fr:slot name="endTimeHourMinuteSecond" key="label.support-lesson.end-time"/>
			<fr:slot name="place" key="label.support-lesson.place"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,,"/>
				<fr:property name="link(edit)" value="/supportLessonsManagement.do?method=prepareEdit" />
				<fr:property name="key(edit)" value="link.edit" />
				<fr:property name="param(edit)" value="externalId/supportLessonID" />
				<fr:property name="link(delete)" value="/supportLessonsManagement.do?method=deleteSupportLesson" />
				<fr:property name="key(delete)" value="link.delete" />
				<fr:property name="param(delete)" value="externalId/supportLessonID" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
