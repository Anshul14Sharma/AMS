package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "tblemployee")
public class Employee extends Model {
    public static final Finder<Long, Employee> find = new Finder<>(Employee.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attendanceId", referencedColumnName = "idattendance", nullable = false)
    private Attendance attendance;

    public Employee(String email, Attendance attendance) {
        this.email = email;
        this.attendance = attendance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }
}
