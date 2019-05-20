package com.orange.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SearchDocuemnt {
	
	public static void main(String[] arg) throws IOException {
		SearchDocuemnt document = new SearchDocuemnt();
		document.searchDocument("D:/lucene");
	}
	
	public void searchDocument(String indexDirectory) throws IOException {
		Directory directory = FSDirectory.open(new File(indexDirectory).toPath());
		
		IndexReader indexReader = DirectoryReader.open(directory);
		
		IndexSearcher indexSearch = new IndexSearcher(indexReader);
		
		TermQuery query = new TermQuery(new Term("content","error"));
		
		TopDocs topDos = indexSearch.search(query, 5);
		
		System.out.println("总共的查询结果：" + topDos.totalHits);
		
		ScoreDoc[] scoreDocArr = topDos.scoreDocs;
		
		for(ScoreDoc scoreDoce : scoreDocArr) {
			int docId = scoreDoce.doc;
			
			float score = scoreDoce.score;
			
			System.out.println(scoreDoce);
			
			Document document = indexSearch.doc(docId);
			System.out.println(document.get("name"));
			System.out.println("=============================");
		}
		
		indexReader.close();
		
	}
	
}
