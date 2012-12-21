/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axisclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** Графический интерфейс клиента справочника
 *
 * @author Oleg Beloglazov
 */
public class MainFrame extends JFrame {
    final int DEFAULT_WIDTH = 700;
    final int DEFAULT_HEIGHT = 500;
    final String DEFAULT_TITLE = "Справочник C++";
    final String RPC_BUTTON_LABEL = "RPC взаимодействие";
    final String DOC_BUTTON_LABEL = "Докумнтно-ориентированное взаимодействие";
    final String DEFAULT_SERVICE_ENDPOINT = "http://localhost:8080/axis/AxisService.jws";
    
    DataModel mClientDataModel = new DataModel();
    ClientDocOriented mClientDoc;
    
    JTextField mFilterTextField;
    JButton mAddArticleButton;
    JButton mRemoveArticleButton;
    JButton mSaveArticleButton;
    JList<String> mArticlesList;
    JTextArea mArticleTextArea;
    
    //ButtonGroup mClientButtonGroup;
    JRadioButtonMenuItem mRpcServiseItem;
    JRadioButtonMenuItem mDocOrServiseItem;
            
    /** Стандартный конструктор 
     *
     */
    public MainFrame() {
        if(!connect()) {
            showError("Cервис не доступен");
            System.exit(1);
        }
        
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setTitle(DEFAULT_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        add(createTopComponent(), BorderLayout.NORTH);
        add(createCentralComponent(), BorderLayout.CENTER);
        setupMenu();
    }
    
    private boolean connect() {
        //String endpoint = JOptionPane.showInputDialog("Введите адрес сервиса", DEFAULT_SERVICE_ENDPOINT);
        mClientDoc = new ClientDocOriented();
        mClientDataModel.setClient(mClientDoc);
        return mClientDataModel.isServiceAvaliable();
    }
    
    /** Создать верхний компонент
     * 
     * @return верхний компонент
     */    
    private Component createTopComponent() {
        mFilterTextField = new JTextField();        
        mFilterTextField.setMaximumSize(new Dimension(1700, 25));
        mFilterTextField.addCaretListener(new FilterListener());
        
        Box topBox = Box.createHorizontalBox(); 
        topBox.setPreferredSize(new Dimension(700, 30));
        topBox.add(Box.createHorizontalStrut(10));
        topBox.add(new JLabel("Фильтр:"));
        topBox.add(Box.createHorizontalStrut(10));        
        topBox.add(mFilterTextField);
        
        return topBox;
    }
    
    /** Создать центральный компонент
     * 
     * @return центральный компонент
     */
    private Component createCentralComponent() {        
        mArticlesList = new JList(mClientDataModel);
        mArticlesList.addListSelectionListener(new SelectionListener(mArticlesList));
        
        mArticlesList.setPreferredSize(new Dimension(140, 500));
        
        mArticleTextArea = new JTextArea();
        mArticleTextArea.setFont(new Font("Serif", Font.PLAIN, 15));
        mArticleTextArea.setLineWrap(true);
        mArticleTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(mArticleTextArea);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mArticlesList, scrollPane);
        splitPane.setDividerLocation(150);
        
        mArticlesList.setSelectedIndex(0);

        return splitPane;
    }
    
    /** Создание меню
     * 
     */
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem addArticleItem = new JMenuItem("Добавить");
        fileMenu.add(addArticleItem);
        JMenuItem removeArticleItem = new JMenuItem("Удалить");
        fileMenu.add(removeArticleItem);
        JMenuItem saveArticleItem = new JMenuItem("Сохранить");
        fileMenu.add(saveArticleItem);
        
        addArticleItem.addActionListener(new AddArticleListener());
        removeArticleItem.addActionListener(new RemoveArticleListener());
        saveArticleItem.addActionListener(new SaveArticleListener());
        
        menuBar.add(fileMenu);             
        
        setJMenuBar(menuBar);
    }
    
    private String selectedArticle() {
        return mArticlesList.getSelectedValue();
    }
    
    private int selectedArticleNumber() {
        return mArticlesList.getSelectedIndex();
    }
    
    private class AddArticleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(checkConnection()) {
                String articleName = JOptionPane.showInputDialog("Введите заголовок");
                if(articleName != null && !articleName.equals("")) {
                    try {
                        mClientDataModel.addArticle(articleName);
                    } catch (Exception ex) {
                        showError(ex.getMessage());
                    }
                }
            }
        }
        
    }
    
    private class RemoveArticleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(checkConnection()) {
                String removingArticle = selectedArticle();
                if(removingArticle != null) {
                    int choise = JOptionPane.showConfirmDialog(null, "Действительно удалить " + removingArticle + "?");
                    if(choise == JOptionPane.YES_OPTION) {
                        try {
                            mClientDataModel.removeArticle(selectedArticleNumber());
                        } catch (Exception ex) {
                            showError(ex.getMessage());
                        }
                    }
                }
            }
        }
        
    }
    
    private class SaveArticleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(checkConnection()) {
                String savingArticle = selectedArticle();
                if(savingArticle != null) {
                    String articleText = mArticleTextArea.getText();                
                    try {
                        mClientDataModel.setArticleContent(selectedArticleNumber(), articleText);
                    } catch (Exception ex) {
                        showError(ex.getMessage());
                    }
                }
            }
        }
        
    }
    
    private class FilterListener implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e) {
            JTextField source = (JTextField) e.getSource();
            mClientDataModel.setFilter(source.getText());
        }
    }
    
    private class SelectionListener implements ListSelectionListener {
        
        private JList mList;
        
        public SelectionListener(JList list) {
            mList = list;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(checkConnection() && !e.getValueIsAdjusting()) {
                String articleContent = mClientDataModel.getArticleContent(mList.getSelectedIndex());
                if(articleContent != null) {
                    mArticleTextArea.setText(articleContent);
                    mArticleTextArea.setCaretPosition(0);
                } else {
                    showError("Ошибка при получении данных");
                }
            }
        }
    }
    
    private boolean checkConnection() {
        if(!mClientDataModel.isServiceAvaliable()) {
            showError("RPC сервис не доступен");
            return false;
        }
        return true;
    }
    
    private String prepareText(String text) {
        String[] strings = text.split("\n");   
        String result = "";
        for(String str : strings) {
            result += str + "\n\r";
        }
        return result;
    }
    
    private static void showError(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage);
    }
}