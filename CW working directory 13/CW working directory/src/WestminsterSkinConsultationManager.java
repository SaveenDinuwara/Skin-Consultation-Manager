import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;


public class WestminsterSkinConsultationManager implements SkinConsultationManager {


    public static Scanner scanner = new Scanner(System.in);
    //Doctor doctorList[] = new Doctor[10];
    static ArrayList<Doctor> doctorList = new ArrayList<Doctor>();
    public static GUI gui;


    public static void main(String[] args) {

        WestminsterSkinConsultationManager manager = new WestminsterSkinConsultationManager();

        int input = 0;
        loop:
        while (input != 7) {
            try {
                System.out.print("""

                        ---------------------------------------
                        \tSkin Consultation Manger\s
                        ---------------------------------------
                        \t-Enter 1 to Add a new doctor
                        \t-Enter 2 to Delete a doctor
                        \t-Enter 3 to Print the list of the doctors
                        \t-Enter 4 to Save in a file
                        \t-Enter 5 to Read the file
                        \t-Enter 6 to Open GUI
                        \t-Enter 7 to Exit
                        >>>>>
                        """);

                //input = scanner.nextLine();
                //int option = Integer.parseInt(input);
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        manager.AddDoctor();
                        break;
                    case 2:
                        manager.DeleteDoctor();
                        break;
                    case 3:
                        manager.PrintListOfDoctors();
                        break;
                    case 4:
                        manager.SaveFile();
                        break;
                    case 5:
                        manager.ReadFile(true);
                        break;
                    case 6:
                        manager.OpenGUI();
                        break;
                    case 7:
                        break loop;

                    default:
                        System.out.println("Invalid Input! Please enter a valid input...");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Invalid Input! Renter...");
            }


        }
    }

    @Override
    public void AddDoctor() {
        ReadFile(true);
        String firstname = null;
        String surname = null;
        LocalDate DOB = null;
        String dob = null;
        String mobile = null;
        String medicalLicense = null;
        String specialisation = null;
        boolean result = false;

        if (doctorList.size() == 10) {
            System.out.println("Doctor List is full!");
        } else {
            while (!result) {
                System.out.print("Enter First name: ");
                firstname = scanner.nextLine().trim();
                result = nameValidation(firstname);
                if (!result || firstname.isEmpty()) {
                    System.out.println("Invalid Input! Renter...");
                    result = false;
                }
            }
            result = false;

            while (!result) {
                System.out.print("Enter Surname: ");
                surname = scanner.nextLine().trim();
                result = nameValidation(firstname);
                if (!result || surname.isEmpty()) {
                    System.out.println("Invalid Input! Renter...");
                    result = false;
                }
            }
            result = false;

            while (!result) {
                System.out.print("Enter Date of Birth: ");
                dob = scanner.nextLine().trim();
                result = dobValidation(dob);
                if (!result || dob.isEmpty()) {
                    System.out.println("Invalid Input! Renter...");
                    result = false;
                }else{
                    DOB = LocalDate.parse(dob);
                }
            }
            result = false;

            while (!result) {
                System.out.print("Enter Mobile number: ");
                mobile = scanner.nextLine().trim();
                result = mobileValidation(mobile);
                if (!result) {
                    System.out.println("Invalid Input! Renter...");
                }
            }
            result = false;

            while (!result) {
                System.out.print("Enter Medical license number: ");
                medicalLicense = scanner.nextLine().trim();
                result = licenseValidation(medicalLicense);
                if (!result || medicalLicense.isEmpty()) {
                    System.out.println("Invalid Input! Renter...");
                    result = false;
                }
            }
            result = false;

            while (!result) {
                System.out.print("Enter Specialisation:");
                specialisation = scanner.nextLine().trim();
                result = nameValidation(specialisation);
                if (!result || specialisation.isEmpty()) {
                    System.out.println("Invalid Input! Renter...");
                    result=false;
                }
            }
            result = false;


            Doctor doctor = new Doctor(firstname, surname, DOB, mobile, medicalLicense, specialisation);

            doctorList.add(doctor);

            SaveFile();
        }

    }

    @Override
    public void DeleteDoctor() {
        ReadFile(true);

        System.out.print("Enter Medical License Number : ");
        String medical = scanner.nextLine();
        System.out.println(medical);

        for (int i = 0; i < doctorList.size(); i++) {

            if (doctorList.get(i).getMedicalLicense().equals(medical)) {
                System.out.println("Deleted Dr. " + doctorList.get(i).getFirstname() + doctorList.get(i).getSurname() + " " +
                        doctorList.get(i).getDOB() +  " " + doctorList.get(i).getMobileNo() + " " + doctorList.get(i).getMedicalLicense()
                        + " " + doctorList.get(i).getSpecialisation());
                doctorList.remove(i);
            }
        }

        SaveFile();
        ReadFile(false);

    }

    @Override
    public void PrintListOfDoctors() {
        ReadFile(false);
        String str;
        String[] surnames = new String[doctorList.size()];

        for (int a = 0; a < doctorList.size(); a++) {
            surnames[a] = doctorList.get(a).getSurname();
        }

        for (int a = 0; a < surnames.length; a++) {
            for (int b = a + 1; b < surnames.length; b++) {
                if (surnames[a].compareTo(surnames[b]) > 0) {
                    str = surnames[a];
                    surnames[a] = surnames[b];
                    surnames[b] = str;
                }
            }
        }
        System.out.print("""
                        -------------------------------------------------------------------------------------------------------------------------------
                        \t\t\t\t\t\tList of available doctors\s
                        -------------------------------------------------------------------------------------------------------------------------------
                        """);
        for (int a = 0; a < surnames.length - 1 ; a++) {

            for(int i = 0; i < doctorList.size(); i++){
                if (doctorList.get(i).getSurname()==surnames[a]){
                    System.out.println("\t" +
                            "First name - " + doctorList.get(i).getFirstname() + "\t" +
                            "Surname - " + doctorList.get(i).getSurname() + "\t" +
                            "Date of Birth - " + doctorList.get(i).getDOB() + "\t" +
                            "Mobile No - " + doctorList.get(i).getMobileNo()+ "\t" +
                            "Medical License - " + doctorList.get(i).getMedicalLicense()+ "\t" +
                            "Specialisation - " + doctorList.get(i).getSpecialisation());
                }

            }
        }

        for(int i = 0; i < doctorList.size(); i++){
            if (doctorList.get(i).getSurname()==surnames[surnames.length - 1]){
                System.out.println("\t"+
                        "First name - " + doctorList.get(i).getFirstname() + "\t" +
                        "Surname - " + doctorList.get(i).getSurname() + "\t" +
                        "Date of Birth - " + doctorList.get(i).getDOB() + "\t" +
                        "Mobile No - " + doctorList.get(i).getMobileNo()+ "\t" +
                        "Medical License - " + doctorList.get(i).getMedicalLicense()+ "\t" +
                        "Specialisation - " + doctorList.get(i).getSpecialisation() +
                        "\n------------------------------------------------------------------------------------------------------------------");

            }
        }
    }

    @Override
    public void SaveFile() {

        try {
            PrintWriter doctorsFile = new PrintWriter("doctors.txt");

            for (Doctor doctor : doctorList) {

                doctorsFile.println(doctor.getFirstname() + " " + doctor.getSurname() + " " + doctor.getDOB() + " " + doctor.getMobileNo() + " " + doctor.getMedicalLicense() + " " + doctor.getSpecialisation());

            }
            doctorsFile.close();

            System.out.println("Saved to the file");

        } catch (Exception e) {
            System.out.println("File Write Error!");
        }

    }

    @Override
    public void ReadFile(boolean input) {
        try  {
            doctorList.clear();
            FileReader file = new FileReader("doctors.txt");
            BufferedReader reader = new BufferedReader(file);

            String line;
            while ((line = reader.readLine()) != null){

                String[] temp = line.split("\\s");
                String surname = temp[0];
                String firstname = temp[1];
                String dob = temp[2];
                LocalDate DOB = LocalDate.parse(dob);
                String mobile = temp[3];
                String license = temp[4];
                String specialisation = temp[5];

                Doctor doctor = new Doctor(surname, firstname, DOB, mobile, license, specialisation);

                doctorList.add(doctor);

                if (input) {
                    System.out.println(surname + " " + firstname + " " + DOB + " " + mobile + " " + license + " " + specialisation);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        @Override
        public void OpenGUI () {


            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        ReadFile(false);
                        gui = new GUI("Westminster Consultation Manager");
                        gui.setVisible(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }




        public boolean nameValidation (String name){

            boolean result = (name != null) && name.matches("^[a-zA-Z]*$");
                return result;
        }

        public boolean dobValidation(String date){
            boolean result = (date != null) && date.matches("\\d{4}-\\d{1,2}-\\d{1,2}");
                return result;
        }

        public boolean mobileValidation(String mobile){
            boolean result = ( mobile != null) && mobile.matches("^(?=(?:[07]{2})(?=[0-9]{8})).*");
                return result;
        }
// Assumed that only A to Z and 0 to 9 can be used.
        public boolean licenseValidation(String license){
            boolean result  = (license != null) && license.matches("^[A-Z0-9]*$");
                return result;
        }

        public static ArrayList<Doctor> getDoctorList() {
        return doctorList;
    }
    }

