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
package pt.ist.fenixedu.teacher.ui.struts.action.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import pt.ist.fenixedu.teacher.domain.credits.util.DepartmentCreditsBean;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.ist.fenixedu.teacher.ui.struts.action.DepartmentCreditsManagerApp;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@StrutsFunctionality(app = DepartmentCreditsManagerApp.class, path = "department-teacher-service",
        titleKey = "link.teacherService", bundle = "DepartmentAdmOfficeResources")
@Mapping(path = "/departmentTeacherService")
@Forwards(@Forward(name = "viewDepartmentTeacherService", path = "/credits/viewDepartmentTeacherService.jsp"))
public class ViewDepartmentTeacherServiceDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareViewDepartmentTeacherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("viewDepartmentTeacherService");
    }

    public ActionForward exportDepartmentTeacherService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        List<Department> departments = new ArrayList<Department>();
        if (departmentCreditsBean.getDepartment() != null) {
            departments.add(departmentCreditsBean.getDepartment());
        } else {
            departments.addAll(departmentCreditsBean.getAvailableDepartments());
        }
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();
        PeriodFormatter periodFormatter =
                new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours().appendSuffix(":")
                        .appendMinutes().toFormatter();
        for (Department department : departments) {
            spreadsheet.getSheet(department.getAcronym());
            spreadsheet.newHeaderRow();
            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.name"), 10000);
            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.degrees"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.semester"));

            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                    "label.teacherService.course.firstTimeEnrolledStudentsNumber"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                    "label.teacherService.course.secondTimeEnrolledStudentsNumber"));

            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER,
                    "label.teacherService.course.totalStudentsNumber"));

            org.fenixedu.academic.domain.ShiftType[] values = org.fenixedu.academic.domain.ShiftType.values();
            for (ShiftType shiftType : values) {
                spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherServiceDistribution.hours")
                        + " " + shiftType.getFullNameTipoAula());
                //    spreadsheet.addHeader("# Alunos / # Turnos " + shiftType.getFullNameTipoAula());
            }

            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.totalHours"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.DEPARTMENT_MEMBER, "label.teacherService.course.availability"));

            for (ExecutionSemester executionSemester : departmentCreditsBean.getExecutionYear().getExecutionPeriodsSet()) {
                for (ExecutionCourse executionCourse : departmentCreditsBean.getDepartment().getDepartmentUnit()
                        .getAllExecutionCoursesByExecutionPeriod(executionSemester)) {

                    spreadsheet.newRow();
                    spreadsheet.addCell(executionCourse.getNome());
                    spreadsheet.addCell(getDegreeSiglas(executionCourse));
                    spreadsheet.addCell(executionCourse.getExecutionPeriod().getSemester());

                    int executionCourseFirstTimeEnrollementStudentNumber = executionCourse.getFirstTimeEnrolmentStudentNumber();
                    int totalStudentsNumber = executionCourse.getTotalEnrolmentStudentNumber();
                    int executionCourseSecondTimeEnrollementStudentNumber =
                            totalStudentsNumber - executionCourseFirstTimeEnrollementStudentNumber;
                    spreadsheet.addCell(executionCourseFirstTimeEnrollementStudentNumber);
                    spreadsheet.addCell(executionCourseSecondTimeEnrollementStudentNumber);
                    spreadsheet.addCell(totalStudentsNumber);

                    Double totalHours = 0.0;
                    for (ShiftType shiftType : values) {
                        Double shiftHours = executionCourse.getAllShiftUnitHours(shiftType).doubleValue();
                        totalHours += shiftHours;
                        spreadsheet.addCell(shiftHours);

//                        Integer numberOfShifts = executionCourse.getNumberOfShifts(shiftType);
//                        spreadsheet.addCell(numberOfShifts == 0 ? 0 : (double) totalStudentsNumber / numberOfShifts);

                    }

                    Duration totalShiftsDuration = executionCourse.getTotalShiftsDuration();
                    spreadsheet.addCell(periodFormatter.print(totalShiftsDuration.toPeriod()));
                    int colNum = spreadsheet.getNextWritableCell();
                    spreadsheet.addCell("");

                    Duration totalLecturedDuration = Duration.ZERO;
                    for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
                        Teacher teacher = professorship.getTeacher();
                        if (teacher != null) {
                            Duration teacherLecturedTime =
                                    TeacherService.getLecturedDurationOnExecutionCourse(teacher, executionCourse);
                            totalLecturedDuration = totalLecturedDuration.plus(teacherLecturedTime);
                            spreadsheet.addCell(teacher.getPerson().getUsername());
                            spreadsheet.addCell(teacher.getPerson().getName());
                            spreadsheet.addCell(periodFormatter.print(teacherLecturedTime.toPeriod()));
                        }

                    }

                    spreadsheet.addCell(periodFormatter.print(totalShiftsDuration.minus(totalLecturedDuration).toPeriod()),
                            colNum);
                }
            }

        }

        response.setContentType("text/plain");
        StringBuilder filename = new StringBuilder("servicoDocencia");
        filename.append((departments.size() == 1 ? departments.iterator().next().getAcronym() : "Departamentos"));
        filename.append("_").append(departmentCreditsBean.getExecutionYear().getQualifiedName().replaceAll("/", "_"))
                .append(".xls");
        response.setHeader("Content-disposition", "attachment; filename=" + filename.toString());
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    public String getDegreeSiglas(ExecutionCourse executionCourse) {
        Set<String> degreeSiglas = new HashSet<String>();
        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            degreeSiglas.add(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
        }
        return StringUtils.join(degreeSiglas, ", ");
    }
}