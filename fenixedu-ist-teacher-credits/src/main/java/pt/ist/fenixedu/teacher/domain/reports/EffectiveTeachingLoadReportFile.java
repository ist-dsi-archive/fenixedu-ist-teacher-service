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
package pt.ist.fenixedu.teacher.domain.reports;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;

import pt.ist.fenixedu.teacher.domain.teacher.DegreeTeachingService;
import pt.ist.fenixedu.teacher.domain.teacher.DegreeTeachingServiceCorrection;
import pt.ist.fenixedu.teacher.domain.teacher.OtherService;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EffectiveTeachingLoadReportFile extends EffectiveTeachingLoadReportFile_Base {

    public EffectiveTeachingLoadReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem das CLE";
    }

    @Override
    protected String getPrefix() {
        return "Listagem das CLE";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        ExecutionYear executionYear = getExecutionYear();
        spreadsheet.setName("CLE_" + executionYear.getQualifiedName().replace("/", ""));
        spreadsheet.setHeader("OID_EXECUTION_COURSE");
        spreadsheet.setHeader("OID_TEACHER");
        spreadsheet.setHeader("IstId");
        spreadsheet.setHeader("CLE");

        Map<Teacher, Map<ExecutionCourse, BigDecimal>> teachingLoad = getLoad(executionYear);
        for (Teacher teacher : teachingLoad.keySet()) {
            Map<ExecutionCourse, BigDecimal> executionCourseLoad = teachingLoad.get(teacher);
            for (ExecutionCourse executionCourse : executionCourseLoad.keySet()) {
                final Row row = spreadsheet.addRow();
                row.setCell(executionCourse.getExternalId());
                row.setCell(teacher.getExternalId());
                row.setCell(teacher.getPerson().getUsername());
                row.setCell(executionCourseLoad.get(executionCourse));
            }
        }
    }

    protected Map<Teacher, Map<ExecutionCourse, BigDecimal>> getLoad(ExecutionYear executionYear) {
        Map<Teacher, Map<ExecutionCourse, BigDecimal>> teachingLoad = new HashMap<Teacher, Map<ExecutionCourse, BigDecimal>>();
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (TeacherService teacherService : executionSemester.getTeacherServicesSet()) {
                for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {
                    double efectiveLoad = degreeTeachingService.getEfectiveLoad();
                    if ((!degreeTeachingService.getProfessorship().getExecutionCourse().isDissertation())
                            && (!degreeTeachingService.getProfessorship().getExecutionCourse().getProjectTutorialCourse())
                            && efectiveLoad != 0.0) {
                        Map<ExecutionCourse, BigDecimal> executionCourseLoad =
                                teachingLoad.get(degreeTeachingService.getTeacherService().getTeacher());
                        if (executionCourseLoad == null) {
                            executionCourseLoad = new HashMap<ExecutionCourse, BigDecimal>();
                        }
                        BigDecimal load = executionCourseLoad.get(degreeTeachingService.getProfessorship().getExecutionCourse());
                        if (load == null) {
                            load = BigDecimal.ZERO;
                        }
                        load = load.add(new BigDecimal(efectiveLoad).setScale(2, BigDecimal.ROUND_HALF_UP));
                        executionCourseLoad.put(degreeTeachingService.getProfessorship().getExecutionCourse(), load);
                        teachingLoad.put(degreeTeachingService.getTeacherService().getTeacher(), executionCourseLoad);
                    }
                }
                for (OtherService otherService : teacherService.getOtherServices()) {
                    if (otherService instanceof DegreeTeachingServiceCorrection) {
                        DegreeTeachingServiceCorrection degreeTeachingServiceCorrection =
                                (DegreeTeachingServiceCorrection) otherService;
                        if (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().getProjectTutorialCourse()) {
                            Map<ExecutionCourse, BigDecimal> executionCourseLoad =
                                    teachingLoad.get(degreeTeachingServiceCorrection.getTeacherService().getTeacher());
                            if (executionCourseLoad == null) {
                                executionCourseLoad = new HashMap<ExecutionCourse, BigDecimal>();
                            }
                            BigDecimal load =
                                    executionCourseLoad.get(degreeTeachingServiceCorrection.getProfessorship()
                                            .getExecutionCourse());
                            if (load == null) {
                                load = BigDecimal.ZERO;
                            }
                            load = load.add(degreeTeachingServiceCorrection.getCorrection());
                            executionCourseLoad
                                    .put(degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse(), load);
                            teachingLoad.put(degreeTeachingServiceCorrection.getTeacherService().getTeacher(),
                                    executionCourseLoad);
                        }
                    }
                }
            }
        }
        return teachingLoad;
    }

}
