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
	File baseFile, mergeFile, saveFile;
	final ToggleGroup group = new ToggleGroup();
	String sysEncode;
	String filePath = null;
	boolean baseFileSetFlag = false;
	boolean mergeFileSetFlag = false;
	String[] baseFieldArray;
	String[] mergeFieldArray;
	String baseFieldRecord;
	String mergeFieldRecord;
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
			baseFieldRecord = line;
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
			mergeFieldRecord = line;
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
		if (!(baseFileSetFlag)) {
			showAlert("元帳ファイルが指定されていません.");
			return;
		}
		if (!(mergeFileSetFlag)) {
			showAlert("付加帳ファイルが指定されていません。");
			return;
		}
		String selectedBaseField = baseFieldCombo.getValue();
		log.appendText("\n元帳のキーフィールド：" + selectedBaseField);
		String selectedMergeField = mergeFieldCombo.getValue();
		log.appendText("\n付加帳のキーフィールド：" + selectedMergeField);
		//
		// addToBaseRButton.setToggleGroup(group);
		// addToBaseRButton.setSelected(true);
		// seperateMergeRButton.setToggleGroup(group);
		//
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(filePath));
		saveFile = fc.showSaveDialog(log.getScene().getWindow());
		try {
			PrintWriter ps = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), sysEncode)));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		boolean addtoFlag = addToBaseRButton.isSelected();
		if (addtoFlag) {
			addToBaseAction();
		} else {
			seperateRecordAction();
		}
	} // end of mergeAction()
		//

	private void addToBaseAction() {
		// 元帳に付加する処理
		// System.out.println("in addToBaseAction()");
		// 元帳と付加帳で一致するフィールド番号
		String keyAtBase = baseFieldCombo.getValue();
		String keyAtMerge = mergeFieldCombo.getValue();
		int baseKeyPos = hitNumber(baseFieldArray,keyAtBase);	
		int mergeKeyPos = hitNumber(mergeFieldArray,keyAtMerge);
		// フィールドの連結
		// merge フィールドから key 以降の要素についてcsv 形式Stringをつくる
		String tmpField = "";
		for (int i = mergeKeyPos + 1; i < mergeFieldArray.length; i++) {
			tmpField += ("," + mergeFieldArray[i]);
		}
		String newField = baseFieldRecord + tmpField;
		// データレレコードを探して一致すれば連結。そうでなければ空白
		// 必要な空白の数は key 以降の mergeField の数
		int spaceNum = mergeFieldArray.length - (mergeKeyPos + 1);
		for (String s : baseRecordList) {
			boolean hit = false;
			// base レコードのこの1行について
			// いったんレコードを配列へ変更
			String[] baseRecordArray = s.split(",");
			// このレコードに於ける key の値
			String thisKey = baseRecordArray[baseKeyPos];
			// このキーをmerge のすべてのレコードについてチェック
			for (String m : mergeRecordList) {
				String[] mergeRecordArray = m.split(",");
				String refKey = mergeRecordArray[mergeKeyPos];
				if (thisKey.equals(refKey)) {
					hit = true;
					s = s + "," + m;
				}
			} //end of for(String m:mergeRecordList
			// ヒットしない場合
			if (!hit) {
				for (int i = 0; i < spaceNum; i++) {
					s += ",";
				}
			}//end of if(hit しない場合
		}//end of for(String s:baseRecordList

	}// end of addBaseAction()

	//
	private void seperateRecordAction() {
		System.out.println("in seperateRecordAction()");

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
	//String[] からkeyと一致する場所を返す
	private int hitNumber(String[] array, String key) {
		int r = 0;
		for(int i=0;i<array.length;i++) {
			if(array[i].equals(key)) {
				r = i;
			}
		}
		return r;
	}
}
