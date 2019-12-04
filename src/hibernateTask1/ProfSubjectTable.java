package hibernateTask1;

import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

public class ProfSubjectTable extends TableView<Object> {

	ObservableList<Object> professorsList;
	ObservableList<Object> subjectsList;

	TableColumn idProfColumn, nameProfColumn, surnameProfColumn, idSubjectColumn, nameSubjectColumn, creditSubjectColumn;

	GraphicInterface graphic;

	ProfSubjectTable(GraphicInterface g) {

		graphic = g;
		professorsList = FXCollections.observableArrayList();
		professorsList.addAll(graphic.manager.getProfessors(-1));

		subjectsList = FXCollections.observableArrayList();
		subjectsList.addAll(graphic.manager.getSubjects(-1));

		// table = new TableView<>();

		idProfColumn = new TableColumn("ID");
		idProfColumn.setCellValueFactory(new PropertyValueFactory<Professor, String>("id"));

		nameProfColumn = new TableColumn("NAME");
		nameProfColumn.setCellValueFactory(new PropertyValueFactory<Professor, String>("name"));

		surnameProfColumn = new TableColumn("SURNAME");
		surnameProfColumn.setCellValueFactory(new PropertyValueFactory<Professor, String>("surname"));

		idSubjectColumn = new TableColumn("ID");
		idSubjectColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("id"));

		nameSubjectColumn = new TableColumn("NAME");
		nameSubjectColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));

		creditSubjectColumn = new TableColumn("CREDITS");
		creditSubjectColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("credits"));

		// setProfessorsList();
		this.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		this.setEditable(true);
	}

	void setProfessorsList(List<Professor> l) {
		this.professorsList.clear();
		this.professorsList.addAll(l);

		this.getColumns().clear();
		this.getColumns().addAll(idProfColumn, nameProfColumn, surnameProfColumn);
		this.setItems(professorsList);

		if (graphic.student != null && graphic.student.getAdmin()) {

			graphic.FieldsAdminBox.setVisible(true);
			graphic.infoLab.setText("Professor Informations:");
			graphic.surnameAndCreditsLab.setText("Surname:");
			graphic.FieldsAdminBox.getChildren().remove(graphic.profIdBox);

		} else {
			graphic.FieldsAdminBox.setVisible(false);
		}
	}

	// update table: set subjects information
	void setSubjectsList(List<Subject> l) {
		this.subjectsList.clear();
		this.subjectsList.addAll(l);

		this.getColumns().clear();
		this.getColumns().addAll(idSubjectColumn, nameSubjectColumn, creditSubjectColumn);
		this.setItems(subjectsList);

		if (graphic.student != null && graphic.student.getAdmin()) {
			graphic.FieldsAdminBox.setVisible(true);
			graphic.infoLab.setText("Subject Informations:");
			graphic.surnameAndCreditsLab.setText("Credits:");
			if (!graphic.FieldsAdminBox.getChildren().contains(graphic.profIdBox))
				graphic.FieldsAdminBox.getChildren().add(2, graphic.profIdBox);
		} else {
			graphic.FieldsAdminBox.setVisible(false);
		}
	}
}
