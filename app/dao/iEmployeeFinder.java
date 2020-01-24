package dao;

import com.google.inject.ImplementedBy;
import models.Attendance;
import models.Employee;

import java.util.List;

@ImplementedBy(EmployeeFinder.class)
public interface iEmployeeFinder {

    Employee byEmail(String email);

    Employee byEmailAndPwd(String email, String pwd);

    Attendance getLatestAttendance(String id);

    List<Attendance> listByEmployeeId(String id);
}
