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

package org.apache.lucene.morphology.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.morphology.LetterDecoderEncoder;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class MorphologyAnalyzer extends Analyzer {
    private LuceneMorphology luceneMorph;

    public MorphologyAnalyzer(LuceneMorphology luceneMorph) {
        this.luceneMorph = luceneMorph;
    }

    public MorphologyAnalyzer(String pathToMorph, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(pathToMorph, letterDecoderEncoder);
    }

    public MorphologyAnalyzer(InputStream inputStream, LetterDecoderEncoder letterDecoderEncoder) throws IOException {
        luceneMorph = new LuceneMorphology(inputStream, letterDecoderEncoder);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_46, reader);
        TokenStream tokenStream = tokenizer;
        tokenStream = new StandardFilter(Version.LUCENE_46,tokenStream);
        tokenStream = new LowerCaseFilter(Version.LUCENE_46,tokenStream);
        tokenStream = new MorphologyFilter(tokenStream, luceneMorph);
        return new TokenStreamComponents(tokenizer, tokenStream);
    }

}
