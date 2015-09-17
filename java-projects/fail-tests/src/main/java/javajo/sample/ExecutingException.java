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

/**
 * 関数実行時に発生した例外をくるむための例外<br/>
 * 主に次の用途で利用する
 * <ul>
 *     <li>{@link javajo.sample.functions.Functions.Function}でラップした{@link javajo.sample.functions.Functions.ExFunction}の実行時に発生した例外をラップする</li>
 *     <li>{@link javajo.sample.functions.Functions.Generator}でラップした{@link javajo.sample.functions.Functions.ExGenerator}の実行時に発生した例外をラップする</li>
 *     <li>{@link javajo.sample.functions.Functions.Condition}でラップした{@link javajo.sample.functions.Functions.ExCondition}の実行時に発生した例外をラップする</li>
 *     <li>{@link javajo.sample.functions.Functions.Task}でラップした{@link javajo.sample.functions.Functions.ExTask}の実行時に発生した例外をラップする</li>
 * </ul>
 */
public class ExecutingException extends RuntimeException {

    public ExecutingException(String message) {
        super(message);
    }

    public ExecutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutingException(Throwable cause) {
        super(cause);
    }
}
