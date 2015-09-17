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

if (root.fileName.endsWith('javajo-gradle')) {
    throw new IllegalStateException('Please execute this script at root directory. [javajo-gradle]')
}

def project = root.resolve('java-projects/fail-tests')
def srcDir = project.resolve('src/test/java')
def rootPackage = 'javajo.sample.tests'


