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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Functions {

    private Functions() {}

    public interface FunctionType{}

    /**
     * function with type * -&gt; *
     * @param <IN> input type
     * @param <OUT> output type
     */
    @FunctionalInterface
    public interface Function<IN, OUT> extends FunctionType {
        /**
         * receive input instance and returns output object.
         * @param input - input object.
         * @return - output object.
         */
        OUT apply(IN input);
    }

    /**
     * function with type * -&gt; *. this interface allows throwing exception.
     * @param <IN> input type
     * @param <OUT> output type
     */
    @FunctionalInterface
    public interface ExFunction<IN, OUT> extends FunctionType {
        OUT apply(IN input) throws Exception;
    }

    /**
     * convert {@link Functions.ExFunction} to {@link Functions.Function}.
     * @param exf - {@link Functions.ExFunction} instance. Null value not allowed.
     * @param <I> - function input type
     * @param <O> - function output type
     * @return - {@link Functions.Function} instance. It's not null.
     * @throws EvaluatingException if function is null, exception will be thrown.
     */
    @NotNull
    @Contract("null -> fail")
    public static <I, O> Function<I, O> function(ExFunction<? super I, ? extends O> exf) throws EvaluatingException {
        verifyNotNullFunction(exf);
        return i -> {
            try {
                return exf.apply(i);
            } catch (Exception e) {
                throw new ExecutingException(e);
            }
        };
    }

    /**
     * check function is not null. if null is passed, {@link EvaluatingException} will be thrown.
     * @param fun - function
     * @param <FUN> - function type
     * @throws EvaluatingException - if function is null, exception will be thrown.
     */
    @Contract("null -> fail")
    public static <FUN extends FunctionType> void verifyNotNullFunction(FUN fun) throws EvaluatingException {
        if (fun == null) {
            throw new EvaluatingException("Function should be non null value.");
        }
    }

    public interface GeneratorType {}

    /**
     * function with type -&gt; *
     * @param <O> output type
     */
    @FunctionalInterface
    public interface Generator<O> extends GeneratorType {
        /**
         * create instance.
         * @return - instance.
         */
        O get();
    }

    /**
     * function with type -&gt; *
     * @param <O> output type
     */
    public interface ExGenerator<O> extends GeneratorType {
        /**
         * create instance.
         * @return - instance.
         * @throws Exception
         */
        O get() throws Exception;
    }

    /**
     * convert {@link Functions.ExGenerator} to {@link Functions.Generator}.
     * @param exg generator instance.
     * @param <O> generator type
     * @return converted generator
     * @throws EvaluatingException - if generator is null, an exception will be thrown.
     */
    @NotNull
    @Contract("null -> fail")
    public static <O> Generator<O> generator(ExGenerator<? extends O> exg) throws EvaluatingException {
        verifyNonNullGenerator(exg);
        return () -> {
            try {
                return exg.get();
            } catch (Exception e) {
                throw new ExecutingException(e);
            }
        };
    }

    /**
     * check generator is not null. if generator is null, an exception will be thrown.
     * @param gen - generator instance.
     * @param <OBJ> - generator type.
     * @throws EvaluatingException if generator is null, an exception will be thrown.
     */
    @Contract("null -> fail")
    public static <OBJ extends GeneratorType> void verifyNonNullGenerator(OBJ gen) throws EvaluatingException {
        if (gen == null) {
            throw new EvaluatingException("Generator should be non null value.");
        }
    }

    public interface OperatorType {}

    /**
     * function with type * -&gt; ()
     * @param <IN> input type
     */
    @FunctionalInterface
    public interface Operator<IN> extends OperatorType {
        /**
         * accept input value.
         * @param input input value.
         */
        void accept(IN input);
    }

    /**
     * function with type * -&gt; ()
     * @param <IN> input type
     */
    @FunctionalInterface
    public interface ExOperator<IN> extends OperatorType {
        /**
         * accept input value.
         * @param input input value.
         * @throws Exception
         */
        void accept(IN input) throws Exception;
    }

    /**
     * convert {@link Functions.ExOperator} to {@link Functions.Operator}.
     * @param exo {@link Functions.ExOperator} instance.
     * @param <I> input type
     * @return {@link Functions.Operator} instance.
     * @throws EvaluatingException - if given operation is null value, an exception will be thrown.
     */
    @NotNull
    @Contract("null -> fail")
    public static <I> Operator<I> operator(ExOperator<? super I> exo) throws EvaluatingException {
        verifyNotNullOperator(exo);
        return i -> {
            try {
                exo.accept(i);
            } catch (Exception e) {
                throw new ExecutingException(e);
            }
        };
    }

    /**
     * check operation is not null value.
     * @param ope - operation instance
     * @param <OPE> operation type
     * @throws EvaluatingException
     */
    @Contract("null -> fail")
    public static  <OPE extends OperatorType> void verifyNotNullOperator(OPE ope) throws EvaluatingException {
        if (ope == null) {
            throw new EvaluatingException("Operator should be non null value.");
        }
    }

    public interface ConditionType {}

    /**
     * function with type * -&gt; *(boolean)
     * @param <SBJ> - subject type
     */
    public interface Condition<SBJ> extends ConditionType {
        /**
         * test subject satisfies condition.
         * @param sbj - a subject to be tested.
         * @return - {@code true} if the subject satisfies the condition. {@code false} if the subject doesn't satisfy the condition.
         */
        boolean test(SBJ sbj);
    }

    /**
     * function with type * -&gt; *(boolean)
     * @param <SBJ> - subject type
     */
    public interface ExCondition<SBJ> extends ConditionType {
        /**
         * test subject satisfies the condition.
         * @param sbj - a subject to be tested.
         * @return - {@code true} if the subject satisfies the condition. {@code false} if the subject doesn't satisfy the condition.
         * @throws Exception
         */
        boolean test(SBJ sbj) throws Exception;
    }

    /**
     * convert {@link javajo.sample.functions.Functions.ExCondition} to {@link javajo.sample.functions.Functions.Condition}.
     * @param exc - {@link javajo.sample.functions.Functions.ExCondition} instance.
     * @param <S> - subject type
     * @return - {@link javajo.sample.functions.Functions.Condition} instance.
     * @throws EvaluatingException
     */
    @NotNull
    @Contract("null -> fail")
    public static <S> Condition<S> condition(ExCondition<? super S> exc) throws EvaluatingException {
        verifyNotNullCondition(exc);
        return s -> {
            try {
                return exc.test(s);
            } catch (Exception e) {
                throw new ExecutingException(e);
            }
        };
    }

    /**
     * check condition is not null value.
     * @param cnd condition instance.
     * @param <CND> condition type
     * @throws EvaluatingException if condition is null value, an exception will be thrown.
     */
    @Contract("null -> fail")
    public static <CND extends ConditionType> void verifyNotNullCondition(CND cnd) throws EvaluatingException {
        if (cnd == null) {
            throw new EvaluatingException("Condition should be non null value.");
        }
    }

    public interface TaskType {}

    @FunctionalInterface
    public interface Task extends TaskType {
        void execute();
    }

    @FunctionalInterface
    public interface ExTask extends TaskType {
        void execute() throws Exception;
    }

    @NotNull
    @Contract("null -> fail")
    public static Task task(ExTask ext) {
        verifyNotNullTask(ext);
        return () -> {
            try {
                ext.execute();
            } catch (Exception e) {
                throw new ExecutingException(e);
            }
        };
    }

    @Contract("null -> fail")
    public static <TSK extends TaskType> void verifyNotNullTask(TSK tsk) throws EvaluatingException {
        if (tsk == null) {
            throw new EvaluatingException("Task should be non null value.");
        }
    }

    @Contract("null -> fail")
    public static void verifyNotNullObject(Object o) throws EvaluatingException {
        if (o == null) {
            throw new EvaluatingException("Object should be non null value.");
        }
    }
}
