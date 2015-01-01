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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.Arrays;

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

class PickCalPanel extends JPanel {

    private static final long serialVersionUID = -6235383197611711852L;

    private static final PickCalBundle.Key[] MONTHS = { PickCalBundle.Key.January, PickCalBundle.Key.February,
			PickCalBundle.Key.March, PickCalBundle.Key.April, PickCalBundle.Key.May, PickCalBundle.Key.June,
			PickCalBundle.Key.July, PickCalBundle.Key.August, PickCalBundle.Key.September, PickCalBundle.Key.October,
			PickCalBundle.Key.November, PickCalBundle.Key.December };
	private static final PickCalBundle.Key[] DAYS = { PickCalBundle.Key.Sunday, PickCalBundle.Key.Monday,
			PickCalBundle.Key.Tuesday, PickCalBundle.Key.Wednesday, PickCalBundle.Key.Thursday,
			PickCalBundle.Key.Friday, PickCalBundle.Key.Saturday };

	private JComboBox<PickCalBundle.Key> monthComboBox;
	private JSpinner yearSpinner;
	private JPanel daysPanel;
	private JSpinner hourSpinner;
	private JSpinner minuteSpinner;
	private JSpinner secondSpinner;
	private JSpinner ampmSpinner;
	private DayButton selectedDayButton;
	private final boolean showTimePanel;

	public PickCalPanel() {
		this(true);
	}

	public PickCalPanel(boolean showTime) {
		showTimePanel = showTime;
		setLayout(new BorderLayout(4, 4));
		add(createMonthYearPanel(), BorderLayout.NORTH);
		add(createDaysPanel(), BorderLayout.CENTER);
		if (showTime) {
			add(createTimeControlPanel(), BorderLayout.SOUTH);
		}
		setDate(LocalDateTime.now());
	}

	public void setDate(final LocalDateTime date) {
		{
			DefaultComboBoxModel<PickCalBundle.Key> model = (DefaultComboBoxModel<PickCalBundle.Key>) monthComboBox.getModel();
			model.setSelectedItem(MONTHS[date.get(ChronoField.MONTH_OF_YEAR) - 1]);
		}
		{
			SpinnerNumberModel model = (SpinnerNumberModel) yearSpinner.getModel();
			model.setValue(Integer.valueOf(date.get(ChronoField.YEAR)));
		}

		updateDaysPanel(date);

		if (showTimePanel) {
			{
				SpinnerNumberModel hourModel = (SpinnerNumberModel) hourSpinner.getModel();
				hourModel.setValue(Integer.valueOf(date.get(ChronoField.HOUR_OF_AMPM)));
			}

			{
				SpinnerNumberModel minuteModel = (SpinnerNumberModel) minuteSpinner.getModel();
				minuteModel.setValue(Integer.valueOf(date.get(ChronoField.MINUTE_OF_HOUR)));
			}

			{
				SpinnerNumberModel secondModel = (SpinnerNumberModel) secondSpinner.getModel();
				secondModel.setValue(Integer.valueOf(date.get(ChronoField.SECOND_OF_MINUTE)));
			}

			{
				SpinnerListModel ampmModel = (SpinnerListModel) ampmSpinner.getModel();
				ampmModel.setValue((date.get(ChronoField.AMPM_OF_DAY) == 0) ? PickCalBundle.Key.AM
						: PickCalBundle.Key.PM);
			}
		}
	}

	public LocalDateTime getDate() {
	    
		Integer year = (Integer) yearSpinner.getValue();

		Month selectedMonth = Month.JANUARY.plus(monthComboBox.getSelectedIndex());

		int maxDay = selectedMonth.maxLength();
		int day = (selectedDayButton == null) ? 1 : Integer.parseInt(selectedDayButton.getText());
		if (day > maxDay) {
			day = maxDay;
		}
		
		LocalDateTime date = LocalDateTime.of(year.intValue(), selectedMonth.getValue(), day, 0, 0);

		if (showTimePanel) {
			Integer hour = (Integer) hourSpinner.getValue();
			PickCalBundle.Key key = (PickCalBundle.Key) ampmSpinner.getValue();
			int offset = key == PickCalBundle.Key.AM ? 0 : 12;
			date = date.with(ChronoField.HOUR_OF_DAY, hour.intValue() + offset);

			Integer minute = (Integer) minuteSpinner.getValue();
			date = date.with(ChronoField.MINUTE_OF_HOUR, minute.intValue());

			Integer second = (Integer) secondSpinner.getValue();
			date = date.with(ChronoField.SECOND_OF_MINUTE, second.intValue());
		}

		return date;
	}

	private Component createMonthYearPanel() {
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		p.add(Box.createHorizontalStrut(10));

		{
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

			DefaultComboBoxModel<PickCalBundle.Key> model = new DefaultComboBoxModel<>();
			for (PickCalBundle.Key m : MONTHS) {
				model.addElement(m);
			}
			monthComboBox = new JComboBox<>(model);
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

	private void updateDaysPanel(LocalDateTime date) {
		daysPanel.removeAll();
		int today = date.get(ChronoField.DAY_OF_MONTH);

		LocalDateTime first = date.with(ChronoField.DAY_OF_MONTH, 1);
		int startDay = first.get(ChronoField.DAY_OF_WEEK);
		
		int numDays = Month.of(date.get(ChronoField.MONTH_OF_YEAR)).maxLength();

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
		int day = 1 - startDay;
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < 7; x++) {

				if ((day > 0) && (day <= numDays)) {
					String dateStr = String.valueOf(day);
					DayButton button = new DayButton(dateStr);
					button.addActionListener(listener);
					if (today == day) {
						selectedDayButton = button;
					}
					button.hilite(today == day);
					daysPanel.add(button);
				} else {
					daysPanel.add(new DayButton());
				}

				day++;
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
				updateDaysPanel(getDate());
			}

		}
	}

	class YearChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			updateDaysPanel(getDate());
		}
	}
}
