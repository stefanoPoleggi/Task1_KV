package hibernateTask1;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

//import javafx.beans.property.*;

@MappedSuperclass
public abstract class Person {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	public void setId(int id) {
		this.id = id;
	}

	public Person() {

	}

	// CONSTRUCTOR
	public Person(int i) {
		id = i;
	}

	public int getId() {
		return id;
	}
}
