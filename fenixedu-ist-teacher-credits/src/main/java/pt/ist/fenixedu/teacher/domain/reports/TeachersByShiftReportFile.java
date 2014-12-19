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
package pt.ist.fenixedu.teacher.domain.reports;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Shift;

import pt.ist.fenixedu.teacher.domain.teacher.DegreeTeachingService;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TeachersByShiftReportFile extends TeachersByShiftReportFile_Base {

    public TeachersByShiftReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de docentes associados a turnos";
    }

    @Override
    protected String getPrefix() {
        return "teachersByShift";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) {

        spreadsheet.setHeader("semestre");
        spreadsheet.setHeader("id docente");
        spreadsheet.setHeader("id turno");
        spreadsheet.setHeader("nome turno");
        spreadsheet.setHeader("id execution course");
        spreadsheet.setHeader("% assegurada pelo docente");
        spreadsheet.setHeader("OID execucao disciplina");
        spreadsheet.setHeader("OID professorship");

        for (ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriodsSet()) {
            for (TeacherService teacherService : executionSemester.getTeacherServicesSet()) {
                for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {

                    final Shift shift = degreeTeachingService.getShift();

                    if (!shift.hasSchoolClassForDegreeType(getDegreeType())) {
                        continue;
                    }

                    Row row = spreadsheet.addRow();
                    row.setCell(executionSemester.getSemester());
                    row.setCell(teacherService.getTeacher().getPerson().getUsername());
                    row.setCell(shift.getExternalId());
                    row.setCell(shift.getNome());
                    row.setCell(shift.getExecutionCourse().getExternalId());
                    row.setCell(degreeTeachingService.getPercentage() != null ? degreeTeachingService.getPercentage().toString()
                            .replace('.', ',') : StringUtils.EMPTY);
                    row.setCell(String.valueOf(shift.getExecutionCourse().getOid()));
                    row.setCell(String.valueOf(degreeTeachingService.getProfessorship().getOid()));
                }
            }
        }
    }
}
