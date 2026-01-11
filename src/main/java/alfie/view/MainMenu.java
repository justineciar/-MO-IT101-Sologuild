/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package alfie.view;

/**
 * 
 * Part of MotorPH Change Requests
 * Change request form: MPHCR01-Feature 1
 * Purpose: Main GUI (Home screen of the application)
 * 
 */

/*
 * Import from one specific class from this project
 * Note: To use wildcard(*) import from own project, add * after the package.
 *      Ex. alfie.util.*;   instead of alfie.util.className; 
 */
import alfie.model.Employee;
import alfie.util.AttendanceFileHandler;    // Both handler can be use(import) to main.
import alfie.util.EmployeeFileHandler;      // Used only here to initialized when need.
import alfie.util.SalaryCalculator;

/*
 * Import from "Java Standard Library(JSL)".
 * Wildcard(*) is used to this project.
 * To save memory, import a needed single class only.
 */
import javax.swing.*;   // All public class in javax.swing from JSL
import java.awt.*;      // All public class in java.awt from JSL
import java.util.List;  // Single class in java.util from JSL

public class MainMenu extends JFrame {  // JFrame as a main GUI

    /*
     * Declaration: variable name & type created for this class
     * Instance variable(Fields): This is belong to the object, not a method.
    */ 
    private EmployeeListView employeeListView;
    private boolean isLoggedIn = false;
    private JPanel mainPanel;

    private final JButton loginButton;
    private final JButton empRecBtn;
    private final JButton addAdminBtn;
    private final JButton exitBtn;
    private final JTextField searchField;
    private final JButton searchButton;
    private final JButton computeButton;
    private final JComboBox<String> monthCombo;
    private final JComboBox<String> yearCombo;

    private final DefaultListModel<String> listModel;
    private final DefaultListModel<String> resultModel;
    private final JList<String> searchResultList;
    private final JList<String> resultList;

    private List<Employee> allEmployees;
    private final AttendanceFileHandler attendanceHandler;
    private final SalaryCalculator salaryCalculator;

    public MainMenu() {     // When the app open for the first time the main screen is not visible until login.
    // *Code block 1.0 -- Login function
        showLoginDialog();  // A. The LoginPanel.java class will show first using JDialog. Also, if the user is log out.
        if (!isLoggedIn) {  // B. Check the value of "isLoggedIn" using logical NOT(!) operator. Means: "If the user is NOT logged in,  show login dialog."
            System.exit(0); // Terminate the entire Java app. Zero(0) means normal/successful termination.
        }

    // *Code Block 2.0 -- Main UI basic settings
        setTitle("MotorPH Payroll System - Main Menu");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

      // *Instantiation Block 1.0 -- File handler
        EmployeeFileHandler handler = new EmployeeFileHandler();    // Instantiation for employee file handler. Import from this project.
        attendanceHandler = new AttendanceFileHandler();            // Instantiation for attendance file handler. Import from this project
        salaryCalculator = new SalaryCalculator(attendanceHandler); /* Instantiation for salary calculator. Import from this project.
                                                                     *  This is dependency injection.
                                                                     *  Instead of SalaryCalculator creating its own AttendanceFileHnadler,
                                                                     *      i: 1. Create the dependecy outside
                                                                     *///         2. Inject it into SalaryCalculator
        allEmployees = handler.readEmployees();                     /* Read employee record(CSV) using readEmployess method(from EmployeeFileHandler)
                                                                     * thru the instantiated "handler" from this class [EmployeeFileHandler();].
                                                                     */

      // *Instantiation Block 2.0 -- Search function
        searchField = new JTextField();
        searchButton = new JButton("Search");
        
      // *Instantiation Block 3.0 -- Search resul list
        // This code creates a list model to store data, binds it to a JList for display, and wraps the list in a JScrollPane to enable scrolling.
        listModel = new DefaultListModel<>();                       // DefaultListModel is part of javax.swing.* JSL
        searchResultList = new JList<>(listModel);                  /* Create [JList] and connects it to [listModel]
                                                                     *  Model binding:
                                                                     *      1. The [JList] automatically displays whatever is in [listModel]
                                                                     *///      2. If the model canges, the list updates instantly.
        JScrollPane scrollPane = new JScrollPane(searchResultList); /* 1. Wraps the [JList] inside the [JScrollPane].
                                                                     *  2. This will add a vertical scroll bar automatically.
         *  DefualtListModel                1. Create a data container.        
         *      └─  JList                   2. Show the data in a list
         *              └─  JScrollPane     3. Put the list inside a scrollable view
        */

      // *Instantiation Block 4.0 -- Compute salary button
        computeButton = new JButton("Compute Salary");
        computeButton.setVisible(true);     // Set the Button true to show in GUI
        computeButton.setEnabled(false);    // Set to false to make it disabled. Become enable when the condition is meet.

      // *Instantiation Block 5.0 -- Model binding. See listModel note.
        resultModel = new DefaultListModel<>();
        resultList = new JList<>(resultModel);
        resultList.setBorder(BorderFactory.createTitledBorder("Salary Computation"));

    // *Code block 3.0 -- Year and Month combo box
        // Both line creates a JCombobox populated with a predefined list of months and years, selection as dropdown.
        monthCombo = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"});
        yearCombo = new JComboBox<>(new String[]{"2023", "2024", "2025"});

    // *Code Block 4.0 -- Search listener
        // These statements attach event listeners using lambda expressions to respond to user actions,
        // enabling search functionality and controlling button availability based on list selection.
        searchButton.addActionListener(e -> performSearch());   // This is a Lambda Expression. The [performSearch()] is a custom method from this class.
                                                                 // [addActionListener] -- As a click handler && [e] -- The ActionEvent
        searchResultList.addListSelectionListener(e -> computeButton.setEnabled(!searchResultList.isSelectionEmpty())); // User must select a search result before computing salary
        /*                  |                                  |                        └─ Item selected -> Button [true]enable || [False]disabled
        *                   |                                  └─ Every time the selection chnages:
        *                   |                                           a. Checks if any item is selected.
        *                   └─ Adds Listener to the JList               b. Enable the compute button.
        */

    // *Code Block 5.0 -- Attendance lookup and salary computation
        // These code block can be refactor in to a new class.
        // <--- Refactoring starts here --->
        computeButton.addActionListener(e -> {                                 // Lambda expression. Attaches ActionListener to [computeButton].
            resultModel.clear();                                                // Clear previous result.
        // *Code Block 5.1 -- Look up for employee ID, Month and Year
            String selectedEmployee = searchResultList.getSelectedValue();      // Get the detail(s) of current selected item. [.getSelectedValue] is part of Swing
            if (selectedEmployee != null && !selectedEmployee.contains("No results")) { // Ensure somthing is seleted and prevents processing the placeholder message "No results".
                String empNum = selectedEmployee.split(" - ")[0];                 // Extract the employee number. Split the string using "-" as delimeter. 
                String month = (String) monthCombo.getSelectedItem();           // Get and dispalyed the selected month.
                String monthTwoDigit = String.format("%02d", monthCombo.getSelectedIndex() + 1); // Convert month to two-digit format.
                                            /*          |                       |            └─ Convert to a real month number
                                             *          |                       └─ Index of selected month (0-based)
                                             *          └─ Format as two digits
                                             *///  Ex. March is selected: Index = 2. Therefor 2 + 1 = 3. Result = "03"
                String year = (String) yearCombo.getSelectedItem();             // Get the selected year from the dropdown (combo box).

            // *Code Block 5.1.1 -- Employee search and filter using Java Stream
                Employee emp = allEmployees.stream()                        // Turns the list into a stream
                    .filter(e1 -> e1.getEmployeeNumber().equals(empNum))   // Keeps only element that match a condition. [e1] = One employee at a time.
                    .findFirst()                                            // Stops the stream at the first matching employee.
                    .orElse(null);                                          // If employee found - return it. If not - return [null].
                /* End Note for Code Block 5.1.1
                 *          *** This code uses Java Streams to search a list of empoyees, filtering by employee number,
                 *              and returns the first matching or [null] if none is found. ***
                */

            // *Code Block 5.1.2 -- Salary calculation and result format
                if (emp != null) {  // Ensure that the employee exists and the search didn't fail
                    double totalHours = salaryCalculator.calculateMonthlyHours(empNum, year, monthTwoDigit); // Compute the daily works and adds them together for the selected month
                                    //          |                   └─ Method from SalaryCalculator.java class
                                    //          └─ Calls the SalaryCalculator and read the attendance records. See: *Instantiation Block 1.0
                    double salary = salaryCalculator.calculateSalary(emp, totalHours); // Calculate salary based on hours.
                                    //                      └─ Method from SalaryCalculator.java class
                    double fixedComp = (salary > 0) ? salaryCalculator.calculateTotalWithAllowances(emp) : 0.0; // Calculate fixed compensation.
                                    //                                          └─ Method from SalaryCalculator.java class
                    double totalPay = salary + fixedComp;   // Calculate final pay.

                    resultModel.addElement("Employee #" + empNum);
                    resultModel.addElement("Month: " + month + " " + year);
                    resultModel.addElement("Total Hours: " + String.format("%.2f", totalHours));
                    resultModel.addElement("Hourly Salary: ₱" + String.format("%.2f", salary));
                    resultModel.addElement("Fixed Allowances: ₱" + String.format("%.2f", fixedComp));
                    resultModel.addElement("TOTAL: ₱" + String.format("%.2f", totalPay));
                    resultModel.addElement("------------------------------");
                /* End note for Code Block 5.1.2
                 *          *** This code checks if the employee exists, calculates total monthly hours, computes salary based on hourly rate,
                 *///           adds fixed allowance if salary is greater than zero, and finally calculates the total pay
                }
            }
        });
        // <--- Refactoring ends here --->

      // *Instantiation Block 6.0 -- Button & Field search panel
        JPanel topSearchPanel = new JPanel(new BorderLayout(5, 5));
        topSearchPanel.add(searchField, BorderLayout.CENTER);
        topSearchPanel.add(searchButton, BorderLayout.EAST);

      // *Instantiation Block 7.0 -- Search result panel
        JPanel searchResultPanel = new JPanel(new BorderLayout(5, 5));
        searchResultPanel.add(scrollPane, BorderLayout.CENTER);

      // *Instantiation Block 8.0 -- Search panel components
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Employee"));
        searchPanel.add(topSearchPanel, BorderLayout.NORTH);
        searchPanel.add(searchResultPanel, BorderLayout.CENTER);

        empRecBtn = new JButton("Employee Records");
        addAdminBtn = new JButton("   "); //add admin
        loginButton = new JButton("Logout");
        exitBtn = new JButton("Exit");

      // *Instantiation Block 9.0 -- Top/Right panel component
        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new BoxLayout(topRightPanel, BoxLayout.Y_AXIS));
        for (JComponent comp : new JComponent[]{empRecBtn, computeButton, monthCombo, yearCombo}) {
            comp.setMaximumSize(new Dimension(200, 30));
            comp.setAlignmentX(Component.CENTER_ALIGNMENT);
            topRightPanel.add(Box.createVerticalStrut(10));
            topRightPanel.add(comp);
        }
        
      // *Instantiation Block 10.0 -- Center/Right panel component
        JPanel centerRightPanel = new JPanel();
        centerRightPanel.setLayout(new BoxLayout(centerRightPanel, BoxLayout.Y_AXIS));
        for (JList list : new JList[]{resultList}) {
            list.setMaximumSize(new Dimension(200, 180));
            list.setAlignmentX(TOP_ALIGNMENT);
            centerRightPanel.add(Box.createVerticalStrut(10));
            centerRightPanel.add(list);
        }
        centerRightPanel.add(new JScrollPane(resultList));

      // *Instantiation Block 11.0 -- Buttom/Right panel component
        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setLayout(new BoxLayout(bottomRightPanel, BoxLayout.Y_AXIS));
        for (JButton btn : new JButton[]{addAdminBtn, loginButton, exitBtn}) {
            btn.setMaximumSize(new Dimension(200, 30));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            bottomRightPanel.add(Box.createVerticalStrut(10));
            bottomRightPanel.add(btn);
        }

      // *Instantiation Block 12.0 -- Right panel component
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(topRightPanel, BorderLayout.NORTH);
        rightPanel.add(centerRightPanel, BorderLayout.CENTER);
        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);

      // *Instantiation Block 13.0 -- Main panel component
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

    // *Code Block 6.0 -- Employee Record Button
        empRecBtn.addActionListener(e -> {  // When button is click. Run the lambda expression code block.
            if (employeeListView == null || !employeeListView.isDisplayable()) {    // Ensure to don't create a multiple windows unnecessarily.
                employeeListView = new EmployeeListView(this, isLoggedIn);           // (this, isLoggedIn) passes login to the window.
            }       // The if statement ensure that the window exist or closed, if both is false, it will create a new window.
            employeeListView.setVisible(true);              // Show the window to the screen.
        });         // employeeListView(Field) are imported from EmployeeListView.java class

    // *Code Block 7.0 -- Right button
        addAdminBtn.setEnabled(isLoggedIn);     // Button enable only when login
        addAdminBtn.addActionListener(e -> new NewAdminForm(this).setVisible(true));    // Method chaining
            /*                                  |             |         └─ Makes it visible
             *                                  |             └─ Passes the window as the parent reference
             *///                               └─ Instantiate and create a NewAdminForm(this) window
        loginButton.addActionListener(e -> handleLogout());     // Calls the handleLogout method
        exitBtn.addActionListener(e -> System.exit(0));         // Terminate the entire java app
    }

    // This method searches employees by employee number thru the search field and update a [JList] with matching results.
    // Call only when the search button is click or text changes.
    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();      // Get the text entered at searchField. trim() -> Removes leadeing/trailing spaces.
        listModel.clear();          // Clear previuos search first before showing the current search. Prevents old result from stacking with new ones.
        if (query.isEmpty()) return;                // Stop the method if the search field is empty.
        allEmployees.stream()       // Converts the allEmployees collection into a stream for functional style processing.
                .filter(e -> e.getEmployeeNumber().toLowerCase().contains(query))       // Allow partial matching.
        // Fileter employees where: └─ There employee number(lowercase)   └─ Contains the search query
                .forEach(e -> listModel.addElement(e.getEmployeeNumber() + " - " + e.getFirstName() + " " + e.getLastName())); // Adds each match employee to the list model.
        if (listModel.isEmpty()) {                  // If no employees matched the query:
            listModel.addElement("No results found.");   // Dispalys a user-friendly message instead of leaving the list blank.
        }
    }

    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(300, 200);
        loginDialog.setLocationRelativeTo(null);
        LoginPanel loginPanel = new LoginPanel(() -> { // On the same package, import is not needed.
            isLoggedIn = true;
            loginDialog.dispose();
        });
        loginDialog.add(loginPanel);
        loginDialog.setVisible(true);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            isLoggedIn = false;
            loginButton.setText("Login");
            addAdminBtn.setEnabled(false);
            if (employeeListView != null && employeeListView.isDisplayable()) {
                employeeListView.setProtectedButtonsEnabled(false);
            }
            showLoginDialog();
            if (!isLoggedIn) {
                dispose();
                System.exit(0);
            } else {
                loginButton.setText("Logout");
                enableProtectedFeatures(true);
            }
        }
    }

    private void enableProtectedFeatures(boolean enable) {
        addAdminBtn.setEnabled(enable);
        if (employeeListView != null && employeeListView.isDisplayable()) {
            employeeListView.setProtectedButtonsEnabled(enable);
        }
    }
}