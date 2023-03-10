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
package org.apache.camel.component.file.remote;

import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.junit5.TestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UriConfigurationTest extends CamelTestSupport {

    @Test
    public void testFtpConfigurationDefaults() {
        FtpEndpoint<?> endpoint = context.getEndpoint("ftp://hostname", FtpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("ftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(21, config.getPort());
        assertNull(config.getUsername());
        assertNull(config.getPassword());
        assertFalse(config.isBinary());
        assertEquals(RemoteFileConfiguration.PathSeparator.UNIX, config.getSeparator());
    }

    @Test
    public void testSftpConfigurationDefaults() {
        SftpEndpoint endpoint = context.getEndpoint("sftp://hostname", SftpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("sftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(22, config.getPort());
        assertNull(config.getUsername());
        assertNull(config.getPassword());
        assertFalse(config.isBinary());
        assertEquals(RemoteFileConfiguration.PathSeparator.UNIX, config.getSeparator());
    }

    @Test
    public void testFtpsConfigurationDefaults() {
        FtpsEndpoint endpoint = context.getEndpoint("ftps://hostname", FtpsEndpoint.class);
        FtpsConfiguration config = endpoint.getFtpsConfiguration();

        assertEquals("ftps", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(21, config.getPort());
        assertNull(config.getUsername());
        assertNull(config.getPassword());
        assertFalse(config.isBinary());
        assertFalse(config.isImplicit());
        assertEquals("TLSv1.3", config.getSecurityProtocol());
        assertEquals(RemoteFileConfiguration.PathSeparator.UNIX, config.getSeparator());
    }

    @Test
    public void testFtpsExplicitConfigurationDefaults() {
        FtpsEndpoint endpoint = context.getEndpoint("ftps://hostname:990?implicit=true", FtpsEndpoint.class);
        FtpsConfiguration config = endpoint.getFtpsConfiguration();

        assertEquals("ftps", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(990, config.getPort());
        assertNull(config.getUsername());
        assertNull(config.getPassword());
        assertFalse(config.isBinary());
        assertTrue(config.isImplicit());
        assertEquals("TLSv1.3", config.getSecurityProtocol());
    }

    @Test
    public void testFtpExplicitConfiguration() {
        FtpEndpoint<?> endpoint
                = context.getEndpoint("ftp://user@hostname:1021/some/file?password=secret&binary=true", FtpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("ftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(1021, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("secret", config.getPassword());
        assertTrue(config.isBinary());
    }

    @Test
    public void testSftpExplicitConfiguration() {
        SftpEndpoint endpoint
                = context.getEndpoint("sftp://user@hostname:1021/some/file?password=secret&binary=true", SftpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("sftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(1021, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("secret", config.getPassword());
        assertTrue(config.isBinary());
    }

    @Test
    public void testFtpsExplicitConfiguration() {
        FtpsEndpoint endpoint = context.getEndpoint(
                "ftps://user@hostname:1021/some/file?password=secret&binary=true&securityProtocol=SSL&implicit=true",
                FtpsEndpoint.class);
        FtpsConfiguration config = endpoint.getFtpsConfiguration();

        assertEquals("ftps", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(1021, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("secret", config.getPassword());
        assertTrue(config.isBinary());
        assertTrue(config.isImplicit());
        assertEquals("SSL", config.getSecurityProtocol());
    }

    @Test
    public void testRemoteFileEndpointFiles() {
        assertRemoteFileEndpointFile("ftp://hostname/foo/bar", "foo/bar");
        assertRemoteFileEndpointFile("ftp://hostname/foo/bar/", "foo/bar/");
        assertRemoteFileEndpointFile("ftp://hostname/foo/", "foo/");
        assertRemoteFileEndpointFile("ftp://hostname/foo", "foo");
        assertRemoteFileEndpointFile("ftp://hostname/", "");
        assertRemoteFileEndpointFile("ftp://hostname", "");
        assertRemoteFileEndpointFile("ftp://hostname//", "");
        assertRemoteFileEndpointFile("ftp://hostname//foo/bar", "foo/bar");
        assertRemoteFileEndpointFile("ftp://hostname//foo/bar/", "foo/bar/");
        assertRemoteFileEndpointFile("sftp://user@hostname:123//foo/bar?password=secret", "foo/bar");
        assertRemoteFileEndpointFile("sftp://user@hostname:123?password=secret", "");
        assertRemoteFileEndpointFile("sftp://hostname/foo/bar", "foo/bar");
        assertRemoteFileEndpointFile("sftp://hostname/foo/bar/", "foo/bar/");
        assertRemoteFileEndpointFile("sftp://hostname/foo/", "foo/");
        assertRemoteFileEndpointFile("sftp://hostname/foo", "foo");
        assertRemoteFileEndpointFile("sftp://hostname/", "");
        assertRemoteFileEndpointFile("sftp://hostname", "");
        assertRemoteFileEndpointFile("sftp://hostname//", "");
        assertRemoteFileEndpointFile("sftp://hostname//foo/bar", "foo/bar");
        assertRemoteFileEndpointFile("sftp://hostname//foo/bar/", "foo/bar/");
        assertRemoteFileEndpointFile("ftps://user@hostname:123//foo/bar?password=secret", "foo/bar");
        assertRemoteFileEndpointFile("ftps://user@hostname:123?password=secret", "");
        assertRemoteFileEndpointFile("ftps://hostname/foo/bar", "foo/bar");
        assertRemoteFileEndpointFile("ftps://hostname/foo/bar/", "foo/bar/");
        assertRemoteFileEndpointFile("ftps://hostname/foo/", "foo/");
        assertRemoteFileEndpointFile("ftps://hostname/foo", "foo");
        assertRemoteFileEndpointFile("ftps://hostname/", "");
        assertRemoteFileEndpointFile("ftps://hostname", "");
        assertRemoteFileEndpointFile("ftps://hostname//", "");
        assertRemoteFileEndpointFile("ftps://hostname//foo/bar", "foo/bar");
        assertRemoteFileEndpointFile("ftps://hostname//foo/bar/", "foo/bar/");
        assertRemoteFileEndpointFile("ftps://hostname//////foo/bar/", "foo/bar/");
    }

    private void assertRemoteFileEndpointFile(String endpointUri, String expectedFile) {
        RemoteFileEndpoint<?> endpoint = TestSupport.resolveMandatoryEndpoint(context, endpointUri, RemoteFileEndpoint.class);
        assertNotNull(endpoint, "Could not find endpoint: " + endpointUri);

        String file = endpoint.getConfiguration().getDirectory();
        assertEquals(expectedFile, file, "For uri: " + endpointUri + " the file is not equal");
    }

    @Test
    public void testSftpKnownHostsFileConfiguration() {
        SftpEndpoint endpoint = context.getEndpoint(
                "sftp://user@hostname:1021/some/file?password=secret&binary=true&knownHostsFile=/home/janstey/.ssh/known_hosts",
                SftpEndpoint.class);
        SftpConfiguration config = endpoint.getConfiguration();

        assertEquals("sftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(1021, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("secret", config.getPassword());
        assertTrue(config.isBinary());
        assertEquals("/home/janstey/.ssh/known_hosts", config.getKnownHostsFile());
    }

    @Test
    public void testPasswordInContextPathConfiguration() {
        FtpEndpoint<?> endpoint = context.getEndpoint("ftp://user:secret@hostname:1021/some/file", FtpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("ftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals(1021, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("secret", config.getPassword());
    }

    @Test
    public void testStartingDirectoryWithDot() throws Exception {
        FtpEndpoint<?> endpoint = context.getEndpoint("ftp://user@hostname?password=secret", FtpEndpoint.class);
        FtpConfiguration config = endpoint.getConfiguration();
        config.setHost("somewhere");
        config.setDirectory("temp.dir");
        RemoteFileConsumer<?> consumer = endpoint.createConsumer(exchange -> {
            // do nothing
        });

        assertNotNull(consumer, "Could not create the consumer");
    }

    @Test
    public void testPathSeparatorAuto() {
        FtpEndpoint<?> endpoint = context.getEndpoint("ftp://hostname/foo/bar?separator=Auto", FtpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("ftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals("foo/bar", config.getDirectory());
        assertEquals(RemoteFileConfiguration.PathSeparator.Auto, config.getSeparator());

        assertEquals("foo/bar/hello.txt", config.normalizePath("foo/bar/hello.txt"));
        assertEquals("foo\\bar\\hello.txt", config.normalizePath("foo\\bar\\hello.txt"));
    }

    @Test
    public void testPathSeparatorUnix() {
        FtpEndpoint<?> endpoint = context.getEndpoint("ftp://hostname/foo/bar?separator=UNIX", FtpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("ftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals("foo/bar", config.getDirectory());
        assertEquals(RemoteFileConfiguration.PathSeparator.UNIX, config.getSeparator());

        assertEquals("foo/bar/hello.txt", config.normalizePath("foo/bar/hello.txt"));
        assertEquals("foo/bar/hello.txt", config.normalizePath("foo\\bar\\hello.txt"));
    }

    @Test
    public void testPathSeparatorWindows() {
        FtpEndpoint<?> endpoint = context.getEndpoint("ftp://hostname/foo/bar?separator=Windows", FtpEndpoint.class);
        RemoteFileConfiguration config = endpoint.getConfiguration();

        assertEquals("ftp", config.getProtocol());
        assertEquals("hostname", config.getHost());
        assertEquals("foo/bar", config.getDirectory());
        assertEquals(RemoteFileConfiguration.PathSeparator.Windows, config.getSeparator());

        assertEquals("foo\\bar\\hello.txt", config.normalizePath("foo/bar/hello.txt"));
        assertEquals("foo\\bar\\hello.txt", config.normalizePath("foo\\bar\\hello.txt"));
    }

}
