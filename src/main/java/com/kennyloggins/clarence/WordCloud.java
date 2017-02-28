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
    private Set<String> blacklist;
    private Set<String> whitelist;
    private Color outlineTargetColor;
    private Task task;
    private Thread thread;
    
    public WordCloud() {
        initComponents();
        
        fc = new JFileChooser();
        outlineTargetColor = Color.black;
        colorPickerPanel.setBackground(outlineTargetColor);
                
        //Get defaults from main word cloud
        int maxIterations = wordCloudMain.getMaxIterations();
        int maxIterationsPerWord = wordCloudMain.getMaxIterationsPerWord();
        int maxWords = wordCloudMain.getMaxWords();
        double fontSize = wordCloudMain.getFontSize();
        double fontScaleFactor = wordCloudMain.getFontScaleFactor();
        int minimumFontSize = wordCloudMain.getMinimumFontSize();
        int stringPadding = (int)wordCloudMain.getStringPadding();
        
        maxWordsTF.setText(Integer.toString(maxWords));
        iterationsPerWordTF.setText(Integer.toString(maxIterationsPerWord));
        iterationsTF.setText(Integer.toString(maxIterations));
        fontSizeTF.setText(Integer.toString((int)fontSize));
        fontScaleSlider.setValue((int) (200 * (fontScaleFactor - 0.5)));
        minimumFontSizeTF.setText(Integer.toString(minimumFontSize));
        stringPaddingTF.setText(Integer.toString(stringPadding));
        
        //Build the default font selection list
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        String[] unselectedFonts = { "Bookshelf Symbol 7", "Marlett", "MS Outlook", 
            "MS Reference Specialty", "MT Extra", "OpenSymbol", "Segoe MDL2 Assets", 
            "Symbol", "Webdings", "Wingdings", "Wingdings 2", "Wingdings 3"};
        Set<String> fontBlacklist = new HashSet<>();
        fontBlacklist.addAll(Arrays.asList(unselectedFonts));
        int[] defaultIndices = new int[fonts.length - fontBlacklist.size()];
        int j = 0;
        for(int i = 0; i < fonts.length; i++) {
            if(!fontBlacklist.contains(fonts[i]))
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
                wordCloudProgressBar.setValue((int)progress);
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
        double scaleX = Double.parseDouble(horizontalScaleTF.getText());
        double scaleY = Double.parseDouble(verticalScaleTF.getText());
        double translateX = Double.parseDouble(horizontalOffsetTF.getText());
        double translateY = Double.parseDouble(verticalOffsetTF.getText());
        
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
        wordCloudProgressBar = new javax.swing.JProgressBar();
        settingsTabbedPane = new javax.swing.JTabbedPane();
        wordCloudSettingsPanel = new javax.swing.JPanel();
        loadWordCountButton = new javax.swing.JButton();
        wordCountScrollPanel = new javax.swing.JScrollPane();
        wordCountTable = new javax.swing.JTable();
        generateButton = new javax.swing.JButton();
        blacklistScrollPane = new javax.swing.JScrollPane();
        blacklistList = new javax.swing.JList<>();
        whitelistScrollPane = new javax.swing.JScrollPane();
        whitelistList = new javax.swing.JList<>();
        loadBlacklistButton = new javax.swing.JButton();
        loadWhitelistButton = new javax.swing.JButton();
        outlineSettingsPanel = new javax.swing.JPanel();
        loadOutlineButton = new javax.swing.JButton();
        colorPickerPanel = new javax.swing.JPanel();
        toleranceSlider = new javax.swing.JSlider();
        saveButton = new javax.swing.JButton();
        verticalScaleTF = new com.kennyloggins.clarence.NumericTextField();
        verticalOffsetTF = new com.kennyloggins.clarence.NumericTextField();
        horizontalScaleTF = new com.kennyloggins.clarence.NumericTextField();
        horizontalOffsetTF = new com.kennyloggins.clarence.NumericTextField();
        colorPickerLabel = new javax.swing.JLabel();
        horizontalScaleLabel = new javax.swing.JLabel();
        verticalOffsetLabel = new javax.swing.JLabel();
        verticalScaleLabel = new javax.swing.JLabel();
        horizontalOffsetLabel = new javax.swing.JLabel();
        toleranceLabel = new javax.swing.JLabel();
        imageSettingsPanel = new javax.swing.JPanel();
        fontListScrollPane = new javax.swing.JScrollPane();
        fontList = new javax.swing.JList<>();
        fontScaleSlider = new javax.swing.JSlider();
        scaleFactorLabel = new javax.swing.JLabel();
        fontSizeTF = new com.kennyloggins.clarence.NumericTextField();
        fontSizeLabel = new javax.swing.JLabel();
        iterationsPerWordTF = new com.kennyloggins.clarence.NumericTextField();
        iterationsPerWordLabel = new javax.swing.JLabel();
        iterationsTF = new com.kennyloggins.clarence.NumericTextField();
        iterationsLabel = new javax.swing.JLabel();
        maxWordsTF = new com.kennyloggins.clarence.NumericTextField();
        maxWordsLabel = new javax.swing.JLabel();
        minimumFontSizeTF = new com.kennyloggins.clarence.NumericTextField();
        minimumFontSizeLabel = new javax.swing.JLabel();
        stringPaddingTF = new com.kennyloggins.clarence.NumericTextField();
        stringPaddingLabel = new javax.swing.JLabel();

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
            .addGap(0, 1053, Short.MAX_VALUE)
        );
        wordCloudMainLayout.setVerticalGroup(
            wordCloudMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        wordCloudProgressBar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                wordCloudProgressBarPropertyChange(evt);
            }
        });

        wordCloudSettingsPanel.setName(""); // NOI18N

        loadWordCountButton.setText("Load");
        loadWordCountButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        loadWordCountButton.setPreferredSize(new java.awt.Dimension(50, 32));
        loadWordCountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadWordCountButtonActionPerformed(evt);
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
        wordCountScrollPanel.setViewportView(wordCountTable);

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        blacklistList.setEnabled(false);
        blacklistScrollPane.setViewportView(blacklistList);

        whitelistList.setEnabled(false);
        whitelistScrollPane.setViewportView(whitelistList);

        loadBlacklistButton.setText("Blacklist");
        loadBlacklistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadBlacklistButtonActionPerformed(evt);
            }
        });

        loadWhitelistButton.setText("Whitelist");
        loadWhitelistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadWhitelistButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout wordCloudSettingsPanelLayout = new javax.swing.GroupLayout(wordCloudSettingsPanel);
        wordCloudSettingsPanel.setLayout(wordCloudSettingsPanelLayout);
        wordCloudSettingsPanelLayout.setHorizontalGroup(
            wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                        .addComponent(loadWordCountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wordCloudSettingsPanelLayout.createSequentialGroup()
                        .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wordCloudSettingsPanelLayout.createSequentialGroup()
                                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                                        .addComponent(blacklistScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(whitelistScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(wordCountScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wordCloudSettingsPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(loadBlacklistButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(loadWhitelistButton)))
                        .addContainerGap())))
        );

        wordCloudSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {generateButton, loadBlacklistButton, loadWhitelistButton, loadWordCountButton});

        wordCloudSettingsPanelLayout.setVerticalGroup(
            wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wordCloudSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(blacklistScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(whitelistScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadBlacklistButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadWhitelistButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wordCountScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(wordCloudSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadWordCountButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generateButton))
                .addContainerGap(395, Short.MAX_VALUE))
        );

        wordCloudSettingsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {generateButton, loadBlacklistButton, loadWhitelistButton, loadWordCountButton});

        settingsTabbedPane.addTab("Content", wordCloudSettingsPanel);

        loadOutlineButton.setText("Load");
        loadOutlineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadOutlineButtonActionPerformed(evt);
            }
        });

        colorPickerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        colorPickerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorPickerPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout colorPickerPanelLayout = new javax.swing.GroupLayout(colorPickerPanel);
        colorPickerPanel.setLayout(colorPickerPanelLayout);
        colorPickerPanelLayout.setHorizontalGroup(
            colorPickerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        colorPickerPanelLayout.setVerticalGroup(
            colorPickerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        toleranceSlider.setMaximum(255);
        toleranceSlider.setValue(0);
        toleranceSlider.setPreferredSize(new java.awt.Dimension(35, 16));

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        verticalScaleTF.setText("1");
        verticalScaleTF.setMinimumSize(new java.awt.Dimension(50, 25));
        verticalScaleTF.setPreferredSize(new java.awt.Dimension(50, 25));
        verticalScaleTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verticalScaleTFActionPerformed(evt);
            }
        });

        verticalOffsetTF.setText("0");
        verticalOffsetTF.setMinimumSize(new java.awt.Dimension(30, 25));
        verticalOffsetTF.setPreferredSize(new java.awt.Dimension(50, 25));
        verticalOffsetTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verticalOffsetTFActionPerformed(evt);
            }
        });

        horizontalScaleTF.setText("1");
        horizontalScaleTF.setMinimumSize(new java.awt.Dimension(30, 25));
        horizontalScaleTF.setPreferredSize(new java.awt.Dimension(50, 25));
        horizontalScaleTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                horizontalScaleTFActionPerformed(evt);
            }
        });

        horizontalOffsetTF.setText("0");
        horizontalOffsetTF.setMinimumSize(new java.awt.Dimension(30, 25));
        horizontalOffsetTF.setPreferredSize(new java.awt.Dimension(50, 25));
        horizontalOffsetTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                horizontalOffsetTFActionPerformed(evt);
            }
        });

        colorPickerLabel.setText("Color Picker");

        horizontalScaleLabel.setText("Horizontal Scale");

        verticalOffsetLabel.setText("Vertical Offset");

        verticalScaleLabel.setText("Vertical Scale");

        horizontalOffsetLabel.setText("Horizontal Offset");

        toleranceLabel.setText("Tolerance");

        javax.swing.GroupLayout outlineSettingsPanelLayout = new javax.swing.GroupLayout(outlineSettingsPanel);
        outlineSettingsPanel.setLayout(outlineSettingsPanelLayout);
        outlineSettingsPanelLayout.setHorizontalGroup(
            outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(toleranceSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(horizontalOffsetTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(horizontalOffsetLabel)
                            .addComponent(toleranceLabel)))
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(colorPickerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPickerLabel))
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(verticalOffsetTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(horizontalScaleTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(verticalScaleTF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(verticalOffsetLabel)
                            .addComponent(verticalScaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(horizontalScaleLabel)))
                    .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                        .addComponent(loadOutlineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {colorPickerLabel, horizontalOffsetLabel, horizontalScaleLabel, toleranceLabel, verticalOffsetLabel, verticalScaleLabel});

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {colorPickerPanel, horizontalOffsetTF, horizontalScaleTF, toleranceSlider, verticalOffsetTF, verticalScaleTF});

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {loadOutlineButton, saveButton});

        outlineSettingsPanelLayout.setVerticalGroup(
            outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outlineSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(colorPickerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPickerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(toleranceSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toleranceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(horizontalOffsetTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(horizontalOffsetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(horizontalScaleTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(horizontalScaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verticalOffsetTF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(verticalOffsetLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verticalScaleTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verticalScaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlineSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadOutlineButton)
                    .addComponent(saveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outlineSettingsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {colorPickerLabel, colorPickerPanel, horizontalOffsetLabel, horizontalOffsetTF, horizontalScaleLabel, horizontalScaleTF, toleranceLabel, toleranceSlider, verticalOffsetLabel, verticalOffsetTF, verticalScaleLabel, verticalScaleTF});

        settingsTabbedPane.addTab("Outline", outlineSettingsPanel);

        fontListScrollPane.setToolTipText("");

        fontList.setFont(new java.awt.Font("Dialog", 1, 9)); // NOI18N
        fontList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        fontList.setRequestFocusEnabled(false);
        fontListScrollPane.setViewportView(fontList);

        fontScaleSlider.setAutoscrolls(true);
        fontScaleSlider.setMinimumSize(new java.awt.Dimension(25, 25));

        scaleFactorLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        scaleFactorLabel.setText("Scale Factor");

        fontSizeTF.setText("0");
        fontSizeTF.setMinimumSize(new java.awt.Dimension(25, 25));
        fontSizeTF.setPreferredSize(new java.awt.Dimension(35, 24));

        fontSizeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        fontSizeLabel.setText("Font Size");

        iterationsPerWordTF.setText("0");
        iterationsPerWordTF.setMinimumSize(new java.awt.Dimension(25, 25));

        iterationsPerWordLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        iterationsPerWordLabel.setText("Iterations per Word");

        iterationsTF.setText("0");
        iterationsTF.setMinimumSize(new java.awt.Dimension(25, 25));

        iterationsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        iterationsLabel.setText("Iterations");

        maxWordsTF.setText("0");
        maxWordsTF.setMinimumSize(new java.awt.Dimension(25, 25));

        maxWordsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        maxWordsLabel.setText("Max Words");

        minimumFontSizeTF.setText("0");

        minimumFontSizeLabel.setText("Min Font Size");

        stringPaddingTF.setText("0");

        stringPaddingLabel.setText("String Padding");

        javax.swing.GroupLayout imageSettingsPanelLayout = new javax.swing.GroupLayout(imageSettingsPanel);
        imageSettingsPanel.setLayout(imageSettingsPanelLayout);
        imageSettingsPanelLayout.setHorizontalGroup(
            imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fontListScrollPane)
                    .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                        .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                .addComponent(iterationsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(iterationsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                    .addComponent(fontScaleSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(scaleFactorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                    .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(fontSizeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(iterationsPerWordTF, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                            .addGap(5, 5, 5)
                                            .addComponent(iterationsPerWordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(fontSizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(stringPaddingTF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(minimumFontSizeTF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(maxWordsTF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(maxWordsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(stringPaddingLabel)
                                            .addComponent(minimumFontSizeLabel))))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        imageSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fontSizeLabel, iterationsLabel, iterationsPerWordLabel, maxWordsLabel, minimumFontSizeLabel, stringPaddingLabel});

        imageSettingsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fontSizeTF, iterationsPerWordTF, iterationsTF, maxWordsTF, minimumFontSizeTF, stringPaddingTF});

        imageSettingsPanelLayout.setVerticalGroup(
            imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imageSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fontListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fontScaleSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scaleFactorLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fontSizeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fontSizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iterationsPerWordTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iterationsPerWordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iterationsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iterationsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maxWordsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxWordsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minimumFontSizeTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minimumFontSizeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(imageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stringPaddingTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stringPaddingLabel))
                .addContainerGap(373, Short.MAX_VALUE))
        );

        imageSettingsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fontSizeLabel, iterationsLabel, iterationsPerWordLabel, maxWordsLabel, minimumFontSizeLabel, stringPaddingLabel});

        imageSettingsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fontSizeTF, iterationsPerWordTF, iterationsTF, maxWordsTF, minimumFontSizeTF, stringPaddingTF});

        settingsTabbedPane.addTab("Image", imageSettingsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wordCloudProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settingsTabbedPane))
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
                        .addComponent(wordCloudProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(settingsTabbedPane)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void colorPickerPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPickerPanelMouseClicked
        outlineTargetColor = JColorChooser.showDialog(colorPickerPanel, "Choose Color", Color.white);
        colorPickerPanel.setBackground(outlineTargetColor);
    }//GEN-LAST:event_colorPickerPanelMouseClicked

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        wordCloudMain.repaint();
    }//GEN-LAST:event_formComponentResized

    private void loadOutlineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadOutlineButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Image Files (*.jpg, *.png, *.gif, *.jpeg)", "jpg", "png", "gif", "jpeg"));
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Shape outline;
            try {
                outline = WordCloudPanel.getShapeFromFile(fc.getSelectedFile().getAbsolutePath(), 
                    colorPickerPanel.getBackground(), toleranceSlider.getValue());
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

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Image Files (*.jpg, *.png, *.gif, *.jpeg)", "jpg", "png", "gif", "jpeg"));
        if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile().getAbsoluteFile();
            try {
                wordCloudMain.saveImage(f);
            } catch(IOException ex) {
                String message = ex.getClass().getName() + "\n" + ex.getMessage();
                JOptionPane.showMessageDialog(wordCountScrollPanel,
                    message,
                    "An Error Occurred.",
                    JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(WordCloud.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        wordCloudMain.setMaxWords(Integer.parseInt(maxWordsTF.getText()));
        wordCloudMain.setMaxIterations(Integer.parseInt(iterationsTF.getText()));
        wordCloudMain.setMaxIterationsPerWord(Integer.parseInt(iterationsPerWordTF.getText()));
        wordCloudMain.setFontSize(Integer.parseInt(fontSizeTF.getText()));
        wordCloudMain.setFonts(fontList.getSelectedValuesList());
        wordCloudMain.setMinimumFontSize(Integer.parseInt(minimumFontSizeTF.getText()));
        wordCloudMain.setFontScaleFactor(0.5 + ((double)fontScaleSlider.getValue() / 200));
        wordCloudMain.setStringPadding(Integer.parseInt(stringPaddingTF.getText()));
        
        if(wordCounts != null) {
            wordCloudMain.setWordCounts(wordCounts);

            task = new Task();
            task.execute();

            thread = new Thread(new Runnable() { 
                @Override
                public void run() {
                    wordCloudMain.generateWordCloud();
                    generateButton.setEnabled(true);
                }
            });

            thread.start();

            generateButton.setEnabled(false);
        }
    }//GEN-LAST:event_generateButtonActionPerformed

    private void loadWordCountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadWordCountButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                wordCounts = CParser.getWordCounts(fc.getSelectedFile(), 4, blacklist, whitelist);
                updateTable();
            } catch (IOException | ParseException ex) {
                String message = ex.getClass().getName() + "\n" + ex.getMessage();
                JOptionPane.showMessageDialog(wordCountScrollPanel,
                    message,
                    "An Error Occurred.",
                    JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(WordCloud.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_loadWordCountButtonActionPerformed

    private void horizontalOffsetTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_horizontalOffsetTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_horizontalOffsetTFActionPerformed

    private void horizontalScaleTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_horizontalScaleTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_horizontalScaleTFActionPerformed

    private void verticalOffsetTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verticalOffsetTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_verticalOffsetTFActionPerformed

    private void verticalScaleTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verticalScaleTFActionPerformed
        drawPreviewImage();
    }//GEN-LAST:event_verticalScaleTFActionPerformed

    private void wordCloudProgressBarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_wordCloudProgressBarPropertyChange
        if(evt.getPropertyName().equals("progress")) {
            int progress = (Integer)evt.getNewValue();
            wordCloudProgressBar.setValue(progress);
        }
    }//GEN-LAST:event_wordCloudProgressBarPropertyChange

    private void loadBlacklistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadBlacklistButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                blacklist = CParser.fileLinesToUppercasedStringSet(fc.getSelectedFile().getAbsoluteFile());
                String[] listData = new String[blacklist.size()];
                int i = 0;
                for(String s : blacklist)
                    listData[i++] = s;
                    
                blacklistList.setListData(listData);
            } catch (IOException ex) {
                String message = ex.getClass().getName() + "\n" + ex.getMessage();
                JOptionPane.showMessageDialog(wordCountScrollPanel,
                    message,
                    "An Error Occurred.",
                    JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(WordCloud.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_loadBlacklistButtonActionPerformed

    private void loadWhitelistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadWhitelistButtonActionPerformed
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                whitelist = CParser.fileLinesToUppercasedStringSet(fc.getSelectedFile().getAbsoluteFile());
                String[] listData = new String[whitelist.size()];
                int i = 0;
                for(String s : whitelist)
                    listData[i++] = s;
                
                whitelistList.setListData(listData);
            } catch (IOException ex) {
                String message = ex.getClass().getName() + "\n" + ex.getMessage();
                JOptionPane.showMessageDialog(wordCountScrollPanel,
                    message,
                    "An Error Occurred.",
                    JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(WordCloud.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_loadWhitelistButtonActionPerformed

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
    private javax.swing.JList<String> blacklistList;
    private javax.swing.JScrollPane blacklistScrollPane;
    private javax.swing.JLabel colorPickerLabel;
    private javax.swing.JPanel colorPickerPanel;
    private javax.swing.JList<String> fontList;
    private javax.swing.JScrollPane fontListScrollPane;
    private javax.swing.JSlider fontScaleSlider;
    private javax.swing.JLabel fontSizeLabel;
    private com.kennyloggins.clarence.NumericTextField fontSizeTF;
    private javax.swing.JButton generateButton;
    private javax.swing.JLabel horizontalOffsetLabel;
    private com.kennyloggins.clarence.NumericTextField horizontalOffsetTF;
    private javax.swing.JLabel horizontalScaleLabel;
    private com.kennyloggins.clarence.NumericTextField horizontalScaleTF;
    private javax.swing.JPanel imageSettingsPanel;
    private javax.swing.JLabel iterationsLabel;
    private javax.swing.JLabel iterationsPerWordLabel;
    private com.kennyloggins.clarence.NumericTextField iterationsPerWordTF;
    private com.kennyloggins.clarence.NumericTextField iterationsTF;
    private javax.swing.JButton loadBlacklistButton;
    private javax.swing.JButton loadOutlineButton;
    private javax.swing.JButton loadWhitelistButton;
    private javax.swing.JButton loadWordCountButton;
    private javax.swing.JLabel maxWordsLabel;
    private com.kennyloggins.clarence.NumericTextField maxWordsTF;
    private javax.swing.JLabel minimumFontSizeLabel;
    private com.kennyloggins.clarence.NumericTextField minimumFontSizeTF;
    private javax.swing.JPanel outlineSettingsPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel scaleFactorLabel;
    private javax.swing.JTabbedPane settingsTabbedPane;
    private javax.swing.JLabel stringPaddingLabel;
    private com.kennyloggins.clarence.NumericTextField stringPaddingTF;
    private javax.swing.JLabel toleranceLabel;
    private javax.swing.JSlider toleranceSlider;
    private javax.swing.JLabel verticalOffsetLabel;
    private com.kennyloggins.clarence.NumericTextField verticalOffsetTF;
    private javax.swing.JLabel verticalScaleLabel;
    private com.kennyloggins.clarence.NumericTextField verticalScaleTF;
    private javax.swing.JList<String> whitelistList;
    private javax.swing.JScrollPane whitelistScrollPane;
    private com.kennyloggins.clarence.WordCloudPanel wordCloudMain;
    private javax.swing.JProgressBar wordCloudProgressBar;
    private javax.swing.JPanel wordCloudSettingsPanel;
    private javax.swing.JScrollPane wordCountScrollPanel;
    private javax.swing.JTable wordCountTable;
    // End of variables declaration//GEN-END:variables
}
