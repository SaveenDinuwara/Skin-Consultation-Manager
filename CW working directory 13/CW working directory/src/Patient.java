import java.time.LocalDate;

public class Patient extends Person{

    private String patientId;

    public Patient(String firstname, String surname, LocalDate dOB, String mobileNo, String patientId){
        super(firstname, surname, dOB, mobileNo);

        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}

