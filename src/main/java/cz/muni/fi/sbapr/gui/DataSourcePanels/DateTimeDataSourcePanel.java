/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui.DataSourcePanels;

import static cz.muni.fi.sbapr.gui.DataSourcePanels.DataSourcePanel.ERROR_BORDER;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import cz.muni.fi.sbapr.utils.RGHelper;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class DateTimeDataSourcePanel extends DataSourcePanel {

    private final static Pattern TIME_PATTERN = Pattern.compile("(\\$today)([\\+\\-]\\d+)?(?:d)?@(\\d+)h(\\d+)m(\\d+)s");
    private final static String ERROR_RESULT = "ERROR";

    /**
     * Creates new form IMGDataSourcePanel
     */
    public DateTimeDataSourcePanel() {
        super();
        initComponents();
        //updateString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonRelative = new javax.swing.JRadioButton();
        buttonCustom = new javax.swing.JRadioButton();
        buttonNow = new javax.swing.JRadioButton();
        fieldTimeZone = new javax.swing.JTextField();
        fieldFormat = new javax.swing.JTextField();
        fieldResult = new javax.swing.JTextField();
        fieldTimeString = new javax.swing.JTextField();
        spinnerDayOffset = new javax.swing.JSpinner();
        spinnerAtHour = new javax.swing.JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        spinnerAtMinute = new javax.swing.JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        spinnerAtSecond = new javax.swing.JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        setMinimumSize(new java.awt.Dimension(300, 170));
        setPreferredSize(new java.awt.Dimension(300, 170));

        buttonGroup1.add(buttonRelative);
        buttonRelative.setText("Relative");
        buttonRelative.addActionListener((java.awt.event.ActionEvent evt) -> {
            selectRelative();
        });

        buttonGroup1.add(buttonCustom);
        buttonCustom.setText("Custom");
        buttonCustom.setToolTipText("");
        buttonCustom.addActionListener((java.awt.event.ActionEvent evt) -> {
            selectCustom();
        });

        buttonGroup1.add(buttonNow);
        buttonNow.setText("Now");
        buttonNow.addActionListener((java.awt.event.ActionEvent evt) -> {
            selectNow();
            updateNow();
        });

        fieldTimeZone.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldTimeZone.setText("Europe/Prague");
        fieldTimeZone.setToolTipText("Timezone");
        fieldTimeZone.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResultField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResultField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateResultField();
            }
        });

        fieldFormat.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldFormat.setText("yyyy MM dd");
        fieldFormat.setToolTipText("Date format");
        fieldFormat.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResultField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResultField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateResultField();
            }
        });

        fieldResult.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldResult.setToolTipText("Output");
        fieldResult.setEnabled(false);

        fieldTimeString.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldTimeString.setText("$now");
        fieldTimeString.setToolTipText("");
        fieldTimeString.setEnabled(false);
        fieldTimeString.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCustom();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCustom();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCustom();
            }
        });

        spinnerDayOffset.setToolTipText("Days offset");
        spinnerDayOffset.setEnabled(false);
        spinnerDayOffset.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            updateRelative();
        });
        /*JSpinner.DefaultEditor dayOffsetEditor = (JSpinner.DefaultEditor)spinnerDayOffset.getEditor();
        dayOffsetEditor.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void removeUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void changedUpdate(DocumentEvent e) {updateRelative();}
        });*/

        spinnerAtHour.setEnabled(false);
        spinnerAtHour.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            updateRelative();
        });
        /*JSpinner.DefaultEditor atHourEditor = (JSpinner.DefaultEditor)spinnerDayOffset.getEditor();
        atHourEditor.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void removeUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void changedUpdate(DocumentEvent e) {updateRelative();}
        });*/

        spinnerAtMinute.setEnabled(false);
        spinnerAtMinute.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            updateRelative();
        });
        /*JSpinner.DefaultEditor atMinuteEditor = (JSpinner.DefaultEditor)spinnerDayOffset.getEditor();
        atMinuteEditor.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void removeUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void changedUpdate(DocumentEvent e) {updateRelative();}
        });*/

        spinnerAtSecond.setEnabled(false);
        spinnerAtSecond.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            updateRelative();
        });
        /*JSpinner.DefaultEditor atSecondEditor = (JSpinner.DefaultEditor)spinnerDayOffset.getEditor();
        atSecondEditor.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void removeUpdate(DocumentEvent e) {updateRelative();}

            @Override
            public void changedUpdate(DocumentEvent e) {updateRelative();}
        });*/

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(fieldResult)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(fieldTimeZone, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(fieldFormat))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(buttonNow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(buttonRelative, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(buttonCustom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(fieldTimeString)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(spinnerDayOffset, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(spinnerAtHour, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(spinnerAtMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(spinnerAtSecond, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fieldTimeZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fieldFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(buttonNow)
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonRelative)
                                        .addComponent(spinnerDayOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spinnerAtHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spinnerAtMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spinnerAtSecond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fieldTimeString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonCustom))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fieldResult, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                                .addGap(9, 9, 9))
        );
    }

    // Variables declaration - do not modify
    private javax.swing.JRadioButton buttonCustom;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton buttonNow;
    private javax.swing.JRadioButton buttonRelative;
    private javax.swing.JTextField fieldFormat;
    private javax.swing.JTextField fieldResult;
    private javax.swing.JTextField fieldTimeString;
    private javax.swing.JTextField fieldTimeZone;
    private javax.swing.JSpinner spinnerAtHour;
    private javax.swing.JSpinner spinnerAtMinute;
    private javax.swing.JSpinner spinnerAtSecond;
    private javax.swing.JSpinner spinnerDayOffset;
    // End of variables declaration

    private void selectNow() {
        fieldTimeString.setEnabled(false);
        spinnerDayOffset.setEnabled(false);
        spinnerAtHour.setEnabled(false);
        spinnerAtMinute.setEnabled(false);
        spinnerAtSecond.setEnabled(false);
    }

    private void updateNow() {
        fieldTimeString.setText("$now");
        updateResultField();
    }

    private void selectRelative() {
        fieldTimeString.setEnabled(false);
        spinnerDayOffset.setEnabled(true);
        spinnerAtHour.setEnabled(true);
        spinnerAtMinute.setEnabled(true);
        spinnerAtSecond.setEnabled(true);
    }

    private void updateRelative() {
        updateTimeStringField();
        updateResultField();
    }

    private void selectCustom() {
        fieldTimeString.setEnabled(true);
        spinnerDayOffset.setEnabled(false);
        spinnerAtHour.setEnabled(false);
        spinnerAtMinute.setEnabled(false);
        spinnerAtSecond.setEnabled(false);
    }

    private void updateCustom() {
        updateSpinners();
        updateResultField();
    }

    private void updateTimeStringField() {
        int offset = (Integer) spinnerDayOffset.getValue();
        int hours = (Integer) spinnerAtHour.getValue();
        int minutes = (Integer) spinnerAtMinute.getValue();
        int seconds = (Integer) spinnerAtSecond.getValue();
        fieldTimeString.setText("$today" + (offset >= 0 ? "+" : "") + offset + "@" + hours + "h" + minutes + "m" + seconds + "s");
    }

    private void updateResultField() {
        if (checkFormat() && checkTimeZone()) {
            fieldResult.setText(parseTimeString(fieldTimeString.getText()));
        } else {
            fieldResult.setText(ERROR_RESULT);
        }
    }

    private void updateSpinners() {
        Matcher matcher = TIME_PATTERN.matcher(fieldTimeString.getText());
        if (matcher.matches()) {
            spinnerDayOffset.setValue(Integer.parseInt(matcher.group(2)));
            spinnerAtHour.setValue(Integer.parseInt(matcher.group(3)));
            spinnerAtMinute.setValue(Integer.parseInt(matcher.group(4)));
            spinnerAtSecond.setValue(Integer.parseInt(matcher.group(5)));
        }

    }

    private boolean checkTimeZone() {
        try {
            ZoneId zoneId = ZoneId.of(fieldTimeZone.getText());
            fieldTimeZone.setBorder(DEFAULT_BORDER);
        } catch (DateTimeException ex) {
            fieldTimeZone.setBorder(ERROR_BORDER);
            return false;
        }
        return true;
    }

    private boolean checkFormat() {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(fieldFormat.getText());
            fieldFormat.setBorder(DEFAULT_BORDER);
        } catch (IllegalArgumentException ex) {
            fieldFormat.setBorder(ERROR_BORDER);
            return false;
        }
        return true;
    }

    private String parseTimeString(String timeString) {
        ZonedDateTime timeStamp;
        ZoneId zoneId = ZoneId.of(fieldTimeZone.getText());
        LocalDate date;
        LocalTime time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(fieldFormat.getText()).withZone(zoneId);
        if (timeString.contains("$today")) {
            Matcher matcher = TIME_PATTERN.matcher(timeString);

            if (matcher.matches()) {
                date = LocalDate.now(zoneId).plusDays(Long.parseLong(matcher.group(2)));
                time = LocalTime.of(
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)),
                        Integer.parseInt(matcher.group(5)));
                timeStamp = Instant.parse(date.toString() + "T" + time.format(DateTimeFormatter.ofPattern("hh:mm:ss")) + "Z").atZone(zoneId);
            } else {
                fieldTimeString.setBorder(ERROR_BORDER);
                return ERROR_RESULT;
            }
        } else if (timeString.contains("now")) {
            {
                timeStamp = Instant.now().atZone(zoneId);
            }
        } else {
            fieldTimeString.setBorder(ERROR_BORDER);
            return ERROR_RESULT;
        }
        fieldTimeString.setBorder(DEFAULT_BORDER);
        return dateTimeFormatter.format(timeStamp);
    }

    @Override
    public void loadElement(Element element) {
        try {
            RGHelper.INSTANCE.printXML();
        } catch (TransformerException ex) {
            Logger.getLogger(DateTimeDataSourcePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        fieldTimeZone.setText(getAttribute(element, "timeZone") == null ? "Europe/Prague" : getAttribute(element, "timeZone"));
        checkTimeZone();
        fieldFormat.setText(getAttribute(element, "format") == null ? "yyyy MM dd" : getAttribute(element, "format"));
        checkFormat();
        String timeString = getAttribute(element, "timeString");
        if (timeString == null) {
            buttonNow.setSelected(true);
            selectNow();
            updateNow();
        } else {
            fieldTimeString.setText(timeString);
            if (timeString.contains("$now")) {
                buttonNow.setSelected(true);
                selectNow();
                updateNow();
            } else if (timeString.contains("$today")) {
                buttonRelative.setSelected(true);
                updateSpinners();
                selectRelative();
                updateRelative();
            }
        }
    }

    @Override
    public boolean updateElement(Element element) {
        new IterableNodeList(element.getChildNodes()).forEach(child -> element.removeChild(child));

        Document doc = RGHelper.INSTANCE.getDoc();
        if (checkTimeZone()) {
            Element timeZoneElement = doc.createElement("timeZone");
            timeZoneElement.appendChild(doc.createTextNode(fieldTimeZone.getText()));
            element.appendChild(timeZoneElement);
        } else {
            JOptionPane.showMessageDialog(this.getParent(), "Incorrect time zone", "Time zone error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (checkFormat()) {
            Element formatElement = doc.createElement("format");
            formatElement.appendChild(doc.createTextNode(fieldFormat.getText()));
            element.appendChild(formatElement);
        } else {
            JOptionPane.showMessageDialog(this.getParent(), "Incorrect time format", "Format error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (TIME_PATTERN.matcher(fieldTimeString.getText()).matches() || fieldTimeString.getText().toLowerCase().contains("$now")) {
            Element timeStringElement = doc.createElement("timeString");
            timeStringElement.appendChild(doc.createTextNode(fieldTimeString.getText()));
            element.appendChild(timeStringElement);
        } else {
            JOptionPane.showMessageDialog(this.getParent(), "Incorrect time string", "String error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;

    }
}
