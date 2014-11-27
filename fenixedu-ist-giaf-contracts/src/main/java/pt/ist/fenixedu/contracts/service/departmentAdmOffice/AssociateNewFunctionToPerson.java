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
/*
 * Created on Oct 27, 2005
 *	by mrsp
 */
package pt.ist.fenixedu.contracts.service.departmentAdmOffice;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AssociateNewFunctionToPerson {

    protected void run(String functionID, String personID, Double credits, YearMonthDay begin, YearMonthDay end)
            throws FenixServiceException, DomainException {

        Person person = (Person) FenixFramework.getDomainObject(personID);
        if (person == null) {
            throw new FenixServiceException("error.noPerson");
        }

        Function function = (Function) FenixFramework.getDomainObject(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }
        new PersonFunction(function.getUnit(), person, function, begin, end, credits);
    }

    // Service Invokers migrated from Berserk

    private static final AssociateNewFunctionToPerson serviceInstance = new AssociateNewFunctionToPerson();

    @Atomic
    public static void runAssociateNewFunctionToPerson(String functionID, String personID, Double credits, YearMonthDay begin,
            YearMonthDay end) throws FenixServiceException, DomainException, NotAuthorizedException {
        serviceInstance.run(functionID, personID, credits, begin, end);
    }

}