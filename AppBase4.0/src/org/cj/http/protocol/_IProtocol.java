// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IProtocol.java
package org.cj.http.protocol;

import org.cj.androidexception.DecodeMessageException;
import org.cj.androidexception.EncodeMessageException;
import org.cj.androidexception.ServerErrorException;
import org.cj.androidexception.SessionTimeoutException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

public interface _IProtocol {
    String getRequestUrl();

    String getRequestXML();

    String getHeaders();

    byte[] getRequestXMLData()
            throws UnsupportedEncodingException, EncodeMessageException;

    byte[] getUploadFileData();

    void parseResponseData(String str)
            throws ParserConfigurationException, SAXException, IOException,
            DecodeMessageException, SessionTimeoutException,
            ServerErrorException;

    void parseResponseData(byte[] b)
            throws ParserConfigurationException, SAXException, IOException,
            DecodeMessageException, SessionTimeoutException,
            ServerErrorException;

    void parseResponseData(InputStream inputstream)
            throws ParserConfigurationException, SAXException, IOException,
            DecodeMessageException, SessionTimeoutException,
            ServerErrorException;

    void parseResponseXML(Element element)
            throws DecodeMessageException;

    void parseResponseError(Element element)
            throws DecodeMessageException, ServerErrorException;

    void parseResponseTimeout(Element element)
            throws SessionTimeoutException;

    void parseResponseDataByXmlPull(byte[] b)
            throws XmlPullParserException, DecodeMessageException,
            SessionTimeoutException, ServerErrorException, IOException;

    void parseResponseDataByXmlPull(InputStream inputstream)
            throws XmlPullParserException, DecodeMessageException,
            SessionTimeoutException, ServerErrorException, IOException;

    void parseResponseDataByXmlPull(String str)
            throws XmlPullParserException, DecodeMessageException,
            SessionTimeoutException, ServerErrorException;

    void parseResponseXMLByXmlPull(XmlPullParser parser);
}
