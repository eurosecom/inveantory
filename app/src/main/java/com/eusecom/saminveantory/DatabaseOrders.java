/* Copyright (c) 2008-2009 -- CommonsWare, LLC

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
*/
	 
package com.eusecom.saminveantory;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseOrders extends SQLiteOpenHelper {
	private static final String DATABASE_NAME7="db22";
	public static final String BANKX="bankx";
	public static final String ORDERX="orderx";
	public static final String SENDX="sendx";
	public static final String DATM="datm";
	
	
	public DatabaseOrders(Context context) {
		//ta 3ka je verzia databaze, nesmiem dat nizsiu ak zvysim vymaze tabulku a znovu ju vytvori
		super(context, DATABASE_NAME7, null, 5);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db22) {
		
		db22.execSQL("CREATE TABLE orders (_id INTEGER PRIMARY KEY AUTOINCREMENT, bankx TEXT, " +
				"orderx TEXT, sendx TEXT, datm TIMESTAMP(14) DEFAULT CURRENT_TIMESTAMP);");
		
		//ContentValues cv7=new ContentValues();
		
		//cv7.put(BANKX, "1");
		//cv7.put(ORDERX, "1");
		//cv7.put(SENDX, "0");

		//db22.insert("orders", BANKX, cv7);
		
		//cv7.put(BANKX, "8");
		//cv7.put(ORDERX, "2");
		//cv7.put(SENDX, "0");

		//db22.insert("orders", BANKX, cv7);
		
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db7, int oldVersion, int newVersion) {
		android.util.Log.w("orders", "Upgrading database, which will destroy all old data");
		db7.execSQL("DROP TABLE IF EXISTS orders");
		onCreate(db7);
	}
}