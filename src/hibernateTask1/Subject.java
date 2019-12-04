package hibernateTask1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//import javafx.beans.	property.*;

@Entity(name = "Subjects")
@Table(name = "subjects")
public class Subject {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private int credits;
	@Column(name = "info", columnDefinition="TEXT")
	private String info;

	// relation with degree
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "degreeId")
	private Degree deg;

	// relation with Subject comments.
	@OneToMany(mappedBy = "subj", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubjectComment> subjectComments = new ArrayList<SubjectComment>();

	// relation with Professors.
	@ManyToMany
	@JoinTable(name = "teaching", joinColumns = @JoinColumn(name = "subjectId"), inverseJoinColumns = @JoinColumn(name = "profId"))
	private Set<Professor> professor = new HashSet<Professor>();

	public Subject() {

	}

	// CONSTRUCTOR
	public Subject(int i, String n, int c, String inf) {
		id = i;
		name = n;
		credits = c;
		info = inf;
	}
	
	public void removeProf(Professor prof) {
        this.professor.remove(prof);
        prof.getSubject().remove(this);
    }

	public Set<Professor> getProfessor() {
		return professor;
	}

	public void setProfessor(Set<Professor> professor) {
		this.professor = professor;
	}

	public List<SubjectComment> getSubjectComments() {
		return subjectComments;
	}

	public void setSubjectComments(List<SubjectComment> subjectComments) {
		this.subjectComments = subjectComments;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Degree getDeg() {
		return deg;
	}

	public void setDeg(Degree deg) {
		this.deg = deg;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getCredits() {
		return credits;
	}

	public String getInfo() {
		return info;
	}

}
