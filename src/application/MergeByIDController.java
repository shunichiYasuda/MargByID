package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
		if (filePath != null) {
			fc.setInitialDirectory(new File(filePath));
		}
		baseFile = fc.showOpenDialog(log.getScene().getWindow()).getAbsoluteFile();
		if (baseFile == null) {
			showAlert("元帳ファイルを選択してください");
			return;
		} else {
			baseFileSetFlag = true;
			filePath = baseFile.getParent();
		}
		log.appendText("元帳ファイルに" + baseFile.getAbsolutePath() + "がセットされました。");
		//
		String line = null;
		// ファイルを変更したときのために combobox をクリア
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
			showAlert("付加ファイルを選択してください");
			return;
		} else {
			mergeFileSetFlag = true;
			filePath = baseFile.getParent();
		}
		log.appendText("\n付加帳ファイルに" + mergeFile.getAbsolutePath() + "がセットされました。");
		String line = null;
		// ファイルを変更したときのために combobox をクリア
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

	// アラート
	private void showAlert(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("ファイルを選択してください");
		alert.getDialogPane().setContentText(str);
		alert.showAndWait(); // 表示
	}
}
