/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import Exceptions.TemplateParserException;
import cz.muni.fi.sbapr.Slide;
import cz.muni.fi.sbapr.utils.RGHelper;
import cz.muni.fi.sbapr.utils.SlideArrayList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Element;

public enum PresentationGUI {

    INSTANCE;

    private SlidesTableModel slideTable;
    private JFrame window;
    private boolean changed = false;

    private ZipFile currentFile = null;
    private boolean encrypted = false;

    public void setSlideTable(SlidesTableModel slideTable) {
        this.slideTable = slideTable;
    }
    private SlideArrayList slides = new SlideArrayList() {
        @Override
        public boolean remove(Object object) {
            boolean res = super.remove(object);
            slideTable.fireTableDataChanged();
            return res;
        }

        @Override
        public void clear() {
            super.clear();
            slideTable.fireTableDataChanged();
        }
    };

    public void createNew(File pptxFile) {
        try {
            RGHelper.INSTANCE.parse(pptxFile);
        } catch (TemplateParserException ex) {
            JOptionPane.showMessageDialog(window, "Couldn't parse the file, make sure if's a correct file.");
        }
    }

    public void open(ZipFile zipFile) throws IOException {
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
            throw new IOException(ex.getMessage());
        }
        slides.addAll(RGHelper.INSTANCE.parseSlides());
        slideTable.fireTableDataChanged();
    }

    public void save() throws IOException {
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
            } catch (ZipException | TemplateParserException ex) {
                throw new IOException(ex.getMessage());
            }
        }
    }

    public void saveAs() throws IOException {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            fc.setMultiSelectionEnabled(false);
            if (fc.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
                //TODO capture access error
                File file = fc.getSelectedFile();
                if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("rg")) {
                    // filename is OK as-is
                } else {
                    file = new File(file.toString() + ".rg");
                    file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".rg");
                }
                fc.getSelectedFile().delete();
                ZipFile zipFile = new ZipFile(file);
                //zipFile.addStream(RGHelper.INSTANCE.getXMLFile(), encrypted ? RGHelper.INSTANCE.getZipParams() : new ZipParameters());
                //zipFile.addStream(RGHelper.INSTANCE.getPPTXFile(), encrypted ? RGHelper.INSTANCE.getZipParams() : new ZipParameters());
                ArrayList<File> files = new ArrayList<>();
                files.add(RGHelper.INSTANCE.getPPTXFile());
                files.add(RGHelper.INSTANCE.getXMLFile());
                zipFile.createZipFile(files, encrypted ? RGHelper.INSTANCE.getZipParams() : new ZipParameters());
                setChanged(false);
            }
        } catch (ZipException | SecurityException | TemplateParserException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void setPassword() {

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a new password:");
        JPasswordField pass = new JPasswordField(50);
        JLabel labelConf = new JLabel("Confirm your password:");
        JPasswordField passConf = new JPasswordField(50);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(pass);
        panel.add(labelConf);
        panel.add(passConf);
        String[] options = new String[]{"OK", "Cancel"};
        JOptionPane passPane = new JOptionPane(panel, JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        JDialog dialog = passPane.createDialog(window, "");
        boolean correctPass = false;
        do {
            dialog.show();
            if (options[0] == passPane.getValue()) {
                char[] password = pass.getPassword();
                char[] passwordConf = passConf.getPassword();
                if (Arrays.equals(passwordConf, password)) {
                    if (password.length == 0) {
                        encrypted = false;
                    } else {
                        encrypted = true;
                    }
                    correctPass = true;
                    RGHelper.INSTANCE.getZipParams().setPassword(password);
                } else {
                    pass.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3));
                    passConf.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3));
                    correctPass = false;
                }
            } else {
                return;
            }
        } while (!correctPass);
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
                JOptionPane.showMessageDialog(window, "Error occured while creating a new slide");
            }
        }
    }

    /**
     *
     * @param rows
     */
    public void deleteSlide(int[] rows) {
        if (rows[0] != -1) {
            DeleteSlideWorker deleteSlideWorker = new DeleteSlideWorker(rows);
            deleteSlideWorker.execute();

        }
    }

    /**
     *
     */
    public class DeleteSlideWorker extends SwingWorker<Boolean, Void> {

        private final List<Integer> rows;
        private boolean result = true;

        /**
         *
         * @param rows
         */
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
                JOptionPane.showMessageDialog(window, "Error occured while duplicating a slide");
            }
        }
    }

    /**
     *
     * @param fromRow
     * @param toRow
     */
    public void moveSlide(int fromRow, int toRow) {
        if (fromRow != -1 && toRow != -1) {
            MoveSlideWorker moveSlideWorker = new MoveSlideWorker(fromRow, toRow);
            moveSlideWorker.execute();

        }
    }

    /**
     *
     */
    public class MoveSlideWorker extends SwingWorker<Boolean, Void> {

        private final int toRow;
        private final int fromRow;
        private boolean result = true;

        /**
         *
         * @param fromRow
         * @param toRow
         */
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

    /**
     *
     * @param row
     */
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
