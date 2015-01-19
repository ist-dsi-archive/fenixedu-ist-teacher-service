/**
 * Copyright © 2011 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Teacher Credits.
 *
 * FenixEdu Teacher Credits is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Teacher Credits is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Teacher Credits.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.teacher.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixedu.contracts.domain.organizationalStructure.PersonFunction;

public class DepartmentCreditsBean implements Serializable {

    protected Department department;

    protected ExecutionSemester executionSemester;

    protected ExecutionYear executionYear;

    protected List<Department> availableDepartments;

    public DepartmentCreditsBean() {
        setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
        setExecutionYear(getExecutionSemester().getExecutionYear());
        User userView = Authenticate.getUser();
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(userView)) {
            setAvailableDepartments(Department.readActiveDepartments());
        } else {
            setAvailableDepartments(new ArrayList<Department>(userView.getPerson().getManageableDepartmentCreditsSet()));
        }
    }

    public DepartmentCreditsBean(Department department, ArrayList<Department> availableDepartments) {
        setExecutionSemester(ExecutionSemester.readActualExecutionSemester());
        setExecutionYear(getExecutionSemester().getExecutionYear());
        setDepartment(department);
        setAvailableDepartments(availableDepartments);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public List<Department> getAvailableDepartments() {
        return availableDepartments;
    }

    public void setAvailableDepartments(List<Department> availableDepartments) {
        this.availableDepartments = availableDepartments;
        if (availableDepartments.size() == 1) {
            setDepartment(availableDepartments.iterator().next());
        }
    }

    public Set<ExecutionCourse> getDepartmentExecutionCourses() {
        Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
        if (department != null) {
            for (ExecutionCourse executionCourse : department.getDepartmentUnit().getAllExecutionCoursesByExecutionPeriod(
                    getExecutionSemester())) {
                if (!executionCourse.isDissertation()) {
                    result.add(executionCourse);
                }
            }
        }
        return result;
    }

    public List<PersonFunction> getDepartmentPersonFunctions() {
        List<PersonFunction> result = new ArrayList<PersonFunction>();
        if (department != null) {
            for (Teacher teacher : department.getAllTeachers(getExecutionSemester())) {
                for (PersonFunction personFuntion : PersonFunction.getPersonFuntions(teacher.getPerson(), getExecutionSemester()
                        .getBeginDateYearMonthDay(), getExecutionSemester().getEndDateYearMonthDay())) {
                    if (!personFuntion.getFunction().isVirtual()) {
                        result.add(personFuntion);
                    }
                }
            }
        }
        return result;
    }
}