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

import javajo.sample.functions.Functions.Function;
import javajo.sample.functions.ValueHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.NoSuchElementException;

import static javajo.sample.data.Maybe.nothing;
import static javajo.sample.data.Maybe.some;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(Enclosed.class)
public class MaybeTest {

    private static class TestException extends RuntimeException {}

    public static class NothingTest {

        private Maybe<String> maybe;

        @Before
        public void setup() {
            maybe = nothing();
        }

        @Test
        public void isNothingReturnsTrue() {
            assertThat(maybe.isNothing(), is(true));
        }

        @Test
        public void isSomeReturnsFalse() {
            assertThat(maybe.isSome(), is(false));
        }

        @Test(expected = NoSuchElementException.class)
        public void getMethodThrowsException() {
            maybe.get();
            fail("This code should not be executed.");
        }

        @Test
        public void orDefaultMethodReturnsDefaultValue() {
            String value = maybe.orDefault("default");
            assertThat(value, is("default"));
        }

        @Test(expected = TestException.class)
        public void orThrowThrowsException() {
            maybe.orThrow(TestException::new);
            fail("This code should not be executed.");
        }

        @Test
        public void sideEffectComesFromOrOnNothingDo() {
            ValueHolder<String> holder = new ValueHolder<>();
            maybe.onSomeDo(Assert::fail)
                    .orOnNothingDo(() -> holder.forcePut("it's nothing."));
            assertThat(holder.getValue(), is("it's nothing."));
        }

        @Test
        public void whenSomeMethodDoNothing() {
            ValueHolder<String> holder = new ValueHolder<>();
            maybe.whenSome(holder::forcePut);
            assertThat(holder.hasValue(), is(false));
        }

        @Test
        public void mappingNothingReturnsNothing() {
            Maybe<Integer> mapped = maybe.map(String::length);
            assertThat(mapped.isNothing(), is(true));
        }

        @Test
        public void flatMappingNothingReturnsNothing() {
            Maybe<String> fmapped = maybe.fmap(s -> s == null ? nothing() : some(s));
            assertThat(fmapped.isNothing(), is(true));
        }
    }

    public static class SomeTest {

        private Maybe<String> maybe;

        @Before
        public void setup() {
            maybe = some("testData");
        }

        @Test
        public void isNothingReturnsFalse() {
            assertThat(maybe.isNothing(), is(false));
        }

        @Test
        public void isSomeReturnsTrue() {
            assertThat(maybe.isSome(), is(true));
        }

        @Test
        public void getMethodReturnsValue() {
            String value = maybe.get();
            assertThat(value, is("testData"));
        }

        @Test
        public void orDefaultReturnsOriginalValue() {
            String value = maybe.orDefault("This value should not be returned.");
            assertThat(value, is("testData"));
        }

        @Test
        public void orThrowReturnsOriginalValue() {
            String value = maybe.orThrow(TestException::new);
            assertThat(value, is("testData"));
        }

        @Test
        public void sideEffectComesFromOnSomeDo() {
            ValueHolder<String> holder = new ValueHolder<>();
            maybe.onSomeDo(holder::forcePut)
                    .orOnNothingDo(() -> fail("This code should not be executed."));
            assertThat(holder.getValue(), is("testData"));
        }

        @Test
        public void whenSomeMethodHasSideEffect() {
            ValueHolder<String> holder = new ValueHolder<>();
            maybe.whenSome(holder::forcePut);
            assertThat(holder.getValue(), is("testData"));
        }
    }

    public static class MappingTest {

        private Maybe<String> maybe;

        @Before
        public void setup() {
            maybe = some("testData");
        }

        @Test
        public void mapping() {
            Maybe<Integer> mapped = maybe.map(String::length);
            assertThat(mapped.isSome(), is(true));
            Integer length = mapped.orDefault(-1);
            assertThat(length, is(8));
        }

        @Test
        public void flatMappingReturningSome() {
            Function<String, Maybe<String>> last5Chars = s -> s.length() < 5 ?
                    nothing() :
                    some(s.substring(s.length() - 5, s.length()));
            Maybe<String> fmapped = maybe.fmap(last5Chars);
            assertThat(fmapped.isSome(), is(true));
            assertThat(fmapped.get(), is("tData"));
        }

        @Test
        public void flatMappingReturningNothing() {
            Function<String, Maybe<String>> takeLast10Chars = s -> s.length() < 10 ?
                    nothing() :
                    some(s.substring(s.length() - 5, s.length()));
            Maybe<String> fmapped = maybe.fmap(takeLast10Chars);
            assertThat(fmapped.isNothing(), is(true));
        }

        @Test
        public void mappedByNullReturningFunctionMakesNothing() {
            Function<String, URL> returnsNull = s -> null;
            Maybe<URL> mapped = maybe.map(returnsNull);
            assertThat(mapped.isNothing(), is(true));
        }

        @Test
        public void flatMappingByNullReturningFunctionMakesNothing() {
            Function<String, Maybe<URL>> returnsNull = s -> null;
            Maybe<URL> fmapped = maybe.fmap(returnsNull);
            assertThat(fmapped.isNothing(), is(true));
        }
    }
}
