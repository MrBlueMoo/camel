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
package org.apache.camel.component.xmlsecurity.api;

import org.apache.camel.Exchange;
import org.apache.camel.spi.Metadata;

public final class XmlSignatureConstants {
    // The schemes
    public static final String SCHEME_VERIFIER = "xmlsecurity-verify";
    public static final String SCHEME_SIGN = "xmlsecurity-sign";
    /**
     * Header for indicating that the message body contains non-xml plain text. This header is used in the XML signature
     * generator. If the value is set to {@link Boolean#TRUE} then the message body is treated as plain text Overwrites
     * the configuration parameter XmlSignerConfiguration#setPlainText(Boolean)
     */
    @Metadata(javaType = "Boolean", applicableFor = SCHEME_SIGN)
    public static final String HEADER_MESSAGE_IS_PLAIN_TEXT = "CamelXmlSignatureMessageIsPlainText";

    /**
     * Header indicating the encoding of the plain text message body. Used in the XML signature generator if the header
     * {@link #HEADER_MESSAGE_IS_PLAIN_TEXT} is set to {@link Boolean#TRUE}. Overwrites the configuration parameter
     * XmlSignerConfiguration#setPlainTextEncoding(String).
     */
    @Metadata(javaType = "String", applicableFor = SCHEME_SIGN)
    public static final String HEADER_PLAIN_TEXT_ENCODING = "CamelXmlSignaturePlainTextEncoding";

    /**
     * Header which indicates that either the resulting signature document in the signature generation case or the
     * resulting output of the verifier should not contain an XML declaration. If the header is not specified then a XML
     * declaration is created.
     * <p>
     * There is one exception: If the verifier result is a plain text this header has no effect.
     * <p>
     * Possible values of the header are {@link Boolean#TRUE} or {@link Boolean#FALSE}.
     * <p>
     * Overwrites the configuration parameter XmlSignatureConfiguration#setOmitXmlDeclaration(Boolean).
     *
     */
    @Metadata(javaType = "Boolean")
    public static final String HEADER_OMIT_XML_DECLARATION = "CamelXmlSignatureOmitXmlDeclaration";
    @Metadata(description = "The content reference URI", javaType = "String", applicableFor = SCHEME_SIGN)
    public static final String HEADER_CONTENT_REFERENCE_URI = "CamelXmlSignatureContentReferenceUri";
    @Metadata(description = "The content reference type", javaType = "String", applicableFor = SCHEME_SIGN)
    public static final String HEADER_CONTENT_REFERENCE_TYPE = "CamelXmlSignatureContentReferenceType";
    @Metadata(description = "The schema resource URI", javaType = "String")
    public static final String HEADER_SCHEMA_RESOURCE_URI = "CamelXmlSignatureSchemaResourceUri";
    @Metadata(description = "XPaths to id attributes", javaType = "String")
    public static final String HEADER_XPATHS_TO_ID_ATTRIBUTES = "CamelXmlSignatureXpathsToIdAttributes";

    /**
     * Header for dynamic specifying the transform methods of the reference to the signed data. The value of the header
     * must be a comma separated list with the transform algorithms, for example:
     * "http://www.w3.org/2000/09/xmldsig#enveloped-signature,http://www.w3.org/TR/2001/REC-xml-c14n-20010315"
     * <p>
     * Used for the XML signer. This header will overwrite the configuration property "transformMethods". You cannot use
     * transform algorithms, which need parameters like http://www.w3.org/TR/1999/REC-xslt-19991116,
     * http://www.w3.org/2002/06/xmldsig-filter2, or http://www.w3.org/TR/1999/REC-xpath-19991116.
     */
    @Metadata(javaType = "String", applicableFor = SCHEME_SIGN)
    public static final String HEADER_TRANSFORM_METHODS = "CamelXmlSignatureTransformMethods";

    /*------------------------- headers for XAdES signer ----------------------------------------------------------*/
    /**
     * Header for the 'Id' attribute value of the XAdES element 'QualifyingProperties'
     *
     */
    @Metadata(description = "for the 'Id' attribute value of `QualifyingProperties` element", javaType = "String")
    public static final String HEADER_XADES_QUALIFYING_PROPERTIES_ID = "CamelXmlSignatureXAdESQualifyingPropertiesId";

    /**
     * Header for the 'Id' attribute value of the XAdES element 'SignedDataObjectProperties'
     *
     */
    @Metadata(description = "for the 'Id' attribute value of `SignedDataObjectProperties` element", javaType = "String")
    public static final String HEADER_XADES_SIGNED_DATA_OBJECT_PROPERTIES_ID
            = "CamelXmlSignatureXAdESSignedDataObjectPropertiesId";

    /**
     * Header for the 'Id' attribute value of the XAdES element 'SignedSignatureProperties'
     *
     */
    @Metadata(description = "for the 'Id' attribute value of `SignedSignatureProperties` element", javaType = "String")
    public static final String HEADER_XADES_SIGNED_SIGNATURE_PROPERTIES_ID
            = "CamelXmlSignatureXAdESSignedSignaturePropertiesId";

    /**
     * Header for the "Encoding" element contained in the "DataObjectFormat" XAdES element.
     */
    @Metadata(description = "for the value of the Encoding element of the `DataObjectFormat` element", javaType = "String")
    public static final String HEADER_XADES_DATA_OBJECT_FORMAT_ENCODING = "CamelXmlSignatureXAdESDataObjectFormatEncoding";

    /**
     * Header for the XAdES namespace. Different namespaces represent different XAdES specification versions. Currently
     * supported namespaces are:
     *
     * http://uri.etsi.org/01903/v1.1.1#,
     *
     * http://uri.etsi.org/01903/v1.2.2#,
     *
     * http://uri.etsi.org/01903/v1.3.2#.
     *
     */
    @Metadata(description = "overwrites the XAdES namespace parameter value", javaType = "String")
    public static final String HEADER_XADES_NAMESPACE = "CamelXmlSignatureXAdESNamespace";

    /**
     * Header for the XAdES namespace prefix. An empty string means that no prefix shall be used. A <code>null</code>
     * header value will have no effect.
     *
     */
    @Metadata(description = "overwrites the XAdES prefix parameter value", javaType = "String")
    public static final String HEADER_XADES_PREFIX = "CamelXmlSignatureXAdESPrefix";
    @Metadata(description = "The name of the charset", javaType = "String")
    public static final String CHARSET_NAME = Exchange.CHARSET_NAME;

    private XmlSignatureConstants() {
        // no instance
    }

}
