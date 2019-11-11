package dao;

import io.ebean.Finder;
import models.Employee;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EmployeeFinder extends Finder<Long, Employee> {

    public EmployeeFinder(Class<Employee> type) {
        super(type);
    }

    public Employee byEmail(String email) {
        return query().where().like("email", email).setMaxRows(1).findOne();
    }
}
