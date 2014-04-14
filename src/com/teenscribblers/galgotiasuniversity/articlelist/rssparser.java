package com.teenscribblers.galgotiasuniversity.articlelist;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class rssparser {

	private static final String TAG = "RSS_PARSE";

	public static void parse() throws DOMException, IOException, ParserConfigurationException, SAXException {
		URL url;

		
			// Set the url (you will need to change this to your RSS URL
			url = new URL(
					"http://www.gunewsteenscribblers.blogspot.in/feeds/posts/default");
			Log.i(TAG, "started parsing");
			// Setup the connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// Connect
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.i(TAG, "HTTP_OK");
				// Retreive the XML from the URL
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc;
				doc = db.parse(url.openConnection().getInputStream());
				doc.getDocumentElement().normalize();

				// This is the root node of each section you want to parse
				NodeList itemLst = doc.getElementsByTagName("entry");

				// This sets up some arrays to hold the data parsed
				arrays.Title = new String[itemLst.getLength()];
				arrays.Content = new String[itemLst.getLength()];
				arrays.Pubdate = new String[itemLst.getLength()];

				// Loop through the XML passing the data to the arrays
				for (int i = 0; i < itemLst.getLength(); i++) {

					Node item = itemLst.item(i);
					if (item.getNodeType() == Node.ELEMENT_NODE) {
						Element ielem = (Element) item;

						// This section gets the elements from the XML
						// that we want to use you will need to add
						// and remove elements that you want / don't want
						NodeList title = ielem.getElementsByTagName("title");
						NodeList content = ielem
								.getElementsByTagName("content");
						NodeList pubdate = ielem
								.getElementsByTagName("published");

						// This is an attribute of an element so I create
						// a string to make it easier to use

						// This section adds an entry to the arrays with the
						// data retrieved from above. I have surrounded each
						// with try/catch just incase the element does not
						// exist
						try {
							arrays.Title[i] = title.item(0).getChildNodes()
									.item(0).getNodeValue();
						} catch (NullPointerException e) {
							Log.e(null, e.toString(), e);

						}

						try {
							arrays.Content[i] = content.item(0).getChildNodes()
									.item(0).getNodeValue();
						} catch (NullPointerException e) {
							Log.e(null, e.toString(), e);

						}
						try {
							arrays.Pubdate[i] = pubdate.item(0).getChildNodes()
									.item(0).getNodeValue();
						} catch (NullPointerException e) {
							Log.e(null, e.toString(), e);

						}

					}
				}
			}

		
	}

}
