package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.action.Secured;
import dao.iEmployeeFinder;
import models.Attendance;
import models.Employee;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Security.Authenticated(Secured.class)
public class EmployeeController extends Controller {
    @Inject
    private iEmployeeFinder finder;

    public Result checkInCheckOut() {
        return ok(views.html.index.render());
    }

    public Result check() {
        return ok(views.html.check.render());

    }

    public Result saveAttendance() {
        JsonNode form = request().body().asJson();
        if (!validateForm(form)) {
            return badRequest();
        }
        String email = form.get("email").asText();
        Employee checkedInEmp = finder.byEmail(email);
        if (checkedInEmp != null) {
            Attendance oldAttendance = finder.byEmployeeId(String.valueOf(checkedInEmp.getId()));
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

    public Result getAttendance(String email) {
        Logger.info("email::{}", email);
        Employee oldEmployee = finder.byEmail(email);
        if (oldEmployee != null) {
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
        } else {
            return notFound(views.html.notfound.render(email));
        }

    }
}
