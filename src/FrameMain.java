import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;

public class FrameMain extends JFrame {

	private JPanel contentPane;
	private JFileChooser fc;
	private DefaultListModel<String> raffleListModel, winnerListModel;
	private JLabel lblCongrats;
	private ButtonIcon btnImport, btnDraw;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameMain frame = new FrameMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameMain() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(1000, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Raffle Picker v0.01");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int choice = JOptionPane.showConfirmDialog(
					null, 
					"Are you sure you want to exit?", 
					"Exit", 
					JOptionPane.YES_NO_OPTION
				);
				if (choice == JOptionPane.YES_OPTION) { //yes
					System.exit(1);
				}
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		raffleListModel = new DefaultListModel<String>();
		
		JList<String> raffleList = new JList<String>(raffleListModel);
		raffleList.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		raffleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPaneList = new JScrollPane(raffleList);
		scrollPaneList.setBounds(10, 60, 295, 385);
		scrollPaneList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.add(scrollPaneList);
		
		winnerListModel = new DefaultListModel<String>();
		
		JList<String> winnerList = new JList<String>(winnerListModel);
		winnerList.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		winnerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPaneWinners = new JScrollPane(winnerList);
		scrollPaneWinners.setBounds(691, 60, 285, 385);
		scrollPaneWinners.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.add(scrollPaneWinners);
		
		btnImport = new ButtonIcon("button_import", "");
		btnImport.setBounds(10, 18, 100, 35);
		btnImport.setBorderPainted(false);
		btnImport.setFocusPainted(false);
		btnImport.setContentAreaFilled(false);
		contentPane.add(btnImport);
		
		btnImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadList();
			}
		});
		
		JLabel lblRecentWinners = new JLabel("Recent Winners");
		lblRecentWinners.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblRecentWinners.setBounds(691, 36, 160, 13);
		contentPane.add(lblRecentWinners);
		
		btnDraw = new ButtonIcon("button_draw", "");
		btnDraw.setBounds(455, 257, 100, 35);
		btnDraw.setBorderPainted(false);
		btnDraw.setFocusPainted(false);
		btnDraw.setContentAreaFilled(false);
		contentPane.add(btnDraw);
		
		btnDraw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawWinner();
			}
		});
		
		lblCongrats = new JLabel("Hello World");
		lblCongrats.setForeground(new Color(255, 255, 255));
		lblCongrats.setOpaque(true);
		lblCongrats.setBackground(Color.decode("#FFB85F"));
		lblCongrats.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		lblCongrats.setHorizontalAlignment(SwingConstants.CENTER);
		lblCongrats.setBounds(315, 166, 366, 80);
		contentPane.add(lblCongrats);
	}
	
	private void loadList() {
		fc = new JFileChooser();
		
		int returnVal = fc.showOpenDialog(this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			raffleListModel.removeAllElements();;
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				
				while ((line = br.readLine()) != null) {
					raffleListModel.addElement(line);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void setButtonState(boolean state) {
		btnImport.setEnabled(state);
		btnDraw.setEnabled(state);
	}
	
	private void drawWinner() {
		setButtonState(false);
		
		int size = raffleListModel.getSize();
		if (size > 1) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for (int i = 0; i < 50; i++) {
							int index = ThreadLocalRandom.current().nextInt(size - 1);
							lblCongrats.setText(raffleListModel.getElementAt(index));
							Thread.sleep(50);
						}
						
						Thread.sleep(50);
						
						int index = ThreadLocalRandom.current().nextInt(size);
						
						String winner = raffleListModel.getElementAt(index);
						winnerListModel.addElement(winner);
						lblCongrats.setText(winner);
						
						raffleListModel.remove(index);
						
						setButtonState(true);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
	}
}
