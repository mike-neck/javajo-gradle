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
package javajo.sample;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TestTime extends TestWatcher {

    private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private Record record;

    @Override
    protected void starting(Description desc) {
        record = new Record(desc.getClassName(), desc.getMethodName());
    }

    public String getTestName() {
        return record.method;
    }

    public String getClassName() {
        return record.klass;
    }

    public String getStamp() {
        return record.toString();
    }

    private static class Record {
        private final String klass;
        private final String method;
        private final LocalDateTime started;
        Record(String klass, String method) {
            this.klass = klass;
            this.method = method;
            this.started = LocalDateTime.now(ZONE);
        }
        @Override
        public String toString() {
            return FORMAT.format(started) + " - " + klass + "#" + method;
        }
    }
}
