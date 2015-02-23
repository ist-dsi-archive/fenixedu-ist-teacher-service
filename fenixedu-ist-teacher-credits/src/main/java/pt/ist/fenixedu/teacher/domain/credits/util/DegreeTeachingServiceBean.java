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
package pt.ist.fenixedu.teacher.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixedu.teacher.domain.teacher.DegreeTeachingService;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherService;
import pt.ist.fenixedu.teacher.domain.teacher.TeacherServiceLog;
import pt.ist.fenixframework.Atomic;

public class DegreeTeachingServiceBean implements Serializable {

    protected Professorship professorship;

    protected Set<ShiftServiceBean> shiftServiceSet = new TreeSet<ShiftServiceBean>();

    public DegreeTeachingServiceBean(Professorship professorship) {
        setProfessorship(professorship);
        for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {
            shiftServiceSet.add(new ShiftServiceBean(shift));
        }
    }

    public Professorship getProfessorship() {
        return professorship;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorship = professorship;
    }

    public Set<ShiftServiceBean> getShiftServiceSet() {
        return shiftServiceSet;
    }

    public void setShiftServiceSet(Set<ShiftServiceBean> shiftServiceSet) {
        this.shiftServiceSet = shiftServiceSet;
    }

    @Atomic
    public void updateDegreeTeachingServices() {
        Teacher teacher = professorship.getTeacher();
        ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
        TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionSemester);
        }

        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.teacher.schedule.change"));

        for (ShiftServiceBean shiftServiceBean : shiftServiceSet) {
            DegreeTeachingService degreeTeachingService =
                    teacherService.getDegreeTeachingServiceByShiftAndProfessorship(shiftServiceBean.getShift(), professorship);

            if (degreeTeachingService != null) {
                degreeTeachingService.updatePercentage(shiftServiceBean.getPercentage());
            } else {
                if (shiftServiceBean.getPercentage() != null && shiftServiceBean.getPercentage() != 0) {
                    new DegreeTeachingService(teacherService, professorship, shiftServiceBean.getShift(),
                            shiftServiceBean.getPercentage());
                }
            }

            if (shiftServiceBean.getPercentage() != null) {
                log.append(shiftServiceBean.getShift().getPresentationName());
                log.append("= ");
                log.append(shiftServiceBean.getPercentage());
                log.append("% ; ");
            }
        }

        new TeacherServiceLog(teacherService, log.toString());
    }

    public class ShiftServiceBean implements Serializable, Comparable<ShiftServiceBean> {

        Shift shift;
        Double percentage;
        Double availablePercentage;

        public ShiftServiceBean(Shift shift) {
            setShift(shift);
            Double availablePercentage = TeacherService.getAvailableShiftPercentage(shift, professorship);
            setAvailablePercentage(availablePercentage);
            DegreeTeachingService degreeTeachingService = getDegreeTeachingService();
            if (degreeTeachingService != null) {
                setPercentage(degreeTeachingService.getPercentage());
            }
        }

        private DegreeTeachingService getDegreeTeachingService() {
            for (DegreeTeachingService degreeTeachingService : shift.getDegreeTeachingServicesSet()) {
                if (professorship == degreeTeachingService.getProfessorship()) {
                    return degreeTeachingService;
                }
            }
            return null;
        }

        public Shift getShift() {
            return shift;
        }

        public void setShift(Shift shift) {
            this.shift = shift;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public Double getAvailablePercentage() {
            return availablePercentage;
        }

        public void setAvailablePercentage(Double availablePercentage) {
            this.availablePercentage = availablePercentage;
        }

        public String getAppliedShiftTeachingService() {
            List<String> appliedShiftTeachingService = new ArrayList<String>();
            for (DegreeTeachingService degreeTeachingService : getShift().getDegreeTeachingServicesSet()) {
                appliedShiftTeachingService.add(degreeTeachingService.getProfessorship().getPerson().getName() + " "
                        + ((Math.round(degreeTeachingService.getPercentage().doubleValue() * 100.0)) / 100.0));
            }
            return String.join("\n", appliedShiftTeachingService);
        }

        @Override
        public int compareTo(ShiftServiceBean o) {
            return Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS.compare(getShift(), o.getShift());
        }
    }

}