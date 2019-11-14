package dao;

import com.google.inject.ImplementedBy;
import models.Employee;

@ImplementedBy(EmployeeFinder.class)
public interface iEmployeeFinder {

    Employee byEmail(String email);

    Employee byEmailAndPwd(String email, String pwd);
}
