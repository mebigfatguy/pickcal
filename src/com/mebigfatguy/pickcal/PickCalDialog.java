/*
 * pickcal - a calendar picker panel
 * Copyright 2011-2014 MeBigFatGuy.com
 * Copyright 2011-2014 Dave Brosius
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *    
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations 
 * under the License. 
 */
package com.mebigfatguy.pickcal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class PickCalDialog extends JDialog {

	private static final long serialVersionUID = -5779259738602081996L;

	private JButton okButton;
	private JButton cancelButton;
	private final PickCalPanel pickCalPanel;
	private Set<DateSelectionListener> listeners;

	public PickCalDialog(boolean showTime) {
	    listeners = new HashSet<>();
		setTitle(PickCalBundle.getString(PickCalBundle.Key.Title));
		setLayout(new BorderLayout(4, 4));
		pickCalPanel = new PickCalPanel(showTime);
		add(pickCalPanel, BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);
		getRootPane().setDefaultButton(okButton);
		pack();
	}
	
	public void addDateSelectionListener(DateSelectionListener dsl) {
	    listeners.add(dsl);
	}
	
	public void removeDateSelectionListener(DateSelectionListener dsl) {
	    listeners.remove(dsl);
	}

	public void setDate(LocalDateTime date) {
		pickCalPanel.setDate(date);
	}

	private Component createControlPanel() {
		JPanel ctrlPanel = new JPanel();

		ctrlPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.X_AXIS));
		ctrlPanel.add(Box.createHorizontalGlue());

		cancelButton = new JButton(PickCalBundle.getString(PickCalBundle.Key.Cancel));
		okButton = new JButton(PickCalBundle.getString(PickCalBundle.Key.OK));

		SwingUtils.sizeUniformly(okButton, cancelButton);
		
		ctrlPanel.add(cancelButton);
		ctrlPanel.add(Box.createHorizontalStrut(10));
		ctrlPanel.add(okButton);
		ctrlPanel.add(Box.createHorizontalStrut(10));

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    LocalDateTime date = pickCalPanel.getDate();
			    for (DateSelectionListener dsl : listeners) {
			        dsl.dateSelected(date);
			    }
				dispose();
			}
		});

		return ctrlPanel;
	}
}
