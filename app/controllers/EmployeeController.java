package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.EmployeeFinder;
import dao.iEmployeeFinder;
import models.Attendance;
import models.Employee;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeController extends Controller {
    @Inject
    private iEmployeeFinder finder;

    public Result checkInCheckOut(Http.Request request) {
        return request
                .session()
                .getOptional("connected")
                .map(user -> ok(views.html.index.render()))
                .orElseGet(() -> ok(views.html.login.render()));
    }

    public Result check() {
        return ok(views.html.check.render());
    }

    public Result saveAttendance() {
        JsonNode form = request().body().asJson();
        String email = form.get("email").asText();
        Employee checkedInEmp = finder.byEmail(email);
        if (checkedInEmp != null) {
            Logger.info("attendance::{}", checkedInEmp);
            if (checkedInEmp.getAttendance() != null) {
                if (checkedInEmp.getAttendance().getCheckOutDT() != null) {
                    Logger.info("attendance::{}", checkedInEmp.getAttendance().getCheckInDT());
                    if (checkedInEmp.getAttendance().getCheckOutDT().after(checkedInEmp.getAttendance().getCheckInDT())) {
                        doCheckIn(checkedInEmp);
                        return ok("Checked In");
                    } else {
                        doCheckOut(checkedInEmp);
                        return ok("Checked Out");
                    }
                } else {
                    doCheckOut(checkedInEmp);
                    return ok("Checked Out");
                }
            } else {
                Attendance attendance = new Attendance(new Date(System.currentTimeMillis()), null);
                attendance.setEmployee(checkedInEmp);
                attendance.save();
                return ok("Checked In");
            }
        } else {
            return ok("404");
        }
    }

    private void doCheckOut(Employee checkedInEmp) {
        Attendance oldAttendance = checkedInEmp.getAttendance();
        oldAttendance.setCheckOutDT(new Date(System.currentTimeMillis()));
        oldAttendance.update();
    }

    private void doCheckIn(Employee checkedInEmp) {
        Attendance oldAttendance = checkedInEmp.getAttendance();
        oldAttendance.setCheckInDT(new Date(System.currentTimeMillis()));
        oldAttendance.update();
    }


    public Result getAttendance(String email) {
        Logger.info("email::{}", email);
        String checkInDT = null;
        String checkOutDT = null;
        Employee oldEmployee = finder.byEmail(email);
        if (oldEmployee.getAttendance() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss a");
            checkInDT = dateFormat.format(oldEmployee.getAttendance().getCheckInDT());
            if (oldEmployee.getAttendance().getCheckOutDT() != null) {
                checkOutDT = dateFormat.format(oldEmployee.getAttendance().getCheckOutDT());
            } else {
                checkOutDT = "";
            }

        } else {
            return notFound(views.html.notfound.render(email));
        }
        return ok(views.html.attendance.render(oldEmployee.getFirstName(), oldEmployee.getLastName(), checkInDT, checkOutDT));
    }
}
