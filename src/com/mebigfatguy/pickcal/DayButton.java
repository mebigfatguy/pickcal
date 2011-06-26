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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;

public class DayButton extends JButton {

	static private Dimension preferredSize = new Dimension(50, 30);

	private final Color baseColor;
	private final Insets margin = new Insets(2, 2, 2, 2);

	public DayButton() {
		setEnabled(false);
		baseColor = getBackground();
	}

	public DayButton(String date) {
		super(date);
		baseColor = getBackground();
	}

	@Override
	public Dimension getPreferredSize() {
		return preferredSize;
	}

	public void hilite(boolean hilite) {
		if (hilite) {
			setBackground(baseColor.darker());
		} else {
			setBackground(baseColor);
		}
	}

	@Override
	public Insets getInsets() {
		return margin;
	}
}
