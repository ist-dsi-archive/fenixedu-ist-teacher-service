/**
 * Nov 22, 2005
 */
package pt.ist.fenixedu.teacher.dto.teacher.professorship;

import java.util.Date;

import org.fenixedu.academic.dto.InfoObject;
import org.fenixedu.academic.util.DiaSemana;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class SupportLessonDTO extends InfoObject {

    private String professorshipID;

    private DiaSemana weekDay;

    private Date startTime;

    private Date endTime;

    private String place;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getProfessorshipID() {
        return professorshipID;
    }

    public void setProfessorshipID(String professorshipID) {
        this.professorshipID = professorshipID;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public DiaSemana getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(DiaSemana weekDay) {
        this.weekDay = weekDay;
    }
}
