package com.mycompany.sathasapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {
    public ArrayList<String> semesters = new ArrayList<>();
        
     public void setal() throws SQLException {
        semesters.clear();
        String query = "SELECT s FROM sems";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String semesterName = resultSet.getString("s");
                semesters.add(semesterName);
            }
        }
    }

    @Override
   public void start(Stage stage) throws SQLException {
        stage.setTitle("COLLEGE MANAGEMENT");
        setal();
        Scene scene = s1(stage);
        stage.setScene(scene);
        stage.show();
    }
    private Button bb(Scene ss, String s) {
        Button B = new Button(s);
        
        EventHandler<MouseEvent> e1 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ss.setCursor(Cursor.HAND);
            }
        };
        
        EventHandler<MouseEvent> e2 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ss.setCursor(Cursor.DEFAULT);
            }
        };
        
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e1);
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e2);
        
        return B;
    }

    private Button bac(Scene sc, Stage s) {
        Button bac = bb(sc, "<- BACK");
        
        EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                s.setScene(sc);
            }
        };
        
        bac.addEventHandler(ActionEvent.ACTION, e);
        
        return bac;
    }
    private Scene s1(Stage s) {
        GridPane g = new GridPane();
        g.setHgap(10);

        Scene ss = new Scene(g, 700, 500);
        Button ac = bb(ss, "TEACHER");
        Button vc = bb(ss, "STUDENT");
        ac.setMinSize(200, 200);
        vc.setMinSize(200, 200);
        
        EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String x = ((Button) t.getSource()).getText();
                if (x.equals("TEACHER")) {
                    s.setScene(tealn(s));
                } else {
                    s.setScene(sln(s));
                }
            }
        };

        ac.addEventHandler(ActionEvent.ACTION, e);
        vc.addEventHandler(ActionEvent.ACTION, e);

        g.setAlignment(Pos.CENTER);
        g.add(vc, 0, 0);
        g.add(ac, 1, 0);
        return ss;
    }
    private Scene sln(Stage s) {
    BorderPane bp = new BorderPane();
    Scene ss = new Scene(bp, 700, 500);
    GridPane g = new GridPane();
    g.setAlignment(Pos.CENTER);
    Button bac = bac(s1(s), s);
    bp.setTop(g);
    Label tx = new Label("WELCOME STUDENT");
    tx.setFont(Font.font("Arial", 24));
    g.add(tx, 2, 0);
    VBox v=new VBox();
    v.getChildren().add(bac);
    bp.setLeft(v);
    GridPane gp = new GridPane();
    bp.setCenter(gp);
    gp.setAlignment(Pos.CENTER);
    gp.setHgap(20);
    gp.setVgap(10);

    Label ln = new Label("ENTER REG NO ");
    TextField tn = new TextField();
    gp.add(ln, 0, 0);
    gp.add(tn, 0, 1);

    Label lp = new Label("ENTER PASSWORD ");
    PasswordField tp = new PasswordField();
    gp.add(lp, 1, 0);
    gp.add(tp, 1, 1);

    Button sub = bb(ss, "SUBMIT");

    EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            String us = tn.getText();
            String pass = tp.getText();
            try {
                
                Connection co = DatabaseConnection.getConnection();

                PreparedStatement pst = co.prepareStatement("SELECT * FROM suse WHERE reg = ? AND pass = ?");
                pst.setString(1, us);
                pst.setString(2, pass);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    s.setScene(sdb(s,us));
                } 
                else {
                    Text err = new Text();
                    gp.add(err, 0, 4);
                    err.setFill(Color.RED);
                    err.setText("INVALID REG NO AND PASSWORD");
                }

                rs.close();
                pst.close();
                co.close();
            } catch (SQLException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    sub.addEventHandler(ActionEvent.ACTION, e);
    gp.add(sub, 0, 3);

    return ss;
}

    private Scene tealn(Stage s){
    BorderPane bp = new BorderPane();
    Scene ss = new Scene(bp, 700, 500);
    GridPane g = new GridPane();
    g.setAlignment(Pos.CENTER);
    Button bac = bac(s1(s), s);
    bp.setTop(g);
    Label tx = new Label("WELCOME TEACHER");
    tx.setFont(Font.font("Arial", 24));
    g.add(tx, 2, 0);
    VBox v=new VBox();
    v.getChildren().add(bac);
    bp.setLeft(v);
    GridPane gp = new GridPane();
    bp.setCenter(gp);
    gp.setAlignment(Pos.CENTER);
    gp.setHgap(20);
    gp.setVgap(10);

    Label ln = new Label("ENTER NAME ");
    TextField tn = new TextField();
    gp.add(ln, 0, 0);
    gp.add(tn, 0, 1);

    Label lp = new Label("ENTER PASSWORD ");
    PasswordField tp = new PasswordField();
    gp.add(lp, 1, 0);
    gp.add(tp, 1, 1);

    Button sub = bb(ss, "SUBMIT");

    EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            String us = tn.getText();
            String pass = tp.getText();
            try {
                
                Connection co = DatabaseConnection.getConnection();

                PreparedStatement pst = co.prepareStatement("SELECT * FROM tuse WHERE un = ? AND pass = ?");
                pst.setString(1, us);
                pst.setString(2, pass);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    s.setScene(tdb(s));
                } 
                else {
                    Text err = new Text();
                    gp.add(err, 0, 4);
                    err.setFill(Color.RED);
                    err.setText("INVALID USERNAME AND PASSWORD");
                }

                rs.close();
                pst.close();
                co.close();
            } catch (SQLException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    sub.addEventHandler(ActionEvent.ACTION, e);
    gp.add(sub, 0, 3);

    return ss;
    }

   private Scene sdb(Stage s, String us) throws SQLException {
        BorderPane bp = new BorderPane();
        Scene ss = new Scene(bp, 700, 500);
        GridPane g = new GridPane();
        g.setPadding(new Insets(10));
        g.setHgap(10);
        g.setVgap(10);
        g.setAlignment(Pos.CENTER);
        VBox v=new VBox();
        Button bac = bac(sln(s), s);
        v.getChildren().add(bac);
        bp.setLeft(v);
        Label tx = new Label("STUDENT DASHBOARD");
        tx.setFont(Font.font("Arial", 24));
        g.add(tx, 1, 0, 2, 1);
        bp.setTop(g);
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(20));
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);

        Connection connection = DatabaseConnection.getConnection();

        String query = "SELECT name, dept, mn, addr FROM suse WHERE reg = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, us);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Label nameLabelTitle = new Label("Name:");
                nameLabelTitle.setFont(Font.font("Arial", 16));
                Label nameLabel = new Label(resultSet.getString("name"));
                nameLabel.setFont(Font.font("Arial", 16));

                Label deptLabelTitle = new Label("Department:");
                deptLabelTitle.setFont(Font.font("Arial", 16));
                Label deptLabel = new Label(resultSet.getString("dept"));
                deptLabel.setFont(Font.font("Arial", 16));

                Label mnLabelTitle = new Label("Mobile Number:");
                mnLabelTitle.setFont(Font.font("Arial", 16));
                Label mnLabel = new Label(resultSet.getString("mn"));
                mnLabel.setFont(Font.font("Arial", 16));

                Label addrLabelTitle = new Label("Address:");
                addrLabelTitle.setFont(Font.font("Arial", 16));
                Label addrLabel = new Label(resultSet.getString("addr"));
                addrLabel.setFont(Font.font("Arial", 16));

                gp.add(nameLabelTitle, 0, 0);
                gp.add(nameLabel, 1, 0);
                gp.add(deptLabelTitle, 0, 1);
                gp.add(deptLabel, 1, 1);
                gp.add(mnLabelTitle, 0, 2);
                gp.add(mnLabel, 1, 2);
                gp.add(addrLabelTitle, 0, 3);
                gp.add(addrLabel, 1, 3);

                // Adding the "See Marks" button
                Button seeMarksButton = new Button("See Marks");
                seeMarksButton.setFont(Font.font("Arial", 16));
                seeMarksButton.setOnAction(e -> {
                    try {
                        s.setScene(mrk(s,us));
                    } catch (SQLException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                gp.add(seeMarksButton, 1, 4);
            } 
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

        return ss;
    }
   
private Scene mrk(Stage s, String us) throws SQLException {
    BorderPane bp = new BorderPane();
    Scene ss = new Scene(bp, 700, 500);
    GridPane g = new GridPane();
    g.setPadding(new Insets(10));
    g.setHgap(10);
    g.setVgap(10);
    g.setAlignment(Pos.CENTER);
    
    VBox v = new VBox();
    Button bac = bac(sdb(s, us), s);  // Assuming you have this method
    v.getChildren().add(bac);
    bp.setLeft(v);
    
    // Fetch user details
    String userName = "";
    String userDept = "";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT name, dept FROM suse WHERE reg = ?")) {
        stmt.setString(1, us);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            userName = rs.getString("name");
            userDept = rs.getString("dept");
        }
    }

    
    ChoiceBox<String> semesterChoiceBox = new ChoiceBox<>();
        setal(); // Retrieve semesters from database
    semesterChoiceBox.getItems().addAll(semesters);
    semesterChoiceBox.setValue(semesters.get(0));
    
    VBox userDetailsBox = new VBox();
    userDetailsBox.setAlignment(Pos.CENTER);
    userDetailsBox.setSpacing(10);
    Label nameLabel = new Label("Name: " + userName);
    nameLabel.setFont(Font.font("Arial", 18));
    Label regLabel = new Label("Reg No: " + us);
    regLabel.setFont(Font.font("Arial", 18));
    Label deptLabel = new Label("Department: " + userDept);
    deptLabel.setFont(Font.font("Arial", 18));
    userDetailsBox.getChildren().addAll(nameLabel, regLabel, deptLabel,semesterChoiceBox);
    bp.setTop(userDetailsBox);
    
    GridPane gp = new GridPane();
    gp.setPadding(new Insets(20));
    gp.setHgap(20);
    gp.setVgap(20);
    gp.setAlignment(Pos.CENTER);
    bp.setCenter(gp);

    // Add ChoiceBox for selecting semester
    
    
    // Event handler for ChoiceBox selection
    semesterChoiceBox.setOnAction(event -> {
        try {
            String selectedSemester = semesterChoiceBox.getValue();
            displayMarks(gp, us, selectedSemester);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
    
    // Display marks for the initial selection
    displayMarks(gp, us, semesterChoiceBox.getValue());
    
    return ss;
}

private void displayMarks(GridPane gp, String us, String semester) throws SQLException {
    gp.getChildren().clear();  // Clear previous content

    // Add headings
    Label subjectHeading = new Label("Subject");
    gp.setHgap(100);
    subjectHeading.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    gp.add(subjectHeading, 0, 0);

    Label gradeHeading = new Label("Grade");
    gradeHeading.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    gp.add(gradeHeading, 1, 0);

    String query = "SELECT * FROM " + semester + " WHERE reg = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, us);
        ResultSet rs = stmt.executeQuery();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        if (rs.next()) {
            for (int i = 2; i <= columnCount; i++) {  
                String columnName = rsmd.getColumnName(i);
                String columnValue = rs.getString(i);

                Label subjectLabel = new Label(columnName);
                subjectLabel.setFont(Font.font("Arial", 14));
                gp.add(subjectLabel, 0, i - 1); 

                Label gradeLabel = new Label(columnValue);
                gradeLabel.setFont(Font.font("Arial", 14));
                gp.add(gradeLabel, 1, i - 1);
            }
        }
    }
}
private Scene tdb(Stage s) throws SQLException
{
    BorderPane bp = new BorderPane();
        Scene ss = new Scene(bp, 700, 500);
        GridPane g = new GridPane();
        g.setPadding(new Insets(10));
        g.setHgap(10);
        g.setVgap(10);
        g.setAlignment(Pos.CENTER);
        VBox v=new VBox();
        Button bac = bac(tealn(s), s);
        v.getChildren().add(bac);
        bp.setLeft(v);
        Label tx = new Label("TEACHERS DASHBOARD");
        tx.setFont(Font.font("Arial", 24));
        g.add(tx, 1, 0, 2, 1);
        bp.setTop(g);
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(20));
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setAlignment(Pos.CENTER);
        bp.setCenter(gp);
        
        Button ac = bb(ss, "STUDENT DETIALS");
        Button vc = bb(ss, "MARKS");
        ac.setMinSize(200, 200);
        ac.setMaxSize(200, 200);
        vc.setMinSize(200, 200);
        
        EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String x = ((Button) t.getSource()).getText();
                if (x.equals("STUDENT DETIALS")) {
                    try {
                        s.setScene(esd(s));
                    } catch (SQLException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        s.setScene(em(s));
                    } catch (SQLException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        ac.addEventHandler(ActionEvent.ACTION, e);
        vc.addEventHandler(ActionEvent.ACTION, e);

        gp.setAlignment(Pos.CENTER);
        gp.add(vc, 0, 0);
        gp.add(ac, 1, 0);
        return ss;
        
}

private Scene esd(Stage s) throws SQLException {
    BorderPane bp = new BorderPane();
    Scene ss = new Scene(bp, 700, 500);
    GridPane g = new GridPane();
    g.setPadding(new Insets(10));
    g.setHgap(10);
    g.setVgap(10);
    g.setAlignment(Pos.CENTER);
    VBox v = new VBox();
    Button bac = bac(tdb(s), s);  // Assuming you have this method
    v.getChildren().add(bac);
    bp.setLeft(v);
    Label tx = new Label("STUDENT DETAILS");
    tx.setFont(Font.font("Arial", 24));
    g.add(tx, 0, 0, 2, 1);
    bp.setTop(g);

    FlowPane flowPane = new FlowPane();
    flowPane.setPadding(new Insets(10));
    flowPane.setHgap(10);
    flowPane.setVgap(10);
    EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String x = ((Button) t.getSource()).getText();
                try {
                    s.setScene(tsdb(s,x));
                } catch (SQLException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

    // Retrieve all registration numbers
    String query = "SELECT reg FROM suse";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String reg = rs.getString("reg");
            Button regButton = new Button(reg);
            regButton.setPrefSize(100, 100);
            regButton.addEventHandler(ActionEvent.ACTION, e);
            flowPane.getChildren().add(regButton);
        }
    }

    // Add "Add New Student" button
    Button addNewStudentButton = new Button("Add New \n Student");
    addNewStudentButton.setPrefSize(100, 100);
    addNewStudentButton.setOnAction(en -> {
        
        addNewStudent();
    });
    flowPane.getChildren().add(addNewStudentButton);

    bp.setCenter(flowPane);
    return ss;
}

private Scene tsdb(Stage s, String us) throws SQLException {
    BorderPane bp = new BorderPane();
    Scene ss = new Scene(bp, 700, 500);
    GridPane g = new GridPane();
    g.setPadding(new Insets(10));
    g.setHgap(10);
    g.setVgap(10);
    g.setAlignment(Pos.CENTER);
    VBox v = new VBox();
    Button bac = bac(esd(s), s);
    v.getChildren().add(bac);
    bp.setLeft(v);
    Label tx = new Label("STUDENT DETAILS");
    tx.setFont(Font.font("Arial", 24));
    g.add(tx, 1, 0, 2, 1);
    bp.setTop(g);
    GridPane gp = new GridPane();
    gp.setPadding(new Insets(20));
    gp.setHgap(20);
    gp.setVgap(20);
    gp.setAlignment(Pos.CENTER);
    bp.setCenter(gp);

    Connection connection = DatabaseConnection.getConnection();

    String query = "SELECT name, dept, mn, addr FROM suse WHERE reg = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, us);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Label nameLabelTitle = new Label("Name:");
            nameLabelTitle.setFont(Font.font("Arial", 16));
            TextField nameLabel = new TextField(resultSet.getString("name"));
            nameLabel.setFont(Font.font("Arial", 16));

            Label deptLabelTitle = new Label("Department:");
            deptLabelTitle.setFont(Font.font("Arial", 16));
            TextField deptLabel = new TextField(resultSet.getString("dept"));
            deptLabel.setFont(Font.font("Arial", 16));

            Label mnLabelTitle = new Label("Mobile Number:");
            mnLabelTitle.setFont(Font.font("Arial", 16));
            TextField mnLabel = new TextField(resultSet.getString("mn"));
            mnLabel.setFont(Font.font("Arial", 16));

            Label addrLabelTitle = new Label("Address:");
            addrLabelTitle.setFont(Font.font("Arial", 16));
            TextField addrLabel = new TextField(resultSet.getString("addr"));
            addrLabel.setFont(Font.font("Arial", 16));

            gp.add(nameLabelTitle, 0, 0);
            gp.add(nameLabel, 1, 0);
            gp.add(deptLabelTitle, 0, 1);
            gp.add(deptLabel, 1, 1);
            gp.add(mnLabelTitle, 0, 2);
            gp.add(mnLabel, 1, 2);
            gp.add(addrLabelTitle, 0, 3);
            gp.add(addrLabel, 1, 3);

            // Add the update button
            Button updateButton = new Button("Update");
            updateButton.setFont(Font.font("Arial", 16));
            gp.add(updateButton, 1, 4);

            // Action for the update button
            updateButton.setOnAction(e -> {
                String updatedName = nameLabel.getText();
                String updatedDept = deptLabel.getText();
                String updatedMn = mnLabel.getText();
                String updatedAddr = addrLabel.getText();

                String updateQuery = "UPDATE suse SET name = ?, dept = ?, mn = ?, addr = ? WHERE reg = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, updatedName);
                    updateStmt.setString(2, updatedDept);
                    updateStmt.setString(3, updatedMn);
                    updateStmt.setString(4, updatedAddr);
                    updateStmt.setString(5, us);

                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Update successful!");
                    } else {
                        System.out.println("Update failed!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        } 
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return ss;
}


    private void addNewStudent() {
        Stage addStudentStage = new Stage();
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10));
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setAlignment(Pos.CENTER);

        Label regLabel = new Label("Reg No:");
        TextField regField = new TextField();
        Label passLabel = new Label("Password:");
        TextField passField = new TextField();
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label deptLabel = new Label("Department:");
        TextField deptField = new TextField();
        Label mnLabel = new Label("Mobile No:");
        TextField mnField = new TextField();
        Label addrLabel = new Label("Address:");
        TextField addrField = new TextField();

        gp.add(regLabel, 0, 0);
        gp.add(regField, 1, 0);
        gp.add(passLabel, 0, 1);
        gp.add(passField, 1, 1);
        gp.add(nameLabel, 0, 2);
        gp.add(nameField, 1, 2);
        gp.add(deptLabel, 0, 3);
        gp.add(deptField, 1, 3);
        gp.add(mnLabel, 0, 4);
        gp.add(mnField, 1, 4);
        gp.add(addrLabel, 0, 5);
        gp.add(addrField, 1, 5);

        Button saveButton = new Button("Save");
        gp.add(saveButton, 1, 6);

        saveButton.setOnAction(e -> {
            String reg = regField.getText();
            String pass = passField.getText();
            String name = nameField.getText();
            String dept = deptField.getText();
            String mn = mnField.getText();
            String addr = addrField.getText();

            try {
                addStudentToDatabase(reg, pass, name, dept, mn, addr);
                addStudentToSemesters(reg);
                addStudentStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(gp, 400, 300);
        addStudentStage.setScene(scene);
        addStudentStage.setTitle("Add New Student");
        addStudentStage.show();
    }

    private void addStudentToDatabase(String reg, String pass, String name, String dept, String mn, String addr) throws SQLException {
        String insertQuery = "INSERT INTO suse (reg, pass, name, dept, mn, addr) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, reg);
            preparedStatement.setString(2, pass);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, dept);
            preparedStatement.setString(5, mn);
            preparedStatement.setString(6, addr);

            preparedStatement.executeUpdate();
        }
    }

    private void addStudentToSemesters(String reg) throws SQLException {
        
        setal();
        for (String semester : semesters) {
            addStudentToSemester(semester, reg);
        }
    }
    
    private void addStudentToSemester(String semester, String reg) throws SQLException {
        String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? AND column_name != 'reg'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, semester);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder columns = new StringBuilder("reg");
            StringBuilder values = new StringBuilder("?");

            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                columns.append(", ").append(columnName);
                values.append(", -1");
            }

            String insertQuery = "INSERT INTO " + semester + " (" + columns + ") VALUES (" + values + ")";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, reg);
                insertStatement.executeUpdate();
            }
        }
    }
    
private Scene em(Stage s) throws SQLException{
    BorderPane bp = new BorderPane();
        Scene ss = new Scene(bp, 700, 500);
        GridPane g = new GridPane();
        g.setPadding(new Insets(10));
        g.setHgap(10);
        g.setVgap(10);
        g.setAlignment(Pos.CENTER);
        VBox v = new VBox();
        VBox vv=new VBox();
        Button bac = bac(tdb(s), s);  // Assuming you have this method
        v.getChildren().add(bac);
        bp.setLeft(v);
        Label tx = new Label("EDITING MARKS");
        tx.setFont(Font.font("Arial", 24));
        vv.getChildren().add(tx);
        bp.setTop(g);
        g.add(vv, 0, 0, 2, 1);
        // ChoiceBox for selecting semester
        ChoiceBox<String> semesterChoiceBox = new ChoiceBox<>();
        setal(); // Populate semesters ArrayList
        semesterChoiceBox.getItems().addAll(semesters);
        semesterChoiceBox.setValue(semesters.get(0)); // Default to first semester

        // Button to add a new semester
        Button addSemesterButton = new Button("Add New Semester");
        addSemesterButton.setOnAction(event -> {
            TextInputDialog semesterDialog = new TextInputDialog();
    semesterDialog.setTitle("Add New Semester");
    semesterDialog.setHeaderText(null);
    semesterDialog.setContentText("Enter Semester Name:");
    Optional<String> result = semesterDialog.showAndWait();

    result.ifPresent(semesterName -> {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Step 1: Add semester name to `sems` table
            String insertSemesterQuery = "INSERT INTO sems (s) VALUES (?)";
            try (PreparedStatement insertSemesterStmt = connection.prepareStatement(insertSemesterQuery)) {
                insertSemesterStmt.setString(1, semesterName);
                insertSemesterStmt.executeUpdate();
            }

            // Prompt user for number of subjects
            TextInputDialog numSubjectsDialog = new TextInputDialog();
            numSubjectsDialog.setTitle("Number of Subjects");
            numSubjectsDialog.setHeaderText(null);
            numSubjectsDialog.setContentText("Enter Number of Subjects:");
            Optional<String> numSubjectsResult = numSubjectsDialog.showAndWait();

            numSubjectsResult.ifPresent(numSubjectsStr -> {
                int numSubjects = Integer.parseInt(numSubjectsStr);

                // Prompt user for subject names
                ArrayList<String> subjectNames = new ArrayList<>();
                for (int i = 1; i <= numSubjects; i++) {
                    TextInputDialog subjectDialog = new TextInputDialog();
                    subjectDialog.setTitle("Subject " + i + " Name");
                    subjectDialog.setHeaderText(null);
                    subjectDialog.setContentText("Enter Name for Subject " + i + ":");
                    Optional<String> subjectResult = subjectDialog.showAndWait();

                    subjectResult.ifPresent(subjectName -> {
                        subjectNames.add(subjectName);
                    });
                }

                // Step 3: Create new table for the semester if it doesn't exist
                String createTableQuery = "CREATE TABLE " + semesterName + " (reg varchar(20) primary key";
                for (String subjectName : subjectNames) {
                    createTableQuery += ", " + subjectName + " varchar(5) DEFAULT '-1'";
                }
                createTableQuery += ")";

                try (Statement createTableStmt = connection.createStatement()) {
                    createTableStmt.executeUpdate(createTableQuery);
                } catch (SQLException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Step 4: Populate reg rows in the new table with initial grade of -1
                String populateQuery = "INSERT INTO " + semesterName + " (reg) SELECT reg FROM suse";
                try (Statement populateStmt = connection.createStatement()) {
                    populateStmt.executeUpdate(populateQuery);
                } catch (SQLException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Inform user about successful addition of semester
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Semester Added");
                alert.setHeaderText(null);
                alert.setContentText("New semester '" + semesterName + "' added successfully!");
                alert.showAndWait();
            });
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while adding semester: " + e.getMessage());
            alert.showAndWait();
        }
    });
        });

        // GridPane for layout
        GridPane editGrid = new GridPane();
        editGrid.setPadding(new Insets(20));
        editGrid.setHgap(10);
        editGrid.setVgap(10);

        
        vv.getChildren().add(new Label("Select Semester:"));
        vv.getChildren().add(semesterChoiceBox);
        vv.getChildren().add(addSemesterButton);

        bp.setCenter(editGrid);

        // Event handler for semesterChoiceBox selection
        semesterChoiceBox.setOnAction(event -> {
            try {
                updateEditingGrid(editGrid, semesterChoiceBox.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Initial display of marks editing grid for the default semester
        updateEditingGrid(editGrid, semesterChoiceBox.getValue());
        
        Button updateAllButton = new Button("Update All");
        updateAllButton.setOnAction(event -> {
        try {
            updateAllGrades(editGrid, semesterChoiceBox.getValue());
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        });
        vv.getChildren().add(updateAllButton);
        
        return ss;
    }

    // Method to update the editing grid with student marks for the selected semester
    private void updateEditingGrid(GridPane editGrid, String selectedSemester) throws SQLException {
        editGrid.getChildren().clear(); // Clear previous content

        // Query to retrieve student marks for the selected semester
        String query = "SELECT * FROM " + selectedSemester;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Get column names dynamically
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int numColumns = rsmd.getColumnCount();

            // Add headers for student marks
            for (int i = 1; i <= numColumns; i++) {
                String columnName = rsmd.getColumnName(i);
                editGrid.add(new Label(columnName), i - 1, 1);
            }

            // Add rows for each student
            int row = 2; // Start row after headers
            while (resultSet.next()) {
                String regNo = resultSet.getString("reg");
                editGrid.add(new Label(regNo), 0, row);

                for (int i = 2; i <= numColumns; i++) {
                    String columnValue = resultSet.getString(i);
                    TextField gradeField = new TextField(columnValue);
                    gradeField.setStyle("-fx-min-width: 30px; -fx-max-width: 30px; -fx-min-height: 30px; -fx-max-height: 30px;");
                    editGrid.add(gradeField, i - 1, row);
                     
                }

                row++;
            }
        }
    }
    
   

            // Iterate through rows (excluding headers)
 private void updateAllGrades(GridPane editGrid, String selectedSemester) throws SQLException {
    int numColumns = editGrid.getColumnCount();
    int rowIndex = 1; // Start row index after headers

    try (Connection connection = DatabaseConnection.getConnection()) {
        String updateQuery = "UPDATE " + selectedSemester + " SET ";
        

        // Iterate through rows (excluding headers)
        while (rowIndex < editGrid.getRowCount()-1) {
            boolean firstColumn = true;
            String regNo = ((Label) editGrid.getChildren().get(rowIndex * numColumns)).getText();
            StringBuilder setClause = new StringBuilder();

            // Iterate through columns (excluding reg column)
            for (int colIndex = 1; colIndex < numColumns; colIndex++) {
                TextField textField = (TextField) editGrid.getChildren().get(rowIndex * numColumns + colIndex);
                String columnName = ((Label) editGrid.getChildren().get(colIndex)).getText();
                String columnValue = textField.getText();

                if (!firstColumn) {
                    setClause.append(", ");
                }
                setClause.append(columnName).append(" = '").append(columnValue).append("'");

                firstColumn = false;
            }

            // Execute update query for the current row
            String fullUpdateQuery = updateQuery + setClause.toString() + " WHERE reg = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(fullUpdateQuery)) {
                preparedStatement.setString(1, regNo);
                preparedStatement.executeUpdate();
            }

            rowIndex++;
        }
    }
}


}

        


   


   

