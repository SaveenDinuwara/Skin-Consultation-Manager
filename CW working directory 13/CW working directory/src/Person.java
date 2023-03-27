import java.time.LocalDate;

public class Person {
    private  String firstname;
    private String surname;
    private LocalDate DOB;
    private String mobileNo;

    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Person(String firstname, String surname, LocalDate DOB, String mobileNo) {
        this.firstname = firstname;
        this.surname = surname;
        this.DOB = DOB;
        this.mobileNo = mobileNo;
    }
}
