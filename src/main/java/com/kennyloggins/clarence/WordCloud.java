/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import com.kennyloggins.clarence.util.CParser;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bill
 */
public class WordCloud extends JFrame {
    private static final long serialVersionUID = 8462848341514542290L;
    private final JFileChooser fc;
    private Map<String, Integer> wordCounts; 
    private Color outlineTargetColor;
    private Task task;
    private Thread thread;
    
    public WordCloud() {
        initComponents();
        
        fc = new JFileChooser();
        outlineTargetColor = Color.black;
        ColorPickerPanel.setBackground(outlineTargetColor);
                
        //Get defaults from main word cloud
        int maxIterations = wordCloudMain.getMaxIterations();
        int maxIterationsPerWord = wordCloudMain.getMaxIterationsPerWord();
        int maxWords = wordCloudMain.getMaxWords();
        double fontSize = wordCloudMain.getFontSize();
        double fontScaleFactor = wordCloudMain.getFontScaleFactor();
        
        MaxWordsTF.setText(Integer.toString(maxWords));
        IterationsPerWordTF.setText(Integer.toString(maxIterationsPerWord));
        IterationsTF.setText(Integer.toString(maxIterations));
        FontSizeTF.setText(Integer.toString((int)fontSize));
        FontScaleSlider.setValue((int) (200 * (fontScaleFactor - 0.5)));
        
        //Build the default font selection list
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        String[] unselectedFonts = { "Bookshelf Symbol 7", "Marlett", "MS Outlook", 
            "MS Reference Specialty", "MT Extra", "OpenSymbol", "Segoe MDL2 Assets", 
            "Symbol", "Webdings", "Wingdings", "Wingdings 2", "Wingdings 3"};
        Set<String> blacklist = new HashSet<>();
        blacklist.addAll(Arrays.asList(unselectedFonts));
        int[] defaultIndices = new int[fonts.length - blacklist.size()];
        int j = 0;
        for(int i = 0; i < fonts.length; i++) {
            if(!blacklist.contains(fonts[i]))
                defaultIndices[j++] = i;
        }
        fontList.setListData(fonts);
        fontList.setSelectedIndices(defaultIndices);
        
        wordCloudMain.initialize();
    }
    
    class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
            double progress = 0;
            while(progress < 100) {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException ie) {}
                double iterationCount = wordCloudMain.getCurrentIterationCount();
                double wordCount = wordCloudMain.getCurrentWordCount();
                
                progress = 100 * Math.max(iterationCount / wordCloudMain.getMaxIterations(), 
                        wordCount / wordCloudMain.getMaxWords());
                setProgress(Math.min((int)progress, 100));
                jProgressBar1.setValue((int)progress);
            }
            return null;
        }
    }
    
    private void updateTable() {
        String[] columnData = { "Word", "Count" };
        Object[][] rowData = new Object[wordCounts.size()][2];
        int i = 0;
        for(Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            rowData[i][0] = entry.getKey();
            rowData[i][1] = entry.getValue();
            i++;    
        }
        
        Arrays.sort(rowData, new Comparator<Object[]>() { 
            @Override
            public int compare(Object[] a, Object[] b) {
                return (Integer)a[1] > (Integer)b[1] ? -1 : a[1].equals(b[1]) ? 0 : 1;
            }
        });
        
        DefaultTableModel dtm = new DefaultTableModel(rowData, columnData);
        wordCountTable.setModel(dtm);
    }
    
    private void drawPreviewImage() {
        wordCloudMain.initialize();
        double scaleX = Double.parseDouble(HorizontalScaleTF.getText());
        double scaleY = Double.parseDouble(VerticalScaleTF.getText());
        double translateX = Double.parseDouble(HorizontalOffsetTF.getText());
        double translateY = Double.parseDouble(VerticalOffsetTF.getText());
        
        wordCloudMain.setOutlineTransform(scaleX, scaleY, translateX, translateY);
        wordCloudMain.repaint();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({ "serial" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        wordCloudMain = new com.kennyloggins.clarence.WordCloudPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        wordCloudSettingsPanel = new javax.swing.JPanel();
        MaxWordsTF = new com.kennyloggins.clarence.NumericTextField();
        IterationsTF = new com.kennyloggins.clarence.NumericTextField();
        IterationsPerWordTF = new com.kennyloggins.clarence.NumericTextField();
        FontSizeTF = new com.kennyloggins.clarence.NumericTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        LoadWordCountButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        wordCountTable = new javax.swing.JTable();
        GenerateButton = new javax.swing.JButton();
        FontScaleSlider = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        fontList = new javax.swing.JList<>();
        outlineSettingsPanel = new javax.swing.JPanel();
        loadOutlineButton = new javax.swing.JButton();
        ColorPickerPanel = new javax.swing.JPanel();
        ToleranceSlider = new javax.swing.JSlider();
        SaveButton = new javax.swing.JButton();
        VerticalScaleTF = new com.kennyloggins.clarence.NumericTextField();
        VerticalOffsetTF = new com.kennyloggins.clarence.NumericTextField();
        HorizontalScaleTF = new com.kennyloggins.clarence.NumericTextField();
        HorizontalOffsetTF = new com.kennyloggins.clarence.NumericTextField();
        ColorPickerLabel = new javax.swing.JLabel();
        HorizontalScaleLabel = new javax.swing.JLabel();
        VerticalOffsetLabel = new javax.swing.JLabel();
        VerticalScaleLabel = new javax.swing.JLabel();
        HorizontalOffsetLabel = new javax.swing.JLabel();
        ToleranceLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 749));
        setPreferredSize(new java.awt.Dimension(1280, 749));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout wordCloudMainLayout = new javax.swing.GroupLayout(wordCloudMain);
        wordCloudMain.setLayout(wordCloudMainLayout);
        wordCloudMainLayout.setHorizontalGroup(
            wordCloudMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 849, Short.MAX_VALUE)
        );
        wordCloudMainLayout.setVerticalGroup(
            wordCloudMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jProgressBar1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jProgressBar1PropertyChange(evt);
            }
        });

        wordCloudSettingsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        wordCloudSettingsPanel.setName(""); // NOI18N

        MaxWordsTF.setText("0");
        MaxWordsTF.setMinimumSize(new java.awt.Dimension(25, 25));

        IterationsTF.setText("0");
        IterationsTF.setMinimumSize(new java.awt.Dimension(25, 25));

        IterationsPerWordTF.setText("0");
        IterationsPerWordTF.setMinimumSize(new java.awt.Dimension(25, 25));

        FontSizeTF.setText("0");
        FontSizeTF.setMinimumSize(new java.awt.Dimension(25, 25));
        FontSizeTF.setPreferredSize(new java.awt.Dimension(35, 24));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Scale Factor");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Iterations per Word");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Max Words");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Font Size");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Iterations");

        LoadWordCountButton.setText("Load");
        LoadWordCountButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        LoadWordCountButton.setPreferredSize(new java.awt.Dimension(50, 32));
        LoadWordCountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadWordCountButtonActionPerformed(evt);
            }
        });

        wordCountTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Word", "Count"
            }
        ));
        wordCountTable.setAutoscrolls(false);
        wordCountTable.setDoubleBuffered(true);
        wordCountTable.setDragEnabled(true);
        wordCountTable.setEnabled(false);
        wordCountTable.setFillsViewportHeight(true);
        wordCountTable.setFocusCycleRoot(true);
        wordCountTable.setInheritsPopupMenu(true);
        wordCountTable.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(wordCountTable);

        GenerateButton.setText("Generate");
        GenerateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateButtonActionPerformed(evt);
            }
        });

        FontScaleSlider.setAutoscrolls(true);
        FontScaleSlider.setMinimumSize(new java.awt.Dimension(25, 25));
        FontScaleSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FontScaleSliderStateChanged(evt);
            }
        });

        jScrollPane1.setToolTipText("");

        fontList.setFont(new java.awt.Font("Dialog", 1, 9)); // NOI18N
        fontList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        fontList.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(fontList);

        javax.swing.GroupLayout wordCloudSettingsPanelLayout = new javax.swing.GroupLayout(wordCloudSettingsPanel);
        wordCloudSettingsPanel.setLayout(wordCloudSettingsPanelLayout);
        wordCloudSettingsPanelLayout.setHorizontalGroup(
            wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                        .addComponent(IterationsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                        .addComponent(MaxWordsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                            .addComponent(FontScaleSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                            .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                                    .addComponent(LoadWordCountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(GenerateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(1, 1, 1))
                        .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                            .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(FontSizeTF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(IterationsPerWordTF, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        wordCloudSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {GenerateButton, LoadWordCountButton});

        wordCloudSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3});

        wordCloudSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        wordCloudSettingsPanelLayout.setVerticalGroup(
            wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FontScaleSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FontSizeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IterationsPerWordTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IterationsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MaxWordsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LoadWordCountButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GenerateButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        wordCloudSettingsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {GenerateButton, LoadWordCountButton});

        jTabbedPane1.addTab("Content", wordCloudSettingsPanel);

        outlineSettingsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        loadOutlineButton.setText("Load");
        loadOutlineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadOutlineButtonActionPerformed(evt);
            }
        });

        ColorPickerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ColorPickerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ColorPickerPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ColorPickerPanelLayout = new javax.swing.GroupLayout(ColorPickerPanel);
        ColorPickerPanel.setLayout(ColorPickerPanelLayout);
        ColorPickerPanelLayout.setHorizontalGroup(
            ColorPickerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        ColorPickerPanelLayout.setVerticalGroup(
            ColorPickerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        ToleranceSlider.setMaximum(255);
        ToleranceSlider.setValue(0);
        ToleranceSlider.setPreferredSize(new java.awt.Dimension(35, 16));

        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        VerticalScaleTF.setText("1");
        VerticalScaleTF.setMinimumSize(new java.awt.Dimension(50, 25));
        VerticalScaleTF.setPreferredSize(new java.awt.Dimension(50, 25));
        VerticalScaleTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerticalScaleTFActionPerformed(evt);
            }
        });

        VerticalOffsetTF.setText("0");
        VerticalOffsetTF.setMinimumSize(new java.awt.Dimension(30, 25));
        VerticalOffsetTF.setPreferredSize(new java.awt.Dimension(50, 25));
        VerticalOffsetTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerticalOffsetTFActionPerformed(evt);
            }
        });

        HorizontalScaleTF.setText("1");
        HorizontalScaleTF.setMinimumSize(new java.awt.Dimension(30, 25));
        HorizontalScaleTF.setPreferredSize(new java.awt.Dimension(50, 25));
        HorizontalScaleTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HorizontalScaleTFActionPerformed(evt);
            }
        });

        HorizontalOffsetTF.setText("0");
        HorizontalOffsetTF.setMinimumSize(new java.awt.Dimension(30, 25));
        HorizontalOffsetTF.setPreferredSize(new java.awt.Dimension(50, 25));
        HorizontalOffsetTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HorizontalOffsetTFActionPerformed(evt);
            }
        });

        ColorPickerLabel.setText("Color Picker");

        HorizontalScaleLabel.setText("Horizontal Scale");

        VerticalOffsetLabel.setText("Vertical Offset");

        VerticalScaleLabel.setText("Vertical Scale");

        HorizontalOffsetLabel.setText("Horizontal Offset");

        ToleranceLabel.setText("Tolerance");

        javax.swing.GroupLayout outlineSettingsPanelLayout = new javax.swing.GroupLayout(outlineSettingsPanel);
        outlineSettingsPanel.setLayout(outlineSettingsPanelLayout);
        outlineSettingsPanelLayout.setHorizontalGroup(
            outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ToleranceSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(HorizontalOffsetTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(HorizontalOffsetLabel)
                            .addComponent(ToleranceLabel)))
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(ColorPickerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ColorPickerLabel))
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(VerticalOffsetTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(HorizontalScaleTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(VerticalScaleTF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(VerticalOffsetLabel)
                            .addComponent(VerticalScaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(HorizontalScaleLabel)))
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addComponent(loadOutlineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ColorPickerLabel, HorizontalOffsetLabel, HorizontalScaleLabel, ToleranceLabel, VerticalOffsetLabel, VerticalScaleLabel});

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ColorPickerPanel, HorizontalOffsetTF, HorizontalScaleTF, ToleranceSlider, VerticalOffsetTF, VerticalScaleTF});

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {SaveButton, loadOutlineButton});

        outlineSettingsPanelLayout.setVerticalGroup(
            outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ColorPickerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ColorPickerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ToleranceSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToleranceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HorizontalOffsetTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HorizontalOffsetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HorizontalScaleTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HorizontalScaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(VerticalOffsetTF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(VerticalOffsetLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(VerticalScaleTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(VerticalScaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadOutlineButton)
                    .addComponent(SaveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ColorPickerLabel, ColorPickerPanel, HorizontalOffsetLabel, HorizontalOffsetTF, HorizontalScaleLabel, HorizontalScaleTF, ToleranceLabel, ToleranceSlider, VerticalOffsetLabel, VerticalOffsetTF, VerticalScaleLabel, VerticalScaleTF});

        jTabbedPane1.addTab("Outline", outlineSettingsPanel);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 435, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Image", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wordCloudMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(wordCloudMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ColorPickerPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ColorPickerPanelMouseClicked
        outlineTargetColor = JColorChooser.showDialog(ColorPickerPanel, "Choose Color", Color.white);
        ColorPickerPanel.setBackground(outlineTargetColor);
    }//GEN-LAST:event_ColorPickerPanelMouseClicked

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        wordCloudMain.repaint();
    }//GEN-LAST:event_formComponentResized

    private void loadOutlineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadOutlineButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Image Files (*.jpg, *.png, *.gif, *.jpeg)", "jpg", "png", "gif", "jpeg"));
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Shape outline;
            try {
                outline = WordCloudPanel.getShapeFromFile(fc.getSelectedFile().getAbsolutePath(), 
                    ColorPickerPanel.getBackground(), ToleranceSlider.getValue());
            } catch(IOException e) {
                outline = null;
            }
            if(outline == null)
                JOptionPane.showMessageDialog(loadOutlineButton, 
                        "There was an error when decoding the image. Verify that you have selected the proper color and tolerance.", 
                        "An Error Occurred.", 
                        JOptionPane.INFORMATION_MESSAGE);
            else {
                wordCloudMain.setOutliningShape(outline);
                drawPreviewImage();
            }
        }
    }//GEN-LAST:event_loadOutlineButtonActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Image Files (*.jpg, *.png, *.gif, *.jpeg)", "jpg", "png", "gif", "jpeg"));
        if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile().getAbsoluteFile();
            wordCloudMain.saveImage(f);
        }
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void FontScaleSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FontScaleSliderStateChanged
        wordCloudMain.setFontScaleFactor(0.5 + ((double)FontScaleSlider.getValue() / 200));
    }//GEN-LAST:event_FontScaleSliderStateChanged

    private void GenerateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateButtonActionPerformed
        wordCloudMain.setMaxWords(Integer.parseInt(MaxWordsTF.getText()));
        wordCloudMain.setMaxIterations(Integer.parseInt(IterationsTF.getText()));
        wordCloudMain.setMaxIterationsPerWord(Integer.parseInt(IterationsPerWordTF.getText()));
        wordCloudMain.setFontSize(Integer.parseInt(FontSizeTF.getText()));
        wordCloudMain.setFonts(fontList.getSelectedValuesList());

        if(wordCounts != null) {
            wordCloudMain.setWordCounts(wordCounts);

            task = new Task();
            task.execute();

            thread = new Thread(new Runnable() { 
                public void run() {
                    wordCloudMain.generateWordCloud();
                    GenerateButton.setEnabled(true);
                }
            });

            thread.start();

            GenerateButton.setEnabled(false);
        }
    }//GEN-LAST:event_GenerateButtonActionPerformed

    private void LoadWordCountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadWordCountButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                wordCounts = CParser.getWordCounts(fc.getSelectedFile(), 4, null, null);
                updateTable();
            } catch (IOException | ParseException ex) {
                String message = ex.getClass().getName() + "\n" + ex.getMessage();
                JOptionPane.showMessageDialog(jScrollPane2,
                    message,
                    "An Error Occurred.",
                    JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(WordCloud.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_LoadWordCountButtonActionPerformed

    private void HorizontalOffsetTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HorizontalOffsetTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_HorizontalOffsetTFActionPerformed

    private void HorizontalScaleTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HorizontalScaleTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_HorizontalScaleTFActionPerformed

    private void VerticalOffsetTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerticalOffsetTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_VerticalOffsetTFActionPerformed

    private void VerticalScaleTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerticalScaleTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_VerticalScaleTFActionPerformed

    private void jProgressBar1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jProgressBar1PropertyChange
        if("progress" == evt.getPropertyName()) {
            int progress = (Integer)evt.getNewValue();
            jProgressBar1.setValue(progress);
        }
    }//GEN-LAST:event_jProgressBar1PropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WordCloud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WordCloud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WordCloud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WordCloud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WordCloud jf = new WordCloud();
                jf.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ColorPickerLabel;
    private javax.swing.JPanel ColorPickerPanel;
    private javax.swing.JSlider FontScaleSlider;
    private com.kennyloggins.clarence.NumericTextField FontSizeTF;
    private javax.swing.JButton GenerateButton;
    private javax.swing.JLabel HorizontalOffsetLabel;
    private com.kennyloggins.clarence.NumericTextField HorizontalOffsetTF;
    private javax.swing.JLabel HorizontalScaleLabel;
    private com.kennyloggins.clarence.NumericTextField HorizontalScaleTF;
    private com.kennyloggins.clarence.NumericTextField IterationsPerWordTF;
    private com.kennyloggins.clarence.NumericTextField IterationsTF;
    private javax.swing.JButton LoadWordCountButton;
    private com.kennyloggins.clarence.NumericTextField MaxWordsTF;
    private javax.swing.JButton SaveButton;
    private javax.swing.JLabel ToleranceLabel;
    private javax.swing.JSlider ToleranceSlider;
    private javax.swing.JLabel VerticalOffsetLabel;
    private com.kennyloggins.clarence.NumericTextField VerticalOffsetTF;
    private javax.swing.JLabel VerticalScaleLabel;
    private com.kennyloggins.clarence.NumericTextField VerticalScaleTF;
    private javax.swing.JList<String> fontList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loadOutlineButton;
    private javax.swing.JPanel outlineSettingsPanel;
    private com.kennyloggins.clarence.WordCloudPanel wordCloudMain;
    private javax.swing.JPanel wordCloudSettingsPanel;
    private javax.swing.JTable wordCountTable;
    // End of variables declaration//GEN-END:variables
}
