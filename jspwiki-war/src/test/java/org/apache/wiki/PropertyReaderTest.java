/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wiki;

import java.util.Properties;

import org.apache.wiki.util.PropertyReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PropertyReaderTest extends TestCase
{
    public void testVariableExpansion()
    {
        Properties p = new Properties();
        
        p.put("var.basedir", "/p/mywiki");

        p.put("jspwiki.fileSystemProvider.pageDir", "$basedir/www/");
        p.put("jspwiki.basicAttachmentProvider.storageDir", "$basedir/www/");
        p.put("jspwiki.workDir", "$basedir/wrk/");
        p.put("jspwiki.xyz", "test basedir"); //don't touch this

        PropertyReader.expandVars(p);
        
        assertTrue( p.getProperty("jspwiki.fileSystemProvider.pageDir").equals("/p/mywiki/www/") );
        assertTrue( p.getProperty("jspwiki.basicAttachmentProvider.storageDir").equals("/p/mywiki/www/") );
        assertTrue( p.getProperty("jspwiki.fileSystemProvider.pageDir").equals("/p/mywiki/www/") );
        assertTrue( p.getProperty("jspwiki.workDir").endsWith("/p/mywiki/wrk/") );
        assertTrue( p.getProperty("jspwiki.xyz").endsWith("test basedir") ); //don't touch this
        
//        System.out.println(p.getProperty("jspwiki.workDir"));
        
        assertFalse( p.getProperty("jspwiki.workDir").endsWith("$basedir/wrk/") );
    }

    public void testVariableExpansion2()
    {
        Properties p = new Properties();
        
        //this time, declare the var at the end... (should overwrite this one);
        p.put("var.basedir", "xxx");
        
        p.put("jspwiki.fileSystemProvider.pageDir", "$basedir/www/");
        p.put("jspwiki.basicAttachmentProvider.storageDir", "$basedir/www/");
        p.put("jspwiki.workDir", "$basedir/wrk/");
        p.put("jspwiki.xyz", "test basedir"); //don't touch this
        p.put("jspwiki.abc", "test $x2"); //don't touch this

        p.put("var.basedir", " /p/mywiki"); //note that this var has a space at the beginning...
        p.put("var.x2", " wiki "); //note that this var has a space at the beginning...
        
        PropertyReader.expandVars(p);
        
        assertTrue( p.getProperty("jspwiki.fileSystemProvider.pageDir").equals("/p/mywiki/www/") );
        assertTrue( p.getProperty("jspwiki.basicAttachmentProvider.storageDir").equals("/p/mywiki/www/") );
        assertTrue( p.getProperty("jspwiki.fileSystemProvider.pageDir").equals("/p/mywiki/www/") );
        assertTrue( p.getProperty("jspwiki.workDir").endsWith("/p/mywiki/wrk/") );
        assertTrue( p.getProperty("jspwiki.xyz").endsWith("test basedir") ); //don't touch this
        
        //System.out.println(p.getProperty("jspwiki.abc"));
        
        assertFalse( p.getProperty("jspwiki.workDir").endsWith("$basedir/wrk/") );
        assertTrue( p.getProperty("jspwiki.abc").endsWith("test wiki") );
    }


    
    public void testMultipleVariableExpansion()
    {
        Properties p = new Properties();
        
        //this time, declare the var at the end... (should overwrite this one);
        p.put("var.x1", "a");
        p.put("var.x2", "b");
        
        p.put("jspwiki.x1", "$x1");
        p.put("jspwiki.x2", "$x2");
        p.put("jspwiki.x3", "$x1/$x2");
        
        PropertyReader.expandVars(p);
        
        //System.out.println(p.getProperty("jspwiki.x1"));
        //System.out.println(p.getProperty("jspwiki.x2"));
        //System.out.println(p.getProperty("jspwiki.x3"));
        
        assertTrue( p.getProperty("jspwiki.x1").equals("a") );
        assertTrue( p.getProperty("jspwiki.x2").equals("b") );
        assertTrue( p.getProperty("jspwiki.x3").equals("a/b") );
    }

    
    public static Test suite()
    {
        return new TestSuite( PropertyReaderTest.class );
    }
}