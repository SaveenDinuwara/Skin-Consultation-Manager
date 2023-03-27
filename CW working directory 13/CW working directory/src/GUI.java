import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;


public class GUI extends JFrame{
    private String filePath;
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    //to use when checking whether check availability button clicked
    private boolean checked = false;

    static ArrayList<Patient> patientList = new ArrayList<>();
    static ArrayList<Consultation> consultationList = new ArrayList<>();

    static ArrayList<String> paymentList = new ArrayList<>();

    public GUI(String title) {
            super(title);

            //Set size of the main UI
            setSize(900, 500);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setLocationRelativeTo(null);
            setLayout(null);

            //Background image on main UI
            setContentPane(new JLabel(new ImageIcon("background.png")));

            JLabel titleMain = new JLabel("Westminster Consultation Manager");
            this.add(titleMain);

        //Add New Consultation Button on Main UI
        JButton addNewBtn = new JButton(new AbstractAction("Add New Consultation") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConsultationGUI();
                ReadConsultationFile();
                ReadPersonFile();

            }
        });

        //Add Show Available Doctors Button on Main UI
        JButton showDoctors = new JButton(new AbstractAction("Show Available Doctors") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowAvailableDoctorsGUI();

            }
        });

        //Consultation History Button on Main UI
        JButton showHistory = new JButton(new AbstractAction("Consultation History") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            ShowHistoryGUI();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });



        //Setting Font color, background color and background transparency for showdoctors Button
        showDoctors.setForeground(Color.WHITE);
        showDoctors.setBackground(Color.WHITE);
        showDoctors.setOpaque(false);

        //Setting Font color, background color and background transparency for addnewconsultation  Button
        addNewBtn.setForeground(Color.WHITE);
        addNewBtn.setBackground(Color.WHITE);
        addNewBtn.setOpaque(false);

        //Setting Font color, background color and background transparency for showHistory  Button
        showHistory.setForeground(Color.WHITE);
        showHistory.setBackground(Color.WHITE);
        showHistory.setOpaque(false);

        //Define borders on buttons
        Border line = new LineBorder(Color.WHITE);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);

        showDoctors.setBorder(compound);
        addNewBtn.setBorder(compound);
        showHistory.setBorder(compound);

        addNewBtn.setBounds(650, 40, 200, 50);
        showDoctors.setBounds(650, 140, 200, 50);
        showHistory.setBounds(650, 240, 200, 50);
        this.add(addNewBtn);
        this.add(showDoctors);
        this.add(showHistory);







    }

    public void ConsultationGUI(){

        final String[] filetype = {null};

        JDialog addNewConsultDialog = new JDialog((Frame) null,"Add New Consultation");
        addNewConsultDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        addNewConsultDialog.setSize(1000, 600);
        //addNewConsultDialog.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addNewConsultDialog.setLayout(null);
        addNewConsultDialog.setLocationRelativeTo(null);
        addNewConsultDialog.setBackground(Color.WHITE);

        JLabel patientFirstNamelbl = new JLabel("First Name*");
        patientFirstNamelbl.setBounds(30, 40, 500, 10);
        addNewConsultDialog.add(patientFirstNamelbl);

        JTextField patientFirstName = new JFormattedTextField("Enter Your First Name");
        patientFirstName.setForeground(Color.GRAY);
        patientFirstName.setBounds(30, 60, 420, 30);
        patientFirstName.setToolTipText("Enter Your First Name");
        addNewConsultDialog.add(patientFirstName);

        patientFirstName.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                patientFirstName.setText("");
                patientFirstName.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (patientFirstName.getText() == null || patientFirstName.getText()=="") {
                    patientFirstName.setForeground(Color.GRAY);
                    patientFirstName.setText("Enter Your First Name");
                }
            }
        });

        JLabel patientSurnamelbl = new JLabel("Last Name*");
        patientSurnamelbl.setBounds(30, 100, 200, 10);
        addNewConsultDialog.add(patientSurnamelbl);

        JTextField patientLastName = new JFormattedTextField("Enter Your Last Name");
        patientLastName.setBounds(30, 120, 420, 30);
        patientLastName.setForeground(Color.GRAY);
        patientLastName.setToolTipText("Enter Your Surname");
        addNewConsultDialog.add(patientLastName);

        patientLastName.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                patientLastName.setText("");
                patientLastName.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (patientLastName.getText() == "") {
                    patientLastName.setForeground(Color.GRAY);
                    patientLastName.setText("Enter Your Surname");
                }
            }
        });

        JLabel patientDOBlbl = new JLabel("Date of Birth*");
        patientDOBlbl.setBounds(30, 160, 200, 10);
        addNewConsultDialog.add(patientDOBlbl);

        JTextField patientDOB = new JFormattedTextField("Enter Your Date of Birth (YYYY-MM-DD)");
        patientDOB.setBounds(30, 180, 420, 30);
        patientDOB.setForeground(Color.GRAY);
        addNewConsultDialog.add(patientDOB);
        patientDOB.setToolTipText("Format YYYY-MM-DD");

        patientDOB.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                patientDOB.setText("");
                patientDOB.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (patientDOB.getText() == "") {
                    patientDOB.setForeground(Color.GRAY);
                    patientDOB.setText("Enter Your Date of Birth (YYYY-MM-DD)");
                }
            }
        });

        JLabel patientMobilelbl = new JLabel("Mobile Number*");
        patientMobilelbl.setBounds(30, 220, 200, 10);
        addNewConsultDialog.add(patientMobilelbl);

        JTextField patientMobile= new JFormattedTextField("Enter Your Mobile Number");
        patientMobile.setBounds(30, 240, 420, 30);
        patientMobile.setForeground(Color.GRAY);
        addNewConsultDialog.add(patientMobile);
        patientMobile.setToolTipText("Enter Your Mobile Number");

        patientMobile.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                patientMobile.setText("");
                patientMobile.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (patientMobile.getText() == "") {
                    patientMobile.setForeground(Color.GRAY);
                    patientMobile.setText("Enter Your Mobile Number");
                }
            }
        });



        JLabel patientIDlbl = new JLabel("NIC Number*");
        patientIDlbl.setBounds(30, 280, 200, 10);
        addNewConsultDialog.add(patientIDlbl);

        JTextField patientID= new JFormattedTextField("Enter Your NIC Number");
        patientID.setBounds(30, 300, 420, 30);
        patientID.setForeground(Color.GRAY);
        patientID.setToolTipText("Enter Your NIC Number");
        addNewConsultDialog.add(patientID);

        patientID.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                patientID.setText("");
                patientID.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (patientID.getText() == "") {
                    patientID.setForeground(Color.GRAY);
                    patientID.setText("Enter Your NIC Number");
                }
            }
        });

        //Notes Label
        JLabel patientNoteslbl = new JLabel("Notes");
        patientNoteslbl.setBounds(30, 340, 200, 10);
        addNewConsultDialog.add(patientNoteslbl);

        //Notes TextArea
        JTextArea patientNotes= new JTextArea();
        patientNotes.setBounds(30, 360, 420, 100);
        patientNotes.setForeground(Color.GRAY);
        patientNotes.setToolTipText("Enter Your Notes for Doctor's Reference");
        addNewConsultDialog.add(patientNotes);

        //Upload Image Label
        JLabel patientImagelbl = new JLabel("Or upload an image");
        patientImagelbl.setBounds(30, 480, 150, 20);
        addNewConsultDialog.add(patientImagelbl);

        //Upload Image Button
        JButton UploadButton = new JButton(new AbstractAction("Upload") {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser filechooser = new JFileChooser();
                filechooser.setDialogTitle("Choose Your File");

                //Filtering input to support only .jpg files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Files", "jpg", "image");
                filechooser.setFileFilter(filter);

                // File selection
                int returnval = filechooser.showOpenDialog(addNewConsultDialog);
                if (returnval == JFileChooser.APPROVE_OPTION)
                {
                    File file = filechooser.getSelectedFile();
                    String filePath2 = file.getAbsolutePath();
                    String filename = file.getName();

                    String[] temp = filename.split("\\.");

                    filetype[0] = temp[1];

                    try {
                        //Key for Encryption
                        int key = 12345;

                        FileInputStream fileinputstream= new FileInputStream(filePath2);

                        byte data[] = new byte[fileinputstream.available()];

                        // Read the array
                        fileinputstream.read(data);

                        int i = 0;

                        for (byte b : data) {
                            data[i] = (byte)(b ^ key);
                            i++;
                        }

                        FileOutputStream fileoutputstream = new FileOutputStream("temp"+"."+ filetype[0]);

                        fileoutputstream.write(data);

                        //Closing file
                        fileinputstream.close();
                        fileoutputstream.close();

                    } catch(IOException f) {
                        f.printStackTrace();
                    }
                }

            }
        });
        UploadButton.setBounds(350, 480, 100, 30);
        UploadButton.setForeground(Color.BLACK);
        UploadButton.setBackground(Color.WHITE);
        //UploadButton.setOpaque(false);

        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);

        //UploadButton.setBorder(compound);
        addNewConsultDialog.add(UploadButton);

        //Available Doctors Combo box (drop down list)
        JLabel doctorLst = new JLabel("Available Doctors");
        doctorLst.setBounds(500, 40, 500, 10);
        addNewConsultDialog.add(doctorLst);

        String[] doctorslstCombo = DoctorsListCombo();
        JComboBox doctorsCombolst = new JComboBox(doctorslstCombo);

        doctorsCombolst.setBounds(500, 60,420,30);
        addNewConsultDialog.add(doctorsCombolst);

        //Consultation Date
        JLabel consultDatelbl = new JLabel("Consultation Date");
        consultDatelbl.setBounds(500, 100, 200, 10);
        addNewConsultDialog.add(consultDatelbl);

        JTextField consultDate = new JFormattedTextField("Enter Consultation Date (YYYY-MM-DD)");
        consultDate.setBounds(500, 120, 420, 30);
        consultDate.setForeground(Color.GRAY);
        consultDate.setToolTipText("Format (YYYY-MM-DD)");
        addNewConsultDialog.add(consultDate);

        consultDate.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                consultDate.setText("");
                consultDate.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (consultDate.getText() == "") {
                    consultDate.setForeground(Color.GRAY);
                    consultDate.setText("Enter Consultation Date (YYYY-MM-DD)");
                }
            }
        });

        JLabel consultStartTimelbl = new JLabel("Start Time");
        consultStartTimelbl.setBounds(500, 190, 100, 10);
        addNewConsultDialog.add(consultStartTimelbl);

        JTextField consultStartTime = new JFormattedTextField("HH:MM");
        consultStartTime.setBounds(580, 180, 100, 30);
        consultStartTime.setForeground(Color.GRAY);
        consultStartTime.setToolTipText("Enter Consultation Start Time (HH:MM)");
        addNewConsultDialog.add(consultStartTime);

        consultStartTime.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                consultStartTime.setText("");
                consultStartTime.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (consultStartTime.getText() == "") {
                    consultStartTime.setForeground(Color.GRAY);
                    consultStartTime.setText("HH:MM");
                }
            }
        });



        JLabel consultEndTimelbl = new JLabel("End Time");
        consultEndTimelbl.setBounds(750, 190, 100, 10);
        addNewConsultDialog.add(consultEndTimelbl);

        JTextField consultEndTime = new JFormattedTextField("HH:MM");
        consultEndTime.setBounds(820, 180, 100, 30);
        consultEndTime.setForeground(Color.GRAY);
        consultEndTime.setToolTipText("Enter Consultation End Time (HH:MM)");
        addNewConsultDialog.add(consultEndTime);

        consultEndTime.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                consultEndTime.setText("");
                consultEndTime.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (consultEndTime.getText() == "") {
                    consultEndTime.setForeground(Color.GRAY);
                    consultEndTime.setText("HH:MM");
                }
            }
        });
        JButton CheckAvailableBtn = new JButton();
        CheckAvailableBtn.setAction(new AbstractAction("Check Availability") {
            @Override
            public void actionPerformed(ActionEvent e) {

                checked = true;

                if(consultDate.getText().equals("Enter Consultation Date (YYYY-MM-DD)")  || consultStartTime.getText().equals("HH:MM")  || consultEndTime.getText().equals("HH:MM") || consultDate.getText().isEmpty() || consultStartTime.getText().isEmpty() || consultEndTime.getText().isEmpty()) {

                    FeedbackDialogGUI("Error!!", "Fill Out All Consultation Details");
                }
                else {
                    LocalDate date = LocalDate.parse(consultDate.getText());
                    LocalTime startTime = LocalTime.parse(consultStartTime.getText());
                    LocalTime endTime = LocalTime.parse(consultEndTime.getText());

                    CalculateHours(startTime, endTime);

                    boolean result = CheckConsultations(getDoctorLicenseFromComboBox(doctorsCombolst.getSelectedItem().toString()), date, startTime, endTime);

                    if (result == true) {
                        CheckAvailableBtn.setOpaque(true);
                        CheckAvailableBtn.setForeground(Color.WHITE);
                        CheckAvailableBtn.setBackground(Color.GREEN);
                        CheckAvailableBtn.setText("Available");

                    } else {

                        CheckAvailableBtn.setOpaque(true);
                        CheckAvailableBtn.setForeground(Color.WHITE);
                        CheckAvailableBtn.setBackground(Color.RED);
                        CheckAvailableBtn.setText("Random Doctor");

                        String randomDoctor = RandomDoctorSelect(doctorsCombolst.getSelectedItem().toString(), date, startTime, endTime);
                        doctorsCombolst.setSelectedItem(randomDoctor);

                        FeedbackDialogGUI("Random Doctor Selected", randomDoctor);

                    }
                }
            }
        });
        CheckAvailableBtn.setBounds(720, 240, 200, 30);
        CheckAvailableBtn.setForeground(Color.BLACK);
        CheckAvailableBtn.setBackground(Color.WHITE);
        CheckAvailableBtn.setOpaque(false);

        CheckAvailableBtn.setBorder(compound);

        addNewConsultDialog.add(CheckAvailableBtn);

        //Price Label
        JLabel pricelbl = new JLabel("Total Price : ");
        pricelbl.setBounds(500, 310, 200, 10);
        addNewConsultDialog.add(pricelbl);

        //Price Showing Label
        JLabel priceShowlbl = new JLabel();
        priceShowlbl.setBounds(600, 310, 200, 10);
        addNewConsultDialog.add(priceShowlbl);

        //Calculate Price Button
        JButton CalculatePriceBtn = new JButton();
        CalculatePriceBtn.setAction(new AbstractAction("Calculate Price") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(patientID.getText().equals("Enter Your NIC Number")  ||  consultStartTime.getText().equals("HH:MM")  || consultEndTime.getText().equals("HH:MM") ||  patientID.getText() == null || consultStartTime.getText() == null || consultEndTime.getText() == null || patientID.getText().isEmpty() || consultStartTime.getText().isEmpty() || consultEndTime.getText().isEmpty()) {

                    FeedbackDialogGUI("Calculate Price Error", "Fill Out All Required Fields");
                }
                else {

                    LocalTime startTime = LocalTime.parse(consultStartTime.getText());
                    LocalTime endTime = LocalTime.parse(consultEndTime.getText());

                    String price = CalculatePayment(patientID.getText(), startTime, endTime);

                    priceShowlbl.setText(price + " £");
                }

                }

        });
        CalculatePriceBtn.setBounds(770, 300, 150, 30);
        CalculatePriceBtn.setForeground(Color.BLACK);
        CalculatePriceBtn.setBackground(Color.WHITE);
        CalculatePriceBtn.setOpaque(false);

        CalculatePriceBtn.setBorder(compound);

        addNewConsultDialog.add(CalculatePriceBtn);

        //Add new consultation button (Save consultation)
        JButton AddBtn = new JButton(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(patientFirstName.getText().equals("Enter Your First Name") || patientLastName.getText().equals("Enter Your Last Name") || patientDOB.getText().equals("Enter Your Date of Birth (YYYY-MM-DD)") || patientMobile.getText().equals("Enter Your Mobile Number")  || patientID.getText().equals("Enter Your NIC Number")  || consultDate.getText().equals("Enter Consultation Date (YYYY-MM-DD)")  || consultStartTime.getText().equals("HH:MM")  || consultEndTime.getText().equals("HH:MM") || patientFirstName.getText() == null || patientLastName.getText() == null || patientDOB.getText() == null || patientMobile.getText() == null || patientID.getText() == null || consultDate.getText() == null || consultStartTime.getText() == null || consultEndTime.getText() == null || patientFirstName.getText().isEmpty() || patientLastName.getText().isEmpty() || patientDOB.getText().isEmpty() || patientMobile.getText().isEmpty() || patientID.getText().isEmpty() || consultDate.getText().isEmpty() || consultStartTime.getText().isEmpty() || consultEndTime.getText().isEmpty()) {

                        FeedbackDialogGUI("Consultation", "Fill Out All Required Fields!!");
                    }
                    else{
                        LocalDate DOB = LocalDate.parse(patientDOB.getText());

                        if (!ExistingPatientCheck(patientID.getText())) {

                            //Calling AddPerson method to create a new patient object and save it into patientList
                            AddPerson(patientFirstName.getText(), patientLastName.getText(), DOB, patientMobile.getText(), patientID.getText());
                        }

                        //calling getDoctorsFromComboBox method to get selected doctor's license
                        String license = getDoctorLicenseFromComboBox(doctorsCombolst.getSelectedItem().toString());

                        //Calling AddConsultation method to create a new consultation object and save it into consultationList
                        LocalDate date = LocalDate.parse(consultDate.getText());
                        LocalTime startTime = LocalTime.parse(consultStartTime.getText());
                        LocalTime endTime = LocalTime.parse(consultEndTime.getText());

                        String notes = "";

                        if (!(patientNotes.getText().isEmpty()) || !(patientNotes.getText() == null)) {
                            //Encrypting Notes
                            IntializeEncryptDecrypt();
                            notes = EncryptText(patientNotes.getText());
                        }

                        String price = CalculatePayment(patientID.getText(),startTime, endTime);




                        int consultId = AddConsultation(license, patientID.getText(), date, startTime, endTime, notes);
                        ReadPaymentFile();
                        paymentList.add(consultId + " " + price);

                        //Rename image to consultID

                        // Temp file
                        File file = new File("temp."+filetype[0]);

                        // Renamed file
                        File file2 = new File(consultId + "." + filetype[0]);

                        if (file2.exists())
                            throw new java.io.IOException("File already Exists");

                        // Rename file
                        boolean success = file.renameTo(file2);

                        if (!success) {
                            FeedbackDialogGUI("Error", "Could not Rename the File");
                        }

                        if (!checked){

                            String randomDoctor = RandomDoctorSelect(doctorsCombolst.getSelectedItem().toString(), date, startTime, endTime);
                            doctorsCombolst.setSelectedItem(randomDoctor);

                            FeedbackDialogGUI("Random Doctor Selected", randomDoctor);

                        }

                        SavePaymentFile();

                        FeedbackDialogGUI("Consultation Placed", "Your Consultation ID is : " + consultId);

                        addNewConsultDialog.dispose();
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();

                    FeedbackDialogGUI("Consultation Error", "Consultation Could Not Be Placed");
                }

            }
        });
        AddBtn.setBounds(700, 480, 100, 30);
        AddBtn.setForeground(Color.BLACK);
        AddBtn.setBackground(Color.WHITE);
        AddBtn.setOpaque(false);

        AddBtn.setBorder(compound);
        addNewConsultDialog.add(AddBtn);

        JButton CancelBtn = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewConsultDialog.dispose();

            }
        });
        CancelBtn.setBounds(820, 480, 100, 30);
        addNewConsultDialog.add(CancelBtn);
        CancelBtn.setForeground(Color.RED);
        CancelBtn.setBackground(Color.WHITE);
        CancelBtn.setOpaque(false);

        Border lineCancel = new LineBorder(Color.RED);
        Border marginCancel = new EmptyBorder(5, 15, 5, 15);
        Border compoundCancel = new CompoundBorder(lineCancel, marginCancel);

        CancelBtn.setBorder(compoundCancel);


        addNewConsultDialog.setVisible(true);
    }

    public void ShowAvailableDoctorsGUI(){

        JDialog availableDoctorsDialog= new JDialog((Frame) null, "All Available Doctors");
        availableDoctorsDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        availableDoctorsDialog.setSize(1000, 550);
        availableDoctorsDialog.setLayout(null);
        availableDoctorsDialog.setLocationRelativeTo(null);

        //Creating a JTable to show list of available doctors
        String tableColumns[] = {"First Name", "Surname", "DOB", "Mobile No", "Medical License", "Specialisation"};

        DefaultTableModel tableModel = new DefaultTableModel(tableColumns, 0){
            // The 0 argument is number of rows.
            @Override
            //Making table not-editable
            public boolean isCellEditable(int row, int column) {

                //Affects all cells
                return false;
            }
        };


        JTable table = new JTable(tableModel);

        //Get saved doctors from doctorList object array and adding into JTable
        if (WestminsterSkinConsultationManager.getDoctorList() != null) {
            for (int i = 0; i < WestminsterSkinConsultationManager.getDoctorList().size(); i++) {

                String firstname = WestminsterSkinConsultationManager.getDoctorList().get(i).getFirstname();
                String surname =  WestminsterSkinConsultationManager.getDoctorList().get(i).getSurname();
                String dob = String.valueOf(WestminsterSkinConsultationManager.getDoctorList().get(i).getDOB());
                String mobile =WestminsterSkinConsultationManager.getDoctorList().get(i).getMobileNo();
                String license = WestminsterSkinConsultationManager.getDoctorList().get(i).getMedicalLicense();
                String specialisation = WestminsterSkinConsultationManager.getDoctorList().get(i).getSpecialisation();

                Object[] objs = {firstname, surname, dob, mobile, license, specialisation};
                tableModel.addRow(objs);

            }
        }

        //Sorting Table (Surname - alphabetically)
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>(25);

        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);


        table.setGridColor(Color.lightGray);
        table.setRowHeight(50);
        table.setFont(Font.getFont("Segoe UI"));
        table.setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(500, 50));
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setUpdateTableInRealTime(true);
        header.setResizingAllowed(false);
        header.setOpaque(false);



        //Center Cell text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.setDefaultRenderer(Object.class, centerRenderer);

        //Adding table on to a scroll pane to show a scroll bar when table rows exceed the form height
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(980,460);
        availableDoctorsDialog.add(scrollPane);

        JButton sortByFirstName = new JButton(new AbstractAction("Sort by First Name") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.getRowSorter().toggleSortOrder(0);

            }
        });
        sortByFirstName.setBounds(20, 470, 200, 30);
        availableDoctorsDialog.add(sortByFirstName);
        sortByFirstName.setForeground(Color.BLACK);
        sortByFirstName.setBackground(Color.WHITE);
        sortByFirstName.setOpaque(false);

        JButton sortByLastName = new JButton(new AbstractAction("Sort by Surname") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.getRowSorter().toggleSortOrder(1);

            }
        });
        sortByLastName.setBounds(240, 470, 200, 30);
        availableDoctorsDialog.add(sortByLastName);
        sortByLastName.setForeground(Color.BLACK);
        sortByLastName.setBackground(Color.WHITE);
        sortByLastName.setOpaque(false);

        availableDoctorsDialog.setVisible(true);
    }

    public void ShowHistoryGUI(){


        JDialog showHistoryDialog= new JDialog((Frame) null, "View Consultation");
        showHistoryDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        showHistoryDialog.setSize(1000, 500);
        showHistoryDialog.setLayout(null);
        showHistoryDialog.setLocationRelativeTo(null);



        JLabel consultIDlbl = new JLabel("Consultation ID : ");
        consultIDlbl.setBounds(30, 20, 100, 10);
        showHistoryDialog.add(consultIDlbl);

        JTextField consultID = new JFormattedTextField("Enter Your Consultation ID");
        consultID.setForeground(Color.GRAY);
        consultID.setBounds(150, 12, 200, 30);
        consultID.setToolTipText("Enter Your Consultation ID");
       showHistoryDialog.add(consultID);


        JLabel patienFirstNamelbl = new JLabel("Patient First Name : ");
        patienFirstNamelbl.setBounds(30, 50, 200, 20);
        showHistoryDialog.add(patienFirstNamelbl);
        patienFirstNamelbl.setVisible(false);

        JLabel patienFirstName = new JLabel();
        patienFirstName.setBounds(150, 50, 200, 20);
        showHistoryDialog.add(patienFirstName);

        JLabel patientLastNamelbl = new JLabel("Patient Surname : ");
        patientLastNamelbl.setBounds(30, 100, 200, 20);
        showHistoryDialog.add(patientLastNamelbl);
        patientLastNamelbl.setVisible(false);

        JLabel patientLastName = new JLabel();
        patientLastName.setBounds(150, 100, 200, 20);
        showHistoryDialog.add(patientLastName);

        JLabel patientDOBlbl = new JLabel("Patient DOB   : ");
        patientDOBlbl.setBounds(30, 150, 200, 20);
        showHistoryDialog.add(patientDOBlbl);
        patientDOBlbl.setVisible(false);

        JLabel patientDOB = new JLabel();
        patientDOB.setBounds(150, 150, 200, 20);
        showHistoryDialog.add(patientDOB);

        JLabel patientMobilelbl = new JLabel("Patient Mobile : ");
        patientMobilelbl.setBounds(30, 200, 200, 20);
        showHistoryDialog.add(patientMobilelbl);
        patientMobilelbl.setVisible(false);

        JLabel patientMobile = new JLabel();
        patientMobile.setBounds(150, 200, 200, 20);
        showHistoryDialog.add(patientMobile);

        JLabel patientIDlbl = new JLabel("Patient NIC/ID : ");
        patientIDlbl.setBounds(30, 250, 250, 20);
        showHistoryDialog.add(patientIDlbl);
        patientIDlbl.setVisible(false);

        JLabel patientID = new JLabel();
        patientID.setBounds(150, 250, 250, 20);
        showHistoryDialog.add(patientID);

        JLabel patientNoteslbl = new JLabel("Patient Notes : ");
        patientNoteslbl.setBounds(30, 300, 300, 20);
        showHistoryDialog.add(patientNoteslbl);
        patientNoteslbl.setVisible(false);

        JButton ViewImageNote = new JButton();
        ViewImageNote.setAction(new AbstractAction("View Image") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {

                            try {
                                int key = 12345;


                                FileInputStream  fileinputstream = new FileInputStream(filePath);


                                // Converting image into byte array,it will
                                // Create a array of same size as image.
                                byte data[] = new byte[fileinputstream.available()];

                                // Read the image data array
                                fileinputstream.read(data);
                                int i = 0;

                                // Performing an XOR operation
                                // on each value of
                                // byte array to Decrypt it.
                                for (byte b : data) {
                                    data[i] = (byte)(b ^ key);
                                    i++;
                                }

                                // Opening file for writing purpose
                                FileOutputStream fileoutputstream = new FileOutputStream(filePath);

                                // Writing Decrypted data on Image
                                fileoutputstream.write(data);
                                fileoutputstream.close();
                                fileinputstream.close();
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            showDecryptedImageGUI(filePath);

                            try {
                                //Key for Encryption
                                int key = 12345;

                                FileInputStream fileinputstream= new FileInputStream(filePath);

                                byte data[] = new byte[fileinputstream.available()];

                                // Read the array
                                fileinputstream.read(data);

                                int i = 0;

                                for (byte b : data) {
                                    data[i] = (byte)(b ^ key);
                                    i++;
                                }

                                FileOutputStream fileoutputstream = new FileOutputStream(filePath);

                                fileoutputstream.write(data);

                                //Closing file
                                fileinputstream.close();
                                fileoutputstream.close();

                            } catch(IOException f) {
                                f.printStackTrace();
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        ViewImageNote.setBounds(350, 410, 150, 30);
        ViewImageNote.setForeground(Color.BLACK);
        ViewImageNote.setBackground(Color.WHITE);
        ViewImageNote.setOpaque(false);
        ViewImageNote.setVisible(false);
        ViewImageNote.setEnabled(false);

        showHistoryDialog.add(ViewImageNote);

        JTextArea patientNotes = new JTextArea();
        patientNotes.setBounds(30, 320, 300, 120);
        showHistoryDialog.add(patientNotes);
        patientNotes.setVisible(false);

        //Doctor Details
        JLabel doctorFirstNamelbl = new JLabel("Doctor First Name : ");
        doctorFirstNamelbl.setBounds(350, 50, 200, 20);
        showHistoryDialog.add(doctorFirstNamelbl);
        doctorFirstNamelbl.setVisible(false);

        JLabel doctorFirstName = new JLabel();
        doctorFirstName.setBounds(490, 50, 200, 20);
        showHistoryDialog.add(doctorFirstName);

        JLabel doctorLastNamelbl = new JLabel("Doctor Last Name : ");
        doctorLastNamelbl.setBounds(350, 100, 200, 20);
        showHistoryDialog.add(doctorLastNamelbl);
        doctorLastNamelbl.setVisible(false);

        JLabel doctorLastName = new JLabel();
        doctorLastName.setBounds(490, 100, 200, 20);
        showHistoryDialog.add(doctorLastName);

        JLabel doctorDOBlbl = new JLabel("Doctor DOB : ");
        doctorDOBlbl.setBounds(350, 150, 200, 20);
        showHistoryDialog.add(doctorDOBlbl);
        doctorDOBlbl.setVisible(false);

        JLabel doctorDOB = new JLabel();
        doctorDOB.setBounds(490, 150, 200, 20);
        showHistoryDialog.add(doctorDOB);

        JLabel doctorMobilelbl = new JLabel("Doctor Mobile : ");
        doctorMobilelbl.setBounds(350, 200, 200, 20);
        showHistoryDialog.add(doctorMobilelbl);
        doctorMobilelbl.setVisible(false);

        JLabel doctorMobile = new JLabel();
        doctorMobile.setBounds(490, 200, 200, 20);
        showHistoryDialog.add(doctorMobile);

        JLabel doctorLicenselbl = new JLabel("Doctor License No. : ");
        doctorLicenselbl.setBounds(350, 250, 200, 20);
        showHistoryDialog.add(doctorLicenselbl);
        doctorLicenselbl.setVisible(false);

        JLabel doctorLicense = new JLabel();
        doctorLicense.setBounds(490, 250, 200, 20);
        showHistoryDialog.add(doctorLicense);

        JLabel doctorSpecializationlbl = new JLabel("Doctor Specialization : ");
        doctorSpecializationlbl.setBounds(350, 300, 200, 20);
        showHistoryDialog.add(doctorSpecializationlbl);
        doctorSpecializationlbl.setVisible(false);

        JLabel doctorSpecialization = new JLabel();
        doctorSpecialization.setBounds(490, 300, 200, 20);
        showHistoryDialog.add(doctorSpecialization);

        //Consultation Details
        JLabel consultIDViewlbl = new JLabel("Consultation ID : ");
        consultIDViewlbl.setBounds(650, 50, 200, 20);
        showHistoryDialog.add(consultIDViewlbl);
        consultIDViewlbl.setVisible(false);

        JLabel consultIDView = new JLabel();
        consultIDView.setBounds(810, 50, 200, 20);
        showHistoryDialog.add(consultIDView);

        JLabel consultDatelbl = new JLabel("Consultation Date : ");
        consultDatelbl.setBounds(650, 100, 200, 20);
        showHistoryDialog.add(consultDatelbl);
        consultDatelbl.setVisible(false);

        JLabel consultDate = new JLabel();
        consultDate.setBounds(810, 100, 200, 20);
        showHistoryDialog.add(consultDate);

        JLabel consultStartlbl = new JLabel("Consultation Start Time : ");
        consultStartlbl.setBounds(650, 150, 200, 20);
        showHistoryDialog.add(consultStartlbl);
        consultStartlbl.setVisible(false);

        JLabel consultStart = new JLabel();
        consultStart.setBounds(810, 150, 200, 20);
        showHistoryDialog.add(consultStart);

        JLabel consultEndlbl = new JLabel("Consultation End Time : ");
        consultEndlbl.setBounds(650, 200, 200, 20);
        showHistoryDialog.add(consultEndlbl);
        consultEndlbl.setVisible(false);

        JLabel consultEnd = new JLabel();
        consultEnd.setBounds(810, 200, 200, 20);
        showHistoryDialog.add(consultEnd);

        JLabel consultPaymentlbl = new JLabel("Total Payment : ");
        consultPaymentlbl.setBounds(650, 250, 200, 20);
        showHistoryDialog.add(consultPaymentlbl);
        consultPaymentlbl.setVisible(false);

        JLabel consultPayment = new JLabel();
        consultPayment.setBounds(810, 250, 200, 20);
        showHistoryDialog.add(consultPayment);


        JButton ViewConsultBtn = new JButton();
        ViewConsultBtn.setAction(new AbstractAction("View Consultation") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(consultID.getText().equals("Enter Your Consultation ID")  ||  consultID.getText() == null || consultID.getText().isEmpty()) {

                    FeedbackDialogGUI("Cannot View Consultation", "Enter Your Consultation ID");
                }
                else {
                        if (CheckConsultationIDAvailability(consultID.getText())) {

                            ViewConsultBtn.setVisible(false);
                            consultID.setVisible(false);
                            consultIDlbl.setVisible(false);

                            patienFirstNamelbl.setVisible(true);
                            patientLastNamelbl.setVisible(true);
                            patientDOBlbl.setVisible(true);
                            patientMobilelbl.setVisible(true);
                            patientIDlbl.setVisible(true);
                            patientNoteslbl.setVisible(true);
                            patientNotes.setVisible(true);
                            doctorFirstNamelbl.setVisible(true);
                            doctorLastNamelbl.setVisible(true);
                            doctorDOBlbl.setVisible(true);
                            doctorMobilelbl.setVisible(true);
                            doctorLicenselbl.setVisible(true);
                            doctorSpecializationlbl.setVisible(true);
                            consultIDViewlbl.setVisible(true);
                            consultDatelbl.setVisible(true);
                            consultStartlbl.setVisible(true);
                            consultEndlbl.setVisible(true);
                            consultPaymentlbl.setVisible(true);
                            ViewImageNote.setVisible(true);

                            ReadPaymentFile();
                            ReadPersonFile();
                            ReadConsultationFile();

                            String doctorLicenseT = null;

                            LocalDate doctorDOBT = null;
                            String doctorMobileT = null;
                            String doctorSpecializationT = null;
                            String doctorFirstNameT = null;
                            String doctorLastNameT = null;

                            String patientIDT = null;
                            String patientFirstNameT = null;
                            String patientLastNameT = null;
                            LocalDate patientDOBT = null;
                            String patientMobileT = null;

                            String consult = consultID.getText();
                            String noteT = null;
                            String decryptedNote = null;

                            LocalDate consultDateT = null;
                            LocalTime startTimeT = null;
                            LocalTime endTimeT = null;

                            String paymentT = null;


                            if (consultationList.size() != 0) {
                                for (int i = 0; i < consultationList.size(); i++) {


                                    if (consultationList.get(i).getConsultSlot().equals(consultID.getText())) {

                                        patientIDT = consultationList.get(i).getPatientID();
                                        doctorLicenseT = consultationList.get(i).getDoctorMedicalLicense();
                                        consultDateT = consultationList.get(i).getConsultDate();
                                        startTimeT = consultationList.get(i).getConsultStartTime();
                                        endTimeT = consultationList.get(i).getConsultEndTime();
                                        noteT = consultationList.get(i).getConsultNote();

                                    }

                                }
                            }

                            try {
                                if (!noteT.isEmpty() || !(noteT == "") || !(noteT == null)) {
                                    decryptedNote = DecryptText(noteT);
                                }
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }

                            if (WestminsterSkinConsultationManager.getDoctorList().size() != 0) {

                                for (int i = 0; i < WestminsterSkinConsultationManager.getDoctorList().size(); i++) {

                                    if (WestminsterSkinConsultationManager.getDoctorList().get(i).getMedicalLicense().equals(doctorLicenseT)) {

                                        doctorFirstNameT = WestminsterSkinConsultationManager.getDoctorList().get(i).getFirstname();
                                        doctorLastNameT = WestminsterSkinConsultationManager.getDoctorList().get(i).getSurname();
                                        doctorSpecializationT = WestminsterSkinConsultationManager.getDoctorList().get(i).getSpecialisation();
                                        doctorDOBT = WestminsterSkinConsultationManager.getDoctorList().get(i).getDOB();
                                        doctorMobileT = WestminsterSkinConsultationManager.getDoctorList().get(i).getMobileNo();

                                    }

                                }
                            }

                            if (patientList.size() != 0) {
                                for (int i = 0; i < patientList.size(); i++) {


                                    if (patientList.get(i).getPatientId().equals(patientIDT)) {

                                        patientFirstNameT = patientList.get(i).getFirstname();
                                        patientLastNameT = patientList.get(i).getSurname();
                                        patientDOBT = patientList.get(i).getDOB();
                                        patientMobileT = patientList.get(i).getMobileNo();

                                    }

                                }
                            }

                            if (paymentList.size() != 0) {
                                for (int i = 0; i < paymentList.size(); i++) {
                                    String line = paymentList.get(i).toString();

                                    String[] temp = line.split("\\s");

                                    String consultId = temp[0];
                                    String paymentT2 = temp[1];

                                    if (consultId.equals(consultID.getText())) {

                                        paymentT = paymentT2;


                                    }


                                }
                            }

                            patienFirstName.setText(patientFirstNameT);
                            patientLastName.setText(patientLastNameT);
                            patientDOB.setText(patientDOBT.toString());
                            patientMobile.setText(patientMobileT);
                            patientID.setText(patientIDT);
                            patientNotes.setText(decryptedNote);
                            patientNotes.setEditable(false);

                            doctorFirstName.setText(doctorFirstNameT);
                            doctorLastName.setText(doctorLastNameT);
                            doctorDOB.setText(doctorDOBT.toString());
                            doctorMobile.setText(doctorMobileT);
                            doctorLicense.setText(doctorLicenseT);
                            doctorSpecialization.setText(doctorSpecializationT);

                            consultIDView.setText(consult);
                            consultDate.setText(consultDateT.toString());
                            consultStart.setText(startTimeT.toString());
                            consultEnd.setText(endTimeT.toString());

                            consultPayment.setText(paymentT + "$");

                            filePath= consult + ".jpg";


                            File file = new File(filePath);
                            if(file.exists()) {
                                ViewImageNote.setEnabled(true);

                            }
                        }
                        else
                        {
                            FeedbackDialogGUI("Cannot View Consultation", "No Consultations Found");
                        }




                }

            }

        });
        ViewConsultBtn.setBounds(400, 12, 150, 30);
        ViewConsultBtn.setForeground(Color.BLACK);
        ViewConsultBtn.setBackground(Color.WHITE);
        ViewConsultBtn.setOpaque(false);

        consultID.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                consultID.setText("");
                consultID.setForeground(Color.BLACK);
            }

            public void focusLost(FocusEvent e) {
                if (consultID.getText().isEmpty() || consultID.getText() == "") {
                    consultID.setForeground(Color.GRAY);
                    consultID.setText("Enter Your Consultation ID");
                }
            }
        });

        //ViewConsultBtn.setBorder(compound);

        showHistoryDialog.add(ViewConsultBtn);



        showHistoryDialog.setVisible(true);

    }

    public void showDecryptedImageGUI(String path){
        JDialog ViewImage = new JDialog((Frame) null,"Patient Notes Image");
        ViewImage.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        ViewImage.setSize(1000, 600);
        ViewImage.setLayout(null);
        ViewImage.setLocationRelativeTo(null);
        ViewImage.setBackground(Color.WHITE);

        ViewImage.setContentPane(new JLabel(new ImageIcon(path)));

        ViewImage.setVisible(true);
    }


    public String[] DoctorsListCombo(){
        String[] list = new String[WestminsterSkinConsultationManager.getDoctorList().size()];

        if (WestminsterSkinConsultationManager.getDoctorList() != null) {
            for (int i = 0; i < WestminsterSkinConsultationManager.getDoctorList().size(); i++) {

                String firstname = WestminsterSkinConsultationManager.getDoctorList().get(i).getFirstname();
                String surname =  WestminsterSkinConsultationManager.getDoctorList().get(i).getSurname();
                String license = WestminsterSkinConsultationManager.getDoctorList().get(i).getMedicalLicense();
                String specialisation = WestminsterSkinConsultationManager.getDoctorList().get(i).getSpecialisation();

                list[i] = "Dr. " + firstname + " " + surname + " " + specialisation + " " + license;

            }
        }

        return list;
    }


    public void AddPerson(String firstname, String surname, LocalDate dob, String mobile, String id) {

        Patient patient = new Patient(firstname, surname, dob, mobile, id);
        patientList.add(patient);

        SavePersonFile();
    }


    public int AddConsultation(String doctorLicense, String patientID, LocalDate consultDate, LocalTime consultStart, LocalTime consultEnd, String note) {
        ReadConsultationFile();
        //Creating an id for consultation
        String id = String.valueOf(consultationList.size() + 1);



        //Creating new consult Object
        Consultation consult = new Consultation(id, doctorLicense, patientID, consultDate, consultStart, consultEnd, note);

        //Adding created consult object to consultationList object arraylist
        consultationList.add(consult);

        SaveConsultationFile();

        return Integer.parseInt(id);
    }


    public void SavePersonFile() {

        try {
            PrintWriter patientsFile = new PrintWriter("patients.txt");

            for (Patient patient : patientList) {

                patientsFile.println(patient.getFirstname() + " " + patient.getSurname() + " " + patient.getDOB() + " " + patient.getMobileNo() + " " + patient.getPatientId());

            }
            patientsFile.close();

            System.out.println("Patients File Saved");

        } catch (Exception e) {
            System.out.println("Patient File Write Error!");
        }

    }


    public void SaveConsultationFile() {

        try {
            PrintWriter consultationFile = new PrintWriter("consultations.txt");

            for (Consultation consultation : consultationList) {

                consultationFile.println(consultation.getConsultSlot() + "_" + consultation.getDoctorMedicalLicense() + "_" + consultation.getPatientID() + "_" + consultation.getConsultDate() + "_" + consultation.getConsultStartTime() + "_" + consultation.getConsultEndTime() + "_" + consultation.getConsultNote());

            }
            consultationFile.close();

            System.out.println("Consultations file saved");

        } catch (Exception e) {
            System.out.println("Consultations File Write Error!");
        }

    }

    public void SavePaymentFile() {

        try {
            PrintWriter paymentFile = new PrintWriter("payment.txt");

            for (String str : paymentList) {

                paymentFile.println(str);
            }
            paymentFile.close();

            System.out.println("Payment File Saved");

        } catch (Exception e) {
            System.out.println("Payment File Write Error");
        }

    }


    public void ReadPaymentFile() {

        try  {
            paymentList.clear();
            FileReader file = new FileReader("payment.txt");
            BufferedReader reader = new BufferedReader(file);

            String line;
            while ((line = reader.readLine()) != null){

                String[] temp = line.split("\\s");

                String consultId = temp[0];
                String payment = temp[1];

                paymentList.add(consultId + " " + payment);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void ReadPersonFile() {

        try  {
            patientList.clear();
            FileReader file = new FileReader("patients.txt");
            BufferedReader reader = new BufferedReader(file);

            String line;
            while ((line = reader.readLine()) != null){

                String[] temp = line.split("\\s");
                String surname = temp[0];
                String firstname = temp[1];
                String dob = temp[2];
                LocalDate DOB = LocalDate.parse(dob);
                String mobile = temp[3];
                String id = temp[4];

                Patient patient = new Patient(surname, firstname, DOB, mobile, id);

                patientList.add(patient);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean ExistingPatientCheck(String id){
        boolean result = false;

        for(int i = 0; i < patientList.size(); i++){
            if (patientList.get(i).getPatientId().equals(id)){

                result = true;
            }

        }

        return result;
    }


    public void ReadConsultationFile() {

        try  {
            consultationList.clear();
            FileReader file = new FileReader("consultations.txt");
            BufferedReader reader = new BufferedReader(file);

            String line;
            while ((line = reader.readLine()) != null){

                String[] temp = line.split("_");
                String id = temp[0];
                String license = temp[1];
                String patientId = temp[2];
                String consultDate = temp[3];
                LocalDate  date = LocalDate.parse(consultDate);
                String startTime = temp[4];
                LocalTime start = LocalTime.parse(startTime);
                String endTime = temp[5];
                LocalTime end = LocalTime.parse(endTime);
                String note = temp[6];

                Consultation consult = new Consultation(id, license, patientId, date, start, end, note);

                consultationList.add(consult);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean CheckConsultations(String license, LocalDate date, LocalTime start, LocalTime end) {

        boolean isAvailable = true;

        ReadConsultationFile();
        if (consultationList.size() != 0) {
            for (int i = 0; i < consultationList.size(); i++) {


                if (consultationList.get(i).getConsultDate().equals(date) && consultationList.get(i).getDoctorMedicalLicense().equals(license)) {

                    boolean result = isBetween(start, end, consultationList.get(i).getConsultStartTime(), consultationList.get(i).getConsultEndTime());


                    if (result) {
                        isAvailable = false;
                    }
                }

            }
        }
        return isAvailable;



    }

    public String getDoctorLicenseFromComboBox(String comboResult){

        //This function is used to get only the medical license number of the doctor from selected item on combo box.
        String[] temp = comboResult.split("\\s");
        String license = temp[4];

        return license;
    }

    public static boolean isBetween(LocalTime start, LocalTime end, LocalTime startSaved, LocalTime endSaved) {

        //Calculate whether the user inserted start and end times conflict with other consultations of the same doctor
        //returns true when conflict
        return (!start.isAfter(startSaved)  && !end.isBefore(endSaved)) || (!end.isBefore(startSaved) && !start.isAfter(endSaved));
    }

    public String RandomDoctorSelect(String doctor, LocalDate date, LocalTime start, LocalTime end){
        ArrayList<String> selectedDoctors = new ArrayList<>();

        String license = getDoctorLicenseFromComboBox(doctor);

        for (int i = 0; i < WestminsterSkinConsultationManager.getDoctorList().size(); i++) {
            //Ignoring user selected doctor
            if (!WestminsterSkinConsultationManager.getDoctorList().get(i).getMedicalLicense().equals(license)){

                boolean result = CheckConsultations(WestminsterSkinConsultationManager.getDoctorList().get(i).getMedicalLicense(), date, start, end);

                if (result == true){

                    String firstname = WestminsterSkinConsultationManager.getDoctorList().get(i).getFirstname();
                    String surname =  WestminsterSkinConsultationManager.getDoctorList().get(i).getSurname();
                    String license2 = WestminsterSkinConsultationManager.getDoctorList().get(i).getMedicalLicense();
                    String specialisation = WestminsterSkinConsultationManager.getDoctorList().get(i).getSpecialisation();

                    selectedDoctors.add("Dr. " + firstname + " " + surname + " " + specialisation + " " + license2);
                }

            }
        }

        //Selecting a random doctor
        Random rand = new Random();
        int randomSelected = rand.nextInt(selectedDoctors.size());

        String randomSelectedDoctor = selectedDoctors.get(randomSelected);

        return randomSelectedDoctor;
    }

    public String CalculatePayment(String patientID, LocalTime start, LocalTime end){

        //Just in case
        ReadConsultationFile();
        ReadPersonFile();

        //
        boolean patientType = PatientCheck(patientID);
        int payment = 0;
        int hours = CalculateHours(start, end);

        if (patientType == false){

            payment = hours * 15;
        }
        else{
            payment = hours * 25;
        }


        return String.valueOf(payment);
    }

   //Checking whether patient has consulted a doctor before
    public boolean PatientCheck(String patientID){
        boolean result = false;

        for (int i = 0; i < patientList.size(); i++){
            if(patientList.get(i).getPatientId().equals(patientID)){
                result = true;
            }
        }

        return result;
    }

    public boolean FeedbackDialogGUI(String title, String feedback){

        JDialog feedbackDialog= new JDialog((Frame) null, title);
        feedbackDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        feedbackDialog.setSize(400, 100);
        feedbackDialog.setLayout(null);
        feedbackDialog.setLocationRelativeTo(null);

        JLabel feedbacklbl = new JLabel(feedback);
        feedbacklbl.setBounds(20, 10, 200, 40);

        //OK Button
        JButton OKButton = new JButton(new AbstractAction("OK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                feedbackDialog.dispose();

            }
        });
        OKButton.setBounds(260, 15, 100, 30);
        OKButton.setForeground(Color.BLACK);
        OKButton.setBackground(Color.WHITE);
        OKButton.setOpaque(false);


        feedbackDialog.add(OKButton);
        feedbackDialog.add(feedbacklbl);


        feedbackDialog.setVisible(true);

        return true;
    }


    public void IntializeEncryptDecrypt() throws Exception {
        myEncryptionKey = "WestminsterConsultationS";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }


    public String EncryptText(String unencryptedString) throws Exception {
        IntializeEncryptDecrypt();
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }


    public String DecryptText(String encryptedString) throws Exception {
        IntializeEncryptDecrypt();
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public int CalculateHours(LocalTime start, LocalTime end){

        int timeDifference = 0;

        // Calculating time difference
        int differenceHours = Math.abs(start.getHour() - end.getHour());
        int differenceMinutes = Math.abs(start.getMinute() - end.getMinute());

        timeDifference = differenceHours;

        if (differenceMinutes > 0){
            timeDifference = timeDifference + 1;
        }

        return timeDifference;
    }

    public boolean CheckConsultationIDAvailability(String id){

        boolean result = false;

        ReadConsultationFile();

        if (consultationList.size() != 0) {
            for (int i = 0; i < consultationList.size(); i++) {


                if (consultationList.get(i).getConsultSlot().equals(id)) {

                    result = true;

                }

            }
        }

        return result;
    }
}

