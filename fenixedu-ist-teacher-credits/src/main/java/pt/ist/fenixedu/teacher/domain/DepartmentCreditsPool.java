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

import java.math.BigDecimal;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

public class DepartmentCreditsPool extends DepartmentCreditsPool_Base {

    public DepartmentCreditsPool() {
        super();
    }

    public DepartmentCreditsPool(Department department, ExecutionYear executionYear, BigDecimal originalCreditsPool,
            BigDecimal creditsPool) {
        super();
        if (department == null || executionYear == null) {
            throw new DomainException("arguments can't be null");
        }
        setDepartment(department);
        setExecutionYear(executionYear);
        if (originalCreditsPool == null) {
            originalCreditsPool = BigDecimal.ZERO;
        }
        setOriginalCreditsPool(originalCreditsPool);
        if (creditsPool == null) {
            creditsPool = BigDecimal.ZERO;
        }
        setCreditsPool(creditsPool);
        setRootDomainObject(Bennu.getInstance());
    }

    public static DepartmentCreditsPool getDepartmentCreditsPool(Department department, ExecutionYear executionYear) {
        for (DepartmentCreditsPool departmentCreditsPool : executionYear.getDepartmentCreditsPoolsSet()) {
            if (departmentCreditsPool.getDepartment().equals(department)) {
                return departmentCreditsPool;
            }
        }
        return null;
    }

}
