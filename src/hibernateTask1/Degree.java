package hibernateTask1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleStringProperty;

@Entity(name = "Degree")
@Table(name = "degree_programmes")
public class Degree {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", unique = true)
	private String name;

	// relation with students.
	@OneToMany(mappedBy = "deg", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Student> student = new ArrayList<Student>();

	// relation with subjects.
	@OneToMany(mappedBy = "deg", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Subject> subject = new ArrayList<Subject>();

	public Degree() {

	}

	public Degree(int i, String n) {
		id = i;
		name = n;
	}

	public List<Student> getStudent() {
		return student;
	}

	public void setStudent(List<Student> student) {
		this.student = student;
	}

	public List<Subject> getSubject() {
		return subject;
	}

	public void setSubject(List<Subject> subject) {
		this.subject = subject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
