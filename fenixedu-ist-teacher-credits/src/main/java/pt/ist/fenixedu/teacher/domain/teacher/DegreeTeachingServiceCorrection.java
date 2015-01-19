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
package pt.ist.fenixedu.teacher.domain.teacher;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class DegreeTeachingServiceCorrection extends DegreeTeachingServiceCorrection_Base {

    public DegreeTeachingServiceCorrection(TeacherService teacherService, Professorship professorship, BigDecimal correction,
            String reason, ExecutionSemester correctedExecutionSemester) {
        super();
        if (teacherService == null || professorship == null || correction == null) {
            throw new DomainException("arguments can't be null");
        }
        setTeacherService(teacherService);
        setProfessorship(professorship);
        setCorrection(correction);
        setReason(reason);
        String correctionString = getCorrection().toString();
        if (getCorrection().compareTo(BigDecimal.ZERO) >= 0) {
            correctionString = "+" + correctionString;
        }
        setCorrectedExecutionSemester(correctedExecutionSemester != null ? correctedExecutionSemester : teacherService
                .getExecutionPeriod());
        new TeacherServiceLog(getTeacherService(), BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.schedule.correction", getProfessorship().getExecutionCourse().getName(), getProfessorship()
                        .getExecutionCourse().getDegreePresentationString(), getReason(), correctionString));
    }

    @Override
    public Double getCredits() {
        return getProfessorship().getExecutionCourse().getUnitCreditValue().multiply(getCorrection()).doubleValue();
    }

}
