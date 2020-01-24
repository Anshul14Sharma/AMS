package models;

import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "tblemployee")
public class Employee extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role", referencedColumnName = "roleid")
    private Role role;
//    @OneToMany(mappedBy = "employee")
//    private List<Attendance> attendance;

//    public Employee(String email, List<Attendance> attendance) {
//        this.email = email;
//        this.attendance = attendance;
//    }

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //    public List<Attendance> getAttendance() {
//        return attendance;
//    }
//
//    public void setAttendance(List<Attendance> attendance) {
//        this.attendance = attendance;
//    }
}
