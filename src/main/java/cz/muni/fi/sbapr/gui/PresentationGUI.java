/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.Slide;
import cz.muni.fi.sbapr.utils.RGHelper;
import cz.muni.fi.sbapr.utils.SlideArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingWorker;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.exception.ZipExceptionConstants;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public enum PresentationGUI {
    INSTANCE;

    public void setSlideTable(SlidesTableModel slideTable) {
        this.slideTable = slideTable;
    }
    private SlideArrayList slides = new SlideArrayList();

    private SlidesTableModel slideTable;
    private JFrame window;
    private boolean changed = false;

    //Move this
    private ZipFile currentFile = null;
    private boolean encrypted = false;

    public void createNew(File pptxFile) {
        try {
            RGHelper.INSTANCE.parse(pptxFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PresentationGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PresentationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void open(ZipFile zipFile) {
        try {
            if (zipFile.isValidZipFile()) {
                if (zipFile.isEncrypted()) {
                    JPanel panel = new JPanel();
                    JLabel label = new JLabel("Enter a password:");
                    JPasswordField pass = new JPasswordField(50);
                    panel.add(label);
                    panel.add(pass);
                    String[] options = new String[]{"OK", "Cancel"};
                    JOptionPane passPane = new JOptionPane(panel, JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);
                    JDialog dialog = passPane.createDialog(window, "");
                    boolean correctPass = false;
                    do {
                        dialog.show();
                        if (options[0] == passPane.getValue()) {
                            char[] password = pass.getPassword();
                            zipFile.setPassword(password);
                            encrypted = true;
                            try {
                                zipFile.getInputStream((FileHeader) zipFile.getFileHeaders().get(0));
                                correctPass = true;
                            } catch (ZipException ex) {
                                if (ex.getCode() == ZipExceptionConstants.WRONG_PASSWORD) {
                                    pass.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3));
                                    correctPass = false;
                                }
                            }
                            RGHelper.INSTANCE.getZipParams().setPassword(password);
                        } else {
                            return;
                        }
                    } while (!correctPass);
                }
                RGHelper.INSTANCE.parse(zipFile);
                currentFile = zipFile;
            }
        } catch (IOException | ZipException ex) {
            Logger.getLogger(PresentationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        slides.addAll(RGHelper.INSTANCE.parseSlides());
        slideTable.fireTableDataChanged();
    }

    public void save() {
        if (currentFile == null) {
            saveAs();
        } else {
            try {
                List<FileHeader> files = currentFile.getFileHeaders();
                for (FileHeader header : files) {
                    currentFile.removeFile(header);
                }
                currentFile.addFiles(new ArrayList<>(Arrays.asList(RGHelper.INSTANCE.getXMLFile(), RGHelper.INSTANCE.getPPTXFile())), encrypted ? RGHelper.INSTANCE.getZipParams() : new ZipParameters());
                setChanged(false);
            } catch (ZipException ex) {
                Logger.getLogger(PresentationGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveAs() {
        try {

            JFileChooser fc = new JFileChooser();
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            fc.setMultiSelectionEnabled(false);
            if (fc.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
                //TODO capture access error
                fc.getSelectedFile().delete();
                ZipFile zipFile = new ZipFile(fc.getSelectedFile());
                zipFile.createZipFile(new ArrayList<>(Arrays.asList(RGHelper.INSTANCE.getXMLFile(), RGHelper.INSTANCE.getPPTXFile())), encrypted ? RGHelper.INSTANCE.getZipParams() : new ZipParameters());
                setChanged(false);
            }
        } catch (ZipException | SecurityException ex) {
            Logger.getLogger(PresentationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exit() {
        if (PresentationGUI.INSTANCE.isChanged()) {
            Object[] options = {"Save", "Don't save", "Cancel"};
            int res = JOptionPane.showOptionDialog(window, "Would you like to save changes?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            switch (res) {
                case JOptionPane.YES_OPTION:
                    PresentationGUI.INSTANCE.save();
                    System.exit(0);
                    break;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
            }
        } else {
            System.exit(0);
        }
    }

    public void close() {
        currentFile = null;
    }

    public SlideArrayList getSlides() {
        return slides;
    }

    public void setChanged(boolean c) {
        changed = c;
    }

    public boolean isChanged() {
        return changed;
    }

    void setMainWindow(JFrame window) {
        this.window = window;
    }

    public void createSlide() {
        CreateSlideWorker createSlideWorker = new CreateSlideWorker();
        createSlideWorker.execute();
    }

    private class CreateSlideWorker extends SwingWorker<Slide, Void> {

        private final Slide slide;

        public CreateSlideWorker() {
            this.slide = new Slide();
        }

        @Override
        protected Slide doInBackground() throws Exception {
            JDialog slideWizard = new SlideEditDialog(window, slide, true);
            slideWizard.pack();
            slideWizard.setVisible(true);
            PresentationGUI.INSTANCE.getSlides().add(slide);
            return slide;
        }

        protected void done() {
            try {
                get();
                slideTable.fireTableRowsInserted(slides.size() - 1, slides.size() - 1);
                setChanged(true);
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(SlidesTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deleteSlide(int[] rows) {
        if (rows[0] != -1) {
            DeleteSlideWorker deleteSlideWorker = new DeleteSlideWorker(rows);
            deleteSlideWorker.execute();
        }
    }

    public class DeleteSlideWorker extends SwingWorker<Boolean, Void> {

        private final List<Integer> rows;
        private boolean result = true;

        public DeleteSlideWorker(int[] rows) {
            this.rows = new ArrayList<>(Arrays.stream(rows).boxed().collect(Collectors.toList()));
            Collections.sort(this.rows);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            try {
                rows.forEach(row -> {
                    Slide slide = PresentationGUI.INSTANCE.getSlides().get(row);
                    PresentationGUI.INSTANCE.getSlides().remove(slide);
                });
                //slide.getDialog().dispose();
            } catch (ArrayIndexOutOfBoundsException e) {
                result = false;
            }
            return result;
        }

        @Override
        protected void done() {
            slideTable.fireTableRowsDeleted(Collections.min(rows), Collections.max(rows));
            setChanged(true);
        }

    }

    void duplicateSlide(int selectedRow) throws CloneNotSupportedException {
        if (selectedRow != -1) {
            DuplicateSlideWorker duplicateSlideWorker = new DuplicateSlideWorker(selectedRow);
            duplicateSlideWorker.execute();
        }
    }

    private class DuplicateSlideWorker extends SwingWorker<Slide, Void> {

        private Slide slide;
        private final int row;

        public DuplicateSlideWorker(int row) {
            this.row = row;
        }

        @Override
        protected Slide doInBackground() throws Exception {
            Slide slide = PresentationGUI.INSTANCE.getSlides().get(row).clone();
            PresentationGUI.INSTANCE.getSlides().add(slide);
            return slide;
        }

        @Override
        protected void done() {
            try {
                get();
                slideTable.fireTableRowsInserted(slides.size() - 1, slides.size() - 1);
                setChanged(true);
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(SlidesTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void moveSlide(int fromRow, int toRow) {
        if (fromRow != -1 && toRow != -1) {
            MoveSlideWorker moveSlideWorker = new MoveSlideWorker(fromRow, toRow);
            moveSlideWorker.execute();
        }
    }

    public class MoveSlideWorker extends SwingWorker<Boolean, Void> {

        private final int toRow;
        private final int fromRow;
        private boolean result = true;

        public MoveSlideWorker(int fromRow, int toRow) {
            this.fromRow = fromRow;
            this.toRow = toRow;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            Slide slide = PresentationGUI.INSTANCE.getSlides().get(fromRow);
            try {
                Element from = getSlides().get(Math.max(fromRow, toRow)).getElement();
                Element to = getSlides().get(Math.min(fromRow, toRow)).getElement();
                to.getParentNode().insertBefore(from, to);
                getSlides().swap(fromRow, toRow);
                //Collections.swap(PresentationGUI.INSTANCE.getSlides(), fromRow, toRow);
            } catch (ArrayIndexOutOfBoundsException e) {
                result = false;
            }
            return result;
        }

        @Override
        protected void done() {
            PresentationGUI.INSTANCE.slideTable.fireTableRowsUpdated(Math.min(fromRow, toRow), Math.max(fromRow, toRow));
            setChanged(true);
        }

    }

    public void updateSlide(int row) {
        UpdateSlideWorker updateSlideWorker = new UpdateSlideWorker(row);
        updateSlideWorker.execute();
    }

    public class UpdateSlideWorker extends SwingWorker<Boolean, Void> {

        private final int row;
        private final Slide slide;
        private boolean result = true;

        public UpdateSlideWorker(int row) {
            this.row = row;
            this.slide = slides.get(row);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            JDialog slideWizard = new SlideEditDialog(window, slide, false);
            slideWizard.pack();
            slideWizard.setVisible(true);
            return result;
        }

        @Override
        protected void done() {
            PresentationGUI.INSTANCE.slideTable.fireTableRowsUpdated(row, row);
            setChanged(true);
        }

    }

    public void clearSlidesTable() {
        PresentationGUI.INSTANCE.getSlides().clear();
    }
}
