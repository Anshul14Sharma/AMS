package models;

import io.ebean.Model;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tblattendance")
public class Attendance extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idattendance")
    private Long idAttendance;
    @Column(name = "checkInDT")
    @Formats.DateTime(pattern = "dd/MM/YYYY hh:mm:ss a")
    private Date checkInDT;
    @Formats.DateTime(pattern = "dd/MM/YYYY hh:mm:ss a")
    @Column(name = "checkOutDT")
    private Date checkOutDT;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employeeId", referencedColumnName = "id", nullable = false)
    private Employee employee;

    public Attendance(Date checkInDT, Date checkOutDT) {
        this.checkInDT = checkInDT;
        this.checkOutDT = checkOutDT;
    }

    public Date getCheckInDT() {
        return checkInDT;
    }

    public void setCheckInDT(Date checkInDT) {
        this.checkInDT = checkInDT;
    }

    public Date getCheckOutDT() {
        return checkOutDT;
    }

    public void setCheckOutDT(Date checkOutDT) {
        this.checkOutDT = checkOutDT;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
