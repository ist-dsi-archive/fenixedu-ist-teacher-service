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
package pt.ist.fenixedu.teacher.domain;

import java.util.Comparator;
import java.util.Date;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.util.CalendarUtil;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.WeekDay;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.ist.fenixedu.teacher.domain.time.calendarStructure.TeacherCreditsFillingCE;
import pt.ist.fenixedu.teacher.dto.teacher.professorship.SupportLessonDTO;
import pt.ist.fenixedu.teacher.util.date.TimePeriod;

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
                    final int c = o1.getWeekDay().getDiaSemana().compareTo(o2.getWeekDay().getDiaSemana());
                    return c == 0 ? o1.getStartTimeHourMinuteSecond().compareTo(o2.getStartTimeHourMinuteSecond()) : c;
                }

            };

    public SupportLesson(SupportLessonDTO supportLessonDTO, Professorship professorship) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setProfessorship(professorship);
        update(supportLessonDTO);
    }

    public void update(SupportLessonDTO supportLessonDTO) {
        setEndTime(supportLessonDTO.getEndTime());
        setStartTime(supportLessonDTO.getStartTime());
        setPlace(supportLessonDTO.getPlace());
        setWeekDay(supportLessonDTO.getWeekDay());
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
                TeacherService.getTeacherServiceByExecutionPeriod(teacher, getProfessorship().getExecutionCourse()
                        .getExecutionPeriod());
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

    public static SupportLesson create(SupportLessonDTO supportLessonDTO, Professorship professorship, RoleType roleType) {
        final SupportLesson supportLesson = new SupportLesson();
        supportLesson.setProfessorship(professorship);
        TeacherCreditsFillingCE.checkValidCreditsPeriod(supportLesson.getProfessorship().getExecutionCourse()
                .getExecutionPeriod(), Authenticate.getUser());
        supportLesson.setEndTime(supportLessonDTO.getEndTime());
        supportLesson.setStartTime(supportLessonDTO.getStartTime());
        supportLesson.setPlace(supportLessonDTO.getPlace());
        supportLesson.setWeekDay(supportLessonDTO.getWeekDay());
        return supportLesson;
    }

    public void delete() {
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

}
