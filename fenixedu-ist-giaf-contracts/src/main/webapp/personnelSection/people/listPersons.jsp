<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" /></h2>

<logic:empty name="personListFinded">
	<p><span class="errors"><bean:message bundle="MANAGER_RESOURCES" key="error.manager.implossible.findPerson" /></span></p>
</logic:empty>

<logic:notEmpty name="personListFinded">
	
	<bean:define id="totalFindedPersons" name="totalFindedPersons" />
	<logic:notEqual name="totalFindedPersons" value="1">
		<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.numberFindedPersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></b>	
	</logic:notEqual>
	
	<logic:equal name="totalFindedPersons" value="1">
		<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.findedOnePersons" arg0="<%= String.valueOf(totalFindedPersons) %>" /></b>
	</logic:equal>
	<br /><br />
		
	&nbsp;&nbsp;&nbsp;
		
	<fr:view name="personListFinded" schema="show.personAndEmployee">
		<fr:schema bundle="MANAGER_RESOURCES" type="org.fenixedu.academic.domain.Person">
			<fr:slot name="this" key="label.empty" layout="view-as-image">
				<fr:property name="classes" value="column3" />
				<fr:property name="moduleRelative" value="false" />
				<fr:property name="contextRelative" value="true" />
				<fr:property name="imageFormat" value="/user/photo/${username}" />
			</fr:slot>
			<fr:slot name="username" key="label.username"/>
			<fr:slot name="presentationName" key="label.name"/>
			<fr:slot name="employee.employeeNumber" key="label.name" schema="view.employee.number" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular"></fr:layout>
	</fr:view>
		
</logic:notEmpty>