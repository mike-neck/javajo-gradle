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
package javajo.sample.codic.model;

import com.owlike.genson.annotation.JsonProperty;

import java.util.List;
import java.util.StringJoiner;

public final class Word {

    private boolean successful;

    private String text;

    @JsonProperty("translated_text")
    private String translatedText;

    private List<Candidates> candidates;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public List<Candidates> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidates> candidates) {
        this.candidates = candidates;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Word:[", "]")
                .add("successful: [" + (successful) + "]")
                .add("text: [" + (text == null ? "null" : text) + "]")
                .add("translatedText: [" + (translatedText == null ? "null" : translatedText) + "]")
                .add("candidates: [" + (candidates == null ? "null" : candidates) + "]")
                .toString();
    }
}
