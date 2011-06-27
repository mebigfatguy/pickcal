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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PickCalPanel extends JPanel {

	private static final PickCalBundle.Key[] MONTHS = { PickCalBundle.Key.January, PickCalBundle.Key.February,
			PickCalBundle.Key.March, PickCalBundle.Key.April, PickCalBundle.Key.May, PickCalBundle.Key.June,
			PickCalBundle.Key.July, PickCalBundle.Key.August, PickCalBundle.Key.September, PickCalBundle.Key.October,
			PickCalBundle.Key.November, PickCalBundle.Key.December };
	private static final PickCalBundle.Key[] DAYS = { PickCalBundle.Key.Sunday, PickCalBundle.Key.Monday,
			PickCalBundle.Key.Tuesday, PickCalBundle.Key.Wednesday, PickCalBundle.Key.Thursday,
			PickCalBundle.Key.Friday, PickCalBundle.Key.Saturday };

	private JComboBox monthComboBox;
	private JSpinner yearSpinner;
	private JPanel daysPanel;
	private JSpinner hourSpinner;
	private JSpinner minuteSpinner;
	private JSpinner secondSpinner;
	private JSpinner ampmSpinner;
	private DayButton selectedDayButton;

	public PickCalPanel() {
		setLayout(new BorderLayout(4, 4));
		add(createMonthYearPanel(), BorderLayout.NORTH);
		add(createDaysPanel(), BorderLayout.CENTER);
		add(createTimeControlPanel(), BorderLayout.SOUTH);
		setTime(Calendar.getInstance());
	}

	public void setTime(final Calendar cal) {
		{
			DefaultComboBoxModel model = (DefaultComboBoxModel) monthComboBox.getModel();
			model.setSelectedItem(MONTHS[cal.get(Calendar.MONTH)]);
		}
		{
			SpinnerNumberModel model = (SpinnerNumberModel) yearSpinner.getModel();
			model.setValue(Integer.valueOf(cal.get(Calendar.YEAR)));
		}

		updateDaysPanel(cal);

		{
			SpinnerNumberModel hourModel = (SpinnerNumberModel) hourSpinner.getModel();
			hourModel.setValue(Integer.valueOf(cal.get(Calendar.HOUR)));
		}

		{
			SpinnerNumberModel minuteModel = (SpinnerNumberModel) minuteSpinner.getModel();
			minuteModel.setValue(Integer.valueOf(cal.get(Calendar.MINUTE)));
		}

		{
			SpinnerNumberModel secondModel = (SpinnerNumberModel) secondSpinner.getModel();
			secondModel.setValue(Integer.valueOf(cal.get(Calendar.SECOND)));
		}

		{
			SpinnerListModel ampmModel = (SpinnerListModel) ampmSpinner.getModel();
			ampmModel.setValue((cal.get(Calendar.AM_PM) == Calendar.AM) ? PickCalBundle.Key.AM : PickCalBundle.Key.PM);
		}
	}

	public Calendar getCalendarFromPanel() {
		Calendar cal = Calendar.getInstance();
		cal.clear();

		Integer year = (Integer) yearSpinner.getValue();
		cal.set(Calendar.YEAR, year.intValue());

		cal.set(Calendar.MONTH, Calendar.JANUARY + monthComboBox.getSelectedIndex());

		int maxDay = cal.getMaximum(Calendar.DAY_OF_MONTH);
		int day = (selectedDayButton == null) ? 1 : Integer.parseInt(selectedDayButton.getText());
		if (day > maxDay) {
			day = maxDay;
		}

		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal;
	}

	private Component createMonthYearPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		p.add(Box.createHorizontalStrut(10));

		{
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

			DefaultComboBoxModel model = new DefaultComboBoxModel();
			for (PickCalBundle.Key m : MONTHS) {
				model.addElement(m);
			}
			monthComboBox = new JComboBox(model);
			monthComboBox.addItemListener(new MonthItemListener());

			p.add(monthComboBox);
		}

		p.add(Box.createHorizontalStrut(10));
		p.add(Box.createHorizontalGlue());

		{
			SpinnerNumberModel model = new SpinnerNumberModel();
			yearSpinner = new JSpinner(model);
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpinner, "####");
			yearSpinner.setEditor(editor);
			yearSpinner.addChangeListener(new YearChangeListener());

			p.add(yearSpinner);
		}

		p.add(Box.createHorizontalStrut(30));
		return p;
	}

	private Component createDaysPanel() {
		daysPanel = new JPanel();
		return daysPanel;
	}

	private Component createTimeControlPanel() {
		JPanel timeCtrlPanel = new JPanel();
		timeCtrlPanel.setLayout(new BoxLayout(timeCtrlPanel, BoxLayout.X_AXIS));
		timeCtrlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		timeCtrlPanel.add(Box.createHorizontalStrut(10));

		{
			SpinnerNumberModel hourModel = new SpinnerNumberModel();
			hourModel.setMinimum(Integer.valueOf(0));
			hourModel.setMaximum(Integer.valueOf(11));
			hourSpinner = new JSpinner(hourModel);
			JSpinner.NumberEditor hourEditor = new JSpinner.NumberEditor(hourSpinner, "#0");
			hourSpinner.setEditor(hourEditor);
			timeCtrlPanel.add(hourSpinner);
		}

		timeCtrlPanel.add(Box.createHorizontalStrut(5));

		{
			SpinnerNumberModel minuteModel = new SpinnerNumberModel();
			minuteModel.setMinimum(Integer.valueOf(0));
			minuteModel.setMaximum(Integer.valueOf(59));
			minuteSpinner = new JSpinner(minuteModel);
			JSpinner.NumberEditor minuteEditor = new JSpinner.NumberEditor(minuteSpinner, "00");
			minuteSpinner.setEditor(minuteEditor);
			timeCtrlPanel.add(minuteSpinner);
		}

		timeCtrlPanel.add(Box.createHorizontalStrut(5));

		{
			SpinnerNumberModel secondModel = new SpinnerNumberModel();
			secondModel.setMinimum(Integer.valueOf(0));
			secondModel.setMaximum(Integer.valueOf(59));
			secondSpinner = new JSpinner(secondModel);
			JSpinner.NumberEditor secondEditor = new JSpinner.NumberEditor(secondSpinner, "00");
			secondSpinner.setEditor(secondEditor);
			timeCtrlPanel.add(secondSpinner);
		}

		timeCtrlPanel.add(Box.createHorizontalStrut(5));

		SpinnerListModel ampmModel = new SpinnerListModel();
		ampmModel.setList(Arrays.asList(PickCalBundle.Key.AM, PickCalBundle.Key.PM));
		ampmSpinner = new JSpinner(ampmModel);
		timeCtrlPanel.add(ampmSpinner);

		timeCtrlPanel.add(Box.createHorizontalGlue());

		return timeCtrlPanel;

	}

	private void updateDaysPanel(Calendar cal) {
		daysPanel.removeAll();
		int today = cal.get(Calendar.DAY_OF_MONTH);
		Calendar first = (Calendar) cal.clone();

		first.set(Calendar.DATE, 1);
		int startDay = first.get(Calendar.DAY_OF_WEEK) - 1;
		int numDays = first.getActualMaximum(Calendar.DATE);

		int rows = 1;
		int startWeek = 7 - startDay + 1;
		while (startWeek < numDays) {
			rows++;
			startWeek += 7;
		}

		daysPanel.setLayout(new GridLayout(rows + 1, 7));

		for (PickCalBundle.Key m : DAYS) {
			JLabel l = new JLabel(m.toString().substring(0, 1));
			l.setToolTipText(m.toString());
			l.setHorizontalAlignment(SwingConstants.CENTER);
			daysPanel.add(l);
		}

		DayButtonListener listener = new DayButtonListener();
		int date = 1 - startDay;
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < 7; x++) {

				if ((date > 0) && (date <= numDays)) {
					String dateStr = String.valueOf(date);
					DayButton button = new DayButton(dateStr);
					button.addActionListener(listener);
					if (today == date) {
						selectedDayButton = button;
					}
					button.hilite(today == date);
					daysPanel.add(button);
				} else {
					daysPanel.add(new DayButton());
				}

				date++;
			}
		}

		invalidate();
		revalidate();
	}

	class DayButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			selectedDayButton.hilite(false);
			selectedDayButton = (DayButton) ae.getSource();
			selectedDayButton.hilite(true);
		}
	}

	class MonthItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				updateDaysPanel(getCalendarFromPanel());
			}

		}
	}

	class YearChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			updateDaysPanel(getCalendarFromPanel());
		}
	}
}
