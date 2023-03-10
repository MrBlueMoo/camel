/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.maven;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

public final class XmlHelper {

    private XmlHelper() {
    }

    public static Document buildNamespaceAwareDocument(File xml)
            throws SAXException, ParserConfigurationException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        try {
            // disable DOCTYPE declaration:
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (ParserConfigurationException e) {
        }
        try {
            // Set secure processing
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        } catch (ParserConfigurationException e) {
        }
        try {
            // Disable the external-general-entities by default
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        } catch (ParserConfigurationException e) {
        }
        try {
            // Disable the external-parameter-entities by default
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (ParserConfigurationException e) {
        }
        return factory.newDocumentBuilder().parse(xml);
    }

    public static Transformer buildTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        return transformer;
    }

    public static XPath buildXPath(NamespaceContext namespaceContext) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(namespaceContext);
        return xPath;
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }
}
