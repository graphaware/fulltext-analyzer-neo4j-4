package com.graphaware.fulltext.analyzers;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.phonetic.DoubleMetaphoneFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.neo4j.annotations.service.ServiceProvider;
import org.neo4j.graphdb.schema.AnalyzerProvider;

@ServiceProvider
public class PhoneticAnalyzer extends AnalyzerProvider {


	public static final int MAX_CODE_LENGTH = 6;

	public PhoneticAnalyzer() {
		super("phonetic");
	}

	@Override
	public String description() {
		return "Phonetic analyzer using the DoubleMetaphoneFilter";
	}

	@Override
	public Analyzer createAnalyzer() {
		return new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String s) {
				Tokenizer tokenizer = new StandardTokenizer();
				TokenStream stream = new DoubleMetaphoneFilter(tokenizer, MAX_CODE_LENGTH, true);
				return new TokenStreamComponents(tokenizer, stream);
			}
		};
	}
}
