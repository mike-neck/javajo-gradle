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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * 分数を表すクラス
 */
public final class Fraction {

    private final long numerator;

    private final long denominator;

    private final boolean negative;

    @Contract("_ -> !null")
    public static Fraction fraction(long num) {
        return new Fraction(num);
    }

    /**
     * 分数型を取得する
     * @param num - 分母
     * @param den - 分子
     * @return - 生成された分数型
     * @throws ArithmeticException 分子に0が渡された場合に例外が発生する
     */
    public static Fraction fraction(long num, long den) throws ArithmeticException {
        if (den == 0) {
            throw new ArithmeticException("Denominator should not be 0.");
        }
        return new Fraction(num, den);
    }

    /**
     * 整数を分数型に変換する
     * @param numerator - 整数値
     */
    private Fraction(long numerator) {
        this.negative = numerator < 0;
        this.numerator = negative ? - numerator : numerator;
        this.denominator = 1;
    }

    /**
     * 分数型を生成する
     * @param numerator - 分母
     * @param denominator - 分子
     */
    private Fraction(long numerator, long denominator) {
        this.negative = (numerator < 0 && denominator > 0) || (numerator > 0 && denominator < 0);
        long left = numerator < 0 ? - numerator : numerator;
        long right = denominator < 0 ? - denominator : denominator;
        long gcd = gcd(left, right);
        this.numerator = left / gcd;
        this.denominator = right / gcd;
    }

    @Contract(pure = true)
    public long getSignedNumerator() {
        return negative ? - numerator : numerator;
    }

    /**
     * マイナス値かどうか調べる
     * @return - マイナス値なら{@code true}、プラス値なら{@code false}
     */
    public boolean isNegative() {
        return negative;
    }

    @NotNull
    @Contract("_ -> !null")
    public Fraction plus(long number) throws IllegalArgumentException {
        return plus(new Fraction(number));
    }

    @NotNull
    @Contract("null -> fail")
    public Fraction plus(Fraction fr) throws IllegalArgumentException {
        if (fr == null) throw new IllegalArgumentException("Null value cannot be added.");
        long leftNumerator = getSignedNumerator() * fr.denominator;
        long rightNumerator = fr.getSignedNumerator() * denominator;
        return new Fraction(leftNumerator + rightNumerator, denominator * fr.denominator);
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        if (denominator == 1) {
            return (negative ? "- " : "") + numerator;
        } else {
            return (negative ? "- " : "") +  numerator + "/" + denominator;
        }
    }

    @Contract("null -> false")
    @Override
    public boolean equals(Object o) {
        if (null == o) return false;
        if (this == o) return true;
        if (!(o instanceof Fraction)) return false;
        Fraction fraction = (Fraction) o;
        return Objects.equals(numerator, fraction.numerator) &&
                Objects.equals(denominator, fraction.denominator) &&
                Objects.equals(negative, fraction.negative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator, negative);
    }

    @FunctionalInterface
    private interface LongBiFunction {
        long apply(long left, long right);
    }

    @FunctionalInterface
    private interface RecursiveLongFunction extends Function<RecursiveLongFunction, LongBiFunction> {}

    /**
     * {@link javajo.sample.Fraction.LongBiFunction} をZコンビネーターで処理する
     * @param lf Zコンビネーター化対象の関数
     * @return Zコンビネーター化した関数
     */
    static LongBiFunction zComb(Function<LongBiFunction, LongBiFunction> lf) {
        Function<RecursiveLongFunction, LongBiFunction> fr = x -> lf.apply((l, r) -> x.apply(x).apply(l, r));
        RecursiveLongFunction rec = x -> lf.apply((l, r) -> x.apply(x).apply(l, r));
        return fr.apply(rec);
    }

    /**
     * 最大公約数を返す
     * @param num - 整数値
     * @param den - 整数値
     * @return 最大公約数
     */
    static long gcd(long num, long den) {
        long left, right;
        if (num < den) {
            left = den;
            right = num;
        } else {
            left = num;
            right = den;
        }
        Function<LongBiFunction, LongBiFunction> lf = f -> (l, r) -> r == 0? l : f.apply(r ,l % r);
        LongBiFunction fun = zComb(lf);
        return fun.apply(left, right);
    }
}
