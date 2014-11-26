/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.teacher.ui.struts.action.scientificCouncil.credits;

import java.io.Serializable;

import jvstm.Atomic;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixedu.contracts.domain.Employee;

public class DepartmentCreditsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Department department;
    private String employeeNumber;

    public DepartmentCreditsBean() {

    }

    public DepartmentCreditsBean(Department department, String employeeNumber) {
        super();
        setDepartment(department);
        setEmployeeNumber(employeeNumber);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    @Atomic
    public void assignPermission(Employee employee) {
        employee.getPerson().getManageableDepartmentCreditsSet().add(department);
        RoleType.grant(RoleType.DEPARTMENT_CREDITS_MANAGER, employee.getPerson().getUser());
        RoleType.grant(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, employee.getPerson().getUser());
    }

    @Atomic
    public void removePermission(Employee employee) {
        if (!hasMultipleDepartments(employee)) {
            RoleType.revoke(RoleType.DEPARTMENT_CREDITS_MANAGER, employee.getPerson().getUser());
            RoleType.revoke(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, employee.getPerson().getUser());
        }
        employee.getPerson().getManageableDepartmentCreditsSet().remove(department);
    }

    public boolean hasMultipleDepartments(Employee employee) {
        int count = 0;
        for (Department department : Bennu.getInstance().getDepartmentsSet()) {
            if (department.getAssociatedPersonsSet().contains(employee.getPerson())) {
                count++;
                if (count > 1) {
                    return true;
                }
            }
        }
        return false;
    }

}
