package dao;

import io.ebean.Finder;
import models.Attendance;
import models.Employee;

import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

@Singleton
public class EmployeeFinder implements iEmployeeFinder {
    private static final Finder<Long, Employee> empFind = new Finder<>(Employee.class);
    private static final Finder<Long, Attendance> attFind = new Finder<>(Attendance.class);

    public Employee byEmail(String email) {
        return empFind.query().where().like("email", email).setMaxRows(1).findOne();
    }

    public Attendance byEmployeeId(String id){
        return attFind.query().where().like("employeeid", id).orderBy("checkindt desc limit 1").findOne();
    }

    public List<Attendance> listByEmployeeId(String id){
        return attFind.query().where().like("employeeid", id).findList();
    }
    public Employee byEmailAndPwd(String email, String pwd) {
        return empFind.query().where().like("email", email).and().like("password", pwd).setMaxRows(1).findOne();
    }
}
