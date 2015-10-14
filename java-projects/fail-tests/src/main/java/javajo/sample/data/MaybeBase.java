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
package javajo.sample.data;

import javajo.sample.functions.Functions.Condition;
import javajo.sample.functions.Functions.Function;
import javajo.sample.functions.Functions.Generator;
import javajo.sample.functions.Functions.Operator;

import java.util.NoSuchElementException;
import java.util.Objects;

import static javajo.sample.functions.Functions.verifyNonNullGenerator;
import static javajo.sample.functions.Functions.verifyNotNullCondition;
import static javajo.sample.functions.Functions.verifyNotNullFunction;
import static javajo.sample.functions.Functions.verifyNotNullObject;
import static javajo.sample.functions.Functions.verifyNotNullOperator;
import static javajo.sample.functions.Functions.verifyNotNullTask;

final class MaybeBase {

    private MaybeBase() {}

    static class Nothing<V> implements Maybe<V> {

        Nothing() {}

        @Override
        public boolean isSome() {
            return false;
        }

        @Override
        public <R> Maybe<R> map(Function<? super V, ? extends R> fun) {
            return new Nothing<>();
        }

        @Override
        public <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun) {
            return new Nothing<>();
        }

        @Override
        public Maybe<V> filter(Condition<? super V> cond) {
            return new Nothing<>();
        }

        @Override
        public V get() throws NoSuchElementException {
            throw new NoSuchElementException("Nothing has no value.");
        }

        @Override
        public V orDefault(V defaultValue) {
            verifyNotNullObject(defaultValue);
            return defaultValue;
        }

        @Override
        public V orThrow(Generator<? extends RuntimeException> gen) {
            verifyNonNullGenerator(gen);
            throw  gen.get();
        }

        @Override
        public OperatingContext onSomeDo(Operator<? super V> op) {
            return tsk -> {
                verifyNotNullTask(tsk);
                tsk.execute();
            };
        }

        @Override
        public void whenSome(Operator<? super V> op) {
            // do nothing
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    }

    static class Some<V> implements Maybe<V> {

        private final V value;

        Some(V value) {
            this.value = value;
        }

        @Override
        public boolean isSome() {
            return true;
        }

        @Override
        public <R> Maybe<R> map(Function<? super V, ? extends R> fun) {
            verifyNotNullFunction(fun);
            R result = fun.apply(value);
            return result == null ? new Nothing<>() : new Some<>(result);
        }

        @Override
        public <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun) {
            verifyNotNullFunction(fun);
            // 演習2解答例
            Maybe<R> maybe = fun.apply(value);
            return maybe == null ? new Nothing<>() : maybe;
        }

        @Override
        public Maybe<V> filter(Condition<? super V> cond) {
            verifyNotNullCondition(cond);
            Function<V, V> fun = v -> cond.test(v) ? v : null;
            return map(fun);
        }

        @Override
        public V get() throws NoSuchElementException {
            return value;
        }

        @Override
        public V orDefault(V defaultValue) {
            return value;
        }

        @Override
        public V orThrow(Generator<? extends RuntimeException> gen) {
            return value;
        }

        @Override
        public OperatingContext onSomeDo(Operator<? super V> op) {
            verifyNotNullOperator(op);
            return tk -> op.accept(value);
        }

        @Override
        public void whenSome(Operator<? super V> op) {
            verifyNotNullOperator(op);
            op.accept(value);
        }

        @Override
        public String toString() {
            return "Some[" + value + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Some)) return false;
            Some<?> some = (Some<?>) o;
            return Objects.equals(value, some.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
