package hibernateTask1;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

public class CacheManager {
	static DB levelDBStore;
	Options options = new Options();
	ManagerEM manager;
	List<Subject> sub_list;
	List<Professor> prof_list;
	List<Comment> prof_comment_list;
	int degree;
	boolean updated;
	List<String> subCommentUpdate, profCommentUpdate, subCommentDelete, profCommentDelete;
	File f;
	
	public CacheManager(int stud_id, ManagerEM m, int d) {
		try {
			f = new File("levelDBStore");
			levelDBStore = factory.open(f, options);
			manager = m;
			degree= d;
			updated = false;
			subCommentUpdate = new ArrayList<String>();
			profCommentUpdate = new ArrayList<String>();
			subCommentDelete = new ArrayList<String>();
			profCommentDelete = new ArrayList<String>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int seeDegree() {
		return degree;
	}
	
	public void initialize(int deg) {
		sub_list = manager.getSubjects(deg);
		prof_list = manager.getProfessors(deg);
		subjectCache();
		profCache();
		profCommentCache();
		subjectCommentCache();
		seeSubject();
		seeProf();
		seeProfComments();
		seeSubComments();
		
	}
	
	public void subjectCache() {
		//key -> subjects:$subject_id:$attribute_name
		String insertName, insertCredits, insertInfo;
		for(int i = 0; i < sub_list.size(); i++) {
			insertName = "subjects:" + sub_list.get(i).getId() + ":name"; //sub_list.get(i).getName();
			insertCredits = "subjects:" + sub_list.get(i).getId() + ":credits";//Integer.toString(sub_list.get(i).getCredits());
			insertInfo= "subjects:" + sub_list.get(i).getId() + ":info";//sub_list.get(i).getInfo();
			
			levelDBStore.put(insertName.getBytes(), sub_list.get(i).getName().getBytes());
			levelDBStore.put(insertCredits.getBytes(), Integer.toString(sub_list.get(i).getCredits()).getBytes());
			levelDBStore.put(insertInfo.getBytes(), sub_list.get(i).getInfo().getBytes());
		}
	}
	
	public void profCache() {
		//key -> professors:$professor_id:$attribute_name
		String insertName, insertSurname, insertInfo;
		for(int i = 0; i < prof_list.size(); i++) {
			insertName = "professors:" + prof_list.get(i).getId() + ":name"; //prof_list.get(i).getName();
			insertSurname = "professors:" + prof_list.get(i).getId() + ":surname";// prof_list.get(i).getSurname();
			insertInfo = "professors:" + prof_list.get(i).getId() + ":info";//prof_list.get(i).getInfo().getBytes());
			
			levelDBStore.put(insertName.getBytes(), prof_list.get(i).getName().getBytes());
			levelDBStore.put(insertSurname.getBytes(), prof_list.get(i).getSurname().getBytes());
			levelDBStore.put(insertInfo.getBytes(), prof_list.get(i).getInfo().getBytes());
		}
	}
	
	public void profCommentCache() {
		List<ProfessorComment> tmpCmt; 
		String insertText, insertDate, strDate;
		for(int i = 0; i < prof_list.size(); i++) {
			tmpCmt = manager.getProfessorCommentsCache(prof_list.get(i).getId());
			for(int j = 0; j < tmpCmt.size(); j++) {
				//key -> prof_comments:$prof_comments_id:$user_id:$professor_id:$attribute_name
				insertText = "prof_comments:" + tmpCmt.get(j).getId() + ":" + tmpCmt.get(j).getStud().getId() +":" + tmpCmt.get(j).getProf().getId() + ":text";
				insertDate = "prof_comments:" + tmpCmt.get(j).getId() + ":" + tmpCmt.get(j).getStud().getId() +":" + tmpCmt.get(j).getProf().getId() + ":date";
				
				
				strDate = tmpCmt.get(j).getDate();
				System.out.println(strDate);
				levelDBStore.put(insertText.getBytes(), tmpCmt.get(j).getText().getBytes());
				levelDBStore.put(insertDate.getBytes(), strDate.getBytes());
			}
		}
	}
	
	private void subjectCommentCache() {
		List<SubjectComment> tmpCmt; 
		String insertText, insertDate, strDate;
		for(int i = 0; i < sub_list.size(); i++) {
			tmpCmt = manager.getSubjectCommentsCache(sub_list.get(i).getId());
			for(int j = 0; j < tmpCmt.size(); j++) {
				//key -> subject_comments:$subject_comment_id:$user_id:$subject_id:$attribute_name
				insertText = "subject_comments:" + tmpCmt.get(j).getId() + ":" + tmpCmt.get(j).getStud().getId() +":" + tmpCmt.get(j).getSubj().getId() + ":text";
				insertDate = "subject_comments:" + tmpCmt.get(j).getId() + ":" + tmpCmt.get(j).getStud().getId() +":" + tmpCmt.get(j).getSubj().getId() + ":date";
	
				strDate = tmpCmt.get(j).getDate();
				System.out.println(strDate);
				levelDBStore.put(insertText.getBytes(), tmpCmt.get(j).getText().getBytes());
				levelDBStore.put(insertDate.getBytes(), strDate.getBytes());
			}
		}
	}
	
	
	public static List<Subject> retrieveSubjects(){
		List<Subject> list = new ArrayList<>();
		int id = 0;
		try {
			String tmp, name = null, credits = null, info = null;
			DBIterator it = levelDBStore.iterator();
			it.seek("subjects".getBytes());
			try {
				while(it.hasNext()) {
					
					String key =  new String(it.peekNext().getKey(), "UTF-8");
					//System.out.println(key);
					String[] keySplit = key.split(":");
		
					if (!keySplit[0].equals("subjects")) { // breaking condition : prefix is not "subjects"
	                    System.out.println("break");
						break;
	                }
										
					if(keySplit[3].equals("credits")) {
						credits =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						id = Integer.parseInt(keySplit[1]);
					}
					if(keySplit[3].equals("info")) {
						info =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						id = Integer.parseInt(keySplit[1]);
					}
					if(keySplit[3].equals("name")) {
						name =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						id = Integer.parseInt(keySplit[1]);
					}
					
					if(name != null  && info != null && credits != null) {
						System.out.println("id:"+keySplit[1]+"name:"+name);
						System.out.println(name);
						System.out.println(credits);
						System.out.println(info);
						list.add(new Subject(id, name, Integer.parseInt(credits), info));
						name = null;
						info = null;
						credits = null;	
					}
					it.next();
				}	
			}finally {
				it.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	List<Professor> retrieveProfessors(){
		List<Professor> list = new ArrayList<>();
		try {
			DBIterator it = levelDBStore.iterator();
			it.seek("professors".getBytes());
			String name = null, surname = null, info = null, id;
			try {
				while(it.hasNext()) {
					String key =  new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if (!keySplit[0].equals("professors")) { // breaking condition : prefix is not "professors"
						System.out.println("break;");
	                    break;
	                }
					id = keySplit[1];
					if(keySplit[2].equals("name")) {
						name =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						//System.out.println("name: " + name);
					}
					if(keySplit[2].equals("surname")) {
						surname =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						//System.out.println("surame: " +surname);
					}
					if(keySplit[2].equals("info")) {
						info =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						//System.out.println("info: "  + info);
						
					}
					if(name != null && info != null && surname != null) {
						list.add(new Professor(Integer.parseInt(id), name, surname, info));
						name = null;
						info = null;
						surname = null;
					}
					
					it.next();
				}
				
			}finally {
				it.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	List<ProfessorComment> retrieveProfessorComments(int profId){
		List<ProfessorComment> list = new ArrayList<>();
		try {
			DBIterator it = levelDBStore.iterator();
			it.seek("prof_comments".getBytes());
			String id = null, usr_id = null, prof_id = null, text = null, date = null;
			try {
				while(it.hasNext()) {
					String key =  new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if (!keySplit[0].equals("prof_comments")) { // breaking condition : prefix is not "prof_comments"
	                    break;
	                }
					if(!keySplit[3].equals(Integer.toString(profId))){
						it.next();
						continue;
					}
					id = keySplit[1];
					usr_id = keySplit[2];
					prof_id = keySplit[3]; 
					if(keySplit[4].equals("text")) {
						text =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("text:" + text);
					}
					if(keySplit[4].equals("date")) {
						date =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("date:" + date);
					}
					if(id != null && usr_id != null && prof_id != null && text != null && date != null) {
						Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
						list.add(new ProfessorComment(Integer.parseInt(id), text, d));
						id = null;
						usr_id = null;
						prof_id = null;
						text = null;
						date = null;
					}
					it.next();
				}
			}finally {
				it.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	List<SubjectComment> retrieveSubjectComments(int subId){
		List<SubjectComment> list = new ArrayList<>();
		try {
			DBIterator it = levelDBStore.iterator();
			it.seek("subject_comments".getBytes());
			String id = null, usr_id = null, sub_id = null, text = null, date = null;
			try {
				while(it.hasNext()) {
					String key =  new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if (!keySplit[0].equals("subject_comments")) { // breaking condition : prefix is not "subject_comments"
	                    break;
	                }
					if(!keySplit[3].equals(Integer.toString(subId))){
						it.next();
						continue;
					}
					id = keySplit[1];
					usr_id = keySplit[2];
					sub_id = keySplit[3];
					if(keySplit[4].equals("text")) {
						text =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
					}
					if(keySplit[4].equals("date")) {
						date =  new String(levelDBStore.get(key.getBytes()), "UTF-8");
					}
					if(id != null && usr_id != null && sub_id != null && text != null && date != null) {
						Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
						list.add(new SubjectComment(Integer.parseInt(id), text, d));
						id = null;
						usr_id = null;
					    sub_id = null;
						text = null;
						date = null;
					}
					it.next();
				}
			}finally {
				it.close();
				//levelDBStore.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void insertSubjectComment(String text, Date d, Student s, int subId) {
		int id = getID("subject_comments");
		//subject_comments:subject_comment_id:user_id:sub_id:text
		  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = dateFormat.format(d);
		String insertText = "subject_comments:" + id + ":" +s.getId()+":"+subId+":text";
		String insertDate = "subject_comments:" + id + ":" +s.getId()+":"+subId+":date";
		levelDBStore.put(insertText.getBytes(), text.getBytes());
		levelDBStore.put(insertDate.getBytes(), strDate.getBytes());
		System.out.println("Inserisco "+insertText + ":" + text);
		manager.createSubjectComment(text, d, s, subId);
	}
	
	public void insertProfessorComment(String text, Date d, Student s, int profId){
		int id = getID("prof_comments");
		//prof_comments:prof_comment_id:user_id:prof_id:text
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String strDate = dateFormat.format(d);  
		String insertText = "prof_comments:" + id + ":" +s.getId()+":"+profId+":text";
		String insertDate = "prof_comments:" + id + ":" +s.getId()+":"+profId+":date";
		levelDBStore.put(insertText.getBytes(), text.getBytes());
		levelDBStore.put(insertDate.getBytes(), strDate.getBytes());
		manager.createProfessorComment(text, d, s, profId);
	
	}
	
	public void updateCommentSubject(int i, String t, int s, int subId) {
		//subject_comments:subject_comment_id:user_id:sub_id:$attribute
		  
		String insertText = "subject_comments:" + i + ":" +s+":"+subId+":text";

		levelDBStore.put(insertText.getBytes(), t.getBytes());
		subCommentUpdate.add(i + ":" + t + ":" + s);
		
	
	}
	
	public void updateCommentProf(int i, String t, int s, int profId) {
		//prof_comments:prof_comment_id:user_id:prof_id:$attribute
		  
		String insertText = "prof_comments:" + i + ":" +s+":"+profId+":text";

		levelDBStore.put(insertText.getBytes(), t.getBytes());
		profCommentUpdate.add(i + ":" + t + ":" + s);
		
	
	}
	
	public void deleteCommentSubject(int sub_cmt_id, int stud, int subId, boolean a) {
		String deleteText = "subject_comments:" + sub_cmt_id+ ":" +stud+":"+subId+":text";
		String deleteDate = "subject_comments:" + sub_cmt_id + ":" +stud+":"+subId+":date";
		
		levelDBStore.delete(deleteText.getBytes());
		levelDBStore.delete(deleteDate.getBytes());
		subCommentDelete.add(sub_cmt_id + ":" + stud + ":" + a);
		
	}
	
	public void deleteCommentProf(int prof_cmt_id, int stud, int profId, boolean a) {
		String deleteText = "prof_comments:" + prof_cmt_id + ":" +stud+":"+profId+":text";
		String deleteDate = "prof_comments:" + prof_cmt_id + ":" +stud+":"+profId+":date";
		
		
		levelDBStore.delete(deleteText.getBytes());
		levelDBStore.delete(deleteDate.getBytes());
		System.out.println("deleted from leveldb " + prof_cmt_id + " user: " + stud);
		profCommentDelete.add(prof_cmt_id  +":" + stud + ":" + a);
		
	}
	
	public static int getID(String t) {
		int id = 0;
		DBIterator it = levelDBStore.iterator();
		it.seek(t.getBytes());
		try {
			try {
				while(it.hasNext()) {
					String key = new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if(!keySplit[0].equals(t)) { //breaking condition
						break;
					}
					int tmp = Integer.parseInt(keySplit[1]);
					if(tmp > id) {
						id = tmp;
					}
					
					it.next();
				}
			}finally {
				it.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		id++;
		return id;
	}
	
	public void assureConsistency() {
		// subCommentUpdate, profCommentUpdate, subCommentDelete, profCommentDelete
		if(subCommentUpdate != null) {
			for(int i = 0; i < subCommentUpdate.size(); i++) {
				String[] key = subCommentUpdate.get(i).split(":"); //key format: key[0]: sub_comment_id, key[1]: text, key[2]: student_id
				if(manager.updateCommentSubject(Integer.parseInt(key[0]), key[1], Integer.parseInt(key[2]))) {
					System.out.println("Comment updated");
				}
			}
		}
		if(profCommentUpdate != null) {
			for(int i = 0; i < profCommentUpdate.size(); i++) {
				System.out.println(profCommentUpdate.get(i));
				String[] key = profCommentUpdate.get(i).split(":"); //key format: key[0]: prof_comment_id, key[1]: text, key[2]: student_id
				if(manager.updateCommentProf(Integer.parseInt(key[0]), key[1], Integer.parseInt(key[2]))) {
					System.out.println("Comment updated");
				}
			}
		}
		if(profCommentDelete != null) {
			for(int i = 0; i < profCommentDelete.size(); i++) {
				String[] key = profCommentDelete.get(i).split(":"); //key format: key[0]: prof_comment_id, key[1]: student_id, key[2]: admin?
				if(manager.deleteCommentProf(Integer.parseInt(key[0]), Integer.parseInt(key[1]), Boolean.parseBoolean(key[2]))) {
					System.out.println("Comment deleted");
				}
			}
		}
		if(subCommentDelete != null) {
			for(int i = 0; i < subCommentDelete.size(); i++) {
				String[] key = subCommentDelete.get(i).split(":"); //key format: key[0]: sub_comment_id, key[1]: student_id, key[2]: admin?
				if(manager.deleteCommentSubject(Integer.parseInt(key[0]), Integer.parseInt(key[1]), Boolean.parseBoolean(key[2]))) {
					System.out.println("Comment deleted");
				}
			}
		}
		subCommentUpdate = null;
		profCommentUpdate = null;
		subCommentDelete = null;
		profCommentDelete = null;
	}
	
	
	public void close() {
		assureConsistency();
		try {
			levelDBStore.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*if(!f.delete()) {
			System.out.println("cache not deleted");
		}*/
		cleanDirectory(f);
	}
	
	public static void removeDirectory(File dir) {
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        if (files != null && files.length > 0) {
	            for (File aFile : files) {
	                removeDirectory(aFile);
	            }
	        }
	        dir.delete();
	    } else {
	        dir.delete();
	    }
	}
	
	public static void cleanDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File aFile : files) {
					removeDirectory(aFile);
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void seeSubject() {
		DBIterator it = levelDBStore.iterator();
		it.seek("subjects".getBytes());
		try {
			try {
				while(it.hasNext()) {
					String key = new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if(!keySplit[0].equals("subjects")) { //breaking condition
						break;
					}
					
					int sub_id = Integer.parseInt(keySplit[1]);
					if(keySplit[2].equals("name")) {
						String name = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("subjects:"+sub_id+":name:"+name);
					}
					
					if(keySplit[2].equals("credits")) {
						String credits = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("subjects:"+sub_id+":credits:"+credits);
					}
					
					if(keySplit[2].equals("credits")) {
						String info = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("subjects:"+sub_id+":info:"+info);
					}
					it.next();
				}
			}finally {
				it.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void seeProf() {
		DBIterator it = levelDBStore.iterator();
		it.seek("professors".getBytes());
		try {
			try {
				while(it.hasNext()) {
					String key = new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if(!keySplit[0].equals("professors")) { //breaking condition
						break;
					}
					
					int sub_id = Integer.parseInt(keySplit[1]);
					if(keySplit[2].equals("name")) {
						String name = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("professors:"+sub_id+":name:"+name);
					}
					
					if(keySplit[2].equals("surname")) {
						String credits = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("professors:"+sub_id+":surname:"+credits);
					}
					
					if(keySplit[2].equals("info")) {
						String info = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("professors:"+sub_id+":info:"+info);
					}
					it.next();
				}
			}finally {
				it.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void seeProfComments() {
		DBIterator it = levelDBStore.iterator();
		it.seek("prof_comments".getBytes());
		try {
			try {
				while(it.hasNext()) {
					String key = new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if(!keySplit[0].equals("prof_comments")) { //breaking condition
						break;
					}
					//key -> prof_comments:$prof_comments_id:$user_id:$professor_id:$attribute_name
					int prof_cmt_id = Integer.parseInt(keySplit[1]);
					if(keySplit[4].equals("text")) {
						String text = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("prof_comments:"+prof_cmt_id+":" + keySplit[2] + ":" + keySplit[3] +":name:"+text);
					}
					
					if(keySplit[4].equals("date")) {
						String date = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("prof_comments:"+prof_cmt_id+":" + keySplit[2] + ":" + keySplit[3] +":name:"+date);
					}
					
					
					it.next();
				}
			}finally {
				it.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void seeSubComments() {
		DBIterator it = levelDBStore.iterator();
		it.seek("subject_comments".getBytes());
		try {
			try {
				while(it.hasNext()) {
					String key = new String(it.peekNext().getKey(), "UTF-8");
					String[] keySplit = key.split(":");
					if(!keySplit[0].equals("subject_comments")) { //breaking condition
						break;
					}
					//key -> subject_comments:$subject_comment_id:$user_id:$subject_id:$attribute_name
					int sub_cmt_id = Integer.parseInt(keySplit[1]);
					if(keySplit[4].equals("text")) {
						String text = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("subject_comments:"+sub_cmt_id+":" + keySplit[2] + ":" + keySplit[3] +":name:"+text);
					}
					
					if(keySplit[4].equals("date")) {
						String date = new String(levelDBStore.get(key.getBytes()), "UTF-8");
						System.out.println("subject_comments:"+sub_cmt_id+":" + keySplit[2] + ":" + keySplit[3] +":name:"+date);
					}
					
					
					it.next();
				}
			}finally {
				it.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}




















