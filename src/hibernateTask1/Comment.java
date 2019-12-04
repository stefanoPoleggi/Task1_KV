package hibernateTask1;

import java.text.*;
import java.time.*  ;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.persistence.*;

//import javafx.beans.property.*;

@MappedSuperclass
public abstract class Comment {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String text;
	private String date;
        
    private static String format = "yyyy-MM-dd HH:mm:ss";
        
	public Comment() {

	}

	public Comment(int i, String t, Date d) {
		id = i;
		text = t;
		date = new SimpleDateFormat(format).format(d);
	}

	public int getId() {
		return id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setDate(Date date) {
		this.date = new SimpleDateFormat(format).format(date);
	}

	public String getText() {
		return text;
	}

	public String getDate() {
		return date;
	}
}