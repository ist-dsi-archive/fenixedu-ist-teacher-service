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
package pt.ist.fenixedu.teacher.ui.struts.action.credits.departmentMember;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixedu.teacher.ui.struts.action.credits.ManageTeacherServiceCommentsDispatchAction;

@Mapping(module = "departmentMember", path = "/teacherServiceComments",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forwards({ @Forward(name = "viewAnnualTeachingCredits", path = "/departmentMember/credits.do?method=viewAnnualTeachingCredits") })
public class DepartmentMemberManageTeacherServiceCommentsDispatchAction extends ManageTeacherServiceCommentsDispatchAction {
}
