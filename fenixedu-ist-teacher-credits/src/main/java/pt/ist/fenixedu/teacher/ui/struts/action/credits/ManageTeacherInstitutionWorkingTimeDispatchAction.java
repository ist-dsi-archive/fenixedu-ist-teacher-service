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
/**
 * Nov 23, 2005
 */
package pt.ist.fenixedu.teacher.ui.struts.action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixedu.teacher.domain.teacher.InstitutionWorkTime;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Mapping(path = "/institutionWorkingTimeManagement", input = "/institutionWorkingTimeManagement.do?method=prepareEdit&page=0",
        scope = "request", parameter = "method", functionality = ViewTeacherCreditsDA.class)
@Exceptions(value = { @ExceptionHandling(type = org.fenixedu.academic.domain.exceptions.DomainException.class,
        handler = org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler.class, scope = "request") })
@Forwards({
        @Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits",
                contextRelative = false),
        @Forward(name = "edit-institution-work-time", path = "/credits/workingTime/editTeacherInstitutionWorkTime.jsp") })
public class ManageTeacherInstitutionWorkingTimeDispatchAction extends FenixDispatchAction {

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException, FenixServiceException {
        Teacher teacher = getDomainObject(request, "teacherId");
        ExecutionSemester executionPeriod = getDomainObject(request, "executionPeriodId");
        TeacherService teacherService = TeacherService.getTeacherService(teacher, executionPeriod);
        request.setAttribute("teacherService", teacherService);
        return mapping.findForward("edit-institution-work-time");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        InstitutionWorkTime institutionWorkTime = getDomainObject(request, "institutionWorkTimeOid");
        if (!canManageTeacherCredits(institutionWorkTime)) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("message.invalid.teacher"));
            saveMessages(request, actionMessages);
            return mapping.findForward("viewAnnualTeachingCredits");
        }
        request.setAttribute("institutionWorkTime", institutionWorkTime);
        return mapping.findForward("edit-institution-work-time");
    }

    private boolean canManageTeacherCredits(InstitutionWorkTime institutionWorkTime) {
        User loggedUser = Authenticate.getUser();
        Teacher teacher = institutionWorkTime.getTeacherService().getTeacher();
        return loggedUser.getPerson().equals(teacher.getPerson())
                || RoleType.SCIENTIFIC_COUNCIL.isMember(loggedUser)
                || loggedUser
                        .getPerson()
                        .getManageableDepartmentCreditsSet()
                        .contains(
                                teacher.getDepartment(institutionWorkTime.getTeacherService().getExecutionPeriod()
                                        .getAcademicInterval()));
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException, FenixServiceException {
        InstitutionWorkTime institutionWorkTime = getDomainObject(request, "institutionWorkTimeOid");
        request.setAttribute("teacherOid", institutionWorkTime.getTeacherService().getTeacher().getExternalId());
        request.setAttribute("executionYearOid", institutionWorkTime.getTeacherService().getExecutionPeriod().getExecutionYear()
                .getExternalId());
        institutionWorkTime.delete();
        return mapping.findForward("viewAnnualTeachingCredits");
    }

}
