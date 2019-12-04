package hibernateTask1;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//import javafx.beans.property.*;

@Entity(name = "SubjectComments")
@Table(name = "subject_comments")
public class SubjectComment extends Comment {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Student stud;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectId")
	private Subject subj;

	public SubjectComment() {
		super();
	}

	// CONSTRUCTOR
	public SubjectComment(int i, String t, Date d) {
		super(i, t, d);
	}

	public Subject getSubj() {
		return subj;
	}

	public void setSubj(Subject subj) {
		this.subj = subj;
	}

	public Student getStud() {
		return stud;
	}

	public void setStud(Student stud) {
		this.stud = stud;
	}

}
