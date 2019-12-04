package hibernateTask1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class ManagerEM {
	private static EntityManagerFactory factory;
	private EntityManager entityManager;

	public void setup() {
		factory = Persistence.createEntityManagerFactory("studentsratings");

	}

	public void exit() {
		factory.close();
	}

	public Degree createDegree(String name) {
		System.out.println("Creating a new degree program");

		Degree degree = new Degree(0, name);

		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(degree);
			entityManager.getTransaction().commit();
			System.out.println("degree Added");
			return degree;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a degree!");
		} finally {
			entityManager.close();
		}
		return degree;

	}

	public Professor createProfessor(String name, String surname, String info) {
		System.out.println("Creating a new professor");

		Professor professor = new Professor(0, name, surname, info);

		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(professor);
			entityManager.getTransaction().commit();
			System.out.println("professor Added");
			return professor;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a professor!");
		} finally {
			entityManager.close();
		}
		return professor;

	}

	public Subject createSubject(String name, int credits, String info, String profIdStr, int degreeId) {
		System.out.println("Creating a new subject");

		Subject subject = new Subject(0, name, credits, info);
		String[] professorsId = profIdStr.split(",", 5);
		try {
			entityManager = factory.createEntityManager();
			Degree degree = entityManager.find(Degree.class, degreeId);
			int profId = 0;
			for (String p : professorsId) {
				profId = Integer.parseInt(p);
				Professor professor = null;
				if (profId > 0) {
					professor = entityManager.find(Professor.class, profId);
				}
				if (profId > 0 && professor == null) {
					System.err.println("the inserted professor Id doesn't exixst");
					entityManager.close();
					return null;
				}
				subject.getProfessor().add(professor);
				professor.getSubject().add(subject);
			}
			degree.getSubject().add(subject);
			subject.setDeg(degree);

			entityManager.getTransaction().begin();
			entityManager.persist(subject);
			entityManager.getTransaction().commit();
			System.out.println("subject Added");
			return subject;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a subject!");
		} finally {
			entityManager.close();
		}
		return subject;

	}

	public SubjectComment createSubjectComment(String text, Date date, Student student, int subjectId) {
		System.out.println("Creating a new subjectComment");

		SubjectComment subjectComment = null;

		try {
			entityManager = factory.createEntityManager();
			Subject subj = entityManager.find(Subject.class, subjectId);

			subjectComment = new SubjectComment(0, text, date);
			subj.getSubjectComments().add(subjectComment);
			subjectComment.setStud(student);
			subjectComment.setSubj(subj);

			entityManager.getTransaction().begin();
			entityManager.persist(subjectComment);
			entityManager.getTransaction().commit();
			System.out.println("subjectComment Added");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a subjectComment!");
		} finally {
			entityManager.close();
		}

		return subjectComment;

	}

	public ProfessorComment createProfessorComment(String text, Date date, Student student, int profId) {
		System.out.println("Creating a new professorComment");

		ProfessorComment professorComment = null;

		try {
			entityManager = factory.createEntityManager();
			Professor prof = entityManager.find(Professor.class, profId);

			professorComment = new ProfessorComment(0, text, date);
			prof.getProfessorComments().add(professorComment);
			professorComment.setStud(student);
			professorComment.setProf(prof);

			entityManager.getTransaction().begin();
			entityManager.persist(professorComment);
			entityManager.getTransaction().commit();
			System.out.println("professorComment Added");
			return professorComment;

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a professorComment!");
		} finally {
			entityManager.close();
		}
		return professorComment;
	}

	public Student checkUser(String username, String password) {

		Student student = null;
		System.out.println("Getting a student");
		String selectUser = "SELECT u FROM Users u WHERE username = ?1 AND password = ?2";
		try {
			entityManager = factory.createEntityManager();

			TypedQuery<Student> query = entityManager.createQuery(selectUser, Student.class);
			query.setParameter(1, username);
			query.setParameter(2, password);

			List<Student> results = query.getResultList();
			if (results.size() == 0) {
				return null;
			} else if (results.size() > 1) {
				System.err.println("In the Database are present more than one user with the same username");
			} else {
				System.out.println(results.get(0).getUsername() + " " + results.get(0).getPassword() + " "
						+ results.get(0).getDeg().getId());
				student = results.get(0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving a student!");

		} finally {
			entityManager.close();
		}
		return student;
	}

	public List<Subject> getSubjects(int degree) {

		List<Subject> results = new ArrayList<>();
		System.out.println("Getting a List of subjects based on the degree");

		String selectionSubjects = "SELECT s FROM Subjects s ORDER BY s.name";
		String selectionSubjectByDegree = "SELECT s FROM Subjects s WHERE degreeId = ?1 ORDER BY s.name";
		try {
			entityManager = factory.createEntityManager();
			if (degree < 0) {
				TypedQuery<Subject> query = entityManager.createQuery(selectionSubjects, Subject.class);
				results = query.getResultList();
			} else {
				
				TypedQuery<Subject> query = entityManager.createQuery(selectionSubjectByDegree, Subject.class);
				query.setParameter(1, degree);
				results = query.getResultList();
				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving subjects!");

		} finally {
			entityManager.close();
		}
		return results;
	}

	public List<Professor> getProfessors(int degree) {

		List<Professor> results = new ArrayList<>();
		System.out.println("Getting a List of professors based on the degree");

		String selectionProfessors = "SELECT p FROM Professors p ORDER BY p.surname, p.name";
		String selectionProfessorByDegree = "SELECT p FROM Subjects s JOIN s.professor p WHERE s.deg.id = ?1 ORDER BY p.surname, p.name";
		try {
			entityManager = factory.createEntityManager();
			if (degree < 0) {
				TypedQuery<Professor> query = entityManager.createQuery(selectionProfessors, Professor.class);
				results = query.getResultList();

			} else {

				TypedQuery<Professor> query = entityManager.createQuery(selectionProfessorByDegree, Professor.class);
				query.setParameter(1, degree);
				results = query.getResultList();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving professors!");

		} finally {
			entityManager.close();
		}
		return results;
	}

	public List<Comment> getSubjectComments(int subjectId) {
		List<Comment> results = new ArrayList<>();
		System.out.println("Getting a list of subject comments");
		if (subjectId == -1)
			return results;
		String selectionSubjectComments = "SELECT sc FROM SubjectComments sc WHERE sc.subj.id = ?1 ORDER BY sc.date";
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Comment> query = entityManager.createQuery(selectionSubjectComments, Comment.class);
			query.setParameter(1, subjectId);
			results = query.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving subject comments!");

		} finally {
			entityManager.close();
		}
		return results;
	}

	public List<SubjectComment> getSubjectCommentsCache(int subjectId) {
		List<SubjectComment> results = new ArrayList<>();
		System.out.println("Getting a list of subject comments");
		if (subjectId == -1)
			return results;
		String selectionSubjectComments = "SELECT sc FROM SubjectComments sc WHERE sc.subj.id = ?1 ORDER BY sc.date";
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<SubjectComment> query = entityManager.createQuery(selectionSubjectComments, SubjectComment.class);
			query.setParameter(1, subjectId);
			results = query.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving subject comments!");

		} finally {
			entityManager.close();
		}
		return results;
	}
	
	public List<Comment> getProfessorComments(int profId) {
		List<Comment> results = new ArrayList<>();
		System.out.println("Getting a list of professor comments");
		if (profId == -1)
			return results;

		String selectionProfessorComments = "SELECT pc FROM ProfComments pc WHERE pc.prof.id = ?1 ORDER BY pc.date";
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Comment> query = entityManager.createQuery(selectionProfessorComments, Comment.class);
			query.setParameter(1, profId);
			results = query.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving professor comments!");

		} finally {
			entityManager.close();
		}
		return results;
	}

	public List<ProfessorComment> getProfessorCommentsCache(int profId) {
		List<ProfessorComment> results = new ArrayList<>();
		System.out.println("Getting a list of professor comments");
		if (profId == -1)
			return results;

		String selectionProfessorComments = "SELECT pc FROM ProfComments pc WHERE pc.prof.id = ?1 ORDER BY pc.date";
		try {
			entityManager = factory.createEntityManager();
			TypedQuery<ProfessorComment> query = entityManager.createQuery(selectionProfessorComments, ProfessorComment.class);
			query.setParameter(1, profId);
			results = query.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving professor comments!");

		} finally {
			entityManager.close();
		}
		return results;
	}
	
	public List<Degree> getDegreeCourses() {
		List<Degree> results = new ArrayList<>();
		System.out.println("Getting a list of degree courses");

		String selectionDegreeComments = "SELECT d FROM Degree d";

		try {
			entityManager = factory.createEntityManager();
			TypedQuery<Degree> query = entityManager.createQuery(selectionDegreeComments, Degree.class);
			results = query.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in retriving Degree courses!");

		} finally {
			entityManager.close();
		}
		return results;
	}

	public boolean updateCommentProf(int profCommentId, String text, int userId) {
		System.out.println("Updating a professor comment");
		boolean updated = false;
		Date date = new Date();

		try {
			entityManager = factory.createEntityManager();
			ProfessorComment professorComment = entityManager.find(ProfessorComment.class, profCommentId);
			// if the user is the owner.
			if (professorComment.getStud().getId() == userId) {
				entityManager.getTransaction().begin();
				professorComment.setText(text);
				professorComment.setDate(date);
				entityManager.getTransaction().commit();
				System.out.println("professor comment updated");
				updated = true;
			} else { // if the user is not owner --> error
				System.err.println("You are not the owner of that comment, please select another comment");
				entityManager.close();
				return updated;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a professor comment!");

		} finally {
			entityManager.close();
		}
		return updated;
	}

	public boolean updateCommentSubject(int subjectCommentId, String text, int userId) {
		System.out.println("Updating a subject comment");
		boolean updated = false;
		Date date = new Date();

		try {
			entityManager = factory.createEntityManager();
			SubjectComment subjectComment = entityManager.find(SubjectComment.class, subjectCommentId);

			// if the user is the owner.
			if (subjectComment.getStud().getId() == userId) {
				entityManager.getTransaction().begin();
				subjectComment.setText(text);
				subjectComment.setDate(date);
				entityManager.getTransaction().commit();
				System.out.println("subject comment updated");
				updated = true;

			} else { // if user is not owner --> error
				System.err.println("You are not the owner of that comment, please select another comment");
				entityManager.close();
				return updated;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a subject comment!");

		} finally {
			entityManager.close();
		}
		return updated;
	}

	public boolean deleteCommentSubject(int subjectCommentId, int userId, boolean admin) {
		boolean deleted = false;
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			SubjectComment subjectComment = entityManager.find(SubjectComment.class, subjectCommentId);

			// if user is owner OR admin he can delete the comment
			if (subjectComment.getStud().getId() == userId || admin) {
				entityManager.remove(subjectComment);
				deleted = true;
			} else { // if user is not owner AND he is not admin --> error
				System.err.println("You are not the owner of that comment, please select another comment");
				return deleted;
			}

			entityManager.getTransaction().commit();
			System.out.println("subject comment removed");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in removing a subject comment!");

		} finally {
			entityManager.close();
		}
		return deleted;
	}

	public boolean deleteCommentProf(int profCommentId, int userId, boolean admin) {
		boolean deleted = false;
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			ProfessorComment professorComment = entityManager.find(ProfessorComment.class, profCommentId);

			// if user is owner OR admin he can delete the comment
			if (professorComment.getStud().getId() == userId || admin) {
				entityManager.remove(professorComment);
				deleted = true;
			} else { // if user is not owner AND he is not admin --> error
				System.err.println("You are not the owner of that comment, please select another comment");
				return deleted;
			}

			entityManager.getTransaction().commit();
			System.out.println("professor comment removed");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in removing a professor comment!");

		} finally {
			entityManager.close();
		}
		return deleted;
	}

	public int newDegreeIndex(Student student, List<Degree> degreeList) {
		int i = 0;
		// System.out.println("test");

		try {
			entityManager = factory.createEntityManager();
			Degree degree = entityManager.find(Degree.class, student.getDeg().getId());
			for (i = 1; i < degreeList.size(); i++) {
				System.out.println(degreeList.get(i).getName() + " " + degree.getName());
				if (degreeList.get(i).getName().equals(degree.getName())) {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in getting the student degree");

		} finally {
			entityManager.close();
		}
		return i;
	}

	void editProfessor(int profId, String name, String surname, String info) {
		System.out.println("Updating a professor");
		try {
			entityManager = factory.createEntityManager();
			Professor professor = entityManager.find(Professor.class, profId);

			entityManager.getTransaction().begin();
			professor.setName(name);
			professor.setSurname(surname);
			professor.setInfo(info);
			entityManager.getTransaction().commit();
			System.out.println("professor updated");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in updating a professor!");

		} finally {
			entityManager.close();
		}
	}

	public boolean editSubject(int subjectId, String name, int credits, String info, String profIdStr) {
		System.out.println("Updating a subject");
		boolean updated = false;	
		try {
			entityManager = factory.createEntityManager();
			Subject subject = entityManager.find(Subject.class, subjectId);
			entityManager.getTransaction().begin();
			subject.setName(name);
			subject.setCredits(credits);
			subject.setInfo(info);
			entityManager.getTransaction().commit();
			
			if(!profIdStr.isBlank()) {
				String[] professorsId = profIdStr.split("," , 5);
				while(subject.getProfessor().size() > 0){
					Professor prof = subject.getProfessor().iterator().next();
					subject.removeProf(prof);
				}
				
				int profId = 0;
				
				for(String p : professorsId) {
					profId = Integer.parseInt(p);
					Professor professor = null;
					if (profId > 0) {
						professor = entityManager.find(Professor.class, profId);
					}
					if (profId > 0 && professor == null) {
						System.err.println("the inserted professor Id doesn't exixst");
						entityManager.close();
						return updated;
					}
					entityManager.getTransaction().begin();
					if (professor != null)
						subject.getProfessor().add(professor);
					entityManager.getTransaction().commit();
					updated = true;
				}	
			}
			
			updated = true;
			System.out.println("subject updated");
			
		} catch (NumberFormatException ex) {
			System.err.println("A problem occurred in updating a subject!");

		} finally {
			entityManager.close();
		}
	
		return updated;
	}

	public void deleteProfessor(int profId) {
		try {
			entityManager = factory.createEntityManager();
			Professor professor = entityManager.find(Professor.class, profId);
			entityManager.getTransaction().begin();
			entityManager.remove(professor);
			entityManager.getTransaction().commit();
			System.out.println("professor removed");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in removing a professor");

		} finally {
			entityManager.close();
		}
	}

	public void deleteSubject(int subjectId) {
		try {
			entityManager = factory.createEntityManager();
			Subject subject = entityManager.find(Subject.class, subjectId);
			entityManager.getTransaction().begin();
			entityManager.remove(subject);
			entityManager.getTransaction().commit();
			System.out.println("subject removed");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("A problem occurred in removing a subject");

		} finally {
			entityManager.close();
		}
	}
	
	public boolean profCommentBelongs(int profCommentId, int userId) {
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			ProfessorComment professorComment = entityManager.find(ProfessorComment.class, profCommentId);

			// if user is owner OR admin he can delete the comment
			if (professorComment.getStud().getId() == userId) {
				return true;
			} else { // if user is not owner AND he is not admin --> error
				return false;
			}
		}finally {
			entityManager.close();
		}
	}

	public boolean subCommentBelongs(int subCommentId, int userId) {
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			SubjectComment subComment = entityManager.find(SubjectComment.class, subCommentId);

			// if user is owner OR admin he can delete the comment
			if (subComment.getStud().getId() == userId) {
				return true;
			} else { // if user is not owner AND he is not admin --> error
				return false;
			}
		}finally {
			entityManager.close();
		}
	}
}