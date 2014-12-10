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
/**
 * Nov 23, 2005
 */
package pt.ist.fenixedu.teacher.service.teacher.professorship;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.WeekDay;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixedu.teacher.domain.SupportLesson;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherServiceLog;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteSupportLesson {

    protected void run(String supportLessonID) {
        SupportLesson supportLesson = FenixFramework.getDomainObject(supportLessonID);
        log(supportLesson);
        supportLesson.delete();
    }

    private void log(final SupportLesson supportLesson) {
        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.teacher.schedule.supportLessons.delete"));
        log.append(WeekDay.getWeekDay(supportLesson.getWeekDay()).getLabel());
        log.append(" ");
        log.append(supportLesson.getStartTime().getHours());
        log.append(":");
        log.append(supportLesson.getStartTime().getMinutes());
        log.append(" - ");
        log.append(supportLesson.getEndTime().getHours());
        log.append(":");
        log.append(supportLesson.getEndTime().getMinutes());
        log.append(" - ");
        log.append(supportLesson.getPlace());
        final TeacherService teacherService = getTeacherService(supportLesson);
        new TeacherServiceLog(teacherService, log.toString());
    }

    private TeacherService getTeacherService(final SupportLesson supportLesson) {
        final Professorship professorship = supportLesson.getProfessorship();
        final ExecutionCourse executionCourse = professorship.getExecutionCourse();
        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        final Teacher teacher = professorship.getTeacher();
        return TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
    }

    // Service Invokers migrated from Berserk

    private static final DeleteSupportLesson serviceInstance = new DeleteSupportLesson();

    @Atomic
    public static void runDeleteSupportLesson(String supportLessonID) throws NotAuthorizedException {
        serviceInstance.run(supportLessonID);
    }

}