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
package pt.ist.fenixedu.teacher.domain;

import java.util.Comparator;
import java.util.Date;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CalendarUtil;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.academic.util.WeekDay;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherServiceLog;
import pt.ist.fenixedu.teacher.domain.time.calendarStructure.TeacherCreditsFillingCE;
import pt.ist.fenixedu.teacher.util.date.TimePeriod;
import pt.ist.fenixframework.Atomic;

/**
 * @author Fernanda Quit�rio 17/10/2003
 * @author jpvl
 * @author Ricardo Rodrigues
 */
public class SupportLesson extends SupportLesson_Base {

    public static final Comparator<SupportLesson> SUPPORT_LESSON_COMPARATOR_BY_HOURS_AND_WEEK_DAY =
            new Comparator<SupportLesson>() {

                @Override
                public int compare(SupportLesson o1, SupportLesson o2) {
                    int c = o1.getWeekDay().getDiaSemana().compareTo(o2.getWeekDay().getDiaSemana());
                    if (c == 0) {
                        c = o1.getStartTimeHourMinuteSecond().compareTo(o2.getStartTimeHourMinuteSecond());
                    }
                    return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
                }

            };

    public SupportLesson(Professorship professorship, WeekDay weekDay, HourMinuteSecond startTimeHourMinuteSecond,
            HourMinuteSecond endTimeHourMinuteSecond, String place) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setProfessorship(professorship);
        update(weekDay, startTimeHourMinuteSecond, endTimeHourMinuteSecond, place);
        addLog("label.teacher.schedule.supportLessons.create");
    }

    public void edit(WeekDay weekDay, HourMinuteSecond startTimeHourMinuteSecond, HourMinuteSecond endTimeHourMinuteSecond,
            String place) {
        update(weekDay, startTimeHourMinuteSecond, endTimeHourMinuteSecond, place);
        addLog("label.teacher.schedule.supportLessons.change");
    }

    private void update(WeekDay weekDay, HourMinuteSecond startTimeHourMinuteSecond, HourMinuteSecond endTimeHourMinuteSecond,
            String place) {
        setWeekDay(DiaSemana.fromJodaWeekDay(weekDay.ordinal() + 1));
        setStartTimeHourMinuteSecond(startTimeHourMinuteSecond);
        setEndTimeHourMinuteSecond(endTimeHourMinuteSecond);
        setPlace(place);
        verifyOverlappings();
    }

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionSemester);
    }

    public void verifyOverlappings() {
        Teacher teacher = getProfessorship().getTeacher();
        TeacherService teacherService =
                TeacherService.getTeacherService(teacher, getProfessorship().getExecutionCourse().getExecutionPeriod());
        verifyOverlappingWithOtherSupportLessons(teacherService);
    }

    private void verifyOverlappingWithOtherSupportLessons(TeacherService teacherService) {
        for (SupportLesson supportLesson : teacherService.getSupportLessons()) {
            if (supportLesson != this) {
                if (supportLesson.getWeekDay().equals(getWeekDay())) {
                    Date supportLessonStart = supportLesson.getStartTime();
                    Date supportLessonEnd = supportLesson.getEndTime();
                    if (CalendarUtil.intersectTimes(getStartTime(), getEndTime(), supportLessonStart, supportLessonEnd)) {
                        throw new DomainException("message.overlapping.support.lesson.period");
                    }
                }
            }
        }
    }

    @Override
    public void setPlace(String place) {
        final int maxPlaceChars = 50;
        if (place != null && place.length() > maxPlaceChars) {
            throw new DomainException("error.place.cannot.have.more.than.characters", Integer.toString(maxPlaceChars));
        }
        super.setPlace(place);
    }

    private SupportLesson() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Atomic
    public void delete() {
        TeacherCreditsFillingCE.checkValidCreditsPeriod(getProfessorship().getExecutionCourse().getExecutionPeriod(),
                Authenticate.getUser());
        addLog("label.teacher.schedule.supportLessons.delete");

        setProfessorship(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getEndTime() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getEndTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndTime(java.util.Date date) {
        if (date == null) {
            setEndTimeHourMinuteSecond(null);
        } else {
            setEndTimeHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartTime() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getStartTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setStartTime(java.util.Date date) {
        if (date == null) {
            setStartTimeHourMinuteSecond(null);
        } else {
            setStartTimeHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    public WeekDay getWeekDayObject() {
        final DiaSemana diaSemana = getWeekDay();
        return diaSemana == null ? null : WeekDay.getWeekDay(diaSemana);
    }

    private void addLog(String key) {
        TeacherService teacherService =
                TeacherService.getTeacherService(getProfessorship().getTeacher(), getProfessorship().getExecutionCourse()
                        .getExecutionPeriod());

        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS, key));

        log.append(WeekDay.getWeekDay(getWeekDay()).getLabel());
        log.append(" ");
        log.append(getStartTime().getHours());
        log.append(":");
        log.append(getStartTime().getMinutes());
        log.append(" - ");
        log.append(getEndTime().getHours());
        log.append(":");
        log.append(getEndTime().getMinutes());
        log.append(" - ");
        log.append(getPlace());

        new TeacherServiceLog(teacherService, log.toString());
    }

}
