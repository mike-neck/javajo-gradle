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

import javajo.sample.EvaluatingException;
import javajo.sample.data.MaybeBase.Nothing;
import javajo.sample.data.MaybeBase.Some;
import javajo.sample.functions.Functions.Condition;
import javajo.sample.functions.Functions.Function;
import javajo.sample.functions.Functions.Generator;
import javajo.sample.functions.Functions.Operator;
import javajo.sample.functions.Functions.Task;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

import static javajo.sample.functions.Functions.verifyNotNullObject;

/**
 * データの{@code null}を避けるためのデータコンテナー<br/>
 * このコンテナーのサブクラスには下記の二つがある。
 * <ul>
 *     <li>データを保持する{@link Some}型</li>
 *     <li>データを保持しない{@link Nothing}型</li>
 * </ul>
 * @param <V> 保持されるデータ型
 */
public interface Maybe<V> {

    /**
     * このコンテナーが値を保持する {@link Some}型であるかテストする
     * @return このコンテナーが {@link Some}型の場合{@code true}を返す。このコンテナーが{@link Nothing}型の場合は{@code false}を返す。
     */
    boolean isSome();

    /**
     * このコンテナーが値を保持しない{@link Nothing}型であるかテストする
     * @return {@code false} when this container has non null value. {@code true} when this container has null value.
     */
    default boolean isNothing() {
        return !isSome();
    }

    /**
     * このコンテナーが保持する値を別の値に変換する
     * @param fun - 変換する関数
     * @param <R> - 変換後の値の型
     * @return - 変換されたデータを保持する{@link Maybe}のインスタンス。変換されたの値が{@code null}でない場合は返される値は{@link Some}型。変換された値が{@link null}の場合は{@link Nothing}型。
     */
    <R> Maybe<R> map(Function<? super V, ? extends R> fun);

    /**
     * このデータコンテナーが保持する値を別の値に変換する<br/>
     * なお、変換関数は{@link Maybe}を返すタイプの関数
     * @param fun - {@link Maybe}型に変換する関数
     * @param <R> - 変換後のデータコンテナが保持する値の型
     * @return - 変換されたデータを保持する{@link Maybe}のインスタンス。変換された値が{@code null}でない場合は{@link Some}型。変換された値が{@code null}の場合は{@link Nothing}型。
     */
    <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun);

    /**
     * このデータコンテナーが保持する値を選別する
     * @param cond - 選別する条件
     * @return - 同じ値をデータを保持する{@link Maybe}のインスタンス。条件に合致しない場合、返されるデータ型は{@link Nothing}になる。
     */
    Maybe<V> filter(Condition<? super V> cond);

    /**
     * このデータコンテナーが保持する値を返す。
     * @return - このデータコンテナーが保持する値
     * @throws NoSuchElementException - このデータコンテナーが{@link Nothing}型だった場合に送出される。
     */
    V get() throws NoSuchElementException;

    /**
     * このデータコンテナーが{@link Some}型の場合は保持する値、{@link Nothing}型の場合は指定されたデフォルト値を返す。
     * @param defaultValue - {@link Nothing}型の場合のデフォルト値。{@code null}値は不可。
     * @return - このデータコンテナーが{@link Some}型の場合は保持する値、{@link Nothing}型の場合は指定されたデフォルト値を返す。
     */
    V orDefault(V defaultValue);

    /**
     * このデータコンテナーが{@link Some}型の場合は保持する場合は値を返し、{@link Nothing}型の場合には与えられた生成関数により生成される例外が送出される。
     * @param gen - 非検査例外を生成する{@link Generator}関数。
     * @return - このデータコンテナーが{@link Some}型の場合は保持する場合は値を返し、{@link Nothing}型の場合には与えられた生成関数により生成される例外が送出される。
     */
    V orThrow(Generator<? extends RuntimeException> gen);

    /**
     * 実行時コンテキスト。{@link Nothing}型の場合の動作を記述する
     */
    interface OperatingContext {
        /**
         * データコンテナーが{@link Nothing}型の場合の処理を実行する。
         * @param task - 処理内容
         */
        void orOnNothingDo(Task task);
    }

    /**
     * このデータコンテナーに処理を行わせる実行時コンテキストを生成する。
     * @param op このデータコンテナーが{@link Some}型の場合に実行する処理
     * @return 実行時コンテキスト。
     */
    OperatingContext onSomeDo(Operator<? super V> op);

    /**
     * このデータコンテナーが{@link Some}型の場合に保持する値をもとに副作用を発生させる
     * @param op このデータコンテナーが保持する値を元にする副作用処理。
     */
    void whenSome(Operator<? super V> op);

    /**
     * {@link Nothing}型のインスタンスを生成する。
     * @param <T> データ型
     * @return {@link Nothing}型の{@link Maybe}のインスタンス
     */
    @NotNull
    static <T> Maybe<T> nothing() {
        return new Nothing<>();
    }

    /**
     * {@link Some}型の{@link Maybe}のインスタンスを生成する。
     * @param value 生成されるデータコンテナーが保持する値。{@code null}を指定した場合は例外が送出される。
     * @param <T> 保持する値の型
     * @return 渡された値を保持する {@link Some}型の{@link Maybe}のインスタンス。
     * @throws EvaluatingException - 渡されたデータが{@code null}の場合に送出される。
     */
    @Contract("null -> fail")
    @NotNull
    static <T> Maybe<T> some(T value) throws EvaluatingException {
        verifyNotNullObject(value);
        return new Some<>(value);
    }

    /**
     * {@link Maybe}のインスタンスを生成する。渡された値が{@code null}の場合は{@link Nothing}型のインスタンスが返され、{@code null}でない場合は{@link Some}型のインスタンスが返される。
     * @param value - データコンテナーに収める値
     * @param <T> 値の型
     * @return {@link Maybe}のインスタンス。
     */
    @NotNull
    static <T> Maybe<T> maybe(T value) {
        if (value == null) {
            return new Nothing<>();
        } else {
            return new Some<>(value);
        }
    }
}
