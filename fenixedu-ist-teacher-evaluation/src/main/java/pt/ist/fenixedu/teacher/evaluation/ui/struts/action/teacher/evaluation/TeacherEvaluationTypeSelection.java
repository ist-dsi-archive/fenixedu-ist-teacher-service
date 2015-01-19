/**
 * Copyright © 2011 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Teacher Evaluation.
 *
 * FenixEdu Teacher Evaluation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Teacher Evaluation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Teacher Evaluation.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.teacher.evaluation.ui.struts.action.teacher.evaluation;

import java.io.Serializable;

import pt.ist.fenixedu.teacher.evaluation.domain.CurricularEvaluation;
import pt.ist.fenixedu.teacher.evaluation.domain.NoEvaluation;
import pt.ist.fenixedu.teacher.evaluation.domain.RadistEvaluation;
import pt.ist.fenixedu.teacher.evaluation.domain.TeacherEvaluation;
import pt.ist.fenixedu.teacher.evaluation.domain.TeacherEvaluationProcess;
import pt.ist.fenixedu.teacher.evaluation.domain.TeacherEvaluationType;
import pt.ist.fenixframework.Atomic;

public class TeacherEvaluationTypeSelection implements Serializable {
    private final TeacherEvaluationProcess process;

    private TeacherEvaluationType type;

    public TeacherEvaluationTypeSelection(TeacherEvaluationProcess process) {
        this.process = process;
        TeacherEvaluation current = process.getCurrentTeacherEvaluation();
        if (current != null) {
            this.type = current.getType();
        }
    }

    public TeacherEvaluationProcess getProcess() {
        return process;
    }

    public void setType(TeacherEvaluationType type) {
        this.type = type;
    }

    public TeacherEvaluationType getType() {
        return type;
    }

    @Atomic
    public void createEvaluation() {
        switch (type) {
        case NO_EVALUATION:
            new NoEvaluation(process);
            break;
        case RADIST:
            new RadistEvaluation(process);
            break;
        case CURRICULAR:
            new CurricularEvaluation(process);
            break;
        }
    }
}
