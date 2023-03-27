import java.time.LocalDate;

public class Doctor extends Person{

    private final String medicalLicense;
    private final String specialisation;
    public Doctor(String firstname, String surname, LocalDate dob, String mobile, String medicalLicense, String specialisation) {
        super(firstname, surname, dob, mobile);

        this.medicalLicense = medicalLicense;
        this.specialisation = specialisation;
    }


    public String getMedicalLicense() {
        return medicalLicense;
    }

    public String getSpecialisation() {
        return specialisation;
    }
}
