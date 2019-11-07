package com.hit.view;

import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

//Observable
public class CacheUnitView extends Observable implements View {
	
	private JFrame jFrame;
	private JButton b1, b2, b3;
	
	public CacheUnitView() { // C'tor
		System.out.println("In: CacheUnitView C'tor");
		jFrame = new JFrame("CacheUnitUI");
	}
	
	@Override
	public void start() {
	
		SwingUtilities.invokeLater(new Runnable() { ///???
			@Override
			public void run() {
				System.out.println("In: CacheUnitView start");
				createUI();
			}
		});
	}
	
	@Override
	public <T> void updateUIData(T t) { // this will get the returned data from server and update the UI
		System.out.println("In: CacheUnitView updateUIData");
		
		if(t.toString().contains("Statistics")) { // Stats to be shown
			
			JOptionPane.showMessageDialog(jFrame, t.toString(), "Your request", JOptionPane.INFORMATION_MESSAGE);	
			
		}
		
		else { // DataModels to be shown
			
			if(!t.toString().equals("Succeeded!") && !t.toString().equals("Failed!")) {
				
				String arr[] = t.toString().split(",");
				String toshow = "";
			
				for(String s : arr) {
					toshow += s;
					toshow += "\n";
				}
				toshow = toshow.substring(1, toshow.length()-2);
			
				JOptionPane.showMessageDialog(jFrame, toshow, "Your request", JOptionPane.INFORMATION_MESSAGE);	
			}
			
			else 
				JOptionPane.showMessageDialog(jFrame, t.toString(), "Your request", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void createUI() {
		
		System.out.println("In: CacheUnitView createUI");
		
		/// Frame initialize
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 
		jFrame.setPreferredSize(new Dimension(700, 500)); // define the window size
		jFrame.pack();
		
		try {
			jFrame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/main/resources/images/b.jpg")))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		 
		/// "Load a Request" button
		ImageIcon loadRequestIcon = new ImageIcon("src/main/resources/images/Load a Request.gif");
		JButton loadRequest = new JButton("Load a Request", loadRequestIcon);
		loadRequest.setBounds(50, 30, 260, 110);
		loadRequest.setRolloverEnabled(true);
		jFrame.add(loadRequest);

		/// "Statistics" button
		ImageIcon statisticsIcon = new ImageIcon("src/main/resources/images/Show Statistics.gif");
		JButton statistics = new JButton("Show Statistics", statisticsIcon);
		statistics.setBounds(370, 30, 260, 110);
		statistics.setRolloverEnabled(true);
		jFrame.add(statistics);
		
		/// ©
		JLabel rights = new JLabel("All rights reserved  © Omer Naim & Lihay Morad 2018");
		rights.setBounds(196, 131, 742, 585);
		rights.setForeground(Color.WHITE);
		jFrame.add(rights);
		
		/// MenuBar
		JMenuBar menuBar = new JMenuBar();
		jFrame.setJMenuBar(menuBar);
		
		JMenu menuInfo = new JMenu("Help");
		menuBar.add(menuInfo);
		
		JMenuItem itemInfo = new JMenuItem("Information");
		menuInfo.add(itemInfo);
		
		itemInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String information = "Introduction: \n\n"
						+ "You have two buttons to use: \n"
						+ "'Load request' let you choose file (request) to send to the server\n"
						+ "'Show statistics' let you get statistics about your requests";
				JOptionPane.showMessageDialog(jFrame, information, "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JMenuItem aboutUs = new JMenuItem("About Us");
		menuInfo.add(aboutUs);
		
		aboutUs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String aboutUs = "Welcome to our MMU project \n"
						+ "The authors of this project are: Omer naim and Lihay morad\n";
				JOptionPane.showMessageDialog(jFrame, aboutUs, "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		/// loadRequest ActionListener
		loadRequest.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("In: ActionListener loadRequest");

				BufferedReader input = null;
				Gson g = new Gson();
				JsonObject json = null;
				String line, request = "";
				File selectedFile = null;

				try {
					
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("src/main/resources/requests"));
					int result = fileChooser.showOpenDialog(jFrame);
					if (result == JFileChooser.APPROVE_OPTION) {
					    selectedFile = fileChooser.getSelectedFile();
					    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					}
					
					input = new BufferedReader(new FileReader(selectedFile));

					while ((line = input.readLine()) != null) {
						System.out.println(request);
						request += line;
					}
					System.out.println(request);
					json = g.fromJson(request, JsonObject.class);

					input.close();

				} catch (IOException ex) {
					ex.printStackTrace();
				}

				setChanged();
				notifyObservers(json); //// can we just use string ??? actually need JSON?

			} // loadRequest Action Listener
		});
		
		/// Statistics ActionLisetener
		statistics.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("In: ActionListener statistics");
				
				Gson g = new Gson();
				JsonObject json = null;
				String requestStatistics = "{ \"headers\" : { \"action\": \"Statistics\" },\"body\": [ ]}";

				json = g.fromJson(requestStatistics, JsonObject.class);
				setChanged();
				notifyObservers(json); //// can we just use string ??? actually need JSON?
				
				
			}
		}); // statistics Action Listener
		
		jFrame.setLocationRelativeTo(null); // the window will open in middle of screen
		jFrame.setResizable(false);
		jFrame.setLayout(null);
		jFrame.setVisible(true); // to make the window visible
			
	}
}