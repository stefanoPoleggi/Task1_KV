package hibernateTask1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//import javafx.beans.property.*;

@Entity(name = "Professors")
@Table(name = "professors")
public class Professor extends Person {

	@Column(name = "info", columnDefinition="TEXT")
	private String info;
	private String name;
	private String surname;

	// relation with profComments.
	@OneToMany(mappedBy = "prof", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ProfessorComment> professorComments = new ArrayList<ProfessorComment>();

	// relation with Subjects.
	@ManyToMany(mappedBy = "professor", cascade = CascadeType.ALL)
	private Set<Subject> subject = new HashSet<Subject>();

	public Professor() {

	}

	// CONSTRUCTOR
	public Professor(int i, String n, String s, String inf) {
		super(i);
		name = n;
		surname = s;
		info = inf;
	}

	public Set<Subject> getSubject() {
		return subject;
	}

	public void setSubject(Set<Subject> subject) {
		this.subject = subject;
	}

	public List<ProfessorComment> getProfessorComments() {
		return professorComments;
	}

	public void setProfessorComments(List<ProfessorComment> professorComments) {
		this.professorComments = professorComments;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

}