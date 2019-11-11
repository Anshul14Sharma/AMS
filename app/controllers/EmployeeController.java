package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.iEmployeeFinder;
import models.Attendance;
import models.Employee;
import dao.EmployeeFinder;
import request.EmployeeForm;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import router.Routes;
import views.html.hello;
import views.html.index;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeController extends Controller {

    private final FormFactory formFactory;

    @Inject
    private EmployeeController(final FormFactory formFactory) {
        this.formFactory = formFactory;
    }


    public Result pageRenderer() {
        return ok(views.html.index.render());
    }

    public Result check() {
        return ok(views.html.check.render());
    }

    public Result saveAttendance() {
//        Form<EmployeeForm> employeeForm = formFactory.form(EmployeeForm.class).bindFromRequest();
        JsonNode form = request().body().asJson();
        Logger.info("email::{}", form);
        String email = form.get("email").asText();
        Logger.info("email::{}", email);
        EmployeeFinder finder = new EmployeeFinder(Employee.class);
//        if (employeeForm.hasErrors()) {
//            return badRequest(index.render());
//        } else {
            Employee checkedInEmp = finder.byEmail(email);
            if (checkedInEmp != null) {
                if (checkedInEmp.getAttendance().getCheckOutDT() != null) {
                    if (checkedInEmp.getAttendance().getCheckOutDT().after(checkedInEmp.getAttendance().getCheckInDT())) {
                        Attendance oldAttendance = checkedInEmp.getAttendance();
                        oldAttendance.setCheckInDT(new Date(System.currentTimeMillis()));
                        checkedInEmp.update();
                        return created(hello.render("Checked In"));
                    } else {
                        Attendance oldAttendance = checkedInEmp.getAttendance();
                        oldAttendance.setCheckOutDT(new Date(System.currentTimeMillis()));
                        checkedInEmp.update();
                        return created(hello.render("Checked Out"));
                    }

                } else {
                    Logger.info("Employee::{}", checkedInEmp.getAttendance().getCheckInDT());
                    Attendance oldAttendance = checkedInEmp.getAttendance();
                    oldAttendance.setCheckOutDT(new Date(System.currentTimeMillis()));
                    checkedInEmp.update();
                    return created(hello.render("Checked Out"));
                }

            } else {
                Attendance attendance = new Attendance(new Date(System.currentTimeMillis()), null);
                Employee employee = new Employee(email, attendance);
                employee.save();
                return created(hello.render("Checked In"));
            }
//        }
    }

    public Result getAttendance(String email) {
        EmployeeFinder finder = new EmployeeFinder(Employee.class);
        Logger.info("email::{}", email);
        String checkInDT = null;
        String checkOutDT = null;
        Employee oldEmployee = finder.byEmail(email);
        if (oldEmployee != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss a");
            checkInDT = dateFormat.format(oldEmployee.getAttendance().getCheckInDT());
            if (oldEmployee.getAttendance().getCheckOutDT() != null) {
                checkOutDT = dateFormat.format(oldEmployee.getAttendance().getCheckOutDT());
            } else {
                checkOutDT = "";
            }

        } else {
            return notFound(views.html.notfound.render());
        }
        return ok(views.html.attendance.render(email, checkInDT, checkOutDT));
    }
}
