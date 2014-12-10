package pt.ist.fenixedu.teacher.service.teacher;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author naat
 */
public class ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType {

    public List<ExecutionCourse> run(String teacherID, String executionYearID, DegreeType degreeType)
            throws FenixServiceException {

        Teacher teacher = FenixFramework.getDomainObject(teacherID);

        List<ExecutionCourse> lecturedExecutionCourses;

        if (executionYearID == null) {
            lecturedExecutionCourses = teacher.getAllLecturedExecutionCourses();

        } else {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
            lecturedExecutionCourses = teacher.getLecturedExecutionCoursesByExecutionYear(executionYear);
        }

        List<ExecutionCourse> result;

        if (degreeType == DegreeType.DEGREE) {
            result = filterExecutionCourses(lecturedExecutionCourses, false);
        } else {
            // master degree
            result = filterExecutionCourses(lecturedExecutionCourses, true);
        }

        return result;

    }

    private List<ExecutionCourse> filterExecutionCourses(List<ExecutionCourse> executionCourses, boolean masterDegreeOnly) {
        List<ExecutionCourse> masterDegreeExecutionCourses = new ArrayList<ExecutionCourse>();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.isMasterDegreeDFAOrDEAOnly() == masterDegreeOnly) {
                masterDegreeExecutionCourses.add(executionCourse);
            }
        }

        return masterDegreeExecutionCourses;
    }

    // Service Invokers migrated from Berserk

    private static final ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType serviceInstance =
            new ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType();

    @Atomic
    public static List<ExecutionCourse> runReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType(
            String teacherID, String executionYearID, DegreeType degreeType) throws FenixServiceException, NotAuthorizedException {
        return serviceInstance.run(teacherID, executionYearID, degreeType);
    }

}