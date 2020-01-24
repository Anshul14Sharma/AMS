package dao;

import io.ebean.Finder;
import models.Attendance;
import models.Employee;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class EmployeeFinder implements iEmployeeFinder {
    private Finder<Long, Employee> empFind = new Finder<>(Employee.class);
    private Finder<Long, Attendance> attFind = new Finder<>(Attendance.class);

    public Employee byEmail(String email) {
        return empFind.query().where().like("email", email).setMaxRows(1).findOne();
    }

    public Attendance getLatestAttendance(String id) {
        return attFind.query().where().like("employeeid", id).orderBy("checkindt desc").setMaxRows(1).findOne();
    }

    public List<Attendance> listByEmployeeId(String id){
        return attFind.query().where().like("employeeid", id).findList();
    }
    public Employee byEmailAndPwd(String email, String pwd) {
        return empFind.query().where().like("email", email).and().like("password", pwd).setMaxRows(1).findOne();
    }

}
