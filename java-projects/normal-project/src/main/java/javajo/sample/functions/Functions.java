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

    /**
     * 関数型の基底型
     */
    public interface FunctionType{}

    /**
     * 関数型<br/>
     * IN型の入力からOUT型の値への変換を行う関数
     * @param <IN> 入力の型
     * @param <OUT> 出力の型
     */
    @FunctionalInterface
    public interface Function<IN, OUT> extends FunctionType {
        /**
         * IN型のパラメーターを一つ取り、OUT型の値に変換して返すメソッド
         * @param input - IN型のパラメーター
         * @return - OUT型の値
         */
        OUT apply(IN input);
    }

    /**
     * 例外許容関数型<br/>
     * IN型の入力からOUT型の値への変換を行う関数で例外が発生するタイプの関数
     * @param <IN> 入力の型
     * @param <OUT> 出力の型
     */
    @FunctionalInterface
    public interface ExFunction<IN, OUT> extends FunctionType {
        /**
         * IN型のパラメーターを一つ取り、OUT型の値に変換して返すメソッド
         * @param input - IN型のパラメーター
         * @return - OUT型の値
         * @throws Exception IN型からOUT型に変換するメソッドで宣言されている検査例外
         */
        OUT apply(IN input) throws Exception;
    }

    /**
     * {@link Functions.ExFunction}を{@link Functions.Function}に変換する関数
     * @param exf - {@link Functions.ExFunction} 例外許容関数。{@code null}は許されない。
     * @param <I> - 関数の入力型
     * @param <O> - 関数の出力型
     * @return - {@link Functions.Function} ラップされた関数。
     * @throws EvaluatingException 渡された関数が{@code null}だった場合に送出される。
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
     * 関数が{@code null}でないことをチェックする。{@code null}の場合、{@link EvaluatingException}が送出される。
     * @param fun - 関数
     * @param <FUN> - 関数型
     * @throws EvaluatingException パラメーターが{@code null}の場合送出される。
     */
    @Contract("null -> fail")
    public static <FUN extends FunctionType> void verifyNotNullFunction(FUN fun) throws EvaluatingException {
        if (fun == null) {
            throw new EvaluatingException("Function should be non null value.");
        }
    }

    /**
     * 生成型の基底型
     */
    public interface GeneratorType {}

    /**
     * 生成型<br/>
     * O型の値を生成する。
     * @param <O> 生成する値の型
     */
    @FunctionalInterface
    public interface Generator<O> extends GeneratorType {
        /**
         * 値を生成する
         * @return - 生成されたO型の値
         */
        O get();
    }

    /**
     * 例外許容生成型<br/>
     * 生成時に検査例外が発生する生成型
     * @param <O> 生成される値の型
     */
    public interface ExGenerator<O> extends GeneratorType {
        /**
         * 値を生成する
         * @return - 生成されたO型の値
         * @throws Exception
         */
        O get() throws Exception;
    }

    /**
     * {@link Functions.ExGenerator}から{@link Functions.Generator}に変換する関数
     * @param exg 例外許容生成型
     * @param <O> 生成型
     * @return ラップされた生成型
     * @throws EvaluatingException - パラメーターに{@code null}が渡された場合に送出する。
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
     * パラメーターの生成型が{@code null}かどうか検査する。
     * @param gen - 生成型の値
     * @param <OBJ> - 生成型
     * @throws EvaluatingException {@code null}が渡された場合に送出される。
     */
    @Contract("null -> fail")
    public static <OBJ extends GeneratorType> void verifyNonNullGenerator(OBJ gen) throws EvaluatingException {
        if (gen == null) {
            throw new EvaluatingException("Generator should be non null value.");
        }
    }

    /**
     * 処理型の基底型
     */
    public interface OperatorType {}

    /**
     * 処理型<br/>
     * IN型の入力値を受け取り、副作用を発生させる。
     * @param <IN> 入力の型
     */
    @FunctionalInterface
    public interface Operator<IN> extends OperatorType {
        /**
         * IN型の入力値を受け取り副作用を発生させる
         * @param input 入力値。
         */
        void accept(IN input);
    }

    /**
     * 例外許容処理型<br/>
     * IN型の入力値を受け取り、副作用を発生させる。
     * @param <IN> 入力の型
     */
    @FunctionalInterface
    public interface ExOperator<IN> extends OperatorType {
        /**
         * IN型の入力値を受け取り副作用を発生させる。
         * @param input 入力値
         * @throws Exception 副作用発生させるメソッドで宣言されている検査例外
         */
        void accept(IN input) throws Exception;
    }

    /**
     * {@link Functions.ExOperator}を{@link Functions.Operator}に変換する関数
     * @param exo 例外許容処理型の値
     * @param <I> 入力値の型
     * @return ラップされた処理型
     * @throws EvaluatingException - 例外許容処理型が{@code null}の場合に送出される。
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
     * 処理型が{@code null}であるか検査する
     * @param ope - 処理型の値
     * @param <OPE> 処理型
     * @throws EvaluatingException パラメーターの処理型が{@code null}の場合に送出される。
     */
    @Contract("null -> fail")
    public static  <OPE extends OperatorType> void verifyNotNullOperator(OPE ope) throws EvaluatingException {
        if (ope == null) {
            throw new EvaluatingException("Operator should be non null value.");
        }
    }

    /**
     * 条件型
     */
    public interface ConditionType {}

    /**
     * 条件型<br/>
     * SBJ型の入力を受け取り条件を満たすかテストする
     * @param <SBJ> - 入力値の型
     */
    public interface Condition<SBJ> extends ConditionType {
        /**
         * 入力された値が条件を満たすかテストする
         * @param sbj - SBJ型の入力値
         * @return - 条件をみたす場合は{@code true}を返す。条件を満たさない場合は{@code false}を返す。
         */
        boolean test(SBJ sbj);
    }

    /**
     * 例外許容条件型<br/>
     * SBJ型の入力を受け取り条件をみたすかテストする
     * @param <SBJ> - 入力値の型
     */
    public interface ExCondition<SBJ> extends ConditionType {
        /**
         * 入力された値が条件を満たすかテストする
         * @param sbj - SBJ型の入力値
         * @return - 条件をみたす場合は{@code true}を返す。条件を満たさない場合は{@code false}を返す。
         * @throws Exception テストの過程で利用されるメソッドが宣言している検査例外
         */
        boolean test(SBJ sbj) throws Exception;
    }

    /**
     * {@link javajo.sample.functions.Functions.ExCondition}を{@link javajo.sample.functions.Functions.Condition}に変換する
     * @param exc - 例外許容条件型
     * @param <S> - テスト対象の型
     * @return - ラップされた{@link javajo.sample.functions.Functions.Condition}
     * @throws EvaluatingException - 入力された条件型が{@code null}の場合に送出される。
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
     * 入力された条件型が{@code null}であるか検査する。
     * @param cnd 条件型の値
     * @param <CND> 条件型
     * @throws EvaluatingException 入力された条件型の値が{@code null}の場合に送出される。
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
