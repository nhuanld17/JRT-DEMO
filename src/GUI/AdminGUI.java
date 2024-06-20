package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import COMPONENT.RoundedPanel;
import COMPONENT.RoundedTextField;
import SIDEFUNCTION.GeneratePayeeAddress;

public class AdminGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private String payeeName;
	private JTabbedPane tabbedPane;
	private AtomicBoolean running;
	private Thread listen;
	private RoundedTextField fullNameTextField;
	private RoundedTextField emailTextField;
	private RoundedTextField payeeAddressTextField;
	private RoundedTextField passwordTextField;
	private JCheckBox addCheckingAccCheckBox;
	private RoundedTextField checkingBalanceTextField;
	private JCheckBox addSavingAccCheckBox;
	private RoundedTextField savingBalanceTextField;
	private JCheckBox payeeAddressCheckBox;
	private JButton createClientBtn;
	private String emailRegex = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}";
	private ArrayList payeeList;
	private ArrayList<String[]> userInfoArrayList = new ArrayList<String[]>();
	private RoundedPanel clientMainPanel;
	private JPanel listUserPanel = new JPanel();;
	private JLabel[] username;
	private JLabel[] payeename;
	private JLabel[] checkingNumber;
	private JLabel[] savingNumber;
	private JLabel[] dateCreated;
	private RoundedTextField searchByPayeeNameTextField;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminGUI adminGUI = new AdminGUI("Nhuan", null, null, null);
					adminGUI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminGUI(String payeeName, Socket socket, BufferedReader reader, PrintWriter writer) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AdminGUI.class.getResource("/icon/icons8-bank-80.png")));
		setTitle("JRT BANK");
		setResizable(false);
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		this.payeeName = payeeName;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 20, 1080, 681);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// SIDE BAR
		
		JPanel sidebarPanel = new JPanel();
		sidebarPanel.setBorder(null);
		sidebarPanel.setBackground(new Color(40, 41, 33));
		sidebarPanel.setBounds(0, 0, 188, 642);
		contentPane.add(sidebarPanel);
		sidebarPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-bank-80.png")));
		lblNewLabel.setBounds(4, 1, 179, 88);
		sidebarPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("JRT BANK");
		lblNewLabel_1.setForeground(new Color(236, 236, 236));
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.BOLD, 27));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(6, 89, 176, 34);
		sidebarPanel.add(lblNewLabel_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.desktop);
		separator.setBackground(SystemColor.desktop);
		separator.setBounds(22, 137, 143, 1);
		sidebarPanel.add(separator);
		
		RoundedPanel panelDashBoardButton = new RoundedPanel(12, 3, new Color(199, 199, 199));	
		panelDashBoardButton.setBackground(new Color(250, 144, 24));
		panelDashBoardButton.setBounds(22, 207, 143, 45);
		sidebarPanel.add(panelDashBoardButton);
		panelDashBoardButton.setLayout(null);
		
		JButton dashboardButton = new JButton("Dashboard");
		dashboardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
			}
		});
		dashboardButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dashboardButton.setFocusable(false);
		dashboardButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		dashboardButton.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-home-24.png")));
		dashboardButton.setBorder(null);
		dashboardButton.setForeground(SystemColor.desktop);
		dashboardButton.setBackground(new Color(250, 144, 24));
		dashboardButton.setBounds(4, 3, 134, 36);
		panelDashBoardButton.add(dashboardButton);
		
		RoundedPanel panelCreateClientButton = new RoundedPanel(12, 3, new Color(199, 199, 199));
		panelCreateClientButton.setBackground(new Color(250, 144, 24));
		panelCreateClientButton.setBounds(22, 274, 143, 45);
		sidebarPanel.add(panelCreateClientButton);
		panelCreateClientButton.setLayout(null);
		
		JButton createClientButton = new JButton("Create Client");
		createClientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		createClientButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		createClientButton.setFocusable(false);
		createClientButton.setForeground(SystemColor.desktop);
		createClientButton.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-plus-24.png")));
		createClientButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		createClientButton.setBorder(null);
		createClientButton.setBackground(new Color(250, 144, 24));
		createClientButton.setBounds(4, 3, 134, 36);
		panelCreateClientButton.add(createClientButton);
		
		RoundedPanel panelClientsButton = new RoundedPanel(12, 3, new Color(199, 199, 199));
		panelClientsButton.setBackground(new Color(250, 144, 24));
		panelClientsButton.setBounds(22, 346, 143, 45);
		sidebarPanel.add(panelClientsButton);
		panelClientsButton.setLayout(null);
		
		JButton btnClients = new JButton("Clients");
		btnClients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(2);
			}
		});
		btnClients.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClients.setFocusable(false);
		btnClients.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-list-24.png")));
		btnClients.setForeground(SystemColor.desktop);
		btnClients.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnClients.setBorder(null);
		btnClients.setBackground(new Color(250, 144, 24));
		btnClients.setBounds(6, 3, 131, 36);
		panelClientsButton.add(btnClients);
		
		RoundedPanel panelTransactionButton = new RoundedPanel(12, 3, new Color(199, 199, 199));
		panelTransactionButton.setBackground(new Color(250, 144, 24));
		panelTransactionButton.setBounds(22, 413, 143, 45);
		sidebarPanel.add(panelTransactionButton);
		panelTransactionButton.setLayout(null);
		
		JButton btnTransaction = new JButton("Transaction");
		btnTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(3);
			}
		});
		btnTransaction.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTransaction.setFocusable(false);
		btnTransaction.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-transaction-24.png")));
		btnTransaction.setForeground(SystemColor.desktop);
		btnTransaction.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnTransaction.setBorder(null);
		btnTransaction.setBackground(new Color(250, 144, 24));
		btnTransaction.setBounds(5, 3, 132, 36);
		panelTransactionButton.add(btnTransaction);
		
		RoundedPanel panelLoggoutButton = new RoundedPanel(12, 3, new Color(199, 199, 199));
		panelLoggoutButton.setLayout(null);
		panelLoggoutButton.setBackground(new Color(250, 144, 24));
		panelLoggoutButton.setBounds(22, 482, 143, 45);
		sidebarPanel.add(panelLoggoutButton);
		
		JButton btnLoggout = new JButton("Logout");
		btnLoggout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		btnLoggout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLoggout.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-logout-24.png")));
		btnLoggout.setForeground(SystemColor.desktop);
		btnLoggout.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnLoggout.setFocusable(false);
		btnLoggout.setBorder(null);
		btnLoggout.setBackground(new Color(250, 144, 24));
		btnLoggout.setBounds(6, 3, 131, 36);
		panelLoggoutButton.add(btnLoggout);
		
		// END SIDEBAR
		
		// TAB
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(183, -27, 885, 673);
		contentPane.add(tabbedPane);
		
		JPanel dashboardPanel = new JPanel();
		dashboardPanel.setBackground(new Color(34, 30, 29));
		tabbedPane.addTab("DASHBOARD", null, dashboardPanel, null);
		dashboardPanel.setLayout(null);
		
		JPanel CreateClientPanel = new JPanel();
		CreateClientPanel.setBackground(new Color(34, 30, 29));
		tabbedPane.addTab("Create Client", null, CreateClientPanel, null);
		CreateClientPanel.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Create New Client");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2.setForeground(new Color(157, 157, 157));
		lblNewLabel_2.setBackground(new Color(157, 157, 157));
		lblNewLabel_2.setBounds(22, 19, 177, 31);
		CreateClientPanel.add(lblNewLabel_2);
		
		RoundedPanel mainPanel = new RoundedPanel(20, 0, new Color(34, 30, 29));
		mainPanel.setForeground(SystemColor.desktop);
		mainPanel.setBackground(new Color(56, 46, 45));
		mainPanel.setBounds(22, 65, 840, 560);
		CreateClientPanel.add(mainPanel);
		mainPanel.setLayout(null);
		
		JLabel lblNewLabel_2_1 = new JLabel("Full Name*   :");
		lblNewLabel_2_1.setForeground(new Color(157, 157, 157));
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1.setBackground(new Color(157, 157, 157));
		lblNewLabel_2_1.setBounds(20, 75, 117, 31);
		mainPanel.add(lblNewLabel_2_1);
		
		fullNameTextField = new RoundedTextField(8, 1, new Color(56, 46, 45));
		fullNameTextField.setForeground(SystemColor.desktop);
		fullNameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		fullNameTextField.setBorder(null);
		fullNameTextField.setBounds(185, 73, 254, 34);
		mainPanel.add(fullNameTextField);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Email Address*   :");
		lblNewLabel_2_1_1.setForeground(new Color(157, 157, 157));
		lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1_1.setBackground(new Color(157, 157, 157));
		lblNewLabel_2_1_1.setBounds(20, 153, 154, 31);
		mainPanel.add(lblNewLabel_2_1_1);
		
		emailTextField = new RoundedTextField(8, 1, new Color(56, 46, 45));
		emailTextField.setForeground(SystemColor.desktop);
		emailTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		emailTextField.setBorder(null);
		emailTextField.setBounds(185, 151, 254, 34);
		mainPanel.add(emailTextField);
		
		payeeAddressTextField = new RoundedTextField(8, 1, new Color(56, 46, 45));
		payeeAddressTextField.setEditable(false);
		payeeAddressTextField.setForeground(SystemColor.desktop);
		payeeAddressTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		payeeAddressTextField.setBackground(new Color(170, 170, 170));
		payeeAddressTextField.setBorder(null);
		payeeAddressTextField.setBounds(185, 228, 254, 34);
		mainPanel.add(payeeAddressTextField);
		
		payeeAddressCheckBox = new JCheckBox("Payee Address");
		payeeAddressCheckBox.setFocusable(false);
		payeeAddressCheckBox.setBorder(null);
		payeeAddressCheckBox.setForeground(SystemColor.text);
		payeeAddressCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		payeeAddressCheckBox.setBackground(new Color(56, 46, 45));
		payeeAddressCheckBox.setBounds(20, 231, 154, 28);
		payeeAddressCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (payeeAddressCheckBox.isSelected()) {
					if (fullNameTextField.getText().trim().isBlank()) {
						JOptionPane.showMessageDialog(null, "Please enter your full name to create a payee address");
						payeeAddressCheckBox.setSelected(false);
						return;
					}
					payeeAddressTextField.setEditable(true);
					String payeeAddress = new GeneratePayeeAddress(fullNameTextField.getText().trim()).createPayeeAddress(payeeList);
					payeeAddressTextField.setText(payeeAddress);
					payeeAddressTextField.setEditable(false);
				} else {
					payeeAddressTextField.setEditable(true);
					payeeAddressTextField.setText("");
					payeeAddressTextField.setEditable(false);
				}
			}
		});
		mainPanel.add(payeeAddressCheckBox);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("Password*   :");
		lblNewLabel_2_1_1_1.setForeground(new Color(157, 157, 157));
		lblNewLabel_2_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1_1_1.setBackground(new Color(157, 157, 157));
		lblNewLabel_2_1_1_1.setBounds(20, 304, 154, 31);
		mainPanel.add(lblNewLabel_2_1_1_1);
		
		passwordTextField = new RoundedTextField(8, 1, new Color(56, 46, 45));
		passwordTextField.setForeground(SystemColor.desktop);
		passwordTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		passwordTextField.setBorder(null);
		passwordTextField.setBounds(185, 302, 254, 34);
		mainPanel.add(passwordTextField);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(SystemColor.desktop);
		separator_1.setBackground(SystemColor.desktop);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(467, 87, 1, 233);
		mainPanel.add(separator_1);
		
		/* <--  Phần tạo số dư cho tài khoản thanh toán của client --> */
		addCheckingAccCheckBox = new JCheckBox("Add Checking Account");
		addCheckingAccCheckBox.setIconTextGap(20);
		addCheckingAccCheckBox.setForeground(SystemColor.text);
		addCheckingAccCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		addCheckingAccCheckBox.setFocusable(false);
		addCheckingAccCheckBox.setBorder(null);
		addCheckingAccCheckBox.setBackground(new Color(56, 46, 45));
		addCheckingAccCheckBox.setBounds(500, 73, 289, 28);
		addCheckingAccCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addCheckingAccCheckBox.isSelected()) {
					checkingBalanceTextField.setEditable(true);
					checkingBalanceTextField.setText("1000");
					checkingBalanceTextField.setEditable(false);
				} else {
					checkingBalanceTextField.setEditable(true);
					checkingBalanceTextField.setText("");
					checkingBalanceTextField.setEditable(false);
				}
			}
		});
		mainPanel.add(addCheckingAccCheckBox);
		
		JLabel lblNewLabel_2_1_1_2 = new JLabel("Checking Account Balance");
		lblNewLabel_2_1_1_2.setForeground(new Color(157, 157, 157));
		lblNewLabel_2_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1_1_2.setBackground(new Color(157, 157, 157));
		lblNewLabel_2_1_1_2.setBounds(510, 112, 243, 31);
		mainPanel.add(lblNewLabel_2_1_1_2);
		
		checkingBalanceTextField = new RoundedTextField(8, 1, new Color(56, 46, 45));
		checkingBalanceTextField.setEditable(false);
		checkingBalanceTextField.setForeground(SystemColor.desktop);
		checkingBalanceTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		checkingBalanceTextField.setBorder(null);
		checkingBalanceTextField.setBounds(500, 150, 254, 34);
		mainPanel.add(checkingBalanceTextField);
		
		/* <-- END Phần tạo số dư cho tài khoản thanh toán của client --> */
		
		/* <--  Phần tạo số dư cho tài khoản tiết kiệm của client --> */
		
		addSavingAccCheckBox = new JCheckBox("Add Saving Account");
		addSavingAccCheckBox.setIconTextGap(20);
		addSavingAccCheckBox.setForeground(SystemColor.text);
		addSavingAccCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		addSavingAccCheckBox.setFocusable(false);
		addSavingAccCheckBox.setBorder(null);
		addSavingAccCheckBox.setBackground(new Color(56, 46, 45));
		addSavingAccCheckBox.setBounds(500, 224, 289, 28);
		addSavingAccCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addSavingAccCheckBox.isSelected()) {
					savingBalanceTextField.setEditable(false);
					savingBalanceTextField.setText("1000");
					savingBalanceTextField.setEditable(false);
				} else {
					savingBalanceTextField.setEditable(true);
					savingBalanceTextField.setText("");
					savingBalanceTextField.setEditable(false);
				}
			}
		});
		mainPanel.add(addSavingAccCheckBox);
		
		JLabel lblNewLabel_2_1_1_2_1 = new JLabel("Saving Account Balance");
		lblNewLabel_2_1_1_2_1.setForeground(new Color(157, 157, 157));
		lblNewLabel_2_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1_1_2_1.setBackground(new Color(157, 157, 157));
		lblNewLabel_2_1_1_2_1.setBounds(510, 263, 243, 31);
		mainPanel.add(lblNewLabel_2_1_1_2_1);
		
		savingBalanceTextField = new RoundedTextField(8, 1, new Color(56, 46, 45));
		savingBalanceTextField.setEditable(false);
		savingBalanceTextField.setForeground(SystemColor.desktop);
		savingBalanceTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		savingBalanceTextField.setBorder(null);
		savingBalanceTextField.setBounds(500, 301, 254, 34);
		mainPanel.add(savingBalanceTextField);
		
		/* <-- END Phần tạo số dư cho tài khoản tiết kiệm của client --> */
		
		RoundedPanel panelAddClientsButton = new RoundedPanel(12, 1, new Color(41, 33, 30));
		panelAddClientsButton.setBackground(new Color(42, 33, 30));
		panelAddClientsButton.setBounds(247, 420, 345, 40);
		mainPanel.add(panelAddClientsButton);
		panelAddClientsButton.setLayout(null);
		
		createClientBtn = new JButton("Create New Client");
		createClientBtn.setFocusable(false);
		createClientBtn.setBorderPainted(false);
		createClientBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		createClientBtn.setForeground(new Color(236, 236, 236));
		createClientBtn.setFont(new Font("Tahoma", Font.PLAIN, 18));
		createClientBtn.setBorder(null);
		createClientBtn.setBackground(new Color(42, 33, 30));
		createClientBtn.setBounds(3, 2, 340, 35);
		createClientBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createClient();
			}
		});
		panelAddClientsButton.add(createClientBtn);
		
		
		
		// END TAB
		
		running = new AtomicBoolean(true);
		listen = new Thread(() -> {
			try {
				while (running.get() && !Thread.currentThread().isInterrupted()) {
					String message = this.reader.readLine();
					
					if (message.startsWith("LISTPAYEENAME")) {
//						System.out.println(message);
						String[] parts = message.split("_");
						
						// Chuyển đổi mảng thành ArrayList
						payeeList = new ArrayList<>();
						payeeList.clear();
						payeeList = new ArrayList<>(Arrays.asList(parts));
						payeeList.remove(0);
						
					}
					
					if (message.startsWith("LIST_USERS")) {
//						System.out.println(message); // oke
//						System.out.println(message.substring(11)); oke
						
						// TODO: In lại danh sách user nhận được từ Server và phân tách tin nhắn nhận được
						String listUser = message.substring(11);
//						System.out.println(listUser);
						String[] users = listUser.split("__");
						userInfoArrayList.clear();
						for (String user : users) {
							String[] userInfo = user.split("_");
							userInfoArrayList.add(userInfo);
						}
						
					    try {
							SwingUtilities.invokeAndWait(new Runnable() {
							    public void run() {
							        // Mã thao tác trên giao diện người dùng ở đây
							    	addUserItem();
							    }
							});
						} catch (InvocationTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (message.startsWith("SEARCHRESULT:")) {
						String str = message.substring(13);
						if (str.isBlank()) {
							listUserPanel.removeAll();
							listUserPanel.revalidate();
							listUserPanel.repaint();
						}
						// Tách chuỗi user thành từng user có các info nối nhau bằng _
						String[] users = str.split("__");
						userInfoArrayList.clear();
						for (String user : users) {
							String[] userInfo = user.split("_");
							userInfoArrayList.add(userInfo);
						}
						// Hiển thị kết quả tìm kiếm lên panel
					    try {
							SwingUtilities.invokeAndWait(new Runnable() {
							    public void run() {
							    	addUserItem();
							    }
							});
						} catch (InvocationTargetException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						
					}
					
				}
			} catch (IOException e) {
		        if (!running.get()) {
		            System.out.println("Thread is stopping because the flag was set to false.");
		        } else {
		            e.printStackTrace();
		            System.out.println("IO Exception: " + e.getMessage());
		        }
			}
		});
		listen.start();
		
		JPanel ClientPanel = new JPanel();
		ClientPanel.setBackground(new Color(34, 30, 29));
		tabbedPane.addTab("Client", null, ClientPanel, null);
		ClientPanel.setLayout(null);
		
		clientMainPanel = new RoundedPanel(20, 0, new Color(34, 30, 29));
		clientMainPanel.setBackground(new Color(56, 46, 45));
		clientMainPanel.setBounds(23, 25, 835, 603);
		ClientPanel.add(clientMainPanel);
		clientMainPanel.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("Search By Payee Name :");
		lblNewLabel_3.setForeground(SystemColor.window);
		lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblNewLabel_3.setBounds(26, 14, 226, 37);
		clientMainPanel.add(lblNewLabel_3);
		
		searchByPayeeNameTextField = new RoundedTextField(8, 1, new Color(56, 46, 45));
		searchByPayeeNameTextField.setForeground(SystemColor.desktop);
		searchByPayeeNameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		searchByPayeeNameTextField.setBorder(null);
		searchByPayeeNameTextField.setBounds(248, 15, 289, 34);
		clientMainPanel.add(searchByPayeeNameTextField);
		
		RoundedPanel searchUserPanel = new RoundedPanel(12, 3, new Color(199, 199, 199));
		searchUserPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchUserPanel.setLayout(null);
		searchUserPanel.setBackground(new Color(250, 144, 24));
		searchUserPanel.setBounds(550, 12, 132, 39);
		clientMainPanel.add(searchUserPanel);
		
		JButton searchUserButton = new JButton("Search");
		searchUserButton.setVerticalTextPosition(SwingConstants.TOP);
		searchUserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchUserButton.setIconTextGap(2);
		searchUserButton.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-search-30.png")));
		searchUserButton.setForeground(SystemColor.desktop);
		searchUserButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		searchUserButton.setFocusable(false);
		searchUserButton.setBorder(null);
		searchUserButton.setBackground(new Color(250, 144, 24));
		searchUserButton.setBounds(4, 3, 121, 32);
		searchUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		searchUserPanel.add(searchUserButton);
		

		listUserPanel.setBackground(new Color(28, 27, 28));
		listUserPanel.setBounds(27, 65, 781, 303);
		listUserPanel.setLayout(null);

//		addUserItem();
		

		

		
		// ============= TEST ==============
		
		
		/*
		 * JPanel panel = new JPanel(); panel.setBounds(10, 10, 760, 48);
		 * listUserPanel.add(panel); panel.setLayout(null);
		 * 
		 * JLabel lblNewLabel_3 = new JLabel("");
		 * lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		 * lblNewLabel_3.setIcon(new
		 * ImageIcon(AdminGUI.class.getResource("/icon/icons8-user-35.png")));
		 * lblNewLabel_3.setBounds(0, 0, 35, 48); panel.add(lblNewLabel_3);
		 * 
		 * JLabel lblNewLabel_4 = new JLabel("Hồ Sỹ Bảo Nhân");
		 * lblNewLabel_4.setForeground(new Color(17, 29, 34));
		 * lblNewLabel_4.setHorizontalAlignment(SwingConstants.LEFT);
		 * lblNewLabel_4.setFont(new Font("Calibri", Font.BOLD, 20));
		 * lblNewLabel_4.setBounds(35, 13, 178, 26); panel.add(lblNewLabel_4);
		 * 
		 * JSeparator separator_2 = new JSeparator();
		 * separator_2.setOrientation(SwingConstants.VERTICAL);
		 * separator_2.setBackground(new Color(28, 27, 28));
		 * separator_2.setForeground(new Color(28, 27, 28)); separator_2.setBounds(215,
		 * 5, 1, 37); panel.add(separator_2);
		 * 
		 * JLabel lblNewLabel_4_1 = new JLabel("hsbnhan2004");
		 * lblNewLabel_4_1.setHorizontalAlignment(SwingConstants.LEFT);
		 * lblNewLabel_4_1.setForeground(new Color(85, 104, 17));
		 * lblNewLabel_4_1.setFont(new Font("Calibri", Font.BOLD, 20));
		 * lblNewLabel_4_1.setBounds(225, 13, 139, 26); panel.add(lblNewLabel_4_1);
		 * 
		 * JSeparator separator_2_1 = new JSeparator();
		 * separator_2_1.setOrientation(SwingConstants.VERTICAL);
		 * separator_2_1.setForeground(new Color(28, 27, 28));
		 * separator_2_1.setBackground(new Color(28, 27, 28));
		 * separator_2_1.setBounds(380, 5, 1, 37); panel.add(separator_2_1);
		 * 
		 * JLabel lblNewLabel_4_1_1 = new JLabel("12345678");
		 * lblNewLabel_4_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		 * lblNewLabel_4_1_1.setForeground(new Color(202, 101, 0));
		 * lblNewLabel_4_1_1.setFont(new Font("Calibri", Font.BOLD, 20));
		 * lblNewLabel_4_1_1.setBounds(386, 13, 93, 26); panel.add(lblNewLabel_4_1_1);
		 * 
		 * JLabel lblNewLabel_4_1_1_1 = new JLabel("12345678");
		 * lblNewLabel_4_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		 * lblNewLabel_4_1_1_1.setForeground(new Color(17, 29, 34));
		 * lblNewLabel_4_1_1_1.setFont(new Font("Calibri", Font.BOLD, 20));
		 * lblNewLabel_4_1_1_1.setBounds(510, 13, 93, 26);
		 * panel.add(lblNewLabel_4_1_1_1);
		 * 
		 * JSeparator separator_2_1_1 = new JSeparator();
		 * separator_2_1_1.setOrientation(SwingConstants.VERTICAL);
		 * separator_2_1_1.setForeground(new Color(28, 27, 28));
		 * separator_2_1_1.setBackground(new Color(28, 27, 28));
		 * separator_2_1_1.setBounds(614, 5, 1, 37); panel.add(separator_2_1_1);
		 * 
		 * JLabel lblNewLabel_4_1_1_1_1 = new JLabel("28/10/2005");
		 * lblNewLabel_4_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		 * lblNewLabel_4_1_1_1_1.setForeground(new Color(17, 29, 34));
		 * lblNewLabel_4_1_1_1_1.setFont(new Font("Calibri", Font.BOLD, 20));
		 * lblNewLabel_4_1_1_1_1.setBounds(625, 13, 125, 26);
		 * panel.add(lblNewLabel_4_1_1_1_1);
		 */
		 
		// =================================
		JScrollPane listUserScrollPane = new JScrollPane(listUserPanel);
		listUserScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		listUserScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listUserScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		listUserScrollPane.setBorder(null);
		listUserScrollPane.setBounds(27, 65, 781, 303);
		clientMainPanel.add(listUserScrollPane);
		
		RoundedPanel reloadUsersPanel = new RoundedPanel(12, 3, new Color(199, 199, 199));
		reloadUsersPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		reloadUsersPanel.setLayout(null);
		reloadUsersPanel.setBackground(new Color(250, 144, 24));
		reloadUsersPanel.setBounds(692, 12, 85, 39);
		clientMainPanel.add(reloadUsersPanel);
		
		JButton reloadUsersButton = new JButton("");
		reloadUsersButton.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-reload-30.png")));
		reloadUsersButton.setVerticalAlignment(SwingConstants.TOP);
		reloadUsersButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		reloadUsersButton.setVerticalTextPosition(SwingConstants.TOP);
		reloadUsersButton.setIconTextGap(2);
		reloadUsersButton.setForeground(SystemColor.desktop);
		reloadUsersButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		reloadUsersButton.setFocusable(false);
		reloadUsersButton.setBorder(null);
		reloadUsersButton.setBackground(new Color(250, 144, 24));
		reloadUsersButton.setBounds(4, 3, 77, 32);
		reloadUsersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUI.this.writer.println("GET_ALL_USERS");
				searchByPayeeNameTextField.setText("");
			}
		});
		reloadUsersPanel.add(reloadUsersButton);
		
		
		
		JPanel Transaction = new JPanel();
		Transaction.setBackground(new Color(34, 30, 29));
		tabbedPane.addTab("Transaction", null, Transaction, null);
		Transaction.setLayout(null);
	}


	protected void search() {
		String payeename = searchByPayeeNameTextField.getText().trim();
		
		if (payeename.isBlank()) {
			JOptionPane.showMessageDialog(null, "Please enter complete information in the search box");
			return;
		}
		
		this.writer.println("SEARCH:"+payeename);
	}

	private void addUserItem() {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (userInfoArrayList.isEmpty()) {
			listUserPanel.removeAll();
			System.out.println("Kết quả tìm kiếm rỗng");
			return;
		}
		
		// In kiểm tra danh sách thông tin người dùng
		for (String[] users : userInfoArrayList) {
			for (String info : users) {
				System.out.print(info+" ");
			}
			System.out.println();
		}
	
		
		listUserPanel.removeAll();
		
		RoundedPanel[] item = new RoundedPanel[userInfoArrayList.size()];
		username = new JLabel[userInfoArrayList.size()];
		payeename = new JLabel[userInfoArrayList.size()];
		checkingNumber = new JLabel[userInfoArrayList.size()];
		savingNumber = new JLabel[userInfoArrayList.size()];
		dateCreated = new JLabel[userInfoArrayList.size()];
		
		// Tọa độ, chiều dài, chiều rộng cảu item
		int x = 10, y = 10, width = 760, height = 48;
		
		for (int i = 0; i < userInfoArrayList.size(); i++) {
			item[i] = new RoundedPanel(12, 0, Color.gray);
			item[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			item[i].setBackground(new Color(202, 202, 202));
			item[i].setBounds(x, y, width, height);
			item[i].setLayout(null);

			y += height + 10;
			
			// Tạo label chứa icon
			JLabel iconLabel = new JLabel("");
			iconLabel.setBounds(0, 0, 35, 48);
			iconLabel.setIcon(new ImageIcon(AdminGUI.class.getResource("/icon/icons8-user-35.png")));
			iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
			item[i].add(iconLabel);
			
			// Label chứa tên người dùng
			username[i] = new JLabel();
			username[i].setText(userInfoArrayList.get(i)[0]);
			username[i].setForeground(new Color(17, 29, 34));
			username[i].setHorizontalAlignment(SwingConstants.LEFT);
			username[i].setFont(new Font("Calibri", Font.BOLD, 20));
			username[i].setToolTipText(userInfoArrayList.get(i)[0]);
			username[i].setBounds(35, 13, 178, 26);
			item[i].add(username[i]);
			
			// Tạo JSeperator 
			JSeparator separator1 = new JSeparator();
			separator1.setOrientation(SwingConstants.VERTICAL);
			separator1.setBackground(new Color(28, 27, 28));
			separator1.setForeground(new Color(28, 27, 28));
			separator1.setBounds(215, 5, 1, 37);
			item[i].add(separator1);
			
			// Tạo JLabel cho payeename
			payeename[i] = new JLabel();
			payeename[i].setText(userInfoArrayList.get(i)[1]);
			payeename[i].setHorizontalAlignment(SwingConstants.LEFT);
			payeename[i].setForeground(new Color(85, 104, 17));
			payeename[i].setFont(new Font("Calibri", Font.BOLD, 20));
			payeename[i].setBounds(225, 13, 139, 26);
			item[i].add(payeename[i]);
			
			// Tạo JSeperator
			JSeparator separator2 = new JSeparator();
			separator2.setOrientation(SwingConstants.VERTICAL);
			separator2.setForeground(new Color(28, 27, 28));
			separator2.setBackground(new Color(28, 27, 28));
			separator2.setBounds(380, 5, 1, 37);
			item[i].add(separator2);
			
			// Tạo JLabel cho số tài khoản thanh toán
			checkingNumber[i] = new JLabel();
			checkingNumber[i].setText(userInfoArrayList.get(i)[2]);
			checkingNumber[i].setHorizontalAlignment(SwingConstants.LEFT);
			checkingNumber[i].setForeground(new Color(202, 101, 0));
			checkingNumber[i].setForeground(new Color(139, 0, 0));
			checkingNumber[i].setFont(new Font("Calibri", Font.BOLD, 20));
			checkingNumber[i].setBounds(386, 13, 93, 26);
			item[i].add(checkingNumber[i]);
			
			// Tạo JLabel cho số tài khoản tiết kiệm
			savingNumber[i] = new JLabel();
			savingNumber[i].setText(userInfoArrayList.get(i)[3]);
			savingNumber[i].setHorizontalAlignment(SwingConstants.RIGHT);
			savingNumber[i].setForeground(new Color(17, 29, 34));
			savingNumber[i].setFont(new Font("Calibri", Font.BOLD, 20));
			savingNumber[i].setBounds(510, 13, 93, 26);
			item[i].add(savingNumber[i]);
			
			// Tạo JSeperator
			JSeparator separator3 = new JSeparator();
			separator3.setOrientation(SwingConstants.VERTICAL);
			separator3.setForeground(new Color(28, 27, 28));
			separator3.setBackground(new Color(28, 27, 28));
			separator3.setBounds(614, 5, 1, 37);
			item[i].add(separator3);
			
			// Tạo JLabel cho ngày tạo tk
			dateCreated[i] = new JLabel();
			dateCreated[i].setText(userInfoArrayList.get(i)[4]);
			dateCreated[i].setHorizontalAlignment(SwingConstants.CENTER);
			dateCreated[i].setForeground(new Color(17, 29, 34));
			dateCreated[i].setFont(new Font("Calibri", Font.BOLD, 20));
			dateCreated[i].setBounds(625, 13, 125, 26);
			item[i].add(dateCreated[i]);
			
			listUserPanel.add(item[i]);
			listUserPanel.revalidate();
			listUserPanel.repaint();
		}
		int size = userInfoArrayList.size();
		listUserPanel.setPreferredSize(new Dimension(781, (10 + size*height + (size-1)*10 + 10)));
		
		// Thêm ActionLister cho item[], khi nhấn chọn vào
		addActionListerForItem(item);
	}

	private void addActionListerForItem(RoundedPanel[] items) {
		for (RoundedPanel item : items) {
			item.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	// Khi nhấn chuột thì đổi màu nền
	            	item.setBackground(new Color(250, 144, 24));
	            	
	            	// Thiết lập màu mặc định cho các Panel còn lại
	            	for (RoundedPanel remainItem : items) {
						if (remainItem != item) {
							remainItem.setBackground(new Color(202, 202, 202));
						}
					}
	            }
			});
		}
	}

	protected void createClient() {
		String clientFullName = fullNameTextField.getText().trim();
		String email = emailTextField.getText().trim();
		String payeeAddress = payeeAddressTextField.getText();
		String password = passwordTextField.getText();
		String checkingBalance = checkingBalanceTextField.getText();
		String savingBalance = savingBalanceTextField.getText();
		
		if (clientFullName.isBlank() || email.isBlank() || payeeAddress.isBlank() || password.isBlank()) {
			JOptionPane.showMessageDialog(null, "Please complete all information");
			return;
		}
		
		if (!email.matches(emailRegex)) {
			JOptionPane.showMessageDialog(null, "Email address is not valid");
			return;
		}
		
		double checkingInit = 0;
		double savingInit = 0;
		
		if (checkingBalance.isBlank()) {
			checkingInit = 0;
		} else {
			checkingInit = Double.valueOf(checkingBalance);
		}
		
		if (savingBalance.isBlank()) {
			savingInit = 0;
		} else {
			savingInit = Double.valueOf(savingBalance);
		}
		
		this.writer.println("CREATECLIENT_"+clientFullName+"_"+email+"_"+payeeAddress+"_"+password+"_"+checkingInit+"_"+savingInit);
		JOptionPane.showMessageDialog(null, "Create a customer successfully");
		setFormEmpty();
	}

	private void setFormEmpty() {
		fullNameTextField.setText(null);
		emailTextField.setText(null);
		payeeAddressCheckBox.setSelected(false);
		passwordTextField.setText(null);
		addCheckingAccCheckBox.setSelected(false);
		addSavingAccCheckBox.setSelected(false);
		payeeAddressTextField.setEditable(true);
		payeeAddressTextField.setText(null);
		payeeAddressTextField.setEditable(false);
		checkingBalanceTextField.setEditable(true);
		checkingBalanceTextField.setText(null);
		checkingBalanceTextField.setEditable(false);
		savingBalanceTextField.setEditable(true);
		savingBalanceTextField.setText(null);
		savingBalanceTextField.setEditable(false);
	}

	protected void logout() {
		int respone = JOptionPane.showConfirmDialog(null, "Logout ?");
		if (respone  == JOptionPane.OK_OPTION) {
			this.writer.println("LOGOUT");
			setVisible(false);
			new LoginGUI().setVisible(true);
			running.set(false);
			listen.interrupt();
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
