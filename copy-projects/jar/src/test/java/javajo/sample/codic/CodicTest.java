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
package javajo.sample.codic;

import javajo.sample.codic.model.Translation;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CodicTest {

    @Test
    public void 取得でacquireに翻訳される() {
        Codic codic = new Codic();
        List<Translation> actual = codic.translate("取得");
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getTranslatedText())
                .isEqualTo("acquisition");
    }
}
