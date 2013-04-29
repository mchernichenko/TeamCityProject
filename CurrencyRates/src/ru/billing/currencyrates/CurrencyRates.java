package ru.billing.currencyrates;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CurrencyRates extends ListActivity {

	private final static String KEY_CHAR_CODE = "CharCode";
	private final static String KEY_VALUE = "Value";
	private final static String KEY_NOMINAL = "Nominal";
	private final static String KEY_NAME = "Name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populate();
		ListView lv = getListView();
		lv.setFadingEdgeLength(3);
		lv.setScrollbarFadingEnabled(true);
		lv.setTextFilterEnabled(true);
	}

	private ArrayList<Map<String, Object>> getData() {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> m;

	//	try {
			String rt_url = getString(R.string.rates_url);
		//	URL url = new URL(getString(R.string.rates_url));
			
		//	HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		    
			// !!!! здесь засада, что делать не пойму
		//	int responseCode = httpConnection.getResponseCode();

			// Если код ответа хороший, парсим поток(ответ сервера),
			// устанавливаем дату в заголовке приложения и
			// заполняем list нужными Map'ами
		//	if (responseCode == HttpURLConnection.HTTP_OK) {	
		//		InputStream in = httpConnection.getInputStream();
		//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//		DocumentBuilder db = dbf.newDocumentBuilder();
		//		Document dom = db.parse(in);
			
				Document dom = (Document) new WebServiceCaller().execute(rt_url);
				Element docElement = dom.getDocumentElement();
				String date = docElement.getAttribute("Date");
				setTitle(getTitle() + " РЅР° " + date);

				NodeList nodeList = docElement.getElementsByTagName("Valute");

				int count = nodeList.getLength();
				if (nodeList != null && count > 0) {
					for (int i = 0; i < count; i++) {
						Element entry = (Element) nodeList.item(i);
						m = new HashMap<String, Object>();

						String charCode = entry
								.getElementsByTagName(KEY_CHAR_CODE).item(0)
								.getFirstChild().getNodeValue();
						String value = entry.getElementsByTagName(KEY_VALUE)
								.item(0).getFirstChild().getNodeValue();
						String nominal = "за " + entry
								.getElementsByTagName(KEY_NOMINAL).item(0)
								.getFirstChild().getNodeValue();
						String name = entry.getElementsByTagName(KEY_NAME)
								.item(0).getFirstChild().getNodeValue();

						m.put(KEY_CHAR_CODE, charCode);
						m.put(KEY_VALUE, value);
						m.put(KEY_NOMINAL, nominal);
						m.put(KEY_NAME, name);

						list.add(m);
					}
				}
		//	} else {
				// Сделать извещения об ошибках, если код ответа нехороший
		//	}

		/*} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} */

		// Отсортируем список по кодам валют
		Collections.sort(list, new MapComparator(KEY_CHAR_CODE));
		return list;
	};

	void populate() {
		ArrayList<Map<String, Object>> data = getData();

		String[] from = { KEY_CHAR_CODE, KEY_VALUE, KEY_NOMINAL, KEY_NAME };
		int[] to = { R.id.charCodeView, R.id.valueView, R.id.nominalView,
				R.id.nameView };

		SimpleAdapter sa = new SimpleAdapter(this, data, R.layout.item_view,
				from, to);

		setListAdapter(sa);
	}

	// Вспомогательный класс для сортировки
	// элементов списка по произвольному ключу.
	// Ключ сортировки передается конструктору
	private class MapComparator implements Comparator<Map<String, Object>> {
		String mSortKey;

		public MapComparator(String sortKey) {
			mSortKey = sortKey;
		}

		public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
			return ((String) lhs.get(mSortKey)).compareTo((String) rhs
					.get(mSortKey));
		}
	}
}