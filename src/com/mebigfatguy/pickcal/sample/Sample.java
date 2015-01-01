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
package com.mebigfatguy.pickcal.sample;

import java.time.LocalDateTime;

import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.mebigfatguy.pickcal.DateSelectionListener;
import com.mebigfatguy.pickcal.PickCalDialog;

public class Sample {

	public static void main(String[] args) {
		final PickCalDialog p = new PickCalDialog((args.length == 0) || "y".equalsIgnoreCase(args[0]));
		
		DateSelectionListener dsl = new DateSelectionListener() {

            @Override
            public void dateSelected(LocalDateTime date) {
                JOptionPane.showMessageDialog(null,  "Date selected: " + date);
                p.dispose();
                System.exit(0);
            }
		};
		p.addDateSelectionListener(dsl);
		p.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		p.setLocationRelativeTo(null);
		p.setVisible(true);
	}
}
