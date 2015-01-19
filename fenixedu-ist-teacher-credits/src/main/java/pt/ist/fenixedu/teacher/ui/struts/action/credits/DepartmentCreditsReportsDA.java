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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixedu.teacher.domain.credits.AnnualCreditsState;
import pt.ist.fenixedu.teacher.domain.credits.AnnualTeachingCredits;
import pt.ist.fenixedu.teacher.domain.credits.util.DepartmentCreditsBean;
import pt.ist.fenixedu.teacher.domain.teacher.DegreeTeachingServiceCorrection;
import pt.ist.fenixedu.teacher.domain.teacher.OtherService;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.ist.fenixedu.teacher.ui.struts.action.DepartmentCreditsManagerApp;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@StrutsFunctionality(app = DepartmentCreditsManagerApp.class, path = "export-department-credits",
        titleKey = "label.department.credits")
@Mapping(path = "/departmentCredits")
@Forwards(@Forward(name = "exportDepartmentCredits", path = "/credits/export/exportDepartmentCredits.jsp"))
public class DepartmentCreditsReportsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareExportDepartmentCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("exportDepartmentCredits");
    }

    public ActionForward exportDepartmentCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        List<Department> departments = new ArrayList<Department>();
        if (departmentCreditsBean.getDepartment() != null) {
            departments.add(departmentCreditsBean.getDepartment());
        } else {
            departments.addAll(departmentCreditsBean.getAvailableDepartments());
        }
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();
        for (Department department : departments) {
            spreadsheet.getSheet(department.getAcronym());
            spreadsheet.newHeaderRow();

            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.teacher.id", Unit.getInstitutionAcronym()));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.name"), 10000);

            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.teachingCredits.simpleCode"));
            //spreadsheet.addHeader("CL correcções");
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.masterDegreeTheses.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.phdDegreeTheses.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.projectsAndTutorials.simpleCode"));

            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.managementPositions.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.otherCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.creditsReduction.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS,
                    "label.credits.serviceExemptionSituations.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS,
                    "label.credits.normalizedAcademicCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.yearCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.finalCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.accumulatedCredits.simpleCode"));

            AnnualCreditsState annualCreditsState =
                    AnnualCreditsState.getAnnualCreditsState(departmentCreditsBean.getExecutionYear());
            if (annualCreditsState.getIsFinalCreditsCalculated()) {
                for (AnnualTeachingCredits annualTeachingCredits : annualCreditsState.getAnnualTeachingCreditsSet()) {
                    Teacher teacher = annualTeachingCredits.getTeacher();
                    Department teacherDepartment =
                            teacher.getLastDepartment(departmentCreditsBean.getExecutionYear().getAcademicInterval());
                    if (teacherDepartment != null && teacherDepartment.equals(department)) {
                        spreadsheet.newRow();
                        spreadsheet.addCell(teacher.getTeacherId());
                        spreadsheet.addCell(teacher.getPerson().getName());
                        spreadsheet.addCell(annualTeachingCredits.getTeachingCredits().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .doubleValue());

//                        BigDecimal correcredCL = getCorrectedCL(departmentCreditsBean, teacher);
//                        spreadsheet.addCell(correcredCL.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

                        spreadsheet.addCell(annualTeachingCredits.getMasterDegreeThesesCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getPhdDegreeThesesCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getProjectsTutorialsCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getManagementFunctionCredits()
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getOthersCredits().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .doubleValue());
                        spreadsheet.addCell("-");
                        spreadsheet.addCell(annualTeachingCredits.getServiceExemptionCredits()
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getAnnualTeachingLoad().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getYearCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getFinalCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getAccumulatedCredits());
                    }
                }
            }
        }

        response.setContentType("text/plain");
        StringBuilder filename = new StringBuilder("creditos");
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

    protected BigDecimal getCorrectedCL(DepartmentCreditsBean departmentCreditsBean, Teacher teacher) {
        BigDecimal correcredCL = BigDecimal.ZERO;
        for (ExecutionSemester executionSemester : departmentCreditsBean.getExecutionYear().getExecutionPeriodsSet()) {
            TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
            if (teacherService != null) {
                for (OtherService otherService : teacherService.getOtherServices()) {
                    if (otherService instanceof DegreeTeachingServiceCorrection) {
                        DegreeTeachingServiceCorrection degreeTeachingServiceCorrection =
                                (DegreeTeachingServiceCorrection) otherService;
                        if ((!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().isDissertation())
                                && (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse()
                                        .getProjectTutorialCourse())) {
                            correcredCL =
                                    correcredCL.add(degreeTeachingServiceCorrection.getCorrection().multiply(
                                            degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse()
                                                    .getUnitCreditValue()));
                        }
                    }
                }
            }
        }
        return correcredCL;
    }
}