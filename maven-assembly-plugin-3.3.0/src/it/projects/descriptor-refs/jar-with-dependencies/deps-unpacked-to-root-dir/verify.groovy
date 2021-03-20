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

import java.io.*;
import java.net.*;
import java.util.jar.*;

boolean result = true;

System.out.println( "Creating JarFile java.io.File." )
    
File jarFile = new File( basedir, "child2/target/child2-1.0-SNAPSHOT-jar-with-dependencies.jar" )
    
System.out.println( "Checking for existence and file-ishness of: " + jarFile )
    
assert ( jarFile.isFile() ) : "jar file is missing or a directory."
    
System.out.println( "Creating JarFile instance." )
    
JarFile jf = new JarFile( jarFile )
    
System.out.println( "Looking for 'test/AppChild1.class' jar entry." )

assert ( jf.getEntry( "test/AppChild1.class" ) != null ) : "child1 class is missing."

assert ( jf.getEntry( "test/App.class" ) != null ) : "child2 class is missing."

System.out.println( "Looking for absence of 'junit/' jar entry." );
    
assert ( jf.getEntry( "junit/framework/TestCase.class" ) == null ) : "junit jar should not be present."
