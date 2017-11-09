/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.Slide;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import cz.muni.fi.sbapr.utils.RGHelper;
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
import java.util.zip.ZipFile;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 *
 * @author Adam
 */
public enum PresentationGUI {
    INSTANCE;

    public void setSlideTable(SlidesTableModel slideTable) {
        this.slideTable = slideTable;
    }
    private XMLSlideShow template;
    private List<Slide> slides = new ArrayList() {
        
        @Override
        public Object set(int index, Object element){
            Slide slide = (Slide) get(index);
            slide.getElement().getParentNode().replaceChild(((Slide) element).getElement(), slide.getElement());
            return super.set(index, element);
        }
        
        @Override
        public boolean add(Object element) {
            boolean res = super.add(element);
            int row = super.indexOf(element);
            if (res) {
                PresentationGUI.INSTANCE.slideTable.fireTableRowsInserted(row, row);
                RGHelper.INSTANCE.getNodeListByName("slides").get(0).appendChild(((Slide) element).getElement());
            }
            return res;
        }

        @Override
        public boolean remove(Object element) {
            int row = super.indexOf(element);
            boolean res = super.remove(element);
            if (res) {
                PresentationGUI.INSTANCE.slideTable.fireTableRowsDeleted(row, row);
                RGHelper.INSTANCE.getNodeListByName("slides").get(0).removeChild(((Slide) element).getElement());
            }
            return res;
        }

        @Override
        public void clear() {
            super.clear();
            PresentationGUI.INSTANCE.slideTable.fireTableDataChanged();
            new IterableNodeList(RGHelper.INSTANCE.getNodeListByName("slides").get(0).getChildNodes()).forEach(node -> node.getParentNode().removeChild(node));
        }
    };
    private SlidesTableModel slideTable;
    private JFrame window;
    private boolean changed = false;

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
            RGHelper.INSTANCE.parse(zipFile);
        } catch (IOException ex) {
            Logger.getLogger(PresentationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        slides.addAll(RGHelper.INSTANCE.parseSlides());
        slideTable.fireTableDataChanged();
    }

    public List<Slide> getSlides() {
        return slides;
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

        protected void done() {
            try {
                get();
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
                Collections.swap(PresentationGUI.INSTANCE.getSlides(), fromRow, toRow);
                
            } catch (ArrayIndexOutOfBoundsException e) {
                result = false;
            }
            return result;
        }

        @Override
        protected void done() {
            PresentationGUI.INSTANCE.slideTable.fireTableRowsUpdated(Math.min(fromRow, toRow), Math.max(fromRow, toRow));
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
        }

    }

    public void clearSlidesTable() {
        PresentationGUI.INSTANCE.getSlides().clear();
    }
}
