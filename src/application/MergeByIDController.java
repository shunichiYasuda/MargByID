package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class MergeByIDController {
	File baseFile, mergeFile;
	final ToggleGroup group = new ToggleGroup();
	String sysEncode;
	String filePath = null;
	boolean fileSetFlag;
	String[] baseFieldArray;
	String[] mergeFieldArray;
	@FXML
	ComboBox<String> baseFieldCombo;
	@FXML
	ComboBox<String> mergeFieldCombo;
	@FXML
	RadioButton addToBaseRButton;
	@FXML
	RadioButton seperateMargeRButton;
	@FXML
	TextArea log;

	@FXML
	private void quitAction() {
		System.exit(0);
	}

	@FXML
	private void openBaseFile() {
		sysEncode = System.getProperty("file.encoding");
		FileChooser fc = new FileChooser();
		baseFile = fc.showOpenDialog(log.getScene().getWindow()).getAbsoluteFile();
		if (baseFile == null) {
			showAlert("�����t�@�C����I�����Ă�������");
			return;
		} else {
			fileSetFlag = false;
			filePath = baseFile.getParent();
		}
		log.appendText("�����t�@�C����" + baseFile.getAbsolutePath() + "���Z�b�g����܂����B");
		//
		String line = null;
		// �t�@�C����ύX�����Ƃ��̂��߂� combobox ���N���A
		baseFieldCombo.getItems().clear();
		mergeFieldCombo.getItems().clear();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(baseFile), "JISAutoDetect"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void openMergeFile() {

	}

	@FXML
	private void mergeAction() {

	}

	//
	//
	private String getPreffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(0, point);
		}
		return fileName;
	}

	// �A���[�g
	private void showAlert(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("�t�@�C����I�����Ă�������");
		alert.getDialogPane().setContentText(str);
		alert.showAndWait(); // �\��
	}
}
