package com.aws.lambdas3demo.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.aws.lambdas3demo.util.AWSUtil;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainSceneController implements Initializable {

	private Scene parent;

	@FXML
	private Label label;

	private FileChooser fileChooser;

	@FXML
	private void handleButtonAction(ActionEvent event) {
		// System.out.println("You clicked me!");
		// label.setText("Hello World!");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("all", "*.*"));
		fileChooser.setTitle("Escoge un archivo");
		// fileChooser.setInitialDirectory(new File("Imágenes"));
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			AWSUtil.uploadFileToS3(AWSUtil.AWS_BUCKET, selectedFile.getName(), selectedFile.getAbsolutePath(), 
			e -> {
				//if (label.getText().trim().isEmpty())
				Platform.runLater(() -> label.setText("Estado: Subiendo..."));
			}, e -> {
				Platform.runLater(() -> {
					label.setText("Estado: Completado");
					System.out.println(e.getEventType());
				});
			});
		} else {
			label.setText("No seleccionó ningun archivo");
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		fileChooser = new FileChooser();
		parent = label.getScene();
	}
}
