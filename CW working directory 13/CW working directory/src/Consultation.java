import java.time.LocalDate;
import java.time.LocalTime;

public class Consultation {

    private String doctorMedicalLicense;

    private String patientID;
    private LocalDate consultDate;
    private LocalTime consultStartTime;
    private LocalTime consultEndTime;
    private String consultSlot;
    private String consultNote;





    public Consultation(String consultID, String doctorLicense, String patientID, LocalDate consultDate, LocalTime consultStart, LocalTime consultEnd, String note) {

        this.consultSlot = consultID;
        this.doctorMedicalLicense = doctorLicense;
        this.patientID = patientID;
        this.consultDate = consultDate;
        this.consultStartTime = consultStart;
        this.consultEndTime = consultEnd;
        this.consultNote = note;
    }

    public String getDoctorMedicalLicense(){return doctorMedicalLicense;}

    public void setDoctorMedicalLicense(String license){this.doctorMedicalLicense = license;}

    public String getPatientID(){return patientID;}

    public void setPatientID(String id){this.patientID = id;}

    public LocalDate getConsultDate() {
        return consultDate;
    }

    public void setConsultDate(LocalDate consultDate) {
        this.consultDate = consultDate;
    }

    public LocalTime getConsultStartTime() {
        return consultStartTime;
    }

    public void setConsultTime(LocalTime consultStartTime) {
        this.consultStartTime = consultStartTime;
    }

    public LocalTime getConsultEndTime(){return consultEndTime;}
    public void setConsultEndTime(LocalTime consultEndTime){this.consultEndTime = consultEndTime;}

    public String getConsultSlot() {
        return consultSlot;
    }

    public void setConsultSlot(String consultSlot) {
        this.consultSlot = consultSlot;
    }

    public String getConsultNote() {
        return consultNote;
    }

    public void setConsultNote(String consultNote) {
        this.consultNote = consultNote;
    }

}