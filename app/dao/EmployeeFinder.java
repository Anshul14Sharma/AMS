package dao;

import io.ebean.Finder;
import models.Employee;

import javax.inject.Singleton;

@Singleton
public class EmployeeFinder implements iEmployeeFinder {
    public static final Finder<Long, Employee> find = new Finder<>(Employee.class);

    public Employee byEmail(String email) {
        return find.query().where().like("email", email).setMaxRows(1).findOne();
    }

    public Employee byEmailAndPwd(String email, String pwd) {
        return find.query().where().like("email", email).and().like("password", pwd).setMaxRows(1).findOne();
    }
}
