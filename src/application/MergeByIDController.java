package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class MergeByIDController {
	File baseFile, mergeFile,saveFile;
	final ToggleGroup group = new ToggleGroup();
	String sysEncode;
	String filePath = null;
	boolean baseFileSetFlag = false;
	boolean mergeFileSetFlag = false;
	String[] baseFieldArray;
	String[] mergeFieldArray;
	List<String> baseRecordList = new ArrayList<String>();
	List<String> mergeRecordList = new ArrayList<String>();
	@FXML
	ComboBox<String> baseFieldCombo;
	@FXML
	ComboBox<String> mergeFieldCombo;
	@FXML
	RadioButton addToBaseRButton;
	@FXML
	RadioButton seperateMergeRButton;
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
		if (filePath != null) {
			fc.setInitialDirectory(new File(filePath));
		}
		baseFile = fc.showOpenDialog(log.getScene().getWindow()).getAbsoluteFile();
		if (baseFile == null) {
			showAlert("�����t�@�C����I�����Ă�������");
			return;
		} else {
			baseFileSetFlag = true;
			filePath = baseFile.getParent();
		}
		log.appendText("�����t�@�C����" + baseFile.getAbsolutePath() + "���Z�b�g����܂����B");
		//
		String line = null;
		// �t�@�C����ύX�����Ƃ��̂��߂� combobox ���N���A
		if (baseFieldCombo != null) {
			baseFieldCombo.getItems().clear();
		}
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(baseFile), "JISAutoDetect"));
			line = br.readLine();
			baseFieldArray = line.split(",");
			for (String s : baseFieldArray) {
				baseFieldCombo.getItems().add(s);
			}
			while ((line = br.readLine()) != null) {
				baseRecordList.add(line);
			}
			//
			br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void openMergeFile() {
		FileChooser fc = new FileChooser();
		if (filePath != null) {
			fc.setInitialDirectory(new File(filePath));
		}
		mergeFile = fc.showOpenDialog(log.getScene().getWindow()).getAbsoluteFile();
		if (mergeFile == null) {
			showAlert("�t���t�@�C����I�����Ă�������");
			return;
		} else {
			mergeFileSetFlag = true;
			filePath = baseFile.getParent();
		}
		log.appendText("\n�t�����t�@�C����" + mergeFile.getAbsolutePath() + "���Z�b�g����܂����B");
		String line = null;
		// �t�@�C����ύX�����Ƃ��̂��߂� combobox ���N���A
		if (mergeFieldCombo != null) {
			mergeFieldCombo.getItems().clear();
		}
		//
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(mergeFile), "JISAutoDetect"));
			line = br.readLine();
			mergeFieldArray = line.split(",");
			for (String s : mergeFieldArray) {
				mergeFieldCombo.getItems().add(s);
			}
			while ((line = br.readLine()) != null) {
				mergeRecordList.add(line);
			}
			//
			br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	private void mergeAction() {
		if(!(baseFileSetFlag)) {
			showAlert("�����t�@�C�����w�肳��Ă��܂���.");
			return;
		}
		if(!(mergeFileSetFlag)) {
			showAlert("�t�����t�@�C�����w�肳��Ă��܂���B");
			return;
		}
		String selectedBaseField  = baseFieldCombo.getValue();
		log.appendText("\n�����̃L�[�t�B�[���h�F"+selectedBaseField);
		String selectedMergeField = mergeFieldCombo.getValue();
		log.appendText("\n�t�����̃L�[�t�B�[���h�F"+selectedMergeField);
		//
//		addToBaseRButton.setToggleGroup(group);
//		addToBaseRButton.setSelected(true);
//		seperateMergeRButton.setToggleGroup(group);
		//
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(mergeFile);
		saveFile = fc.showSaveDialog(log.getScene().getWindow());
		try {
			PrintWriter ps = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), sysEncode)));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		boolean addtoFlag = addToBaseRButton.isSelected();
		if(addtoFlag) {
			addToBaseAction();
		}else {
			seperateRecordAction();
		}
	} //end of mergeAction()
	//
	private void addToBaseAction() {
		//�����ɕt�����鏈��
		
	}
	//
	private void seperateRecordAction() {
		
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
