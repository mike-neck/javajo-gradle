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
 * {@code null}を許容できないパラメーターに{@code null}を渡した時に発生させる例外。<br/>
 * なお、おもに関数を引数に取る関数・メソッドなどの{@code null}チェックで発生させる。
 */
public class EvaluatingException extends RuntimeException {
    public EvaluatingException(String message) {
        super(message);
    }
}
