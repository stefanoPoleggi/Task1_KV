package hibernateTask1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleStringProperty;

@Entity(name = "Users")
@Table(name = "users")
public class Student extends Person {

	private final boolean admin;
	@Column(name = "username", unique = true)
	private String username;
	private String password;

	// relation with Degree.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "degreeId")
	private Degree deg;

	// relation with SubjectComments.
	@OneToMany(mappedBy = "stud", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubjectComment> subjectComments = new ArrayList<SubjectComment>();

	// relation with ProfessorComments.
	@OneToMany(mappedBy = "stud", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProfessorComment> professorComments = new ArrayList<ProfessorComment>();

	public Student() {
		super();
		admin = false;
		username = "";
	}

	public Student(int i, String u, String p, boolean a) {
		super(i);
		this.admin = a;
		this.username = u;
		this.password = p;

	}

	public List<ProfessorComment> getProfessorComments() {
		return professorComments;
	}

	public void setProfessorComments(List<ProfessorComment> professorComments) {
		this.professorComments = professorComments;
	}

	public List<SubjectComment> getSubjectComments() {
		return subjectComments;
	}

	public void setSubjectComments(List<SubjectComment> subjectComments) {
		this.subjectComments = subjectComments;
	}

	public Degree getDeg() {
		return deg;
	}

	public void setDeg(Degree deg) {
		this.deg = deg;
	}

	public Student getStudent() {
		return this;
	}

	public boolean getAdmin() {
		return admin;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
