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

import org.junit.Test;

import static javajo.sample.Fraction.fraction;
import static javajo.sample.Fraction.gcd;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FractionTest {

    @Test
    public void gcdに3と1を渡すと1が帰ってくる() {
        long gcd = gcd(3, 1);
        assertThat(gcd, is(1L));
    }

    @Test
    public void gcdに4と0を渡すと4が帰ってくる() {
        long gcd = gcd(4, 0);
        assertThat(gcd, is(4L));
    }

    @Test
    public void gcdに784と840を渡すと56が帰ってくる() {
        long gcd = gcd(784, 840);
        assertThat(gcd, is(56L));
    }

    @Test
    public void fractionにマイナス1を渡すとネガティブ() {
        Fraction fraction = fraction(-1);
        assertTrue(fraction.isNegative());
    }

    @Test
    public void fractionに1を渡すとポジティブ() {
        Fraction fraction = fraction(1);
        assertFalse(fraction.isNegative());
    }

    @Test
    public void fractionにマイナス1とマイナス3を渡すとポジティブ() {
        Fraction fraction = fraction(-1, -3);
        assertFalse(fraction.isNegative());
    }

    @Test
    public void fractionに2と5を渡すとポジティブ() {
        Fraction fraction = fraction(2, 5);
        assertFalse(fraction.isNegative());
    }

    @Test
    public void fractionにマイナス6と9を渡すとネガティブ() {
        Fraction fraction = fraction(-6, 9);
        assertTrue(fraction.isNegative());
    }

    @Test
    public void fractionに28とマイナス44を渡すとネガティブ() {
        Fraction fraction = fraction(28, -44);
        assertTrue(fraction.isNegative());
    }

    @Test
    public void 分数値3分の5と6分の10は同じ値() {
        Fraction left = fraction(5, 3);
        Fraction right = fraction(10, 6);
        assertThat(left, is(right));
    }

    @Test
    public void 分数値3分のマイナス5とマイナス6分の10は同じ値() {
        Fraction left = fraction(-5, 3);
        Fraction right = fraction(10, -6);
        assertThat(left, is(right));
    }
}
