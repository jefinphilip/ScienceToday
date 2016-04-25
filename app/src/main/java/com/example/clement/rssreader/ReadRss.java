package com.example.clement.rssreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by CLemENt on 4/13/2016.
 */
public class ReadRss extends AsyncTask<Void,Void,Void> {

    Context context;

    String address="http://www.sciencemag.org/rss/news_current.xml";
    URL url;
    ArrayList<FeedItem>feedItems;
    RecyclerView recyclerView;


    ProgressDialog progresDialog;

    public ReadRss(Context context,RecyclerView recyclerView)
    {
        this.recyclerView=recyclerView;
        this.context=context;
        progresDialog=new ProgressDialog(context);
        progresDialog.setMessage("Loading....");

    }
    @Override
    protected Void doInBackground(Void... params) {

      ProcesXml(getdata());
        return null;
    }


    private void ProcesXml(Document getdata) {
        if (getdata!=null) {
            feedItems=new ArrayList<>();
            Element root=getdata.getDocumentElement();
            Node channel;
            channel = (Node) root.getChildNodes().item(1);
            NodeList items =channel.getChildNodes();
            for (int i=0;i<items.getLength();i++)
            {
                Node currentChild=items.item(i);
                if(currentChild.getNodeName().equalsIgnoreCase("item")){
                    FeedItem item= new FeedItem();
                    NodeList itemchild =currentChild.getChildNodes();

                    for (int j=0;j<itemchild.getLength();j++){

                        Node current =itemchild.item(j);

                        if (current.getNodeName().equalsIgnoreCase("title")){
                            item.setTitle(current.getTextContent());
                        }else if (current.getNodeName().equalsIgnoreCase("description")){
                            item.setDescription(current.getTextContent());
                        }else if (current.getNodeName().equalsIgnoreCase("pubDate")){
                            item.setPubDate(current.getTextContent());
                        }else if (current.getNodeName().equalsIgnoreCase("link")){
                            item.setLink(current.getTextContent());
                        }else if (current.getNodeName().equalsIgnoreCase("media:thumbnail")) {
                            //item.setLink(current.getTextContent());
                            //return tumb thumbnail url
                            String url = current.getAttributes().item(0).getTextContent();
                            item.setThumbnailUrl(url);
                        }
                    }
                    feedItems.add(item);


                    Log.d("itemThumbnailurl", item.getThumbnailUrl());
                    //Log.d("itemDescription",item.getDescription());
                    //Log.d("itemPubDate",item.getPubDate());
                    Log.d("itemLink",item.getLink());






                }
                }
            }


            //Log.d("Root", getdata.getDocumentElement().getNodeName());
        }





    public Document getdata(){
        try {
            url=new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream= connection.getInputStream();
            DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=builderFactory.newDocumentBuilder();

            Document xmlDoc =builder.parse(inputStream);
            return xmlDoc;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        progresDialog.show();

        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
        progresDialog.dismiss();
        MyAdapter myAdapter=new MyAdapter(context,feedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new VerticalSpace(50));
    }
}



