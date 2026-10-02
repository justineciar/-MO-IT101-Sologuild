package com.mycompany.motorphemployeeapp;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class LoginFrame extends JFrame{

    // Input fields
    private JTextField employeeField;
    private JPasswordField passwordField;

    // Employee manager
    private EmployeeDataManager manager;

    // Custom fonts
    private Font geologicaRegular;
    private Font geologicaBold;

    public LoginFrame(){

        try{
            // Load fonts
            InputStream regularStream=getClass().getResourceAsStream("/Fonts/Geologica-Regular.ttf");
            InputStream boldStream=getClass().getResourceAsStream("/Fonts/Geologica-Bold.ttf");

            geologicaRegular=Font.createFont(Font.TRUETYPE_FONT,regularStream);
            geologicaBold=Font.createFont(Font.TRUETYPE_FONT,boldStream);

        }catch(Exception e){
            geologicaRegular=new Font("Arial",Font.PLAIN,22);
            geologicaBold=new Font("Arial",Font.BOLD,22);
        }

        try{

            // Initialize manager
            manager=new EmployeeDataManager();

            // Load CSV
            manager.loadEmployeeData("src/main/resources/MotorPH_Employee Data - Employee Details.csv");

            // Frame settings
            setTitle("MotorPH Login");
            setSize(1000,700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            setLayout(null);

            // Background panel
            JPanel background=new JPanel(){
                @Override
                protected void paintComponent(Graphics g){
                    super.paintComponent(g);
                    Graphics2D g2=(Graphics2D)g.create();
                    ImageIcon bg=new ImageIcon(getClass().getResource("/Images/MotorPHBG.png"));
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.8f));
                    g2.drawImage(bg.getImage(),0,0,getWidth(),getHeight(),this);
                    g2.dispose();
                }
            };

            background.setBounds(0,0,1000,700);
            background.setLayout(null);

            // Right panel
            JPanel rightPanel=new JPanel();
            rightPanel.setBounds(500,0,500,700);
            rightPanel.setBackground(new Color(217,217,217,200));
            rightPanel.setLayout(null);

            // Logo
            ImageIcon logoIcon=new ImageIcon(getClass().getResource("/Images/logo.png"));
            Image originalImage=logoIcon.getImage();

            int originalWidth=logoIcon.getIconWidth();
            int originalHeight=logoIcon.getIconHeight();

            // Preserve aspect ratio
            int newHeight=45;
            int newWidth=(originalWidth*newHeight)/originalHeight;

            Image scaledLogo=originalImage.getScaledInstance(newWidth,newHeight,Image.SCALE_SMOOTH);
            JLabel logoLabel=new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setBounds(190,12,newWidth,newHeight);

            // Company label
            JLabel companyLabel=new JLabel("MotorPH"){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setFont(getFont());

                    // Shadow
                    g2.setColor(new Color(120,120,120,130));
                    g2.drawString(getText(),2,31);

                    // Main text
                    g2.setColor(Color.BLACK);
                    g2.drawString(getText(),1,30);
                    g2.dispose();
                }
            };

            companyLabel.setBounds(250,15,220,40);
            companyLabel.setFont(geologicaBold.deriveFont(30f));

            // Login label
            JLabel titleLabel=new JLabel("EMPLOYEE LOGIN"){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setFont(getFont());

                    // Shadow
                    g2.setColor(new Color(120,120,120,130));
                    g2.drawString(getText(),2,41);

                    // Main text
                    g2.setColor(Color.BLACK);
                    g2.drawString(getText(),1,40);
                    g2.dispose();
                }
            };

            titleLabel.setBounds(90,110,350,50);
            titleLabel.setFont(geologicaBold.deriveFont(38f));

            // User label
            JLabel userLabel=new JLabel("Employee ID:"){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setFont(getFont());

                    // Shadow
                    g2.setColor(new Color(120,120,120,130));
                    g2.drawString(getText(),2,26);

                    // Main text
                    g2.setColor(Color.BLACK);
                    g2.drawString(getText(),1,25);
                    g2.dispose();
                }
            };

            userLabel.setBounds(90,210,300,40);
            userLabel.setFont(geologicaBold.deriveFont(22f));

            // Password label
            JLabel passLabel=new JLabel("Password:"){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setFont(getFont());

                    // Shadow
                    g2.setColor(new Color(120,120,120,130));
                    g2.drawString(getText(),2,26);

                    // Main text
                    g2.setColor(Color.BLACK);
                    g2.drawString(getText(),1,25);
                    g2.dispose();
                }
            };

            passLabel.setBounds(90,340,200,40);
            passLabel.setFont(geologicaBold.deriveFont(22f));

            // Employee field
            employeeField=new JTextField(){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
                    super.paintComponent(g);
                    g2.dispose();
                }

                @Override
                protected void paintBorder(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(220,220,220));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,25,25);
                    g2.dispose();
                }
            };

            employeeField.setBounds(90,250,320,60);
            employeeField.setFont(geologicaBold.deriveFont(20f));
            employeeField.setOpaque(false);
            employeeField.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

            // Password field
            passwordField=new JPasswordField(){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
                    super.paintComponent(g);
                    g2.dispose();
                }

                @Override
                protected void paintBorder(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(220,220,220));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,25,25);
                    g2.dispose();
                }
            };

            passwordField.setBounds(90,380,320,60);
            passwordField.setFont(geologicaBold.deriveFont(20f));
            passwordField.setOpaque(false);
            passwordField.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

            // Login button
            JButton loginButton=new JButton("LOGIN"){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

                    // Button background
                    g2.setColor(new Color(78,149,242));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
                    FontMetrics fm=g2.getFontMetrics();
                    Rectangle stringBounds=fm.getStringBounds(getText(),g2).getBounds();

                    int textX=(getWidth()-stringBounds.width)/2;
                    int textY=(getHeight()-stringBounds.height)/2+fm.getAscent();

                    // Shadow
                    g2.setColor(new Color(120,120,120,130));
                    g2.drawString(getText(),textX+1,textY+1);

                    // Main text
                    g2.setColor(Color.BLACK);
                    g2.drawString(getText(),textX,textY);
                    g2.dispose();
                }

                @Override
                protected void paintBorder(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(78,149,242));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,25,25);
                    g2.dispose();
                }
            };

            loginButton.setBounds(90,500,140,50);
            loginButton.setFont(geologicaBold.deriveFont(18f));
            loginButton.setFocusPainted(false);
            loginButton.setContentAreaFilled(false);
            loginButton.setBorderPainted(false);
            loginButton.setOpaque(false);
            loginButton.setRolloverEnabled(false);

            // Login event
            loginButton.addActionListener(e-> login());

            // Employee ID Enter
            employeeField.addActionListener(e -> {
                        if (employeeField.getText().trim().isEmpty()) {
                            employeeField.requestFocusInWindow();
                        } else {
                            passwordField.requestFocusInWindow();
                        }
                    });

            // Password Enter
            passwordField.addActionListener(e -> {

                        if (employeeField.getText().trim().isEmpty()) {
                            employeeField.requestFocusInWindow();

                        } else if (String.valueOf(passwordField.getPassword()).trim().isEmpty()) {
                            passwordField.requestFocusInWindow();

                        } else {
                            login();
                        }
                    });

            // Cancel button
            JButton cancelButton=new JButton("CANCEL"){
                @Override
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

                    // Button background
                    g2.setColor(new Color(227,111,96));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
                    FontMetrics fm=g2.getFontMetrics();
                    Rectangle stringBounds=fm.getStringBounds(getText(),g2).getBounds();

                    int textX=(getWidth()-stringBounds.width)/2;
                    int textY=(getHeight()-stringBounds.height)/2+fm.getAscent();

                    // Shadow
                    g2.setColor(new Color(120,120,120,130));
                    g2.drawString(getText(),textX+1,textY+1);

                    // Main text
                    g2.setColor(Color.BLACK);
                    g2.drawString(getText(),textX,textY);
                    g2.dispose();
                }

                @Override
                protected void paintBorder(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(227,111,96));
                    g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,25,25);
                    g2.dispose();
                }
            };

            cancelButton.setBounds(270,500,140,50);
            cancelButton.setFont(geologicaBold.deriveFont(18f));
            cancelButton.setFocusPainted(false);
            cancelButton.setContentAreaFilled(false);
            cancelButton.setBorderPainted(false);
            cancelButton.setOpaque(false);
            cancelButton.setRolloverEnabled(false);

            // Exit event
            cancelButton.addActionListener(e->System.exit(0));

            // Add components
            rightPanel.add(logoLabel);
            rightPanel.add(companyLabel);
            rightPanel.add(titleLabel);
            rightPanel.add(userLabel);
            rightPanel.add(employeeField);
            rightPanel.add(passLabel);
            rightPanel.add(passwordField);
            rightPanel.add(loginButton);
            rightPanel.add(cancelButton);

            // Add panels
            background.add(rightPanel);
            add(background);
            setVisible(true);

        }catch(Exception e){
            // Exception handling
            JOptionPane.showMessageDialog(this,"Error loading login form.");
            System.out.println("Error: "+e.getMessage());
        }
    }

    // Login method
    private void login(){
        try{
            // Get input
            String employeeNumber=employeeField.getText().trim();
            String password=String.valueOf(passwordField.getPassword());

            // Empty validation
            if(employeeNumber.isEmpty()||password.isEmpty()){
                JOptionPane.showMessageDialog(this,"Please enter Employee ID and Password.");
                employeeField.setText("");
                passwordField.setText("");
                return;
            }

            // Check if employee number contains letters
            if(!employeeNumber.matches("\\d+")){
                JOptionPane.showMessageDialog(this,"Employee ID must contain numbers only.");
                employeeField.setText("");
                passwordField.setText("");
                return;
            }

            // Find employee
            Employee employee=manager.findEmployeeByNumber(employeeNumber);

            // Employee does not exist
            if(employee==null){
                JOptionPane.showMessageDialog(this,"Employee number doesn't exist.");
                employeeField.setText("");
                passwordField.setText("");
                return;
            }

            // Authenticate user
            UserAuthentication auth=new UserAuthentication(manager);
            Employee validEmployee=auth.validateLogin(employeeNumber,password);

            // Invalid password
            if(validEmployee==null){
                JOptionPane.showMessageDialog(this,"Invalid password.");
                passwordField.setText("");
                return;
            }

            // Get position
            String position=employee.getPosition().trim().toLowerCase();
            System.out.println(position);

            new LoginSuccessDialog(this).setVisible(true);

            String rolePrefix = "";

            if (position.contains("hr")) {

                rolePrefix = "HR";

            } else if (position.contains("payroll")) {

                rolePrefix = "Payroll";

            }

            new Dashboard(
                    employee,
                    rolePrefix
                    );

            // Close login
            dispose();

        }catch(Exception e){
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    e.toString()
                    );

            employeeField.setText("");
            passwordField.setText("");
        }
    }
}
