package hibernateTask1;

import java.util.Date;
import java.util.List;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;

public class GraphicInterface extends Application {
	
    Label usernameLab, passwordLab, nameLab, surnameAndCreditsLab, infoLab, studentLab, profIdLab;
    TextField username, name, surnameAndCredits, profId;
    PasswordField password;
    TextArea info, comment, addInfo;
    Button loginBtn, logoutBtn, commentBtn, deleteBtn, updateBtn, addBtn, editBtn, deleteBtnProfSubj;
    ChoiceBox<String> choosePS; //PS -> ProfessorSubject
    ChoiceBox<Degree> chooseDegree;
    HBox loginBox, logoutBox, commentBox, adminButtonsBox, FieldsAdminBox, chooseBox;
    VBox leftBox, rightBox, nameSurnameBox, InfoVBox, profIdBox;
    Group root;
    ManagerEM manager;
    ObservableList<Degree> degreeList;
    Student student;
    CommentTable comments;
    ProfSubjectTable table;	
    CacheManager cache;
  
  @Override
    public void start(Stage primaryStage) {
    	
    	manager = new ManagerEM();
    	manager.setup();
    	table = new ProfSubjectTable(this);
    	comments = new CommentTable(this);
    	 
        //login box on the top
    	usernameLab = new Label("Username:");
        username = new TextField();
        username.setPromptText("Your Username");
        passwordLab = new Label("Password:");
        password = new PasswordField();
        password.setPromptText("Your Password");
        loginBtn = new Button("Login");
        
        loginBox = new HBox(10);
        loginBox.getChildren().addAll(usernameLab,username,passwordLab,password,loginBtn);
       
        //logout box on the top
        studentLab = new Label("");
        logoutBtn = new Button("Logout");
        
        logoutBox = new HBox(10);
        logoutBox.getChildren().addAll(studentLab, logoutBtn);
        logoutBox.setVisible(false); //user not logged, hide logoutbox
        
        //box on the left
        info = new TextArea("");
        info.setPromptText("Informations about Professors are visualized here");
        info.setWrapText(true);
        info.setEditable(false);
        
        leftBox = new VBox(10);
        leftBox.getChildren().addAll(info,comments);
          
        //info Vbox creation for the admin
        infoLab = new Label("Professor Informations:");
        addInfo = new TextArea("");
        addInfo.setWrapText(true);
        
        InfoVBox = new VBox(10);
        InfoVBox.getChildren().addAll(infoLab,addInfo);
        
        //name and surname/credits about professor/subject VBox for the admin
        nameLab = new Label("Name:");
        name = new TextField();
        surnameAndCreditsLab = new Label("Surame:");
        surnameAndCredits = new TextField();       
        
        nameSurnameBox = new VBox(10);
        nameSurnameBox.getChildren().addAll(nameLab,name, surnameAndCreditsLab, surnameAndCredits ); 
        
        //id prof vbox for admin
        profIdLab = new Label("Professor Id:");
        profId = new TextField();
        
        profIdBox = new VBox(10);
        profIdBox.getChildren().addAll(profIdLab, profId);
        
        //Hbox with the 2 vbox created before and the add button.
        addBtn = new Button("Add");
        FieldsAdminBox = new HBox(10);
        FieldsAdminBox.getChildren().addAll(nameSurnameBox, InfoVBox, addBtn);
        FieldsAdminBox.setVisible(false);
        
        //box on the right
        choosePS = new ChoiceBox<>(); //Professor/subject choice box
        choosePS.getItems().add("Professors");
        choosePS.getItems().add("Subjects");
        choosePS.getSelectionModel().selectFirst();
        
        chooseDegree = new ChoiceBox<>(); //degree choice box
        degreeList = FXCollections.observableArrayList();
        degreeList.addAll(manager.getDegreeCourses());
        chooseDegree.setItems(degreeList);
        chooseDegree.getItems().add(0, new Degree(-1,"All"));
        chooseDegree.getSelectionModel().selectFirst();
        
        table.setProfessorsList(manager.getProfessors(-1));
       
        // add event to chooseProfSubject changing selection value
        choosePS.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                
                int degreeId = chooseDegree.getValue().getId();
                if("Subjects".equals(choosePS.getItems().get((Integer) newValue))) {
                	comments.setProfessorComments(-1);
                	table.setSubjectsList(manager.getSubjects(degreeId));
                }
                else if("Professors".equals(choosePS.getItems().get((Integer) newValue))) {
                	comments.setSubjectComments(-1);
                	table.setProfessorsList(manager.getProfessors(degreeId));
                }
                info.setText("Informations about " + choosePS.getItems().get((Integer) newValue) +" are visualized here");  
            }
        });
       
        // add event to chooseDegree changing selection value 
        chooseDegree.getSelectionModel().selectedIndexProperty().addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {      	
            	  
            	if( "All".equals(chooseDegree.getItems().get((int) newValue).getName())){
                    setSubjProfList(-1);
            	} else {
                    int degreeId = chooseDegree.getItems().get((int) newValue).getId();
                    setSubjProfList(degreeId);
            	}
            }
        });     
        
        //admin buttons
    	editBtn = new Button("Edit");
    	deleteBtnProfSubj = new Button("Delete");
    	
    	adminButtonsBox = new HBox(10);
        adminButtonsBox.getChildren().addAll(editBtn, deleteBtnProfSubj);
        adminButtonsBox.setVisible(false);
        
        loginBtn.setOnAction((ActionEvent e) -> loginAction());       
        logoutBtn.setOnAction((ActionEvent e) -> logoutAction());
        
        table.lookup(".scroll-bar:vertical"); 
        
        chooseBox = new HBox(10);
        chooseBox.getChildren().addAll(choosePS, chooseDegree);
        
        rightBox = new VBox(10);
        rightBox.getChildren().addAll(chooseBox,table);
        
        //box on the bottom
        comment = new TextArea("");
        comment.setPromptText("Leave a comment here...");
        commentBtn = new Button("Comment");
        deleteBtn = new Button("Delete");
        updateBtn = new Button("Update");
        
        commentBox = new HBox(10);
        commentBox.getChildren().addAll(comment,commentBtn, deleteBtn, updateBtn);
        
        root = new Group();
        root.getChildren().addAll(loginBox, logoutBox, leftBox, rightBox, adminButtonsBox, commentBox, FieldsAdminBox);
        
        Scene scene = new Scene(root, 1210, 700);
        primaryStage.setTitle("Students Evaluations");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        setPositions();
        setSizes();
        setCssStyle();
        
        //choice box selection 		
        table.setOnMouseClicked((MouseEvent ev)-> profSubjectSelectionButton());
             
        //add comment 
        commentBtn.setOnAction((ActionEvent ev)-> addCommentButton());
        
        //select comment
        comments.setOnMouseClicked((MouseEvent ev)-> commentSelectionButton());
        
        //update comment
         updateBtn.setOnAction((ActionEvent ev)->updateCommentButton());
        
        //delete comment
        deleteBtn.setOnAction((ActionEvent ev)-> deleteCommentButton());
        
        //Add professor/course button for admin
        addBtn.setOnAction((ActionEvent ev)-> addProfSubjectButton()); 
        
        deleteBtnProfSubj.setOnAction((ActionEvent e) -> adminDeleteAction());
        
        editBtn.setOnAction((ActionEvent e) -> adminEditAction());
    }
   
    // STYLE
    private void setCssStyle(){
        root.setStyle("-fx-color:orange; -fx-font-size: 14; -fx-font-family: Arial");
        studentLab.setStyle("-fx-font-size: 20");
    }
    
    // STYLE
    private void setSizes(){
        logoutBtn.setMinWidth(80);//logout button
        loginBtn.setMinWidth(80);//login button
        comment.setMaxHeight(50); 

        //right box
        choosePS.setMinSize(150,30);
        chooseDegree.setMinSize(350,30); chooseDegree.setMaxWidth(350);
        table.setMinHeight(500);
        rightBox.setMinWidth(500);

        //Left Box
        leftBox.setMaxSize(540, 530);
        comments.setMaxSize(540, 430); comments.setMinHeight(430);	//tabella dei commenti
        info.setMaxSize(540,100); info.setMinSize(540, 100);		 

        //admin fields
        FieldsAdminBox.setMinSize(540, 100); FieldsAdminBox.setMaxWidth(640);
        addInfo.setMaxSize(285, 90);

        //admin buttons
        addBtn.setMinSize(60, 120);
        editBtn.setMinWidth(80);
        deleteBtnProfSubj.setMinWidth(80);
    }
        
    // STYLE
    private void setPositions(){
        loginBox.setLayoutX(10); loginBox.setLayoutY(10);
        logoutBox.setLayoutX(450); logoutBox.setLayoutY(10);
        studentLab.setLayoutY(20);
        leftBox.setLayoutX(20); leftBox.setLayoutY(70);
        FieldsAdminBox.setLayoutX(20); FieldsAdminBox.setLayoutY(515);
        rightBox.setLayoutX(leftBox.getWidth()+100); rightBox.setLayoutY(70);
        commentBox.setLayoutX(20); commentBox.setLayoutY(640);  
        adminButtonsBox.setLayoutX(900); adminButtonsBox.setLayoutY(530);
    }
    
    // STYLE updates tables size based on the actual user (admin or not)
  	private void updateSizes(){
  		if( student != null && student.getAdmin()) {
                    table.setMinHeight(400);
                    comments.setMaxSize(540, 330); comments.setMinHeight(330);
  		} else {
                    table.setMinHeight(500);
                    comments.setMaxSize(540, 430); comments.setMinHeight(430);	
  		}
  	}
    
    // function for click on loginButton
    private void loginAction() {
    	student = manager.checkUser(username.getText(), password.getText());
    	
    	if(student != null) {
    		cache = new CacheManager(student.getId(), manager, student.getDeg().getId());
            loginBox.setVisible(false);
            logoutBox.setVisible(true);
            studentLab.setText("User: " + student.getUsername());
            this.setSubjProfList(student.getDeg().getId());    
            cache.initialize(student.getDeg().getId());
            //update the degree course choice box based on the student degree course.
            //chooseDegree.setValue(student.getDeg());
            List <Degree> degree = chooseDegree.getItems();            
            int i = manager.newDegreeIndex(student, degree);        
            chooseDegree.setValue(degreeList.get(i));
            
            //show/hide admin buttons 
            adminButtonsBox.setVisible(student.getAdmin());
            
            //updates STYLE sizes based on the logged user (admin or not)
            updateSizes();
    	} else {
            username.setText("");
            password.setText("");
            System.err.println("Invalid username or password, please retry!");
    	}
    }
       
    private void logoutAction(){
    	chooseDegree.getSelectionModel().selectFirst();
    	logoutBox.setVisible(false);
    	loginBox.setVisible(true);
    	adminButtonsBox.setVisible(false); 
    	FieldsAdminBox.setVisible(false);
    	student = null;
        setSubjProfList(-1);
    	updateSizes();
    	cache.close();
        this.username.setText("");
    	this.password.setText("");
    }
  
    private void setSubjProfList(int degree){
        if("Subjects".equals((String)choosePS.getValue())){
        	if(student != null && student.getDeg().getId() == degree)
        		table.setSubjectsList(cache.retrieveSubjects());
        	else
        		table.setSubjectsList(manager.getSubjects(degree));
    	} else {
    		if(student != null && student.getDeg().getId() == degree)
    			table.setProfessorsList(cache.retrieveProfessors());
    		else
    			table.setProfessorsList(manager.getProfessors(degree));
    	}
    }
    
    private void profSubjectSelectionButton() {
    	String type = (String) choosePS.getValue();
        Object o = table.getSelectionModel().getSelectedItem();
        if(o != null){
            if("Professors".equals(type)){
                Professor p = (Professor)o;
                info.setText(p.getInfo());
                if(student.getDeg().getId() == chooseDegree.getValue().getId() && !student.getAdmin()) {
                	comments.setProfessorCommentsCache(p.getId());
                }else {
                	comments.setProfessorComments(p.getId());
                }
                if( student != null && student.getAdmin()){ // if admin is operating
                    name.setText(p.getName());
                    surnameAndCredits.setText(p.getSurname());
                    addInfo.setText(p.getInfo());
                }
            }else{
                Subject s = (Subject)o;
                info.setText(s.getInfo());
                if(student.getDeg().getId() == chooseDegree.getValue().getId() && !student.getAdmin()) {
                	comments.setSubjectCommentsCache(s.getId());
                }else {
                	comments.setSubjectComments(s.getId());
                }
                
                if( student != null && student.getAdmin()) { // if admin is operating
                    name.setText(s.getName());
                    surnameAndCredits.setText(Integer.toString(s.getCredits()));
                    addInfo.setText(s.getInfo());
                }
            }
        }
    }
    
    private void commentSelectionButton(){
        Comment o = comments.getSelectionModel().getSelectedItem();
        if(o != null){
            comment.setText(o.getText());
        }
    }
    
    private void addCommentButton() {
    	if(student != null) {
            if("Subjects".equals((String)choosePS.getValue())){
            	
                Subject s = (Subject) table.getSelectionModel().getSelectedItem();
                if(student.getDeg().getId() == chooseDegree.getValue().getId()) {
                	cache.insertSubjectComment(comment.getText(), new Date(), student, s.getId());
                }else {
                	manager.createSubjectComment(comment.getText(), new Date(), student, s.getId());
                }
                comments.setSubjectComments(s.getId());
            }else{
                Professor p = (Professor) table.getSelectionModel().getSelectedItem();
                if(student.getDeg().getId() == chooseDegree.getValue().getId()) {
                	cache.insertProfessorComment(comment.getText(), new Date(), student, p.getId());
                }else {
                	manager.createProfessorComment(comment.getText(), new Date(), student, p.getId());
                }
                comments.setProfessorComments(p.getId());
            }
            this.comment.setText("");
        }else 
            System.err.println("You have to login!\n");
    }
    
    private void updateCommentButton() {
    	if(student != null) {
            if("Subjects".equals((String)choosePS.getValue())){
                Subject s = (Subject) table.getSelectionModel().getSelectedItem();
                SubjectComment sc = (SubjectComment) comments.getSelectionModel().getSelectedItem();
                if(s == null || sc == null) {
                    System.err.println("Select a subject and a comment in order to update it");
                    return;
                }
                if(student.getDeg().getId() == chooseDegree.getValue().getId()|| student.getAdmin()) {
                	if(!manager.subCommentBelongs(sc.getId(), student.getId())&& !student.getAdmin()) {
                		System.err.println("You are not the owner of that comment, please select another comment");
                	}
                	cache.updateCommentSubject(sc.getId(), comment.getText(), student.getId(), s.getId());
                	comments.setSubjectCommentsCache(s.getId());
                }else {
                	if(manager.updateCommentSubject(sc.getId(), comment.getText(), student.getId()))
                        comments.setSubjectComments(s.getId());
                }
                
            }else{
                Professor p = (Professor) table.getSelectionModel().getSelectedItem();
                ProfessorComment pc = (ProfessorComment) comments.getSelectionModel().getSelectedItem();
                if(p == null || pc == null) {
                    System.err.println("Select a professor and a comment in order to update it");
                    return;
                }
                if(student.getDeg().getId() == chooseDegree.getValue().getId()|| student.getAdmin()) {
                	if(!manager.profCommentBelongs(pc.getId(), student.getId()) && !student.getAdmin()) {
                		System.err.println("You are not the owner of that comment, please select another comment");
                	}
                	cache.updateCommentProf(pc.getId(), comment.getText(), student.getId(), p.getId());
                	comments.setProfessorCommentsCache(p.getId());
                }else {
                	if(manager.updateCommentProf(pc.getId(), comment.getText(), student.getId()))
                        comments.setProfessorComments(p.getId());
                }
                
                
            }
        }else 
            System.err.println("You have to login!\n");
    }
    
    private void deleteCommentButton() {
    	if(student != null) {
            if("Subjects".equals((String)choosePS.getValue())){
                Subject s = (Subject) table.getSelectionModel().getSelectedItem();
                SubjectComment sc = (SubjectComment) comments.getSelectionModel().getSelectedItem();
                if(student.getDeg().getId() == chooseDegree.getValue().getId()|| student.getAdmin()) {
                	if(!manager.subCommentBelongs(sc.getId(), student.getId())&& !student.getAdmin()) {
                		System.err.println("You are not the owner of that comment, please select another comment");
                	}
                	cache.deleteCommentSubject(sc.getId(), student.getId(), s.getId(), student.getAdmin());
                	comments.setSubjectCommentsCache(s.getId());
                }else {
                	if(manager.deleteCommentSubject(sc.getId(),student.getId(), student.getAdmin()))
                        comments.setSubjectComments(s.getId());
                }
                
            }else{
                Professor p = (Professor) table.getSelectionModel().getSelectedItem();
                ProfessorComment c = (ProfessorComment) comments.getSelectionModel().getSelectedItem();
                if(student.getDeg().getId() == chooseDegree.getValue().getId() || student.getAdmin()) {
                	if(!manager.profCommentBelongs(c.getId(), student.getId())&& !student.getAdmin()) {
                		System.err.println("You are not the owner of that comment, please select another comment");
                	}
                	cache.deleteCommentProf(c.getId(), c.getStud().getId(), p.getId(), student.getAdmin());
                	comments.setProfessorCommentsCache(p.getId());
                }else {
                	if(manager.deleteCommentProf(c.getId(), student.getId(), student.getAdmin()))
                        comments.setProfessorComments(p.getId());
                }
                
            }
        }else 
            System.err.println("You have to login!\n");
    }
  
    private void adminEditAction() {
    	
    	if(student != null && student.getAdmin()) {
            if(name.getText().isBlank() || surnameAndCredits.getText().isBlank()) {
                System.err.println("Insert name and surname(for professor) or credits(for subject)");
                return;
            }
            int degreeId = chooseDegree.getValue().getId();
            String type = (String) choosePS.getValue();
            if("Professors".equals(type)) {
                Professor professor = (Professor) table.getSelectionModel().getSelectedItem();
                if(professor == null) {
                    System.err.println("Select a professor");
                    return;
                }
                manager.editProfessor(professor.getId(), name.getText(), surnameAndCredits.getText(),addInfo.getText());
                info.setText(addInfo.getText());
                name.clear();
                surnameAndCredits.clear();
                addInfo.clear();
                setSubjProfList(degreeId);
            }else{
                Subject subject = (Subject) table.getSelectionModel().getSelectedItem();
                if(subject == null) {
                    System.err.println("Select a subject");
                    return;
                }
                int professorId = 0;
                int credits = 0;
                try{
                    if(!surnameAndCredits.getText().isBlank())
                        credits = Integer.valueOf(surnameAndCredits.getText());
                } catch(NumberFormatException e) {
                    System.err.println("Invalid input in numeric fields");
                    return;
                }
                if(manager.editSubject(subject.getId() ,name.getText(), credits, addInfo.getText(), profId.getText())) {
                    info.setText(addInfo.getText());
                    name.clear();
                    surnameAndCredits.clear();
                    addInfo.clear();
                    profId.clear();
                    setSubjProfList(degreeId);
                }
            }
        }else{
            System.err.println("You have to login as an admin!\n");
    	}
    }
    
    private void adminDeleteAction() {
    	if(student != null) {
            String type = (String) choosePS.getValue();
            if("Professors".equals(type)){
                Professor professor = (Professor) table.getSelectionModel().getSelectedItem();
                if(professor == null) {
                    System.err.println("Select a professor");
                    return;
                }
                manager.deleteProfessor(professor.getId());
                info.clear();
                name.clear();
                surnameAndCredits.clear();
                addInfo.clear();
                comments.setProfessorComments(-1);
                setSubjProfList(chooseDegree.getValue().getId());
            }else {
                Subject subject = (Subject) table.getSelectionModel().getSelectedItem();
                if(subject == null) {
                    System.err.println("Select a subject");
                    return;
                }
                manager.deleteSubject(subject.getId());
                info.clear();
                name.clear();
                surnameAndCredits.clear();
                addInfo.clear();
                comments.setSubjectComments(-1);
                setSubjProfList(chooseDegree.getValue().getId());
            }
        }else{
            System.err.println("You have to login as an admin!\n");
    	}
    }
    
    private void addProfSubjectButton() {
    	int degreeId;
    	
        if("Subjects".equals((String)choosePS.getValue())){
            if(name.getText().isBlank() || surnameAndCredits.getText().isBlank() || profId.getText().isBlank()) {
                System.err.println("Insert name, credits, info(optional) and the id of a professor for the new subject.");
                return;
            }
            if(!"All".equals(chooseDegree.getValue().getName())) {
                degreeId = chooseDegree.getValue().getId();
                try {
                    int credits = Integer.parseInt(surnameAndCredits.getText());
                    if(credits > 0 && credits < 50 )
                        manager.createSubject(name.getText(), credits, addInfo.getText(), profId.getText(), degreeId);
                    else {
                        System.err.println("invalid credits value, insert a number between 1 and 50");
                        return;
                    }
                }catch(NumberFormatException e) {
                    System.err.println("Please, insert a numeric value in \"Credits\" and \"Professor Id\" fields");
                    return;
                }
                name.setText("");
                surnameAndCredits.setText("");
                addInfo.setText("");	
                profId.setText("");
                setSubjProfList(degreeId);
            }else 
                System.err.println("Select a Degree program");
        }else{
            if(name.getText().isBlank() || surnameAndCredits.getText().isBlank()) {
                System.err.println("Insert name, surname and info(optional) for the new professor.");
                return;
            }
            manager.createProfessor(name.getText(),surnameAndCredits.getText(),addInfo.getText());
            name.setText("");
            surnameAndCredits.setText("");
            addInfo.setText(""); 
            if("All".equals(chooseDegree.getValue().getName()))
                setSubjProfList(chooseDegree.getValue().getId());
        }
    }
   
    // MAIN
    public static void main(String[] args) {
        launch(args);
    }
}