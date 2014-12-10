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
package pt.ist.fenixedu.teacher.service.departmentAdmOffice;

import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixedu.teacher.domain.ExpectationEvaluationGroup;
import pt.ist.fenixframework.Atomic;

public class DeleteExpectationEvaluationGroup {

    @Atomic
    public static void run(ExpectationEvaluationGroup group) {
        User user = Authenticate.getUser();
        if (!Group.parse("creditsManager").isMember(user)) {
            StringBuilder message = new StringBuilder();
            final String username = user == null ? "<nobody>" : user.getUsername();
            message.append("User ");
            message.append(username);
            message.append(" tried to execute method but he/she is not authorized to do so");
            throw new IllegalDataAccessException(message.toString(), user.getPerson());
        }
        if (group != null) {
            group.delete();
        }
    }

}