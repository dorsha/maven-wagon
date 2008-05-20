package org.apache.maven.wagon.providers.ssh;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.wagon.TransferFailedException;

import java.util.List;

import junit.framework.TestCase;

public class LSParserTest
    extends TestCase
{
    public void testParseLinux()
        throws TransferFailedException
    {
        String rawLS = "total 32\n" + "drwxr-xr-x  5 joakim joakim 4096 2006-12-11 10:30 .\n"
            + "drwxr-xr-x 14 joakim joakim 4096 2006-12-11 10:30 ..\n"
            + "-rw-r--r--  1 joakim joakim  320 2006-12-09 18:46 .classpath\n"
            + "-rw-r--r--  1 joakim joakim 1194 2006-12-11 09:25 pom.xml\n"
            + "-rw-r--r--  1 joakim joakim  662 2006-12-09 18:46 .project\n"
            + "drwxr-xr-x  4 joakim joakim 4096 2006-11-21 12:26 src\n"
            + "drwxr-xr-x  7 joakim joakim 4096 2006-12-11 10:31 .svn\n"
            + "drwxr-xr-x  3 joakim joakim 4096 2006-12-11 08:39 target\n";

        LSParser parser = new LSParser();
        List files = parser.parseFiles( rawLS );
        assertNotNull( files );
        assertEquals( 8, files.size() );
        assertTrue( files.contains( "pom.xml" ) );
    }

    public void testParseOSX() throws TransferFailedException
    {
        String rawLS = "total 32\n" + "drwxr-xr-x   5  joakim  joakim   238 Dec 11 10:30 .\n"
            + "drwxr-xr-x  14  joakim  joakim   518 Dec 11 10:30 ..\n"
            + "-rw-r--r--   1  joakim  joakim   320 May  9  2006 .classpath\n"
            + "-rw-r--r--   1  joakim  joakim  1194 Dec 11 09:25 pom.xml\n"
            + "-rw-r--r--   1  joakim  joakim   662 May  9  2006 .project\n"
            + "drwxr-xr-x   4  joakim  joakim   204 Dec 11 12:26 src\n"
            + "drwxr-xr-x   7  joakim  joakim   476 Dec 11 10:31 .svn\n"
            + "drwxr-xr-x   3  joakim  joakim   238 Dec 11 08:39 target\n";

        LSParser parser = new LSParser();
        List files = parser.parseFiles( rawLS );
        assertNotNull( files );
        assertEquals( 8, files.size() );
        assertTrue( files.contains( "pom.xml" ) );
    }

    public void testParseCygwin() throws TransferFailedException
    {
        String rawLS = "total 32\n" + "drwxr-xr-x+  5 joakim None    0 Dec 11 10:30 .\n"
            + "drwxr-xr-x+ 14 joakim None    0 Dec 11 10:30 ..\n"
            + "-rw-r--r--+  1 joakim None  320 May  9  2006 .classpath\n"
            + "-rw-r--r--+  1 joakim None 1194 Dec 11 09:25 pom.xml\n"
            + "-rw-r--r--+  1 joakim None  662 May  9  2006 .project\n"
            + "drwxr-xr-x+  4 joakim None    0 Dec 11 12:26 src\n"
            + "drwxr-xr-x+  7 joakim None    0 Dec 11 10:31 .svn\n"
            + "drwxr-xr-x+  3 joakim None    0 Dec 11 08:39 target\n";
        
        LSParser parser = new LSParser();
        List files = parser.parseFiles( rawLS );
        assertNotNull( files );
        assertEquals( 8, files.size() );
        assertTrue( files.contains( "pom.xml" ) );
    }

    /**
     * Snicoll, Jvanzyl, and Tom reported problems with wagon-ssh.getFileList().
     * Just adding a real-world example of the ls to see if it is a problem.
     *   - Joakime
     */
    public void testParsePeopleApacheStaging() throws TransferFailedException
    {
        String rawLS = "total 6\n"
            + "drwxr-xr-x  3 snicoll  snicoll  512 Feb  7 11:04 .\n"
            + "drwxr-xr-x  3 snicoll  snicoll  512 Feb  7 11:04 ..\n"
            + "drwxr-xr-x  3 snicoll  snicoll  512 Feb  7 11:04 org\n";

        LSParser parser = new LSParser();
        List files = parser.parseFiles( rawLS );
        assertNotNull( files );
        assertEquals( 3, files.size() );
        assertTrue( files.contains( "org" ) );
    }
}