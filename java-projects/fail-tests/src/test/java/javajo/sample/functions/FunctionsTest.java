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
package javajo.sample.functions;

import javajo.sample.EvaluatingException;
import javajo.sample.ExecutingException;
import javajo.sample.functions.Functions.ExFunction;
import javajo.sample.functions.Functions.ExGenerator;
import javajo.sample.functions.Functions.ExOperator;
import javajo.sample.functions.Functions.Function;
import javajo.sample.functions.Functions.Generator;
import javajo.sample.functions.Functions.Operator;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.net.URL;

import static javajo.sample.functions.Functions.function;
import static javajo.sample.functions.Functions.generator;
import static javajo.sample.functions.Functions.operator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class FunctionsTest {

    private final String testData = "testData";

    public Integer applyToIntegerFunction(Function<String, Integer> fun) {
        return fun.apply(testData);
    }

    public URL applyToUrlFunction(Function<String, URL> fun) {
        return fun.apply(testData);
    }

    private void act(Operator<String> op) {
        op.accept(testData);
    }

    public static class FunctionTest {

        private FunctionsTest object;

        @Before
        public void setup() {
            this.object = new FunctionsTest();
        }

        @Test
        public void string型の値をInteger型に変換するテスト() {
            Function<String, Integer> length = String::length;
            Integer len = object.applyToIntegerFunction(length);
            assertThat(len, is(8));
        }

        @Test(expected = ExecutingException.class)
        public void strig型の値をURL型に変換するときに例外が発生する場合のテスト() {
            ExFunction<String, URL> toUrl = URL::new;
            object.applyToUrlFunction(function(toUrl));
        }
    }

    private static class GeneratorSupport {

        /**
         * Constructor without Exception
         */
        GeneratorSupport() {}

        /**
         * Constructor with Exception.
         * @param exception whether throw exception or not.
         * @throws Exception if {@code true} is given, an exception is thrown.
         */
        GeneratorSupport(boolean exception) throws Exception {
            if (exception) {
                throw new Exception("Thrown by GeneratorSupport constructor.");
            }
        }
    }

    public static class GeneratorTest {

        private static Integer getHash(Generator<GeneratorSupport> gen) {
            return gen.get().hashCode();
        }

        @Test
        public void generator型の引数を取るメソッドにgeneratorを渡すテスト() {
            Generator<GeneratorSupport> gen = GeneratorSupport::new;
            Integer hash = getHash(gen);
            assertThat(hash, is(notNullValue()));
        }

        @Test
        public void 例外許容型generatorをgenerator型の引数をとるメソッドに渡すテスト() {
            ExGenerator<GeneratorSupport> gen = () -> new GeneratorSupport(false);
            Integer hash = getHash(generator(gen));
            assertThat(hash, is(notNullValue()));
        }

        @Test(expected = ExecutingException.class)
        public void 例外許容型generatorで例外が発生するものをgenerator型の引数をとるメソッドに渡すテスト() {
            ExGenerator<GeneratorSupport> gen = () -> new GeneratorSupport(true);
            getHash(generator(gen));
        }
    }

    public static class OperatorTest {

        private FunctionsTest object;

        private ValueHolder<String> holder;

        @Before
        public void setup() {
            object = new FunctionsTest();
            holder = new ValueHolder<>();
        }

        @Test
        public void 処理型の引数をとるメソッドに処理型のインスタンスを渡すテスト() {
            Operator<String> op = holder::forcePut;
            object.act(op);
            assertThat(holder.getValue(), is("testData"));
        }

        @Test
        public void 例外許容処理型のインスタンスを処理型の引数をとるメソッドに渡すテスト() {
            ExOperator<String> op = holder::putValue;
            object.act(operator(op));
            assertThat(holder.getValue(), is("testData"));
        }

        @Test(expected = ExecutingException.class)
        public void 例外許容処理型で例外を実際に送出する処理型のインスタンスを処理型の引数をとるメソッドに渡すテスト() {
            holder.forcePut("before");
            ExOperator<String> op = holder::putValue;
            object.act(operator(op));
        }
    }

    public static class ConverterTest {

        @Test(expected = EvaluatingException.class)
        public void 例外許容関数型の変換メソッドにnullを渡した場合にEvaluationExceptionが発生するテスト() {
            function(null);
        }

        @Test(expected = EvaluatingException.class)
        public void 例外許容生成型の変換メソッドにnullを渡した場合にEvaluationExceptionが発生するテスト() {
            generator(null);
        }

        @Test(expected = EvaluatingException.class)
        public void 例外許容処理型の変換メソッドにnullを渡した場合にEvaluationExceptionが発生するテスト() {
            operator(null);
        }
    }
}
