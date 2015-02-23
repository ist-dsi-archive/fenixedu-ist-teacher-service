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

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Strings;

public class OtherService extends OtherService_Base {

    public OtherService(TeacherService teacherService, Double credits, String reason, ExecutionSemester correctedExecutionSemester) {
        super();
        setValues(teacherService, credits, reason, correctedExecutionSemester);
    }

    public OtherService() {
        super();
    }

    public OtherService(TeacherService teacherService, Double credits, String reason) {
        setValues(teacherService, credits, reason, null);
    }

    public void setValues(TeacherService teacherService, Double credits, String reason,
            ExecutionSemester correctedExecutionSemester) {
        if (teacherService == null || credits == null || Strings.isNullOrEmpty(reason)) {
            throw new DomainException("arguments can't be null");
        }
        setTeacherService(teacherService);
        setCredits(credits);
        setReason(reason);
        setCorrectedExecutionSemester(correctedExecutionSemester != null ? correctedExecutionSemester : teacherService
                .getExecutionPeriod());
        new TeacherServiceLog(teacherService, BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.teacher.otherService",
                credits.toString(), reason, getCorrectedExecutionSemester().getExecutionYear().getQualifiedName()));
    }

    @Override
    @Atomic
    public void delete() {
        new TeacherServiceLog(getTeacherService(), BundleUtil.getString(Bundle.TEACHER_CREDITS,
                "label.teacher.otherService.delete", getCredits().toString(), getReason(), getCorrectedExecutionSemester()
                        .getExecutionYear().getQualifiedName()));
        setTeacherService(null);
        setCorrectedExecutionSemester(null);
        super.delete();
    }

    @Override
    public Double getCredits() {
        return round(super.getCredits());
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }

}
