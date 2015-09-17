/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.nio.file.*

def self = Paths.get('build.gradle').toAbsolutePath()
def root = self.parent

if (!root.endsWith('javajo-gradle')) {
    throw new IllegalStateException("Please execute this script at root directory. expected: [javajo-gradle], current: [${root.fileName}]")
}

def project = root.resolve('java-projects/fail-tests')
def srcDir = project.resolve('src/test/java')
def rootPackage = 'javajo.sample.tests'

def names = [
        'sheepdog', 'foxhound', 'bulldog', 'chihuahua', 'dalmatian'
]

def asTypeName = {String name ->
    "${name.substring(0,1).toUpperCase()}${name.substring(1)}"
}

names.each {pkg ->
    def packageName = "$rootPackage.$pkg"
    def dir = srcDir.resolve(packageName.replace('.','/'))
    names.each {name ->
        def type = "${asTypeName(name)}Test"
        def classContents = """
package ${packageName};

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import javajo.sample.TestTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ${type} {

    @Rule
    public final TestTime testTime = new TestTime();

    @After
    public void teardown() {
        System.out.println(testTime.getStamp());
    }

    @Test
    public void testName() throws InterruptedException {
        Thread.sleep(1200l);
        assertThat(testTime.getTestName())
                .isEqualTo("testName");
    }

    @Test
    public void className() throws InterruptedException {
        Thread.sleep(1200l);
        assertThat(testTime.getClassName())
                .isEqualTo("${packageName}.${type}");
    }

    @Test
    public void stamp() throws InterruptedException {
        Thread.sleep(1200l);
        assertThat(testTime.getStamp())
                .contains("${packageName}.${type}#stamp");
    }
}
"""
        if (!Files.exists(dir)) {
            Files.createDirectories(dir)
        }
        dir.resolve("${type}.java").toFile().write(classContents, 'UTF-8')
    }
}
