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
package pt.ist.fenixedu.teacher.ui.struts.action.credits;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.portal.EntryPoint;

import pt.ist.fenixedu.teacher.domain.credits.AnnualTeachingCredits;
import pt.ist.fenixedu.teacher.domain.credits.AnnualTeachingCreditsDocument;
import pt.ist.fenixedu.teacher.domain.credits.util.AnnualTeachingCreditsBean;
import pt.ist.fenixedu.teacher.domain.credits.util.TeacherCreditsBean;
import pt.ist.fenixframework.FenixFramework;

public abstract class ViewTeacherCreditsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        request.setAttribute("teacherBean", new TeacherCreditsBean());
        return mapping.findForward("selectTeacher");
    }

    public abstract ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception;

    public abstract ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception;

    protected ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, boolean departmentAdministrativeOffice) throws NumberFormatException,
            FenixServiceException, Exception {
        Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOid"));
        ExecutionYear executionYear = FenixFramework.getDomainObject((String) getFromRequest(request, "executionYearOid"));
        if (teacher == null) {
            Professorship professorship = FenixFramework.getDomainObject(getStringFromRequest(request, "professorshipID"));
            if (professorship != null) {
                teacher = professorship.getTeacher();
                executionYear = professorship.getExecutionCourse().getExecutionYear();
            }
        }
        AnnualTeachingCreditsBean annualTeachingCreditsBean = null;

        for (AnnualTeachingCredits annualTeachingCredits : teacher.getAnnualTeachingCreditsSet()) {
            if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(executionYear)) {
                if (annualTeachingCredits.isPastResume()) {
                    TeacherCreditsBean teacherBean = new TeacherCreditsBean(teacher);
                    teacherBean.preparePastTeachingCredits();
                    request.setAttribute("teacherBean", teacherBean);
                    return mapping.findForward("showPastTeacherCredits");
                } else {
                    if (annualTeachingCredits.isClosed()) {
                        AnnualTeachingCreditsDocument lastTeacherCreditsDocument =
                                annualTeachingCredits.getLastTeacherCreditsDocument(!departmentAdministrativeOffice);
                        if (lastTeacherCreditsDocument != null) {
                            response.setContentType("application/pdf");
                            response.setHeader("Content-disposition",
                                    "attachment; filename=" + lastTeacherCreditsDocument.getFilename());
                            final OutputStream outputStream = response.getOutputStream();
                            outputStream.write(lastTeacherCreditsDocument.getContents());
                            outputStream.close();
                            return null;
                        }
                    }
                    annualTeachingCreditsBean = new AnnualTeachingCreditsBean(annualTeachingCredits);
                    break;
                }
            }
        }
        if (annualTeachingCreditsBean == null) {
            annualTeachingCreditsBean = new AnnualTeachingCreditsBean(executionYear, teacher);
        }
        request.setAttribute("annualTeachingCreditsBean", annualTeachingCreditsBean);
        request.setAttribute("teacherBean", new TeacherCreditsBean());
        return mapping.findForward("showAnnualTeacherCredits");
    }

    public ActionForward recalculateCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOid"));
        ExecutionYear executionYear = FenixFramework.getDomainObject((String) getFromRequest(request, "executionYearOid"));
        AnnualTeachingCredits annualTeachingCredits = AnnualTeachingCredits.readByYearAndTeacher(executionYear, teacher);
        if (annualTeachingCredits != null) {
            annualTeachingCredits.calculateCredits();
        }
        return viewAnnualTeachingCredits(mapping, form, request, response);
    }

}