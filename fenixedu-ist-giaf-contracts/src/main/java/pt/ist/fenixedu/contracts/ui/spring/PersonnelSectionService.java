package pt.ist.fenixedu.contracts.ui.spring;

import java.util.Collection;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.springframework.stereotype.Service;

import pt.ist.fenixedu.contracts.domain.Employee;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@Service
public class PersonnelSectionService {

    public Collection<Person> searchPersons(final PersonnelSectionSearchBean search) {
        return search.search();
    }

    @Atomic(mode = TxMode.WRITE)
    public void createEmployee(Person person) {
        try {
            new Employee(person, Employee.getNextEmployeeNumber());
        } catch (DomainException e) {
            throw new RuntimeException(e);
        }
    }

}