/*
 * pickcal - a calendar picker panel
 * Copyright 2011 MeBigFatGuy.com
 * Copyright 2011 Dave Brosius
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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class PickCalDialog extends JDialog {

	private JButton okButton;
	private JButton cancelButton;

	public PickCalDialog() {
		setLayout(new BorderLayout(4, 4));
		add(new PickCalPanel(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);
		getRootPane().setDefaultButton(okButton);
		pack();
	}

	private Component createControlPanel() {
		JPanel ctrlPanel = new JPanel();

		ctrlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.X_AXIS));
		ctrlPanel.add(Box.createHorizontalGlue());

		cancelButton = new JButton(PickCalBundle.getString(PickCalBundle.Key.Cancel));
		okButton = new JButton(PickCalBundle.getString(PickCalBundle.Key.OK));

		SwingUtils.sizeUniformly(okButton, cancelButton);

		ctrlPanel.add(cancelButton);
		ctrlPanel.add(Box.createHorizontalStrut(10));
		ctrlPanel.add(okButton);
		ctrlPanel.add(Box.createHorizontalStrut(10));

		return ctrlPanel;
	}
}