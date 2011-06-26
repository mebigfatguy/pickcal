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
package com.mebigfatguy.pickcal.sample;

import java.awt.Container;

import javax.swing.JFrame;

import com.mebigfatguy.pickcal.PickCalPanel;

public class Sample extends JFrame {

	public Sample() {
		PickCalPanel p = new PickCalPanel();

		Container cp = getContentPane();
		cp.add(p);
		pack();
	}

	public static void main(String[] args) {
		Sample s = new Sample();
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.setLocationRelativeTo(null);
		s.setVisible(true);
	}
}
