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
package pt.ist.fenixedu.teacher.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class DepartmentCreditsAuthorizationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Department department;
    private Person person;
    private Set<Department> availableDepartments;

    public DepartmentCreditsAuthorizationBean() {

    }

    public DepartmentCreditsAuthorizationBean(Department department, Person person) {
        super();
        setDepartment(department);
        setPerson(person);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Department> getAvailableDepartments() {
        User userView = Authenticate.getUser();
        if (RoleType.SCIENTIFIC_COUNCIL.isMember(userView)) {
            return new HashSet(Department.readActiveDepartments());
        }
        return userView.getPerson().getManageableDepartmentCreditsSet();
    }

    @Atomic
    public boolean assignPermission() {
        if (getDepartment() == null || getPerson() == null) {
            return false;
        }
        getPerson().getManageableDepartmentCreditsSet().add(getDepartment());
        return true;
    }

    @Atomic
    public void removePermission() {
        getPerson().getManageableDepartmentCreditsSet().remove(getDepartment());
    }

}
