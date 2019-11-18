package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.iEmployeeFinder;
import models.Attendance;
import models.Employee;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Result check(Http.Request request) {
        return request
                .session()
                .getOptional("connected")
                .map(user -> ok(views.html.check.render()))
                .orElseGet(() -> ok(views.html.login.render()));
    }

    public Result saveAttendance() {
        JsonNode form = request().body().asJson();
        String email = form.get("email").asText();
        Employee checkedInEmp = finder.byEmail(email);
        if (checkedInEmp != null) {
            Attendance oldAttendance = finder.byEmployeeId(String.valueOf(checkedInEmp.getId()));
            if (oldAttendance != null) {
                if (oldAttendance.getCheckOutDT() != null) {
                    if (oldAttendance.getCheckInDT().getDate() < (new Date(System.currentTimeMillis()).getDate())) {
                        Attendance addAtt = new Attendance(new Date(System.currentTimeMillis()), null);
                        addAtt.setEmployee(checkedInEmp);
                        addAtt.save();
                        return ok("Checked In");
                    } else {
                        oldAttendance.setCheckOutDT(new Date(System.currentTimeMillis()));
                        oldAttendance.update();
                        return ok("Checked Out");
                    }
                } else {
                    oldAttendance.setCheckOutDT(new Date(System.currentTimeMillis()));
                    oldAttendance.update();
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

    public Result getAttendance(String email) {
        Logger.info("email::{}", email);
        Employee oldEmployee = finder.byEmail(email);
        List<String> checkInDT = new ArrayList<>();
        List<String> checkOutDT = new ArrayList<>();
        List<Attendance> attendances = finder.listByEmployeeId(String.valueOf(oldEmployee.getId()));
        if (attendances != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss a");
            for (Attendance att : attendances) {
                checkInDT.add(dateFormat.format(att.getCheckInDT()));
                if (att.getCheckOutDT() != null) {
                    checkOutDT.add(dateFormat.format(att.getCheckOutDT()));
                } else {
                    checkOutDT.add("");
                }
            }
            Logger.info("attendance::{}, ::{}", checkInDT, checkOutDT);
        } else {
            return notFound(views.html.notfound.render(email));
        }
        return ok(views.html.attendance.render(oldEmployee.getFirstName(), oldEmployee.getLastName(), checkInDT, checkOutDT));
    }
}
