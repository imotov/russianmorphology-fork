/**
 * Copyright 2009 Alexander Kuznetsov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.morphology.english;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

public class EnglishAnalyzerTest extends BaseTokenStreamTestCase {

    @Test
    public void testPositionIncrement() throws IOException {
        EnglishAnalyzer englishAnalyzer = new EnglishAnalyzer();
        assertTokenStreamContents(
                englishAnalyzer.tokenStream("test", "There are tests!"),
                new String[]{"there", "are", "be", "test"},
                new int[]{0, 6, 6, 10},
                new int[]{5, 9, 9, 15},
                new String[]{"<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>"},
                new int[]{1, 1, 0, 1}
        );
    }

    @Test
    public void testEmptyString() throws IOException {
        EnglishAnalyzer englishAnalyzer = new EnglishAnalyzer();

        assertSimpleTSOutput(
                englishAnalyzer.tokenStream("test", "Some text with t and lng"),
                new String[]{"some", "text", "with", "t", "and", "lng"}
        );
    }

    public static void assertSimpleTSOutput(TokenStream stream, String[] expected) throws IOException {
        stream.reset();
        CharTermAttribute termAttr = stream.getAttribute(CharTermAttribute.class);
        Assert.assertNotNull(termAttr);
        int i = 0;
        while (stream.incrementToken()) {
            Assert.assertTrue("got extra term: " + termAttr.toString(), i < expected.length);
            Assert.assertEquals("expected different term at index " + i, expected[i], termAttr.toString());
            i++;
        }
        Assert.assertEquals("not all tokens produced", expected.length, i);
    }
}
