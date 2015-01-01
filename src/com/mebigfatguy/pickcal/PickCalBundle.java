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

import java.util.ResourceBundle;

/**
 * manages the resource bundle properties file for this application
 */
public class PickCalBundle {

	/**
	 * an enumeration of all the possible entries in the bundle
	 */
	public enum Key {
		OK("pickcal.ok"),
		Cancel("pickcal.cancel"),
		Title("pickcal.title"),
		January("pickcal.jan"),
		February("pickcal.feb"),
		March("pickcal.mar"),
		April("pickcal.apr"),
		May("pickcal.may"),
		June("pickcal.jun"),
		July("pickcal.jul"),
		August("pickcal.aug"),
		September("pickcal.sep"),
		October("pickcal.oct"),
		November("pickcal.nov"),
		December("pickcal.dec"),
		Sunday("pickcal.sun"),
		Monday("pickcal.mon"),
		Tuesday("pickcal.tue"),
		Wednesday("pickcal.wed"),
		Thursday("pickcal.thu"),
		Friday("pickcal.fri"),
		Saturday("pickcal.sat"),
		AM("pickcal.am"),
		PM("pickcal.pm");

		private final String id;

		/**
		 * creates a key given the properties file name
		 * 
		 * @param id
		 *            the properties file entry name
		 */
		Key(String id) {
			this.id = id;
		}

		/**
		 * retrieves the properties file entry name for this Key
		 * 
		 * @return the properties file entry name id
		 */
		public String id() {
			return id;
		}

		@Override
		public String toString() {
			return getString(this);
		}
	};

	private static ResourceBundle bundle = ResourceBundle.getBundle("com/mebigfatguy/pickcal/resources/strings");

	/**
	 * protects this class from being instantiated as it is meant to be accessed
	 * as a static class
	 */
	private PickCalBundle() {

	}

	/**
	 * retrieves a string from a resource bundle given a key
	 * 
	 * @param key
	 *            the key of the property item that is to be retrieved
	 * @return the string representing the localized name
	 */
	public static String getString(Key key) {
		return bundle.getString(key.id());
	}
}
