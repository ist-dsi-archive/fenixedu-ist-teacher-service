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
 * Nov 21, 2005
 */
package pt.ist.fenixedu.teacher.ui.struts.action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.exceptions.DomainException;
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

import pt.ist.fenixedu.teacher.domain.SupportLesson;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Mapping(path = "/supportLessonsManagement", functionality = ViewTeacherCreditsDA.class)
@Forwards(value = {
        @Forward(name = "successfull-delete", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "successfull-edit", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "edit-support-lesson", path = "/credits/supportLessons/editSupportLesson.jsp"),
        @Forward(name = "list-support-lessons", path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails"),
        @Forward(name = "teacher-not-found", path = "/credits.do?method=viewAnnualTeachingCredits") })
@Exceptions(value = { @ExceptionHandling(type = org.fenixedu.academic.domain.exceptions.DomainException.class,
        handler = org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ManageTeacherSupportLessonsDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        SupportLesson supportLesson = getDomainObject(request, "supportLessonID");
        Professorship professorship =
                supportLesson == null ? getDomainObject(request, "professorshipID") : supportLesson.getProfessorship();

        if (professorship == null || professorship.getTeacher() == null || !canManageTeacherCredits(professorship)) {
            return mapping.findForward("teacher-not-found");
        }
        request.setAttribute("professorship", professorship);
        request.setAttribute("supportLesson", supportLesson);
        return mapping.findForward("edit-support-lesson");
    }

    private boolean canManageTeacherCredits(Professorship professorship) {
        User loggedUser = Authenticate.getUser();
        return loggedUser.getPerson().equals(professorship.getTeacher().getPerson())
                || RoleType.SCIENTIFIC_COUNCIL.isMember(loggedUser)
                || loggedUser
                        .getPerson()
                        .getManageableDepartmentCreditsSet()
                        .contains(
                                professorship
                                        .getTeacher()
                                        .getDepartment(
                                                professorship.getExecutionCourse().getExecutionPeriod().getAcademicInterval())
                                        .orElse(null));
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        SupportLesson supportLesson = getDomainObject(request, "supportLessonID");
        Professorship professorship =
                supportLesson == null ? getDomainObject(request, "professorshipID") : supportLesson.getProfessorship();
        request.setAttribute("professorshipID", professorship.getExternalId());
        try {
            supportLesson.delete();
        } catch (DomainException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
            saveMessages(request, actionMessages);
        }
        return mapping.findForward("successfull-delete");
    }
}
