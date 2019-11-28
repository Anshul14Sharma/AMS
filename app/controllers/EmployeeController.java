package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.action.EmpCache;
import controllers.action.Secured;
import dao.iEmployeeFinder;
import models.Attendance;
import models.Employee;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Security.Authenticated(Secured.class)
public class EmployeeController extends Controller {
    @Inject
    private iEmployeeFinder finder;
    @Inject
    private EmpCache empCache;

    public Result checkInCheckOut() {
        return ok(views.html.index.render());
    }

    public Result check() {
        return ok(views.html.check.render());
    }

    public Result saveAttendance() {
        Optional<String> email = empCache.getSyncCacheApi();
        if (email.isPresent()) {
            Employee checkedInEmp = finder.byEmail(email.get());
            Logger.info("Emp::{}", checkedInEmp);
            Attendance oldAttendance = finder.getLatestAttendance(String.valueOf(checkedInEmp.getId()));
            Calendar calendar = Calendar.getInstance();
            if (oldAttendance != null) {
                if (oldAttendance.getCheckOutDT() == null) {
                    if (oldAttendance.getCheckInDT().getDate() == calendar.get(Calendar.DAY_OF_MONTH)) {
                        oldAttendance.setCheckOutDT(calendar.getTime());
                        oldAttendance.update();
                        return ok("Checked Out");
                    } else {
                        Attendance addAtt = new Attendance(calendar.getTime(), null);
                        addAtt.setEmployee(checkedInEmp);
                        addAtt.save();
                        return ok("Checked In");
                    }
                } else {
                    if (oldAttendance.getCheckInDT().getDate() < calendar.get(Calendar.DAY_OF_MONTH)) {
                        Attendance addAtt = new Attendance(calendar.getTime(), null);
                        addAtt.setEmployee(checkedInEmp);
                        addAtt.save();
                        return ok("Checked In");
                    } else {
                        oldAttendance.setCheckOutDT(calendar.getTime());
                        oldAttendance.update();
                        return ok("Checked Out");
                    }
                }
            } else {
                Attendance attendance = new Attendance(calendar.getTime(), null);
                attendance.setEmployee(checkedInEmp);
                attendance.save();
                return ok("Checked In");
            }
        } else {
            return ok("404");
        }
    }

    private boolean validateForm(JsonNode form) {
        if (form.get("email").asText() == null && form.get("email").asText().isEmpty()) {
            return false;
        }
        return true;
    }

    public Result getAttendance() {
        try {
            Optional<String> email = empCache.getSyncCacheApi();
            if (email.isPresent()) {
                Employee oldEmployee = finder.byEmail(email.get());
                if (oldEmployee != null) {
                    List<Attendance> attendances = finder.listByEmployeeId(String.valueOf(oldEmployee.getId()));
                    if (attendances != null) {
                        return ok(views.html.attendance.render(oldEmployee, attendances));
                    } else {
                        return notFound(views.html.notfound.render(email.get()));
                    }
                } else {
                    return notFound(views.html.notfound.render(email.get()));
                }
            } else {
                return notFound(views.html.notfoundpage.render());
            }
        } catch (Exception e) {
            return internalServerError();
        }
    }

    public Result getAdminAttendance(String email) {
        try {
            Employee oldEmployee = finder.byEmail(email);
            Logger.info("empData::{}", oldEmployee);
            if (oldEmployee != null) {
                List<Attendance> attendances = finder.listByEmployeeId(String.valueOf(oldEmployee.getId()));
                if (attendances != null) {
                    return ok(views.html.attendance.render(oldEmployee, attendances));
                } else {
                    return notFound(views.html.notfound.render(email));
                }
            } else {
                return notFound(views.html.notfound.render(email));
            }
        } catch (Exception e) {
            return internalServerError();
        }
    }
}
