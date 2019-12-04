package hibernateTask1;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//import javafx.beans.property.*;

@Entity(name = "ProfComments")
@Table(name = "prof_comments")
public class ProfessorComment extends Comment {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profId")
	private Professor prof;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Student stud;

	public ProfessorComment() {
		super();
	}

	// CONSTRUCTOR
	public ProfessorComment(int i, String t, Date d) {
		super(i, t, d);
	}

	public Student getStud() {
		return stud;
	}

	public void setStud(Student stud) {
		this.stud = stud;
	}

	public Professor getProf() {
		return prof;
	}

	public void setProf(Professor prof) {
		this.prof = prof;
	}

}