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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
		fc.setTitle("�����t�@�C�����w�肵�Ă�������");
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
		fc.setTitle("�t�����t�@�C�����w�肵�Ă��������B");
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
					new InputStreamReader(new FileInputStream(mergeFile), sysEncode));
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
			showAlert("�����t�@�C�����w�肳��Ă��܂���.");
			return;
		}
		if (!(mergeFileSetFlag)) {
			showAlert("�t�����t�@�C�����w�肳��Ă��܂���B");
			return;
		}
		String selectedBaseField = baseFieldCombo.getValue();
		log.appendText("\n�����̃L�[�t�B�[���h�F" + selectedBaseField);
		String selectedMergeField = mergeFieldCombo.getValue();
		log.appendText("\n�t�����̃L�[�t�B�[���h�F" + selectedMergeField);
		// radioButton �̂ǂꂪ�I������Ă��邩�ɂ�����炸�K�v�ȏ���
		// �����ƕt�����ň�v����t�B�[���h�ԍ�
		String keyAtBase = baseFieldCombo.getValue();
		String keyAtMerge = mergeFieldCombo.getValue();
		int baseKeyPos = hitNumber(baseFieldArray, keyAtBase);
		int mergeKeyPos = hitNumber(mergeFieldArray, keyAtMerge);
		// �t�B�[���h�̘A��
		// merge �t�B�[���h���� key �ȍ~�̗v�f�ɂ���csv �`��String������
		String tmpField = "";
		for (int i = mergeKeyPos + 1; i < mergeFieldArray.length; i++) {
			tmpField += ("," + mergeFieldArray[i]);
			//to check
//			System.out.println("mergeKeyPos+1="+i);
//			System.out.println("field name="+mergeFieldArray[i]);
		}
		String newField = baseFieldRecord + tmpField;
		System.out.println(newField);
	
		// �����o���p�� String List ������
		List<String> writeStringList = new ArrayList<String>();
		// �t�B�[���h�͂ǂ���ɂ���K�v
		writeStringList.add(newField);
		// �f�[�^�����R�[�h��T���Ĉ�v����ΘA���B
		// �����ɕt������ꍇ�́A�����łȂ���΋�
		// �K�v�ȋ󔒂̐��� key �ȍ~�� mergeField �̐�
		int spaceNum = mergeFieldArray.length - (mergeKeyPos + 1);
		// �������珈���𕪂���
		boolean addtoFlag = addToBaseRButton.isSelected();
		for (String s : baseRecordList) {
			boolean hit = false;
			// base ���R�[�h�̂���1�s�ɂ��ă`�F�b�N����
			// �������񃌃R�[�h��z��֕ύX
			String[] baseRecordArray = s.split(",");
			// ���̃��R�[�h�ɉ����� key �̒l
			String thisKey = baseRecordArray[baseKeyPos];
			// ���̃L�[��merge �̂��ׂẴ��R�[�h�ɂ��ă`�F�b�N
			for (String m : mergeRecordList) {
				String[] mergeRecordArray = m.split(",");
				String refKey = mergeRecordArray[mergeKeyPos];
				
				if (thisKey.equals(refKey)) {
					// mergeKeyPos �ȍ~�� refRecord�𕶎���ɁB
					String cutM = cutStringV2(mergeRecordArray, mergeKeyPos);
					hit = true;
					System.out.println(cutM);
					s = s + cutM;
					if (!addtoFlag) {
						writeStringList.add(s);
					}
				}
			} // end of for(String m:mergeRecordList
			if (addtoFlag) { // �����ɂ�������ꍇ�̓q�b�g���Ȃ����R�[�h�̏������K�v
				if (!hit) {
					for (int i = 0; i < spaceNum; i++) {
						s = s + ",";
					}
				} // end of if(hit ���Ȃ��ꍇ
					// �����o���p�̃��X�g�ɕҏW���������������B
				writeStringList.add(s);
			}
		} // end of for(String s:baseRecordList
			// �����o��
		writeString(writeStringList);
		// if (addtoFlag) {
		// addToBaseAction();
		// } else {
		// seperateRecordAction();
		// }
	} // end of mergeAction()
		//

	// �A���[�g
	private void showAlert(String str) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("�t�@�C����I�����Ă�������");
		alert.getDialogPane().setContentText(str);
		alert.showAndWait(); // �\��
	}

	// String[] ����key�ƈ�v����ꏊ��Ԃ�
	private int hitNumber(String[] array, String key) {
		int r = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(key)) {
				r = i;
			}
		}
		return r;
	}

	// �t�@�C���ɏ����o�����\�b�h
	private void writeString(List<String> str) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(filePath));
		saveFile = fc.showSaveDialog(log.getScene().getWindow());
		try {
			PrintWriter ps = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), sysEncode)));
			for (String s : str) {
				ps.println(s);
			}
			//
			log.appendText("\n���ʃt�@�C���F" + saveFile.getAbsolutePath());
			ps.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//merge ���R�[�h�� key field �������J�b�g����B
	private String cutString(String[] array, int n) {
		if (array.length == 1) {
			showAlert("���R�[�h�Ƀt�B�[���h�����������܂���B�m�F���Ă�������");
			return "";
		}
		String[] tmpArray = new String[array.length - 1];
		for (int i = 0; i < n; i++) {
			tmpArray[i] = array[i];
		}
		for (int i = (n + 1); i < array.length; i++) {
			tmpArray[i - 1] = array[i];
		}
		String r = "";
		for (String s : tmpArray) {
			r += ("," + s);
		}
		return r;
	}
	//merge ���R�[�h�� key field ���܂߂Ă�����O���J�b�g����
	private String cutStringV2(String[] array, int n) {
		if (array.length == 1) {
			showAlert("���R�[�h�Ƀt�B�[���h�����������܂���B�m�F���Ă�������");
			return "";
		}
		String[] tmpArray = new String[array.length - (n+1)];
		for (int i = 0; i < tmpArray.length; i++) {
			tmpArray[i] = array[n+1+i];
		}
//		for (int i = (n + 1); i < array.length; i++) {
//			tmpArray[i - 1] = array[i];
//		}
		String r = "";
		for (String s : tmpArray) {
			r += ("," + s);
		}
		return r;
	}
}
