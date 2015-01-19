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
package pt.ist.fenixedu.teacher.ui.struts.action;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.service.services.manager.MergeExecutionCourses;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

@WebListener
public class FenixEduISTTeacherServiceContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FenixFramework.getDomainModel()
                .registerDeletionBlockerListener(
                        Professorship.class,
                        (professorship, blockers) -> {
                            if (!professorship.getSupportLessonsSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnySupportLessons"));
                            }
                            if (!professorship.getDegreeTeachingServicesSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnyDegreeTeachingServices"));
                            }
                            if (!professorship.getDegreeProjectTutorialServicesSet().isEmpty()) {
                                blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                        "error.remove.professorship.hasAnyDegreeProjectTutorialServices"));
                            }
                        });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(
                Shift.class,
                (shift, blockers) -> {
                    if (!shift.getDegreeTeachingServicesSet().isEmpty()) {
                        blockers.add(BundleUtil.getString(Bundle.RESOURCE_ALLOCATION,
                                "error.deleteShift.with.degreeTeachingServices", shift.getNome()));
                    }
                });
        FenixFramework.getDomainModel().registerDeletionBlockerListener(
                Attends.class,
                (attends, blockers) -> {
                    if (!attends.getDegreeProjectTutorialServicesSet().isEmpty()) {
                        blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                                "error.attends.cant.delete.has.degree.project.tutorial.services"));
                    }
                });

        MergeExecutionCourses.registerMergeHandler(FenixEduISTTeacherServiceContextListener::copyProfessorships);
    }

    private static void copyProfessorships(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        for (Professorship professorship : executionCourseFrom.getProfessorshipsSet()) {
            Professorship otherProfessorship = findProfessorShip(executionCourseTo, professorship.getPerson());

            for (; !professorship.getSupportLessonsSet().isEmpty(); otherProfessorship.addSupportLessons(professorship
                    .getSupportLessonsSet().iterator().next())) {
                ;
            }
            for (; !professorship.getDegreeTeachingServicesSet().isEmpty(); otherProfessorship
                    .addDegreeTeachingServices(professorship.getDegreeTeachingServicesSet().iterator().next())) {
                ;
            }
        }
    }

    private static Professorship findProfessorShip(final ExecutionCourse executionCourseTo, final Person person) {
        for (final Professorship professorship : executionCourseTo.getProfessorshipsSet()) {
            if (professorship.getPerson() == person) {
                return professorship;
            }
        }
        return null;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
