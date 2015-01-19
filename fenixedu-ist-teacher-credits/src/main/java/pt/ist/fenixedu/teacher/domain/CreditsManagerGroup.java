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
package pt.ist.fenixedu.teacher.domain;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("creditsManager")
public class CreditsManagerGroup extends GroupStrategy {

    @Override
    public String getPresentationName() {
        return "Credits Manager";
    }

    @Override
    public Set<User> getMembers() {
        Set<User> members = new HashSet<User>();
        for (Department department : Bennu.getInstance().getDepartmentsSet()) {
            for (Person person : department.getAssociatedPersonsSet()) {
                members.add(person.getUser());
            }
        }
        return members;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && !user.getPerson().getManageableDepartmentCreditsSet().isEmpty();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
